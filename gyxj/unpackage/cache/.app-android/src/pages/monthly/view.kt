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
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfo as uni_getSystemInfo
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage
import io.dcloud.uniapp.extapi.request as uni_request
open class GenPagesMonthlyView : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: UTSJSONObject) {
            this.id = options.getString("id") ?: ""
            this.initContainer()
            this.loadDetail()
        }
        , __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "style" to _nS(_uM("height" to (_ctx.containerHeight + "px")))), _uA(
            if (isTrue(_ctx.loading)) {
                _cE("view", _uM("key" to 0, "class" to "loading"), _uA(
                    _cE("text", null, "加载中...")
                ))
            } else {
                if (_ctx.detail != null) {
                    _cE("view", _uM("key" to 1, "class" to "content"), _uA(
                        _cE("view", _uM("class" to "header"), _uA(
                            _cE("text", _uM("class" to "title"), "月检记录详情"),
                            _cE("view", _uM("class" to _nC(_uA(
                                "status",
                                _ctx.getStatusClass(_ctx.detail!!.recordStatus)
                            ))), _uA(
                                _cE("text", null, _tD(_ctx.getStatusText(_ctx.detail!!.recordStatus)), 1)
                            ), 2)
                        )),
                        if (_ctx.problemImages.length > 0) {
                            _cE("view", _uM("key" to 0, "class" to "photos"), _uA(
                                _cE("text", _uM("class" to "label"), "问题图片（" + _tD(_ctx.problemImages.length) + "张）", 1),
                                _cE("view", _uM("class" to "image-list"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.problemImages, fun(img, idx, __index, _cached): Any {
                                        return _cE("image", _uM("key" to idx, "src" to img, "class" to "photo", "mode" to "aspectFill", "onClick" to fun(){
                                            _ctx.preview(img, _ctx.problemImages)
                                        }), null, 8, _uA(
                                            "src",
                                            "onClick"
                                        ))
                                    }), 128)
                                ))
                            ))
                        } else {
                            _cC("v-if", true)
                        },
                        if (_ctx.detail!!.recordStatus >= 1) {
                            _cE("view", _uM("key" to 1), _uA(
                                _cE("view", _uM("class" to "item"), _uA(
                                    _cE("text", _uM("class" to "label"), "整改人"),
                                    _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.rectifierName)), 1)
                                )),
                                _cE("view", _uM("class" to "item"), _uA(
                                    _cE("text", _uM("class" to "label"), "整改建议"),
                                    _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.rectificationSuggestion)), 1)
                                )),
                                _cE("view", _uM("class" to "item"), _uA(
                                    _cE("text", _uM("class" to "label"), "整改效果"),
                                    _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.rectificationEffect)), 1)
                                )),
                                if (_ctx.rectificationImages.length > 0) {
                                    _cE("view", _uM("key" to 0, "class" to "photos"), _uA(
                                        _cE("text", _uM("class" to "label"), "整改图片（" + _tD(_ctx.rectificationImages.length) + "张）", 1),
                                        _cE("view", _uM("class" to "image-list"), _uA(
                                            _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectificationImages, fun(img, idx, __index, _cached): Any {
                                                return _cE("image", _uM("key" to idx, "src" to img, "class" to "photo", "mode" to "aspectFill", "onClick" to fun(){
                                                    _ctx.preview(img, _ctx.rectificationImages)
                                                }), null, 8, _uA(
                                                    "src",
                                                    "onClick"
                                                ))
                                            }), 128)
                                        ))
                                    ))
                                } else {
                                    _cC("v-if", true)
                                }
                            ))
                        } else {
                            _cC("v-if", true)
                        },
                        if (_ctx.detail!!.recordStatus >= 2) {
                            _cE("view", _uM("key" to 2), _uA(
                                _cE("view", _uM("class" to "item"), _uA(
                                    _cE("text", _uM("class" to "label"), "复查人"),
                                    _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.reviewerName)), 1)
                                )),
                                _cE("view", _uM("class" to "item"), _uA(
                                    _cE("text", _uM("class" to "label"), "复查建议"),
                                    _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.reviewSuggestion)), 1)
                                )),
                                _cE("view", _uM("class" to "item"), _uA(
                                    _cE("text", _uM("class" to "label"), "复查效果"),
                                    _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.reviewEffect)), 1)
                                )),
                                if (_ctx.reviewImages.length > 0) {
                                    _cE("view", _uM("key" to 0, "class" to "photos"), _uA(
                                        _cE("text", _uM("class" to "label"), "复查图片（" + _tD(_ctx.reviewImages.length) + "张）", 1),
                                        _cE("view", _uM("class" to "image-list"), _uA(
                                            _cE(Fragment, null, RenderHelpers.renderList(_ctx.reviewImages, fun(img, idx, __index, _cached): Any {
                                                return _cE("image", _uM("key" to idx, "src" to img, "class" to "photo", "mode" to "aspectFill", "onClick" to fun(){
                                                    _ctx.preview(img, _ctx.reviewImages)
                                                }), null, 8, _uA(
                                                    "src",
                                                    "onClick"
                                                ))
                                            }), 128)
                                        ))
                                    ))
                                } else {
                                    _cC("v-if", true)
                                }
                            ))
                        } else {
                            _cC("v-if", true)
                        },
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "问题描述"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.problemDescription), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "专业类别"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.professionalTypeName), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "基本模块"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.moduleName), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "责任要素"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.responsibilityName), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "检查内容"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.issueName), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "性质界定"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.qualityName), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "扣分值"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.getDeductionScore(_ctx.detail!!.qualityName)), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "发现位置"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.location), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "受检单位"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.departmentName)), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "检查人"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.inspector), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "责任人"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.responsiblePerson), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "责任单位"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.responsibleUnit), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "整改期限"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.rectificationDeadline), 1)
                        ))
                    ))
                } else {
                    _cE("view", _uM("key" to 2, "class" to "empty"), _uA(
                        _cE("text", null, "记录不存在")
                    ))
                }
            }
        ), 4)
    }
    open var id: String by `$data`
    open var detail: MonthlyDetail? by `$data`
    open var loading: Boolean by `$data`
    open var containerHeight: Number by `$data`
    open var problemImages: UTSArray<String> by `$data`
    open var rectificationImages: UTSArray<String> by `$data`
    open var reviewImages: UTSArray<String> by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("id" to "", "detail" to null as MonthlyDetail?, "loading" to true, "containerHeight" to 0, "problemImages" to _uA<String>(), "rectificationImages" to _uA<String>(), "reviewImages" to _uA<String>())
    }
    open var initContainer = ::gen_initContainer_fn
    open fun gen_initContainer_fn() {
        uni_getSystemInfo(GetSystemInfoOptions(success = fun(res){
            this.containerHeight = res.windowHeight
        }
        ))
    }
    open var loadDetail = ::gen_loadDetail_fn
    open fun gen_loadDetail_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/" + this.id), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                val d = data.getJSON("data")
                if (d != null) {
                    this.detail = MonthlyDetail(id = d.getString("id") ?: "", recordStatus = d.getNumber("recordStatus") ?: 0, problemDescription = d.getString("problemDescription") ?: "", professionalTypeName = d.getString("professionalTypeName") ?: "", moduleName = d.getString("moduleName") ?: "", responsibilityName = d.getString("responsibilityName") ?: "", issueName = d.getString("issueName") ?: "", qualityName = d.getString("qualityName") ?: "", location = d.getString("location") ?: "", departmentName = d.getString("departmentName") ?: "", inspector = d.getString("inspector") ?: "", responsibleUnit = d.getString("responsibleUnit") ?: "", responsiblePerson = d.getString("responsiblePerson") ?: "", rectificationSuggestion = d.getString("rectificationSuggestion") ?: "", rectificationEffect = d.getString("rectificationEffect") ?: "", rectifierName = d.getString("rectifierName") ?: "", reviewSuggestion = d.getString("reviewSuggestion") ?: "", reviewEffect = d.getString("reviewEffect") ?: "", reviewerName = d.getString("reviewerName") ?: "", photos = d.getString("photos") ?: "", rectificationImages = d.getString("rectificationImages") ?: "", reviewImages = d.getString("reviewImages") ?: "", rectificationDeadline = d.getString("rectificationDeadline") ?: "")
                    this.problemImages = this.parseImages(this.detail!!.photos)
                    this.rectificationImages = this.parseImages(this.detail!!.rectificationImages)
                    this.reviewImages = this.parseImages(this.detail!!.reviewImages)
                }
            }
            this.loading = false
        }
        , fail = fun(_){
            this.loading = false
        }
        ))
    }
    open var parseImages = ::gen_parseImages_fn
    open fun gen_parseImages_fn(s: String): UTSArray<String> {
        if (s == null || s.trim() === "") {
            return _uA()
        }
        try {
            if (s.trim().startsWith("[") && s.trim().endsWith("]")) {
                val arr = UTSAndroid.consoleDebugError(JSON.parse(s), " at pages/monthly/view.uvue:240") as UTSArray<String>
                return arr.map(fun(u): String {
                    return this.toUrl(u)
                }
                ).filter(fun(u): Boolean {
                    return u !== ""
                }
                )
            }
        }
         catch (_: Throwable) {}
        return s.split(",").map(fun(u): String {
            return u.trim()
        }
        ).filter(fun(u): Boolean {
            return u !== ""
        }
        ).map(fun(u): String {
            return this.toUrl(u)
        }
        )
    }
    open var toUrl = ::gen_toUrl_fn
    open fun gen_toUrl_fn(p: String): String {
        if (p == null || p.trim() === "") {
            return ""
        }
        if (p.startsWith("http://") || p.startsWith("https://")) {
            return p
        }
        return "" + apiConfig.baseUrl + "/api" + p
    }
    open var getDeductionScore = ::gen_getDeductionScore_fn
    open fun gen_getDeductionScore_fn(qualityName: String): String {
        if (qualityName === "观察") {
            return "1"
        }
        if (qualityName === "一般") {
            return "3"
        }
        if (qualityName === "重要") {
            return "5"
        }
        return "-"
    }
    open var displayValue = ::gen_displayValue_fn
    open fun gen_displayValue_fn(v: String): String {
        return if (v !== "" && v != null) {
            v
        } else {
            "暂无"
        }
    }
    open var preview = ::gen_preview_fn
    open fun gen_preview_fn(current: String, urls: UTSArray<String>) {
        uni_previewImage(PreviewImageOptions(current = current, urls = urls))
    }
    open var getStatusText = ::gen_getStatusText_fn
    open fun gen_getStatusText_fn(s: Number): String {
        if (s === 0) {
            return "待整改"
        }
        if (s === 1) {
            return "已整改"
        }
        if (s === 2) {
            return "已复查"
        }
        return "未知"
    }
    open var getStatusClass = ::gen_getStatusClass_fn
    open fun gen_getStatusClass_fn(s: Number): String {
        if (s === 0) {
            return "status-pending"
        }
        if (s === 1) {
            return "status-completed"
        }
        if (s === 2) {
            return "status-reviewed"
        }
        return "status-unknown"
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("container" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#f1f5f9")), "loading" to _pS(_uM("paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "textAlign" to "center", "color" to "#475569", "fontSize" to "28rpx")), "content" to _pS(_uM("paddingTop" to "32rpx", "paddingRight" to "32rpx", "paddingBottom" to "32rpx", "paddingLeft" to "32rpx")), "header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to "24rpx", "paddingTop" to "32rpx", "paddingRight" to "32rpx", "paddingBottom" to "32rpx", "paddingLeft" to "32rpx", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "borderTopWidth" to "4rpx", "borderTopStyle" to "solid", "borderTopColor" to "#1e40af")), "title" to _pS(_uM("fontSize" to "36rpx", "fontWeight" to 600, "color" to "#1e40af")), "status" to _pS(_uM("paddingTop" to "8rpx", "paddingRight" to "20rpx", "paddingBottom" to "8rpx", "paddingLeft" to "20rpx", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "fontSize" to "26rpx", "fontWeight" to 500)), "status-pending" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#f59e0b", "color" to "#ffffff")), "status-completed" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#16a34a", "color" to "#ffffff")), "status-reviewed" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#2563eb", "color" to "#ffffff")), "status-unknown" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#94a3b8", "color" to "#ffffff")), "item" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "marginBottom" to "16rpx", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "borderLeftWidth" to "6rpx", "borderLeftStyle" to "solid", "borderLeftColor" to "#2563eb")), "label" to _pS(_uM("fontSize" to "28rpx", "color" to "#1e293b", "marginBottom" to "12rpx", "fontWeight" to 600)), "value" to _pS(_uM("fontSize" to "28rpx", "color" to "#0a2540", "fontWeight" to 600)), "photos" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "marginBottom" to "16rpx", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)")), "image-list" to _pS(_uM("display" to "flex", "flexWrap" to "wrap")), "photo" to _uM(".image-list " to _uM("marginTop" to "8rpx", "marginRight" to "8rpx", "marginBottom" to "8rpx", "marginLeft" to "8rpx"), "" to _uM("width" to "200rpx", "height" to "200rpx", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe")), "empty" to _pS(_uM("paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "textAlign" to "center", "color" to "#475569", "fontSize" to "28rpx")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
