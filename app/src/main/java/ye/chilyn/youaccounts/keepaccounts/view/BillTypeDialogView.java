package ye.chilyn.youaccounts.keepaccounts.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.keepaccounts.adapter.BillTypeAdapter;
import ye.chilyn.youaccounts.util.DialogUtil;

/**
 * Created by Alex on 2018/1/18.
 * 账目类型弹窗的View层
 */

public class BillTypeDialogView {

    private Context mContext;
    private Dialog mDialogBillType;
    private View mBillTypeDialogView;
    private ListView mLvBillType;
    private BillTypeAdapter mAdapterBillType;
    private int mDialogWidthDp = 200, mDialogHeightDp = 200;
    private OnBillTypeSelectedListener mBillTypeSelectedListener;

    public BillTypeDialogView(Context context, OnBillTypeSelectedListener listener) {
        this.mContext = context;
        this.mBillTypeSelectedListener = listener;
        initViews();
        initData();
        setViewListener();
    }

    private void initData() {
        mAdapterBillType = new BillTypeAdapter(mContext);
        mLvBillType.setAdapter(mAdapterBillType);
    }

    private void initViews() {
        mBillTypeDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_choose_bill_type, null);
        mLvBillType = (ListView) mBillTypeDialogView.findViewById(R.id.lv);
    }

    private void setViewListener() {
        mLvBillType.setOnItemClickListener(mOnItemClickListener);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String billType = mAdapterBillType.getItem(position);
            if (mBillTypeSelectedListener != null) {
                mBillTypeSelectedListener.onItemSelected(billType);
            }
            dismissDialog();
        }
    };

    public void showDialog() {
        if (mDialogBillType == null) {
            mDialogBillType = DialogUtil.createDialog(mContext, mBillTypeDialogView, mDialogWidthDp, mDialogHeightDp);
        }

        mDialogBillType.show();
    }

    private void dismissDialog() {
        if (mDialogBillType == null) {
            return;
        }

        mDialogBillType.dismiss();
    }

    public interface OnBillTypeSelectedListener {
        void onItemSelected(String billType);
    }
}
