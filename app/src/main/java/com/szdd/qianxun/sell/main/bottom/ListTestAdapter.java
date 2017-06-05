package com.szdd.qianxun.sell.main.bottom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.szdd.qianxun.R;

import java.util.List;

public class ListTestAdapter extends BaseAdapter {

    private List<String> strList;
    private Context mContext;

    public ListTestAdapter(Context mContext, List<String> strList) {
        this.strList = strList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return strList.size();
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.sell_main_list_item, null, false);
//        convertView.setBackgroundColor(mContext.getResources().getColor(ColorsConstant.colors[position % ColorsConstant.colors.length]));
//        TextView tv = (TextView) convertView.findViewById(R.id.sell_main_item_tv_title);
//        tv.setText("服务" + strList.get(position));
        return convertView;
    }
}
