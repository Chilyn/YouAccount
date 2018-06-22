package ye.chilyn.youaccounts.keepaccount.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.util.DialogUtil;

/**
 * Created by Alex on 2018/1/18.
 * 修改删除账单弹窗的View层
 */

public class DeleteOrModifyDialogView implements View.OnClickListener {

    private Context mContext;
    private ClickCallBack mCallBack;
    private Dialog mDialogDeleteOrModify;
    private View mDeleteOrModifyDialogView;
    private TextView mTvDeleteAccount, mTvModifyAccount;
    private int mDialogWidthDp = 200, mDialogHeightDp = 100;

    public DeleteOrModifyDialogView(Context context, ClickCallBack callBack) {
        this.mContext = context;
        this.mCallBack = callBack;
        initViews();
        setViewListener();
    }

    private void initViews() {
        mDeleteOrModifyDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete_or_modify_account, null);
        mTvModifyAccount = (TextView) mDeleteOrModifyDialogView.findViewById(R.id.tv_modify_account);
        mTvDeleteAccount = (TextView) mDeleteOrModifyDialogView.findViewById(R.id.tv_delete_account);
    }

    private void setViewListener() {
        mTvModifyAccount.setOnClickListener(this);
        mTvDeleteAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_modify_account:
                mCallBack.onModify();
                dismissDialog();
                break;

            case R.id.tv_delete_account:
                mCallBack.onDelete();
                dismissDialog();
                break;
        }
    }

    public void showDialog() {
        if (mDialogDeleteOrModify == null) {
            mDialogDeleteOrModify = DialogUtil.createDialog(mContext, mDeleteOrModifyDialogView, mDialogWidthDp, mDialogHeightDp);
        }

        mDialogDeleteOrModify.show();
    }

    private void dismissDialog() {
        if (mDialogDeleteOrModify == null) {
            return;
        }

        mDialogDeleteOrModify.dismiss();
    }

    public interface ClickCallBack {
        /**用户点击修改账单*/
        void onModify();
        /**用户点击删除账单*/
        void onDelete();
    }
}
