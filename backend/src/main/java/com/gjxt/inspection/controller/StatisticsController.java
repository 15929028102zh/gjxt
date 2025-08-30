package com.gjxt.inspection.controller;

import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.service.ISysInspectionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计分析控制�? *
 * @author gjxt
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private ISysInspectionRecordService inspectionRecordService;

    /**
     * 获取汇总统计数�?     * 包含各状态问题数量、专业分布、部位分布、责任人分布�?     */
    @GetMapping("/summary")
    public Result<Map<String, Object>> getSummaryStatistics() {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 各状态问题数量统计
            List<Map<String, Object>> statusStatsList = inspectionRecordService.getStatusStatistics();
            result.put("statusStatistics", statusStatsList);
            
            // 各专业问题数量统计
            List<Map<String, Object>> professionStats = inspectionRecordService.getProfessionStatistics(null);
            result.put("professionStatistics", professionStats);
            
            // 各部位问题数量统计
            List<Map<String, Object>> locationStats = inspectionRecordService.getLocationStatistics(null);
            result.put("locationStatistics", locationStats);
            
            // 各责任人问题数量统计
            List<Map<String, Object>> responsibleStats = inspectionRecordService.getResponsiblePersonStatistics(null);
            result.put("responsibleStatistics", responsibleStats);
            
            // 转换状态统计为Map格式便于计算
            Map<String, Long> statusStats = new HashMap<>();
            for (Map<String, Object> stat : statusStatsList) {
                String statusName = (String) stat.get("status_name");
                Long count = ((Number) stat.get("count")).longValue();
                statusStats.put(statusName, count);
            }
            
            // 计算总数和占比
            long totalCount = statusStats.values().stream().mapToLong(Long::longValue).sum();
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalCount", totalCount);
            summary.put("pendingRectifyCount", statusStats.getOrDefault("待整改", 0L));
            summary.put("pendingConfirmCount", statusStats.getOrDefault("待确认", 0L));
            summary.put("delayedRectifyCount", statusStats.getOrDefault("延迟整改", 0L));
            summary.put("completedCount", statusStats.getOrDefault("已整改", 0L));
            
            // 计算占比
            if (totalCount > 0) {
                summary.put("pendingRectifyRate", String.format("%.2f", (statusStats.getOrDefault("待整改", 0L) * 100.0 / totalCount)));
                summary.put("pendingConfirmRate", String.format("%.2f", (statusStats.getOrDefault("待确认", 0L) * 100.0 / totalCount)));
                summary.put("delayedRectifyRate", String.format("%.2f", (statusStats.getOrDefault("延迟整改", 0L) * 100.0 / totalCount)));
                summary.put("completedRate", String.format("%.2f", (statusStats.getOrDefault("已整改", 0L) * 100.0 / totalCount)));
            }
            
            result.put("summary", summary);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取各状态问题数量统计（用于柱状图）
     */
    @GetMapping("/status-chart")
    public Result<Map<String, Object>> getStatusChartData() {
        try {
            List<Map<String, Object>> statusStatsList = inspectionRecordService.getStatusStatistics();
            
            // 转换为Map格式
            Map<String, Long> statusStats = new HashMap<>();
            for (Map<String, Object> stat : statusStatsList) {
                String statusName = (String) stat.get("status_name");
                Long count = ((Number) stat.get("count")).longValue();
                statusStats.put(statusName, count);
            }
            
            Map<String, Object> chartData = new HashMap<>();
            chartData.put("categories", new String[]{"待整改", "待确认", "延迟整改", "已整改"});
            chartData.put("data", new Long[]{
                statusStats.getOrDefault("待整改", 0L),
                statusStats.getOrDefault("待确认", 0L),
                statusStats.getOrDefault("延迟整改", 0L),
                statusStats.getOrDefault("已整改", 0L)
            });
            
            return Result.success(chartData);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取各专业问题分布统计（用于饼图）
     */
    @GetMapping("/profession-chart")
    public Result<List<Map<String, Object>>> getProfessionChartData() {
        try {
            List<Map<String, Object>> professionStats = inspectionRecordService.getProfessionStatistics(null);
            return Result.success(professionStats);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取各部位问题分布统计（用于饼图）
     */
    @GetMapping("/location-chart")
    public Result<List<Map<String, Object>>> getLocationChartData() {
        try {
            List<Map<String, Object>> locationStats = inspectionRecordService.getLocationStatistics(null);
            return Result.success(locationStats);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取各责任人问题分布统计（用于柱状图）
     */
    @GetMapping("/responsible-chart")
    public Result<List<Map<String, Object>>> getResponsibleChartData() {
        try {
            List<Map<String, Object>> responsibleStats = inspectionRecordService.getResponsiblePersonStatistics(null);
            return Result.success(responsibleStats);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据状态获取问题详细列表（用于图表交互）
     */
    @GetMapping("/status-details")
    public Result<Map<String, Object>> getStatusDetails(
            @RequestParam String status,
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        try {
            Map<String, Object> result = inspectionRecordService.getRecordsByStatus(status, current, size);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据专业获取问题详细列表（用于图表交互）
     */
    @GetMapping("/profession-details")
    public Result<Map<String, Object>> getProfessionDetails(
            @RequestParam String profession,
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        try {
            Map<String, Object> result = inspectionRecordService.getRecordsByProfession(profession, current, size);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据部位获取问题详细列表（用于图表交互）
     */
    @GetMapping("/location-details")
    public Result<Map<String, Object>> getLocationDetails(
            @RequestParam String location,
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        try {
            Map<String, Object> result = inspectionRecordService.getRecordsByLocation(location, current, size);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据责任人获取问题详细列表（用于图表交互）
     */
    @GetMapping("/responsible-details")
    public Result<Map<String, Object>> getResponsibleDetails(
            @RequestParam String responsiblePerson,
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        try {
            Map<String, Object> result = inspectionRecordService.getRecordsByResponsible(responsiblePerson, current, size);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取时间范围内的统计数据
     */
    @GetMapping("/time-range")
    public Result<Map<String, Object>> getTimeRangeStatistics(
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String responsiblePerson) {
        try {
            Map<String, Object> result = inspectionRecordService.getTimeRangeStatistics(
                    startTime, endTime, profession, location, responsiblePerson);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}