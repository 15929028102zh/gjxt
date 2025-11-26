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



    <!-- 周检和月检数据展示区 -->
    <div class="inspection-data-section">
      <!-- 周检数据展示 -->
      <div class="inspection-card weekly-inspection">
        <div class="inspection-header">
          <h3>周检数据统计</h3>
          <el-date-picker
            v-model="weeklyDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            @change="loadWeeklyData"
            size="small"
          />
        </div>
        <div class="inspection-stats">
          <div class="stat-item">
            <div class="stat-number">{{ weeklyData.totalRecords }}</div>
            <div class="stat-label">总记录数</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ weeklyData.pendingRectification }}</div>
            <div class="stat-label">待整改</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ weeklyData.rectified }}</div>
            <div class="stat-label">已整改</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ weeklyData.reviewed }}</div>
            <div class="stat-label">已复查</div>
          </div>
        </div>
        <div ref="weeklyChart" class="chart-content"></div>
      </div>

      <!-- 月检数据展示 -->
      <div class="inspection-card monthly-inspection">
        <div class="inspection-header">
          <h3>月检数据统计</h3>
          <!-- 保留月份选择但不参与过滤，后续如需可扩展为开始/结束日期 -->
          <el-date-picker
            v-model="monthlyDateRange"
            type="month"
            format="YYYY年MM月"
            placeholder="选择月份"
            @change="loadMonthlyData"
            size="small"
          />
        </div>
        <div class="inspection-stats">
          <div class="stat-item">
            <div class="stat-number">{{ monthlyData.totalRecords }}</div>
            <div class="stat-label">总记录数</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ monthlyData.pendingRectification }}</div>
            <div class="stat-label">待整改</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ monthlyData.rectified }}</div>
            <div class="stat-label">已整改</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ monthlyData.reviewed }}</div>
            <div class="stat-label">已复查</div>
          </div>
        </div>
        <div ref="monthlyChart" class="chart-content"></div>
      </div>
    </div>

    <!-- 移除月度趋势和问题分类统计图表 -->
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import { useRouter } from 'vue-router'
// 移除统计卡片相关的图标导入
import * as echarts from 'echarts'
import { userApi } from '@/api/system'
import { monthlyTaskApi } from '@/api/monthly'
import { convertToHttpUrl } from '@/config/api'
import request from '@/utils/request'

const router = useRouter()

// 用户信息
const userInfo = ref({
  realName: '',
  imageUrl: ''
})

// 当前日期
const currentDate = ref('')

// 移除统计数据相关变量

// 周检数据
const weeklyDateRange = ref(null)
const weeklyData = ref({
  totalRecords: 0,
  pendingRectification: 0,
  rectified: 0,
  reviewed: 0
})

// 月检数据
const monthlyDateRange = ref(new Date())
const monthlyData = ref({
  totalRecords: 0,
  pendingRectification: 0,
  rectified: 0,
  reviewed: 0
})

// 图表引用
const weeklyChart = ref(null)
const monthlyChart = ref(null)

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

// 移除统计数据加载函数

// 获取周检数据
const loadWeeklyData = async () => {
  try {
    console.log('开始加载周检数据...')
    
    // 获取用户信息
    const localUserInfo = localStorage.getItem('userInfo')
    let userId = null
    let departmentId = null
    let isAdmin = false
    
    if (localUserInfo) {
      const userData = JSON.parse(localUserInfo)
      userId = userData.id || userData.userId
      departmentId = userData.deptId || userData.departmentId
      isAdmin = userData.isAdmin === 1
      console.log('用户信息:', userData)
      console.log('userId:', userId, 'departmentId:', departmentId, 'isAdmin:', isAdmin)
    }
    
    // 构建请求参数
    const params = new URLSearchParams()
    // 移除userId参数，根据用户要求不需要传递
    // if (userId) params.append('userId', userId)
    // 管理员用户不进行部门过滤，查询所有数据
    if (!isAdmin && departmentId) params.append('departmentId', departmentId)
    
    // 如果选择了日期范围，添加时间参数
    if (weeklyDateRange.value && weeklyDateRange.value.length === 2) {
      const startDate = weeklyDateRange.value[0].toISOString().split('T')[0]
      const endDate = weeklyDateRange.value[1].toISOString().split('T')[0]
      params.append('startDate', startDate)
      params.append('endDate', endDate)
    }
    
    // 构建部门过滤参数 - 管理员用户不需要部门过滤
    const deptParam = (!isAdmin && departmentId) ? `&departmentId=${departmentId}` : ''
    
    // 获取不同状态的记录数量 - 使用与移动端相同的API接口
    const createApiUrl = (status) => {
      let url = `/api/inspection-record/page?current=1&size=1&status=${status}${deptParam}`
      
      // 如果选择了日期范围，添加时间参数
      if (weeklyDateRange.value && weeklyDateRange.value.length === 2) {
        const startDate = weeklyDateRange.value[0].toISOString().split('T')[0]
        const endDate = weeklyDateRange.value[1].toISOString().split('T')[0]
        url += `&startDate=${startDate}&endDate=${endDate}`
      }
      
      return url
    }
    
    const [pendingResponse, rectifiedResponse, reviewedResponse] = await Promise.all([
      fetch(createApiUrl(0), {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      }),
      fetch(createApiUrl(1), {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      }),
      fetch(createApiUrl(2), {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
    ])
    
    const pendingData = await pendingResponse.json()
    const rectifiedData = await rectifiedResponse.json()
    const reviewedData = await reviewedResponse.json()
    
    if (pendingData.code === 200 && rectifiedData.code === 200 && reviewedData.code === 200) {
      const pending = pendingData.data?.total || 0
      const rectified = rectifiedData.data?.total || 0
      const reviewed = reviewedData.data?.total || 0
      
      weeklyData.value = {
        totalRecords: pending + rectified + reviewed,
        pendingRectification: pending,
        rectified: rectified,
        reviewed: reviewed
      }
      
      console.log('周检数据加载成功:', weeklyData.value)
    } else {
      console.error('获取周检数据失败:', pendingData.message || rectifiedData.message || reviewedData.message)
      // 使用默认数据
      weeklyData.value = {
        totalRecords: 0,
        pendingRectification: 0,
        rectified: 0,
        reviewed: 0
      }
    }
    
    // 重新初始化周检图表
    await nextTick()
    initWeeklyChart()
  } catch (error) {
    console.error('获取周检数据失败:', error)
    // 使用默认数据
    weeklyData.value = {
      totalRecords: 0,
      pendingRectification: 0,
      rectified: 0,
      reviewed: 0
    }
  }
}

// 获取月检数据（与周检一致的权限与统计维度）
const loadMonthlyData = async () => {
  try {
    const localUserInfo = localStorage.getItem('userInfo')
    let departmentId = null
    let isAdmin = false
    if (localUserInfo) {
      try {
        const userData = JSON.parse(localUserInfo)
        departmentId = userData.deptId || userData.departmentId || null
        isAdmin = (userData.isAdmin === 1)
      } catch (e) {}
    }

    let startDate = ''
    let endDate = ''
    if (monthlyDateRange.value && monthlyDateRange.value instanceof Date) {
      const d = monthlyDateRange.value
      const year = d.getFullYear()
      const month = d.getMonth() + 1
      const pad = (n) => String(n).padStart(2, '0')
      startDate = `${year}-${pad(month)}-01`
      const lastDay = new Date(year, month, 0).getDate()
      endDate = `${year}-${pad(month)}-${pad(lastDay)}`
    }

    const token = localStorage.getItem('token') || ''
    const baseParams = {
      departmentId: (!isAdmin && departmentId) ? departmentId : undefined,
      startDate: startDate || undefined,
      endDate: endDate || undefined
    }

    const req = (status) => request.get('/monthly-inspection-record/monthly-count', { params: { ...baseParams, recordStatus: status } })
    const [pendingResp, rectifiedResp, reviewedResp] = await Promise.all([req(0), req(1), req(2)])
    const toVal = (r) => (typeof r?.data === 'number') ? r.data : 0
    const pending = toVal(pendingResp)
    const rectified = toVal(rectifiedResp)
    const reviewed = toVal(reviewedResp)
    monthlyData.value = { totalRecords: pending + rectified + reviewed, pendingRectification: pending, rectified, reviewed }
    await nextTick()
    initMonthlyChart()
  } catch (error) {
    console.error('获取月检数据失败:', error)
    monthlyData.value = { totalRecords: 0, pendingRectification: 0, rectified: 0, reviewed: 0 }
  }
}

// 移除月度趋势图表和问题分类统计图表初始化函数

// 初始化周检图表
const initWeeklyChart = () => {
  if (!weeklyChart.value) return
  
  const chart = echarts.init(weeklyChart.value)
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      bottom: '5%',
      left: 'center'
    },
    series: [{
      name: '周检状态',
      type: 'pie',
      radius: ['30%', '60%'],
      center: ['50%', '45%'],
      data: [
        { value: weeklyData.value.pendingRectification, name: '待整改', itemStyle: { color: '#F56C6C' } },
        { value: weeklyData.value.rectified, name: '已整改', itemStyle: { color: '#E6A23C' } },
        { value: weeklyData.value.reviewed, name: '已复查', itemStyle: { color: '#67C23A' } }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
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

// 初始化月检图表（使用与周检相同的饼图）
const initMonthlyChart = () => {
  if (!monthlyChart.value) return
  
  const chart = echarts.init(monthlyChart.value)
  const option = {
    tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c} ({d}%)' },
    legend: { bottom: '5%', left: 'center' },
    series: [{
      name: '月检状态',
      type: 'pie',
      radius: ['30%', '60%'],
      center: ['50%', '45%'],
      data: [
        { value: monthlyData.value.pendingRectification, name: '待整改', itemStyle: { color: '#F56C6C' } },
        { value: monthlyData.value.rectified, name: '已整改', itemStyle: { color: '#E6A23C' } },
        { value: monthlyData.value.reviewed, name: '已复查', itemStyle: { color: '#67C23A' } }
      ],
      emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' } }
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
// 移除路由跳转函数



// 监听日期范围变化，重新加载数据
watch(weeklyDateRange, async (newRange) => {
  if (newRange && newRange.length === 2) {
    console.log('周检日期范围变化，重新加载数据:', newRange)
    await loadWeeklyData()
  }
}, { deep: true })

watch(monthlyDateRange, async (newRange) => {
  if (newRange && newRange.length === 2) {
    console.log('月检日期范围变化，重新加载数据:', newRange)
    await loadMonthlyData()
  }
}, { deep: true })

// 组件挂载时初始化
onMounted(async () => {
  try {
    console.log('首页组件开始挂载')
    formatCurrentDate()
    console.log('日期格式化完成')
    
    await loadUserInfo()
    console.log('用户信息加载完成')
    
    // 移除统计数据加载
    
    await loadWeeklyData()
    console.log('周检数据加载完成')
    
    await loadMonthlyData()
    console.log('月检数据加载完成')
    
    // 等待DOM更新后初始化图表
    await nextTick()
    console.log('DOM更新完成，开始初始化图表')
    
    // 移除月度趋势图表和问题分类图表初始化
    
    initWeeklyChart()
    console.log('周检图表初始化完成')
    
    initMonthlyChart()
    console.log('月检图表初始化完成')
    
    console.log('首页组件挂载完成')
  } catch (error) {
    console.error('首页组件挂载过程中出现错误:', error)
  }
})
</script>

<style scoped>
.home-container {
  padding: 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: calc(100vh - 60px);
  overflow-y: auto;
}

/* 欢迎区域 - 美化升级 */
.welcome-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  padding: 32px;
  margin-bottom: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: white;
  box-shadow: 0 12px 40px rgba(102, 126, 234, 0.4);
  position: relative;
  overflow: hidden;
}

.welcome-section::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
  border-radius: 50%;
}

.welcome-content {
  position: relative;
  z-index: 1;
}

.welcome-content .greeting {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 12px 0;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.welcome-content .current-date {
  font-size: 18px;
  opacity: 0.9;
  margin: 0;
  font-weight: 300;
}

.user-avatar {
  flex-shrink: 0;
  position: relative;
  z-index: 1;
}

.user-avatar .el-avatar {
  border: 3px solid rgba(255,255,255,0.3);
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
}

/* 数据展示区域美化 */
.inspection-data-section {
  display: flex;
  flex-direction: row;
  gap: 24px;
  margin-bottom: 32px;
}

.inspection-card {
  flex: 1;
  background: white;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  border: 1px solid #f0f2f5;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.inspection-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}

.inspection-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.12);
}

.inspection-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f2f5;
}

.inspection-header h3 {
  font-size: 20px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

.inspection-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.stat-item {
  text-align: center;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.stat-item:hover {
  background: #e9ecef;
  transform: translateY(-2px);
}

.stat-number {
  font-size: 24px;
  font-weight: 700;
  color: #667eea;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #6c757d;
  font-weight: 500;
}

/* 移除图表区域样式 */

.chart-header {
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f2f5;
}

.chart-header h3 {
  font-size: 20px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

.chart-content {
  height: 320px;
  width: 100%;
  border-radius: 8px;
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
    gap: 20px;
    padding: 24px;
  }
  
  .welcome-content .greeting {
    font-size: 28px;
  }
  
  .inspection-data-section {
    flex-direction: column;
    gap: 20px;
  }
  
  .inspection-card {
    padding: 20px;
  }
  
  /* 移除图表容器样式 */
  
  .chart-content {
    height: 280px;
  }
  
  .inspection-stats {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .welcome-section {
    padding: 20px;
  }
  
  .welcome-content .greeting {
    font-size: 24px;
  }
  
  .inspection-card {
    padding: 16px;
  }
  
  .chart-content {
    height: 240px;
  }
  
  .inspection-stats {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  
  .stat-item {
    padding: 12px;
  }
}
</style>