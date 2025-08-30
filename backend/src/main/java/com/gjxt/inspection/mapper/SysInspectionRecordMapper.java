package com.gjxt.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjxt.inspection.entity.SysInspectionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 巡检记录Mapper接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Mapper
public interface SysInspectionRecordMapper extends BaseMapper<SysInspectionRecord> {

    /**
     * 分页查询巡检记录（带关联信息）
     *
     * @param page 分页参数
     * @param taskId 任务ID
     * @param groupLeaderId 检查组组长ID
     * @param deputyLeaderId 检查组副组长ID
     * @param departmentId 所属部门ID
     * @param professionId 专业ID
     * @param natureId 性质界定ID
     * @param locationId 部位ID
     * @param inspectorId 检查人ID
     * @param responsibleUnitId 责任单位ID
     * @param responsiblePersonId 责任人ID
     * @param reviewerId 复查人ID
     * @param status 状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 巡检记录分页列表
     */
    IPage<SysInspectionRecord> selectRecordPage(Page<SysInspectionRecord> page,
                                                @Param("taskId") String taskId,
                                                @Param("groupLeaderId") String groupLeaderId,
                                                @Param("deputyLeaderId") String deputyLeaderId,
                                                @Param("departmentId") String departmentId,
                                                @Param("professionId") String professionId,
                                                @Param("natureId") String natureId,
                                                @Param("locationId") String locationId,
                                                @Param("inspectorId") String inspectorId,
                                                @Param("responsibleUnitId") String responsibleUnitId,
                                                @Param("responsiblePersonId") String responsiblePersonId,
                                                @Param("reviewerId") String reviewerId,
                                                @Param("status") String status,
                                                @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);

    /**
     * 根据ID查询巡检记录详情（带关联信息）
     *
     * @param id 记录ID
     * @return 巡检记录详情
     */
    SysInspectionRecord selectRecordDetail(@Param("id") String id);

    /**
     * 统计各状态问题数量
     *
     * @return 状态统计结果
     */
    List<Map<String, Object>> selectStatusStatistics();

    /**
     * 统计各专业问题数量
     *
     * @param status 状态（可选）
     * @return 专业统计结果
     */
    List<Map<String, Object>> selectProfessionStatistics(@Param("status") String status);

    /**
     * 统计各部位问题数量
     *
     * @param status 状态（可选）
     * @return 部位统计结果
     */
    List<Map<String, Object>> selectLocationStatistics(@Param("status") String status);

    /**
     * 统计各责任人问题数量
     *
     * @param status 状态（可选）
     * @return 责任人统计结果
     */
    List<Map<String, Object>> selectResponsiblePersonStatistics(@Param("status") String status);

    /**
     * 查询延迟整改的记录
     *
     * @param currentTime 当前时间
     * @return 延迟整改记录列表
     */
    List<SysInspectionRecord> selectOverdueRecords(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 批量更新记录状态为延迟整改
     *
     * @param recordIds 记录ID列表
     * @return 更新数量
     */
    int batchUpdateStatusToOverdue(@Param("recordIds") List<String> recordIds);

    /**
     * 查询用户的巡检记录
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 巡检记录列表
     */
    List<SysInspectionRecord> selectByUserId(@Param("userId") Long userId, @Param("status") String status);

    /**
     * 导出巡检记录数据
     *
     * @param status 状态
     * @param professionId 专业ID
     * @param locationId 部位ID
     * @param checkerId 检查人ID
     * @param responsiblePerson 责任人
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 巡检记录列表
     */
    List<SysInspectionRecord> selectExportData(@Param("status") String status,
                                               @Param("professionId") Long professionId,
                                               @Param("locationId") Long locationId,
                                               @Param("checkerId") Long checkerId,
                                               @Param("responsiblePerson") String responsiblePerson,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 根据日期范围统计状态数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 状态统计数据
     */
    List<Map<String, Object>> selectStatusStatisticsByDateRange(@Param("startDate") String startDate,
                                                                @Param("endDate") String endDate);

    /**
     * 插入前端提交的巡检记录（只插入必要字段）
     *
     * @param record 巡检记录
     * @return 插入结果
     */
    int insertSubmitRecord(SysInspectionRecord record);

    /**
     * 根据日期范围统计专业数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期/**
     * 根据日期范围统计各专业问题数量
     */
    List<Map<String, Object>> selectProfessionStatisticsByDateRange(@Param("startDate") String startDate,
                                                                     @Param("endDate") String endDate);

    /**
     * 按用户ID和状态查询当前周记录数量
     *
     * @param userId 用户ID（可为空）
     * @param status 状态（可为空）
     * @param weekStart 本周开始时间
     * @param weekEnd 本周结束时间
     * @return 记录数量
     */
    Long selectWeeklyRecordCountByUserAndStatus(@Param("userId") String userId,
                                               @Param("status") Integer status,
                                               @Param("weekStart") LocalDateTime weekStart,
                                               @Param("weekEnd") LocalDateTime weekEnd);

    /**
     * 执行自定义SQL查询（绕过MyBatis-Plus逻辑删除拦截器）
     *
     * @param sql 原生SQL语句
     * @return 查询结果
     */
    List<Map<String, Object>> selectBySql(@Param("sql") String sql);

}