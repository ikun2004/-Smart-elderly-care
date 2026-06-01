-- 如果存在这个表，就删除它
DROP TABLE IF EXISTS `np_order`;
-- 创建订单表
CREATE TABLE `np_order` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                            `trading_order_no` bigint(20) DEFAULT '0' COMMENT '交易系统订单号',
                            `payment_status` tinyint(4) DEFAULT NULL COMMENT '付款状态,1.未付 2已付 3已关闭',
                            `amount` decimal(32,2) DEFAULT NULL COMMENT '金额',
                            `refund` decimal(12,2) DEFAULT '0.00' COMMENT '退款金额【付款后，单位：元',
                            `is_refund` varchar(32) DEFAULT NULL COMMENT '是否有退款：YES，NO',
                            `member_id` bigint(20) DEFAULT NULL COMMENT '客户id',
                            `project_id` bigint(20) DEFAULT NULL COMMENT '服务项目id',
                            `elder_id` bigint(20) DEFAULT NULL COMMENT '服务对象ID',
                            `estimated_arrival_time` datetime DEFAULT NULL COMMENT '预计服务时间',
                            `mark` varchar(2000) DEFAULT NULL COMMENT '备注',
                            `reason` varchar(2000) DEFAULT NULL COMMENT '取消原因',
                            `status` int(11) DEFAULT NULL COMMENT '订单状态 0待支付 1待执行 2已执行 3已完成 4已关闭 5已退款',
                            `create_time` datetime NOT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `create_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
                            `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
                            `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                            `view_status` varchar(255) DEFAULT '0' COMMENT '是否可见 0可见 1不可见',
                            `order_no` varchar(50) DEFAULT NULL COMMENT '订单编号',
                            `o_create_type` tinyint(4) DEFAULT NULL COMMENT '取消人类型 1前台 2后台',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=320 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='订单';
