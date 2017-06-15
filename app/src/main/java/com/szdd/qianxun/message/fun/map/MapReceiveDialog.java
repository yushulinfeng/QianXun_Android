package com.szdd.qianxun.message.fun.map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.baichuan.util.AndTools;
import com.szdd.qianxun.tools.dialog.DialogTitleList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapReceiveDialog extends DialogTitleList {
	private static final int WAIT_TIME = 60 * 1000;
	private boolean is_sure = false;// 状态标记
	private DialogReceiveListener listener = null;
	private int HANDLER_INT = 3;
	private Handler handler;
	private long create_time = 0;
	private boolean time_thread_running = false;

	public MapReceiveDialog(Context context) {
		super(context, "点对点地图");
		setText("\n对方发起点对点地图。\n是否同意对方获取您的位置？\n");
		setPositiveButton("允许", null);// 此处null即可
		setNegativeButton("拒绝", new DialogButtonListener() {
			public void onButtonClick() {
				cancelDialog();
			}
		});
		setCancelable(false);// 就放在这里
	}

	public void setMapCreateTime(long create_time) {
		this.create_time = create_time;
	}

	public void setListener(DialogReceiveListener listener) {
		this.listener = listener;
	}

	// 初始化handler
	private void initHandler() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == HANDLER_INT) {
					long time_rest = (WAIT_TIME - (System.currentTimeMillis() - create_time)) / 1000;
					if (time_rest > 0) {
						if (is_sure) {
							setTitle("请选择模式(" + time_rest + "s)");
						} else {
							setTitle("点对点地图(" + time_rest + "s)");
							tv_text.setText("\n请求已发送。等待对方确认。\n等待中请不要关闭本窗口……\n（"
									+ time_rest + "s无操作视为拒绝）\n");
						}
					} else {
						AndTools.showToast(context, "已拒绝。");
						cancelDialog();
						MapReceiveDialog.this.dismiss();
					}
				}
				super.handleMessage(msg);
			}
		};
	}

	private void startTimeThread() {
		new Thread() {
			public void run() {
				if (create_time == 0)
					return;
				while (time_thread_running
						&& (System.currentTimeMillis() - create_time < WAIT_TIME)) {
					handler.sendEmptyMessage(HANDLER_INT);
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				}
				handler.sendEmptyMessage(HANDLER_INT);
			}
		}.start();
	}

	@Override
	protected void initViewSet() {
		super.initViewSet();
		// 修改确认按钮的事件
		btn_pos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!is_sure) { // 确认开启地图按钮
					is_sure = true;
					setTitle("请选择模式");
					tv_text.setVisibility(View.GONE);
					btn_neg.setText("拒绝请求");
					btn_pos.setVisibility(View.GONE);
					initList();
				}
			}
		});
		time_thread_running = true;
		initHandler();
		startTimeThread();
	}

	private void initList() {
		array = new String[] {
		// "实时模式（将启用地图）",
		"单次模式（仅允许一次）" };
		list.setVisibility(View.VISIBLE);
		tv_divider_h.setVisibility(View.VISIBLE);
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < array.length; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", array[i]);
			items.add(item);
		}
		SimpleAdapter adapter = new SimpleAdapter(context, items,
				R.layout.dialog_title_list_item, new String[] { "name" },
				new int[] { R.id.dialog_title_list_item_tv });
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					// ModeRealTime();
					// break;
					// case 1:
					ModeOneTime();
					break;
				}
				MapReceiveDialog.this.dismiss();
			}
		});
	}

	@Override
	public void onDetachedFromWindow() {// 这个之后再stop
		super.onDetachedFromWindow();
		time_thread_running = false;// 终止时间线程
	}

	private void ModeRealTime() {
		if (listener != null)
			listener.onRealTime();
	}

	private void ModeOneTime() {
		if (listener != null)
			listener.onOneTime();
	}

	private void cancelDialog() {// 所有的退出方式
		if (is_sure) {// 如果已经确认，就做处理
			if (listener != null)
				listener.onCancel();
		} else {
			if (listener != null)
				listener.onCancel();
		}
	}

	public interface DialogReceiveListener {
		public void onRealTime();

		public void onOneTime();

		public void onCancel();
	}

}
