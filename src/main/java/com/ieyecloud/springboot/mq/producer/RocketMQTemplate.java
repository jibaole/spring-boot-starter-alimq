package com.ieyecloud.springboot.mq.producer;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import lombok.extern.slf4j.Slf4j;


/**
 * @author jibaole
 * @version 1.0
 * @desc 生产者
 * @date 2018/7/7 下午5:19
 */
@Slf4j
public class RocketMQTemplate {
    private ProducerBean producer;


    public void send(String topic, String tag, String body, long delay) {
        log.info("start to send message. [topic: {}, tag: {}, body: {}, delay: {}]", topic, tag, body, delay);
        if (topic == null || tag == null || body == null) {
            throw new RuntimeException("topic, tag, or body is null.");
        }
        Message message = new Message(topic, tag, body.getBytes());
        message.setStartDeliverTime(System.currentTimeMillis() + delay);
        SendResult result = this.producer.send(message);
        log.info("send message success. ", result.toString());
    }


    public void sendAsync(String topic, String tag, String body, long delay) {
        log.info("start to send message async. [topic: {}, tag: {}, body: {}, delay: {}]", topic, tag, body, delay);
        if (topic == null || tag == null || body == null) {
            throw new RuntimeException("topic, tag, or body is null.");
        }
        Message message = new Message(topic, tag, body.getBytes());
        message.setStartDeliverTime(System.currentTimeMillis() + delay);
        this.producer.sendAsync(message, new DefaultSendCallback());
    }


}
