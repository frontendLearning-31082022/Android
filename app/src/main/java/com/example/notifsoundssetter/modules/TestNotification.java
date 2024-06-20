package com.example.notifsoundssetter.modules;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.example.notifsoundssetter.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestNotification {

    public static void showNotification(String title, String message, String pack, PendingIntent intentOfNotif,Context context ) {

        if (pack==null)pack="Null";
        message = pack + "| " + message + "  " + new SimpleDateFormat("HH:mm:ss MM/dd/yyyy").format(new Date());

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }

//        Intent resultIntent = new Intent(this, NotificationActivity.class);
//        resultIntent.putExtra("msg",message);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent = new Intent(context, TestNotification.class);
        intent.putExtra("msg", message);
        intent.putExtra("processName", pack);
        intent.putExtra("TitleMessage", title+message);
//        intent.putExtra("intent", intent);
        String messageForNotif = message.replaceAll("^com\\.[A-z\\.]*.[^\\w|\\s]", "");

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), 1, options);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.icon) // notification icon
                .setContentTitle(title) // title for notification
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentText(messageForNotif)// message for notification
//                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageForNotif));


        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);

        mNotificationManager.notify(2, mBuilder.build());




    }
}
