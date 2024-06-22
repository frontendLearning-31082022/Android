package com.example.notifsoundssetter.modules.error_logs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.notifsoundssetter.MainActivity;
import com.example.notifsoundssetter.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnexpectedCrashSaver implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;
    private Context app = null;
    public final static File errorsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/notifications_errors");

    public UnexpectedCrashSaver(Context app) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.app = app;
    }
    public void uncaughtException(Thread t, Throwable e) {
        StackTraceElement[] arr = e.getStackTrace();
        String report = e.toString()+"\n\n";
        report += "--------- Stack trace ---------\n\n";
        for (int i=0; i<arr.length; i++) {
            report += "    "+arr[i].toString()+"\n";
        }
        report += "-------------------------------\n\n";

        Toast.makeText(app, "Saving",
                Toast.LENGTH_LONG).show();

        report += "--------- Cause ---------\n\n";
        Throwable cause = e.getCause();
        if(cause != null) {
            report += cause.toString() + "\n\n";
            arr = cause.getStackTrace();
            for (int i=0; i<arr.length; i++) {
                report += "    "+arr[i].toString()+"\n";
            }
        }
        report += "-------------------------------\n\n";
        try {
            FileOutputStream trace = app.openFileOutput("stack.trace",
                    Context.MODE_PRIVATE);
            trace.write(report.getBytes());
            trace.close();
        } catch(IOException ioe) {
            // ...
        }
        defaultUEH.uncaughtException(t, e);
    }

    public static void crashHandlerMenu(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean crash_never_ask_again = preferences.getBoolean("crash_never_ask_again", false);
        if (crash_never_ask_again)//Previously user check the checkbox of never ask me again about sending crash dialog
            return;
        String dialog_message = "In the last run, the program encountered an error, we apologize for this, you can kindly send us the error information to fix this error in future updates.";
        String button_positive_text = "Send";
        String button_negative_text = "Close";
        String checkbox_text = "Never ask again";
        String email = "crashreport@example.com";

        String line;
        String trace = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput("stack.trace")));
            while ((line = reader.readLine()) != null) {
                trace += line + "\n";
            }
        } catch (FileNotFoundException fnfe) {
            // ...
        } catch (IOException ioe) {
            // ...
        }
        if (trace.length() < 10)//We didn't have any crash
            return;

        View checkBoxView = View.inflate(context, R.layout.checkbox, null);
        CheckBox checkBox = checkBoxView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("checkbox_checked", true);
                editor.apply();
            }
        });
        checkBox.setText(checkbox_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        //builder.setIcon(R.drawable.ic_setting);
        builder.setMessage(dialog_message);
        builder.setView(checkBoxView);
        builder.setCancelable(false);
        String finalTrace = trace;
        builder.setPositiveButton(button_positive_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                String subject = "Error report";
                String body = "Mail this to appdeveloper@gmail.com: " + "\n" + finalTrace + "\n";

//                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
//                sendIntent.putExtra(Intent.EXTRA_TEXT, body);
//                sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//                sendIntent.setType("message/rfc822");
//                MainActivity.this.startActivity(Intent.createChooser(sendIntent, "Title:"));
                String pathFile = UnexpectedCrashSaver.errorsFolder.getAbsolutePath() + "/error_"
                        + new SimpleDateFormat("d.M.y H:m:s").format(new Date())+".txt";
                try {
                    Files.write(Paths.get(pathFile), finalTrace.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                context.deleteFile("stack.trace");
            }
        });
        builder.setNegativeButton(button_negative_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.deleteFile("stack.trace");
                boolean checkbox_checked = preferences.getBoolean("checkbox_checked", false);
                if (checkbox_checked) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("crash_never_ask_again", true);
                    editor.apply();
                }
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void writeErrorToFiles() throws IOException {
//        String filePath = Environment.getExternalStorageDirectory() + "/logcat.txt";
//        Runtime.getRuntime().exec(new String[]{"logcat", "-d", errorsFolder.getAbsolutePath()+"/errorsAll.txt"});
    }

    public static String getLogs(){
        StringBuilder builder = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"logcat", "-d", "*:E"});

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line=line.replaceAll("at","\r\n\tat");
                builder.append("<div style='background-color:#9ffbcd'>"+ line+"</div>");
            }
        } catch (IOException e){
            Log.e("Ooops","Error getting logs");
        }
        String html="<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                builder.toString()+
                "    \n" +
                "</body>\n" +
                "</html>";


        return html;
    }
}