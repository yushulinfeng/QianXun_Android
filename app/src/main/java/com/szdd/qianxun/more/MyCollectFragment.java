package com.szdd.qianxun.more;

import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.sell.main.bottom.SellMainFragmentBase;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;

import org.json.JSONException;
import org.json.JSONObject;

public class MyCollectFragment extends SellMainFragmentBase {

    public MyCollectFragment() {

    }

    @Override
    public void onListLoad() {
        page++;
        updateDataFromNet();
    }

    @Override
    public void onListRefresh() {
        page = 0;
        updateDataFromNet();
    }

    @Override
    public boolean onLazyLoad() {
        page = 0;
        updateDataFromNet();
        return true;
    }

    private void updateDataFromNet() {
        JSONObject json = new JSONObject();
        try {
            json.put("userId", InfoTool.getUserID(getActivity()));
            json.put("page", page + "");
        } catch (JSONException e) {
        }
        final String jsonObj = json.toString();
        StaticMethod.POST(getActivity(), ServerURL.BUSINESS_COLLECT, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("jsonObj", jsonObj);
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                dealResponse(response);
            }
        });
    }

}
