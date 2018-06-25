package ye.chilyn.youaccount.keepaccount.queryaccount.view;

import android.app.AlertDialog;
import android.os.Message;
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

import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccount.constant.EventType;
import ye.chilyn.youaccount.constant.HandleModelType;
import ye.chilyn.youaccount.constant.RefreshViewType;
import ye.chilyn.youaccount.keepaccount.entity.AccountBean;
import ye.chilyn.youaccount.keepaccount.entity.QueryAccountParameter;
import ye.chilyn.youaccount.keepaccount.view.BaseAccountView;
import ye.chilyn.youaccount.keepaccount.view.ProgressDialogView;
import ye.chilyn.youaccount.util.DateUtil;
import ye.chilyn.youaccount.util.DialogUtil;
import ye.chilyn.youaccount.util.ToastUtil;
import ye.chilyn.youaccount.widget.pickers.DateTimePicker;

/**
 * Created by Alex on 2018/1/22.
 * 账目查询页面的View层
 */

public class QueryAccountView extends BaseAccountView implements View.OnClickListener {

    /**年月模式*/
    private static final int YEAR_MONTH = DateTimePicker.YEAR_MONTH;
    /**年月日模式*/
    private static final int YEAR_MONTH_DAY = DateTimePicker.YEAR_MONTH_DAY;
    private static final int START_TIME = 0, END_TIME = 1;
    private static final int DATE1 = 0, DATE2 = 1;
    private TextView mTvChooseMonthOrDate;
    private RelativeLayout mRlMonth, mRlDate;
    private TextView mTvDate1, mBottomLineDate1, mTvTo, mTvDate2, mBottomLineDate2;
    private TextView mTvAccountsRange, mTvMonth;
    private TextView mTvQuery;
    private TextView mTvTotalMoney;
    private NumberFormat mNumberFormat;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mMonthFormat = new SimpleDateFormat("yyyy-MM");
    private DateTimePicker mPicker;
    private int mCurrentChooseMode = DateTimePicker.YEAR_MONTH;
    private int mCurrentSelectingDate = DATE1;
    private AlertDialog mDialogOverSixMonth;
    private ProgressDialogView mProgressDialogView;

    public QueryAccountView(View rootView, OnHandleModelListener listener) {
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
        mBottomLineDate1 = findView(R.id.bottom_line_date1);
        mTvTo = findView(R.id.tv_to);
        mTvDate2 = findView(R.id.tv_date2);
        mBottomLineDate2 = findView(R.id.bottom_line_date2);
        mTvQuery = findView(R.id.tv_query);
        mTvTotalMoney = findView(R.id.tv_total_money);
        mProgressDialogView = new ProgressDialogView(mContext, getString(R.string.querying));
        mRlDate.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        mTvMonth.setText(mMonthFormat.format(new Date()));
        mTvDate1.setText(mDateFormat.format(new Date()));
        setSelectedDateViews();
        mNumberFormat = NumberFormat.getCurrencyInstance();
        mNumberFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    /**
     * 设置选中的日期
     */
    private void setSelectedDateViews() {
        boolean isSelectingDate1 = true;
        if (mCurrentSelectingDate != DATE1) {
            isSelectingDate1 = false;
        }

        mTvDate1.setSelected(isSelectingDate1);
        mBottomLineDate1.setSelected(isSelectingDate1);
        mTvDate2.setSelected(!isSelectingDate1);
        mBottomLineDate2.setSelected(!isSelectingDate1);
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
                setSelectedDateViews();
                showPicker(getString(R.string.choose_start_date));
                break;

            case R.id.tv_date2:
                mCurrentSelectingDate = DATE2;
                setSelectedDateViews();
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
            mRlMonth.setVisibility(View.INVISIBLE);
            mRlDate.setVisibility(View.VISIBLE);
            return;
        }

        if (mCurrentChooseMode == YEAR_MONTH_DAY) {
            mCurrentChooseMode = YEAR_MONTH;
            mTvChooseMonthOrDate.setText(getString(R.string.choose_month));
            mRlMonth.setVisibility(View.VISIBLE);
            mRlDate.setVisibility(View.INVISIBLE);
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
        Date lastChooseDate = getLastChooseDate();
        //如果获取到的上次选择日期不为空则设置上次选择过的日期
        if (lastChooseDate != null) {
            calendar.setTime(lastChooseDate);
            selectedYear = calendar.get(Calendar.YEAR);
            selectedMonth = calendar.get(Calendar.MONTH) + 1;
            selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        }

        mPicker = new DateTimePicker(mContext, mCurrentChooseMode, DateTimePicker.NONE);
        mPicker.setDateRangeStart(2018, 1, 1);
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

    /**
     * 获取目前查询模式下上次选中的日期
     * @return
     */
    private Date getLastChooseDate() {
        Date lastChooseDate = null;
        //如果是按月份选择的模式，弹窗日期默认选中上次选中的月份
        if (mCurrentChooseMode == YEAR_MONTH) {
            try {
                lastChooseDate = mMonthFormat.parse(mTvMonth.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //如果是按日期选择的模式，弹窗日期默认选中上次选中的日期
        if (mCurrentChooseMode == YEAR_MONTH_DAY && mCurrentSelectingDate == DATE1) {
            try {
                lastChooseDate = mDateFormat.parse(mTvDate1.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (mCurrentChooseMode == YEAR_MONTH_DAY && mCurrentSelectingDate == DATE2) {
            try {
                lastChooseDate = mDateFormat.parse(mTvDate2.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return lastChooseDate;
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
                        new QueryAccountParameter(mUserId, DateUtil.getMonthStartTime(chooseDate), DateUtil.getMonthEndTime(chooseDate)));
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
                    new QueryAccountParameter(mUserId, DateUtil.getDateStartTime(date1), DateUtil.getDateEndTime(date2)));
            return;
        }
    }

    @Override
    public void refreshViews(int refreshType, Object data) {
        mHandler.sendMessage(mHandler.obtainMessage(refreshType, data));
    }

    private ViewHandler mHandler = new ViewHandler(this);

    private static class ViewHandler extends BaseStaticInnerHandler<QueryAccountView> {

        public ViewHandler(QueryAccountView view) {
            super(view);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isReferenceRecycled()) {
                removeCallbacksAndMessages(null);
                return;
            }

            QueryAccountView view = getReference();
            switch (msg.what) {
                case RefreshViewType.SHOW_PROGRESS_DIALOG:
                    view.mProgressDialogView.showProgressDialog();
                    break;

                case RefreshViewType.QUERY_ACCOUNTS_SUCCESS:
                    view.onQueryAccountsSuccess((List<AccountBean>) msg.obj);
                    view.mProgressDialogView.dismissProgressDialog();
                    break;

                case RefreshViewType.DELETE_ACCOUNT_SUCCESS:
                    view.onDeleteAccountSuccess((AccountBean) msg.obj);
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
    protected void onDeleteAccountSuccess(AccountBean bean) {
        ToastUtil.showShortToast(getString(R.string.delete_success));
        long[] queryRangeTimeMill = getQueryRangeTimeMill();
        callHandleModel(HandleModelType.QUERY_ACCOUNTS,
                new QueryAccountParameter(mUserId, queryRangeTimeMill[START_TIME], queryRangeTimeMill[END_TIME]));
        EventBus.getDefault().post(EventType.QUERY_ACCOUNTS_AFTER_DELETE);
    }

    @Override
    public void onEvent(Integer eventType) {
        if (eventType == EventType.QUERY_ACCOUNTS_AFTER_UPDATE) {
            long[] queryRangeTimeMill = getQueryRangeTimeMill();
            callHandleModel(HandleModelType.QUERY_ACCOUNTS,
                    new QueryAccountParameter(mUserId, queryRangeTimeMill[START_TIME], queryRangeTimeMill[END_TIME]));
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
