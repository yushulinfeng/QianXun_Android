package com.szdd.qianxun.message.info;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectEasy;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.top.TActivity;
import com.szdd.qianxun.tools.views.RotateTextView;
import com.szdd.qianxun.tools.views.RoundHeadView;
import com.szdd.qianxun.tools.views.SpringScrollView;
import com.szdd.qianxun.tools.views.SpringScrollView.OverScrollTinyListener;

public class ShowAllInfo extends TActivity implements OverScrollTinyListener {
    public static final int CODE_ALTER_INFO = 1;
    private RotateTextView hobby1, hobby2, hobby3, hobby4;
    private RoundHeadView head_icon;
    private TextView tv_name;
    private ImageView iv_sex;
    private Button btn_watch, btn_chart;
    private TextView tv_credit, tv_account, tv_rank, tv_sign,
            tv_birthday, tv_star, tv_location, tv_school, tv_work;
    private long user_id = 0;
    private String sign = "", hobby = "", work = "", school = "";
    private int credit_state = 0, credit_rank = 0;
    private AnBaseInfo base_info = null;
    private AnExtendInfo extend_info = null;
    // 顶部缩放效果
    private static final int PADDING = -100;
    private ImageView mHeaderImage;
    private LinearLayout top_layout;
    private TextView top_title;

    @Override
    public void onCreate() {// 专用的TActivity
        setContentView(R.layout.msg_show_all_info);
        initActionBar(getResources().getColor(R.color.no_color));
        showBackButton();

        initMessage();
        initView();
        initHeadView();
        loadUserData();
    }

    private void initMessage() {
        Intent intent = getIntent();
        if (intent != null) {
            user_id = intent.getLongExtra("user_id", 0L);
        } else {
            user_id = 0;
        }
    }

    private void initView() {
        hobby1 = (RotateTextView) findViewById(R.id.personalMusic);
        hobby2 = (RotateTextView) findViewById(R.id.personalVideo);
        hobby3 = (RotateTextView) findViewById(R.id.personalMovie);
        hobby4 = (RotateTextView) findViewById(R.id.personalMode);
        hobby1.setDegrees(330);
        hobby2.setDegrees(340);
        hobby3.setDegrees(20);
        hobby4.setDegrees(30);
        hobby1.setText("");
        hobby2.setText("");
        hobby3.setText("");
        hobby4.setText("");
        tv_name = (TextView) findViewById(R.id.show_info_tv_name);
        iv_sex = (ImageView) findViewById(R.id.show_info_iv_sex);
        btn_watch = (Button) findViewById(R.id.show_info_btn_watch);
        btn_chart = (Button) findViewById(R.id.show_info_btn_chart);
        head_icon = (RoundHeadView) findViewById(R.id.ad_picture_user_head);

        tv_credit = (TextView) findViewById(R.id.show_info_tv_credit);
        tv_account = (TextView) findViewById(R.id.show_info_tv_account);
        tv_rank = (TextView) findViewById(R.id.show_info_tv_rank);
        tv_sign = (TextView) findViewById(R.id.show_info_tv_sign);
        tv_birthday = (TextView) findViewById(R.id.show_info_tv_birthday);
        tv_star = (TextView) findViewById(R.id.show_info_tv_star);
        tv_location = (TextView) findViewById(R.id.show_info_tv_location);
        tv_school = (TextView) findViewById(R.id.show_info_tv_school);
        tv_work = (TextView) findViewById(R.id.show_info_tv_work);

        btn_watch.setVisibility(View.GONE);
        btn_watch.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (user_id == 0) {// 修改个人资料
                    alterMyInfo();
                } else {// 关注对方，这个不能取消关注（已关注的不能点此取消）
                    btn_watch.setEnabled(false);
                    //FriendList.collectFriend(user_id, "");// 暂时先用""
                    btn_watch.setText("已关注");
                }
            }
        });
        btn_chart.setText("修改资料");
        btn_chart.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (user_id != 0) {
                    MsgPublicTool.chartTo(ShowAllInfo.this, user_id);
                    finish();
                } else {// 修改资料
                    alterMyInfo();
                }
            }
        });
        if (user_id != 0) {// 如果不是自己，就隐藏
            LinearLayout chart_layout = (LinearLayout) findViewById(R.id.show_info_layout_chart);
            chart_layout.setVisibility(View.GONE);
        }
    }

    private void alterMyInfo() {
        Intent intent = new Intent(ShowAllInfo.this, AlterAllInfo.class);
        intent.putExtra("sign", sign);
        intent.putExtra("hobby", hobby);
        intent.putExtra("work", work);
        intent.putExtra("school", school);
        intent.putExtra("credit_state", credit_state);
        startActivityForResult(intent, 0);
    }

    private void loadDataForView() {
        if (user_id == 0) {
            btn_watch.setText("修改资料");
        }
        tv_rank.setText(StaticMethod.changeLevel(credit_rank) + "级");
        switch (credit_state) {
            case 1:
            case -2:
                tv_credit.setText("实名");
                break;
            case 2:
                tv_credit.setText("学生");
                break;
            default:
                tv_credit.setText("未认证");
                break;
        }
        if (base_info != null) {
            tv_account.setText(base_info.getUserId());
            tv_name.setText(base_info.getNickName());
            iv_sex.setImageResource(base_info.getGender().equals("女") ? R.drawable.girl : R.drawable.boy);
            tv_birthday.setText(base_info.getBirthday());
            tv_star.setText(InfoTool.getStar(base_info.getBirthday()));
            tv_location.setText(base_info.getLocation());
            StaticMethod.BITMAPHEAD(this, ServerURL.getIP() + base_info.getHeadIconNetPath(), head_icon);
        }
        if (extend_info != null) {
            tv_sign.setText(extend_info.getSign());
            tv_work.setText(extend_info.getWork());
            String hobbys = extend_info.getHobby();
            if (!hobbys.equals("") && hobbys.contains("，")) {
                String[] hobby_strs = hobbys.split("，");
                int length_temp = hobby_strs.length;
                switch (length_temp) {// 顺序2314，美观
                    default:// 少了退出，多了选前四个
                        if (length_temp <= 0)
                            break;
                    case 4:
                        hobby4.setText(hobby_strs[3]);
                    case 3:
                        hobby1.setText(hobby_strs[2]);
                    case 2:
                        hobby3.setText(hobby_strs[1]);
                    case 1:
                        hobby2.setText(hobby_strs[0]);
                }
            }
        }
    }


    // 加载拓展信息（自己、他人均可，还是要分开处理，因为别人的其他信息也在这里面）
    private void loadUserData() {
        ConnectEasy.POST(this, ServerURL.BASE_INFO, new ConnectListener() {
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            public ConnectList setParam(ConnectList list) {
                list.put("username", (user_id == 0 ?
                        InfoTool.getUserName(ShowAllInfo.this) : user_id) + "");
                return list;
            }

            public void onResponse(String response) {
                if (response == null || response.equals("") || response.equals("failed")) {
                    showToast("网络错误");
                } else {
                    try {
                        AnUserInfo info = new Gson().fromJson(response, AnUserInfo.class);
                        base_info = new AnBaseInfo(info.getUsername() + "",
                                info.getNickName(), info.getGender(),
                                info.getBirthday(), info.getAddress(),
                                null);
                        base_info.setHeadIconLocalPath(info.getHeadIcon());
                        sign = info.getSign();
                        hobby = info.getHobby();
                        work = info.getJob();
                        school = info.getSchool();
                        base_info.setHeadIconNetPath(info.getHeadIcon());
                        sign = sign.equals(AlterAllInfo.INFO_EMPTY) ? "" : sign;
                        hobby = hobby.equals(AlterAllInfo.INFO_EMPTY) ? ""
                                : hobby;
                        work = work.equals(AlterAllInfo.INFO_EMPTY) ? "" : work;
                        school = school.equals(AlterAllInfo.INFO_EMPTY) ? "" : school;
                        tv_school.setText(school);

                        extend_info = new AnExtendInfo(sign, hobby, work);

                        credit_state = info.getVerifyStatus();
                        credit_rank = info.getRank_credit();

                        String image_url = ServerURL.getIP() + info.getHomePageBackgroundImage();
                        StaticMethod.UBITMAP(image_url, mHeaderImage);
                        loadDataForView();
                    } catch (Exception e) {
                        showToast("系统错误");
                    }
                }
            }
        });

    }

    // ////////// 顶部缩放效果//////////
    private void initHeadView() {
        top_layout = ((LinearLayout) findViewById(R.id.top_layout));
        mHeaderImage = (ImageView) findViewById(R.id.show_info_iv_bg);
        top_title = (TextView) findViewById(R.id.top_text);
        top_title.setText("");
        SpringScrollView scrollView = (SpringScrollView) findViewById(R.id.show_info_scroll);
        scrollView.setOverScrollTinyListener(this);
        scrollLoosen();
    }

    @Override
    public void showBackButton() {
        Button top_back_text = ((Button) findViewById(R.id.top_back_text));
        ImageButton top_back = (ImageButton) findViewById(R.id.top_back);
        OnClickListener back_lis = new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        };
        top_back.setOnClickListener(back_lis);
        top_back_text.setOnClickListener(back_lis);
    }

    @Override
    public void scrollDistance(int tinyDistance, int totalDistance) {
        // 达到边界后的位移。上边界<0,下边界>0。
        if (totalDistance > 0 || tinyDistance == 0
                || mHeaderImage.getPaddingBottom() == 0) {
            if (totalDistance > 0) {
                top_layout.setBackgroundColor(getResources().getColor(
                        R.color.topbar_bg));
                top_title.setText("个人资料");
            }
            return;
        }
        int padding = PADDING - totalDistance / 2;
        if (padding > 0) {
            padding = 0;
        }
        mHeaderImage.setPadding(padding, 0, padding, padding);
        top_layout
                .setBackgroundColor(getResources().getColor(R.color.no_color));
        top_title.setText("");
    }

    @Override
    public void scrollLoosen() {
        mHeaderImage.setPadding(PADDING, 0, PADDING, PADDING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 1) {
            loadUserData();
            setResult(CODE_ALTER_INFO);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showContextMenu() {
    }
}
