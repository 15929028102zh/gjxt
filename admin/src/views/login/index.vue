<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <img src="/logo.png" alt="Logo" class="logo" />
        <h2>岗位巡检系统</h2>
        <p>后台管理系统</p>
      </div>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, userApi } from '@/api/system'
import { setToken, isTokenValid } from '@/utils/token'

const router = useRouter()
const route = useRoute()
const loginFormRef = ref()
const loading = ref(false)

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: ''
})

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    // 调用登录API
    const response = await login({
      username: loginForm.username,
      password: loginForm.password
    })
    
    // 保存token到cookie和sessionStorage，设置2小时过期
    if (response.data) {
      setToken(response.data, 2)
      
      // 获取用户信息并存储到localStorage
      try {
        const userInfoResponse = await userApi.getUserInfo(loginForm.username)
        if (userInfoResponse.code === 200 && userInfoResponse.data) {
          localStorage.setItem('userInfo', JSON.stringify(userInfoResponse.data))
        }
      } catch (userInfoError) {
        console.error('获取用户信息失败:', userInfoError)
        // 即使获取用户信息失败，也允许登录成功
      }
      
      ElMessage.success('登录成功')
      
      // 延迟跳转，确保token已经设置并且页面状态更新
        setTimeout(() => {
          console.log('开始执行跳转逻辑')
          console.log('当前路径:', route.path)
          console.log('Token是否有效:', isTokenValid())
          console.log('准备跳转到根路径 /')
          router.push('/').then(() => {
            console.log('路由跳转Promise成功')
            setTimeout(() => {
              console.log('跳转后的路径:', router.currentRoute.value.path)
              console.log('跳转后的完整路由信息:', router.currentRoute.value)
            }, 100)
          }).catch(err => {
            console.error('路由跳转失败:', err)
          })
        }, 200)
    } else {
      ElMessage.error('登录失败，未获取到token')
    }
  } catch (error) {
    console.error('登录失败:', error)
    // 错误信息已在request拦截器中处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  width: 60px;
  height: 60px;
  margin-bottom: 10px;
}

.login-header h2 {
  color: #303133;
  margin-bottom: 5px;
}

.login-header p {
  color: #909399;
  font-size: 14px;
}

.login-form {
  margin-top: 20px;
}

.login-btn {
  width: 100%;
}
</style>