package com.wzh.factory.data.group;

import com.wzh.factory.model.card.GroupCard;
import com.wzh.factory.model.card.GroupMemberCard;

/**
 * Created by HP on 2017/12/1.
 * 群中心的定义
 *
 * @author by wangWei
 */

public interface GroupCenter {
    // 群卡片的处理
    void dispatch(GroupCard... cards);

    // 群成员的处理
    void dispatch(GroupMemberCard... cards);

}
