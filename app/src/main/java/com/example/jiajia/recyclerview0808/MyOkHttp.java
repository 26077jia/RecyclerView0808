package com.example.jiajia.recyclerview0808;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jiajia on 2016/8/8.
 * OkHttp 同步，先阶段OkHttp不支持异步api来接收响应体
 */
public class MyOkHttp {
    public static OkHttpClient client;

    public static String getData(String url) {
        try {
            client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().toString();
            }else{
                throw new IOException("ExceptionCode"+response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
