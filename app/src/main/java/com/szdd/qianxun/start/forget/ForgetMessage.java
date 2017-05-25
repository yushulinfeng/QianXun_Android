package com.szdd.qianxun.start.forget;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.szdd.qianxun.R;
import com.szdd.qianxun.start.check.UserExistListener;
import com.szdd.qianxun.start.check.UserExistTool;
import com.szdd.qianxun.start.login.Login;
import com.szdd.qianxun.start.tool.SafeCheck;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectEasy;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.top.NetTActivity;

public class ForgetMessage extends NetTActivity implements OnClickListener {
    private static final int CDDE_WAIT_TIME = 60;// 验证码等待时间
    private int wait_time = CDDE_WAIT_TIME;
    private String phone = "", code = "";
    private EditText et_phone, et_code;
    private Button btn_code, btn_login;

    @Override
    public void onCreate() {
        setContentView(R.layout.start_forget_message);
        setTitle("短信验证登录");
        showBackButton();
        initActionBar(getResources().getColor(R.color.topbar_bg));

        initView();
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.forget_mess_et_name);
        et_code = (EditText) findViewById(R.id.forget_mess_et_code);
        btn_code = (Button) findViewById(R.id.forget_mess_btn_getcode);
        btn_login = (Button) findViewById(R.id.forget_mess_btn_login);
        btn_code.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_mess_btn_getcode:
                phone = et_phone.getText().toString();
                if (SafeCheck.isPhone(phone))
                    getCode();
                else
                    showToast("请输入正确的手机号码");
                break;
            case R.id.forget_mess_btn_login:
                phone = et_phone.getText().toString();
                if (SafeCheck.isPhone(phone)) {
                    code = et_code.getText().toString();
                    if (SafeCheck.checkCode(this, code))
                        startLogin();
                } else
                    showToast("请输入正确的手机号码");
                break;
        }
    }

    private void getCode() {
        UserExistTool.isExist(this, phone, true, new UserExistListener() {
            public void onResponse(int is_exist) {
                if (is_exist == 0) {
                    showToast("网络错误");
                } else if (is_exist == 1) {
                    waitOneMinute();
                    UserExistTool.sendSMS(phone);
                    showToast("验证码已发送");
                } else {
                    showToast("此号码未注册");
                }
            }
        });
    }

    private void startLogin() {
        ConnectEasy.POST(this, ServerURL.CODE_FORGET_LOGIN,
                new ConnectListener() {
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        dialog.config(ForgetMessage.this, "正在登录", "请稍候……",
                                false);
                        return dialog;
                    }

                    public ConnectList setParam(ConnectList list) {
                        list.put("username", phone);
                        list.put("checkCode", code);
                        list.put("appSecret", Login.APP_SECRET);
                        return list;
                    }

                    public void onResponse(String response) {
                        if (response == null || response.equals("")) {
                            showToast("连接失败");
                        } else if (response.equals("-2")
                                || response.equals("failed")) {
                            showToast("系统错误");
                        } else if (response.equals("-1")) {
                            showToast("验证码错误");
                        } else {// 登录成功，交给主界面处理
                            showToast("登录成功");
                            loginSuccess(response);
                        }
                    }
                });
    }

    private void loginSuccess(String response) {
        wait_time = -1;
        //界面切换
        setResult(Login.CODE_NEED_FINISH);
        Intent intent = new Intent(ForgetMessage.this,
                Login.class);
        intent.putExtra("name", phone);
        intent.putExtra("pass", "");
        intent.putExtra("is_forget_login", response);
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
