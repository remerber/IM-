package com.wzh.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import com.wzh.factory.data.message.SessionDataSource;
import com.wzh.factory.data.message.SessionRepository;
import com.wzh.factory.model.db.Session;
import com.wzh.factory.presenter.BaseSourcePresenter;
import com.wzh.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * Created by HP on 2017/12/8.
 *
 * @author by wangWei
 */

public class SessionPresenter extends BaseSourcePresenter<Session, Session,
        SessionDataSource, SessionContract.View> implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {

        SessionContract.View view = getView();
        if (view == null) {
            return;
        }
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, sessions);


    }
}
