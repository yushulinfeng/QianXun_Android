package com.szdd.qianxun.more;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.bank.MainTab_03_RequestDetailActivity;
import com.szdd.qianxun.request.Activity_Task_doing;
import com.szdd.qianxun.request.Task_completed;
import com.szdd.qianxun.request.Task_doing_Ta;
import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;

/**
 * Created by DELL on 2016/4/10.
 */
@SuppressLint("ValidFragment")
public class RequestFragment extends ScrollAbleFragment {

    private View view;
    private ListView listView;
    private Context context;
    private RequestAdapter adapter;
    private int falg;

    public RequestFragment(Context context, RequestAdapter adapter, int falg) {
        this.context = context;
        this.adapter = adapter;
        this.falg = falg;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.more_request_fragment_layout, container, false);
        listView = (ListView) view.findViewById(R.id.more_request_list);
        listView.setAdapter(adapter);
        setClick(falg);
        return view;
    }

    @Override
    public View getScrollableView() {
        return listView;
    }

    public void setClick(int falg) {
        switch (falg) {
            case 0:
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(getActivity(), MainTab_03_RequestDetailActivity.class);// 需求二级界面
                        intent.putExtra("fatherA", "two");// 跳到二级界面用的标识
                        String str = adapter.getList().get(position).get("userRequestId").toString();
                        int requestId = Integer.parseInt(str);
                        intent.putExtra("requestId", requestId);
                        getActivity().startActivity(intent);
                    }
                });
                break;
            case 1:
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        int status = Integer.parseInt(adapter.getList().get(position).get("type").toString());
                        Intent intent = null;
                        Bundle bund = new Bundle();
                        if (status == 1) {// 跳转到笑脸界面—— Activity_Task_doing
                            intent = new Intent(getActivity(), Activity_Task_doing.class);
                            bund.putString("userRequestId", adapter.getList().get(position).get("userRequestId").toString());
                            bund.putString("nickname", adapter.getList().get(position).get("Receiver_user_nickname").toString());
                            bund.putString("gender", adapter.getList().get(position).get("Receiver_gender").toString());
                            bund.putString("userId", adapter.getList().get(position).get("Receiver_userId").toString());
                            bund.putString("headIcon", adapter.getList().get(position).get("Receiver_headIcon").toString());
                            bund.putString("phonenumber", adapter.getList().get(position).get("Receiver_username").toString());
                            intent.putExtras(bund);
                        } else if (status == 3) {// 跳转到确认完成界面—— Task_completed
                            try {
                                intent = new Intent(getActivity(), Task_completed.class);
                                bund.putString("icon_url", adapter.getList().get(position).get("Receiver_headIcon").toString());
                                bund.putString("content", adapter.getList().get(position).get("descripe").toString());
                                bund.putString("userId", adapter.getList().get(position).get("Receiver_userId").toString());
                                bund.putString("nickname", adapter.getList().get(position).get("Receiver_user_nickname").toString());
                                bund.putString("userRequestId", adapter.getList().get(position).get("userRequestId").toString());
                                intent.putExtras(bund);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        getActivity().startActivity(intent);
                    }
                });
                break;
            case 2:
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(getActivity(), MainTab_03_RequestDetailActivity.class);
                        intent.putExtra("fatherA", "two");
                        String str = adapter.getList().get(position).get("userRequestId").toString();
                        int requestId = Integer.parseInt(str);
                        intent.putExtra("requestId", requestId);
                        getActivity().startActivity(intent);
                    }
                });
                break;
            case 3:
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(getActivity(), MainTab_03_RequestDetailActivity.class);
                        intent.putExtra("fatherA", "two");
                        String str = adapter.getList().get(position).get("userRequestId").toString();
                        int requestId = Integer.parseInt(str);
                        intent.putExtra("requestId", requestId);
                        getActivity().startActivity(intent);
                    }
                });
                break;
            case 4:
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(getActivity(), Task_doing_Ta.class);
                        Bundle bud = new Bundle();
                        bud.putString("userRequestId", adapter.getList().get(position).get("userRequestId").toString());
                        intent.putExtras(bud);
                        getActivity().startActivity(intent);
                    }
                });
                break;
            case 5:
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(getActivity(), MainTab_03_RequestDetailActivity.class);
                        intent.putExtra("fatherA", "two");
                        String str = adapter.getList().get(position).get("userRequestId").toString();
                        int requestId = Integer.parseInt(str);
                        intent.putExtra("requestId", requestId);
                        getActivity().startActivity(intent);
                    }
                });
                break;
            default:
                break;
        }
    }
}
