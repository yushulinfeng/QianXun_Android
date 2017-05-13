package com.szdd.qianxun.start.register;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

import com.szdd.qianxun.R;
import com.szdd.qianxun.start.check.UserExistTool;
import com.szdd.qianxun.start.tool.SafeCheck;
import com.szdd.qianxun.tools.top.NetTActivity;

public class Register2 extends NetTActivity implements OnClickListener {
	private static final int CDDE_WAIT_TIME = 90;// 验证码等待时间
	private int wait_time = CDDE_WAIT_TIME;
	private EditText et_code, et_pass1, et_pass2;
	private Button btn_eye1, btn_eye2;
	private Button btn_code, btn_next;
	private String phone, code, pass1, pass2;

	@Override
	public void onCreate() {
		setContentView(R.layout.start_register2);
		setTitle("设置密码");
		showBackButton();
		initActionBar(getResources().getColor(R.color.topbar_bg));

		initMessage();
		initView();
		waitOneMinute();// 一分钟后才能再次获取验证码
	}

	private void initMessage() {
		Intent intent = getIntent();
		if (intent != null) {
			phone = intent.getStringExtra("phone");
		} else {
			phone = "";
		}
	}

	private void initView() {
		et_code = (EditText) findViewById(R.id.register2_et_code);
		et_pass1 = (EditText) findViewById(R.id.register2_et_pass1);
		et_pass2 = (EditText) findViewById(R.id.register2_et_pass2);
		btn_eye1 = (Button) findViewById(R.id.register2_btn_eye_pass1);
		btn_eye2 = (Button) findViewById(R.id.register2_btn_eye_pass2);
		btn_code = (Button) findViewById(R.id.register2_btn_getcode);
		btn_next = (Button) findViewById(R.id.register2_btn_next);
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
		btn_code.setOnClickListener(this);
		btn_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register2_btn_getcode:
			getCode();
			break;
		case R.id.register2_btn_next:
			nextStep();
			break;
		}
	}

	private void getCode() {
		if (phone.equals("")) {
			showToast("系统错误");
			return;
		}
		waitOneMinute();
		UserExistTool.sendSMS(phone);
		showToast("验证码已发送");
	}

	private void nextStep() {
		if (phone.equals("")) {
			showToast("系统错误");
			return;
		}
		code = et_code.getText().toString();
		if (!SafeCheck.checkCode(this, code))
			return;
		pass1 = et_pass1.getText().toString();
		pass2 = et_pass2.getText().toString();
		if (!SafeCheck.checkPass(this, pass1, pass2))
			return;
		Intent intent = new Intent(this, Register3.class);
		intent.putExtra("phone", phone);
		intent.putExtra("code", code);
		intent.putExtra("pass", pass1);
		wait_time = -1;// 终止计时线程
		startActivity(intent);
		finish();
	}

	// ////////计时部分//////////

	private void waitOneMinute() {
		btn_code.setEnabled(false);
		startNewThread();
	}

	@Override
	public void receiveMessage(String what) {
		if (what == null) {// 时钟更新
			if (wait_time > 0) {
				btn_code.setText("" + wait_time + "秒");
			} else {
				wait_time = CDDE_WAIT_TIME;
				btn_code.setEnabled(true);
				btn_code.setText("重新获取");
			}
		}
	}

	@Override
	public void newThread() {// 仅用于验证码倒计时
		while (wait_time > 0) {
			wait_time--;
			if (wait_time == 0) {
				sendMessage(null);
				break;// 否则由于主线程修改而造成死循环
			}
			sendMessage(null);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void showContextMenu() {
	}
}
