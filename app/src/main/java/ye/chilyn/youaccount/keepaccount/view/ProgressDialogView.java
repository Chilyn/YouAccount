package ye.chilyn.youaccount.keepaccount.view;

import android.app.ProgressDialog;
import android.content.Context;

import ye.chilyn.youaccount.util.DialogUtil;

/**
 * Created by Alex on 2018/1/23.
 * 进度条弹窗View层
 */

public class ProgressDialogView {

    private ProgressDialog mDialogProgress;
    private String mMessage;
    private Context mContext;

    public ProgressDialogView(Context context, String message) {
        this.mContext = context;
        this.mMessage = message;
    }

    public void showProgressDialog() {
        if (mDialogProgress == null) {
            mDialogProgress = DialogUtil.createDefaultProgressDialog(mContext, mMessage, false);
        }

        mDialogProgress.show();
    }

    public void dismissProgressDialog() {
        if (mDialogProgress == null) {
            return;
        }

        mDialogProgress.dismiss();
    }
}
