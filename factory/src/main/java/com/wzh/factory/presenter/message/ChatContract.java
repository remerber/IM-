package com.wzh.factory.presenter.message;

import com.wzh.factory.model.db.Group;
import com.wzh.factory.model.db.Message;
import com.wzh.factory.model.db.User;
import com.wzh.factory.model.db.view.MemberUserModel;
import com.wzh.factory.presenter.BaseContract;

import java.util.List;

/**
 * Created by HP on 2017/12/7.
 * 聊天契约
 *
 * @author by wangWei
 */

public interface ChatContract {

    interface Presenter extends BaseContract.Presenter {


        void pushText(String content);


        void pushAudio(String path, long time);


        void pushImages(String[] paths);


        boolean rePush(Message message);

    }

    interface View<InitModel> extends BaseContract.RecyclerView<Presenter, Message> {

        void onInit(InitModel model);

    }

    /**
     * 人聊天界面
     */
    interface UserView extends View<User> {
    }

    /**
     * 群聊天界面
     */
    interface GroupView extends View<Group> {

        void showAdminOption(boolean isAdmin);

        void onInitGroupMembers(List<MemberUserModel> members, long moreCount);

    }
}
