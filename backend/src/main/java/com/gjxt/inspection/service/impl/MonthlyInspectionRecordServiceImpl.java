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
import com.gjxt.inspection.service.ISysDeptService;
import com.gjxt.inspection.entity.SysDept;
import com.gjxt.inspection.service.NotificationService;
import com.gjxt.inspection.service.WebSocketNotificationService;
import com.gjxt.inspection.entity.Notification;
import com.gjxt.inspection.entity.SysUser;
import com.gjxt.inspection.util.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class MonthlyInspectionRecordServiceImpl extends ServiceImpl<MonthlyInspectionRecordMapper, MonthlyInspectionRecord>
        implements IMonthlyInspectionRecordService {

    /** 状态码白名单 */
    private static final java.util.Set<Integer> VALID_STATUS = new java.util.HashSet<>(java.util.Arrays.asList(0, 1, 2, 3, 4));

    /** 检查状态码是否合法 */
    private static boolean isValidStatus(Integer s) {
        return s != null && VALID_STATUS.contains(s);
    }

    @Autowired
    private MonthlyInspectionRecordMapper monthlyInspectionRecordMapper;

    @Autowired
    private IMonthlyInspectionTaskService monthlyInspectionTaskService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WebSocketNotificationService webSocketNotificationService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private UserContextUtil userContextUtil;

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

        // 录入期校验：只有当前处于月检任务的录入期内才允许新增记录
        if (StringUtils.hasText(record.getTaskId()) && !isInInspectionPeriod(record.getTaskId())) {
            throw new RuntimeException("当前不在月检任务的录入期内，无法新增记录");
        }

        // V3:检查单位(checkUnitId)必填 — 若前端未传,兜底从当前登录用户所属部门回填
        if (!StringUtils.hasText(record.getCheckUnitId())) {
            SysUser currentUser = userContextUtil.getCurrentUser();
            if (currentUser != null && StringUtils.hasText(currentUser.getDeptId())) {
                record.setCheckUnitId(currentUser.getDeptId());
                record.setCheckUnitName(currentUser.getDeptName());
            }
        }
        if (!StringUtils.hasText(record.getCheckUnitId())) {
            throw new RuntimeException("检查单位(checkUnitId)不能为空,且无法从当前登录用户所属部门自动回填");
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

        // 设置默认状态：所有客户端（Web / Mobile）录入的记录统一先进入
        // "已确认/等待统一提交"(4)，需在 monthlySubmit 页面提交后才进入整改流程。
        // 若调用方已显式指定 recordStatus，则信任调用方。
        if (record.getRecordStatus() == null) {
            record.setRecordStatus(4);
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

        // 阶段守卫：公示期内不允许直接改字段(须走编辑审核)，公示期后禁止编辑
        MonthlyInspectionRecord existing = this.getById(record.getId());
        if (existing == null) {
            return false;
        }
        String phaseReason = checkPhaseAllowsMutation(existing, false);
        if (phaseReason != null) {
            throw new RuntimeException(phaseReason);
        }

        // V3:检查单位(checkUnitId)兜底 — 若前端未传,从当前登录用户所属部门回填(用于旧记录的兼容)
        if (!StringUtils.hasText(record.getCheckUnitId())) {
            SysUser currentUser = userContextUtil.getCurrentUser();
            if (currentUser != null && StringUtils.hasText(currentUser.getDeptId())) {
                record.setCheckUnitId(currentUser.getDeptId());
                record.setCheckUnitName(currentUser.getDeptName());
            }
        }

        // 处理移动端字段映射到数据库字段
        mapMobileFieldsToDbFields(record);

        // V3:编辑场景下,记录原值/修改值快照 + 检测 nature_modified
        buildModificationSnapshotIfUpdate(record);

        record.setUpdateTime(LocalDateTime.now());
        return updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(String id) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        return batchDeleteRecords(java.util.Collections.singletonList(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecords(List<String> ids) {
        return batchDeleteRecords(ids);
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
        assertDeletable(ids);
        // 逻辑删除
        LambdaUpdateWrapper<MonthlyInspectionRecord> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(MonthlyInspectionRecord::getId, ids)
                .set(MonthlyInspectionRecord::getDeleted, 1)
                .set(MonthlyInspectionRecord::getUpdateTime, LocalDateTime.now());
        return update(updateWrapper);
    }

    /**
     * 删除前置校验：已提交的记录（status ∈ {0,1,2,3}）一律不允许删除，
     * 仅 status=4（待提交）的记录可在月检提交页面删除。
     */
    private void assertDeletable(List<String> ids) {
        SysUser currentUser = userContextUtil.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未认证");
        }
        List<MonthlyInspectionRecord> records = this.listByIds(ids);
        if (records == null || records.size() != ids.size()) {
            throw new RuntimeException("部分记录不存在或已删除");
        }
        for (MonthlyInspectionRecord record : records) {
            Integer status = record.getRecordStatus();
            if (status == null || status != 4) {
                throw new RuntimeException("仅待提交状态的记录可删除，已提交的记录不允许删除");
            }
            // 阶段守卫：公示期内不允许直接删(须走删除审核)，公示期后禁止删除
            String phaseReason = checkPhaseAllowsMutation(record, true);
            if (phaseReason != null) {
                throw new RuntimeException(phaseReason);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecordFromEntryPage(String id) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        MonthlyInspectionRecord record = this.getById(id);
        if (record == null) {
            throw new RuntimeException("记录不存在");
        }
        Integer status = record.getRecordStatus();
        if (status == null || status == 3 || status == 4) {
            throw new RuntimeException("仅待整改/已整改/已复查状态的记录可删除");
        }
        // 阶段守卫：公示期内须走删除审核，公示期后禁止删除
        String phaseReason = checkPhaseAllowsMutation(record, true);
        if (phaseReason != null) {
            throw new RuntimeException(phaseReason);
        }
        assertCheckUnitOwner(record);
        // deleted 命中全局 logic-delete-field 配置，属于逻辑删除字段，
        // updateById 不会把它写进 SET，必须走 UpdateWrapper 显式 set
        LambdaUpdateWrapper<MonthlyInspectionRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(MonthlyInspectionRecord::getId, id)
                .set(MonthlyInspectionRecord::getDeleted, 1)
                .set(MonthlyInspectionRecord::getUpdateTime, LocalDateTime.now());
        return this.update(wrapper);
    }

    /**
     * 归属校验：只有录入该问题的管理部门（检查单位）能改/删这条记录。
     * <p>判定依据是"当前用户所属部门 == 记录的 checkUnitId"，且该部门必须是管理部门(dept_type=1)——
     * 运行部门(dept_type=0)即使自己录过自检记录也不放行。</p>
     * <p>月检管理员(is_monthly_admin=1)跨部门管理，豁免本校验。</p>
     */
    private void assertCheckUnitOwner(MonthlyInspectionRecord record) {
        SysUser currentUser = userContextUtil.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未认证");
        }
        if (Integer.valueOf(1).equals(currentUser.getIsMonthlyAdmin())) {
            return;
        }
        String myDeptId = currentUser.getDeptId();
        if (!StringUtils.hasText(myDeptId) || !myDeptId.equals(record.getCheckUnitId())) {
            throw new RuntimeException("仅录入该问题的管理部门可以操作这条记录");
        }
        SysDept dept = deptService.getById(myDeptId);
        if (dept == null || !Integer.valueOf(1).equals(dept.getDeptType())) {
            throw new RuntimeException("仅管理部门可以操作这条记录");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRecordStatus(String id, Integer recordStatus) {
        if (!StringUtils.hasText(id) || !isValidStatus(recordStatus)) {
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
        if (ids == null || ids.isEmpty() || !isValidStatus(recordStatus)) {
            return false;
        }
        return monthlyInspectionRecordMapper.batchUpdateStatus(ids, recordStatus) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitRecord(String id) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        // 只有草稿(0)状态才能提交
        MonthlyInspectionRecord record = this.getById(id);
        if (record == null || record.getRecordStatus() == null || record.getRecordStatus() != 0) {
            return false;
        }
        return updateRecordStatus(id, 1); // 1-已提交
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditRecord(String id) {
        return updateRecordStatus(id, 2); // 2-已审核
    }

    @Override
    public List<Map<String, Object>> getStatusStatistics() {
        return monthlyInspectionRecordMapper.selectStatusStatistics();
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
                                      String rectificationImages, String responsibleUnit, String responsiblePerson,
                                      String rectifierName) {
        if (!StringUtils.hasText(id)) {
            return false;
        }

        MonthlyInspectionRecord record = getById(id);
        if (record == null) {
            return false;
        }

        // 只有状态为"草稿"(0)的记录才能进行整改确认（PC端整改流程）
        // 状态流转：草稿(0) -> 已整改(1) -> 已复查(2)
        if (record.getRecordStatus() == null || record.getRecordStatus() != 0) {
            return false;
        }

        // 至少要有整改建议或整改效果，否则视为空整改
        boolean hasContent = StringUtils.hasText(rectificationSuggestion)
                || StringUtils.hasText(rectificationEffect);
        if (!hasContent) {
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
        // V3 起允许整改时调整责任单位/责任人(可选)
        if (StringUtils.hasText(responsibleUnit)) {
            record.setResponsibleUnit(responsibleUnit);
        }
        if (StringUtils.hasText(responsiblePerson)) {
            record.setResponsiblePerson(responsiblePerson);
        }
        record.setRectificationTime(LocalDateTime.now());
        // 整改人（确认整改时的当前登录用户）
        if (StringUtils.hasText(rectifierName)) {
            record.setRectifierName(rectifierName);
        }

        // 整改流程状态机：草稿(0) -> 已整改(1)，强制设为 1，不接受外部传入
        record.setRecordStatus(1);
        record.setUpdateTime(LocalDateTime.now());

        boolean updated = updateById(record);
        if (updated) {
            notifyAfterRectification(record);
            webSocketNotificationService.broadcastRecordStatusChange(
                    record.getId(), "monthly", 1, null);
        }
        return updated;
    }

    /**
     * 整改确认后推送通知：仅通知该问题的「检查人(inspector)」与「责任人(responsiblePerson)」。
     * inspector/responsiblePerson 以「、」分隔的姓名存储，兼容新格式「昵称(用户名)」与历史纯昵称/用户名，
     * 解析为对应用户后再推送；若均无法解析（历史脏数据），兜底通知记录创建人，避免通知完全丢失。
     * 通知通过 NotificationServiceImpl.batchCreateNotification 统一入库+WebSocket推送。
     */
    private void notifyAfterRectification(MonthlyInspectionRecord record) {
        try {
            String taskNo = record.getTaskNo() != null ? record.getTaskNo() : "";
            String title = "月检问题已整改，请安排复查";
            String content = String.format(
                    "任务[%s]的月检问题已提交整改，请尽快安排复查。问题描述：%s",
                    taskNo,
                    record.getProblemDescription() != null ? record.getProblemDescription() : ""
            );
            String actionUrl = "/monthly/record/detail?id=" + record.getId();

            java.util.Map<String, SysUser> targets = resolveNotifyTargets(record);
            if (targets.isEmpty()) {
                return;
            }

            List<Notification> notifications = new java.util.ArrayList<>();
            for (SysUser u : targets.values()) {
                notifications.add(buildNotification(
                        "RECTIFIED", title, content,
                        u.getId(),
                        u.getRealName() != null ? u.getRealName() : u.getUsername(),
                        u.getDeptId(), u.getDeptName(),
                        record.getTaskId(), taskNo, actionUrl));
            }
            if (!notifications.isEmpty()) {
                notificationService.batchCreateNotification(notifications);
            }
        } catch (Exception ex) {
            // 通知失败不影响主流程
            log.warn("发送整改通知失败 recordId={} : {}", record.getId(), ex.getMessage());
        }
    }

    /**
     * 复查确认后推送通知：仅通知该问题的「检查人(inspector)」与「责任人(responsiblePerson)」，告知问题已闭环。
     * 解析规则同整改通知；若均无法解析则兜底通知记录创建人。
     * 通知通过 NotificationServiceImpl.batchCreateNotification 统一入库+WebSocket推送。
     */
    private void notifyAfterReview(MonthlyInspectionRecord record) {
        try {
            String taskNo = record.getTaskNo() != null ? record.getTaskNo() : "";
            String title = "月检问题已完成复查";
            String content = String.format(
                    "任务[%s]的月检问题已复查完成，问题闭环。复查效果：%s",
                    taskNo,
                    record.getReviewEffect() != null ? record.getReviewEffect() : ""
            );
            String actionUrl = "/monthly/record/detail?id=" + record.getId();

            java.util.Map<String, SysUser> targets = resolveNotifyTargets(record);
            if (targets.isEmpty()) {
                return;
            }

            List<Notification> notifications = new java.util.ArrayList<>();
            for (SysUser u : targets.values()) {
                notifications.add(buildNotification(
                        "REVIEWED", title, content,
                        u.getId(),
                        u.getRealName() != null ? u.getRealName() : u.getUsername(),
                        u.getDeptId(), u.getDeptName(),
                        record.getTaskId(), taskNo, actionUrl));
            }
            notificationService.batchCreateNotification(notifications);
        } catch (Exception ex) {
            // 通知失败不影响主流程
            log.warn("发送复查通知失败 recordId={} : {}", record.getId(), ex.getMessage());
        }
    }

    /**
     * 计算整改/复查通知的目标用户：该问题的检查人(inspector) ∪ 责任人(responsiblePerson)。
     * 以用户ID去重；若两者均无法解析出用户（历史脏数据），兜底返回记录创建人。
     */
    private java.util.Map<String, SysUser> resolveNotifyTargets(MonthlyInspectionRecord record) {
        java.util.Map<String, SysUser> targets = new java.util.LinkedHashMap<>();
        try {
            List<SysUser> allUsers = userService.listEnabledUsers();
            for (SysUser u : resolveUsersByPersonNames(record.getInspector(), allUsers)) {
                targets.put(u.getId(), u);
            }
            for (SysUser u : resolveUsersByPersonNames(record.getResponsiblePerson(), allUsers)) {
                targets.put(u.getId(), u);
            }
        } catch (Exception ex) {
            log.warn("解析检查人/责任人失败 recordId={} : {}", record.getId(), ex.getMessage());
        }
        if (targets.isEmpty() && StringUtils.hasText(record.getCreateUserId())) {
            try {
                SysUser creator = userService.getById(record.getCreateUserId());
                if (creator != null && StringUtils.hasText(creator.getId())) {
                    targets.put(creator.getId(), creator);
                }
            } catch (Exception ex) {
                log.warn("查询记录创建人失败 userId={} : {}", record.getCreateUserId(), ex.getMessage());
            }
        }
        return targets;
    }

    /**
     * 将「检查人/责任人」姓名字符串解析为对应的启用用户列表。
     * 以「、,，」分隔；每个姓名兼容两种存储格式：
     *  - 新格式「昵称(用户名)」：优先按括号内用户名匹配，其次按昵称匹配；
     *  - 历史纯昵称或纯用户名：按 realName 或 username 匹配。
     * 每个姓名最多匹配一个用户，结果按用户ID去重。
     */
    private java.util.List<SysUser> resolveUsersByPersonNames(String personField, java.util.List<SysUser> allUsers) {
        java.util.List<SysUser> result = new java.util.ArrayList<>();
        if (!StringUtils.hasText(personField) || allUsers == null || allUsers.isEmpty()) {
            return result;
        }
        java.util.Set<String> addedIds = new java.util.HashSet<>();
        String[] tokens = personField.split("[、,，]");
        for (String raw : tokens) {
            if (raw == null) continue;
            String token = raw.trim();
            if (token.isEmpty()) continue;

            String realNamePart = token;
            String usernamePart = null;
            int lp = token.indexOf('(');
            int rp = token.lastIndexOf(')');
            if (lp < 0) {
                lp = token.indexOf('（');
                rp = token.lastIndexOf('）');
            }
            if (lp >= 0 && rp > lp) {
                realNamePart = token.substring(0, lp).trim();
                usernamePart = token.substring(lp + 1, rp).trim();
            }

            for (SysUser u : allUsers) {
                if (u == null || !StringUtils.hasText(u.getId())) continue;
                String uname = u.getUsername() == null ? "" : u.getUsername();
                String rname = u.getRealName() == null ? "" : u.getRealName();
                boolean matched = false;
                if (usernamePart != null && !usernamePart.isEmpty() && usernamePart.equalsIgnoreCase(uname)) {
                    matched = true;
                }
                if (!matched && !realNamePart.isEmpty() && realNamePart.equals(rname)) {
                    matched = true;
                }
                if (!matched && token.equalsIgnoreCase(uname)) {
                    matched = true;
                }
                if (!matched && token.equals(rname)) {
                    matched = true;
                }
                if (matched) {
                    if (addedIds.add(u.getId())) {
                        result.add(u);
                    }
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 构造一个标准化的 Notification 对象。
     */
    private Notification buildNotification(String type, String title, String content,
                                           String userId, String userName,
                                           String departmentId, String departmentName,
                                           String taskId, String taskNo, String actionUrl) {
        Notification notification = new Notification();
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setUserId(userId);
        notification.setUserName(userName);
        notification.setDepartmentId(departmentId);
        notification.setDepartmentName(departmentName);
        notification.setTaskId(taskId);
        notification.setTaskNo(taskNo);
        notification.setPriority("high");
        notification.setActionUrl(actionUrl);
        notification.setIsRead(false);
        notification.setCreateTime(LocalDateTime.now());
        return notification;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmReview(String id, String reviewSuggestion, String reviewEffect, String reviewImages,
                                 String responsibleUnit, String responsiblePerson, String reviewerName) {
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

        // 复查建议必填，避免空复查推进状态
        if (!StringUtils.hasText(reviewSuggestion) && !StringUtils.hasText(reviewEffect)) {
            return false;
        }

        // 更新复查信息
        record.setReviewSuggestion(reviewSuggestion);
        record.setReviewEffect(reviewEffect);
        record.setReviewImages(reviewImages);
        // V3 起允许复查时调整责任单位/责任人(可选)
        if (StringUtils.hasText(responsibleUnit)) {
            record.setResponsibleUnit(responsibleUnit);
        }
        if (StringUtils.hasText(responsiblePerson)) {
            record.setResponsiblePerson(responsiblePerson);
        }
        record.setReviewTime(LocalDateTime.now());
        // 复查人（确认复查时的当前登录用户）
        if (StringUtils.hasText(reviewerName)) {
            record.setReviewerName(reviewerName);
        }
        record.setRecordStatus(2); // 更新状态为"已复查"
        record.setUpdateTime(LocalDateTime.now());

        boolean updated = updateById(record);
        if (updated) {
            notifyAfterReview(record);
            webSocketNotificationService.broadcastRecordStatusChange(
                    record.getId(), "monthly", 2, null);
        }
        return updated;
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

        // 整改期限映射：前端表单用的是扩展字段 rectificationDeadline，落库列为 rectification_date
        if (record.getRectificationDeadline() != null) {
            record.setRectificationDate(record.getRectificationDeadline());
        }
    }

    /**
     * V3:编辑场景下构造 modification_snapshot JSON + 检测 nature_modified。
     *
     * 关键字段(对比原值):
     * - responsiblePerson / responsibleUnit / location / qualityId / qualityName
     * - departmentId / departmentName / checkUnitId / checkUnitName
     * - rectificationDate / inspector / problemDescription
     *
     * 如果前端已经传了 modificationSnapshot,优先信任前端(它已基于 issue->quality 联动自动检测过 nature_modified);
     * 否则本方法做兜底对比。
     */
    private void buildModificationSnapshotIfUpdate(MonthlyInspectionRecord newRecord) {
        if (!StringUtils.hasText(newRecord.getId())) {
            return;
        }
        MonthlyInspectionRecord old = this.getById(newRecord.getId());
        if (old == null) {
            return;
        }

        // nature_modified:前端已传则信任,否则根据 qualityId 差异兜底
        if (newRecord.getNatureModified() == null) {
            String oldQ = old.getQualityId();
            String newQ = newRecord.getQualityId();
            boolean qChanged = (oldQ == null && newQ != null) || (oldQ != null && !oldQ.equals(newQ));
            // 若 qualityName 也变化,视为改动
            String oldQn = old.getQualityName();
            String newQn = newRecord.getQualityName();
            boolean qnChanged = (oldQn == null && newQn != null) || (oldQn != null && !oldQn.equals(newQn));
            newRecord.setNatureModified((qChanged || qnChanged) ? 1 : 0);
        }

        // modification_snapshot:始终由后端基于 DB 旧值与本次新值 diff 重建,
        // 前端在 PUT 时若原样回传 GET 的 modificationSnapshot,这里会被新 diff 覆盖。
        java.util.LinkedHashMap<String, Object> snap = new java.util.LinkedHashMap<>();
        tryAddDiff(snap, "responsiblePerson", old.getResponsiblePerson(), newRecord.getResponsiblePerson());
        tryAddDiff(snap, "responsibleUnit", old.getResponsibleUnit(), newRecord.getResponsibleUnit());
        tryAddDiff(snap, "location", old.getLocation(), newRecord.getLocation());
        tryAddDiff(snap, "qualityId", old.getQualityId(), newRecord.getQualityId());
        tryAddDiff(snap, "qualityName", old.getQualityName(), newRecord.getQualityName());
        // departmentName 不在本表(仅 JOIN 展示),不参与 diff
        tryAddDiff(snap, "departmentId", old.getDepartmentId(), newRecord.getDepartmentId());
        tryAddDiff(snap, "checkUnitId", old.getCheckUnitId(), newRecord.getCheckUnitId());
        tryAddDiff(snap, "checkUnitName", old.getCheckUnitName(), newRecord.getCheckUnitName());
        tryAddDiff(snap, "rectificationDate", strOrEmpty(old.getRectificationDate()),
                newRecord.getRectificationDate() == null ? null : newRecord.getRectificationDate().toString());
        tryAddDiff(snap, "inspector", old.getInspector(), newRecord.getInspector());
        tryAddDiff(snap, "problemDescription", old.getProblemDescription(), newRecord.getProblemDescription());

        if (!snap.isEmpty()) {
            try {
                newRecord.setModificationSnapshot(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(snap));
            } catch (Exception ex) {
                org.slf4j.LoggerFactory.getLogger(MonthlyInspectionRecordServiceImpl.class)
                        .warn("构造 modification_snapshot 失败", ex);
            }
        }
    }

    private static void tryAddDiff(java.util.Map<String, Object> snap, String key, String oldVal, String newVal) {
        // V3:newVal 为 null 视为该字段未提交(前端表单没出现这个字段),不参与 diff
        // 避免 controller 用 containsKey 防御后,这里仍然把"没传"误判为"清空"
        if (newVal == null) {
            return;
        }
        boolean changed = (oldVal == null) || !oldVal.equals(newVal);
        if (!changed) {
            return;
        }
        java.util.LinkedHashMap<String, Object> pair = new java.util.LinkedHashMap<>();
        pair.put("from", oldVal != null ? oldVal : "");
        pair.put("to", newVal);
        snap.put(key, pair);
    }

    private static String strOrEmpty(Object o) {
        return o == null ? "" : o.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectRecord(String id) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        try {
            MonthlyInspectionRecord record = this.getById(id);
            if (record == null) {
                return false;
            }

            // 获取部门管理员
            List<SysUser> admins = userService.getAdminsByDeptId(record.getResponsibilityId());
            if (admins != null && !admins.isEmpty()) {
                // 批量创建通知
                List<Notification> notifications = new java.util.ArrayList<>();
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
                    notifications.add(notification);
                }
                // 批量插入通知
                notificationService.batchCreateNotification(notifications);
            }

            // 更新记录状态为待审核
            record.setRecordStatus(3); // 3-待审核
            record.setAuditStatus(0); // 0-未审核
            record.setRecordEditAuditType(0); // 0-回退审核
            record.setUpdateTime(LocalDateTime.now());

            return this.updateById(record);
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(MonthlyInspectionRecordServiceImpl.class)
                    .error("回退月检记录失败 id={}", id, e);
            throw new RuntimeException("回退月检记录失败：" + e.getMessage(), e);
        }
    }

    @Override
    public IPage<MonthlyInspectionRecord> getAuditRecords(long current, long size, String departmentId, Integer recordStatus,
                                                         Integer auditType, String modifyUserName,
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

        if (auditType != null) {
            // 过滤回退审核或编辑审核
            wrapper.eq(MonthlyInspectionRecord::getRecordEditAuditType, auditType);
        }

        if (StringUtils.hasText(modifyUserName)) {
            wrapper.like(MonthlyInspectionRecord::getModifyUserName, modifyUserName.trim());
        }

        // 与 getMonthlyRecordCount 保持一致的日期解析：yyyy-MM-dd HH:mm:ss
        if (StringUtils.hasText(startDate)) {
            try {
                LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                wrapper.ge(MonthlyInspectionRecord::getCreateTime, start);
            } catch (Exception e) {
                // 日期格式错误，忽略此条件
            }
        }

        if (StringUtils.hasText(endDate)) {
            try {
                LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                wrapper.le(MonthlyInspectionRecord::getCreateTime, end);
            } catch (Exception e) {
                // 日期格式错误，忽略此条件
            }
        }

        wrapper.orderByDesc(MonthlyInspectionRecord::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmAudit(String id, String auditorId, String auditReason) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        try {
            MonthlyInspectionRecord record = this.getById(id);
            if (record == null) {
                return false;
            }

            // 只有待审核(3)状态才能审核通过
            if (record.getRecordStatus() == null || record.getRecordStatus() != 3) {
                return false;
            }

            // 审核成功，记录变更为已提交状态（4）
            record.setRecordStatus(4); // 4-已确认
            record.setAuditStatus(1); // 1-已审核
            record.setAuditReason(auditReason);
            record.setAuditorId(auditorId);
            record.setAuditTime(LocalDateTime.now());

            // 保留整改图片、整改建议、复查图片、复查建议等数据，用于追溯
            record.setUpdateTime(LocalDateTime.now());

            return this.updateById(record);
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(MonthlyInspectionRecordServiceImpl.class)
                    .error("审核月检记录失败 id={}", id, e);
            throw new RuntimeException("审核月检记录失败：" + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectAudit(String id) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
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
            org.slf4j.LoggerFactory.getLogger(MonthlyInspectionRecordServiceImpl.class)
                    .error("拒绝审核失败 id={}", id, e);
            throw new RuntimeException("拒绝审核失败：" + e.getMessage(), e);
        }
    }

    // ============== 编辑审核相关逻辑 ==============

    /**
     * 提交编辑审核：申请人发起，记录进入待审核状态，audit_type=1（向后兼容的 4 参重载）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitEditForAudit(String id, String modifyDescription, String modifyUserId, String modifyUserName) {
        return submitEditForAudit(id, modifyDescription, modifyUserId, modifyUserName, 1, null);
    }

    /**
     * 提交编辑/回退审核(向后兼容 5 参重载)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitEditForAudit(String id, String modifyDescription, String modifyUserId, String modifyUserName, Integer auditType) {
        return submitEditForAudit(id, modifyDescription, modifyUserId, modifyUserName, auditType, null);
    }

    /**
     * 提交编辑/回退审核：申请人发起，记录进入待审核状态。
     * @param auditType 0=回退申请（仅 phase=3 公示期允许），1=编辑申请
     * @param pendingEdit 申请人提交的新字段值（仅 auditType=1 时使用）。后端基于此构造
     *                    modification_snapshot JSON，审核通过时由 confirmEditAudit 应用。
     *                    该对象**不直接保存**到记录字段上——记录本身保持原值不变。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitEditForAudit(String id, String modifyDescription, String modifyUserId, String modifyUserName,
                                       Integer auditType, MonthlyInspectionRecord pendingEdit) {
        if (!StringUtils.hasText(id)) {
            throw new RuntimeException("记录ID不能为空");
        }
        if (!StringUtils.hasText(modifyDescription) || modifyDescription.trim().length() < 5) {
            throw new RuntimeException("修改说明至少 5 个字符");
        }

        MonthlyInspectionRecord record = this.getById(id);
        if (record == null) {
            throw new RuntimeException("记录不存在");
        }

        // 待审核中的记录不允许重复提交审核申请（否则会覆盖 pre_edit_status）
        Integer recordStatus = record.getRecordStatus();
        if (recordStatus != null && recordStatus == 3) {
            throw new RuntimeException("记录处于待审核状态，不可重复提交审核申请");
        }

        // 当前必须处于公示期或月检录入期内：录入期内可自由编辑，公示期内必须审核
        String reason = checkEditable(record);
        if (reason != null && !reason.contains("审核")) {
            throw new RuntimeException(reason);
        }

        // 审核类型：0=回退申请 1=编辑申请 2=删除申请
        int type = auditType != null ? auditType : 1;
        if (type != 0 && type != 1 && type != 2) {
            throw new RuntimeException("未知的审核类型");
        }

        // 回退申请仅允许在公示期内发起
        if (type == 0) {
            if (!StringUtils.hasText(record.getTaskId())) {
                throw new RuntimeException("记录未关联任务，无法判定公示期阶段");
            }
            MonthlyInspectionTask task = monthlyInspectionTaskService.getTaskDetail(record.getTaskId());
            if (task == null) {
                throw new RuntimeException("关联任务不存在");
            }
            int phase = task.currentPhase(LocalDateTime.now());
            if (phase != 3) {
                throw new RuntimeException("仅公示期内可申请回退");
            }
        }

        // 删除申请仅允许在公示期内发起
        if (type == 2) {
            if (!StringUtils.hasText(record.getTaskId())) {
                throw new RuntimeException("记录未关联任务，无法判定公示期阶段");
            }
            MonthlyInspectionTask task = monthlyInspectionTaskService.getTaskDetail(record.getTaskId());
            if (task == null) {
                throw new RuntimeException("关联任务不存在");
            }
            if (task.currentPhase(LocalDateTime.now()) != 3) {
                throw new RuntimeException("仅公示期内可申请删除");
            }
        }

        // 归属校验：受检方(运行部门)不能对记录发起修改申请——
        // 编辑申请(1)、删除申请(2)、回退申请(0)一律只允许录入该问题的管理部门发起。
        // status=4 是月检提交页的记录，维持原有规则不动；
        // status 0/1/2 来自月检记录录入页。
        if (recordStatus != null && recordStatus != 4) {
            assertCheckUnitOwner(record);
        }

        // V3:编辑申请(auditType=1)且前端传了 pendingEdit 时,计算原值/修改值 diff 存到 modification_snapshot
        // 关键:diff 构造在临时对象上进行,只把 snapshot 字符串写回原 record,原 record 的字段值不变
        if (type == 1 && pendingEdit != null) {
            pendingEdit.setId(id);
            buildModificationSnapshotIfUpdate(pendingEdit);
            String snap = pendingEdit.getModificationSnapshot();
            if (StringUtils.hasText(snap)) {
                record.setModificationSnapshot(snap);
            }
            // nature_modified 同步:有人为改动则置 1
            if (pendingEdit.getNatureModified() != null) {
                record.setNatureModified(pendingEdit.getNatureModified());
            }
        }

        // V3:记录提交审核前的原状态,审核通过/拒绝后恢复到该值
        Integer preEditStatus = record.getRecordStatus();
        record.setRecordStatus(3); // 3-待审核
        record.setAuditStatus(0);
        record.setRecordEditAuditType(type); // 0-回退审核 / 1-编辑审核
        record.setModifyDescription(modifyDescription.trim());
        record.setModifyUserId(modifyUserId);
        record.setModifyUserName(modifyUserName);
        record.setModifyTime(LocalDateTime.now());
        if (preEditStatus != null) {
            record.setPreEditStatus(preEditStatus);
        }
        record.setUpdateTime(LocalDateTime.now());

        // 通知有月检编辑审核权限的人
        try {
            notifyAuditUsers(record);
        } catch (Exception ex) {
            org.slf4j.LoggerFactory.getLogger(MonthlyInspectionRecordServiceImpl.class)
                    .warn("发送审核通知失败 id={}", id, ex);
        }

        return this.updateById(record);
    }

    /**
     * 撤回记录：公示期前(phase=1/2)直接将 status 0/1/2 改回 4（待提交），无需审核、不发通知。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean withdrawRecord(String id) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        MonthlyInspectionRecord record = this.getById(id);
        if (record == null) {
            throw new RuntimeException("记录不存在");
        }
        Integer status = record.getRecordStatus();
        if (status == null || (status != 0 && status != 1 && status != 2)) {
            throw new RuntimeException("仅待整改/已整改/已复查状态的记录可回退");
        }
        if (!StringUtils.hasText(record.getTaskId())) {
            throw new RuntimeException("记录未关联任务，无法判定公示期阶段");
        }
        MonthlyInspectionTask task = monthlyInspectionTaskService.getTaskDetail(record.getTaskId());
        if (task == null) {
            throw new RuntimeException("关联任务不存在");
        }
        int phase = task.currentPhase(LocalDateTime.now());
        if (phase != 1 && phase != 2) {
            throw new RuntimeException("仅录入期/公示前可自由回退，公示期内请使用申请回退");
        }

        record.setRecordStatus(4); // 4-待提交
        record.setAuditStatus(0);
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    /**
     * 编辑审核通过：审核人确认。
     * <p>若记录上挂有 modification_snapshot(由 submitEditForAudit 写入),把其中每条
     * from→to 变更应用到原 record 字段上,然后清空 snapshot;否则保持原 V2 行为(status=4)。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmEditAudit(String id, String auditorId, String auditReason) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        MonthlyInspectionRecord record = this.getById(id);
        if (record == null) {
            return false;
        }
        if (record.getRecordStatus() == null || record.getRecordStatus() != 3) {
            return false;
        }
        Integer editAuditType = record.getRecordEditAuditType();
        if (editAuditType == null || (editAuditType != 1 && editAuditType != 2)) {
            return false;
        }

        // 删除审核(2):审核通过即逻辑删除记录,不应用字段 diff
        if (editAuditType == 2) {
            // deleted 命中全局 logic-delete-field 配置,属于逻辑删除字段,
            // updateById 不会把它写进 SET,必须走 UpdateWrapper 显式 set(与 batchDeleteRecords 同构)
            LambdaUpdateWrapper<MonthlyInspectionRecord> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(MonthlyInspectionRecord::getId, id)
                    .set(MonthlyInspectionRecord::getDeleted, 1)
                    .set(MonthlyInspectionRecord::getRecordStatus, 2) // 2-已完成
                    .set(MonthlyInspectionRecord::getAuditStatus, 1)  // 1-已审核
                    .set(MonthlyInspectionRecord::getAuditReason, auditReason)
                    .set(MonthlyInspectionRecord::getAuditorId, auditorId)
                    .set(MonthlyInspectionRecord::getAuditTime, LocalDateTime.now())
                    .set(MonthlyInspectionRecord::getUpdateTime, LocalDateTime.now());
            return this.update(wrapper);
        }

        // V3:有 modification_snapshot → 应用 diff 到原字段(生效用户更新值),清空 snapshot
        if (StringUtils.hasText(record.getModificationSnapshot())) {
            applyModificationSnapshot(record);
        }

        // V3:恢复到提交审核前的原状态(0/1/2);若 preEditStatus 为空(旧数据/边界情况),fallback 到 4
        Integer restored = record.getPreEditStatus();
        record.setRecordStatus(restored != null ? restored : 4);
        record.setPreEditStatus(null); // 清空,避免重复恢复
        record.setAuditStatus(1);  // 1-已审核
        record.setAuditReason(auditReason);
        record.setAuditorId(auditorId);
        record.setAuditTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    /**
     * 编辑审核拒绝：审核人拒绝，编辑申请作废,记录回到原状态。
     * V3:同时清空 modification_snapshot,确保被拒绝的 diff 不会以任何方式生效。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectEditAudit(String id, String auditReason) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        MonthlyInspectionRecord record = this.getById(id);
        if (record == null) {
            return false;
        }
        if (record.getRecordStatus() == null || record.getRecordStatus() != 3) {
            return false;
        }
        Integer editAuditType = record.getRecordEditAuditType();
        if (editAuditType == null || (editAuditType != 1 && editAuditType != 2)) {
            return false;
        }
        // V3:恢复到提交审核前的原状态;若 preEditStatus 为空(旧数据),fallback 到 1
        Integer restored = record.getPreEditStatus();
        record.setRecordStatus(restored != null ? restored : 1);
        record.setPreEditStatus(null); // 清空,避免重复恢复
        record.setAuditStatus(0);
        record.setAuditReason(auditReason);
        // V3:清空 modification_snapshot(若存在),被拒绝的 diff 不应保留
        if (StringUtils.hasText(record.getModificationSnapshot())) {
            record.setModificationSnapshot(null);
        }
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    /**
     * V3:把 modification_snapshot JSON 里的 from→to 变更应用到 record 字段上,然后清空 snapshot。
     * 修改字段白名单:与 buildModificationSnapshotIfUpdate 中写入的 key 保持一致。
     */
    private void applyModificationSnapshot(MonthlyInspectionRecord record) {
        String snapshot = record.getModificationSnapshot();
        if (!StringUtils.hasText(snapshot)) {
            return;
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.util.Map<String, java.util.Map<String, String>> snap = mapper.readValue(
                    snapshot,
                    new com.fasterxml.jackson.core.type.TypeReference<java.util.Map<String, java.util.Map<String, String>>>() {});
            snap.forEach((key, pair) -> {
                String to = pair.get("to");
                switch (key) {
                    case "responsiblePerson": record.setResponsiblePerson(to); break;
                    case "responsibleUnit": record.setResponsibleUnit(to); break;
                    case "location": record.setLocation(to); break;
                    case "qualityId": record.setQualityId(to); break;
                    case "qualityName": record.setQualityName(to); break;
                    case "departmentId": record.setDepartmentId(to); break;
                    case "checkUnitId": record.setCheckUnitId(to); break;
                    case "checkUnitName": record.setCheckUnitName(to); break;
                    case "rectificationDate":
                        // snapshot 中以 String 存储,需转回 LocalDate
                        if (to != null && !to.isEmpty()) {
                            try {
                                record.setRectificationDate(java.time.LocalDate.parse(to));
                            } catch (Exception ex) {
                                org.slf4j.LoggerFactory.getLogger(MonthlyInspectionRecordServiceImpl.class)
                                        .warn("rectificationDate 解析失败: {}", to);
                            }
                        } else {
                            record.setRectificationDate(null);
                        }
                        break;
                    case "inspector": record.setInspector(to); break;
                    case "problemDescription": record.setProblemDescription(to); break;
                    default:
                        // 未知字段忽略(白名单控制)
                }
            });
            // 应用后清空 snapshot,避免重复应用
            record.setModificationSnapshot(null);
        } catch (Exception ex) {
            org.slf4j.LoggerFactory.getLogger(MonthlyInspectionRecordServiceImpl.class)
                    .warn("应用 modification_snapshot 失败 id={}", record.getId(), ex);
        }
    }

    /**
     * 判断当前是否可编辑/可删除指定记录
     * @return null 表示可操作；否则返回原因说明
     */
    @Override
    public String checkEditable(MonthlyInspectionRecord record) {
        if (record == null) {
            return "记录不存在";
        }
        // 已完成（2）不允许编辑
        if (record.getRecordStatus() != null && record.getRecordStatus() == 2) {
            return "已复查的记录不可编辑或删除";
        }
        // 待审核中不可编辑
        if (record.getRecordStatus() != null && record.getRecordStatus() == 3) {
            return "记录处于待审核状态，不可编辑或删除";
        }
        // 待提交(4):V3 语义下 4=待提交(月检提交页的记录),允许编辑/删除;
        // 阶段限制由下面的任务阶段判定承担
        return checkPhaseAllowsMutation(record, false);
    }

    /**
     * 任务阶段守卫：拦截绕过前端直接调用编辑/删除接口的请求。
     * 阶段语义与前端 admin/src/utils/taskPhase.js 的 computeTaskPhase 一致：
     * 1=录入期、2=录入结束到公示前、3=公示期、0=其余(已过公示期或任务时间未配置)。
     *
     * @param record   目标记录
     * @param forDelete true=删除场景，false=编辑场景（文案不同）
     * @return null 表示允许操作；否则返回拒绝原因
     */
    private String checkPhaseAllowsMutation(MonthlyInspectionRecord record, boolean forDelete) {
        if (record == null || !StringUtils.hasText(record.getTaskId())) {
            return null; // 无任务关联的历史数据不做阶段限制
        }
        MonthlyInspectionTask task = monthlyInspectionTaskService.getTaskDetail(record.getTaskId());
        if (task == null) {
            return null; // 任务不存在，不做阶段限制
        }
        int phase = task.currentPhase(LocalDateTime.now());
        if (phase == 1 || phase == 2) {
            return null; // 录入期/公示前：可自由编辑删除
        }
        if (phase == 3) {
            return forDelete
                    ? "当前处于公示期内，删除记录需提交删除申请等待审核"
                    : "当前处于公示期内，编辑记录需提交修改说明等待审核";
        }
        return "已超过公示期，记录不可编辑或删除";
    }

    /**
     * 判断当前是否处于月检任务的录入期内
     */
    @Override
    public boolean isInInspectionPeriod(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            return false;
        }
        MonthlyInspectionTask task = monthlyInspectionTaskService.getTaskDetail(taskId);
        if (task == null) {
            return false;
        }
        return task.isInInspectionPeriod(LocalDateTime.now());
    }

    /**
     * 给具备月检编辑审核权限的用户发送通知
     */
    private void notifyAuditUsers(MonthlyInspectionRecord record) {
        try {
            List<SysUser> auditors = userService.listEnabledUsers();
            if (auditors == null || auditors.isEmpty()) {
                return;
            }
            // 仅通知 auditPermission=1 的用户（具有月检审核权限的管理员）
            List<Notification> notifications = new java.util.ArrayList<>();
            for (SysUser u : auditors) {
                if (u == null || u.getAuditPermission() == null || u.getAuditPermission() != 1) {
                    continue;
                }
                Notification notification = new Notification();
                notification.setType("AUDIT");
                notification.setTitle("待审核的月检记录（编辑审核）");
                notification.setContent("用户 " + (record.getModifyUserName() != null ? record.getModifyUserName() : "")
                        + " 申请编辑月检记录，修改说明：" + record.getModifyDescription());
                notification.setUserId(u.getId());
                notification.setUserName(u.getRealName() != null ? u.getRealName() : u.getUsername());
                notification.setDepartmentId(record.getResponsibilityId());
                notification.setIsRead(false);
                notification.setCreateTime(LocalDateTime.now());
                notification.setPriority("high");
                notification.setActionUrl("/monthly/audit?recordId=" + record.getId());
                notifications.add(notification);
            }
            if (!notifications.isEmpty()) {
                notificationService.batchCreateNotification(notifications);
            }
        } catch (Exception ex) {
            org.slf4j.LoggerFactory.getLogger(MonthlyInspectionRecordServiceImpl.class)
                    .warn("发送编辑审核通知失败", ex);
        }
    }
}