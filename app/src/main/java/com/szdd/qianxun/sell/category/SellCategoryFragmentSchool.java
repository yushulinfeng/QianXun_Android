package com.szdd.qianxun.sell.category;

import android.annotation.SuppressLint;

import com.szdd.qianxun.message.info.AnUserInfo;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;

public class SellCategoryFragmentSchool extends SellCategoryFragmentBase {
    private String school = "";

    public SellCategoryFragmentSchool() {
    }

    @SuppressLint("ValidFragment")
    public SellCategoryFragmentSchool(int index) {
        super(index);
    }

    @Override
    public void onListLoad() {
        page++;
        updateDataFromNet();
    }

    private void initSchool() {
        AnUserInfo info = InfoTool.getUserInfo(getActivity());
        if (info != null)
            school = info.getSchool();
        if (school == null)
            school = "";
    }

    @Override
    public void onListRefresh() {
        page = 1;
        updateDataFromNet();
    }

    @Override
    public boolean onLazyLoad() {
        page = 1;
        updateDataFromNet();
        return true;
    }

    private void updateDataFromNet() {
        if (school.equals(""))
            initSchool();
        if (!school.equals(""))
            StaticMethod.POST(getActivity(), ServerURL.BUSINESS_SCHOOL, new ConnectListener() {
                @Override
                public ConnectList setParam(ConnectList list) {
                    list.put("page", page + "");
                    list.put("category", index + "");
                    list.put("school", school);
                    return list;
                }

                @Override
                public ConnectDialog showDialog(ConnectDialog dialog) {
                    return null;
                }

                @Override
                public void onResponse(String response) {
                    dealResponse(response);
                }
            });
    }

}
