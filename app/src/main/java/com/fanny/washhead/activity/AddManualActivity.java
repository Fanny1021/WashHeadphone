package com.fanny.washhead.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fanny.washhead.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class AddManualActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView im_return;
    private ListView list;
    private AutoCompleteTextView tv_search;
    private ArrayList<String> datas;
    private ProductAdapter listAdapter;
    private String[] arr;
    private ArrayAdapter<String> searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manual);
        initData();
        initView();

    }
    private void initData() {
        datas = new ArrayList<String>();
        datas.add("GH-AWH-01");
        datas.add("GH-AWH-02");
        datas.add("GH-AWH-03");
        datas.add("GH-AWH-04");
        datas.add("GH-AWH-05");
        datas.add("GH-AWH-06");
        datas.add("GH-AWH-07");
        arr = new String[]{"GH-AWH-11","GH-AWH-21","GH-AWH-31","GH-AWH-41","GH-AWH-51"};
    }

    private void initView() {
        im_return = (ImageView) findViewById(R.id.im_AddManual_return);
        list = (ListView) findViewById(R.id.lv_product);
        tv_search = (AutoCompleteTextView) findViewById(R.id.autv_search);

        listAdapter = new ProductAdapter(this);
        list.setAdapter(listAdapter);

        searchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arr);
        tv_search.setAdapter(searchAdapter);

        im_return.setOnClickListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(AddManualActivity.this,ConnectActivity.class);
                Bundle data=new Bundle();
                data.putString("name",datas.get(i));
                intent.putExtra("value",data);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.im_AddManual_return:
                finish();
                break;
        }

    }

    class ProductAdapter extends BaseAdapter{
        private LayoutInflater inflater;

        public ProductAdapter(Context context){
            this.inflater=LayoutInflater.from(context);
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
            ViewHolder viewHolder=null;
            if(view==null){
                viewHolder=new ViewHolder();
                view=inflater.inflate(R.layout.product_layout,null);
                viewHolder.tv_name=view.findViewById(R.id.tv_search_name);
                view.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) view.getTag();
            }

            viewHolder.tv_name.setText(datas.get(i));
            return view;
        }
    }

    class ViewHolder {
        public TextView tv_name;
    }

}
