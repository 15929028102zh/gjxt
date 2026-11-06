<template>
  <div class="monthly-delayed-container">
    <div class="page-header">
      <h2>月检延迟整改</h2>
      <p>查询整改期限小于当前日期的月检记录</p>
    </div>

    <div class="search-section">
      <el-card shadow="never">
        <el-form :model="searchForm" inline>
          <el-form-item label="问题描述">
            <el-input v-model="searchForm.problemDescription" placeholder="请输入问题描述" clearable style="width: 220px" />
          </el-form-item>
          <el-form-item label="所属部门">
            <el-select v-model="searchForm.responsibilityId" placeholder="请选择部门" clearable filterable style="width: 220px" :disabled="!isAdminUser">
              <el-option v-for="dept in departmentOptions" :key="dept.id" :label="dept.deptName" :value="dept.id" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
            <el-button :icon="Refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <div class="table-section">
      <el-table :data="tableData" v-loading="loading" stripe border style="width: 100%">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="taskNo" label="任务编号" width="150" show-overflow-tooltip>
          <template #default="{ row }">{{ formatTaskNo(row.taskNo) }}</template>
        </el-table-column>
        <el-table-column prop="problemDescription" label="问题描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="responsiblePerson" label="责任人" width="100" />
        <el-table-column prop="responsibleUnit" label="责任单位" width="120" />
        <el-table-column prop="inspector" label="检查人" width="100" />
        <el-table-column prop="location" label="发生部位" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="350" fixed="right">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.recordStatus)" style="margin-right: 8px;">
              {{ getStatusText(row.recordStatus) }}
            </el-tag>
            <el-button type="primary" size="small" :icon="View" @click="handleView(row)">查看</el-button>
            <el-button v-if="row.recordStatus === 0" type="warning" size="small" :icon="EditPen" @click="handleRectification(row)">确认整改</el-button>
            <el-button v-if="row.recordStatus === 1" type="success" size="small" :icon="CircleCheck" @click="handleReview(row)">确认复查</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

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

    <ViewDialog v-model:visible="viewDialogVisible" :form-data="currentRecord" />
    <RectificationConfirmDialog v-model:visible="rectificationDialogVisible" :form-data="currentRecord" @success="reload" />
    <ReviewDialog v-model:visible="reviewDialogVisible" :form-data="currentRecord" @success="reload" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, View, EditPen, CircleCheck } from '@element-plus/icons-vue'
import { monthlyRecordApi, getDeptList } from '@/api/monthlyRecord'
import ViewDialog from '@/views/inspection/monthly/components/ViewDialog.vue'
import RectificationConfirmDialog from '@/views/inspection/monthly/components/RectificationConfirmDialog.vue'
import ReviewDialog from '@/views/inspection/monthly/components/ReviewDialog.vue'

const loading = ref(false)
const tableData = ref([])
const viewDialogVisible = ref(false)
const rectificationDialogVisible = ref(false)
const reviewDialogVisible = ref(false)
const currentRecord = ref({})

const pagination = reactive({ current: 1, size: 20, total: 0 })

const searchForm = reactive({ problemDescription: '', responsibilityId: '' })
const departmentOptions = ref([])
const isAdminUser = ref(false)

const loadDepartments = async () => {
  try {
    const resp = await getDeptList()
    if (resp.code === 200) {
      departmentOptions.value = resp.data || []
    }
  } catch (e) { console.error(e) }
}

const getToday = () => {
  const d = new Date()
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

const getTableData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      rectificationDeadlineBefore: getToday(),
      problemDescription: searchForm.problemDescription
    }
    if (searchForm.responsibilityId) params.responsibilityId = searchForm.responsibilityId
    const resp = await monthlyRecordApi.getPage(params)
    if (resp.code === 200) {
      const records = resp.data.records || []
      const enrich = (row) => {
        // 任务编号补齐：保留后端返回的值，如果没有就设为空
        row.taskNo = row.taskNo || ''
        row.responsibleUnit = row.responsibleUnit || row.responsibilityUnit || ''
        row.responsiblePerson = row.responsiblePerson || row.dutyPerson || ''
        return row
      }
      const filtered = records
        .map(enrich)
        .filter(r => {
          const dline = r.rectificationDeadline || r.rectificationDate
          return r.recordStatus === 0 && getDelayedDays(dline) > 0
        })
      tableData.value = filtered
      pagination.total = filtered.length
    } else {
      ElMessage.error(resp.message || '获取数据失败')
    }
  } catch (e) {
    console.error('获取月检延迟整改数据失败', e)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { pagination.current = 1; getTableData() }
const handleReset = () => { searchForm.problemDescription = ''; searchForm.responsibilityId = ''; pagination.current = 1; getTableData() }
const handleSizeChange = (val) => { pagination.size = val; getTableData() }
const handleCurrentChange = (val) => { pagination.current = val; getTableData() }

const handleView = (row) => { currentRecord.value = { ...row }; viewDialogVisible.value = true }
const handleRectification = (row) => { currentRecord.value = { ...row }; rectificationDialogVisible.value = true }
const handleReview = (row) => { currentRecord.value = { ...row }; reviewDialogVisible.value = true }
const reload = () => { getTableData() }

const formatDate = (dateStr) => { if (!dateStr) return ''; const d = new Date(dateStr); return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}` }
const getDelayedDays = (deadline) => { if (!deadline) return 0; const end = new Date(deadline).getTime(); const now = new Date(getToday()).getTime(); if (isNaN(end)) return 0; const diff = Math.floor((now - end) / (24*3600*1000)); return diff > 0 ? diff : 0 }
const getStatusType = (s) => ({0:'warning',1:'success',2:'info'}[s] || 'info')
const getStatusText = (s) => ({0:'待整改',1:'已整改',2:'已复查'}[s] || '未知')

const formatTaskNo = (taskNo) => {
  if (!taskNo) return taskNo
  const match = taskNo.match(/^(YJ)(\d{4})(\d{2})(\d{2})(.*)$/)
  if (match) {
    const [, prefix, year, month, day, suffix] = match
    const monthNum = parseInt(month, 10)
    return `${prefix}${year}年${monthNum}月${day}日${suffix}`
  }
  return taskNo
}

onMounted(async () => {
  try {
    const userInfoStr = localStorage.getItem('userInfo')
    if (userInfoStr) {
      const info = JSON.parse(userInfoStr)
      isAdminUser.value = info.isAdmin === 1
      if (!isAdminUser.value) {
        searchForm.responsibilityId = info.deptId || ''
      }
    }
  } catch (_) {}
  await loadDepartments()
  await getTableData()
})
</script>

<style scoped>
.monthly-delayed-container { padding: 20px; }
.page-header { margin-bottom: 16px; }
.page-header h2 { margin: 0 0 6px 0; font-size: 20px; font-weight: 600; color: #303133; }
.page-header p { margin: 0; color: #606266; font-size: 14px; }
.search-section { margin-bottom: 16px; }
.table-section { margin-bottom: 16px; }
.pagination-section { display: flex; justify-content: flex-end; }
.action-buttons { display: flex; gap: 8px; }
</style>