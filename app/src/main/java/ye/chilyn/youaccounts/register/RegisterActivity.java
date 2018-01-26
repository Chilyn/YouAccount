package ye.chilyn.youaccounts.register;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseActivity;
import ye.chilyn.youaccounts.base.interfaces.IBaseModel;
import ye.chilyn.youaccounts.util.SoftKeyboardUtil;
import ye.chilyn.youaccounts.util.ToastUtil;
import ye.chilyn.youaccounts.view.TitleBarView;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TitleBarView mTitleBarView;
    private EditText mEtId, mEtNickname, mEtPassword, mEtConfirmPassword;
    private TextView mTvRegister;
    private IBaseModel mUserSqlModel;

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
        mEtId = findView(R.id.et_id);
        mEtNickname = findView(R.id.et_nickname);
        mEtPassword = findView(R.id.et_password);
        mEtConfirmPassword = findView(R.id.et_confirm_password);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.register_account));
    }

    private void setListener() {
        mTvRegister.setOnClickListener(this);
        mEtConfirmPassword.setOnEditorActionListener(mEditorActionListener);
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
        }
    }

    private void register() {
        String id = mEtId.getText().toString().trim();
        String nickname = mEtNickname.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        String confirmPassword = mEtConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(nickname) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            ToastUtil.showShortToast(getString(R.string.register_can_not_be_empty));
            return;
        }
    }

    @Override
    protected void destroyViews() {
    }

    @Override
    protected void releaseModels() {
    }
}
