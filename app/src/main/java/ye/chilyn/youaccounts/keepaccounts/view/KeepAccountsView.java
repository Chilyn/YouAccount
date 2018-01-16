package ye.chilyn.youaccounts.keepaccounts.view;

import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseView;
import ye.chilyn.youaccounts.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccounts.keepaccounts.adapter.AccountsAdapter;

/**
 * Created by Alex on 2018/1/15.
 */

public class KeepAccountsView extends BaseView implements View.OnClickListener{

    private EditText mEtMoney;
    private TextView mTvBillType, mTvKeepAccounts;
    private ListView mLv;
    private AccountsAdapter mAdapter;

    public KeepAccountsView(View rootView, OnHandleModelListener listener) {
        super(rootView, listener);
        initViews();
        initData();
        setViewListener();
    }

    @Override
    public void initViews() {
        mEtMoney = findView(R.id.et_money);
        mTvBillType = findView(R.id.tv_bill_type);
        mTvKeepAccounts = findView(R.id.tv_keep_accounts);
        mLv = findView(R.id.lv);
    }

    @Override
    public void initData() {
        mAdapter = new AccountsAdapter(mContext);
        mLv.setAdapter(mAdapter);
    }

    @Override
    public void setViewListener() {
        mTvKeepAccounts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_keep_accounts:
                keepAccounts();
                break;

            default:
                break;
        }
    }

    /**
     * 记账
     */
    private void keepAccounts() {

    }

    @Override
    public void refreshViews(int refreshType, Object data) {
        mHandler.handleMessage(mHandler.obtainMessage(refreshType, data));
    }

    private ViewHandler mHandler = new ViewHandler(this);

    private static class ViewHandler extends BaseStaticInnerHandler<KeepAccountsView> {

        public ViewHandler(KeepAccountsView keepAccountsView) {
            super(keepAccountsView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isReferenceRecycled()) {
                removeCallbacksAndMessages(null);
                return;
            }

            KeepAccountsView view = getReference();
            switch (msg.what) {

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void releaseHandler() {
        mHandler.clearReference();
        mHandler.removeCallbacksAndMessages(null);
    }
}
