package com.szdd.qianxun.tools.base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Base64Tool {

	/**
	 * TODO:将以Base64方式编码的字符串解码为byte数组
	 * 
	 * @param encodeString
	 *            待解码的字符串
	 * @return 解码后的byte数组，解码失败返回null
	 */
	public static byte[] decodeFile(String encodeString) {
		byte[] filebyte = null;
		try {
			filebyte = Base64Coder.decode(encodeString);
		} catch (Exception e) {
			filebyte = null;
		}
		return filebyte;
	}

	/**
	 * TODO:将文件以Base64方式编码为字符串
	 * 
	 * @param filepath
	 *            文件的绝对路径
	 * @return 编码后的字符串，编码失败返回null
	 * */
	public static String encodeFile(String filepath) {
		String result = "";
		try {
			FileInputStream fis = new FileInputStream(filepath);
			byte[] filebyte = new byte[fis.available()];
			fis.read(filebyte);
			fis.close();
			result = new String(Base64Coder.encode(filebyte));
		} catch (IOException e) {
			result = null;
		}
		return result;
	}

	/**
	 * TODO: bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String endcodeBitmap(Bitmap bitmap) {
		if (bitmap == null)
			return null;
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			baos.flush();
			baos.close();
			byte[] bitmapBytes = baos.toByteArray();
			result = new String(Base64Coder.encode(bitmapBytes));
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * TODO: base64转为bitmap
	 * 
	 * @param base64Data
	 * @return
	 */
	public static Bitmap decodeBitmap(String base64Data) {
		byte[] bytes = Base64Coder.decode(base64Data);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
}