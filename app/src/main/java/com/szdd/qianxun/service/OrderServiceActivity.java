package com.szdd.qianxun.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.tools.PxAndDip;
import com.szdd.qianxun.tools.alipay.AlipayBuilder;
import com.szdd.qianxun.tools.alipay.AlipayListener;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.ServiceType;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.RoundHeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderServiceActivity extends Activity implements View.OnClickListener {
    private TextView tv_add, tv_sub, tv_count, tv_time, tv_price, tv_unit, tv_sum, tv_username, tv_servicename, tv_exange, tv_way;
    private ImageView iv_back, iv_picture;
    private int buy_count = 1;
    private double price = 0;
    private LinearLayout layout_time;
    private boolean isDialogClicked = false;
    private Button btn_submit;
    public int type;
    private String str_username = "", str_servicename = "",
            serviceTime = "", str_price = "",
            str_unit = "", str_serviceWay = "",
            serviceId = "", str_exchange = "",
            serviceDetail = "";
    private RoundHeadView headView;
    private String headicon_url = "";

    private TextView alipay_name, alipay_id;
    private String my_alipay_name = "", my_alipay_id = "";
    private ViewGroup layout1, layout2;
    private AlertDialog dialog;
    private EditText editName, editId, editId2;
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_order_service);
        mySharedPreferences = getSharedPreferences("cashbalance", Context.MODE_PRIVATE);
        editor = mySharedPreferences.edit();
        serviceId = getIntent().getStringExtra("serviceId");
        initView();
        initEvent();
        initMessage();
        initDialog();
        GetSetviceContent(OrderServiceActivity.this);
    }

    //初始化视图
    public void initView() {
        tv_way = (TextView) this.findViewById(R.id.order_service_tv_way);
        tv_exange = (TextView) this.findViewById(R.id.tv_exchange_order);
        iv_picture = (ImageView) this.findViewById(R.id.order_service_picture);
        headView = (RoundHeadView) this.findViewById(R.id.order_service_roundview_icon);
        tv_username = (TextView) this.findViewById(R.id.order_service_tv_username);
        tv_servicename = (TextView) this.findViewById(R.id.order_service_tv_service);
        tv_add = (TextView) this.findViewById(R.id.order_service_tv_add);
        tv_sub = (TextView) this.findViewById(R.id.order_service_tv_sub);
        tv_count = (TextView) this.findViewById(R.id.order_service_tv_count);
        iv_back = (ImageView) this.findViewById(R.id.order_service_iv_back);
        tv_time = (TextView) this.findViewById(R.id.order_service_tv_time);
        tv_price = (TextView) this.findViewById(R.id.order_service_tv_prices);
        layout_time = (LinearLayout) this.findViewById(R.id.order_service_layout_time);
        tv_unit = (TextView) this.findViewById(R.id.order_service_tv_unit);
        tv_sum = (TextView) this.findViewById(R.id.order_service_tv_Moneysum);
        btn_submit = (Button) this.findViewById(R.id.order_service_btn_submit);

        layout1 = (ViewGroup) this.findViewById(R.id.sell_order_layout1);
        layout2 = (ViewGroup) this.findViewById(R.id.sell_order_layout2);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        alipay_name = (TextView) this.findViewById(R.id.sell_order_name);
        alipay_id = (TextView) this.findViewById(R.id.sell_order_alipayid);

        tv_price.setText(price + "");
        tv_unit.setText(str_unit);
        tv_sum.setText((buy_count * price) + "");
    }

    public void initEvent() {
        iv_back.setOnClickListener(this);
        tv_add.setOnClickListener(this);
        tv_sub.setOnClickListener(this);
        layout_time.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_service_tv_add:
                if (str_exchange == null || str_exchange.equals("")) ChangeCountToBuy(1);
                else showToast("此服务不支持选择购买数量");
                break;
            case R.id.order_service_tv_sub:
                if (str_exchange == null || str_exchange.equals("")) ChangeCountToBuy(0);
                else showToast("此服务不支持选择购买数量");
                break;
            case R.id.order_service_iv_back:
                finish();
                break;
            case R.id.order_service_btn_submit:
                if (alipay_name.getText().toString().isEmpty()) showToast("请填写真实姓名");
                else if (alipay_id.getText().toString().isEmpty()) showToast("请填写支付宝账号");
                else if (type == 3) PostOrderToServer();
                else if (tv_time.getText().toString().equals("请选择")) showToast("请选择服务时间");
                else {
                    if (isDialogClicked) {
                        PostOrderToServer();
                    } else {
                        final SercuityDialog dialog = new SercuityDialog(OrderServiceActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog.dismiss();
                                PostOrderToServer();
                            }
                        });
                        dialog.show();
                        dialog.setCancelable(false);
                        isDialogClicked = true;
                    }
                }

                break;
            case R.id.order_service_layout_time:
                if (serviceTime != null && !serviceTime.equals("")) {
                    ChoseTimeDialog dialog = new ChoseTimeDialog(OrderServiceActivity.this, tv_time, serviceTime);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);//////////
                    dialog.show();
                    //dialog.setCanceledOnTouchOutside(true);
                }
                break;
            case R.id.sell_order_layout1:
            case R.id.sell_order_layout2:
                editId.setText(my_alipay_id);
                editId2.setText(my_alipay_id);
                editName.setText(my_alipay_name);
                dialog.show();
                break;
            case R.id.cash_dialog_enter:
                dialogDealInput();
                break;
            case R.id.cash_dialog_cancle:
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                editId.setText("");
                break;
            default:
                break;
        }
    }

    public void ChangeCountToBuy(int flag) {
        String str = tv_count.getText().toString();
        int index = Integer.parseInt(str);
        if (flag == 1) {
            tv_count.setText(++index + "");
            tv_sum.setText((index * price) + "");
            buy_count = index;
        } else {
            if (--index < 1) {
                tv_count.setText("1");
                tv_sum.setText(price + "");
                buy_count = index;
            } else {
                tv_count.setText(index + "");
                tv_sum.setText((index * price) + "");
                buy_count = index;
            }
        }
    }


    private void pay(int ugsId) {
        if (ServerURL.isTest())
            Log.e("EEEEEE-ugsId", ugsId + "");
        /**
         * 构建支付宝请求对象--仅此一个构造器，防止误解
         *
         * @param service_id     服务ID
         * @param service_name   服务名字
         * @param service_detail 服务详细描述
         * @param service_price  服务价格（请确保是数字格式）
         */
        StaticMethod.PAY(this, new AlipayBuilder("" + ugsId, tv_servicename.getText().toString()
                , serviceDetail, tv_sum.getText().toString()), new AlipayListener() {
            @Override
            public void onResult(boolean is_success) {
                showSuccessOrFail(is_success, is_success ? "付款成功" : "付款失败");
            }
        });
    }

    public void PostOrderToServer() {
        StaticMethod.POST(this, ServerURL.ORDER_SERVICE, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userGetService.number", tv_count.getText().toString());//购买数量
                list.put("userGetService.businessService.id", serviceId);//服务id
                list.put("userGetService.user.id", InfoTool.getUserID(OrderServiceActivity.this));//用户id
                list.put("userGetService.price", tv_price.getText().toString());//服务价格
                list.put("userGetService.extimateTime", tv_time.getText().toString());//服务截止时间
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                dialog.config(OrderServiceActivity.this, "发送中...", "订单提交中...", false);
                return dialog;
            }

            @Override
            public void onResponse(String res) {
                if (res == null)
                    showToast("未知错误发生了");
                else {
                    int res_int = Integer.parseInt(res);
                    if (res_int > 0) pay(res_int);
                    else switch (res_int) {
                        case -1:
                            showToast("预约失败");
                            break;
                        case -2:
                            showToast("服务器出错");
                            break;
                        case -3:
                            showToast("该服务已下线");
                            break;
                        case -4:
                            showToast("该服务不存在");
                            break;
                        default:

                            break;
                    }
                }
            }
        });
    }

    public void GetSetviceContent(final Context context) {
        StaticMethod.POST(context, ServerURL.GET_SETVICE_ORDER, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("serviceId", serviceId);
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                dialog.config(context, "发送中...", "玩命加载中...", true);
                return dialog;
            }

            @Override
            public void onResponse(String response) {//这里没写判断啊!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                if (response == null) {

                } else if (response.startsWith("{")) AnalyData(response);
                else showToast("网络出错");
            }
        });
    }

    public void AnalyData(String str) {
        JSONArray img_array = null;
        JSONObject jsonObject = null;
        if (str == null) {

        } else {
            try {
                jsonObject = new JSONObject(str);
                type = Integer.parseInt(jsonObject.getString("serviceType"));
                if (type == 3) layout_time.setVisibility(View.GONE);
                else {
                    serviceTime = jsonObject.getString("serviceTime");
                    if (serviceTime.equals("") || serviceTime == null)
                        layout_time.setVisibility(View.GONE);
                }
                if (jsonObject.getString("reward_money").equals("")
                        || jsonObject.getString("reward_money").equals("0")
                        || jsonObject.getString("reward_money").equals("0.00")
                        || jsonObject.getString("reward_unit").equals("")) {
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    btn_submit.setText("预约");
                    str_price = null;
                    tv_price.setVisibility(View.GONE);
                    tv_unit.setVisibility(View.GONE);
                    tv_exange.setText(jsonObject.getString("reward_thing"));
                    tv_sum.setText("无");
                } else if (jsonObject.getString("reward_thing").equals("")) {
                    str_exchange = null;
                    str_price = jsonObject.getString("reward_money");
                    price = Double.parseDouble(str_price);
                    tv_price.setText(str_price);
                    tv_sum.setText(str_price);
                    tv_unit.setText(jsonObject.getString("reward_unit"));
                }
                tv_servicename.setText(jsonObject.getString("name"));
                serviceDetail = jsonObject.getString("detail");
                tv_way.setText(ServiceType.getServiceWay(type));
                JSONObject ob = jsonObject.getJSONObject("user");
                if (ob == null) {
                } else {
                    tv_username.setText(ob.getString("nickName"));
                    headicon_url = ob.getString("headIcon");
                    if (headicon_url == null || headicon_url.equals("")) {

                    } else
                        StaticMethod.BITMAPHEAD(this, ServerURL.getIP() + headicon_url, headView);

                }
            } catch (Exception e) {
            }
        }
        try {
            img_array = jsonObject.getJSONArray("images");
            if (img_array == null || img_array.length() == 0) {

            } else {
                try {
                    String path = img_array.getJSONObject(0).getString("link");
                    StaticMethod.BITMAP(OrderServiceActivity.this, ServerURL.getIP() + path, iv_picture);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void initDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        View dialog_view = factory.inflate(R.layout.cash_balance_detail_dialog, null);
        dialog = new AlertDialog.Builder(this).setView(dialog_view).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog_view.findViewById(R.id.cash_dialog_enter).setOnClickListener(this);
        dialog_view.findViewById(R.id.cash_dialog_cancle).setOnClickListener(this);
        editName = (EditText) dialog_view.findViewById(R.id.cash_dialog_editname);
        editId = (EditText) dialog_view.findViewById(R.id.cash_dialog_editid);
        editId2 = (EditText) dialog_view.findViewById(R.id.cash_dialog_editid2);
    }

    private void initMessage() {
        my_alipay_name = mySharedPreferences.getString("alipayname", "");
        my_alipay_id = mySharedPreferences.getString("alipayid", "");
        if (!my_alipay_name.equals("") && !my_alipay_id.equals("")) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        }
        alipay_name.setText(my_alipay_name);
        alipay_id.setText(my_alipay_id);
    }

    private void dialogDealInput() {
        if (editName.getText().toString().isEmpty()) {
            showToast("姓名不能为空");
            return;
        } else if (editId.getText().toString().isEmpty() || editId2.getText().toString().isEmpty()) {
            showToast("账号不能为空");
            return;
        } else if (editId.getText().toString().equals(editId2.getText().toString()))
            alipay_id.setText(editId.getText());
        else {
            showToast("两次账号输入不一致");
            return;
        }
        my_alipay_name = editName.getText().toString();
        my_alipay_id = editId.getText().toString();
        alipay_name.setText(my_alipay_name);
        alipay_id.setText(my_alipay_id);
        editor.putString("alipayname", my_alipay_name);
        editor.putString("alipayid", my_alipay_id);
        editor.commit();
        if (alipay_id.getText().toString().equals("")) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        } else {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        }
        dialog.dismiss();
    }

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }


    private void showSuccessOrFail(final boolean isOK, String text) {
        LayoutInflater factory = LayoutInflater.from(this);
        View dialog_view = null;
        if (isOK) dialog_view = factory.inflate(R.layout.sell_order_service_success, null);
        else dialog_view = factory.inflate(R.layout.sell_order_service_fail, null);
        final AlertDialog sv_dialog = new AlertDialog.Builder(this).setView(dialog_view).create();
        sv_dialog.show();
        Window window = sv_dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;
        lp.width = PxAndDip.dipTopx(this, 250);
        window.setAttributes(lp);
        dialog_view.findViewById(R.id.sell_order_service_frame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOK) finish();
                else sv_dialog.dismiss();
            }
        });
        ((TextView) dialog_view.findViewById(R.id.sell_order_service_text)).setText(text);
    }
}
