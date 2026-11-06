package com.gjxt.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjxt.inspection.entity.MonthlyInspectionTask;
import com.gjxt.inspection.entity.Notification;
import com.gjxt.inspection.entity.SysUser;
import com.gjxt.inspection.mapper.MonthlyInspectionTaskMapper;
import com.gjxt.inspection.service.IMonthlyInspectionTaskService;
import com.gjxt.inspection.service.ISysUserService;
import com.gjxt.inspection.service.WebSocketNotificationService;
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
                                                   Integer status, String taskName,
                                                   LocalDateTime inspectionStartTime,
                                                   LocalDateTime inspectionEndTime) {
        Page<MonthlyInspectionTask> page = new Page<>(current, size);
        Page<MonthlyInspectionTask> result = monthlyInspectionTaskMapper.selectTaskPage(
                page, status, taskName, inspectionStartTime, inspectionEndTime);
        // 自动根据时间区间刷新状态展示字段
        LocalDateTime now = LocalDateTime.now();
        if (result.getRecords() != null) {
            result.getRecords().forEach(t -> fillComputedStatus(t, now));
        }
        return result;
    }

    @Override
    public MonthlyInspectionTask getTaskDetail(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }
        MonthlyInspectionTask task = monthlyInspectionTaskMapper.selectTaskDetail(id);
        if (task != null) {
            fillComputedStatus(task, LocalDateTime.now());
        }
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTask(MonthlyInspectionTask task, String createUserId) {
        if (task == null) {
            return false;
        }

        validateTimeRanges(task);

        task.setId(UUID.randomUUID().toString().replace("-", ""));
        task.setCreateUserId(createUserId);
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        task.setDeleted(0);

        if (!StringUtils.hasText(task.getTaskNo())) {
            task.setTaskNo(generateTaskNo());
        }
        if (!StringUtils.hasText(task.getTaskName())) {
            task.setTaskName(task.getTaskNo());
        }
        if (task.getStatus() == null) {
            task.setStatus(computeStatusByTime(task, LocalDateTime.now()));
        }
        if (task.getNotificationSent() == null) {
            task.setNotificationSent(0);
        }

        return save(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTask(MonthlyInspectionTask task) {
        if (task == null || !StringUtils.hasText(task.getId())) {
            return false;
        }
        validateTimeRanges(task);

        // 重新计算当前状态
        task.setStatus(computeStatusByTime(task, LocalDateTime.now()));
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
        if (result) {
            cleanNotifications(id);
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
        if (result) {
            for (String id : ids) {
                cleanNotifications(id);
            }
        }
        return result;
    }

    @Override
    public List<MonthlyInspectionTask> getTasksByUserId(String userId, Integer status) {
        if (!StringUtils.hasText(userId)) {
            return null;
        }
        List<MonthlyInspectionTask> tasks = monthlyInspectionTaskMapper.selectTasksByUserId(userId, status);
        if (tasks != null) {
            LocalDateTime now = LocalDateTime.now();
            tasks.forEach(t -> fillComputedStatus(t, now));
        }
        return tasks;
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
        if (result && status == 2) {
            cleanNotifications(id);
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
        if (result && status == 2) {
            for (String id : ids) {
                cleanNotifications(id);
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
        // 任务不再持有 profession_id，专业维度已废弃
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
            log.info("开始发送任务通知，任务ID: {}, 任务编号: {}", taskId, task.getTaskNo());

            // 新模式下不再有组长/副组长，向所有启用用户广播
            List<SysUser> users = sysUserService.listEnabledUsers();
            if (users != null) {
                for (SysUser user : users) {
                    sendNotificationToUser(user, task);
                }
            }

            LambdaUpdateWrapper<MonthlyInspectionTask> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MonthlyInspectionTask::getId, taskId)
                    .set(MonthlyInspectionTask::getNotificationSent, 1)
                    .set(MonthlyInspectionTask::getUpdateTime, LocalDateTime.now());
            boolean updateResult = update(updateWrapper);
            if (!updateResult) {
                throw new RuntimeException("更新通知发送状态失败");
            }
            return true;
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            log.error("发送任务通知失败，任务ID: {}, 错误: {}", taskId, e.getMessage(), e);
            throw new RuntimeException("发送任务通知失败: " + e.getMessage());
        }
    }

    private void sendNotificationToUser(SysUser user, MonthlyInspectionTask task) {
        try {
            if (user == null || !StringUtils.hasText(user.getId())) {
                return;
            }
            Notification notification = new Notification();
            notification.setType("TASK_NOTIFICATION");
            notification.setTitle("新的月检任务通知");
            notification.setUserId(user.getId());
            notification.setTaskId(task.getId());
            notification.setTaskNo(task.getTaskNo());
            notification.setPriority("high");
            notification.setActionUrl("/monthly-tasks/" + task.getId());
            notification.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
            notification.setDepartmentId(user.getDeptId());
            notification.setDepartmentName(user.getDeptName());

            String inspectionRange = formatRange(task.getInspectionStartTime(), task.getInspectionEndTime());
            String publicityRange = formatRange(task.getPublicityStartTime(), task.getPublicityEndTime());
            String content = String.format(
                    "您有新的月检任务需要处理：\n" +
                            "任务名称：%s\n" +
                            "任务编号：%s\n" +
                            "月检时间段：%s\n" +
                            "公示时间段：%s\n" +
                            "任务描述：%s\n\n" +
                            "请在月检时间段内录入问题记录。",
                    task.getTaskName() != null ? task.getTaskName() : task.getTaskNo(),
                    task.getTaskNo(),
                    inspectionRange,
                    publicityRange,
                    task.getTaskDescription() != null ? task.getTaskDescription() : "无"
            );
            notification.setContent(content);

            webSocketNotificationService.sendNotificationToUser(user.getId(), notification);
        } catch (Exception e) {
            log.error("发送通知给用户失败，用户ID: {}, 错误: {}", user != null ? user.getId() : null, e.getMessage());
        }
    }

    private String formatRange(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (start == null && end == null) {
            return "未设置";
        }
        if (start != null && end != null) {
            return start.format(formatter) + " ~ " + end.format(formatter);
        }
        return start != null ? start.format(formatter) : end.format(formatter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSendTaskNotifications() {
        try {
            List<MonthlyInspectionTask> tasks = monthlyInspectionTaskMapper.selectTasksForNotification();
            if (tasks == null || tasks.isEmpty()) {
                return true;
            }
            List<String> taskIds = tasks.stream().map(MonthlyInspectionTask::getId).collect(java.util.stream.Collectors.toList());
            return monthlyInspectionTaskMapper.batchUpdateNotificationSent(taskIds) > 0;
        } catch (Exception e) {
            log.error("批量发送任务通知失败", e);
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
        if (!StringUtils.hasText(userId)) return 0;
        List<MonthlyInspectionTask> tasks = getTasksByUserId(userId, 0);
        return tasks != null ? tasks.size() : 0;
    }

    @Override
    public long getInProgressTaskCount(String userId) {
        if (!StringUtils.hasText(userId)) return 0;
        List<MonthlyInspectionTask> tasks = getTasksByUserId(userId, 1);
        return tasks != null ? tasks.size() : 0;
    }

    @Override
    public long getCompletedTaskCount(String userId) {
        if (!StringUtils.hasText(userId)) return 0;
        List<MonthlyInspectionTask> tasks = getTasksByUserId(userId, 2);
        return tasks != null ? tasks.size() : 0;
    }

    /**
     * 时间区间校验：
     * - 月检开始 < 月检结束
     * - 月检结束 <= 公示开始
     * - 公示开始 < 公示结束
     */
    private void validateTimeRanges(MonthlyInspectionTask task) {
        if (task.getInspectionStartTime() != null && task.getInspectionEndTime() != null
                && !task.getInspectionStartTime().isBefore(task.getInspectionEndTime())) {
            throw new RuntimeException("月检开始时间必须早于月检结束时间");
        }
        if (task.getPublicityStartTime() != null && task.getPublicityEndTime() != null
                && !task.getPublicityStartTime().isBefore(task.getPublicityEndTime())) {
            throw new RuntimeException("公示开始时间必须早于公示结束时间");
        }
        if (task.getInspectionEndTime() != null && task.getPublicityStartTime() != null
                && task.getInspectionEndTime().isAfter(task.getPublicityStartTime())) {
            throw new RuntimeException("公示开始时间必须不早于月检结束时间");
        }
    }

    /**
     * 按当前时间计算任务应处的状态。
     * 0=未开始（录入期前），1=进行中（录入期内），2=已完成（公示期后），3=已取消（人工设定）
     */
    private int computeStatusByTime(MonthlyInspectionTask task, LocalDateTime now) {
        if (task == null || now == null) {
            return 0;
        }
        if (task.getInspectionStartTime() != null && task.getInspectionEndTime() != null
                && !now.isBefore(task.getInspectionStartTime())
                && !now.isAfter(task.getInspectionEndTime())) {
            return 1;
        }
        if (task.getPublicityEndTime() != null && now.isAfter(task.getPublicityEndTime())) {
            return 2;
        }
        return 0;
    }

    /**
     * 填充状态展示：当数据库中 status 与时间推断的状态不一致时，使用时间推断结果覆盖 status
     * 仅用于前端展示（实体对象 setStatus 不写回数据库）。
     */
    private void fillComputedStatus(MonthlyInspectionTask task, LocalDateTime now) {
        if (task == null) return;
        if (task.getStatus() == null || task.getStatus() == 3) {
            // 3=已取消保留
            return;
        }
        task.setStatus(computeStatusByTime(task, now));
    }

    private void cleanNotifications(String taskId) {
        try {
            webSocketNotificationService.deleteNotificationsByTaskId(taskId);
            log.info("已清理任务相关通知，任务ID: {}", taskId);
        } catch (Exception e) {
            log.error("清理任务通知失败，任务ID: {}", taskId, e);
        }
    }
}
