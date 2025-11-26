<template>
  <div class="audit-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>月棂待审核</h2>
      <p>管理月棂审核申请记录</p>
    </div>

    <!-- 查询条件卡片 -->
    <el-card class="search-card">
      <template #header>
        <div class="card-header">
          <span>查询条件</span>
        </div>
      </template>
      <el-row :gutter="20" style="margin-bottom: 0">
        <el-col :xs="24" :sm="12" :md="6">
          <div class="search-item">
            <label>问题描述</label>
            <el-input
              v-model="queryParams.problemDescription"
              placeholder="请输入问题描述"
              clearable
            />
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="search-item">
            <label>所属部门</label>
            <el-select
              v-model="queryParams.departmentId"
              placeholder="请选择部门"
              clearable
            >
              <el-option
                v-for="dept in departmentList"
                :key="dept.id"
                :label="dept.deptName"
                :value="dept.id"
              />
            </el-select>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="search-item">
            <label>开始日期</label>
            <el-date-picker
              v-model="queryParams.startDate"
              type="date"
              placeholder="请选择开始日期"
              clearable
            />
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="search-item">
            <label>结束日期</label>
            <el-date-picker
              v-model="queryParams.endDate"
              type="date"
              placeholder="请选择结束日期"
              clearable
            />
          </div>
        </el-col>
      </el-row>
      <el-row style="margin-top: 15px">
        <el-col :span="24">
          <div class="search-buttons">
            <el-button type="primary" @click="handleSearch" size="default">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button @click="handleReset" size="default">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>待审核记录（共 {{ pagination.total }} 条）</span>
        </div>
      </template>
      <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading" class="audit-table">
        <el-table-column prop="recordNo" label="记录编号" width="140" />
        <el-table-column prop="responsibilityName" label="部门" width="140" />
        <el-table-column prop="problemDescription" label="问题描述" min-width="250" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleAudit(scope.row)">审核</el-button>
            <el-button type="danger" size="small" @click="handleReject(scope.row)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSearch"
          @current-change="handleSearch"
        />
      </div>
    </el-card>

    <!-- 审核对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      title="审核记录" 
      width="550px"
      @close="auditForm.auditReason = ''"
    >
      <el-alert 
        title="此操作将会记录为已提交状态"
        type="info"
        :closable="false"
        style="margin-bottom: 20px"
      />
      <el-form :model="auditForm" label-width="100px">
        <el-form-item label="问题描述" v-if="currentRecord">
          <el-text type="info">{{ currentRecord.problemDescription }}</el-text>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input
            v-model="auditForm.auditReason"
            type="textarea"
            rows="5"
            placeholder="请输入审核意见"
            show-word-limit
            maxlength="200"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmAudit">确认审核</el-button>
      </template>
    </el-dialog>

    <!-- 拒绝对话框 -->
    <el-dialog 
      v-model="rejectDialogVisible" 
      title="拒绝审核申请" 
      width="550px"
      @close="rejectForm.rejectReason = ''"
    >
      <el-alert 
        title="拒绝后该记录将被删除"
        type="warning"
        :closable="false"
        style="margin-bottom: 20px"
      />
      <el-form :model="rejectForm" label-width="100px">
        <el-form-item label="问题描述" v-if="currentRecord">
          <el-text type="info">{{ currentRecord.problemDescription }}</el-text>
        </el-form-item>
        <el-form-item label="拒绝原因">
          <el-input
            v-model="rejectForm.rejectReason"
            type="textarea"
            rows="5"
            placeholder="请输入拒绝原因"
            show-word-limit
            maxlength="200"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="handleConfirmReject">确认拒绝</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { monthlyRecordApi, getDeptList } from '@/api/monthlyRecord'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const rejectDialogVisible = ref(false)
const currentRecord = ref(null)
const departmentList = ref([])

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const queryParams = reactive({
  problemDescription: '',
  departmentId: '',
  startDate: null,
  endDate: null
})

const auditForm = reactive({
  auditReason: ''
})

const rejectForm = reactive({
  rejectReason: ''
})

const handleSearch = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      problemDescription: queryParams.problemDescription || undefined,
      departmentId: queryParams.departmentId || undefined,
      startDate: queryParams.startDate ? new Date(queryParams.startDate).toISOString().split('T')[0] : undefined,
      endDate: queryParams.endDate ? new Date(queryParams.endDate).toISOString().split('T')[0] : undefined
    }
    console.log('查询参数:', params)
    const response = await monthlyRecordApi.getAuditRecords(params)
    console.log('查询结果:', response)
    if (response.data) {
      tableData.value = response.data.records || response.data.data || []
      pagination.total = response.data.total || 0
      console.log('表格数据:', tableData.value)
      if (tableData.value.length === 0) {
        ElMessage.info('暂无待审核记录')
      }
    }
  } catch (error) {
    console.error('查询失败详情:', error)
    ElMessage.error('查询失败: ' + (error.message || ''))
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.problemDescription = ''
  queryParams.departmentId = ''
  queryParams.startDate = null
  queryParams.endDate = null
  pagination.current = 1
  handleSearch()
}

const handleAudit = (record) => {
  currentRecord.value = record
  auditForm.auditReason = ''
  dialogVisible.value = true
}

const handleReject = (record) => {
  currentRecord.value = record
  rejectForm.rejectReason = ''
  rejectDialogVisible.value = true
}

const handleConfirmAudit = async () => {
  if (!currentRecord.value) return
  
  try {
    // 多种方式尝试获取userId
    let userId = localStorage.getItem('userId')
    console.log('localStorage userId:', userId)
    
    // 如果没有，尝试从userInfo对象中获取
    if (!userId) {
      const userInfoStr = localStorage.getItem('userInfo')
      console.log('localStorage userInfo:', userInfoStr)
      if (userInfoStr) {
        try {
          const userInfo = JSON.parse(userInfoStr)
          userId = userInfo.id || userInfo.userId
          console.log('从userInfo中提取的userId:', userId)
        } catch (e) {
          console.error('解析userInfo失败:', e)
        }
      }
    }
    
    // 如果还是没有，尝试其他可能的key
    if (!userId) {
      userId = localStorage.getItem('user_id') || localStorage.getItem('id')
      console.log('从其他key获取的userId:', userId)
    }
    
    if (!userId) {
      ElMessage.error('无法获取当前用户信息，请重新登录')
      return
    }
    
    const response = await monthlyRecordApi.confirmAudit(currentRecord.value.id, {
      auditorId: userId,
      auditReason: auditForm.auditReason
    })
    if (response.code === 200 || response.code === 0 || response.success) {
      ElMessage.success('审核成功')
      dialogVisible.value = false
      handleSearch()
    } else {
      ElMessage.error(response.message || '审核失败')
    }
  } catch (error) {
    ElMessage.error('审核失败: ' + (error.message || ''))
  }
}

const handleConfirmReject = async () => {
  if (!currentRecord.value) return
  
  try {
    await ElMessageBox.confirm(
      `确定要拒绝这条审核申请吗？拒绝后沈棂记录将保留，仅删除待审核申请。`,
      '拒绝确认',
      {
        confirmButtonText: '拒绝',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 拒绝审核，将记录章态从待审核(3)改回已提交(1)
    const response = await monthlyRecordApi.rejectAudit(currentRecord.value.id)
    if (response.code === 200 || response.success) {
      ElMessage.success('拒绝成功')
      rejectDialogVisible.value = false
      handleSearch()
    } else {
      ElMessage.error(response.message || '拒绝失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('拒绝失败:', error)
      ElMessage.error('拒绝失败')
    }
  }
}


const loadDepartments = async () => {
  try {
    const response = await getDeptList()
    if (response.code === 200) {
      departmentList.value = response.data || []
    }
  } catch (error) {
    console.error('加载部门列表失败:', error)
  }
}

onMounted(() => {
  loadDepartments()
  handleSearch()
})
</script>

<style scoped>
.audit-container {
  padding: 20px;
  background-color: #f5f7fa;
}

.page-header {
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 2px solid #409eff;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.search-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 6px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  color: #303133;
}

.search-item {
  display: flex;
  flex-direction: column;
}

.search-item label {
  font-size: 12px;
  color: #606266;
  margin-bottom: 8px;
  font-weight: 500;
}

.search-buttons {
  display: flex;
  gap: 10px;
}

.search-buttons .el-button {
  min-width: 100px;
}

.table-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 6px;
}

.audit-table .el-table__header th {
  background-color: #f0f2f5;
  font-weight: 600;
}

.audit-table .el-table__row:hover > td {
  background-color: #f5f7fa !important;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
}
</style>
