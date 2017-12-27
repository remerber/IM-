package com.wzh;

/**
 * Created by HP on 2017/11/12.
 *
 * @author by wangWei
 */

public class Common {
    public interface Constance {
        //手机号的正则，11位电话号码
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";

        // 基础的网络请求地址
       String API_URL = "http://192.168.0.149:8080/api/";
       // String API_URL = "http://192.168.0.103:8080/api/";
        // String API_URL = "http://192.168.155.4:8080/api/";
        long MAX_UPLOAD_IMAGE_LENGTH = 860 * 1024;

    }
}
