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
        <el-descriptions-item label="任务编号">
          {{ detailData.taskNo || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="月检时间">
          {{ formatDate(detailData.inspectionTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="检查部门">
          {{ detailData.departmentName || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="检查专业">
          {{ detailData.professionalTypeName || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="检查组长">
          {{ detailData.groupLeaderName || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="检查副组长">
          {{ detailData.deputyLeaderName || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="责任人">
          {{ detailData.responsiblePerson || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="责任单位">
          {{ detailData.responsibleUnit || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="责任要素">
          {{ detailData.responsibilityName || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="检查内容">
          {{ detailData.issueName || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="性质界定">
          {{ detailData.qualityName || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="安全记分">
          {{ detailData.safetyScore || 0 }}
        </el-descriptions-item>
        <el-descriptions-item label="问题描述">
          {{ detailData.problemDescription || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="发现位置">
          {{ detailData.location || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="检查人">
          {{ detailData.inspector || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="整改期限">
          {{ formatDate(getRectificationDeadline()) }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detailData.recordStatus)">
            {{ getStatusText(detailData.recordStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDate(detailData.createTime) }}
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
    <div class="rectification-info">
      <h4>整改情况</h4>
      <!-- 仅显示整改效果描述 -->
      <el-form
        :model="form"
        label-width="120px"
        disabled
      >
        <el-row :gutter="20">
          <!-- 整改效果描述 -->
          <el-col :span="24">
            <el-form-item label="整改效果描述">
              <el-input
                v-model="form.rectificationEffect"
                type="textarea"
                :rows="3"
                readonly
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      
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
          <!-- 复查效果 -->
          <el-col :span="24">
            <el-form-item label="复查效果" prop="reviewSuggestion">
              <el-input
                v-model="form.reviewSuggestion"
                type="textarea"
                :rows="4"
                placeholder="请描述复查情况..."
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="复查图片">
              <DragUpload
                v-model="imageFileList"
                :limit="3"
                :max-size="2"
                accept="image/jpeg,image/png"
                list-type="picture-card"
                primary-text="将图片拖到此处，或点击上传"
                secondary-text="支持拖拽上传"
                tip-text="只能上传jpg/png文件，且不超过2MB，最多3张"
                @success="handleImageSuccess"
                @error="handleImageError"
                @remove="handleImageRemove"
              />
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
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import DragUpload from '@/components/DragUpload.vue'
import { monthlyRecordApi } from '@/api/monthlyRecord'
import { parseImageUrls, convertToHttpUrl } from '@/config/api'

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
const detailData = reactive({})
const rectificationImages = ref([])

// 弹窗显示状态
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

// 表单数据
const form = reactive({
  id: '',
  natureDefinition: '',
  professionId: null,
  professionName: '',
  departmentId: null,
  departmentName: '',
  rectificationSuggestion: '',
  rectificationEffect: '',
  reviewSuggestion: '',
  reviewEffect: '',
  rectificationImages: [],
  reviewImages: []
})

// 表单验证规则
const rules = {
  reviewSuggestion: [
    { required: true, message: '请填写复查效果', trigger: 'blur' }
  ]
}

// 计算属性
const problemImages = computed(() => {
  return parseImageUrls(detailData.photos)
})

const rectImagesComputed = computed(() => {
  return parseImageUrls(detailData.rectificationImages)
})

// 监听弹窗显示状态
watch(() => props.visible, async (newVal) => {
  if (newVal) {
    resetForm()
    // 调用接口获取记录详情
    if (props.formData && props.formData.id) {
      await loadRecordDetail(props.formData.id)
    }
  }
})

// 加载记录详情
const loadRecordDetail = async (id) => {
  try {
    const response = await monthlyRecordApi.getById(id)
  if (response.code === 200 && response.data) {
    const data = response.data
    // 回写数据到表单
    Object.assign(detailData, data)
    form.id = data.id
    form.natureDefinition = data.natureDefinition || ''
    form.professionId = data.professionId
    form.departmentId = data.departmentId
    form.rectificationSuggestion = data.rectificationSuggestion || data.suggestion || ''
    form.rectificationEffect = data.rectificationEffect || data.rectificationSuggestion || data.suggestion || data.rectifyEffect || data.rectification_result || ''
    form.reviewEffect = data.reviewEffect || ''
      
      // 设置专业和部门的显示名称
      form.professionName = data.professionName || ''
      form.departmentName = data.departmentName || ''
      
    // 处理整改图片（统一解析，兼容别名）
    const rectImgs = data.rectificationImages || data.rectifyImages || data.rectificationImageUrls || ''
    rectificationImages.value = parseImageUrls(rectImgs)
      
      // 处理复查图片
      if (data.reviewImages) {
        if (typeof data.reviewImages === 'string') {
          form.reviewImages = data.reviewImages.split(',').filter(url => url.trim())
        } else if (Array.isArray(data.reviewImages)) {
          form.reviewImages = [...data.reviewImages]
        }
      }
    }
  } catch (error) {
    console.error('获取记录详情失败:', error)
    ElMessage.error('获取记录详情失败')
  }
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    id: '',
    natureDefinition: '',
    professionId: null,
    professionName: '',
    departmentId: null,
    departmentName: '',
    rectificationSuggestion: '',
    rectificationEffect: '',
    reviewSuggestion: '',
    rectificationImages: [],
    reviewImages: []
  })
  Object.keys(detailData).forEach(k => delete detailData[k])
  rectificationImages.value = []
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const getStatusType = (status) => {
  const map = { 0: 'warning', 1: 'success', 2: 'info' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { 0: '待整改', 1: '已整改', 2: '已复查' }
  return map[status] || '未知'
}

const getRectificationDeadline = () => {
  const d = detailData || {}
  return d.rectificationDeadline || d.rectificationDate || ''
}

// 图片上传成功

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

  const submitData = {
    reviewSuggestion: form.reviewSuggestion,
    reviewImages: (form.reviewImages || []).join(',')
  }

    const response = await monthlyRecordApi.confirmReview(form.id, submitData)
    
    if (response.code === 200) {
      ElMessage.success('复查确认成功')
      emit('success')
    } else {
      ElMessage.error(response.message || '复查确认失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitLoading.value = false
  }
}

const handleImageSuccess = (response, file) => {
  try {
    const url = response?.url || response?.data?.url || file?.url
    if (url) {
      form.reviewImages.push(url)
    }
  } catch (_) {}
}

const handleImageError = (error) => {
  console.error('图片上传失败:', error)
  ElMessage.error('图片上传失败')
}

const handleImageRemove = (file) => {
  const idx = form.reviewImages.findIndex(u => u === file?.url)
  if (idx > -1) form.reviewImages.splice(idx, 1)
}
</script>

<style scoped>
.problem-info {
  margin-bottom: 24px;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.problem-info h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.problem-info h5 {
  margin: 16px 0 8px 0;
  color: #606266;
  font-size: 14px;
  font-weight: 500;
}

.rectification-info {
  margin-bottom: 24px;
  padding: 16px;
  background-color: #f0f9ff;
  border-radius: 8px;
}

.rectification-info h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.rectification-info h5 {
  margin: 16px 0 8px 0;
  color: #606266;
  font-size: 14px;
  font-weight: 500;
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.review-form {
  margin-bottom: 24px;
}

.review-form h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-descriptions__label) {
  font-weight: 500;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-textarea__inner) {
  resize: vertical;
}
</style>
