package com.example.notifsoundssetter.modules;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.example.notifsoundssetter.R;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class PlaySound {

    MediaPlayer mediaPlayer;
    Context context;

    public final static File musicFolder= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/notifications_sounds");

    public PlaySound(Context context) {
        this.mediaPlayer = new MediaPlayer();
        this.context = context;

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(context.getApplicationContext(),
                        "start playing sound", Toast.LENGTH_SHORT).show();
                mediaPlayer.start();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(context.getApplicationContext(), String.format(Locale.US,
                        "Media error what=%d extra=%d", what, extra), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    public void play(String fileName) throws IOException {
//        Uri soundUri = Uri.parse(path);
        Uri uri= Uri.fromFile(new File(musicFolder+"/"+fileName));
        mediaPlayer = MediaPlayer.create(context, uri);
//        mediaPlayer.setDataSource(context.getApplicationContext(), R.raw.test);
        mediaPlayer.start();

    }
//
//    @Override
//    public void onChooseFN(String s) {
//        new ConfFile().onChooseFN();
//    }
}
