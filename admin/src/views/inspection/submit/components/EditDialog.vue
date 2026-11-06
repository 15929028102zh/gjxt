<template>
  <el-dialog
    v-model="dialogVisible"
    title="编辑周检记录"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任人" prop="responsiblePerson">
            <el-input v-model="formData.responsiblePerson" placeholder="请输入责任人" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查人" prop="inspector">
            <el-input v-model="formData.inspector" placeholder="请输入检查人" />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任单位" prop="responsibleUnit">
            <el-input v-model="formData.responsibleUnit" placeholder="请输入责任单位" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="位置" prop="location">
            <el-input v-model="formData.location" placeholder="请输入位置" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="问题描述" prop="problemDescription">
          <el-input
            v-model="formData.problemDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入问题描述"
          />
        </el-form-item>

        <el-form-item label="考核情况" prop="assessmentSituation">
          <el-select 
            v-model="formData.assessmentSituation" 
            placeholder="请选择或输入考核情况" 
            style="width: 100%"
            filterable
            allow-create
            default-first-option
          >
            <el-option label="提出警告" value="提出警告" />
            <el-option label="-50" value="-50" />
            <el-option label="-100" value="-100" />
            <el-option label="-200" value="-200" />
            <el-option label="-300" value="-300" />
          </el-select>
        </el-form-item>

        <!-- <el-form-item label="考核金额" prop="assessmentAmount">
          <el-input-number
            v-model="formData.assessmentAmount"
            :min="0"
            :precision="2"
            placeholder="请输入考核金额"
            style="width: 100%"
          />
        </el-form-item> -->

        <el-form-item label="安全记分" prop="safetyScore">
          <el-input-number
            v-model="formData.safetyScore"
            :min="0"
            placeholder="请输入安全记分"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="专业" prop="professionId">
          <el-select v-model="formData.professionId" placeholder="请选择专业" style="width: 100%">
            <el-option
              v-for="item in professionOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="发生部位" prop="occurrenceLocation">
          <el-input
            v-model="formData.occurrenceLocation"
            placeholder="请输入发生部位"
          />
        </el-form-item>

        <!-- <el-form-item label="检查组组长" prop="inspectionGroupLeader">
          <el-input
            v-model="formData.inspectionGroupLeader"
            placeholder="请输入检查组组长"
          />
        </el-form-item>

        <el-form-item label="检查组副组长" prop="inspectionGroupDeputy">
          <el-input
            v-model="formData.inspectionGroupDeputy"
            placeholder="请输入检查组副组长"
          />
        </el-form-item> -->

        <el-form-item label="所属部门" prop="departmentId">
          <el-select v-model="formData.departmentId" placeholder="请选择所属部门" style="width: 100%">
            <el-option
              v-for="item in departmentOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

      <el-form-item label="性质界定" prop="natureId">
        <el-select v-model="formData.natureId" placeholder="请选择性质界定" style="width: 100%">
          <el-option
            v-for="item in natureOptions"
            :key="item.id"
            :label="item.name"
            :value="item.name"
          />
        </el-select>
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="整改期限" prop="rectificationDeadline">
            <el-date-picker
              v-model="formData.rectificationDeadline"
              type="datetime"
              placeholder="请选择整改期限"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="检查时间" prop="inspectionTime">
            <el-date-picker
              v-model="formData.inspectionTime"
              type="datetime"
              placeholder="请选择检查时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
        </el-col> -->
      </el-row>

      <el-form-item label="问题图片" prop="problemImageUrls">
        <DragUpload
          ref="uploadRef"
          v-model="fileList"
          :limit="9"
          list-type="picture-card"
          primary-text="拖拽图片到此处上传"
          secondary-text="或点击选择图片"
          tip-text="支持jpg/png格式，单张图片不超过5MB，最多上传9张，支持拖拽上传"
          @success="handleUploadSuccess"
          @remove="handleRemove"
          @preview="handlePreview"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSubmit">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, nextTick, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { updateRecord, getDictByType } from '@/api/inspection'
import { deptApi } from '@/api/system'
import { convertToHttpUrl, getFullApiUrl, parseImageUrls, getImageUploadUrl } from '@/config/api'
import DragUpload from '@/components/DragUpload.vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  record: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

const dialogVisible = ref(false)
const loading = ref(false)
const formRef = ref()

const fileList = ref([])

// 字典选项数据
const professionOptions = ref([])
const natureOptions = ref([])
const departmentOptions = ref([])

// 表单数据
const formData = reactive({
  id: '',
  responsiblePerson: '',
  inspector: '',
  responsibleUnit: '',
  location: '',
  problemDescription: '',
  assessmentSituation: '',
  assessmentAmount: 0,
  safetyScore: 0,
  professionId: '',
  occurrenceLocation: '',
  departmentId: '',
  natureId: '',
  rectificationDeadline: '',
  inspectionTime: '',
  problemImageUrls: ''
})

// 表单验证规则
const rules = {
  responsiblePerson: [{ required: true, message: '请输入责任人', trigger: 'blur' }],
  inspector: [{ required: true, message: '请输入检查人', trigger: 'blur' }],
  responsibleUnit: [{ required: true, message: '请输入责任单位', trigger: 'blur' }],
  location: [{ required: true, message: '请输入位置', trigger: 'blur' }],
  problemDescription: [{ required: true, message: '请输入问题描述', trigger: 'blur' }],
  assessmentSituation: [{ required: true, message: '请选择考核情况', trigger: 'change' }],
  assessmentAmount: [{ required: true, message: '请输入考核金额', trigger: 'blur' }],
  safetyScore: [{ required: true, message: '请输入安全记分', trigger: 'blur' }],
  professionId: [{ required: true, message: '请选择专业', trigger: 'change' }],
  occurrenceLocation: [{ required: true, message: '请输入发生部位', trigger: 'blur' }],
  departmentId: [{ required: true, message: '请选择所属部门', trigger: 'change' }],
  natureId: [{ required: true, message: '请选择性质界定', trigger: 'change' }]
  // 移除了时间字段的校验：rectificationDeadline 和 inspectionTime
}

// 监听对话框显示状态
watch(
  () => props.modelValue,
  (val) => {
    dialogVisible.value = val
    if (val) {
      // 加载字典数据
      loadProfessionOptions()
      loadNatureOptions()
      loadDepartmentOptions()
      nextTick(() => {
        initFormData()
      })
    }
  },
  { immediate: true }
)

// 监听对话框内部状态变化
watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
})

// 兼容历史数据：nature_definition 早期曾写入字典 id，这里在选项就绪后回填为字典值
watch([natureOptions, () => formData.natureId], ([options, current]) => {
  if (!current || !options.length) return
  if (options.some(item => item.name === current)) return
  const byId = options.find(item => String(item.id) === String(current))
  if (byId) {
    formData.natureId = byId.name
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

// 初始化表单数据
const initFormData = () => {
  if (props.record && props.record.id) {
    // 编辑模式
    Object.assign(formData, {
      id: props.record.id || '',
      responsiblePerson: props.record.responsiblePerson || '',
      inspector: props.record.inspector || '',
      responsibleUnit: props.record.responsibleUnit || '',
      location: props.record.location || '',
      problemDescription: props.record.problemDescription || '',
      assessmentSituation: props.record.assessmentSituation || '',
      assessmentAmount: props.record.assessmentAmount || 0,
      safetyScore: props.record.safetyScore || 0,
      professionId: props.record.professionId || props.record.profession || '',
      occurrenceLocation: props.record.occurrenceLocation || props.record.location || '',
      departmentId: props.record.departmentId || props.record.department || '',
      natureId: props.record.natureDefinition || props.record.natureName || props.record.natureId || '',
      rectificationDeadline: props.record.rectificationDeadline || '',
      inspectionTime: props.record.inspectionTime || '',
      problemImageUrls: props.record.problemImageUrls || ''
    })
    
    // 初始化文件列表
    const imageUrls = parseImageUrls(props.record.problemImageUrls)
    if (imageUrls.length > 0) {
      fileList.value = imageUrls.map((url, index) => {
        return {
          uid: Date.now() + index,
          name: `image-${index}.jpg`,
          status: 'success',
          url: url
        }
      })
    } else {
      fileList.value = []
    }
  } else {
    // 新增模式 - 自动设置当前用户部门
    const currentUserDeptId = getCurrentUserDepartment()
    Object.assign(formData, {
      id: '',
      responsiblePerson: '',
      inspector: '',
      responsibleUnit: '',
      location: '',
      problemDescription: '',
      assessmentSituation: '',
      assessmentAmount: 0,
      safetyScore: 0,
      professionId: '',
      occurrenceLocation: '',
      departmentId: currentUserDeptId,
      natureId: '',
      rectificationDeadline: '',
      inspectionTime: '',
      problemImageUrls: ''
    })
    fileList.value = []
  }
}

// 图片预览
const handlePreview = (file) => {
  // DragUpload组件已处理预览功能
}

// 移除图片
const handleRemove = (file, fileList) => {
  // DragUpload组件已处理移除功能
}

// 文件上传成功
const handleUploadSuccess = (response, file, fileList) => {
  // DragUpload组件已处理上传成功逻辑
}



// 重置文件列表
const resetFileList = () => {
  fileList.value = []
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    loading.value = true
    
    // 处理图片URLs - 转换为逗号分隔的字符串
    const imageUrls = fileList.value
      .filter(file => file.response?.data?.filePath || file.url)
      .map(file => {
        const filePath = file.response?.data?.filePath || file.url
        return convertToHttpUrl(filePath)
      })
    
    const submitData = {
      ...formData,
      problemImageUrls: imageUrls.join(',')
    }
    
    // 移除后端不存在的字段
    delete submitData.inspectionTime
    
    // 将natureId映射为natureDefinition
    if (submitData.natureId) {
      submitData.natureDefinition = submitData.natureId
      delete submitData.natureId
    }
    
    // 确保occurrenceLocation映射到location字段
    if (submitData.occurrenceLocation) {
      submitData.location = submitData.occurrenceLocation
      delete submitData.occurrenceLocation
    }
    
    const response = await updateRecord(submitData)
    
    if (response.code === 200) {
      ElMessage.success('更新成功')
      emit('success')
    } else {
      ElMessage.error(response.message || '更新失败')
    }
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    loading.value = false
  }
}

// 加载专业字典数据（与新增弹框 AddDialog 保持一致）
const loadProfessionOptions = async () => {
  try {
    const response = await getDictByType('专业')
    if (response.code === 200 && Array.isArray(response.data)) {
      professionOptions.value = response.data.map(item => ({
        id: item.id,
        name: item.dictValue || item.dictLabel
      }))
    } else {
      professionOptions.value = []
    }
  } catch (error) {
    console.error('加载专业字典失败:', error)
    professionOptions.value = []
  }
}

// 加载性质界定字典数据（与新增弹框 AddDialog 保持一致）
const loadNatureOptions = async () => {
  try {
    const response = await getDictByType('性质界定')
    if (response.code === 200 && Array.isArray(response.data)) {
      natureOptions.value = response.data.map(item => ({
        id: item.id,
        name: item.dictValue || item.dictLabel
      }))
    } else {
      natureOptions.value = []
    }
  } catch (error) {
    console.error('加载性质界定字典失败:', error)
    natureOptions.value = []
  }
}

// 加载部门数据
const loadDepartmentOptions = async () => {
  try {
    const response = await deptApi.getPage({})
    if (response.data) {
      const deptList = response.data.records || response.data
      departmentOptions.value = deptList.map(item => ({
        label: item.deptName,
        value: item.id
      }))
    } else {
      console.warn('部门数据加载失败')
      departmentOptions.value = []
    }
  } catch (error) {
    console.error('加载部门数据失败:', error)
    departmentOptions.value = []
  }
}

// 关闭对话框
const handleClose = () => {
  formRef.value?.resetFields()
  resetFileList()
  dialogVisible.value = false
}
</script>

<style scoped>
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