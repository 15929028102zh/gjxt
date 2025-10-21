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
import io.dcloud.uniapp.extapi.`$emit` as uni__emit
import io.dcloud.uniapp.extapi.chooseImage as uni_chooseImage
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.getSystemInfo as uni_getSystemInfo
import io.dcloud.uniapp.extapi.hideLoading as uni_hideLoading
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.redirectTo as uni_redirectTo
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.setNavigationBarTitle as uni_setNavigationBarTitle
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading
import io.dcloud.uniapp.extapi.showModal as uni_showModal
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.uploadFile as uni_uploadFile
open class GenPagesRecordDetail : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: UTSJSONObject) {
            this.recordId = options.getString("id") ?: ""
            this.isDelayed = options.getString("delayed") === "true"
            this.checkLogin()
            this.loadedDataCount = 0
            this.loadRecordDetail()
            this.loadNatureOptions()
            this.loadProfessionOptions()
            this.loadDepartments()
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
        return _cE("scroll-view", _uM("class" to "container", "scroll-y" to "true", "scroll-top" to _ctx.scrollTop, "style" to _nS(_uM("height" to (_ctx.containerHeight + "px"))), "enable-back-to-top" to "true"), _uA(
            if (isTrue(_ctx.loading)) {
                _cE("view", _uM("key" to 0, "class" to "loading-state"), _uA(
                    _cE("text", _uM("class" to "loading-text"), "加载中...")
                ))
            } else {
                if (isTrue(_ctx.recordDetail)) {
                    _cE("view", _uM("key" to 1, "class" to "detail-content"), _uA(
                        _cE("view", _uM("class" to "status-header"), _uA(
                            _cE("text", _uM("class" to "page-title"), _tD(if (_ctx.isDelayed) {
                                "延迟整改录入"
                            } else {
                                "问题详情"
                            }), 1),
                            _cE("view", _uM("class" to _nC(_uA(
                                "record-status",
                                _ctx.getStatusClass(_ctx.recordDetail?.status ?: 0)
                            ))), _uA(
                                _cE("text", null, _tD(if (_ctx.isDelayed) {
                                    "延迟整改"
                                } else {
                                    _ctx.getStatusText(_ctx.recordDetail?.status ?: 0)
                                }), 1)
                            ), 2)
                        )),
                        if (_ctx.imageList.length > 0) {
                            _cE("view", _uM("key" to 0, "class" to "form-item"), _uA(
                                _cE("text", _uM("class" to "form-label"), "📷 问题照片 (" + _tD(_ctx.imageList.length) + "张)", 1),
                                _cE("view", _uM("class" to "image-upload"), _uA(
                                    _cE("view", _uM("class" to "image-list"), _uA(
                                        _cE(Fragment, null, RenderHelpers.renderList(_ctx.imageList, fun(image, index, __index, _cached): Any {
                                            return _cE("view", _uM("class" to "image-item", "key" to index), _uA(
                                                _cE("image", _uM("src" to image, "mode" to "aspectFill", "class" to "uploaded-image", "onClick" to fun(){
                                                    _ctx.previewImage(index)
                                                }, "onError" to _ctx.onImageError, "onLoad" to _ctx.onImageLoad), null, 40, _uA(
                                                    "src",
                                                    "onClick",
                                                    "onError",
                                                    "onLoad"
                                                ))
                                            ))
                                        }), 128)
                                    ))
                                ))
                            ))
                        } else {
                            _cC("v-if", true)
                        },
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "问题描述"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.problemDescription ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "责任单位"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.responsibleUnit ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "责任人"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.responsiblePerson ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "性质界定"),
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
                                }), 3)
                            ), 10, _uA(
                                "onClick"
                            ))
                        )),
                        if (isTrue(_ctx.showNaturePopup)) {
                            _cE("view", _uM("key" to 1, "class" to "popup-overlay", "onClick" to _ctx.hideNaturePicker), _uA(
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
                        },
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "考核情况"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.assessmentSituation ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "考核金额"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.assessmentAmount ?: "") + "元", 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "安全记分"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.safetyScore ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "专业"),
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
                                }), 3)
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
                        },
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "发生部位"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.location ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "检查人"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.inspector ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "检查组组长"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.groupLeader ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "检查组副组长"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.deputyLeader ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "所属部门"),
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
                                }), 3)
                            ), 10, _uA(
                                "onClick"
                            ))
                        )),
                        if (isTrue(_ctx.showDepartmentPopup)) {
                            _cE("view", _uM("key" to 3, "class" to "popup-overlay", "onClick" to _ctx.hideDepartmentPicker), _uA(
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
                        },
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "整改期限"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.formatDate(_ctx.recordDetail?.rectificationDate) ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "整改效果描述"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("textarea", _uM("modelValue" to _ctx.rectificationEffect, "onInput" to fun(`$event`: UniInputEvent){
                                    _ctx.rectificationEffect = `$event`.detail.value
                                }, "class" to "form-textarea", "placeholder" to "请描述整改效果...", "maxlength" to "500", "show-confirm-bar" to "false"), null, 40, _uA(
                                    "modelValue",
                                    "onInput"
                                ))
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "📋 整改状态"),
                            _cE("view", _uM("class" to "status-selector"), _uA(
                                _cE("view", _uM("class" to _nC(_uA(
                                    "status-option pending",
                                    _uM("active" to (_ctx.rectificationStatus === 0))
                                )), "onClick" to fun(){
                                    _ctx.setRectificationStatus(0)
                                }), " ⏳ 未整改 ", 10, _uA(
                                    "onClick"
                                )),
                                _cE("view", _uM("class" to _nC(_uA(
                                    "status-option completed",
                                    _uM("active" to (_ctx.rectificationStatus === 1))
                                )), "onClick" to fun(){
                                    _ctx.setRectificationStatus(1)
                                }), " ✅ 已整改 ", 10, _uA(
                                    "onClick"
                                ))
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "📷 整改图片 (" + _tD(_ctx.rectificationImages.length) + "/3)", 1),
                            _cE("view", _uM("class" to "image-upload"), _uA(
                                _cE("view", _uM("class" to "image-list"), _uA(
                                    _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectificationImages, fun(image, index, __index, _cached): Any {
                                        return _cE("view", _uM("class" to "image-item", "key" to index), _uA(
                                            _cE("image", _uM("src" to image, "mode" to "aspectFill", "class" to "uploaded-image", "onClick" to fun(){
                                                _ctx.previewRectificationImage(index)
                                            }, "onError" to _ctx.onImageError, "onLoad" to _ctx.onImageLoad), null, 40, _uA(
                                                "src",
                                                "onClick",
                                                "onError",
                                                "onLoad"
                                            )),
                                            _cE("view", _uM("class" to "image-delete", "onClick" to fun(){
                                                _ctx.removeRectificationImage(index)
                                            }), _uA(
                                                _cE("text", null, "×")
                                            ), 8, _uA(
                                                "onClick"
                                            ))
                                        ))
                                    }), 128),
                                    if (_ctx.rectificationImages.length < 3) {
                                        _cE("view", _uM("key" to 0, "class" to "image-add", "onClick" to _ctx.chooseRectificationImage), _uA(
                                            _cE("text", _uM("class" to "add-icon"), "+"),
                                            _cE("text", _uM("class" to "add-text"), "添加图片")
                                        ), 8, _uA(
                                            "onClick"
                                        ))
                                    } else {
                                        _cC("v-if", true)
                                    }
                                ))
                            ))
                        )),
                        _cE("view", _uM("class" to "save-button-wrapper"), _uA(
                            _cE("button", _uM("class" to "save-btn", "onClick" to _ctx.saveRectificationInfo, "disabled" to _ctx.saving), _tD(if (_ctx.saving) {
                                if (_ctx.isDelayed) {
                                    "确认中..."
                                } else {
                                    "保存中..."
                                }
                            } else {
                                if (_ctx.isDelayed) {
                                    "确认整改"
                                } else {
                                    "保存整改信息"
                                }
                            }), 9, _uA(
                                "onClick",
                                "disabled"
                            ))
                        ))
                    ))
                } else {
                    _cE("view", _uM("key" to 2, "class" to "error-state"), _uA(
                        _cE("text", _uM("class" to "error-text"), "记录不存在或已被删除")
                    ))
                }
            }
        ), 12, _uA(
            "scroll-top"
        ))
    }
    open var recordId: String by `$data`
    open var recordDetail: RecordDetail? by `$data`
    open var imageList: UTSArray<String> by `$data`
    open var loading: Boolean by `$data`
    open var canOperate: Boolean by `$data`
    open var rectificationEffect: String by `$data`
    open var rectificationImages: UTSArray<String> by `$data`
    open var rectificationStatus: Number by `$data`
    open var saving: Boolean by `$data`
    open var scrollTop: Number by `$data`
    open var containerHeight: Number by `$data`
    open var isDelayed: Boolean by `$data`
    open var natureOptions: UTSArray<NatureOption1> by `$data`
    open var professionOptions: UTSArray<ProfessionOption1> by `$data`
    open var departmentOptions: UTSArray<DepartmentOption1> by `$data`
    open var selectedNatureIndex: Number by `$data`
    open var selectedProfessionIndex: Number by `$data`
    open var selectedDepartmentIndex: Number by `$data`
    open var showNaturePopup: Boolean by `$data`
    open var showProfessionPopup: Boolean by `$data`
    open var showDepartmentPopup: Boolean by `$data`
    open var loadedDataCount: Number by `$data`
    open var totalDataCount: Number by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("recordId" to "", "recordDetail" to null as RecordDetail?, "imageList" to _uA<String>(), "loading" to true, "canOperate" to false, "rectificationEffect" to "", "rectificationImages" to _uA<String>(), "rectificationStatus" to 0, "saving" to false, "scrollTop" to 0, "containerHeight" to 0, "isDelayed" to false, "natureOptions" to _uA<NatureOption1>(), "professionOptions" to _uA<ProfessionOption1>(), "departmentOptions" to _uA<DepartmentOption1>(), "selectedNatureIndex" to -1, "selectedProfessionIndex" to -1, "selectedDepartmentIndex" to -1, "showNaturePopup" to false, "showProfessionPopup" to false, "showDepartmentPopup" to false, "loadedDataCount" to 0, "totalDataCount" to 4)
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
    open var checkAllDataLoaded = ::gen_checkAllDataLoaded_fn
    open fun gen_checkAllDataLoaded_fn() {
        this.loadedDataCount++
        console.log("\u6570\u636E\u52A0\u8F7D\u8FDB\u5EA6: " + this.loadedDataCount + "/" + this.totalDataCount, " at pages/record/detail.uvue:391")
        if (this.loadedDataCount >= this.totalDataCount) {
            console.log("所有数据加载完成，开始回写选中值", " at pages/record/detail.uvue:394")
            console.log("当前数据状态:", _uO("recordDetail" to this.recordDetail, "natureOptionsLength" to this.natureOptions.length, "professionOptionsLength" to this.professionOptions.length, "departmentOptionsLength" to this.departmentOptions.length), " at pages/record/detail.uvue:395")
            setTimeout(fun(){
                console.log("开始执行 initSelectedValues", " at pages/record/detail.uvue:403")
                this.initSelectedValues()
                this.loading = false
                console.log("加载状态设置为 false", " at pages/record/detail.uvue:406")
            }
            , 100)
        }
    }
    open var loadRecordDetail = ::gen_loadRecordDetail_fn
    open fun gen_loadRecordDetail_fn() {
        val token = uni_getStorageSync("token")
        uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record/" + this.recordId), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            if (res.statusCode === 200 && data.getNumber("code") === 200) {
                val recordData = data.getJSON("data")
                if (recordData != null) {
                    this.recordDetail = RecordDetail(id = recordData.getString("id") ?: "", problemDescription = recordData.getString("problemDescription") ?: "", locationName = recordData.getString("locationName") ?: "", severity = recordData.getNumber("severity") ?: 1, taskName = recordData.getString("taskName") ?: "", reporterName = recordData.getString("reporterName") ?: "", createTime = recordData.getString("createTime") ?: "", updateTime = recordData.getString("updateTime") ?: "", status = recordData.getNumber("status") ?: 0, problemImageUrls = recordData.getString("problemImageUrls") ?: "", suggestions = recordData.getString("suggestions") ?: "", reporterId = recordData.getString("reporterId") ?: "", responsibleUnit = recordData.getString("responsibleUnit") ?: "", responsiblePerson = recordData.getString("responsiblePerson") ?: "", natureId = "", natureName = "", assessmentSituation = recordData.getString("assessmentSituation") ?: "", assessmentAmount = recordData.getNumber("assessmentAmount") ?: 0, safetyScore = recordData.getNumber("safetyScore") ?: 0, professionId = recordData.getString("professionId") ?: "", professionName = recordData.getString("professionName") ?: "", inspector = recordData.getString("inspectorName") ?: "", groupLeader = recordData.getString("groupLeaderName") ?: "", deputyLeader = recordData.getString("deputyLeaderName") ?: "", departmentId = recordData.getString("departmentId") ?: "", departmentName = recordData.getString("departmentName") ?: "", rectificationDate = recordData.getString("rectificationDeadline") ?: "", location = recordData.getString("location") ?: "", rectificationEffect = recordData.getString("rectificationEffect") ?: "", rectificationImageUrls = recordData.getString("rectificationImageUrls") ?: "", rectificationTime = recordData.getString("rectificationTime") ?: "")
                    val natureDefinitionValue = recordData.getString("natureDefinition") ?: ""
                    console.log("natureDefinition原始值:", natureDefinitionValue, " at pages/record/detail.uvue:464")
                    if (natureDefinitionValue.length === 32 && UTSRegExp("^[a-f0-9]+\$", "i").test(natureDefinitionValue)) {
                        this.recordDetail!!.natureId = natureDefinitionValue
                        console.log("检测到ID格式，作为ID处理:", natureDefinitionValue, " at pages/record/detail.uvue:471")
                    } else {
                        this.recordDetail!!.natureName = natureDefinitionValue
                        console.log("检测到名称格式，作为名称处理:", natureDefinitionValue, " at pages/record/detail.uvue:475")
                    }
                    this.rectificationEffect = this.recordDetail?.rectificationEffect ?: ""
                    if (this.recordDetail?.rectificationEffect != null && this.recordDetail?.rectificationEffect != "") {
                        this.rectificationStatus = 1
                    } else {
                        this.rectificationStatus = 0
                    }
                    val rectificationImages = this.recordDetail?.rectificationImageUrls
                    if (rectificationImages != null && rectificationImages != "") {
                        this.rectificationImages = this.processImageUrls(rectificationImages as String)
                    }
                    val imageUrls = this.recordDetail?.problemImageUrls
                    if (imageUrls != null && imageUrls != "") {
                        this.imageList = this.processImageUrls(imageUrls as String)
                    } else {
                        this.imageList = _uA()
                    }
                    val userInfoStr = uni_getStorageSync("userInfo") as String
                    var userInfo: UTSJSONObject? = null
                    if (userInfoStr != null && userInfoStr != "") {
                        try {
                            userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/detail.uvue:507") as UTSJSONObject
                        }
                         catch (e: Throwable) {
                            console.error("解析用户信息失败:", e, " at pages/record/detail.uvue:509")
                        }
                    }
                    this.canOperate = userInfo != null && (userInfo.getString("role") === "ADMIN" || userInfo.getString("id") === (this.recordDetail?.reporterId ?: ""))
                    uni_setNavigationBarTitle(SetNavigationBarTitleOptions(title = if (this.isDelayed) {
                        "延迟整改录入"
                    } else {
                        "记录详情"
                    }
                    ))
                    this.checkAllDataLoaded()
                }
            } else {
                uni_showToast(ShowToastOptions(title = data.getString("message") ?: "加载失败", icon = "none"))
            }
            this.checkAllDataLoaded()
        }
        , fail = fun(error){
            console.error("加载记录详情失败:", error, " at pages/record/detail.uvue:532")
            uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
            this.checkAllDataLoaded()
        }
        ))
    }
    open var previewImage = ::gen_previewImage_fn
    open fun gen_previewImage_fn(index: Number) {
        uni_previewImage(PreviewImageOptions(current = index, urls = this.imageList))
    }
    open var onImageLoad = ::gen_onImageLoad_fn
    open fun gen_onImageLoad_fn(e: Any) {}
    open var onImageError = ::gen_onImageError_fn
    open fun gen_onImageError_fn(e: Any) {
        uni_showToast(ShowToastOptions(title = "图片加载失败", icon = "none"))
    }
    open var updateRecordStatus = ::gen_updateRecordStatus_fn
    open fun gen_updateRecordStatus_fn(status: Number, actionName: String) {
        uni_showModal(ShowModalOptions(title = "确认操作", content = "\u786E\u5B9A\u8981" + actionName + "\u5417\uFF1F", success = fun(res){
            if (res.confirm) {
                val token = uni_getStorageSync("token")
                uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record/" + this.recordId + "/status"), method = "PUT", header = object : UTSJSONObject() {
                    var Authorization = "Bearer " + token
                    var `Content-Type` = "application/json"
                }, data = _uO("status" to status), success = fun(response){
                    val data = response.data as UTSJSONObject
                    if (response.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                        uni_showToast(ShowToastOptions(title = "" + actionName + "\u6210\u529F", icon = "success"))
                        this.loadRecordDetail()
                    } else {
                        uni_showToast(ShowToastOptions(title = data.getString("message") ?: "操作失败", icon = "none"))
                    }
                }
                , fail = fun(error){
                    console.error("" + actionName + "\u5931\u8D25:", error, " at pages/record/detail.uvue:594")
                    uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
                }
                ))
            }
        }
        ))
    }
    open var startRectification = ::gen_startRectification_fn
    open fun gen_startRectification_fn() {
        this.updateRecordStatus(1, "开始整改")
    }
    open var completeRectification = ::gen_completeRectification_fn
    open fun gen_completeRectification_fn() {
        this.updateRecordStatus(2, "完成整改")
    }
    open var acceptRectification = ::gen_acceptRectification_fn
    open fun gen_acceptRectification_fn() {
        this.updateRecordStatus(3, "验收通过")
    }
    open var getStatusText = ::gen_getStatusText_fn
    open fun gen_getStatusText_fn(status: Number): String {
        when (status) {
            0 -> 
                return "待整改"
            1 -> 
                return "整改中"
            2 -> 
                return "已整改"
            3 -> 
                return "已验收"
            else -> 
                return "未知"
        }
    }
    open var getStatusClass = ::gen_getStatusClass_fn
    open fun gen_getStatusClass_fn(status: Number): String {
        when (status) {
            0 -> 
                return "status-pending"
            1 -> 
                return "status-processing"
            2 -> 
                return "status-completed"
            3 -> 
                return "status-accepted"
            else -> 
                return "status-unknown"
        }
    }
    open var getSeverityText = ::gen_getSeverityText_fn
    open fun gen_getSeverityText_fn(severity: Number): String {
        when (severity) {
            1 -> 
                return "低"
            2 -> 
                return "中"
            3 -> 
                return "高"
            4 -> 
                return "紧急"
            else -> 
                return "未知"
        }
    }
    open var chooseRectificationImage = ::gen_chooseRectificationImage_fn
    open fun gen_chooseRectificationImage_fn() {
        if (this.rectificationImages.length >= 3) {
            uni_showToast(ShowToastOptions(title = "最多只能上传3张图片", icon = "none"))
            return
        }
        uni_chooseImage(ChooseImageOptions(count = 3 - this.rectificationImages.length, sourceType = _uA(
            "camera",
            "album"
        ), success = fun(res){
            val tempFilePaths = res.tempFilePaths
            run {
                var i: Number = 0
                while(i < tempFilePaths.length){
                    this.rectificationImages.push(tempFilePaths[i])
                    i++
                }
            }
        }
        ))
    }
    open var previewRectificationImage = ::gen_previewRectificationImage_fn
    open fun gen_previewRectificationImage_fn(index: Number) {
        uni_previewImage(PreviewImageOptions(current = index, urls = this.rectificationImages))
    }
    open var removeRectificationImage = ::gen_removeRectificationImage_fn
    open fun gen_removeRectificationImage_fn(index: Number) {
        this.rectificationImages.splice(index, 1)
    }
    open var setRectificationStatus = ::gen_setRectificationStatus_fn
    open fun gen_setRectificationStatus_fn(status: Number) {
        this.rectificationStatus = status
    }
    open var convertToHttpUrl = ::gen_convertToHttpUrl_fn
    open fun gen_convertToHttpUrl_fn(relativePath: String): String {
        if (relativePath == null || relativePath == "" || relativePath.trim() === "") {
            return ""
        }
        if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) {
            return relativePath
        }
        val fullUrl = "" + apiConfig.baseUrl + "/api" + relativePath
        return fullUrl
    }
    open var processImageUrls = ::gen_processImageUrls_fn
    open fun gen_processImageUrls_fn(imageUrls: String): UTSArray<String> {
        if (imageUrls == null || imageUrls.trim() === "") {
            return _uA()
        }
        try {
            if (imageUrls.trim().startsWith("[") && imageUrls.trim().endsWith("]")) {
                val parsed = UTSAndroid.consoleDebugError(JSON.parse(imageUrls), " at pages/record/detail.uvue:714") as UTSArray<String>
                return parsed.map(fun(url): String {
                    return this.convertToHttpUrl(url)
                }
                ).filter(fun(url): Boolean {
                    return url !== ""
                }
                )
            }
        }
         catch (e: Throwable) {
            console.log("JSON解析失败，尝试按逗号分隔处理:", e, " at pages/record/detail.uvue:718")
        }
        return imageUrls.split(",").map(fun(url): String {
            return url.trim()
        }
        ).filter(fun(url): Boolean {
            return url !== ""
        }
        ).map(fun(url): String {
            return this.convertToHttpUrl(url)
        }
        )
    }
    open var saveRectificationInfo = ::gen_saveRectificationInfo_fn
    open fun gen_saveRectificationInfo_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend w@{
                if (this.saving) {
                    return@w
                }
                if (this.rectificationEffect.trim().length === 0) {
                    uni_showToast(ShowToastOptions(title = "请填写整改效果", icon = "none"))
                    return@w
                }
                this.saving = true
                try {
                    var imageUrls: UTSArray<String> = _uA()
                    if (this.rectificationImages.length > 0) {
                        uni_showLoading(ShowLoadingOptions(title = "上传图片中..."))
                        imageUrls = await(this.uploadRectificationImages())
                        uni_hideLoading()
                    }
                    val updateData: UTSJSONObject = _uO("__\$originalPosition" to UTSSourceMapPosition("updateData", "pages/record/detail.uvue", 753, 11), "id" to (this.recordDetail?.id ?: ""), "problemDescription" to (this.recordDetail?.problemDescription ?: ""), "responsibleUnit" to (this.recordDetail?.responsibleUnit ?: ""), "responsiblePerson" to (this.recordDetail?.responsiblePerson ?: ""), "natureDefinition" to if (this.selectedNatureIndex !== -1) {
                        this.natureOptions[this.selectedNatureIndex].name
                    } else {
                        (this.recordDetail?.natureName ?: "")
                    }
                    , "assessmentSituation" to (this.recordDetail?.assessmentSituation ?: ""), "assessmentAmount" to (this.recordDetail?.assessmentAmount ?: 0), "safetyScore" to (this.recordDetail?.safetyScore ?: 0), "professionId" to if (this.selectedProfessionIndex !== -1) {
                        this.professionOptions[this.selectedProfessionIndex].id
                    } else {
                        (this.recordDetail?.professionId ?: "")
                    }
                    , "location" to (this.recordDetail?.location ?: ""), "inspector" to (this.recordDetail?.inspector ?: ""), "groupLeader" to (this.recordDetail?.groupLeader ?: ""), "deputyLeader" to (this.recordDetail?.deputyLeader ?: ""), "departmentId" to if (this.selectedDepartmentIndex !== -1) {
                        this.departmentOptions[this.selectedDepartmentIndex].id
                    } else {
                        (this.recordDetail?.departmentId ?: "")
                    }
                    , "rectificationDeadline" to (this.recordDetail?.rectificationDate ?: ""), "problemImageUrls" to (this.recordDetail?.problemImageUrls ?: ""), "reporterId" to (this.recordDetail?.reporterId ?: ""), "rectificationEffect" to this.rectificationEffect, "rectificationImageUrls" to imageUrls.join(","), "status" to this.rectificationStatus, "rectificationTime" to Date().toISOString().replace("T", " ").substring(0, 19))
                    uni_showLoading(ShowLoadingOptions(title = "保存中..."))
                    console.log("准备保存的数据:", updateData, " at pages/record/detail.uvue:787")
                    console.log("性质界定保存值:", _uO("selectedNatureIndex" to this.selectedNatureIndex, "natureDefinition" to updateData["natureDefinition"], "natureOptions" to this.natureOptions), " at pages/record/detail.uvue:788")
                    val token = uni_getStorageSync("token")
                    uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record"), method = "PUT", header = object : UTSJSONObject() {
                        var Authorization = "Bearer " + token
                        var `Content-Type` = "application/json"
                    }, data = updateData, success = fun(response){
                        uni_hideLoading()
                        val data = response.data as UTSJSONObject
                        if (response.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                            uni_showToast(ShowToastOptions(title = if (this.isDelayed) {
                                "确认整改成功"
                            } else {
                                "保存成功"
                            }, icon = "success", duration = 1500))
                            setTimeout(fun(){
                                val pages = getCurrentPages()
                                if (pages.length >= 2) {
                                    val prevPage = pages[pages.length - 2]
                                    if (prevPage.route != null && prevPage.route.includes("/pages/record/record")) {
                                        uni_navigateBack(NavigateBackOptions(delta = 1, success = fun(_){
                                            uni__emit("recordUpdated", null)
                                        }))
                                    } else {
                                        uni_navigateBack(null)
                                    }
                                } else {
                                    uni_redirectTo(RedirectToOptions(url = "/pages/record/record?status=0"))
                                }
                            }, 1600)
                        } else {
                            uni_showToast(ShowToastOptions(title = data.getString("message") ?: "保存失败", icon = "none"))
                        }
                        this.saving = false
                    }
                    , fail = fun(error){
                        uni_hideLoading()
                        console.error("保存整改信息失败:", error, " at pages/record/detail.uvue:850")
                        uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
                        this.saving = false
                    }
                    ))
                }
                 catch (error: Throwable) {
                    uni_hideLoading()
                    console.error("上传图片失败:", error, " at pages/record/detail.uvue:860")
                    uni_showToast(ShowToastOptions(title = "图片上传失败，请重试", icon = "none"))
                    this.saving = false
                }
        })
    }
    open var uploadRectificationImages = ::gen_uploadRectificationImages_fn
    open fun gen_uploadRectificationImages_fn(): UTSPromise<UTSArray<String>> {
        return wrapUTSPromise(suspend w@{
                if (this.rectificationImages.length === 0) {
                    return@w _uA()
                }
                val token = uni_getStorageSync("token")
                val uploadPromises = this.rectificationImages.map(fun(imagePath): UTSPromise<String> {
                    return UTSPromise<String>(fun(resolve, reject){
                        uni_uploadFile(UploadFileOptions(url = getApiUrl("/upload/image"), filePath = imagePath, name = "file", header = object : UTSJSONObject() {
                            var Authorization = "Bearer " + token
                        }, success = fun(res){
                            try {
                                val data = UTSAndroid.consoleDebugError(JSON.parse(res.data), " at pages/record/detail.uvue:887") as UTSJSONObject
                                if (res.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                                    val dataObj = data.getJSON("data")
                                    val imageUrl = dataObj?.getString("filePath") ?: ""
                                    if (imageUrl != null && imageUrl != "") {
                                        resolve(imageUrl)
                                    } else {
                                        reject(UTSError("上传成功但未获取到图片URL"))
                                    }
                                } else {
                                    reject(UTSError(data.getString("message") ?: "上传失败"))
                                }
                            }
                             catch (e: Throwable) {
                                reject(UTSError("解析响应失败"))
                            }
                        }
                        , fail = fun(error){
                            reject(UTSError("\u4E0A\u4F20\u5931\u8D25: " + (error.errMsg ?: "网络错误")))
                        }
                        ))
                    }
                    )
                }
                )
                try {
                    val imageUrls = await(UTSPromise.all(uploadPromises))
                    console.log("所有整改图片上传成功，返回URL数量:", imageUrls.length, " at pages/record/detail.uvue:912")
                    console.log("上传成功的图片URLs:", imageUrls, " at pages/record/detail.uvue:913")
                    return@w imageUrls
                }
                 catch (error: Throwable) {
                    console.error("整改图片上传失败:", error, " at pages/record/detail.uvue:916")
                    throw error
                }
        })
    }
    open var formatDate = ::gen_formatDate_fn
    open fun gen_formatDate_fn(dateStr: String?): String {
        if (dateStr == null || dateStr == "") {
            return ""
        }
        if (dateStr.includes(" ")) {
            return dateStr.split(" ")[0]
        }
        if (dateStr.includes("T")) {
            return dateStr.split("T")[0]
        }
        return dateStr
    }
    open var getSeverityClass = ::gen_getSeverityClass_fn
    open fun gen_getSeverityClass_fn(severity: Number): String {
        when (severity) {
            1 -> 
                return "severity-low"
            2 -> 
                return "severity-medium"
            3 -> 
                return "severity-high"
            4 -> 
                return "severity-urgent"
            else -> 
                return "severity-unknown"
        }
    }
    open var initSelectedValues = ::gen_initSelectedValues_fn
    open fun gen_initSelectedValues_fn() {
        console.log("开始初始化选中值", " at pages/record/detail.uvue:946")
        if (this.recordDetail == null) {
            console.log("recordDetail为空，无法初始化选中值", " at pages/record/detail.uvue:948")
            return
        }
        console.log("记录详情数据:", this.recordDetail, " at pages/record/detail.uvue:952")
        console.log("字典数据状态:", _uO("natureOptions" to this.natureOptions, "professionOptions" to this.professionOptions, "departmentOptions" to this.departmentOptions), " at pages/record/detail.uvue:953")
        val natureId = this.recordDetail!!.natureId ?: ""
        val natureName = this.recordDetail!!.natureName ?: ""
        console.log("性质界定数据:", _uO("natureId" to natureId, "natureName" to natureName), " at pages/record/detail.uvue:962")
        if (natureId !== "") {
            console.log("使用ID匹配:", natureId, " at pages/record/detail.uvue:966")
            run {
                var i: Number = 0
                while(i < this.natureOptions.length){
                    console.log("\u6027\u8D28\u754C\u5B9AID\u6BD4\u8F83: " + this.natureOptions[i].id + " == " + natureId, " at pages/record/detail.uvue:968")
                    if (this.natureOptions[i].id == natureId) {
                        this.selectedNatureIndex = i
                        this.recordDetail!!.natureName = this.natureOptions[i].name
                        console.log("性质界定ID匹配成功，索引:", i, "ID:", this.natureOptions[i].id, "名称:", this.natureOptions[i].name, " at pages/record/detail.uvue:972")
                        break
                    }
                    i++
                }
            }
            if (this.selectedNatureIndex === -1) {
                console.log("性质界定ID在字典中未找到匹配项:", natureId, " at pages/record/detail.uvue:977")
            }
        } else if (natureName !== "") {
            console.log("使用名称匹配:", natureName, " at pages/record/detail.uvue:981")
            run {
                var i: Number = 0
                while(i < this.natureOptions.length){
                    console.log("\u6027\u8D28\u754C\u5B9A\u540D\u79F0\u6BD4\u8F83: " + this.natureOptions[i].name + " == " + natureName, " at pages/record/detail.uvue:983")
                    if (this.natureOptions[i].name == natureName) {
                        this.selectedNatureIndex = i
                        this.recordDetail!!.natureId = this.natureOptions[i].id
                        console.log("性质界定名称匹配成功，索引:", i, "ID:", this.natureOptions[i].id, "名称:", this.natureOptions[i].name, " at pages/record/detail.uvue:987")
                        break
                    }
                    i++
                }
            }
            if (this.selectedNatureIndex === -1) {
                console.log("性质界定名称在字典中未找到匹配项:", natureName, " at pages/record/detail.uvue:992")
            }
        } else {
            console.log("性质界定ID和名称都为空，跳过匹配", " at pages/record/detail.uvue:995")
        }
        if (this.selectedNatureIndex === -1) {
            console.log("可用的字典选项:", this.natureOptions.map(fun(item): String {
                return "" + item.name + "(" + item.id + ")"
            }
            ), " at pages/record/detail.uvue:999")
        }
        val professionId = this.recordDetail!!.professionId ?: ""
        console.log("专业ID:", professionId, " at pages/record/detail.uvue:1004")
        if (professionId !== "") {
            run {
                var i: Number = 0
                while(i < this.professionOptions.length){
                    console.log("\u4E13\u4E1A\u6BD4\u8F83: " + this.professionOptions[i].id + " == " + professionId, " at pages/record/detail.uvue:1007")
                    if (this.professionOptions[i].id == professionId) {
                        this.selectedProfessionIndex = i
                        console.log("专业匹配成功，索引:", i, "名称:", this.professionOptions[i].name, " at pages/record/detail.uvue:1010")
                        break
                    }
                    i++
                }
            }
        } else {
            console.log("专业ID为空，跳过匹配", " at pages/record/detail.uvue:1015")
        }
        val departmentId = this.recordDetail!!.departmentId ?: ""
        console.log("部门ID:", departmentId, " at pages/record/detail.uvue:1020")
        if (departmentId !== "") {
            run {
                var i: Number = 0
                while(i < this.departmentOptions.length){
                    console.log("\u90E8\u95E8\u6BD4\u8F83: " + this.departmentOptions[i].id + " == " + departmentId, " at pages/record/detail.uvue:1023")
                    if (this.departmentOptions[i].id == departmentId) {
                        this.selectedDepartmentIndex = i
                        console.log("部门匹配成功，索引:", i, "名称:", this.departmentOptions[i].name, " at pages/record/detail.uvue:1026")
                        break
                    }
                    i++
                }
            }
        } else {
            console.log("部门ID为空，跳过匹配", " at pages/record/detail.uvue:1031")
        }
        console.log("回写选中值完成:", _uO("natureId" to natureId, "selectedNatureIndex" to this.selectedNatureIndex, "professionId" to professionId, "selectedProfessionIndex" to this.selectedProfessionIndex, "departmentId" to departmentId, "selectedDepartmentIndex" to this.selectedDepartmentIndex), " at pages/record/detail.uvue:1034")
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
                            this.natureOptions.push(NatureOption1(id = id, name = name))
                            i++
                        }
                    }
                }
                console.log("性质界定数据加载成功:", this.natureOptions, " at pages/record/detail.uvue:1067")
            } else {
                val message = data.getString("message") ?: "未知错误"
                console.error("获取性质界定数据失败:", message, " at pages/record/detail.uvue:1070")
                this.natureOptions = _uA(
                    NatureOption1(id = "1", name = "一般问题"),
                    NatureOption1(id = "2", name = "较大问题"),
                    NatureOption1(id = "3", name = "严重问题"),
                    NatureOption1(id = "4", name = "重大问题")
                )
            }
            this.checkAllDataLoaded()
        }
        , fail = fun(error){
            console.error("请求性质界定数据失败:", error, " at pages/record/detail.uvue:1082")
            this.natureOptions = _uA(
                NatureOption1(id = "1", name = "一般问题"),
                NatureOption1(id = "2", name = "较大问题"),
                NatureOption1(id = "3", name = "严重问题"),
                NatureOption1(id = "4", name = "重大问题")
            )
            this.checkAllDataLoaded()
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
                            this.professionOptions.push(ProfessionOption1(id = id, name = name))
                            i++
                        }
                    }
                }
                console.log("专业数据加载成功:", this.professionOptions, " at pages/record/detail.uvue:1121")
            } else {
                val message = data.getString("message") ?: "未知错误"
                console.error("获取专业数据失败:", message, " at pages/record/detail.uvue:1124")
                this.professionOptions = _uA(
                    ProfessionOption1(id = "1", name = "机械"),
                    ProfessionOption1(id = "2", name = "电气"),
                    ProfessionOption1(id = "3", name = "仪表"),
                    ProfessionOption1(id = "4", name = "工艺"),
                    ProfessionOption1(id = "5", name = "安全"),
                    ProfessionOption1(id = "6", name = "环保")
                )
            }
            this.checkAllDataLoaded()
        }
        , fail = fun(error){
            console.error("请求专业数据失败:", error, " at pages/record/detail.uvue:1138")
            this.professionOptions = _uA(
                ProfessionOption1(id = "1", name = "机械"),
                ProfessionOption1(id = "2", name = "电气"),
                ProfessionOption1(id = "3", name = "仪表"),
                ProfessionOption1(id = "4", name = "工艺"),
                ProfessionOption1(id = "5", name = "安全"),
                ProfessionOption1(id = "6", name = "环保")
            )
            this.checkAllDataLoaded()
        }
        ))
    }
    open var loadDepartments = ::gen_loadDepartments_fn
    open fun gen_loadDepartments_fn() {
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
                                this.departmentOptions.push(DepartmentOption1(id = id, name = deptName))
                            }
                            i++
                        }
                    }
                }
                console.log("部门数据加载成功:", this.departmentOptions, " at pages/record/detail.uvue:1179")
            } else {
                val message = data.getString("message") ?: "未知错误"
                console.error("获取部门数据失败:", message, " at pages/record/detail.uvue:1182")
                this.departmentOptions = _uA(
                    DepartmentOption1(id = "1", name = "生产部"),
                    DepartmentOption1(id = "2", name = "安全部"),
                    DepartmentOption1(id = "3", name = "设备部"),
                    DepartmentOption1(id = "4", name = "技术部"),
                    DepartmentOption1(id = "5", name = "质量部"),
                    DepartmentOption1(id = "6", name = "环保部"),
                    DepartmentOption1(id = "7", name = "行政部")
                )
            }
            this.checkAllDataLoaded()
        }
        , fail = fun(error){
            console.error("请求部门数据失败:", error, " at pages/record/detail.uvue:1197")
            this.departmentOptions = _uA(
                DepartmentOption1(id = "1", name = "生产部"),
                DepartmentOption1(id = "2", name = "安全部"),
                DepartmentOption1(id = "3", name = "设备部"),
                DepartmentOption1(id = "4", name = "技术部"),
                DepartmentOption1(id = "5", name = "质量部"),
                DepartmentOption1(id = "6", name = "环保部"),
                DepartmentOption1(id = "7", name = "行政部")
            )
            this.checkAllDataLoaded()
        }
        ))
    }
    open var showNaturePicker = ::gen_showNaturePicker_fn
    open fun gen_showNaturePicker_fn() {
        if (this.natureOptions.length > 0) {
            this.showNaturePopup = true
            console.log("显示性质界定选择弹窗", " at pages/record/detail.uvue:1217")
        }
    }
    open var hideNaturePicker = ::gen_hideNaturePicker_fn
    open fun gen_hideNaturePicker_fn() {
        this.showNaturePopup = false
        console.log("隐藏性质界定选择弹窗", " at pages/record/detail.uvue:1223")
    }
    open var selectNature = ::gen_selectNature_fn
    open fun gen_selectNature_fn(index: Number) {
        console.log("选择性质界定，索引:", index, " at pages/record/detail.uvue:1227")
        this.selectedNatureIndex = index
        this.hideNaturePicker()
    }
    open var showProfessionPicker = ::gen_showProfessionPicker_fn
    open fun gen_showProfessionPicker_fn() {
        if (this.professionOptions.length > 0) {
            this.showProfessionPopup = true
            console.log("显示专业选择弹窗", " at pages/record/detail.uvue:1236")
        }
    }
    open var hideProfessionPicker = ::gen_hideProfessionPicker_fn
    open fun gen_hideProfessionPicker_fn() {
        this.showProfessionPopup = false
        console.log("隐藏专业选择弹窗", " at pages/record/detail.uvue:1242")
    }
    open var selectProfession = ::gen_selectProfession_fn
    open fun gen_selectProfession_fn(index: Number) {
        console.log("选择专业，索引:", index, " at pages/record/detail.uvue:1246")
        this.selectedProfessionIndex = index
        this.hideProfessionPicker()
    }
    open var showDepartmentPicker = ::gen_showDepartmentPicker_fn
    open fun gen_showDepartmentPicker_fn() {
        if (this.departmentOptions.length > 0) {
            this.showDepartmentPopup = true
            console.log("显示部门选择弹窗", " at pages/record/detail.uvue:1255")
        }
    }
    open var hideDepartmentPicker = ::gen_hideDepartmentPicker_fn
    open fun gen_hideDepartmentPicker_fn() {
        this.showDepartmentPopup = false
        console.log("隐藏部门选择弹窗", " at pages/record/detail.uvue:1261")
    }
    open var selectDepartment = ::gen_selectDepartment_fn
    open fun gen_selectDepartment_fn(index: Number) {
        console.log("选择部门，索引:", index, " at pages/record/detail.uvue:1265")
        this.selectedDepartmentIndex = index
        this.hideDepartmentPicker()
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
                return _uM("container" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 120, "paddingLeft" to 20, "backgroundColor" to "#f5f5f5")), "loading-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 60, "paddingRight" to 0, "paddingBottom" to 60, "paddingLeft" to 0)), "loading-text" to _pS(_uM("fontSize" to 16, "color" to "#666666")), "status-header" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 15, "paddingRight" to 15, "paddingBottom" to 15, "paddingLeft" to 15, "marginBottom" to 15, "boxShadow" to "0 1px 3px rgba(0,0,0,0.1)", "display" to "flex", "justifyContent" to "space-between", "alignItems" to "center")), "page-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333")), "record-status" to _pS(_uM("paddingTop" to 6, "paddingRight" to 12, "paddingBottom" to 6, "paddingLeft" to 12, "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "fontSize" to 14, "fontWeight" to "bold")), "status-pending" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#fff3cd", "color" to "#856404")), "status-processing" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#d1ecf1", "color" to "#0c5460")), "status-completed" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#d4edda", "color" to "#155724")), "status-accepted" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#e2e3e5", "color" to "#383d41")), "severity-low" to _pS(_uM("color" to "#28a745", "fontWeight" to "bold")), "severity-medium" to _pS(_uM("color" to "#ffc107", "fontWeight" to "bold")), "severity-high" to _pS(_uM("color" to "#fd7e14", "fontWeight" to "bold")), "severity-urgent" to _pS(_uM("color" to "#dc3545", "fontWeight" to "bold")), "error-state" to _pS(_uM("textAlign" to "center", "paddingTop" to 60, "paddingRight" to 0, "paddingBottom" to 60, "paddingLeft" to 0)), "error-text" to _pS(_uM("fontSize" to 16, "color" to "#666666")), "form-item" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "paddingTop" to 15, "paddingRight" to 15, "paddingBottom" to 15, "paddingLeft" to 15, "marginBottom" to 15, "boxShadow" to "0 1px 3px rgba(0,0,0,0.1)")), "form-label" to _pS(_uM("fontSize" to 16, "color" to "#333333", "marginBottom" to 10, "fontWeight" to "400")), "form-input-wrapper" to _pS(_uM("position" to "relative")), "form-display-text" to _pS(_uM("fontSize" to 16, "color" to "#333333", "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "backgroundImage" to "none", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "minHeight" to 20, "wordBreak" to "break-all")), "form-textarea" to _pS(_uM("width" to "100%", "minHeight" to 100, "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "backgroundImage" to "none", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "fontSize" to 16, "color" to "#333333", "boxSizing" to "border-box", "borderTopColor:focus" to "#007AFF", "borderRightColor:focus" to "#007AFF", "borderBottomColor:focus" to "#007AFF", "borderLeftColor:focus" to "#007AFF", "outline:focus" to "none")), "image-upload" to _pS(_uM("marginTop" to 10)), "image-list" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "10px")), "image-item" to _pS(_uM("position" to "relative", "width" to 80, "height" to 80)), "uploaded-image" to _pS(_uM("width" to "100%", "height" to "100%", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e0e0e0", "borderRightColor" to "#e0e0e0", "borderBottomColor" to "#e0e0e0", "borderLeftColor" to "#e0e0e0")), "image-delete" to _pS(_uM("position" to "absolute", "top" to -5, "right" to -5, "width" to 20, "height" to 20, "backgroundImage" to "none", "backgroundColor" to "#ff4757", "color" to "#FFFFFF", "borderTopLeftRadius" to 10, "borderTopRightRadius" to 10, "borderBottomRightRadius" to 10, "borderBottomLeftRadius" to 10, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "fontSize" to 12, "cursor" to "pointer")), "image-add" to _pS(_uM("width" to 80, "height" to 80, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "#fafafa")), "add-icon" to _pS(_uM("fontSize" to 24, "color" to "#999999", "marginBottom" to 5)), "add-text" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "status-selector" to _pS(_uM("display" to "flex", "flexDirection" to "row", "gap" to 12, "marginTop" to 15, "paddingTop" to 0, "paddingRight" to 5, "paddingBottom" to 0, "paddingLeft" to 5, "flexWrap" to "nowrap", "alignItems" to "center", "justifyContent" to "space-between")), "status-option" to _uM("" to _uM("flex" to 1, "height" to 60, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e8e8e8", "borderRightColor" to "#e8e8e8", "borderBottomColor" to "#e8e8e8", "borderLeftColor" to "#e8e8e8", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "0 2px 8px rgba(0, 0, 0, 0.08)", "transitionProperty" to "all", "transitionDuration" to "0.3s", "transitionTimingFunction" to "cubic-bezier(0.4,0,0.2,1)", "cursor" to "pointer", "position" to "relative", "overflow" to "hidden", "marginTop" to 0, "marginRight" to 6, "marginBottom" to 0, "marginLeft" to 6, "fontSize" to 14, "color" to "#333333", "textAlign" to "center", "content::before" to "''", "position::before" to "absolute", "top::before" to 0, "left::before" to 0, "right::before" to 0, "bottom::before" to 0, "backgroundImage::before" to "linear-gradient(135deg, rgba(255, 255, 255, 0.3) 0%, rgba(255, 255, 255, 0.1) 100%)", "backgroundColor::before" to "rgba(0,0,0,0)", "opacity::before" to 0, "transitionProperty::before" to "opacity", "transitionDuration::before" to "0.3s", "transitionTimingFunction::before" to "ease", "opacity:hover::before" to 1), ".pending" to _uM("borderTopColor" to "#ffa726", "borderRightColor" to "#ffa726", "borderBottomColor" to "#ffa726", "borderLeftColor" to "#ffa726", "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#e67e22"), ".pending.active" to _uM("borderTopColor" to "#ff9800", "borderRightColor" to "#ff9800", "borderBottomColor" to "#ff9800", "borderLeftColor" to "#ff9800", "backgroundImage" to "linear-gradient(135deg, #ff9800 0%, #ffa726 100%)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "0 4px 15px rgba(255, 152, 0, 0.3)", "transform" to "translateY(-2px)", "color" to "#FFFFFF"), ".completed" to _uM("borderTopColor" to "#66bb6a", "borderRightColor" to "#66bb6a", "borderBottomColor" to "#66bb6a", "borderLeftColor" to "#66bb6a", "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#27ae60"), ".completed.active" to _uM("borderTopColor" to "#4caf50", "borderRightColor" to "#4caf50", "borderBottomColor" to "#4caf50", "borderLeftColor" to "#4caf50", "backgroundImage" to "linear-gradient(135deg, #4caf50 0%, #66bb6a 100%)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "0 4px 15px rgba(76, 175, 80, 0.3)", "transform" to "translateY(-2px)", "color" to "#FFFFFF")), "save-button-wrapper" to _pS(_uM("position" to "fixed", "bottom" to 0, "left" to 0, "right" to 0, "backgroundImage" to "none", "backgroundColor" to "#ffffff", "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderTopWidth" to 1, "borderTopStyle" to "solid", "borderTopColor" to "#eeeeee")), "save-btn" to _pS(_uM("width" to "100%", "height" to 44, "backgroundImage" to "none", "backgroundColor" to "#007AFF", "color" to "#FFFFFF", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "fontSize" to 16, "fontWeight" to "bold", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#f8f9fa", "color:disabled" to "#6c757d", "borderTopWidth:disabled" to 1, "borderRightWidth:disabled" to 1, "borderBottomWidth:disabled" to 1, "borderLeftWidth:disabled" to 1, "borderTopStyle:disabled" to "solid", "borderRightStyle:disabled" to "solid", "borderBottomStyle:disabled" to "solid", "borderLeftStyle:disabled" to "solid", "borderTopColor:disabled" to "#dee2e6", "borderRightColor:disabled" to "#dee2e6", "borderBottomColor:disabled" to "#dee2e6", "borderLeftColor:disabled" to "#dee2e6", "opacity:disabled" to 0.6)), "custom-picker-full" to _uM("" to _uM("width" to "100%", "height" to 44, "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "backgroundImage" to "none", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "display" to "flex", "alignItems" to "center", "justifyContent" to "space-between", "boxSizing" to "border-box", "cursor" to "pointer"), ".disabled" to _uM("backgroundImage" to "none", "backgroundColor" to "#f5f5f5", "color" to "#999999", "cursor" to "not-allowed")), "picker-text-full" to _uM("" to _uM("fontSize" to 16, "color" to "#333333", "flex" to 1), ".placeholder" to _uM("color" to "#999999")), "popup-overlay" to _pS(_uM("position" to "fixed", "top" to 0, "left" to 0, "width" to "100%", "height" to "100%", "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0.5)", "zIndex" to 1000, "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "popup-content" to _pS(_uM("width" to "80%", "maxWidth" to 400, "backgroundImage" to "none", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "overflow" to "hidden")), "popup-header" to _pS(_uM("paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "display" to "flex", "alignItems" to "center", "justifyContent" to "space-between")), "popup-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333")), "popup-close" to _pS(_uM("fontSize" to 24, "color" to "#999999", "cursor" to "pointer", "lineHeight" to 1)), "popup-list" to _pS(_uM("maxHeight" to 300, "overflowY" to "auto")), "popup-item" to _pS(_uM("paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f8f9fa", "display" to "flex", "alignItems" to "center", "justifyContent" to "space-between", "cursor" to "pointer", "backgroundImage:hover" to "none", "backgroundColor:hover" to "#f8f9fa", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "popup-item-text" to _pS(_uM("fontSize" to 16, "color" to "#333333", "flex" to 1)), "popup-item-check" to _pS(_uM("fontSize" to 18, "color" to "#007AFF", "fontWeight" to "bold")), "@TRANSITION" to _uM("status-option" to _uM("property" to "all", "duration" to "0.3s", "timingFunction" to "cubic-bezier(0.4,0,0.2,1)", "property::before" to "opacity", "duration::before" to "0.3s", "timingFunction::before" to "ease")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
