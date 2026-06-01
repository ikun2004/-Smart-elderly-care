package cn.yangeit.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class AdminDataVo {

    //首页数据
    //用户数量，老人数量，护理服务数量，房型数量，今日预约数量，今日订单数量
    @Schema(description = "用户数量")
    private Integer userCount;
    @Schema(description = "老人数量")
    private Integer elderCount;
    @Schema(description = "护理服务数量")
    private Integer projectCount;
    @Schema(description = "房型数量")
    private Integer roomCount;
    @Schema(description = "今日预约数量")
    private Integer reservationCount;
    @Schema(description = "今日订单数量")
    private Integer orderCount;


}
