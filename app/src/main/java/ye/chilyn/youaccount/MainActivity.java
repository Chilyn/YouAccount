package ye.chilyn.youaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ye.chilyn.youaccount.base.BaseActivity;
import ye.chilyn.youaccount.keepaccount.fragment.KeepAccountFragment;
import ye.chilyn.youaccount.me.fragment.MeFragment;
import ye.chilyn.youaccount.util.FragmentTabManager;

/**
 * 主页面Activity
 */
public class MainActivity extends BaseActivity {

    private List<Fragment> mFragments = new ArrayList<>();
    private List<View> mTabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ye.chilyn.youaccount.R.layout.activity_main);
        initViews();
        initData();
    }

    private void initViews() {
        mTabs.add(findViewById(ye.chilyn.youaccount.R.id.tab1));
        mTabs.add(findViewById(ye.chilyn.youaccount.R.id.tab2));
    }

    private void initData() {
        mFragments.add(new KeepAccountFragment());
        mFragments.add(new MeFragment());
        FragmentTabManager manager = new FragmentTabManager(this, mFragments, ye.chilyn.youaccount.R.id.fl_fragments, mTabs);
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
