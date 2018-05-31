package com.fanny.washhead.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.fanny.washhead.R;
import com.fanny.washhead.utils.CleanCacheUtils;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout btn_clear;
    private String totalCacheSize="0";
    private ImageView im_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        initView();
    }

    private void initView() {
        btn_clear = (RelativeLayout) findViewById(R.id.rl_clear);
        im_return = (ImageView) findViewById(R.id.im_setup_return);

        btn_clear.setOnClickListener(this);
        im_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_clear:
                try {
                    totalCacheSize = CleanCacheUtils.getTotalCacheSize(getBaseContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(!totalCacheSize.contains("OK")){
                    CleanCacheUtils.clearAllCache(this);
                }else {
                    AlertDialog.Builder alert=new AlertDialog.Builder(this);
                    alert.setTitle("清除缓存");
                    alert.show();
                }
                break;
            case R.id.im_setup_return:
                finish();
                break;
        }
    }
}
