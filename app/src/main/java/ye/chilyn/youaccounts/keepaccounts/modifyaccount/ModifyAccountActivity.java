package ye.chilyn.youaccounts.keepaccounts.modifyaccount;

import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ypy.eventbus.EventBus;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseActivity;
import ye.chilyn.youaccounts.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccounts.base.interfaces.IBaseModel;
import ye.chilyn.youaccounts.contant.EventType;
import ye.chilyn.youaccounts.contant.HandleModelType;
import ye.chilyn.youaccounts.contant.RefreshViewType;
import ye.chilyn.youaccounts.keepaccounts.contant.ExtraKey;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;
import ye.chilyn.youaccounts.keepaccounts.model.KeepAccountsSqlModel;
import ye.chilyn.youaccounts.keepaccounts.view.BillTypeDialogView;
import ye.chilyn.youaccounts.util.SoftKeyboardUtil;
import ye.chilyn.youaccounts.util.ToastUtil;
import ye.chilyn.youaccounts.view.TitleBarView;

/**
 * 修改账目的Activity
 */
public class ModifyAccountActivity extends BaseActivity implements View.OnClickListener {

    private IBaseModel mKeepAccountsSqlModel;
    private EditText mEtMoney;
    private TextView mTvBillType, mTvTime;
    private TextView mTvModify;
    private AccountsBean mModifyBean;
    private BillTypeDialogView mBillTypeDialogView;
    private TitleBarView mTitleBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mTitleBarView = new TitleBarView(findView(R.id.title_bar), this);
        mTitleBarView.setRightOptionViewVisibility(false);
        mEtMoney = findView(R.id.et_money);
        mTvBillType = findView(R.id.tv_bill_type);
        mTvTime = findView(R.id.tv_time);
        mTvModify = findView(R.id.tv_modify);
        mBillTypeDialogView = new BillTypeDialogView(this, mBillTypeSelectedListener);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.modify_account));
        mKeepAccountsSqlModel = new KeepAccountsSqlModel(mRefreshViewListener);
        mModifyBean = (AccountsBean) getIntent().getSerializableExtra(ExtraKey.ACCOUNTS_BEAN);
        if (mModifyBean != null) {
            mEtMoney.setText(mModifyBean.getMoney() + "");
            mEtMoney.setSelection(mEtMoney.getText().length());
            mTvBillType.setText(mModifyBean.getBillType());
            mTvTime.setText(mModifyBean.getTime());
        }
    }

    private void setListener() {
        mTvModify.setOnClickListener(this);
        findView(R.id.ll_bill_type).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_modify:
                modifyAccount();
                break;

            case R.id.ll_bill_type:
                mBillTypeDialogView.showDialog();
                break;
        }
    }

    /**
     * 修改账目
     */
    private void modifyAccount() {
        String moneyStr = mEtMoney.getText().toString();
        try {
            float money = Float.valueOf(moneyStr);
            mModifyBean.setMoney(money);
            mModifyBean.setBillType(mTvBillType.getText().toString());
            mKeepAccountsSqlModel.handleModelEvent(HandleModelType.UPDATE_ACCOUNTS, mModifyBean);
        } catch (NumberFormatException e) {
            mEtMoney.setText(null);
            ToastUtil.showShortToast(AccountsApplication.getAppContext().getString(R.string.data_invalid));
        } finally {
            SoftKeyboardUtil.forceCloseSoftKeyboard(mEtMoney);
        }
    }

    private BillTypeDialogView.OnBillTypeSelectedListener mBillTypeSelectedListener = new BillTypeDialogView.OnBillTypeSelectedListener() {
        @Override
        public void onItemSelected(String billType) {
            mTvBillType.setText(billType);
        }
    };

    private RefreshViewListener mRefreshViewListener = new RefreshViewListener();

    private class RefreshViewListener implements IBaseModel.OnRefreshViewListener {

        @Override
        public void onRefreshView(int refreshType, Object data) {
            mHandler.sendMessage(mHandler.obtainMessage(refreshType, data));
        }
    }

    private ViewHandler mHandler = new ViewHandler(this);

    private static class ViewHandler extends BaseStaticInnerHandler<ModifyAccountActivity> {

        public ViewHandler(ModifyAccountActivity activity) {
            super(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isReferenceRecycled()) {
                removeCallbacksAndMessages(null);
                return;
            }

            ModifyAccountActivity activity = getReference();
            switch (msg.what) {
                case RefreshViewType.UPDATE_ACCOUNT_SUCCESS:
                    ToastUtil.showShortToast(activity.getString(R.string.modify_success));
                    //修改成功通知各页面刷新数据
                    EventBus.getDefault().post(EventType.QUERY_ACCOUNTS_AFTER_UPDATE);
                    activity.finish();
                    break;

                case RefreshViewType.UPDATE_ACCOUNT_FAIL:
                    ToastUtil.showShortToast(activity.getString(R.string.modify_fail));
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoftKeyboardUtil.forceCloseSoftKeyboard(mEtMoney);
    }

    @Override
    protected void onDestroy() {
        mHandler.clearReference();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void destroyViews() {
    }

    @Override
    protected void releaseModels() {
        mKeepAccountsSqlModel.onDestroy();
        mKeepAccountsSqlModel = null;
    }
}
