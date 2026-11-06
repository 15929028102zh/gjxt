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
import io.dcloud.uniapp.extapi.getSystemInfoSync as uni_getSystemInfoSync
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.switchTab as uni_switchTab
open class GenPagesWeeklyWeekly : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(_: OnLoadOptions) {
            if (!this.checkLogin()) {
                return
            }
            this.initContainerHeight()
            refreshUserInfo().then(fun(){
                this.loadUserInfo()
                this.initData()
                this.loadStatistics()
                this.loadRecentRecords()
            }
            )
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
                _cE("image", _uM("class" to "avatar", "src" to if (_ctx.userInfo != null && (_ctx.userInfo.getString("avatar") ?: "") != "") {
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
                    _cE("text", _uM("class" to "button-text"), "周检"),
                    _cE("text", _uM("class" to "button-text-sub"), "每周安全检查")
                ), 8, _uA(
                    "onClick"
                )),
                _cE("view", _uM("class" to "action-button delayed-button", "onClick" to _ctx.goToDelayedRecord), _uA(
                    _cE("view", _uM("class" to "button-icon"), "⏰"),
                    _cE("text", _uM("class" to "button-text"), "延迟整改"),
                    _cE("text", _uM("class" to "button-text-sub"), "延期问题处理")
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
                _cE("view", _uM("class" to "stats-card", "onClick" to _ctx.goToReviewedRecords), _uA(
                    _cE("text", _uM("class" to "stats-number"), _tD(_ctx.statistics.reviewedCount), 1),
                    _cE("text", _uM("class" to "stats-label"), "已复查数量"),
                    _cE("text", _uM("class" to "stats-icon"), "✅")
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
        return _uM("userInfo" to _uO(), "userName" to "用户", "currentDate" to "", "scrollTop" to 0, "containerHeight" to 0, "isRefreshing" to false, "statistics" to StatisticsData(pendingIssues = 0, completedIssues = 0, reviewedCount = 0), "recentRecords" to _uA<RecentRecord>())
    }
    open var checkLogin = ::gen_checkLogin_fn
    open fun gen_checkLogin_fn(): Boolean {
        val token = uni_getStorageSync("token")
        if (token == null || token == "") {
            uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
            return false
        }
        return true
    }
    open var loadUserInfo = ::gen_loadUserInfo_fn
    open fun gen_loadUserInfo_fn() {
        val userInfoData = uni_getStorageSync("userInfo")
        val userInfoStr = if (userInfoData != null) {
            userInfoData as String
        } else {
            ""
        }
        if (userInfoStr != null && userInfoStr != "" && userInfoStr != "null" && userInfoStr != "undefined") {
            try {
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/weekly/weekly.uvue:128") as UTSJSONObject
                this.userInfo = parsedUserInfo
                val realNameValue = parsedUserInfo.getString("realName")
                this.userName = if (realNameValue != null) {
                    realNameValue
                } else {
                    "用户"
                }
            } catch (error: Throwable) {
                console.error("用户信息JSON解析失败:", error, " at pages/weekly/weekly.uvue:134")
                this.userInfo = _uO()
                this.userName = "解析失败用户"
            }
        } else {
            this.userInfo = _uO()
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
        this.currentDate = "" + year + "年" + formattedMonth + "月" + formattedDay + "日 星期" + weekday
    }
    open var loadStatistics = ::gen_loadStatistics_fn
    open fun gen_loadStatistics_fn() {
        console.log("开始加载统计数据", " at pages/weekly/weekly.uvue:157")
        val token = uni_getStorageSync("token")
        var departmentId = ""
        val isAdmin = isWeeklyAdmin()
        val userInfoData = uni_getStorageSync("userInfo")
        val userInfoStr = if (userInfoData != null) {
            userInfoData as String
        } else {
            ""
        }
        if (userInfoStr != null && userInfoStr != "" && userInfoStr != "null" && userInfoStr != "undefined") {
            try {
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/weekly/weekly.uvue:168") as UTSJSONObject
                departmentId = parsedUserInfo.getString("deptId") ?: ""
                console.log("获取到的部门id", departmentId, " at pages/weekly/weekly.uvue:170")
                console.log("是否为管理员", isAdmin, " at pages/weekly/weekly.uvue:171")
            }
             catch (error: Throwable) {
                console.error("用户信息JSON解析失败:", error, " at pages/weekly/weekly.uvue:173")
            }
        }
        val deptParam = if ((!isAdmin && departmentId != null && departmentId != "")) {
            "&departmentId=" + departmentId
        } else {
            ""
        }
        val pendingUrl = getApiUrl("/inspection-record/page?current=1&size=1&status=0" + deptParam)
        console.log("待整改数量请求URL:", pendingUrl, " at pages/weekly/weekly.uvue:182")
        uni_request<Any>(RequestOptions(url = pendingUrl, method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            console.log("待整改数量请求成功:", res, " at pages/weekly/weekly.uvue:190")
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                this.statistics.pendingIssues = data.getNumber("total") ?: 0
                console.log("待整改数量:", this.statistics.pendingIssues, " at pages/weekly/weekly.uvue:195")
            } else {
                console.log("待整改数量请求失败:", response, " at pages/weekly/weekly.uvue:197")
            }
        }
        , fail = fun(error){
            console.log("待整改数量请求错误:", error, " at pages/weekly/weekly.uvue:201")
        }
        ))
        val completedUrl = getApiUrl("/inspection-record/page?current=1&size=1&status=1" + deptParam)
        console.log("已整改数量请求URL:", completedUrl, " at pages/weekly/weekly.uvue:207")
        uni_request<Any>(RequestOptions(url = completedUrl, method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            console.log("已整改数量请求成功:", res, " at pages/weekly/weekly.uvue:215")
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                this.statistics.completedIssues = data.getNumber("total") ?: 0
                console.log("已整改数量:", this.statistics.completedIssues, " at pages/weekly/weekly.uvue:220")
            } else {
                console.log("已整改数量请求失败:", response, " at pages/weekly/weekly.uvue:222")
            }
        }
        , fail = fun(error){
            console.log("已整改数量请求错误:", error, " at pages/weekly/weekly.uvue:226")
        }
        ))
        val reviewedUrl = getApiUrl("/inspection-record/page?current=1&size=1&status=2" + deptParam)
        console.log("已复查数量请求URL:", reviewedUrl, " at pages/weekly/weekly.uvue:232")
        uni_request<Any>(RequestOptions(url = reviewedUrl, method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            console.log("已复查数量请求成功:", res, " at pages/weekly/weekly.uvue:240")
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                this.statistics.reviewedCount = data.getNumber("total") ?: 0
                console.log("已复查数量:", this.statistics.reviewedCount, " at pages/weekly/weekly.uvue:245")
            } else {
                console.log("已复查数量请求失败:", response, " at pages/weekly/weekly.uvue:247")
            }
        }
        , fail = fun(error){
            console.log("已复查数量请求错误:", error, " at pages/weekly/weekly.uvue:251")
        }
        ))
    }
    open var loadRecentRecords = ::gen_loadRecentRecords_fn
    open fun gen_loadRecentRecords_fn() {
        val token = uni_getStorageSync("token")
        var departmentId = ""
        val isAdmin = isWeeklyAdmin()
        val userInfoData = uni_getStorageSync("userInfo")
        val userInfoStr = if (userInfoData != null) {
            userInfoData as String
        } else {
            ""
        }
        if (userInfoStr != null && userInfoStr != "" && userInfoStr != "null" && userInfoStr != "undefined") {
            try {
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/weekly/weekly.uvue:267") as UTSJSONObject
                departmentId = parsedUserInfo.getString("deptId") ?: ""
            }
             catch (error: Throwable) {
                console.error("用户信息JSON解析失败:", error, " at pages/weekly/weekly.uvue:270")
            }
        }
        val deptParam = if ((!isAdmin && departmentId != null && departmentId != "")) {
            "&departmentId=" + departmentId
        } else {
            ""
        }
        val apiUrl = getApiUrl("/inspection-record/page?current=1&size=5&status=0" + deptParam)
        console.log("请求URL:", apiUrl, " at pages/weekly/weekly.uvue:278")
        uni_request<Any>(RequestOptions(url = apiUrl, method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
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
            console.log("加载最近记录失败:", error, " at pages/weekly/weekly.uvue:305")
        }
        ))
    }
    open var getStatusText = ::gen_getStatusText_fn
    open fun gen_getStatusText_fn(status: String?): String {
        if (status == null) {
            return "未知"
        }
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
    open fun gen_getStatusClass_fn(status: String?): String {
        if (status == null) {
            return "status-unknown"
        }
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
    open var goToReviewedRecords = ::gen_goToReviewedRecords_fn
    open fun gen_goToReviewedRecords_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?type=reviewed"))
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
        console.log("开始下拉刷新", " at pages/weekly/weekly.uvue:372")
        this.isRefreshing = true
        this.reloadAllData()
    }
    open var onRefreshRestore = ::gen_onRefreshRestore_fn
    open fun gen_onRefreshRestore_fn() {
        console.log("刷新恢复", " at pages/weekly/weekly.uvue:381")
        this.isRefreshing = false
    }
    open var reloadAllData = ::gen_reloadAllData_fn
    open fun gen_reloadAllData_fn() {
        console.log("重新加载所有数据", " at pages/weekly/weekly.uvue:387")
        try {
            this.initData()
            this.loadUserInfo()
            this.loadStatistics()
            this.loadRecentRecords()
            console.log("数据加载完成", " at pages/weekly/weekly.uvue:400")
        }
         catch (error: Throwable) {
            console.error("数据加载失败:", error, " at pages/weekly/weekly.uvue:403")
        }
         finally {
            setTimeout(fun(){
                console.log("关闭刷新动画", " at pages/weekly/weekly.uvue:407")
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
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("container" to _pS(_uM("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "150rpx", "paddingLeft" to "30rpx", "backgroundImage" to "linear-gradient(180deg, #eff6ff 0%, #dbeafe 100%)", "backgroundColor" to "rgba(0,0,0,0)", "boxSizing" to "border-box", "minHeight" to "1200rpx")), "header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "30rpx", "paddingRight" to "25rpx", "paddingBottom" to "30rpx", "paddingLeft" to "25rpx", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "marginBottom" to "30rpx", "boxShadow" to "0 12rpx 36rpx rgba(30, 64, 175, 0.18)", "position" to "relative", "overflow" to "hidden", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "width::before" to "8rpx", "height::before" to "100%", "backgroundImage::before" to "linear-gradient(180deg, #1e40af, #0ea5e9)", "backgroundColor::before" to "rgba(0,0,0,0)")), "welcome" to _pS(_uM("display" to "flex", "flexDirection" to "column", "flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "zIndex" to 2, "position" to "relative", "paddingLeft" to "12rpx")), "greeting" to _pS(_uM("color" to "#0a2540", "fontSize" to "36rpx", "fontWeight" to 700, "marginBottom" to "10rpx", "letterSpacing" to "1rpx")), "app-name" to _pS(_uM("color" to "#475569", "fontSize" to "26rpx", "fontWeight" to 500, "letterSpacing" to "0.3rpx")), "avatar" to _pS(_uM("width" to "100rpx", "height" to "100rpx", "borderTopLeftRadius" to "50rpx", "borderTopRightRadius" to "50rpx", "borderBottomRightRadius" to "50rpx", "borderBottomLeftRadius" to "50rpx", "backgroundColor" to "#eff6ff", "overflow" to "hidden", "borderTopWidth" to "3rpx", "borderRightWidth" to "3rpx", "borderBottomWidth" to "3rpx", "borderLeftWidth" to "3rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "boxShadow" to "0 6rpx 20rpx rgba(30, 64, 175, 0.12)", "zIndex" to 2, "position" to "relative", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "transform:active" to "scale(0.95)")), "stats-container" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "marginBottom" to "30rpx")), "stats-card" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "backgroundImage" to "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "32rpx", "paddingRight" to "20rpx", "paddingBottom" to "32rpx", "paddingLeft" to "20rpx", "textAlign" to "center", "position" to "relative", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "overflow" to "hidden", "marginTop" to 0, "marginRight" to "8rpx", "marginBottom" to 0, "marginLeft" to "8rpx", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(255,255,255,0.8)", "borderRightColor" to "rgba(255,255,255,0.8)", "borderBottomColor" to "rgba(255,255,255,0.8)", "borderLeftColor" to "rgba(255,255,255,0.8)", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "right::before" to 0, "height::before" to "4rpx", "backgroundImage::before" to "linear-gradient(90deg, #1e40af, #0ea5e9)", "backgroundColor::before" to "rgba(0,0,0,0)")), "stats-number" to _pS(_uM("fontSize" to "48rpx", "fontWeight" to 700, "color" to "#0a2540", "marginBottom" to "8rpx")), "stats-label" to _pS(_uM("fontSize" to "26rpx", "color" to "#1e293b", "fontWeight" to 600, "marginBottom" to "8rpx")), "stats-icon" to _pS(_uM("fontSize" to "36rpx", "position" to "absolute", "top" to "20rpx", "right" to "20rpx", "opacity" to 0.18)), "action-buttons" to _pS(_uM("marginBottom" to "30rpx", "display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between")), "action-button" to _uM(".action-buttons " to _uM("marginTop" to 0, "marginRight" to "8rpx", "marginBottom" to 0, "marginLeft" to "8rpx"), "" to _uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "36rpx", "paddingRight" to "16rpx", "paddingBottom" to "36rpx", "paddingLeft" to "16rpx", "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.12)", "minWidth" to 0, "transitionProperty" to "transform", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "position" to "relative", "overflow" to "hidden", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "right::before" to 0, "height::before" to "6rpx", "backgroundImage::before" to "linear-gradient(90deg, #1e40af, #0ea5e9)", "backgroundColor::before" to "rgba(0,0,0,0)", "transform:active" to "scale(0.97)")), "delayed-button" to _pS(_uM("!backgroundImage" to "none", "!backgroundColor" to "#fffbeb", "!borderTopColor" to "#fde68a", "!borderRightColor" to "#fde68a", "!borderBottomColor" to "#fde68a", "!borderLeftColor" to "#fde68a", "!backgroundImage::before" to "linear-gradient(90deg, #b45309, #f59e0b)", "!backgroundColor::before" to "rgba(0,0,0,0)")), "button-icon" to _uM("" to _uM("fontSize" to "44rpx", "marginBottom" to "14rpx", "color" to "#1e40af", "flexShrink" to 0), ".delayed-button " to _uM("color" to "#b45309")), "button-text" to _uM("" to _uM("fontSize" to "32rpx", "color" to "#0a2540", "fontWeight" to 700, "letterSpacing" to "1rpx"), ".delayed-button " to _uM("color" to "#7c2d12")), "button-text-sub" to _uM("" to _uM("fontSize" to "22rpx", "color" to "#475569", "fontWeight" to 500, "marginTop" to "6rpx", "letterSpacing" to "0.5rpx"), ".delayed-button " to _uM("color" to "#92400e")), "recent-records" to _pS(_uM("marginBottom" to "20rpx")), "section-title" to _pS(_uM("fontSize" to "18rpx", "fontWeight" to 600, "color" to "#0f172a", "marginBottom" to "15rpx", "flexDirection" to "column")), "section-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to "15rpx")), "more-text" to _pS(_uM("fontSize" to "14rpx", "color" to "#2563eb")), "records-list" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "paddingTop" to "15rpx", "paddingRight" to "15rpx", "paddingBottom" to "15rpx", "paddingLeft" to "15rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe")), "record-item" to _uM("" to _uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "15rpx", "paddingRight" to 0, "paddingBottom" to "15rpx", "paddingLeft" to 0, "borderBottomWidth" to "1rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#eff6ff", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000"), ".records-list " to _uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "paddingTop" to "15rpx", "paddingRight" to "15rpx", "paddingBottom" to "15rpx", "paddingLeft" to "15rpx", "marginBottom" to "10rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe")), "record-info" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%")), "record-title" to _pS(_uM("fontSize" to "16rpx", "color" to "#0f172a", "flexDirection" to "column", "marginBottom" to "4rpx")), "record-meta" to _pS(_uM("fontSize" to "12rpx", "color" to "#94a3b8", "flexDirection" to "column")), "record-status" to _pS(_uM("paddingTop" to "4rpx", "paddingRight" to "8rpx", "paddingBottom" to "4rpx", "paddingLeft" to "8rpx", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "fontSize" to "12rpx")), "status-pending" to _pS(_uM("backgroundColor" to "#fef3c7", "color" to "#b45309")), "status-processing" to _pS(_uM("backgroundColor" to "#dbeafe", "color" to "#1e40af")), "status-completed" to _pS(_uM("backgroundColor" to "#dcfce7", "color" to "#15803d")), "status-accepted" to _pS(_uM("backgroundColor" to "#e2e8f0", "color" to "#475569")), "status-unknown" to _pS(_uM("backgroundColor" to "#f1f5f9", "color" to "#94a3b8")), "empty-state" to _pS(_uM("textAlign" to "center", "paddingTop" to "40rpx", "paddingRight" to "20rpx", "paddingBottom" to "40rpx", "paddingLeft" to "20rpx")), "recent-section" to _pS(_uM("marginTop" to "20rpx", "marginRight" to "20rpx", "marginBottom" to "20rpx", "marginLeft" to "20rpx")), "view-all" to _pS(_uM("fontSize" to "14rpx", "color" to "#2563eb")), "empty-records" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "paddingTop" to "40rpx", "paddingRight" to "20rpx", "paddingBottom" to "40rpx", "paddingLeft" to "20rpx", "textAlign" to "center", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe")), "empty-text" to _pS(_uM("fontSize" to "14rpx", "color" to "#94a3b8")), "record-content" to _pS(_uM("display" to "flex", "flexDirection" to "column")), "problem-desc" to _uM(".record-content " to _uM("marginTop" to "8rpx"), "" to _uM("fontSize" to "16rpx", "color" to "#0f172a", "fontWeight" to 500, "lineHeight" to 1.4)), "location-name" to _pS(_uM("fontSize" to "14rpx", "color" to "#475569")), "record-footer" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginTop" to "5rpx")), "create-time" to _pS(_uM("fontSize" to "12rpx", "color" to "#94a3b8")), "status-tag" to _pS(_uM("paddingTop" to "4rpx", "paddingRight" to "8rpx", "paddingBottom" to "4rpx", "paddingLeft" to "8rpx", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "fontSize" to "12rpx")), "status-text" to _pS(_uM("color" to "#ffffff", "fontWeight" to 500)), "@TRANSITION" to _uM("avatar" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease"), "action-button" to _uM("property" to "transform", "duration" to "0.2s", "timingFunction" to "ease")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
