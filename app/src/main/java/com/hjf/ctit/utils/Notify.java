package com.hjf.ctit.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.widget.Toast;

import com.hjf.ctit.R;
import com.hjf.ctit.constants.ActivityConstants;

import java.util.Calendar;

public class Notify {

    private static int MessageID = 0;

    public static void notifcation(final Context context, String messageString, Intent intent, int notificationTitle) {

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Calendar.getInstance().getTime().toString();
        long when = System.currentTimeMillis();
        CharSequence contentTitle = context.getString(notificationTitle);
        String ticker = contentTitle + messageString;
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                ActivityConstants.showHistory, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder notificationCompat = new Notification.Builder(context);
        notificationCompat.setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setContentIntent(pendingIntent)
                .setContentText(messageString)
                .setTicker(ticker)
                .setWhen(when)
                .setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = notificationCompat.getNotification();
        notification.defaults |= Notification.DEFAULT_LIGHTS;  // 通知灯光
        notification.defaults |= Notification.DEFAULT_VIBRATE; // 震动
//        notification.flags |= Notification.FLAG_NO_CLEAR;   // 通知不可以清除
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // 系统默认铃声
        mNotificationManager.notify(MessageID, notification);
        MessageID++;
    }

    public static void toast(Context context, String text, int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
