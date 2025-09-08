
	import { getApiUrl, apiConfig } from '@/config/api.uts'
	type LoginFormData = { __$originalPosition?: UTSSourceMapPosition<"LoginFormData", "pages/login/login.uvue", 51, 7>;
		username: string
		password: string
	}
	
	const __sfc__ = defineComponent({
		data(): {
			form: LoginFormData,
			showPassword: boolean,
			loading: boolean
		} {
			return {
				form: {
					username: '',
					password: ''
				} as LoginFormData,
				showPassword: false,
				loading: false
			}
		},
		methods: {
			togglePassword() {
				this.showPassword = !this.showPassword
			},
			
			validateForm(): boolean {
			if (this.form.username.trim() == '') {
				uni.showToast({
					title: '请输入用户名',
					icon: 'none'
				})
				return false
			}
			if (this.form.password.trim() == '') {
				uni.showToast({
					title: '请输入密码',
					icon: 'none'
				})
				return false
			}
			return true
		},
			
			getUserInfo(token: string) {
		const apiUrl = getApiUrl(`/user/info?username=${this.form.username}`)
		console.log('获取用户信息API地址:', apiUrl, " at pages/login/login.uvue:96")
		uni.request({
			url: apiUrl,
			method: 'GET',
			header: {
				'Content-Type': 'application/json',
				'Authorization': 'Bearer ' + token
			},
				success: (res) => {
					console.log('获取用户信息响应:', res, " at pages/login/login.uvue:105")
					const data = res.data as UTSJSONObject
					console.log('响应数据:', data, " at pages/login/login.uvue:107")
					
					if (res.statusCode == 200 && (data.getNumber('code') ?? 0) == 200) {
						const userInfo = data.getJSON('data')
						console.log('从后端获取的用户信息:', userInfo, " at pages/login/login.uvue:111")
						
						if (userInfo != null) {
							console.log('保存用户信息到本地存储:', JSON.stringify(userInfo), " at pages/login/login.uvue:114")
							uni.setStorageSync('userInfo', JSON.stringify(userInfo))
							
							// 设置用户头像
							this.updateUserAvatar(userInfo)
						}
					} else {
						console.log('获取用户信息失败，使用基本信息', " at pages/login/login.uvue:121")
						// 如果获取用户信息失败，使用基本信息
						uni.setStorageSync('userInfo', JSON.stringify({ 
							id: 'temp_' + Date.now(),
							username: this.form.username,
							realName: this.form.username
						}))
					}
				},
				fail: (error) => {
					console.log('获取用户信息失败:', error, " at pages/login/login.uvue:131")
					// 如果获取用户信息失败，使用基本信息
					uni.setStorageSync('userInfo', JSON.stringify({ 
						id: 'temp_' + Date.now(),
						username: this.form.username,
						realName: this.form.username
					}))
				}
			})
		},
			
		handleLogin() {
			if (!this.validateForm()) return
			
			this.loading = true
			const loginUrl = getApiUrl('/user/login')
			console.log('登录API地址:', loginUrl, " at pages/login/login.uvue:147")
		
			uni.request({
				url: loginUrl,
				method: 'POST',
				header: {
					'Content-Type': 'application/json'
				},
				data: JSON.stringify(this.form),
				success: (res) => {
					const data = res.data as UTSJSONObject
					if (res.statusCode == 200 && (data.getNumber('code') ?? 0) == 200) {
						// 保存token
						const token = data.getString('data') ?? ''
						uni.setStorageSync('token', token)
						
						// 获取用户详细信息
						this.getUserInfo(token)
						
						uni.showToast({
							title: '登录成功',
							icon: 'success'
						})
						
						// 跳转到首页
						setTimeout(() => {
							uni.reLaunch({
								url: '/pages/home/home'
							})
						}, 1500) as number
					} else {
						uni.showToast({
							title: data.getString('message') ?? '登录失败',
							icon: 'none'
						})
					}
					this.loading = false
				},
				fail: (error) => {
					console.log('登录请求失败:', error, " at pages/login/login.uvue:186")
					uni.showToast({
						title: '网络请求失败',
						icon: 'none'
					})
					this.loading = false
				}
			})
		},
		
		updateUserAvatar(userInfo: UTSJSONObject) {
			const imageUrl = userInfo.getString('imageUrl')
			if (imageUrl != null && imageUrl != '') {
				console.log('用户已有头像，设置头像:', imageUrl, " at pages/login/login.uvue:199")
				// 将物理路径转换为HTTP访问路径
				const httpImageUrl = this.convertToHttpUrl(imageUrl)
				// 更新本地用户信息中的头像
				userInfo.set('avatar', httpImageUrl)
				uni.setStorageSync('userInfo', JSON.stringify(userInfo))
				console.log('头像已更新到本地存储:', httpImageUrl, " at pages/login/login.uvue:205")
			} else {
				console.log('用户暂无头像', " at pages/login/login.uvue:207")
			}
		},
		
		// 将相对路径转换为HTTP访问路径
		convertToHttpUrl(relativePath: string): string {
			console.log('相对路径:', relativePath, " at pages/login/login.uvue:213")
			// 如果已经是HTTP路径，直接返回
			if (relativePath.startsWith('http://') || relativePath.startsWith('https://')) {
				return relativePath
			}
			
			// 直接拼接完整的HTTP URL
			// 后端已返回相对路径，如: /files/images/20250818/xxx.jpg
			const httpUrl = `${apiConfig.baseUrl}/api${relativePath}`
			console.log('转换后的HTTP URL:', httpUrl, " at pages/login/login.uvue:222")
			return httpUrl
		},
		
		onLoad() {
			// 检查是否已登录
			const token = uni.getStorageSync('token')
			if (token != null && token != '') {
				uni.reLaunch({
					url: '/pages/home/home'
				})
			}
		}
	}
	})

export default __sfc__
function GenPagesLoginLoginRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
  return _cE("view", _uM({ class: "login-container" }), [
    _cE("view", _uM({ class: "login-header" }), [
      _cE("image", _uM({
        class: "logo",
        src: "/static/logo.png"
      })),
      _cE("text", _uM({ class: "title" }), "石油工业园区设备巡检系统"),
      _cE("text", _uM({ class: "subtitle" }), "请登录您的账号")
    ]),
    _cE("view", _uM({ class: "login-form" }), [
      _cE("view", _uM({ class: "form-item" }), [
        _cE("view", _uM({ class: "input-wrapper" }), [
          _cE("text", _uM({ class: "icon" }), "👤"),
          _cE("input", _uM({
            class: "input",
            type: "text",
            placeholder: "请输入用户名",
            modelValue: _ctx.form.username,
            onInput: ($event: UniInputEvent) => {(_ctx.form.username) = $event.detail.value},
            maxlength: "20"
          }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
        ])
      ]),
      _cE("view", _uM({ class: "form-item" }), [
        _cE("view", _uM({ class: "input-wrapper" }), [
          _cE("text", _uM({ class: "icon" }), "🔒"),
          _cE("input", _uM({
            class: "input",
            type: _ctx.showPassword ? 'text' : 'password',
            placeholder: "请输入密码",
            modelValue: _ctx.form.password,
            onInput: ($event: UniInputEvent) => {(_ctx.form.password) = $event.detail.value},
            maxlength: "20"
          }), null, 40 /* PROPS, NEED_HYDRATION */, ["type", "modelValue", "onInput"]),
          _cE("text", _uM({
            class: "eye-icon",
            onClick: _ctx.togglePassword
          }), _tD(_ctx.showPassword ? '👁️' : '👁️‍🗨️'), 9 /* TEXT, PROPS */, ["onClick"])
        ])
      ]),
      _cE("button", _uM({
        class: "login-btn",
        onClick: _ctx.handleLogin,
        disabled: _ctx.loading
      }), [
        isTrue(_ctx.loading)
          ? _cE("text", _uM({ key: 0 }), "登录中...")
          : _cE("text", _uM({ key: 1 }), "登录")
      ], 8 /* PROPS */, ["onClick", "disabled"]),
      _cE("view", _uM({ class: "tips" }), [
        _cE("text", _uM({ class: "tip-text" }), "测试账号：admin / 123456")
      ])
    ])
  ])
}
const GenPagesLoginLoginStyles = [_uM([["login-container", _pS(_uM([["minHeight", 800], ["backgroundImage", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["flexDirection", "column"], ["justifyContent", "center"], ["alignItems", "center"], ["paddingTop", 40], ["paddingRight", 30], ["paddingBottom", 40], ["paddingLeft", 30]]))], ["login-header", _pS(_uM([["textAlign", "center"], ["marginBottom", 60]]))], ["logo", _pS(_uM([["width", 80], ["height", 80], ["borderTopLeftRadius", 16], ["borderTopRightRadius", 16], ["borderBottomRightRadius", 16], ["borderBottomLeftRadius", 16], ["marginBottom", 20]]))], ["title", _pS(_uM([["fontSize", 24], ["fontWeight", "bold"], ["color", "#ffffff"], ["marginBottom", 8]]))], ["subtitle", _pS(_uM([["fontSize", 16], ["color", "rgba(255,255,255,0.8)"]]))], ["login-form", _pS(_uM([["width", "100%"], ["maxWidth", 400]]))], ["form-item", _pS(_uM([["marginBottom", 20]]))], ["input-wrapper", _pS(_uM([["position", "relative"], ["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.9)"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["flexDirection", "row"], ["alignItems", "center"], ["paddingTop", 0], ["paddingRight", 20], ["paddingBottom", 0], ["paddingLeft", 20], ["height", 65], ["width", "100%"], ["boxSizing", "border-box"]]))], ["icon", _pS(_uM([["fontSize", 18], ["marginRight", 12], ["color", "#666666"], ["flexShrink", 0]]))], ["input", _pS(_uM([["flex", 1], ["height", "100%"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["backgroundImage", "none"], ["backgroundColor", "rgba(0,0,0,0)"], ["fontSize", 16], ["color", "#333333"], ["paddingLeft", 0]]))], ["eye-icon", _pS(_uM([["fontSize", 18], ["color", "#666666"]]))], ["login-btn", _pS(_uM([["width", "100%"], ["height", 50], ["backgroundImage", "none"], ["backgroundColor", "#007AFF"], ["color", "#ffffff"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", 12], ["borderTopRightRadius", 12], ["borderBottomRightRadius", 12], ["borderBottomLeftRadius", 12], ["fontSize", 16], ["fontWeight", "bold"], ["marginTop", 30], ["marginBottom", 20], ["backgroundImage:disabled", "none"], ["backgroundColor:disabled", "#cccccc"]]))], ["tips", _pS(_uM([["textAlign", "center"]]))], ["tip-text", _pS(_uM([["fontSize", 14], ["color", "rgba(255,255,255,0.7)"]]))]])]
