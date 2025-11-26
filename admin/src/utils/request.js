import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { getToken, removeToken, refreshToken } from './token'

// 创建axios实例
const request = axios.create({
  baseURL: '/api', // 基础URL，会被vite代理到后端
  timeout: 10000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    console.log('发送请求:', config.method?.toUpperCase(), config.url, config.data || config.params)
    // 登录接口不需要token
    if (config.url !== '/user/login') {
      // 添加token到请求头
      const token = getToken()
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
        // 每次请求时刷新token过期时间
        refreshToken()
      }
      
      // 添加用户ID到请求头
      try {
        const userInfoStr = localStorage.getItem('userInfo')
        if (userInfoStr) {
          const userInfo = JSON.parse(userInfoStr)
          if (userInfo.id) {
            config.headers.userId = userInfo.id
          }
        }
      } catch (error) {
        console.error('获取用户ID失败:', error)
      }
      
      // 如果没有token，仍然发送请求，让后端返回401错误
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const { data } = response
    console.log('收到响应:', response.config.method?.toUpperCase(), response.config.url, '状态码:', response.status, '数据:', data)
    
    // 如果后端返回的数据结构包含code字段
    if (data.code !== undefined) {
      if (data.code === 200) {
        return data
      } else {
        ElMessage.error(data.message || '请求失败')
        return Promise.reject(new Error(data.message || '请求失败'))
      }
    }
    
    // 直接返回数据
    return data
  },
  error => {
    console.error('响应错误:', error)
    
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          ElMessage.error('未授权，请重新登录')
          removeToken()
          router.push('/login')
          break
        case 403:
          ElMessage.error('拒绝访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(data?.message || `请求失败: ${status}`)
      }
    } else if (error.request) {
      ElMessage.error('网络错误，请检查网络连接')
    } else {
      ElMessage.error('请求配置错误')
    }
    
    return Promise.reject(error)
  }
)

export default request