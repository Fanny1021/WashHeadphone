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
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.fanny.washhead.R;
import com.fanny.washhead.activity.LoginActivity;
import com.fanny.washhead.adapter.HairModeAdapter;
import com.fanny.washhead.adapter.HairStyleAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fanny on 18/1/10.
 */

public class HairParamDialog extends DialogFragment {

    private View diaView;
    private ListView ls_hair;
    public static HairModeAdapter adapter1;

    public interface CallBack {
        void onClick(String Result);
    }

    private CallBack callBack;

    public void show(FragmentManager manager) {
        show(manager, "");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /**
         * 控制位置
         */
//        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(outMetrics);
//        int windowWidth = outMetrics.widthPixels;
//        int windowHeight = outMetrics.heightPixels;
//        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
//        double widthScale=0.83;
//        double heightScale=0.22;
//        params.width = (int) (windowWidth * widthScale); // 宽度设置为屏幕的一定比例大小
//        if (heightScale == 0) {
//            params.gravity = Gravity.CENTER;
//        } else {
//            params.gravity = Gravity.TOP;
//            params.y = (int) (windowHeight * heightScale); // 距离顶端高度设置为屏幕的一定比例大小
//        }
//        getActivity().getWindow().setAttributes(params);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        diaView = inflater.inflate(R.layout.hairparaset_layout, null);
        ls_hair = diaView.findViewById(R.id.ls_hair);

        initData();

        String tag = getTag();
        switch (tag) {
            case "6":
                HairStyleAdapter adapter = new HairStyleAdapter(getActivity(), datas1);
                ls_hair.setAdapter(adapter);
                break;
            case "7":
//                HairModeAdapter adapter2 = new HairModeAdapter(getActivity(), datas2);
//                ls_hair.setAdapter(adapter2);
                break;
            case "8":
                adapter1 = new HairModeAdapter(getActivity(), datas3);
                ls_hair.setAdapter(adapter1);
                ls_hair.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i) {
                            case 0:
                                ParamDialog dialog = new ParamDialog();
                                dialog.show(getFragmentManager(), "1");
                                break;
                            case 1:
                                ParamDialog dialog1 = new ParamDialog();
                                dialog1.show(getFragmentManager(), "2");
                                break;
                            case 2:
                                ParamDialog dialog2 = new ParamDialog();
                                dialog2.show(getFragmentManager(), "3");
                                break;
                            case 3:
                                ParamDialog dialog3 = new ParamDialog();
                                dialog3.show(getFragmentManager(), "4");
                                break;
                            case 4:
                                ParamDialog dialog4 = new ParamDialog();
                                dialog4.show(getFragmentManager(), "5");
                                break;
                            default:
                                break;

                        }
                    }
                });
                break;

            default:
                break;
        }


        builder.setView(diaView)
                .setCancelable(false);

        return builder.create();

    }

    private ArrayList<Map<String, Object>> datas1;
    private ArrayList<String> datas2;
    public static ArrayList<Map<String, Object>> datas3;

    private void initData() {
        /**
         * 初始化发型数据
         */
        datas1 = new ArrayList<Map<String, Object>>();
        datas2 = new ArrayList<>();
        datas3 = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<>();
//        ImageView iv = new ImageView(this);
//        iv.setBackgroundResource(R.drawable.add_guid);
        map.put("icon", R.drawable.add_guid);
        map.put("title", "短发");
//        map.put("content","适用于男士短发、女士短发；平头式、圆头式以及平圆式");
        datas1.add(map);

        map = new HashMap<>();
        map.put("icon", R.drawable.add_guid);
        map.put("title", "中短发");
//        map.put("content","适用于男士短发、女士短发；平头式、圆头式以及平圆式");
        datas1.add(map);

        map = new HashMap<>();
        map.put("icon", R.drawable.add_guid);
        map.put("title", "中长发");
//        map.put("content","适用于男士短发、女士短发；平头式、圆头式以及平圆式");
        datas1.add(map);

        map = new HashMap<>();
        map.put("icon", R.drawable.add_guid);
        map.put("title", "长发");
//        map.put("content","适用于男士短发、女士短发；平头式、圆头式以及平圆式");
        datas1.add(map);

        /**
         * 初始化洗发模式数据
         */

        /**
         * 初始化洗发模式
         */
        datas2.add("轻洗");
        datas2.add("标准洗");
        datas2.add("两次洗");
        datas2.add("自定义洗");

        /**
         * 初始化附加设置
         */
        Map<String, Object> map1 = new HashMap<>();
        map1.put("title", "洗涤次数");
        map1.put("value","2");
        map1.put("unit","次");
        datas3.add(map1);

        map1 = new HashMap<>();
        map1.put("title", "润湿时间");
        map1.put("value","70");
        map1.put("unit","分钟");
        datas3.add(map1);

        map1 = new HashMap<>();
        map1.put("title", "洗发液");
        map1.put("value","15");
        map1.put("unit","毫升");
        datas3.add(map1);

        map1 = new HashMap<>();
        map1.put("title", "搓揉时间");
        map1.put("value","70");
        map1.put("unit","分钟");
        datas3.add(map1);

        map1 = new HashMap<>();
        map1.put("title", "冲洗时间");
        map1.put("value","70");
        map1.put("unit","分钟");
        datas3.add(map1);


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
        //得到dialog对应的窗口
        Window dialogWindow=getDialog().getWindow();

        //去掉dialog默认的padding
        dialogWindow.getDecorView().setPadding(0,0,0,0);

        WindowManager.LayoutParams lp=dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.x=0;
        lp.y=0;
        lp.width= WindowManager.LayoutParams.MATCH_PARENT;
        lp.height= WindowManager.LayoutParams.WRAP_CONTENT;

        lp.alpha=1.0f;
        dialogWindow.setAttributes(lp);
    }
}
