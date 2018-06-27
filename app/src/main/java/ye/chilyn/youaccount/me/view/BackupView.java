package ye.chilyn.youaccount.me.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.base.BaseView;
import ye.chilyn.youaccount.constant.HandleModelType;
import ye.chilyn.youaccount.constant.RefreshViewType;
import ye.chilyn.youaccount.me.entity.UploadInfo;
import ye.chilyn.youaccount.util.DialogUtil;
import ye.chilyn.youaccount.util.ToastUtil;

public class BackupView extends BaseView implements View.OnClickListener {
    
    private ProgressDialog mDialogUploadProgress;
    private AlertDialog mDialogUploadErrorInfo;
    private Dialog mDialogChooseServer;
    private TextView mTvLocalServer, mTvRemoteServer;
    private int mDialogWidthDp = 200, mDialogHeightDp = 100;
    private int mUploadCount;
    private boolean mIsUploadCanceled = false;

    public BackupView(View rootView, OnHandleModelListener listener) {
        super(rootView, listener);
        initViews();
        initData();
        setViewListener();
    }

    @Override
    public void initViews() {
        mDialogUploadProgress = DialogUtil.createDefaultProgressDialog(mContext, "", false);
        mDialogUploadErrorInfo = DialogUtil.createNoNegativeDialog(mContext, "", getString(R.string.confirm));
        mDialogUploadErrorInfo.setCanceledOnTouchOutside(false);
        View chooseServerDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_choose_server, null);
        mTvLocalServer = (TextView) chooseServerDialogView.findViewById(R.id.tv_local_server);
        mTvRemoteServer = (TextView) chooseServerDialogView.findViewById(R.id.tv_remote_server);
        mDialogChooseServer = DialogUtil.createDialog(mContext, chooseServerDialogView, mDialogWidthDp, mDialogHeightDp);
    }

    @Override
    public void initData() {
    }

    @Override
    public void setViewListener() {
        mTvLocalServer.setOnClickListener(this);
        mTvRemoteServer.setOnClickListener(this);
        mDialogUploadProgress.setOnCancelListener(mOnCancelListener);
    }

    private DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            mIsUploadCanceled = true;
            callHandleModel(HandleModelType.CANCEL_UPLOAD, null);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_local_server:
                uploadFile(HandleModelType.UPLOAD_TO_LOCAL_SERVER);
                break;

            case R.id.tv_remote_server:
                uploadFile(HandleModelType.UPLOAD_TO_REMOTE_SERVER);
                break;
        }
    }

    private void uploadFile(int handleModelType) {
        mIsUploadCanceled = false;
        mUploadCount++;
        mDialogChooseServer.dismiss();
        mDialogUploadProgress.setMessage(getString(R.string.connecting_server));
        mDialogUploadProgress.show();
        callHandleModel(handleModelType, mUploadCount);
    }
    
    @Override
    public void refreshViews(int refreshType, Object data) {
        if (refreshType != RefreshViewType.SHOW_CHOOSE_SERVER_DIALOG && mIsUploadCanceled) {
            return;
        }

        switch (refreshType) {
            case RefreshViewType.UPLOAD_FAILED:
                onUploadFailed((UploadInfo) data);
                break;

            case RefreshViewType.UPLOAD_SUCCESS:
                onUploadSuccess((UploadInfo) data);
                break;

            case RefreshViewType.REFRESH_UPLOAD_PROGRESS:
                onRefreshUploadProgress((UploadInfo) data);
                break;

            case RefreshViewType.SHOW_CHOOSE_SERVER_DIALOG:
                mDialogChooseServer.show();
                break;
        }
    }

    private void onUploadFailed(UploadInfo info) {
        if (mUploadCount != info.getId()) {
            return;
        }

        mDialogUploadProgress.dismiss();
        mDialogUploadErrorInfo.setMessage(info.getInfo());
        mDialogUploadErrorInfo.show();
    }

    private void onUploadSuccess(UploadInfo info) {
        if (mUploadCount != info.getId()) {
            return;
        }

        mDialogUploadProgress.dismiss();
        ToastUtil.showShortToast(info.getInfo());
    }

    private void onRefreshUploadProgress(UploadInfo info) {
        if (mUploadCount != info.getId()) {
            return;
        }

        mDialogUploadProgress.setMessage(info.getInfo());
    }

    private String getString(int resId) {
        return mContext.getString(resId);
    }

    @Override
    protected void releaseHandler() {
    }
}
