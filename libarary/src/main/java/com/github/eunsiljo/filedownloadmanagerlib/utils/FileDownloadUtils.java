package com.github.eunsiljo.filedownloadmanagerlib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;

import java.io.File;

/**
 * Created by EunsilJo on 2017. 10. 13..
 */

public class FileDownloadUtils {
    public static String TAG = FileDownloadUtils.class.getSimpleName();

    public static String getFileName(String path){
        if(path == null){
            return "";
        }
        int index = path.lastIndexOf("/");
        String fileName = "";
        if(index != -1){
            fileName = path.substring(index + 1);
        }
        return fileName;
    }

    public static String getExtension(String path){
        if(path == null){
            return "";
        }
        int index = path.lastIndexOf(".");
        String fileExtension = "";
        if(index != -1){
            fileExtension = path.substring(index);
        }
        return fileExtension;
    }

    public static String getStringSize(long size) {
        return (double)size >= 1.073741824E9D?String.format("%1.2fGB", new Object[]{Double.valueOf((double)size / 1.073741824E9D)}):((double)size > 1048576.0D?String.format("%1.2fMB", new Object[]{Double.valueOf((double)size / 1048576.0D)}):((double)size > 1024.0D?String.format("%1.2fKB", new Object[]{Double.valueOf((double)size / 1024.0D)}):(size > 0L?String.format("%1.2dB", new Object[]{Long.valueOf(size)}):"0B")));
    }

    public static long getTotalMemorySize(String path) {
        File file = new File(path);
        file.mkdirs();
        StatFs stat = new StatFs(path);
        long blockSize = (long)stat.getBlockSize();
        long totalBlocks = (long)stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public static long getAvailableMemorySize(String path) {
        File file = new File(path);
        file.mkdirs();
        StatFs stat = new StatFs(path);
        long blockSize = (long)stat.getBlockSize();
        long totalBlocks = (long)stat.getAvailableBlocks();
        return totalBlocks * blockSize;
    }

    public static void setStoragePath(Context context, String path) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("storage_location", path);
        editor.commit();
    }

    public static String getStoragePath(Context context) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName();
        String storagePath = preference.getString("storage_location", defaultPath);
        return storagePath;
    }
}
