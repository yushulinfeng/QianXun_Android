package com.szdd.qianxun.sell.main.bottom;

import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;

public class SellMainFragmentNew extends SellMainFragmentBase {

    public SellMainFragmentNew() {
    }

    @Override
    public void onListLoad() {
        page++;
        updateDataFromNet();
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
        StaticMethod.POST(getActivity(), ServerURL.BUSINESS_MAIN_LAST, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("page", page + "");
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
