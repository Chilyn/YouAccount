package ye.chilyn.youaccount.me.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypy.eventbus.EventBus;

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.base.BaseFragment;
import ye.chilyn.youaccount.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccount.base.interfaces.IBaseModel;
import ye.chilyn.youaccount.base.interfaces.IBaseView;
import ye.chilyn.youaccount.constant.EventType;
import ye.chilyn.youaccount.constant.HandleModelType;
import ye.chilyn.youaccount.constant.RefreshViewType;
import ye.chilyn.youaccount.constant.SharePreferenceKey;
import ye.chilyn.youaccount.me.model.UploadModel;
import ye.chilyn.youaccount.me.modifynickname.ModifyNicknameActivity;
import ye.chilyn.youaccount.me.modifypassword.ModifyPasswordActivity;
import ye.chilyn.youaccount.me.view.BackupView;
import ye.chilyn.youaccount.me.view.ExitDialog;
import ye.chilyn.youaccount.util.SharePreferencesUtils;
import ye.chilyn.youaccount.view.TitleBarView;

/**
 * 我的Fragment
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    /**标题栏*/
    private TitleBarView mTitleBarView;
    private ImageView mIvProfilePhoto;
    private TextView mTvNickname;
    private TextView mTvVersion;

    //-------------------备份数据相关------------------------//
    private LinearLayout mLlBackupData;
    private IBaseView mBackupView;
    private IBaseModel mUploadModel;
    //-------------------备份数据相关------------------------//

    //-------------------退出登录弹窗相关------------------------//
    private LinearLayout mLlExit;
    private ExitDialog mDialogExit;
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
        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mTitleBarView = new TitleBarView(findView(R.id.title_bar), getActivity());
        mTitleBarView.setLeftOptionViewVisibility(false);
        mTitleBarView.setRightOptionViewVisibility(false);
        mIvProfilePhoto = findView(R.id.iv_profile_photo);
        mTvNickname = findView(R.id.tv_nick_name);
        mLlBackupData = findView(R.id.ll_backup_data);
        mTvVersion = findView(R.id.tv_version);
        mLlExit = findView(R.id.ll_exit);
        mHandler.sendEmptyMessage(RefreshViewType.INIT_ME_FRAGMENT_DIALOGS);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.me));
        String nickname = SharePreferencesUtils.getStringValue(SharePreferenceKey.NICKNAME);
        mTvNickname.setText(nickname);
        mTvVersion.setText(getVersionName());
        mUploadModel = new UploadModel(mRefreshViewListener);
    }

    private String getVersionName() {
        try {
            PackageManager pm = AccountApplication.getAppContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(AccountApplication.getAppContext().getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void setListener() {
        mIvProfilePhoto.setOnClickListener(this);
        findView(R.id.rl_modify_password).setOnClickListener(this);
        findView(R.id.rl_modify_nickname).setOnClickListener(this);
        mLlBackupData.setOnClickListener(this);
        mLlExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_profile_photo:
                break;

            case R.id.rl_modify_password:
                startActivity(new Intent(getActivity(), ModifyPasswordActivity.class));
                break;

            case R.id.rl_modify_nickname:
                startActivity(new Intent(getActivity(), ModifyNicknameActivity.class));
                break;

            case R.id.ll_backup_data:
                mBackupView.refreshViews(RefreshViewType.SHOW_CHOOSE_SERVER_DIALOG, null);
                break;

            case R.id.ll_exit:
                mDialogExit.show();
                break;
        }
    }

    public void onEvent(Integer eventType) {
        switch (eventType) {
            case EventType.MODIFY_NICKNAME_SUCCESS:
                mTvNickname.setText(AccountApplication.getLoginUserInfo().getNickname());
                break;

            case EventType.MODIFY_PASSWORD_SUCCESS:
                mDialogExit.exitLogin();
                break;
        }
    }

    private HandleModelListener mHandleModelListener = new HandleModelListener();

    private class HandleModelListener implements IBaseView.OnHandleModelListener {

        @Override
        public void onHandleModel(int type, Object data) {
            switch (type) {
                case HandleModelType.UPLOAD_TO_LOCAL_SERVER:
                case HandleModelType.UPLOAD_TO_REMOTE_SERVER:
                case HandleModelType.CANCEL_UPLOAD:
                    mUploadModel.handleModelEvent(type, data);
                    break;
            }
        }
    }

    private IBaseModel.OnRefreshViewListener mRefreshViewListener = new IBaseModel.OnRefreshViewListener() {
        @Override
        public void onRefreshView(int refreshType, Object data) {
            mHandler.sendMessage(mHandler.obtainMessage(refreshType, data));
        }
    };

    private ViewHandler mHandler = new ViewHandler(this);

    private static class ViewHandler extends BaseStaticInnerHandler<MeFragment> {

        public ViewHandler(MeFragment fragment) {
            super(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isReferenceRecycled()) {
                removeCallbacksAndMessages(null);
                return;
            }

            MeFragment fragment = getReference();
            switch (msg.what) {
                case RefreshViewType.INIT_ME_FRAGMENT_DIALOGS:
                    fragment.initDialogViews();
                    break;

                case RefreshViewType.UPLOAD_FAILED:
                case RefreshViewType.UPLOAD_SUCCESS:
                case RefreshViewType.REFRESH_UPLOAD_PROGRESS:
                    fragment.mBackupView.refreshViews(msg.what, msg.obj);
                    break;
            }
        }
    }

    private void initDialogViews() {
        mBackupView = new BackupView(mLlBackupData, mHandleModelListener);
        mDialogExit = new ExitDialog(getActivity());
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    protected void destroyViews() {
        mBackupView.onDestroy();
    }

    @Override
    protected void releaseModels() {
        mUploadModel.onDestroy();
    }
}
