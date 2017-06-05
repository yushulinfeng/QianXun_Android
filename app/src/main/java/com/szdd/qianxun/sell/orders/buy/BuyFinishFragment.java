package com.szdd.qianxun.sell.orders.buy;

import android.annotation.SuppressLint;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSONException;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.sell.orders.SellAllOrderBuy;
import com.szdd.qianxun.sell.orders.base.SellAllOrderFragment;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.views.QianxunToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyFinishFragment extends SellAllOrderFragment {
    private BuyFinishAdapter buyFinishAdapter;
    private List<Map<String, Object>> finishItems;
    private int page;

    public BuyFinishFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public BuyFinishFragment(BaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public void onListLoad() {
        page++;
        updateDataFromNetForFAdapter();
    }

    @Override
    public void onListRefresh() {
        page = 1;
        updateDataFromNetForFAdapter();
    }

    @Override
    public void onListClick(int position) {
    }

    @Override
    public boolean onLazyLoad() {
        page = 1;
        finishItems = new ArrayList<Map<String, Object>>();
        buyFinishAdapter = new BuyFinishAdapter(getActivity(), finishItems);
        updateDataFromNetForFAdapter();
        return true;
    }

    //连接后台获取服务的详细信息
    private void updateDataFromNetForFAdapter() {
        StaticMethod.POST(getActivity(), SellAllOrderBuy.URL,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        return dialog;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("userId", InfoTool.getUserID(getActivity()));
                        list.put("page", "" + page);
                        list.put("status", "4,5");
                        return list;
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response == null || response.equals("")) {
                            showToast("网络错误");
                            return;
                        }
                        dealResponse(response);
                    }
                });
    }

    //处理后台发送来的数据
    private void dealResponse(String response) {
        if (response.equals("failed")) {
        } else {
            try {
                if (page == 1) finishItems.clear();
                SellAllOrderBuy.Analysis(getActivity(), response, finishItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setAdapter(buyFinishAdapter);
        updateList();
    }

}
