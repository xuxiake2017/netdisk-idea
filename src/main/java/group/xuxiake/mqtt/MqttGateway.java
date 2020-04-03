package group.xuxiake.mqtt;

import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface MqttGateway {

    void sendMessage(byte[] data, @Header(MqttHeaders.TOPIC) String topic);
}
