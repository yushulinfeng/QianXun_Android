package com.szdd.qianxun.dynamic;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.baichuan.util.AppUtil;
import com.szdd.qianxun.message.info.AnBaseInfo;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.service.MyGridView;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.bitmap.BitmapTool;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.FileTool;
import com.szdd.qianxun.tools.map.LocationListener;
import com.szdd.qianxun.tools.top.NetIActivity;
import com.szdd.qianxun.tools.views.QianxunToast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by linorz on 2016/3/29.
 */
public class PostDynamic extends NetIActivity implements View.OnClickListener {
    private final int GET_PICTURE = 555;
    private MyGridView girdView;
    private String postDynamic = ServerURL.POST_DYNAMIC;
    private Activity activity;
    private Button back = null;
    public static Button sendButton = null;
    private ImageView head = null;
    private double addressX = -1, addressY = -1;
    public static Dialog method_dialog = null;// 选择条用相机或者相册的对话框
    private final int RETURNSERVICE = 0;//返回服务
    private final int PHOTORESOULT = 4;// 结果
    private final int PHOTOHRAPH = 1;// 拍照
    private String picture_path = "";// 图片的绝对路径
    private String address = null;
    private boolean isShowLocation = false;
    private boolean isGetAddress = false;
    //自己编写的内容
    private EditText dynamicTest = null;
    private RelativeLayout layout = null;
    private LinearLayout locationLayout = null;
    private TextView locationTest = null, addService = null;
    private long serverId = 0;
    private ArrayList<String> list_file = new ArrayList<String>();
    private ArrayList<String> list_file_tosend = new ArrayList<String>();
    private GridAdapterDynamic adapter;


    @Override
    public void onCreate() {
        activity = this;
        setContentView(R.layout.post_dynamic);
        initView();//初始化各种控件
        initDialog();
    }

    @Override
    public void receiveMessage(String what) {
        postDynamic();
    }

    @Override
    public void newThread() {
        zipBitmap();
        sendMessage(null);
    }

    public void initView() {
        girdView = (MyGridView) this.findViewById(R.id.post_dynamic_gridView);
        list_file.add("");
        adapter = new GridAdapterDynamic(PostDynamic.this, list_file);
        girdView.setAdapter(adapter);
        back = (Button) findViewById(R.id.post_dynamic_back);//返回
        head = (ImageView) findViewById(R.id.post_dynamic_head);//头像
        updateHeadView();//头像显示
        dynamicTest = (EditText) findViewById(R.id.post_dynamic_edittext);//发布的文字
        locationTest = (TextView) findViewById(R.id.post_dynamic_location);//地理位置
        locationLayout = (LinearLayout) findViewById(R.id.post_dynamic_layout1);//位置外的布局
        addService = (TextView) findViewById(R.id.post_dynamic_addservice);//添加服务
        layout = (RelativeLayout) findViewById(R.id.post_dynamic_layout);//添加服务的外层布局
        layout.setOnClickListener(this);
        sendButton = (Button) findViewById(R.id.post_dynamic_sendbtn);//发布按钮
        back.setOnClickListener(this);
        locationLayout.setOnClickListener(this);
        sendButton.setEnabled(false);
        sendButton.setTextColor(0xFFFFFFFF);
        sendButton.setOnClickListener(this);
        dynamicTest.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editStart = dynamicTest.getSelectionStart();
                editEnd = dynamicTest.getSelectionEnd();
                if (temp.length() > 140) {
                    showToast("你输入的字数已经超过了限制!");
                    editable.delete(editStart - 1, editEnd);
                    dynamicTest.setText(editable);
                    dynamicTest.setSelection(dynamicTest.getText().toString().length());
                }
                if (dynamicTest.getText().toString().equals("")) turnSendButton(false);
                else turnSendButton(true);

            }
        });//按钮颜色的改变

    }

    public void zipBitmap() {
        list_file.remove("");
        for (int i = 0; i < list_file.size(); i++) {
            long time = System.currentTimeMillis();
            String path = FileTool.getFilePath() + "/" + time;
            if (StaticMethod.zip(list_file.get(i), path)) {
                list_file_tosend.add(path);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_dynamic_back:
                finish();
                break;
            case R.id.post_dynamic_sendbtn:
                AppUtil.showProgressDialogNotCancel(PostDynamic.this, "玩命加载中...");
                startNewThread();
                break;
            case R.id.post_dynamic_layout1:
                if (isShowLocation) {
                    locationTest.setText("");
                } else {
                    if (isGetAddress) {
                        locationTest.setText(address);
                    } else {
                        com.szdd.qianxun.tools.map.Location.getLocation(this,
                                new LocationListener() {
                                    @Override
                                    public void locationRespose(String locationName, double x, double y, float limit) {
                                        address = locationName;// 地址
                                        addressX = x;
                                        addressY = y;
                                        locationTest.setText(address);
                                        isGetAddress = true;
                                    }
                                });
                    }
                }
                isShowLocation = !isShowLocation;
                break;
            case R.id.post_dynamic_layout:
                Intent intent1 = new Intent(activity, SelectService.class);
                intent1.putExtra("userId", InfoTool.getUserID(activity));
                startActivityForResult(intent1, RETURNSERVICE);
                break;
            case R.id.post_dynamic_dialog_btn1:
                list_file.remove("");
                startMultiAlbum(3, list_file);
//                Intent intent = new Intent(PostDynamic.this, AddPictureActivityDynamic.class);
//                intent.putStringArrayListExtra("list_front", list_file);
//                startActivityForResult(intent, GET_PICTURE);
                break;
            case R.id.post_dynamic_dialog_btn2:
                startCamera();
//                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                picture_path = new File(FileTool.getBaseSDCardPath(), System.currentTimeMillis() + ".jpg").getAbsolutePath();
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

    public void postDynamic() {
        StaticMethod.POST(activity, postDynamic, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("dynamic.user.id", InfoTool.getUserID(activity));//发布人id
                list.put("dynamic.content", dynamicTest.getText().toString());//文字
                list.put("dynamic.publishTime", System.currentTimeMillis());//时间戳
                if (serverId != 0)
                    list.put("dynamic.businessService.id", serverId);//添加的服务id
                //位置信息
                if (locationTest.getText().toString().equals("")) {
                    list.put("dynamic.location_x", "");
                    list.put("dynamic.location_y", "");
                } else {
                    list.put("dynamic.location_x", addressX + "");
                    list.put("dynamic.location_y", addressY + "");
                }
                //三张图片
                for (int i = 0; i < list_file.size(); i++) {
                    list.putFile("dynamic.img" + (i + 1), new File(list_file_tosend.get(i)));
                }
                sendButton.setEnabled(false);
                return list;
            }

            @Override
            public void onResponse(String response) {
                if (response.equals("1")) {
                    AppUtil.dismissProgressDialog();
                    showToast("发布成功");
                    finish();
                } else if (response.equals("-1")) {
                    AppUtil.dismissProgressDialog();
                    sendButton.setEnabled(true);
                    showToast("发布失败");
                } else {
                    AppUtil.dismissProgressDialog();
                    sendButton.setEnabled(true);
                    showToast("网络请求失败");
                }
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                dialog.config(activity, "发送中...", "玩命加载中...", false);
                return dialog;
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_MULITY_START:
                if (resultCode == RESULT_CANCELED) {
                    list_file.remove("");
                    if (list_file.size() < 3) {
                        list_file.add("");
                    }
                } else if (resultCode == RESULT_OK) {
//                    list_file = data.getStringArrayListExtra("dirs");
                    list_file = getMultiAlbum(data);
                    list_file.remove("");
                    if (list_file.size() < 3) {
                        list_file.add("");
                    }
                    adapter = new GridAdapterDynamic(PostDynamic.this, list_file);
                    girdView.setAdapter(adapter);
                    turnSendButton(true);
                }
                method_dialog.dismiss();
                break;
            case RETURNSERVICE:
                if (resultCode == RESULT_OK) {
                    serverId = data.getLongExtra("id", 0);
                    if (serverId != 0) {
                        String str = data.getStringExtra("service");
                        addService.setText(str);
                    } else
                        addService.setText("添加你的服务");
                }
                break;
//            case PHOTOHRAPH:
//                if (resultCode == RESULT_OK) {
//                    File temp = new File(picture_path);
//                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
//                }
//                break;
            case CODE_CAMERA_START:
            case PHOTORESOULT:
                if (resultCode != RESULT_OK) return;

//                Bundle extras = data.getExtras();
//                if (extras != null) {
//                    Bitmap photo = extras.getParcelable("data");
                Bitmap photo = getUriBitmap();
                picture_path = new File(FileTool.getBaseSDCardPath(), System.currentTimeMillis() + ".jpg").getAbsolutePath();
                BitmapTool.writeToFile(photo, "jpg", picture_path);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list_file.remove("");
                        list_file.add(picture_path);
                        if (list_file.size() < 3) {
                            list_file.add("");
                        }
                        adapter = new GridAdapterDynamic(PostDynamic.this, list_file);
                        girdView.setAdapter(adapter);
                        turnSendButton(true);
                    }
                }, 500);
//        }
                method_dialog.dismiss();
                break;
            default:
                break;

        }
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

    // 更新头像与昵称显示
    public void updateHeadView() {// 便于外部刷新
        if (UserStateTool.isLoginEver(this)) {// 只要登录过
            AnBaseInfo base_info = InfoTool.getBaseInfo(this);// 已确保不会返回null
            String user_id = base_info.getUserId();
            if (null != user_id && !"".equals(user_id)) {// 本地有基本信息，就同步
                head.setImageBitmap(base_info.getHeadIcon());
            }
        }
    }

    public static void turnSendButton(boolean isOk) {
        sendButton.setEnabled(isOk);
        if (isOk) sendButton.setTextColor(0xFF000000);
        else sendButton.setTextColor(0xFFFFFFFF);
    }

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }
}
