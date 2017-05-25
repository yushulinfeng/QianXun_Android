package com.szdd.qianxun.start.tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.szdd.qianxun.tools.file.PassTool;

public class UserTool {
    private static final String FIRSTRUN_SAVE_PATH = "all.firstrun";
    private static final String USER_SAVE_PATH = "start.user";
    private static final String USER_LOGIN_STATE = "start.login_state";
    private static int login_state = -1;

    /**
     * 判断用户是否为首次运行
     */
    public static boolean isFirstRun(Context context) {
        SharedPreferences mPref = context.getSharedPreferences(
                FIRSTRUN_SAVE_PATH, Activity.MODE_PRIVATE);
        boolean first = mPref.getBoolean("first", true);
        writeNotFirstRun(context);
        return first;
    }

    /**
     * 存储为非首次运行
     */
    private static void writeNotFirstRun(Context context) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                FIRSTRUN_SAVE_PATH, Activity.MODE_PRIVATE).edit();
        prefs.putBoolean("first", false);
        prefs.commit();
    }

    /**
     * 判断用户是否为登录状态
     */
    public static boolean getLoginState(Context context) {
        if (login_state != -1)
            return login_state == 1 ? true : false;
        SharedPreferences mPref = context.getSharedPreferences(
                USER_LOGIN_STATE, Activity.MODE_PRIVATE);
        boolean login_state = mPref.getBoolean("login_state", false);
        return login_state;
    }

    /**
     * 存储登录状态
     */
    public static void setLoginState(Context context, boolean state) {
        login_state = state ? 1 : 0;
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                USER_LOGIN_STATE, Activity.MODE_PRIVATE).edit();
        prefs.putBoolean("login_state", state);
        prefs.commit();
    }

    /**
     * 保存账户密码（传入原始密码即可，方法中将进行加密）
     */
    public static void saveUser(Context context, String name, String pass) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                USER_SAVE_PATH, Activity.MODE_PRIVATE).edit();
        if (pass != null && !pass.equals(""))
            pass = PassTool.encodePass(pass, name);// 使用用户名加密
        prefs.putString("name", name);
        prefs.putString("pass", pass);
        prefs.commit();
    }

    /**
     * 获取账户密码
     */
    public static String[] getUser(Context context) {
        SharedPreferences mPref = context.getSharedPreferences(USER_SAVE_PATH,
                Activity.MODE_PRIVATE);
        String name = mPref.getString("name", "");// 判断""
        String pass = mPref.getString("pass", "");
        if (!name.equals("") && !pass.equals(""))
            pass = PassTool.decodePass(pass, name);
        return new String[]{name, pass};
    }

}
