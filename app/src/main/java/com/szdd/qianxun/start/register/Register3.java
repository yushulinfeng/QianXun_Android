package com.szdd.qianxun.start.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.info.AnBaseInfo;
import com.szdd.qianxun.message.info.SchoolInfo;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.start.login.Login;
import com.szdd.qianxun.start.tool.SafeCheck;
import com.szdd.qianxun.tools.bitmap.BitmapTool;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectEasy;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.DealFile;
import com.szdd.qianxun.tools.map.Location;
import com.szdd.qianxun.tools.map.LocationListener;
import com.szdd.qianxun.tools.top.TActivity;
import com.szdd.qianxun.tools.views.RoundHeadView;

import java.io.File;
import java.util.Calendar;

public class Register3 extends TActivity implements OnClickListener {
    private final String[] context_items = new String[]{"相机拍照", "相册选取"};
    private static final int CODE_ALBUM_START = 1;
    private static final int CODE_CAMERA_START = 2;
    private static final int CODE_CROP_START = 3;
    private static final int CODE_SCHOOL = 4;
    private static final String ICON_SD_PATH = "Icon";
    private static final String ICON_SD_NAME = "my_head_icon.jpg";

    private String phone = "", code = "", pass = "";
    private String name = "", gender = "", birthday = "", location = "";
    private String icon_path = "", icon_base64 = "";
    private int year = 1, mon = 1, day = 1;
    private ImageButton ibtn_head;
    private EditText et_name, et_birth, et_location;
    private RoundHeadView round_head;
    private RadioGroup radiogroup;// 以后可以删去
    private RadioButton rb_man, rb_woman;
    private Button btn_birth, btn_register;
    private AlertDialog method_dialog;
    private Uri uritempFile = null;
    private TextView tv_school;

    @Override
    public void onCreate() {
        setContentView(R.layout.start_register3);
        setTitle("填写基本资料");
        showBackButton();
        initActionBar(getResources().getColor(R.color.topbar_bg));

        initMessage();
        initDate();
        initView();
        intiDialog();
        initLocation();
    }

    private void initMessage() {
        Intent intent = getIntent();
        if (intent != null) {
            phone = intent.getStringExtra("phone");
            code = intent.getStringExtra("code");
            pass = intent.getStringExtra("pass");
        } else {
            phone = "";
        }
    }

    private void initDate() {
        // 时间与日期重置
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        mon = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String mon_temp = "" + mon;
        if (mon < 10)
            mon_temp = "0" + mon_temp;
        String day_temp = "" + day;
        if (day < 10)
            day_temp = "0" + day_temp;
        birthday = year + "-" + mon_temp + "-" + day_temp;
    }

    private void initView() {
        ibtn_head = (ImageButton) findViewById(R.id.register3_ibtn_head);
        ibtn_head.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.register3_et_name);
        et_birth = (EditText) findViewById(R.id.register3_et_birthday);
        et_location = (EditText) findViewById(R.id.register3_et_location);
        round_head = (RoundHeadView) findViewById(R.id.register3_round_head);
        radiogroup = (RadioGroup) findViewById(R.id.register3_group_sex);
        rb_man = (RadioButton) findViewById(R.id.register3_radio_man);
        rb_woman = (RadioButton) findViewById(R.id.register3_radio_woman);
        tv_school = (TextView) this.findViewById(R.id.register3_et_school);
        et_birth.setText(birthday);
        et_birth.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    showDateDialog();
                return false;
            }
        });

        btn_birth = (Button) findViewById(R.id.register3_btn_birthday);
        btn_register = (Button) findViewById(R.id.register3_btn_register);
        btn_birth.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        tv_school.setOnClickListener(this);
    }

    private void intiDialog() {
        icon_path = getIconPath();
        uritempFile = getTempUri();
        method_dialog = new AlertDialog.Builder(this).setTitle("请选择方式	")
                .setItems(context_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent2 = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                                        uritempFile);
                                startActivityForResult(intent2, CODE_CAMERA_START);
                                break;
                            case 1:
                                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent1, CODE_ALBUM_START);
                                break;
                        }
                    }
                }).create();
    }

    private void initLocation() {
        Location.getLocation(getApplicationContext(), new LocationListener() {
            public void locationRespose(String locationName, double x,
                                        double y, float limit) {
                dealLocation(locationName);
            }
        });
    }

    private void dealLocation(String name) {
        if (name.equals(""))
            return;
        if (name.startsWith("中国"))
            name = name.substring(2);
        int end = name.lastIndexOf("区") + 1;
        if (end != 0)
            name = name.substring(0, end);
        location = name;
        et_location.setText(location);
    }

    private void showDateDialog() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker dp, int newyear, int month,
                                  int dayOfMonth) {
                // 这里说一句，DatePickerDialog很特殊，点击对话框外，确定方法执行一遍，
                // 点击完成，确定方法执行两遍。（本人阻碍了一下午，最后debug才知道是对话框的问题）
                year = newyear;
                mon = month + 1;
                day = dayOfMonth;
                String mon_temp = "" + mon;
                if (mon < 10)
                    mon_temp = "0" + mon_temp;
                String day_temp = "" + day;
                if (day < 10)
                    day_temp = "0" + day_temp;
                birthday = year + "-" + mon_temp + "-" + day_temp;
                et_birth.setText(birthday);
            }
        }, year, mon - 1, day).show();
    }

    private void startToRegister() {
        if (phone.equals("")) {
            showToast("系统错误");
            return;
        }
        if (rb_man.isChecked())
            gender = "男";
        else
            gender = "女";
        if (!new File(icon_path).exists()) {
            showToast("请添加头像");
            return;
        }
        if (tv_school.getText().toString().equals("点击选择")) {
            showToast("请添加学校");
            return;
        }
        name = et_name.getText().toString();
        if (!SafeCheck.checkNickName(this, name))
            return;
        if (name.equals("000000")) {
            showToast("测试账户");
            registerSuccess();
            finish();
            return;
        }

        ConnectEasy.POST(this, ServerURL.REGISTER, new ConnectListener() {
            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                dialog.config(Register3.this, "正在注册", "请稍候……", false);
                return dialog;
            }

            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("user.username", phone);
                list.put("user.password", pass);
                list.put("user.registCheckCode", code);
                list.put("user.school", tv_school.getText().toString());
                list.put("user.birthday", birthday);
                list.put("user.iconfile", new File(icon_path));//icon_base64,前方注释了一句
                list.put("user.address", location);
                list.put("user.nickName", name);
                list.put("user.gender", gender);
                return list;
            }

            @Override
            public void onResponse(String response) {
                if (response == null || response.equals("") || response.equals("-2")
                        || response.equals("fail")) {// 系统错误-2
                    showToast("网络错误");
                } else if (response.equals("1")) {
                    registerSuccess();// 注册成功，自动登录
                } else if (response.equals("-3")) {// 验证码错误-3
                    showToast("验证码错误");
                    Intent intent = new Intent(Register3.this, Register2.class);
                    startActivity(intent);
                    finish();
                } else if (response.equals("-1")) {// 已注册（理论上不可能）-1
                    showToast("用户已注册");
                    Intent intent = new Intent(Register3.this, Register1.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToast("网络错误");
                }
            }
        });

    }

    private void registerSuccess() {
        // 保存本地记录
        AnBaseInfo info = new AnBaseInfo(phone, name, gender, birthday,
                location, icon_path);
        InfoTool.saveBaseInfo(this, info);
        // 自动登录
        Intent intent = new Intent(this, Login.class);
        intent.putExtra("name", phone);
        intent.putExtra("pass", pass);
        startActivity(intent);
        finish();
    }

    // 调用系统裁剪
    public void cropPhoto(Uri uri) {
        cropPhoto(uri, uritempFile, this, CODE_CROP_START);
    }

    // 调用系统裁剪
    public static void cropPhoto(Uri in, Uri out, Activity activity, int code) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(in, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 宽高的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);// 裁剪图片宽高
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);//内存过小的手机返回大的图片会崩溃
//        //uritempFile为Uri类变量，实例化uritempFile
        intent.putExtra(MediaStore.EXTRA_OUTPUT, out);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        activity.startActivityForResult(intent, code);
    }

    // 保存图片
    private void saveBitmap(Bitmap mBitmap) {
        mBitmap = BitmapTool.zipBitmap(mBitmap, 300, 300, true);// 压缩图片使之小于等于300*300
        mBitmap = BitmapTool.getCroppedRoundBitmap(mBitmap);// 处理为圆角
        BitmapTool.writeToFile(mBitmap, "png", icon_path);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register3_ibtn_head:
                method_dialog.show();
                break;
            case R.id.register3_btn_birthday:
                showDateDialog();
                break;
            case R.id.register3_btn_register:
                startToRegister();
                break;
            case R.id.register3_et_school:
                Intent intent = new Intent(Register3.this, SchoolInfo.class);
                startActivityForResult(intent, CODE_SCHOOL);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_ALBUM_START:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }
                break;
            case CODE_CAMERA_START:
                if (resultCode == RESULT_OK) {
                    cropPhoto(uritempFile);// 裁剪图片
                }
                break;
            case CODE_CROP_START:
                if (resultCode != RESULT_OK)
                    return;
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(
                            getContentResolver().openInputStream(uritempFile));
                    if (bitmap != null) {
                        saveBitmap(bitmap);// 保存在SD卡中，并同步到用户信息
                        round_head.setImageBitmap(bitmap);// 用ImageView显示出来
                        ibtn_head.setImageResource(R.drawable.shape_null);
                    }
                } catch (Exception e) {
                }
                break;
            case CODE_SCHOOL:
                if (resultCode == RESULT_OK) {
                    tv_school.setText(data.getStringExtra("school"));
                }
                break;
        }
    }

    @Override
    public void showContextMenu() {
    }

    public static Uri getTempUri() {//请确保GaoNeng文件夹存在
        return Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/GaoNeng/" + "headIconTemp.jpg");

    }

    // 这个在后期修改头像时会用到
    public static String getIconPath() {
        File root = getMessageSDPath();
        if (root == null)
            return null;
        File path = new File(root, ICON_SD_PATH);
        try {
            if (!path.exists())
                path.mkdirs();
        } catch (Exception e) {
            return null;
        }
        File icon = new File(path, ICON_SD_NAME);
        try {
            if (icon.exists())
                icon.delete();
        } catch (Exception e) {
            return null;
        }
        String icon_path0 = icon.getAbsolutePath();
        return icon_path0;
    }

    // 这个在后期修改头像时会用到
    public static String getIconPathOnly() {
        File root = getMessageSDPath();
        if (root == null)
            return null;
        File path = new File(root, ICON_SD_PATH);
        try {
            if (!path.exists())
                path.mkdirs();
        } catch (Exception e) {
            return null;
        }
        File icon = new File(path, ICON_SD_NAME);
        String icon_path0 = icon.getAbsolutePath();
        return icon_path0;
    }

    private static final String MESSAGE_SD_PATH = "Message";

    /**
     * 获取图像存储路径
     */
    public static File getMessageSDPath() {
        File root = DealFile.getBaseSDCardPath();
        if (root == null)
            return null;
        File path = new File(root, MESSAGE_SD_PATH);
        try {
            if (!path.exists())
                path.mkdirs();
        } catch (Exception e) {
            return null;
        }
        return path;
    }

    /**
     * 获取图像存储路径-子路径
     */
    public static File getMessageSDPath(String sub_path) {
        File root = getMessageSDPath();
        if (root == null)
            return null;
        File path = new File(root, sub_path);
        try {
            if (!path.exists())
                path.mkdirs();
        } catch (Exception e) {
            return null;
        }
        return path;
    }

    /**
     * 获取图像存储路径-子路径-文件名
     */
    public static File getMessageSDPath(String sub_path, String file_name,
                                        boolean need_delete) {
        File root = getMessageSDPath(sub_path);
        if (root == null)
            return null;
        File path = new File(root, file_name);
        try {
            if (need_delete)
                if (path.exists())
                    path.delete();
        } catch (Exception e) {
            return null;
        }
        return path;
    }

}
