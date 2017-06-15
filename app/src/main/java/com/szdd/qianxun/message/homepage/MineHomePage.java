package com.szdd.qianxun.message.homepage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.tools.PxAndDip;
import com.szdd.qianxun.message.info.AlterAllInfo;
import com.szdd.qianxun.message.info.AnBaseInfo;
import com.szdd.qianxun.message.info.AnUserInfo;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.start.register.Register3;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.bitmap.BitmapListener;
import com.szdd.qianxun.tools.bitmap.BitmapTool;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectEasy;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.FileTool;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;
import com.szdd.qianxun.tools.views.slidepage.ScrollableLayout;
import com.szdd.qianxun.tools.views.slidepage.SimpleSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by linorz on 2016/3/25.
 */
public class MineHomePage extends FragmentActivity implements View.OnClickListener {
    private Context context;//上下文
    private Button btn_attention;//关注按钮
    private LinearLayout lt_chat;//聊天按钮的布局
    private ImageView mine_background,//背景图片
            mine_sex;//性别的图片
    private ImageView mine_head;//头像
    private TextView mine_name,//姓名
            real_name,//实名认证
            real_student;//学生认证
    private ImageView mine_tab;//滑动条
    private ViewPager mine_viewPager;//界面的viewpager
    private ScrollableLayout mine_sv;//整体滑动
    private SimpleSwipeRefreshLayout mine_refresh;//滑动刷新
    private String userId;
    private long userName = 0;
    private Dialog method_dialog = null;// 选择条用相机或者相册的对话框
    private final int PHOTOHRAPH = 1;// 拍照
    private final int PHOTOZOOM = 2; // 缩放
    private final int PHOTORESOULT = 3;// 结果
    private File picture = null;
    private File headIcon = null;
    private String picture_path = "";// 图片的绝对路径
    private int picOrIcon;
    private int verifyStatus;
    boolean isReport = false;
    private ArrayList<ScrollAbleFragment> fragmentList;
    private PopupMenu popup;
    private Uri uritempFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);//此处需要null，不然内部的处理会报错，与MineHomePage有关
        setContentView(R.layout.msg_mine_homepage_show);
        context = this;
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        if (userId == null) userId = "0";
        uritempFile = Register3.getTempUri();
        initAllView();//初始化界面组建
        initPopup();
        mine_refresh = (SimpleSwipeRefreshLayout) findViewById(R.id.mine_homepage_swipelayout);
        //刷新监听
        mine_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                if (fragmentList != null && fragmentList.size() != 0) fragmentList.clear();
                getPersonalDetail();
                getAllAttention();
            }
        });
        getPersonalDetail();
        getAllAttention();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initAllView() {
        //标题栏
        findViewById(R.id.mine_homepage_back).setOnClickListener(this);//返回
        findViewById(R.id.mine_homepage_threeline).setOnClickListener(this);//菜单
        lt_chat = (LinearLayout) findViewById(R.id.mine_homepage_chat);//聊天
        lt_chat.setEnabled(false);
        btn_attention = (Button) findViewById(R.id.mine_homepage_attention);//关注

        //主页上方
        mine_sv = (ScrollableLayout) findViewById(R.id.mine_homepage_sv);//整体的滑动
        mine_background = (ImageView) findViewById(R.id.mine_homepage_background);//背景图片
        mine_head = (ImageView) findViewById(R.id.mine_homepage_head);//头像
        mine_name = (TextView) findViewById(R.id.mine_homepage_name);//昵称
        mine_sex = (ImageView) findViewById(R.id.mine_homepage_sex);//性别
        real_name = (TextView) findViewById(R.id.mine_homepage_realname);//实名认证
        real_student = (TextView) findViewById(R.id.mine_homepage_realstudent);//学生认证
        //主页下方
        Button btn_service = (Button) findViewById(R.id.mine_homepage_service_btn);
        Button btn_personal = (Button) findViewById(R.id.mine_homepage_personal_btn);
        Button btn_request = (Button) findViewById(R.id.mine_homepage_request_btn);
        btn_service.setOnClickListener(this);//加载监听
        btn_personal.setOnClickListener(this);//加载监听
        btn_request.setOnClickListener(this);//加载监听

        mine_tab = (ImageView) findViewById(R.id.mine_homepage_tab);//滑动条
        mine_viewPager = (ViewPager) findViewById(R.id.mine_homepage_viewpager);//viewpager内容
        if (userId.equals(InfoTool.getUserID(context))) {
            Button alterButton = (Button) findViewById(R.id.mine_homepage_alter);
            alterButton.setVisibility(View.VISIBLE);
            alterButton.setOnClickListener(this);
            initDialog();
            mine_head.setOnClickListener(this);
        } else findViewById(R.id.mine_homepage_layout).setVisibility(View.VISIBLE);

        //预先加载之前的缓存
        AnBaseInfo base_info = InfoTool.getBaseInfo(this);// 已确保不会返回null
        String user_id = base_info.getUserId();
        String nickname = null;
        Bitmap bitmap = null;
        if (null != user_id && !"".equals(user_id)) {// 本地有基本信息，就同步
            if (nickname == null)
                nickname = base_info.getNickName();
            if (nickname == null || nickname.equals(""))
                nickname = "加载中……";
            mine_name.setText(nickname);
            if (bitmap == null)
                bitmap = base_info.getHeadIcon();
            if (bitmap != null)
                mine_head.setImageBitmap(bitmap);
        }
    }

    public void initViewPager(String birthday, String introduce, String sex, String location) {
        fragmentList = new ArrayList<>();
        //fragmentList.add(new MineService(userId));//服务内容
        fragmentList.add(new MineDynamic(userId, birthday, introduce, sex, location));//个人详情和动态内容
        fragmentList.add(new MineRequest(userId));//需求内容

        HomePageAdapter mine_pagerAdapter = new HomePageAdapter(getSupportFragmentManager(), fragmentList);
        mine_viewPager.setAdapter(mine_pagerAdapter);//设置适配器
        mine_viewPager.setOffscreenPageLimit(3);
        //viewpager滑动监听
        final LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) mine_tab.getLayoutParams();
        mine_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ll.leftMargin = PxAndDip.dipTopx(context, 60) * position
                        + (int) (PxAndDip.dipTopx(context, 60) * positionOffset);
                mine_tab.setLayoutParams(ll);//滑动条滑动
            }

            @Override
            public void onPageSelected(int position) {
                mine_sv.getHelper().setCurrentScrollableContainer(fragmentList.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mine_viewPager.setCurrentItem(1);//默认为个人界面
        mine_refresh.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_homepage_service_btn:
                mine_viewPager.setCurrentItem(0);//跳转服务界面
                break;
            case R.id.mine_homepage_personal_btn:
                mine_viewPager.setCurrentItem(1);//跳转个人界面
                break;
            case R.id.mine_homepage_request_btn:
                mine_viewPager.setCurrentItem(2);//跳转需求界面
                break;
            case R.id.mine_homepage_threeline:
                if (userId.equals(InfoTool.getUserID(context)))
                    MsgPublicTool.showAllInfo(MineHomePage.this, 0);
                else
                    MsgPublicTool.showAllInfo(MineHomePage.this, userName);
                //popup.show();
                break;
            case R.id.mine_homepage_alter:
                intentAlterAllInfo();
                break;
            case R.id.mine_homepage_chat:
                MsgPublicTool.chartTo(context, Integer.parseInt(userId), userName);
                break;
            case R.id.mine_homepage_head:
                picOrIcon = 1;
                method_dialog.show();
                break;
            case R.id.mine_homepage_back:
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    public void getAllAttention() {
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
                if (response == null) showToast("网络请求失败");
                else {
                    try {
                        JSONObject jo = new JSONObject(response);
                        JSONArray ja = jo.getJSONArray("list");
                        boolean isAttention = false;
                        for (int i = 0; i < ja.length(); i++) {
                            if (userId.equals(ja.getJSONObject(i).getString("id"))) {
                                isAttention = true;
                                break;
                            }
                        }
                        if (!userId.equals(InfoTool.getUserID(context)))
                            changeAttention(!isAttention);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getPersonalDetail() {
        StaticMethod.POST(context, ServerURL.HOMEPAGE_PERSONAL_DETAIL, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", userId);
                return list;
            }

            @Override
            public void onResponse(String response) {
                if (response == null) showToast("网络请求失败");
                else {
                    try {
                        JSONObject detailJson = new JSONObject(response);
                        mine_name.setText(detailJson.getString("nickName"));
                        String sex = detailJson.getString("gender");
                        userName = detailJson.getLong("username");
                        lt_chat.setEnabled(true);
                        lt_chat.setOnClickListener(MineHomePage.this);//加载监听
                        if (sex.equals("男")) mine_sex.setImageResource(R.drawable.boy);
                        else mine_sex.setImageResource(R.drawable.girl);
                        String birthday = detailJson.getString("birthday");
                        String headIconUrl = detailJson.getString("headIcon");
                        String location = detailJson.getString("address");
                        String introduce = detailJson.getString("sign");
                        verifyStatus = detailJson.getInt("verifyStatus");
                        if (verifyStatus >= 1) {
                            real_name.setText("已实名认证");
                            real_name.setBackgroundResource(R.drawable.mine_homepage_btn_shape1);
                        }
                        if (verifyStatus == 2) {
                            real_student.setText("已学生认证");
                            real_student.setBackgroundResource(R.drawable.mine_homepage_btn_shape2);
                        }
                        int rank = detailJson.getInt("rank");//等级
                        setOranges(StaticMethod.changeLevel(rank));

                        StaticMethod.UBITMAPHEAD(ServerURL.getIP() + headIconUrl, new BitmapListener() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                if (bitmap != null)
                                    mine_head.setImageBitmap(bitmap);
                            }
                        });
                        StaticMethod.UBITMAP(ServerURL.getIP() + detailJson.getString("homePageBackgroundImage"),
                                new BitmapListener() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        if (bitmap != null) mine_background.setImageBitmap(bitmap);
                                        else
                                            mine_background.setImageResource(R.drawable.bg_first_use);
                                        if (userId.equals(InfoTool.getUserID(context)))
                                            mine_background.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    picOrIcon = 2;
                                                    method_dialog.show();
                                                }
                                            });//加载监听
                                    }
                                });
                        initViewPager(birthday, introduce, sex, location);//初始化3个界面
                    } catch (JSONException e) {
                        showToast("网络请求失败");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }
        });
    }

    public void sendAttention() {
        StaticMethod.POST(context, ServerURL.HOMEPAGE_ATTENTION, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("id", InfoTool.getUserID(context));
                    jo.put("concernedId", userId);
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
                if (response == null) showToast("网络请求失败");
                else {
                    switch (response) {
                        case "1":
                            showToast("关注成功");
                            changeAttention(false);
                            break;
                        case "-1":
                            showToast("失败");
                            break;
                        case "-2":
                            showToast("服务器出错");
                            break;
                        case "-3":
                            showToast("已经关注");
                            break;
                        case "-4":
                            showToast("不能关注自己");
                            break;
                    }
                }
            }
        });
    }

    public void cancelAttention() {
        StaticMethod.POST(context, ServerURL.HOMEPAGE_CANCEL_ATTENTION, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("id", InfoTool.getUserID(context));
                    jo.put("concernedId", userId);
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
                if (response == null) showToast("网络请求失败");
                else {
                    switch (response) {
                        case "1":
                            showToast("取消关注");
                            changeAttention(true);
                            break;
                        case "-1":
                            showToast("失败");
                            break;
                        case "-2":
                            showToast("服务器出错");
                            break;
                        case "-3":
                            showToast("未关注");
                            break;
                    }
                }
            }
        });
    }

    public void changeAttention(boolean change) {
        if (change) {
            btn_attention.setBackgroundResource(R.color.homepage_background_yellow);
            btn_attention.setTextColor(getResources().getColor(R.color.black));
            btn_attention.setText("关注");
            btn_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendAttention();
                }
            });
        } else {
            btn_attention.setBackgroundResource(R.color.homepage_text_gray);
            btn_attention.setTextColor(getResources().getColor(R.color.white));
            btn_attention.setText("取消关注");
            btn_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelAttention();
                }
            });
        }
    }

    //设置橙子的数量
    private void setOranges(int num) {
        ImageView[] oranges = new ImageView[5];
        oranges[0] = (ImageView) findViewById(R.id.mine_homepage_o1);
        oranges[1] = (ImageView) findViewById(R.id.mine_homepage_o2);
        oranges[2] = (ImageView) findViewById(R.id.mine_homepage_o3);
        oranges[3] = (ImageView) findViewById(R.id.mine_homepage_o4);
        oranges[4] = (ImageView) findViewById(R.id.mine_homepage_o5);
        if (num > 0) findViewById(R.id.mine_homepage_level).setVisibility(View.VISIBLE);
        for (int i = 0; i < num; i++) oranges[i].setVisibility(View.VISIBLE);
    }

    public void alterBackground() {
        StaticMethod.POST(context, ServerURL.ALTER_BACKGROUND, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", InfoTool.getUserID(context));
                list.putFile("backgroundImage", picture);
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                switch (response) {
                    case "1":
                        showToast("修改成功");
                        break;
                    case "-1":
                        showToast("修改失败");
                        break;
                }
            }
        });
    }

    public void alterHeadIcon() {
        StaticMethod.POST(context, ServerURL.ALTER_HEADICON, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", InfoTool.getUserID(context));
                list.putFile("headIcon", headIcon);
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                switch (response) {
                    case "1":
                        showToast("修改成功");
                        break;
                    case "-1":
                        showToast("修改失败");
                        break;
                }
            }
        });
    }

    /**
     * 初始化选择添加图片方式的对话框
     */
    private void initDialog() {
        String[] context_items = new String[]{"相机拍照", "相册选取"};
        picture_path = new File(FileTool.getBaseSDCardPath(),
                System.currentTimeMillis() + ".jpg").getAbsolutePath();
        method_dialog = new AlertDialog.Builder(this).setTitle("请选择方式	")
                .setItems(context_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// 相机拍照
                                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent2.putExtra("output", uritempFile);
                                intent2.putExtra("outputFormat", "JPEG");//返回格式
                                startActivityForResult(intent2, PHOTOHRAPH);
                                break;
                            case 1:// 选择相册
                                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent1, PHOTOZOOM);
                                break;
                        }
                    }
                }).create();
        method_dialog.setCanceledOnTouchOutside(true);
    }

    /**
     * 返回启动activity的结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PHOTOZOOM:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }
                break;
            case PHOTOHRAPH:
                if (resultCode == RESULT_OK) {
                    cropPhoto(uritempFile);// 裁剪图片
                }
                break;
            case PHOTORESOULT:
                if (resultCode != RESULT_OK)
                    return;
                //将Uri图片转换为Bitmap
                Bitmap photo = null;
                try {
                    photo = BitmapFactory.decodeStream(
                            getContentResolver().openInputStream(uritempFile));
                    if (photo == null) return;
                } catch (Exception e) {
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                BitmapTool.writeToFile(photo, "jpg", picture_path);
                if (picOrIcon == 1) {
                    headIcon = new File(picture_path);
                    mine_head.setImageBitmap(photo);
                    photo = BitmapTool.zipBitmap(photo, 300, 300, true);// 压缩图片使之小于等于300*300
                    photo = BitmapTool.getCroppedRoundBitmap(photo);// 处理为圆角
                    BitmapTool.writeToFile(photo, "png", Register3.getIconPath());
                    alterHeadIcon();//上传
                } else if (picOrIcon == 2) {
                    picture = new File(picture_path);
                    mine_background.setImageBitmap(photo);
                    alterBackground();//上传
                }
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;

        }
    }

    /**
     * 调用系统裁剪图片的方法，可以选择参数为Uri对象 或者 Bitmap对象
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Register3.cropPhoto(uri, uritempFile, this, PHOTORESOULT);
    }

    private void initPopup() {
        popup = new PopupMenu(this, findViewById(R.id.mine_homepage_threeline));
        popup.getMenuInflater().inflate(R.menu.homepage_actionbar, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.homepage_bar_item1:
                        if (userId.equals(InfoTool.getUserID(context)))
                            MsgPublicTool.showAllInfo(MineHomePage.this, 0);
                        else MsgPublicTool.showAllInfo(MineHomePage.this, userName);
                        break;
                    case R.id.homepage_bar_item2:
                        if (userId.equals(InfoTool.getUserID(context)))
                            showToast("不能举报自己");
                        else {
                            if (!isReport) {
                                showToast("举报成功");
                                isReport = true;
                            } else showToast("已经举报了");
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void intentAlterAllInfo() {
        ConnectEasy.POST(this, ServerURL.BASE_INFO, new ConnectListener() {
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            public ConnectList setParam(ConnectList list) {
                list.put("username", userName);
                return list;
            }

            public void onResponse(String response) {
                if (response == null || response.equals("") || response.equals("failed"))
                    showToast("网络错误");
                else try {
                    AnUserInfo info = new Gson().fromJson(response, AnUserInfo.class);
                    AnBaseInfo base_info = new AnBaseInfo(info.getUsername() + "",
                            info.getNickName(), info.getGender(),
                            info.getBirthday(), info.getAddress(),
                            null);
                    base_info.setHeadIconLocalPath(info.getHeadIcon());
                    String sign = info.getSign();
                    String hobby = info.getHobby();
                    String work = info.getJob();
                    String school = info.getSchool();
                    sign = sign.equals(AlterAllInfo.INFO_EMPTY) ? "" : sign;
                    hobby = hobby.equals(AlterAllInfo.INFO_EMPTY) ? "" : hobby;
                    work = work.equals(AlterAllInfo.INFO_EMPTY) ? "" : work;
                    school = school.equals(AlterAllInfo.INFO_EMPTY) ? "" : school;
                    Intent intent = new Intent(MineHomePage.this, AlterAllInfo.class);
                    intent.putExtra("sign", sign);
                    intent.putExtra("hobby", hobby);
                    intent.putExtra("work", work);
                    intent.putExtra("school", school);
                    intent.putExtra("credit_state", verifyStatus);
                    startActivity(intent);
                } catch (Exception e) {
                    showToast("系统错误");
                }
            }
        });
    }

    private void showToast(String text) {
        QianxunToast.showToast(this, text, QianxunToast.LENGTH_SHORT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}