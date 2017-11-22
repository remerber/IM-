package com.wzh.factory.data.helper;


import android.text.TextUtils;

import com.wzh.factory.Factory;
import com.wzh.factory.R;
import com.wzh.factory.data.DataSource;
import com.wzh.factory.model.api.RspModel;
import com.wzh.factory.model.api.account.AccountRspModel;
import com.wzh.factory.model.api.account.LoginModel;
import com.wzh.factory.model.api.account.RegisterModel;
import com.wzh.factory.model.db.User;
import com.wzh.factory.net.NetWork;
import com.wzh.factory.net.RemoteService;
import com.wzh.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017/11/12.
 *
 * @author by wangWei
 */

public class AccountHelper {


    public static void register(final RegisterModel model, final DataSource.Callback<User> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        call.enqueue(new AccountRspCallback(callback));

    }


    /**
     * 登录的调用
     *
     * @param model    登录的Model
     * @param callback 成功与失败的接口回送
     */
    public static void login(final LoginModel model, final DataSource.Callback<User> callback) {
        RemoteService service = NetWork.remote();
        // 得到一个Call
        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        // 异步的请求
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 对设备Id进行绑定的操作
     *
     * @param callback Callback
     */
    public static void bindPush(final DataSource.Callback<User> callback) {
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId)) {
            return;
        }
        RemoteService service = NetWork.remote();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));

    }

    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {

        final DataSource.Callback<User> callback;

        private AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            RspModel<AccountRspModel> rspModel = response.body();
            if (rspModel.success()) {
                AccountRspModel accountRspModel = rspModel.getResult();
                User user = accountRspModel.getUser();
                user.save();
                Account.login(accountRspModel);
                //判断绑定状态，是否绑定设备
                if (accountRspModel.isBind()) {
                    Account.setBind(true);
                    if (callback != null) {
                        callback.onDataLoaded(user);
                    }
                } else {
                    bindPush(callback);
                }

            } else {
                // 错误解析
                Factory.decodeRspCode(rspModel, callback);
            }


        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            if (callback != null) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        }
    }

}
