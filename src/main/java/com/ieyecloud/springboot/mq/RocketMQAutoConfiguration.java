package com.ieyecloud.springboot.mq;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.ieyecloud.springboot.mq.consumer.Consumer;
import com.ieyecloud.springboot.mq.producer.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author jibaole
 * @version 1.0
 * @desc 初始化(生成|消费)相关配置
 * @date 2018/7/7 下午5:19
 */
@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
public class RocketMQAutoConfiguration {

    @Autowired
    private RocketMQProperties propConfig;

    @Bean(initMethod="start", destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "aliyun.mq.producer",value = "enabled",havingValue = "true")
    public RocketMQTemplate mqTimerProducer(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ProducerId, propConfig.getProducer().getProperty("producerId"));
        properties.setProperty(PropertyKeyConst.AccessKey, propConfig.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, propConfig.getSecretKey());
        properties.setProperty(PropertyKeyConst.ONSAddr, propConfig.getOnsAddr());
        properties.setProperty("topic", propConfig.getTopic());
        return  new RocketMQTemplate(properties);
    }

    @Bean(initMethod="start", destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "aliyun.mq.consumer",value = "enabled",havingValue = "true")
    public Consumer mqConsumer(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ConsumerId, propConfig.getConsumer().getProperty("consumerId"));
        properties.setProperty(PropertyKeyConst.AccessKey, propConfig.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, propConfig.getSecretKey());
        properties.setProperty(PropertyKeyConst.ONSAddr, propConfig.getOnsAddr());
        properties.setProperty("topic", propConfig.getTopic());
        return  new Consumer(properties);
    }
}
