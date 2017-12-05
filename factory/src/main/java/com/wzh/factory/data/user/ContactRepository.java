package com.wzh.factory.data.user;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wzh.factory.data.BaseDbRepository;
import com.wzh.factory.model.db.User;
import com.wzh.factory.model.db.User_Table;
import com.wzh.factory.persistence.Account;

import java.util.List;

/**
 * Created by HP on 2017/12/4.
 *
 * @author by wangWei
 */

public class ContactRepository extends BaseDbRepository<User> implements ContactDataSource {


    @Override
    public void load(SucceedCallback<List<User>> callback) {
        super.load(callback);
        // 加载本地数据库数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();


    }

    @Override
    protected boolean isRequired(User user) {
        return user.isFollow()&&!user.getId().equals(Account.getUserId());
    }
}
