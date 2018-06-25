package ye.chilyn.youaccount.me.model;

import java.util.concurrent.ExecutorService;

import ye.chilyn.youaccount.base.BaseModel;
import ye.chilyn.youaccount.constant.HandleModelType;
import ye.chilyn.youaccount.constant.RefreshViewType;
import ye.chilyn.youaccount.entity.UserBean;
import ye.chilyn.youaccount.sql.UsersDao;
import ye.chilyn.youaccount.util.CacheExecutorHelper;

/**
 * Created by Alex on 2018/1/29.
 */

public class ModifyModel extends BaseModel {

    private ExecutorService mSqlTaskExecutor = CacheExecutorHelper.getInstance().getCacheExecutor();
    private UsersDao mUsersDao = new UsersDao();

    public ModifyModel(OnRefreshViewListener listener) {
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
                case HandleModelType.MODIFY_USER_PASSWORD:
                    modifyUserPassword((UserBean) mData);
                    break;

                case HandleModelType.MODIFY_USER_NICKNAME:
                    modifyUserNickname((UserBean) mData);
                    break;

            }
        }
    }

    private void modifyUserPassword(UserBean bean) {
        boolean isSuccess = mUsersDao.updateUserPassword(bean);
        if (isSuccess) {
            callRefreshView(RefreshViewType.MODIFY_PASSWORD_SUCCESS, bean);
        } else {
            callRefreshView(RefreshViewType.MODIFY_PASSWORD_FAIL, null);
        }
    }

    private void modifyUserNickname(UserBean bean) {
        boolean isSuccess = mUsersDao.updateUserNickname(bean);
        if (isSuccess) {
            callRefreshView(RefreshViewType.MODIFY_NICKNAME_SUCCESS, bean);
        } else {
            callRefreshView(RefreshViewType.MODIFY_NICKNAME_FAIL, null);
        }
    }
}
