@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "SENSELESS_COMPARISON", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
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
import kotlin.properties.Delegates
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
            this.loadDetail().then(fun(){
                this.loadCurrentUser()
                this.loadTaskPhase()
            }
            )
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
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "style" to _nS(_uM("height" to (_ctx.containerHeight + "px"))), "enable-back-to-top" to "true"), _uA(
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
                                _cE("text", _uM("class" to "label"), "专业类别"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.professionalTypeName), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "基本模块"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.moduleName), 1)
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
                                _cE("text", _uM("class" to "label"), "性质界定"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.qualityName), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "性质界定是否人工修改"),
                                _cE("text", _uM("class" to "value"), _tD(if (_ctx.detail!!.natureModified === 1) {
                                    "是(已修改)"
                                } else {
                                    "否(未修改)"
                                }), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "扣分值"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.getDeductionScore(_ctx.detail!!.qualityName)), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "发现位置"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.location), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "检查单位"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.checkUnitName)), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "受检单位"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.departmentName)), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "检查人"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.inspector), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "责任人"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.responsiblePerson)), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "责任单位"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.responsibleUnit)), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "整改期限"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.rectificationDeadline), 1)
                            ))
                        )),
                        if (_ctx.problemImages.length > 0) {
                            _cE("view", _uM("key" to 0, "class" to "section"), _uA(
                                _cE("text", _uM("class" to "title"), "问题图片（" + _tD(_ctx.problemImages.length) + "张）", 1),
                                _cE("view", _uM("class" to "image-list"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.problemImages, fun(img, i, __index, _cached): Any {
                                        return _cE("view", _uM("class" to "image-item", "key" to i), _uA(
                                            _cE("image", _uM("src" to _ctx.convertToHttpUrl(img), "mode" to "aspectFill", "class" to "image", "onClick" to fun(){
                                                _ctx.previewProblemImage(i)
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
                            _cE("text", _uM("class" to "title"), "整改情况"),
                            _cE("view", _uM("class" to "readonly-field"), _uA(
                                _cE("text", _uM("class" to "label"), "整改建议："),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.rectificationSuggestion)), 1)
                            )),
                            _cE("view", _uM("class" to "readonly-field"), _uA(
                                _cE("text", _uM("class" to "label"), "整改效果："),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.rectificationEffect)), 1)
                            ))
                        )),
                        if (_ctx.rectImages.length > 0) {
                            _cE("view", _uM("key" to 1, "class" to "section"), _uA(
                                _cE("text", _uM("class" to "title"), "整改图片（" + _tD(_ctx.rectImages.length) + "张）", 1),
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
                                    }), 128)
                                ))
                            ))
                        } else {
                            _cC("v-if", true)
                        },
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("text", _uM("class" to "title"), "复查人"),
                            _cE("view", _uM("class" to "readonly-field"), _uA(
                                _cE("text", _uM("class" to "label"), "复查人："),
                                _cE("text", _uM("class" to "value"), _tD(if (_ctx.currentUserName !== "") {
                                    _ctx.currentUserName
                                } else {
                                    "当前登录用户"
                                }), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("text", _uM("class" to "title"), "复查建议"),
                            _cE("textarea", _uM("class" to "textarea", "modelValue" to _ctx.reviewSuggestion, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.reviewSuggestion = `$event`.detail.value
                            }, "placeholder" to "请填写复查建议/结论", "maxlength" to "500"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("text", _uM("class" to "title"), "复查效果"),
                            _cE("textarea", _uM("class" to "textarea", "modelValue" to _ctx.reviewEffect, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.reviewEffect = `$event`.detail.value
                            }, "placeholder" to "请填写复查实际效果", "maxlength" to "500"), null, 40, _uA(
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
                        if (isTrue(_ctx.isReviewVisible())) {
                            _cE("view", _uM("key" to 2, "class" to "footer"), _uA(
                                _cE("button", _uM("class" to "submit", "onClick" to _ctx.submitReview, "disabled" to _ctx.saving), _tD(if (_ctx.saving) {
                                    "提交中..."
                                } else {
                                    "确认复查"
                                }), 9, _uA(
                                    "onClick",
                                    "disabled"
                                ))
                            ))
                        } else {
                            _cE("view", _uM("key" to 3, "class" to "footer footer-locked"), _uA(
                                _cE("text", _uM("class" to "locked-text"), "当前部门类型无复查权限")
                            ))
                        }
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
    open var reviewEffect: String by `$data`
    open var reviewImages: UTSArray<String> by `$data`
    open var loading: Boolean by `$data`
    open var saving: Boolean by `$data`
    open var problemImages: UTSArray<String> by `$data`
    open var containerHeight: Number by `$data`
    open var currentUser: CurrentUser__2 by `$data`
    open var currentUserName: String by `$data`
    open var taskPhase: Number by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("id" to "", "detail" to null as MonthlyReviewDetail?, "rectImages" to _uA<String>(), "reviewSuggestion" to "", "reviewEffect" to "", "reviewImages" to _uA<String>(), "loading" to true, "saving" to false, "problemImages" to _uA<String>(), "containerHeight" to 0, "currentUser" to CurrentUser__2(deptId = "", deptName = "", deptType = null), "currentUserName" to "", "taskPhase" to 0)
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
    open var loadDetail = ::gen_loadDetail_fn
    open fun gen_loadDetail_fn(): UTSPromise<Unit> {
        return UTSPromise(fun(resolve, _reject){
            val token = uni_getStorageSync("token")
            uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/" + this.id), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
                val data = res.data as UTSJSONObject
                if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                    val d = data.getJSON("data") as UTSJSONObject?
                    if (d != null) {
                        this.detail = MonthlyReviewDetail(id = d!!!!.getString("id") ?: "", problemDescription = d!!!!.getString("problemDescription") ?: "", location = d!!!!.getString("location") ?: "", rectificationEffect = (d!!!!.getString("rectificationEffect") ?: ""), rectificationSuggestion = (d!!!!.getString("rectificationSuggestion") ?: ""), rectificationImages = d!!!!.getString("rectificationImages") ?: "", rectificationDeadline = d!!!!.getString("rectificationDeadline") ?: "", departmentName = d!!!!.getString("departmentName") ?: "", professionalTypeName = d!!!!.getString("professionalTypeName") ?: "", responsibilityName = d!!!!.getString("responsibilityName") ?: "", issueName = d!!!!.getString("issueName") ?: "", moduleName = d!!!!.getString("moduleName") ?: "", inspector = d!!!!.getString("inspector") ?: "", responsiblePerson = d!!!!.getString("responsiblePerson") ?: "", responsibleUnit = d!!!!.getString("responsibleUnit") ?: "", qualityName = d!!!!.getString("qualityName") ?: "", checkUnitName = d!!!!.getString("checkUnitName") ?: "", natureModified = d!!!!.getNumber("natureModified") ?: 0, photos = d!!!!.getString("photos") ?: "", taskId = d!!!!.getString("taskId") ?: "")
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
                resolve(Unit)
            }
            , fail = fun(_){
                this.loading = false
                uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
                resolve(Unit)
            }
            ))
        }
        )
    }
    open var loadCurrentUser = ::gen_loadCurrentUser_fn
    open fun gen_loadCurrentUser_fn() {
        resolveCurrentDept().then(fun(dept: CurrentDept){
            this.currentUser = CurrentUser__2(deptId = dept.deptId, deptName = dept.deptName, deptType = dept.deptType)
            val info = getCachedUserInfo()
            this.currentUserName = info.getString("realName") ?: info.getString("username") ?: ""
        }
        )
    }
    open var loadTaskPhase = ::gen_loadTaskPhase_fn
    open fun gen_loadTaskPhase_fn() {
        if (this.detail == null) {
            return
        }
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-task/" + (this.detail!!.taskId ?: "")), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode !== 200 || (data.getNumber("code") ?: 0) !== 200) {
                return
            }
            val t = data.getJSON("data")
            if (t == null) {
                return
            }
            val tObj = t as UTSJSONObject
            val pubStartStr = tObj.getString("publicityStartTime") ?: ""
            val pubEndStr = tObj.getString("publicityEndTime") ?: ""
            val now = Date()
            val parseDt = fun(s: String): Date? {
                if (s == null || s == "") {
                    return null
                }
                return Date(s.replace(" ", "T"))
            }
            val pubStart = parseDt(pubStartStr)
            val pubEnd = parseDt(pubEndStr)
            if (pubStart == null || pubEnd == null) {
                this.taskPhase = 0
                return
            }
            if (now < pubStart) {
                this.taskPhase = 2
            } else if (now <= pubEnd) {
                this.taskPhase = 3
            } else {
                this.taskPhase = 4
            }
        }
        ))
    }
    open var isReviewVisible = ::gen_isReviewVisible_fn
    open fun gen_isReviewVisible_fn(): Boolean {
        return this.currentUser.deptType !== 0
    }
    open var previewRect = ::gen_previewRect_fn
    open fun gen_previewRect_fn(i: Number) {
        uni_previewImage(PreviewImageOptions(current = i, urls = this.rectImages.map(fun(u): String {
            return this.convertToHttpUrl(u)
        }
        )))
    }
    open var previewProblemImage = ::gen_previewProblemImage_fn
    open fun gen_previewProblemImage_fn(i: Number) {
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
                uni_uploadFile(UploadFileOptions(url = getApiUrl("/upload/image"), filePath = p, name = "file", header = _uO("Authorization" to ("Bearer " + token)), success = fun(upRes){
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
            uni_showToast(ShowToastOptions(title = "请填写复查建议", icon = "none"))
            return
        }
        if (this.reviewEffect.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请填写复查效果", icon = "none"))
            return
        }
        this.saving = true
        val token = uni_getStorageSync("token")
        val data: UTSJSONObject = _uO("reviewSuggestion" to this.reviewSuggestion, "reviewEffect" to this.reviewEffect, "reviewImages" to this.reviewImages.join(","), "reviewerName" to this.currentUserName)
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/" + this.id + "/review"), method = "PUT", header = _uO("Authorization" to ("Bearer " + token), "Content-Type" to "application/json"), data = data, success = fun(res){
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
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("container" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#f1f5f9")), "loading-state" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "textAlign" to "center", "color" to "#475569")), "error" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "textAlign" to "center", "color" to "#475569")), "content" to _pS(_uM("paddingTop" to "24rpx", "paddingRight" to "32rpx", "paddingBottom" to "24rpx", "paddingLeft" to "32rpx")), "section" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "32rpx", "paddingRight" to "32rpx", "paddingBottom" to "32rpx", "paddingLeft" to "32rpx", "marginBottom" to "28rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "borderTopWidth" to "4rpx", "borderTopStyle" to "solid", "borderTopColor" to "#1e40af")), "title" to _pS(_uM("fontSize" to "32rpx", "fontWeight" to 600, "color" to "#1e40af", "marginBottom" to "24rpx", "paddingLeft" to "16rpx", "borderLeftWidth" to "6rpx", "borderLeftStyle" to "solid", "borderLeftColor" to "#2563eb")), "field" to _pS(_uM("marginBottom" to "20rpx", "display" to "flex", "justifyContent" to "space-between", "paddingTop" to "8rpx", "paddingRight" to 0, "paddingBottom" to "8rpx", "paddingLeft" to 0)), "label" to _uM("" to _uM("fontSize" to "28rpx", "color" to "#475569"), ".field-edit " to _uM("width" to "180rpx", "flexShrink" to 0, "color" to "#475569"), ".readonly-field " to _uM("minWidth" to "176rpx", "flexShrink" to 0, "color" to "#2563eb", "fontWeight" to 500)), "value" to _uM("" to _uM("fontSize" to "28rpx", "color" to "#0f172a", "textAlign" to "right", "maxWidth" to 480, "fontWeight" to 500), ".readonly-field " to _uM("textAlign" to "left", "flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "wordBreak" to "break-all", "color" to "#0f172a")), "field-edit" to _pS(_uM("marginBottom" to "20rpx", "display" to "flex", "alignItems" to "center", "paddingTop" to "8rpx", "paddingRight" to 0, "paddingBottom" to "8rpx", "paddingLeft" to 0)), "readonly-field" to _pS(_uM("marginBottom" to "20rpx", "display" to "flex", "alignItems" to "flex-start", "paddingTop" to "12rpx", "paddingRight" to "12rpx", "paddingBottom" to "12rpx", "paddingLeft" to "12rpx", "backgroundImage" to "none", "backgroundColor" to "#eff6ff", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx")), "textarea" to _pS(_uM("width" to "100%", "minHeight" to "200rpx", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "fontSize" to "28rpx", "color" to "#0f172a", "borderTopColor:focus" to "#2563eb", "borderRightColor:focus" to "#2563eb", "borderBottomColor:focus" to "#2563eb", "borderLeftColor:focus" to "#2563eb", "backgroundImage:focus" to "none", "backgroundColor:focus" to "#eff6ff")), "textarea-mini" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "minHeight" to "80rpx", "paddingTop" to "16rpx", "paddingRight" to "20rpx", "paddingBottom" to "16rpx", "paddingLeft" to "20rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "fontSize" to "28rpx", "color" to "#0f172a", "borderTopColor:focus" to "#2563eb", "borderRightColor:focus" to "#2563eb", "borderBottomColor:focus" to "#2563eb", "borderLeftColor:focus" to "#2563eb", "backgroundImage:focus" to "none", "backgroundColor:focus" to "#eff6ff")), "footer-locked" to _pS(_uM("textAlign" to "center", "paddingTop" to "40rpx", "paddingRight" to "30rpx", "paddingBottom" to "40rpx", "paddingLeft" to "30rpx")), "locked-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#94a3b8")), "image-list" to _pS(_uM("display" to "flex", "flexWrap" to "wrap")), "image-item" to _uM(".image-list " to _uM("marginTop" to "10rpx", "marginRight" to "10rpx", "marginBottom" to "10rpx", "marginLeft" to "10rpx"), "" to _uM("position" to "relative", "width" to "200rpx", "height" to "200rpx")), "add" to _uM(".image-list " to _uM("marginTop" to "10rpx", "marginRight" to "10rpx", "marginBottom" to "10rpx", "marginLeft" to "10rpx"), "" to _uM("width" to "200rpx", "height" to "200rpx", "borderTopWidth" to "4rpx", "borderRightWidth" to "4rpx", "borderBottomWidth" to "4rpx", "borderLeftWidth" to "4rpx", "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#2563eb", "borderRightColor" to "#2563eb", "borderBottomColor" to "#2563eb", "borderLeftColor" to "#2563eb", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "#eff6ff")), "no-image" to _uM(".image-list " to _uM("marginTop" to "10rpx", "marginRight" to "10rpx", "marginBottom" to "10rpx", "marginLeft" to "10rpx"), "" to _uM("fontSize" to "24rpx", "color" to "#94a3b8")), "image" to _pS(_uM("width" to "100%", "height" to "100%", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe")), "delete" to _pS(_uM("position" to "absolute", "top" to "-12rpx", "right" to "-12rpx", "width" to "44rpx", "height" to "44rpx", "backgroundImage" to "none", "backgroundColor" to "#dc2626", "borderTopLeftRadius" to "50%", "borderTopRightRadius" to "50%", "borderBottomRightRadius" to "50%", "borderBottomLeftRadius" to "50%", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "color" to "#ffffff", "fontWeight" to 600)), "add-icon" to _pS(_uM("fontSize" to "44rpx", "color" to "#2563eb", "fontWeight" to 600)), "add-text" to _pS(_uM("fontSize" to "24rpx", "color" to "#2563eb", "marginTop" to "8rpx")), "footer" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)")), "submit" to _pS(_uM("width" to "100%", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "backgroundImage" to "none", "backgroundColor" to "#1e40af", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "fontSize" to "32rpx", "fontWeight" to 600, "color::after" to "#ffffff", "borderTopWidth::after" to "medium", "borderRightWidth::after" to "medium", "borderBottomWidth::after" to "medium", "borderLeftWidth::after" to "medium", "borderTopStyle::after" to "none", "borderRightStyle::after" to "none", "borderBottomStyle::after" to "none", "borderLeftStyle::after" to "none", "borderTopColor::after" to "#000000", "borderRightColor::after" to "#000000", "borderBottomColor::after" to "#000000", "borderLeftColor::after" to "#000000")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
