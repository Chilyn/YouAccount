package ye.chilyn.youaccounts.keepaccounts.queryaccounts.view;

import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.ypy.eventbus.EventBus;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccounts.contant.EventType;
import ye.chilyn.youaccounts.contant.HandleModelType;
import ye.chilyn.youaccounts.contant.RefreshViewType;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;
import ye.chilyn.youaccounts.keepaccounts.entity.QueryAccountsParameter;
import ye.chilyn.youaccounts.keepaccounts.view.BaseAccountsView;
import ye.chilyn.youaccounts.util.DateUtil;
import ye.chilyn.youaccounts.util.ToastUtil;
import ye.chilyn.youaccounts.widget.pickers.DateTimePicker;

/**
 * Created by Alex on 2018/1/22.
 */

public class QueryAccountsView extends BaseAccountsView implements View.OnClickListener {

    private static final int YEAR_MONTH = DateTimePicker.YEAR_MONTH;
    private static final int YEAR_MONTH_DAY = DateTimePicker.YEAR_MONTH_DAY;
    private static final int START_TIME = 0;
    private static final int END_TIME = 1;
    private TextView mTvChooseMonthOrDate;
    private TextView mTvAccountsRange, mTvMonth, mTvDate1, mTvDate2;
    private TextView mTvQuery;
    private TextView mTvTotalMoney;
    private NumberFormat mNumberFormat;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mMonthFormat = new SimpleDateFormat("yyyy-MM");
    private DateTimePicker mPicker;
    private int mCurrentChooseMode = DateTimePicker.YEAR_MONTH;

    public QueryAccountsView(View rootView, OnHandleModelListener listener) {
        super(rootView, listener);
        initViews();
        initData();
        setViewListener();
        EventBus.getDefault().register(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        mTvChooseMonthOrDate = findView(R.id.tv_choose_month_or_date);
        mTvAccountsRange = findView(R.id.tv_accounts_range);
        mTvMonth = findView(R.id.tv_month);
        mTvDate1 = findView(R.id.tv_date1);
        mTvDate2 = findView(R.id.tv_date2);
        mTvQuery = findView(R.id.tv_query);
        mTvTotalMoney = findView(R.id.tv_total_money);
    }

    @Override
    public void initData() {
        super.initData();
        mTvMonth.setText(mMonthFormat.format(new Date()));
        mNumberFormat = NumberFormat.getCurrencyInstance();
        mNumberFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    @Override
    public void setViewListener() {
        super.setViewListener();
        mTvMonth.setOnClickListener(this);
        mTvQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_month:
                showPicker(getString(R.string.choose_time));
                break;

            case R.id.tv_query:
                queryAccounts();
                break;
        }
    }

    private void showPicker(String title) {
        Calendar calendar = Calendar.getInstance();
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);


        mPicker = new DateTimePicker(mContext, mCurrentChooseMode, DateTimePicker.NONE);
        mPicker.setDateRangeStart(2017, 1, 1);
        mPicker.setDateRangeEnd(endYear, endMonth, endDay);
        mPicker.setWeightEnable(true);
        mPicker.setWheelModeEnable(true);
        mPicker.setCanLoop(false);
        mPicker.setTitleText(title);
        if (mCurrentChooseMode == DateTimePicker.YEAR_MONTH) {
            mPicker.setOnDateTimePickListener(mYearMonthPickListener);
        } else if (mCurrentChooseMode == YEAR_MONTH_DAY) {
            mPicker.setOnDateTimePickListener(mYearMonthDayPickListener);
        }
        mPicker.setSelectedItem(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DAY_OF_MONTH),
                0, 0);
        mPicker.show();
    }

    private DateTimePicker.OnYearMonthTimePickListener mYearMonthPickListener = new DateTimePicker.OnYearMonthTimePickListener() {
        @Override
        public void onDateTimePicked(String year, String month, String hour, String minute) {
            String chooseMonth = year + "-" + month;
            mTvMonth.setText(chooseMonth);
        }
    };

    private DateTimePicker.OnYearMonthDayTimePickListener mYearMonthDayPickListener = new DateTimePicker.OnYearMonthDayTimePickListener() {
        @Override
        public void onDateTimePicked(String year, String month, String day, String hour, String minute) {

        }
    };

    private void queryAccounts() {
        mTvAccountsRange.setText(mTvMonth.getText());
        try {
            Date chooseDate = mMonthFormat.parse(mTvMonth.getText().toString());
            callHandleModel(HandleModelType.QUERY_ACCOUNTS,
                    new QueryAccountsParameter(1, DateUtil.getMonthStartTime(chooseDate), DateUtil.getMonthEndTime(chooseDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshViews(int refreshType, Object data) {
        mHandler.sendMessage(mHandler.obtainMessage(refreshType, data));
    }

    private ViewHandler mHandler = new ViewHandler(this);

    private static class ViewHandler extends BaseStaticInnerHandler<QueryAccountsView> {

        public ViewHandler(QueryAccountsView view) {
            super(view);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isReferenceRecycled()) {
                removeCallbacksAndMessages(null);
                return;
            }

            QueryAccountsView view = getReference();
            switch (msg.what) {
                case RefreshViewType.QUERY_ACCOUNTS_SUCCESS:
                    view.onQueryAccountsSuccess((List<AccountsBean>) msg.obj);
                    break;

                case RefreshViewType.DELETE_ACCOUNT_SUCCESS:
                    view.onDeleteAccountSuccess((AccountsBean) msg.obj);
                    break;

                case RefreshViewType.DELETE_ACCOUNT_FAIL:
                    ToastUtil.showShortToast(view.getString(R.string.delete_fail));
                    break;

                case RefreshViewType.SHOW_TOTAL_ACCOUNTS:
                    view.showTotalAccounts((Float) msg.obj);
                    break;
            }
        }
    }

    private void showTotalAccounts(Float totalMoney) {
        mTvTotalMoney.setText(mNumberFormat.format(totalMoney));
    }

    @Override
    protected void onDeleteAccountSuccess(AccountsBean bean) {
        ToastUtil.showShortToast(getString(R.string.delete_success));
        long[] queryRangeTimeMill = getQueryRangeTimeMill();
        callHandleModel(HandleModelType.QUERY_ACCOUNTS,
                new QueryAccountsParameter(1, queryRangeTimeMill[START_TIME], queryRangeTimeMill[END_TIME]));
        EventBus.getDefault().post(EventType.QUERY_ACCOUNTS_AFTER_DELETE);
    }

    @Override
    public void onEvent(Integer eventType) {
        if (eventType == EventType.QUERY_ACCOUNTS) {
            long[] queryRangeTimeMill = getQueryRangeTimeMill();
            callHandleModel(HandleModelType.QUERY_ACCOUNTS,
                    new QueryAccountsParameter(1, queryRangeTimeMill[START_TIME], queryRangeTimeMill[END_TIME]));
        }
    }

    /**
     * 获取查询范围的开始时间和结束时间
     * @return 开始时间和结束时间组成的数组，第一个值为开始时间，第二个值为结束时间
     */
    private long[] getQueryRangeTimeMill() {
        long startTime = 0;
        long endTime = 0;
        if (mCurrentChooseMode == YEAR_MONTH) {
            try {
                Date date = mMonthFormat.parse(mTvMonth.getText().toString());
                startTime = DateUtil.getMonthStartTime(date);
                endTime = DateUtil.getMonthEndTime(date);
            } catch (ParseException e) {
                startTime = 0;
                endTime = 0;
            }
        }

        if (mCurrentChooseMode == YEAR_MONTH_DAY) {
            try {
                Date date1 = mDateFormat.parse(mTvDate1.getText().toString());
                Date date2 = mDateFormat.parse(mTvDate2.getText().toString());
                if (date1.getTime() <= date2.getTime()) {
                    startTime = date1.getTime();
                    endTime = date2.getTime();
                } else {
                    startTime = date2.getTime();
                    endTime = date1.getTime();
                }
            } catch (ParseException e) {
                startTime = 0;
                endTime = 0;
            }
        }

        return new long[]{startTime, endTime};
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void releaseHandler() {
        mHandler.clearReference();
        mHandler.removeCallbacksAndMessages(null);
    }
}
