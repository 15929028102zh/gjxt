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
    <div class="rectification-info" v-if="formData.rectificationEffect">
      <h4>整改情况</h4>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="整改效果描述">
          {{ formData.rectificationEffect }}
        </el-descriptions-item>
      </el-descriptions>
      
      <!-- 整改图片展示 -->
      <div v-if="rectificationImages.length > 0" style="margin-top: 16px;">
        <h5>整改图片：</h5>
        <div class="image-preview">
          <el-image
            v-for="(image, index) in rectificationImages"
            :key="index"
            :src="image"
            :preview-src-list="rectificationImages"
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
          <!-- 复查人（当前登录用户，只读） -->
          <el-col :span="24">
            <el-form-item label="复查人">
              <el-input v-model="currentUserName" readonly placeholder="当前登录用户" style="width: 240px;" />
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
                tip="只能上传jpg/png文件，且不超过5MB，最多3张"
                @success="handleImageSuccess"
                @error="handleImageError"
                @remove="handleImageRemove"
                @exceed="handleImageExceed"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
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
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { inspectionApi, getUserList } from '@/api/inspection'
import { getToken } from '@/utils/token'
import { convertToHttpUrl } from '@/config/api'
import DragUpload from '@/components/DragUpload.vue'

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
const saveLoading = ref(false)
const imageFileList = ref([])
const rectificationImages = ref([])
const problemImages = ref([])
// 复查人：当前登录用户姓名（只读，确认复查时写库）
const currentUserName = ref('')
try {
  const ui = JSON.parse(localStorage.getItem('userInfo') || '{}')
  currentUserName.value = ui.realName || ui.username || ''
} catch (e) {
  currentUserName.value = ''
}

// 弹窗显示状态
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

// 表单数据
const form = reactive({
  id: '',
  reviewEffect: '',
  reviewTime: '',
  reviewImageUrls: '',
  status: 2 // 复查后状态为已复查
})

// 表单验证规则
const rules = {
  reviewEffect: [
    { required: true, message: '请填写复查效果描述', trigger: 'blur' }
  ]
}

// 选项数据
// 与移动端保持一致，只需要复查效果描述和图片

import { getFullApiUrl, getImageUploadUrl } from '@/config/api'

// 图片上传相关
const uploadAction = computed(() => {
  return getImageUploadUrl()
})

const uploadHeaders = computed(() => {
  return {
    'Authorization': `Bearer ${getToken()}`
  }
})

import { parseImageUrls } from '@/config/api'

// 监听表单数据变化
watch(() => props.formData, (newData) => {
  if (newData && Object.keys(newData).length > 0) {
    // 设置记录ID
    form.id = newData.id
    
    // 处理问题图片展示
    problemImages.value = parseImageUrls(newData.problemImageUrls)
    
    // 处理整改图片展示
    rectificationImages.value = parseImageUrls(newData.rectificationImageUrls)
    
    // 处理复查图片数据
    if (newData.reviewImageUrls) {
      const urls = parseImageUrls(newData.reviewImageUrls)
      imageFileList.value = urls.map((url, index) => ({
        name: `review_image_${index}`,
        url: url
      }))
    } else {
      imageFileList.value = []
    }
    
    // 与移动端保持一致，不需要设置复查人
  }
}, { immediate: true, deep: true })

// 与移动端保持一致，不需要加载用户数据

// 图片上传前验证
const beforeImageUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isJPG) {
    ElMessage.error('只能上传 JPG/PNG 格式的图片!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!')
    return false
  }
  return true
}

// 图片上传成功
const handleImageSuccess = (response, file) => {
  if (response.code === 200) {
    ElMessage.success('图片上传成功')
    updateImageUrls()
  } else {
    ElMessage.error(response.message || '图片上传失败')
  }
}

// 图片上传失败
const handleImageError = (error) => {
  console.error('图片上传失败:', error)
  ElMessage.error('图片上传失败')
}

// 移除图片
const handleImageRemove = () => {
  updateImageUrls()
}

// 图片数量超限
const handleImageExceed = () => {
  ElMessage.warning('最多只能上传3张图片')
}

// 更新图片URLs
const updateImageUrls = () => {
  const urls = imageFileList.value
    .filter(file => file.response?.data?.filePath || file.url)
    .map(file => {
      const filePath = file.response?.data?.filePath || file.url
      // 使用convertToHttpUrl转换为正确的HTTP访问路径
      return convertToHttpUrl(filePath)
    })
  form.reviewImageUrls = urls.join(',')
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    // 更新复查图片URLs
    updateImageUrls()
    
    // 设置复查时间
    form.reviewTime = new Date().toISOString().replace('T', ' ').substring(0, 19)
    
    submitLoading.value = true
    
    // 获取当前用户ID作为复查人
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const reviewerId = userInfo.id || ''
    
    // 构建复查数据
    const reviewData = {
      reviewerId: reviewerId,
      reviewerName: currentUserName.value,
      reviewEffect: form.reviewEffect,
      reviewImageUrls: form.reviewImageUrls,
      reviewTime: form.reviewTime
    }
    
    const response = await inspectionApi.confirmReview(form.id, reviewData)
    
    if (response.code === 200) {
      ElMessage.success('确认复查成功')
      emit('success')
      handleClose()
    } else {
      ElMessage.error(response.message || '确认复查失败')
    }
  } catch (error) {
    console.error('表单验证或提交失败:', error)
    if (error !== false) { // 不是表单验证失败
      ElMessage.error('提交失败')
    }
  } finally {
    submitLoading.value = false
  }
}

// 保存：仅保存页面上现有数据，不变更记录状态（走通用更新接口，不触发状态机）
const handleSave = async () => {
  try {
    saveLoading.value = true
    // 同步图片URL到表单
    updateImageUrls()
    const payload = {
      id: form.id,
      reviewEffect: form.reviewEffect,
      reviewImageUrls: form.reviewImageUrls
    }
    const response = await inspectionApi.update(payload)
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

// 关闭弹窗
const handleClose = () => {
  formRef.value?.resetFields()
  imageFileList.value = []
  rectificationImages.value = []
  problemImages.value = []
  Object.assign(form, {
    id: '',
    reviewEffect: '',
    reviewTime: '',
    reviewImageUrls: '',
    status: 2
  })
  emit('update:visible', false)
}

// 与移动端保持一致，不需要额外的数据加载
</script>

<style scoped>
.problem-info {
  margin-bottom: 24px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
}

.problem-info h4 {
  margin-top: 0;
  margin-bottom: 16px;
  color: #303133;
}

.rectification-info {
  margin-bottom: 24px;
  padding: 16px;
  background: #f0f9ff;
  border-radius: 8px;
}

.rectification-info h4 {
  margin-top: 0;
  margin-bottom: 16px;
  color: #303133;
}

.rectification-info h5 {
  margin: 8px 0;
  color: #606266;
  font-size: 14px;
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.review-form h4 {
  margin-top: 0;
  margin-bottom: 16px;
  color: #303133;
}

.image-upload-container {
  width: 100%;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-upload--picture-card) {
  width: 100px;
  height: 100px;
}

:deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 100px;
  height: 100px;
}
</style>