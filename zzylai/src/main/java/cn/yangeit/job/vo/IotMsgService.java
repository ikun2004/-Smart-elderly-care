package cn.yangeit.job.vo;

import lombok.Data;

import java.util.Map;

//amqp消息
@Data
public class IotMsgService {
    /**
     * 服务id
     */
    private String serviceId;

    /**
     * 设备上报属性
     */
    private Map<String, Object> properties;

    /**
     * 时间,格式：yyyyMMdd'T'HHmmss'Z'
     */
    private String eventTime;
}
