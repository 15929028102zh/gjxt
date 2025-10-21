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
import io.dcloud.uniapp.extapi.`$off` as uni__off
import io.dcloud.uniapp.extapi.`$on` as uni__on
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfo as uni_getSystemInfo
import io.dcloud.uniapp.extapi.hideLoading as uni_hideLoading
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading
import io.dcloud.uniapp.extapi.showModal as uni_showModal
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.stopPullDownRefresh as uni_stopPullDownRefresh
open class GenPagesRecordRecord : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: OnLoadOptions) {
            this.checkLogin()
            if (options["status"] != null) {
                this.status = options["status"] as String
            }
            if (options["delayed"] != null && options["delayed"] == "true") {
                this.isDelayed = true
            }
            if (options["type"] != null && options["type"] == "reviewed") {
                this.isReviewed = true
                this.status = "2"
            }
            this.initContainerHeight()
            this.loadRecords()
            uni__on("recordUpdated", this.handleRecordUpdated)
        }
        , __ins)
        onPageShow(fun() {
            this.refreshRecords()
        }
        , __ins)
        onUnload(fun() {
            uni__off("recordUpdated", this.handleRecordUpdated)
        }
        , __ins)
        onPullDownRefresh(fun() {
            this.refreshRecords()
        }
        , __ins)
        onReachBottom(fun() {
            if (this.hasMore && !this.loadingMore) {
                this.loadMore()
            }
        }
        , __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "style" to _nS(_uM("height" to (_ctx.containerHeight + "px"))), "enable-back-to-top" to "true", "refresher-enabled" to "true", "refresher-triggered" to _ctx.isRefreshing, "refresher-default-style" to "black", "refresher-background" to "#f5f5f5", "onRefresherrefresh" to _ctx.onRefresh, "onRefresherrestore" to _ctx.onRefreshRestore), _uA(
            _cE("view", _uM("class" to "header"), _uA(
                _cE("text", _uM("class" to "title"), "问题列表"),
                _cE("text", _uM("class" to "subtitle"), "共 " + _tD(_ctx.totalCount) + " 条" + _tD(_ctx.getStatusTitle()) + "问题", 1)
            )),
            _cE(Fragment, null, RenderHelpers.renderList(_ctx.recordList, fun(record, __key, __index, _cached): Any {
                return _cE("view", _uM("class" to "record-item", "key" to record.id, "onClick" to fun(){
                    _ctx.goToDetail(record)
                }
                , "onLongpress" to fun(){
                    _ctx.showDeleteConfirm(record.id, record.problemDescription)
                }
                ), _uA(
                    _cE("view", _uM("class" to "record-content"), _uA(
                        _cE("view", _uM("class" to "left-content"), _uA(
                            _cE("view", _uM("class" to "name-problem-row"), _uA(
                                _cE("text", _uM("class" to "responsible-name"), _tD(record.responsiblePerson), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "right-info"), _uA(
                            _cE("text", _uM("class" to "inspector-info"), "检查人：" + _tD(record.inspectorName), 1),
                            _cE("text", _uM("class" to "date-text"), _tD(record.createDate), 1),
                            _cE("view", _uM("class" to _nC(_uA(
                                "status-tag",
                                _ctx.getStatusClass(record.status)
                            ))), _uA(
                                _cE("text", _uM("class" to "status-text"), _tD(_ctx.getStatusText(record.status)), 1)
                            ), 2)
                        )),
                        _cE("text", _uM("class" to "problem-text"), _tD(record.problemDescription), 1)
                    ))
                ), 40, _uA(
                    "onClick",
                    "onLongpress"
                ))
            }
            ), 128),
            if (isTrue(_ctx.recordList.length === 0 && !_ctx.loading)) {
                _cE("view", _uM("key" to 0, "class" to "empty-state"), _uA(
                    _cE("text", _uM("class" to "empty-icon"), "📝"),
                    _cE("text", _uM("class" to "empty-text"), "暂无记录")
                ))
            } else {
                _cC("v-if", true)
            }
            ,
            if (isTrue(_ctx.loading)) {
                _cE("view", _uM("key" to 1, "class" to "loading-state"), _uA(
                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                ))
            } else {
                _cC("v-if", true)
            }
            ,
            if (isTrue(_ctx.hasMore && _ctx.recordList.length > 0)) {
                _cE("view", _uM("key" to 2, "class" to "load-more", "onClick" to _ctx.loadMore), _uA(
                    _cE("text", _uM("class" to "load-more-text"), _tD(if (_ctx.loadingMore) {
                        "加载中..."
                    } else {
                        "加载更多"
                    }), 1)
                ), 8, _uA(
                    "onClick"
                ))
            } else {
                _cC("v-if", true)
            }
        ), 44, _uA(
            "refresher-triggered",
            "onRefresherrefresh",
            "onRefresherrestore"
        ))
    }
    open var recordList: UTSArray<InspectionRecord> by `$data`
    open var loading: Boolean by `$data`
    open var loadingMore: Boolean by `$data`
    open var hasMore: Boolean by `$data`
    open var totalCount: Number by `$data`
    open var pageNum: Number by `$data`
    open var pageSize: Number by `$data`
    open var status: String by `$data`
    open var isDelayed: Boolean by `$data`
    open var isReviewed: Boolean by `$data`
    open var isRefreshing: Boolean by `$data`
    open var containerHeight: Number by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("recordList" to _uA<InspectionRecord>(), "loading" to false, "loadingMore" to false, "hasMore" to true, "totalCount" to 0, "pageNum" to 1, "pageSize" to 20, "status" to "0", "isDelayed" to false, "isReviewed" to false, "isRefreshing" to false, "containerHeight" to 0)
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
    open var handleRecordUpdated = ::gen_handleRecordUpdated_fn
    open fun gen_handleRecordUpdated_fn() {
        console.log("接收到记录更新事件，开始刷新数据", " at pages/record/record.uvue:146")
        this.refreshRecords()
    }
    open var initContainerHeight = ::gen_initContainerHeight_fn
    open fun gen_initContainerHeight_fn() {
        uni_getSystemInfo(GetSystemInfoOptions(success = fun(res){
            this.containerHeight = res.windowHeight
        }
        ))
    }
    open var loadRecords = ::gen_loadRecords_fn
    open fun gen_loadRecords_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend {
                if (this.pageNum === 1) {
                    this.loading = true
                } else {
                    this.loadingMore = true
                }
                val token = uni_getStorageSync("token")
                var departmentId = ""
                val userInfoData = uni_getStorageSync("userInfo")
                val userInfoStr = if (userInfoData != null) {
                    userInfoData as String
                } else {
                    ""
                }
                if (userInfoStr != null && userInfoStr != "" && userInfoStr != "null" && userInfoStr != "undefined") {
                    try {
                        val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/record.uvue:175") as UTSJSONObject
                        departmentId = parsedUserInfo.getString("deptId") ?: ""
                    }
                     catch (error: Throwable) {
                        console.error("用户信息JSON解析失败:", error, " at pages/record/record.uvue:178")
                    }
                }
                var apiUrl = ""
                if (this.isDelayed) {
                    apiUrl = getApiUrl("/inspection-record/delayed?current=" + this.pageNum + "&size=" + this.pageSize + (if (departmentId != null && departmentId != "") {
                        "&departmentId=" + departmentId
                    } else {
                        ""
                    }))
                } else {
                    apiUrl = getApiUrl("/inspection-record/page?current=" + this.pageNum + "&size=" + this.pageSize + "&status=" + this.status + (if (departmentId != null && departmentId != "") {
                        "&departmentId=" + departmentId
                    } else {
                        ""
                    }
                    ))
                }
                uni_request<Any>(RequestOptions(url = apiUrl, method = "GET", header = object : UTSJSONObject() {
                    var Authorization = "Bearer " + token
                }, success = fun(res){
                    val responseData = res.data as UTSJSONObject
                    if (res.statusCode === 200 && responseData.getNumber("code") === 200) {
                        val dataObj = responseData.getJSON("data") as UTSJSONObject
                        val records = dataObj?.getArray("records") as UTSArray<UTSJSONObject>
                        val newRecords: UTSArray<InspectionRecord> = _uA()
                        if (records != null) {
                            run {
                                var i: Number = 0
                                while(i < records.length){
                                    val item = records[i]
                                    console.log("原始数据对象:", item, " at pages/record/record.uvue:207")
                                    console.log("problemDescription字段:", item.getString("problemDescription"), " at pages/record/record.uvue:208")
                                    val createTime = item.getString("createTime") ?: ""
                                    val record = InspectionRecord(id = item.getString("id") ?: "", problemDescription = item.getString("problemDescription") ?: "", responsiblePerson = item.getString("responsiblePerson") ?: "", inspectorName = item.getString("inspector") ?: "", createDate = this.formatDate(createTime), status = item.getNumber("status") ?: 0)
                                    console.log("处理后的记录对象:", record, " at pages/record/record.uvue:220")
                                    newRecords.push(record)
                                    i++
                                }
                            }
                        }
                        if (this.pageNum === 1) {
                            this.recordList = newRecords
                        } else {
                            this.recordList = this.recordList.concat(newRecords)
                        }
                        this.totalCount = dataObj?.getNumber("total") ?: newRecords.length
                        this.hasMore = newRecords.length >= this.pageSize
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
                    this.loadingMore = false
                }
                , fail = fun(error){
                    console.error("加载记录失败:", error, " at pages/record/record.uvue:244")
                    uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
                    this.loading = false
                    this.loadingMore = false
                }
                ))
        })
    }
    open var getStatusText = ::gen_getStatusText_fn
    open fun gen_getStatusText_fn(status: Number?): String {
        if (status == null) {
            return "未知"
        }
        val statusMap: UTSJSONObject = object : UTSJSONObject(UTSSourceMapPosition("statusMap", "pages/record/record.uvue", 257, 10)) {
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
    open var getStatusClass = ::gen_getStatusClass_fn
    open fun gen_getStatusClass_fn(status: Number?): String {
        if (status == null) {
            return "status-unknown"
        }
        val classMap: UTSJSONObject = object : UTSJSONObject(UTSSourceMapPosition("classMap", "pages/record/record.uvue", 277, 11)) {
            var `0` = "status-pending"
            var `1` = "status-completed"
            var `2` = "status-accepted"
        }
        val statusClass = classMap[status.toString(10)] as String
        return if (statusClass != null) {
            statusClass
        } else {
            "status-unknown"
        }
    }
    open var getStatusTitle = ::gen_getStatusTitle_fn
    open fun gen_getStatusTitle_fn(): String {
        if (this.isDelayed) {
            return "延迟整改"
        }
        if (this.status == "0") {
            return "待整改"
        }
        if (this.status == "1") {
            return "已整改"
        }
        if (this.status == "2") {
            return "已复查"
        }
        return ""
    }
    open var goToDetail = ::gen_goToDetail_fn
    open fun gen_goToDetail_fn(record: InspectionRecord) {
        if (this.isDelayed) {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/detail?id=" + record.id + "&delayed=true"))
        } else if (this.isReviewed) {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/review?id=" + record.id + "&reviewed=true"))
        } else if (record.status === 0) {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/detail?id=" + record.id))
        } else if (record.status === 1) {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/review?id=" + record.id))
        } else {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/review?id=" + record.id))
        }
    }
    open var refreshRecords = ::gen_refreshRecords_fn
    open fun gen_refreshRecords_fn() {
        this.pageNum = 1
        this.recordList = _uA()
        this.hasMore = true
        this.loadRecords()
        uni_stopPullDownRefresh()
    }
    open var onRefresh = ::gen_onRefresh_fn
    open fun gen_onRefresh_fn() {
        this.isRefreshing = true
        this.refreshRecords()
        setTimeout(fun(){
            if (this.isRefreshing) {
                this.isRefreshing = false
                uni_stopPullDownRefresh()
            }
        }
        , 1500)
    }
    open var onRefreshRestore = ::gen_onRefreshRestore_fn
    open fun gen_onRefreshRestore_fn() {
        this.isRefreshing = false
    }
    open var loadMore = ::gen_loadMore_fn
    open fun gen_loadMore_fn() {
        if (this.loadingMore || !this.hasMore) {
            return
        }
        this.pageNum++
        this.loadRecords()
    }
    open var handleScrollToLower = ::gen_handleScrollToLower_fn
    open fun gen_handleScrollToLower_fn() {
        if (this.hasMore && !this.loadingMore) {
            this.loadMore()
        }
    }
    open var showDeleteConfirm = ::gen_showDeleteConfirm_fn
    open fun gen_showDeleteConfirm_fn(recordId: String, problemDescription: String) {
        uni_showModal(ShowModalOptions(title = "确认删除", content = "\u786E\u5B9A\u8981\u5220\u9664\u95EE\u9898\"" + problemDescription + "\"\u5417\uFF1F\u6B64\u64CD\u4F5C\u4E0D\u53EF\u6062\u590D\u3002", confirmText = "删除", cancelText = "取消", confirmColor = "#ff4757", success = fun(res){
            if (res.confirm) {
                this.deleteRecord(recordId)
            }
        }
        ))
    }
    open var deleteRecord = ::gen_deleteRecord_fn
    open fun gen_deleteRecord_fn(recordId: String) {
        val token = uni_getStorageSync("token")
        uni_showLoading(ShowLoadingOptions(title = "删除中..."))
        uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record/" + recordId), method = "DELETE", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            uni_hideLoading()
            val responseData = res.data as UTSJSONObject
            if (res.statusCode === 200 && responseData.getNumber("code") === 200) {
                this.recordList = this.recordList.filter(fun(record): Boolean {
                    return record.id !== recordId
                })
                this.totalCount = Math.max(0, this.totalCount - 1)
                uni_showToast(ShowToastOptions(title = "删除成功", icon = "success"))
            } else {
                val errorMessage = responseData.getString("message") ?: "删除失败"
                uni_showToast(ShowToastOptions(title = errorMessage, icon = "none"))
            }
        }
        , fail = fun(error){
            uni_hideLoading()
            console.error("删除记录失败:", error, " at pages/record/record.uvue:415")
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
                return _uM("container" to _pS(_uM("backgroundColor" to "#f5f5f5", "width" to "100%")), "header" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#e0e0e0")), "title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333", "marginBottom" to 5)), "subtitle" to _pS(_uM("fontSize" to 14, "color" to "#666666")), "record-item" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "marginTop" to 10, "marginRight" to 10, "marginBottom" to 1, "marginLeft" to 10, "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8)), "record-content" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "flex-start", "marginBottom" to 10, "width" to "100%")), "left-content" to _pS(_uM("flex" to 1, "marginRight" to 15, "display" to "flex", "flexDirection" to "column", "gap" to "2px")), "problem-text" to _pS(_uM("fontSize" to 15, "color" to "#666666", "lineHeight" to 1.3, "fontWeight" to "400", "marginTop" to 2)), "right-info" to _pS(_uM("flexShrink" to 0, "textAlign" to "right", "display" to "flex", "flexDirection" to "column", "alignItems" to "flex-end", "justifyContent" to "flex-start", "minWidth" to 120, "maxWidth" to 150, "gap" to "6px", "marginLeft" to "auto")), "inspector-info" to _pS(_uM("fontSize" to 13, "color" to "#333333", "textAlign" to "right")), "date-text" to _pS(_uM("fontSize" to 13, "color" to "#666666")), "status-row" to _pS(_uM("marginTop" to 8)), "status-tag" to _pS(_uM("paddingTop" to 3, "paddingRight" to 10, "paddingBottom" to 3, "paddingLeft" to 10, "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "fontSize" to 12, "alignSelf" to "flex-end")), "status-text" to _pS(_uM("color" to "#ffffff", "fontSize" to 12)), "status-pending" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ff6b6b")), "status-processing" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffa726")), "status-completed" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#66bb6a")), "status-accepted" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#42a5f5")), "name-problem-row" to _pS(_uM("display" to "flex", "alignItems" to "center", "marginBottom" to 2)), "responsible-name" to _pS(_uM("fontSize" to 16, "color" to "#333333", "fontWeight" to "700", "marginTop" to 0, "marginRight" to 0, "marginBottom" to 0, "marginLeft" to 0)), "empty-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 60, "paddingRight" to 20, "paddingBottom" to 60, "paddingLeft" to 20)), "empty-icon" to _pS(_uM("fontSize" to 48, "marginBottom" to 15, "color" to "#cccccc")), "empty-text" to _pS(_uM("fontSize" to 16, "color" to "#999999")), "loading-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20)), "loading-text" to _pS(_uM("fontSize" to 14, "color" to "#999999")), "load-more" to _pS(_uM("textAlign" to "center", "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopWidth" to 1, "borderTopStyle" to "solid", "borderTopColor" to "#f0f0f0")), "load-more-text" to _pS(_uM("fontSize" to 14, "color" to "#666666")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
