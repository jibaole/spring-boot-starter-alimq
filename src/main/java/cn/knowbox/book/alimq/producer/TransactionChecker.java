package cn.knowbox.book.alimq.producer;

import com.aliyun.openservices.ons.api.transaction.TransactionStatus;

import cn.knowbox.book.alimq.event.MessageEvent;

/**
 * 用作LocalTransactionExecuterImpl的构造器参数,用于执行本地事务操作
 * @author hengxi
 *
 */
@FunctionalInterface
public interface TransactionChecker {
	TransactionStatus check(MessageEvent messageEvent, Long hashValue);
}
