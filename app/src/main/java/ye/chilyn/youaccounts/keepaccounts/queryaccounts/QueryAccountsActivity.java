package ye.chilyn.youaccounts.keepaccounts.queryaccounts;

import android.os.Bundle;

import java.util.Date;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseActivity;
import ye.chilyn.youaccounts.base.interfaces.IBaseModel;
import ye.chilyn.youaccounts.base.interfaces.IBaseView;
import ye.chilyn.youaccounts.constant.HandleModelType;
import ye.chilyn.youaccounts.constant.RefreshViewType;
import ye.chilyn.youaccounts.keepaccounts.entity.QueryAccountsParameter;
import ye.chilyn.youaccounts.keepaccounts.model.AccountsCalculateModel;
import ye.chilyn.youaccounts.keepaccounts.model.KeepAccountsSqlModel;
import ye.chilyn.youaccounts.keepaccounts.queryaccounts.view.QueryAccountsView;
import ye.chilyn.youaccounts.util.DateUtil;
import ye.chilyn.youaccounts.view.TitleBarView;

/**
 * 查询账目的Activity
 */
public class QueryAccountsActivity extends BaseActivity {

    private TitleBarView mTitleBarView;
    private IBaseModel mKeepAccountsSqlModel;
    private IBaseModel mAccountsCalculateModel;
    private IBaseView mQueryAccountsView;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_accounts);
        initView();
        initData();
    }

    private void initView() {
        mTitleBarView = new TitleBarView(findView(R.id.title_bar), this);
        mTitleBarView.setRightOptionViewVisibility(false);
        mQueryAccountsView = new QueryAccountsView(findView(R.id.ll_root), mHandleModelListener);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.query_accounts));
        mUserId = AccountsApplication.getLoginUserInfo().getUserId();
        mKeepAccountsSqlModel = new KeepAccountsSqlModel(mRefreshViewListener);
        mAccountsCalculateModel = new AccountsCalculateModel(mRefreshViewListener);
        Date now = new Date();
        mKeepAccountsSqlModel.handleModelEvent(HandleModelType.QUERY_ACCOUNTS,
                new QueryAccountsParameter(mUserId, DateUtil.getMonthStartTime(now), DateUtil.getMonthEndTime(now)));
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
