# lime-picker 选择器
- 用于一组预设数据中的选择。通常与弹出层组件配合使用。
- 插件依赖`lime-style`,`lime-shared`,`lime-loading`不喜勿下

## 安装
插件市场导入即可。首次导入可能需要重新编译

## 文档
[picker](https://limex.qcoon.cn/components/picker.html)

## 代码演示

### 基础用法

Picker 组件可通过 `columns` 属性配置选项数据，或子组件`l-picker-item`的`options`配置数据，二选一。<br>
顶部栏包含标题、确认按钮和取消按钮，点击确认按钮触发 `confirm` 事件，点击取消按钮触发 `cancel` 事件。
```html
<l-picker 
	cancel-btn="取消" 
	confirm-btn="确定"
	title="标题" 
	@confirm="onConfirm" 
	@pick="onChange" 
	@cancel="onCancel">
	<l-picker-item :options="cityOptions"></l-picker-item>
</l-picker>

<l-picker 
	cancel-btn="取消" 
	confirm-btn="确定" 
	title="标题" 
	@confirm="onConfirm" 
	@pick="onChange" 
	@cancel="onCancel"
	:columns="columns">
</l-picker>
```
```js
// 非TS项目不需要引入类型
import { PickerColumn, PickerConfirmEvent } from '@/uni_modules/lime-picker';

// 通过`l-picker-item`子组件时，数据结构为单数组
// PickerColumn 
const cityOptions = [
	{
		label: '北京市',
		value: '北京市',
	},
	{
		label: '上海市',
		value: '上海市',
	}
] as PickerColumn

// 通过 Picker 的 columns 属性的数组结构为[PickerColumn, PickerColumn]

const columns = [
	// 第一列
	cityOptions,
]

const onConfirm = ({values} : PickerConfirmEvent) => {
    showToast(`当前值: ${values.join(',')}`);
};
const onChange = ({values, column, index} : PickerPickEvent) => {
    showToast(`当前值: ${values.join(',')}`);
};
const onCancel = () => {console.log('取消')};
```




### 搭配弹出层使用

在实际场景中，Picker 通常作为用于辅助表单填写，可以搭配 [Popup](https://ext.dcloud.net.cn/plugin?id=20769)。

```html
<button type="primary" @click="showPicker = true">弹窗</button>
<l-popup v-model="showPicker" position="bottom">
	<l-picker 
		cancel-btn="取消" 
		confirm-btn="确定" 
		:columns="cityOptions" 
		@cancel="showPicker = false" 
		@confirm="showPicker = false">
	</l-picker>
</l-popup>
```
```js
import { PickerColumn } from '@/uni_modules/lime-picker';
const showPicker = ref(false)
const cityOptions = [
	[
		{
			label: '北京市',
			value: '北京市',
		},
		{
			label: '上海市',
			value: '上海市',
		}
	]
]  as PickerColumn[]
```


### 多列
`columns` 属性可以通过二维数组的形式配置多列选择。也可以使用`l-picker-item`子组件实现多列。
```html
<l-picker cancel-btn="取消" confirm-btn="确定" :columns="seasonColumns"></l-picker>
<l-picker cancel-btn="取消" confirm-btn="确定">
	<!-- 一个子组件表示一列 -->
	<l-picker-item :options="years"></l-picker-item>
	<l-picker-item :options="seasonOptions"></l-picker-item>
</l-picker>
```
```js
import { PickerColumn } from '@/uni_modules/lime-picker';
const showPicker = ref(false)
const years = [
	{
		label: '2024',
		value: '2024',
	},
	{
		label: '2025',
		value: '2025',
	}
]  as PickerColumn
const seasonOptions = [
	{
		label: '春',
		value: '春',
	},
	{
		label: '夏',
		value: '夏',
	},
	{
		label: '秋',
		value: '秋',
	},
	{
		label: '冬',
		value: '冬',
	},
]  as PickerColumn

const seasonColumns = [years, seasonOptions]

```

### 双向绑定

通过 `v-model` 可以绑定当前选中项的 `values`，修改 `v-model` 绑定的值时，`Picker` 的选中状态也会随之改变。

`v-model` 的值是一个数组，数组的第一位对应第一列选中项的 `value`，第二位对应第二列选中项的 `value`，以此类推。

```html
<l-picker v-model="citys" cancel-btn="取消" confirm-btn="确定">
	<l-picker-item :options="cityOptions"></l-picker-item>
</l-picker>
```
```js
import { PickerColumn } from '@/uni_modules/lime-picker';
const citys = ref<string[]>(['上海市'])
const cityOptions = [
	{
		label: '北京市',
		value: '北京市',
	},
	{
		label: '上海市',
		value: '上海市',
	},
	{
		label: '广州市',
		value: '广州市',
	},
	{
		label: '深圳市',
		value: '深圳市',
	},
] as PickerColumn
```


### 加载状态

若选择器数据是异步获取的，可以通过 `loading` 属性显示加载提示。
```html
<l-picker loading cancel-btn="取消" confirm-btn="确定" :columns="cityOptions"></l-picker>
```
```js
import { PickerColumn } from '@/uni_modules/lime-picker';
const cityOptions = [
	[
		{
			label: '北京市',
			value: '北京市',
		},
		{
			label: '上海市',
			value: '上海市',
		},
		{
			label: '广州市',
			value: '广州市',
		},
		{
			label: '深圳市',
			value: '深圳市',
		},
	]
] as PickerColumn[]
```

### 空状态
当数据为空时，可以使用 `empty` 插槽自定义空状态内容。[empty插件](https://ext.dcloud.net.cn/plugin?id=19563)
```html
<l-picker loading cancel-btn="取消" confirm-btn="确定" :columns="cityOptions">
	<template #empty>
		<l-empty description="没有数据" />
	</template>
</l-picker>
```
```js
import { PickerColumn } from '@/uni_modules/lime-picker';
const cityOptions = [] as PickerColumn[]
```


### 多列联动
通过`pick`事件完成联动操作。
```html
<l-picker cancel-btn="取消" confirm-btn="确定" @confirm="onConfirm" @pick="onChange">
	<l-picker-item :options="provinces"></l-picker-item>
	<l-picker-item :options="cities"></l-picker-item>
	<l-picker-item :options="counties"></l-picker-item>
</l-picker>
```
```js
import { PickerColumn, PickerColumnItem, PickerConfirmEvent, PickerPickEvent } from '@/uni_modules/lime-picker';
const areaList = {
	provinces: {
		'110000': '北京市',
		'440000': '广东省',
	},
	cities: {
		'110100': '北京市',
		'440100': '广州市',
		'440200': '韶关市',
		'440300': '深圳市',
		'440400': '珠海市',
		'440500': '汕头市',
		'440600': '佛山市',
	},
	counties: {
		'110101': '东城区',
		'110102': '西城区',
		'110105': '朝阳区',
		'110106': '丰台区',
		'110107': '石景山区',
		'110108': '海淀区',
		'110109': '门头沟区',
		'110111': '房山区',
		'110112': '通州区',
		'110113': '顺义区',
		'110114': '昌平区',
		'110115': '大兴区',
		'110116': '怀柔区',
		'110117': '平谷区',
		'110118': '密云区',
		'110119': '延庆区',
		'440103': '荔湾区',
		'440104': '越秀区',
		'440105': '海珠区',
		'440106': '天河区',
		'440111': '白云区',
		'440112': '黄埔区',
		'440113': '番禺区',
		'440114': '花都区',
		'440115': '南沙区',
		'440117': '从化区',
		'440118': '增城区',
		'440203': '武江区',
		'440204': '浈江区',
		'440205': '曲江区',
		'440222': '始兴县',
		'440224': '仁化县',
		'440229': '翁源县',
		'440232': '乳源瑶族自治县',
		'440233': '新丰县',
		'440281': '乐昌市',
		'440282': '南雄市',
		'440303': '罗湖区',
		'440304': '福田区',
		'440305': '南山区',
		'440306': '宝安区',
		'440307': '龙岗区',
		'440308': '盐田区',
		'440309': '龙华区',
		'440310': '坪山区',
		'440311': '光明区',
		'440402': '香洲区',
		'440403': '斗门区',
		'440404': '金湾区',
		'440507': '龙湖区',
		'440511': '金平区',
		'440512': '濠江区',
		'440513': '潮阳区',
		'440514': '潮南区',
		'440515': '澄海区',
		'440523': '南澳县',
		'440604': '禅城区',
		'440605': '南海区',
		'440606': '顺德区',
		'440607': '三水区',
		'440608': '高明区',
	},
};

// UTSJSONObject 为uniapp x的 object，非uniapp 可使用Object
const getOptions = (obj : UTSJSONObject, filter ?: ((value : PickerColumnItem) => boolean) | null) : PickerColumn => {
	const res = UTSJSONObject.keys(obj).map((key) : PickerColumnItem => {
		return {
			// id: key,
			value: `${key}`,
			label: `${obj[key]}`
		} as PickerColumnItem
	})
	if (filter != null) {
		return res.filter(filter)
	}
	return res
}
const match = (v1 : string, v2 : string, size : number) : boolean => {
	return  v1.slice(0, size) == v2.slice(0, size)
};
const getCounties = (cityValue : string) : PickerColumn => {
	return getOptions(areaList.counties as UTSJSONObject, (county : PickerColumnItem) : boolean => match(county.value, cityValue, 4));
};
const getCities = (provinceValue : string) : PickerColumn[] => {
	const cities = getOptions(areaList.cities as UTSJSONObject, (city : PickerColumnItem) : boolean => match(city.value, provinceValue, 2));
	const counties = getCounties(cities[0].value);
	return [cities, counties];
};
// 省
const provinces = ref<PickerColumnItem[]>([]);
provinces.value = getOptions(areaList.provinces as UTSJSONObject, null)
const data = getCities(provinces.value[0].value);
// 市
const cities = ref<PickerColumnItem[]>(data[0]);
// 区
const counties = ref<PickerColumnItem[]>(data[1]);

const onConfirm = (context : PickerConfirmEvent) => {
	console.log('context', context)
}
const onChange = ({values, column, index} : PickerPickEvent) => {
	// column 表示第几列
	// index 表示列数组里的下标
	if (column == 0) {
		// 更改省份
		const data = getCities(provinces.value[index].value);
		cities.value = data[0];
		counties.value = data[1];
	}

	if (column == 1) {
		// 更改城市
		counties.value = getCounties(cities.value[index].value);
	}

	if (column == 2) {
		// 更改区县
	}
}
```

### 级联选择
使用 `l-cascade` 组件 通过`columns` 的 `children` 字段可以实现选项级联的效果。如果级联层级较多，推荐使用 [Cascader 级联选项组件](https://ext.dcloud.net.cn/plugin?id=20880)。

```html
<l-cascade :columns="columns" confirmBtn="确认" cancel-btn="取消"></l-cascade>
```
```js
 const columns = [
	{
		lable: '浙江',
		value: 'Zhejiang',
		children: [
		  {
			lable: '杭州',
			value: 'Hangzhou',
			children: [
			  { lable: '西湖区', value: 'Xihu' },
			  { lable: '余杭区', value: 'Yuhang' },
			],
		  },
		  {
			lable: '温州',
			value: 'Wenzhou',
			children: [
			  { lable: '鹿城区', value: 'Lucheng' },
			  { lable: '瓯海区', value: 'Ouhai' },
			],
		  },
		],
		},
		{
		lable: '福建',
		value: 'Fujian',
		children: [
		  {
			lable: '福州',
			value: 'Fuzhou',
			children: [
			  { lable: '鼓楼区', value: 'Gulou' },
			  { lable: '台江区', value: 'Taijiang' },
			],
		  },
		  {
			lable: '厦门',
			value: 'Xiamen',
			children: [
			  { lable: '思明区', value: 'Siming' },
			  { lable: '海沧区', value: 'Haicang' },
			],
		  },
		],
		},
	];
```

### 查看示例
- 导入后直接使用这个标签查看演示效果

```html
<!-- // 代码位于 uni_modules/lime-picker/compoents/lime-picker -->
<lime-picker />
```


### 插件标签
- 默认 l-picker 为 component
- 默认 lime-picker 为 demo

### 关于vue2的使用方式
- 插件使用了`composition-api`, 如果你希望在vue2中使用请按官方的教程[vue-composition-api](https://uniapp.dcloud.net.cn/tutorial/vue-composition-api.html)配置
- 关键代码是: 在main.js中 在vue2部分加上这一段即可
```js
// vue2
import Vue from 'vue'
import VueCompositionAPI from '@vue/composition-api'
Vue.use(VueCompositionAPI)
```



## API

### Picker Props

| 参数 | 说明 | 类型 | 默认值 |
| --- | --- | --- | --- |
| v-model | 当前选中项对应的值 | _string\|number[]_ | - |
| value | 当前选中项对应的值 | _string\|number[]_ | - |
| columns | 对象数组，配置每一列显示的数据 | _PickerColumn[]_ | `false` |
| cancelBtn | 取消按钮文字 | _string_ | `` |
| cancelStyle | 取消按钮样式 | _string_ | `` |
| confirmBtn | 确定按钮文字 | _string_ | `` |
| confirmStyle | 确定按钮样式 | _string_ | `` |
| title |  标题 | _string_ | `` |
| titleStyle |  标题样式 | _string_ | `` |
| loading | 是否显示加载状态 | _boolean_ | `false` |
| loadingColor | 加载图标颜色 | _string_ | `` |
| loadingMaskColor | 加载遮罩背景颜色 | _string_ | `` |
| loadingSize | 加载图标大小 | _string_ | `` |
| itemHeight | 每项高度 | _string_ | `50px` |
| itemColor | 每项文本颜色 | _string_ | `` |
| itemFontSize | 每项文本字体大小 | _string_ | `` |
| itemActiveColor | 每项文本选中颜色 | _string_ | `` |
| indicatorStyle | 选择器中间选中框的样式 | _string_ | `` |
| bgColor | 选择器背景色 | _string_ | `` |
| groupHeight | 选项组高度 | _string_ | `400rpx` |
| radius | 圆角 | _string_ |  |

### PickerItem Props

| 参数 | 说明 | 类型 | 默认值 |
| --- | --- | --- | --- |
| options | 对象数组，配置列显示的数据 | _PickerColumnItem[]_ | `false` |
| value | 值 | _string_ |  |
| column | 当前为第几列 | _number_ |  |


### Events
| 事件名 | 说明                     | 回调参数               |
| ------ | ------------------------ | ---------------------- |
| cancel | 点击取消按钮时触发 | __ |
| change | 选中变化时候触发 | _value: string\|number[]_ |
| confirm | 点击确认按钮时触发 | _{values: PickerValue[], indexs:number[], items:PickerColumnItem[]}:PickerConfirmEvent_ |
| pick | 任何一列选中都会触发，不同的列参数不同,column 表示第几列变化，index 表示变化那一列的选中项下标 | _{values: PickerValue[], column:number,index:number}:PickerPickEvent_ |

### Picker Slots
| 插槽名 | 说明                     | 回调参数               |
| ------ | ------------------------ | ---------------------- |
| default |  | __ |
| header | 头部 | __ |
| footer | 底部 | __ |
| empty | 空数据插槽 | __ |



## 主题定制

### 样式变量

组件提供了下列 CSS 变量，可用于自定义样式，uvue app无效。

| 名称                     | 默认值               | 描述 |
| ------------------------ | -------------------- | ---- |
| --l-picker-border-radius    | _24rpx_                  | -    |
| --l-picker-bg-color | _$bg-color-container_ | -    |
| --l-picker-toolbar-height | _116rpx_ | -    |
| --l-picker-cancel-color | _text-color-2_ | -    |
| --l-picker-confirm-color | _$primary-color_ | -    |
| --l-picker-button-font-size | _32rpx_ | -    |
| --l-picker-title-font-size | _36rpx_ | -    |
| --l-picker-title-font-weight | _700_ | -    |
| --l-picker-title-line-height | _52rpx_ | -    |
| --l-picker-title-color | _$text-color-1_ | -    |
| --l-picker-group-height | _400rpx_ | -    |
| --l-picker-indicator-bg-color | _$fill-4_ | -    |
| --l-picker-indicator-border-radius | _12rpx_ | -    |
| --l-picker-item-height | _50px_ | -    |
| --l-picker-item-color | _$text-color-1_ | -    |
| --l-picker-item-active-color | _$text-color-1_ | -    |
| --l-picker-item-active-color | _$text-color-1_ | -    |
| --l-picker-loading-mask-color | _rgba(255,255,255,.9)_ | -    |
| --l-picker-item-font-size | _$font-size-md_ | -    |


## 打赏

如果你觉得本插件，解决了你的问题，赠人玫瑰，手留余香。  
![](https://testingcf.jsdelivr.net/gh/liangei/image@1.9/alipay.png)
![](https://testingcf.jsdelivr.net/gh/liangei/image@1.9/wpay.png)