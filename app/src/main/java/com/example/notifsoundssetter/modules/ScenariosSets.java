package com.example.notifsoundssetter.modules;

import com.example.notifsoundssetter.MainActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

public class ScenariosSets {

    public static void appSound(){
        MainActivity context=MainActivity.mainContext;

        Consumer<Object> writeConf = x -> {
            Map data= (Map) x;
            data.put("sound",data.get("option"));

            try {
                new ConfFile( ConfFile.Types.AppSound).writeConf(data);
            } catch (Exception e) {
                new String();
            }
        };

        Consumer<Object> soundChoose = x -> {
            Map data= (Map) x;
            data.put("app",data.get("option"));

            new Chooser(Chooser.Types.SOUND,data)
                    .choose(context, "Выберите звук",writeConf);
        };

        new Chooser(Chooser.Types.APP,null)
                .choose(context, "Выберите приложение",soundChoose);

    }

    public static void textSounds() {
        MainActivity context=MainActivity.mainContext;

        Consumer<Object> writeConf = x -> {
            Map data= (Map) x;
            data.put("sound",data.get("option"));
//

            try {
                new ConfFile( ConfFile.Types.TextSound).writeConf(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        };

        Consumer<Object> soundChoose = x -> {
            Map data= (Map) x;
            data.put("text_sound",data.get("text"));

            new Chooser(Chooser.Types.SOUND,data)
                    .choose(context, "Выберите звук",writeConf);
        };

        new InputText().show(context,"Введите текст",soundChoose);

    }
}
