package com.szdd.qianxun.request;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.szdd.qianxun.R;
import com.szdd.qianxun.dynamic.SelectDialog;
import com.szdd.qianxun.message.baichuan.util.AndTools;
import com.szdd.qianxun.message.baichuan.util.AppUtil;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.service.DiaLogWhenBack;
import com.szdd.qianxun.service.MyGridView;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.bitmap.BitmapTool;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.FileTool;
import com.szdd.qianxun.tools.top.NetIActivity;
import com.szdd.qianxun.tools.top.TActivity;
import com.szdd.qianxun.tools.views.QianxunToast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PostRequest extends NetIActivity implements OnClickListener {
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener;
    private Bundle bundle;
    private TextView[] img_sort = new TextView[4];
    private Button btn_publish;
    private EditText edit_content, edit_reward, edit_reward_else, edit_key;
    private TextView tv_real_location;
    public static TextView tv_timeLimit;
    private boolean cansee = false;
    private LinearLayout layout_start, layout_end;
    private TextView tv_click, line, line2, tv_change;
    private ImageView btn_back;
    private String startValue, endingValue;
    private double distance, startLati, startLongi, endingLati, endingLongi;
    private int type = 5;
    private String request_rewardvalue = "", timeLimit = "", startLocation_remark = "",
            startAddress = "", endLocation_remark = "", endAddress = "", request_content = "";// 图片的绝对路径
    private ArrayList<String> picture_paths = new ArrayList<String>();
    private ArrayList<String> picture_paths_tosend = new ArrayList<String>();
    private String post_time = "";// 发送需求的时间
    private static final int CHOSE_START = 5, CHOSE_ENDING = 6, GET_PICTURE = 555;
    private final int PHOTORESOULT = 4;// 结果
    private final int PHOTOHRAPH = 1;// 拍照
    private String picture_path = "";// 图片的绝对路径
    private GridAdapterRequest adapter;
    private MyGridView gridview;
    public static Dialog method_dialog = null;// 选择条用相机或者相册的对话框

    @Override
    public void onCreate() {
        setContentView(R.layout.activity_doing_second);
        init();
        initDialog();
        TActivity.addActionBar(this, R.color.main_yellow);
        initLocation();
        mLocationClient.start();
    }

    @Override
    public void receiveMessage(String what) {
        PostToServer(PostRequest.this, ServerURL.POST_REQUEST_URL);
    }

    @Override
    public void newThread() {
        zipBitmap();
        sendMessage(null);

    }

    public void initActionBar() {
        // 检查系统版本
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return;
        // 透明化状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 添加透明状态栏
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {// 状态栏高度获取成功
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            LinearLayout view = (LinearLayout) findViewById(R.id.top_titlebar_post);
            View statusBarView = new View(this);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, statusBarHeight);
            statusBarView.setLayoutParams(params);
            statusBarView.setBackgroundColor(getResources().getColor(R.color.topbar_bg));
            view.addView(statusBarView);
        }
    }

    public void init() {
        bundle = new Bundle();
        picture_paths.add("");
        gridview = (MyGridView) this.findViewById(R.id.post_request_gridView);
        adapter = new GridAdapterRequest(PostRequest.this, picture_paths);
        gridview.setAdapter(adapter);
        mLocationClient = new LocationClient(getApplicationContext());
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
        tv_change = (TextView) this.findViewById(R.id.text_change);
        edit_reward = (EditText) this.findViewById(R.id.edit_reward);
        edit_content = (EditText) this.findViewById(R.id.edit_task);
        edit_reward_else = (EditText) this.findViewById(R.id.edit_reward_else);
        edit_key = (EditText) this.findViewById(R.id.edit_key);
        btn_publish = (Button) this.findViewById(R.id.btn_publish);
        line2 = (TextView) this.findViewById(R.id.line_Nosee2);
        tv_click = (TextView) this.findViewById(R.id.tv_click);

        tv_real_location = (TextView) this.findViewById(R.id.real_location);
        tv_timeLimit = (TextView) this.findViewById(R.id.time_request_value);
        img_sort[0] = (TextView) this.findViewById(R.id.img_sotr0);
        img_sort[1] = (TextView) this.findViewById(R.id.img_sotr1);
        img_sort[2] = (TextView) this.findViewById(R.id.img_sotr2);
        img_sort[3] = (TextView) this.findViewById(R.id.img_sotr3);
        btn_back = (ImageView) this.findViewById(R.id.back_post);
        // 为组件设置监听
        btn_back.setOnClickListener(this);
        img_sort[0].setOnClickListener(this);
        img_sort[1].setOnClickListener(this);
        img_sort[2].setOnClickListener(this);
        img_sort[3].setOnClickListener(this);
        tv_timeLimit.setOnClickListener(this);
        btn_publish.setOnClickListener(this);
        tv_click.setOnClickListener(this);
        edit_key.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
                int number = text.toString().length();
                tv_change.setText(number + "/3");
            }

            @Override
            public void beforeTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    /**
     * 设置定位参数包括：定位模式（高精度定位模式，低功耗定位模式和仅用设备定位模式）。 返回坐标类型，是否打开GPS，是否返回地址信息、位置语义化信息、
     * POI信息等等。
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();// 该类用来设置sdk的定位方式
        option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 内部类实现百度地图监听，为LocationListener注册监听器
     */
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());

            startLati = location.getLatitude();
            startLongi = location.getLongitude();
            endingLati = location.getLatitude();
            endingLongi = location.getLongitude();

            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());// 地址
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
                // 显示地址(指定区域)
                tv_real_location.setText(FormatString(location.getAddrStr()));
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                // 显示地址
                String currentLocation = location.getAddrStr();
                if (currentLocation.length() > 20) {
                    String str = "";
                    for (int i = 0; i < 15; i++) {
                        str += startValue.charAt(i);
                    }
                    tv_real_location.setText(str + "...");
                } else {
                    tv_real_location.setText(location.getAddrStr());
                }

                // 运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.img_sotr0:
                type = 1;
                img_sort[0].setBackgroundResource(R.drawable.shape_btn_request_selected);
                img_sort[0].setTextColor(Color.parseColor("#ffffff"));
                img_sort[1].setBackgroundResource(R.drawable.shape_btn_request);
                img_sort[1].setTextColor(Color.parseColor("#2FE841"));
                img_sort[2].setBackgroundResource(R.drawable.shape_btn_request);
                img_sort[2].setTextColor(Color.parseColor("#2FE841"));
                img_sort[3].setBackgroundResource(R.drawable.shape_btn_request);
                img_sort[3].setTextColor(Color.parseColor("#2FE841"));
                break;
            case R.id.img_sotr1:
                type = 2;
                img_sort[0].setBackgroundResource(R.drawable.shape_btn_request);
                img_sort[0].setTextColor(Color.parseColor("#2FE841"));
                img_sort[1].setBackgroundResource(R.drawable.shape_btn_request_selected);
                img_sort[1].setTextColor(Color.parseColor("#ffffff"));
                img_sort[2].setBackgroundResource(R.drawable.shape_btn_request);
                img_sort[2].setTextColor(Color.parseColor("#2FE841"));
                img_sort[3].setBackgroundResource(R.drawable.shape_btn_request);
                img_sort[3].setTextColor(Color.parseColor("#2FE841"));
                break;
            case R.id.img_sotr2:
                type = 3;
                img_sort[0].setBackgroundResource(R.drawable.shape_btn_request);
                img_sort[0].setTextColor(Color.parseColor("#2FE841"));
                img_sort[1].setBackgroundResource(R.drawable.shape_btn_request);
                img_sort[1].setTextColor(Color.parseColor("#2FE841"));
                img_sort[2].setBackgroundResource(R.drawable.shape_btn_request_selected);
                img_sort[2].setTextColor(Color.parseColor("#ffffff"));
                img_sort[3].setBackgroundResource(R.drawable.shape_btn_request);
                img_sort[3].setTextColor(Color.parseColor("#2FE841"));
                break;
            case R.id.img_sotr3:
                img_sort[0].setBackgroundResource(R.drawable.shape_btn_request);
                img_sort[0].setTextColor(Color.parseColor("#2FE841"));
                img_sort[1].setBackgroundResource(R.drawable.shape_btn_request);
                img_sort[1].setTextColor(Color.parseColor("#2FE841"));
                img_sort[2].setBackgroundResource(R.drawable.shape_btn_request);
                img_sort[2].setTextColor(Color.parseColor("#2FE841"));
                img_sort[3].setBackgroundResource(R.drawable.shape_btn_request_selected);
                img_sort[3].setTextColor(Color.parseColor("#ffffff"));
                type = 4;
                break;
            case R.id.back_post:// 返回
                final DiaLogWhenBack dialog = new DiaLogWhenBack(PostRequest.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setWhenBack(new DiaLogWhenBack.DialogWhenBackEvent() {
                    @Override
                    public void positiveAction() {
                        dialog.dismiss();
                    }

                    @Override
                    public void negativeAction() {
                        finish();
                    }
                });
                dialog.show();
                break;
            case R.id.time_request_value:// 获取需求时间限制
                DateTimePickDialogUtil date = new DateTimePickDialogUtil(
                        PostRequest.this, null);
                date.dateTimePicKDialog(tv_timeLimit);
                break;
            case R.id.btn_publish:
                if (type == 5) {
                    showToast("请选择一个分类");
                } else {
                    if (edit_reward_else.getText().toString().equals("") && edit_reward.getText().toString().equals("")) {
                        showToast("请选择一种酬劳方式");
                    } else {
                        btn_publish.setOnClickListener(null);
                        Calendar cc = Calendar.getInstance();// 获取时间的工具类
                        timeLimit = tv_timeLimit.getText().toString();
                        post_time = cc.get(Calendar.YEAR) + "-" + (cc.get(Calendar.MONTH) + 1) + "-" + cc.get(Calendar.DAY_OF_MONTH) + "-" + cc.get(Calendar.HOUR) + ":" + cc.get(Calendar.MINUTE);
                        distance = getDistance(startLati, startLongi, endingLati, endingLongi);
                        request_content = edit_content.getText().toString();
                        request_rewardvalue = edit_reward.getText().toString();
                        if (request_rewardvalue != null && !request_rewardvalue.endsWith("")) {
                            double int_money = 0;
                            try {
                                int_money = Double.parseDouble(request_rewardvalue);
                                request_rewardvalue = StaticMethod.formatDouble(int_money);
                            } catch (Exception e) {
                                AndTools.showToast(this, "酬劳格式错误");
                                btn_publish.setOnClickListener(this);
                                return;
                            }
                            if (int_money == 0||"0.00".equals(request_rewardvalue)) {
                                AndTools.showToast(this,"价格不能为零");
                                btn_publish.setOnClickListener(this);
                                return;
                            }
                        }
                        ///////////
                        AppUtil.showProgressDialogNotCancel(PostRequest.this, "玩命加载中...");
                        startNewThread();
                    }
                }
                break;

            case R.id.post_dynamic_dialog_btn1:
                picture_paths.remove("");
                startMultiAlbum(3, picture_paths);
//                intent = new Intent(this, AddPictureActivityDynamic.class);
//                intent.putStringArrayListExtra("list_front", picture_paths);
//                startActivityForResult(intent, GET_PICTURE);
                break;
            case R.id.post_dynamic_dialog_btn2:
                startCamera();
//                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                picture_path = new File("/mnt/sdcard/GaoNeng", System.currentTimeMillis() + ".jpg").getAbsolutePath();
//                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(picture_path)));
//                startActivityForResult(intent2, PHOTOHRAPH);
                break;
            case R.id.post_dynamic_dialog_btn3:
                method_dialog.dismiss();
                break;
            default:
                break;
        }
    }

    public void zipBitmap() {
        picture_paths.remove("");
        for (int i = 0; i < picture_paths.size(); i++) {
            long time = System.currentTimeMillis();
            String path = FileTool.getFilePath() + "/" + time;
            if (StaticMethod.zip(picture_paths.get(i), path)) {
                picture_paths_tosend.add(path);
            }
        }
    }

    public void PostToServer(final Context context, String url) {
        StaticMethod.POST(context, url,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        return null;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("userRequest.user.id", InfoTool.getUserID(PostRequest.this));
                        list.put("userRequest.type", type);
                        list.put("userRequest.startLocation_remark", startLocation_remark);
                        list.put("userRequest.startAddress", startAddress);
                        list.put("userRequest.startLocation_x", "" + startLongi);
                        list.put("userRequest.startLocation_y", startLati + "");
                        list.put("userRequest.endLocation_remark", endLocation_remark);
                        list.put("userRequest.endAddress", endAddress);
                        list.put("userRequest.endLocation_x", "" + endingLongi);
                        list.put("userRequest.endLocation_y", endingLati + "");
                        list.put("userRequest.request_content", request_content);
                        list.put("userRequest.request_limitTime", timeLimit);
                        list.put("userRequest.request_key", edit_key.getText().toString());
                        picture_paths_tosend.remove("");
                        for (int i = 0; i < picture_paths_tosend.size(); i++) {
                            list.put("userRequest.img" + (i + 1), new File(picture_paths_tosend.get(i)));
                        }
                        list.put("userRequest.reward_money", request_rewardvalue);
                        list.put("userRequest.reward_thing", edit_reward_else.getText().toString());
                        long posttime = System.currentTimeMillis();
                        list.put("userRequest.request_postTime", posttime);
                        distance = getDistance(startLati, startLongi, endingLati, endingLongi);
                        list.put("userRequest.distance", "" + distance);
                        return list;
                    }

                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(String response) {
                        if (response == null) showToast("网络错误");
                        else {
                            int value = Integer.parseInt(response);
                            switch (value) {
                                case 1:
                                    AppUtil.dismissProgressDialog();
                                    showToast("发布成功");
                                    finish();
                                    break;
                                case -1:
                                    AppUtil.dismissProgressDialog();
                                    showToast("发布失败");
                                    btn_publish.setOnClickListener(PostRequest.this);

                                    break;
                                case -2:
                                    AppUtil.dismissProgressDialog();
                                    showToast("服务器出错");
                                    btn_publish.setOnClickListener(PostRequest.this);
                                    break;
                                case -3:
                                    AppUtil.dismissProgressDialog();
                                    showToast("您发布的需求包含敏感词汇，已被驳回");
                                    btn_publish.setOnClickListener(PostRequest.this);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CHOSE_START:// 选择起点
                if (resultCode == RESULT_OK) {
                    startLati = data.getExtras().getDouble("latitude");// 得到起点经度
                    startLongi = data.getExtras().getDouble("longitude");// 得到起点纬度
                    startLocation_remark = data.getExtras().getString("targetValue_remark");// 街道、门牌号、或者楼号等具体信息
                    startAddress = data.getExtras().getString("targetValue");
                    startValue = startAddress + startLocation_remark;// 获取地址，在组件中显示
                }
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                break;
            case CHOSE_ENDING:// 选择终点
                if (resultCode == RESULT_OK) {
                    endingLati = data.getExtras().getDouble("latitude");// 得到终点经度
                    endingLongi = data.getExtras().getDouble("longitude");// 得到终点纬度
                    endLocation_remark = data.getExtras().getString("targetValue_remark");// 街道、门牌号、或者楼号等具体信息
                    endAddress = data.getExtras().getString("targetValue");
                    endingValue = endAddress + endLocation_remark;
                }
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                break;
//            case GET_PICTURE:
            case CODE_MULITY_START:
                method_dialog.dismiss();
                if (resultCode == RESULT_CANCELED) {
                    picture_paths.remove("");
                    if (picture_paths.size() < 3) {
                        picture_paths.add("");
                    }
                } else if (resultCode == RESULT_OK) {
                    picture_paths = getMultiAlbum(data);
//                    picture_paths = data.getStringArrayListExtra("dirs");
                    picture_paths.remove("");
                    if (picture_paths.size() < 3) {
                        picture_paths.add("");
                    }
                    adapter = new GridAdapterRequest(PostRequest.this, picture_paths);
                    gridview.setAdapter(adapter);
                }
                break;
//            case PHOTOHRAPH:
//                if (resultCode == RESULT_OK) {
//                    File temp = new File(picture_path);
//                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
//                }
//                break;
            case CODE_CAMERA_START:
//                uritempFile=data.getData();
            case PHOTORESOULT:
                if (resultCode != RESULT_OK) return;
//                Bundle extras = data.getExtras();
//                if (extras != null) {
//                    Bitmap photo = extras.getParcelable("data");
                Bitmap photo = getUriBitmap();
                if (photo == null) return;
                picture_path = new File(FileTool.getBaseSDCardPath(),
                        System.currentTimeMillis() + ".jpg").getAbsolutePath();
                BitmapTool.writeToFile(photo, "jpg", picture_path);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        picture_paths.remove("");
                        picture_paths.add(picture_path);
                        if (picture_paths.size() < 3) {
                            picture_paths.add("");
                        }
                        adapter = new GridAdapterRequest(PostRequest.this, picture_paths);
                        gridview.setAdapter(adapter);
                    }
                }, 500);
//                }
                method_dialog.dismiss();
                break;
            default:
                break;

        }
    }

    public double getDistance(double startlati, double startlongi, double endinglati, double endinglongi) {
        double lat1 = (Math.PI / 180) * startlati;
        double lat2 = (Math.PI / 180) * endinglati;
        double lon1 = (Math.PI / 180) * startlongi;
        double lon2 = (Math.PI / 180) * endinglongi;
        // 地球半径
        double R = 6371;
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
                * Math.cos(lat2) * Math.cos(lon2 - lon1))
                * R;
        return d;
    }

    // 格式化字符串的相关方法,主要解决地点过长的显示问题
    public String FormatString(final String str) {
        String resultStr = "";
        int size = str.length();
        if (size > 17) {
            for (int i = 0; i < 15; i++) {
                resultStr += str.charAt(i);
            }
            return resultStr + "...";
        } else {
            return str;
        }
    }

    @Override
    public void onBackPressed() {
        final DiaLogWhenBack dialog = new DiaLogWhenBack(PostRequest.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setWhenBack(new DiaLogWhenBack.DialogWhenBackEvent() {
            @Override
            public void positiveAction() {
                dialog.dismiss();

            }

            @Override
            public void negativeAction() {
                finish();
            }
        });
        dialog.show();
    }

    /**
     * 初始化选择添加图片方式的对话框
     */
    private void initDialog() {
        View dialog_view = LayoutInflater.from(this).inflate(R.layout.post_dynamic_dialog, null);
        method_dialog = new SelectDialog(this, dialog_view, R.style.dynamic_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            method_dialog.create();
        }
        Window dialogWindow = method_dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 100;
        dialogWindow.setAttributes(lp);
        dialog_view.findViewById(R.id.post_dynamic_dialog_btn1).setOnClickListener(this);
        dialog_view.findViewById(R.id.post_dynamic_dialog_btn2).setOnClickListener(this);
        dialog_view.findViewById(R.id.post_dynamic_dialog_btn3).setOnClickListener(this);
    }

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 宽高的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);// 裁剪图片宽高
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESOULT);
    }

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }
}
