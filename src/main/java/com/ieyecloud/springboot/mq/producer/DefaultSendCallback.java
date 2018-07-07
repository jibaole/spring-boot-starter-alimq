package com.ieyecloud.springboot.mq.producer;

import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jibaole
 * @version 1.0
 * @desc 消息者监听(订阅消费内容)
 * @date 2018/7/7 下午5:29
 */
public class DefaultSendCallback implements SendCallback {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultSendCallback.class);

    @Override
    public void onSuccess(SendResult sendResult) {
        LOG.info("send message success. " + sendResult.toString());
    }

    @Override
    public void onException(OnExceptionContext context) {
        LOG.warn("send message failed. topic=" + context.getTopic() + ", msgId=" + context.getMessageId(), context.getException());
    }
}
