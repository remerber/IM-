package com.wzh.factory.model.db.view;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.wzh.factory.model.db.AppDatabase;

/**
 * 群成员对应的用户的简单表查询
 * Created by HP on 2017/12/15.
 *
 * @author by wangWei
 */
@QueryModel(database = AppDatabase.class)
public class MemberUserModel {

    @Column
    public String userId; // User-id/Member-userId
    @Column
    public String name; // User-name
    @Column
    public String alias; // Member-alias
    @Column
    public String portrait; // User-portrait
}
