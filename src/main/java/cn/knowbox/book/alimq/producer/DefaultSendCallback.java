package cn.knowbox.book.alimq.producer;

import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jibaole
 * @version 1.0
 * @desc 消息者监听(订阅消费内容)
 * @date 2018/7/7 下午5:29
 */
@Slf4j
public class DefaultSendCallback implements SendCallback {


    @Override
    public void onSuccess(SendResult sendResult) {
        log.info("消息发送成功:  topic=" + sendResult.getTopic() + ", msgId=" + sendResult.getMessageId());
    }

    @Override
    public void onException(OnExceptionContext context) {
        log.warn("消息发送失败: topic=" + context.getTopic() + ", msgId=" + context.getMessageId(), context.getException());
    }
}
