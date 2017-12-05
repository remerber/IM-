package com.wzh.factory.presenter.contact;

import com.wzh.factory.model.card.UserCard;
import com.wzh.factory.presenter.BaseContract;

/**
 * Created by HP on 2017/11/27.
 *
 * @author by wangWei
 */

public interface FollowContract {

    interface Presenter extends BaseContract.Presenter {
        //关注一个人
        void follow(String id);
    }


    interface View extends BaseContract.View<Presenter> {

        void onFollowSucceed(UserCard userCard);

    }

}
