# lime-loading 加载中
- 用于表示页面或操作的加载状态，给予用户反馈的同时减缓等待的焦虑感，由一个或一组反馈动效组成。

## 文档
[loading](https://limex.qcoon.cn/components/loading.html)


## 安装
插件市场导入即可，首次导入可以需要重新编译

## 代码演示
### 基础
通过 `type` 属性可以设置加载图标的类型，默认为 `circular`，可选值为 `spinner`。nvue只有`circular`，因为其使用的是原生组件。

```vue
<l-loading />
<l-loading type="spinner"/>
```

### 横向文字
通过`text`或`插槽`设置加载文本,uvue建议使用`text`

```vue
<l-loading>加载中…</l-loading>
<l-loading text="加载中…"></l-loading>
```

### 竖向文字

```vue
<l-loading :vertical="true">加载中…</l-loading>
```

### 尺寸
通过 `size` 属性设置加载图标的大小，默认单位为 `px`, uvue必须带单位。

```vue
<l-loading size="40rpx"></l-loading>
<l-loading size="60rpx"></l-loading>
```

### 颜色
通过 `color` 属性设置加载图标的颜色。

```vue
<l-loading color="red" ></l-loading>
```

### 查看示例
- 导入后直接使用这个标签查看演示效果

```html
<!-- // 代码位于 uni_modules/lime-loading/compoents/lime-loading -->
<lime-loading />
```





### 插件标签
- 默认 l-loading 为 component
- 默认 lime-loading 为 demo


### 关于vue2的使用方式
- 插件使用了`composition-api`, 如果你希望在vue2中使用请按官方的教程[vue-composition-api](https://uniapp.dcloud.net.cn/tutorial/vue-composition-api.html)配置
- 关键代码是: 在main.js中 在vue2部分加上这一段即可.
```js
// vue2
import Vue from 'vue'
import VueCompositionAPI from '@vue/composition-api'
Vue.use(VueCompositionAPI)
```

## API

### Props

| 参数       | 说明                          | 类型               | 默认值     |
| ---------- | ----------------------------- | ------------------ | ---------- |
| color      | 颜色                          | _string_           | ``  |
| type       | 类型，可选值为 `spinner`      | _string_           | `circular` |
| size       | 加载图标大小，默认单位为 `px`,uvue只支持string | _number \| string_ | `40rpx`     |
| text       | 加载文本 |  _string_ | `-`     |
| textColor       | 文本颜色 |  _string_ | `-`     |
| textSize       | 文本大小 `px`,uvue只支持string | _number \| string_ | `-`     |
| vertical   | 是否垂直排列图标和文字内容    | _boolean_          | `false`    |
| mode   | 动画实现的模式.只针对APP,可选值有：`animate`    | _string_          | `raf`    |

### Slots

| 名称    | 说明           |
| ------- | -------------- |
| default | 加载文案       |



## 主题定制

### 样式变量

组件提供了下列 CSS 变量，可用于自定义样式。uvue和nvue不支持！

| 名称                           | 默认值                    | 描述 |
| ------------------------------ | ------------------------- | ---- |
| --l-loading-color       | _#1677ff_ | -    |
| --l-loading-text-color       | _rgba(0,0,0,0.45)_ | -    |
| --l-loading-font-size   | _28rpx_ | -    |
| --l-loading-duration | _2s_                    | -    |