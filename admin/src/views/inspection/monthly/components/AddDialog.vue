<template>
  <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-alert
      v-if="auditMode"
      type="warning"
      :closable="false"
      style="margin-bottom: 16px"
      title="本次修改需提交审核后生效"
    />
    <el-alert
      v-else-if="!isInInspectionPeriod && isEdit"
      type="warning"
      :closable="false"
      style="margin-bottom: 16px"
      title="当前不在月检录入期内，编辑可能受限"
    />

    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="120px"
      class="add-form"
    >
      <el-row :gutter="20">
        <el-col :span="12" v-if="formData.taskNo">
          <el-form-item label="任务编号">
            <el-input v-model="formData.taskNo" placeholder="任务编号" readonly />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.taskName">
          <el-form-item label="任务名称">
            <el-input v-model="formData.taskName" placeholder="任务名称" readonly />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="问题照片" prop="problemImageUrls">
            <DragUpload
              v-model="imageFileList"
              :limit="3"
              accept="image/*"
              :multiple="true"
              list-type="picture-card"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 受检单位 / 发现位置：移至录入图片下方；受检单位变更会按标准组重载岗检标准级联 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="受检单位" prop="departmentId">
            <SearchableSelect
              v-model="formData.departmentId"
              :options="runningDeptList"
              label-key="deptName"
              value-key="id"
              placeholder="请选择受检单位(运行部门)"
              :disabled="deptSelectDisabled"
              style="width: 100%"
              @change="handleDepartmentChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发现位置" prop="location">
            <el-input
              v-model="formData.location"
              placeholder="请输入发现位置"
              maxlength="100"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="问题描述" prop="problemDescription">
            <el-input
              v-model="formData.problemDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入问题描述"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="专业类别" prop="professionalTypeId">
            <SearchableSelect
              v-model="formData.professionalTypeId"
              :options="professionOptions"
              label-key="name"
              value-key="id"
              placeholder="请选择专业类别"
              style="width: 100%"
              @change="handleProfessionChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="基本模块" prop="moduleId">
            <SearchableSelect
              v-model="formData.moduleId"
              :options="moduleOptions"
              label-key="name"
              value-key="id"
              placeholder="请选择基本模块"
              style="width: 100%"
              @change="handleModuleChange"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任要素" prop="responsibilityId">
            <SearchableSelect
              v-model="formData.responsibilityId"
              :options="responsibilityOptions"
              label-key="name"
              value-key="id"
              placeholder="请选择责任要素"
              style="width: 100%"
              @change="handleResponsibilityChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查内容" prop="issueId">
            <SearchableSelect
              v-model="formData.issueId"
              :options="problemOptions"
              label-key="name"
              value-key="id"
              placeholder="请选择检查内容"
              long-text
              style="width: 100%"
              @change="handleIssueChange"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性质界定" prop="qualityId">
            <SearchableSelect
              v-model="formData.qualityId"
              :options="natureOptions"
              label-key="name"
              value-key="id"
              placeholder="请选择性质界定"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否人工修改">
            <el-tag :type="natureModified === 1 ? 'warning' : 'info'" size="default">
              {{ natureModified === 1 ? '是(已修改)' : '否(未修改)' }}
            </el-tag>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 扣分值 / 检查单位：发现位置上移后，检查单位与扣分值同行补齐布局 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="扣分值">
            <el-input
              :model-value="deductionScoreDisplay"
              readonly
              placeholder="选择性质界定后自动显示"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查单位" prop="checkUnitId">
            <SearchableSelect
              v-model="formData.checkUnitId"
              :options="managementDeptList"
              label-key="deptName"
              value-key="id"
              placeholder="请选择检查单位(管理部门)"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="检查人" prop="inspector">
            <SearchableSelect
              v-model="formData.inspector"
              :options="userOptions"
              label-key="displayName"
              value-key="displayName"
              placeholder="请选择检查人（可多选）"
              multiple
              collapse-tags
              collapse-tags-tooltip
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任人" prop="responsiblePerson">
            <SearchableSelect
              v-model="formData.responsiblePerson"
              :options="userOptions"
              label-key="displayName"
              value-key="displayName"
              placeholder="请选择责任人（可多选）"
              multiple
              collapse-tags
              collapse-tags-tooltip
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任单位" prop="responsibleUnit">
            <SearchableSelect
              v-model="formData.responsibleUnit"
              :options="departmentList"
              label-key="deptName"
              value-key="deptName"
              placeholder="请选择责任单位（可多选）"
              multiple
              collapse-tags
              collapse-tags-tooltip
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改期限" prop="rectificationDeadline">
            <el-date-picker
              v-model="formData.rectificationDeadline"
              type="date"
              placeholder="请选择整改期限"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- V3:auditMode 下显示修改说明(待审核流程必填) -->
      <el-row v-if="auditMode" :gutter="20">
        <el-col :span="24">
          <el-form-item label="修改说明" prop="modifyDescription">
            <el-input
              v-model="formData.modifyDescription"
              type="textarea"
              :rows="3"
              placeholder="请说明本次修改内容(至少 5 个字)"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          {{ submitButtonText }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import DragUpload from '@/components/DragUpload.vue'
import SearchableSelect from '@/components/SearchableSelect.vue'
import { monthlyRecordApi, getNatureDefinitionList, getDeptList, getUserOptions } from '@/api/monthlyRecord'
import { gjbzDataApi } from '@/api/gjbz'
import { parseImageUrls } from '@/config/api'

const props = defineProps({
  visible: { type: Boolean, default: false },
  formData: { type: Object, default: () => ({}) },
  isInInspectionPeriod: { type: Boolean, default: true },
  // V3:auditMode=true 时,提交走 submitEditAudit(待审核通道),否则走 update
  auditMode: { type: Boolean, default: false }
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const submitLoading = ref(false)
const imageFileList = ref([])
const professionOptions = ref([])
const moduleOptions = ref([])
const responsibilityOptions = ref([])
const problemOptions = ref([])
const natureOptions = ref([])
const runningDeptList = ref([])
const managementDeptList = ref([])
const taskOptions = ref([])
const userOptions = ref([])
const issueQualityMap = ref({}) // 检查内容(issue) -> 性质界定(quality)
const autoFilledQualityId = ref('') // 性质界定 自动带出值;用户改动后置空,代表解锁
const natureModified = ref(0) // 0=未改动 1=已改动

const formData = reactive({
  id: '',
  taskId: '',
  taskName: '',
  taskNo: '',
  problemImageUrls: [],
  problemDescription: '',
  professionalTypeId: '',
  professionalTypeName: '',
  moduleId: '',
  moduleName: '',
  responsibilityId: '',
  responsibilityName: '',
  issueId: '',
  issueName: '',
  qualityId: '',
  qualityName: '',
  location: '',
  inspector: [],
  responsibleUnit: [],
  responsiblePerson: [],
  rectificationDeadline: '',
  departmentId: '',
  checkUnitId: '',
  checkUnitName: '',
  // V3:auditMode 下必填的修改说明
  modifyDescription: ''
})

const rules = computed(() => {
  const base = {
    problemDescription: [{ required: true, message: '请输入问题描述', trigger: 'blur' }],
    professionalTypeId: [{ required: true, message: '请选择专业类别', trigger: 'change' }],
    moduleId: [{ required: true, message: '请选择模块', trigger: 'change' }],
    responsibilityId: [{ required: true, message: '请选择责任', trigger: 'change' }],
    issueId: [{ required: true, message: '请选择问题', trigger: 'change' }],
    qualityId: [{ required: true, message: '请选择性质界定', trigger: 'change' }],
    location: [{ required: true, message: '请输入发现位置', trigger: 'blur' }],
    inspector: [{ required: true, type: 'array', min: 1, message: '请选择检查人', trigger: 'change' }],
    departmentId: [{ required: true, message: '请选择受检单位', trigger: 'change' }],
    checkUnitId: [{ required: true, message: '请选择检查单位', trigger: 'change' }],
    rectificationDeadline: [{ required: true, message: '请选择整改期限', trigger: 'change' }]
  }
  if (props.auditMode) {
    base.modifyDescription = [
      { required: true, message: '请填写修改说明', trigger: 'blur' },
      { min: 5, message: '修改说明至少 5 个字', trigger: 'blur' }
    ]
  }
  return base
})

const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

const isEdit = computed(() => !!(props.formData && props.formData.id))

// V3:auditMode 下弹窗标题/按钮文案差异化
const dialogTitle = computed(() => {
  if (props.auditMode) return '编辑月检记录'
  return isEdit.value ? '编辑月检记录' : '新增月检记录'
})

const submitButtonText = computed(() => {
  if (props.auditMode) return '保存'
  return isEdit.value ? '更新' : '保存'
})

const isAdminUser = computed(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  // 月检管理员才能用(兼容性保留老 admin 与 username=='admin' 兜底)
  return userInfo.isMonthlyAdmin === 1 || userInfo.isAdmin === 1 || userInfo.username === 'admin'
})

// 受检单位可选性按部门角色判定(对齐移动端 task-record.uvue 的 deptType 规则)
// 运行部门(dept_type=0)用户:受检单位固定为本部门,不可改
// 管理部门(dept_type=1)用户:可正常选择受检单位
const deptSelectDisabled = computed(() => {
  if (isAdminUser.value) return false
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  return runningDeptList.value.some(d => d.id === userInfo.deptId)
})

// 扣分值：根据性质界定自动显示（不入库）
// 观察 → 1  一般 → 3  重要 → 5
const deductionScore = computed(() => {
  const name = formData.qualityName
  if (name === '观察') return 1
  if (name === '一般') return 3
  if (name === '重要') return 5
  return null
})

const deductionScoreDisplay = computed(() => {
  const score = deductionScore.value
  return score === null ? '' : `${score}`
})

let isInitializing = false
watch(() => props.visible, (newVal) => {
  if (newVal) {
    isInitializing = true
    nextTick(async () => {
      resetForm()
      await loadOptions()
      if (props.formData) {
        const isEditMode = !!props.formData.id
        if (isEditMode) {
          // 多值字段（inspector / responsiblePerson / responsibleUnit）从「、」分隔字符串还原为数组
          formData.inspector = splitMulti(props.formData.inspector)
          formData.responsiblePerson = splitMulti(props.formData.responsiblePerson)
          formData.responsibleUnit = splitMulti(props.formData.responsibleUnit)
          Object.assign(formData, props.formData)
          // Object.assign 会覆盖上面 split 出的数组，需要重新 split（针对 props.formData 已是数组的情况）
          formData.inspector = splitMulti(formData.inspector)
          formData.responsiblePerson = splitMulti(formData.responsiblePerson)
          formData.responsibleUnit = splitMulti(formData.responsibleUnit)
          // 整改期限：详情接口以 rectificationDeadline 别名返回，分页列表只有 rectificationDate
          formData.rectificationDeadline = props.formData.rectificationDeadline || props.formData.rectificationDate || ''
          // 受检单位已回填，按对应标准组加载专业类别与 issue->quality 映射（须在级联回灌与 id 修复之前）
          await loadGroupOptions()
          // 编辑时按已有级联值回灌下级选项
          if (formData.professionalTypeName) {
            await loadModuleOptions(formData.professionalTypeName)
            if (formData.moduleName) {
              await loadResponsibilityOptions(formData.moduleName, formData.professionalTypeName)
            }
            if (formData.responsibilityName) {
              await loadIssueOptions(formData.responsibilityName, formData.professionalTypeName, formData.moduleName)
            }
          }
          // 级联 id 修复：移动端可能提交数字序号 id（历史数据），或与 web 端规则不一致的 id，
          // 只要当前 id 在选项中匹配不到，就按名称重新匹配，保证 el-select 能选中
          const repairCascadeId = (idKey, nameKey, optionsRef) => {
            const name = formData[nameKey]
            if (!name) return
            const matched = optionsRef.value.find(o => o.id === formData[idKey])
            if (!matched) {
              const byName = optionsRef.value.find(o => o.name === name)
              if (byName) formData[idKey] = byName.id
            }
          }
          repairCascadeId('professionalTypeId', 'professionalTypeName', professionOptions)
          repairCascadeId('moduleId', 'moduleName', moduleOptions)
          repairCascadeId('responsibilityId', 'responsibilityName', responsibilityOptions)
          repairCascadeId('issueId', 'issueName', problemOptions)
          repairCascadeId('qualityId', 'qualityName', natureOptions)
          // 编辑模式:回填 natureModified 标志和 autoFilledQualityId
          if (formData.issueName) {
            const expectedQualityName = issueQualityMap.value[formData.issueName]
            if (expectedQualityName) {
              const expectedQ = natureOptions.value.find(n => n.name === expectedQualityName)
              if (expectedQ) {
                autoFilledQualityId.value = expectedQ.id
                // 若当前 qualityId 与自动带出值不一致,标记为已改动
                if (formData.qualityId && formData.qualityId !== expectedQ.id) {
                  natureModified.value = 1
                }
              }
            }
          }
          // 数据库回填的 natureModified 优先级最高(以防 issueQualityMap 配置变化)
          if (props.formData.natureModified === 1) {
            natureModified.value = 1
          }
        } else {
          // 新增模式：仅继承 taskId/taskNo/taskName
          formData.taskId = props.formData.taskId || ''
          formData.taskNo = props.formData.taskNo || ''
          formData.taskName = props.formData.taskName || ''
          // 自动带入当前用户姓名作为检查人
          try {
            const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
            if (userInfo.realName) {
              const matchedUser = userOptions.value.find(u => u.realName === userInfo.realName)
              formData.inspector = [matchedUser ? (matchedUser.displayName || matchedUser.realName) : userInfo.realName]
            }
            // V3:受检单位默认 = 当前用户部门(若该部门是运行部门,否则置空)
            if (userInfo.deptId) {
              const matchedRunning = runningDeptList.value.find(d => d.id === userInfo.deptId)
              if (matchedRunning) formData.departmentId = userInfo.deptId
            }
            // V3:检查单位默认 = 当前用户部门(若该部门是管理部门)
            if (userInfo.deptId) {
              const matchedMgmt = managementDeptList.value.find(d => d.id === userInfo.deptId)
              if (matchedMgmt) {
                formData.checkUnitId = userInfo.deptId
                formData.checkUnitName = matchedMgmt.deptName
              }
            }
          } catch (e) {
            /* ignore */
          }
          // 新增模式：受检单位默认值确定后，按对应标准组加载专业类别等选项
          await loadGroupOptions()
        }
        // 图片回显
        // 注意：
        // 1) 不能用 formData.problemImageUrls 判空——resetForm 已把它置为 []，空数组恒为真，
        //    历史写法因此永远进不到 photos 分支，导致编辑时照片全丢。
        // 2) parseImageUrls() 把库里的相对路径 /files/xxx 转成绝对 URL，否则 picture-card 会按当前页面 host 解析导致 404。
        // 3) 必须带 status: 'success'，否则 el-upload 会以"上传中"占位符展示缩略图。
        const displayUrls = parseImageUrls(props.formData.photos || props.formData.problemImageUrls)
        formData.problemImageUrls = displayUrls.map(toRelativePath)
        imageFileList.value = displayUrls.map((url, i) => ({
          url, name: `image-${i}`, uid: i, status: 'success'
        }))
      }
      isInitializing = false
    })
  }
})

// 加载与受检单位无关的静态选项：性质界定字典、运行/管理部门、用户列表
async function loadOptions() {
  try {
    const [natureResponse, runningDeptRes, mgmtDeptRes, userResponse] = await Promise.all([
      getNatureDefinitionList(),
      getDeptList({ deptType: 0 }),
      getDeptList({ deptType: 1 }),
      getUserOptions()
    ])
    if (natureResponse.code === 200) {
      // 字典接口返回 {id, dictValue, dictLabel}，统一映射为 {id, name}
      const raw = natureResponse.data
      natureOptions.value = Array.isArray(raw)
        ? raw.map(item => ({
            id: item.id,
            name: item.dictValue || item.dictLabel || item.name || ''
          }))
        : []
    }
    if (runningDeptRes.code === 200) {
      runningDeptList.value = runningDeptRes.data || []
    }
    if (mgmtDeptRes.code === 200) {
      managementDeptList.value = mgmtDeptRes.data || []
    }
    if (userResponse && userResponse.code === 200) {
      const records = Array.isArray(userResponse.data) ? userResponse.data : []
      userOptions.value = records
        .map(u => {
          const realName = u.realName || u.username || ''
          const username = u.username || ''
          // 下拉显示格式：昵称(用户名)；无用户名或与昵称相同时仅显示昵称
          const displayName = (username && username !== realName) ? `${realName}(${username})` : realName
          return { id: u.id, realName, username, displayName }
        })
        .filter(u => u.realName)
    }
  } catch (error) {
    console.error('加载选项数据失败', error)
  }
}

// 加载与受检单位(标准组)相关的选项：专业类别、检查内容->性质界定映射
// 后端根据 departmentId 自动判定标准组(第一组生产/第二组辅助/全部)
async function loadGroupOptions() {
  const departmentId = formData.departmentId || undefined
  try {
    const [professionResponse, issueWithQualityResponse] = await Promise.all([
      gjbzDataApi.getTypes(departmentId),
      gjbzDataApi.getIssueOptionsWithQuality(departmentId)
    ])
    if (professionResponse.code === 200) {
      const data = professionResponse.data
      professionOptions.value = Array.isArray(data)
        // id 与 name 都用专业名称，下游级联接口按 name 取数据
        ? data.map(item => ({ id: item, name: item }))
        : []
    }
    // 构建 检查内容(issue) -> 性质界定(quality) 映射，供选择检查内容后自动带出
    if (issueWithQualityResponse && issueWithQualityResponse.code === 200) {
      const list = Array.isArray(issueWithQualityResponse.data) ? issueWithQualityResponse.data : []
      issueQualityMap.value = {}
      for (const item of list) {
        const issueName = item?.issue
        const qualityName = item?.quality
        if (issueName && qualityName && !issueQualityMap.value[issueName]) {
          issueQualityMap.value[issueName] = qualityName
        }
      }
    }
  } catch (error) {
    console.error('加载岗检标准选项失败', error)
  }
}

function handleProfessionChange(val) {
  const matched = professionOptions.value.find(p => p.id === val)
  const typeName = matched?.name || val || ''
  formData.professionalTypeName = typeName
  formData.professionalTypeId = val
  resetCascade(['module', 'responsibility', 'issue'])
  loadModuleOptions(typeName)
}

function handleModuleChange(val) {
  const matched = moduleOptions.value.find(p => p.id === val)
  formData.moduleId = val
  formData.moduleName = matched?.name || ''
  resetCascade(['responsibility', 'issue'])
  if (formData.moduleName) loadResponsibilityOptions(formData.moduleName, formData.professionalTypeName)
}

function handleResponsibilityChange(val) {
  const matched = responsibilityOptions.value.find(p => p.id === val)
  formData.responsibilityId = val
  formData.responsibilityName = matched?.name || ''
  resetCascade(['issue'])
  if (formData.responsibilityName) {
    loadIssueOptions(formData.responsibilityName, formData.professionalTypeName, formData.moduleName)
  }
}

function handleIssueChange(val) {
  const matched = problemOptions.value.find(p => p.id === val)
  const issueName = matched?.name || ''
  formData.issueId = val
  formData.issueName = issueName
  // 按 检查内容 -> 性质界定 映射自动带出
  const qualityName = issueQualityMap.value[issueName]
  if (qualityName && natureOptions.value.length > 0) {
    const q = natureOptions.value.find(n => n.name === qualityName)
    if (q) {
      formData.qualityId = q.id
      formData.qualityName = q.name
      autoFilledQualityId.value = q.id // 记录自动带出值,用于锁逻辑
      natureModified.value = 0 // 自动带出视为未改动
    }
  }
}

// 监听 qualityId 变化,用户手动改则标记为已改动(解锁)
watch(() => formData.qualityId, (newVal, oldVal) => {
  if (isInitializing) return
  if (oldVal && newVal !== oldVal && newVal !== autoFilledQualityId.value) {
    natureModified.value = 1
  }
})

function resetCascade(levels) {
  if (levels.includes('module')) {
    formData.moduleId = ''
    formData.moduleName = ''
    moduleOptions.value = []
  }
  if (levels.includes('responsibility')) {
    formData.responsibilityId = ''
    formData.responsibilityName = ''
    responsibilityOptions.value = []
  }
  if (levels.includes('issue')) {
    formData.issueId = ''
    formData.issueName = ''
    problemOptions.value = []
  }
}

async function loadModuleOptions(typeName) {
  if (!typeName) {
    moduleOptions.value = []
    return
  }
  try {
    const response = await gjbzDataApi.getModulesByType(typeName, formData.departmentId || undefined)
    if (response.code === 200) {
      const items = Array.isArray(response.data) ? response.data : []
      moduleOptions.value = items.map((m, i) => ({ id: `${typeName}-${i}`, name: m }))
    }
  } catch (e) {
    console.error('加载模块失败', e)
  }
}

async function loadResponsibilityOptions(moduleName, typeName) {
  if (!moduleName) {
    responsibilityOptions.value = []
    return
  }
  try {
    // 带专业类别: 同名模块在不同专业类别下有不同责任要素, 不加此参数会跨类别串味
    const response = await gjbzDataApi.getResponsibilitiesByModule(
      moduleName,
      formData.departmentId || undefined,
      typeName || undefined
    )
    if (response.code === 200) {
      const items = Array.isArray(response.data) ? response.data : []
      responsibilityOptions.value = items.map((m, i) => ({ id: `${moduleName}-${i}`, name: m }))
    }
  } catch (e) {
    console.error('加载责任失败', e)
  }
}

async function loadIssueOptions(responsibilityName, typeName, moduleName) {
  if (!responsibilityName) {
    problemOptions.value = []
    return
  }
  try {
    // 带专业类别+模块: 同名责任要素在不同模块下有不同检查内容, 不加此参数会跨模块串味
    const response = await gjbzDataApi.getIssuesByResponsibility(
      responsibilityName,
      formData.departmentId || undefined,
      typeName || undefined,
      moduleName || undefined
    )
    if (response.code === 200) {
      const items = Array.isArray(response.data) ? response.data : []
      problemOptions.value = items.map((m, i) => ({ id: `${responsibilityName}-${i}`, name: m }))
    }
  } catch (e) {
    console.error('加载问题失败', e)
  }
}

// 受检单位变更：清空已选岗检标准级联(专业类别/基本模块/责任要素/检查内容)及性质界定，
// 再按新受检单位对应的标准组重新加载选项。初始化回填阶段不触发。
function handleDepartmentChange(val) {
  formData.departmentId = val || ''
  if (isInitializing) return
  // 清空四级级联选择与选项
  formData.professionalTypeId = ''
  formData.professionalTypeName = ''
  professionOptions.value = []
  resetCascade(['module', 'responsibility', 'issue'])
  // 性质界定由检查内容自动带出，一并清空并重置人工修改标志
  formData.qualityId = ''
  formData.qualityName = ''
  autoFilledQualityId.value = ''
  natureModified.value = 0
  // 按新受检单位重新加载专业类别与 issue->quality 映射
  loadGroupOptions()
}

// 库里统一存 /files/... 相对路径，回显时被 parseImageUrls 拼成了绝对地址，提交前需还原
function toRelativePath(url) {
  const value = String(url || '')
  const index = value.indexOf('/files/')
  return index >= 0 ? value.slice(index) : value
}

// 多值字段辅助：把字符串/数组统一处理为数组；空值返回 []
function splitMulti(value) {
  if (Array.isArray(value)) {
    return value.filter(v => v !== null && v !== undefined && String(v).trim() !== '')
  }
  if (value === null || value === undefined || value === '') return []
  return String(value).split(/[、,，]/).map(s => s.trim()).filter(Boolean)
}

// 多值字段辅助：把数组拼成「、」分隔字符串；空数组返回 ''
function joinMulti(value) {
  if (!Array.isArray(value)) return value || ''
  return value.filter(v => v !== null && v !== undefined && String(v).trim() !== '').join('、')
}

async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    submitLoading.value = true

    // 图片以 imageFileList 为准，否则用户删掉的回显图片提交后又会复活
    const images = imageFileList.value
      .map(file => toRelativePath(file.response?.data?.filePath || file.response?.data?.url || file.url))
      .filter(Boolean)
    const photos = images.join(',')

    const payload = {
      ...formData,
      photos,
      problemImageUrls: images,
      inspector: joinMulti(formData.inspector),
      responsiblePerson: joinMulti(formData.responsiblePerson),
      responsibleUnit: joinMulti(formData.responsibleUnit),
      natureModified: natureModified.value
    }
    // checkUnitName 从选中的管理列表中查
    if (formData.checkUnitId) {
      const found = managementDeptList.value.find(d => d.id === formData.checkUnitId)
      if (found) payload.checkUnitName = found.deptName
    }
    // 编辑时不应改 taskId；不回写服务器返回的部门/任务等冗余名称字段
    if (isEdit.value) {
      delete payload.problemImageUrls
      delete payload.departmentName
      delete payload.taskName
      delete payload.taskNo
    }

    // V3:auditMode 走 submitEditAudit(待审核通道),不直接更新记录字段
    if (props.auditMode) {
      if (!formData.id) {
        ElMessage.error('记录ID不能为空,无法提交审核')
        submitLoading.value = false
        return
      }
      const auditPayload = {
        modifyDescription: formData.modifyDescription,
        auditType: 1,
        // V3:把表单中的新字段值传给后端,后端构造 modification_snapshot
        responsiblePerson: payload.responsiblePerson,
        responsibleUnit: payload.responsibleUnit,
        location: payload.location,
        qualityId: payload.qualityId,
        qualityName: payload.qualityName,
        departmentId: payload.departmentId,
        checkUnitId: payload.checkUnitId,
        checkUnitName: payload.checkUnitName,
        rectificationDate: payload.rectificationDeadline,
        inspector: payload.inspector,
        problemDescription: payload.problemDescription,
        natureModified: natureModified.value
      }
      const response = await monthlyRecordApi.submitEditAudit(formData.id, auditPayload)
      if (response.code === 200) {
        ElMessage.success('已提交编辑审核,审核通过后修改生效')
        emit('success')
      } else {
        ElMessage.error(response.message || '提交审核失败')
      }
      return
    }

    const response = isEdit.value
      ? await monthlyRecordApi.update(payload)
      : await monthlyRecordApi.create(payload)

    if (response.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '已保存到提交列表，请到月检提交页面提交')
      emit('success')
    } else {
      ElMessage.error(response.message || (isEdit.value ? '更新失败' : '保存失败'))
    }
  } catch (error) {
    console.error('提交失败', error)
    if (error?.message) ElMessage.error(error.message)
    else ElMessage.error('提交失败')
  } finally {
    submitLoading.value = false
  }
}

function resetForm() {
  Object.assign(formData, {
    id: '',
    taskId: '',
    taskName: '',
    taskNo: '',
    problemImageUrls: [],
    problemDescription: '',
    professionalTypeId: '',
    professionalTypeName: '',
    moduleId: '',
    moduleName: '',
    responsibilityId: '',
    responsibilityName: '',
    issueId: '',
    issueName: '',
    qualityId: '',
    qualityName: '',
    location: '',
    inspector: [],
    responsibleUnit: [],
    responsiblePerson: [],
    rectificationDeadline: '',
    departmentId: '',
    checkUnitId: '',
    checkUnitName: '',
    modifyDescription: ''
  })
  imageFileList.value = []
  moduleOptions.value = []
  responsibilityOptions.value = []
  problemOptions.value = []
  issueQualityMap.value = {}
  autoFilledQualityId.value = ''
  natureModified.value = 0
  if (formRef.value) formRef.value.clearValidate()
}

function handleClose() {
  dialogVisible.value = false
}
</script>

<style scoped>
.add-form { padding: 0 8px; }
.dialog-footer { text-align: right; }
</style>
