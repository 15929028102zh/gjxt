<template>
  <div class="dashboard">
    <div class="page-container">
      <div class="page-header">
        <h1 class="page-title">系统概览</h1>
      </div>
      
      <!-- 统计卡片 -->
      <el-row :gutter="20" class="stats-row">
        <el-col :span="6">
          <el-card class="stat-card" @click="goToPendingIssues">
            <div class="stat-content">
              <div class="stat-icon pending">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.pendingCount }}</div>
                <div class="stat-label">待整改</div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stat-card" @click="goToCompletedIssues">
            <div class="stat-content">
              <div class="stat-icon completed">
                <el-icon><Check /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.completedCount }}</div>
                <div class="stat-label">已整改</div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stat-card" @click="goToReviewedRecords">
            <div class="stat-content">
              <div class="stat-icon reviewed">
                <el-icon><CircleCheck /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.reviewedCount }}</div>
                <div class="stat-label">已复查</div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon online">
                <el-icon><Connection /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.onlineCount }}</div>
                <div class="stat-label">在线用户</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 快捷操作 -->
      <el-row :gutter="20" class="quick-actions">
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>快捷操作</span>
            </template>
            <div class="action-grid">
              <div class="action-item" @click="$router.push('/system/user')">
                <el-icon><User /></el-icon>
                <span>用户管理</span>
              </div>
              <div class="action-item" @click="$router.push('/system/dept')">
                <el-icon><OfficeBuilding /></el-icon>
                <span>部门管理</span>
              </div>
              <div class="action-item" @click="$router.push('/system/dict')">
                <el-icon><Collection /></el-icon>
                <span>字典管理</span>
              </div>
              <div class="action-item">
                <el-icon><Setting /></el-icon>
                <span>系统设置</span>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>系统信息</span>
            </template>
            <div class="system-info">
              <div class="info-item">
                <span class="label">系统版本：</span>
                <span class="value">v1.0.0</span>
              </div>
              <div class="info-item">
                <span class="label">运行环境：</span>
                <span class="value">Java 1.8</span>
              </div>
              <div class="info-item">
                <span class="label">数据库：</span>
                <span class="value">MySQL 8.0</span>
              </div>
              <div class="info-item">
                <span class="label">服务器：</span>
                <span class="value">Spring Boot</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { monthlyInspectionRecordApi } from '@/api/monthlyRecord'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 统计数据
const stats = ref({
  pendingCount: 0,
  completedCount: 0,
  reviewedCount: 0,
  onlineCount: 0
})

// 加载统计数据
const loadStats = async () => {
  try {
    // 获取用户信息，判断是否为管理员
    const userInfo = userStore.userInfo
    const isAdmin = userInfo?.isAdmin === 1
    const departmentId = userInfo?.deptId
    
    // 构建查询参数 - 管理员可以看所有数据，非管理员只能看本部门数据
    const baseParams = { current: 1, size: 1 }
    const deptParams = (!isAdmin && departmentId) ? { ...baseParams, departmentId } : baseParams
    
    // 并行加载各项统计数据
    const [pendingResponse, completedResponse, reviewedResponse] = await Promise.all([
      monthlyInspectionRecordApi.getPage({ ...deptParams, status: 0 }).catch(() => ({ data: { total: 0 } })),
      monthlyInspectionRecordApi.getPage({ ...deptParams, status: 1 }).catch(() => ({ data: { total: 0 } })),
      monthlyInspectionRecordApi.getPage({ ...deptParams, status: 2 }).catch(() => ({ data: { total: 0 } }))
    ])
    
    stats.value = {
      pendingCount: pendingResponse.data?.total || 0,
      completedCount: completedResponse.data?.total || 0,
      reviewedCount: reviewedResponse.data?.total || 0,
      onlineCount: Math.floor(Math.random() * 50) + 10 // 模拟在线用户数
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
    ElMessage.error('加载统计数据失败')
    // 使用默认值
    stats.value = {
      pendingCount: 0,
      completedCount: 0,
      reviewedCount: 0,
      onlineCount: 0
    }
  }
}

// 跳转到待整改记录
const goToPendingIssues = () => {
  router.push('/monthly/record?status=0')
}

// 跳转到已整改记录
const goToCompletedIssues = () => {
  router.push('/monthly/record?status=1')
}

// 跳转到已复查记录
const goToReviewedRecords = () => {
  router.push('/monthly/record?status=2')
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.stat-icon.pending {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
}

.stat-icon.completed {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
}

.stat-icon.reviewed {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.online {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.quick-actions {
  margin-top: 20px;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.action-item:hover {
  border-color: #409eff;
  background-color: #f0f9ff;
}

.action-item .el-icon {
  font-size: 24px;
  color: #409eff;
  margin-bottom: 8px;
}

.action-item span {
  font-size: 14px;
  color: #606266;
}

.system-info {
  padding: 10px 0;
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.label {
  color: #909399;
  font-size: 14px;
}

.value {
  color: #303133;
  font-size: 14px;
  font-weight: 500;
}
</style>