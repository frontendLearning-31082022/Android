package com.example.notifsoundssetter.modules;

import android.os.Environment;

import com.example.notifsoundssetter.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConfFile {
    Types appSound;

    Map<String, Object> allConf;

    public final static File confFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/notifications_conf");

    public ConfFile(Types appSound) {
        this.appSound = appSound;
    }

    public ConfFile() {
        this.allConf = new HashMap<>();
    }

    public void writeConf(Map<String, String> data) throws IOException, JSONException {

        if (appSound.equals(Types.AppSound)) {
            File conf = new File(confFolder.getAbsolutePath() + "/appsConf");
            JSONObject json = new JSONObject();
            if (conf.exists())
                json = new JSONObject(Files.readAllLines(Paths.get(conf.getAbsolutePath())).stream().map(x -> x)
                        .collect(Collectors.joining()));
            if (!conf.exists()) conf.createNewFile();

            json.put(data.get("app"), data.get("sound"));
            Files.write(conf.toPath(), json.toString().getBytes());

            new String();
        } else if (appSound.equals(Types.TextSound)) {
            File conf = new File(confFolder.getAbsolutePath() + "/textConf");
            JSONObject json = new JSONObject();
            if (conf.exists())
                json = new JSONObject(Files.readAllLines(Paths.get(conf.getAbsolutePath())).stream().map(x -> x)
                        .collect(Collectors.joining()));
            if (!conf.exists()) conf.createNewFile();

            String text_lower=data.get("text_sound").toLowerCase();
            json.put(text_lower, data.get("sound"));
            Files.write(conf.toPath(), json.toString().getBytes());
        }


        try {
            MainActivity.confFile.readAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        new String();
    }

    public void readAll() throws Exception {
        File appConf = new File(confFolder.getAbsolutePath() + "/appsConf");
        if (appConf.exists()) {
            JSONObject json = new JSONObject(Files.readAllLines(Paths.get(appConf.getAbsolutePath())).stream().map(x -> x)
                    .collect(Collectors.joining()));
            HashMap apps = new HashMap();
            json.keys().forEachRemaining(x -> {
                try {
                    apps.put(x, json.get(x));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            this.allConf.put("apps", apps);
        }

        File textConf = new File(confFolder.getAbsolutePath() + "/textConf");
        if (textConf.exists()) {
            JSONObject json = new JSONObject(Files.readAllLines(Paths.get(textConf.getAbsolutePath())).stream().map(x -> x)
                    .collect(Collectors.joining()));
            HashMap texts = new HashMap();
            json.keys().forEachRemaining(x -> {
                try {
                    texts.put(x, json.get(x));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            this.allConf.put("textIndexOf", texts);
        }


    }


    public enum Types {
        AppSound,TextSound
    }
}
