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
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.switchTab as uni_switchTab
open class GenPagesWeeklyWeekly : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(_: OnLoadOptions) {
            this.checkLogin()
            this.initData()
            this.initContainerHeight()
            this.loadStatistics()
            this.loadRecentRecords()
        }
        , __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "scroll-top" to _ctx.scrollTop, "style" to _nS(_uM("height" to (_ctx.containerHeight + "px"))), "enable-back-to-top" to "true", "refresher-enabled" to "true", "refresher-triggered" to _ctx.isRefreshing, "refresher-default-style" to "black", "refresher-background" to "#f5f5f5", "onRefresherrefresh" to _ctx.onRefresh, "onRefresherrestore" to _ctx.onRefreshRestore), _uA(
            _cE("view", _uM("class" to "header"), _uA(
                _cE("view", _uM("class" to "welcome"), _uA(
                    _cE("text", _uM("class" to "greeting"), "欢迎回来，" + _tD(_ctx.userName), 1),
                    _cE("text", _uM("class" to "app-name"), _tD(_ctx.currentDate), 1)
                )),
                _cE("image", _uM("class" to "avatar", "src" to if ((_ctx.userInfo.getString("avatar") ?: "") != "") {
                    (_ctx.userInfo.getString("avatar") ?: "")
                } else {
                    "/static/default-avatar.svg"
                }
                , "mode" to "aspectFill"), null, 8, _uA(
                    "src"
                ))
            )),
            _cE("view", _uM("class" to "action-buttons"), _uA(
                _cE("view", _uM("class" to "action-button", "onClick" to _ctx.goToAddRecord), _uA(
                    _cE("view", _uM("class" to "button-icon"), "➕"),
                    _cE("text", null, "周检"),
                    _cE("text", _uM("class" to "button-text"), "每周安全检查")
                ), 8, _uA(
                    "onClick"
                )),
                _cE("view", _uM("class" to "action-button delayed-button", "onClick" to _ctx.goToDelayedRecord), _uA(
                    _cE("view", _uM("class" to "button-icon"), "⏰"),
                    _cE("text", null, "延迟整改"),
                    _cE("text", _uM("class" to "button-text"), "延期问题处理")
                ), 8, _uA(
                    "onClick"
                ))
            )),
            _cE("view", _uM("class" to "stats-container"), _uA(
                _cE("view", _uM("class" to "stats-card", "onClick" to _ctx.goToPendingIssues), _uA(
                    _cE("text", _uM("class" to "stats-number"), _tD(_ctx.statistics.pendingIssues), 1),
                    _cE("text", _uM("class" to "stats-label"), "待整改"),
                    _cE("text", _uM("class" to "stats-icon"), "⚠️")
                ), 8, _uA(
                    "onClick"
                )),
                _cE("view", _uM("class" to "stats-card", "onClick" to _ctx.goToCompletedIssues), _uA(
                    _cE("text", _uM("class" to "stats-number"), _tD(_ctx.statistics.completedIssues), 1),
                    _cE("text", _uM("class" to "stats-label"), "已整改"),
                    _cE("text", _uM("class" to "stats-icon"), "✅")
                ), 8, _uA(
                    "onClick"
                )),
                _cE("view", _uM("class" to "stats-card", "onClick" to _ctx.goToWeeklyReport), _uA(
                    _cE("text", _uM("class" to "stats-number"), _tD(_ctx.statistics.weeklyTotal), 1),
                    _cE("text", _uM("class" to "stats-label"), "本周记录"),
                    _cE("text", _uM("class" to "stats-icon"), "📊")
                ), 8, _uA(
                    "onClick"
                ))
            ))
        ), 44, _uA(
            "scroll-top",
            "refresher-triggered",
            "onRefresherrefresh",
            "onRefresherrestore"
        ))
    }
    open var userInfo: UTSJSONObject by `$data`
    open var userName: String by `$data`
    open var currentDate: String by `$data`
    open var scrollTop: Number by `$data`
    open var containerHeight: Number by `$data`
    open var isRefreshing: Boolean by `$data`
    open var statistics: StatisticsData by `$data`
    open var recentRecords: UTSArray<RecentRecord> by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("userInfo" to UTSJSONObject(), "userName" to "用户", "currentDate" to "", "scrollTop" to 0, "containerHeight" to 0, "isRefreshing" to false, "statistics" to StatisticsData(pendingIssues = 0, completedIssues = 0, weeklyTotal = 0), "recentRecords" to _uA<RecentRecord>())
    }
    open var checkLogin = ::gen_checkLogin_fn
    open fun gen_checkLogin_fn(): Boolean {
        val token = uni_getStorageSync("token")
        if (token == null || token == "") {
            uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
            return false
        }
        this.loadUserInfo()
        return true
    }
    open var loadUserInfo = ::gen_loadUserInfo_fn
    open fun gen_loadUserInfo_fn() {
        val userInfoStr = uni_getStorageSync("userInfo") as String
        if (userInfoStr != null && userInfoStr != "" && userInfoStr != "null" && userInfoStr != "undefined") {
            try {
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/weekly/weekly.uvue:124") as UTSJSONObject
                this.userInfo = parsedUserInfo
                val realNameValue = parsedUserInfo.getString("realName")
                this.userName = if (realNameValue != null) {
                    realNameValue
                } else {
                    "用户"
                }
            } catch (error: Throwable) {
                console.error("用户信息JSON解析失败:", error, " at pages/weekly/weekly.uvue:130")
                this.userInfo = UTSJSONObject()
                this.userName = "解析失败用户"
            }
        } else {
            this.userInfo = UTSJSONObject()
            this.userName = "默认用户"
        }
    }
    open var initData = ::gen_initData_fn
    open fun gen_initData_fn() {
        val now = Date()
        val year = now.getFullYear()
        val month = (now.getMonth() + 1).toString(10)
        val formattedMonth = if (month.length == 1) {
            "0" + month
        } else {
            month
        }
        val day = now.getDate().toString(10)
        val formattedDay = if (day.length == 1) {
            "0" + day
        } else {
            day
        }
        val weekdays = _uA(
            "日",
            "一",
            "二",
            "三",
            "四",
            "五",
            "六"
        )
        val weekday = weekdays[now.getDay()]
        this.currentDate = "" + year + "\u5E74" + formattedMonth + "\u6708" + formattedDay + "\u65E5 \u661F\u671F" + weekday
    }
    open var loadStatistics = ::gen_loadStatistics_fn
    open fun gen_loadStatistics_fn() {
        console.log("开始加载统计数据", " at pages/weekly/weekly.uvue:153")
        val token = uni_getStorageSync("token")
        val pendingUrl = getApiUrl("/inspection-record/weekly-count?status=0")
        console.log("待整改数量请求URL:", pendingUrl, " at pages/weekly/weekly.uvue:158")
        uni_request<Any>(RequestOptions(url = pendingUrl, method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            console.log("待整改数量请求成功:", res, " at pages/weekly/weekly.uvue:166")
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                this.statistics.pendingIssues = response.getNumber("data") ?: 0
                console.log("待整改数量:", this.statistics.pendingIssues, " at pages/weekly/weekly.uvue:170")
            } else {
                console.log("待整改数量请求失败:", response, " at pages/weekly/weekly.uvue:172")
            }
        }
        , fail = fun(error){
            console.log("待整改数量请求错误:", error, " at pages/weekly/weekly.uvue:176")
        }
        ))
        val completedUrl = getApiUrl("/inspection-record/weekly-count?status=1")
        console.log("已整改数量请求URL:", completedUrl, " at pages/weekly/weekly.uvue:182")
        uni_request<Any>(RequestOptions(url = completedUrl, method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            console.log("已整改数量请求成功:", res, " at pages/weekly/weekly.uvue:190")
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                this.statistics.completedIssues = response.getNumber("data") ?: 0
                console.log("已整改数量:", this.statistics.completedIssues, " at pages/weekly/weekly.uvue:194")
            } else {
                console.log("已整改数量请求失败:", response, " at pages/weekly/weekly.uvue:196")
            }
        }
        , fail = fun(error){
            console.log("已整改数量请求错误:", error, " at pages/weekly/weekly.uvue:200")
        }
        ))
        val totalUrl = getApiUrl("/inspection-record/weekly-count")
        console.log("总记录数量请求URL:", totalUrl, " at pages/weekly/weekly.uvue:206")
        uni_request<Any>(RequestOptions(url = totalUrl, method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            console.log("总记录数量请求成功:", res, " at pages/weekly/weekly.uvue:214")
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                this.statistics.weeklyTotal = response.getNumber("data") ?: 0
                console.log("总记录数量:", this.statistics.weeklyTotal, " at pages/weekly/weekly.uvue:218")
            } else {
                console.log("总记录数量请求失败:", response, " at pages/weekly/weekly.uvue:220")
            }
        }
        , fail = fun(error){
            console.log("总记录数量请求错误:", error, " at pages/weekly/weekly.uvue:224")
        }
        ))
    }
    open var loadRecentRecords = ::gen_loadRecentRecords_fn
    open fun gen_loadRecentRecords_fn() {
        val token = uni_getStorageSync("token")
        val apiUrl = getApiUrl("/inspection-record/page?current=1&size=5&status=0")
        console.log("请求URL:", apiUrl, " at pages/weekly/weekly.uvue:232")
        uni_request<Any>(RequestOptions(url = apiUrl, method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                val records = data.getArray("records") as UTSArray<UTSJSONObject>
                this.recentRecords = _uA()
                run {
                    var i: Number = 0
                    while(i < records.length){
                        val item = records[i]
                        val record = RecentRecord(id = item.getString("id") ?: "", problemDescription = item.getString("problemDescription") ?: "", locationName = item.getString("locationName") ?: "", createTime = item.getString("createTime") ?: "", status = item.getString("status") ?: "")
                        this.recentRecords.push(record)
                        i++
                    }
                }
            }
        }
        , fail = fun(error){
            console.log("加载最近记录失败:", error, " at pages/weekly/weekly.uvue:259")
        }
        ))
    }
    open var getStatusText = ::gen_getStatusText_fn
    open fun gen_getStatusText_fn(status: String): String {
        if (status == "0") {
            return "待整改"
        }
        if (status == "1") {
            return "已整改"
        }
        if (status == "2") {
            return "已复查"
        }
        return "未知"
    }
    open var getStatusClass = ::gen_getStatusClass_fn
    open fun gen_getStatusClass_fn(status: String): String {
        if (status == "0") {
            return "status-pending"
        }
        if (status == "1") {
            return "status-completed"
        }
        if (status == "2") {
            return "status-accepted"
        }
        return "status-unknown"
    }
    open var goToPendingIssues = ::gen_goToPendingIssues_fn
    open fun gen_goToPendingIssues_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?status=0"))
    }
    open var goToCompletedIssues = ::gen_goToCompletedIssues_fn
    open fun gen_goToCompletedIssues_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?status=1"))
    }
    open var goToWeeklyReport = ::gen_goToWeeklyReport_fn
    open fun gen_goToWeeklyReport_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/statistics/statistics"))
    }
    open var goToRecords = ::gen_goToRecords_fn
    open fun gen_goToRecords_fn() {
        uni_switchTab(SwitchTabOptions(url = "/pages/record/record"))
    }
    open var goToAddRecord = ::gen_goToAddRecord_fn
    open fun gen_goToAddRecord_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/add"))
    }
    open var goToDelayedRecord = ::gen_goToDelayedRecord_fn
    open fun gen_goToDelayedRecord_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?status=0&delayed=true"))
    }
    open var goToRecordDetail = ::gen_goToRecordDetail_fn
    open fun gen_goToRecordDetail_fn(recordId: String) {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/detail?id=" + recordId))
    }
    open var onRefresh = ::gen_onRefresh_fn
    open fun gen_onRefresh_fn() {
        console.log("开始下拉刷新", " at pages/weekly/weekly.uvue:324")
        this.isRefreshing = true
        this.reloadAllData()
    }
    open var onRefreshRestore = ::gen_onRefreshRestore_fn
    open fun gen_onRefreshRestore_fn() {
        console.log("刷新恢复", " at pages/weekly/weekly.uvue:333")
        this.isRefreshing = false
    }
    open var reloadAllData = ::gen_reloadAllData_fn
    open fun gen_reloadAllData_fn() {
        console.log("重新加载所有数据", " at pages/weekly/weekly.uvue:339")
        try {
            this.initData()
            this.loadUserInfo()
            this.loadStatistics()
            this.loadRecentRecords()
            console.log("数据加载完成", " at pages/weekly/weekly.uvue:352")
        }
         catch (error: Throwable) {
            console.error("数据加载失败:", error, " at pages/weekly/weekly.uvue:355")
        }
         finally{
            setTimeout(fun(){
                console.log("关闭刷新动画", " at pages/weekly/weekly.uvue:359")
                this.isRefreshing = false
            }
            , 1000)
        }
    }
    open var initContainerHeight = ::gen_initContainerHeight_fn
    open fun gen_initContainerHeight_fn() {
        val res = uni_getSystemInfoSync()
        this.containerHeight = res.windowHeight
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
                return _uM("container" to _pS(_uM("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "150rpx", "paddingLeft" to "30rpx", "backgroundImage" to "linear-gradient(180deg, #f8fafc 0%, #e2e8f0 100%)", "backgroundColor" to "rgba(0,0,0,0)", "boxSizing" to "border-box")), "header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "30rpx", "paddingRight" to "25rpx", "paddingBottom" to "30rpx", "paddingLeft" to "25rpx", "backgroundImage" to "linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "marginBottom" to "30rpx", "boxShadow" to "0 8rpx 32rpx rgba(102, 126, 234, 0.3)", "position" to "relative", "overflow" to "hidden", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "right::before" to 0, "bottom::before" to 0, "pointerEvents::before" to "none")), "welcome" to _pS(_uM("display" to "flex", "flexDirection" to "column", "flex" to 1, "zIndex" to 2, "position" to "relative")), "greeting" to _pS(_uM("color" to "rgba(105,72,72,0.95)", "fontSize" to "32rpx", "marginBottom" to "8rpx", "letterSpacing" to "0.5rpx", "textShadow" to "0 2rpx 4rpx rgba(0, 0, 0, 0.1)")), "app-name" to _pS(_uM("color" to "rgba(105,72,72,0.95)", "fontSize" to "26rpx", "fontWeight" to "400", "letterSpacing" to "0.3rpx")), "avatar" to _pS(_uM("width" to "100rpx", "height" to "100rpx", "borderTopLeftRadius" to "50rpx", "borderTopRightRadius" to "50rpx", "borderBottomRightRadius" to "50rpx", "borderBottomLeftRadius" to "50rpx", "backgroundColor" to "rgba(255,255,255,0.2)", "overflow" to "hidden", "borderTopWidth" to "3rpx", "borderRightWidth" to "3rpx", "borderBottomWidth" to "3rpx", "borderLeftWidth" to "3rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(255,255,255,0.4)", "borderRightColor" to "rgba(255,255,255,0.4)", "borderBottomColor" to "rgba(255,255,255,0.4)", "borderLeftColor" to "rgba(255,255,255,0.4)", "boxShadow" to "0 6rpx 20rpx rgba(0, 0, 0, 0.15), 0 0 0 1rpx rgba(255, 255, 255, 0.1)", "zIndex" to 2, "position" to "relative", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "transform:active" to "scale(0.95)")), "stats-container" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "marginBottom" to 30)), "stats-card" to _pS(_uM("flex" to 1, "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 15, "paddingBottom" to 20, "paddingLeft" to 15, "marginTop" to 0, "marginRight" to 5, "marginBottom" to 0, "marginLeft" to 5, "textAlign" to "center", "position" to "relative", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "stats-number" to _pS(_uM("fontSize" to 24, "fontWeight" to "bold", "color" to "#007AFF", "flexDirection" to "column", "marginBottom" to 8)), "stats-label" to _pS(_uM("fontSize" to 14, "color" to "#666666", "flexDirection" to "column", "marginBottom" to 8)), "stats-icon" to _pS(_uM("fontSize" to 20, "position" to "absolute", "top" to 15, "right" to 15)), "action-buttons" to _pS(_uM("marginBottom" to 30, "display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between", "gap" to "10px")), "action-button" to _pS(_uM("flex" to 1, "backgroundImage" to "none", "backgroundColor" to "#d1ecf1", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 15, "paddingRight" to 10, "paddingBottom" to 15, "paddingLeft" to 10, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)", "minWidth" to 0)), "delayed-button" to _pS(_uM("!backgroundImage" to "none", "!backgroundColor" to "#ff4757")), "button-icon" to _pS(_uM("fontSize" to 20, "marginRight" to 8, "color" to "#007AFF", "flexShrink" to 0)), "button-text" to _pS(_uM("fontSize" to 14, "color" to "#333333", "whiteSpace" to "nowrap", "overflow" to "hidden", "textOverflow" to "ellipsis")), "recent-records" to _pS(_uM("marginBottom" to 20)), "section-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333", "marginBottom" to 15, "flexDirection" to "column")), "section-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to 15)), "more-text" to _pS(_uM("fontSize" to 14, "color" to "#007AFF")), "records-list" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 15, "paddingRight" to 15, "paddingBottom" to 15, "paddingLeft" to 15, "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "record-item" to _uM("" to _uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 0, "paddingBottom" to 15, "paddingLeft" to 0, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000"), ".records-list " to _uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 15, "paddingRight" to 15, "paddingBottom" to 15, "paddingLeft" to 15, "marginBottom" to 10, "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)", "borderBottomWidth" to "medium", "borderBottomStyle" to "none", "borderBottomColor" to "#000000")), "record-info" to _pS(_uM("flex" to 1)), "record-title" to _pS(_uM("fontSize" to 16, "color" to "#333333", "flexDirection" to "column", "marginBottom" to 4)), "record-meta" to _pS(_uM("fontSize" to 12, "color" to "#999999", "flexDirection" to "column")), "record-status" to _pS(_uM("paddingTop" to 4, "paddingRight" to 8, "paddingBottom" to 4, "paddingLeft" to 8, "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "fontSize" to 12)), "status-pending" to _pS(_uM("backgroundColor" to "#fff3cd", "color" to "#856404")), "status-processing" to _pS(_uM("backgroundColor" to "#d1ecf1", "color" to "#0c5460")), "status-completed" to _pS(_uM("backgroundColor" to "#d4edda", "color" to "#155724")), "status-accepted" to _pS(_uM("backgroundColor" to "#e2e3e5", "color" to "#383d41")), "status-unknown" to _pS(_uM("backgroundColor" to "#f8f9fa", "color" to "#6c757d")), "empty-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20)), "recent-section" to _pS(_uM("marginTop" to 20, "marginRight" to 20, "marginBottom" to 20, "marginLeft" to 20)), "view-all" to _pS(_uM("fontSize" to 14, "color" to "#007AFF")), "empty-records" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20, "textAlign" to "center", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "empty-text" to _pS(_uM("fontSize" to 14, "color" to "#999999")), "record-content" to _pS(_uM("display" to "flex", "flexDirection" to "column", "gap" to "8px")), "problem-desc" to _pS(_uM("fontSize" to 16, "color" to "#333333", "lineHeight" to 1.4)), "location-name" to _pS(_uM("fontSize" to 14, "color" to "#666666")), "record-footer" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginTop" to 5)), "create-time" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "status-tag" to _pS(_uM("paddingTop" to 4, "paddingRight" to 8, "paddingBottom" to 4, "paddingLeft" to 8, "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "fontSize" to 12)), "status-text" to _pS(_uM("color" to "#ffffff")), "@TRANSITION" to _uM("avatar" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
