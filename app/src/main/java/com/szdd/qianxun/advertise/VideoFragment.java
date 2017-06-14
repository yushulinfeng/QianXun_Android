package com.szdd.qianxun.advertise;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.xlist.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LLX on 2015/10/29.
 */
public class VideoFragment extends Fragment implements XListView.XListViewListener {
    private View rootView;
    private AdapterInAdVideo myadapter;
    private List<Map<String, Object>> items;
    private XListView xlistview;
    private Context context;
    private int page = 1;//页数
    private String videoip = ServerURL.getIP();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent)
                parent.removeView(rootView);
        } else {
            rootView = inflater.inflate(R.layout.ad_video, container, false);
            init(rootView);
            getVideo();
        }
        return rootView;
    }

    // 初始化
    public void init(View view) {
        items = new ArrayList<>();
        xlistview = (XListView) view.findViewById(R.id.ad_gridview_video);
        context = view.getContext();
        myadapter = new AdapterInAdVideo(getActivity(), items);
        xlistview.setAdapter(myadapter);
        xlistview.setXListViewListener(this);
        xlistview.setPullLoadEnable(true);
        xlistview.setPullRefreshEnable(true);
    }

    @Override
    public void onRefresh() {
        items.clear();
        myadapter.notifyDataSetChanged();
        page = 1;
        getVideo();
        xlistview.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        getVideo();
        xlistview.stopLoadMore();
    }

    public void getVideo() {
        StaticMethod.POST(context, ServerURL.GET_VIDEO, new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        return null;
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response == null) showToast("网络请求失败");
                        else analysisVideo(response);
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("page", page);
                        return list;
                    }
                }

        );
    }

    public void analysisVideo(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray list = jsonObject.getJSONArray("list");
            if (list.toString().equals("[]")) return;
            JSONObject jsonvideo;
            String uri_string;
            for (int i = 0; i < list.length(); i++) {
                jsonvideo = list.getJSONObject(i);
                uri_string = videoip + jsonvideo.getString("fpath");
                Map<String, Object> griditem = new HashMap<String, Object>();
                //uri
                griditem.put("video_uri", uri_string);
                //标题
                griditem.put("video_name", jsonvideo.getString("name"));
                //截图
                griditem.put("video_picture", jsonvideo.getString("thumbNail"));
                //Id
                griditem.put("id", jsonvideo.getLong("id"));
                items.add(griditem);
            }
            page++;
            myadapter.notifyDataSetChanged();
        } catch (JSONException e) {
            showToast("网络请求失败");
            e.printStackTrace();
        }
    }

    private void showToast(String text) {
        QianxunToast.showToast(context, text, QianxunToast.LENGTH_SHORT);
    }
}