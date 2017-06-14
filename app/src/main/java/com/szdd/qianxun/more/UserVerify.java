package com.szdd.qianxun.more;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.info.AlterAllInfo;
import com.szdd.qianxun.message.info.credit.RealNameCheck;
import com.szdd.qianxun.message.info.credit.RealStudentCheck;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.tools.top.TActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserVerify extends TActivity {
    @Bind(R.id.more_user_verify_id_status)
    TextView moreUserVerifyIdStatus;
    @Bind(R.id.more_user_verify_stu_status)
    TextView moreUserVerifyStuStatus;

    private int code = 0;

    @Override
    public void onCreate() {
        setContentView(R.layout.more_user_verify);
        ButterKnife.bind(this);
        setTitle("个人认证");
        showBackButton();
        initView();
        initActionBar(getResources().getColor(R.color.topbar_bg));
    }

    private void initView() {
        code = InfoTool.getUserInfo(this).getVerifyStatus();
        switch (code) {
            case -1:
                moreUserVerifyIdStatus.setText("审核中");
                break;
            case -2:
                moreUserVerifyIdStatus.setText("已认证");
                moreUserVerifyStuStatus.setText("审核中");
                break;
            case 0:
                break;
            case 1:
                moreUserVerifyIdStatus.setText("已认证");
                break;
            case 2:
                moreUserVerifyIdStatus.setText("已认证");
                moreUserVerifyStuStatus.setText("已认证");
                break;
            default:
                break;
        }
    }

    @Override
    public void showContextMenu() {

    }

    @OnClick({R.id.more_user_verify_id, R.id.more_user_verify_stu})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.more_user_verify_id:
                if (code == 0)
                    intent = new Intent(this, RealNameCheck.class);
                else if (code == -1)
                    showToast("请耐心等待审核");
                else
                    showToast("您已通过该认证");
                break;
            case R.id.more_user_verify_stu:
                if (code == 1)
                    intent = new Intent(this, RealStudentCheck.class);
                else if (code == -2)
                    showToast("请耐心等待审核");
                else if (code == 2)
                    showToast("您已通过该认证");
                else
                    showToast("请先通过身份认证");
                break;
        }
        if (intent != null) startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AlterAllInfo.CODE_ID_START ||
                resultCode == AlterAllInfo.CODE_STU_START) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
