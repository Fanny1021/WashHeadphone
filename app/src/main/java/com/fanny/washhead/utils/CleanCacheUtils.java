package com.fanny.washhead.utils;

import android.content.Context;
import android.nfc.Tag;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by Fanny on 18/1/5.
 */

public class CleanCacheUtils {

    public static String TAG = "CleanCacheUtils";

    /**
     * 获取长时间保存的文件：Context.getExternalFilesDir()
     * 存储位置： SDCard/Android/data/com.fanny.washhead/files/
     * 获取临时缓存：Context.getExternalCacheDir()
     * 存储位置：SDCard/Android/data/com.fanny.washhead/cache/
     * @param context
     * @return
     */

    /**
     * 获取当前缓存
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }

        Log.e(TAG, cacheSize + "");

        return getFormatSize(cacheSize);

    }

    /**
     * 删除缓存
     *
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED
        )) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int size = 0;
            if (children != null) {
                size = children.length;
                for (int i = 0; i < size; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }

        if (dir == null) {
            return true;
        } else {
            return dir.delete();
        }
    }

    /**
     * 获取文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();
        int size1 = 0;
        if (fileList != null) {
            size1 = fileList.length;
            for (int i = 0; i < size1; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        }

        return size;
    }

    /**
     * 格式化单位
     * 计算
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kilobyte = size / 1024;
        if (kilobyte < 1) {
            return "OK";
        }

        double megaByte = kilobyte / 1024;
        if (megaByte < 1) {
            BigDecimal result = new BigDecimal(Double.toString(kilobyte));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }

        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";

    }
}
