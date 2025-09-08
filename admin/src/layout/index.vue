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
          <el-sub-menu v-if="route.children && route.children.length > 0" :index="route.path">
            <template #title>
              <el-icon><component :is="route.meta.icon" /></el-icon>
              <span>{{ route.meta.title }}</span>
            </template>
            <el-menu-item
              v-for="child in route.children"
              :key="child.path"
              :index="route.path + '/' + child.path"
              v-show="!child.meta?.hideInMenu"
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
  </el-container>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { logout } from '@/api/auth'
import { userApi } from '@/api/system'
import { getToken } from '@/utils/token'

const route = useRoute()
const router = useRouter()

const isCollapse = ref(false)
const visitedViews = ref([])
const userInfo = ref({})

// 菜单路由 - 过滤掉登录页和标记为hideInMenu的页面
const menuRoutes = computed(() => {
  return router.getRoutes().filter(route => {
    return route.path !== '/login' && 
           !route.meta?.hideInMenu && 
           (route.meta?.title || (route.children && route.children.length > 0))
  })
})

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

// 将相对路径转换为HTTP访问路径
const convertToHttpUrl = (relativePath) => {
  if (!relativePath || relativePath.trim() === '') {
    return ''
  }
  
  // 如果已经是完整的HTTP/HTTPS URL，直接返回
  if (relativePath.startsWith('http://') || relativePath.startsWith('https://')) {
    return relativePath
  }
  
  // 直接拼接完整的HTTP URL
  // 后端返回相对路径，如: /files/images/20250831/xxx.jpg
  const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://192.168.244.128:8080'
  const fullUrl = `${baseUrl}/api${relativePath}`
  return fullUrl
}

// 组件挂载时获取用户信息
onMounted(() => {
  getUserInfo()
})

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