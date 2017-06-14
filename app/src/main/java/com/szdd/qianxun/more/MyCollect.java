package com.szdd.qianxun.more;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.top.FragmentTActivity;

public class MyCollect extends FragmentTActivity {
    private MyCollectFragment fragment;

    @Override
    public void onCreate() {
        setContentView(R.layout.more_my_collect);
        setTitle("收藏");
        showBackButton();
        initActionBar(getResources().getColor(R.color.topbar_bg));

        initView();
    }

    private void initView() {
        fragment = new MyCollectFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.more_my_collect_fragment, fragment,
                        "MyCollectFragment").commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fragment.onListRefresh();
    }

    @Override
    public void showContextMenu() {
    }

}
