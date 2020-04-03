package group.xuxiake.controller;

import group.xuxiake.service.MqttService;
import group.xuxiake.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Resource
    private MqttService mqttService;

    @RequestMapping("/lightOption")
    public Result lightOption(String type, String status) {

        return mqttService.lightOption(type, status);
    }
}
