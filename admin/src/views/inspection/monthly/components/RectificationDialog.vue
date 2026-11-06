<template>
  <el-dialog
    v-model="dialogVisible"
    title="确认整改"
    width="70%"
    :before-close="handleClose"
    destroy-on-close
  >
    <!-- 问题信息展示 -->
    <div class="problem-info">
      <h4>问题信息</h4>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="问题描述">
          {{ formData.problemDescription }}
        </el-descriptions-item>
        <el-descriptions-item label="责任人">
          {{ formData.responsiblePerson }}
        </el-descriptions-item>
        <el-descriptions-item label="责任单位">
          {{ formData.responsibleUnit }}
        </el-descriptions-item>
        <el-descriptions-item label="发现位置">
          {{ formData.location }}
        </el-descriptions-item>
        <el-descriptions-item label="检查人">
          {{ formData.inspector }}
        </el-descriptions-item>
        <el-descriptions-item label="性质界定">
          {{ formData.qualityName || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="性质界定是否人工修改">
          <el-tag :type="formData.natureModified === 1 ? 'warning' : 'info'" size="small">
            {{ formData.natureModified === 1 ? '是(已修改)' : '否(未修改)' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="专业">
          {{ formData.professionalTypeName || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="所属部门">
          {{ formData.departmentName || formData.responsibilityName || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDate(formData.createTime) }}
        </el-descriptions-item>
      </el-descriptions>
      
      <!-- 问题照片展示 -->
      <div v-if="problemImages.length > 0" style="margin-top: 16px;">
        <h5>问题照片：</h5>
        <div class="image-preview">
          <el-image
            v-for="(image, index) in problemImages"
            :key="index"
            :src="image"
            :preview-src-list="problemImages"
            style="width: 100px; height: 100px; margin-right: 8px; border-radius: 4px;"
            fit="cover"
          />
        </div>
      </div>
    </div>

    <!-- 整改信息表单 -->
    <div class="rectification-form">
      <h4>整改信息</h4>
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        v-loading="loading"
      >
        <el-row :gutter="20">
          <!-- 整改状态 -->
          <el-col :span="12">
            <el-form-item label="整改状态" prop="status">
              <el-radio-group v-model="form.status" disabled>
                <el-radio :label="0">未整改</el-radio>
                <el-radio :label="1">已整改</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <!-- 整改建议 -->
          <el-col :span="24">
            <el-form-item label="整改建议" prop="rectificationSuggestion">
              <el-input
                v-model="form.rectificationSuggestion"
                type="textarea"
                :rows="3"
                placeholder="请输入整改建议..."
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
          </el-col>

          <!-- 整改效果描述 -->
          <el-col :span="24">
            <el-form-item label="整改效果描述" prop="rectificationEffect">
              <el-input
                v-model="form.rectificationEffect"
                type="textarea"
                :rows="4"
                placeholder="请描述整改效果..."
                maxlength="500"
                show-word-limit
                readonly
              />
            </el-form-item>
          </el-col>

          <!-- 整改图片 -->
          <el-col :span="24">
            <el-form-item label="整改图片">
              <div class="image-upload-container">
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
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确认整改</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import DragUpload from '@/components/DragUpload.vue'
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
const loading = ref(false)
const submitLoading = ref(false)
const imageFileList = ref([])

// 表单数据
const form = reactive({
  id: '',
  status: 1,
  rectificationSuggestion: '',
  rectificationEffect: '',
  rectificationImages: []
})

// 表单验证规则
const rules = {
  status: [
    { required: true, message: '请选择整改状态', trigger: 'change' }
  ],
  rectificationSuggestion: [
    { required: true, message: '请输入整改建议', trigger: 'blur' }
  ],
  rectificationEffect: [
    { required: true, message: '请输入整改效果描述', trigger: 'blur' }
  ]
}

// 计算属性
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

const problemImages = computed(() => {
  if (props.formData && props.formData.problemImageUrls) {
    if (typeof props.formData.problemImageUrls === 'string') {
      return props.formData.problemImageUrls.split(',').filter(url => url.trim())
    }
    return props.formData.problemImageUrls
  }
  return []
})

// 监听弹窗显示状态
watch(() => props.visible, async (newVal) => {
  if (newVal) {
    resetForm()
    // 调用接口获取记录详情
    if (props.formData && props.formData.id) {
      await loadRecordDetail(props.formData.id)
    }
  }
})

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    id: '',
    status: 1,
    rectificationSuggestion: '',
    rectificationEffect: '',
    rectificationImages: []
  })
  imageFileList.value = []
}

// 加载记录详情
const loadRecordDetail = async (id) => {
  try {
    loading.value = true
    const response = await monthlyRecordApi.getById(id)
    
    if (response.code === 200 && response.data) {
      const record = response.data
      // 回写表单数据
      form.id = record.id
      form.rectificationSuggestion = record.rectificationSuggestion || ''
      form.rectificationEffect = record.rectificationEffect || ''

      // 处理整改图片
      if (record.rectificationImages) {
        if (typeof record.rectificationImages === 'string') {
          form.rectificationImages = record.rectificationImages.split(',').filter(url => url.trim())
        } else if (Array.isArray(record.rectificationImages)) {
          form.rectificationImages = [...record.rectificationImages]
        }
      }
    }
  } catch (error) {
    console.error('加载记录详情失败:', error)
    ElMessage.error('加载记录详情失败')
  } finally {
    loading.value = false
  }
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

// 图片上传成功
const handleImageSuccess = (response) => {
  const filePath = response?.data?.filePath || response?.data?.url
  if (filePath) {
    form.rectificationImages.push(filePath)
  }
}

// 图片上传失败
const handleImageError = (error) => {
  console.error('图片上传失败:', error)
  ElMessage.error('图片上传失败')
}

// 移除图片
const handleImageRemove = (file) => {
  const index = form.rectificationImages.findIndex(url => url === file.url)
  if (index > -1) {
    form.rectificationImages.splice(index, 1)
  }
}

// 关闭弹窗
const handleClose = () => {
  dialogVisible.value = false
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitLoading.value = true

    const submitData = {
      ...form,
      rectificationImages: form.rectificationImages.join(',')
    }

    const response = await monthlyRecordApi.confirmRectification(submitData.id, submitData)
    
    if (response.code === 200) {
      ElMessage.success('整改确认成功')
      emit('success')
    } else {
      ElMessage.error(response.message || '整改确认失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitLoading.value = false
  }
}
</script>

<style scoped>
.problem-info {
  margin-bottom: 24px;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.problem-info h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.problem-info h5 {
  margin: 16px 0 8px 0;
  color: #606266;
  font-size: 14px;
  font-weight: 500;
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.rectification-form {
  margin-bottom: 24px;
}

.rectification-form h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.image-upload-container {
  width: 100%;
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