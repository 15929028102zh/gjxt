package com.gjxt.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.entity.SysInspectionRecord;
import com.gjxt.inspection.entity.SysInspectionRecordSubmitData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 巡检记录服务接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
public interface ISysInspectionRecordService extends IService<SysInspectionRecord> {

    /**
     * 分页查询周检记录
     *
     * @param current 当前页
     * @param size 每页大小
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
     * @return 分页结果
     */
    PageResult<SysInspectionRecord> getRecordPage(Long current, Long size, String taskId,
                                                 String departmentId,
                                                 String professionId, String natureId, String locationId, String inspectorId,
                                                 String responsibleUnitId, String responsiblePersonId, String reviewerId,
                                                 String status, String problemDescription, String responsiblePerson,
                                                 LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据ID查询巡检记录详情
     *
     * @param id 记录ID
     * @return 巡检记录详情
     */
    SysInspectionRecord getRecordDetail(Long id);

    /**
     * 根据ID查询巡检记录
     *
     * @param id 记录ID
     * @return 巡检记录
     */
    SysInspectionRecord getRecordById(String id);

    /**
     * 新增巡检记录
     *
     * @param record 巡检记录
     * @return 是否成功
     */
    boolean addRecord(SysInspectionRecord record);

    /**
     * 更新巡检记录
     *
     * @param record 巡检记录
     * @return 是否成功
     */
    boolean updateRecord(SysInspectionRecord record);

    /**
     * 删除巡检记录
     *
     * @param id 记录ID
     * @return 是否成功
     */
    boolean deleteRecord(String id);

    /**
     * 整改确认
     *
     * @param id 记录ID
     * @param rectificationDescription 整改说明
     * @param rectificationPhoto 整改照片
     * @return 是否成功
     */
    boolean rectificationConfirm(Long id, String rectificationDescription, String rectificationPhoto);

    /**
     * 整改确认（简化版）
     *
     * @param id 记录ID
     * @param rectificationEffect 整改效果
     * @param rectificationImageUrls 整改照片URL
     * @param rectifierName 整改人姓名（当前登录用户）
     * @return 是否成功
     */
    boolean rectifyConfirm(String id, String rectificationEffect, String rectificationImageUrls, String rectifierName);

    /**
     * 复查确认
     *
     * @param id 记录ID
     * @param reviewerId 复查人ID
     * @param reviewEffect 复查效果
     * @param reviewImageUrls 复查照片URL
     * @param reviewerName 复查人姓名（当前登录用户）
     * @return 操作结果
     */
    boolean reviewConfirm(String id, String reviewerId, String reviewEffect, String reviewImageUrls, String reviewerName);

    /**
     * 根据用户ID查询巡检记录
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 巡检记录列表
     */
    List<SysInspectionRecord> getRecordsByUserId(String userId, String status);

    /**
     * 分页查询用户记录
     *
     * @param userId 用户ID
     * @param current 当前页
     * @param size 页大小
     * @param status 状态
     * @param departmentId 部门ID
     * @return 分页结果
     */
    PageResult<SysInspectionRecord> getUserRecords(String userId, Long current, Long size, String status, String departmentId);

    /**
     * 获取统计概览
     *
     * @param departmentId 部门ID
     * @return 统计概览数据
     */
    Map<String, Object> getStatisticsOverview(String departmentId);

    /**
     * 获取月检统计数据
     *
     * @param departmentId 部门ID
     * @return 月检统计数据
     */
    Map<String, Object> getMonthlyStatistics(String departmentId);

    /**
     * 统计各状态问题数量
     *
     * @param departmentId 部门ID
     * @return 状态统计列表
     */
    List<Map<String, Object>> getStatusStatistics(String departmentId);

    /**
     * 统计各专业问题数量
     *
     * @param status 状态
     * @param departmentId 部门ID
     * @return 专业统计列表
     */
    List<Map<String, Object>> getProfessionStatistics(String status, String departmentId);

    /**
     * 统计各部位问题数量
     *
     * @param status 状态
     * @param departmentId 部门ID
     * @return 部位统计列表
     */
    List<Map<String, Object>> getLocationStatistics(String status, String departmentId);

    /**
     * 统计各责任人问题数量
     *
     * @param status 状态
     * @param departmentId 部门ID
     * @return 责任人统计列表
     */
    List<Map<String, Object>> getResponsiblePersonStatistics(String status, String departmentId);

    /**
     * 处理延迟整改问题
     * 定时任务调用，将超过整改期限的问题标记为延迟整改
     */
    void handleOverdueRecords();

    /**
     * 处理延迟整改问题
     * 定时任务调用，将超过整改期限的问题标记为延迟整改
     */
    boolean handleDelayedRectification();

    /**
     * 导出巡检记录
     *
     * @param status 状态
     * @param professionId 专业ID
     * @param locationId 部位ID
     * @param checkerId 检查人ID
     * @param responsiblePerson 责任人
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 导出数据
     */
    List<SysInspectionRecord> exportRecords(String status, Long professionId, Long locationId, 
                                           Long checkerId, String responsiblePerson, 
                                           LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 导出巡检记录到Excel
     *
     * @param response HTTP响应
     * @param problemDescription 问题描述
     * @param responsiblePerson 责任人
     * @param professionId 专业ID
     * @param natureId 性质界定ID
     * @param departmentId 部门ID
     * @param status 状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    void exportRecordsToExcel(javax.servlet.http.HttpServletResponse response, 
                             String problemDescription, String responsiblePerson, String inspectorId,
                             String professionId, String natureId, String departmentId, 
                             Integer status, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 生成记录编号
     *
     * @return 记录编号
     */
    String generateRecordNo();

    /**
     * 根据状态查询巡检记录
     *
     * @param status 状态
     * @param current 当前页
     * @param size 每页大小
     * @return 查询结果
     */
    Map<String, Object> getRecordsByStatus(String status, Long current, Long size);

    /**
     * 根据专业查询巡检记录
     *
     * @param profession 专业名称
     * @param current 当前页
     * @param size 每页大小
     * @return 查询结果
     */
    Map<String, Object> getRecordsByProfession(String profession, Long current, Long size);

    /**
     * 根据部位查询巡检记录
     *
     * @param location 部位名称
     * @param current 当前页
     * @param size 每页大小
     * @return 查询结果
     */
    Map<String, Object> getRecordsByLocation(String location, Long current, Long size);

    /**
     * 根据责任人查询巡检记录
     *
     * @param responsiblePerson 责任人
     * @param current 当前页
     * @param size 每页大小
     * @return 查询结果
     */
    Map<String, Object> getRecordsByResponsible(String responsiblePerson, Long current, Long size);

    /**
     * 根据责任人查询巡检记录
     *
     * @param responsiblePerson 责任人
     * @return 巡检记录列表
     */
    List<SysInspectionRecord> getRecordsByResponsiblePerson(String responsiblePerson);

    /**
     * 根据时间范围、专业、部位、责任人统计数据
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param profession 专业
     * @param location 部位
     * @param responsiblePerson 责任人
     * @return 统计数据
     */
    Map<String, Object> getTimeRangeStatistics(String startTime, String endTime, String profession, String location, String responsiblePerson);

    /**
     * 提交前端巡检记录数据
     *
     * @param submitData 前端提交的数据
     * @return 是否成功
     */
    boolean submitRecord(SysInspectionRecordSubmitData submitData);

    /**
     * 获取周检记录数量
     *
     * @param userId 用户ID
     * @param status 状态
     * @param departmentId 部门ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 记录数量
     */
    Long getWeeklyRecordCount(String userId, Integer status, String departmentId, String startDate, String endDate);

    /**
     * 分页查询延迟整改记录
     *
     * @param current 当前页
     * @param size 页大小
     * @param departmentId 部门ID
     * @return 分页结果
     */
    PageResult<SysInspectionRecord> getDelayedRectificationRecords(Long current, Long size, String departmentId);

    /**
     * 批量更新记录状态
     *
     * @param ids 记录ID列表
     * @param status 新状态
     * @return 是否成功
     */
    boolean batchUpdateStatus(List<String> ids, Integer status);

    /**
     * 单个记录状态更新
     *
     * @param id 记录ID
     * @param status 新状态
     * @return 是否成功
     */
    boolean updateSingleStatus(String id, Integer status);

    /**
     * 批量删除巡检记录
     *
     * @param ids 记录ID列表
     * @return 是否成功
     */
    boolean batchDeleteRecords(List<String> ids);

    /**
     * 部门×专业矩阵统计
     *
     * @param departmentId 部门ID（可选）
     * @param status 状态（可选）
     * @param startDate 开始日期（yyyy-MM-dd，可选）
     * @param endDate 结束日期（yyyy-MM-dd，可选）
     * @return 每个部门-专业组合的计数
     */
    List<Map<String, Object>> getDepartmentProfessionMatrix(String departmentId, String status, String startDate, String endDate);

    /**
     * 部门×问题属性矩阵统计
     *
     * @param departmentId 部门ID（可选）
     * @param status 状态（可选）
     * @param startDate 开始日期（yyyy-MM-dd，可选）
     * @param endDate 结束日期（yyyy-MM-dd，可选）
     * @return 每个部门-问题属性组合的计数
     */
    List<Map<String, Object>> getDepartmentNatureMatrix(String departmentId, String status, String startDate, String endDate);

    /**
     * 回退记录（发送审核通知到部门管理员）
     *
     * @param id 记录ID
     * @return 是否成功
     */
    boolean rejectRecord(String id);

    /**
     * 查询待审核记录
     *
     * @param current 当前页
     * @param size 每页大小
     * @param departmentId 部门ID（可选）
     * @param status 状态（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 分页结果
     */
    PageResult<SysInspectionRecord> getAuditRecords(Long current, Long size, String departmentId, String status, 
                                                   LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 审核确认（审核成功，记录回退到待整改状态）
     *
     * @param id 记录ID
     * @param auditorId 审核人 ID
     * @param auditReason 审核意见
     * @return 是否成功
     */
    boolean confirmAudit(String id, String auditorId, String auditReason);
    
    /**
     * 拒绝审核（将记录状态从待审核(3)改回已提交(1)）
     *
     * @param id 记录ID
     * @return 是否成功
     */
    boolean rejectAudit(String id);

}