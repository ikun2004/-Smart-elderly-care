package cn.yangeit.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class NursingProjectAdminDTO{
    @Schema(description = "护理项目ID")
    private Long id;
    @Schema(description = "项目名称",  example = "足浴")
    private String name;

    @Schema(description = "排序",  example = "1")
    private Integer orderNo;

    @Schema(description = "单位",  example = "次")
    private String unit;
    @Schema(description = "项目图片",  example = "https://example.com/project.jpg")
    private String image;
    @Schema(description = "护理要求",  example = "无")
    private String nursingRequirement;
    @Schema(description = "备注",  example = "无")
    private String remark;
    @Schema(description = "价格",  example = "50.00")
    private BigDecimal price;
    @Schema(description = "状态:0禁止，1启用",  example = "1")
    private Integer status;

    // 可根据实际需求添加更多字段
} 