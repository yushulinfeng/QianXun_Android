package com.szdd.qianxun.sell.orderdetail;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.szdd.qianxun.R;

public class SellOrderDetailActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private LinearLayout layout_picture;
    private Button btn_receive;
    private Button btn_reject;
    private Button btn_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_order_detail);
        init();
    }

    //界面初始化
    public void init() {
        iv_back = (ImageView) this.findViewById(R.id.order_detail_iv_back);
        layout_picture = (LinearLayout) this.findViewById(R.id.order_detail_layout_picture);
        btn_receive = (Button) this.findViewById(R.id.order_detail_btn_receive);
        btn_reject = (Button) this.findViewById(R.id.order_detail_btn_reject);
        btn_chat = (Button) this.findViewById(R.id.order_detail_btn_chat);
        iv_back.setOnClickListener(this);
        btn_receive.setOnClickListener(this);
        btn_reject.setOnClickListener(this);
        layout_picture.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail_iv_back://返回
                finish();
                break;
            case R.id.order_detail_layout_picture://交易快照
                break;
            case R.id.order_detail_btn_receive://接单
                break;
            case R.id.order_detail_btn_reject://拒绝
                break;
            case R.id.order_detail_btn_chat://聊天
                break;
            default:
                break;
        }

    }
}
