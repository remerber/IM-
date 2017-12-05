package com.wzh.factory.presenter.contact;

import com.wzh.factory.model.db.User;
import com.wzh.factory.presenter.BaseContract;

/**
 * Created by HP on 2017/11/28.
 *
 * @author by wangWei
 */

public interface PersonalContract {

    interface Presenter extends BaseContract.Presenter {
        User getUserPersonal();//获取用户信息
    }

    interface View extends BaseContract.View<Presenter> {

        String getUserId();

        void onLoadDone(User user);

        //是否发起聊天
        void allowSayHello(boolean isAllow);

        //设置关注状态
        void setFollowStatus(boolean isFollow);


    }

}
