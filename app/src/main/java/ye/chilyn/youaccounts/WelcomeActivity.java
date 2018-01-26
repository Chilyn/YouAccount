package ye.chilyn.youaccounts;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import ye.chilyn.youaccounts.base.BaseActivity;
import ye.chilyn.youaccounts.contant.AppFilePath;
import ye.chilyn.youaccounts.contant.SharePreferenceKey;
import ye.chilyn.youaccounts.login.LoginActivity;
import ye.chilyn.youaccounts.util.SharePreferencesUtils;

public class WelcomeActivity extends BaseActivity {

    private static final int LOGIN_OR_TO_MAIN = 0;
    private boolean mIsLogined = false;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            "android.permission.WRITE_EXTERNAL_STORAGE"
            ,"android.permission.READ_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mIsLogined = SharePreferencesUtils.getBooleanValue(SharePreferenceKey.IS_LOGINED);
        verifyStoragePermissions(this);
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

    private void loginOrToMain() {
        Intent intent;
        if (mIsLogined) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
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
                mHandler.sendEmptyMessageDelayed(LOGIN_OR_TO_MAIN, 2000);
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
                    mHandler.sendEmptyMessageDelayed(LOGIN_OR_TO_MAIN, 2000);
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
