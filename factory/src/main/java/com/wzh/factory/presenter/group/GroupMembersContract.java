package com.wzh.factory.presenter.group;

import com.wzh.factory.model.db.view.MemberUserModel;
import com.wzh.factory.presenter.BaseContract;

/**
 * Created by HP on 2017/12/20.
 * 群成员的契约
 *
 * @author by wangWei
 */

public interface GroupMembersContract {


    interface Presenter extends BaseContract.Presenter {
        // 具有一个刷新的方法
        void refresh();
    }

    // 界面
    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel> {
        // 获取群的ID
        String getGroupId();
    }
}
