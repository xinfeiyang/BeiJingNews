package com.security.news.util;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.mob.MobSDK;
import com.zhy.http.okhttp.OkHttpUtils;

import org.litepal.LitePal;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 全局的Application;
 */
public class MyApplication extends Application {

    private static Context context;
    private static long mainTreadId;
    private static Handler handler;

    /**
     * 程序的入口,初始化一些常用的参数;
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //全局的上下文
        context = getApplicationContext();

        // 主线程Id
        mainTreadId = android.os.Process.myTid();

        // 定义一个handler
        handler = new Handler();

        //初始化LitePal;
        LitePal.initialize(this);

        //OKHttpUtils的相关配置;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);

        //初始化ShareSDK;
        MobSDK.init(context, "10b31081986d5", "bccfb6db9759a3305874f8e8e88fb9f9");
    }

    public static Handler getHandler() {
        return handler;
    }

    public static Context getContext() {
        return context;
    }

    public static long getMainTreadId() {
        return mainTreadId;
    }

}

