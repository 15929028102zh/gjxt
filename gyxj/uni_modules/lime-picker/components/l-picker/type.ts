// @ts-nocheck
export type PickerValue = any;//string | number;
export type PickerColumnItem = {
	id: any|null;
	label : string;
	disabled: boolean | null;
	value : string;//string | number;
	children : PickerColumn | null
}
export type PickerColumn = PickerColumnItem[];
export type PickerPickEvent= {
	values: PickerValue[];
	column : number;
	index : number;
}
export type PickerConfirmEvent = {
	values: PickerValue[]
	indexs: number[]
	items: PickerColumnItem[]
}
/**
 * 定义比较数组时返回的变化对象类型。
 */
export type PickerChangeInfo = {
    column: number;           // 变化的列索引
    direction: 1 | -1 | 0;         // 变化方向：1 表示增加，-1 表示减少, 0表示无变化
    index: number; // 变化后的新值,在列表中表示下标
}


export interface LPickerComponentPublicInstance {
	itemHeight?: string;
	itemColor?: string;
	itemFontSize?: string;
	itemActiveColor?: string;
	itemActiveFontWeight?: number;
}

export interface PickerProps {
	/**
	  * 取消按钮文字
	  */
	cancelBtn ?: string;
	cancelStyle ?: string | UTSJSONObject;
	/**
	  * 确定按钮文字
	  */
	confirmBtn ?: string;
	confirmStyle ?: string | UTSJSONObject;
	/**
	  * 标题
	  */
	title ?: string;
	titleStyle ?: string | UTSJSONObject;
	/**
	 * 用来定义 value / label 在 `options` 中对应的字段别名
	 */
	keys?: UTSJSONObject;
	/**
	 * 配置每一列的选项
	 */
	columns : PickerColumn[];
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
	loadingMaskColor?: string;
	loadingSize: string;
	
	itemHeight?: string;
	itemColor?: string;
	itemFontSize?: string;
	itemActiveColor?: string;
	itemActiveFontWeight?: number;
	
	indicatorStyle?: string;
	bgColor?:string;
	groupHeight?:string;
	radius?:string;
	resetIndex: boolean 
}