package com.szdd.qianxun.main_main;

import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.info.AnBaseInfo;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.slidemenu.app.SlidingFragmentActivity;

//专为MainMain开发
public abstract class MainTActivity extends SlidingFragmentActivity {
    private View statusBarView = null;// 手动保证调用一次
    private int cur_page = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate();
//        //通知栏一体化（注：此处slideMenu会自行处理顶部，确保不会错位）
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //涟漪点击效果（需要在setContentView之后调用，否则在onAtacheView会拉伸）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setTheme(android.R.style.Theme_Material_Light_NoActionBar);
    }

//    protected void showMsgPush(boolean need_show) {
//        LinearLayout layout = (LinearLayout) findViewById(R.id.top_msg_layout);
//        if (!need_show) {
//            layout.setVisibility(View.GONE);
//            return;
//        }
//        Button btn_msg = (Button) findViewById(R.id.top_msg_msg);
//        Button btn_push = (Button) findViewById(R.id.top_msg_push);
//        btn_msg.setEnabled(false);
//        btn_push.setEnabled(true);
//        layout.setVisibility(View.VISIBLE);
//    }

    // ////////允许子类调用的方法
    protected void setTopPosition(int position) {// 此处不要用全局变量
        TextView tv_title = (TextView) findViewById(R.id.top_text);
        String[] text_title = {"消息", "口袋掌柜", "附近需求", "成果", "我的"};
        if (position < 0 || position > 4) {
            tv_title.setText("高能");
            return;
        }
        tv_title.setText(text_title[position]);
        cur_page = position;

        // int bg_color = 0;
        // int text_color = 0;
        //
        // switch (position) {
        // case 0:
        // bg_color = getResources().getColor(R.color.topbar_bg_1);
        // text_color = getResources().getColor(R.color.topbar_text_1);
        // break;
        // case 1:
        // bg_color = getResources().getColor(R.color.topbar_bg_2);
        // text_color = getResources().getColor(R.color.topbar_text_2);
        // break;
        // case 2:
        // bg_color = getResources().getColor(R.color.topbar_bg_3);
        // text_color = getResources().getColor(R.color.topbar_text_3);
        // break;
        // case 3:
        // bg_color = getResources().getColor(R.color.topbar_bg_4);
        // text_color = getResources().getColor(R.color.topbar_text_4);
        // break;
        // case 4:
        // bg_color = getResources().getColor(R.color.topbar_bg_5);
        // text_color = getResources().getColor(R.color.topbar_text_5);
        // break;
        // default:
        // break;
        // }
        // if (bg_color == 0 || text_color == 0)
        // return;
        // LinearLayout top_layout = (LinearLayout)
        // findViewById(R.id.top_layout);
        // top_layout.setBackgroundColor(bg_color);
        // tv_title.setTextColor(text_color);
    }

    protected void initHeadIcon(OnClickListener listener) {
        ImageView top_head = (ImageView) findViewById(R.id.top_head);
        top_head.setOnClickListener(listener);
        // 更新头像
        if (UserStateTool.isLoginEver(this)) {// 只要登录过
            AnBaseInfo base_info = InfoTool.getBaseInfo(this);// 已确保不会返回null
            String user_id = base_info.getUserId();
            if (null != user_id && !"".equals(user_id)) {// 本地有基本信息，就同步
                top_head.setImageBitmap(base_info.getHeadIcon());
            }
        }
    }

    protected void showMenuButton() {
        Button top_menu = (Button) findViewById(R.id.top_menu);
        top_menu.setVisibility(ImageButton.VISIBLE);
        top_menu.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showContextMenu(v, cur_page);
            }
        });

//        Button top_location = (Button) findViewById(R.id.top_location);
//        top_location.setVisibility(ImageButton.VISIBLE);
//        TextView top_location_text = (TextView) findViewById(R.id.top_location_text);
//        top_location_text.setVisibility(ImageButton.VISIBLE);
//        TextView top_title = (TextView) findViewById(R.id.top_text);
//        top_title.setVisibility(ImageButton.VISIBLE);
    }

    protected void hideMenuButton() {
        Button top_menu = (Button) findViewById(R.id.top_menu);
        top_menu.setVisibility(ImageButton.INVISIBLE);

//        Button top_location = (Button) findViewById(R.id.top_location);
//        top_location.setVisibility(ImageButton.INVISIBLE);
//        TextView top_location_text = (TextView) findViewById(R.id.top_location_text);
//        top_location_text.setVisibility(ImageButton.INVISIBLE);
//        TextView top_title = (TextView) findViewById(R.id.top_text);
//        top_title.setVisibility(ImageButton.INVISIBLE);
    }

    // ////////允许子类调用的方法-辅助
    protected void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }

    // 子类只需要实现即可的方法/////////////////////////////////////////////////////////////

    /**
     * 相当于原来的OnCreate()，直接从setContentView开始写即可
     */
    protected abstract void onCreate();

    /**
     * 显示弹出式菜单
     */
    protected abstract void showContextMenu(View v, int cur_page);

    // ////////////////////////////显示popup menu///////////////////////////////
    private void showContextMenuTest(View v, int cur_page) {
        View popupWindow_view = getLayoutInflater().inflate(
                R.layout.tab_03_popbutton_delect, null, false);
        final PopupWindow popupwindow_search_reward = new PopupWindow(
                popupWindow_view, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, true);
        popupwindow_search_reward.showAsDropDown(v);

        TextView popbt_delect_01 = (TextView) popupWindow_view
                .findViewById(R.id.popbt_delect_01);
        popbt_delect_01.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                popupwindow_search_reward.dismiss();
            }
        });
        popupWindow_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow_search_reward != null
                        && popupwindow_search_reward.isShowing()) {
                    popupwindow_search_reward.dismiss();
                }
                return false;
            }
        });
    }

}
