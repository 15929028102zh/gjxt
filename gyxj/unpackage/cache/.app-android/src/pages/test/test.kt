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
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
open class GenPagesTestTest : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("view", _uM("class" to "container"), _uA(
            _cE("view", _uM("class" to "test-section"), _uA(
                _cE("text", _uM("class" to "title"), "延迟整改功能测试"),
                _cE("button", _uM("class" to "test-btn", "onClick" to _ctx.testDelayedMode), "测试延迟整改模式", 8, _uA(
                    "onClick"
                )),
                _cE("button", _uM("class" to "test-btn", "onClick" to _ctx.testNormalMode), "测试正常模式", 8, _uA(
                    "onClick"
                )),
                _cE("view", _uM("class" to "info"), _uA(
                    _cE("text", null, "点击上方按钮测试不同模式下的复查页面显示效果")
                ))
            ))
        ))
    }
    open var testRecordId: String by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("testRecordId" to "1")
    }
    open var testDelayedMode = ::gen_testDelayedMode_fn
    open fun gen_testDelayedMode_fn() {
        console.log("测试延迟整改模式", " at pages/test/test.uvue:25")
        uni_navigateTo(NavigateToOptions(url = "/pages/record/review?id=" + this.testRecordId + "&delayed=true"))
    }
    open var testNormalMode = ::gen_testNormalMode_fn
    open fun gen_testNormalMode_fn() {
        console.log("测试正常模式", " at pages/test/test.uvue:32")
        uni_navigateTo(NavigateToOptions(url = "/pages/record/review?id=" + this.testRecordId))
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
                return _uM("container" to _pS(_uM("paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx")), "test-section" to _pS(_uM("backgroundColor" to "#ffffff", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.1)")), "title" to _pS(_uM("fontSize" to "36rpx", "fontWeight" to "bold", "color" to "#333333", "marginBottom" to "40rpx", "textAlign" to "center")), "test-btn" to _pS(_uM("width" to "100%", "height" to "80rpx", "backgroundColor" to "#007aff", "color" to "#FFFFFF", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "fontSize" to "32rpx", "marginBottom" to "20rpx", "backgroundColor:active" to "#0056cc")), "info" to _pS(_uM("marginTop" to "40rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "textAlign" to "center")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
