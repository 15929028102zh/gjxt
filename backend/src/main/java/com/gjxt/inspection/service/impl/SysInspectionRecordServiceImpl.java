package com.gjxt.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.entity.SysDictionary;
import com.gjxt.inspection.entity.SysInspectionRecord;
import com.gjxt.inspection.mapper.SysInspectionRecordMapper;
import com.gjxt.inspection.service.ISysDictionaryService;
import com.gjxt.inspection.service.ISysInspectionRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import com.gjxt.inspection.entity.SysInspectionRecordSubmitData;
import java.util.Map;
import java.util.UUID;

/**
 * 巡检记录服务实现类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Slf4j
@Service
public class SysInspectionRecordServiceImpl extends ServiceImpl<SysInspectionRecordMapper, SysInspectionRecord> implements ISysInspectionRecordService {

    @Autowired
    private SysInspectionRecordMapper recordMapper;
    
    @Autowired
    private ISysDictionaryService dictionaryService;

    @Override
    public PageResult<SysInspectionRecord> getRecordPage(Long current, Long size, String taskId,
                                                        String groupLeaderId, String deputyLeaderId, String departmentId,
                                                        String professionId, String natureId, String locationId, String inspectorId,
                                                        String responsibleUnitId, String responsiblePersonId, String reviewerId,
                                                        Integer status,
                                                        LocalDateTime startTime, LocalDateTime endTime) {
        Page<SysInspectionRecord> page = new Page<>(current, size);
        String statusStr = status != null ? status.toString() : null;
        IPage<SysInspectionRecord> result = this.baseMapper.selectRecordPage(page, taskId, groupLeaderId, deputyLeaderId, departmentId, professionId, natureId, locationId, inspectorId, responsibleUnitId, responsiblePersonId, reviewerId, statusStr, startTime, endTime);
        return new PageResult<SysInspectionRecord>(result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords());
    }

    @Override
    public SysInspectionRecord getRecordDetail(Long id) {
        return this.getById(id);
    }

    @Override
    public SysInspectionRecord getRecordById(String id) {
        return recordMapper.selectRecordDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRecord(SysInspectionRecord record) {
        // 确保新增记录时ID为null，让MyBatis Plus自动生成UUID
        record.setId(null);
        if (record.getRecordNo() == null || record.getRecordNo().isEmpty()) {
            record.setRecordNo(generateRecordNo());
        }
        record.setStatus(0); // 0-待整改
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        
        // 调试日志：打印接收到的记录对象
        log.info("addRecord - 接收到的记录对象: {}", record);
        log.info("addRecord - 检查组长姓名: {}", record.getGroupLeaderName());
        log.info("addRecord - 副组长姓名: {}", record.getDeputyLeaderName());
        
        return this.save(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRecord(SysInspectionRecord record) {
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rectificationConfirm(Long id, String rectificationDescription, String rectificationPhoto) {
        SysInspectionRecord record = this.getById(id);
        if (record == null) {
            return false;
        }
        
        record.setStatus(1); // 1-待复查
        record.setRectificationEffect(rectificationDescription);
        record.setRectificationImageUrls(rectificationPhoto);
        record.setRectificationTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rectifyConfirm(String id, String rectificationEffect, String rectificationImageUrls) {
        SysInspectionRecord record = this.getById(id);
        if (record == null) {
            return false;
        }
        
        record.setStatus(1); // 1-待复查
        record.setRectificationEffect(rectificationEffect);
        record.setRectificationImageUrls(rectificationImageUrls);
        record.setRectificationTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reviewConfirm(String id, String reviewerId, String reviewEffect, String reviewImageUrls) {
        SysInspectionRecord record = this.getById(id);
        if (record == null) {
            return false;
        }
        
        record.setReviewerId(reviewerId);
        record.setReviewEffect(reviewEffect);
        record.setReviewImageUrls(reviewImageUrls);
        record.setReviewTime(LocalDateTime.now());
        
        // 根据复查效果设置状态
       
            record.setStatus(2); // 2-已完成
      
        
        record.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(record);
    }



    @Override
    public List<SysInspectionRecord> getRecordsByUserId(Long userId, String status) {
        LambdaQueryWrapper<SysInspectionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysInspectionRecord::getInspector, userId);
        if (StringUtils.hasText(status)) {
            wrapper.eq(SysInspectionRecord::getStatus, status);
        }
        wrapper.orderByDesc(SysInspectionRecord::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public PageResult<SysInspectionRecord> getUserRecords(Long userId, Long current, Long size, String status) {
        Page<SysInspectionRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<SysInspectionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysInspectionRecord::getInspector, userId);
        if (StringUtils.hasText(status)) {
            wrapper.eq(SysInspectionRecord::getStatus, status);
        }
        wrapper.orderByDesc(SysInspectionRecord::getCreateTime);
        IPage<SysInspectionRecord> result = this.page(page, wrapper);
        
        return new PageResult<>(result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords());
    }

    @Override
    public List<Map<String, Object>> getStatusStatistics() {
        return recordMapper.selectStatusStatistics();
    }

    @Override
    public List<Map<String, Object>> getProfessionStatistics(String status) {
        return recordMapper.selectProfessionStatistics(status);
    }

    @Override
    public List<Map<String, Object>> getLocationStatistics(String status) {
        return recordMapper.selectLocationStatistics(status);
    }

    @Override
    public List<Map<String, Object>> getResponsiblePersonStatistics(String status) {
        return recordMapper.selectResponsiblePersonStatistics(status);
    }

    @Override
    public void handleOverdueRecords() {
        // 处理逾期记录的逻辑
        // 可以在这里实现自动处理逾期记录的业务逻辑
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleDelayedRectification() {
        // 查询超过整改期限但未整改的记录
        LambdaQueryWrapper<SysInspectionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysInspectionRecord::getStatus, "0") // 待整改状态
               .lt(SysInspectionRecord::getRectificationDeadline, LocalDateTime.now());
        
        List<SysInspectionRecord> overdueRecords = this.list(wrapper);
        
        // 处理逾期记录，例如发送通知、更新状态等
        for (SysInspectionRecord record : overdueRecords) {
            // 这里可以添加具体的处理逻辑
            // 比如发送通知、记录日志等
        }
        
        return true;
    }

    @Override
    public List<SysInspectionRecord> exportRecords(String status, Long professionId, Long locationId, 
                                                  Long checkerId, String responsiblePerson, 
                                                  LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<SysInspectionRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(status)) {
            wrapper.eq(SysInspectionRecord::getStatus, status);
        }
        if (professionId != null) {
            wrapper.eq(SysInspectionRecord::getProfessionId, professionId);
        }
        if (locationId != null) {
            wrapper.like(SysInspectionRecord::getLocation, locationId);
        }
        if (checkerId != null) {
            wrapper.eq(SysInspectionRecord::getInspector, checkerId);
        }
        if (StringUtils.hasText(responsiblePerson)) {
            wrapper.like(SysInspectionRecord::getResponsiblePersonName, responsiblePerson);
        }
        if (startTime != null) {
            wrapper.ge(SysInspectionRecord::getCreateTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(SysInspectionRecord::getCreateTime, endTime);
        }
        
        wrapper.orderByDesc(SysInspectionRecord::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public String generateRecordNo() {
        // 生成记录编号：使用UUID
        String recordNo = UUID.randomUUID().toString();
        log.info("生成新的记录编号: {}", recordNo);
        return recordNo;
    }

    @Override
    public Map<String, Object> getRecordsByStatus(String status, Long current, Long size) {
        Page<SysInspectionRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<SysInspectionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysInspectionRecord::getStatus, status)
               .orderByDesc(SysInspectionRecord::getCreateTime);
        IPage<SysInspectionRecord> result = this.page(page, wrapper);
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("records", result.getRecords());
        resultMap.put("total", result.getTotal());
        resultMap.put("current", result.getCurrent());
        resultMap.put("size", result.getSize());
        return resultMap;
    }

    @Override
    public Map<String, Object> getRecordsByProfession(String profession, Long current, Long size) {
        Page<SysInspectionRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<SysInspectionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(SysInspectionRecord::getProfessionName, profession)
               .orderByDesc(SysInspectionRecord::getCreateTime);
        IPage<SysInspectionRecord> result = this.page(page, wrapper);
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("records", result.getRecords());
        resultMap.put("total", result.getTotal());
        resultMap.put("current", result.getCurrent());
        resultMap.put("size", result.getSize());
        return resultMap;
    }

    @Override
    public Map<String, Object> getRecordsByLocation(String location, Long current, Long size) {
        Page<SysInspectionRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<SysInspectionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(SysInspectionRecord::getLocation, location)
               .orderByDesc(SysInspectionRecord::getCreateTime);
        IPage<SysInspectionRecord> result = this.page(page, wrapper);
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("records", result.getRecords());
        resultMap.put("total", result.getTotal());
        resultMap.put("current", result.getCurrent());
        resultMap.put("size", result.getSize());
        return resultMap;
    }

    @Override
    public Map<String, Object> getRecordsByResponsible(String responsiblePerson, Long current, Long size) {
        Page<SysInspectionRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<SysInspectionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysInspectionRecord::getResponsiblePersonName, responsiblePerson)
               .orderByDesc(SysInspectionRecord::getCreateTime);
        IPage<SysInspectionRecord> result = this.page(page, wrapper);
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("records", result.getRecords());
        resultMap.put("total", result.getTotal());
        resultMap.put("current", result.getCurrent());
        resultMap.put("size", result.getSize());
        return resultMap;
    }

    @Override
    public List<SysInspectionRecord> getRecordsByResponsiblePerson(String responsiblePerson) {
        LambdaQueryWrapper<SysInspectionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysInspectionRecord::getResponsiblePersonName, responsiblePerson)
               .orderByDesc(SysInspectionRecord::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public Map<String, Object> getTimeRangeStatistics(String startTime, String endTime, String profession, String location, String responsiblePerson) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取时间范围内的统计数据
        List<Map<String, Object>> statusResults = recordMapper.selectStatusStatisticsByDateRange(startTime, endTime);
        List<Map<String, Object>> professionResults = recordMapper.selectProfessionStatisticsByDateRange(startTime, endTime);
        
        // 转换状态统计数据
        Map<String, Integer> statusStats = new HashMap<>();
        for (Map<String, Object> statusResult : statusResults) {
            String key = (String) statusResult.get("status");
            Integer count = ((Number) statusResult.get("count")).intValue();
            statusStats.put(key, count);
        }
        
        // 转换专业统计数据
        Map<String, Integer> professionStats = new HashMap<>();
        for (Map<String, Object> professionResult : professionResults) {
            String key = (String) professionResult.get("profession_name");
            Integer count = ((Number) professionResult.get("count")).intValue();
            professionStats.put(key, count);
        }
        
        result.put("statusStatistics", statusStats);
        result.put("professionStatistics", professionStats);
        
        return result;
    }

    @Override
    public boolean submitRecord(SysInspectionRecordSubmitData submitData) {
        try {
            // 使用静态方法创建记录实体，字段映射在实体类中处理
            SysInspectionRecord record = SysInspectionRecord.createFromSubmitData(submitData);
            
            // 生成记录编号
            record.setRecordNo(generateRecordNo());
            
            // 调试日志：检查组长和副组长字段值
            log.info("提交记录 - 检查组长ID: {}, 副组长ID: {}", record.getGroupLeaderId(), record.getDeputyLeaderId());
            log.info("前端提交数据 - groupLeader: {}, deputyLeader: {}", submitData.getGroupLeader(), submitData.getDeputyLeader());
            
            // 使用专门的插入方法
            int result = recordMapper.insertSubmitRecord(record);
            return result > 0;
        } catch (Exception e) {
            log.error("提交巡检记录失败", e);
            return false;
        }
    }

    @Override
    public Long getWeeklyRecordCountByUserAndStatus(String userId, Integer status) {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        
        // 计算本周开始时间（周一00:00:00）
        LocalDateTime weekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        // 计算本周结束时间（周日23:59:59）
        LocalDateTime weekEnd = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        // 调用Mapper方法查询
        Long count = recordMapper.selectWeeklyRecordCountByUserAndStatus(userId, status, weekStart, weekEnd);
        return count != null ? count : 0L;
    }

    @Override
    public void exportRecordsToExcel(HttpServletResponse response, String problemDescription, 
                                    String responsiblePerson, String professionId, String natureId, 
                                    String departmentId, Integer status, LocalDateTime startTime, 
                                    LocalDateTime endTime) {
        try {

            
            // 使用recordMapper调用selectRecordPage方法获取包含关联字段的数据
            // 注意：selectRecordPage方法需要Page对象和所有查询参数
            // 由于导出不需要分页，这里可以创建一个Page对象，但只取其records
            Page<SysInspectionRecord> page = new Page<>(1, -1); // size设置为-1表示不分页
            IPage<SysInspectionRecord> resultPage = recordMapper.selectRecordPage(page,
                    null, null, departmentId, professionId, natureId, problemDescription, null, null, responsiblePerson, null, null, status != null ? status.toString() : null, startTime, endTime);
            List<SysInspectionRecord> records = resultPage.getRecords();
            
            // 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("周检记录");
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "序号", "检查组组长", "副组长", "所属部门", "专业", "问题描述", "发生部位", 
                "整改期限", "检查人", "责任单位", "责任人", "整改效果", 
                "整改时间", "复查效果", "复查人", "复查时间", 
                "性质界定", "考核情况", "安全记分"
            };
            
            // 设置标题样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            
            // 创建标题单元格
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 创建数据样式
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.LEFT);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            
            // 填充数据
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < records.size(); i++) {
                SysInspectionRecord record = records.get(i);
                Row row = sheet.createRow(i + 1);
                
                // 序号
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(i + 1);
                cell0.setCellStyle(dataStyle);
                
                // 检查组组长
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(record.getGroupLeaderName() != null ? record.getGroupLeaderName() : "");
                cell1.setCellStyle(dataStyle);
                
                // 副组长
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(record.getDeputyLeaderName() != null ? record.getDeputyLeaderName() : "");
                cell2.setCellStyle(dataStyle);
                
                // 所属部门
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(record.getDepartmentName() != null ? record.getDepartmentName() : "");
                cell3.setCellStyle(dataStyle);
                
                // 专业
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(record.getProfessionName() != null ? record.getProfessionName() : "");
                cell4.setCellStyle(dataStyle);
                
                // 问题描述
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(record.getProblemDescription() != null ? record.getProblemDescription() : "");
                cell5.setCellStyle(dataStyle);
                
                // 发生部位
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(record.getLocation() != null ? record.getLocation() : "");
                cell6.setCellStyle(dataStyle);
                
                // 整改期限
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(record.getRectificationDeadline() != null ? 
                    record.getRectificationDeadline().format(formatter) : "");
                cell7.setCellStyle(dataStyle);
                
                // 检查人
                Cell cell8 = row.createCell(8);
                cell8.setCellValue(record.getInspectorName() != null ? record.getInspectorName() : "");
                cell8.setCellStyle(dataStyle);
                
                // 责任单位
                Cell cell9 = row.createCell(9);
                cell9.setCellValue(record.getResponsibleUnit() != null ? record.getResponsibleUnit() : "");
                cell9.setCellStyle(dataStyle);
                
                // 责任人
                Cell cell10 = row.createCell(10);
                cell10.setCellValue(record.getResponsiblePersonName() != null ? record.getResponsiblePersonName() : "");
                cell10.setCellStyle(dataStyle);
                
                // 整改效果
                Cell cell11 = row.createCell(11);
                cell11.setCellValue(record.getRectificationEffect() != null ? record.getRectificationEffect() : "");
                cell11.setCellStyle(dataStyle);
                
                // 整改时间
                Cell cell12 = row.createCell(12);
                cell12.setCellValue(record.getRectificationTime() != null ? 
                    record.getRectificationTime().format(formatter) : "");
                cell12.setCellStyle(dataStyle);
                
                // 复查效果
                Cell cell13 = row.createCell(13);
                cell13.setCellValue(record.getReviewEffect() != null ? record.getReviewEffect() : "");
                cell13.setCellStyle(dataStyle);
                
                // 复查人
                Cell cell14 = row.createCell(14);
                cell14.setCellValue(record.getReviewerName() != null ? record.getReviewerName() : "");
                cell14.setCellStyle(dataStyle);
                
                // 复查时间
                Cell cell15 = row.createCell(15);
                cell15.setCellValue(record.getReviewTime() != null ? 
                    record.getReviewTime().format(formatter) : "");
                cell15.setCellStyle(dataStyle);
                
                // 性质界定
                Cell cell16 = row.createCell(16);
                cell16.setCellValue(record.getNatureDefinition() != null ? record.getNatureDefinition() : "");
                cell16.setCellStyle(dataStyle);
                
                // 考核情况
                Cell cell17 = row.createCell(17);
                cell17.setCellValue(record.getAssessmentSituation() != null ? record.getAssessmentSituation() : "");
                cell17.setCellStyle(dataStyle);
                
                // 安全记分
                Cell cell18 = row.createCell(18);
                cell18.setCellValue(record.getSafetyScore() != null ? record.getSafetyScore().toString() : "");
                cell18.setCellStyle(dataStyle);
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // 设置最小列宽
                if (sheet.getColumnWidth(i) < 2000) {
                    sheet.setColumnWidth(i, 2000);
                }
                // 设置最大列宽
                if (sheet.getColumnWidth(i) > 8000) {
                    sheet.setColumnWidth(i, 8000);
                }
            }
            
            // 设置响应头
            String fileName = "周检记录_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            response.setCharacterEncoding("UTF-8");
            
            // 写入响应流
            workbook.write(response.getOutputStream());
            workbook.close();
            
            log.info("导出Excel成功，共导出{}条记录", records.size());
            
        } catch (IOException e) {
            log.error("导出Excel失败", e);
            throw new RuntimeException("导出Excel失败: " + e.getMessage());
        }
    }
}