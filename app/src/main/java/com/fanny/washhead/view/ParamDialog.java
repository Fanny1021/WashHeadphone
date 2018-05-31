package com.fanny.washhead.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.fanny.washhead.R;
import com.fanny.washhead.activity.LoginActivity;

import java.lang.reflect.Field;

/**
 * Created by Fanny on 18/1/10.
 */

public class ParamDialog extends DialogFragment implements NumberPicker.Formatter {

    private String[] mContent = {"0.0", "1.0", "2.0", "3.0", "4.0", "5.0"};

//    private String[] mWashNumm = {"1", "2", "3", "4", "5", "6"};
//    private String[] mWetTime = {"60", "70", "80", "90", "100", "110", "120"};
//    private String[] mHairCap = {"10", "15", "20", "25", "30", "35", "40"};
//    private String[] mkneadNum = {"60", "70", "80", "90", "100", "110", "120"};
//    private String[] mWashTime = {"60", "70", "80", "90", "100", "110", "120"};

    private String[] mQWetTime={"1","2"};
    private String[] mQLiquidNum={"1","2"};
    private String[] mQKnead={"1","2"};
    private String[] mQWashTime={"1","2"};
    private String[] mQDryTime={"1","2","3"};

    private String[] mSWetTime={"2","3","4","5"};
    private String[] mSLiquidNum={"2","3"};
    private String[] mSKnead={"2","3"};
    private String[] mSWashTime={"2","3"};
    private String[] mSDryTime={"2","3","4","5"};

    private String[] mZWetTime={"1","2","3","4","5"};
    private String[] mZLiquidNum={"1","2","3"};
    private String[] mZKnead={"1","2","3"};
    private String[] mZWashTime={"1","2","3"};
    private String[] mZDryTime={"1","2","3","4","5"};

    private String title = "次数设定";

    private NumberPicker numberPicker;
    private View diaView;
    private TextView tvTitle;

    @Override
    public String format(int i) {
        return mContent[i];
    }

    public interface CallBack {
        void onClick(String Result,String title);
    }

    private CallBack callBack;

    public void show(FragmentManager manager) {
        show(manager, "");
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        diaView = inflater.inflate(R.layout.param_layout, null);
        tvTitle = (TextView) diaView.findViewById(R.id.tv_param_name);
        numberPicker = diaView.findViewById(R.id.np_param_scale);

        String tag = getTag();
        switch (tag) {
            case "快洗1":
                mContent = mQWetTime;
                title = "润湿时间设定";
                break;
            case "快洗2":
                mContent = mQLiquidNum;
                title = "洗涤次数设定";
                break;
            case "快洗3":
                mContent = mQKnead;
                title = "搓柔时间设定";
                break;
            case "快洗4":
                mContent = mQWashTime;
                title = "冲洗时间设定";
                break;
            case "快洗5":
                mContent = mQDryTime;
                title = "风干时间设定";
                break;

            case "慢洗1":
                mContent = mSWetTime;
                title = "润湿时间设定";
                break;
            case "慢洗2":
                mContent = mSLiquidNum;
                title = "洗涤次数设定";
                break;
            case "慢洗3":
                mContent = mSKnead;
                title = "搓柔时间设定";
                break;
            case "慢洗4":
                mContent = mSWashTime;
                title = "冲洗时间设定";
                break;
            case "慢洗5":
                mContent = mSDryTime;
                title = "风干时间设定";
                break;

            case "自定义洗1":
                mContent = mZWetTime;
                title = "润湿时间设定";
                break;
            case "自定义洗2":
                mContent = mZLiquidNum;
                title = "洗涤次数设定";
                break;
            case "自定义洗3":
                mContent = mZKnead;
                title = "搓柔时间设定";
                break;
            case "自定义洗4":
                mContent = mZWashTime;
                title = "冲洗时间设定";
                break;
            case "自定义洗5":
                mContent = mZDryTime;
                title = "风干时间设定";
                break;

            default:
                break;
        }


        tvTitle.setText(title);
        numberPicker.setDisplayedValues(mContent);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(mContent.length - 1);

        /**
         * 通过反射改变分割线颜色
         */
        setPidckerDividerColor(Color.parseColor("#cbcdcd"));
        /**
         * 通过反射改变选择器数字的颜色
         */
        setNumberPickerTextColor(numberPicker, Color.parseColor("#27c686"));

        builder.setView(diaView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int value = numberPicker.getValue();
                        String res = mContent[value];
                        Log.e("numbe", res + "");
                        Log.e("title", title);

                        /**
                         * 接口回调，将数据传输出去
                         */
                        callBack.onClick(res,title);


                    }
                })
                .setNegativeButton("跳过", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();

    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallBack) {
            callBack = (CallBack) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callBack = null;
    }

    /**
     * 定义dialog的位置尺寸
     */
    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        lp.x = 0;
        lp.y = 0;
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.alpha = 1.0f;

        //设置dialog底部出现和消失动画
        lp.windowAnimations=R.style.BottomDialogAnimation;

        dialogWindow.setAttributes(lp);
    }

    private void setPidckerDividerColor(int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(numberPicker, new ColorDrawable(color));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
                    Log.w("setTextColor", e);
                } catch (IllegalAccessException e) {
                    Log.w("setTextColor", e);
                } catch (IllegalArgumentException e) {
                    Log.w("setTextColor", e);
                }
            }
        }
        return false;
    }
}
