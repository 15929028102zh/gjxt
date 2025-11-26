@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
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
import io.dcloud.uniapp.extapi.chooseImage as uni_chooseImage
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfoSync as uni_getSystemInfoSync
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showModal as uni_showModal
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.uploadFile as uni_uploadFile
open class GenPagesMonthlyConfirmRectification : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: UTSJSONObject) {
            val id = options["id"] as String?
            this.recordId = if (id != null) {
                id
            } else {
                ""
            }
            this.initPage()
        }
        , __ins)
        onReady(fun() {
            this.calculateContainerHeight()
        }
        , __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("view", _uM("class" to "container"), _uA(
            _cE("view", _uM("class" to "nav-bar"), _uA(
                _cE("view", _uM("class" to "nav-left", "onClick" to _ctx.goBack), _uA(
                    _cE("text", _uM("class" to "nav-icon"), "‹")
                ), 8, _uA(
                    "onClick"
                )),
                _cE("view", _uM("class" to "nav-title"), _uA(
                    _cE("text", null, "确认整改")
                )),
                _cE("view", _uM("class" to "nav-right"))
            )),
            _cE("scroll-view", _uM("class" to "content", "scroll-y" to "true", "style" to _nS(_uM("height" to (_ctx.containerHeight + "px"))), "onScrolltolower" to _ctx.onScrollToLower), _uA(
                _cE("view", _uM("class" to "section"), _uA(
                    _cE("view", _uM("class" to "section-title"), "基本信息"),
                    _cE("view", _uM("class" to "info-card"), _uA(
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "任务编号："),
                            _cE("text", _uM("class" to "value"), _tD(if (_ctx.recordDetail["taskNo"] != null) {
                                _ctx.recordDetail["taskNo"] as String
                            } else {
                                "暂无"
                            }
                            ), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "月检时间："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.formatDateTime(if (_ctx.recordDetail["inspectionTime"] != null) {
                                _ctx.recordDetail["inspectionTime"] as String
                            } else {
                                ""
                            }
                            )), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "检查部门："),
                            _cE("text", _uM("class" to "value"), _tD(if (_ctx.recordDetail["departmentName"] != null) {
                                _ctx.recordDetail["departmentName"] as String
                            } else {
                                "暂无"
                            }
                            ), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "检查人："),
                            _cE("text", _uM("class" to "value"), _tD(if (_ctx.recordDetail["inspector"] != null) {
                                _ctx.recordDetail["inspector"] as String
                            } else {
                                "暂无"
                            }
                            ), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "问题描述："),
                            _cE("text", _uM("class" to "value"), _tD(if (_ctx.recordDetail["problemDescription"] != null) {
                                _ctx.recordDetail["problemDescription"] as String
                            } else {
                                "暂无"
                            }
                            ), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "发现位置："),
                            _cE("text", _uM("class" to "value"), _tD(if (_ctx.recordDetail["location"] != null) {
                                _ctx.recordDetail["location"] as String
                            } else {
                                "暂无"
                            }
                            ), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "整改期限："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.formatDateTime(if (_ctx.recordDetail["rectificationDeadline"] != null) {
                                _ctx.recordDetail["rectificationDeadline"] as String
                            } else {
                                ""
                            }
                            )), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "状态："),
                            _cE("text", _uM("class" to _nC(_uA(
                                "status-tag",
                                _ctx.getStatusClass(_ctx.recordDetail["recordStatus"] as Number)
                            ))), _tD(_ctx.getStatusText(_ctx.recordDetail["recordStatus"] as Number)), 3)
                        ))
                    ))
                )),
                if (_ctx.problemImages.length > 0) {
                    _cE("view", _uM("key" to 0, "class" to "section"), _uA(
                        _cE("view", _uM("class" to "section-title"), "问题图片"),
                        _cE("view", _uM("class" to "image-grid"), _uA(
                            _cE(Fragment, null, RenderHelpers.renderList(_ctx.problemImages, fun(image, index, __index, _cached): Any {
                                return _cE("image", _uM("key" to index, "src" to image, "class" to "problem-image", "mode" to "aspectFill", "onClick" to fun(){
                                    _ctx.previewImage(image as String, _ctx.problemImages as UTSArray<String>)
                                }, "onError" to _ctx.onImageError, "onLoad" to _ctx.onImageLoad), null, 40, _uA(
                                    "src",
                                    "onClick",
                                    "onError",
                                    "onLoad"
                                ))
                            }), 128)
                        ))
                    ))
                } else {
                    _cC("v-if", true)
                }
                ,
                _cE("view", _uM("class" to "section"), _uA(
                    _cE("view", _uM("class" to "section-title"), "整改信息"),
                    _cE("view", _uM("class" to "form-card"), _uA(
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("view", _uM("class" to "form-label"), _uA(
                                _cE("text", _uM("class" to "required"), "*"),
                                _cE("text", null, "整改建议")
                            )),
                            _cE("textarea", _uM("modelValue" to _ctx.rectificationForm["rectificationSuggestion"], "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.rectificationForm["rectificationSuggestion"] = `$event`.detail.value
                            }
                            , "class" to "form-textarea", "placeholder" to "请输入整改建议（至少10个字符）", "maxlength" to "500", "show-count" to true, "auto-height" to ""), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("view", _uM("class" to "form-label"), "整改图片"),
                            _cE("view", _uM("class" to "upload-container"), _uA(
                                if (_ctx.rectificationImages.length > 0) {
                                    _cE("view", _uM("key" to 0, "class" to "uploaded-images"), _uA(
                                        _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectificationImages, fun(image, index, __index, _cached): Any {
                                            return _cE("view", _uM("key" to index, "class" to "uploaded-image-item"), _uA(
                                                _cE("image", _uM("src" to image.url, "class" to "uploaded-image", "mode" to "aspectFill", "onClick" to fun(){
                                                    _ctx.previewImage(image.url, _ctx.rectificationImages.map(fun(img): String {
                                                        return img.url
                                                    }))
                                                }), null, 8, _uA(
                                                    "src",
                                                    "onClick"
                                                )),
                                                _cE("view", _uM("class" to "delete-btn", "onClick" to fun(){
                                                    _ctx.removeImage(index)
                                                }), _uA(
                                                    _cE("text", _uM("class" to "delete-icon"), "×")
                                                ), 8, _uA(
                                                    "onClick"
                                                ))
                                            ))
                                        }), 128)
                                    ))
                                } else {
                                    _cC("v-if", true)
                                }
                                ,
                                if (_ctx.rectificationImages.length < 3) {
                                    _cE("view", _uM("key" to 1, "class" to "upload-btn", "onClick" to _ctx.chooseImage), _uA(
                                        _cE("text", _uM("class" to "upload-icon"), "+"),
                                        _cE("text", _uM("class" to "upload-text"), "添加图片")
                                    ), 8, _uA(
                                        "onClick"
                                    ))
                                } else {
                                    _cC("v-if", true)
                                }
                            )),
                            _cE("view", _uM("class" to "upload-tip"), " 支持jpg、png、gif格式，单个文件不超过2MB，最多上传3张图片 ")
                        ))
                    ))
                ))
            ), 44, _uA(
                "onScrolltolower"
            )),
            _cE("view", _uM("class" to "footer"), _uA(
                _cE("button", _uM("class" to "btn btn-cancel", "onClick" to _ctx.goBack), "取消", 8, _uA(
                    "onClick"
                )),
                _cE("button", _uM("class" to "btn btn-confirm", "disabled" to (!_ctx.canSubmit || _ctx.submitting), "onClick" to _ctx.handleConfirm), _tD(if (_ctx.submitting) {
                    "提交中..."
                } else {
                    "确认整改"
                }
                ), 9, _uA(
                    "disabled",
                    "onClick"
                ))
            )),
            if (isTrue(_ctx.loading)) {
                _cE("view", _uM("key" to 0, "class" to "loading-overlay"), _uA(
                    _cE("view", _uM("class" to "loading-content"), _uA(
                        _cE("text", null, "加载中...")
                    ))
                ))
            } else {
                _cC("v-if", true)
            }
        ))
    }
    open var recordId: String by `$data`
    open var loading: Boolean by `$data`
    open var submitting: Boolean by `$data`
    open var containerHeight: Number by `$data`
    open var recordDetail: UTSJSONObject by `$data`
    open var problemImages: UTSArray<String> by `$data`
    open var rectificationForm: UTSJSONObject by `$data`
    open var rectificationImages: UTSArray<RectificationImage> by `$data`
    open var canSubmit: Boolean by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("recordId" to "", "loading" to false, "submitting" to false, "containerHeight" to 0, "recordDetail" to UTSJSONObject(), "problemImages" to _uA<String>(), "rectificationForm" to object : UTSJSONObject() {
            var rectificationSuggestion = ""
            var rectificationImages = _uA<String>()
        }, "rectificationImages" to _uA<RectificationImage>(), "canSubmit" to computed<Boolean>(fun(): Boolean {
            return (this.rectificationForm["rectificationSuggestion"] as String).trim().length >= 10
        }
        ))
    }
    open var getFileSize = ::gen_getFileSize_fn
    open fun gen_getFileSize_fn(filePath: String): UTSPromise<Number> {
        return wrapUTSPromise(suspend w@{
                return@w UTSPromise<Number>(fun(resolve, _reject){
                    val file = java.io.File(filePath)
                    resolve(file.length())
                }
                )
        })
    }
    open var initPage = ::gen_initPage_fn
    open fun gen_initPage_fn() {
        if (this.recordId != null && this.recordId != "") {
            this.loadRecordDetail()
        }
    }
    open var calculateContainerHeight = ::gen_calculateContainerHeight_fn
    open fun gen_calculateContainerHeight_fn() {
        val systemInfo = uni_getSystemInfoSync()
        val navHeight: Number = 44
        val footerHeight: Number = 80
        this.containerHeight = systemInfo.windowHeight - navHeight - footerHeight
    }
    open var loadRecordDetail = ::gen_loadRecordDetail_fn
    open fun gen_loadRecordDetail_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend {
                this.loading = true
                try {
                    val response = await(uni_request<Any>(RequestOptions(url = "/monthly-inspection-record/" + this.recordId, method = "GET", header = object : UTSJSONObject() {
                        var Authorization = "Bearer " + uni_getStorageSync("token")
                    }))) as RequestTask
                    val resData = (response as UTSJSONObject)["data"] as UTSJSONObject
                    if (resData["code"] as Number === 200) {
                        this.recordDetail = resData["data"] as UTSJSONObject
                        this.parseProblemImages()
                    } else {
                        val message = if (resData["message"] != null) {
                            resData["message"] as String
                        } else {
                            "获取详情失败"
                        }
                        uni_showToast(ShowToastOptions(title = message, icon = "none"))
                    }
                }
                 catch (error: Throwable) {
                    console.error("获取月检记录详情失败:", error, " at pages/monthly/confirm-rectification.uvue:248")
                    uni_showToast(ShowToastOptions(title = "网络错误，请重试", icon = "none"))
                }
                 finally{
                    this.loading = false
                }
        })
    }
    open var parseProblemImages = ::gen_parseProblemImages_fn
    open fun gen_parseProblemImages_fn() {
        val problemImages = this.recordDetail["problemImages"] as String?
        if (problemImages != null) {
            try {
                this.problemImages = UTSAndroid.consoleDebugError(JSON.parse(problemImages), " at pages/monthly/confirm-rectification.uvue:262") as UTSArray<String>
            }
             catch (error: Throwable) {
                console.error("解析问题图片失败:", error, " at pages/monthly/confirm-rectification.uvue:264")
                this.problemImages = _uA()
            }
        }
    }
    open var chooseImage = ::gen_chooseImage_fn
    open fun gen_chooseImage_fn() {
        uni_chooseImage(ChooseImageOptions(count = 3 - this.rectificationImages.length, sizeType = _uA(
            "compressed"
        ), sourceType = _uA(
            "album",
            "camera"
        ), success = fun(res){
            val tempFilePaths = res.tempFilePaths
            for(tempFilePath in resolveUTSValueIterator(tempFilePaths)){
                val file = tempFilePath
                this.getFileSize(file).then(fun(fileSize){
                    if (fileSize > 2097152) {
                        uni_showToast(ShowToastOptions(title = "图片大小不能超过2MB", icon = "none"))
                    } else {
                        this.rectificationImages.push(RectificationImage(url = tempFilePath, file = tempFilePath))
                    }
                }
                )
            }
        }
        ))
    }
    open var removeImage = ::gen_removeImage_fn
    open fun gen_removeImage_fn(index: Number) {
        this.rectificationImages.splice(index, 1)
    }
    open var handleConfirm = ::gen_handleConfirm_fn
    open fun gen_handleConfirm_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend w@{
                if (!this.canSubmit) {
                    uni_showToast(ShowToastOptions(title = "请输入至少10个字符的整改建议", icon = "none"))
                    return@w
                }
                try {
                    await(uni_showModal(ShowModalOptions(title = "确认整改", content = "确认提交整改信息吗？提交后将更新记录状态为\"已整改\"。", confirmText = "确认", cancelText = "取消")))
                    this.submitting = true
                    val uploadedImages = _uA<String>()
                    for(image in resolveUTSValueIterator(this.rectificationImages)){
                        try {
                            val uploadResult = await(this.uploadImage(image.file as String))
                            uploadedImages.push(uploadResult)
                        }
                         catch (error: Throwable) {
                            console.error("图片上传失败:", error, " at pages/monthly/confirm-rectification.uvue:333")
                        }
                    }
                    val response = await(uni_request<Any>(RequestOptions(url = "/monthly-inspection-record/confirm-rectification/" + this.recordId, method = "POST", header = object : UTSJSONObject() {
                        var Authorization = "Bearer " + uni_getStorageSync("token")
                        var `Content-Type` = "application/json"
                    }, data = _uO("rectificationSuggestion" to this.rectificationForm["rectificationSuggestion"], "rectificationImages" to JSON.stringify(uploadedImages), "status" to 1))))
                    val resData = (response as UTSJSONObject)["data"] as UTSJSONObject
                    if (resData["code"] as Number === 200) {
                        uni_showToast(ShowToastOptions(title = "整改确认成功", icon = "success"))
                        setTimeout(fun(){
                            this.goBack()
                        }, 1500)
                    } else {
                        uni_showToast(ShowToastOptions(title = if (resData["message"] != null) {
                            resData["message"] as String
                        } else {
                            "整改确认失败"
                        }
                        , icon = "none"))
                    }
                }
                 catch (error: Throwable) {
                    if ((error as UTSJSONObject)["errMsg"] !== "showModal:fail cancel") {
                        console.error("整改确认失败:", error, " at pages/monthly/confirm-rectification.uvue:370")
                        uni_showToast(ShowToastOptions(title = "网络错误，请重试", icon = "none"))
                    }
                }
                 finally{
                    this.submitting = false
                }
        })
    }
    open var uploadImage = ::gen_uploadImage_fn
    open fun gen_uploadImage_fn(filePath: String): UTSPromise<String> {
        return UTSPromise(fun(resolve, reject){
            uni_uploadFile(UploadFileOptions(url = "/file/upload", filePath = filePath, name = "file", header = object : UTSJSONObject() {
                var Authorization = "Bearer " + uni_getStorageSync("token")
            }, success = fun(res){
                try {
                    val data = UTSAndroid.consoleDebugError(JSON.parse(res.data), " at pages/monthly/confirm-rectification.uvue:392")
                    if ((data as UTSJSONObject)["code"] as Number === 200) {
                        resolve((data as UTSJSONObject)["data"] as String)
                    } else {
                        reject(UTSError((data as UTSJSONObject)["message"] as String ?: "上传失败"))
                    }
                }
                 catch (error: Throwable) {
                    reject(error)
                }
            }
            , fail = fun(error){
                reject(error)
            }
            ))
        }
        )
    }
    open var getStatusText = ::gen_getStatusText_fn
    open fun gen_getStatusText_fn(status: Number): String {
        val statusMap = Map<Number, String>(_uA(
            _uA(
                0,
                "待整改"
            ),
            _uA(
                1,
                "已整改"
            ),
            _uA(
                2,
                "已复查"
            )
        ))
        return statusMap.get(status) ?: "未知"
    }
    open var getStatusClass = ::gen_getStatusClass_fn
    open fun gen_getStatusClass_fn(status: Number): String {
        val classMap = Map<Number, String>(_uA(
            _uA(
                0,
                "status-pending"
            ),
            _uA(
                1,
                "status-rectified"
            ),
            _uA(
                2,
                "status-reviewed"
            )
        ))
        return classMap.get(status) ?: ""
    }
    open var formatDateTime = ::gen_formatDateTime_fn
    open fun gen_formatDateTime_fn(dateTime: String): String {
        if (dateTime == "") {
            return "暂无"
        }
        val date = Date(dateTime)
        return date.toLocaleString()
    }
    open var previewImage = ::gen_previewImage_fn
    open fun gen_previewImage_fn(current: String, urls: UTSArray<String>) {
        uni_previewImage(PreviewImageOptions(current = current, urls = if (urls != null) {
            urls
        } else {
            _uA(
                current
            )
        }
        ))
    }
    open var onImageError = ::gen_onImageError_fn
    open fun gen_onImageError_fn(e: Any) {
        console.error("图片加载失败:", e, " at pages/monthly/confirm-rectification.uvue:441")
    }
    open var onImageLoad = ::gen_onImageLoad_fn
    open fun gen_onImageLoad_fn(e: Any) {
        console.log("图片加载成功:", e, " at pages/monthly/confirm-rectification.uvue:445")
    }
    open var onScrollToLower = ::gen_onScrollToLower_fn
    open fun gen_onScrollToLower_fn() {}
    open var goBack = ::gen_goBack_fn
    open fun gen_goBack_fn() {
        uni_navigateBack(null)
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
                return _uM("container" to _pS(_uM("backgroundColor" to "#f5f5f5")), "nav-bar" to _pS(_uM("display" to "flex", "alignItems" to "center", "height" to 44, "backgroundColor" to "#007AFF", "color" to "#FFFFFF", "position" to "relative")), "nav-left" to _pS(_uM("width" to 44, "height" to 44, "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "nav-icon" to _pS(_uM("fontSize" to 24, "fontWeight" to "bold")), "nav-title" to _pS(_uM("flex" to 1, "textAlign" to "center", "fontSize" to 16, "fontWeight" to "bold")), "nav-right" to _pS(_uM("width" to 44, "height" to 44)), "content" to _pS(_uM("paddingTop" to 16, "paddingRight" to 16, "paddingBottom" to 100, "paddingLeft" to 16)), "section" to _pS(_uM("marginBottom" to 16)), "section-title" to _pS(_uM("fontSize" to 16, "fontWeight" to "bold", "color" to "#333333", "marginBottom" to 12, "paddingLeft" to 8, "borderLeftWidth" to 4, "borderLeftStyle" to "solid", "borderLeftColor" to "#007AFF")), "info-card" to _pS(_uM("backgroundColor" to "#ffffff", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 16, "paddingRight" to 16, "paddingBottom" to 16, "paddingLeft" to 16, "boxShadow" to "0 2px 4px rgba(0, 0, 0, 0.1)")), "form-card" to _pS(_uM("backgroundColor" to "#ffffff", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 16, "paddingRight" to 16, "paddingBottom" to 16, "paddingLeft" to 16, "boxShadow" to "0 2px 4px rgba(0, 0, 0, 0.1)")), "info-item" to _pS(_uM("display" to "flex", "marginBottom" to 12, "alignItems" to "flex-start", "marginBottom:last-child" to 0)), "label" to _pS(_uM("fontSize" to 14, "color" to "#666666", "minWidth" to 80, "flexShrink" to 0)), "value" to _pS(_uM("fontSize" to 14, "color" to "#333333", "flex" to 1, "wordBreak" to "break-all")), "status-tag" to _pS(_uM("paddingTop" to 4, "paddingRight" to 8, "paddingBottom" to 4, "paddingLeft" to 8, "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "fontSize" to 12, "color" to "#ffffff")), "status-pending" to _pS(_uM("backgroundColor" to "#f56c6c")), "status-rectified" to _pS(_uM("backgroundColor" to "#e6a23c")), "status-reviewed" to _pS(_uM("backgroundColor" to "#67c23a")), "image-grid" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "8px")), "problem-image" to _pS(_uM("width" to 100, "height" to 100, "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd")), "form-item" to _pS(_uM("marginBottom" to 20, "marginBottom:last-child" to 0)), "form-label" to _pS(_uM("fontSize" to 14, "color" to "#333333", "marginBottom" to 8, "display" to "flex", "alignItems" to "center")), "required" to _pS(_uM("color" to "#f56c6c", "marginRight" to 4)), "form-textarea" to _pS(_uM("width" to "100%", "minHeight" to 100, "paddingTop" to 12, "paddingRight" to 12, "paddingBottom" to 12, "paddingLeft" to 12, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "fontSize" to 14, "lineHeight" to 1.5, "backgroundColor" to "#ffffff")), "upload-container" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "8px")), "uploaded-images" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "8px")), "uploaded-image-item" to _pS(_uM("position" to "relative", "width" to 80, "height" to 80)), "uploaded-image" to _pS(_uM("width" to "100%", "height" to "100%", "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd")), "delete-btn" to _pS(_uM("position" to "absolute", "top" to -8, "right" to -8, "width" to 20, "height" to 20, "backgroundColor" to "#f56c6c", "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "delete-icon" to _pS(_uM("color" to "#ffffff", "fontSize" to 14, "fontWeight" to "bold")), "upload-btn" to _pS(_uM("width" to 80, "height" to 80, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "backgroundColor" to "#fafafa")), "upload-icon" to _pS(_uM("fontSize" to 24, "color" to "#999999", "marginBottom" to 4)), "upload-text" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "upload-tip" to _pS(_uM("fontSize" to 12, "color" to "#999999", "marginTop" to 8, "lineHeight" to 1.4)), "loading-overlay" to _pS(_uM("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundColor" to "rgba(0,0,0,0.5)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "zIndex" to 1000)), "loading-content" to _pS(_uM("backgroundColor" to "#ffffff", "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "textAlign" to "center")), "footer" to _pS(_uM("position" to "fixed", "bottom" to 0, "left" to 0, "right" to 0, "backgroundColor" to "#ffffff", "paddingTop" to 16, "paddingRight" to 16, "paddingBottom" to 16, "paddingLeft" to 16, "borderTopWidth" to 1, "borderTopStyle" to "solid", "borderTopColor" to "#eeeeee", "display" to "flex", "gap" to "12px", "zIndex" to 100)), "btn" to _pS(_uM("flex" to 1, "height" to 44, "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "fontSize" to 16, "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "btn-cancel" to _pS(_uM("backgroundColor" to "#f5f5f5", "color" to "#666666")), "btn-confirm" to _pS(_uM("backgroundColor" to "#007AFF", "color" to "#ffffff", "backgroundColor:disabled" to "#cccccc", "color:disabled" to "#999999")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
