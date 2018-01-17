package ye.chilyn.youaccounts.keepaccounts.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.base.BaseModel;
import ye.chilyn.youaccounts.contant.HandleModelType;
import ye.chilyn.youaccounts.contant.RefreshViewType;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;

/**
 * Created by Alex on 2018/1/15.
 */

public class KeepAccountsSqlModel extends BaseModel {

    private ExecutorService mSqlTaskExecutor = Executors.newSingleThreadExecutor();
    private AccountsSqlHelper mSqlHelper;

    public KeepAccountsSqlModel(OnRefreshViewListener listener) {
        super(listener);
        mSqlHelper = new AccountsSqlHelper(AccountsApplication.getAppContext());
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

    private void queryAccounts() {
        // TODO: 2018/1/16  
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
