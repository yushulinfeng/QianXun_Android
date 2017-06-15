package com.szdd.qianxun.service;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szdd.qianxun.R;
import com.szdd.qianxun.dynamic.SelectDialog;
import com.szdd.qianxun.message.baichuan.util.AndTools;
import com.szdd.qianxun.message.baichuan.util.AppUtil;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.sell.discuss.DiscussTool;
import com.szdd.qianxun.start.register.Register3;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.bitmap.BitmapTool;
import com.szdd.qianxun.tools.bitmap.MultiBitmapTool;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.FileTool;
import com.szdd.qianxun.tools.map.LocationListener;
import com.szdd.qianxun.tools.top.NetIActivity;

import java.io.File;
import java.util.ArrayList;

//删除此行，并修改104行即可
//使用的键是discussId。
public class PostServiceActivity extends NetIActivity implements View.OnClickListener {
    //后台内容
    private String timeStatus = "", weekStatus = "", str_skill_title = "", str_skill_content = "",
            str_unit = "", str_exchange = "", str_service_district = "", money = "", service_type = "", post_time = "";
    private int str_reward_way = 1, service_way = 0, post_way = 0;
    private boolean timeChose = false, typeChose = false;
    private int number = 9;
    //校园组件
    private ImageView iv_back;
    private LinearLayout layout_chose, layout_time, layout_sort, layout_money;
    private EditText edit_skill, edit_content, edit_money, edit_unit, edit_exchange, edit_district;
    private TextView tv_location, tv_settime;
    private Button btn_post, btn_sth, btn_order, post_service_sort_btn;
    private ImageView iv_sort[] = new ImageView[4];
    private ImageView iv_1, iv_2;
    private GridAdapter adapter;
    private final int GET_SORT = 0;//获取分类
    private final int GET_PICTURE = 5;//获取图片
    private final int GET_SERVICE_TIME = 6;
    private final int PHOTORESOULT = 4;// 结果
    private final int PHOTOHRAPH = 1;// 拍照
    private String picture_path = "";// 图片的绝对路径
    private ArrayList<String> list_file = new ArrayList<String>();
    private ArrayList<String> list_file_tosend = new ArrayList<String>();
    private MyGridView gridview;
    public static Dialog method_dialog = null;// 选择条用相机或者相册的对话框
    private double locationx, locationy;
    private boolean toshowdialog = true;
    ///////////////////处理评论
    private String discuss_id = "";

    @Override
    public void onCreate() {
        setContentView(R.layout.sell_post_service);
        StaticMethod.Location(PostServiceActivity.this, new LocationListener() {
            @Override
            public void locationRespose(String locationName, double x, double y, float limit) {
                locationx = x;
                locationy = y;
            }
        });
        initView();
        initEvent();
        initDialog();
    }

    @Override
    public void newThread() {
        ZipBitmap();
        sendMessage(null);
    }

    @Override
    public void receiveMessage(String what) {
        /////////////////////测试
        /////////////////////旧版本方法
        //PostRequestToServer();

        //删除上面方法和此句，解除此方法的注释即可
        postDiscussAndService();
    }

    private void initView() {
        gridview = (MyGridView) this.findViewById(R.id.post_service_gridView);
        iv_back = (ImageView) this.findViewById(R.id.post_service_iv_back);
        layout_chose = (LinearLayout) this.findViewById(R.id.post_service_chose_sort);
        edit_skill = (EditText) this.findViewById(R.id.post_service_edit_skill);
        edit_content = (EditText) this.findViewById(R.id.post_service_edit_content);
        edit_content.setSingleLine(false);
        edit_content.setHorizontalFadingEdgeEnabled(false);
        edit_money = (EditText) this.findViewById(R.id.post_service_edit_money);
        edit_unit = (EditText) this.findViewById(R.id.post_service_edit_unit);
        edit_exchange = (EditText) this.findViewById(R.id.post_service_edit_exchange);
        edit_district = (EditText) this.findViewById(R.id.post_service_edit_district);
        tv_location = (TextView) this.findViewById(R.id.post_service_tv_location);
        btn_post = (Button) this.findViewById(R.id.post_service_btn_post);
        btn_sth = (Button) this.findViewById(R.id.post_service_btn_sth);
        post_service_sort_btn = (Button) this.findViewById(R.id.post_service_sort_btn);
        btn_order = (Button) this.findViewById(R.id.post_service_btn_order);
        tv_settime = (TextView) this.findViewById(R.id.post_service_tv_set_time);
        iv_sort[0] = (ImageView) this.findViewById(R.id.post_service_iv_online);
        iv_sort[1] = (ImageView) this.findViewById(R.id.post_service_iv_zhiding);
        iv_sort[2] = (ImageView) this.findViewById(R.id.post_service_iv_post);
        iv_sort[3] = (ImageView) this.findViewById(R.id.post_service_iv_godoor);
        iv_1 = (ImageView) this.findViewById(R.id.iv_1);
        iv_2 = (ImageView) this.findViewById(R.id.iv_2);
        layout_time = (LinearLayout) this.findViewById(R.id.post_service_layout_set_time);
        layout_sort = (LinearLayout) this.findViewById(R.id.post_service_layout_sort);
        layout_money = (LinearLayout) this.findViewById(R.id.post_service_layout_money);
        list_file.add("");
        adapter = new GridAdapter(PostServiceActivity.this, list_file);
        gridview.setAdapter(adapter);
        StaticMethod.Location(PostServiceActivity.this, new LocationListener() {
            @Override
            public void locationRespose(String locationName, double x, double y, float limit) {
                tv_location.setText(locationName);
            }
        });
    }

    public void initEvent() {
        iv_back.setOnClickListener(this);
        layout_chose.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        btn_order.setOnClickListener(this);
        btn_sth.setOnClickListener(this);
        iv_sort[0].setOnClickListener(this);
        iv_sort[1].setOnClickListener(this);
        iv_sort[2].setOnClickListener(this);
        iv_sort[3].setOnClickListener(this);
        layout_time.setOnClickListener(this);
        iv_1.setOnClickListener(this);
        iv_2.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.post_service_iv_back://返回
                final DiaLogWhenBack dialog = new DiaLogWhenBack(PostServiceActivity.this);
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
            case R.id.post_service_btn_sth://有现货
                btn_sth.setTextColor(Color.parseColor("#000000"));
                btn_sth.setBackgroundResource(R.drawable.shape_post_service_btn_selected);
                btn_order.setTextColor(Color.parseColor("#aeb0b1"));
                btn_order.setBackgroundResource(R.drawable.shape_post_service_btn_unselected);
                post_way = 1;
                break;
            case R.id.post_service_btn_order://需定制
                btn_order.setTextColor(Color.parseColor("#000000"));
                btn_order.setBackgroundResource(R.drawable.shape_post_service_btn_selected);
                btn_sth.setTextColor(Color.parseColor("#aeb0b1"));
                btn_sth.setBackgroundResource(R.drawable.shape_post_service_btn_unselected);
                post_way = 3;
                break;
            case R.id.post_service_chose_sort://分类
                intent = new Intent(PostServiceActivity.this, ServiceSortActivity.class);
                startActivityForResult(intent, GET_SORT);
                break;
            case R.id.post_service_iv_online://线上
                service_way = 1;
                iv_sort[0].setImageResource(R.drawable.post_service_online_selected);
                iv_sort[0].setBackgroundResource(R.drawable.post_service_iv_selected);
                iv_sort[1].setImageResource(R.drawable.post_service_zhiding_unselected);
                iv_sort[1].setBackgroundResource(R.drawable.post_service_iv_unselected);
                iv_sort[2].setImageResource(R.drawable.post_service_post_unselected);
                iv_sort[2].setBackgroundResource(R.drawable.post_service_iv_unselected);
                iv_sort[3].setImageResource(R.drawable.post_service_godoor_unselected);
                iv_sort[3].setBackgroundResource(R.drawable.post_service_iv_unselected);

                layout_time.setVisibility(View.VISIBLE);
                layout_sort.setVisibility(View.GONE);
                break;
            case R.id.post_service_iv_zhiding://指定
                service_way = 2;
                iv_sort[0].setImageResource(R.drawable.post_service_online_unselected);
                iv_sort[0].setBackgroundResource(R.drawable.post_service_iv_unselected);
                iv_sort[1].setImageResource(R.drawable.post_service_zhiding_selected);
                iv_sort[1].setBackgroundResource(R.drawable.post_service_iv_selected);
                iv_sort[2].setImageResource(R.drawable.post_service_post_unselected);
                iv_sort[2].setBackgroundResource(R.drawable.post_service_iv_unselected);
                iv_sort[3].setImageResource(R.drawable.post_service_godoor_unselected);
                iv_sort[3].setBackgroundResource(R.drawable.post_service_iv_unselected);
                layout_time.setVisibility(View.VISIBLE);
                layout_sort.setVisibility(View.GONE);
                break;
            case R.id.post_service_iv_post://邮寄
                service_way = 3;
                iv_sort[0].setImageResource(R.drawable.post_service_online_unselected);
                iv_sort[0].setBackgroundResource(R.drawable.post_service_iv_unselected);
                iv_sort[1].setImageResource(R.drawable.post_service_zhiding_unselected);
                iv_sort[1].setBackgroundResource(R.drawable.post_service_iv_unselected);
                iv_sort[2].setImageResource(R.drawable.post_service_post_selected);
                iv_sort[2].setBackgroundResource(R.drawable.post_service_iv_selected);
                iv_sort[3].setImageResource(R.drawable.post_service_godoor_unselected);
                iv_sort[3].setBackgroundResource(R.drawable.post_service_iv_unselected);
                layout_time.setVisibility(View.GONE);
                layout_sort.setVisibility(View.VISIBLE);
                break;
            case R.id.post_service_iv_godoor://上门
                service_way = 4;
                iv_sort[0].setImageResource(R.drawable.post_service_online_unselected);
                iv_sort[0].setBackgroundResource(R.drawable.post_service_iv_unselected);
                iv_sort[1].setImageResource(R.drawable.post_service_zhiding_unselected);
                iv_sort[1].setBackgroundResource(R.drawable.post_service_iv_unselected);
                iv_sort[2].setImageResource(R.drawable.post_service_post_unselected);
                iv_sort[2].setBackgroundResource(R.drawable.post_service_iv_unselected);
                iv_sort[3].setImageResource(R.drawable.post_service_godoor_selected);
                iv_sort[3].setBackgroundResource(R.drawable.post_service_iv_selected);
                layout_time.setVisibility(View.VISIBLE);
                layout_sort.setVisibility(View.GONE);
                break;
            case R.id.post_service_layout_set_time://设置服务时间
                intent = new Intent(PostServiceActivity.this, SetTimeActivity.class);
                startActivityForResult(intent, GET_SERVICE_TIME);

                break;
            case R.id.post_service_btn_post:
                onButtonPostClick();
                break;
            case R.id.iv_1:
                iv_1.setImageResource(R.drawable.post_service_iv_selected);
                iv_2.setImageResource(R.drawable.post_service_iv_unselected);
                layout_money.setVisibility(View.VISIBLE);
                edit_exchange.setVisibility(View.GONE);
                str_reward_way = 1;
                break;
            case R.id.iv_2:
                iv_2.setImageResource(R.drawable.post_service_iv_selected);
                iv_1.setImageResource(R.drawable.post_service_iv_unselected);
                layout_money.setVisibility(View.GONE);
                edit_exchange.setVisibility(View.VISIBLE);
                str_reward_way = 2;
                break;
            case R.id.post_dynamic_dialog_btn1:
                while (list_file.contains(""))
                    list_file.remove("");
                startMultiAlbum(9, list_file);
//                intent = new Intent(this, PostServiceAddPictureActivity.class);
//                intent.putStringArrayListExtra("list_front", list_file);
//                startActivityForResult(intent, GET_PICTURE);
                break;
            case R.id.post_dynamic_dialog_btn2:
                startCamera();

//                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent2.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
//                startActivityForResult(intent2, PHOTOHRAPH);
                break;
            case R.id.post_dynamic_dialog_btn3:
                method_dialog.dismiss();
                break;
            default:
                break;
        }

    }

    private void onButtonPostClick() {
        if (!UserStateTool.isLoginNow(PostServiceActivity.this)) {
            UserStateTool.goToLogin(PostServiceActivity.this, false);
            return;
        }
        if (!isNetworkAvailable(PostServiceActivity.this)) {
            Toast.makeText(PostServiceActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        btn_post.setOnClickListener(null);
        str_skill_title = edit_skill.getText().toString();
        str_skill_content = edit_content.getText().toString();
        money = edit_money.getText().toString();
        str_unit = edit_unit.getText().toString();
        str_exchange = edit_exchange.getText().toString();
        if (service_way == 1 || service_way == 2 || service_way == 4) {
            str_service_district = edit_district.getText().toString();
        }
        if (!CheckDatacompleteness())
            return;
        AppUtil.showProgressDialogNotCancel(PostServiceActivity.this, "玩命加载中...");
        startNewThread();
    }

    public void ZipBitmap() {
        list_file.remove("");
        for (int i = 0; i < list_file.size(); i++) {
            try {
                long time = System.currentTimeMillis();
                String path = FileTool.getFilePath() + "/" + time;
                if (StaticMethod.zip(list_file.get(i), path)) {
                    list_file_tosend.add(path);
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 判断数据完整性
     */
    public boolean CheckDatacompleteness() {
        if (!typeChose) {
            Toast.makeText(PostServiceActivity.this, "请为服务选择一个分类", Toast.LENGTH_SHORT).show();
            btn_post.setOnClickListener(this);
            return false;
        }
        if (str_skill_title.equals("")) {
            Toast.makeText(PostServiceActivity.this, "请为服务取一个技能名称", Toast.LENGTH_SHORT).show();
            btn_post.setOnClickListener(this);
            return false;
        }
        if (str_skill_content == null || str_skill_content.equals("")) {
            Toast.makeText(PostServiceActivity.this, "请填写服务详述", Toast.LENGTH_SHORT).show();
            btn_post.setOnClickListener(this);
            return false;
        }
        if (str_reward_way == 1) {
            if (str_unit == null || str_unit.equals("")) {
                Toast.makeText(PostServiceActivity.this, "请填写金额单位", Toast.LENGTH_SHORT).show();
                btn_post.setOnClickListener(this);
                return false;
            } else if (money == null || money.equals("")) {
                Toast.makeText(PostServiceActivity.this, "请填写金额", Toast.LENGTH_SHORT).show();
                btn_post.setOnClickListener(this);
                return false;
            }
        }
        if (str_reward_way == 2) {
            if (str_exchange == null || str_exchange.equals("")) {
                Toast.makeText(PostServiceActivity.this, "请填写交换条件", Toast.LENGTH_SHORT).show();
                btn_post.setOnClickListener(this);
                return false;
            }
            if ((money == null || money.equals("") && ((str_unit == null || str_unit.equals(""))))) {

            } else {
                if (money == null || money.equals("")) {
                    Toast.makeText(PostServiceActivity.this, "请填写金额", Toast.LENGTH_SHORT).show();
                    btn_post.setOnClickListener(this);
                    return false;
                }
                if (str_unit == null || str_unit.equals("")) {
                    Toast.makeText(PostServiceActivity.this, "请填写金额单位", Toast.LENGTH_SHORT).show();
                    btn_post.setOnClickListener(this);
                    return false;
                }
            }


        }
        if (service_way == 0) {
            Toast.makeText(PostServiceActivity.this, "请选择一种服务方式", Toast.LENGTH_SHORT).show();
            btn_post.setOnClickListener(this);
            return false;
        }
        if (service_way == 1 || service_way == 2 || service_way == 4) {
            if (!timeChose) {
                Toast.makeText(PostServiceActivity.this, "请设置服务时间", Toast.LENGTH_SHORT).show();
                btn_post.setOnClickListener(this);
                return false;
            }
            if (str_service_district == null || str_service_district.equals("")) {
                Toast.makeText(PostServiceActivity.this, "请填写服务区域", Toast.LENGTH_SHORT).show();
                btn_post.setOnClickListener(this);
                return false;
            }
        }
        if (service_way == 3) {
            if (post_way == 0) {
                Toast.makeText(PostServiceActivity.this, "请填写邮寄服务类型", Toast.LENGTH_SHORT).show();
                btn_post.setOnClickListener(this);
                return false;
            }
        }
        if ((money != null || (!money.equals("")))) {//已测试，成功
            double int_money = 0;
            try {
                int_money = Double.parseDouble(money);
                money = StaticMethod.formatDouble(int_money);
            } catch (Exception e) {
                AndTools.showToast(this, "价格格式错误");
                btn_post.setOnClickListener(this);
                return false;
            }
            if (int_money == 0 || "0.00".equals(money)) {
                AndTools.showToast(this, "价格不能为零");
                btn_post.setOnClickListener(this);
                return false;
            } else {
                if (money.endsWith(".00")) {
                    money = money.substring(0, money.length() - 3);
                } else if (money.indexOf('.') != -1 && money.endsWith("0")) {
                    money = money.substring(0, money.length() - 1);
                }
            }
        }
        return true;
    }

    public void PostRequestToServer() {
        StaticMethod.POST(this, ServerURL.POST_SERVICE, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {

                list.put("businessService.user.id", InfoTool.getUserID(PostServiceActivity.this));//用户id
                list.put("businessService.category", service_type);//服务分类
                list.put("businessService.name", str_skill_title);//技能·
                list.put("businessService.detail", str_skill_content);//服务内容描述
                post_time = System.currentTimeMillis() + "";
                list_file.remove("");
                for (int i = 0; i < list_file.size(); i++) {
                    list.put("businessService.images.image", new File(list_file_tosend.get(i)));//图片
                }
                list.put("businessService.time", post_time);//时间戳
                list.put("businessService.reward_money", money);//钱
                list.put("businessService.reward_unit", str_unit);//单位
                list.put("businessService.reward_thing", str_exchange);//交换条件
                list.put("businessService.serviceType", service_way);//服务方式
                if (service_way == 1 || service_way == 2 || service_way == 4) {
                    list.put("businessService.serviceTime", timeStatus);
                    list.put("businessService.canServiceDay", weekStatus);
                    list.put("businessService.serviceCity", str_service_district);//服务区域
                    list.put("businessService.status", "");//现货还是定制
                } else if (service_way == 3) {
                    list.put("businessService.status", post_way);//现货还是定制
                    list.put("businessService.serviceTime", "");
                    list.put("businessService.canServiceDay", "");
                    list.put("businessService.serviceCity", "");//服务区域
                }
                list.put("businessService.location_x", locationx + "");
                list.put("businessService.location_y", locationy + "");
                //讨论区
                list.put("businessService.discussId", discuss_id + "");
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                if (response == null) {
                    Toast.makeText(PostServiceActivity.this, "服务器罢工了", Toast.LENGTH_SHORT).show();
                } else {
                    int res = Integer.parseInt(response);
                    switch (res) {
                        case 1:
                            AppUtil.dismissProgressDialog();
                            Toast.makeText(PostServiceActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case -1:
                            AppUtil.dismissProgressDialog();
                            Toast.makeText(PostServiceActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                            list_file.add("");
                            adapter = new GridAdapter(PostServiceActivity.this, list_file);
                            gridview.setAdapter(adapter);
                            btn_post.setOnClickListener(PostServiceActivity.this);
                            break;
                        default:
                            AppUtil.dismissProgressDialog();
                            Toast.makeText(PostServiceActivity.this, "未知错误发生了", Toast.LENGTH_SHORT).show();
                            btn_post.setOnClickListener(PostServiceActivity.this);
                            list_file.add("");
                            adapter = new GridAdapter(PostServiceActivity.this, list_file);
                            gridview.setAdapter(adapter);
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case GET_SORT:
                if (resultCode == RESULT_CANCELED) {

                } else if (resultCode == RESULT_OK) {
                    post_service_sort_btn.setVisibility(View.VISIBLE);
                    String sort = intent.getStringExtra("service_sort");
                    post_service_sort_btn.setText(sort);
                    service_type = intent.getStringExtra("service_type");
                    typeChose = true;
                }
                break;
            case CODE_MULITY_START:
//            case GET_PICTURE:
                if (resultCode == RESULT_CANCELED) {
                    if (list_file.size() < 9) {
                        list_file.add("");
                    }
                } else if (resultCode == RESULT_OK) {
                    list_file = MultiBitmapTool.getBitmapList(resultCode, intent);

//                    list_file = intent.getStringArrayListExtra("dirs");
                    list_file.remove("");
                    if (list_file.size() < 9) {
                        list_file.add("");
                    }
                    adapter = new GridAdapter(PostServiceActivity.this, list_file);
                    gridview.setAdapter(adapter);

                }
                method_dialog.dismiss();
                break;
            case GET_SERVICE_TIME:
                if (resultCode == RESULT_CANCELED) {

                } else if (resultCode == RESULT_OK) {
                    timeStatus = intent.getStringExtra("timestatus");
                    weekStatus = intent.getStringExtra("weekstatus");
                    tv_settime.setText("已设置");
                    tv_settime.setTextColor(Color.parseColor("#000000"));
                    timeChose = true;
                }
                break;
            case CODE_CAMERA_START:
//                if (resultCode == RESULT_OK) {
//                    cropPhoto(uritempFile);// 裁剪图片
//                }
//                break;
            case PHOTORESOULT:
                if (resultCode != RESULT_OK) return;
                method_dialog.dismiss();
                try {
                    Bitmap photo = getUriBitmap();
                    if (photo != null) {
                        picture_path = new File(FileTool.getBaseSDCardPath(),
                                System.currentTimeMillis() + ".jpg").getAbsolutePath();
                        BitmapTool.writeToFile(photo, "jpg", picture_path);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                while (list_file.contains(""))
                                    list_file.remove("");
                                list_file.add(picture_path);
                                if (list_file.size() < 9) {
                                    list_file.add("");
                                }
                                adapter = new GridAdapter(PostServiceActivity.this, list_file);
                                gridview.setAdapter(adapter);
                            }
                        }, 500);
                    }
                } catch (Exception e) {
                }
                break;
            default:
                break;
        }
    }

    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void initDialog() {
        uritempFile = Register3.getTempUri();
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
        Register3.cropPhoto(uri, uritempFile, this, PHOTORESOULT);
    }

    @Override
    public void onBackPressed() {
        final DiaLogWhenBack dialog = new DiaLogWhenBack(PostServiceActivity.this);
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

    //发布带有评论的服务
    private void postDiscussAndService() {
        DiscussTool.getInstance().postDiscuss(this, str_skill_title, str_skill_content,
                new DiscussTool.DiscussPostListener() {
                    @Override
                    public void OnResponse(String discussId) {
                        if (discussId == null)
                            discuss_id = "";
                        else
                            discuss_id = discussId;
                        PostRequestToServer();
                    }
                });
    }
}
