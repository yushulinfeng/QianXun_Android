package com.szdd.qianxun.message.friend;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.szdd.qianxun.message.info.AnUserInfo;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.ShareTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendListTool {
    private static final String FIRSTRUN_SAVE_PATH = "msg.friend_list.read_state";
    private static final String FRIEND_SAVE_PATH = "msg.friend_list.friend_list";

    /**
     * 判断用户是否为首次打开
     */
    public static boolean isFirstRun(Context context) {
        SharedPreferences mPref = context.getSharedPreferences(
                FIRSTRUN_SAVE_PATH, Activity.MODE_PRIVATE);
        boolean first = mPref.getBoolean("first", true);
        return first;
    }

    /**
     * 存储为非首次打开
     */
    public static void writeNotFirstRun(Context context) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                FIRSTRUN_SAVE_PATH, Activity.MODE_PRIVATE).edit();
        prefs.putBoolean("first", false);
        prefs.commit();
    }

    public static boolean addOneFriend(Context context, int id, long username) {
        return addOneFriend(context, id, username, null);
    }

    public static boolean addOneFriend(final Context context, final int id, final long username, final String nickname) {
        final FriendItems items = getFriendList(context);
        final ArrayList<AnFriendItem> array = items.getArray();
        if (isInList(array, id) >= 0)
            return false;
        if (nickname != null) {
            AnFriendItem item = new AnFriendItem(id, username, nickname);
            array.add(item);
            items.setArray(array);
            saveFriendList(context, items);
        } else {
            StaticMethod.POST(context, ServerURL.HOMEPAGE_PERSONAL_DETAIL, new ConnectListener() {
                @Override
                public ConnectList setParam(ConnectList list) {
                    list.put("userId", id);
                    return list;
                }

                @Override
                public ConnectDialog showDialog(ConnectDialog dialog) {
                    return null;
                }

                @Override
                public void onResponse(String response) {
                    try {
                        AnUserInfo info = new Gson().fromJson(response, AnUserInfo.class);
                        String nick = info.getNickName();
                        FriendItems items = getFriendList(context);
                        ArrayList<AnFriendItem> array = items.getArray();
                        AnFriendItem item = new AnFriendItem(id, username, nick);
                        item.setIconPath(info.getHeadIcon());
                        array.add(item);
                        items.setArray(array);
                        saveFriendList(context, items);
                    } catch (Exception e) {
                    }
                }
            });
        }
        return true;
    }

    public static int isInList(ArrayList<AnFriendItem> array, int id) {
        for (int i = 0; i < array.size(); i++) {
            AnFriendItem item = array.get(i);
            if (id == item.getUserID()) {
                return i;
            }
        }
        return -1;
    }

    public static void saveFriendList(Context context, FriendItems items) {
        ShareTool.saveText(context, FRIEND_SAVE_PATH, new Gson().toJson(items) + "");
    }

    public static FriendItems getFriendList(Context context) {
        FriendItems items = new Gson().fromJson(ShareTool.getText(context, FRIEND_SAVE_PATH),
                FriendItems.class);
        if (items == null || items.getArray() == null) {
            items = new FriendItems();
            items.setArray(new ArrayList<AnFriendItem>());
        }
        return items;
    }

    //1：原来的表，2：收藏的表
    public static ArrayList<AnFriendItem> combineFriendList(ArrayList<AnFriendItem> array1,
                                                            ArrayList<AnFriendItem> array2) {
        if (array1 == null && array2 == null) return null;
        if (array1 == null) return array2;
        if (array2 == null) return array1;
        for (int i = 0; i < array1.size(); i++) {
            array1.get(i).setState(false);
        }
        for (int i = 0; i < array2.size(); i++) {
            AnFriendItem item = array2.get(i);
            int index = isInList(array1, item.getUserID());
            if (index >= 0) {
                array1.remove(i);
            }
            array1.add(0, item);
        }
        return array1;
    }

    public static void getCollectFromServer(final Context context, final FriendListener listener) {
        StaticMethod.POST(context, ServerURL.GET_ALL_ATTENTION, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", InfoTool.getUserID(context));
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                ArrayList<AnCollect> collect_list = new ArrayList<AnCollect>();
                try {
                    JSONArray array = new JSONObject(response).getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        collect_list.add(new Gson().fromJson(obj.toString(), AnCollect.class));
                    }
                } catch (Exception e) {
                }
                ArrayList<AnFriendItem> array = new ArrayList<AnFriendItem>();
                for (int i = 0; i < collect_list.size(); i++) {
                    array.add(collect_list.get(i).toAnFriendItem());
                }
                FriendItems items = new FriendItems(array);
                if (listener != null)
                    listener.onResponse(items);
            }
        });
    }


    public static void addAttention(final Context context, final int user_id) {
        StaticMethod.POST(context, ServerURL.HOMEPAGE_ATTENTION, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("id", InfoTool.getUserID(context));
                    jo.put("concernedId", user_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                list.put("jsonObj", jo.toString());
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {//暂不处理结果
//                                if (response == null)
//                                    Toast.makeText(FriendList.this, "网络请求失败", Toast.LENGTH_SHORT).show();
//                                else {
//                                    switch (response) {
//                                        case "1":
//                                            Toast.makeText(FriendList.this, "关注成功", Toast.LENGTH_SHORT).show();
//                                            changeAttention(false);
//                                            break;
//                                        case "-1":
//                                            Toast.makeText(FriendList.this, "失败", Toast.LENGTH_SHORT).show();
//                                            break;
//                                        case "-2":
//                                            Toast.makeText(FriendList.this, "服务器出错", Toast.LENGTH_SHORT).show();
//                                            break;
//                                        case "-3":
//                                            Toast.makeText(FriendList.this, "已经关注", Toast.LENGTH_SHORT).show();
//                                            break;
//                                        case "-4":
//                                            Toast.makeText(FriendList.this, "不能关注自己", Toast.LENGTH_SHORT).show();
//                                            break;
//                                    }
//                                }
            }
        });

    }

    public static void delAttention(final Context context, final int user_id) {
        StaticMethod.POST(context, ServerURL.HOMEPAGE_CANCEL_ATTENTION, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("id", InfoTool.getUserID(context));
                    jo.put("concernedId", user_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                list.put("jsonObj", jo.toString());
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
//                        if (response == null)
//                            Toast.makeText(FriendList.this, "网络请求失败", Toast.LENGTH_SHORT).show();
//                        else {
//                            switch (response) {
//                                case "1":
//                                    Toast.makeText(FriendList.this, "取消关注", Toast.LENGTH_SHORT).show();
//                                    //     changeAttention(true);
//                                    break;
//                                case "-1":
//                                    Toast.makeText(FriendList.this, "失败", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case "-2":
//                                    Toast.makeText(FriendList.this, "服务器出错", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case "-3":
//                                    Toast.makeText(FriendList.this, "未关注", Toast.LENGTH_SHORT).show();
//                                    break;
//                            }
//                        }
            }
        });
    }


}
