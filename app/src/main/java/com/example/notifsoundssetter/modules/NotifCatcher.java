package com.example.notifsoundssetter.modules;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.Date;

public class NotifCatcher extends NotificationListenerService {


    Context context=null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder mIBinder = super.onBind(intent);

        return mIBinder;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {notifMsgCatch(sbn);
        }catch (Exception f){f.printStackTrace();}
    }


    void notifMsgCatch(StatusBarNotification sbn){
//        if (context==null)context= MainActivity.contextMainActivity;

        Bundle extras=null;
        try {extras = sbn.getNotification().extras;}catch (Exception d){return;}

        String pack ="";
        String title ="";
        String text ="";
        PendingIntent intent =null;

        try {
            Notification n = sbn.getNotification(); intent = n.contentIntent;}catch (Exception e){}
        try {  pack = sbn.getPackageName();  if (pack==null)pack=""; }catch (Exception e){}
        try {  title = extras.getString("android.title"); if (title==null)title=""; }catch (Exception e){}
        try {  text = extras.getCharSequence("android.text").toString();  }catch (Exception e){}


        new SoundLogic().onCatch(title,text,pack,new Date());
        new String();


    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        Log.i("Msg","Notification Removed");
    }

    interface NotifCatcherImpl {
        void onCatch(String title, String text, String pack, Date date);
    }
}
