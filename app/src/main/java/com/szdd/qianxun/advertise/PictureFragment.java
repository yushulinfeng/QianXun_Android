package com.szdd.qianxun.advertise;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.customview.LoadMoreRecyclerView;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LLX on 2015/10/29.
 */
public class PictureFragment extends Fragment {
    private String imgip = ServerURL.getIP();//图片ip前缀
    private View rootView;
    private Context context;
    private AdapterInAdPicture myadapter;//适应器
    private LoadMoreRecyclerView mRecyclerView;//recyclerview
    private SwipeRefreshLayout mSwipeRefreshLayout;//刷新
    private TextView hint;//网络提示
    private List<Map<String, Object>> items;//item
    private int page_dynamic = 1;
    private int current_layout = 1;//当前瀑布流列数
    private int CHANGE = 2;//改变列数
    private boolean dynamicIsEmpty = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent)
                parent.removeView(rootView);
        } else {
            rootView = inflater.inflate(R.layout.ad_picture, container, false);
            context = rootView.getContext();
            initAllView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        page_dynamic = 1;
        load(false);
    }

    private void initAllView(View view) {
        hint = (TextView) view.findViewById(R.id.ad_picture_hint);  //网络提示
        final Button change = (Button) view.findViewById(R.id.ad_picture_change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(800);//设置动画持续时间
                change.startAnimation(animation);
                current_layout = 1 + CHANGE++ % 3;
                mRecyclerView.switchLayoutManager(new StaggeredGridLayoutManager(current_layout, StaggeredGridLayoutManager.VERTICAL));
//                mRecyclerView.switchLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
        //recycerview
        mRecyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.ad_recyclerview_picture);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnPauseListenerParams(ImageLoader.getInstance(), false, true);
        mRecyclerView.setAutoLoadMoreEnable(true);
        //设置为瀑布流
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //设置适配器
        items = Collections.synchronizedList(new ArrayList<Map<String, Object>>());
        myadapter = new AdapterInAdPicture(context, items);
        mRecyclerView.setAdapter(myadapter);
        mRecyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                load(true);
            }
        });
        //刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ad_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.ad_background_yellow);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                myadapter.notifyDataSetChanged();
                page_dynamic = 1;
                dynamicIsEmpty = false;
                load(false);
            }
        });
    }

    private void load(boolean isLoadingMore) {
        if (!dynamicIsEmpty) getDynamic(isLoadingMore, true);
    }

    private void getDynamic(final Boolean isLoadMore, final Boolean isRefresh) {
        StaticMethod.POST(context, ServerURL.DYNAMIC_GET_BRIEF, new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        return null;
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            hint.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            hint.setVisibility(View.INVISIBLE);
                            analysisDynamic(response, isLoadMore, isRefresh);
                        }
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("page", page_dynamic);
                        return list;
                    }
                }
        );
    }

    private void analysisDynamic(String response, Boolean isLoadMore, Boolean isRefresh) {
        try {
            JSONObject pictureJson = new JSONObject(response);
            JSONArray list = pictureJson.getJSONArray("list");
            if (list.toString().equals("[]")) dynamicIsEmpty = true;
            else {
                JSONObject json_item;
                String s;
                for (int i = 0; i < list.length(); i++) {
                    Map<String, Object> griditem = new HashMap<>();
                    json_item = list.getJSONObject(i);
                    s = json_item.getString("pic1"); //评论图片
                    if (s.equals("")) griditem.put("comment_picture", "");
                    else griditem.put("comment_picture", imgip + s);
                    griditem.put("user_icon", imgip + json_item.getString("headIcon"));//头像
                    griditem.put("user_id", json_item.getLong("userId"));//Id
                    s = json_item.getString("content");//评论内容/标题
                    griditem.put("comment_context", s);
                    griditem.put("comment_id", json_item.getLong("id"));//动态id
                    items.add(griditem);
                }
                page_dynamic++;
            }
            listNotify(isLoadMore, isRefresh);
        } catch (JSONException e) {
            showToast("网络请求失败");
            mSwipeRefreshLayout.setRefreshing(false);
            e.printStackTrace();
        }
    }

    private void listNotify(boolean isLoadMore, boolean isRefresh) {
        if (current_layout != 0)
            mRecyclerView.switchLayoutManager(new StaggeredGridLayoutManager
                    (current_layout, StaggeredGridLayoutManager.VERTICAL));//变换布局
//        mRecyclerView.switchLayoutManager(new LinearLayoutManager(getActivity()));
        if (isLoadMore) stopLoad();
        if (isRefresh) stopRefesh();
    }

    private void stopRefesh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myadapter.notifyDataSetChanged();//在刷新时
                if (mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 500L);
    }

    private void stopLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.notifyMoreFinish(true);//在加载时
            }
        }, 500L);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        items.clear();
    }

    private void showToast(String text) {
        QianxunToast.showToast(context, text, QianxunToast.LENGTH_SHORT);
    }
}