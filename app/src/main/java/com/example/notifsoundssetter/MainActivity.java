package com.example.notifsoundssetter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.notifsoundssetter.modules.TestNotification;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);


        MainActivity contextMain=this;
        Button buttonOne = (Button) findViewById(R.id.testNotif);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                TestNotification.showNotification("Title","msg","pack",null,contextMain);
            }
        });


    }
}