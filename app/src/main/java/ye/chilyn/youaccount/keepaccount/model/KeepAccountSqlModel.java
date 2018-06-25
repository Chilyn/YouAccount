package ye.chilyn.youaccount.keepaccount.model;

import java.util.List;
import java.util.concurrent.ExecutorService;

import ye.chilyn.youaccount.base.BaseModel;
import ye.chilyn.youaccount.constant.HandleModelType;
import ye.chilyn.youaccount.constant.RefreshViewType;
import ye.chilyn.youaccount.keepaccount.entity.AccountBean;
import ye.chilyn.youaccount.keepaccount.entity.QueryAccountParameter;
import ye.chilyn.youaccount.sql.AccountsDao;
import ye.chilyn.youaccount.util.CacheExecutorHelper;

/**
 * Created by Alex on 2018/1/15.
 * 账目相关数据库操作的Model
 */

public class KeepAccountSqlModel extends BaseModel {

    private ExecutorService mSqlTaskExecutor = CacheExecutorHelper.getInstance().getCacheExecutor();
    private AccountsDao mAccountsDao = new AccountsDao();

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
            }
        }
    }

    private void insertAccounts(AccountBean bean) {
        boolean isSuccess = mAccountsDao.insertAccounts(bean);
        if (isSuccess) {
            callRefreshView(RefreshViewType.INSERT_ACCOUNTS_SUCCESS, null);
        } else {
            callRefreshView(RefreshViewType.INSERT_ACCOUNTS_FAIL, null);
        }
    }

    private void queryAccounts(QueryAccountParameter param) {
        List<AccountBean> listAccountBean = mAccountsDao.queryAccounts(param.getUserId(), param.getStartTime(), param.getEndTime());
        callRefreshView(RefreshViewType.QUERY_ACCOUNTS_SUCCESS, listAccountBean);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
