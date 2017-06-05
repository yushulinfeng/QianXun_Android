package com.szdd.qianxun.sell.orders;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;
import com.szdd.qianxun.tools.views.slidepage.ScrollableHelper;
import com.szdd.qianxun.tools.views.xlist.XListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SellAllOrderFragment extends ScrollAbleFragment implements
        ScrollableHelper.ScrollableContainer, XListView.XListViewListener {
    private XListView mListview;
    private BaseAdapter adapter;

    public SellAllOrderFragment() {
    }

    @SuppressLint("ValidFragment")
    public SellAllOrderFragment(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    public static SellAllOrderFragment newInstance(BaseAdapter adapter) {
        SellAllOrderFragment listFragment = new SellAllOrderFragment(adapter);
        return listFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sell_all_order_fragment, container, false);
        mListview = (XListView) view.findViewById(R.id.sell_all_order_listview);
        mListview.setXListViewListener(this);
        mListview.setPullRefreshEnable(false);
        mListview.setPullLoadEnable(true);
        mListview.setAdapter(adapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QianxunToast.showToast(getActivity(), "点击item", QianxunToast.LENGTH_SHORT);
            }
        });
        return view;
    }

    @Override
    public View getScrollableView() {
        return mListview;
    }

    @Override
    public void onRefresh() {
        // 处理表头
        mListview.stopRefresh();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        String time = sdf.format(new Date());
        mListview.setRefreshTime(time);
    }

    @Override
    public void onLoadMore() {
        mListview.stopLoadMore();
    }


}
