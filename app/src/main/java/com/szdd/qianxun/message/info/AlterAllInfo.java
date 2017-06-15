package com.szdd.qianxun.message.info;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
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

import com.joooonho.SelectableRoundedImageView;
import com.szdd.qianxun.R;
import com.szdd.qianxun.message.baichuan.util.AppUtil;
import com.szdd.qianxun.message.info.credit.RealNameCheck;
import com.szdd.qianxun.message.info.credit.RealStudentCheck;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.MSG_FLAG;
import com.szdd.qianxun.message.msg_tool.ParamTool;
import com.szdd.qianxun.start.register.Register3;
import com.szdd.qianxun.start.tool.SafeCheck;
import com.szdd.qianxun.tools.bitmap.BitmapTool;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectEasy;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.top.TActivity;

import java.io.File;
import java.util.Calendar;

//这边主要是修改网络部分。记得传输message时，处理INFO_EMPTY
public class AlterAllInfo extends TActivity implements OnClickListener {
    public static final String EXTEND_INFO_KEY = "extend_info_key";
    public static final String INFO_EMPTY = "　";// 圆角空格
    public static final String FLAG_INFO = MSG_FLAG.MSG_HIDE_HEAD + "INFO";
    public static final int CODE_ID_START = 4, CODE_STU_START = 5;
    private final String[] context_items = new String[]{"相机拍照", "相册选取"};
    private static final int CODE_ALBUM_START = 1, CODE_CAMERA_START = 2,
            CODE_CROP_START = 3;
    private AlertDialog method_dialog;
    private String icon_path = "", final_icon_path = "";
    private int year = 1, mon = 1, day = 1;
    private String name = "", gender = "", birthday = "", location = "";
    private ImageButton ibtn_head;
    private EditText et_name, et_birth, et_location;
    private SelectableRoundedImageView round_head;
    private RadioGroup radiogroup;// 以后可以删去
    private RadioButton rb_man, rb_woman;
    private Button btn_birth, btn_credit1, btn_credit2, btn_sure;
    private EditText et_sign, et_hobby, et_work;
    private TextView tv_school;
    private String sign = "", hobby = "", work = "", school = "";
    private int credit_state = 0;
    private final String[] hobby_items = {"音乐", "电影", "动漫", "游戏", "旅游", "美食"};
    private boolean[] hobby_select = new boolean[hobby_items.length];
    private AlertDialog.Builder builder = null;
    private Uri uritempFile = null;
    private static final int ALERT_SCHOOL = 6;

    @Override
    public void onCreate() {
        setContentView(R.layout.msg_alter_all_info);
        setTitle("修改资料");
        showBackButton();
        initActionBar(getResources().getColor(R.color.topbar_bg));

        initMessage();
        initView();
        initDialog();
        initHobbyDialog();
        loadBaseInfo();
        loadExtendInfo();
    }

    private void initMessage() {
        Intent intent = getIntent();
        if (intent != null) {
            // 基本信息不必传输过来，因为只能修改自己的信息
            // INFO_EMPTY就不必传输过来了
            sign = intent.getStringExtra("sign");
            hobby = intent.getStringExtra("hobby");
            work = intent.getStringExtra("work");
            school = intent.getStringExtra("school");
            credit_state = intent.getIntExtra("credit_state", 0);
            sign = sign == null ? "" : sign;
            hobby = hobby == null ? "" : hobby;
            work = work == null ? "" : work;
            school = school == null ? "" : school;
        } else {
        }
        initHobbySelect();
    }

    private void initHobbySelect() {
        if (hobby.equals("")) {
            for (int i = 0; i < hobby_select.length; i++) {
                hobby_select[i] = false;
            }
        } else {
            for (int i = 0; i < hobby_select.length; i++) {
                // 我真是越来越佩服我自己了
                if (hobby.contains(hobby_items[i]))
                    hobby_select[i] = true;
                else
                    hobby_select[i] = false;
            }
        }
    }

    private void initDialog() {
        final_icon_path = Register3.getIconPathOnly();
        uritempFile = Register3.getTempUri();
        icon_path = final_icon_path + ".jpg";
        method_dialog = new AlertDialog.Builder(this).setTitle("请选择方式	")
                .setItems(context_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent2.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
                                intent2.putExtra("outputFormat", "JPEG");//返回格式
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

    private void initView() {
        ibtn_head = (ImageButton) findViewById(R.id.all_info_ibtn_head);
        ibtn_head.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.all_info_et_name);
        et_birth = (EditText) findViewById(R.id.all_info_et_birthday);
        et_location = (EditText) findViewById(R.id.all_info_et_location);

        round_head = (SelectableRoundedImageView) findViewById(R.id.all_info_round_head);
        radiogroup = (RadioGroup) findViewById(R.id.all_info_group_sex);
        rb_man = (RadioButton) findViewById(R.id.all_info_radio_man);
        rb_woman = (RadioButton) findViewById(R.id.all_info_radio_woman);
        et_birth.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    showDateDialog();
                return false;
            }
        });

        btn_birth = (Button) findViewById(R.id.all_info_btn_birthday);
        btn_credit1 = (Button) findViewById(R.id.all_info_btn_credit1);
        btn_credit2 = (Button) findViewById(R.id.all_info_btn_credit2);
        btn_sure = (Button) findViewById(R.id.all_info_btn_alter);
        btn_birth.setOnClickListener(this);
        btn_credit1.setOnClickListener(this);
        btn_credit2.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        // ////////测试//////////
        if (ParamTool.isTest())
            registerForContextMenu(btn_credit2);

        et_sign = (EditText) findViewById(R.id.all_info_et_sign);
        et_hobby = (EditText) findViewById(R.id.all_info_et_hobby);
        et_work = (EditText) findViewById(R.id.all_info_et_work);
        tv_school = (TextView) findViewById(R.id.all_info_school);
        tv_school.setOnClickListener(this);
        et_hobby.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    showHobbyDialog();
                return false;
            }
        });
    }

    private void loadBaseInfo() {
        AnBaseInfo base_info = InfoTool.getBaseInfo(this);
        if (base_info.getUserId().equals(""))
            return;
        round_head.setImageBitmap(base_info.getHeadIcon());
//		ibtn_head.setImageResource(R.drawable.shape_null);
        name = base_info.getNickName();
        et_name.setText(name);
        gender = base_info.getGender();
        boolean is_man = true;
        if ("女".equals(gender))
            is_man = false;
        rb_man.setChecked(is_man);
        rb_woman.setChecked(!is_man);
        birthday = base_info.getBirthday();
        try {
            String[] bir_temp = birthday.split("-");
            year = Integer.parseInt(bir_temp[0]);
            mon = Integer.parseInt(bir_temp[1]);
            day = Integer.parseInt(bir_temp[2]);
        } catch (Exception e) {
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
        et_birth.setText(birthday);
        location = base_info.getLocation();
        et_location.setText(location);
    }

    private void loadExtendInfo() {
        et_sign.setText(sign);
        et_hobby.setText(hobby);
        et_work.setText(work);
        tv_school.setText(school);
        // 只能认证一次
        switch (credit_state) {
            case 0:// 未认证
                btn_credit2.setEnabled(false);
                if (ParamTool.isTest())
                    btn_credit2.setEnabled(true);// ////////////////////////测试阶段暂时允许
                break;
            case 1:// 已通过实名认证
                btn_credit1.setText("已通过实名认证");
                btn_credit1.setEnabled(false);
                break;
            case 2:// 已通过学生认证
                btn_credit1.setText("已通过实名认证");
                btn_credit1.setEnabled(false);
                btn_credit2.setText("已通过学生认证");
                btn_credit2.setEnabled(false);
                break;
            case -1:// 认证等待中
            case -2:
                btn_credit1.setText("认证等待中");
                btn_credit1.setEnabled(false);
                btn_credit2.setVisibility(View.GONE);
                break;
            default:// 可能遇到了错误
                btn_credit1.setVisibility(View.GONE);
                btn_credit2.setVisibility(View.GONE);
                break;
        }
        // ////////////////////////测试阶段暂时允许
        if (ParamTool.isTest()) {
            if (btn_credit1.getVisibility() == View.GONE)
                btn_credit1.setText(btn_credit1.getText() + "-hide");
            if (btn_credit2.getVisibility() == View.GONE)
                btn_credit2.setText(btn_credit2.getText() + "-hide");
            btn_credit1.setVisibility(View.VISIBLE);
            btn_credit2.setVisibility(View.VISIBLE);
            btn_credit1.setEnabled(true);
            btn_credit2.setEnabled(true);
        }
    }

    private void updateAllInfo() {
        if (final_icon_path == null) {
            showToast("未找到存储卡");
            return;
        }
        // 必须的非空部分
        if (rb_man.isChecked())
            gender = "男";
        else
            gender = "女";
        if (new File(icon_path).exists()) {// 存在，说明修改了头像
            final_icon_path = icon_path;
            if (!new File(final_icon_path).exists()) {
                showToast("未找到存储卡");
                return;
            }
        } else {// 否则，没改头像，不必处理
        }
        name = et_name.getText().toString();
        if (!SafeCheck.checkNickName(this, name))// 后台要有数据转义，防止风险
            return;
        // 这些是允许为空的
        sign = et_sign.getText().toString();
        hobby = et_hobby.getText().toString();
        work = et_work.getText().toString();
        school = tv_school.getText().toString();
        sign = sign.equals("") ? INFO_EMPTY : sign;
        hobby = hobby.equals("") ? INFO_EMPTY : hobby;
        work = work.equals("") ? INFO_EMPTY : work;
        school = school.equals("") ? INFO_EMPTY : school;
        // 先上传拓展部分
        uploadBaseInfo();
    }

    private void uploadBaseInfo() {
        ConnectEasy.POST(this, ServerURL.ALTER_INFO, new ConnectListener() {
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            public ConnectList setParam(ConnectList list) {
                list.put("user.username", InfoTool.getUserName(AlterAllInfo.this));
                list.put("user.id", InfoTool.getUserID(AlterAllInfo.this));
                list.put("user.nickName", name);
                list.put("user.gender", gender);
                list.put("user.birthday", birthday);
                list.put("user.address", location);
                if (!new File(icon_path).exists())
                    list.putFile("user.iconfile", null);
                else
                    list.putFile("user.iconfile", new File(final_icon_path));
                list.put("user.sign", sign);
                list.put("user.hobby", hobby);
                list.put("user.job", work);
                list.put("user.school", school);
                return list;
            }

            public void onResponse(String response) {
                AppUtil.dismissProgressDialog();
                if (response == null || response.equals("") ||
                        response.equals("-2") || response.equals("fail")) {
                    showToast("网络错误");
                } else if (response.equals("1")) {
                    // 保存本地记录
                    AnBaseInfo info = new AnBaseInfo(InfoTool
                            .getUserID(AlterAllInfo.this), name, gender,
                            birthday, location, final_icon_path);
                    InfoTool.saveBaseInfo(AlterAllInfo.this, info);
                    showToast("修改成功");
                    AlterAllInfo.this.setResult(1);
                    finish();
                } else {
                    showToast("系统错误");
                }
            }
        });
    }

    // 调用系统裁剪
    public void cropPhoto(Uri uri) {
        Register3.cropPhoto(uri, uritempFile, this, CODE_CROP_START);
    }

    // 保存图片
    private void saveBitmap(Bitmap mBitmap) {
        if (final_icon_path == null) {
            showToast("未找到存储卡");
            return;
        }
        mBitmap = BitmapTool.zipBitmap(mBitmap, 300, 300, true);// 压缩图片使之小于等于300*300
        mBitmap = BitmapTool.getCroppedRoundBitmap(mBitmap);// 处理为圆角
        BitmapTool.writeToFile(mBitmap, "png", icon_path);
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

    private void initHobbyDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择爱好");
        DialogInterface.OnMultiChoiceClickListener mutiListener = new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialogInterface, int which,
                                boolean isChecked) {
                hobby_select[which] = isChecked;
                if (!isChecked)
                    return;
                int select_count = 0;
                for (int i = 0; i < hobby_select.length; i++) {
                    if (hobby_select[i])
                        select_count++;
                }
                if (select_count > 4) {
                    showToast("最多选择4项");
                    hobby_select[which] = false;
                    dialogInterface.cancel();
                    builder.create().show();
                }
            }
        };
        DialogInterface.OnClickListener btnListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int which) {
                hobby = "";
                for (int i = 0; i < hobby_select.length; i++) {
                    if (hobby_select[i])
                        hobby += "，" + hobby_items[i];
                }
                if (!hobby.equals(""))
                    hobby = hobby.substring(1);
                et_hobby.setText(hobby);
            }
        };
        builder.setPositiveButton("确定", btnListener);
        builder.setMultiChoiceItems(hobby_items, hobby_select, mutiListener);
    }

    private void showHobbyDialog() {
        // hobby_select就不必再次处理了
        builder.create().show();
    }

    private void realNameCheck() {
        Intent intent = new Intent(this, RealNameCheck.class);
        startActivityForResult(intent, CODE_ID_START);
    }

    private void realStudentCheck() {
        if (credit_state == 0) {
            showToast("请先通过实名认证");
            return;
        }
        Intent intent = new Intent(this, RealStudentCheck.class);
        startActivityForResult(intent, CODE_STU_START);
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
                //将Uri图片转换为Bitmap
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(
                            getContentResolver().openInputStream(uritempFile));
                    if (bitmap != null) {
                        saveBitmap(bitmap);// 保存在SD卡中，并同步到用户信息
                        round_head.setImageBitmap(bitmap);// 用ImageView显示出来
//                        ibtn_head.setImageResource(R.drawable.shape_null);
                    }
                } catch (Exception e) {
                }
                break;
            case CODE_ID_START:// 认证状态刷新
            case CODE_STU_START:
                if (resultCode == CODE_ID_START || resultCode == CODE_STU_START) {
                    btn_credit1.setText("认证等待中");
                    btn_credit1.setEnabled(false);
                    btn_credit2.setVisibility(View.GONE);
                }
                break;
            case ALERT_SCHOOL:
                if (resultCode == RESULT_OK) {
                    tv_school.setText(data.getStringExtra("school"));
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_info_ibtn_head:
                method_dialog.show();
                break;
            case R.id.all_info_btn_birthday:
                showDateDialog();
                break;
            case R.id.all_info_btn_credit1:
                realNameCheck();
                break;
            case R.id.all_info_btn_credit2:
                realStudentCheck();
                break;
            case R.id.all_info_btn_alter:
                updateAllInfo();
                break;
            case R.id.all_info_school:
                Intent intent = new Intent(this, SchoolInfo.class);
                startActivityForResult(intent, ALERT_SCHOOL);
                break;
        }
    }

    @Override
    public void showContextMenu() {
    }

    // ////////测试专用：长按响应//////////
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        if (ServerURL.isTest() && v == btn_credit2) {
            showToast("测试模式");
            credit_state = 1;
            realStudentCheck();
            credit_state = 0;
        }
    }
}
