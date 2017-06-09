package com.szdd.qianxun.bank;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.start.login.Login;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.bitmap.BitmapListener;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.CacheTool;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.RoundHeadView;

import org.json.JSONObject;

import java.util.Calendar;

public class MainTab_03_RequestDetailActivity extends Activity implements
        View.OnClickListener {

    private LinearLayout ll;
    private TextView top_text_detail, users_detail, users_count_detail,
            users_runk_detail, decribe_detail, reward_content_detail;
    private Button apply_detail, report_detail;
    private RoundHeadView otheruserheadview_detail;
    private ImageView sex_detail, img_back;
    private ImageView decribe_icon_detail[] = new ImageView[3];
    private BorderTextView importent_decribe_detail;
    private int requestId;
    private static String fatherA = "";
    private int status;
    private String reward_money, reward_thing;
    private String url = ServerURL.REQUEST_DETAIL_URL;
    // 解析数据
    private JSONObject jsonObj = null;
    private static final String IP = ServerURL.getIP();
    private long postUserName;
    private String postUserId = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listitem_in_tab_03_detail);
        Intent intent = getIntent();
        // 获取数据
        fatherA = intent.getStringExtra("fatherA");

        top_text_detail = (TextView) findViewById(R.id.top_text_detail);
        users_detail = (TextView) findViewById(R.id.users_detail);
        users_count_detail = (TextView) findViewById(R.id.users_count_detail);
        users_runk_detail = (TextView) findViewById(R.id.users_runk_detail);
        decribe_detail = (TextView) findViewById(R.id.decribe_detail);
        reward_content_detail = (TextView) findViewById(R.id.reward_content_detail);
        report_detail = (Button) findViewById(R.id.report_detail);
        apply_detail = (Button) findViewById(R.id.apply_detail);
        if (fatherA.equals("three")) {
            requestId = intent.getIntExtra("requestId", 0);
            report_detail.setOnClickListener(this);
            apply_detail.setOnClickListener(this);
        } else if (fatherA.equals("two")) {
            requestId = intent.getIntExtra("requestId", 0);
            report_detail.setVisibility(Button.GONE);
            apply_detail.setVisibility(Button.GONE);
        } else if (fatherA.equals("four")) {
            requestId = intent.getIntExtra("requestId", 0);
            report_detail.setOnClickListener(this);
            apply_detail.setOnClickListener(this);
        } else if (fatherA.equals("five")) {
            requestId = intent.getIntExtra("requestId", 0);
            report_detail.setOnClickListener(this);
            apply_detail.setOnClickListener(this);
        }
        otheruserheadview_detail = (RoundHeadView) findViewById(R.id.otheruserheadview_detail);
        sex_detail = (ImageView) findViewById(R.id.sex_detail);
        decribe_icon_detail[0] = (ImageView) findViewById(R.id.decribe_icon_detail01);
        decribe_icon_detail[1] = (ImageView) findViewById(R.id.decribe_icon_detail02);
        decribe_icon_detail[2] = (ImageView) findViewById(R.id.decribe_icon_detail03);
        img_back = (ImageView) this.findViewById(R.id.tab_03_back);
        img_back.setOnClickListener(this);
        decribe_icon_detail[0].setOnClickListener(this);
        decribe_icon_detail[1].setOnClickListener(this);
        decribe_icon_detail[2].setOnClickListener(this);

        ll = (LinearLayout) findViewById(R.id.ll);
        ll.setVisibility(LinearLayout.INVISIBLE);
        updateDataFromNet();

    }

    private void updateDataFromNet() {
        String cache = CacheTool.getCache(url + requestId);
        if (cache != null)
            dealResponse(cache);
        else
            StaticMethod.POST(MainTab_03_RequestDetailActivity.this, url,
                    new ConnectListener() {
                        @Override
                        public ConnectDialog showDialog(ConnectDialog dialog) {
                            dialog.config(MainTab_03_RequestDetailActivity.this, "正在连接", "请稍后……", true);
                            return dialog;
                        }

                        @Override
                        public ConnectList setParam(ConnectList list) {
                            JSONObject jsonobject = new JSONObject();
                            try {
                                jsonobject.put("requestId", requestId + "");
                            } catch (org.json.JSONException e1) {
                                return null;
                            }
                            list.put("jsonObj", jsonobject.toString(), false);
                            return list;
                        }

                        @Override
                        public void onResponse(String response) {
                            if (response == null || response.equals("")) {
                                // 网络错误
                                showToast("网络错误,请连接网络后重新加载");
                                finish();
                            } else {
                                dealResponse(response);
                                CacheTool.saveCache(url + requestId, response);
                            }
                        }
                    });
    }

    private void dealResponse(String response) {
        if (response.equals("failed")) {
            showToast("获取详情失败!");
            finish();
        } else {
            try {
                Init(response);
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static JSONObject Analysis(String jsonStr) throws org.json.JSONException {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void Init(String response) throws org.json.JSONException {

        jsonObj = Analysis(response);
        ll.setVisibility(LinearLayout.VISIBLE);
        // jsonObject后台得到的数据
        status = jsonObj.getJSONObject("userRuquest").getInt("status");
        if (status == 0) {
            apply_detail.setClickable(true);
        } else if (status == 1) {
            apply_detail.setClickable(false);
            apply_detail.setTextSize(18);
            apply_detail.setText("已接单");

        }
        getBitmapHead(IP + (String) jsonObj.getJSONObject("userRuquest").getJSONObject("user").getString("headIcon"), otheruserheadview_detail);
        otheruserheadview_detail.setOnClickListener(this);
        top_text_detail.setText(jsonObj.getJSONObject("userRuquest").getJSONObject("user").getString("nickName") + "的需求");
        users_detail.setText("昵称：" + jsonObj.getJSONObject("userRuquest").getJSONObject("user").getString("nickName"));
        postUserName = jsonObj.getJSONObject("userRuquest").getJSONObject("user").getLong("username");
        postUserId = jsonObj.getJSONObject("userRuquest").getJSONObject("user").getString("id");
        if (("" + jsonObj.getJSONObject("userRuquest").getJSONObject("user").getLong("username")).length() == 11) {
            users_count_detail.setText("账号：" + ("" + jsonObj.getJSONObject("userRuquest").getJSONObject("user").getLong("username")).substring(0, 3) + "****" + ("" + jsonObj.getJSONObject("userRuquest").getJSONObject("user").getLong("username")).substring(7, 11));
        } else {
            users_count_detail.setText("账号：无");
        }
        users_runk_detail.setText("信誉等级： " + jsonObj.getJSONObject("userRuquest").getJSONObject("user").getInt("rank"));
        if (jsonObj.getJSONObject("userRuquest").getJSONObject("user").getString("gender").equals("男")) {
            sex_detail.setImageResource(R.drawable.boy);
        } else if (jsonObj.getJSONObject("userRuquest").getJSONObject("user").getString("gender").equals("女")) {
            sex_detail.setImageResource(R.drawable.girl);
        } else {
            sex_detail.setVisibility(ImageView.INVISIBLE);
        }
        decribe_detail.setText(jsonObj.getJSONObject("userRuquest").getString("request_content"));
        reward_money = jsonObj.getJSONObject("userRuquest").getString("reward_money");
        reward_thing = jsonObj.getJSONObject("userRuquest").getString("reward_thing");
        if ((reward_money == null || reward_money.equals("")) && (reward_thing == null || reward_thing.equals(""))) {
            reward_content_detail.setText("^-^");
        } else if ((reward_thing == null || reward_thing.equals("")) && (reward_money != null && !reward_money.equals(""))) {
            reward_content_detail.setText("￥" + reward_money);
        } else if ((reward_thing != null && !reward_thing.equals("")) && (reward_money == null || reward_money.equals(""))) {
            reward_content_detail.setText("￥" + reward_thing);
        } else if ((reward_thing != null && !reward_thing.equals("")) && (reward_money != null && !reward_money.equals(""))) {
            reward_content_detail.setText("￥" + reward_money + " & " + reward_thing);
        }

        Calendar c = Calendar.getInstance();
        int data = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String str = jsonObj.getJSONObject("userRuquest").getString("request_limitTime");
//        if (str.equals("") || (str == null)) {
////            time_detail.setText(" " + 0 + "h" + " " + 0 + " " + "min");
//        } else {
//            try {
//                String[] ss = str.split("-|:");
//                int leftd = Integer.parseInt(ss[2]) - data;
//                int lefth = Integer.parseInt(ss[3]) - hour;
//                int leftm = Integer.parseInt(ss[4]) - minute;
//                lefth += leftd * 24;
//                if (leftm < 0) {
//                    leftm += 60;
//                    lefth--;
//                }
//                time_detail.setText(" " + lefth + " h" + " " + leftm + " min");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        String request_picture[] = new String[3];
        request_picture[0] = jsonObj.getJSONObject("userRuquest").getString("request_picture");
        request_picture[1] = jsonObj.getJSONObject("userRuquest").getString("request_picture2");
        request_picture[2] = jsonObj.getJSONObject("userRuquest").getString("request_picture3");
        for (int i = 0; i < 3 && request_picture[i] != null; i++) {
            if (request_picture[i].equals("")) {
                break;
            }
            decribe_icon_detail[i].setVisibility(View.VISIBLE);
            StaticMethod.BITMAP(MainTab_03_RequestDetailActivity.this, ServerURL.getIP() + request_picture[i], decribe_icon_detail[i]);
        }
    }

    @Override
    public void onClick(View v) {
        int Rid = v.getId();
        switch (Rid) {
            case R.id.apply_detail:

                if (UserStateTool.isLoginNow(MainTab_03_RequestDetailActivity.this)) {
                    Intent intent = new Intent(MainTab_03_RequestDetailActivity.this,
                            MainTab_03_ApplySuccessActivity.class);
                    try {
                        intent.putExtra("id", requestId);
                        intent.putExtra("username", jsonObj.getJSONObject("userRuquest").getJSONObject("user")
                                .getLong("username"));
                        intent.putExtra("nickname", jsonObj.getJSONObject("userRuquest")
                                .getJSONObject("user").getString("nickName"));
                        intent.putExtra("postUserId",postUserId);
                        intent.putExtra("postUserName",postUserName);
                    } catch (org.json.JSONException e2) {
                        e2.printStackTrace();
                    }
                    startActivity(intent);
                } else {
                    showToast("欢迎登录");
                    Intent intent = new Intent(MainTab_03_RequestDetailActivity.this, Login.class);
                    startActivity(intent);
                }

                break;
            case R.id.report_detail:
//                StaticMethod.POST(MainTab_03_RequestDetailActivity.this, ServerURL.REPORT_URL,
//                        new ConnectListener() {
//                            @Override
//                            public ConnectDialog showDialog(ConnectDialog dialog) {
//                                return dialog;
//                            }
//
//                            @Override
//                            public ConnectList setParam(ConnectList list) {
//                                JSONObject jsonobject = new JSONObject();
//                                list.put("requestId", requestId + "", false);
//                                return list;
//                            }
//
//                            @Override
//                            public void onResponse(String response) {
//                                if (response == null || response.equals("")) {
//                                    // 网络错误
//                                    showToast("出现错误！");
//                                    return;
//                                } else {//处理举报后结果response
//
//                                    int isapply = Integer.parseInt(response);
//
//                                    //报名失败
//                                    if (isapply == -2) {//后台出现问题
//                                        showToast("错误！");
//                                    } else if (isapply == -1) {//失败
//                                        showToast("举报失败！");
//                                    } else if (isapply == 1) {//举报成功
//                                        report_detail.setClickable(false);
//                                        showToast("举报成功！");
//                                    }
//
//                                }
//                            }
//                        });
                MsgPublicTool.chartTo(MainTab_03_RequestDetailActivity.this, postUserId, postUserName);
                break;
            case R.id.otheruserheadview_detail:

                try {
                    MsgPublicTool.showHomePage(MainTab_03_RequestDetailActivity.this, (long) jsonObj.getJSONObject("userRuquest").getJSONObject("user")
                            .getInt("id"));
                } catch (org.json.JSONException e1) {
                    e1.printStackTrace();
                }
                break;
            case R.id.decribe_icon_detail01:
                Intent intent01 = new Intent(MainTab_03_RequestDetailActivity.this,
                        BigImageActivity.class);
                try {
                    intent01.putExtra("images", IP + (String) jsonObj.getJSONObject("userRuquest").getString("request_picture"));
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }// 非必须
                int[] location = new int[2];
                decribe_icon_detail[0].getLocationOnScreen(location);
                intent01.putExtra("locationX", location[0]);// 必须
                intent01.putExtra("locationY", location[1]);// 必须
                intent01.putExtra("width", decribe_icon_detail[0].getWidth());// 必须
                intent01.putExtra("height", decribe_icon_detail[0].getHeight());// 必须
                startActivity(intent01);
                overridePendingTransition(0, 0);
                break;
            case R.id.decribe_icon_detail02:
                Intent intent02 = new Intent(MainTab_03_RequestDetailActivity.this,
                        BigImageActivity.class);
                try {
                    intent02.putExtra("images", IP + (String) jsonObj.getJSONObject("userRuquest").getString("request_picture2"));
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                int[] location02 = new int[2];
                decribe_icon_detail[1].getLocationOnScreen(location02);
                intent02.putExtra("locationX", location02[0]);// 必须
                intent02.putExtra("locationY", location02[1]);// 必须
                intent02.putExtra("width", decribe_icon_detail[1].getWidth());// 必须
                intent02.putExtra("height", decribe_icon_detail[1].getHeight());// 必须
                startActivity(intent02);
                break;
            case R.id.decribe_icon_detail03:
                Intent intent03 = new Intent(MainTab_03_RequestDetailActivity.this,
                        BigImageActivity.class);
                try {
                    intent03.putExtra("images", IP + (String) jsonObj.getJSONObject("userRuquest").getString("request_picture3"));
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                int[] location03 = new int[2];
                decribe_icon_detail[2].getLocationOnScreen(location03);
                intent03.putExtra("locationX", location03[0]);// 必须
                intent03.putExtra("locationY", location03[1]);// 必须
                intent03.putExtra("width", decribe_icon_detail[2].getWidth());// 必须
                intent03.putExtra("height", decribe_icon_detail[2].getHeight());// 必须
                startActivity(intent03);
                break;
            case R.id.tab_03_back:
                finish();
                break;
            default:
                break;
        }

    }

    private void getBitmapHead(final String url, final ImageView imageview) {
        StaticMethod.BITMAPHEAD(MainTab_03_RequestDetailActivity.this, url, imageview);
    }

    private void getBitmap(final String url, final ImageView imageview) {
        StaticMethod.BITMAP(MainTab_03_RequestDetailActivity.this, url, new BitmapListener() {
            @Override
            public void onResponse(Bitmap bitmap) {
                if (bitmap != null) {
                    try {
                        imageview.setImageBitmap(bitmap);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }
}
