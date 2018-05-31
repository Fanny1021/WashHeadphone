package com.fanny.washhead.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SignUpCallback;
import com.fanny.washhead.R;
import com.fanny.washhead.utils.MD5Util;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscheckListener;
import cn.jpush.sms.listener.SmscodeListener;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText et_phone;
    private ImageView im_clear;
    private EditText et_pwd;
    private ImageView im_eye;
    private TextView tv_msg;
    private EditText et_msgCode;
    private String phone;
    private String pwd;
    private String smsCode;
    private Button btn_register;
    private ImageView im_return;
    String TAG = "RegisterActivity";
    String SMScode = "000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        initView();
        initData();

    }

    private boolean showPwd = true; //密码可见与否标识

    private void initData() {

        et_phone.addTextChangedListener(new TextWatcher() {
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
        et_pwd.addTextChangedListener(new TextWatcher() {
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
                pwd= MD5Util.digest(pwd);
            }
        });

        et_msgCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                smsCode = editable.toString();
                Log.e(TAG, smsCode);
            }
        });
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        im_clear = (ImageView) findViewById(R.id.im_clear);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        im_eye = (ImageView) findViewById(R.id.im_eye);
        tv_msg = (TextView) findViewById(R.id.tv_sms);
        et_msgCode = (EditText) findViewById(R.id.et_msgCode);
        btn_register = (Button) findViewById(R.id.btn_register);
        im_return = (ImageView) findViewById(R.id.im_register_return);

        et_phone.setOnClickListener(this);
        tv_msg.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        im_eye.setOnClickListener(this);
        im_clear.setOnClickListener(this);
        im_return.setOnClickListener(this);

    }


    /**
     * 获取短信验证码————极光推送
     *
     * @param phone
     * @param tempID
     * @return SMScode——验证码
     */
    public String getResultCode(String phone, String tempID) {

        SMSSDK.getInstance().getSmsCodeAsyn(phone, tempID, new SmscodeListener() {
            @Override
            public void getCodeSuccess(String s) {
                Log.e("GetSucess", s);
                SMScode = s;

            }

            @Override
            public void getCodeFail(int i, String s) {
                Log.e("GetFail", s);
                stopTimer();
            }
        });

        return SMScode;
    }

    Boolean checkResult = false;


    /**
     * 验证短信验证码是否正确
     *
     * @param phone
     * @param code
     * @return 是否验证成功boolean
     * 回调运行在UI线程
     */
    public boolean chechSmsCode(final String phone, String code) {
        SMSSDK.getInstance().checkSmsCodeAsyn(phone, code, new SmscheckListener() {
            @Override
            public void checkCodeSuccess(String s) {
                Log.e("CheckSucess", s);
                checkResult = true;
                /**
                 * 验证成功,将用户数据返回登陆界面
                 */
                Toast.makeText(RegistActivity.this, "验证成功", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("phone", phone);
                intent.putExtra("pwd", pwd);
                setResult(0x01, intent);

                /**
                 *  验证成功后用户后台存储
                 *  将用户信息进行后台保存
                 */

            }

            @Override
            public void checkCodeFail(int i, String s) {
                Log.e("CheckFail", s);
                checkResult = false;

                /**
                 * 验证失败
                 */
                Toast.makeText(RegistActivity.this, "验证失败，请重新验证!", Toast.LENGTH_SHORT).show();

            }
        });

        return checkResult;
    }

    public boolean checkPhoneNum(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        return true;
    }

    public boolean checkPwd(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return true;
    }

    Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x55:
                    Toast.makeText(RegistActivity.this, "用户已注册！", Toast.LENGTH_SHORT).show();
                    break;
                case 0x56:
                    Toast.makeText(RegistActivity.this, "用户未注册！填写验证码", Toast.LENGTH_SHORT).show();
                    break;
                case 0x54:
                    Toast.makeText(RegistActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("phone", phone);
                    intent.putExtra("pwd", pwd);
                    setResult(0x01, intent);
                    //保存用户至后台_leancloud已自动操作

                    finish();
                    break;
            }

        }
    };

    public boolean checkIfRegister(String str) {

//*********************************   自定义验证   ********************************************
//        AVQuery<AVObject> avQuery = new AVQuery<>("_User");
//        avQuery.getInBackground(str, new GetCallback<AVObject>() {
//            @Override
//            public void done(AVObject avObject, AVException e) {
//
//
//                // object 就是 id 为 558e20cbe4b060308e3eb36c 的 Todo 对象实例
//                if (e == null) {
//                    Message msg = new Message();
//                    msg.what = 0x55;
//                    han.sendMessage(msg);
//                } else {
//                    Log.e("checkRegister", e.toString());
//                    Message msg = new Message();
//                    msg.what = 0x56;
//                    han.sendMessage(msg);
//                }
//
//
//            }
//        });
//*********************************   自定义验证   ********************************************

        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_sms:
                /**
                 * 获取手机验证码
                 */

                if (checkPhoneNum(phone) && checkPwd(pwd)) {

                    //1.判断用户是否已注册
                    if (!checkIfRegister(phone)) {
                        //2.用户未注册的情况下方可注册
                        /**
                         * 1.注册控件可用
                         */
                        btn_register.setEnabled(true);
                        /**
                         * 2.1通过极光推送获取验证码
                         */
//                    getResultCode(phone, 1+"")；

                        /**
                         * 2.2 通过leanclod进行注册登陆
                         */
                        AVUser user = new AVUser();
                        user.setUsername("gmf1");
                        user.setPassword(pwd);
                        user.put("mobilePhoneNumber", phone);
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    //success
                                    Log.e(TAG, "success" + "前去验证手机号");
                                    Message msg = new Message();
                                    msg.what = 0x56;
                                    han.sendMessage(msg);
                                } else {
                                    //fail
                                    Log.e(TAG, "fail:" + e.toString());
                                    Message msg = new Message();
                                    msg.what = 0x55;
                                    han.sendMessage(msg);

                                }
                            }
                        });


                        /**
                         * 3.开始倒计时
                         */
                        startTimer();
                        /**
                         * 4.获取验证码按钮暂时60s内不可使用
                         */
                        tv_msg.setClickable(false);
                    } else {
//                        Toast.makeText(RegistActivity.this, "用户已注册", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    return;
                }

                break;
            /**
             * 注册用户
             */
            case R.id.btn_register:
                /**
                 * 验证手机验证码
                 */
                //大坑！！！！！！！！一定注意log不要用"SMS"作为tag！！！！！！！！！！
//                Log.e("SMS", phone);
//                Log.e("SMS", smsCode);
                if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(smsCode)) {
                    /**
                     * 极光验证
                     */
//                    chechSmsCode(phone, SMScode);

                    /**
                     * leancloud验证
                     */
                    AVUser.verifyMobilePhoneInBackground(smsCode, new AVMobilePhoneVerifyCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Log.e("sss", "Verified sucess!");
                                /**
                                 * 主界面跳转
                                 */
                                Message msg = new Message();
                                msg.what = 0x54;
                                han.sendMessage(msg);

                            } else {
                                Log.e("sss", "Verified failed!");
                            }
                        }
                    });
                }

                break;

            /**
             * 是否显示密码
             */
            case R.id.im_eye:
                if (showPwd) {
                    im_eye.setImageDrawable(getResources().getDrawable(R.drawable.icon_eyeopen));
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().toString().length());
                    showPwd = !showPwd;
                } else {
                    im_eye.setImageDrawable(getResources().getDrawable(R.drawable.icon_eyeclose));
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPwd = !showPwd;
                }
                break;
            case R.id.im_clear:
                if (!TextUtils.isEmpty(phone)) {
                    et_phone.setSelection(0, et_phone.getText().length());
                }
                /**
                 * 设置文本框全选
                 */
//                et_phone.setSelectAllOnFocus(true);
//                Selection.selectAll(et_phone.getText());
                et_phone.setText("");

                break;

            case R.id.et_phone:
                if (!TextUtils.isEmpty(phone)) {
                    et_phone.setSelection(0, et_phone.getText().length());
                }
                break;

            case R.id.im_register_return:

                finish();
                break;

            default:
                break;
        }
    }

    private int times;
    private TimerTask timerTask;
    private Timer timer;

    private void startTimer() {
        times = (int) (SMSSDK.getInstance().getIntervalTime() / 1000);
        tv_msg.setText(times + "s");
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
                            tv_msg.setText(times + "s");
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
        tv_msg.setText("重新获取");
        tv_msg.setClickable(true);
    }

}
