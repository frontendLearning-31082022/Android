package com.example.notifsoundssetter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.example.notifsoundssetter.modules.error_logs.UnexpectedCrashSaver;

public class Stacktrace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacktrace);

        TextView stacktrace_text_view = (TextView) findViewById(R.id.stacktrace_text_view);
        stacktrace_text_view.setText(Html.fromHtml( UnexpectedCrashSaver.getLogs()));
    }
}