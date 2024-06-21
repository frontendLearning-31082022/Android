package com.example.notifsoundssetter.modules;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

import com.example.notifsoundssetter.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SchedruleCheker {
    Context context;
    ActiveNotifsList activeNotifsList;
    AlarmManager alarmManager;
//    BroadcastReceiver br;
//    Supplier<Object> onStopBroadcastTimer;

    public SchedruleCheker(Context context, ActiveNotifsList activeNotifsList) {
        this.context = context;
        this.activeNotifsList = activeNotifsList;
        SchedruleCheker pass=this;
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                String minss= i.getIdentifier();
                pass.check(Double.parseDouble(minss));
            }
        };

//        br = new BroadcastReceiver((c,i) -> new UserAccount())
//        Consumer<Context,Intent> printer = (x,y)-> printf("%d долларов \n", x);

        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        context.registerReceiver(br, new IntentFilter("com.example.schedruleCheker"));
    }

    void setNewShedule(double mins) {
//        onStopBroadcastTimer= ()->{check(mins);  return null;};
//        Context context1=new Activity();
        Intent storeBoxIntent= new Intent("com.example.schedruleCheker");
        storeBoxIntent.setIdentifier(String.valueOf(mins));
//        storeBoxIntent.putExtra("VALUE", mins);

        int millis=(int) Math.round(mins*60*1000);

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, storeBoxIntent, PendingIntent.FLAG_MUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pi); //mins*60*1000
//        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);
    }
    private void check(double min) {
        Map<String, Object> all = (Map<String, Object>) MainActivity.confFile.allConf.get("schedruleSound");
        Map<String, Object> sameDurationIndexOf = new HashMap<>();
        sameDurationIndexOf.putAll(all);

        Set<Map.Entry<String, Object>> entries=all.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            try{
                String key = entry.getKey();
                Double val =Double.parseDouble(String.valueOf(entry.getValue()));
                if(val!=min)sameDurationIndexOf.remove(key);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        ArrayList<StatusBarNotification> currentNotifs = activeNotifsList.getAll();
        ArrayList<String>data=new ArrayList<>();
        List<String> containsWordList= currentNotifs.stream().map(x-> {
            Bundle extras = x.getNotification().extras;
            String bigTExt=extras.get(Notification.EXTRA_BIG_TEXT) == null ? null
                    : extras.get(Notification.EXTRA_BIG_TEXT).toString();
            String title=extras.get(Notification.EXTRA_TITLE) == null ? null
                    : extras.get(Notification.EXTRA_TITLE).toString();
           return title+bigTExt;
        }).collect(Collectors.toList());

        for (String str : containsWordList) {
            for (Object substr : sameDurationIndexOf.values()) {
                if(str.toLowerCase().indexOf(
                        String.valueOf(substr.toString().toLowerCase()))>-1)remindNotif(str);
            }
        }

//        activeNotifsList.getAll();

        setNewShedule(min);
        new String();

    }

    private void remindNotif(String str) {
        TestNotification.showNotification("Пропущено увеломление ", str, "", null, MainActivity.mainContext);
    }

    public void start_check() {
        Map<String, String> indexOf_minutes = (Map<String, String>) MainActivity.confFile.allConf.get("schedruleSound");
        List<Object> minutesTypes= indexOf_minutes.values().stream().distinct().collect(Collectors.toList());
        for (Object strNum : minutesTypes) {
            setNewShedule(Double.parseDouble(String.valueOf(strNum)));
        }

//        indexOf_minutes.values().stream().distinct().forEach(x->setNewShedule(Double.parseDouble(x)));
//        indexOf_minutes.values().stream().map(i->String.valueOf(i)).distinct().map(i->Double.parseDouble(String.valueOf(i))).toArray()
//        ArrayList<StatusBarNotification> current = activeNotifsList.getAll();
//        new String();
//        activeNotifsList.getAll();

//        setNewShedule(4000);
//        new String();

    }



}
