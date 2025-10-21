<template>
  <el-dialog
    v-model="dialogVisible"
    title="月检记录详情"
    width="80%"
    :before-close="handleClose"
    destroy-on-close
  >
    <div v-loading="loading">
      <!-- 基本信息 -->
      <div class="info-section">
        <h4>基本信息</h4>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="任务编号">
            {{ formData.taskNo || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="月检时间">
            {{ formatDate(formData.inspectionTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="检查部门">
            {{ formData.departmentName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查专业">
            {{ formData.professionalTypeName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查组长">
            {{ formData.groupLeaderName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查副组长">
            {{ formData.deputyLeaderName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="专业类别">
            {{ formData.professionalTypeName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="模块">
            {{ formData.moduleName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="责任要素">
            {{ formData.responsibilityName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查内容">
            {{ formData.issueName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="性质界定">
            {{ formData.qualityName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="安全记分">
            {{ formData.safetyScore || 0 }}
          </el-descriptions-item>
          <el-descriptions-item label="问题描述">
            {{ formData.problemDescription || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="发现位置">
            {{ formData.location || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查人">
            {{ formData.inspector || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="整改期限">
            {{ formatDate(formData.rectificationDeadline) }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(formData.recordStatus)">
              {{ getStatusText(formData.recordStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDate(formData.createTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 问题图片 -->
      <div class="info-section">
        <h4>问题图片</h4>
        <div v-if="problemImages.length > 0" class="image-preview">
          <el-image
            v-for="(image, index) in problemImages"
            :key="index"
            :src="image"
            :preview-src-list="problemImages"
            style="width: 120px; height: 120px; margin-right: 8px; border-radius: 4px;"
            fit="cover"
          />
        </div>
        <div v-else class="no-image">
          <el-empty description="暂无问题图片" :image-size="80" />
        </div>
      </div>

      <!-- 整改信息 -->
      <div class="info-section" v-if="formData.status >= 1">
        <h4>整改信息</h4>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="整改效果描述">
            {{ formData.rectificationEffect || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="整改时间">
            {{ formatDate(formData.rectificationTime) }}
          </el-descriptions-item>
        </el-descriptions>
        
        <!-- 整改图片 -->
        <div style="margin-top: 16px;">
          <h5>整改图片：</h5>
          <div v-if="rectificationImages.length > 0" class="image-preview">
            <el-image
              v-for="(image, index) in rectificationImages"
              :key="index"
              :src="image"
              :preview-src-list="rectificationImages"
              style="width: 120px; height: 120px; margin-right: 8px; border-radius: 4px;"
              fit="cover"
            />
          </div>
          <div v-else class="no-image">
            <el-empty description="暂无整改图片" :image-size="60" />
          </div>
        </div>
      </div>

      <!-- 复查信息 -->
      <div class="info-section" v-if="formData.status >= 2">
        <h4>复查信息</h4>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="复查时间">
            {{ formatDate(formData.reviewTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="复查备注">
            {{ formData.reviewRemark || '暂无' }}
          </el-descriptions-item>
        </el-descriptions>
        
        <!-- 复查图片 -->
        <div style="margin-top: 16px;">
          <h5>复查图片：</h5>
          <div v-if="reviewImages.length > 0" class="image-preview">
            <el-image
              v-for="(image, index) in reviewImages"
              :key="index"
              :src="image"
              :preview-src-list="reviewImages"
              style="width: 120px; height: 120px; margin-right: 8px; border-radius: 4px;"
              fit="cover"
            />
          </div>
          <div v-else class="no-image">
            <el-empty description="暂无复查图片" :image-size="60" />
          </div>
        </div>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { parseImageUrls } from '@/config/api'

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

const emit = defineEmits(['update:visible'])

// 响应式数据
const loading = ref(false)
const problemImages = ref([])
const rectificationImages = ref([])
const reviewImages = ref([])

// 弹窗显示状态
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

// 监听表单数据变化，处理图片数据
watch(() => props.formData, (newData) => {
  if (newData && Object.keys(newData).length > 0) {
    // 处理问题图片 - 使用全局统一图片解析方法
    problemImages.value = parseImageUrls(newData.photos)
    
    // 处理整改图片 - 使用全局统一图片解析方法
    rectificationImages.value = parseImageUrls(newData.rectificationImages)
    
    // 处理复查图片 - 使用全局统一图片解析方法
    reviewImages.value = parseImageUrls(newData.reviewImages)
  }
}, { immediate: true, deep: true })

// 获取状态类型
const getStatusType = (status) => {
  const statusMap = {
    0: 'warning',
    1: 'success',
    2: 'info'
  }
  return statusMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待整改',
    1: '已整改',
    2: '已复查'
  }
  return statusMap[status] || '未知'
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

// 关闭弹窗
const handleClose = () => {
  problemImages.value = []
  rectificationImages.value = []
  reviewImages.value = []
  emit('update:visible', false)
}
</script>

<style scoped>
.info-section {
  margin-bottom: 24px;
}

.info-section h4 {
  margin-top: 0;
  margin-bottom: 16px;
  color: #303133;
  border-bottom: 2px solid #e4e7ed;
  padding-bottom: 8px;
}

.info-section h5 {
  margin: 8px 0;
  color: #606266;
  font-size: 14px;
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.no-image {
  padding: 20px;
  text-align: center;
  background: #f5f7fa;
  border-radius: 8px;
  border: 1px dashed #e4e7ed;
}

.dialog-footer {
  text-align: right;
}
</style>