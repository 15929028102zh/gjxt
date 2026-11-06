
	import { getApiUrl, apiConfig } from '@/config/api.uts'
	
	type RecordDetail = {
		id: string
		problemDescription: string
		responsibleUnit: string
		responsiblePerson: string
		natureId: string
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
		rectificationDeadline: string
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
		reviewerName: string
	}

	const __sfc__ = defineComponent({
		data() {
			// 同步计算 scroll-view 高度:windowHeight + 200px 偏移确保底部图片完整显示
			// 必须 data() 同步执行避免 scroll-view 定型问题
			let containerHeight = 0
			try {
				const sysInfo = uni.getSystemInfoSync()
				containerHeight = sysInfo.windowHeight ?? 0
			} catch (e) {
				console.error('获取屏幕信息失败:', e)
			}
			return {
				recordId: '',
				recordDetail: null as RecordDetail | null,
				imageList: [] as Array<string>,
				rectificationImages: [] as Array<string>,
				loading: true,
				reviewEffect: '',
				reviewImages: [] as Array<string>,
				saving: false,
				containerHeight: containerHeight,
				// 延迟整改模式标识
				isDelayed: false,
				// 已复查记录标识
				isReviewed: false,
				// 是否为月检标识
				isMonthly: false,
				// 复查人：当前登录用户姓名（只读，确认复查时写库）
				currentUserName: ''
			}
		},
		onLoad(options: UTSJSONObject) {
			this.recordId = options.getString('id') ?? ''
			// 检查是否为延迟整改模式
			const delayedParam = options.getString('delayed')
			if (delayedParam != null && delayedParam == 'true') {
				this.isDelayed = true
			} else {
				this.isDelayed = false
			}

			// 检查是否为已复查记录模式
			const reviewedParam = options.getString('reviewed')
			if (reviewedParam != null && reviewedParam == 'true') {
				this.isReviewed = true
			} else {
				this.isReviewed = false
			}

			// 检查是否为月检记录
			const monthlyParam = options.getString('isMonthly')
			if (monthlyParam != null && monthlyParam == 'true') {
				this.isMonthly = true
			} else {
				this.isMonthly = false
			}

			this.checkLogin()
			this.loadCurrentUserName()
			this.loadRecordDetail()
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

			// 复查人取当前登录用户真实姓名
			loadCurrentUserName() {
				const userInfoStr = uni.getStorageSync('userInfo') as string
				if (userInfoStr == null || userInfoStr == '') return
				try {
					const userInfo = JSON.parse(userInfoStr) as UTSJSONObject
					this.currentUserName = userInfo.getString('realName') ?? userInfo.getString('username') ?? ''
				} catch (e) {
					console.error('解析用户信息失败:', e)
				}
			},


			loadRecordDetail() {
				const token = uni.getStorageSync('token')
				const detailUrl = this.isMonthly
					? getApiUrl(`/monthly-inspection-record/${this.recordId}`)
					: getApiUrl(`/inspection-record/${this.recordId}`)
				uni.showLoading({ title: '加载中...' })
				uni.request({
					url: detailUrl,
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (response) => {
						uni.hideLoading()
						const data = response.data as UTSJSONObject
						if (response.statusCode === 200 && (data.getNumber('code') ?? 0) === 200) {
							const recordData = data.getJSON('data')
							if (recordData != null) {
								this.recordDetail = {
									id: recordData.getString('id') ?? '',
									problemDescription: recordData.getString('problemDescription') ?? '',
									responsibleUnit: recordData.getString('responsibleUnit') ?? '',
									responsiblePerson: recordData.getString('responsiblePerson') ?? '',
									natureId: recordData.getString('natureId') ?? '',
									natureName: recordData.getString('natureName') ?? recordData.getString('natureDefinition') ?? '',
									assessmentSituation: recordData.getString('assessmentSituation') ?? '',
									assessmentAmount: recordData.getNumber('assessmentAmount') ?? 0,
									safetyScore: recordData.getNumber('safetyScore') ?? 0,
									professionId: recordData.getString('professionId') ?? '',
									professionName: recordData.getString('professionName') ?? '',
									location: recordData.getString('location') ?? '',
									inspector: recordData.getString('inspector') ?? '',
									groupLeader: recordData.getString('groupLeader') ?? '',
									deputyLeader: recordData.getString('deputyLeader') ?? '',
									departmentId: recordData.getString('departmentId') ?? '',
									departmentName: recordData.getString('departmentName') ?? '',
									rectificationDeadline: recordData.getString('rectificationDeadline') ?? '',
									status: recordData.getNumber('status') ?? 0,
									createTime: recordData.getString('createTime') ?? '',
									updateTime: recordData.getString('updateTime') ?? '',
									problemImageUrls: recordData.getString('problemImageUrls') ?? '',
									locationName: recordData.getString('locationName') ?? '',
									severity: recordData.getNumber('severity') ?? 0,
									taskName: recordData.getString('taskName') ?? '',
									reporterName: recordData.getString('reporterName') ?? '',
									suggestions: recordData.getString('suggestions') ?? '',
									reporterId: recordData.getString('reporterId') ?? '',
									rectificationEffect: recordData.getString('rectificationEffect') ?? '',
									rectificationImageUrls: recordData.getString('rectificationImageUrls') ?? '',
									rectificationTime: recordData.getString('rectificationTime') ?? '',
									reviewEffect: recordData.getString('reviewEffect') ?? '',
									reviewImageUrls: recordData.getString('reviewImageUrls') ?? '',
									reviewerName: recordData.getString('reviewerName') ?? ''
								} as RecordDetail
								this.imageList = this.processImageUrls(this.recordDetail?.problemImageUrls ?? '')
								this.rectificationImages = this.processImageUrls(this.recordDetail?.rectificationImageUrls ?? '')
								if (this.isReviewed) {
									this.reviewEffect = this.recordDetail?.reviewEffect ?? ''
									this.reviewImages = this.processImageUrls(this.recordDetail?.reviewImageUrls ?? '')
								}
							}
						} else {
							uni.showToast({
								title: data.getString('message') ?? '加载失败',
								icon: 'none'
							})
						}
						this.loading = false
					},
					fail: (error) => {
						uni.hideLoading()
						console.error('加载记录详情失败:', error)
						uni.showToast({ title: '网络错误', icon: 'none' })
						this.loading = false
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
					// 性质界定：周检端不再支持编辑,直接使用 recordDetail.natureName
					natureDefinition: this.recordDetail?.natureName ?? '',
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
                        // 根据是否为月检选择对应的接口
                        const apiUrl = this.isMonthly 
                            ? getApiUrl(`/monthly-inspection-record/${this.recordDetail?.id ?? ''}/review`)
                            : getApiUrl(`/inspection-record/${this.recordDetail?.id ?? ''}/review`)
                        console.log('使用接口:', apiUrl, 'isMonthly:', this.isMonthly)
                        const payload = {
                            reviewSuggestion: this.reviewEffect,
                            reviewEffect: this.reviewEffect,
                            reviewImages: reviewImageUrls.join(','),
                            reviewerId: currentUserId,
                            reviewerName: this.currentUserName
                        }
                        uni.request({
                            url: apiUrl,
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
              _cE("text", _uM({
                class: _nC(["record-status", _ctx.getStatusClass(_ctx.recordDetail?.status ?? 0)])
              }), _tD(_ctx.getStatusText(_ctx.recordDetail?.status ?? 0)), 3 /* TEXT, CLASS */)
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
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.problemDescription ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "整改效果描述"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.rectificationEffect ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "📋 整改状态"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.getStatusText(_ctx.recordDetail?.status ?? 0)), 1 /* TEXT */)
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
                      _cE("text", _uM({ class: "no-image-text-inner" }), "暂无整改图片")
                    ])
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "责任单位"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.responsibleUnit ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "责任人"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.responsiblePerson ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "性质界定"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.natureName ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "考核情况"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.assessmentSituation ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "考核金额"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.assessmentAmount ?? '') + "元", 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "安全记分"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.safetyScore ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "专业"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.professionName ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "发生部位"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.location ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "检查人"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.inspector ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "检查组组长"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.groupLeader ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "检查组副组长"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.deputyLeader ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "所属部门"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.departmentName ?? ''), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "整改期限"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.formatDate(_ctx.recordDetail?.rectificationDeadline ?? '')), 1 /* TEXT */)
              ])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "复查人"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD((!_ctx.isDelayed && !_ctx.isReviewed) ? (_ctx.currentUserName !== '' ? _ctx.currentUserName : '当前登录用户') : ((_ctx.recordDetail?.reviewerName ?? '') !== '' ? (_ctx.recordDetail?.reviewerName ?? '') : '暂无')), 1 /* TEXT */)
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
                  : _cE("text", _uM({
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
                  : isTrue(_ctx.isDelayed || _ctx.isReviewed)
                    ? _cE("view", _uM({
                        key: 1,
                        class: "no-image-text"
                      }), [
                        _cE("text", _uM({ class: "no-image-text-inner" }), "暂无复查图片")
                      ])
                    : _cE("view", _uM({
                        key: 2,
                        class: "no-image-container"
                      }), [
                        _cE("view", _uM({
                          class: "image-add",
                          onClick: _ctx.chooseReviewImage
                        }), [
                          _cE("text", _uM({ class: "add-icon" }), "+"),
                          _cE("text", _uM({ class: "add-text" }), "添加图片")
                        ], 8 /* PROPS */, ["onClick"])
                      ])
              ])
            ]),
            isTrue(!_ctx.isDelayed && !_ctx.isReviewed)
              ? _cE("view", _uM({
                  key: 1,
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
  ], 4 /* STYLE */)
}
const GenPagesRecordReviewStyles = [_uM([["container", _pS(_uM([["backgroundColor", "#f1f5f9"]]))], ["loading-state", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"]]))], ["loading-text", _pS(_uM([["fontSize", 16], ["color", "#475569"]]))], ["detail-content", _pS(_uM([["paddingTop", "24rpx"], ["paddingRight", "32rpx"], ["paddingBottom", "130rpx"], ["paddingLeft", "32rpx"]]))], ["status-header", _pS(_uM([["backgroundImage", "linear-gradient(135deg, #1e40af 0%, #2563eb 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#dbeafe"], ["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"]]))], ["page-title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#ffffff"]]))], ["record-status", _pS(_uM([["paddingTop", 5], ["paddingRight", 12], ["paddingBottom", 5], ["paddingLeft", 12], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["fontSize", 14], ["lineHeight", 1.4]]))], ["status-pending", _pS(_uM([["backgroundColor", "#fef3c7"], ["color", "#b45309"]]))], ["status-completed", _pS(_uM([["backgroundColor", "#dbeafe"], ["color", "#1e40af"]]))], ["status-reviewed", _pS(_uM([["backgroundColor", "#dcfce7"], ["color", "#16a34a"]]))], ["status-unknown", _pS(_uM([["backgroundColor", "#f1f5f9"], ["color", "#475569"]]))], ["form-item", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f1f5f9"]]))], ["form-label", _pS(_uM([["fontSize", 16], ["color", "#0f172a"], ["marginBottom", 10], ["fontWeight", 500]]))], ["form-input-wrapper", _pS(_uM([["marginTop", 8]]))], ["form-display-text", _pS(_uM([["fontSize", 15], ["color", "#475569"], ["lineHeight", 1.5]]))], ["form-textarea", _pS(_uM([["width", "100%"], ["minHeight", 80], ["paddingTop", 10], ["paddingRight", 10], ["paddingBottom", 10], ["paddingLeft", 10], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["fontSize", 15], ["lineHeight", 1.5], ["color", "#0f172a"], ["backgroundColor", "#eff6ff"], ["borderTopColor:focus", "#2563eb"], ["borderRightColor:focus", "#2563eb"], ["borderBottomColor:focus", "#2563eb"], ["borderLeftColor:focus", "#2563eb"], ["outline:focus", "none"]]))], ["image-upload", _pS(_uM([["marginTop", 10]]))], ["image-list", _pS(_uM([["display", "flex"], ["flexWrap", "wrap"]]))], ["image-item", _uM([[".image-list ", _uM([["marginTop", 5], ["marginRight", 5], ["marginBottom", 5], ["marginLeft", 5]])], ["", _uM([["position", "relative"], ["width", 80], ["height", 80]])]])], ["image-add", _uM([[".image-list ", _uM([["marginTop", 5], ["marginRight", 5], ["marginBottom", 5], ["marginLeft", 5]])], ["", _uM([["width", 80], ["height", 80], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "dashed"], ["borderRightStyle", "dashed"], ["borderBottomStyle", "dashed"], ["borderLeftStyle", "dashed"], ["borderTopColor", "#38bdf8"], ["borderRightColor", "#38bdf8"], ["borderBottomColor", "#38bdf8"], ["borderLeftColor", "#38bdf8"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["display", "flex"], ["flexDirection", "column"], ["justifyContent", "center"], ["alignItems", "center"], ["backgroundColor", "#eff6ff"]])], [".no-image-container ", _uM([["marginTop", 15]])]])], ["uploaded-image", _pS(_uM([["width", "100%"], ["height", "100%"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"]]))], ["image-delete", _pS(_uM([["position", "absolute"], ["top", -8], ["right", -8], ["width", 20], ["height", 20], ["backgroundColor", "#dc2626"], ["borderTopLeftRadius", "50%"], ["borderTopRightRadius", "50%"], ["borderBottomRightRadius", "50%"], ["borderBottomLeftRadius", "50%"], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"], ["color", "#FFFFFF"], ["fontSize", 14], ["lineHeight", 1]]))], ["add-icon", _pS(_uM([["fontSize", 24], ["color", "#2563eb"], ["marginBottom", 5]]))], ["add-text", _pS(_uM([["fontSize", 12], ["color", "#475569"]]))], ["no-image-container", _pS(_uM([["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"]]))], ["no-image-text", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["backgroundColor", "#eff6ff"], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["width", "100%"]]))], ["no-image-text-inner", _pS(_uM([["color", "#94a3b8"], ["fontSize", 14], ["textAlign", "center"]]))], ["save-button-wrapper", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"], ["marginTop", "20rpx"]]))], ["save-btn", _pS(_uM([["width", "100%"], ["paddingTop", "24rpx"], ["paddingRight", "24rpx"], ["paddingBottom", "24rpx"], ["paddingLeft", "24rpx"], ["backgroundImage", "none"], ["backgroundColor", "#1e40af"], ["color", "#ffffff"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["fontSize", "32rpx"], ["fontWeight", 600], ["backgroundImage:disabled", "none"], ["backgroundColor:disabled", "#94a3b8"], ["color:disabled", "#f1f5f9"]]))], ["delayed-mode-tip", _pS(_uM([["marginTop", "20rpx"], ["marginRight", "20rpx"], ["marginBottom", "20rpx"], ["marginLeft", "20rpx"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["backgroundColor", "#eff6ff"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#38bdf8"], ["borderRightColor", "#38bdf8"], ["borderBottomColor", "#38bdf8"], ["borderLeftColor", "#38bdf8"], ["borderTopLeftRadius", "10rpx"], ["borderTopRightRadius", "10rpx"], ["borderBottomRightRadius", "10rpx"], ["borderBottomLeftRadius", "10rpx"], ["textAlign", "center"]]))], ["tip-text", _pS(_uM([["color", "#1e40af"], ["fontSize", "28rpx"], ["fontWeight", "bold"]]))], ["error-state", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"]]))], ["error-text", _pS(_uM([["fontSize", 16], ["color", "#94a3b8"]]))], ["custom-picker-full", _uM([["", _uM([["width", "100%"], ["height", 44], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["backgroundImage", "none"], ["backgroundColor", "#eff6ff"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "space-between"], ["boxSizing", "border-box"], ["cursor", "pointer"]])], [".disabled", _uM([["backgroundImage", "none"], ["backgroundColor", "#f1f5f9"], ["cursor", "not-allowed"]])]])], ["picker-text-full", _uM([[".custom-picker-full.disabled ", _uM([["color", "#94a3b8"]])], ["", _uM([["fontSize", 16], ["color", "#0f172a"], ["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"]])], [".placeholder", _uM([["color", "#94a3b8"]])]])], ["popup-overlay", _pS(_uM([["position", "fixed"], ["top", 0], ["left", 0], ["width", "100%"], ["height", "100%"], ["backgroundImage", "none"], ["backgroundColor", "rgba(15,23,42,0.5)"], ["zIndex", 1000], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["popup-content", _pS(_uM([["width", "80%"], ["maxWidth", 400], ["backgroundImage", "none"], ["backgroundColor", "#FFFFFF"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["overflow", "hidden"], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.16)"], ["display", "flex"], ["flexDirection", "column"]]))], ["popup-header", _pS(_uM([["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f1f5f9"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "space-between"], ["backgroundImage", "linear-gradient(135deg, #1e40af 0%, #2563eb 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["flexShrink", 0]]))], ["popup-title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#ffffff"]]))], ["popup-close", _pS(_uM([["fontSize", 24], ["color", "#dbeafe"], ["cursor", "pointer"], ["lineHeight", 1]]))], ["popup-list", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["overflowY", "auto"], ["WebkitOverflowScrolling", "touch"]]))], ["popup-item", _pS(_uM([["paddingTop", 15], ["paddingRight", 20], ["paddingBottom", 15], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#eff6ff"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "space-between"], ["cursor", "pointer"], ["backgroundImage:hover", "none"], ["backgroundColor:hover", "#eff6ff"], ["borderBottomWidth:last-child", "medium"], ["borderBottomStyle:last-child", "none"], ["borderBottomColor:last-child", "#000000"]]))], ["popup-item-text", _pS(_uM([["fontSize", 16], ["color", "#0f172a"], ["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"]]))], ["popup-item-check", _pS(_uM([["fontSize", 18], ["color", "#2563eb"], ["fontWeight", "bold"]]))]])]
