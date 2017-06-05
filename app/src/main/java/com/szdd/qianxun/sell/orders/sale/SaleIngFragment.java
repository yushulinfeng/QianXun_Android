package com.szdd.qianxun.sell.orders.sale;

import android.annotation.SuppressLint;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSONException;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.sell.orders.SellAllOrderSell;
import com.szdd.qianxun.sell.orders.base.SellAllOrderFragment;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SaleIngFragment extends SellAllOrderFragment {
    private SaleIngAdapter saleIngAdapter;
    private List<Map<String, Object>> ingItems;
    private int page;

    public SaleIngFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public SaleIngFragment(BaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public void onListLoad() {
        page++;
        updateDataFromNetForCAdapter();
    }

    @Override
    public void onListRefresh() {
        page = 1;
        updateDataFromNetForCAdapter();
    }

    @Override
    public void onListClick(int position) {
    }

    @Override
    public boolean onLazyLoad() {
        page = 1;
        ingItems = new ArrayList<Map<String, Object>>();
        saleIngAdapter = new SaleIngAdapter(getActivity(), ingItems);
        updateDataFromNetForCAdapter();
        return true;
    }

    //连接后台获取服务的详细信息
    private void updateDataFromNetForCAdapter() {
        StaticMethod.POST(getActivity(), SellAllOrderSell.URL,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        return dialog;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("userId", InfoTool.getUserID(getActivity()));
                        //list.put("userId", ""+1);
                        list.put("page", "" + page);
                        list.put("status", "" + 2);
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
                if (page == 1) ingItems.clear();
                SellAllOrderSell.Analysis(getActivity(), response, ingItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setAdapter(saleIngAdapter);
        updateList();
    }

}
