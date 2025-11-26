package com.gjxt.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjxt.inspection.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知Mapper接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {

    /**
     * 根据用户ID获取通知列表
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    @Select("SELECT * FROM notification WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Notification> selectByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID和部门ID获取通知列表（按部门过滤）
     *
     * @param userId 用户ID
     * @param departmentId 部门ID
     * @return 通知列表
     */
    @Select("SELECT * FROM notification WHERE user_id = #{userId} " +
            "AND (department_id IS NULL OR department_id = #{departmentId}) " +
            "ORDER BY create_time DESC")
    List<Notification> selectByUserIdAndDepartment(@Param("userId") String userId, @Param("departmentId") String departmentId);

    /**
     * 标记通知为已读
     *
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 更新行数
     */
    @Update("UPDATE notification SET is_read = 1 WHERE user_id = #{userId} AND id = #{notificationId}")
    int markAsRead(@Param("userId") String userId, @Param("notificationId") String notificationId);

    /**
     * 删除用户的指定通知
     *
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 删除行数
     */
    @Delete("DELETE FROM notification WHERE user_id = #{userId} AND id = #{notificationId}")
    int deleteByUserIdAndId(@Param("userId") String userId, @Param("notificationId") String notificationId);

    /**
     * 删除过期通知
     *
     * @param userId 用户ID
     * @param now 当前时间
     * @return 删除行数
     */
    @Delete("DELETE FROM notification WHERE user_id = #{userId} AND expire_time IS NOT NULL AND expire_time < #{now}")
    int deleteExpiredNotifications(@Param("userId") String userId, @Param("now") LocalDateTime now);

    /**
     * 根据任务ID删除相关通知
     *
     * @param taskId 任务ID
     * @return 删除行数
     */
    @Delete("DELETE FROM notification WHERE task_id = #{taskId}")
    int deleteByTaskId(@Param("taskId") String taskId);

    /**
     * 获取用户未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    @Select("SELECT COUNT(*) FROM notification WHERE user_id = #{userId} AND is_read = 0")
    int getUnreadCount(@Param("userId") String userId);

    /**
     * 获取用户的月检任务通知列表（包含任务信息）
     *
     * @param userId 用户ID
     * @param departmentId 部门ID
     * @return 通知列表
     */
    @Select("SELECT " +
            "t.id as id, " +
            "t.id as taskId, " +
            "t.task_no as taskNo, " +
            "DATE_FORMAT(t.inspection_time, '%Y-%m') as inspectionMonth, " +
            "t.status, " +
            "t.notification_sent, " +
            "t.task_description, " +
            "t.create_time as createTime, " +
            "t.department_id, " +
            "d.dept_name as department_name, " +
            "CONCAT('月检任务通知 - ', t.task_no) as title, " +
            "CONCAT('您有新的月检任务需要处理，任务编号：', t.task_no, '，状态：', t.status) as content, " +
            "0 as is_read " +
            "FROM sys_monthly_inspection_task t " +
            "LEFT JOIN sys_dept d ON t.department_id = d.id " +
            "WHERE t.notification_sent = 1 " +
            "AND t.deleted = 0 " +
            "AND t.department_id = #{departmentId} " +
            "ORDER BY t.create_time DESC")
    List<Notification> selectTaskNotificationsByUserId(@Param("userId") String userId, @Param("departmentId") String departmentId);

    /**
     * 获取指定部门的月检任务通知列表
     *
     * @param departmentId 部门ID
     * @return 通知列表
     */
    @Select("SELECT " +
            "t.id as id, " +
            "t.id as taskId, " +
            "t.task_no as taskNo, " +
            "DATE_FORMAT(t.inspection_time, '%Y-%m') as inspectionMonth, " +
            "t.status, " +
            "t.notification_sent, " +
            "t.task_description, " +
            "t.create_time as createTime, " +
            "t.department_id, " +
            "d.dept_name as department_name, " +
            "CONCAT('月检任务通知 - ', t.task_no) as title, " +
            "CONCAT('您有新的月检任务需要处理，任务编号：', t.task_no, '，状态：', t.status) as content, " +
            "0 as is_read " +
            "FROM sys_monthly_inspection_task t " +
            "LEFT JOIN sys_dept d ON t.department_id = d.id " +
            "WHERE t.notification_sent = 1 " +
            "AND t.deleted = 0 " +
            "AND t.department_id = #{departmentId} " +
            "ORDER BY t.create_time DESC")
    List<Notification> selectTaskNotificationsByDepartment(@Param("departmentId") String departmentId);

    /**
     * 获取所有部门的月检任务通知列表（管理员使用）
     *
     * @return 通知列表
     */
    @Select("SELECT " +
            "t.id as id, " +
            "t.id as taskId, " +
            "t.task_no as taskNo, " +
            "DATE_FORMAT(t.inspection_time, '%Y-%m') as inspectionMonth, " +
            "t.status, " +
            "t.notification_sent, " +
            "t.task_description, " +
            "t.create_time as createTime, " +
            "t.department_id, " +
            "d.dept_name as department_name, " +
            "CONCAT('月检任务通知 - ', t.task_no) as title, " +
            "CONCAT('您有新的月检任务需要处理，任务编号：', t.task_no, '，状态：', t.status) as content, " +
            "0 as is_read " +
            "FROM sys_monthly_inspection_task t " +
            "LEFT JOIN sys_dept d ON t.department_id = d.id " +
            "WHERE t.notification_sent = 1 " +
            "AND t.deleted = 0 " +
            "ORDER BY t.create_time DESC")
    List<Notification> selectAllTaskNotifications();
}