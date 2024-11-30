package com.idreameducation.ipreppal.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.idreameducation.ipreppal.pal.activity.NotificationActivity;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.PalSplashActivity;
import com.idreameducation.ipreppal.util.Util;

import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Created by sony on 4/6/2017.
 */

public class
FireMsgService extends FirebaseMessagingService {
    private Context context;
    private Global global;
    String tkn;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        global = (Global) getApplicationContext();


//        tkn = FirebaseInstanceId.getInstance().getToken();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isComplete()){
                    tkn = task.getResult();
                    global.setUniqueId(tkn);
                    try {
                        Util.setToken(getBaseContext(), tkn);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    String type ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        context = this;
        global = (Global) getApplicationContext();

        type = remoteMessage.getData().get("type");

        System.out.println("============ recivid 1 ");

        try {
            Map<String, String> notificationMap = remoteMessage.getData();
            String message;
            if (notificationMap.get("replyable") != null) {
                if (notificationMap.get("replyable").equalsIgnoreCase("AssignTopic")) {
                    message = notificationMap.get("sender") + " Assigned you topic " + notificationMap.get("message").split(":")[1];
                    try {
                        Util.setNotificationType(context , "AssignTopic");
                        populateNotification(message , notificationMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if(!TextUtils.isEmpty( notificationMap.get("message"))){
                        message = notificationMap.get("sender") + " : " + notificationMap.get("message");
                        try {
                            Util.setNotificationType(context , "Message");
                            populateNotification(message , notificationMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                message = remoteMessage.getNotification().getBody();
                notificationMap.put("message", message);
                notificationMap.put("notificationMessage", "Message from iDream");
                notificationMap.put("replyable", "Not Showable");
                notificationMap.put("sender", "iDrean");
                notificationMap.put("messageTime", Util.getCurrentDateWithDifferentFormat());
                notificationMap.put("senderId", "iDream");
                try {
                    Util.setNotificationType(context , "App");
                    populateNotification(message , notificationMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  void populateNotification(String message ,Map<String, String> notificationMap) throws Exception {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification builder;

        String CHANNEL_ID = "iDream_channel_01";
        CharSequence name = "iDream";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            Intent intent = new Intent(getApplicationContext(), PalSplashActivity.class).putExtra("goto",type);

            //PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_IMMUTABLE

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 22, intent, PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_IMMUTABLE);

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
            builder = new Notification.Builder(this)
                    .setContentTitle("iDream")
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.notification)
                    .setChannelId(CHANNEL_ID)
                    .build();
        } else {
            builder = new Notification.Builder(this)
                    .setContentTitle("iDream")
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.notification)
                    .build();
        }

        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.contentIntent = pendingIntent;
        notificationManager.notify(1, builder);
        Util.setNotificationCount(context, Util.getNotificationCount(context) + 1);

//        CategoriesActivity.activity.getNotificationCount();
    }
}