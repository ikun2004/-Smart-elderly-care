-- 创建一个编码utf8mb4，排序规则是utf8mb4_general_ci 的名字叫zzylai的数据库，然后使用数据库
create database zzylai character set utf8mb4 collate utf8mb4_general_ci;

use zzylai;



SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for elder
-- ----------------------------
DROP TABLE IF EXISTS `elder`;
CREATE TABLE `elder`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  `id_card_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `sex` int(11) NULL DEFAULT NULL COMMENT '性别（0:女  1:男）',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '状态（0：禁用，1:启用  2:请假 3:退住中 4入住中 5已退住）',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `birthday` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出生日期',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `id_card_national_emblem_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证国徽面',
  `id_card_portrait_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证人像面',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `bed_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '床位编号',
  `bed_id` bigint(20) NULL DEFAULT NULL COMMENT '床位id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_id_card_no`(`name`, `id_card_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '老人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of elder
-- ----------------------------
INSERT INTO `elder` VALUES (1, '刘备', 'https://itheim.oss-cn-beijing.aliyuncs.com/20279fd4-3d4f-4e00-8a52-ac33d6c37a55.png', '110123195001016688', 1, 1, '13654553321', '1950-01-01', '河北省涿州市', 'https://itheim.oss-cn-beijing.aliyuncs.com/1f0e0696-7d6b-49fd-9777-b2aca8f84ae6.jpg', 'https://itheim.oss-cn-beijing.aliyuncs.com/97efeee6-6c16-49c5-bdec-197fc3522da4.jpg', '2024-09-27 17:42:52', NULL, 1, NULL, NULL, '101-1', 1);
INSERT INTO `elder` VALUES (2, '关羽', 'https://itheim.oss-cn-beijing.aliyuncs.com/9368029c-3071-4713-9ac9-9d6842048bed.png', '110123195101016688', 1, 1, '13754553321', '1951-01-01', '山西省运城市', 'https://itheim.oss-cn-beijing.aliyuncs.com/d9bb2fbc-2ed0-443a-8ad1-892d0a16a5a8.jpg', 'https://itheim.oss-cn-beijing.aliyuncs.com/7717c84f-d37a-4120-bb07-bde43e23cfcb.jpg', '2024-09-27 17:45:23', NULL, 1, NULL, NULL, '101-2', 170);
INSERT INTO `elder` VALUES (3, '张飞', 'https://itheim.oss-cn-beijing.aliyuncs.com/6528514b-e7db-468d-8aeb-590d3102125b.png', '110123195201016688', 1, 1, '13854553321', '1952-01-01', '河北省保定市', 'https://itheim.oss-cn-beijing.aliyuncs.com/dbbdba23-60bd-47a7-8e7c-5b7abfff00df.jpg', 'https://itheim.oss-cn-beijing.aliyuncs.com/641c4c20-2d38-4af4-8927-b459a8eba808.jpg', '2024-09-27 17:47:59', NULL, 1, NULL, NULL, '303-1', 25);
INSERT INTO `elder` VALUES (6, '刘爱国', 'https://itheim.oss-cn-beijing.aliyuncs.com/987545b5-0a35-4fff-9c81-01405799840c.png', '132112196712137788', 0, 1, '156165', '1967-12-13 00:00:00', 'xdfsd ', 'https://itheim.oss-cn-beijing.aliyuncs.com/d9f99eed-c3a5-455c-aa05-69cc57ab2c29.png', 'https://itheim.oss-cn-beijing.aliyuncs.com/09d6cc26-6568-4650-8aa1-92e851ff66b5.png', '2024-10-12 13:38:16', NULL, 1, NULL, NULL, '105-2', 202);
INSERT INTO `elder` VALUES (7, '张芳', 'https://itheim.oss-cn-beijing.aliyuncs.com/51b38243-3077-4f3d-9ccb-284b04533443.png', '132123195208121235', 1, 1, '15100000000', '1952-08-12 00:00:00', 'zdgsg', 'https://itheim.oss-cn-beijing.aliyuncs.com/de9eab7b-165c-4524-b0e4-4d076b501398.png', 'https://itheim.oss-cn-beijing.aliyuncs.com/1021f141-e099-47af-bd83-6811890208b2.jpeg', '2024-10-12 13:57:26', NULL, 1, NULL, NULL, '403-1', 36);
INSERT INTO `elder` VALUES (9, '马芸', 'https://itheim.oss-cn-beijing.aliyuncs.com/ca48735f-efbb-41d1-806a-1c67bf697648.png', '132122194812052345', 0, 1, '13212348765', '1948-12-05 00:00:00', '13212348765', 'https://itheim.oss-cn-beijing.aliyuncs.com/7679d834-2386-4d03-9266-90372ec39ad1.jpg', 'https://itheim.oss-cn-beijing.aliyuncs.com/e4a07a95-4e01-49d3-8936-6d71b345df51.jpg', '2024-10-12 14:03:43', NULL, 1, NULL, NULL, '405-1', 39);

-- ----------------------------
-- Table structure for family_member
-- ----------------------------
DROP TABLE IF EXISTS `family_member`;
CREATE TABLE `family_member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'OpenID',
  `gender` int(11) NULL DEFAULT NULL COMMENT '性别(0:男，1:女)',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '老人家属' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of family_member
-- ----------------------------

-- ----------------------------
-- Table structure for family_member_elder
-- ----------------------------
DROP TABLE IF EXISTS `family_member_elder`;
CREATE TABLE `family_member_elder`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `family_member_id` bigint(20) NULL DEFAULT NULL COMMENT '家属id',
  `elder_id` bigint(20) NULL DEFAULT NULL COMMENT '老人id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `member_id_elder_id`(`family_member_id`, `elder_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户老人关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of family_member_elder
-- ----------------------------
INSERT INTO `family_member_elder` VALUES (10, 6, 3, '2025-06-15 01:31:40', NULL, NULL, NULL, '叔叔');
INSERT INTO `family_member_elder` VALUES (11, 6, 1, '2025-06-15 01:32:10', NULL, NULL, NULL, '义父');

-- ----------------------------
-- Table structure for health_assessment
-- ----------------------------
DROP TABLE IF EXISTS `health_assessment`;
CREATE TABLE `health_assessment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `elder_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '老人姓名',
  `id_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `birth_date` datetime(0) NULL DEFAULT NULL COMMENT '出生日期',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `gender` int(11) NULL DEFAULT NULL COMMENT '性别(0:男，1:女)',
  `health_score` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '健康评分',
  `risk_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '严重危险(健康, 提示, 风险, 危险, 严重危险)',
  `suggestion_for_admission` int(11) NULL DEFAULT NULL COMMENT '是否建议入住(0:建议，1:不建议)',
  `nursing_level_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推荐护理等级',
  `admission_status` int(11) NULL DEFAULT NULL COMMENT '入住情况(0:已入住，1:未入住)',
  `total_check_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '总检日期',
  `physical_exam_institution` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '体检机构',
  `physical_report_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '体检报告URL链接',
  `assessment_time` datetime(0) NULL DEFAULT NULL COMMENT '评估时间',
  `report_summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '报告总结',
  `disease_risk` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '疾病风险',
  `abnormal_analysis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常分析',
  `system_score` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '健康系统分值',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '健康评估表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of health_assessment
-- ----------------------------
INSERT INTO `health_assessment` VALUES (8, '刘爱国', '132112196712137788', '1967-12-13 00:00:00', 56, 0, '80.25', 'caution', 0, '一级护理等级', 0, '2023-10-10', '黑马体检', 'https://itheim.oss-cn-beijing.aliyuncs.com/43267d42-9a21-4e71-bf5d-2e938cef85d3.pdf', '2024-09-16 00:09:31', '体检报告中心率、视力、肝脏B超、胆囊B超、脾脏B超、肾脏B超、前列腺B超共7项指标提示异常。综合这些临床指标和数据分析：循环系统、消化系统、泌尿系统存在隐患，其中循环系统、消化系统有“中危”风险；泌尿系统有“低危”风险。建议定期进行相关检查，保持健康的生活方式。', '{\"healthy\":60,\"caution\":30,\"risk\":10,\"danger\":0,\"severeDanger\":0}', '[{\"conclusion\":\"心率偏快\",\"examinationItem\":\"心率\",\"result\":\"92\",\"referenceValue\":\"<90\",\"unit\":\"次/分\",\"interpret\":\"心率稍高于正常范围，可能与紧张、活动或轻度心律失常有关。\",\"advice\":\"建议定期进行心电图检查，保持心情平静，避免过度劳累。\"},{\"conclusion\":\"视力下降\",\"examinationItem\":\"视力\",\"result\":\"左眼0.3，右眼0.4\",\"referenceValue\":\"≥1.0\",\"unit\":\"-\",\"interpret\":\"视力明显低于正常范围，符合老视特征。\",\"advice\":\"建议配戴合适的老花镜，避免长时间用眼，定期检查视力。\"},{\"conclusion\":\"轻度脂肪肝可能\",\"examinationItem\":\"肝脏B超\",\"result\":\"实质回声略粗糙\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"肝脏实质回声略粗糙可能提示轻度脂肪肝，与脂肪代谢异常有关。\",\"advice\":\"建议低脂饮食，适量运动，控制体重，避免饮酒，定期检查肝脏情况。\"},{\"conclusion\":\"慢性胆囊炎可能\",\"examinationItem\":\"胆囊B超\",\"result\":\"胆囊壁毛糙\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"胆囊壁毛糙可能提示慢性胆囊炎，与胆囊炎症或结石刺激有关。\",\"advice\":\"建议保持饮食规律，避免高脂食物，定期检查胆囊情况，如有症状及时就医。\"},{\"conclusion\":\"脾轻度增大\",\"examinationItem\":\"脾脏B超\",\"result\":\"脾轻度增大\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"脾脏轻度增大可能与感染、炎症或其他疾病有关。\",\"advice\":\"建议进一步检查脾脏增大的原因，定期监测脾脏情况。\"},{\"conclusion\":\"右肾小囊肿可能\",\"examinationItem\":\"肾脏B超\",\"result\":\"右肾下极见一大小约5mm的无回声区\",\"referenceValue\":\"-\",\"unit\":\"mm\",\"interpret\":\"右肾小囊肿可能与肾小管憩室增多有关，一般无需特殊处理。\",\"advice\":\"建议定期检查肾脏B超，观察囊肿变化，如有症状及时就医。\"},{\"conclusion\":\"前列腺形态略增大\",\"examinationItem\":\"前列腺B超\",\"result\":\"形态略增大\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"前列腺形态略增大可能与前列腺增生有关，是老年男性常见疾病。\",\"advice\":\"建议进行PSA检查以排除前列腺肿瘤，定期检查前列腺情况。\"}]', '{\"breathingSystem\":95,\"digestiveSystem\":85,\"endocrineSystem\":90,\"immuneSystem\":95,\"circulatorySystem\":80,\"urinarySystem\":85,\"motionSystem\":95,\"senseSystem\":80}', '1', '2024-09-16 00:09:31', NULL, NULL, NULL);
INSERT INTO `health_assessment` VALUES (9, '张芳', '132123195208121235', '1952-08-12 00:00:00', 72, 1, '75.0', 'caution', 0, '一级护理等级', 1, '2023.10.10', '黑马体检', 'https://itheim.oss-cn-beijing.aliyuncs.com/fa03a618-76b7-4db5-b7b8-7fcdded51f6e.pdf', '2024-09-16 16:05:31', '体检报告中双下肢水肿、视力、听力、血压、血红蛋白、尿蛋白、胆固醇、低密度脂蛋白、乙肝表面抗体、脾脏、肾脏共11项指标提示异常，综合这些临床指标和数据分析：心肾功能、感官系统、循环系统、免疫系统、内分泌系统、消化系统存在隐患，其中循环系统、泌尿系统有“中危”风险；其余系统有“低危”风险。', '{\"healthy\":60,\"caution\":30,\"risk\":10,\"danger\":0,\"severeDanger\":0}', '[{\"conclusion\":\"双下肢轻微凹陷性水肿\",\"examinationItem\":\"内科\",\"result\":\"双下肢轻微凹陷性水肿\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"可能由心肾功能异常引起，需进一步检查以确定具体原因。\",\"advice\":\"建议进一步检查心肾功能，根据检查结果采取相应治疗措施。平时避免长时间站立，可抬高下肢休息，以减轻水肿。\"},{\"conclusion\":\"视力略有下降\",\"examinationItem\":\"眼科\",\"result\":\"视力左0.5/，视力右0.6/\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"视力下降是老化的常见表现，可能与眼部疾病或不良用眼习惯有关。\",\"advice\":\"建议定期复查视力，考虑配戴合适的老花镜。平时注意用眼卫生，避免长时间用眼。\"},{\"conclusion\":\"右耳听力下降\",\"examinationItem\":\"耳鼻喉科\",\"result\":\"右耳听力下降\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"听力下降可能与年龄、耳部疾病或长期暴露于噪音环境有关。\",\"advice\":\"建议耳鼻喉科进一步评估，找出听力下降的具体原因。平时避免长时间暴露于噪音环境，注意保护听力。\"},{\"conclusion\":\"血压偏高\",\"examinationItem\":\"一般科室\",\"result\":\"收缩压145mmHg，舒张压90mmHg\",\"referenceValue\":\"收缩压<140mmHg，舒张压<90mmHg\",\"unit\":\"mmHg\",\"interpret\":\"血压偏高可能增加心脑血管疾病的风险。\",\"advice\":\"建议定期监测血压，改善生活方式（如低盐饮食、适量运动、戒烟限酒等）。必要时心内科随访，考虑药物治疗。\"},{\"conclusion\":\"血红蛋白略低\",\"examinationItem\":\"血常规\",\"result\":\"血红蛋白110g/l\",\"referenceValue\":\"120-160g/l\",\"unit\":\"g/l\",\"interpret\":\"血红蛋白略低可能提示轻度贫血，需关注营养状况。\",\"advice\":\"建议增加富含铁质的食物摄入，如瘦肉、动物肝脏、绿叶蔬菜等。必要时咨询医生，考虑补充铁剂。\"},{\"conclusion\":\"尿蛋白微量\",\"examinationItem\":\"尿常规\",\"result\":\"尿蛋白微量\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"尿蛋白微量可能提示肾功能轻度受损或泌尿系统感染。\",\"advice\":\"建议复查尿常规，关注肾功能。平时保持充足的水分摄入，避免过度劳累。\"},{\"conclusion\":\"胆固醇及低密度脂蛋白略高\",\"examinationItem\":\"生化\",\"result\":\"总胆固醇6.2mmol/L，低密度脂蛋白4.0mmol/L\",\"referenceValue\":\"总胆固醇2.3-5.7mmol/L，低密度脂蛋白2.1-3.1mmol/L\",\"unit\":\"mmol/L\",\"interpret\":\"胆固醇及低密度脂蛋白略高可能增加动脉粥样硬化的风险。\",\"advice\":\"建议饮食控制，减少高脂肪、高胆固醇食物的摄入。适量增加运动，保持健康的生活方式。\"},{\"conclusion\":\"乙肝表面抗体阴性\",\"examinationItem\":\"免疫\",\"result\":\"乙肝表面抗体阴性\",\"referenceValue\":\"阴性/阳性\",\"unit\":\"-\",\"interpret\":\"乙肝表面抗体阴性表示体内无乙肝抗体，对乙肝病毒无免疫力。\",\"advice\":\"建议接种乙肝疫苗，以增强对乙肝病毒的抵抗力。\"},{\"conclusion\":\"脾轻度增大\",\"examinationItem\":\"B超\",\"result\":\"脾轻度增大\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"脾轻度增大可能与感染、血液系统疾病或自身免疫性疾病有关。\",\"advice\":\"建议进一步检查以确定脾增大的具体原因。平时注意观察身体状况，如有异常及时就医。\"},{\"conclusion\":\"右肾小囊肿可能\",\"examinationItem\":\"B超\",\"result\":\"右肾下极见一大小约5mm的无回声区，考虑小囊肿可能\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"肾囊肿是一种常见的肾脏良性病变，一般无需特殊处理，但需定期复查。\",\"advice\":\"建议定期复查B超，关注囊肿变化。平时避免剧烈运动，以免囊肿破裂。\"}]', '{\"breathingSystem\":90,\"digestiveSystem\":85,\"endocrineSystem\":80,\"immuneSystem\":80,\"circulatorySystem\":75,\"urinarySystem\":75,\"motionSystem\":90,\"senseSystem\":85}', '1', '2024-09-16 16:05:31', NULL, NULL, NULL);
INSERT INTO `health_assessment` VALUES (10, '2232', '132111196712161234', '1967-12-16 00:00:00', 56, 1, '68.25', 'risk', 0, '三级护理等级', 0, '2023-09-15', 'dsf  ', 'https://itheim.oss-cn-beijing.aliyuncs.com/c6645922-30de-441f-99d6-360c731a85d3.pdf', '2024-09-16 17:25:47', '体检报告中尿蛋白、空腹血糖、总胆固醇、甘油三酯、低密度脂蛋白胆固醇共5项指标提示异常，综合这些临床指标和数据分析：肾脏、心脑血管存在隐患，其中心脑血管有“中危”风险；肾脏部位有“低危”风险。', '{\"healthy\":55,\"caution\":20,\"risk\":20,\"danger\":5,\"severeDanger\":0}', '[{\"conclusion\":\"肥胖\",\"examinationItem\":\"体重指数BMI\",\"result\":\"29.2\",\"referenceValue\":\">24\",\"unit\":\"-\",\"interpret\":\"体重超标包括超重与肥胖。BMI≥24为超重，BMI≥28为肥胖，体重超标是⼀种由多因素引起的慢性代谢性疾病。\",\"advice\":\"采取综合措施预防和控制体重，积极改变生活方式，低脂、低糖、高纤维素膳食，多食果蔬，增加有氧运动。\"},{\"conclusion\":\"空腹血糖受损\",\"examinationItem\":\"空腹血糖\",\"result\":\"6.37\",\"referenceValue\":\"3.9-6.1\",\"unit\":\"mmol/L\",\"interpret\":\"空腹血糖受损是从正常过渡到糖尿病的一个过渡阶段，可能会发展为糖尿病。\",\"advice\":\"调整饮食，减少高糖食物摄入，适量运动，定期监测血糖，必要时就医治疗。\"},{\"conclusion\":\"血脂异常\",\"examinationItem\":\"[\\\"总胆固醇\\\",\\\"甘油三酯\\\",\\\"低密度脂蛋白胆固醇\\\"]\",\"result\":\"[\\\"5.74\\\",\\\"2.17\\\",\\\"3.37\\\"]\",\"referenceValue\":\"[\\\"<5.2\\\",\\\"<1.7\\\",\\\"<3.12\\\"]\",\"unit\":\"mmol/L\",\"interpret\":\"血脂异常可能导致动脉粥样硬化，增加心脑血管病风险。\",\"advice\":\"低脂饮食，增加膳食纤维摄入，适量运动，必要时服用降脂药物。\"},{\"conclusion\":\"尿蛋白弱阳性\",\"examinationItem\":\"尿蛋白\",\"result\":\"+-\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"尿蛋白弱阳性可能提示肾脏功能轻度受损或生理性蛋白尿。\",\"advice\":\"注意休息，避免过度劳累，定期复查尿常规，如有异常及时就医。\"}]', '{\"breathingSystem\":90,\"digestiveSystem\":85,\"endocrineSystem\":75,\"immuneSystem\":80,\"circulatorySystem\":65,\"urinarySystem\":70,\"motionSystem\":85,\"senseSystem\":90}', '1', '2024-09-16 17:25:48', NULL, NULL, NULL);
INSERT INTO `health_assessment` VALUES (11, '李爱国', '121231197612190987', '1976-12-19 00:00:00', 47, 0, '85.0', 'caution', 0, '二级护理等级', 1, '2024.09.10', '黑马', 'https://itheim.oss-cn-beijing.aliyuncs.com/1170d925-a2ff-4435-bf9b-e12715bfa3ee.pdf', '2024-09-20 00:50:26', '体检报告中尿蛋白、血压、血红蛋白、胆固醇、低密度脂蛋白、右耳听力、双下肢水肿、乙肝表面抗体、脾脏和肾脏B超共10项指标提示异常，其余指标均处于正常范围内。综合这些临床指标和数据分析：循环系统、泌尿系统存在隐患，其中循环系统有“中危”风险；泌尿系统部位有“低危”风险。', '{\"healthy\":60,\"caution\":30,\"risk\":10,\"danger\":0,\"severeDanger\":0}', '[{\"conclusion\":\"双下肢轻微凹陷性水肿\",\"examinationItem\":\"内科检查\",\"result\":\"双下肢轻微凹陷性水肿\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"双下肢凹陷性水肿可能提示心功能不全、肾功能不全或下肢静脉回流不畅等问题。\",\"advice\":\"建议进一步检查心肾功能，避免长时间站立或久坐，可适时抬高下肢促进血液回流。\"},{\"conclusion\":\"右耳听力下降\",\"examinationItem\":\"耳鼻喉科检查\",\"result\":\"右耳听力下降\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"听力下降可能与年龄、耳部疾病、噪音暴露或其他因素有关。\",\"advice\":\"建议耳鼻喉科进一步评估，并避免长时间暴露于高噪音环境中。\"},{\"conclusion\":\"血压偏高\",\"examinationItem\":\"一般科室检查\",\"result\":\"收缩压 145 mmHg，舒张压 90 mmHg\",\"referenceValue\":\"收缩压<140 mmHg，舒张压<90 mmHg\",\"unit\":\"mmHg\",\"interpret\":\"血压偏高可能增加心脑血管疾病的风险。\",\"advice\":\"定期监测血压，调整饮食习惯，减少盐分摄入，适量运动，必要时心内科随访。\"},{\"conclusion\":\"血红蛋白略低\",\"examinationItem\":\"血常规检查\",\"result\":\"血红蛋白 110 g/l\",\"referenceValue\":\"120-160 g/l\",\"unit\":\"g/l\",\"interpret\":\"血红蛋白略低可能提示轻度贫血。\",\"advice\":\"建议增加富含铁质的食物摄入，如瘦肉、动物肝脏等，并定期复查血常规。\"},{\"conclusion\":\"尿蛋白微量\",\"examinationItem\":\"尿常规检查\",\"result\":\"尿蛋白（PRO）微量\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"尿蛋白微量可能提示肾功能轻度受损或存在其他肾脏问题。\",\"advice\":\"建议复查尿常规，并关注肾功能。\"},{\"conclusion\":\"胆固醇及低密度脂蛋白略高\",\"examinationItem\":\"生化检查\",\"result\":\"总胆固醇 6.2 mmol/L，低密度脂蛋白 4.0 mmol/L\",\"referenceValue\":\"总胆固醇 2.3-5.7 mmol/L，低密度脂蛋白 2.1-3.1 mmol/L\",\"unit\":\"mmol/L\",\"interpret\":\"胆固醇及低密度脂蛋白偏高可能增加心血管疾病的风险。\",\"advice\":\"建议饮食控制，减少高脂肪食物的摄入，适量运动，并定期复查血脂。\"},{\"conclusion\":\"乙肝表面抗体阴性\",\"examinationItem\":\"免疫检查\",\"result\":\"乙肝表面抗体阴性\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"乙肝表面抗体阴性表示体内没有产生对乙肝病毒的免疫力。\",\"advice\":\"建议接种乙肝疫苗以预防乙肝病毒感染。\"},{\"conclusion\":\"脾轻度增大\",\"examinationItem\":\"B超检查\",\"result\":\"脾轻度增大\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"脾轻度增大可能与多种原因有关，如感染、血液系统疾病等。\",\"advice\":\"建议进一步检查以确定原因，并定期监测脾脏大小。\"},{\"conclusion\":\"右肾小囊肿可能\",\"examinationItem\":\"B超检查\",\"result\":\"右肾下极见一大小约5mm的无回声区\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"右肾小囊肿可能是一种良性病变，但也需要关注其发展情况。\",\"advice\":\"建议定期检查肾脏B超，观察囊肿变化，如有必要可咨询专业医生进行进一步治疗。\"}]', '{\"breathingSystem\":95,\"digestiveSystem\":90,\"endocrineSystem\":90,\"immuneSystem\":85,\"circulatorySystem\":80,\"urinarySystem\":85,\"motionSystem\":95,\"senseSystem\":90}', '1', '2024-09-20 00:50:26', NULL, NULL, NULL);
INSERT INTO `health_assessment` VALUES (12, '李爱国', '132112198712091234', '1987-12-09 00:00:00', 36, 1, '80.0', 'caution', 0, '二级护理等级', 1, '2023-10-10', '黑马', 'https://itheim.oss-cn-beijing.aliyuncs.com/61df7f51-3c65-4663-bc61-962785baa033.pdf', '2024-10-10 10:59:58', '体检报告中心率、肝脏实质回声、胆囊壁、脾脏、右肾下极、前列腺共6项指标提示异常，综合这些临床指标和数据分析：消化系统、循环系统存在隐患，其中消化系统有“中危”风险；循环系统有“低危”风险。', '{\"healthy\":60,\"caution\":30,\"risk\":10,\"danger\":0,\"severeDanger\":0}', '[{\"conclusion\":\"心率偏快\",\"examinationItem\":\"心率\",\"result\":\"92 次/分\",\"referenceValue\":\"< 90\",\"unit\":\"次/分\",\"interpret\":\"心率稍高于正常范围，可能与情绪、活动或轻度心脏负荷有关。\",\"advice\":\"建议定期进行心电图检查，保持情绪稳定，适当进行有氧运动，并避免过度劳累。\"},{\"conclusion\":\"轻度脂肪肝可能\",\"examinationItem\":\"肝实质回声\",\"result\":\"略粗糙\",\"referenceValue\":\"均匀\",\"unit\":\"-\",\"interpret\":\"肝脏实质回声略粗糙，提示可能有轻度脂肪肝，这与饮食习惯、饮酒、缺乏运动等因素有关。\",\"advice\":\"建议改善饮食习惯，减少高脂、高糖食物摄入，增加蔬菜、水果摄入，适量运动，并避免过量饮酒。\"},{\"conclusion\":\"慢性胆囊炎可能\",\"examinationItem\":\"胆囊壁\",\"result\":\"毛糙\",\"referenceValue\":\"光滑\",\"unit\":\"-\",\"interpret\":\"胆囊壁毛糙，可能提示慢性胆囊炎，这可能与胆囊结石、感染、饮食习惯等因素有关。\",\"advice\":\"建议进一步检查以明确诊断，并改善饮食习惯，避免油腻食物，保持规律饮食。\"},{\"conclusion\":\"脾轻度增大\",\"examinationItem\":\"脾\",\"result\":\"轻度增大\",\"referenceValue\":\"正常大小\",\"unit\":\"-\",\"interpret\":\"脾脏轻度增大，可能与感染、炎症、血液系统疾病等因素有关。\",\"advice\":\"建议进一步检查以明确原因，并定期监测脾脏情况。\"},{\"conclusion\":\"右肾小囊肿可能\",\"examinationItem\":\"右肾下极\",\"result\":\"见一大小约 5mm 的无回声区\",\"referenceValue\":\"-\",\"unit\":\"mm\",\"interpret\":\"右肾下极见一小无回声区，提示可能为小囊肿，这是一种常见的良性病变。\",\"advice\":\"建议定期复查以监测囊肿变化，如无明显增大或症状，一般无需特殊处理。\"},{\"conclusion\":\"前列腺形态略增大\",\"examinationItem\":\"前列腺形态\",\"result\":\"略增大\",\"referenceValue\":\"正常大小\",\"unit\":\"-\",\"interpret\":\"前列腺形态略增大，可能与年龄、炎症、前列腺增生等因素有关。\",\"advice\":\"建议进行PSA检查以排除前列腺疾病，并定期复查以监测前列腺变化。\"}]', '{\"breathingSystem\":95,\"digestiveSystem\":80,\"endocrineSystem\":90,\"immuneSystem\":95,\"circulatorySystem\":85,\"urinarySystem\":90,\"motionSystem\":95,\"senseSystem\":85}', '1', '2024-10-10 10:59:58', NULL, NULL, NULL);
INSERT INTO `health_assessment` VALUES (13, '牛爱国', '132112195412131243', '1954-12-13 00:00:00', 69, 0, '75.45', 'caution', 0, '一级护理等级', 1, '2023.10.10', '黑马', 'https://itheim.oss-cn-beijing.aliyuncs.com/9d5a19ab-2775-4e08-b556-de22a71fe4e3.pdf', '2024-10-11 17:12:01', '体检报告中血压、血红蛋白、尿常规、胆固醇、低密度脂蛋白、B超共6项指标提示异常，乙肝抗体指标建议接种疫苗，视力、听力共2项指标建议关注并定期检查，综合这些临床指标和数据分析：心脑血管存在隐患，其中心脑血管、肾脏部位有“低危”风险；需改善生活习惯，调整饮食结构，并定期复查相关指标。', '{\"healthy\":60,\"caution\":30,\"risk\":10,\"danger\":0,\"severeDanger\":0}', '[{\"conclusion\":\"轻微下肢水肿\",\"examinationItem\":\"内科检查\",\"result\":\"双下肢轻微凹陷性水肿\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"双下肢轻微凹陷性水肿可能提示心肾功能异常，需进一步检查以明确原因。\",\"advice\":\"建议心内科和肾内科进一步检查，平时避免长时间站立，可适时抬高下肢促进血液回流。\"},{\"conclusion\":\"视力下降\",\"examinationItem\":\"眼科检查\",\"result\":\"视力左0.5/，视力右0.6/\",\"referenceValue\":\"1.0以上\",\"unit\":\"-\",\"interpret\":\"视力略有下降，可能与年龄相关的老花眼有关。\",\"advice\":\"建议定期复查视力，并考虑配戴合适的老花镜以改善视力。\"},{\"conclusion\":\"右耳听力下降\",\"examinationItem\":\"耳鼻喉科检查\",\"result\":\"右耳听力下降\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"右耳听力下降可能由多种原因引起，如耳部疾病、噪音暴露等，需进一步评估。\",\"advice\":\"建议耳鼻喉科进一步评估听力下降原因，并避免长时间暴露于高噪音环境中。\"},{\"conclusion\":\"血压偏高\",\"examinationItem\":\"一般科室检查\",\"result\":\"收缩压145mmHg，舒张压90mmHg\",\"referenceValue\":\"收缩压<140mmHg，舒张压<90mmHg\",\"unit\":\"mmHg\",\"interpret\":\"血压偏高可能增加心血管疾病风险，需定期监测并控制。\",\"advice\":\"建议定期监测血压，改善生活方式，如低盐饮食、适量运动等，必要时心内科随访。\"},{\"conclusion\":\"血红蛋白略低\",\"examinationItem\":\"血常规检查\",\"result\":\"血红蛋白110g/l\",\"referenceValue\":\"120-160g/l\",\"unit\":\"g/l\",\"interpret\":\"血红蛋白略低可能提示轻度贫血，需注意营养摄入。\",\"advice\":\"建议增加富含铁质的食物摄入，如瘦肉、动物肝脏等，并定期复查血常规。\"},{\"conclusion\":\"胆固醇及低密度脂蛋白偏高\",\"examinationItem\":\"生化检查\",\"result\":\"总胆固醇6.2mmol/L，低密度脂蛋白4.0mmol/L\",\"referenceValue\":\"总胆固醇2.3-5.7mmol/L，低密度脂蛋白2.1-3.1mmol/L\",\"unit\":\"mmol/L\",\"interpret\":\"胆固醇及低密度脂蛋白偏高可能增加心血管疾病风险，需控制饮食。\",\"advice\":\"建议饮食控制，减少高脂食物摄入，如动物内脏、油炸食品等，并定期复查血脂。\"},{\"conclusion\":\"乙肝表面抗体阴性\",\"examinationItem\":\"免疫检查\",\"result\":\"乙肝表面抗体阴性\",\"referenceValue\":\"阴性/阳性\",\"unit\":\"-\",\"interpret\":\"乙肝表面抗体阴性表示体内无乙肝抗体保护，建议接种疫苗以预防乙肝感染。\",\"advice\":\"建议接种乙肝疫苗，并定期复查乙肝抗体情况。\"},{\"conclusion\":\"脾轻度增大，右肾小囊肿可能\",\"examinationItem\":\"B超检查\",\"result\":\"脾轻度增大，右肾下极见一大小约5mm的无回声区\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"脾轻度增大可能与多种原因有关，需进一步检查。右肾小囊肿一般为良性病变，但需定期监测。\",\"advice\":\"建议对脾脏和肾脏进行进一步检查，并定期监测囊肿变化。\"}]', '{\"breathingSystem\":95,\"digestiveSystem\":90,\"endocrineSystem\":85,\"immuneSystem\":80,\"circulatorySystem\":75,\"urinarySystem\":80,\"motionSystem\":95,\"senseSystem\":85}', '1', '2024-10-11 17:12:01', NULL, NULL, NULL);
INSERT INTO `health_assessment` VALUES (14, '张爱国', '132122194812052345', '1948-12-05 00:00:00', 75, 0, '80.25', 'caution', 0, '二级护理等级', 1, '2023-10-10', '黑马体检', 'https://itheim.oss-cn-beijing.aliyuncs.com/6732ea50-39aa-4ff3-b945-6a56129a4e5e.pdf', '2024-10-12 14:01:03', '体检报告中心率、肝脏、胆囊、脾脏、肾脏、前列腺共6项指标提示异常，综合这些临床指标和数据分析：循环系统、消化系统存在隐患，其中循环系统有“低危”风险；消化系统部位有“低危”风险。建议定期进行相关检查，关注异常指标变化，必要时相应科室就诊。', '{\"healthy\":60,\"caution\":30,\"risk\":10,\"danger\":0,\"severeDanger\":0}', '[{\"conclusion\":\"心率略快\",\"examinationItem\":\"心率\",\"result\":\"92 次/分\",\"referenceValue\":\"< 140\",\"unit\":\"次/分\",\"interpret\":\"心率正常范围为60-100次/分，当前心率略偏快，可能受多种因素影响，如紧张、运动、药物等。\",\"advice\":\"建议定期进行心电图检查，注意保持平和心态，避免过度劳累。\"},{\"conclusion\":\"偶发早搏\",\"examinationItem\":\"心律\",\"result\":\"偶发早搏\",\"referenceValue\":\"齐\",\"unit\":\"-\",\"interpret\":\"早搏是心脏提前搏动的简称，偶发早搏可能无明显症状，但值得关注。\",\"advice\":\"建议进行24小时动态心电图监测，评估早搏频率和性质，必要时心内科就诊。\"},{\"conclusion\":\"轻度脂肪肝可能\",\"examinationItem\":\"肝\",\"result\":\"形态大小正常，实质回声略粗糙\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"肝脏实质回声略粗糙可能提示轻度脂肪肝，与饮食习惯、生活方式等有关。\",\"advice\":\"建议低脂饮食，适量运动，控制体重，定期复查肝脏B超。\"},{\"conclusion\":\"慢性胆囊炎可能\",\"examinationItem\":\"胆\",\"result\":\"胆囊壁毛糙\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"胆囊壁毛糙可能提示慢性胆囊炎，与胆汁淤积、细菌感染等有关。\",\"advice\":\"建议饮食规律，避免油腻食物，定期复查胆囊B超，必要时消化内科就诊。\"},{\"conclusion\":\"脾轻度增大\",\"examinationItem\":\"脾\",\"result\":\"轻度增大\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"脾脏轻度增大可能与多种原因有关，如感染、血液系统疾病等，需进一步检查明确原因。\",\"advice\":\"建议定期进行脾脏B超检查，关注脾脏变化，必要时血液科就诊。\"},{\"conclusion\":\"右肾小囊肿可能\",\"examinationItem\":\"肾\",\"result\":\"右肾下极见一大小约 5mm 的无回声区\",\"referenceValue\":\"-\",\"unit\":\"mm\",\"interpret\":\"右肾小囊肿可能为单纯性肾囊肿，一般无明显症状，但需定期复查。\",\"advice\":\"建议定期进行肾脏B超检查，关注囊肿变化，必要时泌尿外科就诊。\"},{\"conclusion\":\"前列腺形态略增大\",\"examinationItem\":\"前列腺\",\"result\":\"形态略增大\",\"referenceValue\":\"-\",\"unit\":\"-\",\"interpret\":\"前列腺形态略增大可能与前列腺增生有关，是中老年男性常见疾病。\",\"advice\":\"建议进行前列腺特异性抗原（PSA）检查，定期复查前列腺B超，必要时泌尿外科就诊。\"}]', '{\"breathingSystem\":100,\"digestiveSystem\":85,\"endocrineSystem\":90,\"immuneSystem\":95,\"circulatorySystem\":80,\"urinarySystem\":90,\"motionSystem\":95,\"senseSystem\":90}', '1', '2024-10-12 14:01:03', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for nursing_project
-- ----------------------------
DROP TABLE IF EXISTS `nursing_project`;
CREATE TABLE `nursing_project`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `order_no` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  `nursing_requirement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '护理要求',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '状态（0：禁用，1：启用）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '护理项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nursing_project
-- ----------------------------
INSERT INTO `nursing_project` VALUES (3, '整理床铺', 1, '次', 15.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/e611fcc9-dc45-49ac-abeb-f2ea99c2cffc.png', '无', 1, '1', NULL, NULL, '2024-08-29 16:52:52', '2024-08-29 08:51:46');
INSERT INTO `nursing_project` VALUES (4, '助餐', 1, '餐', 15.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/d91ba642-88e5-4c3d-8e50-a681ae3300e5.png', '无', 1, '1', NULL, NULL, '2024-08-29 16:53:29', '2024-08-29 08:52:24');
INSERT INTO `nursing_project` VALUES (5, '助浴', 1, '次', 40.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/125df948-7646-4fce-b322-1db0a84856e7.png', '无', 1, '1', NULL, NULL, '2024-08-29 16:53:51', '2024-08-29 08:52:46');
INSERT INTO `nursing_project` VALUES (7, '洗脸', 1, '次', 15.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/95b0ad37-5d61-4ec2-a961-d6fb691a18f0.png', '无', 1, '1', NULL, NULL, '2024-08-29 16:54:45', '2024-08-29 08:53:40');
INSERT INTO `nursing_project` VALUES (8, '洗脚', 1, '次', 20.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/8437eb2d-3ea5-4eee-9d78-017bc8b3a66e.png', '无', 1, '1', NULL, NULL, '2024-08-29 16:55:08', '2024-08-29 08:54:03');
INSERT INTO `nursing_project` VALUES (9, '心理咨询', 1, '小时', 80.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/dc004cc2-688c-4d22-8fbc-8e923219a2bd.png', '无', 1, '1', NULL, NULL, '2024-08-29 16:55:37', '2024-08-29 08:54:32');
INSERT INTO `nursing_project` VALUES (14, '护理项目测试', 1, '次', 10.00, 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/ae7cf766-fb7b-49ff-a73c-c86c25f280e1.png', '无特殊要求', 1, '1', NULL, NULL, '2024-09-19 10:48:41', '2024-09-19 02:48:42');

-- ----------------------------
-- Table structure for reservation
-- ----------------------------
DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预约人姓名',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预约人手机号',
  `time` datetime(0) NOT NULL COMMENT '预约时间',
  `visitor` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '探访人',
  `type` int(11) NOT NULL COMMENT '预约类型，0：参观预约，1：探访预约',
  `status` int(11) NOT NULL COMMENT '预约状态，0：待报道，1：已完成，2：取消，3：过期',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_mobile_time_visitor`(`mobile`, `time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预约信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reservation
-- ----------------------------
INSERT INTO `reservation` VALUES (4, '122', '13211223344', '2024-09-20 16:30:00', '123', 0, 0, '2024-09-20 16:20:06', '2024-09-20 16:20:26', 6, 5, NULL);
INSERT INTO `reservation` VALUES (5, '小李', '13567834456', '2024-09-27 12:30:00', '李天龙', 1, 1, '2024-09-27 00:03:01', NULL, 6, 5, NULL);
INSERT INTO `reservation` VALUES (10, '水电费', '18988886666', '2025-06-15 10:00:00', '刘备', 1, 0, '2025-06-15 09:46:40', NULL, 6, 6, NULL);
INSERT INTO `reservation` VALUES (11, '1111', '18684811111', '2025-06-15 10:00:00', '刘备', 1, 0, '2025-06-15 09:47:28', NULL, 6, 6, NULL);
INSERT INTO `reservation` VALUES (12, '是', '18684111111', '2025-06-15 10:00:00', '刘备', 1, 0, '2025-06-15 09:49:51', NULL, 6, 6, NULL);

-- ----------------------------
-- Table structure for room_type
-- ----------------------------
DROP TABLE IF EXISTS `room_type`;
CREATE TABLE `room_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '房型名称',
  `bed_count` int(11) NOT NULL DEFAULT 0 COMMENT '床位数量',
  `price` decimal(10, 2) NOT NULL COMMENT '床位费用',
  `introduction` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '介绍',
  `photo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '照片',
  `status` tinyint(4) NOT NULL COMMENT '状态，0：禁用，1：启用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 117 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_type
-- ----------------------------
INSERT INTO `room_type` VALUES (1, '单人套房', 0, 4000.00, '宽敞舒适的套房，配备独立卫生间和基本生活设施，满足独自居住的需求，提供私密性和舒适度', 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/e2f1031b-e23e-4379-95d4-ce8fe382f58f.png', 1, '2023-09-26 15:57:50', '2024-05-20 11:00:19', 1671403256519078153, 1, NULL);
INSERT INTO `room_type` VALUES (2, '双人套房', 0, 6000.00, '适合夫妻或朋友两人居住的套房，设有独立卫生间和基本生活设施，提供共享空间和私密性', 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/ff84c185-2e28-431c-951d-d004cc2d5bdc.png', 1, '2023-09-26 15:58:51', '2023-09-26 15:58:51', 1671403256519078153, NULL, NULL);
INSERT INTO `room_type` VALUES (3, '豪华单人间', 0, 3000.00, '豪华装修的单人房间，提供舒适的居住环境和高品质的服务，设计精美，配备独立卫生间和必需设施', 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/d803832c-5b93-4cae-ba95-aeb52ab0c5e0.png', 1, '2023-09-26 15:59:33', '2023-09-26 15:59:33', 1671403256519078153, NULL, NULL);
INSERT INTO `room_type` VALUES (4, '豪华双人间', 0, 4500.00, '精心装修的双人房间，提供舒适和豪华的居住环境，配备独立卫生间和高品质的家具', 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/c3522da7-4c5c-48d2-94f9-9f0b95a048d2.png', 1, '2023-09-26 16:00:03', '2024-08-22 16:12:20', 1671403256519078153, 1, NULL);
INSERT INTO `room_type` VALUES (5, '普通单人间', 0, 2000.00, '简洁实用的单人房间，提供基本的居住设施和舒适度，适合独自居住的老年人，提供相对经济实惠的居住选择', 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/1a330b1c-b0a1-463d-8d9a-221ef17c314f.png', 1, '2023-09-26 16:00:27', '2023-09-26 16:00:27', 1671403256519078153, NULL, NULL);
INSERT INTO `room_type` VALUES (103, '我的房型设置', 0, 6000.00, '啊傻瓜按个按个', 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/9c4a9926-2f58-4306-a8c5-122adb8e700e.jpeg', 1, '2023-12-25 16:04:00', '2023-12-25 16:04:00', 1671403256519078138, NULL, NULL);
INSERT INTO `room_type` VALUES (110, '测试修改', 5, 1000.00, '12312312312312312312312312312312312312312312312312', 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/00167193-00f1-49a6-85e5-74dd0b93819e.png', 1, '2023-12-27 00:07:07', '2024-08-22 19:05:53', 1671403256519078138, 1, NULL);
INSERT INTO `room_type` VALUES (115, '测试新增', 10, 3500.00, '水电费方式', 'https://itheim.oss-cn-beijing.aliyuncs.com/8fe7b29d-fce0-4201-becb-0586e8284a9d.png', 1, '2024-08-22 19:06:33', '2024-08-22 19:06:41', 1, 1, '是否');

SET FOREIGN_KEY_CHECKS = 1;
