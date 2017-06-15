package com.szdd.qianxun.message.info.credit;

import android.view.View;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.top.TActivity;

public class AlipayCheck extends TActivity implements View.OnClickListener {

    @Override
    public void onCreate() {
        setContentView(R.layout.msg_alipay_check);
        setTitle("支付宝认证");
        setTopColors(R.color.btn_bg_orange_normal);
        showBackButton();
        initActionBar(getResources().getColor(R.color.topbar_bg));

    }

    @Override
    public void showContextMenu() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name_check_btn_submit:
                showToast("网络错误");
                break;
        }
    }

}
