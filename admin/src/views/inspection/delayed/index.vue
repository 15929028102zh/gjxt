<template>
  <div class="delayed-rectification-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>延迟整改</h2>
      <p>查询记录创建时间超过整改期限的记录</p>
    </div>

    <!-- 搜索栏 -->
    <div class="search-container">
      <el-form :model="searchForm" inline>
        <el-form-item label="问题描述">
          <el-input 
            v-model="searchForm.problemDescription" 
            placeholder="请输入问题描述" 
            clearable 
            style="width: 200px;"
          />
        </el-form-item>
        <el-form-item label="责任人">
          <el-input 
            v-model="searchForm.responsiblePerson" 
            placeholder="请输入责任人" 
            clearable 
            style="width: 150px;"
          />
        </el-form-item>
        <el-form-item label="专业">
          <el-select 
            v-model="searchForm.professionId" 
            placeholder="请选择专业" 
            clearable 
            style="width: 120px;"
          >
            <el-option
              v-for="item in professionOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="性质界定">
          <el-select 
            v-model="searchForm.natureId" 
            placeholder="请选择性质界定" 
            clearable 
            style="width: 120px;"
          >
            <el-option
              v-for="item in natureOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属部门">
          <el-select 
            v-model="searchForm.departmentId" 
            placeholder="请选择部门" 
            clearable 
            style="width: 120px;"
            :disabled="!isAdminUser"
          >
            <el-option
              v-for="item in departmentOptions"
              :key="item.id"
              :label="item.deptName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            clearable
            style="width: 240px;"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择状态" 
            clearable 
            style="width: 120px;"
          >
            <el-option label="待整改" :value="0" />
            <el-option label="已整改" :value="1" />
            <el-option label="已复查" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
          <el-button @click="handleReset" :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <div class="action-container">
      <el-button 
        type="success" 
        @click="handleExport" 
        :icon="Download"
        :loading="exportLoading"
      >
        导出Excel
      </el-button>
      <el-button 
        v-if="hasWeeklyPermission()"
        type="danger" 
        @click="handleBatchDelete" 
        :disabled="selectedIds.length === 0"
        :icon="Delete"
      >
        批量删除
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-table 
      :data="tableData" 
      v-loading="loading" 
      stripe 
      @selection-change="handleSelectionChange"
      style="width: 100%;"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="problemDescription" label="问题描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="responsiblePerson" label="责任人" width="100" />
      <el-table-column prop="responsibleUnit" label="责任单位" width="120" />
      <el-table-column prop="inspector" label="检查人" width="100" />
      <el-table-column prop="location" label="发生部位" width="120" />
      <el-table-column prop="createTime" label="创建时间" width="160">
        <template #default="{ row }">
          {{ formatDate(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="rectificationDeadline" label="整改期限" width="160">
        <template #default="{ row }">
          {{ formatDate(row.rectificationDeadline) }}
        </template>
      </el-table-column>
      <el-table-column label="延迟天数" width="100">
        <template #default="{ row }">
          <el-tag type="danger">
            {{ getDelayedDays(row.rectificationDeadline) }}天
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="320" fixed="right">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button 
              size="small" 
              @click="handleView(row)" 
              :icon="View"
              class="action-btn view-btn"
            >
              查看
            </el-button>
            <el-button 
              size="small" 
              type="warning" 
              @click="handleRectification(row)" 
              :disabled="row.status !== 0"
              :icon="EditPen"
              class="action-btn rectify-btn"
            >
              确认整改
            </el-button>
            <el-button 
              size="small" 
              type="success" 
              @click="handleReview(row)" 
              :disabled="row.status !== 1"
              :icon="CircleCheck"
              class="action-btn review-btn"
            >
              确认复查
            </el-button>
            <el-button 
              v-if="hasWeeklyPermission()"
              size="small" 
              type="danger" 
              @click="handleDelete(row)" 
              :icon="Delete"
              class="action-btn delete-btn"
            >
              删除
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="pagination.current"
      v-model:page-size="pagination.size"
      :page-sizes="[10, 20, 50, 100]"
      :total="pagination.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      style="margin-top: 20px; text-align: right;"
    />

    <!-- 确认整改弹窗 -->
    <RectificationDialog 
      v-model:visible="rectificationDialogVisible" 
      :form-data="currentRecord"
      @success="handleRectificationSuccess"
    />

    <!-- 确认复查弹窗 -->
    <ReviewDialog 
      v-model:visible="reviewDialogVisible" 
      :form-data="currentRecord"
      @success="handleReviewSuccess"
    />

    <!-- 查看详情弹窗 -->
    <ViewDialog 
      v-model:visible="viewDialogVisible" 
      :form-data="currentRecord"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Search, 
  Refresh, 
  Delete, 
  View, 
  EditPen, 
  CircleCheck,
  Download 
} from '@element-plus/icons-vue'
import { inspectionApi, getDictByType, getDeptList } from '@/api/inspection'
import { hasWeeklyPermission } from '@/utils/permission'
import RectificationDialog from '../weekly/components/RectificationDialog.vue'
import ReviewDialog from '../weekly/components/ReviewDialog.vue'
import ViewDialog from '../weekly/components/ViewDialog.vue'

// 响应式数据
const loading = ref(false)
const exportLoading = ref(false)
const tableData = ref([])
const selectedIds = ref([])
const rectificationDialogVisible = ref(false)
const reviewDialogVisible = ref(false)
const viewDialogVisible = ref(false)
const currentRecord = ref({})

// 搜索表单
const searchForm = reactive({
  problemDescription: '',
  responsiblePerson: '',
  professionId: '',
  natureId: '',
  departmentId: '',
  dateRange: [],
  status: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 下拉选项数据
const professionOptions = ref([])
const natureOptions = ref([])
const departmentOptions = ref([])

// 用户权限
const isAdminUser = ref(false)

// 加载下拉选项数据
const loadOptions = async () => {
  try {
    // 加载专业选项
    const professionResponse = await getDictByType('专业')
    if (professionResponse.code === 200) {
      professionOptions.value = professionResponse.data.map(item => ({
        id: item.id,
        name: item.dictValue || item.dictLabel
      }))
    }

    // 加载性质界定选项
    const natureResponse = await getDictByType('性质界定')
    if (natureResponse.code === 200) {
      natureOptions.value = natureResponse.data.map(item => ({
        id: item.id,
        name: item.dictValue || item.dictLabel
      }))
    }

    // 加载部门选项
    const departmentResponse = await getDeptList()
    if (departmentResponse.code === 200) {
      departmentOptions.value = departmentResponse.data || []
    }
  } catch (error) {
    console.error('加载选项数据失败:', error)
  }
}

// 获取表格数据 - 调用延迟整改接口
const getTableData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      ...searchForm
    }
    
    // 处理时间范围参数
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startTime = searchForm.dateRange[0]
      params.endTime = searchForm.dateRange[1]
    }
    delete params.dateRange
    
    // 调用延迟整改接口
    const response = await inspectionApi.getDelayedRecords(params)
    if (response.code === 200) {
      tableData.value = response.data.records || []
      pagination.total = response.data.total || 0
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取表格数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getTableData()
}

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    problemDescription: '',
    responsiblePerson: '',
    professionId: '',
    natureId: '',
    departmentId: '',
    dateRange: [],
    status: ''
  })
  // 非管理员用户重置后重新设置部门
  if (!isAdminUser.value) {
    getCurrentUserDepartment()
  }
  pagination.current = 1
  getTableData()
}

// 查看详情
const handleView = async (row) => {
  try {
    loading.value = true
    // 通过API获取完整详情
    const response = await inspectionApi.getById(row.id)
    if (response.code === 200) {
      currentRecord.value = response.data
      viewDialogVisible.value = true
    } else {
      // 如果API失败，使用当前行数据
      currentRecord.value = { ...row }
      viewDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取详情失败:', error)
    // API失败时使用当前行数据
    currentRecord.value = { ...row }
    viewDialogVisible.value = true
  } finally {
    loading.value = false
  }
}

// 确认整改
const handleRectification = (row) => {
  currentRecord.value = { ...row }
  rectificationDialogVisible.value = true
}

// 确认复查
const handleReview = (row) => {
  currentRecord.value = { ...row }
  reviewDialogVisible.value = true
}

// 删除记录
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除问题"${row.problemDescription}"吗？此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await inspectionApi.delete(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      getTableData()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除记录失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要删除的记录')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.value.length} 条记录吗？此操作不可恢复。`,
      '确认批量删除',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await inspectionApi.batchDelete(selectedIds.value)
    if (response.code === 200) {
      ElMessage.success('批量删除成功')
      selectedIds.value = []
      getTableData()
    } else {
      ElMessage.error(response.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 导出Excel
const handleExport = async () => {
  try {
    exportLoading.value = true
    
    // 构建导出参数，使用当前搜索条件
    const params = {
      problemDescription: searchForm.problemDescription || undefined,
      responsiblePerson: searchForm.responsiblePerson || undefined,
      professionId: searchForm.professionId || undefined,
      natureId: searchForm.natureId || undefined,
      departmentId: searchForm.departmentId || undefined,
      status: searchForm.status !== '' && searchForm.status !== null && searchForm.status !== undefined ? searchForm.status : undefined
    }
    
    // 处理时间范围参数
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startTime = searchForm.dateRange[0]
      params.endTime = searchForm.dateRange[1]
    }
    
    // 添加延迟整改标识
    params.delayed = true
    
    const response = await inspectionApi.exportRecords(params)
    
    // 创建下载链接
    const blob = new Blob([response], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    
    // 生成文件名
    const now = new Date()
    const dateStr = now.toISOString().slice(0, 10).replace(/-/g, '')
    const timeStr = now.toTimeString().slice(0, 8).replace(/:/g, '')
    link.download = `延迟整改记录_${dateStr}_${timeStr}.xlsx`
    
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败，请稍后重试')
  } finally {
    exportLoading.value = false
  }
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  getTableData()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  getTableData()
}

// 整改成功回调
const handleRectificationSuccess = () => {
  rectificationDialogVisible.value = false
  getTableData()
}

// 复查成功回调
const handleReviewSuccess = () => {
  reviewDialogVisible.value = false
  getTableData()
}

// 获取状态类型
const getStatusType = (status) => {
  const statusMap = {
    0: 'warning',
    1: 'success',
    2: 'info'
  }
  return statusMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待整改',
    1: '已整改',
    2: '已复查'
  }
  return statusMap[status] || '未知'
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

// 计算延迟天数
const getDelayedDays = (deadline) => {
  if (!deadline) return 0
  const deadlineDate = new Date(deadline)
  const currentDate = new Date()
  const diffTime = currentDate - deadlineDate
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  return diffDays > 0 ? diffDays : 0
}

// 获取当前用户部门信息
const getCurrentUserDepartment = () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    // 判断是否为管理员
    isAdminUser.value = userInfo.isAdmin === 1
    
    // 非管理员用户自动设置部门
    if (!isAdminUser.value && userInfo.deptId) {
      searchForm.departmentId = userInfo.deptId
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 页面加载时获取数据
onMounted(() => {
  getCurrentUserDepartment()
  loadOptions()
  getTableData()
})
</script>

<style scoped>
.delayed-rectification-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
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

.search-container {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.action-container {
  margin-bottom: 20px;
}

.el-table {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 操作按钮样式 */
.action-buttons {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 6px;
  flex-wrap: nowrap;
}

.action-btn {
  min-width: 60px;
  height: 28px;
  padding: 0 8px;
  font-size: 12px;
  border-radius: 4px;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.view-btn {
  background: #f0f9ff;
  border-color: #0ea5e9;
  color: #0ea5e9;
}

.view-btn:hover {
  background: #0ea5e9;
  color: #fff;
}

.rectify-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.review-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.delete-btn {
  min-width: 50px;
}

/* 响应式设计 */
@media (max-width: 1400px) {
  .action-buttons {
    flex-wrap: wrap;
    gap: 4px;
  }
  
  .action-btn {
    min-width: 55px;
    font-size: 11px;
    padding: 0 6px;
  }
}

@media (max-width: 1200px) {
  .action-buttons {
    flex-direction: column;
    gap: 3px;
  }
  
  .action-btn {
    width: 100%;
    min-width: auto;
  }
}
</style>