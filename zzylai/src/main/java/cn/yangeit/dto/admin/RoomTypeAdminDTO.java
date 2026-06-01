package cn.yangeit.dto.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RoomTypeAdminDTO{

    @Schema(description = "房型ID",example = "1")
    private Long id;

    /**
     * 房型名称
     */
    @Schema(description = "房型名称",example = "豪华单人间")
    private String name;

    /**
     * 床位数量
     */
    @Schema(description = "床位数量",example = "1")
    private Integer bedCount;

    /**
     * 床位费用
     */
    @Schema(description = "床位费用",example = "50.00")
    private BigDecimal price;

    /**
     * 介绍
     */
    @Schema(description = "介绍",example = "坐享城市繁华，躺着享受人生")
    private String introduction;

    /**
     * 照片
     */
    @Schema(description = "照片",example = "https://example.com/room.jpg")
    private String photo;

    /**
     * 状态，0：禁用，1：启用
     */
    @Schema(description = "状态，0：禁用，1：启用",example = "1")
    private Integer status;
    @Schema(description = "备注",example = "无")
    private String remark;
} 