package com.fanny.washhead.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fanny.washhead.R;
import com.fanny.washhead.view.RoundedRectProgressBar;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.fanny.washhead.activity.MainActivity.datas;

public class ConnectActivity extends AppCompatActivity {

    private RoundedRectProgressBar bar;
    private ImageView im_return;
    private Button btn_con;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Intent intent=getIntent();
        Bundle data = intent.getBundleExtra("value");
        name = data.getString("name");


        bar = (RoundedRectProgressBar) findViewById(R.id.con_progressbar);
        reset();

        im_return = (ImageView) findViewById(R.id.im_con_return);
        im_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_con = (Button) findViewById(R.id.btn_reCon);
        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 取消连接操作
                 */

                /**
                 *  重新连接操作
                 */
            }
        });

    }

    Handler han=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x01){
                Map<String, Object> map = new HashMap<>();
                map.put("icon", R.drawable.add_guid);
                map.put("id", name);
                map.put("online",false);
                datas.add(map);

                finish();
            }
        }
    };

    /**
     * 模拟进度条：从头到尾跑一次
     * 使用timertask
     */
    int progress;
    private Timer timer;

    private void reset() {
        progress = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                bar.setProgress(progress);
                progress++;
                if (progress > 100) {
                    timer.cancel();
                    han.sendEmptyMessage(0x01);
                }
            }
        }, 0, 30);
    }
}
