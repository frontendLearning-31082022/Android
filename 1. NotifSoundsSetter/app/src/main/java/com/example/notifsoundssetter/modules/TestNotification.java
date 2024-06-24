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
    public int id=100;
    String title;
    String message;
    String pack;
    Context context;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;
    Intent intent;

    public TestNotification(String title, String message, String pack, Context context) {
        this.title = title;
        this.message = message;
        this.pack = pack;
        this.context = context;

        prepareText();
        prepare_body();
    }

    public void setAutoHide(int secs){
        mBuilder.setAutoCancel(true);
        mBuilder.setTimeoutAfter(secs*1000);
    }

    public void notifyNow(){
        try{
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pi);

            mNotificationManager.notify(id, mBuilder.build());
        }catch (Exception r){
            r.printStackTrace();
        }
    }

    private void prepare_body(){
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        id++;
        mBuilder.setSmallIcon(R.drawable.icon);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MIN);
    }


    private void prepareText(){
        if (pack==null)pack="Null";
        message = pack + "| " + message + "  " + new SimpleDateFormat("HH:mm:ss MM/dd/yyyy").format(new Date());
        intent = new Intent(context, TestNotification.class);
        intent.putExtra("msg", message);
        intent.putExtra("processName", pack);
        intent.putExtra("TitleMessage", title+message);
//        intent.putExtra("intent", intent);
        String messageForNotif = message.replaceAll("^com\\.[A-z\\.]*.[^\\w|\\s]", "");
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), 1, options);
        mBuilder = new NotificationCompat.Builder(context, "YOUR_CHANNEL_ID")
                .setContentTitle(title) // title for notification
                .setContentText(messageForNotif)// message for notification
//                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageForNotif));
    }
}
