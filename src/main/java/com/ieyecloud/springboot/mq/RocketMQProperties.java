package com.ieyecloud.springboot.mq;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * @author jibaole
 * @version 1.0
 * @desc 配置Bean
 * @date 2018/7/7 下午5:19
 */

@ConfigurationProperties(prefix = "aliyun.mq")
public class RocketMQProperties {
    private String onsAddr;
    private String topic;
    private String accessKey;
    private String secretKey;
    private Properties producer;
    private Properties consumer;
    private String tagSuffix;

    public String getOnsAddr() {
        return onsAddr;
    }

    public void setOnsAddr(String onsAddr) {
        this.onsAddr = onsAddr;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Properties getProducer() {
        return producer;
    }

    public void setProducer(Properties producer) {
        this.producer = producer;
    }

    public Properties getConsumer() {
        return consumer;
    }

    public void setConsumer(Properties consumer) {
        this.consumer = consumer;
    }

    public String getTagSuffix() {
        return tagSuffix;
    }

    public void setTagSuffix(String tagSuffix) {
        this.tagSuffix = tagSuffix;
    }
}
