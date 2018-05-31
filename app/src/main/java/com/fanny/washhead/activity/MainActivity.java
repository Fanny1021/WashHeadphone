package com.fanny.washhead.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.fanny.washhead.R;
import com.fanny.washhead.fragment.SocketConDialog;
import com.fanny.washhead.utils.SocketUtil;
import com.google.zxing.client.android.CaptureActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SocketConDialog.CallBack2 {

    //全局：当前用户
    public static String username;

    private String TAG = "MainActivity";
    public static boolean isLogin = false;

    private ActionBarDrawerToggle mDrawerToogle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private RelativeLayout ll_wash;
    private RelativeLayout ll_community;
    private RelativeLayout ll_question;
    private RelativeLayout ll_setup;
    private Button btn_login;
    private Button btn_add;
    public static ArrayList<Map<String, Object>> datas;
    private ListView ls;
    //    private AnimationDrawable animationDrawable;

    private AddProductAdapter adapter;
    private LinearLayout mainLayout;
    private LinearLayout ll_unlogin;
    private LinearLayout ll_login;
    private TextView tv_username;
    private Button btn_logout;
    private LinearLayout ll_online_machine;
    private TextView online_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        /**
//         * 使用eventbus接受数据
//         */
//        EventBus.getDefault().register(this);


        // 测试 SDK 是否正常工作的代码
        AVObject testObject = new AVObject("TestObject");
        testObject.put("words", "Hello World!");
        testObject.put("name","hahaha");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.e("saved", "success!");
                }
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        findViewById(R.id.im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        /**
         * 测试向leancloud发送http请求
         * 拉取服务器端数据
         */
        findViewById(R.id.im_email_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AVQuery<AVObject> avQuery=new AVQuery<AVObject>("User");
                AVObject todo = AVObject.createWithoutData("_File", "5aa0e0639f5454007128af59");
                todo.fetchInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        String name = avObject.getString("name");
                        String url = avObject.getString("url");

//                        String phone=avObject.getString("password");
                        Log.e("FetchMsg", name + ";" + url);

                    }
                });
            }
        });


        initData();
        initView();

        /**
         * 检查最后用户是否在线
         */
        if (AVUser.getCurrentUser() != null) {
            isLogin = true;
            username = AVUser.getCurrentUser().getUsername();
            if (username != null) {
                Log.e(TAG, username);
                tv_username.setText(username);
            }
        }

    }

    private void initData() {
        datas = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<>();
//        ImageView iv = new ImageView(this);
//        iv.setBackgroundResource(R.drawable.add_guid);
        map.put("icon", R.drawable.add_guid);
        map.put("id", "GH-WH-01");
        map.put("online", false);
        datas.add(map);

        map = new HashMap<>();
        map.put("icon", R.drawable.add_guid);
        map.put("id", "GH-WH-02");
        map.put("online", false);
        datas.add(map);

        map = new HashMap<>();
        map.put("icon", R.drawable.add_guid);
        map.put("id", "GH-WH-03");
        map.put("online", false);
        datas.add(map);
    }

    private void initView() {
        ll_wash = (RelativeLayout) findViewById(R.id.ll_wash);
        ll_community = (RelativeLayout) findViewById(R.id.ll_community);
        ll_question = (RelativeLayout) findViewById(R.id.ll_question);
        ll_setup = (RelativeLayout) findViewById(R.id.ll_setup);
        btn_add = (Button) findViewById(R.id.btn_add);

        ll_unlogin = (LinearLayout) findViewById(R.id.drawer_unlogin);
        btn_login = (Button) findViewById(R.id.btn_menu_login);
        ll_login = (LinearLayout) findViewById(R.id.drawer_user_msg);
        tv_username = (TextView) findViewById(R.id.tv_username);
        btn_logout = (Button) findViewById(R.id.btn_menu_logout);

        ll_online_machine = (LinearLayout) findViewById(R.id.ll_online_machine);
        ll_online_machine.setOnClickListener(this);
        online_name = (TextView) findViewById(R.id.tv_online_name);


        ll_wash.setOnClickListener(this);
        ll_community.setOnClickListener(this);
        ll_question.setOnClickListener(this);
        ll_setup.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

        ls = (ListView) findViewById(R.id.ls_add);
        adapter = new AddProductAdapter(this);
        ls.setAdapter(adapter);

        /**
         * 点击已添加设备逻辑
         */
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /**
                 * 获取当前设备是否在线
                 */
                boolean b = (boolean) datas.get(i).get("online");
                String str = (String) datas.get(i).get("id");

                Log.e(TAG, "设备online：" + b);
                Log.e(TAG, "设备id：" + str);

                if (b == true) {
                    online_name.setText(str);
                    Intent intent0 = new Intent(MainActivity.this, WashHeadActivity.class);
                    Bundle data = new Bundle();
                    data.putString("id", str);
                    intent0.putExtra("data", data);
                    startActivity(intent0);
                } else {
                    /**
                     * 建立socket,连接设备,绑定设备进行上线
                     */
                    SocketConDialog socketConDialog = new SocketConDialog();
                    socketConDialog.show(getSupportFragmentManager(), str);

                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        /**
         * 刷新抽屉菜单用户布局
         */
        if (isLogin == true) {
            ll_login.setVisibility(View.VISIBLE);
            ll_unlogin.setVisibility(View.GONE);
        } else {
            ll_unlogin.setVisibility(View.VISIBLE);
            ll_login.setVisibility(View.GONE);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x23) {
            username = data.getStringExtra("username");
            Log.e(TAG, "当前用户：" + username);

            tv_username.setText(username);
            isLogin = true;

        } else {
            isLogin = false;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_online_machine:
                //在线设备
                String str = online_name.getText().toString();
                if (str.contains("GH")) {
                    Intent intent0 = new Intent(MainActivity.this, WashHeadActivity.class);
                    Bundle data = new Bundle();
                    data.putString("id", str);
                    intent0.putExtra("data", data);
                    startActivity(intent0);
                } else {
                    Toast.makeText(MainActivity.this, "请先连接设备", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btn_menu_login:
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent2, 0x22);
                break;
            case R.id.btn_menu_logout:
                AVUser currentUser = AVUser.getCurrentUser();
                if (currentUser != null) {
                    //清除后台当前缓存用户对象
                    AVUser.logOut();
                    AVUser currentUser1 = AVUser.getCurrentUser();
                    if (currentUser1 == null) {
                        isLogin = false;
                        ll_unlogin.setVisibility(View.VISIBLE);
                        ll_login.setVisibility(View.GONE);
                    }

                }
                break;

            case R.id.ll_wash:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.ll_community:
                String userContact = "15701695175";
                FeedbackAPI.setDefaultUserContactInfo(userContact);
                FeedbackAPI.openFeedbackActivity();
                break;
            case R.id.ll_question:
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_setup:
                Intent intent1 = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_add:
                Intent intent3 = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(intent3);
                break;
        }
    }



    class AddProductAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public AddProductAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;

            if (view == null) {
                holder = new ViewHolder();
                view = mInflater.inflate(R.layout.addproduct_layout, null);
                holder.icon = view.findViewById(R.id.im_addpro);
                holder.name = view.findViewById(R.id.tv_addpro);
                holder.con_flag = view.findViewById(R.id.im_flag_con);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.icon.setBackgroundResource((Integer) datas.get(i).get("icon"));
            holder.name.setText((String) datas.get(i).get("id"));
            boolean b = (boolean) datas.get(i).get("online");
            if (b == false) {
//                holder.con_flag.setBackgroundResource(R.mipmap.icon_uncon);
                holder.con_flag.setImageResource(R.mipmap.icon_uncon);
            } else if (b == true) {
//                holder.con_flag.setBackgroundResource(R.mipmap.icon_con);
                holder.con_flag.setImageResource(R.mipmap.icon_con);
            }

            return view;
        }

        class ViewHolder {
            public ImageView icon;
            public TextView name;
            public ImageView con_flag;
        }
    }


    /**
     * 回掉设备连接绑定的数据接口
     *
     * @param str
     */
    @Override
    public void onClick(String str) {
        /**
         * 设备已连接
         */
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).get("id").equals(str)) {
                datas.get(i).put("online", true);
            }
        }
        online_name.setText(str);
        adapter.notifyDataSetChanged();

//        listenSocket();
    }

//    /**
//     * 监听全局接受输入in数据流
//     */
//    private boolean receiveMsg = true;
//    public void listenSocket() {
//        if (SocketUtil.socket != null) {
//            if (SocketUtil.socket.isConnected()) {
//                /**
//                 * 接收服务器数据，保持监听
//                 */
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        InputStream in = SocketUtil.getInputStream();
//                        while (receiveMsg) {
//                            String RecMsg = "服务器返回消息";
//                            if (in != null) {
//                                try {
//                                    byte buffer[] = new byte[1024 * 4];
//                                    int temp = 0;
//                                    while ((temp = in.read(buffer)) != -1) {
//                                        Log.e(TAG, "temp:" + temp + " ");
//                                        RecMsg = new String(buffer, 0, temp);
//                                        Log.e(TAG, "RecMsg:" + RecMsg + " ");
//                                        /**
//                                         * 将服务器发送来的数据event下去,发送给icactivitty
//                                         */
//                                        Message message = new Message();
//                                        message.what = 0x01;
//                                        message.obj = RecMsg;
////                                        EventBus.getDefault().post(RecMsg);
//                                    }
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//
//                            }
//                        }
//                    }
//                }).start();
//            }
//        }
//    }
}
