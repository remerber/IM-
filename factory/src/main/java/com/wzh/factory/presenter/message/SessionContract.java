package com.wzh.factory.presenter.message;

import com.wzh.factory.model.db.Session;
import com.wzh.factory.presenter.BaseContract;

/**
 * Created by HP on 2017/12/8.
 *
 * @author by wangWei
 */

public interface SessionContract {

    // 什么都不需要额外定义，开始就是调用start即可
    interface Presenter extends BaseContract.Presenter {

    }

    // 都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter, Session> {

    }
}
