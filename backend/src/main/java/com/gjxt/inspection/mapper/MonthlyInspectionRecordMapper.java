package com.gjxt.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjxt.inspection.entity.MonthlyInspectionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 月检记录Mapper接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Mapper
public interface MonthlyInspectionRecordMapper extends BaseMapper<MonthlyInspectionRecord> {

    /**
     * 分页查询月检记录（带关联信息）
     *
     * @param page 分页参数
     * @param params 查询参数
     * @return 月检记录分页数据
     */
    IPage<MonthlyInspectionRecord> selectRecordPage(Page<MonthlyInspectionRecord> page, @Param("params") Map<String, Object> params);

    /**
     * 根据ID查询月检记录详情（带关联信息）
     *
     * @param id 记录ID
     * @return 月检记录详情
     */
    MonthlyInspectionRecord selectRecordById(@Param("id") String id);

    /**
     * 查询月检统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> selectMonthlyStatistics(@Param("params") Map<String, Object> params);

    /**
     * 查询月度趋势数据
     *
     * @param params 查询参数
     * @return 趋势数据
     */
    List<Map<String, Object>> selectMonthlyTrend(@Param("params") Map<String, Object> params);

    /**
     * 根据任务ID查询月检记录列表
     *
     * @param taskId 任务ID
     * @param recordStatus 记录状态（可选）
     * @return 月检记录列表
     */
    List<MonthlyInspectionRecord> selectRecordsByTaskId(@Param("taskId") String taskId, @Param("recordStatus") Integer recordStatus);

    /**
     * 根据用户ID查询月检记录列表
     *
     * @param userId 用户ID
     * @param recordStatus 记录状态（可选）
     * @return 月检记录列表
     */
    List<MonthlyInspectionRecord> selectRecordsByUserId(@Param("userId") String userId, @Param("recordStatus") Integer recordStatus);

    /**
     * 批量更新记录状态
     *
     * @param ids 记录ID列表
     * @param status 状态
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ids") List<String> ids, @Param("status") Integer status);

    /**
     * 根据用户ID查询记录数量
     *
     * @param userId 用户ID
     * @return 记录数量
     */
    int countRecordsByUserId(@Param("userId") String userId);

    /**
     * 查询最新的记录编号
     *
     * @return 最新记录编号
     */
    String selectLatestRecordNo();
}