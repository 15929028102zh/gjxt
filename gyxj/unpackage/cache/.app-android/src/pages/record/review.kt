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
import io.dcloud.uniapp.extapi.getSystemInfo as uni_getSystemInfo
import io.dcloud.uniapp.extapi.hideLoading as uni_hideLoading
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading
import io.dcloud.uniapp.extapi.showToast as uni_showToast
import io.dcloud.uniapp.extapi.uploadFile as uni_uploadFile
open class GenPagesRecordReview : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(options: UTSJSONObject) {
            this.recordId = options.getString("id") ?: ""
            val delayedParam = options.getString("delayed")
            console.log("URL参数 delayed:", delayedParam, " at pages/record/review.uvue:394")
            console.log("参数类型:", UTSAndroid.`typeof`(delayedParam), " at pages/record/review.uvue:395")
            if (delayedParam != null && delayedParam == "true") {
                this.isDelayed = true
                console.log("设置为延迟整改模式", " at pages/record/review.uvue:399")
            } else {
                this.isDelayed = false
                console.log("设置为正常模式", " at pages/record/review.uvue:402")
            }
            console.log("最终 isDelayed 值:", this.isDelayed, " at pages/record/review.uvue:404")
            val reviewedParam = options.getString("reviewed")
            console.log("URL参数 reviewed:", reviewedParam, " at pages/record/review.uvue:408")
            if (reviewedParam != null && reviewedParam == "true") {
                this.isReviewed = true
                console.log("设置为已复查记录模式", " at pages/record/review.uvue:411")
            } else {
                this.isReviewed = false
                console.log("设置为正常模式", " at pages/record/review.uvue:414")
            }
            console.log("最终 isReviewed 值:", this.isReviewed, " at pages/record/review.uvue:416")
            console.log("完整URL参数:", options, " at pages/record/review.uvue:417")
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
                            _cE("text", _uM("class" to "page-title"), "复查记录"),
                            _cE("view", _uM("class" to _nC(_uA(
                                "record-status",
                                _ctx.getStatusClass(_ctx.recordDetail?.status ?: 0)
                            ))), _uA(
                                _cE("text", null, _tD(_ctx.getStatusText(_ctx.recordDetail?.status ?: 0)), 1)
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
                            _cE("text", _uM("class" to "form-label"), "整改效果描述"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.recordDetail?.rectificationEffect ?: ""), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "📋 整改状态"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.getStatusText(_ctx.recordDetail?.status ?: 0)), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "📷 整改图片 (" + _tD(_ctx.rectificationImages.length) + "张)", 1),
                            _cE("view", _uM("class" to "image-upload"), _uA(
                                if (_ctx.rectificationImages.length > 0) {
                                    _cE("view", _uM("key" to 0, "class" to "image-list"), _uA(
                                        _cE(Fragment, null, RenderHelpers.renderList(_ctx.rectificationImages, fun(image, index, __index, _cached): Any {
                                            return _cE("view", _uM("class" to "image-item", "key" to index), _uA(
                                                _cE("image", _uM("src" to image, "mode" to "aspectFill", "class" to "uploaded-image", "onClick" to fun(){
                                                    _ctx.previewRectificationImage(index)
                                                }, "onError" to _ctx.onImageError, "onLoad" to _ctx.onImageLoad), null, 40, _uA(
                                                    "src",
                                                    "onClick",
                                                    "onError",
                                                    "onLoad"
                                                ))
                                            ))
                                        }), 128)
                                    ))
                                } else {
                                    _cE("view", _uM("key" to 1, "class" to "no-image-text"), _uA(
                                        _cE("text", null, "暂无整改图片")
                                    ))
                                }
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
                                _cE("view", _uM("class" to "form-display-text"), _tD(_ctx.formatDate(_ctx.recordDetail?.rectificationDate ?: "")), 1)
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "复查效果描述"),
                            _cE("view", _uM("class" to "form-input-wrapper"), _uA(
                                if (isTrue(!_ctx.isDelayed && !_ctx.isReviewed)) {
                                    _cE("textarea", _uM("key" to 0, "modelValue" to _ctx.reviewEffect, "onInput" to fun(`$event`: UniInputEvent){
                                        _ctx.reviewEffect = `$event`.detail.value
                                    }, "class" to "form-textarea", "placeholder" to "请描述复查效果...", "maxlength" to "500", "show-confirm-bar" to "false"), null, 40, _uA(
                                        "modelValue",
                                        "onInput"
                                    ))
                                } else {
                                    _cE("view", _uM("key" to 1, "class" to "form-display-text"), _tD(if (_ctx.reviewEffect.length > 0) {
                                        _ctx.reviewEffect
                                    } else {
                                        "暂无复查效果描述"
                                    }), 1)
                                }
                            ))
                        )),
                        _cE("view", _uM("class" to "form-item"), _uA(
                            _cE("text", _uM("class" to "form-label"), "📷 复查图片 (" + _tD(_ctx.reviewImages.length) + _tD(if ((_ctx.isDelayed || _ctx.isReviewed)) {
                                ""
                            } else {
                                "/3"
                            }) + ")", 1),
                            _cE("view", _uM("class" to "image-upload"), _uA(
                                if (_ctx.reviewImages.length > 0) {
                                    _cE("view", _uM("key" to 0, "class" to "image-list"), _uA(
                                        _cE(Fragment, null, RenderHelpers.renderList(_ctx.reviewImages, fun(image, index, __index, _cached): Any {
                                            return _cE("view", _uM("class" to "image-item", "key" to index), _uA(
                                                _cE("image", _uM("src" to image, "mode" to "aspectFill", "class" to "uploaded-image", "onClick" to fun(){
                                                    _ctx.previewReviewImage(index)
                                                }, "onError" to _ctx.onImageError, "onLoad" to _ctx.onImageLoad), null, 40, _uA(
                                                    "src",
                                                    "onClick",
                                                    "onError",
                                                    "onLoad"
                                                )),
                                                if (isTrue(!_ctx.isDelayed && !_ctx.isReviewed)) {
                                                    _cE("view", _uM("key" to 0, "class" to "image-delete", "onClick" to fun(){
                                                        _ctx.removeReviewImage(index)
                                                    }), _uA(
                                                        _cE("text", null, "×")
                                                    ), 8, _uA(
                                                        "onClick"
                                                    ))
                                                } else {
                                                    _cC("v-if", true)
                                                }
                                            ))
                                        }), 128),
                                        if (isTrue(!_ctx.isDelayed && !_ctx.isReviewed && _ctx.reviewImages.length < 3)) {
                                            _cE("view", _uM("key" to 0, "class" to "image-add", "onClick" to _ctx.chooseReviewImage), _uA(
                                                _cE("text", _uM("class" to "add-icon"), "+"),
                                                _cE("text", _uM("class" to "add-text"), "添加图片")
                                            ), 8, _uA(
                                                "onClick"
                                            ))
                                        } else {
                                            _cC("v-if", true)
                                        }
                                    ))
                                } else {
                                    _cE("view", _uM("key" to 1, "class" to "no-image-container"), _uA(
                                        if (isTrue(!_ctx.isDelayed && !_ctx.isReviewed)) {
                                            _cE("view", _uM("key" to 0, "class" to "image-add", "onClick" to _ctx.chooseReviewImage), _uA(
                                                _cE("text", _uM("class" to "add-icon"), "+"),
                                                _cE("text", _uM("class" to "add-text"), "添加图片")
                                            ), 8, _uA(
                                                "onClick"
                                            ))
                                        } else {
                                            _cC("v-if", true)
                                        }
                                    ))
                                }
                            ))
                        )),
                        if (isTrue(!_ctx.isDelayed && !_ctx.isReviewed)) {
                            _cE("view", _uM("key" to 4, "class" to "save-button-wrapper"), _uA(
                                _cE("button", _uM("class" to "save-btn", "onClick" to _ctx.saveReviewInfo, "disabled" to _ctx.saving), _tD(if (_ctx.saving) {
                                    "保存中..."
                                } else {
                                    "确认复查"
                                }), 9, _uA(
                                    "onClick",
                                    "disabled"
                                ))
                            ))
                        } else {
                            _cC("v-if", true)
                        }
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
    open var recordDetail: RecordDetail1? by `$data`
    open var imageList: UTSArray<String> by `$data`
    open var rectificationImages: UTSArray<String> by `$data`
    open var loading: Boolean by `$data`
    open var reviewEffect: String by `$data`
    open var reviewImages: UTSArray<String> by `$data`
    open var saving: Boolean by `$data`
    open var scrollTop: Number by `$data`
    open var containerHeight: Number by `$data`
    open var natureOptions: UTSArray<NatureOption2> by `$data`
    open var professionOptions: UTSArray<ProfessionOption2> by `$data`
    open var departmentOptions: UTSArray<DepartmentOption2> by `$data`
    open var selectedNatureIndex: Number by `$data`
    open var selectedProfessionIndex: Number by `$data`
    open var selectedDepartmentIndex: Number by `$data`
    open var showNaturePopup: Boolean by `$data`
    open var showProfessionPopup: Boolean by `$data`
    open var showDepartmentPopup: Boolean by `$data`
    open var loadedDataCount: Number by `$data`
    open var totalDataCount: Number by `$data`
    open var isDelayed: Boolean by `$data`
    open var isReviewed: Boolean by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("recordId" to "", "recordDetail" to null as RecordDetail1?, "imageList" to _uA<String>(), "rectificationImages" to _uA<String>(), "loading" to true, "reviewEffect" to "", "reviewImages" to _uA<String>(), "saving" to false, "scrollTop" to 0, "containerHeight" to 0, "natureOptions" to _uA<NatureOption2>(), "professionOptions" to _uA<ProfessionOption2>(), "departmentOptions" to _uA<DepartmentOption2>(), "selectedNatureIndex" to -1, "selectedProfessionIndex" to -1, "selectedDepartmentIndex" to -1, "showNaturePopup" to false, "showProfessionPopup" to false, "showDepartmentPopup" to false, "loadedDataCount" to 0, "totalDataCount" to 4, "isDelayed" to false, "isReviewed" to false)
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
        console.log("\u6570\u636E\u52A0\u8F7D\u8FDB\u5EA6: " + this.loadedDataCount + "/" + this.totalDataCount, " at pages/record/review.uvue:451")
        if (this.loadedDataCount >= this.totalDataCount) {
            console.log("所有数据加载完成，开始回写选中值", " at pages/record/review.uvue:454")
            console.log("当前数据状态:", _uO("recordDetail" to this.recordDetail, "natureOptionsLength" to this.natureOptions.length, "professionOptionsLength" to this.professionOptions.length, "departmentOptionsLength" to this.departmentOptions.length, "natureOptions" to this.natureOptions, "professionOptions" to this.professionOptions, "departmentOptions" to this.departmentOptions), " at pages/record/review.uvue:455")
            setTimeout(fun(){
                console.log("开始执行 initSelectedValues", " at pages/record/review.uvue:466")
                this.initSelectedValues()
                this.loading = false
                console.log("加载状态设置为 false", " at pages/record/review.uvue:469")
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
                console.log("后端返回的完整数据:", recordData, " at pages/record/review.uvue:486")
                console.log("性质界定相关字段调试:", " at pages/record/review.uvue:489")
                console.log("natureDefinition:", recordData?.getString("natureDefinition"), " at pages/record/review.uvue:490")
                console.log("natureId:", recordData?.getString("natureId"), " at pages/record/review.uvue:491")
                console.log("natureName:", recordData?.getString("natureName"), " at pages/record/review.uvue:492")
                console.log("nature:", recordData?.getString("nature"), " at pages/record/review.uvue:493")
                console.log("natureType:", recordData?.getString("natureType"), " at pages/record/review.uvue:494")
                if (recordData != null) {
                    this.recordDetail = RecordDetail1(id = recordData.getString("id") ?: "", problemDescription = recordData.getString("problemDescription") ?: "", locationName = recordData.getString("locationName") ?: "", severity = recordData.getNumber("severity") ?: 1, taskName = recordData.getString("taskName") ?: "", reporterName = recordData.getString("reporterName") ?: "", createTime = recordData.getString("createTime") ?: "", updateTime = recordData.getString("updateTime") ?: "", status = recordData.getNumber("status") ?: 0, problemImageUrls = recordData.getString("problemImageUrls") ?: "", suggestions = recordData.getString("suggestions") ?: "", reporterId = recordData.getString("reporterId") ?: "", responsibleUnit = recordData.getString("responsibleUnit") ?: "", responsiblePerson = recordData.getString("responsiblePerson") ?: "", natureId = "", natureName = "", assessmentSituation = recordData.getString("assessmentSituation") ?: "", assessmentAmount = recordData.getNumber("assessmentAmount") ?: 0, safetyScore = recordData.getNumber("safetyScore") ?: 0, professionId = recordData.getString("professionId") ?: "", professionName = recordData.getString("professionName") ?: "", inspector = recordData.getString("inspectorName") ?: "", groupLeader = recordData.getString("groupLeaderName") ?: "", deputyLeader = recordData.getString("deputyLeaderName") ?: "", departmentId = recordData.getString("departmentId") ?: "", departmentName = recordData.getString("departmentName") ?: "", rectificationDate = recordData.getString("rectificationDeadline") ?: "", location = recordData.getString("location") ?: "", rectificationEffect = recordData.getString("rectificationEffect") ?: "", rectificationImageUrls = recordData.getString("rectificationImageUrls") ?: "", rectificationTime = recordData.getString("rectificationTime") ?: "", reviewEffect = recordData.getString("reviewEffect") ?: "", reviewImageUrls = recordData.getString("reviewImageUrls") ?: "")
                    val natureDefinitionValue = recordData.getString("natureDefinition") ?: ""
                    console.log("natureDefinition原始值:", natureDefinitionValue, " at pages/record/review.uvue:538")
                    if (natureDefinitionValue.length === 32 && UTSRegExp("^[a-f0-9]+\$", "i").test(natureDefinitionValue)) {
                        this.recordDetail!!.natureId = natureDefinitionValue
                        console.log("检测到ID格式，作为ID处理:", natureDefinitionValue, " at pages/record/review.uvue:545")
                    } else {
                        this.recordDetail!!.natureName = natureDefinitionValue
                        console.log("检测到名称格式，作为名称处理:", natureDefinitionValue, " at pages/record/review.uvue:549")
                    }
                    console.log("数据映射结果调试:", " at pages/record/review.uvue:553")
                    console.log("recordDetail.natureId:", this.recordDetail!!.natureId, " at pages/record/review.uvue:554")
                    console.log("recordDetail.natureName:", this.recordDetail!!.natureName, " at pages/record/review.uvue:555")
                    this.reviewEffect = this.recordDetail?.reviewEffect ?: ""
                    val imageUrls = this.recordDetail?.problemImageUrls
                    if (imageUrls != null && imageUrls != "") {
                        this.imageList = this.processImageUrls(imageUrls as String)
                    }
                    val rectificationImages = this.recordDetail?.rectificationImageUrls
                    console.log("调试：整改图片原始数据", rectificationImages, " at pages/record/review.uvue:568")
                    if (rectificationImages != null && rectificationImages != "") {
                        this.rectificationImages = this.processImageUrls(rectificationImages as String)
                        console.log("调试：最终的整改图片URL列表", this.rectificationImages, " at pages/record/review.uvue:571")
                    } else {
                        console.log("调试：整改图片数据为空", " at pages/record/review.uvue:573")
                    }
                    val reviewImages = this.recordDetail?.reviewImageUrls
                    console.log("调试：复查图片原始数据", reviewImages, " at pages/record/review.uvue:578")
                    if (reviewImages != null && reviewImages != "") {
                        this.reviewImages = this.processImageUrls(reviewImages as String)
                        console.log("调试：最终的复查图片URL列表", this.reviewImages, " at pages/record/review.uvue:581")
                    } else {
                        console.log("调试：复查图片数据为空", " at pages/record/review.uvue:583")
                    }
                }
            } else {
                uni_showToast(ShowToastOptions(title = data.getString("message") ?: "加载失败", icon = "none"))
            }
            this.checkAllDataLoaded()
        }
        , fail = fun(error){
            console.error("加载记录详情失败:", error, " at pages/record/review.uvue:596")
            uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
            this.checkAllDataLoaded()
        }
        ))
    }
    open var chooseReviewImage = ::gen_chooseReviewImage_fn
    open fun gen_chooseReviewImage_fn() {
        if (this.reviewImages.length >= 3) {
            uni_showToast(ShowToastOptions(title = "最多只能上传3张图片", icon = "none"))
            return
        }
        uni_chooseImage(ChooseImageOptions(count = 3 - this.reviewImages.length, sourceType = _uA(
            "camera",
            "album"
        ), success = fun(res){
            val tempFilePaths = res.tempFilePaths
            run {
                var i: Number = 0
                while(i < tempFilePaths.length){
                    this.reviewImages.push(tempFilePaths[i])
                    i++
                }
            }
        }
        ))
    }
    open var previewReviewImage = ::gen_previewReviewImage_fn
    open fun gen_previewReviewImage_fn(index: Number) {
        uni_previewImage(PreviewImageOptions(current = index, urls = this.reviewImages))
    }
    open var removeReviewImage = ::gen_removeReviewImage_fn
    open fun gen_removeReviewImage_fn(index: Number) {
        this.reviewImages.splice(index, 1)
    }
    open var previewImage = ::gen_previewImage_fn
    open fun gen_previewImage_fn(index: Number) {
        uni_previewImage(PreviewImageOptions(current = index, urls = this.imageList))
    }
    open var previewRectificationImage = ::gen_previewRectificationImage_fn
    open fun gen_previewRectificationImage_fn(index: Number) {
        uni_previewImage(PreviewImageOptions(current = index, urls = this.rectificationImages))
    }
    open var formatDate = ::gen_formatDate_fn
    open fun gen_formatDate_fn(dateTimeStr: String): String {
        if (dateTimeStr == null || dateTimeStr == "") {
            return ""
        }
        if (dateTimeStr.includes(" ")) {
            return dateTimeStr.split(" ")[0]
        }
        return dateTimeStr
    }
    open var onImageLoad = ::gen_onImageLoad_fn
    open fun gen_onImageLoad_fn(e: Any) {}
    open var onImageError = ::gen_onImageError_fn
    open fun gen_onImageError_fn(e: Any) {
        uni_showToast(ShowToastOptions(title = "图片加载失败", icon = "none"))
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
                val parsed = UTSAndroid.consoleDebugError(JSON.parse(imageUrls), " at pages/record/review.uvue:694") as UTSArray<String>
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
            console.log("JSON解析失败，尝试按逗号分隔处理:", e, " at pages/record/review.uvue:698")
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
    open var saveReviewInfo = ::gen_saveReviewInfo_fn
    open fun gen_saveReviewInfo_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend w@{
                if (this.saving) {
                    return@w
                }
                if (this.reviewEffect.trim().length === 0) {
                    uni_showToast(ShowToastOptions(title = "请填写复查效果", icon = "none"))
                    return@w
                }
                this.saving = true
                try {
                    var reviewImageUrls: UTSArray<String> = _uA()
                    if (this.reviewImages.length > 0) {
                        uni_showLoading(ShowLoadingOptions(title = "上传复查图片中..."))
                        reviewImageUrls = await(this.uploadReviewImages())
                        uni_hideLoading()
                    }
                    val userInfoStr = uni_getStorageSync("userInfo") as String
                    console.log("获取到的用户信息字符串:", userInfoStr, " at pages/record/review.uvue:735")
                    var currentUserId = ""
                    if (userInfoStr != null && userInfoStr != "") {
                        try {
                            val userInfo = UTSAndroid.consoleDebugError(JSON.parse(userInfoStr), " at pages/record/review.uvue:739") as UTSJSONObject
                            console.log("解析后的用户信息对象:", userInfo, " at pages/record/review.uvue:740")
                            currentUserId = userInfo.getString("id") ?: ""
                            console.log("当前复查人ID:", currentUserId, " at pages/record/review.uvue:742")
                            if (currentUserId === "") {
                                console.log("用户信息对象:", JSON.stringify(userInfo), " at pages/record/review.uvue:744")
                            }
                        } catch (e: Throwable) {
                            console.error("解析用户信息失败:", e, " at pages/record/review.uvue:747")
                        }
                    } else {
                        console.log("用户信息字符串为空或null", " at pages/record/review.uvue:750")
                    }
                    val updateData: UTSJSONObject = _uO("__\$originalPosition" to UTSSourceMapPosition("updateData", "pages/record/review.uvue", 753, 11), "id" to (this.recordDetail?.id ?: ""), "problemDescription" to (this.recordDetail?.problemDescription ?: ""), "responsibleUnit" to (this.recordDetail?.responsibleUnit ?: ""), "responsiblePerson" to (this.recordDetail?.responsiblePerson ?: ""), "natureDefinition" to if (this.selectedNatureIndex !== -1) {
                        this.natureOptions[this.selectedNatureIndex].name
                    } else {
                        (this.recordDetail?.natureId ?: "")
                    }
                    , "rectificationEffect" to (this.recordDetail?.rectificationEffect ?: ""), "rectificationImageUrls" to (this.recordDetail?.rectificationImageUrls ?: ""), "reviewEffect" to this.reviewEffect, "reviewImageUrls" to reviewImageUrls.join(","), "reviewerId" to currentUserId, "status" to 2, "reviewTime" to Date().toISOString().replace("T", " ").substring(0, 19))
                    uni_showLoading(ShowLoadingOptions(title = "保存中..."))
                    val token = uni_getStorageSync("token")
                    uni_request<Any>(RequestOptions(url = getApiUrl("/inspection-record"), method = "PUT", header = object : UTSJSONObject() {
                        var Authorization = "Bearer " + token
                        var `Content-Type` = "application/json"
                    }, data = updateData, success = fun(response){
                        uni_hideLoading()
                        val data = response.data as UTSJSONObject
                        if (response.statusCode === 200 && (data.getNumber("code") ?: 0) === 200) {
                            uni_showToast(ShowToastOptions(title = "复查成功", icon = "success"))
                            setTimeout(fun(){
                                uni_navigateBack(null)
                            }, 1500)
                        } else {
                            uni_showToast(ShowToastOptions(title = data.getString("message") ?: "复查失败", icon = "none"))
                        }
                        this.saving = false
                    }
                    , fail = fun(error){
                        uni_hideLoading()
                        console.error("保存复查信息失败:", error, " at pages/record/review.uvue:803")
                        uni_showToast(ShowToastOptions(title = "网络错误", icon = "none"))
                        this.saving = false
                    }
                    ))
                }
                 catch (error: Throwable) {
                    uni_hideLoading()
                    console.error("上传图片失败:", error, " at pages/record/review.uvue:813")
                    uni_showToast(ShowToastOptions(title = "图片上传失败，请重试", icon = "none"))
                    this.saving = false
                }
        })
    }
    open var uploadReviewImages = ::gen_uploadReviewImages_fn
    open fun gen_uploadReviewImages_fn(): UTSPromise<UTSArray<String>> {
        return wrapUTSPromise(suspend w@{
                if (this.reviewImages.length === 0) {
                    return@w _uA()
                }
                val token = uni_getStorageSync("token")
                val uploadPromises = this.reviewImages.map(fun(imagePath): UTSPromise<String> {
                    return UTSPromise<String>(fun(resolve, reject){
                        uni_uploadFile(UploadFileOptions(url = getApiUrl("/upload/image"), filePath = imagePath, name = "file", header = object : UTSJSONObject() {
                            var Authorization = "Bearer " + token
                        }, success = fun(res){
                            try {
                                val data = UTSAndroid.consoleDebugError(JSON.parse(res.data), " at pages/record/review.uvue:839") as UTSJSONObject
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
                    console.log("所有复查图片上传成功：", imageUrls, " at pages/record/review.uvue:864")
                    return@w imageUrls
                }
                 catch (error: Throwable) {
                    console.error("复查图片上传失败:", error, " at pages/record/review.uvue:867")
                    throw error
                }
        })
    }
    open var getStatusText = ::gen_getStatusText_fn
    open fun gen_getStatusText_fn(status: Number): String {
        when (status) {
            0 -> 
                return "待整改"
            1 -> 
                return "已整改"
            2 -> 
                return "已复查"
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
                return "status-completed"
            2 -> 
                return "status-reviewed"
            else -> 
                return "status-unknown"
        }
    }
    open var initSelectedValues = ::gen_initSelectedValues_fn
    open fun gen_initSelectedValues_fn() {
        console.log("开始初始化选中值", " at pages/record/review.uvue:892")
        if (this.recordDetail == null) {
            console.log("recordDetail为空，无法初始化选中值", " at pages/record/review.uvue:894")
            return
        }
        console.log("记录详情数据:", this.recordDetail, " at pages/record/review.uvue:898")
        console.log("字典数据状态:", _uO("natureOptions" to this.natureOptions, "professionOptions" to this.professionOptions, "departmentOptions" to this.departmentOptions), " at pages/record/review.uvue:899")
        val natureId = this.recordDetail!!.natureId ?: ""
        val natureName = this.recordDetail!!.natureName ?: ""
        console.log("性质界定数据:", _uO("natureId" to natureId, "natureName" to natureName), " at pages/record/review.uvue:908")
        if (natureId !== "") {
            console.log("使用ID匹配:", natureId, " at pages/record/review.uvue:912")
            run {
                var i: Number = 0
                while(i < this.natureOptions.length){
                    console.log("\u6027\u8D28\u754C\u5B9AID\u6BD4\u8F83: " + this.natureOptions[i].id + " == " + natureId, " at pages/record/review.uvue:914")
                    if (this.natureOptions[i].id == natureId) {
                        this.selectedNatureIndex = i
                        this.recordDetail!!.natureName = this.natureOptions[i].name
                        console.log("性质界定ID匹配成功，索引:", i, "ID:", this.natureOptions[i].id, "名称:", this.natureOptions[i].name, " at pages/record/review.uvue:918")
                        break
                    }
                    i++
                }
            }
            if (this.selectedNatureIndex === -1) {
                console.log("性质界定ID在字典中未找到匹配项:", natureId, " at pages/record/review.uvue:923")
            }
        } else if (natureName !== "") {
            console.log("使用名称匹配:", natureName, " at pages/record/review.uvue:927")
            run {
                var i: Number = 0
                while(i < this.natureOptions.length){
                    console.log("\u6027\u8D28\u754C\u5B9A\u540D\u79F0\u6BD4\u8F83: " + this.natureOptions[i].name + " == " + natureName, " at pages/record/review.uvue:929")
                    if (this.natureOptions[i].name == natureName) {
                        this.selectedNatureIndex = i
                        this.recordDetail!!.natureId = this.natureOptions[i].id
                        console.log("性质界定名称匹配成功，索引:", i, "ID:", this.natureOptions[i].id, "名称:", this.natureOptions[i].name, " at pages/record/review.uvue:933")
                        break
                    }
                    i++
                }
            }
            if (this.selectedNatureIndex === -1) {
                console.log("性质界定名称在字典中未找到匹配项:", natureName, " at pages/record/review.uvue:938")
            }
        } else {
            console.log("性质界定ID和名称都为空，跳过匹配", " at pages/record/review.uvue:941")
        }
        if (this.selectedNatureIndex === -1) {
            console.log("可用的字典选项:", this.natureOptions.map(fun(item): String {
                return "" + item.name + "(" + item.id + ")"
            }
            ), " at pages/record/review.uvue:945")
        }
        val professionId = this.recordDetail!!.professionId ?: ""
        console.log("专业ID:", professionId, " at pages/record/review.uvue:950")
        if (professionId !== "") {
            run {
                var i: Number = 0
                while(i < this.professionOptions.length){
                    console.log("\u4E13\u4E1A\u6BD4\u8F83: " + this.professionOptions[i].id + " == " + professionId, " at pages/record/review.uvue:953")
                    if (this.professionOptions[i].id == professionId) {
                        this.selectedProfessionIndex = i
                        console.log("专业匹配成功，索引:", i, "名称:", this.professionOptions[i].name, " at pages/record/review.uvue:956")
                        break
                    }
                    i++
                }
            }
        } else {
            console.log("专业ID为空，跳过匹配", " at pages/record/review.uvue:961")
        }
        val departmentId = this.recordDetail!!.departmentId ?: ""
        console.log("部门ID:", departmentId, " at pages/record/review.uvue:966")
        if (departmentId !== "") {
            run {
                var i: Number = 0
                while(i < this.departmentOptions.length){
                    console.log("\u90E8\u95E8\u6BD4\u8F83: " + this.departmentOptions[i].id + " == " + departmentId, " at pages/record/review.uvue:969")
                    if (this.departmentOptions[i].id == departmentId) {
                        this.selectedDepartmentIndex = i
                        console.log("部门匹配成功，索引:", i, "名称:", this.departmentOptions[i].name, " at pages/record/review.uvue:972")
                        break
                    }
                    i++
                }
            }
        } else {
            console.log("部门ID为空，跳过匹配", " at pages/record/review.uvue:977")
        }
        console.log("回写选中值完成:", _uO("natureId" to natureId, "selectedNatureIndex" to this.selectedNatureIndex, "professionId" to professionId, "selectedProfessionIndex" to this.selectedProfessionIndex, "departmentId" to departmentId, "selectedDepartmentIndex" to this.selectedDepartmentIndex), " at pages/record/review.uvue:980")
    }
    open var loadNatureOptions = ::gen_loadNatureOptions_fn
    open fun gen_loadNatureOptions_fn() {
        val token = uni_getStorageSync("token")
        console.log("开始加载性质界定数据", " at pages/record/review.uvue:990")
        uni_request<Any>(RequestOptions(url = getApiUrl("/dictionary/type/性质界定"), method = "GET", header = object : UTSJSONObject() {
            var Authorization = "Bearer " + token
            var `Content-Type` = "application/json"
        }, success = fun(res){
            val data = res.data as UTSJSONObject
            console.log("性质界定接口响应:", res.statusCode, data.getNumber("code"), " at pages/record/review.uvue:1000")
            if (res.statusCode === 200 && data.getNumber("code") === 200) {
                val dataArray = data.getArray("data")
                console.log("性质界定原始数据:", dataArray, " at pages/record/review.uvue:1003")
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
                            console.log("\u6027\u8D28\u754C\u5B9A\u9879\u76EE " + i + ":", _uO("id" to id, "dictValue" to dictValue, "dictLabel" to dictLabel, "name" to name), " at pages/record/review.uvue:1012")
                            this.natureOptions.push(NatureOption2(id = id, name = name))
                            i++
                        }
                    }
                }
                console.log("性质界定数据加载成功:", this.natureOptions, " at pages/record/review.uvue:1016")
            } else {
                val message = data.getString("message") ?: "未知错误"
                console.error("获取性质界定数据失败:", message, " at pages/record/review.uvue:1019")
                this.natureOptions = _uA(
                    NatureOption2(id = "1", name = "一般问题"),
                    NatureOption2(id = "2", name = "较大问题"),
                    NatureOption2(id = "3", name = "严重问题"),
                    NatureOption2(id = "4", name = "重大问题")
                )
                console.log("使用默认性质界定数据:", this.natureOptions, " at pages/record/review.uvue:1026")
            }
            this.checkAllDataLoaded()
        }
        , fail = fun(error){
            console.error("请求性质界定数据失败:", error, " at pages/record/review.uvue:1031")
            this.natureOptions = _uA(
                NatureOption2(id = "1", name = "一般问题"),
                NatureOption2(id = "2", name = "较大问题"),
                NatureOption2(id = "3", name = "严重问题"),
                NatureOption2(id = "4", name = "重大问题")
            )
            console.log("使用默认性质界定数据:", this.natureOptions, " at pages/record/review.uvue:1038")
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
            if (res.statusCode === 200 && data.getNumber("code") === 200) {
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
                            this.professionOptions.push(ProfessionOption2(id = id, name = name))
                            i++
                        }
                    }
                }
            } else {
                this.professionOptions = _uA(
                    ProfessionOption2(id = "1", name = "机械"),
                    ProfessionOption2(id = "2", name = "电气"),
                    ProfessionOption2(id = "3", name = "仪表"),
                    ProfessionOption2(id = "4", name = "工艺"),
                    ProfessionOption2(id = "5", name = "安全"),
                    ProfessionOption2(id = "6", name = "环保")
                )
            }
            this.checkAllDataLoaded()
        }
        , fail = fun(error){
            this.professionOptions = _uA(
                ProfessionOption2(id = "1", name = "机械"),
                ProfessionOption2(id = "2", name = "电气"),
                ProfessionOption2(id = "3", name = "仪表"),
                ProfessionOption2(id = "4", name = "工艺"),
                ProfessionOption2(id = "5", name = "安全"),
                ProfessionOption2(id = "6", name = "环保")
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
            if (res.statusCode === 200 && data.getNumber("code") === 200) {
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
                                this.departmentOptions.push(DepartmentOption2(id = id, name = deptName))
                            }
                            i++
                        }
                    }
                }
            } else {
                this.departmentOptions = _uA(
                    DepartmentOption2(id = "1", name = "生产部"),
                    DepartmentOption2(id = "2", name = "安全部"),
                    DepartmentOption2(id = "3", name = "设备部"),
                    DepartmentOption2(id = "4", name = "技术部"),
                    DepartmentOption2(id = "5", name = "质量部"),
                    DepartmentOption2(id = "6", name = "环保部"),
                    DepartmentOption2(id = "7", name = "行政部")
                )
            }
            this.checkAllDataLoaded()
        }
        , fail = fun(error){
            this.departmentOptions = _uA(
                DepartmentOption2(id = "1", name = "生产部"),
                DepartmentOption2(id = "2", name = "安全部"),
                DepartmentOption2(id = "3", name = "设备部"),
                DepartmentOption2(id = "4", name = "技术部"),
                DepartmentOption2(id = "5", name = "质量部"),
                DepartmentOption2(id = "6", name = "环保部"),
                DepartmentOption2(id = "7", name = "行政部")
            )
            this.checkAllDataLoaded()
        }
        ))
    }
    open var showNaturePicker = ::gen_showNaturePicker_fn
    open fun gen_showNaturePicker_fn() {
        if (this.natureOptions.length > 0) {
            this.showNaturePopup = true
        }
    }
    open var hideNaturePicker = ::gen_hideNaturePicker_fn
    open fun gen_hideNaturePicker_fn() {
        this.showNaturePopup = false
    }
    open var selectNature = ::gen_selectNature_fn
    open fun gen_selectNature_fn(index: Number) {
        this.selectedNatureIndex = index
        this.hideNaturePicker()
    }
    open var showProfessionPicker = ::gen_showProfessionPicker_fn
    open fun gen_showProfessionPicker_fn() {
        if (this.professionOptions.length > 0) {
            this.showProfessionPopup = true
        }
    }
    open var hideProfessionPicker = ::gen_hideProfessionPicker_fn
    open fun gen_hideProfessionPicker_fn() {
        this.showProfessionPopup = false
    }
    open var selectProfession = ::gen_selectProfession_fn
    open fun gen_selectProfession_fn(index: Number) {
        this.selectedProfessionIndex = index
        this.hideProfessionPicker()
    }
    open var showDepartmentPicker = ::gen_showDepartmentPicker_fn
    open fun gen_showDepartmentPicker_fn() {
        if (this.departmentOptions.length > 0) {
            this.showDepartmentPopup = true
        }
    }
    open var hideDepartmentPicker = ::gen_hideDepartmentPicker_fn
    open fun gen_hideDepartmentPicker_fn() {
        this.showDepartmentPopup = false
    }
    open var selectDepartment = ::gen_selectDepartment_fn
    open fun gen_selectDepartment_fn(index: Number) {
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
                return _uM("container" to _pS(_uM("paddingTop" to 0, "paddingRight" to 0, "paddingBottom" to 0, "paddingLeft" to 0, "backgroundColor" to "#f5f5f5", "display" to "flex", "flexDirection" to "column")), "loading-state" to _pS(_uM("flex" to 1, "display" to "flex", "justifyContent" to "center", "alignItems" to "center")), "loading-text" to _pS(_uM("fontSize" to 16, "color" to "#666666")), "status-header" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#e0e0e0", "display" to "flex", "justifyContent" to "space-between", "alignItems" to "center")), "page-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333")), "record-status" to _pS(_uM("paddingTop" to 5, "paddingRight" to 12, "paddingBottom" to 5, "paddingLeft" to 12, "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "fontSize" to 14)), "status-pending" to _pS(_uM("backgroundColor" to "#fff3cd", "color" to "#856404")), "status-completed" to _pS(_uM("backgroundColor" to "#d1ecf1", "color" to "#0c5460")), "status-reviewed" to _pS(_uM("backgroundColor" to "#d4edda", "color" to "#155724")), "form-item" to _pS(_uM("backgroundImage" to "none", "backgroundColor" to "#ffffff", "paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0")), "form-label" to _pS(_uM("fontSize" to 16, "color" to "#333333", "marginBottom" to 10)), "form-input-wrapper" to _pS(_uM("marginTop" to 8)), "form-display-text" to _pS(_uM("fontSize" to 15, "color" to "#666666", "lineHeight" to 1.5)), "form-textarea" to _pS(_uM("width" to "100%", "minHeight" to 80, "paddingTop" to 10, "paddingRight" to 10, "paddingBottom" to 10, "paddingLeft" to 10, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e0e0e0", "borderRightColor" to "#e0e0e0", "borderBottomColor" to "#e0e0e0", "borderLeftColor" to "#e0e0e0", "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "fontSize" to 15, "lineHeight" to 1.5, "color" to "#333333", "backgroundColor" to "#ffffff")), "image-upload" to _pS(_uM("marginTop" to 10)), "image-list" to _pS(_uM("display" to "flex", "flexWrap" to "wrap", "gap" to "10px")), "image-item" to _pS(_uM("position" to "relative", "width" to 80, "height" to 80)), "uploaded-image" to _pS(_uM("width" to "100%", "height" to "100%", "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e0e0e0", "borderRightColor" to "#e0e0e0", "borderBottomColor" to "#e0e0e0", "borderLeftColor" to "#e0e0e0")), "image-delete" to _pS(_uM("position" to "absolute", "top" to -8, "right" to -8, "width" to 20, "height" to 20, "backgroundColor" to "#ff4757", "display" to "flex", "justifyContent" to "center", "alignItems" to "center", "color" to "#FFFFFF", "fontSize" to 14, "lineHeight" to 1)), "image-add" to _pS(_uM("width" to 80, "height" to 80, "borderTopWidth" to 2, "borderRightWidth" to 2, "borderBottomWidth" to 2, "borderLeftWidth" to 2, "borderTopStyle" to "dashed", "borderRightStyle" to "dashed", "borderBottomStyle" to "dashed", "borderLeftStyle" to "dashed", "borderTopColor" to "#cccccc", "borderRightColor" to "#cccccc", "borderBottomColor" to "#cccccc", "borderLeftColor" to "#cccccc", "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "display" to "flex", "flexDirection" to "column", "justifyContent" to "center", "alignItems" to "center", "backgroundColor" to "#fafafa")), "add-icon" to _pS(_uM("fontSize" to 24, "color" to "#999999", "marginBottom" to 5)), "add-text" to _pS(_uM("fontSize" to 12, "color" to "#999999")), "no-image-container" to _pS(_uM("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "gap" to "15px")), "no-image-text" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "textAlign" to "center", "color" to "#999999", "fontSize" to 14, "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 6, "borderTopRightRadius" to 6, "borderBottomRightRadius" to 6, "borderBottomLeftRadius" to 6, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "width" to "100%")), "save-button-wrapper" to _pS(_uM("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "backgroundImage" to "none", "backgroundColor" to "#ffffff")), "save-btn" to _pS(_uM("width" to "100%", "paddingTop" to 12, "paddingRight" to 12, "paddingBottom" to 12, "paddingLeft" to 12, "backgroundColor" to "#007AFF", "color" to "#FFFFFF", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "fontSize" to 16, "backgroundColor:disabled" to "#cccccc", "color:disabled" to "#999999")), "delayed-mode-tip" to _pS(_uM("marginTop" to "20rpx", "marginRight" to "20rpx", "marginBottom" to "20rpx", "marginLeft" to "20rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "backgroundColor" to "#fff3cd", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#ffeaa7", "borderRightColor" to "#ffeaa7", "borderBottomColor" to "#ffeaa7", "borderLeftColor" to "#ffeaa7", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "textAlign" to "center")), "tip-text" to _pS(_uM("color" to "#856404", "fontSize" to "28rpx", "fontWeight" to "bold")), "error-state" to _pS(_uM("flex" to 1, "display" to "flex", "justifyContent" to "center", "alignItems" to "center")), "error-text" to _pS(_uM("fontSize" to 16, "color" to "#999999")), "custom-picker-full" to _uM("" to _uM("width" to "100%", "height" to 44, "paddingTop" to 12, "paddingRight" to 15, "paddingBottom" to 12, "paddingLeft" to 15, "backgroundImage" to "none", "backgroundColor" to "#f8f9fa", "borderTopLeftRadius" to 8, "borderTopRightRadius" to 8, "borderBottomRightRadius" to 8, "borderBottomLeftRadius" to 8, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e9ecef", "borderRightColor" to "#e9ecef", "borderBottomColor" to "#e9ecef", "borderLeftColor" to "#e9ecef", "display" to "flex", "alignItems" to "center", "justifyContent" to "space-between", "boxSizing" to "border-box", "cursor" to "pointer"), ".disabled" to _uM("backgroundImage" to "none", "backgroundColor" to "#f5f5f5", "color" to "#999999", "cursor" to "not-allowed")), "picker-text-full" to _uM("" to _uM("fontSize" to 16, "color" to "#333333", "flex" to 1), ".placeholder" to _uM("color" to "#999999")), "popup-overlay" to _pS(_uM("position" to "fixed", "top" to 0, "left" to 0, "width" to "100%", "height" to "100%", "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0.5)", "zIndex" to 1000, "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "popup-content" to _pS(_uM("width" to "80%", "maxWidth" to 400, "backgroundImage" to "none", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to 12, "borderTopRightRadius" to 12, "borderBottomRightRadius" to 12, "borderBottomLeftRadius" to 12, "overflow" to "hidden")), "popup-header" to _pS(_uM("paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "display" to "flex", "alignItems" to "center", "justifyContent" to "space-between")), "popup-title" to _pS(_uM("fontSize" to 18, "fontWeight" to "bold", "color" to "#333333")), "popup-close" to _pS(_uM("fontSize" to 24, "color" to "#999999", "cursor" to "pointer", "lineHeight" to 1)), "popup-list" to _pS(_uM("maxHeight" to 300, "overflowY" to "auto")), "popup-item" to _pS(_uM("paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f8f9fa", "display" to "flex", "alignItems" to "center", "justifyContent" to "space-between", "cursor" to "pointer", "backgroundImage:hover" to "none", "backgroundColor:hover" to "#f8f9fa", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "popup-item-text" to _pS(_uM("fontSize" to 16, "color" to "#333333", "flex" to 1)), "popup-item-check" to _pS(_uM("fontSize" to 18, "color" to "#007AFF", "fontWeight" to "bold")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
