package com.gjxt.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.entity.SysUser;

/**
 * 用户服务接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return JWT Token
     */
    String login(String username, String password);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser getUserByUsername(String username);

    /**
     * 分页查询用户列表
     *
     * @param current 当前页
     * @param size 每页大小
     * @param username 用户名（模糊查询）
     * @param realName 真实姓名（模糊查询）
     * @param department 部门
     * @param deptId 部门ID
     * @param role 角色
     * @param status 状态
     * @return 用户分页列表
     */
    PageResult<SysUser> getUserPage(Long current, Long size, String username, String realName, 
                                   String department, Long deptId, String role, Integer status);

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 是否成功
     */
    boolean addUser(SysUser user);

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateUser(SysUser user);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(String id);

    /**
     * 批量删除用户
     *
     * @param ids 用户ID列表
     * @return 是否成功
     */
    boolean batchDeleteUsers(java.util.List<String> ids);

    /**
     * 重置用户密码
     *
     * @param id 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean resetPassword(String id, String newPassword);

    /**
     * 修改用户密码
     *
     * @param id 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean changePassword(String id, String oldPassword, String newPassword);

    /**
     * 启用/禁用用户
     *
     * @param id 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateUserStatus(String id, Integer status);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean checkUsernameExists(String username, String excludeId);

    /**
     * 根据部门获取用户列表
     *
     * @param department 部门
     * @return 用户列表
     */
    java.util.List<SysUser> getUsersByDepartment(String department);

    /**
     * 根据部门ID获取用户列表
     *
     * @param deptId 部门ID
     * @return 用户列表
     */
    java.util.List<SysUser> getUsersByDeptId(Long deptId);

    /**
     * 更新用户头像URL
     *
     * @param id 用户ID
     * @param imageUrl 头像URL
     * @return 是否成功
     */
    boolean updateImageUrl(String id, String imageUrl);

    /**
     * 根据用户名更新用户头像URL
     *
     * @param username 用户名
     * @param imageUrl 头像URL
     * @return 是否成功
     */
    boolean updateImageUrlByUsername(String username, String imageUrl);

}