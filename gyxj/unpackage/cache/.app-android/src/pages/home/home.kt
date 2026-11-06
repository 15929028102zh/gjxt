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
import io.dcloud.uniapp.extapi.getSystemInfo as uni_getSystemInfo
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
open class GenPagesHomeHome : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesHomeHome) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesHomeHome
            val _cache = __ins.renderCache
            val userInfo = ref(_uO())
            val userName = ref("用户")
            val currentDate = ref("加载中...")
            val weeklyStats = ref(StatsData(total = 45, pending = 12, completed = 28, reviewed = 5))
            val monthlyStats = ref(StatsData(total = 156, pending = 23, completed = 108, reviewed = 25))
            var weeklyChartInstance: TuiCharts? = null
            var monthlyChartInstance: TuiCharts? = null
            val weeklyChartOption = ref(_uO())
            val monthlyChartOption = ref(_uO())
            val weeklyChart = ref(null)
            val monthlyChart = ref(null)
            val scrollTop = ref(0)
            val containerHeight = ref(0)
            val isRefreshing = ref(false)
            val updateWeeklyChart = fun(): Unit {
                if (weeklyChartInstance != null) {
                    val weeklyPieChart = weeklyChartInstance!!.getDrawCharts(0)
                    if (weeklyPieChart != null) {
                        weeklyPieChart.update(_uO("series" to _uA(
                            _uO("name" to "待整改", "data" to _uA(
                                _uO("value" to weeklyStats.value.pending)
                            )),
                            _uO("name" to "已整改", "data" to _uA(
                                _uO("value" to weeklyStats.value.completed)
                            )),
                            _uO("name" to "已复查", "data" to _uA(
                                _uO("value" to weeklyStats.value.reviewed)
                            ))
                        )))
                    }
                }
            }
            val updateMonthlyChart = fun(): Unit {
                if (monthlyChartInstance != null) {
                    val monthlyPieChart = monthlyChartInstance!!.getDrawCharts(0)
                    if (monthlyPieChart != null) {
                        monthlyPieChart.update(_uO("series" to _uA(
                            _uO("name" to "待整改", "data" to _uA(
                                _uO("value" to monthlyStats.value.pending)
                            )),
                            _uO("name" to "已整改", "data" to _uA(
                                _uO("value" to monthlyStats.value.completed)
                            )),
                            _uO("name" to "已复查", "data" to _uA(
                                _uO("value" to monthlyStats.value.reviewed)
                            ))
                        )))
                    }
                }
            }
            val loadWeeklyStats = fun(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w1@{
                        try {
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
                                    val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/home/home.uvue:260") as UTSJSONObject
                                    departmentId = parsedUserInfo.getString("deptId") ?: ""
                                }
                                 catch (error: Throwable) {
                                    console.error("用户信息JSON解析失败:", error, " at pages/home/home.uvue:263")
                                }
                            }
                            var pending: Number = 0
                            var completed: Number = 0
                            var reviewed: Number = 0
                            var total: Number = 0
                            var completedRequests: Number = 0
                            val totalRequests: Number = 4
                            val updateWeeklyStats = fun(){
                                weeklyStats.value = StatsData(total = total, pending = pending, completed = completed, reviewed = reviewed)
                                updateWeeklyChart()
                            }
                            if (token == null || token == "") {
                                weeklyStats.value = StatsData(total = 45, pending = 12, completed = 28, reviewed = 5)
                                updateWeeklyChart()
                                return@w1
                            }
                            val deptParam = if ((!isAdmin && departmentId != null && departmentId != "")) {
                                "&departmentId=" + departmentId
                            } else {
                                ""
                            }
                            val pendingUrl = getApiUrl("/inspection-record/page?current=1&size=1&status=0" + (if ((!isAdmin && departmentId != null && departmentId != "")) {
                                "&departmentId=" + departmentId
                            } else {
                                ""
                            }
                            ))
                            uni_request<Any>(RequestOptions(url = pendingUrl, method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
                                if (res.statusCode === 200 && res.data != null) {
                                    val response = res.data as UTSJSONObject
                                    if ((response.getNumber("code") ?: 0) === 200) {
                                        val data = response.getJSON("data") as UTSJSONObject
                                        pending = data.getNumber("total") ?: 0
                                    }
                                }
                                completedRequests++
                                if (completedRequests === totalRequests) {
                                    updateWeeklyStats()
                                }
                            }
                            , fail = fun(error){
                                console.error("获取待整改数量失败:", error, " at pages/home/home.uvue:322")
                                completedRequests++
                                if (completedRequests === totalRequests) {
                                    updateWeeklyStats()
                                }
                            }
                            ))
                            val completedUrl = getApiUrl("/inspection-record/page?current=1&size=1&status=1" + (if ((!isAdmin && departmentId != null && departmentId != "")) {
                                "&departmentId=" + departmentId
                            } else {
                                ""
                            }
                            ))
                            uni_request<Any>(RequestOptions(url = completedUrl, method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
                                if (res.statusCode === 200 && res.data != null) {
                                    val response = res.data as UTSJSONObject
                                    if ((response.getNumber("code") ?: 0) === 200) {
                                        val data = response.getJSON("data") as UTSJSONObject
                                        completed = data.getNumber("total") ?: 0
                                    }
                                }
                                completedRequests++
                                if (completedRequests === totalRequests) {
                                    updateWeeklyStats()
                                }
                            }
                            , fail = fun(error){
                                console.error("获取已整改数量失败:", error, " at pages/home/home.uvue:352")
                                completedRequests++
                                if (completedRequests === totalRequests) {
                                    updateWeeklyStats()
                                }
                            }
                            ))
                            val reviewedUrl = getApiUrl("/inspection-record/page?current=1&size=1&status=2" + (if ((!isAdmin && departmentId != null && departmentId != "")) {
                                "&departmentId=" + departmentId
                            } else {
                                ""
                            }
                            ))
                            uni_request<Any>(RequestOptions(url = reviewedUrl, method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
                                if (res.statusCode === 200 && res.data != null) {
                                    val response = res.data as UTSJSONObject
                                    if ((response.getNumber("code") ?: 0) === 200) {
                                        val data = response.getJSON("data") as UTSJSONObject
                                        reviewed = data.getNumber("total") ?: 0
                                    }
                                }
                                completedRequests++
                                if (completedRequests === totalRequests) {
                                    updateWeeklyStats()
                                }
                            }
                            , fail = fun(error){
                                console.error("获取已复查数量失败:", error, " at pages/home/home.uvue:382")
                                completedRequests++
                                if (completedRequests === totalRequests) {
                                    updateWeeklyStats()
                                }
                            }
                            ))
                            val totalUrl = getApiUrl("/inspection-record/page?current=1&size=1" + (if ((!isAdmin && departmentId != null && departmentId != "")) {
                                "&departmentId=" + departmentId
                            } else {
                                ""
                            }
                            ))
                            uni_request<Any>(RequestOptions(url = totalUrl, method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
                                if (res.statusCode === 200 && res.data != null) {
                                    val response = res.data as UTSJSONObject
                                    if ((response.getNumber("code") ?: 0) === 200) {
                                        val data = response.getJSON("data") as UTSJSONObject
                                        total = data.getNumber("total") ?: 0
                                    }
                                }
                                completedRequests++
                                if (completedRequests === totalRequests) {
                                    updateWeeklyStats()
                                }
                            }
                            , fail = fun(error){
                                console.error("获取总记录数量失败:", error, " at pages/home/home.uvue:412")
                                completedRequests++
                                if (completedRequests === totalRequests) {
                                    updateWeeklyStats()
                                }
                            }
                            ))
                        }
                         catch (error: Throwable) {
                            console.error("加载周检统计数据失败:", error, " at pages/home/home.uvue:421")
                            weeklyStats.value = StatsData(total = 45, pending = 12, completed = 28, reviewed = 5)
                            updateWeeklyChart()
                        }
                })
            }
            val loadMonthlyStats = fun(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        try {
                            val token = uni_getStorageSync("token")
                            val deptParam = ""
                            var pending: Number = 0
                            var completed: Number = 0
                            var reviewed: Number = 0
                            var finished: Number = 0
                            val totalReq: Number = 3
                            val updateMonthly = fun(){
                                monthlyStats.value = StatsData(total = pending + completed + reviewed, pending = pending, completed = completed, reviewed = reviewed)
                                updateMonthlyChart()
                            }
                            val req = fun(status: Number, assign: (n: Number) -> Unit){
                                uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/page?current=1&size=1&recordStatus=" + status + deptParam), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
                                    val resp = res.data as UTSJSONObject
                                    if (res.statusCode === 200 && (resp.getNumber("code") ?: 0) === 200) {
                                        val d = resp.getJSON("data") as UTSJSONObject
                                        assign(d.getNumber("total") ?: 0)
                                    }
                                    finished++
                                    if (finished === totalReq) {
                                        updateMonthly()
                                    }
                                }
                                , fail = fun(_){
                                    finished++
                                    if (finished === totalReq) {
                                        updateMonthly()
                                    }
                                }
                                ))
                            }
                            req(0, fun(n: Number){
                                pending = n
                            }
                            )
                            req(1, fun(n: Number){
                                completed = n
                            }
                            )
                            req(2, fun(n: Number){
                                reviewed = n
                            }
                            )
                        }
                         catch (error: Throwable) {
                            console.error("加载月检统计数据失败:", error, " at pages/home/home.uvue:472")
                            monthlyStats.value = StatsData(total = 0, pending = 0, completed = 0, reviewed = 0)
                            updateMonthlyChart()
                        }
                })
            }
            val checkLogin = fun(): Boolean {
                val token = uni_getStorageSync("token")
                if (token == null || token == "") {
                    console.log("未登录，跳转到登录页面", " at pages/home/home.uvue:484")
                    uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
                    return false
                }
                return true
            }
            val loadUserInfo = fun(): Unit {
                console.log("=== 开始加载用户信息 ===", " at pages/home/home.uvue:495")
                val userInfoStr = uni_getStorageSync("userInfo") as String
                console.log("本地存储原始数据:", userInfoStr, " at pages/home/home.uvue:499")
                if (userInfoStr != null && userInfoStr != "" && userInfoStr != "null" && userInfoStr != "undefined") {
                    try {
                        val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/home/home.uvue:503") as UTSJSONObject
                        console.log("解析后的对象:", parsedUserInfo, " at pages/home/home.uvue:504")
                        val realName = parsedUserInfo.getString("realName")
                        console.log("realName字段值:", realName, " at pages/home/home.uvue:508")
                        console.log("realName类型:", UTSAndroid.`typeof`(realName), " at pages/home/home.uvue:509")
                        userInfo.value = parsedUserInfo
                        val realNameValue = parsedUserInfo.getString("realName")
                        userName.value = if (realNameValue != null) {
                            realNameValue
                        } else {
                            "用户"
                        }
                        console.log("设置后的 userInfo.value:", userInfo.value, " at pages/home/home.uvue:516")
                        console.log("设置后的 userName.value:", userName.value, " at pages/home/home.uvue:517")
                        val testRealName = userInfo.value.getString("realName")
                        console.log("验证: 设置后的realName:", testRealName, " at pages/home/home.uvue:521")
                        nextTick(fun(){
                            console.log("界面更新后检查:", userInfo.value.getString("realName"), " at pages/home/home.uvue:525")
                            userInfo.value = UTSJSONObject.assign(_uO(), userInfo.value)
                        })
                    } catch (error: Throwable) {
                        console.error("用户信息JSON解析失败:", error, " at pages/home/home.uvue:531")
                        userInfo.value = _uO("realName" to "解析失败用户")
                    }
                } else {
                    console.log("本地存储中没有有效的用户信息", " at pages/home/home.uvue:537")
                    userInfo.value = _uO("realName" to "默认用户", "avatar" to "")
                    userName.value = "默认用户"
                }
                console.log("=== 用户信息加载完成 ===", " at pages/home/home.uvue:546")
            }
            val initCurrentDate = fun(): Unit {
                val now = Date()
                val year = now.getFullYear()
                val month = (now.getMonth() + 1).toString(10).padStart(2, "0")
                val day = now.getDate().toString(10).padStart(2, "0")
                val hours = now.getHours().toString(10).padStart(2, "0")
                val minutes = now.getMinutes().toString(10).padStart(2, "0")
                currentDate.value = "" + year + "-" + month + "-" + day + " " + hours + ":" + minutes
            }
            val timeInterval = ref<Number?>(null)
            val startTimeUpdate = fun(): Unit {
                initCurrentDate()
                timeInterval.value = setInterval(fun(){
                    initCurrentDate()
                }
                , 60000)
            }
            val stopTimeUpdate = fun(): Unit {
                val intervalId = timeInterval.value
                if (intervalId != null) {
                    clearInterval(intervalId)
                    timeInterval.value = null
                }
            }
            val initContainerHeight = fun(): Unit {
                uni_getSystemInfo(GetSystemInfoOptions(success = fun(res){
                    containerHeight.value = res.windowHeight
                }
                ))
            }
            val reloadAllData = fun(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        console.log("重新加载所有数据", " at pages/home/home.uvue:591")
                        try {
                            initCurrentDate()
                            loadUserInfo()
                            await(UTSPromise.all(_uA(
                                loadWeeklyStats(),
                                UTSPromise<Unit>(fun(resolve, _reject){
                                    loadMonthlyStats()
                                    setTimeout(fun(){
                                        resolve(Unit)
                                    }
                                    , 800)
                                }
                                )
                            )))
                            console.log("所有数据加载完成", " at pages/home/home.uvue:612")
                        }
                         catch (error: Throwable) {
                            console.error("数据加载失败:", error, " at pages/home/home.uvue:615")
                        }
                         finally {
                            setTimeout(fun(){
                                console.log("关闭刷新动画", " at pages/home/home.uvue:619")
                                isRefreshing.value = false
                            }
                            , 500)
                        }
                })
            }
            val onRefresh = fun(): Unit {
                console.log("开始下拉刷新", " at pages/home/home.uvue:627")
                isRefreshing.value = true
                reloadAllData()
            }
            val onRefreshRestore = fun(): Unit {
                console.log("刷新恢复", " at pages/home/home.uvue:636")
                isRefreshing.value = false
            }
            onMounted(fun(){
                val loginStatus = checkLogin()
                if (loginStatus == false) {
                    return
                }
                initCurrentDate()
                startTimeUpdate()
                initContainerHeight()
                refreshUserInfo().then(fun(){
                    loadUserInfo()
                    nextTick(fun(){
                        loadWeeklyStats()
                        loadMonthlyStats()
                    }
                    )
                }
                )
            }
            )
            onUnmounted(fun(){
                stopTimeUpdate()
            }
            )
            val onWeeklyChartInit = fun(chartInstance: TuiCharts): Unit {
                weeklyChartInstance = chartInstance
                val pieOption: UTSJSONObject = _uO("__\$originalPosition" to UTSSourceMapPosition("pieOption", "pages/home/home.uvue", 683, 9), "type" to "pie", "animation" to true, "dataLabel" to true, "fontSize" to 12, "padding" to _uA(
                    15,
                    15,
                    15,
                    15
                ), "title" to _uO("name" to "周检统计分布", "fontSize" to 14, "color" to "#2c3e50", "offsetY" to 5), "extra" to _uO("pie" to _uO("activeOpacity" to 0.5, "activeRadius" to 8, "offsetAngle" to 0, "labelWidth" to 15, "border" to true, "borderWidth" to 2, "borderColor" to "#FFFFFF", "linearType" to "custom"), "tooltip" to _uO("show" to true)), "series" to _uA(
                    _uO("name" to "待整改", "data" to _uA(
                        _uO("value" to weeklyStats.value.pending)
                    )),
                    _uO("name" to "已整改", "data" to _uA(
                        _uO("value" to weeklyStats.value.completed)
                    )),
                    _uO("name" to "已复查", "data" to _uA(
                        _uO("value" to weeklyStats.value.reviewed)
                    ))
                ), "xAxis" to _uO("disableGrid" to true), "yAxis" to _uO("data" to _uA(
                    _uO("min" to 0)
                )))
                val weeklyPieChart = chartInstance.add(0, pieOption)
                weeklyPieChart.setCoordinates(_uO("height" to 280, "width" to 260, "xOffset" to 15, "yOffset" to 35))
                weeklyPieChart.draw()
            }
            val onMonthlyChartInit = fun(chartInstance: TuiCharts): Unit {
                monthlyChartInstance = chartInstance
                val pieOption: UTSJSONObject = _uO("__\$originalPosition" to UTSSourceMapPosition("pieOption", "pages/home/home.uvue", 757, 9), "type" to "pie", "animation" to true, "dataLabel" to true, "fontSize" to 12, "padding" to _uA(
                    15,
                    15,
                    15,
                    15
                ), "title" to _uO("name" to "月检统计分布", "fontSize" to 14, "color" to "#2c3e50", "offsetY" to 5), "extra" to _uO("pie" to _uO("activeOpacity" to 0.5, "activeRadius" to 8, "offsetAngle" to 0, "labelWidth" to 15, "border" to true, "borderWidth" to 2, "borderColor" to "#FFFFFF", "linearType" to "custom"), "tooltip" to _uO("show" to true)), "series" to _uA(
                    _uO("name" to "待整改", "data" to _uA(
                        _uO("value" to monthlyStats.value.pending)
                    )),
                    _uO("name" to "已整改", "data" to _uA(
                        _uO("value" to monthlyStats.value.completed)
                    )),
                    _uO("name" to "已复查", "data" to _uA(
                        _uO("value" to monthlyStats.value.reviewed)
                    ))
                ), "xAxis" to _uO("disableGrid" to true), "yAxis" to _uO("data" to _uA(
                    _uO("min" to 0)
                )))
                val monthlyPieChart = chartInstance.add(0, pieOption)
                monthlyPieChart.setCoordinates(_uO("height" to 280, "width" to 260, "xOffset" to 15, "yOffset" to 35))
                monthlyPieChart.draw()
            }
            val goToWeeklyPending = fun(): Unit {
                uni_navigateTo(NavigateToOptions(url = "/pages/weekly/weekly?status=pending"))
            }
            val goToWeeklyCompleted = fun(): Unit {
                uni_navigateTo(NavigateToOptions(url = "/pages/weekly/weekly?status=completed"))
            }
            val goToWeeklyReviewed = fun(): Unit {
                uni_navigateTo(NavigateToOptions(url = "/pages/weekly/weekly?status=reviewed"))
            }
            val goToMonthlyPending = fun(): Unit {
                uni_navigateTo(NavigateToOptions(url = "/pages/monthly/monthly?status=pending"))
            }
            val goToMonthlyCompleted = fun(): Unit {
                uni_navigateTo(NavigateToOptions(url = "/pages/monthly/monthly?status=completed"))
            }
            val goToMonthlyReviewed = fun(): Unit {
                uni_navigateTo(NavigateToOptions(url = "/pages/monthly/monthly?status=reviewed"))
            }
            return fun(): Any? {
                val _component_tui_xechars = resolveEasyComponent("tui-xechars", GenUniModulesTuiXecharsComponentsTuiXecharsTuiXecharsClass)
                return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "scroll-top" to scrollTop.value, "style" to _nS(_uM("height" to (containerHeight.value + "px"))), "enable-back-to-top" to "true", "refresher-enabled" to "true", "refresher-triggered" to isRefreshing.value, "refresher-default-style" to "black", "refresher-background" to "#f5f5f5", "onRefresherrefresh" to onRefresh, "onRefresherrestore" to onRefreshRestore), _uA(
                    _cE("view", _uM("class" to "header"), _uA(
                        _cE("view", _uM("class" to "welcome"), _uA(
                            _cE("text", _uM("class" to "greeting"), "欢迎回来，" + _tD(userName.value), 1),
                            _cE("text", _uM("class" to "app-name"), _tD(currentDate.value), 1)
                        )),
                        _cE("image", _uM("class" to "avatar", "src" to if ((userInfo.value.getString("avatar") ?: "") != "") {
                            (userInfo.value.getString("avatar") ?: "")
                        } else {
                            "/static/default-avatar.svg"
                        }
                        , "mode" to "aspectFill"), null, 8, _uA(
                            "src"
                        ))
                    )),
                    _cE("view", _uM("class" to "section"), _uA(
                        _cE("view", _uM("class" to "section-title"), _uA(
                            _cE("text", _uM("class" to "title-text"), "周检统计")
                        )),
                        _cE("view", _uM("class" to "stats-container"), _uA(
                            _cE("view", _uM("class" to "stat-card", "onClick" to goToWeeklyPending), _uA(
                                _cE("text", _uM("class" to "stat-number"), _tD(weeklyStats.value.pending), 1),
                                _cE("text", _uM("class" to "stat-label"), "待整改数")
                            )),
                            _cE("view", _uM("class" to "stat-card", "onClick" to goToWeeklyCompleted), _uA(
                                _cE("text", _uM("class" to "stat-number"), _tD(weeklyStats.value.completed), 1),
                                _cE("text", _uM("class" to "stat-label"), "已整改数")
                            )),
                            _cE("view", _uM("class" to "stat-card", "onClick" to goToWeeklyReviewed), _uA(
                                _cE("text", _uM("class" to "stat-number"), _tD(weeklyStats.value.reviewed), 1),
                                _cE("text", _uM("class" to "stat-label"), "已复查数")
                            ))
                        )),
                        _cE("view", _uM("class" to "chart-container"), _uA(
                            _cE("view", _uM("class" to "chart-title"), _uA(
                                _cE("text", _uM("class" to "chart-title-text"), "周检记录总数: " + _tD(weeklyStats.value.total), 1)
                            )),
                            _cE("view", _uM("class" to "chart-wrapper"), _uA(
                                _cV(_component_tui_xechars, _uM("ref_key" to "weeklyChart", "ref" to weeklyChart, "style" to _nS(_uM("height" to "350px", "width" to "100%", "min-width" to "280px")), "class" to "chart-canvas", "onInitFinished" to onWeeklyChartInit), null, 8, _uA(
                                    "style"
                                ))
                            ))
                        ))
                    )),
                    _cE("view", _uM("class" to "section"), _uA(
                        _cE("view", _uM("class" to "section-title"), _uA(
                            _cE("text", _uM("class" to "title-text"), "月检统计")
                        )),
                        _cE("view", _uM("class" to "stats-container"), _uA(
                            _cE("view", _uM("class" to "stat-card", "onClick" to goToMonthlyPending), _uA(
                                _cE("text", _uM("class" to "stat-number"), _tD(monthlyStats.value.pending), 1),
                                _cE("text", _uM("class" to "stat-label"), "待整改数")
                            )),
                            _cE("view", _uM("class" to "stat-card", "onClick" to goToMonthlyCompleted), _uA(
                                _cE("text", _uM("class" to "stat-number"), _tD(monthlyStats.value.completed), 1),
                                _cE("text", _uM("class" to "stat-label"), "已整改数")
                            )),
                            _cE("view", _uM("class" to "stat-card", "onClick" to goToMonthlyReviewed), _uA(
                                _cE("text", _uM("class" to "stat-number"), _tD(monthlyStats.value.reviewed), 1),
                                _cE("text", _uM("class" to "stat-label"), "已复查数")
                            ))
                        )),
                        _cE("view", _uM("class" to "chart-container"), _uA(
                            _cE("view", _uM("class" to "chart-title"), _uA(
                                _cE("text", _uM("class" to "chart-title-text"), "月检记录总数: " + _tD(monthlyStats.value.total), 1)
                            )),
                            _cE("view", _uM("class" to "chart-wrapper"), _uA(
                                _cV(_component_tui_xechars, _uM("ref_key" to "monthlyChart", "ref" to monthlyChart, "style" to _nS(_uM("height" to "350px", "width" to "100%", "min-width" to "280px")), "class" to "chart-canvas", "onInitFinished" to onMonthlyChartInit), null, 8, _uA(
                                    "style"
                                ))
                            ))
                        ))
                    ))
                ), 44, _uA(
                    "scroll-top",
                    "refresher-triggered"
                ))
            }
        }
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("container" to _pS(_uM("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "150rpx", "paddingLeft" to "30rpx", "backgroundImage" to "linear-gradient(180deg, #eff6ff 0%, #dbeafe 100%)", "backgroundColor" to "rgba(0,0,0,0)", "boxSizing" to "border-box", "minHeight" to "1200rpx")), "header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "30rpx", "paddingRight" to "25rpx", "paddingBottom" to "30rpx", "paddingLeft" to "25rpx", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "marginBottom" to "30rpx", "boxShadow" to "0 12rpx 36rpx rgba(30, 64, 175, 0.18)", "position" to "relative", "overflow" to "hidden", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "width::before" to "8rpx", "height::before" to "100%", "backgroundImage::before" to "linear-gradient(180deg, #1e40af, #0ea5e9)", "backgroundColor::before" to "rgba(0,0,0,0)")), "welcome" to _pS(_uM("display" to "flex", "flexDirection" to "column", "flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "zIndex" to 2, "position" to "relative", "paddingLeft" to "12rpx")), "greeting" to _pS(_uM("color" to "#0a2540", "fontSize" to "36rpx", "fontWeight" to 700, "marginBottom" to "10rpx", "letterSpacing" to "1rpx")), "app-name" to _pS(_uM("color" to "#475569", "fontSize" to "26rpx", "fontWeight" to 500, "letterSpacing" to "0.3rpx")), "avatar" to _pS(_uM("width" to "100rpx", "height" to "100rpx", "borderTopLeftRadius" to "50rpx", "borderTopRightRadius" to "50rpx", "borderBottomRightRadius" to "50rpx", "borderBottomLeftRadius" to "50rpx", "backgroundColor" to "#eff6ff", "overflow" to "hidden", "borderTopWidth" to "3rpx", "borderRightWidth" to "3rpx", "borderBottomWidth" to "3rpx", "borderLeftWidth" to "3rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "boxShadow" to "0 6rpx 20rpx rgba(30, 64, 175, 0.12)", "zIndex" to 2, "position" to "relative", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "transform:active" to "scale(0.95)")), "section" to _pS(_uM("marginBottom" to "40rpx", "paddingTop" to 0, "paddingRight" to "30rpx", "paddingBottom" to 0, "paddingLeft" to "30rpx")), "section-title" to _pS(_uM("marginBottom" to "25rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "space-between", "position" to "relative", "content::after" to "''", "position::after" to "absolute", "bottom::after" to "-10rpx", "left::after" to 0, "width::after" to "80rpx", "height::after" to "4rpx", "backgroundImage::after" to "linear-gradient(90deg, #1e40af, #0ea5e9)", "backgroundColor::after" to "rgba(0,0,0,0)", "borderTopLeftRadius::after" to "2rpx", "borderTopRightRadius::after" to "2rpx", "borderBottomRightRadius::after" to "2rpx", "borderBottomLeftRadius::after" to "2rpx")), "title-text" to _pS(_uM("fontSize" to "36rpx", "fontWeight" to 700, "color" to "#0f172a", "letterSpacing" to "0.8rpx")), "stats-container" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "marginBottom" to "30rpx")), "stat-card" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "backgroundImage" to "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "35rpx", "paddingRight" to "20rpx", "paddingBottom" to "35rpx", "paddingLeft" to "20rpx", "marginTop" to 0, "marginRight" to "8rpx", "marginBottom" to 0, "marginLeft" to "8rpx", "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "boxShadow" to "0 8rpx 25rpx rgba(30, 64, 175, 0.08), 0 3rpx 6rpx rgba(15, 23, 42, 0.04)", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "cubic-bezier(0.4,0,0.2,1)", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(255,255,255,0.8)", "borderRightColor" to "rgba(255,255,255,0.8)", "borderBottomColor" to "rgba(255,255,255,0.8)", "borderLeftColor" to "rgba(255,255,255,0.8)", "position" to "relative", "overflow" to "hidden", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "right::before" to 0, "height::before" to "4rpx", "backgroundImage::before" to "linear-gradient(90deg, #1e40af, #0ea5e9)", "backgroundColor::before" to "rgba(0,0,0,0)", "transform::before" to "scaleX(0)", "transitionProperty::before" to "transform", "transitionDuration::before" to "0.3s", "transitionTimingFunction::before" to "ease", "transform:active" to "translateY(-4rpx) scale(0.98)", "boxShadow:active" to "0 12rpx 35rpx rgba(30, 64, 175, 0.16), 0 6rpx 12rpx rgba(15, 23, 42, 0.08)", "transform:active::before" to "scaleX(1)")), "stat-number" to _pS(_uM("fontSize" to "52rpx", "fontWeight" to 700, "color" to "#0a2540", "marginBottom" to "12rpx", "letterSpacing" to "-1rpx")), "stat-label" to _pS(_uM("fontSize" to "22rpx", "color" to "#1e293b", "textAlign" to "center", "fontWeight" to 600, "letterSpacing" to "0.3rpx")), "chart-container" to _pS(_uM("marginTop" to "25rpx", "paddingTop" to "35rpx", "paddingRight" to "25rpx", "paddingBottom" to "35rpx", "paddingLeft" to "25rpx", "backgroundImage" to "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "boxShadow" to "0 12rpx 40rpx rgba(30, 64, 175, 0.08), 0 4rpx 12rpx rgba(15, 23, 42, 0.04)", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(255,255,255,0.8)", "borderRightColor" to "rgba(255,255,255,0.8)", "borderBottomColor" to "rgba(255,255,255,0.8)", "borderLeftColor" to "rgba(255,255,255,0.8)", "position" to "relative", "overflow" to "hidden", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "right::before" to 0, "height::before" to "2rpx", "backgroundImage::before" to "linear-gradient(90deg, #1e40af, #0ea5e9, #38bdf8)", "backgroundColor::before" to "rgba(0,0,0,0)")), "chart-title" to _pS(_uM("marginBottom" to "25rpx", "textAlign" to "center", "position" to "relative", "content::after" to "''", "position::after" to "absolute", "bottom::after" to "-10rpx", "left::after" to "50%", "transform::after" to "translateX(-50%)", "width::after" to "60rpx", "height::after" to "3rpx", "backgroundImage::after" to "linear-gradient(90deg, #1e40af, #0ea5e9)", "backgroundColor::after" to "rgba(0,0,0,0)", "borderTopLeftRadius::after" to "2rpx", "borderTopRightRadius::after" to "2rpx", "borderBottomRightRadius::after" to "2rpx", "borderBottomLeftRadius::after" to "2rpx")), "chart-title-text" to _pS(_uM("fontSize" to "32rpx", "fontWeight" to 600, "color" to "#0f172a", "letterSpacing" to "0.5rpx")), "chart-wrapper" to _pS(_uM("height" to "500rpx", "width" to "100%", "display" to "flex", "justifyContent" to "center", "alignItems" to "center", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "boxShadow" to "inset 0 2rpx 8rpx rgba(30, 64, 175, 0.04)", "paddingTop" to "15rpx", "paddingRight" to "15rpx", "paddingBottom" to "15rpx", "paddingLeft" to "15rpx", "minHeight" to "400rpx", "overflow" to "hidden")), "chart-canvas" to _pS(_uM("width" to "100%", "height" to "100%", "borderTopLeftRadius" to "8rpx", "borderTopRightRadius" to "8rpx", "borderBottomRightRadius" to "8rpx", "borderBottomLeftRadius" to "8rpx", "minHeight" to 350, "transform:hover" to "scale(1.01)")), "extra-content" to _pS(_uM("marginTop" to "40rpx")), "info-card" to _pS(_uM("backgroundImage" to "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "35rpx", "paddingRight" to "30rpx", "paddingBottom" to "35rpx", "paddingLeft" to "30rpx", "marginBottom" to "25rpx", "boxShadow" to "0 8rpx 25rpx rgba(30, 64, 175, 0.06), 0 3rpx 8rpx rgba(15, 23, 42, 0.04)", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "rgba(255,255,255,0.8)", "borderRightColor" to "rgba(255,255,255,0.8)", "borderBottomColor" to "rgba(255,255,255,0.8)", "borderLeftColor" to "rgba(255,255,255,0.8)", "position" to "relative", "overflow" to "hidden", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "width::before" to "6rpx", "height::before" to "100%", "backgroundImage::before" to "linear-gradient(180deg, #1e40af, #0ea5e9)", "backgroundColor::before" to "rgba(0,0,0,0)", "transform:active" to "translateY(-2rpx)", "boxShadow:active" to "0 12rpx 35rpx rgba(30, 64, 175, 0.12), 0 6rpx 12rpx rgba(15, 23, 42, 0.06)")), "info-title" to _pS(_uM("fontSize" to "30rpx", "fontWeight" to 600, "color" to "#0f172a", "marginBottom" to "18rpx", "letterSpacing" to "0.5rpx")), "info-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#475569", "lineHeight" to 1.6, "letterSpacing" to "0.3rpx")), "@TRANSITION" to _uM("avatar" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease"), "stat-card" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "cubic-bezier(0.4,0,0.2,1)", "property::before" to "transform", "duration::before" to "0.3s", "timingFunction::before" to "ease"), "info-card" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
