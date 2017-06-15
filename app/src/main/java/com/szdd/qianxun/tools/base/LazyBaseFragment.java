package com.szdd.qianxun.tools.base;

import android.view.View;
import android.widget.TextView;

import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;

public abstract class LazyBaseFragment extends ScrollAbleFragment {

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

    /**
     * 子类需要必须覆盖，此处为了协调统一，不得不实现该方法
     */
    @Override
    public View getScrollableView() {
        return new TextView(getActivity());
    }
}
