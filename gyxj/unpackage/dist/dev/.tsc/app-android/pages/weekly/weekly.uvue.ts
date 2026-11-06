
	import { getApiUrl } from '@/config/api.uts'
	import { isWeeklyAdmin } from '@/utils/permission.uts'
	import { refreshUserInfo } from '@/utils/user.uts'
	
	type RecentRecord = { __$originalPosition?: UTSSourceMapPosition<"RecentRecord", "pages/weekly/weekly.uvue", 66, 7>;
		id: string,
		problemDescription: string,
		locationName: string,
		createTime: string,
		status: string
	}

	type StatisticsData = { __$originalPosition?: UTSSourceMapPosition<"StatisticsData", "pages/weekly/weekly.uvue", 74, 7>;
		pendingIssues: number,
		completedIssues: number,
		reviewedCount: number
	}

	const __sfc__ = defineComponent({
		data() {
			return {
				userInfo: {} as UTSJSONObject,
				userName: '用户', // 简化的用户姓名变量
				currentDate: '',
				scrollTop: 0,
				containerHeight: 0,
				// 下拉刷新相关
				isRefreshing: false,
				statistics: {
					pendingIssues: 0,
					completedIssues: 0,
					reviewedCount: 0
				} as StatisticsData,
				recentRecords: [] as RecentRecord[]
			}
		},
		onLoad() {
			if (!this.checkLogin()) return
			this.initContainerHeight()
			// 先刷新服务端 userInfo(部门/角色可能已被后台调整)，再按最新 deptId 过滤与展示
			refreshUserInfo().then(() => {
				this.loadUserInfo()
				this.initData()
				this.loadStatistics()
				this.loadRecentRecords()
			})
		},
		methods: {
			checkLogin() {
				const token = uni.getStorageSync('token')
				if (token == null || token == '') {
					uni.reLaunch({
						url: '/pages/login/login'
					})
					return false
				}
				return true
			},
			
			// 加载用户信息
			loadUserInfo() {
				const userInfoData = uni.getStorageSync('userInfo')
				const userInfoStr = userInfoData != null ? userInfoData as string : ''
				
				if (userInfoStr != null && userInfoStr != '' && userInfoStr != 'null' && userInfoStr != 'undefined') {
					try {
						const parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/weekly/weekly.uvue:128") as UTSJSONObject
						this.userInfo = parsedUserInfo
						// 设置简化的用户姓名
						const realNameValue = parsedUserInfo.getString('realName')
						this.userName = realNameValue != null ? realNameValue : '用户'
					} catch (error) {
						console.error('用户信息JSON解析失败:', error, " at pages/weekly/weekly.uvue:134")
						this.userInfo = {} as UTSJSONObject
						this.userName = '解析失败用户'
					}
				} else {
					this.userInfo = {} as UTSJSONObject
					this.userName = '默认用户'
				}
			},
			
			initData() {
				const now = new Date()
				const year = now.getFullYear()
				const month = (now.getMonth() + 1).toString()
				const formattedMonth = month.length == 1 ? `0${month}` : month
				const day = now.getDate().toString()
				const formattedDay = day.length == 1 ? `0${day}` : day
				const weekdays = ['日', '一', '二', '三', '四', '五', '六']
				const weekday = weekdays[now.getDay()]
				this.currentDate = `${year}年${formattedMonth}月${formattedDay}日 星期${weekday}`
			},
			
			loadStatistics() {
				console.log('开始加载统计数据', " at pages/weekly/weekly.uvue:157")
				const token = uni.getStorageSync('token')
				
				// 获取用户信息：部门 ID + 周检管理员标志
				let departmentId = ''
				// 周检域管理员用 isWeeklyAdmin()(对齐 Web permission.js:老 isAdmin=1 或 isWeeklyAdmin=1)
				const isAdmin = isWeeklyAdmin()
				const userInfoData = uni.getStorageSync('userInfo')
				const userInfoStr = userInfoData != null ? userInfoData as string : ''
				if (userInfoStr != null && userInfoStr != '' && userInfoStr != 'null' && userInfoStr != 'undefined') {
					try {
					const parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/weekly/weekly.uvue:168") as UTSJSONObject
					departmentId = parsedUserInfo.getString('deptId') ?? ''
					console.log("获取到的部门id",departmentId, " at pages/weekly/weekly.uvue:170")
					console.log("是否为管理员",isAdmin, " at pages/weekly/weekly.uvue:171")
					} catch (error) {
						console.error('用户信息JSON解析失败:', error, " at pages/weekly/weekly.uvue:173")
					}
				}
				
				// 构建部门过滤参数 - 管理员用户不需要部门过滤
				const deptParam = (!isAdmin && departmentId != null && departmentId != '') ? `&departmentId=${departmentId}` : ''
				
				// 获取待整改数量（状态为0）- 使用分页查询API获取准确数量
				const pendingUrl = getApiUrl(`/inspection-record/page?current=1&size=1&status=0${deptParam}`)
				console.log('待整改数量请求URL:', pendingUrl, " at pages/weekly/weekly.uvue:182")
				uni.request({
					url: pendingUrl,
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						console.log('待整改数量请求成功:', res, " at pages/weekly/weekly.uvue:190")
						const response = res.data as UTSJSONObject
						if (res.statusCode == 200 && (response.getNumber('code') ?? 0) == 200) {
							const data = response.getJSON('data') as UTSJSONObject
							this.statistics.pendingIssues = data.getNumber('total') ?? 0
							console.log('待整改数量:', this.statistics.pendingIssues, " at pages/weekly/weekly.uvue:195")
						} else {
							console.log('待整改数量请求失败:', response, " at pages/weekly/weekly.uvue:197")
						}
					},
					fail: (error) => {
						console.log('待整改数量请求错误:', error, " at pages/weekly/weekly.uvue:201")
					}
				})
				
				// 获取已整改数量（状态为1）- 使用分页查询API获取准确数量
				const completedUrl = getApiUrl(`/inspection-record/page?current=1&size=1&status=1${deptParam}`)
				console.log('已整改数量请求URL:', completedUrl, " at pages/weekly/weekly.uvue:207")
				uni.request({
					url: completedUrl,
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						console.log('已整改数量请求成功:', res, " at pages/weekly/weekly.uvue:215")
						const response = res.data as UTSJSONObject
						if (res.statusCode == 200 && (response.getNumber('code') ?? 0) == 200) {
							const data = response.getJSON('data') as UTSJSONObject
							this.statistics.completedIssues = data.getNumber('total') ?? 0
							console.log('已整改数量:', this.statistics.completedIssues, " at pages/weekly/weekly.uvue:220")
						} else {
							console.log('已整改数量请求失败:', response, " at pages/weekly/weekly.uvue:222")
						}
					},
					fail: (error) => {
						console.log('已整改数量请求错误:', error, " at pages/weekly/weekly.uvue:226")
					}
				})
				
				// 获取已复查数量（状态为2）- 使用分页查询API获取准确数量
				const reviewedUrl = getApiUrl(`/inspection-record/page?current=1&size=1&status=2${deptParam}`)
				console.log('已复查数量请求URL:', reviewedUrl, " at pages/weekly/weekly.uvue:232")
				uni.request({
					url: reviewedUrl,
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						console.log('已复查数量请求成功:', res, " at pages/weekly/weekly.uvue:240")
						const response = res.data as UTSJSONObject
						if (res.statusCode == 200 && (response.getNumber('code') ?? 0) == 200) {
							const data = response.getJSON('data') as UTSJSONObject
							this.statistics.reviewedCount = data.getNumber('total') ?? 0
							console.log('已复查数量:', this.statistics.reviewedCount, " at pages/weekly/weekly.uvue:245")
						} else {
							console.log('已复查数量请求失败:', response, " at pages/weekly/weekly.uvue:247")
						}
					},
					fail: (error) => {
						console.log('已复查数量请求错误:', error, " at pages/weekly/weekly.uvue:251")
					}
				})
			},
			
			loadRecentRecords() {
				const token = uni.getStorageSync('token')
				
				// 获取用户信息：部门 ID + 周检管理员标志
				let departmentId = ''
				// 周检域管理员用 isWeeklyAdmin()(对齐 Web permission.js)
				const isAdmin = isWeeklyAdmin()
				const userInfoData = uni.getStorageSync('userInfo')
				const userInfoStr = userInfoData != null ? userInfoData as string : ''
				if (userInfoStr != null && userInfoStr != '' && userInfoStr != 'null' && userInfoStr != 'undefined') {
					try {
						const parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/weekly/weekly.uvue:267") as UTSJSONObject
						departmentId = parsedUserInfo.getString('deptId') ?? ''
					} catch (error) {
						console.error('用户信息JSON解析失败:', error, " at pages/weekly/weekly.uvue:270")
					}
				}
				
				// 构建部门过滤参数 - 管理员用户不需要部门过滤
				const deptParam = (!isAdmin && departmentId != null && departmentId != '') ? `&departmentId=${departmentId}` : ''
				
				const apiUrl = getApiUrl(`/inspection-record/page?current=1&size=5&status=0${deptParam}`)
				console.log('请求URL:', apiUrl, " at pages/weekly/weekly.uvue:278")
				uni.request({
				url: apiUrl,
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						const response = res.data as UTSJSONObject
						if (res.statusCode == 200 && (response.getNumber('code') ?? 0) == 200) {
							const data = response.getJSON('data') as UTSJSONObject
							const records = data.getArray('records') as Array<UTSJSONObject>
							this.recentRecords = []
							for (let i = 0; i < records.length; i++) {
								const item = records[i]
								const record: RecentRecord = {
									id: item.getString('id') ?? '',
									problemDescription: item.getString('problemDescription') ?? '',
									locationName: item.getString('locationName') ?? '',
									createTime: item.getString('createTime') ?? '',
									status: item.getString('status') ?? ''
								}
								this.recentRecords.push(record)
							}
						}
					},
					fail: (error) => {
						console.log('加载最近记录失败:', error, " at pages/weekly/weekly.uvue:305")
					}
				})
			},
			
			getStatusText(status: string | null): string {
				if (status == null) return '未知'
				if (status == "0") return '待整改'
				if (status == "1") return '已整改'
				if (status == "2") return '已复查'
				return '未知'
			},
			
			getStatusClass(status: string | null): string {
				if (status == null) return 'status-unknown'
				if (status == "0") return 'status-pending'
				if (status == "1") return 'status-completed'
				if (status == "2") return 'status-accepted'
				return 'status-unknown'
			},
			
			goToPendingIssues() {
				uni.navigateTo({
					url: '/pages/record/record?status=0'
				})
			},
			
			goToCompletedIssues() {
				uni.navigateTo({
					url: '/pages/record/record?status=1'
				})
			},
			
			goToReviewedRecords() {
				uni.navigateTo({
					url: '/pages/record/record?type=reviewed'
				})
			},
			
			goToRecords() {
				uni.switchTab({
					url: '/pages/record/record'
				})
			},
			
			goToAddRecord() {
				uni.navigateTo({
					url: '/pages/record/add'
				})
			},
			
			goToDelayedRecord() {
				// 延迟整改是指超过整改期限但仍未整改的记录
				// 这些记录的状态是0（待整改），但已经超过了整改期限
				uni.navigateTo({
					url: '/pages/record/record?status=0&delayed=true'
				})
			},
			
			goToRecordDetail(recordId: string) {
				uni.navigateTo({
					url: `/pages/record/detail?id=${recordId}`
				})
			},
			
			// 下拉刷新事件处理
			onRefresh() {
				console.log('开始下拉刷新', " at pages/weekly/weekly.uvue:372")
				this.isRefreshing = true
				
				// 重新加载数据
				this.reloadAllData()
			},
			
			// 刷新恢复事件处理
			onRefreshRestore() {
				console.log('刷新恢复', " at pages/weekly/weekly.uvue:381")
				this.isRefreshing = false
			},
			
			// 重新加载所有数据
			reloadAllData() {
				console.log('重新加载所有数据', " at pages/weekly/weekly.uvue:387")
				
				try {
					// 重新初始化时间
					this.initData()
					
					// 重新加载用户信息
					this.loadUserInfo()
					
					// 重新加载统计数据和记录
					this.loadStatistics()
					this.loadRecentRecords()
					
					console.log('数据加载完成', " at pages/weekly/weekly.uvue:400")
					
				} catch (error) {
					console.error('数据加载失败:', error, " at pages/weekly/weekly.uvue:403")
				} finally {
					// 无论成功还是失败，都要关闭刷新动画
					setTimeout(() => {
						console.log('关闭刷新动画', " at pages/weekly/weekly.uvue:407")
						this.isRefreshing = false
					}, 1000) // 1秒后关闭刷新动画
				}
			},
			
			initContainerHeight() {
				const res = uni.getSystemInfoSync()
				this.containerHeight = res.windowHeight
			}
		}
	})

export default __sfc__
function GenPagesWeeklyWeeklyRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
  return _cE("scroll-view", _uM({
    class: "container",
    "scroll-y": "true",
    "scroll-top": _ctx.scrollTop,
    style: _nS(_uM({height: _ctx.containerHeight + 'px'})),
    "enable-back-to-top": "true",
    "refresher-enabled": "true",
    "refresher-triggered": _ctx.isRefreshing,
    "refresher-default-style": "black",
    "refresher-background": "#f5f5f5",
    onRefresherrefresh: _ctx.onRefresh,
    onRefresherrestore: _ctx.onRefreshRestore
  }), [
    _cE("view", _uM({ class: "header" }), [
      _cE("view", _uM({ class: "welcome" }), [
        _cE("text", _uM({ class: "greeting" }), "欢迎回来，" + _tD(_ctx.userName), 1 /* TEXT */),
        _cE("text", _uM({ class: "app-name" }), _tD(_ctx.currentDate), 1 /* TEXT */)
      ]),
      _cE("image", _uM({
        class: "avatar",
        src: _ctx.userInfo != null && (_ctx.userInfo.getString('avatar') ?? '') != '' ? (_ctx.userInfo.getString('avatar') ?? '') : '/static/default-avatar.svg',
        mode: "aspectFill"
      }), null, 8 /* PROPS */, ["src"])
    ]),
    _cE("view", _uM({ class: "action-buttons" }), [
      _cE("view", _uM({
        class: "action-button",
        onClick: _ctx.goToAddRecord
      }), [
        _cE("view", _uM({ class: "button-icon" }), "➕"),
        _cE("text", _uM({ class: "button-text" }), "周检"),
        _cE("text", _uM({ class: "button-text-sub" }), "每周安全检查")
      ], 8 /* PROPS */, ["onClick"]),
      _cE("view", _uM({
        class: "action-button delayed-button",
        onClick: _ctx.goToDelayedRecord
      }), [
        _cE("view", _uM({ class: "button-icon" }), "⏰"),
        _cE("text", _uM({ class: "button-text" }), "延迟整改"),
        _cE("text", _uM({ class: "button-text-sub" }), "延期问题处理")
      ], 8 /* PROPS */, ["onClick"])
    ]),
    _cE("view", _uM({ class: "stats-container" }), [
      _cE("view", _uM({
        class: "stats-card",
        onClick: _ctx.goToPendingIssues
      }), [
        _cE("text", _uM({ class: "stats-number" }), _tD(_ctx.statistics.pendingIssues), 1 /* TEXT */),
        _cE("text", _uM({ class: "stats-label" }), "待整改"),
        _cE("text", _uM({ class: "stats-icon" }), "⚠️")
      ], 8 /* PROPS */, ["onClick"]),
      _cE("view", _uM({
        class: "stats-card",
        onClick: _ctx.goToCompletedIssues
      }), [
        _cE("text", _uM({ class: "stats-number" }), _tD(_ctx.statistics.completedIssues), 1 /* TEXT */),
        _cE("text", _uM({ class: "stats-label" }), "已整改"),
        _cE("text", _uM({ class: "stats-icon" }), "✅")
      ], 8 /* PROPS */, ["onClick"]),
      _cE("view", _uM({
        class: "stats-card",
        onClick: _ctx.goToReviewedRecords
      }), [
        _cE("text", _uM({ class: "stats-number" }), _tD(_ctx.statistics.reviewedCount), 1 /* TEXT */),
        _cE("text", _uM({ class: "stats-label" }), "已复查数量"),
        _cE("text", _uM({ class: "stats-icon" }), "✅")
      ], 8 /* PROPS */, ["onClick"])
    ])
  ], 44 /* STYLE, PROPS, NEED_HYDRATION */, ["scroll-top", "refresher-triggered", "onRefresherrefresh", "onRefresherrestore"])
}
const GenPagesWeeklyWeeklyStyles = [_uM([["container", _pS(_uM([["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "150rpx"], ["paddingLeft", "30rpx"], ["backgroundImage", "linear-gradient(180deg, #eff6ff 0%, #dbeafe 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxSizing", "border-box"], ["minHeight", "1200rpx"]]))], ["header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", "30rpx"], ["paddingRight", "25rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "25rpx"], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "24rpx"], ["borderTopRightRadius", "24rpx"], ["borderBottomRightRadius", "24rpx"], ["borderBottomLeftRadius", "24rpx"], ["marginBottom", "30rpx"], ["boxShadow", "0 12rpx 36rpx rgba(30, 64, 175, 0.18)"], ["position", "relative"], ["overflow", "hidden"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["content::before", "''"], ["position::before", "absolute"], ["top::before", 0], ["left::before", 0], ["width::before", "8rpx"], ["height::before", "100%"], ["backgroundImage::before", "linear-gradient(180deg, #1e40af, #0ea5e9)"], ["backgroundColor::before", "rgba(0,0,0,0)"]]))], ["welcome", _pS(_uM([["display", "flex"], ["flexDirection", "column"], ["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["zIndex", 2], ["position", "relative"], ["paddingLeft", "12rpx"]]))], ["greeting", _pS(_uM([["color", "#0a2540"], ["fontSize", "36rpx"], ["fontWeight", 700], ["marginBottom", "10rpx"], ["letterSpacing", "1rpx"]]))], ["app-name", _pS(_uM([["color", "#475569"], ["fontSize", "26rpx"], ["fontWeight", 500], ["letterSpacing", "0.3rpx"]]))], ["avatar", _pS(_uM([["width", "100rpx"], ["height", "100rpx"], ["borderTopLeftRadius", "50rpx"], ["borderTopRightRadius", "50rpx"], ["borderBottomRightRadius", "50rpx"], ["borderBottomLeftRadius", "50rpx"], ["backgroundColor", "#eff6ff"], ["overflow", "hidden"], ["borderTopWidth", "3rpx"], ["borderRightWidth", "3rpx"], ["borderBottomWidth", "3rpx"], ["borderLeftWidth", "3rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["boxShadow", "0 6rpx 20rpx rgba(30, 64, 175, 0.12)"], ["zIndex", 2], ["position", "relative"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["transform:active", "scale(0.95)"]]))], ["stats-container", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["marginBottom", "30rpx"]]))], ["stats-card", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["backgroundImage", "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "32rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "32rpx"], ["paddingLeft", "20rpx"], ["textAlign", "center"], ["position", "relative"], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"], ["overflow", "hidden"], ["marginTop", 0], ["marginRight", "8rpx"], ["marginBottom", 0], ["marginLeft", "8rpx"], ["borderTopWidth", "1rpx"], ["borderRightWidth", "1rpx"], ["borderBottomWidth", "1rpx"], ["borderLeftWidth", "1rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "rgba(255,255,255,0.8)"], ["borderRightColor", "rgba(255,255,255,0.8)"], ["borderBottomColor", "rgba(255,255,255,0.8)"], ["borderLeftColor", "rgba(255,255,255,0.8)"], ["content::before", "''"], ["position::before", "absolute"], ["top::before", 0], ["left::before", 0], ["right::before", 0], ["height::before", "4rpx"], ["backgroundImage::before", "linear-gradient(90deg, #1e40af, #0ea5e9)"], ["backgroundColor::before", "rgba(0,0,0,0)"]]))], ["stats-number", _pS(_uM([["fontSize", "48rpx"], ["fontWeight", 700], ["color", "#0a2540"], ["marginBottom", "8rpx"]]))], ["stats-label", _pS(_uM([["fontSize", "26rpx"], ["color", "#1e293b"], ["fontWeight", 600], ["marginBottom", "8rpx"]]))], ["stats-icon", _pS(_uM([["fontSize", "36rpx"], ["position", "absolute"], ["top", "20rpx"], ["right", "20rpx"], ["opacity", 0.18]]))], ["action-buttons", _pS(_uM([["marginBottom", "30rpx"], ["display", "flex"], ["flexDirection", "row"], ["justifyContent", "space-between"]]))], ["action-button", _uM([[".action-buttons ", _uM([["marginTop", 0], ["marginRight", "8rpx"], ["marginBottom", 0], ["marginLeft", "8rpx"]])], ["", _uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "36rpx"], ["paddingRight", "16rpx"], ["paddingBottom", "36rpx"], ["paddingLeft", "16rpx"], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.12)"], ["minWidth", 0], ["transitionProperty", "transform"], ["transitionDuration", "0.2s"], ["transitionTimingFunction", "ease"], ["position", "relative"], ["overflow", "hidden"], ["content::before", "''"], ["position::before", "absolute"], ["top::before", 0], ["left::before", 0], ["right::before", 0], ["height::before", "6rpx"], ["backgroundImage::before", "linear-gradient(90deg, #1e40af, #0ea5e9)"], ["backgroundColor::before", "rgba(0,0,0,0)"], ["transform:active", "scale(0.97)"]])]])], ["delayed-button", _pS(_uM([["!backgroundImage", "none"], ["!backgroundColor", "#fffbeb"], ["!borderTopColor", "#fde68a"], ["!borderRightColor", "#fde68a"], ["!borderBottomColor", "#fde68a"], ["!borderLeftColor", "#fde68a"], ["!backgroundImage::before", "linear-gradient(90deg, #b45309, #f59e0b)"], ["!backgroundColor::before", "rgba(0,0,0,0)"]]))], ["button-icon", _uM([["", _uM([["fontSize", "44rpx"], ["marginBottom", "14rpx"], ["color", "#1e40af"], ["flexShrink", 0]])], [".delayed-button ", _uM([["color", "#b45309"]])]])], ["button-text", _uM([["", _uM([["fontSize", "32rpx"], ["color", "#0a2540"], ["fontWeight", 700], ["letterSpacing", "1rpx"]])], [".delayed-button ", _uM([["color", "#7c2d12"]])]])], ["button-text-sub", _uM([["", _uM([["fontSize", "22rpx"], ["color", "#475569"], ["fontWeight", 500], ["marginTop", "6rpx"], ["letterSpacing", "0.5rpx"]])], [".delayed-button ", _uM([["color", "#92400e"]])]])], ["recent-records", _pS(_uM([["marginBottom", "20rpx"]]))], ["section-title", _pS(_uM([["fontSize", "18rpx"], ["fontWeight", 600], ["color", "#0f172a"], ["marginBottom", "15rpx"], ["flexDirection", "column"]]))], ["section-header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["marginBottom", "15rpx"]]))], ["more-text", _pS(_uM([["fontSize", "14rpx"], ["color", "#2563eb"]]))], ["records-list", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "24rpx"], ["borderTopRightRadius", "24rpx"], ["borderBottomRightRadius", "24rpx"], ["borderBottomLeftRadius", "24rpx"], ["paddingTop", "15rpx"], ["paddingRight", "15rpx"], ["paddingBottom", "15rpx"], ["paddingLeft", "15rpx"], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"], ["borderTopWidth", "1rpx"], ["borderRightWidth", "1rpx"], ["borderBottomWidth", "1rpx"], ["borderLeftWidth", "1rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"]]))], ["record-item", _uM([["", _uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", "15rpx"], ["paddingRight", 0], ["paddingBottom", "15rpx"], ["paddingLeft", 0], ["borderBottomWidth", "1rpx"], ["borderBottomStyle", "solid"], ["borderBottomColor", "#eff6ff"], ["borderBottomWidth:last-child", "medium"], ["borderBottomStyle:last-child", "none"], ["borderBottomColor:last-child", "#000000"]])], [".records-list ", _uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "24rpx"], ["borderTopRightRadius", "24rpx"], ["borderBottomRightRadius", "24rpx"], ["borderBottomLeftRadius", "24rpx"], ["paddingTop", "15rpx"], ["paddingRight", "15rpx"], ["paddingBottom", "15rpx"], ["paddingLeft", "15rpx"], ["marginBottom", "10rpx"], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"], ["borderTopWidth", "1rpx"], ["borderRightWidth", "1rpx"], ["borderBottomWidth", "1rpx"], ["borderLeftWidth", "1rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"]])]])], ["record-info", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"]]))], ["record-title", _pS(_uM([["fontSize", "16rpx"], ["color", "#0f172a"], ["flexDirection", "column"], ["marginBottom", "4rpx"]]))], ["record-meta", _pS(_uM([["fontSize", "12rpx"], ["color", "#94a3b8"], ["flexDirection", "column"]]))], ["record-status", _pS(_uM([["paddingTop", "4rpx"], ["paddingRight", "8rpx"], ["paddingBottom", "4rpx"], ["paddingLeft", "8rpx"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["fontSize", "12rpx"]]))], ["status-pending", _pS(_uM([["backgroundColor", "#fef3c7"], ["color", "#b45309"]]))], ["status-processing", _pS(_uM([["backgroundColor", "#dbeafe"], ["color", "#1e40af"]]))], ["status-completed", _pS(_uM([["backgroundColor", "#dcfce7"], ["color", "#15803d"]]))], ["status-accepted", _pS(_uM([["backgroundColor", "#e2e8f0"], ["color", "#475569"]]))], ["status-unknown", _pS(_uM([["backgroundColor", "#f1f5f9"], ["color", "#94a3b8"]]))], ["empty-state", _pS(_uM([["textAlign", "center"], ["paddingTop", "40rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "20rpx"]]))], ["recent-section", _pS(_uM([["marginTop", "20rpx"], ["marginRight", "20rpx"], ["marginBottom", "20rpx"], ["marginLeft", "20rpx"]]))], ["view-all", _pS(_uM([["fontSize", "14rpx"], ["color", "#2563eb"]]))], ["empty-records", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "24rpx"], ["borderTopRightRadius", "24rpx"], ["borderBottomRightRadius", "24rpx"], ["borderBottomLeftRadius", "24rpx"], ["paddingTop", "40rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "20rpx"], ["textAlign", "center"], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"], ["borderTopWidth", "1rpx"], ["borderRightWidth", "1rpx"], ["borderBottomWidth", "1rpx"], ["borderLeftWidth", "1rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"]]))], ["empty-text", _pS(_uM([["fontSize", "14rpx"], ["color", "#94a3b8"]]))], ["record-content", _pS(_uM([["display", "flex"], ["flexDirection", "column"]]))], ["problem-desc", _uM([[".record-content ", _uM([["marginTop", "8rpx"]])], ["", _uM([["fontSize", "16rpx"], ["color", "#0f172a"], ["fontWeight", 500], ["lineHeight", 1.4]])]])], ["location-name", _pS(_uM([["fontSize", "14rpx"], ["color", "#475569"]]))], ["record-footer", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["marginTop", "5rpx"]]))], ["create-time", _pS(_uM([["fontSize", "12rpx"], ["color", "#94a3b8"]]))], ["status-tag", _pS(_uM([["paddingTop", "4rpx"], ["paddingRight", "8rpx"], ["paddingBottom", "4rpx"], ["paddingLeft", "8rpx"], ["borderTopLeftRadius", "24rpx"], ["borderTopRightRadius", "24rpx"], ["borderBottomRightRadius", "24rpx"], ["borderBottomLeftRadius", "24rpx"], ["fontSize", "12rpx"]]))], ["status-text", _pS(_uM([["color", "#ffffff"], ["fontWeight", 500]]))], ["@TRANSITION", _uM([["avatar", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "ease"]])], ["action-button", _uM([["property", "transform"], ["duration", "0.2s"], ["timingFunction", "ease"]])]])]])]
