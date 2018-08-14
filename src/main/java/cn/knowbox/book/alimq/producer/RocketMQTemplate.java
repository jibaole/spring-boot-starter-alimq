package cn.knowbox.book.alimq.producer;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import cn.knowbox.book.alimq.event.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.annotation.Resource;

/**
 * @author jibaole
 * @version 1.0
 * @desc 普通消息,定时消息,延迟消息生产者
 * @date 2018/7/7 下午5:19
 */
@Slf4j
@SuppressWarnings({"WeakerAccess", "unused"})
public class RocketMQTemplate {

    @Resource
    private ProducerBean producer;

    private Message getMessage(MessageEvent event) {
		if(event == null) {
    		throw new RuntimeException("event is null.");
    	}
        log.info("start to send message. [topic: {}, tag: {}]", event.getTopic(), event.getTag());
        if (StringUtils.isEmpty(event.getTopic())  || null == event.getDomain()) {
            throw new RuntimeException("topic, or body is null.");
        }
        Message message = new Message(event.getTopic(), event.getTag(), SerializationUtils.serialize(event));
        message.setKey(event.generateTxId());
		return message;
	}
    
    /****
     * @Description: 同步发送
     * @Param: [event]
     * @Author: jibaole
     */
    public SendResult send(MessageEvent event) {
    	Message message = getMessage(event);
        SendResult result = this.producer.send(message);
        log.info("send message success. ", result.toString());
        return result;
    }

    /****
     * @Description: 同步发送(带延迟时间)
     * @Param: [event, delay]
     * @Author: jibaole
     */
    public SendResult send(MessageEvent event, long delay) {
    	Message message = getMessage(event);
        message.setStartDeliverTime(System.currentTimeMillis() + delay);
        SendResult result = this.producer.send(message);
        log.info("send message success. ", result.toString());
        return result;
    }

    /**
     * @Description: 单向发送
     * @Param: [event]
     * @Author: jibaole
     */
    public void sendOneway(MessageEvent event) {
        Message message = getMessage(event);
        this.producer.sendOneway(message);
        log.info("send message success. ");
    }

    /**
     * @Description: 异步发送
     * @Param: [event]
     * @Author: jibaole
     */
    public void sendAsync(MessageEvent event) {
    	Message message = getMessage(event);
        this.producer.sendAsync(message, new DefaultSendCallback());
    }


    /**
     * @Description: 异步发送(带延迟时间)
     * @Param: [event, delay]
     * @Author: jibaole
     */
    public void sendAsync(MessageEvent event, long delay) {
    	Message message = getMessage(event);
        message.setStartDeliverTime(System.currentTimeMillis() + delay);
        this.producer.sendAsync(message, new DefaultSendCallback());
    }

    /**
     * @Description: 异步发送
     * @Param: [event]
     * @Author: jibaole
     */
    public void sendAsync(MessageEvent event,SendCallback callback) {
    	Message message = getMessage(event);
        this.producer.sendAsync(message, callback);
    }


    /**
     * @Description: 异步发送(带延迟时间)
     * @Param: [event, delay]
     * @Author: jibaole
     */
    public void sendAsync(MessageEvent event, long delay,SendCallback callback) {
    	Message message = getMessage(event);
        message.setStartDeliverTime(System.currentTimeMillis() + delay);
        this.producer.sendAsync(message, callback);
    }
    
    /****
     * @Description: 同步发送(带延迟时间)
     * @Param: [event, delay]
     * @Author: jibaole
     */
    public SendResult send(MessageEvent event, Date date) {
    	long delay = getDelay(date);
    	return send(event,delay);
    }

	private long getDelay(Date date) {
		Date now = new Date();
    	long delay = date.getTime()-now.getTime();
    	if(delay<= 0) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		throw new RuntimeException("消息发送时间:"+sdf.format(date)+" 小于当前时间:"+sdf.format(now));
    	}
		return delay;
	}
    
    /**
     * @Description: 异步发送(带延迟时间)
     * @Param: [event, delay]
     * @Author: jibaole
     */
    public void sendAsync(MessageEvent event, Date date,SendCallback callback) {
    	long delay = getDelay(date);
    	sendAsync(event,delay,callback);
    }
    
    /****
     * @Description: 同步发送(带延迟时间)
     * @Param: [event, delay]
     * @Author: jibaole
     */
    public SendResult send(MessageEvent event, LocalDateTime date) {
    	long delay = getDelay(date);
    	return send(event,delay);
    }

	private long getDelay(LocalDateTime date) {
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime now = LocalDateTime.now();
    	long delay = date.atZone(zone).toInstant().getEpochSecond() - now.atZone(zone).toInstant().getEpochSecond();
    	if(delay<= 0) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		throw new RuntimeException("消息发送时间:"+sdf.format(date)+" 小于当前时间:"+sdf.format(now));
    	}
		return delay;
	}
    
    /**
     * @Description: 异步发送(带延迟时间)
     * @Param: [event, delay]
     * @Author: jibaole
     */
    public void sendAsync(MessageEvent event, LocalDateTime date,SendCallback callback) {
    	long delay = getDelay(date);
    	sendAsync(event,delay,callback);
    }
}
