package com.idreameducation.ipreppal.deviceIdService;


import android.app.Application;
import android.content.Context;


/**
 * @author 大定府羡民（1032694760@qq.com）
 * @since 2020/5/20
 */
public class DemoApp extends Application {
    private final boolean privacyPolicyAgreed = true;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注意APP合规性，若最终用户未同意隐私政策则不要调用
        if (privacyPolicyAgreed) {
            DeviceIdentifier.register(this);
        }
    }

}
