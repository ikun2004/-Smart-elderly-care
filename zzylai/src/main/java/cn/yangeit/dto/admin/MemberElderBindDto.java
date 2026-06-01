package cn.yangeit.dto.admin;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 客户老人关联实体类
 */
@Data
@Builder
public class MemberElderBindDto{

    @Schema(description= "用户id")
    private Long memberId;

    @Schema(description= "老人id")
    private Long elderId;

    @Schema(description= "称呼，如义父")
    private String remark;

}

