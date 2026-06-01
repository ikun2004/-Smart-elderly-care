package cn.yangeit.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value ="device")
@Data
public class Device {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String secret; //  设备秘钥
    private String iotId;//  物联网设备ID
    private String bindingLocation;//绑定位置
    private Integer locationType;//位置类型 0：随身设备 1：固定设备
    private Integer physicalLocationType;// 物理位置类型 0楼层 1房间 2床位
    private String deviceName;//设备名称
    private String nickname;//备注名称
    private String productKey;//产品key
    private String productName;//产品名称
    private String deviceDescription;//位置备注
    private Integer haveEntranceGuard;//产品是否包含门禁，0：否，1：是
    private Date createTime;//创建时间
    private Date updateTime;// 更新时间
    private Long createBy;//创建人id
    private Long updateBy;//更新人id
    private String remark;// 备注
    private String nodeId;//节点id
}
