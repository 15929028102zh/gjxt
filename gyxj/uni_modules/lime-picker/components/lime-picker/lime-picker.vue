<template>
	<view class="demo-block">
		<text class="demo-block__title-text ultra">Picker 选择器</text>
		<text class="demo-block__desc-text">用于一组预设数据中的选择。</text>
		<view class="demo-block__body">
			<view class="demo-block card">
				<text class="demo-block__title-text">基础用法</text>
				<view class="demo-block__body">
					<l-picker cancel-btn="取消" confirm-btn="确定" title="标题" :columns="cityOptions">
						<l-picker-item :options="cityOptions"></l-picker-item>
						<l-picker-item :options="cityOptions"></l-picker-item>
					</l-picker>
				</view>
			</view>
			<view class="demo-block card">
				<text class="demo-block__title-text">配合弹窗插件</text>
				<view class="demo-block__body">
					<button type="primary" @click="showPicker = true">弹窗</button>
					<l-popup v-model="showPicker" position="bottom">
						<l-picker cancel-btn="取消" confirm-btn="确定" :columns="seasonColumns" @cancel="showPicker = false" @confirm="showPicker = false"></l-picker>
					</l-popup>
				</view>
			</view>
			<view class="demo-block card">
				<text class="demo-block__title-text">双向绑定: {{citys.join(',')}}</text>
				<view class="demo-block__body">
					<l-picker v-model="citys" cancel-btn="取消" confirm-btn="确定">
						<l-picker-item :options="cityOptions"></l-picker-item>
					</l-picker>
				</view>
			</view>
			<view class="demo-block card">
				<text class="demo-block__title-text">多列</text>
				<view class="demo-block__body">
					<l-picker cancel-btn="取消" confirm-btn="确定" :columns="seasonColumns" @confirm="onConfirm">
					</l-picker>
				</view>
			</view>
			<view class="demo-block card">
				<text class="demo-block__title-text">多列联动</text>
				<view class="demo-block__body">
					<l-picker cancel-btn="取消" confirm-btn="确定" @confirm="onConfirm" @pick="onColumnChange">
						<l-picker-item :options="provinces"></l-picker-item>
						<l-picker-item :options="cities"></l-picker-item>
						<l-picker-item :options="counties"></l-picker-item>
					</l-picker>
				</view>
			</view>
			<view class="demo-block card">
				<text class="demo-block__title-text">级联选择</text>
				<view class="demo-block__body">
					<l-cascade :columns="cascadeColumns" cancel-btn="取消" confirm-btn="确定" @confirm="onConfirm">
					</l-cascade>
				</view>
			</view>
			<view class="demo-block card">
				<text class="demo-block__title-text">加载状态</text>
				<view class="demo-block__body">
					<l-picker loading cancel-btn="取消" confirm-btn="确定" @confirm="onConfirm" @pick="onColumnChange">
					</l-picker>
				</view>
			</view>
			<view class="demo-block card">
				<text class="demo-block__title-text">空状态</text>
				<view class="demo-block__body">
					<l-picker cancel-btn="取消" confirm-btn="确定" @confirm="onConfirm" @pick="onColumnChange">
						<template #empty>
							<l-empty description="没有数据" />
						</template>
					</l-picker>
				</view>
			</view>
		</view>
	</view>
</template>
<script>
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
	const getOptions = (obj, filter) => {
		const res = Object.keys(obj).map((key) => {
			return {
				value: `${key}`,
				label: `${obj[key]}`
			}
		})
		if (filter) {
			return res.filter(filter)
		}
		return res
	}
	const match = (v1, v2, size) => {
		return  v1.slice(0, size) == v2.slice(0, size)
	};
	const getCounties = (cityValue )  => {
		return getOptions(areaList.counties, (county) => match(county.value, cityValue, 4));
	};
	const getCities = (provinceValue) => {
		const cities = getOptions(areaList.cities, (city) => match(city.value, provinceValue, 2));
		const counties = getCounties(cities[0].value);
		return [cities, counties];
	};
	const cascadeColumns = [
		{
			label: '浙江',
			value: 'Zhejiang',
			children: [
				{
					label: '杭州',
					value: 'Hangzhou',
					children: [
						{ label: '西湖区', value: 'Xihu' },
						{ label: '余杭区', value: 'Yuhang' },
					],
				},
				{
					label: '温州',
					value: 'Wenzhou',
					children: [
						{ label: '鹿城区', value: 'Lucheng' },
						{ label: '瓯海区', value: 'Ouhai' },
					],
				},
			],
		},
		{
			label: '福建',
			value: 'Fujian',
			children: [
				{
					label: '福州',
					value: 'Fuzhou',
					children: [
						{ label: '鼓楼区', value: 'Gulou' },
						{ label: '台江区', value: 'Taijiang' },
					],
				},
				{
					label: '厦门',
					value: 'Xiamen',
					children: [
						{ label: '思明区', value: 'Siming' },
						{ label: '海沧区', value: 'Haicang' },
					],
				},
			],
		},
	];
	export default {
		data() {
			const _year = 2018;
			const _years = []
			for (let i = 2000; i <= _year; i++) {
				_years.push({
					label: `${i}`,
					value: `${i}`
				})
			}
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
			] 
			return {
				showPicker: false,
				citys: ['上海市'],
				cityOptions: [
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
					{
						label: '杭州市',
						value: '杭州市',
					},
					{
						label: '成都市',
						value: '成都市',
					},
					{
						label: '长沙市',
						value: '长沙市',
					},
				],
				seasonColumns: [_years, seasonOptions],
				provinces:[],
				cities:[],
				counties:[],
				options:[],
				cascadeColumns
			}
		},
		methods: {
			onConfirm(context) {
				console.log('context', context)
			},
			onColumnChange({values, column, index}) {
				if (column == 0) {
					// 更改省份
					const data = getCities(this.provinces[index].value);
					this.cities = data[0];
					this.counties = data[1];
					this.options = [this.provinces, this.cities, this.counties]
				}
				
				if (column == 1) {
					// 更改城市
					this.counties = getCounties(this.cities[index].value);
					this.options[2] = this.counties
				}
				
				if (column == 2) {
					// 更改区县
				}
			}
		},
		mounted() {
			this.provinces = getOptions(areaList.provinces, null)
			const data = getCities(this.provinces[0].value);
			this.cities = data[0];
			this.counties = data[1];
			this.options = [this.provinces, this.cities, this.counties]
			
			
			
			setTimeout(()=>{
				this.citys = ['广州市']
			},3000)
		}
	}
</script>
<style lang="scss">
	.btn {
		margin-bottom: 20rpx;
		margin-right: 20rpx;
		align-self: center;
	}

	.demo-block {
		margin: 32px 0 0;

		// overflow: visible;
		&.card {
			// background-color: white;
			padding: 30rpx;
			margin-bottom: 20rpx;
		}

		&__title {
			margin: 0;
			margin-top: 8px;

			&-text {
				color: rgba(0, 0, 0, 0.6);
				font-weight: 400;
				font-size: 14px;
				line-height: 16px;
				display: flex;
				margin-left: 5px;

				&.large {
					color: rgba(0, 0, 0, 0.9);
					font-size: 18px;
					font-weight: 700;
					line-height: 26px;
					margin-left: 20px;
				}

				&.ultra {
					color: rgba(0, 0, 0, 0.9);
					font-size: 24px;
					font-weight: 700;
					line-height: 32px;
					padding-left: 15px ;
				}
			}
		}

		&__desc-text {
			color: rgba(0, 0, 0, 0.6);
			margin: 8px 16px 0 0;
			font-size: 14px;
			line-height: 22px;
			margin-left: 20px;
		}

		&__body {
			margin: 16px 0;
			overflow: visible;

			.demo-block {
				// margin-top: 0px;
				margin: 0;
				overflow: visible;
			}
		}
	}
</style>