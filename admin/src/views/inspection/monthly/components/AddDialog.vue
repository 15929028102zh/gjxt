<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑月检记录' : '新增月检记录'"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="120px"
      class="add-form"
    >
      <el-row :gutter="20">
        <el-col :span="12" v-if="formData.taskNo">
          <el-form-item label="任务编号">
            <el-input
              v-model="formData.taskNo"
              placeholder="任务编号"
              readonly
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.groupLeaderName">
          <el-form-item label="检查组长">
            <el-input
              v-model="formData.groupLeaderName"
              placeholder="检查组长"
              readonly
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.deputyLeaderName">
          <el-form-item label="检查副组长">
            <el-input
              v-model="formData.deputyLeaderName"
              placeholder="检查副组长"
              readonly
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="问题照片" prop="problemImageUrls">
            <DragUpload
              v-model="imageFileList"
              :limit="5"
              accept="image/*"
              :multiple="true"
              list-type="picture-card"
              @success="handleImageUploadSuccess"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="问题描述" prop="problemDescription">
            <el-input
              v-model="formData.problemDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入问题描述"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="专业类别" prop="professionalTypeId">
          <el-select
            v-model="formData.professionalTypeId"
              placeholder="请选择专业类别"
              style="width: 100%"
              clearable
              @change="handleProfessionChange"
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
          <el-form-item label="模块" prop="moduleId">
            <el-select
              v-model="formData.moduleId"
              placeholder="请选择模块"
              style="width: 100%"
              @change="handleModuleChange"
            >
              <el-option
                v-for="item in moduleOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任" prop="responsibilityId">
            <el-select
              v-model="formData.responsibilityId"
              placeholder="请选择责任"
              style="width: 100%"
              @change="handleResponsibilityChange"
            >
              <el-option
                v-for="item in responsibilityOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题" prop="issueId">
            <el-select
              v-model="formData.issueId"
              placeholder="请选择问题"
              style="width: 100%"
            >
              <el-option
                v-for="item in problemOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性质界定" prop="qualityId">
            <el-select
              v-model="formData.qualityId"
              placeholder="请选择性质界定"
              style="width: 100%"
            >
              <el-option
                v-for="item in natureOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="安全记分" prop="safetyScore">
            <el-input-number
              v-model="formData.safetyScore"
              :min="0"
              :max="100"
              :precision="1"
              placeholder="请输入安全记分"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="发现位置" prop="location">
            <el-input
              v-model="formData.location"
              placeholder="请输入发现位置"
              maxlength="100"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查人" prop="inspector">
            <el-input
              v-model="formData.inspector"
              placeholder="请输入检查人"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任单位" prop="responsibleUnit">
            <el-input
              v-model="formData.responsibleUnit"
              placeholder="请输入责任单位"
              maxlength="100"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任人" prop="responsiblePerson">
            <el-input
              v-model="formData.responsiblePerson"
              placeholder="请输入责任人"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="整改期限" prop="rectificationDeadline">
            <el-date-picker
              v-model="formData.rectificationDeadline"
              type="date"
              placeholder="请选择整改期限"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属部门" prop="departmentId">
            <el-select
              v-model="formData.departmentId"
              placeholder="请选择所属部门"
              style="width: 100%"
              :disabled="!isAdminUser"
            >
              <el-option
                v-for="dept in departmentList"
                :key="dept.id"
                :label="dept.deptName"
                :value="dept.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          {{ isEdit ? '更新' : '保存' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import DragUpload from '@/components/DragUpload.vue'
import { monthlyRecordApi, getProfessionList, getNatureDefinitionList, getDeptList } from '@/api/monthlyRecord'
import { gjbzDataApi } from '@/api/gjbz'

// Props
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

// Emits
const emit = defineEmits(['update:visible', 'success'])

// 响应式数据
const formRef = ref()
const submitLoading = ref(false)
const imageFileList = ref([])
const professionOptions = ref([])
const moduleOptions = ref([])
const responsibilityOptions = ref([])
const problemOptions = ref([])
const natureOptions = ref([])
const departmentList = ref([])
const taskOptions = ref([])

// 存储所有岗检标准记录供本地查询
let allGjbzRecords = []

// 表单数据
const formData = reactive({
  id: '',
  taskId: '',
  taskName: '',
  taskNo: '',
  departmentName: '',
  groupLeaderName: '',
  deputyLeaderName: '',
  problemImageUrls: [],
  problemDescription: '',
  professionalTypeId: '',
  professionalTypeName: '',
  moduleId: '',
  moduleName: '',
  responsibilityId: '',
  responsibilityName: '',
  issueId: '',
  issueName: '',
  qualityId: '',
  qualityName: '',
  safetyScore: 0,
  location: '',
  inspector: '',
  responsibleUnit: '',
  responsiblePerson: '',
  rectificationDeadline: '',
  departmentId: ''
})

// 表单验证规则
const rules = {
  problemDescription: [
    { required: true, message: '请输入问题描述', trigger: 'blur' }
  ],
  professionalTypeId: [
    { required: true, message: '请选择专业类别', trigger: 'change' }
  ],
  moduleId: [
    { required: true, message: '请选择模块', trigger: 'change' }
  ],
  responsibilityId: [
    { required: true, message: '请选择责任', trigger: 'change' }
  ],
  issueId: [
    { required: true, message: '请选择问题', trigger: 'change' }
  ],
  qualityId: [
    { required: true, message: '请选择性质界定', trigger: 'change' }
  ],
  location: [
    { required: true, message: '请输入发现位置', trigger: 'blur' }
  ],
  inspector: [
    { required: true, message: '请输入检查人', trigger: 'blur' }
  ],
  responsibleUnit: [
    { required: true, message: '请输入责任单位', trigger: 'blur' }
  ],
  responsiblePerson: [
    { required: true, message: '请输入责任人', trigger: 'blur' }
  ],
  rectificationDeadline: [
    { required: true, message: '请选择整改期限', trigger: 'change' }
  ]
}

// 计算属性
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

const isEdit = computed(() => {
  return !!(props.formData && props.formData.id)
})

const isAdminUser = computed(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  return userInfo.isAdmin === 1 || userInfo.username === 'admin'
})

// 监听弹窗显示状态
let isInitializing = false
watch(() => props.visible, (newVal) => {
  if (newVal) {
    isInitializing = true
    nextTick(async () => {
      console.log('=== 开始初始化弹窗 ===')
      resetForm()
      await loadOptions()
      
      // 编辑模式或新增模式都需要回写数据
      if (props.formData) {
        console.log('\u56de\u5199props.formData:', props.formData)
        console.log('props.formData.professionalTypeId:', props.formData.professionalTypeId, '\u7c7b\u578b:', typeof props.formData.professionalTypeId)
        console.log('props.formData.moduleId:', props.formData.moduleId, '\u7c7b\u578b:', typeof props.formData.moduleId)
        // 编辑模式或新增模式都需要回写数据
        // 根据是否有ID字段判断是否是编辑模式
        const isEdit = !!props.formData.id
        console.log('是否是编辑模式:', isEdit)
        
        Object.assign(formData, {
          ...props.formData,
          taskId: props.formData.taskId || '',
          taskName: props.formData.taskName || '',
          taskNo: props.formData.taskNo || '',
          departmentName: props.formData.departmentName || '',
          groupLeaderName: props.formData.groupLeaderName || '',
          deputyLeaderName: props.formData.deputyLeaderName || '',
          inspector: props.formData.inspector || props.formData.groupLeaderName || '',
          professionalTypeId: props.formData.professionalTypeId || '',
          professionalTypeName: props.formData.professionalTypeName || '',
          moduleId: props.formData.moduleId || '',
          moduleName: props.formData.moduleName || '',
          responsibilityId: props.formData.responsibilityId || '',
          responsibilityName: props.formData.responsibilityName || '',
          issueId: props.formData.issueId || '',
          issueName: props.formData.issueName || '',
          qualityId: props.formData.qualityId || '',
          qualityName: props.formData.qualityName || '',
          problemImageUrls: '',
          safetyScore: props.formData.safetyScore || 0,
          location: props.formData.location || '',
          responsibleUnit: props.formData.responsibleUnit || '',
          responsiblePerson: props.formData.responsiblePerson || '',
          rectificationDeadline: props.formData.rectificationDeadline || '',
          departmentId: props.formData.departmentId || ''
        })
        // 处理图片数据 - 从后端的recordPhotos提取图片路径
        if (props.formData.recordPhotos) {
          const photoUrls = props.formData.recordPhotos.split(',')
          imageFileList.value = photoUrls.map((url, index) => ({
            name: `image_${index}`,
            url: url.trim()
          }))
          console.log('回写图片列表:', imageFileList.value)
        }
        console.log('回写后的formData:', formData)
        
        // 编辑模式下，如果已有专业类别，需要串联加载下级数据
        if (props.formData.id && formData.professionalTypeId) {
          console.log('编辑模式：开始加载专业的下级数据')
          await handleProfessionChange(formData.professionalTypeId)
        }
        // 新增模式下，如果从任务回写了专业类别，也需要加载下级模块
        else if (!props.formData.id && formData.professionalTypeId) {
          console.log('新增模式：开始加载专业的模块')
          await handleProfessionChange(formData.professionalTypeId)
        }
      }
      
      isInitializing = false
      console.log('=== 初始化完成 ===')
    })
  }
})

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.keys(formData).forEach(key => {
    if (Array.isArray(formData[key])) {
      formData[key] = []
    } else if (typeof formData[key] === 'number') {
      formData[key] = 0
    } else {
      formData[key] = ''
    }
  })
}

// 加载选项数据
const loadOptions = async () => {
  try {
    console.log('=== 开始加载选项数据 ===')
    // 只加载专业、性质界定、部门等基础数据
    const [professionResponse, natureResponse, deptResponse] = await Promise.all([
      getProfessionList(),
      getNatureDefinitionList(),
      getDeptList()
    ])

    console.log('professionResponse:', professionResponse)
    
    // 专业类别（第一级下拉框）
    if (professionResponse && professionResponse.code === 200) {
      // API返回的是字符串数组，需要转换为对象数组
      professionOptions.value = (professionResponse.data || []).map(item => {
        // 如果是字符串，直接作为name
        if (typeof item === 'string') {
          return {
            id: item,
            name: item,
            label: item
          }
        }
        // 否则为对象
        return {
          id: item.id,
          name: item.dictValue || item.dictLabel || item.name,
          label: item.dictLabel || item.label || item.name
        }
      })
      console.log('专业类别选项:', professionOptions.value.length, '个')
    } else {
      console.error('专业类别加载失败:', professionResponse)
    }

    // 性质界定选项
    if (natureResponse && natureResponse.code === 200) {
      natureOptions.value = (natureResponse.data || []).map(item => ({
        id: item.id,
        name: item.dictValue || item.dictLabel,
        label: item.dictLabel
      }))
      console.log('性质界定选项:', natureOptions.value.length, '个')
    }

    // 部门列表
    if (deptResponse && deptResponse.code === 200) {
      departmentList.value = deptResponse.data
      console.log('部门列表:', departmentList.value.length, '个')
    }
    
    console.log('=== 选项数据加载完成 ===')
  } catch (error) {
    console.error('加载选项数据失败:', error)
    ElMessage.error('加载选项数据失败: ' + error.message)
  }
}

// 专业类别变化
const handleProfessionChange = async (professionalTypeId) => {
  console.log('handleProfessionChange called with:', professionalTypeId)
  
  if (professionalTypeId) {
    try {
      // 找到选中的专业对象
      let selectedProfession = professionOptions.value.find(item => item.id === professionalTypeId)
      let professionName = ''
      
      if (!selectedProfession) {
        // 按ID找不到，尝试使用formData中的professionalTypeName
        professionName = formData.professionalTypeName
        if (!professionName) {
          console.error('找不到专业:', professionalTypeId)
          return
        }
        // 如果professionName是数字，根据数字不法直接匹配，视为改次后台干脚，实际应详查后台需求
        // 直接使用professionalName(专业名称)调用API
        console.log('按ID找不到，最终使用professionalTypeName:', professionName)
      } else {
        professionName = selectedProfession.name || selectedProfession.label
        console.log('选中专业名称:', professionName)
      }
      
      // 清空下级选项
      moduleOptions.value = []
      responsibilityOptions.value = []
      problemOptions.value = []
      formData.moduleId = ''
      formData.responsibilityId = ''
      formData.issueId = ''
      
      // 从后台API获取该专业下的模块
      const moduleResponse = await gjbzDataApi.getModulesByType(professionName)
      console.log('获取模块响应:', moduleResponse)
      
      if (moduleResponse && moduleResponse.code === 200) {
        const modules = moduleResponse.data || []
        console.log('获取的模块:', modules.length, '个')
        // 转换为下拉框选项格式
        const newModules = modules.map((name, index) => ({
          id: name, // 使用模块名称作为ID
          name
        }))
        moduleOptions.value.splice(0, moduleOptions.value.length, ...newModules)
        console.log('moduleOptions设置成功:', moduleOptions.value.length, '个模块')
      }
    } catch (error) {
      console.error('加载模块数据失败:', error)
    }
  }
}

// 模块变化
const handleModuleChange = async (moduleId) => {
  console.log('handleModuleChange called with:', moduleId)
  
  if (moduleId) {
    try {
      // 清空下级选项
      responsibilityOptions.value = []
      problemOptions.value = []
      formData.responsibilityId = ''
      formData.issueId = ''
      
      // 从后台API获取该模块下的责任
      const moduleName = moduleId // 模块ID就是模块名称
      const respResponse = await gjbzDataApi.getResponsibilitiesByModule(moduleName)
      console.log('获取责任响应:', respResponse)
      
      if (respResponse && respResponse.code === 200) {
        const responsibilities = respResponse.data || []
        console.log('获取的责任:', responsibilities.length, '个')
        // 转换为下拉框选项格式
        const newResponsibilities = responsibilities.map((name, index) => ({
          id: name, // 使用责任名称作为ID
          name
        }))
        responsibilityOptions.value.splice(0, responsibilityOptions.value.length, ...newResponsibilities)
        console.log('responsibilityOptions设置成功:', responsibilityOptions.value.length, '个责任')
      }
    } catch (error) {
      console.error('加载责任数据失败:', error)
    }
  }
}

// 责任变化
const handleResponsibilityChange = async (responsibilityId) => {
  console.log('handleResponsibilityChange called with:', responsibilityId)
  
  if (responsibilityId) {
    try {
      // 清空问题选项
      problemOptions.value = []
      formData.issueId = ''
      
      // 从后台API获取该责任下的问题
      const responsibilityName = responsibilityId // 责任ID就是责任名称
      const issueResponse = await gjbzDataApi.getIssuesByResponsibility(responsibilityName)
      console.log('获取问题响应:', issueResponse)
      
      if (issueResponse && issueResponse.code === 200) {
        const issues = issueResponse.data || []
        console.log('获取的问题:', issues.length, '个')
        // 转换为下拉框选项格式
        const newIssues = issues.map((name, index) => ({
          id: name, // 使用问题名称作为ID
          name
        }))
        problemOptions.value.splice(0, problemOptions.value.length, ...newIssues)
        console.log('problemOptions设置成功:', problemOptions.value.length, '个问题')
      }
    } catch (error) {
      console.error('加载问题数据失败:', error)
    }
  }
}

// 图片上传成功处理
const handleImageUploadSuccess = (response, file, fileList) => {
  if (response.code === 200) {
    ElMessage.success('图片上传成功')
    // 上传成功后更新图片URLs
    updateImageUrls()
  } else {
    ElMessage.error(response.message || '图片上传失败')
  }
}

// 移除图片時更新路径
const handleImageRemove = (file, fileList) => {
  updateImageUrls()
}

// 更新图片URLs - 从上传组件中提取路径並保存为formData.problemImageUrls
const updateImageUrls = () => {
  console.log('updateImageUrls 调用, imageFileList.value:', imageFileList.value)
  const urls = imageFileList.value
    .filter(file => {
      const hasUrl = file.url || file.response?.data?.filePath
      console.log('筛选文件:', file.name, '是否有url:', !!hasUrl, '文件对象:', file)
      return hasUrl
    })
    .map(file => {
      // 优先使用url（DragUpload组件上传成功后会去添加url），其次打针response.data.filePath
      const path = file.url || file.response?.data?.filePath
      console.log('批取url:', path)
      return path
    })
  // 保存为逗号分隔的字符串
  formData.problemImageUrls = urls.join(',')
  console.log('更新后的图片URLs:', formData.problemImageUrls)
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

    // 提交前更新图片URLs
    updateImageUrls()
    
    const submitData = { ...formData }
    
    console.log('调试: formData.problemImageUrls =', formData.problemImageUrls, '类型:', typeof formData.problemImageUrls)
    
    // 新增时设置状态为4(已提交)並一起孠存部门名称
    if (!isEdit.value) {
      submitData.recordStatus = 4
      // 部门名称从部门列表中查找
      if (formData.departmentId && departmentList.value && departmentList.value.length > 0) {
        const dept = departmentList.value.find(d => d.id === formData.departmentId)
        if (dept) {
          submitData.departmentName = dept.deptName || dept.name
        }
      }
    }
    
    // 映射为后端的photos字段
    if (formData.problemImageUrls && formData.problemImageUrls.length > 0) {
      // problemImageUrls 是一个逗号分隔的字符串，直接赋值到photos
      submitData.photos = formData.problemImageUrls
    } else {
      submitData.photos = ''
    }
    console.log('调试: 最终photos =', submitData.photos)
    console.log('调试: 削除problemImageUrls前的submitData =', JSON.parse(JSON.stringify(submitData)))
        
    // 削除前端字段，不应该发送给后端
    delete submitData.problemImageUrls
    delete submitData.recordPhotos
        
    console.log('调试: 削除后的submitData =', JSON.parse(JSON.stringify(submitData)))

    let response
    if (isEdit.value) {
      response = await monthlyRecordApi.update(submitData)
    } else {
      response = await monthlyRecordApi.create(submitData)
    }

    if (response.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '保存成功')
      emit('success')
    } else {
      ElMessage.error(response.message || '操作失败')
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
.add-form {
  padding: 20px 0;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-textarea__inner) {
  resize: vertical;
}
</style>