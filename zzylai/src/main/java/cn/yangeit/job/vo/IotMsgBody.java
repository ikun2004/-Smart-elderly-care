package cn.yangeit.job.vo;

import lombok.Data;

import java.util.List;

//amqp消息-body部分
@Data
public class IotMsgBody {
    /**
     * 服务列表
     */
    private List<IotMsgService> services;
}
