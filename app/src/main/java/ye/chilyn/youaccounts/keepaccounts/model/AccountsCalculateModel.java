package ye.chilyn.youaccounts.keepaccounts.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;

import ye.chilyn.youaccounts.base.BaseModel;
import ye.chilyn.youaccounts.contant.HandleModelType;
import ye.chilyn.youaccounts.contant.RefreshViewType;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;
import ye.chilyn.youaccounts.util.CacheExecutorHelper;

/**
 * Created by Alex on 2018/1/18.
 * 账目数据计算的Model
 */

public class AccountsCalculateModel extends BaseModel {

    private ExecutorService mCalculateExecutor = CacheExecutorHelper.getInstance().getCacheExecutor();

    public AccountsCalculateModel(OnRefreshViewListener listener) {
        super(listener);
    }

    @Override
    public void handleModelEvent(int type, Object data) {
        mCalculateExecutor.execute(new CalculateTask(type, data));
    }

    private class CalculateTask implements Runnable {

        private int mEventType;
        private Object mData;

        public CalculateTask(int mEventType, Object data) {
            this.mEventType = mEventType;
            this.mData = data;
        }

        @Override
        public void run() {
            switch (mEventType) {
                case HandleModelType.CALCULATE_TOTAL_ACCOUNTS:
                    calculateTotalAccounts((List<AccountsBean>) mData);
                    break;
            }
        }
    }

    /**
     * 计算账目总额
     * @param data
     */
    private void calculateTotalAccounts(List<AccountsBean> data) {
        BigDecimal total = new BigDecimal(Float.toString(0.0f));
        for (AccountsBean bean : data) {
            BigDecimal itemValue = new BigDecimal(Float.toString(bean.getMoney()));
            total = total.add(itemValue);
        }
        callRefreshView(RefreshViewType.SHOW_TOTAL_ACCOUNTS, total.floatValue());
    }
}
