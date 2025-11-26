<template>
  <div class="notification-popup-container">
    <!-- 通知弹框 -->
    <transition-group name="notification" tag="div" class="notification-wrapper">
      <div
        v-for="notification in visibleNotifications"
        :key="notification.id"
        class="notification-popup"
        :class="notification.type || 'info'"
        @click="handleNotificationClick(notification)"
      >
        <div class="notification-icon">
          <el-icon v-if="notification.type === 'success'"><Check /></el-icon>
          <el-icon v-else-if="notification.type === 'warning'"><Warning /></el-icon>
          <el-icon v-else-if="notification.type === 'error'"><Close /></el-icon>
          <el-icon v-else><Bell /></el-icon>
        </div>
        
        <div class="notification-content">
          <div class="notification-title">{{ notification.title }}</div>
          <div class="notification-message">{{ notification.content }}</div>
          <div class="notification-time">{{ formatTime(notification.createTime) }}</div>
        </div>
        
        <div class="notification-actions">
          <el-button 
            type="text" 
            size="small" 
            @click.stop="markAsRead(notification)"
            v-if="!notification.isRead"
          >
            标记已读
          </el-button>
          <el-button 
            type="text" 
            size="small" 
            @click.stop="closeNotification(notification)"
          >
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
      </div>
    </transition-group>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { ElIcon, ElButton } from 'element-plus'
import { Bell, Check, Warning, Close } from '@element-plus/icons-vue'
import { useWebSocketStore } from '@/stores/websocket'
import { useRouter } from 'vue-router'

const webSocketStore = useWebSocketStore()
const router = useRouter()

// 显示的通知列表（最多显示5个）
const visibleNotifications = ref([])
const maxVisible = 5
const autoHideDelay = 5000 // 5秒后自动隐藏

// 监听WebSocket store中的新通知
let unsubscribe = null

onMounted(() => {
  // 监听新通知
  unsubscribe = webSocketStore.$subscribe((mutation, state) => {
    if (
      mutation.type === 'direct' &&
      Array.isArray(mutation.events) &&
      mutation.events.some(e => e.key === 'notifications')
    ) {
      // 获取最新的未读通知
      const newNotifications = state.notifications
        .filter(n => !n.isRead && !visibleNotifications.value.find(v => v.id === n.id))
        .slice(0, maxVisible)
      
      // 添加到显示列表
      newNotifications.forEach(notification => {
        addNotificationToDisplay(notification)
      })
    }
  })
})

onUnmounted(() => {
  if (unsubscribe) {
    unsubscribe()
  }
})

// 添加通知到显示列表
const addNotificationToDisplay = (notification) => {
  // 如果已经显示了最大数量，移除最旧的
  if (visibleNotifications.value.length >= maxVisible) {
    visibleNotifications.value.shift()
  }
  
  // 添加新通知
  visibleNotifications.value.push({
    ...notification,
    displayTime: Date.now()
  })
  
  // 设置自动隐藏
  setTimeout(() => {
    removeNotificationFromDisplay(notification.id)
  }, autoHideDelay)
}

// 从显示列表中移除通知
const removeNotificationFromDisplay = (notificationId) => {
  const index = visibleNotifications.value.findIndex(n => n.id === notificationId)
  if (index > -1) {
    visibleNotifications.value.splice(index, 1)
  }
}

// 处理通知点击
const handleNotificationClick = async (notification) => {
  try {
    // 标记为已读
    if (!notification.isRead) {
      await webSocketStore.markAsRead(notification.id)
    }
    
    // 跳转到相关页面
    if (notification.actionUrl) {
      router.push(notification.actionUrl)
    }
    
    // 从显示列表中移除
    removeNotificationFromDisplay(notification.id)
  } catch (error) {
    console.error('处理通知点击失败:', error)
  }
}

// 标记为已读
const markAsRead = async (notification) => {
  try {
    await webSocketStore.markAsRead(notification.id)
    // 更新显示列表中的状态
    const item = visibleNotifications.value.find(n => n.id === notification.id)
    if (item) {
      item.isRead = true
    }
  } catch (error) {
    console.error('标记通知为已读失败:', error)
  }
}

// 关闭通知
const closeNotification = (notification) => {
  removeNotificationFromDisplay(notification.id)
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  
  const time = new Date(timeStr)
  const now = new Date()
  const diff = now - time
  
  if (diff < 60000) { // 1分钟内
    return '刚刚'
  } else if (diff < 3600000) { // 1小时内
    return `${Math.floor(diff / 60000)}分钟前`
  } else if (diff < 86400000) { // 24小时内
    return `${Math.floor(diff / 3600000)}小时前`
  } else {
    return time.toLocaleDateString()
  }
}

// 暴露方法给父组件
defineExpose({
  addNotification: addNotificationToDisplay,
  removeNotification: removeNotificationFromDisplay
})
</script>

<style scoped>
.notification-popup-container {
  position: fixed;
  top: 80px;
  right: 20px;
  z-index: 9999;
  pointer-events: none;
}

.notification-wrapper {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.notification-popup {
  min-width: 320px;
  max-width: 400px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-left: 4px solid #409eff;
  padding: 16px;
  display: flex;
  align-items: flex-start;
  gap: 12px;
  cursor: pointer;
  pointer-events: auto;
  transition: all 0.3s ease;
}

.notification-popup:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
  transform: translateY(-2px);
}

.notification-popup.success {
  border-left-color: #67c23a;
}

.notification-popup.warning {
  border-left-color: #e6a23c;
}

.notification-popup.error {
  border-left-color: #f56c6c;
}

.notification-icon {
  flex-shrink: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #409eff;
}

.notification-popup.success .notification-icon {
  color: #67c23a;
}

.notification-popup.warning .notification-icon {
  color: #e6a23c;
}

.notification-popup.error .notification-icon {
  color: #f56c6c;
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-title {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
  margin-bottom: 4px;
  line-height: 1.4;
}

.notification-message {
  font-size: 13px;
  color: #606266;
  line-height: 1.4;
  margin-bottom: 6px;
  word-break: break-word;
}

.notification-time {
  font-size: 12px;
  color: #909399;
}

.notification-actions {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: 4px;
}

.notification-actions .el-button {
  padding: 4px 8px;
  font-size: 12px;
}

/* 动画效果 */
.notification-enter-active {
  transition: all 0.3s ease;
}

.notification-leave-active {
  transition: all 0.3s ease;
}

.notification-enter-from {
  opacity: 0;
  transform: translateX(100%);
}

.notification-leave-to {
  opacity: 0;
  transform: translateX(100%);
}

.notification-move {
  transition: transform 0.3s ease;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .notification-popup-container {
    top: 60px;
    right: 10px;
    left: 10px;
  }
  
  .notification-popup {
    min-width: auto;
    max-width: none;
  }
}
</style>