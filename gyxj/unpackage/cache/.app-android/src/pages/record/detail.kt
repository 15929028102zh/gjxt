@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "SENSELESS_COMPARISON", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uni.UNIB5A0920
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
import kotlin.properties.Delegates
import io.dcloud.uniapp.extapi.chooseImage as uni_chooseImage
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfoSync as uni_getSystemInfoSync
import io.dcloud.uniapp.extapi.hideLoading as uni_hideLoading
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.uploadFile as uni_uploadFile
open class GenPagesRecordDetail : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: UTSJSONObject) {
            this.recordId = options.getString("id") ?: ""
            this.isDelayed = options.getString("delayed") === "true"
            this.checkLogin()
            this.loadCurrentUserName()
            this.loadRecordDetail()
        }
        , __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "style" to _nS(_uM("height" to (_ctx.containerHeight + "px"))), "enable-back-to-top" to "true"), _uA(
            if (isTrue(_ctx.loading)) {
                _cE("view", _uM("key" to 0, "class" to "loading-state"), _uA(
                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                ))
            } else {
                if (isTrue(_ctx.recordDetail)) {
                    _cE("view", _uM("key" to 1, "class" to "detail-content"), _uA(
                        _cE("view", _uM("class" to "status-header"), _uA(
                            _cE("text", _uM("class" to "page-title"), _tD(if (_ctx.isDelayed) {
                                "延迟整改录入"
                            } else {
                                "问题详情"
                            }), 1),
                            _cE("view", _uM("class" to _nC(_uA(
                                "record-status",
                                _ctx.getStatusClass(_ctx.recordDetail?.status ?: 0)
                            ))), _uA(
                                _cE("text", null, _tD(if (_ctx.isDelayed) {
                                    "延迟整改"
                                } else {
                                    _ctx.getStatusText(_ctx.recordDetail?.status ?: 0)
                                }), 1)
                            ), 2)
                        )),
                        if (_ctx.imageList.length > 0) {
                            _cE("view", _uM("key" to 0, "class" to "form-item"), _uA(
                                _cE("text", _uM("class" to "form-label"), "📷 问题照片 (" + _tD(_ctx.imageList.length) + "张)", 1),
                                _cE("view", _uM("class" to "image-upload"), _uA(
                                    _cE("view", _uM("class" to "image-list"), _uA(
                                        _cE(Fragment, null, RenderHelpers.renderList(_ctx.imageList, fun(image, index, __index, _cached): Any {
                                            return _cE("view", _uM("class" to "image-item", "key" to index), _uA(
                                                _cE("image", _uM("src" to image, "mode" to "aspectFill", "class" to "uploaded-image", "onClick" to fun(){
                                                    _ctx.previewImage(index)
                                                }, "onError" to _ctx.onImageError, "onLoad" to _ctx.onImageLoad), null, 40, _uA(
                                                    "src",
                                                    "onClick",
                                                    "onError",
                                                    "onLoad"
                                                ))
                                            ))
                                        }), 128)
                                    ))
                                ))
                            ))
                        } else {
                            _cC("v-if", true)
                        },
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "问题描述"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.problemDescription ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "责任单位"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.responsibleUnit ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "责任人"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.responsiblePerson ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "性质界定"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.natureName ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "考核情况"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.assessmentSituation ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "考核金额"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.assessmentAmount ?: "") + "元", 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "安全记分"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.safetyScore ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "专业"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.professionName ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "发生部位"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.location ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "检查人"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.inspector ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "检查组组长"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.groupLeader ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "检查组副组长"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.deputyLeader ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "所属部门"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.departmentName ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "整改期限"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(_ctx.formatDate(_ctx.recordDetail?.rectificationDeadline ?: "")), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "整改人"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("text", _uM("class" to "form-display-text"), _tD(if ((_ctx.recordDetail?.rectifierName ?: "") !== "") {
                                    (_ctx.recordDetail?.rectifierName ?: "")
                                } else {
                                    if (_ctx.currentUserName !== "") {
                                        _ctx.currentUserName
                                    } else {
                                        "当前登录用户"
                                    }
                                }), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "整改效果描述"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("textarea", _uM("modelValue" to _ctx.rectificationEffect, "onInput" to fun(`$event`: UniInputEvent){
                                    _ctx.rectificationEffect = `$event`.detail.value
                                }, "class" to "form-textarea", "placeholder" to "请描述整改效果...", "maxlength" to "500", "show-confirm-bar" to "false"), null, 40, _uA(
                                    "modelValue",
                                    "onInput"
                                ))
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "📋 整改状态"),
                            _cE("view", _uM("class" to "status-selector"), _uA(
                                _cE("view", _uM("class" to _nC(_uA(
                                    "status-option pending",
                                    _uM("active" to (_ctx.rectificationStatus === 0))
                                )), "onClick" to fun(){
                                    _ctx.setRectificationStatus(0)
                                }), _uA(
                                    _cE("text", null, "⏳ 未整改")
                                ), 10, _uA(
                                    "onClick"
                                )),
                                _cE("view", _uM("class" to _nC(_uA(
                                    "status-option completed",
                                    _uM("active" to (_ctx.rectificationStatus === 1))
                                )), "onClick" to fun(){
                                    _ctx.setRectificationStatus(1)
                                }), _uA(
                                    _cE("text", null, "✅ 已整改")
                                ), 10, _uA(
                                    "onClick"
                                ))
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "📷 整改图片 (" + _tD(_ctx.rectificationImages.length) + "/3)", 1),
                            _cE("view", _uM("class" to "image-upload"), _uA(
                                _cE("view", _uM("class" to "image-list"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectificationImages, fun(image, index, __index, _cached): Any {
                                        return _cE("view", _uM("class" to "image-item", "key" to index), _uA(
                                            _cE("image", _uM("src" to image, "mode" to "aspectFill", "class" to "uploaded-image", "onClick" to fun(){
                                                _ctx.previewRectificationImage(index)
                                            }, "onError" to _ctx.onImageError, "onLoad" to _ctx.onImageLoad), null, 40, _uA(
                                                "src",
                                                "onClick",
                                                "onError",
                                                "onLoad"
                                            )),
                                            _cE("view", _uM("class" to "image-delete", "onClick" to fun(){
                                                _ctx.removeRectificationImage(index)
                                            }), _uA(
                                                _cE("text", null, "×")
                                            ), 8, _uA(
                                                "onClick"
                                            ))
                                        ))
                                    }), 128),
                                    if (_ctx.rectificationImages.length < 3) {
                                        _cE("view", _uM("key" to 0, "class" to "image-add", "onClick" to _ctx.chooseRectificationImage), _uA(
                                            _cE("text", _uM("class" to "add-icon"), "+"),
                                            _cE("text", _uM("class" to "add-text"), "添加图片")
                                        ), 8, _uA(
                                            "onClick"
                                        ))
                                    } else {
                                        _cC("v-if", true)
                                    }
                                ))
                            ))
                        )),
                        _cE("view", _uM("class" to "save-button-wrapper"), _uA(
                            _cE("button", _uM("class" to "save-btn", "onClick" to _ctx.saveRectificationInfo, "disabled" to _ctx.saving), _tD(if (_ctx.saving) {
                                if (_ctx.isDelayed) {
                                    "确认中..."
                                } else {
                                    "保存中..."
                                }
                            } else {
                                if (_ctx.isDelayed) {
                                    "确认整改"
                                } else {
                                    "保存整改信息"
                                }
                            }), 9, _uA(
                                "onClick",
                                "disabled"
                            ))
                        ))
                    ))
                } else {
                    _cE("view", _uM("key" to 2, "class" to "error-state"), _uA(
                        _cE("text", _uM("class" to "error-text"), "记录不存在或已被删除")
                    ))
                }
            }
        ), 4)
    }
    open var recordId: String by `$data`
    open var recordDetail: RecordDetail__1? by `$data`
    open var imageList: UTSArray<String> by `$data`
    open var loading: Boolean by `$data`
    open var canOperate: Boolean by `$data`
    open var rectificationEffect: String by `$data`
    open var rectificationImages: UTSArray<String> by `$data`
    open var rectificationStatus: Number by `$data`
    open var saving: Boolean by `$data`
    open var containerHeight: Number by `$data`
    open var isDelayed: Boolean by `$data`
    open var currentUserName: String by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        var containerHeight: Number = 0
        try {
            val sysInfo = uni_getSystemInfoSync()
            containerHeight = sysInfo.windowHeight ?: 0
        }
         catch (e: Throwable) {
            console.error("获取屏幕信息失败:", e, " at pages/record/detail.uvue:270")
        }
        return _uM("recordId" to "", "recordDetail" to null as RecordDetail__1?, "imageList" to _uA<String>(), "loading" to true, "canOperate" to false, "rectificationEffect" to "", "rectificationImages" to _uA<String>(), "rectificationStatus" to 0, "saving" to false, "containerHeight" to containerHeight, "isDelayed" to false, "currentUserName" to "")
    }
    open var checkLogin = ::gen_checkLogin_fn
    open fun gen_checkLogin_fn() {
        val token = uni_getStorageSync("token")
        if (token == null || token == "") {
            uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
        }
    }
    open var loadCurrentUserName = ::gen_loadCurrentUserName_fn
    open fun gen_loadCurrentUserName_fn() {
        val userInfoStr = uni_getStorageSync("userInfo") as String
        if (userInfoStr == null || userInfoStr == "") {
            return
        }
        try {
            val userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/detail.uvue:310") as UTSJSONObject
            this.currentUserName = userInfo.getString("realName") ?: userInfo.getString("username") ?: ""
        }
         catch (e: Throwable) {
            console.error("解析用户信息失败:", e, " at pages/record/detail.uvue:313")
        }
    }
    open var loadRecordDetail = ::gen_loadRecordDetail_fn
    open fun gen_loadRecordDetail_fn() {
        val token = uni_getStorageSync("token")
        uni_showLoading(ShowLoadingOptions(title = "加载中..."))
        uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record/" + this.recordId), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(response){
            uni_hideLoading(null)
            val data = response.data as UTSJSONObject
            if (response.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                val recordData = data.getJSON("data")
                if (recordData != null) {
                    this.recordDetail = RecordDetail__1(id = recordData.getString("id") ?: "", problemDescription = recordData.getString("problemDescription") ?: "", responsibleUnit = recordData.getString("responsibleUnit") ?: "", responsiblePerson = recordData.getString("responsiblePerson") ?: "", natureId = recordData.getString("natureId") ?: "", natureName = recordData.getString("natureName") ?: recordData.getString("natureDefinition") ?: "", assessmentSituation = recordData.getString("assessmentSituation") ?: "", assessmentAmount = recordData.getNumber("assessmentAmount") ?: 0, safetyScore = recordData.getNumber("safetyScore") ?: 0, professionId = recordData.getString("professionId") ?: "", professionName = recordData.getString("professionName") ?: "", location = recordData.getString("location") ?: "", inspector = recordData.getString("inspector") ?: "", groupLeader = recordData.getString("groupLeader") ?: "", deputyLeader = recordData.getString("deputyLeader") ?: "", departmentId = recordData.getString("departmentId") ?: "", departmentName = recordData.getString("departmentName") ?: "", rectificationDeadline = recordData.getString("rectificationDeadline") ?: "", status = recordData.getNumber("status") ?: 0, createTime = recordData.getString("createTime") ?: "", updateTime = recordData.getString("updateTime") ?: "", problemImageUrls = recordData.getString("problemImageUrls") ?: "", locationName = recordData.getString("locationName") ?: "", severity = recordData.getNumber("severity") ?: 0, taskName = recordData.getString("taskName") ?: "", reporterName = recordData.getString("reporterName") ?: "", suggestions = recordData.getString("suggestions") ?: "", reporterId = recordData.getString("reporterId") ?: "", rectificationEffect = recordData.getString("rectificationEffect") ?: "", rectificationImageUrls = recordData.getString("rectificationImageUrls") ?: "", rectificationTime = recordData.getString("rectificationTime") ?: "", rectifierName = recordData.getString("rectifierName") ?: "")
                    this.imageList = this.processImageUrls(this.recordDetail?.problemImageUrls ?: "")
                    if (this.recordDetail?.rectificationEffect != null && this.recordDetail!!.rectificationEffect !== "") {
                        this.rectificationEffect = this.recordDetail!!.rectificationEffect
                    }
                    this.rectificationImages = this.processImageUrls(this.recordDetail?.rectificationImageUrls ?: "")
                    this.rectificationStatus = this.recordDetail?.status ?: 0
                }
            } else {
                uni_showToast(ShowToastOptions(title = data.getString("message") ?: "加载失败", icon = "none"))
            }
            this.loading = false
        }
        , fail = fun(error){
            uni_hideLoading(null)
            console.error("加载记录详情失败:", error, " at pages/record/detail.uvue:384")
            uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
            this.loading = false
        }
        ))
    }
    open var convertToHttpUrl = ::gen_convertToHttpUrl_fn
    open fun gen_convertToHttpUrl_fn(relativePath: String): String {
        if (relativePath == null || relativePath == "" || relativePath.trim() === "") {
            return ""
        }
        if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) {
            return relativePath
        }
        return "" + apiConfig.baseUrl + "/api" + relativePath
    }
    open var processImageUrls = ::gen_processImageUrls_fn
    open fun gen_processImageUrls_fn(imageUrls: String): UTSArray<String> {
        if (imageUrls == null || imageUrls.trim() === "") {
            return _uA()
        }
        try {
            if (imageUrls.trim().startsWith("[") && imageUrls.trim().endsWith("]")) {
                val parsed = UTSAndroid.consoleDebugError(JSON.parse(imageUrls), " at pages/record/detail.uvue:407") as UTSArray<String>
                return parsed.map(fun(url): String {
                    return this.convertToHttpUrl(url)
                }
                ).filter(fun(url): Boolean {
                    return url !== ""
                }
                )
            }
        }
         catch (e: Throwable) {
            console.log("JSON解析失败,尝试按逗号分隔处理:", e, " at pages/record/detail.uvue:411")
        }
        return imageUrls.split(",").map(fun(url): String {
            return url.trim()
        }
        ).filter(fun(url): Boolean {
            return url !== ""
        }
        ).map(fun(url): String {
            return this.convertToHttpUrl(url)
        }
        )
    }
    open var previewImage = ::gen_previewImage_fn
    open fun gen_previewImage_fn(index: Number) {
        uni_previewImage(PreviewImageOptions(current = index, urls = this.imageList))
    }
    open var previewRectificationImage = ::gen_previewRectificationImage_fn
    open fun gen_previewRectificationImage_fn(index: Number) {
        uni_previewImage(PreviewImageOptions(current = index, urls = this.rectificationImages))
    }
    open var formatDate = ::gen_formatDate_fn
    open fun gen_formatDate_fn(dateTimeStr: String): String {
        if (dateTimeStr == null || dateTimeStr == "") {
            return ""
        }
        if (dateTimeStr.includes(" ")) {
            return dateTimeStr.split(" ")[0]
        }
        return dateTimeStr
    }
    open var onImageLoad = ::gen_onImageLoad_fn
    open fun gen_onImageLoad_fn(e: Any) {}
    open var onImageError = ::gen_onImageError_fn
    open fun gen_onImageError_fn(e: Any) {
        uni_showToast(ShowToastOptions(title = "图片加载失败", icon = "none"))
    }
    open var chooseRectificationImage = ::gen_chooseRectificationImage_fn
    open fun gen_chooseRectificationImage_fn() {
        if (this.rectificationImages.length >= 3) {
            uni_showToast(ShowToastOptions(title = "最多只能上传3张图片", icon = "none"))
            return
        }
        uni_chooseImage(ChooseImageOptions(count = 3 - this.rectificationImages.length, sourceType = _uA(
            "camera",
            "album"
        ), success = fun(res){
            val tempFilePaths = res.tempFilePaths
            run {
                var i: Number = 0
                while(i < tempFilePaths.length){
                    this.rectificationImages.push(tempFilePaths[i])
                    i++
                }
            }
        }
        ))
    }
    open var removeRectificationImage = ::gen_removeRectificationImage_fn
    open fun gen_removeRectificationImage_fn(index: Number) {
        this.rectificationImages.splice(index, 1)
    }
    open var setRectificationStatus = ::gen_setRectificationStatus_fn
    open fun gen_setRectificationStatus_fn(value: Number) {
        this.rectificationStatus = value
    }
    open var saveRectificationInfo = ::gen_saveRectificationInfo_fn
    open fun gen_saveRectificationInfo_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend w@{
                if (this.saving) {
                    return@w
                }
                if (this.rectificationEffect.trim().length === 0) {
                    uni_showToast(ShowToastOptions(title = "请填写整改效果", icon = "none"))
                    return@w
                }
                this.saving = true
                try {
                    var rectificationImageUrls: UTSArray<String> = _uA()
                    if (this.rectificationImages.length > 0) {
                        uni_showLoading(ShowLoadingOptions(title = "上传整改图片中..."))
                        rectificationImageUrls = await(this.uploadRectificationImages())
                        uni_hideLoading(null)
                    }
                    val userInfoStr = uni_getStorageSync("userInfo") as String
                    var currentUserId = ""
                    var rectifierName = ""
                    if (userInfoStr != null && userInfoStr !== "") {
                        try {
                            val userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/detail.uvue:495") as UTSJSONObject
                            currentUserId = userInfo.getString("id") ?: ""
                            rectifierName = userInfo.getString("realName") ?: userInfo.getString("username") ?: ""
                        }
                         catch (e: Throwable) {
                            console.error("解析用户信息失败:", e, " at pages/record/detail.uvue:499")
                        }
                    }
                    val payload: UTSJSONObject = _uO("__\$originalPosition" to UTSSourceMapPosition("payload", "pages/record/detail.uvue", 503, 12), "rectificationDescription" to this.rectificationEffect, "rectificationEffect" to this.rectificationEffect, "rectificationImages" to rectificationImageUrls.join(","), "rectificationStatus" to this.rectificationStatus, "status" to if (this.rectificationStatus === 1) {
                        1
                    } else {
                        0
                    }
                    , "rectifierId" to currentUserId, "rectifierName" to rectifierName, "rectificationTime" to Date().toISOString().replace("T", " ").substring(0, 19))
                    uni_showLoading(ShowLoadingOptions(title = "保存中..."))
                    val token = uni_getStorageSync("token")
                    uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record/" + (this.recordDetail?.id ?: "") + "/rectify"), method = "PUT", header = _uO("Authorization" to ("Bearer " + token), "Content-Type" to "application/json"), data = payload, success = fun(response){
                        uni_hideLoading(null)
                        val data = response.data as UTSJSONObject
                        if (response.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                            uni_showToast(ShowToastOptions(title = "保存成功", icon = "success"))
                            setTimeout(fun(){
                                uni_navigateBack(null)
                            }, 1500)
                        } else {
                            uni_showToast(ShowToastOptions(title = data.getString("message") ?: "保存失败", icon = "none"))
                        }
                        this.saving = false
                    }
                    , fail = fun(error){
                        uni_hideLoading(null)
                        console.error("保存整改信息失败:", error, " at pages/record/detail.uvue:542")
                        uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
                        this.saving = false
                    }
                    ))
                }
                 catch (error: Throwable) {
                    uni_hideLoading(null)
                    console.error("上传图片失败:", error, " at pages/record/detail.uvue:549")
                    uni_showToast(ShowToastOptions(title = "图片上传失败,请重试", icon = "none"))
                    this.saving = false
                }
        })
    }
    open var uploadRectificationImages = ::gen_uploadRectificationImages_fn
    open fun gen_uploadRectificationImages_fn(): UTSPromise<UTSArray<String>> {
        return wrapUTSPromise(suspend w@{
                if (this.rectificationImages.length === 0) {
                    return@w _uA()
                }
                val token = uni_getStorageSync("token")
                val uploadPromises = this.rectificationImages.map(fun(imagePath): UTSPromise<String> {
                    return UTSPromise<String>(fun(resolve, reject){
                        uni_uploadFile(UploadFileOptions(url = getApiUrl("/upload/image"), filePath = imagePath, name = "file", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
                            try {
                                val data = UTSAndroid.consoleDebugError(JSON.parse(res.data), " at pages/record/detail.uvue:571") as UTSJSONObject
                                if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                                    val dataObj = data.getJSON("data")
                                    val imageUrl = dataObj?.getString("filePath") ?: ""
                                    if (imageUrl != null && imageUrl !== "") {
                                        resolve(imageUrl)
                                    } else {
                                        reject(UTSError("上传成功但未获取到图片URL"))
                                    }
                                } else {
                                    reject(UTSError(data.getString("message") ?: "上传失败"))
                                }
                            }
                             catch (e: Throwable) {
                                reject(UTSError("解析响应失败"))
                            }
                        }
                        , fail = fun(error){
                            reject(UTSError("上传失败: " + (error.errMsg ?: "网络错误")))
                        }
                        ))
                    }
                    )
                }
                )
                return@w await(UTSPromise.all(uploadPromises))
        })
    }
    open var getStatusText = ::gen_getStatusText_fn
    open fun gen_getStatusText_fn(status: Number): String {
        when (status) {
            0 -> 
                return "待整改"
            1 -> 
                return "已整改"
            2 -> 
                return "已复查"
            else -> 
                return "未知"
        }
    }
    open var getStatusClass = ::gen_getStatusClass_fn
    open fun gen_getStatusClass_fn(status: Number): String {
        when (status) {
            0 -> 
                return "status-pending"
            1 -> 
                return "status-completed"
            2 -> 
                return "status-reviewed"
            else -> 
                return "status-unknown"
        }
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("container" to _pS(_uM("backgroundColor" to "#f1f5f9")), "loading-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 60, "paddingRight" to 0, "paddingBottom" to 60, "paddingLeft" to 0)), "loading-text" to _pS(_uM("fontSize" to 16, "color" to "#475569")), "detail-content" to _pS(_uM("paddingTop" to "24rpx", "paddingRight" to "32rpx", "paddingBottom" to "130rpx", "paddingLeft" to "32rpx")), "status-header" to _pS(_uM("backgroundImage" to "linear-gradient(135deg, #1e40af 0%, #2563eb 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 15, "paddingRight" to 15, "paddingBottom" to 15, "paddingLeft" to 15, "marginBottom" to 15, "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "display" to "flex", "justifyContent" to "space-between", "alignItems" to "center")), "page-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#ffffff")), "record-status" to _pS(_uM("paddingTop" to 6, "paddingRight" to 12, "paddingBottom" to 6, "paddingLeft" to 12, "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "fontSize" to 14, "fontWeight" to "bold")), "status-pending" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#fef3c7", "color" to "#b45309")), "status-processing" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#dbeafe", "color" to "#2563eb")), "status-completed" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#dcfce7", "color" to "#16a34a")), "status-accepted" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#1e40af", "color" to "#ffffff")), "status-unknown" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#f1f5f9", "color" to "#475569")), "severity-low" to _pS(_uM("color" to "#16a34a", "fontWeight" to "bold")), "severity-medium" to _pS(_uM("color" to "#f59e0b", "fontWeight" to "bold")), "severity-high" to _pS(_uM("color" to "#dc2626", "fontWeight" to "bold")), "severity-urgent" to _pS(_uM("color" to "#dc2626", "fontWeight" to "bold")), "error-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 60, "paddingRight" to 0, "paddingBottom" to 60, "paddingLeft" to 0)), "error-text" to _pS(_uM("fontSize" to 16, "color" to "#475569")), "form-item" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 15, "paddingRight" to 15, "paddingBottom" to 15, "paddingLeft" to 15, "marginBottom" to 15, "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)")), "form-label" to _pS(_uM("fontSize" to 16, "color" to "#0f172a", "marginBottom" to 10, "fontWeight" to 500)), "form-input-wrapper" to _pS(_uM("position" to "relative")), "form-display-text" to _pS(_uM("fontSize" to 16, "color" to "#0f172a", "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "backgroundImage" to "none", "backgroundColor" to "#eff6ff", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "minHeight" to 20, "overflowWrap" to "break-word")), "form-textarea" to _pS(_uM("width" to "100%", "minHeight" to 100, "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "backgroundImage" to "none", "backgroundColor" to "#eff6ff", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "fontSize" to 16, "color" to "#0f172a", "boxSizing" to "border-box", "borderTopColor:focus" to "#2563eb", "borderRightColor:focus" to "#2563eb", "borderBottomColor:focus" to "#2563eb", "borderLeftColor:focus" to "#2563eb")), "image-upload" to _pS(_uM("marginTop" to 10)), "image-list" to _pS(_uM("display" to "flex", "flexWrap" to "wrap")), "image-item" to _uM(".image-list " to _uM("marginTop" to 5, "marginRight" to 5, "marginBottom" to 5, "marginLeft" to 5), "" to _uM("position" to "relative", "width" to 80, "height" to 80)), "uploaded-image" to _pS(_uM("width" to "100%", "height" to "100%", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe")), "image-delete" to _pS(_uM("position" to "absolute", "top" to -5, "right" to -5, "width" to 20, "height" to 20, "backgroundImage" to "none", "backgroundColor" to "#dc2626", "color" to "#FFFFFF", "borderTopLeftRadius" to 10, "borderTopRightRadius" to 10, "borderBottomRightRadius" to 10, "borderBottomLeftRadius" to 10, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "fontSize" to 12)), "image-add" to _pS(_uM("width" to 80, "height" to 80, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#38bdf8", "borderRightColor" to "#38bdf8", "borderBottomColor" to "#38bdf8", "borderLeftColor" to "#38bdf8", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "display" to "flex", "flexDirection" to "column", "justifyContent" to "center", "alignItems" to "center", "backgroundImage" to "none", "backgroundColor" to "#eff6ff")), "add-icon" to _pS(_uM("fontSize" to 24, "color" to "#2563eb", "marginBottom" to 5)), "add-text" to _pS(_uM("fontSize" to 12, "color" to "#475569")), "status-selector" to _pS(_uM("display" to "flex", "flexDirection" to "row", "marginTop" to 15, "paddingTop" to 0, "paddingRight" to 5, "paddingBottom" to 0, "paddingLeft" to 5, "flexWrap" to "nowrap", "alignItems" to "center", "justifyContent" to "space-between")), "status-option" to _uM(".status-selector " to _uM("marginTop" to 0, "marginRight" to 6, "marginBottom" to 0, "marginLeft" to 6), "" to _uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "height" to 60, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "linear-gradient(135deg, #ffffff 0%, #eff6ff 100%)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "0 2px 8px rgba(30, 64, 175, 0.08)", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "cubic-bezier(0.4,0,0.2,1)", "position" to "relative", "overflow" to "hidden", "marginTop" to 0, "marginRight" to 6, "marginBottom" to 0, "marginLeft" to 6, "fontSize" to 14, "fontWeight" to 600, "color" to "#0f172a", "textAlign" to "center"), ".pending" to _uM("borderTopColor" to "#f59e0b", "borderRightColor" to "#f59e0b", "borderBottomColor" to "#f59e0b", "borderLeftColor" to "#f59e0b", "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#b45309"), ".pending.active" to _uM("borderTopColor" to "#f59e0b", "borderRightColor" to "#f59e0b", "borderBottomColor" to "#f59e0b", "borderLeftColor" to "#f59e0b", "backgroundImage" to "linear-gradient(135deg, #f59e0b 0%, #fbbf24 100%)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "0 4px 15px rgba(245, 158, 11, 0.32)", "transform" to "translateY(-2px)", "color" to "#FFFFFF"), ".completed" to _uM("borderTopColor" to "#16a34a", "borderRightColor" to "#16a34a", "borderBottomColor" to "#16a34a", "borderLeftColor" to "#16a34a", "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#16a34a"), ".completed.active" to _uM("borderTopColor" to "#16a34a", "borderRightColor" to "#16a34a", "borderBottomColor" to "#16a34a", "borderLeftColor" to "#16a34a", "backgroundImage" to "linear-gradient(135deg, #16a34a 0%, #22c55e 100%)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "0 4px 15px rgba(22, 163, 74, 0.32)", "transform" to "translateY(-2px)", "color" to "#FFFFFF")), "save-button-wrapper" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "marginTop" to "20rpx")), "save-btn" to _pS(_uM("width" to "100%", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "backgroundImage" to "none", "backgroundColor" to "#1e40af", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "fontSize" to "32rpx", "fontWeight" to 600, "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#94a3b8", "color:disabled" to "#f1f5f9")), "@TRANSITION" to _uM("status-option" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "cubic-bezier(0.4,0,0.2,1)")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
