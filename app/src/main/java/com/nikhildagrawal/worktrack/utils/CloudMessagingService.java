package com.nikhildagrawal.worktrack.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.TabActivity;

import androidx.core.app.NotificationCompat;

public class CloudMessagingService extends FirebaseMessagingService {


    private static final String TAG = "CloudMessagingService";
    private static final int BROAD_CAST_NOTIFICATION_ID = 100;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG,s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Message",remoteMessage.toString());
        Log.d("Message",remoteMessage.getData().toString());

        String title = remoteMessage.getData().get(getString(R.string.notification_title));
        String message = remoteMessage.getData().get(getString(R.string.notification_message));

        sendBroadCast(title,message);
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }


    public void sendBroadCast(String title, String message){

        //Notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"default_notification_channel");
        Intent notifyIntent = new Intent(this, TabActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.drawable.ic_chat_accent_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.sportcar))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setColor(getResources().getColor(R.color.colorchip))
                .setAutoCancel(true);

        builder.setContentIntent(notifyPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(BROAD_CAST_NOTIFICATION_ID,builder.build());


    }


}
