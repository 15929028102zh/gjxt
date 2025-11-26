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
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesMonthlyRecord : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: Any) {
            if (options.status !== undefined) {
                this.status = parseInt(options.status)
            }
            val systemInfo = uni_getSystemInfoSync()
            this.containerHeight = systemInfo.windowHeight
            this.loadRecords()
        }
        , __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "style" to _nS(_uM("height" to (_ctx.containerHeight + "px"))), "enable-back-to-top" to "true", "refresher-enabled" to "true", "refresher-triggered" to _ctx.isRefreshing, "refresher-default-style" to "black", "refresher-background" to "#f5f5f5", "onRefresherrefresh" to _ctx.onRefresh, "onRefresherrestore" to _ctx.onRefreshRestore), _uA(
            _cE("view", _uM("class" to "header"), _uA(
                _cE("text", _uM("class" to "title"), "月检记录"),
                _cE("text", _uM("class" to "subtitle"), "共 " + _tD(_ctx.totalCount) + " 条" + _tD(_ctx.getStatusTitle()) + "记录", 1)
            )),
            _cE(Fragment, null, RenderHelpers.renderList(_ctx.recordList, fun(record, __key, __index, _cached): Any {
                return _cE("view", _uM("class" to "record-item", "key" to record.id, "onClick" to fun(){
                    _ctx.goToDetail(record)
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
                ), 8, _uA(
                    "onClick"
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
            if (isTrue(_ctx.hasMore && !_ctx.loading && _ctx.recordList.length > 0)) {
                _cE("view", _uM("key" to 2, "class" to "load-more", "onClick" to _ctx.loadMore), _uA(
                    _cE("text", _uM("class" to "load-more-text"), "加载更多")
                ), 8, _uA(
                    "onClick"
                ))
            } else {
                _cC("v-if", true)
            }
            ,
            if (isTrue(!_ctx.hasMore && _ctx.recordList.length > 0)) {
                _cE("view", _uM("key" to 3, "class" to "no-more"), _uA(
                    _cE("text", _uM("class" to "no-more-text"), "没有更多数据了")
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
    open var recordList: UTSArray<MonthlyRecord> by `$data`
    open var loading: Boolean by `$data`
    open var hasMore: Boolean by `$data`
    open var totalCount: Number by `$data`
    open var pageNum: Number by `$data`
    open var pageSize: Number by `$data`
    open var status: Number? by `$data`
    open var isRefreshing: Boolean by `$data`
    open var containerHeight: Number by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("recordList" to _uA<MonthlyRecord>(), "loading" to false, "hasMore" to true, "totalCount" to 0, "pageNum" to 1, "pageSize" to 10, "status" to null as Number?, "isRefreshing" to false, "containerHeight" to 0)
    }
    open var loadRecords = ::gen_loadRecords_fn
    open fun gen_loadRecords_fn() {
        if (this.loading) {
            return
        }
        this.loading = true
        val token = uni_getStorageSync("token")
        var departmentId = ""
        var isAdmin = false
        val userInfoData = uni_getStorageSync("userInfo")
        val userInfoStr = if (userInfoData != null) {
            userInfoData as String
        } else {
            ""
        }
        if (userInfoStr != null && userInfoStr != "" && userInfoStr != "null" && userInfoStr != "undefined") {
            try {
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/record.uvue:118") as UTSJSONObject
                departmentId = parsedUserInfo.getString("deptId") ?: ""
                isAdmin = (parsedUserInfo.getNumber("isAdmin") ?: 0) === 1
            }
             catch (error: Throwable) {
                console.error("用户信息JSON解析失败:", error, " at pages/monthly/record.uvue:122")
            }
        }
        var params = "current=" + this.pageNum + "&size=" + this.pageSize
        if (this.status != null) {
            params += "&status=" + this.status!!
        }
        if (!isAdmin && departmentId != null && departmentId != "") {
            params += "&departmentId=" + departmentId
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?" + params), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            this.loading = false
            this.isRefreshing = false
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                val records = data.getArray("records") as UTSArray<UTSJSONObject>
                val newRecords = records.map(fun(record): MonthlyRecord {
                    return MonthlyRecord(id = record.getNumber("id") ?: 0, problemDescription = record.getString("problemDescription") ?: "", responsiblePerson = record.getString("responsiblePerson") ?: "", inspectorName = record.getString("inspectorName") ?: "", createDate = this.formatDate(record.getString("createTime") ?: ""), status = record.getNumber("status") ?: 0, departmentName = record.getString("departmentName") ?: "")
                })
                if (this.pageNum === 1) {
                    this.recordList = newRecords
                } else {
                    this.recordList = this.recordList.concat(newRecords)
                }
                this.totalCount = data.getNumber("total") ?: 0
                this.hasMore = this.recordList.length < this.totalCount
            } else {
                uni_showToast(ShowToastOptions(title = "加载失败", icon = "none"))
            }
        }
        , fail = fun(error){
            this.loading = false
            this.isRefreshing = false
            console.log("加载月检记录失败:", error, " at pages/monthly/record.uvue:185")
            uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
        }
        ))
    }
    open var loadMore = ::gen_loadMore_fn
    open fun gen_loadMore_fn() {
        if (this.hasMore && !this.loading) {
            this.pageNum++
            this.loadRecords()
        }
    }
    open var onRefresh = ::gen_onRefresh_fn
    open fun gen_onRefresh_fn() {
        this.isRefreshing = true
        this.pageNum = 1
        this.loadRecords()
    }
    open var onRefreshRestore = ::gen_onRefreshRestore_fn
    open fun gen_onRefreshRestore_fn() {
        this.isRefreshing = false
    }
    open var goToDetail = ::gen_goToDetail_fn
    open fun gen_goToDetail_fn(record: MonthlyRecord) {
        uni_navigateTo(NavigateToOptions(url = "/pages/monthly/detail?id=" + record.id))
    }
    open var getStatusTitle = ::gen_getStatusTitle_fn
    open fun gen_getStatusTitle_fn(): String {
        if (this.status === 0) {
            return "待整改"
        }
        if (this.status === 1) {
            return "已整改"
        }
        if (this.status === 2) {
            return "已复查"
        }
        return ""
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
    open var formatDate = ::gen_formatDate_fn
    open fun gen_formatDate_fn(dateStr: String): String {
        if (!dateStr) {
            return ""
        }
        try {
            val date = Date(dateStr)
            val year = date.getFullYear()
            val month = String(date.getMonth() + 1).padStart(2, "0")
            val day = String(date.getDate()).padStart(2, "0")
            return "" + year + "-" + month + "-" + day
        }
         catch (error: Throwable) {
            return dateStr
        }
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
                return _uM("container" to _pS(_uM("backgroundColor" to "#f5f5f5", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx")), "header" to _pS(_uM("backgroundColor" to "#FFFFFF", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "marginBottom" to "20rpx", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "boxShadow" to "0 2rpx 8rpx rgba(0, 0, 0, 0.1)")), "title" to _pS(_uM("fontSize" to "36rpx", "fontWeight" to "bold", "color" to "#333333", "marginBottom" to "10rpx")), "subtitle" to _pS(_uM("fontSize" to "28rpx", "color" to "#666666")), "record-item" to _pS(_uM("backgroundColor" to "#FFFFFF", "marginBottom" to "20rpx", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "boxShadow" to "0 2rpx 8rpx rgba(0, 0, 0, 0.1)", "overflow" to "hidden")), "record-content" to _pS(_uM("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "position" to "relative")), "left-content" to _pS(_uM("marginBottom" to "20rpx")), "name-problem-row" to _pS(_uM("display" to "flex", "alignItems" to "center", "marginBottom" to "10rpx")), "responsible-name" to _pS(_uM("fontSize" to "32rpx", "fontWeight" to "bold", "color" to "#333333")), "right-info" to _pS(_uM("display" to "flex", "flexDirection" to "column", "alignItems" to "flex-end", "position" to "absolute", "top" to "30rpx", "right" to "30rpx")), "inspector-info" to _pS(_uM("fontSize" to "24rpx", "color" to "#999999", "marginBottom" to "10rpx")), "date-text" to _pS(_uM("fontSize" to "24rpx", "color" to "#999999", "marginBottom" to "10rpx")), "status-tag" to _pS(_uM("paddingTop" to "8rpx", "paddingRight" to "16rpx", "paddingBottom" to "8rpx", "paddingLeft" to "16rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "fontSize" to "24rpx")), "status-pending" to _pS(_uM("backgroundColor" to "#fff3cd", "color" to "#856404")), "status-completed" to _pS(_uM("backgroundColor" to "#d4edda", "color" to "#155724")), "status-reviewed" to _pS(_uM("backgroundColor" to "#d1ecf1", "color" to "#0c5460")), "status-unknown" to _pS(_uM("backgroundColor" to "#f8d7da", "color" to "#721c24")), "status-text" to _pS(_uM("fontSize" to "24rpx")), "problem-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#666666", "lineHeight" to 1.5, "marginTop" to "20rpx")), "empty-state" to _pS(_uM("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "paddingTop" to "100rpx", "paddingRight" to 0, "paddingBottom" to "100rpx", "paddingLeft" to 0)), "empty-icon" to _pS(_uM("fontSize" to "80rpx", "marginBottom" to "20rpx")), "empty-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#999999")), "loading-state" to _pS(_uM("display" to "flex", "justifyContent" to "center", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0)), "loading-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#999999")), "load-more" to _pS(_uM("display" to "flex", "justifyContent" to "center", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "backgroundColor" to "#FFFFFF", "marginTop" to "20rpx", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx")), "load-more-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#007aff")), "no-more" to _pS(_uM("display" to "flex", "justifyContent" to "center", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0)), "no-more-text" to _pS(_uM("fontSize" to "24rpx", "color" to "#999999")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
