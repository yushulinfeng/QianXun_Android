package com.szdd.qianxun.message.homepage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.customview.LoadMoreRecyclerView;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.ServiceType;
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
public class MineService extends ScrollAbleFragment implements ScrollableHelper.ScrollableContainer {
    private View rootView;
    private LoadMoreRecyclerView mRecyclerView;//recyclerview
    private Context context;
    private List<Map<String, Object>> items;//item
    private MineServiceAdapter serviceAdapter;//适配器
    private String getService = ServerURL.HOMEPAGE_SERVICE;
    private String imgip = ServerURL.getIP();
    private int page = 1;
    private String userId = null;

    @SuppressLint("ValidFragment")
    public MineService(String userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent)
                parent.removeView(rootView);
        } else {
            rootView = inflater.inflate(R.layout.msg_mine_service, container, false);
            context = rootView.getContext();
            if (userId == null)
                userId = InfoTool.getUserID(context);
            initAllView(rootView);//初始化组件
            getService();
        }
        return rootView;
    }

    //初始化
    private void initAllView(View view) {
        //适配器
        items = new ArrayList<>();
        serviceAdapter = new MineServiceAdapter(context, items);
        //设置适配器
        //recycerview
        mRecyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.mine_service_recyclerview);
        //设置为网格
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        mRecyclerView.setAdapter(serviceAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnPauseListenerParams(ImageLoader.getInstance(), false, true);
        mRecyclerView.setAutoLoadMoreEnable(true);
        mRecyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                getService();
            }
        });
        serviceAdapter.setRemoveItem(new MineServiceAdapter.RemoveItem() {
            @Override
            public void removeNotify(int position) {
                mRecyclerView.notifyRemove(position);
                items.remove(position);
            }
        });
    }

    private void getService() {
        StaticMethod.POST(context, getService, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", userId);
                list.put("page", page);
                return list;
            }

            @Override
            public void onResponse(String response) {
                if (response == null) showToast("网络请求失败");
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray list = jsonObject.getJSONArray("list");
                        if (list.toString().equals("[]")) return;
                        JSONObject json_item;
                        String str;
                        for (int i = 0; i < list.length(); i++) {
                            Map<String, Object> serviceItem = new HashMap<String, Object>();
                            json_item = list.getJSONObject(i);
                            JSONArray ja = json_item.getJSONArray("images");
                            if (ja.length() > 0)
                                str = imgip + ja.getJSONObject(0).getString("link");
                            else str = "";
                            serviceItem.put("url", str);
                            int int_c = json_item.getInt("category");
                            serviceItem.put("style", ServiceType.getServiceType(int_c));//分类信息
                            str = json_item.getString("name");
                            serviceItem.put("content", str);//标题
                            str = json_item.getString("reward_money");
                            serviceItem.put("price", str);//单价
                            str = json_item.getString("reward_unit");
                            serviceItem.put("unit", str);//单位
                            str = json_item.getString("finishedPeople");
                            serviceItem.put("sale", str);//销量
                            long id = json_item.getLong("id");
                            serviceItem.put("id", id);//服务id
                            //添加item
                            items.add(serviceItem);
                        }
                        page++;
                        mRecyclerView.notifyMoreFinish(true);
                    } catch (JSONException e) {
                        showToast("网络请求失败");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }
        });
    }

    private void showToast(String text) {
        QianxunToast.showToast(context, text, QianxunToast.LENGTH_SHORT);
    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }

}