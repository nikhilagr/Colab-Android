package com.nikhildagrawal.worktrack.utils;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CloudMessagingService extends FirebaseMessagingService {


    private static final String TAG = "CloudMessagingService";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG,s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }



}
