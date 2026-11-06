package com.gjxt.inspection.controller;

import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.entity.SysDept;
import com.gjxt.inspection.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 部门管理控制器
 *
 * @author gjxt
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/dept")
public class SysDeptController {

    @Autowired
    private ISysDeptService deptService;

    /**
     * 分页查询部门列表
     */
    @GetMapping("/page")
    public Result<PageResult<SysDept>> getDeptPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String deptName,
            @RequestParam(required = false) Integer status) {
        try {
            PageResult<SysDept> result = deptService.getDeptPage(current, size, deptName, status);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询部门树形结构
     */
    @GetMapping("/tree")
    public Result<List<SysDept>> getDeptTree() {
        try {
            List<SysDept> tree = deptService.getDeptTree();
            return Result.success(tree);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询所有启用状态的部门列表
     * V3:可选 deptType 参数(0=运行部门 1=管理部门)
     */
    @GetMapping("/list")
    public Result<List<SysDept>> getEnabledDeptList(
            @RequestParam(required = false) Integer deptType) {
        try {
            List<SysDept> list = deptService.getEnabledDeptList(deptType);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询部门详情
     */
    @GetMapping("/{id}")
    public Result<SysDept> getDeptById(@PathVariable String id) {
        try {
            SysDept dept = deptService.getById(id);
            if (dept != null) {
                return Result.success(dept);
            }
            return Result.error("部门不存在");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 新增部门
     */
    @PostMapping
    public Result<Void> addDept(@Valid @RequestBody SysDept dept) {
        try {
            boolean success = deptService.addDept(dept);
            return success ? Result.success("新增部门成功") : Result.error("新增部门失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新部门
     */
    @PutMapping
    public Result<Void> updateDept(@Valid @RequestBody SysDept dept) {
        try {
            boolean success = deptService.updateDept(dept);
            return success ? Result.success("更新部门成功") : Result.error("更新部门失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDept(@PathVariable String id) {
        try {
            boolean success = deptService.deleteDept(id);
            return success ? Result.success("删除部门成功") : Result.error("删除部门失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除部门
     */
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteDepts(@RequestBody List<String> ids) {
        try {
            boolean success = deptService.batchDeleteDepts(ids);
            return success ? Result.success("批量删除部门成功") : Result.error("批量删除部门失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查部门名称是否存在
     */
    @GetMapping("/check-name")
    public Result<Boolean> checkDeptNameExists(
            @RequestParam String deptName,
            @RequestParam(required = false) String excludeId) {
        try {
            boolean exists = deptService.checkDeptNameExists(deptName, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查部门是否有子部门
     */
    @GetMapping("/{id}/has-child")
    public Result<Boolean> hasChildDept(@PathVariable String id) {
        try {
            boolean hasChild = deptService.hasChildDept(id);
            return Result.success(hasChild);
        } catch (Exception e) {
            return Result.error("检查失败: " + e.getMessage());
        }
    }

}
