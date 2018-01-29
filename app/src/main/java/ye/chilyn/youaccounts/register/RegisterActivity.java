package ye.chilyn.youaccounts.register;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseActivity;
import ye.chilyn.youaccounts.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccounts.base.interfaces.IBaseModel;
import ye.chilyn.youaccounts.contant.HandleModelType;
import ye.chilyn.youaccounts.contant.RefreshViewType;
import ye.chilyn.youaccounts.entity.UserBean;
import ye.chilyn.youaccounts.register.model.RegisterModel;
import ye.chilyn.youaccounts.util.DialogUtil;
import ye.chilyn.youaccounts.util.MD5Util;
import ye.chilyn.youaccounts.util.SoftKeyboardUtil;
import ye.chilyn.youaccounts.util.ToastUtil;
import ye.chilyn.youaccounts.view.TitleBarView;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TitleBarView mTitleBarView;
    private EditText mEtNickname, mEtPassword, mEtConfirmPassword;
    private ImageView mIvPasswordVisibility, mIvConfirmPasswordVisibility;
    private TextView mTvRegister;
    private IBaseModel mRegisterModel;
    private Dialog mDialogRegisterSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mTitleBarView = new TitleBarView(findView(R.id.title_bar), this);
        mTitleBarView.setRightOptionViewVisibility(false);
        mTvRegister = findView(R.id.tv_register);
        mEtNickname = findView(R.id.et_nickname);
        mEtPassword = findView(R.id.et_password);
        mIvPasswordVisibility = findView(R.id.iv_password_visibility);
        mIvConfirmPasswordVisibility = findView(R.id.iv_confirm_password_visibility);
        mEtConfirmPassword = findView(R.id.et_confirm_password);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.register_account));
        mRegisterModel = new RegisterModel(mRefreshViewListener);
    }

    private void setListener() {
        mTvRegister.setOnClickListener(this);
        mEtConfirmPassword.setOnEditorActionListener(mEditorActionListener);
        mIvPasswordVisibility.setOnClickListener(this);
        mIvConfirmPasswordVisibility.setOnClickListener(this);
    }

    private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                SoftKeyboardUtil.forceCloseSoftKeyboard(mEtConfirmPassword);
                register();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                SoftKeyboardUtil.forceCloseSoftKeyboard(mEtConfirmPassword);
                register();
                break;

            case R.id.iv_password_visibility:
                showOrHidePassword(mIvPasswordVisibility, mEtPassword);
                break;

            case R.id.iv_confirm_password_visibility:
                showOrHidePassword(mIvConfirmPasswordVisibility, mEtConfirmPassword);
                break;
        }
    }

    private void register() {
        String nickname = mEtNickname.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        String confirmPassword = mEtConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword)) {
            ToastUtil.showShortToast(getString(R.string.register_can_not_be_empty));
            return;
        }

        if (password.length() < 8 || confirmPassword.length() < 8) {
            ToastUtil.showShortToast(getString(R.string.password_must_over_eight_character));
            return;
        }

        if (!password.equals(confirmPassword)) {
            ToastUtil.showShortToast(getString(R.string.password_not_match));
            return;
        }

        password = MD5Util.getStringMD5(MD5Util.getStringMD5(password));
        UserBean bean = new UserBean();
        bean.setNickname(nickname);
        bean.setPassword(password);
        mRegisterModel.handleModelEvent(HandleModelType.REGISTER_USER, bean);
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

    private RefreshViewListener mRefreshViewListener = new RefreshViewListener();

    private class RefreshViewListener implements IBaseModel.OnRefreshViewListener {

        @Override
        public void onRefreshView(int refreshType, Object data) {
            mHandler.sendMessage(mHandler.obtainMessage(refreshType, data));
        }
    }

    private ViewHandler mHandler = new ViewHandler(this);

    private static class ViewHandler extends BaseStaticInnerHandler<RegisterActivity> {

        public ViewHandler(RegisterActivity activity) {
            super(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isReferenceRecycled()) {
                removeCallbacksAndMessages(null);
                return;
            }

            RegisterActivity activity = getReference();
            switch (msg.what) {
                case RefreshViewType.REGISTER_USER_SUCCESS:
                    String message = activity.getString(R.string.register_success) + ((UserBean)msg.obj).getNickname();
                    activity.showAlertDialog(message);
                    break;

                case RefreshViewType.REGISTER_USER_FAIL:
                    ToastUtil.showShortToast(activity.getString(R.string.register_fail));
                    break;
            }
        }
    }

    private void showAlertDialog(String msg) {
        mDialogRegisterSuccess = DialogUtil.createCommonAlertDialog(this, msg, null, getString(R.string.confirm),
                    null, mConfirmSuccessListener);
        mDialogRegisterSuccess.setCancelable(false);
        mDialogRegisterSuccess.setCanceledOnTouchOutside(false);
        mDialogRegisterSuccess.show();
    }

    private DialogInterface.OnClickListener mConfirmSuccessListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            finish();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        SoftKeyboardUtil.forceCloseSoftKeyboard(mEtNickname);
    }

    @Override
    protected void onDestroy() {
        mHandler.clearReference();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void destroyViews() {
    }

    @Override
    protected void releaseModels() {
        mRegisterModel.onDestroy();
        mRegisterModel = null;
    }
}
