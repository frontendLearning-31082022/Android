package com.example.notifsoundssetter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.notifsoundssetter.modules.Chooser;
import com.example.notifsoundssetter.modules.ConfFile;
import com.example.notifsoundssetter.modules.PlaySound;
import com.example.notifsoundssetter.modules.ScenariosSets;
import com.example.notifsoundssetter.modules.TestNotification;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MainActivity extends AppCompatActivity {


    public static ConfFile confFile;

    public static MainActivity mainContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainContext=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            askPermissions();
        }

        createFoldersIfNoExist();
        setContentView(R.layout.activity_main);
        confFile=new ConfFile();
        try {
            confFile.readAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);


        MainActivity contextMain = this;
        Button buttonOne = (Button) findViewById(R.id.testNotif);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                TestNotification.showNotification("Title", "msg", "pack", null, contextMain);
            }
        });

        Button buttonSoundForApp = (Button) findViewById(R.id.buttonSoundForApp);
        buttonSoundForApp.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ScenariosSets.appSound();
            }
        });


        Button buttonSoundForText = (Button) findViewById(R.id.buttonSoundByText);
        buttonSoundForText.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ScenariosSets.textSounds();
            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    void askPermissions() {

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                1);


        boolean perFilesAll = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            perFilesAll = Environment.isExternalStorageManager();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            perFilesAll = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            perFilesAll = true;
        }

        if (perFilesAll) return;

        int requestCode = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", MainActivity.this.getPackageName())));
                MainActivity.this.startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                MainActivity.this.startActivityForResult(intent, requestCode);
            }
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    requestCode);
        }


    }

    void createFoldersIfNoExist(){
        if(!PlaySound.musicFolder.exists())PlaySound.musicFolder.mkdirs();
        if(!ConfFile.confFolder.exists()) ConfFile.confFolder.mkdirs();
    }

}