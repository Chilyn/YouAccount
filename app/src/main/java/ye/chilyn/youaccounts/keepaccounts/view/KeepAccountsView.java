package ye.chilyn.youaccounts.keepaccounts.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.MainActivity;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseView;
import ye.chilyn.youaccounts.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccounts.contant.HandleModelType;
import ye.chilyn.youaccounts.contant.RefreshViewType;
import ye.chilyn.youaccounts.keepaccounts.adapter.AccountsAdapter;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;
import ye.chilyn.youaccounts.keepaccounts.entity.QueryAccountsParameter;
import ye.chilyn.youaccounts.util.DateUtil;
import ye.chilyn.youaccounts.util.DialogUtil;
import ye.chilyn.youaccounts.util.ToastUtil;

/**
 * Created by Alex on 2018/1/15.
 */

public class KeepAccountsView extends BaseView implements View.OnClickListener {

    private EditText mEtMoney;
    private TextView mTvBillType, mTvKeepAccounts;
    private TextView mTvThisWeekTotal;
    private ListView mLvAccounts;
    private AccountsAdapter mAdapterAccounts;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private NumberFormat mNumberFormat;
    /**账单类型选择弹窗*/
    private BillTypeDialogView mBillTypeDialogView;
    /**修改或删除弹窗*/
    private DeleteOrModifyDialogView mDeleteOrModifyDialogView;
    private int mLongClickPosition = -1;

    public KeepAccountsView(View rootView, OnHandleModelListener listener) {
        super(rootView, listener);
        initViews();
        initData();
        setViewListener();
    }

    @Override
    public void initViews() {
        mEtMoney = findView(R.id.et_money);
        mTvBillType = findView(R.id.tv_bill_type);
        mTvKeepAccounts = findView(R.id.tv_keep_accounts);
        mTvThisWeekTotal = findView(R.id.tv_this_week_total);
        mLvAccounts = findView(R.id.lv);

        mBillTypeDialogView = new BillTypeDialogView(mContext, mBillTypeSelectedListener);
        mDeleteOrModifyDialogView = new DeleteOrModifyDialogView(mContext, mDeleteOrModifyCallback);
    }

    @Override
    public void initData() {
        mAdapterAccounts = new AccountsAdapter(mContext);
        mLvAccounts.setAdapter(mAdapterAccounts);
        mNumberFormat = NumberFormat.getCurrencyInstance();
        mNumberFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    @Override
    public void setViewListener() {
        mTvBillType.setOnClickListener(this);
        mTvKeepAccounts.setOnClickListener(this);
        mLvAccounts.setOnItemLongClickListener(mOnItemLongClickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bill_type:
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
            AccountsBean bean = new AccountsBean(1, money, mTvBillType.getText().toString(), date.getTime(), dateFormat.format(date));
            callHandleModel(HandleModelType.INSERT_ACCOUNTS, bean);
            closeSoftKeyboard();
        } catch (NumberFormatException e) {
            mEtMoney.setText(null);
            ToastUtil.showShortToast(AccountsApplication.getAppContext().getString(R.string.data_invalid));
        }
    }

    private void closeSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) AccountsApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtMoney.getWindowToken(), 0); //强制隐藏键盘
    }

    private BillTypeDialogView.OnBillTypeSelectedListener mBillTypeSelectedListener = new BillTypeDialogView.OnBillTypeSelectedListener() {
        @Override
        public void onItemSelected(String billType) {
            mTvBillType.setText(billType);
        }
    };

    private DeleteOrModifyDialogView.ClickCallBack mDeleteOrModifyCallback = new DeleteOrModifyDialogView.ClickCallBack() {
        @Override
        public void onModify() {
            mContext.startActivity(new Intent(mContext, MainActivity.class));
        }

        @Override
        public void onDelete() {
            callHandleModel(HandleModelType.DELETE_ACCOUNTS, mAdapterAccounts.getItem(mLongClickPosition));
        }
    };

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mLongClickPosition = position;
            mDeleteOrModifyDialogView.showDialog();
            return false;
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
                case RefreshViewType.INSERT_ACCOUNTS_SUCCESS:
                    view.onInsertAccountsSuccess();
                    break;

                case RefreshViewType.INSERT_ACCOUNTS_FAIL:
                    ToastUtil.showShortToast("记录失败");
                    break;

                case RefreshViewType.QUERY_ACCOUNTS_SUCCESS:
                    view.onQueryAccountsSuccess((List<AccountsBean>) msg.obj);
                    break;

                case RefreshViewType.SHOW_TOTAL_ACCOUNTS:
                    view.showTotalAccounts((Float) msg.obj);
                    break;

                case RefreshViewType.DELETE_ACCOUNT_SUCCESS:
                    view.onDeleteAccountSuccess();
                    break;

                case RefreshViewType.DELETE_ACCOUNT_FAIL:
                    ToastUtil.showShortToast("删除失败");
                    break;
            }
        }
    }

    private void onInsertAccountsSuccess(){
        ToastUtil.showShortToast("记录成功");
        mEtMoney.setText(null);
        Date now = new Date();
        callHandleModel(HandleModelType.QUERY_ACCOUNTS,
                new QueryAccountsParameter(1, DateUtil.getThisWeekStartTime(now), DateUtil.getThisWeekEndTime(now)));
    }

    private void onQueryAccountsSuccess(List<AccountsBean> data) {
        mAdapterAccounts.setListData(data);
    }

    private void showTotalAccounts(Float totalMoney) {
        mTvThisWeekTotal.setText(mNumberFormat.format(totalMoney));
    }

    private void onDeleteAccountSuccess() {
        ToastUtil.showShortToast("删除成功");
        Date now = new Date();
        callHandleModel(HandleModelType.QUERY_ACCOUNTS,
                new QueryAccountsParameter(1, DateUtil.getThisWeekStartTime(now), DateUtil.getThisWeekEndTime(now)));
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
