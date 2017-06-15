package com.szdd.qianxun.message.info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szdd.qianxun.R;

import java.util.ArrayList;

/**
 * Created by DELL on 2016/5/17.
 */
public class MyArrayAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> arrayList;
    private LayoutInflater inflater;

    public MyArrayAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.school_list_item, null);
        ViewHolder holder = new ViewHolder();
        holder.tv_province = (TextView) convertView.findViewById(R.id.tv_province);
        holder.tv_province.setText(arrayList.get(position));
        return convertView;
    }

    class ViewHolder {
        public TextView tv_province;
    }
}
