package ye.chilyn.youaccount.keepaccount.queryaccount;

import android.os.Bundle;

import java.util.Date;

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.base.BaseActivity;
import ye.chilyn.youaccount.base.interfaces.IBaseModel;
import ye.chilyn.youaccount.base.interfaces.IBaseView;
import ye.chilyn.youaccount.constant.HandleModelType;
import ye.chilyn.youaccount.constant.RefreshViewType;
import ye.chilyn.youaccount.keepaccount.entity.QueryAccountParameter;
import ye.chilyn.youaccount.keepaccount.model.AccountCalculateModel;
import ye.chilyn.youaccount.keepaccount.model.KeepAccountSqlModel;
import ye.chilyn.youaccount.keepaccount.queryaccount.view.QueryAccountView;
import ye.chilyn.youaccount.util.DateUtil;
import ye.chilyn.youaccount.view.TitleBarView;

/**
 * 查询账目的Activity
 */
public class QueryAccountActivity extends BaseActivity {

    private TitleBarView mTitleBarView;
    private IBaseModel mKeepAccountsSqlModel;
    private IBaseModel mAccountsCalculateModel;
    private IBaseView mQueryAccountsView;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_account);
        initView();
        initData();
    }

    private void initView() {
        mTitleBarView = new TitleBarView(findView(R.id.title_bar), this);
        mTitleBarView.setRightOptionViewVisibility(false);
        mQueryAccountsView = new QueryAccountView(findView(R.id.ll_root), mHandleModelListener);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.query_accounts));
        mUserId = AccountApplication.getLoginUserInfo().getUserId();
        mKeepAccountsSqlModel = new KeepAccountSqlModel(mRefreshViewListener);
        mAccountsCalculateModel = new AccountCalculateModel(mRefreshViewListener);
        Date now = new Date();
        mKeepAccountsSqlModel.handleModelEvent(HandleModelType.QUERY_ACCOUNTS,
                new QueryAccountParameter(mUserId, DateUtil.getMonthStartTime(now), DateUtil.getMonthEndTime(now)));
        mQueryAccountsView.refreshViews(RefreshViewType.SHOW_PROGRESS_DIALOG, null);
    }

    private HandleModelListener mHandleModelListener = new HandleModelListener();

    private class HandleModelListener implements IBaseView.OnHandleModelListener {

        @Override
        public void onHandleModel(int type, Object data) {
            switch (type) {
                case HandleModelType.QUERY_ACCOUNTS:
                case HandleModelType.UPDATE_ACCOUNTS:
                case HandleModelType.DELETE_ACCOUNTS:
                    mKeepAccountsSqlModel.handleModelEvent(type, data);
                    break;
            }
        }
    }

    private RefreshViewListener mRefreshViewListener = new RefreshViewListener();

    private class RefreshViewListener implements IBaseModel.OnRefreshViewListener {

        @Override
        public void onRefreshView(int refreshType, Object data) {
            switch (refreshType) {
                case RefreshViewType.QUERY_ACCOUNTS_SUCCESS:
                    //计算总账目
                    mAccountsCalculateModel.handleModelEvent(HandleModelType.CALCULATE_TOTAL_ACCOUNTS, data);
                    mQueryAccountsView.refreshViews(refreshType, data);
                    break;

                default:
                    mQueryAccountsView.refreshViews(refreshType, data);
                    break;
            }
        }
    }

    @Override
    protected void destroyViews() {
        mQueryAccountsView.onDestroy();
    }

    protected void releaseModels() {
        mKeepAccountsSqlModel.onDestroy();
        mKeepAccountsSqlModel = null;
    }
}
