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
import io.dcloud.uniapp.extapi.chooseImage as uni_chooseImage
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfo as uni_getSystemInfo
import io.dcloud.uniapp.extapi.hideLoading as uni_hideLoading
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading
import io.dcloud.uniapp.extapi.showModal as uni_showModal
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.uploadFile as uni_uploadFile
open class GenPagesRecordAdd : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: UTSJSONObject) {
            this.checkLogin()
            this.loadNatureOptions()
            this.loadProfessionOptions()
            this.loadDepartments()
            this.loadInspectorOptions()
            this.initDateArrays()
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
        val _component_picker_view_column = resolveComponent("picker-view-column")
        val _component_picker_view = resolveComponent("picker-view")
        val _component_form = resolveComponent("form")
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "scroll-top" to _ctx.scrollTop, "style" to _nS(_uM("height" to (_ctx.containerHeight + "px"))), "enable-back-to-top" to "true"), _uA(
            _cV(_component_form, null, _uM("default" to withSlotCtx(fun(): UTSArray<Any> {
                return _uA(
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label"), "📷 问题照片 (" + _tD(_ctx.imageList.length) + "/3)", 1),
                        _cE("view", _uM("class" to "image-upload"), _uA(
                            _cE("view", _uM("class" to "image-list"), _uA(
                                _cE(Fragment, null, RenderHelpers.renderList(_ctx.imageList, fun(image, index, __index, _cached): Any {
                                    return _cE("view", _uM("class" to "image-item", "key" to index), _uA(
                                        _cE("image", _uM("src" to image, "mode" to "aspectFill", "class" to "uploaded-image", "onClick" to fun(){
                                            _ctx.previewImage(index)
                                        }
                                        ), null, 8, _uA(
                                            "src",
                                            "onClick"
                                        )),
                                        _cE("view", _uM("class" to "image-delete", "onClick" to fun(){
                                            _ctx.removeImage(index)
                                        }
                                        ), _uA(
                                            _cE("text", null, "×")
                                        ), 8, _uA(
                                            "onClick"
                                        ))
                                    ))
                                }
                                ), 128),
                                if (_ctx.imageList.length < 3) {
                                    _cE("view", _uM("key" to 0, "class" to "image-add", "onClick" to _ctx.chooseImage), _uA(
                                        _cE("text", _uM("class" to "add-icon"), "+"),
                                        _cE("text", _uM("class" to "add-text"), "添加照片")
                                    ), 8, _uA(
                                        "onClick"
                                    ))
                                } else {
                                    _cC("v-if", true)
                                }
                            ))
                        ))
                    )),
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label"), "问题描述"),
                        _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                            _cE("textarea", _uM("modelValue" to _ctx.formData.problemDescription, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.formData.problemDescription = `$event`.detail.value
                            }
                            , "class" to "form-textarea", "placeholder" to "请详细描述问题情况...", "maxlength" to "500", "show-confirm-bar" to "false"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        ))
                    )),
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label"), "责任单位"),
                        _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                            _cE("input", _uM("modelValue" to _ctx.formData.responsibleUnit, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.formData.responsibleUnit = `$event`.detail.value
                            }
                            , "class" to "form-input", "placeholder" to "请输入责任单位"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        ))
                    )),
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label"), "责任人"),
                        _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                            _cE("input", _uM("modelValue" to _ctx.formData.responsiblePerson, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.formData.responsiblePerson = `$event`.detail.value
                            }
                            , "class" to "form-input", "placeholder" to "请输入责任人姓名"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        ))
                    )),
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label-left"), "性质界定"),
                        _cE("view", _uM("class" to _nC(_uA(
                            "custom-picker-full",
                            _uM("disabled" to (_ctx.natureOptions.length === 0))
                        )), "onClick" to _ctx.showNaturePicker), _uA(
                            _cE("text", _uM("class" to _nC(_uA(
                                "picker-text-full",
                                _uM("placeholder" to (_ctx.selectedNatureIndex === -1))
                            ))), _tD(if (_ctx.selectedNatureIndex === -1) {
                                "请选择问题性质"
                            } else {
                                if (_ctx.selectedNatureIndex < _ctx.natureOptions.length) {
                                    _ctx.natureOptions[_ctx.selectedNatureIndex].name
                                } else {
                                    "请选择问题性质"
                                }
                            }
                            ), 3)
                        ), 10, _uA(
                            "onClick"
                        ))
                    )),
                    if (isTrue(_ctx.showNaturePopup)) {
                        _cE("view", _uM("key" to 0, "class" to "popup-overlay", "onClick" to _ctx.hideNaturePicker), _uA(
                            _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                                "stop"
                            ))), _uA(
                                _cE("view", _uM("class" to "popup-header"), _uA(
                                    _cE("text", _uM("class" to "popup-title"), "选择性质界定"),
                                    _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideNaturePicker), "×", 8, _uA(
                                        "onClick"
                                    ))
                                )),
                                _cE("view", _uM("class" to "popup-list"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.natureOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to item.id, "class" to "popup-item", "onClick" to fun(){
                                            _ctx.selectNature(index)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD(item.name), 1),
                                            if (_ctx.selectedNatureIndex === index) {
                                                _cE("text", _uM("key" to 0, "class" to "popup-item-check"), "✓")
                                            } else {
                                                _cC("v-if", true)
                                            }
                                        ), 8, _uA(
                                            "onClick"
                                        ))
                                    }), 128)
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
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label-left"), "考核情况"),
                        _cE("view", _uM("class" to _nC(_uA(
                            "custom-picker-full",
                            _uM("disabled" to (_ctx.assessmentOptions.length === 0))
                        )), "onClick" to _ctx.showAssessmentPicker), _uA(
                            _cE("text", _uM("class" to _nC(_uA(
                                "picker-text-full",
                                _uM("placeholder" to (_ctx.selectedAssessmentIndex === -1))
                            ))), _tD(if (_ctx.selectedAssessmentIndex === -1) {
                                "请选择考核情况"
                            } else {
                                if (_ctx.selectedAssessmentIndex < _ctx.assessmentOptions.length) {
                                    _ctx.assessmentOptions[_ctx.selectedAssessmentIndex].label
                                } else {
                                    "请选择考核情况"
                                }
                            }
                            ), 3)
                        ), 10, _uA(
                            "onClick"
                        ))
                    )),
                    if (isTrue(_ctx.showAssessmentPopup)) {
                        _cE("view", _uM("key" to 1, "class" to "popup-overlay", "onClick" to _ctx.hideAssessmentPicker), _uA(
                            _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                                "stop"
                            ))), _uA(
                                _cE("view", _uM("class" to "popup-header"), _uA(
                                    _cE("text", _uM("class" to "popup-title"), "选择考核情况"),
                                    _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideAssessmentPicker), "×", 8, _uA(
                                        "onClick"
                                    ))
                                )),
                                _cE("view", _uM("class" to "popup-list"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.assessmentOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to item.id, "class" to "popup-item", "onClick" to fun(){
                                            _ctx.selectAssessment(index)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD(item.label), 1),
                                            if (_ctx.selectedAssessmentIndex === index) {
                                                _cE("text", _uM("key" to 0, "class" to "popup-item-check"), "✓")
                                            } else {
                                                _cC("v-if", true)
                                            }
                                        ), 8, _uA(
                                            "onClick"
                                        ))
                                    }), 128)
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
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label"), "考核金额"),
                        _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                            _cE("input", _uM("modelValue" to _ctx.formData.assessmentAmount, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.formData.assessmentAmount = `$event`.detail.value
                            }
                            , "class" to "form-input", "placeholder" to "请输入金额", "type" to "number"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            )),
                            _cE("text", _uM("class" to "input-suffix"), "元")
                        ))
                    )),
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label"), "安全记分"),
                        _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                            _cE("input", _uM("modelValue" to _ctx.formData.safetyScore, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.formData.safetyScore = `$event`.detail.value
                            }
                            , "class" to "form-input", "placeholder" to "请输入安全记分", "type" to "number"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        ))
                    )),
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label-left"), "专业"),
                        _cE("view", _uM("class" to _nC(_uA(
                            "custom-picker-full",
                            _uM("disabled" to (_ctx.professionOptions.length === 0))
                        )), "onClick" to _ctx.showProfessionPicker), _uA(
                            _cE("text", _uM("class" to _nC(_uA(
                                "picker-text-full",
                                _uM("placeholder" to (_ctx.selectedProfessionIndex === -1))
                            ))), _tD(if (_ctx.selectedProfessionIndex === -1) {
                                "请选择专业"
                            } else {
                                if (_ctx.selectedProfessionIndex < _ctx.professionOptions.length) {
                                    _ctx.professionOptions[_ctx.selectedProfessionIndex].name
                                } else {
                                    "请选择专业"
                                }
                            }
                            ), 3)
                        ), 10, _uA(
                            "onClick"
                        ))
                    )),
                    if (isTrue(_ctx.showProfessionPopup)) {
                        _cE("view", _uM("key" to 2, "class" to "popup-overlay", "onClick" to _ctx.hideProfessionPicker), _uA(
                            _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                                "stop"
                            ))), _uA(
                                _cE("view", _uM("class" to "popup-header"), _uA(
                                    _cE("text", _uM("class" to "popup-title"), "选择专业"),
                                    _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideProfessionPicker), "×", 8, _uA(
                                        "onClick"
                                    ))
                                )),
                                _cE("view", _uM("class" to "popup-list"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.professionOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to item.id, "class" to "popup-item", "onClick" to fun(){
                                            _ctx.selectProfession(index)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD(item.name), 1),
                                            if (_ctx.selectedProfessionIndex === index) {
                                                _cE("text", _uM("key" to 0, "class" to "popup-item-check"), "✓")
                                            } else {
                                                _cC("v-if", true)
                                            }
                                        ), 8, _uA(
                                            "onClick"
                                        ))
                                    }), 128)
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
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label"), "发生部位"),
                        _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                            _cE("input", _uM("modelValue" to _ctx.formData.location, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.formData.location = `$event`.detail.value
                            }
                            , "class" to "form-input", "placeholder" to "请输入发生部位"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        ))
                    )),
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label-left"), "检查人"),
                        _cE("view", _uM("class" to _nC(_uA(
                            "custom-picker-full",
                            _uM("disabled" to (_ctx.inspectorOptions.length === 0))
                        )), "onClick" to _ctx.showInspectorPicker), _uA(
                            _cE("text", _uM("class" to _nC(_uA(
                                "picker-text-full",
                                _uM("placeholder" to (_ctx.selectedInspectorIndex === -1))
                            ))), _tD(if (_ctx.selectedInspectorIndex === -1) {
                                "请选择检查人"
                            } else {
                                if (_ctx.selectedInspectorIndex < _ctx.inspectorOptions.length) {
                                    _ctx.inspectorOptions[_ctx.selectedInspectorIndex].name
                                } else {
                                    "请选择检查人"
                                }
                            }
                            ), 3)
                        ), 10, _uA(
                            "onClick"
                        ))
                    )),
                    if (isTrue(_ctx.showInspectorPopup)) {
                        _cE("view", _uM("key" to 3, "class" to "popup-overlay", "onClick" to _ctx.hideInspectorPicker), _uA(
                            _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                                "stop"
                            ))), _uA(
                                _cE("view", _uM("class" to "popup-header"), _uA(
                                    _cE("text", _uM("class" to "popup-title"), "选择检查人"),
                                    _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideInspectorPicker), "×", 8, _uA(
                                        "onClick"
                                    ))
                                )),
                                _cE("view", _uM("class" to "popup-list"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.inspectorOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to item.id, "class" to "popup-item", "onClick" to fun(){
                                            _ctx.selectInspector(index)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD(item.name), 1),
                                            if (_ctx.selectedInspectorIndex === index) {
                                                _cE("text", _uM("key" to 0, "class" to "popup-item-check"), "✓")
                                            } else {
                                                _cC("v-if", true)
                                            }
                                        ), 8, _uA(
                                            "onClick"
                                        ))
                                    }), 128)
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
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label"), "检查组组长"),
                        _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                            _cE("input", _uM("modelValue" to _ctx.formData.groupLeader, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.formData.groupLeader = `$event`.detail.value
                            }
                            , "class" to "form-input", "placeholder" to "请输入检查组组长姓名"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        ))
                    )),
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label"), "检查组副组长"),
                        _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                            _cE("input", _uM("modelValue" to _ctx.formData.deputyLeader, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.formData.deputyLeader = `$event`.detail.value
                            }
                            , "class" to "form-input", "placeholder" to "请输入检查组副组长姓名"), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        ))
                    )),
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label-left"), "所属部门"),
                        _cE("view", _uM("class" to _nC(_uA(
                            "custom-picker-full",
                            _uM("disabled" to (_ctx.departmentOptions.length === 0))
                        )), "onClick" to _ctx.showDepartmentPicker), _uA(
                            _cE("text", _uM("class" to _nC(_uA(
                                "picker-text-full",
                                _uM("placeholder" to (_ctx.selectedDepartmentIndex === -1))
                            ))), _tD(if (_ctx.selectedDepartmentIndex === -1) {
                                "请选择所属部门"
                            } else {
                                if (_ctx.selectedDepartmentIndex < _ctx.departmentOptions.length) {
                                    _ctx.departmentOptions[_ctx.selectedDepartmentIndex].name
                                } else {
                                    "请选择所属部门"
                                }
                            }
                            ), 3)
                        ), 10, _uA(
                            "onClick"
                        ))
                    )),
                    if (isTrue(_ctx.showDepartmentPopup)) {
                        _cE("view", _uM("key" to 4, "class" to "popup-overlay", "onClick" to _ctx.hideDepartmentPicker), _uA(
                            _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                                "stop"
                            ))), _uA(
                                _cE("view", _uM("class" to "popup-header"), _uA(
                                    _cE("text", _uM("class" to "popup-title"), "选择所属部门"),
                                    _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideDepartmentPicker), "×", 8, _uA(
                                        "onClick"
                                    ))
                                )),
                                _cE("view", _uM("class" to "popup-list"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.departmentOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to item.id, "class" to "popup-item", "onClick" to fun(){
                                            _ctx.selectDepartment(index)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD(item.name), 1),
                                            if (_ctx.selectedDepartmentIndex === index) {
                                                _cE("text", _uM("key" to 0, "class" to "popup-item-check"), "✓")
                                            } else {
                                                _cC("v-if", true)
                                            }
                                        ), 8, _uA(
                                            "onClick"
                                        ))
                                    }), 128)
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
                    _cE("view", _uM("class" to "form-item"), _uA(
                        _cE("text", _uM("class" to "form-label-left"), "整改时间"),
                        _cE("view", _uM("class" to "picker-container", "onClick" to _ctx.showDatePicker), _uA(
                            _cE("text", _uM("class" to _nC(_uA(
                                "picker-text-full",
                                _uM("placeholder" to (_ctx.formData.rectificationDate === ""))
                            ))), _tD(if (_ctx.formData.rectificationDate !== "") {
                                _ctx.formData.rectificationDate
                            } else {
                                "请选择整改时间"
                            }
                            ), 3)
                        ), 8, _uA(
                            "onClick"
                        )),
                        if (isTrue(_ctx.datePickerVisible)) {
                            _cE("view", _uM("key" to 0, "class" to "date-picker-modal", "onClick" to _ctx.hideDatePicker), _uA(
                                _cE("view", _uM("class" to "date-picker-content", "onClick" to withModifiers(fun(){}, _uA(
                                    "stop"
                                ))), _uA(
                                    _cE("view", _uM("class" to "date-picker-header"), _uA(
                                        _cE("text", _uM("class" to "date-picker-cancel", "onClick" to _ctx.hideDatePicker), "取消", 8, _uA(
                                            "onClick"
                                        )),
                                        _cE("text", _uM("class" to "date-picker-title"), "选择日期"),
                                        _cE("text", _uM("class" to "date-picker-confirm", "onClick" to _ctx.confirmDatePicker), "确定", 8, _uA(
                                            "onClick"
                                        ))
                                    )),
                                    _cV(_component_picker_view, _uM("class" to "date-picker-view", "value" to _ctx.pickerValue, "onChange" to _ctx.onPickerChange, "indicator-style" to "height: 70px; top: 20%; margin-top: -20px;"), _uM("default" to withSlotCtx(fun(): UTSArray<Any> {
                                        return _uA(
                                            _cV(_component_picker_view_column, null, _uM("default" to withSlotCtx(fun(): UTSArray<Any> {
                                                return _uA(
                                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.years, fun(year, index, __index, _cached): Any {
                                                        return _cE("view", _uM("key" to index, "class" to _nC(_uA(
                                                            "picker-item",
                                                            _uM("selected" to (index === _ctx.pickerValue[0]))
                                                        ))), _tD(year) + "年 ", 3)
                                                    }), 128)
                                                )
                                            }), "_" to 1)),
                                            _cV(_component_picker_view_column, null, _uM("default" to withSlotCtx(fun(): UTSArray<Any> {
                                                return _uA(
                                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.months, fun(month, index, __index, _cached): Any {
                                                        return _cE("view", _uM("key" to index, "class" to _nC(_uA(
                                                            "picker-item",
                                                            _uM("selected" to (index === _ctx.pickerValue[1]))
                                                        ))), _tD(month) + "月 ", 3)
                                                    }), 128)
                                                )
                                            }), "_" to 1)),
                                            _cV(_component_picker_view_column, null, _uM("default" to withSlotCtx(fun(): UTSArray<Any> {
                                                return _uA(
                                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.days, fun(day, index, __index, _cached): Any {
                                                        return _cE("view", _uM("key" to index, "class" to _nC(_uA(
                                                            "picker-item",
                                                            _uM("selected" to (index === _ctx.pickerValue[2]))
                                                        ))), _tD(day) + "日 ", 3)
                                                    }), 128),
                                                    _cE(Fragment, null, RenderHelpers.renderList(2, fun(i, __key, __index, _cached): Any {
                                                        return _cE("view", _uM("key" to ("empty-day-" + i), "class" to "picker-item picker-item-spacer"))
                                                    }), 64)
                                                )
                                            }), "_" to 1))
                                        )
                                    }), "_" to 1), 8, _uA(
                                        "value",
                                        "onChange"
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
                    )),
                    _cE("view", _uM("class" to "submit-section"), _uA(
                        _cE("button", _uM("class" to "submit-btn", "type" to "button", "disabled" to _ctx.submitting, "onClick" to _ctx.submitRecord), _uA(
                            _cE("text", null, _tD(if (_ctx.submitting) {
                                "提交中..."
                            } else {
                                "提交检查"
                            }
                            ), 1)
                        ), 8, _uA(
                            "disabled",
                            "onClick"
                        ))
                    ))
                )
            }
            ), "_" to 1))
        ), 12, _uA(
            "scroll-top"
        ))
    }
    open var formData: RecordFormData by `$data`
    open var natureOptions: UTSArray<NatureOption> by `$data`
    open var assessmentOptions: UTSArray<AssessmentOption> by `$data`
    open var professionOptions: UTSArray<ProfessionOption> by `$data`
    open var departmentOptions: UTSArray<DepartmentOption> by `$data`
    open var inspectorOptions: UTSArray<InspectorOption> by `$data`
    open var selectedNatureIndex: Number by `$data`
    open var selectedAssessmentIndex: Number by `$data`
    open var selectedProfessionIndex: Number by `$data`
    open var selectedDepartmentIndex: Number by `$data`
    open var selectedInspectorIndex: Number by `$data`
    open var showNaturePopup: Boolean by `$data`
    open var showAssessmentPopup: Boolean by `$data`
    open var showProfessionPopup: Boolean by `$data`
    open var showInspectorPopup: Boolean by `$data`
    open var showDepartmentPopup: Boolean by `$data`
    open var showDatePopup: Boolean by `$data`
    open var tempDate: String by `$data`
    open var imageList: UTSArray<String> by `$data`
    open var choosingImage: Boolean by `$data`
    open var submitting: Boolean by `$data`
    open var scrollTop: Number by `$data`
    open var containerHeight: Number by `$data`
    open var datePickerVisible: Boolean by `$data`
    open var pickerValue: UTSArray<Number> by `$data`
    open var years: UTSArray<Number> by `$data`
    open var months: UTSArray<Number> by `$data`
    open var days: UTSArray<Number> by `$data`
    open var selectedYear: Number by `$data`
    open var selectedMonth: Number by `$data`
    open var selectedDay: Number by `$data`
    open var pickerDebounceTimer: Number? by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        val defaultDate = Date()
        defaultDate.setDate(defaultDate.getDate() + 3)
        val defaultDateStr = "" + defaultDate.getFullYear() + "-" + (defaultDate.getMonth() + 1).toString(10).padStart(2, "0") + "-" + defaultDate.getDate().toString(10).padStart(2, "0")
        return _uM("formData" to RecordFormData(responsibleUnit = "", responsiblePerson = "", natureId = "", scoreId = "", assessmentAmount = "", safetyScore = "", professionId = "", problemDescription = "", location = "", inspector = "", groupLeader = "", deputyLeader = "", departmentId = "", rectificationDate = defaultDateStr), "natureOptions" to _uA<NatureOption>(), "assessmentOptions" to _uA<AssessmentOption>(AssessmentOption(id = "1", label = "提出警告", value = "提出警告"), AssessmentOption(id = "2", label = "-50", value = "-50"), AssessmentOption(id = "3", label = "-100", value = "-100"), AssessmentOption(id = "4", label = "-200", value = "-200"), AssessmentOption(id = "5", label = "-300", value = "-300")), "professionOptions" to _uA<ProfessionOption>(), "departmentOptions" to _uA<DepartmentOption>(), "inspectorOptions" to _uA<InspectorOption>(), "selectedNatureIndex" to -1, "selectedAssessmentIndex" to -1, "selectedProfessionIndex" to -1, "selectedDepartmentIndex" to -1, "selectedInspectorIndex" to -1, "showNaturePopup" to false, "showAssessmentPopup" to false, "showProfessionPopup" to false, "showInspectorPopup" to false, "showDepartmentPopup" to false, "showDatePopup" to false, "tempDate" to "", "imageList" to _uA<String>(), "choosingImage" to false, "submitting" to false, "scrollTop" to 0, "containerHeight" to 0, "datePickerVisible" to false, "pickerValue" to _uA(
            0,
            0,
            0
        ), "years" to _uA<Number>(), "months" to _uA<Number>(), "days" to _uA<Number>(), "selectedYear" to (fun(): Number {
            val tempDate = Date()
            tempDate.setDate(tempDate.getDate() + 3)
            return tempDate.getFullYear()
        }
        )() as Number, "selectedMonth" to (fun(): Number {
            val tempDate = Date()
            tempDate.setDate(tempDate.getDate() + 3)
            return tempDate.getMonth() + 1
        }
        )() as Number, "selectedDay" to (fun(): Number {
            val tempDate = Date()
            tempDate.setDate(tempDate.getDate() + 3)
            return tempDate.getDate()
        }
        )() as Number, "pickerDebounceTimer" to null as Number?)
    }
    open var checkLogin = ::gen_checkLogin_fn
    open fun gen_checkLogin_fn() {
        val token = uni_getStorageSync("token")
        if (token == null || token == "") {
            uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
        }
    }
    open var initContainerHeight = ::gen_initContainerHeight_fn
    open fun gen_initContainerHeight_fn() {
        uni_getSystemInfo(GetSystemInfoOptions(success = fun(res){
            this.containerHeight = res.windowHeight
        }
        ))
    }
    open var loadNatureOptions = ::gen_loadNatureOptions_fn
    open fun gen_loadNatureOptions_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/dictionary/type/性质界定"), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
            var `Content-Type` = "application/json"
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            val code = data.getNumber("code")
            if (res.statusCode === 200 && code === 200) {
                val dataArray = data.getArray("data")
                if (dataArray != null) {
                    this.natureOptions = _uA()
                    run {
                        var i: Number = 0
                        while(i < dataArray.length){
                            val item = dataArray[i] as UTSJSONObject
                            val id = item.getString("id") ?: ""
                            val dictValue = item.getString("dictValue") ?: ""
                            val dictLabel = item.getString("dictLabel") ?: ""
                            val name = if (dictValue !== "") {
                                dictValue
                            } else {
                                dictLabel
                            }
                            this.natureOptions.push(NatureOption(id = id, name = name))
                            i++
                        }
                    }
                }
                console.log("性质界定数据加载成功:", this.natureOptions, " at pages/record/add.uvue:525")
            } else {
                val message = data.getString("message") ?: "未知错误"
                console.error("获取性质界定数据失败:", message, " at pages/record/add.uvue:528")
                this.natureOptions = _uA(
                    NatureOption(id = "1", name = "一般问题"),
                    NatureOption(id = "2", name = "较大问题"),
                    NatureOption(id = "3", name = "严重问题"),
                    NatureOption(id = "4", name = "重大问题")
                )
            }
        }
        , fail = fun(error){
            console.error("请求性质界定数据失败:", error, " at pages/record/add.uvue:539")
            this.natureOptions = _uA(
                NatureOption(id = "1", name = "一般问题"),
                NatureOption(id = "2", name = "较大问题"),
                NatureOption(id = "3", name = "严重问题"),
                NatureOption(id = "4", name = "重大问题")
            )
        }
        ))
    }
    open var loadProfessionOptions = ::gen_loadProfessionOptions_fn
    open fun gen_loadProfessionOptions_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/dictionary/type/专业"), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
            var `Content-Type` = "application/json"
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            val code = data.getNumber("code")
            if (res.statusCode === 200 && code === 200) {
                val dataArray = data.getArray("data")
                if (dataArray != null) {
                    this.professionOptions = _uA()
                    run {
                        var i: Number = 0
                        while(i < dataArray.length){
                            val item = dataArray[i] as UTSJSONObject
                            val id = item.getString("id") ?: ""
                            val dictValue = item.getString("dictValue") ?: ""
                            val dictLabel = item.getString("dictLabel") ?: ""
                            val name = if (dictValue !== "") {
                                dictValue
                            } else {
                                dictLabel
                            }
                            this.professionOptions.push(ProfessionOption(id = id, name = name))
                            i++
                        }
                    }
                }
                console.log("专业数据加载成功:", this.professionOptions, " at pages/record/add.uvue:580")
            } else {
                val message = data.getString("message") ?: "未知错误"
                console.error("获取专业数据失败:", message, " at pages/record/add.uvue:583")
                this.professionOptions = _uA(
                    ProfessionOption(id = "1", name = "机械"),
                    ProfessionOption(id = "2", name = "电气"),
                    ProfessionOption(id = "3", name = "仪表"),
                    ProfessionOption(id = "4", name = "工艺"),
                    ProfessionOption(id = "5", name = "安全"),
                    ProfessionOption(id = "6", name = "环保")
                )
            }
        }
        , fail = fun(error){
            console.error("请求专业数据失败:", error, " at pages/record/add.uvue:596")
            this.professionOptions = _uA(
                ProfessionOption(id = "1", name = "机械"),
                ProfessionOption(id = "2", name = "电气"),
                ProfessionOption(id = "3", name = "仪表"),
                ProfessionOption(id = "4", name = "工艺"),
                ProfessionOption(id = "5", name = "安全"),
                ProfessionOption(id = "6", name = "环保")
            )
        }
        ))
    }
    open var loadInspectorOptions = ::gen_loadInspectorOptions_fn
    open fun gen_loadInspectorOptions_fn() {
        val token = uni_getStorageSync("token")
        if (token == null || token == "") {
            console.error("未找到token", " at pages/record/add.uvue:614")
            return
        }
        val userInfoStr = uni_getStorageSync("userInfo") as String
        if (userInfoStr == null || userInfoStr == "") {
            console.error("未找到用户信息", " at pages/record/add.uvue:621")
            return
        }
        val userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/add.uvue:625") as UTSJSONObject
        console.log("当前用户信息:", userInfo, " at pages/record/add.uvue:628")
        var deptId = userInfo.getString("deptId") ?: userInfo.getString("dept_id") ?: ""
        if (deptId === "") {
            val deptIdNum = userInfo.getNumber("deptId") ?: userInfo.getNumber("dept_id")
            if (deptIdNum != null) {
                deptId = deptIdNum.toString(10)
            }
        }
        console.log("用户部门ID:", deptId, " at pages/record/add.uvue:637")
        if (deptId == "") {
            console.warn("当前用户没有部门信息，将获取所有用户", " at pages/record/add.uvue:640")
            uni_request<Any>(RequestOptions(url = getApiUrl("/user/page?current=1&size=100"), method = "GET", header = object : UTSJSONObject() {
                var Authorization = "Bearer " + token
            }, success = fun(res){
                val response = res.data as UTSJSONObject
                if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                    val data = response.getJSON("data") as UTSJSONObject
                    val records = data.getArray("records")
                    if (records != null) {
                        this.inspectorOptions = _uA()
                        run {
                            var i: Number = 0
                            while(i < records.length){
                                val item = records[i] as UTSJSONObject
                                val id = item.getString("id") ?: ""
                                val realName = item.getString("realName") ?: ""
                                if (id != "" && realName != "") {
                                    this.inspectorOptions.push(InspectorOption(id = id, name = realName))
                                }
                                i++
                            }
                        }
                    }
                    console.log("所有用户数据加载成功:", this.inspectorOptions, " at pages/record/add.uvue:664")
                    this.setCurrentUserAsInspector()
                } else {
                    val message = response.getString("message") ?: "未知错误"
                    console.error("获取用户列表失败:", message, " at pages/record/add.uvue:669")
                    this.inspectorOptions = _uA(
                        InspectorOption(id = "1", name = "张三"),
                        InspectorOption(id = "2", name = "李四"),
                        InspectorOption(id = "3", name = "王五"),
                        InspectorOption(id = "4", name = "赵六")
                    )
                    this.setCurrentUserAsInspector()
                }
            }
            , fail = fun(error){
                console.error("请求用户列表失败:", error, " at pages/record/add.uvue:682")
                this.inspectorOptions = _uA(
                    InspectorOption(id = "1", name = "张三"),
                    InspectorOption(id = "2", name = "李四"),
                    InspectorOption(id = "3", name = "王五"),
                    InspectorOption(id = "4", name = "赵六")
                )
                this.setCurrentUserAsInspector()
            }
            ))
            return
        }
        uni_request<Any>(RequestOptions(url = getApiUrl("/user/by-department?deptId=" + UTSAndroid.consoleDebugError(encodeURIComponent(deptId), " at pages/record/add.uvue:699")), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val response = res.data as UTSJSONObject
            if (res.statusCode == 200 && (response.getNumber("code") ?: 0) == 200) {
                val data = response.getArray("data")
                if (data != null) {
                    this.inspectorOptions = _uA()
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val item = data[i] as UTSJSONObject
                            val id = item.getString("id") ?: ""
                            val realName = item.getString("realName") ?: ""
                            if (id != "" && realName != "") {
                                this.inspectorOptions.push(InspectorOption(id = id, name = realName))
                            }
                            i++
                        }
                    }
                }
                console.log("\u90E8\u95E8ID" + deptId + "\u7684\u68C0\u67E5\u4EBA\u6570\u636E\u52A0\u8F7D\u6210\u529F:", this.inspectorOptions, " at pages/record/add.uvue:719")
                this.setCurrentUserAsInspector()
            } else {
                val message = response.getString("message") ?: "未知错误"
                console.error("获取部门用户列表失败:", message, " at pages/record/add.uvue:724")
                this.inspectorOptions = _uA(
                    InspectorOption(id = "1", name = "张三"),
                    InspectorOption(id = "2", name = "李四"),
                    InspectorOption(id = "3", name = "王五"),
                    InspectorOption(id = "4", name = "赵六")
                )
                this.setCurrentUserAsInspector()
            }
        }
        , fail = fun(error){
            console.error("请求部门用户列表失败:", error, " at pages/record/add.uvue:737")
            this.inspectorOptions = _uA(
                InspectorOption(id = "1", name = "张三"),
                InspectorOption(id = "2", name = "李四"),
                InspectorOption(id = "3", name = "王五"),
                InspectorOption(id = "4", name = "赵六")
            )
            this.setCurrentUserAsInspector()
        }
        ))
    }
    open var setCurrentUserAsInspector = ::gen_setCurrentUserAsInspector_fn
    open fun gen_setCurrentUserAsInspector_fn() {
        val userInfoStr = uni_getStorageSync("userInfo") as String
        if (userInfoStr == null || userInfoStr == "") {
            console.error("未找到用户信息", " at pages/record/add.uvue:755")
            return
        }
        val userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/add.uvue:759") as UTSJSONObject
        console.log("当前用户信息:", userInfo, " at pages/record/add.uvue:760")
        val currentUserId = userInfo.getString("id") ?: ""
        val currentUserName = userInfo.getString("realName") ?: userInfo.getString("name") ?: userInfo.getString("username") ?: ""
        console.log("当前用户ID:", currentUserId, "姓名:", currentUserName, " at pages/record/add.uvue:766")
        console.log("检查人选项列表:", this.inspectorOptions, " at pages/record/add.uvue:767")
        if (currentUserId == "" && currentUserName == "") {
            console.error("无法获取当前用户信息", " at pages/record/add.uvue:770")
            return
        }
        var currentUserIndex: Number = -1
        if (currentUserId != "") {
            currentUserIndex = this.inspectorOptions.findIndex(fun(option): Boolean {
                return option.id == currentUserId
            }
            )
            console.log("按ID查找结果:", currentUserIndex, "查找ID:", currentUserId, "选项IDs:", this.inspectorOptions.map(fun(opt): String {
                return opt.id
            }
            ), " at pages/record/add.uvue:780")
        }
        if (currentUserIndex === -1 && currentUserName != "") {
            currentUserIndex = this.inspectorOptions.findIndex(fun(option): Boolean {
                return option.name == currentUserName
            }
            )
            console.log("按姓名查找结果:", currentUserIndex, "查找姓名:", currentUserName, "选项姓名:", this.inspectorOptions.map(fun(opt): String {
                return opt.name
            }
            ), " at pages/record/add.uvue:785")
        }
        if (currentUserIndex !== -1) {
            this.selectedInspectorIndex = currentUserIndex
            this.formData.inspector = this.inspectorOptions[currentUserIndex].name
            console.log("已设置当前用户为默认检查人:", this.inspectorOptions[currentUserIndex].name, " at pages/record/add.uvue:791")
        } else {
            console.warn("在检查人列表中未找到当前用户，ID:", currentUserId, "姓名:", currentUserName, " at pages/record/add.uvue:793")
            console.log("当前检查人选项:", this.inspectorOptions, " at pages/record/add.uvue:794")
        }
    }
    open var loadDepartments = ::gen_loadDepartments_fn
    open fun gen_loadDepartments_fn() {
        console.log("调试：开始加载部门数据", " at pages/record/add.uvue:800")
        uni_showToast(ShowToastOptions(title = "开始加载部门数据", icon = "none", duration = 1000))
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/dept/list"), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
            var `Content-Type` = "application/json"
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            val code = data.getNumber("code")
            if (res.statusCode === 200 && code === 200) {
                val dataArray = data.getArray("data")
                if (dataArray != null) {
                    this.departmentOptions = _uA()
                    run {
                        var i: Number = 0
                        while(i < dataArray.length){
                            val item = dataArray[i] as UTSJSONObject
                            val id = item.getString("id") ?: ""
                            val deptName = item.getString("deptName") ?: ""
                            if (id !== "" && deptName !== "") {
                                this.departmentOptions.push(DepartmentOption(id = id, name = deptName))
                            }
                            i++
                        }
                    }
                }
                console.log("部门数据加载成功:", this.departmentOptions, " at pages/record/add.uvue:826")
                this.setCurrentUserDepartment()
            } else {
                val message = data.getString("message") ?: "未知错误"
                console.error("获取部门数据失败:", message, " at pages/record/add.uvue:831")
                this.departmentOptions = _uA(
                    DepartmentOption(id = "1", name = "生产部"),
                    DepartmentOption(id = "2", name = "安全部"),
                    DepartmentOption(id = "3", name = "设备部"),
                    DepartmentOption(id = "4", name = "技术部"),
                    DepartmentOption(id = "5", name = "质量部"),
                    DepartmentOption(id = "6", name = "环保部"),
                    DepartmentOption(id = "7", name = "行政部")
                )
                this.setCurrentUserDepartment()
            }
        }
        , fail = fun(error){
            console.error("请求部门数据失败:", error, " at pages/record/add.uvue:847")
            this.departmentOptions = _uA(
                DepartmentOption(id = "1", name = "生产部"),
                DepartmentOption(id = "2", name = "安全部"),
                DepartmentOption(id = "3", name = "设备部"),
                DepartmentOption(id = "4", name = "技术部"),
                DepartmentOption(id = "5", name = "质量部"),
                DepartmentOption(id = "6", name = "环保部"),
                DepartmentOption(id = "7", name = "行政部")
            )
            this.setCurrentUserDepartment()
        }
        ))
    }
    open var onNatureChange = ::gen_onNatureChange_fn
    open fun gen_onNatureChange_fn(e: UTSJSONObject) {
        console.log("性质界定选择器被点击，事件对象:", e, " at pages/record/add.uvue:865")
        this.selectedNatureIndex = e.getJSON("detail")?.getNumber("value") ?: -1
        console.log("选择的索引:", this.selectedNatureIndex, " at pages/record/add.uvue:867")
        if (this.selectedNatureIndex !== -1) {
            this.formData.natureId = this.natureOptions[this.selectedNatureIndex].id
            console.log("选择的性质界定:", this.natureOptions[this.selectedNatureIndex], " at pages/record/add.uvue:870")
        } else {
            this.formData.natureId = ""
        }
    }
    open var showNaturePicker = ::gen_showNaturePicker_fn
    open fun gen_showNaturePicker_fn() {
        if (this.natureOptions.length > 0) {
            this.showNaturePopup = true
            console.log("显示性质界定选择弹窗", " at pages/record/add.uvue:880")
        }
    }
    open var hideNaturePicker = ::gen_hideNaturePicker_fn
    open fun gen_hideNaturePicker_fn() {
        this.showNaturePopup = false
        console.log("隐藏性质界定选择弹窗", " at pages/record/add.uvue:887")
    }
    open var selectNature = ::gen_selectNature_fn
    open fun gen_selectNature_fn(index: Number) {
        console.log("选择性质界定，索引:", index, " at pages/record/add.uvue:892")
        this.selectedNatureIndex = index
        if (index !== -1) {
            this.formData.natureId = this.natureOptions[index].id
            console.log("选择的性质界定:", this.natureOptions[index], " at pages/record/add.uvue:896")
        } else {
            this.formData.natureId = ""
        }
        this.hideNaturePicker()
    }
    open var showProfessionPicker = ::gen_showProfessionPicker_fn
    open fun gen_showProfessionPicker_fn() {
        if (this.professionOptions.length > 0) {
            this.showProfessionPopup = true
            console.log("显示专业选择弹窗", " at pages/record/add.uvue:907")
        }
    }
    open var hideProfessionPicker = ::gen_hideProfessionPicker_fn
    open fun gen_hideProfessionPicker_fn() {
        this.showProfessionPopup = false
        console.log("隐藏专业选择弹窗", " at pages/record/add.uvue:914")
    }
    open var selectProfession = ::gen_selectProfession_fn
    open fun gen_selectProfession_fn(index: Number) {
        console.log("选择专业，索引:", index, " at pages/record/add.uvue:919")
        this.selectedProfessionIndex = index
        if (index !== -1) {
            this.formData.professionId = this.professionOptions[index].id
            console.log("选择的专业:", this.professionOptions[index], " at pages/record/add.uvue:923")
        } else {
            this.formData.professionId = ""
        }
        this.hideProfessionPicker()
    }
    open var showInspectorPicker = ::gen_showInspectorPicker_fn
    open fun gen_showInspectorPicker_fn() {
        console.log("点击检查人选择器", " at pages/record/add.uvue:932")
        if (this.inspectorOptions.length === 0) {
            console.log("检查人数据为空，开始加载数据", " at pages/record/add.uvue:935")
            this.loadInspectorOptions()
        }
        this.showInspectorPopup = true
        console.log("显示检查人选择弹窗，当前选项数量:", this.inspectorOptions.length, " at pages/record/add.uvue:939")
    }
    open var hideInspectorPicker = ::gen_hideInspectorPicker_fn
    open fun gen_hideInspectorPicker_fn() {
        this.showInspectorPopup = false
        console.log("隐藏检查人选择弹窗", " at pages/record/add.uvue:945")
    }
    open var selectInspector = ::gen_selectInspector_fn
    open fun gen_selectInspector_fn(index: Number) {
        console.log("选择检查人，索引:", index, " at pages/record/add.uvue:950")
        this.selectedInspectorIndex = index
        if (index !== -1) {
            this.formData.inspector = this.inspectorOptions[index].name
            console.log("选择的检查人:", this.inspectorOptions[index], " at pages/record/add.uvue:954")
        } else {
            this.formData.inspector = ""
        }
        this.hideInspectorPicker()
    }
    open var showAssessmentPicker = ::gen_showAssessmentPicker_fn
    open fun gen_showAssessmentPicker_fn() {
        if (this.assessmentOptions.length > 0) {
            this.showAssessmentPopup = true
            console.log("显示考核情况选择弹窗", " at pages/record/add.uvue:965")
        }
    }
    open var hideAssessmentPicker = ::gen_hideAssessmentPicker_fn
    open fun gen_hideAssessmentPicker_fn() {
        this.showAssessmentPopup = false
        console.log("隐藏考核情况选择弹窗", " at pages/record/add.uvue:972")
    }
    open var selectAssessment = ::gen_selectAssessment_fn
    open fun gen_selectAssessment_fn(index: Number) {
        console.log("选择考核情况，索引:", index, " at pages/record/add.uvue:977")
        this.selectedAssessmentIndex = index
        if (index !== -1) {
            this.formData.scoreId = this.assessmentOptions[index].id
            console.log("选择的考核情况:", this.assessmentOptions[index], " at pages/record/add.uvue:981")
        } else {
            this.formData.scoreId = ""
        }
        this.hideAssessmentPicker()
    }
    open var showDepartmentPicker = ::gen_showDepartmentPicker_fn
    open fun gen_showDepartmentPicker_fn() {
        console.log("点击部门选择器", " at pages/record/add.uvue:990")
        if (this.departmentOptions.length === 0) {
            console.log("部门数据为空，开始加载数据", " at pages/record/add.uvue:993")
            this.loadDepartments()
        }
        this.showDepartmentPopup = true
        console.log("显示部门选择弹窗，当前选项数量:", this.departmentOptions.length, " at pages/record/add.uvue:997")
    }
    open var hideDepartmentPicker = ::gen_hideDepartmentPicker_fn
    open fun gen_hideDepartmentPicker_fn() {
        this.showDepartmentPopup = false
        console.log("隐藏部门选择弹窗", " at pages/record/add.uvue:1003")
    }
    open var selectDepartment = ::gen_selectDepartment_fn
    open fun gen_selectDepartment_fn(index: Number) {
        console.log("选择部门，索引:", index, " at pages/record/add.uvue:1008")
        this.selectedDepartmentIndex = index
        if (index !== -1) {
            this.formData.departmentId = this.departmentOptions[index].id
            console.log("选择的部门:", this.departmentOptions[index], " at pages/record/add.uvue:1012")
        } else {
            this.formData.departmentId = ""
        }
        this.hideDepartmentPicker()
    }
    open var setCurrentUserDepartment = ::gen_setCurrentUserDepartment_fn
    open fun gen_setCurrentUserDepartment_fn() {
        val userInfoStr = uni_getStorageSync("userInfo") as String
        if (userInfoStr == null || userInfoStr == "") {
            console.log("未找到用户信息，无法设置默认部门", " at pages/record/add.uvue:1023")
            return
        }
        try {
            val userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/add.uvue:1028") as UTSJSONObject
            var userDeptId = userInfo.getString("deptId") ?: userInfo.getString("dept_id") ?: ""
            if (userDeptId === "") {
                val deptIdNum = userInfo.getNumber("deptId") ?: userInfo.getNumber("dept_id")
                if (deptIdNum != null) {
                    userDeptId = deptIdNum.toString(10)
                }
            }
            val userDeptName = userInfo.getString("deptName") ?: userInfo.getString("dept_name") ?: ""
            console.log("当前用户部门ID:", userDeptId, "部门名称:", userDeptName, " at pages/record/add.uvue:1040")
            console.log("原始用户信息:", userInfo, " at pages/record/add.uvue:1041")
            if (userDeptId !== "") {
                run {
                    var i: Number = 0
                    while(i < this.departmentOptions.length){
                        if (this.departmentOptions[i].id == userDeptId) {
                            this.selectedDepartmentIndex = i
                            this.formData.departmentId = userDeptId
                            console.log("已设置默认部门:", this.departmentOptions[i], " at pages/record/add.uvue:1049")
                            return
                        }
                        i++
                    }
                }
                console.log("根据部门ID未找到匹配的部门选项", " at pages/record/add.uvue:1053")
            }
            if (userDeptName !== "") {
                run {
                    var i: Number = 0
                    while(i < this.departmentOptions.length){
                        if (this.departmentOptions[i].name == userDeptName) {
                            this.selectedDepartmentIndex = i
                            this.formData.departmentId = this.departmentOptions[i].id
                            console.log("已根据名称设置默认部门:", this.departmentOptions[i], " at pages/record/add.uvue:1062")
                            return
                        }
                        i++
                    }
                }
            }
            console.log("未找到匹配的部门，保持默认未选择状态", " at pages/record/add.uvue:1068")
        }
         catch (error: Throwable) {
            console.error("解析用户信息失败:", error, " at pages/record/add.uvue:1070")
        }
    }
    open var onRectificationDateChange = ::gen_onRectificationDateChange_fn
    open fun gen_onRectificationDateChange_fn(e: UTSJSONObject) {
        console.log("整改时间选择器触发，事件对象:", e, " at pages/record/add.uvue:1075")
        this.formData.rectificationDate = e.getJSON("detail")?.getString("value") ?: ""
        console.log("选择的整改时间:", this.formData.rectificationDate, " at pages/record/add.uvue:1077")
    }
    open var initDateArrays = ::gen_initDateArrays_fn
    open fun gen_initDateArrays_fn() {
        val currentYear = Date().getFullYear()
        this.years = _uA()
        run {
            var i = currentYear
            while(i <= currentYear + 2){
                this.years.push(i)
                i++
            }
        }
        this.months = _uA()
        run {
            var i: Number = 1
            while(i <= 12){
                this.months.push(i)
                i++
            }
        }
        this.updateDays()
        val yearIndex = this.years.findIndex(fun(year): Boolean {
            return year === this.selectedYear
        }
        )
        val monthIndex = (this.selectedMonth as Number) - 1
        val dayIndex = this.days.findIndex(fun(day): Boolean {
            return day === this.selectedDay
        }
        )
        this.pickerValue = _uA(
            if (yearIndex >= 0) {
                yearIndex
            } else {
                0
            }
            ,
            monthIndex,
            if (dayIndex >= 0) {
                dayIndex
            } else {
                0
            }
        )
        console.log("初始化日期数组:", _uO("selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "selectedDay" to this.selectedDay, "yearIndex" to yearIndex, "monthIndex" to monthIndex, "dayIndex" to dayIndex, "pickerValue" to this.pickerValue, "daysArray" to this.days), " at pages/record/add.uvue:1108")
    }
    open var updateDays = ::gen_updateDays_fn
    open fun gen_updateDays_fn() {
        val daysInMonth = Date(this.selectedYear, this.selectedMonth, 0).getDate()
        this.days = _uA()
        run {
            var i: Number = 1
            while(i <= daysInMonth){
                this.days.push(i)
                i++
            }
        }
    }
    open var showDatePicker = ::gen_showDatePicker_fn
    open fun gen_showDatePicker_fn() {
        if (this.formData.rectificationDate !== "") {
            val dateParts = this.formData.rectificationDate.split("-")
            if (dateParts.length === 3) {
                this.selectedYear = parseInt(dateParts[0])
                this.selectedMonth = parseInt(dateParts[1])
                this.selectedDay = parseInt(dateParts[2])
            }
        }
        this.updateDays()
        val yearIndex = this.years.findIndex(fun(year): Boolean {
            return year === this.selectedYear
        }
        )
        val monthIndex = this.selectedMonth - 1
        var dayIndex = this.days.findIndex(fun(day): Boolean {
            return day === this.selectedDay
        }
        )
        if (dayIndex < 0) {
            dayIndex = (this.days.length - 1) as Int
            this.selectedDay = this.days[dayIndex]
            console.log("日期不存在，调整为月末:", this.selectedDay, " at pages/record/add.uvue:1151")
        }
        this.pickerValue = _uA(
            if (yearIndex >= 0) {
                yearIndex
            } else {
                0
            }
            ,
            monthIndex,
            dayIndex
        )
        console.log("显示日期选择器:", _uO("selectedDate" to this.formData.rectificationDate, "selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "selectedDay" to this.selectedDay, "yearIndex" to yearIndex, "monthIndex" to monthIndex, "dayIndex" to dayIndex, "pickerValue" to this.pickerValue), " at pages/record/add.uvue:1156")
        this.datePickerVisible = true
    }
    open var hideDatePicker = ::gen_hideDatePicker_fn
    open fun gen_hideDatePicker_fn() {
        this.datePickerVisible = false
    }
    open var onPickerChange = ::gen_onPickerChange_fn
    open fun gen_onPickerChange_fn(e: UniPickerViewChangeEvent) {
        val value = e.detail.value as UTSArray<Number>
        val oldDaysLength = this.days.length
        console.log("picker原始变化事件:", _uO("value" to value, "yearsArray" to this.years, "monthsArray" to this.months, "daysArray" to this.days, "oldDaysLength" to oldDaysLength), " at pages/record/add.uvue:1178")
        if (this.pickerDebounceTimer != null) {
            clearTimeout(this.pickerDebounceTimer as Number)
        }
        this.selectedYear = this.years[value[0]]
        this.selectedMonth = value[1] + 1
        console.log("更新日期数组前:", _uO("selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "dayIndex" to value[2], "daysArrayLength" to this.days.length), " at pages/record/add.uvue:1195")
        this.updateDays()
        console.log("更新日期数组后:", _uO("newDaysArrayLength" to this.days.length, "newDaysArray" to this.days, "dayIndex" to value[2]), " at pages/record/add.uvue:1205")
        if (value[2] < this.days.length) {
            this.selectedDay = this.days[value[2]]
        } else {
            val lastDayIndex = (this.days.length - 1) as Int
            this.selectedDay = this.days[lastDayIndex]
            value[2] = lastDayIndex
            console.log("日期索引超出范围，调整为:", value[2], "对应日期:", this.selectedDay, " at pages/record/add.uvue:1219")
        }
        val correctDayIndex = this.days.findIndex(fun(day): Boolean {
            return day === this.selectedDay
        }
        )
        if (correctDayIndex >= 0 && correctDayIndex !== value[2]) {
            value[2] = correctDayIndex
            console.log("重新计算日期索引:", correctDayIndex, "对应日期:", this.selectedDay, " at pages/record/add.uvue:1226")
        }
        this.pickerValue = value
        console.log("设置辅助滑动定时器，200ms后执行", " at pages/record/add.uvue:1233")
        this.pickerDebounceTimer = setTimeout(fun(){
            console.log("防抖定时器触发，开始执行辅助滑动", " at pages/record/add.uvue:1235")
            this.snapToNearestItem()
        }
        , 200)
        console.log("日期选择最终结果:", _uO("selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "selectedDay" to this.selectedDay, "pickerValue" to this.pickerValue), " at pages/record/add.uvue:1239")
    }
    open var snapToNearestItem = ::gen_snapToNearestItem_fn
    open fun gen_snapToNearestItem_fn() {
        console.log("执行辅助滑动，当前pickerValue:", this.pickerValue, " at pages/record/add.uvue:1249")
        var needUpdate = false
        val newPickerValue = this.pickerValue.slice()
        val yearIndex = this.pickerValue[0]
        val nearestYear = Math.round(yearIndex)
        if (yearIndex !== nearestYear) {
            newPickerValue[0] = nearestYear
            needUpdate = true
            console.log("\u8F85\u52A9\u6ED1\u52A8\uFF1A\u5E74\u4EFD\u4ECE\u7D22\u5F15" + yearIndex + "\u8C03\u6574\u5230" + nearestYear, " at pages/record/add.uvue:1261")
        }
        val monthIndex = this.pickerValue[1]
        val nearestMonth = Math.round(monthIndex)
        if (monthIndex !== nearestMonth) {
            newPickerValue[1] = nearestMonth
            needUpdate = true
            console.log("\u8F85\u52A9\u6ED1\u52A8\uFF1A\u6708\u4EFD\u4ECE\u7D22\u5F15" + monthIndex + "\u8C03\u6574\u5230" + nearestMonth, " at pages/record/add.uvue:1270")
        }
        val dayIndex = this.pickerValue[2]
        val nearestDay = Math.round(dayIndex)
        if (nearestDay < this.days.length && dayIndex !== nearestDay) {
            newPickerValue[2] = nearestDay
            needUpdate = true
            console.log("\u8F85\u52A9\u6ED1\u52A8\uFF1A\u65E5\u671F\u4ECE\u7D22\u5F15" + dayIndex + "\u8C03\u6574\u5230" + nearestDay, " at pages/record/add.uvue:1279")
        }
        if (!needUpdate) {
            console.log("辅助滑动：当前位置已经精确，无需调整", " at pages/record/add.uvue:1284")
            return
        }
        if (needUpdate) {
            setTimeout(fun(){
                this.pickerValue = newPickerValue
                if (newPickerValue[0] < this.years.length) {
                    this.selectedYear = this.years[newPickerValue[0]]
                }
                if (newPickerValue[1] < this.months.length) {
                    this.selectedMonth = newPickerValue[1] + 1
                }
                if (newPickerValue[2] < this.days.length) {
                    this.selectedDay = this.days[newPickerValue[2]]
                }
                console.log("辅助滑动完成，新的选择值:", _uO("pickerValue" to this.pickerValue, "selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "selectedDay" to this.selectedDay), " at pages/record/add.uvue:1305")
            }
            , 50)
        }
    }
    open var confirmDatePicker = ::gen_confirmDatePicker_fn
    open fun gen_confirmDatePicker_fn() {
        val selectedYear = this.years[this.pickerValue[0]]
        val selectedMonth = this.pickerValue[1] + 1
        val selectedDay = this.days[this.pickerValue[2]]
        val year = selectedYear.toString(10).padStart(4, "0")
        val month = selectedMonth.toString(10).padStart(2, "0")
        val day = selectedDay.toString(10).padStart(2, "0")
        val newDate = "" + year + "-" + month + "-" + day
        console.log("确认日期选择:", _uO("pickerValue" to this.pickerValue, "yearsArray" to this.years, "monthsArray" to this.months, "daysArray" to this.days, "selectedYear" to selectedYear, "selectedMonth" to selectedMonth, "selectedDay" to selectedDay, "formattedDate" to newDate), " at pages/record/add.uvue:1327")
        this.formData.rectificationDate = newDate
        this.hideDatePicker()
    }
    open var chooseImage = ::gen_chooseImage_fn
    open fun gen_chooseImage_fn() {
        if (this.choosingImage) {
            console.log("正在选择图片中，忽略重复点击", " at pages/record/add.uvue:1346")
            return
        }
        this.choosingImage = true
        console.log("选择图片前，当前图片数量:", this.imageList.length, " at pages/record/add.uvue:1351")
        console.log("当前图片列表:", this.imageList, " at pages/record/add.uvue:1352")
        uni_chooseImage(ChooseImageOptions(count = 3 - this.imageList.length, sizeType = _uA(
            "compressed"
        ), sourceType = _uA(
            "camera",
            "album"
        ), success = fun(res){
            console.log("选择的图片路径:", res.tempFilePaths, " at pages/record/add.uvue:1358")
            console.log("选择的图片数量:", res.tempFilePaths.length, " at pages/record/add.uvue:1359")
            val newImages = res.tempFilePaths.filter(fun(path): Boolean {
                return !this.imageList.includes(path)
            }
            )
            console.log("过滤重复后的新图片:", newImages, " at pages/record/add.uvue:1363")
            this.imageList = this.imageList.concat(newImages)
            console.log("合并后图片数量:", this.imageList.length, " at pages/record/add.uvue:1366")
            console.log("合并后图片列表:", this.imageList, " at pages/record/add.uvue:1367")
            this.choosingImage = false
        }
        , fail = fun(err){
            console.log("选择图片失败:", err, " at pages/record/add.uvue:1371")
            this.choosingImage = false
        }
        ))
    }
    open var removeImage = ::gen_removeImage_fn
    open fun gen_removeImage_fn(index: Number) {
        this.imageList.splice(index, 1)
    }
    open var previewImage = ::gen_previewImage_fn
    open fun gen_previewImage_fn(index: Number) {
        uni_previewImage(PreviewImageOptions(current = index, urls = this.imageList))
    }
    open var validateForm = ::gen_validateForm_fn
    open fun gen_validateForm_fn(): Boolean {
        if (this.formData.responsibleUnit == null || this.formData.responsibleUnit == "") {
            uni_showToast(ShowToastOptions(title = "请输入责任单位", icon = "none"))
            return false
        }
        if (this.formData.responsiblePerson == null || this.formData.responsiblePerson.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请输入责任人", icon = "none"))
            return false
        }
        if (this.formData.natureId == null || this.formData.natureId == "") {
            uni_showToast(ShowToastOptions(title = "请选择性质界定", icon = "none"))
            return false
        }
        if (this.formData.professionId == null || this.formData.professionId == "") {
            uni_showToast(ShowToastOptions(title = "请选择专业", icon = "none"))
            return false
        }
        if (this.formData.problemDescription == null || this.formData.problemDescription.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请输入问题描述", icon = "none"))
            return false
        }
        if (this.formData.location == null || this.formData.location == "") {
            uni_showToast(ShowToastOptions(title = "请输入发生部位", icon = "none"))
            return false
        }
        if (this.formData.inspector == null || this.formData.inspector.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请输入检查人", icon = "none"))
            return false
        }
        if (this.formData.groupLeader == null || this.formData.groupLeader.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请输入检查组组长", icon = "none"))
            return false
        }
        if (this.formData.deputyLeader == null || this.formData.deputyLeader.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请输入检查组副组长", icon = "none"))
            return false
        }
        if (this.formData.departmentId == null || this.formData.departmentId == "") {
            uni_showToast(ShowToastOptions(title = "请选择所属部门", icon = "none"))
            return false
        }
        if (this.formData.rectificationDate == null || this.formData.rectificationDate == "") {
            uni_showToast(ShowToastOptions(title = "请选择整改时间", icon = "none"))
            return false
        }
        return true
    }
    open var uploadImages = ::gen_uploadImages_fn
    open fun gen_uploadImages_fn(): UTSPromise<UTSArray<String>> {
        return wrapUTSPromise(suspend w@{
                console.log("开始上传图片，图片数量:", this.imageList.length, " at pages/record/add.uvue:1481")
                console.log("待上传图片列表:", this.imageList, " at pages/record/add.uvue:1482")
                if (this.imageList.length === 0) {
                    return@w _uA()
                }
                val uploadPromises = this.imageList.map(fun(imagePath): UTSPromise<String> {
                    return UTSPromise<String>(fun(resolve, reject){
                        val token = uni_getStorageSync("token")
                        console.log("开始上传图片:", imagePath, " at pages/record/add.uvue:1490")
                        console.log("使用token:", if (token != null) {
                            "已获取"
                        } else {
                            "未获取"
                        }
                        , " at pages/record/add.uvue:1491")
                        uni_uploadFile(UploadFileOptions(url = getApiUrl("/upload/image"), filePath = imagePath, name = "file", header = object : UTSJSONObject() {
                            var Authorization = "Bearer " + token
                        }, success = fun(res){
                            console.log("上传响应:", res, " at pages/record/add.uvue:1503")
                            console.log("上传响应数据:", res.data, " at pages/record/add.uvue:1504")
                            try {
                                val data = UTSAndroid.consoleDebugError(JSON.parse(res.data as String), " at pages/record/add.uvue:1507") as UTSJSONObject
                                console.log("解析后的数据:", data, " at pages/record/add.uvue:1508")
                                if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                                    val dataObj = data.getJSON("data")
                                    val imageUrl = dataObj?.getString("filePath") ?: ""
                                    console.log("获取到的图片URL:", imageUrl, " at pages/record/add.uvue:1513")
                                    if (imageUrl != null && imageUrl != "") {
                                        resolve(imageUrl)
                                    } else {
                                        reject(UTSError("上传成功但未获取到图片URL"))
                                    }
                                } else {
                                    console.log("上传失败，状态码:", res.statusCode, "错误码:", data.getNumber("code"), " at pages/record/add.uvue:1521")
                                    reject(UTSError(data.getString("message") ?: "上传失败"))
                                }
                            }
                             catch (e: Throwable) {
                                console.error("解析上传响应失败:", e, " at pages/record/add.uvue:1525")
                                console.error("原始响应数据:", res.data, " at pages/record/add.uvue:1526")
                                reject(UTSError("解析响应失败"))
                            }
                        }
                        , fail = fun(err){
                            console.log("上传图片失败:", err, " at pages/record/add.uvue:1531")
                            reject(UTSError("\u4E0A\u4F20\u5931\u8D25: " + (if (err.errMsg != null) {
                                err.errMsg
                            } else {
                                "网络错误"
                            }
                            )))
                        }
                        ))
                    }
                    )
                }
                )
                try {
                    val imageUrls = await(UTSPromise.all(uploadPromises))
                    console.log("所有图片上传成功，返回URL数量:", imageUrls.length, " at pages/record/add.uvue:1540")
                    console.log("上传成功的图片URLs:", imageUrls, " at pages/record/add.uvue:1541")
                    return@w imageUrls
                }
                 catch (error: Throwable) {
                    console.error("图片上传失败:", error, " at pages/record/add.uvue:1544")
                    console.error("错误详情:", if (error.message != null) {
                        error.message
                    } else {
                        error
                    }
                    , " at pages/record/add.uvue:1545")
                    throw error
                }
        })
    }
    open var submitRecord = ::gen_submitRecord_fn
    open fun gen_submitRecord_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend w@{
                console.log("submitRecord方法被调用", " at pages/record/add.uvue:1551")
                if (!this.validateForm() || this.submitting) {
                    console.log("表单验证失败或正在提交中", " at pages/record/add.uvue:1553")
                    return@w
                }
                this.submitting = true
                try {
                    var imageUrls: UTSArray<String> = _uA()
                    if (this.imageList.length > 0) {
                        uni_showLoading(ShowLoadingOptions(title = "上传图片中..."))
                        try {
                            imageUrls = await(this.uploadImages())
                            uni_hideLoading()
                        }
                         catch (uploadError: Throwable) {
                            uni_hideLoading()
                            console.error("图片上传失败，终止提交:", uploadError, " at pages/record/add.uvue:1571")
                            uni_showToast(ShowToastOptions(title = "图片上传失败，请检查网络连接", icon = "none", duration = 3000))
                            this.submitting = false
                            return@w
                        }
                    }
                    uni_showLoading(ShowLoadingOptions(title = "提交中..."))
                    val token = uni_getStorageSync("token")
                    val submitData: UTSJSONObject = _uO("__\$originalPosition" to UTSSourceMapPosition("submitData", "pages/record/add.uvue", 1588, 12), "responsibleUnit" to this.formData.responsibleUnit, "responsiblePerson" to this.formData.responsiblePerson, "natureId" to this.formData.natureId, "scoreId" to this.formData.scoreId, "assessmentAmount" to this.formData.assessmentAmount, "safetyScore" to this.formData.safetyScore, "professionId" to this.formData.professionId, "problemDescription" to this.formData.problemDescription, "location" to this.formData.location, "inspector" to this.formData.inspector, "groupLeader" to this.formData.groupLeader, "deputyLeader" to this.formData.deputyLeader, "departmentId" to this.formData.departmentId, "rectificationDate" to this.formData.rectificationDate, "imageUrls" to if (imageUrls.length > 0) {
                        JSON.stringify(imageUrls)
                    } else {
                        ""
                    }
                    )
                    console.log("调试：准备提交的数据:", submitData, " at pages/record/add.uvue:1607")
                    console.log("调试：部门ID字段:", this.formData.departmentId, " at pages/record/add.uvue:1608")
                    console.log("调试：当前选中的部门索引:", this.selectedDepartmentIndex, " at pages/record/add.uvue:1609")
                    if (this.selectedDepartmentIndex >= 0 && this.selectedDepartmentIndex < this.departmentOptions.length) {
                        console.log("调试：当前选中的部门对象:", this.departmentOptions[this.selectedDepartmentIndex], " at pages/record/add.uvue:1611")
                    }
                    uni_showToast(ShowToastOptions(title = "\u63D0\u4EA4\u90E8\u95E8ID: " + (this.formData.departmentId ?: "空"), icon = "none", duration = 2000))
                    uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record/submit"), method = "POST", header = object : UTSJSONObject() {
                        var Authorization = "Bearer " + token
                        var `Content-Type` = "application/json"
                    }, data = submitData, success = fun(res){
                        uni_hideLoading()
                        try {
                            val data = res.data as UTSJSONObject
                            if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                                uni_showModal(ShowModalOptions(title = "上传成功", content = "是否继续上传？", confirmText = "是", cancelText = "否", success = fun(modalRes){
                                    if (modalRes.confirm) {
                                        this.resetFormData()
                                        uni_showToast(ShowToastOptions(title = "已清空表单，可继续录入", icon = "success"))
                                    } else if (modalRes.cancel) {
                                        uni_reLaunch(ReLaunchOptions(url = "/pages/weekly/weekly"))
                                    }
                                }))
                            } else {
                                uni_showToast(ShowToastOptions(title = data.getString("message") ?: "提交失败", icon = "none"))
                            }
                        }
                         catch (e: Throwable) {
                            uni_showToast(ShowToastOptions(title = "提交失败", icon = "none"))
                        }
                        this.submitting = false
                    }
                    , fail = fun(error){
                        console.error("提交记录失败:", error, " at pages/record/add.uvue:1670")
                        uni_hideLoading()
                        uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
                        this.submitting = false
                    }
                    ))
                }
                 catch (error: Throwable) {
                    console.error("提交记录失败:", error, " at pages/record/add.uvue:1680")
                    uni_hideLoading()
                    uni_showToast(ShowToastOptions(title = "操作失败", icon = "none"))
                    this.submitting = false
                }
        })
    }
    open var resetFormData = ::gen_resetFormData_fn
    open fun gen_resetFormData_fn() {
        this.formData = RecordFormData(responsibleUnit = "", responsiblePerson = "", natureId = "", scoreId = "", assessmentAmount = "", safetyScore = "", professionId = "", problemDescription = "", location = "", inspector = "", groupLeader = "", deputyLeader = "", departmentId = "", rectificationDate = "")
        this.imageList = _uA<String>()
        this.selectedNatureIndex = -1
        this.selectedAssessmentIndex = -1
        this.selectedProfessionIndex = -1
        this.selectedDepartmentIndex = -1
        this.selectedInspectorIndex = -1
        this.setCurrentUserDepartment()
        this.scrollTop = 0
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
                return _uM("container" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 120, "paddingLeft" to 20, "backgroundColor" to "#f5f5f5")), "form-item" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 15, "paddingRight" to 15, "paddingBottom" to 15, "paddingLeft" to 15, "marginBottom" to 15, "boxShadow" to "0 1px 3px rgba(0,0,0,0.1)")), "form-item-inline" to _pS(_uM("marginBottom" to 20, "display" to "flex", "alignItems" to "center", "flexWrap" to "nowrap")), "form-label" to _pS(_uM("fontSize" to 16, "color" to "#333333", "marginBottom" to 10, "fontWeight" to "400")), "form-label-inline" to _pS(_uM("fontSize" to 16, "color" to "#333333", "fontWeight" to "400", "minWidth" to 80, "textAlign" to "left")), "form-label-left" to _pS(_uM("fontSize" to 16, "color" to "#333333", "marginBottom" to 10, "fontWeight" to "400", "textAlign" to "left")), "form-input-wrapper" to _pS(_uM("position" to "relative", "display" to "flex", "alignItems" to "center")), "form-input-flex" to _pS(_uM("flex" to 1, "display" to "flex", "alignItems" to "center")), "form-input" to _pS(_uM("flex" to 1, "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "backgroundImage" to "none", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "fontSize" to 16, "color" to "#333333", "boxSizing" to "border-box")), "input-suffix" to _pS(_uM("marginLeft" to 10, "fontSize" to 16, "color" to "#666666")), "form-picker" to _pS(_uM("width" to "100%")), "picker-content" to _uM("" to _uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "backgroundImage" to "none", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef"), ".disabled" to _uM("backgroundImage" to "none", "backgroundColor" to "#f5f5f5", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "opacity" to 0.6)), "custom-picker" to _uM("" to _uM("width" to "100%"), ".disabled" to _uM("opacity" to 0.6)), "custom-picker-inline" to _uM("" to _uM("flex" to 1, "display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "backgroundImage" to "none", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef"), ".disabled" to _uM("backgroundImage" to "none", "backgroundColor" to "#f5f5f5", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "opacity" to 0.6)), "custom-picker-full" to _uM("" to _uM("width" to "100%", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "borderTopLeftRadius" to "8rpx", "borderTopRightRadius" to "8rpx", "borderBottomRightRadius" to "8rpx", "borderBottomLeftRadius" to "8rpx", "backgroundColor" to "#ffffff", "minHeight" to "80rpx", "display" to "flex", "alignItems" to "center"), ".disabled" to _uM("backgroundImage" to "none", "backgroundColor" to "#f5f5f5", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "opacity" to 0.6)), "date-picker-full" to _pS(_uM("width" to "100%", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "borderTopLeftRadius" to "8rpx", "borderTopRightRadius" to "8rpx", "borderBottomRightRadius" to "8rpx", "borderBottomLeftRadius" to "8rpx", "backgroundColor" to "#ffffff", "height" to "auto", "minHeight" to "80rpx", "display" to "flex", "alignItems" to "center", "boxSizing" to "border-box")), "picker-text-left" to _uM("" to _uM("fontSize" to 16, "color" to "#333333", "flex" to 1), ".placeholder" to _uM("color" to "#999999")), "picker-text-full" to _uM("" to _uM("fontSize" to "32rpx", "color" to "#333333", "width" to "100%", "textAlign" to "left"), ".placeholder" to _uM("color" to "#999999")), "picker-arrow-right" to _pS(_uM("fontSize" to 14, "color" to "#999999")), "popup-overlay" to _pS(_uM("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0.5)", "zIndex" to 1000, "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "popup-content" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "width" to "80%", "maxWidth" to 400, "maxHeight" to 480, "overflow" to "hidden")), "popup-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 16, "paddingRight" to 20, "paddingBottom" to 16, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0")), "popup-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "700", "color" to "#333333")), "popup-close" to _pS(_uM("fontSize" to 24, "color" to "#999999", "width" to 30, "height" to 30, "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "popup-list" to _pS(_uM("maxHeight" to 300)), "popup-item" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 16, "paddingRight" to 20, "paddingBottom" to 16, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f8f9fa", "transitionProperty" to "backgroundColor", "transitionDuration" to "0.2s", "backgroundImage:hover" to "none", "backgroundColor:hover" to "#f8f9fa", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "popup-item-text" to _pS(_uM("fontSize" to 16, "color" to "#333333")), "popup-item-check" to _pS(_uM("fontSize" to 18, "color" to "#007aff", "fontWeight" to "bold")), "date-picker-wrapper" to _pS(_uM("width" to "100%", "display" to "flex", "flexDirection" to "column", "alignItems" to "center")), "date-picker-label" to _pS(_uM("fontSize" to 16, "color" to "#666666", "textAlign" to "center", "marginBottom" to 10)), "date-picker-popup" to _pS(_uM("width" to "100%", "display" to "flex", "justifyContent" to "center", "alignItems" to "center")), "date-picker-button" to _pS(_uM("paddingTop" to 15, "paddingRight" to 30, "paddingBottom" to 15, "paddingLeft" to 30, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#007aff", "borderRightColor" to "#007aff", "borderBottomColor" to "#007aff", "borderLeftColor" to "#007aff", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "backgroundColor" to "#007aff", "display" to "flex", "justifyContent" to "center", "alignItems" to "center", "minWidth" to 120, "backgroundColor:active" to "#0056cc", "borderTopColor:active" to "#0056cc", "borderRightColor:active" to "#0056cc", "borderBottomColor:active" to "#0056cc", "borderLeftColor:active" to "#0056cc")), "date-picker-button-text" to _pS(_uM("fontSize" to 16, "color" to "#ffffff", "fontWeight" to "400", "textAlign" to "center")), "picker-text" to _uM("" to _uM("fontSize" to 16, "color" to "#333333", "flex" to 1), ".placeholder" to _uM("color" to "#999999")), "picker-arrow" to _pS(_uM("fontSize" to 14, "color" to "#999999")), "form-textarea" to _pS(_uM("width" to "100%", "minHeight" to 100, "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "backgroundImage" to "none", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "fontSize" to 16, "color" to "#333333", "boxSizing" to "border-box")), "char-count" to _pS(_uM("textAlign" to "right", "marginTop" to 5)), "image-list" to _pS(_uM("display" to "flex", "flexWrap" to "wrap")), "image-item" to _pS(_uM("position" to "relative", "width" to 80, "height" to 80)), "uploaded-image" to _pS(_uM("width" to "100%", "height" to "100%", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "backgroundImage" to "none", "backgroundColor" to "#f0f0f0")), "image-delete" to _pS(_uM("position" to "absolute", "top" to -5, "right" to -5, "width" to 20, "height" to 20, "backgroundImage" to "none", "backgroundColor" to "#ff4757", "borderTopLeftRadius" to 10, "borderTopRightRadius" to 10, "borderBottomRightRadius" to 10, "borderBottomLeftRadius" to 10, "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "image-add" to _pS(_uM("width" to 80, "height" to 80, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#cccccc", "borderRightColor" to "#cccccc", "borderBottomColor" to "#cccccc", "borderLeftColor" to "#cccccc", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "#fafafa")), "add-icon" to _pS(_uM("fontSize" to 24, "color" to "#999999", "marginBottom" to 5)), "add-text" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "submit-section" to _pS(_uM("position" to "fixed", "bottom" to 0, "left" to 0, "right" to 0, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopWidth" to 1, "borderTopStyle" to "solid", "borderTopColor" to "#f0f0f0")), "submit-btn" to _pS(_uM("width" to "100%", "height" to 48, "backgroundImage" to "none", "backgroundColor" to "#007AFF", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "fontSize" to 18, "fontWeight" to "bold", "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#cccccc", "color:disabled" to "#999999")), "date-picker-modal" to _pS(_uM("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundColor" to "rgba(0,0,0,0.5)", "display" to "flex", "alignItems" to "flex-end", "zIndex" to 1000)), "date-picker-content" to _pS(_uM("width" to "100%", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 16, "borderTopRightRadius" to 16, "borderBottomRightRadius" to 0, "borderBottomLeftRadius" to 0, "paddingBottom" to "env(safe-area-inset-bottom)")), "date-picker-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 16, "paddingRight" to 20, "paddingBottom" to 16, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0")), "date-picker-cancel" to _pS(_uM("fontSize" to 16, "color" to "#999999")), "date-picker-title" to _pS(_uM("fontSize" to 18, "color" to "#333333")), "date-picker-confirm" to _pS(_uM("fontSize" to 16, "color" to "#007AFF")), "date-picker-view" to _pS(_uM("height" to 320, "paddingTop" to 0, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "position" to "relative")), "picker-item" to _uM("" to _uM("height" to 60, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "fontSize" to 16, "color" to "#666666", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "cubic-bezier(0.4,0,0.2,1)", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "marginTop" to 4, "marginRight" to 8, "marginBottom" to 4, "marginLeft" to 8, "position" to "relative", "zIndex" to 2, "opacity" to 0.6, "backgroundColor" to "rgba(0,0,0,0.02)", "backgroundColor:hover" to "#f0f8ff", "color:hover" to "#007AFF"), ".selected" to _uM("backgroundImage" to "linear-gradient(135deg, #007AFF 0%, #0056CC 100%)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#ffffff", "fontWeight" to "700", "boxShadow" to "0 4px 12px rgba(0, 122, 255, 0.4), inset 0 1px 0 rgba(255, 255, 255, 0.2)", "transform" to "scale(1.05)", "zIndex" to 3, "opacity" to 1, "fontSize" to 18, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#ffffff", "borderRightColor" to "#ffffff", "borderBottomColor" to "#ffffff", "borderLeftColor" to "#ffffff", "textShadow" to "0 1px 2px rgba(0, 0, 0, 0.3)")), "picker-item-spacer" to _pS(_uM("pointerEvents" to "none", "opacity" to 0, "!backgroundImage" to "none", "!backgroundColor" to "rgba(0,0,0,0)")), "@TRANSITION" to _uM("popup-item" to _uM("property" to "backgroundColor", "duration" to "0.2s"), "picker-item" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "cubic-bezier(0.4,0,0.2,1)")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
