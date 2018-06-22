package ye.chilyn.youaccounts.me.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ye.chilyn.youaccounts.AccountApplication;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.constant.SharePreferenceKey;
import ye.chilyn.youaccounts.login.LoginActivity;
import ye.chilyn.youaccounts.util.DialogUtil;
import ye.chilyn.youaccounts.util.SharePreferencesUtils;

public class ExitDialog implements View.OnClickListener {

    private Activity mActivity;
    private Dialog mDialogExit;
    private View mExitDialogView;
    private TextView mTvConfirm;

    public ExitDialog(Activity activity) {
        this.mActivity = activity;
        initDialogViews();
    }

    private void initDialogViews() {
        mExitDialogView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_confirm_exit, null);
        mTvConfirm = (TextView) mExitDialogView.findViewById(R.id.tv_confirm);
        mDialogExit = DialogUtil.createDialog(mActivity, R.id.tv_cancel, mExitDialogView, 250, 101);
        mTvConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                exitLogin();
                break;
        }
    }

    /**
     * 退出登录
     */
    public void exitLogin() {
        mDialogExit.dismiss();
        //清空登录信息
        SharePreferencesUtils.save(SharePreferenceKey.IS_LOGINED, false);
        AccountApplication.setLoginUserInfo(null);
        //跳转登录页面
        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
        mActivity.finish();
    }

    public void show() {
        mDialogExit.show();
    }
}
