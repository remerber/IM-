package com.wzh.factory.data.message;

import com.wzh.factory.model.card.MessageCard;

/**
 * Created by HP on 2017/11/30.
 * 消息的中心，进行消息卡片的消费
 *
 * @author by wangWei
 */

public interface MessageCenter {

    void dispatch(MessageCard... cards);
}
