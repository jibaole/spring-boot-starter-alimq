package com.ieyecloud.springboot.mq.consumer;

import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.ieyecloud.springboot.mq.annotation.RocketMQMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import javax.annotation.Resource;

/**
 * @author jibaole
 * @version 1.0
 * @desc 消费者
 * @date 2018/7/7 下午5:19
 */
@Slf4j
public class Consumer implements BeanPostProcessor {
    @Resource
    private ConsumerBean consumer;


    /**
     * @Description: 获取所有消费者订阅内容(Topic 、 Tag);多个tag用'||'拼接，所有用*
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

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
