package com.szdd.qianxun.message.homepage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;
import com.szdd.qianxun.tools.views.slidepage.ScrollableHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linorz on 2016/3/25.
 */
@SuppressLint("ValidFragment")
public class MineDynamic extends ScrollAbleFragment implements
        ScrollableHelper.ScrollableContainer, HomeListView.XListViewListener {
    private View rootView;
    private Context context;
    private HomeListView dynamicList;
    private MineDynamicAdapter dynamicAdapter;
    private List<Map<String, Object>> items;
    private String imgip = ServerURL.getIP();
    private String getDynamic = ServerURL.HOMEPAGE_DYNAMIC;
    private String birthday = "", introduce = "", sex = "", star = "", age = "", location = "";
    private int page = 1;
    private String userId;

    @SuppressLint("ValidFragment")
    public MineDynamic(String userId, String birthday,
                       String introduce, String sex, String location) {
        this.userId = userId;
        this.birthday = birthday;
        this.introduce = introduce;
        this.sex = sex;
        this.location = location;
        this.star = InfoTool.getStar(birthday);
        this.age = InfoTool.getAge(birthday);
        if (!this.age.equals("保密"))
            this.age += "岁";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent)
                parent.removeView(rootView);
        } else {
            rootView = inflater.inflate(R.layout.msg_mine_personal, container, false);
            context = rootView.getContext();
            initView(rootView);
            getPersonalDetail();
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onLoadMore() {
        getDynamic();
        dynamicList.stopLoadMore();
    }

    //初始化
    public void initView(View view) {
        dynamicList = (HomeListView) view.findViewById(R.id.mine_personal_dynamic);//list
        items = new ArrayList<>();//动态表
        dynamicAdapter = new MineDynamicAdapter(context, items);//适配器
        dynamicList.setAdapter(dynamicAdapter);//设置适配器
        dynamicList.setXListViewListener(this);
        dynamicList.setPullLoadEnable(true);
        dynamicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    long id = (long) items.get(i).get("id");
                    MsgPublicTool.showDynamicDetail(view.getContext(), id);
                }
            }
        });
        if (userId.equals(InfoTool.getUserID(context)))
            dynamicList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i != 0) {
                        long id = (long) items.get(i).get("id");
                        showDialog(i, id);
                    }
                    return true;
                }
            });
    }

    public void getPersonalDetail() {
        Map<String, Object> dynamicItem = new HashMap<String, Object>();
        dynamicItem.put("id", Long.parseLong(userId));
        dynamicItem.put("location", location);
        dynamicItem.put("sex", sex);
        dynamicItem.put("age", age);
        dynamicItem.put("star", star);
        dynamicItem.put("introduce", introduce);
        items.add(dynamicItem);
        dynamicAdapter.notifyDataSetChanged();
        //先个人信息后获取动态
        getDynamic();
    }

    public void getDynamic() {
        StaticMethod.POST(context, getDynamic, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", userId);
                list.put("page", page);
                return list;
            }

            @Override
            public void onResponse(String response) {
                if (response == null) showToast("网络请求失败");
                else analysis(response);
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }
        });
    }

    public void analysis(String response) {
        try {
            JSONObject dynamicJson = new JSONObject(response);
            JSONArray list = dynamicJson.getJSONArray("list");
            if (list.toString().equals("[]")) return;
            JSONObject json_item;
            String str;
            for (int i = 0; i < list.length(); i++) {
                Map<String, Object> dynamicItem = new HashMap<String, Object>();
                json_item = list.getJSONObject(i);
                dynamicItem.put("id", json_item.getLong("id"));
                //3张图片
                str = json_item.getString("pic1");
                if (str.equals("")) dynamicItem.put("image1", "");
                else dynamicItem.put("image1", imgip + str);
                str = json_item.getString("pic2");
                if (str.equals("")) dynamicItem.put("image2", "");
                else dynamicItem.put("image2", imgip + str);
                str = json_item.getString("pic3");
                if (str.equals("")) dynamicItem.put("image3", "");
                else dynamicItem.put("image3", imgip + str);
                //文字
                str = json_item.getString("content");
                dynamicItem.put("text", str);
                //时间
                str = json_item.getString("publishTime");
                if (str.equals("")) {
                    dynamicItem.put("time1", "");
                    dynamicItem.put("time2", "");
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                    String[] sd = (sdf.format(new Date(Long.parseLong(str)))).split("-");
                    dynamicItem.put("time1", Long.parseLong(sd[0]) + "");
                    dynamicItem.put("time2", sd[1]);
                }
                //添加item
                items.add(dynamicItem);
            }
            page++;
            dynamicAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            showToast("网络请求失败");
            e.printStackTrace();
        }
    }

    public void deleteDynamic(final int i, final long id) {
        StaticMethod.POST(context, ServerURL.DELETE_DYNAMIC, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", InfoTool.getUserID(context));
                list.put("dynamicId", id);
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                switch (response) {
                    case "1":
                        items.remove(i);
                        dynamicAdapter.notifyDataSetChanged();
                        if (i == items.size()) dynamicList.setSelection(i - 2);
                        else dynamicList.setSelection(i - 1);
                        break;
                    case "-1":
                        showToast("失败");
                        break;
                    case "-2":
                        showToast("服务器出错");
                        break;
                    case "-3":
                        showToast("这不是您的动态");
                        break;
                    case "-4":
                        showToast("没有此动态");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showDialog(final int i, final long id) {
        Dialog dialog = new AlertDialog.Builder(context).setTitle("是否要删除？")
                .setItems(new String[]{"是", "否"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                deleteDynamic(i, id);
                                break;
                            case 1:
                                dialog.dismiss();
                                break;
                        }
                    }
                }).create();
        dialog.show();
    }

    private void showToast(String text) {
        QianxunToast.showToast(context, text, QianxunToast.LENGTH_SHORT);
    }

    @Override
    public View getScrollableView() {
        return dynamicList;
    }
}
