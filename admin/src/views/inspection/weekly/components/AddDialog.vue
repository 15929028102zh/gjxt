<template>
  <el-dialog
    v-model="dialogVisible"
    title="新增周检记录"
    width="80%"
    :before-close="handleClose"
    destroy-on-close
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <!-- 问题照片 -->
        <el-col :span="24">
          <el-form-item label="问题照片" prop="problemImageUrls">
            <div class="image-upload-container">
              <DragUpload
                v-model="imageFileList"
                :action="uploadAction"
                :limit="3"
                :max-size="2"
                accept="image/jpeg,image/png"
                list-type="picture-card"
                primary-text="将图片拖到此处，或点击上传"
                secondary-text="支持拖拽上传"
                tip-text="只能上传jpg/png文件，且不超过2MB，最多3张"
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

        <!-- 问题描述 -->
        <el-col :span="24">
          <el-form-item label="问题描述" prop="problemDescription">
            <el-input
              v-model="form.problemDescription"
              type="textarea"
              :rows="4"
              placeholder="请详细描述问题情况..."
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
        </el-col>

        <!-- 责任单位和责任人 -->
        <el-col :span="12">
          <el-form-item label="责任单位" prop="responsibleUnit">
            <el-input v-model="form.responsibleUnit" placeholder="请输入责任单位" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任人" prop="responsiblePerson">
            <el-input v-model="form.responsiblePerson" placeholder="请输入责任人姓名" />
          </el-form-item>
        </el-col>

        <!-- 性质界定和考核情况 -->
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
        <el-col :span="12">
          <el-form-item label="考核情况" prop="assessmentSituation">
            <el-select 
              v-model="form.assessmentSituation" 
              placeholder="请选择或输入考核情况"
              style="width: 100%;"
              filterable
              allow-create
              default-first-option
            >
              <el-option
                v-for="item in assessmentOptions"
                :key="item.id"
                :label="item.name"
                :value="item.name"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <!-- 安全记分 -->
        <el-col :span="12">
          <el-form-item label="安全记分" prop="safetyScore">
            <el-input-number
              v-model="form.safetyScore"
              :min="0"
              style="width: 100%;"
              placeholder="请输入安全记分"
            />
          </el-form-item>
        </el-col>

        <!-- 专业和发生部位 -->
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
        <el-col :span="12">
          <el-form-item label="发生部位" prop="location">
            <el-input v-model="form.location" placeholder="请输入发生部位" />
          </el-form-item>
        </el-col>

        <!-- 检查人 -->
        <el-col :span="12">
          <el-form-item label="检查人" prop="inspector">
            <el-input 
              v-model="form.inspector" 
              placeholder="请输入检查人姓名"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>

        <!-- 所属部门 -->
        <el-col :span="12">
          <el-form-item label="所属部门" prop="departmentId">
            <el-select 
              v-model="form.departmentId" 
              placeholder="请选择所属部门"
              style="width: 100%;"
              filterable
            >
              <el-option
                v-for="item in departmentOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <!-- 整改期限 -->
        <el-col :span="12">
          <el-form-item label="整改期限" prop="rectificationDeadline">
            <el-date-picker
              v-model="form.rectificationDeadline"
              type="datetime"
              placeholder="请选择整改期限"
              style="width: 100%;"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { inspectionApi, getDictByType, getUserList, getDeptList } from '@/api/inspection'
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
const imageFileList = ref([])

// 弹窗显示状态
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

// 表单数据
const form = reactive({
  problemDescription: '',
  responsibleUnit: '',
  responsiblePerson: '',
  natureDefinition: '',
  assessmentSituation: '',
  safetyScore: 0,
  professionId: '',
  location: '',
  inspector: '',
  departmentId: '', // 所属部门ID
  rectificationDeadline: '',
  problemImageUrls: '',
  reporterId: '', // 将从当前登录用户获取
  status: 0 // 默认状态为待整改
})

// 表单验证规则
const rules = {
  problemDescription: [
    { required: true, message: '请输入问题描述', trigger: 'blur' }
  ],
  responsibleUnit: [
    { required: true, message: '请输入责任单位', trigger: 'blur' }
  ],
  responsiblePerson: [
    { required: true, message: '请输入责任人', trigger: 'blur' }
  ],
  professionId: [
    { required: true, message: '请选择专业', trigger: 'change' }
  ],
  inspector: [
    { required: true, message: '请输入检查人', trigger: 'blur' }
  ],
  departmentId: [
    { required: true, message: '请选择所属部门', trigger: 'change' }
  ],
  rectificationDeadline: [
    { required: true, message: '请选择整改期限', trigger: 'change' }
  ]
}

// 选项数据
const natureOptions = ref([])
const professionOptions = ref([])
const assessmentOptions = ref([
  { id: '1', name: '提出警告' },
  { id: '2', name: '-50' },
  { id: '3', name: '-100' },
  { id: '4', name: '-200' },
  { id: '5', name: '-300' }
])
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



// 获取当前用户部门信息
const getCurrentUserDepartment = () => {
  try {
    const userInfoStr = localStorage.getItem('userInfo')
    if (userInfoStr) {
      const userInfo = JSON.parse(userInfoStr)
      return userInfo.deptId || userInfo.departmentId || ''
    }
  } catch (error) {
    console.error('获取用户部门信息失败:', error)
  }
  return ''
}

// 监听弹窗显示状态
watch(() => props.visible, (val) => {
  if (val) {
    // 弹窗打开时，如果是新增模式（没有formData或formData为空），设置默认部门
    if (!props.formData || Object.keys(props.formData).length === 0) {
      const userDeptId = getCurrentUserDepartment()
      if (userDeptId) {
        form.departmentId = userDeptId
      }
    }
  }
})

// 监听表单数据变化
watch(() => props.formData, (newData) => {
  if (newData && Object.keys(newData).length > 0) {
    // 排除ID字段，避免新增时主键重复
    const { id, ...dataWithoutId } = newData
    Object.assign(form, dataWithoutId)
    // 处理图片数据
    if (newData.problemImageUrls) {
      const urls = parseImageUrls(newData.problemImageUrls)
      imageFileList.value = urls.map((url, index) => ({
        name: `image_${index}`,
        url: url
      }))
    }
  } else {
    // 新增模式，设置当前用户部门为默认值
    const userDeptId = getCurrentUserDepartment()
    if (userDeptId) {
      form.departmentId = userDeptId
    }
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

    // 加载考核情况
    // const assessmentResponse = await getDictByType('考核情况')
    // if (assessmentResponse.code === 200) {
    //   assessmentOptions.value = assessmentResponse.data.map(item => ({
    //     id: item.id,
    //     name: item.dictValue || item.dictLabel
    //   }))
    // }

    // 加载部门列表
    const deptResponse = await getDeptList()
    if (deptResponse.code === 200) {
      departmentOptions.value = deptResponse.data.map(item => ({
        id: item.id,
        name: item.deptName || item.name
      }))
    }
  } catch (error) {
    console.error('加载字典数据失败:', error)
  }
}

// 设置默认整改时间（7天后）
const setDefaultRectificationTime = () => {
  const now = new Date()
  const defaultTime = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000) // 7天后
  const year = defaultTime.getFullYear()
  const month = String(defaultTime.getMonth() + 1).padStart(2, '0')
  const day = String(defaultTime.getDate()).padStart(2, '0')
  const hours = String(defaultTime.getHours()).padStart(2, '0')
  const minutes = String(defaultTime.getMinutes()).padStart(2, '0')
  const seconds = String(defaultTime.getSeconds()).padStart(2, '0')
  
  form.rectificationDeadline = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
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
  form.problemImageUrls = urls.join(',')
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    // 更新图片URLs
    updateImageUrls()
    
    // 获取当前用户ID作为reporterId
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    form.reporterId = userInfo.id || ''
    
    submitLoading.value = true
    
    // 调试日志：检查提交的数据
    console.log('提交的表单数据:', form)
    
    const response = await inspectionApi.create(form)
    
    if (response.code === 200) {
      ElMessage.success('新增记录成功')
      emit('success')
      handleClose()
    } else {
      ElMessage.error(response.message || '新增记录失败')
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
  Object.assign(form, {
    problemDescription: '',
    responsibleUnit: '',
    responsiblePerson: '',
    natureDefinition: '',
    assessmentSituation: '',
    safetyScore: 0,
    professionId: '',
    location: '',
    inspector: '',
    departmentId: '',
    rectificationDeadline: '',
    problemImageUrls: '',
    reporterId: '',
    status: 0
  })
  // 重新设置默认整改时间
  setDefaultRectificationTime()
  emit('update:visible', false)
}

// 页面挂载时加载数据
onMounted(() => {
  loadDictData()
  setDefaultRectificationTime()
})
</script>

<style scoped>
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