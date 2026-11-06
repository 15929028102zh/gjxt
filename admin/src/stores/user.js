import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', () => {
  // 用户信息
  const userInfo = ref(null)
  
  // 从localStorage初始化用户信息
  const initUserInfo = () => {
    try {
      const storedUserInfo = localStorage.getItem('userInfo')
      if (storedUserInfo) {
        userInfo.value = JSON.parse(storedUserInfo)
      }
    } catch (error) {
      console.error('初始化用户信息失败:', error)
      userInfo.value = null
    }
  }
  
  // 设置用户信息
  const setUserInfo = (info) => {
    userInfo.value = info
    if (info) {
      localStorage.setItem('userInfo', JSON.stringify(info))
    } else {
      localStorage.removeItem('userInfo')
    }
  }
  
  // 清除用户信息
  const clearUserInfo = () => {
    userInfo.value = null
    localStorage.removeItem('userInfo')
  }
  
  // 计算属性：是否已登录
  const isLoggedIn = computed(() => {
    return !!userInfo.value
  })
  
  // 计算属性：是否为管理员(兼容,任一管理员标志位为 1)
  const isAdmin = computed(() => {
    return isWeeklyAdmin.value || isMonthlyAdmin.value
  })

  // 计算属性：周检管理员
  const isWeeklyAdmin = computed(() => {
    return userInfo.value?.isWeeklyAdmin === 1
  })

  // 计算属性：月检管理员
  const isMonthlyAdmin = computed(() => {
    return userInfo.value?.isMonthlyAdmin === 1
  })

  // 计算属性：用户权限列表
  const permissions = computed(() => {
    return userInfo.value?.permissions || []
  })

  // 检查是否有指定权限(任一管理员标志位为 1 即放行,对应「任一即可」共享页面规则)
  const hasPermission = (permission) => {
    if (isAdmin.value) {
      return true
    }
    return permissions.value.includes(permission)
  }

  // 初始化
  initUserInfo()

  return {
    userInfo,
    isLoggedIn,
    isAdmin,
    isWeeklyAdmin,
    isMonthlyAdmin,
    permissions,
    initUserInfo,
    setUserInfo,
    clearUserInfo,
    hasPermission
  }
})