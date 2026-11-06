<template>
  <!--
    月检提交页面的编辑弹框：
    字段、UI、提交流程完全对齐 inspection/monthly/components/AddDialog.vue，
    内部直接复用 AddDialog（新增/编辑两态自适应）。
    这里把外层传入的 record 直接透传给 AddDialog 作为 formData：
    这样在 AddDialog 自己的 watch 触发时,props.formData 一定已是最新记录,
    避免了"watch 调度顺序导致读到空 formData"的竞态(图片/字段全部丢)。
  -->
  <AddDialog
    v-model:visible="innerVisible"
    :form-data="record"
    :is-in-inspection-period="true"
    @success="onSuccess"
  />
</template>

<script setup>
import { computed } from 'vue'
import AddDialog from '@/views/inspection/monthly/components/AddDialog.vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  record: { type: Object, default: () => ({}) }
})

const emit = defineEmits(['update:modelValue', 'success'])

const innerVisible = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v)
})

const onSuccess = () => {
  emit('success')
}
</script>