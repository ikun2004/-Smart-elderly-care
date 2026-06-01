package cn.yangeit.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
//"设备详情响应模型")
public class DeviceDetailVo {
    @Schema(description =  "设备id")
    private Long id;
    @Schema(description =  "物联网设备id")
    private String iotId;
    @Schema(description =  "设备名称")
    private String deviceName;
    @Schema(description =  "设备标识码")
    private String nodeId;
    @Schema(description =  "设备秘钥")
    private String secret;
    @Schema(description =  "产品id")
    public String productKey;
    @Schema(description =  "产品名称")
    public String productName;
    @Schema(description =  "位置类型 0 随身设备 1固定设备")
    private Integer locationType;
    @Schema(description =  "绑定位置,如果是随身设备为老人id，如果是固定设备为位置的最后一级id")
    private Long bindingLocation;
    @Schema(description =  "接入位置")
    private String remark;
    @Schema(description =  "设备状态，ONLINE：设备在线，OFFLINE：设备离线，ABNORMAL：设备异常，INACTIVE：设备未激活，FROZEN：设备冻结")
    private String deviceStatus;
    @Schema(description =  "激活时间,格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime activeTime;
    @Schema(description =  "创建时间,格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    @Schema(description =  "创建人id")
    private Long createBy;
    @Schema(description =  "创建人名称")
    private String creator;
    @Schema(description =  "位置备注")
    private String deviceDescription;
}
