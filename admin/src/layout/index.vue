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
              <el-avatar :size="32" :src="convertToHttpUrl(userInfo.imageUrl) || '/default-avatar.svg'" />
              <span class="username">{{ userInfo.realName || userInfo.username || '用户' }}</span>
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
      width="500px"
      :before-close="hideNotifications"
    >
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
          @click="handleNotificationClick(notification)"
        >
          <div class="notification-dot" v-if="!notification.isRead"></div>
          <div class="notification-content">
            <div class="notification-title">{{ notification.title }}</div>
            <div class="notification-desc">{{ notification.content }}</div>
            <div class="notification-time">{{ notification.createTime }}</div>
          </div>
          <el-icon class="notification-arrow"><ArrowRight /></el-icon>
        </div>
      </div>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Bell, ArrowRight } from '@element-plus/icons-vue'
import { logout } from '@/api/auth'
import { userApi } from '@/api/system'
import { monthlyTaskApi } from '@/api/monthlyRecord'
import { getToken } from '@/utils/token'
import { convertToHttpUrl } from '@/config/api'

const route = useRoute()
const router = useRouter()

const isCollapse = ref(false)
const visitedViews = ref([])
const userInfo = ref({})
const showNotificationModal = ref(false)
const notifications = ref([])
const notificationCount = ref(0)

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
  if (route.meta?.requiresAdmin && userInfo.value?.isAdmin !== 1 && userInfo.value?.username !== 'admin') {
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
    
    // 从token中解析用户名或从localStorage获取
    const userInfoStr = localStorage.getItem('userInfo')
    console.log('Layout组件检查本地用户信息:', userInfoStr ? '存在' : '不存在')
    if (userInfoStr) {
      const storedUserInfo = JSON.parse(userInfoStr)
      userInfo.value = storedUserInfo
      console.log('Layout组件使用本地用户信息:', storedUserInfo)
      return
    }
    
    // 如果没有存储的用户信息，尝试从token解析用户名
    const payload = JSON.parse(atob(token.split('.')[1]))
    const username = payload.sub || payload.username
    console.log('Layout组件从token解析用户名:', username)
    
    if (username) {
      console.log('Layout组件开始调用API获取用户信息')
      const response = await userApi.getUserInfo(username)
      console.log('Layout组件API调用结果:', response)
      if (response.code === 200) {
        userInfo.value = response.data
        localStorage.setItem('userInfo', JSON.stringify(response.data))
        console.log('Layout组件用户信息设置完成:', response.data)
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
  loadNotifications()
})

// 加载通知
const loadNotifications = async () => {
  try {
    const token = getToken()
    if (!token || !userInfo.value.id) return
    
    const response = await monthlyTaskApi.getUserTasks(userInfo.value.id)
    if (response.code === 200) {
      notifications.value = response.data.map(task => ({
        id: task.id,
        taskId: task.id,
        taskNo: task.taskNo,
        title: `月检任务通知 - ${task.taskNo}`,
        content: `您有新的月检任务需要处理，任务编号：${task.taskNo}`,
        createTime: task.createTime,
        isRead: task.notificationRead || false
      }))
      
      notificationCount.value = notifications.value.filter(n => !n.isRead).length
    }
  } catch (error) {
    console.error('加载通知失败:', error)
  }
}

// 显示通知弹窗
const showNotifications = () => {
  showNotificationModal.value = true
}

// 隐藏通知弹窗
const hideNotifications = () => {
  showNotificationModal.value = false
}

// 处理通知点击
const handleNotificationClick = (notification) => {
  // 标记为已读
  notification.isRead = true
  updateNotificationCount()
  
  // 跳转到月检记录录入页面
  router.push(`/monthly/record?taskId=${notification.taskNo}`)
  
  hideNotifications()
}

// 更新通知数量
const updateNotificationCount = () => {
  notificationCount.value = notifications.value.filter(n => !n.isRead).length
}

// 全部标记为已读
const markAllAsRead = () => {
  notifications.value.forEach(n => n.isRead = true)
  notificationCount.value = 0
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
    
    .notification-icon {
      cursor: pointer;
      font-size: 18px;
      color: #606266;
      transition: color 0.3s;
      
      &:hover {
        color: #409eff;
      }
    }
  }
  
  .notification-header {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 16px;
    
    .mark-all-read {
      color: #409eff;
      font-size: 14px;
    }
  }
  
  .notification-list {
    max-height: 400px;
    overflow-y: auto;
    
    .notification-item {
      display: flex;
      align-items: center;
      padding: 12px 0;
      border-bottom: 1px solid #f0f0f0;
      cursor: pointer;
      position: relative;
      transition: background-color 0.3s;
      
      &:hover {
        background-color: #f5f7fa;
      }
      
      &.unread {
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
      }
      
      .notification-arrow {
        color: #c0c4cc;
        font-size: 14px;
        margin-left: 8px;
      }
    }
  }
  
  .empty-notifications {
    text-align: center;
    padding: 40px 0;
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