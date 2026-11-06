package com.gjxt.inspection.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 月检记录实体类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_monthly_inspection_record")
@Schema(name = "MonthlyInspectionRecord", description = "月检记录")
public class MonthlyInspectionRecord {

    /**
     * 主键ID
     */
    @TableId
    @Schema(description = "主键ID")
    private String id;

    /**
     * 记录编号
     */
    @Schema(description = "记录编号")
    private String recordNo;

    /**
     * 关联的月检任务ID
     */
    @Schema(description = "关联的月检任务ID")
    private String taskId;



    /**
     * 专业类别ID
     */
    @Schema(description = "专业类别ID")
    private String professionalTypeId;

    /**
     * 专业类别名称
     */
    @Schema(description = "专业类别名称")
    private String professionalTypeName;

    /**
     * 模块ID
     */
    @Schema(description = "模块ID")
    private String moduleId;

    /**
     * 模块名称
     */
    @Schema(description = "模块名称")
    private String moduleName;

    /**
     * 责任部门ID
     */
    @Schema(description = "责任部门ID")
    private String responsibilityId;

    /**
     * 责任部门名称
     */
    @Schema(description = "责任部门名称")
    private String responsibilityName;

    /**
     * 问题ID
     */
    @Schema(description = "问题ID")
    private String issueId;

    /**
     * 问题名称
     */
    @Schema(description = "问题名称")
    private String issueName;

    /**
     * 性质界定ID
     */
    @Schema(description = "性质界定ID")
    private String qualityId;

    /**
     * 性质界定名称
     */
    @Schema(description = "性质界定名称")
    private String qualityName;

    /**
     * 性质界定是否被人工改动(0=否 1=是)
     */
    @Schema(description = "性质界定是否被人工改动 0=否 1=是")
    @TableField("nature_modified")
    private Integer natureModified;

    /**
     * 问题类型
     */
    @Schema(description = "问题类型")
    private String problemType;

    /**
     * 问题描述
     */
    @Schema(description = "问题描述")
    private String problemDescription;

    /**
     * 发现位置
     */
    @Schema(description = "发现位置")
    private String location;

    /**
     * 受检单位（部门ID）
     */
    @Schema(description = "受检单位（部门ID）")
    @TableField("department_id")
    private String departmentId;

    /**
     * 检查单位ID（管理部门）
     */
    @Schema(description = "检查单位ID(管理部门)")
    @TableField("check_unit_id")
    private String checkUnitId;

    /**
     * 检查单位名称
     */
    @Schema(description = "检查单位名称")
    @TableField("check_unit_name")
    private String checkUnitName;

    /**
     * 责任单位
     */
    @Schema(description = "责任单位")
    private String responsibleUnit;

    /**
     * 责任人
     */
    @Schema(description = "责任人")
    private String responsiblePerson;

    /**
     * 整改建议
     */
    @Schema(description = "整改建议")
    private String suggestion;

    /**
     * 问题照片URL列表（JSON格式）
     */
    @Schema(description = "问题照片URL列表")
    private String photos;

    /**
     * 整改期限
     */
    @Schema(description = "整改期限")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rectificationDate;

    /**
     * 整改建议
     */
    @Schema(description = "整改建议")
    private String rectificationSuggestion;

    /**
     * 整改效果描述
     */
    @Schema(description = "整改效果描述")
    private String rectificationEffect;

    /**
     * 整改图片URL列表
     */
    @Schema(description = "整改图片URL列表")
    private String rectificationImages;

    /**
     * 整改时间
     */
    @Schema(description = "整改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rectificationTime;

    /**
     * 整改人姓名（确认整改时的当前登录用户）
     */
    @Schema(description = "整改人姓名")
    private String rectifierName;

    /**
     * 复查建议
     */
    @Schema(description = "复查建议")
    private String reviewSuggestion;

    /**
     * 复查效果描述
     */
    @Schema(description = "复查效果描述")
    private String reviewEffect;

    /**
     * 复查图片URL列表
     */
    @Schema(description = "复查图片URL列表")
    private String reviewImages;

    /**
     * 复查时间
     */
    @Schema(description = "复查时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewTime;

    /**
     * 复查人姓名（确认复查时的当前登录用户）
     */
    @Schema(description = "复查人姓名")
    private String reviewerName;

    /**
     * 检查人员
     */
    @Schema(description = "检查人员")
    private String inspector;

    /**
     * 安全评分
     */
    @Schema(description = "安全评分")
    private Integer safetyScore;

    /**
     * 记录状态（0-草稿/待整改，1-已整改/已提交，2-已复查/已审核，3-待审核，4-审核确认）
     * 状态流转：草稿(0) -> 已提交(1) -> 待审核(3) -> 审核确认(4)
     * 或：草稿(0) -> 已整改(1) -> 已复查(2) -> 已审核(2)
     */
    @Schema(description = "记录状态")
    private Integer recordStatus;

    /**
     * 审核状态（0:未审核, 1:已审核）
     */
    @Schema(description = "审核状态")
    private Integer auditStatus;

    /**
     * 审核意见
     */
    @Schema(description = "审核意见")
    private String auditReason;

    /**
     * 审核人ID
     */
    @Schema(description = "审核人ID")
    private String auditorId;

    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    /**
     * 待审核类型：null=无, 0=回退审核, 1=编辑审核
     */
    @Schema(description = "待审核类型")
    @TableField("record_edit_audit_type")
    private Integer recordEditAuditType;

    /**
     * 提交编辑/回退审核前的原状态(0/1/2)
     * 审核通过/拒绝后,recordStatus 恢复为该字段值,而不是回到 4 或 1。
     */
    @Schema(description = "提交审核前的原状态")
    @TableField(value = "pre_edit_status", updateStrategy = FieldStrategy.IGNORED)
    private Integer preEditStatus;

    /**
     * 修改说明（编辑审核时由申请人填写）
     */
    @Schema(description = "修改说明")
    @TableField("modify_description")
    private String modifyDescription;

    /**
     * 修改申请人ID
     */
    @Schema(description = "修改申请人ID")
    @TableField("modify_user_id")
    private String modifyUserId;

    /**
     * 修改申请人姓名
     */
    @Schema(description = "修改申请人姓名")
    @TableField("modify_user_name")
    private String modifyUserName;

    /**
     * 修改申请时间
     */
    @Schema(description = "修改申请时间")
    @TableField("modify_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /**
     * 原值/修改值 JSON
     * 格式: {"field":{"from":"原值","to":"修改值"}, ...}
     */
    @Schema(description = "原值/修改值 JSON")
    @TableField(value = "modification_snapshot", updateStrategy = FieldStrategy.IGNORED)
    private String modificationSnapshot;

    /**
     * 创建用户ID
     */
    @Schema(description = "创建用户ID")
    private String createUserId;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @Schema(description = "是否删除")
    private Integer deleted;

    // 以下为扩展字段，不对应数据库字段

    /**
     * 创建用户姓名
     */
    @TableField(exist = false)
    @Schema(description = "创建用户姓名")
    private String createUserName;

    /**
     * 任务编号
     */
    @TableField(exist = false)
    @Schema(description = "任务编号")
    private String taskNo;

    /**
     * 月检时间
     */
    @TableField(exist = false)
    @Schema(description = "月检时间")
    private String inspectionTime;

    /**
     * 检查组长姓名
     */
    @TableField(exist = false)
    @Schema(description = "检查组长姓名")
    private String groupLeaderName;

    /**
     * 检查副组长姓名
     */
    @TableField(exist = false)
    @Schema(description = "检查副组长姓名")
    private String deputyLeaderName;

    /**
     * 任务名称
     */
    @TableField(exist = false)
    @Schema(description = "任务名称")
    private String taskName;

    /**
     * 任务录入开始时间（关联任务表，用于前端计算任务阶段）
     */
    @TableField(exist = false)
    @Schema(description = "任务录入开始时间")
    private LocalDateTime inspectionStartTime;

    /**
     * 任务录入结束时间
     */
    @TableField(exist = false)
    @Schema(description = "任务录入结束时间")
    private LocalDateTime inspectionEndTime;

    /**
     * 公示开始时间
     */
    @TableField(exist = false)
    @Schema(description = "公示开始时间")
    private LocalDateTime publicityStartTime;

    /**
     * 公示结束时间
     */
    @TableField(exist = false)
    @Schema(description = "公示结束时间")
    private LocalDateTime publicityEndTime;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    @Schema(description = "部门名称")
    private String departmentName;

    /**
     * 整改期限
     */
    @TableField(exist = false)
    @Schema(description = "整改期限")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rectificationDeadline;

    /**
     * 专业名称
     */
    @TableField(exist = false)
    @Schema(description = "专业名称")
    private String professionName;

    // 移动端传递的字段映射
    
    /**
     * 专业类别（移动端字段）
     */
    @TableField(exist = false)
    @Schema(description = "专业类别")
    private String professionalCategory;

    /**
     * 模块（移动端字段）
     */
    @TableField(exist = false)
    @Schema(description = "模块")
    private String module;

    /**
     * 责任部门（移动端字段）
     */
    @TableField(exist = false)
    @Schema(description = "责任部门")
    private String responsibility;

    /**
     * 问题（移动端字段）
     */
    @TableField(exist = false)
    @Schema(description = "问题")
    private String issue;

    /**
     * 性质界定（移动端字段）
     */
    @TableField(exist = false)
    @Schema(description = "性质界定")
    private String qualityDetermination;

    /**
     * 审核人姓名
     */
    @TableField(exist = false)
    @Schema(description = "审核人姓名")
    private String auditorName;

    // Getter and Setter methods for fields that Lombok might not generate properly
    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditReason() {
        return auditReason;
    }

    public void setAuditReason(String auditReason) {
        this.auditReason = auditReason;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public LocalDateTime getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }
}