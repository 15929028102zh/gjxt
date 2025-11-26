package com.gjxt.inspection.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjxt.inspection.entity.MonthlyInspectionRecord;
import com.gjxt.inspection.entity.MonthlyInspectionTask;
import com.gjxt.inspection.mapper.MonthlyInspectionRecordMapper;
import com.gjxt.inspection.service.IMonthlyInspectionRecordService;
import com.gjxt.inspection.service.IMonthlyInspectionTaskService;
import com.gjxt.inspection.service.ISysUserService;
import com.gjxt.inspection.service.NotificationService;
import com.gjxt.inspection.entity.Notification;
import com.gjxt.inspection.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 月检记录服务实现类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Service
public class MonthlyInspectionRecordServiceImpl extends ServiceImpl<MonthlyInspectionRecordMapper, MonthlyInspectionRecord>
        implements IMonthlyInspectionRecordService {

    @Autowired
    private MonthlyInspectionRecordMapper monthlyInspectionRecordMapper;

    @Autowired
    private IMonthlyInspectionTaskService monthlyInspectionTaskService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ISysUserService userService;

    @Override
    public IPage<MonthlyInspectionRecord> getRecordPage(long current, long size, Map<String, Object> params) {
        Page<MonthlyInspectionRecord> page = new Page<>(current, size);
        return monthlyInspectionRecordMapper.selectRecordPage(page, params);
    }

    @Override
    public MonthlyInspectionRecord getRecordDetail(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }
        return monthlyInspectionRecordMapper.selectRecordById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRecord(MonthlyInspectionRecord record) {
        if (record == null) {
            return false;
        }

        // 设置基础信息
        record.setId(UUID.randomUUID().toString().replace("-", ""));
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        record.setDeleted(0);

        // 生成记录编号
        if (!StringUtils.hasText(record.getRecordNo())) {
            record.setRecordNo(generateRecordNo());
        }

        // 设置默认状态
        if (record.getRecordStatus() == null) {
            // 判断是否为移动端提交，如果是移动端提交则默认状态为4（已提交）
            // 可以通过请求头或其他方式判断，这里暂时通过是否有移动端特有字段来判断
            if (StringUtils.hasText(record.getProfessionalCategory()) || 
                StringUtils.hasText(record.getModule()) || 
                StringUtils.hasText(record.getResponsibility()) || 
                StringUtils.hasText(record.getIssue()) || 
                StringUtils.hasText(record.getQualityDetermination())) {
                record.setRecordStatus(4); // 移动端默认已提交状态
            } else {
                record.setRecordStatus(0); // PC端默认草稿状态
            }
        }

        // 如果有taskId，自动将关联的月检任务状态更新为"进行中"（状态值为1）
        if (StringUtils.hasText(record.getTaskId())) {
            MonthlyInspectionTask task = monthlyInspectionTaskService.getTaskDetail(record.getTaskId());
            if (task != null && task.getStatus() != null && task.getStatus() == 0) { // 只有未开始状态才更新为进行中
                monthlyInspectionTaskService.updateTaskStatus(record.getTaskId(), 1);
            }
        }

        // 处理移动端字段映射到数据库字段
        mapMobileFieldsToDbFields(record);

        boolean result = save(record);
        if (result) {
            // 可以在这里添加其他业务逻辑，如发送通知等
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRecord(MonthlyInspectionRecord record) {
        if (record == null || !StringUtils.hasText(record.getId())) {
            return false;
        }
        
        // 处理移动端字段映射到数据库字段
        mapMobileFieldsToDbFields(record);
        
        record.setUpdateTime(LocalDateTime.now());
        return updateById(record);
    }

    @Override
    public boolean deleteRecord(String id) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        // 逻辑删除
        LambdaUpdateWrapper<MonthlyInspectionRecord> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MonthlyInspectionRecord::getId, id)
                .set(MonthlyInspectionRecord::getDeleted, 1)
                .set(MonthlyInspectionRecord::getUpdateTime, LocalDateTime.now());
        return update(updateWrapper);
    }

    @Override
    public boolean deleteRecords(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        // 逻辑删除
        LambdaUpdateWrapper<MonthlyInspectionRecord> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(MonthlyInspectionRecord::getId, ids)
                .set(MonthlyInspectionRecord::getDeleted, 1)
                .set(MonthlyInspectionRecord::getUpdateTime, LocalDateTime.now());
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitRecords(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return monthlyInspectionRecordMapper.batchUpdateStatus(ids, 1) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditRecords(List<String> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return monthlyInspectionRecordMapper.batchUpdateStatus(ids, status) > 0;
    }

    @Override
    public Map<String, Object> getMonthlyStatistics(Map<String, Object> params) {
        return monthlyInspectionRecordMapper.selectMonthlyStatistics(params);
    }

    @Override
    public List<Map<String, Object>> getMonthlyTrend(Map<String, Object> params) {
        return monthlyInspectionRecordMapper.selectMonthlyTrend(params);
    }

    @Override
    public List<MonthlyInspectionRecord> getRecordsByTaskId(String taskId, Integer recordStatus) {
        if (!StringUtils.hasText(taskId)) {
            return null;
        }
        return monthlyInspectionRecordMapper.selectRecordsByTaskId(taskId, recordStatus);
    }

    @Override
    public List<MonthlyInspectionRecord> getRecordsByUserId(String createUserId, Integer recordStatus) {
        if (!StringUtils.hasText(createUserId)) {
            return null;
        }
        return monthlyInspectionRecordMapper.selectRecordsByUserId(createUserId, recordStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteRecords(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        // 逻辑删除
        LambdaUpdateWrapper<MonthlyInspectionRecord> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(MonthlyInspectionRecord::getId, ids)
                .set(MonthlyInspectionRecord::getDeleted, 1)
                .set(MonthlyInspectionRecord::getUpdateTime, LocalDateTime.now());
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRecordStatus(String id, Integer recordStatus) {
        if (!StringUtils.hasText(id) || recordStatus == null) {
            return false;
        }
        LambdaUpdateWrapper<MonthlyInspectionRecord> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MonthlyInspectionRecord::getId, id)
                .set(MonthlyInspectionRecord::getRecordStatus, recordStatus)
                .set(MonthlyInspectionRecord::getUpdateTime, LocalDateTime.now());
        return update(updateWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateRecordStatus(List<String> ids, Integer recordStatus) {
        if (ids == null || ids.isEmpty() || recordStatus == null) {
            return false;
        }
        return monthlyInspectionRecordMapper.batchUpdateStatus(ids, recordStatus) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitRecord(String id) {
        return updateRecordStatus(id, 1); // 1-已提交
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditRecord(String id) {
        return updateRecordStatus(id, 2); // 2-已审核
    }

    @Override
    public List<Map<String, Object>> getStatusStatistics() {
        return null;
    }

    @Override
    public List<Map<String, Object>> getDepartmentStatistics(Integer recordStatus) {
        return monthlyInspectionRecordMapper.selectDepartmentStatistics(recordStatus);
    }

    @Override
    public Long getMonthlyRecordCount(String userId, Integer recordStatus, String departmentId, String startDate, String endDate) {
        LambdaQueryWrapper<MonthlyInspectionRecord> queryWrapper = new LambdaQueryWrapper<>();
        
        // 用户ID过滤
        if (StringUtils.hasText(userId)) {
            queryWrapper.eq(MonthlyInspectionRecord::getCreateUserId, userId);
        }
        
        // 记录状态过滤
        if (recordStatus != null) {
            queryWrapper.eq(MonthlyInspectionRecord::getRecordStatus, recordStatus);
        }
        
        // 部门ID过滤
        if (StringUtils.hasText(departmentId)) {
            queryWrapper.eq(MonthlyInspectionRecord::getResponsibilityId, departmentId);
        }
        
        // 日期范围过滤
        if (StringUtils.hasText(startDate)) {
            try {
                LocalDateTime startDateTime = LocalDateTime.parse(startDate + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                queryWrapper.ge(MonthlyInspectionRecord::getCreateTime, startDateTime);
            } catch (Exception e) {
                // 日期格式错误，忽略此条件
            }
        }
        
        if (StringUtils.hasText(endDate)) {
            try {
                LocalDateTime endDateTime = LocalDateTime.parse(endDate + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                queryWrapper.le(MonthlyInspectionRecord::getCreateTime, endDateTime);
            } catch (Exception e) {
                // 日期格式错误，忽略此条件
            }
        }
        
        return this.count(queryWrapper);
    }

    @Override
    public String generateRecordNo() {
        // 生成格式：YC + 年月日 + 4位序号
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "YC" + dateStr;
        
        // 查询当天最大序号
        String latestRecordNo = monthlyInspectionRecordMapper.selectLatestRecordNo();
        int sequence = 1;
        
        if (StringUtils.hasText(latestRecordNo) && latestRecordNo.startsWith(prefix)) {
            try {
                String sequenceStr = latestRecordNo.substring(prefix.length());
                sequence = Integer.parseInt(sequenceStr) + 1;
            } catch (NumberFormatException e) {
                sequence = 1;
            }
        }
        
        return prefix + String.format("%04d", sequence);
    }

    public boolean isRecordNoExists(String recordNo) {
        if (!StringUtils.hasText(recordNo)) {
            return false;
        }
        LambdaQueryWrapper<MonthlyInspectionRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonthlyInspectionRecord::getRecordNo, recordNo)
                .eq(MonthlyInspectionRecord::getDeleted, 0);
        return count(queryWrapper) > 0;
    }

    public MonthlyInspectionRecord getRecordByRecordNo(String recordNo) {
        if (!StringUtils.hasText(recordNo)) {
            return null;
        }
        LambdaQueryWrapper<MonthlyInspectionRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonthlyInspectionRecord::getRecordNo, recordNo)
                .eq(MonthlyInspectionRecord::getDeleted, 0);
        return getOne(queryWrapper);
    }

    public long getDraftRecordCount(String createUserId) {
        if (!StringUtils.hasText(createUserId)) {
            return 0;
        }
        LambdaQueryWrapper<MonthlyInspectionRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonthlyInspectionRecord::getCreateUserId, createUserId)
                .eq(MonthlyInspectionRecord::getRecordStatus, 0)
                .eq(MonthlyInspectionRecord::getDeleted, 0);
        return count(queryWrapper);
    }

    public long getSubmittedRecordCount(String createUserId) {
        if (!StringUtils.hasText(createUserId)) {
            return 0;
        }
        LambdaQueryWrapper<MonthlyInspectionRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonthlyInspectionRecord::getCreateUserId, createUserId)
                .eq(MonthlyInspectionRecord::getRecordStatus, 1)
                .eq(MonthlyInspectionRecord::getDeleted, 0);
        return count(queryWrapper);
    }

    public long getAuditedRecordCount(String createUserId) {
        if (!StringUtils.hasText(createUserId)) {
            return 0;
        }
        LambdaQueryWrapper<MonthlyInspectionRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonthlyInspectionRecord::getCreateUserId, createUserId)
                .eq(MonthlyInspectionRecord::getRecordStatus, 2)
                .eq(MonthlyInspectionRecord::getDeleted, 0);
        return count(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmRectification(String id, String rectificationSuggestion, String rectificationEffect, 
                                      String rectificationImages, String natureDefinition, String professionId, 
                                      String departmentId, Integer status) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        
        MonthlyInspectionRecord record = getById(id);
        if (record == null) {
            return false;
        }
        
        // 只有状态为"待整改"(0)的记录才能进行整改确认
        if (record.getRecordStatus() == null || record.getRecordStatus() != 0) {
            return false;
        }
        
        // 更新整改信息（仅当有传值时更新，避免覆盖原有非空数据）
        if (StringUtils.hasText(rectificationSuggestion)) {
            record.setRectificationSuggestion(rectificationSuggestion);
        }
        if (StringUtils.hasText(rectificationEffect)) {
            record.setRectificationEffect(rectificationEffect);
        }
        if (StringUtils.hasText(rectificationImages)) {
            record.setRectificationImages(rectificationImages);
        }
        record.setRectificationTime(LocalDateTime.now());
        
        // 更新其他字段（如果提供）
        if (StringUtils.hasText(natureDefinition)) {
            record.setQualityName(natureDefinition);
        }
        if (StringUtils.hasText(professionId)) {
            record.setProfessionalTypeId(professionId);
        }
        if (StringUtils.hasText(departmentId)) {
            record.setResponsibilityId(departmentId);
        }
        
        // 设置状态，如果传入了status参数则使用，否则默认为1（已整改）
        record.setRecordStatus(status != null ? status : 1);
        record.setUpdateTime(LocalDateTime.now());
        
        return updateById(record);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmReview(String id, String reviewSuggestion, String reviewEffect, String reviewImages) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        
        MonthlyInspectionRecord record = getById(id);
        if (record == null) {
            return false;
        }
        
        // 只有状态为"已整改"(1)的记录才能进行复查确认
        if (record.getRecordStatus() == null || record.getRecordStatus() != 1) {
            return false;
        }
        
        // 更新复查信息
        record.setReviewSuggestion(reviewSuggestion);
        record.setReviewEffect(reviewEffect);
        record.setReviewImages(reviewImages);
        record.setReviewTime(LocalDateTime.now());
        record.setRecordStatus(2); // 更新状态为"已复查"
        record.setUpdateTime(LocalDateTime.now());
        
        return updateById(record);
    }

    /**
     * 将移动端字段映射到数据库字段
     * 
     * @param record 月检记录对象
     */
    private void mapMobileFieldsToDbFields(MonthlyInspectionRecord record) {
        if (record == null) {
            return;
        }

        // 专业类别映射
        if (StringUtils.hasText(record.getProfessionalCategory())) {
            record.setProfessionalTypeName(record.getProfessionalCategory());
        }

        // 模块映射
        if (StringUtils.hasText(record.getModule())) {
            record.setModuleName(record.getModule());
        }

        // 责任部门映射
        if (StringUtils.hasText(record.getResponsibility())) {
            record.setResponsibilityName(record.getResponsibility());
        }

        // 问题映射
        if (StringUtils.hasText(record.getIssue())) {
            record.setIssueName(record.getIssue());
        }

        // 性质界定映射
        if (StringUtils.hasText(record.getQualityDetermination())) {
            record.setQualityName(record.getQualityDetermination());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectRecord(String id) {
        try {
            MonthlyInspectionRecord record = this.getById(id);
            if (record == null) {
                return false;
            }
            
            // 获取部门管理员
            List<SysUser> admins = userService.getAdminsByDeptId(record.getResponsibilityId());
            if (admins != null && !admins.isEmpty()) {
                // 发送通知给所有部门管理员
                for (SysUser admin : admins) {
                    Notification notification = new Notification();
                    notification.setType("AUDIT");
                    notification.setTitle("待审核的月检记录");
                    notification.setContent("您有一条待审核的月检记录，请及时处理。问题描述：" + record.getProblemDescription());
                    notification.setUserId(admin.getId());
                    notification.setUserName(admin.getRealName());
                    notification.setDepartmentId(record.getResponsibilityId());
                    notification.setIsRead(false);
                    notification.setCreateTime(LocalDateTime.now());
                    notification.setPriority("high");
                    notification.setActionUrl("/monthly/audit?recordId=" + id);
                    
                    notificationService.createNotification(notification);
                }
            }
            
            // 更新记录状态为待审核
            record.setRecordStatus(3); // 3-待审核
            record.setAuditStatus(0); // 0-未审核
            record.setUpdateTime(LocalDateTime.now());
            
            return this.updateById(record);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public IPage<MonthlyInspectionRecord> getAuditRecords(long current, long size, String departmentId, Integer recordStatus,
                                                         String startDate, String endDate) {
        Page<MonthlyInspectionRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<MonthlyInspectionRecord> wrapper = new LambdaQueryWrapper<>();
        
        // 查询待审核的记录（recordStatus=3）
        wrapper.eq(MonthlyInspectionRecord::getRecordStatus, 3);
        
        if (StringUtils.hasText(departmentId)) {
            wrapper.eq(MonthlyInspectionRecord::getResponsibilityId, departmentId);
        }
        
        if (recordStatus != null) {
            wrapper.eq(MonthlyInspectionRecord::getAuditStatus, recordStatus);
        }
        
        if (StringUtils.hasText(startDate)) {
            wrapper.ge(MonthlyInspectionRecord::getCreateTime, 
                    LocalDateTime.parse(startDate + "T00:00:00"));
        }
        
        if (StringUtils.hasText(endDate)) {
            wrapper.le(MonthlyInspectionRecord::getCreateTime, 
                    LocalDateTime.parse(endDate + "T23:59:59"));
        }
        
        wrapper.orderByDesc(MonthlyInspectionRecord::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmAudit(String id, String auditorId, String auditReason) {
        try {
            MonthlyInspectionRecord record = this.getById(id);
            if (record == null) {
                return false;
            }
            
            // 审核成功，记录变更为已提交状态（4）
            record.setRecordStatus(4); // 4-已提交
            record.setAuditStatus(1); // 1-已审核
            record.setAuditReason(auditReason);
            record.setAuditorId(auditorId);
            record.setAuditTime(LocalDateTime.now());
            
            // 清空整改图片、整改建议、复查图片、复查建议
            record.setRectificationImages(null);
            record.setRectificationEffect(null);
            record.setRectificationSuggestion(null);
            record.setReviewImages(null);
            record.setReviewEffect(null);
            record.setReviewSuggestion(null);
            
            record.setUpdateTime(LocalDateTime.now());
            
            return this.updateById(record);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectAudit(String id) {
        try {
            MonthlyInspectionRecord record = this.getById(id);
            if (record == null) {
                return false;
            }
            
            // 拒绝审核，将记录状态从待审核(3)改回已提交(1)
            if (record.getRecordStatus() != null && record.getRecordStatus() == 3) {
                record.setRecordStatus(1); // 1-已提交
                record.setAuditStatus(0); // 0-未审核
                record.setUpdateTime(LocalDateTime.now());
                return this.updateById(record);
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}