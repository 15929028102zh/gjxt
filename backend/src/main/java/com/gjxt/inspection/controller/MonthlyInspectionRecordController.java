package com.gjxt.inspection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.entity.MonthlyInspectionRecord;
import com.gjxt.inspection.service.IMonthlyInspectionRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
            @Parameter(description = "记录状态") @RequestParam(required = false) Integer recordStatus,
            @Parameter(description = "创建用户ID") @RequestParam(required = false) String createUserId,
            @Parameter(description = "整改期限（筛选小于该日期）yyyy-MM-dd") @RequestParam(required = false) String rectificationDeadlineBefore
    ) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("taskId", taskId);
            params.put("taskNo", taskNo);
            params.put("professionalTypeId", professionalTypeId);
            params.put("responsibilityId", responsibilityId);
            params.put("recordStatus", recordStatus);
            params.put("createUserId", createUserId);
            params.put("rectificationDeadlineBefore", rectificationDeadlineBefore);
            
            IPage<MonthlyInspectionRecord> result = monthlyInspectionRecordService.getRecordPage(current, size, params);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("查询月检记录列表失败：" + e.getMessage());
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
            @Parameter(description = "记录信息") @RequestBody MonthlyInspectionRecord record,
            HttpServletRequest request
    ) {
        try {
            // 从请求头中获取用户ID
            String createUserId = request.getHeader("userId");
            if (createUserId == null || createUserId.isEmpty()) {
                return Result.error("用户信息缺失");
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

            String natureDefinition = (String) params.get("natureDefinition");
            // 允许ID为字符串或数字
            String professionId = params.get("professionId") == null ? null : params.get("professionId").toString();
            String departmentId = params.get("departmentId") == null ? null : params.get("departmentId").toString();

            // 允许status为数字或字符串
            Integer status;
            Object statusObj = params.get("status");
            if (statusObj instanceof Number) {
                status = ((Number) statusObj).intValue();
            } else if (statusObj instanceof String) {
                try {
                    status = Integer.parseInt((String) statusObj);
                } catch (NumberFormatException e) {
                    status = null;
                }
            } else {
                status = null;
            }
            
            boolean result = monthlyInspectionRecordService.confirmRectification(
                id, rectificationSuggestion, rectificationEffect, rectificationImages,
                natureDefinition, professionId, departmentId, status
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
            
            boolean result = monthlyInspectionRecordService.confirmReview(
                id, reviewSuggestion, reviewEffect, reviewImages
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
     * 查询待审核记录
     */
    @Operation(summary = "查询待审核记录")
    @GetMapping("/audit-list")
    public Result<IPage<MonthlyInspectionRecord>> getAuditRecords(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") long size,
            @Parameter(description = "部门ID") @RequestParam(required = false) String departmentId,
            @Parameter(description = "记录状态") @RequestParam(required = false) Integer recordStatus,
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate
    ) {
        try {
            IPage<MonthlyInspectionRecord> result = monthlyInspectionRecordService.getAuditRecords(
                    current, size, departmentId, recordStatus, startDate, endDate);
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
            String auditorId = (String) params.get("auditorId");
            String auditReason = (String) params.get("auditReason");
            
            if (auditorId == null || auditorId.isEmpty()) {
                return Result.error("审核人ID不能为空");
            }
            
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
            boolean success = monthlyInspectionRecordService.rejectAudit(id);
            return success ? Result.success("拒绝成功") : Result.error("拒绝失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("拒绝失败：" + e.getMessage());
        }
    }
}