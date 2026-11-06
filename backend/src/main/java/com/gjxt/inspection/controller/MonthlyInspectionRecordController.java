package com.gjxt.inspection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.entity.MonthlyInspectionRecord;
import com.gjxt.inspection.entity.SysDept;
import com.gjxt.inspection.entity.SysUser;
import com.gjxt.inspection.service.IMonthlyInspectionRecordService;
import com.gjxt.inspection.service.ISysDeptService;
import com.gjxt.inspection.util.UserContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 月检记录控制器
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Tag(name = "月检记录管理", description = "月检记录管理相关接口")
@RestController
@RequestMapping("/monthly-inspection-record")
public class MonthlyInspectionRecordController {

    @Autowired
    private IMonthlyInspectionRecordService monthlyInspectionRecordService;

    @Autowired
    private UserContextUtil userContextUtil;

    @Autowired
    private ISysDeptService deptService;

    /**
     * 分页查询月检记录列表
     */
    @Operation(summary = "分页查询月检记录列表")
    @GetMapping("/page")
    public Result<IPage<MonthlyInspectionRecord>> getRecordPage(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") long size,
            @Parameter(description = "任务ID") @RequestParam(required = false) String taskId,
            @Parameter(description = "任务编号") @RequestParam(required = false) String taskNo,
            @Parameter(description = "专业类别ID") @RequestParam(required = false) String professionalTypeId,
            @Parameter(description = "责任部门ID") @RequestParam(required = false) String responsibilityId,
            @Parameter(description = "记录状态(可传多个)") @RequestParam(required = false) List<Integer> recordStatus,
            @Parameter(description = "创建用户ID") @RequestParam(required = false) String createUserId,
            @Parameter(description = "受检单位（部门ID）") @RequestParam(required = false) String departmentId,
            @Parameter(description = "检查单位（管理部门ID）") @RequestParam(required = false) String checkUnitId,
            @Parameter(description = "责任人（模糊匹配）") @RequestParam(required = false) String responsiblePerson,
            @Parameter(description = "检查人（模糊匹配）") @RequestParam(required = false) String inspector,
            @Parameter(description = "记录时间起 yyyy-MM-dd") @RequestParam(required = false) String startDate,
            @Parameter(description = "记录时间止 yyyy-MM-dd") @RequestParam(required = false) String endDate,
            @Parameter(description = "整改期限（筛选小于该日期）yyyy-MM-dd") @RequestParam(required = false) String rectificationDeadlineBefore
    ) {
        try {
            SysUser currentUser = userContextUtil.getCurrentUser();
            if (currentUser == null) {
                return Result.error("用户未认证");
            }

            Map<String, Object> params = new HashMap<>();
            params.put("taskId", taskId);
            params.put("taskNo", taskNo);
            params.put("professionalTypeId", professionalTypeId);
            params.put("responsibilityId", responsibilityId);
            params.put("recordStatusList", recordStatus);
            params.put("createUserId", createUserId);
            params.put("checkUnitId", checkUnitId);
            params.put("responsiblePerson", responsiblePerson);
            params.put("inspector", inspector);
            params.put("startDate", startDate);
            params.put("endDate", endDate);
            params.put("rectificationDeadlineBefore", rectificationDeadlineBefore);

            // 数据范围：月检管理员可按部门自由筛选，其余用户一律限定在本部门，忽略前端传入的部门
            // 周检管理员(IS_WEEKLY_ADMIN=1)也不视为月检管理员,数据范围同样被限制
            if (Integer.valueOf(1).equals(currentUser.getIsMonthlyAdmin())) {
                params.put("departmentId", departmentId);
            } else {
                params.put("scopeUserId", currentUser.getId());
                params.put("scopeDeptId", currentUser.getDeptId());
                // 运行部门(受检单位,dept_type=0):月检记录在公示期开始前对其不可见,
                // 公示开始后才可见(便于其后续确认整改);管理部门(检查单位)与月检管理员不受此限制
                if (isRunningDept(currentUser.getDeptId())) {
                    params.put("hideBeforePublicity", true);
                }
            }

            IPage<MonthlyInspectionRecord> result = monthlyInspectionRecordService.getRecordPage(current, size, params);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("查询月检记录列表失败：" + e.getMessage());
        }
    }

    /**
     * 判断指定部门是否为运行部门(受检单位,dept_type=0)。
     * 部门不存在、类型未知或查询异常时返回 false,即不施加公示期可见性限制,避免误伤。
     */
    private boolean isRunningDept(String deptId) {
        if (deptId == null || deptId.isEmpty()) {
            return false;
        }
        try {
            SysDept dept = deptService.getById(deptId);
            return dept != null && Integer.valueOf(0).equals(dept.getDeptType());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据ID查询月检记录详情
     */
    @Operation(summary = "根据ID查询月检记录详情")
    @GetMapping("/{id}")
    public Result<MonthlyInspectionRecord> getRecordDetail(
            @Parameter(description = "记录ID") @PathVariable String id
    ) {
        try {
            MonthlyInspectionRecord record = monthlyInspectionRecordService.getRecordDetail(id);
            if (record != null) {
                return Result.success(record);
            } else {
                return Result.error("月检记录不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询月检记录详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建月检记录
     */
    @Operation(summary = "创建月检记录")
    @PostMapping
    public Result<String> createRecord(
            @Parameter(description = "记录信息") @RequestBody MonthlyInspectionRecord record
    ) {
        try {
            // 从认证信息获取当前用户ID，防止用户伪装攻击
            String createUserId = userContextUtil.getCurrentUserId();
            if (createUserId == null || createUserId.isEmpty()) {
                return Result.error("用户未认证");
            }

            // 设置创建用户ID
            record.setCreateUserId(createUserId);

            boolean result = monthlyInspectionRecordService.createRecord(record);
            if (result) {
                return Result.success("创建月检记录成功");
            } else {
                return Result.error("创建月检记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("创建月检记录失败：" + e.getMessage());
        }
    }

    /**
     * 更新月检记录
     */
    @Operation(summary = "更新月检记录")
    @PutMapping
    public Result<String> updateRecord(
            @Parameter(description = "记录信息") @RequestBody MonthlyInspectionRecord record
    ) {
        try {
            boolean result = monthlyInspectionRecordService.updateRecord(record);
            if (result) {
                return Result.success("更新月检记录成功");
            } else {
                return Result.error("更新月检记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新月检记录失败：" + e.getMessage());
        }
    }

    /**
     * 删除月检记录
     */
    @Operation(summary = "删除月检记录")
    @DeleteMapping("/{id}")
    public Result<String> deleteRecord(
            @Parameter(description = "记录ID") @PathVariable String id
    ) {
        try {
            boolean result = monthlyInspectionRecordService.deleteRecord(id);
            if (result) {
                return Result.success("删除月检记录成功");
            } else {
                return Result.error("删除月检记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除月检记录失败：" + e.getMessage());
        }
    }

    /**
     * 月检记录录入页直接删除：公示期前(phase=1/2)删除已提交的记录。
     * 与 DELETE /{id}(月检提交页，仅 status=4) 是两条独立路径。
     */
    @Operation(summary = "删除月检记录（录入页，公示期前直接删除）")
    @DeleteMapping("/{id}/entry-delete")
    public Result<String> deleteRecordFromEntryPage(
            @Parameter(description = "记录ID") @PathVariable String id
    ) {
        try {
            boolean result = monthlyInspectionRecordService.deleteRecordFromEntryPage(id);
            return result ? Result.success("删除月检记录成功") : Result.error("删除月检记录失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除月检记录失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除月检记录
     */
    @Operation(summary = "批量删除月检记录")
    @DeleteMapping("/batch")
    public Result<String> batchDeleteRecords(
            @Parameter(description = "记录ID列表") @RequestBody List<String> ids
    ) {
        try {
            boolean result = monthlyInspectionRecordService.batchDeleteRecords(ids);
            if (result) {
                return Result.success("批量删除月检记录成功");
            } else {
                return Result.error("批量删除月检记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("批量删除月检记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据任务ID查询记录列表
     */
    @Operation(summary = "根据任务ID查询记录列表")
    @GetMapping("/task/{taskId}")
    public Result<List<MonthlyInspectionRecord>> getRecordsByTaskId(
            @Parameter(description = "任务ID") @PathVariable String taskId,
            @Parameter(description = "记录状态") @RequestParam(required = false) Integer recordStatus
    ) {
        try {
            List<MonthlyInspectionRecord> records = monthlyInspectionRecordService.getRecordsByTaskId(taskId, recordStatus);
            return Result.success(records);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询任务记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID查询记录列表
     */
    @Operation(summary = "根据用户ID查询记录列表")
    @GetMapping("/user/{userId}")
    public Result<List<MonthlyInspectionRecord>> getRecordsByUserId(
            @Parameter(description = "用户ID") @PathVariable String userId,
            @Parameter(description = "记录状态") @RequestParam(required = false) Integer recordStatus
    ) {
        try {
            List<MonthlyInspectionRecord> records = monthlyInspectionRecordService.getRecordsByUserId(userId, recordStatus);
            return Result.success(records);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询用户记录失败：" + e.getMessage());
        }
    }

    /**
     * 更新记录状态
     */
    @Operation(summary = "更新记录状态")
    @PutMapping("/status")
    public Result<String> updateRecordStatus(
            @Parameter(description = "更新状态请求") @RequestBody Map<String, Object> params
    ) {
        try {
            String id = (String) params.get("id");
            Integer recordStatus = (Integer) params.get("recordStatus");
            
            boolean result = monthlyInspectionRecordService.updateRecordStatus(id, recordStatus);
            if (result) {
                return Result.success("更新记录状态成功");
            } else {
                return Result.error("更新记录状态失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新记录状态失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新记录状态
     */
    @Operation(summary = "批量更新记录状态")
    @PutMapping("/batch-status")
    public Result<String> batchUpdateRecordStatus(
            @Parameter(description = "批量更新状态请求") @RequestBody Map<String, Object> params
    ) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            Integer recordStatus = (Integer) params.get("recordStatus");
            
            boolean result = monthlyInspectionRecordService.batchUpdateRecordStatus(ids, recordStatus);
            if (result) {
                return Result.success("批量更新记录状态成功");
            } else {
                return Result.error("批量更新记录状态失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("批量更新记录状态失败：" + e.getMessage());
        }
    }

    /**
     * 提交记录
     */
    @Operation(summary = "提交记录")
    @PutMapping("/{id}/submit")
    public Result<String> submitRecord(
            @Parameter(description = "记录ID") @PathVariable String id
    ) {
        try {
            boolean result = monthlyInspectionRecordService.submitRecord(id);
            if (result) {
                return Result.success("提交记录成功");
            } else {
                return Result.error("提交记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("提交记录失败：" + e.getMessage());
        }
    }

    /**
     * 确认整改
     */
    @Operation(summary = "确认整改")
    @PutMapping("/{id}/rectify")
    public Result<String> confirmRectification(
            @Parameter(description = "记录ID") @PathVariable String id,
            @Parameter(description = "整改信息") @RequestBody Map<String, Object> params
    ) {
        try {
            String rectificationSuggestion = (String) params.get("rectificationSuggestion");
            String rectificationEffect = (String) params.get("rectificationEffect");
            // 支持字符串或数组形式的图片参数
            String rectificationImages;
            Object imagesObj = params.get("rectificationImages");
            if (imagesObj instanceof String) {
                rectificationImages = (String) imagesObj;
            } else if (imagesObj instanceof java.util.List) {
                @SuppressWarnings("unchecked")
                java.util.List<Object> list = (java.util.List<Object>) imagesObj;
                rectificationImages = list.stream()
                        .filter(java.util.Objects::nonNull)
                        .map(Object::toString)
                        .filter(s -> !s.isEmpty())
                        .collect(java.util.stream.Collectors.joining(","));
            } else {
                rectificationImages = null;
            }

            // 记录状态由 service 层根据状态机强制流转，不接受外部传入
            String responsibleUnit = (String) params.get("responsibleUnit");
            String responsiblePerson = (String) params.get("responsiblePerson");
            String rectifierName = (String) params.get("rectifierName");
            boolean result = monthlyInspectionRecordService.confirmRectification(
                id, rectificationSuggestion, rectificationEffect, rectificationImages,
                responsibleUnit, responsiblePerson, rectifierName
            );

            if (result) {
                return Result.success("整改确认成功");
            } else {
                return Result.error("整改确认失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("整改确认失败：" + e.getMessage());
        }
    }

    /**
     * 确认复查
     */
    @Operation(summary = "确认复查")
    @PutMapping("/{id}/review")
    public Result<String> confirmReview(
            @Parameter(description = "记录ID") @PathVariable String id,
            @Parameter(description = "复查信息") @RequestBody Map<String, Object> params
    ) {
        try {
            String reviewSuggestion = (String) params.get("reviewSuggestion");
            String reviewEffect = (String) params.get("reviewEffect");
            String reviewImages = (String) params.get("reviewImages");
            String responsibleUnit = (String) params.get("responsibleUnit");
            String responsiblePerson = (String) params.get("responsiblePerson");
            String reviewerName = (String) params.get("reviewerName");

            boolean result = monthlyInspectionRecordService.confirmReview(
                id, reviewSuggestion, reviewEffect, reviewImages, responsibleUnit, responsiblePerson, reviewerName
            );
            
            if (result) {
                return Result.success("复查确认成功");
            } else {
                return Result.error("复查确认失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("复查确认失败：" + e.getMessage());
        }
    }

    /**
     * 获取状态统计
     */
    @Operation(summary = "获取状态统计")
    @GetMapping("/statistics/status")
    public Result<List<Map<String, Object>>> getStatusStatistics() {
        try {
            List<Map<String, Object>> statistics = monthlyInspectionRecordService.getStatusStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取状态统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取部门统计
     */
    @Operation(summary = "获取部门统计")
    @GetMapping("/statistics/department")
    public Result<List<Map<String, Object>>> getDepartmentStatistics(
            @Parameter(description = "记录状态") @RequestParam(required = false) Integer recordStatus
    ) {
        try {
            List<Map<String, Object>> statistics = monthlyInspectionRecordService.getDepartmentStatistics(recordStatus);
            return Result.success(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取部门统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取月检记录数量统计
     */
    @Operation(summary = "获取月检记录数量统计")
    @GetMapping("/monthly-count")
    public Result<Long> getMonthlyRecordCount(
            @Parameter(description = "用户ID") @RequestParam(required = false) String userId,
            @Parameter(description = "记录状态") @RequestParam(required = false) Integer recordStatus,
            @Parameter(description = "部门ID") @RequestParam(required = false) String departmentId,
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate
    ) {
        try {
            Long count = monthlyInspectionRecordService.getMonthlyRecordCount(userId, recordStatus, departmentId, startDate, endDate);
            return Result.success(count);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取月检记录数量失败：" + e.getMessage());
        }
    }

    /**
     * 回退记录（发送审核通知）
     */
    @Operation(summary = "回退记录")
    @PostMapping("/{id}/reject")
    public Result<String> rejectRecord(@Parameter(description = "记录ID") @PathVariable String id) {
        try {
            boolean success = monthlyInspectionRecordService.rejectRecord(id);
            return success ? Result.success("回退记录成功") : Result.error("回退记录失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("回退记录失败：" + e.getMessage());
        }
    }

    /**
     * 撤回记录（公示期前直接回到待提交，无需审核）
     */
    @Operation(summary = "撤回记录")
    @PostMapping("/{id}/withdraw")
    public Result<String> withdrawRecord(@Parameter(description = "记录ID") @PathVariable String id) {
        try {
            boolean success = monthlyInspectionRecordService.withdrawRecord(id);
            return success ? Result.success("撤回成功") : Result.error("撤回失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("撤回失败：" + e.getMessage());
        }
    }

    /**
     * 查询待审核记录
     */
    @Operation(summary = "查询待审核记录")
    @GetMapping("/audit-list")
    public Result<IPage<MonthlyInspectionRecord>> getAuditRecords(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") long size,
            @Parameter(description = "部门ID") @RequestParam(required = false) String departmentId,
            @Parameter(description = "记录状态") @RequestParam(required = false) Integer recordStatus,
            @Parameter(description = "审核类型 0=回退审核 1=编辑审核 2=删除审核 null=全部") @RequestParam(required = false) Integer auditType,
            @Parameter(description = "申请人姓名（模糊匹配）") @RequestParam(required = false) String modifyUserName,
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate
    ) {
        try {
            IPage<MonthlyInspectionRecord> result = monthlyInspectionRecordService.getAuditRecords(
                    current, size, departmentId, recordStatus, auditType, modifyUserName, startDate, endDate);
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询待审核记录失败：" + e.getMessage());
        }
    }

    /**
     * 审核确认
     */
    @Operation(summary = "审核确认")
    @PutMapping("/{id}/audit")
    public Result<String> confirmAudit(
            @Parameter(description = "记录ID") @PathVariable String id,
            @Parameter(description = "审核信息") @RequestBody Map<String, Object> params
    ) {
        try {
            // 从认证信息获取审核人ID，防止IDOR伪造审核人
            String auditorId = userContextUtil.getCurrentUserId();
            if (auditorId == null || auditorId.isEmpty()) {
                return Result.error("用户未认证");
            }
            // 仅具有月检审核权限的用户可以审核通过
            if (!userContextUtil.hasAuditPermission()) {
                return Result.error("当前用户无月检审核权限");
            }
            String auditReason = (String) params.get("auditReason");

            boolean success = monthlyInspectionRecordService.confirmAudit(id, auditorId, auditReason);
            return success ? Result.success("审核成功") : Result.error("审核失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("审核失败：" + e.getMessage());
        }
    }

    /**
     * 拒绝审核（将记录状态从待审核(3)改回已提交(1)）
     */
    @Operation(summary = "拒绝审核")
    @PutMapping("/{id}/reject-audit")
    public Result<String> rejectAudit(
            @Parameter(description = "记录ID") @PathVariable String id
    ) {
        try {
            if (!userContextUtil.hasAuditPermission()) {
                return Result.error("当前用户无月检审核权限");
            }
            boolean success = monthlyInspectionRecordService.rejectAudit(id);
            return success ? Result.success("拒绝成功") : Result.error("拒绝失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("拒绝失败：" + e.getMessage());
        }
    }

    /**
     * 提交编辑/删除/回退审核（公示期内编辑/删除记录或申请回退记录时调用）
     * <p>编辑申请(auditType=1)时,请求体可携带待审核字段(responsiblePerson / responsibleUnit /
     * location / qualityId / qualityName / departmentId / checkUnitId / checkUnitName /
     * rectificationDate / inspector / problemDescription / natureModified),后端会基于此
     * 构造 modification_snapshot JSON 存到原 record 上(原字段值不变),审核通过时由
     * confirmEditAudit 应用。回退申请(auditType=0)时这些字段被忽略。
     * 删除申请(auditType=2)时这些字段同样被忽略,审核通过后由 confirmEditAudit 逻辑删除记录。</p>
     */
    @Operation(summary = "提交编辑/删除/回退审核")
    @PostMapping("/{id}/submit-edit-audit")
    public Result<String> submitEditForAudit(
            @Parameter(description = "记录ID") @PathVariable String id,
            @Parameter(description = "修改说明请求") @RequestBody Map<String, Object> params
    ) {
        try {
            String userId = userContextUtil.getCurrentUserId();
            if (userId == null || userId.isEmpty()) {
                return Result.error("用户未认证");
            }
            String userName = userContextUtil.getCurrentRealName();
            String modifyDescription = (String) params.get("modifyDescription");
            if (modifyDescription == null) {
                return Result.error("请填写修改说明");
            }
            Integer auditType = params.get("auditType") == null ? null
                    : Integer.valueOf(params.get("auditType").toString());

            // V3:编辑申请时,构造 MonthlyInspectionRecord 临时对象承载新字段值
            // 关键:只设置请求体中**实际出现**的字段(用 containsKey 判断),不出现则跳过
            // 避免未修改字段被默认成 null/"" 然后被 diff 误判为「清空」
            MonthlyInspectionRecord pendingEdit = null;
            if (auditType == null || auditType == 1) {
                pendingEdit = new MonthlyInspectionRecord();
                if (params.containsKey("responsiblePerson")) pendingEdit.setResponsiblePerson((String) params.get("responsiblePerson"));
                if (params.containsKey("responsibleUnit")) pendingEdit.setResponsibleUnit((String) params.get("responsibleUnit"));
                if (params.containsKey("location")) pendingEdit.setLocation((String) params.get("location"));
                if (params.containsKey("qualityId")) pendingEdit.setQualityId((String) params.get("qualityId"));
                if (params.containsKey("qualityName")) pendingEdit.setQualityName((String) params.get("qualityName"));
                if (params.containsKey("departmentId")) pendingEdit.setDepartmentId((String) params.get("departmentId"));
                if (params.containsKey("checkUnitId")) pendingEdit.setCheckUnitId((String) params.get("checkUnitId"));
                if (params.containsKey("checkUnitName")) pendingEdit.setCheckUnitName((String) params.get("checkUnitName"));
                // rectificationDate 字段类型为 LocalDate,请求体中是 String,需转换
                if (params.containsKey("rectificationDate")) {
                    String rectDateStr = (String) params.get("rectificationDate");
                    if (rectDateStr != null && !rectDateStr.isEmpty()) {
                        try {
                            pendingEdit.setRectificationDate(java.time.LocalDate.parse(rectDateStr));
                        } catch (Exception ex) {
                            org.slf4j.LoggerFactory.getLogger(MonthlyInspectionRecordController.class)
                                    .warn("rectificationDate 解析失败: {}", rectDateStr);
                        }
                    }
                }
                if (params.containsKey("inspector")) pendingEdit.setInspector((String) params.get("inspector"));
                if (params.containsKey("problemDescription")) pendingEdit.setProblemDescription((String) params.get("problemDescription"));
                if (params.containsKey("natureModified")) {
                    Object nm = params.get("natureModified");
                    if (nm != null) {
                        pendingEdit.setNatureModified(Integer.valueOf(nm.toString()));
                    }
                }
            }

            boolean success = monthlyInspectionRecordService.submitEditForAudit(
                    id, modifyDescription, userId, userName, auditType, pendingEdit);
            return success ? Result.success("审核申请已提交") : Result.error("提交审核申请失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("提交审核申请失败：" + e.getMessage());
        }
    }

    /**
     * 编辑审核通过
     */
    @Operation(summary = "编辑审核通过")
    @PutMapping("/{id}/confirm-edit-audit")
    public Result<String> confirmEditAudit(
            @Parameter(description = "记录ID") @PathVariable String id,
            @Parameter(description = "审核意见") @RequestBody Map<String, Object> params
    ) {
        try {
            String auditorId = userContextUtil.getCurrentUserId();
            if (auditorId == null || auditorId.isEmpty()) {
                return Result.error("用户未认证");
            }
            if (!userContextUtil.hasAuditPermission()) {
                return Result.error("当前用户无月检审核权限");
            }
            String auditReason = (String) params.get("auditReason");
            boolean success = monthlyInspectionRecordService.confirmEditAudit(id, auditorId, auditReason);
            return success ? Result.success("审核通过") : Result.error("审核通过失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("审核通过失败：" + e.getMessage());
        }
    }

    /**
     * 编辑审核拒绝
     */
    @Operation(summary = "编辑审核拒绝")
    @PutMapping("/{id}/reject-edit-audit")
    public Result<String> rejectEditAudit(
            @Parameter(description = "记录ID") @PathVariable String id,
            @Parameter(description = "拒绝原因") @RequestBody(required = false) Map<String, Object> params
    ) {
        try {
            if (!userContextUtil.hasAuditPermission()) {
                return Result.error("当前用户无月检审核权限");
            }
            String auditReason = params == null ? null : (String) params.get("auditReason");
            boolean success = monthlyInspectionRecordService.rejectEditAudit(id, auditReason);
            return success ? Result.success("已拒绝") : Result.error("拒绝失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("拒绝失败：" + e.getMessage());
        }
    }
}