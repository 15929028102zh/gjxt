// 完整类型定义
export interface LoadingProps {
  color?: string;
  type: 'circular' | 'spinner' | 'failed';
  // #ifndef APP
  size?: string;
  // #endif
  // #ifdef APP
  size: string;
  // #endif
  text?: string;
  textColor?: string;
  textSize?: string;
  mode: 'raf' | 'animate';
  vertical: boolean;
  animated: boolean;
}

// defineOptions({
	// 	name: 'l-loading'
	// })
	// const props = defineProps({
	// 	color: {
	// 		type: String,
	// 		// #ifdef APP
	// 		default: '#1677ff' // '#c9c9c9'
	// 		// #endif
	// 	},
	// 	type: {
	// 		type: String,
	// 		default: 'circular'
	// 	},
	// 	size: {
	// 		type: String,
	// 		// #ifdef APP
	// 		default: '40rpx',
	// 		// #endif
	// 	},
	// 	text: {
	// 		type: String,
	// 		default: ''
	// 	},
	// 	textColor: {
	// 		type: String,
	// 		default: ''
	// 	},
	// 	textSize: {
	// 		type: String,
	// 		default: ''
	// 	},
	// 	vertical: {
	// 		type: Boolean,
	// 		default: false
	// 	},
	// 	animated: {
	// 		type: Boolean,
	// 		default: true
	// 	}
	// })