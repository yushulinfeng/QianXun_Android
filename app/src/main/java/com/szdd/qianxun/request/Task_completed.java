package com.szdd.qianxun.request;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.bitmap.BitmapListener;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.RoundHeadView;

import org.json.JSONObject;

public class Task_completed extends Activity implements OnClickListener {
    private Button btn_yes, btn_no;
    private ImageView img_headIcon;
    private ImageView img_back;
    private TextView tv_content;
    private TextView tv_nickname;
    private final int NOT_FINISHED = 0;// 否
    private final int IS_FINISHED = 1;// 是
    private String requestId = null;
    private String userId = null;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_completed);
        try {
            init();
            initContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        bundle = getIntent().getExtras();
        requestId = bundle.getString("userRequestId");
        userId = bundle.getString("userId");
        img_headIcon = (ImageView) this.findViewById(R.id.ad_picture_user_head);
        btn_yes = (Button) this.findViewById(R.id.yes_complete);
        btn_no = (Button) this.findViewById(R.id.no_complete);
        tv_content = (TextView) this.findViewById(R.id.completed_content);
        tv_nickname = (TextView) this.findViewById(R.id.com_nickname);
        img_back = (ImageView) this.findViewById(R.id.back_complete);
        btn_yes.setOnClickListener(this);
        btn_no.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    public void initContent() {
        tv_content.setText(bundle.getString("content"));
        tv_nickname.setText(bundle.getString("nickname"));
        StaticMethod.GET(bundle.getString("icon_url"), new BitmapListener() {
            @Override
            public void onResponse(Bitmap bitmap) {
                if (bitmap != null) {
                    img_headIcon.setImageBitmap(bitmap);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes_complete:// 是
                Yes_or_No(IS_FINISHED);
                break;
            case R.id.no_complete:// 否
                Yes_or_No(NOT_FINISHED);
                break;
            case R.id.back_complete:// 返回按钮
                finish();
                break;
            default:
                break;

        }
    }

    public void Yes_or_No(final int status) {
        StaticMethod.POST(Task_completed.this,
                ServerURL.ENSURE_IF_ISFINISHED_1, new ConnectListener() {

                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        dialog.config(Task_completed.this, "发送中...", "正在操作...", false);
                        return dialog;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        JSONObject jsonobject = new JSONObject();
                        try {
                            jsonobject.put("requestId", bundle.getString("userRequestId"));
                            jsonobject.put("status", status);
                        } catch (org.json.JSONException e1) {
                            return null;
                        }
                        list.put("jsonObj", jsonobject.toString(), false);
                        return list;
                    }

                    @Override
                    public void onResponse(String response) {
                        int c = Integer.parseInt(response);
                        switch (c) {
                            case -4:
                                showToast("需求不存在");
                                break;
                            case -3:
                                showToast("需求已完成");
                                break;
                            case -2:
                                showToast("服务器出错");
                                break;
                            case -1:
                                showToast("操作失败");
                                break;
                            case 1:
                                showToast("操作成功");
                                finish();
                                break;
                            case -5:
                                showToast("您已驳回对方请求");
                                finish();
                                break;
                            default:
                                break;
                        }

                    }
                });

    }

    /**
     * 获取用户头像
     *
     * @param url 头像链接
     */
    private void getHeadIcon(final String url) {
        StaticMethod.BITMAPHEAD(Task_completed.this, url, img_headIcon);
    }

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }
}
