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

          <!-- 整改人（当前登录用户，只读） -->
          <el-col :span="12">
            <el-form-item label="整改人">
              <el-input v-model="currentUserName" readonly placeholder="当前登录用户" />
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
                <DragUpload
                  v-model="imageFileList"
                  :action="uploadAction"
                  :limit="3"
                  :max-size="5"
                  accept="image/jpeg,image/png"
                  list-type="picture-card"
                  primary-text="将图片拖到此处，或点击上传"
                  secondary-text="支持拖拽上传"
                  tip-text="只能上传jpg/png文件，且不超过5MB，最多3张"
                  :before-upload-validator="beforeImageUpload"
                  :on-success-callback="handleImageSuccess"
                  :on-error-callback="handleImageError"
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
        <el-button type="info" plain :loading="saveLoading" @click="handleSave">保存</el-button>
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
const problemImages = ref([]) // 添加问题图片状态
// 整改人：当前登录用户姓名（只读，确认整改时写库）
const currentUserName = ref('')

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

import { getFullApiUrl, parseImageUrls, getImageUploadUrl } from '@/config/api'

// 图片上传相关
const uploadAction = computed(() => {
  return getImageUploadUrl()
})

const uploadHeaders = computed(() => {
  return {
    'Authorization': `Bearer ${getToken()}`
  }
})

// 监听表单数据变化
watch(() => props.formData, (newData) => {
  if (newData && Object.keys(newData).length > 0) {
    
    // 复制原有数据
    Object.assign(form, newData)
    
    // 处理问题图片展示
    problemImages.value = parseImageUrls(newData.problemImageUrls)
    
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
        } else {
          // 兼容可能存在ID格式的旧数据
          const idMatch = natureOptions.value.find(option => 
            option.id === newData.natureDefinition || 
            option.id.toString() === newData.natureDefinition.toString()
          )
          if (idMatch) {
            form.natureDefinition = idMatch.name
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
const handleImageSuccess = (response, file, fileList) => {
  if (response.code === 200) {
    ElMessage.success('图片上传成功')
    updateImageUrls()
  } else {
    ElMessage.error(response.message || '图片上传失败')
  }
}

// 图片上传失败
const handleImageError = (error, file, fileList) => {
  console.error('图片上传失败:', error)
  ElMessage.error('图片上传失败')
}

// 移除图片
const handleImageRemove = (file, fileList) => {
  updateImageUrls()
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
  form.rectificationImageUrls = urls.join(',')
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
      departmentId: form.departmentId,
      rectifierName: currentUserName.value
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

// 保存：仅保存页面上现有数据，不变更记录状态（走通用更新接口，不触发状态机）
const handleSave = async () => {
  try {
    saveLoading.value = true
    // 同步图片URL到表单
    updateImageUrls()
    const payload = {
      id: form.id,
      problemDescription: form.problemDescription,
      responsibleUnit: form.responsibleUnit,
      responsiblePerson: form.responsiblePerson,
      natureDefinition: form.natureDefinition,
      professionId: form.professionId,
      departmentId: form.departmentId,
      rectificationEffect: form.rectificationEffect,
      rectificationImageUrls: form.rectificationImageUrls,
      rectificationDeadline: form.rectificationDeadline,
      location: form.location,
      inspector: form.inspector
    }
    const response = await inspectionApi.update(payload)
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
  // 整改人默认取当前登录用户
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    currentUserName.value = userInfo.realName || userInfo.username || ''
  } catch (e) {
    currentUserName.value = ''
  }
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