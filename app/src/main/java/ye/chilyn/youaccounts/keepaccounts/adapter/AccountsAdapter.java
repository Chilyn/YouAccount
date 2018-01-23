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

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.CommonAdapter;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;
import ye.chilyn.youaccounts.util.DateUtil;

/**
 * Created by Alex on 2018/1/15.
 * 账目数据的Adapter
 */

public class AccountsAdapter extends CommonAdapter<AccountsBean, AccountsAdapter.ViewHolder> {

    private static final String TODAY = AccountsApplication.getAppContext().getString(R.string.today);
    private static final String YESTERDAY = AccountsApplication.getAppContext().getString(R.string.yesterday);
    /**金额格式化类*/
    private DecimalFormat mNumberFormat;
    private SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("MM-dd HH:mm");
    private SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm");

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
        holder.mTvTime.setText(createTimeString(item.getTimeMill()));
        BigDecimal decimal = new BigDecimal(Float.toString(item.getMoney()));
        holder.mTvMoney.setText(mNumberFormat.format(decimal.doubleValue()));
        if (position == (getCount() - 1)) {
            holder.mTvDivider.setVisibility(View.GONE);
        } else {
            holder.mTvDivider.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据时间long值创建相关格式的时间字符
     * @param timeMill
     * @return
     */
    private String createTimeString(long timeMill) {
        StringBuilder timeStr = new StringBuilder();
        if (DateUtil.isToday(timeMill)) {
            timeStr.append(TODAY).append(" ").append(mTimeFormat.format(new Date(timeMill)));
        } else if (DateUtil.isYesterday(timeMill)) {
            timeStr.append(YESTERDAY).append(" ").append(mTimeFormat.format(new Date(timeMill)));
        } else {
            timeStr.append(mDateTimeFormat.format(new Date(timeMill)));
        }
        return timeStr.toString();
    }

    @Override
    public void setListData(List<AccountsBean> data) {
        if (data == null) {
            return;
        }

        mListData.clear();
        mListData.addAll(data);
        notifyDataSetChanged();
    }

    protected class ViewHolder extends CommonAdapter.ViewHolder {

        private TextView mTvBillType, mTvTime, mTvMoney, mTvDivider;

        public ViewHolder(View rootView) {
            super(rootView);
            mTvBillType = (TextView) rootView.findViewById(R.id.tv_bill_type);
            mTvTime = (TextView) rootView.findViewById(R.id.tv_time);
            mTvMoney = (TextView) rootView.findViewById(R.id.tv_money);
            mTvDivider = (TextView) rootView.findViewById(R.id.tv_divider);
        }
    }
}
