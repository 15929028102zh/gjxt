
	import { getApiUrl, apiConfig } from '@/config/api.uts'
	
	type EditForm = { __$originalPosition?: UTSSourceMapPosition<"EditForm", "pages/profile/profile.uvue", 73, 7>;
		realName: string
		phone: string
	}
	
	const __sfc__ = defineComponent({
		data(): {
			userInfo: UTSJSONObject,
			userStats: UTSJSONObject,
			showEditModal: boolean,
			showAvatarModal: boolean,
			editForm: EditForm,
			loading: boolean
		} {
			return {
				userInfo: {} as UTSJSONObject,
				userStats: {} as UTSJSONObject,
				showEditModal: false,
				showAvatarModal: false,
				editForm: {
					realName: '',
					phone: ''
				} as EditForm,
				loading: false
			}
		},
		onLoad() {
			this.checkLogin()
			this.loadUserInfo()
			this.loadUserStats()
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
			
			loadUserInfo() {
			const userInfoStr = uni.getStorageSync('userInfo') as string
			console.log('从本地存储获取的用户信息字符串:', userInfoStr, " at pages/profile/profile.uvue:116")
			
			if (userInfoStr != null && userInfoStr != '') {
				const userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/profile/profile.uvue:119") as UTSJSONObject
			console.log('解析后的用户信息:', userInfo, " at pages/profile/profile.uvue:120")
			console.log('用户ID字段:', userInfo.getString('id'), " at pages/profile/profile.uvue:121")
			console.log('用户信息JSON字符串:', JSON.stringify(userInfo), " at pages/profile/profile.uvue:122")
				
				this.userInfo = userInfo
				this.editForm.realName = userInfo.getString('realName') ?? ''
				this.editForm.phone = userInfo.getString('phone') ?? ''
			} else {
				console.log('本地存储中没有用户信息', " at pages/profile/profile.uvue:128")
			}
		},
			
			loadUserStats() {
				const token = uni.getStorageSync('token')
				uni.request({
					url: getApiUrl('/user/stats'),
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
				const data = res.data as UTSJSONObject
				if (res.statusCode == 200 && (data.getNumber('code') ?? 0) == 200) {
					this.userStats = data.getJSON('data') as UTSJSONObject
				}
			},
			fail: (error) => {
				console.log('加载用户统计失败:', error, " at pages/profile/profile.uvue:147")
			}
				})
			},
			
			getRoleText(role: number): string {
				if (role == 1) {
					return '管理员'
				} else if (role == 2) {
					return '超级管理员'
				} else {
					return '普通用户'
				}
		},
			
			showAvatarOptions() {
				this.showAvatarModal = true
			},
			
			closeAvatarModal() {
				this.showAvatarModal = false
			},
			
			changeAvatar() {
				this.closeAvatarModal()
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album', 'camera'],
					success: (res) => {
						const tempFilePath = res.tempFilePaths[0]
						this.uploadAvatar(tempFilePath)
					},
					fail: (error) => {
						console.log('选择图片失败:', error, " at pages/profile/profile.uvue:181")
					}
				})
			},
			
			uploadAvatar(filePath: string) {
				const token = uni.getStorageSync('token')
				// 先上传图片文件获取URL
				uni.uploadFile({
					url: getApiUrl('/upload/image'),
					filePath: filePath,
					name: 'file',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (res) => {
						// 调试：打印上传响应
						console.log('上传图片响应:', res, " at pages/profile/profile.uvue:198")
						console.log('上传图片响应数据:', res.data, " at pages/profile/profile.uvue:199")
						
						const data = UTSAndroid.consoleDebugError(JSON.parse(res.data), " at pages/profile/profile.uvue:201") as UTSJSONObject
						console.log('解析后的数据:', data, " at pages/profile/profile.uvue:202")
						
						if (res.statusCode === 200 && data.getNumber('code') === 200) {
							const dataObj = data.getJSON('data')
							const imageUrl = dataObj?.getString('filePath') ?? ''
							console.log('获取到的图片URL:', imageUrl, " at pages/profile/profile.uvue:207")
							
							if (imageUrl == null || imageUrl == '') {
								console.log('警告：图片URL为空', " at pages/profile/profile.uvue:210")
								uni.showToast({
									title: '上传成功但未获取到图片URL',
									icon: 'none'
								})
								return
							}
							
							// 调用设置头像接口更新数据库
							this.updateUserAvatar(imageUrl)
						} else {
							console.log('上传失败，状态码:', res.statusCode, '错误码:', data.getNumber('code'), " at pages/profile/profile.uvue:221")
							uni.showToast({
								title: data.getString('message') ?? '上传失败',
								icon: 'none'
							})
						}
					},
					fail: (error) => {
						console.log('上传头像失败:', error, " at pages/profile/profile.uvue:229")
						uni.showToast({
							title: '上传失败',
							icon: 'none'
						})
					}
				})
			},
			
			updateUserAvatar(imageUrl: string) {
				const token = uni.getStorageSync('token')
				
				// 调试：打印用户信息
				console.log('用户信息调试:', this.userInfo, " at pages/profile/profile.uvue:242")
				console.log('用户信息类型:', typeof this.userInfo, " at pages/profile/profile.uvue:243")
				console.log('用户信息字符串:', JSON.stringify(this.userInfo), " at pages/profile/profile.uvue:244")
				
				const username = this.userInfo.getString('username') ?? ''
				console.log('获取到的用户名:', username, " at pages/profile/profile.uvue:247")
				
				if (username == null || username == '') {
					uni.showToast({
						title: '用户信息异常',
						icon: 'none'
					})
					return
				}
				
				uni.request({
					url: getApiUrl(`/user/${username}/update-avatar`),
					method: 'PUT',
					header: {
						'Authorization': `Bearer ${token}`,
						'Content-Type': 'application/json'
					},
					data: {
						imageUrl: imageUrl
					},
					success: (res) => {
						const data = res.data as UTSJSONObject
						if (res.statusCode === 200 && data.getNumber('code') === 200) {
							// 将物理路径转换为HTTP访问路径
							const httpImageUrl = this.convertToHttpUrl(imageUrl)
							// 更新本地用户信息
							this.userInfo.set('avatar', httpImageUrl)
							uni.setStorageSync('userInfo', JSON.stringify(this.userInfo))
							uni.showToast({
								title: '头像更新成功',
								icon: 'success'
							})
						} else {
							uni.showToast({
								title: data.getString('message') ?? '设置头像失败',
								icon: 'none'
							})
						}
					},
					fail: (error) => {
						console.log('设置头像失败:', error, " at pages/profile/profile.uvue:287")
						uni.showToast({
							title: '设置头像失败',
							icon: 'none'
						})
					}
				})
			},
			
			editProfile() {
				this.closeAvatarModal()
				this.showEditModal = true
			},
			
			closeEditModal() {
				this.showEditModal = false
				// 重置表单
				const realName = this.userInfo.getString('realName') ?? ''
				const phone = this.userInfo.getString('phone') ?? ''
				this.editForm.realName = realName != '' ? realName : ''
				this.editForm.phone = phone != '' ? phone : ''
			},
			
			async saveProfile() {
				if (this.editForm.realName.trim() == '') {
					uni.showToast({
						title: '请输入真实姓名',
						icon: 'none'
					})
					return
				}
				
				if (this.editForm.phone != '' && !/^1[3-9]\d{9}$/.test(this.editForm.phone)) {
					uni.showToast({
						title: '请输入正确的手机号',
						icon: 'none'
					})
					return
				}
				
				if (this.loading) return
				
				this.loading = true
				
				const token = uni.getStorageSync('token')
				uni.request({
					url: getApiUrl('/user/profile'),
					method: 'PUT',
					header: {
						'Authorization': `Bearer ${token}`,
						'Content-Type': 'application/json'
					},
					data: {
						realName: this.editForm.realName,
						phone: this.editForm.phone
					},
					success: (res) => {
						const data = res.data as UTSJSONObject
						if (res.statusCode === 200 && data.getNumber('code') === 200) {
						// 更新本地用户信息
						this.userInfo.set('realName', this.editForm.realName)
						this.userInfo.set('phone', this.editForm.phone)
						uni.setStorageSync('userInfo', JSON.stringify(this.userInfo))
						
						uni.showToast({
							title: '保存成功',
							icon: 'success'
						})
						
						this.closeEditModal()
					} else {
						const message = data.getString('message') ?? '保存失败'
						uni.showToast({
							title: message,
							icon: 'none'
						})
					}
					this.loading = false
				},
				fail: (error) => {
					console.error('保存用户信息失败:', error, " at pages/profile/profile.uvue:367")
					uni.showToast({
						title: '保存失败',
						icon: 'none'
					})
					this.loading = false
				}
			})
			},
			
			goToMyRecords() {
				uni.navigateTo({
					url: '/pages/record/record?userId=' + (this.userInfo.getString('id') ?? '')
				})
			},
			
			goToMyTasks() {
				uni.navigateTo({
					url: '/pages/task/task?assigneeId=' + (this.userInfo.getString('id') ?? '')
				})
			},
			
			showSettings() {
				uni.showToast({
					title: '功能开发中',
					icon: 'none'
				})
			},
			
			showHelp() {
				uni.showModal({
					title: '帮助与反馈',
					content: '如有问题请联系管理员\n电话：400-123-4567\n邮箱：support@example.com',
					showCancel: false
				})
			},
			
			showAbout() {
				uni.showModal({
					title: '关于我们',
					content: '工业巡检系统 v1.0.0\n\n专业的工业设备巡检管理平台，提供任务管理、记录跟踪、数据分析等功能。',
					showCancel: false
				})
			},
			
			// 将相对路径转换为HTTP访问路径
			convertToHttpUrl(relativePath: string): string {
				console.log('相对路径:', relativePath, " at pages/profile/profile.uvue:414")
				// 如果已经是HTTP路径，直接返回
				if (relativePath.startsWith('http://') || relativePath.startsWith('https://')) {
					return relativePath
				}
				
				// 直接拼接完整的HTTP URL
				// 后端已返回相对路径，如: /files/images/20250818/xxx.jpg
				const httpUrl = `${apiConfig.baseUrl}/api${relativePath}`
				console.log('转换后的HTTP URL:', httpUrl, " at pages/profile/profile.uvue:423")
				return httpUrl
			},
			
			logout() {
				uni.showModal({
					title: '确认退出',
					content: '确定要退出登录吗？',
					success: (res) => {
						if (res.confirm) {
							// 清除本地存储
							uni.removeStorageSync('token')
							uni.removeStorageSync('userInfo')
							
							// 跳转到登录页
							uni.reLaunch({
								url: '/pages/login/login'
							})
						}
					}
				})
			}
		}
	})

export default __sfc__
function GenPagesProfileProfileRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
  return _cE("view", _uM({ class: "container" }), [
    _cE("view", _uM({ class: "user-section" }), [
      _cE("view", _uM({
        class: "user-avatar",
        onClick: _ctx.showAvatarOptions
      }), [
        _cE("image", _uM({
          src: (_ctx.userInfo.getString('avatar') ?? '') != '' ? (_ctx.userInfo.getString('avatar') ?? '') : '/static/default-avatar.svg',
          mode: "aspectFit"
        }), null, 8 /* PROPS */, ["src"])
      ], 8 /* PROPS */, ["onClick"]),
      _cE("view", _uM({ class: "user-info" }), [
        _cE("text", _uM({ class: "user-name" }), _tD((_ctx.userInfo.getString('realName') ?? '') != '' ? (_ctx.userInfo.getString('realName') ?? '') : '未设置'), 1 /* TEXT */),
        _cE("text", _uM({ class: "user-phone" }), _tD((_ctx.userInfo.getString('phone') ?? '') != '' ? (_ctx.userInfo.getString('phone') ?? '') : '未绑定手机'), 1 /* TEXT */)
      ])
    ]),
    _cE("view", _uM({ class: "logout-section" }), [
      _cE("view", _uM({
        class: "logout-btn",
        onClick: _ctx.logout
      }), [
        _cE("text", null, "退出登录")
      ], 8 /* PROPS */, ["onClick"])
    ]),
    isTrue(_ctx.showAvatarModal)
      ? _cE("view", _uM({
          key: 0,
          class: "modal",
          onClick: _ctx.closeAvatarModal
        }), [
          _cE("view", _uM({
            class: "modal-content avatar-options",
            onClick: withModifiers(() => {}, ["stop"])
          }), [
            _cE("view", _uM({
              class: "option-item",
              onClick: _ctx.editProfile
            }), [
              _cE("text", _uM({ class: "option-text" }), "编辑资料")
            ], 8 /* PROPS */, ["onClick"]),
            _cE("view", _uM({
              class: "option-item",
              onClick: _ctx.changeAvatar
            }), [
              _cE("text", _uM({ class: "option-text" }), "修改头像")
            ], 8 /* PROPS */, ["onClick"])
          ], 8 /* PROPS */, ["onClick"])
        ], 8 /* PROPS */, ["onClick"])
      : _cC("v-if", true),
    isTrue(_ctx.showEditModal)
      ? _cE("view", _uM({
          key: 1,
          class: "modal",
          onClick: _ctx.closeEditModal
        }), [
          _cE("view", _uM({
            class: "modal-content",
            onClick: withModifiers(() => {}, ["stop"])
          }), [
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "真实姓名"),
              _cE("input", _uM({
                class: "form-input",
                modelValue: _ctx.editForm.realName,
                onInput: ($event: UniInputEvent) => {(_ctx.editForm.realName) = $event.detail.value},
                placeholder: "请输入真实姓名"
              }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
            ]),
            _cE("view", _uM({ class: "form-item" }), [
              _cE("text", _uM({ class: "form-label" }), "手机号码"),
              _cE("input", _uM({
                class: "form-input",
                modelValue: _ctx.editForm.phone,
                onInput: ($event: UniInputEvent) => {(_ctx.editForm.phone) = $event.detail.value},
                placeholder: "请输入手机号码",
                type: "number"
              }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
            ]),
            _cE("view", _uM({ class: "modal-footer" }), [
              _cE("view", _uM({
                class: "btn-cancel",
                onClick: _ctx.closeEditModal
              }), [
                _cE("text", null, "取消")
              ], 8 /* PROPS */, ["onClick"]),
              _cE("view", _uM({
                class: "btn-confirm",
                onClick: _ctx.saveProfile
              }), [
                _cE("text", null, "保存")
              ], 8 /* PROPS */, ["onClick"])
            ])
          ], 8 /* PROPS */, ["onClick"])
        ], 8 /* PROPS */, ["onClick"])
      : _cC("v-if", true)
  ])
}
const GenPagesProfileProfileStyles = [_uM([["container", _pS(_uM([["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["backgroundColor", "#f5f5f5"]]))], ["user-section", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["paddingTop", 25], ["paddingRight", 20], ["paddingBottom", 25], ["paddingLeft", 20], ["marginBottom", 20], ["display", "flex"], ["alignItems", "center"], ["boxShadow", "0 2px 8px rgba(0,0,0,0.1)"]]))], ["user-avatar", _pS(_uM([["width", 70], ["height", 70], ["borderTopLeftRadius", 35], ["borderTopRightRadius", 35], ["borderBottomRightRadius", 35], ["borderBottomLeftRadius", 35], ["overflow", "hidden"], ["marginRight", 15], ["backgroundImage", "none"], ["backgroundColor", "#f0f0f0"], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e0e0e0"], ["borderRightColor", "#e0e0e0"], ["borderBottomColor", "#e0e0e0"], ["borderLeftColor", "#e0e0e0"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["cursor", "pointer"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["position", "relative"], ["content::after", "''"], ["position::after", "absolute"], ["top::after", 0], ["left::after", 0], ["right::after", 0], ["bottom::after", 0], ["backgroundImage::after", "linear-gradient(135deg, rgba(0, 122, 255, 0.1) 0%, rgba(90, 200, 250, 0.1) 100%)"], ["backgroundColor::after", "rgba(0,0,0,0)"], ["opacity::after", 0], ["transitionProperty::after", "opacity"], ["transitionDuration::after", "0.3s"], ["transitionTimingFunction::after", "ease"], ["transform:active", "scale(0.95)"], ["boxShadow:active", "0 4px 20px rgba(0, 122, 255, 0.3)"], ["opacity:active::after", 1]]))], ["user-info", _pS(_uM([["flex", 1]]))], ["user-name", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#333333"], ["marginBottom", 5]]))], ["user-role", _pS(_uM([["fontSize", 14], ["color", "#007AFF"], ["marginBottom", 3]]))], ["user-phone", _pS(_uM([["fontSize", 12], ["color", "#999999"]]))], ["stats-section", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["marginBottom", 20], ["display", "flex"], ["justifyContent", "space-around"], ["boxShadow", "0 2px 8px rgba(0,0,0,0.1)"]]))], ["stats-item", _pS(_uM([["textAlign", "center"]]))], ["stats-number", _pS(_uM([["fontSize", 24], ["fontWeight", "bold"], ["color", "#007AFF"], ["marginBottom", 5]]))], ["stats-label", _pS(_uM([["fontSize", 12], ["color", "#666666"]]))], ["menu-group", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["marginBottom", 15], ["overflow", "hidden"], ["boxShadow", "0 2px 8px rgba(0,0,0,0.1)"]]))], ["menu-item", _pS(_uM([["display", "flex"], ["alignItems", "center"], ["paddingTop", 18], ["paddingRight", 20], ["paddingBottom", 18], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"], ["borderBottomWidth:last-child", "medium"], ["borderBottomStyle:last-child", "none"], ["borderBottomColor:last-child", "#000000"]]))], ["menu-icon", _pS(_uM([["width", 24], ["height", 24], ["marginRight", 15], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["menu-title", _pS(_uM([["flex", 1], ["fontSize", 16], ["color", "#333333"]]))], ["menu-arrow", _pS(_uM([["fontSize", 16], ["color", "#cccccc"]]))], ["logout-section", _pS(_uM([["marginTop", 30]]))], ["logout-btn", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["paddingTop", 18], ["paddingRight", 18], ["paddingBottom", 18], ["paddingLeft", 18], ["textAlign", "center"], ["boxShadow", "0 2px 8px rgba(0,0,0,0.1)"]]))], ["modal", _pS(_uM([["position", "fixed"], ["top", 0], ["left", 0], ["right", 0], ["bottom", 0], ["backgroundImage", "none"], ["backgroundColor", "rgba(0,0,0,0.5)"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["zIndex", 1000]]))], ["modal-content", _pS(_uM([["backgroundImage", "linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["borderTopLeftRadius", 16], ["borderTopRightRadius", 16], ["borderBottomRightRadius", 16], ["borderBottomLeftRadius", 16], ["width", "80%"], ["maxWidth", 400], ["overflow", "hidden"], ["paddingTop", 25], ["paddingRight", 25], ["paddingBottom", 25], ["paddingLeft", 25], ["boxShadow", "0 12px 40px rgba(0, 0, 0, 0.15)"]]))], ["modal-header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"]]))], ["modal-title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"], ["color", "#333333"]]))], ["modal-close", _pS(_uM([["fontSize", 24], ["color", "#999999"], ["width", 30], ["height", 30], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["form-item", _pS(_uM([["marginBottom", 24], ["position", "relative"]]))], ["form-label", _pS(_uM([["fontSize", 15], ["color", "#2c3e50"], ["marginBottom", 10]]))], ["form-input", _pS(_uM([["width", "100%"], ["height", 50], ["paddingTop", 0], ["paddingRight", 18], ["paddingBottom", 0], ["paddingLeft", 18], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e9ecef"], ["borderRightColor", "#e9ecef"], ["borderBottomColor", "#e9ecef"], ["borderLeftColor", "#e9ecef"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["fontSize", 16], ["boxSizing", "border-box"], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["boxShadow", "0 2px 8px rgba(0, 0, 0, 0.04)"], ["borderTopColor:focus", "#007AFF"], ["borderRightColor:focus", "#007AFF"], ["borderBottomColor:focus", "#007AFF"], ["borderLeftColor:focus", "#007AFF"], ["boxShadow:focus", "0 0 0 3px rgba(0, 122, 255, 0.1)"], ["outline:focus", "none"], ["transform:focus", "translateY(-1px)"], ["color::placeholder", "#adb5bd"], ["fontSize::placeholder", 15]]))], ["modal-footer", _pS(_uM([["flexDirection", "row"], ["paddingTop", 25], ["paddingRight", 0], ["paddingBottom", 0], ["paddingLeft", 0], ["borderTopWidth", "medium"], ["borderTopStyle", "none"], ["borderTopColor", "#000000"], ["gap", "15px"]]))], ["btn-cancel", _pS(_uM([["flex", 1], ["height", 50], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["boxShadow", "0 4px 12px rgba(0, 0, 0, 0.08)"], ["position", "relative"], ["overflow", "hidden"], ["backgroundImage", "linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dee2e6"], ["borderRightColor", "#dee2e6"], ["borderBottomColor", "#dee2e6"], ["borderLeftColor", "#dee2e6"], ["transform:active", "translateY(1px) scale(0.98)"], ["boxShadow:active", "0 2px 6px rgba(0, 0, 0, 0.12)"]]))], ["btn-confirm", _pS(_uM([["flex", 1], ["height", 50], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["boxShadow", "0 4px 12px rgba(0, 0, 0, 0.08)"], ["position", "relative"], ["overflow", "hidden"], ["backgroundImage", "linear-gradient(135deg, #007AFF 0%, #5AC8FA 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "rgba(0,0,0,0)"], ["borderRightColor", "rgba(0,0,0,0)"], ["borderBottomColor", "rgba(0,0,0,0)"], ["borderLeftColor", "rgba(0,0,0,0)"], ["transform:active", "translateY(1px) scale(0.98)"], ["boxShadow:active", "0 2px 6px rgba(0, 122, 255, 0.3)"]]))], ["avatar-options", _pS(_uM([["width", "75%"], ["maxWidth", 320], ["borderTopLeftRadius", 16], ["borderTopRightRadius", 16], ["borderBottomRightRadius", 16], ["borderBottomLeftRadius", 16], ["overflow", "hidden"], ["boxShadow", "0 8px 32px rgba(0, 0, 0, 0.12)"], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["position", "relative"], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20]]))], ["option-item", _pS(_uM([["paddingTop", 16], ["paddingRight", 24], ["paddingBottom", 16], ["paddingLeft", 24], ["marginBottom", 15], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["backgroundImage", "none"], ["backgroundColor", "#f8f9fa"], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "rgba(0,0,0,0)"], ["borderRightColor", "rgba(0,0,0,0)"], ["borderBottomColor", "rgba(0,0,0,0)"], ["borderLeftColor", "rgba(0,0,0,0)"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["transitionProperty", "all"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["position", "relative"], ["boxShadow", "0 2px 8px rgba(0, 0, 0, 0.06)"], ["marginBottom:last-child", 0], ["transform:active", "translateY(1px) scale(0.98)"], ["boxShadow:active", "0 1px 4px rgba(0, 0, 0, 0.1)"], ["backgroundImage:first-child", "linear-gradient(135deg, #e3f2fd 0%, #f0f8ff 100%)"], ["backgroundColor:first-child", "rgba(0,0,0,0)"], ["borderTopColor:first-child", "#007AFF"], ["borderRightColor:first-child", "#007AFF"], ["borderBottomColor:first-child", "#007AFF"], ["borderLeftColor:first-child", "#007AFF"], ["backgroundImage:first-child:active", "linear-gradient(135deg, #007AFF 0%, #5AC8FA 100%)"], ["backgroundColor:first-child:active", "rgba(0,0,0,0)"], ["backgroundImage:last-child", "linear-gradient(135deg, #fff3e0 0%, #ffeaa7 100%)"], ["backgroundColor:last-child", "rgba(0,0,0,0)"], ["borderTopColor:last-child", "#FF6B35"], ["borderRightColor:last-child", "#FF6B35"], ["borderBottomColor:last-child", "#FF6B35"], ["borderLeftColor:last-child", "#FF6B35"], ["backgroundImage:last-child:active", "linear-gradient(135deg, #FF6B35 0%, #ff8a50 100%)"], ["backgroundColor:last-child:active", "rgba(0,0,0,0)"]]))], ["option-text", _pS(_uM([["fontSize", 16], ["color", "#333333"], ["transitionProperty", "color"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"]]))], ["@TRANSITION", _uM([["form-input", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "ease"]])], ["btn-cancel", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "ease"]])], ["btn-confirm", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "ease"]])], ["option-item", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "ease"]])], ["option-text", _uM([["property", "color"], ["duration", "0.3s"], ["timingFunction", "ease"]])], ["user-avatar", _uM([["property", "all"], ["duration", "0.3s"], ["timingFunction", "ease"], ["property::after", "opacity"], ["duration::after", "0.3s"], ["timingFunction::after", "ease"]])]])]])]
