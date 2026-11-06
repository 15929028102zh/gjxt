package com.gjxt.inspection.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 月检任务实体类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@TableName("sys_monthly_inspection_task")
public class MonthlyInspectionTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 任务编号
     */
    @TableField("task_no")
    private String taskNo;

    /**
     * 任务名称
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 月检开始时间
     */
    @TableField("inspection_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionStartTime;

    /**
     * 月检结束时间
     */
    @TableField("inspection_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionEndTime;

    /**
     * 公示开始时间
     */
    @TableField("publicity_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publicityStartTime;

    /**
     * 公示结束时间
     */
    @TableField("publicity_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publicityEndTime;

    /**
     * 任务描述
     */
    @TableField("task_description")
    private String taskDescription;

    /**
     * 状态：0-未开始，1-进行中，2-已完成，3-已取消
     */
    @TableField("status")
    private Integer status;

    /**
     * 通知发送状态：0-未发送，1-已发送
     */
    @TableField("notification_sent")
    private Integer notificationSent;

    /**
     * 创建人ID
     */
    @TableField("create_user_id")
    private String createUserId;

    /**
     * 创建人姓名（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String createUserName;

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDateTime getInspectionStartTime() {
        return inspectionStartTime;
    }

    public void setInspectionStartTime(LocalDateTime inspectionStartTime) {
        this.inspectionStartTime = inspectionStartTime;
    }

    public LocalDateTime getInspectionEndTime() {
        return inspectionEndTime;
    }

    public void setInspectionEndTime(LocalDateTime inspectionEndTime) {
        this.inspectionEndTime = inspectionEndTime;
    }

    public LocalDateTime getPublicityStartTime() {
        return publicityStartTime;
    }

    public void setPublicityStartTime(LocalDateTime publicityStartTime) {
        this.publicityStartTime = publicityStartTime;
    }

    public LocalDateTime getPublicityEndTime() {
        return publicityEndTime;
    }

    public void setPublicityEndTime(LocalDateTime publicityEndTime) {
        this.publicityEndTime = publicityEndTime;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(Integer notificationSent) {
        this.notificationSent = notificationSent;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    /**
     * 判断当前是否处于月检录入期内（允许新增月检记录）
     */
    public boolean isInInspectionPeriod(LocalDateTime now) {
        if (now == null) {
            return false;
        }
        return inspectionStartTime != null && inspectionEndTime != null
                && !now.isBefore(inspectionStartTime)
                && !now.isAfter(inspectionEndTime);
    }

    /**
     * 判断当前是否处于公示期内
     * （月检录入已结束、公示未结束——即"录入已结束但允许公示前自由编辑期"之后到公示结束）
     */
    public boolean isInPublicityPeriod(LocalDateTime now) {
        if (now == null || publicityStartTime == null || publicityEndTime == null) {
            return false;
        }
        return !now.isBefore(publicityStartTime) && !now.isAfter(publicityEndTime);
    }

    /**
     * 综合判断：
     * 0 - 录入期前
     * 1 - 录入期内
     * 2 - 公示前自由期（录入结束、公示开始之前）
     * 3 - 公示期内
     * 4 - 公示期后
     */
    public int currentPhase(LocalDateTime now) {
        if (now == null) {
            return 0;
        }
        if (inspectionStartTime != null && inspectionEndTime != null
                && !now.isBefore(inspectionStartTime)
                && !now.isAfter(inspectionEndTime)) {
            return 1;
        }
        if (publicityStartTime != null && publicityEndTime != null
                && !now.isBefore(publicityStartTime)
                && !now.isAfter(publicityEndTime)) {
            return 3;
        }
        if (inspectionEndTime != null && now.isAfter(inspectionEndTime)
                && publicityStartTime != null && now.isBefore(publicityStartTime)) {
            return 2;
        }
        return 0;
    }

    /**
     * V3:是否已经过了公示期(now > publicityEndTime)。
     * 用于判断受检单位(运行部门)能否看到整改/复查按钮。
     */
    public boolean isAfterPublicityEnd(LocalDateTime now) {
        if (now == null || publicityEndTime == null) {
            return false;
        }
        return now.isAfter(publicityEndTime);
    }
}
