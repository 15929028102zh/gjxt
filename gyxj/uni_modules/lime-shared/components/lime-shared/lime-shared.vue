<template>
	<view id="shared" style="height: 500px; width: 300px; background-color: aqua;">
		
	</view>
</template>

<script lang="ts">
	// #ifdef WEB
	import validator from 'validator'
	// #endif
	import { getRect, getAllRect } from '@/uni_modules/lime-shared/getRect'
	
	import { camelCase } from '@/uni_modules/lime-shared/camelCase'
	import { canIUseCanvas2d } from '@/uni_modules/lime-shared/canIUseCanvas2d'
	import { clamp } from '@/uni_modules/lime-shared/clamp'
	import { cloneDeep } from '@/uni_modules/lime-shared/cloneDeep'
	import { closest } from '@/uni_modules/lime-shared/closest'
	import { debounce } from '@/uni_modules/lime-shared/debounce'
	import { fillZero } from '@/uni_modules/lime-shared/fillZero'
	import { floatAdd } from '@/uni_modules/lime-shared/floatAdd'
	import { floatMul } from '@/uni_modules/lime-shared/floatMul'
	import { floatDiv } from '@/uni_modules/lime-shared/floatDiv'
	import { floatSub } from '@/uni_modules/lime-shared/floatSub'
	import { getClassStr } from '@/uni_modules/lime-shared/getClassStr'
	import { getCurrentPage } from '@/uni_modules/lime-shared/getCurrentPage'
	import { getStyleStr } from '@/uni_modules/lime-shared/getStyleStr'
	import { hasOwn } from '@/uni_modules/lime-shared/hasOwn'
	import { isBase64 } from '@/uni_modules/lime-shared/isBase64'
	import { isBrowser } from '@/uni_modules/lime-shared/isBrowser'
	import { isDef } from '@/uni_modules/lime-shared/isDef'
	import { isEmpty } from '@/uni_modules/lime-shared/isEmpty'
	import { isFunction } from '@/uni_modules/lime-shared/isFunction'
	import { isNumber } from '@/uni_modules/lime-shared/isNumber'
	import { isNumeric } from '@/uni_modules/lime-shared/isNumeric'
	import { isObject } from '@/uni_modules/lime-shared/isObject'
	import { isPromise } from '@/uni_modules/lime-shared/isPromise'
	import { isString } from '@/uni_modules/lime-shared/isString'
	import { kebabCase } from '@/uni_modules/lime-shared/kebabCase'
	import { raf, doubleRaf } from '@/uni_modules/lime-shared/raf'
	import { random } from '@/uni_modules/lime-shared/random'
	import { range } from '@/uni_modules/lime-shared/range'
	import { sleep } from '@/uni_modules/lime-shared/sleep'
	import { throttle } from '@/uni_modules/lime-shared/throttle'
	import { toArray } from '@/uni_modules/lime-shared/toArray'
	import { toBoolean } from '@/uni_modules/lime-shared/toBoolean'
	import { toNumber } from '@/uni_modules/lime-shared/toNumber'
	import { unitConvert } from '@/uni_modules/lime-shared/unitConvert'
	import { getCurrentInstance } from '@/uni_modules/lime-shared/vue'
	import { capitalizedAmount } from '@/uni_modules/lime-shared/capitalizedAmount'
	
	import { obj2url } from '@/uni_modules/lime-shared/obj2url'
	import { isURL, type IsURLOptions } from '@/uni_modules/lime-shared/isURL'
	import { isIP } from '@/uni_modules/lime-shared/isIP'
	import { isDate, type IsDateOptions } from '@/uni_modules/lime-shared/isDate'
	import { isEmail } from '@/uni_modules/lime-shared/isEmail'
	import { isRegExp } from '@/uni_modules/lime-shared/isRegExp'
	import { isValidDomain, type IsValidDomainOptions } from '@/uni_modules/lime-shared/isValidDomain'
	import { merge } from '@/uni_modules/lime-shared/merge'
	import { isByteLength, type IsByteLengthOptions } from '@/uni_modules/lime-shared/isByteLength'
	
	// #ifdef VUE2
	type UTSJSONObject = any
	// #endif
	
	const context = getCurrentInstance()
	// getRect('#shared', context!).then(res =>{
	// 	console.log('res', res.bottom)
	// })
	// getAllRect('#shared', context).then(res =>{
	// 	console.log('res', res)
	// })
	
	
	// console.log('camelCase::', camelCase("hello world"));
	// console.log('camelCase::', camelCase("my_name_is_john", true));
	// console.log('canIUseCanvas2d::', canIUseCanvas2d());
	// console.log('clamp::', clamp(5 ,0, 10));
	// console.log('cloneDeep::', cloneDeep<UTSJSONObject>({a:5}));
	// console.log('closest::', closest([1, 3, 5, 7, 9], 6));
	
	
	
	
	// const saveData = (data: any) => {
	// 	// 模拟保存数据的操作
	// 	console.log(`Saving data: ${data}`);
	// }
	
	// const debouncedSaveData = debounce(saveData, 500);
	// debouncedSaveData('Data 1'); 
	// debouncedSaveData('Data 2'); 
	
	// console.log('fillZero', fillZero(1))
	// console.log('floatAdd', floatAdd(0.1, 0.2), floatAdd(1.05, 0.05), floatAdd(0.1, 0.7), floatAdd(0.0001, 0.0002), floatAdd(123.456 , 789.012))
	// console.log('floatMul', floatMul(0.1, 0.02), floatMul(1.0255, 100))
	// console.log('floatDiv', floatDiv(10.44, 100), floatDiv(1.0255, 100), floatDiv(5.419909340994699, 0.2))
	// console.log('floatSub', floatSub(0.4, 0.1), floatSub(1.0255, 100))
	// const now = () : number => System.nanoTime() / 1_000_000.0
	// console.log('capitalizedAmount', capitalizedAmount(0.4))
	// console.log('capitalizedAmount', capitalizedAmount(100))
	// console.log('capitalizedAmount', capitalizedAmount(100000000))
	// console.log('capitalizedAmount', capitalizedAmount('2023.04'))
	// console.log('capitalizedAmount', capitalizedAmount(-1024))
	// console.log('now', now(), Date.now())
	// console.log('getClassStr', getClassStr({hover: true}))
	// console.log('getStyleStr', getStyleStr({ color: 'red', fontSize: '16px', backgroundColor: '', border: null }))
	// console.log('hasOwn', hasOwn({a: true}, 'key'))
	// console.log('isBase64::', isBase64("SGVsbG8sIFdvcmxkIQ=="));
	// console.log('isBrowser::', isBrowser);
	// console.log('isDef::', isDef('6'));
	// console.log('isEmpty::', isEmpty({a: true}));
	
	// const b = () =>{}
	// console.log('isFunction::', isFunction(b));
	// console.log('isNumber::', isNumber('6'));
	// console.log('isNumeric::', isNumeric('6'));
	// console.log('isObject::', isObject({}));
	
	// const promise = ():Promise<boolean> => {
	// 	return new Promise((resolve) => {
	// 		resolve(true)
	// 	})
	// }
	// const a = promise()
	// console.log('isPromise::', isPromise(a));
	// console.log('isString::', isString('null'));
	// console.log('kebabCase::', kebabCase('my love'));
	// console.log('raf::', raf(()=>{
	// 	console.log('raf:::1')
	// }));
	// console.log('doubleRaf::', doubleRaf(()=>{
	// 	console.log('doubleRaf:::1')
	// }));
	// console.log('random', random(0, 10))
	// console.log('random', random(0, 1, 2))
	// console.log('range', range(0, 10, 2))
	// console.log('sleep', sleep(300).then(res => {
	// 	console.log('log')
	// }))
	
	// const handleScroll = (a: string) => {
	//   console.log("Scroll event handled!", a);
	// }
	
	// // // 使用节流函数对 handleScroll 进行节流，间隔时间为 500 毫秒
	// const throttledScroll = throttle(handleScroll, 500);
	// throttledScroll('5');
	// const page = getCurrentPage()
	// console.log('getCurrentPage::', page)
	
	// console.log('toArray', toArray<number>(5))
	// console.log('toBoolean', toBoolean(5))
	// console.log('toNumber', toNumber('5'))
	// console.log('unitConvert', unitConvert('5'))
	
	// uni.getImageInfo({
	// 	src: '/static/logo.png',
	// 	success(res) {
	// 		console.log('res', res)
	// 	}
	// })
	
	// --------------------------
	// IPv4 验证示例
	// --------------------------
	
	// 标准IPv4格式
	// console.log(isIP('192.168.1.1', 4));             // true
	// console.log(isIP('255.255.255.255', { version: 4 })); // true
	
	// // 边界值验证
	// console.log(isIP('0.0.0.0', 4));                 // true
	// console.log(isIP('223.255.255.255', '4'));       // true
	
	// // 非法IPv4案例
	// console.log(isIP('256.400.999.1', 4));           // false（数值超限）
	// console.log(isIP('192.168.01', 4));              // false（段数不足）
	
	// // --------------------------
	// // IPv6 验证示例
	// // --------------------------
	
	// // 标准IPv6格式
	// console.log(isIP('2001:0db8:85a3:0000:0000:8a2e:0370:7334', 6)); // true
	// console.log(isIP('fe80::1%eth0', { version: 6 }));               // true（带区域标识）
	
	// // 压缩格式验证
	// console.log(isIP('2001:db8::1', '6'));           // true（双冒号压缩）
	// console.log(isIP('::1', 6));                     // true（本地环回简写）
	
	// // IPv4混合格式
	// console.log(isIP('::ffff:192.168.1.1', 6));     // true（IPv4映射地址）
	
	// // 非法IPv6案例
	// console.log(isIP('2001::gggg::1', 6));           // false（非法字符）
	// console.log(isIP('fe80::1%', 6));                // false（空区域标识）
	
	// // --------------------------
	// // 自动版本检测
	// // --------------------------
	
	// // 有效地址检测
	// console.log(isIP('172.16.254.1'));               // true（自动识别IPv4）
	// console.log(isIP('2001:db8:3333:4444:5555:6666:7777:8888')); // true（自动识别IPv6）
	
	// // 无效地址检测
	// console.log(isIP('192.168.1.256'));              // false（无效IPv4）
	// console.log(isIP('2001::gggg::1'));              // false（无效IPv6）
	
	// // --------------------------
	// // 特殊场景
	// // --------------------------
	
	// // 带前后空格处理
	// console.log(isIP(' 203.0.113.50 ', 4));          // true（自动trim）
	// console.log(isIP(' fe80::1%1 ', 6));             // true（自动trim）
	
	// // 非法版本指定
	// console.log(isIP('192.168.1.1', 5));             // false（不存在IPv5）
	// console.log(isIP('2001:db8::1', 'ipv6'));        // false（版本参数格式错误）
	
	// // --------------------------
	// // 边界案例
	// // --------------------------
	
	// // 最小/最大有效值
	// console.log(isIP('0.0.0.0', 4));                 // true
	// console.log(isIP('255.255.255.255', 4));         // true
	// console.log(isIP('0000:0000:0000:0000:0000:0000:0000:0000', 6)); // true
	
	// // 超长地址验证
	// console.log(isIP('192.168.1.1.1', 4));           // false（IPv4段数过多）
	// console.log(isIP('2001:db8:1:2:3:4:5:6:7', 6)); // false（IPv6段数过多）
	
	// const original = { color: 'red' };
	// const merged = merge({ ...original }, { color: 'blue', size: 'M' });
	
	// console.log('original', original);    // 输出: { color: 'red' } (保持不变)
	// console.log('merged', merged);      // 输出: { color: 'red', size: 'M' }
	
	
	type ColorType = {
		color?: string,
		size?: string,
	}
	
	const merged2 = merge({ color: 'red' }, { size: 'M' } as ColorType);
	console.log('merged2:::', merged2)
	
	// // 使用配置对象参数
	// console.log(isByteLength('hello', { min: 3, max: 7 } as ByteLengthOptions)); // true (5字节)
	// console.log(isByteLength('hello', { min: 6 } as ByteLengthOptions));       // false (5 < 6)
	// console.log(isByteLength('hello', { max: 4 } as ByteLengthOptions));       // false (5 > 4)
	
	// // 使用独立参数（旧式调用）
	// console.log(isByteLength('hello', 3, 7));            // true
	// console.log(isByteLength('hello', 6));                // false
	// console.log(isByteLength('hello', null, 4));     // false
	
	// =====================
	// 多字节字符处理示例
	// =====================
	
	// 中文字符（UTF-8 每个汉字3字节）
	// console.log(isByteLength('中国', { min: 6 }));         // true (2字 × 3字节 = 6)
	// console.log(isByteLength('中国', { max: 5 }));         // false (6 > 5)
	
	// // 表情符号（多数占用4字节）
	// console.log(isByteLength('🌟', { min: 4, max: 4 }));  // true
	// console.log(isByteLength('👨👩👧👦', { max: 15 })); // false (家庭表情占25字节)
	
	// // 混合字符集
	// console.log(isByteLength('aé🌟', { min: 7 }));        // true 
	
	// // URL编码字符
	// console.log(isByteLength('%20', { min: 3 }));         // true（实际字节长度3）
	// console.log(isByteLength('%E2%82%AC', { max: 3 }));  // false（欧元符号编码为3字节）
	
	// // 构造函数创建的正则表达式
	// console.log(isRegExp(new RegExp('hello')));      // true
	// console.log(isRegExp(new RegExp('\\d+', 'gi'))); // true
	
	// // 字面量正则表达式
	// console.log(isRegExp(/abc/));                    // true
	// console.log(isRegExp(/^[0-9]+$/gi));             // true
	
	// // 字符串（含正则格式字符串）
	// console.log(isRegExp('/abc/'));                  // false
	// console.log(isRegExp('new RegExp("abc")'));      // false
	
	// console.log(isEmail('"John"@example.com')) // false（实际有效）
	// console.log(isEmail('中国@例子.中国'))       // false（实际有效）
	
	// // 简单键值对
	// console.log(obj2url({ name: '张三', age: 25 }));
	// // "name=%E5%BC%A0%E4%B8%89&age=25"
	// // 包含布尔值
	// console.log(obj2url({ active: true, admin: false }));
	// // "active=true&admin=false"
	
	// // 数字处理
	// console.log(obj2url({ page: 1, limit: 10 }));
	// // "page=1&limit=10"

	// 基础验证
	// console.log("example.com =>", isValidDomain("example.com")); // true
	// console.log("sub.example.co.uk =>", isValidDomain("sub.example.co.uk")); // true
	
	// // 缺少TLD的情况
	// console.log("localhost =>", isValidDomain("localhost")); // false
	// console.log("localhost (不要求TLD) =>", isValidDomain("localhost", { requireTld: false } as DomainOptions)); // true
	
	// // 带结尾点号的情况
	// console.log("example.com. =>", isValidDomain("example.com.")); // false 
	// console.log("example.com. (允许结尾点号) =>", isValidDomain("example.com.", { allowTrailingDot: true }  as DomainOptions)); // true
	
	// // 带下划线的情况
	// console.log("my_site.com =>", isValidDomain("my_site.com")); // false
	// console.log("my_site.com (允许下划线) =>", isValidDomain("my_site.com", { allowUnderscore: true } as DomainOptions)); // true
	
	// // 非法字符测试
	// console.log("含有空格的域名 =>", isValidDomain("exa mple.com")); // false
	// console.log("含有!的域名 =>", isValidDomain("exa!mple.com")); // false
	
	// // 长度测试
	// const longPart = "a".repeat(64);
	// console.log(`超长部分 (${longPart.length}字符) =>`, isValidDomain(`${longPart}.com`)); // false

	// // 连字符测试
	// console.log("以连字符开头 =>", isValidDomain("-example.com")); // false
	// console.log("以连字符结尾 =>", isValidDomain("example-.com")); // false
	
	// // 国际化域名测试
	// console.log("中文域名 =>", isValidDomain("中国.中国")); // true
	// console.log("日文域名 =>", isValidDomain("ドメイン.テスト")); // true
	
	// // 基础格式验证
	// console.log("1. 标准日期格式验证:");
	// console.log("2023/12/31 =>", isDate("2023/12/31")); // true
	// console.log("1999-01-01 =>", isDate("1999-01-01")); // true
	// console.log("02.28.2023 =>", isDate("02.28.2023", { delimiters: ['.'], format: 'MM.DD.YYYY' } as DateOptions)); // true (自定义分隔符)
	
	// // 严格模式验证
	// console.log("2. 严格模式验证:");
	// console.log("严格匹配格式:", isDate("2023/02/28", { strictMode: true, format: "YYYY/MM/DD" }as DateOptions)); // true
	// console.log("长度不符:", isDate("2023/2/28", { strictMode: true, format: "YYYY/MM/DD" }as DateOptions)); // false
	// console.log("错误分隔符:", isDate("2023-02-28", { strictMode: true, format: "YYYY/MM/DD" }as DateOptions)); // false
	
	// // 两位年份处理
	// console.log("3. 两位年份验证:");
	// console.log("23 -> 2023:", isDate("23/12/31", { format: "YY/MM/DD" } as DateOptions)); // true → 2023-12-31
	// console.log("87 -> 1987:", isDate("87-01-01", { format: "YY-MM-DD" } as DateOptions)); // true → 1987-01-01
	// console.log("负数年份:", isDate("-100/12/31", { format: "YYYY/MM/DD" } as DateOptions)); // false
	
	// // 日期有效性验证
	// console.log("4. 无效日期检测:");
	// console.log("闰年2020-02-29:", isDate("2020/02/29")); // true
	// console.log("非闰年2023-02-29:", isDate("2023/02/29")); // false
	// console.log("月份溢出:", isDate("2023/13/01")); // false
	// console.log("日期溢出:", isDate("2023/12/32")); // false
	
	// // Date对象验证
	// console.log("5. Date对象验证:");
	// console.log("有效Date对象:", isDate(new Date())); // true
	// console.log("无效Date对象:", isDate(new Date("invalid")), new Date("invalid")); // false
	// console.log("严格模式Date对象:", isDate(new Date(), { strictMode: true } as DateOptions)); // false
	
	// // 自定义格式验证
	// console.log("6. 自定义格式测试:");
	// console.log("MM-DD-YYYY:", isDate("12-31-2023", { format: "MM-DD-YYYY" } as DateOptions)); // true
	// console.log("DD.MM.YY:", isDate("31.12.23", { format: "DD.MM.YY", delimiters: ['.'] } as DateOptions)); // true
	// console.log("中文分隔符:", isDate("2023年12月31日", { 
	//   format: "YYYY年MM月DD日",
	//   strictMode: true,
	//   delimiters: ['年', '月', '日']
	// } as DateOptions )); // true
	
	
	// 示例测试
	// console.log("示例1 标准HTTP URL:", isURL("http://example.com")); // true
	// console.log("示例2 需要端口时缺少端口:", isURL("https://example.com", { requirePort: true } as URLOptions)); // false
	// console.log("示例3 协议相对URL:", isURL("//example.com", { allowProtocolRelativeUrls: true })); // true
	console.log("示例4 IPv6地址:", isURL("http://[2001:db8::1]:8081", {})); // true
	// console.log("示例5 带认证信息被禁用:", isURL("user:pass@example.com", { disallowAuth: true })); // false
	console.log("示例6 查询参数被禁用:", isURL("http://example.com?q=test", { allowQueryComponents: true })); // false
	console.log("示例7 非字符串输入:", isURL(null, {})); // false
	console.log("示例8 邮件协议被排除:", isURL("mailto:test@example.com", {})); // false
	console.log("示例9 自定义协议:", isURL("ftp://files.example.com", { protocols: ["ftp"] })); // true
	console.log("示例10 白名单检查:", isURL("http://trusted.com", { hostWhitelist: ["trusted.com"] })); // true
	
	
	// #ifdef WEB
	// console.log('validator', validator.isURL())
	console.log("示例4 IPv6地址:", validator.isURL("http://[2001:db8::1]:8081", {})); // true
	console.log("示例6 查询参数被禁用:", validator.isURL("http://example.com?q=test", { allow_query_components: true })); // false
	console.log("示例8 邮件协议被排除:", validator.isURL("mailto:test@example.com", {})); // false
	console.log("示例9 自定义协议:", isURL("ftp://files.example.com", { protocols: ["ftp"] })); // true
	// #endif
	
	export default {
		
	}
</script>

<style>

</style>