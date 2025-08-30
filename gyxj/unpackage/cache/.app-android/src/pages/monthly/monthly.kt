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
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.switchTab as uni_switchTab
open class GenPagesMonthlyMonthly : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: OnLoadOptions) {
            this.checkLogin()
            this.initData()
            this.loadStatistics()
            this.loadMonthlyRecords()
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
                    _cE("text", _uM("class" to "welcome-text"), "月检统计"),
                    _cE("text", _uM("class" to "date-text"), _tD(_ctx.currentDate), 1)
                )),
                _cE("image", _uM("class" to "avatar", "src" to "/static/avatar.png"))
            )),
            _cE("view", _uM("class" to "stats-container"), _uA(
                _cE("view", _uM("class" to "stats-card", "onClick" to _ctx.goToPendingIssues), _uA(
                    _cE("text", _uM("class" to "stats-number"), _tD(_ctx.statistics.monthlyPending), 1),
                    _cE("text", _uM("class" to "stats-label"), "本月待整改"),
                    _cE("text", _uM("class" to "stats-icon"), "⚠️")
                ), 8, _uA(
                    "onClick"
                )),
                _cE("view", _uM("class" to "stats-card", "onClick" to _ctx.goToCompletedIssues), _uA(
                    _cE("text", _uM("class" to "stats-number"), _tD(_ctx.statistics.monthlyCompleted), 1),
                    _cE("text", _uM("class" to "stats-label"), "本月已整改"),
                    _cE("text", _uM("class" to "stats-icon"), "✅")
                ), 8, _uA(
                    "onClick"
                )),
                _cE("view", _uM("class" to "stats-card", "onClick" to _ctx.goToMonthlyReport), _uA(
                    _cE("text", _uM("class" to "stats-number"), _tD(_ctx.statistics.monthlyTotal), 1),
                    _cE("text", _uM("class" to "stats-label"), "本月总记录"),
                    _cE("text", _uM("class" to "stats-icon"), "📊")
                ), 8, _uA(
                    "onClick"
                ))
            )),
            _cE("view", _uM("class" to "trend-section"), _uA(
                _cE("text", _uM("class" to "section-title"), "月度趋势"),
                _cE("view", _uM("class" to "trend-chart"), _uA(
                    _cE("view", _uM("class" to "chart-placeholder"), _uA(
                        _cE("text", _uM("class" to "placeholder-text"), "月度趋势图"),
                        _cE("text", _uM("class" to "placeholder-desc"), "显示本月问题发现与整改趋势")
                    ))
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
                    _cE("view", _uM("class" to "action-item", "onClick" to _ctx.goToMonthlyReport), _uA(
                        _cE("view", _uM("class" to "action-icon"), "📊"),
                        _cE("text", _uM("class" to "action-text"), "月度报告")
                    ), 8, _uA(
                        "onClick"
                    )),
                    _cE("view", _uM("class" to "action-item", "onClick" to _ctx.goToPendingIssues), _uA(
                        _cE("view", _uM("class" to "action-icon"), "⚠️"),
                        _cE("text", _uM("class" to "action-text"), "待整改问题")
                    ), 8, _uA(
                        "onClick"
                    )),
                    _cE("view", _uM("class" to "action-item", "onClick" to _ctx.goToCompletedIssues), _uA(
                        _cE("view", _uM("class" to "action-icon"), "✅"),
                        _cE("text", _uM("class" to "action-text"), "已整改问题")
                    ), 8, _uA(
                        "onClick"
                    ))
                ))
            )),
            _cE("view", _uM("class" to "recent-records"), _uA(
                _cE("view", _uM("class" to "section-header"), _uA(
                    _cE("text", _uM("class" to "section-title"), "本月记录"),
                    _cE("text", _uM("class" to "more-text", "onClick" to _ctx.goToRecords), "查看更多", 8, _uA(
                        "onClick"
                    ))
                )),
                _cE("view", _uM("class" to "records-list"), _uA(
                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.monthlyRecords, fun(record, __key, __index, _cached): Any {
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
                    if (_ctx.monthlyRecords.length === 0) {
                        _cE("view", _uM("key" to 0, "class" to "empty-state"), _uA(
                            _cE("text", _uM("class" to "empty-text"), "本月暂无记录")
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
    open var statistics: MonthlyStatistics by `$data`
    open var monthlyRecords: UTSArray<MonthlyRecord> by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("userInfo" to UTSJSONObject(), "currentDate" to "", "statistics" to MonthlyStatistics(monthlyPending = 0, monthlyCompleted = 0, monthlyTotal = 0), "monthlyRecords" to _uA<MonthlyRecord>())
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
            this.userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/monthly.uvue:135") as UTSJSONObject
        } else {
            this.userInfo = UTSJSONObject()
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
        this.currentDate = "" + year + "\u5E74" + formattedMonth + "\u6708"
    }
    open var loadStatistics = ::gen_loadStatistics_fn
    open fun gen_loadStatistics_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/statistics/overview"), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getJSON("data") as UTSJSONObject
                this.statistics = MonthlyStatistics(monthlyPending = data.getNumber("totalTasks") ?: 0, monthlyCompleted = data.getNumber("todayRecords") ?: 0, monthlyTotal = data.getNumber("monthRecords") ?: 0)
            }
        }
        , fail = fun(error){
            console.log("加载统计数据失败:", error, " at pages/monthly/monthly.uvue:170")
        }
        ))
    }
    open var loadMonthlyRecords = ::gen_loadMonthlyRecords_fn
    open fun gen_loadMonthlyRecords_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record/page?pageNum=1&pageSize=10"), method = "GET", header = object : UTSJSONObject() {
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
            console.log("加载月度记录失败:", error, " at pages/monthly/monthly.uvue:203")
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
    open var goToMonthlyReport = ::gen_goToMonthlyReport_fn
    open fun gen_goToMonthlyReport_fn() {
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
                return _uM("container" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "backgroundColor" to "#f5f5f5", "minHeight" to 800)), "header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to 20)), "welcome" to _pS(_uM("flex" to 1)), "welcome-text" to _pS(_uM("fontSize" to 20, "fontWeight" to "bold", "color" to "#333333", "marginBottom" to 4)), "date-text" to _pS(_uM("fontSize" to 14, "color" to "#666666")), "avatar" to _pS(_uM("width" to 50, "height" to 50, "borderTopLeftRadius" to 25, "borderTopRightRadius" to 25, "borderBottomRightRadius" to 25, "borderBottomLeftRadius" to 25, "backgroundColor" to "#dddddd")), "stats-container" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "marginBottom" to 30)), "stats-card" to _pS(_uM("flex" to 1, "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 15, "paddingBottom" to 20, "paddingLeft" to 15, "marginTop" to 0, "marginRight" to 5, "marginBottom" to 0, "marginLeft" to 5, "textAlign" to "center", "position" to "relative", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "stats-number" to _pS(_uM("fontSize" to 24, "fontWeight" to "bold", "color" to "#007AFF", "marginBottom" to 8)), "stats-label" to _pS(_uM("fontSize" to 14, "color" to "#666666", "marginBottom" to 8)), "stats-icon" to _pS(_uM("fontSize" to 20, "position" to "absolute", "top" to 15, "right" to 15)), "trend-section" to _pS(_uM("marginBottom" to 30)), "trend-chart" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "chart-placeholder" to _pS(_uM("height" to 200, "display" to "flex", "flexDirection" to "column", "justifyContent" to "center", "alignItems" to "center", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#dee2e6", "borderRightColor" to "#dee2e6", "borderBottomColor" to "#dee2e6", "borderLeftColor" to "#dee2e6")), "placeholder-text" to _pS(_uM("fontSize" to 18, "color" to "#6c757d", "marginBottom" to 8)), "placeholder-desc" to _pS(_uM("fontSize" to 14, "color" to "#adb5bd")), "quick-actions" to _pS(_uM("marginBottom" to 30)), "section-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333", "marginBottom" to 15)), "actions-grid" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "justifyContent" to "space-between")), "action-item" to _pS(_uM("width" to "48%", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "marginBottom" to 15, "textAlign" to "center", "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "action-icon" to _pS(_uM("fontSize" to 24, "marginBottom" to 8)), "action-text" to _pS(_uM("fontSize" to 14, "color" to "#333333")), "recent-records" to _pS(_uM("marginBottom" to 20)), "section-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to 15)), "more-text" to _pS(_uM("fontSize" to 14, "color" to "#007AFF")), "records-list" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 15, "paddingRight" to 15, "paddingBottom" to 15, "paddingLeft" to 15, "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "record-item" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 0, "paddingBottom" to 15, "paddingLeft" to 0, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "record-info" to _pS(_uM("flex" to 1)), "record-title" to _pS(_uM("fontSize" to 16, "color" to "#333333", "marginBottom" to 4)), "record-meta" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "record-status" to _pS(_uM("paddingTop" to 4, "paddingRight" to 8, "paddingBottom" to 4, "paddingLeft" to 8, "borderTopLeftRadius" to 4, "borderTopRightRadius" to 4, "borderBottomRightRadius" to 4, "borderBottomLeftRadius" to 4, "fontSize" to 12)), "status-pending" to _pS(_uM("backgroundColor" to "#fff3cd", "color" to "#856404")), "status-processing" to _pS(_uM("backgroundColor" to "#d1ecf1", "color" to "#0c5460")), "status-completed" to _pS(_uM("backgroundColor" to "#d4edda", "color" to "#155724")), "status-accepted" to _pS(_uM("backgroundColor" to "#e2e3e5", "color" to "#383d41")), "status-unknown" to _pS(_uM("backgroundColor" to "#f8f9fa", "color" to "#6c757d")), "empty-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20)), "empty-text" to _pS(_uM("fontSize" to 14, "color" to "#999999")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
