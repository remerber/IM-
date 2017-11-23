package com.wzh.factory.presenter.user;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.wzh.factory.Factory;
import com.wzh.factory.R;
import com.wzh.factory.data.DataSource;
import com.wzh.factory.data.helper.UserHelper;
import com.wzh.factory.model.api.user.UserUpdateModel;
import com.wzh.factory.model.card.UserCard;
import com.wzh.factory.model.db.User;
import com.wzh.factory.net.UploadHelper;
import com.wzh.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by HP on 2017/11/22.
 *
 * @author by wangWei
 */

public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View>
        implements UpdateInfoContract.Presenter, DataSource.Callback<UserCard> {


    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
    }


    @Override
    public void update(final String photoFilePath, final String desc, final boolean isMan) {

        final UpdateInfoContract.View view = getView();
        if (TextUtils.isEmpty(photoFilePath) || TextUtils.isEmpty(desc)) {
            view.showError(R.string.data_account_update_invalid_parameter);
        } else {
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    String url = UploadHelper.uploadPortrait(photoFilePath);
                    if (TextUtils.isEmpty(url)) {
                        // 上传失败
                        view.showError(R.string.data_upload_error);
                    } else {
                        UserUpdateModel model = new UserUpdateModel("", url, desc, isMan ? User.SEX_MAN : User.SEX_WOMAN);
                        //进行网路请求，开始上传
                        UserHelper.update(model, UpdateInfoPresenter.this);
                    }
                }
            });
        }

    }

    @Override
    public void onDataLoaded(UserCard userCard) {
        final UpdateInfoContract.View view = getView();
        if (view == null) {
            return;
        }
        // 强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
              view.updateSucceed();
            }
        });

    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final UpdateInfoContract.View view = getView();
        if (view == null) {
            return;
        }
        // 强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
