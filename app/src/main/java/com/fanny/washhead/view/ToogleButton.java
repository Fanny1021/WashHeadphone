package com.fanny.washhead.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.fanny.washhead.R;

/**
 * Created by Fanny on 18/3/7.
 */

public class ToogleButton extends View {

    private Bitmap backgroundBitmap;
    private Bitmap slideBitmap;
    private int slideBitmapLeftPosMax;
    private int slideBitmapLefePos;
    private int downX;

//    public ToogleButton(Context context) {
//        super(context,null);
//    }
//
    public ToogleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.toogle_background);
        slideBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.toogle_slidebg);

        slideBitmapLeftPosMax = backgroundBitmap.getWidth() - slideBitmap.getWidth();

//        String namespace="http://schemas.android.com/apk/res/com.fanny.washhead.view";
//        boolean booleanValue=attrs.getAttributeBooleanValue(namespace,"state",false);
//        setState(booleanValue);
        setState(true);
    }

//    public ToogleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        canvas.drawBitmap(slideBitmap, slideBitmapLefePos, 0, null);
    }

    public void setState(boolean b) {
        if (b) {
            slideBitmapLefePos = slideBitmapLeftPosMax;
        } else {
            slideBitmapLefePos = 0;
        }
    }

    int TOUCHMODE = 0;
    private int ACTION_DOWN = 1;
    private int ACTION_MOVE = 2;
    private onToogleButtonStateChangedListener listener;
    int preSlideBitmapLeftPos = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                TOUCHMODE = ACTION_DOWN;
                preSlideBitmapLeftPos = slideBitmapLefePos;
                // 获取手指按下时的x坐标
                downX = (int) event.getX();
                break;

            case MotionEvent.ACTION_MOVE:

                TOUCHMODE = ACTION_MOVE;
                int moveX = (int) event.getX();
                Log.e("ToogleButton", "action_move" + moveX);

                int distanceX = moveX - downX;

                slideBitmapLefePos += distanceX;

                if (slideBitmapLefePos > slideBitmapLeftPosMax) {
                    slideBitmapLefePos = slideBitmapLeftPosMax;
                } else {
                    slideBitmapLefePos = 0;
                }

                downX = moveX;

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (TOUCHMODE == ACTION_DOWN) {
                    if (slideBitmapLefePos == 0) {
                        slideBitmapLefePos = slideBitmapLeftPosMax;
                    } else {
                        slideBitmapLefePos = 0;
                    }
                } else {
                    if (slideBitmapLefePos < slideBitmapLeftPosMax / 2) {
                        slideBitmapLefePos = 0;
                    } else {
                        slideBitmapLefePos = slideBitmapLeftPosMax;
                    }
                }

                if (preSlideBitmapLeftPos != slideBitmapLefePos) {
                    if (listener != null) {
                        listener.onStateChanged(slideBitmapLefePos == slideBitmapLeftPosMax);
                    }
                }

                invalidate();

                break;
            default:
                break;

        }
        return true;
    }

    public interface onToogleButtonStateChangedListener {
        void onStateChanged(boolean b);
    }

    public void setOnToogleButtonStateChangedListener(onToogleButtonStateChangedListener listener) {
        this.listener = listener;
    }
}
