package ye.chilyn.youaccounts.keepaccounts.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ypy.eventbus.EventBus;

import java.util.Date;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseFragment;
import ye.chilyn.youaccounts.base.interfaces.IBaseModel;
import ye.chilyn.youaccounts.base.interfaces.IBaseView;
import ye.chilyn.youaccounts.constant.EventType;
import ye.chilyn.youaccounts.constant.HandleModelType;
import ye.chilyn.youaccounts.constant.RefreshViewType;
import ye.chilyn.youaccounts.keepaccounts.entity.QueryAccountsParameter;
import ye.chilyn.youaccounts.keepaccounts.model.AccountsCalculateModel;
import ye.chilyn.youaccounts.keepaccounts.model.KeepAccountsSqlModel;
import ye.chilyn.youaccounts.keepaccounts.queryaccounts.QueryAccountsActivity;
import ye.chilyn.youaccounts.keepaccounts.view.KeepAccountsView;
import ye.chilyn.youaccounts.util.DateUtil;
import ye.chilyn.youaccounts.view.TitleBarView;

/**
 * Created by Alex on 2018/1/15.
 * 记账Fragment
 */

public class KeepAccountsFragment extends BaseFragment {

    /**记账view层*/
    private IBaseView mKeepAccountsView;
    /**数据库操作Model*/
    private IBaseModel mKeepAccountsSqlModel;
    /**账目计算Model*/
    private IBaseModel mAccountsCalculateModel;
    /**标题栏*/
    private TitleBarView mTitleBarView;
    private int mUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keep_accounts, container, false);
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
        mKeepAccountsView = new KeepAccountsView(mRootView, mHandleModelListener);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.keep_accounts));
        mTitleBarView.setRightOptionViewText(getString(R.string.query));

        mUserId = AccountsApplication.getLoginUserInfo().getUserId();
        mKeepAccountsSqlModel = new KeepAccountsSqlModel(mRefreshViewListener);
        mAccountsCalculateModel = new AccountsCalculateModel(mRefreshViewListener);
        Date now = new Date();
        mKeepAccountsSqlModel.handleModelEvent(HandleModelType.QUERY_ACCOUNTS,
                new QueryAccountsParameter(mUserId, DateUtil.getThisWeekStartTime(now), DateUtil.getThisWeekEndTime(now)));
        mKeepAccountsView.refreshViews(RefreshViewType.SHOW_PROGRESS_DIALOG, null);
    }

    private void setViewListener() {
        mTitleBarView.setRightOptionViewListener(mRightOptionViewListener);
    }

    private View.OnClickListener mRightOptionViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //跳转至查询页面
            startActivity(new Intent(getActivity(), QueryAccountsActivity.class));
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
                case HandleModelType.QUERY_ACCOUNTS:
                case HandleModelType.UPDATE_ACCOUNTS:
                case HandleModelType.DELETE_ACCOUNTS:
                    mKeepAccountsSqlModel.handleModelEvent(type, data);
                    break;

                default:
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
                    mKeepAccountsView.refreshViews(refreshType, data);
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
                Date now = new Date();
                mKeepAccountsSqlModel.handleModelEvent(HandleModelType.QUERY_ACCOUNTS,
                        new QueryAccountsParameter(mUserId, DateUtil.getThisWeekStartTime(now), DateUtil.getThisWeekEndTime(now)));
                break;
        }
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
