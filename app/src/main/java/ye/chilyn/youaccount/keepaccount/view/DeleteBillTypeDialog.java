package ye.chilyn.youaccount.keepaccount.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.util.DialogUtil;

public class DeleteBillTypeDialog implements View.OnClickListener {

    private Context mContext;
    private Dialog mDialogDeleteBillType;
    private View mDeleteDialogView;
    private TextView mTvConfirm;
    private TextView mTvTitle;
    private String mTitle;
    private String mBillType;

    public DeleteBillTypeDialog(Context context, String billType) {
        this.mContext = context;
        this.mBillType = billType;
        this.mTitle = context.getString(R.string.confirm_delete_bill_type, billType);
        initDialogViews();
    }

    private void initDialogViews() {
        mDeleteDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete_bill_type, null);
        mTvTitle = mDeleteDialogView.findViewById(R.id.tv_delete_title);
        mTvConfirm = mDeleteDialogView.findViewById(R.id.tv_confirm);
        mDialogDeleteBillType = DialogUtil.createDialog(mContext, R.id.tv_cancel, mDeleteDialogView, 250, 101);
        mTvConfirm.setOnClickListener(this);
        mTvTitle.setText(mTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                mDialogDeleteBillType.dismiss();
                if (mConfirmDeleteListener != null) {
                    mConfirmDeleteListener.confirmDelete(mBillType);
                }
                break;
        }
    }

    public void show() {
        mDialogDeleteBillType.show();
    }

    private ConfirmDeleteListener mConfirmDeleteListener;

    public void setConfirmDeleteListener(ConfirmDeleteListener l) {
        mConfirmDeleteListener = l;
    }

    public interface ConfirmDeleteListener {
        void confirmDelete(String billType);
    }
}
