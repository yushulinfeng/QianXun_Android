package com.szdd.qianxun.message.friend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.top.NetTActivity;

import java.util.ArrayList;

//建议使用两次加载，防止图片缓慢导致卡顿
//不必考虑下拉刷新
//存储模式：id+用户名+关注状态
//使用立即更新机制
public class FriendList extends NetTActivity {
    private ArrayList<AnFriendItem> array;
    private FriendListAdapter adapter;
    private ListView list;
    private TextView text;
    private boolean is_collected = false;//是否为收藏模式

    @Override
    public void onCreate() {
        setContentView(R.layout.msg_friend_list);
        is_collected = getIntent().getBooleanExtra("is_collected", false);
        setTitle("联系人");
        if (is_collected)
            setTitle("关注的人");
        showBackButton();
        //showMenuButton();
        initActionBar(getResources().getColor(R.color.topbar_bg));

        initView();
        initData();
    }

    private void initView() {
        text = (TextView) findViewById(R.id.msg_friend_list_text);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        text.setText("暂无好友");
        list = (ListView) findViewById(R.id.msg_friend_list_list);

        array = FriendListTool.getFriendList(this).getArray();

        adapter = new FriendListAdapter(this, array, is_collected);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {// 只添加点击即可
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                clickListItem(position);
            }
        });
        list.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                longClickListItem(position);
                return true;// 终止传递
            }
        });
    }

    private void initData() {
        FriendListTool.getCollectFromServer(this, new FriendListener() {
            @Override
            public void onResponse(FriendItems friendItems) {
                array = FriendListTool.combineFriendList(array, friendItems.getArray());
                updateList();
            }
        });
    }

    private void clickListItem(int position) {
        MsgPublicTool.chartToNotSave(this, array.get(position).getUserID(),
                array.get(position).getUserName());
    }

    private void longClickListItem(final int position) {
        String[] item_array = new String[]{"发起会话", "查看主页", "修改备注", "关注", "删除"};
        if (array.get(position).getState())
            item_array[3] = "取消关注";
        new AlertDialog.Builder(this).setTitle("请选择操作")
                .setItems(item_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                clickListItem(position);
                                break;
                            case 1:
                                seeUserInfo(position);
                                break;
                            case 2:
                                alterItem(position);
                                break;
                            case 3:
                                collectItem(position);
                                break;
                            case 4:
                                deleteItem(position);
                                break;
                        }
                    }
                }).show();
    }

    private void seeUserInfo(int position) {
        MsgPublicTool.showHomePage(this, array.get(position).getUserID());
    }

    private void alterItem(final int position) {
        View dialogView = LayoutInflater.from(this).inflate(
                R.layout.dialog_edit, null);
        final EditText editText = (EditText) dialogView
                .findViewById(R.id.edt_conversation_title);
        editText.setSingleLine(true);
        editText.setHint("请输入备注");
        String myTag = array.get(position).getMyTag();
        if (myTag != null) {
            editText.setText(myTag);
            editText.setSelection(myTag.length());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("修改昵称");
        builder.setView(dialogView);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String new_tag = editText.getText().toString().trim();
                if (new_tag.equals("")) {
                    new_tag = null;// 为空就用空的tag
                    showToast("已取消备注。");
                }
                array.get(position).setMyTag(new_tag);
                updateList();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private void collectItem(int position) {
        AnFriendItem friend_temp = array.get(position);
        if (friend_temp.getState())
            friend_temp.setState(false);
        else
            friend_temp.setState(true);
        array.remove(position);
        if (friend_temp.getState()) {
            array.add(0, friend_temp);// 开头
            sendAttention(0);
        } else {
            array.add(friend_temp);// 结尾
            cancelAttention(array.size() - 1);
        }
        updateList();
    }

    private void deleteItem(int position) {
        if (array.get(position).getState()) {//如果关注了就取消关注，再删除
            cancelAttention(position);
        }
        array.remove(position);
        updateList();
    }

    private void updateList() {
        adapter.notifyDataSetChanged();
        if (array.size() == 0) {
            text.setText("暂无好友");
        } else {
            text.setText("");
        }
    }

    @Override
    public void receiveMessage(String what) {
    }

    @Override
    public void newThread() {
    }

    private void readAddressBook() {
        Intent intent = new Intent(this, FriendListBook.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void showContextMenu() {
        if (FriendListTool.isFirstRun(this)) {
            String content = "是否允许高能访问您的联系人列表，便于您查看其中的高能用户？"
                    + "\n我们将只进行特征值匹配，不会上传您的联系人信息。";
            new AlertDialog.Builder(this)
                    .setTitle("匹配联系人？")
                    .setMessage(content)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    FriendListTool
                                            .writeNotFirstRun(FriendList.this);
                                    readAddressBook();
                                }
                            }).setNegativeButton("取消", null).create().show();
        } else {
            readAddressBook();
        }
    }

    @Override
    protected void onDestroy() {
        FriendListTool.saveFriendList(this, new FriendItems(array));
        super.onDestroy();
    }

    public void sendAttention(final int position) {
        FriendListTool.addAttention(this, array.get(position).getUserID());
    }

    public void cancelAttention(final int position) {
        FriendListTool.delAttention(this, array.get(position).getUserID());
    }

}



