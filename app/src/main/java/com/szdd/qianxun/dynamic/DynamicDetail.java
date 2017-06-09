package com.szdd.qianxun.dynamic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.views.RoundView;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.map.LocationTransListener;
import com.szdd.qianxun.tools.views.QianxunToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by linorz on 2016/3/31.
 */
public class DynamicDetail extends Activity implements View.OnClickListener {
    private Context context;
    private String imgip = ServerURL.getIP();//图片ip前缀
    private RoundView head;
    private TextView username, time, text, service, supportNum, trampleNum;
    private ImageView sex, image1, image2, image3, supportLine, trampleLine, supportBtn, trampleBtn;
    private String getDetail = ServerURL.GET_DYNAMIC_DETAIL;
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor editor;
    private long dynamicId = 0;
    private long userId = 0;
    private String imageurl1, imageurl2, imageurl3;
    private String serviceId = "";

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_dynamic_detail);
        context = this;
        Intent intent = getIntent();
        dynamicId = intent.getLongExtra("dynamicId", 0);
        //缓存
        mySharedPreferences = getSharedPreferences("dynamicdetail", Context.MODE_PRIVATE);
        editor = mySharedPreferences.edit();
        //初始化所有控件
        initAllView();
        getDetail();
    }

    private void initAllView() {
        findViewById(R.id.dynamic_detail_back).setOnClickListener(this);
        supportBtn = (ImageView) findViewById(R.id.dynamic_detail_supportbtn);
        supportBtn.setOnClickListener(this);
        trampleBtn = (ImageView) findViewById(R.id.dynamic_detail_tramplebtn);
        trampleBtn.setOnClickListener(this);
        head = (RoundView) findViewById(R.id.dynamic_user_head);//用户头像
        head.setOnClickListener(this);
        username = (TextView) findViewById(R.id.dynamic_user_name);//用户名
        sex = (ImageView) findViewById(R.id.dynamic_user_sex);//用户性别
        time = (TextView) findViewById(R.id.dynamic_time);//时间
        service = (TextView) findViewById(R.id.dynamic_detail_service);//添加的服务
        supportNum = (TextView) findViewById(R.id.dynamic_detail_supporttext);//支持人数
        trampleNum = (TextView) findViewById(R.id.dynamic_detail_trampletext);//踩人数
        text = (TextView) findViewById(R.id.dynamic_detail_text);//文字内容
        image1 = (ImageView) findViewById(R.id.dynamic_iv_1);//图片1
        image2 = (ImageView) findViewById(R.id.dynamic_iv_2);//图片2
        image3 = (ImageView) findViewById(R.id.dynamic_iv_3);//图片3
        supportLine = (ImageView) findViewById(R.id.dynamic_detail_supportline);//支持百分比线
        trampleLine = (ImageView) findViewById(R.id.dynamic_detail_trampleline);//踩百分比线
    }

    public void getDetail() {
        StaticMethod.POST(this, getDetail, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("dynamicId", dynamicId);
                return list;
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //位置
                    double location_x = jsonObject.getDouble("location_x");
                    double location_y = jsonObject.getDouble("location_y");
                    /**获取地理位置*/
                    StaticMethod.LocationTrans(location_x, location_y, new LocationTransListener() {
                        @Override
                        public void locationRespose(String locationName) {
                            if (locationName != null) {
                                TextView locationTest = (TextView) findViewById(R.id.dynamic_detail_location);
                                LinearLayout locationLayout = (LinearLayout) findViewById(R.id.dynamic_detail_layout2);
                                if (!locationName.equals("")) {
                                    locationLayout.setVisibility(View.VISIBLE);
                                    if (locationName.startsWith("中国"))
                                        locationTest.setText(locationName.substring(2));
                                    else locationTest.setText(locationName);
                                }
                            }
                        }
                    });
                    //文字内容
                    String content = jsonObject.getString("content");
                    if (!content.equals("")) {
                        findViewById(R.id.dynamic_detail_layout1).setVisibility(View.VISIBLE);
                        text.setText(jsonObject.getString("content"));
                    }
                    //三张图片
                    imageurl1 = jsonObject.getString("pic1");
                    imageurl2 = jsonObject.getString("pic2");
                    imageurl3 = jsonObject.getString("pic3");
                    if (!imageurl1.equals("")) {
                        image1.setVisibility(View.VISIBLE);
                        image1.setOnClickListener(DynamicDetail.this);
                        imageurl1 = imgip + imageurl1;
                        StaticMethod.BITMAP(context, imageurl1, image1);
                    }
                    if (!imageurl2.equals("")) {
                        image2.setVisibility(View.VISIBLE);
                        image2.setOnClickListener(DynamicDetail.this);
                        imageurl2 = imgip + imageurl2;
                        StaticMethod.BITMAP(context, imageurl2, image2);
                    }
                    if (!imageurl3.equals("")) {
                        image3.setVisibility(View.VISIBLE);
                        image3.setOnClickListener(DynamicDetail.this);
                        imageurl3 = imgip + imageurl3;
                        StaticMethod.BITMAP(context, imageurl3, image3);
                    }
                    //用户信息
                    JSONObject json_user = jsonObject.getJSONObject("user");
                    String userIconUrl = json_user.getString("headIcon");
                    StaticMethod.BITMAPHEAD(context, imgip + userIconUrl, head);
                    String gender = json_user.getString("gender");
                    if (gender.equals("女")) sex.setImageResource(R.drawable.girl);
                    else sex.setImageResource(R.drawable.boy);
                    String nickName = json_user.getString("nickName");
                    if (nickName.equals("")) nickName = "未取名";
                    username.setText(nickName);
                    userId = json_user.getLong("id");
                    //服务
                    String service_str = jsonObject.getString("businessService") + "";
                    if (!service_str.equals("") && !service_str.equals("null")) {
                        JSONObject json_service = jsonObject.getJSONObject("businessService");
                        LinearLayout serviceLayout = (LinearLayout) findViewById(R.id.dynamic_detail_layout);//服务外层布局
                        serviceLayout.setOnClickListener(DynamicDetail.this);
                        serviceLayout.setVisibility(View.VISIBLE);
                        service.setText(json_service.getString("name"));
                        serviceId = json_service.getString("id");
                    }
                    //时间
                    String publish_time = jsonObject.getString("publishTime");
                    if (publish_time.equals("")) time.setText("未知时间");
                    else {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                        long publish_time_long = Long.parseLong(publish_time);
                        publish_time = sdf.format(new Date(publish_time_long));
                        time.setText(publish_time);
                    }
                    //支持与踩
                    supportNum.setText(jsonObject.getString("support"));
                    trampleNum.setText(jsonObject.getString("trample"));
                    fixLineLenth();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dynamic_detail_back:
                finish();
                break;
            case R.id.dynamic_user_head:
                MsgPublicTool.showHomePage(context, userId);
                break;
            case R.id.dynamic_iv_1:
                MsgPublicTool.zoomImage(context, imageurl1);
                break;
            case R.id.dynamic_iv_2:
                MsgPublicTool.zoomImage(context, imageurl2);
                break;
            case R.id.dynamic_iv_3:
                MsgPublicTool.zoomImage(context, imageurl3);
                break;
            case R.id.dynamic_detail_layout:
                MsgPublicTool.showServiceDetail(context, serviceId);
                break;
            case R.id.dynamic_detail_supportbtn:
                supportBtn.setEnabled(false);
                trampleBtn.setEnabled(false);
                support();
                break;
            case R.id.dynamic_detail_tramplebtn:
                supportBtn.setEnabled(false);
                trampleBtn.setEnabled(false);
                trample();
                break;
        }
    }

    private void fixLineLenth() {
        float s_num = Float.parseFloat(supportNum.getText().toString());
        float t_num = Float.parseFloat(trampleNum.getText().toString());
        float sum = s_num + t_num;
        LinearLayout.LayoutParams supportparam = (LinearLayout.LayoutParams) supportLine.getLayoutParams();
        LinearLayout.LayoutParams trampleparam = (LinearLayout.LayoutParams) trampleLine.getLayoutParams();
        if (s_num != 0 && t_num != 0) {
            supportparam.weight = s_num / sum;
            trampleparam.weight = t_num / sum;
        }
        if (s_num == 0 && t_num != 0) {
            supportparam.weight = 0;
            trampleparam.weight = 1;
        }
        if (s_num != 0 && t_num == 0) {
            supportparam.weight = 1;
            trampleparam.weight = 0;
        }
        if (s_num == 0 && t_num == 0) {
            supportparam.weight = 0.5f;
            trampleparam.weight = 0.5f;
        }
    }

    private void support() {
        //初始化
        Animation a1 = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        a1.setDuration(100);
        a1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation a1 = new ScaleAnimation(1.5f, 1.0f, 1.5f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                a1.setDuration(500);
                supportBtn.startAnimation(a1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        supportBtn.startAnimation(a1);
        if (mySharedPreferences.getBoolean(dynamicId + "support", false))
            showToast("您已经顶过了");
        else if (mySharedPreferences.getBoolean(dynamicId + "trample", false))
            showToast("您已经踩过了");
        else SupportTrample(true);
    }

    private void trample() {
        //初始化
        Animation a2 = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        a2.setDuration(100);
        a2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation a2 = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                a2.setDuration(500);
                trampleBtn.startAnimation(a2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        trampleBtn.startAnimation(a2);
        if (mySharedPreferences.getBoolean(dynamicId + "support", false))
            showToast("您已经顶过了");
        else if (mySharedPreferences.getBoolean(dynamicId + "trample", false))
            showToast("您已经踩过了");
        else SupportTrample(false);
    }

    private void SupportTrample(final boolean f) {
        String url = "";
        if (f) url = ServerURL.DYNAMIC_SUPPORT;
        else url = ServerURL.DYNAMIC_TRAMPLE;
        StaticMethod.POST(this, url, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("dynamicId", dynamicId);
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                if (response.equals("1")) {
                    if (f) {
                        int s_num = Integer.parseInt(supportNum.getText().toString()) + 1;
                        supportNum.setText(String.valueOf(s_num));
                        editor.putBoolean(dynamicId + "support", true);
                        editor.commit();
                    } else {
                        int t_num = Integer.parseInt(trampleNum.getText().toString()) + 1;
                        trampleNum.setText(String.valueOf(t_num));
                        editor.putBoolean(dynamicId + "trample", true);
                        editor.commit();
                    }
                    fixLineLenth();
                } else {
                    supportBtn.setEnabled(true);
                    trampleBtn.setEnabled(true);
                    showToast("网络请求失败");
                }
            }
        });
    }

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }
}
