<template>
  <el-dialog
    v-model="dialogVisible"
    title="确认复查"
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
        <el-descriptions-item label="发生部位">
          {{ formData.location }}
        </el-descriptions-item>
        <el-descriptions-item label="检查人">
          {{ formData.inspector }}
        </el-descriptions-item>
        <el-descriptions-item label="整改时间">
          {{ formatDate(formData.rectificationTime) }}
        </el-descriptions-item>
      </el-descriptions>
      
      <!-- 问题图片展示 -->
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

    <!-- 整改效果展示 -->
    <div class="rectification-info">
      <h4>整改情况</h4>
      <el-form
        :model="form"
        label-width="120px"
        disabled
      >
        <el-row :gutter="20">
          <!-- 性质界定 -->
          <el-col :span="12">
            <el-form-item label="性质界定">
              <el-input v-model="form.natureDefinition" readonly />
            </el-form-item>
          </el-col>

          <!-- 专业 -->
          <el-col :span="12">
            <el-form-item label="专业">
              <el-input v-model="form.professionName" readonly />
            </el-form-item>
          </el-col>

          <!-- 所属部门 -->
          <el-col :span="12">
            <el-form-item label="所属部门">
              <el-input v-model="form.departmentName" readonly />
            </el-form-item>
          </el-col>

          <!-- 整改建议 -->
          <el-col :span="24">
            <el-form-item label="整改建议">
              <el-input
                v-model="form.rectificationSuggestion"
                type="textarea"
                :rows="3"
                readonly
              />
            </el-form-item>
          </el-col>

          <!-- 整改效果描述 -->
          <el-col :span="24">
            <el-form-item label="整改效果描述">
              <el-input
                v-model="form.rectificationEffect"
                type="textarea"
                :rows="3"
                readonly
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 整改图片展示 -->
      <div v-if="form.rectificationImages && form.rectificationImages.length > 0" style="margin-top: 16px;">
        <h5>整改图片：</h5>
        <div class="image-preview">
          <el-image
            v-for="(image, index) in form.rectificationImages"
            :key="index"
            :src="image"
            :preview-src-list="form.rectificationImages"
            style="width: 100px; height: 100px; margin-right: 8px; border-radius: 4px;"
            fit="cover"
          />
        </div>
      </div>
    </div>

    <!-- 复查信息表单 -->
    <div class="review-form">
      <h4>复查信息</h4>
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        v-loading="loading"
      >
        <el-row :gutter="20">
          <!-- 复查建议 -->
          <el-col :span="24">
            <el-form-item label="复查建议" prop="reviewSuggestion">
              <el-input
                v-model="form.reviewSuggestion"
                type="textarea"
                :rows="3"
                placeholder="请输入复查建议..."
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
          </el-col>

          <!-- 复查效果描述 -->
          <el-col :span="24">
            <el-form-item label="复查效果描述" prop="reviewEffect">
              <el-input
                v-model="form.reviewEffect"
                type="textarea"
                :rows="4"
                placeholder="请描述复查效果..."
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
          </el-col>

          <!-- 复查图片 -->
          <el-col :span="24">
            <el-form-item label="复查图片">
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
          </el-col>
        </el-row>
      </el-form>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确认复查</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import DragUpload from '@/components/DragUpload.vue'
import * as monthlyRecordApi from '@/api/monthlyRecord'

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

// 弹窗显示状态
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

// 表单数据
const form = reactive({
  id: '',
  natureDefinition: '',
  professionId: null,
  professionName: '',
  departmentId: null,
  departmentName: '',
  rectificationSuggestion: '',
  rectificationEffect: '',
  reviewSuggestion: '',
  reviewEffect: '',
  rectificationImages: [],
  reviewImages: []
})

// 表单验证规则
const rules = {
  reviewSuggestion: [
    { required: true, message: '请输入复查建议', trigger: 'blur' }
  ],
  reviewEffect: [
    { required: true, message: '请填写复查效果描述', trigger: 'blur' }
  ]
}

// 计算属性
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

// 加载记录详情
const loadRecordDetail = async (id) => {
  try {
    const response = await monthlyRecordApi.getById(id)
    if (response.code === 200 && response.data) {
      const data = response.data
      // 回写数据到表单
      form.id = data.id
      form.natureDefinition = data.natureDefinition || ''
      form.professionId = data.professionId
      form.departmentId = data.departmentId
      form.rectificationSuggestion = data.rectificationSuggestion || ''
      form.rectificationEffect = data.rectificationEffect || ''
      form.reviewSuggestion = data.reviewSuggestion || ''
      form.reviewEffect = data.reviewEffect || ''
      
      // 设置专业和部门的显示名称
      form.professionName = data.professionName || ''
      form.departmentName = data.departmentName || ''
      
      // 处理整改图片
      if (data.rectificationImages) {
        if (typeof data.rectificationImages === 'string') {
          form.rectificationImages = data.rectificationImages.split(',').filter(url => url.trim())
        } else if (Array.isArray(data.rectificationImages)) {
          form.rectificationImages = [...data.rectificationImages]
        }
      }
      
      // 处理复查图片
      if (data.reviewImages) {
        if (typeof data.reviewImages === 'string') {
          form.reviewImages = data.reviewImages.split(',').filter(url => url.trim())
        } else if (Array.isArray(data.reviewImages)) {
          form.reviewImages = [...data.reviewImages]
        }
      }
    }
  } catch (error) {
    console.error('获取记录详情失败:', error)
    ElMessage.error('获取记录详情失败')
  }
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    id: '',
    natureDefinition: '',
    professionId: null,
    professionName: '',
    departmentId: null,
    departmentName: '',
    rectificationSuggestion: '',
    rectificationEffect: '',
    reviewSuggestion: '',
    reviewEffect: '',
    rectificationImages: [],
    reviewImages: []
  })
  imageFileList.value = []
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

// 图片上传成功
const handleImageSuccess = (response, file) => {
  if (response && response.url) {
    form.reviewImages.push(response.url)
  }
}

// 图片上传失败
const handleImageError = (error) => {
  console.error('图片上传失败:', error)
  ElMessage.error('图片上传失败')
}

// 移除图片
const handleImageRemove = (file) => {
  const index = form.reviewImages.findIndex(url => url === file.url)
  if (index > -1) {
    form.reviewImages.splice(index, 1)
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
      reviewImages: form.reviewImages.join(','),
      status: 2 // 复查后状态为已复查
    }

    const response = await monthlyRecordApi.confirmReview(submitData.id, submitData)
    
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

.rectification-info {
  margin-bottom: 24px;
  padding: 16px;
  background-color: #f0f9ff;
  border-radius: 8px;
}

.rectification-info h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.rectification-info h5 {
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

.review-form {
  margin-bottom: 24px;
}

.review-form h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
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