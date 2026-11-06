# uView Plus + uCharts 使用指南

本指南介绍如何在 UniApp 项目中使用 uView Plus UI 库和 uCharts 图表库。

## 📦 依赖安装

项目已安装以下依赖：

```json
{
  "dependencies": {
    "ucharts": "^0.0.0",
    "uview-plus": "^3.4.94"
  }
}
```

## ⚙️ 配置步骤

### 1. main.uts 配置

在 `main.uts` 中引入并使用 uView Plus：

```typescript
import App from './App.uvue'
import uviewPlus from 'uview-plus'

import { createSSRApp } from 'vue'
export function createApp() {
	const app = createSSRApp(App)
	app.use(uviewPlus)
	return {
		app
	}
}
```

### 2. pages.json 配置

添加 easycom 自动导入配置：

```json
{
	"easycom": {
		"autoscan": true,
		"custom": {
			"^u-(.*)$": "uview-plus/components/u-$1/u-$1.vue",
			"^up-(.*)$": "uview-plus/components/u-$1/u-$1.vue"
		}
	}
}
```

### 3. uni.scss 配置

在 `uni.scss` 文件顶部添加主题样式：

```scss
@import 'uview-plus/theme.scss';
```

### 4. App.uvue 配置

在 `App.uvue` 的 style 部分添加基础样式：

```vue
<style lang="scss">
	@import "uview-plus/index.scss";
	
	/*每个页面公共css */
	.uni-row {
		flex-direction: row;
	}

	.uni-column {
		flex-direction: column;
	}
</style>
```

## 🗓️ 日期时间选择器使用

### 基础用法

```vue
<template>
	<view>
		<!-- 日期输入框 -->
		<u-input
			v-model="selectedDate"
			placeholder="请选择日期"
			@click="showDatePicker = true"
			readonly
			suffix-icon="calendar"
		/>
		
		<!-- 日期选择器 -->
		<u-datetime-picker
			:show="showDatePicker"
			v-model="dateValue"
			mode="date"
			@confirm="onDateConfirm"
			@cancel="showDatePicker = false"
			title="选择日期"
		/>
	</view>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const showDatePicker = ref(false)
const dateValue = ref(Number(new Date()))
const selectedDate = ref('')

const onDateConfirm = (e: any) => {
	const date = new Date(e.value)
	selectedDate.value = date.toLocaleDateString('zh-CN')
	showDatePicker.value = false
}
</script>
```

### 支持的模式

- `date`: 日期选择
- `time`: 时间选择
- `datetime`: 日期时间选择
- `year-month`: 年月选择

### 常用属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| show | Boolean | false | 是否显示选择器 |
| v-model | Number/String | - | 绑定值 |
| mode | String | datetime | 选择器模式 |
| title | String | - | 选择器标题 |
| minDate | Number | 十年前 | 最小可选日期 |
| maxDate | Number | 十年后 | 最大可选日期 |

## 📊 uCharts 图表使用

### 饼图示例

```vue
<template>
	<view class="chart-container">
		<qiun-data-charts
			type="pie"
			:opts="pieChartOpts"
			:chartData="pieChartData"
			:canvas2d="true"
			:canvasId="'pieChart'"
			:canvas-id="'pieChart'"
		/>
	</view>
</template>

<script setup lang="ts">
import { reactive } from 'vue'

// 饼图数据
const pieChartData = reactive({
	categories: ["压力容器", "管道设备", "电气设备", "安全设备", "其他设备"],
	series: [{
		name: "巡检数量",
		data: [45, 32, 28, 15, 12]
	}]
})

// 饼图配置
const pieChartOpts = reactive({
	color: ["#1890FF", "#52C41A", "#FAAD14", "#F5222D", "#722ED1"],
	padding: [5, 5, 5, 5],
	dataLabel: true,
	legend: {
		show: true,
		position: "bottom",
		lineHeight: 25
	},
	extra: {
		pie: {
			activeOpacity: 0.5,
			activeRadius: 10,
			offsetAngle: 0,
			labelWidth: 15,
			border: true,
			borderWidth: 3,
			borderColor: "#FFFFFF"
		}
	}
})
</script>

<style scoped>
.chart-container {
	height: 300px;
	width: 100%;
}
</style>
```

### 环形图示例

```vue
<qiun-data-charts
	type="ring"
	:opts="ringChartOpts"
	:chartData="ringChartData"
	:canvas2d="true"
	:canvasId="'ringChart'"
	:canvas-id="'ringChart'"
/>
```

### 支持的图表类型

- `pie`: 饼图
- `ring`: 环形图
- `line`: 折线图
- `column`: 柱状图
- `area`: 面积图
- `radar`: 雷达图
- `gauge`: 仪表盘
- `candle`: K线图

### 图表配置选项

| 属性 | 类型 | 说明 |
|------|------|------|
| color | Array | 图表颜色数组 |
| padding | Array | 图表内边距 [上, 右, 下, 左] |
| dataLabel | Boolean | 是否显示数据标签 |
| legend | Object | 图例配置 |
| extra | Object | 图表特殊配置 |

## 🎨 常用 uView Plus 组件

### 按钮组件

```vue
<u-button type="primary" @click="handleClick">主要按钮</u-button>
<u-button type="success">成功按钮</u-button>
<u-button type="warning">警告按钮</u-button>
<u-button type="error">错误按钮</u-button>
```

### 表单组件

```vue
<u-form-item label="用户名" label-width="80">
	<u-input v-model="username" placeholder="请输入用户名" />
</u-form-item>
```

### 弹窗组件

```vue
<u-modal :show="showModal" title="提示" @confirm="onConfirm" @cancel="onCancel">
	<view class="slot-content">
		<text>模态框内容</text>
	</view>
</u-modal>
```

## 📱 示例页面

项目中已创建示例页面 `pages/demo/demo.uvue`，展示了：

1. 📅 日期时间选择器的三种模式
2. 📊 饼图和环形图的使用
3. 🔄 数据刷新和导出功能
4. 📱 响应式设计

## 🔗 参考链接

- [uView Plus 官方文档](https://uviewui.com/)
- [uCharts 官方文档](https://www.ucharts.cn/)
- [UniApp 官方文档](https://uniapp.dcloud.net.cn/)
- [参考博客文章](https://blog.csdn.net/qq_29101285/article/details/146940660)

## 💡 使用技巧

1. **性能优化**: 使用 `canvas2d` 模式提升图表渲染性能
2. **响应式设计**: 根据屏幕尺寸调整图表大小和布局
3. **数据更新**: 使用 `reactive` 响应式数据，图表会自动更新
4. **主题定制**: 通过修改 `uni.scss` 中的变量来定制主题
5. **组件复用**: 将常用的图表配置封装成组件，提高代码复用性

## 🐛 常见问题

### Q: 图表不显示？
A: 检查容器是否设置了高度，确保 `canvas-id` 唯一。

### Q: 日期选择器样式异常？
A: 确保已正确导入 uView Plus 的样式文件。

### Q: 组件无法自动导入？
A: 检查 `pages.json` 中的 `easycom` 配置是否正确。

### Q: 编译报错？
A: 确保所有依赖都已正确安装，检查语法是否符合 TypeScript 规范。