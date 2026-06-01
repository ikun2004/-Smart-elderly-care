package cn.yangeit.vo.admin;

import lombok.Data;

/**
 *  健康评估类
 */
@Data
public class HealthAssessmentVo {
    /**
     * 健康风险等级
     */
    private String riskLevel;
    /**
     * 健康指数
     */
    private double healthIndex;

}