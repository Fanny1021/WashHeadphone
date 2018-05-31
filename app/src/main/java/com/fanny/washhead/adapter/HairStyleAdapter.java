package com.fanny.washhead.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanny.washhead.R;
import com.fanny.washhead.activity.MainActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Fanny on 18/1/10.
 */

public class HairStyleAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Map<String, Object>> datas;

    public HairStyleAdapter(Context context, ArrayList<Map<String, Object>> datas) {
        this.mInflater = LayoutInflater.from(context);
        this.datas=datas;
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
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.icon.setBackgroundResource((Integer) datas.get(i).get("icon"));
        holder.name.setText((String) datas.get(i).get("title"));

        return view;
    }

    class ViewHolder {
        public ImageView icon;
        public TextView name;
    }
}
