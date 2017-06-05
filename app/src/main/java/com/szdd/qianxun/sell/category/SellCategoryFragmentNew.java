package com.szdd.qianxun.sell.category;

import android.annotation.SuppressLint;

import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;

public class SellCategoryFragmentNew extends SellCategoryFragmentBase {
    public SellCategoryFragmentNew() {
    }

    @SuppressLint("ValidFragment")
    public SellCategoryFragmentNew(int index) {
        super(index);
    }

    @Override
    public void onListLoad() {
        page++;
        updateDataFromNet();
    }

    @Override
    public void onListRefresh() {
        page=1;
        updateDataFromNet();
    }

    @Override
    public boolean onLazyLoad() {
        page=1;
        updateDataFromNet();
        return true;
    }

    private void updateDataFromNet() {
        StaticMethod.POST(getActivity(), ServerURL.BUSINESS_LAST, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("page", page + "");
                list.put("category", index + "");
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
