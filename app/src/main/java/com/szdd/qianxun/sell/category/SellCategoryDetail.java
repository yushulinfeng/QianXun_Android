package com.szdd.qianxun.sell.category;

import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.baichuan.util.AndTools;
import com.szdd.qianxun.sell.main.top.ListTopAdapter;
import com.szdd.qianxun.tools.file.ServiceType;
import com.szdd.qianxun.tools.top.FragmentTActivity;
import com.szdd.qianxun.tools.views.slidepage.PagerSlidingTabStrip;
import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;
import com.szdd.qianxun.tools.views.slidepage.ScrollableLayout;
import com.szdd.qianxun.tools.views.slidepage.SimpleSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class SellCategoryDetail extends FragmentTActivity {
    private ScrollableLayout mScrollLayout;
    private TextView tv_title, tv_back_title, tv_back_text;
    private RelativeLayout top_layout;
    private int top_limit = 0;
    private String title = "";
    private String text = "";
    private int order_index = 0;
    private SimpleSwipeRefreshLayout swipe_layout;
    private SellCategoryFragmentNew sell_new;
    private SellCategoryFragmentRound sell_round;
    private SellCategoryFragmentSchool sell_school;
    private ImageView img_category;

    private void getMessage() {
        order_index = getIntent().getIntExtra("index", 0);
        title = ServiceType.getServiceType(order_index);
        text = ServiceType.getServiceTypeText(order_index);
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.sell_category_detail);
        getMessage();
        initTopbar();
        initView();

    }

    public void setBitmap(int index) {
        switch (index) {
            case 0:
                img_category.setImageResource(R.drawable.shouhuisumiao);
                break;
            case 1:
                img_category.setImageResource(R.drawable.peiwanyundong);
                break;
            case 2:
                img_category.setImageResource(R.drawable.sheyingps);
                break;
            case 3:
                img_category.setImageResource(R.drawable.xinlingyangba);
                break;
            case 4:
                img_category.setImageResource(R.drawable.shougongdiy);
                break;
            case 5:
                img_category.setImageResource(R.drawable.caiyiwu);
                break;
            case 6:
                img_category.setImageResource(R.drawable.xuebaketang);
                break;
            case 7:
                img_category.setImageResource(R.drawable.lehuozu);
                break;
            case 8:
                img_category.setImageResource(R.drawable.meizhuangdaren);
                break;
            case 9:
                img_category.setImageResource(R.drawable.shangyejie);
                break;
            case 10:
                img_category.setImageResource(R.drawable.zulinyuan);
                break;
            case 11:
                img_category.setImageResource(R.drawable.xianzhibaobei);
                break;
            case 12:
                img_category.setImageResource(R.drawable.lingshishuiguo);
                break;
            case 13:
                img_category.setImageResource(R.drawable.daibanpaotui);
                break;
            case 14:
                img_category.setImageResource(R.drawable.gengduojingcai);
                break;
        }
    }

    private void initView() {
        img_category = (ImageView) this.findViewById(R.id.img_category);
        setBitmap(order_index);
        tv_title = (TextView) findViewById(R.id.sell_category_detail_title);
        tv_back_title = (TextView) findViewById(R.id.sell_category_detail_back_title);
        tv_back_text = (TextView) findViewById(R.id.sell_category_detail_back_text);
        top_layout = (RelativeLayout) findViewById(R.id.sell_category_detail_layout_top);

        tv_title.setText(title);
        tv_back_title.setText(title);
        tv_back_text.setText(text);

        //中间的列表
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        mScrollLayout = (ScrollableLayout) findViewById(R.id.scrollableLayout);
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerStrip);
        initFragmentPager(viewPager, pagerSlidingTabStrip, mScrollLayout);
        mScrollLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            public void onScroll(int currentY, int maxY) {
                if (currentY < top_limit) {
                    if (tv_title.getVisibility() == View.VISIBLE) {
                        tv_title.setVisibility(View.INVISIBLE);
                        // top_layout.setBackgroundResource(R.color.no_color);
                    }
                } else {
                    if (tv_title.getVisibility() == View.INVISIBLE) {
                        tv_title.setVisibility(View.VISIBLE);
                        // top_layout.setBackgroundResource(R.color.main_yellow);
                    }
                }
            }
        });

        //刷新
        swipe_layout = (SimpleSwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        sell_new.onListRefresh();
                        break;
                    case 1:
                        sell_round.onListRefresh();
                        break;
                }
                new Handler().postDelayed(new Runnable() {/////////////////测试专用
                    @Override
                    public void run() {
                        swipe_layout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    public void initFragmentPager(ViewPager viewPager, PagerSlidingTabStrip pagerSlidingTabStrip,
                                  final ScrollableLayout mScrollLayout) {
        final ArrayList<ScrollAbleFragment> fragmentList = new ArrayList<>();
//        fragmentList.add(new ListTestFragment());
//        fragmentList.add(new ListTestFragment());
        sell_new = new SellCategoryFragmentNew(order_index);
        sell_round = new SellCategoryFragmentRound(order_index);
        sell_school = new SellCategoryFragmentSchool(order_index);
        fragmentList.add(sell_new);
        fragmentList.add(sell_round);
        fragmentList.add(sell_school);

        List<String> titleList = new ArrayList<>();
        titleList.add("最新");
        titleList.add("附近");
        titleList.add("同校");

        viewPager.setAdapter(new ListTopAdapter(getSupportFragmentManager(), fragmentList, titleList));
        mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(0));
        pagerSlidingTabStrip.setViewPager(viewPager);

        pagerSlidingTabStrip.setUnderlineHeight(AndTools.dp2px(this, 5));
        pagerSlidingTabStrip.setUnderlineColorResource(R.color.label_color);
        pagerSlidingTabStrip.setIndicatorHeight(AndTools.dp2px(this, 5) + 5);
        pagerSlidingTabStrip.setIndicatorColorResource(R.color.main_yellow);
        pagerSlidingTabStrip.setDividerColorResource(R.color.no_color);

        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(5);
    }

    @Override
    public void showContextMenu() {
    }

    private void initTopbar() {
        top_limit = AndTools.dp2px(this, 70);
        // 透明化状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            LinearLayout.LayoutParams back_param = (LinearLayout.LayoutParams)
                    findViewById(R.id.sell_category_detail_back_layout).getLayoutParams();
            back_param.height = AndTools.dp2px(this, 145);
            findViewById(R.id.sell_category_detail_back_layout).setLayoutParams(back_param);

            RelativeLayout.LayoutParams front_param = (RelativeLayout.LayoutParams)
                    findViewById(R.id.sell_category_detail_layout_top).getLayoutParams();
            front_param.topMargin = AndTools.dp2px(this, 25);
            findViewById(R.id.sell_category_detail_layout_top).setLayoutParams(front_param);
        }

        findViewById(R.id.sell_category_detail_back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.mine_homepage_threeline).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AndTools.showToast(SellCategoryDetail.this, title);
            }
        });

    }


}
