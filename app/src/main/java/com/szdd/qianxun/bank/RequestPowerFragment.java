package com.szdd.qianxun.bank;

import android.content.Context;
import android.view.View;

import com.szdd.qianxun.main_main.MainTab_03;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.map.LocationListener;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/4/19 0019.
 */
public class RequestPowerFragment extends RequestAllFragment {
    private View rootView;
    private Context context = null;
    // 定位相关
    private String address;
    private double latitude, longitude;
    // 后台交互
    private String url = ServerURL.LOCAL_REQUEST_TYPE_URL;
    private String res = "";

    public RequestPowerFragment() {
        super();
        type = 3;
    }

    @Override
    public void onListLoad() {
        try {
            JSONObject jsonobject = new JSONObject(res);
            JSONArray jsonArray = jsonobject.getJSONArray("list");
            page = jsonArray.getJSONObject(jsonArray.length()-1).getInt("id");
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        updateDataFromNet(page, type);
    }

    @Override
    public void onListRefresh() {
        page = -1;
        requestlistItems.clear();
        updateDataFromNet(page, type);
    }

    public void updateDataFromNet(final int page_temp, final int type_temp) {
        is_update=true;
        com.szdd.qianxun.tools.map.Location.getLocation(getActivity(),
                new LocationListener() {
                    @Override
                    public void locationRespose(String locationName, double x,
                                                double y, float limit) {
                        address = locationName;// 地址
                        latitude = y;// 纬度
                        longitude = x;// 经度
                        // 定位后连接后台获取需求
                    }
                });
        StaticMethod.POST(getActivity(), url, new ConnectListener() {
            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
//                if (page_temp == -1) {
//                    dialog.config(getActivity(), "正在连接", "请稍后……", true);
//                    return dialog;
//                } else {
                    return null;
//                }
            }

            @Override
            public ConnectList setParam(ConnectList list) {
                JSONObject jsonobject = new JSONObject();
                try {
                    jsonobject.put("x", longitude);
                    jsonobject.put("y", latitude);
                    jsonobject.put("page", page_temp);
                    jsonobject.put("type", type_temp);
                } catch (org.json.JSONException e1) {
                    return null;
                }
                list.put("jsonObj", jsonobject.toString(), false);
                return list;
            }

            @Override
            public void onResponse(String response) {
                if (response == null) {
                    // 网络错误
                    showToast("网络错误,请连接网络后重新加载");
                    return;
                } else {
                    if (page_temp == -1) {
                        if (response.equals("failed")) {
                            showToast("获取附近需求失败!");
                        } else if (response.equals("")) {
                            showToast("附近无需求!");
                        } else {
                            MainTab_03.Analysis(getActivity(), response, requestlistItems);
                            res = response;
                            updateList();
                        }
                    } else {
                        if (response.equals("failed")) {
                            showToast("加载失败!");
                        } else if (response.equals("")) {
                            showToast("无更多需求!");
                        } else {
                            MainTab_03.Analysis(getActivity(), response, requestlistItems);
                            res = response;
                            updateList();
                        }
                    }
                    is_update=false;
                }
            }
        });
    }
}
