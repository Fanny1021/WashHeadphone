package com.fanny.washhead.fragment;

//import android.app.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanny.washhead.R;
import com.fanny.washhead.view.HairParamDialog;
import com.fanny.washhead.view.ParamDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.R.attr.switchMinWidth;
import static android.R.attr.tag;


/**
 * Created by Fanny on 18/3/15.
 */

public class AddSetDialog extends DialogFragment implements View.OnClickListener {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private View rootView;
    private int width;
    private int height;
    private LinearLayout ll_wetting;
    private LinearLayout ll_liquid;
    private LinearLayout ll_knead;
    private LinearLayout ll_wash;
    private LinearLayout ll_dry;
    private TextView wet_value;
    private TextView liquid_value;
    private TextView knead_value;
    private TextView wash_value;
    private TextView dry_value;
    private Button btn_ok;
    private Button btn_cancle;
    String TAG = "AddSeyDialogActivity";
    private TextView title_mode;
    private String tag;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity());
        rootView = View.inflate(getActivity(), R.layout.add_set_dialoy, null);

        title_mode = rootView.findViewById(R.id.tv_mode_title);

        tag = getTag();

        ll_wetting = rootView.findViewById(R.id.ll_wetting);
        ll_liquid = rootView.findViewById(R.id.ll_liquid);
        ll_knead = rootView.findViewById(R.id.ll_knead);
        ll_wash = rootView.findViewById(R.id.ll_wash);
        ll_dry = rootView.findViewById(R.id.ll_dry);

        wet_value = rootView.findViewById(R.id.tv_set_wetting);
        liquid_value = rootView.findViewById(R.id.tv_set_liquid_num);
        knead_value = rootView.findViewById(R.id.tv_set_knead);
        wash_value = rootView.findViewById(R.id.tv_set_washing);
        dry_value = rootView.findViewById(R.id.tv_set_drying);

        ll_wetting.setOnClickListener(this);
        ll_liquid.setOnClickListener(this);
        ll_knead.setOnClickListener(this);
        ll_wash.setOnClickListener(this);
        ll_dry.setOnClickListener(this);

        /**
         * 设置不同mode下的参数
         */
        checkmode(tag);

        btn_ok = rootView.findViewById(R.id.btn_set_sure);
        btn_cancle = rootView.findViewById(R.id.btn_set_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wetValue = wet_value.getText().toString();
                String liquidNum = liquid_value.getText().toString();
                String kneadValue = knead_value.getText().toString();
                String washTime = wash_value.getText().toString();
                String dryTime = dry_value.getText().toString();
                if (checkData(wetValue, liquidNum, kneadValue, washTime, dryTime)) {
                    callBack1.onClick(wetValue, liquidNum, kneadValue, washTime, dryTime);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "参数设置有误", Toast.LENGTH_SHORT).show();
                }
            }


        });

        builder.setView(rootView);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private boolean checkData(String wetValue, String liquidNum, String kneadValue, String washTime, String dryTime) {
        if (!checkWet(wetValue)) {
            return false;
        }
        if (!checkLiquid(liquidNum)) {
            return false;
        }
        if (!checkKnead(kneadValue)) {
            return false;
        }
        if (!checkWash(washTime)) {
            return false;
        }
        if (!checkDry(dryTime)) {
            return false;
        }
        return true;
    }

    private boolean checkDry(String str) {
        return true;
    }

    private boolean checkWash(String str) {
        return true;
    }

    private boolean checkKnead(String str) {
        return true;
    }

    private boolean checkLiquid(String str) {
        return true;
    }

    private boolean checkWet(String str) {
        return true;
    }


    private void checkmode(String tag) {
        title_mode.setText(tag);
        switch (tag) {
            case "快洗":
//                ll_wetting.setEnabled(false);
//                ll_liquid.setEnabled(false);
//                ll_knead.setEnabled(false);
//                ll_wash.setEnabled(false);
//                ll_dry.setEnabled(false);
                wet_value.setText("1");
                liquid_value.setText("1");
                knead_value.setText("1");
                wash_value.setText("1");
                dry_value.setText("1");
//                ll_wetting.setEnabled(true);
//                ll_liquid.setEnabled(true);
//                ll_knead.setEnabled(true);
//                ll_wash.setEnabled(true);
//                ll_dry.setEnabled(true);
                break;
            case "慢洗":
//                ll_wetting.setEnabled(false);
//                ll_liquid.setEnabled(false);
//                ll_knead.setEnabled(false);
//                ll_wash.setEnabled(false);
//                ll_dry.setEnabled(false);
                wet_value.setText("2");
                liquid_value.setText("2");
                knead_value.setText("2");
                wash_value.setText("2");
                dry_value.setText("2");

//                ll_wetting.setEnabled(true);
//                ll_liquid.setEnabled(true);
//                ll_knead.setEnabled(true);
//                ll_wash.setEnabled(true);
//                ll_dry.setEnabled(true);
                break;
            case "自定义洗":
                wet_value.setText("3");
                liquid_value.setText("3");
                knead_value.setText("3");
                wash_value.setText("3");
                dry_value.setText("3");
//                ll_wetting.setEnabled(true);
//                ll_liquid.setEnabled(true);
//                ll_knead.setEnabled(true);
//                ll_wash.setEnabled(true);
//                ll_dry.setEnabled(true);
                break;
        }
    }


    public void setTvSetWetting(String str) {
        wet_value.setText(str);
    }

    public void setTvSetLiquidNum(String str) {
        liquid_value.setText(str);
    }

    public void setTvSetKnead(String str) {
        knead_value.setText(str);
    }

    public void setTvSetWashing(String str) {
        wash_value.setText(str);
    }

    public void setTvSetDrying(String str) {
        dry_value.setText(str);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public interface CallBack1 {
        void onClick(String wetValue, String liquidNum, String kneadValue, String washTime, String dryTime);
    }

    private CallBack1 callBack1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallBack1) {
            callBack1 = (CallBack1) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callBack1 = null;
    }

    /**
     * 定义dialog的位置尺寸
     */
    @Override
    public void onResume() {
        super.onResume();

        //获取屏幕宽高
//        WindowManager wm = (WindowManager) getActivity()
//                .getSystemService(getActivity().WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        int width =display.getWidth();
//        int height=display.getHeight();
//        WindowManager manager=getActivity().getWindowManager();
//        DisplayMetrics metrics=new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(metrics);
//        width = metrics.widthPixels;
//        height = metrics.heightPixels;

        //得到dialog对应的窗口
        Window dialogWindow = getDialog().getWindow();

        //去掉dialog默认的padding
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
        lp.x = 0;
        lp.y = 0;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

//        lp.width=(int)(width*0.9);
//        lp.height=(int)(height*0.6);

        lp.alpha = 0.9f;
        dialogWindow.setAttributes(lp);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wetting:
                ParamDialog dialog = new ParamDialog();
                dialog.show(getActivity().getFragmentManager(), tag + "1");
                break;
            case R.id.ll_liquid:
                ParamDialog dialog1 = new ParamDialog();
                dialog1.show(getActivity().getFragmentManager(), tag + "2");
                break;
            case R.id.ll_knead:
                ParamDialog dialog2 = new ParamDialog();
                dialog2.show(getActivity().getFragmentManager(), tag + "3");
                break;
            case R.id.ll_wash:
                ParamDialog dialog3 = new ParamDialog();
                dialog3.show(getActivity().getFragmentManager(), tag + "4");

                break;
            case R.id.ll_dry:
                ParamDialog dialog4 = new ParamDialog();
                dialog4.show(getActivity().getFragmentManager(), tag + "5");
                break;

            default:
                break;
        }

    }

}
