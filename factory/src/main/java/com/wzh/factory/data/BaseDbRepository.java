package com.wzh.factory.data;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.wzh.factory.data.helper.DbHelper;
import com.wzh.factory.model.db.BaseDbModel;
import com.wzh.utils.CollectionUtil;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by HP on 2017/12/4.
 * 基础的数据库仓库，实现对数据库操作的监听
 *
 * @author by wangWei
 */

public abstract class BaseDbRepository<Data extends BaseDbModel<Data>> implements DbDataSource<Data>,
        DbHelper.ChangedListener<Data>, QueryTransaction.QueryResultListCallback<Data> {

    private Class<Data> dataClass;//当前泛型对应的真实的class信息
    private SucceedCallback<List<Data>> callback;
    private final List<Data> dataList = new LinkedList<>(); // 当前缓存的数据

    public BaseDbRepository() {
        // 拿当前类的范型数组信息
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();

        dataClass = (Class<Data>) (parameterizedType.getActualTypeArguments()[0]);
    }

    @Override
    public void load(SucceedCallback<List<Data>> callback) {
        this.callback = callback;
        registerDbChangedListener();
    }

    @Override
    public void dispose() {
        this.callback = null;
        DbHelper.removeChangedListener(dataClass, this);
        dataList.clear();
    }


    // 数据库统一通知的地方：增加／更改
    @Override
    public void onDataSave(Data... list) {
        boolean isChanged = false;
        for (Data data : list) {
            if (isRequired(data)) {
                insertOrUpdate(data);
                isChanged = true;
            }
        }
        if (isChanged) {
            notifyDataChange();
        }

    }

    // 数据库统一通知的地方：删除
    @Override
    public void onDataDelete(Data... list) {

        boolean isChanged = false;
        for (Data data : list) {
            if (dataList.remove(data)) {
                isChanged = true;
            }
        }
        if (isChanged) {
            notifyDataChange();
        }
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {
        //数据库加载数据成功
        if (tResult.size() == 0) {
            dataList.clear();
            notifyDataChange();
            return;
        }

        Data[] users = CollectionUtil.toArray(tResult, dataClass);
        onDataSave(users);

    }

    /**
     * 插入或者更新
     *
     * @param data
     */
    private void insertOrUpdate(Data data) {
        int index = indexOf(data);
        if (index > 0) {
            replace(index, data);
        } else {
            insert(data);
        }
    }

    // 更新操作，更新某个坐标下的数据
    protected void replace(int index, Data data) {
        dataList.remove(index);
        dataList.add(index, data);
    }

    // 添加方法
    protected void insert(Data data) {
        dataList.add(data);
    }

    /**
     * 查询一个数据是否在当前的缓存数据中，如果在返回坐标
     *
     * @param newData
     * @return
     */
    protected int indexOf(Data newData) {
        int index = -1;
        for (Data data : dataList) {
            index++;
            if (data.isSame(newData)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * 检查一个User是否是我需要关注的数据
     *
     * @param data Data
     * @return True是我关注的数据
     */
    protected abstract boolean isRequired(Data data);

    /**
     * 添加数据库的监听操作
     */
    private void registerDbChangedListener() {
        DbHelper.addChangedListener(dataClass, this);
    }

    /**
     * 通知界面刷新的方法
     */
    private void notifyDataChange() {
        SucceedCallback<List<Data>> callback = this.callback;
        if (callback != null) {
            callback.onDataLoaded(dataList);
        }

    }

}
