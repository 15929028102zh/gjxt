package com.gjxt.inspection.controller;

import com.gjxt.inspection.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页控制器
 *
 * @author gjxt
 * @since 2024-01-01
 */
@RestController
public class IndexController {

    /**
     * 系统首页
     */
    @GetMapping("/")
    public Result<Map<String, Object>> index() {
        Map<String, Object> data = new HashMap<>();
        data.put("system", "石油工业园区设备巡检系统");
        data.put("version", "1.0.0");
        data.put("author", "gjxt");
        data.put("description", "基于Spring Boot + MyBatis + MySQL的设备巡检管理系统");
        data.put("apiDoc", "/swagger-ui.html");
        data.put("druidMonitor", "/druid");
        return Result.success("欢迎使用石油工业园区设备巡检系统", data);
    }

    /**
     * 系统健康检查
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("系统运行正常", "OK");
    }
}