package com.szdd.qianxun.more;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.start.login.Login;
import com.szdd.qianxun.start.tool.SafeCheck;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectEasy;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.top.TActivity;

public class AlterPass extends TActivity implements OnClickListener {
	private String pass0, pass1, pass2;
	private EditText et_pass0, et_pass1, et_pass2;
	private Button btn_eye1, btn_eye2;
	private Button btn_login;

	@Override
	public void onCreate() {
		setContentView(R.layout.more_alter_pass);
		setTitle("修改密码");
		showBackButton();

		initView();
	}

	private void initView() {
		et_pass0 = (EditText) findViewById(R.id.alter_pass_et_pass0);
		et_pass1 = (EditText) findViewById(R.id.alter_pass_et_pass1);
		et_pass2 = (EditText) findViewById(R.id.alter_pass_et_pass2);
		btn_eye1 = (Button) findViewById(R.id.alter_pass_btn_eye_pass1);
		btn_eye2 = (Button) findViewById(R.id.alter_pass_btn_eye_pass2);
		btn_login = (Button) findViewById(R.id.alter_pass_btn_sure);

		btn_eye1.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					pass1 = et_pass1.getText().toString();
					et_pass1.setHint(pass1);
					et_pass1.setText("");
					et_pass1.setCursorVisible(false);
					break;
				case MotionEvent.ACTION_UP:
					et_pass1.setHint("6-24位数字字母组合");
					et_pass1.setText(pass1);
					et_pass1.setSelection(pass1.length());
					et_pass1.setCursorVisible(true);
					break;
				}
				return false;
			}
		});
		btn_eye2.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					pass2 = et_pass2.getText().toString();
					et_pass2.setHint(pass2);
					et_pass2.setText("");
					et_pass2.setCursorVisible(false);
					break;
				case MotionEvent.ACTION_UP:
					et_pass2.setHint("再次输入密码");
					et_pass2.setText(pass2);
					et_pass2.setSelection(pass2.length());
					et_pass2.setCursorVisible(true);
					break;
				}
				return false;
			}
		});
		btn_login.setOnClickListener(this);
	}

	private void nextStep() {
		pass0 = et_pass0.getText().toString();
		if (!SafeCheck.checkCode(this, pass0))
			return;
		pass1 = et_pass1.getText().toString();
		pass2 = et_pass2.getText().toString();
		if (!SafeCheck.checkPass(this, pass1, pass2))
			return;
		sendToServer();
	}

	private void sendToServer() {
		ConnectEasy.POST(this, "", new ConnectListener() {
			public ConnectDialog showDialog(ConnectDialog dialog) {
				dialog.config(AlterPass.this, "正在连接", "请稍候……", false);
				return dialog;
			}

			public ConnectList setParam(ConnectList list) {
				list.put("oldpass", pass0);
				list.put("newpass", pass1);
				return list;
			}

			public void onResponse(String response) {
				if (response == null || response.equals("")) {
					showToast("连接失败");
				} else if (response.equals("-1")) {
					showToast("系统错误");
				} else if (response.equals("-2")) {
					showToast("验证码错误");
				} else if (response.equals("-3")) {
					showToast("此号码未注册");
				} else if (response.equals("1")) {// 登录成功
					showToast("修改成功");
					// 自动登录
					Intent intent = new Intent(AlterPass.this, Login.class);
					intent.putExtra("name", InfoTool
							.getBaseInfo(AlterPass.this).getUserId());
					intent.putExtra("pass", pass1);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.alter_pass_btn_sure:
			nextStep();
			break;
		}
	}

	@Override
	public void showContextMenu() {
	}

}
