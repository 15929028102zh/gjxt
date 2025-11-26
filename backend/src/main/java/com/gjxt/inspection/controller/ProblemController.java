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
 * 问题类型相关接口
 */
@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private IGjbzDataService gjbzDataService;

    /**
     * 获取所有问题类型列表
     */
    @GetMapping("/list")
    public Result<List<String>> getAllProblems() {
        try {
            List<String> list = gjbzDataService.getIssueOptions();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("获取问题类型列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据责任单位获取问题类型列表
     * 路径：/problem/responsibility/{responsibilityId}
     * 前端传入的 responsibilityId 实际为责任单位名称字符串
     */
    @GetMapping("/responsibility/{responsibilityId}")
    public Result<List<String>> getProblemsByResponsibility(@PathVariable("responsibilityId") String responsibilityId) {
        try {
            List<String> list = gjbzDataService.getIssuesByResponsibility(responsibilityId);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("获取问题类型列表失败: " + e.getMessage());
        }
    }
}