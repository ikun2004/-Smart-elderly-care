package cn.yangeit.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 */
@TableName(value = "np_order") // 表名
@Data
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id; // 订单id
    private Long tradingOrderNo; // 交易订单号
    private Integer paymentStatus; // 付款状态,1.未付 2已付 3已关闭
    private BigDecimal amount; // 金额
    private BigDecimal refund; // 退款金额【付款后，单位：元】
    private String isRefund; // 是否退款【0未退款 1已退款】
    private Long memberId; // 客户ID
    private Long projectId; // 服务项目id
    private Long elderId; // 服务对象ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedArrivalTime; // 预计到达时间
    private String mark; //
    private String reason; // 取消原因
    private Integer status; // 订单状态 0待支付 1待执行 2已执行 3已完成 4已关闭 5已退款
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private Long createBy;
    private Long updateBy;
    private String remark; // 下单备注
    private String viewStatus; // 是否可见 0可见 1不可见
    private String orderNo; // 订单编号
    private Integer oCreateType; // 创建订单的方式 1用户自行创建 2后台创建

    // 其他字段和方法
}