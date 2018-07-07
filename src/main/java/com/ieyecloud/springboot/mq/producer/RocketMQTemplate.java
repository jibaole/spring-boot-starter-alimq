package com.ieyecloud.springboot.mq.producer;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import lombok.extern.slf4j.Slf4j;
import java.util.Properties;

/**
 * @author jibaole
 * @version 1.0
 * @desc 生产者
 * @date 2018/7/7 下午5:19
 */
@Slf4j
public class RocketMQTemplate {
    private Properties properties;
    private Producer producer;
    private String topic;

    public RocketMQTemplate(Properties properties) {
        if (properties == null || properties.get(PropertyKeyConst.ProducerId) == null
                || properties.get(PropertyKeyConst.AccessKey) == null
                || properties.get(PropertyKeyConst.SecretKey) == null
                || properties.get(PropertyKeyConst.ONSAddr) == null
                || properties.get("topic") == null) {
            throw new ONSClientException("producer properties not set properly.");
        }
        this.properties = properties;
        this.topic = properties.getProperty("topic");
    }

    public void start() {
        this.producer = ONSFactory.createProducer(this.properties);
        this.producer.start();
    }

    public void shutdown() {
        if (this.producer != null) {
            this.producer.shutdown();
        }
    }

    public void send(String tag, String body, long delay) {
        log.info("start to send message. [topic: {}, tag: {}, body: {}, delay: {}]", topic, tag, body, delay);
        if (topic == null || tag == null || body == null) {
            throw new RuntimeException("topic, tag, or body is null.");
        }
        Message message = new Message(topic, tag, body.getBytes());
        message.setStartDeliverTime(System.currentTimeMillis() + delay);
        SendResult result = this.producer.send(message);
        log.info("send message success. ", result.toString());
    }


    public void sendAsync(String tag, String body, long delay) {
        this.sendAsync(tag, body, delay, new DefaultSendCallback());
    }

    public void sendAsync(String tag, String body, long delay, SendCallback sendCallback) {
        log.info("start to send message async. [topic: {}, tag: {}, body: {}, delay: {}]", topic, tag, body, delay);
        if (topic == null || tag == null || body == null) {
            throw new RuntimeException("topic, tag, or body is null.");
        }
        Message message = new Message(topic, tag, body.getBytes());
        message.setStartDeliverTime(System.currentTimeMillis() + delay);
        this.producer.sendAsync(message, sendCallback);
    }


    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public boolean isStarted() {
        return this.producer.isStarted();
    }

    public boolean isClosed() {
        return this.producer.isClosed();
    }
}
