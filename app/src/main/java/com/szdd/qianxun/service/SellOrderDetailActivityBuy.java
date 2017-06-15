package com.szdd.qianxun.service;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.ServiceType;
import com.szdd.qianxun.tools.views.QianxunToast;

import org.json.JSONObject;

/**
 * Created by DELL on 2016/3/25.
 */
public class SellOrderDetailActivityBuy extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private LinearLayout layout_showallinfo;
    private TextView tv_payout, tv_serviceTime, tv_serviceCount, tv_serviceWay, tv_paytime, tv_salename;
    private String str_username = "", str_userId = "";
    private String ugsid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_order_detail_buy);
        ugsid = getIntent().getStringExtra("ugsId");
        init();
        initEvent();
        getOrderDetail(SellOrderDetailActivityBuy.this);
    }

    //界面初始化
    public void init() {

        tv_salename = (TextView) this.findViewById(R.id.order_detail_tv_sale_nickname);
        tv_serviceWay = (TextView) this.findViewById(R.id.order_detail_tv_way_service_buy);
        tv_serviceTime = (TextView) this.findViewById(R.id.order_detail_tv_time_buy);
        tv_serviceCount = (TextView) this.findViewById(R.id.order_detail_tv_buy_count_buy);
        tv_paytime = (TextView) this.findViewById(R.id.order_detail_tv_time_pay_buy);
        tv_payout = (TextView) this.findViewById(R.id.order_detail_tv_payout);
        iv_back = (ImageView) this.findViewById(R.id.order_detail_iv_back);
        layout_showallinfo = (LinearLayout) this.findViewById(R.id.order_detail_show_sale_info);

    }

    public void initEvent() {
        iv_back.setOnClickListener(this);
        layout_showallinfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail_iv_back://返回
                finish();
                break;
            case R.id.order_detail_show_sale_info://展示用户信息
                MsgPublicTool.showHomePage(SellOrderDetailActivityBuy.this, str_userId);
                break;
            default:
                break;
        }
    }

    public void getOrderDetail(final Context context) {
        StaticMethod.POST(context, ServerURL.GET_ORDER_BUY, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("ugsId", ugsid);
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                dialog.config(context, "发送中...", "玩命加载中...", true);
                return dialog;
            }

            @Override
            public void onResponse(String response) {
                if (response == null) {
                    QianxunToast.showToast(context, "未知错误！", QianxunToast.LENGTH_SHORT);
                } else if (response.startsWith("{")) {
                    Analysize(response);
                } else {

                }
            }
        });

    }

    public void Analysize(String jsonString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONObject jsonService = jsonObject.getJSONObject("businessService");
            int price = Integer.parseInt(jsonService.getString("reward_money"));
            int number = Integer.parseInt(jsonObject.getString("number"));
            tv_payout.setText(price * number + "元");
            JSONObject jsonUser = jsonService.getJSONObject("user");
            str_username = jsonUser.getString("username");
            str_userId = jsonUser.getString("id");
            tv_salename.setText(jsonUser.getString("nickName"));
            String time = jsonObject.getString("extimateTime");
            if (time.equals("请选择")) {
                tv_serviceTime.setText("无期限");
            } else {
                tv_serviceTime.setText(time);
            }
            tv_serviceCount.setText(jsonObject.getString("number"));
            int type = Integer.parseInt(jsonService.getString("serviceType"));
            tv_serviceWay.setText(ServiceType.getServiceWay(type));
            String date = StaticMethod.formatDateTime("yyyy-MM-dd hh:mm:ss",jsonObject.getLong("startTime"));
            tv_paytime.setText(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
