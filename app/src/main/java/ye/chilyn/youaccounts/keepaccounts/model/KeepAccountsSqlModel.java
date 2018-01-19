package ye.chilyn.youaccounts.keepaccounts.model;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ye.chilyn.youaccounts.base.BaseModel;
import ye.chilyn.youaccounts.contant.HandleModelType;
import ye.chilyn.youaccounts.contant.RefreshViewType;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;
import ye.chilyn.youaccounts.keepaccounts.entity.QueryAccountsParameter;

/**
 * Created by Alex on 2018/1/15.
 */

public class KeepAccountsSqlModel extends BaseModel {

    private ExecutorService mSqlTaskExecutor = Executors.newSingleThreadExecutor();
    private AccountsSqlHelper mSqlHelper = AccountsSqlHelper.getInstance();

    public KeepAccountsSqlModel(OnRefreshViewListener listener) {
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
                    insertAccounts((AccountsBean) mData);
                    break;

                case HandleModelType.QUERY_ACCOUNTS:
                    queryAccounts((QueryAccountsParameter) mData);
                    break;

                case HandleModelType.DELETE_ACCOUNTS:
                    deleteAccounts((AccountsBean) mData);
                    break;

                case HandleModelType.UPDATE_ACCOUNTS:
                    updateAccounts((AccountsBean) mData);
                    break;
            }
        }
    }

    private void insertAccounts(AccountsBean bean) {
        boolean isSuccess = mSqlHelper.insertAccounts(bean);
        if (isSuccess) {
            callRefreshView(RefreshViewType.INSERT_ACCOUNTS_SUCCESS, null);
        } else {
            callRefreshView(RefreshViewType.INSERT_ACCOUNTS_FAIL, null);
        }
    }

    private void queryAccounts(QueryAccountsParameter param) {
        List<AccountsBean> listAccountsBean = mSqlHelper.queryAccounts(param.getUserId(), param.getStartTime(), param.getEndTime());
        callRefreshView(RefreshViewType.QUERY_ACCOUNTS_SUCCESS, listAccountsBean);
    }

    private void deleteAccounts(AccountsBean bean) {
        if (mSqlHelper.deleteAccount(bean)) {
            callRefreshView(RefreshViewType.DELETE_ACCOUNT_SUCCESS, null);
        } else {
            callRefreshView(RefreshViewType.DELETE_ACCOUNT_FAIL, null);
        }
    }

    private void updateAccounts(AccountsBean bean) {
        if (mSqlHelper.updateAccount(bean)) {
            callRefreshView(RefreshViewType.UPDATE_ACCOUNT_SUCCESS, null);
        } else {
            callRefreshView(RefreshViewType.UPDATE_ACCOUNT_FAIL, null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
