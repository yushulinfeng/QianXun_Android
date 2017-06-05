package com.szdd.qianxun.sell.orders.buy;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.top.FragmentTActivity;
import com.szdd.qianxun.tools.views.QianxunToast;

/**
 * Created by Administrator on 2016/4/24 0024.
 */
public class BuyRefund extends FragmentTActivity implements View.OnClickListener {
    private LinearLayout reason01, reason02, reason03, reason04, reason05;
    private ImageView img01, img02, img03, img04, img05;
    private Button comfirm;
    private String reason = "";
    private int ugsId;
    private int status = 0;
    private Intent intent;

    @Override
    public void onCreate() {
        setContentView(R.layout.buy_refund);
        setTitle("拒绝订单");
        intent = new Intent();
        showBackButton();
        initActionBar(getResources().getColor(R.color.topbar_bg));
        init();
    }

    private void init() {
        ugsId = getIntent().getIntExtra("ugsId", 0);
        reason01 = (LinearLayout) findViewById(R.id.buy_refund_ll_reson01);
        reason01.setOnClickListener(this);
        reason02 = (LinearLayout) findViewById(R.id.buy_refund_ll_reson02);
        reason02.setOnClickListener(this);
        reason03 = (LinearLayout) findViewById(R.id.buy_refund_ll_reson03);
        reason03.setOnClickListener(this);
        reason04 = (LinearLayout) findViewById(R.id.buy_refund_ll_reson04);
        reason04.setOnClickListener(this);
        reason05 = (LinearLayout) findViewById(R.id.buy_refund_ll_reson05);
        reason05.setOnClickListener(this);

        img01 = (ImageView) findViewById(R.id.buy_refund_img01);
        img02 = (ImageView) findViewById(R.id.buy_refund_img02);
        img03 = (ImageView) findViewById(R.id.buy_refund_img03);
        img04 = (ImageView) findViewById(R.id.buy_refund_img04);
        img05 = (ImageView) findViewById(R.id.buy_refund_img05);
        comfirm = (Button) findViewById(R.id.buy_refund_btn_comfirm);
        comfirm.setOnClickListener(this);
    }

    @Override
    public void showContextMenu() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent.putExtra("status", status);
        setResult(RESULT_OK);
        finish();
    }

    public void showBackButton() {
        ImageButton top_back = (ImageButton) findViewById(R.id.top_back);
        top_back.setVisibility(ImageButton.VISIBLE);
        top_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.putExtra("status", status);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int Rid = view.getId();
        switch (Rid) {
            case R.id.buy_refund_ll_reson01:
                reason = "临时有事，不能完成！";
                img01.setImageResource(R.drawable.refuseorderrightclik);
                img02.setImageResource(R.drawable.refuseorderright);
                img03.setImageResource(R.drawable.refuseorderright);
                img04.setImageResource(R.drawable.refuseorderright);
                img05.setImageResource(R.drawable.refuseorderright);
                break;
            case R.id.buy_refund_ll_reson02:
                reason = "买家要求太过个性，宝宝心里苦！";
                img02.setImageResource(R.drawable.refuseorderrightclik);
                img01.setImageResource(R.drawable.refuseorderright);
                img03.setImageResource(R.drawable.refuseorderright);
                img04.setImageResource(R.drawable.refuseorderright);
                img05.setImageResource(R.drawable.refuseorderright);
                break;
            case R.id.buy_refund_ll_reson03:
                reason = "本月工资已到账，打烊休息！";
                img03.setImageResource(R.drawable.refuseorderrightclik);
                img01.setImageResource(R.drawable.refuseorderright);
                img02.setImageResource(R.drawable.refuseorderright);
                img04.setImageResource(R.drawable.refuseorderright);
                img05.setImageResource(R.drawable.refuseorderright);
                break;
            case R.id.buy_refund_ll_reson04:
                reason = "材料不足，无法按时完成！";
                img04.setImageResource(R.drawable.refuseorderrightclik);
                img01.setImageResource(R.drawable.refuseorderright);
                img03.setImageResource(R.drawable.refuseorderright);
                img02.setImageResource(R.drawable.refuseorderright);
                img05.setImageResource(R.drawable.refuseorderright);
                break;
            case R.id.buy_refund_ll_reson05:
                img05.setImageResource(R.drawable.refuseorderrightclik);
                img01.setImageResource(R.drawable.refuseorderright);
                img03.setImageResource(R.drawable.refuseorderright);
                img04.setImageResource(R.drawable.refuseorderright);
                img02.setImageResource(R.drawable.refuseorderright);
                reason = "其他！";
                break;
            case R.id.buy_refund_btn_comfirm:
                if (reason.equals("")) {
                    showToast("请选择理由");
                } else {
                    postRefund();
                }
                break;
        }
    }

    private void postRefund() {
        StaticMethod.POST(BuyRefund.this, ServerURL.ALL_ORDER_REFUNDIP_URL,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        dialog.config(BuyRefund.this, "正在连接", "请稍后……", true);
                        return dialog;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {

                        list.put("ugsId", "" + ugsId, false);
                        list.put("reason", reason);

                        return list;
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response == null || response.equals("")) {
                            // 网络错误
                            showToast("网络错误,请连接网络后重新加载");
                            return;
                        } else {
                            int statuss = Integer.parseInt(response);
                            if (statuss == 1) {
                                showToast("成功提交退款申请");
                                finish();
                            } else if (statuss == -1) {
                                showToast("提交退款申请失败");
                            } else if (statuss == -2) {
                                showToast("服务器出错");
                            } else if (statuss == -3) {
                                showToast("服务器状态不正常");
                            }
                        }

                    }
                });
    }

    public void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }

}
