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
        <el-form-item label="月检任务">
          <el-select 
            v-model="searchForm.taskId" 
            placeholder="请选择月检任务" 
            clearable 
            style="width: 200px;"
          >
            <el-option
              v-for="task in taskOptions"
              :key="task.id"
              :label="task.taskName"
              :value="task.id"
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
        <el-table-column prop="taskNo" label="任务编号" width="150" show-overflow-tooltip>
          <template #default="{ row }">
            {{ formatTaskNo(row.taskNo) }}
          </template>
        </el-table-column>
        <el-table-column prop="problemDescription" label="问题描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="responsiblePerson" label="责任人" width="100" />
        <el-table-column prop="responsibleUnit" label="责任单位" width="120" />
        <el-table-column prop="inspector" label="检查人" width="100" />
        <el-table-column prop="location" label="发生部位" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="430" fixed="right">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.recordStatus)" style="margin-right: 8px;">
              {{ getStatusText(row.recordStatus) }}
            </el-tag>
            <el-button type="primary" size="small" @click="handleView(row)" :icon="View">查看</el-button>
            <el-button 
              v-if="row.recordStatus === 0 && hasMonthlyPermission()" 
              type="success" 
              size="small" 
              @click="handleRectificationConfirm(row)"
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
              type="info" 
              size="small" 
              @click="handleReject(row)" 
              :icon="Delete"
            >
              回退
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
    <RectificationConfirmDialog 
      v-model:visible="rectificationConfirmDialogVisible" 
      :form-data="currentRow" 
      @success="handleRectificationConfirmSuccess" 
    />

    <!-- 确认整改弹窗（旧版） -->
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
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Download, Delete, View, Check, CircleCheck, EditPen } from '@element-plus/icons-vue'
import { hasMonthlyPermission, isAdmin as isAdminPermission } from '@/utils/permission'
import { monthlyRecordApi, getProfessionList, getNatureDefinitionList, getDeptList, monthlyTaskApi, rejectRecord } from '@/api/monthlyRecord'
import { gjbzDataApi } from '@/api/gjbz'
import { useWebSocketStore } from '@/stores/websocket'
import AddDialog from './components/AddDialog.vue'
import RectificationConfirmDialog from './components/RectificationConfirmDialog.vue'
import RectificationDialog from './components/RectificationDialog.vue'
import ReviewDialog from './components/ReviewDialog.vue'
import ViewDialog from './components/ViewDialog.vue'

const route = useRoute()
const router = useRouter()

// 响应式数据
const loading = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const addDialogVisible = ref(false)
const taskSelectVisible = ref(false)
const taskSelectLoading = ref(false)
const taskSelectList = ref([])
const selectedTaskId = ref('')
const rectificationConfirmDialogVisible = ref(false)
const rectificationDialogVisible = ref(false)
const reviewDialogVisible = ref(false)
const viewDialogVisible = ref(false)
const currentRow = ref({})
const webSocketStore = useWebSocketStore()

// 搜索表单
const searchForm = reactive({
  problemDescription: '',
  responsiblePerson: '',
  professionId: '',
  natureId: '',
  departmentId: '',
  dateRange: [],
  status: '',
  taskNo: '', // 添加任务编号字段
  taskId: '' // 添加月检任务ID字段
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
const taskOptions = ref([])

// 计算属性
const isAdminUser = computed(() => {
  return isAdminPermission()
})

// 获取表格数据
const getTableData = async () => {
  loading.value = true
  try {
    const statusRaw = searchForm.status
    const statusNum = statusRaw === '' || statusRaw == null ? undefined : Number(statusRaw)
    const params = {
      current: pagination.pageNum,
      size: pagination.pageSize,
      taskId: searchForm.taskId || undefined,
      taskNo: searchForm.taskNo || undefined,
      professionalTypeId: searchForm.professionId || undefined,
      responsibilityId: searchForm.departmentId || undefined,
      recordStatus: statusNum == null || Number.isNaN(statusNum) ? undefined : statusNum,
      responsiblePerson: searchForm.responsiblePerson || undefined,
      inspector: searchForm.inspector || undefined
    }
    
    // 管理员默认查询状态为0(待整改)、1(已整改)、2(已复查)的记录，除非用户主动选择了其他状态
    if (isAdminUser.value && statusNum === undefined) {
      params.recordStatus = [0, 1, 2]
    }
    
    // 非管理员，需要填补部门ID参数
    if (!isAdminUser.value && !searchForm.departmentId) {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      if (userInfo.deptId) {
        params.departmentId = userInfo.deptId
      }
    } else if (searchForm.departmentId) {
      params.departmentId = searchForm.departmentId
    }
    
    // 处理日期范围
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    delete params.dateRange

    const response = await monthlyRecordApi.getPage(params)
    if (response.code === 200) {
      const records = response.data.records || response.data || []
      // 兜底补齐行数据：任务编号、责任单位、责任人
      const enrich = (row) => {
        // 任务编号补齐：优先后端返回，其次通过已加载的任务选项匹配
        if (!row.taskNo && row.taskId && Array.isArray(taskOptions.value) && taskOptions.value.length > 0) {
          const t = taskOptions.value.find(tk => tk.id === row.taskId)
          if (t) row.taskNo = t.taskNo
        }
        // 责任单位与责任人兜底（保持字段名一致）
        row.responsibleUnit = row.responsibleUnit || row.responsibilityUnit || ''
        row.responsiblePerson = row.responsiblePerson || row.dutyPerson || ''
        return row
      }
      tableData.value = (records || []).map(enrich)
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
const handleAdd = async () => {
  if (!searchForm.taskId) {
    ElMessage.warning('请先从下拉框中选择月检任务')
    return
  }
  
  // 从后台获取完整的任务信息
  try {
    loading.value = true
    const response = await monthlyTaskApi.getById(searchForm.taskId)
    console.log('\u4ece后台获取的任务信息:', response.data)
    if (response.code === 200 && response.data) {
      const task = response.data
      console.log('task.professionId:', task.professionId, '\u7c7b\u578b:', typeof task.professionId)
      console.log('task.professionName:', task.professionName, '\u7c7b\u578b:', typeof task.professionName)
      console.log('task.professionalTypeId:', task.professionalTypeId, '\u7c7b\u578b:', typeof task.professionalTypeId)
      console.log('task.professionalTypeName:', task.professionalTypeName, '\u7c7b\u578b:', typeof task.professionalTypeName)
      // 用完整的任务信息初始化对话框
      let professionalTypeName = task.professionName || task.professionalTypeName || ''
      // 如果professionName是数字，从下professionOptions中查找professionId对应的专业名称
      if (professionalTypeName && /^\d+$/.test(String(professionalTypeName))) {
        // professionName是数字，根据数字查找professionOptions中的专业名称
        const profOption = professionOptions.value[parseInt(professionalTypeName) - 1]
        if (profOption) {
          professionalTypeName = profOption.name || profOption.label
          console.log('根据数字下标查找到专业名称:', professionalTypeName)
        }
      }
      
      currentRow.value = {
        taskId: task.id,
        taskNo: task.taskNo,
        taskName: task.taskName,
        departmentName: task.departmentName || '',
        departmentId: task.departmentId || '',
        groupLeaderName: task.groupLeaderName || '',
        deputyLeaderName: task.deputyLeaderName || '',
        professionalTypeId: task.professionId || task.professionalTypeId || '',
        professionalTypeName
      }
      addDialogVisible.value = true
    } else {
      ElMessage.error('获取任务信息失败')
    }
  } catch (error) {
    console.error('获取任务详情失败:', error)
    // 回退到从下拉框查找
    const selectedTask = taskOptions.value.find(task => task.id === searchForm.taskId)
    if (selectedTask) {
      currentRow.value = {
        taskId: selectedTask.id,
        taskNo: selectedTask.taskNo,
        taskName: selectedTask.taskName,
        departmentName: selectedTask.departmentName || '',
        groupLeaderName: selectedTask.groupLeaderName || '',
        deputyLeaderName: selectedTask.deputyLeaderName || ''
      }
      addDialogVisible.value = true
    } else {
      ElMessage.error('任务信息不存在')
    }
  } finally {
    loading.value = false
  }
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

// 确认整改（新版）
const handleRectificationConfirm = async (row) => {
  try {
    loading.value = true
    // 通过API获取完整详情
    const response = await monthlyRecordApi.getById(row.id)
    if (response.code === 200) {
      currentRow.value = response.data
      rectificationConfirmDialogVisible.value = true
    } else {
      // 如果API失败，使用当前行数据
      currentRow.value = { ...row }
      rectificationConfirmDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取记录详情失败:', error)
    // API失败时使用当前行数据
    currentRow.value = { ...row }
    rectificationConfirmDialogVisible.value = true
  } finally {
    loading.value = false
  }
}

// 确认整改（旧版）
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

// 回退记录
const handleReject = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要回退问题"${row.problemDescription}"吗？回退后将发送审核通知到部门管理员。`,
      '回退确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await rejectRecord(row.id)
    if (response.code === 200) {
      ElMessage.success('回退成功，已发送审核通知')
      getTableData()
    } else {
      ElMessage.error(response.message || '回退失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('回退失败:', error)
      ElMessage.error('回退失败')
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

// 整改确认成功回调
const handleRectificationConfirmSuccess = () => {
  rectificationConfirmDialogVisible.value = false
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
    const [professionResponse, natureResponse, deptResponse, taskResponse] = await Promise.all([
      gjbzDataApi.getTypes(),
      getNatureDefinitionList(),
      getDeptList(),
      monthlyTaskApi.getPage({ pageNum: 1, pageSize: 1000 }) // 获取所有月检任务
    ])

    if (professionResponse.code === 200) {
      // 使用与移动端相同的解析方式
      const data = professionResponse.data
      if (data && Array.isArray(data)) {
        professionOptions.value = data.map((item, index) => ({
          id: (index + 1).toString(),
          name: item
        }))
      }
    }

    if (natureResponse.code === 200) {
      natureOptions.value = natureResponse.data
    }

    if (deptResponse.code === 200) {
      departmentList.value = deptResponse.data
    }

    if (taskResponse.code === 200) {
      // 格式化任务名称为 "xx月月检任务"，从任务编号中提取月份
      taskOptions.value = taskResponse.data.records.map(task => {
        // 从任务编号中提取月份，任务编号格式通常为 YJ20250902171122420
        // 截取前10位进行转换：YJ20250902 -> 2025年09月02日
        let month = ''
        if (task.taskNo && task.taskNo.length >= 10) {
          const dateStr = task.taskNo.substring(2, 10) // 提取20250902
          if (dateStr.length === 8) {
            const year = dateStr.substring(0, 4)
            const monthNum = dateStr.substring(4, 6)
            const day = dateStr.substring(6, 8)
            month = `${parseInt(monthNum)}` // 提取月份数字，去掉前导0
          }
        }
        
        return {
          id: task.id,
          taskName: month ? `${month}月月检任务` : (task.taskName || '月检任务'),
          originalName: task.taskName,
          taskNo: task.taskNo,
          month: month
        }
      })
    }
  } catch (error) {
    console.error('加载选项数据失败:', error)
  }
}

// 格式化任务编号，将日期部分转换为月份显示
const formatTaskNo = (taskNo) => {
  if (!taskNo) return taskNo
  
  // 匹配任务编号中的日期部分 (YJ + 8位日期 + 其他)
  const match = taskNo.match(/^(YJ)(\d{4})(\d{2})(\d{2})(.*)$/)
  if (match) {
    const [, prefix, year, month, day, suffix] = match
    const monthNum = parseInt(month, 10)
    return `${prefix}${year}年${monthNum}月${day}日${suffix}`
  }
  
  return taskNo
}

// 组件挂载时加载数据
onMounted(() => {
  // 检查是否有任务ID参数
  const taskId = route.query.taskId
  if (taskId) {
    // 如果有任务ID，设置到搜索表单的taskId字段
    searchForm.taskId = taskId
    ElMessage.info(`正在加载任务ID ${taskId} 相关的月检记录`)
  }
  // 先加载选项，确保任务选项可用于行数据兔底
  loadOptions().then(() => {
    // 加载选项后，子判断是否是管理员
    if (!isAdminUser.value) {
      // 非管理员，一自根据简杆用户信息获取部门ID，然后来关闭v选一个既建的避故可有特殊的修复整改
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      if (userInfo.deptId) {
        searchForm.departmentId = userInfo.deptId
      }
    }
    getTableData()
  })
})

const loadDepartmentTasks = async () => {
  try {
    taskSelectLoading.value = true
    const result = await webSocketStore.fetchTaskNotifications()
    const arr = (Array.isArray(result) ? result : []).map(tn => {
      const label = tn.inspectionMonth ? `${tn.inspectionMonth}月月检任务` : (tn.title || '月检任务')
      return {
        id: tn.taskId,
        taskName: label,
        taskNo: tn.taskNo,
        status: tn.status
      }
    })
    taskSelectList.value = arr
  } catch (e) {
    ElMessage.error('加载任务失败')
  } finally {
    taskSelectLoading.value = false
  }
}

const getTaskStatusText = (s) => {
  const m = { 0: '未开始', 1: '进行中', 2: '已完成', 3: '已取消' }
  return m[s] || '未知'
}

const confirmTaskSelection = () => {
  const task = taskSelectList.value.find(t => t.id === selectedTaskId.value)
  if (!task) {
    ElMessage.warning('请选择任务')
    return
  }
  searchForm.taskId = task.id
  searchForm.taskNo = task.taskNo
  router.push({ path: route.path, query: { taskId: task.id } })
  currentRow.value = { taskNo: task.taskNo, taskId: task.id }
  taskSelectVisible.value = false
  addDialogVisible.value = true
}
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