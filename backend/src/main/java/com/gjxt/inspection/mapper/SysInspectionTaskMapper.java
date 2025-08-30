package com.gjxt.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjxt.inspection.entity.SysInspectionTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 巡检任务Mapper接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Mapper
public interface SysInspectionTaskMapper extends BaseMapper<SysInspectionTask> {

    /**
     * 分页查询巡检任务列表（带关联信息）
     *
     * @param page 分页参数
     * @param taskNo 任务编号
     * @param taskName 任务名称
     * @param status 状态
     * @param assigneeId 分配用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分页结果
     */
    IPage<SysInspectionTask> selectTaskPage(Page<SysInspectionTask> page,
                                            @Param("taskNo") String taskNo,
                                            @Param("taskName") String taskName,
                                            @Param("status") String status,
                                            @Param("assigneeId") Long assigneeId,
                                            @Param("startDate") String startDate,
                                            @Param("endDate") String endDate);

    /**
     * 根据ID查询巡检任务详情（带关联信息）
     *
     * @param id 任务ID
     * @return 任务详情
     */
    SysInspectionTask selectTaskDetail(@Param("id") Long id);

    /**
     * 查询用户的巡检任务
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param status 状态
     * @return 分页结果
     */
    IPage<SysInspectionTask> selectUserTasks(Page<SysInspectionTask> page,
                                             @Param("userId") Long userId,
                                             @Param("status") String status);

    /**
     * 检查任务编号是否存在
     *
     * @param taskNo 任务编号
     * @param excludeId 排除的ID（用于更新时检查）
     * @return 存在数量
     */
    int checkTaskNoExists(@Param("taskNo") String taskNo, @Param("excludeId") String excludeId);

}