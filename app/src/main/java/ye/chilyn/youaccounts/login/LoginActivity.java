package ye.chilyn.youaccounts.login;

import android.content.Intent;
import android.os.Bundle;
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

import ye.chilyn.youaccounts.MainActivity;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseActivity;
import ye.chilyn.youaccounts.base.CommonTextWatcher;
import ye.chilyn.youaccounts.base.interfaces.IBaseModel;

/**
 * Created by Alex on 2018/1/26.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvPasswordVisibility;
    private EditText mEtAccount, mEtPassword;
    private TextView mTvClearAccount, mTvClearPassword;
    private TextView mTvRegister, mTvForgotPassword;
    private TextView mTvLogin;
    // TODO: 2018/1/26
    private IBaseModel mUserSqlModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        setListener();
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
                break;

            case R.id.tv_forgot_password:
                break;

            case R.id.tv_login:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    private void showOrHidePassword() {
        if (mIvPasswordVisibility.isSelected()) {
            mIvPasswordVisibility.setSelected(false);
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            mIvPasswordVisibility.setSelected(true);
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    @Override
    protected void destroyViews() {
    }

    @Override
    protected void releaseModels() {
    }
}
