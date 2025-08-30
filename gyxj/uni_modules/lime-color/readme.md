# lime-color
- 颜色转换

## 安装
插件市场导入插件即可

## 使用
```js
import {tinyColor} from '@/uni_modules/lime-color'
```

## 接受的字符串输入

字符串解析非常宽容。它的目的是使输入颜色尽可能简单。所有的逗号、百分比、括号都是可选的，大多数输入允许使用 0-1、0%-100% 或 0-n（其中 n 取决于值的 100、255 或 360）。
HSL 和 HSV 都需要 0%-100% 或 0-1 作为 S/L/V 属性。H（色相）可以在 0%-100% 或 0-360 之间取值。
RGB 输入需要 0-255 或 0%-100%。
以下是一些字符串输入的示例：


### Hex, 8-digit (RGBA) Hex

```ts
tinyColor('#000');
tinyColor('000');
tinyColor('#369C');
tinyColor('369C');
tinyColor('#f0f0f6');
tinyColor('f0f0f6');
tinyColor('#f0f0f688');
tinyColor('f0f0f688');
```

### RGB, RGBA

```ts
tinyColor('rgb (255, 0, 0)');
tinyColor('rgb 255 0 0');
tinyColor('rgba (255, 0, 0, .5)');
tinyColor({ r: 255, g: 0, b: 0 });
```

### HSL, HSLA

```ts
tinyColor('hsl(0, 100%, 50%)');
tinyColor('hsla(0, 100%, 50%, .5)');
tinyColor('hsl(0, 100%, 50%)');
tinyColor('hsl 0 1.0 0.5');
tinyColor({ h: 0, s: 1, l: 0.5 });
```

### HSV, HSVA

```ts
tinyColor('hsv(0, 100%, 100%)');
tinyColor('hsva(0, 100%, 100%, .5)');
tinyColor('hsv (0 100% 100%)');
tinyColor('hsv 0 1 1');
tinyColor({ h: 0, s: 100, v: 100 });
```

### Named

```ts
tinyColor('RED');
tinyColor('blanchedalmond');
tinyColor('darkblue');
```

### Number
```ts
tinyColor(0x0);
tinyColor(0xaabbcc);
```


## 属性

### originalInput
传递到构造函数中用于创建`tinyColor`实例的原始输入。

```ts
const color = tinyColor('red');
color.originalInput; // "red"
const color2 = tinyColor({ r: 255, g: 255, b: 255 });
color2.originalInput; // "{r: 255, g: 255, b: 255}"
```

### format

返回用于创建`tinyColor`实例的格式

```ts
const color = tinyColor('red');
color.format; // "name"
const color2 = tinyColor({ r: 255, g: 255, b: 255 });
color2.format; // "rgb"
```

### isValid

一个布尔值，指示颜色是否成功被解析。注意：如果颜色无效，则在与其他方法一起使用时将表现得像“黑色”。

```ts
const color1 = tinyColor('red');
color1.isValid; // true
color1.toHexString(); // "#ff0000"

const color2 = tinyColor('not a color');
color2.isValid; // false
color2.toString(); // "#000000"
```

## Methods 方法

### getBrightness

返回颜色的感知亮度，范围从 0-255，这是根据 [Web内容无障碍指南（第1版）](http://www.w3.org/TR/AERT#color-contrast) 定义的。

```ts
const color1 = tinyColor('#fff');
color1.getBrightness(); // 255

const color2 = tinyColor('#000');
color2.getBrightness(); // 0
```

### isLight

返回一个布尔值，指示颜色的感知亮度是否为浅色。

```ts
const color1 = tinyColor('#fff');
color1.isLight(); // true

const color2 = tinyColor('#000');
color2.isLight(); // false
```

### isDark

返回一个布尔值，指示颜色的感知亮度是否为深色。

```ts
const color1 = tinyColor('#fff');
color1.isDark(); // false

const color2 = tinyColor('#000');
color2.isDark(); // true
```

### getLuminance

返回颜色的感知亮度（luminance），范围从 0-1，这是根据 [Web内容无障碍指南（第2版）](http://www.w3.org/TR/2008/REC-WCAG20-20081211/#contrast-ratiodef) 定义的。

```ts
const color1 = tinyColor('#fff');
color1.getLuminance(); // 1

const color2 = tinyColor('#000');
color2.getLuminance(); // 0
```

### getAlpha

返回颜色的`alpha`（透明度）值，范围从 `0-1`。

```ts
const color1 = tinyColor('rgba(255, 0, 0, .5)');
color1.getAlpha(); // 0.5

const color2 = tinyColor('rgb(255, 0, 0)');
color2.getAlpha(); // 1

const color3 = tinyColor('transparent');
color3.getAlpha(); // 0
```

### setAlpha

在当前颜色上设置`alpha`（透明度）值。接受的范围是 `0-1` 之间。

```ts
const color = tinyColor('red');
color.getAlpha(); // 1
color.setAlpha(0.5);
color.getAlpha(); // .5
color.toRgbString(); // "rgba(255, 0, 0, .5)"
```

### onBackground

计算颜色在背景上的显示效果。当颜色完全透明（即 `getAlpha() == 0`）时，结果将是背景颜色。当颜色完全不透明（即 `getAlpha() == 1`）时，结果将是颜色本身。否则，你将得到一个计算结果。

```ts
const color = tinyColor('rgba(255, 0, 0, .5)');
const computedColor = color.onBackground('rgb(0, 0, 255)');
computedColor.toRgbString(); // "rgb(128, 0, 128)"
```

### toHsv

```ts
const color = tinyColor('red');
color.toHsv(); // { h: 0, s: 1, v: 1, a: 1 }
```

### toHsvString

```ts
const color = tinyColor('red');
color.toHsvString(); // "hsv(0, 100%, 100%)"
color.setAlpha(0.5);
color.toHsvString(); // "hsva(0, 100%, 100%, 0.5)"
```

### toHsl

```ts
const color = tinyColor('red');
color.toHsl(); // { h: 0, s: 1, l: 0.5, a: 1 }
```

### toHslString

```ts
const color = tinyColor('red');
color.toHslString(); // "hsl(0, 100%, 50%)"
color.setAlpha(0.5);
color.toHslString(); // "hsla(0, 100%, 50%, 0.5)"
```

### toNumber
```ts
tinyColor('#aabbcc').toNumber() === 0xaabbcc // true
tinyColor('rgb(1, 1, 1)').toNumber() === (1 << 16) + (1 << 8) + 1 // true
```

### toHex

```ts
const color = tinyColor('red');
color.toHex(); // "ff0000"
```

### toHexString

```ts
const color = tinyColor('red');
color.toHexString(); // "#ff0000"
```

### toHex8

```ts
const color = tinyColor('red');
color.toHex8(); // "ff0000ff"
```

### toHex8String

```ts
const color = tinyColor('red');
color.toHex8String(); // "#ff0000ff"
```

### toHexShortString
根据颜色的透明度（Alpha值）返回较短的十六进制值，并且值前面带有#符号
```ts
const color1 = tinyColor('#ff000000');
color1.toHexShortString(); // "#ff000000"
color1.toHexShortString(true); // "#f000"

const color2 = tinyColor('#ff0000ff');
color2.toHexShortString(); // "#ff0000"
color2.toHexShortString(true); // "#f00"
```

### toRgb

```ts
const color = tinyColor('red');
color.toRgb(); // { r: 255, g: 0, b: 0, a: 1 }
```

### toRgbString

```ts
const color = tinyColor('red');
color.toRgbString(); // "rgb(255, 0, 0)"
color.setAlpha(0.5);
color.toRgbString(); // "rgba(255, 0, 0, 0.5)"
```

### toPercentageRgb
将当前颜色转换为百分比表示的 RGB
```ts
const color = tinyColor('red');
color.toPercentageRgb(); // { r: "100%", g: "0%", b: "0%", a: 1 }
```

### toPercentageRgbString

```ts
const color = tinyColor('red');
color.toPercentageRgbString(); // "rgb(100%, 0%, 0%)"
color.setAlpha(0.5);
color.toPercentageRgbString(); // "rgba(100%, 0%, 0%, 0.5)"
```

### toName

```ts
const color = tinyColor('red');
color.toName(); // "red"
```


### toString

根据输入格式打印成字符串。你也可以通过向函数中传入以下之一来覆盖这个行为：`"rgb", "prgb", "hex6", "hex3", "hex8", "name", "hsl", "hsv"`

```ts
const color1 = tinyColor('red');
color1.toString(); // "red"
color1.toString('hsv'); // "hsv(0, 100%, 100%)"

const color2 = tinyColor('rgb(255, 0, 0)');
color2.toString(); // "rgb(255, 0, 0)"
color2.setAlpha(0.5);
color2.toString(); // "rgba(255, 0, 0, 0.5)"
```

### 颜色修改

这些方法操纵当前颜色，并返回它以进行链式调用。例如：

```ts
tinyColor('red')
  .lighten()
  .desaturate()
  .toHexString(); // '#f53d3d'
```

### lighten

`lighten: function(amount = 10) -> TinyColor`.根据给定的量（从0到100）调亮颜色。提供100将始终返回白色.

```ts
tinyColor('#f00').lighten().toString(); // '#ff3333'
tinyColor('#f00').lighten(100).toString(); // '#ffffff'
```

### brighten

`brighten: function(amount = 10) -> TinyColor`. 根据给定的量（从0到100）提高颜色的亮度。

```ts
tinyColor('#f00').brighten().toString(); // '#ff1919'
```

### darken

`darken: function(amount = 10) -> TinyColor`. 根据给定的量（从0到100）调暗颜色。提供100将始终返回黑色.

```ts
tinyColor('#f00').darken().toString(); // '#cc0000'
tinyColor('#f00').darken(100).toString(); // '#000000'
```

### tint

将颜色与纯白色混合，范围从0到100。提供0将不进行任何操作，提供100将始终返回白色.

```ts
tinyColor('#f00').tint().toString(); // "#ff1a1a"
tinyColor('#f00').tint(100).toString(); // "#ffffff"
```

### shade

将颜色与纯黑色混合，范围从0到100。提供0将不进行任何操作，提供100将始终返回黑色。

```ts
tinyColor('#f00').shade().toString(); // "#e60000"
tinyColor('#f00').shade(100).toString(); // "#000000"
```

### desaturate

`desaturate: function(amount = 10) -> TinyColor`. 根据给定的量（从0到100）降低颜色的饱和度。提供100将与调用`greyscale`相同。


```ts
tinyColor('#f00').desaturate().toString(); // "#f20d0d"
tinyColor('#f00').desaturate(100).toString(); // "#808080"
```

### saturate

`saturate: function(amount = 10) -> TinyColor`. 根据给定的量（从0到100）增加颜色的饱和度。

```ts
tinyColor('hsl(0, 10%, 50%)').saturate().toString(); // "hsl(0, 20%, 50%)"
```

### greyscale

`greyscale: function() -> TinyColor`. 完全降低颜色的饱和度，使其变为灰度。与调用`desaturate(100)`相同。

```ts
tinyColor('#f00').greyscale().toString(); // "#808080"
```

### spin

`spin: function(amount = 0) -> TinyColor`. 根据给定的量（从-360到360）旋转色相。调用时使用0、360或-360将不进行任何操作（因为它将色相设置回原来的值）。

```ts
tinyColor('#f00').spin(180).toString(); // "#00ffff"
tinyColor('#f00').spin(-90).toString(); // "#7f00ff"
tinyColor('#f00').spin(90).toString(); // "#80ff00"

// spin(0) and spin(360) do nothing
tinyColor('#f00').spin(0).toString(); // "#ff0000"
tinyColor('#f00').spin(360).toString(); // "#ff0000"
```

### mix

`mix: function(amount = 50) => TinyColor`. 将当前颜色与另一种颜色按给定量（从0到100）混合。0表示不混合（返回当前颜色）。

```ts
let color1 = tinyColor('#f0f');
let color2 = tinyColor('#0f0');

color1.mix(color2).toHexString(); // #808080
```

### 颜色组合

组合函数除非特别说明，否则返回一个`TinyColor`对象的数组。

### analogous
生成一组与当前颜色相似的颜色。
`analogous: function(results = 6, slices = 30) -> array<TinyColor>`.

```ts
const colors = tinyColor('#f00').analogous();
colors.map((t):string => t.toHexString()); // [ "#ff0000", "#ff0066", "#ff0033", "#ff0000", "#ff3300", "#ff6600" ]
```

### monochromatic
生成一组与当前颜色具有相同色相和饱和度的颜色。
`monochromatic: function(, results = 6) -> array<TinyColor>`.

```ts
const colors = tinyColor('#f00').monochromatic();
colors.map((t):string => t.toHexString()); // [ "#ff0000", "#2a0000", "#550000", "#800000", "#aa0000", "#d40000" ]
```

### splitcomplement
生成当前颜色的分裂补色。
`splitcomplement: function() -> array<TinyColor>`.

```ts
const colors = tinyColor('#f00').splitcomplement();
colors.map((t):string => t.toHexString()); // [ "#ff0000", "#ccff00", "#0066ff" ]
```

### triad
生成当前颜色的三色调。
`triad: function() -> array<TinyColor>`. Alias for `polyad(3)`.

```ts
const colors = tinyColor('#f00').triad();
colors.map((t):string => t.toHexString()); // [ "#ff0000", "#00ff00", "#0000ff" ]
```

### tetrad
生成当前颜色的四色调。
`tetrad: function() -> array<TinyColor>`. Alias for `polyad(4)`.

```ts
const colors = tinyColor('#f00').tetrad();
colors.map((t):string => t.toHexString()); // [ "#ff0000", "#80ff00", "#00ffff", "#7f00ff" ]
```

### polyad
生成当前颜色的 n 色调。
`polyad: function(number) -> array<TinyColor>`.

```ts
const colors = tinyColor('#f00').polyad(4);
colors.map((t):string => t.toHexString()); // [ "#ff0000", "#80ff00", "#00ffff", "#7f00ff" ]
```

### complement
计算当前颜色的补色。
`complement: function() -> TinyColor`.

```ts
tinyColor('#f00').complement().toHexString(); // "#00ffff"
```

## 颜色工具

### equals
判断两色是否相同

```ts
let color1 = tinyColor('red');
let color2 = tinyColor('#f00');

color1.equals(color2); // true
```


## 常见操作

### clone

`clone: function() -> TinyColor`.
使用相同的颜色实例化一个新的`TinyColor`对象。对新的对象的任何更改都不会影响旧的对象。

```ts
const color1 = tinyColor('#F00');
const color2 = color1.clone();
color2.setAlpha(0.5);

color1.toString(); // "#ff0000"
color2.toString(); // "rgba(255, 0, 0, 0.5)"
```

### generate
通过一个主色生成10个等级的颜色数组，使用 Ant Design 的颜色生成算法。	

```ts
import {generate, LGenerateOptions} from '@/uni_modules/lime-color'
// 第二个参数为选填，如果不填则默认为 'default'
generate('red',{ theme: 'default'|'dark'} as LGenerateOptions)
// ['#2c1113', '#450f11', '#5b0e0e', '#7e0b0b', '#ad0707', '#dc0303', '#e82d27', '#f3594f', '#f88577', '#faaca0']
```


## 打赏

如果你觉得本插件，解决了你的问题，赠人玫瑰，手留余香。  
![](https://testingcf.jsdelivr.net/gh/liangei/image@1.9/alipay.png)
![](https://testingcf.jsdelivr.net/gh/liangei/image@1.9/wpay.png)