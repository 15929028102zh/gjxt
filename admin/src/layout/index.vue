<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar">
      <div class="logo">
        <img src="/logo.png" alt="Logo" v-if="!isCollapse" />
        <span v-if="!isCollapse">榆林公司岗检系统</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="true"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <template v-for="route in menuRoutes" :key="route.path">
          <el-sub-menu v-if="route.children && route.children.length > 0 && !shouldHideRoute(route)" :index="route.path">
            <template #title>
              <el-icon><component :is="route.meta.icon" /></el-icon>
              <span>{{ route.meta.title }}</span>
            </template>
            <el-menu-item
              v-for="child in route.children"
              :key="child.path"
              :index="route.path === '/' ? '/' + child.path : route.path + '/' + child.path"
              v-show="!child.meta?.hideInMenu && !shouldHideRoute(child)"
            >
              <el-icon><component :is="child.meta.icon" /></el-icon>
              <span>{{ child.meta.title }}</span>
            </el-menu-item>
          </el-sub-menu>
        </template>
      </el-menu>
    </el-aside>
    
    <!-- 主内容区 -->
    <el-container>
      <!-- 顶部导航 -->
      <el-header class="header">
        <div class="header-left">
          <el-button
            type="text"
            @click="toggleCollapse"
            class="collapse-btn"
          >
            <el-icon><Expand v-if="isCollapse" /><Fold v-else /></el-icon>
          </el-button>
          
          <!-- 导航历史 -->
          <div class="nav-history">
            <el-tag
              v-for="(item, index) in visitedViews"
              :key="item.path"
              :type="item.path === route.path ? 'primary' : 'info'"
              :effect="item.path === route.path ? 'dark' : 'plain'"
              closable
              @close="closeTag(index)"
              @click="goToView(item)"
              class="nav-tag"
            >
              {{ item.title }}
            </el-tag>
          </div>
        </div>
        
        <div class="header-right">
          <!-- WebSocket连接状态指示器 -->
          <div class="websocket-status" :title="webSocketStore.isConnected ? 'WebSocket已连接' : 'WebSocket未连接'">
            <el-icon :size="16" :color="webSocketStore.isConnected ? '#67C23A' : '#F56C6C'">
              <Connection v-if="webSocketStore.isConnected" />
              <Close v-else />
            </el-icon>
            <span class="status-text">{{ webSocketStore.isConnected ? '在线' : '离线' }}</span>
          </div>
          
          <!-- 通知图标 -->
          <div class="notification-wrapper" @click="showNotifications">
            <el-icon class="notification-icon" :size="20">
              <Bell />
            </el-icon>
            <el-badge 
              v-if="notificationCount > 0" 
              :value="notificationCount > 99 ? '99+' : notificationCount" 
              class="notification-badge"
            />
          </div>
          
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" :src="convertToHttpUrl(userStore.userInfo?.imageUrl) || '/default-avatar.svg'" />
              <span class="username">{{ userStore.userInfo?.realName || userStore.userInfo?.username || '用户' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <!-- 主内容 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
    
    <!-- 通知弹窗 -->
    <el-dialog
      v-model="showNotificationModal"
      title="通知消息"
      width="600px"
      :before-close="hideNotifications"
    >
      <el-tabs v-model="activeNotificationTab" @tab-change="handleTabChange">
        <!-- 普通通知标签页 -->
        <el-tab-pane label="系统通知" name="system">
          <div class="notification-header">
            <el-button type="text" @click="markAllAsRead" class="mark-all-read">
              全部已读
            </el-button>
          </div>
          
          <div class="notification-list">
            <div v-if="notifications.length === 0" class="empty-notifications">
              <el-empty description="暂无通知" />
            </div>
            
            <div 
                    v-for="notification in notifications" 
                    :key="notification.id" 
                    class="notification-item"
                    :class="{ unread: !notification.isRead }"
                    @click="handleNotificationClick(notification)">
                    <div class="notification-dot" v-if="!notification.isRead"></div>
              <div class="notification-content">
                <div class="notification-title">{{ notification.title }}</div>
                <div class="notification-desc">{{ notification.content }}</div>
                <div class="notification-time">{{ notification.createTime }}</div>
              </div>
              <el-icon class="notification-arrow"><ArrowRight /></el-icon>
            </div>
          </div>
        </el-tab-pane>
        
        <!-- 月检任务通知标签页 -->
        <el-tab-pane label="月检任务" name="tasks">
          <div class="notification-header">
            <el-button type="text" @click="refreshTaskNotifications" class="refresh-btn">
              刷新
            </el-button>
          </div>
          
          <div class="notification-list">
            <div v-if="taskNotifications.length === 0" class="empty-notifications">
              <el-empty description="暂无已通知的月检任务" />
            </div>
            
            <div 
              v-for="taskNotification in taskNotifications" 
              :key="taskNotification.id"
              class="notification-item task-notification"
              @click="handleTaskNotificationClick(taskNotification)"
            >
              <div class="task-status-badge" :class="getStatusClass(taskNotification.status)">
                {{ getStatusText(taskNotification.status) }}
              </div>
              <div class="notification-content">
                <div class="notification-title">{{ taskNotification.title }}</div>
                <div class="notification-desc">
                  任务编号：{{ taskNotification.taskNo }}<br>
                  月检时间：{{ taskNotification.inspectionMonth }}<br>
                  任务状态：{{ getStatusText(taskNotification.status) }}<br>
                  {{ taskNotification.content }}
                </div>
                <div class="notification-time">{{ taskNotification.createTime }}</div>
              </div>
              <el-icon class="notification-arrow"><ArrowRight /></el-icon>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </el-container>
  
  <!-- 通知弹框组件 -->
  <NotificationPopup ref="notificationPopupRef" />
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Bell, ArrowRight, Connection, Close, Expand, Fold, ArrowDown } from '@element-plus/icons-vue'
import { logout } from '@/api/auth'
import { userApi } from '@/api/system'
import { monthlyTaskApi } from '@/api/monthlyRecord'
import { getToken } from '@/utils/token'
import { convertToHttpUrl } from '@/config/api'
import { useWebSocketStore } from '@/stores/websocket'
import { useUserStore } from '@/stores/user'
import NotificationPopup from '@/components/NotificationPopup.vue'

const route = useRoute()
const router = useRouter()

// 使用stores
const webSocketStore = useWebSocketStore()
const userStore = useUserStore()

const isCollapse = ref(false)
const visitedViews = ref([])
const showNotificationModal = ref(false)
const activeNotificationTab = ref('system')
const taskNotifications = ref([])

// 使用WebSocket store中的通知数据
const notifications = computed(() => webSocketStore.notifications)
const notificationCount = computed(() => webSocketStore.unreadCount)

// 菜单路由 - 过滤掉登录页和标记为hideInMenu的页面
const menuRoutes = computed(() => {
  return router.getRoutes().filter(route => {
    return route.path !== '/login' && 
           !route.meta?.hideInMenu && 
           (route.meta?.title || (route.children && route.children.length > 0))
  })
})

// 判断是否应该隐藏路由（基于权限）
const shouldHideRoute = (route) => {
  // 如果路由需要管理员权限，但当前用户不是管理员，则隐藏
  if (route.meta?.requiresAdmin && userStore.userInfo?.isAdmin !== 1) {
    return true
  }
  return false
}

// 当前激活的菜单
const activeMenu = computed(() => {
  return route.path
})

// 添加访问过的视图
const addVisitedView = (view) => {
  if (view.path === '/login') return
  
  const existingIndex = visitedViews.value.findIndex(v => v.path === view.path)
  if (existingIndex === -1) {
    visitedViews.value.push({
      path: view.path,
      title: view.meta?.title || '未知页面',
      name: view.name
    })
  }
}

// 关闭标签
const closeTag = (index) => {
  const view = visitedViews.value[index]
  visitedViews.value.splice(index, 1)
  
  // 如果关闭的是当前页面，跳转到最后一个标签或首页
  if (view.path === route.path) {
    if (visitedViews.value.length > 0) {
      const lastView = visitedViews.value[visitedViews.value.length - 1]
      router.push(lastView.path)
    } else {
      router.push('/home')
    }
  }
}

// 跳转到指定视图
const goToView = (view) => {
  if (view.path !== route.path) {
    router.push(view.path)
  }
}

// 获取用户信息
const getUserInfo = async () => {
  try {
    console.log('Layout组件开始获取用户信息')
    const token = getToken()
    console.log('Layout组件检查token:', token ? '存在' : '不存在')
    if (!token) {
      console.log('Layout组件: token不存在，跳转到登录页')
      router.push('/login')
      return
    }
    
    // 初始化用户store
    userStore.initUserInfo()
    
    // 从用户store获取用户信息
    if (userStore.userInfo) {
      console.log('Layout组件使用store中的用户信息:', userStore.userInfo)
      
      // 连接WebSocket
      webSocketStore.connect()
      
      return
    }
    
    // 如果store中没有用户信息，尝试从token解析用户名
    const payload = JSON.parse(atob(token.split('.')[1]))
    const username = payload.sub || payload.username
    console.log('Layout组件从token解析用户名:', username)
    
    if (username) {
      console.log('Layout组件开始调用API获取用户信息')
      const response = await userApi.getUserInfo(username)
      console.log('Layout组件API调用结果:', response)
      if (response.code === 200) {
        userStore.setUserInfo(response.data)
        console.log('Layout组件用户信息设置完成:', response.data)
        
        // 连接WebSocket
        webSocketStore.connect()
      }
    }
  } catch (error) {
    console.error('Layout组件获取用户信息失败:', error)
    console.log('Layout组件发生异常，但不会跳转到登录页')
  }
}

// 监听路由变化，添加到访问历史
watch(route, (newRoute) => {
  addVisitedView(newRoute)
}, { immediate: true })



// 组件挂载时获取用户信息
onMounted(() => {
  getUserInfo()
  window.__openNotifications = (tab) => {
    showNotificationModal.value = true
    activeNotificationTab.value = tab || 'system'
    if (activeNotificationTab.value === 'tasks') {
      fetchTaskNotifications()
    }
  }
  window.addEventListener('open-notifications', (e) => {
    const detail = e && e.detail ? e.detail : {}
    const tab = detail.tab || 'system'
    showNotificationModal.value = true
    activeNotificationTab.value = tab
    if (tab === 'tasks') {
      fetchTaskNotifications()
    }
  })
})

onUnmounted(() => {
  // 断开WebSocket连接
  webSocketStore.disconnect()
  try { delete window.__openNotifications } catch (_) {}
  window.removeEventListener('open-notifications', () => {})
})

// 显示通知弹窗
const showNotifications = () => {
  showNotificationModal.value = true
  // 获取最新通知
  webSocketStore.fetchNotifications()
  // 如果当前标签页是月检任务，也获取月检任务通知
  if (activeNotificationTab.value === 'tasks') {
    fetchTaskNotifications()
  }
}

// 隐藏通知弹窗
const hideNotifications = () => {
  showNotificationModal.value = false
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
    
    hideNotifications()
  } catch (error) {
    console.error('处理通知点击失败:', error)
  }
}

// 标记所有通知为已读
const markAllAsRead = async () => {
  try {
    const unreadNotifications = notifications.value.filter(n => !n.isRead)
    for (const notification of unreadNotifications) {
      await webSocketStore.markAsRead(notification.id)
    }
    ElMessage.success('所有通知已标记为已读')
  } catch (error) {
    console.error('标记所有通知为已读失败:', error)
    ElMessage.error('操作失败')
  }
}

// 获取月检任务通知列表
const fetchTaskNotifications = async () => {
  try {
    const result = await webSocketStore.fetchTaskNotifications()
    taskNotifications.value = result
  } catch (error) {
    console.error('获取月检任务通知失败:', error)
    ElMessage.error('获取月检任务通知失败')
  }
}

// 处理标签页切换
const handleTabChange = (tabName) => {
  if (tabName === 'tasks') {
    fetchTaskNotifications()
  }
}

// 刷新月检任务通知
const refreshTaskNotifications = () => {
  fetchTaskNotifications()
  ElMessage.success('刷新成功')
}

// 处理月检任务通知点击
const handleTaskNotificationClick = (taskNotification) => {
  // 跳转到月检记录录入页面
  router.push(`/monthly/record?taskId=${taskNotification.taskId}`)
  hideNotifications()
}

// 获取月检任务的状态文本 - 与移动端保持一致
// 任务状态：0-未开始，1-进行中，2-已完成
const getStatusText = (status) => {
  const statusMap = {
    0: '未开始',
    1: '进行中', 
    2: '已完成',
    3: '已取消'
  }
  return statusMap[status] || '未知状态'
}

// 获取任务状态样式类
const getStatusClass = (status) => {
  const classMap = {
    0: 'status-pending',
    1: 'status-processing',
    2: 'status-completed', 
    3: 'status-cancelled'
  }
  return classMap[status] || 'status-unknown'
}

// 切换侧边栏折叠
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

// 处理用户下拉菜单
const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm(
          '确定要退出登录吗？',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        // 调用退出登录API
        await logout()
        
        // 清除本地token
        localStorage.removeItem('token')
        
        // 跳转到登录页
        router.push('/login')
        
        ElMessage.success('退出登录成功')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('退出登录失败:', error)
          // 即使API调用失败，也清除本地token并跳转
          localStorage.removeItem('token')
          router.push('/login')
        }
      }
      break
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
  transition: width 0.3s;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  border-bottom: 1px solid #434a50;
}

.logo img {
  width: 32px;
  height: 32px;
  margin-right: 8px;
}

.header {
  background: white;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.collapse-btn {
  font-size: 18px;
  color: #606266;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.websocket-status {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  border-radius: 4px;
  background-color: #f5f7fa;
  cursor: pointer;
  transition: all 0.3s;
}

.websocket-status:hover {
  background-color: #e4e7ed;
}

.websocket-status .status-text {
  font-size: 12px;
  color: #606266;
  font-weight: 500;
}

.notification-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.notification-icon {
  cursor: pointer;
  font-size: 18px;
  color: #606266;
  transition: color 0.3s;
}

.notification-icon:hover {
  color: #409eff;
}

.notification-badge {
  position: absolute;
  top: -8px;
  right: -8px;
}
  
  .notification-header {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 16px;
  }
  
  .notification-header .mark-all-read {
    color: #409eff;
    font-size: 14px;
  }
  
  .notification-list {
    max-height: 400px;
    overflow-y: auto;
  }
  
  .notification-item {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
    cursor: pointer;
    position: relative;
    transition: background-color 0.3s;
  }
  
  .notification-item:hover {
    background-color: #f5f7fa;
  }
  
  .notification-item.unread {
    background-color: #f0f9ff;
  }
  
  .notification-dot {
    width: 8px;
    height: 8px;
    background-color: #f56c6c;
    border-radius: 50%;
    margin-right: 12px;
    flex-shrink: 0;
  }
  
  .notification-content {
    flex: 1;
  }
  
  .notification-title {
    font-size: 14px;
    font-weight: 500;
    color: #303133;
    margin-bottom: 4px;
  }
  
  .notification-desc {
    font-size: 12px;
    color: #909399;
    margin-bottom: 4px;
  }
  
  .notification-time {
    font-size: 12px;
    color: #c0c4cc;
  }
  
  .notification-arrow {
    color: #c0c4cc;
    font-size: 14px;
    margin-left: 8px;
  }
  
  .empty-notifications {
    text-align: center;
    padding: 40px 0;
  }

  /* 月检任务通知样式 */
  .task-notification .task-status-badge {
    color: white;
    font-size: 12px;
    padding: 2px 8px;
    border-radius: 12px;
    margin-right: 12px;
    flex-shrink: 0;
    font-weight: 500;
  }
  
  /* 不同状态的样式 */
  .task-status-badge.status-pending {
    background-color: #909399; /* 灰色 - 待执行 */
  }
  
  .task-status-badge.status-processing {
    background-color: #409eff; /* 蓝色 - 执行中 */
  }
  
  .task-status-badge.status-completed {
    background-color: #67c23a; /* 绿色 - 已完成 */
  }
  
  .task-status-badge.status-overdue {
    background-color: #f56c6c; /* 红色 - 已逾期 */
  }
  
  .task-status-badge.status-unknown {
    background-color: #c0c4cc; /* 浅灰色 - 未知状态 */
  }
  
  .task-notification .notification-desc {
    line-height: 1.5;
    white-space: pre-line;
  }
  
  .task-notification:hover {
    background-color: #f0f9ff;
    border-left: 3px solid #409eff;
  }

  /* 通知标签页样式 */
  .el-tabs .el-tabs__header {
    margin-bottom: 16px;
  }
  
  .el-tabs .el-tabs__nav-wrap {
    padding: 0 16px;
  }

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #606266;
}

.username {
  font-size: 14px;
}

.main-content {
  background-color: #f5f5f5;
}

.nav-history {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  max-width: 600px;
  overflow-x: auto;
}

.nav-tag {
  cursor: pointer;
  user-select: none;
  white-space: nowrap;
}

.nav-tag:hover {
  opacity: 0.8;
}
</style>