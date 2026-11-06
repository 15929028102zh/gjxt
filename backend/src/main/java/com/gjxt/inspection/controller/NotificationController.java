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
import org.springframework.web.util.HtmlUtils;
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
     * HTML转义，防止XSS攻击（使用Spring HtmlUtils）
     */
    private String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        return HtmlUtils.htmlEscape(input);
    }

    /**
     * 获取当前用户的通知列表
     *
     * @return 通知列表
     */
    @GetMapping("/user/notifications")
    public ResponseEntity<List<Notification>> getUserNotifications() {
        try {
            // 从认证信息获取当前用户ID，防止IDOR攻击
            String currentUserId = userContextUtil.getCurrentUserId();
            if (currentUserId == null || currentUserId.isEmpty()) {
                log.warn("获取通知失败：无法获取当前用户信息");
                return ResponseEntity.status(401).build();
            }

            // 清理过期通知
            baseNotificationService.cleanExpiredNotifications(currentUserId);

            // 获取当前用户的部门ID，按部门过滤通知
            String currentUserDeptId = userContextUtil.getCurrentUserDeptId();
            List<Notification> notifications;

            if (currentUserDeptId != null && !currentUserDeptId.isEmpty()) {
                // 按部门过滤通知
                notifications = baseNotificationService.getNotificationsByUserIdAndDepartment(currentUserId, currentUserDeptId);
                log.info("按部门过滤获取用户通知, userId: {}, deptId: {}, count: {}", currentUserId, currentUserDeptId, notifications.size());
            } else {
                // 获取所有通知
                notifications = baseNotificationService.getNotificationsByUserId(currentUserId);
                log.info("获取用户所有通知, userId: {}, count: {}", currentUserId, notifications.size());
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
            // 从认证信息获取当前用户ID，防止IDOR攻击
            String currentUserId = userContextUtil.getCurrentUserId();
            if (currentUserId == null || currentUserId.isEmpty()) {
                return Result.error("用户未认证");
            }

            // 检查通知是否属于当前用户（防止删除他人通知）
            Notification notification = baseNotificationService.getById(id);
            if (notification == null) {
                return Result.error("通知不存在");
            }
            if (!currentUserId.equals(notification.getUserId())) {
                return Result.error("无权删除他人通知");
            }

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
     * @param notificationId 通知ID
     * @return 操作结果
     */
    @PutMapping("/read/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable String notificationId) {
        try {
            // 从认证信息获取当前用户ID，防止IDOR攻击
            String currentUserId = userContextUtil.getCurrentUserId();
            if (currentUserId == null || currentUserId.isEmpty()) {
                return ResponseEntity.status(401).body("用户未认证");
            }

            notificationService.markNotificationAsRead(currentUserId, notificationId);
            return ResponseEntity.ok("通知已标记为已读");
        } catch (Exception e) {
            log.error("标记通知已读失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("标记通知已读失败");
        }
    }

    /**
     * 删除通知
     *
     * @param notificationId 通知ID
     * @return 操作结果
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable String notificationId) {
        try {
            // 从认证信息获取当前用户ID，防止IDOR攻击
            String currentUserId = userContextUtil.getCurrentUserId();
            if (currentUserId == null || currentUserId.isEmpty()) {
                return ResponseEntity.status(401).body("用户未认证");
            }

            notificationService.deleteNotification(currentUserId, notificationId);
            return ResponseEntity.ok("通知已删除");
        } catch (Exception e) {
            log.error("删除通知失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("删除通知失败");
        }
    }

    /**
     * 获取当前用户未读通知数量
     *
     * @return 未读通知数量
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Integer> getUnreadCount() {
        try {
            // 从认证信息获取当前用户ID，防止IDOR攻击
            String currentUserId = userContextUtil.getCurrentUserId();
            if (currentUserId == null || currentUserId.isEmpty()) {
                return ResponseEntity.status(401).build();
            }

            // 直接走 SQL 聚合,避免拉全量通知再统计
            int unreadCount = baseNotificationService.getUnreadCount(currentUserId);
            return ResponseEntity.ok(unreadCount);
        } catch (Exception e) {
            log.error("获取未读通知数量失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 批量标记通知为已读
     *
     * @return 操作结果
     */
    @PutMapping("/read-all")
    public ResponseEntity<String> markAllAsRead() {
        try {
            // 从认证信息获取当前用户ID，防止IDOR攻击
            String currentUserId = userContextUtil.getCurrentUserId();
            if (currentUserId == null || currentUserId.isEmpty()) {
                return ResponseEntity.status(401).body("用户未认证");
            }

            baseNotificationService.markAllAsRead(currentUserId);
            return ResponseEntity.ok("所有通知已标记为已读");
        } catch (Exception e) {
            log.error("批量标记已读失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("批量标记已读失败");
        }
    }

    /**
     * 清空用户所有通知
     *
     * @return 操作结果
     */
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAllNotifications() {
        try {
            // 从认证信息获取当前用户ID，防止IDOR攻击
            String currentUserId = userContextUtil.getCurrentUserId();
            if (currentUserId == null || currentUserId.isEmpty()) {
                return ResponseEntity.status(401).body("用户未认证");
            }

            baseNotificationService.clearAllNotifications(currentUserId);
            return ResponseEntity.ok("所有通知已清空");
        } catch (Exception e) {
            log.error("清空通知失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("清空通知失败");
        }
    }

    /**
     * 获取当前用户的月检任务通知列表（已通知状态的月检任务）
     *
     * @return 月检任务通知列表
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<Notification>> getTaskNotifications() {
        try {
            // 从认证信息获取当前用户ID，防止IDOR攻击
            String currentUserId = userContextUtil.getCurrentUserId();
            if (currentUserId == null || currentUserId.isEmpty()) {
                return ResponseEntity.status(401).build();
            }

            // 获取当前用户的部门ID
            String currentUserDeptId = userContextUtil.getCurrentUserDeptId();

            // 直接在数据库层按部门过滤获取月检任务通知列表
            List<Notification> taskNotifications;
            if (currentUserDeptId != null && !currentUserDeptId.isEmpty()) {
                taskNotifications = baseNotificationService.getTaskNotificationsByUserId(currentUserId, currentUserDeptId);
                log.info("按部门过滤获取用户月检任务通知, userId: {}, deptId: {}, count: {}", currentUserId, currentUserDeptId, taskNotifications.size());
            } else {
                // 如果没有部门ID，返回空列表
                taskNotifications = Collections.emptyList();
                log.info("用户无部门信息，返回空的月检任务通知列表, userId: {}", currentUserId);
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

            // HTML转义防止XSS攻击
            String escapedTitle = escapeHtml(title);
            String escapedContent = escapeHtml(content);

            boolean result = baseNotificationService.pushNotificationToDepartment(
                    escapedTitle, escapedContent, departmentId, departmentName, priority, actionUrl
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

            // HTML转义防止XSS攻击
            String escapedTitle = escapeHtml(title);
            String escapedContent = escapeHtml(content);

            boolean result = baseNotificationService.pushNotificationToUser(
                    escapedTitle, escapedContent, userId, userName, priority, actionUrl
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