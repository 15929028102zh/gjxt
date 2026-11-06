<template>
  <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="640px"
    :before-close="handleClose"
    destroy-on-close
  >
    <el-alert
      v-if="record"
      type="warning"
      :closable="false"
      style="margin-bottom: 16px"
      :title="alertTitle"
    />

    <!-- V3:若已构造过 modification_snapshot(从后端读回或前端已生成),展示原值/修改值对比 -->
    <el-descriptions
      v-if="diffFields.length > 0"
      title="原值/修改值对比"
      :column="1"
      border
      size="small"
      style="margin-bottom: 16px"
    >
      <el-descriptions-item
        v-for="field in diffFields"
        :key="field.key"
        :label="field.label"
      >
        <div style="display: flex; align-items: center; gap: 8px; flex-wrap: wrap">
          <el-tag type="danger" size="small">{{ field.from || '(空)' }}</el-tag>
          <el-icon><Right /></el-icon>
          <el-tag type="success" size="small">{{ field.to || '(空)' }}</el-tag>
        </div>
      </el-descriptions-item>
    </el-descriptions>

    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="问题描述" v-if="record">
        <el-text>{{ record.problemDescription || '-' }}</el-text>
      </el-form-item>
      <el-form-item :label="reasonLabel" prop="modifyDescription">
        <el-input
          v-model="form.modifyDescription"
          type="textarea"
          :rows="4"
          placeholder="请说明本次编辑/删除的原因（至少 5 个字）"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">
        提交审核
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Right } from '@element-plus/icons-vue'
import { monthlyRecordApi } from '@/api/monthlyRecord'

const props = defineProps({
  visible: { type: Boolean, default: false },
  record: { type: Object, default: () => ({}) },
  // 0=回退申请，1=编辑申请，2=删除申请。影响弹窗标题文案
  auditType: { type: Number, default: 1 }
})

const emit = defineEmits(['update:visible', 'success'])

const dialogVisible = computed({
  get: () => props.visible,
  set: (v) => emit('update:visible', v)
})

const dialogTitle = computed(() => {
  if (props.auditType === 0) return '提交回退申请'
  if (props.auditType === 2) return '提交删除审核'
  return '提交编辑审核'
})

// 删除申请填的是删除原因，其余填修改原因
const reasonLabel = computed(() => {
  return props.auditType === 2 ? '删除说明' : '修改说明'
})

const formRef = ref()
const submitting = ref(false)

const form = reactive({ modifyDescription: '' })

const rules = {
  modifyDescription: [
    { required: true, message: '请填写修改说明', trigger: 'blur' },
    { min: 5, message: '修改说明至少 5 个字', trigger: 'blur' }
  ]
}

const alertTitle = computed(() => {
  if (props.auditType === 2) return '当前处于公示期,删除记录需要提交删除说明等待审核'
  const op = props.auditType === 0 ? '申请回退' : '编辑'
  return `当前处于公示期,${op}记录需要提交修改说明等待审核`
})

// 字段名 → 中文标签
const FIELD_LABELS = {
  responsiblePerson: '责任人',
  responsibleUnit: '责任单位',
  location: '发现位置',
  problemDescription: '问题描述',
  qualityId: '性质界定',
  qualityName: '性质界定',
  departmentId: '受检单位',
  departmentName: '受检单位',
  checkUnitId: '检查单位',
  checkUnitName: '检查单位',
  rectificationDate: '整改期限',
  inspector: '检查人'
}

// 从 record.modification_snapshot 解析原值/修改值对比
const diffFields = computed(() => {
  const raw = props.record?.modificationSnapshot
  if (!raw) return []
  let parsed
  try {
    parsed = typeof raw === 'string' ? JSON.parse(raw) : raw
  } catch (e) {
    return []
  }
  if (!parsed || typeof parsed !== 'object') return []
  return Object.keys(parsed).map(k => ({
    key: k,
    label: FIELD_LABELS[k] || k,
    from: parsed[k]?.from,
    to: parsed[k]?.to
  }))
})

watch(() => props.visible, (val) => {
  if (val) {
    form.modifyDescription = ''
  }
})

async function handleSubmit() {
  if (!formRef.value || !props.record?.id) return
  try {
    await formRef.value.validate()
    submitting.value = true
    const response = await monthlyRecordApi.submitEditAudit(props.record.id, {
      modifyDescription: form.modifyDescription,
      auditType: props.auditType
    })
    if (response.code === 200) {
      ElMessage.success(
        props.auditType === 0 ? '已提交回退申请'
          : props.auditType === 2 ? '已提交删除审核'
            : '已提交编辑审核'
      )
      emit('success')
      dialogVisible.value = false
    } else {
      ElMessage.error(response.message || '提交失败')
    }
  } catch (err) {
    if (err?.message) ElMessage.error(err.message)
    else console.error('提交审核申请失败', err)
  } finally {
    submitting.value = false
  }
}

function handleClose() {
  dialogVisible.value = false
}
</script>
