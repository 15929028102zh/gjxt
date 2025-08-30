<template>
	<view class="l-picker" :style="[styles]">
		<view class="l-picker__toolbar" v-if="cancelBtn || title || confirmBtn">
			<text class="l-picker__cancel" :style="cancelStyle" v-if="cancelBtn"  @click="onCancel">{{cancelBtn}}</text>
			<text class="l-picker__title"  :style="titleStyle" v-if="title">{{title}}</text>
			<text class="l-picker__confirm" :style="confirmStyle" v-if="confirmBtn" @click="onConfirm">{{confirmBtn}}</text>
		</view>
		<slot name="header"></slot>
		<view class="l-picker__main" :style="[groupHeight ? { height:  groupHeight}: {}]">
			<slot>
				<l-picker-item v-for="(options, i) in columns" :options="options" :key="i" :column="i" :value="pickerValue.length > i ? pickerValue[i]: null"></l-picker-item>
			</slot>
			<view class="l-picker__empty" v-if="isEmpty">
				<slot name="empty"></slot>
			</view>
		</view>
		 <slot name="footer" />
		 <view class="l-picker__loading" ref="loadingRef" v-if="loading" :style="[loadingMaskColor ? {background: loadingMaskColor}: {}]">
			 <l-loading  :size="loadingSize" :color="loadingColor"></l-loading>
		 </view>
	</view>
</template>
<script lang="ts">
	// @ts-nocheck
	/**
	 * Picker 选择器组件
	 * @description 多列数据选择器，支持级联数据展示和自定义样式配置
	 * @tutorial https://ext.dcloud.net.cn/plugin?name=lime-picker
	 * 
	 * @property {string} cancelBtn 取消按钮文字
	 * @property {string | UTSJSONObject} cancelStyle 取消按钮样式
	 * @property {string} confirmBtn 确定按钮文字
	 * @property {string | UTSJSONObject} confirmStyle 确定按钮样式
	 * @property {string} title 标题文字
	 * @property {string | UTSJSONObject} titleStyle 标题样式
	 * @property {UTSJSONObject} keys 字段别名配置（例：{value: 'id', label: 'name'}）
	 * @property {PickerColumn[]} columns 选择器列数据（必填）
	 * @property {PickerValue[]} modelValue 选中值（支持v-model）
	 * @property {PickerValue[]} defaultValue 默认选中值
	 * @property {PickerValue[]} value 选中值（兼容旧版）
	 * @property {boolean} loading 是否显示加载状态
	 * @property {string} loadingColor 加载图标颜色
	 * @property {string} loadingMaskColor 加载遮罩颜色
	 * @property {string} loadingSize 加载图标尺寸
	 * @property {string} itemHeight 选项行高度
	 * @property {string} itemColor 选项文字颜色
	 * @property {string} itemFontSize 选项字体大小
	 * @property {string} itemActiveColor 选中项颜色
	 * @property {string} indicatorStyle 指示器样式
	 * @property {string} bgColor 背景颜色
	 * @property {string} groupHeight 选项组高度
	 * @property {string} radius 圆角半径
	 * @property {boolean} resetIndex 是否重置选中索引
	 * 
	 * @event {Function} confirm 点击确定时触发（事件参数：PickerConfirmEvent）
	 * @event {Function} cancel 点击取消时触发
	 * @event {Function} change 值变化时触发（事件参数：PickerPickEvent）
	 * @event {Function} column-change 列数据变化时触发（事件参数：PickerChangeInfo）
	 */
	import type { PickerProps, PickerColumn, PickerValue, PickerColumnItem, PickerConfirmEvent, PickerPickEvent } from './type';
	import { defineComponent, computed, ref, watch, onMounted, nextTick, onBeforeUnmount, provide, reactive, toRaw } from '@/uni_modules/lime-shared/vue';
	import pickerProps from './props';
	
	export default defineComponent({
		name: 'l-picker',
		props: pickerProps,
		emits: ['change', 'cancel', 'pick','confirm' ,'update:modelValue', 'update:value'],
		setup(props, {emit}) {
			const pickerItemInstanceArray = ref<LPickerItemComponentPublicInstance[]>([]);
			
			const modelValue = ref<PickerValue[]>(props.value || props.modelValue || props.defaultValue || [])
			const pickerValue = computed({
				set(value: PickerValue[]) {
					if(value.join('') == modelValue.value.join('')) return
					modelValue.value = value;
					emit('update:modelValue', value)
					emit('change', value)
					// #ifdef VUE2
					emit('input', value)
					// #endif
				},
				get():PickerValue[] {
					return props.value || props.modelValue || modelValue.value
				}
			} as WritableComputedOptions<PickerValue[]>)
			
			const isEmpty = computed(():boolean => {
				return props.columns.length == 0 && pickerItemInstanceArray.value.every(child => child.options.length == 0)
			})
			const styles = computed(()=>{
				const style:Record<string, any> = {}
				if(props.bgColor) {
					style['background'] = props.bgColor!
				}
				if(props.radius) {
					style['border-top-left-radius'] = props.radius!
					style['border-top-right-radius'] = props.radius!
				}
				return style
			})
			
			const curIndexArray = ref<number[]>([]);
			const curValueArray = ref([...pickerValue.value]);
			const curItemArray:PickerColumnItem[] = []
			const realColumns = computed(():PickerColumn[] => {
				const pickerColumns = pickerItemInstanceArray.value.map((child):PickerColumn => child.options)
				if(pickerColumns.length > 0) {
					return pickerColumns
				}
				return props.columns
			})
			const valueArrayEquals = computed(():boolean => pickerValue.value.join('') == curValueArray.value.join(''))
			
			const manageChildInList = (child: LPickerItemComponentPublicInstance, shouldAdd: boolean) => {
				const index = pickerItemInstanceArray.value.indexOf(child);
				if(shouldAdd) {
					if(index != -1) return
					pickerItemInstanceArray.value.push(child)
				} else {
					if(index == -1) return
					pickerItemInstanceArray.value.splice(index, 1);
				}
			}
			
			const updateItems = (item: PickerColumnItem, index:number, column: number) => { 
				curIndexArray.value[column] = index
				curValueArray.value[column] = item.value
				curItemArray[column] = item;
				
				// clearTimeout(timer)
				// timer = setTimeout(()=>{
				// 	emit('change', [...curValueArray.value])
				// },50)
			};
			
			const updatePickerItems = () => {
				const _indexs : number[] = []
				const _values : any[] = []
				pickerItemInstanceArray.value.forEach((child, column)=>{
					if(child.options.length == 0) return
					const value = curValueArray.value.length > column ? curValueArray.value[column] : null
					// #ifdef VUE3
					const index = value == null ? 0 : child._.exposed.getIndexByValue(value)
					child._.exposed.setIndex(index)
					// #endif
					// #ifdef VUE2
					const index = value == null ? 0 : child.getIndexByValue(value)
					child.setIndex(index)
					// #endif
					const item = child.options[index]
					_indexs.push(index)
					_values.push(item.value)
					
					// curIndexArray.value[column] = index
					// curValueArray.value[column] = item.value
					curItemArray[column] = item
					
					// 不能改变单向数据流, 只有值不存在时候才处理
					// if(pickerValue.value.length == 0) {
					// 	pickerValue.value = [...curValueArray.value]
					// }
					// if(pickerValue.value.join('') == curValueArray.value.join('')) return
					// pickerValue.value = [...curValueArray.value]
					
				})
				if (curIndexArray.value.join('') == _indexs.join('')) return
				curIndexArray.value = _indexs
				curValueArray.value = _values
				// if(pickerValue.value.length == 0) {
				pickerValue.value = [...curValueArray.value]
				// }
			}
			
			const onPick = (item: PickerColumnItem, index:number, column: number) => {
				if( curIndexArray.value[column] == index && 
					curValueArray.value[column] == item.value) return
					
				curIndexArray.value[column] = index
				curValueArray.value[column] = item.value
				curItemArray[column] = item
				const obj:PickerPickEvent = {
					values: curValueArray.value,
					column,
					index
				}
				pickerValue.value = [...curValueArray.value]
				emit('pick', obj)
			};
			
			const onCancel = (e: UniPointerEvent) => {
				updatePickerItems()
				emit('cancel', e)
			}
			const onConfirm = (e: UniPointerEvent) => {
				const values = [...curValueArray.value];
				const indexs = [...curIndexArray.value];
				const items = curItemArray.map((item):PickerColumnItem => toRaw(item))
				if(pickerValue.value.join('') != values.join('')) {
					pickerValue.value = values;
				}
				
				const obj:PickerConfirmEvent = {
					values,
					indexs,
					items
				}
				emit('confirm', obj)
			}
			
			const stopPickerValue = watch(pickerValue, () => {
				nextTick(()=>{
					curValueArray.value = pickerValue.value.map((item: PickerValue) => item);
					updatePickerItems()
				})
			})
			const stopColumns = watch(realColumns, ()=>{
				// nextTick(()=>{
				// 	updatePickerItems()
				// })
				updatePickerItems()
			})
			
			onMounted(()=>{
				nextTick(()=>{
					if(
						!valueArrayEquals.value && 
						pickerValue.value.length > 0) {
						curValueArray.value = [...pickerValue.value]
						updatePickerItems()
					}
				})
			})
			
			onBeforeUnmount(()=> {
				stopPickerValue()
				stopColumns()
			})
			
			provide('limePicker', props)
			provide('limePickerOnPick', onPick)
			provide('limePickerUpdateItems', updateItems)
			provide('limePickerItems', pickerItemInstanceArray)
			provide('limePickerManageChildInList', manageChildInList)
			
			return {
				styles,
				pickerValue,
				isEmpty,
				onCancel,
				onConfirm
			}
			
		}
	})
	
	
	
</script>
<style lang="scss">
	@import './index.scss';
</style>