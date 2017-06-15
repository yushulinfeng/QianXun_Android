package com.szdd.qianxun.service;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by DELL on 2016/4/1.
 */
public class SellOrderDetailActivitySale extends Activity implements View.OnClickListener {
    private ImageView iv_servicePicture, iv_back;
    private TextView tv_serviceName, tv_price, tv_unit, tv_buyerName, tv_serviceTime, tv_count, tv_srviceWay, tv_payTime, tv_income;
    private Button btn_chat, btn_receive, btn_reject;
    private LinearLayout layout_buyerInfo;
    private String ugsid = "";
    private String str_userId = "", str_username = "";
    private Button btn_chat_finish, btn_finish, btn_chat_refound, btn_refound, btn_reject_refound;
    private LinearLayout layout_finish, layout_order, layout_refound, layout_serviceTime;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_order_detail_sale);
        ugsid = getIntent().getStringExtra("ugsId");
        initView();
        initEvent();
        getOrder(SellOrderDetailActivitySale.this);
    }

    public void initView() {
        layout_finish = (LinearLayout) findViewById(R.id.layout_finish);
        layout_order = (LinearLayout) findViewById(R.id.layout_order);
        layout_refound = (LinearLayout) findViewById(R.id.layout_refound);
        layout_serviceTime = (LinearLayout) findViewById(R.id.layout_serviceTime);
        String come = getIntent().getStringExtra("come");
        if (come.equals("finish")) {
            layout_finish.setVisibility(View.VISIBLE);
            layout_order.setVisibility(View.GONE);
            layout_refound.setVisibility(View.GONE);
        } else if (come.equals("order")) {
            layout_finish.setVisibility(View.GONE);
            layout_order.setVisibility(View.VISIBLE);
            layout_refound.setVisibility(View.GONE);
        } else if (come.equals("refound")) {
            layout_finish.setVisibility(View.GONE);
            layout_order.setVisibility(View.GONE);
            layout_refound.setVisibility(View.VISIBLE);
        } else if (come.equals("other")) {
            layout_finish.setVisibility(View.GONE);
            layout_order.setVisibility(View.GONE);
            layout_refound.setVisibility(View.GONE);
        }
        btn_finish = (Button) findViewById(R.id.order_detail_btn_finish);
        btn_chat_finish = (Button) findViewById(R.id.order_detail_btn_chat_finish);
        btn_chat_refound = (Button) findViewById(R.id.order_detail_btn_chat_refound);
        btn_refound = (Button) findViewById(R.id.order_detail_btn_refound);
        btn_reject_refound = (Button) findViewById(R.id.order_detail_btn_reject_refound);
        tv_income = (TextView) findViewById(R.id.order_detail_tv_income_sale);
        iv_back = (ImageView) findViewById(R.id.order_detail_sale_iv_back);
        iv_servicePicture = (ImageView) findViewById(R.id.order_service_picture_sale);
        tv_serviceName = (TextView) findViewById(R.id.order_service_tv_service_sale);
        tv_price = (TextView) findViewById(R.id.order_service_tv_prices_sale);
        tv_unit = (TextView) findViewById(R.id.order_service_tv_unit_sale);
        tv_buyerName = (TextView) findViewById(R.id.order_detail_tv_buyer_nickname);
        tv_serviceTime = (TextView) findViewById(R.id.order_detail_tv_time_sale);
        tv_count = (TextView) findViewById(R.id.order_detail_tv_buy_count_sale);
        tv_srviceWay = (TextView) findViewById(R.id.order_detail_tv_way_service_sale);
        tv_payTime = (TextView) findViewById(R.id.order_detail_tv_time_pay_sale);
        btn_chat = (Button) findViewById(R.id.order_detail_btn_chat);
        btn_receive = (Button) findViewById(R.id.order_detail_btn_reject);
        btn_reject = (Button) findViewById(R.id.order_detail_btn_receive);
        layout_buyerInfo = (LinearLayout) findViewById(R.id.order_detail_show_buyer_info);
    }

    public void initEvent() {
        btn_chat_refound.setOnClickListener(this);
        btn_refound.setOnClickListener(this);
        btn_finish.setOnClickListener(this);
        btn_chat_finish.setOnClickListener(this);
        layout_buyerInfo.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        btn_receive.setOnClickListener(this);
        btn_reject.setOnClickListener(this);
    }

    public void getOrder(final Context context) {
        StaticMethod.POST(context, ServerURL.GET_ORDER_SALE, new ConnectListener() {
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
                    showToast("未知错误！");
                } else if (response.startsWith("{")) {
                    try {
                        Analysize(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    public void Analysize(String jsonString) {
        JSONObject jsonObject = null;
        JSONArray array = null;
        try {
            jsonObject = new JSONObject(jsonString);
            //服务详情
            JSONObject jsonService = jsonObject.getJSONObject("businessService");
            tv_serviceName.setText(jsonService.getString("name"));
            tv_price.setText(jsonService.getString("reward_money"));
            tv_unit.setText(jsonService.getString("reward_unit"));
            int price = Integer.parseInt(jsonService.getString("reward_money"));
            int count = Integer.parseInt(jsonObject.getString("number"));
            tv_income.setText(price * count + "元");
            //买家详情
            JSONObject jsonUser = jsonObject.getJSONObject("user");
            str_userId = jsonUser.getString("id");
            str_username = jsonUser.getString("username");
            tv_buyerName.setText(jsonUser.getString("nickName"));
            //订单详情
            String serviceTime = jsonObject.getString("extimateTime");
            if (serviceTime.equals("请选择")) {
                layout_serviceTime.setVisibility(View.GONE);
            } else {
                tv_serviceTime.setText(serviceTime);
            }
            String timeSeconds = jsonObject.getString("startTime");
            if (timeSeconds == null || timeSeconds.equals("")) {
                tv_payTime.setText("未知时间");
            }
            timeSeconds = StaticMethod.formatDateTime("yyyy-MM-dd hh:mm:ss",jsonObject.getLong("startTime"));
            tv_payTime.setText(timeSeconds);
            tv_count.setText(jsonObject.getString("number"));
            int serviceWay = Integer.parseInt(jsonService.getString("serviceType"));
            tv_srviceWay.setText(ServiceType.getServiceWay(serviceWay));
            array = jsonService.getJSONArray("images");
            if (array.length() == 0) {
            } else {
                String picture_url = array.getJSONObject(0).getString("link");
                StaticMethod.BITMAP(SellOrderDetailActivitySale.this, ServerURL.getIP() + picture_url, iv_servicePicture);
            }
        } catch (Exception e) {

        }
    }

    //接单
    public void AgreeOrReject(final Context context, int falg) {
        String url = "";
        if (falg == 1) {//接单
            url = ServerURL.ORDER_SALE_AGREE;
        } else if (falg == -1) {//拒绝
            url = ServerURL.ORDER_SALE_REJECT;
        }
        StaticMethod.POST(context, url, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("ugsId", ugsid);
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                dialog.config(context, "发送中...", "请稍等...", false);
                return dialog;
            }

            @Override
            public void onResponse(String response) {
                int i = Integer.parseInt(response);
                switch (i) {
                    case -1:
                        showToast("操作失败");
                        break;
                    case 1:
                        showToast("操作成功");
                        finish();
                        break;
                    case -2:
                        showToast("服务器出错");
                        break;
                    case -3:
                        showToast("该订单未处在等待接单状态");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail_sale_iv_back:
                finish();
                break;
            case R.id.order_detail_show_buyer_info://跳到购买者的个人主页
                MsgPublicTool.showHomePage(SellOrderDetailActivitySale.this, str_userId);
                break;
            case R.id.order_detail_btn_chat://与买家沟通
                MsgPublicTool.chartTo(SellOrderDetailActivitySale.this, str_userId, Long.parseLong(str_username));
                break;
            case R.id.order_detail_btn_chat_finish://与买家沟通
                MsgPublicTool.chartTo(SellOrderDetailActivitySale.this, str_userId, Long.parseLong(str_username));
                break;
            case R.id.order_detail_btn_chat_refound://与买家沟通
                MsgPublicTool.chartTo(SellOrderDetailActivitySale.this, str_userId, Long.parseLong(str_username));
                break;
            case R.id.order_detail_btn_finish:
                Finish(SellOrderDetailActivitySale.this);
                break;
            case R.id.order_detail_btn_refound://同意退款
                RejectOrDisagreeRefound(SellOrderDetailActivitySale.this, 1);
                break;
            case R.id.order_detail_btn_reject_refound://拒绝退款
                RejectOrDisagreeRefound(SellOrderDetailActivitySale.this, -1);
                break;
            case R.id.order_detail_btn_reject://拒绝
                AgreeOrReject(SellOrderDetailActivitySale.this, -1);
                break;
            case R.id.order_detail_btn_receive://接单
                AgreeOrReject(SellOrderDetailActivitySale.this, 1);
                break;
            default:
                break;
        }

    }

    public void Finish(final Context context) {
        StaticMethod.POST(context, ServerURL.ALL_ORDER_FINISHORDER_URL,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        return null;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("ugsId", "" + ugsid, false);
                        return list;
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response == null || response.equals("网络错误,请连接网络后重新加载")) {
                            showToast("");
                            return;
                        } else {
                            int statuss = Integer.parseInt(response);
                            if (statuss == 1) {
                                showToast("成功接受预约");
                            } else if (statuss == -1) {
                                showToast("接受预约失败");
                            } else if (statuss == -2) {
                                showToast("服务器出错");
                            } else if (statuss == -3) {
                                showToast("服务器状态异常");
                            }
                        }
                    }
                });
    }

    public void RejectOrDisagreeRefound(final Context context, int i) {
        String url = "";
        if (i == 1) {
            url = ServerURL.ALL_ORDER_CONFIRMREFUND_URL;//同意
        } else if (i == -1) {
            url = ServerURL.ALL_ORDER_REFUSEREFUND_URL;//拒绝
        }
        StaticMethod.POST(context, url, new ConnectListener() {
            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                dialog.config(context, "正在连接", "请稍后……", true);
                return dialog;
            }

            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("ugsId", "" + ugsid, false);
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
                        showToast("成功接受预约");
                    } else if (statuss == -1) {
                        showToast("接受预约失败");
                    } else if (statuss == -2) {
                        showToast("服务器出错");
                    } else if (statuss == -3) {
                        showToast("服务器状态不正常");
                    }
                }
            }
        });
    }

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }
}
