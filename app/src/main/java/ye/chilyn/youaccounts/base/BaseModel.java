package ye.chilyn.youaccounts.base;

import ye.chilyn.youaccounts.base.interfaces.IBaseModel;

/**
 * Created by Alex on 2018/1/15.
 */

public abstract class BaseModel implements IBaseModel{

    private OnRefreshViewListener mRefreshViewListener;

    public BaseModel(OnRefreshViewListener listener) {
        setOnRefreshViewListener(listener);
    }

    @Override
    public void setOnRefreshViewListener(OnRefreshViewListener listener) {
        this.mRefreshViewListener = listener;
    }

    @Override
    public void callRefreshView(int type, Object data) {
        synchronized (this) {
            if (mRefreshViewListener != null) {
                mRefreshViewListener.onRefreshView(type, data);
            }
        }
    }

    @Override
    public void onDestroy() {
        synchronized (this) {
            mRefreshViewListener = null;
        }
    }
}
