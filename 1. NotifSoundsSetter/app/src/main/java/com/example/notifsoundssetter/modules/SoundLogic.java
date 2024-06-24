package com.example.notifsoundssetter.modules;

import com.example.notifsoundssetter.MainActivity;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SoundLogic implements NotifCatcher.NotifCatcherImpl {

    @Override
    public void onCatch(String title, String text, String pack, Date date) {
        String sound=null;


        String soundByTextIndexOf = getSoundFileNameByIndexOf(title+text,"textIndexOf");
        sound=soundByTextIndexOf;
        if(soundByTextIndexOf==null){
            String soundByPack = getSoundFileName(pack,"apps");
            sound=soundByPack;
        }
        if (sound == null) return;

        try {
            new PlaySound(MainActivity.mainContext).play(sound);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new String();
    }


    private String getSoundFileName(String packageName,String type) {
        Map textIndexOf = (Map) MainActivity.confFile.allConf.get(type);
        String sound = (String) textIndexOf.get(packageName);
        return sound;
    }
    private String getSoundFileNameByIndexOf(String text,String type) {
        String finalText = text.toLowerCase();

        Map<String,String> textIndexOf = (Map) MainActivity.confFile.allConf.get(type);
        List<String> contains= textIndexOf.entrySet().stream()
                .filter(x-> finalText.indexOf(x.getKey())>-1)
                .map(x->x.getValue()).collect(Collectors.toList());
        return contains.size()>0 ? contains.get(0) : null;
    }
}
