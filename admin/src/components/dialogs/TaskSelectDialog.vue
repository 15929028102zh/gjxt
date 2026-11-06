<template>
  <el-dialog
    v-model="dialogVisible"
    title="选择月检任务"
    width="640px"
    :before-close="handleClose"
    destroy-on-close
    class="task-select-dialog"
  >
    <div class="task-select-toolbar">
      <el-input
        v-model="keyword"
        placeholder="按任务名称或编号搜索..."
        clearable
        size="default"
        @keyup.enter="handleSearch"
        @clear="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="primary" plain size="default" @click="handleSearch">
        <el-icon><Search /></el-icon>
        搜索
      </el-button>
      <span class="hint">默认选中最新月检时间段内的任务</span>
    </div>

    <el-table
      v-loading="loading"
      :data="pagedTasks"
      border
      stripe
      highlight-current-row
      :row-class-name="rowClass"
      @row-click="onRowClick"
      @row-dblclick="onRowDblclick"
      :height="380"
      size="default"
      ref="tableRef"
    >
      <el-table-column label="选择" width="68" align="center">
        <template #default="{ row }">
          <el-radio v-model="selectedId" :value="row.id">
            <span></span>
          </el-radio>
        </template>
      </el-table-column>
      <el-table-column prop="taskNo" label="任务编号" width="180" show-overflow-tooltip />
      <el-table-column prop="taskName" label="任务名称" min-width="160" show-overflow-tooltip />
      <el-table-column prop="phase" label="当前阶段" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="phaseTagType(row.phase)" size="small">
            {{ phaseText(row.phase) }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <div class="task-select-pagination">
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50]"
        :pager-count="5"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" :disabled="!selectedId" @click="handleConfirm">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { monthlyTaskApi } from '@/api/monthly'

const props = defineProps({
  visible: { type: Boolean, default: false },
  // 可选：父组件可传入候选任务 ID（用于场景上下文）
  preferTaskId: { type: String, default: '' }
})

const emit = defineEmits(['update:visible', 'confirm', 'cancel'])

const dialogVisible = computed({
  get: () => props.visible,
  set: (v) => emit('update:visible', v)
})

const loading = ref(false)
const keyword = ref('')
const selectedId = ref('')
const tasks = ref([])
const tableRef = ref(null)
let searchTimer = null

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 搜索防抖：用户停止输入 300ms 后自动触发，避免边输边请求
// 仅在弹框已打开、且非首次清空时生效
let keywordWatchEnabled = false
watch(keyword, () => {
  if (!keywordWatchEnabled) return
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    pagination.current = 1
    loadTasks()
  }, 300)
})

// 当前页内表格渲染的数据
const pagedTasks = computed(() => tasks.value)

watch(() => props.visible, async (val) => {
  if (val) {
    if (searchTimer) {
      clearTimeout(searchTimer)
      searchTimer = null
    }
    keyword.value = ''
    keywordWatchEnabled = true
    selectedId.value = ''
    pagination.current = 1
    pagination.size = 10
    await loadTasks()
  } else {
    keywordWatchEnabled = false
  }
})

async function loadTasks() {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size
    }
    const kw = keyword.value.trim()
    if (kw) params.taskName = kw
    const response = await monthlyTaskApi.getPage(params)
    if (response.code === 200) {
      const records = response.data?.records || []
      const now = new Date()
      tasks.value = records.map(t => ({
        ...t,
        phase: computePhase(t, now)
      }))
      pagination.total = response.data?.total || 0
      // 默认选中：父组件传入的优先 ID → "录入期内"任务 → 当前页第一条
      if (props.preferTaskId && tasks.value.some(t => t.id === props.preferTaskId)) {
        selectedId.value = props.preferTaskId
      } else if (tasks.value.length > 0) {
        const inPeriod = tasks.value.find(t => t.phase === 1)
        selectedId.value = inPeriod ? inPeriod.id : tasks.value[0].id
      }
      await nextTick()
      if (selectedId.value && tableRef.value) {
        const row = tasks.value.find(t => t.id === selectedId.value)
        if (row) tableRef.value.setCurrentRow(row)
      }
    } else {
      ElMessage.error(response.message || '加载任务失败')
    }
  } catch (err) {
    console.error('加载任务失败', err)
    ElMessage.error('加载任务失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.current = 1
  loadTasks()
}

function handleSizeChange(size) {
  pagination.size = size
  pagination.current = 1
  loadTasks()
}

function handleCurrentChange(current) {
  pagination.current = current
  loadTasks()
}

function computePhase(task, now) {
  const start = task.inspectionStartTime ? new Date(task.inspectionStartTime) : null
  const end = task.inspectionEndTime ? new Date(task.inspectionEndTime) : null
  const pStart = task.publicityStartTime ? new Date(task.publicityStartTime) : null
  const pEnd = task.publicityEndTime ? new Date(task.publicityEndTime) : null
  if (start && end && now >= start && now <= end) return 1
  if (pStart && pEnd && now >= pStart && now <= pEnd) return 3
  if (end && now > end && pStart && now < pStart) return 2
  return 0
}

function phaseTagType(phase) {
  const map = { 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }
  return map[phase] || 'info'
}

function phaseText(phase) {
  const map = { 0: '未开始', 1: '录入期内', 2: '公示前', 3: '公示期内' }
  return map[phase] || '未知'
}

function rowClass({ row }) {
  return row.id === selectedId.value ? 'is-selected-row' : ''
}

function onRowClick(row) {
  selectedId.value = row.id
}

function onRowDblclick(row) {
  selectedId.value = row.id
  handleConfirm()
}

function handleConfirm() {
  const task = tasks.value.find(t => t.id === selectedId.value)
  if (!task) {
    ElMessage.warning('请先选择任务')
    return
  }
  emit('confirm', task)
}

function handleClose() {
  emit('cancel')
  dialogVisible.value = false
}
</script>

<style scoped>
.task-select-toolbar {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 12px;
}
.task-select-toolbar .hint {
  color: var(--el-text-color-secondary, #909399);
  font-size: 12px;
}
.task-select-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}
:deep(.is-selected-row) {
  background-color: var(--el-color-primary-light-9, #ecf5ff) !important;
}
.dialog-footer {
  text-align: right;
}
</style>
