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
import io.dcloud.uniapp.extapi.connectSocket as uni_connectSocket
import io.dcloud.uniapp.extapi.exit as uni_exit
import io.dcloud.uniapp.extapi.getWindowInfo as uni_getWindowInfo
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
    val hosts: String = "192.168.93.1,192.168.229.1,192.168.76.128,127.0.0.1"
    val port: String = "8090"
    val id: String = "app-android_QmzJMs"
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
fun TuiGuid(len: Number): String {
    var result = ""
    var characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    var charactersLength = characters.length
    run {
        var i: Number = 0
        while(i < len){
            result += characters.charAt(Math.floor(Math.random() * charactersLength))
            i++
        }
    }
    return result
}
open class TuiChartConfig : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("TuiChartConfig", "uni_modules/tui-xechars/libs/config.uts", 1, 14)
    }
    open var version: String = "v2.5.0-20230101"
    open var yAxisWidth: Any = 15
    open var xAxisHeight: Number = 22
    open var padding: UTSArray<Number> = _uA(
        10,
        10,
        10,
        10
    )
    open var rotate: Boolean = false
    open var fontSize: Number = 13
    open var fontColor: String = "#666666"
    open var dataPointShape = _uA(
        "circle",
        "circle",
        "circle",
        "circle"
    ) as UTSArray<String>
    open var color = _uA(
        "#1890FF",
        "#91CB74",
        "#FAC858",
        "#EE6666",
        "#73C0DE",
        "#3CA272",
        "#FC8452",
        "#9A60B4",
        "#ea7ccc"
    ) as UTSArray<String>
    open var linearColor = _uA(
        "#0EE2F8",
        "#2BDCA8",
        "#FA7D8D",
        "#EB88E2",
        "#2AE3A0",
        "#0EE2F8",
        "#EB88E2",
        "#6773E3",
        "#F78A85"
    ) as UTSArray<String>
    open var pieChartLinePadding: Number = 15
    open var pieChartTextPadding: Number = 5
    open var titleFontSize: Number = 20
    open var subtitleFontSize: Number = 15
    open var radarLabelTextMargin: Number = 13
    open var _xAxisTextAngle_: Number = 0
    open var _pieTextMaxLength_: Number = 0
    constructor(){}
}
typealias ChartsEvent = () -> Unit
open class TouchPosition (
    @JsonNotNull
    open var x: Number,
    @JsonNotNull
    open var y: Number,
) : UTSObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("TouchPosition", "uni_modules/tui-xechars/libs/Interface.uts", 2, 13)
    }
}
open class ChartScrollOption : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartScrollOption", "uni_modules/tui-xechars/libs/Interface.uts", 6, 14)
    }
    open var currentOffset: Number = 0
    open var startTouchX: Number = 0
    open var distance: Number = 0
    open var lastMoveTime: Number = 0
    open var position: String = ""
    open var moveCount: Number = 0
}
open class ChartAnimationOption (
    @JsonNotNull
    open var canvasContext: CanvasContext,
    @JsonNotNull
    open var timing: String,
    @JsonNotNull
    open var duration: Number,
    open var onProcess: (process: Number) -> Unit,
    open var onAnimationFinish: () -> Unit,
) : UTSObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartAnimationOption", "uni_modules/tui-xechars/libs/Interface.uts", 14, 13)
    }
}
open class TuiChartsEvent : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("TuiChartsEvent", "uni_modules/tui-xechars/libs/chartsEvent.uts", 2, 14)
    }
    private var events = Map<String, UTSArray<ChartsEvent>?>()
    constructor(){}
    open fun trigger(type: String) {
        if (this.events.get(type) != null) {
            val listeners = this.events.get("type") as UTSArray<ChartsEvent>
            listeners.forEach(fun(listener: ChartsEvent){
                try {
                    listener()
                }
                 catch (e: Throwable) {}
            }
            )
        }
    }
}
open class Offset : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("Offset", "uni_modules/tui-xechars/libs/type.uts", 5, 14)
    }
    open var x: Number = 0
    open var y: Number = 0
}
open class ChartCurveControlPoints : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartCurveControlPoints", "uni_modules/tui-xechars/libs/type.uts", 9, 14)
    }
    open var ctrA: Offset = Offset()
    open var ctrB: Offset = Offset()
}
open class ChartRange (
    @JsonNotNull
    open var minRange: Number,
    @JsonNotNull
    open var maxRange: Number,
) : UTSObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartRange", "uni_modules/tui-xechars/libs/type.uts", 13, 13)
    }
}
open class ChartColumnDataPoints (
    @JsonNotNull
    open var yAxisPoints: UTSArray<Number>,
    @JsonNotNull
    open var xAxisPoints: UTSArray<Number>,
    @JsonNotNull
    open var calPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>>,
    @JsonNotNull
    open var eachSpacing: Number,
) : UTSObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartColumnDataPoints", "uni_modules/tui-xechars/libs/type.uts", 17, 13)
    }
}
open class ChartsCandleDataPoints (
    @JsonNotNull
    open var yAxisPoints: UTSArray<Number>,
    @JsonNotNull
    open var xAxisPoints: UTSArray<Number>,
    @JsonNotNull
    open var calPoints: UTSArray<UTSArray<UTSArray<ChartOptionsChartDataCalPoints>>>,
    @JsonNotNull
    open var eachSpacing: Number,
) : UTSObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartsCandleDataPoints", "uni_modules/tui-xechars/libs/type.uts", 23, 13)
    }
}
open class ChartMapBounds : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartMapBounds", "uni_modules/tui-xechars/libs/type.uts", 29, 14)
    }
    open var xMin: Number = 180
    open var xMax: Number = 0
    open var yMin: Number = 90
    open var yMax: Number = 0
}
open class ChartOptionsExtraTooltip : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraTooltip", "uni_modules/tui-xechars/libs/type.uts", 35, 14)
    }
    open var showBox: Boolean = true
    open var showArrow: Boolean = true
    open var showCategory: Boolean = false
    open var borderWidth: Number = 0
    open var borderRadius: Number = 0
    open var borderColor: String = "#000000"
    open var borderOpacity: Number = 0.7
    open var bgColor: String = "#000000"
    open var bgOpacity: Number = 0.7
    open var gridType: String = "solid"
    open var dashLength: Number = 4
    open var gridColor: String = "#CCCCCC"
    open var boxPadding: Number = 3
    open var fontSize: Number = 13
    open var lineHeight: Number = 20
    open var fontColor: String = "#FFFFFF"
    open var legendShow: Boolean = true
    open var legendShape: String = "auto"
    open var splitLine: Boolean = true
    open var horizentalLine: Boolean = false
    open var xAxisLabel: Boolean = false
    open var yAxisLabel: Boolean = false
    open var labelBgColor: String = "#FFFFFF"
    open var labelBgOpacity: Number = 0.7
    open var labelFontColor: String = "#666666"
}
open class ChartOptionsExtraMarkLineData : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraMarkLineData", "uni_modules/tui-xechars/libs/type.uts", 62, 14)
    }
    open var value: Number? = null
    open var labelText: String? = null
    open var lineColor: String = "#DE4A42"
    open var showLabel: Boolean = false
    open var labelAlign: String = "left"
    open var labelOffsetX: Number = 0
    open var labelOffsetY: Number = 0
    open var labelPadding: Number = 6
    open var labelFontSize: Number = 13
    open var labelFontColor: String = "#666666"
    open var labelBgColor: String = "#DFE8FF"
    open var labelBgOpacity: Number = 0.8
    open var yAxisIndex: Number? = null
    open var y: Number? = null
}
open class ChartOptionsExtraMarkLine : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraMarkLine", "uni_modules/tui-xechars/libs/type.uts", 78, 14)
    }
    open var type: String = "solid"
    open var dashLength: Number = 4
    open var data: UTSArray<ChartOptionsExtraMarkLineData> = _uA()
}
open class ChartOptionsExtraColumn : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraColumn", "uni_modules/tui-xechars/libs/type.uts", 83, 14)
    }
    open var type: String = "group"
    open var width: Number = 0
    open var seriesGap: Number = 2
    open var categoryGap: Number = 0
    open var barBorderCircle: Boolean = false
    open var barBorderRadius: UTSArray<Number> = _uA()
    open var linearType: String = "none"
    open var linearOpacity: Number = 1
    open var customColor: UTSArray<String> = _uA()
    open var colorStop: Number = 0
    open var meterBorder: Number = 4
    open var meterFillColor: String = "#FFFFFF"
    open var activeBgColor: String = "#000000"
    open var activeBgOpacity: Number = 0.08
    open var activeWidth: Number = -1
    open var labelPosition: String = "outside"
}
open class ChartOptionsExtraMount : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraMount", "uni_modules/tui-xechars/libs/type.uts", 101, 14)
    }
    open var type: String = "mount"
    open var widthRatio: Number = 1
    open var borderWidth: Number = 1
    open var barBorderCircle: Boolean = false
    open var barBorderRadius: UTSArray<Number> = _uA()
    open var linearType: String = "none"
    open var linearOpacity: Number = 1
    open var customColor: UTSArray<String> = _uA()
    open var colorStop: Number = 0
}
open class ChartOptionsExtraBar : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraBar", "uni_modules/tui-xechars/libs/type.uts", 112, 14)
    }
    open var type: String = "group"
    open var width: Number? = null
    open var seriesGap: Number = 2
    open var categoryGap: Number = 3
    open var barBorderCircle: Boolean = false
    open var barBorderRadius: UTSArray<Number>? = null
    open var linearType: String = "none"
    open var linearOpacity: Number = 1
    open var customColor: UTSArray<String>? = null
    open var colorStop: Number = 0
    open var activeBgColor: String = "#000000"
    open var activeBgOpacity: Number = 0.08
}
open class ChartOptionsExtraLine : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraLine", "uni_modules/tui-xechars/libs/type.uts", 126, 14)
    }
    open var type: String = "straight"
    open var width: Number = 2
    open var activeType: String = "none"
    open var linearType: String = "none"
    open var onShadow: Boolean = false
    open var animation: String = "vertical"
}
open class ChartOptionsExtraArea : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraArea", "uni_modules/tui-xechars/libs/type.uts", 134, 14)
    }
    open var type: String = "straight"
    open var opacity: Number = 0.2
    open var addLine: Boolean = true
    open var width: Number = 2
    open var gradient: Boolean = false
    open var activeType: String = "none"
}
open class ChartOptionsExtraScatter : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraScatter", "uni_modules/tui-xechars/libs/type.uts", 142, 14)
    }
    open var id: String? = null
}
open class ChartOptionsExtraBubble : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraBubble", "uni_modules/tui-xechars/libs/type.uts", 145, 14)
    }
    open var border: Number = 2
    open var opacity: Number = 0.5
}
open class ChartOptionsExtraMixColumn : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraMixColumn", "uni_modules/tui-xechars/libs/type.uts", 149, 14)
    }
    open var width: Number? = null
    open var seriesGap: Number? = null
    open var categoryGap: Number? = null
    open var barBorderCircle: Boolean = false
    open var barBorderRadius: UTSArray<Number>? = null
    open var linearType: String = "none"
    open var linearOpacity: Number = 1
    open var customColor: UTSArray<String>? = null
    open var colorStop: Number? = null
}
open class ChartOptionsExtraMixArea : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraMixArea", "uni_modules/tui-xechars/libs/type.uts", 160, 14)
    }
    open var gradient: Boolean = false
    open var opacity: Number = 0.2
}
open class ChartOptionsExtraMixLine : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraMixLine", "uni_modules/tui-xechars/libs/type.uts", 164, 14)
    }
    open var width: Number = 2
}
open class ChartOptionsExtraMix : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraMix", "uni_modules/tui-xechars/libs/type.uts", 167, 14)
    }
    open var column: ChartOptionsExtraMixColumn? = null
    open var area: ChartOptionsExtraMixArea? = null
    open var line: ChartOptionsExtraMixLine? = null
}
open class ChartOptionsExtraPie : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraPie", "uni_modules/tui-xechars/libs/type.uts", 172, 14)
    }
    open var activeOpacity: Number = 0.5
    open var activeRadius: Number = 10
    open var offsetAngle: Number? = null
    open var customRadius: Number? = null
    open var labelWidth: Number = 15
    open var border: Boolean = true
    open var borderWidth: Number = 2
    open var borderColor: String = "#FFFFFF"
    open var linearType: String = "none"
    open var customColor: UTSArray<String>? = null
}
open class ChartOptionsExtraRing : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraRing", "uni_modules/tui-xechars/libs/type.uts", 184, 14)
    }
    open var ringWidth: Number = 30
    open var centerColor: String = "#FFFFFF"
    open var activeOpacity: Number = 0.5
    open var activeRadius: Number = 10
    open var offsetAngle: Number? = null
    open var customRadius: Number? = null
    open var labelWidth: Number = 15
    open var border: Boolean = true
    open var borderWidth: Number = 2
    open var borderColor: String = "#FFFFFF"
    open var linearType: String = "none"
    open var customColor: UTSArray<String>? = null
}
open class ChartOptionsExtraRose : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraRose", "uni_modules/tui-xechars/libs/type.uts", 198, 14)
    }
    open var type: String = "area"
    open var minRadius: Number? = null
    open var activeOpacity: Number = 0.5
    open var activeRadius: Number = 10
    open var offsetAngle: Number? = null
    open var labelWidth: Number = 15
    open var border: Boolean = true
    open var borderWidth: Number = 2
    open var borderColor: String = "#FFFFFF"
    open var linearType: String = "none"
    open var customColor: UTSArray<String>? = null
}
open class ChartOptionsExtraRadar : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraRadar", "uni_modules/tui-xechars/libs/type.uts", 211, 14)
    }
    open var gridType: String = "radar"
    open var gridColor: String = "#CCCCCC"
    open var gridCount: Number = 3
    open var gridEval: Number = 1
    open var radius: Number? = null
    open var axisLabel: Boolean = false
    open var axisLabelTofix: Number = 0
    open var labelShow: Boolean = true
    open var labelColor: String = "#666666"
    open var labelPointShow: Boolean = false
    open var labelPointRadius: Number = 3
    open var labelPointColor: String = "#CCCCCC"
    open var opacity: Number = 0.2
    open var border: Boolean = false
    open var borderWidth: Number = 2
    open var max: Number? = null
    open var linearType: String = "none"
    open var customColor: UTSArray<String>? = null
}
open class ChartOptionsExtraArcbar : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraArcbar", "uni_modules/tui-xechars/libs/type.uts", 231, 14)
    }
    open var type: String = "default"
    open var direction: String = "cw"
    open var width: Number = 12
    open var lineCap: String = "round"
    open var backgroundColor: String = "#E9E9E9"
    open var startAngle: Number = 0.75
    open var endAngle: Number = 0.25
    open var radius: Number? = null
    open var gap: Number = 2
    open var centerX: Number? = null
    open var centerY: Number? = null
    open var linearType: String = "none"
    open var customColor: UTSArray<String>? = null
}
open class ChartOptionsExtraGaugeSplitLine : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraGaugeSplitLine", "uni_modules/tui-xechars/libs/type.uts", 246, 14)
    }
    open var fixRadius: Number? = null
    open var splitNumber: Number = 10
    open var width: Number = 15
    open var color: String = "#FFFFFF"
    open var childNumber: Number = 5
    open var childWidth: Number = 5
}
open class ChartOptionsExtraGaugePointer : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraGaugePointer", "uni_modules/tui-xechars/libs/type.uts", 254, 14)
    }
    open var width: Number = 15
    open var color: String = "auto"
}
open class ChartOptionsExtraGauge : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraGauge", "uni_modules/tui-xechars/libs/type.uts", 258, 14)
    }
    open var type: String = "default"
    open var width: Number = 15
    open var labelColor: String = "#666666"
    open var labelOffset: Number = 13
    open var startAngle: Number = 0.75
    open var endAngle: Number = 0.25
    open var startNumber: Number? = null
    open var endNumber: Number = 100
    open var formatter: Any? = null
    open var format: String? = null
    open var splitLine: ChartOptionsExtraGaugeSplitLine? = null
    open var pointer: ChartOptionsExtraGaugePointer? = null
    open var oldAngle: Number? = null
    open var oldData: Number? = null
}
open class ChartOptionsExtraFunnel : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraFunnel", "uni_modules/tui-xechars/libs/type.uts", 274, 14)
    }
    open var type: String = "funnel"
    open var activeOpacity: Number = 0.3
    open var activeWidth: Number = 10
    open var border: Boolean = true
    open var borderWidth: Number = 2
    open var borderColor: String = "#FFFFFF"
    open var fillOpacity: Number = 1
    open var minSize: Number = 0
    open var labelAlign: String = "right"
    open var linearType: String = "none"
    open var customColor: UTSArray<String>? = null
}
open class ChartOptionsExtraWord : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraWord", "uni_modules/tui-xechars/libs/type.uts", 287, 14)
    }
    open var type: String = "normal"
    open var autoColors: Boolean = true
}
open class ChartOptionsExtraCandleColor : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraCandleColor", "uni_modules/tui-xechars/libs/type.uts", 291, 14)
    }
    open var upLine: String = "#f04864"
    open var upFill: String = "#f04864"
    open var downLine: String = "#2fc25b"
    open var downFill: String = "#2fc25b"
}
open class ChartOptionsExtraCandleAverage : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraCandleAverage", "uni_modules/tui-xechars/libs/type.uts", 297, 14)
    }
    open var show: Boolean = true
    open var name: UTSArray<String>? = null
    open var day: UTSArray<Number>? = null
    open var color: UTSArray<String>? = null
}
open class ChartOptionsExtraCandle : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraCandle", "uni_modules/tui-xechars/libs/type.uts", 303, 14)
    }
    open var color: ChartOptionsExtraCandleColor = ChartOptionsExtraCandleColor()
    open var average: ChartOptionsExtraCandleAverage = ChartOptionsExtraCandleAverage()
}
open class ChartOptionsExtraMap : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtraMap", "uni_modules/tui-xechars/libs/type.uts", 307, 14)
    }
    open var border: Boolean = true
    open var mercator: Boolean = false
    open var borderWidth: Number = 2
    open var borderColor: String = "#666666"
    open var fillOpacity: Number = 0.6
    open var active: Boolean = true
    open var activeTextColor: String = "#FFFFFF"
    open var activeBorderColor: String = "#F04864"
    open var activeFillColor: String = "#FACC14"
    open var activeFillOpacity: Number = 1
}
open class ChartOptionsExtra : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsExtra", "uni_modules/tui-xechars/libs/type.uts", 319, 14)
    }
    open var tooltip: ChartOptionsExtraTooltip? = null
    open var markLine: ChartOptionsExtraMarkLine? = null
    open var column: ChartOptionsExtraColumn? = null
    open var mount: ChartOptionsExtraMount? = null
    open var bar: ChartOptionsExtraBar? = null
    open var line: ChartOptionsExtraLine? = null
    open var area: ChartOptionsExtraArea? = null
    open var scatter: ChartOptionsExtraScatter? = null
    open var bubble: ChartOptionsExtraBubble? = null
    open var mix: ChartOptionsExtraMix? = null
    open var pie: ChartOptionsExtraPie? = null
    open var ring: ChartOptionsExtraRing? = null
    open var rose: ChartOptionsExtraRose? = null
    open var radar: ChartOptionsExtraRadar? = null
    open var arcbar: ChartOptionsExtraArcbar? = null
    open var gauge: ChartOptionsExtraGauge? = null
    open var funnel: ChartOptionsExtraFunnel? = null
    open var word: ChartOptionsExtraWord? = null
    open var candle: ChartOptionsExtraCandle? = null
    open var map: ChartOptionsExtraMap? = null
}
open class ChartOptionsTitle : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsTitle", "uni_modules/tui-xechars/libs/type.uts", 341, 14)
    }
    open var name: String? = null
    open var fontSize: Number = 18
    open var color: String = "#666666"
    open var offsetX: Number? = null
    open var offsetY: Number? = null
}
open class ChartOptionsSubTitle : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsSubTitle", "uni_modules/tui-xechars/libs/type.uts", 348, 14)
    }
    open var name: String? = null
    open var fontSize: Number = 12
    open var color: String = "#7cb5ec"
    open var offsetX: Number? = null
    open var offsetY: Number? = null
}
open class ChartOptionsLegend : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsLegend", "uni_modules/tui-xechars/libs/type.uts", 355, 14)
    }
    open var show: Boolean = true
    open var position: String = "bottom"
    open var float: String = "center"
    open var padding: Number = 5
    open var margin: Number = 5
    open var backgroundColor: String = "rgba(0,0,0,0)"
    open var borderColor: String = "rgba(0,0,0,0)"
    open var borderWidth: Number? = 0
    open var fontSize: Number = 13
    open var fontColor: String = "#666666"
    open var lineHeight: Number = 11
    open var hiddenColor: String = "#CECECE"
    open var itemGap: Number = 10
}
open class ChartOptionsYAxisData : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsYAxisData", "uni_modules/tui-xechars/libs/type.uts", 370, 14)
    }
    open var type: String? = null
    open var position: String = "left"
    open var disabled: Boolean = false
    open var axisLine: Boolean = true
    open var axisLineColor: String = "#CCCCCC"
    open var calibration: Boolean = false
    open var fontColor: String = "#666666"
    open var fontSize: Number? = null
    open var textAlign: String = "right"
    open var title: String? = null
    open var titleFontSize: Number? = null
    open var titleOffsetY: Number? = null
    open var titleOffsetX: Number? = null
    open var titleFontColor: String = "#666666"
    open var min: Number? = null
    open var max: Number? = null
    open var tofix: Number? = null
    open var unit: String? = null
    open var formatter: Any? = null
    open var format: String? = null
    open var categories: UTSArray<String>? = null
}
open class ChartOptionsYAxis : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsYAxis", "uni_modules/tui-xechars/libs/type.uts", 393, 14)
    }
    open var disabled: Boolean = false
    open var disableGrid: Boolean = false
    open var splitNumber: Number = 5
    open var gridType: String = "solid"
    open var dashLength: Number = 8
    open var gridColor: String = "#CCCCCC"
    open var padding: Number = 10
    open var showTitle: Boolean = false
    open var data: UTSArray<ChartOptionsYAxisData> = _uA()
    open var gridSet: String = "number"
    open var fontColor: String = "#666666"
    open var tofix: Number = 0
    open var unit: String = ""
    open var fontSize: Number? = null
    open var formatter: Any? = null
    open var min: Number? = null
    open var max: Number? = null
}
open class ChartOptionsXAxis : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsXAxis", "uni_modules/tui-xechars/libs/type.uts", 412, 14)
    }
    open var disabled: Boolean = false
    open var axisLine: Boolean = true
    open var axisLineColor: String = "#CCCCCC"
    open var calibration: Boolean = false
    open var fontColor: String = "#666666"
    open var fontSize: Number = 13
    open var lineHeight: Number = 20
    open var marginTop: Number = 0
    open var rotateLabel: Boolean = false
    open var rotateAngle: Number = 45
    open var labelCount: Number? = null
    open var itemCount: Number = 5
    open var boundaryGap: String = "center"
    open var disableGrid: Boolean = true
    open var splitNumber: Number? = 5
    open var gridColor: String = "#CCCCCC"
    open var gridType: String = "solid"
    open var dashLength: Number = 4
    open var gridEval: Number = 1
    open var scrollShow: Boolean = false
    open var scrollAlign: String = "left"
    open var scrollColor: String = "#A6A6A6"
    open var scrollBackgroundColor: String = "#EFEBEF"
    open var min: Number? = null
    open var max: Number? = null
    open var title: String? = null
    open var titleFontSize: Number = 13
    open var titleOffsetY: Number = 0
    open var titleOffsetX: Number = 0
    open var titleFontColor: String = "#666666"
    open var formatter: Any? = null
    open var format: String? = null
    open var scrollPosition: String? = null
    open var data: UTSArray<ChartOptionsYAxisData> = _uA()
}
open class ChartOptionsSeriesData : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsSeriesData", "uni_modules/tui-xechars/libs/type.uts", 448, 14)
    }
    open var value: Number? = null
    open var color: String? = null
    open var name: String? = null
    open var data: Number? = null
    open var labelText: String? = null
    open var labelShow: Boolean = true
    open var centerText: String? = null
    open var centerTextSize: Number? = null
    open var centerTextColor: String = "#FFFFFF"
    open var formatter: Any? = null
    open var position: ChartOptionsChartDataCalPoints = ChartOptionsChartDataCalPoints()
    open var angle: Number = 0
    open var proportion: Number = 0
    open var textColor: String = "#666666"
    open var textSize: Number? = null
    open var _startAngle_: Number = 0
    open var _endAngle_: Number = 0
}
open class ChartOptionsMapSeriesPropertiesParent : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsMapSeriesPropertiesParent", "uni_modules/tui-xechars/libs/type.uts", 468, 14)
    }
    open var adcode: Number = 0
}
open class ChartOptionsMapSeriesProperties : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsMapSeriesProperties", "uni_modules/tui-xechars/libs/type.uts", 471, 14)
    }
    open var adcode: Number = 0
    open var name: String = ""
    open var center: UTSArray<Number> = _uA()
    open var centroid: UTSArray<Number> = _uA()
    open var childrenNum: Number = 0
    open var level: String = ""
    open var subFeatureIndex: Number = 0
    open var acroutes: UTSArray<Number> = _uA()
    open var parent: ChartOptionsMapSeriesPropertiesParent = ChartOptionsMapSeriesPropertiesParent()
}
open class ChartOptionsMapSeriesGeometry : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsMapSeriesGeometry", "uni_modules/tui-xechars/libs/type.uts", 482, 14)
    }
    open var type: String = ""
    open var coordinates: UTSArray<UTSArray<UTSArray<UTSArray<Number>>>> = _uA()
}
open class ChartOptionsSeries : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsSeries", "uni_modules/tui-xechars/libs/type.uts", 486, 14)
    }
    open var _data: UTSArray<Number>? = null
    open var zeroPoints: Number? = null
    open var area: UTSArray<Number>? = null
    open var index: Number? = null
    open var name: String? = null
    open var legendText: String? = null
    open var data: UTSArray<ChartOptionsSeriesData>? = null
    open var dataArr: UTSArray<UTSArray<Number>> = _uA()
    open var show: Boolean = true
    open var color: String? = null
    open var fillOpacity: Number = 0.8
    open var textColor: String = "#666666"
    open var textSize: Number? = null
    open var textOffset: Number? = null
    open var linearIndex: Number? = null
    open var type: String? = null
    open var disableLegend: Boolean = false
    open var connectNulls: Boolean = false
    open var setShadow: UTSArray<String>? = null
    open var linearColor: UTSArray<UTSArray<String>>? = null
    open var style: String = "straight"
    open var addPoint: Boolean = false
    open var lineType: String = "solid"
    open var dashLength: Number = 8
    open var pointShape: String = "circle"
    open var legendShape: String? = null
    open var formatter: Any? = null
    open var format: String? = null
    open var rotate: Boolean = false
    open var seriesMA: UTSArray<ChartOptionsSeries>? = null
    open var _radius_: Number = 0
    open var _start_: Number = 0
    open var _proportion_: Number = 0
    open var _rose_proportion_: Number = 0
    open var areav: UTSArray<Number> = _uA()
    open var radius: Number = 0
    open var funnelArea: UTSArray<Number> = _uA()
    open var labelShow: Boolean = true
    open var labelText: String? = null
    open var centerTextColor: String = "#FFFFFF"
    open var arc: Number = 0
    open var text: String = ""
    open var properties: ChartOptionsMapSeriesProperties = ChartOptionsMapSeriesProperties()
    open var geometry: ChartOptionsMapSeriesGeometry = ChartOptionsMapSeriesGeometry()
    open var _endAngle_: Number = 0
    open var _oldAngle_: Number = 0
}
open class ChartOptionsChartDataLegendDataAreaXY : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsChartDataLegendDataAreaXY", "uni_modules/tui-xechars/libs/type.uts", 534, 14)
    }
    open var x: Number = 0
    open var y: Number = 0
}
open class ChartOptionsChartDataLegendDataArea : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsChartDataLegendDataArea", "uni_modules/tui-xechars/libs/type.uts", 538, 14)
    }
    open var start: ChartOptionsChartDataLegendDataAreaXY = ChartOptionsChartDataLegendDataAreaXY()
    open var end: ChartOptionsChartDataLegendDataAreaXY = ChartOptionsChartDataLegendDataAreaXY()
    open var lineStart: ChartOptionsChartDataLegendDataAreaXY = ChartOptionsChartDataLegendDataAreaXY()
    open var lineEnd: ChartOptionsChartDataLegendDataAreaXY = ChartOptionsChartDataLegendDataAreaXY()
    open var width: Number = 0
    open var height: Number = 0
    open var wholeWidth: Number = 0
    open var wholeHeight: Number = 0
    open var text: String = ""
    open var color: String = ""
    open var textColor: String = ""
    open var textSize: Number = 13
}
open class ChartOptionsChartDataLegendData : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsChartDataLegendData", "uni_modules/tui-xechars/libs/type.uts", 552, 14)
    }
    open var area: ChartOptionsChartDataLegendDataArea = ChartOptionsChartDataLegendDataArea()
    open var points: UTSArray<UTSArray<ChartOptionsSeries>> = _uA()
    open var widthArr: UTSArray<Number> = _uA()
    open var heightArr: UTSArray<Number> = _uA()
}
open class ChartOptionsChartDataYAxisDataYAxisWidth : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsChartDataYAxisDataYAxisWidth", "uni_modules/tui-xechars/libs/type.uts", 558, 14)
    }
    open var position: String = "left"
    open var width: Number = 0
}
open class ChartOptionsChartDataYAxisData : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsChartDataYAxisData", "uni_modules/tui-xechars/libs/type.uts", 562, 14)
    }
    open var rangesFormat: UTSArray<UTSArray<String>>? = null
    open var ranges: UTSArray<UTSArray<String>>? = null
    open var yAxisWidth: UTSArray<ChartOptionsChartDataYAxisDataYAxisWidth>? = null
}
open class ChartOptionsChartDataXAxisData : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsChartDataXAxisData", "uni_modules/tui-xechars/libs/type.uts", 567, 14)
    }
    open var xAxisPoints: UTSArray<Number> = _uA()
    open var startX: Number? = null
    open var endX: Number? = null
    open var eachSpacing: Number? = null
    open var ranges: UTSArray<Number>? = null
    open var angle: Number = 0
    open var xAxisHeight: Number = 0
    open var rangesFormat: UTSArray<Number> = _uA()
}
open class ChartOptionsChartDataCategoriesData : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsChartDataCategoriesData", "uni_modules/tui-xechars/libs/type.uts", 577, 14)
    }
    open var angle: Number = 0
    open var xAxisHeight: Number = 0
}
open class ChartOptionsChartDataCalPoints : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsChartDataCalPoints", "uni_modules/tui-xechars/libs/type.uts", 581, 14)
    }
    open var color: String? = null
    open var x: Number = 0
    open var y: Number = 0
    open var y0: Number = 0
    open var x0: Number = 0
    open var r: Number = 0
    open var t: Number = 0
    open var width: Number? = 0
    open var height: Number? = 0
    open var value: Number = 0
}
open class ChartOptionsChartDataToolTiPData : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsChartDataToolTiPData", "uni_modules/tui-xechars/libs/type.uts", 593, 14)
    }
    open var center: Offset = Offset()
    open var radius: Number = 0
    open var series: UTSArray<ChartOptionsSeries> = _uA()
    open var angleList: UTSArray<Number> = _uA()
    open var innerRadius: Number = 0
    open var categories: UTSArray<ChartOptionsSeriesData> = _uA()
    open var totalAngle: Number = 0
}
open class ChartOptionsChartDataMapData : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsChartDataMapData", "uni_modules/tui-xechars/libs/type.uts", 602, 14)
    }
    open var bounds: ChartMapBounds = ChartMapBounds()
    open var scale: Number = 0
    open var xoffset: Number = 0
    open var yoffset: Number = 0
    open var mercator: Boolean = false
}
open class ChartOptionsChartData : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsChartData", "uni_modules/tui-xechars/libs/type.uts", 609, 14)
    }
    open var legendData: ChartOptionsChartDataLegendData = ChartOptionsChartDataLegendData()
    open var yAxisData: ChartOptionsChartDataYAxisData = ChartOptionsChartDataYAxisData()
    open var xAxisData: ChartOptionsChartDataXAxisData = ChartOptionsChartDataXAxisData()
    open var categoriesData: ChartOptionsChartDataCategoriesData = ChartOptionsChartDataCategoriesData()
    open var xAxisPoints: UTSArray<Number>? = null
    open var yAxisPoints: UTSArray<Number>? = null
    open var calPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>>? = null
    open var calMapPoints: UTSArray<UTSArray<UTSArray<ChartOptionsChartDataCalPoints>>>? = null
    open var eachSpacing: Number = 0
    open var pieData: ChartOptionsChartDataToolTiPData? = null
    open var radarData: ChartOptionsChartDataToolTiPData? = null
    open var funnelData: ChartOptionsChartDataToolTiPData? = null
    open var arcbarData: ChartOptionsChartDataToolTiPData? = null
    open var gaugeData: ChartOptionsChartDataToolTiPData? = null
    open var wordCloudData: UTSArray<ChartOptionsSeries>? = null
    open var mapData: ChartOptionsChartDataMapData? = null
}
open class TextList : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("TextList", "uni_modules/tui-xechars/libs/type.uts", 627, 14)
    }
    open var text: String = ""
    open var color: String = ""
    open var legendShape: String = ""
    open var disableLegend: Boolean = false
}
open class ChartOptionsTooltipOption : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsTooltipOption", "uni_modules/tui-xechars/libs/type.uts", 633, 14)
    }
    open var index: Number? = null
    open var offset: Offset? = null
    open var textList: UTSArray<TextList>? = null
    open var formatter: Any? = null
}
fun getToolTipData(seriesData: UTSArray<ChartOptionsSeries>, opts: ChartOptions, index: Any, group: UTSArray<Number>, categories: UTSArray<String>, option: ChartOptionsTooltipOption): ChartOptionsTooltipOption {
    var calPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = if (opts.chartData.calPoints != null) {
        opts.chartData.calPoints!!
    } else {
        _uA()
    }
    var points: ChartOptionsChartDataCalPoints = ChartOptionsChartDataCalPoints()
    if (group.length > 0) {
        var filterPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = _uA()
        run {
            var i: Number = 0
            while(i < group.length){
                filterPoints.push(calPoints[group[i]])
                i++
            }
        }
        points = filterPoints[0][(index as UTSArray<Number>)[0]]
    } else {
        run {
            var i: Number = 0
            while(i < calPoints.length){
                val ix = index as Number
                val posArr: UTSArray<ChartOptionsChartDataCalPoints> = calPoints[i]
                if (posArr.length > ix) {
                    points = posArr[ix]
                    break
                }
                i++
            }
        }
    }
    var textList: UTSArray<TextList> = seriesData.map(fun(item: ChartOptionsSeries): TextList {
        var titleText = ""
        if (opts.categories != null && opts.categories!!.length > 0) {
            titleText = categories[index as Number]
        }
        val textitem = TextList()
        if (option.formatter != null) {
            val kFun = option.formatter as (item: ChartOptionsSeries, titleText: String, index: Any, opts: ChartOptions) -> String
            textitem.text = kFun(item, titleText, index, opts)
        } else {
            textitem.text = titleText + " " + item.name + ": " + ("" + item.data!![0].value)
        }
        textitem.color = item.color!!
        textitem.legendShape = if (opts.extra.tooltip!!.legendShape == "auto") {
            item.legendShape!!
        } else {
            opts.extra.tooltip!!.legendShape
        }
        return textitem
    }
    )
    val offset = Offset()
    offset.x = Math.round(points.x)
    offset.y = Math.round(points.y)
    val tooltipOption = ChartOptionsTooltipOption()
    tooltipOption.textList = textList
    tooltipOption.offset = offset
    return tooltipOption
}
open class ChartOptionsTooltip : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptionsTooltip", "uni_modules/tui-xechars/libs/type.uts", 639, 14)
    }
    open var textList: UTSArray<TextList> = _uA()
    open var offset: Offset = Offset()
    open var option: ChartOptionsTooltipOption = ChartOptionsTooltipOption()
    open var index: Any? = null
    open var group: UTSArray<Number> = _uA()
}
open class ChartOptions : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartOptions", "uni_modules/tui-xechars/libs/type.uts", 646, 14)
    }
    open var type: String = "column"
    open var context: CanvasRenderingContext2D? = null
    open var canvasContext: CanvasContext? = null
    open var width: Number = 320
    open var height: Number = 200
    open var categories: UTSArray<String>? = null
    open var categoriesObj: UTSArray<ChartOptionsSeriesData>? = null
    open var series: UTSArray<ChartOptionsSeries>? = null
    open var _series_: UTSArray<ChartOptionsSeries>? = null
    open var area: UTSArray<Number> = _uA()
    open var xAxis: ChartOptionsXAxis = ChartOptionsXAxis()
    open var yAxis: ChartOptionsYAxis = ChartOptionsYAxis()
    open var legend: ChartOptionsLegend = ChartOptionsLegend()
    open var title: ChartOptionsTitle = ChartOptionsTitle()
    open var subtitle: ChartOptionsSubTitle = ChartOptionsSubTitle()
    open var extra: ChartOptionsExtra = ChartOptionsExtra()
    open var chartData: ChartOptionsChartData = ChartOptionsChartData()
    open var tooltip: ChartOptionsTooltip? = null
    open var showTooltip: Boolean = false
    open var canvas2d: Boolean = true
    open var pixelRatio: Number = 1
    open var animation: Boolean = false
    open var timing: String = "easeOut"
    open var duration: Number = 1000
    open var rotate: Boolean = false
    open var rotateLock: Boolean = false
    open var background: String = "rgba(255,255,255,1)"
    open var color: UTSArray<String>? = null
    open var padding: UTSArray<Number>? = null
    open var fontSize: Number = 13
    open var fontColor: String = "#666666"
    open var dataLabel: Boolean = true
    open var dataPointShape: Boolean = true
    open var dataPointShapeType: String = "solid"
    open var touchMoveLimit: Number = 30
    open var enableScroll: Boolean = false
    open var enableMarkLine: Boolean = false
    open var scrollPosition: String = "current"
    open var _scrollDistance_: Number? = null
    open var _rotate_: Boolean = false
    open var updateData: Boolean = false
    open var seriesMA: UTSArray<ChartOptionsSeries>? = null
    open var yOffset: Number = 0
    open var xOffset: Number = 0
    open var DOMRect: DOMRect? = null
    open var tapLegend: Boolean = true
}
interface TuiChartsInterface {
    var opts: ChartOptions
    var context: CanvasRenderingContext2D
    var uevent: TuiChartsEvent
    var config: TuiChartConfig
    var scrollOption: ChartScrollOption
    var animationInstance: Animation?
    var offsetWidth: Number
    var offsetHeight: Number
    fun update(data: UTSJSONObject)
    fun zoom(e: ChartOptionsXAxis?)
    fun dobuleZoom(e: UniTouchEvent)
    fun getLegendDataIndex(e: UniEvent): Number
    fun touchLegend(e: UniEvent): Number
    fun scrollStart(e: UniEvent)
    fun scroll(e: UniEvent)
    fun scrollEnd(e: UniEvent)
    fun showToolTip(vararg args: Any): Number
    fun getCurrentDataIndex(e: UniEvent): ChartOptionsTooltip
}
fun filterSeries(series: UTSArray<ChartOptionsSeries>): UTSArray<ChartOptionsSeries> {
    var tempSeries: UTSArray<ChartOptionsSeries> = _uA()
    run {
        var i: Number = 0
        while(i < series.length){
            if (series[i].show == true) {
                tempSeries.push(series[i])
            }
            i++
        }
    }
    return tempSeries
}
fun getSeriesDataItem(series: UTSArray<ChartOptionsSeries>, index: Any, group: UTSArray<Number>): UTSArray<ChartOptionsSeries> {
    var data: UTSArray<ChartOptionsSeries> = _uA()
    var newSeries: UTSArray<ChartOptionsSeries> = _uA()
    var indexIsArr = UTSArray.isArray(index)
    if (indexIsArr) {
        var tempSeries = filterSeries(series)
        run {
            var i: Number = 0
            while(i < group.length){
                newSeries.push(tempSeries[group[i]])
                i++
            }
        }
    } else {
        newSeries = series
    }
    run {
        var i: Number = 0
        while(i < newSeries.length){
            var item: ChartOptionsSeries = newSeries[i]
            var tmpindex: Number
            if (indexIsArr) {
                tmpindex = (index as UTSArray<Number>)[i]
            } else {
                tmpindex = index as Number
            }
            if (item.data != null && item.data!!.length > 0 && item.show) {
                var seriesItem = ChartOptionsSeries()
                seriesItem.color = item.color
                seriesItem.type = item.type
                seriesItem.style = item.style
                seriesItem.pointShape = item.pointShape
                seriesItem.disableLegend = item.disableLegend
                seriesItem.legendShape = item.legendShape
                seriesItem.name = item.name
                seriesItem.show = item.show
                if (item.formatter != null) {
                    val kFun = item.formatter as (e: ChartOptionsSeriesData) -> UTSArray<ChartOptionsSeriesData>
                    seriesItem.data = kFun(item.data!![tmpindex])
                } else {
                    seriesItem.data = _uA(
                        item.data!![tmpindex]
                    )
                }
                data.push(seriesItem)
            }
            if (item.dataArr.length > 0 && item.show) {
                var seriesItem = ChartOptionsSeries()
                seriesItem.color = item.color
                seriesItem.type = item.type
                seriesItem.style = item.style
                seriesItem.pointShape = item.pointShape
                seriesItem.disableLegend = item.disableLegend
                seriesItem.legendShape = item.legendShape
                seriesItem.name = item.name
                seriesItem.show = item.show
                if (item.formatter != null) {
                    val kFun = item.formatter as (e: UTSArray<Number>) -> UTSArray<UTSArray<Number>>
                    seriesItem.dataArr = kFun(item.dataArr[tmpindex])
                } else {
                    seriesItem.dataArr = _uA(
                        item.dataArr[tmpindex]
                    )
                }
                data.push(seriesItem)
            }
            i++
        }
    }
    return data
}
fun isInExactChartArea(currentPoints: TouchPosition, opts: ChartOptions): Boolean {
    return currentPoints.x <= opts.width - opts.area[1] + 10 && currentPoints.x >= opts.area[3] - 10 && currentPoints.y >= opts.area[0] && currentPoints.y <= opts.height - opts.area[2]
}
fun findCandleChartCurrentIndex(currentPoints: TouchPosition, calPoints: UTSArray<UTSArray<UTSArray<ChartOptionsChartDataCalPoints>>>, opts: ChartOptions, offset: Number = 0): ChartOptionsTooltip {
    var current: ChartOptionsTooltip = ChartOptionsTooltip()
    var spacing: Number = opts.chartData.eachSpacing / 2
    var xAxisPoints: UTSArray<Number> = _uA()
    if (calPoints.length > 0) {
        if (opts.categories == null) {
            spacing = 0
        } else {
            run {
                var i: Number = 1
                while(i < opts.chartData.xAxisPoints!!.length){
                    xAxisPoints.push(opts.chartData.xAxisPoints!![i] - spacing)
                    i++
                }
            }
        }
        if (isInExactChartArea(currentPoints, opts)) {
            if (opts.categories == null) {
                var timePoints: UTSArray<UTSArray<Number>> = _uA()
                run {
                    var i: Number = 0
                    while(i < calPoints.length){
                        val timePointsChild: UTSArray<Number> = _uA()
                        run {
                            var j: Number = 0
                            while(j < calPoints[i].length){
                                j++
                            }
                        }
                        timePoints.push(timePointsChild)
                        i++
                    }
                }
                var pointValue: UTSArray<Number> = _uA()
                var pointIndex: UTSArray<Number> = _uA()
                run {
                    var i: Number = 0
                    while(i < timePoints.length){
                        pointValue[i] = Math.min(*timePoints[i].toTypedArray())
                        pointIndex[i] = timePoints[i].indexOf(pointValue[i])
                        i++
                    }
                }
                var minValue = Math.min(*pointValue.toTypedArray())
                val currentindex: UTSArray<Number> = _uA()
                run {
                    var i: Number = 0
                    while(i < pointValue.length){
                        if (pointValue[i] == minValue) {
                            current.group.push(i)
                            currentindex.push(pointIndex[i])
                        }
                        i++
                    }
                }
                current.index = currentindex
            } else {
                xAxisPoints.forEach(fun(item, index) {
                    if (currentPoints.x + offset + spacing > item) {
                        current.index = index
                    }
                }
                )
            }
        }
    }
    return current
}
fun findCurrentIndex(currentPoints: TouchPosition, calPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>>, opts: ChartOptions, offset: Number = 0): ChartOptionsTooltip {
    var current: ChartOptionsTooltip = ChartOptionsTooltip()
    var spacing: Number = opts.chartData.eachSpacing / 2
    var xAxisPoints: UTSArray<Number> = _uA()
    if (calPoints.length > 0) {
        if (opts.categories == null) {
            spacing = 0
        } else {
            run {
                var i: Number = 1
                while(i < opts.chartData.xAxisPoints!!.length){
                    xAxisPoints.push(opts.chartData.xAxisPoints!![i] - spacing)
                    i++
                }
            }
            if ((opts.type == "line" || opts.type == "area") && opts.xAxis.boundaryGap == "justify") {
                xAxisPoints = opts.chartData.xAxisPoints!!
            }
        }
        if (isInExactChartArea(currentPoints, opts)) {
            if (opts.categories == null) {
                var timePoints: UTSArray<UTSArray<Number>> = _uA()
                run {
                    var i: Number = 0
                    while(i < calPoints.length){
                        val timePointsChild: UTSArray<Number> = _uA()
                        run {
                            var j: Number = 0
                            while(j < calPoints[i].length){
                                timePointsChild.push(Math.abs(calPoints[i][j].x - currentPoints.x))
                                j++
                            }
                        }
                        timePoints.push(timePointsChild)
                        i++
                    }
                }
                var pointValue: UTSArray<Number> = _uA()
                var pointIndex: UTSArray<Number> = _uA()
                run {
                    var i: Number = 0
                    while(i < timePoints.length){
                        pointValue[i] = Math.min(*timePoints[i].toTypedArray())
                        pointIndex[i] = timePoints[i].indexOf(pointValue[i])
                        i++
                    }
                }
                var minValue = Math.min(*pointValue.toTypedArray())
                val currentindex: UTSArray<Number> = _uA()
                run {
                    var i: Number = 0
                    while(i < pointValue.length){
                        if (pointValue[i] == minValue) {
                            current.group.push(i)
                            currentindex.push(pointIndex[i])
                        }
                        i++
                    }
                }
                current.index = currentindex
            } else {
                xAxisPoints.forEach(fun(item, index) {
                    if (currentPoints.x + offset + spacing > item) {
                        current.index = index
                    }
                }
                )
            }
        }
    }
    return current
}
fun findBarChartCurrentIndex(currentPoints: TouchPosition, calPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>>?, opts: ChartOptions, offset: Number = 0): ChartOptionsTooltip {
    var current: ChartOptionsTooltip = ChartOptionsTooltip()
    var spacing = opts.chartData.eachSpacing / 2
    var yAxisPoints = opts.chartData.yAxisPoints!!
    if (calPoints != null && calPoints.length > 0) {
        if (isInExactChartArea(currentPoints, opts)) {
            yAxisPoints.forEach(fun(item, index) {
                if (currentPoints.y + offset + spacing > item) {
                    current.index = index
                }
            }
            )
        }
    }
    return current
}
fun isInExactLegendArea(currentPoints: TouchPosition, area: ChartOptionsChartDataLegendDataArea): Boolean {
    return currentPoints.x > area.start.x && currentPoints.x < area.end.x && currentPoints.y > area.start.y && currentPoints.y < area.end.y
}
fun findLegendIndex(currentPoints: TouchPosition, legendData: ChartOptionsChartDataLegendData): Number {
    var currentIndex: Number = -1
    var gap: Number = 0
    if (isInExactLegendArea(currentPoints, legendData.area)) {
        var points: UTSArray<UTSArray<ChartOptionsSeries>> = legendData.points
        var index: Number = -1
        run {
            var i: Number = 0
            var len = points.length
            while(i < len){
                var item = points[i]
                run {
                    var j: Number = 0
                    while(j < item.length){
                        index += 1
                        var area = item[j].area
                        if (area != null && currentPoints.x > area[0] - gap && currentPoints.x < area[2] + gap && currentPoints.y > area[1] - gap && currentPoints.y < area[3] + gap) {
                            currentIndex = index
                            break
                        }
                        j++
                    }
                }
                i++
            }
        }
        return currentIndex
    }
    return currentIndex
}
fun calValidDistance(self: TuiChartsInterface, distance: Number, chartData: ChartOptionsChartData, opts: ChartOptions): Number {
    var dataChartAreaWidth = opts.width - opts.area[1] - opts.area[3]
    var dataChartWidth = chartData.eachSpacing * (opts.chartData.xAxisData.xAxisPoints.length - 1)
    if (opts.type == "mount" && opts.extra.mount != null && opts.extra.mount!!.widthRatio > 1) {
        if (opts.extra.mount!!.widthRatio > 2) {
            opts.extra.mount!!.widthRatio = 2
        }
        dataChartWidth += (opts.extra.mount!!.widthRatio - 1) * chartData.eachSpacing
    }
    var validDistance = distance
    if (distance >= 0) {
        validDistance = 0
        self.uevent.trigger("scrollLeft")
        self.scrollOption.position = "left"
        opts.xAxis.scrollPosition = "left"
    } else if (Math.abs(distance) >= dataChartWidth - dataChartAreaWidth) {
        validDistance = dataChartAreaWidth - dataChartWidth
        self.uevent.trigger("scrollRight")
        self.scrollOption.position = "right"
        opts.xAxis.scrollPosition = "right"
    } else {
        self.scrollOption.position = "" + distance
        opts.xAxis.scrollPosition = "" + distance
    }
    return validDistance
}
fun drawToolTipSplitLine(offsetX: Number, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D) {
    var toolTipOption = opts.extra.tooltip ?: ChartOptionsExtraTooltip()
    var startY = opts.area[0] + opts.yOffset
    var endY = (opts.height - opts.area[2]) + opts.yOffset
    val xOffset = opts.xOffset
    if (toolTipOption.gridType == "dash") {
        context.setLineDash(_uA(
            toolTipOption.dashLength,
            toolTipOption.dashLength
        ))
    }
    context.strokeStyle = toolTipOption.gridColor
    context.lineWidth = 1 * opts.pixelRatio
    context.beginPath()
    context.moveTo(offsetX + xOffset, startY)
    context.lineTo(offsetX + xOffset, endY)
    context.stroke()
    context.setLineDash(_uA())
    if (toolTipOption.xAxisLabel) {
        var labelText = opts.categories!![opts.tooltip!!.index!! as Number]
        context.font = "" + config.fontSize + "px sans-serif"
        var textWidth = measureText(labelText, config.fontSize)
        var textX = (offsetX + xOffset) - 0.5 * textWidth
        var textY = endY + 2 * opts.pixelRatio
        context.beginPath()
        context.fillStyle = hexToRgb(toolTipOption.labelBgColor, toolTipOption.labelBgOpacity)
        context.strokeStyle = toolTipOption.labelBgColor
        context.lineWidth = 1 * opts.pixelRatio
        context.rect(textX - toolTipOption.boxPadding * opts.pixelRatio, textY, textWidth + 2 * toolTipOption.boxPadding * opts.pixelRatio, config.fontSize + 2 * toolTipOption.boxPadding * opts.pixelRatio)
        context.closePath()
        context.stroke()
        context.fill()
        context.beginPath()
        context.font = "" + config.fontSize + "px sans-serif"
        context.fillStyle = toolTipOption.labelFontColor
        context.fillText(labelText, textX, textY + toolTipOption.boxPadding * opts.pixelRatio + config.fontSize)
        context.closePath()
        context.stroke()
    }
}
fun drawToolTip(textList: UTSArray<TextList>, offset: Offset, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D) {
    var toolTipOption = opts.extra.tooltip!!
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    if (toolTipOption.showCategory == true && opts.categories != null) {
        val addtext = TextList()
        addtext.text = opts.categories!![parseInt("" + opts.tooltip!!.index)]
        textList.unshift(addtext)
    }
    var fontSize = toolTipOption.fontSize * opts.pixelRatio
    var lineHeight = toolTipOption.lineHeight * opts.pixelRatio
    var boxPadding = toolTipOption.boxPadding * opts.pixelRatio
    var legendWidth = fontSize
    var legendMarginRight = 5 * opts.pixelRatio
    if (toolTipOption.legendShow == false) {
        legendWidth = 0
        legendMarginRight = 0
    }
    var arrowWidth = if (toolTipOption.showArrow) {
        8 * opts.pixelRatio
    } else {
        0
    }
    var isOverRightBorder = false
    if (opts.type == "line" || opts.type == "mount" || opts.type == "area" || opts.type == "candle" || opts.type == "mix") {
        if (toolTipOption.splitLine == true) {
            drawToolTipSplitLine(opts.tooltip!!.offset.x, opts, config, context)
        }
    }
    offset.y -= 8 * opts.pixelRatio
    var textWidth = textList.map(fun(item: TextList): Number {
        return measureText(item.text, fontSize)
    }
    )
    var toolTipWidth = legendWidth + legendMarginRight + 4 * boxPadding + Math.max(*textWidth.toTypedArray())
    var toolTipHeight = 2 * boxPadding + textList.length * lineHeight
    if (toolTipOption.showBox == false) {
        return
    }
    if (offset.x - Math.abs(opts._scrollDistance_ ?: 0) + arrowWidth + toolTipWidth > opts.width + xOffset) {
        isOverRightBorder = true
    }
    if (toolTipHeight + offset.y > opts.height + yOffset) {
        offset.y = opts.height + yOffset - toolTipHeight
    }
    context.beginPath()
    context.fillStyle = hexToRgb(toolTipOption.bgColor, toolTipOption.bgOpacity)
    context.lineWidth = toolTipOption.borderWidth * opts.pixelRatio
    context.strokeStyle = hexToRgb(toolTipOption.borderColor, toolTipOption.borderOpacity)
    var radius = toolTipOption.borderRadius
    if (isOverRightBorder) {
        if (toolTipWidth + arrowWidth > opts.width) {
            offset.x = opts.width + Math.abs(opts._scrollDistance_ ?: 0) + arrowWidth + (toolTipWidth - opts.width) + xOffset
        }
        if (toolTipWidth > offset.x) {
            offset.x = opts.width + Math.abs(opts._scrollDistance_ ?: 0) + arrowWidth + (toolTipWidth - opts.width) + xOffset
        }
        if (toolTipOption.showArrow) {
            context.moveTo(offset.x, offset.y + 10 * opts.pixelRatio)
            context.lineTo(offset.x - arrowWidth, offset.y + 10 * opts.pixelRatio + 5 * opts.pixelRatio)
        }
        context.arc(offset.x - arrowWidth - radius, offset.y + toolTipHeight - radius, radius, 0, Math.PI / 2, false)
        context.arc(offset.x - arrowWidth - Math.round(toolTipWidth) + radius, offset.y + toolTipHeight - radius, radius, Math.PI / 2, Math.PI, false)
        context.arc(offset.x - arrowWidth - Math.round(toolTipWidth) + radius, offset.y + radius, radius, -Math.PI, -Math.PI / 2, false)
        context.arc(offset.x - arrowWidth - radius, offset.y + radius, radius, -Math.PI / 2, 0, false)
        if (toolTipOption.showArrow) {
            context.lineTo(offset.x - arrowWidth, offset.y + 10 * opts.pixelRatio - 5 * opts.pixelRatio)
            context.lineTo(offset.x, offset.y + 10 * opts.pixelRatio)
        }
    } else {
        if (toolTipOption.showArrow) {
            context.moveTo(offset.x, offset.y + 10 * opts.pixelRatio)
            context.lineTo(offset.x + arrowWidth, offset.y + 10 * opts.pixelRatio - 5 * opts.pixelRatio)
        }
        context.arc(offset.x + arrowWidth + radius, offset.y + radius, radius, -Math.PI, -Math.PI / 2, false)
        context.arc(offset.x + arrowWidth + Math.round(toolTipWidth) - radius, offset.y + radius, radius, -Math.PI / 2, 0, false)
        context.arc(offset.x + arrowWidth + Math.round(toolTipWidth) - radius, offset.y + toolTipHeight - radius, radius, 0, Math.PI / 2, false)
        context.arc(offset.x + arrowWidth + radius, offset.y + toolTipHeight - radius, radius, Math.PI / 2, Math.PI, false)
        if (toolTipOption.showArrow) {
            context.lineTo(offset.x + arrowWidth, offset.y + 10 * opts.pixelRatio + 5 * opts.pixelRatio)
            context.lineTo(offset.x, offset.y + 10 * opts.pixelRatio)
        }
    }
    context.closePath()
    context.fill()
    if (toolTipOption.borderWidth > 0) {
        context.stroke()
    }
    if (toolTipOption.legendShow) {
        textList.forEach(fun(item, index) {
            if (item.color != "") {
                context.beginPath()
                context.fillStyle = item.color
                var startX = offset.x + arrowWidth + 2 * boxPadding
                var startY = offset.y + (lineHeight - fontSize) / 2 + lineHeight * index + boxPadding + 1
                if (isOverRightBorder) {
                    startX = offset.x - toolTipWidth - arrowWidth + 2 * boxPadding
                }
                when (item.legendShape) {
                    "line" -> 
                        {
                            context.moveTo(startX, startY + 0.5 * legendWidth - 2 * opts.pixelRatio)
                            context.fillRect(startX, startY + 0.5 * legendWidth - 2 * opts.pixelRatio, legendWidth, 4 * opts.pixelRatio)
                        }
                    "triangle" -> 
                        {
                            context.moveTo(startX + 7.5 * opts.pixelRatio, startY + 0.5 * legendWidth - 5 * opts.pixelRatio)
                            context.lineTo(startX + 2.5 * opts.pixelRatio, startY + 0.5 * legendWidth + 5 * opts.pixelRatio)
                            context.lineTo(startX + 12.5 * opts.pixelRatio, startY + 0.5 * legendWidth + 5 * opts.pixelRatio)
                            context.lineTo(startX + 7.5 * opts.pixelRatio, startY + 0.5 * legendWidth - 5 * opts.pixelRatio)
                        }
                    "diamond" -> 
                        {
                            context.moveTo(startX + 7.5 * opts.pixelRatio, startY + 0.5 * legendWidth - 5 * opts.pixelRatio)
                            context.lineTo(startX + 2.5 * opts.pixelRatio, startY + 0.5 * legendWidth)
                            context.lineTo(startX + 7.5 * opts.pixelRatio, startY + 0.5 * legendWidth + 5 * opts.pixelRatio)
                            context.lineTo(startX + 12.5 * opts.pixelRatio, startY + 0.5 * legendWidth)
                            context.lineTo(startX + 7.5 * opts.pixelRatio, startY + 0.5 * legendWidth - 5 * opts.pixelRatio)
                        }
                    "circle" -> 
                        {
                            context.moveTo(startX + 7.5 * opts.pixelRatio, startY + 0.5 * legendWidth)
                            context.arc(startX + 7.5 * opts.pixelRatio, startY + 0.5 * legendWidth, 5 * opts.pixelRatio, 0, 2 * Math.PI)
                        }
                    "rect" -> 
                        {
                            context.moveTo(startX, startY + 0.5 * legendWidth - 5 * opts.pixelRatio)
                            context.fillRect(startX, startY + 0.5 * legendWidth - 5 * opts.pixelRatio, 15 * opts.pixelRatio, 10 * opts.pixelRatio)
                        }
                    "square" -> 
                        {
                            context.moveTo(startX + 2 * opts.pixelRatio, startY + 0.5 * legendWidth - 5 * opts.pixelRatio)
                            context.fillRect(startX + 2 * opts.pixelRatio, startY + 0.5 * legendWidth - 5 * opts.pixelRatio, 10 * opts.pixelRatio, 10 * opts.pixelRatio)
                        }
                    else -> 
                        {
                            context.moveTo(startX, startY + 0.5 * legendWidth - 5 * opts.pixelRatio)
                            context.fillRect(startX, startY + 0.5 * legendWidth - 5 * opts.pixelRatio, 15 * opts.pixelRatio, 10 * opts.pixelRatio)
                        }
                }
                context.closePath()
                context.fill()
            }
        }
        )
    }
    textList.forEach(fun(item: TextList, index: Number){
        var startX = offset.x + arrowWidth + 2 * boxPadding + legendWidth + legendMarginRight
        if (isOverRightBorder) {
            startX = offset.x - toolTipWidth - arrowWidth + 2 * boxPadding + legendWidth + legendMarginRight
        }
        var startY = offset.y + lineHeight * index + (lineHeight - fontSize) / 2 - 1 + boxPadding + fontSize + 4
        context.beginPath()
        context.font = "" + fontSize + "px sans-serif"
        context.textBaseline = "bottom"
        context.fillStyle = toolTipOption.fontColor
        context.fillText(item.text, startX, startY)
        context.closePath()
        context.stroke()
    }
    )
}
fun getTouches(opts: ChartOptions, e: UniEvent, index: Number = 0): TouchPosition {
    var x: Number
    var y: Number
    var top: Number = opts.DOMRect!!.top
    if (e.type == "click") {
        x = (e as UniPointerEvent).clientX
        y = (e as UniPointerEvent).clientY
    } else {
        x = (e as UniTouchEvent).changedTouches[index].clientX
        y = (e as UniTouchEvent).changedTouches[index].clientY
    }
    if (opts.rotate) {
        y = opts.height - x * opts.pixelRatio
        x = (y - top - (opts.height / opts.pixelRatio / 2) * (opts.pixelRatio - 1)) * opts.pixelRatio
    } else {
        x = x * opts.pixelRatio
        y = (y - top - (opts.height / opts.pixelRatio / 2) * (opts.pixelRatio - 1)) * opts.pixelRatio
    }
    return TouchPosition(x = x, y = y)
}
fun measureText(text: String, fontSize: Number): Number {
    var width: Number = 0
    var textArr: UTSArray<String> = text.split("")
    run {
        var i: Number = 0
        while(i < textArr.length){
            var item = textArr[i]
            if (UTSRegExp("[a-zA-Z]", "").test(item)) {
                width += 7
            } else if (UTSRegExp("[0-9]", "").test(item)) {
                width += 5.5
            } else if (UTSRegExp("\\.", "").test(item)) {
                width += 2.7
            } else if (UTSRegExp("-", "").test(item)) {
                width += 3.25
            } else if (UTSRegExp(":", "").test(item)) {
                width += 2.5
            } else if (UTSRegExp("[\\u4e00-\\u9fa5]", "").test(item)) {
                width += 10
            } else if (UTSRegExp("\\(|\\)", "").test(item)) {
                width += 3.73
            } else if (UTSRegExp("\\s", "").test(item)) {
                width += 2.5
            } else if (UTSRegExp("%", "").test(item)) {
                width += 8
            } else {
                width += 10
            }
            i++
        }
    }
    return width * fontSize / 10
}
fun hexToRgb(hex: String, opacity: Number = 1): String {
    val hexVal = hex.replace(";", "")
    val hexArr = hexVal.split(":")
    val toRgba = fun(hexNumber: String): String {
        return "rgba(" + parseInt("0x" + hexNumber.slice(1, 3)) + "," + parseInt("0x" + hexNumber.slice(3, 5)) + "," + parseInt("0x" + hexNumber.slice(5, 7)) + "," + opacity + ")"
    }
    if (hexArr.length > 1) {
        return "" + hexArr[0] + ": " + toRgba(hexArr[1]) + "; "
    } else {
        return toRgba("" + hexArr[0] + " ")
    }
}
fun fillSeries(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig): UTSArray<ChartOptionsSeries> {
    var index: Number = 0
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            if (item.color == null) {
                item.color = config.color[index]
                index = (index + 1) % config.color.length
            }
            if (item.linearIndex == null) {
                item.linearIndex = i
            }
            if (item.index == null) {
                item.index = 0
            }
            if (item.type == null) {
                item.type = opts.type
            }
            if (item.type == null) {
                item.type = opts.type
            }
            if (item.legendShape == null) {
                when (item.type!!) {
                    "line" -> 
                        item.legendShape = "line"
                    "column", "bar" -> 
                        item.legendShape = "rect"
                    "area", "mount" -> 
                        item.legendShape = "triangle"
                    else -> 
                        item.legendShape = "circle"
                }
            }
            i++
        }
    }
    return series
}
fun calLegendData(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, chartData: ChartOptionsChartData): ChartOptionsChartDataLegendData {
    val legendData: ChartOptionsChartDataLegendData = ChartOptionsChartDataLegendData()
    if (opts.legend.show == false) {
        chartData.legendData = legendData
        return legendData
    }
    var padding = opts.legend.padding * opts.pixelRatio
    var margin = opts.legend.margin * opts.pixelRatio
    var fontSize = opts.legend.fontSize
    var shapeWidth = 15 * opts.pixelRatio
    var shapeRight = 5 * opts.pixelRatio
    var lineHeight = Math.max(opts.legend.lineHeight * opts.pixelRatio, fontSize)
    if (opts.legend.position == "top" || opts.legend.position == "bottom") {
        var legendList: UTSArray<UTSArray<ChartOptionsSeries>> = _uA()
        var widthCount: Number = 0
        var widthCountArr: UTSArray<Number> = _uA()
        var currentRow: UTSArray<ChartOptionsSeries> = _uA()
        run {
            var i: Number = 0
            while(i < series.length){
                var item = series[i]
                val legendText = if (item.legendText != null) {
                    item.legendText!!
                } else {
                    item.name
                }
                var itemWidth = shapeWidth + shapeRight + measureText("" + legendText, fontSize) + opts.legend.itemGap * opts.pixelRatio
                if (widthCount + itemWidth > opts.width - opts.area[1] - opts.area[3]) {
                    legendList.push(currentRow)
                    widthCountArr.push(widthCount - opts.legend.itemGap * opts.pixelRatio)
                    widthCount = itemWidth
                    currentRow = _uA(
                        item
                    )
                } else {
                    widthCount += itemWidth
                    currentRow.push(item)
                }
                i++
            }
        }
        if (currentRow.length > 0) {
            legendList.push(currentRow)
            widthCountArr.push(widthCount - opts.legend.itemGap * opts.pixelRatio)
            legendData.widthArr = widthCountArr
            var legendWidth = Math.max(*widthCountArr.toTypedArray())
            when (opts.legend.float) {
                "left" -> 
                    {
                        legendData.area.start.x = opts.area[3]
                        legendData.area.end.x = opts.area[3] + legendWidth + 2 * padding
                    }
                "right" -> 
                    {
                        legendData.area.start.x = opts.width - opts.area[1] - legendWidth - 2 * padding
                        legendData.area.end.x = opts.width - opts.area[1]
                    }
                else -> 
                    {
                        legendData.area.start.x = (opts.width - legendWidth) / 2 - padding
                        legendData.area.end.x = (opts.width + legendWidth) / 2 + padding
                    }
            }
            legendData.area.width = legendWidth + 2 * padding
            legendData.area.wholeWidth = legendWidth + 2 * padding
            legendData.area.height = legendList.length * lineHeight + 2 * padding
            legendData.area.wholeHeight = legendList.length * lineHeight + 2 * padding + 2 * margin
            legendData.points = legendList
        }
    } else {
        var len = series.length
        var maxHeight = opts.height - opts.area[0] - opts.area[2] - 2 * margin - 2 * padding
        var maxLength = Math.min(Math.floor(maxHeight / lineHeight), len)
        legendData.area.height = maxLength * lineHeight + padding * 2
        legendData.area.wholeHeight = maxLength * lineHeight + padding * 2
        when (opts.legend.float) {
            "top" -> 
                {
                    legendData.area.start.y = opts.area[0] + margin
                    legendData.area.end.y = opts.area[0] + margin + legendData.area.height
                }
            "bottom" -> 
                {
                    legendData.area.start.y = opts.height - opts.area[2] - margin - legendData.area.height
                    legendData.area.end.y = opts.height - opts.area[2] - margin
                }
            else -> 
                {
                    legendData.area.start.y = (opts.height - legendData.area.height) / 2
                    legendData.area.end.y = (opts.height + legendData.area.height) / 2
                }
        }
        var lineNum = if (len % maxLength == 0) {
            len / maxLength
        } else {
            Math.floor((len / maxLength) + 1)
        }
        var currentRow: UTSArray<UTSArray<ChartOptionsSeries>> = _uA()
        run {
            var i: Number = 0
            while(i < lineNum){
                var temp = series.slice(i * maxLength, i * maxLength + maxLength)
                currentRow.push(temp)
                i++
            }
        }
        legendData.points = currentRow
        if (currentRow.length > 0) {
            run {
                var i: Number = 0
                while(i < currentRow.length){
                    var item = currentRow[i]
                    var maxWidth: Number = 0
                    run {
                        var j: Number = 0
                        while(j < item.length){
                            var itemWidth = shapeWidth + shapeRight + measureText("" + item[j].name, fontSize) + opts.legend.itemGap * opts.pixelRatio
                            if (itemWidth > maxWidth) {
                                maxWidth = itemWidth
                            }
                            j++
                        }
                    }
                    legendData.widthArr.push(maxWidth)
                    legendData.heightArr.push(item.length * lineHeight + padding * 2)
                    i++
                }
            }
            var legendWidth: Number = 0
            run {
                var i: Number = 0
                while(i < legendData.widthArr.length){
                    legendWidth += legendData.widthArr[i]
                    i++
                }
            }
            legendData.area.width = legendWidth - opts.legend.itemGap * opts.pixelRatio + 2 * padding
            legendData.area.wholeWidth = legendData.area.width + padding
        }
    }
    when (opts.legend.position) {
        "top" -> 
            {
                legendData.area.start.y = opts.area[0] + margin
                legendData.area.end.y = opts.area[0] + margin + legendData.area.height
            }
        "bottom" -> 
            {
                legendData.area.start.y = opts.height - opts.area[2] - legendData.area.height - margin
                legendData.area.end.y = opts.height - opts.area[2] - margin
            }
        "left" -> 
            {
                legendData.area.start.x = opts.area[3]
                legendData.area.end.x = opts.area[3] + legendData.area.width
            }
        "right" -> 
            {
                legendData.area.start.x = opts.width - opts.area[1] - legendData.area.width
                legendData.area.end.x = opts.width - opts.area[1]
            }
    }
    chartData.legendData = legendData
    return legendData
}
fun calYAxisData(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig): ChartOptionsChartDataYAxisData {
    val rangesArr: UTSArray<UTSArray<String>> = _uA()
    val rangesFormatArr: UTSArray<UTSArray<String>> = _uA()
    val yAxisWidthArr: UTSArray<ChartOptionsChartDataYAxisDataYAxisWidth> = _uA()
    var columnstyle: String = if (opts.extra.column != null) {
        opts.extra.column!!.type
    } else {
        ""
    }
    var YLength = opts.yAxis.data.length
    var newSeries: UTSArray<UTSArray<ChartOptionsSeries>> = _uA()
    if (YLength > 0) {
        run {
            var i: Number = 0
            while(i < YLength){
                val newSeriesChild: UTSArray<ChartOptionsSeries> = _uA()
                run {
                    var j: Number = 0
                    while(j < series.length){
                        if (series[j].index == i) {
                            newSeriesChild.push(series[j])
                        }
                        j++
                    }
                }
                newSeries.push(newSeriesChild)
                i++
            }
        }
        run {
            var i: Number = 0
            while(i < YLength){
                var yData = opts.yAxis.data[i]
                if (opts.yAxis.disabled == true) {
                    yData.disabled = true
                }
                if (yData.type == "categories") {
                    if (yData.formatter == null) {
                        yData.formatter = fun(kVal: String, index: Number, opts: ChartOptions): String {
                            return kVal + (yData.unit ?: "")
                        }
                    }
                    yData.categories = yData.categories ?: opts.categories
                    rangesArr.push(yData.categories!!)
                } else {
                    if (yData.formatter == null) {
                        yData.formatter = fun(kVal: String, index: Number, opts: ChartOptions): String {
                            return parseFloat(kVal).toFixed(yData.tofix ?: 0) + (yData.unit ?: "")
                        }
                    }
                    rangesArr.push(getYAxisTextList(newSeries[i], opts, config, columnstyle, yData))
                }
                var yAxisFontSizes = if (yData.fontSize != null) {
                    yData.fontSize!! * opts.pixelRatio
                } else {
                    config.fontSize
                }
                val cocdyw = ChartOptionsChartDataYAxisDataYAxisWidth()
                cocdyw.position = yData.position
                cocdyw.width = 0
                yAxisWidthArr.push(cocdyw)
                val handlranges = rangesArr[i].map(fun(reassignedItems, index): String {
                    var items = reassignedItems
                    val kFun = yData.formatter as (`val`: String, index: Number, opts: ChartOptions) -> String
                    items = kFun(items, index, opts)
                    cocdyw.width = Math.max(cocdyw.width, measureText(items, yAxisFontSizes) + 5)
                    return items
                })
                rangesFormatArr.push(handlranges)
                var calibration = if (yData.calibration) {
                    4 * opts.pixelRatio
                } else {
                    0
                }
                cocdyw.width = cocdyw.width + calibration + 3 * opts.pixelRatio
                if (yData.disabled == true) {
                    cocdyw.width = 0
                }
                i++
            }
        }
    } else {
        if (opts.type == "bar") {
            rangesArr.push(opts.categories!!)
            if (opts.yAxis.formatter == null) {
                opts.yAxis.formatter = fun(kVal: String, index: Number, opts: ChartOptions): String {
                    return kVal + opts.yAxis.unit
                }
            }
        } else {
            if (opts.yAxis.formatter == null) {
                opts.yAxis.formatter = fun(kVal: String, index: Number, opts: ChartOptions): String {
                    return parseFloat(kVal).toFixed(opts.yAxis.tofix) + opts.yAxis.unit
                }
            }
            rangesArr.push(getYAxisTextList(series, opts, config, columnstyle, ChartOptionsYAxisData()))
        }
        val cocdyw = ChartOptionsChartDataYAxisDataYAxisWidth()
        cocdyw.position = "left"
        cocdyw.width = 0
        yAxisWidthArr.push(cocdyw)
        var yAxisFontSize: Number
        if (opts.yAxis.fontSize == null) {
            yAxisFontSize = config.fontSize
        } else {
            yAxisFontSize = opts.yAxis.fontSize!! * opts.pixelRatio
        }
        val handlranges = rangesArr[0].map(fun(reassignedItem, index): String {
            var item = reassignedItem
            val kFun = opts.yAxis.formatter as (`val`: String, index: Number, opts: ChartOptions) -> String
            item = kFun(item, index, opts)
            cocdyw.width = Math.max(cocdyw.width, measureText(item, yAxisFontSize) + 5)
            return item
        }
        )
        rangesFormatArr.push(handlranges)
        cocdyw.width += 3 * opts.pixelRatio
        if (opts.yAxis.disabled == true) {
            opts.yAxis.data[0].disabled = true
        } else {
            val yAxisData = ChartOptionsYAxisData()
            yAxisData.disabled = false
            yAxisData.position = "left"
            yAxisData.max = opts.yAxis.max
            yAxisData.min = opts.yAxis.min
            yAxisData.formatter = opts.yAxis.formatter
            if (opts.type == "bar") {
                yAxisData.categories = opts.categories
                yAxisData.type = "categories"
            }
            opts.yAxis.data.push(yAxisData)
        }
    }
    val cdyd: ChartOptionsChartDataYAxisData = ChartOptionsChartDataYAxisData()
    cdyd.rangesFormat = rangesFormatArr
    cdyd.ranges = rangesArr
    cdyd.yAxisWidth = yAxisWidthArr
    return cdyd
}
fun dataCombine(series: UTSArray<ChartOptionsSeries>): UTSArray<ChartOptionsSeriesData> {
    val newSeries: UTSArray<ChartOptionsSeriesData> = _uA()
    series.forEach(fun(item: ChartOptionsSeries){
        if (item.data != null) {
            newSeries.push(*item.data!!.toTypedArray())
        }
    }
    )
    return newSeries
}
fun findRange(reassignedNum: Number, reassignedType: String?, reassignedLimit: Number?): Number {
    var limit = reassignedLimit
    var type = reassignedType
    var num = reassignedNum
    if (isNaN(num)) {
        throw UTSError("[TuiCharts] series数据需为Number格式")
    }
    limit = limit ?: 10
    type = type ?: "upper"
    var multiple: Number = 1
    while(limit < 1){
        limit *= 10
        multiple *= 10
    }
    if (type == "upper") {
        num = Math.ceil(num * multiple)
    } else {
        num = Math.floor(num * multiple)
    }
    while(num % limit != 0){
        if (type == "upper") {
            if (num == num + 1) {
                break
            }
            num++
        } else {
            num--
        }
    }
    return num / multiple
}
fun getDataRange(minData: Number, maxData: Number): ChartRange {
    var limit: Number
    var range = maxData - minData
    if (range >= 10000) {
        limit = 1000
    } else if (range >= 1000) {
        limit = 100
    } else if (range >= 100) {
        limit = 10
    } else if (range >= 10) {
        limit = 5
    } else if (range >= 1) {
        limit = 1
    } else if (range >= 0.1) {
        limit = 0.1
    } else if (range >= 0.01) {
        limit = 0.01
    } else if (range >= 0.001) {
        limit = 0.001
    } else if (range >= 0.0001) {
        limit = 0.0001
    } else if (range >= 0.00001) {
        limit = 0.00001
    } else {
        limit = 0.000001
    }
    return ChartRange(minRange = findRange(minData, "lower", limit), maxRange = findRange(maxData, "upper", limit))
}
fun fixColumeStackData(points: UTSArray<ChartOptionsChartDataCalPoints>, eachSpacing: Number, opts: ChartOptions): UTSArray<ChartOptionsChartDataCalPoints> {
    var categoryGap = opts.extra.column!!.categoryGap * opts.pixelRatio
    return points.map(fun(item: ChartOptionsChartDataCalPoints, indexn: Number): ChartOptionsChartDataCalPoints {
        item.width = Math.ceil(eachSpacing - 2 * categoryGap)
        if (opts.extra.column != null && opts.extra.column!!.width > 0) {
            item.width = Math.min(item.width!!, opts.extra.column!!.width * opts.pixelRatio)
        }
        if (item.width!! <= 0) {
            item.width = 1
        }
        return item
    }
    )
}
fun dataCombineStack(series: UTSArray<ChartOptionsSeries>, len: Number): UTSArray<ChartOptionsSeriesData> {
    var sum: Map<Number, Number> = Map<Number, Number>()
    var newseries: UTSArray<ChartOptionsSeriesData> = _uA()
    series.forEach(fun(item: ChartOptionsSeries){
        run {
            var j: Number = 0
            while(j < len){
                val kVal = item.data!![j].value!!
                val old_number = sum.get(j) ?: 0
                sum.set(j, kVal + old_number)
                j++
            }
        }
    }
    )
    series.forEach(fun(item: ChartOptionsSeries, index: Number){
        newseries.push(*item.data!!.toTypedArray())
        sum.forEach(fun(k: Number){
            val sumseries = ChartOptionsSeriesData()
            sumseries.value = k
            newseries.push(sumseries)
        }
        )
    }
    )
    return newseries
}
fun getArrYAxisTextList(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, stack: String, yData: ChartOptionsYAxisData): UTSArray<String> {
    var data: UTSArray<UTSArray<Number>> = _uA()
    series.forEach(fun(item: ChartOptionsSeries){
        data.push(*item.dataArr.toTypedArray())
    }
    )
    var sorted: UTSArray<Number> = _uA()
    data.map(fun(item: UTSArray<Number>){
        item.map(fun(subitem){
            sorted.push(subitem)
        }
        )
    }
    )
    var minData = yData.min ?: 0
    var maxData = yData.max ?: 0
    if (sorted.length > 0) {
        minData = Math.min(*sorted.toTypedArray())
        maxData = Math.max(*sorted.toTypedArray())
    }
    if (minData == maxData) {
        if (maxData == 0) {
            maxData = 10
        } else {
            minData = 0
        }
    }
    var dataRange = getDataRange(minData, maxData)
    var minRange = if ((yData.min == null)) {
        dataRange.minRange
    } else {
        yData.min!!
    }
    var maxRange = if ((yData.max == null)) {
        dataRange.maxRange
    } else {
        yData.max!!
    }
    var eachRange = (maxRange - minRange) / opts.yAxis.splitNumber
    var range: UTSArray<String> = _uA()
    run {
        var i: Number = 0
        while(i <= opts.yAxis.splitNumber){
            range.push("" + (minRange + eachRange * i))
            i++
        }
    }
    return range.reverse()
}
fun getYAxisTextList(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, stack: String, yData: ChartOptionsYAxisData): UTSArray<String> {
    if (opts.type == "candle") {
        return getArrYAxisTextList(series, opts, config, stack, yData)
    }
    var data: UTSArray<ChartOptionsSeriesData> = _uA()
    if (stack == "stack") {
        data = dataCombineStack(series, opts.categories!!.length)
    } else {
        data = dataCombine(series)
    }
    var sorted: UTSArray<Number> = _uA()
    data = data.filter(fun(item): Boolean {
        return item.value != null
    }
    )
    data.map(fun(item: ChartOptionsSeriesData){
        sorted.push(item.value!!)
    }
    )
    var minData = yData.min ?: 0
    var maxData = yData.max ?: 0
    if (sorted.length > 0) {
        minData = Math.min(*sorted.toTypedArray())
        maxData = Math.max(*sorted.toTypedArray())
    }
    if (minData == maxData) {
        if (maxData == 0) {
            maxData = 10
        } else {
            minData = 0
        }
    }
    var dataRange = getDataRange(minData, maxData)
    var minRange = if ((yData.min == null)) {
        dataRange.minRange
    } else {
        yData.min!!
    }
    var maxRange = if ((yData.max == null)) {
        dataRange.maxRange
    } else {
        yData.max!!
    }
    var eachRange = (maxRange - minRange) / opts.yAxis.splitNumber
    var range: UTSArray<String> = _uA()
    run {
        var i: Number = 0
        while(i <= opts.yAxis.splitNumber){
            range.push("" + (minRange + eachRange * i))
            i++
        }
    }
    return range.reverse()
}
fun getXAxisPoints(categories: UTSArray<String>, opts: ChartOptions, config: TuiChartConfig): ChartOptionsChartDataXAxisData {
    var spacingValid = opts.width - opts.area[1] - opts.area[3]
    var dataCount = if (opts.enableScroll) {
        Math.min(opts.xAxis.itemCount, categories.length)
    } else {
        categories.length
    }
    if ((opts.type == "line" || opts.type == "area" || opts.type == "scatter" || opts.type == "bubble" || opts.type == "bar") && dataCount > 1 && opts.xAxis.boundaryGap == "justify") {
        dataCount -= 1
    }
    var widthRatio: Number = 0
    if (opts.type == "mount" && opts.extra != null && opts.extra.mount != null && opts.extra.mount!!.widthRatio > 1) {
        if (opts.extra.mount!!.widthRatio > 2) {
            opts.extra.mount!!.widthRatio = 2
        }
        widthRatio = opts.extra.mount!!.widthRatio - 1
        dataCount += widthRatio
    }
    var eachSpacing = spacingValid / dataCount
    var xAxisPoints: UTSArray<Number> = _uA()
    var startX = opts.area[3]
    var endX = opts.width - opts.area[1]
    categories.forEach(fun(item, index) {
        xAxisPoints.push(startX + widthRatio / 2 * eachSpacing + index * eachSpacing)
    }
    )
    if (opts.xAxis.boundaryGap != "justify") {
        if (opts.enableScroll == true) {
            xAxisPoints.push(startX + widthRatio * eachSpacing + categories.length * eachSpacing)
        } else {
            xAxisPoints.push(endX)
        }
    }
    val optscdxd = ChartOptionsChartDataXAxisData()
    optscdxd.xAxisPoints = xAxisPoints
    optscdxd.startX = startX
    optscdxd.endX = endX
    optscdxd.eachSpacing = eachSpacing
    return optscdxd
}
fun calCategoriesData(categories: UTSArray<String>, opts: ChartOptions, config: TuiChartConfig, eachSpacing: Number, context: CanvasRenderingContext2D): ChartOptionsChartDataCategoriesData {
    val result: ChartOptionsChartDataCategoriesData = ChartOptionsChartDataCategoriesData()
    result.xAxisHeight = opts.xAxis.lineHeight * opts.pixelRatio + opts.xAxis.marginTop * opts.pixelRatio
    var fontSize = opts.xAxis.fontSize * opts.pixelRatio
    var categoriesTextLenth = categories.map(fun(item, index): Number {
        var xitem: String
        if (opts.xAxis.formatter == null) {
            xitem = item
        } else {
            val kFun = opts.xAxis.formatter as (`val`: String, index: Number, opts: ChartOptions) -> String
            xitem = kFun(item, index, opts)
        }
        return measureText("" + xitem, fontSize)
    }
    )
    var maxTextLength = Math.max(*categoriesTextLenth.toTypedArray())
    if (opts.xAxis.rotateLabel == true) {
        result.angle = opts.xAxis.rotateAngle * Math.PI / 180
        var tempHeight = opts.xAxis.marginTop * opts.pixelRatio * 2 + Math.abs(maxTextLength * Math.sin(result.angle))
        tempHeight = if (tempHeight < fontSize + opts.xAxis.marginTop * opts.pixelRatio * 2) {
            tempHeight + opts.xAxis.marginTop * opts.pixelRatio * 2
        } else {
            tempHeight
        }
        result.xAxisHeight = tempHeight
    }
    if (opts.enableScroll && opts.xAxis.scrollShow) {
        result.xAxisHeight += 6 * opts.pixelRatio
    }
    if (opts.xAxis.disabled) {
        result.xAxisHeight = 0
    }
    return result
}
open class ChartsTiming : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ChartsTiming", "uni_modules/tui-xechars/libs/util.uts", 1158, 7)
    }
    open fun easeIn(pos: Number): Number {
        return Math.pow(pos, 3)
    }
    open fun easeOut(pos: Number): Number {
        return Math.pow(pos - 1, 3) + 1
    }
    open fun easeInOut(pos: Number): Number {
        if ((pos / 0.5) < 1) {
            return 0.5 * Math.pow(pos, 3)
        } else {
            return 0.5 * (Math.pow(pos - 2, 3) + 2)
        }
    }
    open fun linear(pos: Number): Number {
        return pos
    }
}
val Timing = ChartsTiming()
fun drawYAxis(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D) {
    if (opts.yAxis.disabled == true) {
        return
    }
    var spacingValid = opts.height - opts.area[0] - opts.area[2]
    var eachSpacing = spacingValid / opts.yAxis.splitNumber
    var startX = opts.area[3]
    var endX = opts.width - opts.area[1]
    var endY = opts.height - opts.area[2]
    context.beginPath()
    context.fillStyle = opts.background
    if (opts.enableScroll == true && opts.xAxis.scrollPosition != null && opts.xAxis.scrollPosition != "left") {
        context.fillRect(0, 0, startX, endY + 2 * opts.pixelRatio)
    }
    if (opts.enableScroll == true && opts.xAxis.scrollPosition != null && opts.xAxis.scrollPosition != "right") {
        context.fillRect(endX, 0, opts.width, endY + 2 * opts.pixelRatio)
    }
    context.closePath()
    context.stroke()
    var tStartLeft = opts.area[3]
    var tStartRight = opts.width - opts.area[1]
    var tStartCenter = opts.area[3] + (opts.width - opts.area[1] - opts.area[3]) / 2
    if (opts.yAxis.data.length > 0) {
        run {
            var i: Number = 0
            while(i < opts.yAxis.data.length){
                var yData = opts.yAxis.data[i]
                var points: UTSArray<Number> = _uA()
                if (yData.type == "categories") {
                    run {
                        var i: Number = 0
                        while(i <= yData.categories!!.length){
                            points.push(opts.area[0] + spacingValid / yData.categories!!.length / 2 + spacingValid / yData.categories!!.length * i)
                            i++
                        }
                    }
                } else {
                    run {
                        var i: Number = 0
                        while(i <= opts.yAxis.splitNumber){
                            points.push(opts.area[0] + eachSpacing * i)
                            i++
                        }
                    }
                }
                if (yData.disabled != true) {
                    var rangesFormat = opts.chartData.yAxisData.rangesFormat!![i]
                    var yAxisFontSize = if (yData.fontSize != null) {
                        yData.fontSize!! * opts.pixelRatio
                    } else {
                        config.fontSize
                    }
                    var yAxisWidth = opts.chartData.yAxisData.yAxisWidth!![i]
                    var textAlign = yData.textAlign ?: "right"
                    rangesFormat.forEach(fun(item, index) {
                        var pos = points[index]
                        context.beginPath()
                        context.font = "" + yAxisFontSize + "px sans-serif"
                        context.lineWidth = (1 * opts.pixelRatio)
                        context.strokeStyle = yData.axisLineColor ?: "#cccccc"
                        context.fillStyle = (yData.fontColor ?: opts.fontColor)
                        var tmpstrat: Number = 0
                        var gapwidth = 4 * opts.pixelRatio
                        if (yAxisWidth.position == "left") {
                            if (yData.calibration == true) {
                                context.moveTo(opts.xOffset + tStartLeft, pos + opts.yOffset)
                                context.lineTo(opts.xOffset + tStartLeft - 3 * opts.pixelRatio, pos + opts.yOffset)
                                gapwidth += 3 * opts.pixelRatio
                            }
                            when (textAlign) {
                                "left" -> 
                                    {
                                        context.textAlign = "left"
                                        tmpstrat = tStartLeft - yAxisWidth.width
                                    }
                                "right" -> 
                                    {
                                        context.textAlign = "right"
                                        tmpstrat = tStartLeft - gapwidth
                                    }
                                else -> 
                                    {
                                        context.textAlign = "center"
                                        tmpstrat = tStartLeft - yAxisWidth.width / 2
                                    }
                            }
                            context.fillText("" + item, opts.xOffset + tmpstrat, opts.yOffset + pos + yAxisFontSize / 2 - 3 * opts.pixelRatio)
                        } else if (yAxisWidth.position == "right") {
                            if (yData.calibration == true) {
                                context.moveTo(opts.xOffset + tStartRight, pos + opts.yOffset)
                                context.lineTo(opts.xOffset + tStartRight + 3 * opts.pixelRatio, pos + opts.yOffset)
                                gapwidth += 3 * opts.pixelRatio
                            }
                            when (textAlign) {
                                "left" -> 
                                    {
                                        context.textAlign = "left"
                                        tmpstrat = tStartRight + gapwidth
                                    }
                                "right" -> 
                                    {
                                        context.textAlign = "right"
                                        tmpstrat = tStartRight + yAxisWidth.width
                                    }
                                else -> 
                                    {
                                        context.textAlign = "center"
                                        tmpstrat = tStartRight + yAxisWidth.width / 2
                                    }
                            }
                            context.fillText("" + item, opts.xOffset + tmpstrat, opts.yOffset + pos + yAxisFontSize / 2 - 3 * opts.pixelRatio)
                        } else if (yAxisWidth.position == "center") {
                            if (yData.calibration == true) {
                                context.moveTo(opts.xOffset + tStartCenter, pos + opts.yOffset)
                                context.lineTo(opts.xOffset + tStartCenter - 3 * opts.pixelRatio, pos + opts.yOffset)
                                gapwidth += 3 * opts.pixelRatio
                            }
                            when (textAlign) {
                                "left" -> 
                                    {
                                        context.textAlign = "left"
                                        tmpstrat = tStartCenter - yAxisWidth.width
                                    }
                                "right" -> 
                                    {
                                        context.textAlign = "right"
                                        tmpstrat = tStartCenter - gapwidth
                                    }
                                else -> 
                                    {
                                        context.textAlign = "center"
                                        tmpstrat = tStartCenter - yAxisWidth.width / 2
                                    }
                            }
                            context.fillText("" + item, opts.xOffset + tmpstrat, opts.yOffset + pos + yAxisFontSize / 2 - 3 * opts.pixelRatio)
                        }
                        context.closePath()
                        context.stroke()
                        context.textAlign = "left"
                    }
                    )
                    if (yData.axisLine != false) {
                        context.beginPath()
                        context.strokeStyle = yData.axisLineColor ?: "#cccccc"
                        context.lineWidth = (1 * opts.pixelRatio)
                        if (yAxisWidth.position == "left") {
                            context.moveTo(opts.xOffset + tStartLeft, opts.height - opts.area[2] + opts.yOffset)
                            context.lineTo(opts.xOffset + tStartLeft, opts.area[0] + opts.yOffset)
                        } else if (yAxisWidth.position == "right") {
                            context.moveTo(opts.xOffset + tStartRight, opts.height - opts.area[2] + opts.yOffset)
                            context.lineTo(opts.xOffset + tStartRight, opts.area[0] + opts.yOffset)
                        } else if (yAxisWidth.position == "center") {
                            context.moveTo(opts.xOffset + tStartCenter, opts.height - opts.area[2] + opts.yOffset)
                            context.lineTo(opts.xOffset + tStartCenter, opts.area[0] + opts.yOffset)
                        }
                        context.stroke()
                    }
                    if (opts.yAxis.showTitle) {
                        var titleFontSize = if (yData.titleFontSize != null) {
                            yData.titleFontSize!! * opts.pixelRatio
                        } else {
                            config.fontSize
                        }
                        var title = yData.title!!
                        context.beginPath()
                        context.font = "" + titleFontSize + "px sans-serif"
                        context.fillStyle = (yData.titleFontColor ?: opts.fontColor)
                        if (yAxisWidth.position == "left") {
                            context.fillText(title, opts.xOffset + tStartLeft - measureText(title, titleFontSize) / 2 + (yData.titleOffsetX ?: 0), opts.yOffset + opts.area[0] - (10 - (yData.titleOffsetY ?: 0)) * opts.pixelRatio)
                        } else if (yAxisWidth.position == "right") {
                            context.fillText(title, opts.xOffset + tStartRight - measureText(title, titleFontSize) / 2 + (yData.titleOffsetX ?: 0), opts.yOffset + opts.area[0] - (10 - (yData.titleOffsetY ?: 0)) * opts.pixelRatio)
                        } else if (yAxisWidth.position == "center") {
                            context.fillText(title, opts.xOffset + tStartCenter - measureText(title, titleFontSize) / 2 + (yData.titleOffsetX ?: 0), opts.yOffset + opts.area[0] - (10 - (yData.titleOffsetY ?: 0)) * opts.pixelRatio)
                        }
                        context.closePath()
                        context.stroke()
                    }
                    if (yAxisWidth.position == "left") {
                        tStartLeft -= (yAxisWidth.width + opts.yAxis.padding * opts.pixelRatio)
                    } else {
                        tStartRight += yAxisWidth.width + opts.yAxis.padding * opts.pixelRatio
                    }
                }
                i++
            }
        }
    }
}
fun drawYAxisGrid(categories: UTSArray<String>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D) {
    if (opts.yAxis.disableGrid == true) {
        return
    }
    var spacingValid = opts.height - opts.area[0] - opts.area[2]
    var eachSpacing = spacingValid / opts.yAxis.splitNumber
    var startX = opts.area[3]
    var xAxisPoints = opts.chartData.xAxisData.xAxisPoints!!
    var xAxiseachSpacing = opts.chartData.xAxisData.eachSpacing!!
    var TotalWidth = xAxiseachSpacing * (xAxisPoints.length - 1)
    if (opts.type == "mount" && opts.extra != null && opts.extra.mount != null && opts.extra.mount!!.widthRatio != null && opts.extra.mount!!.widthRatio > 1) {
        if (opts.extra.mount!!.widthRatio > 2) {
            opts.extra.mount!!.widthRatio = 2
        }
        TotalWidth += (opts.extra.mount!!.widthRatio - 1) * xAxiseachSpacing
    }
    var endX = startX + TotalWidth
    var points: UTSArray<Number> = _uA()
    var startY: Number = 1
    if (opts.xAxis.axisLine == false) {
        startY = 0
    }
    run {
        var i = startY
        while(i < opts.yAxis.splitNumber + 1){
            points.push(opts.height - opts.area[2] - eachSpacing * i)
            i++
        }
    }
    context.save()
    if (opts._scrollDistance_ != null && opts._scrollDistance_ != 0) {
        context.translate(opts._scrollDistance_!!, 0)
    }
    if (opts.yAxis.gridType == "dash") {
        context.setLineDash(_uA(
            opts.yAxis.dashLength * opts.pixelRatio,
            opts.yAxis.dashLength * opts.pixelRatio
        ))
    }
    context.strokeStyle = opts.yAxis.gridColor
    context.lineWidth = (1 * opts.pixelRatio)
    points.forEach(fun(item, index) {
        context.beginPath()
        context.moveTo(startX + opts.xOffset, item + opts.yOffset)
        context.lineTo(endX + opts.xOffset, item + opts.yOffset)
        context.stroke()
    }
    )
    context.setLineDash(_uA())
    context.restore()
}
fun fillCustomColor(linearType: String, customColor: UTSArray<String>?, series: UTSArray<ChartOptionsSeries>, config: TuiChartConfig): UTSArray<String> {
    var newcolor = customColor ?: _uA()
    if (linearType == "custom" && newcolor.length == 0) {
        newcolor = config.linearColor
    }
    if (linearType == "custom" && newcolor.length < series.length) {
        var chazhi = series.length - newcolor.length
        run {
            var i: Number = 0
            while(i < chazhi){
                newcolor.push(config.linearColor[(i + 1) % config.linearColor.length])
                i++
            }
        }
    }
    return newcolor
}
fun drawXAxis(categories: UTSArray<String>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D) {
    var xAxisData = opts.chartData.xAxisData
    var xAxisPoints = xAxisData.xAxisPoints!!
    var startX = xAxisData.startX!!
    var endX = xAxisData.endX!!
    var eachSpacing = xAxisData.eachSpacing!!
    var boundaryGap = "center"
    if (opts.type == "bar" || opts.type == "line" || opts.type == "area" || opts.type == "scatter" || opts.type == "bubble") {
        boundaryGap = opts.xAxis.boundaryGap
    }
    var startY = opts.height - opts.area[2]
    var endY = opts.area[0]
    if (opts.enableScroll && opts.xAxis.scrollShow) {
        var scrollY = opts.height - opts.area[2] + config.xAxisHeight
        var scrollScreenWidth = endX - startX
        var scrollTotalWidth = eachSpacing * (xAxisPoints.length - 1)
        if (opts.type == "mount" && opts.extra != null && opts.extra.mount != null && opts.extra.mount!!.widthRatio != null && opts.extra.mount!!.widthRatio > 1) {
            if (opts.extra.mount!!.widthRatio > 2) {
                opts.extra.mount!!.widthRatio = 2
            }
            scrollTotalWidth += (opts.extra.mount!!.widthRatio - 1) * eachSpacing
        }
        var scrollWidth = scrollScreenWidth * scrollScreenWidth / scrollTotalWidth
        var scrollLeft: Number = 0
        if (opts._scrollDistance_ != null) {
            scrollLeft = -opts._scrollDistance_!! * scrollScreenWidth / scrollTotalWidth
        }
        context.beginPath()
        context.lineCap = "round"
        context.lineWidth = (6 * opts.pixelRatio)
        context.strokeStyle = opts.xAxis.scrollBackgroundColor ?: "#EFEBEF"
        context.moveTo(startX + opts.xOffset, scrollY + opts.yOffset)
        context.lineTo(endX + opts.xOffset, scrollY + opts.yOffset)
        context.stroke()
        context.closePath()
        context.beginPath()
        context.lineCap = "round"
        context.lineWidth = (6 * opts.pixelRatio)
        context.strokeStyle = opts.xAxis.scrollColor ?: "#A6A6A6"
        context.moveTo(opts.xOffset + startX + scrollLeft, scrollY + opts.yOffset)
        context.lineTo(opts.xOffset + startX + scrollLeft + scrollWidth, scrollY + opts.yOffset)
        context.stroke()
        context.closePath()
        context.lineCap = "butt"
    }
    context.save()
    if (opts._scrollDistance_ != null && opts._scrollDistance_ != 0) {
        context.translate(opts._scrollDistance_!!, 0)
    }
    if (opts.xAxis.calibration == true) {
        context.strokeStyle = opts.xAxis.gridColor ?: "#cccccc"
        context.lineCap = "butt"
        context.lineWidth = (1 * opts.pixelRatio)
        xAxisPoints.forEach(fun(item, index) {
            if (index > 0) {
                context.beginPath()
                context.moveTo(opts.xOffset + item - eachSpacing / 2, startY + opts.yOffset)
                context.lineTo(opts.xOffset + item - eachSpacing / 2, startY + 3 * opts.pixelRatio + opts.yOffset)
                context.closePath()
                context.stroke()
            }
        }
        )
    }
    if (opts.xAxis.disableGrid != true) {
        context.strokeStyle = opts.xAxis.gridColor ?: "#cccccc"
        context.lineCap = "butt"
        context.lineWidth = (1 * opts.pixelRatio)
        if (opts.xAxis.gridType == "dash") {
            context.setLineDash(_uA(
                opts.xAxis.dashLength * opts.pixelRatio,
                opts.xAxis.dashLength * opts.pixelRatio
            ))
        }
        opts.xAxis.gridEval = opts.xAxis.gridEval ?: 1
        xAxisPoints.forEach(fun(item, index) {
            if (index % opts.xAxis.gridEval == 0) {
                context.beginPath()
                context.moveTo(item + opts.xOffset, startY + opts.yOffset)
                context.lineTo(item + opts.xOffset, endY + opts.yOffset)
                context.stroke()
            }
        }
        )
        context.setLineDash(_uA())
    }
    if (opts.xAxis.disabled != true) {
        var maxXAxisListLength = categories.length
        if (opts.xAxis.labelCount != null) {
            if (opts.xAxis.itemCount != null) {
                maxXAxisListLength = Math.ceil(categories.length / opts.xAxis.itemCount * opts.xAxis.labelCount!!)
            } else {
                maxXAxisListLength = opts.xAxis.labelCount!!
            }
            maxXAxisListLength -= 1
        }
        var ratio = Math.ceil(categories.length / maxXAxisListLength)
        var newCategories: UTSArray<String> = _uA()
        var cgLength = categories.length
        run {
            var i: Number = 0
            while(i < cgLength){
                if (i % ratio != 0) {
                    newCategories.push("")
                } else {
                    newCategories.push(categories[i])
                }
                i++
            }
        }
        newCategories[cgLength - 1] = categories[cgLength - 1]
        var xAxisFontSize = if (opts.xAxis.fontSize != null) {
            opts.xAxis.fontSize * opts.pixelRatio
        } else {
            config.fontSize
        }
        if (config._xAxisTextAngle_ == 0) {
            newCategories.forEach(fun(item, index) {
                var xitem: String = item
                if (opts.xAxis.formatter != null) {
                    val kFun = opts.xAxis.formatter as (`val`: String, index: Number, opts: ChartOptions) -> String
                    xitem = kFun(item, index, opts)
                }
                var offset = -measureText("" + xitem, xAxisFontSize) / 2
                if (boundaryGap == "center") {
                    offset += eachSpacing / 2
                }
                var scrollHeight: Number = 0
                if (opts.xAxis.scrollShow) {
                    scrollHeight = 6 * opts.pixelRatio
                }
                var _scrollDistance_ = opts._scrollDistance_ ?: 0
                var truePoints = if (boundaryGap == "center") {
                    xAxisPoints[index] + eachSpacing / 2
                } else {
                    xAxisPoints[index]
                }
                if (xitem != "" && (truePoints - Math.abs(_scrollDistance_)) >= (opts.area[3] - 1) && (truePoints - Math.abs(_scrollDistance_)) <= (opts.width - opts.area[1] + 1)) {
                    context.beginPath()
                    context.font = "" + xAxisFontSize + "px sans-serif"
                    context.fillStyle = (opts.xAxis.fontColor ?: opts.fontColor)
                    context.fillText("" + xitem, opts.xOffset + xAxisPoints[index] + offset, opts.yOffset + startY + opts.xAxis.marginTop * opts.pixelRatio + (opts.xAxis.lineHeight - opts.xAxis.fontSize) * opts.pixelRatio / 2 + opts.xAxis.fontSize * opts.pixelRatio)
                    context.closePath()
                    context.stroke()
                }
            })
        } else {
            newCategories.forEach(fun(item, index) {
                var xitem: String = item
                if (opts.xAxis.formatter != null) {
                    val kFun = opts.xAxis.formatter as (`val`: String) -> String
                    xitem = kFun(item)
                }
                var _scrollDistance_ = opts._scrollDistance_ ?: 0
                var truePoints = if (boundaryGap == "center") {
                    xAxisPoints[index] + eachSpacing / 2
                } else {
                    xAxisPoints[index]
                }
                if (xitem != "" && (truePoints - Math.abs(_scrollDistance_)) >= (opts.area[3] - 1) && (truePoints - Math.abs(_scrollDistance_)) <= (opts.width - opts.area[1] + 1)) {
                    context.save()
                    context.beginPath()
                    context.font = "" + xAxisFontSize + "px sans-serif"
                    context.fillStyle = (opts.xAxis.fontColor ?: opts.fontColor)
                    var textWidth = measureText("" + xitem, xAxisFontSize)
                    var offsetX = xAxisPoints[index]
                    if (boundaryGap == "center") {
                        offsetX = xAxisPoints[index] + eachSpacing / 2
                    }
                    var scrollHeight: Number = 0
                    if (opts.xAxis.scrollShow) {
                        scrollHeight = 6 * opts.pixelRatio
                    }
                    var offsetY = startY + opts.xAxis.marginTop * opts.pixelRatio + xAxisFontSize - xAxisFontSize * Math.abs(Math.sin(config._xAxisTextAngle_))
                    if (opts.xAxis.rotateAngle < 0) {
                        offsetX -= xAxisFontSize / 2
                        textWidth = 0
                    } else {
                        offsetX += xAxisFontSize / 2
                        textWidth = -textWidth
                    }
                    context.translate(offsetX, offsetY)
                    context.rotate(-1 * config._xAxisTextAngle_)
                    context.fillText("" + xitem, textWidth + opts.xOffset, 0 + opts.yOffset)
                    context.closePath()
                    context.stroke()
                    context.restore()
                }
            }
            )
        }
    }
    context.restore()
    if (opts.xAxis.title != null) {
        context.beginPath()
        context.font = "" + opts.xAxis.titleFontSize * opts.pixelRatio + "px sans-serif"
        context.fillStyle = opts.xAxis.titleFontColor
        context.fillText("" + opts.xAxis.title!!, opts.xOffset + opts.width - opts.area[1] + opts.xAxis.titleOffsetX * opts.pixelRatio, opts.height - opts.area[2] + opts.xAxis.marginTop * opts.pixelRatio + (opts.xAxis.lineHeight - opts.xAxis.titleFontSize) * opts.pixelRatio / 2 + (opts.xAxis.titleFontSize + opts.xAxis.titleOffsetY) * opts.pixelRatio + opts.yOffset)
        context.closePath()
        context.stroke()
    }
    if (opts.xAxis.axisLine) {
        context.beginPath()
        context.strokeStyle = opts.xAxis.axisLineColor
        context.lineWidth = (1 * opts.pixelRatio)
        context.moveTo(startX + opts.xOffset, opts.height - opts.area[2] + opts.yOffset)
        context.lineTo(endX + opts.xOffset, opts.height - opts.area[2] + opts.yOffset)
        context.stroke()
    }
}
fun getColumnDataPoints(data: UTSArray<ChartOptionsSeriesData>, minRange: Number, maxRange: Number, xAxisPoints: UTSArray<Number>, eachSpacing: Number, opts: ChartOptions, config: TuiChartConfig, zeroPoints: Number, process: Number = 1): UTSArray<ChartOptionsChartDataCalPoints> {
    var points: UTSArray<ChartOptionsChartDataCalPoints> = _uA()
    var validHeight = opts.height - opts.area[0] - opts.area[2]
    var validWidth = opts.width - opts.area[1] - opts.area[3]
    data.forEach(fun(item, index) {
        if (item == null) {
        } else {
            val point = ChartOptionsChartDataCalPoints()
            point.color = item.color
            point.x = xAxisPoints[index]
            var value = item.value!!
            point.x += eachSpacing / 2
            var height = validHeight * (value * process - minRange) / (maxRange - minRange)
            point.y = opts.height - height - opts.area[2]
            points.push(point)
        }
    }
    )
    return points
}
fun getStackDataPoints(data: UTSArray<ChartOptionsSeriesData>, minRange: Number, maxRange: Number, xAxisPoints: UTSArray<Number>, eachSpacing: Number, opts: ChartOptions, config: TuiChartConfig, seriesIndex: Number, stackSeries: UTSArray<ChartOptionsSeries>, process: Number = 1): UTSArray<ChartOptionsChartDataCalPoints> {
    var points: UTSArray<ChartOptionsChartDataCalPoints> = _uA()
    var validHeight = opts.height - opts.area[0] - opts.area[2]
    data.forEach(fun(item: ChartOptionsSeriesData, index: Number) {
        if (item == null) {
        } else {
            var point = ChartOptionsChartDataCalPoints()
            point.color = item.color
            point.x = xAxisPoints[index] + Math.round(eachSpacing / 2)
            var height0: Number
            var height: Number
            if (seriesIndex > 0) {
                var value: Number = 0
                run {
                    var i: Number = 0
                    while(i <= seriesIndex){
                        val kVal = stackSeries[i].data!![index]!!.value!!
                        value += kVal
                        i++
                    }
                }
                var value0 = value - item.value!!
                height = validHeight * (value - minRange) / (maxRange - minRange)
                height0 = validHeight * (value0 - minRange) / (maxRange - minRange)
            } else {
                var value: Number = item.value!!
                height = validHeight * (value - minRange) / (maxRange - minRange)
                height0 = 0
            }
            var heightc = height0
            height *= process
            heightc *= process
            point.y = opts.height - Math.round(height) - opts.area[2]
            point.y0 = opts.height - Math.round(heightc) - opts.area[2]
            points.push(point)
        }
    }
    )
    return points
}
fun fixColumeData(points: UTSArray<ChartOptionsChartDataCalPoints>, eachSpacing: Number, columnLen: Number, index: Number, config: TuiChartConfig, opts: ChartOptions): UTSArray<ChartOptionsChartDataCalPoints> {
    return points.map(fun(item: ChartOptionsChartDataCalPoints): ChartOptionsChartDataCalPoints {
        var seriesGap: Number = 0
        var categoryGap: Number = 0
        if (opts.type == "mix") {
            seriesGap = (opts.extra.mix?.column?.seriesGap ?: 0) * opts.pixelRatio
            categoryGap = (opts.extra.mix?.column?.categoryGap ?: 0) * opts.pixelRatio
        } else {
            seriesGap = (opts.extra.column!!.seriesGap * opts.pixelRatio) ?: 0
            categoryGap = (opts.extra.column!!.categoryGap * opts.pixelRatio) ?: 0
        }
        seriesGap = Math.min(seriesGap, eachSpacing / columnLen)
        categoryGap = Math.min(categoryGap, eachSpacing / columnLen)
        item.width = Math.ceil((eachSpacing - 2 * categoryGap - seriesGap * (columnLen - 1)) / columnLen)
        if (opts.extra.mix != null && opts.extra.mix!!.column!!.width != null && opts.extra.mix!!.column!!.width!! > 0) {
            item.width = Math.min(item.width!!, opts.extra.mix!!.column!!.width!! * opts.pixelRatio)
        }
        if (opts.extra.column != null && opts.extra.column!!.width != null && opts.extra.column!!.width > 0) {
            item.width = Math.min(item.width!!, opts.extra.column!!.width * opts.pixelRatio)
        }
        if (item.width!! <= 0) {
            item.width = 1
        }
        item.x += (index + 0.5 - columnLen / 2) * (item.width!! + seriesGap)
        return item
    }
    )
}
fun drawColumePointText(points: UTSArray<ChartOptionsChartDataCalPoints>, series: ChartOptionsSeries, config: TuiChartConfig, context: CanvasRenderingContext2D, opts: ChartOptions) {
    var data: UTSArray<ChartOptionsSeriesData> = series.data!!
    var textOffset = if (series.textOffset != null) {
        series.textOffset!!
    } else {
        0
    }
    var Position = opts.extra.column!!.labelPosition
    points.forEach(fun(item, index) {
        if (item != null) {
            context.beginPath()
            var fontSize = if (series.textSize != null) {
                series.textSize!! * opts.pixelRatio
            } else {
                config.fontSize
            }
            context.font = "" + fontSize + "px sans-serif"
            context.fillStyle = (series.textColor ?: opts.fontColor)
            var value: Number = data[index].value!!
            var formatVal = value
            context.textAlign = "center"
            var startY = item.y - 4 * opts.pixelRatio + textOffset * opts.pixelRatio
            if (item.y > series.zeroPoints!!) {
                startY = item.y + textOffset * opts.pixelRatio + fontSize
            }
            if (Position == "insideTop") {
                startY = item.y + fontSize + textOffset * opts.pixelRatio
                if (item.y > series.zeroPoints!!) {
                    startY = item.y - textOffset * opts.pixelRatio - 4 * opts.pixelRatio
                }
            }
            if (Position == "center") {
                startY = item.y + textOffset * opts.pixelRatio + (opts.height - opts.area[2] - item.y + fontSize) / 2
                if (series.zeroPoints!! < opts.height - opts.area[2]) {
                    startY = item.y + textOffset * opts.pixelRatio + (series.zeroPoints!! - item.y + fontSize) / 2
                }
                if (item.y > series.zeroPoints!!) {
                    startY = item.y - textOffset * opts.pixelRatio - (item.y - series.zeroPoints!! - fontSize) / 2
                }
                if (opts.extra.column!!.type == "stack") {
                    startY = item.y + textOffset * opts.pixelRatio + (item.y0 - item.y + fontSize) / 2
                }
            }
            if (Position == "bottom") {
                startY = opts.height - opts.area[2] + textOffset * opts.pixelRatio - 4 * opts.pixelRatio
                if (series.zeroPoints!! < opts.height - opts.area[2]) {
                    startY = series.zeroPoints!! + textOffset * opts.pixelRatio - 4 * opts.pixelRatio
                }
                if (item.y > series.zeroPoints!!) {
                    startY = series.zeroPoints!! - textOffset * opts.pixelRatio + fontSize + 2 * opts.pixelRatio
                }
                if (opts.extra.column!!.type == "stack") {
                    startY = item.y0 + textOffset * opts.pixelRatio - 4 * opts.pixelRatio
                }
            }
            context.fillText("" + formatVal, item.x + opts.xOffset, startY + opts.yOffset)
            context.closePath()
            context.stroke()
            context.textAlign = "left"
        }
    }
    )
}
fun drawToolTipSplitArea(offsetX: Number, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, eachSpacing: Number) {
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    var toolTipOption = opts.extra.column!!
    if (toolTipOption.activeWidth == -1) {
        toolTipOption.activeWidth = eachSpacing
    }
    toolTipOption.activeWidth = if (toolTipOption.activeWidth > eachSpacing) {
        eachSpacing
    } else {
        toolTipOption.activeWidth
    }
    var startY = opts.area[0]
    var endY = opts.height - opts.area[2]
    context.beginPath()
    context.fillStyle = hexToRgb(toolTipOption.activeBgColor, toolTipOption.activeBgOpacity)
    context.rect(offsetX - toolTipOption.activeWidth / 2 + xOffset, startY + yOffset, toolTipOption.activeWidth + xOffset, endY - startY)
    context.closePath()
    context.fill()
    context.fillStyle = "#FFFFFF"
}
fun fixColumeMeterData(points: UTSArray<ChartOptionsChartDataCalPoints>, eachSpacing: Number, columnLen: Number, index: Number, config: TuiChartConfig, opts: ChartOptions, border: Number): UTSArray<ChartOptionsChartDataCalPoints> {
    var categoryGap: Number = (opts.extra.column!!.categoryGap * opts.pixelRatio) ?: 0
    return points.map(fun(item: ChartOptionsChartDataCalPoints): ChartOptionsChartDataCalPoints {
        item.width = eachSpacing - 2 * categoryGap
        if (opts.extra.column != null && +opts.extra.column!!.width > 0) {
            item.width = Math.min(item.width!!, +opts.extra.column!!.width * opts.pixelRatio)
        }
        if (index > 0) {
            item.width = item.width!! - border
        }
        return item
    }
    )
}
fun getArrDataPoints(data: UTSArray<UTSArray<Number>>, minRange: Number, maxRange: Number, xAxisPoints: UTSArray<Number>, eachSpacing: Number, opts: ChartOptions, config: TuiChartConfig, process: Number = 1): UTSArray<ChartOptionsChartDataCalPoints> {
    var boundaryGap = opts.xAxis.boundaryGap
    var points: UTSArray<ChartOptionsChartDataCalPoints> = _uA()
    var validHeight = opts.height - opts.area[0] - opts.area[2]
    var validWidth = opts.width - opts.area[1] - opts.area[3]
    data.forEach(fun(item, index){
        val point = ChartOptionsChartDataCalPoints()
        val xranges = opts.chartData.xAxisData.ranges!!.slice()
        val xminRange = xranges.shift()!!
        val xmaxRange = xranges.pop()!!
        var value = item[1]
        point.x = opts.area[3] + validWidth * (item[0] - xminRange) / (xmaxRange - xminRange)
        if (opts.type == "bubble") {
            point.r = item[2]
            point.t = item[3]
        }
        if (boundaryGap == "center") {
            point.x += eachSpacing / 2
        }
        var height = validHeight * (value - minRange) / (maxRange - minRange)
        height *= process
        point.y = opts.height - height - opts.area[2]
        points.push(point)
    }
    )
    return points
}
fun getDataPoints(data: UTSArray<ChartOptionsSeriesData>, minRange: Number, maxRange: Number, xAxisPoints: UTSArray<Number>, eachSpacing: Number, opts: ChartOptions, config: TuiChartConfig, process: Number = 1): UTSArray<ChartOptionsChartDataCalPoints> {
    var boundaryGap = "center"
    if (opts.type == "line" || opts.type == "area" || opts.type == "bubble") {
        boundaryGap = opts.xAxis.boundaryGap
    }
    var points: UTSArray<ChartOptionsChartDataCalPoints> = _uA()
    var validHeight = opts.height - opts.area[0] - opts.area[2]
    var validWidth = opts.width - opts.area[1] - opts.area[3]
    data.forEach(fun(item, index){
        if (item.value == null) {
        } else {
            val point = ChartOptionsChartDataCalPoints()
            point.color = item.color
            point.x = xAxisPoints[index]
            var value: Number = item.value!!
            if (boundaryGap == "center") {
                point.x += eachSpacing / 2
            }
            var height = validHeight * (value - minRange) / (maxRange - minRange)
            height *= process
            point.y = opts.height - height - opts.area[2]
            points.push(point)
        }
    }
    )
    return points
}
fun drawColumnDataPoints(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartColumnDataPoints {
    var xAxisData = opts.chartData.xAxisData
    var xAxisPoints = xAxisData.xAxisPoints!!
    var eachSpacing = xAxisData.eachSpacing!!
    var columnOption = opts.extra.column!!
    var calPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = _uA()
    context.save()
    var leftNum: Number = -2
    var rightNum = xAxisPoints.length + 2
    if (opts._scrollDistance_ != null && opts._scrollDistance_ != 0 && opts.enableScroll == true) {
        context.translate(opts._scrollDistance_!!, 0)
        leftNum = Math.floor(-opts._scrollDistance_!! / eachSpacing) - 2
        rightNum = leftNum + opts.xAxis.itemCount + 4
    }
    if (opts.tooltip != null && opts.tooltip!!.textList.length > 0 && process == 1) {
        drawToolTipSplitArea(opts.tooltip!!.offset.x, opts, config, context, eachSpacing)
    }
    columnOption.customColor = fillCustomColor(columnOption.linearType, columnOption.customColor, series, config)
    series.forEach(fun(eachSeries, seriesIndex) {
        var ranges: UTSArray<String> = opts.chartData.yAxisData.ranges!![eachSeries.index!!].slice()
        var minRange: Number = parseFloat(ranges.pop()!!)
        var maxRange: Number = parseFloat(ranges.shift()!!)
        var spacingValid = opts.height - opts.area[0] - opts.area[2]
        var zeroHeight = spacingValid * (0 - minRange) / (maxRange - minRange)
        var zeroPoints = opts.height - Math.round(zeroHeight) - opts.area[2]
        eachSeries.zeroPoints = zeroPoints
        var data = eachSeries.data!!
        var points: UTSArray<ChartOptionsChartDataCalPoints>
        when (columnOption.type) {
            "group" -> 
                {
                    points = getColumnDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, zeroPoints, process)
                    var tooltipPoints = getStackDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, seriesIndex, series, process)
                    calPoints.push(tooltipPoints)
                    points = fixColumeData(points, eachSpacing, series.length, seriesIndex, config, opts)
                    run {
                        var i: Number = 0
                        while(i < points.length){
                            var item = points[i]
                            if (item != null && i > leftNum && i < rightNum) {
                                var startX = item.x - item.width!! / 2
                                var height = opts.height - item.y - opts.area[2]
                                context.beginPath()
                                var fillColor: Any = item.color ?: eachSeries.color!!
                                var strokeColor = item.color ?: eachSeries.color!!
                                if (columnOption.linearType != "none") {
                                    var grd: CanvasGradient = context.createLinearGradient(startX + opts.xOffset, item.y + opts.yOffset, startX + opts.xOffset, zeroPoints + opts.yOffset)
                                    if (columnOption.linearType == "opacity") {
                                        grd.addColorStop(0, hexToRgb("" + fillColor, columnOption.linearOpacity))
                                        grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                                    } else {
                                        grd.addColorStop(0, hexToRgb(columnOption.customColor[eachSeries.linearIndex!!], columnOption.linearOpacity))
                                        grd.addColorStop(columnOption.colorStop, hexToRgb(columnOption.customColor[eachSeries.linearIndex!!], columnOption.linearOpacity))
                                        grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                                    }
                                    fillColor = grd
                                }
                                if ((columnOption.barBorderRadius.length == 4) || columnOption.barBorderCircle == true) {
                                    val left = startX
                                    val top = if (item.y > zeroPoints) {
                                        zeroPoints
                                    } else {
                                        item.y
                                    }
                                    val width = item.width!!
                                    val height = Math.abs(zeroPoints - item.y)
                                    if (columnOption.barBorderCircle) {
                                        columnOption.barBorderRadius = _uA(
                                            width / 2,
                                            width / 2,
                                            0,
                                            0
                                        )
                                    }
                                    if (item.y > zeroPoints) {
                                        columnOption.barBorderRadius = _uA(
                                            0,
                                            0,
                                            width / 2,
                                            width / 2
                                        )
                                    }
                                    var r0 = columnOption.barBorderRadius[0]
                                    var r1 = columnOption.barBorderRadius[1]
                                    var r2 = columnOption.barBorderRadius[2]
                                    var r3 = columnOption.barBorderRadius[3]
                                    var minRadius = Math.min(width / 2, height / 2)
                                    r0 = if (r0 > minRadius) {
                                        minRadius
                                    } else {
                                        r0
                                    }
                                    r1 = if (r1 > minRadius) {
                                        minRadius
                                    } else {
                                        r1
                                    }
                                    r2 = if (r2 > minRadius) {
                                        minRadius
                                    } else {
                                        r2
                                    }
                                    r3 = if (r3 > minRadius) {
                                        minRadius
                                    } else {
                                        r3
                                    }
                                    r0 = if (r0 < 0) {
                                        0
                                    } else {
                                        r0
                                    }
                                    r1 = if (r1 < 0) {
                                        0
                                    } else {
                                        r1
                                    }
                                    r2 = if (r2 < 0) {
                                        0
                                    } else {
                                        r2
                                    }
                                    r3 = if (r3 < 0) {
                                        0
                                    } else {
                                        r3
                                    }
                                    context.arc(opts.xOffset + left + r0, opts.yOffset + top + r0, r0, -Math.PI, -Math.PI / 2, false)
                                    context.arc(opts.xOffset + left + width - r1, opts.yOffset + top + r1, r1, -Math.PI / 2, 0, false)
                                    context.arc(opts.xOffset + left + width - r2, opts.yOffset + top + height - r2, r2, 0, Math.PI / 2, false)
                                    context.arc(opts.xOffset + left + r3, opts.yOffset + top + height - r3, r3, Math.PI / 2, Math.PI, false)
                                } else {
                                    context.moveTo(opts.xOffset + startX, item.y + opts.yOffset)
                                    context.lineTo(opts.xOffset + startX + item.width!!, item.y + opts.yOffset)
                                    context.lineTo(opts.xOffset + startX + item.width!!, zeroPoints + opts.yOffset)
                                    context.lineTo(opts.xOffset + startX, zeroPoints + opts.yOffset)
                                    context.lineTo(opts.xOffset + startX, item.y + opts.yOffset)
                                    context.lineWidth = 1
                                    context.strokeStyle = strokeColor
                                }
                                context.fillStyle = fillColor
                                context.closePath()
                                context.fill()
                            }
                            i++
                        }
                    }
                }
            "stack" -> 
                {
                    points = getStackDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, seriesIndex, series, process)
                    calPoints.push(points)
                    points = fixColumeStackData(points, eachSpacing, opts)
                    run {
                        var i: Number = 0
                        while(i < points.length){
                            var item = points[i]
                            if (item != null && i > leftNum && i < rightNum) {
                                context.beginPath()
                                var fillColor = item.color ?: eachSeries.color!!
                                var startX = item.x - item.width!! / 2 + 1
                                var height = opts.height - item.y - opts.area[2]
                                var height0 = opts.height - item.y0 - opts.area[2]
                                if (seriesIndex > 0) {
                                    height -= height0
                                }
                                context.fillStyle = fillColor
                                context.moveTo(opts.xOffset + startX, opts.yOffset + item.y)
                                context.fillRect(opts.xOffset + startX, opts.yOffset + item.y, item.width!!, height)
                                context.closePath()
                                context.fill()
                            }
                            i++
                        }
                    }
                }
            "meter" -> 
                {
                    points = getDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
                    calPoints.push(points)
                    points = fixColumeMeterData(points, eachSpacing, series.length, seriesIndex, config, opts, columnOption.meterBorder)
                    run {
                        var i: Number = 0
                        while(i < points.length){
                            var item = points[i]
                            if (item != null && i > leftNum && i < rightNum) {
                                context.beginPath()
                                if (seriesIndex == 0 && columnOption.meterBorder > 0) {
                                    context.strokeStyle = eachSeries.color!!
                                    context.lineWidth = (columnOption.meterBorder * opts.pixelRatio)
                                }
                                if (seriesIndex == 0) {
                                    context.fillStyle = columnOption.meterFillColor
                                } else {
                                    context.fillStyle = item.color ?: eachSeries.color!!
                                }
                                var startX = item.x - item.width!! / 2
                                var height = opts.height - item.y - opts.area[2]
                                if ((columnOption.barBorderRadius.length == 4) || columnOption.barBorderCircle == true) {
                                    val left = startX
                                    val top = item.y
                                    val width = item.width!!
                                    val height = zeroPoints - item.y
                                    if (columnOption.barBorderCircle) {
                                        columnOption.barBorderRadius = _uA(
                                            width / 2,
                                            width / 2,
                                            0,
                                            0
                                        )
                                    }
                                    var r0: Number = columnOption.barBorderRadius[0]
                                    var r1: Number = columnOption.barBorderRadius[1]
                                    var r2: Number = columnOption.barBorderRadius[2]
                                    var r3: Number = columnOption.barBorderRadius[3]
                                    var minRadius = Math.min(width / 2, height / 2)
                                    r0 = if (r0 > minRadius) {
                                        minRadius
                                    } else {
                                        r0
                                    }
                                    r1 = if (r1 > minRadius) {
                                        minRadius
                                    } else {
                                        r1
                                    }
                                    r2 = if (r2 > minRadius) {
                                        minRadius
                                    } else {
                                        r2
                                    }
                                    r3 = if (r3 > minRadius) {
                                        minRadius
                                    } else {
                                        r3
                                    }
                                    r0 = if (r0 < 0) {
                                        0
                                    } else {
                                        r0
                                    }
                                    r1 = if (r1 < 0) {
                                        0
                                    } else {
                                        r1
                                    }
                                    r2 = if (r2 < 0) {
                                        0
                                    } else {
                                        r2
                                    }
                                    r3 = if (r3 < 0) {
                                        0
                                    } else {
                                        r3
                                    }
                                    context.arc(opts.xOffset + left + r0, opts.yOffset + top + r0, r0, -Math.PI, -Math.PI / 2)
                                    context.arc(opts.xOffset + left + width - r1, opts.yOffset + top + r1, r1, -Math.PI / 2, 0)
                                    context.arc(opts.xOffset + left + width - r2, opts.yOffset + top + height - r2, r2, 0, Math.PI / 2)
                                    context.arc(opts.xOffset + left + r3, opts.yOffset + top + height - r3, r3, Math.PI / 2, Math.PI)
                                    context.fill()
                                } else {
                                    context.moveTo(opts.xOffset + startX, item.y + opts.yOffset)
                                    context.lineTo(opts.xOffset + startX + item.width!!, item.y + opts.yOffset)
                                    context.lineTo(opts.xOffset + startX + item.width!!, zeroPoints + opts.yOffset)
                                    context.lineTo(opts.xOffset + startX, zeroPoints + opts.yOffset)
                                    context.lineTo(opts.xOffset + startX, item.y + opts.yOffset)
                                    context.fill()
                                }
                                if (seriesIndex == 0 && columnOption.meterBorder > 0) {
                                    context.closePath()
                                    context.stroke()
                                }
                            }
                            i++
                        }
                    }
                }
        }
    }
    )
    if (opts.dataLabel != false && process == 1) {
        series.forEach(fun(eachSeries, seriesIndex) {
            var ranges: UTSArray<String> = opts.chartData.yAxisData.ranges!![eachSeries.index!!].slice()
            var minRange: Number = parseFloat(ranges.pop()!!)
            var maxRange: Number = parseFloat(ranges.shift()!!)
            var data = eachSeries.data!!
            var points: UTSArray<ChartOptionsChartDataCalPoints>
            when (columnOption.type) {
                "group" -> 
                    {
                        points = getColumnDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
                        points = fixColumeData(points, eachSpacing, series.length, seriesIndex, config, opts)
                        drawColumePointText(points, eachSeries, config, context, opts)
                    }
                "stack" -> 
                    {
                        points = getStackDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, seriesIndex, series, process)
                        drawColumePointText(points, eachSeries, config, context, opts)
                    }
                "meter" -> 
                    {
                        points = getDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
                        drawColumePointText(points, eachSeries, config, context, opts)
                    }
            }
        }
        )
    }
    context.restore()
    return ChartColumnDataPoints(yAxisPoints = _uA<Number>(), xAxisPoints = xAxisPoints, calPoints = calPoints, eachSpacing = eachSpacing)
}
fun drawLegend(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, chartData: ChartOptionsChartData) {
    if (opts.legend.show == false) {
        return
    }
    var legendData = chartData.legendData
    var legendList = legendData.points
    var legendArea = legendData.area
    var padding = opts.legend.padding * opts.pixelRatio
    var fontSize = opts.legend.fontSize * opts.pixelRatio
    var shapeWidth = 15 * opts.pixelRatio
    var shapeRight = 5 * opts.pixelRatio
    var itemGap = opts.legend.itemGap * opts.pixelRatio
    var lineHeight = Math.max(opts.legend.lineHeight * opts.pixelRatio, fontSize)
    context.beginPath()
    context.lineWidth = (opts.legend.borderWidth!! * opts.pixelRatio)
    context.strokeStyle = opts.legend.borderColor
    context.fillStyle = opts.legend.backgroundColor
    context.moveTo(opts.xOffset + legendArea.start.x, opts.yOffset + legendArea.start.y)
    context.rect(legendArea.start.x + opts.xOffset, legendArea.start.y + opts.yOffset, legendArea.width, legendArea.height)
    context.closePath()
    context.fill()
    context.stroke()
    legendList.forEach(fun(itemList, listIndex) {
        var width: Number = 0
        var height: Number = 0
        if (listIndex < legendData.widthArr.length) {
            width = legendData.widthArr[listIndex]
        }
        if (listIndex < legendData.heightArr.length) {
            height = legendData.heightArr[listIndex]
        }
        var startX: Number = 0
        var startY: Number = 0
        if (opts.legend.position == "top" || opts.legend.position == "bottom") {
            when (opts.legend.float) {
                "left" -> 
                    startX = legendArea.start.x + padding
                "right" -> 
                    startX = legendArea.start.x + legendArea.width - width
                else -> 
                    startX = legendArea.start.x + (legendArea.width - width) / 2
            }
            startY = legendArea.start.y + padding + listIndex * lineHeight
        } else {
            if (listIndex == 0) {
                width = 0
            } else {
                width = legendData.widthArr[listIndex - 1]
            }
            startX = legendArea.start.x + padding + width
            startY = legendArea.start.y + padding + (legendArea.height - height) / 2
        }
        context.font = "" + config.fontSize + "px sans-serif"
        run {
            var i: Number = 0
            while(i < itemList.length){
                var item = itemList[i]
                item.area = _uA(
                    0,
                    0,
                    0,
                    0
                )
                item.area!![0] = startX
                item.area!![1] = startY
                item.area!![3] = startY + lineHeight
                context.beginPath()
                context.lineWidth = (1 * opts.pixelRatio)
                context.strokeStyle = if (item.show) {
                    item.color!!
                } else {
                    opts.legend.hiddenColor
                }
                context.fillStyle = if (item.show) {
                    item.color!!
                } else {
                    opts.legend.hiddenColor
                }
                when (item.legendShape) {
                    "line" -> 
                        {
                            context.moveTo(opts.xOffset + startX, opts.yOffset + startY + 0.5 * lineHeight - 2 * opts.pixelRatio)
                            context.fillRect(opts.xOffset + startX, opts.yOffset + startY + 0.5 * lineHeight - 2 * opts.pixelRatio, 15 * opts.pixelRatio, 4 * opts.pixelRatio)
                        }
                    "triangle" -> 
                        {
                            context.moveTo(opts.xOffset + startX + 7.5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight - 5 * opts.pixelRatio)
                            context.lineTo(opts.xOffset + startX + 2.5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight + 5 * opts.pixelRatio)
                            context.lineTo(opts.xOffset + startX + 12.5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight + 5 * opts.pixelRatio)
                            context.lineTo(opts.xOffset + startX + 7.5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight - 5 * opts.pixelRatio)
                        }
                    "diamond" -> 
                        {
                            context.moveTo(opts.xOffset + startX + 7.5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight - 5 * opts.pixelRatio)
                            context.lineTo(opts.xOffset + startX + 2.5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight)
                            context.lineTo(opts.xOffset + startX + 7.5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight + 5 * opts.pixelRatio)
                            context.lineTo(opts.xOffset + startX + 12.5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight)
                            context.lineTo(opts.xOffset + startX + 7.5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight - 5 * opts.pixelRatio)
                        }
                    "circle" -> 
                        {
                            context.moveTo(opts.xOffset + startX + 7.5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight)
                            context.arc(opts.xOffset + startX + 7.5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight, 5 * opts.pixelRatio, 0, 2 * Math.PI)
                        }
                    "rect" -> 
                        {
                            context.moveTo(opts.xOffset + startX, opts.yOffset + startY + 0.5 * lineHeight - 5 * opts.pixelRatio)
                            context.fillRect(opts.xOffset + startX, opts.yOffset + startY + 0.5 * lineHeight - 5 * opts.pixelRatio, 15 * opts.pixelRatio, 10 * opts.pixelRatio)
                        }
                    "square" -> 
                        {
                            context.moveTo(opts.xOffset + startX + 5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight - 5 * opts.pixelRatio)
                            context.fillRect(opts.xOffset + startX + 5 * opts.pixelRatio, opts.yOffset + startY + 0.5 * lineHeight - 5 * opts.pixelRatio, 10 * opts.pixelRatio, 10 * opts.pixelRatio)
                        }
                    "none" -> 
                        {}
                    else -> 
                        {
                            context.moveTo(opts.xOffset + startX, opts.yOffset + startY + 0.5 * lineHeight - 5 * opts.pixelRatio)
                            context.fillRect(opts.xOffset + startX, opts.yOffset + startY + 0.5 * lineHeight - 5 * opts.pixelRatio, 15 * opts.pixelRatio, 10 * opts.pixelRatio)
                        }
                }
                context.closePath()
                context.fill()
                context.stroke()
                startX += shapeWidth + shapeRight
                var fontTrans = 0.5 * lineHeight + 0.5 * fontSize - 2
                val legendText = item.legendText ?: item.name!!
                context.beginPath()
                context.font = "" + fontSize + "px sans-serif"
                context.fillStyle = if (item.show) {
                    opts.legend.fontColor
                } else {
                    opts.legend.hiddenColor
                }
                context.fillText(legendText, startX + opts.xOffset, opts.yOffset + startY + fontTrans)
                context.closePath()
                context.stroke()
                if (opts.legend.position == "top" || opts.legend.position == "bottom") {
                    startX += measureText(legendText, fontSize) + itemGap
                    item.area!![2] = startX
                } else {
                    item.area!![2] = startX + measureText(legendText, fontSize) + itemGap
                    startX -= shapeWidth + shapeRight
                    startY += lineHeight
                }
                i++
            }
        }
    }
    )
}
fun calTooltipYAxisData(point: Number, series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, eachSpacing: Number): UTSArray<String> {
    var ranges = opts.chartData.yAxisData.ranges!!.slice()
    var spacingValid = opts.height - opts.area[0] - opts.area[2]
    var minAxis = opts.area[0]
    var items: UTSArray<String> = _uA()
    run {
        var i: Number = 0
        while(i < ranges.length){
            val numberRanges = ranges[i].map(fun(item: String): Number {
                return parseFloat(item)
            }
            )
            var maxVal = Math.max(*numberRanges.toTypedArray())
            var minVal = Math.min(*numberRanges.toTypedArray())
            var item = maxVal - (maxVal - minVal) * (point - minAxis) / spacingValid
            var kVal: String = ""
            if (opts.yAxis.data != null) {
                if (opts.yAxis.data[i].formatter != null) {
                    val kFun = opts.yAxis.data[i].formatter as (item: Number, i: Number, opts: ChartOptions) -> String
                    kVal = kFun(item, i, opts)
                } else {
                    kVal = item.toFixed(0)
                }
            }
            items.push(kVal)
            i++
        }
    }
    return items
}
fun drawToolTipHorizentalLine(opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, eachSpacing: Number, xAxisPoints: UTSArray<Number>) {
    var toolTipOption = opts.extra.tooltip!!
    var startX = opts.area[3]
    var endX = opts.width - opts.area[1]
    if (toolTipOption.gridType == "dash") {
        context.setLineDash(_uA(
            toolTipOption.dashLength,
            toolTipOption.dashLength
        ))
    }
    context.strokeStyle = toolTipOption.gridColor
    context.lineWidth = 1 * opts.pixelRatio
    context.beginPath()
    context.moveTo(startX, opts.tooltip!!.offset.y)
    context.lineTo(endX, opts.tooltip!!.offset.y)
    context.stroke()
    context.setLineDash(_uA())
    if (toolTipOption.yAxisLabel) {
        var boxPadding = toolTipOption.boxPadding * opts.pixelRatio
        var labelText = calTooltipYAxisData(opts.tooltip!!.offset.y, opts.series!!, opts, config, eachSpacing)
        var widthArr = opts.chartData.yAxisData.yAxisWidth!!
        var tStartLeft = opts.area[3]
        var tStartRight = opts.width - opts.area[1]
        run {
            var i: Number = 0
            while(i < labelText.length){
                val fontsize = toolTipOption.fontSize * opts.pixelRatio
                context.font = "" + fontsize + "px sans-serif"
                var textWidth = measureText(labelText[i], fontsize)
                var bgStartX: Number = 0
                var bgEndX: Number = 0
                var bgWidth: Number = 0
                if (widthArr[i].position == "left") {
                    bgStartX = tStartLeft - (textWidth + boxPadding * 2) - 2 * opts.pixelRatio
                    bgEndX = Math.max(bgStartX, bgStartX + textWidth + boxPadding * 2)
                } else {
                    bgStartX = tStartRight + 2 * opts.pixelRatio
                    bgEndX = Math.max(bgStartX + widthArr[i].width, bgStartX + textWidth + boxPadding * 2)
                }
                bgWidth = bgEndX - bgStartX
                var textX = bgStartX + (bgWidth - textWidth) / 2
                var textY = opts.tooltip!!.offset.y
                context.beginPath()
                context.fillStyle = hexToRgb(toolTipOption.labelBgColor, toolTipOption.labelBgOpacity)
                context.strokeStyle = toolTipOption.labelBgColor
                context.lineWidth = (1 * opts.pixelRatio)
                context.rect(bgStartX, textY - 0.5 * config.fontSize - boxPadding, bgWidth, config.fontSize + 2 * boxPadding)
                context.closePath()
                context.stroke()
                context.fill()
                context.beginPath()
                context.font = "" + config.fontSize + "px sans-serif"
                context.fillStyle = toolTipOption.labelFontColor
                context.fillText(labelText[i], textX, textY + 0.5 * config.fontSize)
                context.closePath()
                context.stroke()
                if (widthArr[i].position == "left") {
                    tStartLeft -= (widthArr[i].width + opts.yAxis.padding * opts.pixelRatio)
                } else {
                    tStartRight += widthArr[i].width + opts.yAxis.padding * opts.pixelRatio
                }
                i++
            }
        }
    }
}
fun drawToolTipBridge(opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number, eachSpacing: Number, xAxisPoints: UTSArray<Number>) {
    var toolTipOption = opts.extra.tooltip ?: ChartOptionsExtraTooltip()
    if (toolTipOption.horizentalLine && opts.tooltip != null && process == 1 && (opts.type == "line" || opts.type == "area" || opts.type == "column" || opts.type == "mount" || opts.type == "candle" || opts.type == "mix")) {
        drawToolTipHorizentalLine(opts, config, context, eachSpacing, xAxisPoints)
    }
    context.save()
    if (opts._scrollDistance_ != null && opts._scrollDistance_ != 0 && opts.enableScroll == true) {
        context.translate(opts._scrollDistance_!!, 0)
    }
    if (opts.tooltip != null && opts.tooltip!!.textList.length > 0 && process == 1) {
        drawToolTip(opts.tooltip!!.textList, opts.tooltip!!.offset, opts, config, context)
    }
    context.restore()
}
fun drawCanvas(opts: ChartOptions, context: CanvasRenderingContext2D) {
    context.save()
    context.translate(0, 0.5)
    context.restore()
}
fun calMarkLineData(points: UTSArray<ChartOptionsExtraMarkLineData>, opts: ChartOptions): UTSArray<ChartOptionsExtraMarkLineData> {
    var minRange: Number
    var maxRange: Number
    var spacingValid = opts.height - opts.area[0] - opts.area[2]
    run {
        var i: Number = 0
        while(i < points.length){
            points[i].yAxisIndex = points[i].yAxisIndex ?: 0
            var range = opts.chartData.yAxisData.ranges!![points[i].yAxisIndex!!].slice()
            minRange = parseFloat(range.pop()!!)
            maxRange = parseFloat(range.shift()!!)
            var height = spacingValid * (points[i].value!! - minRange) / (maxRange - minRange)
            points[i].y = opts.height - Math.round(height) - opts.area[2]
            i++
        }
    }
    return points
}
fun drawMarkLine(opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D) {
    val markLineOption = opts.extra.markLine!!
    var startX = opts.area[3]
    var endX = opts.width - opts.area[1]
    var points = calMarkLineData(markLineOption.data, opts)
    run {
        var i: Number = 0
        while(i < points.length){
            var item = points[i]
            if (markLineOption.type == "dash") {
                context.setLineDash(_uA(
                    markLineOption.dashLength,
                    markLineOption.dashLength
                ))
            }
            context.strokeStyle = item.lineColor
            context.lineWidth = (1 * opts.pixelRatio)
            context.beginPath()
            context.moveTo(opts.xOffset + startX, opts.yOffset + item.y!!)
            context.lineTo(opts.xOffset + endX, opts.yOffset + item.y!!)
            context.stroke()
            context.setLineDash(_uA())
            if (item.showLabel) {
                var fontSize = item.labelFontSize * opts.pixelRatio
                var labelText = item.labelText ?: item.value!!
                context.font = "" + fontSize + "px sans-serif"
                var textWidth = measureText("" + labelText, fontSize)
                var bgWidth = textWidth + item.labelPadding * opts.pixelRatio * 2
                var bgStartX = if (item.labelAlign == "left") {
                    opts.area[3] - bgWidth
                } else {
                    opts.width - opts.area[1]
                }
                bgStartX += item.labelOffsetX
                var bgStartY = item.y!! - 0.5 * fontSize - item.labelPadding * opts.pixelRatio
                bgStartY += item.labelOffsetY
                var textX = bgStartX + item.labelPadding * opts.pixelRatio
                var textY = item.y
                context.fillStyle = hexToRgb(item.labelBgColor, item.labelBgOpacity)
                context.strokeStyle = item.labelBgColor
                context.lineWidth = (1 * opts.pixelRatio)
                context.beginPath()
                context.rect(bgStartX + opts.xOffset, bgStartY + opts.yOffset, bgWidth, fontSize + 2 * item.labelPadding * opts.pixelRatio)
                context.closePath()
                context.stroke()
                context.fill()
                context.font = "" + fontSize + "px sans-serif"
                context.textAlign = "left"
                context.fillStyle = item.labelFontColor
                context.fillText("" + labelText, opts.xOffset + textX, opts.yOffset + bgStartY + fontSize + item.labelPadding * opts.pixelRatio / 2)
                context.stroke()
                context.textAlign = "left"
            }
            i++
        }
    }
}
fun contextRotate(context: CanvasRenderingContext2D, opts: ChartOptions) {
    if (opts.rotateLock != true) {
        context.translate(opts.height, 0)
        context.rotate(90 * Math.PI / 180)
    } else if (opts._rotate_ != true) {
        context.translate(opts.height, 0)
        context.rotate(90 * Math.PI / 180)
        opts._rotate_ = true
    }
}
open class Animation : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("Animation", "uni_modules/tui-xechars/libs/util.uts", 2408, 14)
    }
    open var animationFrameId: Number = 0
    open var isStop: Boolean = false
    open var duration: Number
    open var timing: String
    open var delay: Number = 17
    open var startTimeStamp: Number = 0
    open var opts: ChartAnimationOption
    open var canvasContext: CanvasContext
    constructor(opts: ChartAnimationOption){
        this.canvasContext = opts.canvasContext
        this.duration = opts.duration ?: 1000
        this.timing = opts.timing ?: "easeInOut"
        this.delay = 17
        this.opts = opts
        this.startTimeStamp = Date().getTime()
        this.step()
    }
    open fun step() {
        if (this.isStop == true) {
            if (this.animationFrameId != 0) {
                this.canvasContext.cancelAnimationFrame(this.animationFrameId)
            }
            this.opts.onProcess(1)
            this.opts.onAnimationFinish()
            return
        }
        val curdata = Date().getTime()
        if (curdata - this.startTimeStamp < this.duration) {
            var process = (curdata - this.startTimeStamp) / this.duration
            when (this.timing) {
                "easeIn" -> 
                    process = Timing.easeIn(process)
                "easeOut" -> 
                    process = Timing.easeOut(process)
                "easeInOut" -> 
                    process = Timing.easeInOut(process)
                "linear" -> 
                    process = Timing.linear(process)
                else -> 
                    {}
            }
            this.opts.onProcess(process)
            if (this.animationFrameId != 0) {
                this.canvasContext.cancelAnimationFrame(this.animationFrameId)
            }
            this.animationFrameId = this.canvasContext.requestAnimationFrame(fun(_: Number){
                this.step()
            })
        } else {
            if (this.animationFrameId != 0) {
                this.canvasContext.cancelAnimationFrame(this.animationFrameId)
            }
            this.opts.onProcess(1)
            this.opts.onAnimationFinish()
        }
    }
    open fun stop() {
        this.isStop = true
    }
}
fun getMountDataPoints(series: UTSArray<ChartOptionsSeries>, minRange: Number, maxRange: Number, xAxisPoints: UTSArray<Number>, eachSpacing: Number, opts: ChartOptions, mountOption: ChartOptionsExtraMount, zeroPoints: Number, process: Number = 1): UTSArray<ChartOptionsChartDataCalPoints> {
    var points: UTSArray<ChartOptionsChartDataCalPoints> = _uA()
    var validHeight = opts.height - opts.area[0] - opts.area[2]
    var validWidth = opts.width - opts.area[1] - opts.area[3]
    var mountWidth = eachSpacing * mountOption.widthRatio
    series.forEach(fun(item, index){
        if (item == null) {
        } else {
            var point = ChartOptionsChartDataCalPoints()
            point.color = item.color
            point.x = xAxisPoints[index]
            point.x += eachSpacing / 2
            var value = item.data!![0].value!!
            var height = validHeight * (value * process - minRange) / (maxRange - minRange)
            point.y = opts.height - height - opts.area[2]
            point.value = value
            point.width = mountWidth
            points.push(point)
        }
    }
    )
    return points
}
fun drawMountPointText(points: UTSArray<ChartOptionsChartDataCalPoints>, series: UTSArray<ChartOptionsSeries>, config: TuiChartConfig, context: CanvasRenderingContext2D, opts: ChartOptions, zeroPoints: Number) {
    points.forEach(fun(item, index) {
        if (item != null) {
            var itemseries: ChartOptionsSeries = series[index]
            var textOffset = itemseries.textOffset ?: 0
            context.beginPath()
            var fontSize = if (itemseries.textSize != null) {
                itemseries.textSize!! * opts.pixelRatio
            } else {
                config.fontSize
            }
            context.font = "" + fontSize + "px sans-serif"
            context.fillStyle = itemseries.textColor ?: opts.fontColor
            var value = item.value
            var formatVal: String = "" + value
            if (itemseries.formatter != null) {
                val kFun = itemseries.formatter as (value: Number, index: Number, series: UTSArray<ChartOptionsSeries>, opts: ChartOptions) -> String
                formatVal = kFun(value, index, series, opts)
            }
            context.textAlign = "center"
            var startY = item.y - 4 * opts.pixelRatio + textOffset * opts.pixelRatio
            if (item.y > zeroPoints) {
                startY = item.y + textOffset * opts.pixelRatio + fontSize
            }
            context.fillText(formatVal, item.x + opts.xOffset, startY + opts.yOffset)
            context.closePath()
            context.stroke()
            context.textAlign = "left"
        }
    }
    )
}
fun drawMountDataPoints(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartColumnDataPoints {
    var xAxisData = opts.chartData.xAxisData
    var xAxisPoints = xAxisData.xAxisPoints
    var eachSpacing = xAxisData.eachSpacing!!
    var mountOption = opts.extra.mount!!
    mountOption.widthRatio = if (mountOption.widthRatio <= 0) {
        0
    } else {
        mountOption.widthRatio
    }
    mountOption.widthRatio = if (mountOption.widthRatio >= 2) {
        2
    } else {
        mountOption.widthRatio
    }
    context.save()
    var leftNum: Number = -2
    var rightNum = xAxisPoints.length + 2
    if (opts._scrollDistance_ != null && opts._scrollDistance_ != 0 && opts.enableScroll == true) {
        context.translate(opts._scrollDistance_!!, 0)
        leftNum = Math.floor(-opts._scrollDistance_!! / eachSpacing) - 2
        rightNum = leftNum + opts.xAxis.itemCount + 4
    }
    mountOption.customColor = fillCustomColor(mountOption.linearType, mountOption.customColor, series, config)
    var ranges = opts.chartData.yAxisData.ranges!![0].slice()
    var minRange = parseInt(ranges.pop()!!)
    var maxRange = parseInt(ranges.shift()!!)
    var spacingValid = opts.height - opts.area[0] - opts.area[2]
    var zeroHeight = spacingValid * (0 - minRange) / (maxRange - minRange)
    var zeroPoints = opts.height - Math.round(zeroHeight) - opts.area[2]
    var points = getMountDataPoints(series, minRange, maxRange, xAxisPoints, eachSpacing, opts, mountOption, zeroPoints, process)
    var xOffset = opts.xOffset
    var yOffset = opts.yOffset
    when (mountOption.type) {
        "bar" -> 
            run {
                var i: Number = 0
                while(i < points.length){
                    var item = points[i]
                    if (item != null && i > leftNum && i < rightNum) {
                        var startX = item.x - eachSpacing * mountOption.widthRatio / 2
                        var height = opts.height - item.y - opts.area[2]
                        context.beginPath()
                        var fillColor: Any = item.color!! ?: series[i].color!!
                        var strokeColor = item.color ?: series[i].color
                        if (mountOption.linearType != "none") {
                            var grd = context.createLinearGradient(startX + opts.xOffset, item.y + opts.yOffset, startX + opts.xOffset, zeroPoints + opts.yOffset)
                            if (mountOption.linearType == "opacity") {
                                grd.addColorStop(0, hexToRgb("" + fillColor, mountOption.linearOpacity))
                                grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                            } else {
                                grd.addColorStop(0, hexToRgb(mountOption.customColor[series[i].linearIndex!!], mountOption.linearOpacity))
                                grd.addColorStop(mountOption.colorStop, hexToRgb(mountOption.customColor[series[i].linearIndex!!], mountOption.linearOpacity))
                                grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                            }
                            fillColor = grd
                        }
                        if ((mountOption.barBorderRadius.length == 4) || mountOption.barBorderCircle == true) {
                            val left = startX
                            val top = if (item.y > zeroPoints) {
                                zeroPoints
                            } else {
                                item.y
                            }
                            val width = item.width!!
                            val height = Math.abs(zeroPoints - item.y)
                            if (mountOption.barBorderCircle) {
                                mountOption.barBorderRadius = _uA(
                                    width / 2,
                                    width / 2,
                                    0,
                                    0
                                )
                            }
                            if (item.y > zeroPoints) {
                                mountOption.barBorderRadius = _uA(
                                    0,
                                    0,
                                    width / 2,
                                    width / 2
                                )
                            }
                            var r0 = mountOption.barBorderRadius[0]
                            var r1 = mountOption.barBorderRadius[1]
                            var r2 = mountOption.barBorderRadius[2]
                            var r3 = mountOption.barBorderRadius[3]
                            var minRadius = Math.min(width / 2, height / 2)
                            r0 = if (r0 > minRadius) {
                                minRadius
                            } else {
                                r0
                            }
                            r1 = if (r1 > minRadius) {
                                minRadius
                            } else {
                                r1
                            }
                            r2 = if (r2 > minRadius) {
                                minRadius
                            } else {
                                r2
                            }
                            r3 = if (r3 > minRadius) {
                                minRadius
                            } else {
                                r3
                            }
                            r0 = if (r0 < 0) {
                                0
                            } else {
                                r0
                            }
                            r1 = if (r1 < 0) {
                                0
                            } else {
                                r1
                            }
                            r2 = if (r2 < 0) {
                                0
                            } else {
                                r2
                            }
                            r3 = if (r3 < 0) {
                                0
                            } else {
                                r3
                            }
                            context.arc(opts.xOffset + left + r0, opts.yOffset + top + r0, r0, -Math.PI, -Math.PI / 2)
                            context.arc(opts.xOffset + left + width - r1, opts.yOffset + top + r1, r1, -Math.PI / 2, 0)
                            context.arc(opts.xOffset + left + width - r2, opts.yOffset + top + height - r2, r2, 0, Math.PI / 2)
                            context.arc(opts.xOffset + left + r3, opts.yOffset + top + height - r3, r3, Math.PI / 2, Math.PI)
                        } else {
                            context.moveTo(opts.xOffset + startX, item.y + opts.yOffset)
                            context.lineTo(opts.xOffset + startX + item.width!!, item.y + opts.yOffset)
                            context.lineTo(opts.xOffset + startX + item.width!!, zeroPoints + opts.yOffset)
                            context.lineTo(opts.xOffset + startX, zeroPoints + opts.yOffset)
                            context.lineTo(opts.xOffset + startX, item.y + opts.yOffset)
                        }
                        context.strokeStyle = "" + strokeColor
                        context.fillStyle = fillColor
                        if (mountOption.borderWidth > 0) {
                            context.lineWidth = (mountOption.borderWidth * opts.pixelRatio)
                            context.closePath()
                            context.stroke()
                        }
                        context.fill()
                    }
                    i++
                }
            }
        "triangle" -> 
            run {
                var i: Number = 0
                while(i < points.length){
                    var item = points[i]
                    if (item != null && i > leftNum && i < rightNum) {
                        var startX = item.x - eachSpacing * mountOption.widthRatio / 2
                        var height = opts.height - item.y - opts.area[2]
                        context.beginPath()
                        var fillColor: Any = item.color ?: series[i].color!!
                        var strokeColor = item.color ?: series[i].color
                        if (mountOption.linearType != "none") {
                            var grd = context.createLinearGradient(startX + opts.xOffset, item.y + opts.yOffset, startX + opts.xOffset, zeroPoints + opts.yOffset)
                            if (mountOption.linearType == "opacity") {
                                grd.addColorStop(0, hexToRgb("" + fillColor, mountOption.linearOpacity))
                                grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                            } else {
                                grd.addColorStop(0, hexToRgb(mountOption.customColor[series[i].linearIndex!!], mountOption.linearOpacity))
                                grd.addColorStop(mountOption.colorStop, hexToRgb(mountOption.customColor[series[i].linearIndex!!], mountOption.linearOpacity))
                                grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                            }
                            fillColor = grd
                        }
                        context.moveTo(opts.xOffset + startX, zeroPoints + opts.yOffset)
                        context.lineTo(opts.xOffset + item.x, item.y + opts.yOffset)
                        context.lineTo(opts.xOffset + startX + item.width!!, zeroPoints + opts.yOffset)
                        context.strokeStyle = "" + strokeColor
                        context.fillStyle = fillColor
                        if (mountOption.borderWidth > 0) {
                            context.lineWidth = mountOption.borderWidth * opts.pixelRatio
                            context.stroke()
                        }
                        context.fill()
                    }
                    i++
                }
            }
        "mount" -> 
            run {
                var i: Number = 0
                while(i < points.length){
                    var item = points[i]
                    if (item != null && i > leftNum && i < rightNum) {
                        var startX = item.x - eachSpacing * mountOption.widthRatio / 2
                        var height = opts.height - item.y - opts.area[2]
                        context.beginPath()
                        var fillColor: Any = item.color ?: series[i].color!!
                        var strokeColor = item.color ?: series[i].color
                        if (mountOption.linearType != "none") {
                            var grd = context.createLinearGradient(opts.xOffset + startX, item.y + opts.yOffset, startX + opts.xOffset, zeroPoints + opts.yOffset)
                            if (mountOption.linearType == "opacity") {
                                grd.addColorStop(0, hexToRgb("" + fillColor, mountOption.linearOpacity))
                                grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                            } else {
                                grd.addColorStop(0, hexToRgb(mountOption.customColor[series[i].linearIndex!!], mountOption.linearOpacity))
                                grd.addColorStop(mountOption.colorStop, hexToRgb(mountOption.customColor[series[i].linearIndex!!], mountOption.linearOpacity))
                                grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                            }
                            fillColor = grd
                        }
                        context.moveTo(opts.xOffset + startX, zeroPoints + opts.yOffset)
                        context.bezierCurveTo(item.x - item.width!! / 4 + xOffset, zeroPoints + yOffset, item.x - item.width!! / 4 + xOffset, item.y + yOffset, item.x + xOffset, item.y + yOffset)
                        context.bezierCurveTo(item.x + item.width!! / 4 + xOffset, item.y + yOffset, item.x + item.width!! / 4 + xOffset, zeroPoints + yOffset, startX + item.width!! + xOffset, zeroPoints + yOffset)
                        context.strokeStyle = "" + strokeColor
                        context.fillStyle = fillColor
                        if (mountOption.borderWidth > 0) {
                            context.lineWidth = mountOption.borderWidth * opts.pixelRatio
                            context.stroke()
                        }
                        context.fill()
                    }
                    i++
                }
            }
        "sharp" -> 
            run {
                var i: Number = 0
                while(i < points.length){
                    var item = points[i]
                    if (item != null && i > leftNum && i < rightNum) {
                        var startX = item.x - eachSpacing * mountOption.widthRatio / 2
                        var height = opts.height - item.y - opts.area[2]
                        context.beginPath()
                        var fillColor: Any = item.color ?: series[i].color!!
                        var strokeColor = item.color ?: series[i].color
                        if (mountOption.linearType != "none") {
                            var grd = context.createLinearGradient(opts.xOffset + startX, item.y + opts.yOffset, startX + opts.xOffset, zeroPoints + opts.yOffset)
                            if (mountOption.linearType == "opacity") {
                                grd.addColorStop(0, hexToRgb("" + fillColor, mountOption.linearOpacity))
                                grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                            } else {
                                grd.addColorStop(0, hexToRgb(mountOption.customColor[series[i].linearIndex!!], mountOption.linearOpacity))
                                grd.addColorStop(mountOption.colorStop, hexToRgb(mountOption.customColor[series[i].linearIndex!!], mountOption.linearOpacity))
                                grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                            }
                            fillColor = grd
                        }
                        context.moveTo(startX + opts.xOffset, zeroPoints + opts.yOffset)
                        context.quadraticCurveTo(item.x - 0 + xOffset, zeroPoints - height / 4 + yOffset, item.x + xOffset, item.y + yOffset)
                        context.quadraticCurveTo(item.x + 0 + xOffset, zeroPoints - height / 4 + yOffset, startX + item.width!! + xOffset, zeroPoints + yOffset)
                        context.strokeStyle = "" + strokeColor
                        context.fillStyle = fillColor
                        if (mountOption.borderWidth > 0) {
                            context.lineWidth = mountOption.borderWidth * opts.pixelRatio
                            context.stroke()
                        }
                        context.fill()
                    }
                    i++
                }
            }
    }
    if (opts.dataLabel != false && process == 1) {
        var ranges = opts.chartData.yAxisData.ranges!![0].slice()
        var minRange = parseInt(ranges.pop()!!)
        var maxRange = parseInt(ranges.shift()!!)
        var points = getMountDataPoints(series, minRange, maxRange, xAxisPoints, eachSpacing, opts, mountOption, zeroPoints, process)
        drawMountPointText(points, series, config, context, opts, zeroPoints)
    }
    context.restore()
    return ChartColumnDataPoints(xAxisPoints = xAxisPoints, yAxisPoints = _uA<Number>(), calPoints = _uA(
        points
    ), eachSpacing = eachSpacing)
}
fun getArrXAxisTextList(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, stack: String, index: Number = -1): UTSArray<Number> {
    var data: UTSArray<UTSArray<Number>> = _uA()
    series.forEach(fun(item){
        data.push(*item.dataArr.toTypedArray())
    }
    )
    var sorted: UTSArray<Number> = _uA()
    data.forEach(fun(item: UTSArray<Number>){
        sorted.push(item[0])
    }
    )
    var minData: Number = 0
    var maxData: Number = 0
    if (sorted.length > 0) {
        minData = Math.min(*sorted.toTypedArray())
        maxData = Math.max(*sorted.toTypedArray())
    }
    if (index > -1) {
        if (opts.xAxis.data[index].min != null) {
            minData = Math.min(opts.xAxis.data!![index].min!!, minData)
        }
        if (opts.xAxis.data[index].max != null) {
            maxData = Math.max(opts.xAxis.data[index].max!!, maxData)
        }
    } else {
        if (opts.xAxis.min != null) {
            minData = Math.min(opts.xAxis.min!!, minData)
        }
        if (opts.xAxis.max != null) {
            maxData = Math.max(opts.xAxis.max!!, maxData)
        }
    }
    if (minData == maxData) {
        var rangeSpan = maxData ?: 10
        maxData += rangeSpan
    }
    var minRange = minData
    var maxRange = maxData
    var range: UTSArray<Number> = _uA()
    var eachRange = (maxRange - minRange) / opts.xAxis.splitNumber!!
    run {
        var i: Number = 0
        while(i <= opts.xAxis.splitNumber!!){
            range.push(minRange + eachRange * i)
            i++
        }
    }
    return range
}
fun getXAxisTextList(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, stack: String, index: Number = -1): UTSArray<Number> {
    var data: UTSArray<ChartOptionsSeriesData>
    if (stack == "stack") {
        data = dataCombineStack(series, opts.categories!!.length)
    } else {
        data = dataCombine(series)
    }
    var sorted: UTSArray<Number> = _uA()
    data = data.filter(fun(item: ChartOptionsSeriesData): Boolean {
        if (UTSAndroid.`typeof`(item) == "object" && item != null) {
            if (UTSArray.isArray(item)) {
                return item != null
            } else {
                return item.value != null
            }
        } else {
            return item != null
        }
    }
    )
    data.forEach(fun(item: ChartOptionsSeriesData){
        sorted.push(item.value!!)
    }
    )
    var minData: Number = 0
    var maxData: Number = 0
    if (sorted.length > 0) {
        minData = Math.min(*sorted.toTypedArray())
        maxData = Math.max(*sorted.toTypedArray())
    }
    if (index > -1) {
        if (UTSAndroid.`typeof`(opts.xAxis.data[index].min) == "number") {
            minData = Math.min(opts.xAxis.data!![index].min!!, minData)
        }
        if (UTSAndroid.`typeof`(opts.xAxis.data[index].max) == "number") {
            maxData = Math.max(opts.xAxis.data[index].max!!, maxData)
        }
    } else {
        if (UTSAndroid.`typeof`(opts.xAxis.min) == "number") {
            minData = Math.min(opts.xAxis.min!!, minData)
        }
        if (UTSAndroid.`typeof`(opts.xAxis.max) == "number") {
            maxData = Math.max(opts.xAxis.max!!, maxData)
        }
    }
    if (minData == maxData) {
        var rangeSpan = maxData ?: 10
        maxData += rangeSpan
    }
    var minRange = minData
    var maxRange = maxData
    var range: UTSArray<Number> = _uA()
    var eachRange = (maxRange - minRange) / opts.xAxis.splitNumber!!
    run {
        var i: Number = 0
        while(i <= opts.xAxis.splitNumber!!){
            range.push(minRange + eachRange * i)
            i++
        }
    }
    return range
}
fun calXAxisData(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D): ChartOptionsChartDataXAxisData {
    val type = opts.extra.bar?.type ?: ""
    val result = ChartOptionsChartDataXAxisData()
    result.angle = 0
    result.xAxisHeight = opts.xAxis.lineHeight * opts.pixelRatio + opts.xAxis.marginTop * opts.pixelRatio
    if (opts.type == "scatter") {
        result.ranges = getArrXAxisTextList(series, opts, config, type)
    } else {
        result.ranges = getXAxisTextList(series, opts, config, type)
    }
    result.rangesFormat = result.ranges!!.map(fun(reassignedItem: Number): Number {
        var item = reassignedItem
        item = parseFloat(item.toFixed(2))
        return item
    }
    )
    var xAxisScaleValues = result.ranges!!.map(fun(reassignedItem: Number): String {
        var item = reassignedItem
        item = parseFloat(item.toFixed(2))
        return "" + item
    }
    )
    val xpoints = getXAxisPoints(xAxisScaleValues, opts, config)
    result.xAxisPoints = xpoints.xAxisPoints
    result.startX = xpoints.startX
    result.endX = xpoints.endX
    result.eachSpacing = xpoints.eachSpacing
    var eachSpacing = result.eachSpacing
    var textLength = xAxisScaleValues.map(fun(item): Number {
        return measureText(item, opts.xAxis.fontSize * opts.pixelRatio)
    }
    )
    if (opts.xAxis.disabled == true) {
        result.xAxisHeight = 0
    }
    return result
}
fun drawBarToolTipSplitArea(offsetX: Number, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, eachSpacing: Number) {
    var toolTipOption = opts.extra.bar!!
    var startX = opts.area[3]
    var endX = opts.width - opts.area[1]
    context.beginPath()
    context.fillStyle = hexToRgb(toolTipOption.activeBgColor, toolTipOption.activeBgOpacity)
    context.rect(startX + opts.xOffset, (offsetX - eachSpacing / 2), endX - startX, eachSpacing)
    context.closePath()
    context.fill()
    context.fillStyle = "#FFFFFF"
}
fun getBarDataPoints(data: UTSArray<ChartOptionsSeriesData>, minRange: Number, maxRange: Number, yAxisPoints: UTSArray<Number>, eachSpacing: Number, opts: ChartOptions, config: TuiChartConfig, process: Number = 1): UTSArray<ChartOptionsChartDataCalPoints> {
    var points: UTSArray<ChartOptionsChartDataCalPoints> = _uA()
    var validHeight = opts.height - opts.area[0] - opts.area[2]
    var validWidth = opts.width - opts.area[1] - opts.area[3]
    data.forEach(fun(item, index) {
        if (item == null) {
        } else {
            var point = ChartOptionsChartDataCalPoints()
            point.color = item.color
            point.y = yAxisPoints[index]
            var value: Number = item.value!!
            var height = validWidth * (value - minRange) / (maxRange - minRange)
            height *= process
            point.height = height
            point.value = value
            point.x = height + opts.area[3]
            points.push(point)
        }
    }
    )
    return points
}
fun getBarStackDataPoints(data: UTSArray<ChartOptionsSeriesData>, minRange: Number, maxRange: Number, yAxisPoints: UTSArray<Number>, eachSpacing: Number, opts: ChartOptions, config: TuiChartConfig, seriesIndex: Number, stackSeries: UTSArray<ChartOptionsSeries>, process: Number = 1): UTSArray<ChartOptionsChartDataCalPoints> {
    var points: UTSArray<ChartOptionsChartDataCalPoints> = _uA()
    var validHeight = opts.width - opts.area[1] - opts.area[3]
    data.forEach(fun(item: ChartOptionsSeriesData, index: Number) {
        if (item == null) {
        } else {
            var point = ChartOptionsChartDataCalPoints()
            point.color = item.color
            point.y = yAxisPoints[index]
            var height: Number = 0
            var height0: Number = 0
            if (seriesIndex > 0) {
                var value: Number = 0
                run {
                    var i: Number = 0
                    while(i <= seriesIndex){
                        value += stackSeries[i].data!![index].value!!
                        i++
                    }
                }
                var value0 = value - item.value!!
                height = validHeight * (value - minRange) / (maxRange - minRange)
                height0 = validHeight * (value0 - minRange) / (maxRange - minRange)
            } else {
                var value = item.value!!
                height = validHeight * (value - minRange) / (maxRange - minRange)
                height0 = 0
            }
            var heightc = height0
            height *= process
            heightc *= process
            point.height = height - heightc
            point.x = opts.area[3] + height
            point.x0 = opts.area[3] + heightc
            points.push(point)
        }
    }
    )
    return points
}
fun fixBarData(points: UTSArray<ChartOptionsChartDataCalPoints>, eachSpacing: Number, columnLen: Number, index: Number, config: TuiChartConfig, opts: ChartOptions): UTSArray<ChartOptionsChartDataCalPoints> {
    return points.map(fun(item): ChartOptionsChartDataCalPoints {
        if (item == null) {
        }
        var seriesGap: Number = 0
        var categoryGap: Number = 0
        seriesGap = opts.extra.bar!!.seriesGap * opts.pixelRatio
        categoryGap = opts.extra.bar!!.categoryGap * opts.pixelRatio
        seriesGap = Math.min(seriesGap, eachSpacing / columnLen)
        categoryGap = Math.min(categoryGap, eachSpacing / columnLen)
        item.width = Math.ceil((eachSpacing - 2 * categoryGap - seriesGap * (columnLen - 1)) / columnLen)
        if (opts.extra.bar != null && opts.extra.bar!!.width != null && opts.extra.bar!!.width!! > 0) {
            item.width = Math.min(item.width!!, +opts.extra.bar!!.width!! * opts.pixelRatio)
        }
        if (item.width!! <= 0) {
            item.width = 1
        }
        item.y += (index + 0.5 - columnLen / 2) * (item.width!! + seriesGap)
        return item
    }
    )
}
fun fixBarStackData(points: UTSArray<ChartOptionsChartDataCalPoints>, eachSpacing: Number, columnLen: Number, index: Number, config: TuiChartConfig, opts: ChartOptions, series: UTSArray<ChartOptionsSeries>): UTSArray<ChartOptionsChartDataCalPoints> {
    var categoryGap = opts.extra.bar!!.categoryGap * opts.pixelRatio
    return points.map(fun(item): ChartOptionsChartDataCalPoints {
        if (item == null) {
        }
        item.width = Math.ceil(eachSpacing - 2 * categoryGap)
        if (opts.extra.bar != null && opts.extra.bar!!.width != null && +opts.extra.bar!!.width!! > 0) {
            item.width = Math.min(item.width!!, +opts.extra.bar!!.width!! * opts.pixelRatio)
        }
        if (item.width!! <= 0) {
            item.width = 1
        }
        return item
    }
    )
}
fun drawBarPointText(points: UTSArray<ChartOptionsChartDataCalPoints>, series: ChartOptionsSeries, config: TuiChartConfig, context: CanvasRenderingContext2D, opts: ChartOptions) {
    var data = series.data!!
    var textOffset = series.textOffset ?: 0
    points.forEach(fun(item, index){
        if (item != null) {
            context.beginPath()
            var fontSize = if (series.textSize != null) {
                series.textSize!! * opts.pixelRatio
            } else {
                config.fontSize
            }
            context.font = "" + fontSize + "px sans-serif"
            context.fillStyle = series.textColor ?: opts.fontColor
            var value = data[index].value!!
            var formatVal: String = ""
            if (series.formatter != null) {
                val kFun = series.formatter as (value: Number, index: Number, series: ChartOptionsSeries, opts: ChartOptions) -> String
                formatVal = kFun(value, index, series, opts)
            } else {
                formatVal = "" + value
            }
            context.textAlign = "left"
            context.fillText(formatVal, opts.xOffset + item.x + 4 * opts.pixelRatio, opts.yOffset + item.y + fontSize / 2 - 3)
            context.closePath()
            context.stroke()
        }
    }
    )
}
fun drawBarDataPoints(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartColumnDataPoints {
    var yAxisPoints: UTSArray<Number> = _uA()
    var eachSpacing = (opts.height - opts.area[0] - opts.area[2]) / opts.categories!!.length
    run {
        var i: Number = 0
        while(i < opts.categories!!.length){
            yAxisPoints.push(opts.area[0] + eachSpacing / 2 + eachSpacing * i)
            i++
        }
    }
    var columnOption = opts.extra.bar!!
    var calPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = _uA()
    context.save()
    var leftNum: Number = -2
    var rightNum = yAxisPoints.length + 2
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    if (opts.tooltip != null && opts.tooltip!!.textList.length > 0 && process == 1) {
        drawBarToolTipSplitArea(opts.tooltip!!.offset.y, opts, config, context, eachSpacing)
    }
    columnOption.customColor = fillCustomColor(columnOption.linearType, columnOption.customColor, series, config)
    series.forEach(fun(eachSeries: ChartOptionsSeries, seriesIndex: Number){
        var ranges: UTSArray<Number> = opts.chartData.xAxisData.ranges!!.slice()
        var maxRange = ranges.pop()!!
        var minRange = ranges.shift()!!
        var data = eachSeries.data!!
        var points: UTSArray<ChartOptionsChartDataCalPoints> = _uA()
        when (columnOption.type) {
            "group" -> 
                {
                    points = getBarDataPoints(data, minRange, maxRange, yAxisPoints, eachSpacing, opts, config, process)
                    var tooltipPoints = getBarStackDataPoints(data, minRange, maxRange, yAxisPoints, eachSpacing, opts, config, seriesIndex, series, process)
                    calPoints.push(tooltipPoints)
                    points = fixBarData(points, eachSpacing, series.length, seriesIndex, config, opts)
                    run {
                        var i: Number = 0
                        while(i < points.length){
                            var item = points[i]
                            if (item != null && i > leftNum && i < rightNum) {
                                var startX = opts.area[3]
                                var startY = item.y - item.width!! / 2
                                var height = item.height
                                context.beginPath()
                                var fillColor: Any = item.color ?: eachSeries.color!!
                                var strokeColor: String = item.color ?: eachSeries.color!!
                                if (columnOption.linearType != "none") {
                                    var grd = context.createLinearGradient(startX + xOffset, item.y + yOffset, item.x + xOffset, item.y + yOffset)
                                    if (columnOption.linearType == "opacity") {
                                        grd.addColorStop(0, hexToRgb("" + fillColor, columnOption.linearOpacity))
                                        grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                                    } else {
                                        grd.addColorStop(0, hexToRgb(columnOption.customColor!![eachSeries.linearIndex!!], columnOption.linearOpacity))
                                        grd.addColorStop(columnOption.colorStop, hexToRgb(columnOption.customColor!![eachSeries.linearIndex!!], columnOption.linearOpacity))
                                        grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                                    }
                                    fillColor = grd
                                }
                                if ((columnOption.barBorderRadius != null && columnOption.barBorderRadius!!.length == 4) || columnOption.barBorderCircle == true) {
                                    val left = startX
                                    val width = item.width!!
                                    val top = item.y - item.width!! / 2
                                    val height = item.height!!
                                    if (columnOption.barBorderCircle) {
                                        columnOption.barBorderRadius = _uA(
                                            width / 2,
                                            width / 2,
                                            0,
                                            0
                                        )
                                    }
                                    var r0 = columnOption.barBorderRadius!![0]
                                    var r1 = columnOption.barBorderRadius!![1]
                                    var r2 = columnOption.barBorderRadius!![2]
                                    var r3 = columnOption.barBorderRadius!![3]
                                    var minRadius = Math.min(width / 2, height / 2)
                                    r0 = if (r0 > minRadius) {
                                        minRadius
                                    } else {
                                        r0
                                    }
                                    r1 = if (r1 > minRadius) {
                                        minRadius
                                    } else {
                                        r1
                                    }
                                    r2 = if (r2 > minRadius) {
                                        minRadius
                                    } else {
                                        r2
                                    }
                                    r3 = if (r3 > minRadius) {
                                        minRadius
                                    } else {
                                        r3
                                    }
                                    r0 = if (r0 < 0) {
                                        0
                                    } else {
                                        r0
                                    }
                                    r1 = if (r1 < 0) {
                                        0
                                    } else {
                                        r1
                                    }
                                    r2 = if (r2 < 0) {
                                        0
                                    } else {
                                        r2
                                    }
                                    r3 = if (r3 < 0) {
                                        0
                                    } else {
                                        r3
                                    }
                                    context.arc(xOffset + left + r3, yOffset + top + r3, r3, -Math.PI, -Math.PI / 2, false)
                                    context.arc(xOffset + item.x - r0, yOffset + top + r0, r0, -Math.PI / 2, 0, false)
                                    context.arc(xOffset + item.x - r1, yOffset + top + width - r1, r1, 0, Math.PI / 2, false)
                                    context.arc(xOffset + left + r2, yOffset + top + width - r2, r2, Math.PI / 2, Math.PI, false)
                                } else {
                                    context.moveTo(xOffset + startX, yOffset + startY)
                                    context.lineTo(xOffset + item.x, yOffset + startY)
                                    context.lineTo(xOffset + item.x, yOffset + startY + item.width!!)
                                    context.lineTo(xOffset + startX, yOffset + startY + item.width!!)
                                    context.lineTo(xOffset + startX, yOffset + startY)
                                    context.lineWidth = 1
                                    context.strokeStyle = strokeColor
                                }
                                context.fillStyle = fillColor
                                context.closePath()
                                context.fill()
                            }
                            i++
                        }
                    }
                }
            "stack" -> 
                {
                    points = getBarStackDataPoints(data, minRange, maxRange, yAxisPoints, eachSpacing, opts, config, seriesIndex, series, process)
                    calPoints.push(points)
                    points = fixBarStackData(points, eachSpacing, series.length, seriesIndex, config, opts, series)
                    run {
                        var i: Number = 0
                        while(i < points.length){
                            var item = points[i]
                            if (item != null && i > leftNum && i < rightNum) {
                                context.beginPath()
                                var fillColor = item.color ?: eachSeries.color!!
                                var startX = item.x0
                                context.fillStyle = fillColor
                                context.moveTo(xOffset + startX, yOffset + item.y - item.width!! / 2)
                                context.fillRect(startX + xOffset, item.y - item.width!! / 2 + yOffset, item.height!!, item.width!!)
                                context.closePath()
                                context.fill()
                            }
                            i++
                        }
                    }
                }
        }
    }
    )
    if (opts.dataLabel != false && process == 1) {
        series.forEach(fun(eachSeries, seriesIndex) {
            var ranges: UTSArray<Number> = opts.chartData.xAxisData.ranges!!.slice()
            var maxRange = ranges.pop()!!
            var minRange = ranges.shift()!!
            var data = eachSeries.data!!
            var points: UTSArray<ChartOptionsChartDataCalPoints> = _uA()
            when (columnOption.type) {
                "group" -> 
                    {
                        points = getBarDataPoints(data, minRange, maxRange, yAxisPoints, eachSpacing, opts, config, process)
                        points = fixBarData(points, eachSpacing, series.length, seriesIndex, config, opts)
                        drawBarPointText(points, eachSeries, config, context, opts)
                    }
                "stack" -> 
                    {
                        points = getBarStackDataPoints(data, minRange, maxRange, yAxisPoints, eachSpacing, opts, config, seriesIndex, series, process)
                        drawBarPointText(points, eachSeries, config, context, opts)
                    }
            }
        }
        )
    }
    return ChartColumnDataPoints(xAxisPoints = _uA<Number>(), yAxisPoints = yAxisPoints, calPoints = calPoints, eachSpacing = eachSpacing)
}
fun createCurveControlPoints(points: UTSArray<ChartOptionsChartDataCalPoints>, i: Number): ChartCurveControlPoints {
    val curvepoints = ChartCurveControlPoints()
    fun isNotMiddlePoint(points: UTSArray<ChartOptionsChartDataCalPoints>, i: Number): Boolean {
        if (i > 0 && i < points.length - 2) {
            return points[i].y >= Math.max(points[i - 1].y, points[i + 1].y) || points[i].y <= Math.min(points[i - 1].y, points[i + 1].y)
        } else {
            return false
        }
    }
    fun isNotMiddlePointX(points: UTSArray<ChartOptionsChartDataCalPoints>, i: Number): Boolean {
        if (i > 0 && i < points.length - 2) {
            return points[i].x >= Math.max(points[i - 1].x, points[i + 1].x) || points[i].x <= Math.min(points[i - 1].x, points[i + 1].x)
        } else {
            return false
        }
    }
    var a: Number = 0.2
    var b: Number = 0.2
    var pAx: Number = 0
    var pAy: Number = 0
    var pBx: Number = 0
    var pBy: Number = 0
    if (i < 1) {
        pAx = points[0].x + (points[1].x - points[0].x) * a
        pAy = points[0].y + (points[1].y - points[0].y) * a
    } else {
        pAx = points[i].x + (points[i + 1].x - points[i - 1].x) * a
        pAy = points[i].y + (points[i + 1].y - points[i - 1].y) * a
    }
    if (i > points.length - 3) {
        var last = points.length - 1
        pBx = points[last].x - (points[last].x - points[last - 1].x) * b
        pBy = points[last].y - (points[last].y - points[last - 1].y) * b
    } else {
        pBx = points[i + 1].x - (points[i + 2].x - points[i].x) * b
        pBy = points[i + 1].y - (points[i + 2].y - points[i].y) * b
    }
    if (isNotMiddlePoint(points, i + 1)) {
        pBy = points[i + 1].y
    }
    if (isNotMiddlePoint(points, i)) {
        pAy = points[i].y
    }
    if (isNotMiddlePointX(points, i + 1)) {
        pBx = points[i + 1].x
    }
    if (isNotMiddlePointX(points, i)) {
        pAx = points[i].x
    }
    if (pAy >= Math.max(points[i].y, points[i + 1].y) || pAy <= Math.min(points[i].y, points[i + 1].y)) {
        pAy = points[i].y
    }
    if (pBy >= Math.max(points[i].y, points[i + 1].y) || pBy <= Math.min(points[i].y, points[i + 1].y)) {
        pBy = points[i + 1].y
    }
    if (pAx >= Math.max(points[i].x, points[i + 1].x) || pAx <= Math.min(points[i].x, points[i + 1].x)) {
        pAx = points[i].x
    }
    if (pBx >= Math.max(points[i].x, points[i + 1].x) || pBx <= Math.min(points[i].x, points[i + 1].x)) {
        pBx = points[i + 1].x
    }
    val ctrAOffset = Offset()
    val ctrBOffset = Offset()
    ctrAOffset.x = pAx
    ctrAOffset.y = pAy
    ctrBOffset.x = pBx
    ctrBOffset.y = pBy
    curvepoints.ctrA = ctrAOffset
    curvepoints.ctrB = ctrBOffset
    return curvepoints
}
fun drawArrPointText(points: UTSArray<ChartOptionsChartDataCalPoints>, series: ChartOptionsSeries, config: TuiChartConfig, context: CanvasRenderingContext2D, opts: ChartOptions) {
    var data = series.dataArr
    var textOffset = series.textOffset ?: 0
    points.forEach(fun(item: ChartOptionsChartDataCalPoints, index: Number){
        context.beginPath()
        var fontSize = (series.textSize ?: config.fontSize) * opts.pixelRatio
        context.font = "" + fontSize + "px sans-serif"
        context.fillStyle = series.textColor ?: opts.fontColor
        var value = data[index][1]
        var formatVal: String
        if (series.formatter != null) {
            val kFun = series.formatter as (value: Number, index: Number, series: ChartOptionsSeries, opts: ChartOptions) -> String
            formatVal = kFun(value, index, series, opts)
        } else {
            formatVal = "" + value
        }
        context.textAlign = "center"
        context.fillText(formatVal, item.x + opts.xOffset, opts.yOffset + item.y - 4 + textOffset * opts.pixelRatio)
        context.closePath()
        context.stroke()
        context.textAlign = "left"
    }
    )
}
fun drawPointText(points: UTSArray<ChartOptionsChartDataCalPoints>, series: ChartOptionsSeries, config: TuiChartConfig, context: CanvasRenderingContext2D, opts: ChartOptions) {
    var data = series.data!!
    var textOffset = series.textOffset ?: 0
    points.forEach(fun(item: ChartOptionsChartDataCalPoints, index: Number){
        context.beginPath()
        var fontSize = (series.textSize ?: config.fontSize) * opts.pixelRatio
        context.font = "" + fontSize + "px sans-serif"
        context.fillStyle = series.textColor ?: opts.fontColor
        var value = data[index].value
        var formatVal: String
        if (series.formatter != null) {
            val kFun = series.formatter as (value: Number, index: Number, series: ChartOptionsSeries, opts: ChartOptions) -> String
            formatVal = kFun(value!!, index, series, opts)
        } else {
            formatVal = "" + value
        }
        context.textAlign = "center"
        context.fillText(formatVal, item.x + opts.xOffset, opts.yOffset + item.y - 4 + textOffset * opts.pixelRatio)
        context.closePath()
        context.stroke()
        context.textAlign = "left"
    }
    )
}
fun drawActiveAreaPoint(points: UTSArray<ChartOptionsChartDataCalPoints>, color: String, shape: String, context: CanvasRenderingContext2D, opts: ChartOptions, option: ChartOptionsExtraArea, seriesIndex: Number) {
    if (opts.tooltip == null) {
        return
    }
    if (opts.tooltip!!.group.length > 0 && !opts.tooltip!!.group.includes(seriesIndex)) {
        return
    }
    var pointIndex = if (UTSArray.isArray(opts.tooltip!!.index) == false) {
        (opts.tooltip!!.index as Number)
    } else {
        ((opts.tooltip!!.index as UTSArray<Number>)[opts.tooltip!!.group.indexOf(seriesIndex)] as Number)
    }
    context.beginPath()
    if (option.activeType == "hollow") {
        context.strokeStyle = color
        context.fillStyle = opts.background
        context.lineWidth = 2 * opts.pixelRatio
    } else {
        context.strokeStyle = "#ffffff"
        context.fillStyle = color
        context.lineWidth = 1 * opts.pixelRatio
    }
    if (shape == "diamond") {
        points.forEach(fun(item, index) {
            if (item != null && pointIndex == index) {
                context.moveTo(item.x, item.y - 4.5)
                context.lineTo(item.x - 4.5, item.y)
                context.lineTo(item.x, item.y + 4.5)
                context.lineTo(item.x + 4.5, item.y)
                context.lineTo(item.x, item.y - 4.5)
            }
        })
    } else if (shape == "circle") {
        points.forEach(fun(item, index) {
            if (item != null && pointIndex == index) {
                context.moveTo(item.x + 2.5 * opts.pixelRatio, item.y)
                context.arc(item.x, item.y, 3 * opts.pixelRatio, 0, 2 * Math.PI, false)
            }
        })
    } else if (shape == "square") {
        points.forEach(fun(item, index) {
            if (item != null && pointIndex == index) {
                context.moveTo(item.x - 3.5, item.y - 3.5)
                context.rect(item.x - 3.5, item.y - 3.5, 7, 7)
            }
        })
    } else if (shape == "triangle") {
        points.forEach(fun(item, index) {
            if (item != null && pointIndex == index) {
                context.moveTo(item.x, item.y - 4.5)
                context.lineTo(item.x - 4.5, item.y + 4.5)
                context.lineTo(item.x + 4.5, item.y + 4.5)
                context.lineTo(item.x, item.y - 4.5)
            }
        })
    } else if (shape == "none") {
        return
    }
    context.closePath()
    context.fill()
    context.stroke()
}
fun drawActivePoint(points: UTSArray<ChartOptionsChartDataCalPoints>, color: String, shape: String, context: CanvasRenderingContext2D, opts: ChartOptions, option: ChartOptionsExtraLine, seriesIndex: Number) {
    if (opts.tooltip == null) {
        return
    }
    if (opts.tooltip!!.group.length > 0 && opts.tooltip!!.group.includes(seriesIndex) == false) {
        return
    }
    var pointIndex = if (UTSArray.isArray(opts.tooltip!!.index) == false) {
        opts.tooltip!!.index as Number
    } else {
        (opts.tooltip!!.index as UTSArray<Number>)[opts.tooltip!!.group.indexOf(seriesIndex)] as Number
    }
    context.beginPath()
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    if (option.activeType == "hollow") {
        context.strokeStyle = color
        context.fillStyle = opts.background
        context.lineWidth = 2 * opts.pixelRatio
    } else {
        context.strokeStyle = "#ffffff"
        context.fillStyle = color
        context.lineWidth = 1 * opts.pixelRatio
    }
    if (shape == "diamond") {
        points.forEach(fun(item, index) {
            if (item != null && pointIndex == index) {
                context.moveTo(item.x + xOffset, item.y - 4.5 + yOffset)
                context.lineTo(item.x - 4.5 + xOffset, item.y + yOffset)
                context.lineTo(item.x + xOffset, item.y + 4.5 + yOffset)
                context.lineTo(item.x + 4.5 + xOffset, item.y + yOffset)
                context.lineTo(item.x + xOffset, item.y - 4.5 + yOffset)
            }
        })
    } else if (shape == "circle") {
        points.forEach(fun(item, index) {
            if (item != null && pointIndex == index) {
                context.moveTo(item.x + 2.5 * opts.pixelRatio + xOffset, item.y + yOffset)
                context.arc(item.x + xOffset, item.y + yOffset, 3 * opts.pixelRatio, 0, 2 * Math.PI, false)
            }
        })
    } else if (shape == "square") {
        points.forEach(fun(item, index) {
            if (item != null && pointIndex == index) {
                context.moveTo(item.x - 3.5 + xOffset, item.y - 3.5 + yOffset)
                context.rect(item.x - 3.5 + xOffset, item.y - 3.5 + yOffset, 7, 7)
            }
        })
    } else if (shape == "triangle") {
        points.forEach(fun(item, index) {
            if (item != null && pointIndex == index) {
                context.moveTo(item.x + xOffset, item.y - 4.5 + yOffset)
                context.lineTo(item.x - 4.5 + xOffset, item.y + 4.5 + yOffset)
                context.lineTo(item.x + 4.5 + xOffset, item.y + 4.5 + yOffset)
                context.lineTo(item.x + xOffset, item.y - 4.5 + yOffset)
            }
        })
    } else if (shape == "none") {
        return
    }
    context.closePath()
    context.fill()
    context.stroke()
}
fun drawPointShape(points: UTSArray<ChartOptionsChartDataCalPoints>, color: String, shape: String, context: CanvasRenderingContext2D, opts: ChartOptions) {
    context.beginPath()
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    if (opts.dataPointShapeType == "hollow") {
        context.strokeStyle = color
        context.fillStyle = opts.background
        context.lineWidth = 2 * opts.pixelRatio
    } else {
        context.strokeStyle = "#ffffff"
        context.fillStyle = color
        context.lineWidth = 1 * opts.pixelRatio
    }
    if (shape == "diamond") {
        points.forEach(fun(item, index) {
            if (item != null) {
                context.moveTo(item.x + xOffset, item.y - 4.5 + yOffset)
                context.lineTo(item.x - 4.5 + xOffset, item.y + yOffset)
                context.lineTo(item.x + xOffset, item.y + 4.5 + yOffset)
                context.lineTo(item.x + 4.5 + xOffset, item.y + yOffset)
                context.lineTo(item.x + xOffset, item.y - 4.5 + yOffset)
            }
        })
    } else if (shape == "circle") {
        points.forEach(fun(item, index) {
            if (item != null) {
                context.moveTo(item.x + 2.5 * opts.pixelRatio + xOffset, item.y + yOffset)
                context.arc(item.x + xOffset, item.y + yOffset, 3 * opts.pixelRatio, 0, 2 * Math.PI, false)
            }
        })
    } else if (shape == "square") {
        points.forEach(fun(item, index) {
            if (item != null) {
                context.moveTo(item.x - 3.5 + xOffset, item.y - 3.5 + yOffset)
                context.rect(item.x - 3.5 + xOffset, item.y - 3.5 + yOffset, 7, 7)
            }
        })
    } else if (shape == "triangle") {
        points.forEach(fun(item, index) {
            if (item != null) {
                context.moveTo(item.x + xOffset, item.y - 4.5 + yOffset)
                context.lineTo(item.x - 4.5 + xOffset, item.y + 4.5 + yOffset)
                context.lineTo(item.x + 4.5 + xOffset, item.y + 4.5 + yOffset)
                context.lineTo(item.x + xOffset, item.y - 4.5 + yOffset)
            }
        })
    } else if (shape == "none") {
        return
    }
    context.closePath()
    context.fill()
    context.stroke()
}
fun splitCandlePoints(points: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>>, eachSeries: ChartOptionsSeries): UTSArray<UTSArray<UTSArray<ChartOptionsChartDataCalPoints>>> {
    var newPoints: UTSArray<UTSArray<UTSArray<ChartOptionsChartDataCalPoints>>> = _uA()
    var items: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = _uA()
    points.forEach(fun(item, index) {
        if (eachSeries.connectNulls) {
            if (item != null) {
                items.push(item)
            }
        } else {
            if (item != null) {
                items.push(item)
            } else {
                if (items.length > 0) {
                    newPoints.push(items)
                }
                items = _uA()
            }
        }
    }
    )
    if (items.length > 0) {
        newPoints.push(items)
    }
    return newPoints
}
fun splitPoints(points: UTSArray<ChartOptionsChartDataCalPoints>, eachSeries: ChartOptionsSeries): UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> {
    var newPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = _uA()
    var items: UTSArray<ChartOptionsChartDataCalPoints> = _uA()
    points.forEach(fun(item, index) {
        if (eachSeries.connectNulls) {
            if (item != null) {
                items.push(item)
            }
        } else {
            if (item != null) {
                items.push(item)
            } else {
                if (items.length > 0) {
                    newPoints.push(items)
                }
                items = _uA()
            }
        }
    }
    )
    if (items.length > 0) {
        newPoints.push(items)
    }
    return newPoints
}
fun getLineDataPoints(data: UTSArray<ChartOptionsSeriesData>, minRange: Number, maxRange: Number, xAxisPoints: UTSArray<Number>, eachSpacing: Number, opts: ChartOptions, config: TuiChartConfig, lineOption: ChartOptionsExtraLine, process: Number = 1): UTSArray<ChartOptionsChartDataCalPoints> {
    var boundaryGap = opts.xAxis.boundaryGap
    var points: UTSArray<ChartOptionsChartDataCalPoints> = _uA()
    var validHeight = opts.height - opts.area[0] - opts.area[2]
    var validWidth = opts.width - opts.area[1] - opts.area[3]
    data.forEach(fun(item, index) {
        if (item.value == null) {
        } else {
            var point = ChartOptionsChartDataCalPoints()
            point.color = item.color
            if (lineOption.animation == "vertical") {
                point.x = xAxisPoints[index]
                var value = item.value!!
                if (boundaryGap == "center") {
                    point.x += eachSpacing / 2
                }
                var height = validHeight * (value - minRange) / (maxRange - minRange)
                height *= process
                point.y = opts.height - height - opts.area[2]
                points.push(point)
            } else {
                point.x = xAxisPoints[0] + eachSpacing * index * process
                var value = item.value!!
                if (boundaryGap == "center") {
                    point.x += eachSpacing / 2
                }
                var height = validHeight * (value - minRange) / (maxRange - minRange)
                point.y = opts.height - height - opts.area[2]
                points.push(point)
            }
        }
    }
    )
    return points
}
fun drawLineDataPoints(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartColumnDataPoints {
    var lineOption = opts.extra.line!!
    lineOption.width = lineOption.width * opts.pixelRatio
    var xAxisData = opts.chartData.xAxisData
    var xAxisPoints = xAxisData.xAxisPoints
    var eachSpacing = xAxisData.eachSpacing!!
    var calPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = _uA()
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    context.save()
    var leftSpace: Number = 0
    var rightSpace = opts.width + eachSpacing
    if (opts._scrollDistance_ != null && opts._scrollDistance_ != 0 && opts.enableScroll == true) {
        context.translate(opts._scrollDistance_!!, 0)
        leftSpace = -opts._scrollDistance_!! - eachSpacing * 2 + opts.area[3]
        rightSpace = leftSpace + (opts.xAxis.itemCount + 4) * eachSpacing
    }
    series.forEach(fun(eachSeries, seriesIndex){
        context.beginPath()
        context.strokeStyle = eachSeries.color!!
        context.moveTo(-10000, -10000)
        context.lineTo(-10001, -10001)
        context.stroke()
        var stringRanges: UTSArray<String> = opts.chartData.yAxisData.ranges!![eachSeries.index!!]
        var ranges: UTSArray<Number> = stringRanges.map(fun(k: String): Number {
            return parseFloat(k)
        }
        ).slice()
        var minRange = ranges.pop()!!
        var maxRange = ranges.shift()!!
        var data = eachSeries.data!!
        var points = getLineDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, lineOption, process)
        calPoints.push(points)
        var splitPointList: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = splitPoints(points, eachSeries)
        if (eachSeries.lineType == "dash") {
            var dashLength = eachSeries.dashLength ?: 8
            dashLength *= opts.pixelRatio
            context.setLineDash(_uA(
                dashLength,
                dashLength
            ))
        }
        context.beginPath()
        var strokeColor: Any = eachSeries.color!!
        if (lineOption.linearType != "none" && eachSeries.linearColor != null && eachSeries.linearColor!!.length > 0) {
            var grd = context.createLinearGradient(opts.chartData.xAxisData!!.startX!! + xOffset, opts.height / 2 + yOffset, opts.chartData.xAxisData!!.endX!! + xOffset, opts.height / 2 + yOffset)
            run {
                var i: Number = 0
                while(i < eachSeries.linearColor!!.length){
                    grd.addColorStop(parseFloat(eachSeries.linearColor!![i][0]), hexToRgb(eachSeries.linearColor!![i][1], 1))
                    i++
                }
            }
            strokeColor = grd
        }
        context.strokeStyle = strokeColor
        context.lineWidth = lineOption.width
        splitPointList.forEach(fun(points, index) {
            if (points.length == 1) {
                context.moveTo(points[0].x + xOffset, points[0].y + yOffset)
            } else {
                context.moveTo(points[0].x + xOffset, points[0].y + yOffset)
                var startPoint: Number = 0
                if (lineOption.type == "curve") {
                    run {
                        var j: Number = 0
                        while(j < points.length){
                            var item = points[j]
                            if (startPoint == 0 && item.x > leftSpace) {
                                context.moveTo(item.x + xOffset, item.y + yOffset)
                                startPoint = 1
                            }
                            if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                var ctrlPoint = createCurveControlPoints(points, j - 1)
                                context.bezierCurveTo(ctrlPoint.ctrA.x + xOffset, ctrlPoint.ctrA.y + yOffset, ctrlPoint.ctrB.x + xOffset, ctrlPoint.ctrB.y + yOffset, item.x + xOffset, item.y + yOffset)
                            }
                            j++
                        }
                    }
                }
                if (lineOption.type == "straight") {
                    run {
                        var j: Number = 0
                        while(j < points.length){
                            var item = points[j]
                            if (startPoint == 0 && item.x > leftSpace) {
                                context.moveTo(item.x + xOffset, item.y + yOffset)
                                startPoint = 1
                            }
                            if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                context.lineTo(item.x + xOffset, item.y + yOffset)
                            }
                            j++
                        }
                    }
                }
                if (lineOption.type == "step") {
                    run {
                        var j: Number = 0
                        while(j < points.length){
                            var item = points[j]
                            if (startPoint == 0 && item.x > leftSpace) {
                                context.moveTo(item.x + xOffset, item.y + yOffset)
                                startPoint = 1
                            }
                            if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                context.lineTo(item.x + xOffset, points[j - 1].y + yOffset)
                                context.lineTo(item.x + xOffset, item.y + yOffset)
                            }
                            j++
                        }
                    }
                }
                context.moveTo(points[0].x + xOffset, points[0].y + yOffset)
            }
        }
        )
        context.stroke()
        context.setLineDash(_uA())
        if (opts.dataPointShape != false) {
            drawPointShape(points, eachSeries.color!!, eachSeries.pointShape, context, opts)
        }
        drawActivePoint(points, eachSeries.color!!, eachSeries.pointShape, context, opts, lineOption, 0)
    }
    )
    if (opts.dataLabel != false && process == 1) {
        series.forEach(fun(eachSeries, seriesIndex) {
            var stringRanges = opts.chartData.yAxisData.ranges!![eachSeries.index!!]
            var ranges = stringRanges.map(fun(k: String): Number {
                return parseFloat(k)
            }
            )
            var minRange = ranges.pop()!!
            var maxRange = ranges.shift()!!
            var data = eachSeries.data!!
            var points = getDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
            drawPointText(points, eachSeries, config, context, opts)
        }
        )
    }
    context.restore()
    return ChartColumnDataPoints(xAxisPoints = xAxisPoints, yAxisPoints = _uA<Number>(), calPoints = calPoints, eachSpacing = eachSpacing)
}
fun drawAreaDataPoints(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartColumnDataPoints {
    var areaOption = opts.extra.area!!
    var xAxisData = opts.chartData.xAxisData
    var xAxisPoints = xAxisData.xAxisPoints
    var eachSpacing = xAxisData.eachSpacing!!
    var endY = opts.height - opts.area[2]
    var calPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = _uA()
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    context.save()
    var leftSpace: Number = 0
    var rightSpace = opts.width + eachSpacing
    if (opts._scrollDistance_ != null && opts._scrollDistance_ != 0 && opts.enableScroll == true) {
        context.translate(opts._scrollDistance_!!, 0)
        leftSpace = -opts._scrollDistance_!! - eachSpacing * 2 + opts.area[3]
        rightSpace = leftSpace + (opts.xAxis.itemCount + 4) * eachSpacing
    }
    series.forEach(fun(eachSeries, seriesIndex) {
        var stringRanges: UTSArray<String> = opts.chartData.yAxisData.ranges!![eachSeries.index!!]
        var ranges = stringRanges.map(fun(k: String): Number {
            return parseFloat(k)
        }
        )
        var minRange = ranges.pop()!!
        var maxRange = ranges.shift()!!
        var data = eachSeries.data!!
        var points = getDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
        calPoints.push(points)
        var splitPointList = splitPoints(points, eachSeries)
        run {
            var i: Number = 0
            while(i < splitPointList.length){
                var points = splitPointList[i]
                context.beginPath()
                context.strokeStyle = hexToRgb(eachSeries.color!!, areaOption.opacity)
                if (areaOption.gradient) {
                    var gradient = context.createLinearGradient(0 + xOffset, opts.area[0] + yOffset, 0 + xOffset, opts.height - opts.area[2] + yOffset)
                    gradient.addColorStop(0, hexToRgb(eachSeries.color!!, areaOption.opacity))
                    gradient.addColorStop(1, hexToRgb("#FFFFFF", 0.1))
                    context.fillStyle = gradient
                } else {
                    context.fillStyle = hexToRgb(eachSeries.color!!, areaOption.opacity)
                }
                context.lineWidth = areaOption.width * opts.pixelRatio
                if (points.length > 1) {
                    var firstPoint = points[0]
                    var lastPoint = points[points.length - 1]
                    context.moveTo(firstPoint.x + xOffset, firstPoint.y + yOffset)
                    var startPoint: Number = 0
                    if (areaOption.type == "curve") {
                        run {
                            var j: Number = 0
                            while(j < points.length){
                                var item = points[j]
                                if (startPoint == 0 && item.x > leftSpace) {
                                    context.moveTo(item.x + xOffset, item.y + yOffset)
                                    startPoint = 1
                                }
                                if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                    var ctrlPoint = createCurveControlPoints(points, j - 1)
                                    context.bezierCurveTo(ctrlPoint.ctrA.x + xOffset, ctrlPoint.ctrA.y + yOffset, ctrlPoint.ctrB.x + xOffset, ctrlPoint.ctrB.y + yOffset, item.x + xOffset, item.y + yOffset)
                                }
                                j++
                            }
                        }
                    }
                    if (areaOption.type == "straight") {
                        run {
                            var j: Number = 0
                            while(j < points.length){
                                var item = points[j]
                                if (startPoint == 0 && item.x > leftSpace) {
                                    context.moveTo(item.x + xOffset, item.y + yOffset)
                                    startPoint = 1
                                }
                                if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                    context.lineTo(item.x + xOffset, item.y + yOffset)
                                }
                                j++
                            }
                        }
                    }
                    if (areaOption.type == "step") {
                        run {
                            var j: Number = 0
                            while(j < points.length){
                                var item = points[j]
                                if (startPoint == 0 && item.x > leftSpace) {
                                    context.moveTo(item.x + xOffset, item.y + yOffset)
                                    startPoint = 1
                                }
                                if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                    context.lineTo(item.x + xOffset, points[j - 1].y + yOffset)
                                    context.lineTo(item.x + xOffset, item.y + yOffset)
                                }
                                j++
                            }
                        }
                    }
                    context.lineTo(lastPoint.x + xOffset, endY + yOffset)
                    context.lineTo(firstPoint.x + xOffset, endY + yOffset)
                    context.lineTo(firstPoint.x + xOffset, firstPoint.y + yOffset)
                } else {
                    var item = points[0]
                    context.moveTo(item.x - eachSpacing / 2 + xOffset, item.y + yOffset)
                }
                context.closePath()
                context.fill()
                if (areaOption.addLine) {
                    if (eachSeries.lineType == "dash") {
                        var dashLength = eachSeries.dashLength ?: 8
                        dashLength *= opts.pixelRatio
                        context.setLineDash(_uA(
                            dashLength,
                            dashLength
                        ))
                    }
                    context.beginPath()
                    context.strokeStyle = eachSeries.color!!
                    context.lineWidth = areaOption.width * opts.pixelRatio
                    if (points.length == 1) {
                        context.moveTo(points[0].x + xOffset, points[0].y + yOffset)
                    } else {
                        context.moveTo(points[0].x + xOffset, points[0].y + yOffset)
                        var startPoint: Number = 0
                        if (areaOption.type == "curve") {
                            run {
                                var j: Number = 0
                                while(j < points.length){
                                    var item = points[j]
                                    if (startPoint == 0 && item.x > leftSpace) {
                                        context.moveTo(item.x + xOffset, item.y + yOffset)
                                        startPoint = 1
                                    }
                                    if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                        var ctrlPoint = createCurveControlPoints(points, j - 1)
                                        context.bezierCurveTo(ctrlPoint.ctrA.x + xOffset, ctrlPoint.ctrA.y + yOffset, ctrlPoint.ctrB.x + xOffset, ctrlPoint.ctrB.y + yOffset, item.x + xOffset, item.y + yOffset)
                                    }
                                    j++
                                }
                            }
                        }
                        if (areaOption.type == "straight") {
                            run {
                                var j: Number = 0
                                while(j < points.length){
                                    var item = points[j]
                                    if (startPoint == 0 && item.x > leftSpace) {
                                        context.moveTo(item.x + xOffset, item.y + yOffset)
                                        startPoint = 1
                                    }
                                    if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                        context.lineTo(item.x + xOffset, item.y + yOffset)
                                    }
                                    j++
                                }
                            }
                        }
                        if (areaOption.type == "step") {
                            run {
                                var j: Number = 0
                                while(j < points.length){
                                    var item = points[j]
                                    if (startPoint == 0 && item.x > leftSpace) {
                                        context.moveTo(item.x + xOffset, item.y + yOffset)
                                        startPoint = 1
                                    }
                                    if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                        context.lineTo(item.x + xOffset, points[j - 1].y + yOffset)
                                        context.lineTo(item.x + xOffset, item.y + yOffset)
                                    }
                                    j++
                                }
                            }
                        }
                        context.moveTo(points[0].x + xOffset, points[0].y + yOffset)
                    }
                    context.stroke()
                    context.setLineDash(_uA())
                }
                i++
            }
        }
        if (opts.dataPointShape != false) {
            drawPointShape(points, eachSeries.color!!, eachSeries.pointShape, context, opts)
        }
        drawActiveAreaPoint(points, eachSeries.color!!, eachSeries.pointShape, context, opts, areaOption, seriesIndex)
    }
    )
    if (opts.dataLabel != false && process == 1) {
        series.forEach(fun(eachSeries, seriesIndex){
            var stringRanges = opts.chartData.yAxisData.ranges!![eachSeries.index!!]
            var ranges = stringRanges.map(fun(k: String): Number {
                return parseFloat(k)
            }
            )
            var minRange = ranges.pop()!!
            var maxRange = ranges.shift()!!
            var data = eachSeries.data!!
            var points = getDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
            drawPointText(points, eachSeries, config, context, opts)
        }
        )
    }
    context.restore()
    return ChartColumnDataPoints(xAxisPoints = xAxisPoints, yAxisPoints = _uA(), calPoints = calPoints, eachSpacing = eachSpacing)
}
fun getPieDataPoints(series: UTSArray<ChartOptionsSeries>, radius: Number, process: Number = 1): UTSArray<ChartOptionsSeries> {
    var count: Number = 0
    var _start_: Number = 0
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            var kVal = item.data!![0].value!!
            count += kVal
            i++
        }
    }
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            var kVal = item.data!![0].value!!
            if (count == 0) {
                item._proportion_ = (1 as Number) / series.length * process
            } else {
                item._proportion_ = kVal / count * process
            }
            item._radius_ = radius
            i++
        }
    }
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            item._start_ = _start_
            _start_ += 2 * item._proportion_ * Math.PI
            i++
        }
    }
    return series
}
fun isSameSign(num1: Number, num2: Number): Boolean {
    return Math.abs(num1) == num1 && Math.abs(num2) == num2 || Math.abs(num1) != num1 && Math.abs(num2) != num2
}
fun isCollision(obj1: ChartOptionsChartDataLegendDataArea, obj2: ChartOptionsChartDataLegendDataArea): Boolean {
    obj1.end.x = obj1.start.x + obj1.width
    obj1.end.y = obj1.start.y - obj1.height
    obj2.end.x = obj2.start.x + obj2.width
    obj2.end.y = obj2.start.y - obj2.height
    var flag = obj2.start.x > obj1.end.x || obj2.end.x < obj1.start.x || obj2.end.y > obj1.start.y || obj2.start.y < obj1.end.y
    return !flag
}
fun avoidCollision(obj: ChartOptionsChartDataLegendDataArea, target: ChartOptionsChartDataLegendDataArea?): ChartOptionsChartDataLegendDataArea {
    if (target != null) {
        while(isCollision(obj, target!!)){
            if (obj.start.x > 0) {
                obj.start.y--
            } else if (obj.start.x < 0) {
                obj.start.y++
            } else {
                if (obj.start.y > 0) {
                    obj.start.y++
                } else {
                    obj.start.y--
                }
            }
        }
    }
    return obj
}
fun convertCoordinateOrigin(x: Number, y: Number, center: Offset): ChartOptionsChartDataCalPoints {
    val CoordinateOrigin = ChartOptionsChartDataCalPoints()
    CoordinateOrigin.x = center.x + x
    CoordinateOrigin.y = center.y - y
    return CoordinateOrigin
}
fun drawPieText(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, radius: Number, center: Offset) {
    var lineRadius = config.pieChartLinePadding
    var textObjectCollection: UTSArray<ChartOptionsChartDataLegendDataArea> = _uA()
    var lastTextObject: ChartOptionsChartDataLegendDataArea? = null
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    var seriesConvert = series.map(fun(item: ChartOptionsSeries, index: Number): ChartOptionsSeries {
        var text: String = ""
        if (item.formatter != null) {
            val kFun = item.formatter as (item: ChartOptionsSeries, index: Number, series: UTSArray<ChartOptionsSeries>, opts: ChartOptions) -> String
            text = kFun(item, index, series, opts)
        } else {
            text = (item._proportion_ * 100).toFixed(2) + "%"
        }
        text = if (item.data!![0].labelText != null) {
            item.data!![0].labelText!!
        } else {
            text
        }
        var arc = 2 * Math.PI - (item._start_ + 2 * Math.PI * item._proportion_ / 2)
        if (item._rose_proportion_ > 0) {
            arc = 2 * Math.PI - (item._start_ + 2 * Math.PI * item._rose_proportion_ / 2)
        }
        var color = item.color
        var radius = item._radius_
        val seriesitem = ChartOptionsSeries()
        seriesitem.arc = arc
        seriesitem.text = text
        seriesitem.color = color
        seriesitem.radius = radius
        seriesitem.textColor = item.data!![0].textColor!!
        seriesitem.textSize = item.data!![0].textSize
        seriesitem.labelShow = item.data!![0].labelShow
        return seriesitem
    }
    )
    run {
        var i: Number = 0
        while(i < seriesConvert.length){
            var item = seriesConvert[i]
            var orginX1 = Math.cos(item.arc) * (item.radius + lineRadius)
            var orginY1 = Math.sin(item.arc) * (item.radius + lineRadius)
            var orginX2 = Math.cos(item.arc) * item.radius
            var orginY2 = Math.sin(item.arc) * item.radius
            var orginX3 = if (orginX1 >= 0) {
                orginX1 + config.pieChartTextPadding
            } else {
                orginX1 - config.pieChartTextPadding
            }
            var orginY3 = orginY1
            var textWidth = measureText(item.text, (item.textSize ?: config.fontSize) * opts.pixelRatio)
            var startY = orginY3
            if (lastTextObject != null && isSameSign(lastTextObject!!.start.x, orginX3)) {
                if (orginX3 > 0) {
                    startY = Math.min(orginY3, lastTextObject!!.start.y)
                } else if (orginX1 < 0) {
                    startY = Math.max(orginY3, lastTextObject!!.start.y)
                } else {
                    if (orginY3 > 0) {
                        startY = Math.max(orginY3, lastTextObject!!.start.y)
                    } else {
                        startY = Math.min(orginY3, lastTextObject!!.start.y)
                    }
                }
            }
            if (orginX3 < 0) {
                orginX3 -= textWidth
            }
            val textObject = ChartOptionsChartDataLegendDataArea()
            val lineStart = ChartOptionsChartDataLegendDataAreaXY()
            val lineEnd = ChartOptionsChartDataLegendDataAreaXY()
            val start = ChartOptionsChartDataLegendDataAreaXY()
            lineStart.x = orginX2
            lineStart.y = orginY2
            lineEnd.x = orginX1
            lineEnd.y = orginY1
            start.x = orginX3
            start.y = startY
            textObject.lineStart = lineStart
            textObject.lineEnd = lineEnd
            textObject.start = start
            textObject.width = textWidth
            textObject.height = config.fontSize
            textObject.text = item.text
            textObject.color = item.color!!
            textObject.textColor = item.textColor
            textObject.textSize = item.textSize ?: config.fontSize
            lastTextObject = avoidCollision(textObject, lastTextObject)
            textObjectCollection.push(lastTextObject!!)
            i++
        }
    }
    run {
        var i: Number = 0
        while(i < textObjectCollection.length){
            if (seriesConvert[i].labelShow == false) {
                i++
                continue
            }
            var item = textObjectCollection[i]
            var lineStartPoistion = convertCoordinateOrigin(item.lineStart.x, item.lineStart.y, center)
            var lineEndPoistion = convertCoordinateOrigin(item.lineEnd.x, item.lineEnd.y, center)
            var textPosition = convertCoordinateOrigin(item.start.x, item.start.y, center)
            context.lineWidth = 1 * opts.pixelRatio
            context.font = "" + (item.textSize ?: config.fontSize) * opts.pixelRatio + "px sans-serif"
            context.beginPath()
            context.strokeStyle = item.color
            context.fillStyle = item.color
            context.moveTo(xOffset + lineStartPoistion.x, lineStartPoistion.y + yOffset)
            var curveStartX = if (item.start.x < 0) {
                textPosition.x + item.width
            } else {
                textPosition.x
            }
            var textStartX = if (item.start.x < 0) {
                textPosition.x - 5
            } else {
                textPosition.x + 5
            }
            context.quadraticCurveTo(lineEndPoistion.x + xOffset, lineEndPoistion.y + yOffset, curveStartX + xOffset, textPosition.y + yOffset)
            context.moveTo(xOffset + lineStartPoistion.x, lineStartPoistion.y + yOffset)
            context.stroke()
            context.closePath()
            context.beginPath()
            context.moveTo(xOffset + textPosition.x + item.width, textPosition.y + yOffset)
            context.arc(xOffset + curveStartX, textPosition.y + yOffset, 2 * opts.pixelRatio, 0, 2 * Math.PI)
            context.closePath()
            context.fill()
            context.beginPath()
            context.font = "" + (item.textSize ?: config.fontSize) * opts.pixelRatio + "px sans-serif"
            context.fillStyle = item.textColor ?: opts.fontColor
            context.fillText(item.text, textStartX + xOffset, textPosition.y + 3 + yOffset)
            context.closePath()
            context.stroke()
            context.closePath()
            i++
        }
    }
}
fun drawRingTitle(opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, center: Offset) {
    var titlefontSize = opts.title.fontSize ?: config.titleFontSize
    var subtitlefontSize = opts.subtitle.fontSize ?: config.subtitleFontSize
    var title = opts.title.name ?: ""
    var subtitle = opts.subtitle.name ?: ""
    var titleFontColor = opts.title.color ?: opts.fontColor
    var subtitleFontColor = opts.subtitle.color ?: opts.fontColor
    var titleHeight = if (title != "") {
        titlefontSize
    } else {
        0
    }
    var subtitleHeight = if (subtitle != "") {
        subtitlefontSize
    } else {
        0
    }
    var margin: Number = 5
    if (subtitle != "") {
        var textWidth = measureText(subtitle, subtitlefontSize * opts.pixelRatio)
        var startX = center.x - textWidth / 2 + (opts.subtitle.offsetX ?: 0) * opts.pixelRatio
        var startY = center.y + subtitlefontSize * opts.pixelRatio / 2 + (opts.subtitle.offsetY ?: 0) * opts.pixelRatio
        if (title != "") {
            startY += (titleHeight * opts.pixelRatio + margin) / 2
        }
        context.beginPath()
        context.font = "" + subtitlefontSize * opts.pixelRatio + "px sans-serif"
        context.fillStyle = subtitleFontColor
        context.fillText(subtitle, startX + opts.xOffset, startY + opts.yOffset)
        context.closePath()
        context.stroke()
    }
    if (title != "") {
        var _textWidth = measureText(title, titlefontSize * opts.pixelRatio)
        var _startX = center.x - _textWidth / 2 + (opts.title.offsetX ?: 0)
        var _startY = center.y + titlefontSize * opts.pixelRatio / 2 + (opts.title.offsetY ?: 0) * opts.pixelRatio
        if (subtitle != "") {
            _startY -= (subtitleHeight * opts.pixelRatio + margin) / 2
        }
        context.beginPath()
        context.font = "" + titlefontSize * opts.pixelRatio + "px sans-serif"
        context.fillStyle = titleFontColor
        context.fillText(title, _startX + opts.xOffset, _startY + opts.yOffset)
        context.closePath()
        context.stroke()
    }
}
fun drawRingDataPoints(seriese: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartOptionsChartDataToolTiPData {
    var pieOption = opts.extra.ring!!
    val centerPosition = Offset()
    centerPosition.x = opts.area[3] + (opts.width - opts.area[1] - opts.area[3]) / 2
    centerPosition.y = opts.area[0] + (opts.height - opts.area[0] - opts.area[2]) / 2
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    if (config.pieChartLinePadding == 0) {
        config.pieChartLinePadding = pieOption.activeRadius * opts.pixelRatio
    }
    var radius = Math.min((opts.width - opts.area[1] - opts.area[3]) / 2 - config.pieChartLinePadding - config.pieChartTextPadding - config._pieTextMaxLength_, (opts.height - opts.area[0] - opts.area[2]) / 2 - config.pieChartLinePadding - config.pieChartTextPadding)
    radius = if (radius < 10) {
        10
    } else {
        radius
    }
    if (pieOption.customRadius != null && pieOption.customRadius!! > 0) {
        radius = pieOption.customRadius!! * opts.pixelRatio
    }
    var series = getPieDataPoints(seriese, radius, process)
    var activeRadius = pieOption.activeRadius * opts.pixelRatio
    pieOption.customColor = fillCustomColor(pieOption.linearType, pieOption.customColor, series, config)
    series = series.map(fun(eachSeries: ChartOptionsSeries): ChartOptionsSeries {
        eachSeries._start_ += pieOption.offsetAngle!! * Math.PI / 180
        return eachSeries
    }
    )
    series.forEach(fun(eachSeries: ChartOptionsSeries, seriesIndex: Number){
        if (opts.tooltip != null) {
            if ((opts.tooltip!!.index as Number) == seriesIndex) {
                context.beginPath()
                context.fillStyle = hexToRgb(eachSeries.color!!, pieOption.activeOpacity)
                context.moveTo(xOffset + centerPosition.x, centerPosition.y + yOffset)
                context.arc(xOffset + centerPosition.x, centerPosition.y + yOffset, eachSeries._radius_ + activeRadius, eachSeries._start_, eachSeries._start_ + 2 * eachSeries._proportion_ * Math.PI, false)
                context.closePath()
                context.fill()
            }
        }
        context.beginPath()
        context.lineWidth = pieOption.borderWidth * opts.pixelRatio
        context.lineJoin = "round"
        context.strokeStyle = pieOption.borderColor
        var fillcolor: Any = eachSeries.color!!
        if (pieOption.linearType == "custom") {
            var grd = context.createRadialGradient(centerPosition.x + xOffset, centerPosition.y + yOffset, 0, centerPosition.x + xOffset, centerPosition.y + yOffset, eachSeries._radius_)
            grd.addColorStop(0, hexToRgb(pieOption.customColor!![eachSeries.linearIndex!!], 1))
            grd.addColorStop(1, hexToRgb(eachSeries.color!!, 1))
            fillcolor = grd
        }
        context.fillStyle = fillcolor
        context.moveTo(xOffset + centerPosition.x, centerPosition.y + yOffset)
        context.arc(centerPosition.x + xOffset, centerPosition.y + yOffset, eachSeries._radius_, eachSeries._start_, eachSeries._start_ + 2 * eachSeries._proportion_ * Math.PI, false)
        context.closePath()
        context.fill()
        if (pieOption.border == true) {
            context.stroke()
        }
    }
    )
    var innerPieWidth = radius * 0.6
    if (UTSAndroid.`typeof`(pieOption.ringWidth) == "number" && pieOption.ringWidth > 0) {
        innerPieWidth = Math.max(0, radius - pieOption.ringWidth * opts.pixelRatio)
    }
    context.beginPath()
    context.fillStyle = pieOption.centerColor!!
    context.moveTo(xOffset + centerPosition.x, centerPosition.y + yOffset)
    context.arc(xOffset + centerPosition.x, centerPosition.y + yOffset, innerPieWidth, 0, 2 * Math.PI)
    context.closePath()
    context.fill()
    if (opts.dataLabel != false && process == 1) {
        drawPieText(series, opts, config, context, radius, centerPosition)
    }
    if (process == 1) {
        drawRingTitle(opts, config, context, centerPosition)
    }
    val piedata = ChartOptionsChartDataToolTiPData()
    piedata.center = centerPosition
    piedata.radius = radius
    piedata.series = series
    return piedata
}
fun drawPieDataPoints(seriese: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartOptionsChartDataToolTiPData {
    var pieOption = opts.extra.pie!!
    val centerPosition = Offset()
    centerPosition.x = opts.area[3] + (opts.width - opts.area[1] - opts.area[3]) / 2
    centerPosition.y = opts.area[0] + (opts.height - opts.area[0] - opts.area[2]) / 2
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    if (config.pieChartLinePadding == 0) {
        config.pieChartLinePadding = pieOption.activeRadius * opts.pixelRatio
    }
    var radius = Math.min((opts.width - opts.area[1] - opts.area[3]) / 2 - config.pieChartLinePadding - config.pieChartTextPadding - config._pieTextMaxLength_, (opts.height - opts.area[0] - opts.area[2]) / 2 - config.pieChartLinePadding - config.pieChartTextPadding)
    radius = if (radius < 10) {
        10
    } else {
        radius
    }
    if (pieOption.customRadius != null && pieOption.customRadius!! > 0) {
        radius = pieOption.customRadius!! * opts.pixelRatio
    }
    var series = getPieDataPoints(seriese, radius, process)
    var activeRadius = pieOption.activeRadius * opts.pixelRatio
    pieOption.customColor = fillCustomColor(pieOption.linearType, pieOption.customColor, series, config)
    series = series.map(fun(eachSeries: ChartOptionsSeries): ChartOptionsSeries {
        eachSeries._start_ += pieOption.offsetAngle!! * Math.PI / 180
        return eachSeries
    }
    )
    series.forEach(fun(eachSeries: ChartOptionsSeries, seriesIndex: Number){
        if (opts.tooltip != null) {
            if ((opts.tooltip!!.index as Number) == seriesIndex) {
                context.beginPath()
                context.fillStyle = hexToRgb(eachSeries.color!!, pieOption.activeOpacity)
                context.moveTo(xOffset + centerPosition.x, centerPosition.y + yOffset)
                context.arc(xOffset + centerPosition.x, centerPosition.y + yOffset, eachSeries._radius_ + activeRadius, eachSeries._start_, eachSeries._start_ + 2 * eachSeries._proportion_ * Math.PI, false)
                context.closePath()
                context.fill()
            }
        }
        context.beginPath()
        context.lineWidth = pieOption.borderWidth * opts.pixelRatio
        context.lineJoin = "round"
        context.strokeStyle = pieOption.borderColor
        var fillcolor: Any = eachSeries.color!!
        if (pieOption.linearType == "custom") {
            var grd = context.createRadialGradient(centerPosition.x + xOffset, centerPosition.y + yOffset, 0, centerPosition.x + xOffset, centerPosition.y + yOffset, eachSeries._radius_)
            grd.addColorStop(0, hexToRgb(pieOption.customColor!![eachSeries.linearIndex!!], 1))
            grd.addColorStop(1, hexToRgb(eachSeries.color!!, 1))
            fillcolor = grd
        }
        context.fillStyle = fillcolor
        context.moveTo(xOffset + centerPosition.x, centerPosition.y + yOffset)
        context.arc(xOffset + centerPosition.x, centerPosition.y + yOffset, eachSeries._radius_, eachSeries._start_, eachSeries._start_ + 2 * eachSeries._proportion_ * Math.PI, false)
        context.closePath()
        context.fill()
        if (pieOption.border == true) {
            context.stroke()
        }
    }
    )
    if (opts.dataLabel != false && process == 1) {
        drawPieText(series, opts, config, context, radius, centerPosition)
    }
    val piedata = ChartOptionsChartDataToolTiPData()
    piedata.center = centerPosition
    piedata.radius = radius
    piedata.series = series
    return piedata
}
fun getPieTextMaxLength(eseries: UTSArray<ChartOptionsSeries>, config: TuiChartConfig, context: CanvasRenderingContext2D, opts: ChartOptions): Number {
    var series = getPieDataPoints(eseries, 0)
    var maxLength: Number = 0
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            var text: String = ""
            if (item.formatter != null) {
                val kFun = item.formatter as (proportion: String) -> String
                text = kFun(item._proportion_.toFixed(2))
            } else {
                text = (item._proportion_ * 100).toFixed(0) + "%"
            }
            maxLength = Math.max(maxLength, measureText(text, (item.textSize ?: config.fontSize) * opts.pixelRatio))
            i++
        }
    }
    return maxLength
}
fun isInAngleRange(eangle: Number, estartAngle: Number, eendAngle: Number): Boolean {
    fun adjust(eangle: Number): Number {
        var angle = eangle
        while(angle < 0){
            angle += 2 * Math.PI
        }
        while(angle > 2 * Math.PI){
            angle -= 2 * Math.PI
        }
        return angle
    }
    var angle = adjust(eangle)
    var startAngle = adjust(estartAngle)
    var endAngle = adjust(eendAngle)
    if (startAngle > endAngle) {
        endAngle += 2 * Math.PI
        if (angle < startAngle) {
            angle += 2 * Math.PI
        }
    }
    return angle >= startAngle && angle <= endAngle
}
fun isInExactPieChartArea(currentPoints: TouchPosition, center: Offset, radius: Number): Boolean {
    return Math.pow(currentPoints.x - center.x, 2) + Math.pow(currentPoints.y - center.y, 2) <= Math.pow(radius, 2)
}
fun findPieChartCurrentIndex(currentPoints: TouchPosition, pieData: ChartOptionsChartDataToolTiPData, opts: ChartOptions): ChartOptionsTooltip {
    var tooltip: ChartOptionsTooltip = ChartOptionsTooltip()
    var currentIndex: Number = -1
    var series = getPieDataPoints(pieData.series, 0)
    if (isInExactPieChartArea(currentPoints, pieData.center, pieData.radius)) {
        var angle = Math.atan2(pieData.center.y - currentPoints.y, currentPoints.x - pieData.center.x)
        angle = -angle
        if (opts.extra.pie != null && opts.extra.pie!!.offsetAngle != null) {
            angle = angle - opts.extra.pie!!.offsetAngle!! * Math.PI / 180
        }
        if (opts.extra.ring != null && opts.extra.ring!!.offsetAngle != null) {
            angle = angle - opts.extra.ring!!.offsetAngle!! * Math.PI / 180
        }
        run {
            var i: Number = 0
            var len = series.length
            while(i < len){
                if (isInAngleRange(angle, series[i]._start_, series[i]._start_ + series[i]._proportion_ * 2 * Math.PI)) {
                    currentIndex = i
                    break
                }
                i++
            }
        }
    }
    tooltip.index = currentIndex
    return tooltip
}
fun calCandleMA(dayArr: UTSArray<Number>, nameArr: UTSArray<String>, colorArr: UTSArray<String>, kdata: UTSArray<UTSArray<Number>>): UTSArray<ChartOptionsSeries> {
    var seriesTemp: UTSArray<ChartOptionsSeries> = _uA()
    run {
        var k: Number = 0
        while(k < dayArr.length){
            var seriesItem = ChartOptionsSeries()
            seriesItem.data = _uA()
            seriesItem.name = nameArr[k]
            seriesItem.color = colorArr[k]
            run {
                var i: Number = 0
                var len = kdata.length
                while(i < len){
                    val seriesdata = ChartOptionsSeriesData()
                    if (i < dayArr[k]) {
                        seriesdata.value = null
                        seriesItem.data!!.push(seriesdata)
                        i++
                        continue
                    } else {
                        var sum: Number = 0
                        run {
                            var j: Number = 0
                            while(j < dayArr[k]){
                                sum += kdata[i - j][1]
                                j++
                            }
                        }
                        seriesdata.value = parseFloat((sum / dayArr[k]).toFixed(3))
                        seriesItem.data!!.push(seriesdata)
                    }
                    i++
                }
            }
            seriesTemp.push(seriesItem)
            k++
        }
    }
    return seriesTemp
}
fun getRoseDataPoints(series: UTSArray<ChartOptionsSeries>, type: String, minRadius: Number, radius: Number, process: Number = 1): UTSArray<ChartOptionsSeries> {
    var count: Number = 0
    var _start_: Number = 0
    var dataArr: UTSArray<Number> = _uA()
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            count += item.data!![0].value!!
            dataArr.push(item.data!![0].value!!)
            i++
        }
    }
    var minData = Math.min(*dataArr.toTypedArray())
    var maxData = Math.max(*dataArr.toTypedArray())
    var radiusLength = radius - minRadius
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            if (count == 0) {
                item._proportion_ = (1 as Number) / series.length * process
                item._rose_proportion_ = (1 as Number) / series.length * process
            } else {
                val kVal = item.data!![0].value!!
                item._proportion_ = kVal / count * process
                if (type == "area") {
                    item._rose_proportion_ = (1 as Number) / series.length * process
                } else {
                    item._rose_proportion_ = kVal / count * process
                }
            }
            item._radius_ = (minRadius + radiusLength * ((item.data!![0].value!! - minData) / (maxData - minData))) ?: radius
            i++
        }
    }
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            item._start_ = _start_
            _start_ += 2 * item._rose_proportion_ * Math.PI
            i++
        }
    }
    return series
}
fun drawRoseDataPoints(reassignedSeries: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartOptionsChartDataToolTiPData {
    var series = reassignedSeries
    var roseOption = opts.extra.rose!!
    if (config.pieChartLinePadding == 0) {
        config.pieChartLinePadding = roseOption.activeRadius * opts.pixelRatio
    }
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    var centerPosition = Offset()
    centerPosition.x = opts.area[3] + (opts.width - opts.area[1] - opts.area[3]) / 2
    centerPosition.y = opts.area[0] + (opts.height - opts.area[0] - opts.area[2]) / 2
    var radius = Math.min((opts.width - opts.area[1] - opts.area[3]) / 2 - config.pieChartLinePadding - config.pieChartTextPadding - config._pieTextMaxLength_, (opts.height - opts.area[0] - opts.area[2]) / 2 - config.pieChartLinePadding - config.pieChartTextPadding)
    radius = if (radius < 10) {
        10
    } else {
        radius
    }
    var minRadius = roseOption.minRadius ?: radius * 0.5
    if (radius < minRadius) {
        radius = minRadius + 10
    }
    series = getRoseDataPoints(series, roseOption.type, minRadius, radius, process)
    var activeRadius = roseOption.activeRadius * opts.pixelRatio
    roseOption.customColor = fillCustomColor(roseOption.linearType, roseOption.customColor, series, config)
    series = series.map(fun(eachSeries: ChartOptionsSeries): ChartOptionsSeries {
        eachSeries._start_ += (roseOption.offsetAngle ?: 0) * Math.PI / 180
        return eachSeries
    }
    )
    series.forEach(fun(eachSeries, seriesIndex) {
        if (opts.tooltip != null) {
            if ((opts.tooltip!!.index as Number) == seriesIndex) {
                context.beginPath()
                context.fillStyle = hexToRgb(eachSeries.color!!, roseOption.activeOpacity)
                context.moveTo(xOffset + centerPosition.x, centerPosition.y + yOffset)
                context.arc(xOffset + centerPosition.x, centerPosition.y + yOffset, activeRadius + eachSeries._radius_, eachSeries._start_, eachSeries._start_ + 2 * eachSeries._rose_proportion_ * Math.PI, false)
                context.closePath()
                context.fill()
            }
        }
        context.beginPath()
        context.lineWidth = roseOption.borderWidth * opts.pixelRatio
        context.lineJoin = "round"
        context.strokeStyle = roseOption.borderColor
        var fillcolor: Any = eachSeries.color!!
        if (roseOption.linearType == "custom") {
            var grd = context.createRadialGradient(centerPosition.x + xOffset, centerPosition.y + yOffset, 0, centerPosition.x + xOffset, centerPosition.y + yOffset, eachSeries._radius_)
            grd.addColorStop(0, hexToRgb(roseOption.customColor!![eachSeries.linearIndex!!], 1))
            grd.addColorStop(1, hexToRgb(eachSeries.color!!, 1))
            fillcolor = grd
        }
        context.fillStyle = fillcolor
        context.moveTo(xOffset + centerPosition.x, centerPosition.y + yOffset)
        context.arc(xOffset + centerPosition.x, centerPosition.y + yOffset, eachSeries._radius_, eachSeries._start_, eachSeries._start_ + 2 * eachSeries._rose_proportion_ * Math.PI, false)
        context.closePath()
        context.fill()
        if (roseOption.border == true) {
            context.stroke()
        }
    }
    )
    if (opts.dataLabel != false && process == 1) {
        drawPieText(series, opts, config, context, radius, centerPosition)
    }
    val tooldata = ChartOptionsChartDataToolTiPData()
    tooldata.center = centerPosition
    tooldata.radius = radius
    tooldata.series = series
    return tooldata
}
fun findRoseChartCurrentIndex(currentPoints: TouchPosition, pieData: ChartOptionsChartDataToolTiPData, opts: ChartOptions): ChartOptionsTooltip {
    var tooltip: ChartOptionsTooltip = ChartOptionsTooltip()
    var currentIndex: Number = -1
    var series = getRoseDataPoints(opts._series_!!, opts.extra.rose!!.type, pieData.radius, pieData.radius)
    if (isInExactPieChartArea(currentPoints, pieData.center, pieData.radius)) {
        var angle = Math.atan2(pieData.center.y - currentPoints.y, currentPoints.x - pieData.center.x)
        angle = -angle
        if (opts.extra.rose != null && opts.extra.rose!!.offsetAngle != null) {
            angle = angle - opts.extra.rose!!.offsetAngle!! * Math.PI / 180
        }
        run {
            var i: Number = 0
            var len = series.length
            while(i < len){
                if (isInAngleRange(angle, series[i]._start_, series[i]._start_ + series[i]._rose_proportion_ * 2 * Math.PI)) {
                    currentIndex = i
                    break
                }
                i++
            }
        }
    }
    tooltip.index = currentIndex
    return tooltip
}
fun approximatelyEqual(num1: Number, num2: Number): Boolean {
    return Math.abs(num1 - num2) < 1e-10
}
fun drawRadarLabel(angleList: UTSArray<Number>, radius: Number, centerPosition: Offset, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D) {
    var radarOption = opts.extra.radar ?: ChartOptionsExtraRadar()
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    angleList.forEach(fun(angle, index) {
        if (radarOption.labelPointShow == true && opts.categories!![index] != "") {
            val posPoint = Offset()
            posPoint.x = radius * Math.cos(angle)
            posPoint.y = radius * Math.sin(angle)
            var posPointAxis = convertCoordinateOrigin(posPoint.x, posPoint.y, centerPosition)
            context.fillStyle = radarOption.labelPointColor
            context.beginPath()
            context.arc(posPointAxis.x + xOffset, posPointAxis.y + yOffset, radarOption.labelPointRadius * opts.pixelRatio, 0, 2 * Math.PI, false)
            context.closePath()
            context.fill()
        }
        if (radarOption.labelShow == true) {
            val pos = Offset()
            pos.x = (radius + config.radarLabelTextMargin * opts.pixelRatio) * Math.cos(angle)
            pos.y = (radius + config.radarLabelTextMargin * opts.pixelRatio) * Math.sin(angle)
            var posRelativeCanvas = convertCoordinateOrigin(pos.x, pos.y, centerPosition)
            var startX = posRelativeCanvas.x
            var startY = posRelativeCanvas.y
            if (approximatelyEqual(pos.x, 0)) {
                startX -= measureText(opts.categories!![index] ?: "", config.fontSize) / 2
            } else if (pos.x < 0) {
                startX -= measureText(opts.categories!![index] ?: "", config.fontSize)
            }
            context.beginPath()
            context.font = "" + config.fontSize + "px sans-serif"
            context.fillStyle = radarOption.labelColor ?: opts.fontColor
            context.fillText(opts.categories!![index] ?: "", startX + xOffset, startY + config.fontSize / 2 + yOffset)
            context.closePath()
            context.stroke()
        }
    }
    )
}
fun getRadarDataPoints(angleList: UTSArray<Number>, center: Offset, radius: Number, series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, process: Number = 1): UTSArray<ChartOptionsSeries> {
    var radarOption = opts.extra.radar ?: ChartOptionsExtraRadar()
    radarOption.max = radarOption.max ?: 0
    var flierSeries: UTSArray<ChartOptionsSeriesData> = dataCombine(series)
    var seriesValue: UTSArray<Number> = flierSeries.map(fun(item: ChartOptionsSeriesData): Number {
        return item.value!!
    }
    )
    var maxData = Math.max(radarOption.max!!, Math.max(*seriesValue.toTypedArray()))
    var data: UTSArray<ChartOptionsSeries> = _uA()
    run {
        var i: Number = 0
        while(i < series.length){
            var each: ChartOptionsSeries = series[i]
            var listItem = ChartOptionsSeries()
            listItem.color = each.color
            listItem.legendShape = each.legendShape
            listItem.pointShape = each.pointShape
            listItem.data = _uA()
            each.data!!.forEach(fun(item: ChartOptionsSeriesData, index: Number){
                var tmp = ChartOptionsSeriesData()
                tmp.angle = angleList[index]
                tmp.proportion = item.value!! / maxData
                tmp.value = item.value
                tmp.position = convertCoordinateOrigin(radius * tmp.proportion * process * Math.cos(tmp.angle), radius * tmp.proportion * process * Math.sin(tmp.angle), center)
                listItem.data!!.push(tmp)
            }
            )
            data.push(listItem)
            i++
        }
    }
    return data
}
fun getMaxTextListLength(list: UTSArray<String>, fontSize: Number, context: CanvasRenderingContext2D): Number {
    var lengthList = list.map(fun(item: String): Number {
        return measureText(item, fontSize)
    }
    )
    return Math.max(*lengthList.toTypedArray())
}
fun getRadarCoordinateSeries(length: Number): UTSArray<Number> {
    var eachAngle = 2 * Math.PI / length
    var CoordinateSeries: UTSArray<Number> = _uA()
    run {
        var i: Number = 0
        while(i < length){
            CoordinateSeries.push(eachAngle * i)
            i++
        }
    }
    return CoordinateSeries.map(fun(item: Number): Number {
        return -1 * item + Math.PI / 2
    }
    )
}
fun drawRadarDataPoints(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartOptionsChartDataToolTiPData {
    var radarOption: ChartOptionsExtraRadar = opts.extra.radar!!
    var coordinateAngle: UTSArray<Number> = getRadarCoordinateSeries(opts.categories!!.length)
    var centerPosition = Offset()
    centerPosition.x = opts.area[3] + (opts.width - opts.area[1] - opts.area[3]) / 2
    centerPosition.y = opts.area[0] + (opts.height - opts.area[0] - opts.area[2]) / 2
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    var xr = (opts.width - opts.area[1] - opts.area[3]) / 2
    var yr = (opts.height - opts.area[0] - opts.area[2]) / 2
    var radius = Math.min(xr - (getMaxTextListLength(opts.categories!!, config.fontSize, context) + config.radarLabelTextMargin), yr - config.radarLabelTextMargin)
    radius -= config.radarLabelTextMargin * opts.pixelRatio
    radius = if (radius < 10) {
        10
    } else {
        radius
    }
    radius = if (radarOption.radius != null) {
        radarOption.radius!!
    } else {
        radius
    }
    context.beginPath()
    context.lineWidth = 1 * opts.pixelRatio
    context.strokeStyle = radarOption.gridColor
    coordinateAngle.forEach(fun(angle, index) {
        var pos: ChartOptionsChartDataCalPoints = convertCoordinateOrigin(radius * Math.cos(angle), radius * Math.sin(angle), centerPosition)
        context.moveTo(xOffset + centerPosition.x, centerPosition.y + yOffset)
        if (index % radarOption.gridEval == 0) {
            context.lineTo(pos.x + xOffset, pos.y + yOffset)
        }
    }
    )
    context.stroke()
    context.closePath()
    fun _loop(i: Number) {
        var startPos = ChartOptionsChartDataCalPoints()
        context.beginPath()
        context.lineWidth = (1 * opts.pixelRatio)
        context.strokeStyle = radarOption.gridColor
        if (radarOption.gridType == "radar") {
            coordinateAngle.forEach(fun(angle, index) {
                var pos = convertCoordinateOrigin(radius / radarOption.gridCount * i * Math.cos(angle), radius / radarOption.gridCount * i * Math.sin(angle), centerPosition)
                if (index == 0) {
                    startPos = pos
                    context.moveTo(pos.x + xOffset, pos.y + yOffset)
                } else {
                    context.lineTo(pos.x + xOffset, pos.y + yOffset)
                }
            })
            context.lineTo(startPos.x + xOffset, startPos.y + yOffset)
        } else {
            var pos = convertCoordinateOrigin(radius / radarOption.gridCount * i * Math.cos(1.5), radius / radarOption.gridCount * i * Math.sin(1.5), centerPosition)
            context.arc(centerPosition.x + xOffset, centerPosition.y + yOffset, centerPosition.y - pos.y, 0, 2 * Math.PI, false)
        }
        context.stroke()
        context.closePath()
    }
    run {
        var i: Number = 1
        while(i <= radarOption.gridCount){
            _loop(i)
            i++
        }
    }
    radarOption.customColor = fillCustomColor(radarOption.linearType, radarOption.customColor, series, config)
    var radarDataPoints: UTSArray<ChartOptionsSeries> = getRadarDataPoints(coordinateAngle, centerPosition, radius, series, opts, process)
    radarDataPoints.forEach(fun(eachSeries: ChartOptionsSeries, seriesIndex: Number){
        context.beginPath()
        context.lineWidth = radarOption.borderWidth * opts.pixelRatio
        context.strokeStyle = eachSeries.color!!
        var fillcolor: Any = hexToRgb(eachSeries.color!!, radarOption.opacity)
        if (radarOption.linearType == "custom") {
            var grd = context.createRadialGradient(centerPosition.x + xOffset, centerPosition.y + yOffset, 0, centerPosition.x + xOffset, centerPosition.y + yOffset, radius)
            grd.addColorStop(0, hexToRgb(radarOption.customColor!![series[seriesIndex].linearIndex!!], radarOption.opacity))
            grd.addColorStop(1, hexToRgb(eachSeries.color!!, radarOption.opacity))
            fillcolor = grd
        }
        context.fillStyle = fillcolor
        eachSeries.data!!.forEach(fun(item: ChartOptionsSeriesData, index: Number){
            if (index == 0) {
                context.moveTo(item.position.x + xOffset, item.position.y + yOffset)
            } else {
                context.lineTo(item.position.x + xOffset, item.position.y + yOffset)
            }
        }
        )
        context.closePath()
        context.fill()
        if (radarOption.border == true) {
            context.stroke()
        }
        context.closePath()
        if (opts.dataPointShape != false) {
            var points = eachSeries.data!!.map(fun(item: ChartOptionsSeriesData): ChartOptionsChartDataCalPoints {
                return item.position
            }
            )
            drawPointShape(points, eachSeries.color!!, eachSeries.pointShape, context, opts)
        }
    }
    )
    if (radarOption.axisLabel == true) {
        var flierSeries = dataCombine(series)
        var seriesValue = flierSeries.map(fun(item: ChartOptionsSeriesData): Number {
            return item.value!!
        }
        )
        var maxData = Math.max(radarOption.max!!, Math.max(*seriesValue.toTypedArray()))
        val stepLength = radius / radarOption.gridCount
        val fontSize = opts.fontSize * opts.pixelRatio
        context.font = "" + fontSize + "px sans-serif"
        context.fillStyle = opts.fontColor
        context.textAlign = "left"
        run {
            var i: Number = 0
            while(i < radarOption.gridCount + 1){
                var label = i * maxData / radarOption.gridCount
                label = parseFloat(label.toFixed(radarOption.axisLabelTofix))
                context.fillText("" + label, centerPosition.x + 3 * opts.pixelRatio + xOffset, centerPosition.y - i * stepLength + fontSize / 2 + yOffset)
                i++
            }
        }
    }
    drawRadarLabel(coordinateAngle, radius, centerPosition, opts, config, context)
    if (opts.dataLabel && process == 1) {
        radarDataPoints.forEach(fun(eachSeries, seriesIndex) {
            context.beginPath()
            var fontSize = if (eachSeries.textSize != null) {
                eachSeries.textSize!! * opts.pixelRatio
            } else {
                config.fontSize
            }
            context.font = "" + fontSize + "px sans-serif"
            context.fillStyle = eachSeries.textColor ?: opts.fontColor
            eachSeries.data!!.forEach(fun(item: ChartOptionsSeriesData, index: Number){
                if (Math.abs(item.position.x - centerPosition.x) < 2) {
                    if (item.position.y < centerPosition.y) {
                        context.textAlign = "center"
                        context.fillText("" + item.value, item.position.x + xOffset, item.position.y - 4 + yOffset)
                    } else {
                        context.textAlign = "center"
                        context.fillText("" + item.value, item.position.x + xOffset, item.position.y + fontSize + 2 + yOffset)
                    }
                } else {
                    if (item.position.x < centerPosition.x) {
                        context.textAlign = "right"
                        context.fillText("" + item.value, item.position.x - 4 + xOffset, item.position.y + fontSize / 2 - 2 + yOffset)
                    } else {
                        context.textAlign = "left"
                        context.fillText("" + item.value, item.position.x + 4 + xOffset, item.position.y + fontSize / 2 - 2 + yOffset)
                    }
                }
            }
            )
            context.closePath()
            context.stroke()
        }
        )
        context.textAlign = "left"
    }
    val tooltip = ChartOptionsChartDataToolTiPData()
    tooltip.center = centerPosition
    tooltip.radius = radius
    tooltip.angleList = coordinateAngle
    return tooltip
}
fun findRadarChartCurrentIndex(currentPoints: TouchPosition, radarData: ChartOptionsChartDataToolTiPData, count: Number): ChartOptionsTooltip {
    var tooltip: ChartOptionsTooltip = ChartOptionsTooltip()
    var eachAngleArea = 2 * Math.PI / count
    var currentIndex: Number = -1
    if (isInExactPieChartArea(currentPoints, radarData.center, radarData.radius)) {
        fun fixAngle(eangle: Number): Number {
            var angle = eangle
            if (angle < 0) {
                angle += 2 * Math.PI
            }
            if (angle > 2 * Math.PI) {
                angle -= 2 * Math.PI
            }
            return angle
        }
        var angle = Math.atan2(radarData.center.y - currentPoints.y, currentPoints.x - radarData.center.x)
        angle = -1 * angle
        if (angle < 0) {
            angle += 2 * Math.PI
        }
        var angleList = radarData.angleList.map(fun(reassignedItem): Number {
            var item = reassignedItem
            item = fixAngle(-1 * item)
            return item
        }
        )
        angleList.forEach(fun(item, index) {
            var rangeStart = fixAngle(item - eachAngleArea / 2)
            var rangeEnd = fixAngle(item + eachAngleArea / 2)
            if (rangeEnd < rangeStart) {
                rangeEnd += 2 * Math.PI
            }
            if (angle >= rangeStart && angle <= rangeEnd || angle + 2 * Math.PI >= rangeStart && angle + 2 * Math.PI <= rangeEnd) {
                currentIndex = index
            }
        }
        )
    }
    tooltip.index = currentIndex
    return tooltip
}
fun getArcbarDataPoints(series: UTSArray<ChartOptionsSeries>, arcbarOption: ChartOptionsExtraArcbar, eprocess: Number = 1): UTSArray<ChartOptionsSeries> {
    var process = eprocess
    if (process == 1) {
        process = 0.999999
    }
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            var totalAngle: Number
            if (arcbarOption.type == "circle") {
                totalAngle = 2
            } else {
                if (arcbarOption.direction == "ccw") {
                    if (arcbarOption.startAngle < arcbarOption.endAngle) {
                        totalAngle = 2 + arcbarOption.startAngle - arcbarOption.endAngle
                    } else {
                        totalAngle = arcbarOption.startAngle - arcbarOption.endAngle
                    }
                } else {
                    if (arcbarOption.endAngle < arcbarOption.startAngle) {
                        totalAngle = 2 + arcbarOption.endAngle - arcbarOption.startAngle
                    } else {
                        totalAngle = arcbarOption.startAngle - arcbarOption.endAngle
                    }
                }
            }
            val kVal = item.data!![0].value!!
            item._proportion_ = totalAngle * kVal * process + arcbarOption.startAngle
            if (arcbarOption.direction == "ccw") {
                item._proportion_ = arcbarOption.startAngle - totalAngle * kVal * process
            }
            if (item._proportion_ >= 2) {
                item._proportion_ = item._proportion_ % 2
            }
            i++
        }
    }
    return series
}
fun findFunnelChartCurrentIndex(currentPoints: TouchPosition, funnelData: ChartOptionsChartDataToolTiPData): ChartOptionsTooltip {
    var tooltip: ChartOptionsTooltip = ChartOptionsTooltip()
    var currentIndex: Number = -1
    run {
        var i: Number = 0
        var len = funnelData.series.length
        while(i < len){
            var item = funnelData.series[i]
            if (currentPoints.x > item.funnelArea[0] && currentPoints.x < item.funnelArea[2] && currentPoints.y > item.funnelArea[1] && currentPoints.y < item.funnelArea[3]) {
                currentIndex = i
                break
            }
            i++
        }
    }
    tooltip.index = currentIndex
    return tooltip
}
fun getFunnelDataPoints(series: UTSArray<ChartOptionsSeries>, radius: Number, option: ChartOptionsExtraFunnel, eachSpacing: Number, process: Number = 1): UTSArray<ChartOptionsSeries> {
    run {
        var i: Number = 0
        while(i < series.length){
            val kVal = series[i].data!![0].value!!
            if (option.type == "funnel") {
                series[i].radius = kVal / series[0].data!![0].value!! * radius * process
            } else {
                series[i].radius = (eachSpacing * (series.length - i)) / (eachSpacing * series.length) * radius * process
            }
            series[i]._proportion_ = kVal / series[0].data!![0].value!!
            i++
        }
    }
    return series
}
fun drawFunnelText(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, context: CanvasRenderingContext2D, eachSpacing: Number, labelAlign: String, activeWidth: Number, centerPosition: Offset) {
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            if (item.labelShow == false) {
                i++
                continue
            }
            var startX: Number = 0
            var endX: Number = 0
            var startY: Number = 0
            var fontSize: Number = 0
            var text: String = ""
            if (item.formatter != null) {
                val kFun = item.formatter as (item: ChartOptionsSeries, i: Number, series: UTSArray<ChartOptionsSeries>, opts: ChartOptions) -> String
                text = kFun(item, i, series, opts)
            } else {
                text = (item._proportion_ * 100).toFixed(0) + "%"
            }
            text = item.labelText ?: text
            if (labelAlign == "right") {
                if (i == series.length - 1) {
                    startX = (item.funnelArea[2] + centerPosition.x) / 2
                } else {
                    startX = (item.funnelArea[2] + series[i + 1].funnelArea[2]) / 2
                }
                endX = startX + activeWidth * 2
                startY = item.funnelArea[1] + eachSpacing / 2
                fontSize = (item.textSize ?: opts.fontSize) * opts.pixelRatio
                context.lineWidth = 1 * opts.pixelRatio
                context.strokeStyle = item.color!!
                context.fillStyle = item.color!!
                context.beginPath()
                context.moveTo(startX, startY)
                context.lineTo(endX, startY)
                context.stroke()
                context.closePath()
                context.beginPath()
                context.moveTo(endX, startY)
                context.arc(endX, startY, 2 * opts.pixelRatio, 0, 2 * Math.PI)
                context.closePath()
                context.fill()
                context.beginPath()
                context.font = "" + fontSize + "px sans-serif"
                context.fillStyle = item.textColor ?: opts.fontColor
                context.fillText(text, endX + 5, startY + fontSize / 2 - 2)
                context.closePath()
                context.stroke()
                context.closePath()
            }
            if (labelAlign == "left") {
                if (i == series.length - 1) {
                    startX = (item.funnelArea[0] + centerPosition.x) / 2
                } else {
                    startX = (item.funnelArea[0] + series[i + 1].funnelArea[0]) / 2
                }
                endX = startX - activeWidth * 2
                startY = item.funnelArea[1] + eachSpacing / 2
                fontSize = (item.textSize ?: opts.fontSize) * opts.pixelRatio
                context.lineWidth = 1 * opts.pixelRatio
                context.strokeStyle = item.color!!
                context.fillStyle = item.color!!
                context.beginPath()
                context.moveTo(startX, startY)
                context.lineTo(endX, startY)
                context.stroke()
                context.closePath()
                context.beginPath()
                context.moveTo(endX, startY)
                context.arc(endX, startY, 2, 0, 2 * Math.PI)
                context.closePath()
                context.fill()
                context.beginPath()
                context.font = "" + fontSize + "px sans-serif"
                context.fillStyle = item.textColor ?: opts.fontColor
                context.fillText(text, endX - 5 - measureText(text, fontSize), startY + fontSize / 2 - 2)
                context.closePath()
                context.stroke()
                context.closePath()
            }
            i++
        }
    }
}
fun drawFunnelCenterText(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, context: CanvasRenderingContext2D, eachSpacing: Number, labelAlign: String, activeWidth: Number, centerPosition: Offset) {
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            var startY: Number = 0
            var fontSize: Number = 0
            val text = item.data!![0].centerText!!
            if (text != null) {
                startY = item.funnelArea[1] + eachSpacing / 2
                fontSize = if (item.data!![0].centerTextSize != null) {
                    item.data!![0].centerTextSize!! * opts.pixelRatio
                } else {
                    opts.fontSize * opts.pixelRatio
                }
                context.beginPath()
                context.font = "" + fontSize + "px sans-serif"
                context.fillStyle = item.centerTextColor
                context.fillText(text, centerPosition.x - measureText(text, fontSize) / 2, startY + fontSize / 2 - 2)
                context.closePath()
                context.stroke()
                context.closePath()
            }
            i++
        }
    }
}
fun drawFunnelDataPoints(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartOptionsChartDataToolTiPData {
    var funnelOption: ChartOptionsExtraFunnel = opts.extra.funnel!!
    var eachSpacing: Number = (opts.height - opts.area[0] - opts.area[2]) / series.length
    var centerPosition: Offset = Offset()
    centerPosition.x = opts.area[3] + (opts.width - opts.area[1] - opts.area[3]) / 2
    centerPosition.y = opts.height - opts.area[2]
    var activeWidth = funnelOption.activeWidth * opts.pixelRatio
    var radius = Math.min((opts.width - opts.area[1] - opts.area[3]) / 2 - activeWidth, (opts.height - opts.area[0] - opts.area[2]) / 2 - activeWidth)
    var seriesNew: UTSArray<ChartOptionsSeries> = getFunnelDataPoints(series, radius, funnelOption, eachSpacing, process)
    context.save()
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    context.translate(centerPosition.x + xOffset, centerPosition.y + yOffset)
    funnelOption.customColor = fillCustomColor(funnelOption.linearType, funnelOption.customColor, series, config)
    if (funnelOption.type == "pyramid") {
        run {
            var i: Number = 0
            while(i < seriesNew.length){
                if (i == seriesNew.length - 1) {
                    if (opts.tooltip != null) {
                        if ((opts.tooltip!!.index as Number) == i) {
                            context.beginPath()
                            context.fillStyle = hexToRgb(seriesNew[i].color!!, funnelOption.activeOpacity)
                            context.moveTo(-activeWidth, -eachSpacing)
                            context.lineTo(-seriesNew[i].radius - activeWidth, 0)
                            context.lineTo(seriesNew[i].radius + activeWidth, 0)
                            context.lineTo(activeWidth, -eachSpacing)
                            context.lineTo(-activeWidth, -eachSpacing)
                            context.closePath()
                            context.fill()
                        }
                    }
                    seriesNew[i].funnelArea = _uA(
                        centerPosition.x + xOffset - seriesNew[i].radius,
                        centerPosition.y + yOffset - eachSpacing * (i + 1),
                        centerPosition.x + xOffset + seriesNew[i].radius,
                        centerPosition.y + yOffset - eachSpacing * i
                    )
                    context.beginPath()
                    context.lineWidth = (funnelOption.borderWidth * opts.pixelRatio)
                    context.strokeStyle = funnelOption.borderColor
                    var fillColor: Any = hexToRgb(seriesNew[i].color!!, funnelOption.fillOpacity)
                    if (funnelOption.linearType == "custom") {
                        var grd = context.createLinearGradient(seriesNew[i].radius, -eachSpacing, -seriesNew[i].radius, -eachSpacing)
                        grd.addColorStop(0, hexToRgb(seriesNew[i].color!!, funnelOption.fillOpacity))
                        grd.addColorStop(0.5, hexToRgb(funnelOption.customColor!![seriesNew[i].linearIndex!!], funnelOption.fillOpacity))
                        grd.addColorStop(1, hexToRgb(seriesNew[i].color!!, funnelOption.fillOpacity))
                        fillColor = grd
                    }
                    context.fillStyle = fillColor
                    context.moveTo(0, -eachSpacing)
                    context.lineTo(-seriesNew[i].radius, 0)
                    context.lineTo(seriesNew[i].radius, 0)
                    context.lineTo(0, -eachSpacing)
                    context.closePath()
                    context.fill()
                    if (funnelOption.border == true) {
                        context.stroke()
                    }
                } else {
                    if (opts.tooltip != null) {
                        if ((opts.tooltip!!.index as Number) == i) {
                            context.beginPath()
                            context.fillStyle = hexToRgb(seriesNew[i].color!!, funnelOption.activeOpacity)
                            context.moveTo(0, 0)
                            context.lineTo(-seriesNew[i].radius - activeWidth, 0)
                            context.lineTo(-seriesNew[i + 1].radius - activeWidth, -eachSpacing)
                            context.lineTo(seriesNew[i + 1].radius + activeWidth, -eachSpacing)
                            context.lineTo(seriesNew[i].radius + activeWidth, 0)
                            context.lineTo(0, 0)
                            context.closePath()
                            context.fill()
                        }
                    }
                    seriesNew[i].funnelArea = _uA(
                        centerPosition.x + xOffset - seriesNew[i].radius,
                        centerPosition.y + yOffset - eachSpacing * (i + 1),
                        centerPosition.x + xOffset + seriesNew[i].radius,
                        centerPosition.y + yOffset - eachSpacing * i
                    )
                    context.beginPath()
                    context.lineWidth = (funnelOption.borderWidth * opts.pixelRatio)
                    context.strokeStyle = funnelOption.borderColor
                    var fillColor: Any = hexToRgb(seriesNew[i].color!!, funnelOption.fillOpacity)
                    if (funnelOption.linearType == "custom") {
                        var grd = context.createLinearGradient(seriesNew[i].radius, -eachSpacing, -seriesNew[i].radius, -eachSpacing)
                        grd.addColorStop(0, hexToRgb(seriesNew[i].color!!, funnelOption.fillOpacity))
                        grd.addColorStop(0.5, hexToRgb(funnelOption.customColor!![seriesNew[i].linearIndex!!], funnelOption.fillOpacity))
                        grd.addColorStop(1, hexToRgb(seriesNew[i].color!!, funnelOption.fillOpacity))
                        fillColor = grd
                    }
                    context.fillStyle = fillColor
                    context.moveTo(0, 0)
                    context.lineTo(-seriesNew[i].radius, 0)
                    context.lineTo(-seriesNew[i + 1].radius, -eachSpacing)
                    context.lineTo(seriesNew[i + 1].radius, -eachSpacing)
                    context.lineTo(seriesNew[i].radius, 0)
                    context.lineTo(0, 0)
                    context.closePath()
                    context.fill()
                    if (funnelOption.border == true) {
                        context.stroke()
                    }
                }
                context.translate(0, -eachSpacing)
                i++
            }
        }
    } else {
        context.translate(0, -(seriesNew.length - 1) * eachSpacing)
        run {
            var i: Number = 0
            while(i < seriesNew.length){
                if (i == seriesNew.length - 1) {
                    if (opts.tooltip != null) {
                        if ((opts.tooltip!!.index as Number) == i) {
                            context.beginPath()
                            context.fillStyle = hexToRgb(seriesNew[i].color!!, funnelOption.activeOpacity)
                            context.moveTo(-activeWidth - funnelOption.minSize / 2, 0)
                            context.lineTo(-seriesNew[i].radius - activeWidth, -eachSpacing)
                            context.lineTo(seriesNew[i].radius + activeWidth, -eachSpacing)
                            context.lineTo(activeWidth + funnelOption.minSize / 2, 0)
                            context.lineTo(-activeWidth - funnelOption.minSize / 2, 0)
                            context.closePath()
                            context.fill()
                        }
                    }
                    seriesNew[i].funnelArea = _uA(
                        centerPosition.x + xOffset - seriesNew[i].radius,
                        centerPosition.y + yOffset - eachSpacing,
                        centerPosition.x + xOffset + seriesNew[i].radius,
                        centerPosition.y + yOffset
                    )
                    context.beginPath()
                    context.lineWidth = (funnelOption.borderWidth * opts.pixelRatio)
                    context.strokeStyle = funnelOption.borderColor
                    var fillColor: Any = hexToRgb(seriesNew[i].color!!, funnelOption.fillOpacity)
                    if (funnelOption.linearType == "custom") {
                        var grd = context.createLinearGradient(seriesNew[i].radius, -eachSpacing, -seriesNew[i].radius, -eachSpacing)
                        grd.addColorStop(0, hexToRgb(seriesNew[i].color!!, funnelOption.fillOpacity))
                        grd.addColorStop(0.5, hexToRgb(funnelOption.customColor!![seriesNew[i].linearIndex!!], funnelOption.fillOpacity))
                        grd.addColorStop(1, hexToRgb(seriesNew[i].color!!, funnelOption.fillOpacity))
                        fillColor = grd
                    }
                    context.fillStyle = fillColor
                    context.moveTo(0, 0)
                    context.lineTo(-funnelOption.minSize / 2, 0)
                    context.lineTo(-seriesNew[i].radius, -eachSpacing)
                    context.lineTo(seriesNew[i].radius, -eachSpacing)
                    context.lineTo(funnelOption.minSize / 2, 0)
                    context.lineTo(0, 0)
                    context.closePath()
                    context.fill()
                    if (funnelOption.border == true) {
                        context.stroke()
                    }
                } else {
                    if (opts.tooltip != null) {
                        if ((opts.tooltip!!.index as Number) == i) {
                            context.beginPath()
                            context.fillStyle = hexToRgb(seriesNew[i].color!!, funnelOption.activeOpacity)
                            context.moveTo(0, 0)
                            context.lineTo(-seriesNew[i + 1].radius - activeWidth, 0)
                            context.lineTo(-seriesNew[i].radius - activeWidth, -eachSpacing)
                            context.lineTo(seriesNew[i].radius + activeWidth, -eachSpacing)
                            context.lineTo(seriesNew[i + 1].radius + activeWidth, 0)
                            context.lineTo(0, 0)
                            context.closePath()
                            context.fill()
                        }
                    }
                    seriesNew[i].funnelArea = _uA(
                        centerPosition.x + xOffset - seriesNew[i].radius,
                        centerPosition.y + yOffset - eachSpacing * (seriesNew.length - i),
                        centerPosition.x + xOffset + seriesNew[i].radius,
                        centerPosition.y + yOffset - eachSpacing * (seriesNew.length - i - 1)
                    )
                    context.beginPath()
                    context.lineWidth = funnelOption.borderWidth * opts.pixelRatio
                    context.strokeStyle = funnelOption.borderColor
                    var fillColor: Any = hexToRgb(seriesNew[i].color!!, funnelOption.fillOpacity)
                    if (funnelOption.linearType == "custom") {
                        var grd = context.createLinearGradient(seriesNew[i].radius, -eachSpacing, -seriesNew[i].radius, -eachSpacing)
                        grd.addColorStop(0, hexToRgb(seriesNew[i].color!!, funnelOption.fillOpacity))
                        grd.addColorStop(0.5, hexToRgb(funnelOption.customColor!![seriesNew[i].linearIndex!!], funnelOption.fillOpacity))
                        grd.addColorStop(1, hexToRgb(seriesNew[i].color!!, funnelOption.fillOpacity))
                        fillColor = grd
                    }
                    context.fillStyle = fillColor
                    context.moveTo(0, 0)
                    context.lineTo(-seriesNew[i + 1].radius, 0)
                    context.lineTo(-seriesNew[i].radius, -eachSpacing)
                    context.lineTo(seriesNew[i].radius, -eachSpacing)
                    context.lineTo(seriesNew[i + 1].radius, 0)
                    context.lineTo(0, 0)
                    context.closePath()
                    context.fill()
                    if (funnelOption.border == true) {
                        context.stroke()
                    }
                }
                context.translate(0, eachSpacing)
                i++
            }
        }
    }
    context.restore()
    val offsetCenter = Offset()
    offsetCenter.x = centerPosition.x + xOffset
    offsetCenter.y = centerPosition.y + yOffset
    if (opts.dataLabel != false && process == 1) {
        drawFunnelText(seriesNew, opts, context, eachSpacing, funnelOption.labelAlign, activeWidth, offsetCenter)
    }
    if (process == 1) {
        drawFunnelCenterText(seriesNew, opts, context, eachSpacing, funnelOption.labelAlign, activeWidth, offsetCenter)
    }
    val tooltip = ChartOptionsChartDataToolTiPData()
    tooltip.center = centerPosition
    tooltip.radius = radius
    tooltip.series = seriesNew
    return tooltip
}
fun drawArcbarDataPoints(reassignedSeries: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartOptionsChartDataToolTiPData {
    var series = reassignedSeries
    var arcbarOption = opts.extra.arcbar!!
    series = getArcbarDataPoints(series, arcbarOption, process)
    var centerPosition: Offset = Offset()
    if (arcbarOption.centerX != null || arcbarOption.centerY != null) {
        centerPosition.x = (arcbarOption.centerX ?: opts.width) / 2
        centerPosition.y = (arcbarOption.centerY ?: opts.height) / 2
    } else {
        centerPosition.x = opts.width / 2
        centerPosition.y = opts.height / 2
    }
    var radius: Number = 0
    if (arcbarOption.radius != null) {
        radius = arcbarOption.radius!!
    } else {
        radius = Math.min(centerPosition.x, centerPosition.y)
        radius -= 5 * opts.pixelRatio
        radius -= arcbarOption.width / 2
    }
    radius = if (radius < 10) {
        10
    } else {
        radius
    }
    arcbarOption.customColor = fillCustomColor(arcbarOption.linearType, arcbarOption.customColor, series, config)
    var adjustedY = centerPosition.y + opts.yOffset
    var adjustedX = centerPosition.x + opts.xOffset
    run {
        var i: Number = 0
        while(i < series.length){
            var eachSeries = series[i]
            context.lineWidth = arcbarOption.width * opts.pixelRatio
            context.strokeStyle = arcbarOption.backgroundColor
            context.lineCap = arcbarOption.lineCap
            context.beginPath()
            if (arcbarOption.type == "default") {
                context.arc(adjustedX, adjustedY, radius - (arcbarOption.width * opts.pixelRatio + arcbarOption.gap * opts.pixelRatio) * i, arcbarOption.startAngle * Math.PI, arcbarOption.endAngle * Math.PI, arcbarOption.direction == "ccw")
            } else {
                context.arc(adjustedX, adjustedY, radius - (arcbarOption.width * opts.pixelRatio + arcbarOption.gap * opts.pixelRatio) * i, 0, 2 * Math.PI, arcbarOption.direction == "ccw")
            }
            context.stroke()
            var fillColor: Any = eachSeries.color!!
            if (arcbarOption.linearType == "custom") {
                var grd = context.createLinearGradient(adjustedX - radius, adjustedY, adjustedX + radius, adjustedY)
                grd.addColorStop(0, hexToRgb(eachSeries.color!!, 1))
                grd.addColorStop(1, hexToRgb(arcbarOption.customColor!![eachSeries.linearIndex!!], 1))
                fillColor = grd
            }
            context.lineWidth = arcbarOption.width * opts.pixelRatio
            context.strokeStyle = fillColor
            context.lineCap = arcbarOption.lineCap
            context.beginPath()
            context.arc(adjustedX, adjustedY, radius - (arcbarOption.width * opts.pixelRatio + arcbarOption.gap * opts.pixelRatio) * i, arcbarOption.startAngle * Math.PI, eachSeries._proportion_ * Math.PI, arcbarOption.direction == "ccw")
            context.stroke()
            i++
        }
    }
    drawRingTitle(opts, config, context, centerPosition)
    val tooltip = ChartOptionsChartDataToolTiPData()
    tooltip.center = centerPosition
    tooltip.radius = radius
    tooltip.series = series
    return tooltip
}
fun collisionNew(area: UTSArray<Number>, points: UTSArray<ChartOptionsSeries>, width: Number, height: Number): Boolean {
    var isIn = false
    run {
        var i: Number = 0
        while(i < points.length){
            if (points[i].area != null) {
                if (area[3] < points[i].area!![1] || area[0] > points[i].area!![2] || area[1] > points[i].area!![3] || area[2] < points[i].area!![0]) {
                    if (area[0] < 0 || area[1] < 0 || area[2] > width || area[3] > height) {
                        isIn = true
                        break
                    } else {
                        isIn = false
                    }
                } else {
                    isIn = true
                    break
                }
            }
            i++
        }
    }
    return isIn
}
fun normalInt(min: Number, max: Number, itere: Number): Number {
    var iter = if (itere == 0) {
        1
    } else {
        itere
    }
    var arr: UTSArray<Number> = _uA()
    run {
        var i: Number = 0
        while(i < iter){
            arr.push(Math.random())
            i++
        }
    }
    var sum = arr.reduce(fun(i: Number, j: Number): Number {
        return i + j
    }
    , 0)
    return Math.floor(sum / iter * (max - min)) + min
}
fun getWordCloudPoint(opts: ChartOptions, type: String, context: CanvasRenderingContext2D): UTSArray<ChartOptionsSeries> {
    var points = opts.series!!
    when (type) {
        "normal" -> 
            run {
                var i: Number = 0
                while(i < points.length){
                    var text = points[i].name!!
                    var tHeight = points[i].textSize!! * opts.pixelRatio
                    var tWidth = measureText(text, tHeight)
                    var x: Number = 0
                    var y: Number = 0
                    var area: UTSArray<Number> = _uA()
                    var breaknum: Number = 0
                    while(true){
                        breaknum++
                        x = normalInt(-opts.width / 2, opts.width / 2, 5) - tWidth / 2
                        y = normalInt(-opts.height / 2, opts.height / 2, 5) + tHeight / 2
                        area = _uA(
                            x - 5 + opts.width / 2,
                            y - 5 - tHeight + opts.height / 2,
                            x + tWidth + 5 + opts.width / 2,
                            y + 5 + opts.height / 2
                        )
                        var isCollision = collisionNew(area, points, opts.width, opts.height)
                        if (!isCollision) {
                            break
                        }
                        if (breaknum == 1000) {
                            area = _uA(
                                -100,
                                -100,
                                -100,
                                -100
                            )
                            break
                        }
                    }
                    points[i].area = area
                    i++
                }
            }
        "vertical" -> 
            {
                fun Spin(): Boolean {
                    if (Math.random() > 0.7) {
                        return true
                    } else {
                        return false
                    }
                }
                run {
                    var i: Number = 0
                    while(i < points.length){
                        var text = points[i].name!!
                        var tHeight = points[i].textSize!! * opts.pixelRatio
                        var tWidth = measureText(text, tHeight)
                        var isSpin = Spin()
                        var x: Number = 0
                        var y: Number = 0
                        var area: UTSArray<Number> = _uA()
                        var areav: UTSArray<Number> = _uA()
                        var breaknum: Number = 0
                        while(true){
                            breaknum++
                            var isCollision: Boolean
                            if (isSpin) {
                                x = normalInt(-opts.width / 2, opts.width / 2, 5) - tWidth / 2
                                y = normalInt(-opts.height / 2, opts.height / 2, 5) + tHeight / 2
                                area = _uA(
                                    y - 5 - tWidth + opts.width / 2,
                                    (-x - 5 + opts.height / 2),
                                    y + 5 + opts.width / 2,
                                    (-x + tHeight + 5 + opts.height / 2)
                                )
                                areav = _uA(
                                    opts.width - (opts.width / 2 - opts.height / 2) - (-x + tHeight + 5 + opts.height / 2) - 5,
                                    (opts.height / 2 - opts.width / 2) + (y - 5 - tWidth + opts.width / 2) - 5,
                                    opts.width - (opts.width / 2 - opts.height / 2) - (-x + tHeight + 5 + opts.height / 2) + tHeight,
                                    (opts.height / 2 - opts.width / 2) + (y - 5 - tWidth + opts.width / 2) + tWidth + 5
                                )
                                isCollision = collisionNew(areav, points, opts.height, opts.width)
                            } else {
                                x = normalInt(-opts.width / 2, opts.width / 2, 5) - tWidth / 2
                                y = normalInt(-opts.height / 2, opts.height / 2, 5) + tHeight / 2
                                area = _uA(
                                    x - 5 + opts.width / 2,
                                    y - 5 - tHeight + opts.height / 2,
                                    x + tWidth + 5 + opts.width / 2,
                                    y + 5 + opts.height / 2
                                )
                                isCollision = collisionNew(area, points, opts.width, opts.height)
                            }
                            if (!isCollision) {
                                break
                            }
                            if (breaknum == 1000) {
                                area = _uA(
                                    -1000,
                                    -1000,
                                    -1000,
                                    -1000
                                )
                                break
                            }
                        }
                        if (isSpin) {
                            points[i].area = areav
                            points[i].areav = area
                        } else {
                            points[i].area = area
                        }
                        points[i].rotate = isSpin
                        i++
                    }
                }
            }
    }
    return points
}
fun drawWordCloudDataPoints(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1) {
    var wordOption = opts.extra.word!!
    if (opts.chartData.wordCloudData == null) {
        opts.chartData.wordCloudData = getWordCloudPoint(opts, wordOption.type, context)
    }
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    context.beginPath()
    context.fillStyle = opts.background
    context.rect(xOffset, yOffset, opts.width, opts.height)
    context.fill()
    context.save()
    var points = opts.chartData.wordCloudData!!
    context.translate(xOffset + opts.width / 2, yOffset + opts.height / 2)
    run {
        var i: Number = 0
        while(i < points.length){
            context.save()
            if (points[i].rotate) {
                context.rotate(90 * Math.PI / 180)
            }
            var text = points[i].name!!
            var tHeight = points[i].textSize!! * opts.pixelRatio
            var tWidth = measureText(text, tHeight)
            context.beginPath()
            context.strokeStyle = points[i].color!!
            context.fillStyle = points[i].color!!
            context.font = "" + tHeight + "px sans-serif"
            if (points[i].rotate) {
                if (points[i].areav[0] > 0) {
                    if (opts.tooltip != null) {
                        if ((opts.tooltip!!.index as Number) == i) {
                            context.strokeText(text, (points[i].areav[0] + 5 - opts.width / 2) * process - tWidth * (1 - process) / 2, (points[i].areav[1] + 5 + tHeight - opts.height / 2) * process)
                        } else {
                            context.fillText(text, (points[i].areav[0] + 5 - opts.width / 2) * process - tWidth * (1 - process) / 2, (points[i].areav[1] + 5 + tHeight - opts.height / 2) * process)
                        }
                    } else {
                        context.fillText(text, (points[i].areav[0] + 5 - opts.width / 2) * process - tWidth * (1 - process) / 2, (points[i].areav[1] + 5 + tHeight - opts.height / 2) * process)
                    }
                }
            } else {
                if (points[i].area!![0] > 0) {
                    if (opts.tooltip != null) {
                        if ((opts.tooltip!!.index as Number) == i) {
                            context.strokeText(text, (points[i].area!![0] + 5 - opts.width / 2) * process - tWidth * (1 - process) / 2, (points[i].area!![1] + 5 + tHeight - opts.height / 2) * process)
                        } else {
                            context.fillText(text, (points[i].area!![0] + 5 - opts.width / 2) * process - tWidth * (1 - process) / 2, (points[i].area!![1] + 5 + tHeight - opts.height / 2) * process)
                        }
                    } else {
                        context.fillText(text, (points[i].area!![0] + 5 - opts.width / 2) * process - tWidth * (1 - process) / 2, (points[i].area!![1] + 5 + tHeight - opts.height / 2) * process)
                    }
                }
            }
            context.stroke()
            context.restore()
            i++
        }
    }
    context.restore()
}
fun findWordChartCurrentIndex(currentPoints: TouchPosition, wordData: UTSArray<ChartOptionsSeries>): ChartOptionsTooltip {
    val tooltip = ChartOptionsTooltip()
    var currentIndex: Number = -1
    run {
        var i: Number = 0
        var len = wordData.length
        while(i < len){
            var item = wordData[i]
            if (currentPoints.x > item.area!![0] && currentPoints.x < item.area!![2] && currentPoints.y > item.area!![1] && currentPoints.y < item.area!![3]) {
                currentIndex = i
                break
            }
            i++
        }
    }
    tooltip.index = currentIndex
    return tooltip
}
fun drawGaugeLabel(gaugeOption: ChartOptionsExtraGauge, eradius: Number, centerPosition: Offset, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D) {
    var radius = eradius
    radius -= gaugeOption.width / 2 + gaugeOption.labelOffset * opts.pixelRatio
    radius = if (radius < 10) {
        10
    } else {
        radius
    }
    var totalAngle: Number = 0
    if (gaugeOption.endAngle < gaugeOption.startAngle) {
        totalAngle = 2 + gaugeOption.endAngle - gaugeOption.startAngle
    } else {
        totalAngle = gaugeOption.startAngle - gaugeOption.endAngle
    }
    var splitAngle = totalAngle / gaugeOption.splitLine!!.splitNumber
    var totalNumber = gaugeOption.endNumber - gaugeOption.startNumber!!
    var splitNumber = totalNumber / gaugeOption.splitLine!!.splitNumber
    var nowAngle = gaugeOption.startAngle
    var nowNumber = gaugeOption.startNumber!!
    run {
        var i: Number = 0
        while(i < gaugeOption.splitLine!!.splitNumber + 1){
            val pos = Offset()
            pos.x = radius * Math.cos(nowAngle * Math.PI)
            pos.y = radius * Math.sin(nowAngle * Math.PI)
            var labelText: String = ""
            if (gaugeOption.formatter != null) {
                val kFun = gaugeOption.formatter as (nowNumber: Number, i: Number, opts: ChartOptions) -> String
                labelText = kFun(nowNumber, i, opts)
            } else {
                labelText = "" + nowNumber
            }
            pos.x += centerPosition.x - measureText(labelText, config.fontSize) / 2
            pos.y += centerPosition.y
            var startX = pos.x
            var startY = pos.y
            context.beginPath()
            context.font = "" + config.fontSize + "px sans-serif"
            context.fillStyle = (gaugeOption.labelColor ?: opts.fontColor)
            context.fillText(labelText, startX + opts.xOffset, (startY + config.fontSize / 2) + opts.yOffset)
            context.closePath()
            context.stroke()
            nowAngle += splitAngle
            if (nowAngle >= 2) {
                nowAngle = nowAngle % 2
            }
            nowNumber += splitNumber
            i++
        }
    }
}
fun getGaugeDataPoints(series: UTSArray<ChartOptionsSeries>, categories: UTSArray<ChartOptionsSeriesData>, gaugeOption: ChartOptionsExtraGauge, process: Number = 1): UTSArray<ChartOptionsSeries> {
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            var kVal = item.data!![0].value ?: 0
            if (gaugeOption.pointer!!.color == "auto") {
                run {
                    var i: Number = 0
                    while(i < categories.length){
                        if (kVal <= categories[i].value!!) {
                            item.color = categories[i].color
                            break
                        }
                        i++
                    }
                }
            } else {
                item.color = gaugeOption.pointer!!.color
            }
            var totalAngle: Number = 0
            if (gaugeOption.endAngle < gaugeOption.startAngle) {
                totalAngle = 2 + gaugeOption.endAngle - gaugeOption.startAngle
            } else {
                totalAngle = gaugeOption.startAngle - gaugeOption.endAngle
            }
            item._endAngle_ = totalAngle * kVal + gaugeOption.startAngle
            item._oldAngle_ = gaugeOption.oldAngle!!
            if (gaugeOption.oldAngle!! < gaugeOption.endAngle) {
                item._oldAngle_ += 2
            }
            if (kVal >= gaugeOption.oldData!!) {
                item._proportion_ = (item._endAngle_ - item._oldAngle_) * process + gaugeOption.oldAngle!!
            } else {
                item._proportion_ = item._oldAngle_ - (item._oldAngle_ - item._endAngle_) * process
            }
            if (item._proportion_ >= 2) {
                item._proportion_ = item._proportion_ % 2
            }
            i++
        }
    }
    return series
}
fun getGaugeArcbarDataPoints(series: UTSArray<ChartOptionsSeries>, arcbarOption: ChartOptionsExtraGauge, eprocess: Number = 1): UTSArray<ChartOptionsSeries> {
    var process = eprocess
    if (process == 1) {
        process = 0.999999
    }
    run {
        var i: Number = 0
        while(i < series.length){
            var item = series[i]
            var totalAngle: Number = 0
            if (arcbarOption.type == "circle") {
                totalAngle = 2
            } else {
                if (arcbarOption.endAngle < arcbarOption.startAngle) {
                    totalAngle = 2 + arcbarOption.endAngle - arcbarOption.startAngle
                } else {
                    totalAngle = arcbarOption.startAngle - arcbarOption.endAngle
                }
            }
            item._proportion_ = totalAngle * item.data!![0].value!! * process + arcbarOption.startAngle
            if (item._proportion_ >= 2) {
                item._proportion_ = item._proportion_ % 2
            }
            i++
        }
    }
    return series
}
fun getGaugeAxisPoints(categories: UTSArray<ChartOptionsSeriesData>, startAngle: Number, endAngle: Number): UTSArray<ChartOptionsSeriesData> {
    var totalAngle: Number = 0
    if (endAngle < startAngle) {
        totalAngle = 2 + endAngle - startAngle
    } else {
        totalAngle = startAngle - endAngle
    }
    var tempStartAngle = startAngle
    run {
        var i: Number = 0
        while(i < categories.length){
            categories[i].value = if (categories[i].value == null) {
                0
            } else {
                categories[i].value
            }
            categories[i]._startAngle_ = tempStartAngle
            categories[i]._endAngle_ = totalAngle * categories[i].value!! + startAngle
            if (categories[i]._endAngle_ >= 2) {
                categories[i]._endAngle_ = categories[i]._endAngle_ % 2
            }
            tempStartAngle = categories[i]._endAngle_
            i++
        }
    }
    return categories
}
fun drawGaugeDataPoints(reassignedCategories: UTSArray<ChartOptionsSeriesData>, reassignedSeries: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartOptionsChartDataToolTiPData {
    var categories = reassignedCategories
    var series = reassignedSeries
    var gaugeOption: ChartOptionsExtraGauge = opts.extra.gauge!!
    if (gaugeOption.oldAngle == null) {
        gaugeOption.oldAngle = gaugeOption.startAngle
    }
    if (gaugeOption.oldData == null) {
        gaugeOption.oldData = 0
    }
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    categories = getGaugeAxisPoints(categories, gaugeOption.startAngle, gaugeOption.endAngle)
    var centerPosition = Offset()
    centerPosition.x = opts.width / 2
    centerPosition.y = opts.height / 2
    var radius = Math.min(centerPosition.x, centerPosition.y)
    radius -= 5 * opts.pixelRatio
    radius -= gaugeOption.width / 2
    radius = if (radius < 10) {
        10
    } else {
        radius
    }
    var innerRadius = radius - gaugeOption.width
    var totalAngle: Number = 0
    if (gaugeOption.type == "progress") {
        var pieRadius = radius - gaugeOption.width * 3
        context.beginPath()
        var gradient = context.createLinearGradient(centerPosition.x + xOffset, yOffset + centerPosition.y - pieRadius, centerPosition.x + xOffset, yOffset + centerPosition.y + pieRadius)
        gradient.addColorStop(0, hexToRgb(series[0].color!!, 0.3))
        gradient.addColorStop(1, hexToRgb("#FFFFFF", 0.1))
        context.fillStyle = gradient
        context.arc(centerPosition.x + xOffset, centerPosition.y + yOffset, pieRadius, 0, 2 * Math.PI, false)
        context.fill()
        context.lineWidth = gaugeOption.width
        context.strokeStyle = hexToRgb(series[0].color!!, 0.3)
        context.lineCap = "round"
        context.beginPath()
        context.arc(centerPosition.x + xOffset, centerPosition.y + yOffset, innerRadius, gaugeOption.startAngle * Math.PI, gaugeOption.endAngle * Math.PI, false)
        context.stroke()
        if (gaugeOption.endAngle < gaugeOption.startAngle) {
            totalAngle = 2 + gaugeOption.endAngle - gaugeOption.startAngle
        } else {
            totalAngle = gaugeOption.startAngle - gaugeOption.endAngle
        }
        var splitAngle = totalAngle / gaugeOption.splitLine!!.splitNumber
        var childAngle = totalAngle / gaugeOption.splitLine!!.splitNumber / gaugeOption.splitLine!!.childNumber
        val fixRadius = gaugeOption.splitLine!!.fixRadius!!
        var startX = -radius - gaugeOption.width * 0.5 - fixRadius
        var endX = -radius - gaugeOption.width - fixRadius + gaugeOption.splitLine!!.width
        context.save()
        context.translate(centerPosition.x + xOffset, centerPosition.y + yOffset)
        context.rotate((gaugeOption.startAngle - 1) * Math.PI)
        var len = gaugeOption.splitLine!!.splitNumber * gaugeOption.splitLine!!.childNumber + 1
        var proc = series[0].data!![0].value!! * process
        run {
            var i: Number = 0
            while(i < len){
                context.beginPath()
                if (proc > (i / len)) {
                    context.strokeStyle = hexToRgb(series[0].color!!, 1)
                } else {
                    context.strokeStyle = hexToRgb(series[0].color!!, 0.3)
                }
                context.lineWidth = 3 * opts.pixelRatio
                context.moveTo(startX, 0)
                context.lineTo(endX, 0)
                context.stroke()
                context.rotate(childAngle * Math.PI)
                i++
            }
        }
        context.restore()
        series = getGaugeArcbarDataPoints(series, gaugeOption, process)
        context.lineWidth = gaugeOption.width
        context.strokeStyle = series[0].color!!
        context.lineCap = "round"
        context.beginPath()
        context.arc(centerPosition.x + xOffset, centerPosition.y + yOffset, innerRadius, gaugeOption.startAngle * Math.PI, series[0]._proportion_ * Math.PI, false)
        context.stroke()
        var pointerRadius = radius - gaugeOption.width * 2.5
        context.save()
        context.translate(centerPosition.x + xOffset, centerPosition.y + yOffset)
        context.rotate((series[0]._proportion_ - 1) * Math.PI)
        context.beginPath()
        context.lineWidth = gaugeOption.width / 3
        var gradient3 = context.createLinearGradient(0, -pointerRadius * 0.6, 0, pointerRadius * 0.6)
        gradient3.addColorStop(0, hexToRgb("#FFFFFF", 0))
        gradient3.addColorStop(0.5, hexToRgb(series[0].color!!, 1))
        gradient3.addColorStop(1, hexToRgb("#FFFFFF", 0))
        context.strokeStyle = gradient3
        context.arc(0, 0, pointerRadius, 0.85 * Math.PI, 1.15 * Math.PI, false)
        context.stroke()
        context.beginPath()
        context.lineWidth = 1
        context.strokeStyle = series[0].color!!
        context.fillStyle = series[0].color!!
        val pgw: Number = -pointerRadius - gaugeOption.width / 3 / 2
        context.moveTo(pgw, -4)
        context.lineTo(pgw - 4, 0)
        context.lineTo(pgw, 4)
        context.lineTo(pgw, -4)
        context.stroke()
        context.fill()
        context.restore()
    } else {
        context.lineWidth = gaugeOption.width
        context.lineCap = "butt"
        run {
            var i: Number = 0
            while(i < categories.length){
                var eachCategories = categories[i]
                context.beginPath()
                context.strokeStyle = eachCategories.color!!
                context.arc(centerPosition.x + xOffset, centerPosition.y + yOffset, radius, eachCategories._startAngle_ * Math.PI, eachCategories._endAngle_ * Math.PI, false)
                context.stroke()
                i++
            }
        }
        if (gaugeOption.endAngle < gaugeOption.startAngle) {
            totalAngle = 2 + gaugeOption.endAngle - gaugeOption.startAngle
        } else {
            totalAngle = gaugeOption.startAngle - gaugeOption.endAngle
        }
        var splitAngle = totalAngle / gaugeOption.splitLine!!.splitNumber
        var childAngle = totalAngle / gaugeOption.splitLine!!.splitNumber / gaugeOption.splitLine!!.childNumber
        val fixRadius = gaugeOption.splitLine!!.fixRadius!!
        var startX = -radius - gaugeOption.width * 0.5 - fixRadius
        var endX = -radius - gaugeOption.width * 0.5 - fixRadius + gaugeOption.splitLine!!.width
        var childendX = -radius - gaugeOption.width * 0.5 - fixRadius + gaugeOption.splitLine!!.childWidth
        context.save()
        context.translate(centerPosition.x + xOffset, centerPosition.y + yOffset)
        context.rotate((gaugeOption.startAngle - 1) * Math.PI)
        run {
            var i: Number = 0
            while(i < gaugeOption.splitLine!!.splitNumber + 1){
                context.beginPath()
                context.strokeStyle = gaugeOption.splitLine!!.color
                context.lineWidth = 2 * opts.pixelRatio
                context.moveTo(startX, 0)
                context.lineTo(endX, 0)
                context.stroke()
                context.rotate(splitAngle * Math.PI)
                i++
            }
        }
        context.restore()
        context.save()
        context.translate(centerPosition.x + xOffset, centerPosition.y + yOffset)
        context.rotate((gaugeOption.startAngle - 1) * Math.PI)
        run {
            var i: Number = 0
            while(i < gaugeOption.splitLine!!.splitNumber * gaugeOption.splitLine!!.childNumber + 1){
                context.beginPath()
                context.strokeStyle = gaugeOption.splitLine!!.color
                context.lineWidth = 1 * opts.pixelRatio
                context.moveTo(startX, 0)
                context.lineTo(childendX, 0)
                context.stroke()
                context.rotate(childAngle * Math.PI)
                i++
            }
        }
        context.restore()
        series = getGaugeDataPoints(series, categories, gaugeOption, process)
        run {
            var i: Number = 0
            while(i < series.length){
                var eachSeries = series[i]
                context.save()
                context.translate(centerPosition.x + xOffset, centerPosition.y + yOffset)
                context.rotate((eachSeries._proportion_ - 1) * Math.PI)
                context.beginPath()
                context.fillStyle = eachSeries.color!!
                context.moveTo(gaugeOption.pointer!!.width, 0)
                context.lineTo(0, -gaugeOption.pointer!!.width / 2)
                context.lineTo(-innerRadius, 0)
                context.lineTo(0, gaugeOption.pointer!!.width / 2)
                context.lineTo(gaugeOption.pointer!!.width, 0)
                context.closePath()
                context.fill()
                context.beginPath()
                context.fillStyle = "#FFFFFF"
                context.arc(0, 0, gaugeOption.pointer!!.width / 6, 0, 2 * Math.PI, false)
                context.fill()
                context.restore()
                i++
            }
        }
        if (opts.dataLabel != false) {
            drawGaugeLabel(gaugeOption, radius, centerPosition, opts, config, context)
        }
    }
    drawRingTitle(opts, config, context, centerPosition)
    if (process == 1 && opts.type == "gauge") {
        opts.extra.gauge!!.oldAngle = series[0]._proportion_
        opts.extra.gauge!!.oldData = series[0].data!![0].value!!
    }
    val tooltip = ChartOptionsChartDataToolTiPData()
    tooltip.center = centerPosition
    tooltip.radius = radius
    tooltip.innerRadius = innerRadius
    tooltip.categories = categories
    tooltip.totalAngle = totalAngle
    return tooltip
}
fun drawScatterDataPoints(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartColumnDataPoints {
    var scatterOption = opts.extra.scatter!!
    var xAxisData = opts.chartData.xAxisData
    var xAxisPoints = xAxisData.xAxisPoints
    var eachSpacing = xAxisData.eachSpacing!!
    var calPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = _uA()
    context.save()
    var leftSpace: Number = 0
    var rightSpace = opts.width + eachSpacing
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    if (opts._scrollDistance_ != null && opts._scrollDistance_ != 0 && opts.enableScroll == true) {
        context.translate(opts._scrollDistance_!!, 0)
        leftSpace = -opts._scrollDistance_!! - eachSpacing * 2 + opts.area[3]
        rightSpace = leftSpace + (opts.xAxis.itemCount + 4) * eachSpacing
    }
    series.forEach(fun(eachSeries: ChartOptionsSeries, seriesIndex: Number){
        var ranges = opts.chartData.yAxisData.ranges!![eachSeries.index!!].slice()
        var minRange = parseFloat(ranges.pop()!!)
        var maxRange = parseFloat(ranges.shift()!!)
        var data = eachSeries.dataArr
        var points = getArrDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
        context.beginPath()
        context.strokeStyle = eachSeries.color!!
        context.fillStyle = eachSeries.color!!
        context.lineWidth = 1 * opts.pixelRatio
        var shape = eachSeries.pointShape
        if (shape == "diamond") {
            points.forEach(fun(item) {
                if (item != null) {
                    context.moveTo(xOffset + item.x, item.y - 4.5 + yOffset)
                    context.lineTo(xOffset + item.x - 4.5, item.y + yOffset)
                    context.lineTo(xOffset + item.x, item.y + 4.5 + yOffset)
                    context.lineTo(xOffset + item.x + 4.5, item.y + yOffset)
                    context.lineTo(xOffset + item.x, item.y - 4.5 + yOffset)
                }
            })
        } else if (shape == "circle") {
            points.forEach(fun(item) {
                if (item != null) {
                    context.moveTo(xOffset + item.x + 2.5 * opts.pixelRatio, item.y + yOffset)
                    context.arc(xOffset + item.x, yOffset + item.y, 3 * opts.pixelRatio, 0, 2 * Math.PI, false)
                }
            })
        } else if (shape == "square") {
            points.forEach(fun(item) {
                if (item != null) {
                    context.moveTo(xOffset + item.x - 3.5, yOffset + item.y - 3.5)
                    context.rect(xOffset + item.x - 3.5, yOffset + item.y - 3.5, 7, 7)
                }
            })
        } else if (shape == "triangle") {
            points.forEach(fun(item) {
                if (item != null) {
                    context.moveTo(xOffset + item.x, item.y - 4.5 + yOffset)
                    context.lineTo(xOffset + item.x - 4.5, item.y + 4.5 + yOffset)
                    context.lineTo(xOffset + item.x + 4.5, item.y + 4.5 + yOffset)
                    context.lineTo(xOffset + item.x, item.y - 4.5 + yOffset)
                }
            }
            )
        }
        context.closePath()
        context.fill()
        context.stroke()
    }
    )
    if (opts.dataLabel != false && process == 1) {
        series.forEach(fun(eachSeries, seriesIndex) {
            var ranges = opts.chartData.yAxisData.ranges!![eachSeries.index!!].slice()
            var minRange = parseFloat(ranges.pop()!!)
            var maxRange = parseFloat(ranges.shift()!!)
            var data = eachSeries.dataArr
            var points = getArrDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
            drawArrPointText(points, eachSeries, config, context, opts)
        }
        )
    }
    context.restore()
    return ChartColumnDataPoints(xAxisPoints = xAxisPoints, yAxisPoints = _uA(), calPoints = calPoints, eachSpacing = eachSpacing)
}
fun drawBubbleDataPoints(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartColumnDataPoints {
    var bubbleOption = opts.extra.bubble!!
    var xAxisData = opts.chartData.xAxisData
    var xAxisPoints = xAxisData.xAxisPoints
    var eachSpacing = xAxisData.eachSpacing!!
    var calPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = _uA()
    context.save()
    var leftSpace: Number = 0
    var rightSpace = opts.width + eachSpacing
    if (opts._scrollDistance_ != null && opts._scrollDistance_ != 0 && opts.enableScroll == true) {
        context.translate(opts._scrollDistance_!!, 0)
        leftSpace = -opts._scrollDistance_!! - eachSpacing * 2 + opts.area[3]
        rightSpace = leftSpace + (opts.xAxis.itemCount + 4) * eachSpacing
    }
    series.forEach(fun(eachSeries: ChartOptionsSeries) {
        var ranges = opts.chartData.yAxisData.ranges!![eachSeries.index!!].slice()
        var minRange = parseFloat(ranges.pop()!!)
        var maxRange = parseFloat(ranges.shift()!!)
        var data = eachSeries.dataArr
        var points = getArrDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
        context.beginPath()
        context.strokeStyle = eachSeries.color!!
        context.lineWidth = bubbleOption.border * opts.pixelRatio
        context.fillStyle = hexToRgb(eachSeries.color!!, bubbleOption.opacity)
        points.forEach(fun(item){
            context.moveTo(item.x + item.r, item.y)
            context.arc(item.x, item.y, item.r * opts.pixelRatio, 0, 2 * Math.PI, false)
        }
        )
        context.closePath()
        context.fill()
        context.stroke()
        if (opts.dataLabel != false && process == 1) {
            points.forEach(fun(item, index) {
                context.beginPath()
                var fontSize = (eachSeries.textSize ?: config.fontSize) * opts.pixelRatio
                context.font = "" + fontSize + "px sans-serif"
                context.fillStyle = eachSeries.textColor ?: "#FFFFFF"
                context.textAlign = "center"
                context.fillText("" + item.t, item.x, item.y + fontSize / 2)
                context.closePath()
                context.stroke()
                context.textAlign = "left"
            }
            )
        }
    }
    )
    context.restore()
    return ChartColumnDataPoints(xAxisPoints = xAxisPoints, yAxisPoints = _uA(), calPoints = calPoints, eachSpacing = eachSpacing)
}
fun getCandleToolTipData(series: UTSArray<UTSArray<Number>>, seriesData: UTSArray<ChartOptionsSeries>, opts: ChartOptions, eindex: Any, categories: UTSArray<String>, extra: ChartOptionsExtraCandle, option: ChartOptionsTooltipOption): ChartOptionsTooltipOption {
    var calPoints = opts.chartData.calMapPoints!!
    var upColor = extra.color.upFill
    var downColor = extra.color.downFill
    var index = eindex as Number
    var color = _uA(
        upColor,
        upColor,
        downColor,
        upColor
    ) as UTSArray<String>
    var textList: UTSArray<TextList> = _uA()
    seriesData.forEach(fun(item: ChartOptionsSeries){
        if (index == 0) {
            if (item.dataArr[0][1] - item.dataArr[0][0] < 0) {
                color[1] = downColor
            } else {
                color[1] = upColor
            }
        } else {
            if (item.dataArr[0][0] < series[index - 1][1]) {
                color[0] = downColor
            }
            if (item.dataArr[0][1] < item.dataArr[0][0]) {
                color[1] = downColor
            }
            if (item.dataArr[0][2] > series[index - 1][1]) {
                color[2] = upColor
            }
            if (item.dataArr[0][3] < series[index - 1][1]) {
                color[3] = downColor
            }
        }
        var dataArr = item.dataArr[0]
        val legendShape: String = if (opts.extra.tooltip!!.legendShape == "auto") {
            item.legendShape!!
        } else {
            opts.extra.tooltip!!.legendShape
        }
        val text1 = TextList()
        text1.text = "开盘：" + dataArr[0]
        text1.color = color[0]
        text1.legendShape = legendShape
        val text2 = TextList()
        text2.text = "收盘：" + dataArr[1]
        text2.color = color[1]
        text2.legendShape = legendShape
        val text3 = TextList()
        text3.text = "最低：" + dataArr[2]
        text3.color = color[2]
        text3.legendShape = legendShape
        val text4 = TextList()
        text4.text = "最高：" + dataArr[3]
        text4.color = color[3]
        text4.legendShape = legendShape
        textList.push(text1, text2, text3, text4)
    }
    )
    var validCalPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = _uA()
    var offset = Offset()
    run {
        var i: Number = 0
        while(i < calPoints.length){
            var points = calPoints[i]
            if (points[index] != null) {
                validCalPoints.push(points[index])
            }
            i++
        }
    }
    offset.x = Math.round(validCalPoints[0][0].x)
    val tooltipOption = ChartOptionsTooltipOption()
    tooltipOption.textList = textList
    tooltipOption.offset = offset
    return tooltipOption
}
fun getCandleDataPoints(data: UTSArray<UTSArray<Number>>, minRange: Number, maxRange: Number, xAxisPoints: UTSArray<Number>, eachSpacing: Number, opts: ChartOptions, config: TuiChartConfig, process: Number = 1): UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> {
    var points: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = _uA()
    var validHeight = opts.height - opts.area[0] - opts.area[2]
    data.forEach(fun(item, index) {
        if (item == null) {
        } else {
            var cPoints: UTSArray<ChartOptionsChartDataCalPoints> = _uA()
            item.forEach(fun(items, indexs) {
                var point = ChartOptionsChartDataCalPoints()
                point.x = xAxisPoints[index] + Math.round(eachSpacing / 2)
                var value = items
                var height = validHeight * (value - minRange) / (maxRange - minRange)
                height *= process
                point.y = opts.height - Math.round(height) - opts.area[2]
                cPoints.push(point)
            }
            )
            points.push(cPoints)
        }
    }
    )
    return points
}
fun drawCandleDataPoints(series: UTSArray<ChartOptionsSeries>, seriesMA: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartsCandleDataPoints {
    var candleOption = opts.extra.candle!!
    var xAxisData = opts.chartData.xAxisData
    var xAxisPoints = xAxisData.xAxisPoints
    var eachSpacing = xAxisData.eachSpacing!!
    var calPoints: UTSArray<UTSArray<UTSArray<ChartOptionsChartDataCalPoints>>> = _uA()
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    context.save()
    context.translate(xOffset, yOffset)
    var leftNum: Number = -2
    var rightNum = xAxisPoints.length + 2
    var leftSpace: Number = 0
    var rightSpace = opts.width + eachSpacing
    if (opts._scrollDistance_ != null && opts._scrollDistance_ != 0 && opts.enableScroll == true) {
        context.translate(opts._scrollDistance_!!, 0)
        leftNum = Math.floor(-opts._scrollDistance_!! / eachSpacing) - 2
        rightNum = leftNum + opts.xAxis.itemCount + 4
        leftSpace = -opts._scrollDistance_!! - eachSpacing * 2 + opts.area[3]
        rightSpace = leftSpace + (opts.xAxis.itemCount + 4) * eachSpacing
    }
    if (candleOption.average.show || seriesMA.length > 0) {
        seriesMA.forEach(fun(eachSeries: ChartOptionsSeries){
            var ranges = opts.chartData.yAxisData.ranges!![eachSeries.index!!].slice()
            var minRange = parseFloat(ranges.pop()!!)
            var maxRange = parseFloat(ranges.shift()!!)
            var data = eachSeries.data!!
            var points = getDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
            var splitPointList = splitPoints(points, eachSeries)
            run {
                var i: Number = 0
                while(i < splitPointList.length){
                    var points = splitPointList[i]
                    context.beginPath()
                    context.strokeStyle = eachSeries.color!!
                    context.lineWidth = 1
                    if (points.length == 1) {
                        context.moveTo(points[0].x, points[0].y)
                        context.arc(points[0].x, points[0].y, 1, 0, 2 * Math.PI)
                    } else {
                        context.moveTo(points[0].x, points[0].y)
                        var startPoint: Number = 0
                        run {
                            var j: Number = 0
                            while(j < points.length){
                                var item = points[j]
                                if (startPoint == 0 && item.x > leftSpace) {
                                    context.moveTo(item.x, item.y)
                                    startPoint = 1
                                }
                                if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                    var ctrlPoint = createCurveControlPoints(points, j - 1)
                                    context.bezierCurveTo(ctrlPoint.ctrA.x, ctrlPoint.ctrA.y, ctrlPoint.ctrB.x, ctrlPoint.ctrB.y, item.x, item.y)
                                }
                                j++
                            }
                        }
                        context.moveTo(points[0].x, points[0].y)
                    }
                    context.closePath()
                    context.stroke()
                    i++
                }
            }
        }
        )
    }
    series.forEach(fun(eachSeries, seriesIndex) {
        var ranges = opts.chartData.yAxisData.ranges!![eachSeries.index!!].slice()
        var minRange = parseFloat(ranges.pop()!!)
        var maxRange = parseFloat(ranges.shift()!!)
        var data = eachSeries.dataArr
        var points = getCandleDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
        calPoints.push(points)
        var splitPointList = splitCandlePoints(points, eachSeries)
        run {
            var i: Number = 0
            while(i < splitPointList[0].length){
                if (i > leftNum && i < rightNum) {
                    var item = splitPointList[0][i]
                    context.beginPath()
                    if (data[i][1] - data[i][0] > 0) {
                        context.strokeStyle = candleOption.color.upLine
                        context.fillStyle = candleOption.color.upFill
                        context.lineWidth = 1 * opts.pixelRatio
                        context.moveTo(item[3].x, item[3].y)
                        context.lineTo(item[1].x, item[1].y)
                        context.lineTo(item[1].x - eachSpacing / 4, item[1].y)
                        context.lineTo(item[0].x - eachSpacing / 4, item[0].y)
                        context.lineTo(item[0].x, item[0].y)
                        context.lineTo(item[2].x, item[2].y)
                        context.lineTo(item[0].x, item[0].y)
                        context.lineTo(item[0].x + eachSpacing / 4, item[0].y)
                        context.lineTo(item[1].x + eachSpacing / 4, item[1].y)
                        context.lineTo(item[1].x, item[1].y)
                        context.moveTo(item[3].x, item[3].y)
                    } else {
                        context.strokeStyle = candleOption.color.downLine
                        context.fillStyle = candleOption.color.downFill
                        context.lineWidth = 1 * opts.pixelRatio
                        context.moveTo(item[3].x, item[3].y)
                        context.lineTo(item[0].x, item[0].y)
                        context.lineTo(item[0].x - eachSpacing / 4, item[0].y)
                        context.lineTo(item[1].x - eachSpacing / 4, item[1].y)
                        context.lineTo(item[1].x, item[1].y)
                        context.lineTo(item[2].x, item[2].y)
                        context.lineTo(item[1].x, item[1].y)
                        context.lineTo(item[1].x + eachSpacing / 4, item[1].y)
                        context.lineTo(item[0].x + eachSpacing / 4, item[0].y)
                        context.lineTo(item[0].x, item[0].y)
                        context.moveTo(item[3].x, item[3].y)
                    }
                    context.closePath()
                    context.fill()
                    context.stroke()
                }
                i++
            }
        }
    }
    )
    context.restore()
    return ChartsCandleDataPoints(xAxisPoints = xAxisPoints, yAxisPoints = _uA(), calPoints = calPoints, eachSpacing = eachSpacing)
}
fun isRayIntersectsSegment(poi: UTSArray<Number>, s_poi: UTSArray<Number>, e_poi: UTSArray<Number>): Boolean {
    if (s_poi[1] == e_poi[1]) {
        return false
    }
    if (s_poi[1] > poi[1] && e_poi[1] > poi[1]) {
        return false
    }
    if (s_poi[1] < poi[1] && e_poi[1] < poi[1]) {
        return false
    }
    if (s_poi[1] == poi[1] && e_poi[1] > poi[1]) {
        return false
    }
    if (e_poi[1] == poi[1] && s_poi[1] > poi[1]) {
        return false
    }
    if (s_poi[0] < poi[0] && e_poi[1] < poi[1]) {
        return false
    }
    var xseg = e_poi[0] - (e_poi[0] - s_poi[0]) * (e_poi[1] - poi[1]) / (e_poi[1] - s_poi[1])
    if (xseg < poi[0]) {
        return false
    } else {
        return true
    }
}
fun isPoiWithinPoly(poi: UTSArray<Number>, poly: UTSArray<UTSArray<UTSArray<UTSArray<Number>>>>, mercator: Boolean): Boolean {
    var sinsc: Number = 0
    run {
        var i: Number = 0
        while(i < poly.length){
            var epoly = poly[i][0]
            if (poly.length == 1) {
                epoly = poly[i][0]
            }
            run {
                var j: Number = 0
                while(j < epoly.length - 1){
                    var s_poi = epoly[j]
                    var e_poi = epoly[j + 1]
                    if (mercator) {
                        s_poi = lonlat2mercator(epoly[j][0], epoly[j][1])
                        e_poi = lonlat2mercator(epoly[j + 1][0], epoly[j + 1][1])
                    }
                    if (isRayIntersectsSegment(poi, s_poi, e_poi)) {
                        sinsc += 1
                    }
                    j++
                }
            }
            i++
        }
    }
    if (sinsc % 2 == 1) {
        return true
    } else {
        return false
    }
}
fun pointToCoordinate(pointY: Number, pointX: Number, bounds: ChartMapBounds, scale: Number, xoffset: Number, yoffset: Number): Offset {
    val offset = Offset()
    offset.x = (pointX - xoffset) / scale + bounds.xMin
    offset.y = bounds.yMax - (pointY - yoffset) / scale
    return offset
}
fun findMapChartCurrentIndex(currentPoints: TouchPosition, opts: ChartOptions): ChartOptionsTooltip {
    var tooltip: ChartOptionsTooltip = ChartOptionsTooltip()
    var currentIndex: Number = -1
    var cData = opts.chartData.mapData!!
    var data = opts.series!!
    var tmp = pointToCoordinate(currentPoints.y, currentPoints.x, cData.bounds, cData.scale, cData.xoffset, cData.yoffset)
    var poi = _uA(
        tmp.x,
        tmp.y
    ) as UTSArray<Number>
    run {
        var i: Number = 0
        var len = data.length
        while(i < len){
            var item = data[i].geometry.coordinates
            if (isPoiWithinPoly(poi, item, opts.chartData.mapData!!.mercator)) {
                currentIndex = i
                break
            }
            i++
        }
    }
    tooltip.index = currentIndex
    return tooltip
}
fun coordinateToPoint(latitude: Number, longitude: Number, bounds: ChartMapBounds, scale: Number, xoffset: Number, yoffset: Number): Offset {
    val offset = Offset()
    offset.x = (longitude - bounds.xMin) * scale + xoffset
    offset.y = (bounds.yMax - latitude) * scale + yoffset
    return offset
}
fun lonlat2mercator(longitude: Number, latitude: Number): UTSArray<Number> {
    var mercator: UTSArray<Number> = _uA()
    var x = longitude * 20037508.34 / 180
    var y = Math.log(Math.tan((90 + latitude) * Math.PI / 360)) / (Math.PI / 180)
    y = y * 20037508.34 / 180
    mercator.push(x)
    mercator.push(y)
    return mercator
}
fun getBoundingBox(data: UTSArray<ChartOptionsSeries>): ChartMapBounds {
    var bounds = ChartMapBounds()
    var datacoords: UTSArray<UTSArray<Number>> = _uA()
    run {
        var i: Number = 0
        while(i < data.length){
            var coorda: UTSArray<UTSArray<UTSArray<UTSArray<Number>>>> = data[i].geometry.coordinates
            run {
                var k: Number = 0
                while(k < coorda.length){
                    var coords: UTSArray<UTSArray<UTSArray<Number>>> = coorda[k]
                    if (coords.length == 1) {
                        datacoords = coords[0]
                    }
                    run {
                        var j: Number = 0
                        while(j < datacoords.length){
                            var longitude = datacoords[j][0]
                            var latitude = datacoords[j][1]
                            val point = Offset()
                            point.x = longitude
                            point.y = latitude
                            bounds.xMin = if (bounds.xMin < point.x) {
                                bounds.xMin
                            } else {
                                point.x
                            }
                            bounds.xMax = if (bounds.xMax > point.x) {
                                bounds.xMax
                            } else {
                                point.x
                            }
                            bounds.yMin = if (bounds.yMin < point.y) {
                                bounds.yMin
                            } else {
                                point.y
                            }
                            bounds.yMax = if (bounds.yMax > point.y) {
                                bounds.yMax
                            } else {
                                point.y
                            }
                            j++
                        }
                    }
                    k++
                }
            }
            i++
        }
    }
    return bounds
}
fun drawMapDataPoints(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D) {
    var mapOption = opts.extra.map!!
    var coords: UTSArray<UTSArray<Number>> = _uA()
    var point: Offset = Offset()
    var data: UTSArray<ChartOptionsSeries> = series
    var bounds: ChartMapBounds = getBoundingBox(data)
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    context.save()
    context.translate(xOffset, yOffset)
    if (mapOption.mercator) {
        var max = lonlat2mercator(bounds.xMax, bounds.yMax)
        var min = lonlat2mercator(bounds.xMin, bounds.yMin)
        bounds.xMax = max[0]
        bounds.yMax = max[1]
        bounds.xMin = min[0]
        bounds.yMin = min[1]
    }
    var xScale = opts.width / Math.abs(bounds.xMax - bounds.xMin)
    var yScale = opts.height / Math.abs(bounds.yMax - bounds.yMin)
    var scale = if (xScale < yScale) {
        xScale
    } else {
        yScale
    }
    var xoffset = opts.width / 2 - Math.abs(bounds.xMax - bounds.xMin) / 2 * scale
    var yoffset = opts.height / 2 - Math.abs(bounds.yMax - bounds.yMin) / 2 * scale
    run {
        var i: Number = 0
        while(i < data.length){
            context.beginPath()
            context.lineWidth = mapOption.borderWidth * opts.pixelRatio
            context.strokeStyle = mapOption.borderColor
            context.fillStyle = hexToRgb(series[i].color!!, series[i].fillOpacity)
            if (mapOption.active == true && opts.tooltip != null) {
                if ((opts.tooltip!!.index as Number) == i) {
                    context.strokeStyle = mapOption.activeBorderColor
                    context.fillStyle = hexToRgb(mapOption.activeFillColor, mapOption.activeFillOpacity)
                }
            }
            var coorda: UTSArray<UTSArray<UTSArray<UTSArray<Number>>>> = data[i].geometry.coordinates
            run {
                var k: Number = 0
                while(k < coorda.length){
                    var _coords: UTSArray<UTSArray<UTSArray<Number>>> = coorda[k]
                    if (_coords.length == 1) {
                        coords = _coords[0]
                    }
                    run {
                        var j: Number = 0
                        while(j < coords.length){
                            var gaosi: UTSArray<Number> = _uA()
                            if (mapOption.mercator) {
                                gaosi = lonlat2mercator(coords[j][0], coords[j][1])
                            } else {
                                gaosi = coords[j]
                            }
                            point = coordinateToPoint(gaosi[1], gaosi[0], bounds, scale, xoffset, yoffset)
                            if (j == 0) {
                                context.beginPath()
                                context.moveTo(point.x, point.y)
                            } else {
                                context.lineTo(point.x, point.y)
                            }
                            j++
                        }
                    }
                    context.fill()
                    if (mapOption.border == true) {
                        context.stroke()
                    }
                    k++
                }
            }
            i++
        }
    }
    if (opts.dataLabel == true) {
        run {
            var i: Number = 0
            while(i < data.length){
                var centerPoint = data[i].properties.centroid
                if (centerPoint.length > 0) {
                    if (mapOption.mercator) {
                        centerPoint = lonlat2mercator(data[i].properties.centroid[0], data[i].properties.centroid[1])
                    }
                    point = coordinateToPoint(centerPoint[1], centerPoint[0], bounds, scale, xoffset, yoffset)
                    var fontSize = (data[i].textSize ?: config.fontSize) * opts.pixelRatio
                    var fontColor = data[i].textColor ?: opts.fontColor
                    if (mapOption.active && mapOption.activeTextColor != "" && opts.tooltip != null && (opts.tooltip!!.index as Number) == i) {
                        fontColor = mapOption.activeTextColor
                    }
                    var text = data[i].properties.name
                    context.beginPath()
                    context.font = "" + fontSize + "px sans-serif"
                    context.fillStyle = fontColor
                    context.fillText(text, point.x - measureText(text, fontSize) / 2, point.y + fontSize / 2)
                    context.closePath()
                    context.stroke()
                }
                i++
            }
        }
    }
    val mapdata = ChartOptionsChartDataMapData()
    mapdata.bounds = bounds
    mapdata.scale = scale
    mapdata.xoffset = xoffset
    mapdata.yoffset = yoffset
    mapdata.mercator = mapOption.mercator
    opts.chartData.mapData = mapdata
    drawToolTipBridge(opts, config, context, 1, 0, _uA())
    context.restore()
}
fun getMixToolTipData(seriesData: UTSArray<ChartOptionsSeries>, opts: ChartOptions, eindex: Any, categories: UTSArray<String>, option: ChartOptionsTooltipOption): ChartOptionsTooltipOption {
    val index = eindex as Number
    val points = opts.chartData.xAxisPoints!![index] + opts.chartData.eachSpacing / 2
    var textList = seriesData.map(fun(item: ChartOptionsSeries): TextList {
        val list = TextList()
        if (option.formatter != null) {
            val kFun = option.formatter as (item: ChartOptionsSeries, categories: String, index: Number, opts: ChartOptions) -> String
            list.text = kFun(item, categories[index], index, opts)
        } else {
            list.text = item.name + ": " + item.data!![0].value!!
        }
        list.legendShape = if (opts.extra.tooltip!!.legendShape == "auto") {
            item.legendShape!!
        } else {
            opts.extra.tooltip!!.legendShape
        }
        list.color = item.color!!
        list.disableLegend = if (item.disableLegend) {
            true
        } else {
            false
        }
        return list
    }
    )
    textList = textList.filter(fun(item: TextList): Boolean {
        return item.disableLegend != true
    }
    )
    val offset = Offset()
    offset.x = Math.round(points)
    offset.y = 0
    val tooltipOption = ChartOptionsTooltipOption()
    tooltipOption.textList = textList
    tooltipOption.offset = offset
    return tooltipOption
}
fun drawMixDataPoints(series: UTSArray<ChartOptionsSeries>, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D, process: Number = 1): ChartColumnDataPoints {
    var xAxisData = opts.chartData.xAxisData
    var xAxisPoints = xAxisData.xAxisPoints
    var eachSpacing = xAxisData.eachSpacing!!
    var columnOption = opts.extra.mix?.column ?: ChartOptionsExtraMixColumn()
    var areaOption = opts.extra.mix?.area ?: ChartOptionsExtraMixArea()
    var lineOption = opts.extra.mix?.line ?: ChartOptionsExtraMixLine()
    var endY = opts.height - opts.area[2]
    var calPoints: UTSArray<UTSArray<ChartOptionsChartDataCalPoints>> = _uA()
    var columnIndex: Number = 0
    var columnLength: Number = 0
    val xOffset = opts.xOffset
    val yOffset = opts.yOffset
    series.forEach(fun(eachSeries){
        if (eachSeries.type == "column") {
            columnLength += 1
        }
    }
    )
    context.save()
    var leftNum: Number = -2
    var rightNum = xAxisPoints.length + 2
    var leftSpace: Number = 0
    var rightSpace = opts.width + eachSpacing
    if (opts._scrollDistance_ != null && opts._scrollDistance_ != 0 && opts.enableScroll == true) {
        val scrollDistance = opts._scrollDistance_!!
        context.translate(scrollDistance, 0)
        leftNum = Math.floor(-scrollDistance / eachSpacing) - 2
        rightNum = leftNum + opts.xAxis.itemCount + 4
        leftSpace = -scrollDistance - eachSpacing * 2 + opts.area[3]
        rightSpace = leftSpace + (opts.xAxis.itemCount + 4) * eachSpacing
    }
    columnOption.customColor = fillCustomColor(columnOption.linearType, columnOption.customColor, series, config)
    series.forEach(fun(eachSeries) {
        var ranges = opts.chartData.yAxisData.ranges!![eachSeries.index!!].slice()
        var minRange = parseFloat(ranges.pop()!!)
        var maxRange = parseFloat(ranges.shift()!!)
        var data = eachSeries.data!!
        var points = getDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
        calPoints.push(points)
        if (eachSeries.type == "column") {
            points = fixColumeData(points, eachSpacing, columnLength, columnIndex, config, opts)
            run {
                var i: Number = 0
                while(i < points.length){
                    var item = points[i]
                    if (item != null && i > leftNum && i < rightNum) {
                        var startX = item.x - item.width!! / 2
                        var height = opts.height - item.y - opts.area[2]
                        context.beginPath()
                        var fillColor: Any = item.color ?: eachSeries.color!!
                        var strokeColor = item.color ?: eachSeries.color!!
                        if (columnOption.linearType != "none") {
                            var grd = context.createLinearGradient(startX + xOffset, item.y + yOffset, startX + xOffset, opts.height - opts.area[2] + yOffset)
                            if (columnOption.linearType == "opacity") {
                                grd.addColorStop(0, hexToRgb("" + fillColor, columnOption.linearOpacity))
                                grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                            } else {
                                grd.addColorStop(0, hexToRgb(columnOption.customColor!![eachSeries.linearIndex!!], columnOption.linearOpacity))
                                grd.addColorStop(columnOption.colorStop!!, hexToRgb(columnOption.customColor!![eachSeries.linearIndex!!], columnOption.linearOpacity))
                                grd.addColorStop(1, hexToRgb("" + fillColor, 1))
                            }
                            fillColor = grd
                        }
                        if ((columnOption.barBorderRadius != null && columnOption.barBorderRadius!!.length == 4) || columnOption.barBorderCircle) {
                            val left = startX
                            val top = item.y
                            val width = item.width!!
                            val height = opts.height - opts.area[2] - item.y
                            if (columnOption.barBorderCircle) {
                                columnOption.barBorderRadius = _uA(
                                    width / 2,
                                    width / 2,
                                    0,
                                    0
                                )
                            }
                            var barBorderRadius = columnOption.barBorderRadius!!
                            var r0 = barBorderRadius[0]
                            var r1 = barBorderRadius[1]
                            var r2 = barBorderRadius[2]
                            var r3 = barBorderRadius[3]
                            var minRadius = Math.min(width / 2, height / 2)
                            r0 = if (r0 > minRadius) {
                                minRadius
                            } else {
                                r0
                            }
                            r1 = if (r1 > minRadius) {
                                minRadius
                            } else {
                                r1
                            }
                            r2 = if (r2 > minRadius) {
                                minRadius
                            } else {
                                r2
                            }
                            r3 = if (r3 > minRadius) {
                                minRadius
                            } else {
                                r3
                            }
                            r0 = if (r0 < 0) {
                                0
                            } else {
                                r0
                            }
                            r1 = if (r1 < 0) {
                                0
                            } else {
                                r1
                            }
                            r2 = if (r2 < 0) {
                                0
                            } else {
                                r2
                            }
                            r3 = if (r3 < 0) {
                                0
                            } else {
                                r3
                            }
                            context.arc(xOffset + left + r0, yOffset + top + r0, r0, -Math.PI, -Math.PI / 2)
                            context.arc(xOffset + left + width - r1, yOffset + top + r1, r1, -Math.PI / 2, 0)
                            context.arc(xOffset + left + width - r2, yOffset + top + height - r2, r2, 0, Math.PI / 2)
                            context.arc(xOffset + left + r3, yOffset + top + height - r3, r3, Math.PI / 2, Math.PI)
                        } else {
                            context.moveTo(xOffset + startX, item.y + yOffset)
                            context.lineTo(xOffset + startX + item.width!!, item.y + yOffset)
                            context.lineTo(xOffset + startX + item.width!!, opts.height - opts.area[2] + yOffset)
                            context.lineTo(xOffset + startX, opts.height - opts.area[2] + yOffset)
                            context.lineTo(xOffset + startX, item.y + yOffset)
                            context.lineWidth = 1
                            context.strokeStyle = strokeColor
                        }
                        context.fillStyle = fillColor
                        context.closePath()
                        context.fill()
                    }
                    i++
                }
            }
            columnIndex += 1
        }
        if (eachSeries.type == "area") {
            var splitPointList = splitPoints(points, eachSeries)
            run {
                var i: Number = 0
                while(i < splitPointList.length){
                    var points = splitPointList[i]
                    context.beginPath()
                    context.strokeStyle = eachSeries.color!!
                    context.strokeStyle = hexToRgb(eachSeries.color!!, areaOption.opacity)
                    if (areaOption.gradient) {
                        var gradient = context.createLinearGradient(xOffset, opts.area[0] + yOffset, xOffset, opts.height - opts.area[2] + yOffset)
                        gradient.addColorStop(0, hexToRgb(eachSeries.color!!, areaOption.opacity))
                        gradient.addColorStop(1, hexToRgb("#FFFFFF", 0.1))
                        context.fillStyle = gradient
                    } else {
                        context.fillStyle = hexToRgb(eachSeries.color!!, areaOption.opacity)
                    }
                    context.lineWidth = 2 * opts.pixelRatio
                    if (points.length > 1) {
                        var firstPoint = points[0]
                        var lastPoint = points[points.length - 1]
                        context.moveTo(firstPoint.x + xOffset, firstPoint.y + yOffset)
                        var startPoint: Number = 0
                        if (eachSeries.style == "curve") {
                            run {
                                var j: Number = 0
                                while(j < points.length){
                                    var item = points[j]
                                    if (startPoint == 0 && item.x > leftSpace) {
                                        context.moveTo(item.x + xOffset, item.y + yOffset)
                                        startPoint = 1
                                    }
                                    if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                        var ctrlPoint = createCurveControlPoints(points, j - 1)
                                        context.bezierCurveTo(ctrlPoint.ctrA.x + xOffset, ctrlPoint.ctrA.y + yOffset, ctrlPoint.ctrB.x + xOffset, ctrlPoint.ctrB.y + yOffset, item.x + xOffset, item.y + yOffset)
                                    }
                                    j++
                                }
                            }
                        } else {
                            run {
                                var j: Number = 0
                                while(j < points.length){
                                    var item = points[j]
                                    if (startPoint == 0 && item.x > leftSpace) {
                                        context.moveTo(xOffset + item.x, item.y + yOffset)
                                        startPoint = 1
                                    }
                                    if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                        context.lineTo(xOffset + item.x, item.y + yOffset)
                                    }
                                    j++
                                }
                            }
                        }
                        context.lineTo(xOffset + lastPoint.x, endY + yOffset)
                        context.lineTo(xOffset + firstPoint.x, endY + yOffset)
                        context.lineTo(xOffset + firstPoint.x, firstPoint.y + yOffset)
                    } else {
                        var item = points[0]
                        context.moveTo(xOffset + item.x - eachSpacing / 2, item.y + yOffset)
                    }
                    context.closePath()
                    context.fill()
                    i++
                }
            }
        }
        if (eachSeries.type == "line") {
            var splitPointList = splitPoints(points, eachSeries)
            splitPointList.forEach(fun(points){
                if (eachSeries.lineType == "dash") {
                    var dashLength = eachSeries.dashLength
                    dashLength *= opts.pixelRatio
                    context.setLineDash(_uA(
                        dashLength,
                        dashLength
                    ))
                }
                context.beginPath()
                context.strokeStyle = eachSeries.color!!
                context.lineWidth = lineOption.width * opts.pixelRatio
                if (points.length == 1) {
                    context.moveTo(xOffset + points[0].x, points[0].y + yOffset)
                } else {
                    context.moveTo(xOffset + points[0].x, points[0].y + yOffset)
                    var startPoint: Number = 0
                    if (eachSeries.style == "curve") {
                        run {
                            var j: Number = 0
                            while(j < points.length){
                                var item = points[j]
                                if (startPoint == 0 && item.x > leftSpace) {
                                    context.moveTo(xOffset + item.x, item.y + yOffset)
                                    startPoint = 1
                                }
                                if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                    var ctrlPoint = createCurveControlPoints(points, j - 1)
                                    context.bezierCurveTo(ctrlPoint.ctrA.x + xOffset, ctrlPoint.ctrA.y + yOffset, ctrlPoint.ctrB.x + xOffset, ctrlPoint.ctrB.y + yOffset, item.x + xOffset, item.y + yOffset)
                                }
                                j++
                            }
                        }
                    } else {
                        run {
                            var j: Number = 0
                            while(j < points.length){
                                var item = points[j]
                                if (startPoint == 0 && item.x > leftSpace) {
                                    context.moveTo(xOffset + item.x, item.y + yOffset)
                                    startPoint = 1
                                }
                                if (j > 0 && item.x > leftSpace && item.x < rightSpace) {
                                    context.lineTo(xOffset + item.x, item.y + yOffset)
                                }
                                j++
                            }
                        }
                    }
                    context.moveTo(xOffset + points[0].x, points[0].y + yOffset)
                }
                context.stroke()
                context.setLineDash(_uA())
            }
            )
        }
        if (eachSeries.type == "point") {
            eachSeries.addPoint = true
        }
        if (eachSeries.addPoint == true && eachSeries.type != "column") {
            drawPointShape(points, eachSeries.color!!, eachSeries.pointShape, context, opts)
        }
    }
    )
    if (opts.dataLabel != false && process == 1) {
        var columnIndex: Number = 0
        series.forEach(fun(eachSeries) {
            var ranges = opts.chartData.yAxisData.ranges!![eachSeries.index!!].slice()
            var minRange = parseFloat(ranges.pop()!!)
            var maxRange = parseFloat(ranges.shift()!!)
            var data = eachSeries.data!!
            var points = getDataPoints(data, minRange, maxRange, xAxisPoints, eachSpacing, opts, config, process)
            if (eachSeries.type != "column") {
                drawPointText(points, eachSeries, config, context, opts)
            } else {
                points = fixColumeData(points, eachSpacing, columnLength, columnIndex, config, opts)
                drawPointText(points, eachSeries, config, context, opts)
                columnIndex += 1
            }
        }
        )
    }
    context.restore()
    return ChartColumnDataPoints(xAxisPoints = xAxisPoints, yAxisPoints = _uA(), calPoints = calPoints, eachSpacing = eachSpacing)
}
fun drawCharts(char: TuiChartsInterface, type: String, opts: ChartOptions, config: TuiChartConfig, context: CanvasRenderingContext2D) {
    var _this = char
    var series = opts.series!!
    var categories: UTSArray<String> = opts.categories ?: _uA()
    if (type == "pie" || type == "ring" || type == "mount" || type == "rose" || type == "funnel") {
        categories = _uA()
        run {
            var j: Number = 0
            while(j < series.length){
                categories.push(series[j].name!!)
                j++
            }
        }
        opts.categories = categories
    }
    series = fillSeries(series, opts, config)
    var duration = if (opts.animation) {
        opts.duration
    } else {
        0
    }
    if (_this.animationInstance != null) {
        _this.animationInstance!!.stop()
    }
    var seriesMA: UTSArray<ChartOptionsSeries> = _uA()
    if (type == "candle") {
        var average = opts.extra.candle!!.average
        if (average.show) {
            seriesMA = calCandleMA(average.day!!, average.name!!, average.color!!, series[0].dataArr)
            seriesMA = fillSeries(seriesMA, opts, config)
            opts.seriesMA = seriesMA
        } else if (opts.seriesMA != null) {
            opts.seriesMA = fillSeries(opts.seriesMA!!, opts, config)
            seriesMA = opts.seriesMA!!
        } else {
            seriesMA = series
        }
    } else {
        seriesMA = series
    }
    series = filterSeries(series)
    opts._series_ = series
    opts.area = _uA()
    run {
        var j: Number = 0
        while(j < 4){
            opts.area.push(opts.padding!![j] * opts.pixelRatio)
            j++
        }
    }
    var _calLegendData = calLegendData(seriesMA, opts, opts.chartData)
    var legendHeight = _calLegendData.area.wholeHeight
    var legendWidth = _calLegendData.area.wholeWidth
    when (opts.legend.position) {
        "top" -> 
            opts.area[0] += legendHeight
        "bottom" -> 
            opts.area[2] += legendHeight
        "left" -> 
            opts.area[3] += legendWidth
        "right" -> 
            opts.area[1] += legendWidth
    }
    var _calYAxisData = ChartOptionsChartDataYAxisData()
    var yAxisWidth: UTSArray<ChartOptionsChartDataYAxisDataYAxisWidth> = _uA()
    if (opts.type == "line" || opts.type == "column" || opts.type == "mount" || opts.type == "area" || opts.type == "mix" || opts.type == "candle" || opts.type == "scatter" || opts.type == "bubble" || opts.type == "bar") {
        _calYAxisData = calYAxisData(series, opts, config)
        yAxisWidth = _calYAxisData.yAxisWidth!!
        if (opts.yAxis.showTitle) {
            var maxTitleHeight: Number = 0
            run {
                var i: Number = 0
                while(i < opts.yAxis.data.length){
                    maxTitleHeight = Math.max(maxTitleHeight, if (opts.yAxis.data[i].titleFontSize != null) {
                        opts.yAxis.data[i].titleFontSize!! * opts.pixelRatio
                    } else {
                        config.fontSize
                    }
                    )
                    i++
                }
            }
            opts.area[0] += maxTitleHeight
        }
        var rightIndex: Number = 0
        var leftIndex: Number = 0
        run {
            var i: Number = 0
            while(i < yAxisWidth.length){
                if (yAxisWidth[i].position == "left") {
                    if (leftIndex > 0) {
                        opts.area[3] += yAxisWidth[i].width + opts.yAxis.padding * opts.pixelRatio
                    } else {
                        opts.area[3] += yAxisWidth[i].width
                    }
                    leftIndex += 1
                } else if (yAxisWidth[i].position == "right") {
                    if (rightIndex > 0) {
                        opts.area[1] += yAxisWidth[i].width + opts.yAxis.padding * opts.pixelRatio
                    } else {
                        opts.area[1] += yAxisWidth[i].width
                    }
                    rightIndex += 1
                }
                i++
            }
        }
    } else {
        config.yAxisWidth = yAxisWidth
    }
    opts.chartData.yAxisData = _calYAxisData
    if (opts.categories != null && opts.categories!!.length > 0 && opts.type != "radar" && opts.type != "gauge" && opts.type != "bar") {
        opts.chartData.xAxisData = getXAxisPoints(opts.categories!!, opts, config)
        var _calCategoriesData = calCategoriesData(opts.categories!!, opts, config, opts.chartData.xAxisData.eachSpacing!!, context)
        var xAxisHeight = _calCategoriesData.xAxisHeight
        var angle = _calCategoriesData.angle
        config.xAxisHeight = xAxisHeight
        config._xAxisTextAngle_ = angle
        opts.area[2] += xAxisHeight
        opts.chartData.categoriesData = _calCategoriesData
    } else {
        if (opts.type == "line" || opts.type == "area" || opts.type == "scatter" || opts.type == "bubble" || opts.type == "bar") {
            opts.chartData.xAxisData = calXAxisData(series, opts, config, context)
            categories = opts.chartData.xAxisData.rangesFormat.map(fun(item: Number): String {
                return "" + item
            })
            var _calCategoriesData = calCategoriesData(categories, opts, config, opts.chartData.xAxisData.eachSpacing!!, context)
            var xAxisHeight = _calCategoriesData.xAxisHeight
            var angle = _calCategoriesData.angle
            config.xAxisHeight = xAxisHeight
            config._xAxisTextAngle_ = angle
            opts.area[2] += xAxisHeight
            opts.chartData.categoriesData = _calCategoriesData
        } else {
            opts.chartData.xAxisData = ChartOptionsChartDataXAxisData()
        }
    }
    if (opts.enableScroll && opts.xAxis.scrollAlign == "right" && opts._scrollDistance_ == null) {
        var offsetLeft: Number = 0
        var xAxisPoints = opts.chartData.xAxisData.xAxisPoints
        var startX = opts.chartData.xAxisData.startX!!
        var endX = opts.chartData.xAxisData.endX!!
        var eachSpacing = opts.chartData.xAxisData.eachSpacing!!
        var totalWidth = eachSpacing * (xAxisPoints.length - 1)
        var screenWidth = endX - startX
        offsetLeft = screenWidth - totalWidth
        _this.scrollOption.currentOffset = offsetLeft
        _this.scrollOption.startTouchX = offsetLeft
        _this.scrollOption.distance = 0
        _this.scrollOption.lastMoveTime = 0
        opts._scrollDistance_ = offsetLeft
    }
    if (type == "pie" || type == "ring" || type == "rose") {
        config._pieTextMaxLength_ = if (opts.dataLabel == false) {
            0
        } else {
            getPieTextMaxLength(seriesMA, config, context, opts)
        }
    }
    when (type) {
        "word" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process: Number) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                drawWordCloudDataPoints(series, opts, config, context, process)
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "map" -> 
            {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                drawMapDataPoints(series, opts, config, context)
            }
        "funnel" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process: Number) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                opts.chartData.funnelData = drawFunnelDataPoints(series, opts, config, context, process)
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, 0, _uA())
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "line" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process: Number) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                drawYAxisGrid(categories, opts, config, context)
                drawXAxis(categories, opts, config, context)
                var _drawLineDataPoints = drawLineDataPoints(series, opts, config, context, process)
                var xAxisPoints = _drawLineDataPoints.xAxisPoints
                var calPoints = _drawLineDataPoints.calPoints
                var eachSpacing = _drawLineDataPoints.eachSpacing
                opts.chartData.xAxisPoints = xAxisPoints
                opts.chartData.calPoints = calPoints
                opts.chartData.eachSpacing = eachSpacing
                drawYAxis(series, opts, config, context)
                if (opts.enableMarkLine != false && process == 1) {
                    drawMarkLine(opts, config, context)
                }
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, eachSpacing, xAxisPoints)
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "scatter" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                drawYAxisGrid(categories, opts, config, context)
                drawXAxis(categories, opts, config, context)
                var _drawScatterDataPoints = drawScatterDataPoints(series, opts, config, context, process)
                var xAxisPoints = _drawScatterDataPoints.xAxisPoints
                var calPoints = _drawScatterDataPoints.calPoints
                var eachSpacing = _drawScatterDataPoints.eachSpacing
                opts.chartData.xAxisPoints = xAxisPoints
                opts.chartData.calPoints = calPoints
                opts.chartData.eachSpacing = eachSpacing
                drawYAxis(series, opts, config, context)
                if (opts.enableMarkLine != false && process == 1) {
                    drawMarkLine(opts, config, context)
                }
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, eachSpacing, xAxisPoints)
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "bubble" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process) {
                context.clearRect(0, 0, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                drawYAxisGrid(categories, opts, config, context)
                drawXAxis(categories, opts, config, context)
                var _drawBubbleDataPoints = drawBubbleDataPoints(series, opts, config, context, process)
                var xAxisPoints = _drawBubbleDataPoints.xAxisPoints
                var calPoints = _drawBubbleDataPoints.calPoints
                var eachSpacing = _drawBubbleDataPoints.eachSpacing
                opts.chartData.xAxisPoints = xAxisPoints
                opts.chartData.calPoints = calPoints
                opts.chartData.eachSpacing = eachSpacing
                drawYAxis(series, opts, config, context)
                if (opts.enableMarkLine != false && process == 1) {
                    drawMarkLine(opts, config, context)
                }
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, eachSpacing, xAxisPoints)
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "mix" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                drawYAxisGrid(categories, opts, config, context)
                drawXAxis(categories, opts, config, context)
                var _drawMixDataPoints = drawMixDataPoints(series, opts, config, context, process)
                var xAxisPoints = _drawMixDataPoints.xAxisPoints
                var calPoints = _drawMixDataPoints.calPoints
                var eachSpacing = _drawMixDataPoints.eachSpacing
                opts.chartData.xAxisPoints = xAxisPoints
                opts.chartData.calPoints = calPoints
                opts.chartData.eachSpacing = eachSpacing
                drawYAxis(series, opts, config, context)
                if (opts.enableMarkLine != false && process == 1) {
                    drawMarkLine(opts, config, context)
                }
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, eachSpacing, xAxisPoints)
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "column" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process: Number){
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                drawYAxisGrid(categories, opts, config, context)
                drawXAxis(categories, opts, config, context)
                var _drawColumnDataPoints = drawColumnDataPoints(series, opts, config, context, process)
                var xAxisPoints = _drawColumnDataPoints.xAxisPoints
                var calPoints = _drawColumnDataPoints.calPoints
                var eachSpacing = _drawColumnDataPoints.eachSpacing
                opts.chartData.xAxisPoints = xAxisPoints
                opts.chartData.calPoints = calPoints
                opts.chartData.eachSpacing = eachSpacing
                drawYAxis(series, opts, config, context)
                if (opts.enableMarkLine != false && process == 1) {
                    drawMarkLine(opts, config, context)
                }
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, eachSpacing, xAxisPoints)
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun(){
                _this.uevent.trigger("renderComplete")
            }
            ))
        "mount" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process: Number) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                drawYAxisGrid(categories, opts, config, context)
                drawXAxis(categories, opts, config, context)
                var _drawMountDataPoints = drawMountDataPoints(series, opts, config, context, process)
                var xAxisPoints = _drawMountDataPoints.xAxisPoints
                var calPoints = _drawMountDataPoints.calPoints
                var eachSpacing = _drawMountDataPoints.eachSpacing!!
                opts.chartData.xAxisPoints = xAxisPoints
                opts.chartData.calPoints = calPoints
                opts.chartData.eachSpacing = eachSpacing
                drawYAxis(series, opts, config, context)
                if (opts.enableMarkLine != false && process == 1) {
                    drawMarkLine(opts, config, context)
                }
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, eachSpacing, xAxisPoints)
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "bar" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process: Number) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                drawXAxis(categories, opts, config, context)
                var _drawBarDataPoints = drawBarDataPoints(series, opts, config, context, process)
                var yAxisPoints = _drawBarDataPoints.yAxisPoints
                var calPoints = _drawBarDataPoints.calPoints
                var eachSpacing = _drawBarDataPoints.eachSpacing
                opts.chartData.yAxisPoints = yAxisPoints
                opts.chartData.xAxisPoints = opts.chartData.xAxisData.xAxisPoints
                opts.chartData.calPoints = calPoints
                opts.chartData.eachSpacing = eachSpacing
                drawYAxis(series, opts, config, context)
                if (opts.enableMarkLine != false && process == 1) {
                    drawMarkLine(opts, config, context)
                }
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, eachSpacing, yAxisPoints)
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "area" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process: Number) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                drawYAxisGrid(categories, opts, config, context)
                drawXAxis(categories, opts, config, context)
                var _drawAreaDataPoints = drawAreaDataPoints(series, opts, config, context, process)
                var xAxisPoints = _drawAreaDataPoints.xAxisPoints
                var calPoints = _drawAreaDataPoints.calPoints
                var eachSpacing = _drawAreaDataPoints.eachSpacing!!
                opts.chartData.xAxisPoints = xAxisPoints
                opts.chartData.calPoints = calPoints
                opts.chartData.eachSpacing = eachSpacing
                drawYAxis(series, opts, config, context)
                if (opts.enableMarkLine != false && process == 1) {
                    drawMarkLine(opts, config, context)
                }
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, eachSpacing, xAxisPoints)
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "ring" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process: Number) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                opts.chartData.pieData = drawRingDataPoints(series, opts, config, context, process)
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, 0, _uA())
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "pie" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process: Number) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                opts.chartData.pieData = drawPieDataPoints(series, opts, config, context, process)
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, 0, _uA())
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "rose" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process: Number) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                opts.chartData.pieData = drawRoseDataPoints(series, opts, config, context, process)
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, 0, _uA())
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "radar" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process: Number) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                opts.chartData.radarData = drawRadarDataPoints(series, opts, config, context, process)
                drawLegend(opts.series!!, opts, config, context, opts.chartData)
                drawToolTipBridge(opts, config, context, process, 0, _uA())
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "arcbar" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process: Number) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                opts.chartData.arcbarData = drawArcbarDataPoints(series, opts, config, context, process)
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "gauge" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                opts.chartData.gaugeData = drawGaugeDataPoints(opts.categoriesObj!!, series, opts, config, context, process)
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
        "candle" -> 
            _this.animationInstance = Animation(ChartAnimationOption(canvasContext = opts.canvasContext!!, timing = opts.timing, duration = duration, onProcess = fun(process) {
                context.clearRect(opts.xOffset, opts.yOffset, opts.width, opts.height)
                if (opts.rotate) {
                    contextRotate(context, opts)
                }
                drawYAxisGrid(categories, opts, config, context)
                drawXAxis(categories, opts, config, context)
                var _drawCandleDataPoints = drawCandleDataPoints(series, seriesMA, opts, config, context, process)
                var xAxisPoints = _drawCandleDataPoints.xAxisPoints
                var calPoints = _drawCandleDataPoints.calPoints
                var eachSpacing = _drawCandleDataPoints.eachSpacing
                opts.chartData.xAxisPoints = xAxisPoints
                opts.chartData.calMapPoints = calPoints
                opts.chartData.eachSpacing = eachSpacing
                drawYAxis(series, opts, config, context)
                if (opts.enableMarkLine != false && process == 1) {
                    drawMarkLine(opts, config, context)
                }
                if (seriesMA.length > 0) {
                    drawLegend(seriesMA, opts, config, context, opts.chartData)
                } else {
                    drawLegend(opts.series!!, opts, config, context, opts.chartData)
                }
                if (opts.showTooltip) {
                    drawToolTipBridge(opts, config, context, process, eachSpacing, xAxisPoints)
                }
                drawCanvas(opts, context)
            }
            , onAnimationFinish = fun() {
                _this.uevent.trigger("renderComplete")
            }
            ))
    }
}
fun extraColumnInit(e: UTSJSONObject): ChartOptionsExtraColumn {
    val extra_column = ChartOptionsExtraColumn()
    if (e["type"] != null) {
        extra_column.type = e.getString("type") as String
    }
    if (e["width"] != null) {
        extra_column.width = e.getNumber("width")!!
    }
    if (e["seriesGap"] != null) {
        extra_column.seriesGap = e.getNumber("seriesGap")!!
    }
    if (e["categoryGap"] != null) {
        extra_column.categoryGap = e.getNumber("categoryGap")!!
    }
    if (e["barBorderCircle"] != null) {
        extra_column.barBorderCircle = e.getBoolean("barBorderCircle")!!
    }
    if (e["barBorderRadius"] != null) {
        extra_column.barBorderRadius = e.getArray("barBorderRadius") as UTSArray<Number>
    }
    if (e["linearType"] != null) {
        extra_column.linearType = e.getString("linearType") as String
    }
    if (e["linearOpacity"] != null) {
        extra_column.linearOpacity = e.getNumber("linearOpacity")!!
    }
    if (e["customColor"] != null) {
        extra_column.customColor = e.getArray("customColor") as UTSArray<String>
    }
    if (e["colorStop"] != null) {
        extra_column.colorStop = e.getNumber("colorStop")!!
    }
    if (e["meterBorder"] != null) {
        extra_column.meterBorder = e.getNumber("meterBorder")!!
    }
    if (e["meterFillColor"] != null) {
        extra_column.meterFillColor = e.getString("meterFillColor")!!
    }
    if (e["activeBgColor"] != null) {
        extra_column.activeBgColor = e.getString("activeBgColor")!!
    }
    if (e["activeBgOpacity"] != null) {
        extra_column.activeBgOpacity = e.getNumber("activeBgOpacity")!!
    }
    if (e["labelPosition"] != null) {
        extra_column.labelPosition = e.getString("labelPosition") as String
    }
    return extra_column
}
fun extraMarkLineDataInit(e: UTSJSONObject): ChartOptionsExtraMarkLineData {
    val extra_marklinedata = ChartOptionsExtraMarkLineData()
    if (e["value"] != null) {
        extra_marklinedata.value = e.getNumber("value")
    }
    if (e["labelText"] != null) {
        extra_marklinedata.labelText = e.getString("labelText")
    }
    if (e["lineColor"] != null) {
        extra_marklinedata.lineColor = e.getString("lineColor")!!
    }
    if (e["showLabel"] != null) {
        extra_marklinedata.showLabel = e.getBoolean("showLabel")!!
    }
    if (e["labelAlign"] != null) {
        extra_marklinedata.labelAlign = e.getString("labelAlign") as String
    }
    if (e["labelOffsetX"] != null) {
        extra_marklinedata.labelOffsetX = e.getNumber("labelOffsetX")!!
    }
    if (e["labelOffsetY"] != null) {
        extra_marklinedata.labelOffsetY = e.getNumber("labelOffsetY")!!
    }
    if (e["labelPadding"] != null) {
        extra_marklinedata.labelPadding = e.getNumber("labelPadding")!!
    }
    if (e["labelFontSize"] != null) {
        extra_marklinedata.labelFontSize = e.getNumber("labelFontSize")!!
    }
    if (e["labelFontColor"] != null) {
        extra_marklinedata.labelFontColor = e.getString("labelFontColor")!!
    }
    if (e["labelBgColor"] != null) {
        extra_marklinedata.labelBgColor = e.getString("labelBgColor")!!
    }
    if (e["labelBgOpacity"] != null) {
        extra_marklinedata.labelBgOpacity = e.getNumber("labelBgOpacity")!!
    }
    if (e["yAxisIndex"] != null) {
        extra_marklinedata.yAxisIndex = e.getNumber("yAxisIndex")!!
    }
    if (e["y"] != null) {
        extra_marklinedata.y = e.getNumber("y")!!
    }
    return extra_marklinedata
}
fun extraMarkLineInit(e: UTSJSONObject): ChartOptionsExtraMarkLine {
    val extra_markline = ChartOptionsExtraMarkLine()
    if (e["type"] != null) {
        extra_markline.type = e.getString("type") as String
    }
    if (e["dashLength"] != null) {
        extra_markline.dashLength = e.getNumber("dashLength")!!
    }
    if (e["data"] != null) {
        val markline_data: UTSArray<ChartOptionsExtraMarkLineData> = _uA()
        val data = e.getArray("data") as UTSArray<UTSJSONObject>
        data.forEach(fun(item: UTSJSONObject){
            markline_data.push(extraMarkLineDataInit(item))
        }
        )
        extra_markline.data = markline_data
    }
    return extra_markline
}
fun extraTooltipInit(e: UTSJSONObject): ChartOptionsExtraTooltip {
    val extra_tooltip = ChartOptionsExtraTooltip()
    if (e["showBox"] != null) {
        extra_tooltip.showBox = e.getBoolean("showBox")!!
    }
    if (e["showArrow"] != null) {
        extra_tooltip.showArrow = e.getBoolean("showArrow")!!
    }
    if (e["showCategory"] != null) {
        extra_tooltip.showCategory = e.getBoolean("showCategory")!!
    }
    if (e["borderWidth"] != null) {
        extra_tooltip.borderWidth = e.getNumber("borderWidth")!!
    }
    if (e["borderRadius"] != null) {
        extra_tooltip.borderRadius = e.getNumber("borderRadius")!!
    }
    if (e["borderColor"] != null) {
        extra_tooltip.borderColor = e.getString("borderColor")!!
    }
    if (e["borderOpacity"] != null) {
        extra_tooltip.borderOpacity = e.getNumber("borderOpacity")!!
    }
    if (e["bgColor"] != null) {
        extra_tooltip.bgColor = e.getString("bgColor")!!
    }
    if (e["bgOpacity"] != null) {
        extra_tooltip.bgOpacity = e.getNumber("bgOpacity")!!
    }
    if (e["gridType"] != null) {
        extra_tooltip.gridType = e.getString("gridType") as String
    }
    if (e["dashLength"] != null) {
        extra_tooltip.dashLength = e.getNumber("dashLength")!!
    }
    if (e["gridColor"] != null) {
        extra_tooltip.gridColor = e.getString("gridColor")!!
    }
    if (e["boxPadding"] != null) {
        extra_tooltip.boxPadding = e.getNumber("boxPadding")!!
    }
    if (e["fontSize"] != null) {
        extra_tooltip.fontSize = e.getNumber("fontSize")!!
    }
    if (e["lineHeight"] != null) {
        extra_tooltip.lineHeight = e.getNumber("lineHeight")!!
    }
    if (e["fontColor"] != null) {
        extra_tooltip.fontColor = e.getString("fontColor")!!
    }
    if (e["legendShow"] != null) {
        extra_tooltip.legendShow = e.getBoolean("legendShow")!!
    }
    if (e["legendShape"] != null) {
        extra_tooltip.legendShape = e.getString("legendShape") as String
    }
    if (e["splitLine"] != null) {
        extra_tooltip.splitLine = e.getBoolean("splitLine")!!
    }
    if (e["horizentalLine"] != null) {
        extra_tooltip.horizentalLine = e.getBoolean("horizentalLine")!!
    }
    if (e["xAxisLabel"] != null) {
        extra_tooltip.xAxisLabel = e.getBoolean("xAxisLabel")!!
    }
    if (e["yAxisLabel"] != null) {
        extra_tooltip.yAxisLabel = e.getBoolean("yAxisLabel")!!
    }
    if (e["labelBgColor"] != null) {
        extra_tooltip.labelBgColor = e.getString("labelBgColor")!!
    }
    if (e["labelBgOpacity"] != null) {
        extra_tooltip.labelBgOpacity = e.getNumber("labelBgOpacity")!!
    }
    if (e["labelFontColor"] != null) {
        extra_tooltip.labelFontColor = e.getString("labelFontColor")!!
    }
    return extra_tooltip
}
fun extraMountInit(e: UTSJSONObject): ChartOptionsExtraMount {
    val extra_mount = ChartOptionsExtraMount()
    if (e["type"] != null) {
        extra_mount.type = e.getString("type") as String
    }
    if (e["widthRatio"] != null) {
        extra_mount.widthRatio = e.getNumber("widthRatio")!!
    }
    if (e["borderWidth"] != null) {
        extra_mount.borderWidth = e.getNumber("borderWidth")!!
    }
    if (e["barBorderCircle"] != null) {
        extra_mount.barBorderCircle = e.getBoolean("barBorderCircle")!!
    }
    if (e["barBorderRadius"] != null) {
        extra_mount.barBorderRadius = e.getArray("barBorderRadius") as UTSArray<Number>
    }
    if (e["linearType"] != null) {
        extra_mount.linearType = e.getString("linearType") as String
    }
    if (e["linearOpacity"] != null) {
        extra_mount.linearOpacity = e.getNumber("linearOpacity")!!
    }
    if (e["customColor"] != null) {
        extra_mount.customColor = e.getArray("customColor") as UTSArray<String>
    }
    if (e["colorStop"] != null) {
        extra_mount.colorStop = e.getNumber("colorStop") as Number
    }
    return extra_mount
}
fun extraBarInit(e: UTSJSONObject): ChartOptionsExtraBar {
    val extraBar = ChartOptionsExtraBar()
    if (e["type"] != null) {
        extraBar.type = e.getString("type") as String
    }
    if (e["width"] != null) {
        extraBar.width = e.getNumber("width")!!
    }
    if (e["seriesGap"] != null) {
        extraBar.seriesGap = e.getNumber("seriesGap")!!
    }
    if (e["categoryGap"] != null) {
        extraBar.categoryGap = e.getNumber("categoryGap")!!
    }
    if (e["barBorderCircle"] != null) {
        extraBar.barBorderCircle = e.getBoolean("barBorderCircle")!!
    }
    if (e["barBorderRadius"] != null) {
        extraBar.barBorderRadius = e.getArray("barBorderRadius") as UTSArray<Number>
    }
    if (e["linearType"] != null) {
        extraBar.linearType = e.getString("linearType") as String
    }
    if (e["linearOpacity"] != null) {
        extraBar.linearOpacity = e.getNumber("linearOpacity")!!
    }
    if (e["customColor"] != null) {
        extraBar.customColor = e.getArray("customColor") as UTSArray<String>
    }
    if (e["colorStop"] != null) {
        extraBar.colorStop = e.getNumber("colorStop")!!
    }
    if (e["activeBgColor"] != null) {
        extraBar.activeBgColor = e.getString("activeBgColor")!!
    }
    if (e["activeBgOpacity"] != null) {
        extraBar.activeBgOpacity = e.getNumber("activeBgOpacity")!!
    }
    return extraBar
}
fun extraLineInit(e: UTSJSONObject): ChartOptionsExtraLine {
    val extraLine = ChartOptionsExtraLine()
    if (e["type"] != null) {
        extraLine.type = e.getString("type") as String
    }
    if (e["width"] != null) {
        extraLine.width = e.getNumber("width")!!
    }
    if (e["activeType"] != null) {
        extraLine.activeType = e.getString("activeType") as String
    }
    if (e["linearType"] != null) {
        extraLine.linearType = e.getString("linearType") as String
    }
    if (e["onShadow"] != null) {
        extraLine.onShadow = e.getBoolean("onShadow")!!
    }
    if (e["animation"] != null) {
        extraLine.animation = e.getString("animation") as String
    }
    return extraLine
}
fun extraAreaInit(e: UTSJSONObject): ChartOptionsExtraArea {
    val extraArea = ChartOptionsExtraArea()
    if (e["type"] != null) {
        extraArea.type = e.getString("type") as String
    }
    if (e["opacity"] != null) {
        extraArea.opacity = e.getNumber("opacity")!!
    }
    if (e["addLine"] != null) {
        extraArea.addLine = e.getBoolean("addLine")!!
    }
    if (e["width"] != null) {
        extraArea.width = e.getNumber("width")!!
    }
    if (e["gradient"] != null) {
        extraArea.gradient = e.getBoolean("gradient")!!
    }
    if (e["activeType"] != null) {
        extraArea.activeType = e.getString("activeType") as String
    }
    return extraArea
}
fun extraPieInit(e: UTSJSONObject): ChartOptionsExtraPie {
    val extraPie = ChartOptionsExtraPie()
    if (e["activeOpacity"] != null) {
        extraPie.activeOpacity = e.getNumber("activeOpacity")!!
    }
    if (e["activeRadius"] != null) {
        extraPie.activeRadius = e.getNumber("activeRadius")!!
    }
    if (e["offsetAngle"] != null) {
        extraPie.offsetAngle = e.getNumber("offsetAngle")!!
    }
    if (e["customRadius"] != null) {
        extraPie.customRadius = e.getNumber("customRadius")!!
    }
    if (e["labelWidth"] != null) {
        extraPie.labelWidth = e.getNumber("labelWidth")!!
    }
    if (e["border"] != null) {
        extraPie.border = e.getBoolean("border")!!
    }
    if (e["borderWidth"] != null) {
        extraPie.borderWidth = e.getNumber("borderWidth")!!
    }
    if (e["borderColor"] != null) {
        extraPie.borderColor = e.getString("borderColor")!!
    }
    if (e["linearType"] != null) {
        extraPie.linearType = e.getString("linearType") as String
    }
    if (e["customColor"] != null) {
        extraPie.customColor = e.getArray("customColor") as UTSArray<String>
    }
    return extraPie
}
fun extraRingInit(e: UTSJSONObject): ChartOptionsExtraRing {
    val extraRing = ChartOptionsExtraRing()
    if (e["ringWidth"] != null) {
        extraRing.ringWidth = e.getNumber("ringWidth")!!
    }
    if (e["centerColor"] != null) {
        extraRing.centerColor = e.getString("centerColor")!!
    }
    if (e["activeOpacity"] != null) {
        extraRing.activeOpacity = e.getNumber("activeOpacity")!!
    }
    if (e["activeRadius"] != null) {
        extraRing.activeRadius = e.getNumber("activeRadius")!!
    }
    if (e["offsetAngle"] != null) {
        extraRing.offsetAngle = e.getNumber("offsetAngle")
    }
    if (e["customRadius"] != null) {
        extraRing.customRadius = e.getNumber("customRadius")
    }
    if (e["labelWidth"] != null) {
        extraRing.labelWidth = e.getNumber("labelWidth")!!
    }
    if (e["border"] != null) {
        extraRing.border = e.getBoolean("border")!!
    }
    if (e["borderWidth"] != null) {
        extraRing.borderWidth = e.getNumber("borderWidth")!!
    }
    if (e["borderColor"] != null) {
        extraRing.borderColor = e.getString("borderColor")!!
    }
    if (e["linearType"] != null) {
        extraRing.linearType = e.getString("linearType") as String
    }
    if (e["customColor"] != null) {
        extraRing.customColor = e.getArray("customColor") as UTSArray<String>
    }
    return extraRing
}
fun extraRoseInit(e: UTSJSONObject): ChartOptionsExtraRose {
    val extraRose = ChartOptionsExtraRose()
    if (e["type"] != null) {
        extraRose.type = e.getString("type") as String
    }
    if (e["minRadius"] != null) {
        extraRose.minRadius = e.getNumber("minRadius")
    }
    if (e["activeOpacity"] != null) {
        extraRose.activeOpacity = e.getNumber("activeOpacity")!!
    }
    if (e["activeRadius"] != null) {
        extraRose.activeRadius = e.getNumber("activeRadius")!!
    }
    if (e["offsetAngle"] != null) {
        extraRose.offsetAngle = e.getNumber("offsetAngle")
    }
    if (e["labelWidth"] != null) {
        extraRose.labelWidth = e.getNumber("labelWidth")!!
    }
    if (e["border"] != null) {
        extraRose.border = e.getBoolean("border")!!
    }
    if (e["borderWidth"] != null) {
        extraRose.borderWidth = e.getNumber("borderWidth")!!
    }
    if (e["borderColor"] != null) {
        extraRose.borderColor = e.getString("borderColor")!!
    }
    if (e["linearType"] != null) {
        extraRose.linearType = e.getString("linearType") as String
    }
    if (e["customColor"] != null) {
        extraRose.customColor = e.getArray("customColor") as UTSArray<String>
    }
    return extraRose
}
fun extraScatterInit(e: UTSJSONObject): ChartOptionsExtraScatter {
    val extraScatter = ChartOptionsExtraScatter()
    if (e["id"] != null) {
        extraScatter.id = e.getString("id")
    }
    return extraScatter
}
fun extraBubbleInit(e: UTSJSONObject): ChartOptionsExtraBubble {
    val extraBubble = ChartOptionsExtraBubble()
    if (e["border"] != null) {
        extraBubble.border = e.getNumber("border")!!
    }
    if (e["opacity"] != null) {
        extraBubble.opacity = e.getNumber("opacity")!!
    }
    return extraBubble
}
fun extraMixColumnInit(e: UTSJSONObject): ChartOptionsExtraMixColumn {
    val extraMixColumn = ChartOptionsExtraMixColumn()
    if (e["width"] != null) {
        extraMixColumn.width = e.getNumber("width")!!
    }
    if (e["seriesGap"] != null) {
        extraMixColumn.seriesGap = e.getNumber("seriesGap")!!
    }
    if (e["categoryGap"] != null) {
        extraMixColumn.categoryGap = e.getNumber("categoryGap")!!
    }
    if (e["barBorderCircle"] != null) {
        extraMixColumn.barBorderCircle = e.getBoolean("barBorderCircle")!!
    }
    if (e["barBorderRadius"] != null) {
        extraMixColumn.barBorderRadius = e.getArray("barBorderRadius") as UTSArray<Number>
    }
    if (e["linearType"] != null) {
        extraMixColumn.linearType = e.getString("linearType") as String
    }
    if (e["linearOpacity"] != null) {
        extraMixColumn.linearOpacity = e.getNumber("linearOpacity")!!
    }
    if (e["customColor"] != null) {
        extraMixColumn.customColor = e.getArray("customColor") as UTSArray<String>
    }
    if (e["colorStop"] != null) {
        extraMixColumn.colorStop = e.getNumber("colorStop")!!
    }
    return extraMixColumn
}
fun extraMixAreaInit(e: UTSJSONObject): ChartOptionsExtraMixArea {
    val extraMixArea = ChartOptionsExtraMixArea()
    if (e["gradient"] != null) {
        extraMixArea.gradient = e.getBoolean("gradient")!!
    }
    if (e["opacity"] != null) {
        extraMixArea.opacity = e.getNumber("opacity")!!
    }
    return extraMixArea
}
fun extraMixLineInit(e: UTSJSONObject): ChartOptionsExtraMixLine {
    val extraMixLine = ChartOptionsExtraMixLine()
    if (e["width"] != null) {
        extraMixLine.width = e.getNumber("width")!!
    }
    return extraMixLine
}
fun extraMixInit(e: UTSJSONObject): ChartOptionsExtraMix {
    val extraMix = ChartOptionsExtraMix()
    if (e["column"] != null) {
        extraMix.column = extraMixColumnInit(e.getJSON("column")!!)
    }
    if (e["area"] != null) {
        extraMix.area = extraMixAreaInit(e.getJSON("area")!!)
    }
    if (e["line"] != null) {
        extraMix.line = extraMixLineInit(e.getJSON("line")!!)
    }
    return extraMix
}
fun extraRadarInit(e: UTSJSONObject): ChartOptionsExtraRadar {
    val extraRadar = ChartOptionsExtraRadar()
    if (e["gridType"] != null) {
        extraRadar.gridType = e.getString("gridType") as String
    }
    if (e["gridColor"] != null) {
        extraRadar.gridColor = e.getString("gridColor")!!
    }
    if (e["gridCount"] != null) {
        extraRadar.gridCount = e.getNumber("gridCount")!!
    }
    if (e["gridEval"] != null) {
        extraRadar.gridEval = e.getNumber("gridEval")!!
    }
    if (e["radius"] != null) {
        extraRadar.radius = e.getNumber("radius")!!
    }
    if (e["axisLabel"] != null) {
        extraRadar.axisLabel = e.getBoolean("axisLabel")!!
    }
    if (e["axisLabelTofix"] != null) {
        extraRadar.axisLabelTofix = e.getNumber("axisLabelTofix")!!
    }
    if (e["labelShow"] != null) {
        extraRadar.labelShow = e.getBoolean("labelShow")!!
    }
    if (e["labelColor"] != null) {
        extraRadar.labelColor = e.getString("labelColor")!!
    }
    if (e["labelPointShow"] != null) {
        extraRadar.labelPointShow = e.getBoolean("labelPointShow")!!
    }
    if (e["labelPointRadius"] != null) {
        extraRadar.labelPointRadius = e.getNumber("labelPointRadius")!!
    }
    if (e["labelPointColor"] != null) {
        extraRadar.labelPointColor = e.getString("labelPointColor")!!
    }
    if (e["opacity"] != null) {
        extraRadar.opacity = e.getNumber("opacity")!!
    }
    if (e["border"] != null) {
        extraRadar.border = e.getBoolean("border")!!
    }
    if (e["borderWidth"] != null) {
        extraRadar.borderWidth = e.getNumber("borderWidth")!!
    }
    if (e["max"] != null) {
        extraRadar.max = e.getNumber("max")!!
    }
    if (e["linearType"] != null) {
        extraRadar.linearType = e.getString("linearType") as String
    }
    if (e["customColor"] != null) {
        extraRadar.customColor = e.getArray("customColor") as UTSArray<String>
    }
    return extraRadar
}
fun extraArcbarInit(e: UTSJSONObject): ChartOptionsExtraArcbar {
    val extraArcbar = ChartOptionsExtraArcbar()
    if (e["type"] != null) {
        extraArcbar.type = e.getString("type") as String
    }
    if (e["direction"] != null) {
        extraArcbar.direction = e.getString("direction") as String
    }
    if (e["width"] != null) {
        extraArcbar.width = e.getNumber("width")!!
    }
    if (e["lineCap"] != null) {
        extraArcbar.lineCap = e.getString("lineCap") as String
    }
    if (e["backgroundColor"] != null) {
        extraArcbar.backgroundColor = e.getString("backgroundColor")!!
    }
    if (e["startAngle"] != null) {
        extraArcbar.startAngle = e.getNumber("startAngle")!!
    }
    if (e["endAngle"] != null) {
        extraArcbar.endAngle = e.getNumber("endAngle")!!
    }
    if (e["radius"] != null) {
        extraArcbar.radius = e.getNumber("radius")!!
    }
    if (e["gap"] != null) {
        extraArcbar.gap = e.getNumber("gap")!!
    }
    if (e["centerX"] != null) {
        extraArcbar.centerX = e.getNumber("centerX")!!
    }
    if (e["centerY"] != null) {
        extraArcbar.centerY = e.getNumber("centerY")!!
    }
    if (e["linearType"] != null) {
        extraArcbar.linearType = e.getString("linearType") as String
    }
    if (e["customColor"] != null) {
        extraArcbar.customColor = e.getArray("customColor") as UTSArray<String>
    }
    return extraArcbar
}
fun extraGaugePointerInit(e: UTSJSONObject): ChartOptionsExtraGaugePointer {
    val extraGaugePointer = ChartOptionsExtraGaugePointer()
    if (e["width"] != null) {
        extraGaugePointer.width = e.getNumber("width")!!
    }
    if (e["color"] != null) {
        extraGaugePointer.color = e.getString("color")!!
    }
    return extraGaugePointer
}
fun extraGaugeSplitLineInit(e: UTSJSONObject): ChartOptionsExtraGaugeSplitLine {
    val extraGaugeSplitLine = ChartOptionsExtraGaugeSplitLine()
    if (e["fixRadius"] != null) {
        extraGaugeSplitLine.fixRadius = e.getNumber("fixRadius")!!
    }
    if (e["splitNumber"] != null) {
        extraGaugeSplitLine.splitNumber = e.getNumber("splitNumber")!!
    }
    if (e["width"] != null) {
        extraGaugeSplitLine.width = e.getNumber("width")!!
    }
    if (e["color"] != null) {
        extraGaugeSplitLine.color = e.getString("color")!!
    }
    if (e["childNumber"] != null) {
        extraGaugeSplitLine.childNumber = e.getNumber("childNumber")!!
    }
    if (e["childWidth"] != null) {
        extraGaugeSplitLine.childWidth = e.getNumber("childWidth")!!
    }
    return extraGaugeSplitLine
}
fun extraGaugeInit(e: UTSJSONObject): ChartOptionsExtraGauge {
    val extraGauge = ChartOptionsExtraGauge()
    if (e["type"] != null) {
        extraGauge.type = e.getString("type")!!
    }
    if (e["width"] != null) {
        extraGauge.width = e.getNumber("width")!!
    }
    if (e["labelColor"] != null) {
        extraGauge.labelColor = e.getString("labelColor")!!
    }
    if (e["labelOffset"] != null) {
        extraGauge.labelOffset = e.getNumber("labelOffset")!!
    }
    if (e["startAngle"] != null) {
        extraGauge.startAngle = e.getNumber("startAngle")!!
    }
    if (e["endAngle"] != null) {
        extraGauge.endAngle = e.getNumber("endAngle")!!
    }
    if (e["startNumber"] != null) {
        extraGauge.startNumber = e.getNumber("startNumber")!!
    }
    if (e["endNumber"] != null) {
        extraGauge.endNumber = e.getNumber("endNumber")!!
    }
    if (e["formatter"] != null) {
        extraGauge.formatter = e.getAny("formatter")!!
    }
    if (e["format"] != null) {
        extraGauge.format = e.getString("format")!!
    }
    if (e["splitLine"] != null) {
        extraGauge.splitLine = extraGaugeSplitLineInit(e.getJSON("splitLine")!!)
    }
    if (e["pointer"] != null) {
        extraGauge.pointer = extraGaugePointerInit(e.getJSON("pointer")!!)
    }
    return extraGauge
}
fun extraFunnelInit(e: UTSJSONObject): ChartOptionsExtraFunnel {
    val extraFunnel = ChartOptionsExtraFunnel()
    if (e["type"] != null) {
        extraFunnel.type = e.getString("type") as String
    }
    if (e["activeOpacity"] != null) {
        extraFunnel.activeOpacity = e.getNumber("activeOpacity")!!
    }
    if (e["activeWidth"] != null) {
        extraFunnel.activeWidth = e.getNumber("activeWidth")!!
    }
    if (e["border"] != null) {
        extraFunnel.border = e.getBoolean("border")!!
    }
    if (e["borderWidth"] != null) {
        extraFunnel.borderWidth = e.getNumber("borderWidth")!!
    }
    if (e["borderColor"] != null) {
        extraFunnel.borderColor = e.getString("borderColor")!!
    }
    if (e["fillOpacity"] != null) {
        extraFunnel.fillOpacity = e.getNumber("fillOpacity")!!
    }
    if (e["minSize"] != null) {
        extraFunnel.minSize = e.getNumber("minSize")!!
    }
    if (e["labelAlign"] != null) {
        extraFunnel.labelAlign = e.getString("labelAlign") as String
    }
    if (e["linearType"] != null) {
        extraFunnel.linearType = e.getString("linearType") as String
    }
    if (e["customColor"] != null) {
        extraFunnel.customColor = e.getArray("customColor") as UTSArray<String>
    }
    return extraFunnel
}
fun extraWordInit(e: UTSJSONObject): ChartOptionsExtraWord {
    val extraWord = ChartOptionsExtraWord()
    if (e["type"] != null) {
        extraWord.type = e.getString("type") as String
    }
    if (e["autoColors"] != null) {
        extraWord.autoColors = e.getBoolean("autoColors")!!
    }
    return extraWord
}
fun extraCandleColorInit(e: UTSJSONObject): ChartOptionsExtraCandleColor {
    val extraCandleColor = ChartOptionsExtraCandleColor()
    if (e["upLine"] != null) {
        extraCandleColor.upLine = e.getString("upLine")!!
    }
    if (e["upFill"] != null) {
        extraCandleColor.upFill = e.getString("upFill")!!
    }
    if (e["downLine"] != null) {
        extraCandleColor.downLine = e.getString("downLine")!!
    }
    if (e["downFill"] != null) {
        extraCandleColor.downFill = e.getString("downFill")!!
    }
    return extraCandleColor
}
fun extraCandleAverageInit(e: UTSJSONObject): ChartOptionsExtraCandleAverage {
    val extraCandleAverage = ChartOptionsExtraCandleAverage()
    if (e["show"] != null) {
        extraCandleAverage.show = e.getBoolean("show")!!
    }
    if (e["name"] != null) {
        extraCandleAverage.name = e.getArray("name") as UTSArray<String>
    }
    if (e["day"] != null) {
        extraCandleAverage.day = e.getArray("day") as UTSArray<Number>
    }
    if (e["color"] != null) {
        extraCandleAverage.color = e.getArray("color") as UTSArray<String>
    }
    return extraCandleAverage
}
fun extraCandlelInit(e: UTSJSONObject): ChartOptionsExtraCandle {
    val extraCandle = ChartOptionsExtraCandle()
    if (e["color"] != null) {
        extraCandle.color = extraCandleColorInit(e.getJSON("color")!!)
    }
    if (e["average"] != null) {
        extraCandle.average = extraCandleAverageInit(e.getJSON("average")!!)
    }
    return extraCandle
}
fun extraMapInit(e: UTSJSONObject): ChartOptionsExtraMap {
    val extraMap = ChartOptionsExtraMap()
    if (e["border"] != null) {
        extraMap.border = e.getBoolean("border")!!
    }
    if (e["mercator"] != null) {
        extraMap.mercator = e.getBoolean("mercator")!!
    }
    if (e["borderWidth"] != null) {
        extraMap.borderWidth = e.getNumber("borderWidth")!!
    }
    if (e["borderColor"] != null) {
        extraMap.borderColor = e.getString("borderColor")!!
    }
    if (e["fillOpacity"] != null) {
        extraMap.fillOpacity = e.getNumber("fillOpacity")!!
    }
    if (e["active"] != null) {
        extraMap.active = e.getBoolean("active")!!
    }
    if (e["activeTextColor"] != null) {
        extraMap.activeTextColor = e.getString("activeTextColor")!!
    }
    if (e["activeBorderColor"] != null) {
        extraMap.activeBorderColor = e.getString("activeBorderColor")!!
    }
    if (e["activeFillColor"] != null) {
        extraMap.activeFillColor = e.getString("activeFillColor")!!
    }
    if (e["activeFillOpacity"] != null) {
        extraMap.activeFillOpacity = e.getNumber("activeFillOpacity")!!
    }
    return extraMap
}
fun extraInit(e: UTSJSONObject): ChartOptionsExtra {
    val extra = ChartOptionsExtra()
    if (e["column"] != null) {
        extra.column = extraColumnInit(e.getJSON("column")!!)
    }
    if (e["markLine"] != null) {
        extra.markLine = extraMarkLineInit(e.getJSON("markLine")!!)
    }
    if (e["tooltip"] != null) {
        extra.tooltip = extraTooltipInit(e.getJSON("tooltip")!!)
    }
    if (e["mount"] != null) {
        extra.mount = extraMountInit(e.getJSON("mount")!!)
    }
    if (e["bar"] != null) {
        extra.bar = extraBarInit(e.getJSON("bar")!!)
    }
    if (e["line"] != null) {
        extra.line = extraLineInit(e.getJSON("line")!!)
    }
    if (e["area"] != null) {
        extra.area = extraAreaInit(e.getJSON("area")!!)
    }
    if (e["pie"] != null) {
        extra.pie = extraPieInit(e.getJSON("pie")!!)
    }
    if (e["ring"] != null) {
        extra.ring = extraRingInit(e.getJSON("ring")!!)
    }
    if (e["rose"] != null) {
        extra.rose = extraRoseInit(e.getJSON("rose")!!)
    }
    if (e["scatter"] != null) {
        extra.scatter = extraScatterInit(e.getJSON("scatter")!!)
    }
    if (e["bubble"] != null) {
        extra.bubble = extraBubbleInit(e.getJSON("bubble")!!)
    }
    if (e["mix"] != null) {
        extra.mix = extraMixInit(e.getJSON("mix")!!)
    }
    if (e["radar"] != null) {
        extra.radar = extraRadarInit(e.getJSON("radar")!!)
    }
    if (e["arcbar"] != null) {
        extra.arcbar = extraArcbarInit(e.getJSON("arcbar")!!)
    }
    if (e["gauge"] != null) {
        extra.gauge = extraGaugeInit(e.getJSON("gauge")!!)
    }
    if (e["funnel"] != null) {
        extra.funnel = extraFunnelInit(e.getJSON("funnel")!!)
    }
    if (e["word"] != null) {
        extra.word = extraWordInit(e.getJSON("word")!!)
    }
    if (e["candle"] != null) {
        extra.candle = extraCandlelInit(e.getJSON("candle")!!)
    }
    if (e["map"] != null) {
        extra.map = extraMapInit(e.getJSON("map")!!)
    }
    return extra
}
fun seriesDataInit(e: UTSJSONObject): ChartOptionsSeriesData {
    val series_data = ChartOptionsSeriesData()
    if (e["name"] != null) {
        series_data.name = e.getString("name")!!
    }
    if (e["value"] != null) {
        series_data.value = e.getNumber("value")!!
    }
    if (e["color"] != null) {
        series_data.color = e.getString("color")!!
    }
    if (e["labelText"] != null) {
        series_data.labelText = e.getString("labelText")!!
    }
    if (e["labelShow"] != null) {
        series_data.labelShow = e.getBoolean("labelShow")!!
    }
    if (e["centerText"] != null) {
        series_data.centerText = e.getString("centerText")!!
    }
    if (e["centerTextSize"] != null) {
        series_data.centerTextSize = e.getNumber("centerTextSize")!!
    }
    if (e["centerTextColor"] != null) {
        series_data.centerTextColor = e.getString("centerTextColor")!!
    }
    return series_data
}
fun seriesInit(e: UTSJSONObject, type: String = ""): ChartOptionsSeries {
    val series = ChartOptionsSeries()
    if (e["index"] != null) {
        series.index = e.getNumber("index")!!
    }
    if (e["name"] != null) {
        series.name = e.getString("name")!!
    }
    if (e["legendText"] != null) {
        series.legendText = e.getString("legendText")!!
    }
    if (e["show"] != null) {
        series.show = e.getBoolean("show")!!
    }
    if (e["color"] != null) {
        series.color = e.getString("color")!!
    }
    if (e["fillOpacity"] != null) {
        series.fillOpacity = e.getNumber("fillOpacity")!!
    }
    if (e["textColor"] != null) {
        series.textColor = e.getString("textColor")!!
    }
    if (e["textSize"] != null) {
        series.textSize = e.getNumber("textSize")!!
    }
    if (e["textOffset"] != null) {
        series.textOffset = e.getNumber("textOffset")!!
    }
    if (e["linearIndex"] != null) {
        series.linearIndex = e.getNumber("linearIndex")!!
    }
    if (e["type"] != null) {
        series.type = e.getString("type")!!
    }
    if (e["disableLegend"] != null) {
        series.disableLegend = e.getBoolean("disableLegend")!!
    }
    if (e["connectNulls"] != null) {
        series.connectNulls = e.getBoolean("connectNulls")!!
    }
    if (e["setShadow"] != null) {
        series.setShadow = e.getArray("setShadow") as UTSArray<String>
    }
    if (e["linearColor"] != null) {
        series.linearColor = e.getArray("linearColor") as UTSArray<UTSArray<String>>
    }
    if (e["style"] != null) {
        series.style = e.getString("style")!!
    }
    if (e["addPoint"] != null) {
        series.addPoint = e.getBoolean("addPoint")!!
    }
    if (e["lineType"] != null) {
        series.lineType = e.getString("lineType")!!
    }
    if (e["dashLength"] != null) {
        series.dashLength = e.getNumber("dashLength")!!
    }
    if (e["pointShape"] != null) {
        series.pointShape = e.getString("pointShape")!!
    }
    if (e["legendShape"] != null) {
        series.legendShape = e.getString("legendShape")!!
    }
    if (e["formatter"] != null) {
        series.formatter = e.getAny("formatter")!!
    }
    if (e["format"] != null) {
        series.format = e.getString("format")!!
    }
    if (e["rotate"] != null) {
        series.rotate = e.getBoolean("rotate")!!
    }
    if (e["seriesMA"] != null) {
        val seriesmas: UTSArray<ChartOptionsSeries> = _uA()
        val data = e.getArray("seriesMA") as UTSArray<UTSJSONObject>
        data.forEach(fun(item: UTSJSONObject){
            seriesmas.push(seriesInit(item))
        }
        )
        series.seriesMA = seriesmas
    }
    if (e["data"] != null) {
        if (type == "scatter" || type == "bubble" || type == "candle") {
            series.dataArr = e.getArray("data") as UTSArray<UTSArray<Number>>
        } else {
            val seriesdata: UTSArray<ChartOptionsSeriesData> = _uA()
            val data = e.getArray("data") as UTSArray<UTSJSONObject>
            data.forEach(fun(item: UTSJSONObject){
                seriesdata.push(seriesDataInit(item))
            }
            )
            series.data = seriesdata
        }
    }
    if (type == "map") {
        if (e["properties"] != null) {
            series.properties = mapSeriesPropertiesInit(e.getJSON("properties")!!)
        }
        if (e["geometry"] != null) {
            series.geometry = mapSeriesGeometryInit(e.getJSON("geometry")!!)
        }
    }
    return series
}
fun xAxisInit(e: UTSJSONObject): ChartOptionsXAxis {
    val xAxis: ChartOptionsXAxis = ChartOptionsXAxis()
    if (e["disabled"] != null) {
        xAxis.disabled = e.getBoolean("disabled")!!
    }
    if (e["axisLine"] != null) {
        xAxis.axisLine = e.getBoolean("axisLine")!!
    }
    if (e["axisLineColor"] != null) {
        xAxis.axisLineColor = e.getString("axisLineColor")!!
    }
    if (e["calibration"] != null) {
        xAxis.calibration = e.getBoolean("calibration")!!
    }
    if (e["fontColor"] != null) {
        xAxis.fontColor = e.getString("fontColor")!!
    }
    if (e["fontSize"] != null) {
        xAxis.fontSize = e.getNumber("fontSize")!!
    }
    if (e["lineHeight"] != null) {
        xAxis.lineHeight = e.getNumber("lineHeight")!!
    }
    if (e["marginTop"] != null) {
        xAxis.marginTop = e.getNumber("marginTop")!!
    }
    if (e["rotateLabel"] != null) {
        xAxis.rotateLabel = e.getBoolean("rotateLabel")!!
    }
    if (e["rotateAngle"] != null) {
        xAxis.rotateAngle = e.getNumber("rotateAngle")!!
    }
    if (e["labelCount"] != null) {
        xAxis.labelCount = e.getNumber("labelCount")!!
    }
    if (e["itemCount"] != null) {
        xAxis.itemCount = e.getNumber("itemCount")!!
    }
    if (e["boundaryGap"] != null) {
        xAxis.boundaryGap = e.getString("boundaryGap") as String
    }
    if (e["disableGrid"] != null) {
        xAxis.disableGrid = e.getBoolean("disableGrid")!!
    }
    if (e["splitNumber"] != null) {
        xAxis.splitNumber = e.getNumber("splitNumber")!!
    }
    if (e["gridColor"] != null) {
        xAxis.gridColor = e.getString("gridColor")!!
    }
    if (e["gridType"] != null) {
        xAxis.gridType = e.getString("gridType") as String
    }
    if (e["dashLength"] != null) {
        xAxis.dashLength = e.getNumber("dashLength")!!
    }
    if (e["gridEval"] != null) {
        xAxis.gridEval = e.getNumber("gridEval")!!
    }
    if (e["scrollShow"] != null) {
        xAxis.scrollShow = e.getBoolean("scrollShow")!!
    }
    if (e["scrollAlign"] != null) {
        xAxis.scrollAlign = e.getString("scrollAlign") as String
    }
    if (e["scrollColor"] != null) {
        xAxis.scrollColor = e.getString("scrollColor")!!
    }
    if (e["scrollBackgroundColor"] != null) {
        xAxis.scrollBackgroundColor = e.getString("scrollBackgroundColor")!!
    }
    if (e["min"] != null) {
        xAxis.min = e.getNumber("min")!!
    }
    if (e["max"] != null) {
        xAxis.max = e.getNumber("max")!!
    }
    if (e["title"] != null) {
        xAxis.title = e.getString("title")!!
    }
    if (e["titleFontSize"] != null) {
        xAxis.titleFontSize = e.getNumber("titleFontSize")!!
    }
    if (e["titleOffsetY"] != null) {
        xAxis.titleOffsetY = e.getNumber("titleOffsetY")!!
    }
    if (e["titleOffsetX"] != null) {
        xAxis.titleOffsetX = e.getNumber("titleOffsetX")!!
    }
    if (e["titleFontColor"] != null) {
        xAxis.titleFontColor = e.getString("titleFontColor")!!
    }
    if (e["formatter"] != null) {
        xAxis.formatter = e.getAny("formatter")!!
    }
    if (e["format"] != null) {
        xAxis.format = e.getString("format")!!
    }
    if (e["data"] != null) {
        val xAxis_data: UTSArray<ChartOptionsYAxisData> = _uA()
        val data = e.getArray("data") as UTSArray<UTSJSONObject>
        data.forEach(fun(item: UTSJSONObject){
            xAxis_data.push(yAxisDataInit(item))
        }
        )
        xAxis.data = xAxis_data
    }
    return xAxis
}
fun yAxisDataInit(e: UTSJSONObject): ChartOptionsYAxisData {
    val yAxis_data: ChartOptionsYAxisData = ChartOptionsYAxisData()
    if (e["type"] != null) {
        yAxis_data.type = e.getString("type")!!
    }
    if (e["position"] != null) {
        yAxis_data.position = e.getString("position") as String
    }
    if (e["disabled"] != null) {
        yAxis_data.disabled = e.getBoolean("disabled")!!
    }
    if (e["axisLine"] != null) {
        yAxis_data.axisLine = e.getBoolean("axisLine")!!
    }
    if (e["axisLineColor"] != null) {
        yAxis_data.axisLineColor = e.getString("axisLineColor")!!
    }
    if (e["calibration"] != null) {
        yAxis_data.calibration = e.getBoolean("calibration")!!
    }
    if (e["fontColor"] != null) {
        yAxis_data.fontColor = e.getString("fontColor")!!
    }
    if (e["fontSize"] != null) {
        yAxis_data.fontSize = e.getNumber("fontSize")!!
    }
    if (e["textAlign"] != null) {
        yAxis_data.textAlign = e.getString("textAlign") as String
    }
    if (e["title"] != null) {
        yAxis_data.title = e.getString("title")!!
    }
    if (e["titleFontSize"] != null) {
        yAxis_data.titleFontSize = e.getNumber("titleFontSize")!!
    }
    if (e["titleOffsetY"] != null) {
        yAxis_data.titleOffsetY = e.getNumber("titleOffsetY")!!
    }
    if (e["titleOffsetX"] != null) {
        yAxis_data.titleOffsetX = e.getNumber("titleOffsetX")!!
    }
    if (e["titleFontColor"] != null) {
        yAxis_data.titleFontColor = e.getString("titleFontColor")!!
    }
    if (e["min"] != null) {
        yAxis_data.min = e.getNumber("min")!!
    }
    if (e["max"] != null) {
        yAxis_data.max = e.getNumber("max")!!
    }
    if (e["tofix"] != null) {
        yAxis_data.tofix = e.getNumber("tofix")!!
    }
    if (e["unit"] != null) {
        yAxis_data.unit = e.getString("unit")!!
    }
    if (e["formatter"] != null) {
        yAxis_data.formatter = e.getAny("formatter")!!
    }
    if (e["format"] != null) {
        yAxis_data.format = e.getString("format")!!
    }
    return yAxis_data
}
fun yAxisInit(e: UTSJSONObject): ChartOptionsYAxis {
    val yAxis: ChartOptionsYAxis = ChartOptionsYAxis()
    if (e["disabled"] != null) {
        yAxis.disabled = e.getBoolean("disabled")!!
    }
    if (e["disableGrid"] != null) {
        yAxis.disableGrid = e.getBoolean("disableGrid")!!
    }
    if (e["splitNumber"] != null) {
        yAxis.splitNumber = e.getNumber("splitNumber")!!
    }
    if (e["gridType"] != null) {
        yAxis.gridType = e.getString("gridType") as String
    }
    if (e["dashLength"] != null) {
        yAxis.dashLength = e.getNumber("dashLength")!!
    }
    if (e["gridColor"] != null) {
        yAxis.gridColor = e.getString("gridColor")!!
    }
    if (e["padding"] != null) {
        yAxis.padding = e.getNumber("padding")!!
    }
    if (e["showTitle"] != null) {
        yAxis.showTitle = e.getBoolean("showTitle")!!
    }
    if (e["gridSet"] != null) {
        yAxis.gridSet = e.getString("gridSet")!!
    }
    if (e["fontColor"] != null) {
        yAxis.fontColor = e.getString("fontColor")!!
    }
    if (e["data"] != null) {
        val yAxis_data: UTSArray<ChartOptionsYAxisData> = _uA()
        val data = e.getArray("data") as UTSArray<UTSJSONObject>
        data.forEach(fun(item: UTSJSONObject){
            yAxis_data.push(yAxisDataInit(item))
        }
        )
        yAxis.data = yAxis_data
    }
    return yAxis
}
fun legendInit(e: UTSJSONObject): ChartOptionsLegend {
    val legend = ChartOptionsLegend()
    if (e["show"] != null) {
        legend.show = e.getBoolean("show")!!
    }
    if (e["position"] != null) {
        legend.position = e.getString("position") as String
    }
    if (e["float"] != null) {
        legend.float = e.getString("float") as String
    }
    if (e["padding"] != null) {
        legend.padding = e.getNumber("padding")!!
    }
    if (e["margin"] != null) {
        legend.margin = e.getNumber("margin")!!
    }
    if (e["backgroundColor"] != null) {
        legend.backgroundColor = e.getString("backgroundColor")!!
    }
    if (e["borderColor"] != null) {
        legend.borderColor = e.getString("borderColor")!!
    }
    if (e["borderWidth"] != null) {
        legend.borderWidth = e.getNumber("borderWidth")!!
    }
    if (e["fontSize"] != null) {
        legend.fontSize = e.getNumber("fontSize")!!
    }
    if (e["fontColor"] != null) {
        legend.fontColor = e.getString("fontColor")!!
    }
    if (e["lineHeight"] != null) {
        legend.lineHeight = e.getNumber("lineHeight")!!
    }
    if (e["hiddenColor"] != null) {
        legend.hiddenColor = e.getString("hiddenColor")!!
    }
    if (e["itemGap"] != null) {
        legend.itemGap = e.getNumber("itemGap")!!
    }
    return legend
}
fun titleInit(e: UTSJSONObject): ChartOptionsTitle {
    val title = ChartOptionsTitle()
    if (e["name"] != null) {
        title.name = e.getString("name")!!
    }
    if (e["fontSize"] != null) {
        title.fontSize = e.getNumber("fontSize")!!
    }
    if (e["color"] != null) {
        title.color = e.getString("color")!!
    }
    if (e["offsetX"] != null) {
        title.offsetX = e.getNumber("offsetX")!!
    }
    if (e["offsetY"] != null) {
        title.offsetY = e.getNumber("offsetY")!!
    }
    return title
}
fun subtitleInit(e: UTSJSONObject): ChartOptionsSubTitle {
    val subtitle = ChartOptionsSubTitle()
    if (e["name"] != null) {
        subtitle.name = e.getString("name")!!
    }
    if (e["fontSize"] != null) {
        subtitle.fontSize = e.getNumber("fontSize")!!
    }
    if (e["color"] != null) {
        subtitle.color = e.getString("color")!!
    }
    if (e["offsetX"] != null) {
        subtitle.offsetX = e.getNumber("offsetX")!!
    }
    if (e["offsetY"] != null) {
        subtitle.offsetY = e.getNumber("offsetY")!!
    }
    return subtitle
}
fun mapSeriesPropertiesParentInit(e: UTSJSONObject): ChartOptionsMapSeriesPropertiesParent {
    val parent = ChartOptionsMapSeriesPropertiesParent()
    if (e["adcode"] != null) {
        parent.adcode = e.getNumber("adcode")!!
    }
    return parent
}
fun mapSeriesPropertiesInit(e: UTSJSONObject): ChartOptionsMapSeriesProperties {
    val mapSeriesProperties = ChartOptionsMapSeriesProperties()
    if (e["adcode"] != null) {
        mapSeriesProperties.adcode = e.getNumber("adcode")!!
    }
    if (e["name"] != null) {
        mapSeriesProperties.name = e.getString("name")!!
    }
    if (e["center"] != null) {
        mapSeriesProperties.center = e.getArray("center") as UTSArray<Number>
    }
    if (e["centroid"] != null) {
        mapSeriesProperties.centroid = e.getArray("centroid") as UTSArray<Number>
    }
    if (e["childrenNum"] != null) {
        mapSeriesProperties.childrenNum = e.getNumber("childrenNum")!!
    }
    if (e["level"] != null) {
        mapSeriesProperties.level = e.getString("level")!!
    }
    if (e["subFeatureIndex"] != null) {
        mapSeriesProperties.subFeatureIndex = e.getNumber("subFeatureIndex")!!
    }
    if (e["acroutes"] != null) {
        mapSeriesProperties.acroutes = e.getArray("acroutes") as UTSArray<Number>
    }
    if (e["parent"] != null) {
        mapSeriesProperties.parent = mapSeriesPropertiesParentInit(e.getJSON("parent")!!)
    }
    return mapSeriesProperties
}
fun mapSeriesGeometryInit(e: UTSJSONObject): ChartOptionsMapSeriesGeometry {
    val geometry = ChartOptionsMapSeriesGeometry()
    if (e["type"] != null) {
        geometry.type = e.getString("type")!!
    }
    if (e["coordinates"] != null) {
        geometry.coordinates = e.getArray("coordinates") as UTSArray<UTSArray<UTSArray<UTSArray<Number>>>>
    }
    return geometry
}
fun optionsInit(e: UTSJSONObject): ChartOptions {
    val opts = ChartOptions()
    opts.canvasContext = e["canvasContext"] as CanvasContext
    opts.context = e["context"] as CanvasRenderingContext2D
    if (e["type"] != null) {
        opts.type = e.getString("type")!!
    }
    if (opts.type == "gauge") {
        if (e["categories"] != null) {
            val categoriesObj: UTSArray<ChartOptionsSeriesData> = _uA()
            val cate: UTSArray<UTSJSONObject> = e.getArray("categories") as UTSArray<UTSJSONObject>
            cate.forEach(fun(item: UTSJSONObject){
                val child = seriesDataInit(item)
                categoriesObj.push(child)
            }
            )
            opts.categoriesObj = categoriesObj
        }
    } else {
        if (e["categories"] != null) {
            opts.categories = e.getArray("categories") as UTSArray<String>
        }
    }
    if (e["width"] != null) {
        opts.width = e.getNumber("width")!!
    }
    if (e["height"] != null) {
        opts.height = e.getNumber("height")!!
    }
    if (e["canvas2d"] != null) {
        opts.canvas2d = e.getBoolean("canvas2d")!!
    }
    if (e["pixelRatio"] != null) {
        opts.pixelRatio = e.getNumber("pixelRatio")!!
    }
    if (e["animation"] != null) {
        opts.animation = e.getBoolean("animation")!!
    }
    if (e["timing"] != null) {
        opts.timing = e.getString("timing")!!
    }
    if (e["duration"] != null) {
        opts.duration = e.getNumber("duration")!!
    }
    if (e["rotate"] != null) {
        opts.rotate = e.getBoolean("rotate")!!
    }
    if (e["rotateLock"] != null) {
        opts.rotateLock = e.getBoolean("rotateLock")!!
    }
    if (e["background"] != null) {
        opts.background = e.getString("background")!!
    }
    if (e["color"] != null) {
        opts.color = e.getArray("color") as UTSArray<String>
    }
    if (e["padding"] != null) {
        opts.padding = e.getArray("padding") as UTSArray<Number>
    }
    if (e["fontSize"] != null) {
        opts.fontSize = e.getNumber("fontSize")!!
    }
    if (e["fontColor"] != null) {
        opts.fontColor = e.getString("fontColor")!!
    }
    if (e["dataLabel"] != null) {
        opts.dataLabel = e.getBoolean("dataLabel")!!
    }
    if (e["dataPointShape"] != null) {
        opts.dataPointShape = e.getBoolean("dataPointShape")!!
    }
    if (e["dataPointShapeType"] != null) {
        opts.dataPointShapeType = e.getString("dataPointShapeType")!!
    }
    if (e["touchMoveLimit"] != null) {
        opts.touchMoveLimit = e.getNumber("touchMoveLimit")!!
    }
    if (e["enableScroll"] != null) {
        opts.enableScroll = e.getBoolean("enableScroll")!!
    }
    if (e["enableMarkLine"] != null) {
        opts.enableMarkLine = e.getBoolean("enableMarkLine")!!
    }
    if (e["scrollPosition"] != null) {
        opts.scrollPosition = e.getString("scrollPosition")!!
    }
    if (e["xOffset"] != null) {
        opts.xOffset = e.getNumber("xOffset")!!
    }
    if (e["yOffset"] != null) {
        opts.yOffset = e.getNumber("yOffset")!!
    }
    if (e["tapLegend"] != null) {
        opts.dataLabel = e.getBoolean("tapLegend")!!
    }
    opts.legend.fontSize = opts.fontSize
    opts.legend.lineHeight = opts.fontSize
    opts.legend.fontColor = opts.fontColor
    opts.yAxis.dashLength = 4 * opts.pixelRatio
    if (e["series"] != null) {
        val series: UTSArray<ChartOptionsSeries> = _uA()
        val series_data = e.getArray("series") as UTSArray<UTSJSONObject>
        series_data.forEach(fun(item: UTSJSONObject){
            series.push(seriesInit(item, opts.type))
        }
        )
        opts.series = series
    }
    if (e["xAxis"] != null) {
        opts.xAxis = xAxisInit(e.getJSON("xAxis")!!)
    }
    if (e["yAxis"] != null) {
        opts.yAxis = yAxisInit(e.getJSON("yAxis")!!)
    }
    if (e["legend"] != null) {
        opts.legend = legendInit(e.getJSON("legend")!!)
    }
    if (e["title"] != null) {
        opts.title = titleInit(e.getJSON("title")!!)
    }
    if (e["subtitle"] != null) {
        opts.subtitle = subtitleInit(e.getJSON("subtitle")!!)
    }
    if (e["extra"] != null) {
        opts.extra = extraInit(e.getJSON("extra")!!)
    }
    return opts
}
fun updateOptions(_opts: ChartOptions, e: UTSJSONObject): ChartOptions {
    val opts = _opts
    if (e["type"] != null) {
        opts.type = e.getString("type")!!
    }
    if (opts.type == "gauge") {
        if (e["categories"] != null) {
            val categoriesObj: UTSArray<ChartOptionsSeriesData> = _uA()
            val cate: UTSArray<UTSJSONObject> = e.getArray("categories") as UTSArray<UTSJSONObject>
            cate.forEach(fun(item: UTSJSONObject){
                val child = seriesDataInit(item)
                categoriesObj.push(child)
            }
            )
            opts.categoriesObj = categoriesObj
        }
    } else {
        if (e["categories"] != null) {
            opts.categories = e.getArray("categories") as UTSArray<String>
        }
    }
    if (e["width"] != null) {
        opts.width = e.getNumber("width")!!
    }
    if (e["height"] != null) {
        opts.height = e.getNumber("height")!!
    }
    if (e["canvas2d"] != null) {
        opts.canvas2d = e.getBoolean("canvas2d")!!
    }
    if (e["pixelRatio"] != null) {
        opts.pixelRatio = e.getNumber("pixelRatio")!!
    }
    if (e["animation"] != null) {
        opts.animation = e.getBoolean("animation")!!
    }
    if (e["timing"] != null) {
        opts.timing = e.getString("timing")!!
    }
    if (e["duration"] != null) {
        opts.duration = e.getNumber("duration")!!
    }
    if (e["rotate"] != null) {
        opts.rotate = e.getBoolean("rotate")!!
    }
    if (e["rotateLock"] != null) {
        opts.rotateLock = e.getBoolean("rotateLock")!!
    }
    if (e["background"] != null) {
        opts.background = e.getString("background")!!
    }
    if (e["color"] != null) {
        opts.color = e.getArray("color") as UTSArray<String>
    }
    if (e["padding"] != null) {
        opts.padding = e.getArray("padding") as UTSArray<Number>
    }
    if (e["fontSize"] != null) {
        opts.fontSize = e.getNumber("fontSize")!!
    }
    if (e["fontColor"] != null) {
        opts.fontColor = e.getString("fontColor")!!
    }
    if (e["dataLabel"] != null) {
        opts.dataLabel = e.getBoolean("dataLabel")!!
    }
    if (e["dataPointShape"] != null) {
        opts.dataPointShape = e.getBoolean("dataPointShape")!!
    }
    if (e["dataPointShapeType"] != null) {
        opts.dataPointShapeType = e.getString("dataPointShapeType")!!
    }
    if (e["touchMoveLimit"] != null) {
        opts.touchMoveLimit = e.getNumber("touchMoveLimit")!!
    }
    if (e["enableScroll"] != null) {
        opts.enableScroll = e.getBoolean("enableScroll")!!
    }
    if (e["enableMarkLine"] != null) {
        opts.enableMarkLine = e.getBoolean("enableMarkLine")!!
    }
    if (e["scrollPosition"] != null) {
        opts.scrollPosition = e.getString("scrollPosition")!!
    }
    if (e["xOffset"] != null) {
        opts.xOffset = e.getNumber("xOffset")!!
    }
    if (e["yOffset"] != null) {
        opts.yOffset = e.getNumber("yOffset")!!
    }
    if (e["tapLegend"] != null) {
        opts.dataLabel = e.getBoolean("tapLegend")!!
    }
    opts.legend.fontSize = opts.fontSize
    opts.legend.lineHeight = opts.fontSize
    opts.legend.fontColor = opts.fontColor
    opts.yAxis.dashLength = 4 * opts.pixelRatio
    if (e["series"] != null) {
        val series: UTSArray<ChartOptionsSeries> = _uA()
        val series_data = e.getArray("series") as UTSArray<UTSJSONObject>
        series_data.forEach(fun(item: UTSJSONObject){
            series.push(seriesInit(item, opts.type))
        }
        )
        opts.series = series
    }
    if (e["xAxis"] != null) {
        opts.xAxis = xAxisInit(e.getJSON("xAxis")!!)
    }
    if (e["yAxis"] != null) {
        opts.yAxis = yAxisInit(e.getJSON("yAxis")!!)
    }
    if (e["legend"] != null) {
        opts.legend = legendInit(e.getJSON("legend")!!)
    }
    if (e["title"] != null) {
        opts.title = titleInit(e.getJSON("title")!!)
    }
    if (e["subtitle"] != null) {
        opts.subtitle = subtitleInit(e.getJSON("subtitle")!!)
    }
    if (e["extra"] != null) {
        opts.extra = extraInit(e.getJSON("extra")!!)
    }
    return opts
}
open class TuiDrawCharts : TuiChartsInterface, IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("TuiDrawCharts", "uni_modules/tui-xechars/libs/xcharts.uts", 8, 14)
    }
    override var opts: ChartOptions
    override var context: CanvasRenderingContext2D
    override var uevent: TuiChartsEvent = TuiChartsEvent()
    override var config: TuiChartConfig
    override var scrollOption = ChartScrollOption()
    override var animationInstance: Animation? = null
    override var offsetWidth: Number
    override var offsetHeight: Number
    constructor(option: UTSJSONObject){
        this.context = option["context"] as CanvasRenderingContext2D
        val _opts = optionsInit(option)
        val conf = TuiChartConfig()
        this.offsetWidth = this.context.canvas.offsetWidth
        this.offsetHeight = this.context.canvas.offsetHeight
        conf.fontSize = _opts.fontSize * _opts.pixelRatio
        if (_opts.color != null) {
            conf.color = _opts.color!!
        }
        if (_opts.type == "pie") {
            if (_opts.dataLabel == false) {
                conf.pieChartLinePadding = 0
            } else {
                conf.pieChartLinePadding = _opts.extra.pie!!.labelWidth * _opts.pixelRatio
            }
        }
        if (_opts.type == "ring") {
            if (_opts.dataLabel == false) {
                conf.pieChartLinePadding = 0
            } else {
                conf.pieChartLinePadding = _opts.extra.ring!!.labelWidth * _opts.pixelRatio
            }
        }
        if (_opts.type == "rose") {
            if (_opts.dataLabel == false) {
                conf.pieChartLinePadding = 0
            } else {
                conf.pieChartLinePadding = _opts.extra.rose!!.labelWidth * _opts.pixelRatio
            }
        }
        if (_opts.dataLabel == false) {
            conf.pieChartTextPadding = 0
        } else {
            conf.pieChartTextPadding = conf.pieChartTextPadding * _opts.pixelRatio
        }
        conf.rotate = _opts.rotate
        this.config = conf
        this.opts = _opts
    }
    open fun setCoordinates(e: UTSJSONObject) {
        if (e.getNumber("height") != null) {
            this.opts.height = e.getNumber("height")!!
        }
        if (e.getNumber("width") != null) {
            this.opts.width = e.getNumber("width")!!
        }
        if (e.getNumber("yOffset") != null) {
            this.opts.yOffset = e.getNumber("yOffset")!!
        }
        if (e.getNumber("xOffset") != null) {
            this.opts.xOffset = e.getNumber("xOffset")!!
        }
    }
    open fun draw() {
        drawCharts(this, this.opts.type, this.opts, this.config, this.context)
    }
    open fun clearTooltip() {
        if (this.opts.tooltip != null) {
            this.opts.animation = false
            this.opts.tooltip = null
            this.draw()
        }
    }
    override fun update(data: UTSJSONObject) {
        this.opts = updateOptions(this.opts, data)
        this.opts.updateData = true
        this.opts.animation = true
        this.opts.tooltip = null
        var scrollPosition = data.get("scrollPosition") ?: "current"
        when (scrollPosition) {
            "current" -> 
                this.opts._scrollDistance_ = this.scrollOption.currentOffset
            "left" -> 
                {
                    this.opts._scrollDistance_ = 0
                    this.scrollOption.currentOffset = 0
                    this.scrollOption.startTouchX = 0
                    this.scrollOption.distance = 0
                    this.scrollOption.lastMoveTime = 0
                }
            "right" -> 
                {
                    var _calYAxisData = calYAxisData(this.opts.series!!, this.opts, this.config)
                    var yAxisWidth = _calYAxisData.yAxisWidth!!
                    this.config.yAxisWidth = yAxisWidth
                    var offsetLeft: Number = 0
                    var _getXAxisPoints0 = getXAxisPoints(this.opts.categories!!, this.opts, this.config)
                    var xAxisPoints = _getXAxisPoints0.xAxisPoints
                    var startX = _getXAxisPoints0.startX!!
                    var endX = _getXAxisPoints0.endX!!
                    var eachSpacing = _getXAxisPoints0.eachSpacing!!
                    var totalWidth = eachSpacing * (xAxisPoints.length - 1)
                    var screenWidth = endX - startX
                    offsetLeft = screenWidth - totalWidth
                    this.scrollOption.currentOffset = offsetLeft
                    this.scrollOption.startTouchX = offsetLeft
                    this.scrollOption.distance = 0
                    this.scrollOption.lastMoveTime = 0
                    this.opts._scrollDistance_ = offsetLeft
                }
        }
        drawCharts(this, this.opts.type, this.opts, this.config, this.context)
    }
    override fun zoom(e: ChartOptionsXAxis?) {
        var kVal: ChartOptionsXAxis = e ?: this.opts.xAxis
        if (this.opts.enableScroll != true) {
            console.log("[uCharts] 请启用滚动条后使用", " at uni_modules/tui-xechars/libs/xcharts.uts:116")
            return
        }
        var centerPoint = Math.round(Math.abs(this.scrollOption.currentOffset) / this.opts.chartData.eachSpacing) + Math.round(this.opts.xAxis.itemCount / 2)
        this.opts.animation = false
        this.opts.xAxis.itemCount = kVal.itemCount
        var _calYAxisData = calYAxisData(this.opts.series!!, this.opts, this.config)
        var yAxisWidth = _calYAxisData.yAxisWidth!!
        this.config.yAxisWidth = yAxisWidth
        var offsetLeft: Number = 0
        var _getXAxisPoints0 = getXAxisPoints(this.opts.categories!!, this.opts, this.config)
        var xAxisPoints = _getXAxisPoints0.xAxisPoints
        var startX = _getXAxisPoints0.startX!!
        var endX = _getXAxisPoints0.endX!!
        var eachSpacing = _getXAxisPoints0.eachSpacing!!
        var centerLeft = eachSpacing * centerPoint
        var screenWidth = endX - startX
        var MaxLeft = screenWidth - eachSpacing * (xAxisPoints.length - 1)
        offsetLeft = screenWidth / 2 - centerLeft
        if (offsetLeft > 0) {
            offsetLeft = 0
        }
        if (offsetLeft < MaxLeft) {
            offsetLeft = MaxLeft
        }
        this.scrollOption.currentOffset = offsetLeft
        this.scrollOption.startTouchX = 0
        this.scrollOption.distance = 0
        this.scrollOption.lastMoveTime = 0
        calValidDistance(this, offsetLeft, this.opts.chartData, this.opts)
        this.opts._scrollDistance_ = offsetLeft
        drawCharts(this, this.opts.type, this.opts, this.config, this.context)
    }
    override fun dobuleZoom(e: UniTouchEvent) {}
    override fun getLegendDataIndex(e: UniEvent): Number {
        var touches = getTouches(this.opts, e)
        touches.x = touches.x - this.opts.xOffset
        touches.y = touches.y - this.opts.yOffset
        return findLegendIndex(touches, this.opts.chartData.legendData)
    }
    override fun touchLegend(e: UniEvent): Number {
        var index = this.getLegendDataIndex(e)
        if (index >= 0) {
            this.clearTooltip()
            if (this.opts.type == "candle") {
                this.opts.seriesMA!![index].show = !this.opts.seriesMA!![index].show
            } else {
                this.opts.series!![index].show = !this.opts.series!![index].show
            }
            this.opts.animation = true
            this.opts._scrollDistance_ = this.scrollOption.currentOffset
            drawCharts(this, this.opts.type, this.opts, this.config, this.context)
        }
        return index
    }
    override fun scrollStart(e: UniEvent) {
        var touches = getTouches(this.opts, e)
        if (this.opts.enableScroll == true) {
            this.scrollOption.startTouchX = touches.x
        }
    }
    override fun scroll(e: UniEvent) {
        this.opts.showTooltip = false
        if (this.scrollOption.lastMoveTime == 0) {
            this.scrollOption.lastMoveTime = Date.now()
        }
        var Limit = this.opts.touchMoveLimit
        var currMoveTime = Date.now()
        var duration = currMoveTime - this.scrollOption.lastMoveTime
        if (duration < Math.floor((1000 as Number) / Limit)) {
            return
        }
        if (this.scrollOption.startTouchX == 0) {
            return
        }
        this.scrollOption.lastMoveTime = currMoveTime
        if (this.opts.enableScroll == true) {
            var touches = getTouches(this.opts, e)
            var _distance = touches.x - this.scrollOption.startTouchX
            var currentOffset = this.scrollOption.currentOffset
            var validDistance = calValidDistance(this, currentOffset + _distance, this.opts.chartData, this.opts)
            _distance = validDistance - currentOffset
            this.scrollOption.distance = _distance
            this.opts._scrollDistance_ = currentOffset + _distance
            this.opts.animation = false
            drawCharts(this, this.opts.type, this.opts, this.config, this.context)
        }
    }
    override fun scrollEnd(e: UniEvent) {
        if (this.opts.enableScroll == true) {
            var _scrollOption = this.scrollOption
            var currentOffset = _scrollOption.currentOffset
            var distance = _scrollOption.distance
            this.scrollOption.currentOffset = currentOffset + distance
            this.scrollOption.distance = 0
            this.scrollOption.moveCount = 0
        }
    }
    override fun showToolTip(vararg spreadArgs: Any): Number {
        var args = UTSArray(*spreadArgs)
        var tipsIndex: Number = -1
        val opts: ChartOptions = this.opts
        if (opts.extra.tooltip != null) {
            this.opts.showTooltip = true
            val e = args[0] as UniEvent
            val option: ChartOptionsTooltipOption = if (args.length > 1 && args[1] != null) {
                args[1] as ChartOptionsTooltipOption
            } else {
                ChartOptionsTooltipOption()
            }
            val `_touches$`: TouchPosition = getTouches(this.opts, e)
            val currentOffset: Number = this.scrollOption.currentOffset
            opts._scrollDistance_ = currentOffset
            opts.animation = false
            if (this.opts.type == "line" || this.opts.type == "area" || this.opts.type == "column" || this.opts.type == "scatter" || this.opts.type == "bubble") {
                var current: ChartOptionsTooltip = this.getCurrentDataIndex(e)
                var index: Any? = if (option.index == null) {
                    current.index
                } else {
                    option.index
                }
                if (index != null && (index as Number) > -1) {
                    tipsIndex = index as Number
                    var seriesData = getSeriesDataItem(this.opts.series!!, index, current.group)
                    if (seriesData.length != 0) {
                        val _getToolTipData: ChartOptionsTooltipOption = getToolTipData(seriesData, this.opts, index, current.group, this.opts.categories!!, option)
                        val textList: UTSArray<TextList> = _getToolTipData.textList!!
                        val offset: Offset = _getToolTipData.offset!!
                        offset.y = `_touches$`.y
                        opts.tooltip = ChartOptionsTooltip()
                        opts.tooltip!!.textList = if (option.textList != null) {
                            option.textList!!
                        } else {
                            textList
                        }
                        opts.tooltip!!.offset = if (option.offset != null) {
                            option.offset!!
                        } else {
                            offset
                        }
                        opts.tooltip!!.option = option
                        opts.tooltip!!.index = index
                        opts.tooltip!!.group = current.group
                    }
                } else {
                    this.clearTooltip()
                }
                drawCharts(this, opts.type, opts, this.config, this.context)
            }
            if (this.opts.type == "mount") {
                var index: Any? = if (option.index == null) {
                    this.getCurrentDataIndex(e).index
                } else {
                    option.index
                }
                if (index != null) {
                    tipsIndex = index as Number
                    if (index as Number > -1) {
                        this.opts.animation = false
                        var opts = this.opts
                        var seriesData = opts._series_!![index as Number]
                        val textlist: TextList = TextList()
                        if (option.formatter != null) {
                            val kFun = option.formatter as (seriesData: ChartOptionsSeries, index: Number, opts: ChartOptions) -> String
                            textlist.text = kFun(seriesData, index, opts)
                        } else {
                            textlist.text = seriesData.name + ": " + seriesData.data!![0].value
                        }
                        textlist.color = seriesData.color!!
                        textlist.legendShape = if (this.opts.extra.tooltip!!.legendShape == "auto") {
                            seriesData.legendShape!!
                        } else {
                            this.opts.extra.tooltip!!.legendShape
                        }
                        val textList = _uA(
                            textlist
                        ) as UTSArray<TextList>
                        val offset = Offset()
                        offset.x = opts.chartData.calPoints!![0][index].x
                        offset.y = `_touches$`.y
                        opts.tooltip = ChartOptionsTooltip()
                        opts.tooltip!!.textList = if (option.textList != null) {
                            option.textList!!
                        } else {
                            textList
                        }
                        opts.tooltip!!.offset = if (option.offset != null) {
                            option.offset!!
                        } else {
                            offset
                        }
                        opts.tooltip!!.option = option
                        opts.tooltip!!.index = index
                    }
                } else {
                    this.clearTooltip()
                }
                drawCharts(this, opts.type, opts, this.config, this.context)
            }
            if (this.opts.type == "bar") {
                var current = this.getCurrentDataIndex(e)
                var index: Any? = if (option.index == null) {
                    current.index
                } else {
                    option.index
                }
                if (index != null) {
                    tipsIndex = index as Number
                    if (index as Number > -1) {
                        var seriesData = getSeriesDataItem(this.opts.series!!, index, current.group)
                        if (seriesData.length != 0) {
                            var _getToolTipData = getToolTipData(seriesData, this.opts, index, current.group, this.opts.categories!!, option)
                            var textList = _getToolTipData.textList!!
                            var offset = _getToolTipData.offset!!
                            offset.x = `_touches$`.x
                            offset.y = offset.y + this.opts.yOffset
                            opts.tooltip = ChartOptionsTooltip()
                            opts.tooltip!!.textList = if (option.textList != null) {
                                option.textList!!
                            } else {
                                textList
                            }
                            opts.tooltip!!.offset = if (option.offset != null) {
                                option.offset!!
                            } else {
                                offset
                            }
                            opts.tooltip!!.option = option
                            opts.tooltip!!.index = index
                        }
                    }
                } else {
                    this.clearTooltip()
                }
                drawCharts(this, opts.type, opts, this.config, this.context)
            }
            if (this.opts.type == "mix") {
                var current = this.getCurrentDataIndex(e)
                var index: Any? = if (option.index == null) {
                    current.index
                } else {
                    option.index
                }
                if (index != null) {
                    tipsIndex = index as Number
                    if ((index as Number) > -1) {
                        var currentOffset = this.scrollOption.currentOffset
                        var opts = this.opts
                        opts._scrollDistance_ = currentOffset
                        opts.animation = false
                        var seriesData = getSeriesDataItem(this.opts.series!!, index, current.group)
                        if (seriesData.length != 0) {
                            var _getMixToolTipData = getMixToolTipData(seriesData, this.opts, index, this.opts.categories!!, option)
                            var textList = _getMixToolTipData.textList!!
                            var offset = _getMixToolTipData.offset!!
                            offset.y = `_touches$`.y
                            opts.tooltip = ChartOptionsTooltip()
                            opts.tooltip!!.textList = if (option.textList != null) {
                                option.textList!!
                            } else {
                                textList
                            }
                            opts.tooltip!!.offset = if (option.offset != null) {
                                option.offset!!
                            } else {
                                offset
                            }
                            opts.tooltip!!.option = option
                            opts.tooltip!!.index = index
                        }
                    }
                } else {
                    this.clearTooltip()
                }
                drawCharts(this, opts.type, opts, this.config, this.context)
            }
            if (this.opts.type == "candle") {
                var current = this.getCurrentDataIndex(e)
                var index: Any? = if (option.index == null) {
                    current.index
                } else {
                    option.index
                }
                if (index != null) {
                    tipsIndex = index as Number
                    if (index as Number > -1) {
                        var currentOffset = this.scrollOption.currentOffset
                        var opts = this.opts
                        opts._scrollDistance_ = currentOffset
                        opts.animation = false
                        var seriesData = getSeriesDataItem(this.opts.series!!, index, current.group)
                        if (seriesData.length != 0) {
                            var _getToolTipData = getCandleToolTipData(this.opts.series!![0].dataArr, seriesData, this.opts, index, this.opts.categories!!, this.opts.extra.candle!!, option)
                            var textList = _getToolTipData.textList!!
                            var offset = _getToolTipData.offset!!
                            offset.y = `_touches$`.y
                            opts.tooltip = ChartOptionsTooltip()
                            opts.tooltip!!.textList = option.textList ?: textList
                            opts.tooltip!!.offset = option.offset ?: offset
                            opts.tooltip!!.option = option
                            opts.tooltip!!.index = index
                        }
                    }
                } else {
                    this.clearTooltip()
                }
                drawCharts(this, opts.type, opts, this.config, this.context)
            }
            if (this.opts.type == "pie" || this.opts.type == "ring" || this.opts.type == "rose" || this.opts.type == "funnel") {
                var index: Any? = if (option.index == null) {
                    this.getCurrentDataIndex(e).index!!
                } else {
                    option.index
                }
                if (index != null) {
                    tipsIndex = index as Number
                    if (index as Number > -1) {
                        var opts = this.opts
                        opts.animation = false
                        var seriesData: ChartOptionsSeries = opts._series_!![index as Number]
                        val textlist: TextList = TextList()
                        if (option.formatter != null) {
                            val kFun = option.formatter as (seriesData: ChartOptionsSeries, index: Any, opts: ChartOptions) -> String
                            textlist.text = kFun(seriesData, index, opts)
                        } else {
                            textlist.text = seriesData.name + ": " + seriesData.data!![0].value
                        }
                        textlist.color = seriesData.color!!
                        textlist.legendShape = if (this.opts.extra.tooltip!!.legendShape == "auto") {
                            seriesData.legendShape!!
                        } else {
                            this.opts.extra.tooltip!!.legendShape
                        }
                        val textList = _uA(
                            textlist
                        ) as UTSArray<TextList>
                        var offset = Offset()
                        offset.x = `_touches$`.x
                        offset.y = `_touches$`.y
                        opts.tooltip = ChartOptionsTooltip()
                        opts.tooltip!!.textList = if (option.textList != null) {
                            option.textList!!
                        } else {
                            textList
                        }
                        opts.tooltip!!.offset = if (option.offset != null) {
                            option.offset!!
                        } else {
                            offset
                        }
                        opts.tooltip!!.option = option
                        opts.tooltip!!.index = index
                    } else {
                        this.clearTooltip()
                    }
                    drawCharts(this, opts.type, opts, this.config, this.context)
                }
            }
            if (this.opts.type == "map") {
                var index: Any? = if (option.index == null) {
                    this.getCurrentDataIndex(e).index
                } else {
                    option.index
                }
                if (index != null) {
                    tipsIndex = index as Number
                    if (index as Number > -1) {
                        var opts = this.opts
                        opts.animation = false
                        var seriesData = this.opts.series!![index]
                        seriesData.name = seriesData.properties.name
                        val textList: UTSArray<TextList> = _uA()
                        val text = TextList()
                        if (option.formatter != null) {
                            val kFun = option.formatter as (item: ChartOptionsSeries, index: Number, opts: ChartOptions) -> String
                            text.text = kFun(seriesData, index, this.opts)
                        } else {
                            text.text = seriesData.name!!
                        }
                        text.color = seriesData.color!!
                        text.legendShape = if (this.opts.extra.tooltip!!.legendShape == "auto") {
                            seriesData.legendShape!!
                        } else {
                            this.opts.extra.tooltip!!.legendShape
                        }
                        textList.push(text)
                        val offset = Offset()
                        offset.x = `_touches$`.x - this.opts.xOffset
                        offset.y = `_touches$`.y - this.opts.yOffset
                        opts.tooltip = ChartOptionsTooltip()
                        opts.tooltip!!.textList = if (option.textList != null) {
                            option.textList!!
                        } else {
                            textList
                        }
                        opts.tooltip!!.offset = if (option.offset != null) {
                            option.offset!!
                        } else {
                            offset
                        }
                        opts.tooltip!!.option = option
                        opts.tooltip!!.index = index
                    } else {
                        this.clearTooltip()
                    }
                    opts.updateData = false
                    drawCharts(this, opts.type, opts, this.config, this.context)
                }
            }
            if (this.opts.type == "word") {
                var index: Any? = if (option.index == null) {
                    this.getCurrentDataIndex(e).index
                } else {
                    option.index
                }
                if (index != null) {
                    tipsIndex = index as Number
                    if (index as Number > -1) {
                        var opts = this.opts
                        opts.animation = false
                        var seriesData = this.opts.series!![index as Number]
                        val textlist: TextList = TextList()
                        if (option.formatter != null) {
                            val kFun = option.formatter as (seriesData: ChartOptionsSeries, index: Any, opts: ChartOptions) -> String
                            textlist.text = kFun(seriesData, index, opts)
                        } else {
                            textlist.text = seriesData.name!!
                        }
                        textlist.color = seriesData.color!!
                        textlist.legendShape = if (this.opts.extra.tooltip!!.legendShape == "auto") {
                            seriesData.legendShape!!
                        } else {
                            this.opts.extra.tooltip!!.legendShape
                        }
                        val textList = _uA(
                            textlist
                        ) as UTSArray<TextList>
                        var offset = Offset()
                        offset.x = `_touches$`.x
                        offset.y = `_touches$`.y
                        opts.tooltip = ChartOptionsTooltip()
                        opts.tooltip!!.textList = if (option.textList != null) {
                            option.textList!!
                        } else {
                            textList
                        }
                        opts.tooltip!!.offset = if (option.offset != null) {
                            option.offset!!
                        } else {
                            offset
                        }
                        opts.tooltip!!.option = option
                        opts.tooltip!!.index = index
                    } else {
                        this.clearTooltip()
                    }
                    opts.updateData = false
                    drawCharts(this, opts.type, opts, this.config, this.context)
                }
            }
            if (this.opts.type == "radar") {
                val current = this.getCurrentDataIndex(e)
                var index = if (option.index == null) {
                    current.index
                } else {
                    option.index!!
                }
                if (index != null) {
                    tipsIndex = index as Number
                    if ((index as Number) > -1) {
                        var opts = this.opts
                        opts.animation = false
                        var seriesData = getSeriesDataItem(this.opts.series!!, index, current.group)
                        if (seriesData.length != 0) {
                            var textList: UTSArray<TextList> = seriesData.map(fun(item: ChartOptionsSeries): TextList {
                                val list = TextList()
                                if (option.formatter != null) {
                                    val kFun = option.formatter as (item: ChartOptionsSeries, categories: String, index: Number, opts: ChartOptions) -> String
                                    list.text = kFun(item, this.opts.categories!![index], index, this.opts)
                                } else {
                                    list.text = item.name + ": " + item.data!![0].value!!
                                }
                                list.color = item.color!!
                                list.legendShape = if (this.opts.extra.tooltip!!.legendShape == "auto") {
                                    item.legendShape!!
                                } else {
                                    this.opts.extra.tooltip!!.legendShape
                                }
                                return list
                            }
                            )
                            var offset = Offset()
                            offset.x = `_touches$`.x
                            offset.y = `_touches$`.y
                            opts.tooltip = ChartOptionsTooltip()
                            opts.tooltip!!.textList = if (option.textList != null) {
                                option.textList!!
                            } else {
                                textList
                            }
                            opts.tooltip!!.offset = if (option.offset != null) {
                                option.offset!!
                            } else {
                                offset
                            }
                            opts.tooltip!!.option = option
                            opts.tooltip!!.index = index
                        }
                    } else {
                        this.clearTooltip()
                    }
                    drawCharts(this, opts.type, opts, this.config, this.context)
                }
            }
        }
        return tipsIndex
    }
    override fun getCurrentDataIndex(e: UniEvent): ChartOptionsTooltip {
        var tooltip: ChartOptionsTooltip = ChartOptionsTooltip()
        var `_touches$`: TouchPosition = getTouches(this.opts, e)
        `_touches$`.x = `_touches$`.x - this.opts.xOffset
        `_touches$`.y = `_touches$`.y - this.opts.yOffset
        if (this.opts.type == "pie" || this.opts.type == "ring") {
            return findPieChartCurrentIndex(`_touches$`, this.opts.chartData.pieData!!, this.opts)
        } else if (this.opts.type == "rose") {
            return findRoseChartCurrentIndex(`_touches$`, this.opts.chartData.pieData!!, this.opts)
        } else if (this.opts.type == "radar") {
            return findRadarChartCurrentIndex(`_touches$`, this.opts.chartData.radarData!!, this.opts.categories!!.length)
        } else if (this.opts.type == "funnel") {
            `_touches$`.x = `_touches$`.x + this.opts.xOffset
            `_touches$`.y = `_touches$`.y + this.opts.yOffset
            return findFunnelChartCurrentIndex(`_touches$`, this.opts.chartData.funnelData!!)
        } else if (this.opts.type == "map") {
            return findMapChartCurrentIndex(`_touches$`, this.opts)
        } else if (this.opts.type == "word") {
            return findWordChartCurrentIndex(`_touches$`, this.opts.chartData.wordCloudData!!)
        } else if (this.opts.type == "bar") {
            tooltip = findBarChartCurrentIndex(`_touches$`, this.opts.chartData.calPoints, this.opts, Math.abs(this.scrollOption.currentOffset))
        } else if (this.opts.type == "candle") {
            tooltip = findCandleChartCurrentIndex(`_touches$`, this.opts.chartData.calMapPoints!!, this.opts, Math.abs(this.scrollOption.currentOffset))
        } else {
            tooltip = findCurrentIndex(`_touches$`, this.opts.chartData.calPoints!!, this.opts, Math.abs(this.scrollOption.currentOffset))
        }
        return tooltip
    }
}
open class TuiCharts : IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("TuiCharts", "uni_modules/tui-xechars/libs/charts.uts", 2, 14)
    }
    open var chartsMap = Map<Number, TuiDrawCharts>()
    open var option = UTSJSONObject()
    open var canvasContext: CanvasContext? = null
    open var context: CanvasRenderingContext2D? = null
    open var dpr: Number = uni_getWindowInfo().pixelRatio
    open var offsetWidth: Number = 0
    open var offsetHeight: Number = 0
    open var ins: ComponentPublicInstance? = null
    open var setSize: Boolean = false
    open var baseX: Number = 0
    open var baseY: Number = 0
    open var oldTipsName: Number = -1
    open var canvasRect: DOMRect? = null
    constructor(){}
    open fun getTouchCurrent(e: UniEvent): Number {
        var curArea: Number = -1
        var x: Number
        var y: Number
        val top = this.canvasRect!!.y
        val left = this.canvasRect!!.x
        if (e.type == "click") {
            x = (e as UniPointerEvent).clientX - this.baseX + (this.baseX - left)
            y = (e as UniPointerEvent).clientY - this.baseY + (this.baseY - top)
        } else {
            x = (e as UniTouchEvent).changedTouches[0].clientX - this.baseX + (this.baseX - left)
            y = (e as UniTouchEvent).changedTouches[0].clientY - this.baseY + (this.baseY - top)
        }
        this.chartsMap.forEach(fun(item: TuiDrawCharts, key: Number){
            if (x > item.opts.xOffset && y > item.opts.yOffset && x < item.opts.xOffset + item.opts.width && y < item.opts.yOffset + item.opts.height) {
                curArea = key
            }
        }
        )
        return curArea
    }
    open fun tap(e: UniPointerEvent) {
        if (this.oldTipsName != -1) {
            val curDraw = this.getDrawCharts(this.oldTipsName)!!
            curDraw.opts.DOMRect = this.canvasRect
            val tipsIndex = curDraw.showToolTip(e)
            var legendIndex: Number = -1
            if (curDraw.opts.tapLegend) {
                legendIndex = curDraw.touchLegend(e)
            }
            this.ins?.`$emit`("select", _uO("tipsIndex" to tipsIndex, "legendIndex" to legendIndex, "index" to this.oldTipsName))
        }
    }
    open fun touchStart(e: UniTouchEvent) {
        e.target!!.getBoundingClientRectAsync()!!.then(fun(res: DOMRect){
            this.canvasRect = res
            val curBar = this.getTouchCurrent(e)
            if (curBar != -1) {
                if (curBar != this.oldTipsName && this.oldTipsName != -1) {
                    val oldDraw = this.getDrawCharts(this.oldTipsName)!!
                    oldDraw.opts.DOMRect = null
                    oldDraw.clearTooltip()
                }
                this.oldTipsName = curBar
                val curDraw = this.getDrawCharts(this.oldTipsName)!!
                if (curDraw.opts.enableScroll) {
                    curDraw.opts.DOMRect = res
                    curDraw.scrollStart(e)
                }
            }
        }
        )
    }
    open fun touchMove(e: UniTouchEvent) {
        if (this.oldTipsName != -1) {
            val curDraw = this.getDrawCharts(this.oldTipsName)!!
            if (curDraw.opts.enableScroll) {
                curDraw.scroll(e)
            }
        }
    }
    open fun touchEnd(e: UniTouchEvent) {
        if (this.oldTipsName != -1) {
            val curDraw = this.getDrawCharts(this.oldTipsName)!!
            if (curDraw.opts.enableScroll) {
                curDraw.scrollEnd(e)
            }
        }
    }
    open fun setInsMethod(e: ComponentPublicInstance, rect: DOMRect) {
        this.ins = e
        this.baseX = rect.x
        this.baseY = rect.y
    }
    open fun setContext(context: CanvasContext) {
        this.canvasContext = context
        val ctx = context.getContext("2d")!!
        this.context = ctx
        val canvas = ctx.canvas
        this.offsetWidth = canvas.offsetWidth
        this.offsetHeight = canvas.offsetHeight
    }
    open fun getDrawCharts(name: Number): TuiDrawCharts? {
        return this.chartsMap.get(name)
    }
    open fun add(name: Number, option: UTSJSONObject): TuiDrawCharts {
        val ctx = this.context!!
        option.set("context", ctx)
        if (option.getNumber("width") == null) {
            option.set("width", ctx.canvas.offsetWidth)
        }
        if (option.getNumber("height") == null) {
            option.set("height", ctx.canvas.offsetHeight)
        }
        option.set("pixelRatio", 1)
        option.set("canvas2d", true)
        option.set("canvasContext", this.canvasContext)
        if (option.getArray("padding") == null) {
            option.set("padding", _uA(
                0,
                0,
                0,
                0
            ))
        }
        if (option.getArray("series") == null) {
            option.set("series", _uA<UTSJSONObject>())
        }
        val charts = TuiDrawCharts(option)
        this.chartsMap.set(name, charts)
        this.option = option
        return charts
    }
}
val GenUniModulesTuiXecharsComponentsTuiXecharsTuiXecharsClass = CreateVueComponent(GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "component", name = "", inheritAttrs = GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars.inheritAttrs, inject = GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars.inject, props = GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars.props, propsNeedCastKeys = GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars.propsNeedCastKeys, emits = GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars.emits, components = GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars.components, styles = GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars.setup(props as GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars)
    }
    )
}
, fun(instance, renderer): GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars {
    return GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars(instance)
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
val getBaseUrl = fun(): String {
    return "http://192.168.76.128:8081"
    return "http://192.168.0.18:8081"
}
val apiConfig = reactive(ApiConfig(baseUrl = getBaseUrl()))
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
    open var reviewedCount: Number,
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
    constructor(__v_raw: StatisticsData, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(pendingIssues = __v_raw.pendingIssues, completedIssues = __v_raw.completedIssues, reviewedCount = __v_raw.reviewedCount) {
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
    override var reviewedCount: Number
        get() {
            return _tRG(__v_raw, "reviewedCount", __v_raw.reviewedCount, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("reviewedCount")) {
                return
            }
            val oldValue = __v_raw.reviewedCount
            __v_raw.reviewedCount = value
            _tRS(__v_raw, "reviewedCount", oldValue, value)
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
        return UTSSourceMapPosition("MonthlyRecord", "pages/monthly/monthly.uvue", 111, 7)
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
    open var reviewedCount: Number,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("MonthlyStatistics", "pages/monthly/monthly.uvue", 119, 7)
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
    constructor(__v_raw: MonthlyStatistics, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(monthlyPending = __v_raw.monthlyPending, monthlyCompleted = __v_raw.monthlyCompleted, reviewedCount = __v_raw.reviewedCount) {
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
    override var reviewedCount: Number
        get() {
            return _tRG(__v_raw, "reviewedCount", __v_raw.reviewedCount, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("reviewedCount")) {
                return
            }
            val oldValue = __v_raw.reviewedCount
            __v_raw.reviewedCount = value
            _tRS(__v_raw, "reviewedCount", oldValue, value)
        }
}
open class Notification (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var taskId: String,
    @JsonNotNull
    open var taskNo: String,
    @JsonNotNull
    open var title: String,
    @JsonNotNull
    open var content: String,
    @JsonNotNull
    open var createTime: String,
    @JsonNotNull
    open var isRead: Boolean = false,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("Notification", "pages/monthly/monthly.uvue", 125, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return NotificationReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class NotificationReactiveObject : Notification, IUTSReactive<Notification> {
    override var __v_raw: Notification
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: Notification, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, taskId = __v_raw.taskId, taskNo = __v_raw.taskNo, title = __v_raw.title, content = __v_raw.content, createTime = __v_raw.createTime, isRead = __v_raw.isRead) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): NotificationReactiveObject {
        return NotificationReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
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
    override var taskId: String
        get() {
            return _tRG(__v_raw, "taskId", __v_raw.taskId, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("taskId")) {
                return
            }
            val oldValue = __v_raw.taskId
            __v_raw.taskId = value
            _tRS(__v_raw, "taskId", oldValue, value)
        }
    override var taskNo: String
        get() {
            return _tRG(__v_raw, "taskNo", __v_raw.taskNo, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("taskNo")) {
                return
            }
            val oldValue = __v_raw.taskNo
            __v_raw.taskNo = value
            _tRS(__v_raw, "taskNo", oldValue, value)
        }
    override var title: String
        get() {
            return _tRG(__v_raw, "title", __v_raw.title, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("title")) {
                return
            }
            val oldValue = __v_raw.title
            __v_raw.title = value
            _tRS(__v_raw, "title", oldValue, value)
        }
    override var content: String
        get() {
            return _tRG(__v_raw, "content", __v_raw.content, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("content")) {
                return
            }
            val oldValue = __v_raw.content
            __v_raw.content = value
            _tRS(__v_raw, "content", oldValue, value)
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
    override var isRead: Boolean
        get() {
            return _tRG(__v_raw, "isRead", __v_raw.isRead, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("isRead")) {
                return
            }
            val oldValue = __v_raw.isRead
            __v_raw.isRead = value
            _tRS(__v_raw, "isRead", oldValue, value)
        }
}
val GenPagesMonthlyMonthlyClass = CreateVueComponent(GenPagesMonthlyMonthly::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesMonthlyMonthly.inheritAttrs, inject = GenPagesMonthlyMonthly.inject, props = GenPagesMonthlyMonthly.props, propsNeedCastKeys = GenPagesMonthlyMonthly.propsNeedCastKeys, emits = GenPagesMonthlyMonthly.emits, components = GenPagesMonthlyMonthly.components, styles = GenPagesMonthlyMonthly.styles)
}
, fun(instance, renderer): GenPagesMonthlyMonthly {
    return GenPagesMonthlyMonthly(instance, renderer)
}
)
open class TaskInfo (
    @JsonNotNull
    open var taskNumber: String,
    @JsonNotNull
    open var inspectionDate: String,
    @JsonNotNull
    open var department: String,
    @JsonNotNull
    open var profession: String,
    @JsonNotNull
    open var groupLeader: String,
    @JsonNotNull
    open var deputyLeader: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("TaskInfo", "pages/monthly/task-record.uvue", 360, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return TaskInfoReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class TaskInfoReactiveObject : TaskInfo, IUTSReactive<TaskInfo> {
    override var __v_raw: TaskInfo
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: TaskInfo, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(taskNumber = __v_raw.taskNumber, inspectionDate = __v_raw.inspectionDate, department = __v_raw.department, profession = __v_raw.profession, groupLeader = __v_raw.groupLeader, deputyLeader = __v_raw.deputyLeader) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): TaskInfoReactiveObject {
        return TaskInfoReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var taskNumber: String
        get() {
            return _tRG(__v_raw, "taskNumber", __v_raw.taskNumber, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("taskNumber")) {
                return
            }
            val oldValue = __v_raw.taskNumber
            __v_raw.taskNumber = value
            _tRS(__v_raw, "taskNumber", oldValue, value)
        }
    override var inspectionDate: String
        get() {
            return _tRG(__v_raw, "inspectionDate", __v_raw.inspectionDate, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("inspectionDate")) {
                return
            }
            val oldValue = __v_raw.inspectionDate
            __v_raw.inspectionDate = value
            _tRS(__v_raw, "inspectionDate", oldValue, value)
        }
    override var department: String
        get() {
            return _tRG(__v_raw, "department", __v_raw.department, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("department")) {
                return
            }
            val oldValue = __v_raw.department
            __v_raw.department = value
            _tRS(__v_raw, "department", oldValue, value)
        }
    override var profession: String
        get() {
            return _tRG(__v_raw, "profession", __v_raw.profession, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("profession")) {
                return
            }
            val oldValue = __v_raw.profession
            __v_raw.profession = value
            _tRS(__v_raw, "profession", oldValue, value)
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
}
open class FormData (
    @JsonNotNull
    open var problemType: String,
    @JsonNotNull
    open var problemDescription: String,
    @JsonNotNull
    open var location: String,
    @JsonNotNull
    open var photos: UTSArray<String>,
    @JsonNotNull
    open var safetyScore: String,
    @JsonNotNull
    open var rectificationDate: String,
    @JsonNotNull
    open var module: String,
    @JsonNotNull
    open var responsibility: String,
    @JsonNotNull
    open var issue: String,
    @JsonNotNull
    open var quality: String,
    @JsonNotNull
    open var inspector: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("FormData", "pages/monthly/task-record.uvue", 369, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return FormDataReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class FormDataReactiveObject : FormData, IUTSReactive<FormData> {
    override var __v_raw: FormData
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: FormData, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(problemType = __v_raw.problemType, problemDescription = __v_raw.problemDescription, location = __v_raw.location, photos = __v_raw.photos, safetyScore = __v_raw.safetyScore, rectificationDate = __v_raw.rectificationDate, module = __v_raw.module, responsibility = __v_raw.responsibility, issue = __v_raw.issue, quality = __v_raw.quality, inspector = __v_raw.inspector) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): FormDataReactiveObject {
        return FormDataReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
    }
    override var problemType: String
        get() {
            return _tRG(__v_raw, "problemType", __v_raw.problemType, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("problemType")) {
                return
            }
            val oldValue = __v_raw.problemType
            __v_raw.problemType = value
            _tRS(__v_raw, "problemType", oldValue, value)
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
    override var photos: UTSArray<String>
        get() {
            return _tRG(__v_raw, "photos", __v_raw.photos, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("photos")) {
                return
            }
            val oldValue = __v_raw.photos
            __v_raw.photos = value
            _tRS(__v_raw, "photos", oldValue, value)
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
    override var module: String
        get() {
            return _tRG(__v_raw, "module", __v_raw.module, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("module")) {
                return
            }
            val oldValue = __v_raw.module
            __v_raw.module = value
            _tRS(__v_raw, "module", oldValue, value)
        }
    override var responsibility: String
        get() {
            return _tRG(__v_raw, "responsibility", __v_raw.responsibility, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("responsibility")) {
                return
            }
            val oldValue = __v_raw.responsibility
            __v_raw.responsibility = value
            _tRS(__v_raw, "responsibility", oldValue, value)
        }
    override var issue: String
        get() {
            return _tRG(__v_raw, "issue", __v_raw.issue, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("issue")) {
                return
            }
            val oldValue = __v_raw.issue
            __v_raw.issue = value
            _tRS(__v_raw, "issue", oldValue, value)
        }
    override var quality: String
        get() {
            return _tRG(__v_raw, "quality", __v_raw.quality, __v_isReadonly, __v_isShallow)
        }
        set(value) {
            if (!__v_canSet("quality")) {
                return
            }
            val oldValue = __v_raw.quality
            __v_raw.quality = value
            _tRS(__v_raw, "quality", oldValue, value)
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
}
open class OptionItem (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var name: String,
    open var value: String? = null,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("OptionItem", "pages/monthly/task-record.uvue", 383, 7)
    }
    override fun __v_create(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): UTSReactiveObject {
        return OptionItemReactiveObject(this, __v_isReadonly, __v_isShallow, __v_skip)
    }
}
open class OptionItemReactiveObject : OptionItem, IUTSReactive<OptionItem> {
    override var __v_raw: OptionItem
    override var __v_isReadonly: Boolean
    override var __v_isShallow: Boolean
    override var __v_skip: Boolean
    constructor(__v_raw: OptionItem, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(id = __v_raw.id, name = __v_raw.name, value = __v_raw.value) {
        this.__v_raw = __v_raw
        this.__v_isReadonly = __v_isReadonly
        this.__v_isShallow = __v_isShallow
        this.__v_skip = __v_skip
    }
    override fun __v_clone(__v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean): OptionItemReactiveObject {
        return OptionItemReactiveObject(this.__v_raw, __v_isReadonly, __v_isShallow, __v_skip)
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
    override var value: String?
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
val GenPagesMonthlyTaskRecordClass = CreateVueComponent(GenPagesMonthlyTaskRecord::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesMonthlyTaskRecord.inheritAttrs, inject = GenPagesMonthlyTaskRecord.inject, props = GenPagesMonthlyTaskRecord.props, propsNeedCastKeys = GenPagesMonthlyTaskRecord.propsNeedCastKeys, emits = GenPagesMonthlyTaskRecord.emits, components = GenPagesMonthlyTaskRecord.components, styles = GenPagesMonthlyTaskRecord.styles)
}
, fun(instance, renderer): GenPagesMonthlyTaskRecord {
    return GenPagesMonthlyTaskRecord(instance, renderer)
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
        return UTSSourceMapPosition("LoginFormData", "pages/login/login.uvue", 50, 7)
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
        return UTSSourceMapPosition("InspectionRecord", "pages/record/record.uvue", 70, 7)
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
        return UTSSourceMapPosition("NatureOption", "pages/record/add.uvue", 250, 7)
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
open class ProfessionOption (
    @JsonNotNull
    open var id: String,
    @JsonNotNull
    open var name: String,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ProfessionOption", "pages/record/add.uvue", 261, 7)
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
        return UTSSourceMapPosition("DepartmentOption", "pages/record/add.uvue", 266, 7)
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
    open var assessmentSituation: String,
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
    open var departmentId: String,
    @JsonNotNull
    open var rectificationDate: String,
    open var groupLeader: String? = null,
    open var deputyLeader: String? = null,
) : UTSReactiveObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("RecordFormData", "pages/record/add.uvue", 274, 7)
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
    constructor(__v_raw: RecordFormData, __v_isReadonly: Boolean, __v_isShallow: Boolean, __v_skip: Boolean) : super(responsibleUnit = __v_raw.responsibleUnit, responsiblePerson = __v_raw.responsiblePerson, natureId = __v_raw.natureId, scoreId = __v_raw.scoreId, assessmentSituation = __v_raw.assessmentSituation, safetyScore = __v_raw.safetyScore, professionId = __v_raw.professionId, problemDescription = __v_raw.problemDescription, location = __v_raw.location, inspector = __v_raw.inspector, departmentId = __v_raw.departmentId, rectificationDate = __v_raw.rectificationDate, groupLeader = __v_raw.groupLeader, deputyLeader = __v_raw.deputyLeader) {
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
    override var groupLeader: String?
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
    override var deputyLeader: String?
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
        return UTSSourceMapPosition("NatureOption", "pages/record/detail.uvue", 275, 7)
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
        return UTSSourceMapPosition("ProfessionOption", "pages/record/detail.uvue", 280, 7)
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
        return UTSSourceMapPosition("DepartmentOption", "pages/record/detail.uvue", 285, 7)
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
        return UTSSourceMapPosition("RecordDetail", "pages/record/detail.uvue", 290, 7)
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
        return UTSSourceMapPosition("NatureOption", "pages/record/review.uvue", 307, 7)
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
        return UTSSourceMapPosition("ProfessionOption", "pages/record/review.uvue", 312, 7)
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
        return UTSSourceMapPosition("DepartmentOption", "pages/record/review.uvue", 317, 7)
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
        return UTSSourceMapPosition("RecordDetail", "pages/record/review.uvue", 322, 7)
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
val GenPagesTestTestClass = CreateVueComponent(GenPagesTestTest::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesTestTest.inheritAttrs, inject = GenPagesTestTest.inject, props = GenPagesTestTest.props, propsNeedCastKeys = GenPagesTestTest.propsNeedCastKeys, emits = GenPagesTestTest.emits, components = GenPagesTestTest.components, styles = GenPagesTestTest.styles)
}
, fun(instance, renderer): GenPagesTestTest {
    return GenPagesTestTest(instance, renderer)
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
    override var name: String = "榆林公司岗检系统"
    override var appid: String = "__UNI__B5A0920"
    override var versionName: String = "1.0.1"
    override var versionCode: String = "101"
    override var uniCompilerVersion: String = "4.76"
    constructor() : super() {}
}
fun definePageRoutes() {
    __uniRoutes.push(UniPageRoute(path = "pages/home/home", component = GenPagesHomeHomeClass, meta = UniPageMeta(isQuit = true), style = _uM("navigationBarTitleText" to "首页", "enablePullDownRefresh" to true, "disableScroll" to false)))
    __uniRoutes.push(UniPageRoute(path = "pages/weekly/weekly", component = GenPagesWeeklyWeeklyClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "周检")))
    __uniRoutes.push(UniPageRoute(path = "pages/monthly/monthly", component = GenPagesMonthlyMonthlyClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "月检")))
    __uniRoutes.push(UniPageRoute(path = "pages/monthly/task-record", component = GenPagesMonthlyTaskRecordClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "月检任务录入")))
    __uniRoutes.push(UniPageRoute(path = "pages/profile/profile", component = GenPagesProfileProfileClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "我的")))
    __uniRoutes.push(UniPageRoute(path = "pages/index/index", component = GenPagesIndexIndexClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "首页")))
    __uniRoutes.push(UniPageRoute(path = "pages/login/login", component = GenPagesLoginLoginClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "登录", "navigationStyle" to "custom")))
    __uniRoutes.push(UniPageRoute(path = "pages/record/record", component = GenPagesRecordRecordClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "记录")))
    __uniRoutes.push(UniPageRoute(path = "pages/record/add", component = GenPagesRecordAddClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "新增记录")))
    __uniRoutes.push(UniPageRoute(path = "pages/record/detail", component = GenPagesRecordDetailClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "记录详情")))
    __uniRoutes.push(UniPageRoute(path = "pages/record/review", component = GenPagesRecordReviewClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "复查记录")))
    __uniRoutes.push(UniPageRoute(path = "pages/statistics/statistics", component = GenPagesStatisticsStatisticsClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "统计")))
    __uniRoutes.push(UniPageRoute(path = "pages/chart-test/chart-test", component = GenPagesChartTestChartTestClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "图表测试")))
    __uniRoutes.push(UniPageRoute(path = "pages/test/test", component = GenPagesTestTestClass, meta = UniPageMeta(isQuit = false), style = _uM("navigationBarTitleText" to "延迟整改测试")))
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
