import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { useUserStore } from './user'
import { getFullApiUrl } from '@/config/api'

export const useWebSocketStore = defineStore('websocket', () => {
  // WebSocket连接状态
  const isConnected = ref(false)
  const isConnecting = ref(false)
  
  // STOMP客户端实例
  const stompClient = ref(null)
  
  // 通知列表
  const notifications = ref([])
  
  // 未读通知数量
  const unreadCount = computed(() => {
    return notifications.value.filter(n => !n.isRead).length
  })
  
  // 连接WebSocket
  const connect = () => {
    const userStore = useUserStore()
    
    console.log('WebSocket连接尝试 - 用户信息:', userStore.userInfo)
    console.log('WebSocket连接状态 - isConnected:', isConnected.value, 'isConnecting:', isConnecting.value)
    
    if (!userStore.userInfo) {
      console.log('WebSocket连接失败: 用户信息不存在')
      return
    }
    
    if (!userStore.userInfo.id) {
      console.log('WebSocket连接失败: 用户ID不存在')
      return
    }
    
    if (isConnected.value || isConnecting.value) {
      console.log('WebSocket连接跳过: 已连接或正在连接中')
      return
    }
    
    console.log('开始WebSocket连接...')
    isConnecting.value = true
    
    try {
      // 创建STOMP客户端
      const client = new Client({
        // 使用统一的API配置构建SockJS地址，兼容开发/生产环境
        webSocketFactory: () => new SockJS(getFullApiUrl('/api/ws')),
        connectHeaders: {
          // 可以添加认证头
          Authorization: `Bearer ${localStorage.getItem('token') || ''}`
        },
        debug: (str) => {
          console.log('STOMP Debug:', str)
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        onConnect: (frame) => {
          console.log('WebSocket连接成功:', frame)
          isConnected.value = true
          isConnecting.value = false
          
          // 订阅用户专属通知频道
          // Spring推荐客户端订阅 /user/queue/**，服务端使用 convertAndSendToUser
          client.subscribe('/user/queue/notifications', (message) => {
            try {
              const notification = JSON.parse(message.body)
              console.log('收到用户通知:', notification)
              
              // 添加到通知列表
              addNotification(notification)
              
              // 显示弹框通知和播放提示音
              showNotificationPopup(notification)
              playNotificationSound()
            } catch (error) {
              console.error('处理用户通知消息失败:', error)
            }
          })
          
          // 订阅广播通知频道
          client.subscribe('/topic/notifications', (message) => {
            try {
              const notification = JSON.parse(message.body)
              console.log('收到广播通知:', notification)
              
              // 添加到通知列表
              addNotification(notification)
              
              // 显示弹框通知和播放提示音
              showNotificationPopup(notification)
              playNotificationSound()
            } catch (error) {
              console.error('处理广播通知消息失败:', error)
            }
          })
        },
        onStompError: (frame) => {
          console.error('STOMP错误:', frame.headers['message'])
          console.error('详细信息:', frame.body)
          isConnected.value = false
          isConnecting.value = false
        },
        onWebSocketClose: (event) => {
          console.log('WebSocket连接关闭:', event)
          isConnected.value = false
          isConnecting.value = false
          
          // 自动重连（5秒后）
          if (userStore.userInfo) {
            console.log('WebSocket连接断开，5秒后尝试重连...')
            setTimeout(() => {
              if (!isConnected.value && !isConnecting.value && userStore.userInfo) {
                console.log('开始自动重连WebSocket...')
                connect()
              }
            }, 5000)
          }
        },
        onWebSocketError: (event) => {
          console.error('WebSocket错误:', event)
          isConnected.value = false
          isConnecting.value = false
          
          // 自动重连（3秒后）
          if (userStore.userInfo) {
            console.log('WebSocket连接错误，3秒后尝试重连...')
            setTimeout(() => {
              if (!isConnected.value && !isConnecting.value && userStore.userInfo) {
                console.log('开始自动重连WebSocket...')
                connect()
              }
            }, 3000)
          }
        }
      })
      
      stompClient.value = client
      client.activate()
      
    } catch (error) {
      console.error('WebSocket连接失败:', error)
      isConnecting.value = false
    }
  }
  
  // 断开WebSocket连接
  const disconnect = () => {
    if (stompClient.value) {
      stompClient.value.deactivate()
      stompClient.value = null
    }
    isConnected.value = false
    isConnecting.value = false
  }
  
  // 添加通知到列表
  const addNotification = (notification) => {
    // 检查是否已存在相同ID的通知
    const existingIndex = notifications.value.findIndex(n => n.id === notification.id)
    if (existingIndex >= 0) {
      // 更新现有通知
      notifications.value[existingIndex] = notification
    } else {
      // 添加新通知到列表开头
      notifications.value.unshift(notification)
    }
    
    // 限制通知列表长度，保留最新的100条
    if (notifications.value.length > 100) {
      notifications.value = notifications.value.slice(0, 100)
    }
  }
  
  // 显示弹框通知
  const showNotificationPopup = (notification) => {
    // 使用Element Plus的通知组件
    if (window.ElNotification) {
      window.ElNotification({
        title: notification.title || '新通知',
        message: notification.content,
        type: notification.priority === 'high' ? 'warning' : 'info',
        duration: 5000,
        showClose: true,
        onClick: () => {
          // 点击通知时的处理逻辑
          if (notification.actionUrl) {
            // 跳转到相关页面
            window.$router?.push(notification.actionUrl)
          }
          // 标记为已读
          markAsRead(notification.id)
        }
      })
    }
  }

  // 播放通知提示音
  const playNotificationSound = () => {
    try {
      // 创建音频上下文（如果需要用户交互才能播放）
      const audioContext = new (window.AudioContext || window.webkitAudioContext)()
      
      // 创建一个简单的提示音（使用Web Audio API生成）
      const oscillator = audioContext.createOscillator()
      const gainNode = audioContext.createGain()
      
      // 连接音频节点
      oscillator.connect(gainNode)
      gainNode.connect(audioContext.destination)
      
      // 设置音频参数
      oscillator.frequency.setValueAtTime(800, audioContext.currentTime) // 800Hz频率
      oscillator.type = 'sine' // 正弦波
      
      // 设置音量包络（淡入淡出效果）
      gainNode.gain.setValueAtTime(0, audioContext.currentTime)
      gainNode.gain.linearRampToValueAtTime(0.3, audioContext.currentTime + 0.1)
      gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.5)
      
      // 播放音频
      oscillator.start(audioContext.currentTime)
      oscillator.stop(audioContext.currentTime + 0.5)
      
      console.log('播放通知提示音')
    } catch (error) {
      console.warn('播放提示音失败:', error)
      // 如果Web Audio API失败，尝试使用HTML5 Audio
      try {
        // 创建一个简单的音频数据URL（短促的beep声）
        const audioData = 'data:audio/wav;base64,UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTYIG2m98OScTgwOUarm7blmGgU7k9n1unEiBC13yO/eizEIHWq+8+OWT'
        const audio = new Audio(audioData)
        audio.volume = 0.3
        audio.play().catch(e => console.warn('HTML5 Audio播放失败:', e))
      } catch (fallbackError) {
        console.warn('备用音频播放也失败:', fallbackError)
      }
    }
  }
  
  // 标记通知为已读
  const markAsRead = async (notificationId) => {
    const notification = notifications.value.find(n => n.id === notificationId)
    if (notification && !notification.isRead) {
      try {
        const userStore = useUserStore()
        if (!userStore.userInfo) return
        
        // 调用API标记为已读
        const { markNotificationAsRead } = await import('@/api/notification')
        await markNotificationAsRead(userStore.userInfo.id, notificationId)
        
        // 更新本地状态
        notification.isRead = true
      } catch (error) {
        console.error('标记通知为已读失败:', error)
        throw error
      }
    }
  }
  
  // 删除通知
  const deleteNotification = async (notificationId) => {
    const index = notifications.value.findIndex(n => n.id === notificationId)
    if (index >= 0) {
      try {
        const userStore = useUserStore()
        if (!userStore.userInfo) return
        
        // 调用API删除通知
        const { deleteNotification: deleteNotificationApi } = await import('@/api/notification')
        await deleteNotificationApi(userStore.userInfo.id, notificationId)
        
        // 更新本地状态
        notifications.value.splice(index, 1)
      } catch (error) {
        console.error('删除通知失败:', error)
        throw error
      }
    }
  }
  
  // 清空所有通知
  const clearAllNotifications = () => {
    notifications.value = []
  }
  
  // 获取用户通知列表（从服务器）
  const fetchNotifications = async () => {
    try {
      const userStore = useUserStore()
      if (!userStore.userInfo) return
      
      // 调用API获取通知列表
      const { getUserNotifications } = await import('@/api/notification')
      const response = await getUserNotifications(userStore.userInfo.id)
      
      if (response && Array.isArray(response)) {
        notifications.value = response
      }
    } catch (error) {
      console.error('获取通知列表失败:', error)
    }
  }

  // 获取用户的月检任务通知列表（已通知状态的月检任务）
  const fetchTaskNotifications = async () => {
    try {
      const userStore = useUserStore()
      if (!userStore.userInfo) return []
      
      // 调用API获取月检任务通知列表
      const { getTaskNotifications } = await import('@/api/notification')
      const response = await getTaskNotifications(userStore.userInfo.id)
      
      if (response && Array.isArray(response)) {
        return response
      }
      return []
    } catch (error) {
      console.error('获取月检任务通知列表失败:', error)
      return []
    }
  }
  
  return {
    // 状态
    isConnected,
    isConnecting,
    notifications,
    unreadCount,
    
    // 方法
    connect,
    disconnect,
    addNotification,
    markAsRead,
    deleteNotification,
    clearAllNotifications,
    fetchNotifications,
    fetchTaskNotifications
  }
})