<template>
  <div class="monthly-submit-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>月检提交</h2>
      <p>管理月检记录的提交，提交后状态将变更为待整改</p>
    </div>

    <!-- 查询条件区域 -->
    <div class="search-section">
      <el-card>
        <el-form :model="searchForm" inline>
          <el-form-item label="责任人">
            <el-input
              v-model="searchForm.responsiblePerson"
              placeholder="请输入责任人"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="检查人">
            <el-input
              v-model="searchForm.inspector"
              placeholder="请输入检查人"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="所属部门">
            <el-select
              v-model="searchForm.departmentId"
              placeholder="请选择部门"
              clearable
              filterable
              style="width: 200px"
              :disabled="!isMonthlyAdminUser"
            >
              <el-option
                v-for="dept in departmentList"
                :key="dept.id"
                :label="dept.deptName"
                :value="dept.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="记录时间">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              style="width: 240px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-section">
      <el-button
        type="primary"
        :disabled="selectedRows.length === 0"
        @click="handleBatchSubmit"
      >
        <el-icon><Upload /></el-icon>
        批量提交
      </el-button>
      <span class="selected-info" v-if="selectedRows.length > 0">
        已选择 {{ selectedRows.length }} 条记录
      </span>
    </div>

    <!-- 数据列表 -->
    <div class="table-section">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="responsiblePerson" label="责任人" width="120" />
        <el-table-column prop="problemDescription" label="问题描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="location" label="位置" width="150" show-overflow-tooltip />
        <el-table-column prop="inspector" label="检查人" width="120" />
        <el-table-column label="检查单位" width="150">
          <template #default="{ row }">
            {{ row.checkUnitName || getDepartmentName(row.checkUnitId) }}
          </template>
        </el-table-column>
        <el-table-column label="受检单位" width="150">
          <template #default="{ row }">
            {{ row.departmentName || getDepartmentName(row.departmentId) }}
          </template>
        </el-table-column>
        <el-table-column label="性质界定是否修改" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.natureModified === 1 ? 'warning' : 'info'" size="small">
              {{ row.natureModified === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="记录时间" width="180" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(row)"
            >编辑</el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(row)"
            >删除</el-button>
            <el-button type="success" size="small" @click="handleSubmitRow(row)">提交</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 编辑对话框 -->
    <EditDialog
      v-model="editDialog.visible"
      :record="editDialog.record"
      @success="handleEditSuccess"
    />

    <!-- 编辑审核提交对话框 -->
    <EditAuditDialog
      v-model:visible="editAuditVisible"
      :record="currentRow"
      :audit-type="currentRow.auditType || 1"
      @success="handleEditAuditSuccess"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Upload } from '@element-plus/icons-vue'
import { monthlyRecordApi, getDeptList, monthlyTaskApi } from '@/api/monthlyRecord'
import { isMonthlyAdmin } from '@/utils/permission'
import { phaseOfRecord, computeTaskPhase, hasTaskTimes } from '@/utils/taskPhase'
import EditDialog from './components/EditDialog.vue'
import EditAuditDialog from '@/views/inspection/monthly/components/EditAuditDialog.vue'

// 搜索表单
const searchForm = reactive({
  responsiblePerson: '',
  inspector: '',
  departmentId: '',
  dateRange: null
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])

// 分页
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 部门数据
// allDepartmentList: 全部部门,用于表格名称查找(row.departmentId 多为运行部门)
// departmentList: 管理部门(deptType=1),仅用于搜索栏下拉选项
const allDepartmentList = ref([])
const departmentList = ref([])

// 用户权限：是否为月检管理员
// 仅月检管理员可自由选择部门；非月检管理员登录时下拉自动预填本部门并锁定
const isMonthlyAdminUser = ref(false)

// 登录人是否为运行部门：月检提交页只对检查部门开放，运行部门登录时看不到任何数据
const isRunningDeptUser = ref(false)

// 编辑弹窗状态
const editDialog = reactive({
  visible: false,
  record: null
})

// 编辑审核弹窗
const editAuditVisible = ref(false)
const currentRow = ref({})

// 获取部门名称(查全部部门,含运行/管理两类;表格 row.departmentId 多为运行部门)
const getDepartmentName = (departmentId) => {
  if (!departmentId) return '-'
  const dept = allDepartmentList.value.find(item => item.id === departmentId)
  return dept ? dept.deptName : departmentId
}

// 按部门 ID 解析部门类型
const resolveDeptType = (deptId) => {
  if (!deptId) return null
  const d = allDepartmentList.value.find(item => String(item.id) === String(deptId))
  return d && d.deptType !== null && d.deptType !== undefined ? d.deptType : null
}

// 按部门 ID 决定用 checkUnitId(管理部门) 还是 departmentId(运行部门) 过滤
const applyDeptFilter = (params, deptId) => {
  if (!deptId) return
  const type = resolveDeptType(deptId)
  if (type === 1) {
    params.checkUnitId = deptId
  } else {
    params.departmentId = deptId
  }
}

// 加载部门数据
// allDepartmentList 拉全部部门用于表格名称查找;departmentList 仅管理部门用于搜索栏下拉
const loadDepartmentList = async () => {
  try {
    const response = await getDeptList()
    const list = Array.isArray(response)
      ? response
      : (Array.isArray(response?.data)
        ? response.data
        : (response?.data?.list || []))
    allDepartmentList.value = list
    departmentList.value = list.filter(d => d.deptType === 1)
    // 非月检管理员且自己部门不在检查部门列表中，才允许看数据；否则视为运行部门，无数据
    if (!isMonthlyAdminUser.value) {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      const inMgmtList = userInfo.deptId
        ? departmentList.value.some(d => String(d.id) === String(userInfo.deptId))
        : false
      isRunningDeptUser.value = !inMgmtList
    }
  } catch (error) {
    console.error('加载部门数据失败:', error)
  }
}

// 获取数据
const fetchData = async () => {
  // 运行部门登录：本页不开放，直接返回空数据，不发查询请求
  if (!isMonthlyAdminUser.value && isRunningDeptUser.value) {
    tableData.value = []
    pagination.total = 0
    return
  }
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      recordStatus: 4,
      responsiblePerson: searchForm.responsiblePerson || undefined,
      inspector: searchForm.inspector || undefined,
      startDate: searchForm.dateRange?.[0] || undefined,
      endDate: searchForm.dateRange?.[1] || undefined
    }

    // 管理部门按 checkUnitId(检查单位)过滤;运行部门按 departmentId(受检单位)过滤;
    // 当前页(待提交)只允许管理部门访问——运行部门登录时上面已拦截
    if (!isMonthlyAdminUser.value && !searchForm.departmentId) {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      if (userInfo.deptId) {
        applyDeptFilter(params, userInfo.deptId)
      }
    } else if (searchForm.departmentId) {
      applyDeptFilter(params, searchForm.departmentId)
    }

    const response = await monthlyRecordApi.getPage(params)
    if (response.code === 200) {
      // 同步计算任务阶段：分页接口已带出任务时间字段，
      // 异步回填会写到原始对象、绕过 Vue 响应式代理导致编辑按钮状态不更新
      tableData.value = (response.data.records || []).map(row => {
        if (row.taskId) row.taskPhase = phaseOfRecord(row)
        return row
      })
      pagination.total = response.data.total || 0
      await fillMissingTaskPhases()
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 兜底补齐任务阶段：后端未升级版本不返回任务时间字段，阶段恒为 0，编辑/删除按钮判断会失效。
// 算完后整体重新赋值 tableData.value 触发重渲染，写原始对象不触发 Vue 响应式更新。
const fillMissingTaskPhases = async () => {
  const pending = tableData.value.filter(r => r.taskId && !hasTaskTimes(r))
  if (!pending.length) return
  const taskIds = [...new Set(pending.map(r => r.taskId))]
  const phaseMap = {}
  await Promise.all(taskIds.map(async id => {
    try {
      const resp = await monthlyTaskApi.getById(id)
      if (resp.code === 200 && resp.data) {
        phaseMap[id] = computeTaskPhase(resp.data)
      }
    } catch (e) {
      console.warn('获取任务阶段失败:', id, e)
    }
  }))
  tableData.value = tableData.value.map(r => (
    r.taskId && phaseMap[r.taskId] !== undefined
      ? { ...r, taskPhase: phaseMap[r.taskId] }
      : r
  ))
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

// 重置
const handleReset = () => {
  searchForm.responsiblePerson = ''
  searchForm.inspector = ''
  searchForm.departmentId = ''
  searchForm.dateRange = null
  if (!isMonthlyAdminUser.value) {
    getCurrentUserDepartment()
  }
  pagination.current = 1
  fetchData()
}

// 选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 批量提交
const handleBatchSubmit = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要提交的记录')
    return
  }
  try {
    await ElMessageBox.confirm(
      `确定要提交选中的 ${selectedRows.value.length} 条记录吗？提交后状态将变更为待整改。`,
      '批量提交确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const ids = selectedRows.value.map(row => row.id)
    const response = await monthlyRecordApi.batchUpdateStatus({ ids, recordStatus: 0 })
    if (response.code === 200) {
      ElMessage.success('批量提交成功')
      selectedRows.value = []
      fetchData()
    } else {
      ElMessage.error(response.message || '批量提交失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量提交失败:', error)
      ElMessage.error('批量提交失败')
    }
  }
}

/**
 * 判定记录的编辑/删除限制（与 monthly/index.vue 保持一致）
 */
function classifyRecordAction(row) {
  if (!row?.taskId) {
    return { action: 'edit', reason: '' }
  }
  if (row.taskPhase === 1 || row.taskPhase === 2) {
    return { action: 'edit', reason: '' }
  }
  if (row.taskPhase === 3) {
    return { action: 'audit', reason: '当前处于公示期内，需提交修改说明等待审核' }
  }
  return { action: 'forbid', reason: '已超过公示期，记录不可编辑或删除' }
}

const handleEdit = async (row) => {
  const cls = classifyRecordAction(row)
  if (cls.action === 'forbid') {
    ElMessage.warning(cls.reason)
    return
  }
  if (cls.action === 'audit') {
    try {
      const response = await monthlyRecordApi.getById(row.id)
      if (response.code === 200) {
        currentRow.value = { ...response.data, taskPhase: row.taskPhase, auditType: 1 }
      } else {
        currentRow.value = { ...row, auditType: 1 }
      }
    } catch (e) {
      currentRow.value = { ...row, auditType: 1 }
    }
    editAuditVisible.value = true
    return
  }
  // 列表分页接口返回的字段不全（无 rectificationDeadline、photos 等），必须取详情才能完整回写
  try {
    const response = await monthlyRecordApi.getById(row.id)
    editDialog.record = response.code === 200 && response.data
      ? { ...row, ...response.data }
      : { ...row }
  } catch (e) {
    editDialog.record = { ...row }
  }
  editDialog.visible = true
}

const handleDelete = async (row) => {
  const cls = classifyRecordAction(row)
  if (cls.action === 'forbid') {
    ElMessage.warning(cls.reason)
    return
  }
  if (cls.action === 'audit') {
    try {
      const response = await monthlyRecordApi.getById(row.id)
      if (response.code === 200) {
        currentRow.value = { ...response.data, taskPhase: row.taskPhase, auditType: 2 }
      } else {
        currentRow.value = { ...row, auditType: 2 }
      }
    } catch (e) {
      currentRow.value = { ...row, auditType: 2 }
    }
    editAuditVisible.value = true
    return
  }
  try {
    await ElMessageBox.confirm(
      `确定要删除责任人为"${row.responsiblePerson}"的记录吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const response = await monthlyRecordApi.delete(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      fetchData()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleSubmitRow = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要提交责任人为"${row.responsiblePerson}"的记录吗？提交后状态将变更为待整改。`,
      '提交确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const response = await monthlyRecordApi.updateStatus({ id: row.id, recordStatus: 0 })
    if (response.code === 200) {
      ElMessage.success('提交成功')
      fetchData()
    } else {
      ElMessage.error(response.message || '提交失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('提交失败:', error)
      ElMessage.error('提交失败')
    }
  }
}

const handleEditSuccess = () => {
  editDialog.visible = false
  fetchData()
}

const handleEditAuditSuccess = () => {
  editAuditVisible.value = false
  // 成功提示已由 EditAuditDialog 内部弹出,此处不再重复
  fetchData()
}

// 分页变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  fetchData()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  fetchData()
}

// 获取当前用户部门信息
// 月检管理员：保留空，不强制预填；非月检管理员：预填本部门
const getCurrentUserDepartment = () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    isMonthlyAdminUser.value = isMonthlyAdmin()
    if (!isMonthlyAdminUser.value && userInfo.deptId) {
      searchForm.departmentId = userInfo.deptId
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 初始化
onMounted(async () => {
  getCurrentUserDepartment()
  // 必须先拿到部门列表并判定登录人部门类型，再拉数据，避免运行部门短暂看到数据
  await loadDepartmentList()
  fetchData()
})
</script>

<style scoped>
.monthly-submit-container {
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

.search-section {
  margin-bottom: 20px;
}

.action-section {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
}

.selected-info {
  color: #409eff;
  font-size: 14px;
}

.table-section {
  background: white;
  border-radius: 4px;
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
