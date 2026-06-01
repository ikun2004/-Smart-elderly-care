package cn.yangeit.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderVo {
    private Long id;//  订单编号
    private Integer status;//  订单状态
    private BigDecimal amount;//  订单金额
    private LocalDateTime createTime;//   期望到达时间
    private String paymentStatus;//   支付状态
    private String serviceName;//  服务名称
    private String userName;//  老人名称
    private String remark;//  订单备注
    private String orderNo;//  订单编号
    private String image;//  图片
}
