package com.szdd.qianxun.tools.views.slidepage;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

//专门与ScrollableLayout适配
public class SimpleSwipeRefreshLayout extends SwipeRefreshLayout {

    private View view;

    public SimpleSwipeRefreshLayout(Context context) {
        super(context);
    }

    public SimpleSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewGroup(View view) {
        this.view = view;
    }

    @Override
    public boolean canChildScrollUp() {
        view = getChildAt(0);
        if (view != null && view instanceof ScrollableLayout) {
            final ScrollableLayout mylayout = (ScrollableLayout) view;
            return !mylayout.canScroll();//!mylayout.canPtr()
        }
        return false;
    }


}