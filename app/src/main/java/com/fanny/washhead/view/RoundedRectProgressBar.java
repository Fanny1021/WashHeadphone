package com.fanny.washhead.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.fanny.washhead.R;

/**
 * Created by Fanny on 18/1/10.
 */

public class RoundedRectProgressBar extends View {

    private Paint mPaint=new Paint() ;
    private int barColor=Color.parseColor("#33CC99");// 进度条颜色;
    private int backColor=Color.parseColor("#E6E6E6");// 进度条未完成的颜色;
    private int textColor=Color.parseColor("#ffffff");
    private float radius;
    int progress = 0;



    public RoundedRectProgressBar(Context context) {
        super(context,null);
    }

    public RoundedRectProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
//        mPaint=new Paint();
    }

    public RoundedRectProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        mPaint=new Paint();

        /*获取自定义参数的颜色值*/
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundedRectProgessBar, defStyleAttr, 0);
        int m = typedArray.getIndexCount();
        for (int i = 0; i < m; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.RoundedRectProgessBar_barColor:
                    barColor = typedArray.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.RoundedRectProgessBar_backColor:
                    backColor = typedArray.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.RoundedRectProgessBar_textColor:
                    textColor = typedArray.getColor(attr, Color.WHITE);
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        radius = this.getMeasuredHeight() / 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画背景
         */
        mPaint.setColor(backColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight()), radius, radius, mPaint);
        /**
         * 画进度条
         */
        mPaint.setColor(barColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(0, 0, this.getMeasuredWidth() * progress / 100f, getMeasuredHeight()), radius, radius, mPaint);
        /**
         * 画进度文字
         */
        mPaint.setColor(textColor);
        mPaint.setTextSize(this.getMeasuredHeight() / 1.2f);
        String text = "" + progress + "%";
        float x = this.getMeasuredWidth() * progress / 100 - mPaint.measureText(text) - 10;
        float y = this.getMeasuredHeight() / 2f - mPaint.getFontMetrics().ascent / 2f - mPaint.getFontMetrics().descent / 2f;
        canvas.drawText(text, x, y, mPaint);
    }

    public void setProgress(int progress) {
        if (progress > 100) {
            this.progress = 100;
        } else if (progress < 0) {
            this.progress = 0;
        } else {
            this.progress = progress;
        }

        postInvalidate();
    }
}
