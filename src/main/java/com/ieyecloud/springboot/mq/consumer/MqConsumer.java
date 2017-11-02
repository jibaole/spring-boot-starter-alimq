package com.ieyecloud.springboot.mq.consumer;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author wuxy@ieyecloud.com
 * @date 2017/10/27 14:25
 */
public class MqConsumer {

    private final static Logger LOG = LoggerFactory.getLogger(MqConsumer.class);
    private Properties properties;
    private Consumer consumer;
    private String topic;

    public MqConsumer(Properties properties) {
        if (properties == null || properties.get(PropertyKeyConst.ConsumerId) == null
                || properties.get(PropertyKeyConst.AccessKey) == null
                || properties.get(PropertyKeyConst.SecretKey) == null
                || properties.get(PropertyKeyConst.ONSAddr) == null
                || properties.get("topic") == null) {
            throw new ONSClientException("consumer properties not set properly.");
        }
        this.properties = properties;
        this.topic = properties.getProperty("topic");
    }

    public void start() {
        this.consumer = ONSFactory.createConsumer(properties);
        this.consumer.start();
    }

    public void shutdown() {
        if (this.consumer != null) {
            this.consumer.shutdown();
        }
    }

    /**
     * @param tags            多个tag用'||'拼接，所有用*
     * @param messageListener
     */
    public void subscribe(String tags, AbstractMessageListener messageListener) {
        LOG.info("subscribe [topic: {}, tags: {}, messageListener: {}]", topic, tags, messageListener.getClass().getCanonicalName());
        consumer.subscribe(topic, tags, messageListener);
    }
}
