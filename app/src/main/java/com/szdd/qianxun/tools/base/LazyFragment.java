package com.szdd.qianxun.tools.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 延迟加载的Fragment
 */
public abstract class LazyFragment extends LazyBaseFragment {
    private View mFragmentView;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (mFragmentView == null) {//暂时关闭懒加载
            mFragmentView = onCreateView(inflater, container);
            isPrepared = true;
            lazyLoad();
//        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null)
            parent.removeView(mFragmentView);


//        if (null == mFragmentView) {
//            mFragmentView = inflater.inflate(R.layout.fragment, container, false);
//            mListView = (ListView) mFragmentView.findViewById(R.id.mm_listview);
//            mListView.setAdapter(mAdapter);
//            mPbar = (ProgressBar) mFragmentView.findViewById(R.id.pbar_mm_loading);
//            mPbar.setVisibility(View.VISIBLE);
//        }


        return mFragmentView;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce)
            return;
        if (loadData())
            loadFinish();
    }

    public void loadFinish() {
//        mHasLoadedOnce = true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (null != mFragmentView) {
//            ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
//        }
        mFragmentView=null;
    }


    /**
     * 界面初始化，请不要在其中加载数据
     *
     * @return 根View
     */
    protected abstract View onCreateView(LayoutInflater inflater, ViewGroup container);

    /**
     * 延迟加载数据（仅在初次运行且界面显示时，加载一次数据）
     *
     * @return 同步加载请返回true, 异步加载请返回false，加载完成后需要手动调用loadFinish方法。
     */
    protected abstract boolean loadData();
}
