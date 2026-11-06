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
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfoSync as uni_getSystemInfoSync
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.removeStorageSync as uni_removeStorageSync
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.switchTab as uni_switchTab
open class GenPagesMonthlyMonthly : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: OnLoadOptions) {
            this.checkLogin()
            this.initData()
            this.initContainerHeight()
            this.loadCurrentUser().then(fun(){
                this.loadStatistics()
                this.loadMonthlyRecords()
                this.loadNotifications()
            }
            )
        }
        , __ins)
        onPageShow(fun() {
            console.log("页面显示，同步通知")
            this.syncNotificationsFromStore()
            this.loadStatistics()
            this.loadMonthlyRecords()
        }
        , __ins)
        onPageHide(fun() {
            console.log("页面隐藏")
            if (this.refreshTimer != null) {
                clearTimeout(this.refreshTimer!!)
                this.refreshTimer = null
            }
            this.disconnectWebSocket()
        }
        , __ins)
        onUnload(fun() {
            console.log("页面卸载")
            if (this.refreshTimer != null) {
                clearTimeout(this.refreshTimer!!)
                this.refreshTimer = null
            }
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
                    _cE("text", _uM("class" to "button-text"), "问题录入"),
                    _cE("text", _uM("class" to "button-text-sub"), "选择任务后录入问题")
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
                            _cE("text", _uM("class" to "task-picker-title"), "选择月检任务"),
                            _cE("text", _uM("class" to "task-picker-close", "onClick" to _ctx.closeTaskPicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "task-picker-search"), _uA(
                            _cE("input", _uM("class" to "search-input", "modelValue" to _ctx.taskPickerKeyword, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.taskPickerKeyword = `$event`.detail.value
                            }, "placeholder" to "搜索任务名称或编号"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("view", _uM("class" to "task-picker-body"), _uA(
                            if (isTrue(_ctx.taskPickerLoading)) {
                                _cE("view", _uM("key" to 0, "class" to "empty-tasks"), "加载中...")
                            } else {
                                if (_ctx.filteredTaskPickerList.length === 0) {
                                    _cE("view", _uM("key" to 1, "class" to "empty-tasks"), "暂无匹配任务")
                                } else {
                                    _cE("view", _uM("key" to 2), _uA(
                                        _cE(Fragment, null, RenderHelpers.renderList(_ctx.filteredTaskPickerList, fun(t, __key, __index, _cached): Any {
                                            return _cE("view", _uM("class" to "task-item", "key" to t.getString("id"), "onClick" to fun(){
                                                _ctx.pickTaskAndGo(t)
                                            }), _uA(
                                                _cE("text", _uM("class" to "task-title"), _tD(t.getString("taskName") ?: "月检任务"), 1),
                                                _cE("text", _uM("class" to "task-sub"), "任务编号：" + _tD(t.getString("taskNo") ?: "") + " 月检时间：" + _tD(_ctx.formatTaskDateRange(t)), 1)
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
    open var taskPickerLoaded: Boolean by `$data`
    open var taskPickerKeyword: String by `$data`
    open var scrollTop: Number by `$data`
    open var containerHeight: Number by `$data`
    open var isRefreshing: Boolean by `$data`
    open var refreshTimer: Number? by `$data`
    open var currentUser: CurrentUser by `$data`
    open var filteredTaskPickerList: UTSArray<UTSJSONObject> by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("userInfo" to _uO(), "userName" to "用户", "currentDate" to "", "statistics" to MonthlyStatistics(monthlyPending = 0, monthlyCompleted = 0, reviewedCount = 0), "monthlyRecords" to _uA<MonthlyRecord>(), "notifications" to _uA<Notification>(), "taskNotifications" to _uA<TaskNotification>(), "notificationCount" to 0, "showNotificationModal" to false, "activeNotificationTab" to "system", "showTaskPickerModal" to false, "taskPickerLoading" to false, "taskPickerList" to _uA<UTSJSONObject>(), "taskPickerLoaded" to false, "taskPickerKeyword" to "", "scrollTop" to 0, "containerHeight" to 0, "isRefreshing" to false, "refreshTimer" to null as Number?, "currentUser" to CurrentUser(deptId = "", deptName = "", deptType = 0, isMonthlyAdmin = false), "filteredTaskPickerList" to computed<UTSArray<UTSJSONObject>>(fun(): UTSArray<UTSJSONObject> {
            val kw = this.taskPickerKeyword.trim().toLowerCase()
            if (kw == "") {
                return this.taskPickerList
            }
            return this.taskPickerList.filter(fun(t): Boolean {
                val no = (t.getString("taskNo") ?: "").toLowerCase()
                val name = (t.getString("taskName") ?: "").toLowerCase()
                return no.includes(kw) || name.includes(kw)
            }
            )
        }
        ))
    }
    open var checkLogin = ::gen_checkLogin_fn
    open fun gen_checkLogin_fn() {
        val token = uni_getStorageSync("token")
        if (token == null || token == "") {
            uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
            return
        }
        val expiresData = uni_getStorageSync("tokenExpires")
        val expiresStr = if (expiresData != null) {
            "" + expiresData
        } else {
            ""
        }
        if (expiresStr != "" && expiresStr != "null" && expiresStr != "undefined") {
            val expiresAt = parseInt(expiresStr)
            if (!isNaN(expiresAt) && Date.now() >= expiresAt) {
                uni_removeStorageSync("token")
                uni_removeStorageSync("tokenExpires")
                uni_showToast(ShowToastOptions(title = "登录已过期，请重新登录", icon = "none"))
                uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
                return
            }
        }
        val userInfoStr = uni_getStorageSync("userInfo") as String
        if (userInfoStr != null && userInfoStr != "") {
            this.userInfo = JSON.parse(userInfoStr) as UTSJSONObject
            val realName = this.userInfo.getString("realName") ?: ""
            this.userName = if (realName != "") {
                realName
            } else {
                "用户"
            }
        } else {
            this.userInfo = _uO()
            this.userName = "用户"
        }
    }
    open var handleAuthFailure = ::gen_handleAuthFailure_fn
    open fun gen_handleAuthFailure_fn(statusCode: Number): Boolean {
        if (statusCode == 401 || statusCode == 403) {
            uni_removeStorageSync("token")
            uni_removeStorageSync("tokenExpires")
            uni_showToast(ShowToastOptions(title = "登录已失效，请重新登录", icon = "none"))
            uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
            return true
        }
        return false
    }
    open var loadCurrentUser = ::gen_loadCurrentUser_fn
    open fun gen_loadCurrentUser_fn(): UTSPromise<Unit> {
        return UTSPromise<Unit>(fun(resolve, _reject){
            resolveCurrentDept().then(fun(dept: CurrentDept){
                val admin = isMonthlyAdmin()
                this.currentUser = CurrentUser(deptId = dept.deptId, deptName = dept.deptName, deptType = dept.deptType ?: 0, isMonthlyAdmin = admin)
                resolve(Unit)
            }
            )
        }
        )
    }
    open var buildDeptFilter = ::gen_buildDeptFilter_fn
    open fun gen_buildDeptFilter_fn(): String {
        if (this.currentUser.isMonthlyAdmin) {
            return ""
        }
        val deptId = this.currentUser.deptId
        if (deptId == "") {
            return ""
        }
        val deptType = this.currentUser.deptType
        if (deptType === 1) {
            return "&checkUnitId=" + deptId
        }
        if (deptType === 0) {
            return "&departmentId=" + deptId
        }
        return ""
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
        val deptParam = this.buildDeptFilter()
        var pendingCount: Number = 0
        var completedCount: Number = 0
        var reviewedCount: Number = 0
        var completedRequests: Number = 0
        val totalRequests: Number = 3
        val updateStatistics = fun(){
            this.statistics = MonthlyStatistics(monthlyPending = pendingCount, monthlyCompleted = completedCount, reviewedCount = reviewedCount)
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=1&recordStatus=0" + deptParam), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode === 200 && (response.getNumber("code") ?: 0) === 200) {
                val data = response.getJSON("data") as UTSJSONObject
                pendingCount = data.getNumber("total") ?: 0
            }
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        , fail = fun(error){
            console.log("待整改数量请求错误:", error)
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        ))
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=1&recordStatus=1" + deptParam), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode === 200 && (response.getNumber("code") ?: 0) === 200) {
                val data = response.getJSON("data") as UTSJSONObject
                completedCount = data.getNumber("total") ?: 0
            }
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        , fail = fun(error){
            console.log("已整改数量请求错误:", error)
            completedRequests++
            if (completedRequests === totalRequests) {
                updateStatistics()
            }
        }
        ))
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=1&recordStatus=2" + deptParam), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode === 200 && (response.getNumber("code") ?: 0) === 200) {
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
            uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=1&recordStatus=0&rectificationDeadlineBefore=" + today + this.buildDeptFilter()), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res2){
                val resp2 = res2.data as UTSJSONObject
                if (res2.statusCode === 200 && (resp2.getNumber("code") ?: 0) === 200) {
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
            console.log("已复查数量请求错误:", error)
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
        val deptParam = this.buildDeptFilter()
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=10" + deptParam), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode === 200 && (response.getNumber("code") ?: 0) === 200) {
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
            console.log("加载月检记录失败:", error)
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
    open var getTaskStatusText = ::gen_getTaskStatusText_fn
    open fun gen_getTaskStatusText_fn(status: String): String {
        if (status == "0") {
            return "待整改"
        }
        if (status == "1") {
            return "已整改"
        }
        if (status == "2") {
            return "已审核"
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
    open var formatTaskDateRange = ::gen_formatTaskDateRange_fn
    open fun gen_formatTaskDateRange_fn(t: UTSJSONObject): String {
        val start = t.getString("inspectionStartTime") ?: ""
        val end = t.getString("inspectionEndTime") ?: ""
        val sliceMD = fun(s: String): String {
            if (s == "") {
                return ""
            }
            val m = s.match(UTSRegExp("^(\\d{4})-(\\d{2})-(\\d{2})", ""))
            if (m == null) {
                return s
            }
            return "" + m[2] + "-" + m[3]
        }
        val s = sliceMD(start)
        val e = sliceMD(end)
        if (s != "" && e != "" && s != e) {
            return "" + s + " ~ " + e
        }
        return if (s != "") {
            s
        } else {
            e
        }
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
        uni_showToast(ShowToastOptions(title = "加载中", icon = "none"))
    }
    open var goToAddRecord = ::gen_goToAddRecord_fn
    open fun gen_goToAddRecord_fn() {
        this.openTaskPicker()
    }
    open var openTaskPicker = ::gen_openTaskPicker_fn
    open fun gen_openTaskPicker_fn() {
        val token = uni_getStorageSync("token")
        if (this.taskPickerLoaded && !this.taskPickerLoading) {
            this.showTaskPickerModal = true
            return
        }
        this.taskPickerLoading = true
        val url = getApiUrl("/monthly-inspection-task/page?current=1&size=100")
        console.log("请求月检任务列表URL:", url)
        uni_request<Any>(RequestOptions(url = url, method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            console.log("月检任务列表请求成功:", res)
            val list = _uA<UTSJSONObject>()
            if (res.statusCode === 200) {
                val body = res.data as UTSJSONObject
                val pageData = body.getJSON("data")
                val records = if (pageData != null) {
                    pageData.getArray("records")
                } else {
                    null
                } as UTSArray<UTSJSONObject>?
                val arr = if (records != null) {
                    records
                } else {
                    (_uA<UTSJSONObject>())
                }
                console.log("任务通知数组:", arr.length, "条")
                run {
                    var i: Number = 0
                    while(i < arr.length){
                        val item = arr[i]
                        console.log("任务信息:", item.getString("taskNo"), "状态:", item.getString("status"))
                        list.push(item)
                        i++
                    }
                }
                console.log("月检任务列表长度:", list.length)
            } else {
                console.log("月检任务列表请求失败, 状态码:", res.statusCode)
                this.handleAuthFailure(res.statusCode)
            }
            this.taskPickerList = list
            this.taskPickerLoaded = true
            this.showTaskPickerModal = true
            this.taskPickerLoading = false
        }
        , fail = fun(error){
            console.log("加载月检任务列表失败:", error)
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
        if (taskNo == "") {
            return
        }
        val startStr = item.getString("inspectionStartTime") ?: ""
        val endStr = item.getString("inspectionEndTime") ?: ""
        if (startStr == "" || endStr == "") {
            uni_showToast(ShowToastOptions(title = "任务时间信息缺失", icon = "none"))
            return
        }
        val parseDate = fun(s: String): Date {
            return Date(s.replace(" ", "T"))
        }
        val now = Date()
        val inPeriod = now >= parseDate(startStr) && now <= parseDate(endStr)
        if (!inPeriod) {
            uni_showToast(ShowToastOptions(title = "不在检查时间范围内", icon = "none", duration = 2000))
            return
        }
        uni_navigateTo(NavigateToOptions(url = "/pages/monthly/task-record?taskId=" + taskNo))
        this.showTaskPickerModal = false
    }
    open var goToReviewedRecords = ::gen_goToReviewedRecords_fn
    open fun gen_goToReviewedRecords_fn() {
        uni_navigateTo(NavigateToOptions(url = "/pages/record/record?status=2&monthly=true"))
    }
    open var loadNotifications = ::gen_loadNotifications_fn
    open fun gen_loadNotifications_fn() {
        val token = uni_getStorageSync("token")
        console.log("开始加载系统通知")
        uni_request<Any>(RequestOptions(url = getApiUrl("/notifications/user/notifications"), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            console.log("通知请求响应:", res)
            if (res.statusCode === 200) {
                val data = res.data as UTSArray<UTSJSONObject>
                console.log("系统通知数组:", data.length, "条")
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
                console.log("系统通知未读数:", this.notificationCount)
            } else {
                console.log("通知请求失败, 状态码:", res.statusCode)
            }
        }
        , fail = fun(error){
            console.log("加载系统通知失败:", error)
        }
        ))
        this.loadTaskNotifications()
    }
    open var loadTaskNotifications = ::gen_loadTaskNotifications_fn
    open fun gen_loadTaskNotifications_fn() {
        val token = uni_getStorageSync("token")
        val url = getApiUrl("/notifications/tasks")
        uni_request<Any>(RequestOptions(url = url, method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            console.log("月检任务通知请求成功:", res)
            if (res.statusCode === 200) {
                val data = res.data as UTSArray<UTSJSONObject>
                console.log("任务通知数组:", data.length, "条")
                this.taskNotifications = _uA()
                run {
                    var i: Number = 0
                    while(i < data.length){
                        val item = data[i]
                        val taskNotification = TaskNotification(id = item.getString("id") ?: "", taskId = item.getString("taskId") ?: "", taskNo = item.getString("taskNo") ?: "", title = item.getString("title") ?: "", content = item.getString("content") ?: "", createTime = item.getString("createTime") ?: "", status = item.getString("status") ?: "", inspectionMonth = item.getString("inspectionMonth") ?: "")
                        this.taskNotifications.push(taskNotification)
                        console.log("任务通知详情:", taskNotification)
                        i++
                    }
                }
            } else {
                console.log("月检任务通知请求失败, 状态码:", res.statusCode)
                this.handleAuthFailure(res.statusCode)
            }
        }
        , fail = fun(error){
            console.log("加载月检任务通知失败:", error)
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
        uni_showToast(ShowToastOptions(title = "月检任务", icon = "success", duration = 1500))
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
        this.markNotificationAsRead(notification.id)
        this.updateNotificationCount()
        uni_navigateTo(NavigateToOptions(url = "/pages/monthly/task-record?taskId=" + notification.taskNo))
        this.hideNotifications()
    }
    open var markNotificationAsRead = ::gen_markNotificationAsRead_fn
    open fun gen_markNotificationAsRead_fn(notificationId: String) {
        val token = uni_getStorageSync("token")
        if (notificationId == null || notificationId == "") {
            return
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/notifications/read/" + notificationId), method = "PUT", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            console.log("标记通知已读响应:", res)
        }
        , fail = fun(error){
            console.log("标记通知已读失败:", error)
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
        uni_request<Any>(RequestOptions(url = getApiUrl("/notifications/read-all"), method = "PUT", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            console.log("全部已读响应:", res)
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
            console.log("全部已读失败:", error)
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
        this.loadMonthlyRecords()
        this.loadNotifications()
        val rt = this.refreshTimer
        if (rt != null) {
            clearTimeout(rt)
        }
        this.refreshTimer = setTimeout(fun(){
            this.refreshTimer = null
            this.isRefreshing = false
        }
        , 1000)
    }
    open var onRefreshRestore = ::gen_onRefreshRestore_fn
    open fun gen_onRefreshRestore_fn() {
        this.isRefreshing = false
    }
    open var connectWebSocket = ::gen_connectWebSocket_fn
    open fun gen_connectWebSocket_fn() {}
    open var sendStompConnect = ::gen_sendStompConnect_fn
    open fun gen_sendStompConnect_fn(_token: String, _userId: String) {}
    open var subscribeToNotifications = ::gen_subscribeToNotifications_fn
    open fun gen_subscribeToNotifications_fn(_userId: String) {}
    open var handleWebSocketMessage = ::gen_handleWebSocketMessage_fn
    open fun gen_handleWebSocketMessage_fn(_data: String) {}
    open var handleNewNotification = ::gen_handleNewNotification_fn
    open fun gen_handleNewNotification_fn(_notificationData: UTSJSONObject) {}
    open var disconnectWebSocket = ::gen_disconnectWebSocket_fn
    open fun gen_disconnectWebSocket_fn() {}
    open var handleWebSocketError = ::gen_handleWebSocketError_fn
    open fun gen_handleWebSocketError_fn() {}
    open var attemptReconnect = ::gen_attemptReconnect_fn
    open fun gen_attemptReconnect_fn() {}
    open var syncNotificationsFromStore = ::gen_syncNotificationsFromStore_fn
    open fun gen_syncNotificationsFromStore_fn() {
        try {
            val storeList = notificationStore["list"] as UTSArray<NotificationItem>
            val result: UTSArray<Notification> = _uA()
            var unread: Number = 0
            run {
                var i: Number = 0
                while(i < storeList.length){
                    val s = storeList[i]
                    val n = Notification(id = s.id, taskId = s.taskId ?: "", taskNo = s.taskNo ?: "", title = s.title, content = s.content, createTime = s.createTime, isRead = s.isRead)
                    result.push(n)
                    if (!s.isRead) {
                        unread++
                    }
                    i++
                }
            }
            this.notifications = result
            this.notificationCount = unread
        }
         catch (e: Throwable) {}
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("container" to _pS(_uM("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "150rpx", "paddingLeft" to "30rpx", "backgroundImage" to "linear-gradient(180deg, #eff6ff 0%, #dbeafe 100%)", "backgroundColor" to "rgba(0,0,0,0)", "boxSizing" to "border-box", "minHeight" to "1200rpx")), "header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "30rpx", "paddingRight" to "25rpx", "paddingBottom" to "30rpx", "paddingLeft" to "25rpx", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "marginBottom" to "30rpx", "boxShadow" to "0 12rpx 36rpx rgba(30, 64, 175, 0.18)", "position" to "relative", "overflow" to "hidden", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "width::before" to "8rpx", "height::before" to "100%", "backgroundImage::before" to "linear-gradient(180deg, #1e40af, #0ea5e9)", "backgroundColor::before" to "rgba(0,0,0,0)")), "header-right" to _pS(_uM("display" to "flex", "alignItems" to "flex-start", "zIndex" to 2, "position" to "relative", "marginTop" to "30rpx", "marginLeft" to "15rpx")), "welcome" to _pS(_uM("display" to "flex", "flexDirection" to "column", "flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "zIndex" to 2, "position" to "relative", "paddingLeft" to "12rpx", "paddingRight" to "140rpx")), "greeting" to _pS(_uM("color" to "#0a2540", "fontSize" to "36rpx", "fontWeight" to 700, "marginBottom" to "10rpx", "letterSpacing" to "1rpx")), "app-name" to _pS(_uM("color" to "#475569", "fontSize" to "26rpx", "fontWeight" to 500, "letterSpacing" to "0.3rpx")), "notification-icon-wrapper" to _pS(_uM("position" to "absolute", "top" to "15rpx", "right" to "15rpx", "width" to "70rpx", "height" to "70rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "#eff6ff", "borderTopLeftRadius" to "50%", "borderTopRightRadius" to "50%", "borderBottomRightRadius" to "50%", "borderBottomLeftRadius" to "50%", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "zIndex" to 3, "transform:active" to "scale(0.95)", "backgroundImage:active" to "none", "backgroundColor:active" to "#dbeafe")), "icon-bell" to _pS(_uM("fontSize" to "32rpx", "color" to "#1e40af", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "width" to "100%", "height" to "100%")), "notification-badge" to _pS(_uM("position" to "absolute", "top" to "-6rpx", "right" to "-6rpx", "minWidth" to "32rpx", "height" to "32rpx", "backgroundImage" to "none", "backgroundColor" to "#ff4757", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "borderTopWidth" to "3rpx", "borderRightWidth" to "3rpx", "borderBottomWidth" to "3rpx", "borderLeftWidth" to "3rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#ffffff", "borderRightColor" to "#ffffff", "borderBottomColor" to "#ffffff", "borderLeftColor" to "#ffffff", "boxShadow" to "0 2rpx 8rpx rgba(255, 71, 87, 0.3)", "zIndex" to 4, "paddingTop" to 0, "paddingRight" to "6rpx", "paddingBottom" to 0, "paddingLeft" to "6rpx")), "badge-text" to _pS(_uM("fontSize" to "18rpx", "color" to "#ffffff", "fontWeight" to 700, "lineHeight" to 1, "textAlign" to "center", "whiteSpace" to "nowrap")), "user-avatar" to _pS(_uM("width" to "100rpx", "height" to "100rpx", "borderTopLeftRadius" to "50rpx", "borderTopRightRadius" to "50rpx", "borderBottomRightRadius" to "50rpx", "borderBottomLeftRadius" to "50rpx", "backgroundColor" to "#eff6ff", "overflow" to "hidden", "borderTopWidth" to "3rpx", "borderRightWidth" to "3rpx", "borderBottomWidth" to "3rpx", "borderLeftWidth" to "3rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "boxShadow" to "0 6rpx 20rpx rgba(30, 64, 175, 0.12)", "zIndex" to 2, "position" to "relative", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "transform:active" to "scale(0.95)")), "avatar-image" to _pS(_uM("width" to "100rpx", "height" to "100rpx", "borderTopLeftRadius" to "50rpx", "borderTopRightRadius" to "50rpx", "borderBottomRightRadius" to "50rpx", "borderBottomLeftRadius" to "50rpx")), "avatar-placeholder" to _pS(_uM("color" to "#1e40af", "fontSize" to "28rpx", "fontWeight" to 700)), "avatar" to _pS(_uM("width" to 50, "height" to 50, "borderTopLeftRadius" to 25, "borderTopRightRadius" to 25, "borderBottomRightRadius" to 25, "borderBottomLeftRadius" to 25, "backgroundColor" to "#dddddd")), "stats-container" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "marginBottom" to "30rpx")), "stats-card" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "backgroundImage" to "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "32rpx", "paddingRight" to "20rpx", "paddingBottom" to "32rpx", "paddingLeft" to "20rpx", "textAlign" to "center", "position" to "relative", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "overflow" to "hidden", "marginTop" to 0, "marginRight" to "8rpx", "marginBottom" to 0, "marginLeft" to "8rpx", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(255,255,255,0.8)", "borderRightColor" to "rgba(255,255,255,0.8)", "borderBottomColor" to "rgba(255,255,255,0.8)", "borderLeftColor" to "rgba(255,255,255,0.8)", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "right::before" to 0, "height::before" to "4rpx", "backgroundImage::before" to "linear-gradient(90deg, #1e40af, #0ea5e9)", "backgroundColor::before" to "rgba(0,0,0,0)")), "stats-number" to _pS(_uM("fontSize" to "48rpx", "fontWeight" to 700, "color" to "#0a2540", "marginBottom" to "8rpx")), "stats-label" to _pS(_uM("fontSize" to "26rpx", "color" to "#1e293b", "fontWeight" to 600, "marginBottom" to "8rpx")), "stats-icon" to _pS(_uM("fontSize" to "36rpx", "position" to "absolute", "top" to "20rpx", "right" to "20rpx", "opacity" to 0.18)), "action-buttons" to _pS(_uM("marginBottom" to "30rpx", "display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between")), "action-button" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "36rpx", "paddingRight" to "16rpx", "paddingBottom" to "36rpx", "paddingLeft" to "16rpx", "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.12)", "marginTop" to 0, "marginRight" to "8rpx", "marginBottom" to 0, "marginLeft" to "8rpx", "minWidth" to 0, "transitionProperty" to "transform", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "position" to "relative", "overflow" to "hidden", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "right::before" to 0, "height::before" to "6rpx", "backgroundImage::before" to "linear-gradient(90deg, #1e40af, #0ea5e9)", "backgroundColor::before" to "rgba(0,0,0,0)", "transform:active" to "scale(0.97)")), "delayed-button" to _pS(_uM("!backgroundImage" to "none", "!backgroundColor" to "#fffbeb", "!borderTopColor" to "#fde68a", "!borderRightColor" to "#fde68a", "!borderBottomColor" to "#fde68a", "!borderLeftColor" to "#fde68a", "!backgroundImage::before" to "linear-gradient(90deg, #b45309, #f59e0b)", "!backgroundColor::before" to "rgba(0,0,0,0)")), "button-icon" to _uM("" to _uM("fontSize" to "44rpx", "marginBottom" to "14rpx", "color" to "#1e40af", "flexShrink" to 0), ".delayed-button " to _uM("color" to "#b45309")), "button-text" to _uM("" to _uM("fontSize" to "32rpx", "color" to "#0a2540", "fontWeight" to 700, "letterSpacing" to "1rpx"), ".delayed-button " to _uM("color" to "#7c2d12")), "button-text-sub" to _uM("" to _uM("fontSize" to "22rpx", "color" to "#475569", "fontWeight" to 500, "marginTop" to "6rpx", "letterSpacing" to "0.5rpx"), ".delayed-button " to _uM("color" to "#92400e")), "trend-section" to _pS(_uM("marginBottom" to 30)), "trend-chart" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "chart-placeholder" to _pS(_uM("height" to 200, "display" to "flex", "flexDirection" to "column", "justifyContent" to "center", "alignItems" to "center", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#dee2e6", "borderRightColor" to "#dee2e6", "borderBottomColor" to "#dee2e6", "borderLeftColor" to "#dee2e6")), "placeholder-text" to _pS(_uM("fontSize" to 18, "color" to "#6c757d", "marginBottom" to 8)), "placeholder-desc" to _pS(_uM("fontSize" to 14, "color" to "#adb5bd")), "quick-actions" to _pS(_uM("marginBottom" to 30)), "section-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333", "marginBottom" to 15)), "actions-grid" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "justifyContent" to "space-between")), "action-item" to _pS(_uM("width" to "48%", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "marginBottom" to 15, "textAlign" to "center", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "action-icon" to _pS(_uM("fontSize" to 24, "marginBottom" to 8)), "action-text" to _pS(_uM("fontSize" to 14, "color" to "#333333")), "recent-records" to _pS(_uM("marginBottom" to 20)), "section-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to 15)), "more-text" to _pS(_uM("fontSize" to "26rpx", "color" to "#1e40af", "fontWeight" to 500)), "records-list" to _pS(_uM("backgroundImage" to "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "16rpx", "paddingRight" to "28rpx", "paddingBottom" to "16rpx", "paddingLeft" to "28rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.06)")), "record-item" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "24rpx", "paddingRight" to 0, "paddingBottom" to "24rpx", "paddingLeft" to 0, "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#f1f5f9", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "record-info" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%")), "record-title" to _pS(_uM("fontSize" to "30rpx", "color" to "#0f172a", "marginBottom" to "6rpx", "fontWeight" to 500)), "record-meta" to _pS(_uM("fontSize" to "24rpx", "color" to "#94a3b8")), "record-status" to _pS(_uM("paddingTop" to "8rpx", "paddingRight" to "16rpx", "paddingBottom" to "8rpx", "paddingLeft" to "16rpx", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "fontSize" to "24rpx", "fontWeight" to 500)), "status-pending" to _pS(_uM("backgroundColor" to "#fef3c7", "color" to "#92400e")), "status-processing" to _pS(_uM("backgroundColor" to "#dbeafe", "color" to "#1e40af")), "status-completed" to _pS(_uM("backgroundColor" to "#dcfce7", "color" to "#166534")), "status-accepted" to _pS(_uM("backgroundColor" to "#e0e7ff", "color" to "#3730a3")), "status-unknown" to _pS(_uM("backgroundColor" to "#f1f5f9", "color" to "#475569")), "empty-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20)), "empty-text" to _pS(_uM("fontSize" to 14, "color" to "#999999")), "notification-modal" to _pS(_uM("!position" to "fixed", "!top" to 0, "!left" to 0, "!width" to "100%", "!height" to "100%", "backgroundColor" to "rgba(10,37,64,0.55)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "!zIndex" to 9999)), "notification-content" to _pS(_uM("!width" to "90%", "maxWidth" to "600rpx", "height" to "900rpx", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "overflow" to "hidden", "display" to "flex", "flexDirection" to "column", "boxShadow" to "0 24rpx 64rpx rgba(10, 37, 64, 0.35)", "position" to "relative", "zIndex" to 10000)), "notification-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "28rpx", "paddingRight" to "32rpx", "paddingBottom" to "28rpx", "paddingLeft" to "32rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#e2e8f0", "flexShrink" to 0, "backgroundImage" to "linear-gradient(135deg, #0a2540 0%, #1e40af 100%)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#ffffff")), "notification-title" to _pS(_uM("fontSize" to "32rpx", "fontWeight" to 700, "color" to "#ffffff", "letterSpacing" to "1rpx")), "notification-actions" to _pS(_uM("display" to "flex", "alignItems" to "center")), "mark-all-read" to _uM(".notification-actions " to _uM("marginRight" to 15), "" to _uM("fontSize" to "26rpx", "color" to "#1e40af", "fontWeight" to 500)), "close-btn" to _pS(_uM("fontSize" to "36rpx", "color" to "#64748b", "width" to "60rpx", "height" to "60rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "notification-list" to _pS(_uM("height" to "400rpx", "width" to "100%")), "empty-notifications" to _pS(_uM("paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20, "textAlign" to "center")), "notification-item" to _uM("" to _uM("display" to "flex", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "position" to "relative", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000"), ".unread" to _uM("backgroundColor" to "#eff6ff")), "notification-dot" to _pS(_uM("width" to "8rpx", "height" to "8rpx", "backgroundColor" to "#1e40af", "borderTopLeftRadius" to "4rpx", "borderTopRightRadius" to "4rpx", "borderBottomRightRadius" to "4rpx", "borderBottomLeftRadius" to "4rpx", "marginRight" to "12rpx", "flexShrink" to 0)), "notification-info" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "marginRight" to 10)), "notification-item-title" to _uM("" to _uM("fontSize" to "30rpx", "color" to "#0f172a", "fontWeight" to 600, "marginBottom" to "6rpx"), ".task-notification " to _uM("fontSize" to "32rpx", "fontWeight" to 700, "color" to "#1e40af", "marginBottom" to "10rpx")), "notification-item-content" to _uM("" to _uM("fontSize" to "26rpx", "color" to "#475569", "marginBottom" to "6rpx", "lineHeight" to 1.5), ".task-notification " to _uM("whiteSpace" to "pre-line", "lineHeight" to 1.6, "marginBottom" to "8rpx")), "notification-time" to _pS(_uM("fontSize" to "22rpx", "color" to "#94a3b8")), "notification-arrow" to _pS(_uM("fontSize" to "32rpx", "color" to "#cbd5e1", "flexShrink" to 0)), "notification-tabs" to _pS(_uM("display" to "flex", "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "backgroundColor" to "#ffffff", "flexShrink" to 0)), "tab-item" to _uM("" to _uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "paddingTop" to "26rpx", "paddingRight" to "30rpx", "paddingBottom" to "26rpx", "paddingLeft" to "30rpx", "textAlign" to "center", "backgroundColor" to "#f1f5f9", "borderBottomWidth" to "4rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "rgba(0,0,0,0)", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease"), ".active" to _uM("backgroundColor" to "#ffffff", "borderBottomColor" to "#1e40af")), "tab-text" to _uM("" to _uM("fontSize" to "28rpx", "color" to "#64748b", "fontWeight" to 500), ".tab-item.active " to _uM("color" to "#1e40af", "fontWeight" to 700)), "tab-content" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "display" to "flex", "flexDirection" to "column", "overflow" to "hidden")), "tab-header" to _pS(_uM("display" to "flex", "justifyContent" to "flex-end", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "backgroundColor" to "#ffffff", "flexShrink" to 0)), "refresh-btn" to _pS(_uM("fontSize" to "26rpx", "color" to "#1e40af", "paddingTop" to "8rpx", "paddingRight" to "20rpx", "paddingBottom" to "8rpx", "paddingLeft" to "20rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#1e40af", "borderRightColor" to "#1e40af", "borderBottomColor" to "#1e40af", "borderLeftColor" to "#1e40af", "borderTopLeftRadius" to "8rpx", "borderTopRightRadius" to "8rpx", "borderBottomRightRadius" to "8rpx", "borderBottomLeftRadius" to "8rpx", "backgroundColor" to "rgba(0,0,0,0)", "fontWeight" to 500, "backgroundColor:active" to "#dbeafe")), "task-notification" to _pS(_uM("paddingTop" to "32rpx", "paddingRight" to "32rpx", "paddingBottom" to "32rpx", "paddingLeft" to "32rpx", "borderLeftWidth" to "8rpx", "borderLeftStyle" to "solid", "borderLeftColor" to "#1e40af", "backgroundColor" to "#eff6ff", "backgroundColor:hover" to "#dbeafe", "backgroundColor:active" to "#bfdbfe")), "task-status-badge" to _uM("" to _uM("position" to "absolute", "top" to 15, "right" to 15, "paddingTop" to 4, "paddingRight" to 8, "paddingBottom" to 4, "paddingLeft" to 8, "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "fontSize" to 12, "fontWeight" to 500), ".status-pending" to _uM("backgroundColor" to "#fff3cd", "color" to "#856404", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#ffeaa7", "borderRightColor" to "#ffeaa7", "borderBottomColor" to "#ffeaa7", "borderLeftColor" to "#ffeaa7"), ".status-completed" to _uM("backgroundColor" to "#d4edda", "color" to "#155724", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#c3e6cb", "borderRightColor" to "#c3e6cb", "borderBottomColor" to "#c3e6cb", "borderLeftColor" to "#c3e6cb"), ".status-accepted" to _uM("backgroundColor" to "#e2e3e5", "color" to "#383d41", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#d6d8db", "borderRightColor" to "#d6d8db", "borderBottomColor" to "#d6d8db", "borderLeftColor" to "#d6d8db"), ".status-unknown" to _uM("backgroundColor" to "#f8f9fa", "color" to "#6c757d", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dee2e6", "borderRightColor" to "#dee2e6", "borderBottomColor" to "#dee2e6", "borderLeftColor" to "#dee2e6")), "status-text" to _uM(".task-status-badge " to _uM("color" to "#383d41"), ".status-pending " to _uM("color" to "#856404"), ".status-completed " to _uM("color" to "#155724"), ".status-accepted " to _uM("color" to "#383d41"), ".status-unknown " to _uM("color" to "#6c757d")), "task-picker-overlay" to _pS(_uM("!position" to "fixed", "!top" to 0, "!left" to 0, "!width" to "100%", "!height" to "100%", "backgroundColor" to "rgba(10,37,64,0.55)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "!zIndex" to 9999)), "task-picker" to _pS(_uM("!width" to "90%", "maxWidth" to "600rpx", "maxHeight" to "900rpx", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "overflow" to "hidden", "display" to "flex", "flexDirection" to "column", "boxShadow" to "0 24rpx 64rpx rgba(10, 37, 64, 0.35)", "position" to "relative", "zIndex" to 10000)), "task-picker-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "28rpx", "paddingRight" to "32rpx", "paddingBottom" to "28rpx", "paddingLeft" to "32rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#e2e8f0", "backgroundImage" to "linear-gradient(135deg, #0a2540 0%, #1e40af 100%)", "backgroundColor" to "rgba(0,0,0,0)", "position" to "relative", "color" to "#ffffff")), "task-picker-title" to _pS(_uM("color" to "#ffffff", "fontSize" to "30rpx", "fontWeight" to 700, "letterSpacing" to "1rpx")), "task-picker-close" to _pS(_uM("fontSize" to "44rpx", "color" to "rgba(255,255,255,0.85)", "fontWeight" to 300, "width" to "60rpx", "height" to "60rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "borderTopLeftRadius" to "30rpx", "borderTopRightRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "borderBottomLeftRadius" to "30rpx")), "task-picker-body" to _pS(_uM("paddingTop" to "16rpx", "paddingRight" to "24rpx", "paddingBottom" to "16rpx", "paddingLeft" to "24rpx", "maxHeight" to "800rpx")), "task-picker-search" to _pS(_uM("paddingTop" to "16rpx", "paddingRight" to "24rpx", "paddingBottom" to 0, "paddingLeft" to "24rpx")), "search-input" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#eff6ff", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "paddingTop" to "16rpx", "paddingRight" to "24rpx", "paddingBottom" to "16rpx", "paddingLeft" to "24rpx", "fontSize" to "26rpx", "width" to "100%", "boxSizing" to "border-box", "color" to "#0f172a", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe")), "empty-tasks" to _pS(_uM("paddingTop" to "60rpx", "paddingRight" to "32rpx", "paddingBottom" to "60rpx", "paddingLeft" to "32rpx", "textAlign" to "center", "color" to "#94a3b8", "fontSize" to "26rpx")), "task-item" to _pS(_uM("paddingTop" to "24rpx", "paddingRight" to "16rpx", "paddingBottom" to "24rpx", "paddingLeft" to "16rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#f1f5f9", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "task-title" to _pS(_uM("fontSize" to "28rpx", "color" to "#0f172a", "marginBottom" to "10rpx", "fontWeight" to 600)), "task-sub" to _pS(_uM("fontSize" to "24rpx", "color" to "#64748b")), "@TRANSITION" to _uM("notification-icon-wrapper" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease"), "user-avatar" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease"), "action-button" to _uM("property" to "transform", "duration" to "0.2s", "timingFunction" to "ease"), "tab-item" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
