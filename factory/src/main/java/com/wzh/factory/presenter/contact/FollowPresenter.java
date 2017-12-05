package com.wzh.factory.presenter.contact;

import android.support.annotation.StringRes;

import com.wzh.factory.data.DataSource;
import com.wzh.factory.data.helper.UserHelper;
import com.wzh.factory.model.card.UserCard;
import com.wzh.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by HP on 2017/11/27.
 *
 * @author by wangWei
 */

public class FollowPresenter extends BasePresenter<FollowContract.View>
        implements FollowContract.Presenter, DataSource.Callback<UserCard> {

    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(String id) {
        start();
        UserHelper.follow(id, this);

    }

    @Override
    public void onDataLoaded(final UserCard userCard) {

        final FollowContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSucceed(userCard);
                }
            });
        }

    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final FollowContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
