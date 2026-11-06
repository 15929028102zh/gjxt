package com.gjxt.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjxt.inspection.entity.GjbzData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 工程标准数据Mapper接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Mapper
public interface GjbzDataMapper extends BaseMapper<GjbzData> {

    /**
     * 根据类型查询数据列表
     *
     * @param type 类型
     * @return 数据列表
     */
    @Select("SELECT * FROM gjbz_data WHERE type = #{type}")
    List<GjbzData> selectByType(@Param("type") String type);

    /**
     * 获取所有模块数据
     *
     * @return 模块列表
     */
    @Select("SELECT DISTINCT moudle FROM gjbz_data WHERE moudle IS NOT NULL AND moudle != ''")
    List<String> selectAllModules();

    /**
     * 根据类型获取模块
     */
    @Select("SELECT DISTINCT moudle FROM gjbz_data WHERE type = #{type} AND moudle IS NOT NULL AND moudle != ''")
    List<String> selectModulesByType(@Param("type") String type);

    /**
     * 获取所有责任单位数据
     *
     * @return 责任单位列表
     */
    @Select("SELECT DISTINCT responsibility FROM gjbz_data WHERE responsibility IS NOT NULL AND responsibility != ''")
    List<String> selectAllResponsibilities();

    /**
     * 根据模块筛选责任单位数据
     *
     * @param module 模块名称
     * @param type   专业类别名称，为空时不按专业类别过滤
     * @return 责任单位列表
     */
    @Select("<script>" +
            "SELECT DISTINCT responsibility FROM gjbz_data WHERE moudle = #{module} AND responsibility IS NOT NULL AND responsibility != ''" +
            "<if test='type != null and type != \"\"'> AND type = #{type}</if>" +
            "</script>")
    List<String> selectResponsibilitiesByModule(@Param("module") String module, @Param("type") String type);

    /**
     * 获取所有问题类型数据
     *
     * @return 问题类型列表
     */
    @Select("SELECT DISTINCT issue FROM gjbz_data WHERE issue IS NOT NULL AND issue != ''")
    List<String> selectAllIssues();

    /**
     * 根据责任单位筛选问题类型数据
     *
     * @param responsibility 责任单位名称
     * @param type           专业类别名称，为空时不按专业类别过滤
     * @param module         模块名称，为空时不按模块过滤
     * @return 问题类型列表
     */
    @Select("<script>" +
            "SELECT DISTINCT issue FROM gjbz_data WHERE responsibility = #{responsibility} AND issue IS NOT NULL AND issue != ''" +
            "<if test='type != null and type != \"\"'> AND type = #{type}</if>" +
            "<if test='module != null and module != \"\"'> AND moudle = #{module}</if>" +
            "</script>")
    List<String> selectIssuesByResponsibility(@Param("responsibility") String responsibility, @Param("type") String type, @Param("module") String module);

    /**
     * 获取所有问题类型数据（包含质量等级）
     *
     * @return 问题类型数据列表
     */
    @Select("SELECT DISTINCT issue, quality FROM gjbz_data WHERE issue IS NOT NULL AND issue != '' AND quality IS NOT NULL AND quality != ''")
    List<GjbzData> selectAllIssuesWithQuality();

    /**
     * 获取所有质量等级数据
     *
     * @return 质量等级列表
     */
    @Select("SELECT DISTINCT quality FROM gjbz_data WHERE quality IS NOT NULL AND quality != ''")
    List<String> selectAllQualities();

    /**
     * 获取所有类型数据
     *
     * @return 类型列表
     */
    @Select("SELECT DISTINCT type FROM gjbz_data WHERE type IS NOT NULL AND type != ''")
    List<String> selectAllTypes();

    // ==================== 按标准组(standard_group)过滤的级联查询 ====================
    // 分组过滤规则: group 为 null 时不限制(返回全部);
    //   group 为 1/2 时返回该组 + 通用(standard_group IS NULL OR = 0)的数据。

    /**
     * 按标准组获取专业类别(type)选项
     *
     * @param group 标准组(1/2), null=不限制
     * @return 类型列表
     */
    @Select("<script>" +
            "SELECT DISTINCT type FROM gjbz_data WHERE type IS NOT NULL AND type != ''" +
            "<if test='group != null'> AND (standard_group = #{group} OR standard_group IS NULL OR standard_group = 0)</if>" +
            "</script>")
    List<String> selectAllTypesByGroup(@Param("group") Integer group);

    /**
     * 按类型 + 标准组获取模块(moudle)选项
     *
     * @param type  类型
     * @param group 标准组(1/2), null=不限制
     * @return 模块列表
     */
    @Select("<script>" +
            "SELECT DISTINCT moudle FROM gjbz_data WHERE type = #{type} AND moudle IS NOT NULL AND moudle != ''" +
            "<if test='group != null'> AND (standard_group = #{group} OR standard_group IS NULL OR standard_group = 0)</if>" +
            "</script>")
    List<String> selectModulesByTypeAndGroup(@Param("type") String type, @Param("group") Integer group);

    /**
     * 按模块 + 标准组获取责任要素(responsibility)选项
     *
     * @param module 模块名称
     * @param group  标准组(1/2), null=不限制
     * @param type   专业类别名称，为空时不按专业类别过滤（兼容旧调用方）
     * @return 责任要素列表
     */
    @Select("<script>" +
            "SELECT DISTINCT responsibility FROM gjbz_data WHERE moudle = #{module} AND responsibility IS NOT NULL AND responsibility != ''" +
            "<if test='type != null and type != \"\"'> AND type = #{type}</if>" +
            "<if test='group != null'> AND (standard_group = #{group} OR standard_group IS NULL OR standard_group = 0)</if>" +
            "</script>")
    List<String> selectResponsibilitiesByModuleAndGroup(@Param("module") String module, @Param("group") Integer group, @Param("type") String type);

    /**
     * 按责任要素 + 标准组获取检查内容(issue)选项
     *
     * @param responsibility 责任要素名称
     * @param group          标准组(1/2), null=不限制
     * @param type           专业类别名称，为空时不按专业类别过滤（兼容旧调用方）
     * @param module         模块名称，为空时不按模块过滤（兼容旧调用方）
     * @return 检查内容列表
     */
    @Select("<script>" +
            "SELECT DISTINCT issue FROM gjbz_data WHERE responsibility = #{responsibility} AND issue IS NOT NULL AND issue != ''" +
            "<if test='type != null and type != \"\"'> AND type = #{type}</if>" +
            "<if test='module != null and module != \"\"'> AND moudle = #{module}</if>" +
            "<if test='group != null'> AND (standard_group = #{group} OR standard_group IS NULL OR standard_group = 0)</if>" +
            "</script>")
    List<String> selectIssuesByResponsibilityAndGroup(@Param("responsibility") String responsibility, @Param("group") Integer group, @Param("type") String type, @Param("module") String module);

    /**
     * 按标准组获取检查内容(issue)与性质界定(quality)映射
     *
     * @param group 标准组(1/2), null=不限制
     * @return issue/quality 数据列表
     */
    @Select("<script>" +
            "SELECT DISTINCT issue, quality FROM gjbz_data WHERE issue IS NOT NULL AND issue != '' AND quality IS NOT NULL AND quality != ''" +
            "<if test='group != null'> AND (standard_group = #{group} OR standard_group IS NULL OR standard_group = 0)</if>" +
            "</script>")
    List<GjbzData> selectAllIssuesWithQualityByGroup(@Param("group") Integer group);
}