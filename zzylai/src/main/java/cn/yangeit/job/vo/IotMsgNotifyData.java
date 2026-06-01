package cn.yangeit.job.vo;

import lombok.Data;

// amqp订阅消息
@Data
public class IotMsgNotifyData {

    /**
     * amqp消息-header部分
     */
    private IotMsgHeader header;

    /**
     * amqp消息-body部分
     */
    private IotMsgBody body;
}