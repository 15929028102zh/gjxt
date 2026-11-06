package com.gjxt.inspection.util;

/**
 * 岗检标准分组解析工具。
 *
 * <p>月检受检单位（运行部门）对应两套岗检标准：
 * <ul>
 *   <li>第一组（生产/炼化）：乙烯运行部、全密度聚乙烯运行部、高密度聚乙烯运行部</li>
 *   <li>第二组（辅助/公用）：公用工程部、储运部、运行保障部</li>
 *   <li>其他部门：不限制，加载全部标准</li>
 * </ul>
 *
 * <p>解析基于部门名称关键字匹配（去除空白后），以兼容部门全称/简称
 * （如“全密度聚乙烯运行部”与“全密运行部”）。若后续部门改名导致关键字失效，
 * 只需在此处补充关键字即可，无需改动调用方。
 *
 * @author gjxt
 * @since 2026-09-09
 */
public class StandardGroupResolver {

    /** 第一组（生产/炼化） */
    public static final int GROUP_PRODUCTION = 1;

    /** 第二组（辅助/公用） */
    public static final int GROUP_AUXILIARY = 2;

    /**
     * 第二组（辅助）部门名称关键字。
     * 注意：需在第一组之前匹配，避免“运行保障部”被“运行”类关键字误判。
     */
    private static final String[] GROUP2_KEYWORDS = {"公用工程", "储运", "运行保障"};

    /**
     * 第一组（生产）部门名称关键字。
     * 覆盖全称与简称：乙烯运行部 / 全密度聚乙烯运行部 / 全密运行部 / 高密度聚乙烯运行部 / 高密运行部。
     */
    private static final String[] GROUP1_KEYWORDS = {"乙烯", "全密", "高密", "聚乙烯"};

    private StandardGroupResolver() {
    }

    /**
     * 根据受检单位名称解析其应加载的标准组。
     *
     * @param deptName 部门（受检单位）名称，可为 null
     * @return 1=第一组（生产），2=第二组（辅助），null=不限制（加载全部）
     */
    public static Integer resolveByDeptName(String deptName) {
        if (deptName == null) {
            return null;
        }
        // 去除所有空白字符（含全角空格），并 trim，避免名称中夹杂空格导致匹配失败
        String normalized = deptName.replace("\u3000", " ").replaceAll("\\s+", "").trim();
        if (normalized.isEmpty()) {
            return null;
        }
        // 先匹配第二组（辅助），关键字更具体，避免“运行保障”被“乙烯运行”等误伤
        for (String kw : GROUP2_KEYWORDS) {
            if (normalized.contains(kw)) {
                return GROUP_AUXILIARY;
            }
        }
        // 再匹配第一组（生产）
        for (String kw : GROUP1_KEYWORDS) {
            if (normalized.contains(kw)) {
                return GROUP_PRODUCTION;
            }
        }
        // 其他部门：不限制
        return null;
    }
}
