package com.wzh.italker.frags.account;


import android.content.Context;
import android.widget.EditText;

import com.wzh.common.app.PresenterFragment;
import com.wzh.factory.presenter.account.RegisterContract;
import com.wzh.factory.presenter.account.RegisterPresenter;
import com.wzh.italker.R;
import com.wzh.italker.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 注册
 *
 * @author wang
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter>
        implements RegisterContract.View {

    private AccountTrigger mAccountTrigger;

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_name)
    EditText mName;
    @BindView(R.id.edit_password)
    EditText mPassword;

    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.btn_submit)
    Button mSubmit;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }


    @OnClick(R.id.txt_go_login)
    void onShowLoginClick() {
        mAccountTrigger.triggerView();
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String phone = mPhone.getText().toString();
        String name = mName.getText().toString();
        String password = mPassword.getText().toString();
        mPresenter.register(phone, name, password);

    }


    @Override
    public void showError(int str) {
        super.showError(str);
        // 当需要显示错误的时候触发，一定是结束了

        // 停止Loading
        mLoading.stop();
        // 让控件可以输入
        mPhone.setEnabled(true);
        mName.setEnabled(true);
        mPassword.setEnabled(true);
        // 提交按钮可以继续点击
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();

        // 正在进行时，正在进行注册，界面不可操作
        // 开始Loading
        mLoading.start();
        // 让控件不可以输入
        mPhone.setEnabled(false);
        mName.setEnabled(false);
        mPassword.setEnabled(false);
        // 提交按钮不可以继续点击
        mSubmit.setEnabled(false);

    }

    @Override
    public void registerSuccess() {

        MainActivity.show(getContext());
        getActivity().finish();
    }

}
