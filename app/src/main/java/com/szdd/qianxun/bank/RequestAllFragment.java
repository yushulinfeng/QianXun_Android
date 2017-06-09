package com.szdd.qianxun.bank;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.tools.base.TrueLazyFragment;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.slidepage.ScrollableHelper;
import com.szdd.qianxun.tools.views.xlist.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/19 0019.
 */
public abstract class RequestAllFragment extends TrueLazyFragment implements
        ScrollableHelper.ScrollableContainer, XListView.XListViewListener {
    private BaseAdapter adapter;
    //允许子类调用的视图
    protected View rootView;
    protected XListView mListView;
    protected LinearLayout mLayout;
    // 加载需求
    protected AdapterForTabThree adapterForTabThree;
    protected List<Map<String, Object>> requestlistItems;
    protected int page = -1;
    protected int type = 0;
    protected boolean is_update=false;//防止多次刷新

    public RequestAllFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_request, container, false);
        mLayout = (LinearLayout) rootView.findViewById(R.id.type_request_back);
        mListView = (XListView) rootView.findViewById(R.id.type_request_listview);
        mListView.setXListViewListener(this);
        mListView.setPullRefreshEnable(false);//true
        mListView.setPullLoadEnable(true);
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
                onListLongClick(position-1);
                return true;
            }
        });
        updateList();
        is_update=false;
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
        if(is_update) {
            mListView.stopRefresh();
            return;
        }
        is_update=true;
        onListRefresh();
        updateList();
        mListView.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        if(is_update) {
            mListView.stopLoadMore();
            return;
        }
        is_update=true;
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
    public void onListClick(int position) {
        if (!UserStateTool.isLoginEver(getActivity())) {
            UserStateTool.goToLogin(getActivity());
            return;
        }
        Intent intent = new Intent(getActivity(),
                MainTab_03_RequestDetailActivity.class);
        intent.putExtra("requestId", (Integer) requestlistItems.get(position).get("id"));
        intent.putExtra("fatherA", "three");
        startActivity(intent);
    }

    /**
     * 列表长按事件
     *
     * @param position 点击位置
     */
    public void onListLongClick(int position) {//便于后期拓展
    }

    /**
     * 延迟加载数据（仅在初次运行且界面显示时，加载一次数据）
     *
     * @return 同步加载请返回true, 异步加载请返回false，加载完成后需要手动调用loadFinish方法。
     */
    public boolean onLazyLoad() {
        page = -1;
        requestlistItems = new ArrayList<Map<String, Object>>();
        adapterForTabThree = new AdapterForTabThree(getActivity(), requestlistItems);
        setAdapter(adapterForTabThree);
        updateDataFromNet(page, type);
        return true;
    }

    /**
     * 联网获取数据
     */
    public abstract void updateDataFromNet(int page_temp, int type_temp);

    public void showToast(String text) {
        QianxunToast.showToast(rootView.getContext(), text, QianxunToast.LENGTH_SHORT);
    }

}
