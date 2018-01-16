package ye.chilyn.youaccounts.keepaccounts.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.CommonAdapter;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;

/**
 * Created by Alex on 2018/1/15.
 */

public class AccountsAdapter extends CommonAdapter<AccountsBean, AccountsAdapter.ViewHolder> {

    public AccountsAdapter(Context context) {
        super(context, R.layout.list_item_accounts);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        for (int i = 0; i < 20; i++) {
            date.setTime(date.getTime() + 1000);
            AccountsBean bean = new AccountsBean();
            bean.setBillType(i % 2 == 0 ? "其他":"服饰");
            bean.setMoney(i % 2 == 0 ? 1.2f:2.5f);
            bean.setTime(format.format(date));
            bean.setTimeMill(date.getTime());
            mListData.add(bean);
        }
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, AccountsBean item, int position) {
        holder.mTvBillType.setText(item.getBillType());
        holder.mTvTime.setText(item.getTime());
        holder.mTvMoney.setText(item.getMoney() + "");
    }

    @Override
    protected void setListData(List<AccountsBean> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        mListData.clear();
        mListData.addAll(data);
        notifyDataSetChanged();
    }

    protected class ViewHolder extends CommonAdapter.ViewHolder {

        private TextView mTvBillType, mTvTime, mTvMoney;

        public ViewHolder(View rootView) {
            super(rootView);
            mTvBillType = (TextView) rootView.findViewById(R.id.tv_bill_type);
            mTvTime = (TextView) rootView.findViewById(R.id.tv_time);
            mTvMoney = (TextView) rootView.findViewById(R.id.tv_money);
        }
    }
}
