package com.gjxt.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gjxt.inspection.entity.Notification;

import java.util.List;

/**
 * 通知服务接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
public interface NotificationService extends IService<Notification> {

    /**
     * 根据用户ID获取通知列表
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    List<Notification> getNotificationsByUserId(String userId);

    /**
     * 根据用户ID和部门ID获取通知列表
     *
     * @param userId 用户ID
     * @param departmentId 部门ID
     * @return 通知列表
     */
    List<Notification> getNotificationsByUserIdAndDepartment(String userId, String departmentId);

    /**
     * 手动推送通知到指定部门
     *
     * @param title 通知标题
     * @param content 通知内容
     * @param departmentId 部门ID
     * @param departmentName 部门名称
     * @param priority 优先级
     * @param actionUrl 操作链接
     * @return 推送结果
     */
    boolean pushNotificationToDepartment(String title, String content, String departmentId, 
                                       String departmentName, Integer priority, String actionUrl);

    /**
     * 手动推送通知到指定用户
     *
     * @param title 通知标题
     * @param content 通知内容
     * @param userId 用户ID
     * @param userName 用户名称
     * @param priority 优先级
     * @param actionUrl 操作链接
     * @return 推送结果
     */
    boolean pushNotificationToUser(String title, String content, String userId, 
                                 String userName, Integer priority, String actionUrl);

    /**
     * 标记通知为已读
     *
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean markAsRead(String userId, String notificationId);

    /**
     * 删除用户的指定通知
     *
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean deleteNotification(String userId, String notificationId);

    /**
     * 清理过期通知
     *
     * @param userId 用户ID
     * @return 删除的通知数量
     */
    int cleanExpiredNotifications(String userId);

    /**
     * 根据任务ID删除相关通知
     *
     * @param taskId 任务ID
     * @return 删除的通知数量
     */
    int deleteNotificationsByTaskId(String taskId);

    /**
     * 获取用户未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    int getUnreadCount(String userId);

    /**
     * 创建并保存通知
     *
     * @param notification 通知对象
     * @return 保存的通知
     */
    Notification createNotification(Notification notification);

    /**
     * 获取用户的月检任务通知列表（包含任务信息）
     *
     * @param userId 用户ID
     * @param departmentId 部门ID
     * @return 月检任务通知列表
     */
    List<Notification> getTaskNotificationsByUserId(String userId, String departmentId);

    /**
     * 获取指定部门的月检任务通知列表
     *
     * @param departmentId 部门ID
     * @return 月检任务通知列表
     */
    List<Notification> getTaskNotificationsByDepartment(String departmentId);

    /**
     * 获取所有部门的月检任务通知列表（管理员使用）
     *
     * @return 月检任务通知列表
     */
    List<Notification> getAllTaskNotifications();
}