package com.szdd.qianxun.main_main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.szdd.qianxun.R;
import com.szdd.qianxun.message.friend.FriendList;
import com.szdd.qianxun.message.info.AnBaseInfo;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.ManagerTool;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.more.FeedBack;
import com.szdd.qianxun.more.MainSet;
import com.szdd.qianxun.more.MyCollect;
import com.szdd.qianxun.more.MyPublish;
import com.szdd.qianxun.more.RequestMy;
import com.szdd.qianxun.more.RequestTa;
import com.szdd.qianxun.more.UserVerify;
import com.szdd.qianxun.sell.CashBalanceActivity;
import com.szdd.qianxun.sell.orders.SellAllOrderBuy;
import com.szdd.qianxun.sell.orders.SellAllOrderSell;
import com.szdd.qianxun.tools.bitmap.MultiBitmapTool;
import com.szdd.qianxun.tools.views.QianxunToast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainTab_05 extends Fragment implements OnClickListener {
    private static final int MAIN_TAB_CODE = 0;
    private LinearLayout menu_head_layout;
    private SelectableRoundedImageView menu_head_icon;
    private ImageView menu__head_sex;
    private TextView menu_head_name, menu_head_text;
    private TextView tv_verify_id, tv_verify_stu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_05, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        initTopItem(view);
    }

    private void initTopItem(View view) {
        menu_head_layout = (LinearLayout) view
                .findViewById(R.id.tab05_head_layout);
        menu_head_icon = (SelectableRoundedImageView) view
                .findViewById(R.id.tab05_head_icon);
        menu_head_name = (TextView) view.findViewById(R.id.tab05_head_name);
        menu_head_text = (TextView) view.findViewById(R.id.tab05_head_text);
        menu__head_sex = (ImageView) view.findViewById(R.id.tab05_head_sex);
        tv_verify_id = (TextView) view.findViewById(R.id.tab05_tv_verify_id);
        tv_verify_stu = (TextView) view.findViewById(R.id.tab05_tv_verify_stu);
        menu_head_layout.setOnClickListener(this);
        menu_head_icon.setOnClickListener(this);
        menu_head_name.setOnClickListener(this);
        menu_head_text.setOnClickListener(this);
        updateHead();
    }

    public void updateHead() {
        updateHead(null, null, null);//之后会再次刷新
    }

    public void updateHead(Bitmap bitmap, String nickname, Integer verify_state) {
        if(menu_head_layout==null)
            return;
        // 登录过，则刷新数据
        if (UserStateTool.isLoginEver(getActivity())) {
            AnBaseInfo base_info = InfoTool.getBaseInfo(getActivity());// 已确保不会返回null
            String user_id = base_info.getUserId();
            if (null != user_id && !"".equals(user_id)) {// 本地有基本信息，就同步
                if (nickname == null)
                    nickname = base_info.getNickName();
                menu_head_name.setText(nickname + "");
                if (bitmap == null)
                    bitmap = base_info.getHeadIcon();
                if (bitmap != null)
                    menu_head_icon.setImageBitmap(bitmap);
                menu__head_sex.setImageResource("女".equals(base_info.getGender()) ? R.drawable.girl : R.drawable.boy);
                // menu_head_text.setText("点击查看个人资料");
                menu_head_text.setText(base_info.getLocation() + "");

                if (verify_state == null) {
                    try {
                        verify_state = InfoTool.getUserInfo(getActivity()).getVerifyStatus();
                    } catch (Exception e) {
                        verify_state = 0;
                    }
                }

                //认证状态
                switch (verify_state) {
                    default:
                    case 0:
                        tv_verify_id.setVisibility(View.VISIBLE);
                        tv_verify_id.setText("未认证");
                        break;
                    case 1:
                    case -2:
                        tv_verify_id.setVisibility(View.VISIBLE);
                        tv_verify_stu.setVisibility(View.GONE);
                        break;
                    case 2:
                        tv_verify_id.setVisibility(View.VISIBLE);
                        tv_verify_stu.setVisibility(View.VISIBLE);
                        break;
                }
            }
        } else {
            tv_verify_id.setVisibility(View.GONE);
            tv_verify_stu.setVisibility(View.GONE);
            menu_head_text.setVisibility(View.VISIBLE);
            menu_head_name.setText("点击登录");
            if(ManagerTool.isManagerLogin())
                menu_head_name.setText("管理员");
        }
    }

    // // 暂时保留这个方法吧
    // private void initFloatBtnView(View view) {
    // DisplayMetrics outMetrics = new DisplayMetrics();
    // getActivity().getWindow().getWindowManager().getDefaultDisplay()
    // .getMetrics(outMetrics);
    // int screen_width = outMetrics.widthPixels;
    // // int screen_hight = outMetrics.heightPixels;
    // int width = AndTools.dp2px(getActivity(), 280);
    // int height = AndTools.dp2px(getActivity(), 50);
    // int pad = AndTools.dp2px(getActivity(), 10);
    //
    // FrameLayout framelayout = (FrameLayout) view
    // .findViewById(R.id.tab05_layout);
    // btn_version = new RandomFloatView(getActivity());
    // btn_version.setText("检查更新");
    // btn_version.setGravity(Gravity.CENTER);
    // btn_version.setTextColor(Color.WHITE);
    // btn_version.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    // btn_version.setBackgroundResource(R.drawable.btn_bg_orange);
    // btn_version.setLayoutParams(new LayoutParams(width, height));
    // btn_version.initView(framelayout, (screen_width - width) / 2, pad,
    // false);
    // btn_version.startRandomMove(pad / 2, 0);
    // btn_version.setOnClickListener(this);
    // btn_version.setTouchEnable(false);
    // }

    private void showToast(String text) {
        QianxunToast.showToast(getActivity(), text, QianxunToast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tab05_head_layout:
            case R.id.tab05_head_icon:
            case R.id.tab05_head_name:
            case R.id.tab05_head_text:
                if (UserStateTool.isLoginEver(getActivity())) {
                    MsgPublicTool.showHomePage(getActivity(), InfoTool.getUserID(getActivity()));
                } else {
                    UserStateTool.goToLogin(getActivity());
                }
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //界面按钮点击(需要验证登录状态)
    @OnClick({R.id.tab05_ibtn_request_my,
            R.id.tab05_ibtn_request_ta,
            R.id.tab05_ibtn_collect_people, R.id.tab05_tv_collect_people,
            R.id.tab05_ibtn_collect, R.id.tab05_tv_collect, R.id.tab05_ibtn_buy,
            R.id.tab05_ibtn_sell, R.id.tab05_tv_sell,
            R.id.tab05_ibtn_publish, R.id.tab05_tv_publish,
            R.id.tab05_ibtn_verify, R.id.tab05_ibtn_cash
    })
    public void onButtonClick(View view) {
        if (!UserStateTool.isLoginEver(getActivity())) {
            UserStateTool.goToLogin(getActivity());
            return;
        }
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tab05_ibtn_request_my:
                intent = new Intent(getActivity(), RequestMy.class);
                break;
            case R.id.tab05_ibtn_request_ta:
                intent = new Intent(getActivity(), RequestTa.class);
                break;
            case R.id.tab05_ibtn_collect_people:
            case R.id.tab05_tv_collect_people:
                intent = new Intent(getActivity(), FriendList.class);
                intent.putExtra("is_collected", true);
                break;
            case R.id.tab05_ibtn_buy:
                intent = new Intent(getActivity(), SellAllOrderBuy.class);
                break;
            case R.id.tab05_ibtn_collect:
            case R.id.tab05_tv_collect:
                intent = new Intent(getActivity(), MyCollect.class);
                break;
            case R.id.tab05_ibtn_sell:
            case R.id.tab05_tv_sell:
                intent = new Intent(getActivity(), SellAllOrderSell.class);
                break;
            case R.id.tab05_ibtn_publish:
            case R.id.tab05_tv_publish:
                intent = new Intent(getActivity(), MyPublish.class);
                break;
            case R.id.tab05_ibtn_verify:
                intent = new Intent(getActivity(), UserVerify.class);
                break;
            case R.id.tab05_ibtn_cash:
                if (!UserStateTool.isLoginNow(getActivity())) {
                    showToast("离线状态不能查看");
                    return;
                }
                intent = new Intent(getActivity(), CashBalanceActivity.class);
                break;
        }
        if (intent != null)
            startActivityForResult(intent, MAIN_TAB_CODE);
    }

    //界面按钮点击（无需登录）
    @OnClick({R.id.tab05_ibtn_set, R.id.tab05_ibtn_phone})
    public void onNormalButtonClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tab05_ibtn_set:
                intent = new Intent(getActivity(), MainSet.class);
                break;
            case R.id.tab05_ibtn_phone:
                intent = new Intent(getActivity(), FeedBack.class);
                ///////////////////////////////
//                MultiBitmapTool.startBitmapChoose(this, 0, 9, null);
                break;
        }
        if (intent != null)
            startActivityForResult(intent, MAIN_TAB_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAIN_TAB_CODE && resultCode == MainSet.LOGOUT_CODE) {
            getActivity().finish();
        }
        ArrayList<String> img_array = MultiBitmapTool.getBitmapList(resultCode, data);
        if (img_array != null && img_array.size() != 0) {
            Log.e("EEEEEEEEEEEEE", img_array.size() + "");
            for (int i = 0; i < img_array.size(); i++) {
                Log.e("EEEEEEEEEEEEE", img_array.get(i) + "");
            }
            MultiBitmapTool.startBitmapChoose(this, 0, 9, img_array);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}