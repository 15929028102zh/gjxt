package com.gjxt.inspection.controller;

import com.gjxt.inspection.entity.Notification;
import com.gjxt.inspection.service.NotificationService;
import com.gjxt.inspection.service.WebSocketNotificationService;
import com.gjxt.inspection.util.UserContextUtil;
import com.gjxt.inspection.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 通知控制器
 * 处理通知相关的HTTP请求
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private WebSocketNotificationService notificationService;

    @Autowired
    private NotificationService baseNotificationService;

    @Autowired
    private UserContextUtil userContextUtil;

    /**
     * 获取用户的所有通知
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable String userId) {
        try {
            // 清理过期通知
            baseNotificationService.cleanExpiredNotifications(userId);
            
            // 获取当前用户的部门ID，按部门过滤通知
            String currentUserDeptId = userContextUtil.getCurrentUserDeptId();
            List<Notification> notifications;
            
            if (currentUserDeptId != null && !currentUserDeptId.isEmpty()) {
                // 按部门过滤通知
                notifications = baseNotificationService.getNotificationsByUserIdAndDepartment(userId, currentUserDeptId);
                log.info("按部门过滤获取用户通知, userId: {}, deptId: {}, count: {}", userId, currentUserDeptId, notifications.size());
            } else {
                // 获取所有通知
                notifications = baseNotificationService.getNotificationsByUserId(userId);
                log.info("获取用户所有通知, userId: {}, count: {}", userId, notifications.size());
            }
            
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            log.error("获取用户通知失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 分页获取所有通知（管理员界面）
     */
    @GetMapping("/page")
    public Result<Page<Notification>> getAllNotificationsPage(@RequestParam(defaultValue = "1") long current,
                                                              @RequestParam(defaultValue = "20") long size,
                                                              @RequestParam(required = false) String title,
                                                              @RequestParam(required = false) String type,
                                                              @RequestParam(required = false) String isRead) {
        try {
            Page<Notification> page = new Page<>(current, size);
            QueryWrapper<Notification> qw = new QueryWrapper<>();
            if (title != null && !title.isEmpty()) {
                qw.like("title", title);
            }
            if (type != null && !type.isEmpty()) {
                qw.eq("type", type);
            }
            if (isRead != null && !isRead.isEmpty()) {
                qw.eq("is_read", "1".equals(isRead) || "true".equalsIgnoreCase(isRead));
            }
            qw.orderByDesc("create_time");
            Page<Notification> result = baseNotificationService.page(page, qw);
            return Result.success(result);
        } catch (Exception e) {
            log.error("分页获取通知失败: {}", e.getMessage(), e);
            return Result.error("分页获取通知失败");
        }
    }

    /**
     * 删除通知（按ID）
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteById(@PathVariable String id) {
        try {
            boolean ok = baseNotificationService.removeById(id);
            return ok ? Result.success("删除成功") : Result.error("删除失败")
            ;
        } catch (Exception e) {
            log.error("删除通知失败: {}", e.getMessage(), e);
            return Result.error("删除通知失败");
        }
    }
    /**
     * 标记通知为已读
     *
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 操作结果
     */
    @PutMapping("/user/{userId}/read/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable String userId, @PathVariable String notificationId) {
        try {
            notificationService.markNotificationAsRead(userId, notificationId);
            return ResponseEntity.ok("通知已标记为已读");
        } catch (Exception e) {
            log.error("标记通知已读失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("标记通知已读失败");
        }
    }

    /**
     * 删除通知
     *
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 操作结果
     */
    @DeleteMapping("/user/{userId}/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable String userId, @PathVariable String notificationId) {
        try {
            notificationService.deleteNotification(userId, notificationId);
            return ResponseEntity.ok("通知已删除");
        } catch (Exception e) {
            log.error("删除通知失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("删除通知失败");
        }
    }

    /**
     * 获取用户未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Integer> getUnreadCount(@PathVariable String userId) {
        try {
            List<Notification> notifications = notificationService.getUserNotifications(userId);
            int unreadCount = (int) notifications.stream()
                .filter(n -> !n.getIsRead())
                .count();
            return ResponseEntity.ok(unreadCount);
        } catch (Exception e) {
            log.error("获取未读通知数量失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取用户的月检任务通知列表（已通知状态的月检任务）
     *
     * @param userId 用户ID
     * @return 月检任务通知列表
     */
    @GetMapping("/user/{userId}/tasks")
    public ResponseEntity<List<Notification>> getTaskNotifications(@PathVariable String userId) {
        try {
            // 获取当前用户的部门ID
            String currentUserDeptId = userContextUtil.getCurrentUserDeptId();
            
            // 直接在数据库层按部门过滤获取月检任务通知列表
            List<Notification> taskNotifications;
            if (currentUserDeptId != null && !currentUserDeptId.isEmpty()) {
                taskNotifications = baseNotificationService.getTaskNotificationsByUserId(userId, currentUserDeptId);
                log.info("按部门过滤获取用户月检任务通知, userId: {}, deptId: {}, count: {}", userId, currentUserDeptId, taskNotifications.size());
            } else {
                // 如果没有部门ID，返回空列表
                taskNotifications = Collections.emptyList();
                log.info("用户无部门信息，返回空的月检任务通知列表, userId: {}", userId);
            }
            
            return ResponseEntity.ok(taskNotifications);
        } catch (Exception e) {
            log.error("获取用户月检任务通知失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取指定部门的月检任务通知列表（已通知状态的月检任务）
     *
     * @param departmentId 部门ID
     * @return 月检任务通知列表
     */
    @GetMapping("/department/{departmentId}/tasks")
    public ResponseEntity<List<Notification>> getDepartmentTaskNotifications(@PathVariable String departmentId) {
        try {
            // 获取指定部门的月检任务通知列表
            List<Notification> taskNotifications = baseNotificationService.getTaskNotificationsByDepartment(departmentId);
            log.info("获取部门月检任务通知, deptId: {}, count: {}", departmentId, taskNotifications.size());
            
            return ResponseEntity.ok(taskNotifications);
        } catch (Exception e) {
            log.error("获取部门月检任务通知失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取所有部门的月检任务通知列表（管理员使用）
     *
     * @return 月检任务通知列表
     */
    @GetMapping("/department/tasks")
    public ResponseEntity<List<Notification>> getAllDepartmentTaskNotifications() {
        try {
            // 获取所有部门的月检任务通知列表（管理员使用）
            List<Notification> taskNotifications = baseNotificationService.getAllTaskNotifications();
            log.info("获取所有部门月检任务通知, count: {}", taskNotifications.size());
            
            return ResponseEntity.ok(taskNotifications);
        } catch (Exception e) {
            log.error("获取所有部门月检任务通知失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 手动推送通知到指定部门
     */
    @Operation(summary = "手动推送通知到指定部门")
    @PostMapping("/push-to-department")
    public Result<String> pushNotificationToDepartment(
            @Parameter(description = "通知信息") @RequestBody Map<String, Object> notificationData
    ) {
        try {
            String title = (String) notificationData.get("title");
            String content = (String) notificationData.get("content");
            String departmentId = (String) notificationData.get("departmentId");
            String departmentName = (String) notificationData.get("departmentName");
            Integer priority = (Integer) notificationData.get("priority");
            String actionUrl = (String) notificationData.get("actionUrl");

            if (title == null || content == null) {
                return Result.error("标题和内容不能为空");
            }

            boolean result = baseNotificationService.pushNotificationToDepartment(
                    title, content, departmentId, departmentName, priority, actionUrl
            );
            
            if (result) {
                return Result.success("推送通知成功");
            } else {
                return Result.error("推送通知失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("推送通知失败：" + e.getMessage());
        }
    }

    /**
     * 手动推送通知到指定用户
     */
    @Operation(summary = "手动推送通知到指定用户")
    @PostMapping("/push-to-user")
    public Result<String> pushNotificationToUser(
            @Parameter(description = "通知信息") @RequestBody Map<String, Object> notificationData
    ) {
        try {
            String title = (String) notificationData.get("title");
            String content = (String) notificationData.get("content");
            String userId = (String) notificationData.get("userId");
            String userName = (String) notificationData.get("userName");
            Integer priority = (Integer) notificationData.get("priority");
            String actionUrl = (String) notificationData.get("actionUrl");

            if (title == null || content == null || userId == null) {
                return Result.error("标题、内容和用户ID不能为空");
            }

            boolean result = baseNotificationService.pushNotificationToUser(
                    title, content, userId, userName, priority, actionUrl
            );
            
            if (result) {
                return Result.success("推送通知成功");
            } else {
                return Result.error("推送通知失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("推送通知失败：" + e.getMessage());
        }
    }
}