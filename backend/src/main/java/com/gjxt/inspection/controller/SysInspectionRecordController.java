package com.gjxt.inspection.controller;

import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.entity.SysInspectionRecord;
import com.gjxt.inspection.service.ISysInspectionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import com.gjxt.inspection.entity.SysInspectionRecordSubmitData;
import java.util.Map;

/**
 * 周检记录管理控制器
 *
 * @author gjxt
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/inspection-record")
public class SysInspectionRecordController {

    @Autowired
    private ISysInspectionRecordService inspectionRecordService;

    /**
     * 分页查询周检记录列表
     */
    @GetMapping("/page")
    public Result<PageResult<SysInspectionRecord>> getRecordPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String taskId,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String professionId,
            @RequestParam(required = false) String natureId,
            @RequestParam(required = false) String locationId,
            @RequestParam(required = false) String inspectorId,
            @RequestParam(required = false) String responsibleUnitId,
            @RequestParam(required = false) String responsiblePersonId,
            @RequestParam(required = false) String reviewerId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String problemDescription,
            @RequestParam(required = false) String responsiblePerson,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        try {
            // 转换时间字符串为LocalDateTime
            java.time.LocalDateTime startDateTime = null;
            java.time.LocalDateTime endDateTime = null;
            if (startTime != null && !startTime.isEmpty()) {
                // 如果已经包含时间部分，直接解析；否则添加默认时间
                if (startTime.contains("T")) {
                    startDateTime = java.time.LocalDateTime.parse(startTime);
                } else {
                    startDateTime = java.time.LocalDateTime.parse(startTime + "T00:00:00");
                }
            }
            if (endTime != null && !endTime.isEmpty()) {
                // 如果已经包含时间部分，直接解析；否则添加默认时间
                if (endTime.contains("T")) {
                    endDateTime = java.time.LocalDateTime.parse(endTime);
                } else {
                    endDateTime = java.time.LocalDateTime.parse(endTime + "T23:59:59");
                }
            }
            
            PageResult<SysInspectionRecord> result = inspectionRecordService.getRecordPage(
                    current, size, taskId, departmentId,
                    professionId, natureId, locationId, inspectorId, responsibleUnitId, 
                    responsiblePersonId, reviewerId, status, 
                    problemDescription, responsiblePerson,
                    startDateTime, endDateTime);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询巡检记录详情
     */
    @GetMapping("/{id}")
    public Result<SysInspectionRecord> getRecordById(@PathVariable String id) {
        try {
            SysInspectionRecord record = inspectionRecordService.getRecordById(id);
            if (record != null) {
                return Result.success(record);
            }
            return Result.error("巡检记录不存在");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 新增巡检记录
     */
    @PostMapping
    public Result<Void> addRecord(@Valid @RequestBody SysInspectionRecord record) {
        try {
            boolean success = inspectionRecordService.addRecord(record);
            return success ? Result.success("新增巡检记录成功") : Result.error("新增巡检记录失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 提交巡检记录（前端专用接口）
     */
    @PostMapping("/submit")
    public Result<String> submitRecord(@RequestBody SysInspectionRecordSubmitData submitData) {
        try {
            boolean success = inspectionRecordService.submitRecord(submitData);
            
            if (success) {
                return Result.success("巡检记录提交成功");
            } else {
                return Result.error("巡检记录提交失败");
            }
        } catch (Exception e) {
            return Result.error("系统异常：" + e.getMessage());
        }
    }

    /**
     * 更新巡检记录
     */
    @PutMapping
    public Result<Void> updateRecord(@Valid @RequestBody SysInspectionRecord record) {
        try {
            boolean success = inspectionRecordService.updateRecord(record);
            return success ? Result.success("更新巡检记录成功") : Result.error("更新巡检记录失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除巡检记录
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRecord(@PathVariable String id) {
        try {
            boolean success = inspectionRecordService.deleteRecord(id);
            return success ? Result.success("删除巡检记录成功") : Result.error("删除巡检记录失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除巡检记录
     */
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteRecords(@RequestBody List<String> ids) {
        try {
            boolean success = inspectionRecordService.batchDeleteRecords(ids);
            return success ? Result.success("批量删除巡检记录成功") : Result.error("批量删除巡检记录失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 整改确认
     */
    @PutMapping("/{id}/rectify")
    public Result<Void> rectifyConfirm(@PathVariable String id, @RequestBody Map<String, Object> params) {
        try {
            String rectificationEffect = (String) params.get("rectificationEffect");
            String rectificationImageUrls = (String) params.get("rectificationImageUrls");
            boolean success = inspectionRecordService.rectifyConfirm(id, rectificationEffect, rectificationImageUrls);
            return success ? Result.success("整改确认成功") : Result.error("整改确认失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 复查确认
     */
    @PutMapping("/{id}/review")
    public Result<Void> reviewConfirm(@PathVariable String id, @RequestBody Map<String, Object> params) {
        try {
            String reviewerId = (String) params.get("reviewerId");
            String reviewEffect = (String) params.get("reviewEffect");
            String reviewImageUrls = (String) params.get("reviewImageUrls");
            boolean success = inspectionRecordService.reviewConfirm(id, reviewerId, reviewEffect, reviewImageUrls);
            return success ? Result.success("复查确认成功") : Result.error("复查确认失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询待提交的周检记录列表（状态为4已保存）
     */
    @GetMapping("/submit-list")
    public Result<PageResult<SysInspectionRecord>> getSubmitList(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String responsiblePersonId,
            @RequestParam(required = false) String inspectorId,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        try {
            // 转换时间字符串为LocalDateTime
            java.time.LocalDateTime startDateTime = null;
            java.time.LocalDateTime endDateTime = null;
            if (startTime != null && !startTime.isEmpty()) {
                if (startTime.contains("T")) {
                    startDateTime = java.time.LocalDateTime.parse(startTime);
                } else {
                    startDateTime = java.time.LocalDateTime.parse(startTime + "T00:00:00");
                }
            }
            if (endTime != null && !endTime.isEmpty()) {
                if (endTime.contains("T")) {
                    endDateTime = java.time.LocalDateTime.parse(endTime);
                } else {
                    endDateTime = java.time.LocalDateTime.parse(endTime + "T23:59:59");
                }
            }
            
            // 查询状态为4（已保存）的记录
            PageResult<SysInspectionRecord> result = inspectionRecordService.getRecordPage(
                    current, size, null,
                    departmentId, null, null, null, inspectorId, 
                    null, responsiblePersonId, null, "4", 
                    null, null, startDateTime, endDateTime);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量更新记录状态
     */
    @PutMapping("/batch-status")
    public Result<Void> batchUpdateStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            Integer status = (Integer) params.get("status");
            
            if (ids == null || ids.isEmpty()) {
                return Result.error("记录ID列表不能为空");
            }
            if (status == null) {
                return Result.error("状态不能为空");
            }
            
            boolean success = inspectionRecordService.batchUpdateStatus(ids, status);
            return success ? Result.success("批量更新状态成功") : Result.error("批量更新状态失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 单个记录状态更新
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateSingleStatus(@PathVariable String id, @RequestBody Map<String, Object> params) {
        try {
            Integer status = (Integer) params.get("status");
            
            if (status == null) {
                return Result.error("状态不能为空");
            }
            
            boolean success = inspectionRecordService.updateSingleStatus(id, status);
            return success ? Result.success("更新状态成功") : Result.error("更新状态失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询用户巡检记录
     */
    @GetMapping("/user/{userId}")
    public Result<PageResult<SysInspectionRecord>> getUserRecords(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String departmentId) {
        try {
            PageResult<SysInspectionRecord> result = inspectionRecordService.getUserRecords(userId, current, size, status, departmentId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取统计概览
     */
    @GetMapping("/statistics/overview")
    public Result<Map<String, Object>> getStatisticsOverview(
            @RequestParam(required = false) String departmentId) {
        try {
            Map<String, Object> overview = inspectionRecordService.getStatisticsOverview(departmentId);
            return Result.success(overview);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取月检统计数据
     */
    @GetMapping("/statistics/monthly")
    public Result<Map<String, Object>> getMonthlyStatistics(
            @RequestParam(required = false) String departmentId) {
        try {
            Map<String, Object> statistics = inspectionRecordService.getMonthlyStatistics(departmentId);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 统计各状态问题数量
     */
    @GetMapping("/statistics/status")
    public Result<List<Map<String, Object>>> getStatusStatistics(
            @RequestParam(required = false) String departmentId) {
        try {
            List<Map<String, Object>> statistics = inspectionRecordService.getStatusStatistics(departmentId);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 统计各专业问题数量
     */
    @GetMapping("/statistics/profession")
    public Result<List<Map<String, Object>>> getProfessionStatistics(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String departmentId) {
        try {
            List<Map<String, Object>> statistics = inspectionRecordService.getProfessionStatistics(status, departmentId);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 统计各部位问题数量
     */
    @GetMapping("/statistics/location")
    public Result<List<Map<String, Object>>> getLocationStatistics(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String departmentId) {
        try {
            List<Map<String, Object>> statistics = inspectionRecordService.getLocationStatistics(status, departmentId);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 统计各责任人问题数量
     */
    @GetMapping("/statistics/responsible")
    public Result<List<Map<String, Object>>> getResponsibleStatistics(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String departmentId) {
        try {
            List<Map<String, Object>> statistics = inspectionRecordService.getResponsiblePersonStatistics(status, departmentId);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 处理延迟整改问题
     */
    @PostMapping("/handle-delayed")
    public Result<Void> handleDelayedRectification() {
        try {
            boolean success = inspectionRecordService.handleDelayedRectification();
            if (success) {
                return Result.success("处理延迟整改问题成功");
            } else {
                return Result.error("处理延迟整改问题失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询延迟整改记录
     */
    @GetMapping("/delayed")
    public Result<PageResult<SysInspectionRecord>> getDelayedRectificationRecords(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String departmentId) {
        try {
            PageResult<SysInspectionRecord> result = inspectionRecordService.getDelayedRectificationRecords(current, size, departmentId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 导出巡检记录
     */
    @GetMapping("/export")
    public void exportRecords(
            HttpServletResponse response,
            @RequestParam(required = false) String problemDescription,
            @RequestParam(required = false) String responsiblePerson,
            @RequestParam(required = false) String professionId,
            @RequestParam(required = false) String natureId,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        try {
            // 转换时间字符串为LocalDateTime
            java.time.LocalDateTime startDateTime = null;
            java.time.LocalDateTime endDateTime = null;
            if (startTime != null && !startTime.isEmpty()) {
                if (startTime.contains("T")) {
                    startDateTime = java.time.LocalDateTime.parse(startTime);
                } else {
                    startDateTime = java.time.LocalDateTime.parse(startTime + "T00:00:00");
                }
            }
            if (endTime != null && !endTime.isEmpty()) {
                if (endTime.contains("T")) {
                    endDateTime = java.time.LocalDateTime.parse(endTime);
                } else {
                    endDateTime = java.time.LocalDateTime.parse(endTime + "T23:59:59");
                }
            }
            
            inspectionRecordService.exportRecordsToExcel(response, problemDescription, responsiblePerson, 
                    professionId, natureId, departmentId, status, startDateTime, endDateTime);
        } catch (Exception e) {
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    /**
     * 上传问题现场照片
     */
    @PostMapping("/upload/problem-photo")
    public Result<String> uploadProblemPhoto(@RequestParam("file") MultipartFile file) {
        try {
            // TODO: 实现文件上传逻辑
            String filePath = "/uploads/problem/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            return Result.success("上传成功", filePath);
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传整改照片
     */
    @PostMapping("/upload/rectify-photo")
    public Result<String> uploadRectifyPhoto(@RequestParam("file") MultipartFile file) {
        try {
            // TODO: 实现文件上传逻辑
            String filePath = "/uploads/rectify/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            return Result.success("上传成功", filePath);
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    /**
     * 按用户ID和状态查询当前周记录数量
     */
    @GetMapping("/weekly-count")
    public Result<Long> getWeeklyRecordCount(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Long count = inspectionRecordService.getWeeklyRecordCount(userId, status, departmentId, startDate, endDate);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}
