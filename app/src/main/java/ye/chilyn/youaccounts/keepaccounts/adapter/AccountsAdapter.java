package ye.chilyn.youaccounts.keepaccounts.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    private DecimalFormat mNumberFormat;

    public AccountsAdapter(Context context) {
        super(context, R.layout.list_item_accounts);
        mNumberFormat = new DecimalFormat(",##0.00");
        mNumberFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, AccountsBean item, int position) {
        holder.mTvBillType.setText(item.getBillType());
        holder.mTvTime.setText(item.getTime());
        BigDecimal decimal = new BigDecimal(Float.toString(item.getMoney()));
        holder.mTvMoney.setText(mNumberFormat.format(decimal.doubleValue()));
    }

    @Override
    public void setListData(List<AccountsBean> data) {
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
