package com.szdd.qianxun.sell.main.top;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.tools.PxAndDip;
import com.szdd.qianxun.sell.main.WebBrowser;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.TopScroller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SellMainTop implements View.OnClickListener {
    private View view, mCircleView;
    private FragmentActivity activity;
    private ViewPager viewPager;
    private TopScroller topScroller;
    private Context context;
    private String[] links, titles;

    public SellMainTop(FragmentActivity activity, View view) {
        this.activity = activity;
        this.view = view;
        this.context = view.getContext();
        initTopView();
        initFourView();
    }


    private void initTopView() {
        viewPager = (ViewPager) view.findViewById(R.id.sell_top_pager);
        viewPager.setAdapter(new SellMainTopAdapter(activity));

        //设置自动播放viewpager,循环左右切换
        topScroller = new TopScroller(context,
                (ViewPager) view.findViewById(R.id.sell_top_pager_ad));
        topScroller.setTime(5000, 500);//第一个参数是滑动间隙，第二个是切换的时间
        setImage();//设置view
    }

    private void initFourView() {
        StaticMethod.POST(activity, ServerURL.FIRST_PAGE_CENTER, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                return null;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                try {
                    TextView[] title_tv = new TextView[4];
                    title_tv[0] = (TextView) view.findViewById(R.id.sell_top_title_1);
                    title_tv[1] = (TextView) view.findViewById(R.id.sell_top_title_2);
                    title_tv[2] = (TextView) view.findViewById(R.id.sell_top_title_3);
                    title_tv[3] = (TextView) view.findViewById(R.id.sell_top_title_4);
                    TextView[] text_tv = new TextView[4];
                    text_tv[0] = (TextView) view.findViewById(R.id.sell_top_text_1);
                    text_tv[1] = (TextView) view.findViewById(R.id.sell_top_text_2);
                    text_tv[2] = (TextView) view.findViewById(R.id.sell_top_text_3);
                    text_tv[3] = (TextView) view.findViewById(R.id.sell_top_text_4);
                    ImageView[] img = new ImageView[4];
                    img[0] = (ImageView) view.findViewById(R.id.sell_top_img_1);
                    img[1] = (ImageView) view.findViewById(R.id.sell_top_img_2);
                    img[2] = (ImageView) view.findViewById(R.id.sell_top_img_3);
                    img[3] = (ImageView) view.findViewById(R.id.sell_top_img_4);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    JSONObject json_item;
                    links = new String[4];
                    titles = new String[4];
                    for (int i = 0; i < 4; i++) {
                        json_item = jsonArray.getJSONObject(i);
                        String url = json_item.getString("image");
                        titles[i] = json_item.getString("title");
                        String text = json_item.getString("context");
                        links[i] = json_item.getString("link");
                        StaticMethod.UBITMAP(ServerURL.getIP() + url, img[i]);
                        title_tv[i].setText(titles[i]);
                        text_tv[i].setText(text);
                    }
                    
//                    links = new String[]{"http://www.sylsylsyl.com/software/qianxun/BiaoQing/main.html",
//                            "http://www.sylsylsyl.com/software/qianxun/DaRen/main.html",
//                            "http://www.sylsylsyl.com/software/qianxun/CaiNiao/main.html",
//                            "http://mp.weixin.qq.com/s?__biz=MzIzMDE5MDU1MQ==&mid=402118626&idx=1&sn=e3bc35461e463a55808c9d120d7c6e8a&scene=18#wechat_redirect"};
//                    titles = new String[]{"表情包","达人秀","菜鸟","微团队"};


                    view.findViewById(R.id.sell_main_top_ad1).setOnClickListener(SellMainTop.this);
                    view.findViewById(R.id.sell_main_top_ad2).setOnClickListener(SellMainTop.this);
                    view.findViewById(R.id.sell_main_top_ad3).setOnClickListener(SellMainTop.this);
                    view.findViewById(R.id.sell_main_top_ad4).setOnClickListener(SellMainTop.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sell_main_top_ad1:
                WebBrowser.browse(activity, titles[0], links[0]);
                break;
            case R.id.sell_main_top_ad2:
                WebBrowser.browse(activity, titles[1], links[1]);
                break;
            case R.id.sell_main_top_ad3:
                WebBrowser.browse(activity, titles[2], links[2]);
                break;
            case R.id.sell_main_top_ad4:
                WebBrowser.browse(activity, titles[3], links[3]);
                break;
        }
    }

    private void setImage() {
        StaticMethod.POST(activity, ServerURL.FIRST_PAGE_TOP, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                return null;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    List<View> views = new ArrayList<>();
                    JSONObject json_item;
                    for (int i = -1; i < jsonArray.length() + 1; i++) {
                        int ii = i;
                        if (i == -1) ii = jsonArray.length() - 1;
                        else if (i == jsonArray.length()) ii = 0;
                        json_item = jsonArray.getJSONObject(ii);
                        String url = json_item.getString("image");
                        String title = json_item.getString("title");
                        String link = json_item.getString("link");
                        if (url == null) continue;
                        views.add(getImageView(url, title, link));
                    }
                    if (views.size() == 0) {
                        views.add(getImageView(R.drawable.icon_img_load_fail, null, null));
                        views.add(getImageView(R.drawable.icon_img_load_fail, null, null));
                        views.add(getImageView(R.drawable.icon_img_load_fail, null, null));
                    }
                    topScroller.setViews(views);//设置view
                    //圆点的初始化
                    initCircle(views.size() - 2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private View getImageView(String imgurl, final String title, final String link) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        StaticMethod.BITMAP(context, ServerURL.getIP() + imgurl, imageView);
//        ImageLoader.getInstance().displayImage(ServerURL.getIP() + imgurl, imageView);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WebBrowser.browse((Activity) context, title, link);
//            }
//        });
        return imageView;
    }

    private View getImageView(int imgRes, final String title, final String link) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(imgRes);
        if (title == null || link == null) {
        } else {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebBrowser.browse((Activity) context, title, link);
                }
            });
        }
        return imageView;
    }

    private void initCircle(int circleNum) {
        final LinearLayout mNumLayout = (LinearLayout) view.findViewById(R.id.sell_top_pager_circlelayout);
        for (int i = 0; i < circleNum; i++) {
            View circleView = new Button(context);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
                    PxAndDip.dipTopx(context, 10), PxAndDip.dipTopx(context, 10));
            ll.leftMargin = PxAndDip.dipTopx(context, 5);
            ll.rightMargin = PxAndDip.dipTopx(context, 5);
            circleView.setLayoutParams(ll);
            circleView.setBackgroundResource(R.drawable.tab_sell_circle_light);
            mNumLayout.addView(circleView);
        }
        mCircleView = mNumLayout.getChildAt(0);
        mCircleView.setBackgroundResource(R.drawable.tab_sell_circle_dark);
        topScroller.setProcessPage(new TopScroller.ProcessPage() {
            @Override
            public void pageSelected(int position) {
                mCircleView.setBackgroundResource(R.drawable.tab_sell_circle_light);
                View circleView = mNumLayout.getChildAt(position);
                circleView.setBackgroundResource(R.drawable.tab_sell_circle_dark);
                mCircleView = circleView;
            }
        });
    }
}