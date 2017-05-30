package com.szdd.qianxun.message.baichuan.im;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.IYWP2PPushListener;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.YWConstants;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.LoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWCustomMessageBody;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.login.IYWConnectionListener;
import com.alibaba.mobileim.login.YWLoginCode;
import com.alibaba.mobileim.login.YWLoginState;
import com.alibaba.mobileim.login.YWPwdType;
import com.alibaba.mobileim.ui.chat.widget.YWSmilyMgr;
import com.alibaba.mobileim.utility.IMAutoLoginInfoStoreUtil;
import com.szdd.qianxun.main_main.MyApplication;
import com.szdd.qianxun.message.baichuan.mine.BaiChuanUtils;
import com.szdd.qianxun.message.baichuan.mine.MineConfig;
import com.szdd.qianxun.message.fun.map.MapReceiveDialog;
import com.szdd.qianxun.message.fun.map.MapShowDialog;
import com.szdd.qianxun.message.fun.map.MapStartDialog;
import com.szdd.qianxun.message.msg_tool.MSG_FLAG;
import com.szdd.qianxun.start.login.Login;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.map.LocationListener;
import com.umeng.openim.OpenIMAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * SDK 初始化和登录
 */
public class LoginSampleHelper {
    public static final String AUTO_LOGIN_STATE_ACTION = "com.openim.autoLoginStateActionn";

    private static LoginSampleHelper sInstance = new LoginSampleHelper();

    public static LoginSampleHelper getInstance() {
        return sInstance;
    }

    // 应用APPKEY，这个APPKEY是申请应用时获取的
    public static String APP_KEY;

    // openIM UI解决方案提供的相关API，创建成功后，保存为全局变量使用
    private YWIMKit mIMKit;

    private YWConnectionListenerImpl mYWConnectionListenerImpl = new YWConnectionListenerImpl();
    private Application mApp;

    public YWIMKit getIMKit() {
        return mIMKit;
    }

    public void setIMKit(YWIMKit imkit) {
        mIMKit = imkit;
    }

    public void initIMKitTemp() {
        mIMKit = YWAPI.getIMKitInstance(MineConfig.USER_TEMP, APP_KEY);
    }

    public void initIMKit(String userId, String appKey) {
        mIMKit = YWAPI.getIMKitInstance(userId.toString(), appKey);
        addConnectionListener();
        addPushMessageListener();
    }

    private YWLoginState mAutoLoginState = YWLoginState.idle;

    public YWLoginState getAutoLoginState() {
        return mAutoLoginState;
    }

    public void setAutoLoginState(YWLoginState state) {
        this.mAutoLoginState = state;
    }

    /**
     * 初始化SDK
     *
     * @param context
     */
    public void initSDK_Sample(Application context) {
        mApp = context;
        APP_KEY = OpenIMAgent.getInstance(context).getMessageBCAppkey();

        //初始化IMKit
        final String userId = IMAutoLoginInfoStoreUtil.getLoginUserId();
        final String appkey = IMAutoLoginInfoStoreUtil.getAppkey();
        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(appkey)) {
//		final String userId = IMAutoLoginInfoStoreUtil.getLoginUserId();
            LoginSampleHelper.getInstance().initIMKit(userId, appkey);
//		final String appkey = IMAutoLoginInfoStoreUtil.getAppkey();
            NotificationInitSampleHelper.init();
        }
//		if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(appkey)){

        OpenIMAgent im = OpenIMAgent.getInstance(context);
        im.init();

        //通知栏相关的初始化
        NotificationInitSampleHelper.init();
        initAutoLoginStateCallback();


        //添加自定义表情的初始化
        YWSmilyMgr.setSmilyInitNotify(new YWSmilyMgr.SmilyInitNotify() {
            @Override
            public void onDefaultSmilyInitOk() {
                SmilyCustomSample.addNewEmojiSmiley();
                SmilyCustomSample.addNewImageSmiley();

                //最后要清空通知，防止memory leak
                YWSmilyMgr.setSmilyInitNotify(null);
            }
        });

    }

    //将自动登录的状态广播出去
    private void sendAutoLoginState(YWLoginState loginState) {
        Intent intent = new Intent(AUTO_LOGIN_STATE_ACTION);
        intent.putExtra("state", loginState.getValue());
        LocalBroadcastManager.getInstance(YWChannel.getApplication()).sendBroadcast(intent);
    }

    /**
     * 登录操作
     *
     * @param userId   用户id
     * @param password 密码
     * @param callback 登录操作结果的回调
     */
    //------------------请特别注意，OpenIMSDK会自动对所有输入的用户ID转成小写处理-------------------
    //所以开发者在注册用户信息时，尽量用小写
    public void login_Sample(String userId, String password, String appKey,
                             IWxCallback callback) {

        if (mIMKit == null) {
            return;
        }

        YWLoginParam loginParam = YWLoginParam.createLoginParam(userId,
                password);
        if (TextUtils.isEmpty(appKey) || appKey.equals(YWConstants.YWSDKAppKey)
                || appKey.equals(YWConstants.YWSDKAppKeyCnHupan)) {
            loginParam.setServerType(LoginParam.ACCOUNTTYPE_WANGXIN);
            loginParam.setPwdType(YWPwdType.pwd);
        }
        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = mIMKit.getLoginService();

        mLoginService.login(loginParam, callback);
    }

    //设置连接状态的监听
    private void addConnectionListener() {
        if (mIMKit == null) {
            return;
        }

        YWIMCore imCore = mIMKit.getIMCore();
        imCore.removeConnectionListener(mYWConnectionListenerImpl);
        imCore.addConnectionListener(mYWConnectionListenerImpl);
    }

    private class YWConnectionListenerImpl implements IYWConnectionListener {

        @Override
        public void onReConnecting() {

        }

        @Override
        public void onReConnected() {
//				YWLog.i("LoginSampleHelper", "onReConnected");
        }

        @Override
        public void onDisconnect(int arg0, String arg1) {
            if (arg0 == YWLoginCode.LOGON_FAIL_KICKOFF) {
                sendAutoLoginState(YWLoginState.disconnect);
                //在其它终端登录，当前用户被踢下线
                LoginSampleHelper.getInstance().setAutoLoginState(YWLoginState.disconnect);
                BaiChuanUtils.showToast("您在其他终端登录");
                YWLog.i("LoginSampleHelper", "被踢下线");
                Intent intent = new Intent(MyApplication.getInstance(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getInstance().startActivity(intent);
            }
        }
    }

    /**
     * 添加新消息到达监听，该监听应该在登录之前调用以保证登录后可以及时收到消息
     */
    private void addPushMessageListener() {
        if (mIMKit == null) {
            return;
        }

        IYWConversationService conversationService = mIMKit.getConversationService();
        //添加单聊消息监听，先删除再添加，以免多次添加该监听
        conversationService.removeP2PPushListener(mP2PListener);
        conversationService.addP2PPushListener(mP2PListener);
    }

    private IYWP2PPushListener mP2PListener = new IYWP2PPushListener() {
        @Override
        public void onPushMessage(IYWContact contact, YWMessage message) {
            if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS) {
                if (message.getMessageBody() instanceof YWCustomMessageBody) {
                    YWCustomMessageBody messageBody = (YWCustomMessageBody) message.getMessageBody();
                    if (messageBody.getTransparentFlag() == 1) {
                        String content = messageBody.getContent();
                        try {
                            JSONObject object = new JSONObject(content);
                            if (object.has("type")) {
                                String type = object.getString("type");
                                if (type != null && type.equals("point_to_point")) {
                                    if (object.has("text")) {
                                        String text = object.getString("text");
                                        long create_time = message.getTime() * 1000L;//消息时间是到秒的
                                        Context con = ChattingOperationCustomSample.con;
                                        if (con == null) con = MyApplication.getInstance();
                                        dealFlagText(con, text, create_time);

                                        //Notification.showToastMsgLong(MyApplication.getContext(), "透传消息，content = " + text);

                                    }
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                }
            } else {
//                BaiChuanUtils.showToast("message");
            }
        }
    };

    private MapReceiveDialog map_recv_dia = null;

    private void dealFlagText(Context activity, String text, long create_time) {// 确定text!=null
        if (text.equals(MSG_FLAG.MSG_HIDE_NULL)) {// 不必处理
        } else if (text.startsWith(MSG_FLAG.MSG_MAP_REQUEST)) {// 收到地图请求
            try {
                showMapDialog(activity, create_time);
            } catch (Exception e) {// 可能报错，错了就不用管了
            }
        } else if (text.startsWith(MSG_FLAG.MSG_MAP_REQUEST_STOP)) {// 取消地图请求
            if (map_recv_dia != null) {
                if (map_recv_dia.isShowing())
                    map_recv_dia.dismiss();
                BaiChuanUtils.showToast("对方已取消请求");
                map_recv_dia = null;
            }
        } else if (text.startsWith(MSG_FLAG.MSG_MAP_ONETIME)) {// 一次位置
            String[] xy = text.substring(MSG_FLAG.MSG_MAP_ONETIME.length())
                    .split(MSG_FLAG.MSG_MAP_SPLIT);
            String x_str = xy[0], y_str = xy[1];
            double x = Double.parseDouble(x_str);
            double y = Double.parseDouble(y_str);
            dismissMapStartDialog();
            MapShowDialog dia = new MapShowDialog(activity, y, x);
            dia.show();
            // /////////////////////////////////
            BaiChuanUtils.showToast("X:" + x + "  Y:" + y);
        } else if (text.startsWith(MSG_FLAG.MSG_MAP_REALTIME)) {// 实时位置
            // /////////////////////////////////
            BaiChuanUtils.showToast("开发中……");
            dismissMapStartDialog();
        } else if (text.equals(MSG_FLAG.MSG_MAP_REJECT)) {// 拒绝请求
            // /////////////////////////////////
            BaiChuanUtils.showToast("对方拒绝了您的请求");
            dismissMapStartDialog();
        }
    }

    @SuppressWarnings("deprecation")
    private void dismissMapStartDialog() {
        try {
            MapStartDialog dia = ChattingOperationCustomSample.dia;
            if (dia != null)
                dia.dismiss();
        } catch (Exception e) {
            BaiChuanUtils.showToast("系统错误");
        }
    }

    private void showMapDialog(Context activity, long create_time) {
        final YWConversation conversation = ChattingOperationCustomSample.mConversation;
        if (conversation == null) {
            return;
        }
        map_recv_dia = new MapReceiveDialog(activity);
        map_recv_dia.setMapCreateTime(create_time);
        map_recv_dia.setListener(new MapReceiveDialog.DialogReceiveListener() {
            public void onRealTime() {
                YWMessage message = ChattingOperationCustomSample
                        .getNotShowMessage(MSG_FLAG.MSG_MAP_REALTIME);
                conversation.getMessageSender().sendMessage(message, 120, null);
                BaiChuanUtils.showToast("实时模式\n开发中……");
            }

            public void onOneTime() {
                StaticMethod.Location(MyApplication.getInstance(),
                        new LocationListener() {
                            public void locationRespose(String locationName,
                                                        double x, double y, float limit) {
                                if (locationName != null
                                        && !locationName.equals("")) {
                                    String to_send = MSG_FLAG.MSG_MAP_ONETIME
                                            + x + MSG_FLAG.MSG_MAP_SPLIT + y;
                                    YWMessage message = ChattingOperationCustomSample
                                            .getNotShowMessage(to_send);
                                    conversation.getMessageSender().sendMessage(message, 120, null);
                                } else {
                                    BaiChuanUtils.showToast("定位失败");
                                }
                            }
                        });
            }

            public void onCancel() {
                YWMessage message = ChattingOperationCustomSample
                        .getNotShowMessage(MSG_FLAG.MSG_MAP_REJECT);
                conversation.getMessageSender().sendMessage(message, 120, null);
            }
        });
        map_recv_dia.show();
    }

    /**
     * 登出
     */
    public void loginOut_Sample() {
        if (mIMKit == null) {
            return;
        }

        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = mIMKit.getLoginService();
        mLoginService.logout(new IWxCallback() {

            @Override
            public void onSuccess(Object... arg0) {

            }

            @Override
            public void onProgress(int arg0) {

            }

            @Override
            public void onError(int arg0, String arg1) {

            }
        });
    }

    /**
     * 开发者不需要关注此方法，纯粹是DEMO自动登录的需要
     */
    private void initAutoLoginStateCallback() {
        YWChannel.setAutoLoginCallBack(new IWxCallback() {
            @Override
            public void onSuccess(Object... result) {
                mAutoLoginState = YWLoginState.success;
                sendAutoLoginState(mAutoLoginState);
            }

            @Override
            public void onError(int code, String info) {
                mAutoLoginState = YWLoginState.fail;
                sendAutoLoginState(mAutoLoginState);
            }

            @Override
            public void onProgress(int progress) {
                mAutoLoginState = YWLoginState.logining;
                sendAutoLoginState(mAutoLoginState);
            }
        });
    }

}
