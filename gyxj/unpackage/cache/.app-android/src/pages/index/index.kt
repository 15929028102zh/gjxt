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
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.switchTab as uni_switchTab
open class GenPagesIndexIndex : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: OnLoadOptions) {
            this.checkLogin()
            this.initData()
            refreshUserInfo().then(fun(){
                val userInfoStr = uni_getStorageSync("userInfo") as String
                if (userInfoStr != null && userInfoStr != "") {
                    this.userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/index/index.uvue:126") as UTSJSONObject
                }
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
        return _cE("view", _uM("class" to "container"), _uA(
            _cE("view", _uM("class" to "header"), _uA(
                _cE("view", _uM("class" to "welcome"), _uA(
                    _cE("text", _uM("class" to "welcome-text"), "欢迎回来，" + _tD((_ctx.userInfo as UTSJSONObject).getString("realName") ?: "用户"), 1),
                    _cE("text", _uM("class" to "date-text"), _tD(_ctx.currentDate), 1)
                )),
                _cE("image", _uM("class" to "avatar", "src" to "/static/avatar.png"))
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
                    _cE("text", _uM("class" to "stats-number"), _tD(_ctx.statistics.monthRecords), 1),
                    _cE("text", _uM("class" to "stats-label"), "已复查"),
                    _cE("text", _uM("class" to "stats-icon"), "📊")
                ), 8, _uA(
                    "onClick"
                ))
            )),
            _cE("view", _uM("class" to "quick-actions"), _uA(
                _cE("text", _uM("class" to "section-title"), "快捷功能"),
                _cE("view", _uM("class" to "actions-grid"), _uA(
                    _cE("view", _uM("class" to "action-item", "onClick" to _ctx.goToAddRecord), _uA(
                        _cE("view", _uM("class" to "action-icon"), "➕"),
                        _cE("text", _uM("class" to "action-text"), "新增记录")
                    ), 8, _uA(
                        "onClick"
                    )),
                    _cE("view", _uM("class" to "action-item", "onClick" to _ctx.goToPendingIssues), _uA(
                        _cE("view", _uM("class" to "action-icon"), "⚠️"),
                        _cE("text", _uM("class" to "action-text"), "待整改问题")
                    ), 8, _uA(
                        "onClick"
                    )),
                    _cE("view", _uM("class" to "action-item", "onClick" to _ctx.goToRecords), _uA(
                        _cE("view", _uM("class" to "action-icon"), "📝"),
                        _cE("text", _uM("class" to "action-text"), "巡检记录")
                    ), 8, _uA(
                        "onClick"
                    )),
                    _cE("view", _uM("class" to "action-item", "onClick" to _ctx.goToCompletedIssues), _uA(
                        _cE("view", _uM("class" to "action-icon"), "✅"),
                        _cE("text", _uM("class" to "action-text"), "已整改问题")
                    ), 8, _uA(
                        "onClick"
                    )),
                    _cE("view", _uM("class" to "action-item", "onClick" to _ctx.goToStatistics), _uA(
                        _cE("view", _uM("class" to "action-icon"), "📊"),
                        _cE("text", _uM("class" to "action-text"), "统计分析")
                    ), 8, _uA(
                        "onClick"
                    ))
                ))
            )),
            _cE("view", _uM("class" to "recent-records"), _uA(
                _cE("view", _uM("class" to "section-header"), _uA(
                    _cE("text", _uM("class" to "section-title"), "最近记录"),
                    _cE("text", _uM("class" to "more-text", "onClick" to _ctx.goToRecords), "查看更多", 8, _uA(
                        "onClick"
                    ))
                )),
                _cE("view", _uM("class" to "records-list"), _uA(
                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.recentRecords, fun(record, __key, __index, _cached): Any {
                        return _cE("view", _uM("class" to "record-item", "key" to record.id), _uA(
                            _cE("view", _uM("class" to "record-info"), _uA(
                                _cE("text", _uM("class" to "record-title"), _tD(record.problemDescription), 1),
                                _cE("text", _uM("class" to "record-meta"), _tD(record.locationName) + " · " + _tD(record.createTime), 1)
                            )),
                            _cE("view", _uM("class" to _nC(_uA(
                                "record-status",
                                _ctx.getStatusClass(record.status)
                            ))), _uA(
                                _cE("text", null, _tD(_ctx.getStatusText(record.status)), 1)
                            ), 2)
                        ))
                    }
                    ), 128),
                    if (_ctx.recentRecords.length === 0) {
                        _cE("view", _uM("key" to 0, "class" to "empty-state"), _uA(
                            _cE("text", _uM("class" to "empty-text"), "暂无记录")
                        ))
                    } else {
                        _cC("v-if", true)
                    }
                ))
            ))
        ))
    }
    open var userInfo: UTSJSONObject by `$data`
    open var currentDate: String by `$data`
    open var statistics: StatisticsData__1 by `$data`
    open var recentRecords: UTSArray<RecentRecord__1> by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("userInfo" to _uO(), "currentDate" to "", "statistics" to StatisticsData__1(pendingIssues = 0, completedIssues = 0, monthRecords = 0), "recentRecords" to _uA<RecentRecord__1>())
    }
    open var checkLogin = ::gen_checkLogin_fn
    open fun gen_checkLogin_fn() {
        val token = uni_getStorageSync("token")
        if (token == null || token == "") {
            uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
            return
        }
        val userInfoStr = uni_getStorageSync("userInfo") as String
        if (userInfoStr != null && userInfoStr != "") {
            this.userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/index/index.uvue:143") as UTSJSONObject
        } else {
            this.userInfo = _uO()
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
        val token = uni_getStorageSync("token")
        var departmentId = ""
        val isAdmin = isMonthlyAdmin()
        val userInfoData = uni_getStorageSync("userInfo")
        val userInfoStr = if (userInfoData != null) {
            userInfoData as String
        } else {
            ""
        }
        if (userInfoStr != null && userInfoStr != "" && userInfoStr != "null" && userInfoStr != "undefined") {
            try {
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/index/index.uvue:172") as UTSJSONObject
                departmentId = parsedUserInfo.getString("deptId") ?: ""
            }
             catch (error: Throwable) {
                console.error("用户信息JSON解析失败:", error, " at pages/index/index.uvue:175")
            }
        }
        val deptParam = if ((!isAdmin && departmentId != null && departmentId != "")) {
            "&departmentId=" + departmentId
        } else {
            ""
        }
        var pendingCount: Number = 0
        var completedCount: Number = 0
        var reviewedCount: Number = 0
        var completedRequests: Number = 0
        val totalRequests: Number = 3
        val updateStatistics = fun(){
            this.statistics = StatisticsData__1(pendingIssues = pendingCount, completedIssues = completedCount, monthRecords = reviewedCount)
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=1&status=0" + deptParam), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                pendingCount = data.getNumber("total") ?: 0
            }
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        , fail = fun(error){
            console.log("加载待整改数据失败:", error, " at pages/index/index.uvue:214")
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        ))
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=1&status=1" + deptParam), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                completedCount = data.getNumber("total") ?: 0
            }
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        , fail = fun(error){
            console.log("加载已整改数据失败:", error, " at pages/index/index.uvue:241")
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        ))
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=1&status=2" + deptParam), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                reviewedCount = data.getNumber("total") ?: 0
            }
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        , fail = fun(error){
            console.log("加载已复查数据失败:", error, " at pages/index/index.uvue:268")
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        ))
    }
    open var loadRecentRecords = ::gen_loadRecentRecords_fn
    open fun gen_loadRecentRecords_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record/page?pageNum=1&pageSize=5"), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                val records = data.getArray("records") as UTSArray<UTSJSONObject>
                this.recentRecords = _uA()
                run {
                    var i: Number = 0
                    while(i < records.length){
                        val item = records[i]
                        val record = RecentRecord__1(id = item.getString("id") ?: "", problemDescription = item.getString("problemDescription") ?: "", locationName = item.getString("locationName") ?: "", createTime = item.getString("createTime") ?: "", status = item.getString("status") ?: "")
                        this.recentRecords.push(record)
                        i++
                    }
                }
            }
        }
        , fail = fun(error){
            console.log("加载最近记录失败:", error, " at pages/index/index.uvue:305")
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
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?status=0&monthly=true"))
    }
    open var goToCompletedIssues = ::gen_goToCompletedIssues_fn
    open fun gen_goToCompletedIssues_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?status=1&monthly=true"))
    }
    open var goToReviewedRecords = ::gen_goToReviewedRecords_fn
    open fun gen_goToReviewedRecords_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?status=2&monthly=true"))
    }
    open var goToRecords = ::gen_goToRecords_fn
    open fun gen_goToRecords_fn() {
        uni_switchTab(SwitchTabOptions(url = "/pages/record/record"))
    }
    open var goToStatistics = ::gen_goToStatistics_fn
    open fun gen_goToStatistics_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/statistics/statistics"))
    }
    open var goToAddRecord = ::gen_goToAddRecord_fn
    open fun gen_goToAddRecord_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/add"))
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("container" to _pS(_uM("paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "backgroundColor" to "#f1f5f9")), "header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to "20rpx", "paddingTop" to "24rpx", "paddingRight" to "20rpx", "paddingBottom" to "24rpx", "paddingLeft" to "20rpx", "backgroundImage" to "linear-gradient(135deg, #1e40af 0%, #2563eb 50%, #38bdf8 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "boxShadow" to "0 8rpx 32rpx rgba(30, 64, 175, 0.24)")), "welcome" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%")), "greeting" to _uM(".welcome-text " to _uM("fontSize" to "20rpx", "fontWeight" to "bold", "color" to "#ffffff", "marginBottom" to "4rpx", "textShadow" to "0 2rpx 4rpx rgba(15, 23, 42, 0.18)")), "date-text" to _pS(_uM("fontSize" to "14rpx", "color" to "rgba(255,255,255,0.85)")), "avatar" to _pS(_uM("width" to "50rpx", "height" to "50rpx", "borderTopLeftRadius" to "25rpx", "borderTopRightRadius" to "25rpx", "borderBottomRightRadius" to "25rpx", "borderBottomLeftRadius" to "25rpx", "backgroundColor" to "rgba(255,255,255,0.25)", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(255,255,255,0.4)", "borderRightColor" to "rgba(255,255,255,0.4)", "borderBottomColor" to "rgba(255,255,255,0.4)", "borderLeftColor" to "rgba(255,255,255,0.4)")), "stats-container" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "marginBottom" to "30rpx")), "stats-card" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "paddingTop" to "20rpx", "paddingRight" to "15rpx", "paddingBottom" to "20rpx", "paddingLeft" to "15rpx", "marginTop" to 0, "marginRight" to "5rpx", "marginBottom" to 0, "marginLeft" to "5rpx", "textAlign" to "center", "position" to "relative", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe")), "stats-number" to _pS(_uM("fontSize" to "24rpx", "fontWeight" to "bold", "color" to "#1e40af", "marginBottom" to "5rpx")), "stats-label" to _pS(_uM("fontSize" to "12rpx", "color" to "#475569")), "stats-icon" to _pS(_uM("position" to "absolute", "top" to "15rpx", "right" to "15rpx", "fontSize" to "20rpx", "opacity" to 0.3)), "quick-actions" to _pS(_uM("marginBottom" to "30rpx")), "section-title" to _pS(_uM("fontSize" to "18rpx", "fontWeight" to "bold", "color" to "#0f172a", "marginBottom" to "15rpx")), "actions-grid" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "justifyContent" to "space-between")), "action-item" to _pS(_uM("width" to "48%", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "marginBottom" to "10rpx", "textAlign" to "center", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe")), "action-icon" to _pS(_uM("fontSize" to "32rpx", "marginBottom" to "10rpx", "color" to "#2563eb")), "action-text" to _pS(_uM("fontSize" to "14rpx", "color" to "#0f172a")), "recent-records" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe")), "section-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to "15rpx")), "more-text" to _pS(_uM("fontSize" to "14rpx", "color" to "#2563eb")), "record-item" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "15rpx", "paddingRight" to 0, "paddingBottom" to "15rpx", "paddingLeft" to 0, "borderBottomWidth" to "1rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#eff6ff", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "record-info" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%")), "record-title" to _pS(_uM("fontSize" to "16rpx", "color" to "#0f172a", "marginBottom" to "5rpx")), "record-meta" to _pS(_uM("fontSize" to "12rpx", "color" to "#94a3b8")), "record-status" to _pS(_uM("paddingTop" to "4rpx", "paddingRight" to "8rpx", "paddingBottom" to "4rpx", "paddingLeft" to "8rpx", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "fontSize" to "12rpx")), "status-pending" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#fef3c7", "color" to "#b45309")), "status-processing" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#dbeafe", "color" to "#1e40af")), "status-completed" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#dcfce7", "color" to "#15803d")), "status-accepted" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#e2e8f0", "color" to "#475569")), "empty-state" to _pS(_uM("textAlign" to "center", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0)), "empty-text" to _pS(_uM("fontSize" to "14rpx", "color" to "#94a3b8")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
