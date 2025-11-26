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
import io.dcloud.uniapp.extapi.getFileSystemManager as uni_getFileSystemManager
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfoSync as uni_getSystemInfoSync
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showModal as uni_showModal
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.uploadFile as uni_uploadFile
open class GenPagesMonthlyConfirmReview : BasePage {
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
                _cE("text", _uM("class" to "nav-title"), "确认复查"),
                _cE("view", _uM("class" to "nav-right"))
            )),
            _cE("scroll-view", _uM("class" to "content", "scroll-y" to "true", "style" to _nS(_uM("height" to (_ctx.containerHeight + "px"))), "onScrolltolower" to _ctx.onScrollToLower), _uA(
                _cE("view", _uM("class" to "section"), _uA(
                    _cE("view", _uM("class" to "section-title"), "问题信息"),
                    _cE("view", _uM("class" to "info-card problem-info"), _uA(
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "问题描述："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.recordDetail["problemDescription"] ?: "暂无"), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "责任人："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.recordDetail["responsiblePerson"] ?: "暂无"), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "责任单位："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.recordDetail["responsibleUnit"] ?: "暂无"), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "发现位置："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.recordDetail["location"] ?: "暂无"), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "检查人："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.recordDetail["inspector"] ?: "暂无"), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "整改时间："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.formatDateTime(_ctx.recordDetail["rectificationTime"])), 1)
                        ))
                    )),
                    if (_ctx.problemImages.length > 0) {
                        _cE("view", _uM("key" to 0, "class" to "image-section"), _uA(
                            _cE("view", _uM("class" to "image-title"), "问题照片"),
                            _cE("view", _uM("class" to "image-grid"), _uA(
                                _cE(Fragment, null, RenderHelpers.renderList(_ctx.problemImages, fun(image, index, __index, _cached): Any {
                                    return _cE("image", _uM("key" to index, "src" to image, "class" to "problem-image", "mode" to "aspectFill", "onClick" to fun(){
                                        _ctx.previewImage(image, _ctx.problemImages)
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
                )),
                _cE("view", _uM("class" to "section"), _uA(
                    _cE("view", _uM("class" to "section-title"), "整改情况"),
                    _cE("view", _uM("class" to "info-card rectification-info"), _uA(
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "性质界定："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.recordDetail["natureDefinition"] ?: "暂无"), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "专业："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.recordDetail["professionName"] ?: "暂无"), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "所属部门："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.recordDetail["departmentName"] ?: "暂无"), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "整改建议："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.recordDetail["rectificationSuggestion"] ?: "暂无"), 1)
                        )),
                        _cE("view", _uM("class" to "info-item"), _uA(
                            _cE("text", _uM("class" to "label"), "整改效果："),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.recordDetail["rectificationEffect"] ?: "暂无"), 1)
                        ))
                    )),
                    if (_ctx.rectificationImages.length > 0) {
                        _cE("view", _uM("key" to 0, "class" to "image-section"), _uA(
                            _cE("view", _uM("class" to "image-title"), "整改图片"),
                            _cE("view", _uM("class" to "image-grid"), _uA(
                                _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectificationImages, fun(image, index, __index, _cached): Any {
                                    return _cE("image", _uM("key" to index, "src" to image, "class" to "problem-image", "mode" to "aspectFill", "onClick" to fun(){
                                        _ctx.previewImage(image, _ctx.rectificationImages)
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
                )),
                _cE("view", _uM("class" to "section"), _uA(
                    _cE("view", _uM("class" to "section-title"), "复查信息"),
                    _cE("view", _uM("class" to "form-card"), _uA(
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("view", _uM("class" to "form-label"), _uA(
                                _cE("text", _uM("class" to "required"), "*"),
                                _cE("text", null, "复查建议")
                            )),
                            _cE("textarea", _uM("modelValue" to _ctx.reviewForm["reviewSuggestion"], "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.reviewForm["reviewSuggestion"] = `$event`.detail.value
                            }
                            , "class" to "form-textarea", "placeholder" to "请输入复查建议...", "maxlength" to "500", "show-count" to true, "auto-height" to ""), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("view", _uM("class" to "form-label"), _uA(
                                _cE("text", _uM("class" to "required"), "*"),
                                _cE("text", null, "复查效果描述")
                            )),
                            _cE("textarea", _uM("modelValue" to _ctx.reviewForm["reviewEffect"], "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.reviewForm["reviewEffect"] = `$event`.detail.value
                            }
                            , "class" to "form-textarea", "placeholder" to "请描述复查效果...", "maxlength" to "500", "show-count" to true, "auto-height" to ""), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("view", _uM("class" to "form-label"), "复查图片"),
                            _cE("view", _uM("class" to "upload-container"), _uA(
                                if (_ctx.reviewImages.length > 0) {
                                    _cE("view", _uM("key" to 0, "class" to "uploaded-images"), _uA(
                                        _cE(Fragment, null, RenderHelpers.renderList(_ctx.reviewImages, fun(image, index, __index, _cached): Any {
                                            return _cE("view", _uM("key" to index, "class" to "uploaded-image-item"), _uA(
                                                _cE("image", _uM("src" to image.url, "class" to "uploaded-image", "mode" to "aspectFill", "onClick" to fun(){
                                                    _ctx.previewImage(image.url, _ctx.reviewImages.map(fun(img: ReviewImageType): String {
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
                                if (_ctx.reviewImages.length < 3) {
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
                    "确认复查"
                }
                ), 9, _uA(
                    "disabled",
                    "onClick"
                ))
            )),
            if (isTrue(_ctx.loading)) {
                _cE("view", _uM("key" to 0, "class" to "loading-container"), _uA(
                    _cE("text", _uM("class" to "loading-text"), "加载中...")
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
    open var reviewForm: ReviewFormType by `$data`
    open var reviewImages: UTSArray<ReviewImageType> by `$data`
    open var problemImages: UTSArray<String> by `$data`
    open var rectificationImages: UTSArray<String> by `$data`
    open var canSubmit: Boolean by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("recordId" to "", "loading" to false, "submitting" to false, "containerHeight" to 0, "recordDetail" to UTSJSONObject(), "reviewForm" to ReviewFormType(reviewSuggestion = "", reviewEffect = ""), "reviewImages" to _uA<ReviewImageType>(), "problemImages" to _uA<String>(), "rectificationImages" to _uA<String>(), "canSubmit" to computed<Boolean>(fun(): Boolean {
            return this.reviewForm.reviewSuggestion.trim().length > 0 && this.reviewForm.reviewEffect.trim().length > 0
        }
        ))
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
                    val response = await(uni_request<Any>(RequestOptions(url = "" + this.`$baseUrl` + "/monthly-inspection-record/" + this.recordId, method = "GET", header = object : UTSJSONObject() {
                        var Authorization = "Bearer " + uni_getStorageSync("token")
                    })))
                    if (response.data.code === 200) {
                        this.recordDetail = response.data.data
                        this.parseProblemImages()
                        this.parseRectificationImages()
                        this.initReviewForm()
                    } else {
                        uni_showToast(ShowToastOptions(title = response.data.message || "获取详情失败", icon = "none"))
                    }
                }
                 catch (error: Throwable) {
                    console.error("获取月检记录详情失败:", error, " at pages/monthly/confirm-review.uvue:290")
                    uni_showToast(ShowToastOptions(title = "网络错误，请重试", icon = "none"))
                }
                 finally{
                    this.loading = false
                }
        })
    }
    open var parseProblemImages = ::gen_parseProblemImages_fn
    open fun gen_parseProblemImages_fn() {
        if (this.recordDetail.problemImages) {
            try {
                this.problemImages = UTSAndroid.consoleDebugError(JSON.parse(this.recordDetail.problemImages), " at pages/monthly/confirm-review.uvue:303")
            }
             catch (error: Throwable) {
                console.error("解析问题图片失败:", error, " at pages/monthly/confirm-review.uvue:305")
                this.problemImages = _uA()
            }
        }
    }
    open var parseRectificationImages = ::gen_parseRectificationImages_fn
    open fun gen_parseRectificationImages_fn() {
        if (this.recordDetail.rectificationImages) {
            try {
                this.rectificationImages = UTSAndroid.consoleDebugError(JSON.parse(this.recordDetail.rectificationImages), " at pages/monthly/confirm-review.uvue:314")
            }
             catch (error: Throwable) {
                console.error("解析整改图片失败:", error, " at pages/monthly/confirm-review.uvue:316")
                this.rectificationImages = _uA()
            }
        }
    }
    open var initReviewForm = ::gen_initReviewForm_fn
    open fun gen_initReviewForm_fn() {
        if (this.recordDetail.reviewSuggestion) {
            this.reviewForm.reviewSuggestion = this.recordDetail.reviewSuggestion
        }
        if (this.recordDetail.reviewEffect) {
            this.reviewForm.reviewEffect = this.recordDetail.reviewEffect
        }
        if (this.recordDetail.reviewImages) {
            try {
                val images = UTSAndroid.consoleDebugError(JSON.parse(this.recordDetail.reviewImages), " at pages/monthly/confirm-review.uvue:334")
                this.reviewImages = images.map(fun(url): UTSJSONObject {
                    return (_uO("url" to url, "file" to url))
                }
                )
            }
             catch (error: Throwable) {
                console.error("解析复查图片失败:", error, " at pages/monthly/confirm-review.uvue:337")
                this.reviewImages = _uA()
            }
        }
    }
    open var chooseImage = ::gen_chooseImage_fn
    open fun gen_chooseImage_fn() {
        uni_chooseImage(ChooseImageOptions(count = 3 - this.reviewImages.length, sizeType = _uA(
            "compressed"
        ), sourceType = _uA(
            "album",
            "camera"
        ), success = fun(res){
            res.tempFilePaths.forEach(fun(tempFilePath){
                val fileManager = uni_getFileSystemManager()
                fileManager.getFileInfo(GetFileInfoOptions(filePath = tempFilePath, success = fun(res: UTSJSONObject){
                    val fileInfo = res as UTSJSONObject
                    val size = fileInfo["size"] as Number
                    if (size > 2097152) {
                        uni_showToast(ShowToastOptions(title = "图片大小不能超过2MB", icon = "none"))
                        return
                    }
                    this.reviewImages.push(ReviewImageType(url = tempFilePath, file = tempFilePath))
                }
                ))
            }
            )
        }
        ))
    }
    open var removeImage = ::gen_removeImage_fn
    open fun gen_removeImage_fn(index) {
        this.reviewImages.splice(index, 1)
    }
    open var handleConfirm = ::gen_handleConfirm_fn
    open fun gen_handleConfirm_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend w@{
                if (!this.canSubmit) {
                    uni_showToast(ShowToastOptions(title = "请填写完整的复查信息", icon = "none"))
                    return@w
                }
                try {
                    await(uni_showModal(ShowModalOptions(title = "确认复查", content = "确认提交复查信息吗？提交后将更新记录状态为\"已复查\"。", confirmText = "确认", cancelText = "取消")))
                    this.submitting = true
                    val uploadedImages = _uA()
                    for(image in resolveUTSValueIterator(this.reviewImages)){
                        try {
                            val uploadResult = await(this.uploadImage(image.file))
                            uploadedImages.push(uploadResult)
                        }
                         catch (error: Throwable) {
                            console.error("图片上传失败:", error, " at pages/monthly/confirm-review.uvue:406")
                        }
                    }
                    val response = await(uni_request<Any>(RequestOptions(url = "" + this.`$baseUrl` + "/monthly-inspection-record/confirm-review/" + this.recordId, method = "POST", header = object : UTSJSONObject() {
                        var Authorization = "Bearer " + uni_getStorageSync("token")
                        var `Content-Type` = "application/json"
                    }, data = _uO("reviewSuggestion" to this.reviewForm.reviewSuggestion, "reviewEffect" to this.reviewForm.reviewEffect, "reviewImages" to JSON.stringify(uploadedImages), "status" to 2))))
                    if (response.data.code === 200) {
                        uni_showToast(ShowToastOptions(title = "复查确认成功", icon = "success"))
                        setTimeout(fun(){
                            this.goBack()
                        }, 1500)
                    } else {
                        uni_showToast(ShowToastOptions(title = response.data.message || "复查确认失败", icon = "none"))
                    }
                }
                 catch (error: Throwable) {
                    if (error.errMsg !== "showModal:fail cancel") {
                        console.error("复查确认失败:", error, " at pages/monthly/confirm-review.uvue:443")
                        uni_showToast(ShowToastOptions(title = "网络错误，请重试", icon = "none"))
                    }
                }
                 finally{
                    this.submitting = false
                }
        })
    }
    open var uploadImage = ::gen_uploadImage_fn
    open fun gen_uploadImage_fn(filePath): UTSPromise<*> {
        return UTSPromise(fun(resolve, reject){
            uni_uploadFile(UploadFileOptions(url = "/file/upload", filePath = filePath, name = "file", header = object : UTSJSONObject() {
                var Authorization = "Bearer " + uni_getStorageSync("token")
            }, success = fun(res){
                try {
                    val data = UTSAndroid.consoleDebugError(JSON.parse(res.data), " at pages/monthly/confirm-review.uvue:465")
                    if (data.code === 200) {
                        resolve(data.data)
                    } else {
                        reject(UTSError(data.message || "上传失败"))
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
    open var formatDateTime = ::gen_formatDateTime_fn
    open fun gen_formatDateTime_fn(dateTime): String {
        if (!dateTime) {
            return "暂无"
        }
        val date = Date(dateTime)
        return date.toLocaleString("zh-CN")
    }
    open var previewImage = ::gen_previewImage_fn
    open fun gen_previewImage_fn(current, urls) {
        uni_previewImage(PreviewImageOptions(current = current, urls = urls || _uA(
            current
        )))
    }
    open var onImageError = ::gen_onImageError_fn
    open fun gen_onImageError_fn(e) {
        console.error("图片加载失败:", e, " at pages/monthly/confirm-review.uvue:496")
    }
    open var onImageLoad = ::gen_onImageLoad_fn
    open fun gen_onImageLoad_fn(e) {
        console.log("图片加载成功:", e, " at pages/monthly/confirm-review.uvue:500")
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
                return _uM("container" to _pS(_uM("backgroundColor" to "#f5f5f5")), "content" to _pS(_uM("paddingTop" to 16, "paddingRight" to 16, "paddingBottom" to 100, "paddingLeft" to 16)), "section" to _pS(_uM("marginBottom" to 16)), "section-title" to _pS(_uM("fontSize" to 16, "fontWeight" to "bold", "color" to "#333333", "marginBottom" to 12, "paddingLeft" to 8, "borderLeftWidth" to 4, "borderLeftStyle" to "solid", "borderLeftColor" to "#007AFF")), "info-card" to _pS(_uM("backgroundColor" to "#ffffff", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 16, "paddingRight" to 16, "paddingBottom" to 16, "paddingLeft" to 16, "boxShadow" to "0 2px 4px rgba(0, 0, 0, 0.1)")), "form-card" to _pS(_uM("backgroundColor" to "#ffffff", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 16, "paddingRight" to 16, "paddingBottom" to 16, "paddingLeft" to 16, "boxShadow" to "0 2px 4px rgba(0, 0, 0, 0.1)")), "problem-info" to _pS(_uM("backgroundColor" to "#f8f9fa", "borderLeftWidth" to 4, "borderLeftStyle" to "solid", "borderLeftColor" to "#17a2b8")), "rectification-info" to _pS(_uM("backgroundColor" to "#f0f9ff", "borderLeftWidth" to 4, "borderLeftStyle" to "solid", "borderLeftColor" to "#007bff")), "info-item" to _pS(_uM("display" to "flex", "marginBottom" to 12, "alignItems" to "flex-start", "marginBottom:last-child" to 0)), "label" to _pS(_uM("fontSize" to 14, "color" to "#666666", "minWidth" to 80, "flexShrink" to 0)), "value" to _pS(_uM("fontSize" to 14, "color" to "#333333", "flex" to 1, "wordBreak" to "break-all")), "image-section" to _pS(_uM("marginTop" to 16)), "image-title" to _pS(_uM("fontSize" to 14, "color" to "#666666", "marginBottom" to 8)), "image-grid" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "8px")), "problem-image" to _pS(_uM("width" to 100, "height" to 100, "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd")), "form-item" to _pS(_uM("marginBottom" to 20, "marginBottom:last-child" to 0)), "form-label" to _pS(_uM("fontSize" to 14, "color" to "#333333", "marginBottom" to 8, "display" to "flex", "alignItems" to "center")), "required" to _pS(_uM("color" to "#f56c6c", "marginRight" to 4)), "form-textarea" to _pS(_uM("width" to "100%", "minHeight" to 100, "paddingTop" to 12, "paddingRight" to 12, "paddingBottom" to 12, "paddingLeft" to 12, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "fontSize" to 14, "lineHeight" to 1.5, "backgroundColor" to "#ffffff")), "upload-container" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "8px")), "uploaded-images" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "8px")), "uploaded-image-item" to _pS(_uM("position" to "relative", "width" to 80, "height" to 80)), "uploaded-image" to _pS(_uM("width" to "100%", "height" to "100%", "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd")), "delete-btn" to _pS(_uM("position" to "absolute", "top" to -8, "right" to -8, "width" to 20, "height" to 20, "backgroundColor" to "#f56c6c", "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "delete-icon" to _pS(_uM("color" to "#ffffff", "fontSize" to 14, "fontWeight" to "bold")), "upload-btn" to _pS(_uM("width" to 80, "height" to 80, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "backgroundColor" to "#fafafa")), "upload-icon" to _pS(_uM("fontSize" to 24, "color" to "#999999", "marginBottom" to 4)), "upload-text" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "upload-tip" to _pS(_uM("fontSize" to 12, "color" to "#999999", "marginTop" to 8, "lineHeight" to 1.4)), "footer" to _pS(_uM("position" to "fixed", "bottom" to 0, "left" to 0, "right" to 0, "backgroundColor" to "#ffffff", "paddingTop" to 16, "paddingRight" to 16, "paddingBottom" to 16, "paddingLeft" to 16, "borderTopWidth" to 1, "borderTopStyle" to "solid", "borderTopColor" to "#eeeeee", "display" to "flex", "gap" to "12px", "zIndex" to 100)), "btn" to _pS(_uM("flex" to 1, "height" to 44, "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "fontSize" to 16, "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "btn-cancel" to _pS(_uM("backgroundColor" to "#f5f5f5", "color" to "#666666")), "btn-confirm" to _pS(_uM("backgroundColor" to "#007AFF", "color" to "#ffffff", "backgroundColor:disabled" to "#cccccc", "color:disabled" to "#999999")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
