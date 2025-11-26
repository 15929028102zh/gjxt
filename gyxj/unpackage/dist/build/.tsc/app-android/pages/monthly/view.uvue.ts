
  import { getApiUrl, apiConfig } from '@/config/api.uts'

  type MonthlyDetail = {
    id: string
    recordStatus: number
    problemDescription: string
    responsibleUnit: string
    responsiblePerson: string
    qualityName: string
    safetyScore: number
    location: string
    inspector: string
    rectificationEffect: string
    reviewEffect: string
    photos: string
    rectificationImages: string
    reviewImages: string
    taskNo: string
    inspectionTime: string
    departmentName: string
    professionalTypeName: string
    groupLeaderName: string
    deputyLeaderName: string
    responsibilityName: string
    issueName: string
    moduleName: string
    rectificationDeadline: string
    createTime: string
  }

  const __sfc__ = defineComponent({
    data() {
      return {
        id: '',
        detail: null as MonthlyDetail | null,
        loading: true,
        containerHeight: 0,
        problemImages: [] as string[],
        rectificationImages: [] as string[],
        reviewImages: [] as string[]
      }
    },
    onLoad(options: UTSJSONObject) {
      this.id = options.getString('id') ?? ''
      this.initContainer()
      this.loadDetail()
    },
    methods: {
      initContainer() {
        uni.getSystemInfo({
          success: (res) => { this.containerHeight = res.windowHeight }
        })
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
              const d = data.getJSON('data')
              if (d != null) {
                this.detail = {
                  id: d.getString('id') ?? '',
                  recordStatus: d.getNumber('recordStatus') ?? 0,
                  problemDescription: d.getString('problemDescription') ?? '',
                  responsibleUnit: d.getString('responsibleUnit') ?? '',
                  responsiblePerson: d.getString('responsiblePerson') ?? '',
                  qualityName: d.getString('qualityName') ?? '',
                  safetyScore: d.getNumber('safetyScore') ?? 0,
                  location: d.getString('location') ?? '',
                  inspector: d.getString('inspector') ?? '',
                  rectificationEffect: (d.getString('rectificationEffect') ?? d.getString('rectificationSuggestion') ?? ''),
                  reviewEffect: (d.getString('reviewEffect') ?? d.getString('reviewSuggestion') ?? ''),
                  photos: d.getString('photos') ?? '',
                  rectificationImages: d.getString('rectificationImages') ?? '',
                  reviewImages: d.getString('reviewImages') ?? '',
                  taskNo: d.getString('taskNo') ?? '',
                  inspectionTime: d.getString('inspectionTime') ?? '',
                  departmentName: d.getString('departmentName') ?? '',
                  professionalTypeName: d.getString('professionalTypeName') ?? '',
                  groupLeaderName: d.getString('groupLeaderName') ?? '',
                  deputyLeaderName: d.getString('deputyLeaderName') ?? '',
                  responsibilityName: d.getString('responsibilityName') ?? '',
                  issueName: d.getString('issueName') ?? '',
                  moduleName: d.getString('moduleName') ?? '',
                  rectificationDeadline: d.getString('rectificationDeadline') ?? '',
                  createTime: d.getString('createTime') ?? ''
                }
                this.problemImages = this.parseImages(this.detail.photos)
                this.rectificationImages = this.parseImages(this.detail.rectificationImages)
                this.reviewImages = this.parseImages(this.detail.reviewImages)
              }
            }
            this.loading = false
          },
          fail: (_) => { this.loading = false }
        })
      },
      parseImages(s: string): string[] {
        if (s == null || s.trim() === '') return []
        try {
          if (s.trim().startsWith('[') && s.trim().endsWith(']')) {
            const arr = JSON.parse(s) as string[]
            return arr.map(u => this.toUrl(u)).filter(u => u !== '')
          }
        } catch (_) {}
        return s.split(',').map(u => u.trim()).filter(u => u !== '').map(u => this.toUrl(u))
      },
      toUrl(p: string): string {
        if (p == null || p.trim() === '') return ''
        if (p.startsWith('http://') || p.startsWith('https://')) return p
        return `${apiConfig.baseUrl}/api${p}`
      },
      preview(current: string, urls: string[]) {
        uni.previewImage({ current, urls })
      },
      getStatusText(s: number): string {
        if (s === 0) return '待整改'
        if (s === 1) return '已整改'
        if (s === 2) return '已复查'
        return '未知'
      },
      getStatusClass(s: number): string {
        if (s === 0) return 'status-pending'
        if (s === 1) return 'status-completed'
        if (s === 2) return 'status-reviewed'
        return 'status-unknown'
      }
    }
  })

export default __sfc__
function GenPagesMonthlyViewRender(this: InstanceType<typeof __sfc__>): any | null {
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
          class: "loading"
        }), [
          _cE("text", null, "加载中...")
        ])
      : _ctx.detail != null
        ? _cE("view", _uM({
            key: 1,
            class: "content"
          }), [
            _cE("view", _uM({ class: "header" }), [
              _cE("text", _uM({ class: "title" }), "月检记录详情"),
              _cE("view", _uM({
                class: _nC(["status", _ctx.getStatusClass(_ctx.detail.recordStatus)])
              }), [
                _cE("text", null, _tD(_ctx.getStatusText(_ctx.detail.recordStatus)), 1 /* TEXT */)
              ], 2 /* CLASS */)
            ]),
            _ctx.problemImages.length > 0
              ? _cE("view", _uM({
                  key: 0,
                  class: "photos"
                }), [
                  _cE("text", _uM({ class: "label" }), "问题图片"),
                  _cE("view", _uM({ class: "image-list" }), [
                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.problemImages, (img, idx, __index, _cached): any => {
                      return _cE("image", _uM({
                        key: idx,
                        src: img,
                        class: "photo",
                        mode: "aspectFill",
                        onClick: () => {_ctx.preview(img, _ctx.problemImages)}
                      }), null, 8 /* PROPS */, ["src", "onClick"])
                    }), 128 /* KEYED_FRAGMENT */)
                  ])
                ])
              : _cC("v-if", true),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "整改效果"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.rectificationEffect), 1 /* TEXT */)
            ]),
            _ctx.rectificationImages.length > 0
              ? _cE("view", _uM({
                  key: 1,
                  class: "photos"
                }), [
                  _cE("text", _uM({ class: "label" }), "整改图片"),
                  _cE("view", _uM({ class: "image-list" }), [
                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectificationImages, (img, idx, __index, _cached): any => {
                      return _cE("image", _uM({
                        key: idx,
                        src: img,
                        class: "photo",
                        mode: "aspectFill",
                        onClick: () => {_ctx.preview(img, _ctx.rectificationImages)}
                      }), null, 8 /* PROPS */, ["src", "onClick"])
                    }), 128 /* KEYED_FRAGMENT */)
                  ])
                ])
              : _cC("v-if", true),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "复查效果"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.reviewEffect), 1 /* TEXT */)
            ]),
            _ctx.reviewImages.length > 0
              ? _cE("view", _uM({
                  key: 2,
                  class: "photos"
                }), [
                  _cE("text", _uM({ class: "label" }), "复查图片"),
                  _cE("view", _uM({ class: "image-list" }), [
                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.reviewImages, (img, idx, __index, _cached): any => {
                      return _cE("image", _uM({
                        key: idx,
                        src: img,
                        class: "photo",
                        mode: "aspectFill",
                        onClick: () => {_ctx.preview(img, _ctx.reviewImages)}
                      }), null, 8 /* PROPS */, ["src", "onClick"])
                    }), 128 /* KEYED_FRAGMENT */)
                  ])
                ])
              : _cC("v-if", true),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "问题描述"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.problemDescription), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "任务编号"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.taskNo), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "月检时间"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.inspectionTime), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "模块"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.moduleName), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "所属部门"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.departmentName), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "检查专业"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.professionalTypeName), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "检查组长"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.groupLeaderName), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "检查副组长"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.deputyLeaderName), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "责任单位"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.responsibleUnit), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "责任人"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.responsiblePerson), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "性质界定"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.qualityName), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "安全记分"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.safetyScore), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "责任要素"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.responsibilityName), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "检查内容"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.issueName), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "发生部位"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.location), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "整改期限"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.rectificationDeadline), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "检查人"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.inspector), 1 /* TEXT */)
            ]),
            _cE("view", _uM({ class: "item" }), [
              _cE("text", _uM({ class: "label" }), "创建时间"),
              _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.createTime), 1 /* TEXT */)
            ])
          ])
        : _cE("view", _uM({
            key: 2,
            class: "empty"
          }), [
            _cE("text", null, "记录不存在")
          ])
  ], 4 /* STYLE */)
}
const GenPagesMonthlyViewStyles = [_uM([["container", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#f5f5f5"]]))], ["loading", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["textAlign", "center"]]))], ["content", _pS(_uM([["paddingTop", 16], ["paddingRight", 16], ["paddingBottom", 16], ["paddingLeft", 16]]))], ["header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["marginBottom", 12]]))], ["title", _pS(_uM([["fontSize", 18], ["fontWeight", "bold"]]))], ["status", _pS(_uM([["paddingTop", 4], ["paddingRight", 8], ["paddingBottom", 4], ["paddingLeft", 8], ["borderTopLeftRadius", 4], ["borderTopRightRadius", 4], ["borderBottomRightRadius", 4], ["borderBottomLeftRadius", 4]]))], ["status-pending", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#fff7e6"], ["color", "#d48806"]]))], ["status-completed", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#f6ffed"], ["color", "#389e0d"]]))], ["status-reviewed", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#e6f7ff"], ["color", "#096dd9"]]))], ["status-unknown", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#f0f0f0"], ["color", "#666666"]]))], ["item", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["marginBottom", 8], ["paddingTop", 12], ["paddingRight", 12], ["paddingBottom", 12], ["paddingLeft", 12], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8]]))], ["label", _pS(_uM([["fontSize", 14], ["color", "#666666"], ["marginBottom", 6]]))], ["value", _pS(_uM([["fontSize", 14], ["color", "#333333"]]))], ["photos", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["marginBottom", 8], ["paddingTop", 12], ["paddingRight", 12], ["paddingBottom", 12], ["paddingLeft", 12], ["borderTopLeftRadius", 8], ["borderTopRightRadius", 8], ["borderBottomRightRadius", 8], ["borderBottomLeftRadius", 8]]))], ["image-list", _pS(_uM([["display", "flex"], ["flexWrap", "wrap"], ["gap", "8px"]]))], ["photo", _pS(_uM([["width", 100], ["height", 100], ["borderTopLeftRadius", 6], ["borderTopRightRadius", 6], ["borderBottomRightRadius", 6], ["borderBottomLeftRadius", 6]]))]])]
