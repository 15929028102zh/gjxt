<template>
  <el-dialog
    v-model="dialogVisible"
    title="编辑月检记录"
    width="900px"
    :before-close="handleClose"
  >
    <!-- 只读的基本信息区，保持与查看弹窗一致 -->
    <div class="info-section">
      <h4>基本信息</h4>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="任务编号">{{ viewData.taskNo || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="月检时间">{{ formatDate(viewData.inspectionTime) }}</el-descriptions-item>
        <el-descriptions-item label="检查部门">{{ viewData.departmentName || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="检查专业">{{ viewData.professionalTypeName || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="检查组长">{{ viewData.groupLeaderName || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="检查副组长">{{ viewData.deputyLeaderName || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="专业类别">{{ viewData.professionalTypeName || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="模块">{{ viewData.moduleName || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="责任要素">{{ viewData.responsibilityName || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="检查内容">{{ viewData.issueName || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="性质界定">{{ viewData.qualityName || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(viewData.recordStatus)">{{ getStatusText(viewData.recordStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDate(viewData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="整改时间">{{ formatDate(viewData.rectificationTime) }}</el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 可编辑字段 -->
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="120px"
    >
      <!-- 任务关联字段：仅检查组长/副组长可编辑，其他任务信息只读展示在上方描述区 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="检查组长">
            <el-select v-model="formData.groupLeaderId" placeholder="请选择检查组长" filterable style="width: 100%">
              <el-option v-for="u in userOptions" :key="u.value" :label="u.label" :value="u.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查副组长">
            <el-select v-model="formData.deputyLeaderId" placeholder="请选择检查副组长" filterable style="width: 100%">
              <el-option v-for="u in userOptions" :key="u.value" :label="u.label" :value="u.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 级联选择：专业类别 → 模块 → 责任要素 → 检查内容 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="专业类别" prop="professionalTypeId">
            <el-select v-model="formData.professionalTypeId" placeholder="请选择专业类别" style="width: 100%">
              <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模块" prop="moduleId">
            <el-select v-model="formData.moduleId" placeholder="请选择模块" style="width: 100%" :disabled="!formData.professionalTypeId">
              <el-option v-for="item in moduleOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任要素" prop="responsibilityId">
            <el-select v-model="formData.responsibilityId" placeholder="请选择责任要素" style="width: 100%" :disabled="!formData.moduleId">
              <el-option v-for="item in responsibilityOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查内容" prop="issueId">
            <el-select v-model="formData.issueId" placeholder="请选择检查内容" style="width: 100%" :disabled="!formData.responsibilityId">
              <el-option v-for="item in issueOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="性质界定" prop="qualityId">
        <el-select v-model="formData.qualityId" placeholder="请选择性质界定" style="width: 100%">
          <el-option v-for="item in natureOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item label="问题描述" prop="problemDescription">
        <el-input v-model="formData.problemDescription" type="textarea" :rows="3" placeholder="请输入问题描述" />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="发现位置" prop="location">
            <el-input v-model="formData.location" placeholder="请输入发现位置" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查人" prop="inspector">
            <el-input v-model="formData.inspector" placeholder="请输入检查人" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="安全记分" prop="safetyScore">
            <el-input-number v-model="formData.safetyScore" :min="0" placeholder="请输入安全记分" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改期限" prop="rectificationDeadline">
            <el-date-picker v-model="formData.rectificationDeadline" type="date" placeholder="请选择整改期限" format="YYYY-MM-DD" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="问题图片" prop="photos">
        <DragUpload
          ref="uploadRef"
          v-model="fileList"
          :limit="9"
          list-type="picture-card"
          primary-text="拖拽图片到此处上传"
          secondary-text="或点击选择图片"
          tip-text="支持jpg/png格式，单张图片不超过2MB，最多上传9张，支持拖拽上传"
          @success="handleUploadSuccess"
          @remove="handleRemove"
          @preview="handlePreview"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSubmit">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import DragUpload from '@/components/DragUpload.vue'
import { convertToHttpUrl, parseImageUrls } from '@/config/api'
import { monthlyRecordApi, getTypeOptions, getModuleByProfessionId, getResponsibilityByModuleId, getProblemByResponsibilityId, getNatureDefinitionList, getUserList } from '@/api/monthlyRecord'
import { monthlyTaskApi } from '@/api/monthly'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  record: { type: Object, default: () => ({}) }
})

const emit = defineEmits(['update:modelValue', 'success'])

const dialogVisible = ref(false)
const loading = ref(false)
const formRef = ref()
const fileList = ref([])
const viewData = reactive({})

// 级联选项
const typeOptions = ref([])
const moduleOptions = ref([])
const responsibilityOptions = ref([])
const issueOptions = ref([])
const natureOptions = ref([])
const userOptions = ref([])

// 表单数据
const formData = reactive({
  id: '',
  problemDescription: '',
  location: '',
  inspector: '',
  safetyScore: 0,
  rectificationDeadline: '',
  photos: '',
  taskId: '',
  groupLeaderId: '',
  deputyLeaderId: '',
  professionalTypeId: '',
  moduleId: '',
  responsibilityId: '',
  issueId: '',
  qualityId: ''
})

// 校验规则
const rules = {
  problemDescription: [{ required: true, message: '请输入问题描述', trigger: 'blur' }],
  location: [{ required: true, message: '请输入发现位置', trigger: 'blur' }],
  inspector: [{ required: true, message: '请输入检查人', trigger: 'blur' }],
  safetyScore: [{ required: true, message: '请输入安全记分', trigger: 'blur' }],
  professionalTypeId: [{ required: true, message: '请选择专业类别', trigger: 'change' }],
  moduleId: [{ required: true, message: '请选择模块', trigger: 'change' }],
  responsibilityId: [{ required: true, message: '请选择责任要素', trigger: 'change' }],
  issueId: [{ required: true, message: '请选择检查内容', trigger: 'change' }],
  qualityId: [{ required: true, message: '请选择性质界定', trigger: 'change' }]
}

// 统一提取列表数据，兼容多种响应结构
const extractList = (res) => {
  try {
    if (!res) return []
    if (Array.isArray(res)) return res
    const d = res.data
    if (Array.isArray(d)) return d
    if (Array.isArray(d?.records)) return d.records
    if (Array.isArray(d?.list)) return d.list
    return []
  } catch (_) {
    return []
  }
}

// 打开时初始化
watch(
  () => props.modelValue,
  (val) => {
    dialogVisible.value = val
    if (val) {
      nextTick(() => initFormData())
    }
  },
  { immediate: true }
)

// 同步v-model
watch(dialogVisible, (val) => emit('update:modelValue', val))

// 初始化表单数据
  const initFormData = async () => {
  if (props.record && props.record.id) {
    try {
      // 始终拉取详情，确保任务编号、检查部门、组长/副组长等扩展字段可用
      const res = await monthlyRecordApi.getById(props.record.id)
      const detail = (res && res.code === 200) ? (res.data || props.record) : props.record

      // 只读信息用于显示，与查看弹窗一致
      Object.assign(viewData, detail)

      // 可编辑字段初始化（统一ID类型为字符串，避免Select不匹配）
      // 规范日期格式（仅取到天），统一为 'YYYY-MM-DD'
      const normalizeDate = (val) => {
        if (!val) return ''
        try {
          // Date 实例
          if (val instanceof Date) {
            const y = val.getFullYear()
            const m = String(val.getMonth() + 1).padStart(2, '0')
            const d = String(val.getDate()).padStart(2, '0')
            return `${y}-${m}-${d}`
          }
          // 数字时间戳
          if (typeof val === 'number') {
            const dt = new Date(val)
            if (!isNaN(dt.getTime())) {
              const y = dt.getFullYear()
              const m = String(dt.getMonth() + 1).padStart(2, '0')
              const d = String(dt.getDate()).padStart(2, '0')
              return `${y}-${m}-${d}`
            }
          }
          const s = String(val).trim()
          const head = s.slice(0, 10)
          // 将斜杠替换为短横线，确保符合 el-date-picker 的 value-format="YYYY-MM-DD"
          return head.replaceAll('/', '-')
        } catch (_) { return '' }
      }

      Object.assign(formData, {
        id: detail.id || props.record.id || '',
        problemDescription: detail.problemDescription || props.record.problemDescription || '',
        location: detail.location || props.record.location || '',
        inspector: detail.inspector || props.record.inspector || '',
        safetyScore: detail.safetyScore ?? props.record.safetyScore ?? 0,
        rectificationDeadline: normalizeDate(detail.rectificationDeadline || detail.rectificationDate || props.record.rectificationDeadline || props.record.rectificationDate || ''),
        taskId: (detail.taskId ?? props.record.taskId ?? '')?.toString() || '',
        professionalTypeId: (detail.professionalTypeId ?? props.record.professionalTypeId ?? '')?.toString() || '',
        moduleId: (detail.moduleId ?? props.record.moduleId ?? '')?.toString() || '',
        responsibilityId: (detail.responsibilityId ?? props.record.responsibilityId ?? '')?.toString() || '',
        issueId: (detail.issueId ?? props.record.issueId ?? '')?.toString() || '',
        qualityId: (detail.qualityId ?? props.record.qualityId ?? '')?.toString() || ''
      })

      // 初始化任务详情（用于编辑检查组长与副组长）
      // 兼容可能传入任务编号的情况：优先用ID查询，失败则回退到任务编号查询
      try {
        const taskId = (formData.taskId || '')
        const taskNo = (detail.taskNo || props.record.taskNo || viewData.taskNo || '')

        let task = null

        // 优先使用任务ID（排除明显是编号的字符串，例如以 'YJ' 开头）
        if (taskId && !/^YJ/.test(taskId)) {
          const byIdRes = await monthlyTaskApi.getById(taskId)
          if (byIdRes && byIdRes.code === 200 && byIdRes.data) {
            task = byIdRes.data
          }
        }

        // 若ID查询失败或传入的是任务编号，则尝试使用任务编号查询
        if (!task && taskNo) {
          const byNoRes = await monthlyTaskApi.getByTaskNo(taskNo)
          if (byNoRes && byNoRes.code === 200 && byNoRes.data) {
            task = byNoRes.data
          }
        }

        if (task) {
          // 统一表单内保存真实任务ID
          formData.taskId = (task.id ?? formData.taskId ?? '')?.toString() || ''
          formData.groupLeaderId = (task.groupLeaderId ?? '')?.toString() || ''
          formData.deputyLeaderId = (task.deputyLeaderId ?? '')?.toString() || ''
          // 记录任务对象以供后续级联初始化使用
          // 优先使用任务的专业ID作为级联的初始专业类别
          await initCascadesWithRecord(detail, task)
        } else {
          // 无法获取任务时，回退仅以记录信息初始化级联
          await initCascadesWithRecord(detail, null)
        }
      } catch (e) {
        // 忽略任务详情拉取失败
        console.warn('任务详情加载失败，可能传入了任务编号而非ID：', e)
        // 即便任务详情失败，也以记录信息初始化级联
        await initCascadesWithRecord(detail, null)
      }

      // 图片字段兜底：兼容不同字段名
      const imageUrls = parseImageUrls(
        detail.photos || props.record.photos ||
        detail.problemImages || props.record.problemImages ||
        detail.images || props.record.images ||
        detail.rectificationImages || props.record.rectificationImages ||
        ''
      )
      fileList.value = imageUrls.map((url, index) => ({ uid: Date.now() + index, name: `image-${index}.jpg`, status: 'success', url }))

      // 加载人员选项（与任务级联初始化解耦）
      // 加载人员选项
      await loadUserOptions()
      const normalize = (s) => String(s || '').replace(/\s+/g, '').toLowerCase()
      const findByName = (name) => {
        const n = normalize(name)
        const opts = userOptions.value || []
        const f = opts.find(o => {
          const l = normalize(o.label)
          return l === n || l.includes(n) || n.includes(l)
        })
        return f ? String(f.value) : ''
      }
      if (!formData.groupLeaderId) {
        const id = findByName((viewData.groupLeaderName || detail.groupLeaderName || '').toString())
        if (id) formData.groupLeaderId = id
      }
      if (!formData.deputyLeaderId) {
        const id = findByName((viewData.deputyLeaderName || detail.deputyLeaderName || '').toString())
        if (id) formData.deputyLeaderId = id
      }
    } catch (e) {
      console.error('初始化表单数据失败:', e)
    }
  } else {
    Object.assign(viewData, {})
    Object.assign(formData, { id: '', problemDescription: '', location: '', inspector: '', safetyScore: 0, rectificationDeadline: '', photos: '', taskId: '', groupLeaderId: '', deputyLeaderId: '', professionalTypeId: '', moduleId: '', responsibilityId: '', issueId: '', qualityId: '' })
    fileList.value = []
    // 初始化基础选项
    loadTypeOptions()
    loadNatureOptions()
    loadUserOptions()
  }
}

// 如果传入的行数据缺少ID字段，拉取详情后再回填
const initCascadesWithRecord = async (detailParam, taskParam) => {
  try {
    const detail = detailParam || props.record
    const task = taskParam || null
    // 加载顶层选项
    await loadTypeOptions()
    // 优先使用任务的专业ID，其次使用记录中的专业类别ID
    const preferProfessionId = (task?.professionId ?? detail.professionalTypeId ?? formData.professionalTypeId ?? '')?.toString() || ''
    formData.professionalTypeId = preferProfessionId

    // 级联加载模块
    if (preferProfessionId) {
      await loadModuleOptions(preferProfessionId)
      {
        const current = (detail.moduleId || formData.moduleId || '')?.toString() || ''
        const nameVal = (detail.moduleName || viewData.moduleName || '')?.toString() || ''
        const opts = moduleOptions.value || []
        const matchedById = opts.some(o => String(o.value) === current)
        const normalized = (s) => String(s || '').trim().toLowerCase()
        const matchedByName = opts.find(o => {
          const ov = normalized(o.value)
          const ol = normalized(o.label)
          const nv = normalized(nameVal)
          // 同名或包含关系均视为匹配
          return ov === nv || ol === nv || ol.includes(nv) || nv.includes(ol)
        })
        formData.moduleId = matchedById ? current : (matchedByName ? String(matchedByName.value) : (nameVal || current))
      }
    }

    // 级联加载责任
    if (formData.moduleId) {
      await loadResponsibilityOptions(formData.moduleId)
      {
        const current = (detail.responsibilityId || formData.responsibilityId || '')?.toString() || ''
        const nameVal = (detail.responsibilityName || viewData.responsibilityName || '')?.toString() || ''
        const opts = responsibilityOptions.value || []
        const matchedById = opts.some(o => String(o.value) === current)
        const normalized = (s) => String(s || '').trim().toLowerCase()
        const matchedByName = opts.find(o => {
          const ol = normalized(o.label)
          const nv = normalized(nameVal)
          return ol === nv || ol.includes(nv) || nv.includes(ol)
        })
        formData.responsibilityId = matchedById ? current : (matchedByName ? String(matchedByName.value) : (nameVal || current))
      }
    }

    // 级联加载问题
    if (formData.responsibilityId) {
      await loadIssueOptions(formData.responsibilityId)
      {
        const current = (detail.issueId || formData.issueId || '')?.toString() || ''
        const nameVal = (detail.issueName || viewData.issueName || '')?.toString() || ''
        const opts = issueOptions.value || []
        const matchedById = opts.some(o => String(o.value) === current)
        const normalized = (s) => String(s || '').trim().toLowerCase()
        const matchedByName = opts.find(o => {
          const ol = normalized(o.label)
          const nv = normalized(nameVal)
          return ol === nv || ol.includes(nv) || nv.includes(ol)
        })
        formData.issueId = matchedById ? current : (matchedByName ? String(matchedByName.value) : (nameVal || current))
      }
    }

    // 性质界定选项
    await loadNatureOptions()
    {
      const current = (detail.qualityId || formData.qualityId || '')?.toString() || ''
      const nameVal = (detail.qualityName || detail.qualityDetermination || viewData.qualityName || viewData.qualityDetermination || '')?.toString() || ''
      const opts = natureOptions.value || []
      const matchedById = opts.some(o => String(o.value) === current)
      const normalized = (s) => String(s || '').trim().toLowerCase()
      const matchedByName = opts.find(o => {
        const ol = normalized(o.label)
        const ov = normalized(o.value)
        const nv = normalized(nameVal)
        return ov === nv || ol === nv || ol.includes(nv) || nv.includes(ol)
      })
      formData.qualityId = matchedById ? current : (matchedByName ? String(matchedByName.value) : (nameVal || current))
    }
  } catch (e) {
    // 忽略错误，保证编辑不被阻断
    console.error('初始化级联选项失败:', e)
  }
}

// 图片预览/移除/上传回调由 DragUpload 处理
const handlePreview = () => {}
const handleRemove = () => {}
const handleUploadSuccess = () => {}

// 重置文件列表
const resetFileList = () => { fileList.value = [] }

// 保存提交
const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    loading.value = true

    // —— 提交前兜底：确保选项标签已加载且可解析 ——
    // 若当前选项数组为空或不包含所选值，则主动加载对应选项以获取 label
    const ensureOptionContains = async (optsRef, loaderFn, selectedId) => {
      const sid = (selectedId || '')?.toString()
      if (!sid) return
      const hasValue = (optsRef.value || []).some(o => String(o.value) === sid)
      if (!hasValue && typeof loaderFn === 'function') {
        try { await loaderFn() } catch (_) {}
      }
    }

    // 保证专业/模块/责任/问题/性质的 label 可用
    await ensureOptionContains(typeOptions, async () => { await loadTypeOptions() }, formData.professionalTypeId)
    await ensureOptionContains(moduleOptions, async () => { await loadModuleOptions(formData.professionalTypeId) }, formData.moduleId)
    await ensureOptionContains(responsibilityOptions, async () => { await loadResponsibilityOptions(formData.moduleId) }, formData.responsibilityId)
    await ensureOptionContains(issueOptions, async () => { await loadIssueOptions(formData.responsibilityId) }, formData.issueId)
    await ensureOptionContains(natureOptions, async () => { await loadNatureOptions() }, formData.qualityId)

    // 图片路径处理：保存到数据库的应为相对路径，如 /files/images/20251031/xxx.jpg
    // 1) 新上传图片优先使用响应中的 filePath/url；2) 历史图片从完整URL提取 pathname
    const toRelativePath = (u) => {
      if (!u) return ''
      try {
        const s = typeof u === 'string' ? u : String(u)
        if (s.startsWith('http://') || s.startsWith('https://')) {
          const urlObj = new URL(s)
          return urlObj.pathname || ''
        }
        return s
      } catch (_) {
        return typeof u === 'string' ? u : ''
      }
    }
    // 仅依据 file.url 构造相对路径，避免 response 对象导致 [object Object]
    const imagePaths = fileList.value
      .map(file => toRelativePath(file.url))
      .filter(p => typeof p === 'string' && p)

    // 选项名称提取（用于后端直接回写名称字段）
    const getOptionLabel = (opts, val) => {
      try {
        if (!val) return ''
        const v = String(val)
        const found = (opts || []).find(o => String(o.value) === v)
        return found?.label || ''
      } catch (_) { return '' }
    }

    const submitData = {
      ...formData,
      photos: imagePaths.join(','),
      professionalTypeName: getOptionLabel(typeOptions.value, formData.professionalTypeId) || '',
      moduleName: getOptionLabel(moduleOptions.value, formData.moduleId) || '',
      responsibilityName: getOptionLabel(responsibilityOptions.value, formData.responsibilityId) || '',
      issueName: getOptionLabel(issueOptions.value, formData.issueId) || '',
      qualityName: getOptionLabel(natureOptions.value, formData.qualityId) || '',
      // 同步移动端别名字段，确保后端 mapMobileFieldsToDbFields 能正确映射名称
      professionalCategory: getOptionLabel(typeOptions.value, formData.professionalTypeId) || '',
      module: getOptionLabel(moduleOptions.value, formData.moduleId) || '',
      responsibility: getOptionLabel(responsibilityOptions.value, formData.responsibilityId) || '',
      issue: getOptionLabel(issueOptions.value, formData.issueId) || '',
      qualityDetermination: getOptionLabel(natureOptions.value, formData.qualityId) || ''
    }

    // 将整改期限映射为后端字段 rectificationDate（view中为 rectificationDeadline）
    if (submitData.rectificationDeadline) {
      submitData.rectificationDate = submitData.rectificationDeadline
      delete submitData.rectificationDeadline
    }

    const response = await monthlyRecordApi.update(submitData)
    if (response.code === 200) {
      // 仅允许修改任务的检查组长/副组长
      if (formData.taskId && (formData.groupLeaderId || formData.deputyLeaderId)) {
        try {
          await monthlyTaskApi.update({
            id: formData.taskId,
            groupLeaderId: formData.groupLeaderId || undefined,
            deputyLeaderId: formData.deputyLeaderId || undefined
          })
        } catch (e) {
          // 任务更新失败不阻断记录更新成功
        }
      }
      ElMessage.success('更新成功')
      emit('success')
    } else {
      ElMessage.error(response.message || '更新失败')
    }
  } catch (error) {
    console.error('表单校验失败或提交异常:', error)
  } finally {
    loading.value = false
  }
}

// 与查看弹窗保持一致的辅助函数
const getStatusType = (status) => {
  const statusMap = { 0: 'warning', 1: 'success', 2: 'info' }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = { 0: '待整改', 1: '已整改', 2: '已复查' }
  return statusMap[status] || '未知'
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

// ---- 级联加载逻辑 ----
const loadTypeOptions = async () => {
  try {
    const res = await getTypeOptions()
    const list = extractList(res)
    typeOptions.value = list.map(item => {
      if (typeof item === 'string' || typeof item === 'number') {
        return { label: String(item), value: String(item) }
      }
      return {
        label: item.dictLabel || item.label || item.name || String(item),
        value: (item.dictValue ?? item.value ?? item.id ?? String(item))?.toString()
      }
    })
  } catch (e) {
    typeOptions.value = []
  }
}

const loadModuleOptions = async (professionId) => {
  try {
    if (!professionId) { moduleOptions.value = []; return }
    const res = await getModuleByProfessionId(professionId)
    const list = extractList(res)
    moduleOptions.value = list.map(item => {
      if (typeof item === 'string' || typeof item === 'number') {
        return { label: String(item), value: String(item) }
      }
      return {
        label: item.dictLabel || item.label || item.name || String(item),
        value: (item.dictValue ?? item.value ?? item.id ?? String(item))?.toString()
      }
    })
  } catch (e) {
    moduleOptions.value = []
  }
}

const loadResponsibilityOptions = async (moduleId) => {
  try {
    if (!moduleId) { responsibilityOptions.value = []; return }
    const res = await getResponsibilityByModuleId(moduleId)
    const list = extractList(res)
    responsibilityOptions.value = list.map(item => {
      if (typeof item === 'string' || typeof item === 'number') {
        return { label: String(item), value: String(item) }
      }
      return {
        label: item.dictLabel || item.label || item.name || String(item),
        value: (item.dictValue ?? item.value ?? item.id ?? String(item))?.toString()
      }
    })
  } catch (e) {
    responsibilityOptions.value = []
  }
}

const loadIssueOptions = async (responsibilityId) => {
  try {
    if (!responsibilityId) { issueOptions.value = []; return }
    const res = await getProblemByResponsibilityId(responsibilityId)
    const list = extractList(res)
    issueOptions.value = list.map(item => {
      if (typeof item === 'string' || typeof item === 'number') {
        return { label: String(item), value: String(item) }
      }
      return {
        label: item.dictLabel || item.label || item.name || String(item),
        value: (item.dictValue ?? item.value ?? item.id ?? String(item))?.toString()
      }
    })
  } catch (e) {
    issueOptions.value = []
  }
}

const loadNatureOptions = async () => {
  try {
    const res = await getNatureDefinitionList()
    const list = extractList(res)
    natureOptions.value = list.map(item => {
      if (typeof item === 'string' || typeof item === 'number') {
        return { label: String(item), value: String(item) }
      }
      // 字典类型优先用 dictLabel 作为显示，dictValue 作为值
      return {
        label: item.dictLabel || item.label || item.name || String(item),
        value: (item.dictValue ?? item.value ?? item.id ?? String(item))?.toString()
      }
    })
  } catch (e) {
    natureOptions.value = []
  }
}

const loadUserOptions = async () => {
  try {
    const res = await getUserList({ current: 1, size: 1000 })
    const list = extractList(res)
    userOptions.value = list.map(item => ({ label: item.realName || item.username || item.name, value: (item.id ?? '')?.toString() }))
  } catch (e) {
    userOptions.value = []
  }
}

// 监听用户修改，触发级联联动
watch(() => formData.professionalTypeId, async (newVal, oldVal) => {
  if (newVal === oldVal) return
  formData.moduleId = ''
  formData.responsibilityId = ''
  formData.issueId = ''
  await loadModuleOptions(newVal)
})

watch(() => formData.moduleId, async (newVal, oldVal) => {
  if (newVal === oldVal) return
  formData.responsibilityId = ''
  formData.issueId = ''
  await loadResponsibilityOptions(newVal)
})

watch(() => formData.responsibilityId, async (newVal, oldVal) => {
  if (newVal === oldVal) return
  formData.issueId = ''
  await loadIssueOptions(newVal)
})

// 关闭
const handleClose = () => {
  formRef.value?.resetFields()
  resetFileList()
  dialogVisible.value = false
}
</script>

<style scoped>
.info-section { margin-bottom: 24px; }
.info-section h4 { margin-top: 0; margin-bottom: 16px; color: #303133; border-bottom: 2px solid #e4e7ed; padding-bottom: 8px; }
.dialog-footer { text-align: right; }
:deep(.el-upload--picture-card) { width: 100px; height: 100px; }
:deep(.el-upload-list--picture-card .el-upload-list__item) { width: 100px; height: 100px; }
</style>
