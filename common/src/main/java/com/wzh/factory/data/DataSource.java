package com.wzh.factory.data;

import android.support.annotation.StringRes;

/**
 * Created by HP on 2017/11/12.
 *
 * @author by wangWei
 */

public interface DataSource {


    /**
     * 只关注成功的接口
     *
     * @param <T> 任意类型
     */
    interface SucceedCallback<T> {
        // 数据加载成功, 网络请求成功
        void onDataLoaded(T t);

    }

    /**
     * 只关注失败的接口
     */
    interface FailedCallback {
        // 数据加载失败, 网络请求失败
        void onDataNotAvailable(@StringRes int strRes);
    }

    /**
     * 同时包括了成功和失败的回调接口
     *
     * @param <T>
     */
    interface Callback<T> extends SucceedCallback<T>, FailedCallback {

    }

    /**
     * 销毁操作
     */
    void dispose();
}
