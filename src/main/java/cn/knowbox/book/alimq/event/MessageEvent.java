package cn.knowbox.book.alimq.event;

import lombok.Data;
import java.io.Serializable;
import java.util.UUID;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;

/**
 * @author jibaole
 * @version 1.0
 * @desc MQ统一事件对象，用在跨业务整合中
 * @date 2018/6/29 上午10:46
 */

@Data
public class MessageEvent implements Serializable {

    private static final long serialVersionUID = -2624253925403159396L;

    /**
     * 事件序列ID
     */
    private String txId;

    /**
     * 话题的名字
     */
    private String topic;

    /**
     * 话题的名字
     */
    private String tag;

    /**
     * 需要传递的领域对象
     */
    private Object domain;
    
    /**
     * 传递的领域对象的唯一标识,用来构建消息的唯一标识,不检测重复,可以为空,不影响消息收发
     */
    private String domainKey;


    /**
     * 事件创建时间
     */
    private long createdDate = System.currentTimeMillis();


    /**
     * 方便的生成TxId的方法
     *
     * @return
     */
    public String generateTxId() {
        if (null == txId) {
        	txId = getTopic() + ":" + getTag()+":";
        	if(StringUtil.isNullOrEmpty(domainKey)) {
        		txId = txId + getCreatedDate() + ":" + UUID.randomUUID().toString();
        	}else {
        		txId = txId + domainKey; 
        	} 
        }
        return txId;
    }

}
