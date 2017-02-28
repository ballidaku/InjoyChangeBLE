package com.ble.sharan.myUtilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.IntentCompat;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by brst-pc93 on 2/17/17.
 */

public class MyNotification
{


    public void showNotification(Context context, String message, int number)
    {

        int icon = R.mipmap.ic_notification;
        String title = "InjoyHealth";


        Intent intent = new Intent(context.getApplicationContext(), MainActivityNew.class);
        intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent pIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(NOTIFICATION_SERVICE);


        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (defaultSound == null)
        {
            defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (defaultSound == null)
            {
                defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }
        }
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_notification);


        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).
                                                                                            setContentTitle(title).
                                                                                            setContentText(message).
                                                                                            setContentIntent(pIntent).
                                                                                            setSmallIcon(icon).
                                                                                            setLargeIcon(largeIcon).
                                                                                            setLights(Color.MAGENTA, 1, 2).
                                                                                            setAutoCancel(true).
                                                                                            setStyle(style).setSound(defaultSound);

        //builder.setNumber(5);

        Notification notification = new NotificationCompat.BigTextStyle(builder).bigText(message).build();


        // hide the notification after its selected
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(number, notification);
    }



}
