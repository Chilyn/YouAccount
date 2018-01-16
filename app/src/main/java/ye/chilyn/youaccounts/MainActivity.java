package ye.chilyn.youaccounts;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ye.chilyn.youaccounts.keepaccounts.fragment.KeepAccountsFragment;
import ye.chilyn.youaccounts.me.fragment.MeFragment;
import ye.chilyn.youaccounts.util.FragmentTabManager;

public class MainActivity extends AppCompatActivity {

    private FragmentTabManager mTabManager;
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
        mTabs.add(findViewById(R.id.tv_tab1));
        mTabs.add(findViewById(R.id.tv_tab2));
    }

    private void initData() {
        mFragments.add(new KeepAccountsFragment());
        mFragments.add(new MeFragment());
        mTabManager = new FragmentTabManager(this, mFragments, R.id.fl_fragments, mTabs);
    }
}
