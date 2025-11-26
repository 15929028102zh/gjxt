package com.gjxt.inspection.controller;

import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.entity.GjbzData;
import com.gjxt.inspection.service.IGjbzDataService;
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
@RestController
@RequestMapping("/gjbz-data")
public class GjbzDataController {

    @Autowired
    private IGjbzDataService gjbzDataService;

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
     */
    @GetMapping("/modules/by-type/{type}")
    public Result<List<String>> getModulesByType(@PathVariable String type) {
        try {
            List<String> result = gjbzDataService.getModulesByType(type);
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
     */
    @GetMapping("/options/responsibilities/by-module/{module}")
    public Result<List<String>> getResponsibilitiesByModule(@PathVariable String module) {
        try {
            List<String> result = gjbzDataService.getResponsibilitiesByModule(module);
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
     */
    @GetMapping("/options/issues/by-responsibility/{responsibility}")
    public Result<List<String>> getIssuesByResponsibility(@PathVariable String responsibility) {
        try {
            List<String> result = gjbzDataService.getIssuesByResponsibility(responsibility);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取问题类型选项（包含质量等级）
     */
    @GetMapping("/options/issues-with-quality")
    public Result<List<GjbzData>> getIssueOptionsWithQuality() {
        try {
            List<GjbzData> result = gjbzDataService.getIssueOptionsWithQuality();
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
     */
    @GetMapping("/options/types")
    public Result<List<String>> getTypeOptions() {
        try {
            List<String> result = gjbzDataService.getTypeOptions();
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