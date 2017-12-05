package com.wzh.factory.data;

import java.util.List;

/**
 * Created by HP on 2017/12/4.
 *
 * @author by wangWei
 */

public interface DbDataSource<Data> extends DataSource {


    /**
     * 有一个基本的数据源加载方法
     *
     * @param callback 传递一个callback回调，一般回调到Presenter
     */
    void load(SucceedCallback<List<Data>> callback);
}
