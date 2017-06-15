package com.szdd.qianxun.request;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;

public class Activity_Task_doing extends Activity implements OnClickListener {
    private ImageView img_back;
    private ImageView img_headIcon;
    private ImageView img_phone;// 电话
    private Button img_hurry;// 催一下
    private Button img_communicate;
    private TextView tv_receiver;
    private Bundle bundle;
    private String nickname;// 接单者昵称
    private String requestId;// 需求单号
    private String phoneNumber;// 既是手机号，又是用户id（唯一标识）
    private String user_gender;
    private String url_headIcon;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_doing);
        init();
        getHeadIcon(Activity_Task_doing.this, url_headIcon);
    }

    public void init() {
        bundle = getIntent().getExtras();
        userId = bundle.getString("userId");
        nickname = bundle.getString("nickname");
        requestId = bundle.getString("userRequestId");
        phoneNumber = bundle.getString("phonenumber");
        user_gender = bundle.getString("gender");
        url_headIcon = bundle.getString("headIcon");
        img_back = (ImageView) this.findViewById(R.id.imageview_back);
        img_phone = (ImageView) this.findViewById(R.id.img_phone);
        img_hurry = (Button) this.findViewById(R.id.img_hurry);
        img_headIcon = (ImageView) this.findViewById(R.id.ad_picture_user_head);
        img_communicate = (Button) this.findViewById(R.id.img_communication1);
        tv_receiver = (TextView) this.findViewById(R.id.ad_picture_user_name);
        tv_receiver.setText(nickname);
        img_back.setOnClickListener(this);
        img_phone.setOnClickListener(this);
        img_hurry.setOnClickListener(this);
        img_communicate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_back:// 返回
                finish();
                break;
            case R.id.img_hurry:// 催一下
                StaticMethod.POST(Activity_Task_doing.this, ServerURL.REQUEST_HURRY, new ConnectListener() {
                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("requestId", requestId);
                        list.put("userId", InfoTool.getUserID(Activity_Task_doing.this) + "");
                        return list;
                    }

                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        dialog.config(Activity_Task_doing.this, "操作中", "玩命加载中...", false);
                        return dialog;
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            QianxunToast.makeText(Activity_Task_doing.this, "网络错误", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                int status = Integer.parseInt(response);
                                switch (status) {
                                    case 1:
                                        QianxunToast.makeText(Activity_Task_doing.this, "操作成功", Toast.LENGTH_SHORT).show();
                                        break;
                                    case -2:
                                        QianxunToast.makeText(Activity_Task_doing.this, "服务器出错", Toast.LENGTH_SHORT).show();
                                        break;
                                    case -3:
                                        QianxunToast.makeText(Activity_Task_doing.this, "不是此人的需求", Toast.LENGTH_SHORT).show();
                                        break;
                                    case -4:
                                        QianxunToast.makeText(Activity_Task_doing.this, "不存在此需求", Toast.LENGTH_SHORT).show();
                                        break;
                                    case -6:
                                        QianxunToast.makeText(Activity_Task_doing.this, "未登录", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                });
                break;
            case R.id.img_phone:// 拨打电话
                StaticMethod.callTo(this,phoneNumber);// 用户名就是电话号码
//                try {
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:" + phoneNumber));// 用户名就是电话号码
//                    startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
            case R.id.img_communication1:// 与他联系（聊天界面）
                MsgPublicTool.chartTo(Activity_Task_doing.this, Integer.parseInt(userId), Long.parseLong(phoneNumber));// 转换成Long类型
                break;
            default:
                break;
        }
    }

    private void getHeadIcon(final Context context, final String url) {
        StaticMethod.BITMAPHEAD(Activity_Task_doing.this, url, img_headIcon);
    }
}
