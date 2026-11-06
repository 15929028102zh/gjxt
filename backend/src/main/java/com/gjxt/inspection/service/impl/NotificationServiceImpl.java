package com.gjxt.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjxt.inspection.entity.Notification;
import com.gjxt.inspection.entity.SysUser;
import com.gjxt.inspection.mapper.NotificationMapper;
import com.gjxt.inspection.service.ISysUserService;
import com.gjxt.inspection.service.NotificationService;
import com.gjxt.inspection.service.WebSocketNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 通知服务实现类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Slf4j
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    @Lazy
    private WebSocketNotificationService webSocketNotificationService;

    @Override
    public List<Notification> getNotificationsByUserId(String userId) {
        try {
            return notificationMapper.selectByUserId(userId);
        } catch (Exception e) {
            log.error("获取用户通知失败, userId: {}, error: {}", userId, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Notification> getNotificationsByUserIdAndDepartment(String userId, String departmentId) {
        try {
            return notificationMapper.selectByUserIdAndDepartment(userId, departmentId);
        } catch (Exception e) {
            log.error("获取用户部门通知失败, userId: {}, departmentId: {}, error: {}", userId, departmentId, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean markAsRead(String userId, String notificationId) {
        try {
            int result = notificationMapper.markAsRead(userId, notificationId);
            return result > 0;
        } catch (Exception e) {
            log.error("标记通知已读失败, userId: {}, notificationId: {}, error: {}", userId, notificationId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteNotification(String userId, String notificationId) {
        try {
            int result = notificationMapper.deleteByUserIdAndId(userId, notificationId);
            return result > 0;
        } catch (Exception e) {
            log.error("删除通知失败, userId: {}, notificationId: {}, error: {}", userId, notificationId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public int cleanExpiredNotifications(String userId) {
        try {
            return notificationMapper.deleteExpiredNotifications(userId, LocalDateTime.now());
        } catch (Exception e) {
            log.error("清理过期通知失败, userId: {}, error: {}", userId, e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int deleteNotificationsByTaskId(String taskId) {
        try {
            return notificationMapper.deleteByTaskId(taskId);
        } catch (Exception e) {
            log.error("根据任务ID删除通知失败, taskId: {}, error: {}", taskId, e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int getUnreadCount(String userId) {
        try {
            return notificationMapper.getUnreadCount(userId);
        } catch (Exception e) {
            log.error("获取未读通知数量失败, userId: {}, error: {}", userId, e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public Notification createNotification(Notification notification) {
        try {
            // 设置ID和创建时间
            if (notification.getId() == null || notification.getId().isEmpty()) {
                notification.setId(UUID.randomUUID().toString());
            }
            if (notification.getCreateTime() == null) {
                notification.setCreateTime(LocalDateTime.now());
            }
            if (notification.getIsRead() == null) {
                notification.setIsRead(false);
            }

            // 保存到数据库
            save(notification);
            log.info("创建通知成功, id: {}, userId: {}, title: {}", notification.getId(), notification.getUserId(), notification.getTitle());

            // 真正推送 WebSocket（让通知真正触达在线用户）
            try {
                webSocketNotificationService.pushNotification(notification);
            } catch (Exception pushEx) {
                log.warn("推送通知失败(已入库) id={} userId={} : {}", notification.getId(), notification.getUserId(), pushEx.getMessage());
            }

            return notification;
        } catch (Exception e) {
            log.error("创建通知失败, error: {}", e.getMessage(), e);
            throw new RuntimeException("创建通知失败", e);
        }
    }

    @Override
    public int batchCreateNotification(List<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            return 0;
        }
        try {
            // 设置ID和创建时间
            for (Notification notification : notifications) {
                if (notification.getId() == null || notification.getId().isEmpty()) {
                    notification.setId(UUID.randomUUID().toString());
                }
                if (notification.getCreateTime() == null) {
                    notification.setCreateTime(LocalDateTime.now());
                }
                if (notification.getIsRead() == null) {
                    notification.setIsRead(false);
                }
            }

            // 批量保存到数据库
            boolean result = saveBatch(notifications);
            if (result) {
                log.info("批量创建通知成功, count: {}", notifications.size());

                // 真正推送 WebSocket（每条通知独立发送，目标用户为 notification.userId）
                try {
                    webSocketNotificationService.pushBatchNotifications(notifications);
                } catch (Exception pushEx) {
                    log.warn("批量推送通知失败(已入库) count={} : {}", notifications.size(), pushEx.getMessage());
                }

                return notifications.size();
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error("批量创建通知失败, error: {}", e.getMessage(), e);
            throw new RuntimeException("批量创建通知失败", e);
        }
    }

    @Override
    public List<Notification> getTaskNotificationsByUserId(String userId, String departmentId) {
        try {
            return notificationMapper.selectTaskNotificationsByUserId(userId, departmentId);
        } catch (Exception e) {
            log.error("获取用户月检任务通知失败, userId: {}, departmentId: {}, error: {}", userId, departmentId, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean pushNotificationToDepartment(String title, String content, String departmentId, 
                                              String departmentName, Integer priority, String actionUrl) {
        try {
            // 根据部门ID获取该部门的所有用户
            List<SysUser> users = sysUserService.getUsersByDeptId(departmentId);
            
            if (users.isEmpty()) {
                log.warn("部门 {} 下没有找到用户", departmentName);
                return false;
            }

            log.info("开始向部门 {} 推送通知，共 {} 个用户", departmentName, users.size());

            // 为每个用户创建并发送通知
            for (SysUser user : users) {
                try {
                    Notification notification = new Notification();
                    notification.setId(UUID.randomUUID().toString());
                    notification.setType("MANUAL_NOTIFICATION");
                    notification.setTitle(title);
                    notification.setContent(content);
                    notification.setUserId(user.getId());
                    notification.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
                    notification.setDepartmentId(departmentId);
                    notification.setDepartmentName(departmentName);
                    notification.setPriority(priority != null ? priority.toString() : "medium");
                    notification.setActionUrl(actionUrl);
                    notification.setCreateTime(LocalDateTime.now());
                    notification.setIsRead(false);

                    // 通过WebSocket发送通知
                    webSocketNotificationService.sendNotificationToUser(user.getId(), notification);
                    
                    log.info("成功向用户 {} ({}) 发送通知", user.getRealName(), user.getId());
                } catch (Exception e) {
                    log.error("向用户 {} 发送通知失败: {}", user.getId(), e.getMessage(), e);
                }
            }

            log.info("部门通知推送完成，部门: {}, 用户数: {}", departmentName, users.size());
            return true;
        } catch (Exception e) {
            log.error("推送部门通知失败, departmentId: {}, error: {}", departmentId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean pushNotificationToUser(String title, String content, String userId, 
                                        String userName, Integer priority, String actionUrl) {
        try {
            // 获取用户信息以获取部门信息
            SysUser user = sysUserService.getById(userId);
            if (user == null) {
                log.warn("用户不存在: {}", userId);
                return false;
            }

            // 创建通知对象
            Notification notification = new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setType("MANUAL_NOTIFICATION");
            notification.setTitle(title);
            notification.setContent(content);
            notification.setUserId(userId);
            notification.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
            notification.setDepartmentId(user.getDeptId());
            notification.setDepartmentName(user.getDepartment());
            notification.setPriority(priority != null ? priority.toString() : "medium");
            notification.setActionUrl(actionUrl);
            notification.setCreateTime(LocalDateTime.now());
            notification.setIsRead(false);

            // 通过WebSocket发送通知
            webSocketNotificationService.sendNotificationToUser(userId, notification);
            
            log.info("成功向用户 {} ({}) 发送手动通知", user.getRealName(), userId);
            return true;
        } catch (Exception e) {
            log.error("推送用户通知失败, userId: {}, error: {}", userId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Notification> getTaskNotificationsByDepartment(String departmentId) {
        try {
            return notificationMapper.selectTaskNotificationsByDepartment(departmentId);
        } catch (Exception e) {
            log.error("获取部门月检任务通知失败, departmentId: {}, error: {}", departmentId, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Notification> getAllTaskNotifications() {
        try {
            return notificationMapper.selectAllTaskNotifications();
        } catch (Exception e) {
            log.error("获取所有部门月检任务通知失败, error: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean markAllAsRead(String userId) {
        try {
            List<Notification> notifications = getNotificationsByUserId(userId);
            for (Notification notification : notifications) {
                if (!notification.getIsRead()) {
                    notification.setIsRead(true);
                    updateById(notification);
                }
            }
            log.info("批量标记已读成功, userId: {}, count: {}", userId, notifications.size());
            return true;
        } catch (Exception e) {
            log.error("批量标记已读失败, userId: {}, error: {}", userId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public int clearAllNotifications(String userId) {
        try {
            LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Notification::getUserId, userId);
            boolean result = remove(queryWrapper);
            if (result) {
                log.info("清空通知成功, userId: {}", userId);
                return 1;
            }
            return 0;
        } catch (Exception e) {
            log.error("清空通知失败, userId: {}, error: {}", userId, e.getMessage(), e);
            return 0;
        }
    }
}