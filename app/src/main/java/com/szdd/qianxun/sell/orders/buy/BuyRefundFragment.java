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

public class BuyRefundFragment extends SellAllOrderFragment {
    private BuyRefundAdapter buyRefundAdapter;
    private List<Map<String, Object>> refundItems;
    private int page;

    public BuyRefundFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public BuyRefundFragment(BaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public void onListLoad() {
        page++;
        updateDataFromNetForRAdapter();
    }

    @Override
    public void onListRefresh() {
        page = 1;
        updateDataFromNetForRAdapter();
    }

    @Override
    public void onListClick(int position) {
    }

    @Override
    public boolean onLazyLoad() {
        page = 1;
        refundItems = new ArrayList<>();
        buyRefundAdapter = new BuyRefundAdapter(getActivity(), refundItems);
        updateDataFromNetForRAdapter();
        return true;
    }

    //连接后台获取服务的详细信息
    private void updateDataFromNetForRAdapter() {
        StaticMethod.POST(getActivity(), SellAllOrderBuy.URL,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        dialog.config(getActivity(), "正在连接", "请稍后……", true);
                        return dialog;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("userId", InfoTool.getUserID(getActivity()));
                        //list.put("userId", ""+1);
                        list.put("page", "" + page);
                        list.put("status", "" + 3);
                        return list;
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response == null || response.equals("")) {
                            // 网络错误
                            showToast("网络错误,请连接网络后重新加载");
                            return;
                        }
                        dealResponse(response);
                    }
                });
    }

    //处理后台发送来的数据
    private void dealResponse(String response) {
        if (response.equals("failed")) {
            showToast("失败！");
        } else {
            try {
                if (page == 1) refundItems.clear();
                SellAllOrderBuy.Analysis(getActivity(), response, refundItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setAdapter(buyRefundAdapter);
        updateList();
    }

}
