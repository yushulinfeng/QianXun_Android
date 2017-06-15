package com.szdd.qianxun.message.homepage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;
import com.szdd.qianxun.tools.views.slidepage.ScrollableHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linorz on 2016/3/26.
 */
@SuppressLint("ValidFragment")
public class MineRequest extends ScrollAbleFragment implements HomeListView.XListViewListener,
        ScrollableHelper.ScrollableContainer {
    private View rootView;
    private HomeListView requestList;
    private MineRequestAdapter requestAdapter;
    private Context context;
    private List<Map<String, Object>> items;//item
    // 后台交互
    private String imgip = ServerURL.getIP();
    private int page = 1;
    private String userId;

    public MineRequest(String userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent)
                parent.removeView(rootView);
        } else {
            rootView = inflater.inflate(R.layout.msg_mine_request, container, false);
            context = rootView.getContext();
            //初始化所有控件
            initAllView(rootView);
            getRequest();
        }
        return rootView;
    }

    //初始化
    private void initAllView(View view) {
        requestList = (HomeListView) view.findViewById(R.id.mine_request_list);
        items = new ArrayList<Map<String, Object>>();
        requestAdapter = new MineRequestAdapter(context, items);
        requestList.setAdapter(requestAdapter);
        requestList.setXListViewListener(this);
        requestList.setPullLoadEnable(true);
        requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MsgPublicTool.showRequestDetail(context,
                        (long) items.get(i).get("id") + "", "five");
            }
        });
    }

    @Override
    public void onLoadMore() {
        getRequest();
        requestList.stopLoadMore();
    }

    private void getRequest() {
        StaticMethod.POST(context, ServerURL.HOMEPAGE_REQUEST, new ConnectListener() {
            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", userId);
                list.put("page", page);
                return list;
            }

            @Override
            public void onResponse(String response) {
                if (response == null)
                    QianxunToast.showToast(context, "网络请求失败", QianxunToast.LENGTH_SHORT);
                else {
                    try {
                        JSONObject jo = new JSONObject(response);
                        JSONArray list = jo.getJSONArray("list");
                        if (list.toString().equals("[]")) return;
                        JSONObject json_item;
                        for (int i = 0; i < list.length(); i++) {
                            Map<String, Object> requestItem = new HashMap<String, Object>();
                            json_item = list.getJSONObject(i);
                            requestItem.put("id", json_item.getLong("id"));
                            requestItem.put("label", json_item.getString("request_key"));
                            requestItem.put("text", json_item.getString("request_content"));
                            requestItem.put("distance", json_item.getString("distance"));
                            requestItem.put("time", StaticMethod.formatDateTime("yyyy-MM-dd HH:mm",
                                    json_item.getLong("request_postTime")));
                            requestItem.put("money", json_item.getString("reward_money"));
                            requestItem.put("thing", json_item.getString("reward_thing"));
                            String str = json_item.getString("request_picture");
                            if (str.equals("")) requestItem.put("image1", "");
                            else requestItem.put("image1", imgip + str);
                            str = json_item.getString("request_picture2");
                            if (str.equals("")) requestItem.put("image2", "");
                            else requestItem.put("image2", imgip + str);
                            str = json_item.getString("request_picture3");
                            if (str.equals("")) requestItem.put("image3", "");
                            else requestItem.put("image3", imgip + str);
                            items.add(requestItem);
                        }
                        page++;
                        requestAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public View getScrollableView() {
        return requestList;
    }
}