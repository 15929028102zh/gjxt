
	import { getApiUrl } from '@/config/api.uts'
	import { isMonthlyAdmin, isWeeklyAdmin } from '@/utils/permission.uts'
	import { resolveCurrentDept, CurrentDept } from '@/utils/user.uts'
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
    rectifierName: string
    reviewerName: string
    natureName: string
    natureId: string
    professionName: string
    professionId: string
    departmentName: string
    departmentId: string
    // V3:月检关联任务ID(用于批量拉 task 详情计算阶段)
    taskId: string
    // V3:月检任务期阶段(1/2/3/0),由前端从 taskId 拉 task详情 后计算
    taskPhase: number
    taskPublicityEndTime: string
  }

  // V3:月检任务期阶段缓存(taskId -> phase)
  type TaskPhaseCache = Map<string, number>
  // V3:月检公示结束时间缓存(taskId -> ISO 字符串)
  type TaskPublicityEndCache = Map<string, string>
  // V3:去重集合(taskId)
  type SeenSet = Set<string>
	
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
        containerHeight: 0, // 容器高度
        // V3:当前用户部门类型 0=运行 1=管理;null=未加载
        currentUserDeptType: null as number | null,
        // V3:当前用户 deptId
        currentUserDeptId: '',
        // V3:月检任务期阶段缓存(taskId -> phase)
        taskPhaseCache: new Map<string, number>() as TaskPhaseCache,
        // V3:月检公示结束时间缓存(taskId -> ISO 字符串)
        taskPublicityEndCache: new Map<string, string>() as TaskPublicityEndCache,
        // V3:是否有任务已到 phase=4(公示期后),运行部门据此判断是否能看到自己的记录
        hasAnyTaskInPhase4: false,
        // V3:运行部门 + 公示前 = true,显示等待提示
        isWaitingInput: false
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
				// V3:loadRecords 内部会 await loadCurrentUserDept() 拿部门类型(用于按钮可见性与列表过滤)，
				// 这里不再重复调用，避免多打一轮 /user/info + /dept/{id}
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

			// V3:加载当前用户部门信息(deptType + deptId)。deptType 取部门真实角色，不再因管理员强制置 1；
			// admin 与 deptType 解耦(对齐 Web)：月检管理员在列表过滤处单独判断(看全部)，按钮可见性仍按真实 deptType
			// resolveCurrentDept 先刷新服务端 userInfo 再取部门类型，避免用登录时的旧 deptId 快照判定角色
			loadCurrentUserDept(): Promise<void> {
				return new Promise<void>((resolve) => {
					resolveCurrentDept().then((dept: CurrentDept) => {
						this.currentUserDeptId = dept.deptId
						this.currentUserDeptType = dept.deptType
						resolve()
					})
				})
			},

			// V3:批量拉月检任务的 taskNo+期阶段信息,缓存到 taskPhaseCache / taskPublicityEndCache
			prefetchMonthlyTaskPhases(): Promise<void> {
				return new Promise<void>((resolve) => {
					if (!this.isMonthly) { resolve(); return }
					const token = uni.getStorageSync('token')
					// 一次拉所有月检任务,缓存 publicity_end_time,前端算 phase
					uni.request({
						url: getApiUrl('/monthly-inspection-task/page?current=1&size=500'),
						method: 'GET',
						header: { 'Authorization': `Bearer ${token}` },
						success: (res) => {
							if (res.statusCode === 200) {
								const body = res.data as UTSJSONObject
								const data = body.getJSON('data')
								const records = data != null ? data.getArray('records') : null
								let hasPhase4 = false
								if (records != null) {
									for (let i = 0; i < records.length; i++) {
										const t = records[i] as UTSJSONObject
										const id = t.getString('id') ?? ''
										const pubEndStr = t.getString('publicityEndTime') ?? ''
										if (id != '' && pubEndStr != '') {
											const phase = this.computeTaskPhase(pubEndStr)
											this.taskPublicityEndCache.set(id, pubEndStr)
											this.taskPhaseCache.set(id, phase)
											if (phase === 4) hasPhase4 = true
										}
									}
								}
								this.hasAnyTaskInPhase4 = hasPhase4
							}
							resolve()
						},
						fail: () => { resolve() }
					})
				})
			},

			// V3:刷新当前 recordList 各项 taskPhase / taskPublicityEndTime(从缓存回填)
			refreshRecordsTaskPhase() {
				for (let i = 0; i < this.recordList.length; i++) {
					const r: InspectionRecord = this.recordList[i]
					const id = r.taskId
					if (id != '' && this.taskPhaseCache.has(id)) {
						r.taskPhase = this.taskPhaseCache.get(id) ?? 0
						r.taskPublicityEndTime = this.taskPublicityEndCache.get(id) ?? ''
					}
				}
			},

			// V3:运行部门 + 公示前判定(用于空列表提示)
			isAnyMonthlyTaskInPhase4(): boolean {
				// 直接读 prefetch 阶段已计算好的标志,避免 UTS Map 迭代的类型问题
				return this.hasAnyTaskInPhase4
			},

			// V3:根据 publicityEndTime 计算当前 phase (与 web 后端 MonthlyInspectionTask.currentPhase 一致)
			computeTaskPhase(publicityEndTimeStr: string): number {
				if (publicityEndTimeStr == '') return 0
				const parseLocalDate = (s: string): Date => new Date(s.replace(' ', 'T'))
				const pubEnd = parseLocalDate(publicityEndTimeStr)
				const now = new Date()
				if (now > pubEnd) return 4 // 公示期后
				// 简化判定:当前仅用 publicity_end_time 兜底,后续如果需要更精细可补 inspection_start_time/end_time
				return 3 // 公示期内
			},

			// V3:判断某条月检记录当前是否处于可整改/可复查期(公示结束后)
			isActionableForCurrentUser(record: InspectionRecord): boolean {
				if (!this.isMonthly) return true
				// 管理部门(1)始终可见
				if (this.currentUserDeptType === 1) return true
				// 运行部门(0):仅公示期结束后(record.taskPhase 已结束公示,或now > taskPublicityEndTime)
				if (this.currentUserDeptType === 0) {
					if (record.taskPhase === 0) return false
					return record.taskPhase === 4  // 仅 phase 4(公示期后)允许运行部门操作
				}
				return false
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

				// V3:先拉用户 deptType;若是月检,还需先拉所有任务的 phase 缓存
				if (this.currentUserDeptType === null) {
					await this.loadCurrentUserDept()
				}
				if (this.isMonthly && this.taskPhaseCache.size === 0) {
					await this.prefetchMonthlyTaskPhases()
				}

				const token = uni.getStorageSync('token')
				
				// 获取用户信息：部门 ID + 周检管理员标志
				// 周检/巡检域用 isWeeklyAdmin()(对齐 Web permission.js:老 isAdmin=1 或 isWeeklyAdmin=1)
				let departmentId = ''
				const isWeeklyAdm = isWeeklyAdmin()
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
				
				// V3:月检 deptType 双过滤
				//   管理部门(1) → 后端 /monthly-inspection-record/page?checkUnitId=...
				//   运行部门(0) + 公示期前 → 不调 API,只显示空列表 + 提示
				//   运行部门(0) + 公示期后 → 后端 ?departmentId=... 自己作为受检单位
        let apiUrl = ''
        if (this.isMonthly && this.isDelayed) {
          const dl = (this.deadlineBefore != null && this.deadlineBefore != '') ? this.deadlineBefore : this.formatToday()
          apiUrl = getApiUrl(`/monthly-inspection-record/page?current=${this.pageNum}&size=${this.pageSize}&recordStatus=0&rectificationDeadlineBefore=${dl}`)
        } else if (this.isDelayed) {
          apiUrl = getApiUrl(`/inspection-record/delayed?current=${this.pageNum}&size=${this.pageSize}${(!isWeeklyAdm && departmentId != null && departmentId != '') ? '&departmentId=' + departmentId : ''}`)
        } else if (this.isMonthly) {
          this.isWaitingInput = false
          if (isMonthlyAdmin()) {
            // 月检管理员看全部,不加部门过滤(对齐 Web:admin 不 applyDeptFilter)
            apiUrl = getApiUrl(`/monthly-inspection-record/page?current=${this.pageNum}&size=${this.pageSize}&recordStatus=${this.status}`)
          } else if (this.currentUserDeptType === 1 && this.currentUserDeptId != '') {
            // 管理部门:按 check_unit_id 过滤
            apiUrl = getApiUrl(`/monthly-inspection-record/page?current=${this.pageNum}&size=${this.pageSize}&recordStatus=${this.status}&checkUnitId=${this.currentUserDeptId}`)
          } else if (this.currentUserDeptType === 0) {
            if (!this.isAnyMonthlyTaskInPhase4()) {
              // 公示期前:不调 API,空列表 + 提示
              this.recordList = []
              this.totalCount = 0
              this.hasMore = false
              this.isWaitingInput = true
              this.loading = false
              this.loadingMore = false
              return
            }
            // 公示期后:按 department_id 过滤
            apiUrl = getApiUrl(`/monthly-inspection-record/page?current=${this.pageNum}&size=${this.pageSize}&recordStatus=${this.status}&departmentId=${this.currentUserDeptId}`)
            this.isWaitingInput = false
          } else {
            // 未识别出 deptType,降级按全部显示
            apiUrl = getApiUrl(`/monthly-inspection-record/page?current=${this.pageNum}&size=${this.pageSize}&recordStatus=${this.status}`)
          }
        } else {
          apiUrl = getApiUrl(`/inspection-record/page?current=${this.pageNum}&size=${this.pageSize}&status=${this.status}${(!isWeeklyAdm && departmentId != null && departmentId != '') ? '&departmentId=' + departmentId : ''}`)
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
                    rectificationDate: this.normalizeDate(rd5),
                    rectifierName: item.getString('rectifierName') ?? '',
                    reviewerName: item.getString('reviewerName') ?? '',
                    natureName: item.getString('natureName') ?? '',
                    natureId: '',
                    professionName: item.getString('professionName') ?? '',
                    professionId: item.getString('professionId') ?? '',
                    departmentName: item.getString('departmentName') ?? '',
                    departmentId: item.getString('departmentId') ?? '',
                    // V3:月检关联任务ID(用于批量拉 task 详情计算阶段)
                    taskId: item.getString('taskId') ?? '',
                    // V3:月检任务期阶段公示结束时间,前端从 taskId 拉取
                    taskPhase: this.taskPhaseCache.get(item.getString('taskId') ?? '') ?? 0,
                    taskPublicityEndTime: this.taskPublicityEndCache.get(item.getString('taskId') ?? '') ?? ''
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
					// V3:月检记录 → 阶段从缓存回填
					if (this.isMonthly) {
						this.refreshRecordsTaskPhase()
					}
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

			// V3:长按弹操作菜单;菜单选项按 deptType + phase + status 动态生成
			buildActionItems(record: InspectionRecord): string[] {
				const items: string[] = []
				const isEditablePhase = record.taskPhase === 1 || record.taskPhase === 2 || record.taskPhase === 3
				// 整改:公示期后(phase=4) + 待整改(status=0) + 非管理部门(deptType!==1)
				// 对齐 Web showRectificationButton(userDeptType!==1):整改由运行部门/管理员执行,管理部门不可见
				if (this.isMonthly && record.taskPhase === 4 && record.status === 0 && this.currentUserDeptType !== 1) {
					items.push('整改')
				}
				// 复查:公示期后(phase=4) + 待复查(status=1) + 非运行部门(deptType!==0)
				// 对齐 Web showReviewButton(userDeptType!==0):复查由管理部门/管理员执行,运行部门不可见
				if (this.isMonthly && record.taskPhase === 4 && record.status === 1 && this.currentUserDeptType !== 0) {
					items.push('复查')
				}
				// 编辑(申请):管理部门 + 公示期前/中(phase=1/2/3)
				// 移动端不允许编辑月检记录:编辑统一在 Web 端月检页面进行。
				// 保留原判定逻辑,仅用开关隐藏入口;如需恢复移动端编辑,将 MOBILE_EDIT_ENABLED 改为 true。
				const MOBILE_EDIT_ENABLED: boolean = false
				if (MOBILE_EDIT_ENABLED && this.isMonthly && this.currentUserDeptType === 1 && isEditablePhase) {
					items.push('编辑')
				}
				// 查看:始终可见(非月检也兜底)
				items.push('查看')
				// 删除:仅 status=0(待整改/未开始)时允许;沿用 V2 删除规则
				if (record.status === 0 && !this.isReviewed) {
					items.push('删除')
				}
				return items
			},

			onLongPressRow(record: InspectionRecord) {
				const items = this.buildActionItems(record)
				if (items.length === 0) {
					uni.showToast({ title: '暂无可用操作', icon: 'none' })
					return
				}
				// 用临时变量持有 record(防止 callback 时 this.recordList 已变)
				const snapshot: InspectionRecord = record
				uni.showActionSheet({
					itemList: items,
					success: (res) => {
						const idx = res.tapIndex
						const choice = items[idx] ?? ''
						if (choice === '整改') this.handleRectifyAction(snapshot)
						else if (choice === '复查') this.handleReviewAction(snapshot)
						else if (choice === '编辑') this.handleEditAction(snapshot)
						else if (choice === '查看') this.handleViewAction(snapshot)
						else if (choice === '删除') this.showDeleteConfirm(snapshot.id, snapshot.problemDescription)
					},
					fail: () => {}
				})
			},

			handleRectifyAction(record: InspectionRecord) {
				if (this.isMonthly) {
					uni.navigateTo({ url: `/pages/monthly/rectify?id=${record.id}` })
				} else {
					uni.navigateTo({ url: `/pages/record/detail?id=${record.id}` })
				}
			},

			handleReviewAction(record: InspectionRecord) {
				if (this.isMonthly) {
					uni.navigateTo({ url: `/pages/monthly/review?id=${record.id}` })
				} else {
					uni.navigateTo({ url: `/pages/record/review?id=${record.id}&isMonthly=false` })
				}
			},

			handleEditAction(record: InspectionRecord) {
				// 复用 task-record.uvue 编辑模式(recordId 触发回填 + modification_snapshot 展示)
				if (this.isMonthly) {
					uni.navigateTo({ url: `/pages/monthly/task-record?recordId=${record.id}&mode=edit` })
				} else {
					uni.showToast({ title: '周检尚未支持编辑模式', icon: 'none' })
				}
			},

			handleViewAction(record: InspectionRecord) {
				if (this.isMonthly) {
					uni.navigateTo({ url: `/pages/monthly/view?id=${record.id}` })
				} else {
					uni.navigateTo({ url: `/pages/record/review?id=${record.id}&isMonthly=false&reviewed=true` })
				}
			},

      goToDetail(record: InspectionRecord) {
        // V3:月检 + 运行部门 + 公示期未结束:不允许进入整改/复查
        if (this.isMonthly && !this.isDelayed && !this.isReviewed
            && this.currentUserDeptType === 0 && record.taskPhase < 4 && record.taskPhase != 0
            && (record.status === 0 || record.status === 1)) {
          uni.showToast({ title: '公示期尚未结束，请稍后再操作', icon: 'none', duration: 2000 })
          return
        }
        // 构造回写参数字符串（性质界定/专业/所属部门 - 列表点击立即回显，跳过字典匹配）
        const n = record.natureName
        const p = record.professionName
        const d = record.departmentName
        const writeBackParams = `&natureName=${encodeURIComponent(n)}&professionName=${encodeURIComponent(p)}&departmentName=${encodeURIComponent(d)}`
        // 根据页面模式和记录状态进行不同的跳转
        if (this.isMonthly && this.isDelayed) {
          // 月检延迟整改：使用月检整改页，按月检记录 ID 加载
          uni.navigateTo({ url: `/pages/monthly/rectify?id=${record.id}` })
          return
        }
        if (this.isDelayed) {
          // 周检/通用延迟整改：跳问题详情整改页
          uni.navigateTo({ url: `/pages/record/detail?id=${record.id}&delayed=true${writeBackParams}` })
        } else if (this.isReviewed) {
          // 已复查记录模式：与延迟整改使用相同逻辑跳转到 review.uvue 页面
          uni.navigateTo({
            url: `/pages/record/review?id=${record.id}&reviewed=true&isMonthly=${this.isMonthly ? 'true' : 'false'}${writeBackParams}`
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
          uni.navigateTo({ url: `/pages/record/detail?id=${record.id}${writeBackParams}` })
        } else if (record.status === 1) {
          // 非月检：已整改跳复查
          uni.navigateTo({ url: `/pages/record/review?id=${record.id}&isMonthly=false${writeBackParams}` })
        } else {
          // 其他状态：跳转到 review.uvue 页面（只读模式）
          uni.navigateTo({
            url: `/pages/record/review?id=${record.id}&isMonthly=false${writeBackParams}`
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
        onLongpress: () => {_ctx.onLongPressRow(record)}
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
          _cE("text", _uM({ class: "problem-text" }), _tD(record.problemDescription), 1 /* TEXT */),
          isTrue(record.rectifierName !== '' || record.reviewerName !== '')
            ? _cE("view", _uM({
                key: 0,
                class: "operator-row"
              }), [
                record.rectifierName !== ''
                  ? _cE("text", _uM({
                      key: 0,
                      class: "operator-text"
                    }), "整改人：" + _tD(record.rectifierName), 1 /* TEXT */)
                  : _cC("v-if", true),
                record.reviewerName !== ''
                  ? _cE("text", _uM({
                      key: 1,
                      class: "operator-text"
                    }), "复查人：" + _tD(record.reviewerName), 1 /* TEXT */)
                  : _cC("v-if", true)
              ])
            : _cC("v-if", true)
        ])
      ], 40 /* PROPS, NEED_HYDRATION */, ["onClick", "onLongpress"])
    }), 128 /* KEYED_FRAGMENT */),
    isTrue(_ctx.recordList.length === 0 && !_ctx.loading)
      ? _cE("view", _uM({
          key: 0,
          class: "empty-state"
        }), [
          _cE("text", _uM({ class: "empty-icon" }), _tD(_ctx.isWaitingInput ? '⏳' : '📝'), 1 /* TEXT */),
          _cE("text", _uM({ class: "empty-text" }), _tD(_ctx.isWaitingInput ? '请等待检查单位录入（公示期尚未结束）' : '暂无记录'), 1 /* TEXT */)
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
const GenPagesRecordRecordStyles = [_uM([["container", _pS(_uM([["backgroundColor", "#f1f5f9"], ["width", "100%"]]))], ["header", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["backgroundImage", "linear-gradient(135deg, #1e40af 0%, #2563eb 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#dbeafe"], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"]]))], ["title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#ffffff"], ["marginBottom", 5]]))], ["subtitle", _pS(_uM([["fontSize", 14], ["color", "#ffffff"], ["fontWeight", 500], ["opacity", 0.92]]))], ["record-item", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["marginTop", 10], ["marginRight", 10], ["marginBottom", 1], ["marginLeft", 10], ["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f1f5f9"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"]]))], ["record-content", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "flex-start"], ["marginBottom", 10], ["width", "100%"]]))], ["left-content", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["marginRight", 15], ["display", "flex"], ["flexDirection", "column"]]))], ["name-problem-row", _uM([[".left-content ", _uM([["marginBottom", 2]])], ["", _uM([["display", "flex"], ["alignItems", "center"], ["marginBottom", 2]])]])], ["problem-text", _pS(_uM([["fontSize", 15], ["color", "#1e293b"], ["lineHeight", 1.3], ["fontWeight", 500], ["marginTop", 2]]))], ["operator-row", _pS(_uM([["display", "flex"], ["flexDirection", "row"], ["marginTop", 6]]))], ["operator-text", _pS(_uM([["fontSize", 13], ["color", "#64748b"], ["fontWeight", 500], ["marginRight", 16]]))], ["right-info", _pS(_uM([["flexShrink", 0], ["textAlign", "right"], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "flex-end"], ["justifyContent", "flex-start"], ["minWidth", 120], ["maxWidth", 150], ["marginLeft", "auto"]]))], ["inspector-info", _uM([[".right-info ", _uM([["marginBottom", 6]])], ["", _uM([["fontSize", 13], ["color", "#0f172a"], ["fontWeight", 500], ["textAlign", "right"]])]])], ["date-text", _uM([[".right-info ", _uM([["marginBottom", 6]])], ["", _uM([["fontSize", 13], ["color", "#475569"], ["fontWeight", 500]])]])], ["status-tag", _uM([[".right-info ", _uM([["marginBottom", 0]])], ["", _uM([["paddingTop", 3], ["paddingRight", 10], ["paddingBottom", 3], ["paddingLeft", 10], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["fontSize", 12], ["alignSelf", "flex-end"]])]])], ["status-row", _pS(_uM([["marginTop", 8]]))], ["status-text", _pS(_uM([["color", "#ffffff"], ["fontSize", 12], ["fontWeight", 500]]))], ["status-pending", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#f59e0b"]]))], ["status-processing", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#2563eb"]]))], ["status-completed", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#16a34a"]]))], ["status-accepted", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#1e40af"]]))], ["status-unknown", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#94a3b8"]]))], ["responsible-name", _pS(_uM([["fontSize", 16], ["color", "#0f172a"], ["fontWeight", 700], ["marginTop", 0], ["marginRight", 0], ["marginBottom", 0], ["marginLeft", 0]]))], ["empty-state", _pS(_uM([["textAlign", "center"], ["paddingTop", 60], ["paddingRight", 20], ["paddingBottom", 60], ["paddingLeft", 20]]))], ["empty-icon", _pS(_uM([["fontSize", 48], ["marginBottom", 15], ["color", "#94a3b8"]]))], ["empty-text", _pS(_uM([["fontSize", 16], ["color", "#94a3b8"]]))], ["loading-state", _pS(_uM([["textAlign", "center"], ["paddingTop", 40], ["paddingRight", 20], ["paddingBottom", 40], ["paddingLeft", 20]]))], ["loading-text", _pS(_uM([["fontSize", 14], ["color", "#94a3b8"]]))], ["load-more", _pS(_uM([["textAlign", "center"], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopWidth", 1], ["borderTopStyle", "solid"], ["borderTopColor", "#f1f5f9"]]))], ["load-more-text", _pS(_uM([["fontSize", 14], ["color", "#1e293b"], ["fontWeight", 500]]))]])]
