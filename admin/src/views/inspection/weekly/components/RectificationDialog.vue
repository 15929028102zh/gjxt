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
        <el-descriptions-item label="发生部位">
          {{ formData.location }}
        </el-descriptions-item>
        <el-descriptions-item label="检查人">
          {{ formData.inspector }}
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
          <!-- 性质界定 -->
          <el-col :span="12">
            <el-form-item label="性质界定" prop="natureDefinition">
              <el-select 
                v-model="form.natureDefinition" 
                placeholder="请选择问题性质"
                style="width: 100%;"
              >
                <el-option
                  v-for="item in natureOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.name"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <!-- 专业 -->
          <el-col :span="12">
            <el-form-item label="专业" prop="professionId">
              <el-select 
                v-model="form.professionId" 
                placeholder="请选择专业"
                style="width: 100%;"
              >
                <el-option
                  v-for="item in professionOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <!-- 所属部门 -->
          <el-col :span="12">
            <el-form-item label="所属部门" prop="departmentId">
              <el-select 
                v-model="form.departmentId" 
                placeholder="请选择所属部门"
                style="width: 100%;"
              >
                <el-option
                  v-for="item in departmentOptions"
                  :key="item.id"
                  :label="item.deptName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <!-- 整改状态 -->
          <el-col :span="12">
            <el-form-item label="整改状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="0">未整改</el-radio>
                <el-radio :label="1">已整改</el-radio>
              </el-radio-group>
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
              />
            </el-form-item>
          </el-col>

          <!-- 整改图片 -->
          <el-col :span="24">
            <el-form-item label="整改图片">
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
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确认整改</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { inspectionApi, getDictByType, getDeptList } from '@/api/inspection'
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
const problemImages = ref([]) // 添加问题图片状态

// 弹窗显示状态
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

// 表单数据
const form = reactive({
  id: '',
  problemDescription: '',
  responsibleUnit: '',
  responsiblePerson: '',
  natureDefinition: '',
  assessmentSituation: '',
  assessmentAmount: 0,
  safetyScore: 0,
  professionId: '',
  location: '',
  inspector: '',
  groupLeader: '',
  deputyLeader: '',
  departmentId: '',
  rectificationDeadline: '',
  problemImageUrls: '',
  reporterId: '',
  rectificationEffect: '',
  rectificationImageUrls: '',
  status: 1 // 整改后状态为已整改
})

// 表单验证规则
const rules = {
  rectificationEffect: [
    { required: true, message: '请填写整改效果描述', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择整改状态', trigger: 'change' }
  ]
}

// 选项数据
const natureOptions = ref([])
const professionOptions = ref([])
const departmentOptions = ref([])

// 图片上传相关
const uploadAction = computed(() => {
  return 'http://192.168.244.128:8080/api/upload/image'
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
    
    // 转换为完整URL，添加/api前缀
    return urlList.map(url => {
      if (url && url.startsWith('/')) {
        return `http://192.168.244.128:8080/api${url}`
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
    console.log('整改弹框数据加载:', newData)
    
    // 复制原有数据
    Object.assign(form, newData)
    
    // 处理问题图片展示
    problemImages.value = parseImageUrls(newData.problemImageUrls)
    console.log('问题图片URL:', problemImages.value)
    
    // 处理整改图片数据
    if (newData.rectificationImageUrls) {
      const urls = parseImageUrls(newData.rectificationImageUrls)
      imageFileList.value = urls.map((url, index) => ({
        name: `rectification_image_${index}`,
        url: url
      }))
    } else {
      imageFileList.value = []
    }
    
    // 处理性质界定数据回写（延迟处理等待字典数据加载完成）
    setTimeout(() => {
      if (newData.natureDefinition && natureOptions.value.length > 0) {
        // 数据库存储的是名称，直接使用名称进行匹配
        const matchedOption = natureOptions.value.find(option => 
          option.name === newData.natureDefinition
        )
        if (matchedOption) {
          form.natureDefinition = matchedOption.name
          console.log('性质界定名称匹配成功:', { id: matchedOption.id, name: matchedOption.name })
        } else {
          // 兼容可能存在ID格式的旧数据
          const idMatch = natureOptions.value.find(option => 
            option.id === newData.natureDefinition || 
            option.id.toString() === newData.natureDefinition.toString()
          )
          if (idMatch) {
            form.natureDefinition = idMatch.name
            console.log('性质界定ID匹配转换为名称:', { id: idMatch.id, name: idMatch.name })
          } else {
            console.warn('性质界定匹配失败:', newData.natureDefinition, '可用选项:', natureOptions.value)
          }
        }
      }
    }, 300) // 等待300ms确保字典数据加载完成
  }
}, { immediate: true, deep: true })

// 加载字典数据
const loadDictData = async () => {
  try {
    // 加载性质界定
    const natureResponse = await getDictByType('性质界定')
    if (natureResponse.code === 200) {
      natureOptions.value = natureResponse.data.map(item => ({
        id: item.id,
        name: item.dictValue || item.dictLabel
      }))
    }

    // 加载专业
    const professionResponse = await getDictByType('专业')
    if (professionResponse.code === 200) {
      professionOptions.value = professionResponse.data.map(item => ({
        id: item.id,
        name: item.dictValue || item.dictLabel
      }))
    }
  } catch (error) {
    console.error('加载字典数据失败:', error)
  }
}

// 加载部门数据
const loadDepartmentData = async () => {
  try {
    const response = await getDeptList()
    if (response.code === 200) {
      departmentOptions.value = response.data || []
    }
  } catch (error) {
    console.error('加载部门数据失败:', error)
  }
}

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
  form.rectificationImageUrls = JSON.stringify(urls)
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
    
    // 更新整改图片URLs
    updateImageUrls()
    
    // 设置整改时间
    const rectificationTime = new Date().toISOString().replace('T', ' ').substring(0, 19)
    
    submitLoading.value = true
    
    // 构建整改数据
    const rectificationData = {
      rectificationEffect: form.rectificationEffect,
      rectificationImageUrls: form.rectificationImageUrls,
      rectificationTime: rectificationTime,
      status: form.status,
      natureDefinition: form.natureDefinition,
      professionId: form.professionId,
      departmentId: form.departmentId
    }
    
    const response = await inspectionApi.confirmRectification(form.id, rectificationData)
    
    if (response.code === 200) {
      ElMessage.success('确认整改成功')
      emit('success')
      handleClose()
    } else {
      ElMessage.error(response.message || '确认整改失败')
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
  emit('update:visible', false)
}

// 页面挂载时加载数据
onMounted(() => {
  loadDictData()
  loadDepartmentData()
})
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

.problem-info h5 {
  margin: 8px 0;
  color: #606266;
  font-weight: 500;
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.rectification-form h4 {
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