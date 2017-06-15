package com.szdd.qianxun.start.welcome;

import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.szdd.qianxun.R;
import com.szdd.qianxun.main_main.MainMain;
import com.szdd.qianxun.message.baichuan.im.LoginSampleHelper;
import com.szdd.qianxun.message.baichuan.mine.BaiChuanUtils;
import com.szdd.qianxun.message.baichuan.util.AppUtil;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.MsgTool;
import com.szdd.qianxun.message.msg_tool.ParamTool;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.more.AboutMe;
import com.szdd.qianxun.start.login.Login;
import com.szdd.qianxun.start.tool.UserTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectEasy;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ConnectSign;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.CacheTool;
import com.szdd.qianxun.tools.top.NetActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Welcome extends NetActivity {
    @Bind(R.id.welcome_img_0)
    ImageView img0;
    @Bind(R.id.welcome_img_1)
    ImageView img1;

    private String name = "", pass = "";
    private String msg = "";
    private boolean need_close = false;
    private int curr_index = 0;
    private static final int[] imgs = {R.drawable.welcome_01, R.drawable.welcome_02,
            R.drawable.welcome_03, R.drawable.welcome_04,
            R.drawable.welcome_05, R.drawable.welcome_06,
            R.drawable.welcome_07};

    @Override
    public void onCreate() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.start_welcome);
        ButterKnife.bind(this);

        postRun();
        /*
        getMessage();
        loadFirstImg();
        startNewThread();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                postRun();
            }
        }, 1000);// 时间可以慢慢调整
        */
    }

    private void postRun() {
        if (isSeriousError())
            return;
        initIP();
        UserTool.setLoginState(Welcome.this, false);// 先保存为未登录状态，然后在login中修改
        if (!UserStateTool.isLoginEver(Welcome.this)) {
            try {
                LoginSampleHelper.getInstance().initIMKitTemp();
            } catch (Exception e) {
            }
        }
        updateTimestamp();
    }

    private void initIP() {
        ServerURL.getIP();
    }

    private void loadFirstImg() {
        need_close = false;
        curr_index = (int) (Math.random() * imgs.length);
        img1.setImageResource(imgs[curr_index]);
    }

    private void loadNextImage() {
        int index = (int) (Math.random() * imgs.length);
        while (index == curr_index) {
            index = (int) (Math.random() * imgs.length);
        }
        curr_index = index;
        img0.setImageResource(imgs[curr_index]);
        //方式一通过代码的方式定义透明度动画
        Animation alphaAnimation = new AlphaAnimation((float) 1, (float) 0);
        alphaAnimation.setDuration(1000);//设置动画持续时间为3秒
        alphaAnimation.setFillAfter(true);//设置动画结束后保持当前的位置（即不返回到动画开始前的位置）
        img1.startAnimation(alphaAnimation);
    }

    private void prepareNextImg() {
        img1.setImageResource(imgs[curr_index]);
    }

    private boolean isSeriousError() {
        String value = ParamTool.getParam("serious_error");
        if (value.equals("1")) {
            Toast.makeText(this, "系统正在维护", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AboutMe.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    private void getMessage() {
        msg = getIntent().getStringExtra("note_extra");
        if (msg == null)
            msg = "";
    }

    private void autoLogin() {
        String[] user = UserTool.getUser(this);
        name = user[0];
        pass = user[1];
        if (name.equals("") || pass.length() < 6) {// 未登录过，或者是测试的
            pass = "";
            toLoginActivity();
            return;
        }
        // 有登录记录，到login中自动登录（减少重复代码）
        startLogin();
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private void toMainActivity() {
        //if (!msg.equals("")) {}
        Intent intent = new Intent(this, MainMain.class);
        startActivity(intent);
        finish();
    }

    private void startLogin() {
        ConnectEasy.POSTLOGIN(this, ServerURL.LOGIN, new ConnectListener() {
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            public ConnectList setParam(ConnectList list) {
                list.put("appSecret", Login.APP_SECRET);
                list.put("username", name);
                list.put("password", pass);
                return list;
            }

            public void onResponse(String response) {
                loginBaiChuan(response); // 失败等都在这里面处理了
            }
        });
    }

    private void loginBaiChuan(String response) {
        String pass = MsgTool.dealResponseGetPass(response, name);
        String name = null;
        try {
            name = BaiChuanUtils.getUserName(Long.parseLong(InfoTool.getUserID(this)));
        } catch (Exception e) {
            return;
        }
        // 保存登录状态
        UserTool.setLoginState(Welcome.this, true);// 保存为登录状态
        BaiChuanUtils.login(name, pass, null);//在后台慢慢地登录去吧
        CacheTool.clearCache();//清空缓存
        //等待3秒钟，减少网络占用
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppUtil.dismissProgressDialog();
                toMainActivity();
            }
        }, 3000);
//        AppUtil.dismissProgressDialog();
//        if (state) {
//            toMainActivity();
//        } else {
//            BaiChuanUtils.showToast("登录失败");
//            toLoginActivity();
//        }
    }

    @Override
    protected void onDestroy() {
        need_close = true;
        System.gc();
        super.onDestroy();
    }


    @Override
    public void receiveMessage(String what) {
        if (what == null) {
            loadNextImage();
        } else {
            prepareNextImg();
        }
    }

    @Override
    public void newThread() {
        while (!need_close) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            sendMessage(null);
            if (need_close)
                return;
            try {
                Thread.sleep(900);
            } catch (Exception e) {
            }
            sendMessage("");
        }
    }

    private void updateTimestamp() {
        StaticMethod.POST(this, ServerURL.TIME_STAMP, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                return null;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                ConnectSign.dealTimeSpace(response);//内部已处理

                autoLogin();
//                //千寻项目，取消首次运行的欢迎界面
//                if (UserTool.isFirstRun(Welcome.this)) {
//                    startActivity(new Intent(Welcome.this, FirstRun.class));
//                    finish();
//                } else {
//                    autoLogin();
//                }
            }
        });
    }

}
