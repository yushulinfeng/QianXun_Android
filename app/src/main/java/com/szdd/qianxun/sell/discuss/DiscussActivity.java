package com.szdd.qianxun.sell.discuss;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.baichuan.mine.BaiChuanUtils;
import com.szdd.qianxun.message.baichuan.util.AndTools;
import com.szdd.qianxun.message.info.AnUserInfo;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.ManagerTool;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.top.TActivity;
import com.szdd.qianxun.tools.views.xlist.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscussActivity extends TActivity {
    //discuss_author如果是自己，可以删除回复
    private String discuss_author = "", discuss_id = "",
            discuss_title = "", discuss_text = "";
    private List<AnDiscussReply> list_items = null;
    private DiscussAdapter adapter = null;

    @Bind(R.id.sell_discuss_list)
    XListView sellDiscussList;
    @Bind(R.id.sell_discuss_edit)
    EditText sellDiscussEdit;
    @Bind(R.id.sell_discuss_btn)
    Button sellDiscussBtn;

    @Override
    public void onCreate() {
        setContentView(R.layout.sell_discuss_main);
        ButterKnife.bind(this);
        showBackButton();
        setTitle("评论");

        initMessage();
        initView();
        loadReply();
    }

    private void initMessage() {
        Intent intent = getIntent();
        if (intent != null) {
            discuss_author = intent.getStringExtra("discuss_author");
            discuss_id = intent.getStringExtra("discuss_id");
            discuss_title = intent.getStringExtra("discuss_title");
            discuss_text = intent.getStringExtra("discuss_text");
        }
    }

    private void initView() {
        list_items = new ArrayList<>();
        adapter = new DiscussAdapter(this, discuss_title, discuss_text, list_items);

        sellDiscussList.setPullRefreshEnable(false);
        sellDiscussList.setPullLoadEnable(false);
        sellDiscussList.setSpringOnly();
        sellDiscussList.setAdapter(adapter);
        sellDiscussList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == 1) //评论项目禁止处理
                    return;
                if (!ManagerTool.isManagerLogin())//管理员除外，允许9890管理员账号
                    if (!UserStateTool.isLoginNow(DiscussActivity.this))
                        return;
                final int position_real = position - 2;
                AnDiscussReply reply = list_items.get(position_real);
                final String reply_text = reply.getReplyText();
                final String reply_id = reply.getReplyId();
                if (ServerURL.isTest()) {
                    Log.e("EEEEEE-id1", InfoTool.getUserId());
                    Log.e("EEEEEE-id2", reply.getUserId() + "");
                }
                //自己的回复，或者自己发的技能，或者管理员，可以删除回复
                if (InfoTool.getUserId().equals(reply.getUserId() + "")
                        || InfoTool.getUserId().equals(discuss_author)
                        || ManagerTool.isManagerLogin()) {
                    new AlertDialog.Builder(DiscussActivity.this)
                            .setTitle("删除回复？")
                            .setMessage("将会删除此条回复：\n" + reply_text)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteReply(reply_id, position_real);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            }
        });
    }


    private void loadReply() {
        DiscussTool.getInstance().getReply(discuss_id, new DiscussTool.ReplyGetListener() {
            @Override
            public void OnResponse(ArrayList<AnDiscussReply> list) {
                //保存list可便于管理员删除
                if (list == null || list.size() == 0) {
                    return;
                }
                list_items.clear();
                for (AnDiscussReply reply : list)
                    list_items.add(0, reply);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.sell_discuss_btn)
    public void onClick() {
        if (!UserStateTool.isLoginNow(this)) {
            UserStateTool.goToLogin(this);
            return;
        }
        String text = sellDiscussEdit.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            AndTools.showToast("请输入内容");
            return;
        }
        if (discuss_id != null && discuss_id.startsWith("temp_")) {//测试回复
            AndTools.showToast("回复成功");
            AnUserInfo info = InfoTool.getUserInfo(this);
            AnDiscussReply reply = new AnDiscussReply.Builder(info.getNickName(), text)
                    .replyId("temp_" + System.currentTimeMillis())
                    .userHead(ServerURL.getIP() + info.getHeadIcon())
                    .userId(BaiChuanUtils.getUserName(info.getId()))
                    .replyTime(StaticMethod.formatDateTime("yyyy-MM-dd hh:mm:ss"))
                    .build();
            list_items.add(reply);
            adapter.notifyDataSetChanged();
            sellDiscussEdit.setText("");
            return;
        }
        DiscussTool.getInstance().replayDiscuss(this, discuss_id, text, new DiscussTool.ReplyPostListener() {
            @Override
            public void OnResponse(String replyId, AnDiscussReply reply) {
                if (replyId == null) {
                    AndTools.showToast("回复失败");
                    return;
                }
                AndTools.showToast("回复成功");
                list_items.add(reply);
                adapter.notifyDataSetChanged();
                sellDiscussEdit.setText("");
            }
        });
    }

    private void deleteReply(String replyId, final int position) {
        if (discuss_id != null && discuss_id.startsWith("temp_")) {//测试回复
            list_items.remove(position);
            adapter.notifyDataSetChanged();
            showToast("删除成功");
            return;
        }
        DiscussTool.getInstance().deleteReply(this, discuss_id, replyId,
                new DiscussTool.ReplyDelListener() {
                    @Override
                    public void OnResponse(boolean state) {
                        if (state) {
                            list_items.remove(position);
                            adapter.notifyDataSetChanged();
                            showToast("删除成功");
                        } else {
                            showToast("删除失败");
                        }
                    }
                });
    }

    @Override
    public void showContextMenu() {
    }
}
