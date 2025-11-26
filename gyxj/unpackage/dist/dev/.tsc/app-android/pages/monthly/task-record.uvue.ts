
	import { getApiUrl, apiConfig } from '@/config/api.uts'
	
type TaskInfo = { __$originalPosition?: UTSSourceMapPosition<"TaskInfo", "pages/monthly/task-record.uvue", 376, 6>;
    taskId: string
    taskNumber: string
    inspectionDate: string
    department: string
    profession: string
    groupLeader: string
    deputyLeader: string
}
	
  type FormData = { __$originalPosition?: UTSSourceMapPosition<"FormData", "pages/monthly/task-record.uvue", 386, 8>;
		problemType: string
		problemDescription: string
		location: string
		photos: string[]
		safetyScore: string
		rectificationDate: string
		module: string
		responsibility: string
		issue: string
    quality: string
    inspector: string
    responsibleUnit: string
    responsiblePerson: string
  }
	
	type OptionItem = { __$originalPosition?: UTSSourceMapPosition<"OptionItem", "pages/monthly/task-record.uvue", 402, 7>;
		id: string
		name: string
		value?: string
	}
	
	const __sfc__ = defineComponent({
		data(): {
			taskInfo: TaskInfo
			formData: FormData
			containerHeight: number
			scrollTop: number
			showProblemTypeModal: boolean
			selectedProblemType: string
			problemTypes: any[]
			// 级联选择器相关
			showTypePopup: boolean
			showModulePopup: boolean
			showResponsibilityPopup: boolean
			showIssuePopup: boolean
			showQualityPopup: boolean
			typeOptions: OptionItem[]
			moduleOptions: OptionItem[]
			responsibilityOptions: OptionItem[]
			issueOptions: OptionItem[]
			qualityOptions: OptionItem[]
			selectedTypeIndex: number
			selectedModuleIndex: number
			selectedResponsibilityIndex: number
			selectedIssueIndex: number
			selectedQualityIndex: number
			// 搜索关键字
			typeSearchKeyword: string
			moduleSearchKeyword: string
			responsibilitySearchKeyword: string
			issueSearchKeyword: string
			qualitySearchKeyword: string
			// 日期选择器相关
			datePickerVisible: boolean
			pickerValue: number[]
			years: number[]
			months: number[]
			days: number[]
			selectedYear: number
			selectedMonth: number
			selectedDay: number
			pickerDebounceTimer: number | null
		} {
			// 计算默认整改时间（当前日期往后推7天）
			const defaultDate = new Date()
			defaultDate.setDate(defaultDate.getDate() + 7)
			const defaultDateStr = `${defaultDate.getFullYear()}-${(defaultDate.getMonth() + 1).toString().padStart(2, '0')}-${defaultDate.getDate().toString().padStart(2, '0')}`
			
        return {
            taskInfo: {
                taskId: '',
                taskNumber: '',
                inspectionDate: '',
                department: '',
                profession: '',
                groupLeader: '',
                deputyLeader: ''
            } as TaskInfo,
          formData: {
			problemType: '',
			problemDescription: '',
			location: '',
			photos: [],
			safetyScore: '',
			rectificationDate: defaultDateStr,
			module: '',
			responsibility: '',
			issue: '',
            quality: '',
            inspector: ''
            ,responsibleUnit: '',
            responsiblePerson: ''
          } as FormData,
				containerHeight: 0,
				scrollTop: 0,
				showProblemTypeModal: false,
			selectedProblemType: '',
				problemTypes: [
					'安全隐患',
					'设备故障',
					'环境问题',
					'操作违规',
					'制度缺失',
					'其他问题'
			],
				// 级联选择器相关
				showTypePopup: false,
				showModulePopup: false,
				showResponsibilityPopup: false,
				showIssuePopup: false,
				showQualityPopup: false,
				typeOptions: [] as OptionItem[],
				moduleOptions: [] as OptionItem[],
				responsibilityOptions: [] as OptionItem[],
				issueOptions: [] as OptionItem[],
				qualityOptions: [] as OptionItem[],
				selectedTypeIndex: -1,
				selectedModuleIndex: -1,
				selectedResponsibilityIndex: -1,
				selectedIssueIndex: -1,
				selectedQualityIndex: -1,
				// 搜索关键字初始值
				typeSearchKeyword: '',
				moduleSearchKeyword: '',
				responsibilitySearchKeyword: '',
				issueSearchKeyword: '',
				qualitySearchKeyword: '',
				// 日期选择器相关
				datePickerVisible: false,
				pickerValue: [0, 0, 0],
				years: [] as number[],
				months: [] as number[],
				days: [] as number[],
				selectedYear: (() => {
					const tempDate = new Date()
					tempDate.setDate(tempDate.getDate() + 7)
					return tempDate.getFullYear()
				})() as number,
				selectedMonth: (() => {
					const tempDate = new Date()
					tempDate.setDate(tempDate.getDate() + 7)
					return tempDate.getMonth() + 1
				})() as number,
				selectedDay: (() => {
					const tempDate = new Date()
					tempDate.setDate(tempDate.getDate() + 7)
					return tempDate.getDate()
				})() as number,
				// 防抖定时器
				pickerDebounceTimer: null as number | null
			}
		},
		

	

	// 预览照片
	previewPhoto(photo: string) {
		uni.previewImage({
			current: this.convertToHttpUrl(photo),
			urls: this.formData.photos.map((p: string) => this.convertToHttpUrl(p)) as string[]
		})
	},

	onLoad(options: UTSJSONObject) {
			const taskId = options.getString('taskId') ?? ''
			console.log(taskId, " at pages/monthly/task-record.uvue:553")
			
			if (taskId != null && taskId != '') {
				this.loadTaskInfo(taskId)
			}
			// 初始化级联选择器数据
			this.loadTypeOptions()
			// 初始化日期选择器数据
			this.initDateArrays()
		},
		onReady() {
			this.initContainerHeight()
		},
		computed: {
			// 过滤后的专业类别选项
			filteredTypeOptions(): OptionItem[] {
     if (this.typeSearchKeyword == '') {
					return this.typeOptions
				}
				return this.typeOptions.filter(item => 
					item.name.toLowerCase().includes(this.typeSearchKeyword.toLowerCase())
				)
			},
			// 过滤后的模块选项
     filteredModuleOptions(): OptionItem[] {
      if (this.moduleSearchKeyword == '') {
       return this.moduleOptions
      }
				return this.moduleOptions.filter(item => 
					item.name.toLowerCase().includes(this.moduleSearchKeyword.toLowerCase())
				)
			},
			// 过滤后的责任选项
     filteredResponsibilityOptions(): OptionItem[] {
      if (this.responsibilitySearchKeyword == '') {
       return this.responsibilityOptions
      }
				return this.responsibilityOptions.filter(item => 
					item.name.toLowerCase().includes(this.responsibilitySearchKeyword.toLowerCase())
				)
			},
			// 过滤后的问题选项
     filteredIssueOptions(): OptionItem[] {
      if (this.issueSearchKeyword == '') {
       return this.issueOptions
      }
				return this.issueOptions.filter(item => 
					item.name.toLowerCase().includes(this.issueSearchKeyword.toLowerCase())
				)
			},
			// 过滤后的性质界定选项
     filteredQualityOptions(): OptionItem[] {
      if (this.qualitySearchKeyword == '') {
       return this.qualityOptions
      }
				return this.qualityOptions.filter(item => 
					item.name.toLowerCase().includes(this.qualitySearchKeyword.toLowerCase())
				)
			}
		},
		methods: {
			// 获取默认整改期限（当前日期往后推三个月）
			getDefaultRectificationDate(): string {
				const now = new Date()
				const year = now.getFullYear()
				const month = (now.getMonth() + 1).toString()
				const day = now.getDate().toString()
				// 手动补零
				const padZero = (num: string): string => {
					return num.length < 2 ? `0${num}` : num
				}
				return `${year}-${padZero(month)}-${padZero(day)}`
			},
			// 将相对路径转换为HTTP访问路径
			convertToHttpUrl(relativePath: string): string {
				console.log('相对路径:', relativePath, " at pages/monthly/task-record.uvue:628")
				// 如果已经是HTTP路径，直接返回
				if (relativePath.startsWith('http://') || relativePath.startsWith('https://')) {
					return relativePath
				}
				
				// 直接拼接完整的HTTP URL
				// 后端已返回相对路径，如: /files/images/20250818/xxx.jpg
				const httpUrl = `${apiConfig.baseUrl}/api${relativePath}`
				console.log('转换后的HTTP URL:', httpUrl, " at pages/monthly/task-record.uvue:637")
				return httpUrl
			},
			// 初始化容器高度
			initContainerHeight() {
				const systemInfo = uni.getSystemInfoSync()
				this.containerHeight = systemInfo.windowHeight
			},
			
			// 加载任务信息
			loadTaskInfo(taskId: string) {
				try {
					const token = uni.getStorageSync('token')
					console.log('接收到的taskId参数:', taskId, " at pages/monthly/task-record.uvue:650")
					// 使用task-no接口根据任务编号获取任务详情
					uni.request({
						url: getApiUrl(`/monthly-inspection-task/task-no/${taskId}`),
						method: 'GET',
						header: {
							'Authorization': `Bearer ${token}`
						},
						success: (res) => {
							const respdata=res.data as UTSJSONObject;
                            if ( respdata.getNumber('code')=== 200) {
                                const taskInfo = respdata.getJSON('data')
                                console.log("获取到的任务数据:",taskInfo, " at pages/monthly/task-record.uvue:662")
                                if (taskInfo != null) {
                                    this.taskInfo = {
                                        taskId: taskInfo.getString('id') ?? '',
                                        taskNumber: taskInfo.getString('taskNo') ?? '',
                                        inspectionDate: taskInfo.getString('inspectionTime') ?? '',
                                        department: taskInfo.getString('departmentName') ?? '',
                                        profession: taskInfo.getString('professionId') ?? '',
                                        groupLeader: taskInfo.getString('groupLeaderName') ?? '',
                                        deputyLeader: taskInfo.getString('deputyLeaderName') ?? ''
                                    }
									// 自动填充性质界定
									this.autoFillProfession()
								}
							}
						},
						fail: (error) => {
							console.error('加载任务信息失败:', error, " at pages/monthly/task-record.uvue:679")
							// 模拟数据
                        this.taskInfo = {
                            taskId: '',
                            taskNumber: 'YJ202401001',
                            inspectionDate: '2024年1月',
                            department: '生产部',
                            profession: '违法违规',
                            groupLeader: '张三',
                            deputyLeader: '李四'
                        }
							// 自动填充性质界定
						this.autoFillProfession()
						}
					})
				} catch (error) {
					console.error('加载任务信息失败:', error, " at pages/monthly/task-record.uvue:695")
					// 模拟数据
                    this.taskInfo = {
                        taskId: '',
                        taskNumber: 'YJ202401001',
                        inspectionDate: '2024年1月',
                        department: '生产部',
                        profession: '违法违规',
                        groupLeader: '张三',
                        deputyLeader: '李四'
                    }
					// 自动填充性质界定
					this.autoFillProfession()
				}
			},
			
			// 返回上一页
			goBack() {
				uni.navigateBack()
			},
			
			// 显示问题类型选择器
			showProblemTypePicker() {
				this.selectedProblemType = this.formData.problemType
				this.showProblemTypeModal = true
			},
			
			// 隐藏问题类型选择器
			hideProblemTypePicker() {
				this.showProblemTypeModal = false
			},
			
			// 选择问题类型
			selectProblemType(type: string) {
				this.selectedProblemType = type
			},
			
            // 自动填充性质界定
            autoFillProfession() {
				console.log('开始自动填充性质界定', " at pages/monthly/task-record.uvue:734")
				console.log('taskInfo:', this.taskInfo, " at pages/monthly/task-record.uvue:735")
				console.log('typeOptions:', this.typeOptions, " at pages/monthly/task-record.uvue:736")
				
				if (this.taskInfo != null && this.taskInfo.profession != '' && this.typeOptions.length > 0) {
					console.log('查找性质界定:', this.taskInfo.profession, " at pages/monthly/task-record.uvue:739")
					const index = this.typeOptions.findIndex((item: OptionItem) => item.name === this.taskInfo.profession)
					console.log('找到的性质界定索引:', index, " at pages/monthly/task-record.uvue:741")
					
					if (index !== -1) {
						this.selectedTypeIndex = index
						console.log('已设置性质界定索引:', this.selectedTypeIndex, " at pages/monthly/task-record.uvue:745")
                        // 自动加载模块选项（按专业名称过滤模块）
                        this.loadModuleOptions((this.typeOptions[index] as OptionItem).name)
					} else {
						console.warn('未找到匹配的性质界定:', this.taskInfo.profession, " at pages/monthly/task-record.uvue:749")
						// 尝试模糊匹配
						const fuzzyIndex = this.typeOptions.findIndex((item: OptionItem) => 
							item.name.includes(this.taskInfo.profession) || this.taskInfo.profession.includes(item.name)
						)
						if (fuzzyIndex !== -1) {
							this.selectedTypeIndex = fuzzyIndex
							console.log('模糊匹配到性质界定索引:', this.selectedTypeIndex, " at pages/monthly/task-record.uvue:756")
                            this.loadModuleOptions((this.typeOptions[fuzzyIndex] as OptionItem).name)
						}
					}
				} else {
					if (this.taskInfo == null) {
						console.log('taskInfo为空，等待任务信息加载', " at pages/monthly/task-record.uvue:762")
					} else if (this.taskInfo.profession == '') {
						console.log('专业信息为空', " at pages/monthly/task-record.uvue:764")
					} else if (this.typeOptions.length === 0) {
						console.log('性质界定选项未加载，等待加载完成', " at pages/monthly/task-record.uvue:766")
					}
				}
				
				// 自动加载对应性质界定的岗检标准项
				this.loadGjbzDataByProfession()
			},
			
			// 加载专业类别选项
			loadTypeOptions() {
				const token = uni.getStorageSync('token')
				uni.request({
					url: getApiUrl('/gjbz-data/options/types'),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						const respdata = res.data as UTSJSONObject
						if (respdata.getNumber('code') === 200) {
							const data = respdata.getArray('data')
							console.log("获取到的专业类别数据",data, " at pages/monthly/task-record.uvue:787")
							if (data != null) {
								const options: OptionItem[] = []
								for (let i = 0; i < data.length; i++) {
           const item = data[i] as string
           options.push({
            id: (i + 1).toString(),
            name: item
           } as OptionItem)
								}
								this.typeOptions = options
								console.log("专业类别选项已加载:", this.typeOptions, " at pages/monthly/task-record.uvue:798")
								// 自动填充专业类别
								this.autoFillProfession()
							}
						} else {
						console.error('加载专业类别失败:', respdata.getString('message'), " at pages/monthly/task-record.uvue:803")
						this.loadFallbackTypeOptions()
					}
				},
				fail: (error) => {
					console.error('加载专业类别失败:', error, " at pages/monthly/task-record.uvue:808")
					this.loadFallbackTypeOptions()
				}
			})
		},
		
		// 加载备用专业类别数据
		loadFallbackTypeOptions() {
			this.typeOptions = [
				{ id: '1', name: '电气专业' },
				{ id: '2', name: '机械专业' },
				{ id: '3', name: '仪表专业' },
				{ id: '4', name: '安全专业' },
				{ id: '5', name: '环保专业' }
			]
			console.log("使用备用专业类别数据:", this.typeOptions, " at pages/monthly/task-record.uvue:823")
			// 自动填充专业类别
		this.autoFillProfession()
		},
		

		
		// 根据专业信息加载岗检标准项
			loadGjbzDataByProfession() {
				if (this.taskInfo == null || this.taskInfo.profession === '') {
					return
				}
				
				const token = uni.getStorageSync('token')
				uni.request({
					url: getApiUrl(`/gjbz-data/by-profession/${UTSAndroid.consoleDebugError(encodeURIComponent(this.taskInfo.profession), " at pages/monthly/task-record.uvue:838")}`),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						const respdata = res.data as UTSJSONObject
						if (respdata.getNumber('code') === 200) {
							const data = respdata.getArray('data')
							if (data != null && data.length > 0) {
								console.log(`找到${data.length}个与专业"${this.taskInfo.profession}"相关的岗检标准项`, " at pages/monthly/task-record.uvue:848")
								// 这里可以将数据存储到组件的数据中，供后续使用
								// 例如：this.gjbzDataList = data
							} else {
								console.log(`未找到与专业"${this.taskInfo.profession}"相关的岗检标准项`, " at pages/monthly/task-record.uvue:852")
							}
						} else {
							console.error('加载岗检标准项失败:', respdata.getString('message'), " at pages/monthly/task-record.uvue:855")
						}
					},
					fail: (error) => {
						console.error('加载岗检标准项失败:', error, " at pages/monthly/task-record.uvue:859")
					}
				})
			},
			
			// 确认问题类型
			confirmProblemType() {
				this.formData.problemType = this.selectedProblemType
				this.hideProblemTypePicker()
			},
			

			

			
			// 专业类别选择器方法
			showTypePicker() {
				this.showTypePopup = true
			},
			hideTypePicker() {
				this.showTypePopup = false
				this.typeSearchKeyword = ''
			},
            selectType(index: number) {
				this.selectedTypeIndex = index
				this.hideTypePicker()
				// 重置下级选择
				this.selectedModuleIndex = -1
				this.selectedResponsibilityIndex = -1
				this.selectedIssueIndex = -1
				this.moduleOptions = []
				this.responsibilityOptions = []
				this.issueOptions = []
                // 加载模块选项（按专业名称过滤）
                this.loadModuleOptions((this.typeOptions[index] as OptionItem).name)
			},
			selectTypeFromFiltered(item: OptionItem) {
				// 找到原始数组中的索引
				const index = this.typeOptions.findIndex(option => option.id === item.id)
				if (index !== -1) {
					this.selectType(index)
				}
			},
			
			// 模块选择器方法
			showModulePicker() {
				if (this.selectedTypeIndex === -1) {
					uni.showToast({ title: '请先选择专业类别', icon: 'none' })
					return
				}
				this.showModulePopup = true
			},
			hideModulePicker() {
				this.showModulePopup = false
				this.moduleSearchKeyword = ''
			},
			selectModule(index: number) {
				this.selectedModuleIndex = index
				// 回写选中的值到表单数据
				this.formData.module = (this.moduleOptions[index] as OptionItem).name
				this.hideModulePicker()
				// 重置下级选择
				this.selectedResponsibilityIndex = -1
				this.selectedIssueIndex = -1
				this.formData.responsibility = ''
				this.formData.issue = ''
				this.responsibilityOptions = []
				this.issueOptions = []
				// 加载责任选项
				this.loadResponsibilityOptions((this.moduleOptions[index] as OptionItem).id)
				this.loadResponsibilityOptions((this.moduleOptions[index] as OptionItem).name)
			},
			selectModuleFromFiltered(item: OptionItem) {
				// 找到原始数组中的索引
				const index = this.moduleOptions.findIndex(option => option.id === item.id)
				if (index !== -1) {
					this.selectModule(index)
				}
			},
			
			// 责任选择器方法
			showResponsibilityPicker() {
				if (this.selectedModuleIndex === -1) {
					uni.showToast({ title: '请先选择模块', icon: 'none' })
					return
				}
				this.showResponsibilityPopup = true
			},
			hideResponsibilityPicker() {
				this.showResponsibilityPopup = false
				this.responsibilitySearchKeyword = ''
			},
			selectResponsibility(index: number) {
				this.selectedResponsibilityIndex = index
				// 回写选中的值到表单数据
				this.formData.responsibility = (this.responsibilityOptions[index] as OptionItem).name
				this.hideResponsibilityPicker()
				// 重置下级选择
				this.selectedIssueIndex = -1
				this.formData.issue = ''
				this.issueOptions = []
				// 加载问题选项
				this.loadIssueOptions((this.responsibilityOptions[index] as OptionItem).id)
				this.loadIssueOptions((this.responsibilityOptions[index] as OptionItem).name)
			},
			selectResponsibilityFromFiltered(item: OptionItem) {
				// 找到原始数组中的索引
				const index = this.responsibilityOptions.findIndex(option => option.id === item.id)
				if (index !== -1) {
					this.selectResponsibility(index)
				}
			},
			
			// 问题选择器方法
			showIssuePicker() {
				if (this.selectedResponsibilityIndex === -1) {
					uni.showToast({ title: '请先选择责任', icon: 'none' })
					return
				}
				this.showIssuePopup = true
			},
			hideIssuePicker() {
				this.showIssuePopup = false
				this.issueSearchKeyword = ''
			},
			selectIssue(index: number) {
				this.selectedIssueIndex = index
				// 回写选中的值到表单数据
				this.formData.issue = (this.issueOptions[index] as OptionItem).name
				this.hideIssuePicker()
				// 自动回填问题性质和对应分数
				this.autoFillIssueDetails((this.issueOptions[index] as OptionItem).id, (this.issueOptions[index] as OptionItem).name)
			},
			selectIssueFromFiltered(item: OptionItem) {
				// 找到原始数组中的索引
				const index = this.issueOptions.findIndex(option => option.id === item.id)
				if (index !== -1) {
					this.selectIssue(index)
				}
			},
			
			// 性质界定选择器方法
			showQualityPicker() {
				this.loadQualityOptions()
				this.showQualityPopup = true
			},
			hideQualityPicker() {
				this.showQualityPopup = false
				this.qualitySearchKeyword = ''
			},
			selectQuality(index: number) {
				this.selectedQualityIndex = index
				// 回写选中的值到表单数据
				this.formData.quality = (this.qualityOptions[index] as OptionItem).name
				this.hideQualityPicker()
				this.formData.safetyScore = this.getScoreByQualityName((this.qualityOptions[index] as OptionItem).name)
			},
			selectQualityFromFiltered(item: OptionItem) {
				// 找到原始数组中的索引
				const index = this.qualityOptions.findIndex(option => option.id === item.id)
				if (index !== -1) {
					this.selectQuality(index)
				}
			},
			
            // 加载模块选项（按专业类型精确过滤）
            loadModuleOptions(typeName: string) {
                const token = uni.getStorageSync('token')
                uni.request({
                    url: getApiUrl(`/gjbz-data/modules/by-type/${UTSAndroid.consoleDebugError(encodeURIComponent(typeName), " at pages/monthly/task-record.uvue:1028")}`),
                    method: 'GET',
                    header: {
                        'Authorization': `Bearer ${token}`
                    },
                    success: (res) => {
                        const respdata = res.data as UTSJSONObject
                        if (respdata.getNumber('code') === 200) {
                            const modules = respdata.getArray('data') as string[]
                            const unique = [] as string[]
                            if (modules != null) {
                                for (let i = 0; i < modules.length; i++) {
                                    const m = modules[i]
                                    if (m != null && m != '' && unique.indexOf(m) === -1) unique.push(m)
                                }
                            }
                            this.moduleOptions = unique.map((m, idx) => ({ id: (idx + 1).toString(), name: m } as OptionItem))
                        }
                    },
                    fail: (error) => {
                        console.error('按专业加载模块失败:', error, " at pages/monthly/task-record.uvue:1048")
                        this.moduleOptions = [] as OptionItem[]
                    }
                })
            },
			
			// 加载责任选项
            loadResponsibilityOptions(moduleId: string) {
                const token = uni.getStorageSync('token')
                uni.request({
                    url: getApiUrl(`/gjbz-data/options/responsibilities/by-module/${UTSAndroid.consoleDebugError(encodeURIComponent(moduleId), " at pages/monthly/task-record.uvue:1058")}`),
                    method: 'GET',
                    header: {
                        'Authorization': `Bearer ${token}`
                    },
                    success: (res) => {
                        const respdata = res.data as UTSJSONObject
                        if (respdata.getNumber('code') === 200) {
                            const data = respdata.getArray('data')
                            if (data != null) {
                                const options: OptionItem[] = []
                                for (let i = 0; i < data.length; i++) {
                                    const item = data[i] as string
                                    options.push({
                                        id: (i + 1).toString(),
                                        name: item
                                    } as OptionItem)
                                }
                                this.responsibilityOptions = options
                            }
                        }
                    },
                    fail: (error) => {
                        console.error('加载责任选项失败:', error, " at pages/monthly/task-record.uvue:1081")
                        this.responsibilityOptions = [
                            { id: 'resp1', name: '责任1' },
                            { id: 'resp2', name: '责任2' },
                            { id: 'resp3', name: '责任3' }
                        ] as OptionItem[]
                    }
                })
            },
			
			// 加载问题选项
            loadIssueOptions(responsibilityId: string) {
                const token = uni.getStorageSync('token')
                uni.request({
                    url: getApiUrl(`/gjbz-data/options/issues/by-responsibility/${UTSAndroid.consoleDebugError(encodeURIComponent(responsibilityId), " at pages/monthly/task-record.uvue:1095")}`),
                    method: 'GET',
                    header: {
                        'Authorization': `Bearer ${token}`
                    },
                    success: (res) => {
                        const respdata = res.data as UTSJSONObject
                        if (respdata.getNumber('code') === 200) {
                            const data = respdata.getArray('data')
                            if (data != null) {
                                const options: OptionItem[] = []
                                for (let i = 0; i < data.length; i++) {
                                    const item = data[i] as string
                                    options.push({
                                        id: (i + 1).toString(),
                                        name: item
                                    } as OptionItem)
                                }
                                this.issueOptions = options
                            }
                        }
                    },
                    fail: (error) => {
                        console.error('加载问题选项失败:', error, " at pages/monthly/task-record.uvue:1118")
                        this.issueOptions = [
                            { id: 'issue1', name: '问题1' },
                            { id: 'issue2', name: '问题2' },
                            { id: 'issue3', name: '问题3' }
                        ] as OptionItem[]
                    }
                })
            },
			
			// 加载性质界定选项
			loadQualityOptions() {
				const token = uni.getStorageSync('token')
				uni.request({
					url: getApiUrl('/dictionary/type/性质界定'),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`,
						'Content-Type': 'application/json'
					},
					success: (res) => {
						const data = res.data as UTSJSONObject
						const code = data.getNumber('code')
						if (res.statusCode === 200 && code === 200) {
							// 将API返回的字典数据转换为选择器需要的格式
							const dataArray = data.getArray('data')
							if (dataArray != null) {
								this.qualityOptions = []
								for (let i = 0; i < dataArray.length; i++) {
									const item = dataArray[i] as UTSJSONObject
									const id = item.getString('id') ?? ''
									const dictValue = item.getString('dictValue') ?? ''
									const dictLabel = item.getString('dictLabel') ?? ''
									const name = dictValue !== '' ? dictValue : dictLabel
									this.qualityOptions.push({ id: id, name: name } as OptionItem)
								}
							}
							console.log('性质界定选项加载完成:', this.qualityOptions, " at pages/monthly/task-record.uvue:1155")
						} else {
							const message = data.getString('message') ?? '未知错误'
							console.error('获取性质界定数据失败:', message, " at pages/monthly/task-record.uvue:1158")
							// 使用默认数据作为备选
							this.qualityOptions = [
								{ id: '1', name: '一般问题' },
								{ id: '2', name: '较大问题' },
								{ id: '3', name: '严重问题' },
								{ id: '4', name: '重大问题' }
							] as OptionItem[]
						}
					},
					fail: (error) => {
						console.error('请求性质界定数据失败:', error, " at pages/monthly/task-record.uvue:1169")
						// 使用默认数据作为备选
						this.qualityOptions = [
							{ id: '1', name: '一般问题' },
							{ id: '2', name: '较大问题' },
							{ id: '3', name: '严重问题' },
							{ id: '4', name: '重大问题' }
						] as OptionItem[]
					}
				})
			},
			
			// 加载性质界定详情（扣分值和安全记分关联项）
			loadQualityDetails(qualityId: string) {
				const opt = this.qualityOptions.find((o: OptionItem) => o.id === qualityId)
				if (opt != null) {
					this.formData.safetyScore = this.getScoreByQualityName(opt.name)
				}
			},
			
			// 根据选择的问题自动填充问题性质和对应分数
			autoFillIssueDetails(issueId: string, issueName: string) {
				console.log('开始自动填充问题详情:', issueId, issueName, " at pages/monthly/task-record.uvue:1191")
				
				// 首先加载性质界定选项（如果还没有加载）
				if (this.qualityOptions.length === 0) {
					this.loadQualityOptionsAndFill(issueId, issueName)
					return
				}
				
				this.performAutoFill(issueId, issueName)
			},
			
			// 加载性质界定选项并执行自动填充
			loadQualityOptionsAndFill(issueId: string, issueName: string) {
				const token = uni.getStorageSync('token')
				uni.request({
					url: getApiUrl('/dictionary/type/性质界定'),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`,
						'Content-Type': 'application/json'
					},
					success: (res) => {
						const data = res.data as UTSJSONObject
						const code = data.getNumber('code')
						if (res.statusCode === 200 && code === 200) {
							// 将API返回的字典数据转换为选择器需要的格式
							const dataArray = data.getArray('data')
							if (dataArray != null) {
								this.qualityOptions = []
								for (let i = 0; i < dataArray.length; i++) {
									const item = dataArray[i] as UTSJSONObject
									const id = item.getString('id') ?? ''
									const dictValue = item.getString('dictValue') ?? ''
									const dictLabel = item.getString('dictLabel') ?? ''
									const name = dictValue !== '' ? dictValue : dictLabel
									this.qualityOptions.push({ id: id, name: name } as OptionItem)
								}
								// 加载完成后执行自动填充
								this.performAutoFill(issueId, issueName)
							}
						} else {
							const message = data.getString('message') ?? '未知错误'
							console.error('获取性质界定数据失败:', message, " at pages/monthly/task-record.uvue:1233")
							// 使用备用数据
							this.qualityOptions = [
								{ id: '1', name: '一般问题' },
								{ id: '2', name: '较大问题' },
								{ id: '3', name: '严重问题' },
								{ id: '4', name: '重大问题' }
							] as OptionItem[]
							// 执行自动填充
							this.performAutoFill(issueId, issueName)
						}
					},
					fail: (error) => {
						console.error('请求性质界定数据失败:', error, " at pages/monthly/task-record.uvue:1246")
						// 使用备用数据
						this.qualityOptions = [
							{ id: '1', name: '一般问题' },
							{ id: '2', name: '较大问题' },
							{ id: '3', name: '严重问题' },
							{ id: '4', name: '重大问题' }
						] as OptionItem[]
						// 执行自动填充
						this.performAutoFill(issueId, issueName)
					}
				})
			},
			
			// 执行自动填充逻辑
			performAutoFill(issueId: string, issueName: string) {
				// 根据问题名称智能匹配问题性质和分数
				const issueMapping = this.getIssueQualityMapping(issueName) as UTSJSONObject | null
				
				if (issueMapping != null) {
					const qualityName = issueMapping.getString('quality') ?? ''
					const safetyScoreText = issueMapping.getString('safetyScore') ?? ''
					
					// 智能匹配性质界定
				let qualityIndex = -1
				let matchedQualityName = ''
				
				// 首先尝试精确匹配
				qualityIndex = this.qualityOptions.findIndex((item: OptionItem) => item.name === qualityName)
				
				// 如果精确匹配失败，尝试模糊匹配
				if (qualityIndex === -1) {
					// 根据关键词匹配性质界定
					if (qualityName.includes('严重') || qualityName.includes('重大')) {
						qualityIndex = this.qualityOptions.findIndex((item: OptionItem) => 
							item.name.includes('严重') || item.name.includes('重大') || item.name.includes('重要'))
					} else if (qualityName.includes('一般') || qualityName.includes('轻微')) {
						qualityIndex = this.qualityOptions.findIndex((item: OptionItem) => 
							item.name.includes('一般') || item.name.includes('轻微') || item.name.includes('普通'))
					} else if (qualityName.includes('中等') || qualityName.includes('较大')) {
						qualityIndex = this.qualityOptions.findIndex((item: OptionItem) => 
							item.name.includes('中等') || item.name.includes('较大') || item.name.includes('中'))
					}
				}
				
				// 设置性质界定
				if (qualityIndex !== -1) {
					this.selectedQualityIndex = qualityIndex
					matchedQualityName = (this.qualityOptions[qualityIndex] as OptionItem).name
					this.formData.quality = matchedQualityName
				} else {
					// 如果都没有匹配到，使用第一个选项作为默认值
					if (this.qualityOptions.length > 0) {
						this.selectedQualityIndex = 0
						matchedQualityName = (this.qualityOptions[0] as OptionItem).name
						this.formData.quality = matchedQualityName
					}
				}
					
					this.formData.safetyScore = this.getScoreByQualityName(matchedQualityName)
				
				console.log('自动填充完成:', {
					originalQuality: qualityName,
					matchedQuality: matchedQualityName,
					safetyScore: this.formData.safetyScore,
					selectedQualityIndex: this.selectedQualityIndex
				}, " at pages/monthly/task-record.uvue:1307")
				
				// 显示提示信息
				uni.showToast({
					title: `已自动填充：${matchedQualityName}，${this.formData.safetyScore}分`,
					icon: 'success',
					duration: 2000
				})
				} else {
					console.log('未找到匹配的问题映射规则', " at pages/monthly/task-record.uvue:1321")
				}
			},
			
			// 获取问题与性质界定的映射关系
			getIssueQualityMapping(issueName: string): UTSJSONObject {
    // 定义问题与性质界定的映射规则
			const mappingRules = [
				// 严重问题类型
				{ keywords: ['违章', '违规', '严重', '重大', '危险', '安全隐患'], quality: '严重问题', safetyScore: '15分 - 严重安全记分' },
				{ keywords: ['火灾', '爆炸', '中毒', '触电', '高空坠落'], quality: '重大问题', safetyScore: '25分 - 重大安全记分' },
				
				// 一般问题类型
				{ keywords: ['轻微', '一般', '小问题', '不规范', '不完善'], quality: '一般问题', safetyScore: '5分 - 一般安全记分' },
				{ keywords: ['标识', '标牌', '记录', '台账', '文档'], quality: '一般问题', safetyScore: '3分 - 一般安全记分' },
				
				// 中等问题类型
				{ keywords: ['设备', '工具', '防护', '操作', '流程'], quality: '中等问题', safetyScore: '10分 - 中等安全记分' },
				{ keywords: ['环境', '卫生', '整理', '整顿'], quality: '中等问题', safetyScore: '8分 - 中等安全记分' }
			] as UTSJSONObject[]
				
      // 查找匹配的规则
      for (let i = 0; i < mappingRules.length; i++) {
       const rule = mappingRules[i] as UTSJSONObject
       const keywords = rule.getArray('keywords') as string[]
       for (let j = 0; j < keywords.length; j++) {
        const keyword = keywords[j]
        if (issueName.includes(keyword)) {
					return {
						quality: rule.quality,
						safetyScore: rule.safetyScore
					}
				}
					}
				}
				
				// 如果没有匹配到，返回默认值
				return {
					quality: '一般问题',
					safetyScore: '5分 - 一般安全记分'
				}
			},
			getScoreByQualityName(qualityName: string): string {
				const s: string = (qualityName == null) ? '' : qualityName
				if (s.indexOf('重要') >= 0) return '5'
				if (s.indexOf('一般') >= 0) return '3'
				if (s.indexOf('观察') >= 0) return '1'
				return '0'
			},
			
			// 添加照片
			addPhoto() {
				uni.chooseImage({
					count: 3 - this.formData.photos.length,
					sizeType: ['compressed'],
					sourceType: ['camera', 'album'],
					success: (res) => {
						// 逐个上传选择的图片
						res.tempFilePaths.forEach(tempPath => {
							this.uploadImage(tempPath)
						})
					}
				})
			},
			
			// 上传图片到服务器
			uploadImage(imagePath: string) {
				console.log('=== 移动端图片上传调试信息 ===', " at pages/monthly/task-record.uvue:1388")
				console.log('选择的图片路径:', imagePath, " at pages/monthly/task-record.uvue:1389")
				
				uni.showLoading({
					title: '上传中...'
				})
				
				uni.uploadFile({
					url: getApiUrl('/upload/image'),
					filePath: imagePath,
					name: 'file',
					header: {
						'Authorization': uni.getStorageSync('token')
					},
					success: (uploadRes) => {
						uni.hideLoading()
						console.log('上传响应状态码:', uploadRes.statusCode, " at pages/monthly/task-record.uvue:1404")
						console.log('上传响应数据:', uploadRes.data, " at pages/monthly/task-record.uvue:1405")
						
						try {
							const result = UTSAndroid.consoleDebugError(JSON.parse(uploadRes.data), " at pages/monthly/task-record.uvue:1408") as UTSJSONObject
							console.log('解析后的响应数据:', result, " at pages/monthly/task-record.uvue:1409")
							
							const code = result.getNumber('code')
							if (code === 200) {
								// 上传成功，将服务器返回的URL添加到photos数组
								const data = result.getJSON('data')
								if (data != null) {
									const url = data.getString('url')
									console.log('服务器返回的图片URL:', url, " at pages/monthly/task-record.uvue:1417")
									if (url != null) {
										this.formData.photos.push(url)
										console.log('当前photos数组:', this.formData.photos, " at pages/monthly/task-record.uvue:1420")
									}
								}
							} else {
								let message = result.getString('message')
								if (message == null) {
									message = '上传失败'
								}
								console.error('上传失败，服务器响应:', result, " at pages/monthly/task-record.uvue:1428")
								uni.showToast({
									title: message,
									icon: 'none'
								})
							}
						} catch (e) {
							console.error('解析响应数据失败:', e, " at pages/monthly/task-record.uvue:1435")
							console.error('原始响应数据:', uploadRes.data, " at pages/monthly/task-record.uvue:1436")
							uni.showToast({
								title: '上传失败',
								icon: 'none'
							})
						}
						console.log('=== 移动端图片上传调试信息结束 ===', " at pages/monthly/task-record.uvue:1442")
					},
					fail: (error) => {
						uni.hideLoading()
						console.error('图片上传失败:', error, " at pages/monthly/task-record.uvue:1446")
						uni.showToast({
							title: '上传失败',
							icon: 'none'
						})
					}
				})
			},
			
			// 删除照片
			deletePhoto(index: number) {
				this.formData.photos.splice(index, 1)
			},
			
			// 预览照片
	previewPhoto(photo: string) {
		uni.previewImage({
			current: this.convertToHttpUrl(photo),
			urls: this.formData.photos.map((p: string) => this.convertToHttpUrl(p)) as string[]
		})
	},
			
			// 保存记录
			saveRecord() {
				// 表单验证
				if (this.selectedTypeIndex === -1) {
					uni.showToast({ title: '请选择专业类别', icon: 'none' })
					return
				}
				
				if (this.selectedModuleIndex === -1) {
					uni.showToast({ title: '请选择模块', icon: 'none' })
					return
				}
				
				if (this.selectedResponsibilityIndex === -1) {
					uni.showToast({ title: '请选择责任', icon: 'none' })
					return
				}
				
				if (this.selectedIssueIndex === -1) {
					uni.showToast({ title: '请选择问题', icon: 'none' })
					return
				}
				
				if (this.selectedQualityIndex === -1) {
					uni.showToast({ title: '请选择性质界定', icon: 'none' })
					return
				}
				
				if (this.formData.problemDescription.trim() == '') {
					uni.showToast({ title: '请输入问题描述', icon: 'none' })
					return
				}
				
				if (this.formData.location.trim() == '') {
					uni.showToast({ title: '请输入发现位置', icon: 'none' })
					return
				}
				
        if (this.formData.rectificationDate == '') {
          uni.showToast({ title: '请选择整改期限', icon: 'none' })
          return
        }

        if (this.formData.responsibleUnit.trim() == '') {
          uni.showToast({ title: '请输入责任单位', icon: 'none' })
          return
        }

        if (this.formData.responsiblePerson.trim() == '') {
          uni.showToast({ title: '请输入责任人', icon: 'none' })
          return
        }
				
				if (this.formData.inspector.trim() == '') {
					uni.showToast({ title: '请输入检查人', icon: 'none' })
					return
				}
				
				try {
					uni.showLoading({ title: '保存中...' })
					
					// 获取用户信息
					const userInfoStr = uni.getStorageSync('userInfo') as string
					let userId = ''
					if (userInfoStr != null && userInfoStr != '') {
						try {
							const userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/task-record.uvue:1534") as UTSJSONObject
							userId = userInfo.getString('id') ?? ''
						} catch (e) {
							console.error('解析用户信息失败:', e, " at pages/monthly/task-record.uvue:1537")
						}
					}
					
					if (userId == '') {
						uni.hideLoading()
						uni.showToast({ title: '获取用户信息失败，请重新登录', icon: 'none' })
						return
					}
					
					// 构建保存数据
        const saveData = {__$originalPosition: new UTSSourceMapPosition("saveData", "pages/monthly/task-record.uvue", 1548, 15),
        taskId: this.taskInfo.taskId,
        professionalCategory: this.selectedTypeIndex !== -1 && this.typeOptions[this.selectedTypeIndex] != null ? this.typeOptions[this.selectedTypeIndex].name : '',
        module: this.selectedModuleIndex !== -1 && this.moduleOptions[this.selectedModuleIndex] != null ? this.moduleOptions[this.selectedModuleIndex].name : '',
        responsibility: this.selectedResponsibilityIndex !== -1 && this.responsibilityOptions[this.selectedResponsibilityIndex] != null ? this.responsibilityOptions[this.selectedResponsibilityIndex].name : '',
        issue: this.selectedIssueIndex !== -1 && this.issueOptions[this.selectedIssueIndex] != null ? this.issueOptions[this.selectedIssueIndex].name : '',
        qualityDetermination: this.selectedQualityIndex !== -1 && this.qualityOptions[this.selectedQualityIndex] != null ? this.qualityOptions[this.selectedQualityIndex].name : '',
            problemDescription: this.formData.problemDescription,
            location: this.formData.location,
            rectificationDate: this.formData.rectificationDate,
            inspector: this.formData.inspector,
            safetyScore: this.formData.safetyScore,
            photos: this.formData.photos,
            responsibleUnit: this.formData.responsibleUnit,
            responsiblePerson: this.formData.responsiblePerson
        }
					
					// 调用月检记录保存接口
					uni.request({
						url: getApiUrl('/monthly-inspection-record'),
						method: 'POST',
						header: {
							'Content-Type': 'application/json',
							'Authorization': uni.getStorageSync('token') != null ? uni.getStorageSync('token') as string : '',
							'userId': userId
						},
						data: {
							...saveData,
							photos: this.formData.photos.join(',') // 将数组转换为逗号分隔的字符串
						},
						success: (res) => {
							uni.hideLoading()
							const respdate=res.data as UTSJSONObject
							if (respdate.getNumber('code') == 200) {
								uni.showToast({ title: '保存成功', icon: 'success' })
								setTimeout(() => {
									uni.navigateBack()
								}, 1500)
							} else {
								const msg = respdate.getString('message')
								uni.showToast({ title: msg != null ? msg : '保存失败', icon: 'none' })
							}
						},
						fail: (error) => {
							uni.hideLoading()
							console.error('保存失败:', error, " at pages/monthly/task-record.uvue:1593")
							uni.showToast({ title: '保存失败', icon: 'none' })
						}
					})
				} catch (error) {
					uni.hideLoading()
					console.error('保存失败:', error, " at pages/monthly/task-record.uvue:1599")
					uni.showToast({ title: '保存失败', icon: 'none' })
				}
			},
			
			// 初始化日期数组
			initDateArrays() {
				// 生成年份数组（当前年份到未来2年）
				const currentYear = new Date().getFullYear()
				this.years = []
				for (let i = currentYear; i <= currentYear + 2; i++) {
					this.years.push(i)
				}
				
				// 生成月份数组
				this.months = []
				for (let i = 1; i <= 12; i++) {
					this.months.push(i)
				}
				
				// 生成日期数组
				this.updateDays()
				
				// 设置默认选择值
				const yearIndex = this.years.findIndex(year => year === this.selectedYear)
				const monthIndex = (this.selectedMonth as number) - 1
				const dayIndex = this.days.findIndex(day => day === this.selectedDay)
				this.pickerValue = [yearIndex >= 0 ? yearIndex : 0, monthIndex, dayIndex >= 0 ? dayIndex : 0]
				
				console.log('初始化日期数组:', {
					selectedYear: this.selectedYear,
					selectedMonth: this.selectedMonth,
					selectedDay: this.selectedDay,
					yearIndex, monthIndex, dayIndex,
					pickerValue: this.pickerValue,
					daysArray: this.days
				}, " at pages/monthly/task-record.uvue:1628")
			},
			
			// 更新日期数组
			updateDays() {
				const daysInMonth = new Date(this.selectedYear, this.selectedMonth, 0).getDate()
				this.days = []
				for (let i = 1; i <= daysInMonth; i++) {
					this.days.push(i)
				}
			},
			
			// 显示日期选择器
			showDatePicker() {
				// 如果当前有设置的日期，使用该日期作为选择器的默认值
				if (this.formData.rectificationDate !== '') {
					const dateParts = this.formData.rectificationDate.split('-')
					if (dateParts.length === 3) {
						this.selectedYear = parseInt(dateParts[0])
						this.selectedMonth = parseInt(dateParts[1])
						this.selectedDay = parseInt(dateParts[2])
					}
				}
				
				// 更新日期数组
				this.updateDays()
				
				// 设置picker的默认选择值
				const yearIndex = this.years.findIndex(year => year === this.selectedYear)
				const monthIndex = this.selectedMonth - 1
				let dayIndex = this.days.findIndex(day => day === this.selectedDay)
				
				// 如果找不到对应的日期（比如选择了31号但当前月只有30天），则选择最后一天
				if (dayIndex < 0) {
					dayIndex = (this.days.length - 1) as Int
					this.selectedDay = this.days[dayIndex]
					console.log('日期不存在，调整为月末:', this.selectedDay, " at pages/monthly/task-record.uvue:1671")
				}
				
				this.pickerValue = [yearIndex >= 0 ? yearIndex : 0, monthIndex, dayIndex]
				
				console.log('显示日期选择器:', {
					selectedDate: this.formData.rectificationDate,
					selectedYear: this.selectedYear,
					selectedMonth: this.selectedMonth,
					selectedDay: this.selectedDay,
					yearIndex, monthIndex, dayIndex,
					pickerValue: this.pickerValue
				}, " at pages/monthly/task-record.uvue:1676")
				
				this.datePickerVisible = true
			},
			
			// 隐藏日期选择器
			hideDatePicker() {
				this.datePickerVisible = false
			},
			
			// picker-view变化事件
			onPickerChange(e: UniPickerViewChangeEvent) {
				const value = e.detail.value as number[]
				const oldDaysLength = this.days.length
				
				console.log('picker原始变化事件:', {
					value: value,
					oldSelectedYear: this.selectedYear,
					oldSelectedMonth: this.selectedMonth,
					oldSelectedDay: this.selectedDay,
					oldDaysLength: oldDaysLength
				}, " at pages/monthly/task-record.uvue:1698")
				
				// 更新选择的年月日
				this.selectedYear = this.years[value[0]]
				this.selectedMonth = value[1] + 1  // 月份索引从0开始，实际月份从1开始
				
				// 更新日期数组（因为年月变化可能影响该月的天数）
				this.updateDays()
				
				// 处理日期变化
				let newDayIndex = value[2]
				if (newDayIndex >= this.days.length) {
					// 如果选择的日期超出了当前月的天数，调整为月末
					newDayIndex = this.days.length - 1
					console.log('日期超出范围，调整为月末:', this.days[newDayIndex], " at pages/monthly/task-record.uvue:1718")
				}
				
				this.selectedDay = this.days[newDayIndex]
				
				// 如果天数数组长度发生变化，需要延迟更新pickerValue以避免冲突
				if (oldDaysLength !== this.days.length) {
					// 使用防抖机制，避免频繁更新
					const timer = this.pickerDebounceTimer
					if (timer !== null) {
						clearTimeout(timer)
					}
					
					this.pickerDebounceTimer = setTimeout(() => {
						this.pickerValue = [value[0], value[1], newDayIndex]
						console.log('延迟更新pickerValue:', {
							newPickerValue: this.pickerValue,
							selectedYear: this.selectedYear,
							selectedMonth: this.selectedMonth,
							selectedDay: this.selectedDay
						}, " at pages/monthly/task-record.uvue:1733")
					}, 50) // 50ms延迟，提供更快速的平滑视觉效果
				} else {
					// 直接更新
					this.pickerValue = [value[0], value[1], newDayIndex]
					console.log('直接更新pickerValue:', {
						newPickerValue: this.pickerValue,
						selectedYear: this.selectedYear,
						selectedMonth: this.selectedMonth,
						selectedDay: this.selectedDay
					}, " at pages/monthly/task-record.uvue:1743")
				}
			},
			
			// 确认日期选择
			confirmDatePicker() {
				// 直接从pickerValue索引获取实际选择的值
				const selectedYear = this.years[this.pickerValue[0]]
				const selectedMonth = this.pickerValue[1] + 1  // 月份索引从0开始，实际月份从1开始
				const selectedDay = this.days[this.pickerValue[2]]
				
				const year = selectedYear.toString().padStart(4, '0')
				const month = selectedMonth.toString().padStart(2, '0')
				const day = selectedDay.toString().padStart(2, '0')
				const newDate = `${year}-${month}-${day}`
				
				console.log('确认日期选择:', {
					pickerValue: this.pickerValue,
					yearsArray: this.years,
					monthsArray: this.months,
					daysArray: this.days,
					selectedYear: selectedYear,
					selectedMonth: selectedMonth,
					selectedDay: selectedDay,
					formattedDate: newDate
				}, " at pages/monthly/task-record.uvue:1764")
				
				this.formData.rectificationDate = newDate
				this.hideDatePicker()
			}
		}
	})

export default __sfc__
function GenPagesMonthlyTaskRecordRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
const _component_picker_view_column = resolveComponent("picker-view-column")
const _component_picker_view = resolveComponent("picker-view")

  return _cE("scroll-view", _uM({
    class: "container",
    "scroll-y": "true",
    "scroll-top": _ctx.scrollTop,
    style: _nS(_uM({height: _ctx.containerHeight + 'px'})),
    "enable-back-to-top": "true"
  }), [
    _cE("view", _uM({ class: "header" }), [
      _cE("view", _uM({
        class: "header-left",
        onClick: _ctx.goBack
      }), null, 8 /* PROPS */, ["onClick"]),
      _cE("text", _uM({ class: "header-title" }), "月检任务录入"),
      _cE("view", _uM({ class: "header-right" }))
    ]),
    _cE("view", _uM({ class: "task-info" }), [
      _cE("view", _uM({ class: "info-item" }), [
        _cE("text", _uM({ class: "info-label" }), "任务编号:"),
        _cE("text", _uM({ class: "info-value" }), _tD(_ctx.taskInfo != null ? _ctx.taskInfo.taskNumber : ''), 1 /* TEXT */)
      ]),
      _cE("view", _uM({ class: "info-item" }), [
        _cE("text", _uM({ class: "info-label" }), "月检时间:"),
        _cE("text", _uM({ class: "info-value" }), _tD(_ctx.taskInfo != null ? _ctx.taskInfo.inspectionDate : ''), 1 /* TEXT */)
      ]),
      _cE("view", _uM({ class: "info-item" }), [
        _cE("text", _uM({ class: "info-label" }), "检查部门:"),
        _cE("text", _uM({ class: "info-value" }), _tD(_ctx.taskInfo != null ? _ctx.taskInfo.department : ''), 1 /* TEXT */)
      ]),
      _cE("view", _uM({ class: "info-item" }), [
        _cE("text", _uM({ class: "info-label" }), "检查专业:"),
        _cE("text", _uM({ class: "info-value" }), _tD(_ctx.taskInfo != null ? _ctx.taskInfo.profession : ''), 1 /* TEXT */)
      ]),
      _cE("view", _uM({ class: "info-item" }), [
        _cE("text", _uM({ class: "info-label" }), "检查组长:"),
        _cE("text", _uM({ class: "info-value" }), _tD(_ctx.taskInfo != null ? _ctx.taskInfo.groupLeader : ''), 1 /* TEXT */)
      ]),
      _cE("view", _uM({ class: "info-item" }), [
        _cE("text", _uM({ class: "info-label" }), "检查副组长:"),
        _cE("text", _uM({ class: "info-value" }), _tD(_ctx.taskInfo.deputyLeader), 1 /* TEXT */)
      ])
    ]),
    _cE("view", _uM({ class: "form-section" }), [
      _cE("view", _uM({ class: "section-title" }), [
        _cE("text", _uM({ class: "title-text" }), "问题录入"),
        _cE("text", _uM({ class: "required-mark" }), "*")
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label-left" }), "专业类别"),
        _cE("view", _uM({
          class: _nC(["custom-picker-full", _uM({disabled: _ctx.typeOptions.length === 0})]),
          onClick: _ctx.showTypePicker
        }), [
          _cE("text", _uM({
            class: _nC(["picker-text-full", _uM({placeholder: _ctx.selectedTypeIndex === -1})])
          }), _tD(_ctx.selectedTypeIndex === -1 ? '请选择专业类别' : (_ctx.selectedTypeIndex < _ctx.typeOptions.length ? (_ctx.typeOptions[_ctx.selectedTypeIndex] as OptionItem).name : '请选择专业类别')), 3 /* TEXT, CLASS */)
        ], 10 /* CLASS, PROPS */, ["onClick"])
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label-left" }), "模块"),
        _cE("view", _uM({
          class: _nC(["custom-picker-full", _uM({disabled: _ctx.moduleOptions.length === 0})]),
          onClick: _ctx.showModulePicker
        }), [
          _cE("text", _uM({
            class: _nC(["picker-text-full", _uM({placeholder: _ctx.selectedModuleIndex === -1})])
          }), _tD(_ctx.selectedModuleIndex === -1 ? '请选择模块' : (_ctx.selectedModuleIndex < _ctx.moduleOptions.length ? (_ctx.moduleOptions[_ctx.selectedModuleIndex] as OptionItem).name : '请选择模块')), 3 /* TEXT, CLASS */)
        ], 10 /* CLASS, PROPS */, ["onClick"])
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label-left" }), "责任"),
        _cE("view", _uM({
          class: _nC(["custom-picker-full", _uM({disabled: _ctx.responsibilityOptions.length === 0})]),
          onClick: _ctx.showResponsibilityPicker
        }), [
          _cE("text", _uM({
            class: _nC(["picker-text-full", _uM({placeholder: _ctx.selectedResponsibilityIndex === -1})])
          }), _tD(_ctx.selectedResponsibilityIndex === -1 ? '请选择责任' : (_ctx.selectedResponsibilityIndex < _ctx.responsibilityOptions.length ? (_ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex] as OptionItem).name : '请选择责任')), 3 /* TEXT, CLASS */)
        ], 10 /* CLASS, PROPS */, ["onClick"])
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label-left" }), "问题"),
        _cE("view", _uM({
          class: _nC(["custom-picker-full", _uM({disabled: _ctx.issueOptions.length === 0})]),
          onClick: _ctx.showIssuePicker
        }), [
          _cE("text", _uM({
            class: _nC(["picker-text-full", _uM({placeholder: _ctx.selectedIssueIndex === -1})])
          }), _tD(_ctx.selectedIssueIndex === -1 ? '请选择问题' : (_ctx.selectedIssueIndex < _ctx.issueOptions.length ? (_ctx.issueOptions[_ctx.selectedIssueIndex] as OptionItem).name : '请选择问题')), 3 /* TEXT, CLASS */)
        ], 10 /* CLASS, PROPS */, ["onClick"])
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label-left" }), "性质界定"),
        _cE("view", _uM({
          class: _nC(["custom-picker-full", _uM({disabled: _ctx.qualityOptions.length === 0})]),
          onClick: _ctx.showQualityPicker
        }), [
          _cE("text", _uM({
            class: _nC(["picker-text-full", _uM({placeholder: _ctx.selectedQualityIndex === -1})])
          }), _tD(_ctx.selectedQualityIndex === -1 ? '请选择性质界定' : (_ctx.selectedQualityIndex < _ctx.qualityOptions.length ? (_ctx.qualityOptions[_ctx.selectedQualityIndex] as OptionItem).name : '请选择性质界定')), 3 /* TEXT, CLASS */)
        ], 10 /* CLASS, PROPS */, ["onClick"])
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label-left" }), "安全记分"),
        _cE("input", _uM({
          class: "form-input",
          modelValue: _ctx.formData.safetyScore,
          onInput: ($event: UniInputEvent) => {(_ctx.formData.safetyScore) = $event.detail.value},
          placeholder: "自动带出安全记分",
          readonly: ""
        }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label" }), "问题描述"),
        _cE("textarea", _uM({
          class: "textarea-input",
          modelValue: _ctx.formData.problemDescription,
          onInput: ($event: UniInputEvent) => {(_ctx.formData.problemDescription) = $event.detail.value},
          placeholder: "请详细描述发现的问题",
          maxlength: "500"
        }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"]),
        _cE("text", _uM({ class: "char-count" }), _tD(_ctx.formData.problemDescription.length) + "/500", 1 /* TEXT */)
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label-left" }), "发现位置"),
        _cE("input", _uM({
          class: "form-input",
          modelValue: _ctx.formData.location,
          onInput: ($event: UniInputEvent) => {(_ctx.formData.location) = $event.detail.value},
          placeholder: "请输入问题发现的具体位置"
        }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label-left" }), "责任单位"),
        _cE("input", _uM({
          class: "form-input",
          modelValue: _ctx.formData.responsibleUnit,
          onInput: ($event: UniInputEvent) => {(_ctx.formData.responsibleUnit) = $event.detail.value},
          placeholder: "请输入责任单位"
        }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label-left" }), "责任人"),
        _cE("input", _uM({
          class: "form-input",
          modelValue: _ctx.formData.responsiblePerson,
          onInput: ($event: UniInputEvent) => {(_ctx.formData.responsiblePerson) = $event.detail.value},
          placeholder: "请输入责任人"
        }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label-left" }), "整改期限"),
        _cE("view", _uM({
          class: "picker-container",
          onClick: _ctx.showDatePicker
        }), [
          _cE("text", _uM({
            class: _nC(["picker-text-full", _uM({placeholder: _ctx.formData.rectificationDate === ''})])
          }), _tD(_ctx.formData.rectificationDate !== '' ? _ctx.formData.rectificationDate : '请选择整改期限'), 3 /* TEXT, CLASS */)
        ], 8 /* PROPS */, ["onClick"]),
        isTrue(_ctx.datePickerVisible)
          ? _cE("view", _uM({
              key: 0,
              class: "date-picker-modal",
              onClick: _ctx.hideDatePicker
            }), [
              _cE("view", _uM({
                class: "date-picker-content",
                onClick: withModifiers(() => {}, ["stop"])
              }), [
                _cE("view", _uM({ class: "date-picker-header" }), [
                  _cE("text", _uM({
                    class: "date-picker-cancel",
                    onClick: _ctx.hideDatePicker
                  }), "取消", 8 /* PROPS */, ["onClick"]),
                  _cE("text", _uM({ class: "date-picker-title" }), "选择日期"),
                  _cE("text", _uM({
                    class: "date-picker-confirm",
                    onClick: _ctx.confirmDatePicker
                  }), "确定", 8 /* PROPS */, ["onClick"])
                ]),
                _cV(_component_picker_view, _uM({
                  class: "date-picker-view",
                  value: _ctx.pickerValue,
                  onChange: _ctx.onPickerChange,
                  "indicator-style": "height: 70px; top: 20%; margin-top: -20px;"
                }), _uM({
                  default: withSlotCtx((): any[] => [
                    _cV(_component_picker_view_column, null, _uM({
                      default: withSlotCtx((): any[] => [
                        _cE(Fragment, null, RenderHelpers.renderList(_ctx.years, (year, index, __index, _cached): any => {
                          return _cE("view", _uM({
                            key: index,
                            class: _nC(["picker-item", _uM({selected: index === _ctx.pickerValue[0]})])
                          }), _tD(year) + "年 ", 3 /* TEXT, CLASS */)
                        }), 128 /* KEYED_FRAGMENT */)
                      ]),
                      _: 1 /* STABLE */
                    })),
                    _cV(_component_picker_view_column, null, _uM({
                      default: withSlotCtx((): any[] => [
                        _cE(Fragment, null, RenderHelpers.renderList(_ctx.months, (month, index, __index, _cached): any => {
                          return _cE("view", _uM({
                            key: index,
                            class: _nC(["picker-item", _uM({selected: index === _ctx.pickerValue[1]})])
                          }), _tD(month) + "月 ", 3 /* TEXT, CLASS */)
                        }), 128 /* KEYED_FRAGMENT */)
                      ]),
                      _: 1 /* STABLE */
                    })),
                    _cV(_component_picker_view_column, null, _uM({
                      default: withSlotCtx((): any[] => [
                        _cE(Fragment, null, RenderHelpers.renderList(_ctx.days, (day, index, __index, _cached): any => {
                          return _cE("view", _uM({
                            key: index,
                            class: _nC(["picker-item", _uM({selected: index === _ctx.pickerValue[2]})])
                          }), _tD(day) + "日 ", 3 /* TEXT, CLASS */)
                        }), 128 /* KEYED_FRAGMENT */),
                        _cE(Fragment, null, RenderHelpers.renderList(2, (i, __key, __index, _cached): any => {
                          return _cE("view", _uM({
                            key: 'empty-day-' + i,
                            class: "picker-item picker-item-spacer"
                          }))
                        }), 64 /* STABLE_FRAGMENT */)
                      ]),
                      _: 1 /* STABLE */
                    }))
                  ]),
                  _: 1 /* STABLE */
                }), 8 /* PROPS */, ["value", "onChange"])
              ], 8 /* PROPS */, ["onClick"])
            ], 8 /* PROPS */, ["onClick"])
          : _cC("v-if", true)
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label-left" }), "检查人"),
        _cE("input", _uM({
          class: "form-input",
          modelValue: _ctx.formData.inspector,
          onInput: ($event: UniInputEvent) => {(_ctx.formData.inspector) = $event.detail.value},
          placeholder: "请输入检查人姓名"
        }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("text", _uM({ class: "form-label" }), "问题照片"),
        _cE("view", _uM({ class: "photo-upload" }), [
          _cE(Fragment, null, RenderHelpers.renderList(_ctx.formData.photos, (photo, index, __index, _cached): any => {
            return _cE("view", _uM({
              key: index,
              class: "photo-item"
            }), [
              _cE("image", _uM({
                src: _ctx.convertToHttpUrl(photo),
                class: "photo-preview",
                onClick: () => {_ctx.previewPhoto(photo)}
              }), null, 8 /* PROPS */, ["src", "onClick"]),
              _cE("view", _uM({
                class: "photo-delete",
                onClick: () => {_ctx.deletePhoto(index)}
              }), [
                _cE("text", _uM({ class: "delete-icon" }), "×")
              ], 8 /* PROPS */, ["onClick"])
            ])
          }), 128 /* KEYED_FRAGMENT */),
          _ctx.formData.photos.length < 3
            ? _cE("view", _uM({
                key: 0,
                class: "photo-add",
                onClick: _ctx.addPhoto
              }), [
                _cE("text", _uM({ class: "add-icon" }), "+"),
                _cE("text", _uM({ class: "add-text" }), "添加照片")
              ], 8 /* PROPS */, ["onClick"])
            : _cC("v-if", true)
        ]),
        _cE("text", _uM({ class: "photo-tip" }), "最多可上传3张照片")
      ])
    ]),
    _cE("view", _uM({ class: "bottom-save-container" }), [
      _cE("view", _uM({
        class: "bottom-save-btn",
        onClick: _ctx.saveRecord
      }), [
        _cE("text", _uM({ class: "bottom-save-text" }), "保存")
      ], 8 /* PROPS */, ["onClick"])
    ]),
    isTrue(_ctx.showProblemTypeModal)
      ? _cE("view", _uM({
          key: 0,
          class: "picker-modal",
          onClick: _ctx.hideProblemTypePicker
        }), [
          _cE("view", _uM({
            class: "picker-content",
            onClick: withModifiers(() => {}, ["stop"])
          }), [
            _cE("view", _uM({ class: "picker-header" }), [
              _cE("text", _uM({
                class: "picker-cancel",
                onClick: _ctx.hideProblemTypePicker
              }), "取消", 8 /* PROPS */, ["onClick"]),
              _cE("text", _uM({ class: "picker-title" }), "选择问题类型"),
              _cE("text", _uM({
                class: "picker-confirm",
                onClick: _ctx.confirmProblemType
              }), "确定", 8 /* PROPS */, ["onClick"])
            ]),
            _cE("view", _uM({ class: "picker-list" }), [
              _cE(Fragment, null, RenderHelpers.renderList(_ctx.problemTypes, (type, index, __index, _cached): any => {
                return _cE("view", _uM({
                  key: index,
                  class: _nC(["picker-item", _uM({selected: _ctx.selectedProblemType === type})]),
                  onClick: () => {_ctx.selectProblemType(type)}
                }), [
                  _cE("text", _uM({ class: "picker-item-text" }), _tD(type), 1 /* TEXT */),
                  _ctx.selectedProblemType === type
                    ? _cE("text", _uM({
                        key: 0,
                        class: "picker-check"
                      }), "✓")
                    : _cC("v-if", true)
                ], 10 /* CLASS, PROPS */, ["onClick"])
              }), 128 /* KEYED_FRAGMENT */)
            ])
          ], 8 /* PROPS */, ["onClick"])
        ], 8 /* PROPS */, ["onClick"])
      : _cC("v-if", true),
    isTrue(_ctx.showTypePopup)
      ? _cE("view", _uM({
          key: 1,
          class: "popup-overlay",
          onClick: _ctx.hideTypePicker
        }), [
          _cE("view", _uM({
            class: "popup-content",
            onClick: withModifiers(() => {}, ["stop"])
          }), [
            _cE("view", _uM({ class: "popup-header" }), [
              _cE("text", _uM({ class: "popup-title" }), "选择专业类别"),
              _cE("text", _uM({
                class: "popup-close",
                onClick: _ctx.hideTypePicker
              }), "×", 8 /* PROPS */, ["onClick"])
            ]),
            _cE("view", _uM({ class: "popup-search" }), [
              _cE("input", _uM({
                class: "search-input",
                modelValue: _ctx.typeSearchKeyword,
                onInput: ($event: UniInputEvent) => {(_ctx.typeSearchKeyword) = $event.detail.value},
                placeholder: "搜索专业类别..."
              }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
            ]),
            _cE("scroll-view", _uM({
              class: "popup-list",
              "scroll-y": "true"
            }), [
              _ctx.filteredTypeOptions.length === 0
                ? _cE("view", _uM({
                    key: 0,
                    class: "popup-empty"
                  }), [
                    _cE("text", _uM({ class: "empty-text" }), "暂无匹配的专业类别")
                  ])
                : _cE(Fragment, _uM({ key: 1 }), RenderHelpers.renderList(_ctx.filteredTypeOptions, (item, index, __index, _cached): any => {
                    return _cE("view", _uM({
                      key: (item as OptionItem).id,
                      class: _nC(['popup-item', _ctx.selectedTypeIndex >= 0 && _ctx.typeOptions[_ctx.selectedTypeIndex] != null && _ctx.typeOptions[_ctx.selectedTypeIndex].id === (item as OptionItem).id ? 'popup-item-selected' : '']),
                      onClick: () => {_ctx.selectTypeFromFiltered(item)}
                    }), [
                      _cE("text", _uM({ class: "popup-item-text" }), _tD((item as OptionItem).name), 1 /* TEXT */),
                      isTrue(_ctx.selectedTypeIndex >= 0 && _ctx.typeOptions[_ctx.selectedTypeIndex] != null && _ctx.typeOptions[_ctx.selectedTypeIndex].id === (item as OptionItem).id)
                        ? _cE("text", _uM({
                            key: 0,
                            class: "popup-item-check"
                          }), "✓")
                        : _cC("v-if", true)
                    ], 10 /* CLASS, PROPS */, ["onClick"])
                  }), 128 /* KEYED_FRAGMENT */)
            ])
          ], 8 /* PROPS */, ["onClick"])
        ], 8 /* PROPS */, ["onClick"])
      : _cC("v-if", true),
    isTrue(_ctx.showModulePopup)
      ? _cE("view", _uM({
          key: 2,
          class: "popup-overlay",
          onClick: _ctx.hideModulePicker
        }), [
          _cE("view", _uM({
            class: "popup-content",
            onClick: withModifiers(() => {}, ["stop"])
          }), [
            _cE("view", _uM({ class: "popup-header" }), [
              _cE("text", _uM({ class: "popup-title" }), "选择模块"),
              _cE("text", _uM({
                class: "popup-close",
                onClick: _ctx.hideModulePicker
              }), "×", 8 /* PROPS */, ["onClick"])
            ]),
            _cE("view", _uM({ class: "popup-search" }), [
              _cE("input", _uM({
                class: "search-input",
                modelValue: _ctx.moduleSearchKeyword,
                onInput: ($event: UniInputEvent) => {(_ctx.moduleSearchKeyword) = $event.detail.value},
                placeholder: "搜索模块..."
              }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
            ]),
            _cE("scroll-view", _uM({
              class: "popup-list",
              "scroll-y": "true"
            }), [
              _ctx.moduleOptions.length === 0
                ? _cE("view", _uM({
                    key: 0,
                    class: "popup-loading"
                  }), [
                    _cE("text", _uM({ class: "loading-text" }), "加载中...")
                  ])
                : _ctx.filteredModuleOptions.length === 0
                  ? _cE("view", _uM({
                      key: 1,
                      class: "popup-empty"
                    }), [
                      _cE("text", _uM({ class: "empty-text" }), "暂无匹配的模块")
                    ])
                  : _cE(Fragment, _uM({ key: 2 }), RenderHelpers.renderList(_ctx.filteredModuleOptions, (item, index, __index, _cached): any => {
                      return _cE("view", _uM({
                        key: (item as OptionItem).id,
                        class: _nC(['popup-item', _ctx.selectedModuleIndex >= 0 && _ctx.moduleOptions[_ctx.selectedModuleIndex] != null && _ctx.moduleOptions[_ctx.selectedModuleIndex].id === (item as OptionItem).id ? 'popup-item-selected' : '']),
                        onClick: () => {_ctx.selectModuleFromFiltered(item)}
                      }), [
                        _cE("text", _uM({ class: "popup-item-text" }), _tD((item as OptionItem).name), 1 /* TEXT */),
                        isTrue(_ctx.selectedModuleIndex >= 0 && _ctx.moduleOptions[_ctx.selectedModuleIndex] != null && _ctx.moduleOptions[_ctx.selectedModuleIndex].id === (item as OptionItem).id)
                          ? _cE("text", _uM({
                              key: 0,
                              class: "popup-item-check"
                            }), "✓")
                          : _cC("v-if", true)
                      ], 10 /* CLASS, PROPS */, ["onClick"])
                    }), 128 /* KEYED_FRAGMENT */)
            ])
          ], 8 /* PROPS */, ["onClick"])
        ], 8 /* PROPS */, ["onClick"])
      : _cC("v-if", true),
    isTrue(_ctx.showResponsibilityPopup)
      ? _cE("view", _uM({
          key: 3,
          class: "popup-overlay",
          onClick: _ctx.hideResponsibilityPicker
        }), [
          _cE("view", _uM({
            class: "popup-content",
            onClick: withModifiers(() => {}, ["stop"])
          }), [
            _cE("view", _uM({ class: "popup-header" }), [
              _cE("text", _uM({ class: "popup-title" }), "选择责任"),
              _cE("text", _uM({
                class: "popup-close",
                onClick: _ctx.hideResponsibilityPicker
              }), "×", 8 /* PROPS */, ["onClick"])
            ]),
            _cE("view", _uM({ class: "popup-search" }), [
              _cE("input", _uM({
                class: "search-input",
                modelValue: _ctx.responsibilitySearchKeyword,
                onInput: ($event: UniInputEvent) => {(_ctx.responsibilitySearchKeyword) = $event.detail.value},
                placeholder: "搜索责任部门..."
              }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
            ]),
            _cE("scroll-view", _uM({
              class: "popup-list",
              "scroll-y": "true"
            }), [
              _ctx.responsibilityOptions.length === 0
                ? _cE("view", _uM({
                    key: 0,
                    class: "popup-loading"
                  }), [
                    _cE("text", _uM({ class: "loading-text" }), "加载中...")
                  ])
                : _ctx.filteredResponsibilityOptions.length === 0
                  ? _cE("view", _uM({
                      key: 1,
                      class: "popup-empty"
                    }), [
                      _cE("text", _uM({ class: "empty-text" }), "暂无匹配的责任部门")
                    ])
                  : _cE(Fragment, _uM({ key: 2 }), RenderHelpers.renderList(_ctx.filteredResponsibilityOptions, (item, index, __index, _cached): any => {
                      return _cE("view", _uM({
                        key: (item as OptionItem).id,
                        class: _nC(['popup-item', _ctx.selectedResponsibilityIndex >= 0 && _ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex] != null && _ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex].id === (item as OptionItem).id ? 'popup-item-selected' : '']),
                        onClick: () => {_ctx.selectResponsibilityFromFiltered(item)}
                      }), [
                        _cE("text", _uM({ class: "popup-item-text" }), _tD((item as OptionItem).name), 1 /* TEXT */),
                        isTrue(_ctx.selectedResponsibilityIndex >= 0 && _ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex] != null && _ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex].id === (item as OptionItem).id)
                          ? _cE("text", _uM({
                              key: 0,
                              class: "popup-item-check"
                            }), "✓")
                          : _cC("v-if", true)
                      ], 10 /* CLASS, PROPS */, ["onClick"])
                    }), 128 /* KEYED_FRAGMENT */)
            ])
          ], 8 /* PROPS */, ["onClick"])
        ], 8 /* PROPS */, ["onClick"])
      : _cC("v-if", true),
    isTrue(_ctx.showIssuePopup)
      ? _cE("view", _uM({
          key: 4,
          class: "popup-overlay",
          onClick: _ctx.hideIssuePicker
        }), [
          _cE("view", _uM({
            class: "popup-content",
            onClick: withModifiers(() => {}, ["stop"])
          }), [
            _cE("view", _uM({ class: "popup-header" }), [
              _cE("text", _uM({ class: "popup-title" }), "选择问题"),
              _cE("text", _uM({
                class: "popup-close",
                onClick: _ctx.hideIssuePicker
              }), "×", 8 /* PROPS */, ["onClick"])
            ]),
            _cE("view", _uM({ class: "popup-search" }), [
              _cE("input", _uM({
                class: "search-input",
                modelValue: _ctx.issueSearchKeyword,
                onInput: ($event: UniInputEvent) => {(_ctx.issueSearchKeyword) = $event.detail.value},
                placeholder: "搜索问题..."
              }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
            ]),
            _cE("scroll-view", _uM({
              class: "popup-list",
              "scroll-y": "true"
            }), [
              _ctx.issueOptions.length === 0
                ? _cE("view", _uM({
                    key: 0,
                    class: "popup-loading"
                  }), [
                    _cE("text", _uM({ class: "loading-text" }), "加载中...")
                  ])
                : _ctx.filteredIssueOptions.length === 0
                  ? _cE("view", _uM({
                      key: 1,
                      class: "popup-empty"
                    }), [
                      _cE("text", _uM({ class: "empty-text" }), "暂无匹配的问题")
                    ])
                  : _cE(Fragment, _uM({ key: 2 }), RenderHelpers.renderList(_ctx.filteredIssueOptions, (item, index, __index, _cached): any => {
                      return _cE("view", _uM({
                        key: (item as OptionItem).id,
                        class: _nC(['popup-item', _ctx.selectedIssueIndex !== -1 && _ctx.issueOptions[_ctx.selectedIssueIndex] != null && _ctx.issueOptions[_ctx.selectedIssueIndex].id === (item as OptionItem).id ? 'popup-item-selected' : '']),
                        onClick: () => {_ctx.selectIssueFromFiltered(item)}
                      }), [
                        _cE("text", _uM({ class: "popup-item-text" }), _tD((item as OptionItem).name), 1 /* TEXT */),
                        isTrue(_ctx.selectedIssueIndex !== -1 && _ctx.issueOptions[_ctx.selectedIssueIndex] != null && _ctx.issueOptions[_ctx.selectedIssueIndex].id === (item as OptionItem).id)
                          ? _cE("text", _uM({
                              key: 0,
                              class: "popup-item-check"
                            }), "✓")
                          : _cC("v-if", true)
                      ], 10 /* CLASS, PROPS */, ["onClick"])
                    }), 128 /* KEYED_FRAGMENT */)
            ])
          ], 8 /* PROPS */, ["onClick"])
        ], 8 /* PROPS */, ["onClick"])
      : _cC("v-if", true),
    isTrue(_ctx.showQualityPopup)
      ? _cE("view", _uM({
          key: 5,
          class: "popup-overlay",
          onClick: _ctx.hideQualityPicker
        }), [
          _cE("view", _uM({
            class: "popup-content",
            onClick: withModifiers(() => {}, ["stop"])
          }), [
            _cE("view", _uM({ class: "popup-header" }), [
              _cE("text", _uM({ class: "popup-title" }), "选择性质界定"),
              _cE("text", _uM({
                class: "popup-close",
                onClick: _ctx.hideQualityPicker
              }), "×", 8 /* PROPS */, ["onClick"])
            ]),
            _cE("view", _uM({ class: "popup-search" }), [
              _cE("input", _uM({
                class: "search-input",
                modelValue: _ctx.qualitySearchKeyword,
                onInput: ($event: UniInputEvent) => {(_ctx.qualitySearchKeyword) = $event.detail.value},
                placeholder: "搜索性质界定..."
              }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
            ]),
            _cE("scroll-view", _uM({
              class: "popup-list",
              "scroll-y": "true"
            }), [
              _ctx.qualityOptions.length === 0
                ? _cE("view", _uM({
                    key: 0,
                    class: "popup-loading"
                  }), [
                    _cE("text", _uM({ class: "loading-text" }), "加载中...")
                  ])
                : _ctx.filteredQualityOptions.length === 0
                  ? _cE("view", _uM({
                      key: 1,
                      class: "popup-empty"
                    }), [
                      _cE("text", _uM({ class: "empty-text" }), "暂无匹配的性质界定")
                    ])
                  : _cE(Fragment, _uM({ key: 2 }), RenderHelpers.renderList(_ctx.filteredQualityOptions, (item, index, __index, _cached): any => {
                      return _cE("view", _uM({
                        key: (item as OptionItem).id,
                        class: _nC(['popup-item', _ctx.selectedQualityIndex !== -1 && _ctx.qualityOptions[_ctx.selectedQualityIndex] != null && _ctx.qualityOptions[_ctx.selectedQualityIndex].id === (item as OptionItem).id ? 'popup-item-selected' : '']),
                        onClick: () => {_ctx.selectQualityFromFiltered(item)}
                      }), [
                        _cE("text", _uM({ class: "popup-item-text" }), _tD((item as OptionItem).name), 1 /* TEXT */),
                        isTrue(_ctx.selectedQualityIndex !== -1 && _ctx.qualityOptions[_ctx.selectedQualityIndex] != null && _ctx.qualityOptions[_ctx.selectedQualityIndex].id === (item as OptionItem).id)
                          ? _cE("text", _uM({
                              key: 0,
                              class: "popup-item-check"
                            }), "✓")
                          : _cC("v-if", true)
                      ], 10 /* CLASS, PROPS */, ["onClick"])
                    }), 128 /* KEYED_FRAGMENT */)
            ])
          ], 8 /* PROPS */, ["onClick"])
        ], 8 /* PROPS */, ["onClick"])
      : _cC("v-if", true)
  ], 12 /* STYLE, PROPS */, ["scroll-top"])
}
const GenPagesMonthlyTaskRecordStyles = [_uM([["container", _pS(_uM([["backgroundColor", "#f5f5f5"], ["position", "relative"], ["zIndex", 1], ["paddingBottom", "180rpx"]]))], ["header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["backgroundColor", "#ffffff"], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"]]))], ["header-left", _pS(_uM([["display", "flex"], ["alignItems", "center"], ["gap", "5px"]]))], ["back-icon", _pS(_uM([["fontSize", 24], ["color", "#007AFF"]]))], ["back-text", _pS(_uM([["fontSize", 16], ["color", "#007AFF"]]))], ["header-title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#333333"]]))], ["header-right", _pS(_uM([["display", "flex"], ["alignItems", "center"]]))], ["save-btn", _pS(_uM([["fontSize", 16], ["color", "#007AFF"]]))], ["task-info", _pS(_uM([["backgroundColor", "#ffffff"], ["marginTop", 10], ["marginRight", 20], ["marginBottom", 10], ["marginLeft", 20], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20]]))], ["info-item", _pS(_uM([["display", "flex"], ["paddingTop", 12], ["paddingRight", 0], ["paddingBottom", 12], ["paddingLeft", 0], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"], ["borderBottomWidth:last-child", "medium"], ["borderBottomStyle:last-child", "none"], ["borderBottomColor:last-child", "#000000"]]))], ["info-label", _pS(_uM([["fontSize", 14], ["color", "#666666"], ["width", 100], ["flexShrink", 0], ["textAlign", "left"], ["marginRight", 12]]))], ["info-value", _pS(_uM([["fontSize", 14], ["color", "#333333"], ["flex", 1], ["textAlign", "left"]]))], ["form-section", _pS(_uM([["backgroundColor", "#ffffff"], ["marginTop", 10], ["marginRight", 20], ["marginBottom", 10], ["marginLeft", 20], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20]]))], ["section-title", _pS(_uM([["display", "flex"], ["alignItems", "center"], ["marginBottom", 20]]))], ["title-text", _pS(_uM([["fontSize", 16], ["fontWeight", "bold"], ["color", "#333333"]]))], ["required-mark", _pS(_uM([["fontSize", 16], ["color", "#ff4757"], ["marginLeft", 4]]))], ["form-item", _pS(_uM([["marginBottom", 20], ["marginBottom:last-child", 0]]))], ["form-item-inline", _pS(_uM([["marginBottom", 20], ["display", "flex"], ["flexDirection", "column"], ["gap", "8px"], ["paddingTop", 0], ["paddingRight", 20], ["paddingBottom", 0], ["paddingLeft", 20]]))], ["form-label", _pS(_uM([["fontSize", 14], ["color", "#333333"], ["marginBottom", 8]]))], ["form-label-inline", _pS(_uM([["fontSize", 14], ["color", "#333333"], ["marginBottom", 0]]))], ["picker-wrapper", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["backgroundColor", "#ffffff"]]))], ["picker-wrapper-inline", _uM([["", _uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["backgroundColor", "#ffffff"], ["width", "100%"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["cursor", "pointer"], ["borderTopColor:hover", "#667eea"], ["borderRightColor:hover", "#667eea"], ["borderBottomColor:hover", "#667eea"], ["borderLeftColor:hover", "#667eea"], ["boxShadow:hover", "0 0 0 2px rgba(102, 126, 234, 0.1)"], ["transform:active", "scale(0.98)"]])], [".disabled", _uM([["backgroundColor", "#f5f5f5"], ["color", "#999999"], ["cursor", "not-allowed"], ["opacity", 0.6]])], [".disabled:hover", _uM([["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["boxShadow", "none"]])]])], ["text-input-inline", _pS(_uM([["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["fontSize", 14], ["color", "#333333"], ["backgroundColor", "#ffffff"], ["flex", 1]]))], ["bottom-save-container", _pS(_uM([["position", "fixed"], ["bottom", 0], ["left", 0], ["right", 0], ["paddingTop", "20rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "30rpx"], ["backgroundImage", "linear-gradient(to top, rgba(85, 170, 255, 1.0) 0%, rgba(85, 170, 255, 0.9) 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["backdropFilter", "blur(10rpx)"], ["borderTopWidth", "1rpx"], ["borderTopStyle", "solid"], ["borderTopColor", "rgba(240,240,240,0.8)"], ["zIndex", 999]]))], ["bottom-save-btn", _pS(_uM([["width", "100%"], ["height", "96rpx"], ["backgroundImage", "linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["borderTopLeftRadius", "48rpx"], ["borderTopRightRadius", "48rpx"], ["borderBottomRightRadius", "48rpx"], ["borderBottomLeftRadius", "48rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["boxShadow", "0 12rpx 32rpx rgba(79, 172, 254, 0.4)"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "cubic-bezier(0.4,0,0.2,1)"], ["position", "relative"], ["overflow", "hidden"], ["content::before", "''"], ["position::before", "absolute"], ["top::before", 0], ["left::before", "-100%"], ["width::before", "100%"], ["height::before", "100%"], ["backgroundImage::before", "linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent)"], ["backgroundColor::before", "rgba(0,0,0,0)"], ["transitionProperty::before", "left"], ["transitionDuration::before", "0.6s"], ["transform:active", "translateY(2rpx) scale(0.98)"], ["boxShadow:active", "0 8rpx 24rpx rgba(79, 172, 254, 0.3)"], ["left:active::before", "100%"]]))], ["bottom-save-text", _pS(_uM([["color", "#000000"], ["fontSize", "34rpx"], ["fontWeight", "700"], ["letterSpacing", "3rpx"], ["position", "relative"], ["zIndex", 1]]))], ["picker-text", _uM([["", _uM([["fontSize", 14], ["color", "#333333"]])], [".placeholder", _uM([["color", "#999999"]])]])], ["picker-arrow", _pS(_uM([["fontSize", 16], ["color", "#cccccc"]]))], ["text-input", _pS(_uM([["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["fontSize", 14], ["color", "#333333"], ["backgroundColor", "#ffffff"]]))], ["textarea-input", _pS(_uM([["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["fontSize", 14], ["color", "#333333"], ["backgroundColor", "#ffffff"], ["minHeight", 80], ["resize", "none"]]))], ["char-count", _pS(_uM([["fontSize", 12], ["color", "#999999"], ["textAlign", "right"], ["marginTop", 5]]))], ["photo-upload", _pS(_uM([["display", "flex"], ["flexWrap", "wrap"], ["gap", "10px"], ["marginBottom", 5]]))], ["photo-item", _pS(_uM([["position", "relative"], ["width", 80], ["height", 80]]))], ["photo-preview", _pS(_uM([["width", "100%"], ["height", "100%"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["objectFit", "cover"]]))], ["photo-delete", _pS(_uM([["position", "absolute"], ["top", -5], ["right", -5], ["width", 20], ["height", 20], ["backgroundColor", "#ff4757"], ["borderTopLeftRadius", 10], ["borderTopRightRadius", 10], ["borderBottomRightRadius", 10], ["borderBottomLeftRadius", 10], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["delete-icon", _pS(_uM([["fontSize", 12], ["color", "#ffffff"], ["fontWeight", "bold"]]))], ["photo-add", _pS(_uM([["width", 80], ["height", 80], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "dashed"], ["borderRightStyle", "dashed"], ["borderBottomStyle", "dashed"], ["borderLeftStyle", "dashed"], ["borderTopColor", "#cccccc"], ["borderRightColor", "#cccccc"], ["borderBottomColor", "#cccccc"], ["borderLeftColor", "#cccccc"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"], ["backgroundColor", "#f8f9fa"]]))], ["add-icon", _pS(_uM([["fontSize", 24], ["color", "#999999"], ["marginBottom", 4]]))], ["add-text", _pS(_uM([["fontSize", 12], ["color", "#999999"]]))], ["photo-tip", _pS(_uM([["fontSize", 12], ["color", "#999999"]]))], ["popup-overlay", _pS(_uM([["!position", "fixed"], ["!top", 0], ["!left", 0], ["!width", "100%"], ["!height", "100%"], ["backgroundColor", "rgba(0,0,0,0.5)"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["!zIndex", 9999], ["animation", "fadeIn 0.3s ease"], ["pointerEvents", "auto"]]))], ["picker-modal", _pS(_uM([["!position", "fixed"], ["!top", 0], ["!left", 0], ["!width", "100%"], ["!height", "100%"], ["backgroundColor", "rgba(0,0,0,0.5)"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["!zIndex", 9999], ["animation", "fadeIn 0.3s ease"], ["pointerEvents", "auto"]]))], ["popup-content", _pS(_uM([["!width", "90%"], ["maxWidth", 500], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["overflow", "hidden"], ["animation", "scaleIn 0.3s ease"], ["boxShadow", "0 8px 32px rgba(0, 0, 0, 0.2)"], ["position", "relative"], ["zIndex", 10000]]))], ["picker-content", _pS(_uM([["!width", "90%"], ["maxWidth", 500], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["overflow", "hidden"], ["animation", "scaleIn 0.3s ease"], ["boxShadow", "0 8px 32px rgba(0, 0, 0, 0.2)"], ["position", "relative"], ["zIndex", 10000]]))], ["popup-header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#e9ecef"], ["backgroundImage", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["position", "relative"]]))], ["picker-header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#e9ecef"], ["backgroundImage", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["position", "relative"], ["content::after", "''"], ["position::after", "absolute"], ["bottom::after", 0], ["left::after", "50%"], ["transform::after", "translateX(-50%)"], ["width::after", 30], ["height::after", 3], ["backgroundColor::after", "rgba(255,255,255,0.3)"], ["borderTopLeftRadius::after", 2], ["borderTopRightRadius::after", 2], ["borderBottomRightRadius::after", 2], ["borderBottomLeftRadius::after", 2]]))], ["picker-cancel", _pS(_uM([["fontSize", 16], ["color", "rgba(255,255,255,0.8)"]]))], ["picker-title", _pS(_uM([["fontSize", 16], ["color", "#ffffff"], ["letterSpacing", 1]]))], ["picker-confirm", _pS(_uM([["fontSize", 16], ["color", "#ffffff"], ["backgroundColor", "rgba(255,255,255,0.2)"], ["paddingTop", 6], ["paddingRight", 12], ["paddingBottom", 6], ["paddingLeft", 12], ["borderTopLeftRadius", 15], ["borderTopRightRadius", 15], ["borderBottomRightRadius", 15], ["borderBottomLeftRadius", 15], ["transitionProperty", "all"], ["transitionDuration", "0.2s"], ["transitionTimingFunction", "ease"], ["backgroundColor:active", "rgba(255,255,255,0.3)"], ["transform:active", "scale(0.95)"]]))], ["popup-title", _pS(_uM([["fontSize", 16], ["color", "#ffffff"], ["letterSpacing", 1]]))], ["popup-close", _pS(_uM([["fontSize", 24], ["color", "rgba(255,255,255,0.8)"], ["width", 30], ["height", 30], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["borderTopLeftRadius", 15], ["borderTopRightRadius", 15], ["borderBottomRightRadius", 15], ["borderBottomLeftRadius", 15], ["transitionProperty", "all"], ["transitionDuration", "0.2s"], ["transitionTimingFunction", "ease"], ["backgroundColor:active", "rgba(255,255,255,0.2)"], ["transform:active", "scale(0.9)"]]))], ["popup-search", _pS(_uM([["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#e9ecef"], ["backgroundColor", "#f8f9fa"]]))], ["search-input", _pS(_uM([["width", "100%"], ["height", 40], ["paddingTop", 0], ["paddingRight", 15], ["paddingBottom", 0], ["paddingLeft", 15], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dddddd"], ["borderRightColor", "#dddddd"], ["borderBottomColor", "#dddddd"], ["borderLeftColor", "#dddddd"], ["borderTopLeftRadius", 20], ["borderTopRightRadius", 20], ["borderBottomRightRadius", 20], ["borderBottomLeftRadius", 20], ["fontSize", 14], ["backgroundColor", "#ffffff"], ["outline", "none"], ["transitionProperty", "all"], ["transitionDuration", "0.2s"], ["transitionTimingFunction", "ease"], ["borderTopColor:focus", "#667eea"], ["borderRightColor:focus", "#667eea"], ["borderBottomColor:focus", "#667eea"], ["borderLeftColor:focus", "#667eea"], ["boxShadow:focus", "0 0 0 2px rgba(102, 126, 234, 0.2)"]]))], ["popup-list", _pS(_uM([["maxHeight", 350], ["overflowY", "auto"]]))], ["popup-empty", _pS(_uM([["paddingTop", 40], ["paddingRight", 20], ["paddingBottom", 40], ["paddingLeft", 20], ["textAlign", "center"]]))], ["empty-text", _pS(_uM([["fontSize", 14], ["color", "#999999"]]))], ["picker-list", _pS(_uM([["maxHeight", 300], ["overflowY", "auto"]]))], ["picker-item", _uM([["", _uM([["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"], ["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "cubic-bezier(0.4,0,0.2,1)"], ["cursor", "pointer"], ["position", "relative"], ["backgroundColor:hover", "#f0f8ff"], ["color:hover", "#007AFF"], ["backgroundColor:active", "#e9ecef"], ["transform:active", "scale(0.98)"], ["borderBottomWidth:last-child", "medium"], ["borderBottomStyle:last-child", "none"], ["borderBottomColor:last-child", "#000000"], ["height", 60], ["fontSize", 16], ["color", "#666666"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["marginTop", 4], ["marginRight", 8], ["marginBottom", 4], ["marginLeft", 8], ["zIndex", 2], ["opacity", 0.6], ["backgroundColor", "rgba(0,0,0,0.02)"]])], [".selected", _uM([["backgroundColor", "rgba(0,0,0,0)"], ["backgroundImage", "linear-gradient(135deg, #007AFF 0%, #0056CC 100%)"], ["color", "#ffffff"], ["fontWeight", "700"], ["boxShadow", "0 4px 12px rgba(0, 122, 255, 0.4), inset 0 1px 0 rgba(255, 255, 255, 0.2)"], ["transform", "scale(1.05)"], ["zIndex", 3], ["opacity", 1], ["fontSize", 18], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#ffffff"], ["borderRightColor", "#ffffff"], ["borderBottomColor", "#ffffff"], ["borderLeftColor", "#ffffff"], ["textShadow", "0 1px 2px rgba(0, 0, 0, 0.3)"]])]])], ["popup-item", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"], ["transitionProperty", "all"], ["transitionDuration", "0.2s"], ["transitionTimingFunction", "ease"], ["cursor", "pointer"], ["backgroundColor:hover", "#f8f9fa"]]))], ["popup-item-selected", _pS(_uM([["backgroundColor", "#e8f2ff"], ["borderLeftWidth", 4], ["borderLeftStyle", "solid"], ["borderLeftColor", "#667eea"], ["paddingLeft", 16], ["backgroundColor:hover", "#ddeeff"]]))], ["popup-item-text", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["fontWeight", "400"]]))], ["picker-item-text", _pS(_uM([["fontSize", 16], ["color", "#333333"]]))], ["level-item", _pS(_uM([["flex", 1]]))], ["level-name", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["marginBottom", 4]]))], ["level-desc", _pS(_uM([["fontSize", 12], ["color", "#999999"]]))], ["popup-item-check", _pS(_uM([["fontSize", 18], ["color", "#667eea"], ["fontWeight", "bold"], ["animation", "checkIn 0.3s ease"]]))], ["popup-loading", _pS(_uM([["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"], ["paddingTop", 40], ["paddingRight", 20], ["paddingBottom", 40], ["paddingLeft", 20]]))], ["loading-text", _pS(_uM([["fontSize", 14], ["color", "#999999"]]))], ["form-label-left", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["marginBottom", 10], ["fontWeight", "400"], ["textAlign", "left"]]))], ["custom-picker-full", _uM([["", _uM([["width", "100%"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dddddd"], ["borderRightColor", "#dddddd"], ["borderBottomColor", "#dddddd"], ["borderLeftColor", "#dddddd"], ["borderTopLeftRadius", "8rpx"], ["borderTopRightRadius", "8rpx"], ["borderBottomRightRadius", "8rpx"], ["borderBottomLeftRadius", "8rpx"], ["backgroundColor", "#ffffff"], ["minHeight", "80rpx"], ["display", "flex"], ["alignItems", "center"]])], [".disabled", _uM([["backgroundImage", "none"], ["backgroundColor", "#f5f5f5"], ["borderTopColor", "#dddddd"], ["borderRightColor", "#dddddd"], ["borderBottomColor", "#dddddd"], ["borderLeftColor", "#dddddd"], ["opacity", 0.6]])]])], ["picker-container", _pS(_uM([["width", "100%"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dddddd"], ["borderRightColor", "#dddddd"], ["borderBottomColor", "#dddddd"], ["borderLeftColor", "#dddddd"], ["borderTopLeftRadius", "8rpx"], ["borderTopRightRadius", "8rpx"], ["borderBottomRightRadius", "8rpx"], ["borderBottomLeftRadius", "8rpx"], ["backgroundColor", "#ffffff"], ["minHeight", "80rpx"], ["display", "flex"], ["alignItems", "center"]]))], ["picker-text-full", _uM([["", _uM([["fontSize", "32rpx"], ["color", "#333333"], ["width", "100%"], ["textAlign", "left"]])], [".placeholder", _uM([["color", "#999999"]])]])], ["date-picker-modal", _pS(_uM([["position", "fixed"], ["top", 0], ["left", 0], ["right", 0], ["bottom", 0], ["backgroundColor", "rgba(0,0,0,0.5)"], ["display", "flex"], ["alignItems", "flex-end"], ["zIndex", 1000]]))], ["date-picker-content", _pS(_uM([["width", "100%"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 16], ["borderTopRightRadius", 16], ["borderBottomRightRadius", 0], ["borderBottomLeftRadius", 0], ["paddingBottom", "env(safe-area-inset-bottom)"]]))], ["date-picker-header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 16], ["paddingRight", 20], ["paddingBottom", 16], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"]]))], ["date-picker-cancel", _pS(_uM([["fontSize", 16], ["color", "#999999"]]))], ["date-picker-title", _pS(_uM([["fontSize", 18], ["color", "#333333"]]))], ["date-picker-confirm", _pS(_uM([["fontSize", 16], ["color", "#007AFF"]]))], ["date-picker-view", _pS(_uM([["height", 320], ["paddingTop", 0], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["position", "relative"]]))], ["picker-item-spacer", _pS(_uM([["pointerEvents", "none"], ["opacity", 0], ["!backgroundImage", "none"], ["!backgroundColor", "rgba(0,0,0,0)"]]))], ["form-input", _pS(_uM([["flex", 1], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["backgroundImage", "none"], ["backgroundColor", "#f8f9fa"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["fontSize", 16], ["color", "#333333"], ["boxSizing", "border-box"], ["width", "100%"]]))], ["@FONT-FACE", _uM([["0", _uM([])], ["1", _uM([])], ["2", _uM([])], ["3", _uM([])]])], ["@TRANSITION", _uM([["picker-wrapper-inline", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "ease"]])], ["bottom-save-btn", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "cubic-bezier(0.4,0,0.2,1)"], ["property::before", "left"], ["duration::before", "0.6s"]])], ["picker-confirm", _uM([["property", "all"], ["duration", "0.2s"], ["timingFunction", "ease"]])], ["popup-close", _uM([["property", "all"], ["duration", "0.2s"], ["timingFunction", "ease"]])], ["search-input", _uM([["property", "all"], ["duration", "0.2s"], ["timingFunction", "ease"]])], ["picker-item", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "cubic-bezier(0.4,0,0.2,1)"]])], ["popup-item", _uM([["property", "all"], ["duration", "0.2s"], ["timingFunction", "ease"]])]])]])]
