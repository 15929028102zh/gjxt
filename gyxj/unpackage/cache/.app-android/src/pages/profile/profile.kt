@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uni.UNI630A075
import io.dcloud.uniapp.*
import io.dcloud.uniapp.extapi.*
import io.dcloud.uniapp.framework.*
import io.dcloud.uniapp.runtime.*
import io.dcloud.uniapp.vue.*
import io.dcloud.uniapp.vue.shared.*
import io.dcloud.unicloud.*
import io.dcloud.uts.*
import io.dcloud.uts.Map
import io.dcloud.uts.Set
import io.dcloud.uts.UTSAndroid
import io.dcloud.uniapp.extapi.chooseImage as uni_chooseImage
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.removeStorageSync as uni_removeStorageSync
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.setStorageSync as uni_setStorageSync
import io.dcloud.uniapp.extapi.showModal as uni_showModal
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.uploadFile as uni_uploadFile
open class GenPagesProfileProfile : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(_: OnLoadOptions) {
            this.checkLogin()
            this.loadUserInfo()
            this.loadUserStats()
        }
        , __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("view", _uM("class" to "container"), _uA(
            _cE("view", _uM("class" to "user-section"), _uA(
                _cE("view", _uM("class" to "user-avatar", "onClick" to _ctx.showAvatarOptions), _uA(
                    _cE("image", _uM("src" to if ((_ctx.userInfo.getString("avatar") ?: "") != "") {
                        (_ctx.userInfo.getString("avatar") ?: "")
                    } else {
                        "/static/default-avatar.svg"
                    }
                    , "mode" to "aspectFit"), null, 8, _uA(
                        "src"
                    ))
                ), 8, _uA(
                    "onClick"
                )),
                _cE("view", _uM("class" to "user-info"), _uA(
                    _cE("text", _uM("class" to "user-name"), _tD(if ((_ctx.userInfo.getString("realName") ?: "") != "") {
                        (_ctx.userInfo.getString("realName") ?: "")
                    } else {
                        "未设置"
                    }
                    ), 1),
                    _cE("text", _uM("class" to "user-phone"), _tD(if ((_ctx.userInfo.getString("phone") ?: "") != "") {
                        (_ctx.userInfo.getString("phone") ?: "")
                    } else {
                        "未绑定手机"
                    }
                    ), 1)
                ))
            )),
            _cE("view", _uM("class" to "logout-section"), _uA(
                _cE("view", _uM("class" to "logout-btn", "onClick" to _ctx.logout), _uA(
                    _cE("text", null, "退出登录")
                ), 8, _uA(
                    "onClick"
                ))
            )),
            if (isTrue(_ctx.showAvatarModal)) {
                _cE("view", _uM("key" to 0, "class" to "modal", "onClick" to _ctx.closeAvatarModal), _uA(
                    _cE("view", _uM("class" to "modal-content avatar-options", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "option-item", "onClick" to _ctx.editProfile), _uA(
                            _cE("text", _uM("class" to "option-text"), "编辑资料")
                        ), 8, _uA(
                            "onClick"
                        )),
                        _cE("view", _uM("class" to "option-item", "onClick" to _ctx.changeAvatar), _uA(
                            _cE("text", _uM("class" to "option-text"), "修改头像")
                        ), 8, _uA(
                            "onClick"
                        ))
                    ), 8, _uA(
                        "onClick"
                    ))
                ), 8, _uA(
                    "onClick"
                ))
            } else {
                _cC("v-if", true)
            }
            ,
            if (isTrue(_ctx.showEditModal)) {
                _cE("view", _uM("key" to 1, "class" to "modal", "onClick" to _ctx.closeEditModal), _uA(
                    _cE("view", _uM("class" to "modal-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "真实姓名"),
                            _cE("input", _uM("class" to "form-input", "modelValue" to _ctx.editForm.realName, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.editForm.realName = `$event`.detail.value
                            }, "placeholder" to "请输入真实姓名"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "手机号码"),
                            _cE("input", _uM("class" to "form-input", "modelValue" to _ctx.editForm.phone, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.editForm.phone = `$event`.detail.value
                            }, "placeholder" to "请输入手机号码", "type" to "number"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("view", _uM("class" to "modal-footer"), _uA(
                            _cE("view", _uM("class" to "btn-cancel", "onClick" to _ctx.closeEditModal), _uA(
                                _cE("text", null, "取消")
                            ), 8, _uA(
                                "onClick"
                            )),
                            _cE("view", _uM("class" to "btn-confirm", "onClick" to _ctx.saveProfile), _uA(
                                _cE("text", null, "保存")
                            ), 8, _uA(
                                "onClick"
                            ))
                        ))
                    ), 8, _uA(
                        "onClick"
                    ))
                ), 8, _uA(
                    "onClick"
                ))
            } else {
                _cC("v-if", true)
            }
        ))
    }
    open var userInfo: UTSJSONObject by `$data`
    open var userStats: UTSJSONObject by `$data`
    open var showEditModal: Boolean by `$data`
    open var showAvatarModal: Boolean by `$data`
    open var editForm: EditForm by `$data`
    open var loading: Boolean by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("userInfo" to UTSJSONObject(), "userStats" to UTSJSONObject(), "showEditModal" to false, "showAvatarModal" to false, "editForm" to EditForm(realName = "", phone = ""), "loading" to false)
    }
    open var checkLogin = ::gen_checkLogin_fn
    open fun gen_checkLogin_fn() {
        val token = uni_getStorageSync("token")
        if (token == null || token == "") {
            uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
        }
    }
    open var loadUserInfo = ::gen_loadUserInfo_fn
    open fun gen_loadUserInfo_fn() {
        val userInfoStr = uni_getStorageSync("userInfo") as String
        console.log("从本地存储获取的用户信息字符串:", userInfoStr, " at pages/profile/profile.uvue:116")
        if (userInfoStr != null && userInfoStr != "") {
            val userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/profile/profile.uvue:119") as UTSJSONObject
            console.log("解析后的用户信息:", userInfo, " at pages/profile/profile.uvue:120")
            console.log("用户ID字段:", userInfo.getString("id"), " at pages/profile/profile.uvue:121")
            console.log("用户信息JSON字符串:", JSON.stringify(userInfo), " at pages/profile/profile.uvue:122")
            this.userInfo = userInfo
            this.editForm.realName = userInfo.getString("realName") ?: ""
            this.editForm.phone = userInfo.getString("phone") ?: ""
        } else {
            console.log("本地存储中没有用户信息", " at pages/profile/profile.uvue:128")
        }
    }
    open var loadUserStats = ::gen_loadUserStats_fn
    open fun gen_loadUserStats_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/user/stats"), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode == 200 && (data.getNumber("code") ?: 0) == 200) {
                this.userStats = data.getJSON("data") as UTSJSONObject
            }
        }
        , fail = fun(error){
            console.log("加载用户统计失败:", error, " at pages/profile/profile.uvue:147")
        }
        ))
    }
    open var getRoleText = ::gen_getRoleText_fn
    open fun gen_getRoleText_fn(role: Number): String {
        if (role == 1) {
            return "管理员"
        } else if (role == 2) {
            return "超级管理员"
        } else {
            return "普通用户"
        }
    }
    open var showAvatarOptions = ::gen_showAvatarOptions_fn
    open fun gen_showAvatarOptions_fn() {
        this.showAvatarModal = true
    }
    open var closeAvatarModal = ::gen_closeAvatarModal_fn
    open fun gen_closeAvatarModal_fn() {
        this.showAvatarModal = false
    }
    open var changeAvatar = ::gen_changeAvatar_fn
    open fun gen_changeAvatar_fn() {
        this.closeAvatarModal()
        uni_chooseImage(ChooseImageOptions(count = 1, sizeType = _uA(
            "compressed"
        ), sourceType = _uA(
            "album",
            "camera"
        ), success = fun(res){
            val tempFilePath = res.tempFilePaths[0]
            this.uploadAvatar(tempFilePath)
        }
        , fail = fun(error){
            console.log("选择图片失败:", error, " at pages/profile/profile.uvue:181")
        }
        ))
    }
    open var uploadAvatar = ::gen_uploadAvatar_fn
    open fun gen_uploadAvatar_fn(filePath: String) {
        val token = uni_getStorageSync("token")
        uni_uploadFile(UploadFileOptions(url = getApiUrl("/upload/image"), filePath = filePath, name = "file", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            console.log("上传图片响应:", res, " at pages/profile/profile.uvue:198")
            console.log("上传图片响应数据:", res.data, " at pages/profile/profile.uvue:199")
            val data = UTSAndroid.consoleDebugError(JSON.parse(res.data), " at pages/profile/profile.uvue:201") as UTSJSONObject
            console.log("解析后的数据:", data, " at pages/profile/profile.uvue:202")
            if (res.statusCode === 200 && data.getNumber("code") === 200) {
                val dataObj = data.getJSON("data")
                val imageUrl = dataObj?.getString("filePath") ?: ""
                console.log("获取到的图片URL:", imageUrl, " at pages/profile/profile.uvue:207")
                if (imageUrl == null || imageUrl == "") {
                    console.log("警告：图片URL为空", " at pages/profile/profile.uvue:210")
                    uni_showToast(ShowToastOptions(title = "上传成功但未获取到图片URL", icon = "none"))
                    return
                }
                this.updateUserAvatar(imageUrl)
            } else {
                console.log("上传失败，状态码:", res.statusCode, "错误码:", data.getNumber("code"), " at pages/profile/profile.uvue:221")
                uni_showToast(ShowToastOptions(title = data.getString("message") ?: "上传失败", icon = "none"))
            }
        }
        , fail = fun(error){
            console.log("上传头像失败:", error, " at pages/profile/profile.uvue:229")
            uni_showToast(ShowToastOptions(title = "上传失败", icon = "none"))
        }
        ))
    }
    open var updateUserAvatar = ::gen_updateUserAvatar_fn
    open fun gen_updateUserAvatar_fn(imageUrl: String) {
        val token = uni_getStorageSync("token")
        console.log("用户信息调试:", this.userInfo, " at pages/profile/profile.uvue:242")
        console.log("用户信息类型:", UTSAndroid.`typeof`(this.userInfo), " at pages/profile/profile.uvue:243")
        console.log("用户信息字符串:", JSON.stringify(this.userInfo), " at pages/profile/profile.uvue:244")
        val username = this.userInfo.getString("username") ?: ""
        console.log("获取到的用户名:", username, " at pages/profile/profile.uvue:247")
        if (username == null || username == "") {
            uni_showToast(ShowToastOptions(title = "用户信息异常", icon = "none"))
            return
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/user/" + username + "/update-avatar"), method = "PUT", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
            var `Content-Type` = "application/json"
        }, data = _uO("imageUrl" to imageUrl), success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode === 200 && data.getNumber("code") === 200) {
                val httpImageUrl = this.convertToHttpUrl(imageUrl)
                this.userInfo.set("avatar", httpImageUrl)
                uni_setStorageSync("userInfo", JSON.stringify(this.userInfo))
                uni_showToast(ShowToastOptions(title = "头像更新成功", icon = "success"))
            } else {
                uni_showToast(ShowToastOptions(title = data.getString("message") ?: "设置头像失败", icon = "none"))
            }
        }
        , fail = fun(error){
            console.log("设置头像失败:", error, " at pages/profile/profile.uvue:287")
            uni_showToast(ShowToastOptions(title = "设置头像失败", icon = "none"))
        }
        ))
    }
    open var editProfile = ::gen_editProfile_fn
    open fun gen_editProfile_fn() {
        this.closeAvatarModal()
        this.showEditModal = true
    }
    open var closeEditModal = ::gen_closeEditModal_fn
    open fun gen_closeEditModal_fn() {
        this.showEditModal = false
        val realName = this.userInfo.getString("realName") ?: ""
        val phone = this.userInfo.getString("phone") ?: ""
        this.editForm.realName = if (realName != "") {
            realName
        } else {
            ""
        }
        this.editForm.phone = if (phone != "") {
            phone
        } else {
            ""
        }
    }
    open var saveProfile = ::gen_saveProfile_fn
    open fun gen_saveProfile_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend w@{
                if (this.editForm.realName.trim() == "") {
                    uni_showToast(ShowToastOptions(title = "请输入真实姓名", icon = "none"))
                    return@w
                }
                if (this.editForm.phone != "" && !UTSRegExp("^1[3-9]\\d{9}\$", "").test(this.editForm.phone)) {
                    uni_showToast(ShowToastOptions(title = "请输入正确的手机号", icon = "none"))
                    return@w
                }
                if (this.loading) {
                    return@w
                }
                this.loading = true
                val token = uni_getStorageSync("token")
                uni_request<Any>(RequestOptions(url = getApiUrl("/user/profile"), method = "PUT", header = object : UTSJSONObject() {
                    var Authorization = "Bearer " + token
                    var `Content-Type` = "application/json"
                }, data = _uO("realName" to this.editForm.realName, "phone" to this.editForm.phone), success = fun(res){
                    val data = res.data as UTSJSONObject
                    if (res.statusCode === 200 && data.getNumber("code") === 200) {
                        this.userInfo.set("realName", this.editForm.realName)
                        this.userInfo.set("phone", this.editForm.phone)
                        uni_setStorageSync("userInfo", JSON.stringify(this.userInfo))
                        uni_showToast(ShowToastOptions(title = "保存成功", icon = "success"))
                        this.closeEditModal()
                    } else {
                        val message = data.getString("message") ?: "保存失败"
                        uni_showToast(ShowToastOptions(title = message, icon = "none"))
                    }
                    this.loading = false
                }
                , fail = fun(error){
                    console.error("保存用户信息失败:", error, " at pages/profile/profile.uvue:367")
                    uni_showToast(ShowToastOptions(title = "保存失败", icon = "none"))
                    this.loading = false
                }
                ))
        })
    }
    open var goToMyRecords = ::gen_goToMyRecords_fn
    open fun gen_goToMyRecords_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?userId=" + (this.userInfo.getString("id") ?: "")))
    }
    open var goToMyTasks = ::gen_goToMyTasks_fn
    open fun gen_goToMyTasks_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/task/task?assigneeId=" + (this.userInfo.getString("id") ?: "")))
    }
    open var showSettings = ::gen_showSettings_fn
    open fun gen_showSettings_fn() {
        uni_showToast(ShowToastOptions(title = "功能开发中", icon = "none"))
    }
    open var showHelp = ::gen_showHelp_fn
    open fun gen_showHelp_fn() {
        uni_showModal(ShowModalOptions(title = "帮助与反馈", content = "如有问题请联系管理员\n电话：400-123-4567\n邮箱：support@example.com", showCancel = false))
    }
    open var showAbout = ::gen_showAbout_fn
    open fun gen_showAbout_fn() {
        uni_showModal(ShowModalOptions(title = "关于我们", content = "工业巡检系统 v1.0.0\n\n专业的工业设备巡检管理平台，提供任务管理、记录跟踪、数据分析等功能。", showCancel = false))
    }
    open var convertToHttpUrl = ::gen_convertToHttpUrl_fn
    open fun gen_convertToHttpUrl_fn(relativePath: String): String {
        console.log("相对路径:", relativePath, " at pages/profile/profile.uvue:414")
        if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) {
            return relativePath
        }
        val httpUrl = "" + apiConfig.baseUrl + "/api" + relativePath
        console.log("转换后的HTTP URL:", httpUrl, " at pages/profile/profile.uvue:423")
        return httpUrl
    }
    open var logout = ::gen_logout_fn
    open fun gen_logout_fn() {
        uni_showModal(ShowModalOptions(title = "确认退出", content = "确定要退出登录吗？", success = fun(res){
            if (res.confirm) {
                uni_removeStorageSync("token")
                uni_removeStorageSync("userInfo")
                uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
            }
        }
        ))
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
                return _uM("container" to _pS(_uM("paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "backgroundColor" to "#f5f5f5")), "user-section" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 25, "paddingRight" to 20, "paddingBottom" to 25, "paddingLeft" to 20, "marginBottom" to 20, "display" to "flex", "alignItems" to "center", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "user-avatar" to _pS(_uM("width" to 70, "height" to 70, "borderTopLeftRadius" to 35, "borderTopRightRadius" to 35, "borderBottomRightRadius" to 35, "borderBottomLeftRadius" to 35, "overflow" to "hidden", "marginRight" to 15, "backgroundImage" to "none", "backgroundColor" to "#f0f0f0", "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e0e0e0", "borderRightColor" to "#e0e0e0", "borderBottomColor" to "#e0e0e0", "borderLeftColor" to "#e0e0e0", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "position" to "relative", "content::after" to "''", "position::after" to "absolute", "top::after" to 0, "left::after" to 0, "right::after" to 0, "bottom::after" to 0, "backgroundImage::after" to "linear-gradient(135deg, rgba(0, 122, 255, 0.1) 0%, rgba(90, 200, 250, 0.1) 100%)", "backgroundColor::after" to "rgba(0,0,0,0)", "opacity::after" to 0, "transitionProperty::after" to "opacity", "transitionDuration::after" to "0.3s", "transitionTimingFunction::after" to "ease", "transform:active" to "scale(0.95)", "boxShadow:active" to "0 4px 20px rgba(0, 122, 255, 0.3)", "opacity:active::after" to 1)), "user-info" to _pS(_uM("flex" to 1)), "user-name" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333", "marginBottom" to 5)), "user-role" to _pS(_uM("fontSize" to 14, "color" to "#007AFF", "marginBottom" to 3)), "user-phone" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "stats-section" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "marginBottom" to 20, "display" to "flex", "justifyContent" to "space-around", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "stats-item" to _pS(_uM("textAlign" to "center")), "stats-number" to _pS(_uM("fontSize" to 24, "fontWeight" to "bold", "color" to "#007AFF", "marginBottom" to 5)), "stats-label" to _pS(_uM("fontSize" to 12, "color" to "#666666")), "menu-group" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "marginBottom" to 15, "overflow" to "hidden", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "menu-item" to _pS(_uM("display" to "flex", "alignItems" to "center", "paddingTop" to 18, "paddingRight" to 20, "paddingBottom" to 18, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "menu-icon" to _pS(_uM("width" to 24, "height" to 24, "marginRight" to 15, "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "menu-title" to _pS(_uM("flex" to 1, "fontSize" to 16, "color" to "#333333")), "menu-arrow" to _pS(_uM("fontSize" to 16, "color" to "#cccccc")), "logout-section" to _pS(_uM("marginTop" to 30)), "logout-btn" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 18, "paddingRight" to 18, "paddingBottom" to 18, "paddingLeft" to 18, "textAlign" to "center", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "modal" to _pS(_uM("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0.5)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "zIndex" to 1000)), "modal-content" to _pS(_uM("backgroundImage" to "linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to 16, "borderTopRightRadius" to 16, "borderBottomRightRadius" to 16, "borderBottomLeftRadius" to 16, "width" to "80%", "maxWidth" to 400, "overflow" to "hidden", "paddingTop" to 25, "paddingRight" to 25, "paddingBottom" to 25, "paddingLeft" to 25, "boxShadow" to "0 12px 40px rgba(0, 0, 0, 0.15)")), "modal-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0")), "modal-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333")), "modal-close" to _pS(_uM("fontSize" to 24, "color" to "#999999", "width" to 30, "height" to 30, "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "form-item" to _pS(_uM("marginBottom" to 24, "position" to "relative")), "form-label" to _pS(_uM("fontSize" to 15, "color" to "#2c3e50", "marginBottom" to 10)), "form-input" to _pS(_uM("width" to "100%", "height" to 50, "paddingTop" to 0, "paddingRight" to 18, "paddingBottom" to 0, "paddingLeft" to 18, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "fontSize" to 16, "boxSizing" to "border-box", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "boxShadow" to "0 2px 8px rgba(0, 0, 0, 0.04)", "borderTopColor:focus" to "#007AFF", "borderRightColor:focus" to "#007AFF", "borderBottomColor:focus" to "#007AFF", "borderLeftColor:focus" to "#007AFF", "boxShadow:focus" to "0 0 0 3px rgba(0, 122, 255, 0.1)", "outline:focus" to "none", "transform:focus" to "translateY(-1px)", "color::placeholder" to "#adb5bd", "fontSize::placeholder" to 15)), "modal-footer" to _pS(_uM("flexDirection" to "row", "paddingTop" to 25, "paddingRight" to 0, "paddingBottom" to 0, "paddingLeft" to 0, "borderTopWidth" to "medium", "borderTopStyle" to "none", "borderTopColor" to "#000000", "gap" to "15px")), "btn-cancel" to _pS(_uM("flex" to 1, "height" to 50, "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.08)", "position" to "relative", "overflow" to "hidden", "backgroundImage" to "linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dee2e6", "borderRightColor" to "#dee2e6", "borderBottomColor" to "#dee2e6", "borderLeftColor" to "#dee2e6", "transform:active" to "translateY(1px) scale(0.98)", "boxShadow:active" to "0 2px 6px rgba(0, 0, 0, 0.12)")), "btn-confirm" to _pS(_uM("flex" to 1, "height" to 50, "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.08)", "position" to "relative", "overflow" to "hidden", "backgroundImage" to "linear-gradient(135deg, #007AFF 0%, #5AC8FA 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(0,0,0,0)", "borderRightColor" to "rgba(0,0,0,0)", "borderBottomColor" to "rgba(0,0,0,0)", "borderLeftColor" to "rgba(0,0,0,0)", "transform:active" to "translateY(1px) scale(0.98)", "boxShadow:active" to "0 2px 6px rgba(0, 122, 255, 0.3)")), "avatar-options" to _pS(_uM("width" to "75%", "maxWidth" to 320, "borderTopLeftRadius" to 16, "borderTopRightRadius" to 16, "borderBottomRightRadius" to 16, "borderBottomLeftRadius" to 16, "overflow" to "hidden", "boxShadow" to "0 8px 32px rgba(0, 0, 0, 0.12)", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "position" to "relative", "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20)), "option-item" to _pS(_uM("paddingTop" to 16, "paddingRight" to 24, "paddingBottom" to 16, "paddingLeft" to 24, "marginBottom" to 15, "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "backgroundImage" to "none", "backgroundColor" to "#f8f9fa", "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(0,0,0,0)", "borderRightColor" to "rgba(0,0,0,0)", "borderBottomColor" to "rgba(0,0,0,0)", "borderLeftColor" to "rgba(0,0,0,0)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "position" to "relative", "boxShadow" to "0 2px 8px rgba(0, 0, 0, 0.06)", "marginBottom:last-child" to 0, "transform:active" to "translateY(1px) scale(0.98)", "boxShadow:active" to "0 1px 4px rgba(0, 0, 0, 0.1)", "backgroundImage:first-child" to "linear-gradient(135deg, #e3f2fd 0%, #f0f8ff 100%)", "backgroundColor:first-child" to "rgba(0,0,0,0)", "borderTopColor:first-child" to "#007AFF", "borderRightColor:first-child" to "#007AFF", "borderBottomColor:first-child" to "#007AFF", "borderLeftColor:first-child" to "#007AFF", "backgroundImage:first-child:active" to "linear-gradient(135deg, #007AFF 0%, #5AC8FA 100%)", "backgroundColor:first-child:active" to "rgba(0,0,0,0)", "backgroundImage:last-child" to "linear-gradient(135deg, #fff3e0 0%, #ffeaa7 100%)", "backgroundColor:last-child" to "rgba(0,0,0,0)", "borderTopColor:last-child" to "#FF6B35", "borderRightColor:last-child" to "#FF6B35", "borderBottomColor:last-child" to "#FF6B35", "borderLeftColor:last-child" to "#FF6B35", "backgroundImage:last-child:active" to "linear-gradient(135deg, #FF6B35 0%, #ff8a50 100%)", "backgroundColor:last-child:active" to "rgba(0,0,0,0)")), "option-text" to _pS(_uM("fontSize" to 16, "color" to "#333333", "transitionProperty" to "color", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease")), "@TRANSITION" to _uM("form-input" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease"), "btn-cancel" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease"), "btn-confirm" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease"), "option-item" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease"), "option-text" to _uM("property" to "color", "duration" to "0.3s", "timingFunction" to "ease"), "user-avatar" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease", "property::after" to "opacity", "duration::after" to "0.3s", "timingFunction::after" to "ease")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
