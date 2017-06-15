package com.szdd.qianxun.tools.top;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.ParamTool;
import com.szdd.qianxun.tools.views.QianxunToast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class TActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate();
    }

    // ////////允许子类调用的方法
    public void setTitle(String title) {
        ((TextView) findViewById(R.id.top_text)).setText(title);
        // 由于未知原因的标题栏颜色问题，此处只能这样处理了
        setTopColors(R.color.start_topbar_bg);
    }

    public void setTopColors(int top_color) {// 用setTopColors不行，有编译检查
        try {
            LinearLayout top_layout = (LinearLayout) findViewById(R.id.top_layout);
            top_layout.setBackgroundColor(getResources().getColor(top_color));
        } catch (Exception e) {
        }
    }

    public void showBackButton() {
        ImageButton top_back = (ImageButton) findViewById(R.id.top_back);
        top_back.setVisibility(ImageButton.VISIBLE);
        top_back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showMenuButton() {
        ImageButton top_menu = (ImageButton) findViewById(R.id.top_menu);
        top_menu.setVisibility(ImageButton.VISIBLE);
        top_menu.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showContextMenu();
            }
        });
    }

    public void initActionBar(int color) {
        initActionBar(this, color);
    }

    public static void initKeyBoard(Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    // ////////允许子类调用的方法-辅助
    public void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }

    // 子类只需要实现即可的方法/////////////////////////////////////////////////////////////

    /**
     * 相当于原来的OnCreate()，直接从setContentView开始写即可
     */
    public abstract void onCreate();

    /**
     * 显示弹出式菜单
     */
    public abstract void showContextMenu();

    //静态方法/////////////////////////////////////////////////////////////////////////

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void initActionBar(Activity activity, int color) {
        // 在线参数
        String value = ParamTool.getParam("show_top");
        if (value.equals("0"))
            return;
        // 检查系统版本
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return;
        // 透明化状态栏
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 添加透明状态栏
        int resourceId = activity.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {// 状态栏高度获取成功
            int statusBarHeight = activity.getResources()
                    .getDimensionPixelSize(resourceId);
            LinearLayout view = null;
            try {
                view = (LinearLayout) activity.findViewById(R.id.top_titlebar);
            } catch (Exception e) {
                // 清除透明
                activity.getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                return;
            }
            View statusBarView = new View(activity);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    statusBarHeight);
            statusBarView.setLayoutParams(params);
            statusBarView.setBackgroundColor(color);

            view.addView(statusBarView);
        }
    }

    public static void addActionBar(Activity activity, int color_res) {
        SystemBarTintManager.initSystemStatusBar(activity, color_res);
    }

    /**
     * 只支持MIUI<br>
     * 0--只需要状态栏透明； 1-状态栏透明且黑色字体； 2-清除黑色字体
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void setStatusBarTextColor(Activity context, int type) {
        Window window = context.getWindow();
        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class
                    .forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams
                    .getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);
            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
                    int.class);
            if (type == 0) {
                extraFlagField.invoke(window, tranceFlag, tranceFlag);// 只需要状态栏透明
            } else if (type == 1) {
                extraFlagField.invoke(window, tranceFlag | darkModeFlag,
                        tranceFlag | darkModeFlag);// 状态栏透明且黑色字体
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);// 清除黑色字体
            }
        } catch (Exception e) {
        }
    }
}
