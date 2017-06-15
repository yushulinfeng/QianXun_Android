package com.szdd.qianxun.message.friend;

public class AnFriendItem {
    private String iconPath = "";// 头像地址
    private int userID = 0;// 用户在服务器端的ID
    private long userName = 0;//用户名
    private String nickName = "";// 昵称（null表示没有昵称）
    private String myTag = null;// 备注（null表示没有备注）
    private boolean state = false;// 0普通，1关注

    public AnFriendItem(int userID, long userName, String nickName) {
        this.userID = userID;
        this.userName = userName;
        this.nickName = nickName;
        this.iconPath = "";
        this.myTag = null;
        this.state = false;
    }

    public AnFriendItem(String iconPath, int userID, long userName,
                        String nickName, String myTag, boolean state) {
        this.iconPath = iconPath;
        this.userID = userID;
        this.userName = userName;
        this.nickName = nickName;
        this.myTag = myTag;
        this.state = state;
    }

    //getter - setter

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public long getUserName() {
        return userName;
    }

    public void setUserName(long userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMyTag() {
        return myTag;
    }

    public void setMyTag(String myTag) {
        this.myTag = myTag;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
