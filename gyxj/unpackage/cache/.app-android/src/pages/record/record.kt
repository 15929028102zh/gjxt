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
import io.dcloud.uniapp.extapi.`$off` as uni__off
import io.dcloud.uniapp.extapi.`$on` as uni__on
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfo as uni_getSystemInfo
import io.dcloud.uniapp.extapi.hideLoading as uni_hideLoading
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showActionSheet as uni_showActionSheet
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading
import io.dcloud.uniapp.extapi.showModal as uni_showModal
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.stopPullDownRefresh as uni_stopPullDownRefresh
open class GenPagesRecordRecord : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: OnLoadOptions) {
            this.checkLogin()
            if (options["status"] != null) {
                this.status = options["status"] as String
            }
            if (options["delayed"] != null && options["delayed"] == "true") {
                this.isDelayed = true
            }
            if (options["type"] != null && options["type"] == "reviewed") {
                this.isReviewed = true
                this.status = "2"
            }
            if (options["monthly"] != null && options["monthly"] == "true") {
                this.isMonthly = true
            }
            if (options["deadlineBefore"] != null) {
                this.deadlineBefore = options["deadlineBefore"] as String
            }
            if (options["deadlineFrom"] != null) {
                this.deadlineFrom = options["deadlineFrom"] as String
            }
            this.initContainerHeight()
            this.loadRecords()
            uni__on("recordUpdated", this.handleRecordUpdated)
        }
        , __ins)
        onPageShow(fun() {
            this.refreshRecords()
        }
        , __ins)
        onUnload(fun() {
            uni__off("recordUpdated", this.handleRecordUpdated)
        }
        , __ins)
        onPullDownRefresh(fun() {
            this.refreshRecords()
        }
        , __ins)
        onReachBottom(fun() {
            if (this.hasMore && !this.loadingMore) {
                this.loadMore()
            }
        }
        , __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "style" to _nS(_uM("height" to (_ctx.containerHeight + "px"))), "enable-back-to-top" to "true", "refresher-enabled" to "true", "refresher-triggered" to _ctx.isRefreshing, "refresher-default-style" to "black", "refresher-background" to "#f5f5f5", "onRefresherrefresh" to _ctx.onRefresh, "onRefresherrestore" to _ctx.onRefreshRestore), _uA(
            _cE("view", _uM("class" to "header"), _uA(
                _cE("text", _uM("class" to "title"), "问题列表"),
                _cE("text", _uM("class" to "subtitle"), "共 " + _tD(_ctx.totalCount) + " 条" + _tD(_ctx.getStatusTitle()) + "问题", 1)
            )),
            _cE(Fragment, null, RenderHelpers.renderList(_ctx.recordList, fun(record, __key, __index, _cached): Any {
                return _cE("view", _uM("class" to "record-item", "key" to record.id, "onClick" to fun(){
                    _ctx.goToDetail(record)
                }
                , "onLongpress" to fun(){
                    _ctx.onLongPressRow(record)
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
                        _cE("text", _uM("class" to "problem-text"), _tD(record.problemDescription), 1),
                        if (isTrue(record.rectifierName !== "" || record.reviewerName !== "")) {
                            _cE("view", _uM("key" to 0, "class" to "operator-row"), _uA(
                                if (record.rectifierName !== "") {
                                    _cE("text", _uM("key" to 0, "class" to "operator-text"), "整改人：" + _tD(record.rectifierName), 1)
                                } else {
                                    _cC("v-if", true)
                                },
                                if (record.reviewerName !== "") {
                                    _cE("text", _uM("key" to 1, "class" to "operator-text"), "复查人：" + _tD(record.reviewerName), 1)
                                } else {
                                    _cC("v-if", true)
                                }
                            ))
                        } else {
                            _cC("v-if", true)
                        }
                    ))
                ), 40, _uA(
                    "onClick",
                    "onLongpress"
                ))
            }
            ), 128),
            if (isTrue(_ctx.recordList.length === 0 && !_ctx.loading)) {
                _cE("view", _uM("key" to 0, "class" to "empty-state"), _uA(
                    _cE("text", _uM("class" to "empty-icon"), _tD(if (_ctx.isWaitingInput) {
                        "⏳"
                    } else {
                        "📝"
                    }), 1),
                    _cE("text", _uM("class" to "empty-text"), _tD(if (_ctx.isWaitingInput) {
                        "请等待检查单位录入（公示期尚未结束）"
                    } else {
                        "暂无记录"
                    }), 1)
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
            if (isTrue(_ctx.hasMore && _ctx.recordList.length > 0)) {
                _cE("view", _uM("key" to 2, "class" to "load-more", "onClick" to _ctx.loadMore), _uA(
                    _cE("text", _uM("class" to "load-more-text"), _tD(if (_ctx.loadingMore) {
                        "加载中..."
                    } else {
                        "加载更多"
                    }), 1)
                ), 8, _uA(
                    "onClick"
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
    open var recordList: UTSArray<InspectionRecord> by `$data`
    open var loading: Boolean by `$data`
    open var loadingMore: Boolean by `$data`
    open var hasMore: Boolean by `$data`
    open var totalCount: Number by `$data`
    open var pageNum: Number by `$data`
    open var pageSize: Number by `$data`
    open var status: String by `$data`
    open var isDelayed: Boolean by `$data`
    open var isReviewed: Boolean by `$data`
    open var isMonthly: Boolean by `$data`
    open var deadlineBefore: String by `$data`
    open var deadlineFrom: String by `$data`
    open var isRefreshing: Boolean by `$data`
    open var containerHeight: Number by `$data`
    open var currentUserDeptType: Number? by `$data`
    open var currentUserDeptId: String by `$data`
    open var taskPhaseCache: TaskPhaseCache by `$data`
    open var taskPublicityEndCache: TaskPublicityEndCache by `$data`
    open var hasAnyTaskInPhase4: Boolean by `$data`
    open var isWaitingInput: Boolean by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("recordList" to _uA<InspectionRecord>(), "loading" to false, "loadingMore" to false, "hasMore" to true, "totalCount" to 0, "pageNum" to 1, "pageSize" to 20, "status" to "0", "isDelayed" to false, "isReviewed" to false, "isMonthly" to false, "deadlineBefore" to "", "deadlineFrom" to "", "isRefreshing" to false, "containerHeight" to 0, "currentUserDeptType" to null as Number?, "currentUserDeptId" to "", "taskPhaseCache" to Map<String, Number>() as TaskPhaseCache, "taskPublicityEndCache" to Map<String, String>() as TaskPublicityEndCache, "hasAnyTaskInPhase4" to false, "isWaitingInput" to false)
    }
    open var checkLogin = ::gen_checkLogin_fn
    open fun gen_checkLogin_fn() {
        val tokenData = uni_getStorageSync("token")
        val token = if (tokenData != null) {
            tokenData as String
        } else {
            ""
        }
        if (token == null || token == "") {
            uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
        }
    }
    open var loadCurrentUserDept = ::gen_loadCurrentUserDept_fn
    open fun gen_loadCurrentUserDept_fn(): UTSPromise<Unit> {
        return UTSPromise<Unit>(fun(resolve, _reject){
            resolveCurrentDept().then(fun(dept: CurrentDept){
                this.currentUserDeptId = dept.deptId
                this.currentUserDeptType = dept.deptType
                resolve(Unit)
            }
            )
        }
        )
    }
    open var prefetchMonthlyTaskPhases = ::gen_prefetchMonthlyTaskPhases_fn
    open fun gen_prefetchMonthlyTaskPhases_fn(): UTSPromise<Unit> {
        return UTSPromise<Unit>(fun(resolve, _reject){
            if (!this.isMonthly) {
                resolve(Unit)
                return
            }
            val token = uni_getStorageSync("token")
            uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-task/page?current=1&size=500"), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
                if (res.statusCode === 200) {
                    val body = res.data as UTSJSONObject
                    val data = body.getJSON("data")
                    val records = if (data != null) {
                        data.getArray("records")
                    } else {
                        null
                    }
                    var hasPhase4 = false
                    if (records != null) {
                        run {
                            var i: Number = 0
                            while(i < records.length){
                                val t = records[i] as UTSJSONObject
                                val id = t.getString("id") ?: ""
                                val pubEndStr = t.getString("publicityEndTime") ?: ""
                                if (id != "" && pubEndStr != "") {
                                    val phase = this.computeTaskPhase(pubEndStr)
                                    this.taskPublicityEndCache.set(id, pubEndStr)
                                    this.taskPhaseCache.set(id, phase)
                                    if (phase === 4) {
                                        hasPhase4 = true
                                    }
                                }
                                i++
                            }
                        }
                    }
                    this.hasAnyTaskInPhase4 = hasPhase4
                }
                resolve(Unit)
            }
            , fail = fun(_){
                resolve(Unit)
            }
            ))
        }
        )
    }
    open var refreshRecordsTaskPhase = ::gen_refreshRecordsTaskPhase_fn
    open fun gen_refreshRecordsTaskPhase_fn() {
        run {
            var i: Number = 0
            while(i < this.recordList.length){
                val r: InspectionRecord = this.recordList[i]
                val id = r.taskId
                if (id != "" && this.taskPhaseCache.has(id)) {
                    r.taskPhase = this.taskPhaseCache.get(id) ?: 0
                    r.taskPublicityEndTime = this.taskPublicityEndCache.get(id) ?: ""
                }
                i++
            }
        }
    }
    open var isAnyMonthlyTaskInPhase4 = ::gen_isAnyMonthlyTaskInPhase4_fn
    open fun gen_isAnyMonthlyTaskInPhase4_fn(): Boolean {
        return this.hasAnyTaskInPhase4
    }
    open var computeTaskPhase = ::gen_computeTaskPhase_fn
    open fun gen_computeTaskPhase_fn(publicityEndTimeStr: String): Number {
        if (publicityEndTimeStr == "") {
            return 0
        }
        val parseLocalDate = fun(s: String): Date {
            return Date(s.replace(" ", "T"))
        }
        val pubEnd = parseLocalDate(publicityEndTimeStr)
        val now = Date()
        if (now > pubEnd) {
            return 4
        }
        return 3
    }
    open var isActionableForCurrentUser = ::gen_isActionableForCurrentUser_fn
    open fun gen_isActionableForCurrentUser_fn(record: InspectionRecord): Boolean {
        if (!this.isMonthly) {
            return true
        }
        if (this.currentUserDeptType === 1) {
            return true
        }
        if (this.currentUserDeptType === 0) {
            if (record.taskPhase === 0) {
                return false
            }
            return record.taskPhase === 4
        }
        return false
    }
    open var handleRecordUpdated = ::gen_handleRecordUpdated_fn
    open fun gen_handleRecordUpdated_fn() {
        console.log("接收到记录更新事件，开始刷新数据", " at pages/record/record.uvue:293")
        this.refreshRecords()
    }
    open var initContainerHeight = ::gen_initContainerHeight_fn
    open fun gen_initContainerHeight_fn() {
        uni_getSystemInfo(GetSystemInfoOptions(success = fun(res){
            this.containerHeight = res.windowHeight
        }
        ))
    }
    open var loadRecords = ::gen_loadRecords_fn
    open fun gen_loadRecords_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend w@{
                if (this.pageNum === 1) {
                    this.loading = true
                } else {
                    this.loadingMore = true
                }
                if (this.currentUserDeptType == null) {
                    await(this.loadCurrentUserDept())
                }
                if (this.isMonthly && this.taskPhaseCache.size === 0) {
                    await(this.prefetchMonthlyTaskPhases())
                }
                val token = uni_getStorageSync("token")
                var departmentId = ""
                val isWeeklyAdm = isWeeklyAdmin()
                val userInfoData = uni_getStorageSync("userInfo")
                val userInfoStr = if (userInfoData != null) {
                    userInfoData as String
                } else {
                    ""
                }
                if (userInfoStr != null && userInfoStr != "" && userInfoStr != "null" && userInfoStr != "undefined") {
                    try {
                        val parsedUserInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/record.uvue:332") as UTSJSONObject
                        departmentId = parsedUserInfo.getString("deptId") ?: ""
                    }
                     catch (error: Throwable) {
                        console.error("用户信息JSON解析失败:", error, " at pages/record/record.uvue:335")
                    }
                }
                var apiUrl = ""
                if (this.isMonthly && this.isDelayed) {
                    val dl = if ((this.deadlineBefore != null && this.deadlineBefore != "")) {
                        this.deadlineBefore
                    } else {
                        this.formatToday()
                    }
                    apiUrl = getApiUrl("/monthly-inspection-record/page?current=" + this.pageNum + "&size=" + this.pageSize + "&recordStatus=0&rectificationDeadlineBefore=" + dl)
                } else if (this.isDelayed) {
                    apiUrl = getApiUrl("/inspection-record/delayed?current=" + this.pageNum + "&size=" + this.pageSize + (if ((!isWeeklyAdm && departmentId != null && departmentId != "")) {
                        "&departmentId=" + departmentId
                    } else {
                        ""
                    }))
                } else if (this.isMonthly) {
                    this.isWaitingInput = false
                    if (isMonthlyAdmin()) {
                        apiUrl = getApiUrl("/monthly-inspection-record/page?current=" + this.pageNum + "&size=" + this.pageSize + "&recordStatus=" + this.status)
                    } else if (this.currentUserDeptType === 1 && this.currentUserDeptId != "") {
                        apiUrl = getApiUrl("/monthly-inspection-record/page?current=" + this.pageNum + "&size=" + this.pageSize + "&recordStatus=" + this.status + "&checkUnitId=" + this.currentUserDeptId)
                    } else if (this.currentUserDeptType === 0) {
                        if (!this.isAnyMonthlyTaskInPhase4()) {
                            this.recordList = _uA()
                            this.totalCount = 0
                            this.hasMore = false
                            this.isWaitingInput = true
                            this.loading = false
                            this.loadingMore = false
                            return@w
                        }
                        apiUrl = getApiUrl("/monthly-inspection-record/page?current=" + this.pageNum + "&size=" + this.pageSize + "&recordStatus=" + this.status + "&departmentId=" + this.currentUserDeptId)
                        this.isWaitingInput = false
                    } else {
                        apiUrl = getApiUrl("/monthly-inspection-record/page?current=" + this.pageNum + "&size=" + this.pageSize + "&recordStatus=" + this.status)
                    }
                } else {
                    apiUrl = getApiUrl("/inspection-record/page?current=" + this.pageNum + "&size=" + this.pageSize + "&status=" + this.status + (if ((!isWeeklyAdm && departmentId != null && departmentId != "")) {
                        "&departmentId=" + departmentId
                    } else {
                        ""
                    }
                    ))
                }
                uni_request<Any>(RequestOptions(url = apiUrl, method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
                    val responseData = res.data as UTSJSONObject
                    if (res.statusCode === 200 && responseData.getNumber("code") === 200) {
                        val dataObj = responseData.getJSON("data") as UTSJSONObject
                        val records = dataObj?.getArray("records") as UTSArray<UTSJSONObject>
                        val newRecords: UTSArray<InspectionRecord> = _uA()
                        if (records != null) {
                            run {
                                var i: Number = 0
                                while(i < records.length){
                                    val item = records[i]
                                    console.log("原始数据对象:", item, " at pages/record/record.uvue:396")
                                    console.log("problemDescription字段:", item.getString("problemDescription"), " at pages/record/record.uvue:397")
                                    val createTime = item.getString("createTime") ?: ""
                                    val rd0 = item.getString("rectificationDate") ?: ""
                                    val rd1 = if (rd0 != "") {
                                        rd0
                                    } else {
                                        (item.getString("rectification_date") ?: "")
                                    }
                                    val rd2 = if (rd1 != "") {
                                        rd1
                                    } else {
                                        (item.getString("rectificationDeadline") ?: "")
                                    }
                                    val rd3 = if (rd2 != "") {
                                        rd2
                                    } else {
                                        (item.getString("rectificationTime") ?: "")
                                    }
                                    val rd4 = if (rd3 != "") {
                                        rd3
                                    } else {
                                        (item.getString("deadline") ?: "")
                                    }
                                    val rd5 = if (rd4 != "") {
                                        rd4
                                    } else {
                                        (item.getString("rectifyDeadline") ?: "")
                                    }
                                    val record = InspectionRecord(id = (item.getString("id") ?: item.getString("recordId") ?: ""), problemDescription = item.getString("problemDescription") ?: "", responsiblePerson = item.getString("responsiblePerson") ?: "", inspectorName = item.getString("inspector") ?: "", createDate = this.formatDate(createTime), status = if (this.isMonthly) {
                                        (item.getNumber("recordStatus") ?: 0)
                                    } else {
                                        (item.getNumber("status") ?: 0)
                                    }
                                    , rectificationDate = this.normalizeDate(rd5), rectifierName = item.getString("rectifierName") ?: "", reviewerName = item.getString("reviewerName") ?: "", natureName = item.getString("natureName") ?: "", natureId = "", professionName = item.getString("professionName") ?: "", professionId = item.getString("professionId") ?: "", departmentName = item.getString("departmentName") ?: "", departmentId = item.getString("departmentId") ?: "", taskId = item.getString("taskId") ?: "", taskPhase = this.taskPhaseCache.get(item.getString("taskId") ?: "") ?: 0, taskPublicityEndTime = this.taskPublicityEndCache.get(item.getString("taskId") ?: "") ?: "")
                                    console.log("处理后的记录对象:", record, " at pages/record/record.uvue:429")
                                    newRecords.push(record)
                                    i++
                                }
                            }
                        }
                        var filtered = newRecords
                        if (this.isMonthly && !this.isDelayed && this.status === "0") {
                            val start = if ((this.deadlineFrom != null && this.deadlineFrom != "")) {
                                this.deadlineFrom
                            } else {
                                this.formatToday()
                            }
                            val todayInt = this.dateToInt(start)
                            filtered = newRecords.filter(fun(r): Boolean {
                                val d = if ((r.rectificationDate == null)) {
                                    ""
                                } else {
                                    r.rectificationDate
                                }
                                if (d === "") {
                                    return false
                                }
                                val di = this.dateToInt(d)
                                if (di < 0) {
                                    return false
                                }
                                return di >= todayInt
                            }
                            )
                        }
                        if (this.isMonthly && this.isDelayed) {
                            val today = this.formatToday()
                            val todayInt = this.dateToInt(today)
                            filtered = newRecords.filter(fun(r): Boolean {
                                val d = if ((r.rectificationDate == null)) {
                                    ""
                                } else {
                                    r.rectificationDate
                                }
                                if (d === "") {
                                    return false
                                }
                                val di = this.dateToInt(d)
                                if (di < 0) {
                                    return false
                                }
                                return (r.status === 0) && (di < todayInt)
                            }
                            )
                        }
                        if (this.pageNum === 1) {
                            this.recordList = filtered
                        } else {
                            this.recordList = this.recordList.concat(filtered)
                        }
                        val serverTotal = dataObj?.getNumber("total") ?: newRecords.length
                        if (this.isMonthly && !this.isDelayed && this.status === "0" && this.pageNum === 1) {
                            this.totalCount = filtered.length
                        } else {
                            this.totalCount = filtered.length
                        }
                        this.hasMore = filtered.length >= this.pageSize
                        if (this.isMonthly) {
                            this.refreshRecordsTaskPhase()
                        }
                    } else {
                        val errorMessage = responseData.getString("message") ?: ""
                        uni_showToast(ShowToastOptions(title = if (errorMessage != "") {
                            errorMessage
                        } else {
                            "加载失败"
                        }
                        , icon = "none"))
                    }
                    this.loading = false
                    this.loadingMore = false
                }
                , fail = fun(error){
                    console.error("加载记录失败:", error, " at pages/record/record.uvue:489")
                    uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
                    this.loading = false
                    this.loadingMore = false
                }
                ))
        })
    }
    open var getStatusText = ::gen_getStatusText_fn
    open fun gen_getStatusText_fn(status: Number?): String {
        if (status == null) {
            return "未知"
        }
        val statusMap: UTSJSONObject = _uO("__\$originalPosition" to UTSSourceMapPosition("statusMap", "pages/record/record.uvue", 502, 10), "0" to "待整改", "1" to "已整改", "2" to "已复查")
        val statusText = statusMap[status.toString(10)] as String
        return if (statusText != null) {
            statusText
        } else {
            "未知"
        }
    }
    open var formatDate = ::gen_formatDate_fn
    open fun gen_formatDate_fn(dateTimeStr: String): String {
        if (dateTimeStr == null || dateTimeStr == "") {
            return ""
        }
        if (dateTimeStr.includes(" ")) {
            return dateTimeStr.split(" ")[0]
        }
        return dateTimeStr
    }
    open var formatToday = ::gen_formatToday_fn
    open fun gen_formatToday_fn(): String {
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
        return "" + y + "-" + mm + "-" + dd
    }
    open var normalizeDate = ::gen_normalizeDate_fn
    open fun gen_normalizeDate_fn(s: String): String {
        if (s == null || s === "") {
            return ""
        }
        val t = s.replace("T", " ")
        val core = if (t.length >= 10) {
            t.substring(0, 10)
        } else {
            t
        }
        val parts = core.split("-")
        if (parts.length < 3) {
            return ""
        }
        val y = parts[0]
        val m = if (parts[1].length === 1) {
            ("0" + parts[1])
        } else {
            parts[1]
        }
        val d = if (parts[2].length === 1) {
            ("0" + parts[2])
        } else {
            parts[2]
        }
        return "" + y + "-" + m + "-" + d
    }
    open var dateToInt = ::gen_dateToInt_fn
    open fun gen_dateToInt_fn(s: String): Number {
        if (s == null || s === "") {
            return -1
        }
        val parts = s.split("-")
        if (parts.length < 3) {
            return -1
        }
        val y = parseInt(parts[0])
        val m = parseInt(parts[1])
        val d = parseInt(parts[2])
        if (isNaN(y) || isNaN(m) || isNaN(d)) {
            return -1
        }
        return (y * 10000) + (m * 100) + d
    }
    open var getStatusClass = ::gen_getStatusClass_fn
    open fun gen_getStatusClass_fn(status: Number?): String {
        if (status == null) {
            return "status-unknown"
        }
        val classMap: UTSJSONObject = _uO("__\$originalPosition" to UTSSourceMapPosition("classMap", "pages/record/record.uvue", 554, 11), "0" to "status-pending", "1" to "status-completed", "2" to "status-accepted")
        val statusClass = classMap[status.toString(10)] as String
        return if (statusClass != null) {
            statusClass
        } else {
            "status-unknown"
        }
    }
    open var getStatusTitle = ::gen_getStatusTitle_fn
    open fun gen_getStatusTitle_fn(): String {
        if (this.isDelayed) {
            return "延迟整改"
        }
        if (this.isMonthly) {
            if (this.status == "0") {
                return "月检待整改"
            }
            if (this.status == "1") {
                return "月检已整改"
            }
            if (this.status == "2") {
                return "月检已复查"
            }
            return "月检"
        }
        if (this.status == "0") {
            return "待整改"
        }
        if (this.status == "1") {
            return "已整改"
        }
        if (this.status == "2") {
            return "已复查"
        }
        return ""
    }
    open var buildActionItems = ::gen_buildActionItems_fn
    open fun gen_buildActionItems_fn(record: InspectionRecord): UTSArray<String> {
        val items: UTSArray<String> = _uA()
        val isEditablePhase = record.taskPhase === 1 || record.taskPhase === 2 || record.taskPhase === 3
        if (this.isMonthly && record.taskPhase === 4 && record.status === 0 && this.currentUserDeptType !== 1) {
            items.push("整改")
        }
        if (this.isMonthly && record.taskPhase === 4 && record.status === 1 && this.currentUserDeptType !== 0) {
            items.push("复查")
        }
        val MOBILE_EDIT_ENABLED: Boolean = false
        if (MOBILE_EDIT_ENABLED && this.isMonthly && this.currentUserDeptType === 1 && isEditablePhase) {
            items.push("编辑")
        }
        items.push("查看")
        if (record.status === 0 && !this.isReviewed) {
            items.push("删除")
        }
        return items
    }
    open var onLongPressRow = ::gen_onLongPressRow_fn
    open fun gen_onLongPressRow_fn(record: InspectionRecord) {
        val items = this.buildActionItems(record)
        if (items.length === 0) {
            uni_showToast(ShowToastOptions(title = "暂无可用操作", icon = "none"))
            return
        }
        val snapshot: InspectionRecord = record
        uni_showActionSheet(ShowActionSheetOptions(itemList = items, success = fun(res){
            val idx = res.tapIndex
            val choice = items[idx] ?: ""
            if (choice === "整改") {
                this.handleRectifyAction(snapshot)
            } else if (choice === "复查") {
                this.handleReviewAction(snapshot)
            } else if (choice === "编辑") {
                this.handleEditAction(snapshot)
            } else if (choice === "查看") {
                this.handleViewAction(snapshot)
            } else if (choice === "删除") {
                this.showDeleteConfirm(snapshot.id, snapshot.problemDescription)
            }
        }
        , fail = fun(_){}))
    }
    open var handleRectifyAction = ::gen_handleRectifyAction_fn
    open fun gen_handleRectifyAction_fn(record: InspectionRecord) {
        if (this.isMonthly) {
            uni_navigateTo(NavigateToOptions(url = "/pages/monthly/rectify?id=" + record.id))
        } else {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/detail?id=" + record.id))
        }
    }
    open var handleReviewAction = ::gen_handleReviewAction_fn
    open fun gen_handleReviewAction_fn(record: InspectionRecord) {
        if (this.isMonthly) {
            uni_navigateTo(NavigateToOptions(url = "/pages/monthly/review?id=" + record.id))
        } else {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/review?id=" + record.id + "&isMonthly=false"))
        }
    }
    open var handleEditAction = ::gen_handleEditAction_fn
    open fun gen_handleEditAction_fn(record: InspectionRecord) {
        if (this.isMonthly) {
            uni_navigateTo(NavigateToOptions(url = "/pages/monthly/task-record?recordId=" + record.id + "&mode=edit"))
        } else {
            uni_showToast(ShowToastOptions(title = "周检尚未支持编辑模式", icon = "none"))
        }
    }
    open var handleViewAction = ::gen_handleViewAction_fn
    open fun gen_handleViewAction_fn(record: InspectionRecord) {
        if (this.isMonthly) {
            uni_navigateTo(NavigateToOptions(url = "/pages/monthly/view?id=" + record.id))
        } else {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/review?id=" + record.id + "&isMonthly=false&reviewed=true"))
        }
    }
    open var goToDetail = ::gen_goToDetail_fn
    open fun gen_goToDetail_fn(record: InspectionRecord) {
        if (this.isMonthly && !this.isDelayed && !this.isReviewed && this.currentUserDeptType === 0 && record.taskPhase < 4 && record.taskPhase != 0 && (record.status === 0 || record.status === 1)) {
            uni_showToast(ShowToastOptions(title = "公示期尚未结束，请稍后再操作", icon = "none", duration = 2000))
            return
        }
        val n = record.natureName
        val p = record.professionName
        val d = record.departmentName
        val writeBackParams = "&natureName=" + UTSAndroid.consoleDebugError(encodeURIComponent(n), " at pages/record/record.uvue:675") + "&professionName=" + UTSAndroid.consoleDebugError(encodeURIComponent(p), " at pages/record/record.uvue:675") + "&departmentName=" + UTSAndroid.consoleDebugError(encodeURIComponent(d), " at pages/record/record.uvue:675")
        if (this.isMonthly && this.isDelayed) {
            uni_navigateTo(NavigateToOptions(url = "/pages/monthly/rectify?id=" + record.id))
            return
        }
        if (this.isDelayed) {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/detail?id=" + record.id + "&delayed=true" + writeBackParams))
        } else if (this.isReviewed) {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/review?id=" + record.id + "&reviewed=true&isMonthly=" + (if (this.isMonthly) {
                "true"
            } else {
                "false"
            }) + writeBackParams))
        } else if (this.isMonthly) {
            if (record.status === 0) {
                uni_navigateTo(NavigateToOptions(url = "/pages/monthly/rectify?id=" + record.id))
            } else if (record.status === 1) {
                uni_navigateTo(NavigateToOptions(url = "/pages/monthly/review?id=" + record.id))
            } else {
                uni_navigateTo(NavigateToOptions(url = "/pages/monthly/view?id=" + record.id))
            }
        } else if (record.status === 0) {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/detail?id=" + record.id + writeBackParams))
        } else if (record.status === 1) {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/review?id=" + record.id + "&isMonthly=false" + writeBackParams))
        } else {
            uni_navigateTo(NavigateToOptions(url = "/pages/record/review?id=" + record.id + "&isMonthly=false" + writeBackParams))
        }
    }
    open var refreshRecords = ::gen_refreshRecords_fn
    open fun gen_refreshRecords_fn() {
        this.pageNum = 1
        this.recordList = _uA()
        this.hasMore = true
        this.loadRecords()
        uni_stopPullDownRefresh()
    }
    open var onRefresh = ::gen_onRefresh_fn
    open fun gen_onRefresh_fn() {
        this.isRefreshing = true
        this.refreshRecords()
        setTimeout(fun(){
            if (this.isRefreshing) {
                this.isRefreshing = false
                uni_stopPullDownRefresh()
            }
        }
        , 1500)
    }
    open var onRefreshRestore = ::gen_onRefreshRestore_fn
    open fun gen_onRefreshRestore_fn() {
        this.isRefreshing = false
    }
    open var loadMore = ::gen_loadMore_fn
    open fun gen_loadMore_fn() {
        if (this.loadingMore || !this.hasMore) {
            return
        }
        this.pageNum++
        this.loadRecords()
    }
    open var handleScrollToLower = ::gen_handleScrollToLower_fn
    open fun gen_handleScrollToLower_fn() {
        if (this.hasMore && !this.loadingMore) {
            this.loadMore()
        }
    }
    open var showDeleteConfirm = ::gen_showDeleteConfirm_fn
    open fun gen_showDeleteConfirm_fn(recordId: String, problemDescription: String) {
        uni_showModal(ShowModalOptions(title = "确认删除", content = "确定要删除问题\"" + problemDescription + "\"吗？此操作不可恢复。", confirmText = "删除", cancelText = "取消", confirmColor = "#ff4757", success = fun(res){
            if (res.confirm) {
                this.deleteRecord(recordId)
            }
        }
        ))
    }
    open var deleteRecord = ::gen_deleteRecord_fn
    open fun gen_deleteRecord_fn(recordId: String) {
        val token = uni_getStorageSync("token")
        uni_showLoading(ShowLoadingOptions(title = "删除中..."))
        uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record/" + recordId), method = "DELETE", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            uni_hideLoading(null)
            val responseData = res.data as UTSJSONObject
            if (res.statusCode === 200 && responseData.getNumber("code") === 200) {
                this.recordList = this.recordList.filter(fun(record): Boolean {
                    return record.id !== recordId
                })
                this.totalCount = Math.max(0, this.totalCount - 1)
                uni_showToast(ShowToastOptions(title = "删除成功", icon = "success"))
            } else {
                val errorMessage = responseData.getString("message") ?: "删除失败"
                uni_showToast(ShowToastOptions(title = errorMessage, icon = "none"))
            }
        }
        , fail = fun(error){
            uni_hideLoading(null)
            console.error("删除记录失败:", error, " at pages/record/record.uvue:805")
            uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
        }
        ))
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("container" to _pS(_uM("backgroundColor" to "#f1f5f9", "width" to "100%")), "header" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "backgroundImage" to "linear-gradient(135deg, #1e40af 0%, #2563eb 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#dbeafe", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)")), "title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#ffffff", "marginBottom" to 5)), "subtitle" to _pS(_uM("fontSize" to 14, "color" to "#ffffff", "fontWeight" to 500, "opacity" to 0.92)), "record-item" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "marginTop" to 10, "marginRight" to 10, "marginBottom" to 1, "marginLeft" to 10, "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f1f5f9", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)")), "record-content" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "flex-start", "marginBottom" to 10, "width" to "100%")), "left-content" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "marginRight" to 15, "display" to "flex", "flexDirection" to "column")), "name-problem-row" to _uM(".left-content " to _uM("marginBottom" to 2), "" to _uM("display" to "flex", "alignItems" to "center", "marginBottom" to 2)), "problem-text" to _pS(_uM("fontSize" to 15, "color" to "#1e293b", "lineHeight" to 1.3, "fontWeight" to 500, "marginTop" to 2)), "operator-row" to _pS(_uM("display" to "flex", "flexDirection" to "row", "marginTop" to 6)), "operator-text" to _pS(_uM("fontSize" to 13, "color" to "#64748b", "fontWeight" to 500, "marginRight" to 16)), "right-info" to _pS(_uM("flexShrink" to 0, "textAlign" to "right", "display" to "flex", "flexDirection" to "column", "alignItems" to "flex-end", "justifyContent" to "flex-start", "minWidth" to 120, "maxWidth" to 150, "marginLeft" to "auto")), "inspector-info" to _uM(".right-info " to _uM("marginBottom" to 6), "" to _uM("fontSize" to 13, "color" to "#0f172a", "fontWeight" to 500, "textAlign" to "right")), "date-text" to _uM(".right-info " to _uM("marginBottom" to 6), "" to _uM("fontSize" to 13, "color" to "#475569", "fontWeight" to 500)), "status-tag" to _uM(".right-info " to _uM("marginBottom" to 0), "" to _uM("paddingTop" to 3, "paddingRight" to 10, "paddingBottom" to 3, "paddingLeft" to 10, "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "fontSize" to 12, "alignSelf" to "flex-end")), "status-row" to _pS(_uM("marginTop" to 8)), "status-text" to _pS(_uM("color" to "#ffffff", "fontSize" to 12, "fontWeight" to 500)), "status-pending" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#f59e0b")), "status-processing" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#2563eb")), "status-completed" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#16a34a")), "status-accepted" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#1e40af")), "status-unknown" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#94a3b8")), "responsible-name" to _pS(_uM("fontSize" to 16, "color" to "#0f172a", "fontWeight" to 700, "marginTop" to 0, "marginRight" to 0, "marginBottom" to 0, "marginLeft" to 0)), "empty-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 60, "paddingRight" to 20, "paddingBottom" to 60, "paddingLeft" to 20)), "empty-icon" to _pS(_uM("fontSize" to 48, "marginBottom" to 15, "color" to "#94a3b8")), "empty-text" to _pS(_uM("fontSize" to 16, "color" to "#94a3b8")), "loading-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20)), "loading-text" to _pS(_uM("fontSize" to 14, "color" to "#94a3b8")), "load-more" to _pS(_uM("textAlign" to "center", "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopWidth" to 1, "borderTopStyle" to "solid", "borderTopColor" to "#f1f5f9")), "load-more-text" to _pS(_uM("fontSize" to 14, "color" to "#1e293b", "fontWeight" to 500)))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
