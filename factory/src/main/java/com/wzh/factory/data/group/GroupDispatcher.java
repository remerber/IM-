package com.wzh.factory.data.group;

import com.wzh.factory.data.helper.DbHelper;
import com.wzh.factory.data.helper.GroupHelper;
import com.wzh.factory.data.helper.UserHelper;
import com.wzh.factory.model.card.GroupCard;
import com.wzh.factory.model.card.GroupMemberCard;
import com.wzh.factory.model.db.Group;
import com.wzh.factory.model.db.GroupMember;
import com.wzh.factory.model.db.User;
import com.wzh.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by HP on 2017/12/1.
 * 群成员/群卡片中心的实现类
 *
 * @author by wangWei
 */

public class GroupDispatcher implements GroupCenter {
    private static GroupCenter instance;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static GroupCenter instance() {
        if (instance == null) {
            synchronized (GroupDispatcher.class) {
                if (instance == null) {
                    instance = new GroupDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(GroupCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        executor.execute(new GroupHandler(cards));
    }

    @Override
    public void dispatch(GroupMemberCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        executor.execute(new GroupMemberRspHandler(cards));
    }

    private class GroupMemberRspHandler implements Runnable {
        private final GroupMemberCard[] cards;

        GroupMemberRspHandler(GroupMemberCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<GroupMember> members = new ArrayList<>();
            for (GroupMemberCard model : cards) {
                // 成员对应的人的信息
                User user = UserHelper.search(model.getUserId());
                // 成员对应的群的信息
                Group group = GroupHelper.find(model.getGroupId());
                if (user != null && group != null) {
                    GroupMember member = model.build(group, user);
                    members.add(member);
                }
            }
            if (members.size() > 0) {
                DbHelper.save(GroupMember.class, CollectionUtil.toArray(members, GroupMember.class));
            }
        }
    }

    /**
     * 把群Card处理为群DB类
     */
    private class GroupHandler implements Runnable {
        private final GroupCard[] cards;

        GroupHandler(GroupCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<Group> groups = new ArrayList<>();
            for (GroupCard card : cards) {
                // 搜索管理员
                User owner = UserHelper.search(card.getOwnerId());
                if (owner != null) {
                    Group group = card.build(owner);
                    groups.add(group);
                }
            }
            if (groups.size() > 0) {
                DbHelper.save(Group.class, CollectionUtil.toArray(groups, Group.class));
            }
        }
    }
}
