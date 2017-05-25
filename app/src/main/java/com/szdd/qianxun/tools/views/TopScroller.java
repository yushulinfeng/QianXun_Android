package com.szdd.qianxun.tools.views;

/**
 * Created by linorz on 2016/3/6.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TopScroller extends Scroller {
    private int SCROLLER_TIME = 4000;//滑动的间隙
    private int DURATION_TIME = 1000;//切换的时间
    private boolean shouldAutoScroll = true;
    private ViewPager viewPager = null;
    private List<View> views = new ArrayList<>();
    private ProcessPage processpage;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (shouldAutoScroll) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                handler.sendEmptyMessageDelayed(0, SCROLLER_TIME);
            }
        }
    };

    public TopScroller(Context context, ViewPager vp) {
        super(context);
        viewPager = vp;
    }

    public TopScroller(Context context, Interpolator interpolator, ViewPager vp) {
        super(context, interpolator);
        viewPager = vp;
    }


    public void setTime(int scrollerTime, int durationTime) {
        this.SCROLLER_TIME = scrollerTime + durationTime;
        this.DURATION_TIME = durationTime;
    }

    public void setViews(List<View> list) {
        if (views.size() != 0)
            views.clear();
        views.addAll(list);
        viewPager.setAdapter(new TopAdapter());
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    shouldAutoScroll = true;
                    handler.sendEmptyMessageDelayed(0, SCROLLER_TIME);
                } else {
                    handler.removeMessages(0);
                    shouldAutoScroll = false;
                }
                return false;
            }
        });
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessageDelayed(0, 4000);
        viewPager.setCurrentItem(1);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setShouldAutoScroll() {
        handler.removeMessages(0);
        if (viewPager.isShown()) {
            shouldAutoScroll = true;
            handler.sendEmptyMessageDelayed(0, SCROLLER_TIME);
        } else {
            shouldAutoScroll = false;
        }
    }

    public void setProcessPage(ProcessPage pp) {
        this.processpage = pp;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, shouldAutoScroll ? DURATION_TIME : duration);
    }

    class TopAdapter extends PagerAdapter {
        public TopAdapter() {
            initPagerListener();
        }

        private void initPagerListener() {
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    if (processpage != null && views.size() > 3)
                        if (position > 0 && position < views.size() - 1)
                            processpage.pageSelected(position - 1);
                        else if (position == 0) processpage.pageSelected(views.size() - 3);
                        else if (position == views.size() - 1) processpage.pageSelected(0);
                }

                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                    if (positionOffset == 0.0 && views.size() > 3) {//完全切换完后跳转
                        if (position == 0) viewPager.setCurrentItem(views.size() - 2, false);
                        if (position == views.size() - 1) viewPager.setCurrentItem(1, false);
                    }
                }

                public void onPageScrollStateChanged(int state) {
                }
            });
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position), 0);
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (views.get(position).getParent() != null)
                container.removeView(views.get(position));
        }
    }

    public interface ProcessPage {
        void pageSelected(int position);
    }
}
