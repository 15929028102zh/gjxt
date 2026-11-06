package com.gjxt.inspection.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;

@Data
public class SysInspectionRecordSubmitData {
    private String responsibleUnit;
    private String responsiblePerson;
    private String natureDefinition;
    private String scoreId;
    private BigDecimal safetyScore;
    private String professionId;
    private String problemDescription;
    private String location;
    private String inspector;
    private String departmentId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rectificationDeadline;
    private String problemImageUrls;
    private String imageUrls; // 前端传递的字段名
    private String reporterId;
    private Integer status;
    private String assessmentSituation;
}