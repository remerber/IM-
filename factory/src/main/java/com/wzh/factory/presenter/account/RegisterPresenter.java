package com.wzh.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.wzh.Common;
import com.wzh.factory.R;
import com.wzh.factory.data.DataSource;
import com.wzh.factory.data.helper.AccountHelper;
import com.wzh.factory.model.api.account.RegisterModel;
import com.wzh.factory.model.card.UserCard;
import com.wzh.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

/**
 * Created by HP on 2017/11/12.
 *
 * @author by wangWei
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter,
        DataSource.Callback<UserCard> {


    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }


    @Override
    public void register(String phone, String name, String password) {
        //调用开始方法，在start中默认启动了Loading
        start();

        //得到View接口
        RegisterContract.View view = getView();

        //校验
        if (!checkMobile(phone)) {
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            //开始网络请求
            RegisterModel model = new RegisterModel(phone, password, name);
            //进行网络请求，并为自己设置接口回调
            AccountHelper.register(model, this);
        }

    }

    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone) && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }

    @Override
    public void onDataLoaded(UserCard userCard) {

    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
         //网络请求告知注册失败
        final RegisterContract.View view = getView();
        if (view == null) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
              //调用主界面显示注册失败
                view.showError(strRes);
            }

        });
    }
}
