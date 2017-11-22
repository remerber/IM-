package com.wzh.factory.presenter.account;

import com.wzh.factory.presenter.BaseContract;

/**
 * Created by HP on 2017/11/21.
 *
 * @author by wangWei
 */

public interface LoginContract {

    interface View extends BaseContract.View<Presenter> {
        //登录成功
        void loginSuccess();
    }


    interface Presenter extends BaseContract.Presenter {
        //发起一个登录请求
        void login(String phone, String password);
    }


}
