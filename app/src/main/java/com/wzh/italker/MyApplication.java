package com.wzh.italker;

import com.igexin.sdk.PushManager;
import com.wzh.common.app.BaseApplication;
import com.wzh.factory.Factory;

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
}
