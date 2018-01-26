package ye.chilyn.youaccounts.me.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseFragment;
import ye.chilyn.youaccounts.login.LoginActivity;
import ye.chilyn.youaccounts.util.DialogUtil;
import ye.chilyn.youaccounts.view.TitleBarView;

/**
 * 我的Fragment
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    /**标题栏*/
    private TitleBarView mTitleBarView;
    private ImageView mIvProfilePhoto;
    private LinearLayout mLlModifyPassword, mLlModifyNickname, mLlExit;

    //-------------------退出登录弹窗相关------------------------//
    private Dialog mDialogExit;
    private View mExitDialogView;
    private TextView mTvConfirm;
    //-------------------退出登录弹窗相关------------------------//

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initData();
        setListener();
    }

    private void initViews() {
        mTitleBarView = new TitleBarView(findView(R.id.title_bar), getActivity());
        mTitleBarView.setLeftOptionViewVisibility(false);
        mTitleBarView.setRightOptionViewVisibility(false);
        mIvProfilePhoto = findView(R.id.iv_profile_photo);
        mLlModifyPassword = findView(R.id.ll_modify_password);
        mLlModifyNickname = findView(R.id.ll_modify_nickname);
        mLlExit = findView(R.id.ll_exit);
        mExitDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_confirm_exit, null);
        mTvConfirm = (TextView) mExitDialogView.findViewById(R.id.tv_confirm);
        mDialogExit = DialogUtil.createDialog(getActivity(), R.id.tv_cancel, mExitDialogView, 250, 101);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.me));
    }

    private void setListener() {
        mIvProfilePhoto.setOnClickListener(this);
        mLlModifyPassword.setOnClickListener(this);
        mLlModifyNickname.setOnClickListener(this);
        mLlExit.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_profile_photo:
                break;

            case R.id.ll_modify_password:
                break;

            case R.id.ll_modify_nickname:
                break;

            case R.id.ll_exit:
                mDialogExit.show();
                break;

            case R.id.tv_confirm:
                mDialogExit.dismiss();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }

    @Override
    protected void destroyViews() {
    }

    @Override
    protected void releaseModels() {
    }
}
