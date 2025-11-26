package com.gjxt.inspection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 通知实体类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("notification")
public class Notification {
    
    /**
     * 通知ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    
    /**
     * 通知类型
     */
    @TableField("type")
    private String type;
    
    /**
     * 通知标题
     */
    @TableField("title")
    private String title;
    
    /**
     * 通知内容
     */
    @TableField("content")
    private String content;
    
    /**
     * 接收用户ID
     */
    @TableField("user_id")
    private String userId;
    
    /**
     * 接收用户名
     */
    @TableField("user_name")
    private String userName;
    
    /**
     * 部门ID（用于按部门过滤推送）
     */
    @TableField("department_id")
    private String departmentId;
    
    /**
     * 部门名称
     */
    @TableField("department_name")
    private String departmentName;
    
    /**
     * 任务ID（如果是任务相关通知）
     */
    @TableField("task_id")
    private String taskId;
    
    /**
     * 任务编号
     */
    @TableField("task_no")
    private String taskNo;
    
    /**
     * 是否已读
     */
    @TableField("is_read")
    private Boolean isRead;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 过期时间
     */
    @TableField("expire_time")
    private LocalDateTime expireTime;
    
    /**
     * 优先级（high, medium, low）
     */
    @TableField("priority")
    private String priority;
    
    /**
     * 操作链接
     */
    @TableField("action_url")
    private String actionUrl;
    
    /**
     * 任务状态（非数据库字段，用于月检任务通知）
     */
    @TableField(exist = false)
    private String status;
    
    /**
     * 检查月份（非数据库字段，用于月检任务通知）
     */
    @TableField(exist = false)
    private String inspectionMonth;
}