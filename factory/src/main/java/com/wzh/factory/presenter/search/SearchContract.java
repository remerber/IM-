package com.wzh.factory.presenter.search;

import com.wzh.factory.model.card.GroupCard;
import com.wzh.factory.model.card.UserCard;
import com.wzh.factory.presenter.BaseContract;

import java.util.List;

/**
 * Created by HP on 2017/11/24.
 *
 * @author by wangWei
 */

public interface SearchContract {

    //搜索人的界面
    interface UserView extends BaseContract.View<Presenter> {
        void onSearchDone(List<UserCard> userCards);
    }

    //搜索群的界面
    interface GroupView extends BaseContract.View<Presenter> {
        void onSearchDone(List<GroupCard> groupCards);
    }

    interface Presenter extends BaseContract.Presenter {
        //搜索内容
        void search(String content);
    }
}
