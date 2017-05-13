package com.szdd.qianxun.start.register;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.szdd.qianxun.R;
import com.szdd.qianxun.start.check.UserExistListener;
import com.szdd.qianxun.start.check.UserExistTool;
import com.szdd.qianxun.start.tool.SafeCheck;
import com.szdd.qianxun.tools.top.TActivity;

public class Register1 extends TActivity {
	private EditText et_phone;
	private String phone = "";
	private Button btn_next;

	@Override
	public void onCreate() {
		setContentView(R.layout.start_register1);
		setTitle("注册");
		showBackButton();
		initActionBar(getResources().getColor(R.color.topbar_bg));

		initView();
	}

	private void initView() {
		et_phone = (EditText) findViewById(R.id.register1_et_name);
		btn_next = (Button) findViewById(R.id.register1_btn_next);
		btn_next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoNext();
			}
		});
	}

	private void gotoNext() {
		phone = et_phone.getText().toString();
		if (phone.equals("00000000")) {// ////测试专用
			Intent intent = new Intent(Register1.this, Register2.class);
			intent.putExtra("phone", phone);
			startActivity(intent);
			finish();
			return;
		}
		if (SafeCheck.isPhone(phone)) {
			btn_next.setEnabled(false);
			UserExistTool.isExist(this, phone, true, new UserExistListener() {
				public void onResponse(int is_exist) {
					btn_next.setEnabled(true);
					if (is_exist == 0) {
						showToast("网络错误");
					} else if (is_exist == -1) {
						UserExistTool.sendSMS(phone);
						showToast("验证码已发送");
						Intent intent = new Intent(Register1.this,
								Register2.class);
						intent.putExtra("phone", phone);
						startActivity(intent);
						finish();
					} else {
						showToast("此用户已注册");
					}
				}
			});
		} else {
			showToast("请输入正确的号码");
		}
	}

	@Override
	public void showContextMenu() {
	}

}
