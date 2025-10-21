package com.gjxt.inspection.util;

import com.gjxt.inspection.entity.SysUser;
import com.gjxt.inspection.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户上下文工具类
 * 用于获取当前登录用户信息
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Component
public class UserContextUtil {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ISysUserService userService;

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    public String getCurrentUserId() {
        String token = getCurrentToken();
        if (token != null) {
            try {
                return jwtUtil.getUserIdFromToken(token);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取当前登录用户名
     *
     * @return 用户名
     */
    public String getCurrentUsername() {
        String token = getCurrentToken();
        if (token != null) {
            try {
                return jwtUtil.getUsernameFromToken(token);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取当前登录用户角色
     *
     * @return 用户角色
     */
    public String getCurrentUserRole() {
        String token = getCurrentToken();
        if (token != null) {
            try {
                return jwtUtil.getRoleFromToken(token);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取当前登录用户完整信息
     *
     * @return 用户信息
     */
    public SysUser getCurrentUser() {
        String username = getCurrentUsername();
        if (username != null) {
            try {
                return userService.getUserByUsername(username);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取当前登录用户的部门ID
     *
     * @return 部门ID
     */
    public String getCurrentUserDeptId() {
        SysUser user = getCurrentUser();
        return user != null ? user.getDeptId() : null;
    }

    /**
     * 获取当前登录用户的部门名称
     *
     * @return 部门名称
     */
    public String getCurrentUserDepartment() {
        SysUser user = getCurrentUser();
        return user != null ? user.getDepartment() : null;
    }

    /**
     * 从请求头中获取JWT Token
     *
     * @return JWT Token
     */
    private String getCurrentToken() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String authHeader = request.getHeader(jwtUtil.getHeader());
                return jwtUtil.getTokenFromHeader(authHeader);
            }
        } catch (Exception e) {
            // 忽略异常，返回null
        }
        return null;
    }

    /**
     * 检查当前用户是否有权限访问指定部门的数据
     *
     * @param targetDeptId 目标部门ID
     * @return 是否有权限
     */
    public boolean hasAccessToDepartment(String targetDeptId) {
        String currentUserRole = getCurrentUserRole();
        String currentUserDeptId = getCurrentUserDeptId();
        
        // 管理员可以访问所有部门数据
        if ("admin".equals(currentUserRole)) {
            return true;
        }
        
        // 普通用户只能访问自己部门的数据
        return currentUserDeptId != null && currentUserDeptId.equals(targetDeptId);
    }

    /**
     * 检查当前用户是否为管理员
     *
     * @return 是否为管理员
     */
    public boolean isAdmin() {
        String role = getCurrentUserRole();
        return "admin".equals(role);
    }
}