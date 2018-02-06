package ye.chilyn.youaccounts.me.modifypassword;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypy.eventbus.EventBus;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseActivity;
import ye.chilyn.youaccounts.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccounts.base.interfaces.IBaseModel;
import ye.chilyn.youaccounts.constant.EventType;
import ye.chilyn.youaccounts.constant.HandleModelType;
import ye.chilyn.youaccounts.constant.RefreshViewType;
import ye.chilyn.youaccounts.entity.UserBean;
import ye.chilyn.youaccounts.me.model.ModifyModel;
import ye.chilyn.youaccounts.util.DialogUtil;
import ye.chilyn.youaccounts.util.MD5Util;
import ye.chilyn.youaccounts.util.SoftKeyboardUtil;
import ye.chilyn.youaccounts.util.ToastUtil;
import ye.chilyn.youaccounts.view.TitleBarView;

public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener {

    /**标题栏*/
    private TitleBarView mTitleBarView;
    private EditText mEtOldPassword, mEtNewPassword, mEtConfirmPassword;
    private ImageView mIvOldPwdVisibility, mIvNewPwdVisibility, mIvConfirmPwdVisibility;
    private TextView mTvModify;
    private IBaseModel mModifyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mTitleBarView = new TitleBarView(findView(R.id.title_bar), this);
        mTitleBarView.setRightOptionViewVisibility(false);
        mEtOldPassword = findView(R.id.et_old_password);
        mEtNewPassword = findView(R.id.et_new_password);
        mEtConfirmPassword = findView(R.id.et_confirm_password);
        mIvOldPwdVisibility = findView(R.id.iv_old_password_visibility);
        mIvNewPwdVisibility = findView(R.id.iv_new_password_visibility);
        mIvConfirmPwdVisibility = findView(R.id.iv_confirm_password_visibility);
        mTvModify = findView(R.id.tv_modify);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.modify_password));
        mModifyModel = new ModifyModel(mRefreshViewListener);
    }

    private void setListener() {
        mIvOldPwdVisibility.setOnClickListener(this);
        mIvNewPwdVisibility.setOnClickListener(this);
        mIvConfirmPwdVisibility.setOnClickListener(this);
        mTvModify.setOnClickListener(this);
        mEtConfirmPassword.setOnEditorActionListener(mEditorActionListener);
    }

    private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                SoftKeyboardUtil.forceCloseSoftKeyboard(mEtConfirmPassword);
                modifyPassword();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_old_password_visibility:
                showOrHidePassword(mIvOldPwdVisibility, mEtOldPassword);
                break;

            case R.id.iv_new_password_visibility:
                showOrHidePassword(mIvNewPwdVisibility, mEtNewPassword);
                break;

            case R.id.iv_confirm_password_visibility:
                showOrHidePassword(mIvConfirmPwdVisibility, mEtConfirmPassword);
                break;

            case R.id.tv_modify:
                SoftKeyboardUtil.forceCloseSoftKeyboard(mEtConfirmPassword);
                modifyPassword();
                break;
        }
    }

    private void showOrHidePassword(ImageView ivPasswordVisibility, EditText editText) {
        if (ivPasswordVisibility.isSelected()) {
            ivPasswordVisibility.setSelected(false);
            editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setSelection(editText.getText().length());
        } else {
            ivPasswordVisibility.setSelected(true);
            editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setSelection(editText.getText().length());
        }
    }

    private void modifyPassword() {
        String oldPassword = mEtOldPassword.getText().toString().trim();
        String newPassword = mEtNewPassword.getText().toString().trim();
        String confirmPassword = mEtConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) ||
                TextUtils.isEmpty(confirmPassword)) {
            ToastUtil.showShortToast(getString(R.string.password_can_not_be_empty));
            return;
        }

        //密码长度不够
        if (oldPassword.length() < 8 || newPassword.length() < 8 || confirmPassword.length() < 8) {
            ToastUtil.showShortToast(getString(R.string.password_must_over_eight_character));
            return;
        }

        //旧密码不正确
        UserBean loginUserBean = AccountsApplication.getLoginUserInfo();
        oldPassword = MD5Util.getStringMD5(MD5Util.getStringMD5(oldPassword));
        if (!loginUserBean.getPassword().equals(oldPassword)) {
            ToastUtil.showShortToast(getString(R.string.old_password_not_correct));
            return;
        }

        //新密码不匹配
        if (!newPassword.equals(confirmPassword)) {
            ToastUtil.showShortToast(getString(R.string.new_password_not_match));
            return;
        }

        newPassword = MD5Util.getStringMD5(MD5Util.getStringMD5(newPassword));
        loginUserBean.setPassword(newPassword);
        mModifyModel.handleModelEvent(HandleModelType.MODIFY_USER_PASSWORD, loginUserBean);
    }

    private RefreshViewListener mRefreshViewListener = new RefreshViewListener();

    private class RefreshViewListener implements IBaseModel.OnRefreshViewListener {

        @Override
        public void onRefreshView(int refreshType, Object data) {
            mHandler.sendMessage(mHandler.obtainMessage(refreshType, data));
        }
    }

    private ViewHandler mHandler = new ViewHandler(this);

    private static class ViewHandler extends BaseStaticInnerHandler<ModifyPasswordActivity> {

        public ViewHandler(ModifyPasswordActivity activity) {
            super(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isReferenceRecycled()) {
                removeCallbacksAndMessages(null);
                return;
            }

            ModifyPasswordActivity activity = getReference();
            switch (msg.what) {
                case RefreshViewType.MODIFY_PASSWORD_SUCCESS:
                    activity.showModifySuccessDialog();
                    break;

                case RefreshViewType.MODIFY_PASSWORD_FAIL:
                    ToastUtil.showShortToast(activity.getString(R.string.modify_password_fail));
                    break;
            }
        }
    }

    private void showModifySuccessDialog() {
        AlertDialog dialog = DialogUtil.createCommonAlertDialog(this, getString(R.string.password_has_been_modified),
                null, getString(R.string.confirm), null, mConfirmListener);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private DialogInterface.OnClickListener mConfirmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            EventBus.getDefault().post(EventType.MODIFY_PASSWORD_SUCCESS);
            finish();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        SoftKeyboardUtil.forceCloseSoftKeyboard(mEtConfirmPassword);
    }

    @Override
    protected void destroyViews() {
    }

    @Override
    protected void releaseModels() {
        mModifyModel.onDestroy();
        mModifyModel = null;
    }
}
