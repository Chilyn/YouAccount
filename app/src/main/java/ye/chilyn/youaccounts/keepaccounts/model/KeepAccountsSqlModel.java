package ye.chilyn.youaccounts.keepaccounts.model;

import java.util.List;
import java.util.concurrent.ExecutorService;

import ye.chilyn.youaccounts.base.BaseModel;
import ye.chilyn.youaccounts.constant.HandleModelType;
import ye.chilyn.youaccounts.constant.RefreshViewType;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;
import ye.chilyn.youaccounts.keepaccounts.entity.QueryAccountsParameter;
import ye.chilyn.youaccounts.sql.AccountsDao;
import ye.chilyn.youaccounts.util.CacheExecutorHelper;

/**
 * Created by Alex on 2018/1/15.
 * 账目相关数据库操作的Model
 */

public class KeepAccountsSqlModel extends BaseModel {

    private ExecutorService mSqlTaskExecutor = CacheExecutorHelper.getInstance().getCacheExecutor();
    private AccountsDao mAccountsDao = new AccountsDao();

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
        boolean isSuccess = mAccountsDao.insertAccounts(bean);
        if (isSuccess) {
            callRefreshView(RefreshViewType.INSERT_ACCOUNTS_SUCCESS, null);
        } else {
            callRefreshView(RefreshViewType.INSERT_ACCOUNTS_FAIL, null);
        }
    }

    private void queryAccounts(QueryAccountsParameter param) {
        List<AccountsBean> listAccountsBean = mAccountsDao.queryAccounts(param.getUserId(), param.getStartTime(), param.getEndTime());
        callRefreshView(RefreshViewType.QUERY_ACCOUNTS_SUCCESS, listAccountsBean);
    }

    private void deleteAccounts(AccountsBean bean) {
        if (mAccountsDao.deleteAccount(bean)) {
            callRefreshView(RefreshViewType.DELETE_ACCOUNT_SUCCESS, bean);
        } else {
            callRefreshView(RefreshViewType.DELETE_ACCOUNT_FAIL, null);
        }
    }

    private void updateAccounts(AccountsBean bean) {
        if (mAccountsDao.updateAccount(bean)) {
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
