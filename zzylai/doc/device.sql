-- 如果device表存在，就删除这个表
DROP TABLE IF EXISTS `device`;
-- 创建设备表
CREATE TABLE `device` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                          `secret` varchar(50) DEFAULT NULL COMMENT '设备秘钥',
                          `iot_id` varchar(255) DEFAULT NULL COMMENT '物联网设备ID',
                          `binding_location` varchar(50) DEFAULT NULL COMMENT '绑定位置',
                          `location_type` int(11) DEFAULT NULL COMMENT '位置类型 0：随身设备 1：固定设备',
                          `physical_location_type` int(11) DEFAULT NULL COMMENT '物理位置类型 0楼层 1房间 2床位',
                          `device_name` varchar(100) DEFAULT NULL COMMENT '设备名称',
                          `nickname` varchar(100) DEFAULT NULL COMMENT '备注名称',
                          `product_key` varchar(100) DEFAULT NULL COMMENT '产品key',
                          `product_name` varchar(100) DEFAULT NULL COMMENT '产品名称',
                          `device_description` varchar(255) DEFAULT NULL COMMENT '位置备注',
                          `have_entrance_guard` int(11) NOT NULL DEFAULT '0' COMMENT '产品是否包含门禁，0：否，1：是',
                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                          `create_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
                          `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
                          `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                          `node_id` varchar(50) DEFAULT NULL COMMENT '节点id',
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE KEY `binding_location_location_type_physical_location_type_product_id` (`binding_location`,`location_type`,`physical_location_type`,`product_key`) USING BTREE,
                          KEY `device_id` (`iot_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=234 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 如果device_data表存在，就删除这个表
DROP TABLE IF EXISTS `device_data`;
-- 创建设备数据表

CREATE TABLE `device_data` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '告警规则ID，自增主键',
                               `device_name` varchar(255) DEFAULT NULL COMMENT '设备名称',
                               `iot_id` varchar(255) DEFAULT NULL COMMENT '设备ID',
                               `nickname` varchar(255) DEFAULT NULL COMMENT '备注名称',
                               `product_key` varchar(500) DEFAULT NULL COMMENT '所属产品的key',
                               `product_name` varchar(500) DEFAULT NULL COMMENT '产品名称',
                               `function_id` varchar(255) DEFAULT NULL COMMENT '功能名称',
                               `access_location` varchar(255) DEFAULT NULL COMMENT '接入位置',
                               `location_type` int(11) DEFAULT NULL COMMENT '位置类型 0：随身设备 1：固定设备',
                               `physical_location_type` int(11) DEFAULT NULL COMMENT '物理位置类型 0楼层 1房间 2床位',
                               `device_description` varchar(255) DEFAULT NULL COMMENT '位置备注',
                               `data_value` varchar(50) DEFAULT NULL COMMENT '数据值',
                               `alarm_time` datetime DEFAULT NULL COMMENT '数据上报时间',
                               `create_time` datetime NOT NULL COMMENT '创建时间',
                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                               `create_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
                               `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
                               `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                               PRIMARY KEY (`id`) USING BTREE,
                               KEY `iot_id` (`iot_id`) USING BTREE,
                               KEY `function_name` (`function_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=39876 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

