package ye.chilyn.youaccount;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import ye.chilyn.youaccount.base.BaseActivity;
import ye.chilyn.youaccount.constant.AppFilePath;
import ye.chilyn.youaccount.constant.SharePreferenceKey;
import ye.chilyn.youaccount.entity.UserBean;
import ye.chilyn.youaccount.login.LoginActivity;
import ye.chilyn.youaccount.sql.UsersDao;
import ye.chilyn.youaccount.util.SharePreferencesUtils;

public class WelcomeActivity extends BaseActivity {

    private static final int LOGIN_OR_TO_MAIN = 0;
    private static final long WAITING_MILLIS = 300L;
    private boolean mIsLogined = false;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            "android.permission.WRITE_EXTERNAL_STORAGE"
            ,"android.permission.READ_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ye.chilyn.youaccount.R.layout.activity_welcome);
        mIsLogined = SharePreferencesUtils.getBooleanValue(SharePreferenceKey.IS_LOGINED);
        verifyStoragePermissions(this);
    }

    @Override
    protected boolean isDarkFontMode() {
        return true;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_OR_TO_MAIN:
                    loginOrToMain();
                    break;
            }
        }
    };

    /**
     * 跳转登录页面或者主页面
     */
    private void loginOrToMain() {
        //登录状态为未登录，直接跳登录页面
        if (!mIsLogined) {
            toActivity(LoginActivity.class);
            return;
        }

        //登录状态为已登录，判断用户是否存在，不存在跳登录页面，否则直接进主页
        if (!isUserExisted()) {
            resetUserData();
            toActivity(LoginActivity.class);
        } else {
            toActivity(MainActivity.class);
        }
    }

    private void toActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

    private boolean isUserExisted() {
        UsersDao usersDao = new UsersDao();
        String nickname = SharePreferencesUtils.getStringValue(SharePreferenceKey.NICKNAME);
        UserBean userBean = new UserBean(0, nickname, null);
        return usersDao.isUserExisted(userBean);
    }

    private void resetUserData() {
        SharePreferencesUtils.save(SharePreferenceKey.IS_LOGINED, false);
        SharePreferencesUtils.save(SharePreferenceKey.USER_ID, -1);
        SharePreferencesUtils.save(SharePreferenceKey.NICKNAME, "");
        SharePreferencesUtils.save(SharePreferenceKey.PASSWORD, "");
    }

    public void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {
                //创建app相关存储目录
                AppFilePath.createAppDirectories();
                mHandler.sendEmptyMessageDelayed(LOGIN_OR_TO_MAIN, WAITING_MILLIS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //创建app相关存储目录
                    AppFilePath.createAppDirectories();
                    mHandler.sendEmptyMessageDelayed(LOGIN_OR_TO_MAIN, WAITING_MILLIS);
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    protected void destroyViews() {
    }

    @Override
    protected void releaseModels() {
    }
}
