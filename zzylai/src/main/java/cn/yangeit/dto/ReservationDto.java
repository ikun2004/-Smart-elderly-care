package cn.yangeit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDto {

    /**
     * 预约人
     */

    private String name;

    /**
     * 预约人手机号
     */

    private String mobile;

    /**
     * 时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    /**
     * 探访人
     */

    private String visitor;

    /**
     * 预约类型，0：参观预约，1：探访预约
     */

    private Integer type;

    /**
     * 预约状态，0：待报道，1：已完成，2：取消，3：过期
     */

    private Integer status;


    private Long elderId;
}