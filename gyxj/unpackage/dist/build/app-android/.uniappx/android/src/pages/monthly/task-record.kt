@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "SENSELESS_COMPARISON", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uni.UNIB5A0920
import io.dcloud.uniapp.*
import io.dcloud.uniapp.extapi.*
import io.dcloud.uniapp.framework.*
import io.dcloud.uniapp.runtime.*
import io.dcloud.uniapp.vue.*
import io.dcloud.uniapp.vue.shared.*
import io.dcloud.uts.*
import io.dcloud.uts.Map
import io.dcloud.uts.Set
import io.dcloud.uts.UTSAndroid
import kotlin.properties.Delegates
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
            val recordId = options.getString("recordId") ?: ""
            val mode = options.getString("mode") ?: ""
            console.log("task-record onLoad: taskId=", taskId, " recordId=", recordId, " mode=", mode)
            this.loadCurrentUser().then(fun(){
                if (!this.currentUser.isMonthlyAdmin && this.currentUser.deptType === 0) {
                    uni_showToast(ShowToastOptions(title = "您的部门为运行部门，无录入权限", icon = "none", duration = 2000))
                    setTimeout(fun(){
                        uni_navigateBack(null)
                    }
                    , 1800)
                    return
                }
                this.formData.checkUnitId = this.currentUser.deptId
                this.formData.checkUnitName = this.currentUser.deptName
                this.loadTypeOptions()
                this.loadInspectorOptions()
                this.loadDeptOptions()
                this.loadIssueQualityMap()
                this.initDateArrays()
                if (recordId != "" && mode === "edit") {
                    this.isEditMode = true
                    this.recordId = recordId
                    this.loadRecordForEdit(recordId)
                    return
                }
                if (taskId != null && taskId != "") {
                    this.loadTaskInfo(taskId)
                }
            }
            )
        }
        , __ins)
        onReady(fun() {
            this.initContainerHeight()
        }
        , __ins)
        onUnload(fun() {
            val timer = this.pickerDebounceTimer
            if (timer != null) {
                clearTimeout(timer)
                this.pickerDebounceTimer = null
            }
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
                _cE("text", _uM("class" to "header-title"), _tD(if (_ctx.isEditMode) {
                    "月检记录编辑"
                } else {
                    "月检任务录入"
                }
                ), 1),
                _cE("view", _uM("class" to "header-right"))
            )),
            _cE("view", _uM("class" to "task-info"), _uA(
                _cE("view", _uM("class" to "info-item"), _uA(
                    _cE("text", _uM("class" to "info-label"), "任务名称:"),
                    _cE("text", _uM("class" to "info-value"), _tD(if (_ctx.taskInfo != null) {
                        _ctx.taskInfo.taskName
                    } else {
                        ""
                    }
                    ), 1)
                )),
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
                    _cE("text", _uM("class" to "info-label"), "公示时间:"),
                    _cE("text", _uM("class" to "info-value"), _tD(if (_ctx.taskInfo != null) {
                        _ctx.taskInfo.publicityDate
                    } else {
                        ""
                    }
                    ), 1)
                ))
            )),
            if (isTrue(_ctx.isEditMode && _ctx.originalSnapshot != "")) {
                _cE("view", _uM("key" to 0, "class" to "snapshot-panel"), _uA(
                    _cE("view", _uM("class" to "snapshot-title-row"), _uA(
                        _cE("text", _uM("class" to "snapshot-title"), "上次修改对比（仅参考）"),
                        _cE("text", _uM("class" to "snapshot-edit-badge"), "编辑模式")
                    )),
                    _cE("text", _uM("class" to "snapshot-content"), _tD(_ctx.originalSnapshot), 1)
                ))
            } else {
                _cC("v-if", true)
            }
            ,
            _cE("view", _uM("class" to "form-section"), _uA(
                _cE("view", _uM("class" to "section-title"), _uA(
                    _cE("text", _uM("class" to "title-text"), "问题录入"),
                    _cE("text", _uM("class" to "required-mark"), "*")
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
                    _cE("text", _uM("class" to "photo-tip"), "最多可上传3张照片，单张不超过5MB")
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "受检单位"),
                    _cE("view", _uM("class" to _nC(_uA(
                        "custom-picker-full",
                        _uM("disabled" to (_ctx.deptOptions.length === 0))
                    )), "onClick" to _ctx.showInspectedUnitPicker), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.formData.departmentId === ""))
                        ))), _tD(if (_ctx.selectedInspectedUnitName !== "") {
                            _ctx.selectedInspectedUnitName
                        } else {
                            "请选择受检单位"
                        }
                        ), 3)
                    ), 10, _uA(
                        "onClick"
                    ))
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
                                (_ctx.typeOptions[_ctx.selectedTypeIndex] as OptionItem__1).name
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
                    _cE("text", _uM("class" to "form-label-left"), "基本模块"),
                    _cE("view", _uM("class" to _nC(_uA(
                        "custom-picker-full",
                        _uM("disabled" to (_ctx.moduleOptions.length === 0))
                    )), "onClick" to _ctx.showModulePicker), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.selectedModuleIndex === -1))
                        ))), _tD(if (_ctx.selectedModuleIndex === -1) {
                            "请选择基本模块"
                        } else {
                            if (_ctx.selectedModuleIndex < _ctx.moduleOptions.length) {
                                (_ctx.moduleOptions[_ctx.selectedModuleIndex] as OptionItem__1).name
                            } else {
                                "请选择基本模块"
                            }
                        }
                        ), 3)
                    ), 10, _uA(
                        "onClick"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "责任要素"),
                    _cE("view", _uM("class" to _nC(_uA(
                        "custom-picker-full",
                        _uM("disabled" to (_ctx.responsibilityOptions.length === 0))
                    )), "onClick" to _ctx.showResponsibilityPicker), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.selectedResponsibilityIndex === -1))
                        ))), _tD(if (_ctx.selectedResponsibilityIndex === -1) {
                            "请选择责任要素"
                        } else {
                            if (_ctx.selectedResponsibilityIndex < _ctx.responsibilityOptions.length) {
                                (_ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex] as OptionItem__1).name
                            } else {
                                "请选择责任要素"
                            }
                        }
                        ), 3)
                    ), 10, _uA(
                        "onClick"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "检查内容"),
                    _cE("view", _uM("class" to _nC(_uA(
                        "custom-picker-full",
                        _uM("disabled" to (_ctx.issueOptions.length === 0))
                    )), "onClick" to _ctx.showIssuePicker), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.selectedIssueIndex === -1))
                        ))), _tD(if (_ctx.selectedIssueIndex === -1) {
                            "请选择检查内容"
                        } else {
                            if (_ctx.selectedIssueIndex < _ctx.issueOptions.length) {
                                (_ctx.issueOptions[_ctx.selectedIssueIndex] as OptionItem__1).name
                            } else {
                                "请选择检查内容"
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
                                (_ctx.qualityOptions[_ctx.selectedQualityIndex] as OptionItem__1).name
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
                    _cE("text", _uM("class" to "form-label-left"), "是否人工修改"),
                    _cE("view", _uM("class" to "custom-picker-full readonly-display"), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("nature-modified-yes" to (_ctx.formData.natureModified === 1))
                        ))), _tD(if (_ctx.formData.natureModified === 1) {
                            "是(已修改)"
                        } else {
                            "否(未修改)"
                        }
                        ), 3)
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "扣分值"),
                    _cE("view", _uM("class" to _nC(_uA(
                        "custom-picker-full readonly-display",
                        _uM("placeholder" to (_ctx.selectedQualityIndex === -1))
                    ))), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.selectedQualityIndex === -1))
                        ))), _tD(if (_ctx.selectedQualityIndex === -1) {
                            "选择性质界定后自动显示"
                        } else {
                            if (_ctx.selectedQualityIndex < _ctx.qualityOptions.length) {
                                ((_ctx.qualityOptions[_ctx.selectedQualityIndex] as OptionItem__1).score ?: "")
                            } else {
                                "选择性质界定后自动显示"
                            }
                        }
                        ), 3)
                    ), 2)
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "检查单位"),
                    _cE("view", _uM("class" to "custom-picker-full readonly-display"), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.formData.checkUnitName === ""))
                        ))), _tD(if (_ctx.formData.checkUnitName !== "") {
                            _ctx.formData.checkUnitName
                        } else {
                            "当前用户部门未设置"
                        }
                        ), 3)
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
                            _uM("placeholder" to (_ctx.formData.inspector.length === 0))
                        ))), _tD(if (_ctx.formData.inspector.length > 0) {
                            _ctx.formData.inspector.join("、")
                        } else {
                            "请选择检查人（可多选）"
                        }
                        ), 3)
                    ), 10, _uA(
                        "onClick"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "责任人"),
                    _cE("view", _uM("class" to _nC(_uA(
                        "custom-picker-full",
                        _uM("disabled" to (_ctx.inspectorOptions.length === 0))
                    )), "onClick" to _ctx.showResponsiblePersonPicker), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.formData.responsiblePerson.length === 0))
                        ))), _tD(if (_ctx.formData.responsiblePerson.length > 0) {
                            _ctx.formData.responsiblePerson.join("、")
                        } else {
                            "请选择责任人（可多选）"
                        }
                        ), 3)
                    ), 10, _uA(
                        "onClick"
                    ))
                )),
                _cE("view", _uM("class" to "form-item"), _uA(
                    _cE("text", _uM("class" to "form-label-left"), "责任单位"),
                    _cE("view", _uM("class" to _nC(_uA(
                        "custom-picker-full",
                        _uM("disabled" to (_ctx.deptOptions.length === 0))
                    )), "onClick" to _ctx.showResponsibleUnitPicker), _uA(
                        _cE("text", _uM("class" to _nC(_uA(
                            "picker-text-full",
                            _uM("placeholder" to (_ctx.formData.responsibleUnit.length === 0))
                        ))), _tD(if (_ctx.formData.responsibleUnit.length > 0) {
                            _ctx.formData.responsibleUnit.join("、")
                        } else {
                            "请选择责任单位（可多选）"
                        }
                        ), 3)
                    ), 10, _uA(
                        "onClick"
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
                _cE("view", _uM("key" to 1, "class" to "picker-modal", "onClick" to _ctx.hideProblemTypePicker), _uA(
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
                _cE("view", _uM("key" to 2, "class" to "popup-overlay", "onClick" to _ctx.hideTypePicker), _uA(
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
                                    return _cE("view", _uM("key" to (item as OptionItem__1).id, "class" to _nC(_uA(
                                        "popup-item",
                                        if (_ctx.selectedTypeIndex >= 0 && _ctx.typeOptions[_ctx.selectedTypeIndex] != null && _ctx.typeOptions[_ctx.selectedTypeIndex].id === (item as OptionItem__1).id) {
                                            "popup-item-selected"
                                        } else {
                                            ""
                                        }
                                    )), "onClick" to fun(){
                                        _ctx.selectTypeFromFiltered(item)
                                    }), _uA(
                                        _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem__1).name), 1),
                                        if (isTrue(_ctx.selectedTypeIndex >= 0 && _ctx.typeOptions[_ctx.selectedTypeIndex] != null && _ctx.typeOptions[_ctx.selectedTypeIndex].id === (item as OptionItem__1).id)) {
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
                _cE("view", _uM("key" to 3, "class" to "popup-overlay", "onClick" to _ctx.hideModulePicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择基本模块"),
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
                                        return _cE("view", _uM("key" to (item as OptionItem__1).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.selectedModuleIndex >= 0 && _ctx.moduleOptions[_ctx.selectedModuleIndex] != null && _ctx.moduleOptions[_ctx.selectedModuleIndex].id === (item as OptionItem__1).id) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.selectModuleFromFiltered(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem__1).name), 1),
                                            if (isTrue(_ctx.selectedModuleIndex >= 0 && _ctx.moduleOptions[_ctx.selectedModuleIndex] != null && _ctx.moduleOptions[_ctx.selectedModuleIndex].id === (item as OptionItem__1).id)) {
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
                _cE("view", _uM("key" to 4, "class" to "popup-overlay", "onClick" to _ctx.hideResponsibilityPicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择责任要素"),
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
                                        return _cE("view", _uM("key" to (item as OptionItem__1).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.selectedResponsibilityIndex >= 0 && _ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex] != null && _ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex].id === (item as OptionItem__1).id) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.selectResponsibilityFromFiltered(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem__1).name), 1),
                                            if (isTrue(_ctx.selectedResponsibilityIndex >= 0 && _ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex] != null && _ctx.responsibilityOptions[_ctx.selectedResponsibilityIndex].id === (item as OptionItem__1).id)) {
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
                _cE("view", _uM("key" to 5, "class" to "popup-overlay", "onClick" to _ctx.hideIssuePicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择检查内容"),
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
                                        return _cE("view", _uM("key" to (item as OptionItem__1).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.selectedIssueIndex !== -1 && _ctx.issueOptions[_ctx.selectedIssueIndex] != null && _ctx.issueOptions[_ctx.selectedIssueIndex].id === (item as OptionItem__1).id) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.selectIssueFromFiltered(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem__1).name), 1),
                                            if (isTrue(_ctx.selectedIssueIndex !== -1 && _ctx.issueOptions[_ctx.selectedIssueIndex] != null && _ctx.issueOptions[_ctx.selectedIssueIndex].id === (item as OptionItem__1).id)) {
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
                _cE("view", _uM("key" to 6, "class" to "popup-overlay", "onClick" to _ctx.hideQualityPicker), _uA(
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
                                        return _cE("view", _uM("key" to (item as OptionItem__1).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.selectedQualityIndex !== -1 && _ctx.qualityOptions[_ctx.selectedQualityIndex] != null && _ctx.qualityOptions[_ctx.selectedQualityIndex].id === (item as OptionItem__1).id) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.selectQualityFromFiltered(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem__1).name), 1),
                                            if (isTrue(_ctx.selectedQualityIndex !== -1 && _ctx.qualityOptions[_ctx.selectedQualityIndex] != null && _ctx.qualityOptions[_ctx.selectedQualityIndex].id === (item as OptionItem__1).id)) {
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
            if (isTrue(_ctx.showInspectorPopup)) {
                _cE("view", _uM("key" to 7, "class" to "popup-overlay", "onClick" to _ctx.hideInspectorPicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择检查人（可多选）"),
                            _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideInspectorPicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "popup-search"), _uA(
                            _cE("input", _uM("class" to "search-input", "modelValue" to _ctx.inspectorSearchKeyword, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.inspectorSearchKeyword = `$event`.detail.value
                            }, "placeholder" to "搜索检查人..."), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("scroll-view", _uM("class" to "popup-list", "scroll-y" to "true"), _uA(
                            if (_ctx.inspectorOptions.length === 0) {
                                _cE("view", _uM("key" to 0, "class" to "popup-loading"), _uA(
                                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                                ))
                            } else {
                                if (_ctx.filteredInspectorOptions.length === 0) {
                                    _cE("view", _uM("key" to 1, "class" to "popup-empty"), _uA(
                                        _cE("text", _uM("class" to "empty-text"), "暂无匹配的人员")
                                    ))
                                } else {
                                    _cE(Fragment, _uM("key" to 2), RenderHelpers.renderList(_ctx.filteredInspectorOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to (item as OptionItem__1).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.formData.inspector.indexOf((item as OptionItem__1).displayName ?: (item as OptionItem__1).name) !== -1) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.toggleInspectorFromFiltered(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem__1).displayName ?: (item as OptionItem__1).name), 1),
                                            if (_ctx.formData.inspector.indexOf((item as OptionItem__1).displayName ?: (item as OptionItem__1).name) !== -1) {
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
                            _cE("text", _uM("class" to "popup-btn popup-btn-cancel", "onClick" to _ctx.hideInspectorPicker), "取消", 8, _uA(
                                "onClick"
                            )),
                            _cE("text", _uM("class" to "popup-btn popup-btn-confirm", "onClick" to _ctx.confirmInspectorPicker), "确定（" + _tD(_ctx.formData.inspector.length) + "）", 9, _uA(
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
            if (isTrue(_ctx.showResponsiblePersonPopup)) {
                _cE("view", _uM("key" to 8, "class" to "popup-overlay", "onClick" to _ctx.hideResponsiblePersonPicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择责任人（可多选）"),
                            _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideResponsiblePersonPicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "popup-search"), _uA(
                            _cE("input", _uM("class" to "search-input", "modelValue" to _ctx.inspectorSearchKeyword, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.inspectorSearchKeyword = `$event`.detail.value
                            }, "placeholder" to "搜索责任人..."), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("scroll-view", _uM("class" to "popup-list", "scroll-y" to "true"), _uA(
                            if (_ctx.inspectorOptions.length === 0) {
                                _cE("view", _uM("key" to 0, "class" to "popup-loading"), _uA(
                                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                                ))
                            } else {
                                if (_ctx.filteredResponsiblePersonOptions.length === 0) {
                                    _cE("view", _uM("key" to 1, "class" to "popup-empty"), _uA(
                                        _cE("text", _uM("class" to "empty-text"), "暂无匹配的人员")
                                    ))
                                } else {
                                    _cE(Fragment, _uM("key" to 2), RenderHelpers.renderList(_ctx.filteredResponsiblePersonOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to (item as OptionItem__1).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.formData.responsiblePerson.indexOf((item as OptionItem__1).displayName ?: (item as OptionItem__1).name) !== -1) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.toggleResponsiblePersonFromFiltered(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem__1).displayName ?: (item as OptionItem__1).name), 1),
                                            if (_ctx.formData.responsiblePerson.indexOf((item as OptionItem__1).displayName ?: (item as OptionItem__1).name) !== -1) {
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
                            _cE("text", _uM("class" to "popup-btn popup-btn-cancel", "onClick" to _ctx.hideResponsiblePersonPicker), "取消", 8, _uA(
                                "onClick"
                            )),
                            _cE("text", _uM("class" to "popup-btn popup-btn-confirm", "onClick" to _ctx.confirmResponsiblePersonPicker), "确定（" + _tD(_ctx.formData.responsiblePerson.length) + "）", 9, _uA(
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
            if (isTrue(_ctx.showInspectedUnitPopup)) {
                _cE("view", _uM("key" to 9, "class" to "popup-overlay", "onClick" to _ctx.hideInspectedUnitPicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择受检单位"),
                            _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideInspectedUnitPicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "popup-search"), _uA(
                            _cE("input", _uM("class" to "search-input", "modelValue" to _ctx.deptSearchKeyword, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.deptSearchKeyword = `$event`.detail.value
                            }, "placeholder" to "搜索部门..."), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("scroll-view", _uM("class" to "popup-list", "scroll-y" to "true"), _uA(
                            if (_ctx.deptOptions.length === 0) {
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
                                        return _cE("view", _uM("key" to (item as OptionItem__1).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.selectedDeptIndex !== -1 && _ctx.deptOptions[_ctx.selectedDeptIndex] != null && _ctx.deptOptions[_ctx.selectedDeptIndex].id === (item as OptionItem__1).id) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.selectInspectedUnitFromFiltered(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem__1).name), 1),
                                            if (isTrue(_ctx.selectedDeptIndex !== -1 && _ctx.deptOptions[_ctx.selectedDeptIndex] != null && _ctx.deptOptions[_ctx.selectedDeptIndex].id === (item as OptionItem__1).id)) {
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
            if (isTrue(_ctx.showResponsibleUnitPopup)) {
                _cE("view", _uM("key" to 10, "class" to "popup-overlay", "onClick" to _ctx.hideResponsibleUnitPicker), _uA(
                    _cE("view", _uM("class" to "popup-content", "onClick" to withModifiers(fun(){}, _uA(
                        "stop"
                    ))), _uA(
                        _cE("view", _uM("class" to "popup-header"), _uA(
                            _cE("text", _uM("class" to "popup-title"), "选择责任单位（可多选）"),
                            _cE("text", _uM("class" to "popup-close", "onClick" to _ctx.hideResponsibleUnitPicker), "×", 8, _uA(
                                "onClick"
                            ))
                        )),
                        _cE("view", _uM("class" to "popup-search"), _uA(
                            _cE("input", _uM("class" to "search-input", "modelValue" to _ctx.responsibleUnitSearchKeyword, "onInput" to fun(`$event`: UniInputEvent){
                                _ctx.responsibleUnitSearchKeyword = `$event`.detail.value
                            }, "placeholder" to "搜索部门..."), null, 40, _uA(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        _cE("scroll-view", _uM("class" to "popup-list", "scroll-y" to "true"), _uA(
                            if (_ctx.deptOptions.length === 0) {
                                _cE("view", _uM("key" to 0, "class" to "popup-loading"), _uA(
                                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                                ))
                            } else {
                                if (_ctx.filteredResponsibleUnitOptions.length === 0) {
                                    _cE("view", _uM("key" to 1, "class" to "popup-empty"), _uA(
                                        _cE("text", _uM("class" to "empty-text"), "暂无匹配的部门")
                                    ))
                                } else {
                                    _cE(Fragment, _uM("key" to 2), RenderHelpers.renderList(_ctx.filteredResponsibleUnitOptions, fun(item, index, __index, _cached): Any {
                                        return _cE("view", _uM("key" to (item as OptionItem__1).id, "class" to _nC(_uA(
                                            "popup-item",
                                            if (_ctx.formData.responsibleUnit.indexOf((item as OptionItem__1).name) !== -1) {
                                                "popup-item-selected"
                                            } else {
                                                ""
                                            }
                                        )), "onClick" to fun(){
                                            _ctx.toggleResponsibleUnitFromFiltered(item)
                                        }), _uA(
                                            _cE("text", _uM("class" to "popup-item-text"), _tD((item as OptionItem__1).name), 1),
                                            if (_ctx.formData.responsibleUnit.indexOf((item as OptionItem__1).name) !== -1) {
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
                            _cE("text", _uM("class" to "popup-btn popup-btn-cancel", "onClick" to _ctx.hideResponsibleUnitPicker), "取消", 8, _uA(
                                "onClick"
                            )),
                            _cE("text", _uM("class" to "popup-btn popup-btn-confirm", "onClick" to _ctx.confirmResponsibleUnitPicker), "确定（" + _tD(_ctx.formData.responsibleUnit.length) + "）", 9, _uA(
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
    open var showInspectorPopup: Boolean by `$data`
    open var showResponsiblePersonPopup: Boolean by `$data`
    open var typeOptions: UTSArray<OptionItem__1> by `$data`
    open var moduleOptions: UTSArray<OptionItem__1> by `$data`
    open var responsibilityOptions: UTSArray<OptionItem__1> by `$data`
    open var issueOptions: UTSArray<OptionItem__1> by `$data`
    open var qualityOptions: UTSArray<OptionItem__1> by `$data`
    open var inspectorOptions: UTSArray<OptionItem__1> by `$data`
    open var deptOptions: UTSArray<OptionItem__1> by `$data`
    open var showDeptPopup: Boolean by `$data`
    open var showInspectedUnitPopup: Boolean by `$data`
    open var showResponsibleUnitPopup: Boolean by `$data`
    open var selectedDeptIndex: Number by `$data`
    open var deptSearchKeyword: String by `$data`
    open var responsibleUnitSearchKeyword: String by `$data`
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
    open var selectedQualityName: String by `$data`
    open var deductionScoreDisplay: String by `$data`
    open var inspectorSearchKeyword: String by `$data`
    open var datePickerVisible: Boolean by `$data`
    open var pickerValue: UTSArray<Number> by `$data`
    open var years: UTSArray<Number> by `$data`
    open var months: UTSArray<Number> by `$data`
    open var days: UTSArray<Number> by `$data`
    open var selectedYear: Number by `$data`
    open var selectedMonth: Number by `$data`
    open var selectedDay: Number by `$data`
    open var pickerDebounceTimer: Number? by `$data`
    open var issueQualityMap: IssueQualityMap by `$data`
    open var issueQualityKeys: UTSArray<String> by `$data`
    open var currentUser: CurrentUser__3 by `$data`
    open var autoFilledQualityId: String by `$data`
    open var isEditMode: Boolean by `$data`
    open var recordId: String by `$data`
    open var originalSnapshot: String by `$data`
    open var filteredTypeOptions: UTSArray<OptionItem__1> by `$data`
    open var filteredModuleOptions: UTSArray<OptionItem__1> by `$data`
    open var filteredResponsibilityOptions: UTSArray<OptionItem__1> by `$data`
    open var filteredIssueOptions: UTSArray<OptionItem__1> by `$data`
    open var filteredQualityOptions: UTSArray<OptionItem__1> by `$data`
    open var filteredInspectorOptions: UTSArray<OptionItem__1> by `$data`
    open var filteredResponsiblePersonOptions: UTSArray<OptionItem__1> by `$data`
    open var filteredDeptOptions: UTSArray<OptionItem__1> by `$data`
    open var filteredResponsibleUnitOptions: UTSArray<OptionItem__1> by `$data`
    open var selectedInspectedUnitName: String by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        val defaultDate = Date()
        defaultDate.setDate(defaultDate.getDate() + 7)
        val defaultDateStr = "" + defaultDate.getFullYear() + "-" + (defaultDate.getMonth() + 1).toString(10).padStart(2, "0") + "-" + defaultDate.getDate().toString(10).padStart(2, "0")
        return _uM("taskInfo" to TaskInfo(taskId = "", taskNumber = "", taskName = "", inspectionDate = "", publicityDate = "", inspectionStartTime = "", inspectionEndTime = ""), "formData" to FormData(problemType = "", problemDescription = "", location = "", photos = _uA(), rectificationDate = defaultDateStr, module = "", responsibility = "", issue = "", quality = "", inspector = _uA(), responsibleUnit = _uA(), responsiblePerson = _uA(), departmentId = "", checkUnitId = "", checkUnitName = "", natureModified = 0, safetyScore = ""), "containerHeight" to 0, "scrollTop" to 0, "showProblemTypeModal" to false, "selectedProblemType" to "", "problemTypes" to _uA(
            "安全隐患",
            "设备故障",
            "环境问题",
            "操作违规",
            "制度缺失",
            "其他问题"
        ), "showTypePopup" to false, "showModulePopup" to false, "showResponsibilityPopup" to false, "showIssuePopup" to false, "showQualityPopup" to false, "showInspectorPopup" to false, "showResponsiblePersonPopup" to false, "typeOptions" to _uA<OptionItem__1>(), "moduleOptions" to _uA<OptionItem__1>(), "responsibilityOptions" to _uA<OptionItem__1>(), "issueOptions" to _uA<OptionItem__1>(), "qualityOptions" to _uA<OptionItem__1>(), "inspectorOptions" to _uA<OptionItem__1>(), "deptOptions" to _uA<OptionItem__1>(), "showDeptPopup" to false, "showInspectedUnitPopup" to false, "showResponsibleUnitPopup" to false, "selectedDeptIndex" to -1, "deptSearchKeyword" to "", "responsibleUnitSearchKeyword" to "", "selectedTypeIndex" to -1, "selectedModuleIndex" to -1, "selectedResponsibilityIndex" to -1, "selectedIssueIndex" to -1, "selectedQualityIndex" to -1, "typeSearchKeyword" to "", "moduleSearchKeyword" to "", "responsibilitySearchKeyword" to "", "issueSearchKeyword" to "", "qualitySearchKeyword" to "", "selectedQualityName" to "", "deductionScoreDisplay" to "", "inspectorSearchKeyword" to "", "datePickerVisible" to false, "pickerValue" to _uA(
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
        )() as Number, "pickerDebounceTimer" to null as Number?, "issueQualityMap" to Map<String, String>() as IssueQualityMap, "issueQualityKeys" to _uA<String>(), "currentUser" to CurrentUser__3(id = "", realName = "", deptId = "", deptName = "", deptType = 0, isMonthlyAdmin = false), "autoFilledQualityId" to "", "isEditMode" to false, "recordId" to "", "originalSnapshot" to "", "filteredTypeOptions" to computed<UTSArray<OptionItem__1>>(fun(): UTSArray<OptionItem__1> {
            if (this.typeSearchKeyword == "") {
                return this.typeOptions
            }
            return this.typeOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.typeSearchKeyword.toLowerCase())
            }
            )
        }
        ), "filteredModuleOptions" to computed<UTSArray<OptionItem__1>>(fun(): UTSArray<OptionItem__1> {
            if (this.moduleSearchKeyword == "") {
                return this.moduleOptions
            }
            return this.moduleOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.moduleSearchKeyword.toLowerCase())
            }
            )
        }
        ), "filteredResponsibilityOptions" to computed<UTSArray<OptionItem__1>>(fun(): UTSArray<OptionItem__1> {
            if (this.responsibilitySearchKeyword == "") {
                return this.responsibilityOptions
            }
            return this.responsibilityOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.responsibilitySearchKeyword.toLowerCase())
            }
            )
        }
        ), "filteredIssueOptions" to computed<UTSArray<OptionItem__1>>(fun(): UTSArray<OptionItem__1> {
            if (this.issueSearchKeyword == "") {
                return this.issueOptions
            }
            return this.issueOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.issueSearchKeyword.toLowerCase())
            }
            )
        }
        ), "filteredQualityOptions" to computed<UTSArray<OptionItem__1>>(fun(): UTSArray<OptionItem__1> {
            if (this.qualitySearchKeyword == "") {
                return this.qualityOptions
            }
            return this.qualityOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.qualitySearchKeyword.toLowerCase())
            }
            )
        }
        ), "filteredInspectorOptions" to computed<UTSArray<OptionItem__1>>(fun(): UTSArray<OptionItem__1> {
            if (this.inspectorSearchKeyword == "") {
                return this.inspectorOptions
            }
            val kw = this.inspectorSearchKeyword.toLowerCase()
            return this.inspectorOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(kw) || (item.displayName ?: "").toLowerCase().includes(kw)
            }
            )
        }
        ), "filteredResponsiblePersonOptions" to computed<UTSArray<OptionItem__1>>(fun(): UTSArray<OptionItem__1> {
            if (this.inspectorSearchKeyword == "") {
                return this.inspectorOptions
            }
            val kw = this.inspectorSearchKeyword.toLowerCase()
            return this.inspectorOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(kw) || (item.displayName ?: "").toLowerCase().includes(kw)
            }
            )
        }
        ), "filteredDeptOptions" to computed<UTSArray<OptionItem__1>>(fun(): UTSArray<OptionItem__1> {
            if (this.deptSearchKeyword == "") {
                return this.deptOptions
            }
            return this.deptOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.deptSearchKeyword.toLowerCase())
            }
            )
        }
        ), "filteredResponsibleUnitOptions" to computed<UTSArray<OptionItem__1>>(fun(): UTSArray<OptionItem__1> {
            if (this.responsibleUnitSearchKeyword == "") {
                return this.deptOptions
            }
            return this.deptOptions.filter(fun(item): Boolean {
                return item.name.toLowerCase().includes(this.responsibleUnitSearchKeyword.toLowerCase())
            }
            )
        }
        ), "selectedInspectedUnitName" to computed<String>(fun(): String {
            if (this.selectedDeptIndex === -1) {
                return ""
            }
            val opt = this.deptOptions[this.selectedDeptIndex]
            return if (opt != null) {
                opt.name
            } else {
                ""
            }
        }
        ))
    }
    open var syncDeductionScore = ::gen_syncDeductionScore_fn
    open fun gen_syncDeductionScore_fn() {
        var name = ""
        if (this.selectedQualityIndex !== -1 && this.selectedQualityIndex < this.qualityOptions.length) {
            val opt = this.qualityOptions[this.selectedQualityIndex] as OptionItem__1
            name = opt.name
        }
        if (name === "") {
            name = this.selectedQualityName
        }
        val trimmed = stripOuterWhitespace(name)
        val normalized = resolveQualityLabel(trimmed, trimmed)
        if (normalized.indexOf("观察") !== -1) {
            this.deductionScoreDisplay = "1"
        } else if (normalized.indexOf("一般") !== -1) {
            this.deductionScoreDisplay = "3"
        } else if (normalized.indexOf("重要") !== -1) {
            this.deductionScoreDisplay = "5"
        } else {
            this.deductionScoreDisplay = ""
        }
        val optScore = if ((this.selectedQualityIndex !== -1 && this.selectedQualityIndex < this.qualityOptions.length)) {
            ((this.qualityOptions[this.selectedQualityIndex] as OptionItem__1).score ?: "<null>")
        } else {
            "<no-index>"
        }
        console.log("syncDeductionScore: name=[" + name + "] normalized=[" + normalized + "] display=[" + this.deductionScoreDisplay + "] optScore=[" + optScore + "] optsLen=" + this.qualityOptions.length)
    }
    open var loadRecordForEdit = ::gen_loadRecordForEdit_fn
    open fun gen_loadRecordForEdit_fn(recordId: String) {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-record/" + recordId), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val body = res.data as UTSJSONObject
            if (res.statusCode !== 200 || (body.getNumber("code") ?: 0) !== 200) {
                uni_showToast(ShowToastOptions(title = body.getString("message") ?: "加载记录失败", icon = "none"))
                setTimeout(fun(){
                    uni_navigateBack(null)
                }
                , 1500)
                return
            }
            val d = body.getJSON("data")
            if (d == null) {
                uni_showToast(ShowToastOptions(title = "记录不存在", icon = "none"))
                setTimeout(fun(){
                    uni_navigateBack(null)
                }
                , 1500)
                return
            }
            val dObj = d as UTSJSONObject
            val snap = dObj.getString("modificationSnapshot") ?: ""
            this.originalSnapshot = snap
            this.formData.problemDescription = dObj.getString("problemDescription") ?: ""
            this.formData.location = dObj.getString("location") ?: ""
            val photos = dObj.getString("photos") ?: ""
            if (photos != "" && photos != null) {
                val photoArr = photos.split(",").map(fun(s): String {
                    return stripOuterWhitespace(s)
                }
                ).filter(fun(s): Boolean {
                    return s != ""
                }
                )
                this.formData.photos = photoArr
            }
            val rectDate = dObj.getString("rectificationDate") ?: dObj.getString("rectificationDeadline") ?: ""
            if (rectDate != "" && rectDate != null) {
                val norm = if (rectDate.length >= 10) {
                    rectDate.substring(0, 10)
                } else {
                    rectDate
                }
                this.formData.rectificationDate = norm
            }
            val insp = dObj.getString("inspector") ?: ""
            if (insp != "" && insp != null) {
                val parts = insp.split(UTSRegExp("[、,]", "")).map(fun(s): String {
                    return stripOuterWhitespace(s)
                }
                ).filter(fun(s): Boolean {
                    return s != ""
                }
                )
                this.formData.inspector = parts
            }
            val ru = dObj.getString("responsibleUnit") ?: ""
            if (ru != "" && ru != null) {
                val parts = ru.split(UTSRegExp("[、,]", "")).map(fun(s): String {
                    return stripOuterWhitespace(s)
                }
                ).filter(fun(s): Boolean {
                    return s != ""
                }
                )
                this.formData.responsibleUnit = parts
            }
            val rp = dObj.getString("responsiblePerson") ?: ""
            if (rp != "" && rp != null) {
                val parts = rp.split(UTSRegExp("[、,]", "")).map(fun(s): String {
                    return stripOuterWhitespace(s)
                }
                ).filter(fun(s): Boolean {
                    return s != ""
                }
                )
                this.formData.responsiblePerson = parts
            }
            this.formData.departmentId = dObj.getString("departmentId") ?: ""
            this.formData.checkUnitId = dObj.getString("checkUnitId") ?: this.currentUser.deptId
            this.formData.checkUnitName = dObj.getString("checkUnitName") ?: this.currentUser.deptName
            this.formData.problemType = dObj.getString("professionalTypeName") ?: dObj.getString("professionalCategory") ?: ""
            this.formData.module = dObj.getString("moduleName") ?: ""
            this.formData.responsibility = dObj.getString("responsibilityName") ?: ""
            this.formData.issue = dObj.getString("issueName") ?: ""
            this.formData.quality = dObj.getString("qualityName") ?: ""
            this.formData.safetyScore = ""
            this.formData.natureModified = (dObj.getNumber("natureModified") ?: 0) as Number
            this.taskInfo.taskId = dObj.getString("taskId") ?: ""
            uni_showToast(ShowToastOptions(title = "已进入编辑模式", icon = "none", duration = 1500))
        }
        , fail = fun(_){
            uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
            setTimeout(fun(){
                uni_navigateBack(null)
            }
            , 1500)
        }
        ))
    }
    open var loadCurrentUser = ::gen_loadCurrentUser_fn
    open fun gen_loadCurrentUser_fn(): UTSPromise<Unit> {
        return UTSPromise<Unit>(fun(resolve, _reject){
            resolveCurrentDept().then(fun(dept: CurrentDept){
                val info = getCachedUserInfo()
                val admin = isMonthlyAdmin()
                this.currentUser = CurrentUser__3(id = info.getString("id") ?: "", realName = info.getString("realName") ?: info.getString("username") ?: "", deptId = dept.deptId, deptName = dept.deptName, deptType = dept.deptType ?: 0, isMonthlyAdmin = admin)
                resolve(Unit)
            }
            )
        }
        )
    }
    open var previewPhoto = ::gen_previewPhoto_fn
    open fun gen_previewPhoto_fn(photo: String) {
        uni_previewImage(PreviewImageOptions(current = this.convertToHttpUrl(photo), urls = this.formData.photos.map(fun(p: String): String {
            return this.convertToHttpUrl(p)
        }
        ) as UTSArray<String>))
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
        console.log("相对路径:", relativePath)
        if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) {
            return relativePath
        }
        val httpUrl = "" + apiConfig.baseUrl + "/api" + relativePath
        console.log("转换后的HTTP URL:", httpUrl)
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
            console.log("接收到的taskId参数:", taskId)
            uni_request<Any>(RequestOptions(url = getApiUrl("/monthly-inspection-task/task-no/" + taskId), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
                val respdata = res.data as UTSJSONObject
                if (respdata.getNumber("code") === 200) {
                    val taskInfo = respdata.getJSON("data")
                    console.log("获取到的任务数据:", taskInfo)
                    if (taskInfo != null) {
                        val startStr = taskInfo.getString("inspectionStartTime") ?: ""
                        val endStr = taskInfo.getString("inspectionEndTime") ?: ""
                        val pStartStr = taskInfo.getString("publicityStartTime") ?: ""
                        val pEndStr = taskInfo.getString("publicityEndTime") ?: ""
                        val joinDateRange = fun(a: String, b: String): String {
                            if (a == "" && b == "") {
                                return ""
                            }
                            if (a == "") {
                                return b
                            }
                            if (b == "" || a == b) {
                                return a
                            }
                            return "" + a + " ~ " + b
                        }
                        this.taskInfo = TaskInfo(taskId = taskInfo.getString("id") ?: "", taskNumber = taskInfo.getString("taskNo") ?: "", taskName = taskInfo.getString("taskName") ?: "", inspectionDate = joinDateRange(startStr, endStr), publicityDate = joinDateRange(pStartStr, pEndStr), inspectionStartTime = startStr, inspectionEndTime = endStr)
                        this.autoFillProfession()
                    }
                }
            }
            , fail = fun(error){
                console.error("加载任务信息失败:", error)
                this.taskInfo = TaskInfo(taskId = "", taskNumber = "YJ202401001", taskName = "示例月检任务", inspectionDate = "2024年1月", publicityDate = "2024年1月", inspectionStartTime = "", inspectionEndTime = "")
            }
            ))
        }
         catch (error: Throwable) {
            console.error("加载任务信息失败:", error)
            this.taskInfo = TaskInfo(taskId = "", taskNumber = "YJ202401001", taskName = "示例月检任务", inspectionDate = "2024年1月", publicityDate = "2024年1月", inspectionStartTime = "", inspectionEndTime = "")
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
        console.log("autoFillProfession 已下线，无需自动填充")
    }
    open var loadTypeOptions = ::gen_loadTypeOptions_fn
    open fun gen_loadTypeOptions_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/gjbz-data/options/types" + (if (this.formData.departmentId != "") {
            "?departmentId=" + encodeURIComponent(this.formData.departmentId)
        } else {
            ""
        }
        )), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val respdata = res.data as UTSJSONObject
            if (respdata.getNumber("code") === 200) {
                val data = respdata.getArray("data")
                console.log("获取到的专业类别数据", data)
                if (data != null) {
                    val options: UTSArray<OptionItem__1> = _uA()
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val item = data[i] as String
                            options.push(OptionItem__1(id = (i + 1).toString(10), name = item))
                            i++
                        }
                    }
                    this.typeOptions = options
                    console.log("专业类别选项已加载:", this.typeOptions)
                    this.autoFillProfession()
                }
            } else {
                console.error("加载专业类别失败:", respdata.getString("message"))
                this.loadFallbackTypeOptions()
            }
        }
        , fail = fun(error){
            console.error("加载专业类别失败:", error)
            this.loadFallbackTypeOptions()
        }
        ))
    }
    open var loadFallbackTypeOptions = ::gen_loadFallbackTypeOptions_fn
    open fun gen_loadFallbackTypeOptions_fn() {
        this.typeOptions = _uA(
            OptionItem__1(id = "1", name = "电气专业"),
            OptionItem__1(id = "2", name = "机械专业"),
            OptionItem__1(id = "3", name = "仪表专业"),
            OptionItem__1(id = "4", name = "安全专业"),
            OptionItem__1(id = "5", name = "环保专业")
        )
        console.log("使用备用专业类别数据:", this.typeOptions)
        this.autoFillProfession()
    }
    open var loadGjbzDataByProfession = ::gen_loadGjbzDataByProfession_fn
    open fun gen_loadGjbzDataByProfession_fn() {
        console.log("loadGjbzDataByProfession 已下线")
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
        this.loadModuleOptions((this.typeOptions[index] as OptionItem__1).name)
    }
    open var selectTypeFromFiltered = ::gen_selectTypeFromFiltered_fn
    open fun gen_selectTypeFromFiltered_fn(item: OptionItem__1) {
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
        this.formData.module = (this.moduleOptions[index] as OptionItem__1).name
        this.hideModulePicker()
        this.selectedResponsibilityIndex = -1
        this.selectedIssueIndex = -1
        this.formData.responsibility = ""
        this.formData.issue = ""
        this.responsibilityOptions = _uA()
        this.issueOptions = _uA()
        this.loadResponsibilityOptions((this.moduleOptions[index] as OptionItem__1).name)
    }
    open var selectModuleFromFiltered = ::gen_selectModuleFromFiltered_fn
    open fun gen_selectModuleFromFiltered_fn(item: OptionItem__1) {
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
            uni_showToast(ShowToastOptions(title = "请先选择基本模块", icon = "none"))
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
        this.formData.responsibility = (this.responsibilityOptions[index] as OptionItem__1).name
        this.hideResponsibilityPicker()
        this.selectedIssueIndex = -1
        this.formData.issue = ""
        this.issueOptions = _uA()
        this.loadIssueOptions((this.responsibilityOptions[index] as OptionItem__1).name)
    }
    open var selectResponsibilityFromFiltered = ::gen_selectResponsibilityFromFiltered_fn
    open fun gen_selectResponsibilityFromFiltered_fn(item: OptionItem__1) {
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
            uni_showToast(ShowToastOptions(title = "请先选择责任要素", icon = "none"))
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
        val issueName = (this.issueOptions[index] as OptionItem__1).name
        this.formData.issue = issueName
        this.hideIssuePicker()
        console.log("selectIssue: issueName=", issueName, "mapSize=", this.issueQualityMap.size, "qualityOpts=", this.qualityOptions.length)
        this.autoFillQualityByIssue(issueName)
    }
    open var selectIssueFromFiltered = ::gen_selectIssueFromFiltered_fn
    open fun gen_selectIssueFromFiltered_fn(item: OptionItem__1) {
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
        val picked = this.qualityOptions[index] as OptionItem__1
        this.selectedQualityName = picked.name
        this.formData.quality = this.selectedQualityName
        this.syncDeductionScore()
        if (this.autoFilledQualityId != "" && picked.id !== this.autoFilledQualityId) {
            this.formData.natureModified = 1
        } else if (this.autoFilledQualityId == "" && picked.id != "") {
            this.formData.natureModified = 1
        }
        this.hideQualityPicker()
    }
    open var selectQualityFromFiltered = ::gen_selectQualityFromFiltered_fn
    open fun gen_selectQualityFromFiltered_fn(item: OptionItem__1) {
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
        uni_request<Any>(RequestOptions(url = getApiUrl("/gjbz-data/modules/by-type/" + encodeURIComponent(typeName) + (if (this.formData.departmentId != "") {
            "?departmentId=" + encodeURIComponent(this.formData.departmentId)
        } else {
            ""
        }
        )), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
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
                this.moduleOptions = unique.map(fun(m, idx): OptionItem__1 {
                    return (OptionItem__1(id = (idx + 1).toString(10), name = m))
                }
                )
            }
        }
        , fail = fun(error){
            console.error("按专业加载模块失败:", error)
            this.moduleOptions = _uA<OptionItem__1>()
        }
        ))
    }
    open var loadResponsibilityOptions = ::gen_loadResponsibilityOptions_fn
    open fun gen_loadResponsibilityOptions_fn(moduleId: String) {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/gjbz-data/options/responsibilities/by-module/" + encodeURIComponent(moduleId) + (if (this.formData.departmentId != "") {
            "?departmentId=" + encodeURIComponent(this.formData.departmentId)
        } else {
            ""
        }
        )), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val respdata = res.data as UTSJSONObject
            if (respdata.getNumber("code") === 200) {
                val data = respdata.getArray("data")
                if (data != null) {
                    val options: UTSArray<OptionItem__1> = _uA()
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val item = data[i] as String
                            options.push(OptionItem__1(id = (i + 1).toString(10), name = item))
                            i++
                        }
                    }
                    this.responsibilityOptions = options
                }
            }
        }
        , fail = fun(error){
            console.error("加载责任选项失败:", error)
            this.responsibilityOptions = _uA<OptionItem__1>(OptionItem__1(id = "resp1", name = "责任1"), OptionItem__1(id = "resp2", name = "责任2"), OptionItem__1(id = "resp3", name = "责任3"))
        }
        ))
    }
    open var loadIssueOptions = ::gen_loadIssueOptions_fn
    open fun gen_loadIssueOptions_fn(responsibilityId: String) {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/gjbz-data/options/issues/by-responsibility/" + encodeURIComponent(responsibilityId) + (if (this.formData.departmentId != "") {
            "?departmentId=" + encodeURIComponent(this.formData.departmentId)
        } else {
            ""
        }
        )), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val respdata = res.data as UTSJSONObject
            if (respdata.getNumber("code") === 200) {
                val data = respdata.getArray("data")
                if (data != null) {
                    val options: UTSArray<OptionItem__1> = _uA()
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val item = data[i] as String
                            options.push(OptionItem__1(id = (i + 1).toString(10), name = item))
                            i++
                        }
                    }
                    this.issueOptions = options
                }
            }
        }
        , fail = fun(error){
            console.error("加载问题选项失败:", error)
            this.issueOptions = _uA<OptionItem__1>(OptionItem__1(id = "issue1", name = "问题1"), OptionItem__1(id = "issue2", name = "问题2"), OptionItem__1(id = "issue3", name = "问题3"))
        }
        ))
    }
    open var loadQualityOptions = ::gen_loadQualityOptions_fn
    open fun gen_loadQualityOptions_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/dictionary/type/性质界定"), method = "GET", header = _uO("Authorization" to ("Bearer " + token), "Content-Type" to "application/json"), success = fun(res){
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
                            val label = resolveQualityLabel(dictValue, dictLabel)
                            this.qualityOptions.push(OptionItem__1(id = id, name = label, value = label, code = dictValue, score = qualityScoreOf(label)))
                            i++
                        }
                    }
                }
                console.log("性质界定选项加载完成:", this.qualityOptions)
            } else {
                val message = data.getString("message") ?: "未知错误"
                console.error("获取性质界定数据失败:", message)
                this.qualityOptions = _uA<OptionItem__1>(OptionItem__1(id = "1", name = "观察", value = "观察", score = "1"), OptionItem__1(id = "2", name = "一般", value = "一般", score = "3"), OptionItem__1(id = "3", name = "重要", value = "重要", score = "5"))
            }
        }
        , fail = fun(error){
            console.error("请求性质界定数据失败:", error)
            this.qualityOptions = _uA<OptionItem__1>(OptionItem__1(id = "1", name = "观察", value = "观察", score = "1"), OptionItem__1(id = "2", name = "一般", value = "一般", score = "3"), OptionItem__1(id = "3", name = "重要", value = "重要", score = "5"))
        }
        ))
    }
    open var loadQualityDetails = ::gen_loadQualityDetails_fn
    open fun gen_loadQualityDetails_fn(qualityId: String) {
        console.log("loadQualityDetails 已下线")
    }
    open var loadInspectorOptions = ::gen_loadInspectorOptions_fn
    open fun gen_loadInspectorOptions_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/user/list"), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val respdata = res.data as UTSJSONObject
            if (respdata.getNumber("code") === 200) {
                val records = respdata.getArray("data") as UTSArray<UTSJSONObject>?
                val options: UTSArray<OptionItem__1> = _uA()
                if (records != null) {
                    run {
                        var i: Number = 0
                        while(i < records.length){
                            val u = records[i]
                            val realName = if (u != null) {
                                (u.getString("realName") ?: "")
                            } else {
                                ""
                            }
                            val username = if (u != null) {
                                (u.getString("username") ?: "")
                            } else {
                                ""
                            }
                            val name = if (realName != "") {
                                realName
                            } else {
                                username
                            }
                            if (name != "") {
                                val displayName = if ((username != "" && username != name)) {
                                    (name + "(" + username + ")")
                                } else {
                                    name
                                }
                                options.push(OptionItem__1(id = (i + 1).toString(10), name = name, displayName = displayName))
                            }
                            i++
                        }
                    }
                }
                this.inspectorOptions = options
                if (this.formData.inspector.length === 0) {
                    val userInfoStr = uni_getStorageSync("userInfo") as String
                    if (userInfoStr != null && userInfoStr != "") {
                        try {
                            val userInfo = JSON.parse(userInfoStr) as UTSJSONObject
                            val currentName = userInfo.getString("realName") ?: userInfo.getString("username") ?: ""
                            if (currentName != "" && options.findIndex(fun(o): Boolean {
                                return o.name === currentName
                            }
                            ) !== -1) {
                                val defOpt = options.find(fun(o): Boolean {
                                    return o.name === currentName
                                }
                                )
                                this.formData.inspector = _uA(
                                    if (defOpt != null) {
                                        (defOpt.displayName ?: defOpt.name)
                                    } else {
                                        currentName
                                    }
                                )
                            }
                        }
                         catch (e: Throwable) {
                            console.error("解析当前用户失败:", e)
                        }
                    }
                }
            }
        }
        , fail = fun(error){
            console.error("加载检查人列表失败:", error)
        }
        ))
    }
    open var showInspectorPicker = ::gen_showInspectorPicker_fn
    open fun gen_showInspectorPicker_fn() {
        if (this.inspectorOptions.length === 0) {
            this.loadInspectorOptions()
        }
        this.showInspectorPopup = true
    }
    open var hideInspectorPicker = ::gen_hideInspectorPicker_fn
    open fun gen_hideInspectorPicker_fn() {
        this.showInspectorPopup = false
        this.inspectorSearchKeyword = ""
    }
    open var toggleInspectorFromFiltered = ::gen_toggleInspectorFromFiltered_fn
    open fun gen_toggleInspectorFromFiltered_fn(item: OptionItem__1) {
        val kVal = item.displayName ?: item.name
        val idx = this.formData.inspector.indexOf(kVal)
        if (idx === -1) {
            this.formData.inspector.push(kVal)
        } else {
            this.formData.inspector.splice(idx, 1)
        }
    }
    open var confirmInspectorPicker = ::gen_confirmInspectorPicker_fn
    open fun gen_confirmInspectorPicker_fn() {
        this.hideInspectorPicker()
    }
    open var showResponsiblePersonPicker = ::gen_showResponsiblePersonPicker_fn
    open fun gen_showResponsiblePersonPicker_fn() {
        if (this.inspectorOptions.length === 0) {
            this.loadInspectorOptions()
        }
        this.showResponsiblePersonPopup = true
    }
    open var hideResponsiblePersonPicker = ::gen_hideResponsiblePersonPicker_fn
    open fun gen_hideResponsiblePersonPicker_fn() {
        this.showResponsiblePersonPopup = false
        this.inspectorSearchKeyword = ""
    }
    open var toggleResponsiblePersonFromFiltered = ::gen_toggleResponsiblePersonFromFiltered_fn
    open fun gen_toggleResponsiblePersonFromFiltered_fn(item: OptionItem__1) {
        val kVal = item.displayName ?: item.name
        val idx = this.formData.responsiblePerson.indexOf(kVal)
        if (idx === -1) {
            this.formData.responsiblePerson.push(kVal)
        } else {
            this.formData.responsiblePerson.splice(idx, 1)
        }
    }
    open var confirmResponsiblePersonPicker = ::gen_confirmResponsiblePersonPicker_fn
    open fun gen_confirmResponsiblePersonPicker_fn() {
        this.hideResponsiblePersonPicker()
    }
    open var loadDeptOptions = ::gen_loadDeptOptions_fn
    open fun gen_loadDeptOptions_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/dept/list?deptType=0"), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val respdata = res.data as UTSJSONObject
            if (respdata.getNumber("code") === 200) {
                val list = respdata.getArray("data") as UTSArray<UTSJSONObject>?
                val options: UTSArray<OptionItem__1> = _uA()
                if (list != null) {
                    run {
                        var i: Number = 0
                        while(i < list.length){
                            val d = list[i]
                            val id = if (d != null) {
                                (d.getString("id") ?: "")
                            } else {
                                ""
                            }
                            val name = if (d != null) {
                                (d.getString("deptName") ?: "")
                            } else {
                                ""
                            }
                            if (id != "" && name != "") {
                                options.push(OptionItem__1(id = id, name = name))
                            }
                            i++
                        }
                    }
                }
                this.deptOptions = options
                if (this.formData.departmentId != "") {
                    val idx = options.findIndex(fun(o): Boolean {
                        return o.id === this.formData.departmentId
                    })
                    if (idx !== -1) {
                        this.selectedDeptIndex = idx
                    }
                } else {
                    this.selectedDeptIndex = -1
                }
            }
        }
        , fail = fun(error){
            console.error("加载部门字典失败:", error)
        }
        ))
    }
    open var showInspectedUnitPicker = ::gen_showInspectedUnitPicker_fn
    open fun gen_showInspectedUnitPicker_fn() {
        if (this.deptOptions.length === 0) {
            this.loadDeptOptions()
        }
        this.showInspectedUnitPopup = true
    }
    open var hideInspectedUnitPicker = ::gen_hideInspectedUnitPicker_fn
    open fun gen_hideInspectedUnitPicker_fn() {
        this.showInspectedUnitPopup = false
        this.deptSearchKeyword = ""
    }
    open var selectInspectedUnitFromFiltered = ::gen_selectInspectedUnitFromFiltered_fn
    open fun gen_selectInspectedUnitFromFiltered_fn(item: OptionItem__1) {
        val newIndex = this.deptOptions.findIndex(fun(o): Boolean {
            return o.id === item.id
        }
        )
        val deptChanged = this.formData.departmentId !== item.id
        this.selectedDeptIndex = newIndex
        this.formData.departmentId = item.id
        this.hideInspectedUnitPicker()
        if (deptChanged) {
            this.selectedTypeIndex = -1
            this.selectedModuleIndex = -1
            this.selectedResponsibilityIndex = -1
            this.selectedIssueIndex = -1
            this.selectedQualityIndex = -1
            this.selectedQualityName = ""
            this.deductionScoreDisplay = ""
            this.formData.problemType = ""
            this.formData.module = ""
            this.formData.responsibility = ""
            this.formData.issue = ""
            this.formData.quality = ""
            this.formData.natureModified = 0
            this.autoFilledQualityId = ""
            this.typeOptions = _uA<OptionItem__1>()
            this.moduleOptions = _uA<OptionItem__1>()
            this.responsibilityOptions = _uA<OptionItem__1>()
            this.issueOptions = _uA<OptionItem__1>()
            this.loadTypeOptions()
            this.loadIssueQualityMap()
        }
    }
    open var showResponsibleUnitPicker = ::gen_showResponsibleUnitPicker_fn
    open fun gen_showResponsibleUnitPicker_fn() {
        if (this.deptOptions.length === 0) {
            this.loadDeptOptions()
        }
        this.showResponsibleUnitPopup = true
    }
    open var hideResponsibleUnitPicker = ::gen_hideResponsibleUnitPicker_fn
    open fun gen_hideResponsibleUnitPicker_fn() {
        this.showResponsibleUnitPopup = false
        this.responsibleUnitSearchKeyword = ""
    }
    open var toggleResponsibleUnitFromFiltered = ::gen_toggleResponsibleUnitFromFiltered_fn
    open fun gen_toggleResponsibleUnitFromFiltered_fn(item: OptionItem__1) {
        val idx = this.formData.responsibleUnit.indexOf(item.name)
        if (idx === -1) {
            this.formData.responsibleUnit.push(item.name)
        } else {
            this.formData.responsibleUnit.splice(idx, 1)
        }
    }
    open var confirmResponsibleUnitPicker = ::gen_confirmResponsibleUnitPicker_fn
    open fun gen_confirmResponsibleUnitPicker_fn() {
        this.hideResponsibleUnitPicker()
    }
    open var loadIssueQualityMap = ::gen_loadIssueQualityMap_fn
    open fun gen_loadIssueQualityMap_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/gjbz-data/options/issues-with-quality" + (if (this.formData.departmentId != "") {
            "?departmentId=" + encodeURIComponent(this.formData.departmentId)
        } else {
            ""
        }
        )), method = "GET", header = _uO("Authorization" to ("Bearer " + token)), success = fun(res){
            val respdata = res.data as UTSJSONObject
            if (respdata.getNumber("code") === 200) {
                val list = respdata.getArray("data")
                val map = Map<String, String>() as IssueQualityMap
                val keys: UTSArray<String> = _uA()
                if (list != null) {
                    run {
                        var i: Number = 0
                        while(i < list.length){
                            val item = list[i] as UTSJSONObject
                            val issueName = item.getString("issue") ?: ""
                            val qualityName = item.getString("quality") ?: ""
                            if (issueName != "" && qualityName != "" && map.get(issueName) == null) {
                                map.set(issueName, qualityName)
                                keys.push(issueName)
                            }
                            i++
                        }
                    }
                }
                this.issueQualityMap = map
                this.issueQualityKeys = keys
                console.log("检查内容->性质界定映射已加载, 条数:", map.size)
            }
        }
        , fail = fun(error){
            console.error("加载检查内容映射失败:", error)
        }
        ))
    }
    open var autoFillQualityByIssue = ::gen_autoFillQualityByIssue_fn
    open fun gen_autoFillQualityByIssue_fn(issueName: String) {
        if (issueName == "") {
            return
        }
        val trimmedKey = stripOuterWhitespace(issueName)
        var qualityName = this.issueQualityMap.get(trimmedKey)
        if (qualityName == null) {
            run {
                var i: Number = 0
                while(i < this.issueQualityKeys.length){
                    val k = this.issueQualityKeys[i]
                    if (stripOuterWhitespace(k) == trimmedKey) {
                        qualityName = this.issueQualityMap.get(k)
                        break
                    }
                    i++
                }
            }
        }
        if (qualityName == null) {
            val lk = trimmedKey.toLowerCase()
            run {
                var i: Number = 0
                while(i < this.issueQualityKeys.length){
                    val k = this.issueQualityKeys[i]
                    if (stripOuterWhitespace(k).toLowerCase() == lk) {
                        qualityName = this.issueQualityMap.get(k)
                        break
                    }
                    i++
                }
            }
        }
        console.log("autoFillQualityByIssue:", issueName, "->", qualityName, "mapSize:", this.issueQualityMap.size, "qualityOpts:", this.qualityOptions.length)
        if (qualityName == null || qualityName == "") {
            console.log("该检查内容未配置性质界定:", issueName)
            return
        }
        if (this.qualityOptions.length === 0) {
            this.loadQualityOptionsAndApply(qualityName)
            return
        }
        this.applyQualityByName(qualityName)
    }
    open var loadQualityOptionsAndApply = ::gen_loadQualityOptionsAndApply_fn
    open fun gen_loadQualityOptionsAndApply_fn(qualityName: String) {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/dictionary/type/性质界定"), method = "GET", header = _uO("Authorization" to ("Bearer " + token), "Content-Type" to "application/json"), success = fun(res){
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
                            val label = resolveQualityLabel(dictValue, dictLabel)
                            this.qualityOptions.push(OptionItem__1(id = id, name = label, value = label, code = dictValue, score = qualityScoreOf(label)))
                            i++
                        }
                    }
                    this.applyQualityByName(qualityName)
                }
            } else {
                console.error("加载性质界定失败:", data.getString("message") ?: "未知错误")
            }
        }
        , fail = fun(error){
            console.error("请求性质界定失败:", error)
        }
        ))
    }
    open var applyQualityByName = ::gen_applyQualityByName_fn
    open fun gen_applyQualityByName_fn(qualityName: String) {
        if (qualityName == "") {
            return
        }
        val lq = stripOuterWhitespace(qualityName).toLowerCase()
        var idx: Number = -1
        run {
            var i: Number = 0
            while(i < this.qualityOptions.length){
                val o = this.qualityOptions[i] as OptionItem__1
                val nameKey = stripOuterWhitespace(o.name).toLowerCase()
                val valueKey = stripOuterWhitespace(o.value ?: "").toLowerCase()
                val idKey = stripOuterWhitespace(o.id).toLowerCase()
                if (nameKey == lq || valueKey == lq || idKey == lq) {
                    idx = i
                    break
                }
                i++
            }
        }
        if (idx === -1) {
            val dump = this.qualityOptions.map(fun(o: OptionItem__1): String {
                return "" + o.id + "|" + o.name + "|" + (o.value ?: "")
            }
            ).join(",")
            console.log("性质界定未匹配:", qualityName, "可用选项:", dump)
            return
        }
        this.selectedQualityIndex = idx
        this.selectedQualityName = (this.qualityOptions[idx] as OptionItem__1).name
        this.formData.quality = this.selectedQualityName
        this.syncDeductionScore()
        this.autoFilledQualityId = (this.qualityOptions[idx] as OptionItem__1).id
        this.formData.natureModified = 0
        uni_showToast(ShowToastOptions(title = "已自动选中：" + qualityName, icon = "none", duration = 1500))
    }
    open var autoFillIssueDetails = ::gen_autoFillIssueDetails_fn
    open fun gen_autoFillIssueDetails_fn(issueId: String, issueName: String) {
        console.log("autoFillIssueDetails 已下线，改用 autoFillQualityByIssue")
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
        console.log("=== 移动端图片上传调试信息 ===")
        console.log("选择的图片路径:", imagePath)
        uni_showLoading(ShowLoadingOptions(title = "上传中..."))
        uni_uploadFile(UploadFileOptions(url = getApiUrl("/upload/image"), filePath = imagePath, name = "file", header = _uO("Authorization" to if (uni_getStorageSync("token") != null) {
            "Bearer " + uni_getStorageSync("token") as String
        } else {
            ""
        }
        ), success = fun(uploadRes){
            uni_hideLoading(null)
            console.log("上传响应状态码:", uploadRes.statusCode)
            console.log("上传响应数据:", uploadRes.data)
            try {
                val result = JSON.parse(uploadRes.data) as UTSJSONObject
                console.log("解析后的响应数据:", result)
                val code = result.getNumber("code")
                if (code === 200) {
                    val data = result.getJSON("data")
                    if (data != null) {
                        val url = data.getString("filePath") ?: data.getString("url") ?: ""
                        console.log("服务器返回的图片URL:", url)
                        if (url != null && url != "") {
                            this.formData.photos.push(url)
                            console.log("当前photos数组:", this.formData.photos)
                        }
                    }
                } else {
                    var message = result.getString("message")
                    if (message == null) {
                        message = "上传失败"
                    }
                    console.error("上传失败，服务器响应:", result)
                    uni_showToast(ShowToastOptions(title = message, icon = "none"))
                }
            }
             catch (e: Throwable) {
                console.error("解析响应数据失败:", e)
                console.error("原始响应数据:", uploadRes.data)
                uni_showToast(ShowToastOptions(title = "上传失败", icon = "none"))
            }
            console.log("=== 移动端图片上传调试信息结束 ===")
        }
        , fail = fun(error){
            uni_hideLoading(null)
            console.error("图片上传失败:", error)
            uni_showToast(ShowToastOptions(title = "上传失败", icon = "none"))
        }
        ))
    }
    open var deletePhoto = ::gen_deletePhoto_fn
    open fun gen_deletePhoto_fn(index: Number) {
        this.formData.photos.splice(index, 1)
    }
    open var saveRecord = ::gen_saveRecord_fn
    open fun gen_saveRecord_fn() {
        if (this.selectedTypeIndex === -1) {
            uni_showToast(ShowToastOptions(title = "请选择专业类别", icon = "none"))
            return
        }
        if (this.selectedModuleIndex === -1) {
            uni_showToast(ShowToastOptions(title = "请选择基本模块", icon = "none"))
            return
        }
        if (this.selectedResponsibilityIndex === -1) {
            uni_showToast(ShowToastOptions(title = "请选择责任要素", icon = "none"))
            return
        }
        if (this.selectedIssueIndex === -1) {
            uni_showToast(ShowToastOptions(title = "请选择检查内容", icon = "none"))
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
        if (this.formData.inspector.length === 0) {
            uni_showToast(ShowToastOptions(title = "请选择检查人", icon = "none"))
            return
        }
        if (this.formData.departmentId == "") {
            uni_showToast(ShowToastOptions(title = "请选择受检单位", icon = "none"))
            return
        }
        if (this.formData.checkUnitId == "") {
            if (this.currentUser.deptId != "") {
                this.formData.checkUnitId = this.currentUser.deptId
                this.formData.checkUnitName = this.currentUser.deptName
            } else {
                uni_showToast(ShowToastOptions(title = "检查单位未设置,请确认当前用户所属部门", icon = "none"))
                return
            }
        }
        if (!this.isEditMode) {
            if (this.taskInfo.inspectionStartTime == "" || this.taskInfo.inspectionEndTime == "") {
                uni_showToast(ShowToastOptions(title = "当前任务不在月检录入期内，无法新增记录", icon = "none"))
                return
            }
            val parseLocalDate = fun(s: String): Date {
                return Date(s.replace(" ", "T"))
            }
            val start = parseLocalDate(this.taskInfo.inspectionStartTime)
            val end = parseLocalDate(this.taskInfo.inspectionEndTime)
            val now = Date()
            if (now < start || now > end) {
                uni_showToast(ShowToastOptions(title = "当前任务不在月检录入期内，无法新增记录", icon = "none"))
                return
            }
        }
        try {
            uni_showLoading(ShowLoadingOptions(title = "保存中..."))
            val userInfoStr = uni_getStorageSync("userInfo") as String
            var userId = ""
            if (userInfoStr != null && userInfoStr != "") {
                try {
                    val userInfo = JSON.parse(userInfoStr) as UTSJSONObject
                    userId = userInfo.getString("id") ?: ""
                }
                 catch (e: Throwable) {
                    console.error("解析用户信息失败:", e)
                }
            }
            if (userId == "") {
                uni_hideLoading(null)
                uni_showToast(ShowToastOptions(title = "获取用户信息失败，请重新登录", icon = "none"))
                return
            }
            val selectedType = if (this.selectedTypeIndex !== -1 && this.typeOptions[this.selectedTypeIndex] != null) {
                this.typeOptions[this.selectedTypeIndex] as OptionItem__1
            } else {
                null
            }
            val selectedModule = if (this.selectedModuleIndex !== -1 && this.moduleOptions[this.selectedModuleIndex] != null) {
                this.moduleOptions[this.selectedModuleIndex] as OptionItem__1
            } else {
                null
            }
            val selectedResp = if (this.selectedResponsibilityIndex !== -1 && this.responsibilityOptions[this.selectedResponsibilityIndex] != null) {
                this.responsibilityOptions[this.selectedResponsibilityIndex] as OptionItem__1
            } else {
                null
            }
            val selectedIssue = if (this.selectedIssueIndex !== -1 && this.issueOptions[this.selectedIssueIndex] != null) {
                this.issueOptions[this.selectedIssueIndex] as OptionItem__1
            } else {
                null
            }
            val selectedQuality = if (this.selectedQualityIndex !== -1 && this.qualityOptions[this.selectedQualityIndex] != null) {
                this.qualityOptions[this.selectedQualityIndex] as OptionItem__1
            } else {
                null
            }
            val saveData: UTSJSONObject = _uO("taskId" to this.taskInfo.taskId, "professionalTypeId" to if (selectedType != null) {
                selectedType.name
            } else {
                ""
            }
            , "professionalCategory" to if (selectedType != null) {
                selectedType.name
            } else {
                ""
            }
            , "moduleId" to if (selectedModule != null) {
                selectedModule.name
            } else {
                ""
            }
            , "module" to if (selectedModule != null) {
                selectedModule.name
            } else {
                ""
            }
            , "responsibilityId" to if (selectedResp != null) {
                selectedResp.name
            } else {
                ""
            }
            , "responsibility" to if (selectedResp != null) {
                selectedResp.name
            } else {
                ""
            }
            , "issueId" to if (selectedIssue != null) {
                selectedIssue.name
            } else {
                ""
            }
            , "issue" to if (selectedIssue != null) {
                selectedIssue.name
            } else {
                ""
            }
            , "qualityId" to if (selectedQuality != null) {
                selectedQuality.id
            } else {
                ""
            }
            , "qualityDetermination" to if (selectedQuality != null) {
                selectedQuality.name
            } else {
                ""
            }
            , "problemDescription" to this.formData.problemDescription, "location" to this.formData.location, "rectificationDate" to this.formData.rectificationDate, "inspector" to this.formData.inspector.join("、"), "photos" to this.formData.photos, "responsibleUnit" to this.formData.responsibleUnit.join("、"), "responsiblePerson" to this.formData.responsiblePerson.join("、"), "departmentId" to this.formData.departmentId, "checkUnitId" to this.formData.checkUnitId, "checkUnitName" to this.formData.checkUnitName, "natureModified" to this.formData.natureModified)
            val reqUrl = getApiUrl("/monthly-inspection-record")
            val reqMethod = if (this.isEditMode) {
                "PUT"
            } else {
                "POST"
            }
            uni_request<Any>(RequestOptions(url = reqUrl, method = reqMethod, header = _uO("Content-Type" to "application/json", "Authorization" to if (uni_getStorageSync("token") != null) {
                "Bearer " + uni_getStorageSync("token") as String
            } else {
                ""
            }
            , "userId" to userId), data = UTSJSONObject.assign(_uO(), saveData, _uO("id" to if (this.isEditMode) {
                this.recordId
            } else {
                ""
            }
            , "photos" to this.formData.photos.join(","))), success = fun(res){
                uni_hideLoading(null)
                val respdate = res.data as UTSJSONObject
                console.log("保存响应 status=", res.statusCode, "body=", JSON.stringify(res.data))
                if (respdate.getNumber("code") == 200) {
                    uni_showToast(ShowToastOptions(title = if (this.isEditMode) {
                        "修改成功"
                    } else {
                        "保存成功"
                    }, icon = "success"))
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
                uni_hideLoading(null)
                console.error("保存失败:", error)
                uni_showToast(ShowToastOptions(title = "保存失败", icon = "none"))
            }
            ))
        }
         catch (error: Throwable) {
            uni_hideLoading(null)
            console.error("保存失败:", error)
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
        console.log("初始化日期数组:", _uO("selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "selectedDay" to this.selectedDay, "yearIndex" to yearIndex, "monthIndex" to monthIndex, "dayIndex" to dayIndex, "pickerValue" to this.pickerValue, "daysArray" to this.days))
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
            console.log("日期不存在，调整为月末:", this.selectedDay)
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
        console.log("显示日期选择器:", _uO("selectedDate" to this.formData.rectificationDate, "selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "selectedDay" to this.selectedDay, "yearIndex" to yearIndex, "monthIndex" to monthIndex, "dayIndex" to dayIndex, "pickerValue" to this.pickerValue))
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
        console.log("picker原始变化事件:", _uO("value" to value, "oldSelectedYear" to this.selectedYear, "oldSelectedMonth" to this.selectedMonth, "oldSelectedDay" to this.selectedDay, "oldDaysLength" to oldDaysLength))
        this.selectedYear = this.years[value[0]]
        this.selectedMonth = value[1] + 1
        this.updateDays()
        var newDayIndex = value[2]
        if (newDayIndex >= this.days.length) {
            newDayIndex = this.days.length - 1
            console.log("日期超出范围，调整为月末:", this.days[newDayIndex])
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
                console.log("延迟更新pickerValue:", _uO("newPickerValue" to this.pickerValue, "selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "selectedDay" to this.selectedDay))
            }, 50)
        } else {
            this.pickerValue = _uA(
                value[0],
                value[1],
                newDayIndex
            )
            console.log("直接更新pickerValue:", _uO("newPickerValue" to this.pickerValue, "selectedYear" to this.selectedYear, "selectedMonth" to this.selectedMonth, "selectedDay" to this.selectedDay))
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
        console.log("确认日期选择:", _uO("pickerValue" to this.pickerValue, "yearsArray" to this.years, "monthsArray" to this.months, "daysArray" to this.days, "selectedYear" to selectedYear, "selectedMonth" to selectedMonth, "selectedDay" to selectedDay, "formattedDate" to newDate))
        this.formData.rectificationDate = newDate
        this.hideDatePicker()
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("container" to _pS(_uM("minHeight" to "1500rpx", "backgroundImage" to "linear-gradient(180deg, #eff6ff 0%, #dbeafe 100%)", "backgroundColor" to "rgba(0,0,0,0)", "position" to "relative", "zIndex" to 1, "paddingBottom" to "180rpx")), "header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "32rpx", "paddingRight" to "40rpx", "paddingBottom" to "32rpx", "paddingLeft" to "40rpx", "backgroundImage" to "linear-gradient(135deg, #0a2540 0%, #1e40af 55%, #0ea5e9 100%)", "backgroundColor" to "rgba(0,0,0,0)", "borderBottomWidth" to "medium", "borderBottomStyle" to "none", "borderBottomColor" to "#000000", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.25)")), "header-left" to _pS(_uM("display" to "flex", "alignItems" to "center")), "back-icon" to _pS(_uM("fontSize" to "40rpx", "color" to "#ffffff")), "back-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#ffffff")), "header-title" to _pS(_uM("fontSize" to "34rpx", "fontWeight" to 700, "color" to "#ffffff", "letterSpacing" to "1rpx")), "header-right" to _pS(_uM("display" to "flex", "alignItems" to "center")), "save-btn" to _pS(_uM("fontSize" to "28rpx", "color" to "#ffffff", "fontWeight" to 500)), "task-info" to _pS(_uM("backgroundImage" to "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)", "backgroundColor" to "rgba(0,0,0,0)", "marginTop" to "20rpx", "marginRight" to "30rpx", "marginBottom" to "20rpx", "marginLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "32rpx", "paddingRight" to "32rpx", "paddingBottom" to "32rpx", "paddingLeft" to "32rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "content::before" to "''", "height::before" to "4rpx", "backgroundImage::before" to "linear-gradient(90deg, #1e40af, #0ea5e9)", "backgroundColor::before" to "rgba(0,0,0,0)", "marginTop::before" to "-16rpx", "marginRight::before" to "-32rpx", "marginBottom::before" to "24rpx", "marginLeft::before" to "-32rpx", "borderTopLeftRadius::before" to "4rpx", "borderTopRightRadius::before" to "4rpx", "borderBottomRightRadius::before" to "4rpx", "borderBottomLeftRadius::before" to "4rpx")), "info-item" to _pS(_uM("display" to "flex", "alignItems" to "flex-start", "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0, "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#f1f5f9", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "info-label" to _pS(_uM("fontSize" to "26rpx", "color" to "#64748b", "width" to "160rpx", "flexShrink" to 0, "textAlign" to "left", "marginRight" to "20rpx")), "info-value" to _pS(_uM("fontSize" to "28rpx", "color" to "#0f172a", "fontWeight" to 600, "flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "textAlign" to "left")), "snapshot-panel" to _pS(_uM("backgroundImage" to "linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%)", "backgroundColor" to "rgba(0,0,0,0)", "marginTop" to "20rpx", "marginRight" to "30rpx", "marginBottom" to "20rpx", "marginLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "28rpx", "paddingRight" to "32rpx", "paddingBottom" to "28rpx", "paddingLeft" to "32rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)", "borderTopWidth" to "4rpx", "borderTopStyle" to "solid", "borderTopColor" to "#1e40af")), "snapshot-title-row" to _pS(_uM("display" to "flex", "alignItems" to "center", "justifyContent" to "space-between", "marginBottom" to "16rpx")), "snapshot-title" to _pS(_uM("fontSize" to "30rpx", "fontWeight" to 600, "color" to "#1e40af", "paddingLeft" to "16rpx", "borderLeftWidth" to "6rpx", "borderLeftStyle" to "solid", "borderLeftColor" to "#2563eb")), "snapshot-edit-badge" to _pS(_uM("fontSize" to "22rpx", "color" to "#ffffff", "backgroundImage" to "none", "backgroundColor" to "#1e40af", "paddingTop" to "4rpx", "paddingRight" to "16rpx", "paddingBottom" to "4rpx", "paddingLeft" to "16rpx", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "fontWeight" to 500)), "snapshot-content" to _pS(_uM("fontSize" to "24rpx", "color" to "#1e3a8a", "lineHeight" to 1.6, "wordBreak" to "break-all", "whiteSpace" to "pre-wrap", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#bfdbfe", "borderRightColor" to "#bfdbfe", "borderBottomColor" to "#bfdbfe", "borderLeftColor" to "#bfdbfe")), "form-section" to _pS(_uM("backgroundImage" to "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)", "backgroundColor" to "rgba(0,0,0,0)", "marginTop" to "20rpx", "marginRight" to "30rpx", "marginBottom" to "20rpx", "marginLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "32rpx", "paddingRight" to "32rpx", "paddingBottom" to "32rpx", "paddingLeft" to "32rpx", "boxShadow" to "0 8rpx 24rpx rgba(30, 64, 175, 0.08)")), "section-title" to _pS(_uM("display" to "flex", "alignItems" to "center", "marginBottom" to "32rpx", "paddingBottom" to "20rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#e2e8f0")), "title-text" to _pS(_uM("fontSize" to "32rpx", "fontWeight" to 700, "color" to "#0f172a", "letterSpacing" to "1rpx")), "required-mark" to _pS(_uM("fontSize" to "30rpx", "color" to "#dc2626", "marginLeft" to "6rpx")), "form-item" to _pS(_uM("marginBottom" to "32rpx", "marginBottom:last-child" to 0)), "form-item-inline" to _pS(_uM("marginBottom" to "32rpx", "display" to "flex", "flexDirection" to "column", "gap" to "12rpx", "paddingTop" to 0, "paddingRight" to "30rpx", "paddingBottom" to 0, "paddingLeft" to "30rpx")), "form-label" to _pS(_uM("fontSize" to "28rpx", "color" to "#0f172a", "marginBottom" to "12rpx")), "form-label-inline" to _pS(_uM("fontSize" to "28rpx", "color" to "#0f172a", "fontWeight" to 600, "marginBottom" to 0)), "picker-wrapper" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "20rpx", "paddingRight" to "24rpx", "paddingBottom" to "20rpx", "paddingLeft" to "24rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e2e8f0", "borderRightColor" to "#e2e8f0", "borderBottomColor" to "#e2e8f0", "borderLeftColor" to "#e2e8f0", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "backgroundColor" to "#ffffff")), "picker-wrapper-inline" to _uM("" to _uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "20rpx", "paddingRight" to "24rpx", "paddingBottom" to "20rpx", "paddingLeft" to "24rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e2e8f0", "borderRightColor" to "#e2e8f0", "borderBottomColor" to "#e2e8f0", "borderLeftColor" to "#e2e8f0", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "backgroundColor" to "#ffffff", "width" to "100%", "transitionProperty" to "all", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "cursor" to "pointer", "borderTopColor:hover" to "#1e40af", "borderRightColor:hover" to "#1e40af", "borderBottomColor:hover" to "#1e40af", "borderLeftColor:hover" to "#1e40af", "boxShadow:hover" to "0 0 0 4rpx rgba(30, 64, 175, 0.08)", "transform:active" to "scale(0.98)"), ".disabled" to _uM("backgroundColor" to "#f1f5f9", "color" to "#94a3b8", "cursor" to "not-allowed", "opacity" to 0.6), ".disabled:hover" to _uM("borderTopColor" to "#e2e8f0", "borderRightColor" to "#e2e8f0", "borderBottomColor" to "#e2e8f0", "borderLeftColor" to "#e2e8f0", "boxShadow" to "none")), "text-input-inline" to _pS(_uM("paddingTop" to "20rpx", "paddingRight" to "24rpx", "paddingBottom" to "20rpx", "paddingLeft" to "24rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e2e8f0", "borderRightColor" to "#e2e8f0", "borderBottomColor" to "#e2e8f0", "borderLeftColor" to "#e2e8f0", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "fontSize" to "28rpx", "color" to "#0f172a", "backgroundColor" to "#ffffff", "flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%")), "bottom-save-container" to _pS(_uM("position" to "fixed", "bottom" to 0, "left" to 0, "right" to 0, "paddingTop" to "24rpx", "paddingRight" to "40rpx", "paddingBottom" to "56rpx", "paddingLeft" to "40rpx", "backgroundImage" to "linear-gradient(to top, rgba(255, 255, 255, 0.98) 0%, rgba(255, 255, 255, 0.85) 100%)", "backgroundColor" to "rgba(0,0,0,0)", "backdropFilter" to "blur(20rpx)", "borderTopWidth" to "2rpx", "borderTopStyle" to "solid", "borderTopColor" to "rgba(30,64,175,0.08)", "zIndex" to 999)), "bottom-save-btn" to _pS(_uM("width" to "100%", "height" to "96rpx", "backgroundColor" to "#1e40af", "borderTopLeftRadius" to "48rpx", "borderTopRightRadius" to "48rpx", "borderBottomRightRadius" to "48rpx", "borderBottomLeftRadius" to "48rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "boxShadow" to "0 12rpx 32rpx rgba(37, 99, 235, 0.4)", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "cubic-bezier(0.4,0,0.2,1)", "position" to "relative", "overflow" to "hidden", "transform:active" to "translateY(2rpx) scale(0.98)", "boxShadow:active" to "0 8rpx 24rpx rgba(37, 99, 235, 0.3)")), "bottom-save-text" to _pS(_uM("color" to "#ffffff", "fontSize" to "34rpx", "fontWeight" to 700, "letterSpacing" to "6rpx")), "picker-text" to _uM("" to _uM("fontSize" to "28rpx", "color" to "#0f172a"), ".placeholder" to _uM("color" to "#94a3b8")), "picker-arrow" to _pS(_uM("fontSize" to "28rpx", "color" to "#cbd5e1")), "text-input" to _pS(_uM("paddingTop" to "20rpx", "paddingRight" to "24rpx", "paddingBottom" to "20rpx", "paddingLeft" to "24rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e2e8f0", "borderRightColor" to "#e2e8f0", "borderBottomColor" to "#e2e8f0", "borderLeftColor" to "#e2e8f0", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "fontSize" to "28rpx", "color" to "#0f172a", "backgroundColor" to "#ffffff")), "textarea-input" to _pS(_uM("paddingTop" to "20rpx", "paddingRight" to "24rpx", "paddingBottom" to "20rpx", "paddingLeft" to "24rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e2e8f0", "borderRightColor" to "#e2e8f0", "borderBottomColor" to "#e2e8f0", "borderLeftColor" to "#e2e8f0", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "fontSize" to "28rpx", "color" to "#0f172a", "backgroundColor" to "#ffffff", "minHeight" to "160rpx", "resize" to "none")), "char-count" to _pS(_uM("fontSize" to "22rpx", "color" to "#94a3b8", "textAlign" to "right", "marginTop" to "8rpx")), "photo-upload" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "marginBottom" to 5)), "photo-item" to _uM(".photo-upload " to _uM("marginTop" to 5, "marginRight" to 5, "marginBottom" to 5, "marginLeft" to 5), "" to _uM("position" to "relative", "width" to "160rpx", "height" to "160rpx")), "photo-add" to _uM(".photo-upload " to _uM("marginTop" to 5, "marginRight" to 5, "marginBottom" to 5, "marginLeft" to 5), "" to _uM("width" to "160rpx", "height" to "160rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#93c5fd", "borderRightColor" to "#93c5fd", "borderBottomColor" to "#93c5fd", "borderLeftColor" to "#93c5fd", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "#eff6ff")), "photo-preview" to _pS(_uM("width" to "100%", "height" to "100%", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx")), "photo-delete" to _pS(_uM("position" to "absolute", "top" to "-10rpx", "right" to "-10rpx", "width" to "40rpx", "height" to "40rpx", "backgroundColor" to "#dc2626", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "delete-icon" to _pS(_uM("fontSize" to "24rpx", "color" to "#ffffff", "fontWeight" to "bold")), "add-icon" to _pS(_uM("fontSize" to "48rpx", "color" to "#1e40af", "marginBottom" to "8rpx")), "add-text" to _pS(_uM("fontSize" to "22rpx", "color" to "#1e40af")), "photo-tip" to _pS(_uM("fontSize" to "22rpx", "color" to "#94a3b8", "marginTop" to "8rpx")), "popup-overlay" to _pS(_uM("!position" to "fixed", "!top" to 0, "!left" to 0, "!width" to "100%", "!height" to "100%", "backgroundColor" to "rgba(10,37,64,0.55)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "!zIndex" to 9999, "animation" to "fadeIn 0.3s ease", "pointerEvents" to "auto")), "picker-modal" to _pS(_uM("!position" to "fixed", "!top" to 0, "!left" to 0, "!width" to "100%", "!height" to "100%", "backgroundColor" to "rgba(10,37,64,0.55)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "!zIndex" to 9999, "pointerEvents" to "auto")), "popup-content" to _pS(_uM("!width" to "90%", "maxWidth" to "600rpx", "maxHeight" to "900rpx", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "overflow" to "hidden", "boxShadow" to "0 24rpx 64rpx rgba(10, 37, 64, 0.35)", "position" to "relative", "zIndex" to 10000)), "picker-content" to _pS(_uM("!width" to "90%", "maxWidth" to "600rpx", "maxHeight" to "900rpx", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "24rpx", "borderTopRightRadius" to "24rpx", "borderBottomRightRadius" to "24rpx", "borderBottomLeftRadius" to "24rpx", "overflow" to "hidden", "boxShadow" to "0 24rpx 64rpx rgba(10, 37, 64, 0.35)", "position" to "relative", "zIndex" to 10000)), "popup-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "28rpx", "paddingRight" to "32rpx", "paddingBottom" to "28rpx", "paddingLeft" to "32rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#e2e8f0", "backgroundImage" to "linear-gradient(135deg, #0a2540 0%, #1e40af 100%)", "backgroundColor" to "rgba(0,0,0,0)", "position" to "relative", "color" to "#ffffff")), "picker-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "28rpx", "paddingRight" to "32rpx", "paddingBottom" to "28rpx", "paddingLeft" to "32rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#e2e8f0", "backgroundImage" to "linear-gradient(135deg, #0a2540 0%, #1e40af 100%)", "backgroundColor" to "rgba(0,0,0,0)", "position" to "relative", "color" to "#ffffff", "content::after" to "''", "position::after" to "absolute", "bottom::after" to 0, "left::after" to "50%", "transform::after" to "translateX(-50%)", "width::after" to "60rpx", "height::after" to "6rpx", "backgroundColor::after" to "rgba(255,255,255,0.3)", "borderTopLeftRadius::after" to "4rpx", "borderTopRightRadius::after" to "4rpx", "borderBottomRightRadius::after" to "4rpx", "borderBottomLeftRadius::after" to "4rpx")), "picker-cancel" to _pS(_uM("fontSize" to "28rpx", "color" to "rgba(255,255,255,0.85)", "fontWeight" to 500)), "picker-title" to _pS(_uM("fontSize" to "30rpx", "fontWeight" to 700, "color" to "#ffffff", "letterSpacing" to "1rpx")), "picker-confirm" to _pS(_uM("fontSize" to "26rpx", "color" to "#ffffff", "fontWeight" to 600, "backgroundColor" to "rgba(255,255,255,0.22)", "paddingTop" to "10rpx", "paddingRight" to "20rpx", "paddingBottom" to "10rpx", "paddingLeft" to "20rpx", "borderTopLeftRadius" to "30rpx", "borderTopRightRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "borderBottomLeftRadius" to "30rpx", "transitionProperty" to "all", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "backgroundColor:active" to "rgba(255,255,255,0.35)", "transform:active" to "scale(0.95)")), "popup-title" to _pS(_uM("fontSize" to "30rpx", "fontWeight" to 700, "color" to "#ffffff", "letterSpacing" to "1rpx")), "popup-close" to _pS(_uM("fontSize" to "44rpx", "color" to "rgba(255,255,255,0.85)", "fontWeight" to 300, "width" to "60rpx", "height" to "60rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "borderTopLeftRadius" to "30rpx", "borderTopRightRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "borderBottomLeftRadius" to "30rpx", "transitionProperty" to "all", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "backgroundColor:active" to "rgba(255,255,255,0.22)", "transform:active" to "scale(0.9)")), "popup-search" to _pS(_uM("paddingTop" to "24rpx", "paddingRight" to "32rpx", "paddingBottom" to "24rpx", "paddingLeft" to "32rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#e2e8f0", "backgroundColor" to "#f8fafc")), "search-input" to _pS(_uM("width" to "100%", "height" to "72rpx", "paddingTop" to 0, "paddingRight" to "24rpx", "paddingBottom" to 0, "paddingLeft" to "24rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dbeafe", "borderRightColor" to "#dbeafe", "borderBottomColor" to "#dbeafe", "borderLeftColor" to "#dbeafe", "borderTopLeftRadius" to "36rpx", "borderTopRightRadius" to "36rpx", "borderBottomRightRadius" to "36rpx", "borderBottomLeftRadius" to "36rpx", "fontSize" to "28rpx", "backgroundColor" to "#ffffff", "outline" to "none", "transitionProperty" to "all", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "color" to "#0f172a", "borderTopColor:focus" to "#1e40af", "borderRightColor:focus" to "#1e40af", "borderBottomColor:focus" to "#1e40af", "borderLeftColor:focus" to "#1e40af", "boxShadow:focus" to "0 0 0 4rpx rgba(30, 64, 175, 0.12)")), "popup-list" to _pS(_uM("maxHeight" to "520rpx", "overflowY" to "auto")), "popup-empty" to _pS(_uM("paddingTop" to "80rpx", "paddingRight" to "32rpx", "paddingBottom" to "80rpx", "paddingLeft" to "32rpx", "textAlign" to "center")), "empty-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#94a3b8")), "picker-list" to _pS(_uM("maxHeight" to "600rpx", "overflowY" to "auto")), "picker-item" to _uM("" to _uM("display" to "flex", "justifyContent" to "center", "alignItems" to "center", "paddingTop" to "28rpx", "paddingRight" to "32rpx", "paddingBottom" to "28rpx", "paddingLeft" to "32rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#f1f5f9", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "cubic-bezier(0.4,0,0.2,1)", "cursor" to "pointer", "position" to "relative", "backgroundColor:hover" to "#eff6ff", "color:hover" to "#1e40af", "backgroundColor:active" to "#dbeafe", "transform:active" to "scale(0.98)", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000", "height" to "110rpx", "fontSize" to "30rpx", "color" to "#475569", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "marginTop" to "6rpx", "marginRight" to "12rpx", "marginBottom" to "6rpx", "marginLeft" to "12rpx", "zIndex" to 2, "opacity" to 0.6, "backgroundColor" to "rgba(30,64,175,0.04)"), ".selected" to _uM("backgroundColor" to "rgba(0,0,0,0)", "backgroundImage" to "linear-gradient(135deg, #1e40af 0%, #0ea5e9 100%)", "color" to "#ffffff", "fontWeight" to 700, "boxShadow" to "0 8rpx 20rpx rgba(30, 64, 175, 0.35), inset 0 2rpx 0 rgba(255, 255, 255, 0.2)", "transform" to "scale(1.05)", "zIndex" to 3, "opacity" to 1, "fontSize" to "34rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#ffffff", "borderRightColor" to "#ffffff", "borderBottomColor" to "#ffffff", "borderLeftColor" to "#ffffff", "textShadow" to "0 2rpx 4rpx rgba(0, 0, 0, 0.18)")), "popup-item" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "28rpx", "paddingRight" to "32rpx", "paddingBottom" to "28rpx", "paddingLeft" to "32rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#f1f5f9", "transitionProperty" to "all", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "cursor" to "pointer", "backgroundColor:hover" to "#eff6ff")), "popup-item-selected" to _pS(_uM("backgroundColor" to "#dbeafe", "borderLeftWidth" to "8rpx", "borderLeftStyle" to "solid", "borderLeftColor" to "#1e40af", "paddingLeft" to "24rpx", "backgroundColor:hover" to "#bfdbfe")), "popup-item-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#0f172a", "fontWeight" to 500)), "picker-item-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#0f172a")), "level-item" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%")), "level-name" to _pS(_uM("fontSize" to "28rpx", "color" to "#0f172a", "marginBottom" to "6rpx")), "level-desc" to _pS(_uM("fontSize" to "22rpx", "color" to "#94a3b8")), "popup-item-check" to _pS(_uM("fontSize" to "36rpx", "color" to "#1e40af", "fontWeight" to "bold")), "popup-footer" to _pS(_uM("display" to "flex", "borderTopWidth" to "2rpx", "borderTopStyle" to "solid", "borderTopColor" to "#e2e8f0", "backgroundColor" to "#f8fafc")), "popup-btn" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "paddingTop" to "28rpx", "paddingRight" to 0, "paddingBottom" to "28rpx", "paddingLeft" to 0, "textAlign" to "center", "fontSize" to "28rpx", "fontWeight" to 500)), "popup-btn-cancel" to _pS(_uM("color" to "#475569", "borderRightWidth" to "2rpx", "borderRightStyle" to "solid", "borderRightColor" to "#e2e8f0")), "popup-btn-confirm" to _pS(_uM("color" to "#1e40af", "fontWeight" to 700)), "popup-loading" to _pS(_uM("display" to "flex", "justifyContent" to "center", "alignItems" to "center", "paddingTop" to "80rpx", "paddingRight" to "32rpx", "paddingBottom" to "80rpx", "paddingLeft" to "32rpx")), "loading-text" to _pS(_uM("fontSize" to "28rpx", "color" to "#94a3b8")), "form-label-left" to _pS(_uM("fontSize" to "28rpx", "color" to "#0f172a", "marginBottom" to "16rpx", "fontWeight" to 600, "textAlign" to "left")), "custom-picker-full" to _uM("" to _uM("width" to "100%", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e2e8f0", "borderRightColor" to "#e2e8f0", "borderBottomColor" to "#e2e8f0", "borderLeftColor" to "#e2e8f0", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "backgroundColor" to "#ffffff", "minHeight" to "88rpx", "display" to "flex", "alignItems" to "center", "transitionProperty" to "borderColor", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "borderTopColor:hover" to "#1e40af", "borderRightColor:hover" to "#1e40af", "borderBottomColor:hover" to "#1e40af", "borderLeftColor:hover" to "#1e40af"), ".disabled" to _uM("backgroundImage" to "none", "backgroundColor" to "#f1f5f9", "borderTopColor" to "#e2e8f0", "borderRightColor" to "#e2e8f0", "borderBottomColor" to "#e2e8f0", "borderLeftColor" to "#e2e8f0", "opacity" to 0.6)), "picker-container" to _pS(_uM("width" to "100%", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e2e8f0", "borderRightColor" to "#e2e8f0", "borderBottomColor" to "#e2e8f0", "borderLeftColor" to "#e2e8f0", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "backgroundColor" to "#ffffff", "minHeight" to "88rpx", "display" to "flex", "alignItems" to "center")), "picker-text-full" to _uM("" to _uM("fontSize" to "28rpx", "color" to "#0f172a", "width" to "100%", "textAlign" to "left"), ".placeholder" to _uM("color" to "#94a3b8"), ".nature-modified-yes" to _uM("color" to "#e6a23c")), "date-picker-modal" to _pS(_uM("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundColor" to "rgba(10,37,64,0.55)", "display" to "flex", "alignItems" to "flex-end", "zIndex" to 1000)), "date-picker-content" to _pS(_uM("width" to "100%", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "32rpx", "borderTopRightRadius" to "32rpx", "borderBottomRightRadius" to 0, "borderBottomLeftRadius" to 0, "paddingBottom" to "env(safe-area-inset-bottom)")), "date-picker-header" to _pS(_uM("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "28rpx", "paddingRight" to "32rpx", "paddingBottom" to "28rpx", "paddingLeft" to "32rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#e2e8f0")), "date-picker-cancel" to _pS(_uM("fontSize" to "28rpx", "color" to "#94a3b8")), "date-picker-title" to _pS(_uM("fontSize" to "32rpx", "fontWeight" to 700, "color" to "#0f172a")), "date-picker-confirm" to _pS(_uM("fontSize" to "28rpx", "color" to "#1e40af", "fontWeight" to 700)), "date-picker-view" to _pS(_uM("height" to "600rpx", "paddingTop" to 0, "paddingRight" to "32rpx", "paddingBottom" to "32rpx", "paddingLeft" to "32rpx", "position" to "relative")), "picker-item-spacer" to _pS(_uM("pointerEvents" to "none", "opacity" to 0, "!backgroundImage" to "none", "!backgroundColor" to "rgba(0,0,0,0)")), "form-input" to _pS(_uM("flexGrow" to 1, "flexShrink" to 1, "flexBasis" to "0%", "paddingTop" to "20rpx", "paddingRight" to "24rpx", "paddingBottom" to "20rpx", "paddingLeft" to "24rpx", "backgroundImage" to "none", "backgroundColor" to "#f8fafc", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e2e8f0", "borderRightColor" to "#e2e8f0", "borderBottomColor" to "#e2e8f0", "borderLeftColor" to "#e2e8f0", "fontSize" to "28rpx", "color" to "#0f172a", "boxSizing" to "border-box", "width" to "100%")), "@TRANSITION" to _uM("picker-wrapper-inline" to _uM("property" to "all", "duration" to "0.2s", "timingFunction" to "ease"), "bottom-save-btn" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "cubic-bezier(0.4,0,0.2,1)"), "picker-confirm" to _uM("property" to "all", "duration" to "0.2s", "timingFunction" to "ease"), "popup-close" to _uM("property" to "all", "duration" to "0.2s", "timingFunction" to "ease"), "search-input" to _uM("property" to "all", "duration" to "0.2s", "timingFunction" to "ease"), "picker-item" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "cubic-bezier(0.4,0,0.2,1)"), "popup-item" to _uM("property" to "all", "duration" to "0.2s", "timingFunction" to "ease"), "custom-picker-full" to _uM("property" to "borderColor", "duration" to "0.2s", "timingFunction" to "ease")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
