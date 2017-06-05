package com.szdd.qianxun.sell.orders;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.alibaba.fastjson.JSONException;
import com.szdd.qianxun.R;
import com.szdd.qianxun.sell.main.top.ListTopAdapter;
import com.szdd.qianxun.sell.orders.sale.SaleFinishFragment;
import com.szdd.qianxun.sell.orders.sale.SaleIngFragment;
import com.szdd.qianxun.sell.orders.sale.SaleOrderFragment;
import com.szdd.qianxun.sell.orders.sale.SaleRefundFragment;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.top.FragmentTActivity;
import com.szdd.qianxun.tools.views.slidepage.PagerSlidingTabStrip;
import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellAllOrderSell extends FragmentTActivity {
    public static final String URL = ServerURL.ALL_ORDER_SELLED_URL;

     private static int status_rufuseorder = 0;//用来记录拒绝订单的状态值，1为成功拒绝，0为拒绝失败或未拒绝过
    @Override
    public void onCreate() {
        setContentView(R.layout.sell_all_user_sell);
        setTitle("已售出的服务");
        //((TextView) findViewById(R.id.common_top_title)).setText("已售出的服务");
        showBackButton();
        initActionBar(getResources().getColor(R.color.topbar_bg));
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerStrip);
        initFragmentPager(viewPager, pagerSlidingTabStrip);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        status_rufuseorder = data.getIntExtra("status",0);

    }

    public static int getStatus_rufuseorder(){
        return status_rufuseorder;
    }

    public void initFragmentPager(ViewPager viewPager, PagerSlidingTabStrip pagerSlidingTabStrip) {

        final ArrayList<ScrollAbleFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SaleOrderFragment());
        fragmentList.add(new SaleIngFragment());
        fragmentList.add(new SaleFinishFragment());
        fragmentList.add(new SaleRefundFragment());

        List<String> titleList = new ArrayList<>();
        titleList.add("待接单");
        titleList.add("进行中");
        titleList.add("已完成");
        titleList.add("退款中");

        viewPager.setAdapter(new ListTopAdapter(getSupportFragmentManager(), fragmentList, titleList));
        viewPager.setCurrentItem(0);

        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setIndicatorColorResource(R.color.main_yellow);
        pagerSlidingTabStrip.setIndicatorHeight(5);
        pagerSlidingTabStrip.setUnderlineColorResource(R.color.no_color);
        pagerSlidingTabStrip.setDividerColorResource(R.color.no_color);
    }

    /**
     * 解析
     *
     * @throws JSONException
     * @throws org.json.JSONException
     */
    public static void Analysis(Context context, String jsonStr, List<Map<String, Object>> listitems) {
        /******************* 解析 ***********************/
        JSONArray jsonArray = null;
        // 初始化list数组对象
        JSONObject jsonobject;
        // List<Map<String, Object>> list = new ArrayList<Map<String,
        // Object>>();
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
                    // 初始化map数组对象
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", jsonObject.getInt("id"));
                    map.put("headview", jsonObject.getJSONObject("user").getString("headIcon"));
                    map.put("username", jsonObject.getJSONObject("user").getLong("username"));
                    map.put("nickname", jsonObject.getJSONObject("user").getString("nickName"));
                    if(jsonObject.getJSONObject("businessService").getJSONArray("images").length()!=0)
                        map.put("describe", jsonObject.getJSONObject("businessService").getJSONArray("images").getJSONObject(0).getString("link"));
                    map.put("progectcontent", jsonObject.getJSONObject("businessService").getString("name"));
                    map.put("reward_money", jsonObject.getJSONObject("businessService").getString("reward_money"));
                    map.put("reward_unit", jsonObject.getJSONObject("businessService").getString("reward_unit"));
                    map.put("numbercontent", jsonObject.getInt("number"));
                    String timeStr = jsonObject.getString("statusTime");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
                    long publish_time_long = Long.parseLong(timeStr);
                    String date = sdf.format(publish_time_long);
                    map.put("timecontent", date);
                    map.put("connectcontent", jsonObject.getJSONObject("user").getLong("username"));
                    map.put("charge", jsonObject.getInt("price"));
                    map.put("status",
                            jsonObject.getInt("status"));
                    listitems.add(map);
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
//            Toast.makeText(SellAllOrderBuy.this, "无更多内容", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void showContextMenu() {
    }

}
