package cn.knowbox.book.alimq.consumer;

import cn.knowbox.book.alimq.annotation.RocketMQMessageListener;
import cn.knowbox.book.alimq.consumer.AbstractMessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import java.util.Properties;

/**
 * @author jibaole
 * @version 1.0
 * @desc 消费者
 * @date 2018/7/7 下午5:19
 */
@Slf4j
public class Consumer implements BeanPostProcessor {

    private Properties properties;
    private com.aliyun.openservices.ons.api.Consumer consumer;
    private String topic;

    public Consumer(Properties properties) {
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

    /****
     * @Description: 多个tag用'||'拼接，所有用*
     * @Param: [tags, messageListener]
     * @Author: jibaole
     */
    public void subscribe(String tags, AbstractMessageListener messageListener) {
        log.info("subscribe [topic: {}, tags: {}, messageListener: {}]", topic, tags, messageListener.getClass().getCanonicalName());
        consumer.subscribe(topic, tags, messageListener);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    /**
     * @Description: 获取所有消费者订阅内容(Topic、Tag)
     * @Param: [bean, beanName]
     * @Author: jibaole
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = AopUtils.getTargetClass(bean);
        RocketMQMessageListener annotation = clazz.getAnnotation(RocketMQMessageListener.class);
        if (null != annotation) {
            AbstractMessageListener listener = (AbstractMessageListener) bean;
            consumer.subscribe(annotation.topic(), annotation.tag(), listener);
        }
        return bean;
    }
}
