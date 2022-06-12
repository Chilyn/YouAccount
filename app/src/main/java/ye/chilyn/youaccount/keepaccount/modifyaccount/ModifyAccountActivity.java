package ye.chilyn.youaccount.keepaccount.modifyaccount;

import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ypy.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.base.BaseActivity;
import ye.chilyn.youaccount.base.common.BaseStaticInnerHandler;
import ye.chilyn.youaccount.base.interfaces.IBaseModel;
import ye.chilyn.youaccount.constant.EventType;
import ye.chilyn.youaccount.constant.HandleModelType;
import ye.chilyn.youaccount.constant.RefreshViewType;
import ye.chilyn.youaccount.keepaccount.constant.ExtraKey;
import ye.chilyn.youaccount.keepaccount.entity.AccountBean;
import ye.chilyn.youaccount.keepaccount.interfaces.OnBillTypeSelectedListener;
import ye.chilyn.youaccount.keepaccount.model.KeepAccountSqlModel;
import ye.chilyn.youaccount.keepaccount.view.BillTypeWindowView;
import ye.chilyn.youaccount.util.SoftKeyboardUtil;
import ye.chilyn.youaccount.util.ToastUtil;
import ye.chilyn.youaccount.view.TitleBarView;
import ye.chilyn.youaccount.widget.pickers.DateTimePicker;

/**
 * 修改账目的Activity
 */
public class ModifyAccountActivity extends BaseActivity implements View.OnClickListener {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private IBaseModel mKeepAccountsSqlModel;
    private EditText mEtMoney;
    private EditText mEtBillType;
    private TextView mTvTime;
    private EditText mEtTime;
    private TextView mTvModify;
    private AccountBean mModifyBean;
    private BillTypeWindowView mBillTypeWindowView;
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
        mEtBillType = findView(R.id.et_bill_type);
        mTvTime = findView(R.id.tv_time);
        mTvModify = findView(R.id.tv_modify);
        mBillTypeWindowView = new BillTypeWindowView(mEtBillType, mBillTypeSelectedListener);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.modify_account));
        mKeepAccountsSqlModel = new KeepAccountSqlModel(mRefreshViewListener);
        mModifyBean = (AccountBean) getIntent().getSerializableExtra(ExtraKey.ACCOUNT_BEAN);
        if (mModifyBean != null) {
            mEtMoney.setText(mModifyBean.getMoney() + "");
            mEtMoney.setSelection(mEtMoney.getText().length());
            mEtBillType.setText(mModifyBean.getBillType());
            mTvTime.setText(mModifyBean.getTime());
        }
    }

    private void setListener() {
        mTvTime.setOnClickListener(this);
        mTvModify.setOnClickListener(this);
        findView(R.id.tv_choose_type).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_time:
                modifyTime();
                break;

            case R.id.tv_modify:
                modifyAccount();
                break;

            case R.id.tv_choose_type:
                mBillTypeWindowView.show();
                break;
        }
    }

    private void modifyTime() {
        Calendar calendar = Calendar.getInstance();
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);

        Date selectedDate = null;
        try {
            selectedDate = dateFormat.parse(mTvTime.getText().toString());
        } catch (ParseException e) {
        }

        if (selectedDate != null) {
            calendar.setTime(selectedDate);
        } else {
            calendar.setTimeInMillis(mModifyBean.getTimeMill());
        }
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH) + 1;
        int selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        int selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        int selectedMinute = calendar.get(Calendar.MINUTE);

        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.YEAR_MONTH_DAY, DateTimePicker.HOUR_24);
        picker.setDateRangeStart(2018, 1, 1);
        picker.setDateRangeEnd(endYear, endMonth, endDay);
        picker.setTimeRangeStart(0, 0);
        picker.setTimeRangeEnd(23, 59);
        picker.setWeightEnable(true);
        picker.setWheelModeEnable(true);
        picker.setCanLoop(false);
        picker.setTitleText(R.string.modify_time);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                StringBuilder sb = new StringBuilder();
                sb.append(year)
                    .append("-")
                    .append(month)
                    .append("-")
                    .append(day)
                    .append(" ")
                    .append(hour)
                    .append(":")
                    .append(minute);
                mTvTime.setText(sb.toString());
            }
        });
        picker.setSelectedItem(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
        picker.show();
    }

    /**
     * 修改账目
     */
    private void modifyAccount() {
        String billType = mEtBillType.getText().toString();
        if (TextUtils.isEmpty(billType)) {
            ToastUtil.showShortToast(AccountApplication.getAppContext().getString(R.string.data_invalid));
            return;
        }

        String moneyStr = mEtMoney.getText().toString();
        String timeStr = mTvTime.getText().toString();
        try {
            Date modifyDate = dateFormat.parse(timeStr);
            // 修改时间的秒数随机，防止时间戳相同
            long randomMillTime = modifyDate.getTime() + new Random().nextInt(60000);
            modifyDate.setTime(randomMillTime);
            float money = Float.valueOf(moneyStr);
            mModifyBean.setMoney(money);
            mModifyBean.setBillType(billType);
            mModifyBean.setTime(timeStr);
            mModifyBean.setUpdateTimeMill(modifyDate.getTime());
            mKeepAccountsSqlModel.handleModelEvent(HandleModelType.UPDATE_ACCOUNTS, mModifyBean);
        } catch (NumberFormatException e) {
            mEtMoney.setText(null);
            ToastUtil.showShortToast(AccountApplication.getAppContext().getString(R.string.data_invalid));
        } catch (ParseException e) {
            ToastUtil.showShortToast(getString(R.string.modify_time_invalid));
        } finally {
            SoftKeyboardUtil.forceCloseSoftKeyboard(mEtMoney);
        }
    }

    private OnBillTypeSelectedListener mBillTypeSelectedListener = new OnBillTypeSelectedListener() {
        @Override
        public void onItemSelected(String billType) {
            mEtBillType.setText(billType);
            mEtBillType.setSelection(billType.length());
        }

        @Override
        public void onItemDelete(String billType) {

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
