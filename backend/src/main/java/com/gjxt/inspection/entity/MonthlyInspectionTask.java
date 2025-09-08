package com.gjxt.inspection.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

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
     * 月检时间（格式：YYYY-MM-DD HH）
     */
    @TableField("inspection_time")
    private String inspectionTime;

    /**
     * 月检组长ID
     */
    @TableField("group_leader_id")
    private String groupLeaderId;

    /**
     * 月检副组长ID
     */
    @TableField("deputy_leader_id")
    private String deputyLeaderId;

    /**
     * 部门ID
     */
    @TableField("department_id")
    private String departmentId;

    /**
     * 专业ID
     */
    @TableField("profession_id")
    private String professionId;

    /**
     * 任务描述
     */
    @TableField("task_description")
    private String taskDescription;

    /**
     * 状态：0-未开始，1-进行中，2-已完成
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

    // 关联查询字段（非数据库字段）
    /**
     * 月检组长姓名
     */
    @TableField(exist = false)
    private String groupLeaderName;

    /**
     * 月检副组长姓名
     */
    @TableField(exist = false)
    private String deputyLeaderName;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String departmentName;

    /**
     * 专业名称
     */
    @TableField(exist = false)
    private String professionName;

    /**
     * 创建人姓名
     */
    @TableField(exist = false)
    private String createUserName;

    // Getter and Setter methods
    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(String inspectionTime) {
        this.inspectionTime = inspectionTime;
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

    // 关联查询字段的getter和setter
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

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}