package com.fanny.washhead.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.fanny.washhead.R;
import com.fanny.washhead.view.CircleProgressBar;

/**
 * Created by Fanny on 18/1/2.
 */

public class GuideUtils {
    private Context context;
    private ImageView imageView;
    private WindowManager windowManager;
    private static GuideUtils instance=null;

    /** 是否第一次进入该程序 **/
    private boolean isFirst=true;

    /**采用私有的方式，只保证这种通过单例来引用，同时保证这个对象不会存在多个**/
    private GuideUtils(){

    }

    /**采用单例的设计模式，同时用了同步锁**/
    public static GuideUtils getInstance(){
        synchronized (GuideUtils.class){
            if(instance == null){
                instance=new GuideUtils();
            }
        }
        return instance;
    }

    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    WindowManager.LayoutParams params=new WindowManager.LayoutParams();
//                    params.type=WindowManager.LayoutParams.TYPE_PHONE;
                    params.format= PixelFormat.RGBA_8888;
                    params.gravity= Gravity.LEFT|Gravity.TOP;
//                    params.width=ScreenUtils.getScreenWidth(context);
//                    params.height=ScreenUtils.getScreenHeight(context);
                    params.width= WindowManager.LayoutParams.WRAP_CONTENT;
                    params.height=WindowManager.LayoutParams.WRAP_CONTENT;
                    params.windowAnimations= R.style.view_anim;

                    windowManager.addView(imageView,params);
                    break;
            }
        }
    };

    public void initGuide(Activity context,int drawableResourceId){
        if(!isFirst()){
            return;
        }

        this.context=context;
        windowManager=context.getWindowManager();

        //        imageView.setLayoutParams(new ViewGroup.LayoutParams
//                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//        imageView.setLayoutParams(new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT));

//        imageView.setLayoutParams(new ViewGroup.LayoutParams(20,20));
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);



        imageView=new ImageView(context);

//        imageView.setBackgroundColor(Color.GREEN);
        imageView.setImageResource(drawableResourceId);

        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Matrix matrix = new Matrix();
        matrix.setScale(1, 1);
        Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(b);




        handler.sendEmptyMessage(1);
//        handler.sendEmptyMessageDelayed(1,1000);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager.removeView(imageView);
            }
        });
    }


    public boolean isFirst(){
        return isFirst;
    }

    public void setFirst(boolean isFirst){
        this.isFirst=isFirst;
    }


}
