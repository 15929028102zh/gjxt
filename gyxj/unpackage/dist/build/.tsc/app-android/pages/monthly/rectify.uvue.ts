
  import { getApiUrl, apiConfig } from '@/config/api.uts'
  import { resolveCurrentDept, getCachedUserInfo, CurrentDept } from '@/utils/user.uts'

  // V3:当前用户类型(对齐 Web:整改按钮只看 deptType,与 admin 无关)
  type CurrentUser = {
    deptId: string
    deptName: string
    deptType: number | null  // 0=运行部门 1=管理部门 null=无部门(如管理员)
  }

  // 多选下拉选项(对齐 task-record.uvue 的 picker 实现)
  type OptionItem = {
    id: string
    name: string
  }

  type RecordDetail = {
    id: string
    problemDescription: string
    location: string
    responsibleUnit: string
    responsiblePerson: string
    qualityName: string
    checkUnitName: string
    natureModified: number
    professionalTypeName: string
    departmentName: string
    inspector: string
    rectificationEffect: string
    rectificationSuggestion: string
    rectificationImages: string
    photos: string
    responsibilityName: string
    issueName: string
    moduleName: string
    rectificationDeadline: string
    // V3:用于拉 task 详情计算 phase
    taskId: string
  }

  const __sfc__ = defineComponent({
    data() {
      return {
        id: '',
        detail: null as RecordDetail | null,
        rectificationSuggestion: '',
        rectificationEffect: '',
        rectificationImages: [] as string[],
        problemImages: [] as string[],
        loading: true,
        saving: false,
        containerHeight: 0,
        // V3:当前用户(用于部门类型判断)
        currentUser: { deptId: '', deptName: '', deptType: null } as CurrentUser,
        // 整改人：当前登录用户姓名（只读）
        currentUserName: '',
        // V3:任务期阶段 0=未知 1=录入前 2=录入中 3=公示期 4=公示期后
        taskPhase: 0,
        taskPublicityEndTime: '',
        // 责任人/责任单位(可多选,对齐 Web SearchableSelect multiple)
        responsiblePersonArr: [] as string[],
        responsibleUnitArr: [] as string[],
        // 下拉选项与弹窗状态
        userOptions: [] as OptionItem[],
        deptOptionsList: [] as OptionItem[],
        showPersonPopup: false,
        showUnitPopup: false,
        personSearchKeyword: '',
        unitSearchKeyword: ''
      }
    },
    onLoad(options: UTSJSONObject) {
      this.id = options.getString('id') ?? ''
      this.loadDetail().then(() => {
        // V3:详情加载完成后,拉当前用户 + 任务 phase(用于按钮可见性判断)
        this.loadCurrentUser()
        this.loadTaskPhase()
      })
    },
    onReady() {
      this.initContainerHeight()
    },
    computed: {
      // 过滤后的责任人选项(按姓名)
      filteredUserOptions(): OptionItem[] {
        if (this.personSearchKeyword == '') return this.userOptions
        return this.userOptions.filter(item => item.name.toLowerCase().includes(this.personSearchKeyword.toLowerCase()))
      },
      // 过滤后的责任单位选项(按部门名)
      filteredDeptOptions(): OptionItem[] {
        if (this.unitSearchKeyword == '') return this.deptOptionsList
        return this.deptOptionsList.filter(item => item.name.toLowerCase().includes(this.unitSearchKeyword.toLowerCase()))
      }
    },
    methods: {
      initContainerHeight() {
        uni.getSystemInfo({
          success: (res) => { this.containerHeight = res.windowHeight }
        })
      },
      convertToHttpUrl(path: string): string {
        if (path == null || path == '') return ''
        if (path.startsWith('http://') || path.startsWith('https://')) return path
        return `${apiConfig.baseUrl}/api${path}`
      },
      getDeductionScore(qualityName: string): string {
        if (qualityName === '观察') return '1'
        if (qualityName === '一般') return '3'
        if (qualityName === '重要') return '5'
        return '-'
      },
      displayValue(v: string): string {
        return v !== '' && v != null ? v : '暂无'
      },
      loadDetail(): Promise<void> {
        return new Promise((resolve) => {
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
                    responsibleUnit: d!!.getString('responsibleUnit') ?? '',
                    responsiblePerson: d!!.getString('responsiblePerson') ?? '',
                    qualityName: d!!.getString('qualityName') ?? '',
                    checkUnitName: d!!.getString('checkUnitName') ?? '',
                    natureModified: d!!.getNumber('natureModified') ?? 0,
                    professionalTypeName: d!!.getString('professionalTypeName') ?? '',
                    departmentName: d!!.getString('departmentName') ?? '',
                    inspector: d!!.getString('inspector') ?? '',
                    rectificationEffect: d!!.getString('rectificationEffect') ?? '',
                    rectificationSuggestion: d!!.getString('rectificationSuggestion') ?? '',
                    rectificationImages: d!!.getString('rectificationImages') ?? '',
                    photos: d!!.getString('photos') ?? '',
                    responsibilityName: d!!.getString('responsibilityName') ?? '',
                    issueName: d!!.getString('issueName') ?? '',
                    moduleName: d!!.getString('moduleName') ?? '',
                    rectificationDeadline: d!!.getString('rectificationDeadline') ?? '',
                    taskId: d!!.getString('taskId') ?? ''
                  }
                  if (this.detail.photos != null && this.detail.photos.trim() !== '') {
                    this.problemImages = this.processImageUrls(this.detail.photos)
                  }
                  // 责任人/单位回显(「、」/逗号分隔字符串 -> 数组,对齐 Web splitMulti)
                  this.responsiblePersonArr = this.splitMulti(this.detail.responsiblePerson)
                  this.responsibleUnitArr = this.splitMulti(this.detail.responsibleUnit)
                }
              } else {
                uni.showToast({ title: data.getString('message') ?? '加载失败', icon: 'none' })
              }
              this.loading = false
              resolve()
            },
            fail: (_) => {
              this.loading = false
              uni.showToast({ title: '网络错误', icon: 'none' })
              resolve()
            }
          })
        })
      },
      // V3:加载当前用户(取 deptType 用于判断整改按钮可见性)
      // resolveCurrentDept 先刷新服务端 userInfo 再取部门类型，避免用登录时的旧 deptId 快照判定角色
      loadCurrentUser() {
        resolveCurrentDept().then((dept: CurrentDept) => {
          this.currentUser = { deptId: dept.deptId, deptName: dept.deptName, deptType: dept.deptType }
          // 整改人取当前登录用户真实姓名
          const info = getCachedUserInfo()
          this.currentUserName = info.getString('realName') ?? info.getString('username') ?? ''
        })
      },
      // V3:从 taskId 拉任务,计算当前阶段(公示期前/中/后)
      loadTaskPhase() {
        if (this.detail == null) return
        const token = uni.getStorageSync('token')
        uni.request({
          url: getApiUrl(`/monthly-inspection-task/${this.detail.taskId ?? ''}`),
          method: 'GET',
          header: { 'Authorization': `Bearer ${token}` },
          success: (res) => {
            const data = res.data as UTSJSONObject
            if (res.statusCode !== 200 || (data.getNumber('code') ?? 0) !== 200) return
            const t = data.getJSON('data')
            if (t == null) return
            const tObj = t as UTSJSONObject
            const pubStartStr = tObj.getString('publicityStartTime') ?? ''
            const pubEndStr = tObj.getString('publicityEndTime') ?? ''
            this.taskPublicityEndTime = pubEndStr
            const now = new Date()
            const parseDt = (s: string): Date | null => {
              if (s == null || s == '') return null
              return new Date(s.replace(' ', 'T'))
            }
            const pubStart = parseDt(pubStartStr)
            const pubEnd = parseDt(pubEndStr)
            if (pubStart == null || pubEnd == null) { this.taskPhase = 0; return }
            if (now < pubStart) this.taskPhase = 2       // 公示期前
            else if (now <= pubEnd) this.taskPhase = 3  // 公示期内
            else this.taskPhase = 4                    // 公示期后
          }
        })
      },
      // V3:整改按钮可见性 — 公示期后 AND deptType !== 1(对齐 Web showRectificationButton)
      isRectifyVisible(): boolean {
        if (this.taskPhase !== 4) return false
        return this.currentUser.deptType !== 1
      },
      processImageUrls(imageUrls: string): string[] {
        if (imageUrls == null || imageUrls.trim() === '') {
          return []
        }
        try {
          const trimmed = imageUrls.trim()
          if (trimmed.startsWith('[') && trimmed.endsWith(']')) {
            const parsed = JSON.parse(trimmed) as string[]
            const result: string[] = []
            for (let i = 0; i < parsed.length; i++) {
              const u = parsed[i]
              if (u != null && u.trim() !== '') {
                result.push(u)
              }
            }
            return result
          }
        } catch (_) {}
        const parts = imageUrls.split(',')
        const res: string[] = []
        for (let i = 0; i < parts.length; i++) {
          const u = parts[i].trim()
          if (u !== '') res.push(u)
        }
        return res
      },
      previewProblemImage(i: number) {
        const urls = this.problemImages.map((u) => this.convertToHttpUrl(u))
        uni.previewImage({ current: i, urls })
      },
      chooseImage() {
        if (this.rectificationImages.length >= 3) {
          uni.showToast({ title: '最多3张', icon: 'none' }); return
        }
        uni.chooseImage({
          count: 3 - this.rectificationImages.length,
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
                    const result = JSON.parse(upRes.data) as UTSJSONObject
                    if (upRes.statusCode === 200 && (result.getNumber('code') ?? 0) === 200) {
                      const dataObj = result.getJSON('data')
                      const url = dataObj?.getString('filePath') ?? ''
                      if (url != '') this.rectificationImages.push(url)
                    } else {
                      uni.showToast({ title: result.getString('message') ?? '上传失败', icon: 'none' })
                    }
                  } catch (_) { uni.showToast({ title: '上传失败', icon: 'none' }) }
                },
                fail: (_) => { uni.showToast({ title: '上传失败', icon: 'none' }) }
              })
            })
          }
        })
      },
      removeImage(i: number) { this.rectificationImages.splice(i, 1) },
      previewImage(i: number) {
        uni.previewImage({ current: i, urls: this.rectificationImages.map((u) => this.convertToHttpUrl(u)) })
      },
      // ===== 责任人/责任单位多选(对齐 Web RectificationConfirmDialog) =====
      // 「、」/逗号分隔字符串 -> 数组(空值返回 [])
      splitMulti(value: string): string[] {
        if (value == null || value == '') return [] as string[]
        const parts = value.replace(/，/g, '、').replace(/,/g, '、').split('、')
        const res: string[] = []
        for (let i = 0; i < parts.length; i++) {
          const s = parts[i].trim()
          if (s != '') res.push(s)
        }
        return res
      },
      // 数组 -> 「、」拼接字符串
      joinMulti(arr: string[]): string {
        const res: string[] = []
        for (let i = 0; i < arr.length; i++) {
          const s = arr[i]
          if (s != null && s.trim() != '') res.push(s)
        }
        return res.join('、')
      },
      // 加载责任人选项(用户列表,取 realName)
      loadUserOptions() {
        const token = uni.getStorageSync('token')
        uni.request({
          url: getApiUrl('/user/list'),
          method: 'GET',
          header: { 'Authorization': `Bearer ${token}` },
          success: (res) => {
            const respdata = res.data as UTSJSONObject
            if ((respdata.getNumber('code') ?? 0) === 200) {
              const records = respdata.getArray('data') as UTSJSONObject[] | null
              const options: OptionItem[] = []
              if (records != null) {
                for (let i = 0; i < records.length; i++) {
                  const u = records[i]
                  const name = (u != null ? (u.getString('realName') ?? u.getString('username') ?? '') : '')
                  if (name != '') options.push({ id: (i + 1).toString(), name: name } as OptionItem)
                }
              }
              this.userOptions = options
            }
          },
          fail: (error) => { console.error('加载责任人列表失败:', error) }
        })
      },
      // 加载责任单位选项(全部部门,取 deptName,对齐 Web getDeptList)
      loadDeptOptionsList() {
        const token = uni.getStorageSync('token')
        uni.request({
          url: getApiUrl('/dept/list'),
          method: 'GET',
          header: { 'Authorization': `Bearer ${token}` },
          success: (res) => {
            const respdata = res.data as UTSJSONObject
            if ((respdata.getNumber('code') ?? 0) === 200) {
              const list = respdata.getArray('data') as UTSJSONObject[] | null
              const options: OptionItem[] = []
              if (list != null) {
                for (let i = 0; i < list.length; i++) {
                  const dp = list[i]
                  const id = dp != null ? (dp.getString('id') ?? '') : ''
                  const name = dp != null ? (dp.getString('deptName') ?? '') : ''
                  if (id != '' && name != '') options.push({ id: id, name: name } as OptionItem)
                }
              }
              this.deptOptionsList = options
            }
          },
          fail: (error) => { console.error('加载责任单位列表失败:', error) }
        })
      },
      showPersonPicker() {
        if (this.userOptions.length === 0) this.loadUserOptions()
        this.showPersonPopup = true
      },
      hidePersonPicker() {
        this.showPersonPopup = false
        this.personSearchKeyword = ''
      },
      togglePerson(item: OptionItem) {
        const idx = this.responsiblePersonArr.indexOf(item.name)
        if (idx === -1) this.responsiblePersonArr.push(item.name)
        else this.responsiblePersonArr.splice(idx, 1)
      },
      confirmPersonPicker() { this.hidePersonPicker() },
      showUnitPicker() {
        if (this.deptOptionsList.length === 0) this.loadDeptOptionsList()
        this.showUnitPopup = true
      },
      hideUnitPicker() {
        this.showUnitPopup = false
        this.unitSearchKeyword = ''
      },
      toggleUnit(item: OptionItem) {
        const idx = this.responsibleUnitArr.indexOf(item.name)
        if (idx === -1) this.responsibleUnitArr.push(item.name)
        else this.responsibleUnitArr.splice(idx, 1)
      },
      confirmUnitPicker() { this.hideUnitPicker() },
      // 保存草稿:仅保存现有数据,不变更记录状态(对齐 Web handleSave,走通用 update 接口)
      handleSave() {
        if (this.saving) return
        let nm = 0
        if (this.detail != null) { nm = this.detail!!.natureModified }
        this.saving = true
        const token = uni.getStorageSync('token')
        const payload = {
          id: this.id,
          responsiblePerson: this.joinMulti(this.responsiblePersonArr),
          responsibleUnit: this.joinMulti(this.responsibleUnitArr),
          rectificationSuggestion: this.rectificationSuggestion,
          rectificationEffect: this.rectificationEffect,
          rectificationImages: this.rectificationImages.join(','),
          // 回传原值,避免后端把 nature_modified 兜底重置为 0
          natureModified: nm
        }
        uni.request({
          url: getApiUrl('/monthly-inspection-record'),
          method: 'PUT',
          header: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
          data: payload,
          success: (res) => {
            const resp = res.data as UTSJSONObject
            if (res.statusCode === 200 && (resp.getNumber('code') ?? 0) === 200) {
              uni.showToast({ title: '保存成功,状态未变更', icon: 'none' })
            } else {
              uni.showToast({ title: resp.getString('message') ?? '保存失败', icon: 'none' })
            }
            this.saving = false
          },
          fail: (_) => { this.saving = false; uni.showToast({ title: '网络错误', icon: 'none' }) }
        })
      },
      submitRectify() {
        if (this.saving) return
        if (this.rectificationSuggestion.trim() == '') {
          uni.showToast({ title: '请填写整改建议', icon: 'none' }); return
        }
        if (this.rectificationEffect.trim() == '') {
          uni.showToast({ title: '请填写整改效果', icon: 'none' }); return
        }
        this.saving = true
        const token = uni.getStorageSync('token')
        const data = {
          rectificationSuggestion: this.rectificationSuggestion,
          rectificationEffect: this.rectificationEffect,
          rectificationImages: this.rectificationImages.join(','),
          // 责任人/单位多选,「、」拼接(对齐 Web joinMulti)
          responsibleUnit: this.joinMulti(this.responsibleUnitArr),
          responsiblePerson: this.joinMulti(this.responsiblePersonArr),
          // 整改人（当前登录用户）
          rectifierName: this.currentUserName
        }
        uni.request({
          url: getApiUrl(`/monthly-inspection-record/${this.id}/rectify`),
          method: 'PUT',
          header: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
          data: data,
          success: (res) => {
            const resp = res.data as UTSJSONObject
            if (res.statusCode === 200 && (resp.getNumber('code') ?? 0) === 200) {
              uni.showToast({ title: '整改确认成功', icon: 'success' })
              setTimeout(function(){ uni.navigateBack() }, 1200)
            } else {
              uni.showToast({ title: resp.getString('message') ?? '提交失败', icon: 'none' })
            }
            this.saving = false
          },
          fail: (_) => { this.saving = false; uni.showToast({ title: '网络错误', icon: 'none' }) }
        })
      }
    }
  })

export default __sfc__
function GenPagesMonthlyRectifyRender(this: InstanceType<typeof __sfc__>): any | null {
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
                _cE("text", _uM({ class: "label" }), "专业类别"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.professionalTypeName), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "基本模块"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.moduleName), 1 /* TEXT */)
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
                _cE("text", _uM({ class: "label" }), "性质界定"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.qualityName), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "性质界定是否人工修改"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.natureModified === 1 ? '是(已修改)' : '否(未修改)'), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "扣分值"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.getDeductionScore(_ctx.detail.qualityName)), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "发现位置"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.location), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "检查单位"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.displayValue(_ctx.detail.checkUnitName)), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "受检单位"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.displayValue(_ctx.detail.departmentName)), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "检查人"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.inspector), 1 /* TEXT */)
              ]),
              _cE("view", _uM({ class: "field" }), [
                _cE("text", _uM({ class: "label" }), "整改期限"),
                _cE("text", _uM({ class: "value" }), _tD(_ctx.detail.rectificationDeadline), 1 /* TEXT */)
              ])
            ]),
            _ctx.problemImages.length > 0
              ? _cE("view", _uM({
                  key: 0,
                  class: "section"
                }), [
                  _cE("text", _uM({ class: "title" }), "问题图片（" + _tD(_ctx.problemImages.length) + "张）", 1 /* TEXT */),
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
                          onClick: () => {_ctx.previewProblemImage(i)}
                        }), null, 8 /* PROPS */, ["src", "onClick"])
                      ])
                    }), 128 /* KEYED_FRAGMENT */)
                  ])
                ])
              : _cC("v-if", true),
            _cE("view", _uM({ class: "section" }), [
              _cE("text", _uM({ class: "title" }), "整改信息"),
              _cE("view", _uM({ class: "form-item" }), [
                _cE("text", _uM({ class: "form-label-left" }), "责任人"),
                _cE("view", _uM({
                  class: _nC(["custom-picker-full", _uM({disabled: _ctx.userOptions.length === 0})]),
                  onClick: _ctx.showPersonPicker
                }), [
                  _cE("text", _uM({
                    class: _nC(["picker-text-full", _uM({placeholder: _ctx.responsiblePersonArr.length === 0})])
                  }), _tD(_ctx.responsiblePersonArr.length > 0 ? _ctx.responsiblePersonArr.join('、') : '请选择责任人（可多选）'), 3 /* TEXT, CLASS */)
                ], 10 /* CLASS, PROPS */, ["onClick"])
              ]),
              _cE("view", _uM({ class: "form-item" }), [
                _cE("text", _uM({ class: "form-label-left" }), "责任单位"),
                _cE("view", _uM({
                  class: _nC(["custom-picker-full", _uM({disabled: _ctx.deptOptionsList.length === 0})]),
                  onClick: _ctx.showUnitPicker
                }), [
                  _cE("text", _uM({
                    class: _nC(["picker-text-full", _uM({placeholder: _ctx.responsibleUnitArr.length === 0})])
                  }), _tD(_ctx.responsibleUnitArr.length > 0 ? _ctx.responsibleUnitArr.join('、') : '请选择责任单位（可多选）'), 3 /* TEXT, CLASS */)
                ], 10 /* CLASS, PROPS */, ["onClick"])
              ]),
              _cE("view", _uM({ class: "form-item" }), [
                _cE("text", _uM({ class: "form-label-left" }), "整改人"),
                _cE("view", _uM({ class: "custom-picker-full disabled" }), [
                  _cE("text", _uM({ class: "picker-text-full" }), _tD(_ctx.currentUserName !== '' ? _ctx.currentUserName : '当前登录用户'), 1 /* TEXT */)
                ])
              ])
            ]),
            _cE("view", _uM({ class: "section" }), [
              _cE("text", _uM({ class: "title" }), "整改建议"),
              _cE("textarea", _uM({
                class: "textarea",
                modelValue: _ctx.rectificationSuggestion,
                onInput: ($event: UniInputEvent) => {(_ctx.rectificationSuggestion) = $event.detail.value},
                placeholder: "请填写整改建议/措施",
                maxlength: "500"
              }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
            ]),
            _cE("view", _uM({ class: "section" }), [
              _cE("text", _uM({ class: "title" }), "整改效果"),
              _cE("textarea", _uM({
                class: "textarea",
                modelValue: _ctx.rectificationEffect,
                onInput: ($event: UniInputEvent) => {(_ctx.rectificationEffect) = $event.detail.value},
                placeholder: "请填写整改完成后的实际效果",
                maxlength: "500"
              }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
            ]),
            _cE("view", _uM({ class: "section" }), [
              _cE("text", _uM({ class: "title" }), "整改图片（最多3张，已选" + _tD(_ctx.rectificationImages.length) + "张）", 1 /* TEXT */),
              _cE("view", _uM({ class: "image-list" }), [
                _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectificationImages, (img, i, __index, _cached): any => {
                  return _cE("view", _uM({
                    class: "image-item",
                    key: i
                  }), [
                    _cE("image", _uM({
                      src: _ctx.convertToHttpUrl(img),
                      mode: "aspectFill",
                      class: "image",
                      onClick: () => {_ctx.previewImage(i)}
                    }), null, 8 /* PROPS */, ["src", "onClick"]),
                    _cE("view", _uM({
                      class: "delete",
                      onClick: () => {_ctx.removeImage(i)}
                    }), [
                      _cE("text", null, "×")
                    ], 8 /* PROPS */, ["onClick"])
                  ])
                }), 128 /* KEYED_FRAGMENT */),
                _ctx.rectificationImages.length < 3
                  ? _cE("view", _uM({
                      key: 0,
                      class: "add",
                      onClick: _ctx.chooseImage
                    }), [
                      _cE("text", _uM({ class: "add-icon" }), "+"),
                      _cE("text", _uM({ class: "add-text" }), "添加图片")
                    ], 8 /* PROPS */, ["onClick"])
                  : _cC("v-if", true)
              ])
            ]),
            _cE("view", _uM({ class: "footer footer-actions" }), [
              _cE("button", _uM({
                class: _nC(["btn-save", _uM({'btn-save-only': !_ctx.isRectifyVisible()})]),
                onClick: _ctx.handleSave,
                disabled: _ctx.saving
              }), _tD(_ctx.saving ? '处理中...' : '保存'), 11 /* TEXT, CLASS, PROPS */, ["onClick", "disabled"]),
              isTrue(_ctx.isRectifyVisible())
                ? _cE("button", _uM({
                    key: 0,
                    class: "btn-confirm",
                    onClick: _ctx.submitRectify,
                    disabled: _ctx.saving
                  }), _tD(_ctx.saving ? '提交中...' : '确认整改'), 9 /* TEXT, PROPS */, ["onClick", "disabled"])
                : _cC("v-if", true)
            ]),
            isTrue(!_ctx.isRectifyVisible())
              ? _cE("view", _uM({
                  key: 1,
                  class: "footer footer-locked"
                }), [
                  _cE("text", _uM({ class: "locked-text" }), _tD(_ctx.taskPhase === 0 ? '加载任务阶段中...' : _ctx.taskPhase < 4 ? '公示期尚未结束,暂不可确认整改(可先保存)' : '当前部门类型无整改权限'), 1 /* TEXT */)
                ])
              : _cC("v-if", true)
          ])
        : _cE("view", _uM({
            key: 2,
            class: "error"
          }), [
            _cE("text", null, "记录不存在或已删除")
          ]),
    isTrue(_ctx.showPersonPopup)
      ? _cE("view", _uM({
          key: 3,
          class: "popup-overlay",
          onClick: _ctx.hidePersonPicker
        }), [
          _cE("view", _uM({
            class: "popup-content",
            onClick: withModifiers(() => {}, ["stop"])
          }), [
            _cE("view", _uM({ class: "popup-header" }), [
              _cE("text", _uM({ class: "popup-title" }), "选择责任人（可多选）"),
              _cE("text", _uM({
                class: "popup-close",
                onClick: _ctx.hidePersonPicker
              }), "×", 8 /* PROPS */, ["onClick"])
            ]),
            _cE("view", _uM({ class: "popup-search" }), [
              _cE("input", _uM({
                class: "search-input",
                modelValue: _ctx.personSearchKeyword,
                onInput: ($event: UniInputEvent) => {(_ctx.personSearchKeyword) = $event.detail.value},
                placeholder: "搜索责任人..."
              }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
            ]),
            _cE("scroll-view", _uM({
              class: "popup-list",
              "scroll-y": "true"
            }), [
              _ctx.userOptions.length === 0
                ? _cE("view", _uM({
                    key: 0,
                    class: "popup-loading"
                  }), [
                    _cE("text", _uM({ class: "loading-text" }), "加载中...")
                  ])
                : _ctx.filteredUserOptions.length === 0
                  ? _cE("view", _uM({
                      key: 1,
                      class: "popup-empty"
                    }), [
                      _cE("text", _uM({ class: "empty-text" }), "暂无匹配的人员")
                    ])
                  : _cE(Fragment, _uM({ key: 2 }), RenderHelpers.renderList(_ctx.filteredUserOptions, (item, index, __index, _cached): any => {
                      return _cE("view", _uM({
                        key: (item as OptionItem).id,
                        class: _nC(['popup-item', _ctx.responsiblePersonArr.indexOf((item as OptionItem).name) !== -1 ? 'popup-item-selected' : '']),
                        onClick: () => {_ctx.togglePerson(item)}
                      }), [
                        _cE("text", _uM({ class: "popup-item-text" }), _tD((item as OptionItem).name), 1 /* TEXT */),
                        _ctx.responsiblePersonArr.indexOf((item as OptionItem).name) !== -1
                          ? _cE("text", _uM({
                              key: 0,
                              class: "popup-item-check"
                            }), "✓")
                          : _cC("v-if", true)
                      ], 10 /* CLASS, PROPS */, ["onClick"])
                    }), 128 /* KEYED_FRAGMENT */)
            ]),
            _cE("view", _uM({ class: "popup-footer" }), [
              _cE("text", _uM({
                class: "popup-btn popup-btn-cancel",
                onClick: _ctx.hidePersonPicker
              }), "取消", 8 /* PROPS */, ["onClick"]),
              _cE("text", _uM({
                class: "popup-btn popup-btn-confirm",
                onClick: _ctx.confirmPersonPicker
              }), "确定（" + _tD(_ctx.responsiblePersonArr.length) + "）", 9 /* TEXT, PROPS */, ["onClick"])
            ])
          ], 8 /* PROPS */, ["onClick"])
        ], 8 /* PROPS */, ["onClick"])
      : _cC("v-if", true),
    isTrue(_ctx.showUnitPopup)
      ? _cE("view", _uM({
          key: 4,
          class: "popup-overlay",
          onClick: _ctx.hideUnitPicker
        }), [
          _cE("view", _uM({
            class: "popup-content",
            onClick: withModifiers(() => {}, ["stop"])
          }), [
            _cE("view", _uM({ class: "popup-header" }), [
              _cE("text", _uM({ class: "popup-title" }), "选择责任单位（可多选）"),
              _cE("text", _uM({
                class: "popup-close",
                onClick: _ctx.hideUnitPicker
              }), "×", 8 /* PROPS */, ["onClick"])
            ]),
            _cE("view", _uM({ class: "popup-search" }), [
              _cE("input", _uM({
                class: "search-input",
                modelValue: _ctx.unitSearchKeyword,
                onInput: ($event: UniInputEvent) => {(_ctx.unitSearchKeyword) = $event.detail.value},
                placeholder: "搜索部门..."
              }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
            ]),
            _cE("scroll-view", _uM({
              class: "popup-list",
              "scroll-y": "true"
            }), [
              _ctx.deptOptionsList.length === 0
                ? _cE("view", _uM({
                    key: 0,
                    class: "popup-loading"
                  }), [
                    _cE("text", _uM({ class: "loading-text" }), "加载中...")
                  ])
                : _ctx.filteredDeptOptions.length === 0
                  ? _cE("view", _uM({
                      key: 1,
                      class: "popup-empty"
                    }), [
                      _cE("text", _uM({ class: "empty-text" }), "暂无匹配的部门")
                    ])
                  : _cE(Fragment, _uM({ key: 2 }), RenderHelpers.renderList(_ctx.filteredDeptOptions, (item, index, __index, _cached): any => {
                      return _cE("view", _uM({
                        key: (item as OptionItem).id,
                        class: _nC(['popup-item', _ctx.responsibleUnitArr.indexOf((item as OptionItem).name) !== -1 ? 'popup-item-selected' : '']),
                        onClick: () => {_ctx.toggleUnit(item)}
                      }), [
                        _cE("text", _uM({ class: "popup-item-text" }), _tD((item as OptionItem).name), 1 /* TEXT */),
                        _ctx.responsibleUnitArr.indexOf((item as OptionItem).name) !== -1
                          ? _cE("text", _uM({
                              key: 0,
                              class: "popup-item-check"
                            }), "✓")
                          : _cC("v-if", true)
                      ], 10 /* CLASS, PROPS */, ["onClick"])
                    }), 128 /* KEYED_FRAGMENT */)
            ]),
            _cE("view", _uM({ class: "popup-footer" }), [
              _cE("text", _uM({
                class: "popup-btn popup-btn-cancel",
                onClick: _ctx.hideUnitPicker
              }), "取消", 8 /* PROPS */, ["onClick"]),
              _cE("text", _uM({
                class: "popup-btn popup-btn-confirm",
                onClick: _ctx.confirmUnitPicker
              }), "确定（" + _tD(_ctx.responsibleUnitArr.length) + "）", 9 /* TEXT, PROPS */, ["onClick"])
            ])
          ], 8 /* PROPS */, ["onClick"])
        ], 8 /* PROPS */, ["onClick"])
      : _cC("v-if", true)
  ], 4 /* STYLE */)
}
const GenPagesMonthlyRectifyStyles = [_uM([["container", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#f1f5f9"]]))], ["loading-state", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["textAlign", "center"], ["color", "#475569"]]))], ["error", _pS(_uM([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["textAlign", "center"], ["color", "#475569"]]))], ["content", _pS(_uM([["paddingTop", "24rpx"], ["paddingRight", "32rpx"], ["paddingBottom", "24rpx"], ["paddingLeft", "32rpx"]]))], ["section", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "32rpx"], ["paddingRight", "32rpx"], ["paddingBottom", "32rpx"], ["paddingLeft", "32rpx"], ["marginBottom", "28rpx"], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"], ["borderTopWidth", "4rpx"], ["borderTopStyle", "solid"], ["borderTopColor", "#1e40af"]]))], ["title", _pS(_uM([["fontSize", "32rpx"], ["fontWeight", 600], ["color", "#1e40af"], ["marginBottom", "24rpx"], ["paddingLeft", "16rpx"], ["borderLeftWidth", "6rpx"], ["borderLeftStyle", "solid"], ["borderLeftColor", "#2563eb"]]))], ["field", _pS(_uM([["marginBottom", "20rpx"], ["display", "flex"], ["justifyContent", "space-between"], ["paddingTop", "8rpx"], ["paddingRight", 0], ["paddingBottom", "8rpx"], ["paddingLeft", 0]]))], ["label", _uM([["", _uM([["fontSize", "28rpx"], ["color", "#475569"]])], [".field-edit ", _uM([["width", "180rpx"], ["flexShrink", 0], ["color", "#475569"]])], [".readonly-field ", _uM([["minWidth", "176rpx"], ["flexShrink", 0], ["color", "#2563eb"], ["fontWeight", 500]])]])], ["value", _uM([["", _uM([["fontSize", "28rpx"], ["color", "#0f172a"], ["textAlign", "right"], ["maxWidth", 480], ["fontWeight", 500]])], [".readonly-field ", _uM([["textAlign", "left"], ["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["wordBreak", "break-all"], ["color", "#0f172a"]])]])], ["field-edit", _pS(_uM([["marginBottom", "20rpx"], ["display", "flex"], ["alignItems", "center"], ["paddingTop", "8rpx"], ["paddingRight", 0], ["paddingBottom", "8rpx"], ["paddingLeft", 0]]))], ["readonly-field", _pS(_uM([["marginBottom", "20rpx"], ["display", "flex"], ["alignItems", "flex-start"], ["paddingTop", "12rpx"], ["paddingRight", "12rpx"], ["paddingBottom", "12rpx"], ["paddingLeft", "12rpx"], ["backgroundImage", "none"], ["backgroundColor", "#eff6ff"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"]]))], ["textarea", _pS(_uM([["width", "100%"], ["minHeight", "200rpx"], ["paddingTop", "24rpx"], ["paddingRight", "24rpx"], ["paddingBottom", "24rpx"], ["paddingLeft", "24rpx"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["fontSize", "28rpx"], ["color", "#0f172a"], ["borderTopColor:focus", "#2563eb"], ["borderRightColor:focus", "#2563eb"], ["borderBottomColor:focus", "#2563eb"], ["borderLeftColor:focus", "#2563eb"], ["backgroundImage:focus", "none"], ["backgroundColor:focus", "#eff6ff"]]))], ["textarea-mini", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["minHeight", "80rpx"], ["paddingTop", "16rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "16rpx"], ["paddingLeft", "20rpx"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["fontSize", "28rpx"], ["color", "#0f172a"], ["borderTopColor:focus", "#2563eb"], ["borderRightColor:focus", "#2563eb"], ["borderBottomColor:focus", "#2563eb"], ["borderLeftColor:focus", "#2563eb"], ["backgroundImage:focus", "none"], ["backgroundColor:focus", "#eff6ff"]]))], ["footer-locked", _pS(_uM([["textAlign", "center"], ["paddingTop", "40rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "30rpx"]]))], ["locked-text", _pS(_uM([["fontSize", "28rpx"], ["color", "#94a3b8"]]))], ["image-list", _pS(_uM([["display", "flex"], ["flexWrap", "wrap"]]))], ["image-item", _uM([[".image-list ", _uM([["marginTop", "10rpx"], ["marginRight", "10rpx"], ["marginBottom", "10rpx"], ["marginLeft", "10rpx"]])], ["", _uM([["position", "relative"], ["width", "200rpx"], ["height", "200rpx"]])]])], ["add", _uM([[".image-list ", _uM([["marginTop", "10rpx"], ["marginRight", "10rpx"], ["marginBottom", "10rpx"], ["marginLeft", "10rpx"]])], ["", _uM([["width", "200rpx"], ["height", "200rpx"], ["borderTopWidth", "4rpx"], ["borderRightWidth", "4rpx"], ["borderBottomWidth", "4rpx"], ["borderLeftWidth", "4rpx"], ["borderTopStyle", "dashed"], ["borderRightStyle", "dashed"], ["borderBottomStyle", "dashed"], ["borderLeftStyle", "dashed"], ["borderTopColor", "#2563eb"], ["borderRightColor", "#2563eb"], ["borderBottomColor", "#2563eb"], ["borderLeftColor", "#2563eb"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"], ["backgroundImage", "none"], ["backgroundColor", "#eff6ff"]])]])], ["no-image", _uM([[".image-list ", _uM([["marginTop", "10rpx"], ["marginRight", "10rpx"], ["marginBottom", "10rpx"], ["marginLeft", "10rpx"]])], ["", _uM([["fontSize", "24rpx"], ["color", "#94a3b8"]])]])], ["image", _pS(_uM([["width", "100%"], ["height", "100%"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"]]))], ["delete", _pS(_uM([["position", "absolute"], ["top", "-12rpx"], ["right", "-12rpx"], ["width", "44rpx"], ["height", "44rpx"], ["backgroundImage", "none"], ["backgroundColor", "#dc2626"], ["borderTopLeftRadius", "50%"], ["borderTopRightRadius", "50%"], ["borderBottomRightRadius", "50%"], ["borderBottomLeftRadius", "50%"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["color", "#ffffff"], ["fontWeight", 600]]))], ["add-icon", _pS(_uM([["fontSize", "44rpx"], ["color", "#2563eb"], ["fontWeight", 600]]))], ["add-text", _pS(_uM([["fontSize", "24rpx"], ["color", "#2563eb"], ["marginTop", "8rpx"]]))], ["footer", _pS(_uM([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["boxShadow", "0 8rpx 24rpx rgba(30, 64, 175, 0.08)"]]))], ["submit", _pS(_uM([["width", "100%"], ["paddingTop", "24rpx"], ["paddingRight", "24rpx"], ["paddingBottom", "24rpx"], ["paddingLeft", "24rpx"], ["backgroundImage", "none"], ["backgroundColor", "#1e40af"], ["color", "#ffffff"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["fontSize", "32rpx"], ["fontWeight", 600], ["color::after", "#ffffff"], ["borderTopWidth::after", "medium"], ["borderRightWidth::after", "medium"], ["borderBottomWidth::after", "medium"], ["borderLeftWidth::after", "medium"], ["borderTopStyle::after", "none"], ["borderRightStyle::after", "none"], ["borderBottomStyle::after", "none"], ["borderLeftStyle::after", "none"], ["borderTopColor::after", "#000000"], ["borderRightColor::after", "#000000"], ["borderBottomColor::after", "#000000"], ["borderLeftColor::after", "#000000"]]))], ["form-item", _pS(_uM([["marginBottom", "24rpx"]]))], ["form-label-left", _pS(_uM([["fontSize", "28rpx"], ["color", "#475569"], ["marginBottom", "12rpx"], ["fontWeight", 500], ["textAlign", "left"]]))], ["custom-picker-full", _uM([["", _uM([["width", "100%"], ["paddingTop", "24rpx"], ["paddingRight", "24rpx"], ["paddingBottom", "24rpx"], ["paddingLeft", "24rpx"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e2e8f0"], ["borderRightColor", "#e2e8f0"], ["borderBottomColor", "#e2e8f0"], ["borderLeftColor", "#e2e8f0"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["backgroundColor", "#ffffff"], ["minHeight", "88rpx"], ["display", "flex"], ["alignItems", "center"]])], [".disabled", _uM([["backgroundImage", "none"], ["backgroundColor", "#f1f5f9"], ["borderTopColor", "#e2e8f0"], ["borderRightColor", "#e2e8f0"], ["borderBottomColor", "#e2e8f0"], ["borderLeftColor", "#e2e8f0"], ["opacity", 0.6]])]])], ["picker-text-full", _uM([["", _uM([["fontSize", "28rpx"], ["color", "#0f172a"], ["width", "100%"], ["textAlign", "left"]])], [".placeholder", _uM([["color", "#94a3b8"]])]])], ["footer-actions", _pS(_uM([["display", "flex"], ["flexDirection", "row"]]))], ["btn-save", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["paddingTop", "24rpx"], ["paddingRight", "24rpx"], ["paddingBottom", "24rpx"], ["paddingLeft", "24rpx"], ["backgroundImage", "none"], ["backgroundColor", "#64748b"], ["color", "#ffffff"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["fontSize", "30rpx"], ["fontWeight", 600], ["marginRight", "20rpx"], ["borderTopWidth::after", "medium"], ["borderRightWidth::after", "medium"], ["borderBottomWidth::after", "medium"], ["borderLeftWidth::after", "medium"], ["borderTopStyle::after", "none"], ["borderRightStyle::after", "none"], ["borderBottomStyle::after", "none"], ["borderLeftStyle::after", "none"], ["borderTopColor::after", "#000000"], ["borderRightColor::after", "#000000"], ["borderBottomColor::after", "#000000"], ["borderLeftColor::after", "#000000"]]))], ["btn-save-only", _pS(_uM([["marginRight", 0]]))], ["btn-confirm", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["paddingTop", "24rpx"], ["paddingRight", "24rpx"], ["paddingBottom", "24rpx"], ["paddingLeft", "24rpx"], ["backgroundImage", "none"], ["backgroundColor", "#1e40af"], ["color", "#ffffff"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["fontSize", "30rpx"], ["fontWeight", 600], ["borderTopWidth::after", "medium"], ["borderRightWidth::after", "medium"], ["borderBottomWidth::after", "medium"], ["borderLeftWidth::after", "medium"], ["borderTopStyle::after", "none"], ["borderRightStyle::after", "none"], ["borderBottomStyle::after", "none"], ["borderLeftStyle::after", "none"], ["borderTopColor::after", "#000000"], ["borderRightColor::after", "#000000"], ["borderBottomColor::after", "#000000"], ["borderLeftColor::after", "#000000"]]))], ["popup-overlay", _pS(_uM([["!position", "fixed"], ["!top", 0], ["!left", 0], ["!width", "100%"], ["!height", "100%"], ["backgroundColor", "rgba(10,37,64,0.55)"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["!zIndex", 9999], ["pointerEvents", "auto"]]))], ["popup-content", _pS(_uM([["!width", "90%"], ["maxWidth", "600rpx"], ["maxHeight", "900rpx"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "24rpx"], ["borderTopRightRadius", "24rpx"], ["borderBottomRightRadius", "24rpx"], ["borderBottomLeftRadius", "24rpx"], ["overflow", "hidden"], ["boxShadow", "0 24rpx 64rpx rgba(10, 37, 64, 0.35)"], ["position", "relative"], ["zIndex", 10000]]))], ["popup-header", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", "28rpx"], ["paddingRight", "32rpx"], ["paddingBottom", "28rpx"], ["paddingLeft", "32rpx"], ["borderBottomWidth", "2rpx"], ["borderBottomStyle", "solid"], ["borderBottomColor", "#e2e8f0"], ["backgroundImage", "linear-gradient(135deg, #0a2540 0%, #1e40af 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["position", "relative"], ["color", "#ffffff"]]))], ["popup-title", _pS(_uM([["fontSize", "30rpx"], ["fontWeight", 700], ["color", "#ffffff"], ["letterSpacing", "1rpx"]]))], ["popup-close", _pS(_uM([["fontSize", "44rpx"], ["color", "rgba(255,255,255,0.85)"], ["fontWeight", 300], ["width", "60rpx"], ["height", "60rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["borderTopLeftRadius", "30rpx"], ["borderTopRightRadius", "30rpx"], ["borderBottomRightRadius", "30rpx"], ["borderBottomLeftRadius", "30rpx"]]))], ["popup-search", _pS(_uM([["paddingTop", "24rpx"], ["paddingRight", "32rpx"], ["paddingBottom", "24rpx"], ["paddingLeft", "32rpx"], ["borderBottomWidth", "2rpx"], ["borderBottomStyle", "solid"], ["borderBottomColor", "#e2e8f0"], ["backgroundColor", "#f8fafc"]]))], ["search-input", _pS(_uM([["width", "100%"], ["height", "72rpx"], ["paddingTop", 0], ["paddingRight", "24rpx"], ["paddingBottom", 0], ["paddingLeft", "24rpx"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#dbeafe"], ["borderRightColor", "#dbeafe"], ["borderBottomColor", "#dbeafe"], ["borderLeftColor", "#dbeafe"], ["borderTopLeftRadius", "36rpx"], ["borderTopRightRadius", "36rpx"], ["borderBottomRightRadius", "36rpx"], ["borderBottomLeftRadius", "36rpx"], ["fontSize", "28rpx"], ["backgroundColor", "#ffffff"], ["color", "#0f172a"]]))], ["popup-list", _pS(_uM([["maxHeight", "520rpx"], ["overflowY", "auto"]]))], ["popup-empty", _pS(_uM([["paddingTop", "80rpx"], ["paddingRight", "32rpx"], ["paddingBottom", "80rpx"], ["paddingLeft", "32rpx"], ["textAlign", "center"]]))], ["popup-loading", _pS(_uM([["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"], ["paddingTop", "80rpx"], ["paddingRight", "32rpx"], ["paddingBottom", "80rpx"], ["paddingLeft", "32rpx"]]))], ["empty-text", _pS(_uM([["fontSize", "28rpx"], ["color", "#94a3b8"]]))], ["loading-text", _pS(_uM([["fontSize", "28rpx"], ["color", "#94a3b8"]]))], ["popup-item", _pS(_uM([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["paddingTop", "28rpx"], ["paddingRight", "32rpx"], ["paddingBottom", "28rpx"], ["paddingLeft", "32rpx"], ["borderBottomWidth", "2rpx"], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f1f5f9"]]))], ["popup-item-selected", _pS(_uM([["backgroundColor", "#dbeafe"], ["borderLeftWidth", "8rpx"], ["borderLeftStyle", "solid"], ["borderLeftColor", "#1e40af"], ["paddingLeft", "24rpx"]]))], ["popup-item-text", _pS(_uM([["fontSize", "28rpx"], ["color", "#0f172a"], ["fontWeight", 500]]))], ["popup-item-check", _pS(_uM([["fontSize", "36rpx"], ["color", "#1e40af"], ["fontWeight", "bold"]]))], ["popup-footer", _pS(_uM([["display", "flex"], ["borderTopWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderTopColor", "#e2e8f0"], ["backgroundColor", "#f8fafc"]]))], ["popup-btn", _pS(_uM([["flexGrow", 1], ["flexShrink", 1], ["flexBasis", "0%"], ["paddingTop", "28rpx"], ["paddingRight", 0], ["paddingBottom", "28rpx"], ["paddingLeft", 0], ["textAlign", "center"], ["fontSize", "28rpx"], ["fontWeight", 500]]))], ["popup-btn-cancel", _pS(_uM([["color", "#475569"], ["borderRightWidth", "2rpx"], ["borderRightStyle", "solid"], ["borderRightColor", "#e2e8f0"]]))], ["popup-btn-confirm", _pS(_uM([["color", "#1e40af"], ["fontWeight", 700]]))]])]
