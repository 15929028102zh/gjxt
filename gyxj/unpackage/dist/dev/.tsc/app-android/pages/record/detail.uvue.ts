
	import { getApiUrl, apiConfig } from '@/config/api.uts'
	
	type NatureOption = { __$originalPosition?: UTSSourceMapPosition<"NatureOption", "pages/record/detail.uvue", 286, 7>;
		id: string
		name: string
	}
	
	type ProfessionOption = { __$originalPosition?: UTSSourceMapPosition<"ProfessionOption", "pages/record/detail.uvue", 291, 7>;
		id: string
		name: string
	}
	
	type DepartmentOption = { __$originalPosition?: UTSSourceMapPosition<"DepartmentOption", "pages/record/detail.uvue", 296, 7>;
		id: string
		name: string
	}
	
	type RecordDetail = { __$originalPosition?: UTSSourceMapPosition<"RecordDetail", "pages/record/detail.uvue", 301, 7>;
		id: string
		problemDescription: string
		responsibleUnit: string
		responsiblePerson: string
		natureId: string  // 修改为string类型，但会从 natureName 字段映射
		natureName: string
		assessmentSituation: string
		assessmentAmount: number
		safetyScore: number
		professionId: string  // 修改为string类型
		professionName: string
		location: string
		inspector: string
		groupLeader: string
		deputyLeader: string
		departmentId: string  // 修改为string类型
		departmentName: string
		rectificationDate: string
		status: number
		createTime: string
		updateTime: string
		problemImageUrls: string
		locationName: string
		severity: number
		taskName: string
		reporterName: string
		suggestions: string
		reporterId: string
		rectificationEffect: string
		rectificationImageUrls: string
		rectificationTime: string
	}

	const __sfc__ = defineComponent({
		data() {
			return {
				recordId: '',
				recordDetail: null as RecordDetail | null,
				imageList: [] as Array<string>,
				loading: true,
				canOperate: false,
				rectificationEffect: '',
				rectificationImages: [] as Array<string>,
				rectificationStatus: 0,
				saving: false,
				scrollTop: 0,
				containerHeight: 0,
				// 下拉选择框相关数据
				natureOptions: [] as NatureOption[],
				professionOptions: [] as ProfessionOption[],
				departmentOptions: [] as DepartmentOption[],
				selectedNatureIndex: -1,
				selectedProfessionIndex: -1,
				selectedDepartmentIndex: -1,
				showNaturePopup: false,
				showProfessionPopup: false,
				showDepartmentPopup: false,
			// 数据加载状态跟踪
			loadedDataCount: 0,
			totalDataCount: 4 // 总共需要加载4类数据
		}
	},
		onLoad(options: UTSJSONObject) {
			this.recordId = options.getString('id') ?? ''
			this.checkLogin()
			// 重置数据加载计数器
			this.loadedDataCount = 0
			// 并发加载所有数据
			this.loadRecordDetail()
			this.loadNatureOptions()
			this.loadProfessionOptions()
			this.loadDepartments()
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
			
			// 检查所有数据是否加载完成
			checkAllDataLoaded() {
				this.loadedDataCount++
				console.log(`数据加载进度: ${this.loadedDataCount}/${this.totalDataCount}`, " at pages/record/detail.uvue:400")
				
				if (this.loadedDataCount >= this.totalDataCount) {
					console.log('所有数据加载完成，开始回写选中值', " at pages/record/detail.uvue:403")
					console.log('当前数据状态:', {
						recordDetail: this.recordDetail,
						natureOptionsLength: this.natureOptions.length,
						professionOptionsLength: this.professionOptions.length,
						departmentOptionsLength: this.departmentOptions.length
					}, " at pages/record/detail.uvue:404")
					// 所有数据加载完成，延迟100ms回写选中值，确保数据已经更新到界面
					setTimeout(() => {
						console.log('开始执行 initSelectedValues', " at pages/record/detail.uvue:412")
						this.initSelectedValues()
						this.loading = false
						console.log('加载状态设置为 false', " at pages/record/detail.uvue:415")
					}, 100)
				}
			},
			
			loadRecordDetail() {
				const token = uni.getStorageSync('token')
				uni.request({
					url: getApiUrl(`/inspection-record/${this.recordId}`),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
				const data = res.data as UTSJSONObject
				if (res.statusCode === 200 && data.getNumber('code') === 200) {
					const recordData = data.getJSON('data')

					if (recordData != null) {
								this.recordDetail = {
				id: recordData.getString('id') ?? '',
				problemDescription: recordData.getString('problemDescription') ?? '',
				locationName: recordData.getString('locationName') ?? '',
				severity: recordData.getNumber('severity') ?? 1,
				taskName: recordData.getString('taskName') ?? '',
				reporterName: recordData.getString('reporterName') ?? '',
				createTime: recordData.getString('createTime') ?? '',
				updateTime: recordData.getString('updateTime') ?? '',
				status: recordData.getNumber('status') ?? 0,
				problemImageUrls: recordData.getString('problemImageUrls') ?? '',
				suggestions: recordData.getString('suggestions') ?? '',
				reporterId: recordData.getString('reporterId') ?? '',
				responsibleUnit: recordData.getString('responsibleUnit') ?? '',
				responsiblePerson: recordData.getString('responsiblePerson') ?? '',
				// 性质界定：检查数据库返回值的类型
				natureId: '', // 将在后面根据数据类型确定
				natureName: '', // 名称通过匹配获得
				assessmentSituation: recordData.getString('assessmentSituation') ?? '',
				assessmentAmount: recordData.getNumber('assessmentAmount') ?? 0,
				safetyScore: recordData.getNumber('safetyScore') ?? 0,
				// 专业ID使用字符串类型
				professionId: recordData.getString('professionId') ?? '',
				professionName: recordData.getString('professionName') ?? '',
				inspector: recordData.getString('inspectorName') ?? '',
				groupLeader: recordData.getString('groupLeaderName') ?? '',
				deputyLeader: recordData.getString('deputyLeaderName') ?? '',
				// 部门ID使用字符串类型
				departmentId: recordData.getString('departmentId') ?? '',
				departmentName: recordData.getString('departmentName') ?? '',
				rectificationDate: recordData.getString('rectificationDeadline') ?? '',
				location: recordData.getString('location') ?? '',
				rectificationEffect: recordData.getString('rectificationEffect') ?? '',
				rectificationImageUrls: recordData.getString('rectificationImageUrls') ?? '',
				rectificationTime: recordData.getString('rectificationTime') ?? ''
			} as RecordDetail
										
			// 处理性质界定字段映射
			const natureDefinitionValue = recordData.getString('natureDefinition') ?? ''
			console.log('natureDefinition原始值:', natureDefinitionValue, " at pages/record/detail.uvue:473")
										
			// 判断返回值是ID还是名称，然后正确映射
			// 如果是32位的ID格式，则作为ID处理；否则作为名称处理
			if (natureDefinitionValue.length === 32 && /^[a-f0-9]+$/i.test(natureDefinitionValue)) {
				// 这是ID，直接赋值
				this.recordDetail.natureId = natureDefinitionValue
				console.log('检测到ID格式，作为ID处理:', natureDefinitionValue, " at pages/record/detail.uvue:480")
			} else {
				// 这是名称，需要转换为ID
				this.recordDetail.natureName = natureDefinitionValue
				console.log('检测到名称格式，作为名称处理:', natureDefinitionValue, " at pages/record/detail.uvue:484")
				// natureId会在字典加载完成后通过名称匹配设置
			}
						
						// 回写整改信息到表单
						this.rectificationEffect = this.recordDetail?.rectificationEffect ?? ''
						// 根据记录状态和整改效果来判断整改状态
						if (this.recordDetail?.rectificationEffect != null && this.recordDetail?.rectificationEffect != '') {
							this.rectificationStatus = 1  // 已整改
						} else {
							this.rectificationStatus = 0  // 未整改
						}
						
						// 处理整改图片
								const rectificationImages = this.recordDetail?.rectificationImageUrls
								if (rectificationImages != null && rectificationImages != '') {
									try {
										let urlList: Array<string> = []
										if (typeof rectificationImages === 'string') {
											const parsed = UTSAndroid.consoleDebugError(JSON.parse(rectificationImages as string), " at pages/record/detail.uvue:503")
											if (Array.isArray(parsed)) {
												urlList = parsed as Array<string>
											}
										} else if (Array.isArray(rectificationImages)) {
											urlList = rectificationImages as Array<string>
										}
										
										// 为整改图片URL添加完整的HTTP地址
										this.rectificationImages = urlList.map(url => this.convertToHttpUrl(url))
									} catch (e) {
										if (typeof rectificationImages === 'string') {
											const urlList = (rectificationImages as string).split(',')
											this.rectificationImages = urlList.map(url => this.convertToHttpUrl(url.trim()))
										}
									}
								}
						
						// 处理图片URL
						const imageUrls = this.recordDetail?.problemImageUrls
						if (imageUrls != null && imageUrls != '') {
							try {
								let urlList: Array<string> = []
								if (typeof imageUrls === 'string') {
									const parsed = UTSAndroid.consoleDebugError(JSON.parse(imageUrls as string), " at pages/record/detail.uvue:527")
									if (Array.isArray(parsed)) {
										urlList = parsed as Array<string>
									}
								} else if (Array.isArray(imageUrls)) {
									urlList = imageUrls as Array<string>
								}
								
								// 为图片URL添加完整的HTTP地址
								this.imageList = urlList.map(url => this.convertToHttpUrl(url))
							} catch (e) {
								if (typeof imageUrls === 'string') {
									const urlList = (imageUrls as string).split(',')
									this.imageList = urlList.map(url => this.convertToHttpUrl(url.trim()))
								}
							}
						} else {
							this.imageList = []
						}
						
						// 检查是否可以操作（简单权限控制）
						const userInfoStr = uni.getStorageSync('userInfo') as string
						let userInfo: UTSJSONObject | null = null
						if (userInfoStr != null && userInfoStr != '') {
							try {
								userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/detail.uvue:552") as UTSJSONObject
							} catch (e) {
								console.error('解析用户信息失败:', e, " at pages/record/detail.uvue:554")
							}
						}
						this.canOperate = userInfo != null && (userInfo.getString('role') === 'ADMIN' || userInfo.getString('id') === (this.recordDetail?.reporterId ?? ''))
						
						// 设置页面标题
						uni.setNavigationBarTitle({
							title: '记录详情'
						})
						
						// 记录详情加载完成
						this.checkAllDataLoaded()
					}
				} else {
					uni.showToast({
						title: data.getString('message') ?? '加载失败',
						icon: 'none'
					})
				}
				// 不管成功还是失败，都要调用检查方法
				this.checkAllDataLoaded()
				},
				fail: (error) => {
				console.error('加载记录详情失败:', error, " at pages/record/detail.uvue:577")
				uni.showToast({
					title: '网络错误',
					icon: 'none'
				})
				// 加载失败也要调用检查方法
				this.checkAllDataLoaded()
			}
		})
		},
		
		previewImage(index: number) {
			uni.previewImage({
				current: index,
				urls: this.imageList
			})
		},
		
		onImageLoad(e: any) {
			// 图片加载成功
		},
		
		onImageError(e: any) {
			uni.showToast({
				title: '图片加载失败',
				icon: 'none'
			})
		},
		
		updateRecordStatus(status: number, actionName: string) {
		uni.showModal({
			title: '确认操作',
			content: `确定要${actionName}吗？`,
			success: (res) => {
				if (res.confirm) {
					const token = uni.getStorageSync('token')
					uni.request({
						url: getApiUrl(`/inspection-record/${this.recordId}/status`),
						method: 'PUT',
						header: {
							'Authorization': `Bearer ${token}`,
							'Content-Type': 'application/json'
						},
						data: {
							status: status
						},
						success: (response) => {
							const data = response.data as UTSJSONObject
							if (response.statusCode === 200 && (data.getNumber('code') ?? 0) === 200) {
								uni.showToast({
									title: `${actionName}成功`,
									icon: 'success'
								})
								this.loadRecordDetail()
							} else {
					uni.showToast({
						title: data.getString('message') ?? '操作失败',
						icon: 'none'
					})
				}
						},
						fail: (error) => {
							console.error(`${actionName}失败:`, error, " at pages/record/detail.uvue:639")
							uni.showToast({
								title: '网络错误',
								icon: 'none'
							})
						}
					})
					}
			}
		})
		},
		
		startRectification() {
			this.updateRecordStatus(1, '开始整改')
		},
		
		completeRectification() {
			this.updateRecordStatus(2, '完成整改')
		},
		
		acceptRectification() {
			this.updateRecordStatus(3, '验收通过')
		},
		
		getStatusText(status: number): string {
			switch (status) {
				case 0: return '待整改'
				case 1: return '整改中'
				case 2: return '已整改'
				case 3: return '已验收'
				default: return '未知'
			}
		},
		
		getStatusClass(status: number): string {
			switch (status) {
				case 0: return 'status-pending'
				case 1: return 'status-processing'
				case 2: return 'status-completed'
				case 3: return 'status-accepted'
				default: return 'status-unknown'
			}
		},
		
		getSeverityText(severity: number): string {
			switch (severity) {
				case 1: return '低'
				case 2: return '中'
				case 3: return '高'
				case 4: return '紧急'
				default: return '未知'
			}
		},
		
		// 整改图片相关方法
		chooseRectificationImage() {
			if (this.rectificationImages.length >= 3) {
				uni.showToast({
					title: '最多只能上传3张图片',
					icon: 'none'
				})
				return
			}
			
			uni.chooseImage({
				count: 3 - this.rectificationImages.length,
				sourceType: ['camera', 'album'],
				success: (res) => {
					const tempFilePaths = res.tempFilePaths
					// 直接存储本地文件路径，不立即上传
					for (let i = 0; i < tempFilePaths.length; i++) {
						this.rectificationImages.push(tempFilePaths[i])
					}
				}
			})
		},
		

		
		previewRectificationImage(index: number) {
			uni.previewImage({
				current: index,
				urls: this.rectificationImages
			})
		},
		
		removeRectificationImage(index: number) {
			this.rectificationImages.splice(index, 1)
		},
		
		setRectificationStatus(status: number) {
			this.rectificationStatus = status
		},
		
		// 将相对路径转换为HTTP访问路径
		convertToHttpUrl(relativePath: string): string {
			if (relativePath == null || relativePath == '' || relativePath.trim() === '') {
				return ''
			}
			
			// 如果已经是完整的HTTP/HTTPS URL，直接返回
			if (relativePath.startsWith('http://') || relativePath.startsWith('https://')) {
				return relativePath
			}
			
			// 直接拼接完整的HTTP URL
			// 后端已返回相对路径，如: /files/images/20250818/xxx.jpg
			const fullUrl = `${apiConfig.baseUrl}/api${relativePath}`
			return fullUrl
		},

		async saveRectificationInfo() {
			if (this.saving) return
			
			if (this.rectificationEffect.trim().length === 0) {
				uni.showToast({
					title: '请填写整改效果',
					icon: 'none'
				})
				return
			}
			
			this.saving = true
			
			try {
				// 1. 先上传整改图片获取URL
				let imageUrls: string[] = []
				if (this.rectificationImages.length > 0) {
					uni.showLoading({
						title: '上传图片中...'
					})
					imageUrls = await this.uploadRectificationImages()
					uni.hideLoading()
				}
				
				// 2. 组织全页数据 - 保持原有字段不变，更新整改相关字段
				const updateData = {__$originalPosition: new UTSSourceMapPosition("updateData", "pages/record/detail.uvue", 775, 11),
					// 保持原有记录的所有字段
					id: this.recordDetail?.id ?? '',
					problemDescription: this.recordDetail?.problemDescription ?? '',
					responsibleUnit: this.recordDetail?.responsibleUnit ?? '',
					responsiblePerson: this.recordDetail?.responsiblePerson ?? '',
					// 性质界定：因为数据库存储的是名称，所以保存名称
					natureDefinition: this.selectedNatureIndex !== -1 ? this.natureOptions[this.selectedNatureIndex].name : (this.recordDetail?.natureName ?? ''),
					assessmentSituation: this.recordDetail?.assessmentSituation ?? '',
					assessmentAmount: this.recordDetail?.assessmentAmount ?? 0,
					safetyScore: this.recordDetail?.safetyScore ?? 0,
					professionId: this.selectedProfessionIndex !== -1 ? this.professionOptions[this.selectedProfessionIndex].id : (this.recordDetail?.professionId ?? ''),
					location: this.recordDetail?.location ?? '',
					inspector: this.recordDetail?.inspector ?? '',
					groupLeader: this.recordDetail?.groupLeader ?? '',
					deputyLeader: this.recordDetail?.deputyLeader ?? '',
					departmentId: this.selectedDepartmentIndex !== -1 ? this.departmentOptions[this.selectedDepartmentIndex].id : (this.recordDetail?.departmentId ?? ''),
					rectificationDeadline: this.recordDetail?.rectificationDate ?? '',
					problemImageUrls: this.recordDetail?.problemImageUrls ?? '',
					reporterId: this.recordDetail?.reporterId ?? '',
					
					// 更新整改相关字段
					rectificationEffect: this.rectificationEffect,
					rectificationImageUrls: imageUrls.join(','), // 使用上传后的URL，以逗号分隔的字符串存储
					status: this.rectificationStatus, // 整改状态更新到status字段：0=未整改，1=已整改
					rectificationTime: new Date().toISOString().replace('T', ' ').substring(0, 19) // 当前时间，格式: yyyy-MM-dd HH:mm:ss
				}
				
				// 3. 调用updateRecord接口
				uni.showLoading({
					title: '保存中...'
				})
				
				// 添加调试信息
				console.log('准备保存的数据:', updateData, " at pages/record/detail.uvue:809")
				console.log('性质界定保存值:', {
					selectedNatureIndex: this.selectedNatureIndex,
					natureDefinition: updateData.natureDefinition,
					natureOptions: this.natureOptions
				}, " at pages/record/detail.uvue:810")
				
				const token = uni.getStorageSync('token')
				uni.request({
					url: getApiUrl('/inspection-record'),
					method: 'PUT',
					header: {
						'Authorization': `Bearer ${token}`,
						'Content-Type': 'application/json'
					},
					data: updateData,
					success: (response) => {
						uni.hideLoading()
						const data = response.data as UTSJSONObject
						if (response.statusCode === 200 && (data.getNumber('code') ?? 0) === 200) {
							uni.showToast({
								title: '保存成功',
								icon: 'success',
								duration: 1500
							})
							// 延迟返回，让用户看到成功提示
							setTimeout(() => {
								// 返回到record页面并触发刷新
								const pages = getCurrentPages()
								if (pages.length >= 2) {
									// 获取上一个页面实例
									const prevPage = pages[pages.length - 2]
									// 检查上一个页面是否是record页面
									if (prevPage.route != null && prevPage.route.includes('/pages/record/record')) {
										// 通过eventChannel通知上一个页面刷新
										uni.navigateBack({
											delta: 1,
											success: () => {
												// 通过$emit触发全局事件通知record页面刷新
												uni.$emit('recordUpdated')
											}
										})
									} else {
										// 如果上一个页面不是record页面，直接返回
										uni.navigateBack()
									}
								} else {
									// 如果没有上一个页面，跳转到record页面
									uni.redirectTo({
										url: '/pages/record/record?status=0'
									})
								}
							}, 1600)
						} else {
							uni.showToast({
								title: data.getString('message') ?? '保存失败',
								icon: 'none'
							})
						}
						this.saving = false
					},
					fail: (error) => {
						uni.hideLoading()
						console.error('保存整改信息失败:', error, " at pages/record/detail.uvue:872")
						uni.showToast({
							title: '网络错误',
							icon: 'none'
						})
						this.saving = false
					}
				})
			} catch (error) {
				uni.hideLoading()
				console.error('上传图片失败:', error, " at pages/record/detail.uvue:882")
				uni.showToast({
					title: '图片上传失败，请重试',
					icon: 'none'
				})
				this.saving = false
			}
		},
		
		// 批量上传整改图片
		async uploadRectificationImages(): Promise<string[]> {
			if (this.rectificationImages.length === 0) {
				return []
			}
			
			const token = uni.getStorageSync('token')
			const uploadPromises = this.rectificationImages.map(imagePath => {
				return new Promise<string>((resolve, reject) => {
					uni.uploadFile({
						url: getApiUrl('/upload/image'),
						filePath: imagePath,
						name: 'file',
						header: {
							'Authorization': `Bearer ${token}`
						},
						success: (res) => {
							try {
								const data = UTSAndroid.consoleDebugError(JSON.parse(res.data), " at pages/record/detail.uvue:909") as UTSJSONObject
								if (res.statusCode === 200 && (data.getNumber('code') ?? 0) === 200) {
									const dataObj = data.getJSON('data')
									const imageUrl = dataObj?.getString('filePath') ?? ''
									if (imageUrl != null && imageUrl != '') {
										resolve(imageUrl)
									} else {
										reject(new Error('上传成功但未获取到图片URL'))
									}
								} else {
									reject(new Error(data.getString('message') ?? '上传失败'))
								}
							} catch (e) {
								reject(new Error('解析响应失败'))
							}
						},
						fail: (error) => {
							reject(new Error(`上传失败: ${error.errMsg ?? '网络错误'}`))
						}
					})
				})
			})
			
			try {
				const imageUrls = await Promise.all(uploadPromises)
				console.log('所有整改图片上传成功，返回URL数量:', imageUrls.length, " at pages/record/detail.uvue:934")
				console.log('上传成功的图片URLs:', imageUrls, " at pages/record/detail.uvue:935")
				return imageUrls
			} catch (error) {
				console.error('整改图片上传失败:', error, " at pages/record/detail.uvue:938")
				throw error
			}
		},
		
		formatDate(dateStr: string | null): string {
		if (dateStr == null || dateStr == '') return ''
		// 如果是完整的日期时间格式，只取日期部分
		if (dateStr.includes(' ')) {
			return dateStr.split(' ')[0]
		}
		// 如果是ISO格式，转换为日期
		if (dateStr.includes('T')) {
			return dateStr.split('T')[0]
		}
		return dateStr
		},
		
		getSeverityClass(severity: number): string {
			switch (severity) {
				case 1: return 'severity-low'
				case 2: return 'severity-medium'
				case 3: return 'severity-high'
				case 4: return 'severity-urgent'
				default: return 'severity-unknown'
			}
		},
					
		// 初始化选中值（数据加载完成后调用）
		initSelectedValues() {
			console.log('开始初始化选中值', " at pages/record/detail.uvue:968")
			if (this.recordDetail == null) {
				console.log('recordDetail为空，无法初始化选中值', " at pages/record/detail.uvue:970")
				return
			}
			
			console.log('记录详情数据:', this.recordDetail, " at pages/record/detail.uvue:974")
			console.log('字典数据状态:', {
				natureOptions: this.natureOptions,
				professionOptions: this.professionOptions,
				departmentOptions: this.departmentOptions
			}, " at pages/record/detail.uvue:975")
					
			// 回写性质界定（支持ID或名称匹配）
			const natureId = this.recordDetail.natureId ?? ''
			const natureName = this.recordDetail.natureName ?? ''
			console.log('性质界定数据:', { natureId, natureName }, " at pages/record/detail.uvue:984")
			
			if (natureId !== '') {
				// 有ID值，使用ID匹配
				console.log('使用ID匹配:', natureId, " at pages/record/detail.uvue:988")
				for (let i = 0; i < this.natureOptions.length; i++) {
					console.log(`性质界定ID比较: ${this.natureOptions[i].id} == ${natureId}`, " at pages/record/detail.uvue:990")
					if (this.natureOptions[i].id == natureId) {
						this.selectedNatureIndex = i
						this.recordDetail.natureName = this.natureOptions[i].name
						console.log('性质界定ID匹配成功，索引:', i, 'ID:', this.natureOptions[i].id, '名称:', this.natureOptions[i].name, " at pages/record/detail.uvue:994")
						break
					}
				}
				if (this.selectedNatureIndex === -1) {
					console.log('性质界定ID在字典中未找到匹配项:', natureId, " at pages/record/detail.uvue:999")
				}
			} else if (natureName !== '') {
				// 没有ID但有名称，使用名称匹配
				console.log('使用名称匹配:', natureName, " at pages/record/detail.uvue:1003")
				for (let i = 0; i < this.natureOptions.length; i++) {
					console.log(`性质界定名称比较: ${this.natureOptions[i].name} == ${natureName}`, " at pages/record/detail.uvue:1005")
					if (this.natureOptions[i].name == natureName) {
						this.selectedNatureIndex = i
						this.recordDetail.natureId = this.natureOptions[i].id
						console.log('性质界定名称匹配成功，索引:', i, 'ID:', this.natureOptions[i].id, '名称:', this.natureOptions[i].name, " at pages/record/detail.uvue:1009")
						break
					}
				}
				if (this.selectedNatureIndex === -1) {
					console.log('性质界定名称在字典中未找到匹配项:', natureName, " at pages/record/detail.uvue:1014")
				}
			} else {
				console.log('性质界定ID和名称都为空，跳过匹配', " at pages/record/detail.uvue:1017")
			}
			
			if (this.selectedNatureIndex === -1) {
				console.log('可用的字典选项:', this.natureOptions.map(item => `${item.name}(${item.id})`), " at pages/record/detail.uvue:1021")
			}
					
			// 回写专业（使用字符串类型ID）
			const professionId = this.recordDetail.professionId ?? ''
			console.log('专业ID:', professionId, " at pages/record/detail.uvue:1026")
			if (professionId !== '') {
				for (let i = 0; i < this.professionOptions.length; i++) {
					console.log(`专业比较: ${this.professionOptions[i].id} == ${professionId}`, " at pages/record/detail.uvue:1029")
					if (this.professionOptions[i].id == professionId) {
						this.selectedProfessionIndex = i
						console.log('专业匹配成功，索引:', i, '名称:', this.professionOptions[i].name, " at pages/record/detail.uvue:1032")
						break
					}
				}
			} else {
				console.log('专业ID为空，跳过匹配', " at pages/record/detail.uvue:1037")
			}
					
			// 回写部门（使用字符串类型ID）
			const departmentId = this.recordDetail.departmentId ?? ''
			console.log('部门ID:', departmentId, " at pages/record/detail.uvue:1042")
			if (departmentId !== '') {
				for (let i = 0; i < this.departmentOptions.length; i++) {
					console.log(`部门比较: ${this.departmentOptions[i].id} == ${departmentId}`, " at pages/record/detail.uvue:1045")
					if (this.departmentOptions[i].id == departmentId) {
						this.selectedDepartmentIndex = i
						console.log('部门匹配成功，索引:', i, '名称:', this.departmentOptions[i].name, " at pages/record/detail.uvue:1048")
						break
					}
				}
			} else {
				console.log('部门ID为空，跳过匹配', " at pages/record/detail.uvue:1053")
			}
					
			console.log('回写选中值完成:', {
				natureId, selectedNatureIndex: this.selectedNatureIndex,
				professionId, selectedProfessionIndex: this.selectedProfessionIndex,
				departmentId, selectedDepartmentIndex: this.selectedDepartmentIndex
			}, " at pages/record/detail.uvue:1056")
		},
					
		// 加载性质界定字典数据
		loadNatureOptions() {
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
						console.log('性质界定数据加载成功:', this.natureOptions, " at pages/record/detail.uvue:1089")
					} else {
						const message = data.getString('message') ?? '未知错误'
						console.error('获取性质界定数据失败:', message, " at pages/record/detail.uvue:1092")
						this.natureOptions = [
							{ id: '1', name: '一般问题' },
							{ id: '2', name: '较大问题' },
							{ id: '3', name: '严重问题' },
							{ id: '4', name: '重大问题' }
						]
					}
					// 无论成功还是失败，都调用数据加载完成检查
					this.checkAllDataLoaded()
				},
				fail: (error) => {
					console.error('请求性质界定数据失败:', error, " at pages/record/detail.uvue:1104")
					this.natureOptions = [
						{ id: '1', name: '一般问题' },
						{ id: '2', name: '较大问题' },
						{ id: '3', name: '严重问题' },
						{ id: '4', name: '重大问题' }
					]
					// 加载失败也要调用数据加载完成检查
					this.checkAllDataLoaded()
				}
			})
		},
					
		// 加载专业字典数据
		loadProfessionOptions() {
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
						console.log('专业数据加载成功:', this.professionOptions, " at pages/record/detail.uvue:1143")
					} else {
						const message = data.getString('message') ?? '未知错误'
						console.error('获取专业数据失败:', message, " at pages/record/detail.uvue:1146")
						this.professionOptions = [
							{ id: '1', name: '机械' },
							{ id: '2', name: '电气' },
							{ id: '3', name: '仪表' },
							{ id: '4', name: '工艺' },
							{ id: '5', name: '安全' },
							{ id: '6', name: '环保' }
						]
					}
					// 无论成功还是失败，都调用数据加载完成检查
					this.checkAllDataLoaded()
				},
				fail: (error) => {
					console.error('请求专业数据失败:', error, " at pages/record/detail.uvue:1160")
					this.professionOptions = [
						{ id: '1', name: '机械' },
						{ id: '2', name: '电气' },
						{ id: '3', name: '仪表' },
						{ id: '4', name: '工艺' },
						{ id: '5', name: '安全' },
						{ id: '6', name: '环保' }
					]
					// 加载失败也要调用数据加载完成检查
					this.checkAllDataLoaded()
				}
			})
		},
					
		// 加载部门字典数据
		loadDepartments() {
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
						console.log('部门数据加载成功:', this.departmentOptions, " at pages/record/detail.uvue:1201")
					} else {
						const message = data.getString('message') ?? '未知错误'
						console.error('获取部门数据失败:', message, " at pages/record/detail.uvue:1204")
						this.departmentOptions = [
							{ id: '1', name: '生产部' },
							{ id: '2', name: '安全部' },
							{ id: '3', name: '设备部' },
							{ id: '4', name: '技术部' },
							{ id: '5', name: '质量部' },
							{ id: '6', name: '环保部' },
							{ id: '7', name: '行政部' }
						]
					}
					// 无论成功还是失败，都调用数据加载完成检查
					this.checkAllDataLoaded()
				},
				fail: (error) => {
					console.error('请求部门数据失败:', error, " at pages/record/detail.uvue:1219")
					this.departmentOptions = [
						{ id: '1', name: '生产部' },
						{ id: '2', name: '安全部' },
						{ id: '3', name: '设备部' },
						{ id: '4', name: '技术部' },
						{ id: '5', name: '质量部' },
						{ id: '6', name: '环保部' },
						{ id: '7', name: '行政部' }
					]
					// 加载失败也要调用数据加载完成检查
					this.checkAllDataLoaded()
				}
			})
		},
					
		// 性质界定选择器方法
		showNaturePicker() {
			if (this.natureOptions.length > 0) {
				this.showNaturePopup = true
				console.log('显示性质界定选择弹窗', " at pages/record/detail.uvue:1239")
			}
		},
					
		hideNaturePicker() {
			this.showNaturePopup = false
			console.log('隐藏性质界定选择弹窗', " at pages/record/detail.uvue:1245")
		},
					
		selectNature(index: number) {
			console.log('选择性质界定，索引:', index, " at pages/record/detail.uvue:1249")
			this.selectedNatureIndex = index
			this.hideNaturePicker()
		},
					
		// 专业选择器方法
		showProfessionPicker() {
			if (this.professionOptions.length > 0) {
				this.showProfessionPopup = true
				console.log('显示专业选择弹窗', " at pages/record/detail.uvue:1258")
			}
		},
					
		hideProfessionPicker() {
			this.showProfessionPopup = false
			console.log('隐藏专业选择弹窗', " at pages/record/detail.uvue:1264")
		},
					
		selectProfession(index: number) {
			console.log('选择专业，索引:', index, " at pages/record/detail.uvue:1268")
			this.selectedProfessionIndex = index
			this.hideProfessionPicker()
		},
					
		// 部门选择器方法
		showDepartmentPicker() {
			if (this.departmentOptions.length > 0) {
				this.showDepartmentPopup = true
				console.log('显示部门选择弹窗', " at pages/record/detail.uvue:1277")
			}
		},
					
		hideDepartmentPicker() {
			this.showDepartmentPopup = false
			console.log('隐藏部门选择弹窗', " at pages/record/detail.uvue:1283")
		},
					
		selectDepartment(index: number) {
			console.log('选择部门，索引:', index, " at pages/record/detail.uvue:1287")
			this.selectedDepartmentIndex = index
			this.hideDepartmentPicker()
		}
	}
})

export default __sfc__
function GenPagesRecordDetailRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
  return _cE("scroll-view", _uM({
    class: "container",
    "scroll-y": "true",
    "scroll-top": _ctx.scrollTop,
    style: _nS(_uM({height: _ctx.containerHeight + 'px'})),
    "enable-back-to-top": "true"
  }), [
    isTrue(_ctx.loading)
      ? _cE("view", _uM({
          key: 0,
          class: "loading-state"
        }), [
          _cE("text", _uM({ class: "loading-text" }), "加载中...")
        ])
      : isTrue(_ctx.recordDetail)
        ? _cE("view", _uM({
            key: 1,
            class: "detail-content"
          }), [
            _cE("view", _uM({ class: "status-header" }), [
              _cE("text", _uM({ class: "page-title" }), "问题详情"),
              _cE("view", _uM({
                class: _nC(["record-status", _ctx.getStatusClass(_ctx.recordDetail?.status ?? 0)])
              }), [
                _cE("text", null, _tD(_ctx.getStatusText(_ctx.recordDetail?.status ?? 0)), 1 /* TEXT */)
              ], 2 /* CLASS */)
            ]),
            _ctx.imageList.length > 0
              ? _cE("view", _uM({
                  key: 0,
                  class: "form-item"
                }), [
                  _cE("text", _uM({ class: "form-label" }), "📷 问题照片 (" + _tD(_ctx.imageList.length) + "张)", 1 /* TEXT */),
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
                            onClick: () => {_ctx.previewImage(index)},
                            onError: _ctx.onImageError,
                            onLoad: _ctx.onImageLoad
                          }), null, 40 /* PROPS, NEED_HYDRATION */, ["src", "onClick", "onError", "onLoad"])
                        ])
                      }), 128 /* KEYED_FRAGMENT */)
                    ])
                  ])
                ])
              : _cC("v-if", true),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "问题描述"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.problemDescription ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "责任单位"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.responsibleUnit ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "责任人"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.responsiblePerson ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "性质界定"),
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
                  key: 1,
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
              _cE("text", _uM({ class: "form-label" }), "考核情况"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.assessmentSituation ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "考核金额"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.assessmentAmount ?? '') + "元", 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "安全记分"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.safetyScore ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "专业"),
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
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.location ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "检查人"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.inspector ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "检查组组长"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.groupLeader ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "检查组副组长"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.deputyLeader ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "所属部门"),
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
                  key: 3,
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
              _cE("text", _uM({ class: "form-label" }), "整改期限"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.formatDate(_ctx.recordDetail?.rectificationDate) ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "整改效果描述"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("textarea", _uM({
                  modelValue: _ctx.rectificationEffect,
                  onInput: ($event: UniInputEvent) => {(_ctx.rectificationEffect) = $event.detail.value},
                  class: "form-textarea",
                  placeholder: "请描述整改效果...",
                  maxlength: "500",
                  "show-confirm-bar": "false"
                }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "📋 整改状态"),
              _cE("view", _uM({ class: "status-selector" }), [
                _cE("view", _uM({
                  class: _nC(["status-option pending", _uM({active: _ctx.rectificationStatus === 0})]),
                  onClick: () => {_ctx.setRectificationStatus(0)}
                }), " ⏳ 未整改 ", 10 /* CLASS, PROPS */, ["onClick"]),
                _cE("view", _uM({
                  class: _nC(["status-option completed", _uM({active: _ctx.rectificationStatus === 1})]),
                  onClick: () => {_ctx.setRectificationStatus(1)}
                }), " ✅ 已整改 ", 10 /* CLASS, PROPS */, ["onClick"])
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "📷 整改图片 (" + _tD(_ctx.rectificationImages.length) + "/3)", 1 /* TEXT */),
              _cE("view", _uM({ class: "image-upload" }), [
                _cE("view", _uM({ class: "image-list" }), [
                  _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectificationImages, (image, index, __index, _cached): any => {
                    return _cE("view", _uM({
                      class: "image-item",
                      key: index
                    }), [
                      _cE("image", _uM({
                        src: image,
                        mode: "aspectFill",
                        class: "uploaded-image",
                        onClick: () => {_ctx.previewRectificationImage(index)},
                        onError: _ctx.onImageError,
                        onLoad: _ctx.onImageLoad
                      }), null, 40 /* PROPS, NEED_HYDRATION */, ["src", "onClick", "onError", "onLoad"]),
                      _cE("view", _uM({
                        class: "image-delete",
                        onClick: () => {_ctx.removeRectificationImage(index)}
                      }), [
                        _cE("text", null, "×")
                      ], 8 /* PROPS */, ["onClick"])
                    ])
                  }), 128 /* KEYED_FRAGMENT */),
                  _ctx.rectificationImages.length < 3
                    ? _cE("view", _uM({
                        key: 0,
                        class: "image-add",
                        onClick: _ctx.chooseRectificationImage
                      }), [
                        _cE("text", _uM({ class: "add-icon" }), "+"),
                        _cE("text", _uM({ class: "add-text" }), "添加图片")
                      ], 8 /* PROPS */, ["onClick"])
                    : _cC("v-if", true)
                ])
              ])
            ]),
            _ctx.rectificationImages.length > 0
              ? _cE("view", _uM({
                  key: 4,
                  class: "form-item"
                }), [
                  _cE("text", _uM({ class: "form-label" }), "🔍 整改图片调试信息"),
                  _cE("view", _uM({ class: "form-input-wrapper" }), [
                    _cE("view", _uM({ class: "form-display-text" }), [
                      _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectificationImages, (image, index, __index, _cached): any => {
                        return _cE("text", _uM({ key: index }), " 整改图片" + _tD(index + 1) + ": " + _tD(image) + "\n ", 1 /* TEXT */)
                      }), 128 /* KEYED_FRAGMENT */)
                    ])
                  ])
                ])
              : _cC("v-if", true),
            _cE("view", _uM({ class: "save-button-wrapper" }), [
              _cE("button", _uM({
                class: "save-btn",
                onClick: _ctx.saveRectificationInfo,
                disabled: _ctx.saving
              }), _tD(_ctx.saving ? '保存中...' : '保存整改信息'), 9 /* TEXT, PROPS */, ["onClick", "disabled"])
            ])
          ])
        : _cE("view", _uM({
            key: 2,
            class: "error-state"
          }), [
            _cE("text", _uM({ class: "error-text" }), "记录不存在或已被删除")
          ])
  ], 12 /* STYLE, PROPS */, ["scroll-top"])
}
const GenPagesRecordDetailStyles = [_uM([["container", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 120], ["paddingLeft", 20], ["backgroundColor", "#f5f5f5"]]))], ["loading-state", _pS(_uM([["textAlign", "center"], ["paddingTop", 60], ["paddingRight", 0], ["paddingBottom", 60], ["paddingLeft", 0]]))], ["loading-text", _pS(_uM([["fontSize", 16], ["color", "#666666"]]))], ["status-header", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["paddingTop", 15], ["paddingRight", 15], ["paddingBottom", 15], ["paddingLeft", 15], ["marginBottom", 15], ["boxShadow", "0 1px 3px rgba(0,0,0,0.1)"], ["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"]]))], ["page-title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#333333"]]))], ["record-status", _pS(_uM([["paddingTop", 6], ["paddingRight", 12], ["paddingBottom", 6], ["paddingLeft", 12], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["fontSize", 14], ["fontWeight", "bold"]]))], ["status-pending", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#fff3cd"], ["color", "#856404"]]))], ["status-processing", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#d1ecf1"], ["color", "#0c5460"]]))], ["status-completed", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#d4edda"], ["color", "#155724"]]))], ["status-accepted", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#e2e3e5"], ["color", "#383d41"]]))], ["severity-low", _pS(_uM([["color", "#28a745"], ["fontWeight", "bold"]]))], ["severity-medium", _pS(_uM([["color", "#ffc107"], ["fontWeight", "bold"]]))], ["severity-high", _pS(_uM([["color", "#fd7e14"], ["fontWeight", "bold"]]))], ["severity-urgent", _pS(_uM([["color", "#dc3545"], ["fontWeight", "bold"]]))], ["error-state", _pS(_uM([["textAlign", "center"], ["paddingTop", 60], ["paddingRight", 0], ["paddingBottom", 60], ["paddingLeft", 0]]))], ["error-text", _pS(_uM([["fontSize", 16], ["color", "#666666"]]))], ["form-item", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["paddingTop", 15], ["paddingRight", 15], ["paddingBottom", 15], ["paddingLeft", 15], ["marginBottom", 15], ["boxShadow", "0 1px 3px rgba(0,0,0,0.1)"]]))], ["form-label", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["marginBottom", 10], ["fontWeight", "400"]]))], ["form-input-wrapper", _pS(_uM([["position", "relative"]]))], ["form-display-text", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["backgroundImage", "none"], ["backgroundColor", "#f8f9fa"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["minHeight", 20], ["wordBreak", "break-all"]]))], ["form-textarea", _pS(_uM([["width", "100%"], ["minHeight", 100], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["backgroundImage", "none"], ["backgroundColor", "#f8f9fa"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["fontSize", 16], ["color", "#333333"], ["boxSizing", "border-box"], ["borderTopColor:focus", "#007AFF"], ["borderRightColor:focus", "#007AFF"], ["borderBottomColor:focus", "#007AFF"], ["borderLeftColor:focus", "#007AFF"], ["outline:focus", "none"]]))], ["image-upload", _pS(_uM([["marginTop", 10]]))], ["image-list", _pS(_uM([["display", "flex"], ["flexWrap", "wrap"], ["gap", "10px"]]))], ["image-item", _pS(_uM([["position", "relative"], ["width", 80], ["height", 80]]))], ["uploaded-image", _pS(_uM([["width", "100%"], ["height", "100%"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e0e0e0"], ["borderRightColor", "#e0e0e0"], ["borderBottomColor", "#e0e0e0"], ["borderLeftColor", "#e0e0e0"]]))], ["image-delete", _pS(_uM([["position", "absolute"], ["top", -5], ["right", -5], ["width", 20], ["height", 20], ["backgroundImage", "none"], ["backgroundColor", "#ff4757"], ["color", "#FFFFFF"], ["borderTopLeftRadius", 10], ["borderTopRightRadius", 10], ["borderBottomRightRadius", 10], ["borderBottomLeftRadius", 10], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["fontSize", 12], ["cursor", "pointer"]]))], ["image-add", _pS(_uM([["width", 80], ["height", 80], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "dashed"], ["borderRightStyle", "dashed"], ["borderBottomStyle", "dashed"], ["borderLeftStyle", "dashed"], ["borderTopColor", "#dddddd"], ["borderRightColor", "#dddddd"], ["borderBottomColor", "#dddddd"], ["borderLeftColor", "#dddddd"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"], ["backgroundImage", "none"], ["backgroundColor", "#fafafa"]]))], ["add-icon", _pS(_uM([["fontSize", 24], ["color", "#999999"], ["marginBottom", 5]]))], ["add-text", _pS(_uM([["fontSize", 12], ["color", "#999999"]]))], ["status-selector", _pS(_uM([["display", "flex"], ["flexDirection", "row"], ["gap", 12], ["marginTop", 15], ["paddingTop", 0], ["paddingRight", 5], ["paddingBottom", 0], ["paddingLeft", 5], ["flexWrap", "nowrap"], ["alignItems", "center"], ["justifyContent", "space-between"]]))], ["status-option", _uM([["", _uM([["flex", 1], ["height", 60], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e8e8e8"], ["borderRightColor", "#e8e8e8"], ["borderBottomColor", "#e8e8e8"], ["borderLeftColor", "#e8e8e8"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["backgroundImage", "linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxShadow", "0 2px 8px rgba(0, 0, 0, 0.08)"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "cubic-bezier(0.4,0,0.2,1)"], ["cursor", "pointer"], ["position", "relative"], ["overflow", "hidden"], ["marginTop", 0], ["marginRight", 6], ["marginBottom", 0], ["marginLeft", 6], ["fontSize", 14], ["color", "#333333"], ["textAlign", "center"], ["content::before", "''"], ["position::before", "absolute"], ["top::before", 0], ["left::before", 0], ["right::before", 0], ["bottom::before", 0], ["backgroundImage::before", "linear-gradient(135deg, rgba(255, 255, 255, 0.3) 0%, rgba(255, 255, 255, 0.1) 100%)"], ["backgroundColor::before", "rgba(0,0,0,0)"], ["opacity::before", 0], ["transitionProperty::before", "opacity"], ["transitionDuration::before", "0.3s"], ["transitionTimingFunction::before", "ease"], ["opacity:hover::before", 1]])], [".pending", _uM([["borderTopColor", "#ffa726"], ["borderRightColor", "#ffa726"], ["borderBottomColor", "#ffa726"], ["borderLeftColor", "#ffa726"], ["backgroundImage", "none"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#e67e22"]])], [".pending.active", _uM([["borderTopColor", "#ff9800"], ["borderRightColor", "#ff9800"], ["borderBottomColor", "#ff9800"], ["borderLeftColor", "#ff9800"], ["backgroundImage", "linear-gradient(135deg, #ff9800 0%, #ffa726 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxShadow", "0 4px 15px rgba(255, 152, 0, 0.3)"], ["transform", "translateY(-2px)"], ["color", "#FFFFFF"]])], [".completed", _uM([["borderTopColor", "#66bb6a"], ["borderRightColor", "#66bb6a"], ["borderBottomColor", "#66bb6a"], ["borderLeftColor", "#66bb6a"], ["backgroundImage", "none"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#27ae60"]])], [".completed.active", _uM([["borderTopColor", "#4caf50"], ["borderRightColor", "#4caf50"], ["borderBottomColor", "#4caf50"], ["borderLeftColor", "#4caf50"], ["backgroundImage", "linear-gradient(135deg, #4caf50 0%, #66bb6a 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxShadow", "0 4px 15px rgba(76, 175, 80, 0.3)"], ["transform", "translateY(-2px)"], ["color", "#FFFFFF"]])]])], ["save-button-wrapper", _pS(_uM([["position", "fixed"], ["bottom", 0], ["left", 0], ["right", 0], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderTopWidth", 1], ["borderTopStyle", "solid"], ["borderTopColor", "#eeeeee"]]))], ["save-btn", _pS(_uM([["width", "100%"], ["height", 44], ["backgroundImage", "none"], ["backgroundColor", "#007AFF"], ["color", "#FFFFFF"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["fontSize", 16], ["fontWeight", "bold"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["backgroundImage:disabled", "none"], ["backgroundColor:disabled", "#f8f9fa"], ["color:disabled", "#6c757d"], ["borderTopWidth:disabled", 1], ["borderRightWidth:disabled", 1], ["borderBottomWidth:disabled", 1], ["borderLeftWidth:disabled", 1], ["borderTopStyle:disabled", "solid"], ["borderRightStyle:disabled", "solid"], ["borderBottomStyle:disabled", "solid"], ["borderLeftStyle:disabled", "solid"], ["borderTopColor:disabled", "#dee2e6"], ["borderRightColor:disabled", "#dee2e6"], ["borderBottomColor:disabled", "#dee2e6"], ["borderLeftColor:disabled", "#dee2e6"], ["opacity:disabled", 0.6]]))], ["custom-picker-full", _uM([["", _uM([["width", "100%"], ["height", 44], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["backgroundImage", "none"], ["backgroundColor", "#f8f9fa"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "space-between"], ["boxSizing", "border-box"], ["cursor", "pointer"]])], [".disabled", _uM([["backgroundImage", "none"], ["backgroundColor", "#f5f5f5"], ["color", "#999999"], ["cursor", "not-allowed"]])]])], ["picker-text-full", _uM([["", _uM([["fontSize", 16], ["color", "#333333"], ["flex", 1]])], [".placeholder", _uM([["color", "#999999"]])]])], ["popup-overlay", _pS(_uM([["position", "fixed"], ["top", 0], ["left", 0], ["width", "100%"], ["height", "100%"], ["backgroundImage", "none"], ["backgroundColor", "rgba(0,0,0,0.5)"], ["zIndex", 1000], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["popup-content", _pS(_uM([["width", "80%"], ["maxWidth", 400], ["backgroundImage", "none"], ["backgroundColor", "#FFFFFF"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["overflow", "hidden"]]))], ["popup-header", _pS(_uM([["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "space-between"]]))], ["popup-title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#333333"]]))], ["popup-close", _pS(_uM([["fontSize", 24], ["color", "#999999"], ["cursor", "pointer"], ["lineHeight", 1]]))], ["popup-list", _pS(_uM([["maxHeight", 300], ["overflowY", "auto"]]))], ["popup-item", _pS(_uM([["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f8f9fa"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "space-between"], ["cursor", "pointer"], ["backgroundImage:hover", "none"], ["backgroundColor:hover", "#f8f9fa"], ["borderBottomWidth:last-child", "medium"], ["borderBottomStyle:last-child", "none"], ["borderBottomColor:last-child", "#000000"]]))], ["popup-item-text", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["flex", 1]]))], ["popup-item-check", _pS(_uM([["fontSize", 18], ["color", "#007AFF"], ["fontWeight", "bold"]]))], ["@TRANSITION", _uM([["status-option", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "cubic-bezier(0.4,0,0.2,1)"], ["property::before", "opacity"], ["duration::before", "0.3s"], ["timingFunction::before", "ease"]])]])]])]
