package cn.yangeit.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderAdminDTO{
    @Schema(description = "订单ID",  example = "1")
    private Long id;//  订单id


    @Schema(description = "订单状态0待支付 1待执行 2已执行 3已完成 4已关闭 5已退款",  example = "1")
    private Integer status;//订单状态 0待支付 1待执行 2已执行 3已完成 4已关闭 5已退款


}