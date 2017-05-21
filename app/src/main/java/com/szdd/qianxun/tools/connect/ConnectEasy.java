package com.szdd.qianxun.tools.connect;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ConnectEasy extends AsyncTask<Void, Void, String> {
    private Context context;
    private String url;
    private ConnectList list;
    private ConnectDialog dialog;
    private ConnectListener listener;
    private static RequestQueue queue;

    public ConnectEasy(Context context, String url, ConnectListener listener) {
        this.context = context;
        this.url = url;
        this.listener = listener;
        list = new ConnectList();
        dialog = new ConnectDialog();
        if (listener != null) {
            list = listener.setParam(list);
            dialog = listener.showDialog(dialog);
        }
        if (list == null)// 防止listener返回错了
            list = new ConnectList();
        if (dialog == null)
            dialog = listener.showDialog(dialog);
    }

    @Override
    protected void onPreExecute() {
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        ConnectBase con = new ConnectBase(context);
        return con.executePost(url, list);
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener != null)
            listener.onResponse(result);
        if (dialog != null)
            dialog.hide();
    }

    // ///////////////////////基于回调的方法///////////////////////

    /**
     * 向指定网址发起post请求
     *
     * @param context  context
     * @param url      网址
     * @param listener 监听回调
     */
    public static void POST(Context context, String url,
                            ConnectListener listener) {
        //刷新URL
        url = ServerURL.getSignedURL(url);
        VOLLEY(context, url, listener);
//        //发起请求
//        ConnectEasy connect = new ConnectEasy(context, url, listener);
//        connect.execute();
    }

    /**
     * 向指定网址发起post请求，强制使用原来的方法
     *
     * @param context  context
     * @param url      网址
     * @param listener 监听回调
     */
    public static void POSTLOGIN(Context context, String url,
                                 ConnectListener listener) {
        //刷新URL
        url = ServerURL.getSignedURL(url);
        //发起请求
        ConnectEasy connect = new ConnectEasy(context, url, listener);
        connect.execute();
    }

    // ///////////////////////基于Volley的方法///////////////////////

    public static void VOLLEY(final Context context, final String url,
                              final ConnectListener listener) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        final ConnectDialog dialog;
        final ConnectList list;
        if (listener == null) {
            dialog = null;
            list = new ConnectList();
        } else {
            dialog = listener.showDialog(new ConnectDialog());
            if (dialog != null)
                dialog.show();
            ConnectList list_temp = listener.setParam(new ConnectList());
            if (list_temp == null)
                list = new ConnectList();
            else
                list = list_temp;
        }
        Request request = null;
        if (!list.hasFile()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {//编码处理
                                response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                            } catch (Exception e) {
                            }
                            if (ServerURL.isTest())
                                Log.e("EEE-VOLLEY", response + "");
                            if (listener != null)
                                listener.onResponse(response);
                            if (dialog != null)
                                dialog.hide();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (listener != null)
                                listener.onResponse("");
                            if (dialog != null)
                                dialog.hide();
//                        //这句千万不能写，不知为何会报错
//                        if (error != null)
//                            Log.e("EEE-VOLLEY-ERROR", error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    if (listener != null) {
                        if (list != null) {
                            if (ServerURL.isTest())
                                Log.e("EEE-VOLLEY-params", list.getMap().size() + "");
                            return list.getMap();
                        }
                    }
                    return new HashMap<String, String>();
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> map = new HashMap<>();
                    String cookie = ConnectTool.getCookie(context);
                    map.put("Cookie", cookie == null ? "" : cookie);
                    return map;
                }
            };
            request = stringRequest;
        } else {
            MultipartRequest multipartRequest = new MultipartRequest(url,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (listener != null)
                                listener.onResponse("");
                            if (dialog != null)
                                dialog.hide();
                        }
                    },
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (ServerURL.isTest())
                                Log.e("EEE-VOLLEY-before", response);
                            try {//编码处理
                                String response_temp = new String(response.getBytes("ISO-8859-1"), "utf-8");
                                response_temp = URLDecoder.decode(response_temp, "UTF-8");
                                response = response_temp;
                            } catch (Exception e) {
                            }
                            if (ServerURL.isTest())
                                Log.e("EEE-VOLLEY", response);
                            if (listener != null)
                                listener.onResponse(response);
                            if (dialog != null)
                                dialog.hide();
                        }
                    }, list.getListKey(), list.getListFile(), list.getMap());
            request = multipartRequest;
        }
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        queue.add(request);
    }

}
