package com.wzh.factory.net;

import android.text.TextUtils;

import com.wzh.Common;
import com.wzh.factory.Factory;
import com.wzh.factory.persistence.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HP on 2017/11/17.
 * 网络请求的封装
 *
 * @author by wangWei
 */

public class NetWork {

    private static NetWork instance;
    private Retrofit retrofit;

    static {
        instance = new NetWork();
    }

    private NetWork() {

    }

    //构建一个Retrofit
    public static Retrofit getRetrofit() {


        if (instance.retrofit != null) {
            return instance.retrofit;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        //拿到我们的请求
                        Request original = chain.request();
                        //重新进行rebuild
                        Request.Builder builder = original.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())) {
                            //注入一个token
                            builder.addHeader("token", Account.getToken());
                        }
                        builder.addHeader("Content-Type", "application/json");
                        Request request = builder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();
        instance.retrofit = builder.baseUrl(Common.Constance.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
        return instance.retrofit;

    }

    /**
     * 返回一个代理请求
     *
     * @return
     */
    public static RemoteService remote() {
        return NetWork.getRetrofit().create(RemoteService.class);
    }

}
