package com.szdd.qianxun.main_main;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;

public class MainBottom {
    private Activity activity;
    // 底部标签（LinearLayout包含iv与tv）
    private LinearLayout tab_linear_01, tab_linear_02, tab_linear_03,
            tab_linear_04, tab_linear_05;
    private TextView tab_tv_01, tab_tv_02, tab_tv_03, tab_tv_04, tab_tv_05;
    private ImageView tab_iv_01, tab_iv_02, tab_iv_03, tab_iv_04, tab_iv_05;
    private ImageView tab_iv_add;

    public MainBottom(Activity activity) {
        this.activity = activity;

        initView();
    }

    private void initView() {
        tab_linear_01 = (LinearLayout) activity
                .findViewById(R.id.tab_linear_01);
        tab_linear_02 = (LinearLayout) activity
                .findViewById(R.id.tab_linear_02);
        tab_linear_03 = (LinearLayout) activity
                .findViewById(R.id.tab_linear_03);
        tab_linear_04 = (LinearLayout) activity
                .findViewById(R.id.tab_linear_04);
        tab_linear_05 = (LinearLayout) activity
                .findViewById(R.id.tab_linear_05);

        tab_tv_01 = (TextView) activity.findViewById(R.id.tab_tv_01);
        tab_tv_02 = (TextView) activity.findViewById(R.id.tab_tv_02);
        tab_tv_03 = (TextView) activity.findViewById(R.id.tab_tv_03);
        tab_tv_04 = (TextView) activity.findViewById(R.id.tab_tv_04);
        tab_tv_05 = (TextView) activity.findViewById(R.id.tab_tv_05);

        tab_iv_01 = (ImageView) activity.findViewById(R.id.tab_iv_01);
        tab_iv_02 = (ImageView) activity.findViewById(R.id.tab_iv_02);
        tab_iv_03 = (ImageView) activity.findViewById(R.id.tab_iv_03);
        tab_iv_04 = (ImageView) activity.findViewById(R.id.tab_iv_04);
        tab_iv_05 = (ImageView) activity.findViewById(R.id.tab_iv_05);
        tab_iv_add = (ImageView) activity.findViewById(R.id.tab_iv_add);
    }

    public void setClickListener(OnClickListener listener) {
        tab_linear_01.setOnClickListener(listener);
        tab_linear_02.setOnClickListener(listener);
        tab_linear_03.setOnClickListener(listener);
        tab_linear_04.setOnClickListener(listener);
        tab_linear_05.setOnClickListener(listener);
        tab_iv_add.setOnClickListener(listener);
    }

    public void transAddImage() {
        tab_iv_add.setImageResource(R.drawable.icon_add_normal);
    }

    public void restoreAddImage() {
        /** 设置旋转动画 */
        RotateAnimation animation = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);//设置动画持续时间
        /** 开始动画 */
        tab_iv_add.startAnimation(animation);
    }


    public void setCurrentTab(int position) {
        resetTextView();
        switch (position) {
            case 0:
                tab_tv_01.setTextColor(activity.getResources().getColor(
                        R.color.tab_btn_light));
                tab_iv_01.setImageResource(R.drawable.tab_01_btn_light);
                break;
            case 1:
                tab_tv_02.setTextColor(activity.getResources().getColor(
                        R.color.tab_btn_light));
                tab_iv_02.setImageResource(R.drawable.tab_02_btn_light);
                break;
            case 2:
                tab_tv_03.setTextColor(activity.getResources().getColor(
                        R.color.tab_btn_light));
                tab_iv_03.setImageResource(R.drawable.tab_03_btn_light);
                break;
            case 3:
                tab_tv_04.setTextColor(activity.getResources().getColor(
                        R.color.tab_btn_light));
                tab_iv_04.setImageResource(R.drawable.tab_04_btn_light);
                break;
            case 4:
                tab_tv_05.setTextColor(activity.getResources().getColor(
                        R.color.tab_btn_light));
                tab_iv_05.setImageResource(R.drawable.tab_05_btn_light);
                break;
        }
    }

    // 重置颜色
    private void resetTextView() {
        tab_tv_01.setTextColor(activity.getResources().getColor(
                R.color.tab_btn_dark));
        tab_tv_02.setTextColor(activity.getResources().getColor(
                R.color.tab_btn_dark));
        tab_tv_03.setTextColor(activity.getResources().getColor(
                R.color.tab_btn_dark));
        tab_tv_04.setTextColor(activity.getResources().getColor(
                R.color.tab_btn_dark));
        tab_tv_05.setTextColor(activity.getResources().getColor(
                R.color.tab_btn_dark));
        tab_iv_01.setImageResource(R.drawable.tab_01_btn_dark);
        tab_iv_02.setImageResource(R.drawable.tab_02_btn_dark);
        tab_iv_03.setImageResource(R.drawable.tab_03_btn_dark);
        tab_iv_04.setImageResource(R.drawable.tab_04_btn_dark);
        tab_iv_05.setImageResource(R.drawable.tab_05_btn_dark);
    }
}
