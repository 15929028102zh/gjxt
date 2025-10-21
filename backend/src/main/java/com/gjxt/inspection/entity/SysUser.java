package com.gjxt.inspection.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户实体类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@TableName("sys_user")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名，用于登录
     */
    @TableField("username")
    private String username;

    /**
     * 用户密码，加密存储
     */
    @TableField("password")
    private String password;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 手机号码
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 所属部门
     */
    @TableField("department")
    private String department;

    /**
     * 部门ID
     */
    @TableField("dept_id")
    private String deptId;

    /**
     * 部门名称（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String deptName;

    /**
     * 职位
     */
    @TableField("position")
    private String position;

    /**
     * 用户角色（inspector:巡检员, reviewer:复查员, admin:管理员）
     */
    @TableField("role")
    private String role;

    /**
     * 状态（0:禁用, 1:启用）
     */
    @TableField("status")
    private Integer status;

    /**
     * 用户头像URL
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 周检操作权限（0:允许编辑删除, 1:禁用编辑删除）
     */
    @TableField("weekly_permission")
    private Integer weeklyPermission;

    /**
     * 月检操作权限（0:允许编辑删除, 1:禁用编辑删除）
     */
    @TableField("monthly_permission")
    private Integer monthlyPermission;

    /**
     * 是否为管理员（0:否, 1:是）
     */
    @TableField("is_admin")
    private Integer isAdmin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * 获取昵称（返回真实姓名）
     * 用于前端显示兼容
     */
    public String getNickname() {
        return this.realName;
    }

    public void setNickname(String nickname) {
        this.realName = nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getWeeklyPermission() {
        return weeklyPermission;
    }

    public void setWeeklyPermission(Integer weeklyPermission) {
        this.weeklyPermission = weeklyPermission;
    }

    public Integer getMonthlyPermission() {
        return monthlyPermission;
    }

    public void setMonthlyPermission(Integer monthlyPermission) {
        this.monthlyPermission = monthlyPermission;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }
}