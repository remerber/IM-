package com.wzh.factory.presenter.group;

import com.wzh.factory.model.Author;
import com.wzh.factory.presenter.BaseContract;

/**
 * Created by HP on 2017/12/13.
 * 群创建的契约
 *
 * @author by wangWei
 */

public interface GroupCreateContract {

    interface Presenter extends BaseContract.Presenter {

        void create(String name, String desc, String picture);

        void changeSelect(ViewModel model, boolean isSelected);

    }


    interface View extends BaseContract.RecyclerView<Presenter, ViewModel> {
        void onCreateSucceed();
    }


    class ViewModel {
        public Author author;
        public boolean isSelected;
    }
}
