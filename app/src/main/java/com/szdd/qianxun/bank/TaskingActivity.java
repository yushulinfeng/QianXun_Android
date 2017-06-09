package com.szdd.qianxun.bank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.RoundHeadView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TaskingActivity extends Activity {
    private int userid;
    private String url = "http://192.168.0.107:8080/qianxun/userRequest_getLocal.action";
    private JSONObject jsonObj = null;

    private RoundHeadView ad_picture_user_head;
    private TextView ad_picture_user_name;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String res;
            switch (msg.what) {
                case 0x111:
                    res = (String) msg.obj;
                    if (res.equals("failed")) {
                        showToast("任务进行失败！");
                    } else {
                        showToast("任务进行！");
                        try {
                            jsonObj = Analysis(res);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private static JSONObject Analysis(String jsonStr) throws JSONException,
            org.json.JSONException {
        /******************* 解析 ***********************/
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
            // 初始化list数组对象
            // ArrayList<HashMap<String, Object>> list = new
            // ArrayList<HashMap<String, Object>>();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordering);
        Intent intent = getIntent();
        userid = intent.getIntExtra("userid", 0);


    }

    private void Init() {
        PostDataThread thread = new PostDataThread();
        thread.start();
    }

    /**
     * 向后台发送数据的子线程
     */
    private class PostDataThread extends Thread {

        @Override
        public void run() {
            super.run();

            JSONObject jsonobject = new JSONObject();
            try {
                jsonobject.put("userid", userid);

            } catch (org.json.JSONException e1) {
                e1.printStackTrace();
            }

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("jsonObj", jsonobject.toString()));
            String result = "";
            try {
                result = NetCore.postResultToNet(url, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.what = 0x111;
            msg.obj = result;
            mhandler.sendMessage(msg);
        }
    }

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }
}
