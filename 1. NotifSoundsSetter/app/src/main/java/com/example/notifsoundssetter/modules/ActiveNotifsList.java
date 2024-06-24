package com.example.notifsoundssetter.modules;

import android.service.notification.StatusBarNotification;

import java.util.ArrayList;

public class ActiveNotifsList {
    private ArrayList<StatusBarNotification> list;

    public ActiveNotifsList(){
        this.list=new ArrayList<>();
    }

    public void posted(StatusBarNotification sbn) {
        list.add(sbn);
    }

    public void removed(StatusBarNotification sbn) {
        list.removeIf(x->x.getUid()==sbn.getUid());
    }

    public ArrayList<StatusBarNotification>  getAll(){
        return this.list;
    }
}
