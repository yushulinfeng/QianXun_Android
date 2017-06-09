package com.szdd.qianxun.bank;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.szdd.qianxun.main_main.MainTab_03;
import com.szdd.qianxun.tools.views.QianxunToast;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class ApplyIsAgreeService extends Service {

    boolean ishandle = false;
    private String url = "http://192.168.0.107:8080/qianxun/userRequest_getLocal.action";

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String res;
            switch (msg.what) {
                case 0x111:
                    res = (String) msg.obj;
                    if (res.equals("failed")) {
                        showToast("接单失败！");
                        Intent intent = new Intent(ApplyIsAgreeService.this, MainTab_03.class);
                        startActivity(intent);
                        ishandle = true;
                    } else if (res.equals("failed")) {
                        showToast("成功接单！");
                        Intent intent = new Intent(ApplyIsAgreeService.this, TaskingActivity.class);
                        intent.putExtra("userid", "userid");
                        startActivity(intent);
                        ishandle = true;
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PostDataThread thread = new PostDataThread();
        thread.start();
        if (ishandle) {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
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
                jsonobject.put("userid", "userid");

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
