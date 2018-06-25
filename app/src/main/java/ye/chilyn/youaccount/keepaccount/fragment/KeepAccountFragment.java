package ye.chilyn.youaccount.keepaccount.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ypy.eventbus.EventBus;

import java.util.Date;

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.base.BaseFragment;
import ye.chilyn.youaccount.base.interfaces.IBaseModel;
import ye.chilyn.youaccount.base.interfaces.IBaseView;
import ye.chilyn.youaccount.constant.EventType;
import ye.chilyn.youaccount.constant.HandleModelType;
import ye.chilyn.youaccount.constant.RefreshViewType;
import ye.chilyn.youaccount.keepaccount.entity.QueryAccountParameter;
import ye.chilyn.youaccount.keepaccount.model.AccountCalculateModel;
import ye.chilyn.youaccount.keepaccount.model.KeepAccountSqlModel;
import ye.chilyn.youaccount.keepaccount.queryaccount.QueryAccountActivity;
import ye.chilyn.youaccount.keepaccount.view.KeepAccountView;
import ye.chilyn.youaccount.util.DateUtil;
import ye.chilyn.youaccount.view.TitleBarView;

/**
 * Created by Alex on 2018/1/15.
 * 记账Fragment
 */

public class KeepAccountFragment extends BaseFragment {

    /**记账view层*/
    private IBaseView mKeepAccountsView;
    /**数据库操作Model*/
    private IBaseModel mKeepAccountsSqlModel;
    /**标题栏*/
    private TitleBarView mTitleBarView;
    private int mUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keep_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initData();
        setViewListener();
        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mTitleBarView = new TitleBarView(findView(R.id.title_bar), getActivity());
        mTitleBarView.setLeftOptionViewVisibility(false);
        mKeepAccountsView = new KeepAccountView(mRootView, mHandleModelListener);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.keep_accounts));
        mTitleBarView.setRightOptionViewText(getString(R.string.query));

        mUserId = AccountApplication.getLoginUserInfo().getUserId();
        mKeepAccountsSqlModel = new KeepAccountSqlModel(mRefreshViewListener);
        queryAccounts();
        mKeepAccountsView.refreshViews(RefreshViewType.SHOW_PROGRESS_DIALOG, null);
    }

    private void setViewListener() {
        mTitleBarView.setRightOptionViewListener(mRightOptionViewListener);
    }

    private View.OnClickListener mRightOptionViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //跳转至查询页面
            startActivity(new Intent(getActivity(), QueryAccountActivity.class));
            //强制关闭键盘
            mKeepAccountsView.refreshViews(RefreshViewType.FORCE_CLOSE_SOFT_KEYBOARD, null);
        }
    };

    private HandleModelListener mHandleModelListener = new HandleModelListener();

    private class HandleModelListener implements IBaseView.OnHandleModelListener {

        @Override
        public void onHandleModel(int type, Object data) {
            switch (type) {
                case HandleModelType.INSERT_ACCOUNTS:
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
                case RefreshViewType.INSERT_ACCOUNTS_SUCCESS:
                case RefreshViewType.DELETE_ACCOUNT_SUCCESS:
                    mKeepAccountsView.refreshViews(refreshType, data);
                    queryAccounts();
                    break;

                default:
                    mKeepAccountsView.refreshViews(refreshType, data);
                    break;
            }
        }
    }

    public void onEvent(Integer eventType) {
        switch (eventType) {
            case EventType.QUERY_ACCOUNTS_AFTER_DELETE:
            case EventType.QUERY_ACCOUNTS_AFTER_UPDATE:
                queryAccounts();
                break;
        }
    }

    private void queryAccounts() {
        Date now = new Date();
        mKeepAccountsSqlModel.handleModelEvent(HandleModelType.QUERY_ACCOUNTS,
                new QueryAccountParameter(mUserId, DateUtil.getThisWeekStartTime(now), DateUtil.getThisWeekEndTime(now)));
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    protected void destroyViews() {
        mKeepAccountsView.onDestroy();
    }

    @Override
    protected void releaseModels() {
        mKeepAccountsSqlModel.onDestroy();
        mKeepAccountsSqlModel = null;
    }
}
