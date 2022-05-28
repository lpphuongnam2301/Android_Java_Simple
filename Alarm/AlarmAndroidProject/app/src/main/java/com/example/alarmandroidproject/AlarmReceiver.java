package com.example.alarmandroidproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String time = intent.getStringExtra("time_clock");
        Intent intentAlarm = new Intent(context, AlarmClock.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentAlarm.putExtra("timetime", time);
        context.startActivity(intentAlarm);
    }
}
