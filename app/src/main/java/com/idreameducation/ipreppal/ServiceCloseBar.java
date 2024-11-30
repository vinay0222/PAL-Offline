package com.idreameducation.ipreppal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class ServiceCloseBar extends Service {

    private static Timer timer;
    private static TimerTask task;
    private static final int TIME_INTERVAL=100;
    @Override
    public void onCreate() {
        super.onCreate();
//        try {
//            timer=new Timer();
//            task = new TimerTask() {
//                public void run() {
//                    try {
//                        @SuppressLint("WrongConstant") Object sbservice = getSystemService( "statusbar" );
//                        Class<?> statusbarManager = Class.forName( "android.app.StatusBarManager" );
//                        Method expandMethod;
//                        if (Build.VERSION.SDK_INT >= 17) {
//                            expandMethod = statusbarManager.getMethod("collapsePanels");
//                        } else {
//                            expandMethod = statusbarManager.getMethod("collapse");
//                        }
//                        expandMethod .invoke( sbservice );
//                    } catch (Exception e) {
//                    };
//                }
//            };
//            timer.scheduleAtFixedRate(task, 0, TIME_INTERVAL);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDestroy() {
        try {
            task.cancel();
            timer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}