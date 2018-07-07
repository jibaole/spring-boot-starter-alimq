package com.ieyecloud.springboot.mq.producer;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.ieyecloud.springboot.mq.event.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;

/**
 * @author jibaole
 * @version 1.0
 * @desc 生产者
 * @date 2018/7/7 下午5:19
 */
@Slf4j
public class RocketMQTemplate {

    @Resource
    private ProducerBean producer;


    public SendResult send(MessageEvent event) {
        log.info("start to send message. [topic: {}, tag: {}]", event.getTopic(), event.getTag());
        if (StringUtils.isEmpty(event.getTopic()) || StringUtils.isEmpty(event.getTag()) || null == event.getDomain()) {
            throw new RuntimeException("topic, tag, or body is null.");
        }
        Message message = new Message(event.getTopic(), event.getTag(), SerializationUtils.serialize(event.getDomain()));
        SendResult result = this.producer.send(message);
        log.info("send message success. ", result.toString());
        return result;
    }

    public SendResult send(MessageEvent event, long delay) {
        log.info("start to send message. [topic: {}, tag: {}]", event.getTopic(), event.getTag());
        if (StringUtils.isEmpty(event.getTopic()) || StringUtils.isEmpty(event.getTag()) || null == event.getDomain()) {
            throw new RuntimeException("topic, tag, or body is null.");
        }
        Message message = new Message(event.getTopic(), event.getTag(), SerializationUtils.serialize(event.getDomain()));
        message.setStartDeliverTime(System.currentTimeMillis() + delay);
        SendResult result = this.producer.send(message);
        log.info("send message success. ", result.toString());
        return result;
    }

    public void sendOneway(MessageEvent event) {
        log.info("start to send message. [topic: {}, tag: {}]", event.getTopic(), event.getTag());
        if (StringUtils.isEmpty(event.getTopic()) || StringUtils.isEmpty(event.getTag()) || null == event.getDomain()) {
            throw new RuntimeException("topic, tag, or body is null.");
        }
        Message message = new Message(event.getTopic(), event.getTag(), SerializationUtils.serialize(event.getDomain()));
        this.producer.sendOneway(message);
        log.info("send message success. ");
    }

    public void sendAsync(MessageEvent event) {
        log.info("start to send message. [topic: {}, tag: {}]", event.getTopic(), event.getTag());
        if (StringUtils.isEmpty(event.getTopic()) || StringUtils.isEmpty(event.getTag()) || null == event.getDomain()) {
            throw new RuntimeException("topic, tag, or body is null.");
        }
        Message message = new Message(event.getTopic(), event.getTag(), SerializationUtils.serialize(event.getDomain()));
        this.producer.sendAsync(message, new DefaultSendCallback());
    }

    public void sendAsync(MessageEvent event, long delay) {
        log.info("start to send message. [topic: {}, tag: {}]", event.getTopic(), event.getTag());
        if (StringUtils.isEmpty(event.getTopic()) || StringUtils.isEmpty(event.getTag()) || null == event.getDomain()) {
            throw new RuntimeException("topic, tag, or body is null.");
        }
        Message message = new Message(event.getTopic(), event.getTag(), SerializationUtils.serialize(event.getDomain()));
        message.setStartDeliverTime(System.currentTimeMillis() + delay);
        this.producer.sendAsync(message, new DefaultSendCallback());
    }


}
