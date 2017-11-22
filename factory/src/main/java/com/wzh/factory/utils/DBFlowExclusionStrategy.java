package com.wzh.factory.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

/**
 * Created by HP on 2017/11/22.
 *
 * @author by wangWei
 */

public class DBFlowExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        //被跳过的字段，只要是属于DBFlow数据的
        return f.getDeclaredClass().equals(ModelAdapter.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
