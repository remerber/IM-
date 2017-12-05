package com.wzh.factory.presenter.contact;

import android.support.v7.util.DiffUtil;

import com.wzh.common.widget.recycler.RecyclerAdapter;
import com.wzh.factory.data.DataSource;
import com.wzh.factory.data.helper.UserHelper;
import com.wzh.factory.data.user.ContactDataSource;
import com.wzh.factory.data.user.ContactRepository;
import com.wzh.factory.model.db.User;
import com.wzh.factory.presenter.BaseSourcePresenter;
import com.wzh.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * Created by HP on 2017/11/28.
 *
 * @author by wangWei
 */

public class ContactPresenter extends BaseSourcePresenter<User, User, ContactDataSource, ContactContract.View>
        implements ContactContract.Presenter, DataSource.SucceedCallback<List<User>> {

    public ContactPresenter(ContactContract.View view) {
        //初始化数据仓库
        super(new ContactRepository(), view);
    }

    @Override
    public void start() {
        super.start();
        //加载网络数据
        UserHelper.refreshContacts();
    }


    @Override
    public void onDataLoaded(List<User> users) {
        // 运行到这里的时候是子线程
        // 无论怎么操作，数据变更，最终都会通知到这里来
        final ContactContract.View view = getView();
        if (view == null) {
            return;
        }

        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> old = adapter.getItems();


        DiffUtil.Callback callback = new DiffUiDataCallback<>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);


        refreshData(result, users);


    }
}
