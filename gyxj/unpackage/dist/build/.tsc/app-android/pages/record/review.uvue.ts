
	import { getApiUrl, apiConfig } from '@/config/api.uts'
	
	type NatureOption = {
		id: string
		name: string
	}
	
	type ProfessionOption = {
		id: string
		name: string
	}
	
	type DepartmentOption = {
		id: string
		name: string
	}
	
	type RecordDetail = {
		id: string
		problemDescription: string
		responsibleUnit: string
		responsiblePerson: string
		natureId: string  // 修改为string类型
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
		reviewEffect: string
		reviewImageUrls: string
	}

	const __sfc__ = defineComponent({
		data() {
			return {
				recordId: '',
				recordDetail: null as RecordDetail | null,
				imageList: [] as Array<string>,
				rectificationImages: [] as Array<string>,
				loading: true,
				reviewEffect: '',
				reviewImages: [] as Array<string>,
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
				totalDataCount: 4, // 总共需要加载4类数据
				// 延迟整改模式标识
				isDelayed: false,
				// 已复查记录标识
				isReviewed: false
			}
		},
		onLoad(options: UTSJSONObject) {
			this.recordId = options.getString('id') ?? ''
			// 检查是否为延迟整改模式
			const delayedParam = options.getString('delayed')
			console.log('URL参数 delayed:', delayedParam)
			console.log('参数类型:', typeof delayedParam)
			// 直接判断参数是否存在且为'true'
			if (delayedParam != null && delayedParam == 'true') {
				this.isDelayed = true
				console.log('设置为延迟整改模式')
			} else {
				this.isDelayed = false
				console.log('设置为正常模式')
			}
			console.log('最终 isDelayed 值:', this.isDelayed)
			
			// 检查是否为已复查记录模式
			const reviewedParam = options.getString('reviewed')
			console.log('URL参数 reviewed:', reviewedParam)
			if (reviewedParam != null && reviewedParam == 'true') {
				this.isReviewed = true
				console.log('设置为已复查记录模式')
			} else {
				this.isReviewed = false
				console.log('设置为正常模式')
			}
			console.log('最终 isReviewed 值:', this.isReviewed)
			console.log('完整URL参数:', options)
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
				console.log(`数据加载进度: ${this.loadedDataCount}/${this.totalDataCount}`)
				
				if (this.loadedDataCount >= this.totalDataCount) {
					console.log('所有数据加载完成，开始回写选中值')
					console.log('当前数据状态:', {
						recordDetail: this.recordDetail,
						natureOptionsLength: this.natureOptions.length,
						professionOptionsLength: this.professionOptions.length,
						departmentOptionsLength: this.departmentOptions.length,
						natureOptions: this.natureOptions,
						professionOptions: this.professionOptions,
						departmentOptions: this.departmentOptions
					})
					// 所有数据加载完成，延迟100ms回写选中值，确保数据已经更新到界面
					setTimeout(() => {
						console.log('开始执行 initSelectedValues')
						this.initSelectedValues()
						this.loading = false
						console.log('加载状态设置为 false')
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
							console.log('后端返回的完整数据:', recordData)
												
							// 打印所有与性质界定相关的字段
							console.log('性质界定相关字段调试:')
							console.log('natureDefinition:', recordData?.getString('natureDefinition'))
							console.log('natureId:', recordData?.getString('natureId'))
							console.log('natureName:', recordData?.getString('natureName'))
							console.log('nature:', recordData?.getString('nature'))
							console.log('natureType:', recordData?.getString('natureType'))
												
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
									rectificationTime: recordData.getString('rectificationTime') ?? '',
									reviewEffect: recordData.getString('reviewEffect') ?? '',
									reviewImageUrls: recordData.getString('reviewImageUrls') ?? ''
								} as RecordDetail
															
								// 处理性质界定字段映射
								const natureDefinitionValue = recordData.getString('natureDefinition') ?? ''
								console.log('natureDefinition原始值:', natureDefinitionValue)
								
								// 判断返回值是ID还是名称，然后正确映射
								// 如果是32位的ID格式，则作为ID处理；否则作为名称处理
								if (natureDefinitionValue.length === 32 && /^[a-f0-9]+$/i.test(natureDefinitionValue)) {
									// 这是ID，直接赋值
									this.recordDetail.natureId = natureDefinitionValue
									console.log('检测到ID格式，作为ID处理:', natureDefinitionValue)
								} else {
									// 这是名称，需要转换为ID
									this.recordDetail.natureName = natureDefinitionValue
									console.log('检测到名称格式，作为名称处理:', natureDefinitionValue)
									// natureId会在字典加载完成后通过名称匹配设置
								}
								
								console.log('数据映射结果调试:')
								console.log('recordDetail.natureId:', this.recordDetail.natureId)
								console.log('recordDetail.natureName:', this.recordDetail.natureName)
								
								// 回写复查信息到表单
								this.reviewEffect = this.recordDetail?.reviewEffect ?? ''
												
								// 处理问题图片
								const imageUrls = this.recordDetail?.problemImageUrls
								if (imageUrls != null && imageUrls != '') {
									this.imageList = this.processImageUrls(imageUrls as string)
								}
												
								// 处理整改图片
								const rectificationImages = this.recordDetail?.rectificationImageUrls
								console.log('调试：整改图片原始数据', rectificationImages)
								if (rectificationImages != null && rectificationImages != '') {
									this.rectificationImages = this.processImageUrls(rectificationImages as string)
									console.log('调试：最终的整改图片URL列表', this.rectificationImages)
								} else {
									console.log('调试：整改图片数据为空')
								}
								
								// 处理复查图片
								const reviewImages = this.recordDetail?.reviewImageUrls
								console.log('调试：复查图片原始数据', reviewImages)
								if (reviewImages != null && reviewImages != '') {
									this.reviewImages = this.processImageUrls(reviewImages as string)
									console.log('调试：最终的复查图片URL列表', this.reviewImages)
								} else {
									console.log('调试：复查图片数据为空')
								}
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
						console.error('加载记录详情失败:', error)
						uni.showToast({
							title: '网络错误',
							icon: 'none'
						})
						// 加载失败也要调用检查方法
						this.checkAllDataLoaded()
					}
				})
				},
				
				// 复查图片相关方法
				chooseReviewImage() {
					if (this.reviewImages.length >= 3) {
						uni.showToast({
							title: '最多只能上传3张图片',
							icon: 'none'
						})
						return
					}
					
					uni.chooseImage({
						count: 3 - this.reviewImages.length,
						sourceType: ['camera', 'album'],
						success: (res) => {
							const tempFilePaths = res.tempFilePaths
							for (let i = 0; i < tempFilePaths.length; i++) {
								this.reviewImages.push(tempFilePaths[i])
							}
						}
					})
				},
				
				previewReviewImage(index: number) {
					uni.previewImage({
						current: index,
						urls: this.reviewImages
					})
				},
				
				removeReviewImage(index: number) {
					this.reviewImages.splice(index, 1)
				},
							
				previewImage(index: number) {
					uni.previewImage({
						current: index,
						urls: this.imageList
					})
				},
							
				previewRectificationImage(index: number) {
					uni.previewImage({
						current: index,
						urls: this.rectificationImages
					})
				},
							
				formatDate(dateTimeStr: string): string {
					if (dateTimeStr == null || dateTimeStr == '') return ''
					// 如果是完整的日期时间格式，只取日期部分
					if (dateTimeStr.includes(' ')) {
						return dateTimeStr.split(' ')[0]
					}
					return dateTimeStr
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
				
				convertToHttpUrl(relativePath: string): string {
					if (relativePath == null || relativePath == '' || relativePath.trim() === '') {
						return ''
					}
					if (relativePath.startsWith('http://') || relativePath.startsWith('https://')) {
						return relativePath
					}
					const fullUrl = `${apiConfig.baseUrl}/api${relativePath}`
					return fullUrl
				},
				
				// 处理图片URL数组，支持JSON数组格式和逗号分隔字符串格式
      processImageUrls(imageUrls: string): string[] {
       if (imageUrls == null || imageUrls.trim() === '') {
						return []
					}
					
					try {
						// 尝试解析为JSON数组
						if (imageUrls.trim().startsWith('[') && imageUrls.trim().endsWith(']')) {
							const parsed = JSON.parse(imageUrls) as string[]
							return parsed.map(url => this.convertToHttpUrl(url)).filter(url => url !== '')
						}
					} catch (e) {
						console.log('JSON解析失败，尝试按逗号分隔处理:', e)
					}
					
					// 按逗号分隔处理
					return imageUrls.split(',')
						.map(url => url.trim())
						.filter(url => url !== '')
						.map(url => this.convertToHttpUrl(url))
				},
				
				async saveReviewInfo() {
					if (this.saving) return
					
					if (this.reviewEffect.trim().length === 0) {
						uni.showToast({
							title: '请填写复查效果',
							icon: 'none'
						})
						return
					}
					
					this.saving = true
					
					try {
						let reviewImageUrls: string[] = []
									
						// 上传复查图片
						if (this.reviewImages.length > 0) {
							uni.showLoading({
								title: '上传复查图片中...'
							})
							reviewImageUrls = await this.uploadReviewImages()
							uni.hideLoading()
						}
						
						// 获取当前登录用户信息
			const userInfoStr = uni.getStorageSync('userInfo') as string
			console.log('获取到的用户信息字符串:', userInfoStr)
			let currentUserId = ''
			if (userInfoStr != null && userInfoStr != '') {
				try {
					const userInfo = JSON.parse(userInfoStr) as UTSJSONObject
					console.log('解析后的用户信息对象:', userInfo)
					currentUserId = userInfo.getString('id') ?? ''
					console.log('当前复查人ID:', currentUserId)
					if (currentUserId === '') {
						console.log('用户信息对象:', JSON.stringify(userInfo))
					}
				} catch (e) {
					console.error('解析用户信息失败:', e)
				}
			} else {
				console.log('用户信息字符串为空或null')
			}
				
				const updateData = {
					id: this.recordDetail?.id ?? '',
					problemDescription: this.recordDetail?.problemDescription ?? '',
					responsibleUnit: this.recordDetail?.responsibleUnit ?? '',
					responsiblePerson: this.recordDetail?.responsiblePerson ?? '',
					// 性质界定：保存当前选中的名称（与数据库存储格式一致）
					natureDefinition: this.selectedNatureIndex !== -1 ? this.natureOptions[this.selectedNatureIndex].name : (this.recordDetail?.natureId ?? ''),
					rectificationEffect: this.recordDetail?.rectificationEffect ?? '',
					rectificationImageUrls: this.recordDetail?.rectificationImageUrls ?? '',
					reviewEffect: this.reviewEffect,
					reviewImageUrls: reviewImageUrls.join(','),
					reviewerId: currentUserId, // 设置复查人ID
					status: 2,
					reviewTime: new Date().toISOString().replace('T', ' ').substring(0, 19)
				}
						
						uni.showLoading({
							title: '保存中...'
						})
						
                        const token = uni.getStorageSync('token')
                        // 统一为Web端复查接口
                        const payload = {
                            reviewSuggestion: this.reviewEffect,
                            reviewEffect: this.reviewEffect,
                            reviewImages: reviewImageUrls.join(',')
                        }
                        uni.request({
                            url: getApiUrl(`/monthly-inspection-record/${this.recordDetail?.id ?? ''}/review`),
                            method: 'PUT',
                            header: {
                                'Authorization': `Bearer ${token}`,
                                'Content-Type': 'application/json'
                            },
                            data: payload,
                            success: (response) => {
                                uni.hideLoading()
                                const data = response.data as UTSJSONObject
                                if (response.statusCode === 200 && (data.getNumber('code') ?? 0) === 200) {
                                    uni.showToast({
                                        title: '复查成功',
                                        icon: 'success'
                                    })
                                    setTimeout(() => {
                                        uni.navigateBack()
                                    }, 1500)
                                } else {
                                    uni.showToast({
                                        title: data.getString('message') ?? '复查失败',
                                        icon: 'none'
                                    })
                                }
                                this.saving = false
                            },
							fail: (error) => {
								uni.hideLoading()
								console.error('保存复查信息失败:', error)
								uni.showToast({
									title: '网络错误',
									icon: 'none'
								})
								this.saving = false
							}
						})
					} catch (error) {
						uni.hideLoading()
						console.error('上传图片失败:', error)
						uni.showToast({
							title: '图片上传失败，请重试',
							icon: 'none'
						})
						this.saving = false
					}
				},
				
				async uploadReviewImages(): Promise<string[]> {
					if (this.reviewImages.length === 0) {
						return []
					}
					
					const token = uni.getStorageSync('token')
					const uploadPromises = this.reviewImages.map(imagePath => {
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
										const data = JSON.parse(res.data) as UTSJSONObject
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
						console.log('所有复查图片上传成功：', imageUrls)
						return imageUrls
					} catch (error) {
						console.error('复查图片上传失败:', error)
						throw error
					}
				},
				
				getStatusText(status: number): string {
					switch (status) {
						case 0: return '待整改'
						case 1: return '已整改'
						case 2: return '已复查'
						default: return '未知'
					}
				},
				
				getStatusClass(status: number): string {
					switch (status) {
						case 0: return 'status-pending'
						case 1: return 'status-completed'
						case 2: return 'status-reviewed'
						default: return 'status-unknown'
					}
				},
							
				// 初始化选中值（数据加载完成后调用）
				initSelectedValues() {
					console.log('开始初始化选中值')
					if (this.recordDetail == null) {
						console.log('recordDetail为空，无法初始化选中值')
						return
					}
								
					console.log('记录详情数据:', this.recordDetail)
					console.log('字典数据状态:', {
						natureOptions: this.natureOptions,
						professionOptions: this.professionOptions,
						departmentOptions: this.departmentOptions
					})
								
					// 回写性质界定（支持ID或名称匹配）
					const natureId = this.recordDetail.natureId ?? ''
					const natureName = this.recordDetail.natureName ?? ''
					console.log('性质界定数据:', { natureId, natureName })
					
					if (natureId !== '') {
						// 有ID值，使用ID匹配
						console.log('使用ID匹配:', natureId)
						for (let i = 0; i < this.natureOptions.length; i++) {
							console.log(`性质界定ID比较: ${this.natureOptions[i].id} == ${natureId}`)
							if (this.natureOptions[i].id == natureId) {
								this.selectedNatureIndex = i
								this.recordDetail.natureName = this.natureOptions[i].name
								console.log('性质界定ID匹配成功，索引:', i, 'ID:', this.natureOptions[i].id, '名称:', this.natureOptions[i].name)
								break
							}
						}
						if (this.selectedNatureIndex === -1) {
							console.log('性质界定ID在字典中未找到匹配项:', natureId)
						}
					} else if (natureName !== '') {
						// 没有ID但有名称，使用名称匹配
						console.log('使用名称匹配:', natureName)
						for (let i = 0; i < this.natureOptions.length; i++) {
							console.log(`性质界定名称比较: ${this.natureOptions[i].name} == ${natureName}`)
							if (this.natureOptions[i].name == natureName) {
								this.selectedNatureIndex = i
								this.recordDetail.natureId = this.natureOptions[i].id
								console.log('性质界定名称匹配成功，索引:', i, 'ID:', this.natureOptions[i].id, '名称:', this.natureOptions[i].name)
								break
							}
						}
						if (this.selectedNatureIndex === -1) {
							console.log('性质界定名称在字典中未找到匹配项:', natureName)
						}
					} else {
						console.log('性质界定ID和名称都为空，跳过匹配')
					}
					
					if (this.selectedNatureIndex === -1) {
						console.log('可用的字典选项:', this.natureOptions.map(item => `${item.name}(${item.id})`))
					}
								
					// 回写专业（使用字符串类型ID）
					const professionId = this.recordDetail.professionId ?? ''
					console.log('专业ID:', professionId)
					if (professionId !== '') {
						for (let i = 0; i < this.professionOptions.length; i++) {
							console.log(`专业比较: ${this.professionOptions[i].id} == ${professionId}`)
							if (this.professionOptions[i].id == professionId) {
								this.selectedProfessionIndex = i
								console.log('专业匹配成功，索引:', i, '名称:', this.professionOptions[i].name)
								break
							}
						}
					} else {
						console.log('专业ID为空，跳过匹配')
					}
								
					// 回写部门（使用字符串类型ID）
					const departmentId = this.recordDetail.departmentId ?? ''
					console.log('部门ID:', departmentId)
					if (departmentId !== '') {
						for (let i = 0; i < this.departmentOptions.length; i++) {
							console.log(`部门比较: ${this.departmentOptions[i].id} == ${departmentId}`)
							if (this.departmentOptions[i].id == departmentId) {
								this.selectedDepartmentIndex = i
								console.log('部门匹配成功，索引:', i, '名称:', this.departmentOptions[i].name)
								break
							}
						}
					} else {
						console.log('部门ID为空，跳过匹配')
					}
								
					console.log('回写选中值完成:', {
						natureId, selectedNatureIndex: this.selectedNatureIndex,
						professionId, selectedProfessionIndex: this.selectedProfessionIndex,
						departmentId, selectedDepartmentIndex: this.selectedDepartmentIndex
					})
				},
							
				// 加载性质界定字典数据
				loadNatureOptions() {
					const token = uni.getStorageSync('token')
					console.log('开始加载性质界定数据')
					uni.request({
						url: getApiUrl('/dictionary/type/性质界定'),
						method: 'GET',
						header: {
							'Authorization': `Bearer ${token}`,
							'Content-Type': 'application/json'
						},
						success: (res) => {
							const data = res.data as UTSJSONObject
							console.log('性质界定接口响应:', res.statusCode, data.getNumber('code'))
							if (res.statusCode === 200 && data.getNumber('code') === 200) {
								const dataArray = data.getArray('data')
								console.log('性质界定原始数据:', dataArray)
								if (dataArray != null) {
									this.natureOptions = []
									for (let i = 0; i < dataArray.length; i++) {
										const item = dataArray[i] as UTSJSONObject
										const id = item.getString('id') ?? ''
										const dictValue = item.getString('dictValue') ?? ''
										const dictLabel = item.getString('dictLabel') ?? ''
										const name = dictValue !== '' ? dictValue : dictLabel
										console.log(`性质界定项目 ${i}:`, { id, dictValue, dictLabel, name })
										this.natureOptions.push({ id: id, name: name })
									}
								}
								console.log('性质界定数据加载成功:', this.natureOptions)
							} else {
								const message = data.getString('message') ?? '未知错误'
								console.error('获取性质界定数据失败:', message)
								this.natureOptions = [
									{ id: '1', name: '一般问题' },
									{ id: '2', name: '较大问题' },
									{ id: '3', name: '严重问题' },
									{ id: '4', name: '重大问题' }
								]
								console.log('使用默认性质界定数据:', this.natureOptions)
							}
							this.checkAllDataLoaded()
						},
						fail: (error) => {
							console.error('请求性质界定数据失败:', error)
							this.natureOptions = [
								{ id: '1', name: '一般问题' },
								{ id: '2', name: '较大问题' },
								{ id: '3', name: '严重问题' },
								{ id: '4', name: '重大问题' }
							]
							console.log('使用默认性质界定数据:', this.natureOptions)
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
							if (res.statusCode === 200 && data.getNumber('code') === 200) {
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
							} else {
								this.professionOptions = [
									{ id: '1', name: '机械' },
									{ id: '2', name: '电气' },
									{ id: '3', name: '仪表' },
									{ id: '4', name: '工艺' },
									{ id: '5', name: '安全' },
									{ id: '6', name: '环保' }
								]
							}
							this.checkAllDataLoaded()
						},
						fail: (error) => {
							this.professionOptions = [
								{ id: '1', name: '机械' },
								{ id: '2', name: '电气' },
								{ id: '3', name: '仪表' },
								{ id: '4', name: '工艺' },
								{ id: '5', name: '安全' },
								{ id: '6', name: '环保' }
							]
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
							if (res.statusCode === 200 && data.getNumber('code') === 200) {
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
							} else {
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
							this.checkAllDataLoaded()
						},
						fail: (error) => {
							this.departmentOptions = [
								{ id: '1', name: '生产部' },
								{ id: '2', name: '安全部' },
								{ id: '3', name: '设备部' },
								{ id: '4', name: '技术部' },
								{ id: '5', name: '质量部' },
								{ id: '6', name: '环保部' },
								{ id: '7', name: '行政部' }
							]
							this.checkAllDataLoaded()
						}
					})
				},
				
				// 性质界定选择器方法
				showNaturePicker() {
					if (this.natureOptions.length > 0) {
						this.showNaturePopup = true
					}
				},
				
				hideNaturePicker() {
					this.showNaturePopup = false
				},
				
				selectNature(index: number) {
					this.selectedNatureIndex = index
					this.hideNaturePicker()
				},
				
				// 专业选择器方法
				showProfessionPicker() {
					if (this.professionOptions.length > 0) {
						this.showProfessionPopup = true
					}
				},
				
				hideProfessionPicker() {
					this.showProfessionPopup = false
				},
				
				selectProfession(index: number) {
					this.selectedProfessionIndex = index
					this.hideProfessionPicker()
				},
				
				// 部门选择器方法
				showDepartmentPicker() {
					if (this.departmentOptions.length > 0) {
						this.showDepartmentPopup = true
					}
				},
				
				hideDepartmentPicker() {
					this.showDepartmentPopup = false
				},
				
				selectDepartment(index: number) {
					this.selectedDepartmentIndex = index
					this.hideDepartmentPicker()
				}

			}
		})
	
export default __sfc__
function GenPagesRecordReviewRender(this: InstanceType<typeof __sfc__>): any | null {
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
              _cE("text", _uM({ class: "page-title" }), "复查记录"),
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
              _cE("text", _uM({ class: "form-label" }), "整改效果描述"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.rectificationEffect ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "📋 整改状态"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.getStatusText(_ctx.recordDetail?.status ?? 0)), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "📷 整改图片 (" + _tD(_ctx.rectificationImages.length) + "张)", 1 /* TEXT */),
              _cE("view", _uM({ class: "image-upload" }), [
                _ctx.rectificationImages.length > 0
                  ? _cE("view", _uM({
                      key: 0,
                      class: "image-list"
                    }), [
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
                          }), null, 40 /* PROPS, NEED_HYDRATION */, ["src", "onClick", "onError", "onLoad"])
                        ])
                      }), 128 /* KEYED_FRAGMENT */)
                    ])
                  : _cE("view", _uM({
                      key: 1,
                      class: "no-image-text"
                    }), [
                      _cE("text", null, "暂无整改图片")
                    ])
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
                _cE("view", _uM({ class: "form-display-text" }), _tD(_ctx.formatDate(_ctx.recordDetail?.rectificationDate ?? '')), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "复查效果描述"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                isTrue(!_ctx.isDelayed && !_ctx.isReviewed)
                  ? _cE("textarea", _uM({
                      key: 0,
                      modelValue: _ctx.reviewEffect,
                      onInput: ($event: UniInputEvent) => {(_ctx.reviewEffect) = $event.detail.value},
                      class: "form-textarea",
                      placeholder: "请描述复查效果...",
                      maxlength: "500",
                      "show-confirm-bar": "false"
                    }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
                  : _cE("view", _uM({
                      key: 1,
                      class: "form-display-text"
                    }), _tD(_ctx.reviewEffect.length > 0 ? _ctx.reviewEffect : '暂无复查效果描述'), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "📷 复查图片 (" + _tD(_ctx.reviewImages.length) + _tD((_ctx.isDelayed || _ctx.isReviewed) ? '' : '/3') + ")", 1 /* TEXT */),
              _cE("view", _uM({ class: "image-upload" }), [
                _ctx.reviewImages.length > 0
                  ? _cE("view", _uM({
                      key: 0,
                      class: "image-list"
                    }), [
                      _cE(Fragment, null, RenderHelpers.renderList(_ctx.reviewImages, (image, index, __index, _cached): any => {
                        return _cE("view", _uM({
                          class: "image-item",
                          key: index
                        }), [
                          _cE("image", _uM({
                            src: image,
                            mode: "aspectFill",
                            class: "uploaded-image",
                            onClick: () => {_ctx.previewReviewImage(index)},
                            onError: _ctx.onImageError,
                            onLoad: _ctx.onImageLoad
                          }), null, 40 /* PROPS, NEED_HYDRATION */, ["src", "onClick", "onError", "onLoad"]),
                          isTrue(!_ctx.isDelayed && !_ctx.isReviewed)
                            ? _cE("view", _uM({
                                key: 0,
                                class: "image-delete",
                                onClick: () => {_ctx.removeReviewImage(index)}
                              }), [
                                _cE("text", null, "×")
                              ], 8 /* PROPS */, ["onClick"])
                            : _cC("v-if", true)
                        ])
                      }), 128 /* KEYED_FRAGMENT */),
                      isTrue(!_ctx.isDelayed && !_ctx.isReviewed && _ctx.reviewImages.length < 3)
                        ? _cE("view", _uM({
                            key: 0,
                            class: "image-add",
                            onClick: _ctx.chooseReviewImage
                          }), [
                            _cE("text", _uM({ class: "add-icon" }), "+"),
                            _cE("text", _uM({ class: "add-text" }), "添加图片")
                          ], 8 /* PROPS */, ["onClick"])
                        : _cC("v-if", true)
                    ])
                  : _cE("view", _uM({
                      key: 1,
                      class: "no-image-container"
                    }), [
                      isTrue(!_ctx.isDelayed && !_ctx.isReviewed)
                        ? _cE("view", _uM({
                            key: 0,
                            class: "image-add",
                            onClick: _ctx.chooseReviewImage
                          }), [
                            _cE("text", _uM({ class: "add-icon" }), "+"),
                            _cE("text", _uM({ class: "add-text" }), "添加图片")
                          ], 8 /* PROPS */, ["onClick"])
                        : _cC("v-if", true)
                    ])
              ])
            ]),
            isTrue(!_ctx.isDelayed && !_ctx.isReviewed)
              ? _cE("view", _uM({
                  key: 4,
                  class: "save-button-wrapper"
                }), [
                  _cE("button", _uM({
                    class: "save-btn",
                    onClick: _ctx.saveReviewInfo,
                    disabled: _ctx.saving
                  }), _tD(_ctx.saving ? '保存中...' : '确认复查'), 9 /* TEXT, PROPS */, ["onClick", "disabled"])
                ])
              : _cC("v-if", true)
          ])
        : _cE("view", _uM({
            key: 2,
            class: "error-state"
          }), [
            _cE("text", _uM({ class: "error-text" }), "记录不存在或已被删除")
          ])
  ], 12 /* STYLE, PROPS */, ["scroll-top"])
}
const GenPagesRecordReviewStyles = [_uM([["container", _pS(_uM([["paddingTop", 0], ["paddingRight", 0], ["paddingBottom", 0], ["paddingLeft", 0], ["backgroundColor", "#f5f5f5"], ["display", "flex"], ["flexDirection", "column"]]))], ["loading-state", _pS(_uM([["flex", 1], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"]]))], ["loading-text", _pS(_uM([["fontSize", 16], ["color", "#666666"]]))], ["status-header", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#e0e0e0"], ["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"]]))], ["page-title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#333333"]]))], ["record-status", _pS(_uM([["paddingTop", 5], ["paddingRight", 12], ["paddingBottom", 5], ["paddingLeft", 12], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["fontSize", 14]]))], ["status-pending", _pS(_uM([["backgroundColor", "#fff3cd"], ["color", "#856404"]]))], ["status-completed", _pS(_uM([["backgroundColor", "#d1ecf1"], ["color", "#0c5460"]]))], ["status-reviewed", _pS(_uM([["backgroundColor", "#d4edda"], ["color", "#155724"]]))], ["form-item", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"]]))], ["form-label", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["marginBottom", 10]]))], ["form-input-wrapper", _pS(_uM([["marginTop", 8]]))], ["form-display-text", _pS(_uM([["fontSize", 15], ["color", "#666666"], ["lineHeight", 1.5]]))], ["form-textarea", _pS(_uM([["width", "100%"], ["minHeight", 80], ["paddingTop", 10], ["paddingRight", 10], ["paddingBottom", 10], ["paddingLeft", 10], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e0e0e0"], ["borderRightColor", "#e0e0e0"], ["borderBottomColor", "#e0e0e0"], ["borderLeftColor", "#e0e0e0"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["fontSize", 15], ["lineHeight", 1.5], ["color", "#333333"], ["backgroundColor", "#ffffff"]]))], ["image-upload", _pS(_uM([["marginTop", 10]]))], ["image-list", _pS(_uM([["display", "flex"], ["flexWrap", "wrap"], ["gap", "10px"]]))], ["image-item", _pS(_uM([["position", "relative"], ["width", 80], ["height", 80]]))], ["uploaded-image", _pS(_uM([["width", "100%"], ["height", "100%"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e0e0e0"], ["borderRightColor", "#e0e0e0"], ["borderBottomColor", "#e0e0e0"], ["borderLeftColor", "#e0e0e0"]]))], ["image-delete", _pS(_uM([["position", "absolute"], ["top", -8], ["right", -8], ["width", 20], ["height", 20], ["backgroundColor", "#ff4757"], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"], ["color", "#FFFFFF"], ["fontSize", 14], ["lineHeight", 1]]))], ["image-add", _pS(_uM([["width", 80], ["height", 80], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "dashed"], ["borderRightStyle", "dashed"], ["borderBottomStyle", "dashed"], ["borderLeftStyle", "dashed"], ["borderTopColor", "#cccccc"], ["borderRightColor", "#cccccc"], ["borderBottomColor", "#cccccc"], ["borderLeftColor", "#cccccc"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["display", "flex"], ["flexDirection", "column"], ["justifyContent", "center"], ["alignItems", "center"], ["backgroundColor", "#fafafa"]]))], ["add-icon", _pS(_uM([["fontSize", 24], ["color", "#999999"], ["marginBottom", 5]]))], ["add-text", _pS(_uM([["fontSize", 12], ["color", "#999999"]]))], ["no-image-container", _pS(_uM([["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["gap", "15px"]]))], ["no-image-text", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["textAlign", "center"], ["color", "#999999"], ["fontSize", 14], ["backgroundColor", "#f8f9fa"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["width", "100%"]]))], ["save-button-wrapper", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"]]))], ["save-btn", _pS(_uM([["width", "100%"], ["paddingTop", 12], ["paddingRight", 12], ["paddingBottom", 12], ["paddingLeft", 12], ["backgroundColor", "#007AFF"], ["color", "#FFFFFF"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["fontSize", 16], ["backgroundColor:disabled", "#cccccc"], ["color:disabled", "#999999"]]))], ["delayed-mode-tip", _pS(_uM([["marginTop", "20rpx"], ["marginRight", "20rpx"], ["marginBottom", "20rpx"], ["marginLeft", "20rpx"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["backgroundColor", "#fff3cd"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#ffeaa7"], ["borderRightColor", "#ffeaa7"], ["borderBottomColor", "#ffeaa7"], ["borderLeftColor", "#ffeaa7"], ["borderTopLeftRadius", "10rpx"], ["borderTopRightRadius", "10rpx"], ["borderBottomRightRadius", "10rpx"], ["borderBottomLeftRadius", "10rpx"], ["textAlign", "center"]]))], ["tip-text", _pS(_uM([["color", "#856404"], ["fontSize", "28rpx"], ["fontWeight", "bold"]]))], ["error-state", _pS(_uM([["flex", 1], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"]]))], ["error-text", _pS(_uM([["fontSize", 16], ["color", "#999999"]]))], ["custom-picker-full", _uM([["", _uM([["width", "100%"], ["height", 44], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["backgroundImage", "none"], ["backgroundColor", "#f8f9fa"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "space-between"], ["boxSizing", "border-box"], ["cursor", "pointer"]])], [".disabled", _uM([["backgroundImage", "none"], ["backgroundColor", "#f5f5f5"], ["color", "#999999"], ["cursor", "not-allowed"]])]])], ["picker-text-full", _uM([["", _uM([["fontSize", 16], ["color", "#333333"], ["flex", 1]])], [".placeholder", _uM([["color", "#999999"]])]])], ["popup-overlay", _pS(_uM([["position", "fixed"], ["top", 0], ["left", 0], ["width", "100%"], ["height", "100%"], ["backgroundImage", "none"], ["backgroundColor", "rgba(0,0,0,0.5)"], ["zIndex", 1000], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["popup-content", _pS(_uM([["width", "80%"], ["maxWidth", 400], ["backgroundImage", "none"], ["backgroundColor", "#FFFFFF"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["overflow", "hidden"]]))], ["popup-header", _pS(_uM([["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "space-between"]]))], ["popup-title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#333333"]]))], ["popup-close", _pS(_uM([["fontSize", 24], ["color", "#999999"], ["cursor", "pointer"], ["lineHeight", 1]]))], ["popup-list", _pS(_uM([["maxHeight", 300], ["overflowY", "auto"]]))], ["popup-item", _pS(_uM([["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f8f9fa"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "space-between"], ["cursor", "pointer"], ["backgroundImage:hover", "none"], ["backgroundColor:hover", "#f8f9fa"], ["borderBottomWidth:last-child", "medium"], ["borderBottomStyle:last-child", "none"], ["borderBottomColor:last-child", "#000000"]]))], ["popup-item-text", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["flex", 1]]))], ["popup-item-check", _pS(_uM([["fontSize", 18], ["color", "#007AFF"], ["fontWeight", "bold"]]))]])]
