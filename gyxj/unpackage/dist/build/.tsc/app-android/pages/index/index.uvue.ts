
	import { getApiUrl } from '@/config/api.uts'
	type RecentRecord = {
		id: string,
		problemDescription: string,
		locationName: string,
		createTime: string,
		status: string
	}

	type StatisticsData = {
		pendingIssues: number,
		completedIssues: number,
		monthRecords: number
	}

	type RequestResponse = {
		data: UTSJSONObject,
		statusCode: number
	}

	const __sfc__ = defineComponent({
		data() {
			return {
				userInfo: {} as UTSJSONObject,
				currentDate: '',
				statistics: {
			pendingIssues: 0,
			completedIssues: 0,
			monthRecords: 0
		} as StatisticsData,
				recentRecords: [] as RecentRecord[]
			}
		},
		onLoad(options: OnLoadOptions) {
			this.checkLogin()
			this.initData()
			this.loadStatistics()
			this.loadRecentRecords()
		},
		methods: {
			checkLogin() {
			const token = uni.getStorageSync('token')
			if (token == null || token == '') {
				uni.reLaunch({
					url: '/pages/login/login'
				})
				return
			}
			const userInfoStr = uni.getStorageSync('userInfo') as string
			if (userInfoStr != null && userInfoStr != '') {
				this.userInfo = JSON.parse(userInfoStr) as UTSJSONObject
			} else {
				this.userInfo = {} as UTSJSONObject
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
				const token = uni.getStorageSync('token')
				
				// 获取用户信息，包括部门和管理员权限
				let departmentId = ''
				let isAdmin = false
				const userInfoData = uni.getStorageSync('userInfo')
				const userInfoStr = userInfoData != null ? userInfoData as string : ''
				if (userInfoStr != null && userInfoStr != '' && userInfoStr != 'null' && userInfoStr != 'undefined') {
					try {
						const parsedUserInfo = JSON.parse(userInfoStr) as UTSJSONObject
						departmentId = parsedUserInfo.getString('deptId') ?? ''
						isAdmin = (parsedUserInfo.getNumber('isAdmin') ?? 0) === 1
					} catch (error) {
						console.error('用户信息JSON解析失败:', error)
					}
				}
				
				// 构建部门过滤参数 - 管理员用户不需要部门过滤
				const deptParam = (!isAdmin && departmentId != null && departmentId != '') ? `&departmentId=${departmentId}` : ''
				
				// 分别获取待整改、已整改、已复查的数量
				let pendingCount = 0, completedCount = 0, reviewedCount = 0
				let completedRequests = 0
				const totalRequests = 3
				
				const updateStatistics = () => {
					this.statistics = {
						pendingIssues: pendingCount,
						completedIssues: completedCount,
						monthRecords: reviewedCount
					}
				}
				
				// 获取待整改数量（状态为0）
				uni.request({
					url: getApiUrl(`/monthly-inspection-record/page?current=1&size=1&status=0${deptParam}`),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						const response = res.data as UTSJSONObject
						if (res.statusCode == 200 && (response.getNumber('code') ?? 0) == 200) {
							const data = response.getJSON('data') as UTSJSONObject
							pendingCount = data.getNumber('total') ?? 0
						}
						completedRequests++
						if (completedRequests === totalRequests) {
							updateStatistics()
						}
					},
					fail: (error) => {
						console.log('加载待整改数据失败:', error)
						completedRequests++
						if (completedRequests === totalRequests) {
							updateStatistics()
						}
					}
				})
				
				// 获取已整改数量（状态为1）
				uni.request({
					url: getApiUrl(`/monthly-inspection-record/page?current=1&size=1&status=1${deptParam}`),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						const response = res.data as UTSJSONObject
						if (res.statusCode == 200 && (response.getNumber('code') ?? 0) == 200) {
							const data = response.getJSON('data') as UTSJSONObject
							completedCount = data.getNumber('total') ?? 0
						}
						completedRequests++
						if (completedRequests === totalRequests) {
							updateStatistics()
						}
					},
					fail: (error) => {
						console.log('加载已整改数据失败:', error)
						completedRequests++
						if (completedRequests === totalRequests) {
							updateStatistics()
						}
					}
				})
				
				// 获取已复查数量（状态为2）
				uni.request({
					url: getApiUrl(`/monthly-inspection-record/page?current=1&size=1&status=2${deptParam}`),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						const response = res.data as UTSJSONObject
						if (res.statusCode == 200 && (response.getNumber('code') ?? 0) == 200) {
							const data = response.getJSON('data') as UTSJSONObject
							reviewedCount = data.getNumber('total') ?? 0
						}
						completedRequests++
						if (completedRequests === totalRequests) {
							updateStatistics()
						}
					},
					fail: (error) => {
						console.log('加载已复查数据失败:', error)
						completedRequests++
						if (completedRequests === totalRequests) {
							updateStatistics()
						}
					}
				})
			},
			
			loadRecentRecords() {
				const token = uni.getStorageSync('token')
				uni.request({
					url: getApiUrl('/inspection-record/page?pageNum=1&pageSize=5'),
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
						console.log('加载最近记录失败:', error)
					}
				})
			},
			getStatusText(status: string): string {
			if (status == "0") return '待整改'
			if (status == "1") return '已整改'
			if (status == "2") return '已复查'
			return '未知'
		},
		
		getStatusClass(status: string): string {
			if (status == "0") return 'status-pending'
			if (status == "1") return 'status-completed'
			if (status == "2") return 'status-accepted'
			return 'status-unknown'
		},
			
			goToPendingIssues() {
				uni.navigateTo({
					url: '/pages/record/record?status=0&monthly=true'
				})
			},
			
			goToCompletedIssues() {
				uni.navigateTo({
					url: '/pages/record/record?status=1&monthly=true'
				})
			},
			
			goToReviewedRecords() {
				uni.navigateTo({
					url: '/pages/record/record?status=2&monthly=true'
				})
			},
			
			goToRecords() {
				uni.switchTab({
					url: '/pages/record/record'
				})
			},
			
			goToStatistics() {
				uni.navigateTo({
					url: '/pages/statistics/statistics'
				})
			},
			
			goToAddRecord() {
				uni.navigateTo({
					url: '/pages/record/add'
				})
			}
		}
	})

export default __sfc__
function GenPagesIndexIndexRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
  return _cE("view", _uM({ class: "container" }), [
    _cE("view", _uM({ class: "header" }), [
      _cE("view", _uM({ class: "welcome" }), [
        _cE("text", _uM({ class: "welcome-text" }), "欢迎回来，" + _tD((_ctx.userInfo as UTSJSONObject).getString('realName') ?? '用户'), 1 /* TEXT */),
        _cE("text", _uM({ class: "date-text" }), _tD(_ctx.currentDate), 1 /* TEXT */)
      ]),
      _cE("image", _uM({
        class: "avatar",
        src: "/static/avatar.png"
      }))
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
        _cE("text", _uM({ class: "stats-number" }), _tD(_ctx.statistics.monthRecords), 1 /* TEXT */),
        _cE("text", _uM({ class: "stats-label" }), "已复查"),
        _cE("text", _uM({ class: "stats-icon" }), "📊")
      ], 8 /* PROPS */, ["onClick"])
    ]),
    _cE("view", _uM({ class: "quick-actions" }), [
      _cE("text", _uM({ class: "section-title" }), "快捷功能"),
      _cE("view", _uM({ class: "actions-grid" }), [
        _cE("view", _uM({
          class: "action-item",
          onClick: _ctx.goToAddRecord
        }), [
          _cE("view", _uM({ class: "action-icon" }), "➕"),
          _cE("text", _uM({ class: "action-text" }), "新增记录")
        ], 8 /* PROPS */, ["onClick"]),
        _cE("view", _uM({
          class: "action-item",
          onClick: _ctx.goToPendingIssues
        }), [
          _cE("view", _uM({ class: "action-icon" }), "⚠️"),
          _cE("text", _uM({ class: "action-text" }), "待整改问题")
        ], 8 /* PROPS */, ["onClick"]),
        _cE("view", _uM({
          class: "action-item",
          onClick: _ctx.goToRecords
        }), [
          _cE("view", _uM({ class: "action-icon" }), "📝"),
          _cE("text", _uM({ class: "action-text" }), "巡检记录")
        ], 8 /* PROPS */, ["onClick"]),
        _cE("view", _uM({
          class: "action-item",
          onClick: _ctx.goToCompletedIssues
        }), [
          _cE("view", _uM({ class: "action-icon" }), "✅"),
          _cE("text", _uM({ class: "action-text" }), "已整改问题")
        ], 8 /* PROPS */, ["onClick"]),
        _cE("view", _uM({
          class: "action-item",
          onClick: _ctx.goToStatistics
        }), [
          _cE("view", _uM({ class: "action-icon" }), "📊"),
          _cE("text", _uM({ class: "action-text" }), "统计分析")
        ], 8 /* PROPS */, ["onClick"])
      ])
    ]),
    _cE("view", _uM({ class: "recent-records" }), [
      _cE("view", _uM({ class: "section-header" }), [
        _cE("text", _uM({ class: "section-title" }), "最近记录"),
        _cE("text", _uM({
          class: "more-text",
          onClick: _ctx.goToRecords
        }), "查看更多", 8 /* PROPS */, ["onClick"])
      ]),
      _cE("view", _uM({ class: "records-list" }), [
        _cE(Fragment, null, RenderHelpers.renderList(_ctx.recentRecords, (record, __key, __index, _cached): any => {
          return _cE("view", _uM({
            class: "record-item",
            key: record.id
          }), [
            _cE("view", _uM({ class: "record-info" }), [
              _cE("text", _uM({ class: "record-title" }), _tD(record.problemDescription), 1 /* TEXT */),
              _cE("text", _uM({ class: "record-meta" }), _tD(record.locationName) + " · " + _tD(record.createTime), 1 /* TEXT */)
            ]),
            _cE("view", _uM({
              class: _nC(["record-status", _ctx.getStatusClass(record.status)])
            }), [
              _cE("text", null, _tD(_ctx.getStatusText(record.status)), 1 /* TEXT */)
            ], 2 /* CLASS */)
          ])
        }), 128 /* KEYED_FRAGMENT */),
        _ctx.recentRecords.length === 0
          ? _cE("view", _uM({
              key: 0,
              class: "empty-state"
            }), [
              _cE("text", _uM({ class: "empty-text" }), "暂无记录")
            ])
          : _cC("v-if", true)
      ])
    ])
  ])
}
const GenPagesIndexIndexStyles = [_uM([["container", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["backgroundColor", "#f5f5f5"]]))], ["header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["marginBottom", 20]]))], ["welcome", _pS(_uM([["flex", 1]]))], ["greeting", _uM([[".welcome-text ", _uM([["fontSize", 20], ["fontWeight", "bold"], ["color", "#333333"], ["marginBottom", 4]])]])], ["date-text", _pS(_uM([["fontSize", 14], ["color", "#666666"]]))], ["avatar", _pS(_uM([["width", 50], ["height", 50], ["borderTopLeftRadius", 25], ["borderTopRightRadius", 25], ["borderBottomRightRadius", 25], ["borderBottomLeftRadius", 25], ["backgroundColor", "#dddddd"]]))], ["stats-container", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["marginBottom", 30]]))], ["stats-card", _pS(_uM([["flex", 1], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["paddingTop", 20], ["paddingRight", 15], ["paddingBottom", 20], ["paddingLeft", 15], ["marginTop", 0], ["marginRight", 5], ["marginBottom", 0], ["marginLeft", 5], ["textAlign", "center"], ["position", "relative"], ["boxShadow", "0 2px 8px rgba(0,0,0,0.1)"]]))], ["stats-number", _pS(_uM([["fontSize", 24], ["fontWeight", "bold"], ["color", "#007AFF"], ["marginBottom", 5]]))], ["stats-label", _pS(_uM([["fontSize", 12], ["color", "#666666"]]))], ["stats-icon", _pS(_uM([["position", "absolute"], ["top", 15], ["right", 15], ["fontSize", 20], ["opacity", 0.3]]))], ["quick-actions", _pS(_uM([["marginBottom", 30]]))], ["section-title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#333333"], ["marginBottom", 15]]))], ["actions-grid", _pS(_uM([["display", "flex"], ["flexWrap", "wrap"], ["justifyContent", "space-between"]]))], ["action-item", _pS(_uM([["width", "48%"], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["marginBottom", 10], ["textAlign", "center"], ["boxShadow", "0 2px 8px rgba(0,0,0,0.1)"]]))], ["action-icon", _pS(_uM([["fontSize", 32], ["marginBottom", 10]]))], ["action-text", _pS(_uM([["fontSize", 14], ["color", "#333333"]]))], ["recent-records", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["boxShadow", "0 2px 8px rgba(0,0,0,0.1)"]]))], ["section-header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["marginBottom", 15]]))], ["more-text", _pS(_uM([["fontSize", 14], ["color", "#007AFF"]]))], ["record-item", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 15], ["paddingRight", 0], ["paddingBottom", 15], ["paddingLeft", 0], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"], ["borderBottomWidth:last-child", "medium"], ["borderBottomStyle:last-child", "none"], ["borderBottomColor:last-child", "#000000"]]))], ["record-info", _pS(_uM([["flex", 1]]))], ["record-title", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["marginBottom", 5]]))], ["record-meta", _pS(_uM([["fontSize", 12], ["color", "#999999"]]))], ["record-status", _pS(_uM([["paddingTop", 4], ["paddingRight", 8], ["paddingBottom", 4], ["paddingLeft", 8], ["borderTopLeftRadius", 4], ["borderTopRightRadius", 4], ["borderBottomRightRadius", 4], ["borderBottomLeftRadius", 4], ["fontSize", 12]]))], ["status-pending", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#fff3cd"], ["color", "#856404"]]))], ["status-processing", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#d1ecf1"], ["color", "#0c5460"]]))], ["status-completed", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#d4edda"], ["color", "#155724"]]))], ["status-accepted", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#e2e3e5"], ["color", "#383d41"]]))], ["empty-state", _pS(_uM([["textAlign", "center"], ["paddingTop", 40], ["paddingRight", 0], ["paddingBottom", 40], ["paddingLeft", 0]]))], ["empty-text", _pS(_uM([["fontSize", 14], ["color", "#999999"]]))]])]
