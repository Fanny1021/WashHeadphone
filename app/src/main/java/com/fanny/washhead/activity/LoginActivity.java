package com.fanny.washhead.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.fanny.washhead.R;
import com.fanny.washhead.utils.MD5Util;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.sms.SMSSDK;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_login;
    private RelativeLayout rl_clear;
    String TAG = "LoginActivity";
    private EditText et_account;
    private EditText et_login_pwd;
    private ImageView im_clear;
    private TextView tv_forget;
    private String phone;
    private String pwd;
    private String smsCode;
    private Button btn_login;

    //默认为快速登陆方式
    private boolean isNormalLogin = true;
    private EditText et_quick_phone;
    private EditText et_quick_smscode;
    private TextView tv_sms;
    private LinearLayout ll_quick;
    private LinearLayout ll_normal;
    private TextView tv_quick;
    private TextView tv_normal;
    private ImageView im_clear_quick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initData();

    }

    private void initData() {
        /**
         * 隐藏和显示登陆布局
         */
        if (isNormalLogin) {
            ll_normal.setVisibility(View.VISIBLE);
            ll_quick.setVisibility(View.GONE);
        } else {
            ll_normal.setVisibility(View.GONE);
            ll_quick.setVisibility(View.VISIBLE);
        }

        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                phone = editable.toString();
            }
        });
//        phone = et_phone.getText().toString();
//        if(!TextUtils.isEmpty(phone)){
//            et_phone.setSelection(0,et_phone.getText().length());
//        }
        et_login_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                pwd = editable.toString();
                /**
                 * 对密码进行md5加密
                 */
                pwd = MD5Util.digest(pwd);
            }
        });

        et_quick_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                phone = editable.toString();
            }
        });

        et_quick_smscode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                smsCode = editable.toString();
            }
        });
    }

    private void initView() {
        ll_quick = (LinearLayout) findViewById(R.id.ll_quickly_login);
        ll_normal = (LinearLayout) findViewById(R.id.ll_normal_login);
        tv_quick = (TextView) findViewById(R.id.tv_c_quick);
        tv_normal = (TextView) findViewById(R.id.tv_c_normal);

        /**
         * 快速登陆
         */
        et_quick_phone = (EditText) findViewById(R.id.et_login_phone_quick);
        et_quick_smscode = (EditText) findViewById(R.id.et_smscode_login);
        tv_sms = (TextView) findViewById(R.id.tv_smscode_request);
        im_clear_quick = (ImageView) findViewById(R.id.im_login_clear_quick);
        /**
         * 普通登陆
         */
        et_account = (EditText) findViewById(R.id.et_login_phone);
        et_login_pwd = (EditText) findViewById(R.id.et_pwd_login);
        /**
         * 隐藏密码
         */
        et_login_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

        im_clear = (ImageView) findViewById(R.id.im_login_clear);
        tv_forget = (TextView) findViewById(R.id.tv_forget_pwd);
        tv_login = (TextView) findViewById(R.id.tv_now_register);
        rl_clear = (RelativeLayout) findViewById(R.id.rl_login_clear);
        btn_login = (Button) findViewById(R.id.btn_login);

        im_clear.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        rl_clear.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_sms.setOnClickListener(this);
        tv_quick.setOnClickListener(this);
        tv_normal.setOnClickListener(this);
        im_clear_quick.setOnClickListener(this);


    }

    private int times;
    private TimerTask timerTask;
    private Timer timer;

    private void startTimer() {
        times = (int) (SMSSDK.getInstance().getIntervalTime() / 1000);
        tv_sms.setText(times + "s");
        /**
         * 倒计时
         */
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            times--;
                            if (times <= 0) {
                                stopTimer();
                                return;
                            }
                            tv_sms.setText(times + "s");
                        }
                    });
                }
            };
        }
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(timerTask, 1000, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        tv_sms.setText("重新获取");
        tv_sms.setClickable(true);
    }


    /**
     * 检验手机号码和邮箱格式
     *
     * @param str
     * @return
     */
    public boolean checkAccount(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        return true;
    }

    /**
     * 检验密码格式
     *
     * @param str
     * @return
     */
    public boolean checkPwd(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return true;
    }

    /**
     * 检验验证码
     *
     * @param str
     * @return
     */
    public boolean checkSmsCode(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return true;
    }

    Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    startTimer();
                    tv_sms.setClickable(false);
                    break;
                case 0x02:
                    String data = (String) msg.obj;
                    Toast.makeText(LoginActivity.this, "登陆失败" + data, Toast.LENGTH_SHORT).show();
                    break;
                case 0x03:
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    /**
                     *  更新登陆用户信息，finish界面
                     */
                    AVUser currentUser=AVUser.getCurrentUser();
                    String username = currentUser.getUsername();
                    Log.e(TAG,"当前用户："+username);

                    if(username!=null && !username.equals("")){
                        Intent intent = new Intent();
                        intent.putExtra("username", username);
                        setResult(0x23, intent);
                        finish();
                    }

                    break;
                case 0x04:
                    String data1 = (String) msg.obj;
                    Toast.makeText(LoginActivity.this, "登陆失败" + data1, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //普通登陆：
            case R.id.tv_c_normal:
                /**
                 * 字体变色
                 */
                tv_normal.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_quick.setTextColor(getResources().getColor(R.color.black_overlay));

                if (!isNormalLogin) {
                    isNormalLogin = !isNormalLogin;
                    ll_normal.setVisibility(View.VISIBLE);
                    ll_quick.setVisibility(View.GONE);
                    /**
                     * 清除快速登陆的输入框信息
                     */
                    et_quick_phone.setText("");
                    et_quick_smscode.setText("");
                }

                break;
            //快速登陆：
            case R.id.tv_c_quick:
                /**
                 * 字体变色
                 */
                tv_quick.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_normal.setTextColor(getResources().getColor(R.color.black_overlay));

                if (isNormalLogin) {
                    isNormalLogin = !isNormalLogin;
                    ll_normal.setVisibility(View.GONE);
                    ll_quick.setVisibility(View.VISIBLE);
                    /**
                     * 清除普通登陆的输入框信息
                     */
                    et_account.setText("");
                    et_login_pwd.setText("");
                }

                break;
            //前往注册
            case R.id.tv_now_register:
                Intent intentRes = new Intent(LoginActivity.this, RegistActivity.class);
                startActivityForResult(intentRes, 0x02);
                break;
            //退出登陆
            case R.id.rl_login_clear:
                finish();
                break;
            //普通登陆清除输入框
            case R.id.im_login_clear:
                if (!TextUtils.isEmpty(phone)) {
                    et_account.setSelection(0, et_account.getText().length());
                }
                et_account.setText("");
                break;
            //快速登陆清除输入框
            case R.id.im_login_clear_quick:
                if (!TextUtils.isEmpty(phone)) {
                    et_quick_phone.setSelection(0, et_quick_phone.getText().length());
                }
                et_quick_phone.setText("");
                break;
            //忘记密码
            case R.id.tv_forget_pwd:
                Toast.makeText(LoginActivity.this, "忘记密码", Toast.LENGTH_SHORT).show();


                break;
            //发送验证码
            case R.id.tv_smscode_request:

                //1.首先，调用发送登录验证码的接口：
                AVUser.requestLoginSmsCodeInBackground(phone, new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Log.e(TAG, "requestSms success!");
                            /**
                             * 发送验证码成功，UI界面开始倒计时
                             */
                            Message msg = new Message();
                            msg.what = 0x01;
                            han.sendMessage(msg);

                        } else {
                            Log.e(TAG, "requestSms fail!");
                            /**
                             * 发送验证码失败，提示ui登陆有误
                             */
                            Message msg = new Message();
                            msg.what = 0x02;
                            msg.obj = e.toString();
                            han.sendMessage(msg);
                        }
                    }
                });
                break;
            //用户登陆
            case R.id.btn_login:
                /**
                 * 用户普通登陆:手机和密码登陆
                 */
                if (isNormalLogin) {
                    if (checkAccount(phone) && checkPwd(pwd)) {
                        AVUser.loginByMobilePhoneNumberInBackground(phone, pwd, new LogInCallback<AVUser>() {
                            @Override
                            public void done(AVUser avUser, AVException e) {
                                if (e == null) {
                                    Log.e(TAG, "login success!");
                                    Message msg = new Message();
                                    msg.what = 0x03;
                                    han.sendMessage(msg);
                                } else {
                                    Log.e(TAG, "login fail!");
                                    Log.e(TAG, "普通登陆失败!" + e.toString());
                                    Message msg = new Message();
                                    msg.what = 0x04;
                                    msg.obj = e.toString();
                                    han.sendMessage(msg);
                                }
                            }
                        });
                    }else {
                        Toast.makeText(LoginActivity.this,"账号或者密码输入有误，请检查后重新输入",Toast.LENGTH_SHORT).show();
                    }
                } else {

                    /**
                     * 用户快速登陆:手机和验证码登陆
                     */
                    //1.首先，调用发送登录验证码的接口：
                    //2.然后在界面上引导用户输入收到的 6 位短信验证码：
                    if (checkSmsCode(smsCode) && checkAccount(phone)) {
                        AVUser.signUpOrLoginByMobilePhoneInBackground(phone, smsCode, new LogInCallback<AVUser>() {
                            @Override
                            public void done(AVUser avUser, AVException e) {
                                if (e == null) {
                                    Log.e(TAG, "快速登陆成功!");
                                    Message msg = new Message();
                                    msg.what = 0x03;
                                    han.sendMessage(msg);
                                } else {
                                    Log.e(TAG, "快速登陆失败!" + e.toString());
                                    Message msg = new Message();
                                    msg.what = 0x04;
                                    msg.obj = e.toString();
                                    han.sendMessage(msg);
                                }
                            }
                        });
                    }else{
                        Toast.makeText(LoginActivity.this,"账号或者验证码有误，请检查后重新输入",Toast.LENGTH_SHORT).show();
                    }

                }


                /**
                 * 用户登陆:用户名和密码登陆
                 */

//                if(checkAccount(phone) && checkPwd(pwd)){
//                    AVUser.logInInBackground(phone, pwd, new LogInCallback<AVUser>() {
//                        @Override
//                        public void done(AVUser avUser, AVException e) {
//                            if(e==null){
//                                Log.e(TAG,"login success!");
//                            }else {
//                                Log.e(TAG,"login fail!");
//                                Log.e(TAG,"login fail!:"+e.toString());
//
//                            }
//                        }
//                    });
//                }


                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * 切换为普通登陆
         */
        if(!isNormalLogin){
            isNormalLogin=!isNormalLogin;
            ll_normal.setVisibility(View.VISIBLE);
            ll_quick.setVisibility(View.GONE);
            tv_normal.setTextColor(getResources().getColor(R.color.colorAccent));
            tv_quick.setTextColor(getResources().getColor(R.color.black_overlay));

            /**
             * 清除快速登陆的输入框信息
             */
            et_quick_phone.setText("");
            et_quick_smscode.setText("");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x01) {
            phone = data.getStringExtra("phone");
            pwd = data.getStringExtra("pwd");
            Log.e(TAG, phone + ";" + pwd);

            et_account.setText(phone);
//            et_login_pwd.setText(pwd);

        }
    }
}
