package com.wzh.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by HP on 2017/11/28.
 *
 * @author by wangWei
 */

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>>  extends DiffUtil.Callback {

    private List<T> mOldList,mNewList;

    public DiffUiDataCallback(List<T> mOldList, List<T> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();//旧的数据大小
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();//新的数据大小
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld=mOldList.get(oldItemPosition);
        T beanNew=mNewList.get(newItemPosition);
        return beanNew.isSame(beanOld);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld=mOldList.get(oldItemPosition);
        T beanNew=mNewList.get(newItemPosition);
        return beanNew.isUiContentSame(beanOld);
    }



    public  interface  UiDataDiffer<T>{
        boolean isSame(T old);

        boolean isUiContentSame(T old);
    }
}
