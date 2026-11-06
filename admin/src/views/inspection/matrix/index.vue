<template>
  <div class="matrix-container">
    <div class="page-header">
      <h2>周检数据汇总</h2>
      <p>按选择条件统计周检记录数量，按部门-专业透视展示。</p>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-section" shadow="never">
      <el-form :inline="true" :model="filters">
        <el-form-item label="部门">
          <el-select v-model="filters.departmentId" placeholder="选择部门" clearable filterable style="width: 220px">
            <el-option v-for="dept in departmentList" :key="dept.id" :label="dept.deptName" :value="dept.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filters.status" placeholder="全部状态" clearable style="width: 180px">
            <el-option v-for="opt in statusOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="filters.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="fetchData">查询</el-button>
          <el-button :icon="Refresh" @click="resetFilters">重置</el-button>
          <el-button type="success" :icon="Download" @click="exportExcel">导出Excel</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 透视矩阵表格 -->
    <el-card class="table-section" shadow="never">
      <el-table :data="pivotRows" border style="width: 100%" v-loading="loading">
        <el-table-column prop="departmentName" label="部门" fixed="left" min-width="160" />
        <el-table-column v-for="col in dynamicColumns" :key="col" :prop="col" :label="col" align="center" min-width="120" />
        <el-table-column prop="rowTotal" label="总计" align="center" min-width="120" />
      </el-table>

      <div class="summary">共 {{ pivotRows.length }} 个部门，合计 {{ grandTotal }} 条记录。</div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, Download } from '@element-plus/icons-vue'
import * as XLSX from 'xlsx'
import { inspectionApi, getDeptList } from '@/api/inspection'

// 过滤条件
const filters = reactive({
  departmentId: '',
  status: '',
  dateRange: null
})

// 状态选项（与后端约定值保持一致）
const statusOptions = [
  { label: '全部', value: '' },
  { label: '待整改', value: 0 },
  { label: '已整改', value: 1 },
  { label: '已复查', value: 2 },
  { label: '已提交', value: 4 }
]

// 部门列表
const departmentList = ref([])
const loading = ref(false)

// 原始数据与透视结果
const rawList = ref([])
const dynamicColumns = ref([])
const pivotRows = ref([])
const grandTotal = ref(0)

// 加载部门列表
const loadDepartments = async () => {
  try {
    const res = await getDeptList()
    const list = Array.isArray(res)
      ? res
      : (Array.isArray(res?.data) ? res.data : (res?.data?.list || []))
    departmentList.value = list
  } catch (e) {
    console.error('加载部门失败:', e)
  }
}

// 获取当前用户部门（用于默认过滤）
const initUserDept = () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const isAdmin = userInfo.isAdmin === 1
    if (!isAdmin && userInfo.deptId) {
      filters.departmentId = userInfo.deptId
    }
  } catch {}
}

// 拉取矩阵数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      departmentId: filters.departmentId || undefined,
      status: filters.status === '' ? undefined : filters.status,
      startDate: filters.dateRange?.[0] || undefined,
      endDate: filters.dateRange?.[1] || undefined
    }
    const res = await inspectionApi.getDepartmentProfessionMatrix(params)
    const list = Array.isArray(res?.data) ? res.data : (Array.isArray(res) ? res : [])
    rawList.value = list
    buildPivot()
  } catch (e) {
    console.error('获取矩阵数据失败:', e)
    ElMessage.error('获取矩阵数据失败')
  } finally {
    loading.value = false
  }
}

// 透视构建：部门为行，专业为列
const buildPivot = () => {
  // 列：所有出现过的专业名
  const professions = Array.from(new Set((rawList.value || []).map(i => i.profession_name || i.professionName))).filter(Boolean)
  dynamicColumns.value = professions

  // 行分组：部门
  const byDept = {}
  for (const item of rawList.value || []) {
    const deptName = item.department_name || item.departmentName || '-'
    const profName = item.profession_name || item.professionName || '-'
    const count = Number(item.total || item.count || 0)
    if (!byDept[deptName]) byDept[deptName] = {}
    byDept[deptName][profName] = (byDept[deptName][profName] || 0) + count
  }

  const rows = []
  let overall = 0
  for (const [deptName, profMap] of Object.entries(byDept)) {
    const row = { departmentName: deptName, rowTotal: 0 }
    for (const col of dynamicColumns.value) {
      const val = profMap[col] || 0
      row[col] = val
      row.rowTotal += val
    }
    overall += row.rowTotal
    rows.push(row)
  }
  pivotRows.value = rows
  grandTotal.value = overall
}

const resetFilters = () => {
  filters.status = ''
  filters.dateRange = null
  // 保留部门筛选以便非管理员默认部门不被清空
  fetchData()
}

// 导出Excel（按当前页面列结构导出）
const exportExcel = () => {
  try {
    if (!pivotRows.value || pivotRows.value.length === 0) {
      ElMessage.warning('暂无数据可导出')
      return
    }

    // 表头：部门 + 动态专业列 + 总计
    const headers = ['部门', ...dynamicColumns.value, '总计']

    // 行数据：按当前透视表结构输出
    const rows = pivotRows.value.map(row => {
      const dept = row.departmentName
      const values = dynamicColumns.value.map(col => Number(row[col] || 0))
      const total = Number(row.rowTotal || 0)
      return [dept, ...values, total]
    })

    // 列总计行（与页面汇总一致）
    const columnTotals = dynamicColumns.value.map(col => {
      return pivotRows.value.reduce((sum, r) => sum + Number(r[col] || 0), 0)
    })
    const totalRow = ['合计', ...columnTotals, Number(grandTotal.value || 0)]

    const aoa = [headers, ...rows, totalRow]
    const ws = XLSX.utils.aoa_to_sheet(aoa)
    // 列宽优化
    ws['!cols'] = [{ wch: 18 }, ...dynamicColumns.value.map(() => ({ wch: 10 })), { wch: 10 }]

    const wb = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(wb, ws, '周检数据汇总')
    const fileName = `周检数据汇总_${new Date().toISOString().slice(0,10)}.xlsx`
    XLSX.writeFile(wb, fileName)
  } catch (e) {
    console.error('导出Excel失败:', e)
    ElMessage.error('导出失败，请稍后重试')
  }
}

onMounted(async () => {
  initUserDept()
  await loadDepartments()
  await fetchData()
})
</script>

<style scoped>
.matrix-container { padding: 20px; }
.page-header { margin-bottom: 16px; }
.page-header h2 { margin: 0 0 6px 0; font-size: 20px; font-weight: 600; }
.page-header p { margin: 0; color: #606266; }
.search-section { margin-bottom: 16px; }
.table-section { margin-top: 8px; }
.summary { margin-top: 12px; color: #606266; }
</style>