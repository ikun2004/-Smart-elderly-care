package cn.yangeit.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeCountVo {
    /**
     * 时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//get
    private LocalDateTime time;
    /**
     * 次数
     */

    private Integer count;
}

