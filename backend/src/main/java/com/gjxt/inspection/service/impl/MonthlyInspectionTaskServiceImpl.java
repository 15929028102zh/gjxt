package com.gjxt.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjxt.inspection.entity.MonthlyInspectionTask;
import com.gjxt.inspection.mapper.MonthlyInspectionTaskMapper;
import com.gjxt.inspection.service.IMonthlyInspectionTaskService;
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
@Service
public class MonthlyInspectionTaskServiceImpl extends ServiceImpl<MonthlyInspectionTaskMapper, MonthlyInspectionTask>
        implements IMonthlyInspectionTaskService {

    @Autowired
    private MonthlyInspectionTaskMapper monthlyInspectionTaskMapper;

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

        // 创建成功后发送通知
        if (result) {
            sendTaskNotification(task.getId());
        }

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

        return update(updateWrapper);
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

        return update(updateWrapper);
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

        return update(updateWrapper);
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

        return update(updateWrapper);
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
            return false;
        }

        MonthlyInspectionTask task = getTaskDetail(taskId);
        if (task == null) {
            return false;
        }

        try {
            // TODO: 实现具体的通知发送逻辑
            // 1. 发送给组长
            // 2. 发送给副组长
            // 3. 可以集成WebSocket、消息队列等方式实现实时通知

            // 更新通知发送状态
            LambdaUpdateWrapper<MonthlyInspectionTask> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MonthlyInspectionTask::getId, taskId)
                    .set(MonthlyInspectionTask::getNotificationSent, 1)
                    .set(MonthlyInspectionTask::getUpdateTime, LocalDateTime.now());

            return update(updateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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