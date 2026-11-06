package com.gjxt.inspection.service;

import com.gjxt.inspection.entity.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 发送通知给指定用户（保存 + WebSocket 推送，由 NotificationServiceImpl.createNotification 内部驱动）
     *
     * @param userId 用户ID
     * @param notification 通知内容
     */
    public void sendNotificationToUser(String userId, Notification notification) {
        try {
            // 设置用户ID
            notification.setUserId(userId);

            // 保存 + 推送（推送由 NotificationServiceImpl.createNotification 内部统一处理）
            notificationService.createNotification(notification);

            log.info("发送WebSocket通知给用户: {}, 通知内容: {}", userId, notification.getTitle());
        } catch (Exception e) {
            log.error("发送WebSocket通知失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 仅推送一条通知（不写库）。
     * 用于调用方已经入库（如 batchCreateNotification）的场景，避免重复入库。
     *
     * @param notification 已落库的通知对象
     */
    public void pushNotification(Notification notification) {
        if (notification == null) {
            return;
        }
        try {
            String userId = notification.getUserId();
            if (userId != null && !userId.isEmpty()) {
                messagingTemplate.convertAndSendToUser(
                    userId,
                    "/user/queue/notifications",
                    notification
                );
            } else {
                messagingTemplate.convertAndSend("/topic/notifications", notification);
            }
        } catch (Exception e) {
            log.warn("WebSocket 推送通知失败 userId={} : {}", notification.getUserId(), e.getMessage());
        }
    }

    /**
     * 批量仅推送通知（不写库）。
     * 单条推送失败不影响整体流程，异常会被吞掉并记录日志。
     *
     * @param notifications 已落库的通知对象列表
     */
    public void pushBatchNotifications(List<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            return;
        }
        int pushed = 0;
        int failed = 0;
        for (Notification n : notifications) {
            try {
                if (n == null) continue;
                String userId = n.getUserId();
                if (userId != null && !userId.isEmpty()) {
                    messagingTemplate.convertAndSendToUser(
                        userId,
                        "/user/queue/notifications",
                        n
                    );
                } else {
                    messagingTemplate.convertAndSend("/topic/notifications", n);
                }
                pushed++;
            } catch (Exception e) {
                failed++;
                log.warn("WebSocket 批量推送失败 userId={} : {}", n.getUserId(), e.getMessage());
            }
        }
        if (pushed > 0) {
            log.info("WebSocket 批量推送完成，成功 {} 条，失败 {} 条", pushed, failed);
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

    /**
     * 广播记录状态变更事件到 /topic/record-events
     * 已订阅该主题的客户端（通常为打开的列表页）将收到事件并自行刷新。
     */
    public void broadcastRecordStatusChange(String recordId, String recordType, Integer newStatus, String operator) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("recordId", recordId);
            payload.put("recordType", recordType);
            payload.put("newStatus", newStatus);
            payload.put("operator", operator);
            payload.put("timestamp", LocalDateTime.now().toString());
            messagingTemplate.convertAndSend("/topic/record-events", payload);
        } catch (Exception e) {
            log.warn("广播记录状态变更事件失败: {}", e.getMessage());
        }
    }
}