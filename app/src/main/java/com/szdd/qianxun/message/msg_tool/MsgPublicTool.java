package com.szdd.qianxun.message.msg_tool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.szdd.qianxun.advertise.ZoomImage;
import com.szdd.qianxun.bank.MainTab_03_RequestDetailActivity;
import com.szdd.qianxun.dynamic.DynamicDetail;
import com.szdd.qianxun.message.baichuan.mine.BaiChuanUtils;
import com.szdd.qianxun.message.friend.FriendListTool;
import com.szdd.qianxun.message.homepage.MineHomePage;
import com.szdd.qianxun.message.info.ShowAllInfo;
import com.szdd.qianxun.sell.orders.detail.ServiceDetailActivity;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;

/**
 * 消息-公共工具
 */
public class MsgPublicTool {

    public static void chartTo(final Context context, final long user_name) {

    }

    public static void chartTo(final Context context,
                               final String id,
                               final long user_name) {
        try {
            chartTo(context, Integer.parseInt(id), user_name);
        } catch (Exception e) {
        }
    }

    public static void chartTo(final Context context,
                               final String id,
                               final long user_name, String service) {
        try {
            chartTo(context, Integer.parseInt(id), user_name, service);
        } catch (Exception e) {
        }
    }

    /**
     * 跳转到聊天界面（参数：对方的手机号） <br/>
     * 未登录将跳转到登录界面
     */
    public static void chartTo(final Context context, final int id, final long user_name) {
        if (UserStateTool.isLoginNow(context)) {
            FriendListTool.addOneFriend(context, id, user_name);//
            BaiChuanUtils.chartTo(context, id, user_name + "");
        }
    }
    public static void chartToNotSave(final Context context, final int id, final long user_name) {
        if (UserStateTool.isLoginNow(context)) {
            BaiChuanUtils.chartTo(context, id, user_name + "");
        }
    }

    public static void chartTo(final Context context, final int id,
                               final long user_name, String service) {
        if (!UserStateTool.isLoginEver(context)) {
            UserStateTool.goToLogin(context);
            return;
        }
        if (UserStateTool.isLoginNow(context)) {
            if(InfoTool.getUserID(context).equals(id+"")){
                QianxunToast.showToast(context,"您不能与自己聊天",QianxunToast.LENGTH_SHORT);
                return;
            }
            FriendListTool.addOneFriend(context, id, user_name);//
            BaiChuanUtils.chartTo(context, id, user_name + "", service);
        }else{
            QianxunToast.showToast(context,"离线状态不能聊天",QianxunToast.LENGTH_SHORT);
            return;
        }
    }

    /**
     * 跳转到用户信息界面
     */
    public static void showAllInfo(Context context, long user_id) {
        if (user_id == 0) {// 显示我的资料，代码暂时相同
            Intent intent = new Intent(context, ShowAllInfo.class);
            intent.putExtra("user_id", user_id);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, ShowAllInfo.class);
            intent.putExtra("user_id", user_id);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转个人主页
     */
    public static void showHomePage(Context context, long userId) {
        showHomePage(context, userId + "");
    }

    public static void showHomePage(Context context, String userId) {
        if(!UserStateTool.isLoginEver(context)){
            UserStateTool.goToLogin(context);
            return;
        }
        if ("0".equals(userId)) {// 显示我的资料
            Intent intent = new Intent(context, MineHomePage.class);
            intent.putExtra("userId", InfoTool.getUserID(context));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, MineHomePage.class);
            intent.putExtra("userId", userId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void showHomePageByName(final Context context,
                                          final long user_name) {
        showHomePageByName(context, user_name + "");
    }

    public static void showHomePageByName(final Context context,
                                          final String user_name) {
        if (user_name == null || user_name.equals("")) {
            return;
        }
        if (user_name.equals(InfoTool.getUserName(context))) {
            showHomePage(context, 0);
            return;
        }
        StaticMethod.POST(context, ServerURL.GET_ID_BY_NAME, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("username", user_name);
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                if (response == null || response.equals("")) {
                    return;
                }
                if (response.equals("-3")) {
                    return;
                }
                showHomePage(context, response);
            }
        });

    }

    /**
     * 跳转动态
     */
    public static void showDynamicDetail(Context context, long dynamicId) {
        Intent intent = new Intent(context, DynamicDetail.class);
        intent.putExtra("dynamicId", dynamicId);
        context.startActivity(intent);
    }

    /**
     * 跳转放大图片(原图)
     */
    public static void zoomImage(Context context, Bitmap bitmap) {
        Intent intent = new Intent(context, ZoomImage.class);
        intent.putExtra("bitmap", bitmap);
        context.startActivity(intent);
    }

    /**
     * 跳转放大图片(url)
     */
    public static void zoomImage(Context context, String url) {
        Intent intent = new Intent(context, ZoomImage.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    /**
     * 跳转到服务详情
     */
    public static void showServiceDetail(Context context, String serviceId) {
        Intent intent = new Intent(context, ServiceDetailActivity.class);
        intent.putExtra("serviceId", serviceId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转到需求详情
     */
    public static void showRequestDetail(Context context, String requestId, String fatherA) {
        Intent intent = new Intent(context, MainTab_03_RequestDetailActivity.class);
        intent.putExtra("requestId", Integer.valueOf(requestId));
        intent.putExtra("fatherA", fatherA);
        context.startActivity(intent);
    }
}
