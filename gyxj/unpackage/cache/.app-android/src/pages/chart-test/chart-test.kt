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
open class GenPagesChartTestChartTest : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesChartTestChartTest) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesChartTestChartTest
            val _cache = __ins.renderCache
            val testChart = ref(null)
            val chartStatus = ref("正在初始化...")
            val chartOption = ref(_uO("type" to "pie", "color" to _uA(
                "#ff6b6b",
                "#4ecdc4",
                "#45b7d1"
            ), "title" to _uO("name" to "测试图表", "fontSize" to 16, "color" to "#333333"), "subtitle" to _uO("name" to "总计: 100", "fontSize" to 12, "color" to "#999999"), "extra" to _uO("pie" to _uO("activeOpacity" to 0.5, "activeRadius" to 10, "offsetAngle" to 0, "labelWidth" to 15, "border" to true, "borderWidth" to 2, "borderColor" to "#FFFFFF")), "series" to _uA(
                _uO("name" to "项目A", "value" to 30, "color" to "#ff6b6b"),
                _uO("name" to "项目B", "value" to 40, "color" to "#4ecdc4"),
                _uO("name" to "项目C", "value" to 30, "color" to "#45b7d1")
            )))
            val onChartInit = fun(): Unit {
                console.log("图表初始化完成", " at pages/chart-test/chart-test.uvue:73")
                chartStatus.value = "图表已初始化"
            }
            onMounted(fun(){
                console.log("测试页面加载", " at pages/chart-test/chart-test.uvue:78")
                nextTick(fun(){
                    console.log("开始初始化图表", " at pages/chart-test/chart-test.uvue:80")
                    chartStatus.value = "图表配置已设置"
                }
                )
            }
            )
            return fun(): Any? {
                val _component_tui_xechars = resolveEasyComponent("tui-xechars", GenUniModulesTuiXecharsComponentsTuiXecharsTuiXecharsClass)
                return _cE("view", _uM("class" to "container"), _uA(
                    _cE("view", _uM("class" to "title"), _uA(
                        _cE("text", _uM("class" to "title-text"), "图表测试页面")
                    )),
                    _cE("view", _uM("class" to "chart-wrapper"), _uA(
                        _cV(_component_tui_xechars, _uM("ref_key" to "testChart", "ref" to testChart, "option" to chartOption.value, "width" to 350, "height" to 300, "onInitFinished" to onChartInit), null, 8, _uA(
                            "option"
                        ))
                    )),
                    _cE("view", _uM("class" to "info"), _uA(
                        _cE("text", _uM("class" to "info-text"), "图表状态: " + _tD(chartStatus.value), 1)
                    ))
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
                return _uM("container" to _pS(_uM("paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "backgroundColor" to "#f5f5f5", "height" to "100%")), "title" to _pS(_uM("textAlign" to "center", "marginBottom" to "40rpx")), "title-text" to _pS(_uM("fontSize" to "36rpx", "fontWeight" to "bold", "color" to "#333333")), "chart-wrapper" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "marginBottom" to "20rpx", "boxShadow" to "0 2rpx 8rpx rgba(0, 0, 0, 0.1)")), "info" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "textAlign" to "center")), "info-text" to _pS(_uM("color" to "#666666", "fontSize" to "28rpx")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
