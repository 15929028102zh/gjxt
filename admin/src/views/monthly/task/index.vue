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
            v-model="searchForm.taskName"
            placeholder="按任务名称搜索"
            style="width: 200px"
            clearable
          />
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            style="width: 140px"
            clearable
          >
            <el-option label="未开始" :value="0" />
            <el-option label="进行中" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已取消" :value="3" />
          </el-select>
          <el-date-picker
            v-model="searchForm.dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="月检开始"
            end-placeholder="月检结束"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 360px"
          />
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
          <el-table-column prop="taskNo" label="任务编号" width="180" show-overflow-tooltip>
            <template #default="{ row }">
              {{ formatTaskNo(row.taskNo) }}
            </template>
          </el-table-column>
          <el-table-column prop="taskName" label="任务名称" min-width="160" show-overflow-tooltip />
          <el-table-column label="月检时间段" width="300">
            <template #default="{ row }">
              <div class="range-cell">
                <div>{{ formatDateTime(row.inspectionStartTime) }}</div>
                <div class="separator">至</div>
                <div>{{ formatDateTime(row.inspectionEndTime) }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="公示时间段" width="300">
            <template #default="{ row }">
              <div class="range-cell">
                <div>{{ formatDateTime(row.publicityStartTime) }}</div>
                <div class="separator">至</div>
                <div>{{ formatDateTime(row.publicityEndTime) }}</div>
              </div>
            </template>
          </el-table-column>
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
          <el-table-column prop="createTime" label="创建时间" width="170" />
          <el-table-column label="操作" width="240" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleView(row)">查看</el-button>
              <el-button
                v-if="hasMonthlyPermission()"
                type="warning"
                size="small"
                @click="handleEdit(row)"
              >
                编辑
              </el-button>
              <el-button
                v-if="hasMonthlyPermission()"
                type="info"
                size="small"
                @click="handleNotify(row)"
                :disabled="row.notificationSent"
              >
                通知
              </el-button>
              <el-button
                v-if="hasMonthlyPermission()"
                type="danger"
                size="small"
                @click="handleDelete(row)"
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
      width="640px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item v-if="form.id" label="任务编号" prop="taskNo">
          <el-input
            v-model="form.taskNo"
            placeholder="系统自动生成"
            disabled
          />
        </el-form-item>

        <el-form-item label="任务名称" prop="taskName">
          <el-input
            v-model="form.taskName"
            placeholder="请输入任务名称"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="月检时间段" prop="inspectionRange">
          <el-date-picker
            v-model="form.inspectionRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="月检开始时间"
            end-placeholder="月检结束时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="公示时间段" prop="publicityRange">
          <el-date-picker
            v-model="form.publicityRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="公示开始时间"
            end-placeholder="公示结束时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="任务描述" prop="taskDescription">
          <el-input
            v-model="form.taskDescription"
            type="textarea"
            :rows="4"
            placeholder="请输入任务描述"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-form-item v-if="form.id" label="任务状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">未开始</el-radio>
            <el-radio :label="1">进行中</el-radio>
            <el-radio :label="2">已完成</el-radio>
            <el-radio :label="3">已取消</el-radio>
          </el-radio-group>
          <div class="form-hint">状态会自动根据月检/公示时间段计算，手动调整时将作为取消状态保留</div>
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
      width="640px"
    >
      <div class="task-detail" v-if="currentTask">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="任务编号">{{ currentTask.taskNo }}</el-descriptions-item>
          <el-descriptions-item label="任务名称">{{ currentTask.taskName }}</el-descriptions-item>
          <el-descriptions-item label="月检时间段">
            {{ formatDateTime(currentTask.inspectionStartTime) }} ~ {{ formatDateTime(currentTask.inspectionEndTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="公示时间段">
            {{ formatDateTime(currentTask.publicityStartTime) }} ~ {{ formatDateTime(currentTask.publicityEndTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="当前阶段">
            <el-tag :type="phaseTagType(currentTask.phase)">{{ phaseText(currentTask.phase) }}</el-tag>
          </el-descriptions-item>
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
          <el-descriptions-item label="创建人">{{ currentTask.createUserName || '系统' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDateTime(currentTask.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="任务描述">{{ currentTask.taskDescription || '无' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Bell } from '@element-plus/icons-vue'
import { monthlyTaskApi } from '@/api/monthly'
import { hasMonthlyPermission } from '@/utils/permission'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const formRef = ref()
const selectedIds = ref([])
const currentTask = ref(null)
const route = useRoute()

const searchForm = reactive({
  taskName: '',
  status: null,
  dateRange: []
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const tableData = ref([])

const form = reactive({
  id: null,
  taskNo: '',
  taskName: '',
  inspectionRange: [],
  publicityRange: [],
  taskDescription: '',
  status: 0
})

const formRules = {
  taskName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
  inspectionRange: [{ required: true, message: '请选择月检时间段', trigger: 'change' }],
  publicityRange: [{ required: true, message: '请选择公示时间段', trigger: 'change' }]
}

const dialogTitle = computed(() => (form.id ? '编辑任务' : '新增任务'))

const getStatusType = (status) => {
  const typeMap = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'danger' }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = { 0: '未开始', 1: '进行中', 2: '已完成', 3: '已取消' }
  return textMap[status] || '未知'
}

function phaseTagType(phase) {
  const map = { 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }
  return map[phase] || 'info'
}

function phaseText(phase) {
  const map = { 0: '未开始', 1: '录入期内', 2: '公示前', 3: '公示期内' }
  return map[phase] || '未知'
}

function formatDateTime(value) {
  if (!value) return '-'
  const d = new Date(value)
  if (isNaN(d.getTime())) return value
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
}

async function loadTaskList() {
  loading.value = true
  try {
    const [start, end] = searchForm.dateRange || []
    const params = {
      current: pagination.current,
      size: pagination.size,
      status: searchForm.status,
      taskName: searchForm.taskName || undefined,
      inspectionStartTime: start,
      inspectionEndTime: end
    }
    const response = await monthlyTaskApi.getPage(params)
    if (response.code === 200) {
      const records = response.data?.records || []
      tableData.value = records.map(t => ({ ...t }))
      pagination.total = response.data?.total || 0
    } else {
      ElMessage.error(response.message || '加载任务列表失败')
    }
  } catch (error) {
    console.error('加载任务列表失败', error)
    ElMessage.error('加载任务列表失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.current = 1
  loadTaskList()
}

function resetSearch() {
  searchForm.taskName = ''
  searchForm.status = null
  searchForm.dateRange = []
  handleSearch()
}

function handleAdd() {
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  Object.assign(form, {
    id: row.id,
    taskNo: row.taskNo,
    taskName: row.taskName,
    inspectionRange: row.inspectionStartTime && row.inspectionEndTime
      ? [row.inspectionStartTime, row.inspectionEndTime] : [],
    publicityRange: row.publicityStartTime && row.publicityEndTime
      ? [row.publicityStartTime, row.publicityEndTime] : [],
    taskDescription: row.taskDescription,
    status: row.status
  })
  dialogVisible.value = true
}

function handleView(row) {
  const now = new Date()
  let phase = 0
  const start = row.inspectionStartTime ? new Date(row.inspectionStartTime) : null
  const end = row.inspectionEndTime ? new Date(row.inspectionEndTime) : null
  const pStart = row.publicityStartTime ? new Date(row.publicityStartTime) : null
  const pEnd = row.publicityEndTime ? new Date(row.publicityEndTime) : null
  if (start && end && now >= start && now <= end) phase = 1
  else if (pStart && pEnd && now >= pStart && now <= pEnd) phase = 3
  else if (end && now > end && pStart && now < pStart) phase = 2
  currentTask.value = { ...row, phase }
  detailDialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定要删除任务 "${row.taskName || row.taskNo}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await monthlyTaskApi.delete(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadTaskList()
    } else {
      ElMessage.error(response.message || '删除任务失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除任务失败', error)
      ElMessage.error('删除任务失败')
    }
  }
}

async function handleNotify(row) {
  try {
    const response = await monthlyTaskApi.sendNotification(row.id)
    if (response.code === 200) {
      ElMessage.success('通知发送成功')
      loadTaskList()
    } else {
      ElMessage.error(response.message || '发送通知失败')
    }
  } catch (error) {
    console.error('发送通知失败', error)
    ElMessage.error('发送通知失败')
  }
}

async function handleBatchNotify() {
  try {
    await ElMessageBox.confirm(`确定要向选中的 ${selectedIds.value.length} 个任务发送通知吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await monthlyTaskApi.batchSendNotification(selectedIds.value)
    if (response.code === 200) {
      ElMessage.success('批量通知发送成功')
      loadTaskList()
    } else {
      ElMessage.error(response.message || '批量通知失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量通知失败', error)
      ElMessage.error('批量通知失败')
    }
  }
}

function handleSelectionChange(selection) {
  selectedIds.value = selection.map(item => item.id)
}

function handleSizeChange(size) {
  pagination.size = size
  loadTaskList()
}

function handleCurrentChange(page) {
  pagination.current = page
  loadTaskList()
}

async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    submitLoading.value = true

    const payload = {
      id: form.id || undefined,
      taskNo: form.taskNo || undefined,
      taskName: form.taskName,
      taskDescription: form.taskDescription || '',
      status: form.id ? form.status : 0
    }
    if (Array.isArray(form.inspectionRange) && form.inspectionRange.length === 2) {
      payload.inspectionStartTime = form.inspectionRange[0]
      payload.inspectionEndTime = form.inspectionRange[1]
    }
    if (Array.isArray(form.publicityRange) && form.publicityRange.length === 2) {
      payload.publicityStartTime = form.publicityRange[0]
      payload.publicityEndTime = form.publicityRange[1]
    }

    if (form.id) {
      const response = await monthlyTaskApi.update(payload)
      if (response.code === 200) {
        ElMessage.success('更新成功')
        dialogVisible.value = false
        loadTaskList()
      } else {
        ElMessage.error(response.message || '更新失败')
      }
    } else {
      const response = await monthlyTaskApi.create(payload)
      if (response.code === 200) {
        ElMessage.success('创建成功')
        dialogVisible.value = false
        loadTaskList()
      } else {
        ElMessage.error(response.message || '创建失败')
      }
    }
  } catch (error) {
    console.error('提交表单失败', error)
    if (error?.message) ElMessage.error(error.message)
    else ElMessage.error('提交失败')
  } finally {
    submitLoading.value = false
  }
}

function resetForm() {
  Object.assign(form, {
    id: null,
    taskNo: '',
    taskName: '',
    inspectionRange: [],
    publicityRange: [],
    taskDescription: '',
    status: 0
  })
  if (formRef.value) formRef.value.clearValidate()
}

function formatTaskNo(taskNo) {
  if (!taskNo) return taskNo
  const match = taskNo.match(/^(YJ)(\d{4})(\d{2})(\d{2})(.*)$/)
  if (match) {
    const [, prefix, year, month, day, suffix] = match
    return `${prefix}${year}年${parseInt(month, 10)}月${parseInt(day, 10)}日${suffix}`
  }
  return taskNo
}

onMounted(() => {
  loadTaskList()
  openTaskByQuery()
})

// 从月检记录录入页"去补配"跳转过来时，按任务编号直接打开编辑弹窗
const openTaskByQuery = async () => {
  const taskNo = route.query.taskNo
  if (!taskNo) return
  try {
    const resp = await monthlyTaskApi.getByTaskNo(taskNo)
    if (resp.code === 200 && resp.data) {
      handleEdit(resp.data)
    } else {
      ElMessage.warning(`未找到任务 ${taskNo}`)
    }
  } catch (e) {
    console.error('按任务编号打开任务失败:', e)
  }
}
</script>

<style scoped>
.monthly-task-management { padding: 0; }
.page-container { padding: 20px; }
.page-header { margin-bottom: 20px; }
.page-title { font-size: 24px; font-weight: 600; color: #303133; margin: 0; }
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
  flex-wrap: wrap;
}
.action-buttons { display: flex; gap: 12px; }
.table-container { margin-bottom: 20px; }
.pagination-container { display: flex; justify-content: flex-end; }
.task-detail { padding: 20px 0; }
.range-cell { font-size: 12px; line-height: 1.4; color: #303133; }
.range-cell .separator { color: #909399; }
.form-hint { color: #909399; font-size: 12px; line-height: 1.5; }
</style>
