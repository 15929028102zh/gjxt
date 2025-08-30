
	import { getApiUrl, apiConfig } from '@/config/api.uts'
	// import CalendarPicker from '@/components/CalendarPicker.uvue'
	
	type NatureOption = { __$originalPosition?: UTSSourceMapPosition<"NatureOption", "pages/record/add.uvue", 311, 7>;
		id: string
		name: string
	}
	
	type AssessmentOption = { __$originalPosition?: UTSSourceMapPosition<"AssessmentOption", "pages/record/add.uvue", 316, 7>;
		id: string
		label: string
		value: string
	}
	
	type ProfessionOption = { __$originalPosition?: UTSSourceMapPosition<"ProfessionOption", "pages/record/add.uvue", 322, 7>;
		id: string
		name: string
	}
	
	type DepartmentOption = { __$originalPosition?: UTSSourceMapPosition<"DepartmentOption", "pages/record/add.uvue", 327, 7>;
		id: string
		name: string
	}
	
	type InspectorOption = { __$originalPosition?: UTSSourceMapPosition<"InspectorOption", "pages/record/add.uvue", 332, 7>;
		id: string
		name: string
	}
	

	type RecordFormData = { __$originalPosition?: UTSSourceMapPosition<"RecordFormData", "pages/record/add.uvue", 338, 7>;
		responsibleUnit: string
		responsiblePerson: string
		natureId: string
		scoreId: string
		assessmentAmount: string
		safetyScore: string
		professionId: string
		problemDescription: string
		location: string
		inspector: string
		groupLeader: string
		deputyLeader: string
		departmentId: string
		rectificationDate: string
	}
	
	const __sfc__ = defineComponent({
		components: {
			// CalendarPicker
		},
		data(): {
			formData: RecordFormData
			natureOptions: NatureOption[]
			assessmentOptions: AssessmentOption[]
			professionOptions: ProfessionOption[]
			departmentOptions: DepartmentOption[]
			selectedNatureIndex: number
			selectedAssessmentIndex: number
			selectedProfessionIndex: number
			selectedDepartmentIndex: number
			selectedInspectorIndex: number
			showNaturePopup: boolean
			showAssessmentPopup: boolean
			showProfessionPopup: boolean
			showInspectorPopup: boolean
			showDepartmentPopup: boolean
			showDatePopup: boolean
			tempDate: string
			imageList: string[]
			submitting: boolean
			scrollTop: number
			containerHeight: number
			inspectorOptions: InspectorOption[]
			datePickerVisible: boolean
			pickerValue: number[]
			years: number[]
			months: number[]
			days: number[]
			selectedYear: number
			selectedMonth: number
			selectedDay: number

		} {
			// 计算默认整改时间（当前日期往后推三天）
			const defaultDate = new Date()
			defaultDate.setDate(defaultDate.getDate() + 3)
			const defaultDateStr = `${defaultDate.getFullYear()}-${(defaultDate.getMonth() + 1).toString().padStart(2, '0')}-${defaultDate.getDate().toString().padStart(2, '0')}`
			
			return {
				formData: {
				responsibleUnit: '',
				responsiblePerson: '',
				natureId: '',
				scoreId: '',
				assessmentAmount: '',
				safetyScore: '',
				professionId: '',
				problemDescription: '',
				location: '',
				inspector: '',
				groupLeader: '',
				deputyLeader: '',
				departmentId: '',
				rectificationDate: defaultDateStr
			} as RecordFormData,
			natureOptions: [] as NatureOption[],
			assessmentOptions: [
				{id: '1', label: '提出警告', value: '提出警告'},
				{id: '2', label: '-50', value: '-50'},
				{id: '3', label: '-100', value: '-100'},
				{id: '4', label: '-200', value: '-200'},
				{id: '5', label: '-300', value: '-300'}
			] as AssessmentOption[],
			professionOptions: [] as ProfessionOption[],
			departmentOptions: [] as DepartmentOption[],
			inspectorOptions: [] as InspectorOption[],
			selectedNatureIndex: -1,
			selectedAssessmentIndex: -1,
			selectedProfessionIndex: -1,
			selectedDepartmentIndex: -1,
			selectedInspectorIndex: -1,
			showNaturePopup: false,
			showAssessmentPopup: false,
			showProfessionPopup: false,
			showInspectorPopup: false,
			showDepartmentPopup: false,
			showDatePopup: false,
			tempDate: '',
			imageList: [] as string[],
			choosingImage: false,
			submitting: false,
			scrollTop: 0,
			containerHeight: 0,
			datePickerVisible: false,
			pickerValue: [0, 0, 0],
			years: [] as number[],
			months: [] as number[],
			days: [] as number[],
			selectedYear: (() => {
				const tempDate = new Date()
				tempDate.setDate(tempDate.getDate() + 3)
				return tempDate.getFullYear()
			})() as number,
			selectedMonth: (() => {
				const tempDate = new Date()
				tempDate.setDate(tempDate.getDate() + 3)
				return tempDate.getMonth() + 1
			})() as number,
			selectedDay: (() => {
				const tempDate = new Date()
				tempDate.setDate(tempDate.getDate() + 3)
				return tempDate.getDate()
			})() as number,
			// 防抖定时器
			pickerDebounceTimer: null as number | null
			}
		},
		onLoad(options: UTSJSONObject) {
			this.checkLogin()
			this.loadNatureOptions()
			this.loadProfessionOptions()
			this.loadDepartments()
			this.loadInspectorOptions()
			this.initDateArrays()
			// setCurrentUserAsInspector 将在 loadInspectorOptions 成功后调用
		},
		onReady() {
			this.initContainerHeight()
		},
		methods: {
			checkLogin() {
				const token = uni.getStorageSync('token')
				if (token == null || token == '') {
					uni.reLaunch({
						url: '/pages/login/login'
					})
				}
			},
			
			// 初始化容器高度
			initContainerHeight() {
				uni.getSystemInfo({
					success: (res) => {
						this.containerHeight = res.windowHeight
					}
				})
			},
			
			loadNatureOptions() {
				// 从API获取性质界定数据
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
						// 使用dictValue作为显示值，id作为实际值
						const dataArray = data.getArray('data')
						if (dataArray != null) {
							this.natureOptions = []
							for (let i = 0; i < dataArray.length; i++) {
								const item = dataArray[i] as UTSJSONObject
								const id = item.getString('id') ?? ''
								const dictValue = item.getString('dictValue') ?? ''
								const dictLabel = item.getString('dictLabel') ?? ''
								const name = dictValue !== '' ? dictValue : dictLabel
								this.natureOptions.push({ id: id, name: name })
							}
						}
						console.log('性质界定数据加载成功:', this.natureOptions, " at pages/record/add.uvue:525")
						} else {
						const message = data.getString('message') ?? '未知错误'
						console.error('获取性质界定数据失败:', message, " at pages/record/add.uvue:528")
							// 使用默认数据作为备选
							this.natureOptions = [
								{ id: '1', name: '一般问题' },
								{ id: '2', name: '较大问题' },
								{ id: '3', name: '严重问题' },
								{ id: '4', name: '重大问题' }
							]
						}
					},
					fail: (error) => {
						console.error('请求性质界定数据失败:', error, " at pages/record/add.uvue:539")
						// 使用默认数据作为备选
						this.natureOptions = [
							{ id: '1', name: '一般问题' },
							{ id: '2', name: '较大问题' },
							{ id: '3', name: '严重问题' },
							{ id: '4', name: '重大问题' }
						]
					}
				})
			},
		

		loadProfessionOptions() {
			// 从API获取专业数据
			const token = uni.getStorageSync('token')
			uni.request({
				url: getApiUrl('/dictionary/type/专业'),
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
						// 使用dictValue作为显示值，id作为实际值
						const dataArray = data.getArray('data')
						if (dataArray != null) {
							this.professionOptions = []
							for (let i = 0; i < dataArray.length; i++) {
								const item = dataArray[i] as UTSJSONObject
								const id = item.getString('id') ?? ''
								const dictValue = item.getString('dictValue') ?? ''
								const dictLabel = item.getString('dictLabel') ?? ''
								const name = dictValue !== '' ? dictValue : dictLabel
								this.professionOptions.push({ id: id, name: name })
							}
						}
						console.log('专业数据加载成功:', this.professionOptions, " at pages/record/add.uvue:580")
					} else {
						const message = data.getString('message') ?? '未知错误'
						console.error('获取专业数据失败:', message, " at pages/record/add.uvue:583")
						// 使用默认数据作为备选
						this.professionOptions = [
							{ id: '1', name: '机械' },
							{ id: '2', name: '电气' },
							{ id: '3', name: '仪表' },
							{ id: '4', name: '工艺' },
							{ id: '5', name: '安全' },
							{ id: '6', name: '环保' }
						]
					}
				},
				fail: (error) => {
					console.error('请求专业数据失败:', error, " at pages/record/add.uvue:596")
					// 使用默认数据作为备选
					this.professionOptions = [
						{ id: '1', name: '机械' },
						{ id: '2', name: '电气' },
						{ id: '3', name: '仪表' },
						{ id: '4', name: '工艺' },
						{ id: '5', name: '安全' },
						{ id: '6', name: '环保' }
					]
				}
			})
		},
		
		// 加载检查人选项
		loadInspectorOptions() {
			const token = uni.getStorageSync('token')
			if (token == null || token == '') {
				console.error('未找到token', " at pages/record/add.uvue:614")
				return
			}
			
			// 获取当前用户信息以确定部门
			const userInfoStr = uni.getStorageSync('userInfo') as string
			if (userInfoStr == null || userInfoStr == '') {
				console.error('未找到用户信息', " at pages/record/add.uvue:621")
				return
			}
			
			const userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/add.uvue:625") as UTSJSONObject
			
			// 获取当前用户的部门信息
			console.log('当前用户信息:', userInfo, " at pages/record/add.uvue:628")
			let deptId = userInfo.getString('deptId') ?? userInfo.getString('dept_id') ?? ''
			if (deptId === '') {
				// 如果字符串获取失败，尝试数字类型
				const deptIdNum = userInfo.getNumber('deptId') ?? userInfo.getNumber('dept_id')
				if (deptIdNum != null) {
					deptId = deptIdNum.toString()
				}
			}
			console.log('用户部门ID:', deptId, " at pages/record/add.uvue:637")
			
			if (deptId == '') {
				console.warn('当前用户没有部门信息，将获取所有用户', " at pages/record/add.uvue:640")
				// 如果没有部门信息，调用获取所有用户的接口
				uni.request({
					url: getApiUrl('/user/page?current=1&size=100'),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						const response = res.data as UTSJSONObject
						if (res.statusCode == 200 && (response.getNumber('code') ?? 0) == 200) {
							const data = response.getJSON('data') as UTSJSONObject
							const records = data.getArray('records')
							if (records != null) {
								this.inspectorOptions = []
								for (let i = 0; i < records.length; i++) {
									const item = records[i] as UTSJSONObject
									const id = item.getString('id') ?? ''
									const realName = item.getString('realName') ?? ''
									if (id != '' && realName != '') {
										this.inspectorOptions.push({ id: id, name: realName } as InspectorOption)
									}
								}
							}
						console.log('所有用户数据加载成功:', this.inspectorOptions, " at pages/record/add.uvue:664")
						// 数据加载完成后设置当前用户为默认检查人
						this.setCurrentUserAsInspector()
					} else {
						const message = response.getString('message') ?? '未知错误'
						console.error('获取用户列表失败:', message, " at pages/record/add.uvue:669")
						// 使用默认数据作为备选
						this.inspectorOptions = [
							{ id: '1', name: '张三' } as InspectorOption,
							{ id: '2', name: '李四' } as InspectorOption,
							{ id: '3', name: '王五' } as InspectorOption,
							{ id: '4', name: '赵六' } as InspectorOption
						]
						// 使用默认数据后也设置当前用户
						this.setCurrentUserAsInspector()
					}
					},
					fail: (error) => {
					console.error('请求用户列表失败:', error, " at pages/record/add.uvue:682")
					// 使用默认数据作为备选
					this.inspectorOptions = [
						{ id: '1', name: '张三' } as InspectorOption,
						{ id: '2', name: '李四' } as InspectorOption,
						{ id: '3', name: '王五' } as InspectorOption,
						{ id: '4', name: '赵六' } as InspectorOption
					]
					// 使用默认数据后也设置当前用户
					this.setCurrentUserAsInspector()
				}
				})
				return
			}
			
			// 从后台API按部门ID获取用户列表
			uni.request({
				url: getApiUrl(`/user/by-department?deptId=${UTSAndroid.consoleDebugError(encodeURIComponent(deptId), " at pages/record/add.uvue:699")}`),
				method: 'GET',
				header: {
					'Authorization': `Bearer ${token}`
				},
				success: (res) => {
					const response = res.data as UTSJSONObject
					if (res.statusCode == 200 && (response.getNumber('code') ?? 0) == 200) {
						const data = response.getArray('data')
						if (data != null) {
							this.inspectorOptions = []
							for (let i = 0; i < data.length; i++) {
								const item = data[i] as UTSJSONObject
								const id = item.getString('id') ?? ''
								const realName = item.getString('realName') ?? ''
								if (id != '' && realName != '') {
									this.inspectorOptions.push({ id: id, name: realName } as InspectorOption)
								}
							}
						}
					console.log(`部门ID${deptId}的检查人数据加载成功:`, this.inspectorOptions, " at pages/record/add.uvue:719")
					// 数据加载完成后设置当前用户为默认检查人
					this.setCurrentUserAsInspector()
				} else {
					const message = response.getString('message') ?? '未知错误'
					console.error('获取部门用户列表失败:', message, " at pages/record/add.uvue:724")
					// 使用默认数据作为备选
					this.inspectorOptions = [
						{ id: '1', name: '张三' } as InspectorOption,
						{ id: '2', name: '李四' } as InspectorOption,
						{ id: '3', name: '王五' } as InspectorOption,
						{ id: '4', name: '赵六' } as InspectorOption
					]
					// 使用默认数据后也设置当前用户
					this.setCurrentUserAsInspector()
				}
				},
				fail: (error) => {
				console.error('请求部门用户列表失败:', error, " at pages/record/add.uvue:737")
				// 使用默认数据作为备选
				this.inspectorOptions = [
					{ id: '1', name: '张三' } as InspectorOption,
					{ id: '2', name: '李四' } as InspectorOption,
					{ id: '3', name: '王五' } as InspectorOption,
					{ id: '4', name: '赵六' } as InspectorOption
				]
				// 使用默认数据后也设置当前用户
				this.setCurrentUserAsInspector()
			}
			})
		},
		
		// 设置当前登录用户为默认检查人
		setCurrentUserAsInspector() {
			const userInfoStr = uni.getStorageSync('userInfo') as string
			if (userInfoStr == null || userInfoStr == '') {
				console.error('未找到用户信息', " at pages/record/add.uvue:755")
				return
			}
			
			const userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/add.uvue:759") as UTSJSONObject
			console.log('当前用户信息:', userInfo, " at pages/record/add.uvue:760")
			
			// 获取当前用户ID和姓名，尝试多个可能的字段
			const currentUserId = userInfo.getString('id') ?? ''
			const currentUserName = userInfo.getString('realName') ?? userInfo.getString('name') ?? userInfo.getString('username') ?? ''
			
			console.log('当前用户ID:', currentUserId, '姓名:', currentUserName, " at pages/record/add.uvue:766")
			console.log('检查人选项列表:', this.inspectorOptions, " at pages/record/add.uvue:767")
			
			if (currentUserId == '' && currentUserName == '') {
				console.error('无法获取当前用户信息', " at pages/record/add.uvue:770")
				return
			}
			
			// 在检查人选项中查找当前用户，优先按ID匹配，其次按姓名匹配
			let currentUserIndex = -1
			
			if (currentUserId != '') {
				// 使用宽松比较，避免类型转换问题
				currentUserIndex = this.inspectorOptions.findIndex(option => option.id == currentUserId)
				console.log('按ID查找结果:', currentUserIndex, '查找ID:', currentUserId, '选项IDs:', this.inspectorOptions.map(opt => opt.id), " at pages/record/add.uvue:780")
			}
			
			if (currentUserIndex === -1 && currentUserName != '') {
				currentUserIndex = this.inspectorOptions.findIndex(option => option.name == currentUserName)
				console.log('按姓名查找结果:', currentUserIndex, '查找姓名:', currentUserName, '选项姓名:', this.inspectorOptions.map(opt => opt.name), " at pages/record/add.uvue:785")
			}
			
			if (currentUserIndex !== -1) {
				this.selectedInspectorIndex = currentUserIndex
				this.formData.inspector = this.inspectorOptions[currentUserIndex].name
				console.log('已设置当前用户为默认检查人:', this.inspectorOptions[currentUserIndex].name, " at pages/record/add.uvue:791")
			} else {
				console.warn('在检查人列表中未找到当前用户，ID:', currentUserId, '姓名:', currentUserName, " at pages/record/add.uvue:793")
				console.log('当前检查人选项:', this.inspectorOptions, " at pages/record/add.uvue:794")
			}
		},
		
		loadDepartments() {
			// 从API获取部门数据
			console.log('调试：开始加载部门数据', " at pages/record/add.uvue:800")
			uni.showToast({ title: '开始加载部门数据', icon: 'none', duration: 1000 })
			const token = uni.getStorageSync('token')
			uni.request({
				url: getApiUrl('/dept/list'),
				method: 'GET',
				header: {
					'Authorization': `Bearer ${token}`,
					'Content-Type': 'application/json'
				},
				success: (res) => {
					const data = res.data as UTSJSONObject
					const code = data.getNumber('code')
					if (res.statusCode === 200 && code === 200) {
						const dataArray = data.getArray('data')
						if (dataArray != null) {
							this.departmentOptions = []
							for (let i = 0; i < dataArray.length; i++) {
								const item = dataArray[i] as UTSJSONObject
								const id = item.getString('id') ?? ''
								const deptName = item.getString('deptName') ?? ''
								if (id !== '' && deptName !== '') {
									this.departmentOptions.push({ id: id, name: deptName })
								}
							}
						}
						console.log('部门数据加载成功:', this.departmentOptions, " at pages/record/add.uvue:826")
						// 数据加载完成后设置当前用户的部门为默认选择
						this.setCurrentUserDepartment()
					} else {
						const message = data.getString('message') ?? '未知错误'
						console.error('获取部门数据失败:', message, " at pages/record/add.uvue:831")
						// 使用默认数据作为备选
						this.departmentOptions = [
							{ id: '1', name: '生产部' },
							{ id: '2', name: '安全部' },
							{ id: '3', name: '设备部' },
							{ id: '4', name: '技术部' },
							{ id: '5', name: '质量部' },
							{ id: '6', name: '环保部' },
							{ id: '7', name: '行政部' }
						]
						// 使用默认数据时也尝试设置用户默认部门
						this.setCurrentUserDepartment()
					}
				},
				fail: (error) => {
					console.error('请求部门数据失败:', error, " at pages/record/add.uvue:847")
					// 使用默认数据作为备选
					this.departmentOptions = [
						{ id: '1', name: '生产部' },
						{ id: '2', name: '安全部' },
						{ id: '3', name: '设备部' },
						{ id: '4', name: '技术部' },
						{ id: '5', name: '质量部' },
						{ id: '6', name: '环保部' },
						{ id: '7', name: '行政部' }
					]
					// 使用默认数据时也尝试设置用户默认部门
					this.setCurrentUserDepartment()
				}
			})
		},
			
			onNatureChange(e: UTSJSONObject) {
			console.log('性质界定选择器被点击，事件对象:', e, " at pages/record/add.uvue:865")
			this.selectedNatureIndex = e.getJSON('detail')?.getNumber('value') ?? -1
			console.log('选择的索引:', this.selectedNatureIndex, " at pages/record/add.uvue:867")
			if (this.selectedNatureIndex !== -1) {
				this.formData.natureId = this.natureOptions[this.selectedNatureIndex].id
				console.log('选择的性质界定:', this.natureOptions[this.selectedNatureIndex], " at pages/record/add.uvue:870")
			} else {
				this.formData.natureId = ''
			}
		},
		
		// 显示性质界定选择弹窗
		showNaturePicker() {
			if (this.natureOptions.length > 0) {
				this.showNaturePopup = true
				console.log('显示性质界定选择弹窗', " at pages/record/add.uvue:880")
			}
		},
		
		// 隐藏性质界定选择弹窗
		hideNaturePicker() {
			this.showNaturePopup = false
			console.log('隐藏性质界定选择弹窗', " at pages/record/add.uvue:887")
		},
		
		// 选择性质界定
		selectNature(index: number) {
			console.log('选择性质界定，索引:', index, " at pages/record/add.uvue:892")
			this.selectedNatureIndex = index
			if (index !== -1) {
				this.formData.natureId = this.natureOptions[index].id
				console.log('选择的性质界定:', this.natureOptions[index], " at pages/record/add.uvue:896")
			} else {
				this.formData.natureId = ''
			}
			this.hideNaturePicker()
		},
		
		// 显示专业选择弹窗
		showProfessionPicker() {
			if (this.professionOptions.length > 0) {
				this.showProfessionPopup = true
				console.log('显示专业选择弹窗', " at pages/record/add.uvue:907")
			}
		},
		
		// 隐藏专业选择弹窗
		hideProfessionPicker() {
			this.showProfessionPopup = false
			console.log('隐藏专业选择弹窗', " at pages/record/add.uvue:914")
		},
		
		// 选择专业
		selectProfession(index: number) {
			console.log('选择专业，索引:', index, " at pages/record/add.uvue:919")
			this.selectedProfessionIndex = index
			if (index !== -1) {
				this.formData.professionId = this.professionOptions[index].id
				console.log('选择的专业:', this.professionOptions[index], " at pages/record/add.uvue:923")
			} else {
				this.formData.professionId = ''
			}
			this.hideProfessionPicker()
		},
		
		// 显示检查人选择弹窗
		showInspectorPicker() {
			console.log('点击检查人选择器', " at pages/record/add.uvue:932")
			// 如果没有数据，先加载数据
			if (this.inspectorOptions.length === 0) {
				console.log('检查人数据为空，开始加载数据', " at pages/record/add.uvue:935")
				this.loadInspectorOptions()
			}
			this.showInspectorPopup = true
			console.log('显示检查人选择弹窗，当前选项数量:', this.inspectorOptions.length, " at pages/record/add.uvue:939")
		},
		
		// 隐藏检查人选择弹窗
		hideInspectorPicker() {
			this.showInspectorPopup = false
			console.log('隐藏检查人选择弹窗', " at pages/record/add.uvue:945")
		},
		
		// 选择检查人
		selectInspector(index: number) {
			console.log('选择检查人，索引:', index, " at pages/record/add.uvue:950")
			this.selectedInspectorIndex = index
			if (index !== -1) {
				this.formData.inspector = this.inspectorOptions[index].name
				console.log('选择的检查人:', this.inspectorOptions[index], " at pages/record/add.uvue:954")
			} else {
				this.formData.inspector = ''
			}
			this.hideInspectorPicker()
		},
		
		// 显示考核情况选择弹窗
		showAssessmentPicker() {
			if (this.assessmentOptions.length > 0) {
				this.showAssessmentPopup = true
				console.log('显示考核情况选择弹窗', " at pages/record/add.uvue:965")
			}
		},
		
		// 隐藏考核情况选择弹窗
		hideAssessmentPicker() {
			this.showAssessmentPopup = false
			console.log('隐藏考核情况选择弹窗', " at pages/record/add.uvue:972")
		},
		
		// 选择考核情况
		selectAssessment(index: number) {
			console.log('选择考核情况，索引:', index, " at pages/record/add.uvue:977")
			this.selectedAssessmentIndex = index
			if (index !== -1) {
				this.formData.scoreId = this.assessmentOptions[index].id
				console.log('选择的考核情况:', this.assessmentOptions[index], " at pages/record/add.uvue:981")
			} else {
				this.formData.scoreId = ''
			}
			this.hideAssessmentPicker()
		},
		
		// 显示部门选择弹窗
		showDepartmentPicker() {
			console.log('点击部门选择器', " at pages/record/add.uvue:990")
			// 如果没有数据，先加载数据
			if (this.departmentOptions.length === 0) {
				console.log('部门数据为空，开始加载数据', " at pages/record/add.uvue:993")
				this.loadDepartments()
			}
			this.showDepartmentPopup = true
			console.log('显示部门选择弹窗，当前选项数量:', this.departmentOptions.length, " at pages/record/add.uvue:997")
		},
		
		// 隐藏部门选择弹窗
		hideDepartmentPicker() {
			this.showDepartmentPopup = false
			console.log('隐藏部门选择弹窗', " at pages/record/add.uvue:1003")
		},
		
		// 选择部门
		selectDepartment(index: number) {
			console.log('选择部门，索引:', index, " at pages/record/add.uvue:1008")
			this.selectedDepartmentIndex = index
			if (index !== -1) {
				this.formData.departmentId = this.departmentOptions[index].id
				console.log('选择的部门:', this.departmentOptions[index], " at pages/record/add.uvue:1012")
			} else {
				this.formData.departmentId = ''
			}
			this.hideDepartmentPicker()
		},
		
		// 设置当前用户的部门为默认选择
		setCurrentUserDepartment() {
			const userInfoStr = uni.getStorageSync('userInfo') as string
			if (userInfoStr == null || userInfoStr == '') {
				console.log('未找到用户信息，无法设置默认部门', " at pages/record/add.uvue:1023")
				return
			}
			
			try {
				const userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/add.uvue:1028") as UTSJSONObject
				// 尝试获取部门ID，支持字符串和数字类型
				let userDeptId = userInfo.getString('deptId') ?? userInfo.getString('dept_id') ?? ''
				if (userDeptId === '') {
					// 如果字符串获取失败，尝试数字类型
					const deptIdNum = userInfo.getNumber('deptId') ?? userInfo.getNumber('dept_id')
					if (deptIdNum != null) {
						userDeptId = deptIdNum.toString()
					}
				}
				const userDeptName = userInfo.getString('deptName') ?? userInfo.getString('dept_name') ?? ''
				
				console.log('当前用户部门ID:', userDeptId, '部门名称:', userDeptName, " at pages/record/add.uvue:1040")
				console.log('原始用户信息:', userInfo, " at pages/record/add.uvue:1041")
				
				if (userDeptId !== '') {
					// 根据部门ID查找对应的部门选项
					for (let i = 0; i < this.departmentOptions.length; i++) {
						if (this.departmentOptions[i].id == userDeptId) {
							this.selectedDepartmentIndex = i
							this.formData.departmentId = userDeptId
							console.log('已设置默认部门:', this.departmentOptions[i], " at pages/record/add.uvue:1049")
							return
						}
					}
					console.log('根据部门ID未找到匹配的部门选项', " at pages/record/add.uvue:1053")
				}
				
				if (userDeptName !== '') {
					// 如果根据ID没找到，尝试根据部门名称查找
				for (let i = 0; i < this.departmentOptions.length; i++) {
					if (this.departmentOptions[i].name == userDeptName) {
						this.selectedDepartmentIndex = i
						this.formData.departmentId = this.departmentOptions[i].id
						console.log('已根据名称设置默认部门:', this.departmentOptions[i], " at pages/record/add.uvue:1062")
						return
					}
				}
				}
				
				console.log('未找到匹配的部门，保持默认未选择状态', " at pages/record/add.uvue:1068")
			} catch (error) {
				console.error('解析用户信息失败:', error, " at pages/record/add.uvue:1070")
			}
		},
		
		onRectificationDateChange(e: UTSJSONObject) {
			console.log('整改时间选择器触发，事件对象:', e, " at pages/record/add.uvue:1075")
			this.formData.rectificationDate = e.getJSON('detail')?.getString('value') ?? ''
			console.log('选择的整改时间:', this.formData.rectificationDate, " at pages/record/add.uvue:1077")
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
			}, " at pages/record/add.uvue:1108")
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
				console.log('日期不存在，调整为月末:', this.selectedDay, " at pages/record/add.uvue:1151")
			}
			
			this.pickerValue = [yearIndex >= 0 ? yearIndex : 0, monthIndex, dayIndex]
			
			console.log('显示日期选择器:', {
				selectedDate: this.formData.rectificationDate,
				selectedYear: this.selectedYear,
				selectedMonth: this.selectedMonth,
				selectedDay: this.selectedDay,
				yearIndex, monthIndex, dayIndex,
				pickerValue: this.pickerValue
			}, " at pages/record/add.uvue:1156")
			
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
				yearsArray: this.years,
				monthsArray: this.months,
				daysArray: this.days,
				oldDaysLength: oldDaysLength
			}, " at pages/record/add.uvue:1178")
			
			// 清除之前的防抖定时器
			if (this.pickerDebounceTimer !== null) {
				clearTimeout(this.pickerDebounceTimer as number)
			}
			
			// 直接根据索引获取对应的值
			this.selectedYear = this.years[value[0]]
			this.selectedMonth = value[1] + 1  // 月份索引从0开始，实际月份从1开始
			
			console.log('更新日期数组前:', {
				selectedYear: this.selectedYear,
				selectedMonth: this.selectedMonth,
				dayIndex: value[2],
				daysArrayLength: this.days.length
			}, " at pages/record/add.uvue:1195")
			
			// 更新日期数组
			this.updateDays()
			
			console.log('更新日期数组后:', {
				newDaysArrayLength: this.days.length,
				newDaysArray: this.days,
				dayIndex: value[2]
			}, " at pages/record/add.uvue:1205")
			
			// 更新选中的日期
			if (value[2] < this.days.length) {
				this.selectedDay = this.days[value[2]]
			} else {
				// 如果日期索引超出范围，选择最后一天
				const lastDayIndex = (this.days.length - 1) as Int
				this.selectedDay = this.days[lastDayIndex]
				value[2] = lastDayIndex
				console.log('日期索引超出范围，调整为:', value[2], '对应日期:', this.selectedDay, " at pages/record/add.uvue:1219")
			}
			
			// 重新计算正确的日期索引，确保选中项在可视区域内
			const correctDayIndex = this.days.findIndex(day => day === this.selectedDay)
			if (correctDayIndex >= 0 && correctDayIndex !== value[2]) {
				value[2] = correctDayIndex
				console.log('重新计算日期索引:', correctDayIndex, '对应日期:', this.selectedDay, " at pages/record/add.uvue:1226")
			}
			
			// 更新pickerValue
			this.pickerValue = value
			
			// 设置防抖定时器，在用户停止滑动后进行辅助滑动
			console.log('设置辅助滑动定时器，200ms后执行', " at pages/record/add.uvue:1233")
			this.pickerDebounceTimer = setTimeout(() => {
				console.log('防抖定时器触发，开始执行辅助滑动', " at pages/record/add.uvue:1235")
				this.snapToNearestItem()
			}, 200) // 200ms后执行辅助滑动，提供更快的响应
			
			console.log('日期选择最终结果:', {
				selectedYear: this.selectedYear,
				selectedMonth: this.selectedMonth, 
				selectedDay: this.selectedDay,
				pickerValue: this.pickerValue
			}, " at pages/record/add.uvue:1239")
		},
		
		// 辅助滑动：自动吸附到最近的选项
		snapToNearestItem() {
			console.log('执行辅助滑动，当前pickerValue:', this.pickerValue, " at pages/record/add.uvue:1249")
			
			// 简化的辅助滑动逻辑：总是尝试提供视觉反馈
			let needUpdate = false
			const newPickerValue = [...this.pickerValue]
			
			// 检查年份是否需要微调（强制吸附到最近的整数位置）
			const yearIndex = this.pickerValue[0]
			const nearestYear = Math.round(yearIndex)
			if (yearIndex !== nearestYear) {
				newPickerValue[0] = nearestYear
				needUpdate = true
				console.log(`辅助滑动：年份从索引${yearIndex}调整到${nearestYear}`, " at pages/record/add.uvue:1261")
			}
			
			// 检查月份是否需要微调（强制吸附到最近的整数位置）
			const monthIndex = this.pickerValue[1]
			const nearestMonth = Math.round(monthIndex)
			if (monthIndex !== nearestMonth) {
				newPickerValue[1] = nearestMonth
				needUpdate = true
				console.log(`辅助滑动：月份从索引${monthIndex}调整到${nearestMonth}`, " at pages/record/add.uvue:1270")
			}
			
			// 检查日期是否需要微调（强制吸附到最近的整数位置）
			const dayIndex = this.pickerValue[2]
			const nearestDay = Math.round(dayIndex)
			if (nearestDay < this.days.length && dayIndex !== nearestDay) {
				newPickerValue[2] = nearestDay
				needUpdate = true
				console.log(`辅助滑动：日期从索引${dayIndex}调整到${nearestDay}`, " at pages/record/add.uvue:1279")
			}
			
			// 如果没有需要微调的，直接返回，不干扰正常滑动
			if (!needUpdate) {
				console.log('辅助滑动：当前位置已经精确，无需调整', " at pages/record/add.uvue:1284")
				return
			}
			
			// 如果需要更新，则应用新的选择值
			if (needUpdate) {
				// 添加平滑过渡效果
				setTimeout(() => {
					this.pickerValue = newPickerValue
					
					// 更新对应的选择值
					if (newPickerValue[0] < this.years.length) {
						this.selectedYear = this.years[newPickerValue[0]]
					}
					if (newPickerValue[1] < this.months.length) {
						this.selectedMonth = newPickerValue[1] + 1
					}
					if (newPickerValue[2] < this.days.length) {
						this.selectedDay = this.days[newPickerValue[2]]
					}
					
					console.log('辅助滑动完成，新的选择值:', {
						pickerValue: this.pickerValue,
						selectedYear: this.selectedYear,
						selectedMonth: this.selectedMonth,
						selectedDay: this.selectedDay
					}, " at pages/record/add.uvue:1305")
				}, 50) // 50ms延迟，提供更快速的平滑视觉效果
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
			}, " at pages/record/add.uvue:1327")
			
			this.formData.rectificationDate = newDate
			this.hideDatePicker()
		},
		

		
	chooseImage() {
				if (this.choosingImage) {
					console.log('正在选择图片中，忽略重复点击', " at pages/record/add.uvue:1346")
					return
				}
				
				this.choosingImage = true
				console.log('选择图片前，当前图片数量:', this.imageList.length, " at pages/record/add.uvue:1351")
				console.log('当前图片列表:', this.imageList, " at pages/record/add.uvue:1352")
				uni.chooseImage({
					count: 3 - this.imageList.length,
					sizeType: ['compressed'],
					sourceType: ['camera', 'album'],
					success: (res) => {
						console.log('选择的图片路径:', res.tempFilePaths, " at pages/record/add.uvue:1358")
						console.log('选择的图片数量:', res.tempFilePaths.length, " at pages/record/add.uvue:1359")
						
						// 过滤重复的图片路径
						const newImages = res.tempFilePaths.filter(path => !this.imageList.includes(path))
						console.log('过滤重复后的新图片:', newImages, " at pages/record/add.uvue:1363")
						
						this.imageList = this.imageList.concat(newImages)
						console.log('合并后图片数量:', this.imageList.length, " at pages/record/add.uvue:1366")
						console.log('合并后图片列表:', this.imageList, " at pages/record/add.uvue:1367")
						this.choosingImage = false
					},
					fail: (err) => {
						console.log('选择图片失败:', err, " at pages/record/add.uvue:1371")
						this.choosingImage = false
					}
				})
			},
			
			removeImage(index: number) {
				this.imageList.splice(index, 1)
			},
			
			previewImage(index: number) {
				uni.previewImage({
					current: index,
					urls: this.imageList
				})
			},
			
			validateForm() {
			if (this.formData.responsibleUnit == null || this.formData.responsibleUnit == '') {
				uni.showToast({
					title: '请输入责任单位',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.responsiblePerson == null || this.formData.responsiblePerson.trim() == '') {
				uni.showToast({
					title: '请输入责任人',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.natureId == null || this.formData.natureId == '') {
				uni.showToast({
					title: '请选择性质界定',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.professionId == null || this.formData.professionId == '') {
				uni.showToast({
					title: '请选择专业',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.problemDescription == null || this.formData.problemDescription.trim() == '') {
				uni.showToast({
					title: '请输入问题描述',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.location == null || this.formData.location == '') {
				uni.showToast({
					title: '请输入发生部位',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.inspector == null || this.formData.inspector.trim() == '') {
				uni.showToast({
					title: '请输入检查人',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.groupLeader == null || this.formData.groupLeader.trim() == '') {
				uni.showToast({
					title: '请输入检查组组长',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.deputyLeader == null || this.formData.deputyLeader.trim() == '') {
				uni.showToast({
					title: '请输入检查组副组长',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.departmentId == null || this.formData.departmentId == '') {
				uni.showToast({
					title: '请选择所属部门',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.rectificationDate == null || this.formData.rectificationDate == '') {
				uni.showToast({
					title: '请选择整改时间',
					icon: 'none'
				})
				return false
			}
			
			return true
		},
			
			async uploadImages(): Promise<string[]> {
				console.log('开始上传图片，图片数量:', this.imageList.length, " at pages/record/add.uvue:1481")
				console.log('待上传图片列表:', this.imageList, " at pages/record/add.uvue:1482")
				if (this.imageList.length === 0) {
					return []
				}
				
				const uploadPromises = this.imageList.map(imagePath => {
				return new Promise<string>((resolve, reject) => {
					const token = uni.getStorageSync('token')
					console.log('开始上传图片:', imagePath, " at pages/record/add.uvue:1490")
					console.log('使用token:', token != null ? '已获取' : '未获取', " at pages/record/add.uvue:1491")
					

					
					uni.uploadFile({
						url: getApiUrl('/upload/image'),
						filePath: imagePath,
						name: 'file',
						header: {
							'Authorization': `Bearer ${token}`
						},
						success: (res) => {
							console.log('上传响应:', res, " at pages/record/add.uvue:1503")
							console.log('上传响应数据:', res.data, " at pages/record/add.uvue:1504")
							
							try {
								const data = UTSAndroid.consoleDebugError(JSON.parse(res.data as string), " at pages/record/add.uvue:1507") as UTSJSONObject
								console.log('解析后的数据:', data, " at pages/record/add.uvue:1508")
								
								if (res.statusCode === 200 && (data.getNumber('code') ?? 0) === 200) {
									const dataObj = data.getJSON('data')
									const imageUrl = dataObj?.getString('filePath') ?? ''
									console.log('获取到的图片URL:', imageUrl, " at pages/record/add.uvue:1513")
									
									if (imageUrl != null && imageUrl != '') {
										resolve(imageUrl)
									} else {
										reject(new Error('上传成功但未获取到图片URL'))
									}
								} else {
									console.log('上传失败，状态码:', res.statusCode, '错误码:', data.getNumber('code'), " at pages/record/add.uvue:1521")
									reject(new Error(data.getString('message') ?? '上传失败'))
								}
							} catch (e) {
								console.error('解析上传响应失败:', e, " at pages/record/add.uvue:1525")
								console.error('原始响应数据:', res.data, " at pages/record/add.uvue:1526")
								reject(new Error('解析响应失败'))
							}
						},
						fail: (err) => {
							console.log('上传图片失败:', err, " at pages/record/add.uvue:1531")
							reject(new Error(`上传失败: ${err.errMsg != null ? err.errMsg : '网络错误'}`))
						}
					})
					})
				})
				
				try {
					const imageUrls = await Promise.all(uploadPromises)
					console.log('所有图片上传成功，返回URL数量:', imageUrls.length, " at pages/record/add.uvue:1540")
					console.log('上传成功的图片URLs:', imageUrls, " at pages/record/add.uvue:1541")
					return imageUrls
				} catch (error) {
					console.error('图片上传失败:', error, " at pages/record/add.uvue:1544")
					console.error('错误详情:', error.message != null ? error.message : error, " at pages/record/add.uvue:1545")
					throw error
				}
			},
			
			async submitRecord() {
				console.log('submitRecord方法被调用', " at pages/record/add.uvue:1551")
				if (!this.validateForm() || this.submitting) {
					console.log('表单验证失败或正在提交中', " at pages/record/add.uvue:1553")
					return
				}
				
				this.submitting = true
				
				try {
					// 上传图片
				let imageUrls: string[] = []
				if (this.imageList.length > 0) {
					uni.showLoading({
						title: '上传图片中...'
					})
					try {
						imageUrls = await this.uploadImages()
						uni.hideLoading()
					} catch (uploadError) {
						uni.hideLoading()
						console.error('图片上传失败，终止提交:', uploadError, " at pages/record/add.uvue:1571")
						uni.showToast({
							title: '图片上传失败，请检查网络连接',
							icon: 'none',
							duration: 3000
						})
						this.submitting = false
						return
					}
				}
					
					// 提交记录
					uni.showLoading({
						title: '提交中...'
					})
					
					const token = uni.getStorageSync('token')
					const submitData = {__$originalPosition: new UTSSourceMapPosition("submitData", "pages/record/add.uvue", 1588, 12),
				responsibleUnit: this.formData.responsibleUnit,
				responsiblePerson: this.formData.responsiblePerson,
				natureId: this.formData.natureId,
				scoreId: this.formData.scoreId,
				assessmentAmount: this.formData.assessmentAmount,
				safetyScore: this.formData.safetyScore,
				professionId: this.formData.professionId,
				problemDescription: this.formData.problemDescription,
				location: this.formData.location,
				inspector: this.formData.inspector,
				groupLeader: this.formData.groupLeader,
				deputyLeader: this.formData.deputyLeader,
				departmentId: this.formData.departmentId,
				rectificationDate: this.formData.rectificationDate,
				imageUrls: imageUrls.length > 0 ? JSON.stringify(imageUrls) : ''
			}
						
				// 调试信息：检查部门信息是否正确包含在提交数据中
				console.log('调试：准备提交的数据:', submitData, " at pages/record/add.uvue:1607")
				console.log('调试：部门ID字段:', this.formData.departmentId, " at pages/record/add.uvue:1608")
				console.log('调试：当前选中的部门索引:', this.selectedDepartmentIndex, " at pages/record/add.uvue:1609")
				if (this.selectedDepartmentIndex >= 0 && this.selectedDepartmentIndex < this.departmentOptions.length) {
					console.log('调试：当前选中的部门对象:', this.departmentOptions[this.selectedDepartmentIndex], " at pages/record/add.uvue:1611")
				}
				uni.showToast({
					title: `提交部门ID: ${this.formData.departmentId ?? '空'}`,
					icon: 'none',
					duration: 2000
				})
					
					uni.request({
						url: getApiUrl('/inspection-record/submit'),
						method: 'POST',
						header: {
							'Authorization': `Bearer ${token}`,
							'Content-Type': 'application/json'
						},
						data: submitData,
						success: (res) => {
							uni.hideLoading()
							try {
								const data = res.data as UTSJSONObject
								
								if (res.statusCode === 200 && (data.getNumber('code') ?? 0) === 200) {
										// 显示上传成功弹窗，询问是否继续上传
										uni.showModal({
											title: '上传成功',
											content: '是否继续上传？',
											confirmText: '是',
											cancelText: '否',
											success: (modalRes) => {
												if (modalRes.confirm) {
													// 用户选择继续上传，清空表单数据（保留默认选项）
													this.resetFormData()
													uni.showToast({
														title: '已清空表单，可继续录入',
														icon: 'success'
													})
												} else if (modalRes.cancel) {
													// 用户选择不继续，跳转到周检页面
													uni.reLaunch({
														url: '/pages/weekly/weekly'
													})
												}
											}
										})
									} else {
										uni.showToast({
											title: data.getString('message') ?? '提交失败',
											icon: 'none'
										})
									}
							} catch (e) {
								uni.showToast({
									title: '提交失败',
									icon: 'none'
								})
							}
							this.submitting = false
						},
				fail: (error) => {
					console.error('提交记录失败:', error, " at pages/record/add.uvue:1670")
					uni.hideLoading()
					uni.showToast({
						title: '网络错误',
						icon: 'none'
					})
					this.submitting = false
				}
			})
		} catch (error) {
			console.error('提交记录失败:', error, " at pages/record/add.uvue:1680")
			uni.hideLoading()
			uni.showToast({
					title: '操作失败',
					icon: 'none'
				})
				this.submitting = false
			}
		},
		
		// 重置表单数据（保留默认选项）
		resetFormData() {
			// 清空表单数据
			this.formData = {
				responsibleUnit: '',
				responsiblePerson: '',
				natureId: '',
				scoreId: '',
				assessmentAmount: '',
				safetyScore: '',
				professionId: '',
				problemDescription: '',
				location: '',
				inspector: '',
				groupLeader: '',
				deputyLeader: '',
				departmentId: '',
				rectificationDate: ''
			} as RecordFormData
			
			// 清空图片列表
			this.imageList = [] as string[]
			
			// 重置选择器索引
			this.selectedNatureIndex = -1
			this.selectedAssessmentIndex = -1
			this.selectedProfessionIndex = -1
			this.selectedDepartmentIndex = -1
			this.selectedInspectorIndex = -1
			
			// 重新设置当前用户的默认部门
			this.setCurrentUserDepartment()
			
			// 滚动到顶部
			this.scrollTop = 0
		}
	}
})

export default __sfc__
function GenPagesRecordAddRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
const _component_picker_view_column = resolveComponent("picker-view-column")
const _component_picker_view = resolveComponent("picker-view")
const _component_form = resolveComponent("form")

  return _cE("scroll-view", _uM({
    class: "container",
    "scroll-y": "true",
    "scroll-top": _ctx.scrollTop,
    style: _nS(_uM({height: _ctx.containerHeight + 'px'})),
    "enable-back-to-top": "true"
  }), [
    _cV(_component_form, null, _uM({
      default: withSlotCtx((): any[] => [
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label" }), "📷 问题照片 (" + _tD(_ctx.imageList.length) + "/3)", 1 /* TEXT */),
          _cE("view", _uM({ class: "image-upload" }), [
            _cE("view", _uM({ class: "image-list" }), [
              _cE(Fragment, null, RenderHelpers.renderList(_ctx.imageList, (image, index, __index, _cached): any => {
                return _cE("view", _uM({
                  class: "image-item",
                  key: index
                }), [
                  _cE("image", _uM({
                    src: image,
                    mode: "aspectFill",
                    class: "uploaded-image",
                    onClick: () => {_ctx.previewImage(index)}
                  }), null, 8 /* PROPS */, ["src", "onClick"]),
                  _cE("view", _uM({
                    class: "image-delete",
                    onClick: () => {_ctx.removeImage(index)}
                  }), [
                    _cE("text", null, "×")
                  ], 8 /* PROPS */, ["onClick"])
                ])
              }), 128 /* KEYED_FRAGMENT */),
              _ctx.imageList.length < 3
                ? _cE("view", _uM({
                    key: 0,
                    class: "image-add",
                    onClick: _ctx.chooseImage
                  }), [
                    _cE("text", _uM({ class: "add-icon" }), "+"),
                    _cE("text", _uM({ class: "add-text" }), "添加照片")
                  ], 8 /* PROPS */, ["onClick"])
                : _cC("v-if", true)
            ])
          ])
        ]),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label" }), "问题描述"),
          _cE("view", _uM({ class: "form-input-wrapper" }), [
            _cE("textarea", _uM({
              modelValue: _ctx.formData.problemDescription,
              onInput: ($event: UniInputEvent) => {(_ctx.formData.problemDescription) = $event.detail.value},
              class: "form-textarea",
              placeholder: "请详细描述问题情况...",
              maxlength: "500",
              "show-confirm-bar": "false"
            }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
          ])
        ]),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label" }), "责任单位"),
          _cE("view", _uM({ class: "form-input-wrapper" }), [
            _cE("input", _uM({
              modelValue: _ctx.formData.responsibleUnit,
              onInput: ($event: UniInputEvent) => {(_ctx.formData.responsibleUnit) = $event.detail.value},
              class: "form-input",
              placeholder: "请输入责任单位"
            }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
          ])
        ]),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label" }), "责任人"),
          _cE("view", _uM({ class: "form-input-wrapper" }), [
            _cE("input", _uM({
              modelValue: _ctx.formData.responsiblePerson,
              onInput: ($event: UniInputEvent) => {(_ctx.formData.responsiblePerson) = $event.detail.value},
              class: "form-input",
              placeholder: "请输入责任人姓名"
            }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
          ])
        ]),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label-left" }), "性质界定"),
          _cE("view", _uM({
            class: _nC(["custom-picker-full", _uM({disabled: _ctx.natureOptions.length === 0})]),
            onClick: _ctx.showNaturePicker
          }), [
            _cE("text", _uM({
              class: _nC(["picker-text-full", _uM({placeholder: _ctx.selectedNatureIndex === -1})])
            }), _tD(_ctx.selectedNatureIndex === -1 ? '请选择问题性质' : (_ctx.selectedNatureIndex < _ctx.natureOptions.length ? _ctx.natureOptions[_ctx.selectedNatureIndex].name : '请选择问题性质')), 3 /* TEXT, CLASS */)
          ], 10 /* CLASS, PROPS */, ["onClick"])
        ]),
        isTrue(_ctx.showNaturePopup)
          ? _cE("view", _uM({
              key: 0,
              class: "popup-overlay",
              onClick: _ctx.hideNaturePicker
            }), [
              _cE("view", _uM({
                class: "popup-content",
                onClick: withModifiers(() => {}, ["stop"])
              }), [
                _cE("view", _uM({ class: "popup-header" }), [
                  _cE("text", _uM({ class: "popup-title" }), "选择性质界定"),
                  _cE("text", _uM({
                    class: "popup-close",
                    onClick: _ctx.hideNaturePicker
                  }), "×", 8 /* PROPS */, ["onClick"])
                ]),
                _cE("view", _uM({ class: "popup-list" }), [
                  _cE(Fragment, null, RenderHelpers.renderList(_ctx.natureOptions, (item, index, __index, _cached): any => {
                    return _cE("view", _uM({
                      key: item.id,
                      class: "popup-item",
                      onClick: () => {_ctx.selectNature(index)}
                    }), [
                      _cE("text", _uM({ class: "popup-item-text" }), _tD(item.name), 1 /* TEXT */),
                      _ctx.selectedNatureIndex === index
                        ? _cE("text", _uM({
                            key: 0,
                            class: "popup-item-check"
                          }), "✓")
                        : _cC("v-if", true)
                    ], 8 /* PROPS */, ["onClick"])
                  }), 128 /* KEYED_FRAGMENT */)
                ])
              ], 8 /* PROPS */, ["onClick"])
            ], 8 /* PROPS */, ["onClick"])
          : _cC("v-if", true),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label-left" }), "考核情况"),
          _cE("view", _uM({
            class: _nC(["custom-picker-full", _uM({disabled: _ctx.assessmentOptions.length === 0})]),
            onClick: _ctx.showAssessmentPicker
          }), [
            _cE("text", _uM({
              class: _nC(["picker-text-full", _uM({placeholder: _ctx.selectedAssessmentIndex === -1})])
            }), _tD(_ctx.selectedAssessmentIndex === -1 ? '请选择考核情况' : (_ctx.selectedAssessmentIndex < _ctx.assessmentOptions.length ? _ctx.assessmentOptions[_ctx.selectedAssessmentIndex].label : '请选择考核情况')), 3 /* TEXT, CLASS */)
          ], 10 /* CLASS, PROPS */, ["onClick"])
        ]),
        isTrue(_ctx.showAssessmentPopup)
          ? _cE("view", _uM({
              key: 1,
              class: "popup-overlay",
              onClick: _ctx.hideAssessmentPicker
            }), [
              _cE("view", _uM({
                class: "popup-content",
                onClick: withModifiers(() => {}, ["stop"])
              }), [
                _cE("view", _uM({ class: "popup-header" }), [
                  _cE("text", _uM({ class: "popup-title" }), "选择考核情况"),
                  _cE("text", _uM({
                    class: "popup-close",
                    onClick: _ctx.hideAssessmentPicker
                  }), "×", 8 /* PROPS */, ["onClick"])
                ]),
                _cE("view", _uM({ class: "popup-list" }), [
                  _cE(Fragment, null, RenderHelpers.renderList(_ctx.assessmentOptions, (item, index, __index, _cached): any => {
                    return _cE("view", _uM({
                      key: item.id,
                      class: "popup-item",
                      onClick: () => {_ctx.selectAssessment(index)}
                    }), [
                      _cE("text", _uM({ class: "popup-item-text" }), _tD(item.label), 1 /* TEXT */),
                      _ctx.selectedAssessmentIndex === index
                        ? _cE("text", _uM({
                            key: 0,
                            class: "popup-item-check"
                          }), "✓")
                        : _cC("v-if", true)
                    ], 8 /* PROPS */, ["onClick"])
                  }), 128 /* KEYED_FRAGMENT */)
                ])
              ], 8 /* PROPS */, ["onClick"])
            ], 8 /* PROPS */, ["onClick"])
          : _cC("v-if", true),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label" }), "考核金额"),
          _cE("view", _uM({ class: "form-input-wrapper" }), [
            _cE("input", _uM({
              modelValue: _ctx.formData.assessmentAmount,
              onInput: ($event: UniInputEvent) => {(_ctx.formData.assessmentAmount) = $event.detail.value},
              class: "form-input",
              placeholder: "请输入金额",
              type: "number"
            }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"]),
            _cE("text", _uM({ class: "input-suffix" }), "元")
          ])
        ]),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label" }), "安全记分"),
          _cE("view", _uM({ class: "form-input-wrapper" }), [
            _cE("input", _uM({
              modelValue: _ctx.formData.safetyScore,
              onInput: ($event: UniInputEvent) => {(_ctx.formData.safetyScore) = $event.detail.value},
              class: "form-input",
              placeholder: "请输入安全记分",
              type: "number"
            }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
          ])
        ]),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label-left" }), "专业"),
          _cE("view", _uM({
            class: _nC(["custom-picker-full", _uM({disabled: _ctx.professionOptions.length === 0})]),
            onClick: _ctx.showProfessionPicker
          }), [
            _cE("text", _uM({
              class: _nC(["picker-text-full", _uM({placeholder: _ctx.selectedProfessionIndex === -1})])
            }), _tD(_ctx.selectedProfessionIndex === -1 ? '请选择专业' : (_ctx.selectedProfessionIndex < _ctx.professionOptions.length ? _ctx.professionOptions[_ctx.selectedProfessionIndex].name : '请选择专业')), 3 /* TEXT, CLASS */)
          ], 10 /* CLASS, PROPS */, ["onClick"])
        ]),
        isTrue(_ctx.showProfessionPopup)
          ? _cE("view", _uM({
              key: 2,
              class: "popup-overlay",
              onClick: _ctx.hideProfessionPicker
            }), [
              _cE("view", _uM({
                class: "popup-content",
                onClick: withModifiers(() => {}, ["stop"])
              }), [
                _cE("view", _uM({ class: "popup-header" }), [
                  _cE("text", _uM({ class: "popup-title" }), "选择专业"),
                  _cE("text", _uM({
                    class: "popup-close",
                    onClick: _ctx.hideProfessionPicker
                  }), "×", 8 /* PROPS */, ["onClick"])
                ]),
                _cE("view", _uM({ class: "popup-list" }), [
                  _cE(Fragment, null, RenderHelpers.renderList(_ctx.professionOptions, (item, index, __index, _cached): any => {
                    return _cE("view", _uM({
                      key: item.id,
                      class: "popup-item",
                      onClick: () => {_ctx.selectProfession(index)}
                    }), [
                      _cE("text", _uM({ class: "popup-item-text" }), _tD(item.name), 1 /* TEXT */),
                      _ctx.selectedProfessionIndex === index
                        ? _cE("text", _uM({
                            key: 0,
                            class: "popup-item-check"
                          }), "✓")
                        : _cC("v-if", true)
                    ], 8 /* PROPS */, ["onClick"])
                  }), 128 /* KEYED_FRAGMENT */)
                ])
              ], 8 /* PROPS */, ["onClick"])
            ], 8 /* PROPS */, ["onClick"])
          : _cC("v-if", true),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label" }), "发生部位"),
          _cE("view", _uM({ class: "form-input-wrapper" }), [
            _cE("input", _uM({
              modelValue: _ctx.formData.location,
              onInput: ($event: UniInputEvent) => {(_ctx.formData.location) = $event.detail.value},
              class: "form-input",
              placeholder: "请输入发生部位"
            }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
          ])
        ]),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label-left" }), "检查人"),
          _cE("view", _uM({
            class: _nC(["custom-picker-full", _uM({disabled: _ctx.inspectorOptions.length === 0})]),
            onClick: _ctx.showInspectorPicker
          }), [
            _cE("text", _uM({
              class: _nC(["picker-text-full", _uM({placeholder: _ctx.selectedInspectorIndex === -1})])
            }), _tD(_ctx.selectedInspectorIndex === -1 ? '请选择检查人' : (_ctx.selectedInspectorIndex < _ctx.inspectorOptions.length ? _ctx.inspectorOptions[_ctx.selectedInspectorIndex].name : '请选择检查人')), 3 /* TEXT, CLASS */)
          ], 10 /* CLASS, PROPS */, ["onClick"])
        ]),
        isTrue(_ctx.showInspectorPopup)
          ? _cE("view", _uM({
              key: 3,
              class: "popup-overlay",
              onClick: _ctx.hideInspectorPicker
            }), [
              _cE("view", _uM({
                class: "popup-content",
                onClick: withModifiers(() => {}, ["stop"])
              }), [
                _cE("view", _uM({ class: "popup-header" }), [
                  _cE("text", _uM({ class: "popup-title" }), "选择检查人"),
                  _cE("text", _uM({
                    class: "popup-close",
                    onClick: _ctx.hideInspectorPicker
                  }), "×", 8 /* PROPS */, ["onClick"])
                ]),
                _cE("view", _uM({ class: "popup-list" }), [
                  _cE(Fragment, null, RenderHelpers.renderList(_ctx.inspectorOptions, (item, index, __index, _cached): any => {
                    return _cE("view", _uM({
                      key: item.id,
                      class: "popup-item",
                      onClick: () => {_ctx.selectInspector(index)}
                    }), [
                      _cE("text", _uM({ class: "popup-item-text" }), _tD(item.name), 1 /* TEXT */),
                      _ctx.selectedInspectorIndex === index
                        ? _cE("text", _uM({
                            key: 0,
                            class: "popup-item-check"
                          }), "✓")
                        : _cC("v-if", true)
                    ], 8 /* PROPS */, ["onClick"])
                  }), 128 /* KEYED_FRAGMENT */)
                ])
              ], 8 /* PROPS */, ["onClick"])
            ], 8 /* PROPS */, ["onClick"])
          : _cC("v-if", true),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label" }), "检查组组长"),
          _cE("view", _uM({ class: "form-input-wrapper" }), [
            _cE("input", _uM({
              modelValue: _ctx.formData.groupLeader,
              onInput: ($event: UniInputEvent) => {(_ctx.formData.groupLeader) = $event.detail.value},
              class: "form-input",
              placeholder: "请输入检查组组长姓名"
            }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
          ])
        ]),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label" }), "检查组副组长"),
          _cE("view", _uM({ class: "form-input-wrapper" }), [
            _cE("input", _uM({
              modelValue: _ctx.formData.deputyLeader,
              onInput: ($event: UniInputEvent) => {(_ctx.formData.deputyLeader) = $event.detail.value},
              class: "form-input",
              placeholder: "请输入检查组副组长姓名"
            }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
          ])
        ]),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label-left" }), "所属部门"),
          _cE("view", _uM({
            class: _nC(["custom-picker-full", _uM({disabled: _ctx.departmentOptions.length === 0})]),
            onClick: _ctx.showDepartmentPicker
          }), [
            _cE("text", _uM({
              class: _nC(["picker-text-full", _uM({placeholder: _ctx.selectedDepartmentIndex === -1})])
            }), _tD(_ctx.selectedDepartmentIndex === -1 ? '请选择所属部门' : (_ctx.selectedDepartmentIndex < _ctx.departmentOptions.length ? _ctx.departmentOptions[_ctx.selectedDepartmentIndex].name : '请选择所属部门')), 3 /* TEXT, CLASS */)
          ], 10 /* CLASS, PROPS */, ["onClick"])
        ]),
        isTrue(_ctx.showDepartmentPopup)
          ? _cE("view", _uM({
              key: 4,
              class: "popup-overlay",
              onClick: _ctx.hideDepartmentPicker
            }), [
              _cE("view", _uM({
                class: "popup-content",
                onClick: withModifiers(() => {}, ["stop"])
              }), [
                _cE("view", _uM({ class: "popup-header" }), [
                  _cE("text", _uM({ class: "popup-title" }), "选择所属部门"),
                  _cE("text", _uM({
                    class: "popup-close",
                    onClick: _ctx.hideDepartmentPicker
                  }), "×", 8 /* PROPS */, ["onClick"])
                ]),
                _cE("view", _uM({ class: "popup-list" }), [
                  _cE(Fragment, null, RenderHelpers.renderList(_ctx.departmentOptions, (item, index, __index, _cached): any => {
                    return _cE("view", _uM({
                      key: item.id,
                      class: "popup-item",
                      onClick: () => {_ctx.selectDepartment(index)}
                    }), [
                      _cE("text", _uM({ class: "popup-item-text" }), _tD(item.name), 1 /* TEXT */),
                      _ctx.selectedDepartmentIndex === index
                        ? _cE("text", _uM({
                            key: 0,
                            class: "popup-item-check"
                          }), "✓")
                        : _cC("v-if", true)
                    ], 8 /* PROPS */, ["onClick"])
                  }), 128 /* KEYED_FRAGMENT */)
                ])
              ], 8 /* PROPS */, ["onClick"])
            ], 8 /* PROPS */, ["onClick"])
          : _cC("v-if", true),
        _cE("view", _uM({ class: "form-item" }), [
          _cE("text", _uM({ class: "form-label-left" }), "整改时间"),
          _cE("view", _uM({
            class: "picker-container",
            onClick: _ctx.showDatePicker
          }), [
            _cE("text", _uM({
              class: _nC(["picker-text-full", _uM({placeholder: _ctx.formData.rectificationDate === ''})])
            }), _tD(_ctx.formData.rectificationDate !== '' ? _ctx.formData.rectificationDate : '请选择整改时间'), 3 /* TEXT, CLASS */)
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
        _cE("view", _uM({ class: "submit-section" }), [
          _cE("button", _uM({
            class: "submit-btn",
            type: "button",
            disabled: _ctx.submitting,
            onClick: _ctx.submitRecord
          }), [
            _cE("text", null, _tD(_ctx.submitting ? '提交中...' : '提交检查'), 1 /* TEXT */)
          ], 8 /* PROPS */, ["disabled", "onClick"])
        ])
      ]),
      _: 1 /* STABLE */
    }))
  ], 12 /* STYLE, PROPS */, ["scroll-top"])
}
const GenPagesRecordAddStyles = [_uM([["container", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 120], ["paddingLeft", 20], ["backgroundColor", "#f5f5f5"]]))], ["form-item", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["paddingTop", 15], ["paddingRight", 15], ["paddingBottom", 15], ["paddingLeft", 15], ["marginBottom", 15], ["boxShadow", "0 1px 3px rgba(0,0,0,0.1)"]]))], ["form-item-inline", _pS(_uM([["marginBottom", 20], ["display", "flex"], ["alignItems", "center"], ["flexWrap", "nowrap"]]))], ["form-label", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["marginBottom", 10], ["fontWeight", "400"]]))], ["form-label-inline", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["fontWeight", "400"], ["minWidth", 80], ["textAlign", "left"]]))], ["form-label-left", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["marginBottom", 10], ["fontWeight", "400"], ["textAlign", "left"]]))], ["form-input-wrapper", _pS(_uM([["position", "relative"], ["display", "flex"], ["alignItems", "center"]]))], ["form-input-flex", _pS(_uM([["flex", 1], ["display", "flex"], ["alignItems", "center"]]))], ["form-input", _pS(_uM([["flex", 1], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["backgroundImage", "none"], ["backgroundColor", "#f8f9fa"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["fontSize", 16], ["color", "#333333"], ["boxSizing", "border-box"]]))], ["input-suffix", _pS(_uM([["marginLeft", 10], ["fontSize", 16], ["color", "#666666"]]))], ["form-picker", _pS(_uM([["width", "100%"]]))], ["picker-content", _uM([["", _uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["backgroundImage", "none"], ["backgroundColor", "#f8f9fa"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"]])], [".disabled", _uM([["backgroundImage", "none"], ["backgroundColor", "#f5f5f5"], ["borderTopColor", "#dddddd"], ["borderRightColor", "#dddddd"], ["borderBottomColor", "#dddddd"], ["borderLeftColor", "#dddddd"], ["opacity", 0.6]])]])], ["custom-picker", _uM([["", _uM([["width", "100%"]])], [".disabled", _uM([["opacity", 0.6]])]])], ["custom-picker-inline", _uM([["", _uM([["flex", 1], ["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["backgroundImage", "none"], ["backgroundColor", "#f8f9fa"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"]])], [".disabled", _uM([["backgroundImage", "none"], ["backgroundColor", "#f5f5f5"], ["borderTopColor", "#dddddd"], ["borderRightColor", "#dddddd"], ["borderBottomColor", "#dddddd"], ["borderLeftColor", "#dddddd"], ["opacity", 0.6]])]])], ["custom-picker-full", _uM([["", _uM([["width", "100%"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dddddd"], ["borderRightColor", "#dddddd"], ["borderBottomColor", "#dddddd"], ["borderLeftColor", "#dddddd"], ["borderTopLeftRadius", "8rpx"], ["borderTopRightRadius", "8rpx"], ["borderBottomRightRadius", "8rpx"], ["borderBottomLeftRadius", "8rpx"], ["backgroundColor", "#ffffff"], ["minHeight", "80rpx"], ["display", "flex"], ["alignItems", "center"]])], [".disabled", _uM([["backgroundImage", "none"], ["backgroundColor", "#f5f5f5"], ["borderTopColor", "#dddddd"], ["borderRightColor", "#dddddd"], ["borderBottomColor", "#dddddd"], ["borderLeftColor", "#dddddd"], ["opacity", 0.6]])]])], ["date-picker-full", _pS(_uM([["width", "100%"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dddddd"], ["borderRightColor", "#dddddd"], ["borderBottomColor", "#dddddd"], ["borderLeftColor", "#dddddd"], ["borderTopLeftRadius", "8rpx"], ["borderTopRightRadius", "8rpx"], ["borderBottomRightRadius", "8rpx"], ["borderBottomLeftRadius", "8rpx"], ["backgroundColor", "#ffffff"], ["height", "auto"], ["minHeight", "80rpx"], ["display", "flex"], ["alignItems", "center"], ["boxSizing", "border-box"]]))], ["picker-text-left", _uM([["", _uM([["fontSize", 16], ["color", "#333333"], ["flex", 1]])], [".placeholder", _uM([["color", "#999999"]])]])], ["picker-text-full", _uM([["", _uM([["fontSize", "32rpx"], ["color", "#333333"], ["width", "100%"], ["textAlign", "left"]])], [".placeholder", _uM([["color", "#999999"]])]])], ["picker-arrow-right", _pS(_uM([["fontSize", 14], ["color", "#999999"]]))], ["popup-overlay", _pS(_uM([["position", "fixed"], ["top", 0], ["left", 0], ["right", 0], ["bottom", 0], ["backgroundImage", "none"], ["backgroundColor", "rgba(0,0,0,0.5)"], ["zIndex", 1000], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["popup-content", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["width", "80%"], ["maxWidth", 400], ["maxHeight", 480], ["overflow", "hidden"]]))], ["popup-header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 16], ["paddingRight", 20], ["paddingBottom", 16], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"]]))], ["popup-title", _pS(_uM([["fontSize", 18], ["fontWeight", "700"], ["color", "#333333"]]))], ["popup-close", _pS(_uM([["fontSize", 24], ["color", "#999999"], ["width", 30], ["height", 30], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["popup-list", _pS(_uM([["maxHeight", 300]]))], ["popup-item", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 16], ["paddingRight", 20], ["paddingBottom", 16], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f8f9fa"], ["transitionProperty", "backgroundColor"], ["transitionDuration", "0.2s"], ["backgroundImage:hover", "none"], ["backgroundColor:hover", "#f8f9fa"], ["borderBottomWidth:last-child", "medium"], ["borderBottomStyle:last-child", "none"], ["borderBottomColor:last-child", "#000000"]]))], ["popup-item-text", _pS(_uM([["fontSize", 16], ["color", "#333333"]]))], ["popup-item-check", _pS(_uM([["fontSize", 18], ["color", "#007aff"], ["fontWeight", "bold"]]))], ["date-picker-wrapper", _pS(_uM([["width", "100%"], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"]]))], ["date-picker-label", _pS(_uM([["fontSize", 16], ["color", "#666666"], ["textAlign", "center"], ["marginBottom", 10]]))], ["date-picker-popup", _pS(_uM([["width", "100%"], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"]]))], ["date-picker-button", _pS(_uM([["paddingTop", 15], ["paddingRight", 30], ["paddingBottom", 15], ["paddingLeft", 30], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#007aff"], ["borderRightColor", "#007aff"], ["borderBottomColor", "#007aff"], ["borderLeftColor", "#007aff"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["backgroundColor", "#007aff"], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"], ["minWidth", 120], ["backgroundColor:active", "#0056cc"], ["borderTopColor:active", "#0056cc"], ["borderRightColor:active", "#0056cc"], ["borderBottomColor:active", "#0056cc"], ["borderLeftColor:active", "#0056cc"]]))], ["date-picker-button-text", _pS(_uM([["fontSize", 16], ["color", "#ffffff"], ["fontWeight", "400"], ["textAlign", "center"]]))], ["picker-text", _uM([["", _uM([["fontSize", 16], ["color", "#333333"], ["flex", 1]])], [".placeholder", _uM([["color", "#999999"]])]])], ["picker-arrow", _pS(_uM([["fontSize", 14], ["color", "#999999"]]))], ["form-textarea", _pS(_uM([["width", "100%"], ["minHeight", 100], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["backgroundImage", "none"], ["backgroundColor", "#f8f9fa"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["fontSize", 16], ["color", "#333333"], ["boxSizing", "border-box"]]))], ["char-count", _pS(_uM([["textAlign", "right"], ["marginTop", 5]]))], ["image-list", _pS(_uM([["display", "flex"], ["flexWrap", "wrap"]]))], ["image-item", _pS(_uM([["position", "relative"], ["width", 80], ["height", 80]]))], ["uploaded-image", _pS(_uM([["width", "100%"], ["height", "100%"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["backgroundImage", "none"], ["backgroundColor", "#f0f0f0"]]))], ["image-delete", _pS(_uM([["position", "absolute"], ["top", -5], ["right", -5], ["width", 20], ["height", 20], ["backgroundImage", "none"], ["backgroundColor", "#ff4757"], ["borderTopLeftRadius", 10], ["borderTopRightRadius", 10], ["borderBottomRightRadius", 10], ["borderBottomLeftRadius", 10], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["image-add", _pS(_uM([["width", 80], ["height", 80], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "dashed"], ["borderRightStyle", "dashed"], ["borderBottomStyle", "dashed"], ["borderLeftStyle", "dashed"], ["borderTopColor", "#cccccc"], ["borderRightColor", "#cccccc"], ["borderBottomColor", "#cccccc"], ["borderLeftColor", "#cccccc"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"], ["backgroundImage", "none"], ["backgroundColor", "#fafafa"]]))], ["add-icon", _pS(_uM([["fontSize", 24], ["color", "#999999"], ["marginBottom", 5]]))], ["add-text", _pS(_uM([["fontSize", 12], ["color", "#999999"]]))], ["submit-section", _pS(_uM([["position", "fixed"], ["bottom", 0], ["left", 0], ["right", 0], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopWidth", 1], ["borderTopStyle", "solid"], ["borderTopColor", "#f0f0f0"]]))], ["submit-btn", _pS(_uM([["width", "100%"], ["height", 48], ["backgroundImage", "none"], ["backgroundColor", "#007AFF"], ["color", "#ffffff"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["fontSize", 18], ["fontWeight", "bold"], ["backgroundImage:disabled", "none"], ["backgroundColor:disabled", "#cccccc"], ["color:disabled", "#999999"]]))], ["date-picker-modal", _pS(_uM([["position", "fixed"], ["top", 0], ["left", 0], ["right", 0], ["bottom", 0], ["backgroundColor", "rgba(0,0,0,0.5)"], ["display", "flex"], ["alignItems", "flex-end"], ["zIndex", 1000]]))], ["date-picker-content", _pS(_uM([["width", "100%"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 16], ["borderTopRightRadius", 16], ["borderBottomRightRadius", 0], ["borderBottomLeftRadius", 0], ["paddingBottom", "env(safe-area-inset-bottom)"]]))], ["date-picker-header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 16], ["paddingRight", 20], ["paddingBottom", 16], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"]]))], ["date-picker-cancel", _pS(_uM([["fontSize", 16], ["color", "#999999"]]))], ["date-picker-title", _pS(_uM([["fontSize", 18], ["color", "#333333"]]))], ["date-picker-confirm", _pS(_uM([["fontSize", 16], ["color", "#007AFF"]]))], ["date-picker-view", _pS(_uM([["height", 320], ["paddingTop", 0], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["position", "relative"]]))], ["picker-item", _uM([["", _uM([["height", 60], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["fontSize", 16], ["color", "#666666"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "cubic-bezier(0.4,0,0.2,1)"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["marginTop", 4], ["marginRight", 8], ["marginBottom", 4], ["marginLeft", 8], ["position", "relative"], ["zIndex", 2], ["opacity", 0.6], ["backgroundColor", "rgba(0,0,0,0.02)"], ["backgroundColor:hover", "#f0f8ff"], ["color:hover", "#007AFF"]])], [".selected", _uM([["backgroundImage", "linear-gradient(135deg, #007AFF 0%, #0056CC 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#ffffff"], ["fontWeight", "700"], ["boxShadow", "0 4px 12px rgba(0, 122, 255, 0.4), inset 0 1px 0 rgba(255, 255, 255, 0.2)"], ["transform", "scale(1.05)"], ["zIndex", 3], ["opacity", 1], ["fontSize", 18], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#ffffff"], ["borderRightColor", "#ffffff"], ["borderBottomColor", "#ffffff"], ["borderLeftColor", "#ffffff"], ["textShadow", "0 1px 2px rgba(0, 0, 0, 0.3)"]])]])], ["picker-item-spacer", _pS(_uM([["pointerEvents", "none"], ["opacity", 0], ["!backgroundImage", "none"], ["!backgroundColor", "rgba(0,0,0,0)"]]))], ["@TRANSITION", _uM([["popup-item", _uM([["property", "backgroundColor"], ["duration", "0.2s"]])], ["picker-item", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "cubic-bezier(0.4,0,0.2,1)"]])]])]])]
