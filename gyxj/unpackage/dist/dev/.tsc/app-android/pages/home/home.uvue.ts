import _easycom_tui_xechars from '@/uni_modules/tui-xechars/components/tui-xechars/tui-xechars.uvue'
import { ref, onMounted, onUnmounted, nextTick, reactive } from 'vue'
	import { getApiUrl } from '@/config/api.uts'
	import { isWeeklyAdmin } from '@/utils/permission.uts'
	import { refreshUserInfo } from '@/utils/user.uts'
	import { TuiCharts, TuiDrawCharts } from '@/uni_modules/tui-xechars'

	// 定义统计数据类型
	type StatsData = { __$originalPosition?: UTSSourceMapPosition<"StatsData", "pages/home/home.uvue", 112, 7>;
		total: number
		pending: number
		completed: number
		reviewed: number
	}

	// 定义uni.request响应类型
	type UniRequestResponse = { __$originalPosition?: UTSSourceMapPosition<"UniRequestResponse", "pages/home/home.uvue", 120, 7>;
		statusCode: number
		data: any
		header: any
		cookies?: string[]
	}


	
	// 用户信息
	
const __sfc__ = defineComponent({
  __name: 'home',
  setup(__props) {
const __ins = getCurrentInstance()!;
const _ctx = __ins.proxy as InstanceType<typeof __sfc__>;
const _cache = __ins.renderCache;

	const userInfo = ref({} as UTSJSONObject)
	const userName = ref('用户') // 简化的用户姓名变量
	const currentDate = ref('加载中...')

	// 统计数据
	const weeklyStats = ref({
		total: 45,
		pending: 12,
		completed: 28,
		reviewed: 5
	} as StatsData)

	const monthlyStats = ref({
		total: 156,
		pending: 23,
		completed: 108,
		reviewed: 25
	} as StatsData)

	// 图表实例
	let weeklyChartInstance : TuiCharts | null = null
	let monthlyChartInstance : TuiCharts | null = null
	
	// 图表配置选项
	const weeklyChartOption = ref({} as UTSJSONObject)
	const monthlyChartOption = ref({} as UTSJSONObject)
	
	// 图表引用
	const weeklyChart = ref(null)
	const monthlyChart = ref(null)
	
	// 趋势图表数据类型
	
	// 滚动相关
	const scrollTop = ref(0)
	const containerHeight = ref(0)
	
	// 下拉刷新相关
	const isRefreshing = ref(false)




	// 更新周检图表数据
	const updateWeeklyChart = () : void => {
		if (weeklyChartInstance != null) {
			const weeklyPieChart = weeklyChartInstance!.getDrawCharts(0)
			if (weeklyPieChart != null) {
				weeklyPieChart.update({
					series: [
						{
							name: '待整改',
							data: [
								{
									value: weeklyStats.value.pending
								}
							]
						},
						{
							name: '已整改',
							data: [
								{
									value: weeklyStats.value.completed
								}
							]
						},
						{
							name: '已复查',
							data: [
								{
									value: weeklyStats.value.reviewed
								}
							]
						}
					]
				})
			}
		}
	}

	// 更新月检图表数据
	const updateMonthlyChart = () : void => {
		if (monthlyChartInstance != null) {
			const monthlyPieChart = monthlyChartInstance!.getDrawCharts(0)
			if (monthlyPieChart != null) {
				monthlyPieChart.update({
					series: [
						{
							name: '待整改',
							data: [
								{
									value: monthlyStats.value.pending
								}
							]
						},
						{
							name: '已整改',
							data: [
								{
									value: monthlyStats.value.completed
								}
							]
						},
						{
							name: '已复查',
							data: [
								{
									value: monthlyStats.value.reviewed
								}
							]
						}
					]
				})
			}
		}
	}

	// 加载周检统计数据
	const loadWeeklyStats = async () : Promise<void> => {
		try {
			const token = uni.getStorageSync('token')
			
			// 获取用户信息：部门 ID + 周检管理员标志
			let departmentId = ''
			// 周检域管理员用 isWeeklyAdmin()(对齐 Web permission.js:老 isAdmin=1 或 isWeeklyAdmin=1)
			const isAdmin = isWeeklyAdmin()
			const userInfoData = uni.getStorageSync('userInfo')
			const userInfoStr = userInfoData != null ? userInfoData as string : ''
			if (userInfoStr != null && userInfoStr != '' && userInfoStr != 'null' && userInfoStr != 'undefined') {
				try {
					const parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/home/home.uvue:260") as UTSJSONObject
					departmentId = parsedUserInfo.getString('deptId') ?? ''
				} catch (error) {
					console.error('用户信息JSON解析失败:', error, " at pages/home/home.uvue:263")
				}
			}
			
			// 解析响应数据
			let pending = 0, completed = 0, reviewed = 0, total = 0
			let completedRequests = 0
			const totalRequests = 4
			
			// 更新统计数据
			const updateWeeklyStats = () => {
				weeklyStats.value = {
					total: total,
					pending: pending,
					completed: completed,
					reviewed: reviewed
				} as StatsData
				
				// 在数据更新后重新更新图表
				updateWeeklyChart()
			}
			
			// 如果没有token，使用模拟数据
			if (token == null || token == '') {
				weeklyStats.value = {
					total: 45,
					pending: 12,
					completed: 28,
					reviewed: 5
				} as StatsData
				updateWeeklyChart()
				return
			}
			
			// 构建部门过滤参数 - 管理员用户不需要部门过滤
			const deptParam = (!isAdmin && departmentId != null && departmentId != '') ? `&departmentId=${departmentId}` : ''
			
			// 获取待整改数量（状态为0）- 使用分页查询API获取准确数量
			const pendingUrl = getApiUrl(`/inspection-record/page?current=1&size=1&status=0${(!isAdmin && departmentId != null && departmentId != '') ? '&departmentId=' + departmentId : ''}`)
			uni.request({
				url: pendingUrl,
				method: 'GET',
				header: {
					'Authorization': `Bearer ${token}`
				},
				success: (res) => {
					if (res.statusCode === 200 && res.data != null) {
						const response = res.data as UTSJSONObject
						if ((response.getNumber('code') ?? 0) === 200) {
							const data = response.getJSON('data') as UTSJSONObject
							pending = data.getNumber('total') ?? 0
						}
					}
					completedRequests++
					if (completedRequests === totalRequests) {
						updateWeeklyStats()
					}
				},
				fail: (error) => {
					console.error('获取待整改数量失败:', error, " at pages/home/home.uvue:322")
					completedRequests++
					if (completedRequests === totalRequests) {
						updateWeeklyStats()
					}
				}
			})
			
			// 获取已整改数量（状态为1）- 使用分页查询API获取准确数量
			const completedUrl = getApiUrl(`/inspection-record/page?current=1&size=1&status=1${(!isAdmin && departmentId != null && departmentId != '') ? '&departmentId=' + departmentId : ''}`)
			uni.request({
				url: completedUrl,
				method: 'GET',
				header: {
					'Authorization': `Bearer ${token}`
				},
				success: (res) => {
					if (res.statusCode === 200 && res.data != null) {
						const response = res.data as UTSJSONObject
						if ((response.getNumber('code') ?? 0) === 200) {
							const data = response.getJSON('data') as UTSJSONObject
							completed = data.getNumber('total') ?? 0
						}
					}
					completedRequests++
					if (completedRequests === totalRequests) {
						updateWeeklyStats()
					}
				},
				fail: (error) => {
					console.error('获取已整改数量失败:', error, " at pages/home/home.uvue:352")
					completedRequests++
					if (completedRequests === totalRequests) {
						updateWeeklyStats()
					}
				}
			})
			
			// 获取已复查数量（状态为2）- 使用分页查询API获取准确数量
			const reviewedUrl = getApiUrl(`/inspection-record/page?current=1&size=1&status=2${(!isAdmin && departmentId != null && departmentId != '') ? '&departmentId=' + departmentId : ''}`)
			uni.request({
				url: reviewedUrl,
				method: 'GET',
				header: {
					'Authorization': `Bearer ${token}`
				},
				success: (res) => {
					if (res.statusCode === 200 && res.data != null) {
						const response = res.data as UTSJSONObject
						if ((response.getNumber('code') ?? 0) === 200) {
							const data = response.getJSON('data') as UTSJSONObject
							reviewed = data.getNumber('total') ?? 0
						}
					}
					completedRequests++
					if (completedRequests === totalRequests) {
						updateWeeklyStats()
					}
				},
				fail: (error) => {
					console.error('获取已复查数量失败:', error, " at pages/home/home.uvue:382")
					completedRequests++
					if (completedRequests === totalRequests) {
						updateWeeklyStats()
					}
				}
			})
			
			// 获取总记录数量 - 使用分页查询API获取准确数量
			const totalUrl = getApiUrl(`/inspection-record/page?current=1&size=1${(!isAdmin && departmentId != null && departmentId != '') ? '&departmentId=' + departmentId : ''}`)
			uni.request({
				url: totalUrl,
				method: 'GET',
				header: {
					'Authorization': `Bearer ${token}`
				},
				success: (res) => {
					if (res.statusCode === 200 && res.data != null) {
						const response = res.data as UTSJSONObject
						if ((response.getNumber('code') ?? 0) === 200) {
							const data = response.getJSON('data') as UTSJSONObject
							total = data.getNumber('total') ?? 0
						}
					}
					completedRequests++
					if (completedRequests === totalRequests) {
						updateWeeklyStats()
					}
				},
				fail: (error) => {
					console.error('获取总记录数量失败:', error, " at pages/home/home.uvue:412")
					completedRequests++
					if (completedRequests === totalRequests) {
						updateWeeklyStats()
					}
				}
			})
			
		} catch (error) {
			console.error('加载周检统计数据失败:', error, " at pages/home/home.uvue:421")
			// 使用模拟数据作为后备
			weeklyStats.value = {
				total: 45,
				pending: 12,
				completed: 28,
				reviewed: 5
			} as StatsData
			updateWeeklyChart()
		}
	}

	// 加载月检统计数据
    const loadMonthlyStats = async () : Promise<void> => {
        try {
            const token = uni.getStorageSync('token')
            // 部门隔离由后端按登录用户强制处理，前端不再传部门参数(对齐 Web:月检统计无客户端权限分支)
            const deptParam = ''
            let pending = 0, completed = 0, reviewed = 0
            let finished = 0
            const totalReq = 3
            const updateMonthly = () => {
                monthlyStats.value = {
                    total: pending + completed + reviewed,
                    pending: pending,
                    completed: completed,
                    reviewed: reviewed
                } as StatsData
                updateMonthlyChart()
            }
            const req = (status: number, assign: (n:number)=>void) => {
                uni.request({
                    url: getApiUrl(`/monthly-inspection-record/page?current=1&size=1&recordStatus=${status}${deptParam}`),
                    method: 'GET',
                    header: { 'Authorization': `Bearer ${token}` },
                    success: (res) => {
                        const resp = res.data as UTSJSONObject
                        if (res.statusCode === 200 && (resp.getNumber('code') ?? 0) === 200) {
                            const d = resp.getJSON('data') as UTSJSONObject
                            assign(d.getNumber('total') ?? 0)
                        }
                        finished++
                        if (finished === totalReq) updateMonthly()
                    },
                    fail: (_) => { finished++; if (finished === totalReq) updateMonthly() }
                })
            }
            req(0, (n: number) => { pending = n })
            req(1, (n: number) => { completed = n })
            req(2, (n: number) => { reviewed = n })
        } catch (error) {
            console.error('加载月检统计数据失败:', error, " at pages/home/home.uvue:472")
            monthlyStats.value = { total: 0, pending: 0, completed: 0, reviewed: 0 } as StatsData
            updateMonthlyChart()
        }
    }



	// 检查登录状态
	const checkLogin = () : boolean => {
		const token = uni.getStorageSync('token')
		if (token == null || token == '') {
			console.log('未登录，跳转到登录页面', " at pages/home/home.uvue:484")
			uni.reLaunch({
				url: '/pages/login/login'
			})
			return false
		}
		return true
	}
	
	// 加载用户信息
	const loadUserInfo = () : void => {
		console.log('=== 开始加载用户信息 ===', " at pages/home/home.uvue:495")
		
		// 检查本地存储
		const userInfoStr = uni.getStorageSync('userInfo') as string
		console.log('本地存储原始数据:', userInfoStr, " at pages/home/home.uvue:499")
		
		if (userInfoStr != null && userInfoStr != '' && userInfoStr != 'null' && userInfoStr != 'undefined') {
			try {
				const parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/home/home.uvue:503") as UTSJSONObject
				console.log('解析后的对象:', parsedUserInfo, " at pages/home/home.uvue:504")
				
				// 检查realName字段
				const realName = parsedUserInfo.getString('realName')
				console.log('realName字段值:', realName, " at pages/home/home.uvue:508")
				console.log('realName类型:', typeof realName, " at pages/home/home.uvue:509")
				
				// 设置用户信息
				userInfo.value = parsedUserInfo
				// 同时设置简化的用户姓名
				const realNameValue = parsedUserInfo.getString('realName')
				userName.value = realNameValue != null ? realNameValue : '用户'
				console.log('设置后的 userInfo.value:', userInfo.value, " at pages/home/home.uvue:516")
				console.log('设置后的 userName.value:', userName.value, " at pages/home/home.uvue:517")
				
				// 验证设置结果
				const testRealName = userInfo.value.getString('realName')
				console.log('验证: 设置后的realName:', testRealName, " at pages/home/home.uvue:521")
				
				// 强制触发界面更新
				nextTick(() => {
					console.log('界面更新后检查:', userInfo.value.getString('realName'), " at pages/home/home.uvue:525")
					// 强制触发响应式更新
					userInfo.value = {...userInfo.value}
				})
				
			} catch (error) {
				console.error('用户信息JSON解析失败:', error, " at pages/home/home.uvue:531")
				userInfo.value = {
					realName: '解析失败用户'
				} as UTSJSONObject
			}
		} else {
			console.log('本地存储中没有有效的用户信息', " at pages/home/home.uvue:537")
			// 设置默认用户信息
			userInfo.value = {
				realName: '默认用户',
				avatar: ''
			} as UTSJSONObject
			userName.value = '默认用户'
		}
		
		console.log('=== 用户信息加载完成 ===', " at pages/home/home.uvue:546")
	}
	
	// 初始化当前日期
	const initCurrentDate = () : void => {
		const now = new Date()
		const year = now.getFullYear()
		const month = (now.getMonth() + 1).toString().padStart(2, '0')
		const day = now.getDate().toString().padStart(2, '0')
		const hours = now.getHours().toString().padStart(2, '0')
		const minutes = now.getMinutes().toString().padStart(2, '0')
		currentDate.value = `${year}-${month}-${day} ${hours}:${minutes}`
	}
	
	// 定时器ID
	const timeInterval = ref<number | null>(null)
	
	// 开始时间更新定时器
	const startTimeUpdate = () : void => {
		initCurrentDate()
		timeInterval.value = setInterval(() => {
			initCurrentDate()
		}, 60000) // 每分钟更新一次
	}
	
	// 停止时间更新定时器
	const stopTimeUpdate = () : void => {
		const intervalId = timeInterval.value
		if (intervalId !== null) {
			clearInterval(intervalId)
			timeInterval.value = null
		}
	}

	// 初始化容器高度
	const initContainerHeight = () : void => {
		uni.getSystemInfo({
			success: (res) => {
				containerHeight.value = res.windowHeight
			}
		})
	}
	
	// 重新加载所有数据
	const reloadAllData = async () : Promise<void> => {
		console.log('重新加载所有数据', " at pages/home/home.uvue:591")
		
		try {
			// 重新初始化时间
			initCurrentDate()
			
			// 重新加载用户信息
			loadUserInfo()
			
			// 并发加载统计数据
			await Promise.all([
				loadWeeklyStats(),
				new Promise<void>((resolve) => {
					loadMonthlyStats()
					// 给月检数据加载一个短暂的延时
					setTimeout(() => {
						resolve()
					}, 800)
				})
			])
			
			console.log('所有数据加载完成', " at pages/home/home.uvue:612")
			
		} catch (error) {
			console.error('数据加载失败:', error, " at pages/home/home.uvue:615")
		} finally {
			// 无论成功还是失败，都要关闭刷新动画
			setTimeout(() => {
				console.log('关闭刷新动画', " at pages/home/home.uvue:619")
				isRefreshing.value = false
			}, 500) // 稍微延迟一下，让用户看到加载效果
		}
	}
	
	// 下拉刷新事件处理
	const onRefresh = () : void => {
		console.log('开始下拉刷新', " at pages/home/home.uvue:627")
		isRefreshing.value = true
		
		// 重新加载数据
		reloadAllData()
	}
	
	// 刷新恢复事件处理
	const onRefreshRestore = () : void => {
		console.log('刷新恢复', " at pages/home/home.uvue:636")
		isRefreshing.value = false
	}
	
	// 页面挂载时初始化
	onMounted(() => {
		// 检查登录状态
		const loginStatus = checkLogin()
		if (loginStatus == false) {
			return
		}
		
		// 立即初始化日期显示
		initCurrentDate()

		// 启动时间更新
		startTimeUpdate()

		// 初始化容器高度
		initContainerHeight()

		// 先刷新服务端 userInfo(部门/角色可能已被后台调整)，再按最新 deptId 加载用户信息与统计
		refreshUserInfo().then(() => {
			loadUserInfo()
			// 等待DOM渲染完成后加载数据
			nextTick(() => {
				// 加载数据，图表会在initFinished回调中初始化
				loadWeeklyStats()
				loadMonthlyStats()
			})
		})
	})
	
	// 页面卸载时清理
	onUnmounted(() => {
		stopTimeUpdate()
	})





	// 图表初始化回调函数
	const onWeeklyChartInit = (chartInstance : TuiCharts) : void => {
		weeklyChartInstance = chartInstance
		
		// 创建饼图
		const pieOption = { __$originalPosition: new UTSSourceMapPosition("pieOption", "pages/home/home.uvue", 683, 9), 
			type: 'pie',
			animation: true,
			dataLabel: true,
			fontSize: 12,
			padding: [15, 15, 15, 15],
			title: {
				name: '周检统计分布',
				fontSize: 14,
				color: '#2c3e50',
				offsetY: 5
			},
			extra: {
				pie: {
					activeOpacity: 0.5,
					activeRadius: 8,
					offsetAngle: 0,
					labelWidth: 15,
					border: true,
					borderWidth: 2,
					borderColor: '#FFFFFF',
					linearType: 'custom'
				},
				tooltip: {
					show: true
				}
			},
			series: [
				{
					name: '待整改',
					data: [
						{
							value: weeklyStats.value.pending
						}
					]
				},
				{
					name: '已整改',
					data: [
						{
							value: weeklyStats.value.completed
						}
					]
				},
				{
					name: '已复查',
					data: [
						{
							value: weeklyStats.value.reviewed
						}
					]
				}
			],
			xAxis: {
				disableGrid: true
			},
			yAxis: {
				data: [
					{
						min: 0
					}
				]
			}
		} as UTSJSONObject
		
		const weeklyPieChart = chartInstance.add(0, pieOption)
		weeklyPieChart.setCoordinates({ height: 280, width: 260, xOffset: 15, yOffset: 35 })
		weeklyPieChart.draw()
	}
	
	const onMonthlyChartInit = (chartInstance : TuiCharts) : void => {
		monthlyChartInstance = chartInstance
		
		// 创建饼图
		const pieOption = { __$originalPosition: new UTSSourceMapPosition("pieOption", "pages/home/home.uvue", 757, 9), 
			type: 'pie',
			animation: true,
			dataLabel: true,
			fontSize: 12,
			padding: [15, 15, 15, 15],
			title: {
				name: '月检统计分布',
				fontSize: 14,
				color: '#2c3e50',
				offsetY: 5
			},
			extra: {
				pie: {
					activeOpacity: 0.5,
					activeRadius: 8,
					offsetAngle: 0,
					labelWidth: 15,
					border: true,
					borderWidth: 2,
					borderColor: '#FFFFFF',
					linearType: 'custom'
				},
				tooltip: {
					show: true
				}
			},
			series: [
				{
					name: '待整改',
					data: [
						{
							value: monthlyStats.value.pending
						}
					]
				},
				{
					name: '已整改',
					data: [
						{
							value: monthlyStats.value.completed
						}
					]
				},
				{
					name: '已复查',
					data: [
						{
							value: monthlyStats.value.reviewed
						}
					]
				}
			],
			xAxis: {
				disableGrid: true
			},
			yAxis: {
				data: [
					{
						min: 0
					}
				]
			}
		} as UTSJSONObject
		
		const monthlyPieChart = chartInstance.add(0, pieOption)
		monthlyPieChart.setCoordinates({ height: 280, width: 260, xOffset: 15, yOffset: 35 })
		monthlyPieChart.draw()
	}
	

	// 最终的跳转方法
	const goToWeeklyPending = () : void => {
		uni.navigateTo({
			url: '/pages/weekly/weekly?status=pending'
		})
	}

	const goToWeeklyCompleted = () : void => {
		uni.navigateTo({
			url: '/pages/weekly/weekly?status=completed'
		})
	}

	const goToWeeklyReviewed = () : void => {
		uni.navigateTo({
			url: '/pages/weekly/weekly?status=reviewed'
		})
	}

	const goToMonthlyPending = () : void => {
		uni.navigateTo({
			url: '/pages/monthly/monthly?status=pending'
		})
	}

	const goToMonthlyCompleted = () : void => {
		uni.navigateTo({
			url: '/pages/monthly/monthly?status=completed'
		})
	}

	const goToMonthlyReviewed = () : void => {
		uni.navigateTo({
			url: '/pages/monthly/monthly?status=reviewed'
		})
	}

return (): any | null => {

const _component_tui_xechars = resolveEasyComponent("tui-xechars",_easycom_tui_xechars)

  return _cE("scroll-view", _uM({
    class: "container",
    "scroll-y": "true",
    "scroll-top": scrollTop.value,
    style: _nS(_uM({height: containerHeight.value + 'px'})),
    "enable-back-to-top": "true",
    "refresher-enabled": "true",
    "refresher-triggered": isRefreshing.value,
    "refresher-default-style": "black",
    "refresher-background": "#f5f5f5",
    onRefresherrefresh: onRefresh,
    onRefresherrestore: onRefreshRestore
  }), [
    _cE("view", _uM({ class: "header" }), [
      _cE("view", _uM({ class: "welcome" }), [
        _cE("text", _uM({ class: "greeting" }), "欢迎回来，" + _tD(userName.value), 1 /* TEXT */),
        _cE("text", _uM({ class: "app-name" }), _tD(currentDate.value), 1 /* TEXT */)
      ]),
      _cE("image", _uM({
        class: "avatar",
        src: (userInfo.value.getString('avatar') ?? '') != '' ? (userInfo.value.getString('avatar') ?? '') : '/static/default-avatar.svg',
        mode: "aspectFill"
      }), null, 8 /* PROPS */, ["src"])
    ]),
    _cE("view", _uM({ class: "section" }), [
      _cE("view", _uM({ class: "section-title" }), [
        _cE("text", _uM({ class: "title-text" }), "周检统计")
      ]),
      _cE("view", _uM({ class: "stats-container" }), [
        _cE("view", _uM({
          class: "stat-card",
          onClick: goToWeeklyPending
        }), [
          _cE("text", _uM({ class: "stat-number" }), _tD(weeklyStats.value.pending), 1 /* TEXT */),
          _cE("text", _uM({ class: "stat-label" }), "待整改数")
        ]),
        _cE("view", _uM({
          class: "stat-card",
          onClick: goToWeeklyCompleted
        }), [
          _cE("text", _uM({ class: "stat-number" }), _tD(weeklyStats.value.completed), 1 /* TEXT */),
          _cE("text", _uM({ class: "stat-label" }), "已整改数")
        ]),
        _cE("view", _uM({
          class: "stat-card",
          onClick: goToWeeklyReviewed
        }), [
          _cE("text", _uM({ class: "stat-number" }), _tD(weeklyStats.value.reviewed), 1 /* TEXT */),
          _cE("text", _uM({ class: "stat-label" }), "已复查数")
        ])
      ]),
      _cE("view", _uM({ class: "chart-container" }), [
        _cE("view", _uM({ class: "chart-title" }), [
          _cE("text", _uM({ class: "chart-title-text" }), "周检记录总数: " + _tD(weeklyStats.value.total), 1 /* TEXT */)
        ]),
        _cE("view", _uM({ class: "chart-wrapper" }), [
          _cV(_component_tui_xechars, _uM({
            ref_key: "weeklyChart",
            ref: weeklyChart,
            style: _nS(_uM({"height":"350px","width":"100%","min-width":"280px"})),
            class: "chart-canvas",
            onInitFinished: onWeeklyChartInit
          }), null, 8 /* PROPS */, ["style"])
        ])
      ])
    ]),
    _cE("view", _uM({ class: "section" }), [
      _cE("view", _uM({ class: "section-title" }), [
        _cE("text", _uM({ class: "title-text" }), "月检统计")
      ]),
      _cE("view", _uM({ class: "stats-container" }), [
        _cE("view", _uM({
          class: "stat-card",
          onClick: goToMonthlyPending
        }), [
          _cE("text", _uM({ class: "stat-number" }), _tD(monthlyStats.value.pending), 1 /* TEXT */),
          _cE("text", _uM({ class: "stat-label" }), "待整改数")
        ]),
        _cE("view", _uM({
          class: "stat-card",
          onClick: goToMonthlyCompleted
        }), [
          _cE("text", _uM({ class: "stat-number" }), _tD(monthlyStats.value.completed), 1 /* TEXT */),
          _cE("text", _uM({ class: "stat-label" }), "已整改数")
        ]),
        _cE("view", _uM({
          class: "stat-card",
          onClick: goToMonthlyReviewed
        }), [
          _cE("text", _uM({ class: "stat-number" }), _tD(monthlyStats.value.reviewed), 1 /* TEXT */),
          _cE("text", _uM({ class: "stat-label" }), "已复查数")
        ])
      ]),
      _cE("view", _uM({ class: "chart-container" }), [
        _cE("view", _uM({ class: "chart-title" }), [
          _cE("text", _uM({ class: "chart-title-text" }), "月检记录总数: " + _tD(monthlyStats.value.total), 1 /* TEXT */)
        ]),
        _cE("view", _uM({ class: "chart-wrapper" }), [
          _cV(_component_tui_xechars, _uM({
            ref_key: "monthlyChart",
            ref: monthlyChart,
            style: _nS(_uM({"height":"350px","width":"100%","min-width":"280px"})),
            class: "chart-canvas",
            onInitFinished: onMonthlyChartInit
          }), null, 8 /* PROPS */, ["style"])
        ])
      ])
    ])
  ], 44 /* STYLE, PROPS, NEED_HYDRATION */, ["scroll-top", "refresher-triggered"])
}
}

})
export default __sfc__
const GenPagesHomeHomeStyles = [_uM([["container", _pS(_uM([["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "150rpx"], ["paddingLeft", "30rpx"], ["backgroundImage", "linear-gradient(180deg, #eff6ff 0%, #dbeafe 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxSizing", "border-box"], ["minHeight", "1200rpx"]]))], ["header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", "30rpx"], ["paddingRight", "25rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "25rpx"], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "24rpx"], ["borderTopRightRadius", "24rpx"], ["borderBottomRightRadius", "24rpx"], ["borderBottomLeftRadius", "24rpx"], ["marginBottom", "30rpx"], ["boxShadow", "0 12rpx 36rpx rgba(30, 64, 175, 0.18)"], ["position", "relative"], ["overflow", "hidden"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["content::before", "''"], ["position::before", "absolute"], ["top::before", 0], ["left::before", 0], ["width::before", "8rpx"], ["height::before", "100%"], ["backgroundImage::before", "linear-gradient(180deg, #1e40af, #0ea5e9)"], ["backgroundColor::before", "rgba(0,0,0,0)"]]))], ["welcome", _pS(_uM([["display", "flex"], ["flexDirection", "column"], ["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["zIndex", 2], ["position", "relative"], ["paddingLeft", "12rpx"]]))], ["greeting", _pS(_uM([["color", "#0a2540"], ["fontSize", "36rpx"], ["fontWeight", 700], ["marginBottom", "10rpx"], ["letterSpacing", "1rpx"]]))], ["app-name", _pS(_uM([["color", "#475569"], ["fontSize", "26rpx"], ["fontWeight", 500], ["letterSpacing", "0.3rpx"]]))], ["avatar", _pS(_uM([["width", "100rpx"], ["height", "100rpx"], ["borderTopLeftRadius", "50rpx"], ["borderTopRightRadius", "50rpx"], ["borderBottomRightRadius", "50rpx"], ["borderBottomLeftRadius", "50rpx"], ["backgroundColor", "#eff6ff"], ["overflow", "hidden"], ["borderTopWidth", "3rpx"], ["borderRightWidth", "3rpx"], ["borderBottomWidth", "3rpx"], ["borderLeftWidth", "3rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["boxShadow", "0 6rpx 20rpx rgba(30, 64, 175, 0.12)"], ["zIndex", 2], ["position", "relative"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["transform:active", "scale(0.95)"]]))], ["section", _pS(_uM([["marginBottom", "40rpx"], ["paddingTop", 0], ["paddingRight", "30rpx"], ["paddingBottom", 0], ["paddingLeft", "30rpx"]]))], ["section-title", _pS(_uM([["marginBottom", "25rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "space-between"], ["position", "relative"], ["content::after", "''"], ["position::after", "absolute"], ["bottom::after", "-10rpx"], ["left::after", 0], ["width::after", "80rpx"], ["height::after", "4rpx"], ["backgroundImage::after", "linear-gradient(90deg, #1e40af, #0ea5e9)"], ["backgroundColor::after", "rgba(0,0,0,0)"], ["borderTopLeftRadius::after", "2rpx"], ["borderTopRightRadius::after", "2rpx"], ["borderBottomRightRadius::after", "2rpx"], ["borderBottomLeftRadius::after", "2rpx"]]))], ["title-text", _pS(_uM([["fontSize", "36rpx"], ["fontWeight", 700], ["color", "#0f172a"], ["letterSpacing", "0.8rpx"]]))], ["stats-container", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["marginBottom", "30rpx"]]))], ["stat-card", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["backgroundImage", "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "35rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "35rpx"], ["paddingLeft", "20rpx"], ["marginTop", 0], ["marginRight", "8rpx"], ["marginBottom", 0], ["marginLeft", "8rpx"], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["boxShadow", "0 8rpx 25rpx rgba(30, 64, 175, 0.08), 0 3rpx 6rpx rgba(15, 23, 42, 0.04)"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "cubic-bezier(0.4,0,0.2,1)"], ["borderTopWidth", "1rpx"], ["borderRightWidth", "1rpx"], ["borderBottomWidth", "1rpx"], ["borderLeftWidth", "1rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "rgba(255,255,255,0.8)"], ["borderRightColor", "rgba(255,255,255,0.8)"], ["borderBottomColor", "rgba(255,255,255,0.8)"], ["borderLeftColor", "rgba(255,255,255,0.8)"], ["position", "relative"], ["overflow", "hidden"], ["content::before", "''"], ["position::before", "absolute"], ["top::before", 0], ["left::before", 0], ["right::before", 0], ["height::before", "4rpx"], ["backgroundImage::before", "linear-gradient(90deg, #1e40af, #0ea5e9)"], ["backgroundColor::before", "rgba(0,0,0,0)"], ["transform::before", "scaleX(0)"], ["transitionProperty::before", "transform"], ["transitionDuration::before", "0.3s"], ["transitionTimingFunction::before", "ease"], ["transform:active", "translateY(-4rpx) scale(0.98)"], ["boxShadow:active", "0 12rpx 35rpx rgba(30, 64, 175, 0.16), 0 6rpx 12rpx rgba(15, 23, 42, 0.08)"], ["transform:active::before", "scaleX(1)"]]))], ["stat-number", _pS(_uM([["fontSize", "52rpx"], ["fontWeight", 700], ["color", "#0a2540"], ["marginBottom", "12rpx"], ["letterSpacing", "-1rpx"]]))], ["stat-label", _pS(_uM([["fontSize", "22rpx"], ["color", "#1e293b"], ["textAlign", "center"], ["fontWeight", 600], ["letterSpacing", "0.3rpx"]]))], ["chart-container", _pS(_uM([["marginTop", "25rpx"], ["paddingTop", "35rpx"], ["paddingRight", "25rpx"], ["paddingBottom", "35rpx"], ["paddingLeft", "25rpx"], ["backgroundImage", "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["borderTopLeftRadius", "24rpx"], ["borderTopRightRadius", "24rpx"], ["borderBottomRightRadius", "24rpx"], ["borderBottomLeftRadius", "24rpx"], ["boxShadow", "0 12rpx 40rpx rgba(30, 64, 175, 0.08), 0 4rpx 12rpx rgba(15, 23, 42, 0.04)"], ["borderTopWidth", "1rpx"], ["borderRightWidth", "1rpx"], ["borderBottomWidth", "1rpx"], ["borderLeftWidth", "1rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "rgba(255,255,255,0.8)"], ["borderRightColor", "rgba(255,255,255,0.8)"], ["borderBottomColor", "rgba(255,255,255,0.8)"], ["borderLeftColor", "rgba(255,255,255,0.8)"], ["position", "relative"], ["overflow", "hidden"], ["content::before", "''"], ["position::before", "absolute"], ["top::before", 0], ["left::before", 0], ["right::before", 0], ["height::before", "2rpx"], ["backgroundImage::before", "linear-gradient(90deg, #1e40af, #0ea5e9, #38bdf8)"], ["backgroundColor::before", "rgba(0,0,0,0)"]]))], ["chart-title", _pS(_uM([["marginBottom", "25rpx"], ["textAlign", "center"], ["position", "relative"], ["content::after", "''"], ["position::after", "absolute"], ["bottom::after", "-10rpx"], ["left::after", "50%"], ["transform::after", "translateX(-50%)"], ["width::after", "60rpx"], ["height::after", "3rpx"], ["backgroundImage::after", "linear-gradient(90deg, #1e40af, #0ea5e9)"], ["backgroundColor::after", "rgba(0,0,0,0)"], ["borderTopLeftRadius::after", "2rpx"], ["borderTopRightRadius::after", "2rpx"], ["borderBottomRightRadius::after", "2rpx"], ["borderBottomLeftRadius::after", "2rpx"]]))], ["chart-title-text", _pS(_uM([["fontSize", "32rpx"], ["fontWeight", 600], ["color", "#0f172a"], ["letterSpacing", "0.5rpx"]]))], ["chart-wrapper", _pS(_uM([["height", "500rpx"], ["width", "100%"], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["boxShadow", "inset 0 2rpx 8rpx rgba(30, 64, 175, 0.04)"], ["paddingTop", "15rpx"], ["paddingRight", "15rpx"], ["paddingBottom", "15rpx"], ["paddingLeft", "15rpx"], ["minHeight", "400rpx"], ["overflow", "hidden"]]))], ["chart-canvas", _pS(_uM([["width", "100%"], ["height", "100%"], ["borderTopLeftRadius", "8rpx"], ["borderTopRightRadius", "8rpx"], ["borderBottomRightRadius", "8rpx"], ["borderBottomLeftRadius", "8rpx"], ["minHeight", 350], ["transform:hover", "scale(1.01)"]]))], ["extra-content", _pS(_uM([["marginTop", "40rpx"]]))], ["info-card", _pS(_uM([["backgroundImage", "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "35rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "35rpx"], ["paddingLeft", "30rpx"], ["marginBottom", "25rpx"], ["boxShadow", "0 8rpx 25rpx rgba(30, 64, 175, 0.06), 0 3rpx 8rpx rgba(15, 23, 42, 0.04)"], ["borderTopWidth", "1rpx"], ["borderRightWidth", "1rpx"], ["borderBottomWidth", "1rpx"], ["borderLeftWidth", "1rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "rgba(255,255,255,0.8)"], ["borderRightColor", "rgba(255,255,255,0.8)"], ["borderBottomColor", "rgba(255,255,255,0.8)"], ["borderLeftColor", "rgba(255,255,255,0.8)"], ["position", "relative"], ["overflow", "hidden"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["content::before", "''"], ["position::before", "absolute"], ["top::before", 0], ["left::before", 0], ["width::before", "6rpx"], ["height::before", "100%"], ["backgroundImage::before", "linear-gradient(180deg, #1e40af, #0ea5e9)"], ["backgroundColor::before", "rgba(0,0,0,0)"], ["transform:active", "translateY(-2rpx)"], ["boxShadow:active", "0 12rpx 35rpx rgba(30, 64, 175, 0.12), 0 6rpx 12rpx rgba(15, 23, 42, 0.06)"]]))], ["info-title", _pS(_uM([["fontSize", "30rpx"], ["fontWeight", 600], ["color", "#0f172a"], ["marginBottom", "18rpx"], ["letterSpacing", "0.5rpx"]]))], ["info-text", _pS(_uM([["fontSize", "28rpx"], ["color", "#475569"], ["lineHeight", 1.6], ["letterSpacing", "0.3rpx"]]))], ["@TRANSITION", _uM([["avatar", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "ease"]])], ["stat-card", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "cubic-bezier(0.4,0,0.2,1)"], ["property::before", "transform"], ["duration::before", "0.3s"], ["timingFunction::before", "ease"]])], ["info-card", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "ease"]])]])]])]
