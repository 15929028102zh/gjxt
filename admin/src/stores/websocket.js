import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { useUserStore } from './user'
import { getFullApiUrl } from '@/config/api'

// 重连退避参数(防止连接失败时反复调用 /api/ws/info 给 nginx worker_connections 雪崩)
const MAX_RECONNECT_ATTEMPTS = 10
const BASE_RECONNECT_DELAY_MS = 3000
const MAX_RECONNECT_DELAY_MS = 60000

export const useWebSocketStore = defineStore('websocket', () => {
  // WebSocket连接状态
  const isConnected = ref(false)
  const isConnecting = ref(false)

  // STOMP客户端实例
  const stompClient = ref(null)

  // 通知列表
  const notifications = ref([])

  // 记录状态变更事件订阅者列表(列表页可注册回调以自动刷新)
  const recordEventSubscribers = ref([])

  // 重连控制
  const reconnectAttempts = ref(0)
  let reconnectTimer = null  // 单一计时器,避免多重试
  let manualDisconnect = false  // 主动断开标志位

  // 未读通知数量
  const unreadCount = computed(() => {
    return notifications.value.filter(n => !n.isRead).length
  })

  // 计算下次重连延迟:指数退避 + 抖动,防止 thundering herd
  const computeReconnectDelay = () => {
    const exp = Math.min(reconnectAttempts.value, 6)
    const base = Math.min(BASE_RECONNECT_DELAY_MS * Math.pow(2, exp), MAX_RECONNECT_DELAY_MS)
    // 抖动 ±30%,避免多客户端同时重连
    const jitter = base * 0.3 * (Math.random() * 2 - 1)
    return Math.max(1000, base + jitter)
  }

  const clearReconnectTimer = () => {
    if (reconnectTimer) {
      clearTimeout(reconnectTimer)
      reconnectTimer = null
    }
  }

  const scheduleReconnect = (userStore) => {
    if (manualDisconnect) return
    if (!userStore?.userInfo?.id) return
    if (reconnectAttempts.value >= MAX_RECONNECT_ATTEMPTS) {
      console.warn(`[WS] 已达最大重连次数 ${MAX_RECONNECT_ATTEMPTS},停止重试,请刷新页面或检查网络`)
      // 清理 stompClient,避免连接僵死占用 nginx / 后端 fd
      if (stompClient.value) {
        try {
          stompClient.value.deactivate()
        } catch (e) { /* ignore */ }
        stompClient.value = null
      }
      isConnecting.value = false
      return
    }
    clearReconnectTimer()
    const delay = Math.round(computeReconnectDelay())
    reconnectAttempts.value += 1
    console.log(`[WS] 第 ${reconnectAttempts.value}/${MAX_RECONNECT_ATTEMPTS} 次重连,等待 ${delay}ms`)
    reconnectTimer = setTimeout(() => {
      reconnectTimer = null
      connect()
    }, delay)
  }

  // 连接WebSocket
  const connect = () => {
    const userStore = useUserStore()

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

    manualDisconnect = false
    isConnecting.value = true
    console.log('[WS] 开始连接...')

    try {
      // 关键:transports:['websocket'] 让 SockJS 只走 WebSocket 通道,
      // 不再 fallback 到 xhr-streaming/xhr-polling,从而避免反复 /api/ws/info 探测
      const client = new Client({
        webSocketFactory: () => new SockJS(getFullApiUrl('/api/ws'), null, {
          transports: ['websocket']
        }),
        connectHeaders: {
          Authorization: `Bearer ${localStorage.getItem('token') || ''}`
        },
        debug: () => {},  // 关闭 stompjs 内置 debug 日志,降低控制台噪音
        reconnectDelay: 0,  // 禁用 stompjs 内置重连,改由我们自己统一控制(避免多重试)
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        onConnect: () => {
          console.log('[WS] 连接成功')
          isConnected.value = true
          isConnecting.value = false
          reconnectAttempts.value = 0  // 重连成功,重置计数
          clearReconnectTimer()

          // 订阅用户专属通知频道
          client.subscribe('/user/queue/notifications', (message) => {
            try {
              const notification = JSON.parse(message.body)
              addNotification(notification)
              showNotificationPopup(notification)
              playNotificationSound()
            } catch (error) {
              console.error('[WS] 处理用户通知消息失败:', error)
            }
          })

          // 订阅广播通知频道
          client.subscribe('/topic/notifications', (message) => {
            try {
              const notification = JSON.parse(message.body)
              addNotification(notification)
              showNotificationPopup(notification)
              playNotificationSound()
            } catch (error) {
              console.error('[WS] 处理广播通知消息失败:', error)
            }
          })

          // 订阅记录状态变更广播(用于列表页自动刷新)
          client.subscribe('/topic/record-events', (message) => {
            try {
              const event = JSON.parse(message.body)
              recordEventSubscribers.value.forEach(cb => {
                try { cb(event) } catch (e) { /* 忽略订阅者错误 */ }
              })
            } catch (error) {
              console.error('[WS] 处理记录状态变更事件失败:', error)
            }
          })
        },
        onStompError: (frame) => {
          console.error('[WS] STOMP 错误:', frame.headers?.['message'])
          isConnected.value = false
          isConnecting.value = false
          // P0-Fix: STOMP ERROR 帧不会自动触发 onWebSocketClose,
          // 必须主动 deactivate + scheduleReconnect,否则连接僵死
          try {
            client.deactivate()
          } catch (e) { /* ignore */ }
          scheduleReconnect(userStore)
        },
        onWebSocketClose: (event) => {
          if (manualDisconnect) return  // 主动断开,不重连
          console.log('[WS] 连接关闭:', event?.code)
          isConnected.value = false
          isConnecting.value = false
          scheduleReconnect(userStore)
        },
        onWebSocketError: () => {
          if (manualDisconnect) return  // 主动断开,不重连
          console.warn('[WS] 连接错误,准备退避重连')
          isConnected.value = false
          isConnecting.value = false
          scheduleReconnect(userStore)
        }
      })

      stompClient.value = client
      client.activate()

    } catch (error) {
      console.error('[WS] 创建客户端失败:', error)
      isConnecting.value = false
      scheduleReconnect(userStore)
    }
  }

  // 断开WebSocket连接
  const disconnect = () => {
    manualDisconnect = true
    clearReconnectTimer()
    reconnectAttempts.value = 0
    if (stompClient.value) {
      try {
        stompClient.value.deactivate()
      } catch (e) { /* ignore */ }
      stompClient.value = null
    }
    isConnected.value = false
    isConnecting.value = false
  }

  // 手动重置重连计数器(用于用户刷新页面或主动重试按钮)
  const resetReconnect = () => {
    reconnectAttempts.value = 0
    clearReconnectTimer()
    manualDisconnect = false
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

    // 限制通知列表长度,保留最新的100条
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
      // 创建音频上下文(如果需要用户交互才能播放)
      const audioContext = new (window.AudioContext || window.webkitAudioContext)()

      // 创建一个简单的提示音(使用Web Audio API生成)
      const oscillator = audioContext.createOscillator()
      const gainNode = audioContext.createGain()

      // 连接音频节点
      oscillator.connect(gainNode)
      gainNode.connect(audioContext.destination)

      // 设置音频参数
      oscillator.frequency.setValueAtTime(800, audioContext.currentTime) // 800Hz频率
      oscillator.type = 'sine' // 正弦波

      // 设置音量包络(淡入淡出效果)
      gainNode.gain.setValueAtTime(0, audioContext.currentTime)
      gainNode.gain.linearRampToValueAtTime(0.3, audioContext.currentTime + 0.1)
      gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.5)

      // 播放音频
      oscillator.start(audioContext.currentTime)
      oscillator.stop(audioContext.currentTime + 0.5)

      console.log('播放通知提示音')
    } catch (error) {
      console.warn('播放提示音失败:', error)
      // 如果Web Audio API失败,尝试使用HTML5 Audio
      try {
        // 创建一个简单的音频数据URL(短促的beep声)
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
        // 调用API标记为已读(不需要传递userId,后端从token解析)
        const { markNotificationAsRead } = await import('@/api/notification')
        await markNotificationAsRead(notificationId)

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
    if (index > -1) {
      try {
        // 调用API删除通知(不需要传递userId,后端从token解析)
        const { deleteNotification: deleteNotificationApi } = await import('@/api/notification')
        await deleteNotificationApi(notificationId)

        // 删除本地通知
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

  // 获取通知列表(从服务器)
  const fetchNotifications = async () => {
    try {
      // 调用API获取通知列表(不需要传递userId,后端从token解析)
      const { getUserNotifications } = await import('@/api/notification')
      const response = await getUserNotifications()

      if (response && Array.isArray(response)) {
        notifications.value = response
      }
    } catch (error) {
      console.error('获取通知列表失败:', error)
    }
  }

  // 获取用户的月检任务通知列表(已通知状态的月检任务)
  const fetchTaskNotifications = async () => {
    try {
      // 调用API获取月检任务通知列表(不需要传递userId,后端从token解析)
      const { getTaskNotifications } = await import('@/api/notification')
      const response = await getTaskNotifications()

      if (response && Array.isArray(response)) {
        return response
      }
      return []
    } catch (error) {
      console.error('获取月检任务通知列表失败:', error)
      return []
    }
  }

  // 注册记录状态变更事件,返回反注册函数
  const subscribeRecordEvents = (callback) => {
    recordEventSubscribers.value.push(callback)
    return () => {
      const idx = recordEventSubscribers.value.indexOf(callback)
      if (idx > -1) recordEventSubscribers.value.splice(idx, 1)
    }
  }

  return {
    // 状态
    isConnected,
    isConnecting,
    reconnectAttempts,
    notifications,
    unreadCount,

    // 方法
    connect,
    disconnect,
    resetReconnect,
    addNotification,
    markAsRead,
    deleteNotification,
    clearAllNotifications,
    fetchNotifications,
    fetchTaskNotifications,
    subscribeRecordEvents
  }
})