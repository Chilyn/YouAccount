package ye.chilyn.youaccount.keepaccount.view;

import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccount.constant.HandleModelType;
import ye.chilyn.youaccount.constant.RefreshViewType;
import ye.chilyn.youaccount.keepaccount.entity.AccountBean;
import ye.chilyn.youaccount.keepaccount.interfaces.OnBillTypeSelectedListener;
import ye.chilyn.youaccount.util.SoftKeyboardUtil;
import ye.chilyn.youaccount.util.ToastUtil;

/**
 * Created by Alex on 2018/1/15.
 * 记账页面你的View层
 */

public class KeepAccountView extends BaseAccountView implements View.OnClickListener {

    private EditText mEtMoney;
    private EditText mEtBillType;
    private TextView mTvKeepAccounts;
    private TextView mTvThisWeekTotal;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private NumberFormat mNumberFormat;
    /**账单类型选择弹窗*/
    private BillTypeWindowView mBillTypeWindowView;
    private ProgressDialogView mProgressDialogView;

    public KeepAccountView(View rootView, OnHandleModelListener listener) {
        super(rootView, listener);
        initViews();
        initData();
        setViewListener();
    }

    @Override
    public void initViews() {
        super.initViews();
        mEtMoney = findView(R.id.et_money);
        mEtBillType = findView(R.id.et_bill_type);
        mTvKeepAccounts = findView(R.id.tv_keep_accounts);
        mTvThisWeekTotal = findView(R.id.tv_this_week_total);
        mBillTypeWindowView = new BillTypeWindowView(mEtBillType, mBillTypeSelectedListener, true);
        mProgressDialogView = new ProgressDialogView(mContext, getString(R.string.querying));
    }

    @Override
    public void initData() {
        super.initData();
        mNumberFormat = NumberFormat.getCurrencyInstance();
        mNumberFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    @Override
    public void setViewListener() {
        super.setViewListener();
        findView(R.id.tv_choose_type).setOnClickListener(this);
        mTvKeepAccounts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_choose_type:
                mBillTypeWindowView.show();
                break;

            case R.id.tv_keep_accounts:
                keepAccounts();
                break;
        }
    }

    /**
     * 记账
     */
    private void keepAccounts() {
        String billType = mEtBillType.getText().toString();
        if (TextUtils.isEmpty(billType)) {
            ToastUtil.showShortToast(AccountApplication.getAppContext().getString(R.string.data_invalid));
            return;
        }

        String moneyStr = mEtMoney.getText().toString();
        try {
            float money = Float.valueOf(moneyStr);
            Date date = new Date();
            AccountBean bean = new AccountBean(mUserId, money, billType, date.getTime(), dateFormat.format(date));
            callHandleModel(HandleModelType.INSERT_ACCOUNTS, bean);
            SoftKeyboardUtil.forceCloseSoftKeyboard(mEtMoney);
            mProgressDialogView.showProgressDialog();
        } catch (NumberFormatException e) {
            mEtMoney.setText(null);
            ToastUtil.showShortToast(getString(R.string.data_invalid));
        }
    }

    private OnBillTypeSelectedListener mBillTypeSelectedListener = new OnBillTypeSelectedListener() {
        @Override
        public void onItemSelected(String billType) {
            mEtBillType.setText(billType);
            mEtBillType.setSelection(billType.length());
        }

        @Override
        public void onItemDelete(String billType) {
            callHandleModel(HandleModelType.DELETE_BILL_TYPE, billType);
        }
    };

    @Override
    public void refreshViews(int refreshType, Object data) {
        mHandler.sendMessage(mHandler.obtainMessage(refreshType, data));
    }

    private ViewHandler mHandler = new ViewHandler(this);

    private static class ViewHandler extends BaseStaticInnerHandler<KeepAccountView> {

        public ViewHandler(KeepAccountView keepAccountsView) {
            super(keepAccountsView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isReferenceRecycled()) {
                removeCallbacksAndMessages(null);
                return;
            }

            KeepAccountView view = getReference();
            switch (msg.what) {
                case RefreshViewType.SHOW_PROGRESS_DIALOG:
                    view.mProgressDialogView.showProgressDialog();
                    break;

                case RefreshViewType.INSERT_ACCOUNTS_SUCCESS:
                    view.onInsertAccountsSuccess();
                    view.mProgressDialogView.dismissProgressDialog();
                    break;

                case RefreshViewType.INSERT_ACCOUNTS_FAIL:
                    ToastUtil.showShortToast(view.getString(R.string.record_fail));
                    view.mProgressDialogView.dismissProgressDialog();
                    break;

                case RefreshViewType.QUERY_ACCOUNTS_SUCCESS:
                    view.onQueryAccountsSuccess((List<AccountBean>) msg.obj);
                    view.mProgressDialogView.dismissProgressDialog();
                    break;

                case RefreshViewType.SHOW_TOTAL_PAYMENTS:
                    view.showTotalAccounts((Float) msg.obj);
                    break;

                case RefreshViewType.DELETE_ACCOUNT_SUCCESS:
                    view.onDeleteAccountSuccess(null);
                    break;

                case RefreshViewType.DELETE_ACCOUNT_FAIL:
                    ToastUtil.showShortToast(view.getString(R.string.delete_fail));
                    break;

                case RefreshViewType.FORCE_CLOSE_SOFT_KEYBOARD:
                    SoftKeyboardUtil.forceCloseSoftKeyboard(view.mEtMoney);
                    break;

                case RefreshViewType.UPDATE_CUSTOM_BILL_TYPES:
                    view.mBillTypeWindowView.updateCustomBillTypes((List<String>) msg.obj);
                    break;
            }
        }
    }

    private void onInsertAccountsSuccess(){
        ToastUtil.showShortToast(getString(R.string.record_success));
        mEtMoney.setText(null);
    }

    /**
     * 显示账目总金额
     * @param totalMoney
     */
    private void showTotalAccounts(Float totalMoney) {
        mTvThisWeekTotal.setText(mNumberFormat.format(totalMoney));
    }

    @Override
    protected void onDeleteAccountSuccess(AccountBean bean) {
        ToastUtil.showShortToast(getString(R.string.delete_success));
    }

    @Override
    public void onEvent(Integer eventType) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void releaseHandler() {
        mHandler.clearReference();
        mHandler.removeCallbacksAndMessages(null);
    }
}
