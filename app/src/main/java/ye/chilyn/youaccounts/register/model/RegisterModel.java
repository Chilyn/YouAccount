package ye.chilyn.youaccounts.register.model;

import java.util.concurrent.ExecutorService;

import ye.chilyn.youaccounts.base.BaseModel;
import ye.chilyn.youaccounts.constant.HandleModelType;
import ye.chilyn.youaccounts.constant.RefreshViewType;
import ye.chilyn.youaccounts.entity.UserBean;
import ye.chilyn.youaccounts.model.UsersDao;
import ye.chilyn.youaccounts.util.CacheExecutorHelper;

/**
 * Created by Alex on 2018/1/29.
 */

public class RegisterModel extends BaseModel {

    private ExecutorService mSqlTaskExecutor = CacheExecutorHelper.getInstance().getCacheExecutor();
    private UsersDao mUsersDao = new UsersDao();

    public RegisterModel(OnRefreshViewListener listener) {
        super(listener);
    }

    @Override
    public void handleModelEvent(int type, Object data) {
        mSqlTaskExecutor.execute(new RegisterTask(type, data));
    }

    private class RegisterTask implements Runnable {

        private int mEventType;
        private Object mData;

        public RegisterTask(int mEventType, Object data) {
            this.mEventType = mEventType;
            this.mData = data;
        }

        @Override
        public void run() {
            switch (mEventType) {
                case HandleModelType.REGISTER_USER:
                    registerUser((UserBean) mData);
                    break;
            }
        }
    }

    private void registerUser(UserBean bean) {
        if (mUsersDao.isUserExisted(bean)) {
            callRefreshView(RefreshViewType.USER_HAS_REGISTERED_BEFORE, null);
            return;
        }

        boolean isSuccess = mUsersDao.insertUsers(bean);
        if (isSuccess) {
            callRefreshView(RefreshViewType.REGISTER_USER_SUCCESS, bean);
        } else {
            callRefreshView(RefreshViewType.REGISTER_USER_FAIL, bean);
        }
    }
}
