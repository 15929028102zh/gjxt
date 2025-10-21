package com.gjxt.inspection.controller;

import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.entity.SysUser;
import com.gjxt.inspection.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制�? *
 * @author gjxt
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody Map<String, String> loginForm) {
        try {
            String username = loginForm.get("username");
            String password = loginForm.get("password");
            String token = userService.login(username, password);
            return Result.success("登录成功", token);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<SysUser> getUserInfo(@RequestParam String username) {
        try {
            SysUser user = userService.getUserByUsername(username);
            if (user != null) {
                // 清除密码信息
                user.setPassword(null);
                return Result.success(user);
            }
            return Result.error("用户不存在");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        try {
            // 这里可以添加清除session、token等逻辑
            // 目前前端主要通过清除本地token来实现退出
            return Result.success("退出登录成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping("/page")
    public Result<PageResult<SysUser>> getUserPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String deptId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Integer status) {
        try {
            PageResult<SysUser> result = userService.getUserPage(current, size, username, realName, department, deptId, role, status);
            // 清除密码信息
            if (result.getRecords() != null) {
                result.getRecords().forEach(user -> user.setPassword(null));
            }
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询用户详情
     */
    @GetMapping("/{id}")
    public Result<SysUser> getUserById(@PathVariable String id) {
        try {
            SysUser user = userService.getById(id);
            if (user != null) {
                // 清除密码信息
                user.setPassword(null);
                return Result.success(user);
            }
            return Result.error("用户不存在");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Result<Void> addUser(@Valid @RequestBody SysUser user) {
        try {
            boolean success = userService.addUser(user);
            return success ? Result.success("新增用户成功") : Result.error("新增用户失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @PutMapping
    public Result<Void> updateUser(@Valid @RequestBody SysUser user) {
        try {
            boolean success = userService.updateUser(user);
            return success ? Result.success("更新用户成功") : Result.error("更新用户失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable String id) {
        try {
            boolean success = userService.deleteUser(id);
            return success ? Result.success("删除用户成功") : Result.error("删除用户失败");
        } catch (Exception e) {            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteUsers(@RequestBody List<String> ids) {
        try {
            boolean success = userService.batchDeleteUsers(ids);
            return success ? Result.success("批量删除用户成功") : Result.error("批量删除用户失败");
        } catch (Exception e) {            return Result.error(e.getMessage());
        }
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable String id, @RequestBody(required = false) Map<String, String> params) {
        try {
            String newPassword = params != null ? params.get("newPassword") : null;
            boolean success = userService.resetPassword(id, newPassword);
            return success ? Result.success("重置密码成功") : Result.error("重置密码失败");
        } catch (Exception e) {            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改用户密码
     */
    @PutMapping("/{id}/change-password")
    public Result<Void> changePassword(@PathVariable String id, @RequestBody Map<String, String> params) {
        try {
            String oldPassword = params.get("oldPassword");
            String newPassword = params.get("newPassword");
            boolean success = userService.changePassword(id, oldPassword, newPassword);
            return success ? Result.success("修改密码成功") : Result.error("修改密码失败");
        } catch (Exception e) {            return Result.error(e.getMessage());
        }
    }

    /**
     * 启用/禁用用户
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable String id, @RequestBody Map<String, Integer> params) {
        try {
            Integer status = params.get("status");
            boolean success = userService.updateUserStatus(id, status);
            String message = status == 1 ? "启用用户成功" : "禁用用户成功";
            return success ? Result.success(message) : Result.error("操作失败");
        } catch (Exception e) {            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    public Result<Boolean> checkUsernameExists(
            @RequestParam String username,
            @RequestParam(required = false) String excludeId) {
        try {
            boolean exists = userService.checkUsernameExists(username, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据部门名称查询用户列表
     */
    @GetMapping("/by-department")
    public Result<List<SysUser>> getUsersByDepartment(@RequestParam String deptId) {
        try {
            List<SysUser> users = userService.getUsersByDeptId(deptId);
            // 清除密码信息
            if (users != null) {
                users.forEach(user -> user.setPassword(null));
            }
            return Result.success(users);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    /**
     * 更新用户头像URL
     */
    @PutMapping("/{username}/update-avatar")
    public Result<Void> updateImageUrl(@PathVariable String username, @RequestBody Map<String, String> params) {
        try {
            String imageUrl = params.get("imageUrl");
            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                return Result.error("头像URL不能为空");
            }
            boolean success = userService.updateImageUrlByUsername(username, imageUrl);
            return success ? Result.success("更新头像成功") : Result.error("更新头像失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户权限
     */
    @PutMapping("/{id}/permissions")
    public Result<Void> updateUserPermissions(@PathVariable String id, @RequestBody Map<String, Integer> params) {
        try {
            Integer weeklyPermission = params.get("weeklyPermission");
            Integer monthlyPermission = params.get("monthlyPermission");
            
            // 参数验证
            if (weeklyPermission != null && (weeklyPermission < 0 || weeklyPermission > 1)) {
                return Result.error("周检权限值必须为0或1");
            }
            if (monthlyPermission != null && (monthlyPermission < 0 || monthlyPermission > 1)) {
                return Result.error("月检权限值必须为0或1");
            }
            
            boolean success = userService.updateUserPermissions(id, weeklyPermission, monthlyPermission);
            return success ? Result.success("更新用户权限成功") : Result.error("更新用户权限失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户权限信息
     */
    @GetMapping("/{userId}/permissions")
    public Result<SysUser> getUserPermissions(@PathVariable String userId) {
        try {
            SysUser userPermissions = userService.getUserPermissions(userId);
            if (userPermissions != null) {
                return Result.success(userPermissions);
            }
            return Result.error("用户不存在");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


}