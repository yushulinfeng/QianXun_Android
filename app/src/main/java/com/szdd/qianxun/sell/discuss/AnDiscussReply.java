package com.szdd.qianxun.sell.discuss;

import android.text.TextUtils;

import com.szdd.qianxun.message.baichuan.mine.BaiChuanUtils;

public class AnDiscussReply {

    private String userId;//同阿里百川ID
    private String userName;
    private String userHead;

    private String replyId;
    private String discussId;

    private int replyFloor;
    private String replyText;
    private String replyTime;

    //项目中没有此项，为便于后期拓展，通过标识符分割replyText
    //可修改先关的getter算法即可。所以使用private+builder。
    private String extraText;

    //constructor

    public AnDiscussReply() {
    }

    public AnDiscussReply(String userName, String replyText) {
        this.userName = userName;
        this.replyText = replyText;
    }

    //getter-setter

    public long getUserId() {
        return BaiChuanUtils.getUserId(userId);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        if (TextUtils.isEmpty(userName))
            return "";
        try {
            String name_my = userName.substring(0, userName.lastIndexOf('_'));
            return name_my;
        } catch (Exception e) {
            if (userName == null)
                return "";
            return userName;
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getDiscussId() {
        return discussId;
    }

    public void setDiscussId(String discussId) {
        this.discussId = discussId;
    }

    public int getReplyFloor() {
        return replyFloor;
    }

    public void setReplyFloor(int replyFloor) {
        this.replyFloor = replyFloor;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public String getExtraText() {
        return extraText;
    }

    public void setExtraText(String extraText) {
        this.extraText = extraText;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    //builder

    static class Builder {
        private AnDiscussReply reply;

        public Builder() {
            reply = new AnDiscussReply();
        }

        public Builder(String userName, String replyText) {
            reply = new AnDiscussReply(userName, replyText);
        }

        public Builder userId(String userId) {
            reply.setUserId(userId);
            return this;
        }

        public Builder userName(String userName) {
            reply.setUserName(userName);
            return this;
        }

        public Builder userHead(String userHead) {
            reply.setUserHead(userHead);
            return this;
        }

        public Builder replyId(String replyId) {
            reply.setReplyId(replyId);
            return this;
        }

        public Builder discussId(String discussId) {
            reply.setDiscussId(discussId);
            return this;
        }

        public Builder replyFloor(int replyFloor) {
            reply.setReplyFloor(replyFloor);
            return this;
        }

        public Builder replyText(String replyText) {
            reply.setReplyText(replyText);
            return this;
        }

        public Builder replyTime(String replyTime) {
            reply.setReplyTime(replyTime);
            return this;
        }

        public AnDiscussReply build() {
            return reply;
        }
    }

}
