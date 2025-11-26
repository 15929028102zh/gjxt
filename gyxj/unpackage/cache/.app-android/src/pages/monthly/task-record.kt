@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
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
import io.dcloud.uniapp.extapi.chooseImage as uni_chooseImage
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfoSync as uni_getSystemInfoSync
import io.dcloud.uniapp.extapi.hideLoading as uni_hideLoading
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.uploadFile as uni_uploadFile
open class GenPagesMonthlyTaskRecord : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: UTSJSONObject) {
            val taskId = options.getString("taskId") ?: ""
            console.log(taskId, " at pages/monthly/task-record.uvue:553")
            if (taskId != null && taskId != "") {
                this.loadTaskInfo(taskId)
            }
            this.loadTypeOptions()
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
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "scroll-top" to _ctx.scrollTop, "style" to _nS(_uM("height" to (_ctx.containerHeight + "px"))), "enable-back-to-top" to "true"), _uA(
            _cE("view", _uM("class" to "header"), _uA(
                _cE("view", _uM("class" to "header-left", "onClick" to _ctx.goBack), null, 8, _uA(
                    "onClick"
                )),
                _cE("text", _uM("class" to "header-title"), "月检任务录入"),
                _cE("view", _uM("class" to "header-right"))
            )),
            _cE("view", _uM("class" to "task-info"), _uA(
                _cE("view", _uM("class" to "info-item"), _uA(
                    _cE("text", _uM("class" to "info-label"), "任务编号:"),
                    _cE("text", _uM("class" to "info-value"), _tD(if (_ctx.taskInfo != null) {
                        _ctx.taskInfo.taskNumber
                    } else {
                        ""
                    }
                    ), 1)
                )),
                _cE("view", _uM("class" to "info-item"), _uA(
                    _cE("text", _uM("class" to "info-label"), "月检时间:"),
                    _cE("text", _uM("class" to "info-value"), _tD(if (_ctx.taskInfo != null) {
                        _ctx.taskInfo.inspectionDate
                    } else {
                        ""
                    }
                    ), 1)
                )),
                _cE("view", _uM("class" to "info-item"), _uA(
                    _cE("text", _uM("class" to "info-label"), "检查部门:"),
                    _cE("text", _uM("class" to "info-value"), _tD(if (_ctx.taskInfo != null) {
                        _ctx.taskInfo.department
                    } else {
                        ""
                    }
                    ), 1)
                )),
                _cE("view", _uM("class" to "info-item"), _uA(
                    _cE("text", _uM("class" to "info-label"), "检查专业:"),
                    _cE("text", _uM("class" to "info-value"), _tD(if (_ctx.taskInfo != null) {
                        _ctx.taskInfo.profession
                    } else {
                        ""
                    }
                    ), 1)
                )),
                _cE("view", _uM("class" to "info-item"), _uA(
                    _cE("text", _uM("class" to "info-label"), "检查组长:"),
                    _cE("text", _uM("class" to "info-value"), _tD(if (_ctx.taskInfo != null) {
                        _ctx.taskInfo.groupLeader
                    } else {
                        ""
                    }
                    ), 1)
                )),
                _cE("view", _uM("class" to "info-item"), _uA(
                    _cE("text", _uM("class" to "info-label"), "检查副组长:"),
                    _cE("text", _uM("class" to "info-value"), _tD(_ctx.taskInfo.deputyLeader), 1)
                ))
            )),
            _cE("view", _uM("class" to "form-section"), _uA(
                _cE("view", _uM("class" to "section-title"), _uA(
                    _cE("text", _uM("class" to "title-text"), "问题录入"),
                    _cE("text", _uM("class" to "required-mark"), "*")
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "专业类别"),
                    _cE("view", _uM("class" to _nC(_uA(
                        "custom-picker-full",
                        _uM("disabled" to (_ctx.typeOptions.length === 0))
                    )), "onClick" to _ctx.showTypePicker), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.selectedTypeIndex === -1))
                        ))), _tD(if (_ctx.selectedTypeIndex === -1) {
                            "请选择专业类别"
                        } else {
                            if (_ctx.selectedTypeIndex < _ctx.typeOptions.length) {
                                (_ctx.typeOptions[_ctx.selectedTypeIndex] as OptionItem).name
                            } else {
                                "请选择专业类别"
                            }
                        }
                        ), 3)
                    ), 10, _uA(
                        "onClick"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "模块"),
                    _cE("view", _uM("class" to _nC(_uA(
                        "custom-picker-full",
                        _uM("disabled" to (_ctx.moduleOptions.length === 0))
                    )), "onClick" to _ctx.showModulePicker), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.selectedModuleIndex === -1))
                        ))), _tD(if (_ctx.selectedModuleIndex === -1) {
                            "请选择模块"
                        } else {
                            if (_ctx.selectedModuleIndex < _ctx.moduleOptions.length) {
                                (_ctx.moduleOptions[_ctx.selectedModuleIndex] as OptionItem).name
                            } else {
                                "请选择模块"
                            }
                        }
                        ), 3)
                    ), 10, _uA(
                        "onClick"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "责任"),
                    _cE("view", _uM("class" to _nC(_uA(
                        "custom-picker-full",
                        _uM("disabled" to (_ctx.responsibilityOptions.length === 0))
                    )), "onClick" to _ctx.showResponsibilityPicker), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.selectedResponsibilityIndex === -1))
                        ))), _tD(if (_ctx.selectedResponsibilityIndex === -1) {
                            "请选择责任"
                        } else {
                            if (_ctx.selectedResponsibilityIndex < _ctx.responsibilityOptions.length) {
                                (_ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex] as OptionItem).name
                            } else {
                                "请选择责任"
                            }
                        }
                        ), 3)
                    ), 10, _uA(
                        "onClick"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "问题"),
                    _cE("view", _uM("class" to _nC(_uA(
                        "custom-picker-full",
                        _uM("disabled" to (_ctx.issueOptions.length === 0))
                    )), "onClick" to _ctx.showIssuePicker), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.selectedIssueIndex === -1))
                        ))), _tD(if (_ctx.selectedIssueIndex === -1) {
                            "请选择问题"
                        } else {
                            if (_ctx.selectedIssueIndex < _ctx.issueOptions.length) {
                                (_ctx.issueOptions[_ctx.selectedIssueIndex] as OptionItem).name
                            } else {
                                "请选择问题"
                            }
                        }
                        ), 3)
                    ), 10, _uA(
                        "onClick"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "性质界定"),
                    _cE("view", _uM("class" to _nC(_uA(
                        "custom-picker-full",
                        _uM("disabled" to (_ctx.qualityOptions.length === 0))
                    )), "onClick" to _ctx.showQualityPicker), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.selectedQualityIndex === -1))
                        ))), _tD(if (_ctx.selectedQualityIndex === -1) {
                            "请选择性质界定"
                        } else {
                            if (_ctx.selectedQualityIndex < _ctx.qualityOptions.length) {
                                (_ctx.qualityOptions[_ctx.selectedQualityIndex] as OptionItem).name
                            } else {
                                "请选择性质界定"
                            }
                        }
                        ), 3)
                    ), 10, _uA(
                        "onClick"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "安全记分"),
                    _cE("input", _uM("class" to "form-input", "modelValue" to _ctx.formData.safetyScore, "onInput" to fun(`$event`: UniInputEvent){
                        _ctx.formData.safetyScore = `$event`.detail.value
                    }
                    , "placeholder" to "自动带出安全记分", "readonly" to ""), null, 40, _uA(
                        "modelValue",
                        "onInput"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label"), "问题描述"),
                    _cE("textarea", _uM("class" to "textarea-input", "modelValue" to _ctx.formData.problemDescription, "onInput" to fun(`$event`: UniInputEvent){
                        _ctx.formData.problemDescription = `$event`.detail.value
                    }
                    , "placeholder" to "请详细描述发现的问题", "maxlength" to "500"), null, 40, _uA(
                        "modelValue",
                        "onInput"
                    )),
                    _cE("text", _uM("class" to "char-count"), _tD(_ctx.formData.problemDescription.length) + "/500", 1)
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "发现位置"),
                    _cE("input", _uM("class" to "form-input", "modelValue" to _ctx.formData.location, "onInput" to fun(`$event`: UniInputEvent){
                        _ctx.formData.location = `$event`.detail.value
                    }
                    , "placeholder" to "请输入问题发现的具体位置"), null, 40, _uA(
                        "modelValue",
                        "onInput"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "责任单位"),
                    _cE("input", _uM("class" to "form-input", "modelValue" to _ctx.formData.responsibleUnit, "onInput" to fun(`$event`: UniInputEvent){
                        _ctx.formData.responsibleUnit = `$event`.detail.value
                    }
                    , "placeholder" to "请输入责任单位"), null, 40, _uA(
                        "modelValue",
                        "onInput"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "责任人"),
                    _cE("input", _uM("class" to "form-input", "modelValue" to _ctx.formData.responsiblePerson, "onInput" to fun(`$event`: UniInputEvent){
                        _ctx.formData.responsiblePerson = `$event`.detail.value
                    }
                    , "placeholder" to "请输入责任人"), null, 40, _uA(
                        "modelValue",
                        "onInput"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "整改期限"),
                    _cE("view", _uM("class" to "picker-container", "onClick" to _ctx.showDatePicker), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.formData.rectificationDate === ""))
                        ))), _tD(if (_ctx.formData.rectificationDate !== "") {
                            _ctx.formData.rectificationDate
                        } else {
                            "请选择整改期限"
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
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "检查人"),
                    _cE("input", _uM("class" to "form-input", "modelValue" to _ctx.formData.inspector, "onInput" to fun(`$event`: UniInputEvent){
                        _ctx.formData.inspector = `$event`.detail.value
                    }
                    , "placeholder" to "请输入检查人姓名"), null, 40, _uA(
                        "modelValue",
                        "onInput"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label"), "问题照片"),
                    _cE("view", _uM("class" to "photo-upload"), _uA(
                        _cE(Fragment, null, RenderHelpers.renderList(_ctx.formData.photos, fun(photo, index, __index, _cached): Any {
                            return _cE("view", _uM("key" to index, "class" to "photo-item"), _uA(
                                _cE("image", _uM("src" to _ctx.convertToHttpUrl(photo), "class" to "photo-preview", "onClick" to fun(){
                                    _ctx.previewPhoto(photo)
                                }
                                ), null, 8, _uA(
                                    "src",
                                    "onClick"
                                )),
                                _cE("view", _uM("class" to "photo-delete", "onClick" to fun(){
                                    _ctx.deletePhoto(index)
                                }
                                ), _uA(
                                    _cE("text", _uM("class" to "delete-icon"), "×")
                                ), 8, _uA(
                                    "onClick"
                                ))
                            ))
                        }
                        ), 128),
                        if (_ctx.formData.photos.length < 3) {
                            _cE("view", _uM("key" to 0, "class" to "photo-add", "onClick" to _ctx.addPhoto), _uA(
                                _cE("text", _uM("class" to "add-icon"), "+"),
                                _cE("text", _uM("class" to "add-text"), "添加照片")
                            ), 8, _uA(
                                "onClick"
                            ))
                        } else {
                            _cC("v-if", true)
                        }
                    )),
                    _cE("text", _uM("class" to "photo-tip"), "最多可上传3张照片")
                ))
            )),
            _cE("view", _uM("class" to "bottom-save-container"), _uA(
                _cE("view", _uM("class" to "bottom-save-btn", "onClick" to _ctx.saveRecord), _uA(
                    _cE("text", _uM("class" to "bottom-save-text"), "保存")
                ), 8, _uA(
                    "onClick"
                ))
            )),
            if (isTrue(_ctx.showProblemTypeModal)) {
                _cE("view", _uM("key" to 0, "class" to "picker-modal", "onClick" to _ctx.hideProblemTypePicker), _uA(
                    _cE("view", _uM("class" to "picker-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "picker-header"), _uA(
                            _cE("text", _uM("class" to "picker-cancel", "onClick" to _ctx.hideProblemTypePicker), "取消", 8, _uA(
                                "onClick"
                            )),
                            _cE("text", _uM("class" to "picker-title"), "选择问题类型"),
                            _cE("text", _uM("class" to "picker-confirm", "onClick" to _ctx.confirmProblemType), "确定", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "picker-list"), _uA(
                            _cE(Fragment, null, RenderHelpers.renderList(_ctx.problemTypes, fun(type, index, __index, _cached): Any {
                                return _cE("view", _uM("key" to index, "class" to _nC(_uA(
                                    "picker-item",
                                    _uM("selected" to (_ctx.selectedProblemType === type))
                                )), "onClick" to fun(){
                                    _ctx.selectProblemType(type)
                                }), _uA(
                                    _cE("text", _uM("class" to "picker-item-text"), _tD(type), 1),
                                    if (_ctx.selectedProblemType === type) {
                                        _cE("text", _uM("key" to 0, "class" to "picker-check"), "✓")
                                    } else {
                                        _cC("v-if", true)
                                    }
                                ), 10, _uA(
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
            if (isTrue(_ctx.showTypePopup)) {
                _cE("view", _uM("key" to 1, "class" to "popup-overlay", "onClick" to _ctx.hideTypePicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择专业类别"),
                            _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideTypePicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "popup-search"), _uA(
                            _cE("input", _uM("class" to "search-input", "modelValue" to _ctx.typeSearchKeyword, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.typeSearchKeyword = `$event`.detail.value
                            }, "placeholder" to "搜索专业类别..."), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("scroll-view", _uM("class" to "popup-list", "scroll-y" to "true"), _uA(
                            if (_ctx.filteredTypeOptions.length === 0) {
                                _cE("view", _uM("key" to 0, "class" to "popup-empty"), _uA(
                                    _cE("text", _uM("class" to "empty-text"), "暂无匹配的专业类别")
                                ))
                            } else {
                                _cE(Fragment, _uM("key" to 1), RenderHelpers.renderList(_ctx.filteredTypeOptions, fun(item, index, __index, _cached): Any {
                                    return _cE("view", _uM("key" to (item as OptionItem).id, "class" to _nC(_uA(
                                        "popup-item",
                                        if (_ctx.selectedTypeIndex >= 0 && _ctx.typeOptions[_ctx.selectedTypeIndex] != null && _ctx.typeOptions[_ctx.selectedTypeIndex].id === (item as OptionItem).id) {
                                            "popup-item-selected"
                                        } else {
                                            ""
                                        }
                                    )), "onClick" to fun(){
                                        _ctx.selectTypeFromFiltered(item)
                                    }), _uA(
                                        _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem).name), 1),
                                        if (isTrue(_ctx.selectedTypeIndex >= 0 && _ctx.typeOptions[_ctx.selectedTypeIndex] != null && _ctx.typeOptions[_ctx.selectedTypeIndex].id === (item as OptionItem).id)) {
                                            _cE("text", _uM("key" to 0, "class" to "popup-item-check"), "✓")
                                        } else {
                                            _cC("v-if", true)
                                        }
                                    ), 10, _uA(
                                        "onClick"
                                    ))
                                }), 128)
                            }
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
            if (isTrue(_ctx.showModulePopup)) {
                _cE("view", _uM("key" to 2, "class" to "popup-overlay", "onClick" to _ctx.hideModulePicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择模块"),
                            _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideModulePicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "popup-search"), _uA(
                            _cE("input", _uM("class" to "search-input", "modelValue" to _ctx.moduleSearchKeyword, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.moduleSearchKeyword = `$event`.detail.value
                            }, "placeholder" to "搜索模块..."), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("scroll-view", _uM("class" to "popup-list", "scroll-y" to "true"), _uA(
                            if (_ctx.moduleOptions.length === 0) {
                                _cE("view", _uM("key" to 0, "class" to "popup-loading"), _uA(
                                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                                ))
                            } else {
                                if (_ctx.filteredModuleOptions.length === 0) {
                                    _cE("view", _uM("key" to 1, "class" to "popup-empty"), _uA(
                                        _cE("text", _uM("class" to "empty-text"), "暂无匹配的模块")
                                    ))
                                } else {
                                    _cE(Fragment, _uM("key" to 2), RenderHelpers.renderList(_ctx.filteredModuleOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to (item as OptionItem).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.selectedModuleIndex >= 0 && _ctx.moduleOptions[_ctx.selectedModuleIndex] != null && _ctx.moduleOptions[_ctx.selectedModuleIndex].id === (item as OptionItem).id) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.selectModuleFromFiltered(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem).name), 1),
                                            if (isTrue(_ctx.selectedModuleIndex >= 0 && _ctx.moduleOptions[_ctx.selectedModuleIndex] != null && _ctx.moduleOptions[_ctx.selectedModuleIndex].id === (item as OptionItem).id)) {
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
            if (isTrue(_ctx.showResponsibilityPopup)) {
                _cE("view", _uM("key" to 3, "class" to "popup-overlay", "onClick" to _ctx.hideResponsibilityPicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择责任"),
                            _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideResponsibilityPicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "popup-search"), _uA(
                            _cE("input", _uM("class" to "search-input", "modelValue" to _ctx.responsibilitySearchKeyword, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.responsibilitySearchKeyword = `$event`.detail.value
                            }, "placeholder" to "搜索责任部门..."), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("scroll-view", _uM("class" to "popup-list", "scroll-y" to "true"), _uA(
                            if (_ctx.responsibilityOptions.length === 0) {
                                _cE("view", _uM("key" to 0, "class" to "popup-loading"), _uA(
                                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                                ))
                            } else {
                                if (_ctx.filteredResponsibilityOptions.length === 0) {
                                    _cE("view", _uM("key" to 1, "class" to "popup-empty"), _uA(
                                        _cE("text", _uM("class" to "empty-text"), "暂无匹配的责任部门")
                                    ))
                                } else {
                                    _cE(Fragment, _uM("key" to 2), RenderHelpers.renderList(_ctx.filteredResponsibilityOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to (item as OptionItem).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.selectedResponsibilityIndex >= 0 && _ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex] != null && _ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex].id === (item as OptionItem).id) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.selectResponsibilityFromFiltered(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem).name), 1),
                                            if (isTrue(_ctx.selectedResponsibilityIndex >= 0 && _ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex] != null && _ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex].id === (item as OptionItem).id)) {
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
            if (isTrue(_ctx.showIssuePopup)) {
                _cE("view", _uM("key" to 4, "class" to "popup-overlay", "onClick" to _ctx.hideIssuePicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择问题"),
                            _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideIssuePicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "popup-search"), _uA(
                            _cE("input", _uM("class" to "search-input", "modelValue" to _ctx.issueSearchKeyword, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.issueSearchKeyword = `$event`.detail.value
                            }, "placeholder" to "搜索问题..."), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("scroll-view", _uM("class" to "popup-list", "scroll-y" to "true"), _uA(
                            if (_ctx.issueOptions.length === 0) {
                                _cE("view", _uM("key" to 0, "class" to "popup-loading"), _uA(
                                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                                ))
                            } else {
                                if (_ctx.filteredIssueOptions.length === 0) {
                                    _cE("view", _uM("key" to 1, "class" to "popup-empty"), _uA(
                                        _cE("text", _uM("class" to "empty-text"), "暂无匹配的问题")
                                    ))
                                } else {
                                    _cE(Fragment, _uM("key" to 2), RenderHelpers.renderList(_ctx.filteredIssueOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to (item as OptionItem).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.selectedIssueIndex !== -1 && _ctx.issueOptions[_ctx.selectedIssueIndex] != null && _ctx.issueOptions[_ctx.selectedIssueIndex].id === (item as OptionItem).id) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.selectIssueFromFiltered(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem).name), 1),
                                            if (isTrue(_ctx.selectedIssueIndex !== -1 && _ctx.issueOptions[_ctx.selectedIssueIndex] != null && _ctx.issueOptions[_ctx.selectedIssueIndex].id === (item as OptionItem).id)) {
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
            if (isTrue(_ctx.showQualityPopup)) {
                _cE("view", _uM("key" to 5, "class" to "popup-overlay", "onClick" to _ctx.hideQualityPicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择性质界定"),
                            _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideQualityPicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "popup-search"), _uA(
                            _cE("input", _uM("class" to "search-input", "modelValue" to _ctx.qualitySearchKeyword, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.qualitySearchKeyword = `$event`.detail.value
                            }, "placeholder" to "搜索性质界定..."), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("scroll-view", _uM("class" to "popup-list", "scroll-y" to "true"), _uA(
                            if (_ctx.qualityOptions.length === 0) {
                                _cE("view", _uM("key" to 0, "class" to "popup-loading"), _uA(
                                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                                ))
                            } else {
                                if (_ctx.filteredQualityOptions.length === 0) {
                                    _cE("view", _uM("key" to 1, "class" to "popup-empty"), _uA(
                                        _cE("text", _uM("class" to "empty-text"), "暂无匹配的性质界定")
                                    ))
                                } else {
                                    _cE(Fragment, _uM("key" to 2), RenderHelpers.renderList(_ctx.filteredQualityOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to (item as OptionItem).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.selectedQualityIndex !== -1 && _ctx.qualityOptions[_ctx.selectedQualityIndex] != null && _ctx.qualityOptions[_ctx.selectedQualityIndex].id === (item as OptionItem).id) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.selectQualityFromFiltered(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem).name), 1),
                                            if (isTrue(_ctx.selectedQualityIndex !== -1 && _ctx.qualityOptions[_ctx.selectedQualityIndex] != null && _ctx.qualityOptions[_ctx.selectedQualityIndex].id === (item as OptionItem).id)) {
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
        ), 12, _uA(
            "scroll-top"
        ))
    }
    open var taskInfo: TaskInfo by `$data`
    open var formData: FormData by `$data`
    open var containerHeight: Number by `$data`
    open var scrollTop: Number by `$data`
    open var showProblemTypeModal: Boolean by `$data`
    open var selectedProblemType: String by `$data`
    open var problemTypes: UTSArray<String> by `$data`
    open var showTypePopup: Boolean by `$data`
    open var showModulePopup: Boolean by `$data`
    open var showResponsibilityPopup: Boolean by `$data`
    open var showIssuePopup: Boolean by `$data`
    open var showQualityPopup: Boolean by `$data`
    open var typeOptions: UTSArray<OptionItem> by `$data`
    open var moduleOptions: UTSArray<OptionItem> by `$data`
    open var responsibilityOptions: UTSArray<OptionItem> by `$data`
    open var issueOptions: UTSArray<OptionItem> by `$data`
    open var qualityOptions: UTSArray<OptionItem> by `$data`
    open var selectedTypeIndex: Number by `$data`
    open var selectedModuleIndex: Number by `$data`
    open var selectedResponsibilityIndex: Number by `$data`
    open var selectedIssueIndex: Number by `$data`
    open var selectedQualityIndex: Number by `$data`
    open var typeSearchKeyword: String by `$data`
    open var moduleSearchKeyword: String by `$data`
    open var responsibilitySearchKeyword: String by `$data`
    open var issueSearchKeyword: String by `$data`
    open var qualitySearchKeyword: String by `$data`
    open var datePickerVisible: Boolean by `$data`
    open var pickerValue: UTSArray<Number> by `$data`
    open var years: UTSArray<Number> by `$data`
    open var months: UTSArray<Number> by `$data`
    open var days: UTSArray<Number> by `$data`
    open var selectedYear: Number by `$data`
    open var selectedMonth: Number by `$data`
    open var selectedDay: Number by `$data`
    open var pickerDebounceTimer: Number? by `$data`
    open var filteredTypeOptions: UTSArray<OptionItem> by `$data`
    open var filteredModuleOptions: UTSArray<OptionItem> by `$data`
    open var filteredResponsibilityOptions: UTSArray<OptionItem> by `$data`
    open var filteredIssueOptions: UTSArray<OptionItem> by `$data`
    open var filteredQualityOptions: UTSArray<OptionItem> by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        val defaultDate = Date()
        defaultDate.setDate(defaultDate.getDate() + 7)
        val defaultDateStr = "" + defaultDate.getFullYear() + "-" + (defaultDate.getMonth() + 1).toString(10).padStart(2, "0") + "-" + defaultDate.getDate().toString(10).padStart(2, "0")
        return _uM("taskInfo" to TaskInfo(taskId = "", taskNumber = "", inspectionDate = "", department = "", profession = "", groupLeader = "", deputyLeader = ""), "formData" to FormData(problemType = "", problemDescription = "", location = "", photos = _uA(), safetyScore = "", rectificationDate = defaultDateStr, module = "", responsibility = "", issue = "", quality = "", inspector = "", responsibleUnit = "", responsiblePerson = ""), "containerHeight" to 0, "scrollTop" to 0, "showProblemTypeModal" to false, "selectedProblemType" to "", "problemTypes" to _uA(
            "安全隐患",
            "设备故障",
            "环境问题",
            "操作违规",
            "制度缺失",
            "其他问题"
        ), "showTypePopup" to false, "showModulePopup" to false, "showResponsibilityPopup" to false, "showIssuePopup" to false, "showQualityPopup" to false, "typeOptions" to _uA<OptionItem>(), "moduleOptions" to _uA<OptionItem>(), "responsibilityOptions" to _uA<OptionItem>(), "issueOptions" to _uA<OptionItem>(), "qualityOptions" to _uA<OptionItem>(), "selectedTypeIndex" to -1, "selectedModuleIndex" to -1, "selectedResponsibilityIndex" to -1, "selectedIssueIndex" to -1, "selectedQualityIndex" to -1, "typeSearchKeyword" to "", "moduleSearchKeyword" to "", "responsibilitySearchKeyword" to "", "issueSearchKeyword" to "", "qualitySearchKeyword" to "", "datePickerVisible" to false, "pickerValue" to _uA(
            0,
            0,
            0
        ), "years" to _uA<Number>(), "months" to _uA<Number>(), "days" to _uA<Number>(), "selectedYear" to (fun(): Number {
            val tempDate = Date()
            tempDate.setDate(tempDate.getDate() + 7)
            return tempDate.getFullYear()
        }
        )() as Number, "selectedMonth" to (fun(): Number {
            val tempDate = Date()
            tempDate.setDate(tempDate.getDate() + 7)
            return tempDate.getMonth() + 1
        }
        )() as Number, "selectedDay" to (fun(): Number {
            val tempDate = Date()
            tempDate.setDate(tempDate.getDate() + 7)
            return tempDate.getDate()
        }
        )() as Number, "pickerDebounceTimer" to null as Number?, "filteredTypeOptions" to computed<UTSArray<OptionItem>>(fun(): UTSArray<OptionItem> {
            if (this.typeSearchKeyword == "") {
                return this.typeOptions
            }
            return this.typeOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.typeSearchKeyword.toLowerCase())
            }
            )
        }
        ), "filteredModuleOptions" to computed<UTSArray<OptionItem>>(fun(): UTSArray<OptionItem> {
            if (this.moduleSearchKeyword == "") {
                return this.moduleOptions
            }
            return this.moduleOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.moduleSearchKeyword.toLowerCase())
            }
            )
        }
        ), "filteredResponsibilityOptions" to computed<UTSArray<OptionItem>>(fun(): UTSArray<OptionItem> {
            if (this.responsibilitySearchKeyword == "") {
                return this.responsibilityOptions
            }
            return this.responsibilityOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.responsibilitySearchKeyword.toLowerCase())
            }
            )
        }
        ), "filteredIssueOptions" to computed<UTSArray<OptionItem>>(fun(): UTSArray<OptionItem> {
            if (this.issueSearchKeyword == "") {
                return this.issueOptions
            }
            return this.issueOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.issueSearchKeyword.toLowerCase())
            }
            )
        }
        ), "filteredQualityOptions" to computed<UTSArray<OptionItem>>(fun(): UTSArray<OptionItem> {
            if (this.qualitySearchKeyword == "") {
                return this.qualityOptions
            }
            return this.qualityOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.qualitySearchKeyword.toLowerCase())
            }
            )
        }
        ))
    }
    open var getDefaultRectificationDate = ::gen_getDefaultRectificationDate_fn
    open fun gen_getDefaultRectificationDate_fn(): String {
        val now = Date()
        val year = now.getFullYear()
        val month = (now.getMonth() + 1).toString(10)
        val day = now.getDate().toString(10)
        val padZero = fun(num: String): String {
            return if (num.length < 2) {
                "0" + num
            } else {
                num
            }
        }
        return "" + year + "-" + padZero(month) + "-" + padZero(day)
    }
    open var convertToHttpUrl = ::gen_convertToHttpUrl_fn
    open fun gen_convertToHttpUrl_fn(relativePath: String): String {
        console.log("相对路径:", relativePath, " at pages/monthly/task-record.uvue:628")
        if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) {
            return relativePath
        }
        val httpUrl = "" + apiConfig.baseUrl + "/api" + relativePath
        console.log("转换后的HTTP URL:", httpUrl, " at pages/monthly/task-record.uvue:637")
        return httpUrl
    }
    open var initContainerHeight = ::gen_initContainerHeight_fn
    open fun gen_initContainerHeight_fn() {
        val systemInfo = uni_getSystemInfoSync()
        this.containerHeight = systemInfo.windowHeight
    }
    open var loadTaskInfo = ::gen_loadTaskInfo_fn
    open fun gen_loadTaskInfo_fn(taskId: String) {
        try {
            val token = uni_getStorageSync("token")
            console.log("接收到的taskId参数:", taskId, " at pages/monthly/task-record.uvue:650")
            uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-task/task-no/" + taskId), method = "GET", header = object : UTSJSONObject() {
                var Authorization = "Bearer " + token
            }, success = fun(res){
                val respdata = res.data as UTSJSONObject
                if (respdata.getNumber("code") === 200) {
                    val taskInfo = respdata.getJSON("data")
                    console.log("获取到的任务数据:", taskInfo, " at pages/monthly/task-record.uvue:662")
                    if (taskInfo != null) {
                        this.taskInfo = TaskInfo(taskId = taskInfo.getString("id") ?: "", taskNumber = taskInfo.getString("taskNo") ?: "", inspectionDate = taskInfo.getString("inspectionTime") ?: "", department = taskInfo.getString("departmentName") ?: "", profession = taskInfo.getString("professionId") ?: "", groupLeader = taskInfo.getString("groupLeaderName") ?: "", deputyLeader = taskInfo.getString("deputyLeaderName") ?: "")
                        this.autoFillProfession()
                    }
                }
            }
            , fail = fun(error){
                console.error("加载任务信息失败:", error, " at pages/monthly/task-record.uvue:679")
                this.taskInfo = TaskInfo(taskId = "", taskNumber = "YJ202401001", inspectionDate = "2024年1月", department = "生产部", profession = "违法违规", groupLeader = "张三", deputyLeader = "李四")
                this.autoFillProfession()
            }
            ))
        }
         catch (error: Throwable) {
            console.error("加载任务信息失败:", error, " at pages/monthly/task-record.uvue:695")
            this.taskInfo = TaskInfo(taskId = "", taskNumber = "YJ202401001", inspectionDate = "2024年1月", department = "生产部", profession = "违法违规", groupLeader = "张三", deputyLeader = "李四")
            this.autoFillProfession()
        }
    }
    open var goBack = ::gen_goBack_fn
    open fun gen_goBack_fn() {
        uni_navigateBack(null)
    }
    open var showProblemTypePicker = ::gen_showProblemTypePicker_fn
    open fun gen_showProblemTypePicker_fn() {
        this.selectedProblemType = this.formData.problemType
        this.showProblemTypeModal = true
    }
    open var hideProblemTypePicker = ::gen_hideProblemTypePicker_fn
    open fun gen_hideProblemTypePicker_fn() {
        this.showProblemTypeModal = false
    }
    open var selectProblemType = ::gen_selectProblemType_fn
    open fun gen_selectProblemType_fn(type: String) {
        this.selectedProblemType = type
    }
    open var autoFillProfession = ::gen_autoFillProfession_fn
    open fun gen_autoFillProfession_fn() {
        console.log("开始自动填充性质界定", " at pages/monthly/task-record.uvue:734")
        console.log("taskInfo:", this.taskInfo, " at pages/monthly/task-record.uvue:735")
        console.log("typeOptions:", this.typeOptions, " at pages/monthly/task-record.uvue:736")
        if (this.taskInfo != null && this.taskInfo.profession != "" && this.typeOptions.length > 0) {
            console.log("查找性质界定:", this.taskInfo.profession, " at pages/monthly/task-record.uvue:739")
            val index = this.typeOptions.findIndex(fun(item: OptionItem): Boolean {
                return item.name === this.taskInfo.profession
            })
            console.log("找到的性质界定索引:", index, " at pages/monthly/task-record.uvue:741")
            if (index !== -1) {
                this.selectedTypeIndex = index
                console.log("已设置性质界定索引:", this.selectedTypeIndex, " at pages/monthly/task-record.uvue:745")
                this.loadModuleOptions((this.typeOptions[index] as OptionItem).name)
            } else {
                console.warn("未找到匹配的性质界定:", this.taskInfo.profession, " at pages/monthly/task-record.uvue:749")
                val fuzzyIndex = this.typeOptions.findIndex(fun(item: OptionItem): Boolean {
                    return item.name.includes(this.taskInfo.profession) || this.taskInfo.profession.includes(item.name)
                })
                if (fuzzyIndex !== -1) {
                    this.selectedTypeIndex = fuzzyIndex
                    console.log("模糊匹配到性质界定索引:", this.selectedTypeIndex, " at pages/monthly/task-record.uvue:756")
                    this.loadModuleOptions((this.typeOptions[fuzzyIndex] as OptionItem).name)
                }
            }
        } else {
            if (this.taskInfo == null) {
                console.log("taskInfo为空，等待任务信息加载", " at pages/monthly/task-record.uvue:762")
            } else if (this.taskInfo.profession == "") {
                console.log("专业信息为空", " at pages/monthly/task-record.uvue:764")
            } else if (this.typeOptions.length === 0) {
                console.log("性质界定选项未加载，等待加载完成", " at pages/monthly/task-record.uvue:766")
            }
        }
        this.loadGjbzDataByProfession()
    }
    open var loadTypeOptions = ::gen_loadTypeOptions_fn
    open fun gen_loadTypeOptions_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/gjbz-data/options/types"), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val respdata = res.data as UTSJSONObject
            if (respdata.getNumber("code") === 200) {
                val data = respdata.getArray("data")
                console.log("获取到的专业类别数据", data, " at pages/monthly/task-record.uvue:787")
                if (data != null) {
                    val options: UTSArray<OptionItem> = _uA()
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val item = data[i] as String
                            options.push(OptionItem(id = (i + 1).toString(10), name = item))
                            i++
                        }
                    }
                    this.typeOptions = options
                    console.log("专业类别选项已加载:", this.typeOptions, " at pages/monthly/task-record.uvue:798")
                    this.autoFillProfession()
                }
            } else {
                console.error("加载专业类别失败:", respdata.getString("message"), " at pages/monthly/task-record.uvue:803")
                this.loadFallbackTypeOptions()
            }
        }
        , fail = fun(error){
            console.error("加载专业类别失败:", error, " at pages/monthly/task-record.uvue:808")
            this.loadFallbackTypeOptions()
        }
        ))
    }
    open var loadFallbackTypeOptions = ::gen_loadFallbackTypeOptions_fn
    open fun gen_loadFallbackTypeOptions_fn() {
        this.typeOptions = _uA(
            OptionItem(id = "1", name = "电气专业"),
            OptionItem(id = "2", name = "机械专业"),
            OptionItem(id = "3", name = "仪表专业"),
            OptionItem(id = "4", name = "安全专业"),
            OptionItem(id = "5", name = "环保专业")
        )
        console.log("使用备用专业类别数据:", this.typeOptions, " at pages/monthly/task-record.uvue:823")
        this.autoFillProfession()
    }
    open var loadGjbzDataByProfession = ::gen_loadGjbzDataByProfession_fn
    open fun gen_loadGjbzDataByProfession_fn() {
        if (this.taskInfo == null || this.taskInfo.profession === "") {
            return
        }
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/gjbz-data/by-profession/" + UTSAndroid.consoleDebugError(encodeURIComponent(this.taskInfo.profession), " at pages/monthly/task-record.uvue:838")), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val respdata = res.data as UTSJSONObject
            if (respdata.getNumber("code") === 200) {
                val data = respdata.getArray("data")
                if (data != null && data.length > 0) {
                    console.log("\u627E\u5230" + data.length + "\u4E2A\u4E0E\u4E13\u4E1A\"" + this.taskInfo.profession + "\"\u76F8\u5173\u7684\u5C97\u68C0\u6807\u51C6\u9879", " at pages/monthly/task-record.uvue:848")
                } else {
                    console.log("\u672A\u627E\u5230\u4E0E\u4E13\u4E1A\"" + this.taskInfo.profession + "\"\u76F8\u5173\u7684\u5C97\u68C0\u6807\u51C6\u9879", " at pages/monthly/task-record.uvue:852")
                }
            } else {
                console.error("加载岗检标准项失败:", respdata.getString("message"), " at pages/monthly/task-record.uvue:855")
            }
        }
        , fail = fun(error){
            console.error("加载岗检标准项失败:", error, " at pages/monthly/task-record.uvue:859")
        }
        ))
    }
    open var confirmProblemType = ::gen_confirmProblemType_fn
    open fun gen_confirmProblemType_fn() {
        this.formData.problemType = this.selectedProblemType
        this.hideProblemTypePicker()
    }
    open var showTypePicker = ::gen_showTypePicker_fn
    open fun gen_showTypePicker_fn() {
        this.showTypePopup = true
    }
    open var hideTypePicker = ::gen_hideTypePicker_fn
    open fun gen_hideTypePicker_fn() {
        this.showTypePopup = false
        this.typeSearchKeyword = ""
    }
    open var selectType = ::gen_selectType_fn
    open fun gen_selectType_fn(index: Number) {
        this.selectedTypeIndex = index
        this.hideTypePicker()
        this.selectedModuleIndex = -1
        this.selectedResponsibilityIndex = -1
        this.selectedIssueIndex = -1
        this.moduleOptions = _uA()
        this.responsibilityOptions = _uA()
        this.issueOptions = _uA()
        this.loadModuleOptions((this.typeOptions[index] as OptionItem).name)
    }
    open var selectTypeFromFiltered = ::gen_selectTypeFromFiltered_fn
    open fun gen_selectTypeFromFiltered_fn(item: OptionItem) {
        val index = this.typeOptions.findIndex(fun(option): Boolean {
            return option.id === item.id
        }
        )
        if (index !== -1) {
            this.selectType(index)
        }
    }
    open var showModulePicker = ::gen_showModulePicker_fn
    open fun gen_showModulePicker_fn() {
        if (this.selectedTypeIndex === -1) {
            uni_showToast(ShowToastOptions(title = "请先选择专业类别", icon = "none"))
            return
        }
        this.showModulePopup = true
    }
    open var hideModulePicker = ::gen_hideModulePicker_fn
    open fun gen_hideModulePicker_fn() {
        this.showModulePopup = false
        this.moduleSearchKeyword = ""
    }
    open var selectModule = ::gen_selectModule_fn
    open fun gen_selectModule_fn(index: Number) {
        this.selectedModuleIndex = index
        this.formData.module = (this.moduleOptions[index] as OptionItem).name
        this.hideModulePicker()
        this.selectedResponsibilityIndex = -1
        this.selectedIssueIndex = -1
        this.formData.responsibility = ""
        this.formData.issue = ""
        this.responsibilityOptions = _uA()
        this.issueOptions = _uA()
        this.loadResponsibilityOptions((this.moduleOptions[index] as OptionItem).id)
        this.loadResponsibilityOptions((this.moduleOptions[index] as OptionItem).name)
    }
    open var selectModuleFromFiltered = ::gen_selectModuleFromFiltered_fn
    open fun gen_selectModuleFromFiltered_fn(item: OptionItem) {
        val index = this.moduleOptions.findIndex(fun(option): Boolean {
            return option.id === item.id
        }
        )
        if (index !== -1) {
            this.selectModule(index)
        }
    }
    open var showResponsibilityPicker = ::gen_showResponsibilityPicker_fn
    open fun gen_showResponsibilityPicker_fn() {
        if (this.selectedModuleIndex === -1) {
            uni_showToast(ShowToastOptions(title = "请先选择模块", icon = "none"))
            return
        }
        this.showResponsibilityPopup = true
    }
    open var hideResponsibilityPicker = ::gen_hideResponsibilityPicker_fn
    open fun gen_hideResponsibilityPicker_fn() {
        this.showResponsibilityPopup = false
        this.responsibilitySearchKeyword = ""
    }
    open var selectResponsibility = ::gen_selectResponsibility_fn
    open fun gen_selectResponsibility_fn(index: Number) {
        this.selectedResponsibilityIndex = index
        this.formData.responsibility = (this.responsibilityOptions[index] as OptionItem).name
        this.hideResponsibilityPicker()
        this.selectedIssueIndex = -1
        this.formData.issue = ""
        this.issueOptions = _uA()
        this.loadIssueOptions((this.responsibilityOptions[index] as OptionItem).id)
        this.loadIssueOptions((this.responsibilityOptions[index] as OptionItem).name)
    }
    open var selectResponsibilityFromFiltered = ::gen_selectResponsibilityFromFiltered_fn
    open fun gen_selectResponsibilityFromFiltered_fn(item: OptionItem) {
        val index = this.responsibilityOptions.findIndex(fun(option): Boolean {
            return option.id === item.id
        }
        )
        if (index !== -1) {
            this.selectResponsibility(index)
        }
    }
    open var showIssuePicker = ::gen_showIssuePicker_fn
    open fun gen_showIssuePicker_fn() {
        if (this.selectedResponsibilityIndex === -1) {
            uni_showToast(ShowToastOptions(title = "请先选择责任", icon = "none"))
            return
        }
        this.showIssuePopup = true
    }
    open var hideIssuePicker = ::gen_hideIssuePicker_fn
    open fun gen_hideIssuePicker_fn() {
        this.showIssuePopup = false
        this.issueSearchKeyword = ""
    }
    open var selectIssue = ::gen_selectIssue_fn
    open fun gen_selectIssue_fn(index: Number) {
        this.selectedIssueIndex = index
        this.formData.issue = (this.issueOptions[index] as OptionItem).name
        this.hideIssuePicker()
        this.autoFillIssueDetails((this.issueOptions[index] as OptionItem).id, (this.issueOptions[index] as OptionItem).name)
    }
    open var selectIssueFromFiltered = ::gen_selectIssueFromFiltered_fn
    open fun gen_selectIssueFromFiltered_fn(item: OptionItem) {
        val index = this.issueOptions.findIndex(fun(option): Boolean {
            return option.id === item.id
        }
        )
        if (index !== -1) {
            this.selectIssue(index)
        }
    }
    open var showQualityPicker = ::gen_showQualityPicker_fn
    open fun gen_showQualityPicker_fn() {
        this.loadQualityOptions()
        this.showQualityPopup = true
    }
    open var hideQualityPicker = ::gen_hideQualityPicker_fn
    open fun gen_hideQualityPicker_fn() {
        this.showQualityPopup = false
        this.qualitySearchKeyword = ""
    }
    open var selectQuality = ::gen_selectQuality_fn
    open fun gen_selectQuality_fn(index: Number) {
        this.selectedQualityIndex = index
        this.formData.quality = (this.qualityOptions[index] as OptionItem).name
        this.hideQualityPicker()
        this.formData.safetyScore = this.getScoreByQualityName((this.qualityOptions[index] as OptionItem).name)
    }
    open var selectQualityFromFiltered = ::gen_selectQualityFromFiltered_fn
    open fun gen_selectQualityFromFiltered_fn(item: OptionItem) {
        val index = this.qualityOptions.findIndex(fun(option): Boolean {
            return option.id === item.id
        }
        )
        if (index !== -1) {
            this.selectQuality(index)
        }
    }
    open var loadModuleOptions = ::gen_loadModuleOptions_fn
    open fun gen_loadModuleOptions_fn(typeName: String) {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/gjbz-data/modules/by-type/" + UTSAndroid.consoleDebugError(encodeURIComponent(typeName), " at pages/monthly/task-record.uvue:1028")), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val respdata = res.data as UTSJSONObject
            if (respdata.getNumber("code") === 200) {
                val modules = respdata.getArray("data") as UTSArray<String>
                val unique = _uA<String>()
                if (modules != null) {
                    run {
                        var i: Number = 0
                        while(i < modules.length){
                            val m = modules[i]
                            if (m != null && m != "" && unique.indexOf(m) === -1) {
                                unique.push(m)
                            }
                            i++
                        }
                    }
                }
                this.moduleOptions = unique.map(fun(m, idx): OptionItem {
                    return (OptionItem(id = (idx + 1).toString(10), name = m))
                }
                )
            }
        }
        , fail = fun(error){
            console.error("按专业加载模块失败:", error, " at pages/monthly/task-record.uvue:1048")
            this.moduleOptions = _uA<OptionItem>()
        }
        ))
    }
    open var loadResponsibilityOptions = ::gen_loadResponsibilityOptions_fn
    open fun gen_loadResponsibilityOptions_fn(moduleId: String) {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/gjbz-data/options/responsibilities/by-module/" + UTSAndroid.consoleDebugError(encodeURIComponent(moduleId), " at pages/monthly/task-record.uvue:1058")), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val respdata = res.data as UTSJSONObject
            if (respdata.getNumber("code") === 200) {
                val data = respdata.getArray("data")
                if (data != null) {
                    val options: UTSArray<OptionItem> = _uA()
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val item = data[i] as String
                            options.push(OptionItem(id = (i + 1).toString(10), name = item))
                            i++
                        }
                    }
                    this.responsibilityOptions = options
                }
            }
        }
        , fail = fun(error){
            console.error("加载责任选项失败:", error, " at pages/monthly/task-record.uvue:1081")
            this.responsibilityOptions = _uA<OptionItem>(OptionItem(id = "resp1", name = "责任1"), OptionItem(id = "resp2", name = "责任2"), OptionItem(id = "resp3", name = "责任3"))
        }
        ))
    }
    open var loadIssueOptions = ::gen_loadIssueOptions_fn
    open fun gen_loadIssueOptions_fn(responsibilityId: String) {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/gjbz-data/options/issues/by-responsibility/" + UTSAndroid.consoleDebugError(encodeURIComponent(responsibilityId), " at pages/monthly/task-record.uvue:1095")), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val respdata = res.data as UTSJSONObject
            if (respdata.getNumber("code") === 200) {
                val data = respdata.getArray("data")
                if (data != null) {
                    val options: UTSArray<OptionItem> = _uA()
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val item = data[i] as String
                            options.push(OptionItem(id = (i + 1).toString(10), name = item))
                            i++
                        }
                    }
                    this.issueOptions = options
                }
            }
        }
        , fail = fun(error){
            console.error("加载问题选项失败:", error, " at pages/monthly/task-record.uvue:1118")
            this.issueOptions = _uA<OptionItem>(OptionItem(id = "issue1", name = "问题1"), OptionItem(id = "issue2", name = "问题2"), OptionItem(id = "issue3", name = "问题3"))
        }
        ))
    }
    open var loadQualityOptions = ::gen_loadQualityOptions_fn
    open fun gen_loadQualityOptions_fn() {
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
                    this.qualityOptions = _uA()
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
                            this.qualityOptions.push(OptionItem(id = id, name = name))
                            i++
                        }
                    }
                }
                console.log("性质界定选项加载完成:", this.qualityOptions, " at pages/monthly/task-record.uvue:1155")
            } else {
                val message = data.getString("message") ?: "未知错误"
                console.error("获取性质界定数据失败:", message, " at pages/monthly/task-record.uvue:1158")
                this.qualityOptions = _uA<OptionItem>(OptionItem(id = "1", name = "一般问题"), OptionItem(id = "2", name = "较大问题"), OptionItem(id = "3", name = "严重问题"), OptionItem(id = "4", name = "重大问题"))
            }
        }
        , fail = fun(error){
            console.error("请求性质界定数据失败:", error, " at pages/monthly/task-record.uvue:1169")
            this.qualityOptions = _uA<OptionItem>(OptionItem(id = "1", name = "一般问题"), OptionItem(id = "2", name = "较大问题"), OptionItem(id = "3", name = "严重问题"), OptionItem(id = "4", name = "重大问题"))
        }
        ))
    }
    open var loadQualityDetails = ::gen_loadQualityDetails_fn
    open fun gen_loadQualityDetails_fn(qualityId: String) {
        val opt = this.qualityOptions.find(fun(o: OptionItem): Boolean {
            return o.id === qualityId
        }
        )
        if (opt != null) {
            this.formData.safetyScore = this.getScoreByQualityName(opt.name)
        }
    }
    open var autoFillIssueDetails = ::gen_autoFillIssueDetails_fn
    open fun gen_autoFillIssueDetails_fn(issueId: String, issueName: String) {
        console.log("开始自动填充问题详情:", issueId, issueName, " at pages/monthly/task-record.uvue:1191")
        if (this.qualityOptions.length === 0) {
            this.loadQualityOptionsAndFill(issueId, issueName)
            return
        }
        this.performAutoFill(issueId, issueName)
    }
    open var loadQualityOptionsAndFill = ::gen_loadQualityOptionsAndFill_fn
    open fun gen_loadQualityOptionsAndFill_fn(issueId: String, issueName: String) {
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
                    this.qualityOptions = _uA()
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
                            this.qualityOptions.push(OptionItem(id = id, name = name))
                            i++
                        }
                    }
                    this.performAutoFill(issueId, issueName)
                }
            } else {
                val message = data.getString("message") ?: "未知错误"
                console.error("获取性质界定数据失败:", message, " at pages/monthly/task-record.uvue:1233")
                this.qualityOptions = _uA<OptionItem>(OptionItem(id = "1", name = "一般问题"), OptionItem(id = "2", name = "较大问题"), OptionItem(id = "3", name = "严重问题"), OptionItem(id = "4", name = "重大问题"))
                this.performAutoFill(issueId, issueName)
            }
        }
        , fail = fun(error){
            console.error("请求性质界定数据失败:", error, " at pages/monthly/task-record.uvue:1246")
            this.qualityOptions = _uA<OptionItem>(OptionItem(id = "1", name = "一般问题"), OptionItem(id = "2", name = "较大问题"), OptionItem(id = "3", name = "严重问题"), OptionItem(id = "4", name = "重大问题"))
            this.performAutoFill(issueId, issueName)
        }
        ))
    }
    open var performAutoFill = ::gen_performAutoFill_fn
    open fun gen_performAutoFill_fn(issueId: String, issueName: String) {
        val issueMapping = this.getIssueQualityMapping(issueName) as UTSJSONObject?
        if (issueMapping != null) {
            val qualityName = issueMapping.getString("quality") ?: ""
            val safetyScoreText = issueMapping.getString("safetyScore") ?: ""
            var qualityIndex: Number = -1
            var matchedQualityName = ""
            qualityIndex = this.qualityOptions.findIndex(fun(item: OptionItem): Boolean {
                return item.name === qualityName
            })
            if (qualityIndex === -1) {
                if (qualityName.includes("严重") || qualityName.includes("重大")) {
                    qualityIndex = this.qualityOptions.findIndex(fun(item: OptionItem): Boolean {
                        return item.name.includes("严重") || item.name.includes("重大") || item.name.includes("重要")
                    })
                } else if (qualityName.includes("一般") || qualityName.includes("轻微")) {
                    qualityIndex = this.qualityOptions.findIndex(fun(item: OptionItem): Boolean {
                        return item.name.includes("一般") || item.name.includes("轻微") || item.name.includes("普通")
                    })
                } else if (qualityName.includes("中等") || qualityName.includes("较大")) {
                    qualityIndex = this.qualityOptions.findIndex(fun(item: OptionItem): Boolean {
                        return item.name.includes("中等") || item.name.includes("较大") || item.name.includes("中")
                    }
                    )
                }
            }
            if (qualityIndex !== -1) {
                this.selectedQualityIndex = qualityIndex
                matchedQualityName = (this.qualityOptions[qualityIndex] as OptionItem).name
                this.formData.quality = matchedQualityName
            } else {
                if (this.qualityOptions.length > 0) {
                    this.selectedQualityIndex = 0
                    matchedQualityName = (this.qualityOptions[0] as OptionItem).name
                    this.formData.quality = matchedQualityName
                }
            }
            this.formData.safetyScore = this.getScoreByQualityName(matchedQualityName)
            console.log("自动填充完成:", _uO("originalQuality" to qualityName, "matchedQuality" to matchedQualityName, "safetyScore" to this.formData.safetyScore, "selectedQualityIndex" to this.selectedQualityIndex), " at pages/monthly/task-record.uvue:1307")
            uni_showToast(ShowToastOptions(title = "\u5DF2\u81EA\u52A8\u586B\u5145\uFF1A" + matchedQualityName + "\uFF0C" + this.formData.safetyScore + "\u5206", icon = "success", duration = 2000))
        } else {
            console.log("未找到匹配的问题映射规则", " at pages/monthly/task-record.uvue:1321")
        }
    }
    open var getIssueQualityMapping = ::gen_getIssueQualityMapping_fn
    open fun gen_getIssueQualityMapping_fn(issueName: String): UTSJSONObject {
        val mappingRules = _uA<UTSJSONObject>(object : UTSJSONObject() {
            var keywords = _uA(
                "违章",
                "违规",
                "严重",
                "重大",
                "危险",
                "安全隐患"
            )
            var quality = "严重问题"
            var safetyScore = "15分 - 严重安全记分"
        }, object : UTSJSONObject() {
            var keywords = _uA(
                "火灾",
                "爆炸",
                "中毒",
                "触电",
                "高空坠落"
            )
            var quality = "重大问题"
            var safetyScore = "25分 - 重大安全记分"
        }, object : UTSJSONObject() {
            var keywords = _uA(
                "轻微",
                "一般",
                "小问题",
                "不规范",
                "不完善"
            )
            var quality = "一般问题"
            var safetyScore = "5分 - 一般安全记分"
        }, object : UTSJSONObject() {
            var keywords = _uA(
                "标识",
                "标牌",
                "记录",
                "台账",
                "文档"
            )
            var quality = "一般问题"
            var safetyScore = "3分 - 一般安全记分"
        }, object : UTSJSONObject() {
            var keywords = _uA(
                "设备",
                "工具",
                "防护",
                "操作",
                "流程"
            )
            var quality = "中等问题"
            var safetyScore = "10分 - 中等安全记分"
        }, object : UTSJSONObject() {
            var keywords = _uA(
                "环境",
                "卫生",
                "整理",
                "整顿"
            )
            var quality = "中等问题"
            var safetyScore = "8分 - 中等安全记分"
        })
        run {
            var i: Number = 0
            while(i < mappingRules.length){
                val rule = mappingRules[i] as UTSJSONObject
                val keywords = rule.getArray("keywords") as UTSArray<String>
                run {
                    var j: Number = 0
                    while(j < keywords.length){
                        val keyword = keywords[j]
                        if (issueName.includes(keyword)) {
                            return object : UTSJSONObject() {
                                var quality = rule["quality"]
                                var safetyScore = rule["safetyScore"]
                            }
                        }
                        j++
                    }
                }
                i++
            }
        }
        return object : UTSJSONObject() {
            var quality = "一般问题"
            var safetyScore = "5分 - 一般安全记分"
        }
    }
    open var getScoreByQualityName = ::gen_getScoreByQualityName_fn
    open fun gen_getScoreByQualityName_fn(qualityName: String): String {
        val s: String = if ((qualityName == null)) {
            ""
        } else {
            qualityName
        }
        if (s.indexOf("重要") >= 0) {
            return "5"
        }
        if (s.indexOf("一般") >= 0) {
            return "3"
        }
        if (s.indexOf("观察") >= 0) {
            return "1"
        }
        return "0"
    }
    open var addPhoto = ::gen_addPhoto_fn
    open fun gen_addPhoto_fn() {
        uni_chooseImage(ChooseImageOptions(count = 3 - this.formData.photos.length, sizeType = _uA(
            "compressed"
        ), sourceType = _uA(
            "camera",
            "album"
        ), success = fun(res){
            res.tempFilePaths.forEach(fun(tempPath){
                this.uploadImage(tempPath)
            }
            )
        }
        ))
    }
    open var uploadImage = ::gen_uploadImage_fn
    open fun gen_uploadImage_fn(imagePath: String) {
        console.log("=== 移动端图片上传调试信息 ===", " at pages/monthly/task-record.uvue:1388")
        console.log("选择的图片路径:", imagePath, " at pages/monthly/task-record.uvue:1389")
        uni_showLoading(ShowLoadingOptions(title = "上传中..."))
        uni_uploadFile(UploadFileOptions(url = getApiUrl("/upload/image"), filePath = imagePath, name = "file", header = object : UTSJSONObject() {
            var Authorization = uni_getStorageSync("token")
        }, success = fun(uploadRes){
            uni_hideLoading()
            console.log("上传响应状态码:", uploadRes.statusCode, " at pages/monthly/task-record.uvue:1404")
            console.log("上传响应数据:", uploadRes.data, " at pages/monthly/task-record.uvue:1405")
            try {
                val result = UTSAndroid.consoleDebugError(JSON.parse(uploadRes.data), " at pages/monthly/task-record.uvue:1408") as UTSJSONObject
                console.log("解析后的响应数据:", result, " at pages/monthly/task-record.uvue:1409")
                val code = result.getNumber("code")
                if (code === 200) {
                    val data = result.getJSON("data")
                    if (data != null) {
                        val url = data.getString("url")
                        console.log("服务器返回的图片URL:", url, " at pages/monthly/task-record.uvue:1417")
                        if (url != null) {
                            this.formData.photos.push(url)
                            console.log("当前photos数组:", this.formData.photos, " at pages/monthly/task-record.uvue:1420")
                        }
                    }
                } else {
                    var message = result.getString("message")
                    if (message == null) {
                        message = "上传失败"
                    }
                    console.error("上传失败，服务器响应:", result, " at pages/monthly/task-record.uvue:1428")
                    uni_showToast(ShowToastOptions(title = message, icon = "none"))
                }
            }
             catch (e: Throwable) {
                console.error("解析响应数据失败:", e, " at pages/monthly/task-record.uvue:1435")
                console.error("原始响应数据:", uploadRes.data, " at pages/monthly/task-record.uvue:1436")
                uni_showToast(ShowToastOptions(title = "上传失败", icon = "none"))
            }
            console.log("=== 移动端图片上传调试信息结束 ===", " at pages/monthly/task-record.uvue:1442")
        }
        , fail = fun(error){
            uni_hideLoading()
            console.error("图片上传失败:", error, " at pages/monthly/task-record.uvue:1446")
            uni_showToast(ShowToastOptions(title = "上传失败", icon = "none"))
        }
        ))
    }
    open var deletePhoto = ::gen_deletePhoto_fn
    open fun gen_deletePhoto_fn(index: Number) {
        this.formData.photos.splice(index, 1)
    }
    open var previewPhoto = ::gen_previewPhoto_fn
    open fun gen_previewPhoto_fn(photo: String) {
        uni_previewImage(PreviewImageOptions(current = this.convertToHttpUrl(photo), urls = this.formData.photos.map(fun(p: String): String {
            return this.convertToHttpUrl(p)
        }
        ) as UTSArray<String>))
    }
    open var saveRecord = ::gen_saveRecord_fn
    open fun gen_saveRecord_fn() {
        if (this.selectedTypeIndex === -1) {
            uni_showToast(ShowToastOptions(title = "请选择专业类别", icon = "none"))
            return
        }
        if (this.selectedModuleIndex === -1) {
            uni_showToast(ShowToastOptions(title = "请选择模块", icon = "none"))
            return
        }
        if (this.selectedResponsibilityIndex === -1) {
            uni_showToast(ShowToastOptions(title = "请选择责任", icon = "none"))
            return
        }
        if (this.selectedIssueIndex === -1) {
            uni_showToast(ShowToastOptions(title = "请选择问题", icon = "none"))
            return
        }
        if (this.selectedQualityIndex === -1) {
            uni_showToast(ShowToastOptions(title = "请选择性质界定", icon = "none"))
            return
        }
        if (this.formData.problemDescription.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请输入问题描述", icon = "none"))
            return
        }
        if (this.formData.location.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请输入发现位置", icon = "none"))
            return
        }
        if (this.formData.rectificationDate == "") {
            uni_showToast(ShowToastOptions(title = "请选择整改期限", icon = "none"))
            return
        }
        if (this.formData.responsibleUnit.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请输入责任单位", icon = "none"))
            return
        }
        if (this.formData.responsiblePerson.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请输入责任人", icon = "none"))
            return
        }
        if (this.formData.inspector.trim() == "") {
            uni_showToast(ShowToastOptions(title = "请输入检查人", icon = "none"))
            return
        }
        try {
            uni_showLoading(ShowLoadingOptions(title = "保存中..."))
            val userInfoStr = uni_getStorageSync("userInfo") as String
            var userId = ""
            if (userInfoStr != null && userInfoStr != "") {
                try {
                    val userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/monthly/task-record.uvue:1534") as UTSJSONObject
                    userId = userInfo.getString("id") ?: ""
                }
                 catch (e: Throwable) {
                    console.error("解析用户信息失败:", e, " at pages/monthly/task-record.uvue:1537")
                }
            }
            if (userId == "") {
                uni_hideLoading()
                uni_showToast(ShowToastOptions(title = "获取用户信息失败，请重新登录", icon = "none"))
                return
            }
            val saveData: UTSJSONObject = _uO("__\$originalPosition" to UTSSourceMapPosition("saveData", "pages/monthly/task-record.uvue", 1548, 15), "taskId" to this.taskInfo.taskId, "professionalCategory" to if (this.selectedTypeIndex !== -1 && this.typeOptions[this.selectedTypeIndex] != null) {
                this.typeOptions[this.selectedTypeIndex].name
            } else {
                ""
            }
            , "module" to if (this.selectedModuleIndex !== -1 && this.moduleOptions[this.selectedModuleIndex] != null) {
                this.moduleOptions[this.selectedModuleIndex].name
            } else {
                ""
            }
            , "responsibility" to if (this.selectedResponsibilityIndex !== -1 && this.responsibilityOptions[this.selectedResponsibilityIndex] != null) {
                this.responsibilityOptions[this.selectedResponsibilityIndex].name
            } else {
                ""
            }
            , "issue" to if (this.selectedIssueIndex !== -1 && this.issueOptions[this.selectedIssueIndex] != null) {
                this.issueOptions[this.selectedIssueIndex].name
            } else {
                ""
            }
            , "qualityDetermination" to if (this.selectedQualityIndex !== -1 && this.qualityOptions[this.selectedQualityIndex] != null) {
                this.qualityOptions[this.selectedQualityIndex].name
            } else {
                ""
            }
            , "problemDescription" to this.formData.problemDescription, "location" to this.formData.location, "rectificationDate" to this.formData.rectificationDate, "inspector" to this.formData.inspector, "safetyScore" to this.formData.safetyScore, "photos" to this.formData.photos, "responsibleUnit" to this.formData.responsibleUnit, "responsiblePerson" to this.formData.responsiblePerson)
            uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record"), method = "POST", header = object : UTSJSONObject() {
                var `Content-Type` = "application/json"
                var Authorization = if (uni_getStorageSync("token") != null) {
                    uni_getStorageSync("token") as String
                } else {
                    ""
                }
                var userId = userId
            }, data = UTSJSONObject.assign(UTSJSONObject(), saveData, _uO("photos" to this.formData.photos.join(","))), success = fun(res){
                uni_hideLoading()
                val respdate = res.data as UTSJSONObject
                if (respdate.getNumber("code") == 200) {
                    uni_showToast(ShowToastOptions(title = "保存成功", icon = "success"))
                    setTimeout(fun(){
                        uni_navigateBack(null)
                    }, 1500)
                } else {
                    val msg = respdate.getString("message")
                    uni_showToast(ShowToastOptions(title = if (msg != null) {
                        msg
                    } else {
                        "保存失败"
                    }
                    , icon = "none"))
                }
            }
            , fail = fun(error){
                uni_hideLoading()
                console.error("保存失败:", error, " at pages/monthly/task-record.uvue:1593")
                uni_showToast(ShowToastOptions(title = "保存失败", icon = "none"))
            }
            ))
        }
         catch (error: Throwable) {
            uni_hideLoading()
            console.error("保存失败:", error, " at pages/monthly/task-record.uvue:1599")
            uni_showToast(ShowToastOptions(title = "保存失败", icon = "none"))
        }
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
        console.log("初始化日期数组:", _uO("selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "selectedDay" to this.selectedDay, "yearIndex" to yearIndex, "monthIndex" to monthIndex, "dayIndex" to dayIndex, "pickerValue" to this.pickerValue, "daysArray" to this.days), " at pages/monthly/task-record.uvue:1628")
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
            console.log("日期不存在，调整为月末:", this.selectedDay, " at pages/monthly/task-record.uvue:1671")
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
        console.log("显示日期选择器:", _uO("selectedDate" to this.formData.rectificationDate, "selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "selectedDay" to this.selectedDay, "yearIndex" to yearIndex, "monthIndex" to monthIndex, "dayIndex" to dayIndex, "pickerValue" to this.pickerValue), " at pages/monthly/task-record.uvue:1676")
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
        console.log("picker原始变化事件:", _uO("value" to value, "oldSelectedYear" to this.selectedYear, "oldSelectedMonth" to this.selectedMonth, "oldSelectedDay" to this.selectedDay, "oldDaysLength" to oldDaysLength), " at pages/monthly/task-record.uvue:1698")
        this.selectedYear = this.years[value[0]]
        this.selectedMonth = value[1] + 1
        this.updateDays()
        var newDayIndex = value[2]
        if (newDayIndex >= this.days.length) {
            newDayIndex = this.days.length - 1
            console.log("日期超出范围，调整为月末:", this.days[newDayIndex], " at pages/monthly/task-record.uvue:1718")
        }
        this.selectedDay = this.days[newDayIndex]
        if (oldDaysLength !== this.days.length) {
            val timer = this.pickerDebounceTimer
            if (timer != null) {
                clearTimeout(timer)
            }
            this.pickerDebounceTimer = setTimeout(fun(){
                this.pickerValue = _uA(
                    value[0],
                    value[1],
                    newDayIndex
                )
                console.log("延迟更新pickerValue:", _uO("newPickerValue" to this.pickerValue, "selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "selectedDay" to this.selectedDay), " at pages/monthly/task-record.uvue:1733")
            }, 50)
        } else {
            this.pickerValue = _uA(
                value[0],
                value[1],
                newDayIndex
            )
            console.log("直接更新pickerValue:", _uO("newPickerValue" to this.pickerValue, "selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "selectedDay" to this.selectedDay), " at pages/monthly/task-record.uvue:1743")
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
        console.log("确认日期选择:", _uO("pickerValue" to this.pickerValue, "yearsArray" to this.years, "monthsArray" to this.months, "daysArray" to this.days, "selectedYear" to selectedYear, "selectedMonth" to selectedMonth, "selectedDay" to selectedDay, "formattedDate" to newDate), " at pages/monthly/task-record.uvue:1764")
        this.formData.rectificationDate = newDate
        this.hideDatePicker()
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
                return _uM("container" to _pS(_uM("backgroundColor" to "#f5f5f5", "position" to "relative", "zIndex" to 1, "paddingBottom" to "180rpx")), "header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "backgroundColor" to "#ffffff", "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0")), "header-left" to _pS(_uM("display" to "flex", "alignItems" to "center", "gap" to "5px")), "back-icon" to _pS(_uM("fontSize" to 24, "color" to "#007AFF")), "back-text" to _pS(_uM("fontSize" to 16, "color" to "#007AFF")), "header-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333")), "header-right" to _pS(_uM("display" to "flex", "alignItems" to "center")), "save-btn" to _pS(_uM("fontSize" to 16, "color" to "#007AFF")), "task-info" to _pS(_uM("backgroundColor" to "#ffffff", "marginTop" to 10, "marginRight" to 20, "marginBottom" to 10, "marginLeft" to 20, "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20)), "info-item" to _pS(_uM("display" to "flex", "paddingTop" to 12, "paddingRight" to 0, "paddingBottom" to 12, "paddingLeft" to 0, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "info-label" to _pS(_uM("fontSize" to 14, "color" to "#666666", "width" to 100, "flexShrink" to 0, "textAlign" to "left", "marginRight" to 12)), "info-value" to _pS(_uM("fontSize" to 14, "color" to "#333333", "flex" to 1, "textAlign" to "left")), "form-section" to _pS(_uM("backgroundColor" to "#ffffff", "marginTop" to 10, "marginRight" to 20, "marginBottom" to 10, "marginLeft" to 20, "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20)), "section-title" to _pS(_uM("display" to "flex", "alignItems" to "center", "marginBottom" to 20)), "title-text" to _pS(_uM("fontSize" to 16, "fontWeight" to "bold", "color" to "#333333")), "required-mark" to _pS(_uM("fontSize" to 16, "color" to "#ff4757", "marginLeft" to 4)), "form-item" to _pS(_uM("marginBottom" to 20, "marginBottom:last-child" to 0)), "form-item-inline" to _pS(_uM("marginBottom" to 20, "display" to "flex", "flexDirection" to "column", "gap" to "8px", "paddingTop" to 0, "paddingRight" to 20, "paddingBottom" to 0, "paddingLeft" to 20)), "form-label" to _pS(_uM("fontSize" to 14, "color" to "#333333", "marginBottom" to 8)), "form-label-inline" to _pS(_uM("fontSize" to 14, "color" to "#333333", "marginBottom" to 0)), "picker-wrapper" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "backgroundColor" to "#ffffff")), "picker-wrapper-inline" to _uM("" to _uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "backgroundColor" to "#ffffff", "width" to "100%", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "cursor" to "pointer", "borderTopColor:hover" to "#667eea", "borderRightColor:hover" to "#667eea", "borderBottomColor:hover" to "#667eea", "borderLeftColor:hover" to "#667eea", "boxShadow:hover" to "0 0 0 2px rgba(102, 126, 234, 0.1)", "transform:active" to "scale(0.98)"), ".disabled" to _uM("backgroundColor" to "#f5f5f5", "color" to "#999999", "cursor" to "not-allowed", "opacity" to 0.6), ".disabled:hover" to _uM("borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "boxShadow" to "none")), "text-input-inline" to _pS(_uM("paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "fontSize" to 14, "color" to "#333333", "backgroundColor" to "#ffffff", "flex" to 1)), "bottom-save-container" to _pS(_uM("position" to "fixed", "bottom" to 0, "left" to 0, "right" to 0, "paddingTop" to "20rpx", "paddingRight" to "30rpx", "paddingBottom" to "40rpx", "paddingLeft" to "30rpx", "backgroundImage" to "linear-gradient(to top, rgba(85, 170, 255, 1.0) 0%, rgba(85, 170, 255, 0.9) 100%)", "backgroundColor" to "rgba(0,0,0,0)", "backdropFilter" to "blur(10rpx)", "borderTopWidth" to "1rpx", "borderTopStyle" to "solid", "borderTopColor" to "rgba(240,240,240,0.8)", "zIndex" to 999)), "bottom-save-btn" to _pS(_uM("width" to "100%", "height" to "96rpx", "backgroundImage" to "linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderTopLeftRadius" to "48rpx", "borderTopRightRadius" to "48rpx", "borderBottomRightRadius" to "48rpx", "borderBottomLeftRadius" to "48rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "boxShadow" to "0 12rpx 32rpx rgba(79, 172, 254, 0.4)", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "cubic-bezier(0.4,0,0.2,1)", "position" to "relative", "overflow" to "hidden", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to "-100%", "width::before" to "100%", "height::before" to "100%", "backgroundImage::before" to "linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent)", "backgroundColor::before" to "rgba(0,0,0,0)", "transitionProperty::before" to "left", "transitionDuration::before" to "0.6s", "transform:active" to "translateY(2rpx) scale(0.98)", "boxShadow:active" to "0 8rpx 24rpx rgba(79, 172, 254, 0.3)", "left:active::before" to "100%")), "bottom-save-text" to _pS(_uM("color" to "#000000", "fontSize" to "34rpx", "fontWeight" to "700", "letterSpacing" to "3rpx", "position" to "relative", "zIndex" to 1)), "picker-text" to _uM("" to _uM("fontSize" to 14, "color" to "#333333"), ".placeholder" to _uM("color" to "#999999")), "picker-arrow" to _pS(_uM("fontSize" to 16, "color" to "#cccccc")), "text-input" to _pS(_uM("paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "fontSize" to 14, "color" to "#333333", "backgroundColor" to "#ffffff")), "textarea-input" to _pS(_uM("paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "fontSize" to 14, "color" to "#333333", "backgroundColor" to "#ffffff", "minHeight" to 80, "resize" to "none")), "char-count" to _pS(_uM("fontSize" to 12, "color" to "#999999", "textAlign" to "right", "marginTop" to 5)), "photo-upload" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "10px", "marginBottom" to 5)), "photo-item" to _pS(_uM("position" to "relative", "width" to 80, "height" to 80)), "photo-preview" to _pS(_uM("width" to "100%", "height" to "100%", "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "objectFit" to "cover")), "photo-delete" to _pS(_uM("position" to "absolute", "top" to -5, "right" to -5, "width" to 20, "height" to 20, "backgroundColor" to "#ff4757", "borderTopLeftRadius" to 10, "borderTopRightRadius" to 10, "borderBottomRightRadius" to 10, "borderBottomLeftRadius" to 10, "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "delete-icon" to _pS(_uM("fontSize" to 12, "color" to "#ffffff", "fontWeight" to "bold")), "photo-add" to _pS(_uM("width" to 80, "height" to 80, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#cccccc", "borderRightColor" to "#cccccc", "borderBottomColor" to "#cccccc", "borderLeftColor" to "#cccccc", "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "backgroundColor" to "#f8f9fa")), "add-icon" to _pS(_uM("fontSize" to 24, "color" to "#999999", "marginBottom" to 4)), "add-text" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "photo-tip" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "popup-overlay" to _pS(_uM("!position" to "fixed", "!top" to 0, "!left" to 0, "!width" to "100%", "!height" to "100%", "backgroundColor" to "rgba(0,0,0,0.5)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "!zIndex" to 9999, "animation" to "fadeIn 0.3s ease", "pointerEvents" to "auto")), "picker-modal" to _pS(_uM("!position" to "fixed", "!top" to 0, "!left" to 0, "!width" to "100%", "!height" to "100%", "backgroundColor" to "rgba(0,0,0,0.5)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "!zIndex" to 9999, "animation" to "fadeIn 0.3s ease", "pointerEvents" to "auto")), "popup-content" to _pS(_uM("!width" to "90%", "maxWidth" to 500, "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "overflow" to "hidden", "animation" to "scaleIn 0.3s ease", "boxShadow" to "0 8px 32px rgba(0, 0, 0, 0.2)", "position" to "relative", "zIndex" to 10000)), "picker-content" to _pS(_uM("!width" to "90%", "maxWidth" to 500, "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "overflow" to "hidden", "animation" to "scaleIn 0.3s ease", "boxShadow" to "0 8px 32px rgba(0, 0, 0, 0.2)", "position" to "relative", "zIndex" to 10000)), "popup-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#e9ecef", "backgroundImage" to "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", "backgroundColor" to "rgba(0,0,0,0)", "position" to "relative")), "picker-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#e9ecef", "backgroundImage" to "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", "backgroundColor" to "rgba(0,0,0,0)", "position" to "relative", "content::after" to "''", "position::after" to "absolute", "bottom::after" to 0, "left::after" to "50%", "transform::after" to "translateX(-50%)", "width::after" to 30, "height::after" to 3, "backgroundColor::after" to "rgba(255,255,255,0.3)", "borderTopLeftRadius::after" to 2, "borderTopRightRadius::after" to 2, "borderBottomRightRadius::after" to 2, "borderBottomLeftRadius::after" to 2)), "picker-cancel" to _pS(_uM("fontSize" to 16, "color" to "rgba(255,255,255,0.8)")), "picker-title" to _pS(_uM("fontSize" to 16, "color" to "#ffffff", "letterSpacing" to 1)), "picker-confirm" to _pS(_uM("fontSize" to 16, "color" to "#ffffff", "backgroundColor" to "rgba(255,255,255,0.2)", "paddingTop" to 6, "paddingRight" to 12, "paddingBottom" to 6, "paddingLeft" to 12, "borderTopLeftRadius" to 15, "borderTopRightRadius" to 15, "borderBottomRightRadius" to 15, "borderBottomLeftRadius" to 15, "transitionProperty" to "all", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "backgroundColor:active" to "rgba(255,255,255,0.3)", "transform:active" to "scale(0.95)")), "popup-title" to _pS(_uM("fontSize" to 16, "color" to "#ffffff", "letterSpacing" to 1)), "popup-close" to _pS(_uM("fontSize" to 24, "color" to "rgba(255,255,255,0.8)", "width" to 30, "height" to 30, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "borderTopLeftRadius" to 15, "borderTopRightRadius" to 15, "borderBottomRightRadius" to 15, "borderBottomLeftRadius" to 15, "transitionProperty" to "all", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "backgroundColor:active" to "rgba(255,255,255,0.2)", "transform:active" to "scale(0.9)")), "popup-search" to _pS(_uM("paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#e9ecef", "backgroundColor" to "#f8f9fa")), "search-input" to _pS(_uM("width" to "100%", "height" to 40, "paddingTop" to 0, "paddingRight" to 15, "paddingBottom" to 0, "paddingLeft" to 15, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "borderTopLeftRadius" to 20, "borderTopRightRadius" to 20, "borderBottomRightRadius" to 20, "borderBottomLeftRadius" to 20, "fontSize" to 14, "backgroundColor" to "#ffffff", "outline" to "none", "transitionProperty" to "all", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "borderTopColor:focus" to "#667eea", "borderRightColor:focus" to "#667eea", "borderBottomColor:focus" to "#667eea", "borderLeftColor:focus" to "#667eea", "boxShadow:focus" to "0 0 0 2px rgba(102, 126, 234, 0.2)")), "popup-list" to _pS(_uM("maxHeight" to 350, "overflowY" to "auto")), "popup-empty" to _pS(_uM("paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20, "textAlign" to "center")), "empty-text" to _pS(_uM("fontSize" to 14, "color" to "#999999")), "picker-list" to _pS(_uM("maxHeight" to 300, "overflowY" to "auto")), "picker-item" to _uM("" to _uM("display" to "flex", "justifyContent" to "center", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "cubic-bezier(0.4,0,0.2,1)", "cursor" to "pointer", "position" to "relative", "backgroundColor:hover" to "#f0f8ff", "color:hover" to "#007AFF", "backgroundColor:active" to "#e9ecef", "transform:active" to "scale(0.98)", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000", "height" to 60, "fontSize" to 16, "color" to "#666666", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "marginTop" to 4, "marginRight" to 8, "marginBottom" to 4, "marginLeft" to 8, "zIndex" to 2, "opacity" to 0.6, "backgroundColor" to "rgba(0,0,0,0.02)"), ".selected" to _uM("backgroundColor" to "rgba(0,0,0,0)", "backgroundImage" to "linear-gradient(135deg, #007AFF 0%, #0056CC 100%)", "color" to "#ffffff", "fontWeight" to "700", "boxShadow" to "0 4px 12px rgba(0, 122, 255, 0.4), inset 0 1px 0 rgba(255, 255, 255, 0.2)", "transform" to "scale(1.05)", "zIndex" to 3, "opacity" to 1, "fontSize" to 18, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#ffffff", "borderRightColor" to "#ffffff", "borderBottomColor" to "#ffffff", "borderLeftColor" to "#ffffff", "textShadow" to "0 1px 2px rgba(0, 0, 0, 0.3)")), "popup-item" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "transitionProperty" to "all", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "cursor" to "pointer", "backgroundColor:hover" to "#f8f9fa")), "popup-item-selected" to _pS(_uM("backgroundColor" to "#e8f2ff", "borderLeftWidth" to 4, "borderLeftStyle" to "solid", "borderLeftColor" to "#667eea", "paddingLeft" to 16, "backgroundColor:hover" to "#ddeeff")), "popup-item-text" to _pS(_uM("fontSize" to 16, "color" to "#333333", "fontWeight" to "400")), "picker-item-text" to _pS(_uM("fontSize" to 16, "color" to "#333333")), "level-item" to _pS(_uM("flex" to 1)), "level-name" to _pS(_uM("fontSize" to 16, "color" to "#333333", "marginBottom" to 4)), "level-desc" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "popup-item-check" to _pS(_uM("fontSize" to 18, "color" to "#667eea", "fontWeight" to "bold", "animation" to "checkIn 0.3s ease")), "popup-loading" to _pS(_uM("display" to "flex", "justifyContent" to "center", "alignItems" to "center", "paddingTop" to 40, "paddingRight" to 20, "paddingBottom" to 40, "paddingLeft" to 20)), "loading-text" to _pS(_uM("fontSize" to 14, "color" to "#999999")), "form-label-left" to _pS(_uM("fontSize" to 16, "color" to "#333333", "marginBottom" to 10, "fontWeight" to "400", "textAlign" to "left")), "custom-picker-full" to _uM("" to _uM("width" to "100%", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "borderTopLeftRadius" to "8rpx", "borderTopRightRadius" to "8rpx", "borderBottomRightRadius" to "8rpx", "borderBottomLeftRadius" to "8rpx", "backgroundColor" to "#ffffff", "minHeight" to "80rpx", "display" to "flex", "alignItems" to "center"), ".disabled" to _uM("backgroundImage" to "none", "backgroundColor" to "#f5f5f5", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "opacity" to 0.6)), "picker-container" to _pS(_uM("width" to "100%", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "borderTopLeftRadius" to "8rpx", "borderTopRightRadius" to "8rpx", "borderBottomRightRadius" to "8rpx", "borderBottomLeftRadius" to "8rpx", "backgroundColor" to "#ffffff", "minHeight" to "80rpx", "display" to "flex", "alignItems" to "center")), "picker-text-full" to _uM("" to _uM("fontSize" to "32rpx", "color" to "#333333", "width" to "100%", "textAlign" to "left"), ".placeholder" to _uM("color" to "#999999")), "date-picker-modal" to _pS(_uM("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundColor" to "rgba(0,0,0,0.5)", "display" to "flex", "alignItems" to "flex-end", "zIndex" to 1000)), "date-picker-content" to _pS(_uM("width" to "100%", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 16, "borderTopRightRadius" to 16, "borderBottomRightRadius" to 0, "borderBottomLeftRadius" to 0, "paddingBottom" to "env(safe-area-inset-bottom)")), "date-picker-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to 16, "paddingRight" to 20, "paddingBottom" to 16, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0")), "date-picker-cancel" to _pS(_uM("fontSize" to 16, "color" to "#999999")), "date-picker-title" to _pS(_uM("fontSize" to 18, "color" to "#333333")), "date-picker-confirm" to _pS(_uM("fontSize" to 16, "color" to "#007AFF")), "date-picker-view" to _pS(_uM("height" to 320, "paddingTop" to 0, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "position" to "relative")), "picker-item-spacer" to _pS(_uM("pointerEvents" to "none", "opacity" to 0, "!backgroundImage" to "none", "!backgroundColor" to "rgba(0,0,0,0)")), "form-input" to _pS(_uM("flex" to 1, "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "backgroundImage" to "none", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "fontSize" to 16, "color" to "#333333", "boxSizing" to "border-box", "width" to "100%")), "@FONT-FACE" to _uM("0" to _uM(), "1" to _uM(), "2" to _uM(), "3" to _uM()), "@TRANSITION" to _uM("picker-wrapper-inline" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "ease"), "bottom-save-btn" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "cubic-bezier(0.4,0,0.2,1)", "property::before" to "left", "duration::before" to "0.6s"), "picker-confirm" to _uM("property" to "all", "duration" to "0.2s", "timingFunction" to "ease"), "popup-close" to _uM("property" to "all", "duration" to "0.2s", "timingFunction" to "ease"), "search-input" to _uM("property" to "all", "duration" to "0.2s", "timingFunction" to "ease"), "picker-item" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "cubic-bezier(0.4,0,0.2,1)"), "popup-item" to _uM("property" to "all", "duration" to "0.2s", "timingFunction" to "ease")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
