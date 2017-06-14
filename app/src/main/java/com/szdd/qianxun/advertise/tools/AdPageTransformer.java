package com.szdd.qianxun.advertise.tools;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by linorz on 2016/1/24.
 */
public class AdPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View view, float position) {
        if (position <= 0) {
            //从右向左滑动为当前View

            //设置旋转中心点；
            ViewHelper.setPivotX(view, view.getMeasuredWidth());
            ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);

            //只在Y轴做旋转操作
            ViewHelper.setRotationY(view, 90f * position);
        } else if (position <= 1) {
            //从左向右滑动为当前View
            ViewHelper.setPivotX(view, 0);
            ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
            ViewHelper.setRotationY(view, 90f * position);
        }
    }


//    private static final float ROT_MAX = 20.0f;
//    private float mRot;
//
//    public void transformPage(View view, float position) {
//
//        Log.e("TAG", view + " , " + position + "");
//
//        if (position < -1) { // [-Infinity,-1)
//            // This page is way off-screen to the left.
//            ViewHelper.setRotation(view, 0);
//
//        } else if (position <= 1) // a页滑动至b页 ； a页从 0.0 ~ -1 ；b页从1 ~ 0.0
//        { // [-1,1]
//            // Modify the default slide transition to shrink the page as well
//            if (position < 0) {
//
//                mRot = (ROT_MAX * position);
//                ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
//                ViewHelper.setPivotY(view, view.getMeasuredHeight());
//                ViewHelper.setRotation(view, mRot);
//            } else {
//
//                mRot = (ROT_MAX * position);
//                ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
//                ViewHelper.setPivotY(view, view.getMeasuredHeight());
//                ViewHelper.setRotation(view, mRot);
//            }
//
//            // Scale the page down (between MIN_SCALE and 1)
//
//            // Fade the page relative to its size.
//
//        } else { // (1,+Infinity]
//            // This page is way off-screen to the right.
//            ViewHelper.setRotation(view, 0);
//        }
//    }


//    private static final float MIN_SCALE = 0.75f;
//
//    public void transformPage(View view, float position) {
//        int pageWidth = view.getWidth();
//
//        if (position < -1) { // [-Infinity,-1)
//            // This page is way off-screen to the left.
//            view.setAlpha(0);
//
//        } else if (position <= 0) { // [-1,0]
//            // Use the default slide transition when moving to the left page
//            view.setAlpha(1);
//            view.setTranslationX(0);
//            view.setScaleX(1);
//            view.setScaleY(1);
//
//        } else if (position <= 1) { // (0,1]
//            // Fade the page out.
//            view.setAlpha(1 - position);
//
//            // Counteract the default slide transition
//            view.setTranslationX(pageWidth * -position);
//
//            // Scale the page down (between MIN_SCALE and 1)
//            float scaleFactor = MIN_SCALE
//                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);
//
//        } else { // (1,+Infinity]
//            // This page is way off-screen to the right.
//            view.setAlpha(0);
//        }
//    }
}