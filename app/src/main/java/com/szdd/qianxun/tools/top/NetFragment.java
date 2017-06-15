package com.szdd.qianxun.tools.top;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 网络辅助Fragment，其他的联网类可以参考此类复制代码，或继承此类
 * 
 * @author Sun Yu Lin
 */
public abstract class NetFragment extends Fragment {
	private String STRING = "string";
	private int HANDLER_INT = 1000000;
	private Handler handler;
	private Message message;

	@Override
	/**实现父类的onCreateView方法*/
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
		return onCreateView(inflater, container);
	}

	// 允许子类调用的方法////////////////////////////////////////////////////////////
	public final void init() {
		// 使每个类对应的message的值不同
		try {
			HANDLER_INT = (int) (Math.random() * 100000);// 确保6位以内
		} catch (Exception e) {
			HANDLER_INT = 1000000;
		}
		// 初始化handler
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == HANDLER_INT) {
					String string = msg.getData().getString(STRING);
					receiveMessage(string);
				}
				super.handleMessage(msg);
			}
		};
	}

	/** 发送消息到主线程，默认用null即可 */
	public final void sendMessage(String what) {
		message = new Message();
		message.what = HANDLER_INT;
		Bundle bundle = new Bundle();
		bundle.putString(STRING, what);
		message.setData(bundle);
		handler.sendMessage(message);
	}

	/** 运行新线程 */
	public final void startNewThread() {
		new Thread(new Runnable() {
			public void run() {
				newThread();
			}
		}).start();
	}

	// 子类只需要实现即可的方法/////////////////////////////////////////////////////////////
	/** 相当于父类的onCreateView方法 */
	public abstract View onCreateView(LayoutInflater inflater,
			ViewGroup container);

	/**
	 * 主线程收到message的响应<br>
	 * what只是为了方便字符串信息传输
	 */
	public abstract void receiveMessage(String what);

	/** 新线程的运行项目，其中的代码将在新的线程中执行 */
	public abstract void newThread();
}
