package com.szdd.qianxun.sell.orders.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

public class MyTempAdapter extends BaseAdapter {
    private List<String> strList;
    private Context mContext;
    private int my_layout_item;

    public MyTempAdapter(Context mContext, int my_layout_item,
                         List<String> strList) {
        this.strList = strList;
        this.mContext = mContext;
        this.my_layout_item = my_layout_item;
    }

    @Override
    public int getCount() {
        return strList.size();
    }

    @Override
    public Object getItem(int position) {
        return strList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(my_layout_item, null, false);
        return convertView;
    }
}