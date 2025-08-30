<template>
  <div class="home-container">
    <div class="welcome-card">
      <div class="logo-section">
        <img src="/logo.png" alt="Logo" class="logo" />
        <h1 class="system-title">工业巡检系统</h1>
      </div>
      
      <div class="welcome-content">
        <h2 class="welcome-title">欢迎使用工业巡检管理系统</h2>
        <p class="welcome-desc">请从左侧菜单选择功能模块进行操作</p>
        
        <div class="quick-actions">
          <el-button type="primary" @click="goToUserManagement">
            <el-icon><User /></el-icon>
            用户管理
          </el-button>
          <el-button type="success" @click="goToDeptManagement">
            <el-icon><OfficeBuilding /></el-icon>
            部门管理
          </el-button>
          <el-button type="info" @click="goToDictManagement">
            <el-icon><Collection /></el-icon>
            字典管理
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { User, OfficeBuilding, Collection } from '@element-plus/icons-vue'

const router = useRouter()

const goToUserManagement = () => {
  router.push('/system/user')
}

const goToDeptManagement = () => {
  router.push('/system/dept')
}

const goToDictManagement = () => {
  router.push('/system/dict')
}
</script>

<style scoped>
.home-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100%;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.welcome-card {
  background: white;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  text-align: center;
  max-width: 600px;
  width: 100%;
}

.logo-section {
  margin-bottom: 30px;
}

.logo {
  width: 80px;
  height: 80px;
  margin-bottom: 16px;
}

.system-title {
  color: #2c3e50;
  font-size: 28px;
  font-weight: 600;
  margin: 0;
}

.welcome-content {
  margin-top: 30px;
}

.welcome-title {
  color: #34495e;
  font-size: 24px;
  font-weight: 500;
  margin-bottom: 12px;
}

.welcome-desc {
  color: #7f8c8d;
  font-size: 16px;
  margin-bottom: 30px;
  line-height: 1.6;
}

.quick-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  flex-wrap: wrap;
}

.quick-actions .el-button {
  padding: 12px 24px;
  font-size: 14px;
}

@media (max-width: 768px) {
  .welcome-card {
    padding: 30px 20px;
  }
  
  .system-title {
    font-size: 24px;
  }
  
  .welcome-title {
    font-size: 20px;
  }
  
  .quick-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .quick-actions .el-button {
    width: 200px;
  }
}
</style>