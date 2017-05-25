package com.szdd.qianxun.start.forget;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.start.check.UserExistListener;
import com.szdd.qianxun.start.check.UserExistTool;
import com.szdd.qianxun.start.login.Login;
import com.szdd.qianxun.start.tool.SafeCheck;
import com.szdd.qianxun.tools.top.TActivity;

public class ForgetFind1 extends TActivity {
    private EditText et_phone;
    private String phone = "";
    private Button btn_next;
    private String normal_alter_phone = null;

    @Override
    public void onCreate() {
        setContentView(R.layout.start_forget_find1);
        setTitle("密码找回");
        showBackButton();
        initActionBar(getResources().getColor(R.color.topbar_bg));

        initMessage();
        initView();
    }

    private void initMessage() {
        normal_alter_phone = getIntent().getStringExtra("normal_alter_phone");
        if (normal_alter_phone == null || normal_alter_phone.trim().equals(""))
            normal_alter_phone = null;
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.forget_find1_et_name);
        btn_next = (Button) findViewById(R.id.forget_find1_btn_next);
        btn_next.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gotoNext();
            }
        });
        if (normal_alter_phone != null) {
            setTitle("修改密码");
            TextView tv_name = (TextView) findViewById(R.id.forget_find1_tv_name);
            tv_name.setText("手机号");
            et_phone.setHint("请确认您的手机号");
            et_phone.setText(normal_alter_phone);
            et_phone.setSelection(normal_alter_phone.length());
            showToast("请确认您的手机号");
        }
    }

    private void gotoNext() {
        phone = et_phone.getText().toString();
        if (phone.equals("00000000")) {// ////测试专用
            Intent intent = new Intent(ForgetFind1.this, ForgetFind2.class);
            intent.putExtra("phone", phone);
            startActivityForResult(intent, 0);
            return;
        }
        if (SafeCheck.isPhone(phone)) {
            if (normal_alter_phone != null) {
                if (!normal_alter_phone.equals(phone)) {
                    showToast("　　　　手机号码有误　　　　\n请输入与您高能号绑定的手机号");
                    return;
                }
            }
            btn_next.setEnabled(false);
            UserExistTool.isExist(this, phone, true, new UserExistListener() {
                public void onResponse(int is_exist) {
                    btn_next.setEnabled(true);
                    if (is_exist == 0) {
                        showToast("网络错误");
                    } else if (is_exist == 1) {
                        UserExistTool.sendSMS(phone);
                        showToast("验证码已发送");
                        Intent intent = new Intent(ForgetFind1.this,
                                ForgetFind2.class);
                        intent.putExtra("phone", phone);
                        startActivityForResult(intent, 0);
                    } else {
                        showToast("此号码未注册");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Login.CODE_NEED_FINISH) {// 产生跳转，就关闭本页
            setResult(Login.CODE_NEED_FINISH);
            finish();
        }
    }
}
