package com.param.bs_backend.config.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.bs_backend.mapper.DeviceMapper;
import com.param.bs_backend.mapper.MessageMapper;
import com.param.bs_backend.pojo.Message;
import com.param.bs_backend.pojo.MqttMessageData;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MqttConsumerCallBack implements MqttCallback {
    private final DeviceMapper deviceMapper;
    private final MessageMapper messageMapper;

    // Constructor injection
    public MqttConsumerCallBack(DeviceMapper deviceMapper,MessageMapper messageMapper) {
        this.deviceMapper = deviceMapper;
        this.messageMapper=messageMapper;
    }


    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 客戶端斷開連接的回調
     */
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("與服務器斷開連接，可重連");
    }

    /**
     * 消息到達的回調
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        try {
            String payload = new String(message.getPayload());

            // 解析 MQTT 消息
            MqttMessageData mqttMessageData = objectMapper.readValue(payload, MqttMessageData.class);

            // 查詢設備名稱對應的所有設備 ID
            List<String> deviceIds = deviceMapper.findDeviceIdsByDeviceName(mqttMessageData.getClientId());

            // 插入消息到數據庫
            for (String deviceId : deviceIds) {
                Message deviceMessage = new Message();
                deviceMessage.setDeviceId(deviceId);
                deviceMessage.setTimestamp(new Date(mqttMessageData.getTimestamp()));
                deviceMessage.setMessageType(mqttMessageData.getAlert() == 1 ? 1 : 0);
                deviceMessage.setMessageContent(mqttMessageData.getInfo());
                deviceMessage.setLatitude(mqttMessageData.getLat());
                deviceMessage.setLongitude(mqttMessageData.getLng());
                deviceMessage.setValue(mqttMessageData.getValue());

                messageMapper.insertMessage(deviceMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(String.format("接收消息主題 : %s", topic));
        System.out.println(String.format("接收消息Qos : %d", message.getQos()));
        System.out.println(String.format("接收消息內容 : %s", new String(message.getPayload())));
        System.out.println(String.format("接收消息retained : %b", message.isRetained()));
    }

    /**
     * 消息發佈成功回調
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println(String.format("接收消息成功"));
    }
}