package com.wzh.factory.data.user;

import android.text.TextUtils;

import com.wzh.factory.data.helper.DbHelper;
import com.wzh.factory.model.card.UserCard;
import com.wzh.factory.model.db.User;
import com.wzh.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by HP on 2017/11/30.
 *
 * @author by wangWei
 */

public class UserDispatcher implements UserCenter {

    private static UserCenter instance;
    //单线程池：处理卡片一个个的消息进行处理
    private final Executor executor = Executors.newSingleThreadExecutor();


    public static UserCenter instance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null) {
                    instance = new UserDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(UserCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        //丢到线程中去
        executor.execute(new UserCardHandler(cards));

    }

    private class UserCardHandler implements Runnable {
        private final UserCard[] cards;

        private UserCardHandler(UserCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<User> users = new ArrayList<>();
            for (UserCard card : cards) {
                if (card == null || TextUtils.isEmpty(card.getId())) {
                    continue;
                }
                users.add(card.build());
            }
            //进行数据库存储，并发送通知，异步操作
            DbHelper.save(User.class, CollectionUtil.toArray(users, User.class));
        }
    }
}
