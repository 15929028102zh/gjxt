<template>
  <el-dialog
    v-model="dialogVisible"
    title="确认整改"
    width="80%"
    :before-close="handleClose"
    destroy-on-close
  >
    <div v-loading="loading">
      <!-- 基本信息 -->
      <div class="info-section">
        <h4>基本信息</h4>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="任务编号">
            {{ formData.taskNo || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="月检时间">
            {{ formatDate(formData.inspectionTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="检查部门">
            {{ formData.departmentName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查专业">
            {{ formData.professionalTypeName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查组长">
            {{ formData.groupLeaderName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查副组长">
            {{ formData.deputyLeaderName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="专业类别">
            {{ formData.professionalTypeName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="模块">
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
          <el-descriptions-item label="安全记分">
            {{ formData.safetyScore || 0 }}
          </el-descriptions-item>
          <el-descriptions-item label="问题描述">
            {{ formData.problemDescription || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="发现位置">
            {{ formData.location || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查人">
            {{ formData.inspector || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="整改期限">
            {{ formatDate(formData.rectificationDeadline) }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(formData.recordStatus)">
              {{ getStatusText(formData.recordStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDate(formData.createTime) }}
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
          <el-form-item label="整改效果" prop="rectificationSuggestion">
            <el-input
              v-model="rectificationForm.rectificationSuggestion"
              type="textarea"
              :rows="4"
              placeholder="请输入整改效果"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="整改图片">
            <DragUpload
              v-model="imageFileList"
              :limit="3"
              :max-size="2"
              accept="image/jpeg,image/png"
              list-type="picture-card"
              primary-text="将图片拖到此处，或点击上传"
              secondary-text="支持拖拽上传"
              tip-text="只能上传jpg/png文件，且不超过2MB，最多3张"
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
import { Plus, UploadFilled } from '@element-plus/icons-vue'
import DragUpload from '@/components/DragUpload.vue'
import { parseImageUrls } from '@/config/api'
import { monthlyRecordApi } from '@/api/monthlyRecord'

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

// 响应式数据
const formRef = ref()
const uploadRef = ref()
const loading = ref(false)
const submitLoading = ref(false)
const problemImages = ref([])
const imageFileList = ref([])

// 整改表单数据
const rectificationForm = ref({
  id: '',
  rectificationSuggestion: '',
  rectificationImages: []
})

// 表单验证规则
const rules = {
  rectificationSuggestion: [
    { required: true, message: '请输入整改建议', trigger: 'blur' },
    { min: 10, message: '整改建议至少需要10个字符', trigger: 'blur' }
  ]
}

// 弹窗显示状态
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

// 监听弹窗显示状态
watch(dialogVisible, (newVal) => {
  if (newVal && props.formData?.id) {
    initFormData()
  } else if (!newVal) {
    resetForm()
  }
})

// 初始化表单数据
const initFormData = () => {
  rectificationForm.value.id = props.formData.id
  
  // 处理问题图片
  problemImages.value = parseImageUrls(props.formData.photos)
}

// 重置表单
const resetForm = () => {
  rectificationForm.value = {
    id: '',
    rectificationSuggestion: '',
    rectificationImages: []
  }
  imageFileList.value = []
  problemImages.value = []
  
  nextTick(() => {
    formRef.value?.clearValidate()
  })
}

// 获取状态类型
const getStatusType = (status) => {
  const statusMap = {
    0: 'warning',
    1: 'success',
    2: 'info'
  }
  return statusMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待整改',
    1: '已整改',
    2: '已复查'
  }
  return statusMap[status] || '未知'
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}



// 确认整改
const handleConfirm = async () => {
  try {
    // 表单验证（失败时仅提示并停止，不弹通用失败）
    const valid = await formRef.value
      .validate()
      .then(() => true)
      .catch(() => false)
    if (!valid) {
      ElMessage.warning('请完善必填项后再提交')
      return
    }

    // 二次确认
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
      status: 1,
      rectificationImages: (rectificationForm.value.rectificationImages || []).join(',')
    }

    // 调用接口（JSON提交）
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

const handleImageSuccess = (response, file) => {
  try {
    const url = response?.url || response?.data?.url || file?.url
    if (url) rectificationForm.value.rectificationImages.push(url)
  } catch (_) {}
}

const handleImageError = (error) => {
  console.error('图片上传失败:', error)
  ElMessage.error('图片上传失败')
}

const handleImageRemove = (file) => {
  const idx = rectificationForm.value.rectificationImages.findIndex(u => u === file?.url)
  if (idx > -1) rectificationForm.value.rectificationImages.splice(idx, 1)
}

// 关闭弹窗
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

.upload-tip {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
}

.upload-demo {
  width: 100%;
}

:deep(.el-upload-dragger) {
  width: 100%;
  height: 180px;
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
}

:deep(.el-upload-dragger:hover) {
  border-color: #409eff;
}

:deep(.el-upload-dragger .el-icon--upload) {
  font-size: 67px;
  color: #c0c4cc;
  margin: 40px 0 16px;
  line-height: 50px;
}

:deep(.el-upload__text) {
  color: #606266;
  font-size: 14px;
  text-align: center;
}

:deep(.el-upload__text em) {
  color: #409eff;
  font-style: normal;
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