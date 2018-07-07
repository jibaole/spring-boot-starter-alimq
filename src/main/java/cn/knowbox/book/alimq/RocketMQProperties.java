package cn.knowbox.book.alimq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Properties;

/**
 * @author jibaole
 * @version 1.0
 * @desc 配置Bean
 * @date 2018/7/7 下午5:19
 */

@ConfigurationProperties(prefix = "aliyun.mq")
@Data
public class RocketMQProperties {

    private String onsAddr;

    private String topic;

    private String accessKey;

    private String secretKey;

    private Properties producer;

    private Properties consumer;

    private String tagSuffix;
}
