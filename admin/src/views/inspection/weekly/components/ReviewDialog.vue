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
              <div class="image-upload-container">
                <el-upload
                  v-model:file-list="imageFileList"
                  :action="uploadAction"
                  :headers="uploadHeaders"
                  list-type="picture-card"
                  :on-success="handleImageSuccess"
                  :on-error="handleImageError"
                  :on-remove="handleImageRemove"
                  :before-upload="beforeImageUpload"
                  :limit="3"
                  :on-exceed="handleImageExceed"
                >
                  <el-icon><Plus /></el-icon>
                  <template #tip>
                    <div class="el-upload__tip">
                      只能上传jpg/png文件，且不超过2MB，最多3张
                    </div>
                  </template>
                </el-upload>
              </div>
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
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { inspectionApi, getUserList } from '@/api/inspection'
import { getToken } from '@/utils/token'

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
const rectificationImages = ref([])
const problemImages = ref([])

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

// 图片上传相关
const uploadAction = computed(() => {
  return 'http://192.168.42.128:8080/api/upload/image'
})

const uploadHeaders = computed(() => {
  return {
    'Authorization': `Bearer ${getToken()}`
  }
})

// 解析图片URL的工具函数
const parseImageUrls = (imageUrls) => {
  if (!imageUrls) return []
  
  try {
    let urlList = []
    if (typeof imageUrls === 'string') {
      // 首先尝试JSON解析
      try {
        const parsed = JSON.parse(imageUrls)
        if (Array.isArray(parsed)) {
          urlList = parsed
        }
      } catch (e) {
        // JSON解析失败，尝试逗号分割
        urlList = imageUrls.split(',').map(url => url.trim()).filter(url => url)
      }
    } else if (Array.isArray(imageUrls)) {
      urlList = imageUrls
    }
    
    // 转换为完整URL
    return urlList.map(url => {
      if (url && url.startsWith('/')) {
        return `http://192.168.42.128:8080/api${url}`
      }
      return url || ''
    }).filter(url => url)
  } catch (e) {
    console.error('解析图片URL失败:', e, '原始数据:', imageUrls)
    return []
  }
}

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
      try {
        const urls = typeof newData.reviewImageUrls === 'string' 
          ? JSON.parse(newData.reviewImageUrls) 
          : newData.reviewImageUrls
        if (Array.isArray(urls)) {
          imageFileList.value = urls.map((url, index) => ({
            name: `review_image_${index}`,
            url: url
          }))
        }
      } catch (e) {
        console.error('解析复查图片URL失败:', e)
      }
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
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('只能上传 JPG/PNG 格式的图片!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
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
    .map(file => file.response?.data?.filePath || file.url)
  form.reviewImageUrls = JSON.stringify(urls)
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