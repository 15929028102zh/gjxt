package com.gjxt.inspection.controller;

import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.entity.GjbzData;
import com.gjxt.inspection.entity.SysDept;
import com.gjxt.inspection.service.IGjbzDataService;
import com.gjxt.inspection.service.ISysDeptService;
import com.gjxt.inspection.util.StandardGroupResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 工程标准数据控制器
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/gjbz-data")
public class GjbzDataController {

    @Autowired
    private IGjbzDataService gjbzDataService;

    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 解析本次级联查询应使用的标准组。
     *
     * <p>优先级：显式传入的 standardGroup &gt; 根据 departmentId 查部门名称自动判定 &gt; null(不限制)。
     * 受检单位→标准组映射规则见 {@link StandardGroupResolver}：
     * 公用工程/储运/运行保障→第二组(2)；乙烯/全密/高密/聚乙烯→第一组(1)；其他部门→null(全部)。
     *
     * @param departmentId  受检单位(部门)ID，可为空
     * @param standardGroup 直接指定的标准组，可为空
     * @return 标准组(1/2)，null 表示不限制
     */
    private Integer resolveStandardGroup(String departmentId, Integer standardGroup) {
        if (standardGroup != null) {
            log.info("[标准组解析] 使用显式 standardGroup={}", standardGroup);
            return standardGroup;
        }
        if (departmentId == null || departmentId.trim().isEmpty()) {
            log.info("[标准组解析] departmentId 为空 → 不过滤(返回全部标准)。若前端已选受检单位却出现此日志, 说明 departmentId 未传到后端");
            return null;
        }
        try {
            SysDept dept = sysDeptService.getById(departmentId.trim());
            if (dept == null) {
                log.warn("[标准组解析] 未查到部门: departmentId={} → 不过滤(返回全部)。请核对该 ID 是否存在于 sys_dept 且 deleted=0", departmentId);
                return null;
            }
            Integer group = StandardGroupResolver.resolveByDeptName(dept.getDeptName());
            log.info("[标准组解析] departmentId={}, deptName='{}' → group={}", departmentId, dept.getDeptName(), group);
            if (group == null) {
                log.warn("[标准组解析] deptName='{}' 未命中任何关键字(公用工程/储运/运行保障→2; 乙烯/全密/高密/聚乙烯→1) → 不过滤(返回全部)", dept.getDeptName());
            }
            return group;
        } catch (Exception e) {
            // 部门查询异常时不施加过滤，避免误伤(返回全部)
            log.error("[标准组解析] 异常: departmentId=" + departmentId + " → 不过滤(返回全部)", e);
            return null;
        }
    }

    /**
     * 分页查询工程标准数据列表
     */
    @GetMapping("/page")
    public Result<PageResult<GjbzData>> getGjbzDataPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String type) {
        try {
            PageResult<GjbzData> result = gjbzDataService.getGjbzDataPage(current, size, type);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据类型查询数据列表
     */
    @GetMapping("/type/{type}")
    public Result<List<GjbzData>> getDataByType(@PathVariable String type) {
        try {
            List<GjbzData> result = gjbzDataService.getDataByType(type);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取所有下拉框选项数据
     */
    @GetMapping("/options")
    public Result<Map<String, List<String>>> getAllOptions() {
        try {
            Map<String, List<String>> result = gjbzDataService.getAllOptions();
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取模块选项
     */
    @GetMapping("/options/modules")
    public Result<List<String>> getModuleOptions() {
        try {
            List<String> result = gjbzDataService.getModuleOptions();
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据类型获取模块选项
     * 可选 departmentId/standardGroup：按受检单位所属标准组过滤
     */
    @GetMapping("/modules/by-type/{type}")
    public Result<List<String>> getModulesByType(
            @PathVariable String type,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) Integer standardGroup) {
        try {
            Integer group = resolveStandardGroup(departmentId, standardGroup);
            List<String> result = gjbzDataService.getModulesByType(type, group);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取责任单位选项
     */
    @GetMapping("/options/responsibilities")
    public Result<List<String>> getResponsibilityOptions() {
        try {
            List<String> result = gjbzDataService.getResponsibilityOptions();
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据模块获取责任单位选项
     * 可选 departmentId/standardGroup：按受检单位所属标准组过滤
     * 可选 type：按专业类别过滤，避免同名模块跨专业类别串味
     */
    @GetMapping("/options/responsibilities/by-module/{module}")
    public Result<List<String>> getResponsibilitiesByModule(
            @PathVariable String module,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) Integer standardGroup,
            @RequestParam(required = false) String type) {
        try {
            Integer group = resolveStandardGroup(departmentId, standardGroup);
            List<String> result = gjbzDataService.getResponsibilitiesByModule(module, group, type);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据模块获取责任单位选项（query 参数传值版）
     * 路径参数版遇到名称含 "/" 时会被拆成多级路径，故另提供 query 版本。
     */
    @GetMapping("/options/responsibilities/by-module")
    public Result<List<String>> getResponsibilitiesByModuleQuery(
            @RequestParam String module,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) Integer standardGroup,
            @RequestParam(required = false) String type) {
        try {
            Integer group = resolveStandardGroup(departmentId, standardGroup);
            List<String> result = gjbzDataService.getResponsibilitiesByModule(module, group, type);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取问题类型选项
     */
    @GetMapping("/options/issues")
    public Result<List<String>> getIssueOptions() {
        try {
            List<String> result = gjbzDataService.getIssueOptions();
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据责任单位获取问题类型选项
     * 可选 departmentId/standardGroup：按受检单位所属标准组过滤
     * 可选 type/module：按专业类别+模块过滤，避免责任要素同名跨模块串味
     */
    @GetMapping("/options/issues/by-responsibility/{responsibility}")
    public Result<List<String>> getIssuesByResponsibility(
            @PathVariable String responsibility,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) Integer standardGroup,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String module) {
        try {
            Integer group = resolveStandardGroup(departmentId, standardGroup);
            List<String> result = gjbzDataService.getIssuesByResponsibility(responsibility, group, type, module);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据责任单位获取问题类型选项（query 参数传值版）
     * 路径参数版遇到名称含 "/" 时会被拆成多级路径（如 盲板抽/堵作业要求），故另提供 query 版本。
     */
    @GetMapping("/options/issues/by-responsibility")
    public Result<List<String>> getIssuesByResponsibilityQuery(
            @RequestParam String responsibility,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) Integer standardGroup,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String module) {
        try {
            Integer group = resolveStandardGroup(departmentId, standardGroup);
            List<String> result = gjbzDataService.getIssuesByResponsibility(responsibility, group, type, module);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取问题类型选项（包含质量等级）
     * 可选 departmentId/standardGroup：按受检单位所属标准组过滤
     */
    @GetMapping("/options/issues-with-quality")
    public Result<List<GjbzData>> getIssueOptionsWithQuality(
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) Integer standardGroup) {
        try {
            Integer group = resolveStandardGroup(departmentId, standardGroup);
            List<GjbzData> result = gjbzDataService.getIssueOptionsWithQuality(group);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取质量等级选项
     */
    @GetMapping("/options/qualities")
    public Result<List<String>> getQualityOptions() {
        try {
            List<String> result = gjbzDataService.getQualityOptions();
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取类型选项
     * 可选 departmentId/standardGroup：按受检单位所属标准组过滤
     */
    @GetMapping("/options/types")
    public Result<List<String>> getTypeOptions(
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) Integer standardGroup) {
        try {
            Integer group = resolveStandardGroup(departmentId, standardGroup);
            List<String> result = gjbzDataService.getTypeOptions(group);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据专业信息查询岗检标准项
     */
    @GetMapping("/by-profession/{profession}")
    public Result<List<GjbzData>> getGjbzDataByProfession(@PathVariable String profession) {
        try {
            List<GjbzData> result = gjbzDataService.getDataByProfession(profession);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询工程标准数据
     */
    @GetMapping("/{id}")
    public Result<GjbzData> getGjbzDataById(@PathVariable Integer id) {
        try {
            GjbzData result = gjbzDataService.getById(id);
            if (result != null) {
                return Result.success(result);
            } else {
                return Result.error("数据不存在");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 添加工程标准数据
     */
    @PostMapping
    public Result<Void> addGjbzData(@Valid @RequestBody GjbzData gjbzData) {
        try {
            boolean success = gjbzDataService.addGjbzData(gjbzData);
            if (success) {
                return Result.success();
            } else {
                return Result.error("添加失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新工程标准数据
     */
    @PutMapping
    public Result<Void> updateGjbzData(@Valid @RequestBody GjbzData gjbzData) {
        try {
            boolean success = gjbzDataService.updateGjbzData(gjbzData);
            if (success) {
                return Result.success();
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除工程标准数据
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteGjbzData(@PathVariable Integer id) {
        try {
            boolean success = gjbzDataService.deleteGjbzData(id);
            if (success) {
                return Result.success();
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除工程标准数据
     */
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteGjbzData(@RequestBody List<Integer> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return Result.error("请选择要删除的数据");
            }
            boolean success = gjbzDataService.batchDeleteGjbzData(ids);
            if (success) {
                return Result.success();
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}