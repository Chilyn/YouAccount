package ye.chilyn.youaccount.keepaccount.model;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.base.BaseModel;
import ye.chilyn.youaccount.constant.HandleModelType;
import ye.chilyn.youaccount.constant.RefreshViewType;
import ye.chilyn.youaccount.constant.SharePreferenceKey;
import ye.chilyn.youaccount.keepaccount.entity.AccountBean;
import ye.chilyn.youaccount.keepaccount.entity.QueryAccountParameter;
import ye.chilyn.youaccount.sql.AccountsDao;
import ye.chilyn.youaccount.util.CacheExecutorHelper;
import ye.chilyn.youaccount.util.SharePreferencesUtils;

/**
 * Created by Alex on 2018/1/15.
 * 账目相关数据库操作的Model
 */

public class KeepAccountSqlModel extends BaseModel {

    private ExecutorService mSqlTaskExecutor = CacheExecutorHelper.getInstance().getCacheExecutor();
    private AccountsDao mAccountsDao = new AccountsDao();
    private List<String> mDefaultBillTypes;
    private List<String> mCustomBillTypes;

    public KeepAccountSqlModel(OnRefreshViewListener listener) {
        super(listener);
    }

    @Override
    public void handleModelEvent(int type, Object data) {
        mSqlTaskExecutor.execute(new SqlTask(type, data));
    }

    private class SqlTask implements Runnable {

        private int mEventType;
        private Object mData;

        public SqlTask(int mEventType, Object data) {
            this.mEventType = mEventType;
            this.mData = data;
        }

        @Override
        public void run() {
            switch (mEventType) {
                case HandleModelType.INSERT_ACCOUNTS:
                    insertAccounts((AccountBean) mData);
                    break;

                case HandleModelType.QUERY_ACCOUNTS:
                    queryAccounts((QueryAccountParameter) mData);
                    break;

                case HandleModelType.DELETE_ACCOUNTS:
                    deleteAccounts((AccountBean) mData);
                    break;

                case HandleModelType.UPDATE_ACCOUNTS:
                    updateAccounts((AccountBean) mData);
                    break;

                case HandleModelType.DELETE_BILL_TYPE:
                    deleteBillType((String) mData);
                    break;
            }
        }
    }

    private void insertAccounts(AccountBean bean) {
        boolean isSuccess = mAccountsDao.insertAccounts(bean);
        if (isSuccess) {
            callRefreshView(RefreshViewType.INSERT_ACCOUNTS_SUCCESS, null);
            addNewBillType(bean.getBillType());
        } else {
            callRefreshView(RefreshViewType.INSERT_ACCOUNTS_FAIL, null);
        }
    }

    private void addNewBillType(String billType) {
        if (mCustomBillTypes == null) {
            initBillTypes();
        }

        if (!mDefaultBillTypes.contains(billType) && !mCustomBillTypes.contains(billType)) {
            mCustomBillTypes.add(billType);
            SharePreferencesUtils.save(SharePreferenceKey.BILL_TYPES, new Gson().toJson(mCustomBillTypes));
            callRefreshView(RefreshViewType.UPDATE_CUSTOM_BILL_TYPES, mCustomBillTypes);
        }
    }

    private void queryAccounts(QueryAccountParameter param) {
        List<AccountBean> listAccountBean = mAccountsDao.queryAccounts(param.getUserId(), param.getStartTime(), param.getEndTime());
        callRefreshView(RefreshViewType.QUERY_ACCOUNTS_SUCCESS, listAccountBean);
        float totalPayments = mAccountsDao.queryTotalPayments(param.getUserId(), param.getStartTime(), param.getEndTime());
        callRefreshView(RefreshViewType.SHOW_TOTAL_PAYMENTS, totalPayments);
    }

    private void deleteAccounts(AccountBean bean) {
        if (mAccountsDao.deleteAccount(bean)) {
            callRefreshView(RefreshViewType.DELETE_ACCOUNT_SUCCESS, bean);
        } else {
            callRefreshView(RefreshViewType.DELETE_ACCOUNT_FAIL, null);
        }
    }

    private void updateAccounts(AccountBean bean) {
        if (mAccountsDao.updateAccount(bean)) {
            callRefreshView(RefreshViewType.UPDATE_ACCOUNT_SUCCESS, null);
        } else {
            callRefreshView(RefreshViewType.UPDATE_ACCOUNT_FAIL, null);
        }
    }

    private void deleteBillType(String billType) {
        if (mCustomBillTypes == null) {
            initBillTypes();
        }

        mCustomBillTypes.remove(billType);
        SharePreferencesUtils.save(SharePreferenceKey.BILL_TYPES, new Gson().toJson(mCustomBillTypes));
        callRefreshView(RefreshViewType.UPDATE_CUSTOM_BILL_TYPES, mCustomBillTypes);
    }

    private void initBillTypes() {
        mCustomBillTypes = new ArrayList<>();
        String customJson = SharePreferencesUtils.getStringValue(SharePreferenceKey.BILL_TYPES);
        if (!TextUtils.isEmpty(customJson)) {
            String[] customBillTypes = new Gson().fromJson(customJson, String[].class);
            mCustomBillTypes.addAll(Arrays.asList(customBillTypes));
        }

        mDefaultBillTypes = new ArrayList<>();
        String[] defaultBillTypes = AccountApplication.getAppContext().getResources().getStringArray(R.array.bill_type);
        mDefaultBillTypes.addAll(Arrays.asList(defaultBillTypes));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
