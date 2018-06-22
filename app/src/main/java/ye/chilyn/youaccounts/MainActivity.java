package ye.chilyn.youaccounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ye.chilyn.youaccounts.base.BaseActivity;
import ye.chilyn.youaccounts.keepaccounts.fragment.KeepAccountsFragment;
import ye.chilyn.youaccounts.me.fragment.MeFragment;
import ye.chilyn.youaccounts.util.FragmentTabManager;

/**
 * 主页面Activity
 */
public class MainActivity extends BaseActivity {

    private List<Fragment> mFragments = new ArrayList<>();
    private List<View> mTabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
    }

    private void initViews() {
        mTabs.add(findViewById(R.id.tab1));
        mTabs.add(findViewById(R.id.tab2));
    }

    private void initData() {
        mFragments.add(new KeepAccountsFragment());
        mFragments.add(new MeFragment());
        FragmentTabManager manager = new FragmentTabManager(this, mFragments, R.id.fl_fragments, mTabs);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按回退键变成home键效果
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void destroyViews() {
    }

    @Override
    protected void releaseModels() {
    }
}
