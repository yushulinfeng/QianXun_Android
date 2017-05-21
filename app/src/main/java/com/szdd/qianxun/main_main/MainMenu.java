package com.szdd.qianxun.main_main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.szdd.qianxun.R;
import com.szdd.qianxun.message.friend.FriendList;
import com.szdd.qianxun.message.info.ShowAllInfo;
import com.szdd.qianxun.message.info.AnBaseInfo;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.ParamTool;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.more.MainSet;
import com.szdd.qianxun.start.forget.ForgetFind1;
import com.szdd.qianxun.tools.views.QianxunToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMenu extends Fragment {
    private MainMain activity;
    // 侧滑菜单
    private LinearLayout menu_head_layout;// 建议使用header写
    private SelectableRoundedImageView menu_head_icon;
    private TextView menu_head_name, menu_head_state;
    private ListView menu_list;
    private List<Map<String, Object>> menu_maps;
    private SimpleAdapter menu_adapter;
    private String[] menu_items = {"好友列表", "软件设置", "修改密码", "注销登录"};
    private int[] menu_icons = {R.drawable.icon_menu_friend, R.drawable.icon_menu_set,
            R.drawable.icon_menu_alert, R.drawable.icon_menu_logout};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = (MainMain) getActivity();
        View view = inflater.inflate(R.layout.main_menu, container, false);

        initView(view);
        initListener();

        // ////////测试专用//////////
        if (ParamTool.isTest())
            initTest();
        return view;
    }

    private void initView(View view) {
        menu_head_layout = (LinearLayout) view
                .findViewById(R.id.main_menu_head_layout);
        menu_head_icon = (SelectableRoundedImageView) view
                .findViewById(R.id.main_menu_head_icon);
        menu_head_name = (TextView) view.findViewById(R.id.main_menu_head_name);
        menu_head_state = (TextView) view
                .findViewById(R.id.main_menu_head_state);
        menu_list = (ListView) view.findViewById(R.id.main_menu_list);

        updateHeadView();
        initListView();
    }

    // 更新头像与昵称显示
    public void updateHeadView() {// 便于外部刷新
        updateHeadView(null, null);//使用缓存进行刷新
    }

    // 更新头像与昵称显示
    public void updateHeadView(Bitmap bitmap, String nickname) {// 便于外部刷新
        if (UserStateTool.isLoginEver(activity)) {// 只要登录过
            AnBaseInfo base_info = InfoTool.getBaseInfo(activity);// 已确保不会返回null
            String user_id = base_info.getUserId();
            if (null != user_id && !"".equals(user_id)) {// 本地有基本信息，就同步
                if (nickname == null)
                    nickname = base_info.getNickName();
                if (nickname == null || nickname.equals(""))
                    nickname = "加载中……";
                menu_head_name.setText(nickname);
                if (bitmap == null)
                    bitmap = base_info.getHeadIcon();
                if (bitmap != null)
                    menu_head_icon.setImageBitmap(bitmap);
                menu_head_state.setVisibility(View.VISIBLE);
                menu_head_state.setText("（在线）");
            }
            if (!UserStateTool.isLoginNow(activity)) {
                menu_head_state.setText("（离线）");
            }
        }
    }

    private void initListView() {
        menu_maps = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < menu_items.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("head", menu_icons[i]);
            item.put("name", menu_items[i]);
            if (i < 2)
                item.put("arrow", R.drawable.shape_null);
            else
                item.put("arrow", R.drawable.shape_null);
            menu_maps.add(item);
        }

        menu_adapter = new SimpleAdapter(activity, menu_maps,
                R.layout.main_menu_item,
                new String[]{"head", "name", "arrow"}, new int[]{
                R.id.main_menu_item_icon,
                R.id.main_menu_item_title,
                R.id.main_menu_item_arrow});//暂时取消图标
        menu_list.setAdapter(menu_adapter);
    }

    private void initListener() {
        menu_head_layout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                activity.showContent();// 收回菜单
                clickHeadIcon();
            }
        });
        menu_list.setOnItemClickListener(new OnItemClickListener() {// 只添加点击即可
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                activity.showContent();// 收回菜单
                clickListItem(position);
            }
        });
    }

    private void clickHeadIcon() {
        if (!UserStateTool.isLoginEver(activity)) {// 未登录
            UserStateTool.goToLogin(activity);
            return;
        }
        // 现在已登录，显示个人资料(静态方法即使传入Activity，也不能返回)
        Intent intent = new Intent(activity, ShowAllInfo.class);
        intent.putExtra("user_id", 0L);
        activity.startActivityForResult(intent, 0);
    }

    private void clickListItem(int position) {
        switch (position) {
            case 0:// 好友列表
                if (UserStateTool.isLoginEver(activity)) {
                    Intent intent = new Intent(activity, FriendList.class);
                    activity.startActivity(intent);
                } else {// 未登录
                    UserStateTool.goToLogin(activity);
                }
                break;
            case 1://  软件设置
                Intent intent = new Intent(activity, MainSet.class);
                activity.startActivity(intent);
                break;
            case 2://修改密码
                alterPass();
                break;
            case 3:// 退出登录
                logout();
                break;
        }
    }

    private void alterPass() {
        if (UserStateTool.isLoginNow(activity)) {
            Intent intent = new Intent(activity, ForgetFind1.class);// AlterPass
            intent.putExtra("normal_alter_phone",
                    InfoTool.getBaseInfo(getActivity()).getUserId());
            activity.startActivity(intent);
        } else {// 未登录
            UserStateTool.goToLogin(activity);
        }
    }

    private void logout() {
        if (UserStateTool.isLoginEver(activity)) {
            new AlertDialog.Builder(activity)
                    .setTitle("注销登录？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    UserStateTool.logout(activity);
                                }
                            }).setNegativeButton("取消", null).create().show();
        } else {
            showToast("您尚未登录");
            UserStateTool.logout(activity);
            UserStateTool.goToLogin(activity, false);
            activity.finish();
        }

    }

    private void showToast(String text) {
        QianxunToast.showToast(activity, text, QianxunToast.LENGTH_SHORT);
    }

    public void initTest() {
        activity.registerForContextMenu(menu_head_layout);
    }

}
