package com.szdd.qianxun.sell.main.bottom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.sell.orders.detail.ServiceDetailActivity;
import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;
import com.szdd.qianxun.tools.views.slidepage.ScrollableHelper;
import com.szdd.qianxun.tools.views.xlist.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ListTestFragment extends ScrollAbleFragment implements
        ScrollableHelper.ScrollableContainer, XListView.XListViewListener {
    private XListView mListview;

    public static ListTestFragment newInstance() {
        ListTestFragment listFragment = new ListTestFragment();
        return listFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sell_main_list, container, false);
        mListview = (XListView) view.findViewById(R.id.sell_main_listview);
        List<String> strlist = new ArrayList<String>();
        for (int i = 0; i < new Random().nextInt(100) + 31; i++) {
            strlist.add(String.valueOf(i));
        }
        mListview.setXListViewListener(this);
        mListview.setPullRefreshEnable(false);
        mListview.setPullLoadEnable(true);
        mListview.setAdapter(new ListTestAdapter(getActivity(), strlist));
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ServiceDetailActivity.class);
                intent.putExtra("serviceId", 0);
                startActivity(intent);
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

