package cn.knowbox.book.alimq;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
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


    @Bean(name = "producer",initMethod = "start", destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "aliyun.mq.producer",value = "enabled",havingValue = "true")
    public ProducerBean producer() {
        ProducerBean producerBean = new ProducerBean();
        Properties properties = new Properties();
        System.out.println("执行producer初始化");
        properties.put(PropertyKeyConst.ProducerId, propConfig.getProducer().getProperty("producerId"));
        properties.put(PropertyKeyConst.AccessKey, propConfig.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, propConfig.getSecretKey());
        properties.put(PropertyKeyConst.ONSAddr, propConfig.getOnsAddr());
        producerBean.setProperties(properties);
        producerBean.start();
        return producerBean;
    }

    @Bean(name = "consumer",initMethod = "start", destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "aliyun.mq.consumer",value = "enabled",havingValue = "true")
    public ConsumerBean consumer(){
        ConsumerBean consumerBean = new ConsumerBean();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ConsumerId, propConfig.getConsumer().getProperty("consumerId"));
        properties.setProperty(PropertyKeyConst.AccessKey, propConfig.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, propConfig.getSecretKey());
        properties.setProperty(PropertyKeyConst.ONSAddr, propConfig.getOnsAddr());
        consumerBean.setProperties(properties);
        return  consumerBean;
    }
}
