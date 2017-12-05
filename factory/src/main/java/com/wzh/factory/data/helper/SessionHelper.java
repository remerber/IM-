package com.wzh.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wzh.factory.model.db.Session;
import com.wzh.factory.model.db.Session_Table;

/**
 * Created by HP on 2017/12/4.
 * 会话辅助工具类
 * @author by wangWei
 */
public class SessionHelper {
    // 从本地查询Session
    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
