package com.gjxt.inspection.controller;

import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.entity.SysInspectionTask;
import com.gjxt.inspection.service.ISysInspectionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 巡检任务管理控制器
 *
 * @author gjxt
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/inspection-task")
public class SysInspectionTaskController {

    @Autowired
    private ISysInspectionTaskService inspectionTaskService;

    /**
     * 分页查询巡检任务列表
     */
    @GetMapping("/page")
    public Result<PageResult<SysInspectionTask>> getInspectionTaskPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String taskName,
            @RequestParam(required = false) String locationId,
            @RequestParam(required = false) String assignedUserId,
            @RequestParam(required = false) Integer status) {
        try {
            PageResult<SysInspectionTask> result = inspectionTaskService.getInspectionTaskPage(current, size, taskName, locationId, assignedUserId, status);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询巡检任务详情
     */
    @GetMapping("/{id}")
    public Result<SysInspectionTask> getInspectionTaskById(@PathVariable String id) {
        try {
            SysInspectionTask task = inspectionTaskService.getById(id);
            if (task != null) {
                return Result.success(task);
            }
            return Result.error("巡检任务不存在");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 新增巡检任务
     */
    @PostMapping
    public Result<Void> addInspectionTask(@Valid @RequestBody SysInspectionTask task) {
        try {
            boolean success = inspectionTaskService.addInspectionTask(task);
            return success ? Result.success("新增巡检任务成功") : Result.error("新增巡检任务失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新巡检任务
     */
    @PutMapping
    public Result<Void> updateInspectionTask(@Valid @RequestBody SysInspectionTask task) {
        try {
            boolean success = inspectionTaskService.updateInspectionTask(task);
            return success ? Result.success("更新巡检任务成功") : Result.error("更新巡检任务失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除巡检任务
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteInspectionTask(@PathVariable String id) {
        try {
            boolean success = inspectionTaskService.deleteTask(Long.valueOf(id));
            return success ? Result.success("删除巡检任务成功") : Result.error("删除巡检任务失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除巡检任务
     */
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteTasks(@RequestBody List<String> ids) {
        try {
            boolean success = inspectionTaskService.batchDeleteTasks(ids);
            return success ? Result.success("批量删除巡检任务成功") : Result.error("批量删除巡检任务失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分配巡检任务
     */
    @PutMapping("/{id}/assign")
    public Result<Void> assignTask(@PathVariable String id, @RequestBody Map<String, Object> params) {
        try {
            String assignedUserId = (String) params.get("assignedUserId");
            String plannedInspectionTime = (String) params.get("plannedInspectionTime");
            boolean success = inspectionTaskService.assignTask(id, assignedUserId, plannedInspectionTime);
            return success ? Result.success("分配巡检任务成功") : Result.error("分配巡检任务失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 完成巡检任务
     */
    @PutMapping("/{id}/complete")
    public Result<Void> completeTask(@PathVariable String id, @RequestBody Map<String, Object> params) {
        try {
            String actualInspectionTime = (String) params.get("actualInspectionTime");
            String inspectionResult = (String) params.get("inspectionResult");
            String inspectionImages = (String) params.get("inspectionImages");
            boolean success = inspectionTaskService.completeTask(Long.valueOf(id));
            return success ? Result.success("完成巡检任务成功") : Result.error("完成巡检任务失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户的巡检任务列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<SysInspectionTask>> getUserInspectionTasks(@PathVariable String userId) {
        try {
            List<SysInspectionTask> tasks = inspectionTaskService.getUserInspectionTasks(userId);
            return Result.success(tasks);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取待分配的巡检任务列表
     */
    @GetMapping("/pending")
    public Result<List<SysInspectionTask>> getPendingInspectionTasks() {
        try {
            List<SysInspectionTask> tasks = inspectionTaskService.getPendingInspectionTasks();
            return Result.success(tasks);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取已完成的巡检任务列表
     */
    @GetMapping("/completed")
    public Result<List<SysInspectionTask>> getCompletedInspectionTasks() {
        try {
            List<SysInspectionTask> tasks = inspectionTaskService.getCompletedInspectionTasks();
            return Result.success(tasks);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取超期的巡检任务列表
     */
    @GetMapping("/overdue")
    public Result<List<SysInspectionTask>> getOverdueInspectionTasks() {
        try {
            List<SysInspectionTask> tasks = inspectionTaskService.getOverdueInspectionTasks();
            return Result.success(tasks);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}
