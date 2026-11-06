package com.gjxt.inspection.task;

import com.gjxt.inspection.service.ISysInspectionRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 巡检相关定时任务
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Slf4j
@Component
public class InspectionTask {

    @Autowired
    private ISysInspectionRecordService inspectionRecordService;

    /**
     * 延迟整改检查任务
     * 每天凌晨1点执行，检查超过整改期限但未整改的记录
     */
    @Scheduled(cron = "${system.task.delay-check-cron}")
    public void checkDelayedRectification() {
        log.info("延迟整改检查任务开始执行");
        try {
            boolean result = inspectionRecordService.handleDelayedRectification();
            log.info("延迟整改检查任务执行完成，结果: {}", result);
        } catch (Exception e) {
            log.error("延迟整改检查任务执行失败", e);
        }
    }

}
