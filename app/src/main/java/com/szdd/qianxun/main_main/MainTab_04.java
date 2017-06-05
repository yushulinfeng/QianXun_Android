package com.szdd.qianxun.main_main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.AdPagerAdapter;
import com.szdd.qianxun.advertise.PictureFragment;
import com.szdd.qianxun.advertise.VideoFragment;
import com.szdd.qianxun.advertise.tools.AdPageTransformer;

import java.util.ArrayList;

public class MainTab_04 extends Fragment implements OnClickListener {
    private AdPagerAdapter adPager;
    private ViewPager pager;
    private Button ad_btn_picture;
    private Button ad_btn_video;
    private Context context;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) parent.removeView(rootView);
        } else {
            rootView = inflater.inflate(R.layout.tab_04, container, false);
            context = rootView.getContext();
            initAllView(rootView);
        }
        return rootView;
    }

    public void initAllView(View view) {
        ad_btn_picture = (Button) view.findViewById(R.id.ad_btn_picture);
        ad_btn_video = (Button) view.findViewById(R.id.ad_btn_video);

        ad_btn_picture.setOnClickListener(this);
        ad_btn_video.setOnClickListener(this);

        pager = (ViewPager) view.findViewById(R.id.ad_viewpager);

        final ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new PictureFragment());
        fragmentList.add(new VideoFragment());
        adPager = new AdPagerAdapter(getFragmentManager(), fragmentList);

        pager.setAdapter(adPager);
        pager.setPageTransformer(true, new AdPageTransformer());
        ad_btn_picture.setTextColor(getResources().getColor(R.color.topbar_bg));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ad_btn_video.setTextColor(getResources().getColor(R.color.black));
                        ad_btn_picture.setTextColor(getResources().getColor(R.color.topbar_bg));
                        break;
                    case 1:
                        ad_btn_picture.setTextColor(getResources().getColor(R.color.black));
                        ad_btn_video.setTextColor(getResources().getColor(R.color.topbar_bg));
                        break;

                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ad_btn_picture:
                pager.setCurrentItem(0, true);
                break;
            case R.id.ad_btn_video:
                pager.setCurrentItem(1, true);
                break;
        }
    }
}
