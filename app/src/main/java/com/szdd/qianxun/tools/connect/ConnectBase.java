package com.szdd.qianxun.tools.connect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 安卓与后台连接类，本类中所有方法必须在多线程中执行<br>
 * （静态方法将影响封装与后期拓展，此处用普通方法）
 *
 * @author Sun Yu Lin
 */
@SuppressWarnings("deprecation")
public class ConnectBase {
	private static final String DECODE_UNICODE = "\\\\u([0-9a-zA-Z]{4})";
	private static String JSP_COOKIE = null;// 维持会话的cookie
	private Context context = null;

	/**
	 * 使用旧cookie
	 */
	public ConnectBase(Context context) {
		this.context = context;
		JSP_COOKIE = ConnectTool.getCookie(context);
	}

	/**
	 * 刷新cookie
	 */
	public ConnectBase(Context context, boolean refreash) {
		this.context = context;
	}

	/**
	 * 以协定方式执行post连接后台，发送head与body并接收后台返回。
	 *
	 * @param url
	 *            连接地址
	 * @param list
	 *            主体信息
	 * @return 后台返回的字符串信息
	 */
	public String executePost(String url, ConnectList list) {
		if (!isNetworkEnable(context)) {// 网络不可用，直接返回null
			return null;
		}
		if (!url.startsWith("http")) {
			url = ServerURL.getIP() + url;
		}
		final int COONECT_TIME_OUT = 10000;// 设定连接超时10秒
		final int READ_TIME_OUT = 15000;// 设定读取超时为15秒
		BufferedReader in = null;
		try {
			// 定义HttpClient，实例化Post方法
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(url);
			// 设定超时，超时将以异常形式提示
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, COONECT_TIME_OUT);// 请求超时
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					READ_TIME_OUT);// 读取超时
			// 添加cookie信息
			if (JSP_COOKIE != null)// 若为null，则不添加，等待服务器返回
				request.addHeader("Cookie", JSP_COOKIE);
			// 添加body信息
			List<NameValuePair> body = list.getList();
			UrlEncodedFormEntity formEntiry = new UrlEncodedFormEntity(body);
			request.setEntity(formEntiry);
			// 执行请求
			HttpResponse response = client.execute(request);

			// cookie处理，维护会话
			Header head = response.getFirstHeader("set-Cookie");
			if (head != null) {
				JSP_COOKIE = head.getValue();
				ConnectTool.saveCookie(context, JSP_COOKIE);
			}

			// 接收返回
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
			in.close();
			String result = sb.toString();
			int code = response.getStatusLine().getStatusCode();
			Log.e("EEEE", "EEEE   url:" + url);
			Log.e("EEEE", "EEEE   code:" + code);
			Log.e("EEEE", "EEEE   size:" + list.getList().size());
			Log.e("EEEE", "EEEE   result:" + result);
			if (code == 500 || code == 404) {
				return null;
			}
			result = URLDecoder.decode(result, "UTF-8");
			result = URLDecoder.decode(result, "UTF-8");
			result = result.trim();
			if (result.equals(""))
				return null;// 后台返回""则返回null。
			return result;
		} catch (Exception e) {// 很有可能是请求超时了
			// e.printStackTrace();
			if (e != null)
				Log.e("EEEE", "EEEE   " + e.getMessage());
			return null;
		} finally {// 这个在finally中很有必要
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

	// 解决\\u问题
	public static String decode(String s) {
		Pattern reUnicode = Pattern.compile(DECODE_UNICODE);
		Matcher m = reUnicode.matcher(s);
		StringBuffer sb = new StringBuffer(s.length());
		while (m.find()) {
			m.appendReplacement(sb,
					Character.toString((char) Integer.parseInt(m.group(1), 16)));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	// 解决\\u问题，简便方法
	public static String decodeEasy(String s) {
		try {// json自带解析
			return new JSONObject(s).toString();
			// JSONObject.parse(s).toString();//更好，尤其是带引号的
		} catch (JSONException e) {
			return s;
		}
	}

	// 检测网络是否可用
	private static boolean isNetworkEnable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
}
