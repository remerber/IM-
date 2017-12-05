package com.wzh.factory.presenter.contact;

import com.wzh.factory.model.db.User;
import com.wzh.factory.presenter.BaseContract;

/**
 * Created by HP on 2017/11/28.
 *
 * @author by wangWei
 */

public interface ContactContract {

    // 什么都不需要额外定义，开始就是调用start即可
    interface Presenter extends BaseContract.Presenter {

    }

    // 都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter, User> {

    }
}
