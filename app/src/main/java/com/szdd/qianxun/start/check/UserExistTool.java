package com.szdd.qianxun.start.check;

import android.content.Context;
import cn.smssdk.SMSSDK;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectEasy;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;

public class UserExistTool {

	/**
	 * 检测用户是否存在
	 */
	public static void isExist(final Context context, final String user_id,
			final boolean show_dialog, final UserExistListener listener) {
		if (context == null || user_id == null || user_id.trim().equals("")) {
			if (listener == null)
				return;
			listener.onResponse(0);
			return;
		}
		ConnectEasy.POST(context, ServerURL.USER_EXIST_CHECK,
				new ConnectListener() {
					@Override
					public ConnectDialog showDialog(ConnectDialog dialog) {
						if (show_dialog)
							dialog.config(context, "正在连接服务器", "请稍候……", true);
						return dialog;
					}

					@Override
					public ConnectList setParam(ConnectList list) {
						list.put("username", user_id);
						return list;
					}

					@Override
					public void onResponse(String response) {
						if (listener == null)
							return;
						if (response == null || response.equals("")
								|| response.equals("failed")) {
							listener.onResponse(0);
						} else if (response.equals("-1")) {
							listener.onResponse(1);
						} else if (response.equals("1")) {
							listener.onResponse(-1);
						} else {
							listener.onResponse(0);
						}
					}
				});
	}

	/**
	 * 发送验证码到指定号码
	 */
	public static void sendSMS(String phone) {
		// ////////////////////////////////////暂时注释不用
		SMSSDK.getVerificationCode("+86", phone);
	}

}
