package com.szdd.qianxun.sell.orders.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.MoreImagesActivity;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.sell.discuss.DiscussActivity;
import com.szdd.qianxun.sell.discuss.DiscussTool;
import com.szdd.qianxun.service.OrderServiceActivity;
import com.szdd.qianxun.service.SercuityDialog;
import com.szdd.qianxun.start.login.Login;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.CacheTool;
import com.szdd.qianxun.tools.map.LocationListener;
import com.szdd.qianxun.tools.views.QianxunToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//删除此行，和430行的测试即可
public class ServiceDetailActivity extends Activity implements View.OnClickListener {
    //定义控件
    private Context context;
    private ImageView sexImg;
    private ImageView servicetypeicon, todaycanorderimg;
    private TextView nickname, nametestify, studenttestify, serviceTitle, price, ichangeprice, changeprice, describe,
            distance, canorder, servicetype, school, alreadyordernum;
    private ImageView headview, collect;
    private List<ImageView> desIV;
    private List<ImageView> ranIV;
    private List<ImageView> collectheads;
    private static final String IP = ServerURL.getIP();
    // 解析数据
    private JSONObject jsonObj = null;
    private JSONObject jsonObj_collector = null;
    private JSONArray jsonArrayCollector = null;
    private boolean alreadycollect = false;
    //由服务列表传过来的数据
    private String serviceId;
    private int userId;
    private int collecternumber;//收藏者的数目
    private double latitude;// 纬度
    private double longitude;// 经度
    //评论
    private LinearLayout layout_discuss;
    private TextView tv_discuss_num;
    private String discuss_author, discuss_id, discuss_title, discuss_text;
    private int discuss_num;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_service_detail);
        context = this;
        Intent intent = getIntent();
        serviceId = intent.getStringExtra("serviceId");
        if (serviceId == null) finish();
        findViewById(R.id.service_detail_iv_back).setOnClickListener(this);
        findViewById(R.id.service_detail_iv_message).setOnClickListener(this);
        sexImg = (ImageView) findViewById(R.id.service_detail_iv_sex);
        ranIV = new ArrayList<>();
        ranIV.add((ImageView) findViewById(R.id.service_detail_iv_rank01));
        ranIV.add((ImageView) findViewById(R.id.service_detail_iv_rank02));
        ranIV.add((ImageView) findViewById(R.id.service_detail_iv_rank03));
        ranIV.add((ImageView) findViewById(R.id.service_detail_iv_rank04));
        ranIV.add((ImageView) findViewById(R.id.service_detail_iv_rank05));
        desIV = new ArrayList<>();
        desIV.add((ImageView) findViewById(R.id.service_detail_im_describe01));
        desIV.add((ImageView) findViewById(R.id.service_detail_im_describe02));
        desIV.add((ImageView) findViewById(R.id.service_detail_im_describe03));
        desIV.add((ImageView) findViewById(R.id.service_detail_im_describe04));
        desIV.add((ImageView) findViewById(R.id.service_detail_im_describe05));
        desIV.add((ImageView) findViewById(R.id.service_detail_im_describe06));
        desIV.add((ImageView) findViewById(R.id.service_detail_im_describe07));
        desIV.add((ImageView) findViewById(R.id.service_detail_im_describe08));
        desIV.add((ImageView) findViewById(R.id.service_detail_im_describe09));
        servicetypeicon = (ImageView) findViewById(R.id.service_detail_tv_servicetypeicon);
        todaycanorderimg = (ImageView) findViewById(R.id.service_detail_tv_canorderimg);
        nickname = (TextView) findViewById(R.id.service_detail_tv_nickname);
        nametestify = (TextView) findViewById(R.id.service_detail_tv_nametestify);
        studenttestify = (TextView) findViewById(R.id.service_detail_tv_studenttestify);
        serviceTitle = (TextView) findViewById(R.id.service_detail_tv_service);
        price = (TextView) findViewById(R.id.service_detail_tv_price);
        ichangeprice = (TextView) findViewById(R.id.service_detail_tv_Ichangeprice);
        changeprice = (TextView) findViewById(R.id.service_detail_tv_changeprice);
        school = (TextView) findViewById(R.id.service_detail_tv_school);
        alreadyordernum = (TextView) findViewById(R.id.service_detail_tv_alreadyordernum);

        layout_discuss = (LinearLayout) findViewById(R.id.service_detail_discuss_layout);
        tv_discuss_num = (TextView) findViewById(R.id.service_detail_discuss_text_num);
        layout_discuss.setOnClickListener(this);

        describe = (TextView) findViewById(R.id.service_detail_tv_describe);
        distance = (TextView) findViewById(R.id.service_detail_tv_distance);
        canorder = (TextView) findViewById(R.id.service_detail_tv_canorder);
        servicetype = (TextView) findViewById(R.id.service_detail_tv_servicetype);
        headview = (ImageView) findViewById(R.id.service_detail_rhv_headview);
        collect = (ImageView) findViewById(R.id.service_detail_im_collectclick);
        collectheads = new ArrayList<>();
        collectheads.add((ImageView) findViewById(R.id.service_detail_rhv_collectheadview01));
        collectheads.add((ImageView) findViewById(R.id.service_detail_rhv_collectheadview02));
        collectheads.add((ImageView) findViewById(R.id.service_detail_rhv_collectheadview03));
        collectheads.add((ImageView) findViewById(R.id.service_detail_rhv_collectheadview04));
        headview.setOnClickListener(this);
        findViewById(R.id.service_detail_im_collect).setOnClickListener(this);
        findViewById(R.id.service_detail_im_chat).setOnClickListener(this);
        findViewById(R.id.service_detail_im_rightorder).setOnClickListener(this);
        findViewById(R.id.service_detail_tv_affordbusiness).setOnClickListener(this);
        updateDataFromNetForService();
        updateDataFromNetForServiceCollector();
    }

    //连接后台获取服务的详细信息
    private void updateDataFromNetForService() {
        String cache = CacheTool.getCache(ServerURL.SERVICE_DETAIL_URL + serviceId);
        if (cache != null)
            try {
                Init(cache);
            } catch (JSONException e) {
            }
        else
            StaticMethod.POST(context, ServerURL.SERVICE_DETAIL_URL, new ConnectListener() {
                @Override
                public ConnectDialog showDialog(ConnectDialog dialog) {
                    dialog.config(context, "正在连接", "请稍后……", true);
                    return dialog;
                }

                @Override
                public ConnectList setParam(ConnectList list) {
                    list.put("serviceId", serviceId, false);
                    return list;
                }

                @Override
                public void onResponse(String response) {
                    if (response == null || response.equals("")) {
                        // 网络错误
                        showToast("网络错误,请连接网络后重新加载");
                        finish();
                        return;
                    }
                    //处理后台发送来的数据
                    if (response.equals("failed")) {
                        showToast("获取失败！");
                        finish();
                    } else {
                        try {
                            Init(response);
                            CacheTool.saveCache(ServerURL.SERVICE_DETAIL_URL + serviceId, response);
                        } catch (JSONException e) {
                            Log.e("JSON-EEERROR", "ServiceDetail");
                        }
                    }
                }
            });
    }

    //连接后台获取服务收藏者的详细信息
    private void updateDataFromNetForServiceCollector() {
        StaticMethod.POST(context, ServerURL.SERVICE_DETAIL_COLLECTOR_URL, new ConnectListener() {
            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return dialog;
            }

            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("serviceId", serviceId, false);
                return list;
            }

            @Override
            public void onResponse(String response) {
                if (response == null || response.equals("")) {
                    finish();
                    return;
                }
                //处理后台发送来的数据
                if (response.equals("failed")) {
                    finish();
                    return;
                } else {
                    try {
                        InitCollector(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateDataFromNetForDiscuss() {
        if (discuss_id == null) {
            return;
        }
        DiscussTool.getInstance().getDiscuss(discuss_id, new DiscussTool.DiscussGetListener() {
            @Override
            public void OnResponse(String authorId, String title, String text, int replyCount) {
                if (title == null)
                    return;
                discuss_author = authorId;
                //discuss_title = title;//这两个不必了
                //discuss_text = text;
                discuss_num = replyCount;
                tv_discuss_num.setText("(" + discuss_num + ")");
            }
        });
    }

    //连接后台收藏服务
    private void postDataForServiceCollect(final String serviceId, final String userId) {
        StaticMethod.POST(context, ServerURL.SERVICE_DETAIL_ADDCOLLECTOR_URL, new ConnectListener() {
            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return dialog;
            }

            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", userId, false);
                list.put("serviceId", serviceId, false);
                return list;
            }


            @Override
            public void onResponse(String response) {
                if (response == null || response.equals("")) {
                    finish();
                    return;
                }
                //处理后台发送来的数据
                if (response.equals("failed")) {
                    finish();
                } else {
                    int collectstatus = Integer.parseInt(response);
                    if (collectstatus == 1) {
                        showToast("收藏成功");
                        alreadycollect = true;
                        collect.setImageResource(R.drawable.collect_star_click);
                    } else if (collectstatus == -2) showToast("服务器出错!");
                    else if (collectstatus == -3) showToast("用户未登录");
                    else if (collectstatus == -5) showToast("没有此服务");
                    else if (collectstatus == -4) {
                        showToast("已收藏");
                        alreadycollect = true;
                        collect.setImageResource(R.drawable.collect_star_click);
                    } else if (collectstatus == -1) {
                        showToast("收藏失败");
                        finish();
                    }
                }
            }
        });
    }

    private void Init(String response) throws org.json.JSONException {
        jsonObj = new JSONObject(response);
        //初始化头像
        JSONObject user_json = jsonObj.getJSONObject("user");
        getBitmapHead(IP + user_json.getString("headIcon"), headview);
        //初始化昵称
        nickname.setText(user_json.getString("nickName"));
        //初始化性别
        String sex = user_json.getString("gender");
        userId = user_json.getInt("id");
        if (sex.equals("男")) sexImg.setImageResource(R.drawable.boy);
        else if (sex.equals("女")) sexImg.setImageResource(R.drawable.girl);
        else sexImg.setVisibility(ImageView.INVISIBLE);
        //初始化信誉等级
        int rank = StaticMethod.changeLevel(user_json.getInt("rank"));
        for (int i = 0; i < rank; i++) ranIV.get(i).setVisibility(View.VISIBLE);

        //初始化认证方式
        int testify = user_json.getInt("verifyStatus");
        if (testify >= 1) {
            nametestify.setText("已实名认证");
            nametestify.setBackgroundResource(R.drawable.tv_nameprove);
        }
        if (testify == 2) {
            studenttestify.setText("已学生认证");
            studenttestify.setBackgroundResource(R.drawable.tv_studentprove);
        }

        //初始化技能
        String name = jsonObj.getString("name");
        if (!name.equals("")) {
            serviceTitle.setVisibility(View.VISIBLE);
            serviceTitle.setText("技能 " + name);
        }
        //初始化价格
        String unit = jsonObj.getString("reward_unit");
        String money = jsonObj.getString("reward_money");
        if (!money.equals("")) price.setVisibility(View.VISIBLE);
        if (unit.equals("")) price.setText(money + "元");
        else price.setText(money + "元" + "/" + unit);

        //初始化交换服务
        String exchange = jsonObj.getString("reward_thing");
        if (!exchange.equals("")) {
            ichangeprice.setVisibility(View.VISIBLE);
            changeprice.setVisibility(View.VISIBLE);
            changeprice.setText(exchange);
        }

        //学校以及约单数
        if (!user_json.getString("school").equals("")) {
            school.setText(" " + user_json.getString("school"));
        }
        if (!jsonObj.getString("finishedPeople").equals("")) {//////////
            alreadyordernum.setText(jsonObj.getString("finishedPeople"));
        }
        //初始化服务具体描述
        describe.setText(jsonObj.getString("detail"));


        JSONArray imagesArray = jsonObj.getJSONArray("images");
        final ArrayList<String> urls = new ArrayList<>();
        for (int i = 0; i < imagesArray.length(); i++) {
            desIV.get(i).setVisibility(ImageView.VISIBLE);
            final String imgUrl = IP + imagesArray.getJSONObject(i).get("link");
            urls.add(imgUrl);
            getBitmap(imgUrl, desIV.get(i));
            final int finalI = i;
            desIV.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    MsgPublicTool.zoomImage(context, imgUrl);
                    Intent intent = new Intent(context, MoreImagesActivity.class);
                    intent.putExtra("urls", urls);
                    intent.putExtra("num", finalI);
                    context.startActivity(intent);
                }
            });
        }
        final double location_x = jsonObj.getDouble("location_x");
        final double location_y = jsonObj.getDouble("location_y");
        com.szdd.qianxun.tools.map.Location.getLocation(context,
                new LocationListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void locationRespose(String locationName, double x, double y, float limit) {
                        try {
                            double dis = getDistance(y, x, location_y, location_x);
                            DecimalFormat df = new DecimalFormat("0.00");
                            String dis_text = df.format(dis);
                            distance.setText(dis_text + "公里");
                        } catch (Exception e) {
                            distance.setText("失败");
                        }
                    }
                });

        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);

        String canServiceDay = jsonObj.getString("canServiceDay");
        if (!canServiceDay.equals("") && canServiceDay != null) {
            String[] sweek = canServiceDay.split("#");
            if (sweek[week - 1].equals("0")) {
                todaycanorderimg.setImageResource(R.drawable.clock_fail);
                canorder.setText("今天不可约");
            } else {
                todaycanorderimg.setImageResource(R.drawable.clock_right);
                canorder.setText("今天可约呦");
            }
        } else todaycanorderimg.setImageResource(R.drawable.clock_fail);
        switch (jsonObj.getString("serviceType")) {
            case "1":
                servicetype.setText("线上");
                servicetypeicon.setImageResource(R.drawable.plane);
                break;
            case "2":
                servicetype.setText("指定");
                servicetypeicon.setImageResource(R.drawable.point);
                break;
            case "3":
                servicetype.setText("邮寄");
                servicetypeicon.setImageResource(R.drawable.post_mail);
                break;
            case "4":
                servicetype.setText("上门");
                servicetypeicon.setImageResource(R.drawable.face_to_face);
                break;
            default:
                break;
        }

        collecternumber = jsonObj.getInt("favoriteNumber");
        //缺少一个接口，获得某人是否已经收藏某服务
        if (alreadycollect) {
            collect.setImageResource(R.drawable.collect_star_click);
        }

        try {
            discuss_id = jsonObj.getString("discussId");
            discuss_id = TextUtils.isEmpty(discuss_id) ? null : discuss_id;
        } catch (Exception e) {
            discuss_id = null;
        }

//        ////////////////////////////////////////////////////////////////////
//        /////////////////前期测试专用
//        if (discuss_id == null) {
//            if ("331".equals(serviceId))
//                discuss_id = "5752c00055c400215c9e6318";
//            else
//                discuss_id = "temp_" + serviceId;
//        }
//        //discuss_id = "5752c00055c400215c9e6318";
//        Log.e("EEEEE-discussId", discuss_id);
//        ///////////////////////////////////////////////////////////////////

        if (discuss_id != null) {
            discuss_title = name;
            discuss_text = describe.getText().toString();
            layout_discuss.setVisibility(View.VISIBLE);
        }
        if (discuss_id != null && !discuss_id.startsWith("temp_"))
            updateDataFromNetForDiscuss();
    }


    private void InitCollector(String response) throws JSONException {
        jsonObj_collector = new JSONObject(response);
        jsonArrayCollector = jsonObj_collector.getJSONArray("list");
        if (jsonArrayCollector.length() != 0) {
            TextView collecterNum = (TextView) findViewById(R.id.service_detail_rhv_morecollecternumber);
            collecterNum.setText("(" + jsonArrayCollector.length() + ")");
            if (jsonArrayCollector.length() <= 4) {
                for (int i = 0; i < jsonArrayCollector.length(); i++) {
                    collectheads.get(i).setVisibility(View.VISIBLE);
                    collectheads.get(i).setOnClickListener(this);
                    try {
                        getBitmapHead(IP + jsonArrayCollector.getJSONObject(i).getString("headIcon"), collectheads.get(i));
                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    collectheads.get(i).setVisibility(View.VISIBLE);
                    collectheads.get(i).setOnClickListener(this);
                    try {
                        getBitmapHead(IP + jsonArrayCollector.getJSONObject(i).getString("headIcon"), collectheads.get(i));
                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            findViewById(R.id.service_detail_collecter_layout).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int Rid = view.getId();
        Intent intent;
        switch (Rid) {
            case R.id.service_detail_iv_back:
                finish();
                break;
            case R.id.service_detail_iv_message:
                showToast("服务详情！");
                break;
            case R.id.service_detail_rhv_headview:
                if (!UserStateTool.isLoginEver(context)) {
                    UserStateTool.goToLogin(context);
                    return;
                }
                try {
                    MsgPublicTool.showHomePage(context, jsonObj.getJSONObject("user").getLong("id"));
                } catch (org.json.JSONException e1) {
                    e1.printStackTrace();
                }
                break;
            case R.id.service_detail_tv_affordbusiness:
                SercuityDialog dialog = new SercuityDialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.show();
                break;
            case R.id.service_detail_im_collect:
                if (UserStateTool.isLoginNow(context))
                    if (InfoTool.getUserID(this).equals(userId + ""))
                        showToast("不能收藏自己的服务");
                    else
                        postDataForServiceCollect(serviceId, InfoTool.getUserID(context));
                else {
                    showToast("欢迎登录");
                    intent = new Intent(context, Login.class);
                    startActivity(intent);
                }

                break;
            case R.id.service_detail_im_chat:
                if (!UserStateTool.isLoginEver(context)) {
                    UserStateTool.goToLogin(context);
                    return;
                }
                try {
                    String send = "服务名称：\n技能 " + jsonObj.getString("name") + "\n服务详情：\n" + jsonObj.getString("detail");
                    MsgPublicTool.chartTo(context, jsonObj.getJSONObject("user").getInt("id"),
                            jsonObj.getJSONObject("user").getLong("username"),
                            send);
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.service_detail_im_rightorder:
                if (UserStateTool.isLoginNow(context)) {
                    if (Integer.parseInt(InfoTool.getUserID(context)) == userId)
                        showToast("不允许自己预约");
                    else {
                        intent = new Intent(context, OrderServiceActivity.class);
                        intent.putExtra("come", "order");
                        intent.putExtra("serviceId", serviceId);
                        startActivity(intent);
                    }

                } else {
                    showToast("欢迎登录");
                    intent = new Intent(context, Login.class);
                    startActivity(intent);
                }
                break;
            case R.id.service_detail_collecter_layout:
                intent = new Intent(context, ServiceDetailCollecterActivity.class);
                intent.putExtra("serviceId", serviceId);
                startActivity(intent);
                break;
            case R.id.service_detail_rhv_collectheadview01:
                try {
                    MsgPublicTool.showHomePage(context, jsonArrayCollector.getJSONObject(0).getLong("id"));
                } catch (org.json.JSONException e1) {
                    e1.printStackTrace();
                }
                break;
            case R.id.service_detail_rhv_collectheadview02:
                try {
                    MsgPublicTool.showHomePage(context, jsonArrayCollector.getJSONObject(1).getLong("id"));
                } catch (org.json.JSONException e1) {
                    e1.printStackTrace();
                }
                break;
            case R.id.service_detail_rhv_collectheadview03:
                try {
                    MsgPublicTool.showHomePage(context, jsonArrayCollector.getJSONObject(2).getLong("id"));
                } catch (org.json.JSONException e1) {
                    e1.printStackTrace();
                }
                break;
            case R.id.service_detail_rhv_collectheadview04:
                try {
                    MsgPublicTool.showHomePage(context, jsonArrayCollector.getJSONObject(3).getLong("id"));
                } catch (org.json.JSONException e1) {
                    e1.printStackTrace();
                }
                break;
            case R.id.service_detail_discuss_layout:
                Intent intent_discuss = new Intent(this, DiscussActivity.class);
                intent_discuss.putExtra("discuss_author", discuss_author);
                intent_discuss.putExtra("discuss_id", discuss_id);
                intent_discuss.putExtra("discuss_title", discuss_title);
                intent_discuss.putExtra("discuss_text", discuss_text);
                //count不必传递了
                startActivity(intent_discuss);
                break;
        }
    }

    public double getDistance(double startlati, double startlongi, double endinglati, double endinglongi) {
        double lat1 = (Math.PI / 180) * startlati;
        double lat2 = (Math.PI / 180) * endinglati;
        double lon1 = (Math.PI / 180) * startlongi;
        double lon2 = (Math.PI / 180) * endinglongi;
        double R = 6371;
        return Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
    }

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }

    private void getBitmap(final String url, final ImageView imageview) {
        StaticMethod.BITMAP(context, url, imageview);
    }

    private void getBitmapHead(final String url, final ImageView imageview) {
        StaticMethod.BITMAPHEAD(context, url, imageview);
    }
}