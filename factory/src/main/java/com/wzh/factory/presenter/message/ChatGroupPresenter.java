package com.wzh.factory.presenter.message;

import com.wzh.factory.data.helper.GroupHelper;
import com.wzh.factory.data.message.MessageGroupRepository;
import com.wzh.factory.model.db.Group;
import com.wzh.factory.model.db.Message;
import com.wzh.factory.model.db.view.MemberUserModel;
import com.wzh.factory.persistence.Account;

import java.util.List;

/**
 * Created by HP on 2017/12/7.
 * 群聊天的逻辑
 *
 * @author by wangWei
 */

public class ChatGroupPresenter extends ChatPresenter<ChatContract.GroupView> implements ChatContract.Presenter {

    public ChatGroupPresenter(ChatContract.GroupView view, String receiverId) {
        super(new MessageGroupRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void start() {
        super.start();

        Group group = GroupHelper.findFromLocal(mReceiverId);
        if (group != null) {
            ChatContract.GroupView view = getView();
            boolean isAdmin = Account.getUserId().equalsIgnoreCase(group.getOwner().getId());
            view.showAdminOption(isAdmin);

            view.onInit(group);

            List<MemberUserModel> models = group.getLatelyGroupMembers();
            final long memberCount = group.getGroupMemberCount();
            long moreCount = memberCount - models.size();
            view.onInitGroupMembers(models, moreCount);
        }
    }
}
