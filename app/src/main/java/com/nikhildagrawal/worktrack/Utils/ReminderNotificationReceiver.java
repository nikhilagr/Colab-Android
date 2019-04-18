package com.nikhildagrawal.worktrack.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.fragments.AddReminderFragment;

import java.util.Date;

import androidx.core.app.NotificationCompat;

public class ReminderNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String CHANNEL_ID = "REMINDER_CHANNEL_ID";
        NotificationCompat.Builder builder = new NotificationCompat.Builder( context , CHANNEL_ID);

   Log.d("RECEIVER", intent.getExtras().getString("title"));
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setContentTitle(AddReminderFragment.reminderTitle)
                .setContentText(AddReminderFragment.reminderDescription)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id=(int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(id,builder.build());
    }
}
