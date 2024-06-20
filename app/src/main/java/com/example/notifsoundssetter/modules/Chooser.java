package com.example.notifsoundssetter.modules;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class Chooser {

    Types type;

    Map data;

    public Chooser(Types type,Map data) {
        this.type = type;
        this.data=data;
        if(data==null)this.data=new HashMap();

    }

    public void choose(Context context,String title,Consumer<Object> onChoose) {
        String[] options = getOptions(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.put("option", options[which]);
                onChoose.accept(data);
            }
        });
        builder.show();
    }

    private String[] getOptions(Context context) {
        if (type.equals(Types.APP)) {
            PackageManager pm = context.getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            return packages.stream().map(x -> x.packageName).sorted().toArray(size -> new String[size]);
        } else if (type.equals(Types.SOUND)) {
            return Arrays.stream(PlaySound.musicFolder.listFiles()).map(q->q.getName()).toArray(size -> new String[size]);
        }


        return new String[]{};
    }

    public enum Types {
        APP,SOUND
    }

    public interface ChooserFN {
        void onChooseFN(Consumer<Object> consumer);
    }
}
