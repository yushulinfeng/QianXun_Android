package com.szdd.qianxun.sell.category;

import android.annotation.SuppressLint;

import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.map.LocationListener;

import org.json.JSONException;
import org.json.JSONObject;

public class SellCategoryFragmentRound extends SellCategoryFragmentBase {
    public SellCategoryFragmentRound() {
    }

    @SuppressLint("ValidFragment")
    public SellCategoryFragmentRound(int index) {
        super(index);
    }

    @Override
    public void onListLoad() {
        updateDataFromNet();
    }

    @Override
    public void onListRefresh() {
        page = -1;
        updateDataFromNet();
    }

    @Override
    public boolean onLazyLoad() {
        page = -1;
        updateDataFromNet();
        return true;
    }

    private void updateDataFromNet() {
        StaticMethod.Location(getActivity(), new LocationListener() {
            @Override
            public void locationRespose(String locationName, double x, double y, float limit) {
                JSONObject json = new JSONObject();
                try {
                    json.put("location_x", x + "");
                    json.put("location_y", y + "");
                    json.put("page", page + "");
                    json.put("category", index + "");
                } catch (JSONException e) {
                }
                final String jsonObj = json.toString();
                StaticMethod.POST(getActivity(), ServerURL.BUSINESS_ROUND, new ConnectListener() {
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
                        if (item_list != null && item_list.size() != 0)
                            page = item_list.get(item_list.size() - 1).getId();
                    }
                });
            }
        });
    }

}
