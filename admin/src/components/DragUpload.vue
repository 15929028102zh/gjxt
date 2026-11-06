<template>
  <div class="drag-upload-container">
    <el-upload
      ref="uploadRef"
      v-model:file-list="fileList"
      :action="uploadAction"
      :headers="uploadHeaders"
      :list-type="listType"
      :before-upload="beforeUpload"
      :on-success="handleUploadSuccess"
      :on-error="handleUploadError"
      :on-remove="handleRemove"
      :on-preview="handlePreview"
      :limit="limit"
      :accept="accept"
      :multiple="multiple"
      :disabled="disabled"
      :drag="true"
      class="drag-upload"
    >
      <div class="upload-dragger">
        <div class="upload-icon">
          <el-icon v-if="listType === 'picture-card'"><Plus /></el-icon>
          <el-icon v-else class="el-icon--upload"><upload-filled /></el-icon>
        </div>
        <div class="upload-text">
          <div class="primary-text">{{ primaryText }}</div>
          <div class="secondary-text">{{ secondaryText }}</div>
        </div>
      </div>
      
      <template #tip>
        <div class="el-upload__tip" v-if="tipText">
          {{ tipText }}
        </div>
      </template>
    </el-upload>

    <!-- 图片预览对话框 -->
    <el-dialog v-model="previewVisible" title="图片预览" width="50%">
      <img :src="previewUrl" alt="预览图片" style="width: 100%; height: auto;" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, UploadFilled } from '@element-plus/icons-vue'
import { getImageUploadUrl } from '@/config/api'
import { getToken } from '@/utils/token'

const props = defineProps({
  // 文件列表
  modelValue: {
    type: Array,
    default: () => []
  },
  // 上传地址
  action: {
    type: String,
    default: ''
  },
  // 上传限制数量
  limit: {
    type: Number,
    default: 9
  },
  // 接受的文件类型
  accept: {
    type: String,
    default: 'image/*'
  },
  // 文件大小限制(MB)
  maxSize: {
    type: Number,
    default: 5
  },
  // 是否支持多选
  multiple: {
    type: Boolean,
    default: true
  },
  // 是否禁用
  disabled: {
    type: Boolean,
    default: false
  },
  // 列表类型
  listType: {
    type: String,
    default: 'picture-card',
    validator: (value) => ['text', 'picture', 'picture-card'].includes(value)
  },
  // 主要提示文字
  primaryText: {
    type: String,
    default: '将文件拖到此处，或点击上传'
  },
  // 次要提示文字
  secondaryText: {
    type: String,
    default: '支持拖拽上传'
  },
  // 底部提示文字
  tipText: {
    type: String,
    default: '支持jpg/png格式，单张图片不超过5MB'
  },
  // 自定义上传前验证
  beforeUploadValidator: {
    type: Function,
    default: null
  },
  // 自定义成功回调
  onSuccessCallback: {
    type: Function,
    default: null
  },
  // 自定义错误回调
  onErrorCallback: {
    type: Function,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'success', 'error', 'remove', 'preview'])

const uploadRef = ref()
const fileList = ref([])
const previewVisible = ref(false)
const previewUrl = ref('')

// 计算上传地址
const uploadAction = computed(() => {
  return props.action || getImageUploadUrl()
})

// 计算上传头部
const uploadHeaders = computed(() => {
  return {
    'Authorization': `Bearer ${getToken()}`
  }
})

// 监听外部文件列表变化
watch(() => props.modelValue, (newVal) => {
  fileList.value = newVal || []
}, { immediate: true, deep: true })

// 监听内部文件列表变化
watch(fileList, (newVal) => {
  emit('update:modelValue', newVal)
}, { deep: true })

// 上传前验证
const beforeUpload = (file) => {
  // 自定义验证器优先
  if (props.beforeUploadValidator) {
    return props.beforeUploadValidator(file)
  }

  // 默认验证
  const isImage = file.type.startsWith('image/')
  const isLtMax = file.size / 1024 / 1024 < props.maxSize

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLtMax) {
    ElMessage.error(`上传图片大小不能超过 ${props.maxSize}MB!`)
    return false
  }
  return true
}

// 上传成功处理
const handleUploadSuccess = (response, file, fileList) => {
  if (response.code === 200) {
    // 更新文件URL
    const index = fileList.findIndex(item => item.uid === file.uid)
    if (index > -1) {
      fileList[index].url = response.data.filePath || response.data.url
      fileList[index].status = 'success'
    }
    
    ElMessage.success('图片上传成功')
    
    // 触发自定义成功回调
    if (props.onSuccessCallback) {
      props.onSuccessCallback(response, file, fileList)
    }
    
    emit('success', response, file, fileList)
  } else {
    ElMessage.error(response.message || '图片上传失败')
    handleUploadError(response, file, fileList)
  }
}

// 上传失败处理
const handleUploadError = (error, file, fileList) => {
  ElMessage.error('图片上传失败')
  
  // 触发自定义错误回调
  if (props.onErrorCallback) {
    props.onErrorCallback(error, file, fileList)
  }
  
  emit('error', error, file, fileList)
}

// 移除文件
const handleRemove = (file, fileList) => {
  emit('remove', file, fileList)
}

// 预览文件
const handlePreview = (file) => {
  previewUrl.value = file.url || file.response?.data?.filePath
  previewVisible.value = true
  emit('preview', file)
}

// 清空文件列表
const clearFiles = () => {
  fileList.value = []
  uploadRef.value?.clearFiles()
}

// 手动上传
const submit = () => {
  uploadRef.value?.submit()
}

// 暴露方法
defineExpose({
  clearFiles,
  submit,
  uploadRef
})
</script>

<style scoped>
.drag-upload-container {
  width: 100%;
}

.drag-upload :deep(.el-upload) {
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
}

.drag-upload :deep(.el-upload:hover) {
  border-color: #409eff;
}

.drag-upload :deep(.el-upload.is-dragover) {
  border-color: #409eff;
  background-color: rgba(64, 158, 255, 0.06);
}

.upload-dragger {
  padding: 40px 20px;
  text-align: center;
  color: #606266;
  min-height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.upload-icon {
  font-size: 48px;
  color: #c0c4cc;
  margin-bottom: 16px;
}

.upload-text {
  line-height: 1.5;
}

.primary-text {
  font-size: 16px;
  color: #606266;
  margin-bottom: 4px;
}

.secondary-text {
  font-size: 14px;
  color: #909399;
}

/* picture-card 模式样式调整 */
.drag-upload.picture-card :deep(.el-upload) {
  width: 148px;
  height: 148px;
  border: 2px dashed #d9d9d9;
}

.drag-upload.picture-card .upload-dragger {
  padding: 20px 10px;
  min-height: auto;
  height: 100%;
}

.drag-upload.picture-card .upload-icon {
  font-size: 28px;
  margin-bottom: 8px;
}

.drag-upload.picture-card .primary-text {
  font-size: 12px;
  margin-bottom: 2px;
}

.drag-upload.picture-card .secondary-text {
  font-size: 11px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .upload-dragger {
    padding: 30px 15px;
    min-height: 100px;
  }
  
  .upload-icon {
    font-size: 36px;
    margin-bottom: 12px;
  }
  
  .primary-text {
    font-size: 14px;
  }
  
  .secondary-text {
    font-size: 12px;
  }
}
</style>