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
import io.dcloud.uniapp.extapi.connectSocket as uni_connectSocket
import io.dcloud.uniapp.extapi.exit as uni_exit
import io.dcloud.uniapp.extapi.showToast as uni_showToast
val runBlock1 = run {
    __uniConfig.getAppStyles = fun(): Map<String, Map<String, Map<String, Any>>> {
        return GenApp.styles
    }
}
fun initRuntimeSocket(hosts: String, port: String, id: String): UTSPromise<SocketTask?> {
    if (hosts == "" || port == "" || id == "") {
        return UTSPromise.resolve(null)
    }
    return hosts.split(",").reduce<UTSPromise<SocketTask?>>(fun(promise: UTSPromise<SocketTask?>, host: String): UTSPromise<SocketTask?> {
        return promise.then(fun(socket): UTSPromise<SocketTask?> {
            if (socket != null) {
                return UTSPromise.resolve(socket)
            }
            return tryConnectSocket(host, port, id)
        }
        )
    }
    , UTSPromise.resolve(null))
}
val SOCKET_TIMEOUT: Number = 500
fun tryConnectSocket(host: String, port: String, id: String): UTSPromise<SocketTask?> {
    return UTSPromise(fun(resolve, reject){
        val socket = uni_connectSocket(ConnectSocketOptions(url = "ws://" + host + ":" + port + "/" + id, fail = fun(_) {
            resolve(null)
        }
        ))
        val timer = setTimeout(fun(){
            socket.close(CloseSocketOptions(code = 1006, reason = "connect timeout"))
            resolve(null)
        }
        , SOCKET_TIMEOUT)
        socket.onOpen(fun(e){
            clearTimeout(timer)
            resolve(socket)
        }
        )
        socket.onClose(fun(e){
            clearTimeout(timer)
            resolve(null)
        }
        )
        socket.onError(fun(e){
            clearTimeout(timer)
            resolve(null)
        }
        )
    }
    )
}
fun initRuntimeSocketService(): UTSPromise<Boolean> {
    val hosts: String = "192.168.93.1,192.168.229.1,192.168.42.128,127.0.0.1"
    val port: String = "8090"
    val id: String = "app-android_C6LFWr"
    if (hosts == "" || port == "" || id == "") {
        return UTSPromise.resolve(false)
    }
    var socketTask: SocketTask? = null
    __registerWebViewUniConsole(fun(): String {
        return "!function(){\"use strict\";\"function\"==typeof SuppressedError&&SuppressedError;var e=[\"log\",\"warn\",\"error\",\"info\",\"debug\"],n=e.reduce((function(e,n){return e[n]=console[n].bind(console),e}),{}),t=null,r=new Set,o={};function i(e){if(null!=t){var n=e.map((function(e){if(\"string\"==typeof e)return e;var n=e&&\"promise\"in e&&\"reason\"in e,t=n?\"UnhandledPromiseRejection: \":\"\";if(n&&(e=e.reason),e instanceof Error&&e.stack)return e.message&&!e.stack.includes(e.message)?\"\".concat(t).concat(e.message,\"\\n\").concat(e.stack):\"\".concat(t).concat(e.stack);if(\"object\"==typeof e&&null!==e)try{return t+JSON.stringify(e)}catch(e){return t+String(e)}return t+String(e)})).filter(Boolean);n.length>0&&t(JSON.stringify(Object.assign({type:\"error\",data:n},o)))}else e.forEach((function(e){r.add(e)}))}function a(e,n){try{return{type:e,args:u(n)}}catch(e){}return{type:e,args:[]}}function u(e){return e.map((function(e){return c(e)}))}function c(e,n){if(void 0===n&&(n=0),n>=7)return{type:\"object\",value:\"[Maximum depth reached]\"};switch(typeof e){case\"string\":return{type:\"string\",value:e};case\"number\":return function(e){return{type:\"number\",value:String(e)}}(e);case\"boolean\":return function(e){return{type:\"boolean\",value:String(e)}}(e);case\"object\":try{return function(e,n){if(null===e)return{type:\"null\"};if(function(e){return e.\$&&s(e.\$)}(e))return function(e,n){return{type:\"object\",className:\"ComponentPublicInstance\",value:{properties:Object.entries(e.\$.type).map((function(e){return f(e[0],e[1],n+1)}))}}}(e,n);if(s(e))return function(e,n){return{type:\"object\",className:\"ComponentInternalInstance\",value:{properties:Object.entries(e.type).map((function(e){return f(e[0],e[1],n+1)}))}}}(e,n);if(function(e){return e.style&&null!=e.tagName&&null!=e.nodeName}(e))return function(e,n){return{type:\"object\",value:{properties:Object.entries(e).filter((function(e){var n=e[0];return[\"id\",\"tagName\",\"nodeName\",\"dataset\",\"offsetTop\",\"offsetLeft\",\"style\"].includes(n)})).map((function(e){return f(e[0],e[1],n+1)}))}}}(e,n);if(function(e){return\"function\"==typeof e.getPropertyValue&&\"function\"==typeof e.setProperty&&e.\$styles}(e))return function(e,n){return{type:\"object\",value:{properties:Object.entries(e.\$styles).map((function(e){return f(e[0],e[1],n+1)}))}}}(e,n);if(Array.isArray(e))return{type:\"object\",subType:\"array\",value:{properties:e.map((function(e,t){return function(e,n,t){var r=c(e,t);return r.name=\"\".concat(n),r}(e,t,n+1)}))}};if(e instanceof Set)return{type:\"object\",subType:\"set\",className:\"Set\",description:\"Set(\".concat(e.size,\")\"),value:{entries:Array.from(e).map((function(e){return function(e,n){return{value:c(e,n)}}(e,n+1)}))}};if(e instanceof Map)return{type:\"object\",subType:\"map\",className:\"Map\",description:\"Map(\".concat(e.size,\")\"),value:{entries:Array.from(e.entries()).map((function(e){return function(e,n){return{key:c(e[0],n),value:c(e[1],n)}}(e,n+1)}))}};if(e instanceof Promise)return{type:\"object\",subType:\"promise\",value:{properties:[]}};if(e instanceof RegExp)return{type:\"object\",subType:\"regexp\",value:String(e),className:\"Regexp\"};if(e instanceof Date)return{type:\"object\",subType:\"date\",value:String(e),className:\"Date\"};if(e instanceof Error)return{type:\"object\",subType:\"error\",value:e.message||String(e),className:e.name||\"Error\"};var t=void 0,r=e.constructor;r&&r.get\$UTSMetadata\$&&(t=r.get\$UTSMetadata\$().name);var o=Object.entries(e);(function(e){return e.modifier&&e.modifier._attribute&&e.nodeContent})(e)&&(o=o.filter((function(e){var n=e[0];return\"modifier\"!==n&&\"nodeContent\"!==n})));return{type:\"object\",className:t,value:{properties:o.map((function(e){return f(e[0],e[1],n+1)}))}}}(e,n)}catch(e){return{type:\"object\",value:{properties:[]}}}case\"undefined\":return{type:\"undefined\"};case\"function\":return function(e){return{type:\"function\",value:\"function \".concat(e.name,\"() {}\")}}(e);case\"symbol\":return function(e){return{type:\"symbol\",value:e.description}}(e);case\"bigint\":return function(e){return{type:\"bigint\",value:String(e)}}(e)}}function s(e){return e.type&&null!=e.uid&&e.appContext}function f(e,n,t){var r=c(n,t);return r.name=e,r}var l=null,p=[],y={},g=\"---BEGIN:EXCEPTION---\",d=\"---END:EXCEPTION---\";function v(e){null!=l?l(JSON.stringify(Object.assign({type:\"console\",data:e},y))):p.push.apply(p,e)}var m=/^\\s*at\\s+[\\w/./-]+:\\d+\$/;function b(){function t(e){return function(){for(var t=[],r=0;r<arguments.length;r++)t[r]=arguments[r];var o=function(e,n,t){if(t||2===arguments.length)for(var r,o=0,i=n.length;o<i;o++)!r&&o in n||(r||(r=Array.prototype.slice.call(n,0,o)),r[o]=n[o]);return e.concat(r||Array.prototype.slice.call(n))}([],t,!0);if(o.length){var u=o[o.length-1];\"string\"==typeof u&&m.test(u)&&o.pop()}if(n[e].apply(n,o),\"error\"===e&&1===t.length){var c=t[0];if(\"string\"==typeof c&&c.startsWith(g)){var s=g.length,f=c.length-d.length;return void i([c.slice(s,f)])}if(c instanceof Error)return void i([c])}v([a(e,t)])}}return function(){var e=console.log,n=Symbol();try{console.log=n}catch(e){return!1}var t=console.log===n;return console.log=e,t}()?(e.forEach((function(e){console[e]=t(e)})),function(){e.forEach((function(e){console[e]=n[e]}))}):function(){}}function _(e){var n={type:\"WEB_INVOKE_APPSERVICE\",args:{data:{name:\"console\",arg:e}}};return window.__uniapp_x_postMessageToService?window.__uniapp_x_postMessageToService(n):window.__uniapp_x_.postMessageToService(JSON.stringify(n))}!function(){if(!window.__UNI_CONSOLE_WEBVIEW__){window.__UNI_CONSOLE_WEBVIEW__=!0;var e=\"[web-view]\".concat(window.__UNI_PAGE_ROUTE__?\"[\".concat(window.__UNI_PAGE_ROUTE__,\"]\"):\"\");b(),function(e,n){if(void 0===n&&(n={}),l=e,Object.assign(y,n),null!=e&&p.length>0){var t=p.slice();p.length=0,v(t)}}((function(e){_(e)}),{channel:e}),function(e,n){if(void 0===n&&(n={}),t=e,Object.assign(o,n),null!=e&&r.size>0){var a=Array.from(r);r.clear(),i(a)}}((function(e){_(e)}),{channel:e}),window.addEventListener(\"error\",(function(e){i([e.error])})),window.addEventListener(\"unhandledrejection\",(function(e){i([e])}))}}()}();"
    }
    , fun(data: String){
        socketTask?.send(SendSocketMessageOptions(data = data))
    }
    )
    return UTSPromise.resolve().then(fun(): UTSPromise<Boolean> {
        return initRuntimeSocket(hosts, port, id).then(fun(socket): Boolean {
            if (socket == null) {
                return false
            }
            socketTask = socket
            return true
        }
        )
    }
    ).`catch`(fun(): Boolean {
        return false
    }
    )
}
val runBlock2 = run {
    initRuntimeSocketService()
}
var firstBackTime: Number = 0
open class GenApp : BaseApp {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onLaunch(fun(_: OnLaunchOptions) {
            console.log("App Launch", " at App.uvue:7")
        }
        , __ins)
        onAppShow(fun(_: OnShowOptions) {
            console.log("App Show", " at App.uvue:10")
        }
        , __ins)
        onAppHide(fun() {
            console.log("App Hide", " at App.uvue:13")
        }
        , __ins)
        onLastPageBackPress(fun() {
            console.log("App LastPageBackPress", " at App.uvue:17")
            if (firstBackTime == 0) {
                uni_showToast(ShowToastOptions(title = "再按一次退出应用", position = "bottom"))
                firstBackTime = Date.now()
                setTimeout(fun(){
                    firstBackTime = 0
                }, 2000)
            } else if (Date.now() - firstBackTime < 2000) {
                firstBackTime = Date.now()
                uni_exit(null)
            }
        }
        , __ins)
        onExit(fun() {
            console.log("App Exit", " at App.uvue:34")
        }
        , __ins)
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("uni-row" to _pS(_uM("flexDirection" to "row")), "uni-column" to _pS(_uM("flexDirection" to "column")))
            }
    }
}
val GenAppClass = CreateVueAppComponent(GenApp::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "app", name = "", inheritAttrs = true, inject = Map(), props = Map(), propsNeedCastKeys = _uA(), emits = Map(), components = Map(), styles = GenApp.styles)
}
, fun(instance): GenApp {
    return GenApp(instance)
}
)
open class ApiConfig (
    @JsonNotNull
    open var baseUrl: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ApiConfig", "config/api.uts", 2, 13)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return ApiConfigReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class ApiConfigReactiveObject : ApiConfig, IUTSReactive<ApiConfig> {
    override var __v_raw: ApiConfig
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: ApiConfig, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(baseUrl = __v_raw.baseUrl) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): ApiConfigReactiveObject {
        return ApiConfigReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var baseUrl: String
        get() {
            return _tRG(__v_raw, "baseUrl", __v_raw.baseUrl, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("baseUrl")) {
                return
            }
            val oldValue = __v_raw.baseUrl
            __v_raw.baseUrl = value
            _tRS(__v_raw, "baseUrl", oldValue, value)
        }
}
val apiConfig = reactive(ApiConfig(baseUrl = "http://192.168.42.128:8080"))
val getApiUrl = fun(endpoint: String): String {
    return "" + apiConfig.baseUrl + "/api" + endpoint
}
open class StatsData (
    @JsonNotNull
    open var total: Number,
    @JsonNotNull
    open var pending: Number,
    @JsonNotNull
    open var completed: Number,
    @JsonNotNull
    open var reviewed: Number,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("StatsData", "pages/home/home.uvue", 142, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return StatsDataReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class StatsDataReactiveObject : StatsData, IUTSReactive<StatsData> {
    override var __v_raw: StatsData
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: StatsData, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(total = __v_raw.total, pending = __v_raw.pending, completed = __v_raw.completed, reviewed = __v_raw.reviewed) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): StatsDataReactiveObject {
        return StatsDataReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var total: Number
        get() {
            return _tRG(__v_raw, "total", __v_raw.total, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("total")) {
                return
            }
            val oldValue = __v_raw.total
            __v_raw.total = value
            _tRS(__v_raw, "total", oldValue, value)
        }
    override var pending: Number
        get() {
            return _tRG(__v_raw, "pending", __v_raw.pending, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("pending")) {
                return
            }
            val oldValue = __v_raw.pending
            __v_raw.pending = value
            _tRS(__v_raw, "pending", oldValue, value)
        }
    override var completed: Number
        get() {
            return _tRG(__v_raw, "completed", __v_raw.completed, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("completed")) {
                return
            }
            val oldValue = __v_raw.completed
            __v_raw.completed = value
            _tRS(__v_raw, "completed", oldValue, value)
        }
    override var reviewed: Number
        get() {
            return _tRG(__v_raw, "reviewed", __v_raw.reviewed, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("reviewed")) {
                return
            }
            val oldValue = __v_raw.reviewed
            __v_raw.reviewed = value
            _tRS(__v_raw, "reviewed", oldValue, value)
        }
}
open class TrendData (
    @JsonNotNull
    open var months: UTSArray<String>,
    @JsonNotNull
    open var values: UTSArray<Number>,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("TrendData", "pages/home/home.uvue", 195, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return TrendDataReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class TrendDataReactiveObject : TrendData, IUTSReactive<TrendData> {
    override var __v_raw: TrendData
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: TrendData, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(months = __v_raw.months, values = __v_raw.values) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): TrendDataReactiveObject {
        return TrendDataReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var months: UTSArray<String>
        get() {
            return _tRG(__v_raw, "months", __v_raw.months, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("months")) {
                return
            }
            val oldValue = __v_raw.months
            __v_raw.months = value
            _tRS(__v_raw, "months", oldValue, value)
        }
    override var values: UTSArray<Number>
        get() {
            return _tRG(__v_raw, "values", __v_raw.values, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("values")) {
                return
            }
            val oldValue = __v_raw.values
            __v_raw.values = value
            _tRS(__v_raw, "values", oldValue, value)
        }
}
val GenPagesHomeHomeClass = CreateVueComponent(GenPagesHomeHome::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesHomeHome.inheritAttrs, inject = GenPagesHomeHome.inject, props = GenPagesHomeHome.props, propsNeedCastKeys = GenPagesHomeHome.propsNeedCastKeys, emits = GenPagesHomeHome.emits, components = GenPagesHomeHome.components, styles = GenPagesHomeHome.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesHomeHome.setup(props as GenPagesHomeHome)
    }
    )
}
, fun(instance, renderer): GenPagesHomeHome {
    return GenPagesHomeHome(instance, renderer)
}
)
open class RecentRecord (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var problemDescription: String,
    @JsonNotNull
    open var locationName: String,
    @JsonNotNull
    open var createTime: String,
    @JsonNotNull
    open var status: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("RecentRecord", "pages/weekly/weekly.uvue", 64, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return RecentRecordReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class RecentRecordReactiveObject : RecentRecord, IUTSReactive<RecentRecord> {
    override var __v_raw: RecentRecord
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: RecentRecord, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, problemDescription = __v_raw.problemDescription, locationName = __v_raw.locationName, createTime = __v_raw.createTime, status = __v_raw.status) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): RecentRecordReactiveObject {
        return RecentRecordReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var problemDescription: String
        get() {
            return _tRG(__v_raw, "problemDescription", __v_raw.problemDescription, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("problemDescription")) {
                return
            }
            val oldValue = __v_raw.problemDescription
            __v_raw.problemDescription = value
            _tRS(__v_raw, "problemDescription", oldValue, value)
        }
    override var locationName: String
        get() {
            return _tRG(__v_raw, "locationName", __v_raw.locationName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("locationName")) {
                return
            }
            val oldValue = __v_raw.locationName
            __v_raw.locationName = value
            _tRS(__v_raw, "locationName", oldValue, value)
        }
    override var createTime: String
        get() {
            return _tRG(__v_raw, "createTime", __v_raw.createTime, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("createTime")) {
                return
            }
            val oldValue = __v_raw.createTime
            __v_raw.createTime = value
            _tRS(__v_raw, "createTime", oldValue, value)
        }
    override var status: String
        get() {
            return _tRG(__v_raw, "status", __v_raw.status, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("status")) {
                return
            }
            val oldValue = __v_raw.status
            __v_raw.status = value
            _tRS(__v_raw, "status", oldValue, value)
        }
}
open class StatisticsData (
    @JsonNotNull
    open var pendingIssues: Number,
    @JsonNotNull
    open var completedIssues: Number,
    @JsonNotNull
    open var weeklyTotal: Number,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("StatisticsData", "pages/weekly/weekly.uvue", 72, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return StatisticsDataReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class StatisticsDataReactiveObject : StatisticsData, IUTSReactive<StatisticsData> {
    override var __v_raw: StatisticsData
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: StatisticsData, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(pendingIssues = __v_raw.pendingIssues, completedIssues = __v_raw.completedIssues, weeklyTotal = __v_raw.weeklyTotal) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): StatisticsDataReactiveObject {
        return StatisticsDataReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var pendingIssues: Number
        get() {
            return _tRG(__v_raw, "pendingIssues", __v_raw.pendingIssues, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("pendingIssues")) {
                return
            }
            val oldValue = __v_raw.pendingIssues
            __v_raw.pendingIssues = value
            _tRS(__v_raw, "pendingIssues", oldValue, value)
        }
    override var completedIssues: Number
        get() {
            return _tRG(__v_raw, "completedIssues", __v_raw.completedIssues, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("completedIssues")) {
                return
            }
            val oldValue = __v_raw.completedIssues
            __v_raw.completedIssues = value
            _tRS(__v_raw, "completedIssues", oldValue, value)
        }
    override var weeklyTotal: Number
        get() {
            return _tRG(__v_raw, "weeklyTotal", __v_raw.weeklyTotal, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("weeklyTotal")) {
                return
            }
            val oldValue = __v_raw.weeklyTotal
            __v_raw.weeklyTotal = value
            _tRS(__v_raw, "weeklyTotal", oldValue, value)
        }
}
val GenPagesWeeklyWeeklyClass = CreateVueComponent(GenPagesWeeklyWeekly::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesWeeklyWeekly.inheritAttrs, inject = GenPagesWeeklyWeekly.inject, props = GenPagesWeeklyWeekly.props, propsNeedCastKeys = GenPagesWeeklyWeekly.propsNeedCastKeys, emits = GenPagesWeeklyWeekly.emits, components = GenPagesWeeklyWeekly.components, styles = GenPagesWeeklyWeekly.styles)
}
, fun(instance, renderer): GenPagesWeeklyWeekly {
    return GenPagesWeeklyWeekly(instance, renderer)
}
)
open class MonthlyRecord (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var problemDescription: String,
    @JsonNotNull
    open var locationName: String,
    @JsonNotNull
    open var createTime: String,
    @JsonNotNull
    open var status: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("MonthlyRecord", "pages/monthly/monthly.uvue", 91, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return MonthlyRecordReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class MonthlyRecordReactiveObject : MonthlyRecord, IUTSReactive<MonthlyRecord> {
    override var __v_raw: MonthlyRecord
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: MonthlyRecord, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, problemDescription = __v_raw.problemDescription, locationName = __v_raw.locationName, createTime = __v_raw.createTime, status = __v_raw.status) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): MonthlyRecordReactiveObject {
        return MonthlyRecordReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var problemDescription: String
        get() {
            return _tRG(__v_raw, "problemDescription", __v_raw.problemDescription, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("problemDescription")) {
                return
            }
            val oldValue = __v_raw.problemDescription
            __v_raw.problemDescription = value
            _tRS(__v_raw, "problemDescription", oldValue, value)
        }
    override var locationName: String
        get() {
            return _tRG(__v_raw, "locationName", __v_raw.locationName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("locationName")) {
                return
            }
            val oldValue = __v_raw.locationName
            __v_raw.locationName = value
            _tRS(__v_raw, "locationName", oldValue, value)
        }
    override var createTime: String
        get() {
            return _tRG(__v_raw, "createTime", __v_raw.createTime, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("createTime")) {
                return
            }
            val oldValue = __v_raw.createTime
            __v_raw.createTime = value
            _tRS(__v_raw, "createTime", oldValue, value)
        }
    override var status: String
        get() {
            return _tRG(__v_raw, "status", __v_raw.status, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("status")) {
                return
            }
            val oldValue = __v_raw.status
            __v_raw.status = value
            _tRS(__v_raw, "status", oldValue, value)
        }
}
open class MonthlyStatistics (
    @JsonNotNull
    open var monthlyPending: Number,
    @JsonNotNull
    open var monthlyCompleted: Number,
    @JsonNotNull
    open var monthlyTotal: Number,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("MonthlyStatistics", "pages/monthly/monthly.uvue", 99, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return MonthlyStatisticsReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class MonthlyStatisticsReactiveObject : MonthlyStatistics, IUTSReactive<MonthlyStatistics> {
    override var __v_raw: MonthlyStatistics
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: MonthlyStatistics, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(monthlyPending = __v_raw.monthlyPending, monthlyCompleted = __v_raw.monthlyCompleted, monthlyTotal = __v_raw.monthlyTotal) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): MonthlyStatisticsReactiveObject {
        return MonthlyStatisticsReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var monthlyPending: Number
        get() {
            return _tRG(__v_raw, "monthlyPending", __v_raw.monthlyPending, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("monthlyPending")) {
                return
            }
            val oldValue = __v_raw.monthlyPending
            __v_raw.monthlyPending = value
            _tRS(__v_raw, "monthlyPending", oldValue, value)
        }
    override var monthlyCompleted: Number
        get() {
            return _tRG(__v_raw, "monthlyCompleted", __v_raw.monthlyCompleted, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("monthlyCompleted")) {
                return
            }
            val oldValue = __v_raw.monthlyCompleted
            __v_raw.monthlyCompleted = value
            _tRS(__v_raw, "monthlyCompleted", oldValue, value)
        }
    override var monthlyTotal: Number
        get() {
            return _tRG(__v_raw, "monthlyTotal", __v_raw.monthlyTotal, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("monthlyTotal")) {
                return
            }
            val oldValue = __v_raw.monthlyTotal
            __v_raw.monthlyTotal = value
            _tRS(__v_raw, "monthlyTotal", oldValue, value)
        }
}
val GenPagesMonthlyMonthlyClass = CreateVueComponent(GenPagesMonthlyMonthly::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesMonthlyMonthly.inheritAttrs, inject = GenPagesMonthlyMonthly.inject, props = GenPagesMonthlyMonthly.props, propsNeedCastKeys = GenPagesMonthlyMonthly.propsNeedCastKeys, emits = GenPagesMonthlyMonthly.emits, components = GenPagesMonthlyMonthly.components, styles = GenPagesMonthlyMonthly.styles)
}
, fun(instance, renderer): GenPagesMonthlyMonthly {
    return GenPagesMonthlyMonthly(instance, renderer)
}
)
open class EditForm (
    @JsonNotNull
    open var realName: String,
    @JsonNotNull
    open var phone: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("EditForm", "pages/profile/profile.uvue", 73, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return EditFormReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class EditFormReactiveObject : EditForm, IUTSReactive<EditForm> {
    override var __v_raw: EditForm
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: EditForm, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(realName = __v_raw.realName, phone = __v_raw.phone) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): EditFormReactiveObject {
        return EditFormReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var realName: String
        get() {
            return _tRG(__v_raw, "realName", __v_raw.realName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("realName")) {
                return
            }
            val oldValue = __v_raw.realName
            __v_raw.realName = value
            _tRS(__v_raw, "realName", oldValue, value)
        }
    override var phone: String
        get() {
            return _tRG(__v_raw, "phone", __v_raw.phone, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("phone")) {
                return
            }
            val oldValue = __v_raw.phone
            __v_raw.phone = value
            _tRS(__v_raw, "phone", oldValue, value)
        }
}
val GenPagesProfileProfileClass = CreateVueComponent(GenPagesProfileProfile::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesProfileProfile.inheritAttrs, inject = GenPagesProfileProfile.inject, props = GenPagesProfileProfile.props, propsNeedCastKeys = GenPagesProfileProfile.propsNeedCastKeys, emits = GenPagesProfileProfile.emits, components = GenPagesProfileProfile.components, styles = GenPagesProfileProfile.styles)
}
, fun(instance, renderer): GenPagesProfileProfile {
    return GenPagesProfileProfile(instance, renderer)
}
)
open class RecentRecord1 (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var problemDescription: String,
    @JsonNotNull
    open var locationName: String,
    @JsonNotNull
    open var createTime: String,
    @JsonNotNull
    open var status: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("RecentRecord", "pages/index/index.uvue", 84, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return RecentRecord1ReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class RecentRecord1ReactiveObject : RecentRecord1, IUTSReactive<RecentRecord1> {
    override var __v_raw: RecentRecord1
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: RecentRecord1, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, problemDescription = __v_raw.problemDescription, locationName = __v_raw.locationName, createTime = __v_raw.createTime, status = __v_raw.status) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): RecentRecord1ReactiveObject {
        return RecentRecord1ReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var problemDescription: String
        get() {
            return _tRG(__v_raw, "problemDescription", __v_raw.problemDescription, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("problemDescription")) {
                return
            }
            val oldValue = __v_raw.problemDescription
            __v_raw.problemDescription = value
            _tRS(__v_raw, "problemDescription", oldValue, value)
        }
    override var locationName: String
        get() {
            return _tRG(__v_raw, "locationName", __v_raw.locationName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("locationName")) {
                return
            }
            val oldValue = __v_raw.locationName
            __v_raw.locationName = value
            _tRS(__v_raw, "locationName", oldValue, value)
        }
    override var createTime: String
        get() {
            return _tRG(__v_raw, "createTime", __v_raw.createTime, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("createTime")) {
                return
            }
            val oldValue = __v_raw.createTime
            __v_raw.createTime = value
            _tRS(__v_raw, "createTime", oldValue, value)
        }
    override var status: String
        get() {
            return _tRG(__v_raw, "status", __v_raw.status, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("status")) {
                return
            }
            val oldValue = __v_raw.status
            __v_raw.status = value
            _tRS(__v_raw, "status", oldValue, value)
        }
}
open class StatisticsData1 (
    @JsonNotNull
    open var pendingIssues: Number,
    @JsonNotNull
    open var completedIssues: Number,
    @JsonNotNull
    open var monthRecords: Number,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("StatisticsData", "pages/index/index.uvue", 92, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return StatisticsData1ReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class StatisticsData1ReactiveObject : StatisticsData1, IUTSReactive<StatisticsData1> {
    override var __v_raw: StatisticsData1
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: StatisticsData1, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(pendingIssues = __v_raw.pendingIssues, completedIssues = __v_raw.completedIssues, monthRecords = __v_raw.monthRecords) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): StatisticsData1ReactiveObject {
        return StatisticsData1ReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var pendingIssues: Number
        get() {
            return _tRG(__v_raw, "pendingIssues", __v_raw.pendingIssues, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("pendingIssues")) {
                return
            }
            val oldValue = __v_raw.pendingIssues
            __v_raw.pendingIssues = value
            _tRS(__v_raw, "pendingIssues", oldValue, value)
        }
    override var completedIssues: Number
        get() {
            return _tRG(__v_raw, "completedIssues", __v_raw.completedIssues, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("completedIssues")) {
                return
            }
            val oldValue = __v_raw.completedIssues
            __v_raw.completedIssues = value
            _tRS(__v_raw, "completedIssues", oldValue, value)
        }
    override var monthRecords: Number
        get() {
            return _tRG(__v_raw, "monthRecords", __v_raw.monthRecords, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("monthRecords")) {
                return
            }
            val oldValue = __v_raw.monthRecords
            __v_raw.monthRecords = value
            _tRS(__v_raw, "monthRecords", oldValue, value)
        }
}
val GenPagesIndexIndexClass = CreateVueComponent(GenPagesIndexIndex::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesIndexIndex.inheritAttrs, inject = GenPagesIndexIndex.inject, props = GenPagesIndexIndex.props, propsNeedCastKeys = GenPagesIndexIndex.propsNeedCastKeys, emits = GenPagesIndexIndex.emits, components = GenPagesIndexIndex.components, styles = GenPagesIndexIndex.styles)
}
, fun(instance, renderer): GenPagesIndexIndex {
    return GenPagesIndexIndex(instance, renderer)
}
)
open class LoginFormData (
    @JsonNotNull
    open var username: String,
    @JsonNotNull
    open var password: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("LoginFormData", "pages/login/login.uvue", 51, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return LoginFormDataReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class LoginFormDataReactiveObject : LoginFormData, IUTSReactive<LoginFormData> {
    override var __v_raw: LoginFormData
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: LoginFormData, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(username = __v_raw.username, password = __v_raw.password) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): LoginFormDataReactiveObject {
        return LoginFormDataReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var username: String
        get() {
            return _tRG(__v_raw, "username", __v_raw.username, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("username")) {
                return
            }
            val oldValue = __v_raw.username
            __v_raw.username = value
            _tRS(__v_raw, "username", oldValue, value)
        }
    override var password: String
        get() {
            return _tRG(__v_raw, "password", __v_raw.password, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("password")) {
                return
            }
            val oldValue = __v_raw.password
            __v_raw.password = value
            _tRS(__v_raw, "password", oldValue, value)
        }
}
val GenPagesLoginLoginClass = CreateVueComponent(GenPagesLoginLogin::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesLoginLogin.inheritAttrs, inject = GenPagesLoginLogin.inject, props = GenPagesLoginLogin.props, propsNeedCastKeys = GenPagesLoginLogin.propsNeedCastKeys, emits = GenPagesLoginLogin.emits, components = GenPagesLoginLogin.components, styles = GenPagesLoginLogin.styles)
}
, fun(instance, renderer): GenPagesLoginLogin {
    return GenPagesLoginLogin(instance, renderer)
}
)
open class InspectionRecord (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var problemDescription: String,
    @JsonNotNull
    open var responsiblePerson: String,
    @JsonNotNull
    open var inspectorName: String,
    @JsonNotNull
    open var createDate: String,
    @JsonNotNull
    open var status: Number,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("InspectionRecord", "pages/record/record.uvue", 62, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return InspectionRecordReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class InspectionRecordReactiveObject : InspectionRecord, IUTSReactive<InspectionRecord> {
    override var __v_raw: InspectionRecord
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: InspectionRecord, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, problemDescription = __v_raw.problemDescription, responsiblePerson = __v_raw.responsiblePerson, inspectorName = __v_raw.inspectorName, createDate = __v_raw.createDate, status = __v_raw.status) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): InspectionRecordReactiveObject {
        return InspectionRecordReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var problemDescription: String
        get() {
            return _tRG(__v_raw, "problemDescription", __v_raw.problemDescription, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("problemDescription")) {
                return
            }
            val oldValue = __v_raw.problemDescription
            __v_raw.problemDescription = value
            _tRS(__v_raw, "problemDescription", oldValue, value)
        }
    override var responsiblePerson: String
        get() {
            return _tRG(__v_raw, "responsiblePerson", __v_raw.responsiblePerson, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("responsiblePerson")) {
                return
            }
            val oldValue = __v_raw.responsiblePerson
            __v_raw.responsiblePerson = value
            _tRS(__v_raw, "responsiblePerson", oldValue, value)
        }
    override var inspectorName: String
        get() {
            return _tRG(__v_raw, "inspectorName", __v_raw.inspectorName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("inspectorName")) {
                return
            }
            val oldValue = __v_raw.inspectorName
            __v_raw.inspectorName = value
            _tRS(__v_raw, "inspectorName", oldValue, value)
        }
    override var createDate: String
        get() {
            return _tRG(__v_raw, "createDate", __v_raw.createDate, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("createDate")) {
                return
            }
            val oldValue = __v_raw.createDate
            __v_raw.createDate = value
            _tRS(__v_raw, "createDate", oldValue, value)
        }
    override var status: Number
        get() {
            return _tRG(__v_raw, "status", __v_raw.status, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("status")) {
                return
            }
            val oldValue = __v_raw.status
            __v_raw.status = value
            _tRS(__v_raw, "status", oldValue, value)
        }
}
val GenPagesRecordRecordClass = CreateVueComponent(GenPagesRecordRecord::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesRecordRecord.inheritAttrs, inject = GenPagesRecordRecord.inject, props = GenPagesRecordRecord.props, propsNeedCastKeys = GenPagesRecordRecord.propsNeedCastKeys, emits = GenPagesRecordRecord.emits, components = GenPagesRecordRecord.components, styles = GenPagesRecordRecord.styles)
}
, fun(instance, renderer): GenPagesRecordRecord {
    return GenPagesRecordRecord(instance, renderer)
}
)
open class NatureOption (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var name: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("NatureOption", "pages/record/add.uvue", 311, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return NatureOptionReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class NatureOptionReactiveObject : NatureOption, IUTSReactive<NatureOption> {
    override var __v_raw: NatureOption
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: NatureOption, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, name = __v_raw.name) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): NatureOptionReactiveObject {
        return NatureOptionReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var name: String
        get() {
            return _tRG(__v_raw, "name", __v_raw.name, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("name")) {
                return
            }
            val oldValue = __v_raw.name
            __v_raw.name = value
            _tRS(__v_raw, "name", oldValue, value)
        }
}
open class AssessmentOption (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var label: String,
    @JsonNotNull
    open var value: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("AssessmentOption", "pages/record/add.uvue", 316, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return AssessmentOptionReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class AssessmentOptionReactiveObject : AssessmentOption, IUTSReactive<AssessmentOption> {
    override var __v_raw: AssessmentOption
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: AssessmentOption, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, label = __v_raw.label, value = __v_raw.value) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): AssessmentOptionReactiveObject {
        return AssessmentOptionReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var label: String
        get() {
            return _tRG(__v_raw, "label", __v_raw.label, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("label")) {
                return
            }
            val oldValue = __v_raw.label
            __v_raw.label = value
            _tRS(__v_raw, "label", oldValue, value)
        }
    override var value: String
        get() {
            return _tRG(__v_raw, "value", __v_raw.value, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("value")) {
                return
            }
            val oldValue = __v_raw.value
            __v_raw.value = value
            _tRS(__v_raw, "value", oldValue, value)
        }
}
open class ProfessionOption (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var name: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ProfessionOption", "pages/record/add.uvue", 322, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return ProfessionOptionReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class ProfessionOptionReactiveObject : ProfessionOption, IUTSReactive<ProfessionOption> {
    override var __v_raw: ProfessionOption
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: ProfessionOption, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, name = __v_raw.name) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): ProfessionOptionReactiveObject {
        return ProfessionOptionReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var name: String
        get() {
            return _tRG(__v_raw, "name", __v_raw.name, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("name")) {
                return
            }
            val oldValue = __v_raw.name
            __v_raw.name = value
            _tRS(__v_raw, "name", oldValue, value)
        }
}
open class DepartmentOption (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var name: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("DepartmentOption", "pages/record/add.uvue", 327, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return DepartmentOptionReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class DepartmentOptionReactiveObject : DepartmentOption, IUTSReactive<DepartmentOption> {
    override var __v_raw: DepartmentOption
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: DepartmentOption, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, name = __v_raw.name) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): DepartmentOptionReactiveObject {
        return DepartmentOptionReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var name: String
        get() {
            return _tRG(__v_raw, "name", __v_raw.name, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("name")) {
                return
            }
            val oldValue = __v_raw.name
            __v_raw.name = value
            _tRS(__v_raw, "name", oldValue, value)
        }
}
open class InspectorOption (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var name: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("InspectorOption", "pages/record/add.uvue", 332, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return InspectorOptionReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class InspectorOptionReactiveObject : InspectorOption, IUTSReactive<InspectorOption> {
    override var __v_raw: InspectorOption
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: InspectorOption, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, name = __v_raw.name) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): InspectorOptionReactiveObject {
        return InspectorOptionReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var name: String
        get() {
            return _tRG(__v_raw, "name", __v_raw.name, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("name")) {
                return
            }
            val oldValue = __v_raw.name
            __v_raw.name = value
            _tRS(__v_raw, "name", oldValue, value)
        }
}
open class RecordFormData (
    @JsonNotNull
    open var responsibleUnit: String,
    @JsonNotNull
    open var responsiblePerson: String,
    @JsonNotNull
    open var natureId: String,
    @JsonNotNull
    open var scoreId: String,
    @JsonNotNull
    open var assessmentAmount: String,
    @JsonNotNull
    open var safetyScore: String,
    @JsonNotNull
    open var professionId: String,
    @JsonNotNull
    open var problemDescription: String,
    @JsonNotNull
    open var location: String,
    @JsonNotNull
    open var inspector: String,
    @JsonNotNull
    open var groupLeader: String,
    @JsonNotNull
    open var deputyLeader: String,
    @JsonNotNull
    open var departmentId: String,
    @JsonNotNull
    open var rectificationDate: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("RecordFormData", "pages/record/add.uvue", 338, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return RecordFormDataReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class RecordFormDataReactiveObject : RecordFormData, IUTSReactive<RecordFormData> {
    override var __v_raw: RecordFormData
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: RecordFormData, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(responsibleUnit = __v_raw.responsibleUnit, responsiblePerson = __v_raw.responsiblePerson, natureId = __v_raw.natureId, scoreId = __v_raw.scoreId, assessmentAmount = __v_raw.assessmentAmount, safetyScore = __v_raw.safetyScore, professionId = __v_raw.professionId, problemDescription = __v_raw.problemDescription, location = __v_raw.location, inspector = __v_raw.inspector, groupLeader = __v_raw.groupLeader, deputyLeader = __v_raw.deputyLeader, departmentId = __v_raw.departmentId, rectificationDate = __v_raw.rectificationDate) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): RecordFormDataReactiveObject {
        return RecordFormDataReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var responsibleUnit: String
        get() {
            return _tRG(__v_raw, "responsibleUnit", __v_raw.responsibleUnit, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("responsibleUnit")) {
                return
            }
            val oldValue = __v_raw.responsibleUnit
            __v_raw.responsibleUnit = value
            _tRS(__v_raw, "responsibleUnit", oldValue, value)
        }
    override var responsiblePerson: String
        get() {
            return _tRG(__v_raw, "responsiblePerson", __v_raw.responsiblePerson, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("responsiblePerson")) {
                return
            }
            val oldValue = __v_raw.responsiblePerson
            __v_raw.responsiblePerson = value
            _tRS(__v_raw, "responsiblePerson", oldValue, value)
        }
    override var natureId: String
        get() {
            return _tRG(__v_raw, "natureId", __v_raw.natureId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("natureId")) {
                return
            }
            val oldValue = __v_raw.natureId
            __v_raw.natureId = value
            _tRS(__v_raw, "natureId", oldValue, value)
        }
    override var scoreId: String
        get() {
            return _tRG(__v_raw, "scoreId", __v_raw.scoreId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("scoreId")) {
                return
            }
            val oldValue = __v_raw.scoreId
            __v_raw.scoreId = value
            _tRS(__v_raw, "scoreId", oldValue, value)
        }
    override var assessmentAmount: String
        get() {
            return _tRG(__v_raw, "assessmentAmount", __v_raw.assessmentAmount, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("assessmentAmount")) {
                return
            }
            val oldValue = __v_raw.assessmentAmount
            __v_raw.assessmentAmount = value
            _tRS(__v_raw, "assessmentAmount", oldValue, value)
        }
    override var safetyScore: String
        get() {
            return _tRG(__v_raw, "safetyScore", __v_raw.safetyScore, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("safetyScore")) {
                return
            }
            val oldValue = __v_raw.safetyScore
            __v_raw.safetyScore = value
            _tRS(__v_raw, "safetyScore", oldValue, value)
        }
    override var professionId: String
        get() {
            return _tRG(__v_raw, "professionId", __v_raw.professionId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("professionId")) {
                return
            }
            val oldValue = __v_raw.professionId
            __v_raw.professionId = value
            _tRS(__v_raw, "professionId", oldValue, value)
        }
    override var problemDescription: String
        get() {
            return _tRG(__v_raw, "problemDescription", __v_raw.problemDescription, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("problemDescription")) {
                return
            }
            val oldValue = __v_raw.problemDescription
            __v_raw.problemDescription = value
            _tRS(__v_raw, "problemDescription", oldValue, value)
        }
    override var location: String
        get() {
            return _tRG(__v_raw, "location", __v_raw.location, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("location")) {
                return
            }
            val oldValue = __v_raw.location
            __v_raw.location = value
            _tRS(__v_raw, "location", oldValue, value)
        }
    override var inspector: String
        get() {
            return _tRG(__v_raw, "inspector", __v_raw.inspector, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("inspector")) {
                return
            }
            val oldValue = __v_raw.inspector
            __v_raw.inspector = value
            _tRS(__v_raw, "inspector", oldValue, value)
        }
    override var groupLeader: String
        get() {
            return _tRG(__v_raw, "groupLeader", __v_raw.groupLeader, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("groupLeader")) {
                return
            }
            val oldValue = __v_raw.groupLeader
            __v_raw.groupLeader = value
            _tRS(__v_raw, "groupLeader", oldValue, value)
        }
    override var deputyLeader: String
        get() {
            return _tRG(__v_raw, "deputyLeader", __v_raw.deputyLeader, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("deputyLeader")) {
                return
            }
            val oldValue = __v_raw.deputyLeader
            __v_raw.deputyLeader = value
            _tRS(__v_raw, "deputyLeader", oldValue, value)
        }
    override var departmentId: String
        get() {
            return _tRG(__v_raw, "departmentId", __v_raw.departmentId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("departmentId")) {
                return
            }
            val oldValue = __v_raw.departmentId
            __v_raw.departmentId = value
            _tRS(__v_raw, "departmentId", oldValue, value)
        }
    override var rectificationDate: String
        get() {
            return _tRG(__v_raw, "rectificationDate", __v_raw.rectificationDate, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("rectificationDate")) {
                return
            }
            val oldValue = __v_raw.rectificationDate
            __v_raw.rectificationDate = value
            _tRS(__v_raw, "rectificationDate", oldValue, value)
        }
}
val GenPagesRecordAddClass = CreateVueComponent(GenPagesRecordAdd::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesRecordAdd.inheritAttrs, inject = GenPagesRecordAdd.inject, props = GenPagesRecordAdd.props, propsNeedCastKeys = GenPagesRecordAdd.propsNeedCastKeys, emits = GenPagesRecordAdd.emits, components = GenPagesRecordAdd.components, styles = GenPagesRecordAdd.styles)
}
, fun(instance, renderer): GenPagesRecordAdd {
    return GenPagesRecordAdd(instance, renderer)
}
)
open class NatureOption1 (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var name: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("NatureOption", "pages/record/detail.uvue", 286, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return NatureOption1ReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class NatureOption1ReactiveObject : NatureOption1, IUTSReactive<NatureOption1> {
    override var __v_raw: NatureOption1
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: NatureOption1, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, name = __v_raw.name) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): NatureOption1ReactiveObject {
        return NatureOption1ReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var name: String
        get() {
            return _tRG(__v_raw, "name", __v_raw.name, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("name")) {
                return
            }
            val oldValue = __v_raw.name
            __v_raw.name = value
            _tRS(__v_raw, "name", oldValue, value)
        }
}
open class ProfessionOption1 (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var name: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ProfessionOption", "pages/record/detail.uvue", 291, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return ProfessionOption1ReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class ProfessionOption1ReactiveObject : ProfessionOption1, IUTSReactive<ProfessionOption1> {
    override var __v_raw: ProfessionOption1
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: ProfessionOption1, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, name = __v_raw.name) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): ProfessionOption1ReactiveObject {
        return ProfessionOption1ReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var name: String
        get() {
            return _tRG(__v_raw, "name", __v_raw.name, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("name")) {
                return
            }
            val oldValue = __v_raw.name
            __v_raw.name = value
            _tRS(__v_raw, "name", oldValue, value)
        }
}
open class DepartmentOption1 (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var name: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("DepartmentOption", "pages/record/detail.uvue", 296, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return DepartmentOption1ReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class DepartmentOption1ReactiveObject : DepartmentOption1, IUTSReactive<DepartmentOption1> {
    override var __v_raw: DepartmentOption1
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: DepartmentOption1, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, name = __v_raw.name) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): DepartmentOption1ReactiveObject {
        return DepartmentOption1ReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var name: String
        get() {
            return _tRG(__v_raw, "name", __v_raw.name, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("name")) {
                return
            }
            val oldValue = __v_raw.name
            __v_raw.name = value
            _tRS(__v_raw, "name", oldValue, value)
        }
}
open class RecordDetail (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var problemDescription: String,
    @JsonNotNull
    open var responsibleUnit: String,
    @JsonNotNull
    open var responsiblePerson: String,
    @JsonNotNull
    open var natureId: String,
    @JsonNotNull
    open var natureName: String,
    @JsonNotNull
    open var assessmentSituation: String,
    @JsonNotNull
    open var assessmentAmount: Number,
    @JsonNotNull
    open var safetyScore: Number,
    @JsonNotNull
    open var professionId: String,
    @JsonNotNull
    open var professionName: String,
    @JsonNotNull
    open var location: String,
    @JsonNotNull
    open var inspector: String,
    @JsonNotNull
    open var groupLeader: String,
    @JsonNotNull
    open var deputyLeader: String,
    @JsonNotNull
    open var departmentId: String,
    @JsonNotNull
    open var departmentName: String,
    @JsonNotNull
    open var rectificationDate: String,
    @JsonNotNull
    open var status: Number,
    @JsonNotNull
    open var createTime: String,
    @JsonNotNull
    open var updateTime: String,
    @JsonNotNull
    open var problemImageUrls: String,
    @JsonNotNull
    open var locationName: String,
    @JsonNotNull
    open var severity: Number,
    @JsonNotNull
    open var taskName: String,
    @JsonNotNull
    open var reporterName: String,
    @JsonNotNull
    open var suggestions: String,
    @JsonNotNull
    open var reporterId: String,
    @JsonNotNull
    open var rectificationEffect: String,
    @JsonNotNull
    open var rectificationImageUrls: String,
    @JsonNotNull
    open var rectificationTime: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("RecordDetail", "pages/record/detail.uvue", 301, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return RecordDetailReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class RecordDetailReactiveObject : RecordDetail, IUTSReactive<RecordDetail> {
    override var __v_raw: RecordDetail
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: RecordDetail, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, problemDescription = __v_raw.problemDescription, responsibleUnit = __v_raw.responsibleUnit, responsiblePerson = __v_raw.responsiblePerson, natureId = __v_raw.natureId, natureName = __v_raw.natureName, assessmentSituation = __v_raw.assessmentSituation, assessmentAmount = __v_raw.assessmentAmount, safetyScore = __v_raw.safetyScore, professionId = __v_raw.professionId, professionName = __v_raw.professionName, location = __v_raw.location, inspector = __v_raw.inspector, groupLeader = __v_raw.groupLeader, deputyLeader = __v_raw.deputyLeader, departmentId = __v_raw.departmentId, departmentName = __v_raw.departmentName, rectificationDate = __v_raw.rectificationDate, status = __v_raw.status, createTime = __v_raw.createTime, updateTime = __v_raw.updateTime, problemImageUrls = __v_raw.problemImageUrls, locationName = __v_raw.locationName, severity = __v_raw.severity, taskName = __v_raw.taskName, reporterName = __v_raw.reporterName, suggestions = __v_raw.suggestions, reporterId = __v_raw.reporterId, rectificationEffect = __v_raw.rectificationEffect, rectificationImageUrls = __v_raw.rectificationImageUrls, rectificationTime = __v_raw.rectificationTime) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): RecordDetailReactiveObject {
        return RecordDetailReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var problemDescription: String
        get() {
            return _tRG(__v_raw, "problemDescription", __v_raw.problemDescription, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("problemDescription")) {
                return
            }
            val oldValue = __v_raw.problemDescription
            __v_raw.problemDescription = value
            _tRS(__v_raw, "problemDescription", oldValue, value)
        }
    override var responsibleUnit: String
        get() {
            return _tRG(__v_raw, "responsibleUnit", __v_raw.responsibleUnit, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("responsibleUnit")) {
                return
            }
            val oldValue = __v_raw.responsibleUnit
            __v_raw.responsibleUnit = value
            _tRS(__v_raw, "responsibleUnit", oldValue, value)
        }
    override var responsiblePerson: String
        get() {
            return _tRG(__v_raw, "responsiblePerson", __v_raw.responsiblePerson, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("responsiblePerson")) {
                return
            }
            val oldValue = __v_raw.responsiblePerson
            __v_raw.responsiblePerson = value
            _tRS(__v_raw, "responsiblePerson", oldValue, value)
        }
    override var natureId: String
        get() {
            return _tRG(__v_raw, "natureId", __v_raw.natureId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("natureId")) {
                return
            }
            val oldValue = __v_raw.natureId
            __v_raw.natureId = value
            _tRS(__v_raw, "natureId", oldValue, value)
        }
    override var natureName: String
        get() {
            return _tRG(__v_raw, "natureName", __v_raw.natureName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("natureName")) {
                return
            }
            val oldValue = __v_raw.natureName
            __v_raw.natureName = value
            _tRS(__v_raw, "natureName", oldValue, value)
        }
    override var assessmentSituation: String
        get() {
            return _tRG(__v_raw, "assessmentSituation", __v_raw.assessmentSituation, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("assessmentSituation")) {
                return
            }
            val oldValue = __v_raw.assessmentSituation
            __v_raw.assessmentSituation = value
            _tRS(__v_raw, "assessmentSituation", oldValue, value)
        }
    override var assessmentAmount: Number
        get() {
            return _tRG(__v_raw, "assessmentAmount", __v_raw.assessmentAmount, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("assessmentAmount")) {
                return
            }
            val oldValue = __v_raw.assessmentAmount
            __v_raw.assessmentAmount = value
            _tRS(__v_raw, "assessmentAmount", oldValue, value)
        }
    override var safetyScore: Number
        get() {
            return _tRG(__v_raw, "safetyScore", __v_raw.safetyScore, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("safetyScore")) {
                return
            }
            val oldValue = __v_raw.safetyScore
            __v_raw.safetyScore = value
            _tRS(__v_raw, "safetyScore", oldValue, value)
        }
    override var professionId: String
        get() {
            return _tRG(__v_raw, "professionId", __v_raw.professionId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("professionId")) {
                return
            }
            val oldValue = __v_raw.professionId
            __v_raw.professionId = value
            _tRS(__v_raw, "professionId", oldValue, value)
        }
    override var professionName: String
        get() {
            return _tRG(__v_raw, "professionName", __v_raw.professionName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("professionName")) {
                return
            }
            val oldValue = __v_raw.professionName
            __v_raw.professionName = value
            _tRS(__v_raw, "professionName", oldValue, value)
        }
    override var location: String
        get() {
            return _tRG(__v_raw, "location", __v_raw.location, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("location")) {
                return
            }
            val oldValue = __v_raw.location
            __v_raw.location = value
            _tRS(__v_raw, "location", oldValue, value)
        }
    override var inspector: String
        get() {
            return _tRG(__v_raw, "inspector", __v_raw.inspector, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("inspector")) {
                return
            }
            val oldValue = __v_raw.inspector
            __v_raw.inspector = value
            _tRS(__v_raw, "inspector", oldValue, value)
        }
    override var groupLeader: String
        get() {
            return _tRG(__v_raw, "groupLeader", __v_raw.groupLeader, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("groupLeader")) {
                return
            }
            val oldValue = __v_raw.groupLeader
            __v_raw.groupLeader = value
            _tRS(__v_raw, "groupLeader", oldValue, value)
        }
    override var deputyLeader: String
        get() {
            return _tRG(__v_raw, "deputyLeader", __v_raw.deputyLeader, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("deputyLeader")) {
                return
            }
            val oldValue = __v_raw.deputyLeader
            __v_raw.deputyLeader = value
            _tRS(__v_raw, "deputyLeader", oldValue, value)
        }
    override var departmentId: String
        get() {
            return _tRG(__v_raw, "departmentId", __v_raw.departmentId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("departmentId")) {
                return
            }
            val oldValue = __v_raw.departmentId
            __v_raw.departmentId = value
            _tRS(__v_raw, "departmentId", oldValue, value)
        }
    override var departmentName: String
        get() {
            return _tRG(__v_raw, "departmentName", __v_raw.departmentName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("departmentName")) {
                return
            }
            val oldValue = __v_raw.departmentName
            __v_raw.departmentName = value
            _tRS(__v_raw, "departmentName", oldValue, value)
        }
    override var rectificationDate: String
        get() {
            return _tRG(__v_raw, "rectificationDate", __v_raw.rectificationDate, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("rectificationDate")) {
                return
            }
            val oldValue = __v_raw.rectificationDate
            __v_raw.rectificationDate = value
            _tRS(__v_raw, "rectificationDate", oldValue, value)
        }
    override var status: Number
        get() {
            return _tRG(__v_raw, "status", __v_raw.status, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("status")) {
                return
            }
            val oldValue = __v_raw.status
            __v_raw.status = value
            _tRS(__v_raw, "status", oldValue, value)
        }
    override var createTime: String
        get() {
            return _tRG(__v_raw, "createTime", __v_raw.createTime, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("createTime")) {
                return
            }
            val oldValue = __v_raw.createTime
            __v_raw.createTime = value
            _tRS(__v_raw, "createTime", oldValue, value)
        }
    override var updateTime: String
        get() {
            return _tRG(__v_raw, "updateTime", __v_raw.updateTime, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("updateTime")) {
                return
            }
            val oldValue = __v_raw.updateTime
            __v_raw.updateTime = value
            _tRS(__v_raw, "updateTime", oldValue, value)
        }
    override var problemImageUrls: String
        get() {
            return _tRG(__v_raw, "problemImageUrls", __v_raw.problemImageUrls, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("problemImageUrls")) {
                return
            }
            val oldValue = __v_raw.problemImageUrls
            __v_raw.problemImageUrls = value
            _tRS(__v_raw, "problemImageUrls", oldValue, value)
        }
    override var locationName: String
        get() {
            return _tRG(__v_raw, "locationName", __v_raw.locationName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("locationName")) {
                return
            }
            val oldValue = __v_raw.locationName
            __v_raw.locationName = value
            _tRS(__v_raw, "locationName", oldValue, value)
        }
    override var severity: Number
        get() {
            return _tRG(__v_raw, "severity", __v_raw.severity, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("severity")) {
                return
            }
            val oldValue = __v_raw.severity
            __v_raw.severity = value
            _tRS(__v_raw, "severity", oldValue, value)
        }
    override var taskName: String
        get() {
            return _tRG(__v_raw, "taskName", __v_raw.taskName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("taskName")) {
                return
            }
            val oldValue = __v_raw.taskName
            __v_raw.taskName = value
            _tRS(__v_raw, "taskName", oldValue, value)
        }
    override var reporterName: String
        get() {
            return _tRG(__v_raw, "reporterName", __v_raw.reporterName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("reporterName")) {
                return
            }
            val oldValue = __v_raw.reporterName
            __v_raw.reporterName = value
            _tRS(__v_raw, "reporterName", oldValue, value)
        }
    override var suggestions: String
        get() {
            return _tRG(__v_raw, "suggestions", __v_raw.suggestions, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("suggestions")) {
                return
            }
            val oldValue = __v_raw.suggestions
            __v_raw.suggestions = value
            _tRS(__v_raw, "suggestions", oldValue, value)
        }
    override var reporterId: String
        get() {
            return _tRG(__v_raw, "reporterId", __v_raw.reporterId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("reporterId")) {
                return
            }
            val oldValue = __v_raw.reporterId
            __v_raw.reporterId = value
            _tRS(__v_raw, "reporterId", oldValue, value)
        }
    override var rectificationEffect: String
        get() {
            return _tRG(__v_raw, "rectificationEffect", __v_raw.rectificationEffect, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("rectificationEffect")) {
                return
            }
            val oldValue = __v_raw.rectificationEffect
            __v_raw.rectificationEffect = value
            _tRS(__v_raw, "rectificationEffect", oldValue, value)
        }
    override var rectificationImageUrls: String
        get() {
            return _tRG(__v_raw, "rectificationImageUrls", __v_raw.rectificationImageUrls, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("rectificationImageUrls")) {
                return
            }
            val oldValue = __v_raw.rectificationImageUrls
            __v_raw.rectificationImageUrls = value
            _tRS(__v_raw, "rectificationImageUrls", oldValue, value)
        }
    override var rectificationTime: String
        get() {
            return _tRG(__v_raw, "rectificationTime", __v_raw.rectificationTime, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("rectificationTime")) {
                return
            }
            val oldValue = __v_raw.rectificationTime
            __v_raw.rectificationTime = value
            _tRS(__v_raw, "rectificationTime", oldValue, value)
        }
}
val GenPagesRecordDetailClass = CreateVueComponent(GenPagesRecordDetail::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesRecordDetail.inheritAttrs, inject = GenPagesRecordDetail.inject, props = GenPagesRecordDetail.props, propsNeedCastKeys = GenPagesRecordDetail.propsNeedCastKeys, emits = GenPagesRecordDetail.emits, components = GenPagesRecordDetail.components, styles = GenPagesRecordDetail.styles)
}
, fun(instance, renderer): GenPagesRecordDetail {
    return GenPagesRecordDetail(instance, renderer)
}
)
open class NatureOption2 (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var name: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("NatureOption", "pages/record/review.uvue", 296, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return NatureOption2ReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class NatureOption2ReactiveObject : NatureOption2, IUTSReactive<NatureOption2> {
    override var __v_raw: NatureOption2
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: NatureOption2, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, name = __v_raw.name) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): NatureOption2ReactiveObject {
        return NatureOption2ReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var name: String
        get() {
            return _tRG(__v_raw, "name", __v_raw.name, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("name")) {
                return
            }
            val oldValue = __v_raw.name
            __v_raw.name = value
            _tRS(__v_raw, "name", oldValue, value)
        }
}
open class ProfessionOption2 (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var name: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ProfessionOption", "pages/record/review.uvue", 301, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return ProfessionOption2ReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class ProfessionOption2ReactiveObject : ProfessionOption2, IUTSReactive<ProfessionOption2> {
    override var __v_raw: ProfessionOption2
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: ProfessionOption2, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, name = __v_raw.name) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): ProfessionOption2ReactiveObject {
        return ProfessionOption2ReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var name: String
        get() {
            return _tRG(__v_raw, "name", __v_raw.name, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("name")) {
                return
            }
            val oldValue = __v_raw.name
            __v_raw.name = value
            _tRS(__v_raw, "name", oldValue, value)
        }
}
open class DepartmentOption2 (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var name: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("DepartmentOption", "pages/record/review.uvue", 306, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return DepartmentOption2ReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class DepartmentOption2ReactiveObject : DepartmentOption2, IUTSReactive<DepartmentOption2> {
    override var __v_raw: DepartmentOption2
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: DepartmentOption2, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, name = __v_raw.name) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): DepartmentOption2ReactiveObject {
        return DepartmentOption2ReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var name: String
        get() {
            return _tRG(__v_raw, "name", __v_raw.name, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("name")) {
                return
            }
            val oldValue = __v_raw.name
            __v_raw.name = value
            _tRS(__v_raw, "name", oldValue, value)
        }
}
open class RecordDetail1 (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var problemDescription: String,
    @JsonNotNull
    open var responsibleUnit: String,
    @JsonNotNull
    open var responsiblePerson: String,
    @JsonNotNull
    open var natureId: String,
    @JsonNotNull
    open var natureName: String,
    @JsonNotNull
    open var assessmentSituation: String,
    @JsonNotNull
    open var assessmentAmount: Number,
    @JsonNotNull
    open var safetyScore: Number,
    @JsonNotNull
    open var professionId: String,
    @JsonNotNull
    open var professionName: String,
    @JsonNotNull
    open var location: String,
    @JsonNotNull
    open var inspector: String,
    @JsonNotNull
    open var groupLeader: String,
    @JsonNotNull
    open var deputyLeader: String,
    @JsonNotNull
    open var departmentId: String,
    @JsonNotNull
    open var departmentName: String,
    @JsonNotNull
    open var rectificationDate: String,
    @JsonNotNull
    open var status: Number,
    @JsonNotNull
    open var createTime: String,
    @JsonNotNull
    open var updateTime: String,
    @JsonNotNull
    open var problemImageUrls: String,
    @JsonNotNull
    open var locationName: String,
    @JsonNotNull
    open var severity: Number,
    @JsonNotNull
    open var taskName: String,
    @JsonNotNull
    open var reporterName: String,
    @JsonNotNull
    open var suggestions: String,
    @JsonNotNull
    open var reporterId: String,
    @JsonNotNull
    open var rectificationEffect: String,
    @JsonNotNull
    open var rectificationImageUrls: String,
    @JsonNotNull
    open var rectificationTime: String,
    @JsonNotNull
    open var reviewEffect: String,
    @JsonNotNull
    open var reviewImageUrls: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("RecordDetail", "pages/record/review.uvue", 311, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return RecordDetail1ReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class RecordDetail1ReactiveObject : RecordDetail1, IUTSReactive<RecordDetail1> {
    override var __v_raw: RecordDetail1
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: RecordDetail1, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, problemDescription = __v_raw.problemDescription, responsibleUnit = __v_raw.responsibleUnit, responsiblePerson = __v_raw.responsiblePerson, natureId = __v_raw.natureId, natureName = __v_raw.natureName, assessmentSituation = __v_raw.assessmentSituation, assessmentAmount = __v_raw.assessmentAmount, safetyScore = __v_raw.safetyScore, professionId = __v_raw.professionId, professionName = __v_raw.professionName, location = __v_raw.location, inspector = __v_raw.inspector, groupLeader = __v_raw.groupLeader, deputyLeader = __v_raw.deputyLeader, departmentId = __v_raw.departmentId, departmentName = __v_raw.departmentName, rectificationDate = __v_raw.rectificationDate, status = __v_raw.status, createTime = __v_raw.createTime, updateTime = __v_raw.updateTime, problemImageUrls = __v_raw.problemImageUrls, locationName = __v_raw.locationName, severity = __v_raw.severity, taskName = __v_raw.taskName, reporterName = __v_raw.reporterName, suggestions = __v_raw.suggestions, reporterId = __v_raw.reporterId, rectificationEffect = __v_raw.rectificationEffect, rectificationImageUrls = __v_raw.rectificationImageUrls, rectificationTime = __v_raw.rectificationTime, reviewEffect = __v_raw.reviewEffect, reviewImageUrls = __v_raw.reviewImageUrls) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): RecordDetail1ReactiveObject {
        return RecordDetail1ReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var id: String
        get() {
            return _tRG(__v_raw, "id", __v_raw.id, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("id")) {
                return
            }
            val oldValue = __v_raw.id
            __v_raw.id = value
            _tRS(__v_raw, "id", oldValue, value)
        }
    override var problemDescription: String
        get() {
            return _tRG(__v_raw, "problemDescription", __v_raw.problemDescription, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("problemDescription")) {
                return
            }
            val oldValue = __v_raw.problemDescription
            __v_raw.problemDescription = value
            _tRS(__v_raw, "problemDescription", oldValue, value)
        }
    override var responsibleUnit: String
        get() {
            return _tRG(__v_raw, "responsibleUnit", __v_raw.responsibleUnit, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("responsibleUnit")) {
                return
            }
            val oldValue = __v_raw.responsibleUnit
            __v_raw.responsibleUnit = value
            _tRS(__v_raw, "responsibleUnit", oldValue, value)
        }
    override var responsiblePerson: String
        get() {
            return _tRG(__v_raw, "responsiblePerson", __v_raw.responsiblePerson, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("responsiblePerson")) {
                return
            }
            val oldValue = __v_raw.responsiblePerson
            __v_raw.responsiblePerson = value
            _tRS(__v_raw, "responsiblePerson", oldValue, value)
        }
    override var natureId: String
        get() {
            return _tRG(__v_raw, "natureId", __v_raw.natureId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("natureId")) {
                return
            }
            val oldValue = __v_raw.natureId
            __v_raw.natureId = value
            _tRS(__v_raw, "natureId", oldValue, value)
        }
    override var natureName: String
        get() {
            return _tRG(__v_raw, "natureName", __v_raw.natureName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("natureName")) {
                return
            }
            val oldValue = __v_raw.natureName
            __v_raw.natureName = value
            _tRS(__v_raw, "natureName", oldValue, value)
        }
    override var assessmentSituation: String
        get() {
            return _tRG(__v_raw, "assessmentSituation", __v_raw.assessmentSituation, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("assessmentSituation")) {
                return
            }
            val oldValue = __v_raw.assessmentSituation
            __v_raw.assessmentSituation = value
            _tRS(__v_raw, "assessmentSituation", oldValue, value)
        }
    override var assessmentAmount: Number
        get() {
            return _tRG(__v_raw, "assessmentAmount", __v_raw.assessmentAmount, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("assessmentAmount")) {
                return
            }
            val oldValue = __v_raw.assessmentAmount
            __v_raw.assessmentAmount = value
            _tRS(__v_raw, "assessmentAmount", oldValue, value)
        }
    override var safetyScore: Number
        get() {
            return _tRG(__v_raw, "safetyScore", __v_raw.safetyScore, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("safetyScore")) {
                return
            }
            val oldValue = __v_raw.safetyScore
            __v_raw.safetyScore = value
            _tRS(__v_raw, "safetyScore", oldValue, value)
        }
    override var professionId: String
        get() {
            return _tRG(__v_raw, "professionId", __v_raw.professionId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("professionId")) {
                return
            }
            val oldValue = __v_raw.professionId
            __v_raw.professionId = value
            _tRS(__v_raw, "professionId", oldValue, value)
        }
    override var professionName: String
        get() {
            return _tRG(__v_raw, "professionName", __v_raw.professionName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("professionName")) {
                return
            }
            val oldValue = __v_raw.professionName
            __v_raw.professionName = value
            _tRS(__v_raw, "professionName", oldValue, value)
        }
    override var location: String
        get() {
            return _tRG(__v_raw, "location", __v_raw.location, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("location")) {
                return
            }
            val oldValue = __v_raw.location
            __v_raw.location = value
            _tRS(__v_raw, "location", oldValue, value)
        }
    override var inspector: String
        get() {
            return _tRG(__v_raw, "inspector", __v_raw.inspector, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("inspector")) {
                return
            }
            val oldValue = __v_raw.inspector
            __v_raw.inspector = value
            _tRS(__v_raw, "inspector", oldValue, value)
        }
    override var groupLeader: String
        get() {
            return _tRG(__v_raw, "groupLeader", __v_raw.groupLeader, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("groupLeader")) {
                return
            }
            val oldValue = __v_raw.groupLeader
            __v_raw.groupLeader = value
            _tRS(__v_raw, "groupLeader", oldValue, value)
        }
    override var deputyLeader: String
        get() {
            return _tRG(__v_raw, "deputyLeader", __v_raw.deputyLeader, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("deputyLeader")) {
                return
            }
            val oldValue = __v_raw.deputyLeader
            __v_raw.deputyLeader = value
            _tRS(__v_raw, "deputyLeader", oldValue, value)
        }
    override var departmentId: String
        get() {
            return _tRG(__v_raw, "departmentId", __v_raw.departmentId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("departmentId")) {
                return
            }
            val oldValue = __v_raw.departmentId
            __v_raw.departmentId = value
            _tRS(__v_raw, "departmentId", oldValue, value)
        }
    override var departmentName: String
        get() {
            return _tRG(__v_raw, "departmentName", __v_raw.departmentName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("departmentName")) {
                return
            }
            val oldValue = __v_raw.departmentName
            __v_raw.departmentName = value
            _tRS(__v_raw, "departmentName", oldValue, value)
        }
    override var rectificationDate: String
        get() {
            return _tRG(__v_raw, "rectificationDate", __v_raw.rectificationDate, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("rectificationDate")) {
                return
            }
            val oldValue = __v_raw.rectificationDate
            __v_raw.rectificationDate = value
            _tRS(__v_raw, "rectificationDate", oldValue, value)
        }
    override var status: Number
        get() {
            return _tRG(__v_raw, "status", __v_raw.status, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("status")) {
                return
            }
            val oldValue = __v_raw.status
            __v_raw.status = value
            _tRS(__v_raw, "status", oldValue, value)
        }
    override var createTime: String
        get() {
            return _tRG(__v_raw, "createTime", __v_raw.createTime, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("createTime")) {
                return
            }
            val oldValue = __v_raw.createTime
            __v_raw.createTime = value
            _tRS(__v_raw, "createTime", oldValue, value)
        }
    override var updateTime: String
        get() {
            return _tRG(__v_raw, "updateTime", __v_raw.updateTime, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("updateTime")) {
                return
            }
            val oldValue = __v_raw.updateTime
            __v_raw.updateTime = value
            _tRS(__v_raw, "updateTime", oldValue, value)
        }
    override var problemImageUrls: String
        get() {
            return _tRG(__v_raw, "problemImageUrls", __v_raw.problemImageUrls, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("problemImageUrls")) {
                return
            }
            val oldValue = __v_raw.problemImageUrls
            __v_raw.problemImageUrls = value
            _tRS(__v_raw, "problemImageUrls", oldValue, value)
        }
    override var locationName: String
        get() {
            return _tRG(__v_raw, "locationName", __v_raw.locationName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("locationName")) {
                return
            }
            val oldValue = __v_raw.locationName
            __v_raw.locationName = value
            _tRS(__v_raw, "locationName", oldValue, value)
        }
    override var severity: Number
        get() {
            return _tRG(__v_raw, "severity", __v_raw.severity, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("severity")) {
                return
            }
            val oldValue = __v_raw.severity
            __v_raw.severity = value
            _tRS(__v_raw, "severity", oldValue, value)
        }
    override var taskName: String
        get() {
            return _tRG(__v_raw, "taskName", __v_raw.taskName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("taskName")) {
                return
            }
            val oldValue = __v_raw.taskName
            __v_raw.taskName = value
            _tRS(__v_raw, "taskName", oldValue, value)
        }
    override var reporterName: String
        get() {
            return _tRG(__v_raw, "reporterName", __v_raw.reporterName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("reporterName")) {
                return
            }
            val oldValue = __v_raw.reporterName
            __v_raw.reporterName = value
            _tRS(__v_raw, "reporterName", oldValue, value)
        }
    override var suggestions: String
        get() {
            return _tRG(__v_raw, "suggestions", __v_raw.suggestions, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("suggestions")) {
                return
            }
            val oldValue = __v_raw.suggestions
            __v_raw.suggestions = value
            _tRS(__v_raw, "suggestions", oldValue, value)
        }
    override var reporterId: String
        get() {
            return _tRG(__v_raw, "reporterId", __v_raw.reporterId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("reporterId")) {
                return
            }
            val oldValue = __v_raw.reporterId
            __v_raw.reporterId = value
            _tRS(__v_raw, "reporterId", oldValue, value)
        }
    override var rectificationEffect: String
        get() {
            return _tRG(__v_raw, "rectificationEffect", __v_raw.rectificationEffect, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("rectificationEffect")) {
                return
            }
            val oldValue = __v_raw.rectificationEffect
            __v_raw.rectificationEffect = value
            _tRS(__v_raw, "rectificationEffect", oldValue, value)
        }
    override var rectificationImageUrls: String
        get() {
            return _tRG(__v_raw, "rectificationImageUrls", __v_raw.rectificationImageUrls, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("rectificationImageUrls")) {
                return
            }
            val oldValue = __v_raw.rectificationImageUrls
            __v_raw.rectificationImageUrls = value
            _tRS(__v_raw, "rectificationImageUrls", oldValue, value)
        }
    override var rectificationTime: String
        get() {
            return _tRG(__v_raw, "rectificationTime", __v_raw.rectificationTime, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("rectificationTime")) {
                return
            }
            val oldValue = __v_raw.rectificationTime
            __v_raw.rectificationTime = value
            _tRS(__v_raw, "rectificationTime", oldValue, value)
        }
    override var reviewEffect: String
        get() {
            return _tRG(__v_raw, "reviewEffect", __v_raw.reviewEffect, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("reviewEffect")) {
                return
            }
            val oldValue = __v_raw.reviewEffect
            __v_raw.reviewEffect = value
            _tRS(__v_raw, "reviewEffect", oldValue, value)
        }
    override var reviewImageUrls: String
        get() {
            return _tRG(__v_raw, "reviewImageUrls", __v_raw.reviewImageUrls, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("reviewImageUrls")) {
                return
            }
            val oldValue = __v_raw.reviewImageUrls
            __v_raw.reviewImageUrls = value
            _tRS(__v_raw, "reviewImageUrls", oldValue, value)
        }
}
val GenPagesRecordReviewClass = CreateVueComponent(GenPagesRecordReview::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesRecordReview.inheritAttrs, inject = GenPagesRecordReview.inject, props = GenPagesRecordReview.props, propsNeedCastKeys = GenPagesRecordReview.propsNeedCastKeys, emits = GenPagesRecordReview.emits, components = GenPagesRecordReview.components, styles = GenPagesRecordReview.styles)
}
, fun(instance, renderer): GenPagesRecordReview {
    return GenPagesRecordReview(instance, renderer)
}
)
open class PeriodTab (
    @JsonNotNull
    open var label: String,
    @JsonNotNull
    open var value: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("PeriodTab", "pages/statistics/statistics.uvue", 148, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return PeriodTabReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class PeriodTabReactiveObject : PeriodTab, IUTSReactive<PeriodTab> {
    override var __v_raw: PeriodTab
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: PeriodTab, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(label = __v_raw.label, value = __v_raw.value) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): PeriodTabReactiveObject {
        return PeriodTabReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var label: String
        get() {
            return _tRG(__v_raw, "label", __v_raw.label, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("label")) {
                return
            }
            val oldValue = __v_raw.label
            __v_raw.label = value
            _tRS(__v_raw, "label", oldValue, value)
        }
    override var value: String
        get() {
            return _tRG(__v_raw, "value", __v_raw.value, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("value")) {
                return
            }
            val oldValue = __v_raw.value
            __v_raw.value = value
            _tRS(__v_raw, "value", oldValue, value)
        }
}
open class OverviewData (
    @JsonNotNull
    open var totalRecords: Number,
    @JsonNotNull
    open var completedTasks: Number,
    @JsonNotNull
    open var pendingRecords: Number,
    @JsonNotNull
    open var completionRate: Number,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("OverviewData", "pages/statistics/statistics.uvue", 153, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return OverviewDataReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class OverviewDataReactiveObject : OverviewData, IUTSReactive<OverviewData> {
    override var __v_raw: OverviewData
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: OverviewData, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(totalRecords = __v_raw.totalRecords, completedTasks = __v_raw.completedTasks, pendingRecords = __v_raw.pendingRecords, completionRate = __v_raw.completionRate) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): OverviewDataReactiveObject {
        return OverviewDataReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var totalRecords: Number
        get() {
            return _tRG(__v_raw, "totalRecords", __v_raw.totalRecords, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("totalRecords")) {
                return
            }
            val oldValue = __v_raw.totalRecords
            __v_raw.totalRecords = value
            _tRS(__v_raw, "totalRecords", oldValue, value)
        }
    override var completedTasks: Number
        get() {
            return _tRG(__v_raw, "completedTasks", __v_raw.completedTasks, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("completedTasks")) {
                return
            }
            val oldValue = __v_raw.completedTasks
            __v_raw.completedTasks = value
            _tRS(__v_raw, "completedTasks", oldValue, value)
        }
    override var pendingRecords: Number
        get() {
            return _tRG(__v_raw, "pendingRecords", __v_raw.pendingRecords, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("pendingRecords")) {
                return
            }
            val oldValue = __v_raw.pendingRecords
            __v_raw.pendingRecords = value
            _tRS(__v_raw, "pendingRecords", oldValue, value)
        }
    override var completionRate: Number
        get() {
            return _tRG(__v_raw, "completionRate", __v_raw.completionRate, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("completionRate")) {
                return
            }
            val oldValue = __v_raw.completionRate
            __v_raw.completionRate = value
            _tRS(__v_raw, "completionRate", oldValue, value)
        }
}
open class StatusDistributionItem (
    @JsonNotNull
    open var status: String,
    @JsonNotNull
    open var count: Number,
    @JsonNotNull
    open var percentage: Number,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("StatusDistributionItem", "pages/statistics/statistics.uvue", 160, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return StatusDistributionItemReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class StatusDistributionItemReactiveObject : StatusDistributionItem, IUTSReactive<StatusDistributionItem> {
    override var __v_raw: StatusDistributionItem
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: StatusDistributionItem, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(status = __v_raw.status, count = __v_raw.count, percentage = __v_raw.percentage) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): StatusDistributionItemReactiveObject {
        return StatusDistributionItemReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var status: String
        get() {
            return _tRG(__v_raw, "status", __v_raw.status, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("status")) {
                return
            }
            val oldValue = __v_raw.status
            __v_raw.status = value
            _tRS(__v_raw, "status", oldValue, value)
        }
    override var count: Number
        get() {
            return _tRG(__v_raw, "count", __v_raw.count, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("count")) {
                return
            }
            val oldValue = __v_raw.count
            __v_raw.count = value
            _tRS(__v_raw, "count", oldValue, value)
        }
    override var percentage: Number
        get() {
            return _tRG(__v_raw, "percentage", __v_raw.percentage, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("percentage")) {
                return
            }
            val oldValue = __v_raw.percentage
            __v_raw.percentage = value
            _tRS(__v_raw, "percentage", oldValue, value)
        }
}
open class SeverityDistributionItem (
    @JsonNotNull
    open var severity: String,
    @JsonNotNull
    open var count: Number,
    @JsonNotNull
    open var percentage: Number,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("SeverityDistributionItem", "pages/statistics/statistics.uvue", 166, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return SeverityDistributionItemReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class SeverityDistributionItemReactiveObject : SeverityDistributionItem, IUTSReactive<SeverityDistributionItem> {
    override var __v_raw: SeverityDistributionItem
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: SeverityDistributionItem, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(severity = __v_raw.severity, count = __v_raw.count, percentage = __v_raw.percentage) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): SeverityDistributionItemReactiveObject {
        return SeverityDistributionItemReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var severity: String
        get() {
            return _tRG(__v_raw, "severity", __v_raw.severity, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("severity")) {
                return
            }
            val oldValue = __v_raw.severity
            __v_raw.severity = value
            _tRS(__v_raw, "severity", oldValue, value)
        }
    override var count: Number
        get() {
            return _tRG(__v_raw, "count", __v_raw.count, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("count")) {
                return
            }
            val oldValue = __v_raw.count
            __v_raw.count = value
            _tRS(__v_raw, "count", oldValue, value)
        }
    override var percentage: Number
        get() {
            return _tRG(__v_raw, "percentage", __v_raw.percentage, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("percentage")) {
                return
            }
            val oldValue = __v_raw.percentage
            __v_raw.percentage = value
            _tRS(__v_raw, "percentage", oldValue, value)
        }
}
open class TrendDataItem (
    @JsonNotNull
    open var date: String,
    @JsonNotNull
    open var newCount: Number,
    @JsonNotNull
    open var completedCount: Number,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("TrendDataItem", "pages/statistics/statistics.uvue", 172, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return TrendDataItemReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class TrendDataItemReactiveObject : TrendDataItem, IUTSReactive<TrendDataItem> {
    override var __v_raw: TrendDataItem
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: TrendDataItem, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(date = __v_raw.date, newCount = __v_raw.newCount, completedCount = __v_raw.completedCount) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): TrendDataItemReactiveObject {
        return TrendDataItemReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var date: String
        get() {
            return _tRG(__v_raw, "date", __v_raw.date, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("date")) {
                return
            }
            val oldValue = __v_raw.date
            __v_raw.date = value
            _tRS(__v_raw, "date", oldValue, value)
        }
    override var newCount: Number
        get() {
            return _tRG(__v_raw, "newCount", __v_raw.newCount, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("newCount")) {
                return
            }
            val oldValue = __v_raw.newCount
            __v_raw.newCount = value
            _tRS(__v_raw, "newCount", oldValue, value)
        }
    override var completedCount: Number
        get() {
            return _tRG(__v_raw, "completedCount", __v_raw.completedCount, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("completedCount")) {
                return
            }
            val oldValue = __v_raw.completedCount
            __v_raw.completedCount = value
            _tRS(__v_raw, "completedCount", oldValue, value)
        }
}
open class LocationStatsItem (
    @JsonNotNull
    open var locationId: String,
    @JsonNotNull
    open var locationName: String,
    @JsonNotNull
    open var recordCount: Number,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("LocationStatsItem", "pages/statistics/statistics.uvue", 178, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return LocationStatsItemReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class LocationStatsItemReactiveObject : LocationStatsItem, IUTSReactive<LocationStatsItem> {
    override var __v_raw: LocationStatsItem
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: LocationStatsItem, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(locationId = __v_raw.locationId, locationName = __v_raw.locationName, recordCount = __v_raw.recordCount) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): LocationStatsItemReactiveObject {
        return LocationStatsItemReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var locationId: String
        get() {
            return _tRG(__v_raw, "locationId", __v_raw.locationId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("locationId")) {
                return
            }
            val oldValue = __v_raw.locationId
            __v_raw.locationId = value
            _tRS(__v_raw, "locationId", oldValue, value)
        }
    override var locationName: String
        get() {
            return _tRG(__v_raw, "locationName", __v_raw.locationName, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("locationName")) {
                return
            }
            val oldValue = __v_raw.locationName
            __v_raw.locationName = value
            _tRS(__v_raw, "locationName", oldValue, value)
        }
    override var recordCount: Number
        get() {
            return _tRG(__v_raw, "recordCount", __v_raw.recordCount, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("recordCount")) {
                return
            }
            val oldValue = __v_raw.recordCount
            __v_raw.recordCount = value
            _tRS(__v_raw, "recordCount", oldValue, value)
        }
}
val GenPagesStatisticsStatisticsClass = CreateVueComponent(GenPagesStatisticsStatistics::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesStatisticsStatistics.inheritAttrs, inject = GenPagesStatisticsStatistics.inject, props = GenPagesStatisticsStatistics.props, propsNeedCastKeys = GenPagesStatisticsStatistics.propsNeedCastKeys, emits = GenPagesStatisticsStatistics.emits, components = GenPagesStatisticsStatistics.components, styles = GenPagesStatisticsStatistics.styles)
}
, fun(instance, renderer): GenPagesStatisticsStatistics {
    return GenPagesStatisticsStatistics(instance, renderer)
}
)
val GenPagesChartTestChartTestClass = CreateVueComponent(GenPagesChartTestChartTest::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesChartTestChartTest.inheritAttrs, inject = GenPagesChartTestChartTest.inject, props = GenPagesChartTestChartTest.props, propsNeedCastKeys = GenPagesChartTestChartTest.propsNeedCastKeys, emits = GenPagesChartTestChartTest.emits, components = GenPagesChartTestChartTest.components, styles = GenPagesChartTestChartTest.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesChartTestChartTest.setup(props as GenPagesChartTestChartTest)
    }
    )
}
, fun(instance, renderer): GenPagesChartTestChartTest {
    return GenPagesChartTestChartTest(instance, renderer)
}
)
fun createApp(): UTSJSONObject {
    val app = createSSRApp(GenAppClass)
    return _uO("app" to app)
}
fun main(app: IApp) {
    definePageRoutes()
    defineAppConfig()
    (createApp()["app"] as VueApp).mount(app, GenUniApp())
}
open class UniAppConfig : io.dcloud.uniapp.appframe.AppConfig {
    override var name: String = "工业巡检"
    override var appid: String = "__UNI__630A075"
    override var versionName: String = "1.0.0"
    override var versionCode: String = "100"
    override var uniCompilerVersion: String = "4.76"
    constructor() : super() {}
}
fun definePageRoutes() {
    __uniRoutes.push(UniPageRoute(path = "pages/home/home", component = GenPagesHomeHomeClass, meta = UniPageMeta(isQuit = true), style = _uM("navigationBarTitleText" to "首页", "enablePullDownRefresh" to true, "disableScroll" to false)))
    __uniRoutes.push(UniPageRoute(path = "pages/weekly/weekly", component = GenPagesWeeklyWeeklyClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "周检")))
    __uniRoutes.push(UniPageRoute(path = "pages/monthly/monthly", component = GenPagesMonthlyMonthlyClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "月检")))
    __uniRoutes.push(UniPageRoute(path = "pages/profile/profile", component = GenPagesProfileProfileClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "我的")))
    __uniRoutes.push(UniPageRoute(path = "pages/index/index", component = GenPagesIndexIndexClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "首页")))
    __uniRoutes.push(UniPageRoute(path = "pages/login/login", component = GenPagesLoginLoginClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "登录", "navigationStyle" to "custom")))
    __uniRoutes.push(UniPageRoute(path = "pages/record/record", component = GenPagesRecordRecordClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "记录")))
    __uniRoutes.push(UniPageRoute(path = "pages/record/add", component = GenPagesRecordAddClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "新增记录")))
    __uniRoutes.push(UniPageRoute(path = "pages/record/detail", component = GenPagesRecordDetailClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "记录详情")))
    __uniRoutes.push(UniPageRoute(path = "pages/record/review", component = GenPagesRecordReviewClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "复查记录")))
    __uniRoutes.push(UniPageRoute(path = "pages/statistics/statistics", component = GenPagesStatisticsStatisticsClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "统计")))
    __uniRoutes.push(UniPageRoute(path = "pages/chart-test/chart-test", component = GenPagesChartTestChartTestClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "图表测试")))
}
val __uniTabBar: Map<String, Any?>? = _uM("color" to "#7A7E83", "selectedColor" to "#3cc51f", "borderStyle" to "black", "backgroundColor" to "#ffffff", "list" to _uA(
    _uM("pagePath" to "pages/home/home", "iconPath" to "static/home.png", "selectedIconPath" to "static/home-active.png", "text" to "首页"),
    _uM("pagePath" to "pages/weekly/weekly", "iconPath" to "static/category.png", "selectedIconPath" to "static/category-active.png", "text" to "周检"),
    _uM("pagePath" to "pages/monthly/monthly", "iconPath" to "static/date.png", "selectedIconPath" to "static/date-active.png", "text" to "月检"),
    _uM("pagePath" to "pages/profile/profile", "iconPath" to "static/profile.png", "selectedIconPath" to "static/profile-active.png", "text" to "我的")
))
val __uniLaunchPage: Map<String, Any?> = _uM("url" to "pages/home/home", "style" to _uM("navigationBarTitleText" to "首页", "enablePullDownRefresh" to true, "disableScroll" to false))
fun defineAppConfig() {
    __uniConfig.entryPagePath = "/pages/home/home"
    __uniConfig.globalStyle = _uM("navigationBarTextStyle" to "white", "navigationBarTitleText" to "石油工业园区设备巡检系统", "navigationBarBackgroundColor" to "#007AFF", "backgroundColor" to "#F5F5F5")
    __uniConfig.getTabBarConfig = fun(): Map<String, Any>? {
        return _uM("color" to "#7A7E83", "selectedColor" to "#3cc51f", "borderStyle" to "black", "backgroundColor" to "#ffffff", "list" to _uA(
            _uM("pagePath" to "pages/home/home", "iconPath" to "static/home.png", "selectedIconPath" to "static/home-active.png", "text" to "首页"),
            _uM("pagePath" to "pages/weekly/weekly", "iconPath" to "static/category.png", "selectedIconPath" to "static/category-active.png", "text" to "周检"),
            _uM("pagePath" to "pages/monthly/monthly", "iconPath" to "static/date.png", "selectedIconPath" to "static/date-active.png", "text" to "月检"),
            _uM("pagePath" to "pages/profile/profile", "iconPath" to "static/profile.png", "selectedIconPath" to "static/profile-active.png", "text" to "我的")
        ))
    }
    __uniConfig.tabBar = __uniConfig.getTabBarConfig()
    __uniConfig.conditionUrl = ""
    __uniConfig.uniIdRouter = _uM()
    __uniConfig.ready = true
}
open class GenUniApp : UniAppImpl() {
    open val vm: GenApp?
        get() {
            return getAppVm() as GenApp?
        }
    open val `$vm`: GenApp?
        get() {
            return getAppVm() as GenApp?
        }
}
fun getApp(): GenUniApp {
    return getUniApp() as GenUniApp
}
