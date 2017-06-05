package com.szdd.qianxun.sell.orders.buy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.views.QianxunToast;

/**
 * Created by Administrator on 2016/3/31 0031.
 */
public class BuyCommentDetailActivity extends Activity implements
        View.OnClickListener {

    private EditText buy_comment_detail_et_comment;
    private Button buy_comment_detail_btn_canclecomment, buy_comment_detail_btn_surecomment;
    private String commentdetail_url;
    private int id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_allorder_buy_comment_detail);
        Intent intent = getIntent();
        //id = intent.getIntExtra("id",0);
        buy_comment_detail_et_comment = (EditText) findViewById(R.id.buy_comment_detail_et_comment);
        buy_comment_detail_btn_canclecomment = (Button) findViewById(R.id.buy_comment_detail_btn_canclecomment);
        buy_comment_detail_btn_canclecomment.setOnClickListener(this);
        buy_comment_detail_btn_surecomment = (Button) findViewById(R.id.buy_comment_detail_btn_surecomment);
        buy_comment_detail_btn_surecomment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int Rid = view.getId();
        switch (Rid) {
            case R.id.buy_comment_detail_btn_canclecomment:
                finish();
                break;
            case R.id.buy_comment_detail_btn_surecomment:
                final String commentcontent = buy_comment_detail_et_comment.getText().toString();
                StaticMethod.POST(BuyCommentDetailActivity.this, commentdetail_url,
                        new ConnectListener() {
                            @Override
                            public ConnectDialog showDialog(ConnectDialog dialog) {

                                dialog.config(BuyCommentDetailActivity.this, "正在连接", "请稍后……", true);
                                return dialog;
                            }

                            @Override
                            public ConnectList setParam(ConnectList list) {
                                list.put("id", "" + id, false);
                                list.put("commentContent", commentcontent, false);
                                list.put("userId", InfoTool.getUserID(BuyCommentDetailActivity.this), false);
                                return list;
                            }

                            @Override
                            public void onResponse(String response) {
                                if (response == null || response.equals("")) {
                                    // 网络错误
                                    QianxunToast.showToast(BuyCommentDetailActivity.this, "网络错误,请连接网络后重新加载", QianxunToast.LENGTH_SHORT);
                                    return;
                                }
                            }
                        });
                break;
        }
    }
}
