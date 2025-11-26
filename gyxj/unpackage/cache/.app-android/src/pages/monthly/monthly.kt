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
import io.dcloud.uniapp.extapi.closeSocket as uni_closeSocket
import io.dcloud.uniapp.extapi.connectSocket as uni_connectSocket
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfoSync as uni_getSystemInfoSync
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.onSocketClose as uni_onSocketClose
import io.dcloud.uniapp.extapi.onSocketError as uni_onSocketError
import io.dcloud.uniapp.extapi.onSocketMessage as uni_onSocketMessage
import io.dcloud.uniapp.extapi.onSocketOpen as uni_onSocketOpen
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.sendSocketMessage as uni_sendSocketMessage
import io.dcloud.uniapp.extapi.showToast as uni_showToast
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
        onPageShow(fun() {
            console.log("页面显示，连接WebSocket", " at pages/monthly/monthly.uvue:267")
            this.connectWebSocket()
        }
        , __ins)
        onPageHide(fun() {
            console.log("页面隐藏，断开WebSocket", " at pages/monthly/monthly.uvue:273")
            this.disconnectWebSocket()
        }
        , __ins)
        onUnload(fun() {
            console.log("页面卸载，断开WebSocket", " at pages/monthly/monthly.uvue:279")
            this.disconnectWebSocket()
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
                                _cE("text", _uM("class" to "close-btn", "onClick" to _ctx.hideNotifications), "×", 8, _uA(
                                    "onClick"
                                ))
                            ))
                        )),
                        _cE("view", _uM("class" to "notification-tabs"), _uA(
                            _cE("view", _uM("class" to _nC(_uA(
                                "tab-item",
                                _uM("active" to (_ctx.activeNotificationTab === "system"))
                            )), "onClick" to fun(){
                                _ctx.switchNotificationTab("system")
                            }), _uA(
                                _cE("text", _uM("class" to "tab-text"), "系统通知")
                            ), 10, _uA(
                                "onClick"
                            )),
                            _cE("view", _uM("class" to _nC(_uA(
                                "tab-item",
                                _uM("active" to (_ctx.activeNotificationTab === "tasks"))
                            )), "onClick" to fun(){
                                _ctx.switchNotificationTab("tasks")
                            }), _uA(
                                _cE("text", _uM("class" to "tab-text"), "月检任务")
                            ), 10, _uA(
                                "onClick"
                            ))
                        )),
                        if (_ctx.activeNotificationTab === "system") {
                            _cE("view", _uM("key" to 0, "class" to "tab-content"), _uA(
                                _cE("view", _uM("class" to "tab-header"), _uA(
                                    _cE("text", _uM("class" to "mark-all-read", "onClick" to _ctx.markAllAsRead), "全部已读", 8, _uA(
                                        "onClick"
                                    ))
                                )),
                                _cE("scroll-view", _uM("class" to "notification-list", "scroll-y" to "true", "show-scrollbar" to true, "enable-back-to-top" to false), _uA(
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
                            ))
                        } else {
                            _cC("v-if", true)
                        },
                        if (_ctx.activeNotificationTab === "tasks") {
                            _cE("view", _uM("key" to 1, "class" to "tab-content"), _uA(
                                _cE("view", _uM("class" to "tab-header"), _uA(
                                    _cE("text", _uM("class" to "refresh-btn", "onClick" to _ctx.refreshTaskNotifications), "刷新", 8, _uA(
                                        "onClick"
                                    ))
                                )),
                                _cE("scroll-view", _uM("class" to "notification-list", "scroll-y" to "true", "show-scrollbar" to true, "enable-back-to-top" to false), _uA(
                                    if (_ctx.taskNotifications.length === 0) {
                                        _cE("view", _uM("key" to 0, "class" to "empty-notifications"), _uA(
                                            _cE("text", _uM("class" to "empty-text"), "暂无已通知的月检任务")
                                        ))
                                    } else {
                                        _cC("v-if", true)
                                    },
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.taskNotifications, fun(taskNotification, __key, __index, _cached): Any {
                                        return _cE("view", _uM("key" to taskNotification.id, "class" to "notification-item task-notification", "onClick" to fun(){
                                            _ctx.handleTaskNotificationClick(taskNotification)
                                        }), _uA(
                                            _cE("view", _uM("class" to _nC(_uA(
                                                "task-status-badge",
                                                _ctx.getTaskStatusClass(taskNotification.status)
                                            ))), _uA(
                                                _cE("text", _uM("class" to "status-text"), _tD(_ctx.getTaskStatusText(taskNotification.status)), 1)
                                            ), 2),
                                            _cE("view", _uM("class" to "notification-info"), _uA(
                                                _cE("text", _uM("class" to "notification-item-title"), _tD(taskNotification.title), 1),
                                                _cE("text", _uM("class" to "notification-item-content"), " 任务编号：" + _tD(taskNotification.taskNo) + " 月检时间：" + _tD(taskNotification.inspectionMonth) + " 任务状态：" + _tD(_ctx.getTaskStatusText(taskNotification.status)) + " " + _tD(taskNotification.content), 1),
                                                _cE("text", _uM("class" to "notification-time"), _tD(taskNotification.createTime), 1)
                                            )),
                                            _cE("text", _uM("class" to "notification-arrow"), "›")
                                        ), 8, _uA(
                                            "onClick"
                                        ))
                                    }), 128)
                                ))
                            ))
                        } else {
                            _cC("v-if", true)
                        }
                    ), 8, _uA(
                        "onClick"
                    ))
                ), 8, _uA(
                    "onClick"
                ))
            } else {
                _cC("v-if", true)
            }
            ,
            if (isTrue(_ctx.showTaskPickerModal)) {
                _cE("view", _uM("key" to 1, "class" to "task-picker-overlay", "onClick" to _ctx.closeTaskPicker), _uA(
                    _cE("view", _uM("class" to "task-picker", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "task-picker-header"), _uA(
                            _cE("text", null, "选择月检任务"),
                            _cE("text", _uM("class" to "task-picker-close", "onClick" to _ctx.closeTaskPicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "task-picker-body"), _uA(
                            if (isTrue(_ctx.taskPickerLoading)) {
                                _cE("view", _uM("key" to 0, "class" to "empty-tasks"), "加载中...")
                            } else {
                                if (_ctx.taskPickerList.length === 0) {
                                    _cE("view", _uM("key" to 1, "class" to "empty-tasks"), "当前无待执行或进行中的任务")
                                } else {
                                    _cE("view", _uM("key" to 2), _uA(
                                        _cE(Fragment, null, RenderHelpers.renderList(_ctx.taskPickerList, fun(t, __key, __index, _cached): Any {
                                            return _cE("view", _uM("class" to "task-item", "key" to t.getString("id"), "onClick" to fun(){
                                                _ctx.pickTaskAndGo(t)
                                            }), _uA(
                                                _cE("text", _uM("class" to "task-title"), "任务编号：" + _tD(t.getString("taskNo") ?: ""), 1),
                                                _cE("text", _uM("class" to "task-sub"), "月检时间：" + _tD(t.getString("inspectionMonth") ?: "") + " 状态：" + _tD(_ctx.getTaskStatusText(t.getString("status") ?: "")), 1)
                                            ), 8, _uA(
                                                "onClick"
                                            ))
                                        }), 128)
                                    ))
                                }
                            }
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
    open var taskNotifications: UTSArray<TaskNotification> by `$data`
    open var notificationCount: Number by `$data`
    open var showNotificationModal: Boolean by `$data`
    open var activeNotificationTab: String by `$data`
    open var showTaskPickerModal: Boolean by `$data`
    open var taskPickerLoading: Boolean by `$data`
    open var taskPickerList: UTSArray<UTSJSONObject> by `$data`
    open var socketTask: Any? by `$data`
    open var isSocketConnected: Boolean by `$data`
    open var reconnectAttempts: Number by `$data`
    open var maxReconnectAttempts: Number by `$data`
    open var reconnectInterval: Number by `$data`
    open var scrollTop: Number by `$data`
    open var containerHeight: Number by `$data`
    open var isRefreshing: Boolean by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("userInfo" to UTSJSONObject(), "userName" to "用户", "currentDate" to "", "statistics" to MonthlyStatistics(monthlyPending = 0, monthlyCompleted = 0, reviewedCount = 0), "monthlyRecords" to _uA<MonthlyRecord>(), "notifications" to _uA<Notification>(), "taskNotifications" to _uA<TaskNotification>(), "notificationCount" to 0, "showNotificationModal" to false, "activeNotificationTab" to "system", "showTaskPickerModal" to false, "taskPickerLoading" to false, "taskPickerList" to _uA<UTSJSONObject>(), "socketTask" to null as Any?, "isSocketConnected" to false, "reconnectAttempts" to 0, "maxReconnectAttempts" to 5, "reconnectInterval" to 3000, "scrollTop" to 0, "containerHeight" to 0, "isRefreshing" to false)
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
            this.userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/monthly.uvue:293") as UTSJSONObject
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
        var isAdmin = false
        val userInfoData = uni_getStorageSync("userInfo")
        val userInfoStr = if (userInfoData != null) {
            userInfoData as String
        } else {
            ""
        }
        if (userInfoStr != null && userInfoStr != "" && userInfoStr != "null" && userInfoStr != "undefined") {
            try {
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/monthly.uvue:325") as UTSJSONObject
                departmentId = parsedUserInfo.getString("deptId") ?: ""
                isAdmin = (parsedUserInfo.getNumber("isAdmin") ?: 0) === 1
            }
             catch (error: Throwable) {
                console.error("用户信息JSON解析失败:", error, " at pages/monthly/monthly.uvue:329")
            }
        }
        val deptParam = if ((!isAdmin && departmentId != null && departmentId != "")) {
            "&responsibilityId=" + departmentId
        } else {
            ""
        }
        var pendingCount: Number = 0
        var completedCount: Number = 0
        var reviewedCount: Number = 0
        var completedRequests: Number = 0
        val totalRequests: Number = 3
        val updateStatistics = fun(){
            this.statistics = MonthlyStatistics(monthlyPending = pendingCount, monthlyCompleted = completedCount, reviewedCount = reviewedCount)
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=1&recordStatus=0" + deptParam), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
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
            console.log("加载待整改数据失败:", error, " at pages/monthly/monthly.uvue:368")
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        ))
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=1&recordStatus=1" + deptParam), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
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
            console.log("加载已整改数据失败:", error, " at pages/monthly/monthly.uvue:395")
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        ))
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=1&recordStatus=2" + deptParam), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                reviewedCount = data.getNumber("total") ?: 0
            }
            val now = Date()
            val y = now.getFullYear()
            val m = now.getMonth() + 1
            val d = now.getDate()
            val mm = if (m < 10) {
                ("0" + m)
            } else {
                ("" + m)
            }
            val dd = if (d < 10) {
                ("0" + d)
            } else {
                ("" + d)
            }
            val today = "" + y + "-" + mm + "-" + dd
            uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=1&recordStatus=0&rectificationDeadlineBefore=" + today + deptParam), method = "GET", header = object : UTSJSONObject() {
                var Authorization = "Bearer " + token
            }, success = fun(res2){
                val resp2 = res2.data as UTSJSONObject
                if (res2.statusCode == 200 && (resp2.getNumber("code") ?: 0) == 200) {
                    val data2 = resp2.getJSON("data") as UTSJSONObject
                    val delayedCount = data2.getNumber("total") ?: 0
                }
                completedRequests++
                if (completedRequests === totalRequests) {
                    updateStatistics()
                }
            }
            , fail = fun(_){
                completedRequests++
                if (completedRequests === totalRequests) {
                    updateStatistics()
                }
            }
            ))
        }
        , fail = fun(error){
            console.log("加载已复查数据失败:", error, " at pages/monthly/monthly.uvue:450")
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        ))
    }
    open var loadMonthlyRecords = ::gen_loadMonthlyRecords_fn
    open fun gen_loadMonthlyRecords_fn() {
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
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/monthly.uvue:469") as UTSJSONObject
                departmentId = parsedUserInfo.getString("deptId") ?: ""
                isAdmin = (parsedUserInfo.getNumber("isAdmin") ?: 0) === 1
            }
             catch (error: Throwable) {
                console.error("用户信息JSON解析失败:", error, " at pages/monthly/monthly.uvue:473")
            }
        }
        val deptParam = if ((!isAdmin && departmentId != null && departmentId != "")) {
            "&responsibilityId=" + departmentId
        } else {
            ""
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=10" + deptParam), method = "GET", header = object : UTSJSONObject() {
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
            console.log("加载月度记录失败:", error, " at pages/monthly/monthly.uvue:506")
        }
        ))
    }
    open var getStatusText = ::gen_getStatusText_fn
    open fun gen_getStatusText_fn(status: String): String {
        if (status == "0") {
            return "待执行"
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
    open var getTaskStatusText = ::gen_getTaskStatusText_fn
    open fun gen_getTaskStatusText_fn(status: String): String {
        if (status == "0") {
            return "未开始"
        }
        if (status == "1") {
            return "进行中"
        }
        if (status == "2") {
            return "已完成"
        }
        return "未知"
    }
    open var getTaskStatusClass = ::gen_getTaskStatusClass_fn
    open fun gen_getTaskStatusClass_fn(status: String): String {
        if (status == "0") {
            return "status-pending"
        }
        if (status == "1") {
            return "status-processing"
        }
        if (status == "2") {
            return "status-completed"
        }
        return "status-unknown"
    }
    open var goToPendingIssues = ::gen_goToPendingIssues_fn
    open fun gen_goToPendingIssues_fn() {
        val now = Date()
        val y = now.getFullYear()
        val m = now.getMonth() + 1
        val d = now.getDate()
        val mm = if (m < 10) {
            ("0" + m)
        } else {
            ("" + m)
        }
        val dd = if (d < 10) {
            ("0" + d)
        } else {
            ("" + d)
        }
        val today = "" + y + "-" + mm + "-" + dd
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?status=0&monthly=true&deadlineFrom=" + today))
    }
    open var goToCompletedIssues = ::gen_goToCompletedIssues_fn
    open fun gen_goToCompletedIssues_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?status=1&monthly=true"))
    }
    open var goToMonthlyReport = ::gen_goToMonthlyReport_fn
    open fun gen_goToMonthlyReport_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/statistics/statistics"))
    }
    open var goToDelayedRecord = ::gen_goToDelayedRecord_fn
    open fun gen_goToDelayedRecord_fn() {
        val now = Date()
        val y = now.getFullYear()
        val m = now.getMonth() + 1
        val d = now.getDate()
        val mm = if (m < 10) {
            ("0" + m)
        } else {
            ("" + m)
        }
        val dd = if (d < 10) {
            ("0" + d)
        } else {
            ("" + d)
        }
        val today = "" + y + "-" + mm + "-" + dd
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?status=0&monthly=true&delayed=true&deadlineBefore=" + today))
    }
    open var goToRecords = ::gen_goToRecords_fn
    open fun gen_goToRecords_fn() {
        uni_showToast(ShowToastOptions(title = "记录查看功能开发中", icon = "none"))
    }
    open var goToAddRecord = ::gen_goToAddRecord_fn
    open fun gen_goToAddRecord_fn() {
        this.openTaskPicker()
    }
    open var openTaskPicker = ::gen_openTaskPicker_fn
    open fun gen_openTaskPicker_fn() {
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
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/monthly.uvue:600") as UTSJSONObject
                departmentId = parsedUserInfo.getString("deptId") ?: ""
                isAdmin = (parsedUserInfo.getNumber("isAdmin") ?: 0) === 1
            }
             catch (error: Throwable) {
                console.error("用户信息JSON解析失败:", error, " at pages/monthly/monthly.uvue:604")
            }
        }
        console.log("openTaskPicker: 部门ID=", departmentId, "是否管理员=", isAdmin, " at pages/monthly/monthly.uvue:608")
        if (!isAdmin && (departmentId == null || departmentId == "")) {
            console.log("非管理员用户部门ID为空，无法加载月检任务", " at pages/monthly/monthly.uvue:612")
            this.taskPickerLoading = false
            this.taskPickerList = _uA()
            this.showTaskPickerModal = true
            uni_showToast(ShowToastOptions(title = "未设置所属部门", icon = "none"))
            return
        }
        val url = if (isAdmin) {
            getApiUrl("/notifications/department/tasks")
        } else {
            getApiUrl("/notifications/department/" + departmentId + "/tasks")
        }
        this.taskPickerLoading = true
        console.log("请求月检任务URL:", url, " at pages/monthly/monthly.uvue:629")
        uni_request<Any>(RequestOptions(url = url, method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            console.log("月检任务选择器请求响应:", res, " at pages/monthly/monthly.uvue:636")
            val list = _uA<UTSJSONObject>()
            if (res.statusCode === 200) {
                val arr = res.data as UTSArray<UTSJSONObject>
                console.log("获取到的月检任务数据:", arr.length, "条", " at pages/monthly/monthly.uvue:640")
                run {
                    var i: Number = 0
                    while(i < arr.length){
                        val item = arr[i]
                        console.log("处理任务:", item.getString("taskNo"), "状态:", item.getString("status"), " at pages/monthly/monthly.uvue:645")
                        list.push(item)
                        i++
                    }
                }
                console.log("月检任务总数:", list.length, " at pages/monthly/monthly.uvue:648")
            } else {
                console.log("月检任务选择器请求失败，响应码:", res.statusCode, " at pages/monthly/monthly.uvue:650")
            }
            this.taskPickerList = list
            this.showTaskPickerModal = true
            this.taskPickerLoading = false
        }
        , fail = fun(error){
            console.log("加载月检任务选择器失败:", error, " at pages/monthly/monthly.uvue:657")
            this.taskPickerLoading = false
            this.taskPickerList = _uA()
            this.showTaskPickerModal = true
        }
        ))
    }
    open var closeTaskPicker = ::gen_closeTaskPicker_fn
    open fun gen_closeTaskPicker_fn() {
        this.showTaskPickerModal = false
    }
    open var pickTaskAndGo = ::gen_pickTaskAndGo_fn
    open fun gen_pickTaskAndGo_fn(item: UTSJSONObject) {
        val taskNo = item.getString("taskNo") ?: ""
        if (taskNo != null && taskNo != "") {
            uni_navigateTo(NavigateToOptions(url = "/pages/monthly/task-record?taskId=" + taskNo))
            this.showTaskPickerModal = false
        }
    }
    open var goToReviewedRecords = ::gen_goToReviewedRecords_fn
    open fun gen_goToReviewedRecords_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?status=2&monthly=true"))
    }
    open var loadNotifications = ::gen_loadNotifications_fn
    open fun gen_loadNotifications_fn() {
        val token = uni_getStorageSync("token")
        val userId = this.userInfo.getString("id") ?: ""
        console.log("开始加载通知，用户ID:", userId, " at pages/monthly/monthly.uvue:684")
        if (userId == null || userId == "") {
            console.log("用户ID为空，无法加载通知", " at pages/monthly/monthly.uvue:687")
            return
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/notifications/user/" + userId), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            console.log("通知请求响应:", res, " at pages/monthly/monthly.uvue:699")
            if (res.statusCode == 200) {
                val data = res.data as UTSArray<UTSJSONObject>
                console.log("获取到的通知数据:", data.length, "条", " at pages/monthly/monthly.uvue:702")
                this.notifications = _uA()
                var unreadCount: Number = 0
                run {
                    var i: Number = 0
                    while(i < data.length){
                        val item = data[i]
                        val notification = Notification(id = item.getString("id") ?: "", taskId = item.getString("taskId") ?: "", taskNo = item.getString("taskNo") ?: "", title = item.getString("title") ?: "", content = item.getString("content") ?: "", createTime = item.getString("createTime") ?: "", isRead = item.getBoolean("isRead") ?: false)
                        this.notifications.push(notification)
                        if (!notification.isRead) {
                            unreadCount++
                        }
                        i++
                    }
                }
                this.notificationCount = unreadCount
                console.log("设置通知条数:", this.notificationCount, " at pages/monthly/monthly.uvue:722")
            } else {
                console.log("通知请求失败，响应码:", res.statusCode, " at pages/monthly/monthly.uvue:724")
            }
        }
        , fail = fun(error){
            console.log("加载通知失败:", error, " at pages/monthly/monthly.uvue:728")
        }
        ))
        this.loadTaskNotifications()
    }
    open var loadTaskNotifications = ::gen_loadTaskNotifications_fn
    open fun gen_loadTaskNotifications_fn() {
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
                val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/monthly.uvue:747") as UTSJSONObject
                departmentId = parsedUserInfo.getString("deptId") ?: ""
                isAdmin = (parsedUserInfo.getNumber("isAdmin") ?: 0) === 1
            }
             catch (error: Throwable) {
                console.error("用户信息JSON解析失败:", error, " at pages/monthly/monthly.uvue:751")
            }
        }
        if (!isAdmin && (departmentId == null || departmentId == "")) {
            console.log("非管理员用户部门ID为空，无法加载月检任务通知", " at pages/monthly/monthly.uvue:757")
            return
        }
        val url = if (isAdmin) {
            getApiUrl("/notifications/department/tasks")
        } else {
            getApiUrl("/notifications/department/" + departmentId + "/tasks")
        }
        uni_request<Any>(RequestOptions(url = url, method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            console.log("月检任务通知请求响应:", res, " at pages/monthly/monthly.uvue:773")
            if (res.statusCode == 200) {
                val data = res.data as UTSArray<UTSJSONObject>
                console.log("获取到的月检任务通知数据:", data.length, "条", " at pages/monthly/monthly.uvue:777")
                this.taskNotifications = _uA()
                run {
                    var i: Number = 0
                    while(i < data.length){
                        val item = data[i]
                        val taskNotification = TaskNotification(id = item.getString("id") ?: "", taskId = item.getString("taskId") ?: "", taskNo = item.getString("taskNo") ?: "", title = item.getString("title") ?: "", content = item.getString("content") ?: "", createTime = item.getString("createTime") ?: "", status = item.getString("status") ?: "", inspectionMonth = item.getString("inspectionMonth") ?: "")
                        this.taskNotifications.push(taskNotification)
                        console.log("处理月检任务通知:", taskNotification, " at pages/monthly/monthly.uvue:792")
                        i++
                    }
                }
            } else {
                console.log("月检任务通知请求失败，响应码:", res.statusCode, " at pages/monthly/monthly.uvue:795")
            }
        }
        , fail = fun(error){
            console.log("加载月检任务通知失败:", error, " at pages/monthly/monthly.uvue:799")
        }
        ))
    }
    open var switchNotificationTab = ::gen_switchNotificationTab_fn
    open fun gen_switchNotificationTab_fn(tab: String) {
        this.activeNotificationTab = tab
        if (tab === "tasks") {
            this.loadTaskNotifications()
        }
    }
    open var refreshTaskNotifications = ::gen_refreshTaskNotifications_fn
    open fun gen_refreshTaskNotifications_fn() {
        this.loadTaskNotifications()
        uni_showToast(ShowToastOptions(title = "刷新成功", icon = "success", duration = 1500))
    }
    open var handleTaskNotificationClick = ::gen_handleTaskNotificationClick_fn
    open fun gen_handleTaskNotificationClick_fn(taskNotification: TaskNotification) {
        uni_navigateTo(NavigateToOptions(url = "/pages/monthly/task-record?taskId=" + taskNotification.taskNo))
        this.hideNotifications()
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
        if (!notification.isRead) {
            this.markNotificationAsRead(notification.id)
        }
        notification.isRead = true
        this.updateNotificationCount()
        uni_navigateTo(NavigateToOptions(url = "/pages/monthly/task-record?taskId=" + notification.taskNo))
        this.hideNotifications()
    }
    open var markNotificationAsRead = ::gen_markNotificationAsRead_fn
    open fun gen_markNotificationAsRead_fn(notificationId: String) {
        val token = uni_getStorageSync("token")
        val userId = this.userInfo.getString("id") ?: ""
        if (userId == null || userId == "" || notificationId == null || notificationId == "") {
            return
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/notifications/user/" + userId + "/read/" + notificationId), method = "PUT", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            console.log("标记通知已读成功:", res, " at pages/monthly/monthly.uvue:875")
        }
        , fail = fun(error){
            console.log("标记通知已读失败:", error, " at pages/monthly/monthly.uvue:878")
        }
        ))
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
        val token = uni_getStorageSync("token")
        val userId = this.userInfo.getString("id") ?: ""
        if (userId == null || userId == "") {
            return
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/notifications/user/" + userId + "/read-all"), method = "PUT", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            console.log("标记所有通知已读成功:", res, " at pages/monthly/monthly.uvue:915")
            run {
                var i: Number = 0
                while(i < this.notifications.length){
                    this.notifications[i].isRead = true
                    i++
                }
            }
            this.notificationCount = 0
        }
        , fail = fun(error){
            console.log("标记所有通知已读失败:", error, " at pages/monthly/monthly.uvue:923")
        }
        ))
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
    open var connectWebSocket = ::gen_connectWebSocket_fn
    open fun gen_connectWebSocket_fn() {
        val token = uni_getStorageSync("token")
        val userId = this.userInfo.getString("id") ?: ""
        if (token == null || token == "" || userId == null || userId == "") {
            console.log("缺少token或用户ID，无法连接WebSocket", " at pages/monthly/monthly.uvue:958")
            return
        }
        if (this.socketTask != null) {
            this.disconnectWebSocket()
        }
        val wsUrl = "ws://localhost:8081/ws/websocket"
        console.log("正在连接WebSocket:", wsUrl, " at pages/monthly/monthly.uvue:969")
        this.socketTask = uni_connectSocket(ConnectSocketOptions(url = wsUrl, protocols = _uA(
            "v10.stomp",
            "v11.stomp",
            "v12.stomp"
        ), success = fun(_){
            console.log("WebSocket连接请求发送成功", " at pages/monthly/monthly.uvue:975")
        }
        , fail = fun(error){
            console.error("WebSocket连接失败:", error, " at pages/monthly/monthly.uvue:978")
            this.handleWebSocketError()
        }
        ))
        uni_onSocketOpen(fun(res){
            console.log("WebSocket连接已打开", res, " at pages/monthly/monthly.uvue:985")
            this.isSocketConnected = true
            this.reconnectAttempts = 0
            val token = uni_getStorageSync("token")
            val userId = this.userInfo.getString("id") ?: ""
            this.sendStompConnect(token as String, userId as String)
        }
        )
        uni_onSocketMessage(fun(message){
            val messageData = message.data as String
            console.log("收到WebSocket消息:", messageData, " at pages/monthly/monthly.uvue:998")
            this.handleWebSocketMessage(messageData)
        }
        )
        uni_onSocketClose(fun(event){
            console.log("WebSocket连接已关闭:", event, " at pages/monthly/monthly.uvue:1004")
            this.isSocketConnected = false
            this.socketTask = null
            if (event.code != 1000) {
                this.attemptReconnect()
            }
        }
        )
        uni_onSocketError(fun(error){
            console.error("WebSocket错误:", error, " at pages/monthly/monthly.uvue:1016")
            this.handleWebSocketError()
        }
        )
    }
    open var sendStompConnect = ::gen_sendStompConnect_fn
    open fun gen_sendStompConnect_fn(token: String, userId: String) {
        val connectFrame = "CONNECT\nAccept-version:1.0,1.1,1.2\nhost:localhost\nAuthorization:Bearer " + token + "\n\n" + String.fromCharCode(0)
        if (this.isSocketConnected) {
            uni_sendSocketMessage(SendSocketMessageOptions(data = connectFrame, success = fun(_){
                console.log("STOMP连接帧发送成功", " at pages/monthly/monthly.uvue:1034")
                setTimeout(fun(){
                    this.subscribeToNotifications(userId)
                }
                , 1000)
            }
            , fail = fun(error){
                console.error("STOMP连接帧发送失败:", error, " at pages/monthly/monthly.uvue:1041")
            }
            ))
        }
    }
    open var subscribeToNotifications = ::gen_subscribeToNotifications_fn
    open fun gen_subscribeToNotifications_fn(userId: String) {
        val subscribeFrame = "SUBSCRIBE\nid:sub-0\ndestination:/user/" + userId + "/queue/notifications\n\n" + String.fromCharCode(0)
        if (this.isSocketConnected) {
            uni_sendSocketMessage(SendSocketMessageOptions(data = subscribeFrame, success = fun(_){
                console.log("订阅通知成功", " at pages/monthly/monthly.uvue:1059")
            }
            , fail = fun(error){
                console.error("订阅通知失败:", error, " at pages/monthly/monthly.uvue:1062")
            }
            ))
        }
    }
    open var handleWebSocketMessage = ::gen_handleWebSocketMessage_fn
    open fun gen_handleWebSocketMessage_fn(data: String) {
        try {
            if (data.startsWith("CONNECTED")) {
                console.log("STOMP连接成功", " at pages/monthly/monthly.uvue:1073")
                return
            }
            if (data.startsWith("MESSAGE")) {
                val lines = data.split("\n")
                var messageBody = ""
                var isBody = false
                run {
                    var i: Number = 0
                    while(i < lines.length){
                        if (isBody) {
                            messageBody += lines[i]
                        } else if (lines[i] === "") {
                            isBody = true
                        }
                        i++
                    }
                }
                messageBody = messageBody.replace(UTSRegExp("\\x00\$", ""), "")
                if (messageBody != null && messageBody != "") {
                    console.log("收到通知消息:", messageBody, " at pages/monthly/monthly.uvue:1095")
                    val notification = UTSAndroid.consoleDebugError(JSON.parse(messageBody), " at pages/monthly/monthly.uvue:1096") as UTSJSONObject
                    this.handleNewNotification(notification)
                }
            }
        }
         catch (error: Throwable) {
            console.error("处理WebSocket消息失败:", error, " at pages/monthly/monthly.uvue:1101")
        }
    }
    open var handleNewNotification = ::gen_handleNewNotification_fn
    open fun gen_handleNewNotification_fn(notificationData: UTSJSONObject) {
        val newNotification = Notification(id = notificationData.getString("id") ?: "", taskId = notificationData.getString("taskId") ?: "", taskNo = notificationData.getString("taskNo") ?: "", title = notificationData.getString("title") ?: "", content = notificationData.getString("content") ?: "", createTime = notificationData.getString("createTime") ?: "", isRead = false)
        this.notifications.unshift(newNotification)
        this.notificationCount++
        uni_showToast(ShowToastOptions(title = "\u65B0\u901A\u77E5: " + newNotification.title, icon = "none", duration = 3000))
        console.log("新通知已添加，当前未读数量:", this.notificationCount, " at pages/monthly/monthly.uvue:1130")
    }
    open var disconnectWebSocket = ::gen_disconnectWebSocket_fn
    open fun gen_disconnectWebSocket_fn() {
        if (this.isSocketConnected) {
            console.log("正在断开WebSocket连接", " at pages/monthly/monthly.uvue:1136")
            uni_closeSocket(CloseSocketOptions(code = 1000, reason = "主动断开"))
            this.socketTask = null
            this.isSocketConnected = false
        }
    }
    open var handleWebSocketError = ::gen_handleWebSocketError_fn
    open fun gen_handleWebSocketError_fn() {
        this.isSocketConnected = false
        this.attemptReconnect()
    }
    open var attemptReconnect = ::gen_attemptReconnect_fn
    open fun gen_attemptReconnect_fn() {
        if (this.reconnectAttempts >= this.maxReconnectAttempts) {
            console.log("达到最大重连次数，停止重连", " at pages/monthly/monthly.uvue:1155")
            return
        }
        this.reconnectAttempts++
        console.log("\u7B2C" + this.reconnectAttempts + "\u6B21\u91CD\u8FDE\u5C1D\u8BD5\uFF0C" + this.reconnectInterval + "ms\u540E\u91CD\u8FDE", " at pages/monthly/monthly.uvue:1160")
        setTimeout(fun(){
            if (!this.isSocketConnected) {
                this.connectWebSocket()
            }
        }
        , this.reconnectInterval)
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
                return _uM("container" to _pS(_uM("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "150rpx", "paddingLeft" to "30rpx", "backgroundImage" to "linear-gradient(180deg, #f8fafc 0%, #e2e8f0 100%)", "backgroundColor" to "rgba(0,0,0,0)", "boxSizing" to "border-box")), "header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "30rpx", "paddingRight" to "25rpx", "paddingBottom" to "30rpx", "paddingLeft" to "25rpx", "backgroundImage" to "linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "marginBottom" to "30rpx", "boxShadow" to "0 8rpx 32rpx rgba(102, 126, 234, 0.3)", "position" to "relative", "overflow" to "hidden", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "right::before" to 0, "bottom::before" to 0, "pointerEvents::before" to "none")), "header-right" to _pS(_uM("display" to "flex", "alignItems" to "flex-start", "gap" to "15rpx", "zIndex" to 2, "position" to "relative", "marginTop" to "30rpx")), "welcome" to _pS(_uM("display" to "flex", "flexDirection" to "column", "flex" to 1, "zIndex" to 2, "position" to "relative", "paddingRight" to "140rpx")), "greeting" to _pS(_uM("color" to "rgba(105,72,72,0.95)", "fontSize" to "32rpx", "marginBottom" to "8rpx", "letterSpacing" to "0.5rpx", "textShadow" to "0 2rpx 4rpx rgba(0, 0, 0, 0.1)")), "app-name" to _pS(_uM("color" to "rgba(105,72,72,0.95)", "fontSize" to "26rpx", "fontWeight" to "400", "letterSpacing" to "0.3rpx")), "notification-icon-wrapper" to _pS(_uM("position" to "absolute", "top" to "15rpx", "right" to "15rpx", "width" to "70rpx", "height" to "70rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.2)", "backdropFilter" to "blur(10rpx)", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(255,255,255,0.3)", "borderRightColor" to "rgba(255,255,255,0.3)", "borderBottomColor" to "rgba(255,255,255,0.3)", "borderLeftColor" to "rgba(255,255,255,0.3)", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "zIndex" to 3, "transform:active" to "scale(0.95)", "backgroundImage:active" to "none", "backgroundColor:active" to "rgba(255,255,255,0.3)")), "icon-bell" to _pS(_uM("fontSize" to "32rpx", "color" to "rgba(255,255,255,0.9)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "width" to "100%", "height" to "100%")), "notification-badge" to _pS(_uM("position" to "absolute", "top" to "-6rpx", "right" to "-6rpx", "minWidth" to "32rpx", "height" to "32rpx", "backgroundImage" to "none", "backgroundColor" to "#ff4757", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "borderTopWidth" to "3rpx", "borderRightWidth" to "3rpx", "borderBottomWidth" to "3rpx", "borderLeftWidth" to "3rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#ffffff", "borderRightColor" to "#ffffff", "borderBottomColor" to "#ffffff", "borderLeftColor" to "#ffffff", "boxShadow" to "0 2rpx 8rpx rgba(255, 71, 87, 0.3)", "zIndex" to 4, "paddingTop" to 0, "paddingRight" to "6rpx", "paddingBottom" to 0, "paddingLeft" to "6rpx")), "badge-text" to _pS(_uM("fontSize" to "18rpx", "color" to "#ffffff", "fontWeight" to "700", "lineHeight" to 1, "textAlign" to "center", "whiteSpace" to "nowrap")), "user-avatar" to _pS(_uM("width" to "100rpx", "height" to "100rpx", "borderTopLeftRadius" to "50rpx", "borderTopRightRadius" to "50rpx", "borderBottomRightRadius" to "50rpx", "borderBottomLeftRadius" to "50rpx", "backgroundColor" to "rgba(255,255,255,0.2)", "overflow" to "hidden", "borderTopWidth" to "3rpx", "borderRightWidth" to "3rpx", "borderBottomWidth" to "3rpx", "borderLeftWidth" to "3rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(255,255,255,0.4)", "borderRightColor" to "rgba(255,255,255,0.4)", "borderBottomColor" to "rgba(255,255,255,0.4)", "borderLeftColor" to "rgba(255,255,255,0.4)", "boxShadow" to "0 6rpx 20rpx rgba(0, 0, 0, 0.15), 0 0 0 1rpx rgba(255, 255, 255, 0.1)", "zIndex" to 2, "position" to "relative", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "transform:active" to "scale(0.95)")), "avatar-image" to _pS(_uM("width" to "100rpx", "height" to "100rpx", "borderTopLeftRadius" to "50rpx", "borderTopRightRadius" to "50rpx", "borderBottomRightRadius" to "50rpx", "borderBottomLeftRadius" to "50rpx")), "avatar-placeholder" to _pS(_uM("color" to "rgba(255,255,255,0.9)", "fontSize" to "28rpx", "textTransform" to "uppercase")), "avatar" to _pS(_uM("width" to 50, "height" to 50, "borderTopLeftRadius" to 25, "borderTopRightRadius" to 25, "borderBottomRightRadius" to 25, "borderBottomLeftRadius" to 25, "backgroundColor" to "#dddddd")), "stats-container" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "marginBottom" to 30, "gap" to "10px")), "stats-card" to _pS(_uM("flex" to 1, "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 15, "paddingBottom" to 20, "paddingLeft" to 15, "textAlign" to "center", "position" to "relative", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)", "overflow" to "hidden")), "stats-number" to _pS(_uM("fontSize" to 24, "fontWeight" to "bold", "color" to "#333333", "marginBottom" to 8)), "stats-label" to _pS(_uM("fontSize" to 14, "color" to "#666666", "marginBottom" to 8)), "stats-icon" to _pS(_uM("fontSize" to 20, "position" to "absolute", "top" to 15, "right" to 15, "opacity" to 0.3)), "action-buttons" to _pS(_uM("marginBottom" to 30, "display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between", "gap" to "10px")), "action-button" to _pS(_uM("flex" to 1, "backgroundImage" to "none", "backgroundColor" to "#d1ecf1", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 15, "paddingRight" to 10, "paddingBottom" to 15, "paddingLeft" to 10, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)", "minWidth" to 0)), "delayed-button" to _pS(_uM("!backgroundImage" to "none", "!backgroundColor" to "#ff4757")), "button-icon" to _pS(_uM("fontSize" to 20, "marginRight" to 8, "color" to "#007AFF", "flexShrink" to 0)), "button-text" to _pS(_uM("fontSize" to 14, "color" to "#333333", "whiteSpace" to "nowrap", "overflow" to "hidden", "textOverflow" to "ellipsis")), "trend-section" to _pS(_uM("marginBottom" to 30)), "trend-chart" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "chart-placeholder" to _pS(_uM("height" to 200, "display" to "flex", "flexDirection" to "column", "justifyContent" to "center", "alignItems" to "center", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#dee2e6", "borderRightColor" to "#dee2e6", "borderBottomColor" to "#dee2e6", "borderLeftColor" to "#dee2e6")), "placeholder-text" to _pS(_uM("fontSize" to 18, "color" to "#6c757d", "marginBottom" to 8)), "placeholder-desc" to _pS(_uM("fontSize" to 14, "color" to "#adb5bd")), "quick-actions" to _pS(_uM("marginBottom" to 30)), "section-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333", "marginBottom" to 15)), "actions-grid" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "justifyContent" to "space-between")), "action-item" to _pS(_uM("width" to "48%", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "marginBottom" to 15, "textAlign" to "center", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "action-icon" to _pS(_uM("fontSize" to 24, "marginBottom" to 8)), "action-text" to _pS(_uM("fontSize" to 14, "color" to "#333333")), "recent-records" to _pS(_uM("marginBottom" to 20)), "section-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to 15)), "more-text" to _pS(_uM("fontSize" to 14, "color" to "#007AFF")), "records-list" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 15, "paddingRight" to 15, "paddingBottom" to 15, "paddingLeft" to 15, "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "record-item" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 0, "paddingBottom" to 15, "paddingLeft" to 0, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "record-info" to _pS(_uM("flex" to 1)), "record-title" to _pS(_uM("fontSize" to 16, "color" to "#333333", "marginBottom" to 4)), "record-meta" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "record-status" to _pS(_uM("paddingTop" to 4, "paddingRight" to 8, "paddingBottom" to 4, "paddingLeft" to 8, "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "fontSize" to 12)), "status-pending" to _pS(_uM("backgroundColor" to "#fff3cd", "color" to "#856404")), "status-processing" to _pS(_uM("backgroundColor" to "#d1ecf1", "color" to "#0c5460")), "status-completed" to _pS(_uM("backgroundColor" to "#d4edda", "color" to "#155724")), "status-accepted" to _pS(_uM("backgroundColor" to "#e2e3e5", "color" to "#383d41")), "status-unknown" to _pS(_uM("backgroundColor" to "#f8f9fa", "color" to "#6c757d")), "empty-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20)), "empty-text" to _pS(_uM("fontSize" to 14, "color" to "#999999")), "notification-modal" to _pS(_uM("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundColor" to "rgba(0,0,0,0.5)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "zIndex" to 1000)), "notification-content" to _pS(_uM("width" to "90%", "maxWidth" to 400, "height" to 500, "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "overflow" to "hidden", "display" to "flex", "flexDirection" to "column")), "notification-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "flexShrink" to 0, "backgroundColor" to "#ffffff")), "notification-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333")), "notification-actions" to _pS(_uM("display" to "flex", "alignItems" to "center", "gap" to "15px")), "mark-all-read" to _pS(_uM("fontSize" to 14, "color" to "#007AFF")), "close-btn" to _pS(_uM("fontSize" to 20, "color" to "#999999", "width" to 30, "height" to 30, "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "notification-list" to _pS(_uM("height" to 400, "width" to "100%")), "empty-notifications" to _pS(_uM("paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20, "textAlign" to "center")), "notification-item" to _uM("" to _uM("display" to "flex", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "position" to "relative", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000"), ".unread" to _uM("backgroundColor" to "#f8f9ff")), "notification-dot" to _pS(_uM("width" to 8, "height" to 8, "backgroundColor" to "#007AFF", "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "marginRight" to 12, "flexShrink" to 0)), "notification-info" to _pS(_uM("flex" to 1, "marginRight" to 10)), "notification-item-title" to _uM("" to _uM("fontSize" to 16, "color" to "#333333", "marginBottom" to 4), ".task-notification " to _uM("fontSize" to 17, "color" to "#007AFF", "marginBottom" to 8)), "notification-item-content" to _uM("" to _uM("fontSize" to 14, "color" to "#666666", "marginBottom" to 4, "lineHeight" to 1.4), ".task-notification " to _uM("lineHeight" to 1.6, "marginBottom" to 8)), "notification-time" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "notification-arrow" to _pS(_uM("fontSize" to 18, "color" to "#cccccc", "flexShrink" to 0)), "notification-tabs" to _pS(_uM("display" to "flex", "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "backgroundColor" to "#ffffff", "flexShrink" to 0)), "tab-item" to _uM("" to _uM("flex" to 1, "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "textAlign" to "center", "backgroundColor" to "#f8f9fa", "borderBottomWidth" to 2, "borderBottomStyle" to "solid", "borderBottomColor" to "rgba(0,0,0,0)", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease"), ".active" to _uM("backgroundColor" to "#ffffff", "borderBottomColor" to "#007AFF")), "tab-text" to _uM("" to _uM("fontSize" to 16, "color" to "#666666"), ".tab-item.active " to _uM("color" to "#007AFF")), "tab-content" to _pS(_uM("flex" to 1, "display" to "flex", "flexDirection" to "column", "overflow" to "hidden")), "tab-header" to _pS(_uM("display" to "flex", "justifyContent" to "flex-end", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "backgroundColor" to "#ffffff", "flexShrink" to 0)), "refresh-btn" to _pS(_uM("fontSize" to 14, "color" to "#007AFF", "paddingTop" to 5, "paddingRight" to 10, "paddingBottom" to 5, "paddingLeft" to 10, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#007AFF", "borderRightColor" to "#007AFF", "borderBottomColor" to "#007AFF", "borderLeftColor" to "#007AFF", "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "backgroundColor" to "rgba(0,0,0,0)", "backgroundColor:active" to "#f0f8ff")), "task-notification" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "borderLeftWidth" to 4, "borderLeftStyle" to "solid", "borderLeftColor" to "#007AFF", "backgroundColor" to "#f8fafe", "backgroundColor:hover" to "#f0f8ff", "backgroundColor:active" to "#e6f3ff")), "task-status-badge" to _uM("" to _uM("position" to "absolute", "top" to 15, "right" to 15, "paddingTop" to 4, "paddingRight" to 8, "paddingBottom" to 4, "paddingLeft" to 8, "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "fontSize" to 12), ".status-pending" to _uM("backgroundColor" to "#fff3cd", "color" to "#856404", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#ffeaa7", "borderRightColor" to "#ffeaa7", "borderBottomColor" to "#ffeaa7", "borderLeftColor" to "#ffeaa7"), ".status-completed" to _uM("backgroundColor" to "#d4edda", "color" to "#155724", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#c3e6cb", "borderRightColor" to "#c3e6cb", "borderBottomColor" to "#c3e6cb", "borderLeftColor" to "#c3e6cb"), ".status-accepted" to _uM("backgroundColor" to "#e2e3e5", "color" to "#383d41", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#d6d8db", "borderRightColor" to "#d6d8db", "borderBottomColor" to "#d6d8db", "borderLeftColor" to "#d6d8db"), ".status-unknown" to _uM("backgroundColor" to "#f8f9fa", "color" to "#6c757d", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dee2e6", "borderRightColor" to "#dee2e6", "borderBottomColor" to "#dee2e6", "borderLeftColor" to "#dee2e6")), "task-picker-overlay" to _pS(_uM("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundColor" to "rgba(0,0,0,0.5)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "zIndex" to 1000)), "task-picker" to _pS(_uM("width" to "90%", "maxWidth" to 400, "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "overflow" to "hidden", "display" to "flex", "flexDirection" to "column")), "task-picker-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 16, "paddingRight" to 20, "paddingBottom" to 16, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0")), "task-picker-close" to _pS(_uM("fontSize" to 20, "color" to "#999999", "width" to 30, "height" to 30, "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "task-picker-body" to _pS(_uM("paddingTop" to 12, "paddingRight" to 16, "paddingBottom" to 12, "paddingLeft" to 16, "overflowY" to "auto")), "empty-tasks" to _pS(_uM("paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20, "textAlign" to "center", "color" to "#999999")), "task-item" to _pS(_uM("paddingTop" to 12, "paddingRight" to 10, "paddingBottom" to 12, "paddingLeft" to 10, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "task-title" to _pS(_uM("fontSize" to 14, "color" to "#333333", "marginBottom" to 6)), "task-sub" to _pS(_uM("fontSize" to 12, "color" to "#666666")), "@TRANSITION" to _uM("notification-icon-wrapper" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease"), "user-avatar" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease"), "tab-item" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
