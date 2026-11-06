
	import { getApiUrl, apiConfig } from '@/config/api.uts'

	type RecordDetail = { __$originalPosition?: UTSSourceMapPosition<"RecordDetail", "pages/record/detail.uvue", 226, 7>;
		id: string
		problemDescription: string
		responsibleUnit: string
		responsiblePerson: string
		natureId: string
		natureName: string
		assessmentSituation: string
		assessmentAmount: number
		safetyScore: number
		professionId: string
		professionName: string
		location: string
		inspector: string
		groupLeader: string
		deputyLeader: string
		departmentId: string
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
		rectifierName: string
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
				console.error('获取屏幕信息失败:', e, " at pages/record/detail.uvue:270")
			}
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
				containerHeight: containerHeight,
				isDelayed: false,
				// 整改人：当前登录用户姓名（只读，确认整改时写库）
				currentUserName: ''
			}
		},
		onLoad(options: UTSJSONObject) {
			this.recordId = options.getString('id') ?? ''
			this.isDelayed = options.getString('delayed') === 'true'
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

			// 整改人取当前登录用户真实姓名
			loadCurrentUserName() {
				const userInfoStr = uni.getStorageSync('userInfo') as string
				if (userInfoStr == null || userInfoStr == '') return
				try {
					const userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/detail.uvue:310") as UTSJSONObject
					this.currentUserName = userInfo.getString('realName') ?? userInfo.getString('username') ?? ''
				} catch (e) {
					console.error('解析用户信息失败:', e, " at pages/record/detail.uvue:313")
				}
			},


			loadRecordDetail() {
				const token = uni.getStorageSync('token')
				uni.showLoading({ title: '加载中...' })
				uni.request({
					url: getApiUrl(`/inspection-record/${this.recordId}`),
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
									rectifierName: recordData.getString('rectifierName') ?? ''
								} as RecordDetail
								this.imageList = this.processImageUrls(this.recordDetail?.problemImageUrls ?? '')
								if (this.recordDetail?.rectificationEffect != null && this.recordDetail.rectificationEffect !== '') {
									this.rectificationEffect = this.recordDetail.rectificationEffect
								}
								this.rectificationImages = this.processImageUrls(this.recordDetail?.rectificationImageUrls ?? '')
								this.rectificationStatus = this.recordDetail?.status ?? 0
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
						console.error('加载记录详情失败:', error, " at pages/record/detail.uvue:384")
						uni.showToast({ title: '网络错误', icon: 'none' })
						this.loading = false
					}
				})
			},

			convertToHttpUrl(relativePath: string): string {
				if (relativePath == null || relativePath == '' || relativePath.trim() === '') {
					return ''
				}
				if (relativePath.startsWith('http://') || relativePath.startsWith('https://')) {
					return relativePath
				}
				return `${apiConfig.baseUrl}/api${relativePath}`
			},

			processImageUrls(imageUrls: string): string[] {
				if (imageUrls == null || imageUrls.trim() === '') {
					return []
				}
				try {
					if (imageUrls.trim().startsWith('[') && imageUrls.trim().endsWith(']')) {
						const parsed = UTSAndroid.consoleDebugError(JSON.parse(imageUrls), " at pages/record/detail.uvue:407") as string[]
						return parsed.map(url => this.convertToHttpUrl(url)).filter(url => url !== '')
					}
				} catch (e) {
					console.log('JSON解析失败,尝试按逗号分隔处理:', e, " at pages/record/detail.uvue:411")
				}
				return imageUrls.split(',')
					.map(url => url.trim())
					.filter(url => url !== '')
					.map(url => this.convertToHttpUrl(url))
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
				if (dateTimeStr.includes(' ')) {
					return dateTimeStr.split(' ')[0]
				}
				return dateTimeStr
			},

			onImageLoad(e: any) {},

			onImageError(e: any) {
				uni.showToast({ title: '图片加载失败', icon: 'none' })
			},

			chooseRectificationImage() {
				if (this.rectificationImages.length >= 3) {
					uni.showToast({ title: '最多只能上传3张图片', icon: 'none' })
					return
				}
				uni.chooseImage({
					count: 3 - this.rectificationImages.length,
					sourceType: ['camera', 'album'],
					success: (res) => {
						const tempFilePaths = res.tempFilePaths
						for (let i = 0; i < tempFilePaths.length; i++) {
							this.rectificationImages.push(tempFilePaths[i])
						}
					}
				})
			},

			removeRectificationImage(index: number) {
				this.rectificationImages.splice(index, 1)
			},

			setRectificationStatus(value: number) {
				this.rectificationStatus = value
			},

			async saveRectificationInfo() {
				if (this.saving) return

				if (this.rectificationEffect.trim().length === 0) {
					uni.showToast({ title: '请填写整改效果', icon: 'none' })
					return
				}

				this.saving = true

				try {
					let rectificationImageUrls: string[] = []
					if (this.rectificationImages.length > 0) {
						uni.showLoading({ title: '上传整改图片中...' })
						rectificationImageUrls = await this.uploadRectificationImages()
						uni.hideLoading()
					}

					const userInfoStr = uni.getStorageSync('userInfo') as string
					let currentUserId = ''
					let rectifierName = ''
					if (userInfoStr != null && userInfoStr !== '') {
						try {
							const userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/detail.uvue:495") as UTSJSONObject
							currentUserId = userInfo.getString('id') ?? ''
							rectifierName = userInfo.getString('realName') ?? userInfo.getString('username') ?? ''
						} catch (e) {
							console.error('解析用户信息失败:', e, " at pages/record/detail.uvue:499")
						}
					}

					const payload = {__$originalPosition: new UTSSourceMapPosition("payload", "pages/record/detail.uvue", 503, 12),
						rectificationDescription: this.rectificationEffect,
						rectificationEffect: this.rectificationEffect,
						rectificationImages: rectificationImageUrls.join(','),
						rectificationStatus: this.rectificationStatus,
						status: this.rectificationStatus === 1 ? 1 : 0,
						rectifierId: currentUserId,
						rectifierName: rectifierName,
						rectificationTime: new Date().toISOString().replace('T', ' ').substring(0, 19)
					}

					uni.showLoading({ title: '保存中...' })
					const token = uni.getStorageSync('token')
					uni.request({
						url: getApiUrl(`/inspection-record/${this.recordDetail?.id ?? ''}/rectify`),
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
								uni.showToast({ title: '保存成功', icon: 'success' })
								setTimeout(() => {
									uni.navigateBack()
								}, 1500)
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
							console.error('保存整改信息失败:', error, " at pages/record/detail.uvue:542")
							uni.showToast({ title: '网络错误', icon: 'none' })
							this.saving = false
						}
					})
				} catch (error) {
					uni.hideLoading()
					console.error('上传图片失败:', error, " at pages/record/detail.uvue:549")
					uni.showToast({ title: '图片上传失败,请重试', icon: 'none' })
					this.saving = false
				}
			},

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
									const data = UTSAndroid.consoleDebugError(JSON.parse(res.data), " at pages/record/detail.uvue:571") as UTSJSONObject
									if (res.statusCode === 200 && (data.getNumber('code') ?? 0) === 200) {
										const dataObj = data.getJSON('data')
										const imageUrl = dataObj?.getString('filePath') ?? ''
										if (imageUrl != null && imageUrl !== '') {
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
				return await Promise.all(uploadPromises)
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
function GenPagesRecordDetailRender(this: InstanceType<typeof __sfc__>): any | null {
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
              _cE("text", _uM({ class: "page-title" }), _tD(_ctx.isDelayed ? '延迟整改录入' : '问题详情'), 1 /* TEXT */),
              _cE("view", _uM({
                class: _nC(["record-status", _ctx.getStatusClass(_ctx.recordDetail?.status ?? 0)])
              }), [
                _cE("text", null, _tD(_ctx.isDelayed ? '延迟整改' : _ctx.getStatusText(_ctx.recordDetail?.status ?? 0)), 1 /* TEXT */)
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
                _cE("text", _uM({ class: "form-display-text" }), _tD(_ctx.recordDetail?.problemDescription ?? ''), 1 /* TEXT */)
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
              _cE("text", _uM({ class: "form-label" }), "整改人"),
              _cE("view", _uM({ class: "form-input-wrapper" }), [
                _cE("text", _uM({ class: "form-display-text" }), _tD((_ctx.recordDetail?.rectifierName ?? '') !== '' ? (_ctx.recordDetail?.rectifierName ?? '') : (_ctx.currentUserName !== '' ? _ctx.currentUserName : '当前登录用户')), 1 /* TEXT */)
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
                }), [
                  _cE("text", null, "⏳ 未整改")
                ], 10 /* CLASS, PROPS */, ["onClick"]),
                _cE("view", _uM({
                  class: _nC(["status-option completed", _uM({active: _ctx.rectificationStatus === 1})]),
                  onClick: () => {_ctx.setRectificationStatus(1)}
                }), [
                  _cE("text", null, "✅ 已整改")
                ], 10 /* CLASS, PROPS */, ["onClick"])
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
            _cE("view", _uM({ class: "save-button-wrapper" }), [
              _cE("button", _uM({
                class: "save-btn",
                onClick: _ctx.saveRectificationInfo,
                disabled: _ctx.saving
              }), _tD(_ctx.saving ? (_ctx.isDelayed ? '确认中...' : '保存中...') : (_ctx.isDelayed ? '确认整改' : '保存整改信息')), 9 /* TEXT, PROPS */, ["onClick", "disabled"])
            ])
          ])
        : _cE("view", _uM({
            key: 2,
            class: "error-state"
          }), [
            _cE("text", _uM({ class: "error-text" }), "记录不存在或已被删除")
          ])
  ], 4 /* STYLE */)
}
const GenPagesRecordDetailStyles = [_uM([["container", _pS(_uM([["backgroundColor", "#f1f5f9"]]))], ["loading-state", _pS(_uM([["textAlign", "center"], ["paddingTop", 60], ["paddingRight", 0], ["paddingBottom", 60], ["paddingLeft", 0]]))], ["loading-text", _pS(_uM([["fontSize", 16], ["color", "#475569"]]))], ["detail-content", _pS(_uM([["paddingTop", "24rpx"], ["paddingRight", "32rpx"], ["paddingBottom", "130rpx"], ["paddingLeft", "32rpx"]]))], ["status-header", _pS(_uM([["backgroundImage", "linear-gradient(135deg, #1e40af 0%, #2563eb 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["paddingTop", 15], ["paddingRight", 15], ["paddingBottom", 15], ["paddingLeft", 15], ["marginBottom", 15], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"], ["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"]]))], ["page-title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#ffffff"]]))], ["record-status", _pS(_uM([["paddingTop", 6], ["paddingRight", 12], ["paddingBottom", 6], ["paddingLeft", 12], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6], ["fontSize", 14], ["fontWeight", "bold"]]))], ["status-pending", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#fef3c7"], ["color", "#b45309"]]))], ["status-processing", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#dbeafe"], ["color", "#2563eb"]]))], ["status-completed", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#dcfce7"], ["color", "#16a34a"]]))], ["status-accepted", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#1e40af"], ["color", "#ffffff"]]))], ["status-unknown", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#f1f5f9"], ["color", "#475569"]]))], ["severity-low", _pS(_uM([["color", "#16a34a"], ["fontWeight", "bold"]]))], ["severity-medium", _pS(_uM([["color", "#f59e0b"], ["fontWeight", "bold"]]))], ["severity-high", _pS(_uM([["color", "#dc2626"], ["fontWeight", "bold"]]))], ["severity-urgent", _pS(_uM([["color", "#dc2626"], ["fontWeight", "bold"]]))], ["error-state", _pS(_uM([["textAlign", "center"], ["paddingTop", 60], ["paddingRight", 0], ["paddingBottom", 60], ["paddingLeft", 0]]))], ["error-text", _pS(_uM([["fontSize", 16], ["color", "#475569"]]))], ["form-item", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["paddingTop", 15], ["paddingRight", 15], ["paddingBottom", 15], ["paddingLeft", 15], ["marginBottom", 15], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"]]))], ["form-label", _pS(_uM([["fontSize", 16], ["color", "#0f172a"], ["marginBottom", 10], ["fontWeight", 500]]))], ["form-input-wrapper", _pS(_uM([["position", "relative"]]))], ["form-display-text", _pS(_uM([["fontSize", 16], ["color", "#0f172a"], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["backgroundImage", "none"], ["backgroundColor", "#eff6ff"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["minHeight", 20], ["overflowWrap", "break-word"]]))], ["form-textarea", _pS(_uM([["width", "100%"], ["minHeight", 100], ["paddingTop", 12], ["paddingRight", 15], ["paddingBottom", 12], ["paddingLeft", 15], ["backgroundImage", "none"], ["backgroundColor", "#eff6ff"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["fontSize", 16], ["color", "#0f172a"], ["boxSizing", "border-box"], ["borderTopColor:focus", "#2563eb"], ["borderRightColor:focus", "#2563eb"], ["borderBottomColor:focus", "#2563eb"], ["borderLeftColor:focus", "#2563eb"]]))], ["image-upload", _pS(_uM([["marginTop", 10]]))], ["image-list", _pS(_uM([["display", "flex"], ["flexWrap", "wrap"]]))], ["image-item", _uM([[".image-list ", _uM([["marginTop", 5], ["marginRight", 5], ["marginBottom", 5], ["marginLeft", 5]])], ["", _uM([["position", "relative"], ["width", 80], ["height", 80]])]])], ["uploaded-image", _pS(_uM([["width", "100%"], ["height", "100%"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"]]))], ["image-delete", _pS(_uM([["position", "absolute"], ["top", -5], ["right", -5], ["width", 20], ["height", 20], ["backgroundImage", "none"], ["backgroundColor", "#dc2626"], ["color", "#FFFFFF"], ["borderTopLeftRadius", 10], ["borderTopRightRadius", 10], ["borderBottomRightRadius", 10], ["borderBottomLeftRadius", 10], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["fontSize", 12]]))], ["image-add", _pS(_uM([["width", 80], ["height", 80], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "dashed"], ["borderRightStyle", "dashed"], ["borderBottomStyle", "dashed"], ["borderLeftStyle", "dashed"], ["borderTopColor", "#38bdf8"], ["borderRightColor", "#38bdf8"], ["borderBottomColor", "#38bdf8"], ["borderLeftColor", "#38bdf8"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["display", "flex"], ["flexDirection", "column"], ["justifyContent", "center"], ["alignItems", "center"], ["backgroundImage", "none"], ["backgroundColor", "#eff6ff"]]))], ["add-icon", _pS(_uM([["fontSize", 24], ["color", "#2563eb"], ["marginBottom", 5]]))], ["add-text", _pS(_uM([["fontSize", 12], ["color", "#475569"]]))], ["status-selector", _pS(_uM([["display", "flex"], ["flexDirection", "row"], ["marginTop", 15], ["paddingTop", 0], ["paddingRight", 5], ["paddingBottom", 0], ["paddingLeft", 5], ["flexWrap", "nowrap"], ["alignItems", "center"], ["justifyContent", "space-between"]]))], ["status-option", _uM([[".status-selector ", _uM([["marginTop", 0], ["marginRight", 6], ["marginBottom", 0], ["marginLeft", 6]])], ["", _uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["height", 60], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["backgroundImage", "linear-gradient(135deg, #ffffff 0%, #eff6ff 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxShadow", "0 2px 8px rgba(30, 64, 175, 0.08)"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "cubic-bezier(0.4,0,0.2,1)"], ["position", "relative"], ["overflow", "hidden"], ["marginTop", 0], ["marginRight", 6], ["marginBottom", 0], ["marginLeft", 6], ["fontSize", 14], ["fontWeight", 600], ["color", "#0f172a"], ["textAlign", "center"]])], [".pending", _uM([["borderTopColor", "#f59e0b"], ["borderRightColor", "#f59e0b"], ["borderBottomColor", "#f59e0b"], ["borderLeftColor", "#f59e0b"], ["backgroundImage", "none"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#b45309"]])], [".pending.active", _uM([["borderTopColor", "#f59e0b"], ["borderRightColor", "#f59e0b"], ["borderBottomColor", "#f59e0b"], ["borderLeftColor", "#f59e0b"], ["backgroundImage", "linear-gradient(135deg, #f59e0b 0%, #fbbf24 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxShadow", "0 4px 15px rgba(245, 158, 11, 0.32)"], ["transform", "translateY(-2px)"], ["color", "#FFFFFF"]])], [".completed", _uM([["borderTopColor", "#16a34a"], ["borderRightColor", "#16a34a"], ["borderBottomColor", "#16a34a"], ["borderLeftColor", "#16a34a"], ["backgroundImage", "none"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#16a34a"]])], [".completed.active", _uM([["borderTopColor", "#16a34a"], ["borderRightColor", "#16a34a"], ["borderBottomColor", "#16a34a"], ["borderLeftColor", "#16a34a"], ["backgroundImage", "linear-gradient(135deg, #16a34a 0%, #22c55e 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxShadow", "0 4px 15px rgba(22, 163, 74, 0.32)"], ["transform", "translateY(-2px)"], ["color", "#FFFFFF"]])]])], ["save-button-wrapper", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"], ["marginTop", "20rpx"]]))], ["save-btn", _pS(_uM([["width", "100%"], ["paddingTop", "24rpx"], ["paddingRight", "24rpx"], ["paddingBottom", "24rpx"], ["paddingLeft", "24rpx"], ["backgroundImage", "none"], ["backgroundColor", "#1e40af"], ["color", "#ffffff"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["fontSize", "32rpx"], ["fontWeight", 600], ["backgroundImage:disabled", "none"], ["backgroundColor:disabled", "#94a3b8"], ["color:disabled", "#f1f5f9"]]))], ["@TRANSITION", _uM([["status-option", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "cubic-bezier(0.4,0,0.2,1)"]])]])]])]
