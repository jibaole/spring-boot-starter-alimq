package cn.knowbox.book.alimq.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;

/**
 * @author jibaole
 * @version 1.0
 * @desc 消息监听者需要继承该抽象类，实现handle方法，消息消费逻辑处理(如果抛出异常，则重新入队列)
 * @date 2018/7/7 下午5:19
 */
@Slf4j
public abstract class AbstractMessageListener<T> implements MessageListener {
    public abstract void handle(T body);

    @Override
    public Action consume(Message message, ConsumeContext context) {
        log.info("接收消息:[topic: {}, tag: {}, msgId: {}, startDeliverTime: {}]", message.getTopic(), message.getTag(), message.getMsgID(), message.getStartDeliverTime());
        try {
            handle((T)SerializationUtils.deserialize(message.getBody()));
            log.info("handle message success. message id:"+message.getMsgID());
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            log.warn("handle message fail, requeue it. message id:"+message.getMsgID(), e);
            return Action.ReconsumeLater;
        }
    }
}
