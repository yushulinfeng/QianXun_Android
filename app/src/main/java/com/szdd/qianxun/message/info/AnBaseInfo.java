package com.szdd.qianxun.message.info;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.szdd.qianxun.R;
import com.szdd.qianxun.main_main.MyApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnBaseInfo {

    private String userId = "";// 账号

    private Bitmap headIcon = null;// 头像
    private String nickName = "";// 昵称
    private String gender = "";// 性别（男，女）
    private String birthday = "";// 生日（2015-01-10）
    private String location = "";// 地区

    private String headIconLocalPath = "";// 头像本地位置
    private String headIconNetPath = "";// 头像网络位置
    private long birthdayMills = 0;// 用户生日的毫秒数

    public AnBaseInfo() {
    }

    public AnBaseInfo(String userId, String nickName, String gender,
                      String birthday, String location, String headIconLocalPath) {
        this.userId = userId;
        this.nickName = nickName;
        this.gender = gender;
        this.birthday = birthday;
        this.location = location;
        this.headIconLocalPath = headIconLocalPath;
        this.birthdayMills = countBirthdayMills(birthday);
        this.headIcon = null;
    }

    private long countBirthdayMills(String date_str) {
        long result = 0;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            result = Long.valueOf(df.parse(date_str).getTime());
        } catch (Exception e) {
        }
        return result;
    }

    public String getUserId() {
        return userId;
    }

    public Bitmap getHeadIcon() {
        if (userId != null && !userId.equals(""))
            try {
                this.headIcon = BitmapFactory.decodeFile(headIconLocalPath);
            } catch (Exception e) {
                this.headIcon = null;
            }
        if (headIcon == null) {
            try {
                headIcon = BitmapFactory.decodeResource(
                        MyApplication.getInstance().getResources(),
                        R.drawable.head_icon_default);
            } catch (Exception e) {
                headIcon = null;
            }
        }
        return headIcon;
    }

    public String getNickName() {
        return nickName;
    }

    public String getHeadIconNetPath() {
        return headIconNetPath;
    }

    public void setHeadIconNetPath(String headIconNetPath) {
        this.headIconNetPath = headIconNetPath;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getLocation() {
        return location;
    }

    public String getHeadIconLocalPath() {
        return headIconLocalPath;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setHeadIcon(Bitmap headIcon) {
        this.headIcon = headIcon;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
        this.birthdayMills = countBirthdayMills(birthday);
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setHeadIconLocalPath(String headIconLocalPath) {
        this.headIconLocalPath = headIconLocalPath;
    }

    public long getBirthdayMills() {
        return birthdayMills;
    }

    public void setBirthdayMills(long birthdayMills) {
        this.birthdayMills = birthdayMills;
        try {
            Date birthdayDate = new Date(birthdayMills);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            this.birthday = dateFormat.format(birthdayDate);
        } catch (Exception e) {
            this.birthday = "";
        }
    }

}
