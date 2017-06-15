package com.szdd.qianxun.message.msg_tool;

import com.szdd.qianxun.main_main.MyApplication;
import com.szdd.qianxun.tools.connect.ConnectSign;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

//登录辅助类
public class MsgTool {

    /**
     * 获取用户登录密码
     */
    public static String dealResponseGetPass(String result,String userName) {
        if (result == null)
            return null;
        try {
            JSONObject data = new JSONObject(result);
            ConnectSign.dealTimeSpace(data.getLong("timestamp"));//处理与服务器的时间差值
            String user_id = data.getString("userId");
            String pass = data.getString("password");
            InfoTool.saveUserID(MyApplication.getInstance(), user_id);
            InfoTool.initUserName(userName);
            return pass;
        } catch (JSONException e) {
            return null;
        }
    }


    private static String getRandomString() {
        final long LIMIT = 1000000L;
        long num = (long) (Math.random() * LIMIT);
        while (num < LIMIT / 10) {
            num = (long) (Math.random() * LIMIT);
        }
        return num + "";
    }

    // /////////////////////SHA256///////////////////////

    /**
     * 生成 SHA256值
     */
    public static String getSHA256(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// SHA-1,MD5均可
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (Exception e) {
            return null;
        }
        return strDes;
    }

    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

}
