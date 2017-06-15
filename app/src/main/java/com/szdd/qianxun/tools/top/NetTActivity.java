package com.szdd.qianxun.tools.top;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.baichuan.util.AndTools;
import com.szdd.qianxun.message.msg_tool.ParamTool;

public abstract class NetTActivity extends NetActivity {

	// ////////允许子类调用的方法
	public void setTitle(String title) {
		((TextView) findViewById(R.id.top_text)).setText(title);
		// 由于未知原因的标题栏颜色问题，此处只能这样处理了
		setTopColors(R.color.start_topbar_bg);
	}

	public void setTopColors(int top_color) {// 用setTopColors不行，有编译检查
		try {
			LinearLayout top_layout = (LinearLayout) findViewById(R.id.top_layout);
			top_layout.setBackgroundColor(getResources().getColor(top_color));
		} catch (Exception e) {
		}
	}

	public void showBackButton() {
		ImageButton top_back = (ImageButton) findViewById(R.id.top_back);
		top_back.setVisibility(ImageButton.VISIBLE);
		top_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void showMenuButton() {
		ImageButton top_menu = (ImageButton) findViewById(R.id.top_menu);
		top_menu.setVisibility(ImageButton.VISIBLE);
		top_menu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showContextMenu();
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void initActionBar(int color) {
		// 在线参数
		String value = ParamTool.getParam("show_top");
		if (value.equals("0"))
			return;
		// 检查系统版本
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			return;
		// 透明化状态栏
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 添加透明状态栏
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {// 状态栏高度获取成功
			int statusBarHeight = getResources().getDimensionPixelSize(
					resourceId);
			LinearLayout view = (LinearLayout) findViewById(R.id.top_titlebar);

			View statusBarView = new View(this);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					statusBarHeight);
			statusBarView.setLayoutParams(params);
			statusBarView.setBackgroundColor(color);

			view.addView(statusBarView);
		}
	}

	// ////////允许子类调用的方法-辅助
	public void showToast(String text) {
		AndTools.showToast(text);
	}

	/**
	 * 显示弹出式菜单
	 */
	public abstract void showContextMenu();
}
