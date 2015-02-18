package com.dr8.sense6batterypercent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by drait on 2/17/15.
 */
public class Unpack {

    private static SharedPreferences prefs;
    private static ProgressDialog pd;

    @SuppressLint("DefaultLocale")
    public static void doUnzip(Context ctx) {

        final String intpath = ctx.getFilesDir().getParent() + "/files/";
        final String extpath = Environment.getExternalStorageDirectory().toString() + "/tmp/";

        prefs = ctx.getSharedPreferences("com.dr8.sense6batterypercent_preferences", Context.MODE_WORLD_READABLE);

        Log.d("S6BAT:", "Doing unpack...");
        File sd = new File(Environment.getExternalStorageDirectory().toString());
        if (sd.canWrite()) {
            File f = new File(extpath);
            if (prefs.getBoolean("firstrun", true)) {
                Log.d("S6BAT:", "firstrun true or missing");
                if (f.isDirectory()) {
                    try {
                        InputStream in = ctx.getResources().openRawResource(R.raw.images);
                        FileOutputStream out;
                        out = new FileOutputStream(extpath + "images.zip");
                        byte[] buff = new byte[1024];
                        int read = 0;
                        while ((read = in.read(buff)) > 0) {
                            out.write(buff, 0, read);
                        }
                        in.close();
                        out.close();
                    } catch (IOException e) {
                    }
                } else {
                    Log.d("S6BAT:", "ext /tmp dir not found, making");
                    f.mkdirs();
                    InputStream in = ctx.getResources().openRawResource(R.raw.images);
                    FileOutputStream out;
                    File f2 = new File(intpath);
                    if (!f2.isDirectory()) {
                        f2.mkdirs();
                        f2.setExecutable(true, false);
                        f2.setReadable(true, false);
                        f2.setWritable(true, true);
                    }
                    try {
                        out = new FileOutputStream(extpath + "images.zip");
                        byte[] buff = new byte[1024];
                        int read = 0;
                        while ((read = in.read(buff)) > 0) {
                            out.write(buff, 0, read);
                        }
                        in.close();
                        out.close();
                    } catch (IOException e) {
                    }
                }
                final String item = "images.zip";
                final String path = extpath;
                final File df = new File(intpath);
                df.mkdir();
                Log.d("S6BAT:", "Made it to the unpackZIP method");
                final String finalIntpath = intpath;
                AsyncTask<Void, Void, Void> importTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        ZipStuff.unpackZip(intpath, path + "/" + item);
                        ChmodRecursive(df);
                        prefs.edit().putBoolean("firstrun", false).commit();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        pd.dismiss();
                    }
                };

                pd = ProgressDialog.show(ctx, "Please Wait", "Unpacking battery icons...", true, false);
                importTask.execute((Void[])null);

            } else {
                Toast.makeText(ctx, "Your /sdcard is not writeable. Please check XPrivacy or other modules for blocking behavior", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("S6BAT:", "Can't write to SD");
        }
    }

    public static void ChmodRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                ChmodRecursive(child);
            }
        }
        fileOrDirectory.setExecutable(true, false);
        fileOrDirectory.setReadable(true, false);
        fileOrDirectory.setWritable(true, true);
    }
}
