package com.gjxt.inspection.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.entity.MonthlyInspectionTask;
import com.gjxt.inspection.service.IMonthlyInspectionTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 月检任务控制器
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Tag(name = "月检任务管理", description = "月检任务管理相关接口")
@RestController
@RequestMapping("/monthly-inspection-task")
public class MonthlyInspectionTaskController {

    @Autowired
    private IMonthlyInspectionTaskService monthlyInspectionTaskService;

    /**
     * 分页查询月检任务列表
     */
    @Operation(summary = "分页查询月检任务列表")
    @GetMapping("/page")
    public Result<Page<MonthlyInspectionTask>> getTaskPage(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") long size,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "任务名称（模糊）") @RequestParam(required = false) String taskName,
            @Parameter(description = "月检开始时间下界，格式 yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) String inspectionStartTime,
            @Parameter(description = "月检结束时间上界，格式 yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) String inspectionEndTime
    ) {
        try {
            LocalDateTime start = parseDateTime(inspectionStartTime);
            LocalDateTime end = parseDateTime(inspectionEndTime);
            Page<MonthlyInspectionTask> page = monthlyInspectionTaskService.getTaskPage(
                    current, size, status, taskName, start, end
            );
            return Result.success(page);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询月检任务列表失败：" + e.getMessage());
        }
    }

    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据ID查询任务详情
     */
    @Operation(summary = "根据ID查询任务详情")
    @GetMapping("/{id}")
    public Result<MonthlyInspectionTask> getTaskDetail(
            @Parameter(description = "任务ID") @PathVariable String id
    ) {
        try {
            MonthlyInspectionTask task = monthlyInspectionTaskService.getTaskDetail(id);
            if (task != null) {
                return Result.success(task);
            } else {
                return Result.error("任务不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询任务详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建月检任务
     */
    @Operation(summary = "创建月检任务")
    @PostMapping
    public Result<String> createTask(
            @Parameter(description = "任务信息") @RequestBody MonthlyInspectionTask task,
            HttpServletRequest request
    ) {
        try {
            // 从请求中获取当前用户ID（实际项目中可能从JWT token或session中获取）
            String createUserId = request.getHeader("userId");
            if (createUserId == null) {
                createUserId = "admin"; // 默认值，实际项目中应该从认证信息中获取
            }

            boolean result = monthlyInspectionTaskService.createTask(task, createUserId);
            if (result) {
                return Result.success("创建月检任务成功");
            } else {
                return Result.error("创建月检任务失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("创建月检任务失败：" + e.getMessage());
        }
    }

    /**
     * 更新月检任务
     */
    @Operation(summary = "更新月检任务")
    @PutMapping
    public Result<String> updateTask(
            @Parameter(description = "任务信息") @RequestBody MonthlyInspectionTask task
    ) {
        try {
            boolean result = monthlyInspectionTaskService.updateTask(task);
            if (result) {
                return Result.success("更新月检任务成功");
            } else {
                return Result.error("更新月检任务失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新月检任务失败：" + e.getMessage());
        }
    }

    /**
     * 删除月检任务
     */
    @Operation(summary = "删除月检任务")
    @DeleteMapping("/{id}")
    public Result<String> deleteTask(
            @Parameter(description = "任务ID") @PathVariable String id
    ) {
        try {
            boolean result = monthlyInspectionTaskService.deleteTask(id);
            if (result) {
                return Result.success("删除月检任务成功");
            } else {
                return Result.error("删除月检任务失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除月检任务失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除月检任务
     */
    @Operation(summary = "批量删除月检任务")
    @DeleteMapping("/batch")
    public Result<String> batchDeleteTasks(
            @Parameter(description = "任务ID列表") @RequestBody List<String> ids
    ) {
        try {
            boolean result = monthlyInspectionTaskService.batchDeleteTasks(ids);
            if (result) {
                return Result.success("批量删除月检任务成功");
            } else {
                return Result.error("批量删除月检任务失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("批量删除月检任务失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID查询相关任务
     */
    @Operation(summary = "根据用户ID查询相关任务")
    @GetMapping("/user/{userId}")
    public Result<List<MonthlyInspectionTask>> getTasksByUserId(
            @Parameter(description = "用户ID") @PathVariable String userId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status
    ) {
        try {
            List<MonthlyInspectionTask> tasks = monthlyInspectionTaskService.getTasksByUserId(userId, status);
            return Result.success(tasks);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询用户相关任务失败：" + e.getMessage());
        }
    }

    /**
     * 更新任务状态
     */
    @Operation(summary = "更新任务状态")
    @PutMapping("/{id}/status")
    public Result<String> updateTaskStatus(
            @Parameter(description = "任务ID") @PathVariable String id,
            @Parameter(description = "状态") @RequestParam Integer status
    ) {
        try {
            boolean result = monthlyInspectionTaskService.updateTaskStatus(id, status);
            if (result) {
                return Result.success("更新任务状态成功");
            } else {
                return Result.error("更新任务状态失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新任务状态失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新任务状态
     */
    @Operation(summary = "批量更新任务状态")
    @PutMapping("/batch/status")
    public Result<String> batchUpdateTaskStatus(
            @Parameter(description = "任务ID列表") @RequestBody List<String> ids,
            @Parameter(description = "状态") @RequestParam Integer status
    ) {
        try {
            boolean result = monthlyInspectionTaskService.batchUpdateTaskStatus(ids, status);
            if (result) {
                return Result.success("批量更新任务状态成功");
            } else {
                return Result.error("批量更新任务状态失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("批量更新任务状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取状态统计
     */
    @Operation(summary = "获取状态统计")
    @GetMapping("/statistics/status")
    public Result<List<Map<String, Object>>> getStatusStatistics() {
        try {
            List<Map<String, Object>> statistics = monthlyInspectionTaskService.getStatusStatistics();
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
            @Parameter(description = "状态") @RequestParam(required = false) Integer status
    ) {
        try {
            List<Map<String, Object>> statistics = monthlyInspectionTaskService.getDepartmentStatistics(status);
            return Result.success(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取部门统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取专业统计
     */
    @Operation(summary = "获取专业统计")
    @GetMapping("/statistics/profession")
    public Result<List<Map<String, Object>>> getProfessionStatistics(
            @Parameter(description = "状态") @RequestParam(required = false) Integer status
    ) {
        try {
            List<Map<String, Object>> statistics = monthlyInspectionTaskService.getProfessionStatistics(status);
            return Result.success(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取专业统计失败：" + e.getMessage());
        }
    }

    /**
     * 发送任务通知
     */
    @Operation(summary = "发送任务通知")
    @PostMapping("/{id}/notify")
    public Result<String> sendTaskNotification(
            @Parameter(description = "任务ID") @PathVariable String id
    ) {
        try {
            boolean result = monthlyInspectionTaskService.sendTaskNotification(id);
            if (result) {
                return Result.success("发送任务通知成功");
            } else {
                return Result.error("发送任务通知失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("发送任务通知失败：" + e.getMessage());
        }
    }

    /**
     * 批量发送任务通知
     */
    @Operation(summary = "批量发送任务通知")
    @PostMapping("/batch/notify")
    public Result<String> batchSendTaskNotifications() {
        try {
            boolean result = monthlyInspectionTaskService.batchSendTaskNotifications();
            if (result) {
                return Result.success("批量发送任务通知成功");
            } else {
                return Result.error("批量发送任务通知失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("批量发送任务通知失败：" + e.getMessage());
        }
    }

    /**
     * 生成任务编号
     */
    @Operation(summary = "生成任务编号")
    @GetMapping("/generate-task-no")
    public Result<String> generateTaskNo() {
        try {
            String taskNo = monthlyInspectionTaskService.generateTaskNo();
            return Result.success(taskNo);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("生成任务编号失败：" + e.getMessage());
        }
    }

    /**
     * 检查任务编号是否存在
     */
    @Operation(summary = "检查任务编号是否存在")
    @GetMapping("/check-task-no")
    public Result<Boolean> checkTaskNoExists(
            @Parameter(description = "任务编号") @RequestParam String taskNo
    ) {
        try {
            boolean exists = monthlyInspectionTaskService.isTaskNoExists(taskNo);
            return Result.success(exists);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("检查任务编号失败：" + e.getMessage());
        }
    }

    /**
     * 根据任务编号查询任务
     */
    @Operation(summary = "根据任务编号查询任务")
    @GetMapping("/task-no/{taskNo}")
    public Result<MonthlyInspectionTask> getTaskByTaskNo(
            @Parameter(description = "任务编号") @PathVariable String taskNo
    ) {
        try {
            MonthlyInspectionTask task = monthlyInspectionTaskService.getTaskByTaskNo(taskNo);
            if (task != null) {
                return Result.success(task);
            } else {
                return Result.error("任务不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询任务失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户任务统计
     */
    @Operation(summary = "获取用户任务统计")
    @GetMapping("/user/{userId}/statistics")
    public Result<Map<String, Long>> getUserTaskStatistics(
            @Parameter(description = "用户ID") @PathVariable String userId
    ) {
        try {
            long pendingCount = monthlyInspectionTaskService.getPendingTaskCount(userId);
            long inProgressCount = monthlyInspectionTaskService.getInProgressTaskCount(userId);
            long completedCount = monthlyInspectionTaskService.getCompletedTaskCount(userId);

            Map<String, Long> statistics = new HashMap<>();
            statistics.put("pending", pendingCount);
            statistics.put("inProgress", inProgressCount);
            statistics.put("completed", completedCount);
            statistics.put("total", pendingCount + inProgressCount + completedCount);

            return Result.success(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取用户任务统计失败：" + e.getMessage());
        }
    }
}