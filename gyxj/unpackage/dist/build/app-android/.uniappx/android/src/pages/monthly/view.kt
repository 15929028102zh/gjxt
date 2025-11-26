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
                                _cE("text", _uM("class" to "label"), "问题图片"),
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
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "整改效果"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.rectificationEffect), 1)
                        )),
                        if (_ctx.rectificationImages.length > 0) {
                            _cE("view", _uM("key" to 1, "class" to "photos"), _uA(
                                _cE("text", _uM("class" to "label"), "整改图片"),
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
                        },
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "复查效果"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.reviewEffect), 1)
                        )),
                        if (_ctx.reviewImages.length > 0) {
                            _cE("view", _uM("key" to 2, "class" to "photos"), _uA(
                                _cE("text", _uM("class" to "label"), "复查图片"),
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
                        },
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "问题描述"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.problemDescription), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "任务编号"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.taskNo), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "月检时间"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.inspectionTime), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "模块"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.moduleName), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "所属部门"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.departmentName), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "检查专业"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.professionalTypeName), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "检查组长"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.groupLeaderName), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "检查副组长"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.deputyLeaderName), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "责任单位"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.responsibleUnit), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "责任人"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.responsiblePerson), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "性质界定"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.qualityName), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "安全记分"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.safetyScore), 1)
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
                            _cE("text", _uM("class" to "label"), "发生部位"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.location), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "整改期限"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.rectificationDeadline), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "检查人"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.inspector), 1)
                        )),
                        _cE("view", _uM("class" to "item"), _uA(
                            _cE("text", _uM("class" to "label"), "创建时间"),
                            _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.createTime), 1)
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
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/" + this.id), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                val d = data.getJSON("data")
                if (d != null) {
                    this.detail = MonthlyDetail(id = d.getString("id") ?: "", recordStatus = d.getNumber("recordStatus") ?: 0, problemDescription = d.getString("problemDescription") ?: "", responsibleUnit = d.getString("responsibleUnit") ?: "", responsiblePerson = d.getString("responsiblePerson") ?: "", qualityName = d.getString("qualityName") ?: "", safetyScore = d.getNumber("safetyScore") ?: 0, location = d.getString("location") ?: "", inspector = d.getString("inspector") ?: "", rectificationEffect = (d.getString("rectificationEffect") ?: d.getString("rectificationSuggestion") ?: ""), reviewEffect = (d.getString("reviewEffect") ?: d.getString("reviewSuggestion") ?: ""), photos = d.getString("photos") ?: "", rectificationImages = d.getString("rectificationImages") ?: "", reviewImages = d.getString("reviewImages") ?: "", taskNo = d.getString("taskNo") ?: "", inspectionTime = d.getString("inspectionTime") ?: "", departmentName = d.getString("departmentName") ?: "", professionalTypeName = d.getString("professionalTypeName") ?: "", groupLeaderName = d.getString("groupLeaderName") ?: "", deputyLeaderName = d.getString("deputyLeaderName") ?: "", responsibilityName = d.getString("responsibilityName") ?: "", issueName = d.getString("issueName") ?: "", moduleName = d.getString("moduleName") ?: "", rectificationDeadline = d.getString("rectificationDeadline") ?: "", createTime = d.getString("createTime") ?: "")
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
                val arr = JSON.parse(s) as UTSArray<String>
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
            ), _uA(
                GenApp.styles
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("container" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#f5f5f5")), "loading" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "textAlign" to "center")), "content" to _pS(_uM("paddingTop" to 16, "paddingRight" to 16, "paddingBottom" to 16, "paddingLeft" to 16)), "header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to 12)), "title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold")), "status" to _pS(_uM("paddingTop" to 4, "paddingRight" to 8, "paddingBottom" to 4, "paddingLeft" to 8, "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4)), "status-pending" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#fff7e6", "color" to "#d48806")), "status-completed" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#f6ffed", "color" to "#389e0d")), "status-reviewed" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#e6f7ff", "color" to "#096dd9")), "status-unknown" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#f0f0f0", "color" to "#666666")), "item" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "marginBottom" to 8, "paddingTop" to 12, "paddingRight" to 12, "paddingBottom" to 12, "paddingLeft" to 12, "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8)), "label" to _pS(_uM("fontSize" to 14, "color" to "#666666", "marginBottom" to 6)), "value" to _pS(_uM("fontSize" to 14, "color" to "#333333")), "photos" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "marginBottom" to 8, "paddingTop" to 12, "paddingRight" to 12, "paddingBottom" to 12, "paddingLeft" to 12, "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8)), "image-list" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "8px")), "photo" to _pS(_uM("width" to 100, "height" to 100, "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6)))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
