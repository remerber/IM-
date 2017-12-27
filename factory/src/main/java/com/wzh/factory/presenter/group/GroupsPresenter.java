package com.wzh.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import com.wzh.common.widget.recycler.RecyclerAdapter;
import com.wzh.factory.data.DataSource;
import com.wzh.factory.data.group.GroupsDataSource;
import com.wzh.factory.data.group.GroupsRepository;
import com.wzh.factory.data.helper.GroupHelper;
import com.wzh.factory.model.db.Group;
import com.wzh.factory.presenter.BaseSourcePresenter;
import com.wzh.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * Created by HP on 2017/11/28.
 *
 * @author by wangWei
 */

public class GroupsPresenter extends BaseSourcePresenter<Group, Group, GroupsDataSource, GroupsContract.View>
        implements GroupsContract.Presenter, DataSource.SucceedCallback<List<Group>> {

    public GroupsPresenter(GroupsContract.View view) {

        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();
        GroupHelper.refreshGroups();
    }


    @Override
    public void onDataLoaded(List<Group> groups) {
        // 运行到这里的时候是子线程
        // 无论怎么操作，数据变更，最终都会通知到这里来
        final GroupsContract.View view = getView();
        if (view == null) {
            return;
        }

        RecyclerAdapter<Group> adapter = view.getRecyclerAdapter();
        List<Group> old = adapter.getItems();


        DiffUtil.Callback callback = new DiffUiDataCallback<>(old, groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);


        refreshData(result, groups);


    }
}
