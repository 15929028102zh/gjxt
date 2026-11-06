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
                                                @Param("departmentId") String departmentId,
                                                @Param("professionId") String professionId,
                                                @Param("natureId") String natureId,
                                                @Param("locationId") String locationId,
                                                @Param("inspectorId") String inspectorId,
                                                @Param("responsibleUnitId") String responsibleUnitId,
                                                @Param("responsiblePersonId") String responsiblePersonId,
                                                @Param("reviewerId") String reviewerId,
                                                @Param("status") String status,
                                                @Param("problemDescription") String problemDescription,
                                                @Param("responsiblePerson") String responsiblePerson,
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
     * @param departmentId 部门ID（可选）
     * @return 状态统计结果
     */
    List<Map<String, Object>> selectStatusStatistics(@Param("departmentId") String departmentId);

    /**
     * 统计各专业问题数量
     *
     * @param status 状态（可选）
     * @param departmentId 部门ID（可选）
     * @return 专业统计结果
     */
    List<Map<String, Object>> selectProfessionStatistics(@Param("status") String status, @Param("departmentId") String departmentId);

    /**
     * 统计各部位问题数量
     *
     * @param status 状态（可选）
     * @param departmentId 部门ID（可选）
     * @return 部位统计结果
     */
    List<Map<String, Object>> selectLocationStatistics(@Param("status") String status, @Param("departmentId") String departmentId);

    /**
     * 统计各责任人问题数量
     *
     * @param status 状态（可选）
     * @param departmentId 部门ID（可选）
     * @return 责任人统计结果
     */
    List<Map<String, Object>> selectResponsiblePersonStatistics(@Param("status") String status, @Param("departmentId") String departmentId);

    /**
     * 统计总记录数
     *
     * @param departmentId 部门ID（可选）
     * @return 总记录数
     */
    Long selectTotalRecordCount(@Param("departmentId") String departmentId);

    /**
     * 根据状态统计记录数
     *
     * @param status 状态
     * @param departmentId 部门ID（可选）
     * @return 记录数
     */
    Long selectRecordCountByStatus(@Param("status") Integer status, @Param("departmentId") String departmentId);

    /**
     * 统计当月记录数
     *
     * @param departmentId 部门ID（可选）
     * @return 当月记录数
     */
    Long selectCurrentMonthRecordCount(@Param("departmentId") String departmentId);

    /**
     * 统计上月记录数
     *
     * @param departmentId 部门ID（可选）
     * @return 上月记录数
     */
    Long selectLastMonthRecordCount(@Param("departmentId") String departmentId);

    /**
     * 统计月度趋势
     *
     * @param departmentId 部门ID（可选）
     * @return 月度趋势统计
     */
    List<Map<String, Object>> selectMonthlyTrendStatistics(@Param("departmentId") String departmentId);

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
    List<SysInspectionRecord> selectByUserId(@Param("userId") String userId, @Param("status") String status);

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
                                               @Param("checkerName") String checkerName,
                                               @Param("responsiblePerson") String responsiblePerson,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime,
                                               @Param("departmentId") String departmentId,
                                               @Param("natureId") String natureId,
                                               @Param("problemDescription") String problemDescription);

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
     * 查询用户本周巡检记录数量
     *
     * @param userId 用户ID
     * @param status 状态
     * @param departmentId 部门ID（可选）
     * @param weekStart 周开始时间
     * @param weekEnd 周结束时间
     * @return 记录数量
     */
    Long selectWeeklyRecordCount(@Param("userId") String userId,
                                @Param("status") Integer status,
                                @Param("departmentId") String departmentId,
                                @Param("weekStart") LocalDateTime weekStart,
                                @Param("weekEnd") LocalDateTime weekEnd);

    /**
     * 执行自定义SQL查询
     *
     * @param sql SQL语句
     * @return 查询结果
     */
    List<Map<String, Object>> selectBySql(@Param("sql") String sql);

    /**
     * 分页查询延迟整改的记录
     *
     * @param page 分页参数
     * @param currentTime 当前时间
     * @param departmentId 部门ID
     * @return 延迟整改记录分页列表
     */
    IPage<SysInspectionRecord> selectDelayedRectificationRecords(Page<SysInspectionRecord> page,
                                                                @Param("currentTime") LocalDateTime currentTime,
                                                                @Param("departmentId") String departmentId);

    /**
     * 部门×专业矩阵统计
     *
     * @param departmentId 部门ID（可选）
     * @param status 状态（可选）
     * @param startDate 开始日期（yyyy-MM-dd，可选）
     * @param endDate 结束日期（yyyy-MM-dd，可选）
     * @return 每个部门-专业组合的计数
     */
    List<Map<String, Object>> selectDepartmentProfessionMatrix(@Param("departmentId") String departmentId,
                                                               @Param("status") String status,
                                                               @Param("startDate") String startDate,
                                                               @Param("endDate") String endDate);

    /**
     * 部门×问题属性矩阵统计
     *
     * @param departmentId 部门ID（可选）
     * @param status 状态（可选）
     * @param startDate 开始日期（yyyy-MM-dd，可选）
     * @param endDate 结束日期（yyyy-MM-dd，可选）
     * @return 每个部门-问题属性组合的计数
     */
    List<Map<String, Object>> selectDepartmentNatureMatrix(@Param("departmentId") String departmentId,
                                                           @Param("status") String status,
                                                           @Param("startDate") String startDate,
                                                           @Param("endDate") String endDate);

}