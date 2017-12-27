package com.wzh.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.wzh.factory.data.BaseDbRepository;
import com.wzh.factory.model.db.Message;
import com.wzh.factory.model.db.Message_Table;

import java.util.Collections;
import java.util.List;

/**
 * Created by HP on 2017/12/7.
 *
 * @author by wangWei
 */

public class MessageGroupRepository extends BaseDbRepository<Message> implements MessageDataSource {
    //聊天对象id
    private String receiverId;

    public MessageGroupRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);
       // 无论是直接发还是别人发，只要是发到这个群的，
        // 那个这个group_id就是receiverId
        SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();


    }

    @Override
    protected boolean isRequired(Message message) {
        return message.getGroup() != null
                && receiverId.equalsIgnoreCase(message.getGroup().getId());
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
