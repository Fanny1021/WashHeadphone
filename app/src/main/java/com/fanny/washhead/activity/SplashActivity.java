package com.fanny.washhead.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fanny.washhead.R;
import com.fanny.washhead.view.CircleProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    private int preColor = Color.parseColor("#2c2200");//黑色——外环颜色
    private int progressColor = Color.parseColor("#6bb849");//绿色——进度条颜色
    private int CircleColor = Color.parseColor("#CCCCCC");//灰色——内环颜色
    private int textColor = Color.parseColor("#9bb879");


    @BindView(R.id.bar_progress)
    CircleProgressBar barProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


        barProgress.setTextColor(textColor).setCircleBackgroud(CircleColor)
                .setPreProgress(progressColor).setProgress(preColor)
                .setProdressWidth(10).setPaddingscale(0.8f);

        han.sendEmptyMessageDelayed(1, 100);

    }

    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            barProgress.setValue(msg.what);
            if (msg.what == 100 && m == false) {
//                pg.setCircleBackgroud(progressColor);
                Intent intent = new Intent(SplashActivity.this, GuidActivity.class);
                startActivity(intent);
                finish();
            }
            if (msg.what <100) {
                han.sendEmptyMessageDelayed(msg.what + 1, 100);
            }
        }
    };

    boolean m = false;

    @OnClick(R.id.bar_progress)
    public void onViewClicked() {
//        Toast.makeText(getBaseContext(), "$$$", Toast.LENGTH_SHORT).show();
        m = true;

        Intent intent = new Intent(SplashActivity.this, GuidActivity.class);
        startActivity(intent);
        finish();

    }
}
