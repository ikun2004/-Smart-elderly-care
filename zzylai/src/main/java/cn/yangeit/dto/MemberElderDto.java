package cn.yangeit.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 客户老人关联实体类
 */
@Data
@Builder

public class MemberElderDto {

    /**
     * 姓名
     */
    @Schema(description = "姓名", example = "张三",required = true)
    private String name;

    /**
     * 身份证号
     */
    @Schema(description = "身份证号", example = "123456789012345678",required = true)
    private String idCard;

    /**
     * 备注
     */
    @Schema(description = "备注", example = "无")
    private String remark;

}

