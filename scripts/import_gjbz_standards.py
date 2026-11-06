# -*- coding: utf-8 -*-
"""
岗检标准 Excel 导入脚本（2025 最新版）

解析两份附件 Excel，生成可重复执行的 INSERT SQL（先按 standard_group 清空再重建）。

用法:
    python scripts/import_gjbz_standards.py

输出:
    backend/import_gjbz_data_2025.sql

数据来源与分组约定:
    附件1 (Sheet: 炼化生产)  -> standard_group = 1  适用: 乙烯运行部/全密度聚乙烯运行部/高密度聚乙烯运行部
    附件2 (Sheet: 生产辅助)  -> standard_group = 2  适用: 公用工程部/储运部/运行保障部

字段映射 (Excel 列 -> gjbz_data 表列):
    序号       -> (仅用于排序/校验, 不入库)
    专业类别   -> type
    基本模块   -> moudle
    责任要素   -> responsibility
    检查内容   -> issue
    问题性质   -> quality

处理规则:
    1. 合并单元格导致的空值: 序号/专业类别/基本模块/责任要素 沿用上一行的非空值(carry-forward)
    2. 序号缺失行: 沿用上一行序号 + 1
    3. [object Object] 占位: 视为空值
    4. 检查内容为空的行: 跳过(叶子节点无数据)
    5. 所有文本做 strip 处理
"""
import os
import sys
import openpyxl

# 保证中文输出不乱码
if hasattr(sys.stdout, "reconfigure"):
    sys.stdout.reconfigure(encoding="utf-8")

# ---------------------------------------------------------------------------
# 配置
# ---------------------------------------------------------------------------
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

SOURCES = [
    {
        "path": os.path.join(BASE_DIR, "附件1.榆林公司第一组岗检标准（生产） 2025最新版.xlsx"),
        "sheet": "炼化生产",
        "group": 1,
        "group_label": "第一组(生产/炼化)",
    },
    {
        "path": os.path.join(BASE_DIR, "附件2.榆林公司第二组岗检标准（辅助） 2025最新版.xlsx"),
        "sheet": "生产辅助",
        "group": 2,
        "group_label": "第二组(辅助/公用)",
    },
]

OUTPUT_SQL = os.path.join(BASE_DIR, "backend", "import_gjbz_data_2025.sql")

HEADER_ROW = 3          # 表头所在行(序号/专业类别/...)
DATA_START_ROW = 4      # 数据起始行(表头之后, 可能含空分隔行)
COL_COUNT = 6           # A-F 共 6 列

# 需要 carry-forward 的列索引(1-based): 序号(1)/专业类别(2)/基本模块(3)/责任要素(4)
CARRY_FORWARD_COLS = (1, 2, 3, 4)


# ---------------------------------------------------------------------------
# 工具函数
# ---------------------------------------------------------------------------
def clean_value(v):
    """清洗单元格值: None->'', [object Object]->'', 去除内部换行, 其余 strip。

    Excel 单元格内常因自动换行残留 \n/\r(如 '应急组织\n与职责'),
    中文语境下直接去除换行符即可拼回完整短语, 同时保证生成的 SQL 为单行。
    """
    if v is None:
        return ""
    s = str(v)
    if s.strip() == "[object Object]":
        return ""
    # 去除单元格内嵌入的换行/回车(中文换行残留), 再 strip 首尾空白
    s = s.replace("\r\n", "").replace("\r", "").replace("\n", "")
    return s.strip()


def sql_escape(s):
    """转义 SQL 字符串中的单引号与反斜杠。"""
    if s is None:
        return ""
    return s.replace("\\", "\\\\").replace("'", "''")


def parse_sheet(path, sheet_name, group):
    """
    解析单个 Sheet, 返回记录列表。
    每条记录: dict(type, moudle, responsibility, issue, quality, standard_group, seq)
    """
    if not os.path.exists(path):
        raise FileNotFoundError("Excel 文件不存在: %s" % path)

    wb = openpyxl.load_workbook(path, data_only=True)
    if sheet_name in wb.sheetnames:
        ws = wb[sheet_name]
    else:
        ws = wb.active
        print("  [警告] 未找到 Sheet '%s', 回退到活动 Sheet '%s'" % (sheet_name, ws.title))

    records = []
    # carry-forward 状态
    last_seq = 0
    last_type = ""
    last_moudle = ""
    last_resp = ""

    for r in range(DATA_START_ROW, ws.max_row + 1):
        raw = [ws.cell(r, c).value for c in range(1, COL_COUNT + 1)]
        seq_raw, type_raw, moudle_raw, resp_raw, issue_raw, quality_raw = raw

        seq_s = clean_value(seq_raw)
        type_s = clean_value(type_raw)
        moudle_s = clean_value(moudle_raw)
        resp_s = clean_value(resp_raw)
        issue_s = clean_value(issue_raw)
        quality_s = clean_value(quality_raw)

        # 整行全空 -> 跳过(分隔行)
        if not any([seq_s, type_s, moudle_s, resp_s, issue_s, quality_s]):
            continue

        # 检查内容(叶子)为空 -> 跳过(纯合并表头行, 无实际检查项)
        if not issue_s:
            continue

        # carry-forward: 序号
        if seq_s == "":
            cur_seq = last_seq + 1
        else:
            try:
                cur_seq = int(float(seq_s))
            except ValueError:
                cur_seq = last_seq + 1
        last_seq = cur_seq

        # carry-forward: 专业类别/基本模块/责任要素
        if type_s:
            last_type = type_s
        if moudle_s:
            last_moudle = moudle_s
        if resp_s:
            last_resp = resp_s

        records.append({
            "seq": cur_seq,
            "type": last_type,
            "moudle": last_moudle,
            "responsibility": last_resp,
            "issue": issue_s,
            "quality": quality_s,
            "standard_group": group,
        })

    return records


# ---------------------------------------------------------------------------
# 主流程
# ---------------------------------------------------------------------------
def main():
    all_records = []
    summary = []
    for src in SOURCES:
        print("解析: %s (Sheet=%s, group=%d)" % (os.path.basename(src["path"]), src["sheet"], src["group"]))
        recs = parse_sheet(src["path"], src["sheet"], src["group"])
        print("  -> 有效记录 %d 条" % len(recs))
        all_records.extend(recs)
        summary.append((src["group"], src["group_label"], len(recs)))

    # 生成 SQL
    lines = []
    lines.append("-- ============================================================")
    lines.append("-- 岗检标准数据导入 (2025 最新版)")
    lines.append("-- 由 scripts/import_gjbz_standards.py 自动生成, 请勿手工编辑")
    lines.append("-- 执行前请先运行 alter_gjbz_data_add_standard_group.sql 确保字段存在")
    lines.append("-- 策略: 按 standard_group 分组清空后重建(幂等, 可重复执行)")
    lines.append("-- ============================================================")
    lines.append("USE `gjxt_inspection`;")
    lines.append("")
    lines.append("SET NAMES utf8mb4;")
    lines.append("")

    for group, label, cnt in summary:
        lines.append("-- ---------- %s : %d 条 ----------" % (label, cnt))
        lines.append("DELETE FROM `gjbz_data` WHERE `standard_group` = %d;" % group)
        lines.append("")

    lines.append("INSERT INTO `gjbz_data` (`type`, `moudle`, `responsibility`, `issue`, `quality`, `standard_group`) VALUES")

    value_lines = []
    for rec in all_records:
        value_lines.append(
            "('%s', '%s', '%s', '%s', '%s', %d)" % (
                sql_escape(rec["type"]),
                sql_escape(rec["moudle"]),
                sql_escape(rec["responsibility"]),
                sql_escape(rec["issue"]),
                sql_escape(rec["quality"]),
                rec["standard_group"],
            )
        )
    # 最后一条以分号结尾, 其余以逗号结尾
    for i, vl in enumerate(value_lines):
        suffix = ";" if i == len(value_lines) - 1 else ","
        lines.append(vl + suffix)

    lines.append("")
    lines.append("-- 验证")
    lines.append("SELECT `standard_group`, COUNT(*) AS cnt FROM `gjbz_data` GROUP BY `standard_group` ORDER BY `standard_group`;")
    lines.append("")

    with open(OUTPUT_SQL, "w", encoding="utf-8") as f:
        f.write("\n".join(lines))

    print("")
    print("=" * 50)
    print("导入 SQL 已生成: %s" % OUTPUT_SQL)
    print("总计 %d 条记录" % len(all_records))
    for group, label, cnt in summary:
        print("  group=%d %s : %d 条" % (group, label, cnt))


if __name__ == "__main__":
    main()
