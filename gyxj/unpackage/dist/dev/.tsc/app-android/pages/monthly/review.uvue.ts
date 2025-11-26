
  import { getApiUrl, apiConfig } from '@/config/api.uts'

  type MonthlyReviewDetail = { __$originalPosition?: UTSSourceMapPosition<"MonthlyReviewDetail", "pages/monthly/review.uvue", 70, 8>;
    id: string
    problemDescription: string
    location: string
    rectificationEffect: string
    rectificationImages: string
    taskNo: string
    inspectionTime: string
    departmentName: string
    professionalTypeName: string
    responsibilityName: string
    issueName: string
    inspector: string
    groupLeaderName: string
    deputyLeaderName: string
    photos: string
  }

  const __sfc__ = defineComponent({
    data() {
      return {
        id: '',
        detail: null as MonthlyReviewDetail | null,
        rectImages: [] as string[],
        reviewSuggestion: '',
        reviewImages: [] as string[],
        loading: true,
        saving: false,
        problemImages: [] as string[],
        containerHeight: 0
      }
    },
    onLoad(options: UTSJSONObject) {
      this.id = options.getString('id') ?? ''
      this.loadDetail()
    },
    onReady() {
      uni.getSystemInfo({ success: (res) => { this.containerHeight = res.windowHeight } })
    },
    methods: {
      convertToHttpUrl(path: string): string {
        if (path == null || path == '') return ''
        if (path.startsWith('http://') || path.startsWith('https://')) return path
        return `${apiConfig.baseUrl}/api${path}`
      },
      loadDetail() {
        const token = uni.getStorageSync('token')
        uni.request({
          url: getApiUrl(`/monthly-inspection-record/${this.id}`),
          method: 'GET',
          header: { 'Authorization': `Bearer ${token}` },
          success: (res) => {
            const data = res.data as UTSJSONObject
            if (res.statusCode === 200 && (data.getNumber('code') ?? 0) === 200) {
              const d = data.getJSON('data') as UTSJSONObject | null
              if (d != null) {
                this.detail = {
                  id: d!!.getString('id') ?? '',
                  problemDescription: d!!.getString('problemDescription') ?? '',
                  location: d!!.getString('location') ?? '',
                  rectificationEffect: (d!!.getString('rectificationEffect') ?? d!!.getString('rectificationSuggestion') ?? ''),
                  rectificationImages: d!!.getString('rectificationImages') ?? '',
                  taskNo: d!!.getString('taskNo') ?? '',
                  inspectionTime: d!!.getString('inspectionTime') ?? '',
                  departmentName: d!!.getString('departmentName') ?? '',
                  professionalTypeName: d!!.getString('professionalTypeName') ?? '',
                  responsibilityName: d!!.getString('responsibilityName') ?? '',
                  issueName: d!!.getString('issueName') ?? '',
                  inspector: d!!.getString('inspector') ?? '',
                  groupLeaderName: d!!.getString('groupLeaderName') ?? '',
                  deputyLeaderName: d!!.getString('deputyLeaderName') ?? '',
                  photos: d!!.getString('photos') ?? ''
                }
                if (this.detail.photos != null && this.detail.photos != '') {
                  const p = this.detail.photos
                  if (p.trim().startsWith('[') && p.trim().endsWith(']')) {
                    try { this.problemImages = (UTSAndroid.consoleDebugError(JSON.parse(p), " at pages/monthly/review.uvue:146") as string[]) } catch (_) {}
                  } else { this.problemImages = p.split(',').map((u) => u.trim()).filter((u) => u != '') }
                }
                const imgs = this.detail.rectificationImages
                if (imgs != null && imgs != '') {
                  if (imgs.trim().startsWith('[') && imgs.trim().endsWith(']')) {
                    try { this.rectImages = (UTSAndroid.consoleDebugError(JSON.parse(imgs), " at pages/monthly/review.uvue:152") as string[]) } catch (_) {}
                  } else { this.rectImages = imgs.split(',').map((u) => u.trim()).filter((u) => u != '') }
                }
              }
            } else { uni.showToast({ title: data.getString('message') ?? '加载失败', icon: 'none' }) }
            this.loading = false
          },
          fail: (_) => { this.loading = false; uni.showToast({ title: '网络错误', icon: 'none' }) }
        })
      },
      previewRect(i: number) { uni.previewImage({ current: i, urls: this.rectImages.map((u) => this.convertToHttpUrl(u)) }) },
      previewProblem(i: number) { uni.previewImage({ current: i, urls: this.problemImages.map((u) => this.convertToHttpUrl(u)) }) },
      chooseReview() {
        if (this.reviewImages.length >= 3) { uni.showToast({ title: '最多3张', icon: 'none' }); return }
        uni.chooseImage({
          count: 3 - this.reviewImages.length,
          sourceType: ['camera', 'album'],
          success: (res) => {
            const token = uni.getStorageSync('token')
            res.tempFilePaths.forEach((p) => {
              uni.uploadFile({
                url: getApiUrl('/upload/image'),
                filePath: p,
                name: 'file',
                header: { 'Authorization': `Bearer ${token}` },
                success: (upRes) => {
                  try {
                    const result = UTSAndroid.consoleDebugError(JSON.parse(upRes.data), " at pages/monthly/review.uvue:179") as UTSJSONObject
                    if (upRes.statusCode === 200 && (result.getNumber('code') ?? 0) === 200) {
                      const dataObj = result.getJSON('data')
                      const url = dataObj?.getString('filePath') ?? ''
                      if (url != '') this.reviewImages.push(url)
                    } else { uni.showToast({ title: result.getString('message') ?? '上传失败', icon: 'none' }) }
                  } catch (_) { uni.showToast({ title: '上传失败', icon: 'none' }) }
                },
                fail: (_) => { uni.showToast({ title: '上传失败', icon: 'none' }) }
              })
            })
          }
        })
      },
      previewReview(i: number) { uni.previewImage({ current: i, urls: this.reviewImages.map((u) => this.convertToHttpUrl(u)) }) },
      removeReview(i: number) { this.reviewImages.splice(i, 1) },
      submitReview() {
        if (this.saving) return
        if (this.reviewSuggestion.trim() == '') { uni.showToast({ title: '请填写复查效果', icon: 'none' }); return }
        this.saving = true
        const token = uni.getStorageSync('token')
        const data = {__$originalPosition: new UTSSourceMapPosition("data", "pages/monthly/review.uvue", 200, 15),
          reviewSuggestion: this.reviewSuggestion,
          reviewImages: this.reviewImages.join(',')
        }
        uni.request({
          url: getApiUrl(`/monthly-inspection-record/${this.id}/review`),
          method: 'PUT',
          header: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
          data: data,
          success: (res) => {
            const resp = res.data as UTSJSONObject
            if (res.statusCode === 200 && (resp.getNumber('code') ?? 0) === 200) {
              uni.showToast({ title: '复查确认成功', icon: 'success' })
              setTimeout(function(){ uni.navigateBack() }, 1200)
            } else { uni.showToast({ title: resp.getString('message') ?? '提交失败', icon: 'none' }) }
            this.saving = false
          },
          fail: (_) => { this.saving = false; uni.showToast({ title: '网络错误', icon: 'none' }) }
        })
      }
    }
  })

export default __sfc__
function GenPagesMonthlyReviewRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
  return _cE("scroll-view", _uM({
    class: "container",
    "scroll-y": "true",
    style: _nS(_uM({height: _ctx.containerHeight + 'px'}))
  }), [
    isTrue(_ctx.loading)
      ? _cE("view", _uM({
          key: 0,
          class: "loading-state"
        }), [
          _cE("text", null, "加载中...")
        ])
      : _ctx.detail != null
        ? _cE("view", _uM({
            key: 1,
            class: "content"
          }), [
            _cE("view", _uM({ class: "section" }), [
              _cE("text", _uM({ class: "title" }), "问题信息"),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "问题描述"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.problemDescription), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "发生部位"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.location), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "任务编号"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.taskNo), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "月检时间"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.inspectionTime), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "所属部门"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.departmentName), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "检查专业"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.professionalTypeName), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "责任要素"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.responsibilityName), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "检查内容"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.issueName), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "检查人"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.inspector), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "检查组长"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.groupLeaderName), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "检查副组长"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.deputyLeaderName), 1 /* TEXT */)
              ])
            ]),
            _ctx.problemImages.length > 0
              ? _cE("view", _uM({
                  key: 0,
                  class: "section"
                }), [
                  _cE("text", _uM({ class: "title" }), "问题图片"),
                  _cE("view", _uM({ class: "image-list" }), [
                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.problemImages, (img, i, __index, _cached): any => {
                      return _cE("view", _uM({
                        class: "image-item",
                        key: i
                      }), [
                        _cE("image", _uM({
                          src: _ctx.convertToHttpUrl(img),
                          mode: "aspectFill",
                          class: "image",
                          onClick: () => {_ctx.previewProblem(i)}
                        }), null, 8 /* PROPS */, ["src", "onClick"])
                      ])
                    }), 128 /* KEYED_FRAGMENT */)
                  ])
                ])
              : _cC("v-if", true),
            _cE("view", _uM({ class: "section" }), [
              _cE("text", _uM({ class: "title" }), "整改效果"),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "整改效果"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.rectificationEffect), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "image-list" }), [
                _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectImages, (img, i, __index, _cached): any => {
                  return _cE("view", _uM({
                    class: "image-item",
                    key: i
                  }), [
                    _cE("image", _uM({
                      src: _ctx.convertToHttpUrl(img),
                      mode: "aspectFill",
                      class: "image",
                      onClick: () => {_ctx.previewRect(i)}
                    }), null, 8 /* PROPS */, ["src", "onClick"])
                  ])
                }), 128 /* KEYED_FRAGMENT */),
                _ctx.rectImages.length == 0
                  ? _cE("view", _uM({
                      key: 0,
                      class: "no-image"
                    }), [
                      _cE("text", null, "暂无整改图片")
                    ])
                  : _cC("v-if", true)
              ])
            ]),
            _cE("view", _uM({ class: "section" }), [
              _cE("text", _uM({ class: "title" }), "复查效果"),
              _cE("textarea", _uM({
                class: "textarea",
                modelValue: _ctx.reviewSuggestion,
                onInput: ($event: UniInputEvent) => {(_ctx.reviewSuggestion) = $event.detail.value},
                placeholder: "请填写复查效果",
                maxlength: "500"
              }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
            ]),
            _cE("view", _uM({ class: "section" }), [
              _cE("text", _uM({ class: "title" }), "复查图片（最多3张，已选" + _tD(_ctx.reviewImages.length) + "张）", 1 /* TEXT */),
              _cE("view", _uM({ class: "image-list" }), [
                _cE(Fragment, null, RenderHelpers.renderList(_ctx.reviewImages, (img, i, __index, _cached): any => {
                  return _cE("view", _uM({
                    class: "image-item",
                    key: i
                  }), [
                    _cE("image", _uM({
                      src: _ctx.convertToHttpUrl(img),
                      mode: "aspectFill",
                      class: "image",
                      onClick: () => {_ctx.previewReview(i)}
                    }), null, 8 /* PROPS */, ["src", "onClick"]),
                    _cE("view", _uM({
                      class: "delete",
                      onClick: () => {_ctx.removeReview(i)}
                    }), [
                      _cE("text", null, "×")
                    ], 8 /* PROPS */, ["onClick"])
                  ])
                }), 128 /* KEYED_FRAGMENT */),
                _ctx.reviewImages.length < 3
                  ? _cE("view", _uM({
                      key: 0,
                      class: "add",
                      onClick: _ctx.chooseReview
                    }), [
                      _cE("text", _uM({ class: "add-icon" }), "+"),
                      _cE("text", _uM({ class: "add-text" }), "添加图片")
                    ], 8 /* PROPS */, ["onClick"])
                  : _cC("v-if", true)
              ])
            ]),
            _cE("view", _uM({ class: "footer" }), [
              _cE("button", _uM({
                class: "submit",
                onClick: _ctx.submitReview,
                disabled: _ctx.saving
              }), _tD(_ctx.saving ? '提交中...' : '确认复查'), 9 /* TEXT, PROPS */, ["onClick", "disabled"])
            ])
          ])
        : _cE("view", _uM({
            key: 2,
            class: "error"
          }), [
            _cE("text", null, "记录不存在或已删除")
          ])
  ], 4 /* STYLE */)
}
const GenPagesMonthlyReviewStyles = [_uM([["container", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#f5f5f5"]]))], ["loading-state", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["textAlign", "center"], ["color", "#666666"]]))], ["error", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["textAlign", "center"], ["color", "#666666"]]))], ["content", _pS(_uM([["paddingTop", 12], ["paddingRight", 16], ["paddingBottom", 12], ["paddingLeft", 16]]))], ["section", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", 10], ["borderTopRightRadius", 10], ["borderBottomRightRadius", 10], ["borderBottomLeftRadius", 10], ["paddingTop", 16], ["paddingRight", 16], ["paddingBottom", 16], ["paddingLeft", 16], ["marginBottom", 14], ["boxShadow", "0 2px 8px rgba(0,0,0,0.06)"]]))], ["title", _pS(_uM([["fontSize", 16], ["color", "#303133"], ["marginBottom", 12]]))], ["field", _pS(_uM([["marginBottom", 10], ["display", "flex"], ["justifyContent", "space-between"]]))], ["label", _pS(_uM([["fontSize", 14], ["color", "#666666"]]))], ["value", _pS(_uM([["fontSize", 14], ["color", "#333333"], ["textAlign", "right"]]))], ["textarea", _pS(_uM([["width", "100%"], ["minHeight", 100], ["paddingTop", 12], ["paddingRight", 12], ["paddingBottom", 12], ["paddingLeft", 12], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e0e0e0"], ["borderRightColor", "#e0e0e0"], ["borderBottomColor", "#e0e0e0"], ["borderLeftColor", "#e0e0e0"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["fontSize", 14]]))], ["image-list", _pS(_uM([["display", "flex"], ["flexWrap", "wrap"], ["gap", "10px"]]))], ["image-item", _pS(_uM([["position", "relative"], ["width", 100], ["height", 100]]))], ["image", _pS(_uM([["width", "100%"], ["height", "100%"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e0e0e0"], ["borderRightColor", "#e0e0e0"], ["borderBottomColor", "#e0e0e0"], ["borderLeftColor", "#e0e0e0"]]))], ["delete", _pS(_uM([["position", "absolute"], ["top", -6], ["right", -6], ["width", 22], ["height", 22], ["backgroundImage", "none"], ["backgroundColor", "#ff4757"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["color", "#ffffff"]]))], ["add", _pS(_uM([["width", 100], ["height", 100], ["borderTopWidth", 2], ["borderRightWidth", 2], ["borderBottomWidth", 2], ["borderLeftWidth", 2], ["borderTopStyle", "dashed"], ["borderRightStyle", "dashed"], ["borderBottomStyle", "dashed"], ["borderLeftStyle", "dashed"], ["borderTopColor", "#cccccc"], ["borderRightColor", "#cccccc"], ["borderBottomColor", "#cccccc"], ["borderLeftColor", "#cccccc"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"], ["backgroundImage", "none"], ["backgroundColor", "#fafafa"]]))], ["add-icon", _pS(_uM([["fontSize", 22], ["color", "#999999"]]))], ["add-text", _pS(_uM([["fontSize", 12], ["color", "#999999"]]))], ["no-image", _pS(_uM([["fontSize", 12], ["color", "#999999"]]))], ["footer", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["paddingTop", 15], ["paddingRight", 15], ["paddingBottom", 15], ["paddingLeft", 15], ["borderTopLeftRadius", 10], ["borderTopRightRadius", 10], ["borderBottomRightRadius", 10], ["borderBottomLeftRadius", 10], ["boxShadow", "0 2px 8px rgba(0,0,0,0.06)"]]))], ["submit", _pS(_uM([["width", "100%"], ["paddingTop", 12], ["paddingRight", 12], ["paddingBottom", 12], ["paddingLeft", 12], ["backgroundImage", "none"], ["backgroundColor", "#007AFF"], ["color", "#ffffff"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8], ["fontSize", 16]]))]])]
