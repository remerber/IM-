package com.wzh.factory.data.group;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wzh.factory.data.BaseDbRepository;
import com.wzh.factory.data.helper.GroupHelper;
import com.wzh.factory.model.db.Group;
import com.wzh.factory.model.db.Group_Table;
import com.wzh.factory.model.db.view.MemberUserModel;

import java.util.List;

/**
 * Created by HP on 2017/12/4.
 *
 * @author by wangWei
 */

public class GroupsRepository extends BaseDbRepository<Group>
        implements GroupsDataSource {


    @Override
    public void load(SucceedCallback<List<Group>> callback) {
        super.load(callback);
        // 加载本地数据库数据
        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();

    }

    @Override
    protected boolean isRequired(Group group) {
        //你加入群或者你创建群，只有群的信息，没有成员的信息，
        //需要对成员信息进行初始化操作
        if (group.getGroupMemberCount() > 0) {
            group.holder = buildGroupHolder(group);
        }else{
            group.holder=null;
            GroupHelper.refreshGroupMember(group);
        }

        return true;
    }

    /**
     * 初始化界面显示的成员信息
     *
     * @param group
     * @return
     */
    private String buildGroupHolder(Group group) {
        List<MemberUserModel> userModels = group.getLatelyGroupMembers();
        if (userModels == null || userModels.size() == 0) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (MemberUserModel userModel : userModels) {
            builder.append(TextUtils.isEmpty(userModel.alias) ? userModel.name : userModel.alias);
            builder.append(", ");
        }

        builder.delete(builder.lastIndexOf(", "), builder.length());

        return builder.toString();
    }
}
