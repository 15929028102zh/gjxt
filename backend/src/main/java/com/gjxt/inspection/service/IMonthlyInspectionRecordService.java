package com.gjxt.inspection.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gjxt.inspection.entity.MonthlyInspectionRecord;

import java.util.List;
import java.util.Map;

/**
 * 月检记录Service接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
public interface IMonthlyInspectionRecordService extends IService<MonthlyInspectionRecord> {

    /**
     * 分页查询月检记录
     *
     * @param current 当前页
     * @param size 每页大小
     * @param params 查询参数
     * @return 分页数据
     */
    IPage<MonthlyInspectionRecord> getRecordPage(long current, long size, Map<String, Object> params);

    /**
     * 创建月检记录
     *
     * @param record 记录信息
     * @return 是否成功
     */
    boolean createRecord(MonthlyInspectionRecord record);

    /**
     * 更新月检记录
     *
     * @param record 记录信息
     * @return 是否成功
     */
    boolean updateRecord(MonthlyInspectionRecord record);

    /**
     * 根据ID获取记录详情
     *
     * @param id 记录ID
     * @return 记录详情
     */
    MonthlyInspectionRecord getRecordDetail(String id);

    /**
     * 删除月检记录
     *
     * @param ids 记录ID列表
     * @return 是否成功
     */
    boolean deleteRecords(List<String> ids);

    /**
     * 提交月检记录
     *
     * @param ids 记录ID列表
     * @return 是否成功
     */
    boolean submitRecords(List<String> ids);

    /**
     * 审核月检记录
     *
     * @param ids 记录ID列表
     * @param status 审核状态
     * @return 是否成功
     */
    boolean auditRecords(List<String> ids, Integer status);

    /**
     * 获取月检统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getMonthlyStatistics(Map<String, Object> params);

    /**
     * 获取月度趋势数据
     *
     * @param params 查询参数
     * @return 趋势数据
     */
    List<Map<String, Object>> getMonthlyTrend(Map<String, Object> params);

    /**
     * 根据任务ID获取记录列表
     *
     * @param taskId 任务ID
     * @param recordStatus 记录状态（可选）
     * @return 记录列表
     */
    List<MonthlyInspectionRecord> getRecordsByTaskId(String taskId, Integer recordStatus);

    /**
     * 根据用户ID获取记录列表
     *
     * @param userId 用户ID
     * @param recordStatus 记录状态（可选）
     * @return 记录列表
     */
    List<MonthlyInspectionRecord> getRecordsByUserId(String userId, Integer recordStatus);

    /**
     * 批量删除记录
     *
     * @param ids 记录ID列表
     * @return 是否成功
     */
    boolean batchDeleteRecords(List<String> ids);

    /**
     * 删除单个记录
     *
     * @param id 记录ID
     * @return 是否成功
     */
    boolean deleteRecord(String id);

    /**
     * 生成记录编号
     *
     * @return 记录编号
     */
    String generateRecordNo();

    /**
     * 更新记录状态
     *
     * @param id 记录ID
     * @param recordStatus 记录状态
     * @return 是否成功
     */
    boolean updateRecordStatus(String id, Integer recordStatus);

    /**
     * 批量更新记录状态
     *
     * @param ids 记录ID列表
     * @param recordStatus 记录状态
     * @return 是否成功
     */
    boolean batchUpdateRecordStatus(List<String> ids, Integer recordStatus);

    /**
     * 提交单个记录
     *
     * @param id 记录ID
     * @return 是否成功
     */
    boolean submitRecord(String id);

    /**
     * 审核单个记录
     *
     * @param id 记录ID
     * @return 是否成功
     */
    boolean auditRecord(String id);

    /**
     * 确认整改
     *
     * @param id 记录ID
     * @param rectificationSuggestion 整改建议
     * @param rectificationEffect 整改效果描述
     * @param rectificationImages 整改图片
     * @param natureDefinition 性质界定
     * @param professionId 专业ID
     * @param departmentId 部门ID
     * @param status 整改状态
     * @return 是否成功
     */
    boolean confirmRectification(String id, String rectificationSuggestion, String rectificationEffect, 
                               String rectificationImages, String natureDefinition, String professionId, 
                               String departmentId, Integer status);

    /**
     * 确认复查
     *
     * @param id 记录ID
     * @param reviewSuggestion 复查建议
     * @param reviewEffect 复查效果描述
     * @param reviewImages 复查图片
     * @return 是否成功
     */
    boolean confirmReview(String id, String reviewSuggestion, String reviewEffect, String reviewImages);

    /**
     * 获取状态统计数据
     *
     * @return 状态统计数据
     */
    List<Map<String, Object>> getStatusStatistics();

    /**
     * 获取部门统计数据
     *
     * @param recordStatus 记录状态
     * @return 部门统计数据
     */
    List<Map<String, Object>> getDepartmentStatistics(Integer recordStatus);
}