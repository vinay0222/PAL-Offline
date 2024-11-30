package com.idreameducation.ipreppal.notification;

import static android.app.PendingIntent.FLAG_MUTABLE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.idreameducation.ipreppal.R.drawable.iprep_logo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.pal.activity.PalSplashActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

    private Context mContext;
    private String title, message, imageUrl;

    public generatePictureStyleNotification(Context context, String title, String message, String imageUrl) {
        super();
        this.mContext = context;
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        InputStream in;
        try {
            URL url = new URL(this.imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        Intent intent = new Intent(mContext, PalSplashActivity.class);
        intent.putExtra("key", "value");
        @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent, FLAG_MUTABLE);

//        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notif = new Notification.Builder(mContext)
//                .setContentIntent(pendingIntent)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(result)
//                .setStyle(new Notification.BigPictureStyle().bigPicture(result))
//                .build();
//        notif.flags|= Notification.FLAG_AUTO_CANCEL;
//        notificationManager.notify(1, notif);



        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(iprep_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(result)
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
}