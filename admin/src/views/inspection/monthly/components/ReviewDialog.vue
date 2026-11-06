<template>
  <el-dialog
    v-model="dialogVisible"
    title="确认复查"
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
            {{ detailData.problemDescription || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="专业类别">
            {{ detailData.professionalTypeName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="基本模块">
            {{ detailData.moduleName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="责任要素">
            {{ detailData.responsibilityName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查内容">
            {{ detailData.issueName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="性质界定">
            {{ detailData.qualityName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="性质界定是否人工修改">
            <el-tag :type="detailData.natureModified === 1 ? 'warning' : 'info'" size="small">
              {{ detailData.natureModified === 1 ? '是(已修改)' : '否(未修改)' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="扣分值">
            {{ reviewDeductionScoreDisplay || '选择性质界定后自动显示' }}
          </el-descriptions-item>
          <el-descriptions-item label="发现位置">
            {{ detailData.location || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查单位">
            {{ detailData.checkUnitName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="受检单位">
            {{ detailData.departmentName || detailData.departmentId || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查人">
            {{ detailData.inspector || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="责任人">
            {{ detailData.responsiblePerson || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="责任单位">
            {{ detailData.responsibleUnit || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="整改期限">
            {{ formatDate(detailData.rectificationDeadline || detailData.rectificationDate) }}
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

      <!-- 整改信息（只读） -->
      <div class="info-section">
        <h4>整改信息</h4>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="整改建议">
            {{ detailData.rectificationSuggestion || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="整改效果">
            {{ detailData.rectificationEffect || '暂无' }}
          </el-descriptions-item>
        </el-descriptions>
        <div v-if="rectificationImages.length > 0" style="margin-top: 12px;">
          <h5>整改图片：</h5>
          <div class="image-preview">
            <el-image
              v-for="(image, index) in rectificationImages"
              :key="index"
              :src="image"
              :preview-src-list="rectificationImages"
              style="width: 120px; height: 120px; margin-right: 8px; border-radius: 4px;"
              fit="cover"
            />
          </div>
        </div>
      </div>

      <!-- 复查信息表单 -->
      <div class="info-section">
        <h4>复查信息</h4>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="120px"
        >
          <el-form-item label="复查人">
            <el-input v-model="currentUserName" readonly placeholder="当前登录用户" style="width: 240px;" />
          </el-form-item>
          <el-form-item label="复查效果" prop="reviewEffect">
            <el-input
              v-model="form.reviewEffect"
              type="textarea"
              :rows="3"
              placeholder="请描述复查实际效果"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="复查图片">
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
        <el-button type="info" plain :loading="saveLoading" @click="handleSave">保存</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确认复查</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import DragUpload from '@/components/DragUpload.vue'
import { monthlyRecordApi } from '@/api/monthlyRecord'
import { parseImageUrls } from '@/config/api'

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
const imageFileList = ref([])
const detailData = reactive({})
const rectificationImages = ref([])
// 复查人：当前登录用户姓名（只读，确认复查时写库）
const currentUserName = ref('')
try {
  const ui = JSON.parse(localStorage.getItem('userInfo') || '{}')
  currentUserName.value = ui.realName || ui.username || ''
} catch (e) {
  currentUserName.value = ''
}

const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

const form = reactive({
  id: '',
  reviewEffect: '',
  reviewImages: []
})

const rules = {
  reviewEffect: [
    { required: true, message: '请输入复查效果', trigger: 'blur' }
  ]
}

const problemImages = computed(() => {
  return parseImageUrls(detailData.photos)
})

const reviewDeductionScore = computed(() => {
  const name = detailData.qualityName
  if (name === '观察') return 1
  if (name === '一般') return 3
  if (name === '重要') return 5
  return null
})

const reviewDeductionScoreDisplay = computed(() => {
  const score = reviewDeductionScore.value
  return score === null ? '' : `${score}`
})

watch(() => props.visible, async (newVal) => {
  if (newVal) {
    resetForm()
    if (props.formData && props.formData.id) {
      await loadRecordDetail(props.formData.id)
    }
  }
})

const loadRecordDetail = async (id) => {
  try {
    const response = await monthlyRecordApi.getById(id)
    if (response.code === 200 && response.data) {
      const data = response.data
      Object.assign(detailData, data)
      form.id = data.id

      const rectImgs = data.rectificationImages || data.rectifyImages || data.rectificationImageUrls || ''
      rectificationImages.value = parseImageUrls(rectImgs)
    }
  } catch (error) {
    console.error('获取记录详情失败:', error)
    ElMessage.error('获取记录详情失败')
  }
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    id: '',
    reviewEffect: '',
    reviewImages: []
  })
  Object.keys(detailData).forEach(k => delete detailData[k])
  rectificationImages.value = []
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

const handleClose = () => {
  dialogVisible.value = false
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitLoading.value = true

    const submitData = {
      reviewEffect: form.reviewEffect,
      reviewImages: (form.reviewImages || []).join(','),
      reviewerName: currentUserName.value
    }

    const response = await monthlyRecordApi.confirmReview(form.id, submitData)

    if (response.code === 200) {
      ElMessage.success('复查确认成功')
      emit('success')
    } else {
      ElMessage.error(response.message || '复查确认失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitLoading.value = false
  }
}

// 保存：仅保存页面上现有数据，不变更记录状态（走通用更新接口，不触发状态机）
const handleSave = async () => {
  try {
    saveLoading.value = true
    const payload = {
      id: form.id,
      reviewEffect: form.reviewEffect,
      reviewImages: (form.reviewImages || []).join(',')
    }
    const response = await monthlyRecordApi.update(payload)
    if (response.code === 200) {
      ElMessage.success('保存成功，记录状态未变更')
      emit('success')
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    console.error('保存复查信息失败:', error)
    ElMessage.error(error?.message || '保存失败')
  } finally {
    saveLoading.value = false
  }
}

const handleImageSuccess = (response, file) => {
  const url = response?.data?.filePath || response?.data?.url || file?.url
  if (url) {
    form.reviewImages.push(url)
  }
}

const handleImageError = (error) => {
  console.error('图片上传失败:', error)
  ElMessage.error('图片上传失败')
}

const handleImageRemove = (file) => {
  const idx = form.reviewImages.findIndex(u => u === file?.url)
  if (idx > -1) form.reviewImages.splice(idx, 1)
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

.info-section h5 {
  margin: 8px 0;
  color: #606266;
  font-size: 14px;
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

:deep(.el-descriptions__label) {
  font-weight: 500;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-textarea__inner) {
  resize: vertical;
}
</style>