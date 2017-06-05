package com.szdd.qianxun.sell.main;


import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szdd.qianxun.R;
import com.szdd.qianxun.bank.RequestKnowFragment;
import com.szdd.qianxun.bank.RequestPowerFragment;
import com.szdd.qianxun.bank.RequestRunFragment;
import com.szdd.qianxun.bank.RequestSourceFragment;
import com.szdd.qianxun.message.baichuan.util.AndTools;
import com.szdd.qianxun.sell.main.top.ListTopAdapter;
import com.szdd.qianxun.sell.main.top.SellMainTop;
import com.szdd.qianxun.tools.base.LazyFragment;
import com.szdd.qianxun.tools.views.slidepage.PagerSlidingTabStrip;
import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;
import com.szdd.qianxun.tools.views.slidepage.ScrollableLayout;
import com.szdd.qianxun.tools.views.slidepage.SimpleSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainTabSell extends LazyFragment {
    private ScrollableLayout mScrollLayout;
    private View view;
    private ViewPager viewPager;
    //    private SellMainFragmentNew sell_new;
    //    private SellMainFragmentAll sell_all;
    //    private SellMainFragmentRound sell_round;
    //    private SellMainFragmentSchool sell_school;
    private RequestPowerFragment sell_new;
    private RequestRunFragment sell_all;
    private RequestSourceFragment sell_round;
    private RequestKnowFragment sell_school;
    private ArrayList<ScrollAbleFragment> fragmentList;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.tab_sell, container, false);
        new SellMainTop(getActivity(), view);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        // ScrollableLayout
        mScrollLayout = (ScrollableLayout) view.findViewById(R.id.scrollableLayout);
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.pagerStrip);
        initFragmentPager(viewPager, pagerSlidingTabStrip, mScrollLayout);
        //刷新
        final SimpleSwipeRefreshLayout lay = (SimpleSwipeRefreshLayout) view.findViewById(R.id.swipelayout);
        lay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        sell_new.onListRefresh();
                        break;
                    case 1:
                        sell_all.onListRefresh();
                        break;
                    case 2:
                        sell_round.onListRefresh();
                        break;
                    case 3:
                        sell_school.onListRefresh();
                        break;
                }
                new Handler().postDelayed(new Runnable() {//测试专用
                    @Override
                    public void run() {
                        lay.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        return view;
    }

    @Override
    protected boolean loadData() {//此处不能延迟加载
        return true;
    }

    public void initFragmentPager(ViewPager viewPager, PagerSlidingTabStrip pagerSlidingTabStrip,
                                  final ScrollableLayout mScrollLayout) {
        //使用这个通过参数传递优化代码
        List<String> titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();

//        titleList.add("最新");
//        titleList.add("综合");
//        titleList.add("附近");
//        titleList.add("同校");
        titleList.add("技能");
        titleList.add("跑腿");
        titleList.add("资源");
        titleList.add("知道");

//        sell_new = new SellMainFragmentNew();
//        sell_all = new SellMainFragmentAll();
//        sell_round = new SellMainFragmentRound();
//        sell_school = new SellMainFragmentSchool();
        sell_new = new RequestPowerFragment();
        sell_all = new RequestRunFragment();
        sell_round = new RequestSourceFragment();
        sell_school = new RequestKnowFragment();

        fragmentList.add(sell_new);
        fragmentList.add(sell_all);
        fragmentList.add(sell_round);
        fragmentList.add(sell_school);


        viewPager.setAdapter(new ListTopAdapter(getChildFragmentManager(), fragmentList, titleList));
        mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(0));

        pagerSlidingTabStrip.setUnderlineHeight(AndTools.dp2px(getActivity(), 5));
        pagerSlidingTabStrip.setUnderlineColorResource(R.color.label_color);
        pagerSlidingTabStrip.setIndicatorHeight(AndTools.dp2px(getActivity(), 5) + 8);
        pagerSlidingTabStrip.setIndicatorColorResource(R.color.main_yellow);
        pagerSlidingTabStrip.setDividerColorResource(R.color.no_color);

        pagerSlidingTabStrip.setViewPager(viewPager);
        //  pagerSlidingTabStrip.setIndicatorColorResource(R.color.topbar_bg);
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
        viewPager.setOffscreenPageLimit(7);
    }

}




