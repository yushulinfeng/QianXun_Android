package com.szdd.qianxun.message.fun.map;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import com.szdd.qianxun.message.baichuan.util.AndTools;
import com.szdd.qianxun.tools.dialog.DialogTitleList;

//点对点地图-确认与等待
public class MapStartDialog extends DialogTitleList {
	private boolean is_sure = false;// 状态标记
	private DialogStateListener listener = null;
	private static final int CDDE_WAIT_TIME = 60;// 验证码等待时间
	private static int wait_time = CDDE_WAIT_TIME;
	private int HANDLER_INT = 1;
	private Handler handler;
	private Activity activity;

	public MapStartDialog(Activity activity) {
		super(activity, "点对点地图");
		this.activity = activity;
		initStartUI();
	}

	private void initStartUI() {
		wait_time = CDDE_WAIT_TIME;
		is_sure = false;
		initHandler();
		String text_show = "　　点对点地图是一种方便两人之间确定位置、路线的工具。地图的开启需要对方的确认。";
		setText(text_show + "\n\n　　确认开启？");
		setPositiveButton("确认", null);// 此处null即可
		setNegativeButton("取消", new DialogButtonListener() {
			public void onButtonClick() {
				cancelDialog();
			}
		});
	}

	// 初始化handler
	private void initHandler() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == HANDLER_INT) {
					if (wait_time > 0) {
						tv_text.setText("\n请求已发送。等待对方确认。\n等待中请不要关闭本窗口……\n（"
								+ wait_time + "s无回应视为对方拒绝）\n");
					} else if (wait_time == 0) {
						wait_time = -1;
						AndTools.showToast(context, "未收到对方回应。\n已视为对方拒绝。");
						cancelDialog();
						dismissSelf();
					}
					// <0不必处理
				}
				super.handleMessage(msg);
			}
		};
	}

	private void startTimeThread() {
		new Thread() {
			public void run() {
				while (wait_time > 0) {
					wait_time--;
					if (wait_time == 0) {
						handler.sendEmptyMessage(HANDLER_INT);
						break;// 否则由于主线程修改而造成死循环
					}
					handler.sendEmptyMessage(HANDLER_INT);
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				}
			}
		}.start();
	}

	@Override
	protected void initViewSet() {
		if (wait_time == -1) {
			initStartUI();
		}
		super.initViewSet();
		// 修改确认按钮的事件
		btn_pos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (is_sure) {// 测试按钮，到时候应该需要删除
					if (listener != null)
						listener.onTest();
					dismissSelf();
				} else {// 确认开启地图按钮
					is_sure = true;
					setCancelable(false);
					tv_text.setText("\n请求已发送。等待对方确认。\n等待中请不要关闭本窗口……\n（"
							+ wait_time + "s无回应视为对方拒绝）\n");
					btn_neg.setText("取消请求");
					// btn_pos.setText("测试");// //////////////////
					btn_pos.setVisibility(View.GONE);
					startTimeThread();
					if (listener != null)
						listener.onSure();
				}
			}
		});
	}

	public void setListener(DialogStateListener listener) {
		this.listener = listener;
	}

	private void cancelDialog() {// 所有的退出方式
		if (is_sure) {// 如果已经确认，就做处理
			if (listener != null)
				listener.onCancel();
		}
	}

	@SuppressWarnings("deprecation")
	private void dismissSelf() {
		this.dismiss();
		// activity.dismissDialog(SingleChatActivity.ID_MAP_START_DIA);
		// activity.removeDialog(SingleChatActivity.ID_MAP_START_DIA);
	}

	@Override
	public void onDetachedFromWindow() {// 这个之后再stop
		super.onDetachedFromWindow();
		wait_time = -1;// 终止时间线程
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!is_sure)
				dismissSelf();
			// 已经确认的，就不允许back键退出了
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public interface DialogStateListener {
		public void onSure();

		public void onCancel();

		public void onTest();
	}
}
