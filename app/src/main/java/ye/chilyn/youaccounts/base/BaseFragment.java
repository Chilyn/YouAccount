package ye.chilyn.youaccounts.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Alex on 2018/1/18.
 */

public abstract class BaseFragment extends Fragment {

    protected View mRootView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRootView = view;
    }

    @Override
    public void onDestroyView() {
        destroyViews();
        releaseModels();
        super.onDestroyView();
    }

    protected abstract void destroyViews();
    protected abstract void releaseModels();

    public <T extends View> T findView(int id) {
        return (T) mRootView.findViewById(id);
    }
}
