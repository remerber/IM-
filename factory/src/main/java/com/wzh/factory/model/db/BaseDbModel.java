package com.wzh.factory.model.db;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.wzh.factory.utils.DiffUiDataCallback;

/**
 * Created by HP on 2017/12/4.
 * 我们APP中的基础的一个BaseDbModel，
 * 基础了数据库框架DbFlow中的基础类
 * 同时定义类我们需要的方法
 *
 * @author by wangWei
 */

public abstract class BaseDbModel<Model> extends BaseModel
        implements DiffUiDataCallback.UiDataDiffer<Model> {
}
