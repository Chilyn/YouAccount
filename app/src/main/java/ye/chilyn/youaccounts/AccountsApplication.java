package ye.chilyn.youaccounts;

import android.app.Application;
import android.content.Context;

import ye.chilyn.youaccounts.constant.SharePreferenceKey;
import ye.chilyn.youaccounts.entity.UserBean;
import ye.chilyn.youaccounts.util.SharePreferencesUtils;

/**
 * Created by Alex on 2018/1/16.
 */

public class AccountsApplication extends Application {

    private static Context mContext;
    private static UserBean mLoginUserBean;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = getApplicationContext();
        SharePreferencesUtils.init(getAppContext());
        initLoginUserInfo();
    }

    /**
     * 初始化已登录用户信息
     */
    private void initLoginUserInfo() {
        boolean isLogined = SharePreferencesUtils.getBooleanValue(SharePreferenceKey.IS_LOGINED);
        if (isLogined) {
            int userId = SharePreferencesUtils.getIntValue(SharePreferenceKey.USER_ID);
            String nickname = SharePreferencesUtils.getStringValue(SharePreferenceKey.NICKNAME);
            String password = SharePreferencesUtils.getStringValue(SharePreferenceKey.PASSWORD);
            mLoginUserBean = new UserBean(userId, nickname, password);
        }
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static void setLoginUserInfo (UserBean bean) {
        mLoginUserBean = bean;
    }

    public static UserBean getLoginUserInfo() {
        if (mLoginUserBean == null) {
            return null;
        }

        return mLoginUserBean.copy();
    }
}
