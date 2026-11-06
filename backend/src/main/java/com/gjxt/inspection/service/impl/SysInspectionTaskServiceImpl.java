package com.gjxt.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.entity.SysInspectionTask;
import com.gjxt.inspection.mapper.SysInspectionTaskMapper;
import com.gjxt.inspection.service.ISysInspectionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 巡检任务管理服务实现类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Service
public class SysInspectionTaskServiceImpl extends ServiceImpl<SysInspectionTaskMapper, SysInspectionTask> implements ISysInspectionTaskService {

    @Autowired
    private SysInspectionTaskMapper taskMapper;

    @Override
    public PageResult<SysInspectionTask> getTaskPage(Long current, Long size, String taskNo, String taskName, 
                                                    Long professionId, Long locationId, String assignedUserId, 
                                                    String status, String startTime, String endTime) {
        Page<SysInspectionTask> page = new Page<>(current, size);
        
        // 将参数映射到新的Mapper接口参数
        IPage<SysInspectionTask> pageResult = taskMapper.selectTaskPage(page, taskNo, taskName, status, assignedUserId, startTime, endTime);
        return PageResult.build(pageResult);
    }

    @Override
    public SysInspectionTask getTaskById(Long id) {
        return taskMapper.selectTaskDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTask(SysInspectionTask task) {
        // 生成任务编号
        if (!StringUtils.hasText(task.getTaskNo())) {
            task.setTaskNo(generateTaskNo());
        }
        
        // 检查任务编号是否已存在
        if (checkTaskNoExists(task.getTaskNo(), null)) {
            throw new RuntimeException("任务编号已存在");
        }
        
        // 设置创建时间
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        task.setDeleted(0);
        
        // 如果没有设置状态，默认为待分配
        if (task.getStatus() == null) {
            task.setStatus("0"); // 0-待分配
        }
        
        return this.save(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTask(SysInspectionTask task) {
        // 检查任务是否存在
        SysInspectionTask existTask = this.getById(task.getId());
        if (existTask == null || existTask.getDeleted() == 1) {
            throw new RuntimeException("巡检任务不存在");
        }
        
        // 检查任务编号是否已存在（排除自己）
        if (StringUtils.hasText(task.getTaskNo()) && checkTaskNoExists(task.getTaskNo(), task.getId())) {
            throw new RuntimeException("任务编号已存在");
        }
        
        // 设置更新时间
        task.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTask(Long id) {
        // 检查任务是否存在
        SysInspectionTask task = this.getById(id);
        if (task == null || task.getDeleted() == 1) {
            throw new RuntimeException("巡检任务不存在");
        }
        
        // 检查任务状态，已完成的任务不能删除
        if ("2".equals(task.getStatus())) {
            throw new RuntimeException("已完成的任务不能删除");
        }
        
        // 逻辑删除
        task.setDeleted(1);
        task.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTasks(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("请选择要删除的巡检任务");
        }
        
        for (String id : ids) {
            deleteTask(Long.valueOf(id));
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignTask(String taskId, String assignedUserId, String plannedInspectionTime) {
        // 检查任务是否存在
        SysInspectionTask task = this.getById(taskId);
        if (task == null || task.getDeleted() == 1) {
            throw new RuntimeException("巡检任务不存在");
        }
        
        // 检查任务状态，只有待分配的任务才能分配
        if (!"pending".equals(task.getStatus())) {
            throw new RuntimeException("只有待分配的任务才能分配");
        }
        
        // 更新任务信息
        task.setUserId(assignedUserId);
        task.setStatus("in_progress"); // in_progress-进行中
        if (StringUtils.hasText(plannedInspectionTime)) {
            // 这里可以解析plannedInspectionTime字符串并设置到scheduleTime字段
            // task.setScheduleTime(LocalDateTime.parse(plannedInspectionTime));
        }
        task.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeTask(Long taskId) {
        // 检查任务是否存在
        SysInspectionTask task = this.getById(taskId);
        if (task == null || task.getDeleted() == 1) {
            throw new RuntimeException("巡检任务不存在");
        }
        
        // 检查任务状态，只有进行中的任务才能完成
        if (!"in_progress".equals(task.getStatus())) {
            throw new RuntimeException("只有进行中的任务才能完成");
        }
        
        // 更新任务信息
        task.setStatus("completed"); // completed-已完成
        task.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(task);
    }

    @Override
    public PageResult<SysInspectionTask> getUserTasks(String userId, Long current, Long size, String status) {
        Page<SysInspectionTask> page = new Page<>(current, size);
        IPage<SysInspectionTask> pageResult = taskMapper.selectUserTasks(page, userId, status);
        return PageResult.build(pageResult);
    }

    @Override
    public boolean checkTaskNoExists(String taskNo, String excludeId) {
        if (!StringUtils.hasText(taskNo)) {
            return false;
        }
        
        int count = taskMapper.checkTaskNoExists(taskNo, excludeId);
        return count > 0;
    }

    @Override
    public String generateTaskNo() {
        // 生成任务编号：RW + 年月日 + 4位流水号
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "RW" + dateStr;
        
        // 查询当天最大的流水号
        LambdaQueryWrapper<SysInspectionTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(SysInspectionTask::getTaskNo, prefix)
                   .eq(SysInspectionTask::getDeleted, 0)
                   .orderByDesc(SysInspectionTask::getTaskNo)
                   .last("LIMIT 1");
        
        SysInspectionTask lastTask = this.getOne(queryWrapper);
        
        int sequence = 1;
        if (lastTask != null && StringUtils.hasText(lastTask.getTaskNo())) {
            String lastTaskNo = lastTask.getTaskNo();
            if (lastTaskNo.length() >= 4) {
                try {
                    String lastSequenceStr = lastTaskNo.substring(lastTaskNo.length() - 4);
                    int lastSequence = Integer.parseInt(lastSequenceStr);
                    sequence = lastSequence + 1;
                } catch (NumberFormatException e) {
                    // 解析失败，使用默认序号1
                }
            }
        }
        
        return prefix + String.format("%04d", sequence);
    }

    @Override
    public List<SysInspectionTask> getPendingInspectionTasks() {
        LambdaQueryWrapper<SysInspectionTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysInspectionTask::getStatus, "PENDING")
                   .eq(SysInspectionTask::getDeleted, 0)
                   .orderByDesc(SysInspectionTask::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<SysInspectionTask> getCompletedInspectionTasks() {
        LambdaQueryWrapper<SysInspectionTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysInspectionTask::getStatus, "COMPLETED")
                   .eq(SysInspectionTask::getDeleted, 0)
                   .orderByDesc(SysInspectionTask::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<SysInspectionTask> getOverdueInspectionTasks() {
        LambdaQueryWrapper<SysInspectionTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysInspectionTask::getStatus, "OVERDUE")
                   .eq(SysInspectionTask::getDeleted, 0)
                   .orderByDesc(SysInspectionTask::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<SysInspectionTask> getUserInspectionTasks(String userId) {
        LambdaQueryWrapper<SysInspectionTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysInspectionTask::getUserId, userId)
                   .eq(SysInspectionTask::getDeleted, 0)
                   .orderByDesc(SysInspectionTask::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public PageResult<SysInspectionTask> getInspectionTaskPage(Long current, Long size, String taskName, String locationId, String assignedUserId, Integer status) {
        Page<SysInspectionTask> page = new Page<>(current, size);
        LambdaQueryWrapper<SysInspectionTask> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(taskName)) {
            queryWrapper.like(SysInspectionTask::getTaskName, taskName);
        }
        if (StringUtils.hasText(locationId)) {
            queryWrapper.eq(SysInspectionTask::getLocationId, locationId);
        }
        if (StringUtils.hasText(assignedUserId)) {
            queryWrapper.eq(SysInspectionTask::getUserId, assignedUserId);
        }
        if (status != null) {
            queryWrapper.eq(SysInspectionTask::getStatus, status);
        }
        
        queryWrapper.eq(SysInspectionTask::getDeleted, 0)
                   .orderByDesc(SysInspectionTask::getCreateTime);
        
        Page<SysInspectionTask> pageResult = this.page(page, queryWrapper);
        return PageResult.build(pageResult);
    }

    @Override
    public boolean addInspectionTask(SysInspectionTask task) {
        task.setTaskNo(generateTaskNo());
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        task.setDeleted(0);
        return this.save(task);
    }

    @Override
    public boolean updateInspectionTask(SysInspectionTask task) {
        task.setUpdateTime(LocalDateTime.now());
        return this.updateById(task);
    }

}