package com.gjxt.inspection.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gjxt.inspection.entity.MonthlyInspectionTask;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 月检任务服务接口
 */
public interface IMonthlyInspectionTaskService extends IService<MonthlyInspectionTask> {

    /**
     * 分页查询月检任务列表
     *
     * @param status 状态（可选）
     * @param taskName 任务名称（可选，模糊匹配）
     * @param inspectionStartTime 月检开始时间下界（可选）
     * @param inspectionEndTime 月检结束时间上界（可选）
     */
    Page<MonthlyInspectionTask> getTaskPage(long current, long size,
                                           Integer status, String taskName,
                                           LocalDateTime inspectionStartTime,
                                           LocalDateTime inspectionEndTime);

    /**
     * 根据ID查询任务详情
     */
    MonthlyInspectionTask getTaskDetail(String id);

    /**
     * 创建月检任务
     */
    boolean createTask(MonthlyInspectionTask task, String createUserId);

    /**
     * 更新月检任务
     */
    boolean updateTask(MonthlyInspectionTask task);

    /**
     * 删除月检任务
     */
    boolean deleteTask(String id);

    /**
     * 批量删除月检任务
     */
    boolean batchDeleteTasks(List<String> ids);

    /**
     * 按创建人查询任务
     */
    List<MonthlyInspectionTask> getTasksByUserId(String userId, Integer status);

    /**
     * 更新任务状态
     */
    boolean updateTaskStatus(String id, Integer status);

    /**
     * 批量更新任务状态
     */
    boolean batchUpdateTaskStatus(List<String> ids, Integer status);

    /**
     * 获取状态统计
     */
    List<Map<String, Object>> getStatusStatistics();

    /**
     * 获取部门统计（按创建人所属部门）
     */
    List<Map<String, Object>> getDepartmentStatistics(Integer status);

    /**
     * 获取专业统计（已废弃，任务不再持有 profession_id）
     */
    List<Map<String, Object>> getProfessionStatistics(Integer status);

    /**
     * 发送任务通知（基于月检时间段向所有用户广播）
     */
    boolean sendTaskNotification(String taskId);

    /**
     * 批量发送任务通知
     */
    boolean batchSendTaskNotifications();

    /**
     * 生成任务编号
     */
    String generateTaskNo();

    /**
     * 检查任务编号是否存在
     */
    boolean isTaskNoExists(String taskNo);

    /**
     * 根据任务编号查询任务
     */
    MonthlyInspectionTask getTaskByTaskNo(String taskNo);

    /**
     * 获取用户的待办任务数量
     */
    long getPendingTaskCount(String userId);

    /**
     * 获取用户的进行中任务数量
     */
    long getInProgressTaskCount(String userId);

    /**
     * 获取用户的已完成任务数量
     */
    long getCompletedTaskCount(String userId);
}
