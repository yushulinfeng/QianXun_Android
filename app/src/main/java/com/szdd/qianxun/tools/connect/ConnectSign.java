package com.szdd.qianxun.tools.connect;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ConnectSign {
    public static String KEY_TIME = "timestamp", KEY_SIGN = "signature";
    private static long TIME_SPACE = 0;
    private static String SECRET_KEY = "qianxun";

    /**
     * 获取签名的MD5
     *
     * @param time 时间戳
     * @return 时间戳+秘钥，取两次MD5
     */
    public static String getSignMD5(long time) {
        String all = time + SECRET_KEY;
        String result = getMD5(all);
        result = getMD5(result);
        return result;
    }

    /**
     * 处理时间差，安卓专用
     *
     * @param time 服务器时间
     */
    public static void dealTimeSpace(long time) {
        TIME_SPACE = time - System.currentTimeMillis();
    }

    /**
     * 获取签名的URL后缀，安卓专用
     *
     * @return 以"?"开头的URL后缀
     */
    public static String getSignURL() {
        String result = "?";
        long time = getTimeSnap();
        result += KEY_TIME + "=" + time;
        result += "&";
        result += KEY_SIGN + "=" + getSignMD5(time);
        return result;
    }


    /**
     * 获取时间戳
     *
     * @return 当前的服务器时间
     */
    private static long getTimeSnap() {
        return System.currentTimeMillis() + TIME_SPACE;
    }

    /**
     * 获取文本数据的MD5编码（注：安卓没有直接就按MD5的包）
     *
     * @param text 要编码的文本数据
     * @return 数据的32位MD5字符串值
     */
    private static String getMD5(String text) {// 返回32位MD5数组
        String result = "";
        MessageDigest message = null;
        byte[] bytes = null;
        try {
            message = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
        bytes = message.digest(text.getBytes());
        result = new String(toHexString(bytes));
        return result;
    }


    /**
     * 将byte数组转换为Hex字符串，这其实是HttpClient里面的codec.jar中Hex类中的encodeHex方法
     * （这里没有必要导入整个包，所以只拿出来这个方法）
     *
     * @param md 要转换的byte数组
     * @return 转换后的字符串
     */
    private static String toHexString(byte[] md) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        int j = md.length;
        char str[] = new char[j * 2];
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[2 * i] = hexDigits[byte0 >>> 4 & 0xf];
            str[i * 2 + 1] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

}
