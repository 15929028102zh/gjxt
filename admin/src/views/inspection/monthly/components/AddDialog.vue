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
        <el-col :span="12" v-if="isEdit && formData.taskNo">
          <el-form-item label="任务编号">
            <el-input
              v-model="formData.taskNo"
              placeholder="任务编号"
              readonly
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="isEdit && formData.departmentName">
          <el-form-item label="检查部门">
            <el-input
              v-model="formData.departmentName"
              placeholder="检查部门"
              readonly
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="问题照片" prop="problemImageUrls">
            <DragUpload
              v-model="formData.problemImageUrls"
              :limit="5"
              accept="image/*"
              :multiple="true"
              list-type="picture-card"
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
import { monthlyRecordApi, getProfessionList, getNatureDefinitionList, getDeptList, getModuleByProfessionId, getResponsibilityByModuleId, getProblemByResponsibilityId } from '@/api/monthlyRecord'

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
const professionOptions = ref([])
const moduleOptions = ref([])
const responsibilityOptions = ref([])
const problemOptions = ref([])
const natureOptions = ref([])
const departmentList = ref([])

// 表单数据
const formData = reactive({
  id: '',
  taskId: '',
  taskName: '',
  taskNo: '',
  departmentName: '',
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
watch(() => props.visible, (newVal) => {
  if (newVal) {
    nextTick(() => {
      resetForm()
      loadOptions()
      // 编辑模式下初始化表单数据
      if (props.formData && props.formData.id) {
        Object.assign(formData, {
          ...props.formData,
          taskId: props.formData.taskId || '',
          taskName: props.formData.taskName || '',
          taskNo: props.formData.taskNo || '',
          departmentName: props.formData.departmentName || '',
          professionalTypeId: props.formData.professionalTypeId,
          professionalTypeName: props.formData.professionalTypeName || '',
          moduleId: props.formData.moduleId,
          moduleName: props.formData.moduleName || '',
          responsibilityId: props.formData.responsibilityId,
          responsibilityName: props.formData.responsibilityName || '',
          issueId: props.formData.issueId,
          issueName: props.formData.issueName || '',
          qualityId: props.formData.qualityId,
          qualityName: props.formData.qualityName || '',
          problemImageUrls: Array.isArray(props.formData.photos) 
            ? props.formData.photos.filter(url => url && url.trim() !== '')
            : (props.formData.photos ? props.formData.photos.split(',').filter(url => url && url.trim() !== '') : [])
        })
        
        // 级联加载相关选项数据
        if (formData.professionalTypeId) {
          handleProfessionChange(formData.professionalTypeId)
        }
        if (formData.moduleId) {
          handleModuleChange(formData.moduleId)
        }
        if (formData.responsibilityId) {
          handleResponsibilityChange(formData.responsibilityId)
        }
      }
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
    const [professionResponse, natureResponse, deptResponse] = await Promise.all([
      getProfessionList(),
      getNatureDefinitionList(),
      getDeptList()
    ])

    if (professionResponse.code === 200) {
      professionOptions.value = professionResponse.data
    }

    if (natureResponse.code === 200) {
      natureOptions.value = natureResponse.data
    }

    if (deptResponse.code === 200) {
      departmentList.value = deptResponse.data
    }
  } catch (error) {
    console.error('加载选项数据失败:', error)
  }
}

// 专业类别变化
const handleProfessionChange = async (professionalTypeId) => {
  // 清空下级选项
  formData.moduleId = ''
  formData.responsibilityId = ''
  formData.problemId = ''
  moduleOptions.value = []
  responsibilityOptions.value = []
  problemOptions.value = []
  
  if (professionalTypeId) {
    try {
      const response = await getModuleByProfessionId(professionalTypeId)
      if (response.code === 200) {
        moduleOptions.value = response.data
      }
    } catch (error) {
      console.error('加载模块数据失败:', error)
    }
  }
}

// 模块变化
const handleModuleChange = async (moduleId) => {
  formData.responsibilityId = ''
  formData.problemId = ''
  responsibilityOptions.value = []
  problemOptions.value = []

  if (moduleId) {
    try {
      const response = await getResponsibilityByModuleId(moduleId)
      if (response.code === 200) {
        responsibilityOptions.value = response.data
      }
    } catch (error) {
      console.error('加载责任数据失败:', error)
    }
  }
}

// 责任变化
const handleResponsibilityChange = async (responsibilityId) => {
  formData.problemId = ''
  problemOptions.value = []

  if (responsibilityId) {
    try {
      const response = await getProblemByResponsibilityId(responsibilityId)
      if (response.code === 200) {
        problemOptions.value = response.data
      }
    } catch (error) {
      console.error('加载问题数据失败:', error)
    }
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

    const submitData = { ...formData }
    
    // 处理图片数组 - 将problemImageUrls映射为后端的photos字段
    if (submitData.problemImageUrls && submitData.problemImageUrls.length > 0) {
      submitData.photos = submitData.problemImageUrls.join(',')
    } else {
      submitData.photos = ''
    }
    // 删除前端字段
    delete submitData.problemImageUrls

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