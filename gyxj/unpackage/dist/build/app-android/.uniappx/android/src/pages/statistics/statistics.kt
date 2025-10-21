@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
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
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesStatisticsStatistics : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(_: OnLoadOptions) {
            this.checkLogin()
            this.loadStatistics()
        }
        , __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("view", _uM("class" to "container"), _uA(
            _cE("view", _uM("class" to "filter-section"), _uA(
                _cE("view", _uM("class" to "time-filter"), _uA(
                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.periodTabs, fun(item, __key, __index, _cached): Any {
                        return _cE("view", _uM("class" to _nC(_uA(
                            "time-tab",
                            _uM("active" to (_ctx.currentPeriod === item.value))
                        )), "key" to item.value, "onClick" to fun(){
                            _ctx.changePeriod(item.value)
                        }
                        ), _uA(
                            _cE("text", null, _tD(item.label), 1)
                        ), 10, _uA(
                            "onClick"
                        ))
                    }
                    ), 128)
                ))
            )),
            _cE("view", _uM("class" to "overview-section"), _uA(
                _cE("view", _uM("class" to "overview-title"), _uA(
                    _cE("text", null, "数据概览"),
                    _cE("text", _uM("class" to "period-text"), _tD(_ctx.getPeriodText()), 1)
                )),
                _cE("view", _uM("class" to "stats-grid"), _uA(
                    _cE("view", _uM("class" to "stats-item"), _uA(
                        _cE("text", _uM("class" to "stats-number"), _tD(_ctx.overview.totalRecords), 1),
                        _cE("text", _uM("class" to "stats-label"), "总记录数"),
                        _cE("text", _uM("class" to "stats-icon"), "📝")
                    )),
                    _cE("view", _uM("class" to "stats-item"), _uA(
                        _cE("text", _uM("class" to "stats-number"), _tD(_ctx.overview.completedTasks), 1),
                        _cE("text", _uM("class" to "stats-label"), "完成任务"),
                        _cE("text", _uM("class" to "stats-icon"), "✅")
                    )),
                    _cE("view", _uM("class" to "stats-item"), _uA(
                        _cE("text", _uM("class" to "stats-number"), _tD(_ctx.overview.pendingRecords), 1),
                        _cE("text", _uM("class" to "stats-label"), "待整改"),
                        _cE("text", _uM("class" to "stats-icon"), "⏳")
                    )),
                    _cE("view", _uM("class" to "stats-item"), _uA(
                        _cE("text", _uM("class" to "stats-number"), _tD(_ctx.overview.completionRate) + "%", 1),
                        _cE("text", _uM("class" to "stats-label"), "完成率"),
                        _cE("text", _uM("class" to "stats-icon"), "📊")
                    ))
                ))
            )),
            _cE("view", _uM("class" to "chart-section"), _uA(
                _cE("view", _uM("class" to "chart-header"), _uA(
                    _cE("text", _uM("class" to "chart-title"), "记录状态分布")
                )),
                _cE("view", _uM("class" to "status-chart"), _uA(
                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.statusDistribution, fun(item, __key, __index, _cached): Any {
                        return _cE("view", _uM("class" to "status-item", "key" to item.status), _uA(
                            _cE("view", _uM("class" to "status-info"), _uA(
                                _cE("view", _uM("class" to _nC(_uA(
                                    "status-dot",
                                    _ctx.getStatusDotClass(item.status)
                                ))), null, 2),
                                _cE("text", _uM("class" to "status-name"), _tD(_ctx.getStatusText(item.status)), 1)
                            )),
                            _cE("view", _uM("class" to "status-data"), _uA(
                                _cE("text", _uM("class" to "status-count"), _tD(item.count), 1),
                                _cE("text", _uM("class" to "status-percent"), _tD(item.percentage) + "%", 1)
                            )),
                            _cE("view", _uM("class" to "status-bar"), _uA(
                                _cE("view", _uM("class" to _nC(_uA(
                                    "status-progress",
                                    _ctx.getStatusDotClass(item.status)
                                )), "style" to _nS(_uM("width" to (item.percentage + "%")))), null, 6)
                            ))
                        ))
                    }
                    ), 128)
                ))
            )),
            _cE("view", _uM("class" to "chart-section"), _uA(
                _cE("view", _uM("class" to "chart-header"), _uA(
                    _cE("text", _uM("class" to "chart-title"), "严重程度分布")
                )),
                _cE("view", _uM("class" to "severity-chart"), _uA(
                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.severityDistribution, fun(item, __key, __index, _cached): Any {
                        return _cE("view", _uM("class" to "severity-item", "key" to item.severity), _uA(
                            _cE("view", _uM("class" to "severity-info"), _uA(
                                _cE("view", _uM("class" to _nC(_uA(
                                    "severity-dot",
                                    _ctx.getSeverityDotClass(item.severity)
                                ))), null, 2),
                                _cE("text", _uM("class" to "severity-name"), _tD(_ctx.getSeverityText(item.severity)), 1)
                            )),
                            _cE("view", _uM("class" to "severity-data"), _uA(
                                _cE("text", _uM("class" to "severity-count"), _tD(item.count), 1),
                                _cE("text", _uM("class" to "severity-percent"), _tD(item.percentage) + "%", 1)
                            )),
                            _cE("view", _uM("class" to "severity-bar"), _uA(
                                _cE("view", _uM("class" to _nC(_uA(
                                    "severity-progress",
                                    _ctx.getSeverityDotClass(item.severity)
                                )), "style" to _nS(_uM("width" to (item.percentage + "%")))), null, 6)
                            ))
                        ))
                    }
                    ), 128)
                ))
            )),
            _cE("view", _uM("class" to "chart-section"), _uA(
                _cE("view", _uM("class" to "chart-header"), _uA(
                    _cE("text", _uM("class" to "chart-title"), "趋势分析")
                )),
                _cE("view", _uM("class" to "trend-chart"), _uA(
                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.trendData, fun(item, __key, __index, _cached): Any {
                        return _cE("view", _uM("class" to "trend-item", "key" to item.date), _uA(
                            _cE("view", _uM("class" to "trend-date"), _uA(
                                _cE("text", null, _tD(_ctx.formatTrendDate(item.date)), 1)
                            )),
                            _cE("view", _uM("class" to "trend-bars"), _uA(
                                _cE("view", _uM("class" to "trend-bar"), _uA(
                                    _cE("text", _uM("class" to "trend-label"), "新增"),
                                    _cE("view", _uM("class" to "trend-progress new", "style" to _nS(_uM("width" to (_ctx.getTrendWidth(item.newCount, "new") + "%")))), null, 4),
                                    _cE("text", _uM("class" to "trend-count"), _tD(item.newCount), 1)
                                )),
                                _cE("view", _uM("class" to "trend-bar"), _uA(
                                    _cE("text", _uM("class" to "trend-label"), "完成"),
                                    _cE("view", _uM("class" to "trend-progress completed", "style" to _nS(_uM("width" to (_ctx.getTrendWidth(item.completedCount, "completed") + "%")))), null, 4),
                                    _cE("text", _uM("class" to "trend-count"), _tD(item.completedCount), 1)
                                ))
                            ))
                        ))
                    }
                    ), 128)
                ))
            )),
            _cE("view", _uM("class" to "chart-section"), _uA(
                _cE("view", _uM("class" to "chart-header"), _uA(
                    _cE("text", _uM("class" to "chart-title"), "位置统计 TOP5")
                )),
                _cE("view", _uM("class" to "location-chart"), _uA(
                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.locationStats, fun(item, index, __index, _cached): Any {
                        return _cE("view", _uM("class" to "location-item", "key" to item.locationId), _uA(
                            _cE("view", _uM("class" to "location-rank"), _uA(
                                _cE("text", null, _tD(index + 1), 1)
                            )),
                            _cE("view", _uM("class" to "location-info"), _uA(
                                _cE("text", _uM("class" to "location-name"), _tD(item.locationName), 1),
                                _cE("view", _uM("class" to "location-bar"), _uA(
                                    _cE("view", _uM("class" to "location-progress", "style" to _nS(_uM("width" to (_ctx.getLocationWidth(item.recordCount) + "%")))), null, 4)
                                ))
                            )),
                            _cE("view", _uM("class" to "location-count"), _uA(
                                _cE("text", null, _tD(item.recordCount), 1)
                            ))
                        ))
                    }
                    ), 128),
                    if (_ctx.locationStats.length === 0) {
                        _cE("view", _uM("key" to 0, "class" to "empty-chart"), _uA(
                            _cE("text", null, "暂无数据")
                        ))
                    } else {
                        _cC("v-if", true)
                    }
                ))
            ))
        ))
    }
    open var currentPeriod: String by `$data`
    open var periodTabs: UTSArray<PeriodTab> by `$data`
    open var overview: OverviewData by `$data`
    open var statusDistribution: UTSArray<StatusDistributionItem> by `$data`
    open var severityDistribution: UTSArray<SeverityDistributionItem> by `$data`
    open var trendData: UTSArray<TrendDataItem> by `$data`
    open var locationStats: UTSArray<LocationStatsItem> by `$data`
    open var loading: Boolean by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("currentPeriod" to "month", "periodTabs" to _uA<PeriodTab>(PeriodTab(label = "本周", value = "week"), PeriodTab(label = "本月", value = "month"), PeriodTab(label = "本季", value = "quarter"), PeriodTab(label = "本年", value = "year")), "overview" to OverviewData(totalRecords = 0, completedTasks = 0, pendingRecords = 0, completionRate = 0), "statusDistribution" to _uA<StatusDistributionItem>(), "severityDistribution" to _uA<SeverityDistributionItem>(), "trendData" to _uA<TrendDataItem>(), "locationStats" to _uA<LocationStatsItem>(), "loading" to false)
    }
    open var checkLogin = ::gen_checkLogin_fn
    open fun gen_checkLogin_fn() {
        val token = uni_getStorageSync("token") as String
        if (token == null || token == "") {
            uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
        }
    }
    open var changePeriod = ::gen_changePeriod_fn
    open fun gen_changePeriod_fn(period: String) {
        this.currentPeriod = period
        this.loadStatistics()
    }
    open var getPeriodText = ::gen_getPeriodText_fn
    open fun gen_getPeriodText_fn(): String {
        if (this.currentPeriod == "week") {
            return "本周"
        }
        if (this.currentPeriod == "month") {
            return "本月"
        }
        if (this.currentPeriod == "quarter") {
            return "本季度"
        }
        if (this.currentPeriod == "year") {
            return "本年"
        }
        return "本月"
    }
    open var loadStatistics = ::gen_loadStatistics_fn
    open fun gen_loadStatistics_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend w@{
                if (this.loading) {
                    return@w
                }
                this.loading = true
                try {
                    val token = uni_getStorageSync("token") as String
                    await(this.loadOverview(token))
                    await(this.loadStatusDistribution(token))
                    await(this.loadSeverityDistribution(token))
                    await(this.loadTrendData(token))
                    await(this.loadLocationStats(token))
                }
                 catch (error: Throwable) {
                    console.error("加载统计数据失败:", error)
                    uni_showToast(ShowToastOptions(title = "加载失败", icon = "none"))
                }
                 finally{
                    this.loading = false
                }
        })
    }
    open var loadOverview = ::gen_loadOverview_fn
    open fun gen_loadOverview_fn(token: String) {
        uni_request<Any>(RequestOptions(url = getApiUrl("/statistics/overview?period=" + this.currentPeriod), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                val overviewData = data.getJSON("data") as UTSJSONObject
                this.overview = OverviewData(totalRecords = overviewData.getNumber("totalRecords") ?: 0, completedTasks = overviewData.getNumber("completedTasks") ?: 0, pendingRecords = overviewData.getNumber("pendingRecords") ?: 0, completionRate = overviewData.getNumber("completionRate") ?: 0)
            }
        }
        , fail = fun(error){
            console.error("加载概览数据失败:", error)
        }
        ))
    }
    open var loadStatusDistribution = ::gen_loadStatusDistribution_fn
    open fun gen_loadStatusDistribution_fn(token: String) {
        uni_request<Any>(RequestOptions(url = getApiUrl("/statistics/status-distribution?period=" + this.currentPeriod), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                val dataArray = data.getArray("data") as UTSArray<UTSJSONObject>
                this.statusDistribution = _uA()
                run {
                    var i: Number = 0
                    while(i < dataArray.length){
                        val item = dataArray[i]
                        this.statusDistribution.push(StatusDistributionItem(status = item.getString("status") ?: "", count = item.getNumber("count") ?: 0, percentage = item.getNumber("percentage") ?: 0))
                        i++
                    }
                }
            }
        }
        , fail = fun(error){
            console.error("加载状态分布失败:", error)
        }
        ))
    }
    open var loadSeverityDistribution = ::gen_loadSeverityDistribution_fn
    open fun gen_loadSeverityDistribution_fn(token: String) {
        uni_request<Any>(RequestOptions(url = getApiUrl("/statistics/severity-distribution?period=" + this.currentPeriod), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                val dataArray = data.getArray("data") as UTSArray<UTSJSONObject>
                this.severityDistribution = _uA()
                run {
                    var i: Number = 0
                    while(i < dataArray.length){
                        val item = dataArray[i]
                        this.severityDistribution.push(SeverityDistributionItem(severity = item.getString("severity") ?: "", count = item.getNumber("count") ?: 0, percentage = item.getNumber("percentage") ?: 0))
                        i++
                    }
                }
            }
        }
        , fail = fun(error){
            console.error("加载严重程度分布失败:", error)
        }
        ))
    }
    open var loadTrendData = ::gen_loadTrendData_fn
    open fun gen_loadTrendData_fn(token: String) {
        uni_request<Any>(RequestOptions(url = getApiUrl("/statistics/trends?period=" + this.currentPeriod), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                val dataArray = data.getArray("data") as UTSArray<UTSJSONObject>
                this.trendData = _uA()
                run {
                    var i: Number = 0
                    while(i < dataArray.length){
                        val item = dataArray[i]
                        this.trendData.push(TrendDataItem(date = item.getString("date") ?: "", newCount = item.getNumber("newCount") ?: 0, completedCount = item.getNumber("completedCount") ?: 0))
                        i++
                    }
                }
            }
        }
        , fail = fun(error){
            console.error("加载趋势数据失败:", error)
        }
        ))
    }
    open var loadLocationStats = ::gen_loadLocationStats_fn
    open fun gen_loadLocationStats_fn(token: String) {
        uni_request<Any>(RequestOptions(url = getApiUrl("/statistics/location-stats?period=" + this.currentPeriod + "&limit=5"), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                val dataArray = data.getArray("data") as UTSArray<UTSJSONObject>
                this.locationStats = _uA()
                run {
                    var i: Number = 0
                    while(i < dataArray.length){
                        val item = dataArray[i]
                        this.locationStats.push(LocationStatsItem(locationId = item.getString("locationId") ?: "", locationName = item.getString("locationName") ?: "", recordCount = item.getNumber("count") ?: 0))
                        i++
                    }
                }
            }
        }
        , fail = fun(error){
            console.error("加载位置统计失败:", error)
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
    open var getStatusDotClass = ::gen_getStatusDotClass_fn
    open fun gen_getStatusDotClass_fn(status: String): String {
        if (status == "0") {
            return "dot-pending"
        }
        if (status == "1") {
            return "dot-processing"
        }
        if (status == "2") {
            return "dot-completed"
        }
        if (status == "3") {
            return "dot-accepted"
        }
        return "dot-unknown"
    }
    open var getSeverityText = ::gen_getSeverityText_fn
    open fun gen_getSeverityText_fn(severity: String): String {
        if (severity == "1") {
            return "低"
        }
        if (severity == "2") {
            return "中"
        }
        if (severity == "3") {
            return "高"
        }
        if (severity == "4") {
            return "紧急"
        }
        return "未知"
    }
    open var getSeverityDotClass = ::gen_getSeverityDotClass_fn
    open fun gen_getSeverityDotClass_fn(severity: String): String {
        if (severity == "1") {
            return "dot-low"
        }
        if (severity == "2") {
            return "dot-medium"
        }
        if (severity == "3") {
            return "dot-high"
        }
        if (severity == "4") {
            return "dot-urgent"
        }
        return "dot-unknown"
    }
    open var formatTrendDate = ::gen_formatTrendDate_fn
    open fun gen_formatTrendDate_fn(date: String): String {
        if (date == null || date == "") {
            return ""
        }
        val d = Date(date)
        return "" + (d.getMonth() + 1) + "/" + d.getDate()
    }
    open var getTrendWidth = ::gen_getTrendWidth_fn
    open fun gen_getTrendWidth_fn(count: Number, type: String): Number {
        if (this.trendData.length == 0) {
            return 0
        }
        var maxCount: Number = 0
        run {
            var i: Number = 0
            while(i < this.trendData.length){
                val item = this.trendData[i]
                val currentCount = if (type === "new") {
                    item.newCount
                } else {
                    item.completedCount
                }
                if (currentCount > maxCount) {
                    maxCount = currentCount
                }
                i++
            }
        }
        return if (maxCount > 0) {
            (count / maxCount) * 100
        } else {
            0
        }
    }
    open var getLocationWidth = ::gen_getLocationWidth_fn
    open fun gen_getLocationWidth_fn(count: Number): Number {
        if (this.locationStats.length == 0) {
            return 0
        }
        var maxCount: Number = 0
        run {
            var i: Number = 0
            while(i < this.locationStats.length){
                val item = this.locationStats[i]
                if (item.recordCount > maxCount) {
                    maxCount = item.recordCount
                }
                i++
            }
        }
        return if (maxCount > 0) {
            (count / maxCount) * 100
        } else {
            0
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
                return _uM("container" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "backgroundColor" to "#f5f5f5")), "filter-section" to _pS(_uM("marginBottom" to 20)), "time-filter" to _pS(_uM("display" to "flex", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 4, "paddingRight" to 4, "paddingBottom" to 4, "paddingLeft" to 4, "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "time-tab" to _uM("" to _uM("flex" to 1, "paddingTop" to 10, "paddingRight" to 15, "paddingBottom" to 10, "paddingLeft" to 15, "textAlign" to "center", "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "transitionProperty" to "all", "transitionDuration" to "0.3s"), ".active" to _uM("backgroundImage" to "none", "backgroundColor" to "#007AFF", "color" to "#ffffff")), "overview-section" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "marginBottom" to 20, "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "overview-title" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to 20, "paddingBottom" to 15, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0")), "period-text" to _pS(_uM("fontSize" to 14, "color" to "#666666")), "stats-grid" to _pS(_uM("display" to "flex", "flexWrap" to "wrap")), "stats-item" to _pS(_uM("flex" to 1, "minWidth" to 150, "backgroundImage" to "none", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 20, "paddingRight" to 15, "paddingBottom" to 20, "paddingLeft" to 15, "textAlign" to "center", "position" to "relative")), "stats-number" to _pS(_uM("fontSize" to 20, "fontWeight" to "bold", "color" to "#007AFF", "marginBottom" to 5)), "stats-label" to _pS(_uM("fontSize" to 12, "color" to "#666666")), "stats-icon" to _pS(_uM("position" to "absolute", "top" to 15, "right" to 15, "fontSize" to 20, "opacity" to 0.3)), "chart-section" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "marginBottom" to 20, "boxShadow" to "0 2px 8px rgba(0,0,0,0.1)")), "chart-header" to _pS(_uM("marginBottom" to 20, "paddingBottom" to 15, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0")), "chart-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333")), "status-item" to _pS(_uM("marginBottom" to 15, "marginBottom:last-child" to 0)), "severity-item" to _pS(_uM("marginBottom" to 15, "marginBottom:last-child:last-child" to 0)), "status-info" to _pS(_uM("display" to "flex", "alignItems" to "center", "marginBottom" to 8)), "severity-info" to _pS(_uM("display" to "flex", "alignItems" to "center", "marginBottom" to 8)), "status-dot" to _pS(_uM("width" to 12, "height" to 12, "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "marginRight" to 10)), "severity-dot" to _pS(_uM("width" to 12, "height" to 12, "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "marginRight" to 10)), "dot-pending" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffc107")), "dot-processing" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#17a2b8")), "dot-completed" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#28a745")), "dot-accepted" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#6c757d")), "dot-low" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#28a745")), "dot-medium" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffc107")), "dot-high" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#fd7e14")), "dot-urgent" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#dc3545")), "status-name" to _pS(_uM("fontSize" to 14, "color" to "#333333", "flex" to 1)), "severity-name" to _pS(_uM("fontSize" to 14, "color" to "#333333", "flex" to 1)), "status-data" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "marginBottom" to 5)), "severity-data" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "marginBottom" to 5)), "status-count" to _pS(_uM("fontSize" to 16, "fontWeight" to "bold", "color" to "#333333")), "severity-count" to _pS(_uM("fontSize" to 16, "fontWeight" to "bold", "color" to "#333333")), "status-percent" to _pS(_uM("fontSize" to 14, "color" to "#666666")), "severity-percent" to _pS(_uM("fontSize" to 14, "color" to "#666666")), "status-bar" to _pS(_uM("height" to 6, "backgroundImage" to "none", "backgroundColor" to "#f0f0f0", "borderTopLeftRadius" to 3, "borderTopRightRadius" to 3, "borderBottomRightRadius" to 3, "borderBottomLeftRadius" to 3, "overflow" to "hidden")), "severity-bar" to _pS(_uM("height" to 6, "backgroundImage" to "none", "backgroundColor" to "#f0f0f0", "borderTopLeftRadius" to 3, "borderTopRightRadius" to 3, "borderBottomRightRadius" to 3, "borderBottomLeftRadius" to 3, "overflow" to "hidden")), "status-progress" to _pS(_uM("height" to "100%", "borderTopLeftRadius" to 3, "borderTopRightRadius" to 3, "borderBottomRightRadius" to 3, "borderBottomLeftRadius" to 3, "transitionProperty" to "width", "transitionDuration" to "0.3s")), "severity-progress" to _pS(_uM("height" to "100%", "borderTopLeftRadius" to 3, "borderTopRightRadius" to 3, "borderBottomRightRadius" to 3, "borderBottomLeftRadius" to 3, "transitionProperty" to "width", "transitionDuration" to "0.3s")), "trend-item" to _pS(_uM("marginBottom" to 20, "marginBottom:last-child" to 0)), "trend-date" to _pS(_uM("marginBottom" to 10)), "trend-bar" to _pS(_uM("display" to "flex", "alignItems" to "center", "marginBottom" to 8)), "trend-label" to _pS(_uM("fontSize" to 12, "color" to "#666666", "minWidth" to 40, "marginRight" to 10)), "trend-progress" to _uM("" to _uM("height" to 20, "borderTopLeftRadius" to 10, "borderTopRightRadius" to 10, "borderBottomRightRadius" to 10, "borderBottomLeftRadius" to 10, "marginRight" to 10, "transitionProperty" to "width", "transitionDuration" to "0.3s", "flex" to 1, "maxWidth" to 200), ".new" to _uM("backgroundImage" to "none", "backgroundColor" to "#007AFF"), ".completed" to _uM("backgroundImage" to "none", "backgroundColor" to "#28a745")), "trend-count" to _pS(_uM("fontSize" to 12, "color" to "#333333", "minWidth" to 30, "textAlign" to "right")), "location-item" to _pS(_uM("display" to "flex", "alignItems" to "center", "marginBottom" to 15, "marginBottom:last-child" to 0)), "location-rank" to _pS(_uM("width" to 24, "height" to 24, "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "backgroundImage" to "none", "backgroundColor" to "#007AFF", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "marginRight" to 15)), "location-info" to _pS(_uM("flex" to 1, "marginRight" to 15)), "location-name" to _pS(_uM("fontSize" to 14, "color" to "#333333", "marginBottom" to 5)), "location-bar" to _pS(_uM("height" to 6, "backgroundImage" to "none", "backgroundColor" to "#f0f0f0", "borderTopLeftRadius" to 3, "borderTopRightRadius" to 3, "borderBottomRightRadius" to 3, "borderBottomLeftRadius" to 3, "overflow" to "hidden")), "location-progress" to _pS(_uM("height" to "100%", "backgroundImage" to "none", "backgroundColor" to "#007AFF", "borderTopLeftRadius" to 3, "borderTopRightRadius" to 3, "borderBottomRightRadius" to 3, "borderBottomLeftRadius" to 3, "transitionProperty" to "width", "transitionDuration" to "0.3s")), "location-count" to _pS(_uM("minWidth" to 30, "textAlign" to "right")), "empty-chart" to _pS(_uM("textAlign" to "center", "paddingTop" to 40, "paddingRight" to 0, "paddingBottom" to 40, "paddingLeft" to 0)), "@TRANSITION" to _uM("time-tab" to _uM("property" to "all", "duration" to "0.3s"), "status-progress" to _uM("property" to "width", "duration" to "0.3s"), "severity-progress" to _uM("property" to "width", "duration" to "0.3s"), "trend-progress" to _uM("property" to "width", "duration" to "0.3s"), "location-progress" to _uM("property" to "width", "duration" to "0.3s")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
