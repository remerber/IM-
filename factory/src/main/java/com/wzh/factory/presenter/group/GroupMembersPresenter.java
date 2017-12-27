package com.wzh.factory.presenter.group;

import com.wzh.factory.Factory;
import com.wzh.factory.data.helper.GroupHelper;
import com.wzh.factory.model.db.view.MemberUserModel;
import com.wzh.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

/**
 * Created by HP on 2017/12/20.
 *
 * @author by wangWei
 */

public class GroupMembersPresenter extends BaseRecyclerPresenter<MemberUserModel,GroupMembersContract.View>
  implements  GroupMembersContract.Presenter
{
    public GroupMembersPresenter(GroupMembersContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        start();
        Factory.runOnAsync(loader);

    }
    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMembersContract.View view = getView();
            if (view == null) {
                return;
            }
//可以通过构造函数，也可以通过这种方法传递
            String groupId = view.getGroupId();
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, -1);
            refreshData(models);
        }
    };
}
