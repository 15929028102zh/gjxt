<template>
  <el-dialog
    v-model="dialogVisible"
    title="确认整改"
    width="80%"
    :before-close="handleClose"
    destroy-on-close
  >
    <div v-loading="loading">
      <!-- 基本信息（与月检 AddDialog 字段统一） -->
      <div class="info-section">
        <h4>问题信息</h4>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="问题描述">
            {{ formData.problemDescription || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="专业类别">
            {{ formData.professionalTypeName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="基本模块">
            {{ formData.moduleName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="责任要素">
            {{ formData.responsibilityName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查内容">
            {{ formData.issueName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="性质界定">
            {{ formData.qualityName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="性质界定是否人工修改">
            <el-tag :type="formData.natureModified === 1 ? 'warning' : 'info'" size="small">
              {{ formData.natureModified === 1 ? '是(已修改)' : '否(未修改)' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="扣分值">
            {{ deductionScoreDisplay || '选择性质界定后自动显示' }}
          </el-descriptions-item>
          <el-descriptions-item label="发现位置">
            {{ formData.location || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查单位">
            {{ formData.checkUnitName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="受检单位">
            {{ formData.departmentName || formData.departmentId || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查人">
            {{ formData.inspector || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="整改期限">
            {{ formatDate(formData.rectificationDeadline || formData.rectificationDate) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 问题图片 -->
      <div class="info-section">
        <h4>问题图片</h4>
        <div v-if="problemImages.length > 0" class="image-preview">
          <el-image
            v-for="(image, index) in problemImages"
            :key="index"
            :src="image"
            :preview-src-list="problemImages"
            style="width: 120px; height: 120px; margin-right: 8px; border-radius: 4px;"
            fit="cover"
          />
        </div>
        <div v-else class="no-image">
          <el-empty description="暂无问题图片" :image-size="80" />
        </div>
      </div>

      <!-- 整改信息表单 -->
      <div class="info-section">
        <h4>整改信息</h4>
        <el-form ref="formRef" :model="rectificationForm" :rules="rules" label-width="120px">
          <el-form-item label="责任人" prop="responsiblePerson">
            <SearchableSelect
              v-model="rectificationForm.responsiblePerson"
              :options="userOptions"
              label-key="realName"
              value-key="realName"
              placeholder="请选择责任人（可多选）"
              multiple
              collapse-tags
              collapse-tags-tooltip
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="责任单位" prop="responsibleUnit">
            <SearchableSelect
              v-model="rectificationForm.responsibleUnit"
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
          <el-form-item label="整改人">
            <el-input v-model="currentUserName" readonly placeholder="当前登录用户" style="width: 240px;" />
          </el-form-item>
          <el-form-item label="原因分析" prop="rectificationSuggestion">
            <el-input
              v-model="rectificationForm.rectificationSuggestion"
              type="textarea"
              :rows="3"
              placeholder="请输入原因分析"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="整改效果" prop="rectificationEffect">
            <el-input
              v-model="rectificationForm.rectificationEffect"
              type="textarea"
              :rows="3"
              placeholder="请描述整改完成后的实际效果"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="整改图片">
            <DragUpload
              v-model="imageFileList"
              :limit="3"
              :max-size="5"
              accept="image/jpeg,image/png"
              list-type="picture-card"
              primary-text="将图片拖到此处，或点击上传"
              secondary-text="支持拖拽上传"
              tip-text="只能上传jpg/png文件，且不超过5MB，最多3张"
              @success="handleImageSuccess"
              @error="handleImageError"
              @remove="handleImageRemove"
            />
          </el-form-item>
        </el-form>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="info" plain :loading="saveLoading" @click="handleSave">
          保存
        </el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleConfirm">
          确认整改
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import DragUpload from '@/components/DragUpload.vue'
import SearchableSelect from '@/components/SearchableSelect.vue'
import { parseImageUrls } from '@/config/api'
import { monthlyRecordApi, getDeptList, getUserOptions } from '@/api/monthlyRecord'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  formData: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const loading = ref(false)
const submitLoading = ref(false)
const saveLoading = ref(false)
const problemImages = ref([])
const imageFileList = ref([])
// 整改人：当前登录用户姓名（只读，确认整改时写库）
const currentUserName = ref('')
try {
  const ui = JSON.parse(localStorage.getItem('userInfo') || '{}')
  currentUserName.value = ui.realName || ui.username || ''
} catch (e) {
  currentUserName.value = ''
}
// 责任人/责任单位下拉选项（与 AddDialog 保持一致的数据源）
const userOptions = ref([])
const departmentList = ref([])
let optionsLoaded = false

const rectificationForm = ref({
  id: '',
  responsiblePerson: [],
  responsibleUnit: [],
  rectificationSuggestion: '',
  rectificationEffect: '',
  rectificationImages: []
})

const rules = {
  rectificationSuggestion: [
    { required: true, message: '请输入原因分析', trigger: 'blur' }
  ],
  rectificationEffect: [
    { required: true, message: '请输入整改效果', trigger: 'blur' }
  ]
}

const deductionScore = computed(() => {
  const name = props.formData?.qualityName
  if (name === '观察') return 1
  if (name === '一般') return 3
  if (name === '重要') return 5
  return null
})

const deductionScoreDisplay = computed(() => {
  const score = deductionScore.value
  return score === null ? '' : `${score}`
})

const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

watch(dialogVisible, (newVal) => {
  if (newVal && props.formData?.id) {
    initFormData()
  } else if (!newVal) {
    resetForm()
  }
})

// 多值字段辅助：把字符串/数组统一处理为数组；空值返回 []（与 AddDialog 一致）
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

// 加载责任人（用户列表）与责任单位（部门列表）下拉选项，仅首次打开时加载
const loadOptions = async () => {
  if (optionsLoaded) return
  try {
    const [deptRes, userRes] = await Promise.all([
      getDeptList(),
      getUserOptions()
    ])
    if (deptRes.code === 200) {
      departmentList.value = deptRes.data || []
    }
    if (userRes && userRes.code === 200) {
      const records = Array.isArray(userRes.data) ? userRes.data : []
      userOptions.value = records
        .map(u => ({ id: u.id, realName: u.realName || u.username }))
        .filter(u => u.realName)
    }
    optionsLoaded = true
  } catch (error) {
    console.error('加载责任人/责任单位选项失败:', error)
  }
}

const initFormData = () => {
  rectificationForm.value.id = props.formData.id
  // 责任人/责任单位回显（「、」分隔字符串 -> 数组）
  rectificationForm.value.responsiblePerson = splitMulti(props.formData.responsiblePerson)
  rectificationForm.value.responsibleUnit = splitMulti(props.formData.responsibleUnit)
  // 已保存过的整改信息回显（保存按钮存过草稿的场景）
  rectificationForm.value.rectificationSuggestion = props.formData.rectificationSuggestion || ''
  rectificationForm.value.rectificationEffect = props.formData.rectificationEffect || ''
  const savedImages = parseImageUrls(props.formData.rectificationImages)
  rectificationForm.value.rectificationImages = [...savedImages]
  imageFileList.value = savedImages.map(url => ({ name: url.split('/').pop(), url }))
  problemImages.value = parseImageUrls(props.formData.photos)
  loadOptions()
}

const resetForm = () => {
  rectificationForm.value = {
    id: '',
    responsiblePerson: [],
    responsibleUnit: [],
    rectificationSuggestion: '',
    rectificationEffect: '',
    rectificationImages: []
  }
  imageFileList.value = []
  problemImages.value = []

  nextTick(() => {
    formRef.value?.clearValidate()
  })
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

const handleConfirm = async () => {
  try {
    const valid = await formRef.value
      .validate()
      .then(() => true)
      .catch(() => false)
    if (!valid) {
      ElMessage.warning('请完善必填项后再提交')
      return
    }

    await ElMessageBox.confirm(
      '确认提交整改信息吗？提交后将更新记录状态为"已整改"。',
      '确认整改',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    submitLoading.value = true

    const payload = {
      rectificationSuggestion: rectificationForm.value.rectificationSuggestion,
      rectificationEffect: rectificationForm.value.rectificationEffect,
      rectificationImages: (rectificationForm.value.rectificationImages || []).join(','),
      responsiblePerson: joinMulti(rectificationForm.value.responsiblePerson),
      responsibleUnit: joinMulti(rectificationForm.value.responsibleUnit),
      rectifierName: currentUserName.value
    }

    await monthlyRecordApi.confirmRectification(rectificationForm.value.id, payload)

    ElMessage.success('整改确认成功')
    emit('success')
    handleClose()
  } catch (error) {
    console.error('整改确认失败:', error)
    if (error !== 'cancel') {
      ElMessage.error(error?.message || '整改确认失败')
    }
  } finally {
    submitLoading.value = false
  }
}

// 保存：仅保存页面上现有数据，不变更记录状态（走通用更新接口，不触发状态机）
const handleSave = async () => {
  try {
    saveLoading.value = true
    const payload = {
      id: rectificationForm.value.id,
      responsiblePerson: joinMulti(rectificationForm.value.responsiblePerson),
      responsibleUnit: joinMulti(rectificationForm.value.responsibleUnit),
      rectificationSuggestion: rectificationForm.value.rectificationSuggestion,
      rectificationEffect: rectificationForm.value.rectificationEffect,
      rectificationImages: (rectificationForm.value.rectificationImages || []).join(','),
      // 回传原值，避免后端把 nature_modified 兜底重置为 0
      natureModified: props.formData?.natureModified
    }
    const response = await monthlyRecordApi.update(payload)
    if (response.code === 200) {
      ElMessage.success('保存成功，记录状态未变更')
      emit('success')
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    console.error('保存整改信息失败:', error)
    ElMessage.error(error?.message || '保存失败')
  } finally {
    saveLoading.value = false
  }
}

const handleImageSuccess = (response, file) => {
  const url = response?.data?.filePath || response?.data?.url || file?.url
  if (url) rectificationForm.value.rectificationImages.push(url)
}

const handleImageError = (error) => {
  console.error('图片上传失败:', error)
  ElMessage.error('图片上传失败')
}

const handleImageRemove = (file) => {
  const idx = rectificationForm.value.rectificationImages.findIndex(u => u === file?.url)
  if (idx > -1) rectificationForm.value.rectificationImages.splice(idx, 1)
}

const handleClose = () => {
  emit('update:visible', false)
}
</script>

<style scoped>
.info-section {
  margin-bottom: 24px;
}

.info-section h4 {
  margin-top: 0;
  margin-bottom: 16px;
  color: #303133;
  border-bottom: 2px solid #e4e7ed;
  padding-bottom: 8px;
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.no-image {
  padding: 20px;
  text-align: center;
  background: #f5f7fa;
  border-radius: 8px;
  border: 1px dashed #e4e7ed;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-upload--picture-card) {
  width: 120px;
  height: 120px;
}

:deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 120px;
  height: 120px;
}
</style>