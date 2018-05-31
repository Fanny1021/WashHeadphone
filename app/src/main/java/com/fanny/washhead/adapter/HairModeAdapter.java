package com.fanny.washhead.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanny.washhead.R;
import com.fanny.washhead.activity.AddManualActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Fanny on 18/1/10.
 */

public class HairModeAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Map<String, Object>> datas;

    public HairModeAdapter(Context context, ArrayList<Map<String, Object>> datas) {
        this.inflater = LayoutInflater.from(context);
        this.datas = datas;
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
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.hairmode_item, null);
            viewHolder.tv_name = view.findViewById(R.id.tv_itemname);
            viewHolder.tv_value = view.findViewById(R.id.tv_value);
            viewHolder.tv_unit = view.findViewById(R.id.tv_unit);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_name.setText((String)datas.get(i).get("title"));
        viewHolder.tv_value.setText((String)datas.get(i).get("value"));
        viewHolder.tv_unit.setText((String)datas.get(i).get("unit"));
        return view;
    }


    class ViewHolder {
        public TextView tv_name;
        public TextView tv_value;
        public TextView tv_unit;
    }
}
