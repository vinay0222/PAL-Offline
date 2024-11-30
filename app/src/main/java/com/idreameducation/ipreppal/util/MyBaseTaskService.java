package com.idreameducation.ipreppal.util;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

/**
 * Base class for Services that keep track of the number of active jobs and self-stop when the
 * count is zero.
 */
public abstract class MyBaseTaskService extends Service {

    static final int PROGRESS_NOTIFICATION_ID = 4;
    static final int FINISHED_NOTIFICATION_ID = 5;

    private static final String TAG = "MyBaseTaskService";
    private int mNumTasks = 0;

    public void taskStarted() {
        changeNumberOfTasks(1);
    }

    public void taskCompleted() {
        changeNumberOfTasks(-1);
    }

    private synchronized void changeNumberOfTasks(int delta) {
        mNumTasks += delta;

        // If there are no tasks left, stop the service
        if (mNumTasks <= 0) {
            stopSelf();
        }
    }

    /**
     * Show notification with a progress bar.
     */
    protected void showProgressNotification(String caption, long completedUnits, long totalUnits) {
        int percentComplete = 0;
        if (totalUnits > 0) {
            percentComplete = (int) (100 * completedUnits / totalUnits);
        }

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.notification)
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText(caption)
//                .setProgress(100, percentComplete, false)
//                .setOngoing(true)
//                .setAutoCancel(false);
//
//        NotificationManager manager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        manager.notify(PROGRESS_NOTIFICATION_ID, builder.build());
    }

    /**
     * Show notification that the activity finished.
     */
    protected void showFinishedNotification(String caption, Intent intent, boolean success) {
        // Make PendingIntent for notification
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* requestCode */, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        int icon = success ? R.mipmap.notification: R.mipmap.notification;
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(icon)
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText(caption)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager manager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        manager.notify(FINISHED_NOTIFICATION_ID, builder.build());
    }

    /**
     * Dismiss the progress notification.
     */
    protected void dismissProgressNotification() {
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.cancel(PROGRESS_NOTIFICATION_ID);
    }
}
