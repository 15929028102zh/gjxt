
	import { getApiUrl, apiConfig } from '@/config/api.uts'
	type LoginFormData = { __$originalPosition?: UTSSourceMapPosition<"LoginFormData", "pages/login/login.uvue", 48, 7>;
		username: string
		password: string
	}

	const __sfc__ = defineComponent({
		data() {
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
		console.log('获取用户信息API地址:', apiUrl, " at pages/login/login.uvue:89")
		uni.request({
			url: apiUrl,
			method: 'GET',
			header: {
				'Content-Type': 'application/json',
				'Authorization': 'Bearer ' + token
			},
				success: (res) => {
					console.log('获取用户信息响应:', res, " at pages/login/login.uvue:98")
					const data = res.data as UTSJSONObject
					console.log('响应数据:', data, " at pages/login/login.uvue:100")
					
					if (res.statusCode == 200 && (data.getNumber('code') ?? 0) == 200) {
						const userInfo = data.getJSON('data')
						console.log('从后端获取的用户信息:', userInfo, " at pages/login/login.uvue:104")
						
						if (userInfo != null) {
							console.log('保存用户信息到本地存储:', JSON.stringify(userInfo), " at pages/login/login.uvue:107")
							uni.setStorageSync('userInfo', JSON.stringify(userInfo))
							
							// 设置用户头像
							this.updateUserAvatar(userInfo)
						}
					} else {
						console.log('获取用户信息失败，使用基本信息', " at pages/login/login.uvue:114")
						// 如果获取用户信息失败，使用基本信息
						uni.setStorageSync('userInfo', JSON.stringify({ 
							id: 'temp_' + Date.now(),
							username: this.form.username,
							realName: this.form.username
						}))
					}
				},
				fail: (error) => {
					console.log('获取用户信息失败:', error, " at pages/login/login.uvue:124")
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
			console.log('登录API地址:', loginUrl, " at pages/login/login.uvue:140")
		
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
						// 记录token过期时间戳(与后端 jwt.expiration 24小时一致)，供各页 checkLogin 主动校验，
						// 避免token过期后所有需认证接口静默返回403（对齐Web端cookie过期→重新登录的机制）
						uni.setStorageSync('tokenExpires', (Date.now() + 24 * 60 * 60 * 1000).toString())
						
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
						}, 1500)
					} else {
						uni.showToast({
							title: data.getString('message') ?? '登录失败',
							icon: 'none'
						})
					}
					this.loading = false
				},
				fail: (error) => {
					console.log('登录请求失败:', error, " at pages/login/login.uvue:182")
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
				console.log('用户已有头像，设置头像:', imageUrl, " at pages/login/login.uvue:195")
				// 将物理路径转换为HTTP访问路径
				const httpImageUrl = this.convertToHttpUrl(imageUrl)
				// 更新本地用户信息中的头像
				userInfo.set('avatar', httpImageUrl)
				uni.setStorageSync('userInfo', JSON.stringify(userInfo))
				console.log('头像已更新到本地存储:', httpImageUrl, " at pages/login/login.uvue:201")
			} else {
				console.log('用户暂无头像', " at pages/login/login.uvue:203")
			}
		},
		
		// 将相对路径转换为HTTP访问路径
		convertToHttpUrl(relativePath: string): string {
			console.log('相对路径:', relativePath, " at pages/login/login.uvue:209")
			// 如果已经是HTTP路径，直接返回
			if (relativePath.startsWith('http://') || relativePath.startsWith('https://')) {
				return relativePath
			}
			
			// 直接拼接完整的HTTP URL
			// 后端已返回相对路径，如: /files/images/20250818/xxx.jpg
			const httpUrl = `${apiConfig.baseUrl}/api${relativePath}`
			console.log('转换后的HTTP URL:', httpUrl, " at pages/login/login.uvue:218")
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
      _cE("text", _uM({ class: "system-title" }), "榆林公司岗检系统"),
      _cE("text", _uM({ class: "system-subtitle" }), "ENTERPRISE INSPECTION PLATFORM")
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
        type: "button",
        onClick: _ctx.handleLogin,
        disabled: _ctx.loading
      }), _tD(_ctx.loading ? '登录中...' : '登录'), 9 /* TEXT, PROPS */, ["onClick", "disabled"])
    ])
  ])
}
const GenPagesLoginLoginStyles = [_uM([["login-container", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["backgroundColor", "#14295e"], ["backgroundImage", "linear-gradient(160deg, #061a33 0%, #14295e 45%, #1e40af 100%)"], ["flexDirection", "column"], ["justifyContent", "center"], ["alignItems", "center"], ["paddingTop", 80], ["paddingRight", 32], ["paddingBottom", 40], ["paddingLeft", 32], ["position", "relative"], ["overflow", "hidden"], ["content::before", "''"], ["position::before", "absolute"], ["top::before", "-160rpx"], ["right::before", "-120rpx"], ["width::before", "480rpx"], ["height::before", "480rpx"], ["pointerEvents::before", "none"], ["content::after", "''"], ["position::after", "absolute"], ["bottom::after", "-200rpx"], ["left::after", "-160rpx"], ["width::after", "560rpx"], ["height::after", "560rpx"], ["pointerEvents::after", "none"]]))], ["login-header", _pS(_uM([["textAlign", "center"], ["marginBottom", "64rpx"], ["alignItems", "center"], ["justifyContent", "center"], ["position", "relative"], ["zIndex", 2]]))], ["logo", _pS(_uM([["width", "144rpx"], ["height", "144rpx"], ["borderTopLeftRadius", "28rpx"], ["borderTopRightRadius", "28rpx"], ["borderBottomRightRadius", "28rpx"], ["borderBottomLeftRadius", "28rpx"], ["marginBottom", "28rpx"], ["alignSelf", "center"], ["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.95)"], ["paddingTop", "12rpx"], ["paddingRight", "12rpx"], ["paddingBottom", "12rpx"], ["paddingLeft", "12rpx"], ["boxShadow", "0 16rpx 40rpx rgba(8, 47, 110, 0.35)"]]))], ["system-title", _pS(_uM([["fontSize", "38rpx"], ["fontWeight", 700], ["color", "#ffffff"], ["marginBottom", "12rpx"], ["textAlign", "center"], ["letterSpacing", "4rpx"]]))], ["system-subtitle", _pS(_uM([["fontSize", "24rpx"], ["color", "rgba(219,234,254,0.95)"], ["letterSpacing", "6rpx"], ["textAlign", "center"]]))], ["login-form", _pS(_uM([["width", "100%"], ["maxWidth", "560rpx"], ["position", "relative"], ["zIndex", 2]]))], ["form-item", _pS(_uM([["marginBottom", "28rpx"]]))], ["input-wrapper", _pS(_uM([["position", "relative"], ["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.96)"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["flexDirection", "row"], ["alignItems", "center"], ["paddingTop", 0], ["paddingRight", "28rpx"], ["paddingBottom", 0], ["paddingLeft", "28rpx"], ["height", "96rpx"], ["width", "100%"], ["boxSizing", "border-box"], ["boxShadow", "0 8rpx 24rpx rgba(8, 47, 110, 0.18)"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "rgba(0,0,0,0)"], ["borderRightColor", "rgba(0,0,0,0)"], ["borderBottomColor", "rgba(0,0,0,0)"], ["borderLeftColor", "rgba(0,0,0,0)"], ["transitionProperty", "borderColor"], ["transitionDuration", "0.2s"], ["transitionTimingFunction", "ease"], ["borderTopColor:focus-within", "#60a5fa"], ["borderRightColor:focus-within", "#60a5fa"], ["borderBottomColor:focus-within", "#60a5fa"], ["borderLeftColor:focus-within", "#60a5fa"], ["boxShadow:focus-within", "0 8rpx 28rpx rgba(37, 99, 235, 0.35)"]]))], ["icon", _pS(_uM([["fontSize", "32rpx"], ["marginRight", "16rpx"], ["color", "#2563eb"], ["flexShrink", 0]]))], ["input", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["height", "100%"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["backgroundImage", "none"], ["backgroundColor", "rgba(0,0,0,0)"], ["fontSize", "30rpx"], ["color", "#0f172a"], ["paddingLeft", 0]]))], ["eye-icon", _pS(_uM([["fontSize", "30rpx"], ["color", "#64748b"], ["paddingTop", "8rpx"], ["paddingRight", "8rpx"], ["paddingBottom", "8rpx"], ["paddingLeft", "8rpx"]]))], ["login-btn", _pS(_uM([["width", "100%"], ["paddingTop", "24rpx"], ["paddingRight", "24rpx"], ["paddingBottom", "24rpx"], ["paddingLeft", "24rpx"], ["backgroundImage", "none"], ["backgroundColor", "#1e40af"], ["color", "#ffffff"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["fontSize", "32rpx"], ["fontWeight", 600], ["marginTop", "48rpx"], ["marginBottom", "24rpx"], ["color::after", "#ffffff"], ["backgroundColor::after", "rgba(0,0,0,0)"], ["borderTopWidth::after", "medium"], ["borderRightWidth::after", "medium"], ["borderBottomWidth::after", "medium"], ["borderLeftWidth::after", "medium"], ["borderTopStyle::after", "none"], ["borderRightStyle::after", "none"], ["borderBottomStyle::after", "none"], ["borderLeftStyle::after", "none"], ["borderTopColor::after", "#000000"], ["borderRightColor::after", "#000000"], ["borderBottomColor::after", "#000000"], ["borderLeftColor::after", "#000000"], ["backgroundImage:active", "none"], ["backgroundColor:active", "#1e3a8a"], ["backgroundImage:disabled", "none"], ["backgroundColor:disabled", "#94a3b8"], ["color:disabled", "#ffffff"], ["color:disabled::after", "#ffffff"], ["backgroundColor:disabled::after", "rgba(0,0,0,0)"]]))], ["@TRANSITION", _uM([["input-wrapper", _uM([["property", "borderColor"], ["duration", "0.2s"], ["timingFunction", "ease"]])]])]])]
