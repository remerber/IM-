package com.wzh.factory;

import com.wzh.common.app.BaseApplication;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author wang
 * @version 1.0.0
 */
public class Factory {
    // 单例模式ø
    private static final Factory instance;
    private final Executor executor;

    static {
        instance = new Factory();
    }

    private Factory() {
        // 新建一个4个线程的线程池
        executor = Executors.newFixedThreadPool(4);
    }

    /**
     * 返回全局的Application
     *
     * @return Application
     */
    public static BaseApplication app() {
        return BaseApplication.getInstance();
    }


    /**
     * 异步运行的方法
     *
     * @param runnable Runnable
     */
    public static void runOnAsync(Runnable runnable) {
        // 拿到单例，拿到线程池，然后异步执行
        instance.executor.execute(runnable);
    }
}
