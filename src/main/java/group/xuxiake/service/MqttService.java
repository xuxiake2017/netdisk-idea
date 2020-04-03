package group.xuxiake.service;

import group.xuxiake.util.Result;

public interface MqttService {

    Result lightOption(String type, String status);
}
