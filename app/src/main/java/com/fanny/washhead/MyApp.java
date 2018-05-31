package com.fanny.washhead;

//import android.app.Application;

import android.app.Application;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.avos.avoscloud.AVOSCloud;

import cn.jpush.sms.SMSSDK;


/**
 * Created by Fanny on 17/12/21.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        MobSDK.init(this);

        FeedbackAPI.init(this,"24754243","0e713b5280aa19b69ce1f90f8731d79c");
        FeedbackAPI.setHistoryTextSize(15);
//        FeedbackAPI.setBackIcon(R.drawable.icon_return);
        SMSSDK.getInstance().initSdk(this);

        /**
         * 设置前后两次获取验证码的时间间隔为45秒
         */
        SMSSDK.getInstance().setIntervalTime(45*1000);
        SMSSDK.getInstance().setDebugMode(true);

        /**
         * 初始化leancloud
         * 初始化参数依次为 this, AppId, AppKey
         */
        AVOSCloud.initialize(this,"Q99SLmp9WcOiFpXSvDB25nzN-gzGzoHsz","jyVQpf7meqOU3KhUd7dV8M69");
        /**
         * 发布应用关闭
         */
        AVOSCloud.setDebugLogEnabled(true);
    }
}
