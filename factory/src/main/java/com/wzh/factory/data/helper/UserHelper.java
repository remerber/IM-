package com.wzh.factory.data.helper;

import com.wzh.factory.Factory;
import com.wzh.factory.R;
import com.wzh.factory.data.DataSource;
import com.wzh.factory.model.api.RspModel;
import com.wzh.factory.model.api.user.UserUpdateModel;
import com.wzh.factory.model.card.UserCard;
import com.wzh.factory.model.db.User;
import com.wzh.factory.net.NetWork;
import com.wzh.factory.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017/11/22.
 *
 * @author by wangWei
 */

public class UserHelper {

    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {

                    UserCard userCard = rspModel.getResult();
                    User user = userCard.build();
                    user.save();
                    callback.onDataLoaded(userCard);


                } else {

                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });


    }

}