package com.szdd.qianxun.message.friend;

import com.google.gson.Gson;

public class AnCollect {
    private String headIcon;
    private int id;
    private String nickName;
    private long username;

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getUsername() {
        return username;
    }

    public void setUsername(long username) {
        this.username = username;
    }

    public String toString() {
        String result = "";
        try {
            result = new Gson().toJson(this);
        } catch (Exception e) {
        }
        return result;
    }

    public AnFriendItem toAnFriendItem() {
        AnFriendItem item = new AnFriendItem(headIcon, id, username, nickName, null, true);
        return item;
    }

}