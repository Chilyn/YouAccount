package ye.chilyn.youaccounts.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ye.chilyn.youaccounts.AccountApplication;
import ye.chilyn.youaccounts.MainActivity;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseActivity;
import ye.chilyn.youaccounts.base.common.CommonTextWatcher;
import ye.chilyn.youaccounts.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccounts.base.interfaces.IBaseModel;
import ye.chilyn.youaccounts.constant.HandleModelType;
import ye.chilyn.youaccounts.constant.RefreshViewType;
import ye.chilyn.youaccounts.constant.SharePreferenceKey;
import ye.chilyn.youaccounts.entity.UserBean;
import ye.chilyn.youaccounts.login.model.LoginModel;
import ye.chilyn.youaccounts.register.RegisterActivity;
import ye.chilyn.youaccounts.util.MD5Util;
import ye.chilyn.youaccounts.util.SharePreferencesUtils;
import ye.chilyn.youaccounts.util.SoftKeyboardUtil;
import ye.chilyn.youaccounts.util.ToastUtil;

/**
 * Created by Alex on 2018/1/26.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvPasswordVisibility;
    private EditText mEtAccount, mEtPassword;
    private TextView mTvClearAccount, mTvClearPassword;
    private TextView mTvRegister, mTvForgotPassword;
    private TextView mTvLogin;
    private IBaseModel mLoginModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        mIvPasswordVisibility = findView(R.id.iv_password_visibility);
        mEtAccount = findView(R.id.et_account);
        mEtPassword = findView(R.id.et_password);
        mTvClearAccount = findView(R.id.tv_clear_account);
        mTvClearPassword = findView(R.id.tv_clear_password);
        mTvRegister = findView(R.id.tv_register);
        mTvForgotPassword = findView(R.id.tv_forgot_password);
        mTvLogin = findView(R.id.tv_login);
    }

    private void initData() {
        mLoginModel = new LoginModel(mRefreshViewListener);
        String nickname = SharePreferencesUtils.getStringValue(SharePreferenceKey.NICKNAME);
        mEtAccount.setText(nickname);
        mEtAccount.setSelection(mEtAccount.getText().length());
    }

    private void setListener() {
        mIvPasswordVisibility.setOnClickListener(this);
        mEtAccount.addTextChangedListener(mAccountTextWatcher);
        mEtPassword.addTextChangedListener(mPasswordTextWatcher);
        mEtPassword.setOnEditorActionListener(mEditorActionListener);
        mTvClearAccount.setOnClickListener(this);
        mTvClearPassword.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mTvForgotPassword.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
    }

    private TextWatcher mAccountTextWatcher = new CommonTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                mTvClearAccount.setVisibility(View.VISIBLE);
            } else {
                mTvClearAccount.setVisibility(View.GONE);
            }
        }
    };

    private TextWatcher mPasswordTextWatcher = new CommonTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                mTvClearPassword.setVisibility(View.VISIBLE);
            } else {
                mTvClearPassword.setVisibility(View.GONE);
            }
        }
    };

    private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                SoftKeyboardUtil.forceCloseSoftKeyboard(mEtPassword);
                login();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_password_visibility:
                showOrHidePassword();
                break;

            case R.id.tv_clear_account:
                mEtAccount.setText(null);
                break;

            case R.id.tv_clear_password:
                mEtPassword.setText(null);
                break;

            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.tv_forgot_password:
                break;

            case R.id.tv_login:
                SoftKeyboardUtil.forceCloseSoftKeyboard(mEtPassword);
                login();
                break;
        }
    }

    /**
     * 显示或隐藏密码
     */
    private void showOrHidePassword() {
        if (mIvPasswordVisibility.isSelected()) {
            mIvPasswordVisibility.setSelected(false);
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mEtPassword.setSelection(mEtPassword.getText().length());
        } else {
            mIvPasswordVisibility.setSelected(true);
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mEtPassword.setSelection(mEtPassword.getText().length());
        }
    }

    /**
     * 登录操作
     */
    private void login() {
        String nickname = mEtAccount.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            ToastUtil.showShortToast(getString(R.string.account_can_not_be_empty));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtil.showShortToast(getString(R.string.password_can_not_be_empty));
            return;
        }

        password = MD5Util.getStringMD5(MD5Util.getStringMD5(password));
        UserBean bean = new UserBean();
        bean.setNickname(nickname);
        bean.setPassword(password);
        mLoginModel.handleModelEvent(HandleModelType.USER_LOGIN, bean);
    }

    private RefreshViewListener mRefreshViewListener = new RefreshViewListener();

    private class RefreshViewListener implements IBaseModel.OnRefreshViewListener {

        @Override
        public void onRefreshView(int refreshType, Object data) {
            mHandler.sendMessage(mHandler.obtainMessage(refreshType, data));
        }
    }

    private ViewHandler mHandler = new ViewHandler(this);

    private static class ViewHandler extends BaseStaticInnerHandler<LoginActivity> {

        public ViewHandler(LoginActivity activity) {
            super(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isReferenceRecycled()) {
                removeCallbacksAndMessages(null);
                return;
            }

            LoginActivity activity = getReference();
            switch (msg.what) {
                case RefreshViewType.USER_NOT_EXIST:
                    ToastUtil.showShortToast(activity.getString(R.string.user_not_exist));
                    break;

                case RefreshViewType.LOGIN_SUCCESS:
                    activity.onLoginSuccess((UserBean) msg.obj);
                    break;

                case RefreshViewType.LOGIN_FAIL:
                    ToastUtil.showShortToast(activity.getString(R.string.password_is_wrong));
                    break;
            }
        }
    }

    private void onLoginSuccess(UserBean bean) {
        //保存登录用户信息到SharePreference
        SharePreferencesUtils.save(SharePreferenceKey.IS_LOGINED, true);
        SharePreferencesUtils.save(SharePreferenceKey.USER_ID, bean.getUserId());
        SharePreferencesUtils.save(SharePreferenceKey.NICKNAME, bean.getNickname());
        SharePreferencesUtils.save(SharePreferenceKey.PASSWORD, bean.getPassword());
        //保存登录账户全局变量
        AccountApplication.setLoginUserInfo(bean.copy());
        //显示登录成功
        ToastUtil.showShortToast(getString(R.string.login_success));
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void destroyViews() {
    }

    @Override
    protected void releaseModels() {
        mLoginModel.onDestroy();
        mLoginModel = null;
    }
}
