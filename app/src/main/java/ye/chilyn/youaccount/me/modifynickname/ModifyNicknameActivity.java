package ye.chilyn.youaccount.me.modifynickname;

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

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.base.BaseActivity;
import ye.chilyn.youaccount.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccount.base.interfaces.IBaseModel;
import ye.chilyn.youaccount.constant.EventType;
import ye.chilyn.youaccount.constant.HandleModelType;
import ye.chilyn.youaccount.constant.RefreshViewType;
import ye.chilyn.youaccount.constant.SharePreferenceKey;
import ye.chilyn.youaccount.entity.UserBean;
import ye.chilyn.youaccount.me.model.ModifyModel;
import ye.chilyn.youaccount.util.MD5Util;
import ye.chilyn.youaccount.util.SharePreferencesUtils;
import ye.chilyn.youaccount.util.SoftKeyboardUtil;
import ye.chilyn.youaccount.util.ToastUtil;
import ye.chilyn.youaccount.view.TitleBarView;

public class ModifyNicknameActivity extends BaseActivity implements View.OnClickListener {

    /**标题栏*/
    private TitleBarView mTitleBarView;
    private EditText mEtNickname, mEtPassword;
    private ImageView mIvPasswordVisibility;
    private TextView mTvModify;
    private IBaseModel mModifyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nickname);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mTitleBarView = new TitleBarView(findView(R.id.title_bar), this);
        mTitleBarView.setRightOptionViewVisibility(false);
        mEtNickname = findView(R.id.et_nickname);
        mIvPasswordVisibility = findView(R.id.iv_password_visibility);
        mEtPassword = findView(R.id.et_password);
        mTvModify = findView(R.id.tv_modify);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.modify_nick_name));
        mModifyModel = new ModifyModel(mRefreshViewListener);
    }

    private void setListener() {
        mIvPasswordVisibility.setOnClickListener(this);
        mTvModify.setOnClickListener(this);
        mEtPassword.setOnEditorActionListener(mEditorActionListener);
    }

    private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                SoftKeyboardUtil.forceCloseSoftKeyboard(mEtPassword);
                modifyNickname();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_password_visibility:
                showOrHidePassword(mIvPasswordVisibility, mEtPassword);
                break;

            case R.id.tv_modify:
                SoftKeyboardUtil.forceCloseSoftKeyboard(mEtPassword);
                modifyNickname();
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

    private void modifyNickname() {
        String nickname = mEtNickname.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            ToastUtil.showShortToast(getString(R.string.nickname_can_not_be_empty));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtil.showShortToast(getString(R.string.password_can_not_be_empty));
            return;
        }

        UserBean loginUserBean = AccountApplication.getLoginUserInfo();
        password = MD5Util.getStringMD5(MD5Util.getStringMD5(password));
        if (!loginUserBean.getPassword().equals(password)) {
            ToastUtil.showShortToast(getString(R.string.password_is_wrong));
            return;
        }

        loginUserBean.setNickname(nickname);
        mModifyModel.handleModelEvent(HandleModelType.MODIFY_USER_NICKNAME, loginUserBean);
    }

    private RefreshViewListener mRefreshViewListener = new RefreshViewListener();

    private class RefreshViewListener implements IBaseModel.OnRefreshViewListener {

        @Override
        public void onRefreshView(int refreshType, Object data) {
            mHandler.sendMessage(mHandler.obtainMessage(refreshType, data));
        }
    }

    private ViewHandler mHandler = new ViewHandler(this);

    private static class ViewHandler extends BaseStaticInnerHandler<ModifyNicknameActivity> {

        public ViewHandler(ModifyNicknameActivity activity) {
            super(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isReferenceRecycled()) {
                removeCallbacksAndMessages(null);
                return;
            }

            ModifyNicknameActivity activity = getReference();
            switch (msg.what) {
                case RefreshViewType.MODIFY_NICKNAME_SUCCESS:
                    activity.onModifyNicknameSuccess((UserBean) msg.obj);
                    break;

                case RefreshViewType.MODIFY_NICKNAME_FAIL:
                    ToastUtil.showShortToast(activity.getString(R.string.modify_nickname_fail));
                    break;
            }
        }
    }

    private void onModifyNicknameSuccess(UserBean bean) {
        SharePreferencesUtils.save(SharePreferenceKey.NICKNAME, bean.getNickname());
        AccountApplication.setLoginUserInfo(bean.copy());
        ToastUtil.showShortToast(getString(R.string.nickname_has_been_modified));
        EventBus.getDefault().post(EventType.MODIFY_NICKNAME_SUCCESS);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoftKeyboardUtil.forceCloseSoftKeyboard(mEtPassword);
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
