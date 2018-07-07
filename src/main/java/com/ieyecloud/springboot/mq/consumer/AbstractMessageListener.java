package com.ieyecloud.springboot.mq.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jibaole
 * @version 1.0
 * @desc 消息监听者需要继承该抽象类，实现handle方法，消息消费逻辑处理(如果抛出异常，则重新入队列)
 * @date 2018/7/7 下午5:19
 */
public abstract class AbstractMessageListener implements MessageListener {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractMessageListener.class);

    public abstract void handle(String body);

    @Override
    public Action consume(Message message, ConsumeContext context) {
        LOG.info("receive message. [topic: {}, tag: {}, body: {}, msgId: {}, startDeliverTime: {}]", message.getTopic(), message.getTag(), new String(message.getBody()), message.getMsgID(), message.getStartDeliverTime());
        try {
            handle(new String(message.getBody()));
            LOG.info("handle message success.");
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            LOG.warn("handle message fail, requeue it.", e);
            return Action.ReconsumeLater;
        }
    }
}
