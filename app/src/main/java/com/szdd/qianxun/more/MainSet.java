package com.szdd.qianxun.more;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.CPUpdateDownloadCallback;
import com.baidu.autoupdatesdk.ConfirmDialoigActivity;
import com.szdd.qianxun.R;
import com.szdd.qianxun.main_main.MainMain;
import com.szdd.qianxun.message.baichuan.mine.BaiChuanUtils;
import com.szdd.qianxun.message.baichuan.util.AndTools;
import com.szdd.qianxun.message.baichuan.util.AppUtil;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.tools.file.FileTool;
import com.szdd.qianxun.tools.file.ShareTool;
import com.szdd.qianxun.tools.font.FontTool;
import com.szdd.qianxun.tools.top.TActivity;
import com.umeng.analytics.MobclickAgent;

public class MainSet extends TActivity implements View.OnClickListener {
    public static final int LOGOUT_CODE = 1;
    private Button btn_msg_voice, btn_msg_verb, btn_push_collect, btn_font, btn_version,
            btn_about, btn_feedback, btn_clean, btn_exit, btn_logout;
    private ToggleButton tbtn_msg_voice, tbtn_msg_verb, tbtn_push_collect;
    private int font_index = 0;

    @Override
    public void onCreate() {
        setContentView(R.layout.main_set);
        setTitle("软件设置");
        showBackButton();
        initView();
        initActionBar(getResources().getColor(R.color.topbar_bg));
    }

    private void initView() {
        tbtn_msg_voice = (ToggleButton)
                findViewById(R.id.tab05_tbtn_msg_voice);
        tbtn_msg_verb = (ToggleButton)
                findViewById(R.id.tab05_tbtn_msg_verb);
        tbtn_push_collect = (ToggleButton)
                findViewById(R.id.tab05_tbtn_push_collect);
        btn_msg_voice = (Button) findViewById(R.id.tab05_btn_msg_voice);
        btn_msg_verb = (Button) findViewById(R.id.tab05_btn_msg_verb);
        btn_push_collect = (Button)
                findViewById(R.id.tab05_btn_push_collect);
        btn_font = (Button) findViewById(R.id.tab05_btn_font);
        btn_version = (Button) findViewById(R.id.tab05_btn_version);
        btn_feedback = (Button) findViewById(R.id.tab05_btn_feedback);
        btn_clean = (Button) findViewById(R.id.tab05_btn_cleancache);
        btn_about = (Button) findViewById(R.id.tab05_btn_aboutme);
        btn_exit = (Button) findViewById(R.id.tab05_btn_exit);
        btn_logout = (Button) findViewById(R.id.tab05_btn_logout);

        tbtn_msg_voice.setOnClickListener(this);
        tbtn_msg_verb.setOnClickListener(this);
        tbtn_push_collect.setOnClickListener(this);
        btn_msg_voice.setOnClickListener(this);
        btn_msg_verb.setOnClickListener(this);
        btn_push_collect.setOnClickListener(this);
        btn_font.setOnClickListener(this);
        btn_version.setOnClickListener(this);
        btn_feedback.setOnClickListener(this);
        btn_clean.setOnClickListener(this);
        btn_about.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        btn_exit.setVisibility(View.GONE);//处理麻烦，暂时隐藏

        if (!BaiChuanUtils.getSoundState())
            tbtn_msg_voice.setChecked(false);
        if (!BaiChuanUtils.getVibrateState())
            tbtn_msg_verb.setChecked(false);
        if (!BaiChuanUtils.getNoteState())
            tbtn_push_collect.setChecked(false);
    }

    private static boolean updateLocker = false;//防止多次下载

    public static void checkNewVersion(final Activity activity, final boolean can_next_time) {
        if (updateLocker) {
            AndTools.showToast("正在更新……");
        }
        updateLocker = true;
        final String SKIP_SAVE_PATH = "version_skip";
        BDAutoUpdateSDK.cpUpdateCheck(activity, new CPCheckUpdateCallback() {
            @Override
            public void onCheckUpdateCallback(final AppUpdateInfo appUpdateInfo, final AppUpdateInfoForInstall appUpdateInfoForInstall) {
                if ((appUpdateInfo == null) && (appUpdateInfoForInstall == null)) {//已经是最新版本
                    if (!can_next_time)
                        AndTools.showToast("已是最新版本。");
                    updateLocker = false;
                } else {
                    final String newVersion, newText;
                    final int newState;
                    if (appUpdateInfo != null) {
                        newVersion = appUpdateInfo.getAppVersionName();
                        newText = appUpdateInfo.getAppChangeLog();
                        newState = 1;
                    } else {
                        newVersion = appUpdateInfoForInstall.getAppVersionName();
                        newText = appUpdateInfoForInstall.getAppChangeLog();
                        newState = 2;
                    }
                    if (can_next_time) {
                        //此处包含双重忽略，均为不忽略才会提示更新。逻辑已处理好。
                        // 手动检查更新仅受到ShareTool的影响。
                        String skip_code = ShareTool.getText(activity, SKIP_SAVE_PATH);
                        if (!newVersion.equals(skip_code))
                            BDAutoUpdateSDK.uiUpdateAction(activity, null);
                        updateLocker = false;
                        return;
                    }
                    ConfirmDialoigActivity.show(activity, newState, "版本号：" + newVersion, "更新内容：\r\n" + newText,
                            new ConfirmDialoigActivity.OnActionListener() {
                                @Override
                                public void onAction() {
                                    if (newState == 1) {
                                        BDAutoUpdateSDK.cpUpdateDownload(activity, appUpdateInfo, new CPUpdateDownloadCallback() {
                                            @Override
                                            public void onStart() {
                                                AppUtil.sendNote("高能-版本更新", "进度：0%");
                                            }

                                            @Override
                                            public void onPercent(int i, long l, long l1) {
                                                AppUtil.sendNote("高能-版本更新", "下载进度：" + i + "%");
                                            }

                                            @Override
                                            public void onDownloadComplete(String s) {
                                                AppUtil.sendNote(null);
                                                updateLocker = false;
                                                BDAutoUpdateSDK.cpUpdateInstall(activity, s);
                                            }

                                            @Override
                                            public void onFail(Throwable throwable, String s) {
                                                AppUtil.sendNote(null);
                                                updateLocker = false;
                                                AndTools.showToast(activity, "更新失败");
                                            }

                                            @Override
                                            public void onStop() {
                                            }
                                        });
                                    } else {
                                        BDAutoUpdateSDK.cpUpdateInstall(activity, appUpdateInfoForInstall.getInstallPath());
                                        updateLocker = false;
                                    }
                                }

                                @Override
                                public void onIgnoreUpdate() {//忽略更新
                                    ShareTool.saveText(activity, SKIP_SAVE_PATH, newVersion);
                                }

                                @Override
                                public void onClose() {//关闭
                                    updateLocker = false;
                                }
                            });
                }
            }
        });
    }

    /**
     * 清除缓存
     */
    private void clearCatch() {
        new AlertDialog.Builder(MainSet.this).setTitle("清除缓存？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FileTool.clearCatch();//会保留头像
                        showToast("缓存已清空。");
                    }
                }).setNegativeButton("取消", null).create().show();
    }

    private void changeFront() {
        font_index = FontTool.getIndex(MainSet.this);
        new AlertDialog.Builder(MainSet.this)
                .setTitle("切换字体")
                .setSingleChoiceItems(new String[]{"默认", "可爱"}, font_index,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                font_index = which;
                            }
                        })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        restartActivity();
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void restartActivity() {
        ////////////////////////为删减应用，已设置界面不可见，并删除了字体文件
        ////////////////////////需要启用，请在assets下放置font_cute.ttf文件，并在Application中配置
        if (FontTool.getIndex(MainSet.this) == font_index) return;
        FontTool.saveIndex(MainSet.this, font_index);
        new AlertDialog.Builder(MainSet.this)
                .setTitle("重启高能？")
                .setMessage("切换字体需要重启高能。现在重启？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FontTool.dealFont(MainSet.this);
                        Intent intent = new Intent(MainSet.this,
                                MainMain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("新字体将在下次启动时应用");
                    }
                }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("更多界面");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("更多界面");
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tab05_btn_font:
                changeFront();
                break;
            case R.id.tab05_btn_version:
                checkNewVersion(this, false);
                break;
            case R.id.tab05_btn_feedback:
                intent = new Intent(MainSet.this, FeedBack.class);
                MainSet.this.startActivity(intent);
                break;
            case R.id.tab05_btn_cleancache:
                clearCatch();
                break;
            case R.id.tab05_btn_aboutme:
                intent = new Intent(MainSet.this, AboutMe.class);
                MainSet.this.startActivity(intent);
                break;
            case R.id.tab05_btn_logout:
                logout();
                break;
//            case R.id.tab05_btn_exit:
//                MainSet.this.finish();
//                break;
            // 推送相关
            case R.id.tab05_btn_msg_voice:
                tbtn_msg_voice.setChecked(!tbtn_msg_voice.isChecked());
            case R.id.tab05_tbtn_msg_voice:
                BaiChuanUtils.setSoundState(tbtn_msg_voice.isChecked());
                break;
            case R.id.tab05_btn_msg_verb:
                tbtn_msg_voice.setChecked(!tbtn_msg_voice.isChecked());
            case R.id.tab05_tbtn_msg_verb:
                BaiChuanUtils.setVibrateState(tbtn_msg_verb.isChecked());
                break;
            case R.id.tab05_btn_push_collect:
                tbtn_push_collect.setChecked(!tbtn_push_collect.isChecked());
            case R.id.tab05_tbtn_push_collect:
                BaiChuanUtils.setNoteState(tbtn_push_collect.isChecked());
                break;
        }
    }

    private void logout() {
        if (UserStateTool.isLoginEver(this)) {
            new AlertDialog.Builder(this)
                    .setTitle("注销登录？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    setResult(LOGOUT_CODE);
                                    UserStateTool.logout(MainSet.this);
                                }
                            }).setNegativeButton("取消", null).create().show();
        } else {
            showToast("您尚未登录");
            UserStateTool.logout(this);
            UserStateTool.goToLogin(this, false);
            this.finish();
        }
    }

    @Override
    public void showContextMenu() {
    }
}
