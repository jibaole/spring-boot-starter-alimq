package com.ieyecloud.springboot.mq.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.ieyecloud.springboot.mq.consumer.MqConsumer;
import com.ieyecloud.springboot.mq.producer.MqTimerProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author wuxy@ieyecloud.com
 * @date 2017/10/27 14:23
 */
@Configuration
@EnableConfigurationProperties(MqPropertiesConfig.class)
public class MqAutoConfig {

    @Autowired
    private MqPropertiesConfig propConfig;

    @Bean(initMethod="start", destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "aliyun.mq.producer",value = "enabled",havingValue = "true")
    public MqTimerProducer mqTimerProducer(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ProducerId, propConfig.getProducer().getProperty("producerId"));
        properties.setProperty(PropertyKeyConst.AccessKey, propConfig.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, propConfig.getSecretKey());
        properties.setProperty(PropertyKeyConst.ONSAddr, propConfig.getOnsAddr());
        properties.setProperty("topic", propConfig.getTopic());
        return  new MqTimerProducer(properties);
    }

    @Bean(initMethod="start", destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "aliyun.mq.consumer",value = "enabled",havingValue = "true")
    public MqConsumer mqConsumer(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ConsumerId, propConfig.getConsumer().getProperty("consumerId"));
        properties.setProperty(PropertyKeyConst.AccessKey, propConfig.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, propConfig.getSecretKey());
        properties.setProperty(PropertyKeyConst.ONSAddr, propConfig.getOnsAddr());
        properties.setProperty("topic", propConfig.getTopic());
        return  new MqConsumer(properties);
    }
}
