package cn.knowbox.book.alimq.producer;

import org.springframework.util.SerializationUtils;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;

import cn.knowbox.book.alimq.event.MessageEvent;
import cn.knowbox.book.alimq.utils.HashUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认的本地事务状态检测器实现
 * @author hengxi
 *
 */
@Slf4j
public class LocalTransactionCheckerImpl implements LocalTransactionChecker {
	
	/**
	 * 	根据消息体和消息体hash值判断事务执行是否成功
	 */
	private TransactionChecker transactionCheck;
	
	public LocalTransactionCheckerImpl(TransactionChecker transactionCheck) {
		this.transactionCheck = transactionCheck;
	}
	
	public void init(TransactionChecker transactionCheck) {
		this.transactionCheck = transactionCheck;
	}
	
    @Override
    public TransactionStatus check(Message msg) {
    	if(transactionCheck == null) {
    		log.warn("mq Check 被回调但 LocalTransactionCheckerImpl 需要的 TransactionChecker尚未设置,原因:TransactionMessageTemplate的init方法尚未被调用");
    		return TransactionStatus.Unknow;
    	}
        //消息 ID（有可能消息体一样，但消息 ID 不一样，当前消息属于 Half 消息，所以消息 ID 在控制台无法查询）
        String msgId = msg.getMsgID();
        //消息体内容进行 crc32，也可以使用其它的方法如 MD5
        long crc32Id = HashUtil.crc32Code(msg.getBody());
        //消息 ID、消息本 crc32Id 主要是用来防止消息重复
        //如果业务本身是幂等的，可以忽略，否则需要利用 msgId 或 crc32Id 来做幂等
        //如果要求消息绝对不重复，推荐做法是对消息体使用 crc32 或  md5 来防止重复消息
        TransactionStatus transactionStatus = TransactionStatus.Unknow;
        MessageEvent messageEvent = null;
        Object object = null;
        try {
        	object = SerializationUtils.deserialize(msg.getBody());
        	if(object instanceof MessageEvent) {
        		messageEvent = (MessageEvent)object;
        	}else {
        		log.error("数据异常,回滚事务,message body:"+JSON.toJSONString(object));
        		return TransactionStatus.RollbackTransaction;
        	}
        	transactionStatus = transactionCheck.check(messageEvent,crc32Id);
        }catch (IllegalArgumentException e) {
			if(object == null) {
				log.error("SerializationUtils.deserialize 反序列化失败,message body:"+msg.getBody() == null?null:new String(msg.getBody()));
			}
		} catch (Exception e) {
        	log.error("TransactionChecker.check has error.message id:"+msgId+" , message:"+JSON.toJSONString(messageEvent),e);
        }
        return transactionStatus == null?TransactionStatus.Unknow:transactionStatus;
    }
 }
