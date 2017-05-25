package com.szdd.qianxun.start.forget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.szdd.qianxun.R;
import com.szdd.qianxun.start.login.Login;

//返回式启动
public class ForgetMenu extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 对话框全屏代码（写在style中不成功）
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        win.setGravity(Gravity.BOTTOM);
        win.setWindowAnimations(R.style.bottomDialogAnim); // 添加动画
        // 界面
        setContentView(R.layout.start_forget_menu);
        initVIew();
    }

    private void initVIew() {
        Button btn_find = (Button) findViewById(R.id.forget_menu_btn_find);
        Button btn_mess = (Button) findViewById(R.id.forget_menu_btn_message);
        Button btn_cancel = (Button) findViewById(R.id.forget_menu_btn_cancel);
        LinearLayout layout = (LinearLayout) findViewById(R.id.forget_menu_layout);
        btn_find.setOnClickListener(this);
        btn_mess.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.forget_menu_btn_find:
                intent = new Intent(ForgetMenu.this, ForgetFind1.class);
                break;
            case R.id.forget_menu_btn_message:
                intent = new Intent(ForgetMenu.this, ForgetMessage.class);
                break;
            case R.id.forget_menu_btn_cancel:
            case R.id.forget_menu_layout:
                break;
        }
        if (intent != null) {
            startActivityForResult(intent, 0);
        }
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
