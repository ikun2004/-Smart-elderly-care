package cn.yangeit.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReservationAdminDTO{
    @Schema(description = "预约ID")
    private Long id;

    @Schema(description = "预约状态，0：待报道，1：已完成，2：取消，3：过期")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    // 可根据实际需求添加更多字段
} 