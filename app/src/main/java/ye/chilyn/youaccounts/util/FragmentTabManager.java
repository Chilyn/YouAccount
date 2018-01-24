package ye.chilyn.youaccounts.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.util.List;

/**
 * Fragment切换的管理类
 */

public class FragmentTabManager implements View.OnClickListener{
    private List<Fragment> mFragments; // 一个tab页面对应一个Fragment
    private List<View> mTabs; // 一个tab页面对应的tab集合
    private FragmentActivity mFragmentActivity; // Fragment所属的Activity
    private int mFragmentContentId; // Activity中所要被替换的区域的id
    private int mCurrentTab; // 当前Tab页面索引
    private OnTabClickListener mTabClickListener;

    public FragmentTabManager(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId, List<View> tabs) {
        this.mFragments = fragments;
        this.mTabs = tabs;
        this.mFragmentActivity = fragmentActivity;
        this.mFragmentContentId = fragmentContentId;

        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        ft.commit();

        for (int i = 0; i < tabs.size(); i++) {
            if (i == 0) {
                tabs.get(i).setSelected(true);
            }
            tabs.get(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        for(int i = 0; i < mTabs.size(); i++){
            if(mTabs.get(i).getId() == view.getId()){
                Fragment fragment = mFragments.get(i);
                FragmentTransaction ft = obtainFragmentTransaction(i);

                getCurrentFragment().onPause(); // 暂停当前tab
//                getCurrentFragment().onStop(); // 暂停当前tab

                if(fragment.isAdded()){
//                    fragment.onStart(); // 启动目标tab的onStart()
                    fragment.onResume(); // 启动目标tab的onResume()
                }else{
                    ft.add(mFragmentContentId, fragment);
                }
                showTab(i); // 显示目标tab
                ft.commit();
                mTabs.get(i).setSelected(true);
            } else {
                mTabs.get(i).setSelected(false);
            }
        }

        if (mTabClickListener != null) {
            mTabClickListener.onClick(view);
        }
    }

    /**
     * 切换tab
     * @param idx
     */
    private void showTab(int idx){
        for(int i = 0; i < mFragments.size(); i++){
            Fragment fragment = mFragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);

            if(idx == i){
                ft.show(fragment);
            }else{
                ft.hide(fragment);
            }
            ft.commit();
        }
        mCurrentTab = idx; // 更新目标tab为当前tab
    }

    /**
     * 获取一个FragmentTransaction
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index){
        FragmentTransaction ft = mFragmentActivity.getSupportFragmentManager().beginTransaction();
        return ft;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public Fragment getCurrentFragment(){
        return mFragments.get(mCurrentTab);
    }

    public void setOnTabClickListener(OnTabClickListener listener) {
        mTabClickListener = listener;
    }

    public interface OnTabClickListener {
        void onClick(View tab);
    }
}
