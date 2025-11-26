package com.gjxt.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjxt.inspection.entity.MonthlyInspectionTask;
import com.gjxt.inspection.entity.Notification;
import com.gjxt.inspection.mapper.MonthlyInspectionTaskMapper;
import com.gjxt.inspection.service.IMonthlyInspectionTaskService;
import com.gjxt.inspection.service.ISysUserService;
import com.gjxt.inspection.service.WebSocketNotificationService;
import com.gjxt.inspection.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 月检任务服务实现类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Slf4j
@Service
public class MonthlyInspectionTaskServiceImpl extends ServiceImpl<MonthlyInspectionTaskMapper, MonthlyInspectionTask>
        implements IMonthlyInspectionTaskService {

    @Autowired
    private MonthlyInspectionTaskMapper monthlyInspectionTaskMapper;

    @Autowired
    private WebSocketNotificationService webSocketNotificationService;
    
    @Autowired
    private ISysUserService sysUserService;

    @Override
    public Page<MonthlyInspectionTask> getTaskPage(long current, long size,
                                                   String groupLeaderId, String deputyLeaderId,
                                                   String departmentId, String professionId,
                                                   Integer status, String inspectionTime) {
        Page<MonthlyInspectionTask> page = new Page<>(current, size);
        return monthlyInspectionTaskMapper.selectTaskPage(page, groupLeaderId, deputyLeaderId,
                departmentId, professionId, status, inspectionTime);
    }

    @Override
    public MonthlyInspectionTask getTaskDetail(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }
        return monthlyInspectionTaskMapper.selectTaskDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTask(MonthlyInspectionTask task, String createUserId) {
        if (task == null) {
            return false;
        }

        // 设置基础信息
        task.setId(UUID.randomUUID().toString().replace("-", ""));
        task.setCreateUserId(createUserId);
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        task.setDeleted(0);

        // 生成任务编号
        if (!StringUtils.hasText(task.getTaskNo())) {
            task.setTaskNo(generateTaskNo());
        }

        // 设置默认状态和通知状态
        if (task.getStatus() == null) {
            task.setStatus(0); // 默认未开始
        }
        if (task.getNotificationSent() == null) {
            task.setNotificationSent(0); // 默认未发送通知
        }

        boolean result = save(task);

        // 不再自动发送通知，需要手动点击通知按钮才发送
        // 创建任务时 notificationSent 已设置为 0（未发送通知）

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTask(MonthlyInspectionTask task) {
        if (task == null || !StringUtils.hasText(task.getId())) {
            return false;
        }

        task.setUpdateTime(LocalDateTime.now());
        return updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTask(String id) {
        if (!StringUtils.hasText(id)) {
            return false;
        }

        LambdaUpdateWrapper<MonthlyInspectionTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MonthlyInspectionTask::getId, id)
                .set(MonthlyInspectionTask::getDeleted, 1)
                .set(MonthlyInspectionTask::getUpdateTime, LocalDateTime.now());

        boolean result = update(updateWrapper);
        
        // 删除任务时清理相关通知
        if (result) {
            try {
                webSocketNotificationService.deleteNotificationsByTaskId(id);
                log.info("已清理任务相关通知，任务ID: {}", id);
            } catch (Exception e) {
                log.error("清理任务通知失败，任务ID: {}", id, e);
                // 不影响主要业务流程，只记录错误日志
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTasks(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<MonthlyInspectionTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(MonthlyInspectionTask::getId, ids)
                .set(MonthlyInspectionTask::getDeleted, 1)
                .set(MonthlyInspectionTask::getUpdateTime, LocalDateTime.now());

        boolean result = update(updateWrapper);
        
        // 批量删除任务时清理相关通知
        if (result) {
            for (String id : ids) {
                try {
                    webSocketNotificationService.deleteNotificationsByTaskId(id);
                    log.info("已清理任务相关通知，任务ID: {}", id);
                } catch (Exception e) {
                    log.error("清理任务通知失败，任务ID: {}", id, e);
                    // 不影响主要业务流程，只记录错误日志
                }
            }
        }

        return result;
    }

    @Override
    public List<MonthlyInspectionTask> getTasksByUserId(String userId, Integer status) {
        if (!StringUtils.hasText(userId)) {
            return null;
        }
        return monthlyInspectionTaskMapper.selectTasksByUserId(userId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTaskStatus(String id, Integer status) {
        if (!StringUtils.hasText(id) || status == null) {
            return false;
        }

        LambdaUpdateWrapper<MonthlyInspectionTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MonthlyInspectionTask::getId, id)
                .set(MonthlyInspectionTask::getStatus, status)
                .set(MonthlyInspectionTask::getUpdateTime, LocalDateTime.now());

        boolean result = update(updateWrapper);
        
        // 当任务状态更新为已完成时，清理相关通知
        if (result && status == 2) { // 状态2表示已完成
            try {
                webSocketNotificationService.deleteNotificationsByTaskId(id);
                log.info("任务已完成，已清理相关通知，任务ID: {}", id);
            } catch (Exception e) {
                log.error("清理任务通知失败，任务ID: {}", id, e);
                // 不影响主要业务流程，只记录错误日志
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateTaskStatus(List<String> ids, Integer status) {
        if (ids == null || ids.isEmpty() || status == null) {
            return false;
        }

        LambdaUpdateWrapper<MonthlyInspectionTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(MonthlyInspectionTask::getId, ids)
                .set(MonthlyInspectionTask::getStatus, status)
                .set(MonthlyInspectionTask::getUpdateTime, LocalDateTime.now());

        boolean result = update(updateWrapper);
        
        // 当批量更新任务状态为已完成时，清理相关通知
        if (result && status == 2) { // 状态2表示已完成
            for (String id : ids) {
                try {
                    webSocketNotificationService.deleteNotificationsByTaskId(id);
                    log.info("任务已完成，已清理相关通知，任务ID: {}", id);
                } catch (Exception e) {
                    log.error("清理任务通知失败，任务ID: {}", id, e);
                    // 不影响主要业务流程，只记录错误日志
                }
            }
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getStatusStatistics() {
        return monthlyInspectionTaskMapper.selectStatusStatistics();
    }

    @Override
    public List<Map<String, Object>> getDepartmentStatistics(Integer status) {
        return monthlyInspectionTaskMapper.selectDepartmentStatistics(status);
    }

    @Override
    public List<Map<String, Object>> getProfessionStatistics(Integer status) {
        return monthlyInspectionTaskMapper.selectProfessionStatistics(status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendTaskNotification(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new RuntimeException("任务ID不能为空");
        }

        MonthlyInspectionTask task = getTaskDetail(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        try {
            // 实现具体的通知发送逻辑
            log.info("开始发送任务通知，任务ID: {}, 任务编号: {}", taskId, task.getTaskNo());
            
            // 1. 发送给组长
            if (StringUtils.hasText(task.getGroupLeaderId())) {
                sendNotificationToUser(task.getGroupLeaderId(), task, "组长");
                log.info("已发送通知给组长，用户ID: {}", task.getGroupLeaderId());
            }
            
            // 2. 发送给副组长
            if (StringUtils.hasText(task.getDeputyLeaderId())) {
                sendNotificationToUser(task.getDeputyLeaderId(), task, "副组长");
                log.info("已发送通知给副组长，用户ID: {}", task.getDeputyLeaderId());
            }
            
            // 3. 记录通知发送日志
            log.info("任务通知发送完成，任务编号: {}, 组长: {}, 副组长: {}", 
                    task.getTaskNo(), task.getGroupLeaderName(), task.getDeputyLeaderName());

            // 更新通知发送状态
            LambdaUpdateWrapper<MonthlyInspectionTask> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MonthlyInspectionTask::getId, taskId)
                    .set(MonthlyInspectionTask::getNotificationSent, 1)
                    .set(MonthlyInspectionTask::getUpdateTime, LocalDateTime.now());

            boolean updateResult = update(updateWrapper);
            if (!updateResult) {
                throw new RuntimeException("更新通知发送状态失败");
            }
            
            return true;
        } catch (Exception e) {
            log.error("发送任务通知失败，任务ID: {}, 错误信息: {}", taskId, e.getMessage(), e);
            throw new RuntimeException("发送任务通知失败: " + e.getMessage());
        }
    }
    
    /**
     * 发送通知给指定用户
     */
    private void sendNotificationToUser(String userId, MonthlyInspectionTask task, String role) {
        try {
            // 获取用户信息以获取部门信息
            SysUser user = sysUserService.getById(userId);
            
            // 创建通知对象
            Notification notification = new Notification();
            notification.setType("TASK_NOTIFICATION");
            notification.setTitle("新的月检任务通知");
            notification.setUserId(userId);
            notification.setTaskId(task.getId());
            notification.setTaskNo(task.getTaskNo());
            notification.setPriority("high");
            notification.setActionUrl("/monthly-tasks/" + task.getId());
            
            // 设置用户名称
            if (user != null) {
                notification.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
            }
            
            // 设置部门信息（使用任务的部门信息，而非用户的部门信息）
            notification.setDepartmentId(task.getDepartmentId());
            notification.setDepartmentName(task.getDepartmentName());
            
            // 设置通知内容
            String notificationContent = String.format(
                "您有新的月检任务需要处理：\n" +
                "任务编号：%s\n" +
                "月检时间：%s\n" +
                "您的角色：%s\n" +
                "任务描述：%s\n\n" +
                "请及时处理该任务。",
                task.getTaskNo(),
                task.getInspectionTime(),
                role,
                task.getTaskDescription() != null ? task.getTaskDescription() : "无"
            );
            notification.setContent(notificationContent);
            
            // 设置通知过期时间（任务完成或删除时自动清理）
            notification.setExpireTime(null); // 不设置过期时间，直到任务完成或删除
            
            log.info("发送WebSocket通知给用户 {} ({}): {}, 任务部门: {}", userId, role, task.getTaskNo(), 
                    task.getDepartmentName() != null ? task.getDepartmentName() : "未知");
            
            // 通过WebSocket发送通知
            webSocketNotificationService.sendNotificationToUser(userId, notification);
            
            log.info("WebSocket通知发送成功，用户: {}, 任务: {}", userId, task.getTaskNo());
            
        } catch (Exception e) {
            log.error("发送通知给用户失败，用户ID: {}, 角色: {}, 错误: {}", userId, role, e.getMessage());
            // 这里不抛出异常，避免影响整个通知流程
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSendTaskNotifications() {
        try {
            List<MonthlyInspectionTask> tasks = monthlyInspectionTaskMapper.selectTasksForNotification();
            if (tasks == null || tasks.isEmpty()) {
                return true;
            }

            List<String> taskIds = tasks.stream()
                    .map(MonthlyInspectionTask::getId)
                    .collect(java.util.stream.Collectors.toList());

            // TODO: 实现批量通知发送逻辑

            // 批量更新通知发送状态
            return monthlyInspectionTaskMapper.batchUpdateNotificationSent(taskIds) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String generateTaskNo() {
        String prefix = "YJ";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String suffix = String.format("%03d", (int) (Math.random() * 1000));
        return prefix + timestamp + suffix;
    }

    @Override
    public boolean isTaskNoExists(String taskNo) {
        if (!StringUtils.hasText(taskNo)) {
            return false;
        }
        return monthlyInspectionTaskMapper.selectByTaskNo(taskNo) != null;
    }

    @Override
    public MonthlyInspectionTask getTaskByTaskNo(String taskNo) {
        if (!StringUtils.hasText(taskNo)) {
            return null;
        }
        return monthlyInspectionTaskMapper.selectByTaskNo(taskNo);
    }

    @Override
    public long getPendingTaskCount(String userId) {
        if (!StringUtils.hasText(userId)) {
            return 0;
        }
        List<MonthlyInspectionTask> tasks = getTasksByUserId(userId, 0);
        return tasks != null ? tasks.size() : 0;
    }

    @Override
    public long getInProgressTaskCount(String userId) {
        if (!StringUtils.hasText(userId)) {
            return 0;
        }
        List<MonthlyInspectionTask> tasks = getTasksByUserId(userId, 1);
        return tasks != null ? tasks.size() : 0;
    }

    @Override
    public long getCompletedTaskCount(String userId) {
        if (!StringUtils.hasText(userId)) {
            return 0;
        }
        List<MonthlyInspectionTask> tasks = getTasksByUserId(userId, 2);
        return tasks != null ? tasks.size() : 0;
    }
}