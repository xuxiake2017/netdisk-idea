package group.xuxiake.service.impl;

import com.google.gson.Gson;
import group.xuxiake.entity.mqtt.MessageBase;
import group.xuxiake.mqtt.MqttGateway;
import group.xuxiake.service.MqttService;
import group.xuxiake.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("mqttService")
public class MqttServiceImpl implements MqttService {

    @Resource
    private MqttGateway mqttGateway;

    @Override
    public Result lightOption(String type, String status) {

        Result result = new Result();
        if (StringUtils.isAnyEmpty(type, status)) {
            return Result.paramIsNull(result);
        }
        MessageBase messageBase = new MessageBase();
        messageBase.setType(type);
        messageBase.setStatus(status);
        String content = new Gson().toJson(messageBase);
        String topic = "inTopic";
        mqttGateway.sendMessage(content.getBytes(), topic);
        return result;
    }
}
