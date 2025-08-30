// @ts-nocheck
import {PickerColumn, PickerColumnItem ,PickerValue} from '../l-picker/type';
export type KeysType = {
  value: string;
  label: string;
  children:string;
  disabled?: string;
}
export interface CascadeProps {
	/**
	  * 取消按钮文字
	  */
	cancelBtn ?: string;
	cancelStyle ?: string;
	/**
	  * 确定按钮文字
	  */
	confirmBtn ?: string;
	confirmStyle ?: string;
	/**
	  * 标题
	  */
	title ?: string;
	titleStyle ?: string;
	/**
	 * 用来定义 value / label 在 `options` 中对应的字段别名
	 */
	keys?: UTSJSONObject;
	/**
	 * 配置每一列的选项
	 */
	columns : UTSJSONObject[];
	/**
	  * 选中值
	  */
	modelValue ?: PickerValue[];
	defaultValue ?: PickerValue[];
	value ?: PickerValue[];
	/**
	 * 是否为加载状态
	 */
	loading: boolean;
	loadingColor?: string;
	loadingMaskColor ?: string;
	loadingSize: string;
	
	itemHeight?: string;
	itemColor?: string;
	itemFontSize?: string;
	itemActiveColor?: string;
	
	indicatorStyle?: string;
	bgColor?: string;
	groupHeight?: string;
	radius?: string;
	resetIndex: boolean 
}