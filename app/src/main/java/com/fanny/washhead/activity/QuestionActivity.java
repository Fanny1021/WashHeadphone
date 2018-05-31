package com.fanny.washhead.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fanny.washhead.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class QuestionActivity extends AppCompatActivity {

    private List<Map<String, Object>> datas;
    private ListView lv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initData();

        lv_content = (ListView) findViewById(R.id.lv_question);
        QuestionAdapter adapter=new QuestionAdapter(this);
        lv_content.setAdapter(adapter);

        findViewById(R.id.im_question_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initData() {
        datas=new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<>();
        map.put("title", "01:售后服务电话是多少？");
        map.put("content", "技术支持："+"010-"+"\n"+"售后："+ "010-"+"\n");
        datas.add(map);

        map=new HashMap<String,Object>();
        map.put("title", "手机APP无法控制开机？");
        map.put("content", "原因一："+"......"+"\n"+"原因二："+"......"+"\n"+"原因三："+"......"+"\n");
        datas.add(map);

        map=new HashMap<String,Object>();
        map.put("title", "问题三");
        map.put("content", "答案三");
        datas.add(map);
    }

    class QuestionAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public QuestionAdapter(Context context) {
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
                view = mInflater.inflate(R.layout.question_layout, null);
                holder.title = view.findViewById(R.id.tv_title);
                holder.content = view.findViewById(R.id.tv_answer);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.title.setText((String) datas.get(i).get("title"));
            holder.content.setText((String) datas.get(i).get("content"));

            return view;
        }

        class ViewHolder {
            public TextView title;
            public TextView content;
        }
    }
}
