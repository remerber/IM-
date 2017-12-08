package com.wzh.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.wzh.factory.data.BaseDbRepository;
import com.wzh.factory.model.db.Session;
import com.wzh.factory.model.db.Session_Table;

import java.util.Collections;
import java.util.List;

/**
 * Created by HP on 2017/12/8.
 *
 * @author by wangWei
 */

public class SessionRepository extends BaseDbRepository<Session> implements SessionDataSource {

    @Override
    public void load(SucceedCallback<List<Session>> callback) {
        super.load(callback);
// 数据库查询
        SQLite.select()
                .from(Session.class)
                .orderBy(Session_Table.modifyAt, false) // false 是倒序
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();

    }

    @Override
    protected boolean isRequired(Session session) {
        return true;
    }

    @Override
    protected void insert(Session session) {
        dataList.add(session);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
