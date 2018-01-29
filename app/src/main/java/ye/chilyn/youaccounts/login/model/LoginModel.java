package ye.chilyn.youaccounts.login.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ye.chilyn.youaccounts.base.BaseModel;
import ye.chilyn.youaccounts.contant.HandleModelType;
import ye.chilyn.youaccounts.contant.RefreshViewType;
import ye.chilyn.youaccounts.contant.SharePreferenceKey;
import ye.chilyn.youaccounts.entity.UserBean;
import ye.chilyn.youaccounts.model.UsersDao;
import ye.chilyn.youaccounts.util.SharePreferencesUtils;

/**
 * Created by Alex on 2018/1/29.
 */

public class LoginModel extends BaseModel {

    private ExecutorService mSqlTaskExecutor = Executors.newSingleThreadExecutor();
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
            int uid = mUsersDao.queryUserId(bean.getNickname());
            SharePreferencesUtils.save(SharePreferenceKey.IS_LOGINED, true);
            SharePreferencesUtils.save(SharePreferenceKey.USER_ID, uid);
            SharePreferencesUtils.save(SharePreferenceKey.NICKNAME, bean.getNickname());
            callRefreshView(RefreshViewType.LOGIN_SUCCESS, null);
        } else {
            callRefreshView(RefreshViewType.LOGIN_FAIL, null);
        }
    }
}
