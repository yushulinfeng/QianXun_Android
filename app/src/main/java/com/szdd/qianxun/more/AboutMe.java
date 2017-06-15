package com.szdd.qianxun.more;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.top.TActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutMe extends TActivity {

    @Bind(R.id.more_veesion)
    TextView moreVeesion;
    @Bind(R.id.more_time)
    TextView moreTime;

    @Override
    public void onCreate() {
        setContentView(R.layout.more_about_me);
        ButterKnife.bind(this);
        setTitle("关于软件");
        showBackButton();
        initView();
        initActionBar(getResources().getColor(R.color.topbar_bg));
    }

    private void initView() {
        try {
            PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String name = packInfo.versionName;
            moreVeesion.setText("软件版本：v" + name);
        } catch (PackageManager.NameNotFoundException e) {
        }
//        String time = ParamTool.getParam("version_time");
//        if (!time.equals(""))
//            moreTime.setText(time);
    }

    @Override
    public void showContextMenu() {
    }

}
