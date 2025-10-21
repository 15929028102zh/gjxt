package com.gjxt.inspection.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 巡检任务实体类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_inspection_task")
public class SysInspectionTask extends BaseEntity {

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
     * 所属专业ID，外键，关联sys_profession表
     */
    @TableField("profession_id")
    private Long professionId;

    /**
     * 发生部位ID，外键，关联sys_location表
     */
    @TableField("location_id")
    private Long locationId;

    /**
     * 接收任务用户ID，外键，关联sys_user表
     */
    @TableField("user_id")
    private String userId;

    /**
     * 计划巡检时间
     */
    @TableField("schedule_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduleTime;

    /**
     * 最后整改时间
     */
    @TableField("rectification_deadline")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rectificationDeadline;

    /**
     * 任务状态（pending:未开始, in_progress:进行中, completed:已完成, cancelled:已取消）
     */
    @TableField("status")
    private String status;

    /**
     * 任务描述
     */
    @TableField("description")
    private String description;

    // 关联查询字段
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
     * 用户名称
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 用户真实姓名（非数据库字段）
     */
    @TableField(exist = false)
    private String userRealName;

    // 手动添加getter和setter方法以解决编译问题
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

    public Long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(Long professionId) {
        this.professionId = professionId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(LocalDateTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public LocalDateTime getRectificationDeadline() {
        return rectificationDeadline;
    }

    public void setRectificationDeadline(LocalDateTime rectificationDeadline) {
        this.rectificationDeadline = rectificationDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

}