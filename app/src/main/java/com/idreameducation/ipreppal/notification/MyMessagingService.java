package com.idreameducation.ipreppal.notification;


import static android.app.PendingIntent.FLAG_MUTABLE;
import static com.idreameducation.ipreppal.R.drawable.iprep_logo;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.idreameducation.ipreppal.pal.activity.PalSplashActivity;
import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;
import com.idreameducation.ipreppal.util.Util;

import java.util.HashMap;

public class MyMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        System.out.println("---- new Token "+s);
    }

    /** service run only when app is running  */

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        System.out.println("============ recivid recivedd  ");

        String type = remoteMessage.getData().get("type");
        String userid;
        userid = remoteMessage.getData().get("studentId");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String message = remoteMessage.getData().get("message");
        String fileLink = remoteMessage.getData().get("fileLink");

        System.out.println("============ recivid 2 "+type);
        System.out.println("============ userid "+userid);

        if(type==null || Util.getUserId(getApplicationContext())==null) return;

        if(userid==null) userid="userID";

        if(type.equals("chat_notification") || type.equals("assigned_notification")) {
            Intent intent = new Intent(getApplicationContext(), PalSplashActivity.class).putExtra("goto",type);

            //PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_IMMUTABLE

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 22, intent, PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_IMMUTABLE);
            String CHANNEL_ID = "channel_name";// The id of the channel.


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(iprep_logo)
                    .setContentTitle(title+" ffff")
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);


            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "all";// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            notificationManager.notify(22, notificationBuilder.build()); // 0 is the request code, it should be unique id

            HashMap<String,String> data =new HashMap<>();
            data.put("message",title);
            data.put("messageTime", String.valueOf(System.currentTimeMillis()));
            data.put("notificationMessage",body);
            data.put("sender",title);
            data.put("studentId",Util.getUserId(getApplicationContext()));
            data.put("type",type);

            /** This push -> You have a new message from XYZ**/
            FirebaseDatabase.getInstance().getReference().child("notifications").child("student").child(Util.getUserId(getApplicationContext()))
                    .push().setValue(data);

        }
        else if(Util.getUserId(getApplicationContext()).equals(userid)) {

            Intent intent = new Intent(getApplicationContext(), PalSplashActivity.class);

            @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 22, intent, FLAG_MUTABLE);
            String CHANNEL_ID = "channel_name";// The id of the channel.
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(iprep_logo)
                    .setContentTitle(remoteMessage.getNotification().getTitle()+" ffff")
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "all";// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            notificationManager.notify(22, notificationBuilder.build()); // 0 is the request code, it should be unique id

        }
        else if(type.equals("video_notification")) {

            Intent intent = new Intent(getApplicationContext(), PalSplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);

            @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 22, intent, FLAG_MUTABLE);
            String CHANNEL_ID = "channel_name";// The id of the channel.
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(iprep_logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "all";// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            notificationManager.notify(22, notificationBuilder.build()); // 0 is the request code, it should be unique id

            HashMap<String,String> data =new HashMap<>();
            data.put("message",title);
            data.put("messageTime", String.valueOf(System.currentTimeMillis()));
            data.put("notificationMessage",message);
            data.put("sender","iPrep");
            data.put("studentId",Util.getUserId(getApplicationContext()));
            data.put("type",type);
            data.put("fileLink",fileLink);

            FirebaseDatabase.getInstance().getReference().child("notifications").child("student").child(Util.getUserId(getApplicationContext()))
                    .push().setValue(data);

        }
        else if(type.equals("image_notification") ) {
            new generatePictureStyleNotification(this,title, message,
                    fileLink).execute();

            HashMap<String,String> data =new HashMap<>();
            data.put("message",title);
            data.put("messageTime", String.valueOf(System.currentTimeMillis()));
            data.put("notificationMessage",message);
            data.put("sender","iPrep");
            data.put("studentId",Util.getUserId(getApplicationContext()));
            data.put("type",type);
            data.put("fileLink",fileLink);

            FirebaseDatabase.getInstance().getReference().child("notifications").child("student").child(Util.getUserId(getApplicationContext()))
                    .push().setValue(data);

        }
    }
}

