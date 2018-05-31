package com.fanny.washhead.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Fanny on 18/1/2.
 */

public class ScreenUtils {

    /**
     * 获取DisplayMetrics对象
     * @方法名 getDisPlayMetrics
     * @param context
     * @return
     */
    public static DisplayMetrics getDisPlayMetrics(Context context){
        DisplayMetrics metrics=new DisplayMetrics();
        if(context!=null){
            ( (Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
        return metrics;
    }

    /**
     * 获取屏幕的宽度（像素）
     * @方法名 getScreenWidth
     * @param context
     * @return  int
     */
    public static int getScreenWidth(Context context){
        int width=getDisPlayMetrics(context).widthPixels;
        return width;
    }

    /**
     * 获取屏幕高度（像素）
     * @方法名 getScreenHeight
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        int height=getDisPlayMetrics(context).heightPixels;
        return height;
    }

    /**
     * 获取屏幕密度（0.75/1.0/1.5）
     * @param context
     * @return
     */
    public static float getDensity(Context context){
        float density=getDisPlayMetrics(context).density;
        return density;
    }

    public static int getDensityDpi(Context context){
        int densityDpi=getDisPlayMetrics(context).densityDpi;
        return densityDpi;
    }
}
