package com.wzh.common.app;

import android.app.Application;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

/**
 * Created by HP on 2017/11/3.
 *
 * @author by wangWei
 */

public class BaseApplication extends Application {

    private static BaseApplication mInstance = new BaseApplication();


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    /**
     * 外部获取单例
     *
     * @return Application
     */
    public static BaseApplication getInstance() {
        return mInstance;
    }


    /**
     * 获取缓存文件夹的地址
     *
     * @return
     */
    public static File getCacheDirFile() {
        return mInstance.getCacheDir();
    }

    public static File getPortraitTmpFile() {
        //得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "portrait");
        //创建所有对应的文件夹
        dir.mkdirs();

        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }
        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        return path.getAbsoluteFile();

    }


    public static  void  showToast(final String msg){
        //Toast只能在主线程中显示，所以需要线程切换
        //保证一定在主线程中进行show的操作
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 这里进行回调的时候一定就是主线程状态了
                Toast.makeText(mInstance, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示一个Toast
     *
     * @param msgId 传递的是字符串的资源
     */
    public static void showToast(@StringRes int msgId) {
        showToast(mInstance.getString(msgId));
    }


}
