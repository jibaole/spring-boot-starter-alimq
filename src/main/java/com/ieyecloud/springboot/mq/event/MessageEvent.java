package com.ieyecloud.springboot.mq.event;

import lombok.Data;
import java.io.Serializable;
import java.util.UUID;

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
            txId = getTopic() + ":" + getCreatedDate() + ":" + UUID.randomUUID().toString();
        }
        return txId;
    }

}
