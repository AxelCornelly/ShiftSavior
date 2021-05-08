package edu.harvard.cs50.shiftsavior;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nBuilder = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(TaskActivity.broadCastCode, nBuilder.build());
    }
}
