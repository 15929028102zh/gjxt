
	import { getApiUrl } from '@/config/api.uts'
	type RecordStatusTab = {
		key: string
		label: string
		value: string
	}

  type InspectionRecord = {
    id: string
    problemDescription: string
    responsiblePerson: string
    inspectorName: string
    createDate: string
    status: number
    rectificationDate: string
  }
	
	const __sfc__ = defineComponent({
		data() {
      return {
				recordList: [] as InspectionRecord[],
				loading: false,
				loadingMore: false,
				hasMore: true,
				totalCount: 0,
        pageNum: 1,
        pageSize: 20,
        status: '0', // 默认显示待整改记录
        isDelayed: false, // 是否是延迟整改
        isReviewed: false, // 是否是已复查记录
        isMonthly: false, // 是否是月检记录
        deadlineBefore: '', // 延迟过滤上限（yyyy-MM-dd）
        deadlineFrom: '', // 待整改过滤下限（yyyy-MM-dd）
        isRefreshing: false, // 下拉刷新状态
        containerHeight: 0 // 容器高度
      }
		},
		onLoad(options: OnLoadOptions) {
				this.checkLogin()
				// 从URL参数获取status值
				if (options['status'] != null) {
					this.status = options['status'] as string
				}
				// 检查是否是延迟整改
				if (options['delayed'] != null && options['delayed'] == 'true') {
					this.isDelayed = true
				}
				// 检查是否是已复查记录
				if (options['type'] != null && options['type'] == 'reviewed') {
					this.isReviewed = true
					this.status = '2' // 设置状态为已复查
				}
				// 检查是否是月检记录
        if (options['monthly'] != null && options['monthly'] == 'true') {
          this.isMonthly = true
        }
        if (options['deadlineBefore'] != null) {
          this.deadlineBefore = options['deadlineBefore'] as string
        }
        if (options['deadlineFrom'] != null) {
          this.deadlineFrom = options['deadlineFrom'] as string
        }
				this.initContainerHeight()
				this.loadRecords()
			
			// 监听全局事件，当记录更新时刷新数据
			uni.$on('recordUpdated', this.handleRecordUpdated)
		},
		onShow() {
			// 页面显示时刷新数据，以便及时反映数据变化
			this.refreshRecords()
		},
		onUnload() {
			// 页面卸载时移除事件监听
			uni.$off('recordUpdated', this.handleRecordUpdated)
		},
		onPullDownRefresh() {
			this.refreshRecords()
		},
		onReachBottom() {
			if (this.hasMore && !this.loadingMore) {
				this.loadMore()
			}
		},
			methods: {
			checkLogin() {
				const tokenData = uni.getStorageSync('token')
				const token = tokenData != null ? tokenData as string : ''
				if (token == null || token == '') {
					uni.reLaunch({
						url: '/pages/login/login'
					})
				}
			},
			
			// 处理记录更新事件
			handleRecordUpdated() {
				console.log('接收到记录更新事件，开始刷新数据')
				this.refreshRecords()
			},
			
			// 初始化容器高度
			initContainerHeight() {
				uni.getSystemInfo({
					success: (res) => {
						this.containerHeight = res.windowHeight
					}
				})
			},

			
      async loadRecords() {
				if (this.pageNum === 1) {
					this.loading = true
				} else {
					this.loadingMore = true
				}
			
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
				
				// 根据是否是延迟整改或月检记录选择不同的API
				// 管理员用户不需要部门过滤，查询所有数据
        let apiUrl = ''
        if (this.isMonthly && this.isDelayed) {
          const dl = (this.deadlineBefore != null && this.deadlineBefore != '') ? this.deadlineBefore : this.formatToday()
          apiUrl = getApiUrl(`/monthly-inspection-record/page?current=${this.pageNum}&size=${this.pageSize}&recordStatus=0&rectificationDeadlineBefore=${dl}${(!isAdmin && departmentId != null && departmentId != '') ? '&responsibilityId=' + departmentId : ''}`)
        } else if (this.isDelayed) {
          apiUrl = getApiUrl(`/inspection-record/delayed?current=${this.pageNum}&size=${this.pageSize}${(!isAdmin && departmentId != null && departmentId != '') ? '&departmentId=' + departmentId : ''}`)
        } else if (this.isMonthly) {
          apiUrl = getApiUrl(`/monthly-inspection-record/page?current=${this.pageNum}&size=${this.pageSize}&recordStatus=${this.status}${(!isAdmin && departmentId != null && departmentId != '') ? '&responsibilityId=' + departmentId : ''}`)
        } else {
          apiUrl = getApiUrl(`/inspection-record/page?current=${this.pageNum}&size=${this.pageSize}&status=${this.status}${(!isAdmin && departmentId != null && departmentId != '') ? '&departmentId=' + departmentId : ''}`)
        }
				
				uni.request({
					url: apiUrl,
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						const responseData = res.data as UTSJSONObject
						if (res.statusCode === 200 && responseData.getNumber('code') === 200) {
							const dataObj = responseData.getJSON('data') as UTSJSONObject
							const records = dataObj?.getArray('records') as Array<UTSJSONObject>
							const newRecords: InspectionRecord[] = []
							
							if (records != null) {
								for (let i = 0; i < records.length; i++) {
									const item = records[i]
									// 打印原始数据对象，检查字段内容
									console.log('原始数据对象:', item)
									console.log('problemDescription字段:', item.getString('problemDescription'))
									
                  const createTime = item.getString('createTime') ?? ''
                  const rd0 = item.getString('rectificationDate') ?? ''
                  const rd1 = rd0 != '' ? rd0 : (item.getString('rectification_date') ?? '')
                  const rd2 = rd1 != '' ? rd1 : (item.getString('rectificationDeadline') ?? '')
                  const rd3 = rd2 != '' ? rd2 : (item.getString('rectificationTime') ?? '')
                  const rd4 = rd3 != '' ? rd3 : (item.getString('deadline') ?? '')
                  const rd5 = rd4 != '' ? rd4 : (item.getString('rectifyDeadline') ?? '')
                  const record: InspectionRecord = {
                    id: (item.getString('id') ?? item.getString('recordId') ?? ''),
                    problemDescription: item.getString('problemDescription') ?? '',
                    responsiblePerson: item.getString('responsiblePerson') ?? '',
                    inspectorName: item.getString('inspector') ?? '',
                    createDate: this.formatDate(createTime),
                    status: this.isMonthly ? (item.getNumber('recordStatus') ?? 0) : (item.getNumber('status') ?? 0),
                    rectificationDate: this.normalizeDate(rd5)
                  }
									// 打印处理后的记录对象
									console.log('处理后的记录对象:', record)
									newRecords.push(record)
								}
							}
							
                          // 统一过滤
                          let filtered = newRecords
                          // 月检待整改列表排除延迟整改（整改期限小于今天）
                          if (this.isMonthly && !this.isDelayed && this.status === '0') {
                            const start = (this.deadlineFrom != null && this.deadlineFrom != '') ? this.deadlineFrom : this.formatToday()
                            const todayInt = this.dateToInt(start)
                            filtered = newRecords.filter(r => {
                              const d = (r.rectificationDate == null) ? '' : r.rectificationDate
                              if (d === '') return false
                              const di = this.dateToInt(d)
                              if (di < 0) return false
                              return di >= todayInt
                            })
                          }
                          // 月检延迟整改：仅保留“待整改且延期>0”的记录（整改期限 < 今天）
                          if (this.isMonthly && this.isDelayed) {
                            const today = this.formatToday()
                            const todayInt = this.dateToInt(today)
                            filtered = newRecords.filter(r => {
                              const d = (r.rectificationDate == null) ? '' : r.rectificationDate
                              if (d === '') return false
                              const di = this.dateToInt(d)
                              if (di < 0) return false
                              return (r.status === 0) && (di < todayInt)
                            })
                          }
                            if (this.pageNum === 1) {
                                this.recordList = filtered
                            } else {
                                this.recordList = this.recordList.concat(filtered)
                            }

                          // 统一列表计数与月检页：月检待整改需排除延迟整改数量
                          const serverTotal = dataObj?.getNumber('total') ?? newRecords.length
                          if (this.isMonthly && !this.isDelayed && this.status === '0' && this.pageNum === 1) {
                            this.totalCount = filtered.length
                          } else {
                            this.totalCount = filtered.length
                          }
                          this.hasMore = filtered.length >= this.pageSize
				} else {
					const errorMessage = responseData.getString('message') ?? ''
					uni.showToast({
						title: errorMessage != '' ? errorMessage : '加载失败',
						icon: 'none'
					})
				}
					this.loading = false
					this.loadingMore = false
				},
				fail: (error) => {
					console.error('加载记录失败:', error)
					uni.showToast({
						title: '网络错误',
						icon: 'none'
					})
					this.loading = false
					this.loadingMore = false
				}
		})
	},
			
		getStatusText(status: number | null): string {
			if (status == null) return '未知'
			const statusMap: UTSJSONObject = {
				"0": '待整改',
				"1": '已整改',
				"2": '已复查'
			}
			const statusText = statusMap[status.toString()] as string
			return statusText != null ? statusText : '未知'
		},
		
      formatDate(dateTimeStr: string): string {
			if (dateTimeStr == null || dateTimeStr == '') return ''
			// 如果是完整的日期时间格式，只取日期部分
			if (dateTimeStr.includes(' ')) {
				return dateTimeStr.split(' ')[0]
			}
        return dateTimeStr
      },

      formatToday(): string {
        const now = new Date()
        const y = now.getFullYear()
        const m = now.getMonth() + 1
        const d = now.getDate()
        const mm = m < 10 ? ('0' + m) : ('' + m)
        const dd = d < 10 ? ('0' + d) : ('' + d)
        return `${y}-${mm}-${dd}`
      },
      normalizeDate(s: string): string {
        if (s == null || s === '') return ''
        // 支持 "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-ddTHH:mm:ss"
        const t = s.replace('T', ' ')
        const core = t.length >= 10 ? t.substring(0, 10) : t
        const parts = core.split('-')
        if (parts.length < 3) return ''
        const y = parts[0]
        const m = parts[1].length === 1 ? ('0' + parts[1]) : parts[1]
        const d = parts[2].length === 1 ? ('0' + parts[2]) : parts[2]
        return `${y}-${m}-${d}`
      },
      dateToInt(s: string): number {
        if (s == null || s === '') return -1
        const parts = s.split('-')
        if (parts.length < 3) return -1
        const y = parseInt(parts[0])
        const m = parseInt(parts[1])
        const d = parseInt(parts[2])
        if (isNaN(y) || isNaN(m) || isNaN(d)) return -1
        return (y * 10000) + (m * 100) + d
      },
			
		getStatusClass(status: number | null): string {
				if (status == null) return 'status-unknown'
				const classMap: UTSJSONObject = {
					"0": 'status-pending',
					"1": 'status-completed',
					"2": 'status-accepted'
				}
				const statusClass = classMap[status.toString()] as string
				return statusClass != null ? statusClass : 'status-unknown'
			},
			
      getStatusTitle(): string {
        if (this.isDelayed) return '延迟整改'
        if (this.isMonthly) {
          if (this.status == '0') return '月检待整改'
          if (this.status == '1') return '月检已整改'
          if (this.status == '2') return '月检已复查'
          return '月检'
        }
        if (this.status == '0') return '待整改'
        if (this.status == '1') return '已整改'
        if (this.status == '2') return '已复查'
        return ''
      },
			
      goToDetail(record: InspectionRecord) {
        // 根据页面模式和记录状态进行不同的跳转
        if (this.isMonthly && this.isDelayed) {
          // 月检延迟整改：使用月检整改页，按月检记录ID加载
          uni.navigateTo({ url: `/pages/monthly/rectify?id=${record.id}` })
          return
        }
        if (this.isDelayed) {
          // 周检/通用延迟整改：跳问题详情整改页
          uni.navigateTo({ url: `/pages/record/detail?id=${record.id}&delayed=true` })
        } else if (this.isReviewed) {
          // 已复查记录模式：与延迟整改使用相同逻辑跳转到review.uvue页面
          uni.navigateTo({
            url: `/pages/record/review?id=${record.id}&reviewed=true`
          })
        } else if (this.isMonthly) {
          // 月检：根据状态跳转
          if (record.status === 0) {
            uni.navigateTo({ url: `/pages/monthly/rectify?id=${record.id}` })
          } else if (record.status === 1) {
            uni.navigateTo({ url: `/pages/monthly/review?id=${record.id}` })
          } else {
            // 已复查或其他状态跳转到只读查看页
            uni.navigateTo({ url: `/pages/monthly/view?id=${record.id}` })
          }
        } else if (record.status === 0) {
          // 非月检：待整改跳详情
          uni.navigateTo({ url: `/pages/record/detail?id=${record.id}` })
        } else if (record.status === 1) {
          // 非月检：已整改跳复查
          uni.navigateTo({ url: `/pages/record/review?id=${record.id}` })
        } else {
          // 其他状态：跳转到review.uvue页面（只读模式）
          uni.navigateTo({
            url: `/pages/record/review?id=${record.id}`
          })
        }
      },
			
			refreshRecords() {
				this.pageNum = 1
				this.recordList = []
				this.hasMore = true
				this.loadRecords()
				uni.stopPullDownRefresh()
			},
			
			// 下拉刷新
			onRefresh() {
			this.isRefreshing = true
			this.refreshRecords()
			
			// 设置定时器，3秒后自动关闭刷新状态
			setTimeout(() => {
				if (this.isRefreshing) {
					this.isRefreshing = false
					uni.stopPullDownRefresh()
				}
			}, 1500)
		},
			
			// 下拉刷新恢复
			onRefreshRestore() {
				this.isRefreshing = false
			},
			
			loadMore() {
				if (this.loadingMore || !this.hasMore) {
					return
				}
				this.pageNum++
				this.loadRecords()
			},
			
			handleScrollToLower() {
				if (this.hasMore && !this.loadingMore) {
					this.loadMore()
				}
			},
			
			showDeleteConfirm(recordId: string, problemDescription: string) {
				uni.showModal({
					title: '确认删除',
					content: `确定要删除问题"${problemDescription}"吗？此操作不可恢复。`,
					confirmText: '删除',
					cancelText: '取消',
					confirmColor: '#ff4757',
					success: (res) => {
						if (res.confirm) {
							this.deleteRecord(recordId)
						}
					}
				})
			},
			
			deleteRecord(recordId: string) {
				const token = uni.getStorageSync('token')
				
				uni.showLoading({
					title: '删除中...'
				})
				
				uni.request({
					url: getApiUrl(`/inspection-record/${recordId}`),
					method: 'DELETE',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						uni.hideLoading()
						const responseData = res.data as UTSJSONObject
						if (res.statusCode === 200 && responseData.getNumber('code') === 200) {
							// 删除成功，从本地列表中移除该记录
							this.recordList = this.recordList.filter(record => record.id !== recordId)
							this.totalCount = Math.max(0, this.totalCount - 1)
							
							uni.showToast({
								title: '删除成功',
								icon: 'success'
							})
						} else {
							const errorMessage = responseData.getString('message') ?? '删除失败'
							uni.showToast({
								title: errorMessage,
								icon: 'none'
							})
						}
					},
					fail: (error) => {
						uni.hideLoading()
						console.error('删除记录失败:', error)
						uni.showToast({
							title: '网络错误',
							icon: 'none'
						})
					}
				})
			}
		}
	})

export default __sfc__
function GenPagesRecordRecordRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
  return _cE("scroll-view", _uM({
    class: "container",
    "scroll-y": "true",
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
      _cE("text", _uM({ class: "title" }), "问题列表"),
      _cE("text", _uM({ class: "subtitle" }), "共 " + _tD(_ctx.totalCount) + " 条" + _tD(_ctx.getStatusTitle()) + "问题", 1 /* TEXT */)
    ]),
    _cE(Fragment, null, RenderHelpers.renderList(_ctx.recordList, (record, __key, __index, _cached): any => {
      return _cE("view", _uM({
        class: "record-item",
        key: record.id,
        onClick: () => {_ctx.goToDetail(record)},
        onLongpress: () => {_ctx.showDeleteConfirm(record.id, record.problemDescription)}
      }), [
        _cE("view", _uM({ class: "record-content" }), [
          _cE("view", _uM({ class: "left-content" }), [
            _cE("view", _uM({ class: "name-problem-row" }), [
              _cE("text", _uM({ class: "responsible-name" }), _tD(record.responsiblePerson), 1 /* TEXT */)
            ])
          ]),
          _cE("view", _uM({ class: "right-info" }), [
            _cE("text", _uM({ class: "inspector-info" }), "检查人：" + _tD(record.inspectorName), 1 /* TEXT */),
            _cE("text", _uM({ class: "date-text" }), _tD(record.createDate), 1 /* TEXT */),
            _cE("view", _uM({
              class: _nC(["status-tag", _ctx.getStatusClass(record.status)])
            }), [
              _cE("text", _uM({ class: "status-text" }), _tD(_ctx.getStatusText(record.status)), 1 /* TEXT */)
            ], 2 /* CLASS */)
          ]),
          _cE("text", _uM({ class: "problem-text" }), _tD(record.problemDescription), 1 /* TEXT */)
        ])
      ], 40 /* PROPS, NEED_HYDRATION */, ["onClick", "onLongpress"])
    }), 128 /* KEYED_FRAGMENT */),
    isTrue(_ctx.recordList.length === 0 && !_ctx.loading)
      ? _cE("view", _uM({
          key: 0,
          class: "empty-state"
        }), [
          _cE("text", _uM({ class: "empty-icon" }), "📝"),
          _cE("text", _uM({ class: "empty-text" }), "暂无记录")
        ])
      : _cC("v-if", true),
    isTrue(_ctx.loading)
      ? _cE("view", _uM({
          key: 1,
          class: "loading-state"
        }), [
          _cE("text", _uM({ class: "loading-text" }), "加载中...")
        ])
      : _cC("v-if", true),
    isTrue(_ctx.hasMore && _ctx.recordList.length > 0)
      ? _cE("view", _uM({
          key: 2,
          class: "load-more",
          onClick: _ctx.loadMore
        }), [
          _cE("text", _uM({ class: "load-more-text" }), _tD(_ctx.loadingMore ? '加载中...' : '加载更多'), 1 /* TEXT */)
        ], 8 /* PROPS */, ["onClick"])
      : _cC("v-if", true)
  ], 44 /* STYLE, PROPS, NEED_HYDRATION */, ["refresher-triggered", "onRefresherrefresh", "onRefresherrestore"])
}
const GenPagesRecordRecordStyles = [_uM([["container", _pS(_uM([["backgroundColor", "#f5f5f5"], ["width", "100%"]]))], ["header", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#e0e0e0"]]))], ["title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#333333"], ["marginBottom", 5]]))], ["subtitle", _pS(_uM([["fontSize", 14], ["color", "#666666"]]))], ["record-item", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["marginTop", 10], ["marginRight", 10], ["marginBottom", 1], ["marginLeft", 10], ["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8]]))], ["record-content", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "flex-start"], ["marginBottom", 10], ["width", "100%"]]))], ["left-content", _pS(_uM([["flex", 1], ["marginRight", 15], ["display", "flex"], ["flexDirection", "column"], ["gap", "2px"]]))], ["problem-text", _pS(_uM([["fontSize", 15], ["color", "#666666"], ["lineHeight", 1.3], ["fontWeight", "400"], ["marginTop", 2]]))], ["right-info", _pS(_uM([["flexShrink", 0], ["textAlign", "right"], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "flex-end"], ["justifyContent", "flex-start"], ["minWidth", 120], ["maxWidth", 150], ["gap", "6px"], ["marginLeft", "auto"]]))], ["inspector-info", _pS(_uM([["fontSize", 13], ["color", "#333333"], ["textAlign", "right"]]))], ["date-text", _pS(_uM([["fontSize", 13], ["color", "#666666"]]))], ["status-row", _pS(_uM([["marginTop", 8]]))], ["status-tag", _pS(_uM([["paddingTop", 3], ["paddingRight", 10], ["paddingBottom", 3], ["paddingLeft", 10], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["fontSize", 12], ["alignSelf", "flex-end"]]))], ["status-text", _pS(_uM([["color", "#ffffff"], ["fontSize", 12]]))], ["status-pending", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ff6b6b"]]))], ["status-processing", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffa726"]]))], ["status-completed", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#66bb6a"]]))], ["status-accepted", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#42a5f5"]]))], ["name-problem-row", _pS(_uM([["display", "flex"], ["alignItems", "center"], ["marginBottom", 2]]))], ["responsible-name", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["fontWeight", "700"], ["marginTop", 0], ["marginRight", 0], ["marginBottom", 0], ["marginLeft", 0]]))], ["empty-state", _pS(_uM([["textAlign", "center"], ["paddingTop", 60], ["paddingRight", 20], ["paddingBottom", 60], ["paddingLeft", 20]]))], ["empty-icon", _pS(_uM([["fontSize", 48], ["marginBottom", 15], ["color", "#cccccc"]]))], ["empty-text", _pS(_uM([["fontSize", 16], ["color", "#999999"]]))], ["loading-state", _pS(_uM([["textAlign", "center"], ["paddingTop", 40], ["paddingRight", 20], ["paddingBottom", 40], ["paddingLeft", 20]]))], ["loading-text", _pS(_uM([["fontSize", 14], ["color", "#999999"]]))], ["load-more", _pS(_uM([["textAlign", "center"], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopWidth", 1], ["borderTopStyle", "solid"], ["borderTopColor", "#f0f0f0"]]))], ["load-more-text", _pS(_uM([["fontSize", 14], ["color", "#666666"]]))]])]
