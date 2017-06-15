package com.szdd.qianxun.message.msg_tool;

import com.szdd.qianxun.main_main.MyApplication;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.umeng.onlineconfig.OnlineConfigAgent;

public class ParamTool {

	/**
	 * 获取友盟在线参数
	 * 
	 * @param key
	 *            参数名<br>
	 *            server_ip,menu_type,show_top,is_test
	 * @return 对应值
	 */
	public static String getParam(String key) {
		String value = OnlineConfigAgent.getInstance().getConfigParams(
				MyApplication.getInstance(), key);
		if (value == null || value.trim().equals(""))
			return "";
		value = value.trim();
		return value;
	}

	/**
	 * 是否为测试模式
	 * 
	 * @return true：测试模式
	 */
	public static boolean isTest() {
		return ServerURL.isTest();
	}

}
