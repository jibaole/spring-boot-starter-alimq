package cn.knowbox.book.alimq.producer;

import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;

import cn.knowbox.book.alimq.event.MessageEvent;
import cn.knowbox.book.alimq.utils.HashUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认的本地事务执行器实现
 * @author hengxi
 *
 */
@Slf4j
public class LocalTransactionExecuterImpl implements LocalTransactionExecuter {
	
	private TransactionExecuter transactionExecuter;
	public LocalTransactionExecuterImpl(TransactionExecuter transactionExecuter) {
		this.transactionExecuter = transactionExecuter;
	}
    @Override
    public TransactionStatus execute(Message msg, Object arg) {
        // 消息 ID（有可能消息体一样，但消息 ID 不一样，当前消息 ID 在控制台无法查询）
        String msgId = msg.getMsgID();
        // 消息体内容进行 crc32，也可以使用其它的如 MD5
        long crc32Id = HashUtil.crc32Code(msg.getBody());
        // 消息 ID 和 crc32id 主要是用来防止消息重复
        // 如果业务本身是幂等的，可以忽略，否则需要利用 msgId 或 crc32Id 来做幂等
        // 如果要求消息绝对不重复，推荐做法是对消息体 body 使用 crc32或 md5来防止重复消息
        TransactionStatus transactionStatus = TransactionStatus.Unknow;
        MessageEvent messageEvent = null;
        try {
        	messageEvent = (MessageEvent)SerializationUtils.deserialize(msg.getBody());
        	transactionStatus = transactionExecuter.executer(messageEvent,crc32Id,arg);
        } catch (Exception e) {
            log.error("TransactionChecker.check has error.message id:"+msgId+" , message:"+JSON.toJSONString(messageEvent)+", arg:"+JSON.toJSONString(arg),e);
        }
        return transactionStatus == null?TransactionStatus.Unknow:transactionStatus;
    }

}
