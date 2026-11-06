<template>
  <el-select
    v-model="selectedValue"
    :placeholder="placeholder"
    :clearable="clearable"
    :disabled="disabled"
    :multiple="multiple"
    :filterable="filterable"
    @change="handleChange"
  >
    <el-option
      v-for="item in options"
      :key="item.dictValue"
      :label="item.dictLabel"
      :value="item.dictValue"
    />
  </el-select>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { dictApi } from '@/api/system'

// Props
const props = defineProps({
  modelValue: {
    type: [String, Array],
    default: ''
  },
  dictType: {
    type: String,
    required: true
  },
  placeholder: {
    type: String,
    default: '请选择'
  },
  clearable: {
    type: Boolean,
    default: true
  },
  disabled: {
    type: Boolean,
    default: false
  },
  multiple: {
    type: Boolean,
    default: false
  },
  filterable: {
    type: Boolean,
    default: false
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'change'])

// 响应式数据
const selectedValue = ref(props.modelValue)
const options = ref([])
const loading = ref(false)

// 监听modelValue变化
watch(() => props.modelValue, (newValue) => {
  selectedValue.value = newValue
})

// 监听dictType变化
watch(() => props.dictType, () => {
  if (props.dictType) {
    loadOptions()
  }
}, { immediate: true })

// 加载字典选项
const loadOptions = async () => {
  if (!props.dictType) return
  
  loading.value = true
  try {
    const response = await dictApi.getByType(props.dictType)
    if (response.data) {
      options.value = response.data
    }
  } catch (error) {
    console.error('加载字典选项失败:', error)
    options.value = []
  } finally {
    loading.value = false
  }
}

// 处理选择变化
const handleChange = (value) => {
  emit('update:modelValue', value)
  emit('change', value)
}

// 组件挂载时加载选项
onMounted(() => {
  if (props.dictType) {
    loadOptions()
  }
})
</script>

<style scoped>
/* 组件样式 */
</style>