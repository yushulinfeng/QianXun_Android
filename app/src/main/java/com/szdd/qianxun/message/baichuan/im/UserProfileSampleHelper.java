package com.szdd.qianxun.message.baichuan.im;

import android.content.Intent;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactHeadClickCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWCrossContactProfileCallback;
import com.alibaba.mobileim.lib.model.contact.Contact;
import com.szdd.qianxun.main_main.MyApplication;
import com.szdd.qianxun.message.baichuan.mine.BaiChuanUtils;
import com.szdd.qianxun.message.baichuan.mine.MineConfig;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;

/**
 * 用户自定义昵称和头像
 */
public class UserProfileSampleHelper {

    private static final String TAG = UserProfileSampleHelper.class.getSimpleName();

    private static boolean enableUseLocalUserProfile = true;

    //初始化，建议放在登录之前
    public static void initProfileCallback() {
        if (!enableUseLocalUserProfile) {
            //目前SDK会自动获取导入到OpenIM的帐户昵称和头像，如果用户设置了回调，则SDK不会从服务器获取昵称和头像
            return;
        }
        YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
        if (imKit == null) {
            return;
        }
        final IYWContactService contactManager = imKit.getContactService();

        //头像点击的回调（开发者可以按需设置）
        contactManager.setContactHeadClickCallback(new IYWContactHeadClickCallback() {
            @Override
            public Intent onShowProfileActivity(String userId, String appKey) {
                if (MineConfig.USER_SYSTEM.equals(userId))
                    return null;
                long id = BaiChuanUtils.getUserId(userId);
                if (id < 0)
                    return null;
                MsgPublicTool.showHomePage(MyApplication.getInstance(), id);
                return null;
            }
        });


        //设置用户信息回调，如果开发者已经把用户信息导入了IM服务器，则不需要再调用该方法，IMSDK会自动到IM服务器获取用户信息
        contactManager.setCrossContactProfileCallback(new IYWCrossContactProfileCallback() {

            /**
             * 设置头像点击事件, 该方法已废弃，
             * 后续请使用{@link IYWContactService#setContactHeadClickCallback(IYWContactHeadClickCallback)}
             * @param userId 需要打开页面的用户
             * @param appKey 需要返回个人信息的用户所属站点
             * @return
             * @deprecated
             */
            @Override
            public Intent onShowProfileActivity(String userId, String appKey) {
                return null;
            }

            @Override
            public void updateContactInfo(Contact contact) {
            }

            //此方法会在SDK需要显示头像和昵称的时候，调用。同一个用户会被多次调用的情况。
            //比如显示会话列表，显示聊天窗口时同一个用户都会被调用到。
            //当返回null时，SDK内部才会从云旺服务器获取对应的profile
            @Override
            public IYWContact onFetchContactInfo(final String userId, final String appKey) {
                return null;
            }
        });
    }

}
