package com.szdd.qianxun.message.baichuan.im;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.WXAPI;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageOperateion;
import com.alibaba.mobileim.aop.model.GoodsInfo;
import com.alibaba.mobileim.aop.model.ReplyBarItem;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactProfileCallback;
import com.alibaba.mobileim.contact.IYWCrossContactProfileCallback;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWCustomMessageBody;
import com.alibaba.mobileim.conversation.YWGeoMessageBody;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.fundamental.widget.YWAlertDialog;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;
import com.alibaba.mobileim.lib.model.message.Message;
import com.szdd.qianxun.R;
import com.szdd.qianxun.main_main.MyApplication;
import com.szdd.qianxun.message.baichuan.mine.BaiChuanUtils;
import com.szdd.qianxun.message.baichuan.util.AndTools;
import com.szdd.qianxun.message.fun.map.MapShowDialog;
import com.szdd.qianxun.message.fun.map.MapShowMeDialog;
import com.szdd.qianxun.message.fun.map.MapStartDialog;
import com.szdd.qianxun.message.msg_tool.MSG_FLAG;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.more.RequestMy;
import com.szdd.qianxun.more.RequestTa;
import com.szdd.qianxun.more.UserVerify;
import com.szdd.qianxun.sell.main.WebBrowser;
import com.szdd.qianxun.sell.orders.SellAllOrderBuy;
import com.szdd.qianxun.sell.orders.SellAllOrderSell;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.file.ShareTool;
import com.szdd.qianxun.tools.map.LocationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天界面逻辑定义
 */
public class ChattingOperationCustomSample extends IMChattingPageOperateion {

    private static final String TAG = "ChattingOperationCustomSample";

    // 默认写法
    public ChattingOperationCustomSample(Pointcut pointcut) {
        super(pointcut);
    }

    /**
     * 单聊ui界面，点击url的事件拦截 返回true;表示自定义处理，返回false，由默认处理
     *
     * @param fragment 可以通过 fragment.getActivity拿到Context
     * @param message  点击的url所属的message
     * @param url      点击的url
     */
    @Override
    public boolean onUrlClick(Fragment fragment, YWMessage message, String url,
                              YWConversation conversation) {
        //Notification.showToastMsgLong(fragment.getActivity(), "用户点击了url:" + url);

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse(url));
//        fragment.startActivity(intent);

        return false;
    }

    private static int ITEM_ID_1 = 0x1;
    private static int ITEM_ID_2 = 0x2;
    private static int ITEM_ID_3 = 0X3;
    private static int ITEM_ID_4 = 0X4;

    public static YWConversation mConversation;


    /**
     * 用于增加聊天窗口 下方回复栏的操作区的item ReplyBarItem itemId:唯一标识 建议从1开始
     * ItemImageRes：显示的图片 ItemLabel：文字 label YWConversation
     * conversation：当前会话，通过conversation.getConversationType() 区分个人单聊，与群聊天
     */
    @Override
    public List<ReplyBarItem> getReplybarItems(Fragment pointcut,
                                               YWConversation conversation) {
        mConversation = conversation;
        con = pointcut.getActivity();
        List<ReplyBarItem> replyBarItems = new ArrayList<ReplyBarItem>();
        if (conversation.getConversationType() == YWConversationType.P2P) {
            ReplyBarItem replyBarItem = new ReplyBarItem();
            replyBarItem.setItemId(ITEM_ID_1);
            replyBarItem.setItemImageRes(R.drawable.msg_bottom_location);//demo_reply_bar_location
            replyBarItem.setItemLabel("位置");
            replyBarItems.add(replyBarItem);

            ReplyBarItem replyBarItem2 = new ReplyBarItem();
            replyBarItem2.setItemId(ITEM_ID_2);
            replyBarItem2.setItemImageRes(R.drawable.msg_bottom_map);
            replyBarItem2.setItemLabel("地图");
            replyBarItems.add(replyBarItem2);

            ReplyBarItem replyBarItem3 = new ReplyBarItem();
            replyBarItem3.setItemId(ITEM_ID_3);
            replyBarItem3.setItemImageRes(R.drawable.msg_bottom_phone);
            replyBarItem3.setItemLabel("电话");
            replyBarItems.add(replyBarItem3);

            ReplyBarItem replyBarItem4 = new ReplyBarItem();
            replyBarItem4.setItemId(ITEM_ID_4);
            replyBarItem4.setItemImageRes(R.drawable.msg_bottom_message);
            replyBarItem4.setItemLabel("短信");
            replyBarItems.add(replyBarItem4);
        }
        return replyBarItems;
    }

    /**
     * 当自定义的item点击时的回调
     */
    @Override
    public void onReplyBarItemClick(Fragment pointcut, ReplyBarItem item,
                                    YWConversation conversation) {
        if (conversation.getConversationType() == YWConversationType.P2P) {
            if (item.getItemId() == ITEM_ID_1) {
                sendGeoMessage(conversation);
            } else if (item.getItemId() == ITEM_ID_2) {
                pointToPointMap(pointcut.getActivity(), conversation);
            } else if (item.getItemId() == ITEM_ID_3) {
                final String id = BaiChuanUtils.getAnotherUserId(conversation);
                String phone = ShareTool.getText(pointcut.getActivity(), id);
                final Context context = pointcut.getActivity();
                if (phone == null || phone.equals("")) {
                    BaiChuanUtils.getPhoneById(context, BaiChuanUtils.getUserId(id), new BaiChuanUtils.PhoneListener() {
                        @Override
                        public void onResponse(String phone) {
                            if (phone != null && phone.trim().length() == 11) {
                                String userPhone = phone.trim();
                                ShareTool.saveText(context, id, userPhone);////存储手机号
                                StaticMethod.callTo(context,userPhone);
                            } else {
                                BaiChuanUtils.showToast("对方未开启此服务");
                            }
                        }
                    });
                } else {
                    StaticMethod.callTo(pointcut.getActivity(),phone);
                }
            } else if (item.getItemId() == ITEM_ID_4) {
                final String id = BaiChuanUtils.getAnotherUserId(conversation);
                String phone = ShareTool.getText(pointcut.getActivity(), id);
                final Context context = pointcut.getActivity();
                if (phone == null || phone.equals("")) {
                    BaiChuanUtils.getPhoneById(context, BaiChuanUtils.getUserId(id), new BaiChuanUtils.PhoneListener() {
                        @Override
                        public void onResponse(String phone) {
                            if (phone != null && phone.trim().length() == 11) {
                                String userPhone = phone.trim();
                                ShareTool.saveText(context, id, userPhone);////存储手机号
                                // 启动编辑短信的界面
                                StaticMethod.msgTo(context,userPhone);
                            } else {
                                BaiChuanUtils.showToast("对方未开启此服务");
                            }
                        }
                    });
                } else {
                    // 启动编辑短信的界面
                    StaticMethod.msgTo( pointcut.getActivity(),phone);
                }
            }
        }
    }

    public static MapStartDialog dia = null;
    public static Context con = null;

    //
    public void pointToPointMap(final Activity activity,
                                final YWConversation conversation) {
        dia = new MapStartDialog(activity);
        dia.setListener(new MapStartDialog.DialogStateListener() {
            public void onSure() {
                YWMessage message = getNotShowMessage(MSG_FLAG.MSG_HIDE_NULL);
                conversation.getMessageSender().sendMessage(message, 120, null);
                message = getNotShowMessage(MSG_FLAG.MSG_MAP_REQUEST);
                conversation.getMessageSender().sendMessage(message, 120, null);
                AndTools.showToast(activity, "请求已发送");// ///////////////
            }

            public void onCancel() {
                YWMessage message = getNotShowMessage(MSG_FLAG.MSG_MAP_REQUEST_STOP);
                conversation.getMessageSender().sendMessage(message, 120, null);
                AndTools.showToast(activity, "请求已取消");// ///////////////
            }

            public void onTest() {
                AndTools.showToast(activity, "TEST");// ///////////////
                MapShowDialog dialog = new MapShowDialog(activity,
                        117.147528, 36.673454);
                dialog.show();
            }
        });
        dia.show();
    }

    public static YWMessage getNotShowMessage(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        YWCustomMessageBody messageBody = new YWCustomMessageBody();
        JSONObject object = new JSONObject();
        try {
            object.put("type", "point_to_point");
            object.put("text", content);
        } catch (JSONException e) {
        }
        //设置透传标记，1表示透传消息，0表示非透传消息，默认为0，
        // 如果想要发送透传消息必须调用该方法，并且参数必须传1
        messageBody.setTransparentFlag(1);
        messageBody.setContent(object.toString());
        YWMessage message = YWMessageChannel.createCustomMessage(messageBody);
        return message;
    }

    /**
     * 返回自定义的拍照Item,开发者通过该方法可以实现修改拍照ReplyBarItem的Icon和文案
     *
     * @return 返回null则默认使用sdk的UI
     */
    @Override
    public ReplyBarItem getCustomPhotoReplyBarItem() {
        ReplyBarItem photoReplyBarItem = new ReplyBarItem();
        photoReplyBarItem.setItemImageRes(R.drawable.msg_bottom_camera);//__leak_canary_icon
        photoReplyBarItem.setItemLabel("照相");
        return photoReplyBarItem;
//        return null;
    }

    /**
     * 返回自定义的照片选择Item,开发者通过该方法可以实现修改照片选择ReplyBarItem的Icon和文案
     *
     * @return 返回null则默认使用sdk的UI
     */
    @Override
    public ReplyBarItem getCustomAlbumReplyBarItem() {
        ReplyBarItem albumReplyBarItem = new ReplyBarItem();
        albumReplyBarItem.setItemImageRes(R.drawable.msg_bottom_album);//__leak_canary_icon
        albumReplyBarItem.setItemLabel("照片");
        return albumReplyBarItem;
//        return null;
    }

    @Override
    public int getFastReplyResId(YWConversation conversation) {
        return R.drawable.aliwx_reply_bar_face_bg;
    }

    @Override
    public boolean onFastReplyClick(Fragment pointcut, YWConversation ywConversation) {
        return false;
    }

    @Override
    public int getRecordResId(YWConversation conversation) {
        return 0;
    }

    @Override
    public boolean onRecordItemClick(Fragment pointcut, YWConversation ywConversation) {
        return false;
    }

    public static int count = 1;

    /**
     * 发送单聊地理位置消息
     */
    public static void sendGeoMessage(final YWConversation conversation) {
        StaticMethod.Location(MyApplication.getInstance(), new LocationListener() {
            @Override
            public void locationRespose(String locationName, double x, double y, float limit) {
                if (locationName == null || locationName.equals("")) {
                    BaiChuanUtils.showToast("定位失败");
                    return;
                }
                locationName = locationName.trim();
                if (locationName.startsWith("中国"))
                    locationName = locationName.substring(2);
                conversation.getMessageSender().sendMessage(
                        YWMessageChannel.createGeoMessage(x,
                                y, locationName), 120, null);
            }
        });
    }

    /**
     * 定制点击消息事件, 每一条消息的点击事件都会回调该方法，开发者根据消息类型，对不同类型的消息设置不同的点击事件
     *
     * @param fragment 聊天窗口fragment对象
     * @param message  被点击的消息
     * @return true:使用用户自定义的消息点击事件，false：使用默认的消息点击事件
     */
    @Override
    public boolean onMessageClick(Fragment fragment, YWMessage message) {
        if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO) {
            double x = ((Message) message).getLatitude();
            double y = ((Message) message).getLongitude();
            String name = ((Message) message).getContent();
            if (name != null) {//缩短地区名字
                int index = name.indexOf("省");
                if (index != -1)
                    name = name.substring(index + 1);
            }
            new MapShowMeDialog(fragment.getActivity(), x, y, name).show();
            return true;
        } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS) {
            //Notification.showToastMsg(fragment.getActivity(), "你点击了自定义消息");
            int msgType = -1;
            String content = message.getMessageBody().getContent();
            try {
                JSONObject object = new JSONObject(content);
                msgType = object.getInt("type");
            } catch (Exception e) {
            }
            Intent intent = null;
            switch (msgType) {
                case 0:
                    break;
                case 1:
                    try {
                        String url = new JSONObject(content).getString("extra");
                        WebBrowser.browse(fragment.getActivity(),
                                fragment.getString(R.string.app_name), url);
                    } catch (Exception e) {
                    }
                    break;
                case 2:
                    intent = new Intent(fragment.getActivity(), RequestMy.class);
                    break;
                case 3:
                    intent = new Intent(fragment.getActivity(), RequestTa.class);
                    break;
                case 4:
                    intent = new Intent(fragment.getActivity(), SellAllOrderSell.class);
                    break;
                case 5:
                    intent = new Intent(fragment.getActivity(), SellAllOrderBuy.class);
                    break;
                case 6:
                    intent = new Intent(fragment.getActivity(), UserVerify.class);
                    break;
                case 7:
                    try {
                        String req_id = new JSONObject(content).getString("extra");
                        MsgPublicTool.showServiceDetail(fragment.getActivity(), req_id);
                    } catch (Exception e) {
                    }
                    break;
            }
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                fragment.startActivity(intent);
            }
            return true;
        }
        return false;
    }

    /**
     * 定制长按消息事件，每一条消息的长按事件都会回调该方法，开发者根据消息类型，对不同类型的消息设置不同的长按事件
     *
     * @param fragment 聊天窗口fragment对象
     * @param message  被点击的消息
     * @return true:使用用户自定义的长按消息事件，false：使用默认的长按消息事件
     */
    @Override
    public boolean onMessageLongClick(final Fragment fragment, final YWMessage message) {
        //Notification.showToastMsg(fragment.getActivity(), "触发了自定义MessageLongClick事件");

        if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_IMAGE || //自定义图片长按的事件处理，无复制选项。
                message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GIF
                ) {

            new YWAlertDialog.Builder(fragment.getActivity()).setTitle(getShowName(WXAPI.getInstance().getConversationManager().getConversationByConversationId(message.getConversationId()))).setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    WXAPI.getInstance().getConversationManager().getConversationByConversationId(message.getConversationId()).getMessageLoader().deleteMessage(message);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();
            return true;
        } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_AUDIO) {
            String[] items = new String[1];
            if (mUserInCallMode) { //当前为听筒模式
                items[0] = "使用扬声器模式";
            } else { //当前为扬声器模式
                items[0] = "使用听筒模式";
            }
            new YWAlertDialog.Builder(fragment.getActivity()).setTitle(getShowName(WXAPI.getInstance().getConversationManager().getConversationByConversationId(message.getConversationId()))).setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mUserInCallMode) {
                        mUserInCallMode = false;
                    } else {
                        mUserInCallMode = true;
                    }
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();
            return true;
        } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO) {
            BaiChuanUtils.showToast("点击查看地图");
            //Notification.showToastMsg(fragment.getActivity(), "你长按了地理位置消息");
            return false;
        } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS) {
            BaiChuanUtils.showToast("点击查看详情");
            //Notification.showToastMsg(fragment.getActivity(), "你长按了自定义消息");
            return true;
        }

        return false;
    }

    public String getShowName(YWConversation conversation) {
        String conversationName;
        //added by 照虚  2015-3-26,有点无奈
        if (conversation == null) {
            return "";
        }

        if (conversation.getConversationType() == YWConversationType.Tribe) {
            conversationName = ((YWTribeConversationBody) conversation.getConversationBody()).getTribe().getTribeName();
        } else {
            IYWContact contact = ((YWP2PConversationBody) conversation.getConversationBody()).getContact();
            String userId = contact.getUserId();
            String appkey = contact.getAppKey();
            conversationName = userId;
            IYWCrossContactProfileCallback callback = WXAPI.getInstance().getCrossContactProfileCallback();
            if (callback != null) {
                IYWContact iContact = callback.onFetchContactInfo(userId, appkey);
                if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
                    conversationName = iContact.getShowName();
                    return conversationName;
                }
            } else {
                IYWContactProfileCallback profileCallback = WXAPI.getInstance().getContactProfileCallback();
                if (profileCallback != null) {
                    IYWContact iContact = profileCallback.onFetchContactInfo(userId);
                    if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
                        conversationName = iContact.getShowName();
                        return conversationName;
                    }
                }
            }
            IYWContact iContact = WXAPI.getInstance().getWXIMContact(userId);
            if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
                conversationName = iContact.getShowName();
            }
        }
        return conversationName;
    }

    /**
     * 获取url对应的商品详情信息，当openIM发送或者接收到url消息时会首先调用{@link ChattingOperationCustomSample#getCustomUrlView(Fragment, YWMessage, String, YWConversation)},
     * 若getCustomUrlView()返回null，才会回调调用该方法获取商品详情，若getCustomUrlView()返回非null的view对象，则直接用此view展示url消息，不再回调该方法。因此，如果希望该方法被调用,
     * 请确保{@link ChattingOperationCustomSample#getCustomUrlView(Fragment, YWMessage, String, YWConversation)}返回null。
     *
     * @param fragment       可以通过 fragment.getActivity拿到Context
     * @param message        url所属的message
     * @param url            url
     * @param ywConversation message所属的conversion
     * @return 商品信息
     */
    @Override
    public GoodsInfo getGoodsInfoFromUrl(Fragment fragment, YWMessage message, String url, YWConversation ywConversation) {
        return null;
    }

    /**
     * 获取url对应的自定义view,当openIM发送或者接收到url消息时会回调该方法获取该url的自定义view。若开发者实现了该方法并且返回了一个view对象，openIM将会使用该view展示对应的url消息。
     *
     * @param fragment       可以通过 fragment.getActivity拿到Context
     * @param message        url所属的message
     * @param url            url
     * @param ywConversation message所属的conversion
     * @return 自定义Url view
     */
    @Override
    public View getCustomUrlView(Fragment fragment, YWMessage message, String url, YWConversation ywConversation) {
        if (url.equals("https://www.baidu.com/ ")) {
            LinearLayout layout = (LinearLayout) View.inflate(
                    MyApplication.getInstance(),
                    R.layout.demo_custom_tribe_msg_layout, null);
            TextView textView = (TextView) layout.findViewById(R.id.msg_content);
            textView.setText("I'm from getCustomUrlView!");
            return layout;
        }
        return null;
    }


    /**
     * 开发者可以根据用户操作设置该值
     */
    private static boolean mUserInCallMode = false;

    /**
     * 是否使用听筒模式播放语音消息
     *
     * @param fragment
     * @param message
     * @return true：使用听筒模式， false：使用扬声器模式
     */
    @Override
    public boolean useInCallMode(Fragment fragment, YWMessage message) {
        return mUserInCallMode;
    }

    /**
     * 当打开聊天窗口时，自动发送该字符串给对方
     *
     * @param fragment     聊天窗口fragment
     * @param conversation 当前会话
     * @return 自动发送的内容（注意，内容empty则不自动发送）
     */
    @Override
    public String messageToSendWhenOpenChatting(Fragment fragment, YWConversation conversation) {
        //p2p、客服和店铺会话处理，否则不处理，
        int mCvsType = conversation.getConversationType().getValue();
        if (mCvsType == YWConversationType.P2P.getValue() || mCvsType == YWConversationType.SHOP.getValue()) {
//            return "你好";
            return null;
        } else {
            return null;
        }

    }

    /**
     * 当打开聊天窗口时，自动发送该消息给对方
     *
     * @param fragment     聊天窗口fragment
     * @param conversation 当前会话
     * @return 自动发送的消息（注意，内容empty则不自动发送）
     */
    @Override
    public YWMessage ywMessageToSendWhenOpenChatting(Fragment fragment, YWConversation conversation) {
//        YWMessageBody messageBody = new YWMessageBody();
//        messageBody.setSummary("WithoutHead");
//        messageBody.setContent("hi，我是单聊自定义消息之好友名片");
//        YWMessage message = YWMessageChannel.createCustomMessage(messageBody);
//        return message;
        return null;
    }


    /*****************
     * 以下是定制自定义消息view的示例代码
     ****************/

    //自定义消息view的种类数
    private final int typeCount = 2;

    /**
     * 自定义viewT
     * ype，viewType的值必须从0开始，然后依次+1递增，且viewType的个数必须等于typeCount，切记切记！！！
     ***/
    //地理位置消息
    private final int type_0 = 0;

    //单聊自定义消息(推送消息)
    private final int type_1 = 1;

    /**
     * 自定义消息view的种类数
     *
     * @return 自定义消息view的种类数
     */
    @Override
    public int getCustomViewTypeCount() {
        return typeCount;
    }

    /**
     * 自定义消息view的类型，开发者可以根据自己的需求定制多种自定义消息view，这里需要根据消息返回view的类型
     *
     * @param message 需要自定义显示的消息
     * @return 自定义消息view的类型
     */
    @Override
    public int getCustomViewType(YWMessage message) {
        if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO) {
            return type_0;
        } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS) {
            int msgType = -1;
            try {
                String content = message.getMessageBody().getContent();
                JSONObject object = new JSONObject(content);
                msgType = object.getInt("type");
            } catch (Exception e) {
            }
            if (msgType >= 0) {
                return type_1;
            }
        }
        return super.getCustomViewType(message);
    }

    /**
     * 是否需要隐藏头像
     *
     * @param viewType 自定义view类型
     * @return true: 隐藏头像  false：不隐藏头像
     */
    @Override
    public boolean needHideHead(int viewType) {
        if (viewType == type_1) {
            return true;
        }
        return super.needHideHead(viewType);
    }

    /**
     * 根据viewType获取自定义view
     *
     * @param fragment       聊天窗口fragment
     * @param message        当前需要自定义view的消息
     * @param convertView    自定义view
     * @param viewType       自定义view的类型
     * @param headLoadHelper 头像加载管理器，用户可以调用该对象的方法加载头像
     * @return 自定义view
     */
    @Override
    public View getCustomView(Fragment fragment, YWMessage message, View convertView, int viewType, YWContactHeadLoadHelper headLoadHelper) {
        YWLog.i(TAG, "getCustomView, type = " + viewType);
        if (viewType == type_0) { //地理位置消息
            ViewHolder0 holder = null;
            if (convertView == null) {
                holder = new ViewHolder0();
                convertView = View.inflate(fragment.getActivity(), R.layout.demo_geo_message_layout, null);
                holder.address = (TextView) convertView.findViewById(R.id.content);
                convertView.setTag(holder);
                YWLog.i(TAG, "getCustomView, convertView == null");
            } else {
                holder = (ViewHolder0) convertView.getTag();
                YWLog.i(TAG, "getCustomView, convertView != null");
            }
            YWGeoMessageBody messageBody = (YWGeoMessageBody) message.getMessageBody();
            holder.address.setText(messageBody.getAddress());
            return convertView;
        } else if (viewType == type_1) {  //单聊自定义消息(推送消息)
            String text = null;
            try {
                String content = message.getMessageBody().getContent();
                JSONObject object = new JSONObject(content);
                text = object.getString("detail");//context
            } catch (Exception e) {
            }

            ViewHolder1 holder = null;
            if (convertView == null) {
                holder = new ViewHolder1();
                convertView = View.inflate(fragment.getActivity(), R.layout.demo_custom_msg_layout_without_head, null);
                holder.head = (ImageView) convertView.findViewById(R.id.head);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder1) convertView.getTag();
            }
            holder.name.setText(text);
            holder.head.setImageResource(R.drawable.ic_launcher);
            return convertView;
        }
        return super.getCustomView(fragment, message, convertView, viewType, headLoadHelper);
    }

    public class ViewHolder0 {
        TextView address;
    }

    public class ViewHolder1 {
        ImageView head;
        TextView name;
    }


    /**************** 以上是定制自定义消息view的示例代码 ****************/

    /**
     * 双击放大文字消息的开关
     *
     * @param fragment
     * @return true:开启双击放大文字 false: 关闭双击放大文字
     */
    @Override
    public boolean enableDoubleClickEnlargeMessageText(Fragment fragment) {
        return true;
    }

}