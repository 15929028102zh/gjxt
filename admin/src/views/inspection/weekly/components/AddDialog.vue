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
            <el-input 
              v-model="form.assessmentSituation" 
              placeholder="请输入考核情况"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>

        <!-- 考核金额和安全记分 -->
        <el-col :span="12">
          <el-form-item label="考核金额" prop="assessmentAmount">
            <el-input-number
              v-model="form.assessmentAmount"
              :min="0"
              :precision="2"
              style="width: 100%;"
              placeholder="请输入金额"
            />
          </el-form-item>
        </el-col>
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

        <!-- 检查人、组长、副组长 -->
        <el-col :span="8">
          <el-form-item label="检查人" prop="inspector">
            <el-select 
              v-model="form.inspector" 
              placeholder="请选择检查人"
              style="width: 100%;"
              filterable
            >
              <el-option
                v-for="item in inspectorOptions"
                :key="item.id"
                :label="item.realName"
                :value="item.realName"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="检查组组长" prop="groupLeader">
            <el-input 
              v-model="form.groupLeader"
              placeholder="请输入组长姓名"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="检查组副组长" prop="deputyLeader">
            <el-input 
              v-model="form.deputyLeader"
              placeholder="请输入副组长姓名"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>

        <!-- 所属部门和整改期限 -->
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
    { required: true, message: '请选择检查人', trigger: 'change' }
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
const inspectorOptions = ref([])
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
    
    // 返回处理后的URL列表
    return urlList.filter(url => url)
  } catch (e) {
    console.error('解析图片URL失败:', e, '原始数据:', imageUrls)
    return []
  }
}

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

// 加载用户数据
const loadUserData = async () => {
  try {
    const response = await getUserList({ size: 1000 })
    if (response.code === 200) {
      inspectorOptions.value = response.data.records || []
    }
  } catch (error) {
    console.error('加载用户数据失败:', error)
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
  form.problemImageUrls = JSON.stringify(urls)
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
    console.log('检查组长:', form.groupLeader)
    console.log('副组长:', form.deputyLeader)
    
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
    status: 0
  })
  emit('update:visible', false)
}

// 页面挂载时加载数据
onMounted(() => {
  loadDictData()
  loadUserData()
  loadDepartmentData()
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