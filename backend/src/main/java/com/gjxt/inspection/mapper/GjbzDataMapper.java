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
     * @return 责任单位列表
     */
    @Select("SELECT DISTINCT responsibility FROM gjbz_data WHERE moudle = #{module} AND responsibility IS NOT NULL AND responsibility != ''")
    List<String> selectResponsibilitiesByModule(@Param("module") String module);

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
     * @return 问题类型列表
     */
    @Select("SELECT DISTINCT issue FROM gjbz_data WHERE responsibility = #{responsibility} AND issue IS NOT NULL AND issue != ''")
    List<String> selectIssuesByResponsibility(@Param("responsibility") String responsibility);

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
}