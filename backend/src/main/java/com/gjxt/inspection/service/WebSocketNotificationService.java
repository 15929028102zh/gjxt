package com.gjxt.inspection.service;

import com.gjxt.inspection.entity.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * WebSocket通知服务
 * 负责通过WebSocket推送通知消息
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Slf4j
@Service
public class WebSocketNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;

    /**
     * 发送通知给指定用户
     *
     * @param userId 用户ID
     * @param notification 通知内容
     */
    public void sendNotificationToUser(String userId, Notification notification) {
        try {
            // 设置用户ID
            notification.setUserId(userId);
            
            // 保存通知到数据库
            Notification savedNotification = notificationService.createNotification(notification);
            
            log.info("发送WebSocket通知给用户: {}, 通知内容: {}", userId, notification.getTitle());
            
            // 通过WebSocket发送通知
            messagingTemplate.convertAndSendToUser(
                userId, 
                "/queue/notifications", 
                savedNotification
            );
            
            log.info("WebSocket通知发送成功");
            
        } catch (Exception e) {
            log.error("发送WebSocket通知失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 发送通知给多个用户
     *
     * @param userIds 用户ID列表
     * @param notification 通知内容
     */
    public void sendNotificationToUsers(List<String> userIds, Notification notification) {
        for (String userId : userIds) {
            // 为每个用户创建独立的通知对象
            Notification userNotification = new Notification();
            userNotification.setType(notification.getType());
            userNotification.setTitle(notification.getTitle());
            userNotification.setContent(notification.getContent());
            userNotification.setUserId(userId);
            userNotification.setTaskId(notification.getTaskId());
            userNotification.setTaskNo(notification.getTaskNo());
            userNotification.setPriority(notification.getPriority());
            userNotification.setActionUrl(notification.getActionUrl());
            userNotification.setExpireTime(notification.getExpireTime());
            
            sendNotificationToUser(userId, userNotification);
        }
    }

    /**
     * 获取用户的所有通知
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    public List<Notification> getUserNotifications(String userId) {
        return notificationService.getNotificationsByUserId(userId);
    }

    /**
     * 标记通知为已读
     *
     * @param userId 用户ID
     * @param notificationId 通知ID
     */
    public void markNotificationAsRead(String userId, String notificationId) {
        notificationService.markAsRead(userId, notificationId);
    }

    /**
     * 删除用户的通知
     *
     * @param userId 用户ID
     * @param notificationId 通知ID
     */
    public void deleteNotification(String userId, String notificationId) {
        notificationService.deleteNotification(userId, notificationId);
    }

    /**
     * 清理过期通知
     *
     * @param userId 用户ID
     */
    public void cleanExpiredNotifications(String userId) {
        notificationService.cleanExpiredNotifications(userId);
    }

    /**
     * 根据任务ID删除相关通知
     *
     * @param taskId 任务ID
     */
    public void deleteNotificationsByTaskId(String taskId) {
        notificationService.deleteNotificationsByTaskId(taskId);
    }
}