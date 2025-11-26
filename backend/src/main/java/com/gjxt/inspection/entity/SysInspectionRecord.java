package com.gjxt.inspection.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 周检记录实体类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@TableName("sys_inspection_record")
public class SysInspectionRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableField("record_no")
    private String recordNo;





    /**
     * 检查组组长姓名
     */
    @TableField("group_leader_name")
    private String groupLeaderNameField;

    /**
     * 检查组副组长姓名
     */
    @TableField("deputy_leader_name")
    private String deputyLeaderNameField;

    /**
     * 所属部门ID，外键，关联sys_dept表
     */
    @TableField("department_id")
    private String departmentId;

    /**
     * 问题所属专业ID，外键，关联sys_profession表
     */
    @TableField("profession_id")
    private String professionId;

    /**
     * 问题描述
     */
    @TableField("problem_description")
    private String problemDescription;

    /**
     * 问题发生部位
     */
    @TableField("location")
    private String location;

    /**
     * 问题图片URL（多个URL用逗号分隔）
     */
    @TableField("problem_image_urls")
    private String problemImageUrls;

    /**
     * 整改期限
     */
    @TableField("rectification_deadline")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rectificationDeadline;

    /**
     * 检查人
     */
    @TableField("inspector")
    private String inspector;

    /**
     * 责任单位
     */
    @TableField("responsible_unit")
    private String responsibleUnit;

    /**
     * 责任人
     */
    @TableField("responsible_person")
    private String responsiblePerson;

    /**
     * 整改效果
     */
    @TableField("rectification_effect")
    private String rectificationEffect;

    /**
     * 整改后图片URL（多个URL用逗号分隔）
     */
    @TableField("rectification_image_urls")
    private String rectificationImageUrls;

    /**
     * 整改时间
     */
    @TableField("rectification_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rectificationTime;

    /**
     * 复查效果
     */
    @TableField("review_effect")
    private String reviewEffect;

    /**
     * 复查人ID，外键，关联sys_user表
     */
    @TableField("reviewer_id")
    private String reviewerId;

    /**
     * 复查时间
     */
    @TableField("review_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewTime;

    /**
     * 复查照片URL（多个URL用逗号分隔）
     */
    @TableField("review_image_urls")
    private String reviewImageUrls;

    /**
     * 性质界定
     */
    @TableField("nature_definition")
    private String natureDefinition;

    /**
     * 考核情况
     */
    @TableField("assessment_situation")
    private String assessmentSituation;

    /**
     * 安全记分
     */
    @TableField("safety_score")
    private BigDecimal safetyScore;



    /**
     * 问题状态（0:待整改, 1:待复查, 2:已完成, 3:待审核）
     */
    @TableField("status")
    private Integer status;

    /**
     * 审核状态（0:未审核, 1:已审核）
     */
    @TableField("audit_status")
    private Integer auditStatus;

    /**
     * 审核意见
     */
    @TableField("audit_reason")
    private String auditReason;

    /**
     * 审核人ID
     */
    @TableField("auditor_id")
    private String auditorId;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    // 关联查询字段
    /**
     * 任务编号
     */
    @TableField(exist = false)
    private String taskNo;

    /**
     * 检查组组长姓名
     */
    @TableField(exist = false)
    private String groupLeaderName;

    /**
     * 检查组副组长姓名
     */
    @TableField(exist = false)
    private String deputyLeaderName;

    /**
     * 所属部门名称
     */
    @TableField(exist = false)
    private String departmentName;

    /**
     * 专业名称
     */
    @TableField(exist = false)
    private String professionName;

    /**
     * 部位名称
     */
    @TableField(exist = false)
    private String locationName;

    /**
     * 检查人姓名
     */
    @TableField(exist = false)
    private String inspectorName;

    /**
     * 责任单位名称
     */
    @TableField(exist = false)
    private String responsibleUnitName;

    /**
     * 责任人姓名
     */
    @TableField(exist = false)
    private String responsiblePersonName;

    /**
     * 复查人姓名
     */
    @TableField(exist = false)
    private String reviewerName;

    /**
     * 审核人姓名
     */
    @TableField(exist = false)
    private String auditorName;

    // Getter and Setter methods
    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }





    public String getGroupLeaderNameField() {
        return groupLeaderNameField;
    }

    public void setGroupLeaderNameField(String groupLeaderNameField) {
        this.groupLeaderNameField = groupLeaderNameField;
    }

    public String getDeputyLeaderNameField() {
        return deputyLeaderNameField;
    }

    public void setDeputyLeaderNameField(String deputyLeaderNameField) {
        this.deputyLeaderNameField = deputyLeaderNameField;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getProfessionId() {
        return professionId;
    }

    public void setProfessionId(String professionId) {
        this.professionId = professionId;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProblemImageUrls() {
        return problemImageUrls;
    }

    public void setProblemImageUrls(String problemImageUrls) {
        this.problemImageUrls = problemImageUrls;
    }

    public LocalDateTime getRectificationDeadline() {
        return rectificationDeadline;
    }

    public void setRectificationDeadline(LocalDateTime rectificationDeadline) {
        this.rectificationDeadline = rectificationDeadline;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getResponsibleUnit() {
        return responsibleUnit;
    }

    public void setResponsibleUnit(String responsibleUnit) {
        this.responsibleUnit = responsibleUnit;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getRectificationEffect() {
        return rectificationEffect;
    }

    public void setRectificationEffect(String rectificationEffect) {
        this.rectificationEffect = rectificationEffect;
    }

    public String getRectificationImageUrls() {
        return rectificationImageUrls;
    }

    public void setRectificationImageUrls(String rectificationImageUrls) {
        this.rectificationImageUrls = rectificationImageUrls;
    }

    public LocalDateTime getRectificationTime() {
        return rectificationTime;
    }

    public void setRectificationTime(LocalDateTime rectificationTime) {
        this.rectificationTime = rectificationTime;
    }

    public String getReviewEffect() {
        return reviewEffect;
    }

    public void setReviewEffect(String reviewEffect) {
        this.reviewEffect = reviewEffect;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public LocalDateTime getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(LocalDateTime reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getReviewImageUrls() {
        return reviewImageUrls;
    }

    public void setReviewImageUrls(String reviewImageUrls) {
        this.reviewImageUrls = reviewImageUrls;
    }

    public String getNatureDefinition() {
        return natureDefinition;
    }

    public void setNatureDefinition(String natureDefinition) {
        this.natureDefinition = natureDefinition;
    }

    public String getAssessmentSituation() {
        return assessmentSituation;
    }

    public void setAssessmentSituation(String assessmentSituation) {
        this.assessmentSituation = assessmentSituation;
    }

    public BigDecimal getSafetyScore() {
        return safetyScore;
    }

    public void setSafetyScore(BigDecimal safetyScore) {
        this.safetyScore = safetyScore;
    }



    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    // 关联查询字段的getter和setter
    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getGroupLeaderName() {
        return groupLeaderName;
    }

    public void setGroupLeaderName(String groupLeaderName) {
        this.groupLeaderName = groupLeaderName;
    }

    public String getDeputyLeaderName() {
        return deputyLeaderName;
    }

    public void setDeputyLeaderName(String deputyLeaderName) {
        this.deputyLeaderName = deputyLeaderName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public String getResponsibleUnitName() {
        return responsibleUnitName;
    }

    public void setResponsibleUnitName(String responsibleUnitName) {
        this.responsibleUnitName = responsibleUnitName;
    }

    public String getResponsiblePersonName() {
        return responsiblePersonName;
    }

    public void setResponsiblePersonName(String responsiblePersonName) {
        this.responsiblePersonName = responsiblePersonName;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
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



    /**
     * 根据前端提交的数据创建巡检记录
     * 只设置页面上存在的字段，其他字段保持为空，整改状态默认为未整改
     *
     * @param submitData 前端提交的数据
     * @return 巡检记录实体
     */
    public static SysInspectionRecord createFromSubmitData(SysInspectionRecordSubmitData submitData) {
        SysInspectionRecord record = new SysInspectionRecord();
        
        // 设置页面存在的字段
        if (submitData.getResponsibleUnit() != null) {
            record.setResponsibleUnit(submitData.getResponsibleUnit());
        }
        if (submitData.getResponsiblePerson() != null) {
            record.setResponsiblePerson(submitData.getResponsiblePerson());
        }
        // natureId字段映射移除，由Service层处理
        // 性质界定字段：应该存储字典ID以便后续关联
        if (submitData.getNatureDefinition() != null) {
            record.setNatureDefinition(submitData.getNatureDefinition());
        }
        // assessmentAmount字段已移除，不再设置扣分相关字段
        if (submitData.getSafetyScore() != null) {
            try {
                record.setSafetyScore(submitData.getSafetyScore());
            } catch (NumberFormatException e) {
                // 如果转换失败，保持为null
            }
        }
        if (submitData.getProfessionId() != null) {
            record.setProfessionId(submitData.getProfessionId());
        }
        if (submitData.getProblemDescription() != null) {
            record.setProblemDescription(submitData.getProblemDescription());
        }
        if (submitData.getLocation() != null) {
            record.setLocation(submitData.getLocation());
        }
        if (submitData.getInspector() != null) {
            record.setInspector(submitData.getInspector());
        }
        // 检查组长和副组长字段已移除
        if (submitData.getDepartmentId() != null) {
            record.setDepartmentId(submitData.getDepartmentId());
        }
        // 使用日志记录调试信息
        System.err.println("=== 调试信息 ===");
        System.err.println("前端传递的整改期限: " + submitData.getRectificationDeadline());
        
        if (submitData.getRectificationDeadline() != null) {
            System.err.println("整改期限不为null，开始解析");
            try {
                // 解析整改时间
                LocalDateTime rectificationDeadline = submitData.getRectificationDeadline();
                System.err.println("解析后的整改期限: " + rectificationDeadline);
                if (rectificationDeadline != null) {
                    record.setRectificationDeadline(rectificationDeadline);
                    System.err.println("成功设置整改期限: " + rectificationDeadline);
                } else {
                    // 如果为null，设置默认值为当前时间后7天
                    record.setRectificationDeadline(LocalDateTime.now().plusDays(7));
                    System.err.println("设置默认整改期限(解析为null): " + record.getRectificationDeadline());
                }
            } catch (Exception e) {
                // 如果解析失败，设置默认值为当前时间后7天
                record.setRectificationDeadline(LocalDateTime.now().plusDays(7));
                System.err.println("解析失败，设置默认整改期限: " + record.getRectificationDeadline() + ", 错误: " + e.getMessage());
            }
        } else {
            // 如果整改期限为空，设置默认值为当前时间后7天
            record.setRectificationDeadline(LocalDateTime.now().plusDays(7));
            System.err.println("前端未传递整改期限，设置默认值: " + record.getRectificationDeadline());
        }
        System.err.println("=== 调试信息结束 ===");
        // 处理图片URL字段 - 前端传递的是imageUrls字段
        if (submitData.getImageUrls() != null && !submitData.getImageUrls().isEmpty()) {
            record.setProblemImageUrls(submitData.getImageUrls());
        } else if (submitData.getProblemImageUrls() != null && !submitData.getProblemImageUrls().isEmpty()) {
            record.setProblemImageUrls(submitData.getProblemImageUrls());
        }
        // scoreId字段映射：根据scoreId获取对应的考核值并设置到assessmentSituation
        if (submitData.getAssessmentSituation() != null) {
            record.setAssessmentSituation(submitData.getAssessmentSituation());
        }
        
        // 设置默认值
        record.setRectificationEffect("未整改"); // 整改状态默认为未整改
        record.setStatus(0); // 状态默认为0（待整改）
        
        return record;
    }
    

}