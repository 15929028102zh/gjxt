package com.gjxt.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.common.ResultCode;
import com.gjxt.inspection.entity.SysUser;
import com.gjxt.inspection.mapper.SysUserMapper;
import com.gjxt.inspection.service.ISysUserService;
import com.gjxt.inspection.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务实现类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String login(String username, String password) {
        // 查询用户
        SysUser user = baseMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException(ResultCode.USER_NOT_FOUND.getMessage());
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new RuntimeException(ResultCode.USER_DISABLED.getMessage());
        }

        // 验证密码（明文比较）
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException(ResultCode.LOGIN_ERROR.getMessage());
        }

        // 生成JWT Token
        return jwtUtil.generateToken(Long.valueOf(user.getId()), user.getUsername(), user.getRole());
    }

    @Override
    public SysUser getUserByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public PageResult<SysUser> getUserPage(Long current, Long size, String username, String realName, 
                                          String department, String deptId, String role, Integer status) {
        // 直接使用关联查询的分页方法
        IPage<SysUser> result = baseMapper.selectUserPageWithDept(
            new Page<>(current, size), username, realName, department, deptId, role, status);
        
        return PageResult.build(result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(SysUser user) {
        // 检查用户名是否存在
        if (checkUsernameExists(user.getUsername(), null)) {
            throw new RuntimeException(ResultCode.USERNAME_EXISTS.getMessage());
        }

        // 设置明文密码
        if (StringUtils.isBlank(user.getPassword())) {
            // 默认密码
            user.setPassword("123456");
        }

        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(1);
        }

        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        return this.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        // 检查用户名是否存在
        if (checkUsernameExists(user.getUsername(), user.getId())) {
            throw new RuntimeException(ResultCode.USERNAME_EXISTS.getMessage());
        }

        // 不更新密码字段
        user.setPassword(null);
        
        // 设置更新时间
        user.setUpdateTime(LocalDateTime.now());

        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(String id) {
        // 检查用户是否存在
        SysUser user = this.getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }
        
        // 逻辑删除
        user.setDeleted(1);
        user.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteUsers(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        for (String id : ids) {
            if (!deleteUser(id)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(String id, String newPassword) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(StringUtils.isNotBlank(newPassword) ? newPassword : "123456"));
        user.setUpdateTime(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(String id, String oldPassword, String newPassword) {
        SysUser user = this.getById(id);
        if (user == null) {
            throw new RuntimeException(ResultCode.USER_NOT_FOUND.getMessage());
        }

        // 验证旧密码（明文比较）
        if (!oldPassword.equals(user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        // 更新新密码（明文存储）
        user.setPassword(newPassword);
        user.setUpdateTime(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserStatus(String id, Integer status) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setStatus(status);
        return this.updateById(user);
    }

    @Override
    public boolean checkUsernameExists(String username, String excludeId) {
        return baseMapper.checkUsernameExists(username, excludeId) > 0;
    }

    @Override
    public List<SysUser> getUsersByDepartment(String department) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getDepartment, department)
                   .eq(SysUser::getDeleted, 0)
                   .orderByAsc(SysUser::getRealName);
        return this.list(queryWrapper);
    }

    @Override
    public List<SysUser> getUsersByDeptId(String deptId) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getDeptId, deptId)
                   .eq(SysUser::getDeleted, 0)
                   .orderByAsc(SysUser::getRealName);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateImageUrl(String id, String imageUrl) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setImageUrl(imageUrl);
        user.setUpdateTime(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateImageUrlByUsername(String username, String imageUrl) {
        // 直接根据用户名更新头像URL
        int result = baseMapper.updateImageUrlByUsername(username, imageUrl, LocalDateTime.now());
        return result > 0;
    }

}