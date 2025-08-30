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
     * 检查组组长ID，外键，关联sys_user表
     */
    @TableField("group_leader_id")
    private String groupLeaderId;

    /**
     * 检查组副组长ID，外键，关联sys_user表
     */
    @TableField("deputy_leader_id")
    private String deputyLeaderId;

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
     * 问题状态（0:待整改, 1:待复查, 2:已完成）
     */
    @TableField("status")
    private Integer status;

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

    

    // Getter and Setter methods
    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }



    public String getGroupLeaderId() {
        return groupLeaderId;
    }

    public void setGroupLeaderId(String groupLeaderId) {
        this.groupLeaderId = groupLeaderId;
    }

    public String getDeputyLeaderId() {
        return deputyLeaderId;
    }

    public void setDeputyLeaderId(String deputyLeaderId) {
        this.deputyLeaderId = deputyLeaderId;
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
        // 前端提交的groupLeader和deputyLeader是姓名字符串，存储到姓名字段
        if (submitData.getGroupLeader() != null) {
            record.setGroupLeaderNameField(submitData.getGroupLeader());
        }
        if (submitData.getDeputyLeader() != null) {
            record.setDeputyLeaderNameField(submitData.getDeputyLeader());
        }
        if (submitData.getDepartmentId() != null) {
            record.setDepartmentId(submitData.getDepartmentId());
        }
        if (submitData.getRectificationDeadline() != null) {
            try {
                // 解析整改时间
                LocalDateTime rectificationDeadline = submitData.getRectificationDeadline();
                if (rectificationDeadline != null) {
                    record.setRectificationDeadline(rectificationDeadline);
                }
            } catch (Exception e) {
                // 如果解析失败，保持为null
            }
        }
        if (submitData.getProblemImageUrls() != null && !submitData.getProblemImageUrls().isEmpty()) {
            record.setProblemImageUrls(String.join(",", submitData.getProblemImageUrls()));
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