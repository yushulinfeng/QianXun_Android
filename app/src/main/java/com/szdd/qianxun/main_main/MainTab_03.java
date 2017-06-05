package com.szdd.qianxun.main_main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szdd.qianxun.R;
import com.szdd.qianxun.bank.RequestFPAdapter;
import com.szdd.qianxun.bank.RequestKnowFragment;
import com.szdd.qianxun.bank.RequestPowerFragment;
import com.szdd.qianxun.bank.RequestRunFragment;
import com.szdd.qianxun.bank.RequestSourceFragment;
import com.szdd.qianxun.sell.main.top.ListTopAdapter;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.slidepage.PagerSlidingTabStrip;
import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTab_03 extends Fragment {

    private Context context;
    private View rootView;
    private RequestFPAdapter rfpPager;
    private ViewPager pager;
    PagerSlidingTabStrip pagerSlidingTabStrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.tab_03, container, false);
            context = rootView.getContext();
        }
        initView(rootView);
        return rootView;
    }
    private void initView(View view) {
        pager = (ViewPager) view.findViewById(R.id.lcrequest_viewpager);
        pagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.pagerStrip);
        final ArrayList<ScrollAbleFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new RequestPowerFragment());
        fragmentList.add(new RequestRunFragment());
        fragmentList.add(new RequestSourceFragment());
        fragmentList.add(new RequestKnowFragment());
        List<String> titleList = new ArrayList<>();
        titleList.add("技能");
        titleList.add("跑腿");
        titleList.add("资源");
        titleList.add("知道");
        pager.setAdapter(new ListTopAdapter(getFragmentManager(), fragmentList, titleList));
        pager.setCurrentItem(0);
        pagerSlidingTabStrip.setViewPager(pager);
        pagerSlidingTabStrip.setIndicatorColorResource(R.color.main_yellow);
        pagerSlidingTabStrip.setIndicatorHeight(8);
        pagerSlidingTabStrip.setTabPaddingLeftRight(10);
        pagerSlidingTabStrip.setDividerPadding(10);
        pagerSlidingTabStrip.setUnderlineColorResource(R.color.no_color);
        pagerSlidingTabStrip.setDividerColorResource(R.color.no_color);
    }

    public static void Analysis(Context context, String jsonStr, List<Map<String, Object>> listItems) {
        JSONArray jsonArray = null;
        JSONObject jsonobject;
        try {
            jsonobject = new JSONObject(jsonStr);
            jsonArray = jsonobject.getJSONArray("list");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jsonArray != null && jsonArray.length() != 0) {
            JSONObject jsonObject;

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", jsonObject.getInt("id"));
                    map.put("headIcon", jsonObject.getJSONObject("user").getString("headIcon"));
                    map.put("username", jsonObject.getJSONObject("user").getLong("username"));
                    map.put("nickName", jsonObject.getJSONObject("user").getString("nickName"));
                    map.put("gender", jsonObject.getJSONObject("user").getString("gender"));
                    map.put("rank_credit", jsonObject.getJSONObject("user").getInt("rank_credit"));
                    map.put("verifyStatus", jsonObject.getJSONObject("user").getInt("verifyStatus"));
                    map.put("request_content", jsonObject.getString("request_content"));
                    map.put("request_picture", jsonObject.getString("request_picture"));
                    map.put("request_picture2", jsonObject.getString("request_picture2"));
                    map.put("request_picture3", jsonObject.getString("request_picture3"));
                    map.put("reward_money", jsonObject.getString("reward_money"));
                    map.put("reward_thing", jsonObject.getString("reward_thing"));
                    map.put("distance", jsonObject.getString("distance"));
                    String timeStr =  jsonObject.getString("request_postTime");
                    SimpleDateFormat sdf = null;
                    String date = "";
                    if(timeStr!=null){
                        sdf = new SimpleDateFormat("MM月dd日hh:mm");
                        long publish_time_long = Long.parseLong(timeStr);
                        date = sdf.format(publish_time_long);
                    }
                    map.put("request_postTime",date);
                    map.put("request_key", jsonObject.getString("request_key"));
                    map.put("status", jsonObject.getInt("status"));
                    listItems.add(map);
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }
        } else QianxunToast.showToast(context, "无更多需求", QianxunToast.LENGTH_SHORT);
    }

    public static void initEightImages(Context context) {
        StaticMethod.POST(context, ServerURL.IMAGES_URL,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        return dialog;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        return list;
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response == null || response.equals("")) {
                            return;
                        } else {
                            String[] images = new String[8];
                            JSONObject jsonobject;
                            try {
                                jsonobject = new JSONObject(response);
                                JSONArray jsonArray = jsonobject.getJSONArray("list");
                                if (jsonArray != null && jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        images[i] = (String) jsonArray.get(i);
                                    }
                                }
                            } catch (org.json.JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}