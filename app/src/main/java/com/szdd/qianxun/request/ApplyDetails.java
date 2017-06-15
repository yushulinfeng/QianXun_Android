package com.szdd.qianxun.request;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplyDetails extends Activity  {

    private List<Map<String, Object>> listitems;
    private ListView listview;
    private AdapterForDetails adapter;
    private TextView tv_title;
    private ImageView btn_back;
    public int peopleNumber = 0;
    private Bundle bundle;
    private String JsonString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_details);
        bundle = getIntent().getExtras();
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        btn_back = (ImageView) this.findViewById(R.id.back_setting);
        listitems = new ArrayList<>();
        listview = (ListView) findViewById(R.id.apply_detail_loadmorelistview);
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        StaticMethod.POST(ApplyDetails.this, ServerURL.RECEIVED_PEOPLE_NUMBER,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        dialog.config(ApplyDetails.this, "发送中...", "玩命加载中...", false);
                        return dialog;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("requestId", bundle.getString("userRequestId"));
                        return list;
                    }

                    @Override
                    public void onResponse(String response) {
                        JsonString = response;
                        if (JsonString != null) {
                            AnalysizeJsonData(JsonString);
                            adapter = new AdapterForDetails(ApplyDetails.this, ApplyDetails.this, listitems);
                            listview.setAdapter(adapter);
                        }
                        tv_title.setText(peopleNumber + "人已报名");
                    }
                });
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                try {
                    MsgPublicTool.showHomePage(ApplyDetails.this, Long.parseLong(listitems.get(position).get("applyid").toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void AnalysizeJsonData(String jsonstring) {
        if (jsonstring.equals(null) || jsonstring.equals("")) {
        } else {
            JSONObject object = null;
            try {
                object = JSONObject.parseObject(jsonstring);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONArray array = null;
            try {
                array = object.getJSONArray("receivers");
            } catch (Exception e) {
                e.printStackTrace();
            }
            peopleNumber = array.size();
            for (int i = 0; i < array.size(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("applyname", array.getJSONObject(i).getString("nickName"));
                item.put("applyid", array.getJSONObject(i).getString("username"));
                item.put("gender", array.getJSONObject(i).getString("gender"));
                item.put("applyrank", array.getJSONObject(i).getString("rank_credit"));
                item.put("icon_url", ServerURL.getIP() + array.getJSONObject(i).getString("headIcon"));
                item.put("requestId", bundle.getString("userRequestId"));
                listitems.add(item);
            }
        }
    }
}
