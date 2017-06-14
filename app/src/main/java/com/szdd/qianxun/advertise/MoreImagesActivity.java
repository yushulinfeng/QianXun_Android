package com.szdd.qianxun.advertise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.customview.CustomSurfaceView;

import java.util.ArrayList;


/**
 * Created by linorz on 2016/5/31.
 */
public class MoreImagesActivity extends Activity {
    private Context context;
    private ViewPager viewPager;
    private ArrayList<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_img_activity);
        context = this;
        findViewById(R.id.zoom_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.img_viewpager);
        Intent intent = getIntent();
        ArrayList<String> urls = intent.getStringArrayListExtra("urls");

        views = new ArrayList<>();
        for (String url : urls) views.add(getZoomView(url));

        viewPager.setAdapter(new MoreImagesAdapter());

        viewPager.setCurrentItem(intent.getIntExtra("num", 0));
    }

    private View getZoomView(String imgurl) {
        final CustomSurfaceView csv = new CustomSurfaceView(context,null);
        csv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ImageLoader.getInstance().loadImage(imgurl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                csv.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_img_load_fail));
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                csv.setBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                csv.setBitmap(null);
            }
        });

        return csv;
    }

    class MoreImagesAdapter extends PagerAdapter {
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
}
