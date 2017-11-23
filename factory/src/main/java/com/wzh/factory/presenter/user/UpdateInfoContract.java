package com.wzh.factory.presenter.user;

import com.wzh.factory.presenter.BaseContract;

/**
 * Created by HP on 2017/11/22.
 *
 * @author by wangWei
 */

public interface UpdateInfoContract {

    interface Presenter extends BaseContract.Presenter {
        //更新
        void update(String photoFilePath, String desc, boolean isMan);
    }

    interface View extends BaseContract.View<Presenter> {

        void updateSucceed();
    }

}
