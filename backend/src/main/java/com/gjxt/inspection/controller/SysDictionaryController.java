package com.gjxt.inspection.controller;

import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.entity.SysDictionary;
import com.gjxt.inspection.service.ISysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 字典管理控制器
 *
 * @author gjxt
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/dictionary")
public class SysDictionaryController {

    @Autowired
    private ISysDictionaryService dictionaryService;

    /**
     * 分页查询字典列表
     */
    @GetMapping("/page")
    public Result<PageResult<SysDictionary>> getDictionaryPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String dictType,
            @RequestParam(required = false) String dictLabel,
            @RequestParam(required = false) Integer status) {
        try {
            PageResult<SysDictionary> result = dictionaryService.getDictionaryPage(current, size, dictType, dictLabel, status);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据字典类型查询字典列表
     */
    @GetMapping("/type/{dictType}")
    public Result<List<SysDictionary>> getDictionaryByType(@PathVariable String dictType) {
        try {
            List<SysDictionary> list = dictionaryService.getDictionaryByType(dictType);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取所有字典数据（按类型分组）
     */
    @GetMapping("/all")
    public Result<Map<String, List<SysDictionary>>> getAllDictionaryData() {
        try {
            Map<String, List<SysDictionary>> result = dictionaryService.getAllDictionaryData();
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取所有字典类型
     */
    @GetMapping("/types")
    public Result<List<String>> getAllDictTypes() {
        try {
            List<String> types = dictionaryService.getAllDictTypes();
            return Result.success(types);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询字典详情
     */
    @GetMapping("/{id}")
    public Result<SysDictionary> getDictionaryById(@PathVariable String id) {
        try {
            SysDictionary dictionary = dictionaryService.getById(id);
            if (dictionary != null) {
                return Result.success(dictionary);
            }
            return Result.error("字典不存在");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 新增字典
     */
    @PostMapping
    public Result<Void> addDictionary(@Valid @RequestBody SysDictionary dictionary) {
        try {
            boolean success = dictionaryService.addDictionary(dictionary);
            return success ? Result.success("新增字典成功") : Result.error("新增字典失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新字典
     */
    @PutMapping
    public Result<Void> updateDictionary(@Valid @RequestBody SysDictionary dictionary) {
        try {
            boolean success = dictionaryService.updateDictionary(dictionary);
            return success ? Result.success("更新字典成功") : Result.error("更新字典失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除字典
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDictionary(@PathVariable String id) {
        System.out.println("=== 删除字典控制器被调用，ID: " + id + " ===");
        try {
            boolean success = dictionaryService.deleteDictionary(id);
            System.out.println("=== 删除操作结果: " + success + " ===");
            return success ? Result.success("删除字典成功") : Result.error("删除字典失败");
        } catch (Exception e) {
            System.out.println("=== 删除操作异常: " + e.getMessage() + " ===");
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除字典
     */
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteDictionaries(@RequestBody List<String> ids) {
        try {
            boolean success = dictionaryService.batchDeleteDictionaries(ids);
            return success ? Result.success("批量删除字典成功") : Result.error("批量删除字典失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查字典标签是否存在
     */
    @GetMapping("/check-label")
    public Result<Boolean> checkDictLabelExists(
            @RequestParam String dictType,
            @RequestParam String dictLabel,
            @RequestParam(required = false) String excludeId) {
        try {
            boolean exists = dictionaryService.checkDictLabelExists(dictType, dictLabel, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查字典键值是否存在
     */
    @GetMapping("/check-value")
    public Result<Boolean> checkDictValueExists(
            @RequestParam String dictType,
            @RequestParam String dictValue,
            @RequestParam(required = false) String excludeId) {
        try {
            boolean exists = dictionaryService.checkDictValueExists(dictType, dictValue, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}