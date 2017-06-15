package com.szdd.qianxun.message.info.credit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.info.AlterAllInfo;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.start.register.Register3;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.bitmap.BitmapTool;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.top.TActivity;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RealNameCheck extends TActivity implements OnClickListener {
    public static final String CHECK_SD_PATH = "check";
    public static final String CHECK_ID_SD_NAME = "real_name_check.jpg";
    public static final String CHECK_STU_SD_NAME = "real_name_check.jpg";
    private final String[] context_items = new String[]{"相机拍照", "相册选取"};
    private static final int CODE_ALBUM_START = 1;
    private static final int CODE_CAMERA_START = 2;
    private String icon_path = "";
    private AlertDialog method_dialog;
    private ImageButton ibtn_head;
    private ImageView round_head;
    private EditText et_name, et_code;
    private Button btn_submit;
    private String name = "", code = "";
    private boolean is_add_head = false;
    private Uri uritempFile = null;

    @Override
    public void onCreate() {
        setContentView(R.layout.msg_real_name_check);
        setTitle("实名认证");
        setTopColors(R.color.btn_bg_orange_normal);
        showBackButton();
        initActionBar(getResources().getColor(R.color.topbar_bg));
        is_add_head = false;

        initView();
        intiDialog();
    }

    private void initView() {
        ibtn_head = (ImageButton) findViewById(R.id.name_check_ibtn_head);
        round_head = (ImageView) findViewById(R.id.name_check_round_head);
        et_name = (EditText) findViewById(R.id.name_check_et_name);
        et_code = (EditText) findViewById(R.id.name_check_et_code);
        btn_submit = (Button) findViewById(R.id.name_check_btn_submit);
        ibtn_head.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    private void intiDialog() {
        icon_path = Register3.getMessageSDPath(CHECK_SD_PATH, CHECK_ID_SD_NAME,
                true).getAbsolutePath();
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
            case R.id.name_check_ibtn_head:
                method_dialog.show();
                break;
            case R.id.name_check_btn_submit:
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
        Pattern pat = Pattern.compile("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)");
        code = code.toUpperCase();
        Matcher mac = pat.matcher(code);
        if (!mac.find()) {
            showToast("请输入有效身份证号");
            return;
        }

        // 上传数据到服务器
        btn_submit.setEnabled(false);
        StaticMethod.POST(this, ServerURL.CREDIT_ID, new ConnectListener() {
            public ConnectDialog showDialog(ConnectDialog dialog) {
                dialog.config(RealNameCheck.this, "数据上传中", "请稍候……", false);
                return dialog;
            }

            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userVerify.phone", InfoTool.getUserName(RealNameCheck.this));
                list.put("userVerify.realName", name);
                list.put("userVerify.IDNumber", code);
                list.put("userVerify.IDImage", "");//icon_base64
                list.put("userVerify.imageFile", new File(icon_path));
                return list;
            }

            @Override
            public void onResponse(String response) {
                btn_submit.setEnabled(true);
                if (response == null || response.equals("")
                        || response.equals("fail") || response.equals("-2")) {
                    showToast("网络错误");
                } else if (response.equals("-4")) {
                    UserStateTool.goToLogin(RealNameCheck.this);
                } else if (response.equals("-3")) {
                    showToast("您的信息正在审核中");
                } else if (response.equals("1")) {
                    showToast("上传成功，请等待审核");
                    RealNameCheck.this.setResult(AlterAllInfo.CODE_ID_START);
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
