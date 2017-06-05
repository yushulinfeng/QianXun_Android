package com.szdd.qianxun.sell.main.bottom;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.ManagerTool;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.base.LazyFragment;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.slidepage.ScrollableHelper;
import com.szdd.qianxun.tools.views.xlist.XListView;

import java.util.ArrayList;

public abstract class SellMainFragmentBase extends LazyFragment implements
        ScrollableHelper.ScrollableContainer, XListView.XListViewListener {
    private SellMainAdapterBase adapter;
    private boolean is_first_init = true;
    //允许子类调用的视图
    protected View rootView;
    protected XListView mListView;
    protected LinearLayout mLayout;
    protected TextView mTextView;
    protected ImageView mImageView;
    protected ArrayList<AnSellMainItem> item_list = null;
    protected int index = 0;
    protected int page = 1;

    public SellMainFragmentBase() {
    }

    @SuppressLint("ValidFragment")
    public SellMainFragmentBase(SellMainAdapterBase adapter) {
        this.adapter = adapter;
    }

    @SuppressLint("ValidFragment")
    public SellMainFragmentBase(int index) {
        this.index = index;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.sell_main_fragment, container, false);
        mLayout = (LinearLayout) rootView.findViewById(R.id.sell_main_back);
        mImageView = (ImageView) rootView.findViewById(R.id.sell_main_back_img);
        mTextView = (TextView) rootView.findViewById(R.id.sell_main_back_text);
        mListView = (XListView) rootView.findViewById(R.id.sell_main_listview);
        mListView.setXListViewListener(this);
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(true);
        item_list = new ArrayList<>();
        adapter = new SellMainAdapterBase(getActivity(), item_list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListClick(position - 1);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onListLongClick(position - 1);
                return true;
            }
        });
        updateList();
        return rootView;
    }

    protected BaseAdapter getAdapter() {
        return adapter;
    }

    protected void setAdapter(SellMainAdapterBase adapter) {
        this.adapter = adapter;
        mListView.setAdapter(adapter);
    }

    /**
     * 为方便后期处理，因此将此方法放到此处
     */
    protected void dealResponse(String response) {
        if (page == 1)
            item_list.clear();
        adapter.dealResponse(response);
        updateList();
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
            if (!is_first_init) {
                mImageView.setBackgroundResource(R.drawable.no_resquest_img);
                mTextView.setVisibility(View.GONE);
//                mTextView.setText("空空如也");
            } else {
                is_first_init = false;
                AnimationDrawable animationDrawable = (AnimationDrawable) mImageView
                        .getBackground();
                animationDrawable.start();
            }
            mListView.setFooterText("", "");
        } else {
            mLayout.setVisibility(View.GONE);
            mListView.setFooterText("查看更多", "松开载入更多");
        }
        adapter.notifyDataSetChanged();
        String time = StaticMethod.formatDateTime("HH:mm");
        mListView.setRefreshTime(time);
    }

    @Override
    public View getScrollableView() {
        return mListView;
    }

    @Override
    public void onRefresh() {//暂时没有用到此功能
        item_list.clear();//在这里clear
        onListRefresh();
        updateList();
        mListView.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        onListLoad();
        updateList();
        //好看
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.stopLoadMore();
            }
        }, 1000);
    }

    public void showToast(String text) {
        QianxunToast.showToast(getActivity(), text, QianxunToast.LENGTH_SHORT);
    }

    /**
     * 列表点击事件
     *
     * @param position 点击位置
     */
    public void onListClick(int position) {
        MsgPublicTool.showServiceDetail(getActivity(), item_list.get(position).getId() + "");
    }

    /**
     * 列表长按事件
     *
     * @param position 点击位置
     */
    public void onListLongClick(int position) {
        if (ManagerTool.isManagerLogin())
            ManagerTool.onListLongClick(getActivity(), position, item_list, adapter);
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
     * 下拉刷新，通过外层布局调用
     */
    public abstract void onListRefresh();

    /**
     * 延迟加载数据（仅在初次运行且界面显示时，加载一次数据）
     *
     * @return 同步加载请返回true, 异步加载请返回false，加载完成后需要手动调用loadFinish方法。
     */
    public abstract boolean onLazyLoad();

}
