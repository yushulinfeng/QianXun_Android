package com.szdd.qianxun.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.szdd.qianxun.R;
import com.szdd.qianxun.bank.LoadMoreListView.OnLoadMore;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;

public class MainTab_03_ApplySuccessActivity extends Activity implements
		OnLoadMore, OnClickListener {

	private LinearLayout applysuccessxml;
	private LinearLayout asxml;
	private Button cancelapply;
	private Button chatwith;

	private static TextView applynumber;
	private static int applymembernumber;
	private LoadMoreListView lmlistview;
	private List<Map<String, Object>> listItems;
	private AdapterForTabThreeTwo adapterfortabthreetwo;
	private int id;
	private Long username_request;
	private String username_self;
	private String nickname_request;

	private String applyurl = ServerURL.SIGN_UP_URL;
	private String cancelapplyurl = ServerURL.CANCEL_SIGN_URL;
	private TextView applysuccess_tabtop;
	private ImageView tab_03_back_success;

	private JSONObject jsonObj = null;

	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String res;
			switch (msg.what) {
			case 0x111:
				res = (String) msg.obj;
				if (res.equals("failed")) {
					showToast("报名失败!");
					finish();
				} else {

					try {
						Analysis(res);
						asxml.setVisibility(LinearLayout.VISIBLE);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 0x222:
				res = (String) msg.obj;
				int cancelstatus = Integer.parseInt(res);
				if (cancelstatus == -2) {
					showToast("取消失败!");
				} else if (cancelstatus == 1) {
					showToast("你已经取消报名");
					finish();
				} else if (cancelstatus == -3) {
					showToast("未报名，无法取消");
				}
				break;
			default:
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_03_02);
		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		username_request = intent.getLongExtra("username", 0);
		nickname_request = intent.getStringExtra("nickname");
		username_self = InfoTool.getBaseInfo(
				MainTab_03_ApplySuccessActivity.this).getUserId();// 获取当前用户的username
		listItems = new ArrayList<Map<String, Object>>();
		applysuccessxml = (LinearLayout) findViewById(R.id.applysuccessxml);
		asxml = (LinearLayout) findViewById(R.id.applysuccessxml);
		applynumber = (TextView) findViewById(R.id.applynumber);
		cancelapply = (Button) findViewById(R.id.cancelapply);
		chatwith = (Button) findViewById(R.id.chatwith);
		applysuccess_tabtop = (TextView) findViewById(R.id.applysuccess_tabtop);
		tab_03_back_success = (ImageView) findViewById(R.id.tab_03_back_success);
		cancelapply.setOnClickListener(this);
		chatwith.setOnClickListener(this);
		lmlistview = (LoadMoreListView) findViewById(R.id.lmListView_02);
		lmlistview.setLoadMoreListen(this);
		tab_03_back_success.setOnClickListener(this);
		asxml.setVisibility(LinearLayout.INVISIBLE);
		updateDataFromNet();
	}

	// 报名时与后台连接
	private void updateDataFromNet() {

		StaticMethod.POST(MainTab_03_ApplySuccessActivity.this, applyurl,
				new ConnectListener() {
					@Override
					public ConnectDialog showDialog(ConnectDialog dialog) {

						dialog.config(MainTab_03_ApplySuccessActivity.this,
								"正在连接", "请稍后……", true);
						return dialog;
					}

					@Override
					public ConnectList setParam(ConnectList list) {
						JSONObject jsonobject = new JSONObject();
						try {
							jsonobject.put("requestId", "" + id);
							jsonobject.put("username", username_self);
						} catch (org.json.JSONException e1) {
							return null;
						}
						list.put("jsonObj", jsonobject.toString(), false);

						Log.e("jsonobject.toString()",""+jsonobject.toString());
						return list;
					}

					@Override
					public void onResponse(String response) {
						if (response == null || response.equals("")) {
							// 网络错误
							showToast("网络错误,请连接网络后重新加载!");
							finish();
						}else{
							Message msg = new Message();
							msg.what = 0x111;
							msg.obj = response;
							mhandler.sendMessage(msg);
						}

					}
				});
	}

	// 取消报名时跟后台的连接
	private void cancleapplyFromNet() {

		StaticMethod.POST(MainTab_03_ApplySuccessActivity.this, cancelapplyurl,
				new ConnectListener() {
					@Override
					public ConnectDialog showDialog(ConnectDialog dialog) {
						return dialog;
					}

					@Override
					public ConnectList setParam(ConnectList list) {
						JSONObject jsonobject = new JSONObject();
						try {
							jsonobject.put("requestId", "" + id);
							jsonobject.put("username", username_self);
						} catch (org.json.JSONException e1) {
							return null;
						}
						list.put("jsonObj", jsonobject.toString(), false);

						return list;
					}

					@Override
					public void onResponse(String response) {
						if (response == null || response.equals("")) {
							// 网络错误
							showToast("网络错误,请连接网络后重新加载!");
							return;
						}else{
							Message msg = new Message();
							msg.what = 0x222;
							msg.obj = response;
							mhandler.sendMessage(msg);
						}

					}
				});
	}

	/**
	 * 解析
	 * 
	 * @throws JSONException
	 * @throws org.json.JSONException
	 */
	private void Analysis(String jsonStr) {
		/******************* 解析 ***********************/
		JSONArray jsonArray = null;
		// 初始化list数组对象
		JSONObject jsonobject;
		int isapply = 0;
		try {
			jsonobject = new JSONObject(jsonStr);
			// 报名人数
			isapply = jsonobject.getInt("status");
			// 报名失败
			if (isapply == 2) {
				showToast("报名失败!");
				finish();
			} else if (isapply == -2) {// 后台出现问题
				showToast("错误!");
				finish();
			} else if (isapply == -3) {// 已经报名过了，显示报名的界面
				jsonArray = jsonobject.getJSONArray("list");
				applysuccess_tabtop.setText(nickname_request);
				applynumber.setText("" + jsonArray.length());
				JSONObject jsonObject;
				for (int i = 0; i < jsonArray.length(); i++) {
					try {

						jsonObject = jsonArray.getJSONObject(i);
						// 初始化map数组对象
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", jsonObject.getInt("id"));
						map.put("username", jsonObject.getLong("username"));
						map.put("headIcon", jsonObject.getString("headIcon"));

						map.put("nickName", jsonObject.getString("nickName"));
						listItems.add(map);
					} catch (org.json.JSONException e) {
						e.printStackTrace();
					}
				}
				adapterfortabthreetwo = new AdapterForTabThreeTwo(
						MainTab_03_ApplySuccessActivity.this, listItems);
				lmlistview.setAdapter(adapterfortabthreetwo);
				showToast("您已报名了!");
			} else if (isapply == 1) {
				// 报名成功，将结果显示出来
				jsonArray = jsonobject.getJSONArray("list");
				applynumber.setText("" + jsonArray.length());
				applysuccess_tabtop.setText(nickname_request);
				JSONObject jsonObject;
				for (int i = 0; i < jsonArray.length(); i++) {
					try {
						jsonObject = jsonArray.getJSONObject(i);
						// 初始化map数组对象
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", jsonObject.getInt("id"));
						map.put("username", jsonObject.getLong("username"));
						map.put("headIcon", jsonObject.getString("headIcon"));
						map.put("nickName", jsonObject.getString("nickName"));
						listItems.add(map);
					} catch (org.json.JSONException e) {
						e.printStackTrace();
					}
				}
				adapterfortabthreetwo = new AdapterForTabThreeTwo(
						MainTab_03_ApplySuccessActivity.this, listItems);
				lmlistview.setAdapter(adapterfortabthreetwo);
				showToast("报名成功!");
			} else if (isapply == -4) {
				showToast("不允许自己报名!");
				finish();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		int Rid = v.getId();
		switch (Rid) {
		case R.id.cancelapply:
			cancleapplyFromNet();
			break;
		case R.id.chatwith:
			MsgPublicTool.chartTo(MainTab_03_ApplySuccessActivity.this,getIntent().getStringExtra("postUserId"),
					getIntent().getLongExtra("postUserName",0000l));

			break;
		case R.id.tab_03_back_success:
			finish();
			break;
		}
	}

	@Override
	public void loadMore() throws org.json.JSONException {
		showToast("无其他报名人员!");
	}
	private void showToast(String text) {
		QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
	}
}
