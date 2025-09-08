package com.gjxt.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjxt.inspection.entity.MonthlyInspectionTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 月检任务Mapper接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Mapper
public interface MonthlyInspectionTaskMapper extends BaseMapper<MonthlyInspectionTask> {

    /**
     * 分页查询月检任务列表（带关联信息）
     *
     * @param page 分页参数
     * @param groupLeaderId 组长ID（可选）
     * @param deputyLeaderId 副组长ID（可选）
     * @param departmentId 部门ID（可选）
     * @param professionId 专业ID（可选）
     * @param status 状态（可选）
     * @param inspectionTime 月检时间（可选）
     * @return 月检任务分页列表
     */
    Page<MonthlyInspectionTask> selectTaskPage(
            Page<MonthlyInspectionTask> page,
            @Param("groupLeaderId") String groupLeaderId,
            @Param("deputyLeaderId") String deputyLeaderId,
            @Param("departmentId") String departmentId,
            @Param("professionId") String professionId,
            @Param("status") Integer status,
            @Param("inspectionTime") String inspectionTime
    );

    /**
     * 根据ID查询月检任务详情（带关联信息）
     *
     * @param id 任务ID
     * @return 月检任务详情
     */
    MonthlyInspectionTask selectTaskDetail(@Param("id") String id);

    /**
     * 根据组长或副组长ID查询相关任务
     *
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return 任务列表
     */
    List<MonthlyInspectionTask> selectTasksByUserId(
            @Param("userId") String userId,
            @Param("status") Integer status
    );

    /**
     * 统计各状态任务数量
     *
     * @return 状态统计结果
     */
    List<Map<String, Object>> selectStatusStatistics();

    /**
     * 统计各部门任务数量
     *
     * @param status 状态（可选）
     * @return 部门统计结果
     */
    List<Map<String, Object>> selectDepartmentStatistics(@Param("status") Integer status);

    /**
     * 统计各专业任务数量
     *
     * @param status 状态（可选）
     * @return 专业统计结果
     */
    List<Map<String, Object>> selectProfessionStatistics(@Param("status") Integer status);

    /**
     * 查询需要发送通知的任务
     *
     * @return 未发送通知的任务列表
     */
    List<MonthlyInspectionTask> selectTasksForNotification();

    /**
     * 批量更新通知发送状态
     *
     * @param taskIds 任务ID列表
     * @return 更新数量
     */
    int batchUpdateNotificationSent(@Param("taskIds") List<String> taskIds);

    /**
     * 根据任务编号查询任务
     *
     * @param taskNo 任务编号
     * @return 月检任务
     */
    MonthlyInspectionTask selectByTaskNo(@Param("taskNo") String taskNo);
}