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
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfoSync as uni_getSystemInfoSync
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesMonthlyDetail : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: Any) {
            if (options.id) {
                this.recordId = parseInt(options.id)
            }
            val systemInfo = uni_getSystemInfoSync()
            this.containerHeight = systemInfo.windowHeight
            this.loadRecordDetail()
        }
        , __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "scroll-top" to _ctx.scrollTop, "style" to _nS(_uM("height" to (_ctx.containerHeight + "px"))), "enable-back-to-top" to "true"), _uA(
            if (isTrue(_ctx.loading)) {
                _cE("view", _uM("key" to 0, "class" to "loading-state"), _uA(
                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                ))
            } else {
                if (isTrue(_ctx.recordDetail)) {
                    _cE("view", _uM("key" to 1, "class" to "detail-content"), _uA(
                        _cE("view", _uM("class" to "status-header"), _uA(
                            _cE("text", _uM("class" to "page-title"), "月检记录详情"),
                            _cE("view", _uM("class" to _nC(_uA(
                                "record-status",
                                _ctx.getStatusClass(_ctx.recordDetail?.status ?: 0)
                            ))), _uA(
                                _cE("text", null, _tD(_ctx.getStatusText(_ctx.recordDetail?.status ?: 0)), 1)
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
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.problemDescription ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "责任单位"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.responsibleUnit ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "责任人"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.responsiblePerson ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "检查人"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.inspectorName ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "所属部门"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.departmentName ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "创建时间"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.formatDateTime(_ctx.recordDetail?.createTime ?: "")), 1)
                            ))
                        )),
                        if (isTrue(_ctx.recordDetail?.rectificationTime)) {
                            _cE("view", _uM("key" to 1, "class" to "form-item"), _uA(
                                _cE("text", _uM("class" to "form-label"), "整改时间"),
                                _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                    _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.formatDateTime(_ctx.recordDetail?.rectificationTime ?: "")), 1)
                                ))
                            ))
                        } else {
                            _cC("v-if", true)
                        },
                        if (isTrue(_ctx.recordDetail?.reviewTime)) {
                            _cE("view", _uM("key" to 2, "class" to "form-item"), _uA(
                                _cE("text", _uM("class" to "form-label"), "复查时间"),
                                _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                    _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.formatDateTime(_ctx.recordDetail?.reviewTime ?: "")), 1)
                                ))
                            ))
                        } else {
                            _cC("v-if", true)
                        },
                        if (isTrue(_ctx.showActionButtons())) {
                            _cE("view", _uM("key" to 3, "class" to "action-buttons"), _uA(
                                if (_ctx.recordDetail?.status === 0) {
                                    _cE("button", _uM("key" to 0, "class" to "action-btn confirm-btn", "onClick" to _ctx.goToConfirmRectification), " 确认整改 ", 8, _uA(
                                        "onClick"
                                    ))
                                } else {
                                    _cC("v-if", true)
                                },
                                if (_ctx.recordDetail?.status === 1) {
                                    _cE("button", _uM("key" to 1, "class" to "action-btn review-btn", "onClick" to _ctx.goToConfirmReview), " 确认复查 ", 8, _uA(
                                        "onClick"
                                    ))
                                } else {
                                    _cC("v-if", true)
                                }
                            ))
                        } else {
                            _cC("v-if", true)
                        }
                    ))
                } else {
                    _cE("view", _uM("key" to 2, "class" to "error-state"), _uA(
                        _cE("text", _uM("class" to "error-text"), "记录不存在或已被删除")
                    ))
                }
            }
        ), 12, _uA(
            "scroll-top"
        ))
    }
    open var recordDetail: MonthlyRecordDetail? by `$data`
    open var loading: Boolean by `$data`
    open var recordId: Number by `$data`
    open var imageList: UTSArray<String> by `$data`
    open var scrollTop: Number by `$data`
    open var containerHeight: Number by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("recordDetail" to null as MonthlyRecordDetail?, "loading" to true, "recordId" to 0, "imageList" to _uA<String>(), "scrollTop" to 0, "containerHeight" to 0)
    }
    open var loadRecordDetail = ::gen_loadRecordDetail_fn
    open fun gen_loadRecordDetail_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/" + this.recordId), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            this.loading = false
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                this.recordDetail = object : UTSJSONObject() {
                    var id = data.getNumber("id") ?: 0
                    var problemDescription = data.getString("problemDescription") ?: ""
                    var responsibleUnit = data.getString("responsibleUnit") ?: ""
                    var responsiblePerson = data.getString("responsiblePerson") ?: ""
                    var inspectorName = data.getString("inspectorName") ?: ""
                    var departmentName = data.getString("departmentName") ?: ""
                    var status = data.getNumber("status") ?: 0
                    var createTime = data.getString("createTime") ?: ""
                    var rectificationTime = data.getString("rectificationTime") ?: ""
                    var reviewTime = data.getString("reviewTime") ?: ""
                    var images = _uA()
                }
                val images = data.getArray("images") as UTSArray<String>
                if (images && images.length > 0) {
                    this.imageList = images.map(fun(img): String {
                        return getApiUrl("/uploads/images/" + img)
                    }
                    )
                }
            } else {
                uni_showToast(ShowToastOptions(title = "加载失败", icon = "none"))
            }
        }
        , fail = fun(error){
            this.loading = false
            console.log("加载月检记录详情失败:", error, " at pages/monthly/detail.uvue:206")
            uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
        }
        ))
    }
    open var showActionButtons = ::gen_showActionButtons_fn
    open fun gen_showActionButtons_fn(): Boolean {
        if (!this.recordDetail) {
            return false
        }
        val userInfoData = uni_getStorageSync("userInfo")
        val userInfoStr = if (userInfoData != null) {
            userInfoData as String
        } else {
            ""
        }
        if (userInfoStr != null && userInfoStr != "" && userInfoStr != "null" && userInfoStr != "undefined") {
            try {
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/detail.uvue:223") as UTSJSONObject
                val role = parsedUserInfo.getString("role") ?: ""
                return role === "ADMIN" || role === "SUPER_ADMIN"
            }
             catch (error: Throwable) {
                return false
            }
        }
        return false
    }
    open var goToConfirmRectification = ::gen_goToConfirmRectification_fn
    open fun gen_goToConfirmRectification_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/monthly/confirm-rectification?id=" + this.recordId))
    }
    open var goToConfirmReview = ::gen_goToConfirmReview_fn
    open fun gen_goToConfirmReview_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/monthly/confirm-review?id=" + this.recordId))
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
    open var formatDateTime = ::gen_formatDateTime_fn
    open fun gen_formatDateTime_fn(dateStr: String): String {
        if (!dateStr) {
            return ""
        }
        try {
            val date = Date(dateStr)
            val year = date.getFullYear()
            val month = String(date.getMonth() + 1).padStart(2, "0")
            val day = String(date.getDate()).padStart(2, "0")
            val hours = String(date.getHours()).padStart(2, "0")
            val minutes = String(date.getMinutes()).padStart(2, "0")
            return "" + year + "-" + month + "-" + day + " " + hours + ":" + minutes
        }
         catch (error: Throwable) {
            return dateStr
        }
    }
    open var previewImage = ::gen_previewImage_fn
    open fun gen_previewImage_fn(index: Number) {
        uni_previewImage(PreviewImageOptions(urls = this.imageList, current = index))
    }
    open var onImageError = ::gen_onImageError_fn
    open fun gen_onImageError_fn() {
        console.log("图片加载失败", " at pages/monthly/detail.uvue:288")
    }
    open var onImageLoad = ::gen_onImageLoad_fn
    open fun gen_onImageLoad_fn() {
        console.log("图片加载成功", " at pages/monthly/detail.uvue:292")
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
                return _uM("container" to _pS(_uM("backgroundColor" to "#f5f5f5", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx")), "loading-state" to _pS(_uM("display" to "flex", "justifyContent" to "center", "alignItems" to "center", "height" to "400rpx")), "loading-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#999999")), "error-state" to _pS(_uM("display" to "flex", "justifyContent" to "center", "alignItems" to "center", "height" to "400rpx")), "error-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#999999")), "detail-content" to _pS(_uM("paddingBottom" to "40rpx")), "status-header" to _pS(_uM("backgroundColor" to "#FFFFFF", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "marginBottom" to "20rpx", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "boxShadow" to "0 2rpx 8rpx rgba(0, 0, 0, 0.1)", "display" to "flex", "justifyContent" to "space-between", "alignItems" to "center")), "page-title" to _pS(_uM("fontSize" to "36rpx", "fontWeight" to "bold", "color" to "#333333")), "record-status" to _pS(_uM("paddingTop" to "12rpx", "paddingRight" to "24rpx", "paddingBottom" to "12rpx", "paddingLeft" to "24rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "fontSize" to "24rpx", "fontWeight" to "bold")), "status-pending" to _pS(_uM("backgroundColor" to "#fff3cd", "color" to "#856404")), "status-completed" to _pS(_uM("backgroundColor" to "#d4edda", "color" to "#155724")), "status-reviewed" to _pS(_uM("backgroundColor" to "#d1ecf1", "color" to "#0c5460")), "status-unknown" to _pS(_uM("backgroundColor" to "#f8d7da", "color" to "#721c24")), "form-item" to _pS(_uM("backgroundColor" to "#FFFFFF", "marginBottom" to "20rpx", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "boxShadow" to "0 2rpx 8rpx rgba(0, 0, 0, 0.1)", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx")), "form-label" to _pS(_uM("fontSize" to "28rpx", "color" to "#333333", "fontWeight" to "bold", "marginBottom" to "20rpx")), "form-input-wrapper" to _pS(_uM("width" to "100%")), "form-display-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#666666", "lineHeight" to 1.5, "minHeight" to "40rpx")), "image-upload" to _pS(_uM("marginTop" to "20rpx")), "image-list" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "20rpx")), "image-item" to _pS(_uM("width" to "200rpx", "height" to "200rpx", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "overflow" to "hidden", "boxShadow" to "0 2rpx 8rpx rgba(0, 0, 0, 0.1)")), "uploaded-image" to _pS(_uM("width" to "100%", "height" to "100%")), "action-buttons" to _pS(_uM("display" to "flex", "gap" to "20rpx", "marginTop" to "40rpx", "paddingTop" to 0, "paddingRight" to "20rpx", "paddingBottom" to 0, "paddingLeft" to "20rpx")), "action-btn" to _pS(_uM("flex" to 1, "height" to "88rpx", "borderTopLeftRadius" to "44rpx", "borderTopRightRadius" to "44rpx", "borderBottomRightRadius" to "44rpx", "borderBottomLeftRadius" to "44rpx", "fontSize" to "32rpx", "fontWeight" to "bold", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "color" to "#FFFFFF", "opacity:active" to 0.8)), "confirm-btn" to _pS(_uM("backgroundImage" to "linear-gradient(135deg, #4CAF50, #45a049)", "backgroundColor" to "rgba(0,0,0,0)")), "review-btn" to _pS(_uM("backgroundImage" to "linear-gradient(135deg, #2196F3, #1976D2)", "backgroundColor" to "rgba(0,0,0,0)")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
