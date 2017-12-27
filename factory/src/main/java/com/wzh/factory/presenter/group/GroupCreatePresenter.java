package com.wzh.factory.presenter.group;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.wzh.factory.Factory;
import com.wzh.factory.R;
import com.wzh.factory.data.DataSource;
import com.wzh.factory.data.helper.GroupHelper;
import com.wzh.factory.data.helper.UserHelper;
import com.wzh.factory.model.api.group.GroupCreateModel;
import com.wzh.factory.model.card.GroupCard;
import com.wzh.factory.model.db.view.UserSampleModel;
import com.wzh.factory.net.UploadHelper;
import com.wzh.factory.presenter.BaseRecyclerPresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by HP on 2017/12/13.
 *
 * @author by wangWei
 */

public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel, GroupCreateContract.View>
        implements GroupCreateContract.Presenter, DataSource.Callback<GroupCard> {

    private Set<String> users = new HashSet<>();


    public GroupCreatePresenter(GroupCreateContract.View view) {
        super(view);
    }


    @Override
    public void start() {
        super.start();
        Factory.runOnAsync(loader);

    }

    @Override
    public void create(final String name, final String desc, final String picture) {
        GroupCreateContract.View view = getView();
        if (view == null) {
            return;
        }
        view.showLoading();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) ||
                TextUtils.isEmpty(picture) || users.size() == 0) {
            view.showError(R.string.label_group_create_invalid);
            return;
        }
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = uploadPicture(picture);
                if (TextUtils.isEmpty(url)) {
                    return;
                }
                GroupCreateModel model = new GroupCreateModel(name, desc, picture, users);
                GroupHelper.create(model, GroupCreatePresenter.this);
            }
        });

    }

    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {

        if (isSelected) {
            users.add(model.author.getId());
        } else {
            users.remove(model.author.getId());
        }

    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            List<UserSampleModel> sampleModels = UserHelper.getSampleContact();
            List<GroupCreateContract.ViewModel> models = new ArrayList<>();
            for (UserSampleModel sampleModel : sampleModels) {
                GroupCreateContract.ViewModel viewModel = new GroupCreateContract.ViewModel();
                viewModel.author = sampleModel;
                models.add(viewModel);
            }

            refreshData(models);
        }
    };

    private String uploadPicture(String path) {
        String url = UploadHelper.uploadPortrait(path);
        if (TextUtils.isEmpty(url)) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    GroupCreateContract.View view = getView();
                    if (view != null) {
                        view.showError(R.string.data_upload_error);
                    }
                }
            });
        }
        return url;
    }

    @Override
    public void onDataLoaded(GroupCard groupCard) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.onCreateSucceed();
                }
            }
        });
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.showError(strRes);
                }
            }
        });
    }
}
