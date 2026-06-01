package cn.yangeit.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "老人更新参数")
public class ElderAdminDTO{
    @Schema(description = "老人ID")
    private Long id;

    @Schema(description = "姓名",example = "张三")
    private String name;

    @Schema(description = "身份证",example = "123456789012345678")
    private String idCardNo;

    @Schema(description = "手机号",example = "13888888888")
    private String phone;

    @Schema(description = "头像",example = "https://example.com/avatar.jpg")
    private String image;

    @Schema(description = "出生日期",example = "1990-01-01")
    private String birthday;

    @Schema(description = "状态",example = "1")
    private Integer status;

    /**
     * 性别（0:女  1:男）
     */
    @Schema(description = "性别 0女，1男",example = "1")
    private Integer sex;

    /**
     * 家庭住址
     */
    @Schema(description = "家庭住址",example = "中国")
    private String address;


    // 可根据实际需求添加更多字段
} 