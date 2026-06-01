package cn.yangeit.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 健康评估表
 * @TableName health_assessment
 */
@TableName(value ="health_assessment")
@Data
public class HealthAssessment {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "id")
    private Long id;

    /**
     * 老人姓名
     */
    @Schema(description = "老人姓名")
    private String elderName;

    /**
     * 身份证号
     */
    @Schema(description = "身份证号")
    private String idCard;

    /**
     * 出生日期
     */
    @Schema(description = "出生日期：年月日")
    private String birthDate;

    /**
     * 年龄
     */
    @Schema(description = "年龄")
    private Integer age;

    /**
     * 性别(0:男，1:女)
     */
    @Schema(description = "性别(0:男，1:女)")
    private Integer gender;

    /**
     * 健康评分
     */
    @Schema(description = "健康评分")
    private String healthScore;

    /**
     * 严重危险(健康, 提示, 风险, 危险, 严重危险)
     */
    @Schema(description = "严重危险(健康, 提示, 风险, 危险, 严重危险)")
    private String riskLevel;

    /**
     * 是否建议入住(0:建议，1:不建议)
     */
    @Schema(description = "是否建议入住(0:建议，1:不建议)")
    private Integer suggestionForAdmission;

    /**
     * 推荐护理等级
     */
    @Schema(description = "推荐护理等级")
    private String nursingLevelName;

    /**
     * 入住情况(0:已入住，1:未入住)
     */
    @Schema(description = "入住情况(0:已入住，1:未入住)")
    private Integer admissionStatus;

    /**
     * 总检日期
     */
    @Schema(description = "总检日期")
    private String totalCheckDate;

    /**
     * 体检机构
     */
    @Schema(description = "体检机构")
    private String physicalExamInstitution;

    /**
     * 体检报告URL链接
     */
    @Schema(description = "体检报告URL链接")
    private String physicalReportUrl;

    /**
     * 评估时间
     */
    @Schema(description = "评估时间")
    private LocalDateTime assessmentTime;

    /**
     * 报告总结
     */
    @Schema(description = "报告总结")
    private String reportSummary;

    /**
     * 疾病风险
     */
    @Schema(description = "疾病风险")
    private String diseaseRisk;

    /**
     * 异常分析
     */
    @Schema(description = "异常分析")
    private String abnormalAnalysis;

    /**
     * 健康系统分值
     */
    @Schema(description = "健康系统分值")
    private String systemScore;

    /**
     * 创建者
     */
    @Schema(description = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @Schema(description = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}