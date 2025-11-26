package com.gjxt.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.entity.GjbzData;

import java.util.List;
import java.util.Map;

/**
 * 工程标准数据服务接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
public interface IGjbzDataService extends IService<GjbzData> {

    /**
     * 分页查询工程标准数据
     *
     * @param current 当前页
     * @param size    页大小
     * @param type    类型
     * @return 分页结果
     */
    PageResult<GjbzData> getGjbzDataPage(Long current, Long size, String type);

    /**
     * 根据类型查询数据列表
     *
     * @param type 类型
     * @return 数据列表
     */
    List<GjbzData> getDataByType(String type);

    /**
     * 根据专业信息查询岗检标准项
     *
     * @param profession 专业信息
     * @return 岗检标准项列表
     */
    List<GjbzData> getDataByProfession(String profession);

    /**
     * 获取所有下拉框选项数据
     *
     * @return 选项数据Map
     */
    Map<String, List<String>> getAllOptions();

    /**
     * 获取模块选项
     *
     * @return 模块列表
     */
    List<String> getModuleOptions();

    /**
     * 根据类型获取模块选项
     */
    List<String> getModulesByType(String type);

    /**
     * 获取责任单位选项
     *
     * @return 责任单位列表
     */
    List<String> getResponsibilityOptions();

    /**
     * 根据模块筛选责任单位选项
     *
     * @param module 模块名称
     * @return 责任单位列表
     */
    List<String> getResponsibilitiesByModule(String module);

    /**
     * 获取问题类型选项
     *
     * @return 问题类型列表
     */
    List<String> getIssueOptions();

    /**
     * 根据责任单位筛选问题类型选项
     *
     * @param responsibility 责任单位名称
     * @return 问题类型列表
     */
    List<String> getIssuesByResponsibility(String responsibility);

    /**
     * 获取问题类型选项（包含质量等级）
     *
     * @return 问题类型数据列表
     */
    List<GjbzData> getIssueOptionsWithQuality();

    /**
     * 获取质量等级选项
     *
     * @return 质量等级列表
     */
    List<String> getQualityOptions();

    /**
     * 获取类型选项
     *
     * @return 类型列表
     */
    List<String> getTypeOptions();

    /**
     * 添加工程标准数据
     *
     * @param gjbzData 工程标准数据
     * @return 是否成功
     */
    boolean addGjbzData(GjbzData gjbzData);

    /**
     * 更新工程标准数据
     *
     * @param gjbzData 工程标准数据
     * @return 是否成功
     */
    boolean updateGjbzData(GjbzData gjbzData);

    /**
     * 删除工程标准数据
     *
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteGjbzData(Integer id);

    /**
     * 批量删除工程标准数据
     *
     * @param ids 主键ID列表
     * @return 是否成功
     */
    boolean batchDeleteGjbzData(List<Integer> ids);
}