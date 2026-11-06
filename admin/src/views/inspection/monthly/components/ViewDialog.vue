<template>
  <el-dialog
    v-model="dialogVisible"
    title="月检记录详情"
    width="80%"
    :before-close="handleClose"
    destroy-on-close
  >
    <div v-loading="loading">
      <!-- 基本信息（与月检 AddDialog 字段统一） -->
      <div class="info-section">
        <h4>问题信息</h4>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="问题描述">
            {{ formData.problemDescription || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="专业类别">
            {{ formData.professionalTypeName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="基本模块">
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
          <el-descriptions-item label="性质界定是否人工修改">
            <el-tag :type="formData.natureModified === 1 ? 'warning' : 'info'" size="small">
              {{ formData.natureModified === 1 ? '是(已修改)' : '否(未修改)' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="扣分值">
            {{ deductionScoreDisplay || '选择性质界定后自动显示' }}
          </el-descriptions-item>
          <el-descriptions-item label="发现位置">
            {{ formData.location || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查单位">
            {{ formData.checkUnitName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="受检单位">
            {{ formData.departmentName || formData.departmentId || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="检查人">
            {{ formData.inspector || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="责任人">
            {{ formData.responsiblePerson || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="责任单位">
            {{ formData.responsibleUnit || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="整改期限">
            {{ formatDate(formData.rectificationDeadline || formData.rectificationDate) }}
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
      <div class="info-section" v-if="formData.recordStatus >= 1">
        <h4>整改信息</h4>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="整改人">
            {{ formData.rectifierName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="整改建议">
            {{ formData.rectificationSuggestion || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="整改效果">
            {{ formData.rectificationEffect || '暂无' }}
          </el-descriptions-item>
        </el-descriptions>

        <div style="margin-top: 12px;" v-if="rectificationImages.length > 0">
          <h5>整改图片：</h5>
          <div class="image-preview">
            <el-image
              v-for="(image, index) in rectificationImages"
              :key="index"
              :src="image"
              :preview-src-list="rectificationImages"
              style="width: 120px; height: 120px; margin-right: 8px; border-radius: 4px;"
              fit="cover"
            />
          </div>
        </div>
      </div>

      <!-- 复查信息 -->
      <div class="info-section" v-if="formData.recordStatus >= 2">
        <h4>复查信息</h4>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="复查人">
            {{ formData.reviewerName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="复查建议">
            {{ formData.reviewSuggestion || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="复查效果">
            {{ formData.reviewEffect || '暂无' }}
          </el-descriptions-item>
        </el-descriptions>

        <div style="margin-top: 12px;" v-if="reviewImages.length > 0">
          <h5>复查图片：</h5>
          <div class="image-preview">
            <el-image
              v-for="(image, index) in reviewImages"
              :key="index"
              :src="image"
              :preview-src-list="reviewImages"
              style="width: 120px; height: 120px; margin-right: 8px; border-radius: 4px;"
              fit="cover"
            />
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

const loading = ref(false)
const problemImages = ref([])
const rectificationImages = ref([])
const reviewImages = ref([])

const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

const deductionScore = computed(() => {
  const name = props.formData?.qualityName
  if (name === '观察') return 1
  if (name === '一般') return 3
  if (name === '重要') return 5
  return null
})

const deductionScoreDisplay = computed(() => {
  const score = deductionScore.value
  return score === null ? '' : `${score}`
})

watch(() => props.formData, (newData) => {
  if (newData && Object.keys(newData).length > 0) {
    problemImages.value = parseImageUrls(newData.photos)
    rectificationImages.value = parseImageUrls(newData.rectificationImages)
    reviewImages.value = parseImageUrls(newData.reviewImages)
  }
}, { immediate: true, deep: true })

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

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