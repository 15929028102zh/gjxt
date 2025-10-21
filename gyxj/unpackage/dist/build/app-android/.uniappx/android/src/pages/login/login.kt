@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uni.UNIB5A0920
import io.dcloud.uniapp.*
import io.dcloud.uniapp.extapi.*
import io.dcloud.uniapp.framework.*
import io.dcloud.uniapp.runtime.*
import io.dcloud.uniapp.vue.*
import io.dcloud.uniapp.vue.shared.*
import io.dcloud.uts.*
import io.dcloud.uts.Map
import io.dcloud.uts.Set
import io.dcloud.uts.UTSAndroid
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.setStorageSync as uni_setStorageSync
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesLoginLogin : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("view", _uM("class" to "login-container"), _uA(
            _cE("view", _uM("class" to "login-header"), _uA(
                _cE("image", _uM("class" to "logo", "src" to "/static/logo.png")),
                _cE("text", _uM("class" to "system-title"), "榆林公司岗检系统")
            )),
            _cE("view", _uM("class" to "login-form"), _uA(
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("view", _uM("class" to "input-wrapper"), _uA(
                        _cE("text", _uM("class" to "icon"), "👤"),
                        _cE("input", _uM("class" to "input", "type" to "text", "placeholder" to "请输入用户名", "modelValue" to _ctx.form.username, "onInput" to fun(`$event`: UniInputEvent){
                            _ctx.form.username = `$event`.detail.value
                        }
                        , "maxlength" to "20"), null, 40, _uA(
                            "modelValue",
                            "onInput"
                        ))
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("view", _uM("class" to "input-wrapper"), _uA(
                        _cE("text", _uM("class" to "icon"), "🔒"),
                        _cE("input", _uM("class" to "input", "type" to if (_ctx.showPassword) {
                            "text"
                        } else {
                            "password"
                        }
                        , "placeholder" to "请输入密码", "modelValue" to _ctx.form.password, "onInput" to fun(`$event`: UniInputEvent){
                            _ctx.form.password = `$event`.detail.value
                        }
                        , "maxlength" to "20"), null, 40, _uA(
                            "type",
                            "modelValue",
                            "onInput"
                        )),
                        _cE("text", _uM("class" to "eye-icon", "onClick" to _ctx.togglePassword), _tD(if (_ctx.showPassword) {
                            "👁️"
                        } else {
                            "👁️‍🗨️"
                        }
                        ), 9, _uA(
                            "onClick"
                        ))
                    ))
                )),
                _cE("button", _uM("class" to "login-btn", "onClick" to _ctx.handleLogin, "disabled" to _ctx.loading), _uA(
                    if (isTrue(_ctx.loading)) {
                        _cE("text", _uM("key" to 0), "登录中...")
                    } else {
                        _cE("text", _uM("key" to 1), "登录")
                    }
                ), 8, _uA(
                    "onClick",
                    "disabled"
                )),
                _cE("view", _uM("class" to "tips"), _uA(
                    _cE("text", _uM("class" to "tip-text"), "测试账号：admin / 123456")
                ))
            ))
        ))
    }
    open var form: LoginFormData by `$data`
    open var showPassword: Boolean by `$data`
    open var loading: Boolean by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("form" to LoginFormData(username = "", password = ""), "showPassword" to false, "loading" to false)
    }
    open var togglePassword = ::gen_togglePassword_fn
    open fun gen_togglePassword_fn() {
        this.showPassword = !this.showPassword
    }
    open var validateForm = ::gen_validateForm_fn
    open fun gen_validateForm_fn(): Boolean {
        if (this.form.username.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请输入用户名", icon = "none"))
            return false
        }
        if (this.form.password.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请输入密码", icon = "none"))
            return false
        }
        return true
    }
    open var getUserInfo = ::gen_getUserInfo_fn
    open fun gen_getUserInfo_fn(token: String) {
        val apiUrl = getApiUrl("/user/info?username=" + this.form.username)
        console.log("获取用户信息API地址:", apiUrl)
        uni_request<Any>(RequestOptions(url = apiUrl, method = "GET", header = object : UTSJSONObject() {
            var `Content-Type` = "application/json"
            var Authorization = "Bearer " + token
        }, success = fun(res){
            console.log("获取用户信息响应:", res)
            val data = res.data as UTSJSONObject
            console.log("响应数据:", data)
            if (res.statusCode == 200 && (data.getNumber("code") ?: 0) == 200) {
                val userInfo = data.getJSON("data")
                console.log("从后端获取的用户信息:", userInfo)
                if (userInfo != null) {
                    console.log("保存用户信息到本地存储:", JSON.stringify(userInfo))
                    uni_setStorageSync("userInfo", JSON.stringify(userInfo))
                    this.updateUserAvatar(userInfo)
                }
            } else {
                console.log("获取用户信息失败，使用基本信息")
                uni_setStorageSync("userInfo", JSON.stringify(_uO("id" to ("temp_" + Date.now()), "username" to this.form.username, "realName" to this.form.username)))
            }
        }
        , fail = fun(error){
            console.log("获取用户信息失败:", error)
            uni_setStorageSync("userInfo", JSON.stringify(_uO("id" to ("temp_" + Date.now()), "username" to this.form.username, "realName" to this.form.username)))
        }
        ))
    }
    open var handleLogin = ::gen_handleLogin_fn
    open fun gen_handleLogin_fn() {
        if (!this.validateForm()) {
            return
        }
        this.loading = true
        val loginUrl = getApiUrl("/user/login")
        console.log("登录API地址:", loginUrl)
        uni_request<Any>(RequestOptions(url = loginUrl, method = "POST", header = object : UTSJSONObject() {
            var `Content-Type` = "application/json"
        }, data = JSON.stringify(this.form), success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode == 200 && (data.getNumber("code") ?: 0) == 200) {
                val token = data.getString("data") ?: ""
                uni_setStorageSync("token", token)
                this.getUserInfo(token)
                uni_showToast(ShowToastOptions(title = "登录成功", icon = "success"))
                setTimeout(fun(){
                    uni_reLaunch(ReLaunchOptions(url = "/pages/home/home"))
                }, 1500) as Number
            } else {
                uni_showToast(ShowToastOptions(title = data.getString("message") ?: "登录失败", icon = "none"))
            }
            this.loading = false
        }
        , fail = fun(error){
            console.log("登录请求失败:", error)
            uni_showToast(ShowToastOptions(title = "网络请求失败", icon = "none"))
            this.loading = false
        }
        ))
    }
    open var updateUserAvatar = ::gen_updateUserAvatar_fn
    open fun gen_updateUserAvatar_fn(userInfo: UTSJSONObject) {
        val imageUrl = userInfo.getString("imageUrl")
        if (imageUrl != null && imageUrl != "") {
            console.log("用户已有头像，设置头像:", imageUrl)
            val httpImageUrl = this.convertToHttpUrl(imageUrl)
            userInfo.set("avatar", httpImageUrl)
            uni_setStorageSync("userInfo", JSON.stringify(userInfo))
            console.log("头像已更新到本地存储:", httpImageUrl)
        } else {
            console.log("用户暂无头像")
        }
    }
    open var convertToHttpUrl = ::gen_convertToHttpUrl_fn
    open fun gen_convertToHttpUrl_fn(relativePath: String): String {
        console.log("相对路径:", relativePath)
        if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) {
            return relativePath
        }
        val httpUrl = "" + apiConfig.baseUrl + "/api" + relativePath
        console.log("转换后的HTTP URL:", httpUrl)
        return httpUrl
    }
    open var onLoad = ::gen_onLoad_fn
    open fun gen_onLoad_fn() {
        val token = uni_getStorageSync("token")
        if (token != null && token != "") {
            uni_reLaunch(ReLaunchOptions(url = "/pages/home/home"))
        }
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ), _uA(
                GenApp.styles
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("login-container" to _pS(_uM("minHeight" to 800, "backgroundImage" to "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", "backgroundColor" to "rgba(0,0,0,0)", "flexDirection" to "column", "justifyContent" to "center", "alignItems" to "center", "paddingTop" to 40, "paddingRight" to 30, "paddingBottom" to 40, "paddingLeft" to 30)), "login-header" to _pS(_uM("textAlign" to "center", "marginBottom" to 60, "alignItems" to "center", "justifyContent" to "center")), "logo" to _pS(_uM("width" to 120, "height" to 120, "borderTopLeftRadius" to 20, "borderTopRightRadius" to 20, "borderBottomRightRadius" to 20, "borderBottomLeftRadius" to 20, "marginBottom" to 30, "alignSelf" to "center")), "system-title" to _pS(_uM("fontSize" to 28, "fontWeight" to "bold", "color" to "rgb(31,29,29)", "marginBottom" to 12, "textAlign" to "center", "letterSpacing" to 2)), "login-form" to _pS(_uM("width" to "100%", "maxWidth" to 400)), "form-item" to _pS(_uM("marginBottom" to 20)), "input-wrapper" to _pS(_uM("position" to "relative", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.9)", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "flexDirection" to "row", "alignItems" to "center", "paddingTop" to 0, "paddingRight" to 20, "paddingBottom" to 0, "paddingLeft" to 20, "height" to 65, "width" to "100%", "boxSizing" to "border-box")), "icon" to _pS(_uM("fontSize" to 18, "marginRight" to 12, "color" to "#666666", "flexShrink" to 0)), "input" to _pS(_uM("flex" to 1, "height" to "100%", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0)", "fontSize" to 16, "color" to "#333333", "paddingLeft" to 0)), "eye-icon" to _pS(_uM("fontSize" to 18, "color" to "#666666")), "login-btn" to _pS(_uM("width" to "100%", "height" to 50, "backgroundImage" to "none", "backgroundColor" to "#007AFF", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "fontSize" to 16, "fontWeight" to "bold", "marginTop" to 30, "marginBottom" to 20, "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#cccccc")), "tips" to _pS(_uM("textAlign" to "center")), "tip-text" to _pS(_uM("fontSize" to 14, "color" to "rgba(255,255,255,0.7)")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
