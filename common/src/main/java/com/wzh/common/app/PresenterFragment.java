package com.wzh.common.app;

import android.content.Context;
import android.support.annotation.StringRes;

import com.wzh.factory.presenter.BaseContract;

/**
 * Created by HP on 2017/11/12.
 *
 * @author by wangWei
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends BaseFragment
        implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initPresenter();
    }

    /**
     * 初始化Presenter
     *
     * @return
     */
    protected abstract Presenter initPresenter();

    @Override
    public void showError(@StringRes int str) {
        //显示错误，优先使用占位布局
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(str);
        } else {
            BaseApplication.showToast(str);
        }

    }

    @Override
    public void showLoading() {
        //ToDo 显示一个Loading
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.mPresenter = presenter;
    }
}
