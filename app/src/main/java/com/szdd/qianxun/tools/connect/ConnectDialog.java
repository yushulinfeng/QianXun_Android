package com.szdd.qianxun.tools.connect;

import com.szdd.qianxun.tools.dialog.DialogProgress;
import android.content.Context;

public class ConnectDialog {
	private DialogProgress process_dialog = null;
	private Context context;
	private boolean can_cancel;
	private String title, text;

	public ConnectDialog() {
		context = null;
		title = "";
		text = "";
	}

	public void defaultDialog(Context context) {
		config(context, "正在连接服务器", "请稍候……", true);
	}

	public void config(Context context, String title, String text,
			boolean can_cancel) {
		this.context = context;
		this.title = title;
		this.text = text;
		this.can_cancel = can_cancel;
	}

	public void show() {
		if (context != null) {
			process_dialog = new DialogProgress(context);// (context, title);
			process_dialog.setText(text);
			process_dialog.setCancelable(can_cancel);
			process_dialog.show();
		}
	}

	public void hide() {
		if (process_dialog != null)
			process_dialog.dismiss();
	}
}
