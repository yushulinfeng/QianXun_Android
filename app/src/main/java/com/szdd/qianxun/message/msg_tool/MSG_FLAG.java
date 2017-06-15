package com.szdd.qianxun.message.msg_tool;

//注意千万不能有包含关系
public class MSG_FLAG {

	public static final String MSG_HIDE_HEAD = "×_";// 所有隐藏头相关的起始标志（乘号，下划线）
	public static final String MSG_SHOW_HEAD = "_×";// 所有隐藏头相关的起始标志（乘号，下划线）
	public static final String MSG_HIDE_NULL = MSG_HIDE_HEAD + "　　";//NULL：+两个全角空格
	public static final String MSG_MAP_SHOW = MSG_SHOW_HEAD+"show";// 消息中的地图显示
	public static final String MSG_MAP_SPLIT = "-";// 地图的分隔符
	public static final String MSG_MAP_REQUEST = MSG_HIDE_HEAD + "map_request_start";
	public static final String MSG_MAP_REQUEST_STOP = MSG_HIDE_HEAD
			+ "map_request_stop";
	public static final String MSG_MAP_REJECT = MSG_HIDE_HEAD + "map_reject";
	public static final String MSG_MAP_ONETIME = MSG_HIDE_HEAD + "map_onetime";// +x-y

	// 软件园：MSG_MAP_ONETIME+"36.673454-117.147528"

	public static final String MSG_MAP_START = MSG_HIDE_HEAD + "map_start";
	public static final String MSG_MAP_RECEIVE = MSG_HIDE_HEAD + "map_receive";
	public static final String MSG_MAP_REALTIME = MSG_HIDE_HEAD
			+ "map_realtime";// +x-y

}
