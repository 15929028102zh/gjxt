<template>
	<picker-view
		class="l-picker-item__group" 
		:style="{opacity: options.length > 0 ? 1 : 0}"
		:indicator-style="indicatorStyles"
		:value="innerIndex"
		@change="handlePick"
		indicator-class="l-picker-item__indicator">
		<picker-view-column class="l-picker-item__wrapper">
			<text
				class="l-picker-item__group-item" 
				v-for="(item, i) in options" 
				:style="[itemStyles, curIndex == i ? itemActiveStyles: {}]"
				:class="{'l-picker-item__group-item--active': curIndex == i}" 
				:key="item.value">{{item.label}}</text>
		</picker-view-column>
	</picker-view>
</template>
<script lang="ts">
	// @ts-nocheck
	/**
	 * PickerItem 选择器子项组件
	 * @description 用于构建多列选择器的单个列项，通常作为 Picker 组件的子组件使用
	 * <br>插件类型：LPickerItemComponentPublicInstance 
	 * @tutorial https://ext.dcloud.net.cn/plugin?name=lime-picker
	 * 
	 * @property {PickerColumnItem[]} options 当前列的选项列表（必填）
	 * @property {PickerValue} value 当前选中值
	 * @property {number} column 列索引标识（从0开始计数）
	 * @property {string | number} name 列名称标识符
	 */
	import { defineComponent, getCurrentInstance, inject, ref, computed, watch, onBeforeUnmount } from '@/uni_modules/lime-shared/vue';
	import { unitConvert } from '@/uni_modules/lime-shared/unitConvert';
	import { clamp } from '@/uni_modules/lime-shared/clamp'
	import type { PickerItemProps, ManageChildInList, OnPick, UpdateItems } from './type';
	import type { PickerColumnItem, PickerValue } from '../l-picker/type';
	
	import pickerItemProps from './props';
	
	
	export default defineComponent({
		name: 'l-picker-item',
		props: pickerItemProps,
		setup(props, {expose}) {
			const instance = getCurrentInstance()!;
			const picker = inject<LPickerComponentPublicInstance|null>('limePicker', null);
			const pickerItemInstanceArray = inject<Ref<LPickerItemComponentPublicInstance[]>|null>('limePickerItems', null);
			const manageChildInList = inject<ManageChildInList|null>('limePickerManageChildInList', null);
			manageChildInList?.(instance.proxy! as LPickerItemComponentPublicInstance , true)
			
			const onPick = inject<OnPick|null>('limePickerOnPick', null);
			const updateItems = inject<UpdateItems|null>('limePickerUpdateItems', null);
			
			const curIndex = ref(-1)
			const curValue = ref<PickerValue|null>(null);
			const column = computed(() : number => props.column != -1 ? props.column : pickerItemInstanceArray?.value.indexOf(instance.proxy! as LPickerItemComponentPublicInstance) ?? props.column);
			
			const elementPosition = computed(() : boolean[] => {
				const totalElements = pickerItemInstanceArray?.value.length || 0;
				return [column.value == 0, column.value == totalElements - 1]
			});
			const innerIndex = computed(():number[] => [curIndex.value])
			
			const indicatorStyles = computed(() : string => {
				const [isFirst, isLast] = elementPosition.value
				let style = ``
				
				if(isFirst) {
					style+= 'border-top-left-radius:12rpx; border-bottom-left-radius:12rpx;'
				}
				if(isLast) {
					style+= 'border-top-right-radius:12rpx; border-bottom-right-radius:12rpx;'
				}
				return `
					${style}
					height: ${picker?.itemHeight || '50px'};
					background-color: rgba(0, 0, 0, 0.04); ${picker?.indicatorStyle}`
			})
			const itemStyles = computed(()=>{
				const style:Record<string, any> = {};
				if(picker?.itemColor) {
					style['color'] = picker.itemColor!
				}
				if(picker?.itemFontSize) {
					style['font-size'] = picker.itemFontSize!
				}
				return style
			})
			
			const itemActiveStyles = computed(() =>{
				const style:Record<string, any> = {};
				if(picker?.itemActiveColor) {
					style['color'] = picker.itemActiveColor!
				}
				if (picker?.itemActiveFontWeight) {
					style['font-weight'] = picker.itemActiveFontWeight!
				}
				return style
			})
			
			const getIndexByValue = (val: PickerValue | null) => {
			    let defaultIndex = 0;
			    if (val != null) {
			        defaultIndex = props.options.findIndex((item) => item.value == val);
			    }
			    return defaultIndex < 0 ? 0 : defaultIndex;
			};
			
			const setIndex = (index: number) =>{
				let lastIndex = curIndex.value
				let _index = clamp(index, 0,  props.options.length - 1)
				if(props.options.length > _index) {
					curIndex.value = _index;
					curValue.value = props.options[_index].value
				}
				// #ifdef WEB
				if(lastIndex == _index || lastIndex == -1) return
				if(instance.proxy!.$el) {
					const childs = Array.from(instance.proxy!.$el.parentElement.children).slice(column.value + 1);
					childs.forEach((child) => {
						(child as HTMLElement).style.setProperty('--picker-duration', '300ms');
						setTimeout(function() {
							(child as HTMLElement).style.setProperty('--picker-duration', '0ms');
						}, 299);
					})
				}
				// #endif
			}
			
			const setValue = (value: PickerValue|null) =>{
				if(value == curValue.value) return
				curValue.value = value
				const index = getIndexByValue(value)
				setIndex(index)
			}
			const setOptions = () => {}
			const setUpdateItems = () => {
				const curItem = props.options.length > curIndex.value ? props.options[curIndex.value] : null;
				if(curItem == null) return
				updateItems?.(curItem, curIndex.value, column.value);
			}
			
			const handlePick = (e: UniPickerViewChangeEvent) => {
				if(props.options.length == 0) return
				const index = clamp(e.detail.value[0], 0,  props.options.length - 1);
				const curItem = props.options[index];
				if(index == curIndex.value) return
				setIndex(index)
				onPick?.(curItem, index, column.value);
			}
			
			
			// const stop = watch(():PickerColumnItem[] => props.options, ()=> {
			// 	if(JSON.stringify(o) == JSON.stringify(v)) return
			// 	const index = (picker?.resetIndex || false) ? 0 : Math.max( Math.min(props.options.length - 1, curIndex.value), 0)
			// 	const curItem = props.options[index];
			// 	setIndex(index)
			// 	setUpdateItems();
			// 	// nextTick(()=>{
			// 	// 	onPick?.(curItem, index, column.value);
			// 	// })
			// }) // ,{immediate: true}
			
			const stopValue = watch(():PickerValue|null => props.value, (v : PickerValue|null)=>{
				setValue(v);
				setUpdateItems();
			},{immediate: true})
			
			onBeforeUnmount(()=>{
				manageChildInList?.(instance.proxy! as LPickerItemComponentPublicInstance, false)
				// stop()
				stopValue()
			})
			
			// #ifdef VUE3
			expose({
				setIndex,
				setValue,
				// setOptions,
				// setUpdateItems,
				getIndexByValue
			})
			// #endif
			
			return {
				indicatorStyles,
				innerIndex,
				curIndex,
				handlePick,
				itemStyles,
				itemActiveStyles,
				// #ifdef VUE2
				setIndex,
				setValue,
				getIndexByValue
				// #endif
			}
		}
	})
	
</script>
<style lang="scss">
	@import './index';
</style>