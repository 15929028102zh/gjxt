
	import { getApiUrl } from '@/config/api.uts'
	type MonthlyRecord = {
		id: string,
		problemDescription: string,
		locationName: string,
		createTime: string,
		status: string
	}

	type MonthlyStatistics = {
		monthlyPending: number,
		monthlyCompleted: number,
		monthlyTotal: number
	}

	type Notification = {
		id: string,
		taskId: string,
		taskNo: string,
		title: string,
		content: string,
		createTime: string,
		isRead: boolean
	}

	const __sfc__ = defineComponent({
		data() {
				return {
					userInfo: {} as UTSJSONObject,
					userName: '用户', // 简化的用户姓名变量
					currentDate: '',
					statistics: {
						monthlyPending: 0,
						monthlyCompleted: 0,
						monthlyTotal: 0
					} as MonthlyStatistics,
					monthlyRecords: [] as MonthlyRecord[],
					notifications: [] as Notification[],
					notificationCount: 0,
					showNotificationModal: false,
					// 滚动相关
					scrollTop: 0,
					containerHeight: 0,
					// 下拉刷新相关
					isRefreshing: false
				}
			},
		onLoad(options: OnLoadOptions) {
			this.checkLogin()
			this.initData()
			this.initContainerHeight()
			this.loadStatistics()
			this.loadMonthlyRecords()
			this.loadNotifications()
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
					// 设置用户名
					const realName = this.userInfo.getString('realName') ?? ''
					this.userName = realName != '' ? realName : '用户'
				} else {
					this.userInfo = {} as UTSJSONObject
					this.userName = '用户'
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
				
				// 获取用户部门信息
				let departmentId = ''
				const userInfoData = uni.getStorageSync('userInfo')
				const userInfoStr = userInfoData != null ? userInfoData as string : ''
				if (userInfoStr != null && userInfoStr != '' && userInfoStr != 'null' && userInfoStr != 'undefined') {
					try {
						const parsedUserInfo = JSON.parse(userInfoStr) as UTSJSONObject
						departmentId = parsedUserInfo.getString('deptId') ?? ''
					} catch (error) {
						console.error('用户信息JSON解析失败:', error)
					}
				}
				
				// 构建部门过滤参数
				const deptParam = departmentId != null && departmentId != '' ? `?departmentId=${departmentId}` : ''
				
				uni.request({
					url: getApiUrl(`/statistics/overview${deptParam}`),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						const response = res.data as UTSJSONObject
						if (res.statusCode == 200 && (response.getNumber('code') ?? 0) == 200) {
							const data = response.getJSON('data') as UTSJSONObject
							// 暂时使用原有接口数据，后续可调整
							this.statistics = {
								monthlyPending: data.getNumber('totalTasks') ?? 0,
								monthlyCompleted: data.getNumber('todayRecords') ?? 0,
								monthlyTotal: data.getNumber('monthRecords') ?? 0
							}
						}
					},
					fail: (error) => {
						console.log('加载统计数据失败:', error)
					}
				})
			},
			
			loadMonthlyRecords() {
				const token = uni.getStorageSync('token')
				
				// 获取用户部门信息
				let departmentId = ''
				const userInfoData = uni.getStorageSync('userInfo')
				const userInfoStr = userInfoData != null ? userInfoData as string : ''
				if (userInfoStr != null && userInfoStr != '' && userInfoStr != 'null' && userInfoStr != 'undefined') {
					try {
						const parsedUserInfo = JSON.parse(userInfoStr) as UTSJSONObject
						departmentId = parsedUserInfo.getString('deptId') ?? ''
					} catch (error) {
						console.error('用户信息JSON解析失败:', error)
					}
				}
				
				// 构建部门过滤参数
				const deptParam = departmentId != null && departmentId != '' ? `&departmentId=${departmentId}` : ''
				
				uni.request({
					url: getApiUrl(`/inspection-record/page?pageNum=1&pageSize=10${deptParam}`),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						const response = res.data as UTSJSONObject
						if (res.statusCode == 200 && (response.getNumber('code') ?? 0) == 200) {
							const data = response.getJSON('data') as UTSJSONObject
							const records = data.getArray('records') as Array<UTSJSONObject>
							this.monthlyRecords = []
							for (let i = 0; i < records.length; i++) {
								const item = records[i]
								const record: MonthlyRecord = {
									id: item.getString('id') ?? '',
									problemDescription: item.getString('problemDescription') ?? '',
									locationName: item.getString('locationName') ?? '',
									createTime: item.getString('createTime') ?? '',
									status: item.getString('status') ?? ''
								}
								this.monthlyRecords.push(record)
							}
						}
					},
					fail: (error) => {
						console.log('加载月度记录失败:', error)
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
					url: '/pages/record/record?status=0'
				})
			},
			
			goToCompletedIssues() {
				uni.navigateTo({
					url: '/pages/record/record?status=1'
				})
			},
			
			goToMonthlyReport() {
				uni.navigateTo({
					url: '/pages/statistics/statistics'
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
			
			loadNotifications() {
				const token = uni.getStorageSync('token')
				const userId = this.userInfo.getString('id') ?? ''
				console.log('开始加载通知，用户ID:', userId)
				
      if (userId == null || userId == '') {
       console.log('用户ID为空，无法加载通知')
       return
				}
				
				uni.request({
					url: getApiUrl(`/monthly-inspection-task/user/${userId}`),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						console.log('通知请求响应:', res)
						const response = res.data as UTSJSONObject
						if (res.statusCode == 200 && (response.getNumber('code') ?? 0) == 200) {
							const data = response.getArray('data') as Array<UTSJSONObject>
							console.log('获取到的通知数据:', data.length, '条')
							this.notifications = []
							let unreadCount = 0
							for (let i = 0; i < data.length; i++) {
								const item = data[i]
								const notification: Notification = {
									id: item.getString('id') ?? '',
									taskId: item.getString('taskId') ?? '',
									taskNo: item.getString('taskNo') ?? '',
									title: `月检任务通知 - ${item.getString('taskNo')}`,
									content: `您有新的月检任务需要处理，任务编号：${item.getString('taskNo')}`,
									createTime: item.getString('createTime') ?? '',
									isRead: false
								}
								this.notifications.push(notification)
								if (!notification.isRead) {
									unreadCount++
								}
							}
							this.notificationCount = unreadCount
							console.log('设置通知条数:', this.notificationCount)
						} else {
							console.log('通知请求失败，响应码:', res.statusCode, '业务码:', response.getNumber('code'))
						}
					},
					fail: (error) => {
						console.log('加载通知失败:', error)
					}
				})
			},
			
			showNotifications() {
				this.showNotificationModal = true
			},
			
			hideNotifications() {
				this.showNotificationModal = false
			},
			
			handleNotificationClick(notification: Notification) {
				// 标记为已读
				notification.isRead = true
				this.updateNotificationCount()
				
				// 跳转到月检任务录入页面，传递任务信息
				uni.navigateTo({
					url: `/pages/monthly/task-record?taskNo=${notification.taskNo}`
				})
				
				this.hideNotifications()
			},
			
			updateNotificationCount() {
				let unreadCount = 0
				for (let i = 0; i < this.notifications.length; i++) {
					if (!this.notifications[i].isRead) {
						unreadCount++
					}
				}
				this.notificationCount = unreadCount
			},
			
			goToProfile() {
				uni.switchTab({
					url: '/pages/profile/profile'
				})
			},
			
			markAllAsRead() {
				for (let i = 0; i < this.notifications.length; i++) {
					this.notifications[i].isRead = true
				}
				this.notificationCount = 0
			},
			
			// 初始化容器高度
			initContainerHeight() {
				const systemInfo = uni.getSystemInfoSync()
				this.containerHeight = systemInfo.windowHeight
			},
			
			// 下拉刷新
			onRefresh() {
				this.isRefreshing = true
				// 重新加载数据
				this.loadStatistics()
				this.loadMonthlyRecords()
				this.loadNotifications()
				// 模拟刷新延迟
				setTimeout(() => {
					this.isRefreshing = false
				}, 1000)
			},
			
			// 刷新恢复
			onRefreshRestore() {
				this.isRefreshing = false
			}
		}
	})

export default __sfc__
function GenPagesMonthlyMonthlyRender(this: InstanceType<typeof __sfc__>): any | null {
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
        _cE("text", _uM({ class: "app-name" }), _tD(_ctx.currentDate), 1 /* TEXT */),
        _cE("view", _uM({
          class: "notification-icon-wrapper",
          onClick: _ctx.showNotifications
        }), [
          _cE("text", _uM({ class: "icon-bell" }), "🔔"),
          isTrue(_ctx.notificationCount > 0 && _ctx.notificationCount <= 99)
            ? _cE("view", _uM({
                key: 0,
                class: "notification-badge"
              }), [
                _cE("text", _uM({ class: "badge-text" }), _tD(_ctx.notificationCount), 1 /* TEXT */)
              ])
            : _ctx.notificationCount > 99
              ? _cE("view", _uM({
                  key: 1,
                  class: "notification-badge"
                }), [
                  _cE("text", _uM({ class: "badge-text" }), "99+")
                ])
              : _cC("v-if", true)
        ], 8 /* PROPS */, ["onClick"])
      ]),
      _cE("view", _uM({ class: "header-right" }), [
        _cE("view", _uM({
          class: "user-avatar",
          onClick: _ctx.goToProfile
        }), [
          isTrue(_ctx.userInfo != null && (_ctx.userInfo.getString('avatar') ?? '') != '')
            ? _cE("image", _uM({
                key: 0,
                class: "avatar-image",
                src: _ctx.userInfo.getString('avatar') ?? '',
                mode: "aspectFill"
              }), null, 8 /* PROPS */, ["src"])
            : _cE("text", _uM({
                key: 1,
                class: "avatar-placeholder"
              }), _tD(_ctx.userName.charAt(0)), 1 /* TEXT */)
        ], 8 /* PROPS */, ["onClick"])
      ])
    ]),
    _cE("view", _uM({ class: "stats-container" }), [
      _cE("view", _uM({
        class: "stats-card",
        onClick: _ctx.goToPendingIssues
      }), [
        _cE("text", _uM({ class: "stats-number" }), _tD(_ctx.statistics.monthlyPending), 1 /* TEXT */),
        _cE("text", _uM({ class: "stats-label" }), "本月待整改"),
        _cE("text", _uM({ class: "stats-icon" }), "⚠️")
      ], 8 /* PROPS */, ["onClick"]),
      _cE("view", _uM({
        class: "stats-card",
        onClick: _ctx.goToCompletedIssues
      }), [
        _cE("text", _uM({ class: "stats-number" }), _tD(_ctx.statistics.monthlyCompleted), 1 /* TEXT */),
        _cE("text", _uM({ class: "stats-label" }), "本月已整改"),
        _cE("text", _uM({ class: "stats-icon" }), "✅")
      ], 8 /* PROPS */, ["onClick"]),
      _cE("view", _uM({
        class: "stats-card",
        onClick: _ctx.goToMonthlyReport
      }), [
        _cE("text", _uM({ class: "stats-number" }), _tD(_ctx.statistics.monthlyTotal), 1 /* TEXT */),
        _cE("text", _uM({ class: "stats-label" }), "本月总记录"),
        _cE("text", _uM({ class: "stats-icon" }), "📊")
      ], 8 /* PROPS */, ["onClick"])
    ]),
    _cE("view", _uM({ class: "trend-section" }), [
      _cE("text", _uM({ class: "section-title" }), "月度趋势"),
      _cE("view", _uM({ class: "trend-chart" }), [
        _cE("view", _uM({ class: "chart-placeholder" }), [
          _cE("text", _uM({ class: "placeholder-text" }), "月度趋势图"),
          _cE("text", _uM({ class: "placeholder-desc" }), "显示本月问题发现与整改趋势")
        ])
      ])
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
          onClick: _ctx.goToMonthlyReport
        }), [
          _cE("view", _uM({ class: "action-icon" }), "📊"),
          _cE("text", _uM({ class: "action-text" }), "月度报告")
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
          onClick: _ctx.goToCompletedIssues
        }), [
          _cE("view", _uM({ class: "action-icon" }), "✅"),
          _cE("text", _uM({ class: "action-text" }), "已整改问题")
        ], 8 /* PROPS */, ["onClick"])
      ])
    ]),
    _cE("view", _uM({ class: "recent-records" }), [
      _cE("view", _uM({ class: "section-header" }), [
        _cE("text", _uM({ class: "section-title" }), "本月记录"),
        _cE("text", _uM({
          class: "more-text",
          onClick: _ctx.goToRecords
        }), "查看更多", 8 /* PROPS */, ["onClick"])
      ]),
      _cE("view", _uM({ class: "records-list" }), [
        _cE(Fragment, null, RenderHelpers.renderList(_ctx.monthlyRecords, (record, __key, __index, _cached): any => {
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
        _ctx.monthlyRecords.length === 0
          ? _cE("view", _uM({
              key: 0,
              class: "empty-state"
            }), [
              _cE("text", _uM({ class: "empty-text" }), "本月暂无记录")
            ])
          : _cC("v-if", true)
      ])
    ]),
    isTrue(_ctx.showNotificationModal)
      ? _cE("view", _uM({
          key: 0,
          class: "notification-modal",
          onClick: _ctx.hideNotifications
        }), [
          _cE("view", _uM({
            class: "notification-content",
            onClick: withModifiers(() => {}, ["stop"])
          }), [
            _cE("view", _uM({ class: "notification-header" }), [
              _cE("text", _uM({ class: "notification-title" }), "通知消息"),
              _cE("view", _uM({ class: "notification-actions" }), [
                _cE("text", _uM({
                  class: "mark-all-read",
                  onClick: _ctx.markAllAsRead
                }), "全部已读", 8 /* PROPS */, ["onClick"])
              ])
            ]),
            _cE("view", _uM({ class: "notification-list" }), [
              _ctx.notifications.length === 0
                ? _cE("view", _uM({
                    key: 0,
                    class: "empty-notifications"
                  }), [
                    _cE("text", _uM({ class: "empty-text" }), "暂无通知")
                  ])
                : _cC("v-if", true),
              _cE(Fragment, null, RenderHelpers.renderList(_ctx.notifications, (notification, __key, __index, _cached): any => {
                return _cE("view", _uM({
                  key: notification.id,
                  class: _nC(["notification-item", _uM({unread: !notification.isRead})]),
                  onClick: () => {_ctx.handleNotificationClick(notification)}
                }), [
                  isTrue(!notification.isRead)
                    ? _cE("view", _uM({
                        key: 0,
                        class: "notification-dot"
                      }))
                    : _cC("v-if", true),
                  _cE("view", _uM({ class: "notification-info" }), [
                    _cE("text", _uM({ class: "notification-item-title" }), _tD(notification.title), 1 /* TEXT */),
                    _cE("text", _uM({ class: "notification-item-content" }), _tD(notification.content), 1 /* TEXT */),
                    _cE("text", _uM({ class: "notification-time" }), _tD(notification.createTime), 1 /* TEXT */)
                  ]),
                  _cE("text", _uM({ class: "notification-arrow" }), "›")
                ], 10 /* CLASS, PROPS */, ["onClick"])
              }), 128 /* KEYED_FRAGMENT */)
            ])
          ], 8 /* PROPS */, ["onClick"])
        ], 8 /* PROPS */, ["onClick"])
      : _cC("v-if", true)
  ], 44 /* STYLE, PROPS, NEED_HYDRATION */, ["scroll-top", "refresher-triggered", "onRefresherrefresh", "onRefresherrestore"])
}
const GenPagesMonthlyMonthlyStyles = [_uM([["container", _pS(_uM([["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "150rpx"], ["paddingLeft", "30rpx"], ["backgroundImage", "linear-gradient(180deg, #f8fafc 0%, #e2e8f0 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxSizing", "border-box"]]))], ["header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", "30rpx"], ["paddingRight", "25rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "25rpx"], ["backgroundImage", "linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["borderTopLeftRadius", "24rpx"], ["borderTopRightRadius", "24rpx"], ["borderBottomRightRadius", "24rpx"], ["borderBottomLeftRadius", "24rpx"], ["marginBottom", "30rpx"], ["boxShadow", "0 8rpx 32rpx rgba(102, 126, 234, 0.3)"], ["position", "relative"], ["overflow", "hidden"], ["content::before", "''"], ["position::before", "absolute"], ["top::before", 0], ["left::before", 0], ["right::before", 0], ["bottom::before", 0], ["pointerEvents::before", "none"]]))], ["header-right", _pS(_uM([["display", "flex"], ["alignItems", "flex-start"], ["gap", "15rpx"], ["zIndex", 2], ["position", "relative"], ["marginTop", "30rpx"]]))], ["welcome", _pS(_uM([["display", "flex"], ["flexDirection", "column"], ["flex", 1], ["zIndex", 2], ["position", "relative"], ["paddingRight", "140rpx"]]))], ["greeting", _pS(_uM([["color", "rgba(105,72,72,0.95)"], ["fontSize", "32rpx"], ["marginBottom", "8rpx"], ["letterSpacing", "0.5rpx"], ["textShadow", "0 2rpx 4rpx rgba(0, 0, 0, 0.1)"]]))], ["app-name", _pS(_uM([["color", "rgba(105,72,72,0.95)"], ["fontSize", "26rpx"], ["fontWeight", "400"], ["letterSpacing", "0.3rpx"]]))], ["notification-icon-wrapper", _pS(_uM([["position", "absolute"], ["top", "15rpx"], ["right", "15rpx"], ["width", "70rpx"], ["height", "70rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.2)"], ["backdropFilter", "blur(10rpx)"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "rgba(255,255,255,0.3)"], ["borderRightColor", "rgba(255,255,255,0.3)"], ["borderBottomColor", "rgba(255,255,255,0.3)"], ["borderLeftColor", "rgba(255,255,255,0.3)"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["zIndex", 3], ["transform:active", "scale(0.95)"], ["backgroundImage:active", "none"], ["backgroundColor:active", "rgba(255,255,255,0.3)"]]))], ["icon-bell", _pS(_uM([["fontSize", "32rpx"], ["color", "rgba(255,255,255,0.9)"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["width", "100%"], ["height", "100%"]]))], ["notification-badge", _pS(_uM([["position", "absolute"], ["top", "-6rpx"], ["right", "-6rpx"], ["minWidth", "32rpx"], ["height", "32rpx"], ["backgroundImage", "none"], ["backgroundColor", "#ff4757"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["borderTopWidth", "3rpx"], ["borderRightWidth", "3rpx"], ["borderBottomWidth", "3rpx"], ["borderLeftWidth", "3rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#ffffff"], ["borderRightColor", "#ffffff"], ["borderBottomColor", "#ffffff"], ["borderLeftColor", "#ffffff"], ["boxShadow", "0 2rpx 8rpx rgba(255, 71, 87, 0.3)"], ["zIndex", 4], ["paddingTop", 0], ["paddingRight", "6rpx"], ["paddingBottom", 0], ["paddingLeft", "6rpx"]]))], ["badge-text", _pS(_uM([["fontSize", "18rpx"], ["color", "#ffffff"], ["fontWeight", "700"], ["lineHeight", 1], ["textAlign", "center"], ["whiteSpace", "nowrap"]]))], ["user-avatar", _pS(_uM([["width", "100rpx"], ["height", "100rpx"], ["borderTopLeftRadius", "50rpx"], ["borderTopRightRadius", "50rpx"], ["borderBottomRightRadius", "50rpx"], ["borderBottomLeftRadius", "50rpx"], ["backgroundColor", "rgba(255,255,255,0.2)"], ["overflow", "hidden"], ["borderTopWidth", "3rpx"], ["borderRightWidth", "3rpx"], ["borderBottomWidth", "3rpx"], ["borderLeftWidth", "3rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "rgba(255,255,255,0.4)"], ["borderRightColor", "rgba(255,255,255,0.4)"], ["borderBottomColor", "rgba(255,255,255,0.4)"], ["borderLeftColor", "rgba(255,255,255,0.4)"], ["boxShadow", "0 6rpx 20rpx rgba(0, 0, 0, 0.15), 0 0 0 1rpx rgba(255, 255, 255, 0.1)"], ["zIndex", 2], ["position", "relative"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["transform:active", "scale(0.95)"]]))], ["avatar-image", _pS(_uM([["width", "100rpx"], ["height", "100rpx"], ["borderTopLeftRadius", "50rpx"], ["borderTopRightRadius", "50rpx"], ["borderBottomRightRadius", "50rpx"], ["borderBottomLeftRadius", "50rpx"]]))], ["avatar-placeholder", _pS(_uM([["color", "rgba(255,255,255,0.9)"], ["fontSize", "28rpx"], ["textTransform", "uppercase"]]))], ["avatar", _pS(_uM([["width", 50], ["height", 50], ["borderTopLeftRadius", 25], ["borderTopRightRadius", 25], ["borderBottomRightRadius", 25], ["borderBottomLeftRadius", 25], ["backgroundColor", "#dddddd"]]))], ["stats-container", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["marginBottom", 30]]))], ["stats-card", _pS(_uM([["flex", 1], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["paddingTop", 20], ["paddingRight", 15], ["paddingBottom", 20], ["paddingLeft", 15], ["marginTop", 0], ["marginRight", 5], ["marginBottom", 0], ["marginLeft", 5], ["textAlign", "center"], ["position", "relative"], ["boxShadow", "0 2px 8px rgba(0,0,0,0.1)"]]))], ["stats-number", _pS(_uM([["fontSize", 24], ["fontWeight", "bold"], ["color", "#007AFF"], ["marginBottom", 8]]))], ["stats-label", _pS(_uM([["fontSize", 14], ["color", "#666666"], ["marginBottom", 8]]))], ["stats-icon", _pS(_uM([["fontSize", 20], ["position", "absolute"], ["top", 15], ["right", 15]]))], ["trend-section", _pS(_uM([["marginBottom", 30]]))], ["trend-chart", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["boxShadow", "0 2px 8px rgba(0,0,0,0.1)"]]))], ["chart-placeholder", _pS(_uM([["height", 200], ["display", "flex"], ["flexDirection", "column"], ["justifyContent", "center"], ["alignItems", "center"], ["backgroundColor", "#f8f9fa"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "dashed"], ["borderRightStyle", "dashed"], ["borderBottomStyle", "dashed"], ["borderLeftStyle", "dashed"], ["borderTopColor", "#dee2e6"], ["borderRightColor", "#dee2e6"], ["borderBottomColor", "#dee2e6"], ["borderLeftColor", "#dee2e6"]]))], ["placeholder-text", _pS(_uM([["fontSize", 18], ["color", "#6c757d"], ["marginBottom", 8]]))], ["placeholder-desc", _pS(_uM([["fontSize", 14], ["color", "#adb5bd"]]))], ["quick-actions", _pS(_uM([["marginBottom", 30]]))], ["section-title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#333333"], ["marginBottom", 15]]))], ["actions-grid", _pS(_uM([["display", "flex"], ["flexWrap", "wrap"], ["justifyContent", "space-between"]]))], ["action-item", _pS(_uM([["width", "48%"], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["marginBottom", 15], ["textAlign", "center"], ["boxShadow", "0 2px 8px rgba(0,0,0,0.1)"]]))], ["action-icon", _pS(_uM([["fontSize", 24], ["marginBottom", 8]]))], ["action-text", _pS(_uM([["fontSize", 14], ["color", "#333333"]]))], ["recent-records", _pS(_uM([["marginBottom", 20]]))], ["section-header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["marginBottom", 15]]))], ["more-text", _pS(_uM([["fontSize", 14], ["color", "#007AFF"]]))], ["records-list", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["paddingTop", 15], ["paddingRight", 15], ["paddingBottom", 15], ["paddingLeft", 15], ["boxShadow", "0 2px 8px rgba(0,0,0,0.1)"]]))], ["record-item", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 15], ["paddingRight", 0], ["paddingBottom", 15], ["paddingLeft", 0], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"], ["borderBottomWidth:last-child", "medium"], ["borderBottomStyle:last-child", "none"], ["borderBottomColor:last-child", "#000000"]]))], ["record-info", _pS(_uM([["flex", 1]]))], ["record-title", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["marginBottom", 4]]))], ["record-meta", _pS(_uM([["fontSize", 12], ["color", "#999999"]]))], ["record-status", _pS(_uM([["paddingTop", 4], ["paddingRight", 8], ["paddingBottom", 4], ["paddingLeft", 8], ["borderTopLeftRadius", 4], ["borderTopRightRadius", 4], ["borderBottomRightRadius", 4], ["borderBottomLeftRadius", 4], ["fontSize", 12]]))], ["status-pending", _pS(_uM([["backgroundColor", "#fff3cd"], ["color", "#856404"]]))], ["status-processing", _pS(_uM([["backgroundColor", "#d1ecf1"], ["color", "#0c5460"]]))], ["status-completed", _pS(_uM([["backgroundColor", "#d4edda"], ["color", "#155724"]]))], ["status-accepted", _pS(_uM([["backgroundColor", "#e2e3e5"], ["color", "#383d41"]]))], ["status-unknown", _pS(_uM([["backgroundColor", "#f8f9fa"], ["color", "#6c757d"]]))], ["empty-state", _pS(_uM([["textAlign", "center"], ["paddingTop", 40], ["paddingRight", 20], ["paddingBottom", 40], ["paddingLeft", 20]]))], ["empty-text", _pS(_uM([["fontSize", 14], ["color", "#999999"]]))], ["notification-modal", _pS(_uM([["position", "fixed"], ["top", 0], ["left", 0], ["right", 0], ["bottom", 0], ["backgroundColor", "rgba(0,0,0,0.5)"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["zIndex", 1000]]))], ["notification-content", _pS(_uM([["width", "90%"], ["maxWidth", 400], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["overflow", "hidden"]]))], ["notification-header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"]]))], ["notification-title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#333333"]]))], ["notification-actions", _pS(_uM([["display", "flex"], ["alignItems", "center"], ["gap", "15px"]]))], ["mark-all-read", _pS(_uM([["fontSize", 14], ["color", "#007AFF"]]))], ["close-btn", _pS(_uM([["fontSize", 20], ["color", "#999999"], ["width", 30], ["height", 30], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["notification-list", _pS(_uM([["maxHeight", 400], ["overflowY", "auto"]]))], ["empty-notifications", _pS(_uM([["paddingTop", 40], ["paddingRight", 20], ["paddingBottom", 40], ["paddingLeft", 20], ["textAlign", "center"]]))], ["notification-item", _uM([["", _uM([["display", "flex"], ["alignItems", "center"], ["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"], ["position", "relative"], ["borderBottomWidth:last-child", "medium"], ["borderBottomStyle:last-child", "none"], ["borderBottomColor:last-child", "#000000"]])], [".unread", _uM([["backgroundColor", "#f8f9ff"]])]])], ["notification-dot", _pS(_uM([["width", 8], ["height", 8], ["backgroundColor", "#007AFF"], ["borderTopLeftRadius", 4], ["borderTopRightRadius", 4], ["borderBottomRightRadius", 4], ["borderBottomLeftRadius", 4], ["marginRight", 12], ["flexShrink", 0]]))], ["notification-info", _pS(_uM([["flex", 1], ["marginRight", 10]]))], ["notification-item-title", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["marginBottom", 4]]))], ["notification-item-content", _pS(_uM([["fontSize", 14], ["color", "#666666"], ["marginBottom", 4], ["lineHeight", 1.4]]))], ["notification-time", _pS(_uM([["fontSize", 12], ["color", "#999999"]]))], ["notification-arrow", _pS(_uM([["fontSize", 18], ["color", "#cccccc"], ["flexShrink", 0]]))], ["@TRANSITION", _uM([["notification-icon-wrapper", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "ease"]])], ["user-avatar", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "ease"]])]])]])]
