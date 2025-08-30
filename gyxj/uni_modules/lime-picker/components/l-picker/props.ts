// @ts-nocheck
export default {
	/**
	  * 取消按钮文字
	  */
	cancelBtn: {
		type: String,
		default: null
	},
	cancelStyle: {
		type:[String, Object],
		default: null
	},
	/**
	  * 确定按钮文字
	  */
	confirmBtn: {
		type: String,
		default: null
	},
	confirmStyle: {
		type: [String, Object],
		default: null
	},
	keys: {
		type: Object,
		default: null
	},
	/**
	  * 标题
	  */
	title: {
		type: String,
		default: null
	},
	titleStyle: {
		type: [String, Object],
		default: null
	},
	/**
	 * 配置每一列的选项
	 */
	columns: {
		type: Array,
		default: () => []
	},
	/**
	  * 选中值
	  */
	modelValue: {
		type: Array,
		default: null
	},
	defaultValue: {
		type: Array,
		default: null
	},
	value: {
		type: Array,
		default: null
	},
	/**
	 * 是否为加载状态
	 */
	loading: {
		type: Boolean,
		default: false
	},
	loadingColor: {
		type: String,
		default: null
	},
	loadingMaskColor: {
		type: String,
		default: null
	},
	loadingSize: {
		type: String,
		default: '64rpx'
	},
	itemHeight: {
		type: String,
		default: ''
	},
	itemColor: {
		type: String,
		default: ''
	},
	itemFontSize: {
		type: String,
		default: ''
	},
	itemActiveColor: {
		type: String,
		default: ''
	},

	indicatorStyle: {
		type: String,
		default: ''
	},
	bgColor:{
		type: String,
		default: null
	},
	groupHeight:{
		type: String,
		default: ''
	},
	radius:{
		type: String,
		default: null
	},
	/**
	 * 列表更新后，是否归0
	 */
	resetIndex: {
		type: Boolean,
		default: false
	}
}