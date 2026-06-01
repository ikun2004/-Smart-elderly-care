package cn.yangeit.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeviceDto {

    private Long id;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "备标识码，通常使用IMEI、MAC地址或Serial No作为node_id", required = true)
    private String nodeId;
    @Schema(description =  "设备id")
    public String iotId;
    @Schema(description =  "产品的id")
    public String productKey;
    @Schema(description =  "产品名称")
    private String productName;
    @Schema(description =  "位置名称回显字段")
    private String deviceDescription;
    @Schema(description =  "位置类型 0 老人 1位置")
    Integer locationType;
    @Schema(description =  "绑定位置，如床号，房间号，位置id，如果是老人，则为老人id")
    Long bindingLocation;
    @Schema(description =  "设备名称")
    String deviceName;
    @Schema(description =  "物理位置类型 0楼层 1房间 2床位")
    Integer physicalLocationType;
}
