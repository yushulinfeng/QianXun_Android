package com.szdd.qianxun.request;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;

import org.json.JSONObject;

public class Task_doing_Ta extends Activity implements OnClickListener {
    private TextView request_contents, user_name, request_start, request_end, request_reward;
    private ImageView user_gender, call_user, img_back;
    private Button img_communicate, img_finished;
    private ImageView headIcon;
    private String post_userId, post_userName, icon_url;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordering);
        init();
        getData(Task_doing_Ta.this, ServerURL.REQUEST_DETAIL_URL);
        getHeadIcon(headIcon, icon_url);
    }

    public void getData(final Context context, final String url) {
        StaticMethod.POST(context, url, new ConnectListener() {
            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                dialog.config(context, "发送中...", "玩命加载中...", false);
                return dialog;
            }

            @Override
            public ConnectList setParam(ConnectList list) {
                JSONObject jsonobject = new JSONObject();
                try {
                    jsonobject.put("requestId", bundle.getString("userRequestId"));
                } catch (org.json.JSONException e1) {
                    return null;
                }
                list.put("jsonObj", jsonobject.toString(), false);
                return list;
            }

            @Override
            public void onResponse(String response) {
                if (response != null) Analysize(response);
            }
        });
    }

    public void Analysize(String str) {
        if (str == null || str.equals("")) {
            return;
        }
        JSONObject json = null;
        JSONObject jsonObject = null;
        try {
            json = new JSONObject(str);
            jsonObject = json.getJSONObject("userRuquest");
        } catch (Exception e) {
        }
        try {
            request_start.setText(jsonObject.getString("startAddress"));
            request_end.setText(jsonObject.getString("endAddress"));
            String[] url = new String[3];
            url[0] = ServerURL.getIP() + jsonObject.getString("request_picture");
            url[1] = ServerURL.getIP() + jsonObject.getString("request_picture2");
            url[2] = ServerURL.getIP() + jsonObject.getString("request_picture3");
            String raward1 = jsonObject.getString("reward_money");
            String raward2 = jsonObject.getString("reward_thing");
            request_reward.setText("￥:" + raward1 + "￥:" + raward2);
            String strrt = jsonObject.getString("request_content");
            request_contents.setText(strrt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject ob = jsonObject.getJSONObject("user");
            post_userName = ob.getString("username");
            post_userId = ob.getString("id");
            user_name.setText(ob.getString("nickName"));
            icon_url = ServerURL.getIP() + ob.getString("headIcon");
            String gender = ob.getString("gender");
            if (gender.equals("男")) {
                user_gender.setBackgroundResource(R.drawable.boy);
            } else {
                user_gender.setBackgroundResource(R.drawable.girl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void EnsureCompleted(final Context context, final String url) {
        StaticMethod.POST(context, url, new ConnectListener() {

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                dialog.config(context, "发送中...", "正在操作...", false);
                return dialog;
            }

            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("username", InfoTool.getUserName(context));
                list.put("requestId", bundle.getString("userRequestId"));
                return list;
            }

            @Override
            public void onResponse(String response) {
                if (response == null || response.equals(""))
                    showToast("操作失败");
                else {
                    int c = Integer.parseInt(response);
                    switch (c) {
                        case -3:
                            showToast("您未接此单");
                            break;
                        case 1:
                            showToast("操作成功");
                            finish();
                            break;
                        case -1:
                            showToast("操作失败");
                            break;
                        case -2:
                            showToast("服务器出错");
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    public void init() {
        bundle = getIntent().getExtras();
        headIcon = (ImageView) this.findViewById(R.id.Ta_request_user_head);
        img_back = (ImageView) this.findViewById(R.id.ordering_doing);
        request_reward = (TextView) this
                .findViewById(R.id.ordering_reward_content);
        user_name = (TextView) this.findViewById(R.id.Ta_request_user_name);
        request_start = (TextView) this
                .findViewById(R.id.ordering_startaddress_content);
        request_end = (TextView) this
                .findViewById(R.id.ordering_endaddress_content);
        request_contents = (TextView) this
                .findViewById(R.id.ordering_request_content);
        user_gender = (ImageView) this.findViewById(R.id.Ta_request_user_sex);
        call_user = (ImageView) this.findViewById(R.id.Ta_request_call_user);
        img_communicate = (Button) this.findViewById(R.id.ordering_chathwith);
        img_finished = (Button) this.findViewById(R.id.ordering_task_ok);
        call_user.setOnClickListener(this);
        img_communicate.setOnClickListener(this);
        img_finished.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Ta_request_call_user:// 打电话
                StaticMethod.callTo(this,post_userName);// 用户名就是电话号码
//                try {
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:" + post_userName));
//                    startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
            case R.id.ordering_chathwith:// 联系
                MsgPublicTool.chartTo(Task_doing_Ta.this, post_userId, Long.parseLong(post_userName));
                break;
            case R.id.ordering_task_ok:// 完成
                EnsureCompleted(Task_doing_Ta.this, ServerURL.CONFIRM_ISFINISHED_1);
                break;
            case R.id.ordering_doing:// 返回
                finish();
                break;
            default:
                break;
        }
    }

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }

    public void getHeadIcon(final ImageView img, String url) {
        StaticMethod.BITMAPHEAD(Task_doing_Ta.this, url, img);
    }
}
