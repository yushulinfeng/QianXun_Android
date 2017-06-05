package com.szdd.qianxun.sell.orders.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.bank.AdapterForTabThreeTwo;
import com.szdd.qianxun.bank.LoadMoreListView;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/27 0027.
 */
public class ServiceDetailCollecterActivity extends Activity implements
        LoadMoreListView.OnLoadMore, View.OnClickListener {

    private LoadMoreListView lmlistview;
    private List<Map<String, Object>> listItems;
    private AdapterForTabThreeTwo adapterfortabthreetwo;
    private String serviceId;

    private ImageView service_detail_collecter_iv_back;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_service_detail_collecter);
        Intent intent = getIntent();
        serviceId = intent.getStringExtra("serviceId");
        listItems = new ArrayList<Map<String, Object>>();
        lmlistview = (LoadMoreListView) findViewById(R.id.service_detail_collecter_lmListView_02);
        lmlistview.setLoadMoreListen(this);
        adapterfortabthreetwo = new AdapterForTabThreeTwo(
                ServiceDetailCollecterActivity.this, listItems);
        lmlistview.setAdapter(adapterfortabthreetwo);
        service_detail_collecter_iv_back = (ImageView)findViewById(R.id.service_detail_collecter_iv_back);
        service_detail_collecter_iv_back.setOnClickListener(this);
        updateDataFromNetForServiceCollector();
    }

    //连接后台获取服务收藏者的详细信息
    private void updateDataFromNetForServiceCollector() {
        StaticMethod.POST(ServiceDetailCollecterActivity.this, ServerURL.SERVICE_DETAIL_COLLECTOR_URL,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        return dialog;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                       // list.put("serviceId", "1", false);
                       list.put("serviceId", serviceId, false);

                        return list;
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response == null || response.equals("")) {
                            // 网络错误
                            finish();
                            return;
                        }
                        //处理后台发送来的数据
                        if (response.equals("failed")) {
                            finish();
                        } else {
                            try {
                                Analysis(response);
                            } catch (com.alibaba.fastjson.JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            adapterfortabthreetwo.notifyDataSetChanged();
                        }
                    }
                });
    }
    /**
     * 解析
     *
     * @throws com.alibaba.fastjson.JSONException
     * @throws org.json.JSONException
     */
    private void Analysis(String jsonStr) {
        /******************* 解析 ***********************/
        JSONArray jsonArray = null;
        // 初始化list数组对象
        JSONObject jsonobject;
        int isapply = 0;
        try {
            jsonobject = new JSONObject(jsonStr);
                jsonArray = jsonobject.getJSONArray("list");
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {

                        jsonObject = jsonArray.getJSONObject(i);
                        // 初始化map数组对象
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", jsonObject.getInt("id"));
                        map.put("username", jsonObject.getLong("username"));
                        map.put("headIcon", jsonObject.getString("headIcon"));
                        map.put("nickName", jsonObject.getString("nickName"));
                        listItems.add(map);
                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e1) {
            e1.printStackTrace();
            }

        }

    @Override
    public void onClick(View v) {
        int Rid = v.getId();
        switch (Rid) {
            case R.id.service_detail_collecter_iv_back:
                finish();
                break;
        }
    }

    @Override
    public void loadMore() throws org.json.JSONException {
        QianxunToast.showToast(this, "无更多收藏者", QianxunToast.LENGTH_SHORT);
        lmlistview.onLoadComplete();
    }
}
