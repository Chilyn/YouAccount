package ye.chilyn.youaccounts;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import ye.chilyn.youaccounts.contant.AppFilePath;
import ye.chilyn.youaccounts.contant.EventType;
import ye.chilyn.youaccounts.keepaccounts.fragment.KeepAccountsFragment;
import ye.chilyn.youaccounts.me.fragment.MeFragment;
import ye.chilyn.youaccounts.util.FragmentTabManager;

/**
 * 主页面Activity
 */
public class MainActivity extends AppCompatActivity {

    private FragmentTabManager mTabManager;
    private List<Fragment> mFragments = new ArrayList<>();
    private List<View> mTabs = new ArrayList<>();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            "android.permission.WRITE_EXTERNAL_STORAGE"
            ,"android.permission.READ_EXTERNAL_STORAGE"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        initViews();
        initData();
    }

    private void initViews() {
        mTabs.add(findViewById(R.id.tv_tab1));
        mTabs.add(findViewById(R.id.tv_tab2));
    }

    private void initData() {
        mFragments.add(new KeepAccountsFragment());
        mFragments.add(new MeFragment());
        mTabManager = new FragmentTabManager(this, mFragments, R.id.fl_fragments, mTabs);
    }

    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {
                AccountsApplication.setCanCreateFile(true);
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
                    AccountsApplication.setCanCreateFile(true);
                    //通知获取写入外部存储权限成功
                    EventBus.getDefault().post(EventType.WRITE_FILE_PERMISSION_GOTTEN);
                } else {
                    finish();
                }
                break;
        }
    }
}
