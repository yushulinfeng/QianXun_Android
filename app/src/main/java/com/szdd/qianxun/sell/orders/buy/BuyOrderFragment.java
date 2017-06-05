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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyOrderFragment extends SellAllOrderFragment {
    private BuyOrderAdapter buyOrderAdapter;
    private List<Map<String, Object>> orderItems;
    private int page;

    public BuyOrderFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public BuyOrderFragment(BaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public void onListLoad() {
        page++;
        updateDataFromNetForOAdapter();
    }

    @Override
    public void onListRefresh() {
        page = 1;
        updateDataFromNetForOAdapter();
    }


    @Override
    public void onListClick(int position) {
        showToast("可以adapter");
    }

    @Override
    public boolean onLazyLoad() {
        page = 1;
        orderItems = new ArrayList<Map<String, Object>>();
        buyOrderAdapter = new BuyOrderAdapter(getActivity(), orderItems);
        updateDataFromNetForOAdapter();
        return true;
    }

    //连接后台获取服务的详细信息
    private void updateDataFromNetForOAdapter() {
        StaticMethod.POST(getActivity(), SellAllOrderBuy.URL,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        return dialog;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("userId", InfoTool.getUserID(getActivity()));
                        //list.put("userId", "" + 1);
                        list.put("page", "" + page);
                        list.put("status", "" + 6);
                        return list;
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response == null || response.equals("")) { // 网络错误
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
                if (page == 1) orderItems.clear();
                SellAllOrderBuy.Analysis(getActivity(), response, orderItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        buyOrderAdapter.notifyDataSetChanged();
        setAdapter(buyOrderAdapter);
        updateList();
    }

}
