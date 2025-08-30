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
import uts.sdk.modules.tuiXechars.GenUniModulesTuiXecharsComponentsTuiXecharsTuiXecharsClass
open class GenPagesChartsDemoChartsDemo : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesChartsDemoChartsDemo) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesChartsDemoChartsDemo
            val _cache = __ins.renderCache
            val lineChart = ref(null)
            val barChart = ref(null)
            val pieChart = ref(null)
            val lineChartOption = ref(object : UTSJSONObject() {
                var type = "line"
                var color = _uA(
                    "#36CFC9",
                    "#6B7280"
                )
                var title = object : UTSJSONObject() {
                    var name = "产品销量趋势"
                    var fontSize: Number = 16
                    var color = "#333333"
                }
                var subtitle = object : UTSJSONObject() {
                    var name = "月度对比"
                    var fontSize: Number = 12
                    var color = "#999999"
                }
                var categories = _uA(
                    "1月",
                    "2月",
                    "3月",
                    "4月",
                    "5月",
                    "6月"
                )
                var series = _uA(
                    object : UTSJSONObject() {
                        var name = "产品A"
                        var data: UTSArray<Number> = _uA(
                            120,
                            150,
                            130,
                            180,
                            160,
                            200
                        )
                    },
                    object : UTSJSONObject() {
                        var name = "产品B"
                        var data: UTSArray<Number> = _uA(
                            90,
                            120,
                            140,
                            130,
                            170,
                            190
                        )
                    }
                )
                var extra = object : UTSJSONObject() {
                    var line = object : UTSJSONObject() {
                        var type = "straight"
                        var width: Number = 2
                        var activeType = "solid"
                    }
                }
            })
            val barChartOption = ref(object : UTSJSONObject() {
                var type = "column"
                var color = _uA(
                    "#4F46E5"
                )
                var title = object : UTSJSONObject() {
                    var name = "月度销量统计"
                    var fontSize: Number = 16
                    var color = "#333333"
                }
                var subtitle = object : UTSJSONObject() {
                    var name = "单位：件"
                    var fontSize: Number = 12
                    var color = "#999999"
                }
                var categories = _uA(
                    "1月",
                    "2月",
                    "3月",
                    "4月",
                    "5月",
                    "6月"
                )
                var series = _uA(
                    object : UTSJSONObject() {
                        var name = "销量"
                        var data: UTSArray<Number> = _uA(
                            1200,
                            1900,
                            1500,
                            2400,
                            1800,
                            2800
                        )
                    }
                )
                var extra = object : UTSJSONObject() {
                    var column = object : UTSJSONObject() {
                        var type = "group"
                        var width: Number = 30
                        var activeBgColor = "#000000"
                        var activeBgOpacity: Number = 0.08
                    }
                }
            })
            val pieChartOption = ref(object : UTSJSONObject() {
                var type = "pie"
                var color = _uA(
                    "#FF6B6B",
                    "#4ECDC4",
                    "#45B7D1",
                    "#96CEB4"
                )
                var title = object : UTSJSONObject() {
                    var name = "产品分类占比"
                    var fontSize: Number = 16
                    var color = "#333333"
                }
                var subtitle = object : UTSJSONObject() {
                    var name = "总计：100%"
                    var fontSize: Number = 12
                    var color = "#999999"
                }
                var extra = object : UTSJSONObject() {
                    var pie = object : UTSJSONObject() {
                        var activeOpacity: Number = 0.5
                        var activeRadius: Number = 10
                        var offsetAngle: Number = 0
                        var labelWidth: Number = 15
                        var border = true
                        var borderWidth: Number = 2
                        var borderColor = "#FFFFFF"
                    }
                }
                var series = _uA(
                    object : UTSJSONObject() {
                        var name = "电子产品"
                        var value: Number = 45
                        var color = "#FF6B6B"
                    },
                    object : UTSJSONObject() {
                        var name = "服装"
                        var value: Number = 25
                        var color = "#4ECDC4"
                    },
                    object : UTSJSONObject() {
                        var name = "食品"
                        var value: Number = 20
                        var color = "#45B7D1"
                    },
                    object : UTSJSONObject() {
                        var name = "其他"
                        var value: Number = 10
                        var color = "#96CEB4"
                    }
                )
            })
            val onLineChartInit = fun(): Unit {
                console.log("折线图初始化完成", " at pages/charts-demo/charts-demo.uvue:168")
            }
            val onBarChartInit = fun(): Unit {
                console.log("柱状图初始化完成", " at pages/charts-demo/charts-demo.uvue:172")
            }
            val onPieChartInit = fun(): Unit {
                console.log("饼图初始化完成", " at pages/charts-demo/charts-demo.uvue:176")
            }
            val updateLineChart = fun(): Unit {
                lineChartOption.value = UTSJSONObject.assign<UTSJSONObject>(UTSJSONObject(), lineChartOption.value, object : UTSJSONObject() {
                    var series = _uA(
                        object : UTSJSONObject() {
                            var name = "产品A"
                            var data: UTSArray<Number> = _uA(
                                140,
                                170,
                                150,
                                200,
                                180,
                                220
                            )
                        },
                        object : UTSJSONObject() {
                            var name = "产品B"
                            var data: UTSArray<Number> = _uA(
                                110,
                                140,
                                160,
                                150,
                                190,
                                210
                            )
                        }
                    )
                }) as UTSJSONObject
            }
            val updateBarChart = fun(): Unit {
                barChartOption.value = UTSJSONObject.assign<UTSJSONObject>(UTSJSONObject(), barChartOption.value, object : UTSJSONObject() {
                    var series = _uA(
                        object : UTSJSONObject() {
                            var name = "销量"
                            var data: UTSArray<Number> = _uA(
                                1400,
                                2100,
                                1700,
                                2600,
                                2000,
                                3000
                            )
                        }
                    )
                }) as UTSJSONObject
            }
            val updatePieChart = fun(): Unit {
                pieChartOption.value = UTSJSONObject.assign<UTSJSONObject>(UTSJSONObject(), pieChartOption.value, object : UTSJSONObject() {
                    var series = _uA(
                        object : UTSJSONObject() {
                            var name = "电子产品"
                            var value: Number = 50
                            var color = "#FF6B6B"
                        },
                        object : UTSJSONObject() {
                            var name = "服装"
                            var value: Number = 30
                            var color = "#4ECDC4"
                        },
                        object : UTSJSONObject() {
                            var name = "食品"
                            var value: Number = 15
                            var color = "#45B7D1"
                        },
                        object : UTSJSONObject() {
                            var name = "其他"
                            var value: Number = 5
                            var color = "#96CEB4"
                        }
                    )
                }) as UTSJSONObject
            }
            onMounted(fun(){
                console.log("图表页面加载完成", " at pages/charts-demo/charts-demo.uvue:240")
                setTimeout(fun(){
                    console.log("更新图表数据", " at pages/charts-demo/charts-demo.uvue:244")
                    updateLineChart()
                    updateBarChart()
                    updatePieChart()
                }
                , 3000)
            }
            )
            return fun(): Any? {
                val _component_tui_xechars = resolveEasyComponent("tui-xechars", GenUniModulesTuiXecharsComponentsTuiXecharsTuiXecharsClass)
                return _cE("view", _uM("class" to "chart-page"), _uA(
                    _cE("view", _uM("class" to "chart-section"), _uA(
                        _cE("text", _uM("class" to "section-title"), "折线图示例"),
                        _cE("view", _uM("class" to "chart-wrapper"), _uA(
                            _cV(_component_tui_xechars, _uM("ref_key" to "lineChart", "ref" to lineChart, "option" to lineChartOption.value, "width" to 350, "height" to 300, "onInitFinished" to onLineChartInit), null, 8, _uA(
                                "option"
                            ))
                        ))
                    )),
                    _cE("view", _uM("class" to "chart-section"), _uA(
                        _cE("text", _uM("class" to "section-title"), "柱状图示例"),
                        _cE("view", _uM("class" to "chart-wrapper"), _uA(
                            _cV(_component_tui_xechars, _uM("ref_key" to "barChart", "ref" to barChart, "option" to barChartOption.value, "width" to 350, "height" to 300, "onInitFinished" to onBarChartInit), null, 8, _uA(
                                "option"
                            ))
                        ))
                    )),
                    _cE("view", _uM("class" to "chart-section"), _uA(
                        _cE("text", _uM("class" to "section-title"), "饼图示例"),
                        _cE("view", _uM("class" to "chart-wrapper"), _uA(
                            _cV(_component_tui_xechars, _uM("ref_key" to "pieChart", "ref" to pieChart, "option" to pieChartOption.value, "width" to 350, "height" to 300, "onInitFinished" to onPieChartInit), null, 8, _uA(
                                "option"
                            ))
                        ))
                    ))
                ))
            }
        }
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ), _uA(
                GenApp.styles
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("chart-page" to _pS(_uM("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "backgroundColor" to "#F9FAFB")), "chart-section" to _pS(_uM("backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "marginBottom" to "30rpx", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.08)")), "section-title" to _pS(_uM("fontSize" to "32rpx", "fontWeight" to "bold", "color" to "#1F2937", "marginBottom" to "20rpx")), "chart-wrapper" to _pS(_uM("width" to "100%", "display" to "flex", "justifyContent" to "center", "alignItems" to "center", "backgroundColor" to "#FAFAFA", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "marginTop" to "20rpx")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
