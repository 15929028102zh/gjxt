<template>
  <div class="monthly-inspection-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>月检记录录入</h2>
      <p>管理月检记录的录入、整改确认和复查确认</p>
    </div>

    <!-- 搜索栏 -->
    <div class="search-container">
      <el-form :model="searchForm" inline>
        <el-form-item label="任务编号" v-if="searchForm.taskNo">
          <el-input 
            v-model="searchForm.taskNo" 
            placeholder="任务编号" 
            readonly
            style="width: 150px;"
          />
        </el-form-item>
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
            style="width: 150px;"
            :disabled="!isAdminUser"
          >
            <el-option
              v-for="dept in departmentList"
              :key="dept.id"
              :label="dept.deptName"
              :value="dept.id"
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
            <el-option label="待整改" value="0" />
            <el-option label="已整改" value="1" />
            <el-option label="已复查" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
          <el-button @click="handleReset" :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button type="primary" @click="handleAdd" :icon="Plus" v-if="hasMonthlyPermission()">新增</el-button>
      <el-button type="success" @click="handleExport" :icon="Download">导出</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="selectedRows.length === 0" :icon="Delete" v-if="hasMonthlyPermission()">批量删除</el-button>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table 
        :data="tableData" 
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="problemDescription" label="问题描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="responsiblePerson" label="责任人" width="100" />
        <el-table-column prop="responsibleUnit" label="责任单位" width="120" />
        <el-table-column prop="inspector" label="检查人" width="100" />
        <el-table-column prop="location" label="发生部位" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="recordStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.recordStatus)">
              {{ getStatusText(row.recordStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="350" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)" :icon="View">查看</el-button>
            <el-button 
              v-if="row.recordStatus === 0 && hasMonthlyPermission()" 
              type="success" 
              size="small" 
              @click="handleRectification(row)"
              :icon="Check"
            >
              确认整改
            </el-button>
            <el-button 
              v-if="row.recordStatus === 1 && hasMonthlyPermission()" 
              type="warning" 
              size="small" 
              @click="handleReview(row)"
              :icon="CircleCheck"
            >
              确认复查
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="handleDelete(row)" 
              :icon="Delete"
              v-if="hasMonthlyPermission()"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新增/编辑弹窗 -->
    <AddDialog 
      v-model:visible="addDialogVisible" 
      :form-data="currentRow" 
      @success="handleAddSuccess" 
    />

    <!-- 确认整改弹窗 -->
    <RectificationDialog 
      v-model:visible="rectificationDialogVisible" 
      :form-data="currentRow" 
      @success="handleRectificationSuccess" 
    />

    <!-- 确认复查弹窗 -->
    <ReviewDialog 
      v-model:visible="reviewDialogVisible" 
      :form-data="currentRow" 
      @success="handleReviewSuccess" 
    />

    <!-- 查看详情弹窗 -->
    <ViewDialog 
      v-model:visible="viewDialogVisible" 
      :form-data="currentRow" 
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Download, Delete, View, Check, CircleCheck, EditPen } from '@element-plus/icons-vue'
import { hasMonthlyPermission } from '@/utils/permission'
import { monthlyRecordApi, getProfessionList, getNatureDefinitionList, getDeptList } from '@/api/monthlyRecord'
import AddDialog from './components/AddDialog.vue'
import RectificationDialog from './components/RectificationDialog.vue'
import ReviewDialog from './components/ReviewDialog.vue'
import ViewDialog from './components/ViewDialog.vue'

const route = useRoute()

// 响应式数据
const loading = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const addDialogVisible = ref(false)
const rectificationDialogVisible = ref(false)
const reviewDialogVisible = ref(false)
const viewDialogVisible = ref(false)
const currentRow = ref({})

// 搜索表单
const searchForm = reactive({
  problemDescription: '',
  responsiblePerson: '',
  professionId: '',
  natureId: '',
  departmentId: '',
  dateRange: [],
  status: '',
  taskNo: '' // 添加任务编号字段
})

// 分页数据
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 下拉选项数据
const professionOptions = ref([])
const natureOptions = ref([])
const departmentList = ref([])

// 计算属性
const isAdminUser = computed(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  return userInfo.isAdmin === 1 || userInfo.username === 'admin'
})

// 获取表格数据
const getTableData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    
    // 处理日期范围
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    delete params.dateRange

    const response = await monthlyRecordApi.getPage(params)
    if (response.code === 200) {
      tableData.value = response.data.records || response.data
      pagination.total = response.data.total || 0
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  getTableData()
}

// 重置
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    if (Array.isArray(searchForm[key])) {
      searchForm[key] = []
    } else {
      searchForm[key] = ''
    }
  })
  pagination.pageNum = 1
  getTableData()
}

// 新增记录
const handleAdd = () => {
  currentRow.value = {}
  addDialogVisible.value = true
}

// 导出
const handleExport = async () => {
  try {
    const params = { ...searchForm }
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    delete params.dateRange

    await monthlyRecordApi.exportRecords(params)
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的记录')
    return
  }

  try {
    await ElMessageBox.confirm('确定要删除选中的记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const ids = selectedRows.value.map(row => row.id)
    await monthlyRecordApi.batchDelete(ids)
    ElMessage.success('删除成功')
    getTableData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 查看详情
const handleView = async (row) => {
  try {
    loading.value = true
    // 通过API获取完整详情
    const response = await monthlyRecordApi.getById(row.id)
    if (response.code === 200) {
      currentRow.value = response.data
      viewDialogVisible.value = true
    } else {
      // 如果API失败，使用当前行数据
      currentRow.value = { ...row }
      viewDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取记录详情失败:', error)
    // API失败时使用当前行数据
    currentRow.value = { ...row }
    viewDialogVisible.value = true
  } finally {
    loading.value = false
  }
}

// 确认整改
const handleRectification = async (row) => {
  try {
    loading.value = true
    // 通过API获取完整详情
    const response = await monthlyRecordApi.getById(row.id)
    if (response.code === 200) {
      currentRow.value = response.data
      rectificationDialogVisible.value = true
    } else {
      // 如果API失败，使用当前行数据
      currentRow.value = { ...row }
      rectificationDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取记录详情失败:', error)
    // API失败时使用当前行数据
    currentRow.value = { ...row }
    rectificationDialogVisible.value = true
  } finally {
    loading.value = false
  }
}

// 确认复查
const handleReview = (row) => {
  currentRow.value = { ...row }
  reviewDialogVisible.value = true
}

// 删除记录
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await monthlyRecordApi.delete(row.id)
    ElMessage.success('删除成功')
    getTableData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.pageNum = 1
  getTableData()
}

// 当前页变化
const handleCurrentChange = (page) => {
  pagination.pageNum = page
  getTableData()
}

// 新增成功回调
const handleAddSuccess = () => {
  addDialogVisible.value = false
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

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待整改',
    1: '已整改',
    2: '已复查'
  }
  return statusMap[status] || '未知'
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    0: 'warning',
    1: 'success',
    2: 'info'
  }
  return typeMap[status] || 'info'
}

// 加载下拉选项数据
const loadOptions = async () => {
  try {
    const [professionResponse, natureResponse, deptResponse] = await Promise.all([
      getProfessionList(),
      getNatureDefinitionList(),
      getDeptList()
    ])

    if (professionResponse.code === 200) {
      professionOptions.value = professionResponse.data
    }

    if (natureResponse.code === 200) {
      natureOptions.value = natureResponse.data
    }

    if (deptResponse.code === 200) {
      departmentList.value = deptResponse.data
    }
  } catch (error) {
    console.error('加载选项数据失败:', error)
  }
}

// 组件挂载时加载数据
onMounted(() => {
  // 检查是否有任务ID参数
  const taskId = route.query.taskId
  if (taskId) {
    // 如果有任务ID，可以预填充相关信息或者显示任务相关的记录
    searchForm.taskNo = taskId
    ElMessage.info(`正在加载任务 ${taskId} 相关的月检记录`)
  }
  
  getTableData()
  loadOptions()
})
</script>

<style scoped>
.monthly-inspection-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 20px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.search-container {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.action-buttons {
  margin-bottom: 20px;
  display: flex;
  gap: 12px;
}

.table-container {
  margin-bottom: 20px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
}
</style>