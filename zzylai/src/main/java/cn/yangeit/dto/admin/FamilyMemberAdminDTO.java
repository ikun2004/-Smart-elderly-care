package cn.yangeit.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FamilyMemberAdminDTO{
    @Schema(description = "用户id",example = "11")
    private Long id;

    @Schema(description = "姓名",example = "张三")
    private String name;

    @Schema(description = "手机号",example = "13888888888")
    private String phone;

    @Schema(description = "头像",example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "openId",example = "sdfghjkasdfgbn")
    private String openId;

    @Schema(description = "性别 0男，1女",example = "0")
    private Integer gender;

    @Schema(description = "备注",example = "无")
    private String remark;

    // 可根据实际需求添加更多字段
} 