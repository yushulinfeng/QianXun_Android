package com.szdd.qianxun.sell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linorz on 2016/4/19.
 */
public class CashBalanceActivity extends Activity implements View.OnClickListener {
    private TextView cashBalance;
    private double res_dou = -1;
    private ListView list;
    private List<Map<String, Object>> items;
    private CashAdapter cashAdapter;
    private LayoutInflater inflater;
    private boolean showRecord = false;
    private boolean hasload = false;
    private ImageView cash_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_balance);
        inflater = LayoutInflater.from(this);
        findViewById(R.id.cash_layout2).setOnClickListener(this);
        findViewById(R.id.cash_back).setOnClickListener(this);
        cash_arrow = (ImageView) findViewById(R.id.cash_arrow);
        cashBalance = (TextView) findViewById(R.id.cash_balance_money);
        list = (ListView) findViewById(R.id.cash_balance_list);
        items = new ArrayList<>();
        cashAdapter = new CashAdapter();
        list.setAdapter(cashAdapter);
        getCash();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cash_layout1:
                //提现
                Intent intent = new Intent(this, CashBalanceDetial.class);
                if (res_dou < 0) return;
                intent.putExtra("cashbalance", res_dou);
                startActivityForResult(intent, 0);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.cash_layout2:
                changeArrow();
                if (hasload)
                    if (showRecord) {
                        list.setAdapter(null);
                        showRecord = false;
                    } else {
                        list.setAdapter(cashAdapter);
                        showRecord = true;
                    }
                else getCashRecord();
                break;
            case R.id.cash_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void getCashRecord() {
        StaticMethod.POST(this, ServerURL.GETCASHRECORD, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", InfoTool.getUserID(CashBalanceActivity.this.getBaseContext()));
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                if (response == null)
                    showToast("未知错误");
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            String name = jsonArray.getJSONObject(i).getString("account_name");
                            map.put("name", name);
                            String email = jsonArray.getJSONObject(i).getString("account_email");
                            map.put("email", email);
                            String fee = jsonArray.getJSONObject(i).getString("account_fee");
                            map.put("fee", fee);
                            String id = jsonArray.getJSONObject(i).getString("id");
                            map.put("id", id);

                            String status = "";
                            int status_int = jsonArray.getJSONObject(i).getInt("finalStatus");
                            if (status_int == 0) status = "正在进行中";
                            else if (status_int == 1) status = "提现成功";
                            else if (status_int == 2) status = "提现失败";
                            map.put("status", status);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String time = jsonArray.getJSONObject(i).getString("account_date");
                            Date date = new Date(time);
                            map.put("time", sdf.format(date));

                            items.add(map);
                        }
                        cashAdapter.notifyDataSetChanged();
                        hasload = true;
                        showRecord = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                getCash();
                break;
        }
    }

    private void getCash() {
        StaticMethod.POST(this, ServerURL.GETMYCASH, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", InfoTool.getUserID(CashBalanceActivity.this));
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
                    res_dou = Double.parseDouble(response);
                    if (res_dou >= 0) {
                        cashBalance.setText(get2Double(res_dou) + "元");
                        findViewById(R.id.cash_layout1).setOnClickListener(CashBalanceActivity.this);
                    } else switch ((int) res_dou) {
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

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }

    //保留2位小数
    private String get2Double(double a) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(a);
    }

    private class CashAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view;
            if (convertView != null) view = convertView;
            else view = inflater.inflate(R.layout.cash_balance_record, null);
            TextView name = (TextView) view.findViewById(R.id.cash_record_name);
            TextView email = (TextView) view.findViewById(R.id.cash_record_email);
            TextView fee = (TextView) view.findViewById(R.id.cash_record_price);
            TextView status = (TextView) view.findViewById(R.id.cash_record_status);
            TextView id = (TextView) view.findViewById(R.id.cash_record_id);
            TextView time = (TextView) view.findViewById(R.id.cash_record_time);

            name.setText((String) items.get(i).get("name"));
            email.setText((String) items.get(i).get("email"));
            fee.setText((String) items.get(i).get("fee"));
            status.setText((String) items.get(i).get("status"));
            id.setText((String) items.get(i).get("id"));
            time.setText((String) items.get(i).get("time"));

            return view;
        }
    }

    private void changeArrow() {
        Animation a = null;
        if (showRecord) a = new RotateAnimation(90f, 0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        else a = new RotateAnimation(0f, 90f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        a.setFillAfter(true);
        a.setDuration(200);
        cash_arrow.startAnimation(a);
    }
}
