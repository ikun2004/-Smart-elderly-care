package cn.yangeit.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReFundOrderDto {
    private String productOrderNo;//订单编号
    private String tradingOrderNo;//支付编号
    private String tradingChannel;//取消原因
}
