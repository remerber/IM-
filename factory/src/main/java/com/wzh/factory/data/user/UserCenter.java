package com.wzh.factory.data.user;

import com.wzh.factory.model.card.UserCard;

/**
 * Created by HP on 2017/11/30.
 *
 * @author by wangWei
 *         用户中心的基本定义
 */

public interface UserCenter {
    /**
     * 分发处理一堆用户卡片的信息，并更新到数据库
     *
     * @param cards
     */
    void dispatch(UserCard... cards);
}
