package com.szdd.qianxun.more;

import android.content.Context;
import android.support.v4.view.ViewPager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.sell.main.top.ListTopAdapter;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.top.FragmentTActivity;
import com.szdd.qianxun.tools.views.slidepage.PagerSlidingTabStrip;
import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RequestMy extends FragmentTActivity {
    private List<Map<String, Object>> list_my01 = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> list_my02 = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> list_my03 = new ArrayList<Map<String, Object>>();
    private ArrayList<ScrollAbleFragment> fragmentList;
    private ViewPager viewPager;
    private PagerSlidingTabStrip pagerSlidingTabStrip;

    @Override
    public void onCreate() {
        setContentView(R.layout.more_request_my);
        setTitle("我的需求");
        showBackButton();
        initActionBar(getResources().getColor(R.color.topbar_bg));
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerStrip);
        getDataFromServer(RequestMy.this);
    }

    public void initFragmentPager(final ViewPager viewPager, PagerSlidingTabStrip pagerSlidingTabStrip) {
        List<String> strlist = new ArrayList<String>();
        for (int i = 0; i < new Random().nextInt(100) + 31; i++) {
            strlist.add(String.valueOf(i));
        }
        fragmentList = new ArrayList<>();
        fragmentList.add(new RequestFragment(RequestMy.this, new RequestAdapter(RequestMy.this, R.layout.expandable_listitem, list_my01, 0), 0));
        fragmentList.add(new RequestFragment(RequestMy.this, new RequestAdapter(RequestMy.this, R.layout.expandable_listitem, list_my02, 1), 1));
        fragmentList.add(new RequestFragment(RequestMy.this, new RequestAdapter(RequestMy.this, R.layout.expandable_listitem, list_my03, 2), 2));
        List<String> titleList = new ArrayList<>();
        titleList.add("报名中");
        titleList.add("进行中");
        titleList.add("已完成");
        viewPager.setAdapter(new ListTopAdapter(getSupportFragmentManager(), fragmentList, titleList));
        viewPager.setCurrentItem(0);
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setIndicatorColorResource(R.color.main_yellow);
        pagerSlidingTabStrip.setIndicatorHeight(5);
        pagerSlidingTabStrip.setUnderlineColorResource(R.color.no_color);
        pagerSlidingTabStrip.setDividerColorResource(R.color.no_color);
    }

    @Override
    public void showContextMenu() {
    }

    public void getDataFromServer(final Context context) {

        StaticMethod.POST(context, ServerURL.GET_REQUEST_URL,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        dialog.config(context, "发送中...", "玩命加载中...", true);
                        return dialog;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("userId", InfoTool.getUserID(RequestMy.this));
                        return list;
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response == null || response.equals("")) showToast("网络错误，请稍后重试");
                        else if (response.equals("failed")) showToast("操作失败！");
                        else {
                            AnalysizeData(response);
                            initFragmentPager(viewPager, pagerSlidingTabStrip);
                        }
                    }
                });
    }

    public void AnalysizeData(String Jsonstring) {
        if (Jsonstring == null || Jsonstring.equals("")) return;
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(Jsonstring);
        } catch (Exception e) {
            return;
        }
        JSONArray array;
        array = jsonObject.getJSONArray("list");
        if (array == null || array.size() == 0) return;
        int type = 10;
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String, Object> child1Data = new HashMap<String, Object>();
            child1Data.put("descripe", object.getString("request_content"));
            child1Data.put("reward", object.getString("reward_money"));
            child1Data.put("reward_self", object.getString("reward_thing"));
            child1Data.put("userRequestId", object.getString("id"));
            try {//发单者的信息
                JSONObject o = object.getJSONObject("user");
                String str_username = o.get("username").toString();
                String username = InfoTool.getUserName(RequestMy.this);
                if (!str_username.equals(username)) continue;
                child1Data.put("post_userName", o.get("username"));
                child1Data.put("post_userId", o.get("id"));
                child1Data.put("post_headIcon", ServerURL.getIP() + o.get("headIcon"));
                child1Data.put("post_user_nickname", o.get("nickName"));
                child1Data.put("post_gender", o.get("gender"));
            } catch (Exception e) {
            }
            try {// 接单者的信息
                JSONObject o = object.getJSONObject("finalReceiver");
                child1Data.put("Receiver_userId", o.get("id"));
                child1Data.put("Receiver_headIcon", ServerURL.getIP() + o.get("headIcon"));
                child1Data.put("Receiver_user_nickname", o.get("nickName"));
                child1Data.put("Receiver_gender", o.get("gender"));
                child1Data.put("Receiver_username", o.get("username"));
            } catch (Exception e) {
            }
            child1Data.put("posttime", object.getString("request_postTime"));
            child1Data.put("type", object.getString("status"));
            type = Integer.parseInt(object.getString("status"));
            if (type == 0) {
                list_my01.add(child1Data);
            } else if (type == 1 || type == 3) {// 我的需求——任务进行中
                list_my02.add(child1Data);
            } else if (type == 2) {// 我的需求——已完成
                list_my03.add(child1Data);
            }
        }
    }
}
