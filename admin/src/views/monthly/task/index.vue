<template>
  <div class="monthly-task-management">
    <div class="page-container">
      <div class="page-header">
        <h1 class="page-title">月检任务管理</h1>
      </div>
      
      <!-- 搜索和操作栏 -->
      <div class="toolbar">
        <div class="search-form">
          <el-input
            v-model="searchForm.taskNo"
            placeholder="请输入任务编号"
            style="width: 200px"
            clearable
          />
          <el-select
            v-model="searchForm.departmentId"
            placeholder="请选择部门"
            style="width: 150px"
            clearable
          >
            <el-option
              v-for="dept in deptOptions"
              :key="dept.id"
              :label="dept.deptName"
              :value="dept.id"
            />
          </el-select>
          <el-select
            v-model="searchForm.professionId"
            placeholder="请选择专业"
            style="width: 150px"
            clearable
          >
            <el-option
              v-for="profession in professionOptions"
              :key="profession.id"
              :label="profession.professionName"
              :value="profession.dictValue"
            />
          </el-select>
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            style="width: 120px"
            clearable
          >
            <el-option label="未开始" :value="0" />
            <el-option label="进行中" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已取消" :value="3" />
          </el-select>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </div>
        
        <div class="action-buttons">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增任务
          </el-button>
          <el-button 
            type="success" 
            @click="handleBatchNotify"
            :disabled="selectedIds.length === 0"
          >
            <el-icon><Bell /></el-icon>
            批量通知
          </el-button>
        </div>
      </div>
      
      <!-- 任务表格 -->
      <div class="table-container">
        <el-table
          v-loading="loading"
          :data="tableData"
          @selection-change="handleSelectionChange"
          stripe
          border
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="taskNo" label="任务编号" width="150" />
          <el-table-column prop="inspectionTime" label="月检时间" width="160" />
          <el-table-column prop="groupLeaderName" label="组长" width="100" />
          <el-table-column prop="deputyLeaderName" label="副组长" width="100" />
          <el-table-column prop="departmentName" label="部门" width="120" />
          <el-table-column prop="professionId" label="专业" width="100" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="notificationSent" label="通知状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.notificationSent ? 'success' : 'warning'">
                {{ row.notificationSent ? '已通知' : '未通知' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160" />
          <el-table-column label="操作" width="280" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleView(row)">查看</el-button>
              <el-button 
                type="warning" 
                size="small" 
                @click="handleEdit(row)"
                :disabled="row.status === 2 || row.status === 3"
              >
                编辑
              </el-button>
              <el-button 
                type="info" 
                size="small" 
                @click="handleNotify(row)"
                :disabled="row.notificationSent"
              >
                通知
              </el-button>
              <el-button 
                type="danger" 
                size="small" 
                @click="handleDelete(row)"
                :disabled="row.status === 'in_progress'"
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
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    
    <!-- 任务表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12" v-if="form.id">
            <el-form-item label="任务编号" prop="taskNo">
              <el-input 
                v-model="form.taskNo" 
                placeholder="系统自动生成"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="form.id ? 12 : 24">
            <el-form-item label="月检时间" prop="inspectionTime">
              <el-date-picker
                v-model="form.inspectionTime"
                type="datetime"
                placeholder="选择月检时间"
                format="YYYY-MM-DD HH:mm"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组长" prop="groupLeaderId">
              <el-select v-model="form.groupLeaderId" placeholder="请选择组长" style="width: 100%">
                <el-option
                  v-for="user in userOptions"
                  :key="user.id"
                  :label="user.nickname"
                  :value="user.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="副组长" prop="deputyLeaderId">
              <el-select v-model="form.deputyLeaderId" placeholder="请选择副组长" style="width: 100%">
                <el-option
                  v-for="user in userOptions"
                  :key="user.id"
                  :label="user.nickname"
                  :value="user.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门" prop="departmentId">
              <el-select v-model="form.departmentId" placeholder="请选择部门" style="width: 100%">
                <el-option
                  v-for="dept in deptOptions"
                  :key="dept.id"
                  :label="dept.deptName"
                  :value="dept.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业" prop="professionId">
              <el-select v-model="form.professionId" placeholder="请选择专业" style="width: 100%">
                <el-option
                  v-for="profession in professionOptions"
                  :key="profession.id"
                  :label="profession.professionName"
                  :value="profession.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="任务描述" prop="taskDescription">
          <el-input
            v-model="form.taskDescription"
            type="textarea"
            :rows="4"
            placeholder="请输入任务描述"
          />
        </el-form-item>
        
        <el-form-item label="任务状态" prop="status" v-if="form.id">
          <el-radio-group v-model="form.status">
            <el-radio label="pending">待执行</el-radio>
            <el-radio label="in_progress">进行中</el-radio>
            <el-radio label="completed">已完成</el-radio>
            <el-radio label="cancelled">已取消</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
    
    <!-- 任务详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="任务详情"
      width="600px"
    >
      <div class="task-detail" v-if="currentTask">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="任务编号">{{ currentTask.taskNo }}</el-descriptions-item>
          <el-descriptions-item label="月检时间">{{ currentTask.inspectionTime }}</el-descriptions-item>
          <el-descriptions-item label="组长">{{ currentTask.groupLeaderName }}</el-descriptions-item>
          <el-descriptions-item label="副组长">{{ currentTask.deputyLeaderName }}</el-descriptions-item>
          <el-descriptions-item label="部门">{{ currentTask.departmentName }}</el-descriptions-item>
          <el-descriptions-item label="专业">{{ currentTask.professionName }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentTask.status)">
              {{ getStatusText(currentTask.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="通知状态">
            <el-tag :type="currentTask.notificationSent ? 'success' : 'warning'">
              {{ currentTask.notificationSent ? '已通知' : '未通知' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ currentTask.createTime }}</el-descriptions-item>
          <el-descriptions-item label="任务描述" :span="2">{{ currentTask.taskDescription || '无' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Bell } from '@element-plus/icons-vue'
import { monthlyTaskApi, professionApi } from '@/api/monthly'
import { deptApi, userApi } from '@/api/system'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const formRef = ref()
const selectedIds = ref([])
const currentTask = ref(null)

// 搜索表单
const searchForm = reactive({
  taskNo: '',
  departmentId: null,
  professionId: null,
  status: null // 使用null作为默认值，表示不筛选状态
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 选项数据
const deptOptions = ref([])
const professionOptions = ref([])
const userOptions = ref([])

// 表单数据
const form = reactive({
  id: null,
  taskNo: '',
  inspectionTime: '',
  groupLeaderId: null,
  deputyLeaderId: null,
  departmentId: null,
  professionId: null,
  taskDescription: '',
  status: 0 // 0-未开始，1-进行中，2-已完成
})

// 表单验证规则
const formRules = {
  taskNo: [
    { required: true, message: '请输入任务编号', trigger: 'blur' }
  ],
  inspectionTime: [
    { required: true, message: '请选择月检时间', trigger: 'change' }
  ],
  groupLeaderId: [
    { required: true, message: '请选择组长', trigger: 'change' }
  ],
  deputyLeaderId: [
    { required: true, message: '请选择副组长', trigger: 'change' }
  ],
  departmentId: [
    { required: true, message: '请选择部门', trigger: 'change' }
  ],
  professionId: [
    { required: true, message: '请选择专业', trigger: 'change' }
  ]
}

// 对话框标题
const dialogTitle = computed(() => {
  return form.id ? '编辑任务' : '新增任务'
})

// 状态类型映射
const getStatusType = (status) => {
  const typeMap = {
    0: 'warning',  // 未开始
    1: 'primary',  // 进行中
    2: 'success',  // 已完成
    3: 'danger'    // 已取消
  }
  return typeMap[status] || 'info'
}

// 状态文本映射
const getStatusText = (status) => {
  const textMap = {
    0: '未开始',
    1: '进行中',
    2: '已完成',
    3: '已取消'
  }
  return textMap[status] || '未知'
}

// 加载任务列表
const loadTaskList = async () => {
  loading.value = true
  
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      taskNo: searchForm.taskNo,
      departmentId: searchForm.departmentId,
      professionId: searchForm.professionId,
      status: searchForm.status
    }
    
    const response = await monthlyTaskApi.getPage(params)
    
    if (response.code === 200) {
      tableData.value = response.data.records || []
      pagination.total = response.data.total || 0
    } else {
      ElMessage.error(response.message || '加载任务列表失败')
    }
  } catch (error) {
    console.error('加载任务列表失败:', error)
    ElMessage.error('加载任务列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadTaskList()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    taskNo: '',
    departmentId: null,
    professionId: null,
    status: null // 使用null作为默认值，表示不筛选状态
  })
  handleSearch()
}

// 新增任务
const handleAdd = () => {
  resetForm()
  // 任务编号由后端自动生成，无需前端预先调用
  dialogVisible.value = true
}

// 编辑任务
const handleEdit = (row) => {
  Object.assign(form, {
    id: row.id,
    taskNo: row.taskNo,
    inspectionTime: row.inspectionTime,
    groupLeaderId: row.groupLeaderId,
    deputyLeaderId: row.deputyLeaderId,
    departmentId: row.departmentId,
    professionId: row.professionId,
    taskDescription: row.taskDescription,
    status: row.status
  })
  dialogVisible.value = true
}

// 查看任务详情
const handleView = (row) => {
  currentTask.value = {
    ...row,
    // 映射专业ID为专业名称
    professionName: professionOptions.value.find(p => p.id === row.professionId)?.dictLabel || row.professionId
  }
  detailDialogVisible.value = true
}

// 删除任务
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除任务 "${row.taskNo}" 吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await monthlyTaskApi.delete(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadTaskList()
    } else {
      ElMessage.error(response.message || '删除任务失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除任务失败:', error)
      ElMessage.error('删除任务失败')
    }
  }
}

// 发送通知
const handleNotify = async (row) => {
  try {
    const response = await monthlyTaskApi.sendNotification(row.id)
    if (response.code === 200) {
      ElMessage.success('通知发送成功')
      loadTaskList()
    } else {
      ElMessage.error(response.message || '发送通知失败')
    }
  } catch (error) {
    console.error('发送通知失败:', error)
    ElMessage.error('发送通知失败')
  }
}

// 批量通知
const handleBatchNotify = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要向选中的 ${selectedIds.value.length} 个任务发送通知吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await monthlyTaskApi.batchSendNotification(selectedIds.value)
    if (response.code === 200) {
      ElMessage.success('批量通知发送成功')
      loadTaskList()
    } else {
      ElMessage.error(response.message || '批量通知失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量通知失败:', error)
      ElMessage.error('批量通知失败')
    }
  }
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  loadTaskList()
}

// 当前页变化
const handleCurrentChange = (page) => {
  pagination.current = page
  loadTaskList()
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    const submitData = { ...form }
    
    if (form.id) {
      const response = await monthlyTaskApi.update(submitData)
      if (response.code === 200) {
        ElMessage.success('更新成功')
        dialogVisible.value = false
        loadTaskList()
      } else {
        ElMessage.error(response.message || '更新失败')
      }
    } else {
      const response = await monthlyTaskApi.create(submitData)
      if (response.code === 200) {
        ElMessage.success('创建成功')
        dialogVisible.value = false
        loadTaskList()
      } else {
        ElMessage.error(response.message || '创建失败')
      }
    }
  } catch (error) {
    console.error('提交表单失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    id: null,
    taskNo: '',
    inspectionTime: '',
    groupLeaderId: null,
    deputyLeaderId: null,
    departmentId: null,
    professionId: null,
    taskDescription: '',
    status: 0 // 0-未开始，1-进行中，2-已完成
  })
  
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 加载选项数据
const loadOptions = async () => {
  try {
    const [deptResponse, professionResponse, userResponse] = await Promise.all([
      deptApi.getTree(),
      professionApi.getList(),
      userApi.getPage({ size: 1000 }) // 获取所有用户
    ])
    
    if (deptResponse.code === 200) {
      deptOptions.value = deptResponse.data
    }
    
    if (professionResponse.code === 200) {
      professionOptions.value = professionResponse.data
    }
    
    if (userResponse.code === 200) {
      userOptions.value = userResponse.data.records || userResponse.data
    }
  } catch (error) {
    console.error('加载选项数据失败:', error)
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadTaskList()
  loadOptions()
})
</script>

<style scoped>
.monthly-task-management {
  padding: 0;
}

.page-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
}

.search-form {
  display: flex;
  gap: 12px;
  align-items: center;
}

.action-buttons {
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

.task-detail {
  padding: 20px 0;
}
</style>