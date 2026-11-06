package com.gjxt.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjxt.inspection.entity.MonthlyInspectionTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
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
     * 分页查询月检任务列表
     *
     * @param page 分页参数
     * @param status 状态（可选）
     * @param taskName 任务名称（可选，模糊匹配）
     * @param inspectionStartTime 月检开始时间下界（可选）
     * @param inspectionEndTime 月检结束时间上界（可选）
     * @return 月检任务分页列表
     */
    Page<MonthlyInspectionTask> selectTaskPage(
            Page<MonthlyInspectionTask> page,
            @Param("status") Integer status,
            @Param("taskName") String taskName,
            @Param("inspectionStartTime") LocalDateTime inspectionStartTime,
            @Param("inspectionEndTime") LocalDateTime inspectionEndTime
    );

    /**
     * 根据ID查询月检任务详情（带关联信息）
     */
    MonthlyInspectionTask selectTaskDetail(@Param("id") String id);

    /**
     * 按创建人查询任务列表
     */
    List<MonthlyInspectionTask> selectTasksByUserId(
            @Param("userId") String userId,
            @Param("status") Integer status
    );

    /**
     * 统计各状态任务数量
     */
    List<Map<String, Object>> selectStatusStatistics();

    /**
     * 统计各部门任务数量（按创建人所属部门）
     */
    List<Map<String, Object>> selectDepartmentStatistics(@Param("status") Integer status);

    /**
     * 统计各专业任务数量（任务已不再持有 profession_id，返回空集）
     */
    List<Map<String, Object>> selectProfessionStatistics(@Param("status") Integer status);

    /**
     * 查询需要发送通知的任务
     */
    List<MonthlyInspectionTask> selectTasksForNotification();

    /**
     * 批量更新通知发送状态
     */
    int batchUpdateNotificationSent(@Param("taskIds") List<String> taskIds);

    /**
     * 根据任务编号查询任务
     */
    MonthlyInspectionTask selectByTaskNo(@Param("taskNo") String taskNo);
}
