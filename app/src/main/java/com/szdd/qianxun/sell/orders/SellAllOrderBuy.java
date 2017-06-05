package com.szdd.qianxun.sell.orders;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.alibaba.fastjson.JSONException;
import com.szdd.qianxun.R;
import com.szdd.qianxun.sell.main.top.ListTopAdapter;
import com.szdd.qianxun.sell.orders.buy.BuyIngFragment;
import com.szdd.qianxun.sell.orders.buy.BuyFinishFragment;
import com.szdd.qianxun.sell.orders.buy.BuyOrderFragment;
import com.szdd.qianxun.sell.orders.buy.BuyRefundFragment;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.top.FragmentTActivity;
import com.szdd.qianxun.tools.views.slidepage.PagerSlidingTabStrip;
import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellAllOrderBuy extends FragmentTActivity {
    public static final String URL = ServerURL.ALL_ORDER_BOUGHT_URL;
    private static int status_rufund = 0;//用来记录申请退款的状态值，1为成功申请，0为申请失败或未拒绝过

    @Override
    public void onCreate() {
        setContentView(R.layout.sell_all_order_buy);
        setTitle("已购买的服务");
        showBackButton();
        // ((TextView) findViewById(R.id.common_top_title)).setText("已购买的服务");
        initActionBar(getResources().getColor(R.color.topbar_bg));
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerStrip);
        initFragmentPager(viewPager, pagerSlidingTabStrip);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        status_rufund = data.getIntExtra("status",0);

    }

    public static int getStatus_rufund(){
        return status_rufund;
    }

    public void initFragmentPager(ViewPager viewPager, PagerSlidingTabStrip pagerSlidingTabStrip) {
        final ArrayList<ScrollAbleFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new BuyOrderFragment());
        fragmentList.add(new BuyIngFragment());
        fragmentList.add(new BuyFinishFragment());
        fragmentList.add(new BuyRefundFragment());

        List<String> titleList = new ArrayList<>();
        titleList.add("已预约");
        titleList.add("已支付");
        titleList.add("已完成");
        titleList.add("已退款");

        viewPager.setAdapter(new ListTopAdapter(getSupportFragmentManager(), fragmentList, titleList));
        viewPager.setCurrentItem(0);

        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setIndicatorColorResource(R.color.main_yellow);
        pagerSlidingTabStrip.setIndicatorHeight(8);
        pagerSlidingTabStrip.setTabPaddingLeftRight(10);
        pagerSlidingTabStrip.setDividerPadding(10);
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
                    map.put("headview", jsonObject.getJSONObject("businessService").getJSONObject("user")
                            .getString("headIcon"));
                    map.put("username", jsonObject.getJSONObject("businessService").getJSONObject("user")
                            .getLong("username"));
                    map.put("nickname", jsonObject.getJSONObject("businessService").getJSONObject("user")
                            .getString("nickName"));
                    if (jsonObject.getJSONObject("businessService").getJSONArray("images").length() != 0)
                        map.put("describe", jsonObject.getJSONObject("businessService").getJSONArray("images").getJSONObject(0).getString("link"));
                    map.put("progectcontent",
                            jsonObject.getJSONObject("businessService").getString("name"));
                    map.put("status",
                            jsonObject.getInt("status"));
                    map.put("reward_money",
                            jsonObject.getJSONObject("businessService").getString("reward_money"));
                    map.put("reward_unit",
                            jsonObject.getJSONObject("businessService").getString("reward_unit"));
                    map.put("numbercontent",
                            jsonObject.getInt("number"));
                    String timeStr = jsonObject.getString("statusTime");
                    SimpleDateFormat sdf = null;
                    String date = "";
                    if(timeStr!=null){
                        sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
                        long publish_time_long = Long.parseLong(timeStr);
                        date = sdf.format(publish_time_long);
                    }
                    map.put("timecontent",date);
                    map.put("connectcontent", jsonObject.getJSONObject("businessService").getJSONObject("user").getLong("username"));
                    map.put("charge",jsonObject.getInt("price"));
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
