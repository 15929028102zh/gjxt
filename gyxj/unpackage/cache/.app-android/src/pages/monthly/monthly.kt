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
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.switchTab as uni_switchTab
open class GenPagesMonthlyMonthly : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: OnLoadOptions) {
            this.checkLogin()
            this.initData()
            this.initContainerHeight()
            this.loadStatistics()
            this.loadMonthlyRecords()
            this.loadNotifications()
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
                    _cE("text", _uM("class" to "app-name"), _tD(_ctx.currentDate), 1),
                    _cE("view", _uM("class" to "notification-icon-wrapper", "onClick" to _ctx.showNotifications), _uA(
                        _cE("text", _uM("class" to "icon-bell"), "🔔"),
                        if (isTrue(_ctx.notificationCount > 0 && _ctx.notificationCount <= 99)) {
                            _cE("view", _uM("key" to 0, "class" to "notification-badge"), _uA(
                                _cE("text", _uM("class" to "badge-text"), _tD(_ctx.notificationCount), 1)
                            ))
                        } else {
                            if (_ctx.notificationCount > 99) {
                                _cE("view", _uM("key" to 1, "class" to "notification-badge"), _uA(
                                    _cE("text", _uM("class" to "badge-text"), "99+")
                                ))
                            } else {
                                _cC("v-if", true)
                            }
                        }
                    ), 8, _uA(
                        "onClick"
                    ))
                )),
                _cE("view", _uM("class" to "header-right"), _uA(
                    _cE("view", _uM("class" to "user-avatar", "onClick" to _ctx.goToProfile), _uA(
                        if (isTrue(_ctx.userInfo != null && (_ctx.userInfo.getString("avatar") ?: "") != "")) {
                            _cE("image", _uM("key" to 0, "class" to "avatar-image", "src" to (_ctx.userInfo.getString("avatar") ?: ""), "mode" to "aspectFill"), null, 8, _uA(
                                "src"
                            ))
                        } else {
                            _cE("text", _uM("key" to 1, "class" to "avatar-placeholder"), _tD(_ctx.userName.charAt(0)), 1)
                        }
                    ), 8, _uA(
                        "onClick"
                    ))
                ))
            )),
            _cE("view", _uM("class" to "action-buttons"), _uA(
                _cE("view", _uM("class" to "action-button", "onClick" to _ctx.goToAddRecord), _uA(
                    _cE("view", _uM("class" to "button-icon"), "➕"),
                    _cE("text", null, "月检"),
                    _cE("text", _uM("class" to "button-text"), "每月安全检查")
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
                    _cE("text", _uM("class" to "stats-number"), _tD(_ctx.statistics.monthlyPending), 1),
                    _cE("text", _uM("class" to "stats-label"), "待整改"),
                    _cE("text", _uM("class" to "stats-icon"), "⚠️")
                ), 8, _uA(
                    "onClick"
                )),
                _cE("view", _uM("class" to "stats-card", "onClick" to _ctx.goToCompletedIssues), _uA(
                    _cE("text", _uM("class" to "stats-number"), _tD(_ctx.statistics.monthlyCompleted), 1),
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
            )),
            if (isTrue(_ctx.showNotificationModal)) {
                _cE("view", _uM("key" to 0, "class" to "notification-modal", "onClick" to _ctx.hideNotifications), _uA(
                    _cE("view", _uM("class" to "notification-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "notification-header"), _uA(
                            _cE("text", _uM("class" to "notification-title"), "通知消息"),
                            _cE("view", _uM("class" to "notification-actions"), _uA(
                                _cE("text", _uM("class" to "mark-all-read", "onClick" to _ctx.markAllAsRead), "全部已读", 8, _uA(
                                    "onClick"
                                ))
                            ))
                        )),
                        _cE("view", _uM("class" to "notification-list"), _uA(
                            if (_ctx.notifications.length === 0) {
                                _cE("view", _uM("key" to 0, "class" to "empty-notifications"), _uA(
                                    _cE("text", _uM("class" to "empty-text"), "暂无通知")
                                ))
                            } else {
                                _cC("v-if", true)
                            },
                            _cE(Fragment, null, RenderHelpers.renderList(_ctx.notifications, fun(notification, __key, __index, _cached): Any {
                                return _cE("view", _uM("key" to notification.id, "class" to _nC(_uA(
                                    "notification-item",
                                    _uM("unread" to !notification.isRead)
                                )), "onClick" to fun(){
                                    _ctx.handleNotificationClick(notification)
                                }), _uA(
                                    if (isTrue(!notification.isRead)) {
                                        _cE("view", _uM("key" to 0, "class" to "notification-dot"))
                                    } else {
                                        _cC("v-if", true)
                                    },
                                    _cE("view", _uM("class" to "notification-info"), _uA(
                                        _cE("text", _uM("class" to "notification-item-title"), _tD(notification.title), 1),
                                        _cE("text", _uM("class" to "notification-item-content"), _tD(notification.content), 1),
                                        _cE("text", _uM("class" to "notification-time"), _tD(notification.createTime), 1)
                                    )),
                                    _cE("text", _uM("class" to "notification-arrow"), "›")
                                ), 10, _uA(
                                    "onClick"
                                ))
                            }), 128)
                        ))
                    ), 8, _uA(
                        "onClick"
                    ))
                ), 8, _uA(
                    "onClick"
                ))
            } else {
                _cC("v-if", true)
            }
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
    open var statistics: MonthlyStatistics by `$data`
    open var monthlyRecords: UTSArray<MonthlyRecord> by `$data`
    open var notifications: UTSArray<Notification> by `$data`
    open var notificationCount: Number by `$data`
    open var showNotificationModal: Boolean by `$data`
    open var scrollTop: Number by `$data`
    open var containerHeight: Number by `$data`
    open var isRefreshing: Boolean by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("userInfo" to UTSJSONObject(), "userName" to "用户", "currentDate" to "", "statistics" to MonthlyStatistics(monthlyPending = 0, monthlyCompleted = 0, reviewedCount = 0), "monthlyRecords" to _uA<MonthlyRecord>(), "notifications" to _uA<Notification>(), "notificationCount" to 0, "showNotificationModal" to false, "scrollTop" to 0, "containerHeight" to 0, "isRefreshing" to false)
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
            this.userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/monthly.uvue:176") as UTSJSONObject
            val realName = this.userInfo.getString("realName") ?: ""
            this.userName = if (realName != "") {
                realName
            } else {
                "用户"
            }
        } else {
            this.userInfo = UTSJSONObject()
            this.userName = "用户"
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
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/monthly.uvue:207") as UTSJSONObject
                departmentId = parsedUserInfo.getString("deptId") ?: ""
            }
             catch (error: Throwable) {
                console.error("用户信息JSON解析失败:", error, " at pages/monthly/monthly.uvue:210")
            }
        }
        val deptParam = if (departmentId != null && departmentId != "") {
            "?departmentId=" + departmentId
        } else {
            ""
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/statistics/overview" + deptParam), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                this.statistics = MonthlyStatistics(monthlyPending = data.getNumber("totalTasks") ?: 0, monthlyCompleted = data.getNumber("todayRecords") ?: 0, reviewedCount = data.getNumber("monthRecords") ?: 0)
            }
        }
        , fail = fun(error){
            console.log("加载统计数据失败:", error, " at pages/monthly/monthly.uvue:236")
        }
        ))
    }
    open var loadMonthlyRecords = ::gen_loadMonthlyRecords_fn
    open fun gen_loadMonthlyRecords_fn() {
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
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/monthly.uvue:250") as UTSJSONObject
                departmentId = parsedUserInfo.getString("deptId") ?: ""
            }
             catch (error: Throwable) {
                console.error("用户信息JSON解析失败:", error, " at pages/monthly/monthly.uvue:253")
            }
        }
        val deptParam = if (departmentId != null && departmentId != "") {
            "&departmentId=" + departmentId
        } else {
            ""
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record/page?pageNum=1&pageSize=10" + deptParam), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                val records = data.getArray("records") as UTSArray<UTSJSONObject>
                this.monthlyRecords = _uA()
                run {
                    var i: Number = 0
                    while(i < records.length){
                        val item = records[i]
                        val record = MonthlyRecord(id = item.getString("id") ?: "", problemDescription = item.getString("problemDescription") ?: "", locationName = item.getString("locationName") ?: "", createTime = item.getString("createTime") ?: "", status = item.getString("status") ?: "")
                        this.monthlyRecords.push(record)
                        i++
                    }
                }
            }
        }
        , fail = fun(error){
            console.log("加载月度记录失败:", error, " at pages/monthly/monthly.uvue:286")
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
    open var goToReviewedRecords = ::gen_goToReviewedRecords_fn
    open fun gen_goToReviewedRecords_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?status=2"))
    }
    open var goToMonthlyReport = ::gen_goToMonthlyReport_fn
    open fun gen_goToMonthlyReport_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/statistics/statistics"))
    }
    open var goToDelayedRecord = ::gen_goToDelayedRecord_fn
    open fun gen_goToDelayedRecord_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/delayed"))
    }
    open var goToRecords = ::gen_goToRecords_fn
    open fun gen_goToRecords_fn() {
        uni_switchTab(SwitchTabOptions(url = "/pages/record/record"))
    }
    open var goToAddRecord = ::gen_goToAddRecord_fn
    open fun gen_goToAddRecord_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/add"))
    }
    open var loadNotifications = ::gen_loadNotifications_fn
    open fun gen_loadNotifications_fn() {
        val token = uni_getStorageSync("token")
        val userId = this.userInfo.getString("id") ?: ""
        console.log("开始加载通知，用户ID:", userId, " at pages/monthly/monthly.uvue:350")
        if (userId == null || userId == "") {
            console.log("用户ID为空，无法加载通知", " at pages/monthly/monthly.uvue:353")
            return
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-task/user/" + userId), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            console.log("通知请求响应:", res, " at pages/monthly/monthly.uvue:364")
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getArray("data") as UTSArray<UTSJSONObject>
                console.log("获取到的通知数据:", data.length, "条", " at pages/monthly/monthly.uvue:368")
                this.notifications = _uA()
                var unreadCount: Number = 0
                run {
                    var i: Number = 0
                    while(i < data.length){
                        val item = data[i]
                        val notification = Notification(id = item.getString("id") ?: "", taskId = item.getString("taskId") ?: "", taskNo = item.getString("taskNo") ?: "", title = "\u6708\u68C0\u4EFB\u52A1\u901A\u77E5 - " + item.getString("taskNo"), content = "\u60A8\u6709\u65B0\u7684\u6708\u68C0\u4EFB\u52A1\u9700\u8981\u5904\u7406\uFF0C\u4EFB\u52A1\u7F16\u53F7\uFF1A" + item.getString("taskNo"), createTime = item.getString("createTime") ?: "", isRead = false)
                        this.notifications.push(notification)
                        if (!notification.isRead) {
                            unreadCount++
                        }
                        i++
                    }
                }
                this.notificationCount = unreadCount
                console.log("设置通知条数:", this.notificationCount, " at pages/monthly/monthly.uvue:388")
            } else {
                console.log("通知请求失败，响应码:", res.statusCode, "业务码:", response.getNumber("code"), " at pages/monthly/monthly.uvue:390")
            }
        }
        , fail = fun(error){
            console.log("加载通知失败:", error, " at pages/monthly/monthly.uvue:394")
        }
        ))
    }
    open var showNotifications = ::gen_showNotifications_fn
    open fun gen_showNotifications_fn() {
        this.showNotificationModal = true
    }
    open var hideNotifications = ::gen_hideNotifications_fn
    open fun gen_hideNotifications_fn() {
        this.showNotificationModal = false
    }
    open var handleNotificationClick = ::gen_handleNotificationClick_fn
    open fun gen_handleNotificationClick_fn(notification: Notification) {
        notification.isRead = true
        this.updateNotificationCount()
        uni_navigateTo(NavigateToOptions(url = "/pages/monthly/task-record?taskId=" + notification.taskNo))
        this.hideNotifications()
    }
    open var updateNotificationCount = ::gen_updateNotificationCount_fn
    open fun gen_updateNotificationCount_fn() {
        var unreadCount: Number = 0
        run {
            var i: Number = 0
            while(i < this.notifications.length){
                if (!this.notifications[i].isRead) {
                    unreadCount++
                }
                i++
            }
        }
        this.notificationCount = unreadCount
    }
    open var goToProfile = ::gen_goToProfile_fn
    open fun gen_goToProfile_fn() {
        uni_switchTab(SwitchTabOptions(url = "/pages/profile/profile"))
    }
    open var markAllAsRead = ::gen_markAllAsRead_fn
    open fun gen_markAllAsRead_fn() {
        run {
            var i: Number = 0
            while(i < this.notifications.length){
                this.notifications[i].isRead = true
                i++
            }
        }
        this.notificationCount = 0
    }
    open var initContainerHeight = ::gen_initContainerHeight_fn
    open fun gen_initContainerHeight_fn() {
        val systemInfo = uni_getSystemInfoSync()
        this.containerHeight = systemInfo.windowHeight
    }
    open var onRefresh = ::gen_onRefresh_fn
    open fun gen_onRefresh_fn() {
        this.isRefreshing = true
        this.loadStatistics()
        this.loadMonthlyRecords()
        this.loadNotifications()
        setTimeout(fun(){
            this.isRefreshing = false
        }
        , 1000)
    }
    open var onRefreshRestore = ::gen_onRefreshRestore_fn
    open fun gen_onRefreshRestore_fn() {
        this.isRefreshing = false
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
                return _uM("container" to _pS(_uM("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "150rpx", "paddingLeft" to "30rpx", "backgroundImage" to "linear-gradient(180deg, #f8fafc 0%, #e2e8f0 100%)", "backgroundColor" to "rgba(0,0,0,0)", "boxSizing" to "border-box")), "header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "30rpx", "paddingRight" to "25rpx", "paddingBottom" to "30rpx", "paddingLeft" to "25rpx", "backgroundImage" to "linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "marginBottom" to "30rpx", "boxShadow" to "0 8rpx 32rpx rgba(102, 126, 234, 0.3)", "position" to "relative", "overflow" to "hidden", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "right::before" to 0, "bottom::before" to 0, "pointerEvents::before" to "none")), "header-right" to _pS(_uM("display" to "flex", "alignItems" to "flex-start", "gap" to "15rpx", "zIndex" to 2, "position" to "relative", "marginTop" to "30rpx")), "welcome" to _pS(_uM("display" to "flex", "flexDirection" to "column", "flex" to 1, "zIndex" to 2, "position" to "relative", "paddingRight" to "140rpx")), "greeting" to _pS(_uM("color" to "rgba(105,72,72,0.95)", "fontSize" to "32rpx", "marginBottom" to "8rpx", "letterSpacing" to "0.5rpx", "textShadow" to "0 2rpx 4rpx rgba(0, 0, 0, 0.1)")), "app-name" to _pS(_uM("color" to "rgba(105,72,72,0.95)", "fontSize" to "26rpx", "fontWeight" to "400", "letterSpacing" to "0.3rpx")), "notification-icon-wrapper" to _pS(_uM("position" to "absolute", "top" to "15rpx", "right" to "15rpx", "width" to "70rpx", "height" to "70rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.2)", "backdropFilter" to "blur(10rpx)", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(255,255,255,0.3)", "borderRightColor" to "rgba(255,255,255,0.3)", "borderBottomColor" to "rgba(255,255,255,0.3)", "borderLeftColor" to "rgba(255,255,255,0.3)", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "zIndex" to 3, "transform:active" to "scale(0.95)", "backgroundImage:active" to "none", "backgroundColor:active" to "rgba(255,255,255,0.3)")), "icon-bell" to _pS(_uM("fontSize" to "32rpx", "color" to "rgba(255,255,255,0.9)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "width" to "100%", "height" to "100%")), "notification-badge" to _pS(_uM("position" to "absolute", "top" to "-6rpx", "right" to "-6rpx", "minWidth" to "32rpx", "height" to "32rpx", "backgroundImage" to "none", "backgroundColor" to "#ff4757", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "borderTopWidth" to "3rpx", "borderRightWidth" to "3rpx", "borderBottomWidth" to "3rpx", "borderLeftWidth" to "3rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#ffffff", "borderRightColor" to "#ffffff", "borderBottomColor" to "#ffffff", "borderLeftColor" to "#ffffff", "boxShadow" to "0 2rpx 8rpx rgba(255, 71, 87, 0.3)", "zIndex" to 4, "paddingTop" to 0, "paddingRight" to "6rpx", "paddingBottom" to 0, "paddingLeft" to "6rpx")), "badge-text" to _pS(_uM("fontSize" to "18rpx", "color" to "#ffffff", "fontWeight" to "700", "lineHeight" to 1, "textAlign" to "center", "whiteSpace" to "nowrap")), "user-avatar" to _pS(_uM("width" to "100rpx", "height" to "100rpx", "borderTopLeftRadius" to "50rpx", "borderTopRightRadius" to "50rpx", "borderBottomRightRadius" to "50rpx", "borderBottomLeftRadius" to "50rpx", "backgroundColor" to "rgba(255,255,255,0.2)", "overflow" to "hidden", "borderTopWidth" to "3rpx", "borderRightWidth" to "3rpx", "borderBottomWidth" to "3rpx", "borderLeftWidth" to "3rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(255,255,255,0.4)", "borderRightColor" to "rgba(255,255,255,0.4)", "borderBottomColor" to "rgba(255,255,255,0.4)", "borderLeftColor" to "rgba(255,255,255,0.4)", "boxShadow" to "0 6rpx 20rpx rgba(0, 0, 0, 0.15), 0 0 0 1rpx rgba(255, 255, 255, 0.1)", "zIndex" to 2, "position" to "relative", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "transform:active" to "scale(0.95)")), "avatar-image" to _pS(_uM("width" to "100rpx", "height" to "100rpx", "borderTopLeftRadius" to "50rpx", "borderTopRightRadius" to "50rpx", "borderBottomRightRadius" to "50rpx", "borderBottomLeftRadius" to "50rpx")), "avatar-placeholder" to _pS(_uM("color" to "rgba(255,255,255,0.9)", "fontSize" to "28rpx", "textTransform" to "uppercase")), "avatar" to _pS(_uM("width" to 50, "height" to 50, "borderTopLeftRadius" to 25, "borderTopRightRadius" to 25, "borderBottomRightRadius" to 25, "borderBottomLeftRadius" to 25, "backgroundColor" to "#dddddd")), "stats-container" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "marginBottom" to 30, "gap" to "10px")), "stats-card" to _pS(_uM("flex" to 1, "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 15, "paddingBottom" to 20, "paddingLeft" to 15, "textAlign" to "center", "position" to "relative", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)", "overflow" to "hidden")), "stats-number" to _pS(_uM("fontSize" to 24, "fontWeight" to "bold", "color" to "#333333", "marginBottom" to 8)), "stats-label" to _pS(_uM("fontSize" to 14, "color" to "#666666", "marginBottom" to 8)), "stats-icon" to _pS(_uM("fontSize" to 20, "position" to "absolute", "top" to 15, "right" to 15, "opacity" to 0.3)), "action-buttons" to _pS(_uM("marginBottom" to 30, "display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between", "gap" to "10px")), "action-button" to _pS(_uM("flex" to 1, "backgroundImage" to "none", "backgroundColor" to "#d1ecf1", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 15, "paddingRight" to 10, "paddingBottom" to 15, "paddingLeft" to 10, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)", "minWidth" to 0)), "delayed-button" to _pS(_uM("!backgroundImage" to "none", "!backgroundColor" to "#ff4757")), "button-icon" to _pS(_uM("fontSize" to 20, "marginRight" to 8, "color" to "#007AFF", "flexShrink" to 0)), "button-text" to _pS(_uM("fontSize" to 14, "color" to "#333333", "whiteSpace" to "nowrap", "overflow" to "hidden", "textOverflow" to "ellipsis")), "trend-section" to _pS(_uM("marginBottom" to 30)), "trend-chart" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "chart-placeholder" to _pS(_uM("height" to 200, "display" to "flex", "flexDirection" to "column", "justifyContent" to "center", "alignItems" to "center", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#dee2e6", "borderRightColor" to "#dee2e6", "borderBottomColor" to "#dee2e6", "borderLeftColor" to "#dee2e6")), "placeholder-text" to _pS(_uM("fontSize" to 18, "color" to "#6c757d", "marginBottom" to 8)), "placeholder-desc" to _pS(_uM("fontSize" to 14, "color" to "#adb5bd")), "quick-actions" to _pS(_uM("marginBottom" to 30)), "section-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333", "marginBottom" to 15)), "actions-grid" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "justifyContent" to "space-between")), "action-item" to _pS(_uM("width" to "48%", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "marginBottom" to 15, "textAlign" to "center", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "action-icon" to _pS(_uM("fontSize" to 24, "marginBottom" to 8)), "action-text" to _pS(_uM("fontSize" to 14, "color" to "#333333")), "recent-records" to _pS(_uM("marginBottom" to 20)), "section-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to 15)), "more-text" to _pS(_uM("fontSize" to 14, "color" to "#007AFF")), "records-list" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 15, "paddingRight" to 15, "paddingBottom" to 15, "paddingLeft" to 15, "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "record-item" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 0, "paddingBottom" to 15, "paddingLeft" to 0, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "record-info" to _pS(_uM("flex" to 1)), "record-title" to _pS(_uM("fontSize" to 16, "color" to "#333333", "marginBottom" to 4)), "record-meta" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "record-status" to _pS(_uM("paddingTop" to 4, "paddingRight" to 8, "paddingBottom" to 4, "paddingLeft" to 8, "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "fontSize" to 12)), "status-pending" to _pS(_uM("backgroundColor" to "#fff3cd", "color" to "#856404")), "status-processing" to _pS(_uM("backgroundColor" to "#d1ecf1", "color" to "#0c5460")), "status-completed" to _pS(_uM("backgroundColor" to "#d4edda", "color" to "#155724")), "status-accepted" to _pS(_uM("backgroundColor" to "#e2e3e5", "color" to "#383d41")), "status-unknown" to _pS(_uM("backgroundColor" to "#f8f9fa", "color" to "#6c757d")), "empty-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20)), "empty-text" to _pS(_uM("fontSize" to 14, "color" to "#999999")), "notification-modal" to _pS(_uM("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundColor" to "rgba(0,0,0,0.5)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "zIndex" to 1000)), "notification-content" to _pS(_uM("width" to "90%", "maxWidth" to 400, "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "overflow" to "hidden")), "notification-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0")), "notification-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333")), "notification-actions" to _pS(_uM("display" to "flex", "alignItems" to "center", "gap" to "15px")), "mark-all-read" to _pS(_uM("fontSize" to 14, "color" to "#007AFF")), "close-btn" to _pS(_uM("fontSize" to 20, "color" to "#999999", "width" to 30, "height" to 30, "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "notification-list" to _pS(_uM("maxHeight" to 400, "overflowY" to "auto")), "empty-notifications" to _pS(_uM("paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20, "textAlign" to "center")), "notification-item" to _uM("" to _uM("display" to "flex", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "position" to "relative", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000"), ".unread" to _uM("backgroundColor" to "#f8f9ff")), "notification-dot" to _pS(_uM("width" to 8, "height" to 8, "backgroundColor" to "#007AFF", "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "marginRight" to 12, "flexShrink" to 0)), "notification-info" to _pS(_uM("flex" to 1, "marginRight" to 10)), "notification-item-title" to _pS(_uM("fontSize" to 16, "color" to "#333333", "marginBottom" to 4)), "notification-item-content" to _pS(_uM("fontSize" to 14, "color" to "#666666", "marginBottom" to 4, "lineHeight" to 1.4)), "notification-time" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "notification-arrow" to _pS(_uM("fontSize" to 18, "color" to "#cccccc", "flexShrink" to 0)), "@TRANSITION" to _uM("notification-icon-wrapper" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease"), "user-avatar" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
