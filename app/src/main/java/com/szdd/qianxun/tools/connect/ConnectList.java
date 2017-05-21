package com.szdd.qianxun.tools.connect;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class ConnectList {
    private List<NameValuePair> list = null;
    private Map<String, String> map = null;
    private List<String> list_key = null;
    private List<File> list_file = null;
    private boolean has_file = false;

    public ConnectList() {
        list = new ArrayList<>();
        map = new HashMap<>();
        list_key = new ArrayList<>();
        list_file = new ArrayList<>();
    }

    /**
     * 添加一个文件，服务器收到File类
     */
    public ConnectList put(String key, File file) {
        list_key.add(key);
        list_file.add(file);
        has_file = true;
        return this;
    }

    /**
     * 添加一个文件，服务器收到Set-File类
     */
    public ConnectList put(String key, List<File> files) {
        for (File file : files)
            put(key, file);
        return this;
    }

    /**
     * 添加一个键值对，是否强制encode，影响list，不影响map
     */
    public ConnectList put(String key, String value, boolean should_encode) {
        map.put(key, value == null ? "" : value);//map直接put。
        try {
            if (should_encode)
                value = URLEncoder.encode(value == null ? "" : value, "UTF-8");
        } catch (Exception e) {
            return this;
        }
        NameValuePair item = new BasicNameValuePair(key, value);
        list.add(item);
        return this;
    }

    /**
     * 添加一个文件，服务器收到File类
     * 防止null冲突
     */
    public ConnectList putFile(String key, File file) {
        return put(key, file);
    }

    /**
     * 添加一个键值对，String值
     */
    public ConnectList put(String key, String value) {
        return put(key, value, true);
    }

    /**
     * 添加一个键值对，Long值
     */
    public ConnectList put(String key, long value) {
        return put(key, value + "");
    }

    public List<NameValuePair> getList() {
        return list;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public List<String> getListKey() {
        return list_key;
    }

    public List<File> getListFile() {
        return list_file;
    }

    /**
     * 是否包含文件
     */
    public boolean hasFile() {
        return has_file;
    }

    // ///////////////////静态方法////////////////////////////////

    /**
     * 直接获取网络数据类
     *
     * @param key_value 键1，值1，键2，值2，……键n,值n
     * @return 网络数据类
     */
    public static ConnectList getSimpleList(String... key_value) {
        if (key_value.length % 2 == 1)
            return null;
        ConnectList list = new ConnectList();
        for (int i = 0; i < key_value.length; i += 2) {
            list.put(key_value[i], key_value[i + 1]);
        }
        return list;
    }

}
