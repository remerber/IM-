package com.wzh.factory.presenter.message;

import com.wzh.factory.data.helper.UserHelper;
import com.wzh.factory.data.message.MessageRepository;
import com.wzh.factory.model.db.Message;
import com.wzh.factory.model.db.User;

/**
 * Created by HP on 2017/12/7.
 *
 * @author by wangWei
 */

public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView> implements ChatContract.Presenter {

    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();
        //从本地拿到这个人的消息
        User receiver = UserHelper.findFromLocal(mReceiverId);
        getView().onInit(receiver);
    }
}
