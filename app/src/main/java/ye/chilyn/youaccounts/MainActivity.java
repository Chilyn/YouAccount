package ye.chilyn.youaccounts;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

    private TextView mTvTabKeepAccount, mTvTabMe;
    private FragmentTabManager mTabManager;
    private List<Fragment> mFragments = new ArrayList<>();
    private List<View> mTabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
        setListener();
    }

    private void initViews() {
        mTvTabKeepAccount = (TextView) findViewById(R.id.tv_tab1);
        mTvTabMe = (TextView) findViewById(R.id.tv_tab2);
        mTabs.add(mTvTabKeepAccount);
        mTabs.add(mTvTabMe);
    }

    private void initData() {
        mTvTabKeepAccount.setSelected(true);
        mFragments.add(new KeepAccountsFragment());
        mFragments.add(new MeFragment());
        mTabManager = new FragmentTabManager(this, mFragments, R.id.fl_fragments, mTabs);
    }

    private void setListener() {
        mTabManager.setOnTabClickListener(mTabClickListener);
    }

    private FragmentTabManager.OnTabClickListener mTabClickListener = new FragmentTabManager.OnTabClickListener() {
        @Override
        public void onClick(View tab) {
            switch (tab.getId()) {
                case R.id.tv_tab1:
                    mTvTabKeepAccount.setSelected(true);
                    mTvTabMe.setSelected(false);
                    break;

                case R.id.tv_tab2:
                    mTvTabKeepAccount.setSelected(false);
                    mTvTabMe.setSelected(true);
                    break;
            }
        }
    };
}
