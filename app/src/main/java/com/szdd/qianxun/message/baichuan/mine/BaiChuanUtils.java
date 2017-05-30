package com.szdd.qianxun.message.baichuan.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.login.YWLoginState;
import com.google.gson.Gson;
import com.szdd.qianxun.main_main.MainMain;
import com.szdd.qianxun.main_main.MyApplication;
import com.szdd.qianxun.message.baichuan.im.DemoSimpleKVStore;
import com.szdd.qianxun.message.baichuan.im.LoginSampleHelper;
import com.szdd.qianxun.message.baichuan.im.NotificationInitSampleHelper;
import com.szdd.qianxun.message.baichuan.im.UserProfileSampleHelper;
import com.szdd.qianxun.message.baichuan.util.AppUtil;
import com.szdd.qianxun.message.info.AnUserInfo;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.start.login.Login;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectEasy;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.ShareTool;
import com.szdd.qianxun.tools.views.QianxunToast;

import java.security.MessageDigest;

public class BaiChuanUtils {

    public static void showToast(String text) {
        QianxunToast.showToast(MyApplication.getInstance(), text, QianxunToast.LENGTH_SHORT);
    }

    ///////////////////////////////////////////////

    private static final String APP_KEY = "23352754";
    private static final String APP_SECRET = "4b205589d59038312f3d51dbb04d0f85";

    public static long getUserId(String userName) {
        try {
            return Long.parseLong(userName);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getUserName(long userId) {// 11位字符串，前方补充0。Android调用
        return getUserName(userId + "");
    }

    public static String getUserName(String id) {// 11位字符串，前方补充0。Android调用
        while (id.length() < 11) {
            id = "0" + id;
        }
        return id;
    }

    public static String getUserPass(long userId) {// key+username+secret,md5,first 16
        String pass = APP_KEY + getUserName(userId) + APP_SECRET;
        try {
            pass = getMD5(pass);
            pass = pass.substring(0, 16);
        } catch (Exception e) {
            pass = getUserName(userId);// 出错，则与用户名相同
        }
        return pass;
    }


    // ////////工具方法//////////

    /**
     * 获取文本数据的MD5编码
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
            bytes = message.digest(text.getBytes("utf8"));
        } catch (Exception e) {
        }
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

    //////////////////////////////////////////////////////

    public interface LoginListener {
        public void onResponse(boolean state);
    }

    //测试专用，后期可以删除
    public static void login(final Activity activity, int userId) {
        login(getUserName(userId), getUserPass(userId), new LoginListener() {
            @Override
            public void onResponse(boolean state) {
                AppUtil.dismissProgressDialog();
                Intent intent = new Intent(activity, MainMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    public static void login(String name, String pass,
                             final LoginListener listener) {
        //初始化imkit
        LoginSampleHelper.getInstance().initIMKit(name, LoginSampleHelper.APP_KEY);
        //通知栏相关的初始化
        NotificationInitSampleHelper.init();
        //自定义头像和昵称回调初始化(如果不需要自定义头像和昵称，则可以省去)
        UserProfileSampleHelper.initProfileCallback();

        //判断当前网络状态，若当前无网络则提示用户无网络
        if (YWChannel.getInstance().getNetWorkState().isNetWorkNull()) {
            BaiChuanUtils.showToast("网络已断开，请稍后再试");
            return;
        }

        LoginSampleHelper.getInstance().login_Sample(name, pass, LoginSampleHelper.APP_KEY, new IWxCallback() {
            @Override
            public void onSuccess(Object... arg0) {//用户名和密码不能存储，需要每次获取
                if (listener != null)
                    listener.onResponse(true);
            }

            @Override
            public void onProgress(int arg0) {
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                if (listener != null)
                    listener.onResponse(false);
            }
        });
    }


    public static void logout(final Activity activity) {
        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = LoginSampleHelper.getInstance()
                .getIMKit().getLoginService();
        mLoginService.logout(new IWxCallback() {
            //此时logout已关闭所有基于IMBaseActivity的OpenIM相关Actiivity，s
            @Override
            public void onSuccess(Object... arg0) {
                BaiChuanUtils.showToast("退出成功");
                LoginSampleHelper.getInstance().setAutoLoginState(YWLoginState.idle);
                activity.finish();
                Intent intent = new Intent(activity, Login.class);
                activity.startActivity(intent);
            }

            @Override
            public void onProgress(int arg0) {
            }

            @Override
            public void onError(int arg0, String arg1) {
                BaiChuanUtils.showToast("退出失败");
            }
        });
    }

    public static void chartTo(Context context, int userId, String phone) {
        chartTo(context, userId, phone, null);
    }

    public static void chartTo(Context context, int userId, String phone, String service) {
        if (!UserStateTool.isLoginEver(context)) {
            showToast("请登录");
            return;
        }
        String userName = getUserName(userId);
        ShareTool.saveText(context, userName, phone);////存储手机号
        Intent intent = LoginSampleHelper.getInstance()
                .getIMKit().getChattingActivityIntent(userName);
        intent.putExtra("phone", phone);
        if (service != null)
            intent.putExtra("service", service);
        context.startActivity(intent);
    }

    public interface PhoneListener {
        public void onResponse(String phone);
    }

    public static void getPhoneById(final Context context,
                                    final long userId, final PhoneListener listener) {
        ConnectEasy.POST(context, ServerURL.HOMEPAGE_PERSONAL_DETAIL, new ConnectListener() {
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            public ConnectList setParam(ConnectList list) {
                list.put("userId", userId);
                return list;
            }

            public void onResponse(String response) {
                // 各种失败都不用管
                if (response == null || response.equals("")
                        || response.equals("failed") || response.equals("-1")
                        || response.equals("-2")) {
                    listener.onResponse(null);
                } else {// 如果成功的话
                    try {
                        AnUserInfo info = new Gson().fromJson(response, AnUserInfo.class);
                        String phone = info.getUsername() + "";
                        listener.onResponse(phone);
                    } catch (Exception e) {
                        listener.onResponse(null);
                    }
                }
            }
        });
    }

    public static boolean getSoundState() {
        return DemoSimpleKVStore.getNeedSound() == 1;
    }

    public static boolean getVibrateState() {
        return DemoSimpleKVStore.getNeedVibration() == 1;
    }

    public static boolean getNoteState() {
        return DemoSimpleKVStore.getNeedNote() == 1;
    }

    public static void setSoundState(boolean state) {
        DemoSimpleKVStore.setNeedSound(state ? 1 : 0);
    }

    public static void setVibrateState(boolean state) {
        DemoSimpleKVStore.setNeedVibration(state ? 1 : 0);
    }

    public static void setNoteState(boolean state) {
        DemoSimpleKVStore.setNeedNote(state ? 1 : 0);
    }

    public static String getAnotherUserId(YWConversation conversation) {
        String id = ((YWP2PConversationBody) conversation
                .getConversationBody()).getContact().getUserId();
        return id;
    }

}
