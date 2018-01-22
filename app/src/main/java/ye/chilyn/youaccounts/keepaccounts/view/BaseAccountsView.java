package ye.chilyn.youaccounts.keepaccounts.view;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ypy.eventbus.EventBus;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseView;
import ye.chilyn.youaccounts.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccounts.contant.HandleModelType;
import ye.chilyn.youaccounts.contant.RefreshViewType;
import ye.chilyn.youaccounts.keepaccounts.adapter.AccountsAdapter;
import ye.chilyn.youaccounts.keepaccounts.contant.ExtraKey;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;
import ye.chilyn.youaccounts.keepaccounts.entity.QueryAccountsParameter;
import ye.chilyn.youaccounts.keepaccounts.modifyaccount.ModifyAccountActivity;
import ye.chilyn.youaccounts.util.DateUtil;
import ye.chilyn.youaccounts.util.SoftKeyboardUtil;
import ye.chilyn.youaccounts.util.ToastUtil;

/**
 * Created by Alex on 2018/1/15.
 */

public abstract class BaseAccountsView extends BaseView {

    private ListView mLvAccounts;
    private AccountsAdapter mAdapterAccounts;
    /**修改或删除弹窗*/
    private DeleteOrModifyDialogView mDeleteOrModifyDialogView;
    private int mLongClickPosition = -1;

    public BaseAccountsView(View rootView, OnHandleModelListener listener) {
        super(rootView, listener);
    }

    @Override
    public void initViews() {
        mLvAccounts = findView(R.id.lv);
        mDeleteOrModifyDialogView = new DeleteOrModifyDialogView(mContext, mDeleteOrModifyCallback);
    }

    @Override
    public void initData() {
        mAdapterAccounts = new AccountsAdapter(mContext);
        mLvAccounts.setAdapter(mAdapterAccounts);
    }

    @Override
    public void setViewListener() {
        mLvAccounts.setOnItemLongClickListener(mOnItemLongClickListener);
    }

    private DeleteOrModifyDialogView.ClickCallBack mDeleteOrModifyCallback = new DeleteOrModifyDialogView.ClickCallBack() {
        @Override
        public void onModify() {
            Intent intent = new Intent(mContext, ModifyAccountActivity.class);
            intent.putExtra(ExtraKey.ACCOUNTS_BEAN, mAdapterAccounts.getItem(mLongClickPosition));
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
            return false;
        }
    };

    protected void onQueryAccountsSuccess(List<AccountsBean> data) {
        mAdapterAccounts.setListData(data);
    }

    protected abstract void onDeleteAccountSuccess(AccountsBean bean);
    public abstract void onEvent(Integer eventType);

    protected String getString(int id) {
        return mContext.getString(id);
    }
}
