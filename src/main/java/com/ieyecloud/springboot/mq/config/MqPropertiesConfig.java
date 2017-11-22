package com.ieyecloud.springboot.mq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * @author wuxy@ieyecloud.com
 * @date 2017/10/27 14:06
 */
@ConfigurationProperties(prefix = "aliyun.mq")
public class MqPropertiesConfig {
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
