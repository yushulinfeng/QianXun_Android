package com.szdd.qianxun.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.xlist.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linorz on 2016/4/8.
 */
public class SelectService extends Activity implements XListView.XListViewListener {
    private Context context;
    private LayoutInflater inflater;
    private List<Map<String, Object>> items;
    private XListView servicelist;
    private SelectServiceAdapter adapter;
    private Activity activity;
    private String getService = ServerURL.HOMEPAGE_SERVICE;
    private String userId;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        if (userId == null) userId = InfoTool.getUserID(this);
        setContentView(R.layout.post_dynamic_selectservice);
        initAllView();
    }

    public void initAllView() {
        activity = this;
        context = getBaseContext();
        inflater = LayoutInflater.from(context);
        Button back = (Button) findViewById(R.id.selectservice_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        servicelist = (XListView) findViewById(R.id.selectservice_list);
        items = new ArrayList<>();
        adapter = new SelectServiceAdapter();
        servicelist.setAdapter(adapter);
        servicelist.setPullRefreshEnable(true);
        servicelist.setPullLoadEnable(true);
        servicelist.setXListViewListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getService(true);
    }

    @Override
    public void onRefresh() {
        items.clear();
        page = 1;
        adapter.notifyDataSetChanged();
        getService(true);
        servicelist.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        getService(false);
        servicelist.stopLoadMore();
    }

    public void getService(final boolean isFirst) {
        StaticMethod.POST(context, getService, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", userId);
                list.put("page", page);
                return list;
            }

            @Override
            public void onResponse(String response) {
                if (response == null) showToast("网络请求失败");
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray list = jsonObject.getJSONArray("list");
                        if (list.toString().equals("[]")) {
                            if (page == 1) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("content", "无服务");
                                map.put("id", 0L);
                                items.add(map);
                                adapter.notifyDataSetChanged();
                            }
                            showToast("没有服务了");
                            return;
                        } else if (isFirst) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("content", "不添加服务");
                            map.put("id", 0L);
                            items.add(map);
                            adapter.notifyDataSetChanged();
                        }
                        JSONObject json_item;
                        for (int i = 0; i < list.length(); i++) {
                            Map<String, Object> serviceItem = new HashMap<String, Object>();
                            json_item = list.getJSONObject(i);
                            String content = json_item.getString("name");
                            serviceItem.put("content", content);//标题
                            long id = json_item.getLong("id");
                            serviceItem.put("id", id);//服务id
                            items.add(serviceItem);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        showToast("网络请求失败");
                        e.printStackTrace();
                    }
                }
                page++;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }
        });
    }

    class SelectServiceAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.post_dynamic_selectservice_item, null);
            TextView text = (TextView) view.findViewById(R.id.selectservice_item_text);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.selectservice_item_layout);
            text.setText((String) items.get(i).get("content"));
            final String str = text.getText().toString();
            final long id = (long) items.get(i).get("id");
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("id", id);
                    intent.putExtra("service", str);
                    activity.setResult(RESULT_OK, intent);
                    activity.finish();
                }
            });
            return view;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    private void showToast(String text) {
        QianxunToast.showToast(context, text, QianxunToast.LENGTH_SHORT);
    }
}
