package com.gjxt.inspection.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gjxt.inspection.entity.MonthlyInspectionTask;

import java.util.List;
import java.util.Map;

/**
 * 月检任务服务接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
public interface IMonthlyInspectionTaskService extends IService<MonthlyInspectionTask> {

    /**
     * 分页查询月检任务列表
     *
     * @param current 当前页
     * @param size 每页大小
     * @param groupLeaderId 组长ID（可选）
     * @param deputyLeaderId 副组长ID（可选）
     * @param departmentId 部门ID（可选）
     * @param professionId 专业ID（可选）
     * @param status 状态（可选）
     * @param inspectionTime 月检时间（可选）
     * @return 分页结果
     */
    Page<MonthlyInspectionTask> getTaskPage(
            long current, long size,
            String groupLeaderId, String deputyLeaderId,
            String departmentId, String professionId,
            Integer status, String inspectionTime
    );

    /**
     * 根据ID查询任务详情
     *
     * @param id 任务ID
     * @return 任务详情
     */
    MonthlyInspectionTask getTaskDetail(String id);

    /**
     * 创建月检任务
     *
     * @param task 任务信息
     * @param createUserId 创建人ID
     * @return 创建结果
     */
    boolean createTask(MonthlyInspectionTask task, String createUserId);

    /**
     * 更新月检任务
     *
     * @param task 任务信息
     * @return 更新结果
     */
    boolean updateTask(MonthlyInspectionTask task);

    /**
     * 删除月检任务
     *
     * @param id 任务ID
     * @return 删除结果
     */
    boolean deleteTask(String id);

    /**
     * 批量删除月检任务
     *
     * @param ids 任务ID列表
     * @return 删除结果
     */
    boolean batchDeleteTasks(List<String> ids);

    /**
     * 根据用户ID查询相关任务（组长或副组长）
     *
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return 任务列表
     */
    List<MonthlyInspectionTask> getTasksByUserId(String userId, Integer status);

    /**
     * 更新任务状态
     *
     * @param id 任务ID
     * @param status 新状态
     * @return 更新结果
     */
    boolean updateTaskStatus(String id, Integer status);

    /**
     * 批量更新任务状态
     *
     * @param ids 任务ID列表
     * @param status 新状态
     * @return 更新结果
     */
    boolean batchUpdateTaskStatus(List<String> ids, Integer status);

    /**
     * 获取状态统计
     *
     * @return 状态统计结果
     */
    List<Map<String, Object>> getStatusStatistics();

    /**
     * 获取部门统计
     *
     * @param status 状态（可选）
     * @return 部门统计结果
     */
    List<Map<String, Object>> getDepartmentStatistics(Integer status);

    /**
     * 获取专业统计
     *
     * @param status 状态（可选）
     * @return 专业统计结果
     */
    List<Map<String, Object>> getProfessionStatistics(Integer status);

    /**
     * 发送任务通知
     *
     * @param taskId 任务ID
     * @return 发送结果
     */
    boolean sendTaskNotification(String taskId);

    /**
     * 批量发送任务通知
     *
     * @return 发送结果
     */
    boolean batchSendTaskNotifications();

    /**
     * 生成任务编号
     *
     * @return 任务编号
     */
    String generateTaskNo();

    /**
     * 检查任务编号是否存在
     *
     * @param taskNo 任务编号
     * @return 是否存在
     */
    boolean isTaskNoExists(String taskNo);

    /**
     * 根据任务编号查询任务
     *
     * @param taskNo 任务编号
     * @return 任务信息
     */
    MonthlyInspectionTask getTaskByTaskNo(String taskNo);

    /**
     * 获取用户的待办任务数量
     *
     * @param userId 用户ID
     * @return 待办任务数量
     */
    long getPendingTaskCount(String userId);

    /**
     * 获取用户的进行中任务数量
     *
     * @param userId 用户ID
     * @return 进行中任务数量
     */
    long getInProgressTaskCount(String userId);

    /**
     * 获取用户的已完成任务数量
     *
     * @param userId 用户ID
     * @return 已完成任务数量
     */
    long getCompletedTaskCount(String userId);
}