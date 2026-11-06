package com.gjxt.inspection.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gjxt.inspection.entity.MonthlyInspectionRecord;

import java.util.List;
import java.util.Map;

/**
 * 月检记录服务接口（与线上运行的 production 版本签名完全对齐）
 *
 * @author gjxt
 * @since 2024-01-01
 */
public interface IMonthlyInspectionRecordService extends IService<MonthlyInspectionRecord> {

    IPage<MonthlyInspectionRecord> getRecordPage(long current, long size, Map<String, Object> params);

    boolean createRecord(MonthlyInspectionRecord record);

    boolean updateRecord(MonthlyInspectionRecord record);

    MonthlyInspectionRecord getRecordDetail(String id);

    boolean deleteRecords(List<String> ids);

    boolean submitRecords(List<String> ids);

    boolean auditRecords(List<String> ids, Integer status);

    Map<String, Object> getMonthlyStatistics(Map<String, Object> params);

    List<Map<String, Object>> getMonthlyTrend(Map<String, Object> params);

    List<MonthlyInspectionRecord> getRecordsByTaskId(String taskId, Integer recordStatus);

    List<MonthlyInspectionRecord> getRecordsByUserId(String createUserId, Integer recordStatus);

    boolean batchDeleteRecords(List<String> ids);

    boolean deleteRecord(String id);

    /**
     * 月检记录录入页直接删除：公示期前(phase=1/2)把已提交的记录(status 0/1/2)逻辑删除。
     * <p>与 {@link #deleteRecord(String)} / {@link #batchDeleteRecords(List)} 完全独立——
     * 那两个是月检提交页的路径，只放行 status=4，规则不变。</p>
     * <p>公示期(phase=3)不允许直删，须走删除审核(auditType=2)；公示期后一律禁止。</p>
     */
    boolean deleteRecordFromEntryPage(String id);

    String generateRecordNo();

    boolean updateRecordStatus(String id, Integer recordStatus);

    boolean batchUpdateRecordStatus(List<String> ids, Integer recordStatus);

    boolean submitRecord(String id);

    boolean auditRecord(String id);

    /**
     * 确认整改。性质界定/专业在整改环节为只读展示项，不参与更新。
     * V3 起允许整改时调整责任人/责任单位。
     */
    boolean confirmRectification(String id, String rectificationSuggestion, String rectificationEffect,
                                  String rectificationImages, String responsibleUnit, String responsiblePerson,
                                  String rectifierName);

    boolean confirmReview(String id, String reviewSuggestion, String reviewEffect, String reviewImages,
                          String responsibleUnit, String responsiblePerson, String reviewerName);

    List<Map<String, Object>> getStatusStatistics();

    List<Map<String, Object>> getDepartmentStatistics(Integer recordStatus);

    Long getMonthlyRecordCount(String userId, Integer recordStatus, String departmentId,
                               String startDate, String endDate);

    boolean rejectRecord(String id);

    /**
     * 加载审核记录（含回退审核与编辑审核两种类型）
     *
     * @param auditType 0=回退审核，1=编辑审核，null=全部
     */
    IPage<MonthlyInspectionRecord> getAuditRecords(long current, long size, String departmentId,
                                                    Integer recordStatus, Integer auditType,
                                                    String modifyUserName,
                                                    String startDate, String endDate);

    boolean confirmAudit(String id, String auditorId, String auditReason);

    boolean rejectAudit(String id);

    /**
     * 提交编辑审核（公示期内编辑/删除记录时使用，默认 auditType=1 编辑审核）
     */
    boolean submitEditForAudit(String id, String modifyDescription, String modifyUserId, String modifyUserName);

    /**
     * 提交编辑/回退审核
     * @param auditType 0=回退申请，1=编辑申请。回退申请要求任务处于公示期(phase=3)
     */
    boolean submitEditForAudit(String id, String modifyDescription, String modifyUserId, String modifyUserName, Integer auditType);

    /**
     * 提交编辑/回退审核(带待审核字段值)
     * <p>编辑申请(auditType=1)时,pendingEdit 携带申请人修改后的字段值。
     * 后端基于此构造 modification_snapshot JSON 存到原 record 上(原 record 字段值不变),
     * 审核通过时由 confirmEditAudit 应用快照生效。回退申请(auditType=0)时该参数忽略。
     *
     * @param pendingEdit 申请人提交的新字段值；可为 null（兼容旧调用）
     */
    boolean submitEditForAudit(String id, String modifyDescription, String modifyUserId, String modifyUserName,
                                Integer auditType, MonthlyInspectionRecord pendingEdit);

    /**
     * 编辑审核通过（审核人操作）
     */
    boolean confirmEditAudit(String id, String auditorId, String auditReason);

    /**
     * 编辑审核拒绝
     */
    boolean rejectEditAudit(String id, String auditReason);

    /**
     * 撤回记录：公示期前(phase=1/2)直接将记录从 0/1/2 改回 status=4（待提交），无需审核
     */
    boolean withdrawRecord(String id);

    /**
     * 判断当前是否可编辑/可删除指定记录
     * 规则：
     *  - 月检录入期结束后、公示期开始前：可自由编辑/删除
     *  - 公示期内：需要先提交审核，审核通过后方可编辑/删除
     *  - 公示期结束后：不允许编辑/删除
     *
     * @return 可编辑/删除则返回 null；否则返回不能操作的提示
     */
    String checkEditable(MonthlyInspectionRecord record);

    /**
     * 判断当前是否处于月检任务的录入期内
     */
    boolean isInInspectionPeriod(String taskId);
}