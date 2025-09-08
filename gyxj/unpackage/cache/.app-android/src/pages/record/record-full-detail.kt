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
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfoSync as uni_getSystemInfoSync
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesRecordRecordFullDetail : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: OnLoadOptions) {
            this.checkLogin()
            if (options["id"] != null) {
                this.recordId = options["id"] as String
                this.loadRecordDetail()
            }
            val systemInfo = uni_getSystemInfoSync()
            this.containerHeight = systemInfo.windowHeight
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
                        _cE("view", _uM("class" to "page-header"), _uA(
                            _cE("text", _uM("class" to "page-title"), "巡检记录详情"),
                            _cE("view", _uM("class" to _nC(_uA(
                                "record-status",
                                _ctx.getStatusClass(_ctx.recordDetail?.status ?: 0)
                            ))), _uA(
                                _cE("text", null, _tD(_ctx.getStatusText(_ctx.recordDetail?.status ?: 0)), 1)
                            ), 2)
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("view", _uM("class" to "section-title"), _uA(
                                _cE("text", _uM("class" to "section-icon"), "📋"),
                                _cE("text", _uM("class" to "section-text"), "基本信息")
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "记录编号"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.recordNumber ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "问题描述"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.problemDescription ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "发生部位"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.location ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "创建时间"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.formatDateTime(_ctx.recordDetail?.createTime)), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "更新时间"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.formatDateTime(_ctx.recordDetail?.updateTime)), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("view", _uM("class" to "section-title"), _uA(
                                _cE("text", _uM("class" to "section-icon"), "👥"),
                                _cE("text", _uM("class" to "section-text"), "责任信息")
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "责任单位"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.responsibleUnit ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "责任人"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.responsiblePerson ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "所属部门"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.departmentName ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("view", _uM("class" to "section-title"), _uA(
                                _cE("text", _uM("class" to "section-icon"), "🔍"),
                                _cE("text", _uM("class" to "section-text"), "检查信息")
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "检查人"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.inspectorName ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "检查组组长"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.groupLeader ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "检查组副组长"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.deputyLeader ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "专业"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.professionName ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "复核人"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.reviewerName ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("view", _uM("class" to "section-title"), _uA(
                                _cE("text", _uM("class" to "section-icon"), "🏷️"),
                                _cE("text", _uM("class" to "section-text"), "问题分类")
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "性质界定"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.natureName ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "问题等级"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.getProblemLevelText(_ctx.recordDetail?.problemLevel)), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("view", _uM("class" to "section-title"), _uA(
                                _cE("text", _uM("class" to "section-icon"), "💰"),
                                _cE("text", _uM("class" to "section-text"), "考核信息")
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "考核情况"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.assessmentSituation ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "考核金额"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.assessmentAmount ?: "") + "元", 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "安全记分"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.safetyScore ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("view", _uM("class" to "section-title"), _uA(
                                _cE("text", _uM("class" to "section-icon"), "🔧"),
                                _cE("text", _uM("class" to "section-text"), "整改信息")
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "整改期限"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.formatDateTime(_ctx.recordDetail?.rectificationDeadline)), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "整改时间"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.formatDateTime(_ctx.recordDetail?.rectificationTime)), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "整改效果描述"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.rectificationEffect ?: ""), 1)
                            ))
                        )),
                        if (_ctx.problemImages.length > 0) {
                            _cE("view", _uM("key" to 0, "class" to "section"), _uA(
                                _cE("view", _uM("class" to "section-title"), _uA(
                                    _cE("text", _uM("class" to "section-icon"), "📷"),
                                    _cE("text", _uM("class" to "section-text"), "问题图片 (" + _tD(_ctx.problemImages.length) + "张)", 1)
                                )),
                                _cE("view", _uM("class" to "image-grid"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.problemImages, fun(image, index, __index, _cached): Any {
                                        return _cE("view", _uM("class" to "image-item", "key" to index), _uA(
                                            _cE("image", _uM("src" to image, "mode" to "aspectFill", "class" to "problem-image", "onClick" to fun(){
                                                _ctx.previewImage(index, _ctx.problemImages)
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
                        if (_ctx.rectificationImages.length > 0) {
                            _cE("view", _uM("key" to 1, "class" to "section"), _uA(
                                _cE("view", _uM("class" to "section-title"), _uA(
                                    _cE("text", _uM("class" to "section-icon"), "🔧"),
                                    _cE("text", _uM("class" to "section-text"), "整改图片 (" + _tD(_ctx.rectificationImages.length) + "张)", 1)
                                )),
                                _cE("view", _uM("class" to "image-grid"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectificationImages, fun(image, index, __index, _cached): Any {
                                        return _cE("view", _uM("class" to "image-item", "key" to index), _uA(
                                            _cE("image", _uM("src" to image, "mode" to "aspectFill", "class" to "rectification-image", "onClick" to fun(){
                                                _ctx.previewImage(index, _ctx.rectificationImages)
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
                            _cE("view", _uM("class" to "section-title"), _uA(
                                _cE("text", _uM("class" to "section-icon"), "⚙️"),
                                _cE("text", _uM("class" to "section-text"), "系统信息")
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "记录ID"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.id ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "创建人"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.createBy ?: ""), 1)
                            )),
                            _cE("view", _uM("class" to "info-item"), _uA(
                                _cE("text", _uM("class" to "info-label"), "更新人"),
                                _cE("text", _uM("class" to "info-value"), _tD(_ctx.recordDetail?.updateBy ?: ""), 1)
                            ))
                        ))
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
    open var recordDetail: InspectionRecordDetail? by `$data`
    open var loading: Boolean by `$data`
    open var recordId: String by `$data`
    open var scrollTop: Number by `$data`
    open var containerHeight: Number by `$data`
    open var problemImages: UTSArray<String> by `$data`
    open var rectificationImages: UTSArray<String> by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("recordDetail" to null as InspectionRecordDetail?, "loading" to false, "recordId" to "", "scrollTop" to 0, "containerHeight" to 0, "problemImages" to _uA<String>(), "rectificationImages" to _uA<String>())
    }
    open var checkLogin = ::gen_checkLogin_fn
    open fun gen_checkLogin_fn() {
        val tokenData = uni_getStorageSync("token")
        val token = if (tokenData != null) {
            tokenData as String
        } else {
            ""
        }
        if (token == null || token == "") {
            uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
        }
    }
    open var loadRecordDetail = ::gen_loadRecordDetail_fn
    open fun gen_loadRecordDetail_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend {
                this.loading = true
                val token = uni_getStorageSync("token")
                uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record/" + this.recordId), method = "GET", header = object : UTSJSONObject() {
                    var Authorization = "Bearer " + token
                }, success = fun(res){
                    val responseData = res.data as UTSJSONObject
                    if (res.statusCode === 200 && responseData.getNumber("code") === 200) {
                        val data = responseData.getJSON("data") as UTSJSONObject
                        if (data != null) {
                            this.recordDetail = InspectionRecordDetail(id = data.getString("id") ?: "", recordNumber = data.getString("recordNo") ?: "", problemDescription = data.getString("problemDescription") ?: "", location = data.getString("location") ?: "", responsibleUnit = data.getString("responsibleUnit") ?: "", responsiblePerson = data.getString("responsiblePerson") ?: "", inspectorName = data.getString("inspectorName") ?: data.getString("inspector") ?: "", groupLeader = data.getString("groupLeaderName") ?: "", deputyLeader = data.getString("deputyLeaderName") ?: "", professionName = data.getString("professionName") ?: "", reviewerName = data.getString("reviewerName") ?: "", departmentName = data.getString("departmentName") ?: "", natureName = data.getString("natureDefinition") ?: "", problemLevel = data.getNumber("problemLevel") ?: 0, assessmentSituation = data.getString("assessmentSituation") ?: "", assessmentAmount = data.getString("assessmentAmount") ?: "", safetyScore = data.getString("safetyScore") ?: "", rectificationDeadline = data.getString("rectificationDeadline") ?: "", rectificationTime = data.getString("rectificationTime") ?: "", rectificationEffect = data.getString("rectificationEffect") ?: "", status = data.getNumber("status") ?: 0, createTime = data.getString("createTime") ?: "", updateTime = data.getString("updateTime") ?: "", createBy = data.getString("createBy") ?: "", updateBy = data.getString("updateBy") ?: "", problemImages = data.getString("problemImageUrls") ?: "", rectificationImages = data.getString("rectificationImageUrls") ?: "")
                            this.processProblemImages()
                            this.processRectificationImages()
                        }
                    } else {
                        val errorMessage = responseData.getString("message") ?: ""
                        uni_showToast(ShowToastOptions(title = if (errorMessage != "") {
                            errorMessage
                        } else {
                            "加载失败"
                        }
                        , icon = "none"))
                    }
                    this.loading = false
                }
                , fail = fun(error){
                    console.error("加载记录详情失败:", error, " at pages/record/record-full-detail.uvue:363")
                    uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
                    this.loading = false
                }
                ))
        })
    }
    open var processProblemImages = ::gen_processProblemImages_fn
    open fun gen_processProblemImages_fn() {
        if (this.recordDetail?.problemImages != null && this.recordDetail!!.problemImages != "") {
            try {
                val images = UTSAndroid.consoleDebugError(JSON.parse(this.recordDetail!!.problemImages), " at pages/record/record-full-detail.uvue:376") as UTSArray<String>
                this.problemImages = images
            } catch (e: Throwable) {
                console.error("解析问题图片失败:", e, " at pages/record/record-full-detail.uvue:379")
                this.problemImages = _uA()
            }
        } else {
            this.problemImages = _uA()
        }
    }
    open var processRectificationImages = ::gen_processRectificationImages_fn
    open fun gen_processRectificationImages_fn() {
        if (this.recordDetail?.rectificationImages != null && this.recordDetail!!.rectificationImages != "") {
            try {
                val images = UTSAndroid.consoleDebugError(JSON.parse(this.recordDetail!!.rectificationImages), " at pages/record/record-full-detail.uvue:390") as UTSArray<String>
                this.rectificationImages = images
            } catch (e: Throwable) {
                console.error("解析整改图片失败:", e, " at pages/record/record-full-detail.uvue:393")
                this.rectificationImages = _uA()
            }
        } else {
            this.rectificationImages = _uA()
        }
    }
    open var getStatusText = ::gen_getStatusText_fn
    open fun gen_getStatusText_fn(status: Number?): String {
        if (status == null) {
            return "未知"
        }
        val statusMap: UTSJSONObject = object : UTSJSONObject(UTSSourceMapPosition("statusMap", "pages/record/record-full-detail.uvue", 403, 11)) {
            var `0` = "待整改"
            var `1` = "已整改"
            var `2` = "已复查"
        }
        val statusText = statusMap[status.toString(10)] as String
        return if (statusText != null) {
            statusText
        } else {
            "未知"
        }
    }
    open var getStatusClass = ::gen_getStatusClass_fn
    open fun gen_getStatusClass_fn(status: Number?): String {
        if (status == null) {
            return "status-unknown"
        }
        val classMap: UTSJSONObject = object : UTSJSONObject(UTSSourceMapPosition("classMap", "pages/record/record-full-detail.uvue", 414, 11)) {
            var `0` = "status-pending"
            var `1` = "status-completed"
            var `2` = "status-reviewed"
        }
        val statusClass = classMap[status.toString(10)] as String
        return if (statusClass != null) {
            statusClass
        } else {
            "status-unknown"
        }
    }
    open var getProblemLevelText = ::gen_getProblemLevelText_fn
    open fun gen_getProblemLevelText_fn(level: Number?): String {
        if (level == null) {
            return "未知等级"
        }
        val levelMap: UTSJSONObject = object : UTSJSONObject(UTSSourceMapPosition("levelMap", "pages/record/record-full-detail.uvue", 425, 11)) {
            var `1` = "一般问题"
            var `2` = "重要问题"
            var `3` = "严重问题"
        }
        val levelText = levelMap[level.toString(10)] as String
        return if (levelText != null) {
            levelText
        } else {
            "未知等级"
        }
    }
    open var formatDateTime = ::gen_formatDateTime_fn
    open fun gen_formatDateTime_fn(dateTimeStr: String?): String {
        if (dateTimeStr == null || dateTimeStr == "") {
            return ""
        }
        return dateTimeStr
    }
    open var previewImage = ::gen_previewImage_fn
    open fun gen_previewImage_fn(index: Number, images: UTSArray<String>) {
        uni_previewImage(PreviewImageOptions(current = index, urls = images))
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
                return _uM("container" to _pS(_uM("backgroundColor" to "#f5f5f5")), "loading-state" to _pS(_uM("display" to "flex", "justifyContent" to "center", "alignItems" to "center", "height" to 200)), "loading-text" to _pS(_uM("fontSize" to 16, "color" to "#666666")), "error-state" to _pS(_uM("display" to "flex", "justifyContent" to "center", "alignItems" to "center", "height" to 200)), "error-text" to _pS(_uM("fontSize" to 16, "color" to "#999999")), "detail-content" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20)), "page-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to 20, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "boxShadow" to "0 2px 8px rgba(0, 0, 0, 0.1)")), "page-title" to _pS(_uM("fontSize" to 20, "fontWeight" to "bold", "color" to "#333333")), "record-status" to _pS(_uM("paddingTop" to 6, "paddingRight" to 12, "paddingBottom" to 6, "paddingLeft" to 12, "borderTopLeftRadius" to 20, "borderTopRightRadius" to 20, "borderBottomRightRadius" to 20, "borderBottomLeftRadius" to 20, "fontSize" to 14)), "status-pending" to _pS(_uM("backgroundColor" to "#fff3cd", "color" to "#856404", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#ffeaa7", "borderRightColor" to "#ffeaa7", "borderBottomColor" to "#ffeaa7", "borderLeftColor" to "#ffeaa7")), "status-completed" to _pS(_uM("backgroundColor" to "#d1ecf1", "color" to "#0c5460", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#bee5eb", "borderRightColor" to "#bee5eb", "borderBottomColor" to "#bee5eb", "borderLeftColor" to "#bee5eb")), "status-reviewed" to _pS(_uM("backgroundColor" to "#d4edda", "color" to "#155724", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#c3e6cb", "borderRightColor" to "#c3e6cb", "borderBottomColor" to "#c3e6cb", "borderLeftColor" to "#c3e6cb")), "section" to _pS(_uM("marginBottom" to 20, "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "boxShadow" to "0 2px 8px rgba(0, 0, 0, 0.1)")), "section-title" to _pS(_uM("display" to "flex", "alignItems" to "center", "marginBottom" to 16, "paddingBottom" to 12, "borderBottomWidth" to 2, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0")), "section-icon" to _pS(_uM("fontSize" to 18, "marginRight" to 8)), "section-text" to _pS(_uM("fontSize" to 16, "color" to "#333333")), "info-item" to _pS(_uM("display" to "flex", "marginBottom" to 12, "paddingTop" to 8, "paddingRight" to 0, "paddingBottom" to 8, "paddingLeft" to 0, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f8f8f8", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000", "marginBottom:last-child" to 0)), "info-label" to _pS(_uM("width" to 100, "flexShrink" to 0, "fontSize" to 14, "color" to "#666666")), "info-value" to _pS(_uM("flex" to 1, "fontSize" to 14, "color" to "#333333", "wordWrap" to "break-word", "lineHeight" to 1.4)), "image-grid" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "10px")), "image-item" to _pS(_uM("height" to 100, "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "overflow" to "hidden", "boxShadow" to "0 2px 4px rgba(0, 0, 0, 0.1)")), "problem-image" to _pS(_uM("width" to "100%", "height" to "100%", "objectFit" to "cover")), "rectification-image" to _pS(_uM("width" to "100%", "height" to "100%", "objectFit" to "cover")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
