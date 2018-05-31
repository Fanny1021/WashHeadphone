package com.fanny.washhead.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fanny.washhead.R;
import com.fanny.washhead.fragment.AddSetDialog;
import com.fanny.washhead.utils.Constant;
import com.fanny.washhead.utils.SocketManagerUtil;
import com.fanny.washhead.utils.SocketUtil;
import com.fanny.washhead.view.HairParamDialog;
import com.fanny.washhead.view.ParamDialog;
import com.fanny.washhead.view.ToogleButton;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class WashHeadActivity extends AppCompatActivity implements ParamDialog.CallBack, AddSetDialog.CallBack1, View.OnClickListener {
    String TAG = "WashHeadActivity";
    private TextView tv_hairStyle;
    private TextView tv_hairMode;
    private ImageView im_set;
    private ImageView im_back;
//    private ToogleButton toogleButton;

    private char[] sendData;
    private byte[] sentbuff = new byte[16];
    private TextView tv_id;
    private Spinner spinner_mode;
    private Spinner spinner_water;
    private String[] modeItems;
    private Spinner spinner_hair;
    private ArrayList<String> lists;
    private ArrayList<String> lists1;
    private LinearLayout ll_set;
    private LinearLayout ll_power;
    private LinearLayout ll_start;
    private TextView tv_pause;
    private Button btnExit;
    private TextView tvTime;
    private ImageView imWarn;
    private ImageView imWet;
    private ImageView imLiquid;
    private ImageView imKnead;
    private ImageView imWash;
    private ImageView imDry;
    private ImageView imFinish;
    private ImageView imDisinfect;
    private String id;
    private ArrayList list;

    private boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wash_head);

        /**
         * 第一次加载，控制spinner的默认加载
         * 在电源开关，开机检测成功后设置为false
         */
        isFirst=true;

        /**
         * 洗头机设备名
         */
        tv_id = (TextView) findViewById(R.id.tv_head_name);
        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra("data");
        id = data.getString("id");
        if (id != null) {
            tv_id.setText(id);
        }

        /**
         * 水温
         */
        spinner_water = (Spinner) findViewById(R.id.spinner_water);
        String[] stringArray = getResources().getStringArray(R.array.water_temp);
        list = new ArrayList();
        for (int i = 0; i < stringArray.length; i++) {
            list.add(stringArray[i]);
        }
        ArrayAdapter arrayAdapter0 = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner_water.setAdapter(arrayAdapter0);

        spinner_water.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(isFirst){
                    return;
                }

                mwaterTemp = (String) list.get(i);
                /**
                 * 解析水温数据［31，41］
                 */
                String a = mwaterTemp.substring(0, 1);//高位
                String b = mwaterTemp.substring(1);//低位
                int m = Integer.parseInt(a, 16);
                int n = Integer.parseInt(b, 16);
                Log.e(TAG, m + "");
                Log.e(TAG, n + "");

                sendData[10] = 'S';
                sendData[11] = 'T';
                sendData[12] = (char) m;
                sendData[13] = (char) n;
                sendData[14] = 0x00;

                /**
                 * 发送数据至下位机
                 */
                if (!SocketUtil.socket.isConnected() || SocketUtil.connectStaus != 1) {
                    Toast.makeText(WashHeadActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketUtil.SendData(sendData);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//                Toast.makeText(WashHeadActivity.this, "weixuanzhong", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 洗发模式
         */
        spinner_mode = (Spinner) findViewById(R.id.spinner_mode);
        modeItems = getResources().getStringArray(R.array.wash_mode);
        lists = new ArrayList<>();
        lists.add("快洗");
        lists.add("慢洗");
        lists.add("自定义洗");
//        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.item_select,lists);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, lists);
        spinner_mode.setAdapter(arrayAdapter);
        spinner_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(isFirst){
                    return;
                }

                tag = lists.get(i);
                switch (tag) {
                    case "快洗":
                        sendData[10] = 'L';
                        sendData[11] = 'A';
                        sendData[12] = 0x00;
                        sendData[13] = 0x00;
                        sendData[14] = 0x00;

                        /**
                         * 发送数据至下位机
                         */
                        if (!SocketUtil.socket.isConnected() || SocketUtil.connectStaus != 1) {
                            Toast.makeText(WashHeadActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SocketUtil.SendData(sendData);
                        break;
                    case "慢洗":
                        sendData[10] = 'L';
                        sendData[11] = 'B';
                        sendData[12] = 0x00;
                        sendData[13] = 0x00;
                        sendData[14] = 0x00;

                        /**
                         * 发送数据至下位机
                         */
                        if (!SocketUtil.socket.isConnected() || SocketUtil.connectStaus != 1) {
                            Toast.makeText(WashHeadActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SocketUtil.SendData(sendData);
                        break;
                    case "自定义洗":
                        sendData[10] = 'L';
                        sendData[11] = 'C';
                        sendData[12] = 0x00;
                        sendData[13] = 0x00;
                        sendData[14] = 0x00;

                        /**
                         * 发送数据至下位机
                         */
                        if (!SocketUtil.socket.isConnected() || SocketUtil.connectStaus != 1) {
                            Toast.makeText(WashHeadActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SocketUtil.SendData(sendData);

                        break;
                    default:

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//                Toast.makeText(WashHeadActivity.this, "weixuanzhong", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 发型选择
         */
        spinner_hair = (Spinner) findViewById(R.id.spinner_hair);
        lists1 = new ArrayList<>();
        lists1.add("长发");
        lists1.add("短发");
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, lists1);
        spinner_hair.setAdapter(arrayAdapter1);
        spinner_hair.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(WashHeadActivity.this, "xuanzhong" + lists1.get(i), Toast.LENGTH_SHORT).show();

                if(isFirst){
                    return;
                }

                if (i == 0) {
                    sendData[10] = 'L';
                    sendData[11] = 'L';
                    sendData[12] = 0x00;
                    sendData[13] = 0x00;
                    sendData[14] = 0x00;

                    /**
                     * 发送数据至下位机
                     */
                    if (!SocketUtil.socket.isConnected() || SocketUtil.connectStaus != 1) {
                        Toast.makeText(WashHeadActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SocketUtil.SendData(sendData);
                }
                if (i == 1) {
                    sendData[10] = 'L';
                    sendData[11] = 'S';
                    sendData[12] = 0x00;
                    sendData[13] = 0x00;
                    sendData[14] = 0x00;

                    /**
                     * 发送数据至下位机
                     */
                    if (!SocketUtil.socket.isConnected() || SocketUtil.connectStaus != 1) {
                        Toast.makeText(WashHeadActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SocketUtil.SendData(sendData);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//                Toast.makeText(WashHeadActivity.this, "weixuanzhong", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 当前进度
         * 1.时间、中途停止、报警
         * 2.洗头进度
         */
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(this);
        tvTime = (TextView) findViewById(R.id.tv_wash_time);
        imWarn = (ImageView) findViewById(R.id.im_warn_msg);
        imWarn.setOnClickListener(this);

        imWet = (ImageView) findViewById(R.id.im_wet);
        imLiquid = (ImageView) findViewById(R.id.im_liquid);
        imKnead = (ImageView) findViewById(R.id.im_knead);
        imWash = (ImageView) findViewById(R.id.im_wash);
        imDry = (ImageView) findViewById(R.id.im_dry);
        imFinish = (ImageView) findViewById(R.id.im_finish);
        imDisinfect = (ImageView) findViewById(R.id.im_disinfect);


        /**
         * 附加设置
         */
        ll_set = (LinearLayout) findViewById(R.id.ll_wh_set);
        ll_set.setOnClickListener(this);

        /**
         * 电源按钮点击
         * 控制开启设备和急停
         */
        ll_power = (LinearLayout) findViewById(R.id.ll_power);
        ll_power.setOnClickListener(this);


        /**
         * 启动按钮点击
         * 控制设备（快洗／慢洗／自定义洗）启动和暂停
         */
        ll_start = (LinearLayout) findViewById(R.id.ll_start);
        tv_pause = (TextView) findViewById(R.id.tv_start_pause);
        ll_start.setOnClickListener(this);


        /**
         * 退出页面
         */
        im_back = (ImageView) findViewById(R.id.im_washhead_back);
        im_back.setOnClickListener(this);

//        toogleButton = (ToogleButton) findViewById(R.id.toogle_mode);
//        toogleButton.setState(true);
//        toogleButton.setOnToogleButtonStateChangedListener(new ToogleButton.onToogleButtonStateChangedListener() {
//            @Override
//            public void onStateChanged(boolean b) {
//                Toast.makeText(getApplicationContext(), "状态发生了变化" + b, Toast.LENGTH_SHORT).show();
//            }
//        });

        initSocketData();

    }

    private void initSocketData() {
        sendData = new char[23];

        sendData[0] = 0xEA;
        sendData[1] = 0xEB;
        sendData[2] = 11;
        sendData[3] = 00;
        sendData[4] = 10;
        sendData[5] = 01;
        sendData[6] = 02;
        sendData[7] = 0x00;
        sendData[8] = 0x0A;
        sendData[9] = '$';
        sendData[10] = 'A';
        sendData[11] = 'A';
        sendData[12] = 0;
        sendData[13] = 0;
        sendData[14] = 0;
        sendData[15] = 0;
        sendData[16] = 0;
        sendData[17] = 0;
        sendData[18] = 0;
        sendData[19] = '#';
        sendData[20] = 0x55;
        sendData[21] = 0xE5;
        sendData[22] = 0xD4;

    }

    /**
     * 参数设置的接口回调
     * 从paramdialog获取到传递过来的设置数据
     *
     * @param Result
     */
    @Override
    public void onClick(String Result, String title) {
        Toast.makeText(WashHeadActivity.this, "设置参数为：" + Result, Toast.LENGTH_SHORT).show();
        /**
         * 通过tag找到addsetdialog，将回调的数据传递过去
         */
        AddSetDialog dialog = (AddSetDialog) getSupportFragmentManager().findFragmentByTag(tag);
        int m = 0;
        if (dialog != null) {
            switch (title) {
                case "润湿时间设定":
                    dialog.setTvSetWetting(Result);
                    /**
                     * 发送润湿时间数据至下位机
                     */
                    /**
                     * 解析数据
                     */
                    m = Integer.parseInt(Result, 16);
                    Log.e(TAG, m + "");

                    sendData[10] = 'S';
                    sendData[11] = 'W';
                    sendData[12] = 0x00;
                    sendData[13] = (char) m;
                    sendData[14] = 0x00;

                    /**
                     * 发送数据至下位机
                     */
                    if (!SocketUtil.socket.isConnected() || SocketUtil.connectStaus != 1) {
                        Toast.makeText(WashHeadActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SocketUtil.SendData(sendData);
                    break;
                case "洗涤次数设定":
                    dialog.setTvSetLiquidNum(Result);
                    /**
                     * 发送洗涤次数数据至下位机
                     */
                    /**
                     * 解析数据
                     */
                    m = Integer.parseInt(Result, 16);
                    Log.e(TAG, m + "");

                    sendData[10] = 'S';
                    sendData[11] = 'S';
                    sendData[12] = 0x00;
                    sendData[13] = (char) m;
                    sendData[14] = 0x00;

                    /**
                     * 发送数据至下位机
                     */
                    if (!SocketUtil.socket.isConnected() || SocketUtil.connectStaus != 1) {
                        Toast.makeText(WashHeadActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SocketUtil.SendData(sendData);

                    break;
                case "搓柔时间设定":
                    dialog.setTvSetKnead(Result);
                    /**
                     * 发送搓揉时间数据至下位机
                     */

                    /**
                     * 解析数据
                     */
                    m = Integer.parseInt(Result, 16);
                    Log.e(TAG, m + "");

                    sendData[10] = 'S';
                    sendData[11] = 'M';
                    sendData[12] = 0x00;
                    sendData[13] = (char) m;
                    sendData[14] = 0x00;

                    /**
                     * 发送数据至下位机
                     */
                    if (!SocketUtil.socket.isConnected() || SocketUtil.connectStaus != 1) {
                        Toast.makeText(WashHeadActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SocketUtil.SendData(sendData);
                    break;
                case "冲洗时间设定":
                    dialog.setTvSetWashing(Result);
                    /**
                     * 发送冲洗时间数据至下位机
                     */
                    /**
                     * 解析数据
                     */
                    m = Integer.parseInt(Result, 16);
                    Log.e(TAG, m + "");

                    sendData[10] = 'S';
                    sendData[11] = 'F';
                    sendData[12] = 0x00;
                    sendData[13] = (char) m;
                    sendData[14] = 0x00;

                    /**
                     * 发送数据至下位机
                     */
                    if (!SocketUtil.socket.isConnected() || SocketUtil.connectStaus != 1) {
                        Toast.makeText(WashHeadActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SocketUtil.SendData(sendData);
                    break;
                case "风干时间设定":
                    dialog.setTvSetDrying(Result);
                    /**
                     * 发送风干时间数据至下位机
                     */
                    /**
                     * 解析数据
                     */
                    m = Integer.parseInt(Result, 16);
                    Log.e(TAG, m + "");

                    sendData[10] = 'S';
                    sendData[11] = 'P';
                    sendData[12] = 0x00;
                    sendData[13] = (char) m;
                    sendData[14] = 0x00;

                    /**
                     * 发送数据至下位机
                     */
                    if (!SocketUtil.socket.isConnected() || SocketUtil.connectStaus != 1) {
                        Toast.makeText(WashHeadActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SocketUtil.SendData(sendData);

                    break;
                default:
                    break;
            }

        } else {
            Log.e(TAG, "kong");
        }
    }

    String tag = "";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_hairStyle:
//                HairParamDialog dialog = new HairParamDialog();
//                dialog.show(getFragmentManager(), "6");
//                break;
//            case R.id.tv_hairMode:
//                HairParamDialog dialog1 = new HairParamDialog();
//                dialog1.show(getFragmentManager(), "7");
//                break;
            case R.id.ll_wh_set:
//                HairParamDialog dialog2 = new HairParamDialog();
//                dialog2.show(getFragmentManager(), "8");

                AddSetDialog dialog = new AddSetDialog();
                dialog.show(getSupportFragmentManager(), tag);

                break;
            case R.id.btn_exit:

                break;
            case R.id.im_warn_msg:
                Toast.makeText(WashHeadActivity.this, "报警信息", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_power:
                isFirst=false;
                Log.e(TAG,"电源打开");
                break;
            case R.id.ll_start:
//                imWet.setImageResource(R.mipmap.icon_rol_green);

                /**
                 * 发送数据
                 */
                if (!SocketUtil.socket.isConnected() || SocketUtil.connectStaus != 1) {
                    Toast.makeText(WashHeadActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                    return;
                }

                SocketUtil.SendData(sendData);

                break;
            case R.id.im_washhead_back:
                finish();
                break;
            default:
                break;
        }
    }

    private boolean checkWaterTemp(String str) {
        if (str.length() <= 0) {
            return false;
        }
        return true;
    }


    /**
     * 回调接口，获取附加设置的数据
     * 从addsetdialog回调
     *
     * @param wetValue
     * @param liquidNum
     * @param kneadValue
     * @param washTime
     * @param dryTime
     */
    String mwetValue;
    String mliquidNum;
    String mkneadValue;
    String mwashTime;
    String mdryTime;

    String mwaterTemp;

    @Override
    public void onClick(String wetValue, String liquidNum, String kneadValue, String washTime, String dryTime) {
        Log.e(TAG, washTime + ";" + liquidNum + ";" + kneadValue + ";" + washTime + ";" + dryTime);
        mwetValue = wetValue;
        mliquidNum = liquidNum;
        mkneadValue = kneadValue;
        mwashTime = washTime;
        mdryTime = dryTime;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (SocketUtil.getInputStream() != null) {
                SocketUtil.getInputStream().close();
                receiveMsg = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.TcpMsg_MSG:
                    Log.e(TAG, "msg");
                    break;
                case Constant.TcpMsg_UNEXPECTED_CLOSE:
                    Log.e(TAG, "unexpectedClosed");
                    break;
                case Constant.TcpMsg_CLIENT_CLOSE_OK:
                    Log.e(TAG, "clientCloseOK");
                    break;
                case Constant.TcpMsg_CLIENT_CLOSE_ERR:
                    Log.e(TAG, "clientCloseERR");
                    break;

            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (SocketUtil.socket != null) {
            //isConnected,isClosed等仅能判断本地socket状态
            if (SocketUtil.socket.isConnected()) {
                listenSocket();
            } else {
                Toast.makeText(WashHeadActivity.this, "网络未连接notconnected", Toast.LENGTH_SHORT).show();
            }
            /**
             * 监听长连接回馈结果
             */
//                    try {
//                        SocketManagerUtil socketManagerUtil=new SocketManagerUtil(SocketUtil.socket,handler);
//                        socketManagerUtil.start();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

        } else {
            Toast.makeText(WashHeadActivity.this, "网络未连接null", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 监听接受输入in数据流
     */
    private boolean receiveMsg = true;

    public void listenSocket() {
        if (SocketUtil.socket != null) {
            if (SocketUtil.socket.isConnected()) {
                /**
                 * 接收服务器数据，保持监听
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InputStream in = SocketUtil.getInputStream();
                        while (receiveMsg) {
                            String RecMsg = "服务器返回消息";
                            if (in != null) {
                                try {
                                    byte buffer[] = new byte[1024 * 4];
                                    int temp = 0;
                                    while ((temp = in.read(buffer)) != -1) {
                                        Log.e(TAG + ":" + id, "temp:" + temp + " ");
                                        RecMsg = new String(buffer, 0, temp);
                                        Log.e(TAG + ":" + id, "RecMsg:" + RecMsg + " ");
                                        /**
                                         * 将服务器发送来的数据event下去,发送给icactivitty
                                         */
                                        Message message = new Message();
                                        message.what = 0x01;
                                        message.obj = RecMsg;
//                                        EventBus.getDefault().post(RecMsg);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }
                        }
                    }
                }).start();
            }
        }
    }
}
