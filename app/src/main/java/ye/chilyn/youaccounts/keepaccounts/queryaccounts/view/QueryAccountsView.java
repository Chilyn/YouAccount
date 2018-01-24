package ye.chilyn.youaccounts.keepaccounts.queryaccounts.view;

import android.app.AlertDialog;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
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
import ye.chilyn.youaccounts.keepaccounts.view.ProgressDialogView;
import ye.chilyn.youaccounts.util.DateUtil;
import ye.chilyn.youaccounts.util.DialogUtil;
import ye.chilyn.youaccounts.util.ToastUtil;
import ye.chilyn.youaccounts.widget.pickers.DateTimePicker;

/**
 * Created by Alex on 2018/1/22.
 * 账目查询页面的View层
 */

public class QueryAccountsView extends BaseAccountsView implements View.OnClickListener {

    /**年月模式*/
    private static final int YEAR_MONTH = DateTimePicker.YEAR_MONTH;
    /**年月日模式*/
    private static final int YEAR_MONTH_DAY = DateTimePicker.YEAR_MONTH_DAY;
    private static final int START_TIME = 0, END_TIME = 1;
    private static final int DATE1 = 0, DATE2 = 1;
    private static final int NONE = -1;
    private TextView mTvChooseMonthOrDate;
    private RelativeLayout mRlMonth, mRlDate;
    private TextView mTvAccountsRange, mTvMonth, mTvDate1, mTvTo, mTvDate2;
    private TextView mTvQuery;
    private TextView mTvTotalMoney;
    private NumberFormat mNumberFormat;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mMonthFormat = new SimpleDateFormat("yyyy-MM");
    private DateTimePicker mPicker;
    private int mCurrentChooseMode = DateTimePicker.YEAR_MONTH;
    private int mCurrentSelectingDate = NONE;
    private AlertDialog mDialogOverSixMonth;
    private ProgressDialogView mProgressDialogView;

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
        mRlMonth = findView(R.id.rl_month);
        mRlDate = findView(R.id.rl_date);
        mTvAccountsRange = findView(R.id.tv_accounts_range);
        mTvMonth = findView(R.id.tv_month);
        mTvDate1 = findView(R.id.tv_date1);
        mTvTo = findView(R.id.tv_to);
        mTvDate2 = findView(R.id.tv_date2);
        mTvQuery = findView(R.id.tv_query);
        mTvTotalMoney = findView(R.id.tv_total_money);
        mProgressDialogView = new ProgressDialogView(mContext, getString(R.string.querying));
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
        mTvChooseMonthOrDate.setOnClickListener(this);
        mTvMonth.setOnClickListener(this);
        mTvDate1.setOnClickListener(this);
        mTvDate2.setOnClickListener(this);
        mTvQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_choose_month_or_date:
                changeQueryMode();
                break;

            case R.id.tv_month:
                showPicker(getString(R.string.choose_time));
                break;

            case R.id.tv_date1:
                mCurrentSelectingDate = DATE1;
                showPicker(getString(R.string.choose_start_date));
                break;

            case R.id.tv_date2:
                mCurrentSelectingDate = DATE2;
                showPicker(getString(R.string.choose_end_date));
                break;

            case R.id.tv_query:
                mProgressDialogView.showProgressDialog();
                queryAccounts();
                break;
        }
    }

    /**
     * 修改查询模式
     */
    private void changeQueryMode() {
        if (mCurrentChooseMode == YEAR_MONTH) {
            mCurrentChooseMode = YEAR_MONTH_DAY;
            mTvChooseMonthOrDate.setText(getString(R.string.choose_date));
            mRlMonth.setVisibility(View.GONE);
            mRlDate.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(mTvDate1.getText())) {
                mTvDate1.setText(mDateFormat.format(new Date()));
            }
            return;
        }

        if (mCurrentChooseMode == YEAR_MONTH_DAY) {
            mCurrentChooseMode = YEAR_MONTH;
            mTvChooseMonthOrDate.setText(getString(R.string.choose_month));
            mRlMonth.setVisibility(View.VISIBLE);
            mRlDate.setVisibility(View.GONE);
            return;
        }
    }

    /**
     * 显示日期选择弹窗
     * @param title
     */
    private void showPicker(String title) {
        Calendar calendar = Calendar.getInstance();
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);
        int selectedYear = endYear;
        int selectedMonth = endMonth;
        int selectedDay = endDay;
        //如果是按日期选择的模式，弹窗日期默认选中上次选中的月份
        if (mCurrentChooseMode == YEAR_MONTH) {
            try {
                Date lastChooseDate = mMonthFormat.parse(mTvMonth.getText().toString());
                calendar.setTime(lastChooseDate);
                selectedYear = calendar.get(Calendar.YEAR);
                selectedMonth = calendar.get(Calendar.MONTH) + 1;
                selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //如果是按日期选择的模式，弹窗日期默认选中上次选中的日期
        if (mCurrentChooseMode == YEAR_MONTH_DAY && mCurrentSelectingDate == DATE1) {
            try {
                Date lastChooseDate = mDateFormat.parse(mTvDate1.getText().toString());
                calendar.setTime(lastChooseDate);
                selectedYear = calendar.get(Calendar.YEAR);
                selectedMonth = calendar.get(Calendar.MONTH) + 1;
                selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (mCurrentChooseMode == YEAR_MONTH_DAY && mCurrentSelectingDate == DATE2) {
            try {
                Date lastChooseDate = mDateFormat.parse(mTvDate2.getText().toString());
                calendar.setTime(lastChooseDate);
                selectedYear = calendar.get(Calendar.YEAR);
                selectedMonth = calendar.get(Calendar.MONTH) + 1;
                selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

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
        mPicker.setSelectedItem(selectedYear, selectedMonth, selectedDay, 0, 0);
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
            StringBuilder sb = new StringBuilder();
            sb.append(year).append("-").append(month).append("-").append(day);
            switch (mCurrentSelectingDate) {
                case DATE1:
                    mTvDate1.setText(sb.toString());
                    break;

                case DATE2:
                    mTvDate2.setText(sb.toString());
                    break;
            }
            mCurrentSelectingDate = NONE;
        }
    };

    /**
     * 查询账目
     */
    private void queryAccounts() {
        if (mCurrentChooseMode == YEAR_MONTH) {
            Date chooseDate;
            try {
                chooseDate = mMonthFormat.parse(mTvMonth.getText().toString());
                //显示查询范围
                if (DateUtil.isThisMonth(chooseDate)) {
                    mTvAccountsRange.setText(getString(R.string.this_month));
                } else {
                    mTvAccountsRange.setText(mTvMonth.getText());
                }

                callHandleModel(HandleModelType.QUERY_ACCOUNTS,
                        new QueryAccountsParameter(1, DateUtil.getMonthStartTime(chooseDate), DateUtil.getMonthEndTime(chooseDate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return;
        }

        if (mCurrentChooseMode == YEAR_MONTH_DAY) {
            Date date1 = null, date2;
            try {
                date1 = mDateFormat.parse(mTvDate1.getText().toString());
                date2 = mDateFormat.parse(mTvDate2.getText().toString());
            } catch (ParseException e) {
                date2 = date1;
            }

            if (date1.getTime() > date2.getTime()) {
                //如果第二个日期在第一个日期前面，将两个日期调换
                Date temp = new Date(date1.getTime());
                date1.setTime(date2.getTime());
                date2.setTime(temp.getTime());
            }

            //如果两个日期不相等并且范围超过6个月则提示
            if (!DateUtil.isTheSameDate(date1, date2) && DateUtil.isOverSixMonth(date1, date2)) {
                mProgressDialogView.dismissProgressDialog();
                showOverSixMonthDialog();
                return;
            }

            //显示查询范围
            if (date1.getTime() == date2.getTime()) {
                mTvAccountsRange.setText(mDateFormat.format(date1));
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(mDateFormat.format(date1))
                    .append(" ")
                    .append(mTvTo.getText())
                    .append(" ")
                    .append(mDateFormat.format(date2));
                mTvAccountsRange.setText(sb.toString());
            }

            callHandleModel(HandleModelType.QUERY_ACCOUNTS,
                    new QueryAccountsParameter(1, DateUtil.getDateStartTime(date1), DateUtil.getDateEndTime(date2)));
            return;
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
                case RefreshViewType.SHOW_PROGRESS_DIALOG:
                    view.mProgressDialogView.showProgressDialog();
                    break;

                case RefreshViewType.QUERY_ACCOUNTS_SUCCESS:
                    view.onQueryAccountsSuccess((List<AccountsBean>) msg.obj);
                    view.mProgressDialogView.dismissProgressDialog();
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

    /**
     * 显示账目总额
     * @param totalMoney
     */
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
        //无论是什么查询模式下，如果账目范围显示本月则直接查询本月范围的数据
        if (getString(R.string.this_month).equals(mTvAccountsRange.getText().toString())) {
            Date date = new Date();
            startTime = DateUtil.getMonthStartTime(date);
            endTime = DateUtil.getMonthEndTime(date);
            return new long[]{startTime, endTime};
        }

        //按月查询模式
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

        //按日期查询模式
        if (mCurrentChooseMode == YEAR_MONTH_DAY) {
            Date date1 = null, date2;
            try {
                date1 = mDateFormat.parse(mTvDate1.getText().toString());
                date2 = mDateFormat.parse(mTvDate2.getText().toString());
            } catch (ParseException e) {
                date2 = date1;
            }

            if (date1.getTime() > date2.getTime()) {
                //如果第二个日期在第一个日期前面，将两个日期调换
                Date temp = new Date(date1.getTime());
                date1.setTime(date2.getTime());
                date2.setTime(temp.getTime());
            }

            startTime = DateUtil.getDateStartTime(date1);
            endTime = DateUtil.getDateEndTime(date2);
        }

        return new long[]{startTime, endTime};
    }

    private void showOverSixMonthDialog() {
        if (mDialogOverSixMonth == null) {
            mDialogOverSixMonth = DialogUtil.createNoNegativeDialog(mContext,
                    getString(R.string.over_six_month_message), getString(R.string.confirm));
        }

        mDialogOverSixMonth.show();
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
