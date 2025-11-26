package com.gjxt.inspection.controller;

import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.service.IGjbzDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 责任单位相关接口
 */
@RestController
@RequestMapping("/responsibility")
public class ResponsibilityController {

    @Autowired
    private IGjbzDataService gjbzDataService;

    /**
     * 获取所有责任单位列表
     */
    @GetMapping("/list")
    public Result<List<String>> getAllResponsibilities() {
        try {
            List<String> list = gjbzDataService.getResponsibilityOptions();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("获取责任单位列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据模块获取责任单位列表
     * 路径：/responsibility/module/{moduleId}
     * 前端传入的 moduleId 实际为模块名称字符串
     */
    @GetMapping("/module/{moduleId}")
    public Result<List<String>> getResponsibilitiesByModule(@PathVariable("moduleId") String moduleId) {
        try {
            List<String> list = gjbzDataService.getResponsibilitiesByModule(moduleId);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("获取责任单位列表失败: " + e.getMessage());
        }
    }
}