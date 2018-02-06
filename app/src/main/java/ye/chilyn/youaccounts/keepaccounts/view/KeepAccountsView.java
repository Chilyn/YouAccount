package ye.chilyn.youaccounts.keepaccounts.view;

import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccounts.constant.HandleModelType;
import ye.chilyn.youaccounts.constant.RefreshViewType;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;
import ye.chilyn.youaccounts.keepaccounts.entity.QueryAccountsParameter;
import ye.chilyn.youaccounts.util.DateUtil;
import ye.chilyn.youaccounts.util.SoftKeyboardUtil;
import ye.chilyn.youaccounts.util.ToastUtil;

/**
 * Created by Alex on 2018/1/15.
 * 记账页面你的View层
 */

public class KeepAccountsView extends BaseAccountsView implements View.OnClickListener {

    private EditText mEtMoney;
    private TextView mTvBillType, mTvKeepAccounts;
    private TextView mTvThisWeekTotal;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private NumberFormat mNumberFormat;
    /**账单类型选择弹窗*/
    private BillTypeDialogView mBillTypeDialogView;
    private ProgressDialogView mProgressDialogView;

    public KeepAccountsView(View rootView, OnHandleModelListener listener) {
        super(rootView, listener);
        initViews();
        initData();
        setViewListener();
    }

    @Override
    public void initViews() {
        super.initViews();
        mEtMoney = findView(R.id.et_money);
        mTvBillType = findView(R.id.tv_bill_type);
        mTvKeepAccounts = findView(R.id.tv_keep_accounts);
        mTvThisWeekTotal = findView(R.id.tv_this_week_total);
        mBillTypeDialogView = new BillTypeDialogView(mContext, mBillTypeSelectedListener);
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
        findView(R.id.ll_bill_type).setOnClickListener(this);
        mTvKeepAccounts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_bill_type:
                mBillTypeDialogView.showDialog();
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
        String moneyStr = mEtMoney.getText().toString();
        try {
            float money = Float.valueOf(moneyStr);
            Date date = new Date();
            AccountsBean bean = new AccountsBean(mUserId, money, mTvBillType.getText().toString(), date.getTime(), dateFormat.format(date));
            callHandleModel(HandleModelType.INSERT_ACCOUNTS, bean);
            SoftKeyboardUtil.forceCloseSoftKeyboard(mEtMoney);
            mProgressDialogView.showProgressDialog();
        } catch (NumberFormatException e) {
            mEtMoney.setText(null);
            ToastUtil.showShortToast(getString(R.string.data_invalid));
        }
    }

    private BillTypeDialogView.OnBillTypeSelectedListener mBillTypeSelectedListener = new BillTypeDialogView.OnBillTypeSelectedListener() {
        @Override
        public void onItemSelected(String billType) {
            mTvBillType.setText(billType);
        }
    };

    @Override
    public void refreshViews(int refreshType, Object data) {
        mHandler.sendMessage(mHandler.obtainMessage(refreshType, data));
    }

    private ViewHandler mHandler = new ViewHandler(this);

    private static class ViewHandler extends BaseStaticInnerHandler<KeepAccountsView> {

        public ViewHandler(KeepAccountsView keepAccountsView) {
            super(keepAccountsView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isReferenceRecycled()) {
                removeCallbacksAndMessages(null);
                return;
            }

            KeepAccountsView view = getReference();
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
                    view.onQueryAccountsSuccess((List<AccountsBean>) msg.obj);
                    view.mProgressDialogView.dismissProgressDialog();
                    break;

                case RefreshViewType.SHOW_TOTAL_ACCOUNTS:
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
            }
        }
    }

    private void onInsertAccountsSuccess(){
        ToastUtil.showShortToast(getString(R.string.record_success));
        mEtMoney.setText(null);
        Date now = new Date();
        callHandleModel(HandleModelType.QUERY_ACCOUNTS,
                new QueryAccountsParameter(mUserId, DateUtil.getThisWeekStartTime(now), DateUtil.getThisWeekEndTime(now)));
    }

    /**
     * 显示账目总金额
     * @param totalMoney
     */
    private void showTotalAccounts(Float totalMoney) {
        mTvThisWeekTotal.setText(mNumberFormat.format(totalMoney));
    }

    @Override
    protected void onDeleteAccountSuccess(AccountsBean bean) {
        ToastUtil.showShortToast(getString(R.string.delete_success));
        Date now = new Date();
        callHandleModel(HandleModelType.QUERY_ACCOUNTS,
                new QueryAccountsParameter(mUserId, DateUtil.getThisWeekStartTime(now), DateUtil.getThisWeekEndTime(now)));
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
