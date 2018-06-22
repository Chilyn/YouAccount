package ye.chilyn.youaccounts.keepaccount.view;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import ye.chilyn.youaccounts.AccountApplication;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseView;
import ye.chilyn.youaccounts.constant.HandleModelType;
import ye.chilyn.youaccounts.keepaccount.adapter.AccountAdapter;
import ye.chilyn.youaccounts.keepaccount.constant.ExtraKey;
import ye.chilyn.youaccounts.keepaccount.entity.AccountBean;
import ye.chilyn.youaccounts.keepaccount.modifyaccount.ModifyAccountActivity;
import ye.chilyn.youaccounts.util.SoftKeyboardUtil;

/**
 * Created by Alex on 2018/1/15.
 * 账目相关View层的基类
 */

public abstract class BaseAccountView extends BaseView {

    /**显示账目的列表*/
    private ListView mLvAccounts;
    private AccountAdapter mAdapterAccounts;
    /**修改或删除弹窗*/
    private DeleteOrModifyDialogView mDeleteOrModifyDialogView;
    private int mLongClickPosition = -1;
    protected int mUserId;

    public BaseAccountView(View rootView, OnHandleModelListener listener) {
        super(rootView, listener);
    }

    @Override
    public void initViews() {
        mLvAccounts = findView(R.id.lv);
        mDeleteOrModifyDialogView = new DeleteOrModifyDialogView(mContext, mDeleteOrModifyCallback);
    }

    @Override
    public void initData() {
        mUserId = AccountApplication.getLoginUserInfo().getUserId();
        mAdapterAccounts = new AccountAdapter(mContext);
        mLvAccounts.setAdapter(mAdapterAccounts);
    }

    @Override
    public void setViewListener() {
        mLvAccounts.setOnItemLongClickListener(mOnItemLongClickListener);
    }

    private DeleteOrModifyDialogView.ClickCallBack mDeleteOrModifyCallback = new DeleteOrModifyDialogView.ClickCallBack() {
        @Override
        public void onModify() {
            //跳转修改页面
            Intent intent = new Intent(mContext, ModifyAccountActivity.class);
            intent.putExtra(ExtraKey.ACCOUNT_BEAN, mAdapterAccounts.getItem(mLongClickPosition));
            mContext.startActivity(intent);
        }

        @Override
        public void onDelete() {
            callHandleModel(HandleModelType.DELETE_ACCOUNTS, mAdapterAccounts.getItem(mLongClickPosition));
        }
    };

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mLongClickPosition = position;
            mDeleteOrModifyDialogView.showDialog();
            SoftKeyboardUtil.forceCloseSoftKeyboard(mLvAccounts);
            return false;
        }
    };

    /**
     * 查询账目成功的相关操作
     * @param data
     */
    protected void onQueryAccountsSuccess(List<AccountBean> data) {
        mAdapterAccounts.setListData(data);
    }

    /**
     * 删除账目成功相关操作
     * @param bean
     */
    protected abstract void onDeleteAccountSuccess(AccountBean bean);
    public abstract void onEvent(Integer eventType);

    protected String getString(int id) {
        return mContext.getString(id);
    }
}
