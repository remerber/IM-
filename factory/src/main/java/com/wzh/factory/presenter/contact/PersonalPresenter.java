package com.wzh.factory.presenter.contact;

import com.wzh.factory.Factory;
import com.wzh.factory.data.helper.UserHelper;
import com.wzh.factory.model.db.User;
import com.wzh.factory.persistence.Account;
import com.wzh.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by HP on 2017/11/28.
 *
 * @author by wangWei
 */

public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter

{
    private User user;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }


    @Override
    public void start() {
        super.start();
        //个人界面用户数据优先从网络上拉取
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getView();
                if (view != null) {
                    String id = view.getUserId();
                    User user = UserHelper.findFromNet(id);
                    onLoad(user);

                }
            }
        });

    }

    private void onLoad(final User user) {
        this.user = user;
        //是否就是我自己
        boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        //是否已经被关注
        final boolean isFollow = isSelf || user.isFollow();
        //已经关注同时不是自己才能聊天
        final boolean allowSayHello = isFollow && !isSelf;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                PersonalContract.View view = getView();
                if (view == null) {
                    return;
                }
                view.onLoadDone(user);
                view.allowSayHello(allowSayHello);
                view.setFollowStatus(isFollow);

            }
        });

    }

    @Override
    public User getUserPersonal() {
        return user;
    }
}
