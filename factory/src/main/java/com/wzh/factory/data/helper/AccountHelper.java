package com.wzh.factory.data.helper;

import com.wzh.factory.R;
import com.wzh.factory.data.DataSource;
import com.wzh.factory.model.api.account.RegisterModel;
import com.wzh.factory.model.card.UserCard;

/**
 * Created by HP on 2017/11/12.
 *
 * @author by wangWei
 */

public class AccountHelper {


    public static void register(final RegisterModel model, final DataSource.Callback<UserCard> callback) {
        callback.onDataNotAvailable(R.string.data_account_register_invalid_parameter_password);
    }
}
