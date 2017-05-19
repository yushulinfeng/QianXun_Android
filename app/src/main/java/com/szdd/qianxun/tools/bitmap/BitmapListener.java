package com.szdd.qianxun.tools.bitmap;

import android.graphics.Bitmap;

public interface BitmapListener {
	/**
	 * 网络执行完毕后自动回调
	 *
	 * @param response
	 *            网络返回的图片，错误将返回null
	 */
	public void onResponse(Bitmap bitmap);
}
