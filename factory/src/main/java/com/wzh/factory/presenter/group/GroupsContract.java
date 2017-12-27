package com.wzh.factory.presenter.group;

import com.wzh.factory.model.db.Group;
import com.wzh.factory.presenter.BaseContract;

/**
 * Created by HP on 2017/11/28.
 *  我的群契约
 * @author by wangWei
 */

public interface GroupsContract {

    interface Presenter extends BaseContract.Presenter {

    }

    interface View extends BaseContract.RecyclerView<Presenter, Group> {

    }
}
