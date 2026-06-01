package cn.yangeit.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data

public class HealthAssessmentDto{

    /**
     * 老人姓名
     */
    @Schema(description = "老人姓名",required = true)
    private String elderName;

    /**
     * 身份证号
     */
    @Schema(description = "身份证号",required = true)
    private String idCard;

    /**
     * 体检机构
     */
    @Schema(description = "体检机构",required = true)
    private String physicalExamInstitution;

    /**
     * 体检报告URL链接
     */
    @Schema(description = "体检报告URL链接",required = true)
    private String physicalReportUrl;

}