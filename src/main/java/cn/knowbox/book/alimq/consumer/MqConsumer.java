package cn.knowbox.book.alimq.consumer;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Properties;

/**
 * @author jibaole
 * @version 1.0
 * @desc 消费者
 * @date 2018/7/7 下午5:19
 */
@Slf4j
public class MqConsumer  {

    private Properties properties;
    @Autowired
    private com.aliyun.openservices.ons.api.Consumer consumer;

    public MqConsumer(Properties properties) {
        if (properties == null || properties.get(PropertyKeyConst.ConsumerId) == null
                || properties.get(PropertyKeyConst.AccessKey) == null
                || properties.get(PropertyKeyConst.SecretKey) == null
                || properties.get(PropertyKeyConst.ONSAddr) == null) {
            throw new ONSClientException("consumer properties not set properly.");
        }
        this.properties = properties;
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
     * @des
     * @param topic
     * @param messageListener
     */
    public void subscribe(String topic, AbstractMessageListener messageListener) {
        consumer.subscribe(topic, "*", messageListener);
    }

    /**
     * @des 多个tag用'||'拼接，所有用*
     * @param topic
     * @param tag
     * @param messageListener
     */
    public void subscribe(String topic,String tag, AbstractMessageListener messageListener) {
        consumer.subscribe(topic, tag, messageListener);
    }
}
