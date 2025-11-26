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
import io.dcloud.uniapp.extapi.chooseImage as uni_chooseImage
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfo as uni_getSystemInfo
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.uploadFile as uni_uploadFile
open class GenPagesMonthlyReview : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: UTSJSONObject) {
            this.id = options.getString("id") ?: ""
            this.loadDetail()
        }
        , __ins)
        onReady(fun() {
            uni_getSystemInfo(GetSystemInfoOptions(success = fun(res){
                this.containerHeight = res.windowHeight
            }
            ))
        }
        , __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "style" to _nS(_uM("height" to (_ctx.containerHeight + "px")))), _uA(
            if (isTrue(_ctx.loading)) {
                _cE("view", _uM("key" to 0, "class" to "loading-state"), _uA(
                    _cE("text", null, "加载中...")
                ))
            } else {
                if (_ctx.detail != null) {
                    _cE("view", _uM("key" to 1, "class" to "content"), _uA(
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("text", _uM("class" to "title"), "问题信息"),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "问题描述"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.problemDescription), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "发生部位"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.location), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "任务编号"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.taskNo), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "月检时间"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.inspectionTime), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "所属部门"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.departmentName), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "检查专业"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.professionalTypeName), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "责任要素"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.responsibilityName), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "检查内容"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.issueName), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "检查人"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.inspector), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "检查组长"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.groupLeaderName), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "检查副组长"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.deputyLeaderName), 1)
                            ))
                        )),
                        if (_ctx.problemImages.length > 0) {
                            _cE("view", _uM("key" to 0, "class" to "section"), _uA(
                                _cE("text", _uM("class" to "title"), "问题图片"),
                                _cE("view", _uM("class" to "image-list"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.problemImages, fun(img, i, __index, _cached): Any {
                                        return _cE("view", _uM("class" to "image-item", "key" to i), _uA(
                                            _cE("image", _uM("src" to _ctx.convertToHttpUrl(img), "mode" to "aspectFill", "class" to "image", "onClick" to fun(){
                                                _ctx.previewProblem(i)
                                            }), null, 8, _uA(
                                                "src",
                                                "onClick"
                                            ))
                                        ))
                                    }), 128)
                                ))
                            ))
                        } else {
                            _cC("v-if", true)
                        },
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("text", _uM("class" to "title"), "整改效果"),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "整改效果"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.rectificationEffect), 1)
                            )),
                            _cE("view", _uM("class" to "image-list"), _uA(
                                _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectImages, fun(img, i, __index, _cached): Any {
                                    return _cE("view", _uM("class" to "image-item", "key" to i), _uA(
                                        _cE("image", _uM("src" to _ctx.convertToHttpUrl(img), "mode" to "aspectFill", "class" to "image", "onClick" to fun(){
                                            _ctx.previewRect(i)
                                        }), null, 8, _uA(
                                            "src",
                                            "onClick"
                                        ))
                                    ))
                                }), 128),
                                if (_ctx.rectImages.length == 0) {
                                    _cE("view", _uM("key" to 0, "class" to "no-image"), _uA(
                                        _cE("text", null, "暂无整改图片")
                                    ))
                                } else {
                                    _cC("v-if", true)
                                }
                            ))
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("text", _uM("class" to "title"), "复查效果"),
                            _cE("textarea", _uM("class" to "textarea", "modelValue" to _ctx.reviewSuggestion, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.reviewSuggestion = `$event`.detail.value
                            }, "placeholder" to "请填写复查效果", "maxlength" to "500"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("text", _uM("class" to "title"), "复查图片（最多3张，已选" + _tD(_ctx.reviewImages.length) + "张）", 1),
                            _cE("view", _uM("class" to "image-list"), _uA(
                                _cE(Fragment, null, RenderHelpers.renderList(_ctx.reviewImages, fun(img, i, __index, _cached): Any {
                                    return _cE("view", _uM("class" to "image-item", "key" to i), _uA(
                                        _cE("image", _uM("src" to _ctx.convertToHttpUrl(img), "mode" to "aspectFill", "class" to "image", "onClick" to fun(){
                                            _ctx.previewReview(i)
                                        }), null, 8, _uA(
                                            "src",
                                            "onClick"
                                        )),
                                        _cE("view", _uM("class" to "delete", "onClick" to fun(){
                                            _ctx.removeReview(i)
                                        }), _uA(
                                            _cE("text", null, "×")
                                        ), 8, _uA(
                                            "onClick"
                                        ))
                                    ))
                                }), 128),
                                if (_ctx.reviewImages.length < 3) {
                                    _cE("view", _uM("key" to 0, "class" to "add", "onClick" to _ctx.chooseReview), _uA(
                                        _cE("text", _uM("class" to "add-icon"), "+"),
                                        _cE("text", _uM("class" to "add-text"), "添加图片")
                                    ), 8, _uA(
                                        "onClick"
                                    ))
                                } else {
                                    _cC("v-if", true)
                                }
                            ))
                        )),
                        _cE("view", _uM("class" to "footer"), _uA(
                            _cE("button", _uM("class" to "submit", "onClick" to _ctx.submitReview, "disabled" to _ctx.saving), _tD(if (_ctx.saving) {
                                "提交中..."
                            } else {
                                "确认复查"
                            }), 9, _uA(
                                "onClick",
                                "disabled"
                            ))
                        ))
                    ))
                } else {
                    _cE("view", _uM("key" to 2, "class" to "error"), _uA(
                        _cE("text", null, "记录不存在或已删除")
                    ))
                }
            }
        ), 4)
    }
    open var id: String by `$data`
    open var detail: MonthlyReviewDetail? by `$data`
    open var rectImages: UTSArray<String> by `$data`
    open var reviewSuggestion: String by `$data`
    open var reviewImages: UTSArray<String> by `$data`
    open var loading: Boolean by `$data`
    open var saving: Boolean by `$data`
    open var problemImages: UTSArray<String> by `$data`
    open var containerHeight: Number by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("id" to "", "detail" to null as MonthlyReviewDetail?, "rectImages" to _uA<String>(), "reviewSuggestion" to "", "reviewImages" to _uA<String>(), "loading" to true, "saving" to false, "problemImages" to _uA<String>(), "containerHeight" to 0)
    }
    open var convertToHttpUrl = ::gen_convertToHttpUrl_fn
    open fun gen_convertToHttpUrl_fn(path: String): String {
        if (path == null || path == "") {
            return ""
        }
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path
        }
        return "" + apiConfig.baseUrl + "/api" + path
    }
    open var loadDetail = ::gen_loadDetail_fn
    open fun gen_loadDetail_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/" + this.id), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                val d = data.getJSON("data") as UTSJSONObject?
                if (d != null) {
                    this.detail = MonthlyReviewDetail(id = d!!!!.getString("id") ?: "", problemDescription = d!!!!.getString("problemDescription") ?: "", location = d!!!!.getString("location") ?: "", rectificationEffect = (d!!!!.getString("rectificationEffect") ?: d!!!!.getString("rectificationSuggestion") ?: ""), rectificationImages = d!!!!.getString("rectificationImages") ?: "", taskNo = d!!!!.getString("taskNo") ?: "", inspectionTime = d!!!!.getString("inspectionTime") ?: "", departmentName = d!!!!.getString("departmentName") ?: "", professionalTypeName = d!!!!.getString("professionalTypeName") ?: "", responsibilityName = d!!!!.getString("responsibilityName") ?: "", issueName = d!!!!.getString("issueName") ?: "", inspector = d!!!!.getString("inspector") ?: "", groupLeaderName = d!!!!.getString("groupLeaderName") ?: "", deputyLeaderName = d!!!!.getString("deputyLeaderName") ?: "", photos = d!!!!.getString("photos") ?: "")
                    if (this.detail!!.photos != null && this.detail!!.photos != "") {
                        val p = this.detail!!.photos
                        if (p.trim().startsWith("[") && p.trim().endsWith("]")) {
                            try {
                                this.problemImages = (JSON.parse(p) as UTSArray<String>)
                            } catch (_: Throwable) {}
                        } else {
                            this.problemImages = p.split(",").map(fun(u): String {
                                return u.trim()
                            }
                            ).filter(fun(u): Boolean {
                                return u != ""
                            }
                            )
                        }
                    }
                    val imgs = this.detail!!.rectificationImages
                    if (imgs != null && imgs != "") {
                        if (imgs.trim().startsWith("[") && imgs.trim().endsWith("]")) {
                            try {
                                this.rectImages = (JSON.parse(imgs) as UTSArray<String>)
                            } catch (_: Throwable) {}
                        } else {
                            this.rectImages = imgs.split(",").map(fun(u): String {
                                return u.trim()
                            }
                            ).filter(fun(u): Boolean {
                                return u != ""
                            }
                            )
                        }
                    }
                }
            } else {
                uni_showToast(ShowToastOptions(title = data.getString("message") ?: "加载失败", icon = "none"))
            }
            this.loading = false
        }
        , fail = fun(_){
            this.loading = false
            uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
        }
        ))
    }
    open var previewRect = ::gen_previewRect_fn
    open fun gen_previewRect_fn(i: Number) {
        uni_previewImage(PreviewImageOptions(current = i, urls = this.rectImages.map(fun(u): String {
            return this.convertToHttpUrl(u)
        }
        )))
    }
    open var previewProblem = ::gen_previewProblem_fn
    open fun gen_previewProblem_fn(i: Number) {
        uni_previewImage(PreviewImageOptions(current = i, urls = this.problemImages.map(fun(u): String {
            return this.convertToHttpUrl(u)
        }
        )))
    }
    open var chooseReview = ::gen_chooseReview_fn
    open fun gen_chooseReview_fn() {
        if (this.reviewImages.length >= 3) {
            uni_showToast(ShowToastOptions(title = "最多3张", icon = "none"))
            return
        }
        uni_chooseImage(ChooseImageOptions(count = 3 - this.reviewImages.length, sourceType = _uA(
            "camera",
            "album"
        ), success = fun(res){
            val token = uni_getStorageSync("token")
            res.tempFilePaths.forEach(fun(p){
                uni_uploadFile(UploadFileOptions(url = getApiUrl("/upload/image"), filePath = p, name = "file", header = object : UTSJSONObject() {
                    var Authorization = "Bearer " + token
                }, success = fun(upRes){
                    try {
                        val result = JSON.parse(upRes.data) as UTSJSONObject
                        if (upRes.statusCode === 200 && (result.getNumber("code") ?: 0) === 200) {
                            val dataObj = result.getJSON("data")
                            val url = dataObj?.getString("filePath") ?: ""
                            if (url != "") {
                                this.reviewImages.push(url)
                            }
                        } else {
                            uni_showToast(ShowToastOptions(title = result.getString("message") ?: "上传失败", icon = "none"))
                        }
                    }
                     catch (_: Throwable) {
                        uni_showToast(ShowToastOptions(title = "上传失败", icon = "none"))
                    }
                }
                , fail = fun(_){
                    uni_showToast(ShowToastOptions(title = "上传失败", icon = "none"))
                }
                ))
            }
            )
        }
        ))
    }
    open var previewReview = ::gen_previewReview_fn
    open fun gen_previewReview_fn(i: Number) {
        uni_previewImage(PreviewImageOptions(current = i, urls = this.reviewImages.map(fun(u): String {
            return this.convertToHttpUrl(u)
        }
        )))
    }
    open var removeReview = ::gen_removeReview_fn
    open fun gen_removeReview_fn(i: Number) {
        this.reviewImages.splice(i, 1)
    }
    open var submitReview = ::gen_submitReview_fn
    open fun gen_submitReview_fn() {
        if (this.saving) {
            return
        }
        if (this.reviewSuggestion.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请填写复查效果", icon = "none"))
            return
        }
        this.saving = true
        val token = uni_getStorageSync("token")
        val data: UTSJSONObject = _uO("reviewSuggestion" to this.reviewSuggestion, "reviewImages" to this.reviewImages.join(","))
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/" + this.id + "/review"), method = "PUT", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
            var `Content-Type` = "application/json"
        }, data = data, success = fun(res){
            val resp = res.data as UTSJSONObject
            if (res.statusCode === 200 && (resp.getNumber("code") ?: 0) === 200) {
                uni_showToast(ShowToastOptions(title = "复查确认成功", icon = "success"))
                setTimeout(fun() {
                    uni_navigateBack(null)
                }, 1200)
            } else {
                uni_showToast(ShowToastOptions(title = resp.getString("message") ?: "提交失败", icon = "none"))
            }
            this.saving = false
        }
        , fail = fun(_){
            this.saving = false
            uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
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
                return _uM("container" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#f5f5f5")), "loading-state" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "textAlign" to "center", "color" to "#666666")), "error" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "textAlign" to "center", "color" to "#666666")), "content" to _pS(_uM("paddingTop" to 12, "paddingRight" to 16, "paddingBottom" to 12, "paddingLeft" to 16)), "section" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 10, "borderTopRightRadius" to 10, "borderBottomRightRadius" to 10, "borderBottomLeftRadius" to 10, "paddingTop" to 16, "paddingRight" to 16, "paddingBottom" to 16, "paddingLeft" to 16, "marginBottom" to 14, "boxShadow" to "0 2px 8px rgba(0,0,0,0.06)")), "title" to _pS(_uM("fontSize" to 16, "color" to "#303133", "marginBottom" to 12)), "field" to _pS(_uM("marginBottom" to 10, "display" to "flex", "justifyContent" to "space-between")), "label" to _pS(_uM("fontSize" to 14, "color" to "#666666")), "value" to _pS(_uM("fontSize" to 14, "color" to "#333333", "textAlign" to "right")), "textarea" to _pS(_uM("width" to "100%", "minHeight" to 100, "paddingTop" to 12, "paddingRight" to 12, "paddingBottom" to 12, "paddingLeft" to 12, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e0e0e0", "borderRightColor" to "#e0e0e0", "borderBottomColor" to "#e0e0e0", "borderLeftColor" to "#e0e0e0", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "backgroundImage" to "none", "backgroundColor" to "#ffffff", "fontSize" to 14)), "image-list" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "10px")), "image-item" to _pS(_uM("position" to "relative", "width" to 100, "height" to 100)), "image" to _pS(_uM("width" to "100%", "height" to "100%", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e0e0e0", "borderRightColor" to "#e0e0e0", "borderBottomColor" to "#e0e0e0", "borderLeftColor" to "#e0e0e0")), "delete" to _pS(_uM("position" to "absolute", "top" to -6, "right" to -6, "width" to 22, "height" to 22, "backgroundImage" to "none", "backgroundColor" to "#ff4757", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "color" to "#ffffff")), "add" to _pS(_uM("width" to 100, "height" to 100, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#cccccc", "borderRightColor" to "#cccccc", "borderBottomColor" to "#cccccc", "borderLeftColor" to "#cccccc", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "#fafafa")), "add-icon" to _pS(_uM("fontSize" to 22, "color" to "#999999")), "add-text" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "no-image" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "footer" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "paddingTop" to 15, "paddingRight" to 15, "paddingBottom" to 15, "paddingLeft" to 15, "borderTopLeftRadius" to 10, "borderTopRightRadius" to 10, "borderBottomRightRadius" to 10, "borderBottomLeftRadius" to 10, "boxShadow" to "0 2px 8px rgba(0,0,0,0.06)")), "submit" to _pS(_uM("width" to "100%", "paddingTop" to 12, "paddingRight" to 12, "paddingBottom" to 12, "paddingLeft" to 12, "backgroundImage" to "none", "backgroundColor" to "#007AFF", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "fontSize" to 16)))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
