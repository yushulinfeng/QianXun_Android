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

public class SaleOrderFragment extends SellAllOrderFragment {
    private SaleOrderAdapter saleOrderAdapter;
    private List<Map<String, Object>> orderItems;
    private int page;

    public SaleOrderFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public SaleOrderFragment(BaseAdapter adapter) {
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
    }

    @Override
    public boolean onLazyLoad() {
        page = 1;
        orderItems = new ArrayList<>();
        saleOrderAdapter = new SaleOrderAdapter(getActivity(), orderItems);
        updateDataFromNetForOAdapter();
        return true;
    }

    //连接后台获取服务的详细信息
    private void updateDataFromNetForOAdapter() {
        StaticMethod.POST(getActivity(), SellAllOrderSell.URL,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        return dialog;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("userId", InfoTool.getUserID(getActivity()));
                        list.put("page", "" + page);
                        list.put("status", "6");
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
                SellAllOrderSell.Analysis(getActivity(), response, orderItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        saleOrderAdapter.notifyDataSetChanged();
        setAdapter(saleOrderAdapter);
        updateList();
    }

}
