package cn.yangeit.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.Builder;


@TableName(value ="device_data")
@Data
@Builder
public class DeviceData {

    @TableId(type = IdType.AUTO)
    private Long id;//  主键
    private String deviceName;//设备名称
    private String iotId;//设备ID
    private String nickname;// 备注名称
    private String productKey;// 所属产品的key
    private String productName;//产品名称
    private String functionId;// 功能名称
    private String accessLocation;// 接入位置
    private Integer locationType;//位置类型 0：随身设备 1：固定设备
    private Integer physicalLocationType;//物理位置类型 0楼层 1房间 2床位
    private String deviceDescription;//位置备注
    private String dataValue;//数据值
    private LocalDateTime alarmTime;//数据上报时间
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;// 更新时间
    private Long createBy;//创建人id
    private Long updateBy;//更新人id
    private String remark;//备注
}
