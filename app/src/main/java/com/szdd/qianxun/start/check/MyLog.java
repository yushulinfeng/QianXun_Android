package com.szdd.qianxun.start.check;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Environment;

public class MyLog {
	private static final String file_name = "Qianxun_log.log";

	// 保存数据
	public static void saveLog(String log) {
		File file = null;
		try {
			file = new File(Environment.getExternalStorageDirectory(),
					file_name);
		} catch (Exception e) {// 无存储卡
			return;
		}
		try { // 写入文件//adapter从1开始
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			fos.write(log.getBytes("GBK"));// 以GBK方式存储，方便手动修改测试
			fos.flush();// 清除缓存
			fos.close();// 一般关闭要写在finally中
		} catch (Exception e) {
			return;
		}
	}

}
