package com.gjxt.inspection.controller;

import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.service.IGjbzDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模块控制器
 * 处理模块相关的API请求
 *
 * @author gjxt
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/module")
public class ModuleController {

    @Autowired
    private IGjbzDataService gjbzDataService;

    /**
     * 根据专业ID获取模块列表
     * 前端调用路径：/api/module/profession/{professionId}
     */
    @GetMapping("/profession/{professionId}")
    public Result<List<String>> getModulesByProfessionId(@PathVariable String professionId) {
        try {
            // 根据专业ID获取模块列表
            // 这里需要根据实际的业务逻辑来实现
            // 目前先返回所有模块选项，后续可以根据专业ID进行过滤
            List<String> modules = gjbzDataService.getModuleOptions();
            return Result.success(modules);
        } catch (Exception e) {
            return Result.error("获取模块列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有模块列表
     */
    @GetMapping("/list")
    public Result<List<String>> getAllModules() {
        try {
            List<String> modules = gjbzDataService.getModuleOptions();
            return Result.success(modules);
        } catch (Exception e) {
            return Result.error("获取模块列表失败: " + e.getMessage());
        }
    }
}