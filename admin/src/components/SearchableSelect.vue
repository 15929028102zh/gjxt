<template>
  <el-tooltip
    :content="selectedFullText"
    placement="top"
    effect="dark"
    :disabled="!longText || !selectedFullText"
    popper-class="searchable-select-tooltip"
  >
    <el-select
      v-model="model"
      :placeholder="placeholder"
      :clearable="clearable"
      :multiple="multiple"
      :collapse-tags="collapseTags"
      :collapse-tags-tooltip="collapseTagsTooltip"
      :filterable="shouldFilter"
      :filter-method="shouldFilter ? handleFilter : undefined"
      :remote="shouldFilter"
      :remote-method="shouldFilter ? handleFilter : undefined"
      :popper-class="popperClass"
      :style="style"
      @change="onChange"
    >
      <template v-if="shouldFilter" #prefix>
        <el-icon><Search /></el-icon>
      </template>
      <el-option
        v-for="item in displayedOptions"
        :key="item[valueKey]"
        :label="item[labelKey]"
        :value="item[valueKey]"
      >
        <span class="searchable-option-text" :title="longText ? String(item[labelKey] ?? '') : null">
          {{ item[labelKey] }}
        </span>
      </el-option>
      <template v-if="shouldFilter && displayedOptions.length === 0">
        <div class="searchable-empty">
          <span>无匹配结果</span>
        </div>
      </template>
    </el-select>
  </el-tooltip>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { Search } from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: { type: [String, Number, Array, null], default: null },
  options: { type: Array, default: () => [] },
  valueKey: { type: String, default: 'id' },
  labelKey: { type: String, default: 'name' },
  placeholder: { type: String, default: '请选择' },
  clearable: { type: Boolean, default: true },
  multiple: { type: Boolean, default: false },
  collapseTags: { type: Boolean, default: false },
  collapseTagsTooltip: { type: Boolean, default: false },
  // 当选项数超过该阈值时启用搜索（默认 5）
  searchThreshold: { type: Number, default: 5 },
  // 选项文本很长时启用：下拉面板换行完整显示，选中值悬浮显示全文
  longText: { type: Boolean, default: false },
  style: { type: [String, Object], default: '' }
})

const emit = defineEmits(['update:modelValue', 'change'])

const innerOptions = ref([...(props.options || [])])
const filterKeyword = ref('')

const model = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v)
})

watch(() => props.options, (val) => {
  innerOptions.value = Array.isArray(val) ? [...val] : []
  filterKeyword.value = ''
}, { immediate: true })

const shouldFilter = computed(() => innerOptions.value.length > props.searchThreshold)

const displayedOptions = computed(() => {
  if (!shouldFilter.value) return innerOptions.value
  const kw = (filterKeyword.value || '').trim().toLowerCase()
  if (!kw) return innerOptions.value
  return innerOptions.value.filter(item => {
    const label = String(item[props.labelKey] ?? '').toLowerCase()
    const value = String(item[props.valueKey] ?? '').toLowerCase()
    return label.includes(kw) || value.includes(kw)
  })
})

function handleFilter(keyword) {
  filterKeyword.value = keyword || ''
}

// 长文本模式下，当前选中项的完整文案（供 tooltip 展示）
const selectedFullText = computed(() => {
  if (!props.longText) return ''
  const v = props.modelValue
  if (v === null || v === undefined || v === '') return ''
  const values = Array.isArray(v) ? v : [v]
  for (const val of values) {
    const item = innerOptions.value.find(o => o[props.valueKey] === val)
    if (item) return String(item[props.labelKey] ?? '')
  }
  return ''
})

const popperClass = computed(() => (
  props.longText
    ? 'searchable-select-popper searchable-select-popper--long'
    : 'searchable-select-popper'
))

function onChange(val) {
  emit('change', val)
}
</script>

<style>
.searchable-select-popper .el-select-dropdown__empty {
  padding: 8px;
}
.searchable-empty {
  padding: 8px 0;
  color: #909399;
  text-align: center;
  font-size: 13px;
}

/* 长文本模式：下拉选项换行完整显示，不再单行截断 */
.searchable-select-popper--long .el-select-dropdown__item {
  height: auto;
  line-height: 1.6;
  white-space: normal;
  word-break: break-word;
  overflow: visible;
  text-overflow: clip;
  padding: 8px 32px 8px 20px;
}
.searchable-select-popper--long .el-select-dropdown__wrap {
  max-height: 380px;
}

/* 选中值悬浮提示：限制宽度并换行，否则长文本会撑成一行 */
.searchable-select-tooltip {
  max-width: 520px;
  white-space: normal;
  word-break: break-word;
  line-height: 1.6;
}
</style>
