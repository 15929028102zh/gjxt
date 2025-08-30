package com.gjxt.inspection.task;

import com.gjxt.inspection.service.ISysInspectionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 巡检相关定时任务
 *
 * @author gjxt
 * @since 2024-01-01
 */
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
        try {
            boolean result = inspectionRecordService.handleDelayedRectification();
            if (result) {            } else {            }
        } catch (Exception e) {        }    }

}