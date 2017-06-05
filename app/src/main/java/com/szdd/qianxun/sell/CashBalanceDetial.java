package com.szdd.qianxun.sell;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.tools.PxAndDip;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.start.check.UserExistTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.top.NetActivity;
import com.szdd.qianxun.tools.views.QianxunToast;

import java.text.DecimalFormat;

/**
 * Created by linorz on 2016/4/21.
 */
public class CashBalanceDetial extends NetActivity implements View.OnClickListener {
    private static final int CDDE_WAIT_TIME = 90;// 验证码等待时间
    private int wait_time = CDDE_WAIT_TIME;
    private TextView cashbalance, alipay_name, alipay_id;
    private AlertDialog dialog;
    private EditText editName, editId, editId2, editMoney, editCode;
    private ViewGroup layout1, layout2;
    private String my_alipay_name = "", my_alipay_id = "";
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor editor;
    private Button getCode, sendButton;

    @Override
    public void onCreate() {
        setContentView(R.layout.cash_balance_detail);
        mySharedPreferences = getSharedPreferences("cashbalance", Context.MODE_PRIVATE);
        editor = mySharedPreferences.edit();
        cashbalance = (TextView) findViewById(R.id.cash_detail_money);
        findViewById(R.id.cash_detail_back).setOnClickListener(this);
        alipay_name = (TextView) findViewById(R.id.cash_detail_name);
        alipay_id = (TextView) findViewById(R.id.cash_detail_alipayid);
        layout1 = (ViewGroup) findViewById(R.id.cash_detail_layout1);
        layout1.setOnClickListener(this);
        layout2 = (ViewGroup) findViewById(R.id.cash_detail_layout2);
        layout2.setOnClickListener(this);
        getCode = (Button) findViewById(R.id.cash_detail_getcode);
        getCode.setOnClickListener(this);
        sendButton = (Button) findViewById(R.id.cash_detail_drawmoney);
        sendButton.setOnClickListener(this);
        editCode = (EditText) findViewById(R.id.cash_detail_editcode);
        editMoney = (EditText) findViewById(R.id.cash_detail_edit);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                turnSendButton(checkFillAll());
            }
        };
        editCode.addTextChangedListener(textWatcher);
        editMoney.addTextChangedListener(textWatcher);

        initMessage();//初始化账号
        initDialog();
        getCash();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cash_detail_layout1:
            case R.id.cash_detail_layout2:
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
            case R.id.cash_detail_getcode:
                getCode();
                break;
            case R.id.cash_detail_drawmoney:
                //发送取款请求
                if (dealMoney()) drawMoney();
                break;
            case R.id.cash_detail_back:
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
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
        turnSendButton(checkFillAll());
        dialog.dismiss();
    }

    private boolean dealMoney() {
        try {
            editMoney.setText(String.valueOf(get2Double(Double.parseDouble(editMoney.getText().toString()))));
            double drawmoney = Double.parseDouble(editMoney.getText().toString());
            double cashmoney = Double.parseDouble(cashbalance.getText().toString());
            if (drawmoney == 0) showToast("取款不能为0");
            else if (drawmoney - cashmoney > 0) showToast("没有这么多余额");
            else return true;
        } catch (Exception e) {
            showToast("请输入正确的数字");
        }
        return false;
    }

    private boolean checkFillAll() {

        if (alipay_name.getText().toString().isEmpty()) {
//            showToast("姓名不能为空");
            return false;
        }
        if (alipay_id.getText().toString().isEmpty()) {
//            showToast("支付宝账号不能为空");
            return false;
        }
        if (editMoney.getText().toString().isEmpty()) {
//            showToast("取款金额不能为空");
            return false;
        }
        if (editCode.getText().toString().isEmpty()) {
//            showToast("验证码不能为空");
            return false;
        }
        return true;
    }

    private void getCash() {
        StaticMethod.POST(this, ServerURL.GETMYCASH, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", InfoTool.getUserID(CashBalanceDetial.this));
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                if (response == null) showToast("未知错误发生了");
                else {
                    double res_dou = Double.parseDouble(response);
                    if (res_dou >= 0) cashbalance.setText(response);
                    else switch ((int) res_dou) {
                        case -1:
                            showToast("获取失败");
                            break;
                        case -2:
                            showToast("服务器出错");
                            break;
                        case -3:
                            showToast("未登录");
                            break;
                        case -4:
                            showToast("没有此用户");
                            break;
                        default:
                            showToast("未知错误");
                            break;
                    }
                }
            }
        });
    }

    private void drawMoney() {
        final Context context = this;
        StaticMethod.POST(this, ServerURL.ALIPAY_GETCASH, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("tradeRecord.userId", InfoTool.getUserID(context));
                list.put("tradeRecord.account_name", my_alipay_name);
                list.put("tradeRecord.account_email", my_alipay_id);
                list.put("tradeRecord.account_fee", editMoney.getText().toString());
                list.put("tradeRecord.account_note", "高能用户个人提现");
                list.put("tradeRecord.checkCode", editCode.getText().toString());
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                if (response == null) showToast("未知错误发生了");
                else {
                    int res_int = Integer.parseInt(response);
                    if (res_int > 0) showSuccessOrFail(true, "请求成功");
                    else switch (res_int) {
                        case -1:
                            showSuccessOrFail(false, "提现失败");
//                        showToast("提现失败");
                            break;
                        case -2:
                            showSuccessOrFail(false, "服务器出错");
//                        showToast("服务器出错");
                            break;
                        case -3:
                            showSuccessOrFail(false, "未登录");
//                        showToast("未登录");
                            break;
                        case -4:
                            showSuccessOrFail(false, "填写信息有空值");
//                        showToast("填写信息有空值");
                            break;
                        case -5:
                            showSuccessOrFail(false, "没有此用户");
//                        showToast("没有此用户");
                            break;
                        case -6:
                            showSuccessOrFail(false, "余额不足");
//                        showToast("余额不足");
                            break;
                        case -7:
                            showSuccessOrFail(false, "每天最多提款一次");
                        default:
                            break;
                    }
                }
            }
        });
    }

    private void turnSendButton(boolean isOk) {
        sendButton.setEnabled(isOk);
        if (isOk) {
            sendButton.setTextColor(0xFF000000);
            sendButton.setBackgroundColor(0xFFFFD600);
        } else {
            sendButton.setTextColor(0xFFFFFFFF);
            sendButton.setBackgroundColor(0xFF949494);
        }
    }

    private void getCode() {
        if (UserStateTool.isLoginNow(this)) {
            waitOneMinute();
            UserExistTool.sendSMS(InfoTool.getUserName(this));
            showToast("验证码已发送");
        } else showToast("系统错误");
    }

    //保留2位小数
    private String get2Double(double a) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(a);
    }

    private void waitOneMinute() {
        getCode.setEnabled(false);
        startNewThread();
    }

    @Override
    public void receiveMessage(String what) {
        if (what == null) {// 时钟更新
            if (wait_time > 0) {
                getCode.setText("" + wait_time + "秒");
            } else {
                wait_time = CDDE_WAIT_TIME;
                getCode.setEnabled(true);
                getCode.setText("重新获取");
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
                if (isOK) {
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else sv_dialog.dismiss();
            }
        });
        ((TextView) dialog_view.findViewById(R.id.sell_order_service_text)).setText(text);
    }

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        return true;
    }
}
