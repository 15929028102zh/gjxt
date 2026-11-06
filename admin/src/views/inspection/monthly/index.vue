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
          <SearchableSelect
            v-model="searchForm.professionId"
            :options="professionOptions"
            label-key="name"
            value-key="id"
            placeholder="请选择专业"
            style="width: 120px;"
          />
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
        <el-form-item :label="deptFilterLabel">
          <el-select
            v-model="searchForm.departmentId"
            :placeholder="deptFilterLabel === '所属部门' ? '请选择部门' : `请选择${deptFilterLabel}`"
            :disabled="!isMonthlyAdminUser"
            clearable
            style="width: 150px;"
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
            filterable
            :loading="taskOptionLoading"
            style="width: 200px;"
            @visible-change="onTaskSelectVisible"
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
            <el-option label="待审核" value="3" />
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
      <el-button type="primary" @click="handleAdd" :icon="Plus">新增</el-button>
      <el-button type="success" @click="handleExport" :icon="Download" v-if="isMonthlyAdminUser">导出</el-button>
    </div>

    <!-- 任务时间段没配全：记录算不出录入/公示阶段，操作按钮会整体消失，这里给出原因和补救入口 -->
    <el-alert
      v-if="unconfiguredTasks.length"
      type="warning"
      :closable="false"
      show-icon
      class="task-time-warning"
    >
      <template #title>
        有 {{ unconfiguredTasks.length }} 个月检任务未配置时间段，相关记录无法判断所处阶段，操作按钮已隐藏
      </template>
      <div v-for="t in unconfiguredTasks" :key="t.taskId" class="task-time-warning-item">
        <span class="task-time-warning-name">{{ t.taskName || t.taskNo || t.taskId }}（{{ t.taskNo }}）：缺少{{ t.missing.join('、') }}</span>
        <el-button v-if="isMonthlyAdminUser" type="primary" link @click="goFixTaskTime(t)">去补配</el-button>
        <span v-else class="task-time-warning-hint">请联系月检管理员补配</span>
      </div>
    </el-alert>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        stripe
        border
      >
        <el-table-column prop="taskName" label="月检任务名称" width="180" show-overflow-tooltip />
        <el-table-column v-if="showCheckUnitColumn" prop="checkUnitName" label="检查单位" width="120" show-overflow-tooltip />
        <el-table-column prop="checkUnitName" label="检查部门" width="120" show-overflow-tooltip />
        <el-table-column v-if="showInspectedUnitColumn" prop="departmentName" label="受检单位" width="120" show-overflow-tooltip />
        <el-table-column prop="problemDescription" label="问题描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="responsiblePerson" label="责任人" width="100" />
        <el-table-column prop="qualityName" label="问题性质" width="120" />
        <el-table-column prop="inspector" label="检查人" width="100" />
        <el-table-column prop="location" label="发现位置" width="120" />
        <el-table-column label="性质界定是否修改" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.natureModified === 1 ? 'warning' : 'info'" size="small">
              {{ row.natureModified === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="rectifierName" label="整改人" width="100">
          <template #default="{ row }">
            {{ row.rectifierName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="reviewerName" label="复查人" width="100">
          <template #default="{ row }">
            {{ row.reviewerName || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="500" fixed="right">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.recordStatus)" style="margin-right: 8px;">
              {{ getStatusText(row.recordStatus) }}
            </el-tag>
            <el-button type="primary" size="small" @click="handleView(row)" :icon="View">查看</el-button>
            <el-tooltip
              v-if="row.recordStatus === 0 && showRectificationButton"
              :disabled="isAfterPublicity(row)"
              content="公示期结束后才可确认整改"
              placement="top"
            >
              <!-- 禁用状态下按钮不响应鼠标事件，用 span 包裹保证 tooltip 仍能触发；
                   因为包裹后不再是 el-button 直接相邻，需手动补上左右间距 -->
              <span style="margin: 0 8px;">
                <el-button
                  type="success"
                  size="small"
                  :disabled="!isAfterPublicity(row)"
                  @click="handleRectificationConfirm(row)"
                  :icon="Check"
                >
                  确认整改
                </el-button>
              </span>
            </el-tooltip>
            <el-button
              v-if="row.recordStatus === 1 && showReviewButton"
              type="warning"
              size="small"
              @click="handleReview(row)"
              :icon="CircleCheck"
            >
              确认复查
            </el-button>
            <el-button
              v-if="canWithdraw(row) && row.recordStatus !== 3"
              type="info"
              size="small"
              @click="handleReject(row)"
              :icon="canWithdrawIcon(row)"
            >
              {{ rejectButtonText(row) }}
            </el-button>
            <el-button
              v-if="canDelete(row) && row.recordStatus !== 3"
              type="danger"
              size="small"
              @click="handleDelete(row)"
              :icon="Delete"
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

    <!-- 新增/编辑弹窗(V3:auditMode 决定走 update 还是 submitEditAudit) -->
    <AddDialog
      v-model:visible="addDialogVisible"
      :form-data="currentRow"
      :is-in-inspection-period="isInInspectionPeriod"
      :audit-mode="currentRow?.auditMode === true"
      @success="handleAddSuccess"
    />

    <!-- 任务选择弹窗 -->
    <TaskSelectDialog
      v-model:visible="taskSelectVisible"
      :prefer-task-id="searchForm.taskId"
      @confirm="handleTaskSelectConfirm"
      @cancel="taskSelectVisible = false"
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

    <!-- 删除审核弹窗（公示期内删除走申请+审核） -->
    <EditAuditDialog
      v-model:visible="deleteAuditVisible"
      :record="currentRow"
      :audit-type="2"
      @success="handleDeleteAuditSuccess"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Download, Delete, View, Check, CircleCheck, EditPen, Back } from '@element-plus/icons-vue'
import * as XLSX from 'xlsx'
import { hasMonthlyPermission, isMonthlyAdmin } from '@/utils/permission'
import { phaseOfRecord, computeTaskPhase, hasTaskTimes, missingTaskTimeRanges } from '@/utils/taskPhase'
import { monthlyRecordApi, getNatureDefinitionList, getDeptList, monthlyTaskApi } from '@/api/monthlyRecord'
import { gjbzDataApi } from '@/api/gjbz'
import AddDialog from './components/AddDialog.vue'
import TaskSelectDialog from '@/components/dialogs/TaskSelectDialog.vue'
import SearchableSelect from '@/components/SearchableSelect.vue'
import RectificationConfirmDialog from './components/RectificationConfirmDialog.vue'
import RectificationDialog from './components/RectificationDialog.vue'
import ReviewDialog from './components/ReviewDialog.vue'
import ViewDialog from './components/ViewDialog.vue'
import EditAuditDialog from './components/EditAuditDialog.vue'

const route = useRoute()
const router = useRouter()

// 响应式数据
const loading = ref(false)
const tableData = ref([])
const addDialogVisible = ref(false)
const taskSelectVisible = ref(false)
const rectificationConfirmDialogVisible = ref(false)
const rectificationDialogVisible = ref(false)
const reviewDialogVisible = ref(false)
const viewDialogVisible = ref(false)
const deleteAuditVisible = ref(false)
const currentRow = ref({})
const isInInspectionPeriod = ref(true)

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
// 月检管理员：仅月检管理员可自由选择部门；非月检管理员登录时部门下拉预填本部门并锁定
const isMonthlyAdminUser = computed(() => {
  return isMonthlyAdmin()
})

// 未配置时间段的月检任务（按任务去重）。
// 这类任务的记录 taskPhase 恒为 0，回退/编辑/删除按钮全部隐藏且无任何提示，
// 用户会以为系统出错，这里把原因和补救入口显式摆出来。
const unconfiguredTasks = computed(() => {
  const map = new Map()
  for (const row of tableData.value) {
    if (!row.taskId || row.taskPhase !== 0) continue
    const missing = missingTaskTimeRanges(row)
    if (!missing.length) continue
    if (!map.has(row.taskId)) {
      map.set(row.taskId, {
        taskId: row.taskId,
        taskNo: row.taskNo || '',
        taskName: row.taskName || '',
        missing
      })
    }
  }
  return [...map.values()]
})

// 跳到月检任务页，按任务编号直接打开编辑弹窗补配时间段
const goFixTaskTime = (task) => {
  router.push({ path: '/monthly/task', query: { taskNo: task.taskNo || task.taskId } })
}

// 当前用户的部门类型:1=管理部门,0=运行部门,null=未知/管理员
const userDeptType = computed(() => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    if (!userInfo.deptId) return null
    const myDept = departmentList.value.find(d => String(d.id) === String(userInfo.deptId))
    return myDept && myDept.deptType !== null && myDept.deptType !== undefined ? myDept.deptType : null
  } catch {
    return null
  }
})

// 按部门 ID 解析部门类型
const resolveDeptType = (deptId) => {
  if (!deptId) return null
  const d = departmentList.value.find(item => String(item.id) === String(deptId))
  return d && d.deptType !== null && d.deptType !== undefined ? d.deptType : null
}

// 按部门 ID 决定用 checkUnitId(管理部门) 还是 departmentId(运行部门) 过滤
// 兜底:如果 resolveDeptType 返回 null(部门列表未加载/部门被过滤剔除),默认按受检单位过滤
// 这样不会把"该部门作为受检单位"的记录错误丢掉(管理员登录时仍能通过手动选择切换)
const applyDeptFilter = (params, deptId) => {
  if (!deptId) return
  const type = resolveDeptType(deptId)
  if (type === 1) {
    params.checkUnitId = deptId
  } else {
    params.departmentId = deptId
  }
}

// 搜索栏「部门」下拉的标签:按身份动态切换
const deptFilterLabel = computed(() => {
  if (userDeptType.value === 1) return '检查单位'
  if (userDeptType.value === 0) return '受检单位'
  return '所属部门'
})

// 表格「检查单位」列:运行部门/管理员显示,管理部门隐藏(管理部门每行都相同,冗余)
const showCheckUnitColumn = computed(() => {
  return userDeptType.value !== 1
})

// 表格「受检单位」列:管理部门/管理员显示,运行部门隐藏(运行部门每行都相同,冗余)
const showInspectedUnitColumn = computed(() => {
  return userDeptType.value !== 0
})

// 确认整改按钮:管理部门(deptType=1)不可见——整改由运行部门(受检单位)执行,管理员/运行部门可见
const showRectificationButton = computed(() => {
  return userDeptType.value !== 1
})

// 确认复查按钮:运行部门(deptType=0)不可见——复查由管理部门(检查单位)执行,管理员/管理部门可见
const showReviewButton = computed(() => {
  return userDeptType.value !== 0
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
      recordStatus: statusNum == null || Number.isNaN(statusNum) ? undefined : statusNum,
      responsiblePerson: searchForm.responsiblePerson || undefined,
      inspector: searchForm.inspector || undefined
    }

    // 主列表只展示已提交且在流程中的记录(0/1/2/3);
    // 状态 4(待提交)只出现在 monthlySubmit 页面,不在这里显示
    // 注意：后端 controller 的 @RequestParam 名是 recordStatus（数组/单值都行，Spring 自动识别），
    // 它收到后会放进 params map 的 recordStatusList key 给 mapper 用。所以前端必须发 recordStatus=...（数组形式），
    // 发 recordStatusList=... 后端 Spring 接收不到，过滤条件不触发、status=4 漏出来。
    if (statusNum === undefined) {
      params.recordStatus = [0, 1, 2, 3]
    }

    // 非管理员：管理部门按检查单位(checkUnitId)过滤，运行部门按受检单位(departmentId)过滤
    // 手动选择的下拉项也按所选部门的 deptType 决定过滤字段，保持与下拉筛选一致
    if (!isMonthlyAdminUser.value && !searchForm.departmentId) {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      if (userInfo.deptId) {
        applyDeptFilter(params, userInfo.deptId)
      }
    } else if (searchForm.departmentId) {
      applyDeptFilter(params, searchForm.departmentId)
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
      // 兜底补齐行数据：任务名称、责任单位、责任人、任务阶段
      const enrich = (row) => {
        // 任务名称补齐：优先后端返回，其次通过已加载的任务选项匹配
        if (!row.taskName && row.taskId && Array.isArray(taskOptions.value) && taskOptions.value.length > 0) {
          const t = taskOptions.value.find(tk => tk.id === row.taskId)
          if (t) row.taskName = t.originalName || t.taskName
        }
        // 任务阶段：分页接口已关联带出任务时间字段，同步计算。
        // 异步回填会写到原始对象、绕过 Vue 响应式代理，导致回退/编辑按钮不渲染。
        // 1=录入期、2=录入期结束到公示期前、3=公示期、4=公示期后、0=其它（不允许回退）
        if (row.taskId) {
          row.taskPhase = phaseOfRecord(row)
        }
        // 责任单位与责任人兜底（保持字段名一致）
        row.responsibleUnit = row.responsibleUnit || row.responsibilityUnit || ''
        row.responsiblePerson = row.responsiblePerson || row.dutyPerson || ''
        return row
      }
      tableData.value = (records || []).map(enrich)
      pagination.total = response.data.total || 0
      await fillMissingTaskPhases()
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 兜底补齐任务阶段：后端未升级版本不返回任务时间字段，阶段恒为 0，会导致回退/编辑按钮全部不显示。
// 这里异步拉任务详情算阶段，算完后整体重新赋值 tableData.value 触发重渲染——
// 写原始对象绕过 Vue 响应式代理，正是之前按钮不显示的根因。
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
  // 非月检管理员：重置后保持预填本部门
  if (!isMonthlyAdminUser.value) {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    if (userInfo.deptId) {
      searchForm.departmentId = userInfo.deptId
    }
  }
  pagination.pageNum = 1
  getTableData()
}

// 新增记录：先弹任务选择对话框
const handleAdd = () => {
  taskSelectVisible.value = true
}

function handleTaskSelectConfirm(task) {
  // 校验录入期
  const now = new Date()
  const start = task.inspectionStartTime ? new Date(task.inspectionStartTime) : null
  const end = task.inspectionEndTime ? new Date(task.inspectionEndTime) : null
  isInInspectionPeriod.value = !!(start && end && now >= start && now <= end)
  if (!isInInspectionPeriod.value) {
    ElMessage.warning('当前任务不在月检录入期内，无法新增记录')
    return
  }
  taskSelectVisible.value = false
  searchForm.taskId = task.id
  searchForm.taskNo = task.taskNo
  currentRow.value = {
    taskId: task.id,
    taskNo: task.taskNo,
    taskName: task.taskName
  }
  addDialogVisible.value = true
}

// 扣分值：观察=1, 一般=3, 重要=5；其它返回空
function getDeductionScore(qualityName) {
  if (qualityName === '观察') return 1
  if (qualityName === '一般') return 3
  if (qualityName === '重要') return 5
  return ''
}

// 整改期限：YYYY.M.D（不使用前导 0）
function formatRectificationDate(value) {
  if (!value) return ''
  const s = String(value)
  const m = s.match(/^(\d{4})-(\d{1,2})-(\d{1,2})/)
  if (m) {
    return `${parseInt(m[1], 10)}.${parseInt(m[2], 10)}.${parseInt(m[3], 10)}`
  }
  // 兜底：尝试作为 Date 解析
  const d = new Date(s)
  if (!isNaN(d.getTime())) {
    return `${d.getFullYear()}.${d.getMonth() + 1}.${d.getDate()}`
  }
  return s
}

// 对照岗检标准的条款 = 基本模块 / 责任要素 / 检查内容
function buildStandardClause(record) {
  const parts = [
    record.moduleName,
    record.responsibilityName,
    record.issueName
  ].filter(v => v != null && String(v).trim() !== '')
  return parts.join('/')
}

// 表头固定：序号 / 受检单位 / 存在问题 / 专业类别 / 检查人 / 扣分值 / 问题属性 / 整改期限 / 对照岗检标准的条款 / 备注
const EXPORT_HEADERS = [
  '序号', '受检单位', '存在问题', '专业类别', '检查人',
  '扣分值', '问题属性', '整改期限', '对照岗检标准的条款', '备注'
]

const EXPORT_TITLE = '岗位责任制大检查问题清单'

/**
 * 收集导出参数（复用搜索条件 + 强制拉满分页为单页一次性导出）
 */
function collectExportParams() {
  const params = {}
  if (searchForm.taskId) params.taskId = searchForm.taskId
  if (searchForm.taskNo) params.taskNo = searchForm.taskNo
  if (searchForm.professionId) params.professionalTypeId = searchForm.professionId
  if (searchForm.responsiblePerson) params.responsiblePerson = searchForm.responsiblePerson
  if (searchForm.inspector) params.inspector = searchForm.inspector

  const statusRaw = searchForm.status
  const statusNum = statusRaw === '' || statusRaw == null ? undefined : Number(statusRaw)
  if (statusNum !== undefined && !Number.isNaN(statusNum)) {
    params.recordStatus = statusNum
  } else {
    // 主列表默认排除 4(待提交),与 getTableData 保持一致
    // 注意：前端必须发 recordStatus（不是 recordStatusList），后端 controller 才接收得到
    params.recordStatus = [0, 1, 2, 3]
  }

  if (!isMonthlyAdminUser.value && !searchForm.departmentId) {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    if (userInfo.deptId) applyDeptFilter(params, userInfo.deptId)
  } else if (searchForm.departmentId) {
    applyDeptFilter(params, searchForm.departmentId)
  }

  if (searchForm.dateRange && searchForm.dateRange.length === 2) {
    params.startDate = searchForm.dateRange[0]
    params.endDate = searchForm.dateRange[1]
  }
  return params
}

// 收集全部命中记录（不分页，一次拉完）
async function fetchAllMatchedRecords() {
  const params = collectExportParams()
  // 一次性拉满：服务端分页 size 上限根据 MyBatis Plus / DB 情况已用 1000 测试无压力；超过则分批
  const EXPORT_BATCH = 1000
  let allRecords = []
  let current = 1
  let total = 0
  // 首轮
  const first = await monthlyRecordApi.getPage({ ...params, current, size: EXPORT_BATCH })
  if (first.code !== 200) {
    throw new Error(first.message || '获取数据失败')
  }
  const firstRecords = first.data?.records || []
  total = first.data?.total || 0
  allRecords = allRecords.concat(firstRecords)
  // 后续轮
  while (allRecords.length < total) {
    current += 1
    const next = await monthlyRecordApi.getPage({ ...params, current, size: EXPORT_BATCH })
    if (next.code !== 200) break
    const recs = next.data?.records || []
    if (!recs || recs.length === 0) break
    allRecords = allRecords.concat(recs)
  }
  return allRecords
}

// Sheet 名只允许 31 字内、不含 :\/?*[]，处理超长 / 非法字符
function sanitizeSheetName(name) {
  const cleaned = String(name || '未分类')
    .replace(/[:\\/?*\[\]]/g, '_')
    .slice(0, 31)
  return cleaned || '未分类'
}

/**
 * 构造导出一个 sheet 的二维数组：
 *   第 1 行：标题（合并后续表头铺满整行）
 *   第 2 行：表头
 *   第 3 行起：数据
 */
function buildSheetData(records, departmentName) {
  const rows = []
  rows.push([EXPORT_TITLE])
  rows.push(EXPORT_HEADERS)
  records.forEach((r, idx) => {
    const score = getDeductionScore(r.qualityName)
    rows.push([
      idx + 1,
      r.departmentName || departmentName || '',
      r.problemDescription || '',
      r.professionalTypeName || '',
      r.inspector || '',
      score === '' ? '' : `${score} 分`,
      r.qualityName || '',
      formatRectificationDate(r.rectificationDeadline || r.rectificationDate),
      buildStandardClause(r),
      ''
    ])
  })
  return rows
}

// 导出全部命中数据为 Excel
const handleExport = async () => {
  // 防止重复点击
  if (loading.value) {
    ElMessage.warning('数据加载中，请稍候')
    return
  }
  loading.value = true
  try {
    const records = await fetchAllMatchedRecords()
    if (!records || records.length === 0) {
      ElMessage.warning('当前查询条件下没有可导出的数据')
      return
    }

    // 兜底补齐：任务名称 + 责任单位/责任人（与 getTableData 中 enrich 行为保持一致）
    const enriched = records.map(row => {
      if (!row.taskName && row.taskId && taskOptions.value?.length) {
        const t = taskOptions.value.find(tk => tk.id === row.taskId)
        if (t) row.taskName = t.originalName || t.taskName
      }
      row.responsibleUnit = row.responsibleUnit || row.responsibilityName || ''
      row.responsiblePerson = row.responsiblePerson || row.dutyPerson || ''
      return row
    })

    // Sheet 固定顺序（与公示模板一致：亮点→7 个生产/储运/保障单位→后勤综治→其他）
    // 不在表内的部门统一放到末尾（仍按部门名升序）
    const SHEET_ORDER = [
      '亮点', '乙烯运行部', '全密度聚乙烯运行部', '高密度聚乙烯运行部',
      '储运部', '公用工程部', '运行保障部', '后勤综治', '其他'
    ]
    // 部门 → 归属 sheet 映射（按公示模板分组：宝石花/昆仑物流/恒泰运输 进后勤综治；机动设备部/控制管理部/机电仪中心西北项目部/消防队 进其他）
    // 受检单位列保持原始部门名；只改 sheet 名
    const DEPT_TO_SHEET = {
      '宝石花': '后勤综治',
      '昆仑物流': '后勤综治',
      '恒泰运输': '后勤综治',
      '机动设备部': '其他',
      '控制管理部': '其他',
      '机电仪中心西北项目部': '其他',
      '消防队': '其他'
    }
    // 必须提前到 forEach 之前声明，否则回调里 resolveSheetName 处于 TDZ 会抛
    // "Cannot access 'xxx' before initialization"
    const resolveSheetName = (dept) => DEPT_TO_SHEET[dept] || dept
    const sheetRank = (name) => {
      const idx = SHEET_ORDER.indexOf(name)
      return idx === -1 ? Number.MAX_SAFE_INTEGER : idx
    }

    // 按 sheet 名分组：受检单位→后勤综治/其他 映射后归并；其他部门以自身名称作为 sheet 名；空值归入「未分类」
    // 数据行的「受检单位」列仍保留原始部门名（不替换为 sheet 名）
    const groups = new Map()
    enriched.forEach(r => {
      const dept = (r.departmentName || '').trim() || '未分类'
      const sheetKey = dept === '未分类' ? dept : resolveSheetName(dept)
      if (!groups.has(sheetKey)) groups.set(sheetKey, [])
      groups.get(sheetKey).push(r)
    })

    const wb = XLSX.utils.book_new()

    const sortedKeys = Array.from(groups.keys()).sort((a, b) => {
      const ra = sheetRank(a), rb = sheetRank(b)
      if (ra !== rb) return ra - rb
      return a.localeCompare(b, 'zh-CN')
    })

    sortedKeys.forEach(dept => {
      const list = groups.get(dept).slice().sort((a, b) => {
        const an = (a.recordNo || '').toString()
        const bn = (b.recordNo || '').toString()
        return an.localeCompare(bn)
      })
      const sheetRows = buildSheetData(list, dept)
      const ws = XLSX.utils.aoa_to_sheet(sheetRows)

      // 合并标题单元格：第 1 行第 1 列 -> 最后一列
      ws['!merges'] = [{ s: { r: 0, c: 0 }, e: { r: 0, c: EXPORT_HEADERS.length - 1 } }]

      // 列宽（按字符宽度粗略设置，确保打印时不挤压）
      ws['!cols'] = [
        { wch: 6 },  // 序号
        { wch: 22 }, // 受检单位
        { wch: 38 }, // 存在问题
        { wch: 14 }, // 专业类别
        { wch: 10 }, // 检查人
        { wch: 8 },  // 扣分值
        { wch: 10 }, // 问题属性
        { wch: 12 }, // 整改期限
        { wch: 36 }, // 对照岗检标准的条款
        { wch: 14 }  // 备注
      ]

      // 标题单元格样式需要通过 xlsx-style 等扩展库设置；这里只设置单元格类型为字符串
      // 设置标题行单元格为合并首格的值即可（已由 aoa_to_sheet 完成）

      const sheetName = sanitizeSheetName(dept)
      XLSX.utils.book_append_sheet(wb, ws, sheetName)
    })

    // 文件名：月检问题清单_yyyyMMdd_HHmmss.xlsx
    const now = new Date()
    const yyyy = now.getFullYear()
    const mm = String(now.getMonth() + 1).padStart(2, '0')
    const dd = String(now.getDate()).padStart(2, '0')
    const HH = String(now.getHours()).padStart(2, '0')
    const MM = String(now.getMinutes()).padStart(2, '0')
    const SS = String(now.getSeconds()).padStart(2, '0')
    const fileName = `月检问题清单_${yyyy}${mm}${dd}_${HH}${MM}${SS}.xlsx`

    XLSX.writeFile(wb, fileName)
    ElMessage.success(`已导出 ${enriched.length} 条记录（${groups.size} 个受检单位）`)
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error(error?.message || '导出失败')
  } finally {
    loading.value = false
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

// 确认整改（新版）：仅公示期结束后（phase 4）可操作，与移动端 isActionableForCurrentUser 门控一致
const handleRectificationConfirm = async (row) => {
  if (row.taskPhase !== 4) {
    ElMessage.warning('公示期结束后才可确认整改')
    return
  }
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

// 确认整改按钮是否可点击：仅公示期结束后（phase 4）启用，之前禁用
const isAfterPublicity = (row) => {
  return row.taskPhase === 4
}

// 是否为"录入该问题的管理部门"：本部门是管理部门(dept_type=1)且该行的检查单位就是本部门
// 月检管理员跨部门管理，豁免该限制
const isCheckUnitOwner = (row) => {
  if (isMonthlyAdminUser.value) {
    return true
  }
  if (userDeptType.value !== 1) {
    return false
  }
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  const myDeptId = userInfo.deptId
  if (!myDeptId || !row.checkUnitId) {
    return false
  }
  return String(row.checkUnitId) === String(myDeptId)
}

// 回退按钮是否可见（公示期前 + 公示期内允许，公示期外禁用）
// phase 3 时按钮文案是"编辑"，走编辑审核通道，只有录入该问题的管理部门能发起
const canWithdraw = (row) => {
  if (row.taskPhase !== 1 && row.taskPhase !== 2 && row.taskPhase !== 3) {
    return false
  }
  if (row.taskPhase === 3 && !isCheckUnitOwner(row)) {
    return false
  }
  return true
}

// 删除按钮是否可见：公示期前(1/2)直接删除、公示期(3)走删除审核，公示期后不可删
const canDelete = (row) => {
  if (row.taskPhase !== 1 && row.taskPhase !== 2 && row.taskPhase !== 3) {
    return false
  }
  return isCheckUnitOwner(row)
}

// 回退按钮图标：公示期用编辑图标，其他用回退图标
const canWithdrawIcon = (row) => {
  return row.taskPhase === 3 ? EditPen : Back
}

// 回退按钮文字（公示期内为"编辑"，其余为"回退"）
const rejectButtonText = (row) => {
  return row.taskPhase === 3 ? '编辑' : '回退'
}

// 删除记录
// phase 1/2：公示期前直接删除（逻辑删除），调 entryDelete
// phase 3：公示期内不能直删，改为提交删除说明走删除审核(auditType=2)
const handleDelete = async (row) => {
  if (row.taskPhase === 3) {
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
    deleteAuditVisible.value = true
    return
  }
  if (row.taskPhase !== 1 && row.taskPhase !== 2) {
    ElMessage.warning('当前不在可删除时段')
    return
  }
  try {
    await ElMessageBox.confirm(
      `确定要删除问题"${row.problemDescription}"吗？删除后该记录不可恢复。`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const response = await monthlyRecordApi.entryDelete(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      getTableData()
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

// 回退/申请记录
// phase 1/2：直接调 withdrawRecord，记录回到 status=4，无需审核
// phase 3：打开 AddDialog(auditMode=true),用户改完字段后走 submitEditAudit 提交待审核,
//          后端把 diff 存到 modification_snapshot,审核通过后由 confirmEditAudit 应用
const handleReject = async (row) => {
  if (row.taskPhase === 0 || row.taskPhase === 4) {
    ElMessage.warning('当前不在可回退时段')
    return
  }
  // phase 1/2：直接撤回
  if (row.taskPhase === 1 || row.taskPhase === 2) {
    try {
      await ElMessageBox.confirm(
        `确定要回退问题"${row.problemDescription}"吗？回退后该记录将回到待提交状态，可重新编辑/提交。`,
        '回退确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      const response = await monthlyRecordApi.withdrawRecord(row.id)
      if (response.code === 200) {
        ElMessage.success('回退成功，记录已回到待提交状态')
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
    return
  }
  // phase 3：打开 AddDialog(auditMode=true),用户在表单内编辑后点击"提交审核"
  try {
    const response = await monthlyRecordApi.getById(row.id)
    if (response.code === 200) {
      currentRow.value = { ...response.data, taskPhase: row.taskPhase, auditMode: true }
    } else {
      currentRow.value = { ...row, auditMode: true }
    }
  } catch (e) {
    currentRow.value = { ...row, auditMode: true }
  }
  addDialogVisible.value = true
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

// 删除审核提交成功回调（成功提示已由 EditAuditDialog 内部弹出，此处不再重复）
const handleDeleteAuditSuccess = () => {
  deleteAuditVisible.value = false
  getTableData()
}

// 获取状态文本
// 状态说明：0-草稿/待整改，1-已整改/已提交，2-已复查/已审核，3-待审核，4-审核确认
const getStatusText = (status) => {
  const statusMap = {
    0: '待整改',
    1: '已整改',
    2: '已复查',
    3: '待审核',
    4: '已确认'
  }
  return statusMap[status] || '未知'
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    0: 'warning',   // 待整改 - 橙色
    1: 'success',   // 已整改 - 绿色
    2: 'info',      // 已复查 - 蓝色
    3: 'danger',    // 待审核 - 红色
    4: 'success'    // 已确认 - 绿色
  }
  return typeMap[status] || 'info'
}

// 加载下拉选项数据
const loadOptions = async () => {
  try {
    const [professionResponse, natureResponse, deptResponse] = await Promise.all([
      gjbzDataApi.getTypes(),
      getNatureDefinitionList(),
      getDeptList()
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
      const raw = natureResponse.data
      natureOptions.value = Array.isArray(raw)
        ? raw.map(item => ({
            id: item.id,
            name: item.dictValue || item.dictLabel || item.name || ''
          }))
        : []
    }

    if (deptResponse.code === 200) {
      const allDepts = deptResponse.data || []
      // 非管理员：按登录人部门动态过滤——只显示与自己部门同类型(运行/管理)的部门；管理员看全部
      if (isMonthlyAdminUser.value) {
        departmentList.value = allDepts
      } else {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const myDept = userInfo.deptId
          ? allDepts.find(d => String(d.id) === String(userInfo.deptId))
          : null
        departmentList.value = (myDept && myDept.deptType !== null && myDept.deptType !== undefined)
          ? allDepts.filter(d => d.deptType === myDept.deptType)
          : allDepts
      }
    }
  } catch (error) {
    console.error('加载选项数据失败:', error)
  }
}

// 月检任务下拉：懒加载。首次打开下拉时拉取，之后命中缓存。
const taskOptionLoading = ref(false)
let taskOptionLoaded = false

async function loadTaskOptions() {
  if (taskOptionLoaded || taskOptionLoading.value) return
  taskOptionLoading.value = true
  try {
    const response = await monthlyTaskApi.getPage({ pageNum: 1, pageSize: 100 })
    if (response.code === 200) {
      const records = response.data?.records || []
      taskOptions.value = records.map(task => {
        let month = ''
        if (task.taskNo && task.taskNo.length >= 10) {
          const dateStr = task.taskNo.substring(2, 10)
          if (dateStr.length === 8) {
            month = `${parseInt(dateStr.substring(4, 6), 10)}`
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
      taskOptionLoaded = true
    }
  } catch (error) {
    console.error('加载月检任务列表失败', error)
  } finally {
    taskOptionLoading.value = false
  }
}

function onTaskSelectVisible(visible) {
  if (visible) loadTaskOptions()
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
    if (!isMonthlyAdminUser.value) {
      // 非管理员，一自根据简杆用户信息获取部门ID，然后来关闭v选一个既建的避故可有特殊的修复整改
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      if (userInfo.deptId) {
        searchForm.departmentId = userInfo.deptId
      }
    }
    getTableData()
  })
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

.task-time-warning {
  margin-bottom: 16px;
}

.task-time-warning-item {
  display: flex;
  align-items: center;
  gap: 8px;
  line-height: 24px;
}

.task-time-warning-name {
  flex: 1;
}

.task-time-warning-hint {
  color: #909399;
  font-size: 12px;
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