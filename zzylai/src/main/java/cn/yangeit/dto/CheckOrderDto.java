package cn.yangeit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yangeit
 * @date 2025年06月14日 22:26
 * @Description
 */
@Data
@Builder
public class CheckOrderDto {
    //服务金额
    private Float amount;
    //老人ID
    private Long elderId;
    //服务时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedArrivalTime;
    //老人姓名
    private String name;
    //服务项目ID
    private Long projectId;
    //备注
    private String remark;
}
