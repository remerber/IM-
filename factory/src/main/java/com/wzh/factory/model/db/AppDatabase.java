package com.wzh.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by HP on 2017/11/21.
 *
 * @author by wangWei
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "AppDatabase";
    public static final int VERSION = 1;
}
