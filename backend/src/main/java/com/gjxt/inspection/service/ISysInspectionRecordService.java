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
                                                 String groupLeaderId, String deputyLeaderId, String departmentId,
                                                 String professionId, String natureId, String locationId, String inspectorId,
                                                 String responsibleUnitId, String responsiblePersonId, String reviewerId,
                                                 Integer status,
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
     * @return 是否成功
     */
    boolean rectifyConfirm(String id, String rectificationEffect, String rectificationImageUrls);

    /**
     * 复查确认
     *
     * @param id 记录ID
     * @param reviewerId 复查人ID
     * @param reviewEffect 复查效果
     * @param reviewImageUrls 复查照片URL
     * @return 操作结果
     */
    boolean reviewConfirm(String id, String reviewerId, String reviewEffect, String reviewImageUrls);

    /**
     * 根据用户ID查询巡检记录
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 巡检记录列表
     */
    List<SysInspectionRecord> getRecordsByUserId(Long userId, String status);

    /**
     * 获取用户相关的巡检记录
     *
     * @param userId 用户ID
     * @param current 当前页
     * @param size 每页大小
     * @param status 状态
     * @return 分页结果
     */
    PageResult<SysInspectionRecord> getUserRecords(Long userId, Long current, Long size, String status);

    /**
     * 统计各状态问题数量
     *
     * @return 状态统计结果
     */
    List<Map<String, Object>> getStatusStatistics();

    /**
     * 统计各专业问题数量
     *
     * @param status 状态（可选）
     * @return 专业统计结果
     */
    List<Map<String, Object>> getProfessionStatistics(String status);

    /**
     * 统计各部位问题数量
     *
     * @param status 状态（可选）
     * @return 部位统计结果
     */
    List<Map<String, Object>> getLocationStatistics(String status);

    /**
     * 统计各责任人问题数量
     *
     * @param status 状态（可选）
     * @return 责任人统计结果
     */
    List<Map<String, Object>> getResponsiblePersonStatistics(String status);

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
                             String problemDescription, String responsiblePerson, 
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
     * 按用户ID和状态查询当前周记录数量
     *
     * @param userId 用户ID（可为空）
     * @param status 状态（可为空，0-待整改，1-已整改，2-已复查）
     * @return 记录数量
     */
    Long getWeeklyRecordCountByUserAndStatus(String userId, Integer status);

    /**
     * 查询延迟整改的记录（整改期限小于当前日期且状态为待整改）
     *
     * @param current 当前页
     * @param size 每页大小
     * @return 延迟整改记录分页结果
     */
    PageResult<SysInspectionRecord> getDelayedRectificationRecords(Long current, Long size);

}