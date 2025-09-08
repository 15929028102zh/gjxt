<template>
  <div class="home-container">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="greeting">欢迎回来，{{ userInfo.realName || '用户' }}</h1>
        <p class="current-date">{{ currentDate }}</p>
      </div>
      <div class="user-avatar">
        <el-avatar :size="60" :src="convertToHttpUrl(userInfo.imageUrl) || '/default-avatar.svg'" />
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-container">
      <div class="stats-card" @click="goToPendingTasks">
        <div class="stats-icon pending">
          <el-icon><Clock /></el-icon>
        </div>
        <div class="stats-content">
          <div class="stats-number">{{ statistics.pendingTasks }}</div>
          <div class="stats-label">待处理任务</div>
        </div>
      </div>
      
      <div class="stats-card" @click="goToCompletedTasks">
        <div class="stats-icon completed">
          <el-icon><Check /></el-icon>
        </div>
        <div class="stats-content">
          <div class="stats-number">{{ statistics.completedTasks }}</div>
          <div class="stats-label">已完成任务</div>
        </div>
      </div>
      
      <div class="stats-card" @click="goToTotalInspections">
        <div class="stats-icon total">
          <el-icon><Document /></el-icon>
        </div>
        <div class="stats-content">
          <div class="stats-number">{{ statistics.totalInspections }}</div>
          <div class="stats-label">总巡检次数</div>
        </div>
      </div>
      
      <div class="stats-card" @click="goToPendingIssues">
        <div class="stats-icon issues">
          <el-icon><Warning /></el-icon>
        </div>
        <div class="stats-content">
          <div class="stats-number">{{ statistics.pendingIssues }}</div>
          <div class="stats-label">待整改问题</div>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-container">
      <div class="chart-card">
        <div class="chart-header">
          <h3>月度巡检趋势</h3>
        </div>
        <div ref="monthlyTrendChart" class="chart-content"></div>
      </div>
      
      <div class="chart-card">
        <div class="chart-header">
          <h3>问题分类统计</h3>
        </div>
        <div ref="issueCategoryChart" class="chart-content"></div>
      </div>
    </div>

    <!-- 快捷操作 -->
    <div class="quick-actions-section">
      <h3 class="section-title">快捷操作</h3>
      <div class="quick-actions">
        <div class="action-button" @click="goToUserManagement">
          <div class="action-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="action-content">
            <div class="action-title">用户管理</div>
            <div class="action-desc">管理系统用户</div>
          </div>
        </div>
        
        <div class="action-button" @click="goToDeptManagement">
          <div class="action-icon">
            <el-icon><OfficeBuilding /></el-icon>
          </div>
          <div class="action-content">
            <div class="action-title">部门管理</div>
            <div class="action-desc">管理组织架构</div>
          </div>
        </div>
        
        <div class="action-button" @click="goToDictManagement">
          <div class="action-icon">
            <el-icon><Collection /></el-icon>
          </div>
          <div class="action-content">
            <div class="action-title">字典管理</div>
            <div class="action-desc">管理数据字典</div>
          </div>
        </div>
        
        <div class="action-button" @click="goToMonthlyTasks">
          <div class="action-icon">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="action-content">
            <div class="action-title">月检任务</div>
            <div class="action-desc">管理月检计划</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { User, OfficeBuilding, Collection, Clock, Check, Document, Warning, Calendar } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { userApi } from '@/api/system'

const router = useRouter()

// 用户信息
const userInfo = ref({
  realName: '',
  imageUrl: ''
})

// 当前日期
const currentDate = ref('')

// 统计数据
const statistics = ref({
  pendingTasks: 0,
  completedTasks: 0,
  totalInspections: 0,
  pendingIssues: 0
})

// 图表引用
const monthlyTrendChart = ref(null)
const issueCategoryChart = ref(null)

// 获取用户信息
const loadUserInfo = async () => {
  try {
    // 先从localStorage获取
    const localUserInfo = localStorage.getItem('userInfo')
    if (localUserInfo) {
      const userData = JSON.parse(localUserInfo)
      userInfo.value = {
        realName: userData.realName || userData.real_name || '',
        imageUrl: userData.imageUrl || userData.image_url || ''
      }
    }
    
    // 获取username用于API调用
    let username = null
    if (localUserInfo) {
      const userData = JSON.parse(localUserInfo)
      username = userData.username
    }
    
    // 如果没有username，尝试从token解析
    if (!username) {
      const token = localStorage.getItem('token')
      if (token) {
        try {
          const payload = JSON.parse(atob(token.split('.')[1]))
          username = payload.sub || payload.username
        } catch (e) {
          console.error('解析token失败:', e)
        }
      }
    }
    
    // 如果有username，从API获取最新数据
    if (username) {
      const response = await userApi.getUserInfo(username)
      if (response.code === 200) {
        userInfo.value = {
          realName: response.data.realName || response.data.real_name || '',
          imageUrl: response.data.imageUrl || response.data.image_url || ''
        }
      }
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 获取统计数据
const loadStatistics = async () => {
  try {
    // 模拟数据，实际应该从API获取
    statistics.value = {
      pendingTasks: 12,
      completedTasks: 156,
      totalInspections: 2340,
      pendingIssues: 8
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 初始化月度趋势图表
const initMonthlyTrendChart = () => {
  if (!monthlyTrendChart.value) return
  
  const chart = echarts.init(monthlyTrendChart.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月']
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      name: '巡检次数',
      type: 'line',
      smooth: true,
      data: [320, 332, 301, 334, 390, 330],
      itemStyle: {
        color: '#409EFF'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0, color: 'rgba(64, 158, 255, 0.3)'
          }, {
            offset: 1, color: 'rgba(64, 158, 255, 0.1)'
          }]
        }
      }
    }]
  }
  chart.setOption(option)
  
  // 响应式
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

// 初始化问题分类图表
const initIssueCategoryChart = () => {
  if (!issueCategoryChart.value) return
  
  const chart = echarts.init(issueCategoryChart.value)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      bottom: '5%',
      left: 'center'
    },
    series: [{
      name: '问题分类',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '18',
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: [
        { value: 35, name: '设备故障', itemStyle: { color: '#FF6B6B' } },
        { value: 25, name: '环境异常', itemStyle: { color: '#4ECDC4' } },
        { value: 20, name: '安全隐患', itemStyle: { color: '#45B7D1' } },
        { value: 15, name: '维护需求', itemStyle: { color: '#96CEB4' } },
        { value: 5, name: '其他', itemStyle: { color: '#FFEAA7' } }
      ]
    }]
  }
  chart.setOption(option)
  
  // 响应式
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

// 格式化当前日期
const formatCurrentDate = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  const weekday = weekdays[now.getDay()]
  currentDate.value = `${year}年${month}月${day}日 ${weekday}`
}

// 路由跳转方法
const goToUserManagement = () => {
  router.push('/system/user')
}

const goToDeptManagement = () => {
  router.push('/system/dept')
}

const goToDictManagement = () => {
  router.push('/system/dict')
}

const goToMonthlyTasks = () => {
  // 跳转到月检任务页面
  router.push('/inspection/monthly')
}

const goToPendingTasks = () => {
  // 跳转到待处理任务页面
  router.push('/inspection/pending')
}

const goToCompletedTasks = () => {
  // 跳转到已完成任务页面
  router.push('/inspection/completed')
}

const goToTotalInspections = () => {
  // 跳转到巡检记录页面
  router.push('/inspection/records')
}

const goToPendingIssues = () => {
  // 跳转到待整改问题页面
  router.push('/inspection/issues')
}

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

// 组件挂载时初始化
onMounted(async () => {
  try {
    console.log('首页组件开始挂载')
    formatCurrentDate()
    console.log('日期格式化完成')
    
    await loadUserInfo()
    console.log('用户信息加载完成')
    
    await loadStatistics()
    console.log('统计数据加载完成')
    
    // 等待DOM更新后初始化图表
    await nextTick()
    console.log('DOM更新完成，开始初始化图表')
    
    initMonthlyTrendChart()
    console.log('月度趋势图表初始化完成')
    
    initIssueCategoryChart()
    console.log('问题分类图表初始化完成')
    
    console.log('首页组件挂载完成')
  } catch (error) {
    console.error('首页组件挂载过程中出现错误:', error)
  }
})
</script>

<style scoped>
.home-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
  overflow-y: auto;
}

/* 欢迎区域 */
.welcome-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: white;
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.3);
}

.welcome-content .greeting {
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 8px 0;
}

.welcome-content .current-date {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.user-avatar {
  flex-shrink: 0;
}

/* 统计卡片 */
.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stats-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid #f0f0f0;
}

.stats-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.stats-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 20px;
}

.stats-icon.pending {
  background: rgba(255, 193, 7, 0.1);
  color: #ffc107;
}

.stats-icon.completed {
  background: rgba(40, 167, 69, 0.1);
  color: #28a745;
}

.stats-icon.total {
  background: rgba(0, 123, 255, 0.1);
  color: #007bff;
}

.stats-icon.issues {
  background: rgba(220, 53, 69, 0.1);
  color: #dc3545;
}

.stats-content {
  flex: 1;
}

.stats-number {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 4px;
}

.stats-label {
  font-size: 14px;
  color: #6c757d;
}

/* 图表区域 */
.charts-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.chart-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border: 1px solid #f0f0f0;
}

.chart-header {
  margin-bottom: 16px;
}

.chart-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

.chart-content {
  height: 300px;
  width: 100%;
}

/* 快捷操作区域 */
.quick-actions-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border: 1px solid #f0f0f0;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 16px 0;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 16px;
}

.action-button {
  display: flex;
  align-items: center;
  padding: 16px;
  border-radius: 8px;
  background: #f8f9fa;
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid #e9ecef;
}

.action-button:hover {
  background: #e9ecef;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.action-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: #667eea;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 18px;
}

.action-content {
  flex: 1;
}

.action-title {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 4px;
}

.action-desc {
  font-size: 14px;
  color: #6c757d;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .home-container {
    padding: 16px;
  }
  
  .welcome-section {
    flex-direction: column;
    text-align: center;
    gap: 16px;
  }
  
  .stats-container {
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 16px;
  }
  
  .charts-container {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .chart-content {
    height: 250px;
  }
  
  .quick-actions {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  
  .welcome-content .greeting {
    font-size: 24px;
  }
}

@media (max-width: 480px) {
  .stats-container {
    grid-template-columns: 1fr;
  }
  
  .stats-card {
    padding: 16px;
  }
  
  .chart-content {
    height: 200px;
  }
}
</style>