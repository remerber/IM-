package com.wzh.italker.frags.message;


import com.wzh.factory.presenter.BaseContract;
import com.wzh.italker.R;

/** 群聊天界面
 *  @author  wang
 */
public class ChatGroupFragment extends ChatFragment {


    public ChatGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    protected BaseContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(Object o) {

    }
}
