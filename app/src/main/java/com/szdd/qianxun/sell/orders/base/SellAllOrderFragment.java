package com.szdd.qianxun.sell.orders.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.base.LazyFragment;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.slidepage.ScrollableHelper;
import com.szdd.qianxun.tools.views.xlist.XListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class SellAllOrderFragment extends LazyFragment implements
        ScrollableHelper.ScrollableContainer, XListView.XListViewListener {
    private BaseAdapter adapter;
    //允许子类调用的视图
    protected View rootView;
    protected XListView mListView;
    protected LinearLayout mLayout;
    protected Context context;

    public SellAllOrderFragment() {
    }

    @SuppressLint("ValidFragment")
    public SellAllOrderFragment(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    public static SellAllOrderFragment newInstance(BaseAdapter adapter) {
        SellAllOrderFragment listFragment = new SellAllOrderFragment(adapter) {
            @Override
            public void onListLoad() {
            }

            @Override
            public void onListRefresh() {
            }

            @Override
            public void onListClick(int position) {
            }

            @Override
            public boolean onLazyLoad() {
                return true;
            }
        };
        return listFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.sell_all_order_fragment, container, false);
        mLayout = (LinearLayout) rootView.findViewById(R.id.sell_all_order_back);
        mListView = (XListView) rootView.findViewById(R.id.sell_all_order_listview);
        mListView.setXListViewListener(this);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListClick(position);
            }
        });
        updateList();
        context = rootView.getContext();
        return rootView;
    }

    protected BaseAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        mListView.setAdapter(adapter);
    }

    /**
     * 请使用此方法，代替notifyDatasetChanged
     */
    protected void updateList() {
        if (adapter == null) {
            mLayout.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            return;
        } else {
            mLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
        if (adapter.getCount() == 0) {
            mLayout.setVisibility(View.VISIBLE);
            mListView.setFooterText("", "");
        } else {
            mLayout.setVisibility(View.GONE);
            mListView.setFooterText("查看更多", "松开载入更多");
        }
        adapter.notifyDataSetChanged();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        String time = sdf.format(new Date());
        mListView.setRefreshTime(time);
    }

    @Override
    public View getScrollableView() {
        return mListView;
    }

    @Override
    public void onRefresh() {
        onListRefresh();
        updateList();
        mListView.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        onListLoad();
        updateList();
        mListView.stopLoadMore();
    }

    @Override
    protected boolean loadData() {
        return onLazyLoad();
    }

    /**
     * 上拉加载，不必刷新adapter
     */
    public abstract void onListLoad();

    /**
     * 下拉刷新，不必刷新adapter
     */
    public abstract void onListRefresh();

    /**
     * 列表点击事件
     *
     * @param position 点击位置
     */
    public abstract void onListClick(int position);

    /**
     * 延迟加载数据（仅在初次运行且界面显示时，加载一次数据）
     *
     * @return 同步加载请返回true, 异步加载请返回false，加载完成后需要手动调用loadFinish方法。
     */
    public abstract boolean onLazyLoad();

    public void showToast(String text) {
        QianxunToast.showToast(context, text, QianxunToast.LENGTH_SHORT);
    }

}
