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
import io.dcloud.uniapp.extapi.chooseImage as uni_chooseImage
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfo as uni_getSystemInfo
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.uploadFile as uni_uploadFile
open class GenPagesMonthlyRectify : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: UTSJSONObject) {
            this.id = options.getString("id") ?: ""
            this.loadDetail().then(fun(){
                this.loadCurrentUser()
                this.loadTaskPhase()
            }
            )
        }
        , __ins)
        onReady(fun() {
            this.initContainerHeight()
        }
        , __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "style" to _nS(_uM("height" to (_ctx.containerHeight + "px")))), _uA(
            if (isTrue(_ctx.loading)) {
                _cE("view", _uM("key" to 0, "class" to "loading-state"), _uA(
                    _cE("text", null, "加载中...")
                ))
            } else {
                if (_ctx.detail != null) {
                    _cE("view", _uM("key" to 1, "class" to "content"), _uA(
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("text", _uM("class" to "title"), "问题信息"),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "问题描述"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.problemDescription), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "专业类别"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.professionalTypeName), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "基本模块"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.moduleName), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "责任要素"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.responsibilityName), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "检查内容"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.issueName), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "性质界定"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.qualityName), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "性质界定是否人工修改"),
                                _cE("text", _uM("class" to "value"), _tD(if (_ctx.detail!!.natureModified === 1) {
                                    "是(已修改)"
                                } else {
                                    "否(未修改)"
                                }), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "扣分值"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.getDeductionScore(_ctx.detail!!.qualityName)), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "发现位置"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.location), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "检查单位"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.checkUnitName)), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "受检单位"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.displayValue(_ctx.detail!!.departmentName)), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "检查人"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.inspector), 1)
                            )),
                            _cE("view", _uM("class" to "field"), _uA(
                                _cE("text", _uM("class" to "label"), "整改期限"),
                                _cE("text", _uM("class" to "value"), _tD(_ctx.detail!!.rectificationDeadline), 1)
                            ))
                        )),
                        if (_ctx.problemImages.length > 0) {
                            _cE("view", _uM("key" to 0, "class" to "section"), _uA(
                                _cE("text", _uM("class" to "title"), "问题图片（" + _tD(_ctx.problemImages.length) + "张）", 1),
                                _cE("view", _uM("class" to "image-list"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.problemImages, fun(img, i, __index, _cached): Any {
                                        return _cE("view", _uM("class" to "image-item", "key" to i), _uA(
                                            _cE("image", _uM("src" to _ctx.convertToHttpUrl(img), "mode" to "aspectFill", "class" to "image", "onClick" to fun(){
                                                _ctx.previewProblemImage(i)
                                            }), null, 8, _uA(
                                                "src",
                                                "onClick"
                                            ))
                                        ))
                                    }), 128)
                                ))
                            ))
                        } else {
                            _cC("v-if", true)
                        },
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("text", _uM("class" to "title"), "整改信息"),
                            _cE("view", _uM("class" to "form-item"), _uA(
                                _cE("text", _uM("class" to "form-label-left"), "责任人"),
                                _cE("view", _uM("class" to _nC(_uA(
                                    "custom-picker-full",
                                    _uM("disabled" to (_ctx.userOptions.length === 0))
                                )), "onClick" to _ctx.showPersonPicker), _uA(
                                    _cE("text", _uM("class" to _nC(_uA(
                                        "picker-text-full",
                                        _uM("placeholder" to (_ctx.responsiblePersonArr.length === 0))
                                    ))), _tD(if (_ctx.responsiblePersonArr.length > 0) {
                                        _ctx.responsiblePersonArr.join("、")
                                    } else {
                                        "请选择责任人（可多选）"
                                    }), 3)
                                ), 10, _uA(
                                    "onClick"
                                ))
                            )),
                            _cE("view", _uM("class" to "form-item"), _uA(
                                _cE("text", _uM("class" to "form-label-left"), "责任单位"),
                                _cE("view", _uM("class" to _nC(_uA(
                                    "custom-picker-full",
                                    _uM("disabled" to (_ctx.deptOptionsList.length === 0))
                                )), "onClick" to _ctx.showUnitPicker), _uA(
                                    _cE("text", _uM("class" to _nC(_uA(
                                        "picker-text-full",
                                        _uM("placeholder" to (_ctx.responsibleUnitArr.length === 0))
                                    ))), _tD(if (_ctx.responsibleUnitArr.length > 0) {
                                        _ctx.responsibleUnitArr.join("、")
                                    } else {
                                        "请选择责任单位（可多选）"
                                    }), 3)
                                ), 10, _uA(
                                    "onClick"
                                ))
                            )),
                            _cE("view", _uM("class" to "form-item"), _uA(
                                _cE("text", _uM("class" to "form-label-left"), "整改人"),
                                _cE("view", _uM("class" to "custom-picker-full disabled"), _uA(
                                    _cE("text", _uM("class" to "picker-text-full"), _tD(if (_ctx.currentUserName !== "") {
                                        _ctx.currentUserName
                                    } else {
                                        "当前登录用户"
                                    }), 1)
                                ))
                            ))
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("text", _uM("class" to "title"), "整改建议"),
                            _cE("textarea", _uM("class" to "textarea", "modelValue" to _ctx.rectificationSuggestion, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.rectificationSuggestion = `$event`.detail.value
                            }, "placeholder" to "请填写整改建议/措施", "maxlength" to "500"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("text", _uM("class" to "title"), "整改效果"),
                            _cE("textarea", _uM("class" to "textarea", "modelValue" to _ctx.rectificationEffect, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.rectificationEffect = `$event`.detail.value
                            }, "placeholder" to "请填写整改完成后的实际效果", "maxlength" to "500"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("view", _uM("class" to "section"), _uA(
                            _cE("text", _uM("class" to "title"), "整改图片（最多3张，已选" + _tD(_ctx.rectificationImages.length) + "张）", 1),
                            _cE("view", _uM("class" to "image-list"), _uA(
                                _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectificationImages, fun(img, i, __index, _cached): Any {
                                    return _cE("view", _uM("class" to "image-item", "key" to i), _uA(
                                        _cE("image", _uM("src" to _ctx.convertToHttpUrl(img), "mode" to "aspectFill", "class" to "image", "onClick" to fun(){
                                            _ctx.previewImage(i)
                                        }), null, 8, _uA(
                                            "src",
                                            "onClick"
                                        )),
                                        _cE("view", _uM("class" to "delete", "onClick" to fun(){
                                            _ctx.removeImage(i)
                                        }), _uA(
                                            _cE("text", null, "×")
                                        ), 8, _uA(
                                            "onClick"
                                        ))
                                    ))
                                }), 128),
                                if (_ctx.rectificationImages.length < 3) {
                                    _cE("view", _uM("key" to 0, "class" to "add", "onClick" to _ctx.chooseImage), _uA(
                                        _cE("text", _uM("class" to "add-icon"), "+"),
                                        _cE("text", _uM("class" to "add-text"), "添加图片")
                                    ), 8, _uA(
                                        "onClick"
                                    ))
                                } else {
                                    _cC("v-if", true)
                                }
                            ))
                        )),
                        _cE("view", _uM("class" to "footer footer-actions"), _uA(
                            _cE("button", _uM("class" to _nC(_uA(
                                "btn-save",
                                _uM("btn-save-only" to !_ctx.isRectifyVisible())
                            )), "onClick" to _ctx.handleSave, "disabled" to _ctx.saving), _tD(if (_ctx.saving) {
                                "处理中..."
                            } else {
                                "保存"
                            }), 11, _uA(
                                "onClick",
                                "disabled"
                            )),
                            if (isTrue(_ctx.isRectifyVisible())) {
                                _cE("button", _uM("key" to 0, "class" to "btn-confirm", "onClick" to _ctx.submitRectify, "disabled" to _ctx.saving), _tD(if (_ctx.saving) {
                                    "提交中..."
                                } else {
                                    "确认整改"
                                }), 9, _uA(
                                    "onClick",
                                    "disabled"
                                ))
                            } else {
                                _cC("v-if", true)
                            }
                        )),
                        if (isTrue(!_ctx.isRectifyVisible())) {
                            _cE("view", _uM("key" to 1, "class" to "footer footer-locked"), _uA(
                                _cE("text", _uM("class" to "locked-text"), _tD(if (_ctx.taskPhase === 0) {
                                    "加载任务阶段中..."
                                } else {
                                    if (_ctx.taskPhase < 4) {
                                        "公示期尚未结束,暂不可确认整改(可先保存)"
                                    } else {
                                        "当前部门类型无整改权限"
                                    }
                                }), 1)
                            ))
                        } else {
                            _cC("v-if", true)
                        }
                    ))
                } else {
                    _cE("view", _uM("key" to 2, "class" to "error"), _uA(
                        _cE("text", null, "记录不存在或已删除")
                    ))
                }
            }
            ,
            if (isTrue(_ctx.showPersonPopup)) {
                _cE("view", _uM("key" to 3, "class" to "popup-overlay", "onClick" to _ctx.hidePersonPicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择责任人（可多选）"),
                            _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hidePersonPicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "popup-search"), _uA(
                            _cE("input", _uM("class" to "search-input", "modelValue" to _ctx.personSearchKeyword, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.personSearchKeyword = `$event`.detail.value
                            }, "placeholder" to "搜索责任人..."), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("scroll-view", _uM("class" to "popup-list", "scroll-y" to "true"), _uA(
                            if (_ctx.userOptions.length === 0) {
                                _cE("view", _uM("key" to 0, "class" to "popup-loading"), _uA(
                                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                                ))
                            } else {
                                if (_ctx.filteredUserOptions.length === 0) {
                                    _cE("view", _uM("key" to 1, "class" to "popup-empty"), _uA(
                                        _cE("text", _uM("class" to "empty-text"), "暂无匹配的人员")
                                    ))
                                } else {
                                    _cE(Fragment, _uM("key" to 2), RenderHelpers.renderList(_ctx.filteredUserOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to (item as OptionItem).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.responsiblePersonArr.indexOf((item as OptionItem).name) !== -1) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.togglePerson(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem).name), 1),
                                            if (_ctx.responsiblePersonArr.indexOf((item as OptionItem).name) !== -1) {
                                                _cE("text", _uM("key" to 0, "class" to "popup-item-check"), "✓")
                                            } else {
                                                _cC("v-if", true)
                                            }
                                        ), 10, _uA(
                                            "onClick"
                                        ))
                                    }), 128)
                                }
                            }
                        )),
                        _cE("view", _uM("class" to "popup-footer"), _uA(
                            _cE("text", _uM("class" to "popup-btn popup-btn-cancel", "onClick" to _ctx.hidePersonPicker), "取消", 8, _uA(
                                "onClick"
                            )),
                            _cE("text", _uM("class" to "popup-btn popup-btn-confirm", "onClick" to _ctx.confirmPersonPicker), "确定（" + _tD(_ctx.responsiblePersonArr.length) + "）", 9, _uA(
                                "onClick"
                            ))
                        ))
                    ), 8, _uA(
                        "onClick"
                    ))
                ), 8, _uA(
                    "onClick"
                ))
            } else {
                _cC("v-if", true)
            }
            ,
            if (isTrue(_ctx.showUnitPopup)) {
                _cE("view", _uM("key" to 4, "class" to "popup-overlay", "onClick" to _ctx.hideUnitPicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择责任单位（可多选）"),
                            _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideUnitPicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "popup-search"), _uA(
                            _cE("input", _uM("class" to "search-input", "modelValue" to _ctx.unitSearchKeyword, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.unitSearchKeyword = `$event`.detail.value
                            }, "placeholder" to "搜索部门..."), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("scroll-view", _uM("class" to "popup-list", "scroll-y" to "true"), _uA(
                            if (_ctx.deptOptionsList.length === 0) {
                                _cE("view", _uM("key" to 0, "class" to "popup-loading"), _uA(
                                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                                ))
                            } else {
                                if (_ctx.filteredDeptOptions.length === 0) {
                                    _cE("view", _uM("key" to 1, "class" to "popup-empty"), _uA(
                                        _cE("text", _uM("class" to "empty-text"), "暂无匹配的部门")
                                    ))
                                } else {
                                    _cE(Fragment, _uM("key" to 2), RenderHelpers.renderList(_ctx.filteredDeptOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to (item as OptionItem).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.responsibleUnitArr.indexOf((item as OptionItem).name) !== -1) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.toggleUnit(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem).name), 1),
                                            if (_ctx.responsibleUnitArr.indexOf((item as OptionItem).name) !== -1) {
                                                _cE("text", _uM("key" to 0, "class" to "popup-item-check"), "✓")
                                            } else {
                                                _cC("v-if", true)
                                            }
                                        ), 10, _uA(
                                            "onClick"
                                        ))
                                    }), 128)
                                }
                            }
                        )),
                        _cE("view", _uM("class" to "popup-footer"), _uA(
                            _cE("text", _uM("class" to "popup-btn popup-btn-cancel", "onClick" to _ctx.hideUnitPicker), "取消", 8, _uA(
                                "onClick"
                            )),
                            _cE("text", _uM("class" to "popup-btn popup-btn-confirm", "onClick" to _ctx.confirmUnitPicker), "确定（" + _tD(_ctx.responsibleUnitArr.length) + "）", 9, _uA(
                                "onClick"
                            ))
                        ))
                    ), 8, _uA(
                        "onClick"
                    ))
                ), 8, _uA(
                    "onClick"
                ))
            } else {
                _cC("v-if", true)
            }
        ), 4)
    }
    open var id: String by `$data`
    open var detail: RecordDetail? by `$data`
    open var rectificationSuggestion: String by `$data`
    open var rectificationEffect: String by `$data`
    open var rectificationImages: UTSArray<String> by `$data`
    open var problemImages: UTSArray<String> by `$data`
    open var loading: Boolean by `$data`
    open var saving: Boolean by `$data`
    open var containerHeight: Number by `$data`
    open var currentUser: CurrentUser__1 by `$data`
    open var currentUserName: String by `$data`
    open var taskPhase: Number by `$data`
    open var taskPublicityEndTime: String by `$data`
    open var responsiblePersonArr: UTSArray<String> by `$data`
    open var responsibleUnitArr: UTSArray<String> by `$data`
    open var userOptions: UTSArray<OptionItem> by `$data`
    open var deptOptionsList: UTSArray<OptionItem> by `$data`
    open var showPersonPopup: Boolean by `$data`
    open var showUnitPopup: Boolean by `$data`
    open var personSearchKeyword: String by `$data`
    open var unitSearchKeyword: String by `$data`
    open var filteredUserOptions: UTSArray<OptionItem> by `$data`
    open var filteredDeptOptions: UTSArray<OptionItem> by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("id" to "", "detail" to null as RecordDetail?, "rectificationSuggestion" to "", "rectificationEffect" to "", "rectificationImages" to _uA<String>(), "problemImages" to _uA<String>(), "loading" to true, "saving" to false, "containerHeight" to 0, "currentUser" to CurrentUser__1(deptId = "", deptName = "", deptType = null), "currentUserName" to "", "taskPhase" to 0, "taskPublicityEndTime" to "", "responsiblePersonArr" to _uA<String>(), "responsibleUnitArr" to _uA<String>(), "userOptions" to _uA<OptionItem>(), "deptOptionsList" to _uA<OptionItem>(), "showPersonPopup" to false, "showUnitPopup" to false, "personSearchKeyword" to "", "unitSearchKeyword" to "", "filteredUserOptions" to computed<UTSArray<OptionItem>>(fun(): UTSArray<OptionItem> {
            if (this.personSearchKeyword == "") {
                return this.userOptions
            }
            return this.userOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.personSearchKeyword.toLowerCase())
            }
            )
        }
        ), "filteredDeptOptions" to computed<UTSArray<OptionItem>>(fun(): UTSArray<OptionItem> {
            if (this.unitSearchKeyword == "") {
                return this.deptOptionsList
            }
            return this.deptOptionsList.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.unitSearchKeyword.toLowerCase())
            }
            )
        }
        ))
    }
    open var initContainerHeight = ::gen_initContainerHeight_fn
    open fun gen_initContainerHeight_fn() {
        uni_getSystemInfo(GetSystemInfoOptions(success = fun(res){
            this.containerHeight = res.windowHeight
        }
        ))
    }
    open var convertToHttpUrl = ::gen_convertToHttpUrl_fn
    open fun gen_convertToHttpUrl_fn(path: String): String {
        if (path == null || path == "") {
            return ""
        }
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path
        }
        return "" + apiConfig.baseUrl + "/api" + path
    }
    open var getDeductionScore = ::gen_getDeductionScore_fn
    open fun gen_getDeductionScore_fn(qualityName: String): String {
        if (qualityName === "观察") {
            return "1"
        }
        if (qualityName === "一般") {
            return "3"
        }
        if (qualityName === "重要") {
            return "5"
        }
        return "-"
    }
    open var displayValue = ::gen_displayValue_fn
    open fun gen_displayValue_fn(v: String): String {
        return if (v !== "" && v != null) {
            v
        } else {
            "暂无"
        }
    }
    open var loadDetail = ::gen_loadDetail_fn
    open fun gen_loadDetail_fn(): UTSPromise<Unit> {
        return UTSPromise(fun(resolve, _reject){
            val token = uni_getStorageSync("token")
            uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/" + this.id), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
                val data = res.data as UTSJSONObject
                if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                    val d = data.getJSON("data") as UTSJSONObject?
                    if (d != null) {
                        this.detail = RecordDetail(id = d!!!!.getString("id") ?: "", problemDescription = d!!!!.getString("problemDescription") ?: "", location = d!!!!.getString("location") ?: "", responsibleUnit = d!!!!.getString("responsibleUnit") ?: "", responsiblePerson = d!!!!.getString("responsiblePerson") ?: "", qualityName = d!!!!.getString("qualityName") ?: "", checkUnitName = d!!!!.getString("checkUnitName") ?: "", natureModified = d!!!!.getNumber("natureModified") ?: 0, professionalTypeName = d!!!!.getString("professionalTypeName") ?: "", departmentName = d!!!!.getString("departmentName") ?: "", inspector = d!!!!.getString("inspector") ?: "", rectificationEffect = d!!!!.getString("rectificationEffect") ?: "", rectificationSuggestion = d!!!!.getString("rectificationSuggestion") ?: "", rectificationImages = d!!!!.getString("rectificationImages") ?: "", photos = d!!!!.getString("photos") ?: "", responsibilityName = d!!!!.getString("responsibilityName") ?: "", issueName = d!!!!.getString("issueName") ?: "", moduleName = d!!!!.getString("moduleName") ?: "", rectificationDeadline = d!!!!.getString("rectificationDeadline") ?: "", taskId = d!!!!.getString("taskId") ?: "")
                        if (this.detail!!.photos != null && this.detail!!.photos.trim() !== "") {
                            this.problemImages = this.processImageUrls(this.detail!!.photos)
                        }
                        this.responsiblePersonArr = this.splitMulti(this.detail!!.responsiblePerson)
                        this.responsibleUnitArr = this.splitMulti(this.detail!!.responsibleUnit)
                    }
                } else {
                    uni_showToast(ShowToastOptions(title = data.getString("message") ?: "加载失败", icon = "none"))
                }
                this.loading = false
                resolve(Unit)
            }
            , fail = fun(_){
                this.loading = false
                uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
                resolve(Unit)
            }
            ))
        }
        )
    }
    open var loadCurrentUser = ::gen_loadCurrentUser_fn
    open fun gen_loadCurrentUser_fn() {
        resolveCurrentDept().then(fun(dept: CurrentDept){
            this.currentUser = CurrentUser__1(deptId = dept.deptId, deptName = dept.deptName, deptType = dept.deptType)
            val info = getCachedUserInfo()
            this.currentUserName = info.getString("realName") ?: info.getString("username") ?: ""
        }
        )
    }
    open var loadTaskPhase = ::gen_loadTaskPhase_fn
    open fun gen_loadTaskPhase_fn() {
        if (this.detail == null) {
            return
        }
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-task/" + (this.detail!!.taskId ?: "")), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode !== 200 || (data.getNumber("code") ?: 0) !== 200) {
                return
            }
            val t = data.getJSON("data")
            if (t == null) {
                return
            }
            val tObj = t as UTSJSONObject
            val pubStartStr = tObj.getString("publicityStartTime") ?: ""
            val pubEndStr = tObj.getString("publicityEndTime") ?: ""
            this.taskPublicityEndTime = pubEndStr
            val now = Date()
            val parseDt = fun(s: String): Date? {
                if (s == null || s == "") {
                    return null
                }
                return Date(s.replace(" ", "T"))
            }
            val pubStart = parseDt(pubStartStr)
            val pubEnd = parseDt(pubEndStr)
            if (pubStart == null || pubEnd == null) {
                this.taskPhase = 0
                return
            }
            if (now < pubStart) {
                this.taskPhase = 2
            } else if (now <= pubEnd) {
                this.taskPhase = 3
            } else {
                this.taskPhase = 4
            }
        }
        ))
    }
    open var isRectifyVisible = ::gen_isRectifyVisible_fn
    open fun gen_isRectifyVisible_fn(): Boolean {
        if (this.taskPhase !== 4) {
            return false
        }
        return this.currentUser.deptType !== 1
    }
    open var processImageUrls = ::gen_processImageUrls_fn
    open fun gen_processImageUrls_fn(imageUrls: String): UTSArray<String> {
        if (imageUrls == null || imageUrls.trim() === "") {
            return _uA()
        }
        try {
            val trimmed = imageUrls.trim()
            if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                val parsed = UTSAndroid.consoleDebugError(JSON.parse(trimmed), " at pages/monthly/rectify.uvue:376") as UTSArray<String>
                val result: UTSArray<String> = _uA()
                run {
                    var i: Number = 0
                    while(i < parsed.length){
                        val u = parsed[i]
                        if (u != null && u.trim() !== "") {
                            result.push(u)
                        }
                        i++
                    }
                }
                return result
            }
        }
         catch (_: Throwable) {}
        val parts = imageUrls.split(",")
        val res: UTSArray<String> = _uA()
        run {
            var i: Number = 0
            while(i < parts.length){
                val u = parts[i].trim()
                if (u !== "") {
                    res.push(u)
                }
                i++
            }
        }
        return res
    }
    open var previewProblemImage = ::gen_previewProblemImage_fn
    open fun gen_previewProblemImage_fn(i: Number) {
        val urls = this.problemImages.map(fun(u): String {
            return this.convertToHttpUrl(u)
        }
        )
        uni_previewImage(PreviewImageOptions(current = i, urls = urls))
    }
    open var chooseImage = ::gen_chooseImage_fn
    open fun gen_chooseImage_fn() {
        if (this.rectificationImages.length >= 3) {
            uni_showToast(ShowToastOptions(title = "最多3张", icon = "none"))
            return
        }
        uni_chooseImage(ChooseImageOptions(count = 3 - this.rectificationImages.length, sourceType = _uA(
            "camera",
            "album"
        ), success = fun(res){
            val token = uni_getStorageSync("token")
            res.tempFilePaths.forEach(fun(p){
                uni_uploadFile(UploadFileOptions(url = getApiUrl("/upload/image"), filePath = p, name = "file", header = _uO("Authorization" to ("Bearer " + token)), success = fun(upRes){
                    try {
                        val result = UTSAndroid.consoleDebugError(JSON.parse(upRes.data), " at pages/monthly/rectify.uvue:416") as UTSJSONObject
                        if (upRes.statusCode === 200 && (result.getNumber("code") ?: 0) === 200) {
                            val dataObj = result.getJSON("data")
                            val url = dataObj?.getString("filePath") ?: ""
                            if (url != "") {
                                this.rectificationImages.push(url)
                            }
                        } else {
                            uni_showToast(ShowToastOptions(title = result.getString("message") ?: "上传失败", icon = "none"))
                        }
                    }
                     catch (_: Throwable) {
                        uni_showToast(ShowToastOptions(title = "上传失败", icon = "none"))
                    }
                }
                , fail = fun(_){
                    uni_showToast(ShowToastOptions(title = "上传失败", icon = "none"))
                }
                ))
            }
            )
        }
        ))
    }
    open var removeImage = ::gen_removeImage_fn
    open fun gen_removeImage_fn(i: Number) {
        this.rectificationImages.splice(i, 1)
    }
    open var previewImage = ::gen_previewImage_fn
    open fun gen_previewImage_fn(i: Number) {
        uni_previewImage(PreviewImageOptions(current = i, urls = this.rectificationImages.map(fun(u): String {
            return this.convertToHttpUrl(u)
        }
        )))
    }
    open var splitMulti = ::gen_splitMulti_fn
    open fun gen_splitMulti_fn(value: String): UTSArray<String> {
        if (value == null || value == "") {
            return _uA<String>()
        }
        val parts = value.replace(UTSRegExp("，", "g"), "、").replace(UTSRegExp(",", "g"), "、").split("、")
        val res: UTSArray<String> = _uA()
        run {
            var i: Number = 0
            while(i < parts.length){
                val s = parts[i].trim()
                if (s != "") {
                    res.push(s)
                }
                i++
            }
        }
        return res
    }
    open var joinMulti = ::gen_joinMulti_fn
    open fun gen_joinMulti_fn(arr: UTSArray<String>): String {
        val res: UTSArray<String> = _uA()
        run {
            var i: Number = 0
            while(i < arr.length){
                val s = arr[i]
                if (s != null && s.trim() != "") {
                    res.push(s)
                }
                i++
            }
        }
        return res.join("、")
    }
    open var loadUserOptions = ::gen_loadUserOptions_fn
    open fun gen_loadUserOptions_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/user/list"), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val respdata = res.data as UTSJSONObject
            if ((respdata.getNumber("code") ?: 0) === 200) {
                val records = respdata.getArray("data") as UTSArray<UTSJSONObject>?
                val options: UTSArray<OptionItem> = _uA()
                if (records != null) {
                    run {
                        var i: Number = 0
                        while(i < records.length){
                            val u = records[i]
                            val name = if (u != null) {
                                (u.getString("realName") ?: u.getString("username") ?: "")
                            } else {
                                ""
                            }
                            if (name != "") {
                                options.push(OptionItem(id = (i + 1).toString(10), name = name))
                            }
                            i++
                        }
                    }
                }
                this.userOptions = options
            }
        }
        , fail = fun(error){
            console.error("加载责任人列表失败:", error, " at pages/monthly/rectify.uvue:479")
        }
        ))
    }
    open var loadDeptOptionsList = ::gen_loadDeptOptionsList_fn
    open fun gen_loadDeptOptionsList_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/dept/list"), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val respdata = res.data as UTSJSONObject
            if ((respdata.getNumber("code") ?: 0) === 200) {
                val list = respdata.getArray("data") as UTSArray<UTSJSONObject>?
                val options: UTSArray<OptionItem> = _uA()
                if (list != null) {
                    run {
                        var i: Number = 0
                        while(i < list.length){
                            val dp = list[i]
                            val id = if (dp != null) {
                                (dp.getString("id") ?: "")
                            } else {
                                ""
                            }
                            val name = if (dp != null) {
                                (dp.getString("deptName") ?: "")
                            } else {
                                ""
                            }
                            if (id != "" && name != "") {
                                options.push(OptionItem(id = id, name = name))
                            }
                            i++
                        }
                    }
                }
                this.deptOptionsList = options
            }
        }
        , fail = fun(error){
            console.error("加载责任单位列表失败:", error, " at pages/monthly/rectify.uvue:505")
        }
        ))
    }
    open var showPersonPicker = ::gen_showPersonPicker_fn
    open fun gen_showPersonPicker_fn() {
        if (this.userOptions.length === 0) {
            this.loadUserOptions()
        }
        this.showPersonPopup = true
    }
    open var hidePersonPicker = ::gen_hidePersonPicker_fn
    open fun gen_hidePersonPicker_fn() {
        this.showPersonPopup = false
        this.personSearchKeyword = ""
    }
    open var togglePerson = ::gen_togglePerson_fn
    open fun gen_togglePerson_fn(item: OptionItem) {
        val idx = this.responsiblePersonArr.indexOf(item.name)
        if (idx === -1) {
            this.responsiblePersonArr.push(item.name)
        } else {
            this.responsiblePersonArr.splice(idx, 1)
        }
    }
    open var confirmPersonPicker = ::gen_confirmPersonPicker_fn
    open fun gen_confirmPersonPicker_fn() {
        this.hidePersonPicker()
    }
    open var showUnitPicker = ::gen_showUnitPicker_fn
    open fun gen_showUnitPicker_fn() {
        if (this.deptOptionsList.length === 0) {
            this.loadDeptOptionsList()
        }
        this.showUnitPopup = true
    }
    open var hideUnitPicker = ::gen_hideUnitPicker_fn
    open fun gen_hideUnitPicker_fn() {
        this.showUnitPopup = false
        this.unitSearchKeyword = ""
    }
    open var toggleUnit = ::gen_toggleUnit_fn
    open fun gen_toggleUnit_fn(item: OptionItem) {
        val idx = this.responsibleUnitArr.indexOf(item.name)
        if (idx === -1) {
            this.responsibleUnitArr.push(item.name)
        } else {
            this.responsibleUnitArr.splice(idx, 1)
        }
    }
    open var confirmUnitPicker = ::gen_confirmUnitPicker_fn
    open fun gen_confirmUnitPicker_fn() {
        this.hideUnitPicker()
    }
    open var handleSave = ::gen_handleSave_fn
    open fun gen_handleSave_fn() {
        if (this.saving) {
            return
        }
        var nm: Number = 0
        if (this.detail != null) {
            nm = this.detail!!!!.natureModified
        }
        this.saving = true
        val token = uni_getStorageSync("token")
        val payload: UTSJSONObject = _uO("__\$originalPosition" to UTSSourceMapPosition("payload", "pages/monthly/rectify.uvue", 543, 15), "id" to this.id, "responsiblePerson" to this.joinMulti(this.responsiblePersonArr), "responsibleUnit" to this.joinMulti(this.responsibleUnitArr), "rectificationSuggestion" to this.rectificationSuggestion, "rectificationEffect" to this.rectificationEffect, "rectificationImages" to this.rectificationImages.join(","), "natureModified" to nm)
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record"), method = "PUT", header = _uO("Authorization" to ("Bearer " + token), "Content-Type" to "application/json"), data = payload, success = fun(res){
            val resp = res.data as UTSJSONObject
            if (res.statusCode === 200 && (resp.getNumber("code") ?: 0) === 200) {
                uni_showToast(ShowToastOptions(title = "保存成功,状态未变更", icon = "none"))
            } else {
                uni_showToast(ShowToastOptions(title = resp.getString("message") ?: "保存失败", icon = "none"))
            }
            this.saving = false
        }
        , fail = fun(_){
            this.saving = false
            uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
        }
        ))
    }
    open var submitRectify = ::gen_submitRectify_fn
    open fun gen_submitRectify_fn() {
        if (this.saving) {
            return
        }
        if (this.rectificationSuggestion.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请填写整改建议", icon = "none"))
            return
        }
        if (this.rectificationEffect.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请填写整改效果", icon = "none"))
            return
        }
        this.saving = true
        val token = uni_getStorageSync("token")
        val data: UTSJSONObject = _uO("__\$originalPosition" to UTSSourceMapPosition("data", "pages/monthly/rectify.uvue", 580, 15), "rectificationSuggestion" to this.rectificationSuggestion, "rectificationEffect" to this.rectificationEffect, "rectificationImages" to this.rectificationImages.join(","), "responsibleUnit" to this.joinMulti(this.responsibleUnitArr), "responsiblePerson" to this.joinMulti(this.responsiblePersonArr), "rectifierName" to this.currentUserName)
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/" + this.id + "/rectify"), method = "PUT", header = _uO("Authorization" to ("Bearer " + token), "Content-Type" to "application/json"), data = data, success = fun(res){
            val resp = res.data as UTSJSONObject
            if (res.statusCode === 200 && (resp.getNumber("code") ?: 0) === 200) {
                uni_showToast(ShowToastOptions(title = "整改确认成功", icon = "success"))
                setTimeout(fun() {
                    uni_navigateBack(null)
                }, 1200)
            } else {
                uni_showToast(ShowToastOptions(title = resp.getString("message") ?: "提交失败", icon = "none"))
            }
            this.saving = false
        }
        , fail = fun(_){
            this.saving = false
            uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
        }
        ))
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("container" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#f1f5f9")), "loading-state" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "textAlign" to "center", "color" to "#475569")), "error" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "textAlign" to "center", "color" to "#475569")), "content" to _pS(_uM("paddingTop" to "24rpx", "paddingRight" to "32rpx", "paddingBottom" to "24rpx", "paddingLeft" to "32rpx")), "section" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "32rpx", "paddingRight" to "32rpx", "paddingBottom" to "32rpx", "paddingLeft" to "32rpx", "marginBottom" to "28rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "borderTopWidth" to "4rpx", "borderTopStyle" to "solid", "borderTopColor" to "#1e40af")), "title" to _pS(_uM("fontSize" to "32rpx", "fontWeight" to 600, "color" to "#1e40af", "marginBottom" to "24rpx", "paddingLeft" to "16rpx", "borderLeftWidth" to "6rpx", "borderLeftStyle" to "solid", "borderLeftColor" to "#2563eb")), "field" to _pS(_uM("marginBottom" to "20rpx", "display" to "flex", "justifyContent" to "space-between", "paddingTop" to "8rpx", "paddingRight" to 0, "paddingBottom" to "8rpx", "paddingLeft" to 0)), "label" to _uM("" to _uM("fontSize" to "28rpx", "color" to "#475569"), ".field-edit " to _uM("width" to "180rpx", "flexShrink" to 0, "color" to "#475569"), ".readonly-field " to _uM("minWidth" to "176rpx", "flexShrink" to 0, "color" to "#2563eb", "fontWeight" to 500)), "value" to _uM("" to _uM("fontSize" to "28rpx", "color" to "#0f172a", "textAlign" to "right", "maxWidth" to 480, "fontWeight" to 500), ".readonly-field " to _uM("textAlign" to "left", "flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "wordBreak" to "break-all", "color" to "#0f172a")), "field-edit" to _pS(_uM("marginBottom" to "20rpx", "display" to "flex", "alignItems" to "center", "paddingTop" to "8rpx", "paddingRight" to 0, "paddingBottom" to "8rpx", "paddingLeft" to 0)), "readonly-field" to _pS(_uM("marginBottom" to "20rpx", "display" to "flex", "alignItems" to "flex-start", "paddingTop" to "12rpx", "paddingRight" to "12rpx", "paddingBottom" to "12rpx", "paddingLeft" to "12rpx", "backgroundImage" to "none", "backgroundColor" to "#eff6ff", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx")), "textarea" to _pS(_uM("width" to "100%", "minHeight" to "200rpx", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "fontSize" to "28rpx", "color" to "#0f172a", "borderTopColor:focus" to "#2563eb", "borderRightColor:focus" to "#2563eb", "borderBottomColor:focus" to "#2563eb", "borderLeftColor:focus" to "#2563eb", "backgroundImage:focus" to "none", "backgroundColor:focus" to "#eff6ff")), "textarea-mini" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "minHeight" to "80rpx", "paddingTop" to "16rpx", "paddingRight" to "20rpx", "paddingBottom" to "16rpx", "paddingLeft" to "20rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "fontSize" to "28rpx", "color" to "#0f172a", "borderTopColor:focus" to "#2563eb", "borderRightColor:focus" to "#2563eb", "borderBottomColor:focus" to "#2563eb", "borderLeftColor:focus" to "#2563eb", "backgroundImage:focus" to "none", "backgroundColor:focus" to "#eff6ff")), "footer-locked" to _pS(_uM("textAlign" to "center", "paddingTop" to "40rpx", "paddingRight" to "30rpx", "paddingBottom" to "40rpx", "paddingLeft" to "30rpx")), "locked-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#94a3b8")), "image-list" to _pS(_uM("display" to "flex", "flexWrap" to "wrap")), "image-item" to _uM(".image-list " to _uM("marginTop" to "10rpx", "marginRight" to "10rpx", "marginBottom" to "10rpx", "marginLeft" to "10rpx"), "" to _uM("position" to "relative", "width" to "200rpx", "height" to "200rpx")), "add" to _uM(".image-list " to _uM("marginTop" to "10rpx", "marginRight" to "10rpx", "marginBottom" to "10rpx", "marginLeft" to "10rpx"), "" to _uM("width" to "200rpx", "height" to "200rpx", "borderTopWidth" to "4rpx", "borderRightWidth" to "4rpx", "borderBottomWidth" to "4rpx", "borderLeftWidth" to "4rpx", "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#2563eb", "borderRightColor" to "#2563eb", "borderBottomColor" to "#2563eb", "borderLeftColor" to "#2563eb", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "#eff6ff")), "no-image" to _uM(".image-list " to _uM("marginTop" to "10rpx", "marginRight" to "10rpx", "marginBottom" to "10rpx", "marginLeft" to "10rpx"), "" to _uM("fontSize" to "24rpx", "color" to "#94a3b8")), "image" to _pS(_uM("width" to "100%", "height" to "100%", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe")), "delete" to _pS(_uM("position" to "absolute", "top" to "-12rpx", "right" to "-12rpx", "width" to "44rpx", "height" to "44rpx", "backgroundImage" to "none", "backgroundColor" to "#dc2626", "borderTopLeftRadius" to "50%", "borderTopRightRadius" to "50%", "borderBottomRightRadius" to "50%", "borderBottomLeftRadius" to "50%", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "color" to "#ffffff", "fontWeight" to 600)), "add-icon" to _pS(_uM("fontSize" to "44rpx", "color" to "#2563eb", "fontWeight" to 600)), "add-text" to _pS(_uM("fontSize" to "24rpx", "color" to "#2563eb", "marginTop" to "8rpx")), "footer" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)")), "submit" to _pS(_uM("width" to "100%", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "backgroundImage" to "none", "backgroundColor" to "#1e40af", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "fontSize" to "32rpx", "fontWeight" to 600, "color::after" to "#ffffff", "borderTopWidth::after" to "medium", "borderRightWidth::after" to "medium", "borderBottomWidth::after" to "medium", "borderLeftWidth::after" to "medium", "borderTopStyle::after" to "none", "borderRightStyle::after" to "none", "borderBottomStyle::after" to "none", "borderLeftStyle::after" to "none", "borderTopColor::after" to "#000000", "borderRightColor::after" to "#000000", "borderBottomColor::after" to "#000000", "borderLeftColor::after" to "#000000")), "form-item" to _pS(_uM("marginBottom" to "24rpx")), "form-label-left" to _pS(_uM("fontSize" to "28rpx", "color" to "#475569", "marginBottom" to "12rpx", "fontWeight" to 500, "textAlign" to "left")), "custom-picker-full" to _uM("" to _uM("width" to "100%", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e2e8f0", "borderRightColor" to "#e2e8f0", "borderBottomColor" to "#e2e8f0", "borderLeftColor" to "#e2e8f0", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "backgroundColor" to "#ffffff", "minHeight" to "88rpx", "display" to "flex", "alignItems" to "center"), ".disabled" to _uM("backgroundImage" to "none", "backgroundColor" to "#f1f5f9", "borderTopColor" to "#e2e8f0", "borderRightColor" to "#e2e8f0", "borderBottomColor" to "#e2e8f0", "borderLeftColor" to "#e2e8f0", "opacity" to 0.6)), "picker-text-full" to _uM("" to _uM("fontSize" to "28rpx", "color" to "#0f172a", "width" to "100%", "textAlign" to "left"), ".placeholder" to _uM("color" to "#94a3b8")), "footer-actions" to _pS(_uM("display" to "flex", "flexDirection" to "row")), "btn-save" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "backgroundImage" to "none", "backgroundColor" to "#64748b", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "fontSize" to "30rpx", "fontWeight" to 600, "marginRight" to "20rpx", "borderTopWidth::after" to "medium", "borderRightWidth::after" to "medium", "borderBottomWidth::after" to "medium", "borderLeftWidth::after" to "medium", "borderTopStyle::after" to "none", "borderRightStyle::after" to "none", "borderBottomStyle::after" to "none", "borderLeftStyle::after" to "none", "borderTopColor::after" to "#000000", "borderRightColor::after" to "#000000", "borderBottomColor::after" to "#000000", "borderLeftColor::after" to "#000000")), "btn-save-only" to _pS(_uM("marginRight" to 0)), "btn-confirm" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "backgroundImage" to "none", "backgroundColor" to "#1e40af", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "fontSize" to "30rpx", "fontWeight" to 600, "borderTopWidth::after" to "medium", "borderRightWidth::after" to "medium", "borderBottomWidth::after" to "medium", "borderLeftWidth::after" to "medium", "borderTopStyle::after" to "none", "borderRightStyle::after" to "none", "borderBottomStyle::after" to "none", "borderLeftStyle::after" to "none", "borderTopColor::after" to "#000000", "borderRightColor::after" to "#000000", "borderBottomColor::after" to "#000000", "borderLeftColor::after" to "#000000")), "popup-overlay" to _pS(_uM("!position" to "fixed", "!top" to 0, "!left" to 0, "!width" to "100%", "!height" to "100%", "backgroundColor" to "rgba(10,37,64,0.55)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "!zIndex" to 9999, "pointerEvents" to "auto")), "popup-content" to _pS(_uM("!width" to "90%", "maxWidth" to "600rpx", "maxHeight" to "900rpx", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "overflow" to "hidden", "boxShadow" to "0 24rpx 64rpx rgba(10, 37, 64, 0.35)", "position" to "relative", "zIndex" to 10000)), "popup-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "28rpx", "paddingRight" to "32rpx", "paddingBottom" to "28rpx", "paddingLeft" to "32rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#e2e8f0", "backgroundImage" to "linear-gradient(135deg, #0a2540 0%, #1e40af 100%)", "backgroundColor" to "rgba(0,0,0,0)", "position" to "relative", "color" to "#ffffff")), "popup-title" to _pS(_uM("fontSize" to "30rpx", "fontWeight" to 700, "color" to "#ffffff", "letterSpacing" to "1rpx")), "popup-close" to _pS(_uM("fontSize" to "44rpx", "color" to "rgba(255,255,255,0.85)", "fontWeight" to 300, "width" to "60rpx", "height" to "60rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "borderTopLeftRadius" to "30rpx", "borderTopRightRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "borderBottomLeftRadius" to "30rpx")), "popup-search" to _pS(_uM("paddingTop" to "24rpx", "paddingRight" to "32rpx", "paddingBottom" to "24rpx", "paddingLeft" to "32rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#e2e8f0", "backgroundColor" to "#f8fafc")), "search-input" to _pS(_uM("width" to "100%", "height" to "72rpx", "paddingTop" to 0, "paddingRight" to "24rpx", "paddingBottom" to 0, "paddingLeft" to "24rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "borderTopLeftRadius" to "36rpx", "borderTopRightRadius" to "36rpx", "borderBottomRightRadius" to "36rpx", "borderBottomLeftRadius" to "36rpx", "fontSize" to "28rpx", "backgroundColor" to "#ffffff", "color" to "#0f172a")), "popup-list" to _pS(_uM("maxHeight" to "520rpx", "overflowY" to "auto")), "popup-empty" to _pS(_uM("paddingTop" to "80rpx", "paddingRight" to "32rpx", "paddingBottom" to "80rpx", "paddingLeft" to "32rpx", "textAlign" to "center")), "popup-loading" to _pS(_uM("display" to "flex", "justifyContent" to "center", "alignItems" to "center", "paddingTop" to "80rpx", "paddingRight" to "32rpx", "paddingBottom" to "80rpx", "paddingLeft" to "32rpx")), "empty-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#94a3b8")), "loading-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#94a3b8")), "popup-item" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "28rpx", "paddingRight" to "32rpx", "paddingBottom" to "28rpx", "paddingLeft" to "32rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#f1f5f9")), "popup-item-selected" to _pS(_uM("backgroundColor" to "#dbeafe", "borderLeftWidth" to "8rpx", "borderLeftStyle" to "solid", "borderLeftColor" to "#1e40af", "paddingLeft" to "24rpx")), "popup-item-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#0f172a", "fontWeight" to 500)), "popup-item-check" to _pS(_uM("fontSize" to "36rpx", "color" to "#1e40af", "fontWeight" to "bold")), "popup-footer" to _pS(_uM("display" to "flex", "borderTopWidth" to "2rpx", "borderTopStyle" to "solid", "borderTopColor" to "#e2e8f0", "backgroundColor" to "#f8fafc")), "popup-btn" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "paddingTop" to "28rpx", "paddingRight" to 0, "paddingBottom" to "28rpx", "paddingLeft" to 0, "textAlign" to "center", "fontSize" to "28rpx", "fontWeight" to 500)), "popup-btn-cancel" to _pS(_uM("color" to "#475569", "borderRightWidth" to "2rpx", "borderRightStyle" to "solid", "borderRightColor" to "#e2e8f0")), "popup-btn-confirm" to _pS(_uM("color" to "#1e40af", "fontWeight" to 700)))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
