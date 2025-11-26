package com.gjxt.inspection.entity;

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
     * 记录状态（0-草稿，1-已提交，2-已审核，3-待审核）
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