package com.szdd.qianxun.message.baichuan.util;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import com.szdd.qianxun.R;
import com.szdd.qianxun.main_main.MyApplication;
import com.szdd.qianxun.tools.dialog.DialogProgress;

import java.util.List;

/**
 * 工具类 Created by zhongqian.wzq on 2014/11/1.
 */
public class AppUtil {
    private static DialogProgress mProgressDialog;
    private static String mNotifyTitle;
    private static PendingIntent mPendIntent;
    private static Notification mNotification;
    private static NotificationManager mNotificationManager;

    /**
     * 显示进度对话框
     *
     * @param context
     * @param title
     */
    public synchronized static void showProgressDialog(Context context,
                                                       String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new DialogProgress(context);
        }
        if (!TextUtils.isEmpty(title))
            mProgressDialog.setTitleNew(title);
//		mProgressDialog.setText("请稍等……");
        mProgressDialog.setText(title);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    /**
     * 显示进度对话框，不可取消版
     *
     * @param context
     * @param title
     */
    public synchronized static void showProgressDialogNotCancel(
            Context context, String title) {
        showProgressDialog(context, title);
        mProgressDialog.setCancelable(false);
    }

    /**
     * 关闭进度对话框
     */
    public synchronized static void dismissProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    /**
     * 创建并显示一个只包含“是”与“否”按钮简单对话框
     *
     * @param context
     * @param title
     * @param callback
     */
    public static void showAlertDialog(final Context context,
                                       final String title, final DialogCallback callback) {
        new AlertDialog.Builder(context).setTitle(title)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onPositive();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 点击对话框确定按钮后的回调接口
     */
    public interface DialogCallback {
        public void onPositive();
    }

    /**
     * 检测当前App是否在前台运行
     *
     * @param context
     * @return true 前台运行，false 后台运行
     */
    public static boolean isAppForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager
                .getRunningTasks(Integer.MAX_VALUE);

        // 正在运行的应用
        ActivityManager.RunningTaskInfo foregroundTask = runningTasks.get(0);
        String packageName = foregroundTask.topActivity.getPackageName();
        String myPackageName = context.getPackageName();

        // 比较包名
        return packageName.equals(myPackageName);
    }

    /**
     * 发出有新消息通知
     *
     * @param note_title 消息标题
     * @param note_text  消息内容
     * @param cls        要打开的类
     * @param note_extra 要添加的数据（键值note_extra）
     */
    @SuppressWarnings("deprecation")
    public static void sendNote(String note_title, String note_text,
                                Class<?> cls, String note_extra) {
        Context ctx = MyApplication.getInstance();
        NotificationManager mNotificationManager = (NotificationManager) ctx
                .getSystemService(Service.NOTIFICATION_SERVICE);
        Notification mNotification = new Notification();
        mNotification.icon = R.drawable.ic_launcher; // 设置图标，公用图标
        mNotification.tickerText = ctx.getString(R.string.app_name);
        mNotification.defaults = Notification.DEFAULT_ALL;// 全部默认
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;// 点击清除
        if (TextUtils.isEmpty(note_title))
            note_title = ctx.getString(R.string.app_name);
        PendingIntent mPendIntent = null;
        if (cls != null) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(ctx.getPackageName(), cls.getName());
            if (note_extra != null)
                intent.putExtra("note_extra", note_extra);
            mPendIntent = PendingIntent.getActivity(ctx, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        mNotification.setLatestEventInfo(ctx, note_title, note_text,
                mPendIntent);
        mNotification.when = System.currentTimeMillis(); // 当前时间 ，通知时间
        mNotificationManager.notify(1, mNotification);
    }

    @SuppressWarnings("deprecation")
    public static void sendNote(String note_title, String note_text) {
        Context ctx = MyApplication.getInstance();
        NotificationManager mNotificationManager = (NotificationManager) ctx
                .getSystemService(Service.NOTIFICATION_SERVICE);
        Notification mNotification = new Notification();
        mNotification.icon = R.drawable.ic_launcher; // 设置图标，公用图标
        mNotification.tickerText = ctx.getString(R.string.app_name);
        mNotification.defaults = Notification.DEFAULT_LIGHTS;// 全部默认
        mNotification.flags = Notification.FLAG_NO_CLEAR;// 点击不清除
        if (TextUtils.isEmpty(note_title))
            note_title = ctx.getString(R.string.app_name);
        mNotification.when = System.currentTimeMillis(); // 当前时间 ，通知时间
        mNotification.setLatestEventInfo(ctx, note_title, note_text,
                null);
        mNotificationManager.notify(1, mNotification);
    }

    @SuppressWarnings("deprecation")
    public static void sendNote(String note_title) {
        Context ctx = MyApplication.getInstance();
        NotificationManager mNotificationManager = (NotificationManager) ctx
                .getSystemService(Service.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(1);
    }

}
