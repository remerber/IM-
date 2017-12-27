package com.wzh.italker;

import android.content.Context;

import com.igexin.sdk.PushManager;
import com.wzh.common.app.BaseApplication;
import com.wzh.factory.Factory;
import com.wzh.factory.persistence.Account;
import com.wzh.italker.activities.AccountActivity;

/**
 * Created by HP on 2017/11/3.
 *
 * @author by wangWei
 */

public class MyApplication extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        // 调用Factory进行初始化
        Factory.setup();
        // 推送进行初始化
        PushManager.getInstance().initialize(this);
    }

    @Override
    protected void showAccountView(Context context) {
        super.showAccountView(context);
        //登陆界面
        AccountActivity.show(context);
        //清理缓存
        Account.clear(context);
    }
}
