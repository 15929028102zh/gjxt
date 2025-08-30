package com.gjxt.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjxt.inspection.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户Mapper接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @param excludeId 排除的用户ID
     * @return 存在数量
     */
    int checkUsernameExists(@Param("username") String username, @Param("excludeId") String excludeId);

    /**
     * 查询用户列表（包含部门名称）
     *
     * @return 用户列表
     */
    java.util.List<SysUser> selectUserListWithDept();

    /**
     * 分页查询用户列表（包含部门名称）
     */
    IPage<SysUser> selectUserPageWithDept(Page<SysUser> page, 
                                          @Param("username") String username,
                                          @Param("realName") String realName,
                                          @Param("department") String department,
                                          @Param("deptId") Long deptId,
                                          @Param("role") String role,
                                          @Param("status") Integer status);

    /**
     * 根据用户名更新头像URL
     *
     * @param username 用户名
     * @param imageUrl 头像URL
     * @param updateTime 更新时间
     * @return 更新行数
     */
    @Update("UPDATE sys_user SET image_url = #{imageUrl}, update_time = #{updateTime} WHERE username = #{username} AND deleted = 0")
    int updateImageUrlByUsername(@Param("username") String username, 
                                @Param("imageUrl") String imageUrl, 
                                @Param("updateTime") java.time.LocalDateTime updateTime);


}