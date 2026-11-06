<template>
  <div class="audit-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>月检待审核</h2>
      <p>管理月检审核申请记录，包括回退审核、编辑审核与删除审核</p>
    </div>

    <!-- 审核类型切换 -->
    <el-tabs v-model="auditType" class="audit-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="回退审核" name="0" />
      <el-tab-pane label="编辑审核" name="1" />
      <el-tab-pane label="删除审核" name="2" />
    </el-tabs>

    <!-- 查询条件卡片 -->
    <el-card class="search-card">
      <template #header>
        <div class="card-header">
          <span>查询条件 - {{ auditTypeLabel }}</span>
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
              filterable
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
            <label>申请人姓名</label>
            <el-input
              v-model="queryParams.modifyUserName"
              placeholder="请输入申请人姓名"
              clearable
            />
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
        <el-table-column prop="problemDescription" label="问题描述" min-width="200" show-overflow-tooltip />
        <el-table-column :label="reasonColumnLabel" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <el-text v-if="row.modifyDescription">{{ row.modifyDescription }}</el-text>
            <el-text v-else type="info">-</el-text>
          </template>
        </el-table-column>
        <el-table-column label="申请人" width="120">
          <template #default="{ row }">
            <span>{{ row.modifyUserName || row.rejectedUserName || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="申请时间" width="180">
          <template #default="{ row }">
            <span>{{ formatTime(row.modifyTime || row.rejectedTime || row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleAudit(scope.row)">
              {{ auditType === '0' ? '审核' : '通过' }}
            </el-button>
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
      width="640px"
      @close="auditForm.auditReason = ''"
    >
      <el-alert
        :title="confirmAuditTip"
        type="info"
        :closable="false"
        style="margin-bottom: 20px"
      />
      <el-form :model="auditForm" label-width="100px">
        <el-form-item label="问题描述" v-if="currentRecord">
          <el-text type="info">{{ currentRecord.problemDescription }}</el-text>
        </el-form-item>
        <el-form-item :label="reasonColumnLabel" v-if="auditType !== '0' && currentRecord?.modifyDescription">
          <el-text type="warning">{{ currentRecord.modifyDescription }}</el-text>
        </el-form-item>

        <!-- V3:编辑审核时显示 modification_snapshot 字段 diff(原值 → 新值) -->
        <el-form-item v-if="auditType === '1' && diffFields.length > 0" label="字段变更">
          <div style="display: flex; flex-direction: column; gap: 6px; width: 100%">
            <div
              v-for="field in diffFields"
              :key="field.key"
              style="display: flex; align-items: center; gap: 8px; flex-wrap: wrap; padding: 4px 0"
            >
              <el-tag size="small" type="info">{{ field.label }}</el-tag>
              <el-tag size="small" type="danger">{{ field.from || '(空)' }}</el-tag>
              <el-icon><Right /></el-icon>
              <el-tag size="small" type="success">{{ field.to || '(空)' }}</el-tag>
            </div>
          </div>
        </el-form-item>

        <el-form-item label="审核意见">
          <el-input
            v-model="auditForm.auditReason"
            type="textarea"
            rows="4"
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
        :title="rejectAuditTip"
        type="warning"
        :closable="false"
        style="margin-bottom: 20px"
      />
      <el-form :model="rejectForm" label-width="100px">
        <el-form-item label="问题描述" v-if="currentRecord">
          <el-text type="info">{{ currentRecord.problemDescription }}</el-text>
        </el-form-item>
        <el-form-item :label="reasonColumnLabel" v-if="auditType !== '0' && currentRecord?.modifyDescription">
          <el-text type="warning">{{ currentRecord.modifyDescription }}</el-text>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Right } from '@element-plus/icons-vue'
import { monthlyRecordApi, getDeptList } from '@/api/monthlyRecord'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const rejectDialogVisible = ref(false)
const currentRecord = ref(null)
const departmentList = ref([])

// 审核类型：0=回退审核  1=编辑审核  2=删除审核
const auditType = ref('0')

const auditTypeLabel = computed(() => ({
  '0': '回退审核',
  '1': '编辑审核',
  '2': '删除审核'
}[auditType.value] || '审核'))

// 删除审核填的是删除原因，其余填修改原因
const reasonColumnLabel = computed(() => auditType.value === '2' ? '删除说明' : '修改说明')

const confirmAuditTip = computed(() => {
  if (auditType.value === '1') return '审核通过后,本次修改的字段值将直接生效(原值 → 新值),记录回到待提交状态'
  if (auditType.value === '2') return '审核通过后,该记录将被删除且不可恢复'
  return '审核通过后将记录状态回退到待整改'
})

const rejectAuditTip = computed(() => {
  if (auditType.value === '1') return '拒绝后该记录将保持原状态(已整改),编辑申请作废(modification_snapshot 清空)'
  if (auditType.value === '2') return '拒绝后删除申请作废,该记录回到待提交状态'
  return '拒绝后该记录状态回到已整改'
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const queryParams = reactive({
  problemDescription: '',
  departmentId: '',
  modifyUserName: '',
  startDate: null,
  endDate: null
})

const auditForm = reactive({
  auditReason: ''
})

const rejectForm = reactive({
  rejectReason: ''
})

// V3:从 currentRecord.modificationSnapshot 解析原值/新值对比
const FIELD_LABELS = {
  responsiblePerson: '责任人',
  responsibleUnit: '责任单位',
  location: '发现位置',
  problemDescription: '问题描述',
  qualityId: '性质界定',
  qualityName: '性质界定',
  departmentId: '受检单位',
  departmentName: '受检单位',
  checkUnitId: '检查单位',
  checkUnitName: '检查单位',
  rectificationDate: '整改期限',
  inspector: '检查人'
}
const diffFields = computed(() => {
  const raw = currentRecord.value?.modificationSnapshot
  if (!raw) return []
  let parsed
  try {
    parsed = typeof raw === 'string' ? JSON.parse(raw) : raw
  } catch (e) {
    return []
  }
  if (!parsed || typeof parsed !== 'object') return []
  return Object.keys(parsed).map(k => ({
    key: k,
    label: FIELD_LABELS[k] || k,
    from: parsed[k]?.from,
    to: parsed[k]?.to
  }))
})

function getCurrentUserId() {
  let userId = localStorage.getItem('userId')
  if (!userId) {
    try {
      const userInfoStr = localStorage.getItem('userInfo')
      if (userInfoStr) {
        const userInfo = JSON.parse(userInfoStr)
        userId = userInfo.id || userInfo.userId
      }
    } catch (_) { /* ignore */ }
  }
  if (!userId) {
    userId = localStorage.getItem('user_id') || localStorage.getItem('id')
  }
  return userId
}

function formatTime(value) {
  if (!value) return '-'
  try {
    const d = new Date(value)
    if (isNaN(d.getTime())) return value
    const pad = (n) => String(n).padStart(2, '0')
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
  } catch (_) {
    return value
  }
}

const handleSearch = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      auditType: Number(auditType.value),
      problemDescription: queryParams.problemDescription || undefined,
      departmentId: queryParams.departmentId || undefined,
      modifyUserName: queryParams.modifyUserName || undefined,
      startDate: queryParams.startDate ? new Date(queryParams.startDate).toISOString().split('T')[0] : undefined,
      endDate: queryParams.endDate ? new Date(queryParams.endDate).toISOString().split('T')[0] : undefined
    }
    const response = await monthlyRecordApi.getAuditRecords(params)
    if (response.data) {
      tableData.value = response.data.records || response.data.data || []
      pagination.total = response.data.total || 0
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
  queryParams.modifyUserName = ''
  queryParams.startDate = null
  queryParams.endDate = null
  pagination.current = 1
  handleSearch()
}

const handleTabChange = () => {
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
  const userId = getCurrentUserId()
  if (!userId) {
    ElMessage.error('无法获取当前用户信息，请重新登录')
    return
  }
  try {
    const payload = {
      auditorId: userId,
      auditReason: auditForm.auditReason
    }
    const response = auditType.value === '0'
      ? await monthlyRecordApi.confirmAudit(currentRecord.value.id, payload)
      : await monthlyRecordApi.confirmEditAudit(currentRecord.value.id, payload)
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
      `确定要拒绝这条审核申请吗？`,
      '拒绝确认',
      {
        confirmButtonText: '拒绝',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const payload = auditType.value === '0'
      ? undefined
      : { auditReason: rejectForm.rejectReason }
    const response = auditType.value === '0'
      ? await monthlyRecordApi.rejectAudit(currentRecord.value.id)
      : await monthlyRecordApi.rejectEditAudit(currentRecord.value.id, payload)
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

.audit-tabs {
  margin-bottom: 16px;
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
