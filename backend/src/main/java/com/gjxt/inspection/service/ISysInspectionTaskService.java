package com.gjxt.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.entity.SysInspectionTask;

import java.util.List;

/**
 * 巡检任务管理服务接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
public interface ISysInspectionTaskService extends IService<SysInspectionTask> {

    /**
     * 分页查询巡检任务列表
     *
     * @param current 当前页
     * @param size 每页大小
     * @param taskNo 任务编号
     * @param taskName 任务名称
     * @param professionId 专业ID
     * @param locationId 部位ID
     * @param assignedUserId 分配用户ID
     * @param status 状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    PageResult<SysInspectionTask> getTaskPage(Long current, Long size, String taskNo, String taskName, 
                                              Long professionId, Long locationId, Long assignedUserId, 
                                              String status, String startTime, String endTime);

    /**
     * 根据ID查询巡检任务详情
     *
     * @param id 任务ID
     * @return 任务详情
     */
    SysInspectionTask getTaskById(Long id);

    /**
     * 新增巡检任务
     *
     * @param task 任务信息
     * @return 是否成功
     */
    boolean addTask(SysInspectionTask task);

    /**
     * 更新巡检任务
     *
     * @param task 任务信息
     * @return 是否成功
     */
    boolean updateTask(SysInspectionTask task);

    /**
     * 删除巡检任务
     *
     * @param id 任务ID
     * @return 是否成功
     */
    boolean deleteTask(Long id);

    /**
     * 批量删除巡检任务
     *
     * @param ids 任务ID列表
     * @return 是否成功
     */
    boolean batchDeleteTasks(List<String> ids);

    /**
     * 分配任务
     *
     * @param id 任务ID
     * @param assignedUserId 分配的用户ID
     * @param plannedInspectionTime 计划巡检时间
     * @return 是否成功
     */
    boolean assignTask(String id, String assignedUserId, String plannedInspectionTime);

    /**
     * 完成巡检任务
     *
     * @param id 任务ID
     * @return 是否成功
     */
    boolean completeTask(Long id);

    /**
     * 查询用户的巡检任务
     *
     * @param userId 用户ID
     * @param current 当前页
     * @param size 每页大小
     * @param status 状态
     * @return 分页结果
     */
    PageResult<SysInspectionTask> getUserTasks(Long userId, Long current, Long size, String status);

    /**
     * 检查任务编号是否存在
     *
     * @param taskNo 任务编号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkTaskNoExists(String taskNo, String excludeId);

    /**
     * 生成任务编号
     *
     * @return 任务编号
     */
    String generateTaskNo();

    /**
     * 获取待检查任务列表
     *
     * @return 待检查任务列表
     */
    List<SysInspectionTask> getPendingInspectionTasks();

    /**
     * 获取已完成任务列表
     *
     * @return 已完成任务列表
     */
    List<SysInspectionTask> getCompletedInspectionTasks();

    /**
     * 获取逾期任务列表
     *
     * @return 逾期任务列表
     */
    List<SysInspectionTask> getOverdueInspectionTasks();

    /**
     * 获取用户的巡检任务列表
     *
     * @param userId 用户ID
     * @return 用户的巡检任务列表
     */
    List<SysInspectionTask> getUserInspectionTasks(String userId);

    /**
     * 分页查询巡检任务
     *
     * @param current 当前页
     * @param size 页大小
     * @param taskName 任务名称
     * @param locationId 位置ID
     * @param assignedUserId 分配用户ID
     * @param status 状态
     * @return 分页结果
     */
    PageResult<SysInspectionTask> getInspectionTaskPage(Long current, Long size, String taskName, String locationId, String assignedUserId, Integer status);

    /**
     * 添加巡检任务
     *
     * @param task 巡检任务
     * @return 是否成功
     */
    boolean addInspectionTask(SysInspectionTask task);

    /**
     * 更新巡检任务
     *
     * @param task 巡检任务
     * @return 是否成功
     */
    boolean updateInspectionTask(SysInspectionTask task);

}