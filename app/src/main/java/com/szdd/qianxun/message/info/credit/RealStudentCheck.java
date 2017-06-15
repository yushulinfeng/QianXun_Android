package com.szdd.qianxun.message.info.credit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.info.AlterAllInfo;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.start.register.Register3;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.bitmap.BitmapTool;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.top.TActivity;

import java.io.File;

public class RealStudentCheck extends TActivity implements OnClickListener {
    private final String[] context_items = new String[]{"相机拍照", "相册选取"};
    private final String[] state_items = new String[]{"专科", "本科", "硕士", "博士"};
    private static final int CODE_ALBUM_START = 1;
    private static final int CODE_CAMERA_START = 2;
    private String icon_path = "";
    private AlertDialog method_dialog;
    private ImageButton ibtn_head;
    private ImageView round_head;
    private EditText et_name, et_code;
    private EditText et_state, et_time, et_school, et_subschool, et_major;
    private Button btn_submit;
    private String name = "", code = "";
    private String time = "", state = "", school = "", subschool = "",
            major = "";
    private boolean is_add_head = false;
    private Uri uritempFile = null;

    @Override
    public void onCreate() {
        setContentView(R.layout.msg_real_stu_check);
        setTitle("学生认证");
        setTopColors(R.color.btn_bg_orange_normal);
        showBackButton();
        initActionBar(getResources().getColor(R.color.topbar_bg));

        initView();
        intiDialog();
    }

    private void initView() {
        ibtn_head = (ImageButton) findViewById(R.id.stu_check_ibtn_head);
        round_head = (ImageView) findViewById(R.id.stu_check_round_head);
        et_name = (EditText) findViewById(R.id.stu_check_et_name);
        et_code = (EditText) findViewById(R.id.stu_check_et_code);
        et_state = (EditText) findViewById(R.id.stu_check_et_state);
        et_time = (EditText) findViewById(R.id.stu_check_et_time);
        et_school = (EditText) findViewById(R.id.stu_check_et_school);
        et_subschool = (EditText) findViewById(R.id.stu_check_et_subschool);
        et_major = (EditText) findViewById(R.id.stu_check_et_major);
        btn_submit = (Button) findViewById(R.id.stu_check_btn_submit);
        ibtn_head.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        et_state.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    showStateDialog();
                return false;
            }
        });
        et_time.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    showDateDialog();
                return false;
            }
        });
    }

    private void intiDialog() {
        icon_path = Register3.getMessageSDPath(RealNameCheck.CHECK_SD_PATH,
                RealNameCheck.CHECK_STU_SD_NAME, true).getAbsolutePath();
        uritempFile = Register3.getTempUri();
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
                                Intent intent1 = new Intent(Intent.ACTION_PICK,
                                        null);
                                intent1.setDataAndType(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        "image/*");
                                startActivityForResult(intent1, CODE_ALBUM_START);
                                break;
                        }
                    }
                }).create();
    }

    private void showStateDialog() {
        new AlertDialog.Builder(this).setTitle("学历程度")
                .setItems(state_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        state = state_items[which];
                        et_state.setText(state);
                    }
                }).show();
    }

    private void showDateDialog() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker dp, int newyear, int month,
                                  int dayOfMonth) {
                // 这里说一句，DatePickerDialog很特殊，点击对话框外，确定方法执行一遍，
                // 点击完成，确定方法执行两遍。（本人阻碍了一下午，最后debug才知道是对话框的问题）
                time = newyear + "";
                et_time.setText(time + "年");
            }
        }, 2016, 8, 1).show();
    }

    // 保存图片
    private void dealBitmap(Uri uri) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(
                    getContentResolver().openInputStream(uri));
            if (bitmap != null) {
                round_head.setImageBitmap(bitmap);// 用ImageView显示出来
                ibtn_head.setImageResource(R.drawable.shape_null);
                BitmapTool.writeToFile(bitmap, "jpg", icon_path);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stu_check_ibtn_head:
                method_dialog.show();
                break;
            case R.id.stu_check_btn_submit:
                sendToServer();
                break;
        }
    }

    private void sendToServer() {
        name = et_name.getText().toString();
        code = et_code.getText().toString();
        if (!is_add_head) {
            showToast("请添加图片");
            return;
        }
        if (name.trim().equals("")) {
            showToast("请输入有效姓名");
            return;
        }
        if (code.length() < 4) {
            showToast("请输入有效身份证号");
            return;
        }
        school = et_school.getText().toString();
        subschool = et_subschool.getText().toString();
        major = et_major.getText().toString();
        if (state.equals("") || time.equals("")) {
            showToast("请完善相关信息");
            return;
        }

        // 上传数据到服务器
        btn_submit.setEnabled(false);
        StaticMethod.POST(this, ServerURL.CREDIT_SDU, new ConnectListener() {
            public ConnectDialog showDialog(ConnectDialog dialog) {
                dialog.config(RealStudentCheck.this, "数据上传中", "请稍候……", false);
                return dialog;
            }

            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("studentVerify.phone",
                        InfoTool.getUserName(RealStudentCheck.this));
                list.put("studentVerify.realName", name);
                list.put("studentVerify.stuID", code);
                list.put("studentVerify.admissionTime", time);
                list.put("studentVerify.degree", state);
                list.put("studentVerify.school", school);
                list.put("studentVerify.college", subschool);
                list.put("studentVerify.major", major);
                list.put("studentVerify.stuIDImage", "");//icon_base64
                list.put("studentVerify.imageFile", new File(icon_path));
                return list;
            }

            @Override
            public void onResponse(String response) {
                btn_submit.setEnabled(true);
                Log.e("EEEEEEEEEE",response+"");
                if (response == null || response.equals("")
                        || response.equals("fail") || response.equals("-2")) {
                    showToast("网络错误");
                } else if (response.equals("1")) {
                    showToast("上传成功，请等待审核");
                    RealStudentCheck.this
                            .setResult(AlterAllInfo.CODE_STU_START);
                    finish();
                } else {
                    showToast("系统错误");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_ALBUM_START:
                if (resultCode == RESULT_OK) {
                    is_add_head = true;
                    Uri selectedImage = data.getData();
                    dealBitmap(selectedImage);
                }
                break;
            case CODE_CAMERA_START:
                if (resultCode == RESULT_OK) {
                    is_add_head = true;
                    dealBitmap(uritempFile);// 处理图片
                }
                break;
        }
    }

    @Override
    public void showContextMenu() {
    }

}
