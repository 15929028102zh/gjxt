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
import io.dcloud.uniapp.extapi.createCanvasContextAsync as uni_createCanvasContextAsync
import io.dcloud.uniapp.extapi.getElementById as uni_getElementById
import io.dcloud.uniapp.extapi.getWindowInfo as uni_getWindowInfo
open class GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars : VueComponent {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenUniModulesTuiXecharsComponentsTuiXecharsTuiXechars
            val _cache = __ins.renderCache
            val guid: String = "tui" + TuiGuid(20)
            fun emits(event: String, vararg do_not_transform_spread: Any?) {
                __ins.emit(event, *do_not_transform_spread)
            }
            val charts = TuiCharts()
            val ins = getCurrentInstance()?.proxy as ComponentPublicInstance
            onMounted(fun(){
                nextTick(fun(){
                    (uni_getElementById(guid) as UniElement).getBoundingClientRectAsync()?.then(fun(rst: DOMRect){
                        uni_createCanvasContextAsync(CreateCanvasContextAsyncOptions(id = guid, component = ins, success = fun(context: CanvasContext){
                            val ctx = context.getContext("2d")!!
                            val canvas = ctx.canvas
                            val dpr = uni_getWindowInfo().pixelRatio
                            canvas.width = canvas.offsetWidth * dpr
                            canvas.height = canvas.offsetHeight * dpr
                            ctx.scale(dpr, dpr)
                            charts.setInsMethod(ins, rst)
                            charts.setContext(context)
                            emits("initFinished", charts)
                        }
                        ))
                    }
                    )
                }
                )
            }
            )
            fun gen_touchStart_fn(e: UniTouchEvent) {
                charts.touchStart(e)
            }
            val touchStart = ::gen_touchStart_fn
            fun gen_touchMove_fn(e: UniTouchEvent) {
                charts.touchMove(e)
            }
            val touchMove = ::gen_touchMove_fn
            fun gen_touchEnd_fn(e: UniTouchEvent) {
                charts.touchEnd(e)
            }
            val touchEnd = ::gen_touchEnd_fn
            fun gen_tap_fn(e: UniPointerEvent) {
                charts.tap(e)
            }
            val tap = ::gen_tap_fn
            return fun(): Any? {
                return _cE("canvas", _uM("class" to "tui-xcharts-main", "id" to guid, "onClick" to tap, "onTouchstart" to touchStart, "onTouchmove" to touchMove, "onTouchend" to touchEnd), null, 32)
            }
        }
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("tui-xcharts-main" to _pS(_uM("width" to "690rpx", "height" to "500rpx")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM("initFinished" to null, "select" to null)
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
