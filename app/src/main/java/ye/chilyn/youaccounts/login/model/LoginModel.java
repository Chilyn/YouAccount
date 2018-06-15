package ye.chilyn.youaccounts.login.model;

import java.util.concurrent.ExecutorService;

import ye.chilyn.youaccounts.base.BaseModel;
import ye.chilyn.youaccounts.constant.HandleModelType;
import ye.chilyn.youaccounts.constant.RefreshViewType;
import ye.chilyn.youaccounts.entity.UserBean;
import ye.chilyn.youaccounts.sql.UsersDao;
import ye.chilyn.youaccounts.util.CacheExecutorHelper;

/**
 * Created by Alex on 2018/1/29.
 */

public class LoginModel extends BaseModel {

    private ExecutorService mSqlTaskExecutor = CacheExecutorHelper.getInstance().getCacheExecutor();
    private UsersDao mUsersDao = new UsersDao();

    public LoginModel(OnRefreshViewListener listener) {
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
                case HandleModelType.USER_LOGIN:
                    login((UserBean) mData);
                    break;
            }
        }
    }

    private void login(UserBean bean) {
        if (!mUsersDao.isUserExisted(bean)) {
            callRefreshView(RefreshViewType.USER_NOT_EXIST, null);
            return;
        }

        boolean isSuccess = mUsersDao.matchUser(bean);
        if (isSuccess) {
            //根据插入的用户昵称查找出用户自增长ID
            int uid = mUsersDao.queryUserId(bean.getNickname());
            bean.setUserId(uid);
            callRefreshView(RefreshViewType.LOGIN_SUCCESS, bean);
        } else {
            callRefreshView(RefreshViewType.LOGIN_FAIL, null);
        }
    }
}
