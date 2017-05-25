package com.szdd.qianxun.start.check;

public interface UserExistListener {
	/**
	 * 用户是否存在
	 * 
	 * @param is_exist
	 *            -1：不存在；1：存在；0：网络错误(或系统错误)
	 */
	public void onResponse(int is_exist);
}
