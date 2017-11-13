package com.wzh.italker.frags.account;


import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import com.wzh.common.app.BaseFragment;
import com.wzh.italker.R;

import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登陆
 *
 * @author wang
 */
public class LoginFragment extends BaseFragment {

    private AccountTrigger mAccountTrigger;

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_password)
    EditText mPassword;

    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.btn_submit)
    Button mSubmit;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (AccountTrigger) context;

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }


    @OnClick(R.id.txt_go_register)
    void onShowRegisterClick() {
        mAccountTrigger.triggerView();
    }

}
