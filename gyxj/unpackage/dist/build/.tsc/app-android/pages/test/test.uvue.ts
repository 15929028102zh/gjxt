
	const __sfc__ = defineComponent({
		data() {
			return {
				testRecordId: '1' // 使用一个测试记录ID
			}
		},
		methods: {
			testDelayedMode() {
				console.log('测试延迟整改模式')
				uni.navigateTo({
					url: `/pages/record/review?id=${this.testRecordId}&delayed=true`
				})
			},
			
			testNormalMode() {
				console.log('测试正常模式')
				uni.navigateTo({
					url: `/pages/record/review?id=${this.testRecordId}`
				})
			}
		}
	})

export default __sfc__
function GenPagesTestTestRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
  return _cE("view", _uM({ class: "container" }), [
    _cE("view", _uM({ class: "test-section" }), [
      _cE("text", _uM({ class: "title" }), "延迟整改功能测试"),
      _cE("button", _uM({
        class: "test-btn",
        onClick: _ctx.testDelayedMode
      }), "测试延迟整改模式", 8 /* PROPS */, ["onClick"]),
      _cE("button", _uM({
        class: "test-btn",
        onClick: _ctx.testNormalMode
      }), "测试正常模式", 8 /* PROPS */, ["onClick"]),
      _cE("view", _uM({ class: "info" }), [
        _cE("text", null, "点击上方按钮测试不同模式下的复查页面显示效果")
      ])
    ])
  ])
}
const GenPagesTestTestStyles = [_uM([["container", _pS(_uM([["paddingTop", "40rpx"], ["paddingRight", "40rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "40rpx"]]))], ["test-section", _pS(_uM([["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "40rpx"], ["paddingRight", "40rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "40rpx"], ["boxShadow", "0 4rpx 12rpx rgba(0, 0, 0, 0.1)"]]))], ["title", _pS(_uM([["fontSize", "36rpx"], ["fontWeight", "bold"], ["color", "#333333"], ["marginBottom", "40rpx"], ["textAlign", "center"]]))], ["test-btn", _pS(_uM([["width", "100%"], ["height", "80rpx"], ["backgroundColor", "#007aff"], ["color", "#FFFFFF"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", "10rpx"], ["borderTopRightRadius", "10rpx"], ["borderBottomRightRadius", "10rpx"], ["borderBottomLeftRadius", "10rpx"], ["fontSize", "32rpx"], ["marginBottom", "20rpx"], ["backgroundColor:active", "#0056cc"]]))], ["info", _pS(_uM([["marginTop", "40rpx"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["backgroundColor", "#f8f9fa"], ["borderTopLeftRadius", "10rpx"], ["borderTopRightRadius", "10rpx"], ["borderBottomRightRadius", "10rpx"], ["borderBottomLeftRadius", "10rpx"], ["textAlign", "center"]]))]])]
