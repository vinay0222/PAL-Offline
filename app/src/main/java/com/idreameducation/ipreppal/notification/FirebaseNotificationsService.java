package com.idreameducation.ipreppal.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.activity.PalSplashActivity;
import com.idreameducation.ipreppal.util.Util;


public class FirebaseNotificationsService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "channel_id";
    public static final String TYPE = "type";
    private static final String NORMAL_TYPE = "normal";
    private static final String URL_TYPE = "url";
    private static final String SUPPORT_TYPE = "Support";
    private static final String URL = "link";

    Intent intent;

    String type = NORMAL_TYPE;
    //    FirebaseAuth mAuth;
    private static final String TAG = "NOTIFICATION_SERVICE";

    public FirebaseNotificationsService() {
        System.out.println("============ recivid 3 ");
        Log.d(TAG, "Constructor called");
//        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        System.out.println("============ recivid 3 ");

        type = remoteMessage.getData().get(TYPE);
        String type2 = remoteMessage.getData().get(TYPE);
        String userid = remoteMessage.getData().get("studentId");



        System.out.println("===== type "+type);
        System.out.println("===== userid "+userid);

        if(Util.getUserId(getApplicationContext()).equals(userid)) {

            if (type.equals(URL_TYPE)) {
                String url = remoteMessage.getData().get(URL);
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(intent);

                Log.d(TAG, "type:link " + type + ":" + url);
            } else if (type.equals(NORMAL_TYPE)) {
                intent = new Intent(getApplicationContext(), PalSplashActivity.class).putExtra("goto",type2);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (type.equals(SUPPORT_TYPE)) {
                intent = new Intent(getApplicationContext(), PalSplashActivity.class).putExtra("goto",type2);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            intent.putExtra(TYPE, remoteMessage.getData().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(remoteMessage.getNotification().getTitle()+"  rseweww")
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSmallIcon(R.drawable.ic_iprep_logo)
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                    .setAutoCancel(false);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }
    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s); Log.d(TAG, "Token received");
    }

}