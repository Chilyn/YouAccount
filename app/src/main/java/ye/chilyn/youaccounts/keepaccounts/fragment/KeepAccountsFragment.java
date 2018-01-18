package ye.chilyn.youaccounts.keepaccounts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ypy.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.interfaces.IBaseModel;
import ye.chilyn.youaccounts.base.interfaces.IBaseView;
import ye.chilyn.youaccounts.contant.EventType;
import ye.chilyn.youaccounts.contant.HandleModelType;
import ye.chilyn.youaccounts.keepaccounts.entity.QueryAccountsParameter;
import ye.chilyn.youaccounts.keepaccounts.model.KeepAccountsSqlModel;
import ye.chilyn.youaccounts.keepaccounts.view.KeepAccountsView;
import ye.chilyn.youaccounts.util.DateUtil;

/**
 * Created by Alex on 2018/1/15.
 */

public class KeepAccountsFragment extends Fragment {

    private IBaseView mKeepAccountsView;
    private IBaseModel mKeepAccountsSqlModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keep_accounts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mKeepAccountsView = new KeepAccountsView(view, mHandleModelListener);
        mKeepAccountsSqlModel = new KeepAccountsSqlModel(mRefreshViewListener);
        initData();
        EventBus.getDefault().register(this);
    }

    private void initData() {
        if (AccountsApplication.canCreateFile()) {
            Date now = new Date();
            mKeepAccountsSqlModel.handleModelEvent(HandleModelType.QUERY_ACCOUNTS,
                    new QueryAccountsParameter(1, DateUtil.getThisWeekStartTime(now), DateUtil.getThisWeekEndTime(now)));
        }
    }

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
            mKeepAccountsView.refreshViews(refreshType, data);
        }
    }

    public void onEvent(Integer eventType) {
        switch (eventType) {
            case EventType.WRITE_FILE_PERMISSION_GOTTEN:
                Date now = new Date();
                mKeepAccountsSqlModel.handleModelEvent(HandleModelType.QUERY_ACCOUNTS,
                        new QueryAccountsParameter(1, DateUtil.getThisWeekStartTime(now), DateUtil.getThisWeekEndTime(now)));
                break;

            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        mKeepAccountsView.onDestroy();
        releaseModels();
        super.onDestroyView();
    }

    private void releaseModels() {
        mKeepAccountsSqlModel.onDestroy();
        mKeepAccountsSqlModel = null;
    }
}
