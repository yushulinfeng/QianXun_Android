package com.szdd.qianxun.message.friend;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.baichuan.util.AndTools;
import com.szdd.qianxun.start.check.UserExistListener;
import com.szdd.qianxun.start.check.UserExistTool;
import com.szdd.qianxun.tools.top.NetTActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendListBook extends NetTActivity {
	private static final int THREAD_HANDLER_INT = 0;
	private TextView tv_load;
	private ListView list;
	private List<Map<String, Object>> listItems;
	private List<Map<String, Object>> listItems_temp;
	private SimpleAdapter simpleAdapter;
	private Cursor cursor;
	private ContentResolver cr;
	private Handler thrad_handler;
	private boolean need_finish = false;
	private boolean is_dealing = false;// 是否正在处理信息

	@Override
	public void onCreate() {
		setContentView(R.layout.msg_friend_list_book);
		setTitle("通讯录");
		showBackButton();
		initActionBar(getResources().getColor(R.color.topbar_bg));

		initView();
		// 直接读取竟然会卡顿
		tv_load.setText("");// 加载中\n请稍候……（loading就不必了）
		need_finish = false;
		is_dealing = false;
		startNewThread();
	}

	private void updateList() {
		for (int i = 0; i < listItems_temp.size(); i++) {
			listItems.add(listItems_temp.get(i));
		}
		if (listItems.size() == 0)
			tv_load.setText("通讯录\n为空");
		else
			simpleAdapter.notifyDataSetChanged();
		// 这里，在更新时，疯狂的快速滑动列表会有不可捕获的运行时异常
		// 然后谷歌到，主线程更新界面时，有多线程在添加列表项（相当于多线程也在修改界面）
		// 放在主线程中添加列表项即可，但是会界面卡顿，我添加了sleep延时，出错概率降低了很多。
		// 但是，刷新时点击依旧报错
		// 后来，我使用计时器思路，仍然卡顿。没有搜索到很好的方法。
		// 最后的最后，我想起了C++课设，于是使用了双handler控制思路，成功了……
	}

	@Override
	public void receiveMessage(String what) {
		updateList();
		if (what == null)// 读取还没有结束
			thrad_handler.sendEmptyMessage(THREAD_HANDLER_INT);
	}

	@Override
	public void newThread() {
		Looper.prepare();
		initContact();
		thrad_handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case THREAD_HANDLER_INT:
					addConnect();
					break;
				}
			}
		};
		thrad_handler.sendEmptyMessage(THREAD_HANDLER_INT);
		Looper.loop();
	}

	private void initView() {
		tv_load = (TextView) findViewById(R.id.con_contact_load);
		list = (ListView) findViewById(R.id.con_contact_list);

		listItems = new ArrayList<Map<String, Object>>();
		listItems_temp = new ArrayList<Map<String, Object>>();
		simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.msg_friend_list_book_item, new String[] { "name",
						"phone" }, new int[] { R.id.con_contact_item_name,
						R.id.con_contact_item_number });
		list.setAdapter(simpleAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dealItemCLick(position);
			}
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				return true;// 为了长按有震动
			}
		});
	}

	private void initContact() {
		// 得到contentresolver对象
		cr = getContentResolver();
		// 取得电话本中开始一项的光标，必须先moveToNext()
		cursor = cr.query(Contacts.CONTENT_URI, null, null, null,
				Contacts.SORT_KEY_ALTERNATIVE);// 姓名排序
	}

	private void addConnect() {
		int message_count = 0;
		listItems_temp.clear();
		while (!need_finish && message_count < 10 && cursor.moveToNext()) {// 每次加载10条
			message_count++;
			Map<String, Object> listItem = new HashMap<String, Object>();
			// 取得联系人的名字索引
			int nameIndex = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			String contact = cursor.getString(nameIndex);
			listItem.put("name", contact);
			// 取得联系人的ID索引值
			String contactId = cursor.getString(cursor
					.getColumnIndex(Contacts._ID));
			// 查询该位联系人的电话号码，类似的可以查询email，photo
			Cursor phone = cr.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID
					+ " = " + contactId, null, null);
			// 一个人可能有几个号码
			while (phone.moveToNext()) {
				String strPhoneNumber = phone.getString(phone
						.getColumnIndex(Phone.NUMBER));
				listItem.put("phone", strPhoneNumber);
				break;// 此处只获取第一个号码
			}
			phone.close();
			listItems_temp.add(listItem);
		}
		if (message_count < 10)
			sendMessage("END");// 更新界面
		else if (!need_finish)
			sendMessage(null);// 更新界面
	}

	private void dealItemCLick(int position) {
		if (is_dealing)
			return;
		is_dealing = true;
		final String name = (String) listItems.get(position).get("name");
		final String phone = (String) listItems.get(position).get("phone");
		long user_id = 0;
		try {
			if (phone.length() == 11)// 不是11位就不管了
				user_id = Long.parseLong(phone);
		} catch (Exception e) {
			user_id = 0;
		}
		if (user_id == 0) {
			showToast("该用户未注册高能");
			is_dealing = false;
			return;
		}
		final long friend_id = user_id;
		// 连接服务器判断是否此账号已经注册
		UserExistTool.isExist(this, phone, false, new UserExistListener() {
			public void onResponse(int is_exist) {
				if (is_exist == 0) {
					showToast("网络错误");
				} else if (is_exist == 1) {// 用户存在，就创建会话
					//MsgPublicTool.chartTo(FriendListBook.this, friend_id);
				} else {
					AndTools.showToast(FriendListBook.this, "“" + name + "”"
							+ "未注册高能");
				}
				is_dealing = false;
			}
		});
	}

	private void exit() {
		finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	protected void onDestroy() {
		need_finish = true;
		if (cursor != null)
			cursor.close();
		super.onDestroy();
	}

	@Override
	public void showBackButton() {
		ImageButton top_back = (ImageButton) findViewById(R.id.top_back);
		top_back.setVisibility(ImageButton.VISIBLE);
		top_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				exit();
			}
		});
	}

	@Override
	public void showContextMenu() {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
