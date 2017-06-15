package com.szdd.qianxun.message.msg_tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.szdd.qianxun.message.info.AnBaseInfo;
import com.szdd.qianxun.message.info.AnUserInfo;
import com.szdd.qianxun.tools.file.ShareTool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InfoTool {
    private static final String BASE_INFO_SAVE_PATH = "message.base_info";
    private static final String USER_ID_SAVE_PATH = "message.user_id";

    private static String userId = "";
    private static String userName = "";

    public static void initUserId(String userId_new) {
        userId = userId_new;
    }

    public static String getUserName() {
        return userName;
    }

    public static void initUserName(String userName_new) {
        userName = userName_new;
    }

    public static String getUserId() {
        return userId;
    }

    /**
     * 保存基本信息
     */
    public static void saveBaseInfo(Context context, AnBaseInfo info) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                BASE_INFO_SAVE_PATH, Activity.MODE_PRIVATE).edit();
        prefs.putString("nick", info.getNickName());
        prefs.putString("gender", info.getGender());
        prefs.putString("birthday", info.getBirthday());
        prefs.putString("location", info.getLocation());
        prefs.putString("iconpath", info.getHeadIconLocalPath());
        prefs.commit();
    }

    /**
     * 获取基本信息
     */
    public static AnBaseInfo getBaseInfo(Context context) {
        SharedPreferences mPref = context.getSharedPreferences(
                BASE_INFO_SAVE_PATH, Activity.MODE_PRIVATE);
        String name = getUserName();
        String nick = mPref.getString("nick", "");
        String gender = mPref.getString("gender", "");
        String birthday = mPref.getString("birthday", "");
        String location = mPref.getString("location", "");
        String iconpath = mPref.getString("iconpath", "");
        AnBaseInfo info = new AnBaseInfo(name, nick, gender, birthday, location,
                iconpath);
        return info;// 自行判断吧，id为""就是获取失败了
    }

    /**
     * 最新版本的用户数据
     */
    public static void saveUserInfo(Context context, AnUserInfo info) {
        ShareTool.saveText(context, AnUserInfo.SAVE_PATH, new Gson().toJson(info));
    }

    public static void saveUserInfo(Context context, String info) {
        ShareTool.saveText(context, AnUserInfo.SAVE_PATH, info);
    }

    //可能为null
    public static AnUserInfo getUserInfo(Context context) {
        try {
            String info_text = ShareTool.getText(context, AnUserInfo.SAVE_PATH);
            AnUserInfo info = new Gson().fromJson(info_text, AnUserInfo.class);
            return info;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户账号（username，即手机号）
     */
    public static String getUserName(Context context) {
        String name = getUserName();
        if (name == null || name.equals("")) {
            SharedPreferences mPref = context.getSharedPreferences(
                    BASE_INFO_SAVE_PATH, Activity.MODE_PRIVATE);
            name = mPref.getString("name", "");
        }
        return name;// 自行判断吧，name为""就是获取失败了
    }

    /**
     * 获取用户ID(不是手机号)
     */
    public static String getUserID(Context context) {
        String id = getUserId();
        if (id == null || id.equals("")) {
            SharedPreferences mPref = context.getSharedPreferences(
                    USER_ID_SAVE_PATH, Activity.MODE_PRIVATE);
            id = mPref.getString("user_id", "");
        }
        return id;// 自行判断吧，id为""就是获取失败了
    }

    /**
     * 获取用户ID
     */
    public static void saveUserID(Context context, String user_id) {
        initUserId(user_id);
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                USER_ID_SAVE_PATH, Activity.MODE_PRIVATE).edit();
        prefs.putString("user_id", user_id);
        prefs.commit();
    }

    private final static int[] dayArr = new int[]{20, 19, 21, 20, 21, 22, 23,
            23, 23, 24, 23, 22};
    private final static String[] constellationArr = new String[]{"摩羯座",
            "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
            "天蝎座", "射手座", "摩羯座"};

    /**
     * 已知生日求星座
     */
    public static String getStar(String birthday) {
        try {
            int mon = 1, day = 1;
            String[] bir_temp = birthday.split("-");
            mon = Integer.parseInt(bir_temp[1]);
            day = Integer.parseInt(bir_temp[2]);
            return getConstellation(mon, day);
        } catch (Exception e) {
            return "保密";
        }
    }

    private static String getConstellation(int month, int day) {
        return day < dayArr[month - 1] ? constellationArr[month - 1]
                : constellationArr[month];
    }

    /**
     * 已知生日求年龄
     */
    public static String getAge(String birthday) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String[] current_time = (formatter.format(curDate)).split("-");
            String[] bir_temp = birthday.split("-");
            int current_year = Integer.parseInt(current_time[0]);
            int current_month = Integer.parseInt(current_time[1]);
            int current_day = Integer.parseInt(current_time[2]);
            int bir_year = Integer.parseInt(bir_temp[0]);
            int bir_month = Integer.parseInt(bir_temp[1]);
            int bir_day = Integer.parseInt(bir_temp[2]);
            String age = "0";
            if (current_year > bir_year)
                if (current_month > bir_month)
                    age = (current_year - bir_year) + "";
                else if (current_month == bir_month)
                    if (current_day >= bir_day)
                        age = (current_year - bir_year) + "";
                    else
                        age = (current_year - bir_year - 1) + "";
                else
                    age = (current_year - bir_year - 1) + "";
            return age;
        } catch (Exception e) {
            return "保密";
        }
    }

}
